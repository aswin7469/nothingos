package javax.xml.xpath;

import java.net.URL;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.PrintStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

final class XPathFactoryFinder {
    private static final int DEFAULT_LINE_LENGTH = 80;
    private static final Class SERVICE_CLASS;
    private static final String SERVICE_ID;
    /* access modifiers changed from: private */
    public static boolean debug = false;
    private final ClassLoader classLoader;

    static {
        String property = System.getProperty("jaxp.debug");
        debug = property != null && !"false".equals(property);
        Class<XPathFactory> cls = XPathFactory.class;
        SERVICE_CLASS = cls;
        SERVICE_ID = "META-INF/services/" + cls.getName();
    }

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
                if (XPathFactoryFinder.debug) {
                    XPathFactoryFinder.debugPrintln("Read properties file " + file);
                }
                try {
                    fileInputStream = new FileInputStream(file);
                    cacheProps.load((InputStream) fileInputStream);
                    fileInputStream.close();
                    return;
                } catch (Exception e) {
                    if (XPathFactoryFinder.debug) {
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

    /* access modifiers changed from: private */
    public static void debugPrintln(String str) {
        if (debug) {
            PrintStream printStream = System.err;
            printStream.println("JAXP: " + str);
        }
    }

    public XPathFactoryFinder(ClassLoader classLoader2) {
        this.classLoader = classLoader2;
        if (debug) {
            debugDisplayClassLoader();
        }
    }

    private void debugDisplayClassLoader() {
        if (this.classLoader == Thread.currentThread().getContextClassLoader()) {
            debugPrintln("using thread context class loader (" + this.classLoader + ") for search");
        } else if (this.classLoader == ClassLoader.getSystemClassLoader()) {
            debugPrintln("using system class loader (" + this.classLoader + ") for search");
        } else {
            debugPrintln("using class loader (" + this.classLoader + ") for search");
        }
    }

    public XPathFactory newFactory(String str) {
        if (str != null) {
            XPathFactory _newFactory = _newFactory(str);
            if (debug) {
                if (_newFactory != null) {
                    debugPrintln("factory '" + _newFactory.getClass().getName() + "' was found for " + str);
                } else {
                    debugPrintln("unable to find a factory for " + str);
                }
            }
            return _newFactory;
        }
        throw new NullPointerException("uri == null");
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x007b A[Catch:{ Exception -> 0x0098 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private javax.xml.xpath.XPathFactory _newFactory(java.lang.String r7) {
        /*
            r6 = this;
            java.lang.String r0 = "The value is '"
            java.lang.String r1 = "found "
            java.lang.String r2 = "Looking up system property '"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.Class r4 = SERVICE_CLASS
            java.lang.String r4 = r4.getName()
            r3.append((java.lang.String) r4)
            java.lang.String r4 = ":"
            r3.append((java.lang.String) r4)
            r3.append((java.lang.String) r7)
            java.lang.String r3 = r3.toString()
            boolean r4 = debug     // Catch:{ Exception -> 0x006b }
            java.lang.String r5 = "'"
            if (r4 == 0) goto L_0x0038
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x006b }
            r4.<init>((java.lang.String) r2)     // Catch:{ Exception -> 0x006b }
            r4.append((java.lang.String) r3)     // Catch:{ Exception -> 0x006b }
            r4.append((java.lang.String) r5)     // Catch:{ Exception -> 0x006b }
            java.lang.String r2 = r4.toString()     // Catch:{ Exception -> 0x006b }
            debugPrintln(r2)     // Catch:{ Exception -> 0x006b }
        L_0x0038:
            java.lang.String r2 = java.lang.System.getProperty(r3)     // Catch:{ Exception -> 0x006b }
            if (r2 == 0) goto L_0x0061
            int r4 = r2.length()     // Catch:{ Exception -> 0x006b }
            if (r4 <= 0) goto L_0x0061
            boolean r4 = debug     // Catch:{ Exception -> 0x006b }
            if (r4 == 0) goto L_0x005a
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x006b }
            r4.<init>((java.lang.String) r0)     // Catch:{ Exception -> 0x006b }
            r4.append((java.lang.String) r2)     // Catch:{ Exception -> 0x006b }
            r4.append((java.lang.String) r5)     // Catch:{ Exception -> 0x006b }
            java.lang.String r0 = r4.toString()     // Catch:{ Exception -> 0x006b }
            debugPrintln(r0)     // Catch:{ Exception -> 0x006b }
        L_0x005a:
            javax.xml.xpath.XPathFactory r0 = r6.createInstance(r2)     // Catch:{ Exception -> 0x006b }
            if (r0 == 0) goto L_0x006f
            return r0
        L_0x0061:
            boolean r0 = debug     // Catch:{ Exception -> 0x006b }
            if (r0 == 0) goto L_0x006f
            java.lang.String r0 = "The property is undefined."
            debugPrintln(r0)     // Catch:{ Exception -> 0x006b }
            goto L_0x006f
        L_0x006b:
            r0 = move-exception
            r0.printStackTrace()
        L_0x006f:
            java.util.Properties r0 = javax.xml.xpath.XPathFactoryFinder.CacheHolder.cacheProps     // Catch:{ Exception -> 0x0098 }
            java.lang.String r0 = r0.getProperty(r3)     // Catch:{ Exception -> 0x0098 }
            boolean r2 = debug     // Catch:{ Exception -> 0x0098 }
            if (r2 == 0) goto L_0x008f
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0098 }
            r2.<init>((java.lang.String) r1)     // Catch:{ Exception -> 0x0098 }
            r2.append((java.lang.String) r0)     // Catch:{ Exception -> 0x0098 }
            java.lang.String r1 = " in $java.home/jaxp.properties"
            r2.append((java.lang.String) r1)     // Catch:{ Exception -> 0x0098 }
            java.lang.String r1 = r2.toString()     // Catch:{ Exception -> 0x0098 }
            debugPrintln(r1)     // Catch:{ Exception -> 0x0098 }
        L_0x008f:
            if (r0 == 0) goto L_0x00a0
            javax.xml.xpath.XPathFactory r0 = r6.createInstance(r0)     // Catch:{ Exception -> 0x0098 }
            if (r0 == 0) goto L_0x00a0
            return r0
        L_0x0098:
            r0 = move-exception
            boolean r1 = debug
            if (r1 == 0) goto L_0x00a0
            r0.printStackTrace()
        L_0x00a0:
            java.lang.Iterable r0 = r6.createServiceFileIterator()
            java.util.Iterator r0 = r0.iterator()
        L_0x00a8:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x00f2
            java.lang.Object r1 = r0.next()
            java.net.URL r1 = (java.net.URL) r1
            boolean r2 = debug
            if (r2 == 0) goto L_0x00c9
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "looking into "
            r2.<init>((java.lang.String) r3)
            r2.append((java.lang.Object) r1)
            java.lang.String r2 = r2.toString()
            debugPrintln(r2)
        L_0x00c9:
            java.lang.String r2 = r1.toExternalForm()     // Catch:{ IOException -> 0x00d8 }
            java.io.InputStream r3 = r1.openStream()     // Catch:{ IOException -> 0x00d8 }
            javax.xml.xpath.XPathFactory r1 = r6.loadFromServicesFile(r7, r2, r3)     // Catch:{ IOException -> 0x00d8 }
            if (r1 == 0) goto L_0x00a8
            return r1
        L_0x00d8:
            r2 = move-exception
            boolean r3 = debug
            if (r3 == 0) goto L_0x00a8
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "failed to read "
            r3.<init>((java.lang.String) r4)
            r3.append((java.lang.Object) r1)
            java.lang.String r1 = r3.toString()
            debugPrintln(r1)
            r2.printStackTrace()
            goto L_0x00a8
        L_0x00f2:
            java.lang.String r0 = "http://java.sun.com/jaxp/xpath/dom"
            boolean r7 = r7.equals(r0)
            if (r7 == 0) goto L_0x010a
            boolean r7 = debug
            if (r7 == 0) goto L_0x0103
            java.lang.String r7 = "attempting to use the platform default W3C DOM XPath lib"
            debugPrintln(r7)
        L_0x0103:
            java.lang.String r7 = "org.apache.xpath.jaxp.XPathFactoryImpl"
            javax.xml.xpath.XPathFactory r6 = r6.createInstance(r7)
            return r6
        L_0x010a:
            boolean r6 = debug
            if (r6 == 0) goto L_0x0113
            java.lang.String r6 = "all things were tried, but none was found. bailing out."
            debugPrintln(r6)
        L_0x0113:
            r6 = 0
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.xml.xpath.XPathFactoryFinder._newFactory(java.lang.String):javax.xml.xpath.XPathFactory");
    }

    /* access modifiers changed from: package-private */
    public XPathFactory createInstance(String str) {
        Class<?> cls;
        try {
            if (debug) {
                debugPrintln("instantiating " + str);
            }
            ClassLoader classLoader2 = this.classLoader;
            if (classLoader2 != null) {
                cls = classLoader2.loadClass(str);
            } else {
                cls = Class.forName(str);
            }
            if (debug) {
                debugPrintln("loaded it from " + which(cls));
            }
            Object newInstance = cls.newInstance();
            if (newInstance instanceof XPathFactory) {
                return (XPathFactory) newInstance;
            }
            if (!debug) {
                return null;
            }
            debugPrintln(str + " is not assignable to " + SERVICE_CLASS.getName());
            return null;
        } catch (VirtualMachineError e) {
            throw e;
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th) {
            if (!debug) {
                return null;
            }
            debugPrintln("failed to instantiate " + str);
            th.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x002e */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002e A[LOOP:0: B:8:0x002e->B:23:0x002e, LOOP_START, SYNTHETIC, Splitter:B:8:0x002e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private javax.xml.xpath.XPathFactory loadFromServicesFile(java.lang.String r4, java.lang.String r5, java.p026io.InputStream r6) {
        /*
            r3 = this;
            boolean r0 = debug
            if (r0 == 0) goto L_0x0015
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Reading "
            r0.<init>((java.lang.String) r1)
            r0.append((java.lang.String) r5)
            java.lang.String r5 = r0.toString()
            debugPrintln(r5)
        L_0x0015:
            r5 = 80
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ UnsupportedEncodingException -> 0x0024 }
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch:{ UnsupportedEncodingException -> 0x0024 }
            java.lang.String r2 = "UTF-8"
            r1.<init>((java.p026io.InputStream) r6, (java.lang.String) r2)     // Catch:{ UnsupportedEncodingException -> 0x0024 }
            r0.<init>(r1, r5)     // Catch:{ UnsupportedEncodingException -> 0x0024 }
            goto L_0x002e
        L_0x0024:
            java.io.BufferedReader r0 = new java.io.BufferedReader
            java.io.InputStreamReader r1 = new java.io.InputStreamReader
            r1.<init>(r6)
            r0.<init>(r1, r5)
        L_0x002e:
            java.lang.String r5 = r0.readLine()     // Catch:{ IOException -> 0x0058 }
            if (r5 == 0) goto L_0x0058
            r6 = 35
            int r6 = r5.indexOf((int) r6)
            r1 = -1
            if (r6 == r1) goto L_0x0042
            r1 = 0
            java.lang.String r5 = r5.substring(r1, r6)
        L_0x0042:
            java.lang.String r5 = r5.trim()
            int r6 = r5.length()
            if (r6 != 0) goto L_0x004d
            goto L_0x002e
        L_0x004d:
            javax.xml.xpath.XPathFactory r5 = r3.createInstance(r5)     // Catch:{ Exception -> 0x002e }
            boolean r6 = r5.isObjectModelSupported(r4)     // Catch:{ Exception -> 0x002e }
            if (r6 == 0) goto L_0x002e
            goto L_0x0059
        L_0x0058:
            r5 = 0
        L_0x0059:
            libcore.p030io.IoUtils.closeQuietly((java.lang.AutoCloseable) r0)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.xml.xpath.XPathFactoryFinder.loadFromServicesFile(java.lang.String, java.lang.String, java.io.InputStream):javax.xml.xpath.XPathFactory");
    }

    private Iterable<URL> createServiceFileIterator() {
        ClassLoader classLoader2 = this.classLoader;
        if (classLoader2 == null) {
            return Collections.singleton(XPathFactoryFinder.class.getClassLoader().getResource(SERVICE_ID));
        }
        try {
            String str = SERVICE_ID;
            Enumeration<URL> resources = classLoader2.getResources(str);
            if (debug && !resources.hasMoreElements()) {
                debugPrintln("no " + str + " file was found");
            }
            return Collections.list(resources);
        } catch (IOException e) {
            if (debug) {
                debugPrintln("failed to enumerate resources " + SERVICE_ID);
                e.printStackTrace();
            }
            return Collections.emptySet();
        }
    }

    private static String which(Class cls) {
        return which(cls.getName(), cls.getClassLoader());
    }

    private static String which(String str, ClassLoader classLoader2) {
        String str2 = str.replace('.', '/') + ".class";
        if (classLoader2 == null) {
            classLoader2 = ClassLoader.getSystemClassLoader();
        }
        URL resource = classLoader2.getResource(str2);
        if (resource != null) {
            return resource.toString();
        }
        return null;
    }
}
