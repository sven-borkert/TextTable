
package net.borkert.util;

import java.util.Date;

public class TestItem {

    private long id;
    private long version;
    private String uuid;
    private String name;
    private Date date;
    private int status = 1;
    private boolean error;
    private TestItem otherItem;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public TestItem getOtherItem() {
        return otherItem;
    }

    public void setOtherItem(TestItem otherItem) {
        this.otherItem = otherItem;
    }
}
