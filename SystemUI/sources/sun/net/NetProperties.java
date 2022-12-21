package sun.net;

import java.p026io.BufferedInputStream;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

public class NetProperties {
    private static Properties props = new Properties();

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                NetProperties.loadDefaultProperties();
                return null;
            }
        });
    }

    private NetProperties() {
    }

    /* access modifiers changed from: private */
    public static void loadDefaultProperties() {
        String property = System.getProperty("java.home");
        if (property != null) {
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(new File(property, "lib"), "net.properties").getCanonicalPath()));
                props.load((InputStream) bufferedInputStream);
                bufferedInputStream.close();
            } catch (Exception unused) {
            }
        } else {
            throw new Error("Can't find java.home ??");
        }
    }

    public static String get(String str) {
        try {
            return System.getProperty(str, props.getProperty(str));
        } catch (IllegalArgumentException | NullPointerException unused) {
            return null;
        }
    }

    public static Integer getInteger(String str, int i) {
        String str2;
        try {
            str2 = System.getProperty(str, props.getProperty(str));
        } catch (IllegalArgumentException | NullPointerException unused) {
            str2 = null;
        }
        if (str2 != null) {
            try {
                return Integer.decode(str2);
            } catch (NumberFormatException unused2) {
            }
        }
        return new Integer(i);
    }

    public static Boolean getBoolean(String str) {
        String str2;
        try {
            str2 = System.getProperty(str, props.getProperty(str));
        } catch (IllegalArgumentException | NullPointerException unused) {
            str2 = null;
        }
        if (str2 != null) {
            try {
                return Boolean.valueOf(str2);
            } catch (NumberFormatException unused2) {
            }
        }
        return null;
    }
}
