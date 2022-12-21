package javax.xml.datatype;

import java.net.URL;
import java.p026io.BufferedReader;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.InputStreamReader;
import java.p026io.PrintStream;
import java.p026io.UnsupportedEncodingException;
import java.util.Properties;
import libcore.p030io.IoUtils;

final class FactoryFinder {
    private static final String CLASS_NAME = "javax.xml.datatype.FactoryFinder";
    private static final int DEFAULT_LINE_LENGTH = 80;
    /* access modifiers changed from: private */
    public static boolean debug = false;

    private static class CacheHolder {
        /* access modifiers changed from: private */
        public static Properties cacheProps = new Properties();

        private CacheHolder() {
        }

        static {
            FileInputStream fileInputStream;
            String property = System.getProperty("java.home");
            File file = new File(property + File.separator + "lib" + File.separator + "jaxp.properties");
            if (file.exists()) {
                if (FactoryFinder.debug) {
                    FactoryFinder.debugPrintln("Read properties file " + file);
                }
                try {
                    fileInputStream = new FileInputStream(file);
                    cacheProps.load((InputStream) fileInputStream);
                    fileInputStream.close();
                    return;
                } catch (Exception e) {
                    if (FactoryFinder.debug) {
                        e.printStackTrace();
                        return;
                    }
                    return;
                } catch (Throwable th) {
                    th.addSuppressed(th);
                }
            } else {
                return;
            }
            throw th;
        }
    }

    static {
        String property = System.getProperty("jaxp.debug");
        debug = property != null && !"false".equals(property);
    }

    private FactoryFinder() {
    }

    /* access modifiers changed from: private */
    public static void debugPrintln(String str) {
        if (debug) {
            PrintStream printStream = System.err;
            printStream.println("javax.xml.datatype.FactoryFinder:" + str);
        }
    }

    private static ClassLoader findClassLoader() throws ConfigurationError {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (debug) {
            debugPrintln("Using context class loader: " + contextClassLoader);
        }
        if (contextClassLoader == null) {
            contextClassLoader = FactoryFinder.class.getClassLoader();
            if (debug) {
                debugPrintln("Using the class loader of FactoryFinder: " + contextClassLoader);
            }
        }
        return contextClassLoader;
    }

    static Object newInstance(String str, ClassLoader classLoader) throws ConfigurationError {
        Class<?> cls;
        if (classLoader == null) {
            try {
                cls = Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new ConfigurationError("Provider " + str + " not found", e);
            } catch (Exception e2) {
                throw new ConfigurationError("Provider " + str + " could not be instantiated: " + e2, e2);
            }
        } else {
            cls = classLoader.loadClass(str);
        }
        if (debug) {
            debugPrintln("Loaded " + str + " from " + which(cls));
        }
        return cls.newInstance();
    }

    static Object find(String str, String str2) throws ConfigurationError {
        ClassLoader findClassLoader = findClassLoader();
        String property = System.getProperty(str);
        if (property == null || property.length() <= 0) {
            try {
                String property2 = CacheHolder.cacheProps.getProperty(str);
                if (debug) {
                    debugPrintln("found " + property2 + " in $java.home/jaxp.properties");
                }
                if (property2 != null) {
                    return newInstance(property2, findClassLoader);
                }
            } catch (Exception e) {
                if (debug) {
                    e.printStackTrace();
                }
            }
            Object findJarServiceProvider = findJarServiceProvider(str);
            if (findJarServiceProvider != null) {
                return findJarServiceProvider;
            }
            if (str2 != null) {
                if (debug) {
                    debugPrintln("loaded from fallback value: " + str2);
                }
                return newInstance(str2, findClassLoader);
            }
            throw new ConfigurationError("Provider for " + str + " cannot be found", (Exception) null);
        }
        if (debug) {
            debugPrintln("found " + property + " in the system property " + str);
        }
        return newInstance(property, findClassLoader);
    }

    private static Object findJarServiceProvider(String str) throws ConfigurationError {
        BufferedReader bufferedReader;
        String str2 = "META-INF/services/" + str;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream resourceAsStream = contextClassLoader != null ? contextClassLoader.getResourceAsStream(str2) : null;
        if (resourceAsStream == null) {
            contextClassLoader = FactoryFinder.class.getClassLoader();
            resourceAsStream = contextClassLoader.getResourceAsStream(str2);
        }
        if (resourceAsStream == null) {
            return null;
        }
        if (debug) {
            debugPrintln("found jar resource=" + str2 + " using ClassLoader: " + contextClassLoader);
        }
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"), 80);
        } catch (UnsupportedEncodingException unused) {
            bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream), 80);
        }
        try {
            String readLine = bufferedReader.readLine();
            if (readLine == null || "".equals(readLine)) {
                return null;
            }
            if (debug) {
                debugPrintln("found in resource, value=" + readLine);
            }
            return newInstance(readLine, contextClassLoader);
        } catch (IOException unused2) {
            return null;
        } finally {
            IoUtils.closeQuietly((AutoCloseable) bufferedReader);
        }
    }

    static class ConfigurationError extends Error {
        private static final long serialVersionUID = -3644413026244211347L;
        private Exception exception;

        ConfigurationError(String str, Exception exc) {
            super(str);
            this.exception = exc;
        }

        /* access modifiers changed from: package-private */
        public Exception getException() {
            return this.exception;
        }
    }

    private static String which(Class cls) {
        URL url;
        try {
            String str = cls.getName().replace('.', '/') + ".class";
            ClassLoader classLoader = cls.getClassLoader();
            if (classLoader != null) {
                url = classLoader.getResource(str);
            } else {
                url = ClassLoader.getSystemResource(str);
            }
            if (url != null) {
                return url.toString();
            }
            return "unknown location";
        } catch (VirtualMachineError e) {
            throw e;
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th) {
            if (!debug) {
                return "unknown location";
            }
            th.printStackTrace();
            return "unknown location";
        }
    }
}
