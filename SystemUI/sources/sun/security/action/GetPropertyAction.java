package sun.security.action;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

public class GetPropertyAction implements PrivilegedAction<String> {
    private String defaultVal;
    private String theProp;

    public GetPropertyAction(String str) {
        this.theProp = str;
    }

    public GetPropertyAction(String str, String str2) {
        this.theProp = str;
        this.defaultVal = str2;
    }

    public String run() {
        String property = System.getProperty(this.theProp);
        return property == null ? this.defaultVal : property;
    }

    public static String privilegedGetProperty(String str) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(str);
        }
        return (String) AccessController.doPrivileged(new GetPropertyAction(str));
    }

    public static String privilegedGetProperty(String str, String str2) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(str, str2);
        }
        return (String) AccessController.doPrivileged(new GetPropertyAction(str, str2));
    }

    public static Properties privilegedGetProperties() {
        if (System.getSecurityManager() == null) {
            return System.getProperties();
        }
        return (Properties) AccessController.doPrivileged(new PrivilegedAction<Properties>() {
            public Properties run() {
                return System.getProperties();
            }
        });
    }
}
