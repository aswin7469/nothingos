package java.sql;

public class DriverPropertyInfo {
    public String[] choices = null;
    public String description = null;
    public String name;
    public boolean required = false;
    public String value;

    public DriverPropertyInfo(String str, String str2) {
        this.name = str;
        this.value = str2;
    }
}
