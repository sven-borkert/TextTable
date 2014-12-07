
package net.borkert.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextTable {

  public static final int ALIGN_LEFT = 0;
  public static final int ALIGN_RIGHT = 1;
  public static final int ALIGN_CENTER = 2;

  private List tableData;
  private List<ColumnConfiguration> tableColumns = new ArrayList<>();
  private String lineSeparator = System.getProperty("line.separator");

  public void printTable(OutputStream outputStream) throws IOException {

    OutputStreamWriter writer = new OutputStreamWriter(outputStream);

    // Analyse the width of the column data
    analyseData();
    int tableWidth = 0;
    // Calculate width of table.
    for (ColumnConfiguration c : getTableColumns()) {
      // Each row with 1 blank before and after the value
      tableWidth += c.getWidth() + 2;
    }
    // Add space for the lines at beginning and end of the table and between the rows
    tableWidth += getTableColumns().size() + 1;
    // Line buffer
    char[] line = new char[tableWidth];
    int linePos = 0;
    // Print top line
    for (int i = 0; i < tableWidth; i++) {
      line[i] = '-';
    }
    writer.write(line);
    writer.write(getLineSeparator());

    // Print the table header
    // Begin of table
    line[linePos++] = '|';
    line[linePos++] = ' ';
    for (ColumnConfiguration c : getTableColumns()) {
      int offset = (c.getWidth() - c.getHeadline().length()) / 2;
      // Fill whitespace before value
      for (int i = 0; i < offset; i++) {
        line[linePos++] = ' ';
      }
      // Write headline
      System.arraycopy(c.getHeadline().toCharArray(), 0, line, linePos, c.getHeadline().length());
      linePos += c.getHeadline().length();
      // Fill whitespace after value
      for (int i = 0; i < c.getWidth() - c.getHeadline().length() - offset; i++) {
        line[linePos++] = ' ';
      }
      // End of each column
      line[linePos++] = ' ';
      line[linePos++] = '|';
      if (linePos + 1 < line.length) {
        line[linePos++] = ' ';
      }
    }
    writer.write(line);
    writer.write(getLineSeparator());

    // Print line
    for (int i = 0; i < tableWidth; i++) {
      line[i] = '-';
    }
    writer.write(line);
    writer.write(getLineSeparator());

    // Iterate table rows
    for (Object o : getTableData()) {
      linePos = 0;
      // Begin of row
      line[linePos++] = '|';
      line[linePos++] = ' ';
      for (ColumnConfiguration c : getTableColumns()) {
        Object result;
        if (c.getProperty() == null) {
          result = o.toString();
        } else {
          result = getValueFromObject(o, c);
        }
        String data = result != null ? result.toString() : "";

        int offset = 0; // ALIGN_LEFT
        if (c.getLayout() == ALIGN_CENTER) {
          offset = (c.getWidth() - data.length()) / 2;
        }
        if (c.getLayout() == ALIGN_RIGHT) {
          offset = (c.getWidth() - data.length());
        }

        // Fill whitespace before value
        for (int i = 0; i < offset; i++) {
          line[linePos++] = ' ';
        }
        int charsToWrite = data.length() > c.maxWidth ? c.maxWidth : data.length();
        System.arraycopy(data.toCharArray(), 0, line, linePos, charsToWrite);
        linePos += charsToWrite;
        // Fill whitespace after value
        for (int i = 0; i < c.getWidth() - data.length() - offset; i++) {
          line[linePos++] = ' ';
        }
        // End of each column
        line[linePos++] = ' ';
        line[linePos++] = '|';
        if (linePos + 1 < line.length) {
          line[linePos++] = ' ';
        }
      }
      writer.write(line);
      writer.write(getLineSeparator());
    }
    // Print line
    for (int i = 0; i < tableWidth; i++) {
      line[i] = '-';
    }
    writer.write(line);
    writer.write(getLineSeparator());
    writer.flush();
  }

  public void addColumnConfiguration(String propertyName, String headline, int maxWidth) {
    getTableColumns().add(new ColumnConfiguration(propertyName, headline, maxWidth));
  }

  public void addColumnConfiguration(String propertyName, String headline, int maxWidth, int align) {
    getTableColumns().add(new ColumnConfiguration(propertyName, headline, maxWidth, align));
  }

  public void addColumnConfiguration(String propertyName, String headline, int maxWidth, int align, String dateFormat) {
    getTableColumns().add(new ColumnConfiguration(propertyName, headline, maxWidth, align, dateFormat));
  }

  private void analyseData() {
    if (getTableData() != null) {
      for (ColumnConfiguration c : getTableColumns()) {
        if (c.getProperty() != null && c.getProperty().trim().equals("")) {
          continue;
        }
        // Length of the headline is min size of the column
        c.setWidth(c.getHeadline().length());
        for (Object o : getTableData()) {
          Object result;
          if (c.getProperty() == null) {
            result = o.toString();
          } else {
            result = getValueFromObject(o, c);
          }
          if (result != null) {
            String s = result.toString();
            if (s.length() > c.getWidth()) {
              c.setWidth(s.length());
            }
          }
        }
      }
    }
  }

  private String getValueFromObject(Object o, ColumnConfiguration c) {
    if (c.getProperty() == null) {
      return null;
    }
    try {
      Object val = PropertyUtils.getProperty(o, c.getProperty());
      if (val != null) {
        if (val instanceof Date) {
          SimpleDateFormat format = new SimpleDateFormat(c.getDateFormat());
          return format.format(val);
        }
        return val.toString();
      } else {
        return "<null>";
      }
    } catch (org.apache.commons.beanutils.NestedNullException ex){
      return "<null>";
    } catch (NoSuchMethodException ex) {
      System.err.println("Getter not found: " + c.getProperty());
    } catch (Exception e) {
      System.err.println("Error in TextTable: " + e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  public List getTableData() {
    return tableData;
  }

  public void setTableData(List tableData) {
    this.tableData = tableData;
  }

  private List<ColumnConfiguration> getTableColumns() {
    return tableColumns;
  }

  private void setTableColumns(List<ColumnConfiguration> tableColumns) {
    this.tableColumns = tableColumns;
  }

  public String getLineSeparator() {
    return lineSeparator;
  }

  public void setLineSeparator(String lineSeparator) {
    this.lineSeparator = lineSeparator;
  }

  private static class ColumnConfiguration {

    private String property;
    private String headline;
    private int maxWidth;
    private int width;
    private String dateFormat = "yyyy.MM.dd HH:mm:ss.SSS";
    private int layout = ALIGN_LEFT;

    public ColumnConfiguration(String propertyName, String headline, int maxWidth) {
      setProperty(propertyName);
      setHeadline(headline);
      if (headline.length() > maxWidth) {
        setMaxWidth(headline.length());
      } else {
        setMaxWidth(maxWidth);
      }
      setWidth(headline.length());
    }

    public ColumnConfiguration(String propertyName, String headline, int maxWidth, int layout) {
      setProperty(propertyName);
      setHeadline(headline);
      if (headline.length() > maxWidth) {
        setMaxWidth(headline.length());
      } else {
        setMaxWidth(maxWidth);
      }
      setWidth(headline.length());
      setLayout(layout);
    }

    public ColumnConfiguration(String propertyName, String headline, int maxWidth, int layout, String dateFormat) {
      setProperty(propertyName);
      setHeadline(headline);
      if (headline.length() > maxWidth) {
        setMaxWidth(headline.length());
      } else {
        setMaxWidth(maxWidth);
      }
      setWidth(headline.length());
      setDateFormat(dateFormat);
      setLayout(layout);
    }

    public String getProperty() {
      return property;
    }

    public void setProperty(String property) {
      this.property = property;
    }

    public String getHeadline() {
      return headline;
    }

    public void setHeadline(String headline) {
      this.headline = headline;
    }

    public int getWidth() {
      return width;
    }

    public void setWidth(int width) {
      if (width > maxWidth) {
        this.width = maxWidth;
      } else {
        this.width = width;
      }
    }

    public int getMaxWidth() {
      return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
      this.maxWidth = maxWidth;
    }

    public String getDateFormat() {
      return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
      this.dateFormat = dateFormat;
    }

    public int getLayout() {
      return layout;
    }

    public void setLayout(int layout) {
      this.layout = layout;
    }
  }
}
