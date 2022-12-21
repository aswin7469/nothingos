package java.sql;

import android.net.wifi.WifiEnterpriseConfig;
import java.p026io.OutputStream;
import java.p026io.PrintStream;
import java.p026io.PrintWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

public class DriverManager {
    static final SQLPermission SET_LOG_PERMISSION = new SQLPermission("setLog");
    private static volatile PrintStream logStream = null;
    private static final Object logSync = new Object();
    private static volatile PrintWriter logWriter = null;
    private static volatile int loginTimeout = 0;
    private static final CopyOnWriteArrayList<DriverInfo> registeredDrivers = new CopyOnWriteArrayList<>();

    static {
        loadInitialDrivers();
        println("JDBC DriverManager initialized");
    }

    private DriverManager() {
    }

    public static PrintWriter getLogWriter() {
        return logWriter;
    }

    public static void setLogWriter(PrintWriter printWriter) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SET_LOG_PERMISSION);
        }
        logStream = null;
        logWriter = printWriter;
    }

    @CallerSensitive
    public static Connection getConnection(String str, Properties properties) throws SQLException {
        return getConnection(str, properties, Reflection.getCallerClass());
    }

    @CallerSensitive
    public static Connection getConnection(String str, String str2, String str3) throws SQLException {
        Properties properties = new Properties();
        if (str2 != null) {
            properties.put("user", str2);
        }
        if (str3 != null) {
            properties.put(WifiEnterpriseConfig.PASSWORD_KEY, str3);
        }
        return getConnection(str, properties, Reflection.getCallerClass());
    }

    @CallerSensitive
    public static Connection getConnection(String str) throws SQLException {
        return getConnection(str, new Properties(), Reflection.getCallerClass());
    }

    @CallerSensitive
    public static Driver getDriver(String str) throws SQLException {
        println("DriverManager.getDriver(\"" + str + "\")");
        Class<?> callerClass = Reflection.getCallerClass();
        Iterator<DriverInfo> it = registeredDrivers.iterator();
        while (it.hasNext()) {
            DriverInfo next = it.next();
            if (isDriverAllowed(next.driver, callerClass)) {
                try {
                    if (next.driver.acceptsURL(str)) {
                        println("getDriver returning " + next.driver.getClass().getName());
                        return next.driver;
                    }
                } catch (SQLException unused) {
                    continue;
                }
            } else {
                println("    skipping: " + next.driver.getClass().getName());
            }
        }
        println("getDriver: no suitable driver");
        throw new SQLException("No suitable driver", "08001");
    }

    public static synchronized void registerDriver(Driver driver) throws SQLException {
        synchronized (DriverManager.class) {
            if (driver != null) {
                registeredDrivers.addIfAbsent(new DriverInfo(driver));
                println("registerDriver: " + driver);
            } else {
                throw new NullPointerException();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003f, code lost:
        return;
     */
    @sun.reflect.CallerSensitive
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void deregisterDriver(java.sql.Driver r4) throws java.sql.SQLException {
        /*
            java.lang.String r0 = "DriverManager.deregisterDriver: "
            java.lang.Class<java.sql.DriverManager> r1 = java.sql.DriverManager.class
            monitor-enter(r1)
            if (r4 != 0) goto L_0x0009
            monitor-exit(r1)
            return
        L_0x0009:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0040 }
            r2.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0040 }
            r2.append((java.lang.Object) r4)     // Catch:{ all -> 0x0040 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x0040 }
            println(r0)     // Catch:{ all -> 0x0040 }
            java.sql.DriverInfo r0 = new java.sql.DriverInfo     // Catch:{ all -> 0x0040 }
            r0.<init>(r4)     // Catch:{ all -> 0x0040 }
            java.util.concurrent.CopyOnWriteArrayList<java.sql.DriverInfo> r2 = registeredDrivers     // Catch:{ all -> 0x0040 }
            boolean r3 = r2.contains(r0)     // Catch:{ all -> 0x0040 }
            if (r3 == 0) goto L_0x0039
            java.lang.Class r3 = sun.reflect.Reflection.getCallerClass()     // Catch:{ all -> 0x0040 }
            boolean r4 = isDriverAllowed((java.sql.Driver) r4, (java.lang.Class<?>) r3)     // Catch:{ all -> 0x0040 }
            if (r4 == 0) goto L_0x0033
            r2.remove((java.lang.Object) r0)     // Catch:{ all -> 0x0040 }
            goto L_0x003e
        L_0x0033:
            java.lang.SecurityException r4 = new java.lang.SecurityException     // Catch:{ all -> 0x0040 }
            r4.<init>()     // Catch:{ all -> 0x0040 }
            throw r4     // Catch:{ all -> 0x0040 }
        L_0x0039:
            java.lang.String r4 = "    couldn't find driver to unload"
            println(r4)     // Catch:{ all -> 0x0040 }
        L_0x003e:
            monitor-exit(r1)
            return
        L_0x0040:
            r4 = move-exception
            monitor-exit(r1)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.sql.DriverManager.deregisterDriver(java.sql.Driver):void");
    }

    @CallerSensitive
    public static Enumeration<Driver> getDrivers() {
        Vector vector = new Vector();
        Class<?> callerClass = Reflection.getCallerClass();
        Iterator<DriverInfo> it = registeredDrivers.iterator();
        while (it.hasNext()) {
            DriverInfo next = it.next();
            if (isDriverAllowed(next.driver, callerClass)) {
                vector.addElement(next.driver);
            } else {
                println("    skipping: " + next.getClass().getName());
            }
        }
        return vector.elements();
    }

    public static void setLoginTimeout(int i) {
        loginTimeout = i;
    }

    public static int getLoginTimeout() {
        return loginTimeout;
    }

    @Deprecated
    public static void setLogStream(PrintStream printStream) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SET_LOG_PERMISSION);
        }
        logStream = printStream;
        if (printStream != null) {
            logWriter = new PrintWriter((OutputStream) printStream);
        } else {
            logWriter = null;
        }
    }

    @Deprecated
    public static PrintStream getLogStream() {
        return logStream;
    }

    public static void println(String str) {
        synchronized (logSync) {
            if (logWriter != null) {
                logWriter.println(str);
                logWriter.flush();
            }
        }
    }

    private static boolean isDriverAllowed(Driver driver, Class<?> cls) {
        return isDriverAllowed(driver, cls != null ? cls.getClassLoader() : null);
    }

    private static boolean isDriverAllowed(Driver driver, ClassLoader classLoader) {
        Class<?> cls;
        if (driver == null) {
            return false;
        }
        try {
            cls = Class.forName(driver.getClass().getName(), true, classLoader);
        } catch (Exception unused) {
            cls = null;
        }
        if (cls == driver.getClass()) {
            return true;
        }
        return false;
    }

    private static void loadInitialDrivers() {
        String str;
        try {
            str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    return System.getProperty("jdbc.drivers");
                }
            });
        } catch (Exception unused) {
            str = null;
        }
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                Iterator<S> it = ServiceLoader.load(Driver.class).iterator();
                while (it.hasNext()) {
                    try {
                        it.next();
                    } catch (Throwable unused) {
                        return null;
                    }
                }
                return null;
            }
        });
        println("DriverManager.initialize: jdbc.drivers = " + str);
        if (str != null && !str.equals("")) {
            String[] split = str.split(":");
            println("number of Drivers:" + split.length);
            for (String str2 : split) {
                try {
                    println("DriverManager.Initialize: loading " + str2);
                    Class.forName(str2, true, ClassLoader.getSystemClassLoader());
                } catch (Exception e) {
                    println("DriverManager.Initialize: load failed: " + e);
                }
            }
        }
    }

    private static Connection getConnection(String str, Properties properties, Class<?> cls) throws SQLException {
        SQLException sQLException = null;
        ClassLoader classLoader = cls != null ? cls.getClassLoader() : null;
        synchronized (DriverManager.class) {
            if (classLoader == null) {
                classLoader = Thread.currentThread().getContextClassLoader();
            }
        }
        if (str != null) {
            println("DriverManager.getConnection(\"" + str + "\")");
            Iterator<DriverInfo> it = registeredDrivers.iterator();
            while (it.hasNext()) {
                DriverInfo next = it.next();
                if (isDriverAllowed(next.driver, classLoader)) {
                    try {
                        println("    trying " + next.driver.getClass().getName());
                        Connection connect = next.driver.connect(str, properties);
                        if (connect != null) {
                            println("getConnection returning " + next.driver.getClass().getName());
                            return connect;
                        }
                    } catch (SQLException e) {
                        if (sQLException == null) {
                            sQLException = e;
                        }
                    }
                } else {
                    println("    skipping: " + next.getClass().getName());
                }
            }
            if (sQLException != null) {
                println("getConnection failed: " + sQLException);
                throw sQLException;
            }
            println("getConnection: no suitable driver found for " + str);
            throw new SQLException("No suitable driver found for " + str, "08001");
        }
        throw new SQLException("The url cannot be null", "08001");
    }
}
