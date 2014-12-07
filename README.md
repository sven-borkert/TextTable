TextTable
=========

A simple Java library to print tables of bean lists or properties of beans.

## Example of printing a list of beans

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

Creates the following table representation of the data:

    ----------------------------------------------------------------------------------------------------------------
    | Id | Version |  Name  | Error |       Date       |                 UUID                 | Status | Parent ID |
    ----------------------------------------------------------------------------------------------------------------
    |  1 |     123 | Item 1 | true  | 2014.12.06 11:43 | f1767a90-723e-11e4-9149-08606e842dac |   1    | <null>    |
    |  2 |     321 | öäüß€ô | false | 2014.12.06 11:43 | f1763a90-723e-11e4-9149-083061842fab |   12   | 1         |
    ----------------------------------------------------------------------------------------------------------------

## Example of printing properties of a bean

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

Creates:

    ----------------------------------------------------
    | Property  |                Value                 |
    ----------------------------------------------------
    | Id        | 1                                    |
    | Version   | 123                                  |
    | Name      | Item 1                               |
    | Date      | 2014.12.06 12:20:00                  |
    | Error     | true                                 |
    | OtherItem | <null>                               |
    | Status    | 1                                    |
    | Uuid      | f1767a90-723e-11e4-9149-08606e842dac |
    ----------------------------------------------------
