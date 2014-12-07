
package net.borkert.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BeanToTextTableConverter {

  private final Set<String> ignoredProperties = new HashSet<>();
  private final Map<Class, String> classNamesMap = new HashMap<>();
  private String dateFormat = "yyyy.MM.dd HH:mm:ss.SSS";

  public BeanToTextTableConverter() {
  }

  public BeanToTextTableConverter(Collection<String> ignoredProperties) {
    this.ignoredProperties.addAll(ignoredProperties);
  }

  public BeanToTextTableConverter(String[] ignoredProperties) {
    this.ignoredProperties.addAll(Arrays.asList(ignoredProperties));
  }

  public TextTable createTextTable(Object bean)
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    List<BeanProperty> data = new ArrayList<>();
    TextTable table = new TextTable();
    table.setTableData(data);
    table.addColumnConfiguration("property", "Property", 100);
    table.addColumnConfiguration("value", "Value", 150, TextTable.ALIGN_LEFT, getDateFormat());

    if (bean != null) {
      PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(bean);
      Map<String, PropertyDescriptor> pdMap = new HashMap<>();
      for (PropertyDescriptor pd : properties) {
        String pName = pd.getName();
        if (pName.equalsIgnoreCase("class")) continue;
        if (ignoredProperties.contains(pName)) continue;
        pdMap.put(pName, pd);
      }
      List<String> pNames = new ArrayList<>(pdMap.keySet());
      if (pNames.contains("id")) {
        pNames.remove("id");
        data.add(new BeanProperty("Id", BeanUtils.getProperty(bean, "id")));
      }
      if (pNames.contains("version")) {
        pNames.remove("version");
        data.add(new BeanProperty("Version", BeanUtils.getProperty(bean, "version")));
      }
      if (pNames.contains("name")) {
        pNames.remove("name");
        data.add(new BeanProperty("Name", BeanUtils.getProperty(bean, "name")));
      }
      if (pNames.contains("enabled")) {
        pNames.remove("enabled");
        data.add(new BeanProperty("Enabled", BeanUtils.getProperty(bean, "enabled")));
      }
      Collections.sort(pNames);
      for (String pName : pNames) {
        String pDName = removeUnwantedCharacters(firstCharToUpper(pName));
        Object propertyValue = PropertyUtils.getProperty(bean, pName);
        if (propertyValue != null && hasMethod(propertyValue, "getId")) {
          String displayName = getClassNamesMap().containsKey(propertyValue.getClass().getName()) ? getClassNamesMap().get(propertyValue.getClass().getName()) : propertyValue.getClass().getName();
          data.add(new BeanProperty("Reference: " + displayName, BeanUtils.getProperty(propertyValue, "id")));
        } else {
          data.add(new BeanProperty(pDName, PropertyUtils.getProperty(bean, pName)));
        }
      }
    }
    return table;
  }

  private boolean hasMethod(Object object, String method) {
    if (object.getClass().isPrimitive()) return false;
    for (Method c : object.getClass().getMethods()) {
      if (c.getName().equals(method)) {
        return true;
      }
    }
    return false;
  }

  private String firstCharToUpper(String s) {
    final StringBuilder result = new StringBuilder(s.length());
    String[] words = s.split("\\s");
    for (int i = 0, l = words.length; i < l; ++i) {
      if (i > 0) result.append(" ");
      result.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));
    }
    return result.toString();
  }

  private String removeUnwantedCharacters(String value){
    if(value==null){
      return null;
    }
    return value.replace((char)0, '?').trim();
  }

  public String getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  public Map<Class, String> getClassNamesMap() {
    return classNamesMap;
  }

  public static class BeanProperty {

    private String property;
    private Object value;

    public BeanProperty(String property, Object value) {
      this.property = property;
      this.value = value;
    }

    public String getProperty() {
      return property;
    }

    public void setProperty(String property) {
      this.property = property;
    }

    public Object getValue() {
      return value;
    }

    public void setValue(Object value) {
      this.value = value;
    }
  }
}
