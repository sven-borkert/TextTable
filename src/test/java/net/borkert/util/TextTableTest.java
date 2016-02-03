
package net.borkert.util;

import junit.framework.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TextTableTest {

  @Test
  public void printTableTest()
      throws Exception {

    List<TestItem> data = new ArrayList<>();
    TestItem i1 = new TestItem();
    i1.setId(1);
    i1.setVersion(123);
    i1.setUuid("f1767a90-723e-11e4-9149-08606e842dac");
    i1.setName("Item 1");
    i1.setDate(new SimpleDateFormat("yyyy.MM.dd HH:mm").parse("2014.12.06 11:43"));
    i1.setStatus(1);
    i1.setError(true);
    data.add(i1);

    TestItem i2 = new TestItem();
    i2.setId(2);
    i2.setVersion(321);
    i2.setUuid("f1763a90-723e-11e4-9149-083061842fab");
    i2.setName("öäüß€ô");
    i2.setDate(new SimpleDateFormat("yyyy.MM.dd HH:mm").parse("2014.12.06 11:43"));
    i2.setStatus(12);
    i2.setError(false);
    i2.setOtherItem(i1);
    data.add(i2);

    TextTable table = new TextTable();
    table.setTableData(data);
    table.addColumnConfiguration("id", "Id", 2, TextTable.ALIGN_RIGHT);
    table.addColumnConfiguration("version", "Version", 2, TextTable.ALIGN_RIGHT);
    table.addColumnConfiguration("name", "Name", 15, TextTable.ALIGN_LEFT);
    table.addColumnConfiguration("error", "Error", 6, TextTable.ALIGN_LEFT);
    table.addColumnConfiguration("date", "Date", 23, TextTable.ALIGN_CENTER, "yyyy.MM.dd HH:mm");
    table.addColumnConfiguration("uuid", "UUID", 36, TextTable.ALIGN_LEFT);
    table.addColumnConfiguration("status", "Status", 6, TextTable.ALIGN_CENTER);
    table.addColumnConfiguration("otherItem.id", "Parent ID", 2, TextTable.ALIGN_LEFT);
    table.printTable(System.out);

    ByteArrayOutputStream b = new ByteArrayOutputStream();
    table.printTable(b);

    String expectedResult =
        "----------------------------------------------------------------------------------------------------------------" + table.getLineSeparator() +
        "| Id | Version |  Name  | Error |       Date       |                 UUID                 | Status | Parent ID |" + table.getLineSeparator() +
        "----------------------------------------------------------------------------------------------------------------" + table.getLineSeparator() +
        "|  1 |     123 | Item 1 | true  | 2014.12.06 11:43 | f1767a90-723e-11e4-9149-08606e842dac |   1    | <null>    |" + table.getLineSeparator() +
        "|  2 |     321 | öäüß€ô | false | 2014.12.06 11:43 | f1763a90-723e-11e4-9149-083061842fab |   12   | 1         |" + table.getLineSeparator() +
        "----------------------------------------------------------------------------------------------------------------" + table.getLineSeparator();

    Assert.assertEquals(expectedResult, b.toString());
  }

  @Test
  public void printTableFixedWidthTest()
      throws Exception {

    List<TestItem> data = new ArrayList<>();
    TestItem i1 = new TestItem();
    i1.setId(1);
    i1.setVersion(123);
    i1.setUuid("f1767a90-723e-11e4-9149-08606e842dac");
    i1.setName("Item 1");
    i1.setDate(new SimpleDateFormat("yyyy.MM.dd HH:mm").parse("2014.12.06 11:43"));
    i1.setStatus(1);
    i1.setError(true);
    data.add(i1);

    TestItem i2 = new TestItem();
    i2.setId(2);
    i2.setVersion(321);
    i2.setUuid("f1763a90-723e-11e4-9149-083061842fab");
    i2.setName("öäüß€ô");
    i2.setDate(new SimpleDateFormat("yyyy.MM.dd HH:mm").parse("2014.12.06 11:43"));
    i2.setStatus(12);
    i2.setError(false);
    i2.setOtherItem(i1);
    data.add(i2);

    TextTable table = new TextTable();
    table.setTableData(data);
    table.addColumnConfigurationFixedWidth("id", "Id", 2, TextTable.ALIGN_RIGHT);
    table.addColumnConfigurationFixedWidth("version", "Version", 2, TextTable.ALIGN_RIGHT);
    table.addColumnConfigurationFixedWidth("name", "Name", 15, TextTable.ALIGN_LEFT);
    table.addColumnConfigurationFixedWidth("error", "Error", 6, TextTable.ALIGN_LEFT);
    table.addColumnConfigurationFixedWidth("date", "Date", 23, TextTable.ALIGN_CENTER, "yyyy.MM.dd HH:mm");
    table.addColumnConfigurationFixedWidth("uuid", "UUID", 36, TextTable.ALIGN_LEFT);
    table.addColumnConfigurationFixedWidth("status", "Status", 6, TextTable.ALIGN_CENTER);
    table.addColumnConfigurationFixedWidth("otherItem.id", "Parent ID", 2, TextTable.ALIGN_LEFT);
    table.printTable(System.out);

    ByteArrayOutputStream b = new ByteArrayOutputStream();
    table.printTable(b);

    String expectedResult =
        "---------------------------------------------------------------------------------------------------------------------------------" + table.getLineSeparator() +
        "| Id | Version |      Name       | Error  |          Date           |                 UUID                 | Status | Parent ID |" + table.getLineSeparator() +
        "---------------------------------------------------------------------------------------------------------------------------------" + table.getLineSeparator() +
        "|  1 |     123 | Item 1          | true   |    2014.12.06 11:43     | f1767a90-723e-11e4-9149-08606e842dac |   1    | <null>    |" + table.getLineSeparator() +
        "|  2 |     321 | öäüß€ô          | false  |    2014.12.06 11:43     | f1763a90-723e-11e4-9149-083061842fab |   12   | 1         |" + table.getLineSeparator() +
        "---------------------------------------------------------------------------------------------------------------------------------" + table.getLineSeparator();

    Assert.assertEquals(expectedResult, b.toString());
  }

  @Test
  public void printBeanTableTest()
      throws Exception {

    TestItem i1 = new TestItem();
    i1.setDate(new SimpleDateFormat("yyyy.MM.dd HH:mm").parse("2014.12.06 12:20"));
    i1.setId(1);
    i1.setVersion(123);
    i1.setUuid("f1767a90-723e-11e4-9149-08606e842dac");
    i1.setName("Item 1");
    i1.setStatus(1);
    i1.setError(true);

    BeanToTextTableConverter c = new BeanToTextTableConverter();
    c.setDateFormat("yyyy.MM.dd HH:mm:ss");

    TextTable table = c.createTextTable(i1);
    table.printTable(System.out);

    ByteArrayOutputStream b = new ByteArrayOutputStream();
    table.printTable(b);

    String expectedResult =
        "----------------------------------------------------" + table.getLineSeparator() +
        "| Property  |                Value                 |" + table.getLineSeparator() +
        "----------------------------------------------------" + table.getLineSeparator() +
        "| Id        | 1                                    |" + table.getLineSeparator() +
        "| Version   | 123                                  |" + table.getLineSeparator() +
        "| Name      | Item 1                               |" + table.getLineSeparator() +
        "| Date      | 2014.12.06 12:20:00                  |" + table.getLineSeparator() +
        "| Error     | true                                 |" + table.getLineSeparator() +
        "| OtherItem | <null>                               |" + table.getLineSeparator() +
        "| Status    | 1                                    |" + table.getLineSeparator() +
        "| Uuid      | f1767a90-723e-11e4-9149-08606e842dac |" + table.getLineSeparator() +
        "----------------------------------------------------" + table.getLineSeparator();

    Assert.assertEquals(expectedResult, b.toString());
  }

}
