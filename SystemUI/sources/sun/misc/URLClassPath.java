package sun.misc;

import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.settingslib.datetime.ZoneGetter;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.p026io.Closeable;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.FileNotFoundException;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.PrintStream;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import sun.net.util.URLUtil;
import sun.net.www.ParseUtil;
import sun.security.action.GetPropertyAction;

public class URLClassPath {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = (AccessController.doPrivileged(new GetPropertyAction("sun.misc.URLClassPath.debug")) != null);
    private static final boolean DEBUG_LOOKUP_CACHE = (AccessController.doPrivileged(new GetPropertyAction("sun.misc.URLClassPath.debugLookupCache")) != null);
    private static final boolean DISABLE_ACC_CHECKING;
    /* access modifiers changed from: private */
    public static final boolean DISABLE_JAR_CHECKING;
    static final String JAVA_VERSION = ((String) AccessController.doPrivileged(new GetPropertyAction("java.version")));
    static final String USER_AGENT_JAVA_VERSION = "UA-Java-Version";
    private static volatile boolean lookupCacheEnabled = false;
    /* access modifiers changed from: private */
    public final AccessControlContext acc;
    private boolean closed;
    /* access modifiers changed from: private */
    public URLStreamHandler jarHandler;
    HashMap<String, Loader> lmap;
    ArrayList<Loader> loaders;
    private ClassLoader lookupCacheLoader;
    private URL[] lookupCacheURLs;
    private ArrayList<URL> path;
    Stack<URL> urls;

    private static int[] getLookupCacheForClassLoader(ClassLoader classLoader, String str) {
        return null;
    }

    private URL[] getLookupCacheURLs(ClassLoader classLoader) {
        return null;
    }

    private static boolean knownToNotExist0(ClassLoader classLoader, String str) {
        return false;
    }

    static {
        boolean z = true;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.misc.URLClassPath.disableJarChecking"));
        DISABLE_JAR_CHECKING = str != null && (str.equals("true") || str.equals(""));
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("jdk.net.URLClassPath.disableRestrictedPermissions"));
        if (str2 == null || (!str2.equals("true") && !str2.equals(""))) {
            z = false;
        }
        DISABLE_ACC_CHECKING = z;
    }

    public URLClassPath(URL[] urlArr, URLStreamHandlerFactory uRLStreamHandlerFactory, AccessControlContext accessControlContext) {
        this.path = new ArrayList<>();
        this.urls = new Stack<>();
        this.loaders = new ArrayList<>();
        this.lmap = new HashMap<>();
        this.closed = false;
        for (URL add : urlArr) {
            this.path.add(add);
        }
        push(urlArr);
        if (uRLStreamHandlerFactory != null) {
            this.jarHandler = uRLStreamHandlerFactory.createURLStreamHandler("jar");
        }
        if (DISABLE_ACC_CHECKING) {
            this.acc = null;
        } else {
            this.acc = accessControlContext;
        }
    }

    public URLClassPath(URL[] urlArr) {
        this(urlArr, (URLStreamHandlerFactory) null, (AccessControlContext) null);
    }

    public URLClassPath(URL[] urlArr, AccessControlContext accessControlContext) {
        this(urlArr, (URLStreamHandlerFactory) null, accessControlContext);
    }

    public synchronized List<IOException> closeLoaders() {
        if (this.closed) {
            return Collections.emptyList();
        }
        LinkedList linkedList = new LinkedList();
        Iterator<Loader> it = this.loaders.iterator();
        while (it.hasNext()) {
            try {
                it.next().close();
            } catch (IOException e) {
                linkedList.add(e);
            }
        }
        this.closed = true;
        return linkedList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0029, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void addURL(java.net.URL r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = r3.closed     // Catch:{ all -> 0x0030 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r3)
            return
        L_0x0007:
            java.util.Stack<java.net.URL> r0 = r3.urls     // Catch:{ all -> 0x0030 }
            monitor-enter(r0)     // Catch:{ all -> 0x0030 }
            if (r4 == 0) goto L_0x002a
            java.util.ArrayList<java.net.URL> r1 = r3.path     // Catch:{ all -> 0x002d }
            boolean r1 = r1.contains(r4)     // Catch:{ all -> 0x002d }
            if (r1 == 0) goto L_0x0015
            goto L_0x002a
        L_0x0015:
            java.util.Stack<java.net.URL> r1 = r3.urls     // Catch:{ all -> 0x002d }
            r2 = 0
            r1.add(r2, r4)     // Catch:{ all -> 0x002d }
            java.util.ArrayList<java.net.URL> r1 = r3.path     // Catch:{ all -> 0x002d }
            r1.add(r4)     // Catch:{ all -> 0x002d }
            java.net.URL[] r4 = r3.lookupCacheURLs     // Catch:{ all -> 0x002d }
            if (r4 == 0) goto L_0x0027
            disableAllLookupCaches()     // Catch:{ all -> 0x002d }
        L_0x0027:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            monitor-exit(r3)
            return
        L_0x002a:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            monitor-exit(r3)
            return
        L_0x002d:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            throw r4     // Catch:{ all -> 0x0030 }
        L_0x0030:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.URLClassPath.addURL(java.net.URL):void");
    }

    public URL[] getURLs() {
        URL[] urlArr;
        synchronized (this.urls) {
            ArrayList<URL> arrayList = this.path;
            urlArr = (URL[]) arrayList.toArray(new URL[arrayList.size()]);
        }
        return urlArr;
    }

    public URL findResource(String str, boolean z) {
        int[] lookupCache = getLookupCache(str);
        int i = 0;
        while (true) {
            Loader nextLoader = getNextLoader(lookupCache, i);
            if (nextLoader == null) {
                return null;
            }
            URL findResource = nextLoader.findResource(str, z);
            if (findResource != null) {
                return findResource;
            }
            i++;
        }
    }

    public Resource getResource(String str, boolean z) {
        if (DEBUG) {
            PrintStream printStream = System.err;
            printStream.println("URLClassPath.getResource(\"" + str + "\")");
        }
        int[] lookupCache = getLookupCache(str);
        int i = 0;
        while (true) {
            Loader nextLoader = getNextLoader(lookupCache, i);
            if (nextLoader == null) {
                return null;
            }
            Resource resource = nextLoader.getResource(str, z);
            if (resource != null) {
                return resource;
            }
            i++;
        }
    }

    public Enumeration<URL> findResources(String str, boolean z) {
        return new Enumeration<URL>(str, z) {
            private int[] cache;
            private int index = 0;
            private URL url;
            final /* synthetic */ boolean val$check;
            final /* synthetic */ String val$name;

            {
                this.val$name = r2;
                this.val$check = r3;
                this.cache = URLClassPath.this.getLookupCache(r2);
                this.url = null;
            }

            private boolean next() {
                URL findResource;
                if (this.url != null) {
                    return true;
                }
                do {
                    URLClassPath uRLClassPath = URLClassPath.this;
                    int[] iArr = this.cache;
                    int i = this.index;
                    this.index = i + 1;
                    Loader r0 = uRLClassPath.getNextLoader(iArr, i);
                    if (r0 == null) {
                        return false;
                    }
                    findResource = r0.findResource(this.val$name, this.val$check);
                    this.url = findResource;
                } while (findResource == null);
                return true;
            }

            public boolean hasMoreElements() {
                return next();
            }

            public URL nextElement() {
                if (next()) {
                    URL url2 = this.url;
                    this.url = null;
                    return url2;
                }
                throw new NoSuchElementException();
            }
        };
    }

    public Resource getResource(String str) {
        return getResource(str, true);
    }

    public Enumeration<Resource> getResources(String str, boolean z) {
        return new Enumeration<Resource>(str, z) {
            private int[] cache;
            private int index = 0;
            private Resource res;
            final /* synthetic */ boolean val$check;
            final /* synthetic */ String val$name;

            {
                this.val$name = r2;
                this.val$check = r3;
                this.cache = URLClassPath.this.getLookupCache(r2);
                this.res = null;
            }

            private boolean next() {
                Resource resource;
                if (this.res != null) {
                    return true;
                }
                do {
                    URLClassPath uRLClassPath = URLClassPath.this;
                    int[] iArr = this.cache;
                    int i = this.index;
                    this.index = i + 1;
                    Loader r0 = uRLClassPath.getNextLoader(iArr, i);
                    if (r0 == null) {
                        return false;
                    }
                    resource = r0.getResource(this.val$name, this.val$check);
                    this.res = resource;
                } while (resource == null);
                return true;
            }

            public boolean hasMoreElements() {
                return next();
            }

            public Resource nextElement() {
                if (next()) {
                    Resource resource = this.res;
                    this.res = null;
                    return resource;
                }
                throw new NoSuchElementException();
            }
        };
    }

    public Enumeration<Resource> getResources(String str) {
        return getResources(str, true);
    }

    /* access modifiers changed from: package-private */
    public synchronized void initLookupCache(ClassLoader classLoader) {
        URL[] lookupCacheURLs2 = getLookupCacheURLs(classLoader);
        this.lookupCacheURLs = lookupCacheURLs2;
        if (lookupCacheURLs2 != null) {
            this.lookupCacheLoader = classLoader;
        } else {
            disableAllLookupCaches();
        }
    }

    static void disableAllLookupCaches() {
        lookupCacheEnabled = false;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0012, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean knownToNotExist(java.lang.String r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            java.net.URL[] r0 = r1.lookupCacheURLs     // Catch:{ all -> 0x0014 }
            if (r0 == 0) goto L_0x0011
            boolean r0 = lookupCacheEnabled     // Catch:{ all -> 0x0014 }
            if (r0 == 0) goto L_0x0011
            java.lang.ClassLoader r0 = r1.lookupCacheLoader     // Catch:{ all -> 0x0014 }
            boolean r2 = knownToNotExist0(r0, r2)     // Catch:{ all -> 0x0014 }
            monitor-exit(r1)
            return r2
        L_0x0011:
            monitor-exit(r1)
            r1 = 0
            return r1
        L_0x0014:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.URLClassPath.knownToNotExist(java.lang.String):boolean");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0047, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0049, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004b, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int[] getLookupCache(java.lang.String r5) {
        /*
            r4 = this;
            java.lang.String r0 = "Expanded loaders FAILED "
            monitor-enter(r4)
            java.net.URL[] r1 = r4.lookupCacheURLs     // Catch:{ all -> 0x004c }
            r2 = 0
            if (r1 == 0) goto L_0x004a
            boolean r1 = lookupCacheEnabled     // Catch:{ all -> 0x004c }
            if (r1 != 0) goto L_0x000d
            goto L_0x004a
        L_0x000d:
            java.lang.ClassLoader r1 = r4.lookupCacheLoader     // Catch:{ all -> 0x004c }
            int[] r5 = getLookupCacheForClassLoader(r1, r5)     // Catch:{ all -> 0x004c }
            if (r5 == 0) goto L_0x0048
            int r1 = r5.length     // Catch:{ all -> 0x004c }
            if (r1 <= 0) goto L_0x0048
            int r1 = r5.length     // Catch:{ all -> 0x004c }
            int r1 = r1 + -1
            r1 = r5[r1]     // Catch:{ all -> 0x004c }
            boolean r3 = r4.ensureLoaderOpened(r1)     // Catch:{ all -> 0x004c }
            if (r3 != 0) goto L_0x0048
            boolean r5 = DEBUG_LOOKUP_CACHE     // Catch:{ all -> 0x004c }
            if (r5 == 0) goto L_0x0046
            java.io.PrintStream r5 = java.lang.System.out     // Catch:{ all -> 0x004c }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x004c }
            r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x004c }
            java.util.ArrayList<sun.misc.URLClassPath$Loader> r0 = r4.loaders     // Catch:{ all -> 0x004c }
            int r0 = r0.size()     // Catch:{ all -> 0x004c }
            r3.append((int) r0)     // Catch:{ all -> 0x004c }
            java.lang.String r0 = " for maxindex="
            r3.append((java.lang.String) r0)     // Catch:{ all -> 0x004c }
            r3.append((int) r1)     // Catch:{ all -> 0x004c }
            java.lang.String r0 = r3.toString()     // Catch:{ all -> 0x004c }
            r5.println((java.lang.String) r0)     // Catch:{ all -> 0x004c }
        L_0x0046:
            monitor-exit(r4)
            return r2
        L_0x0048:
            monitor-exit(r4)
            return r5
        L_0x004a:
            monitor-exit(r4)
            return r2
        L_0x004c:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.URLClassPath.getLookupCache(java.lang.String):int[]");
    }

    private boolean ensureLoaderOpened(int i) {
        if (this.loaders.size() > i) {
            return true;
        }
        if (getLoader(i) == null || !lookupCacheEnabled) {
            return false;
        }
        if (!DEBUG_LOOKUP_CACHE) {
            return true;
        }
        PrintStream printStream = System.out;
        printStream.println("Expanded loaders " + this.loaders.size() + " to index=" + i);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void validateLookupCache(int r4, java.lang.String r5) {
        /*
            r3 = this;
            java.lang.String r0 = "WARNING: resource lookup cache invalidated for lookupCacheLoader at "
            monitor-enter(r3)
            java.net.URL[] r1 = r3.lookupCacheURLs     // Catch:{ all -> 0x003c }
            if (r1 == 0) goto L_0x003a
            boolean r1 = lookupCacheEnabled     // Catch:{ all -> 0x003c }
            if (r1 == 0) goto L_0x003a
            java.net.URL[] r1 = r3.lookupCacheURLs     // Catch:{ all -> 0x003c }
            int r2 = r1.length     // Catch:{ all -> 0x003c }
            if (r4 >= r2) goto L_0x001e
            r1 = r1[r4]     // Catch:{ all -> 0x003c }
            java.lang.String r1 = sun.net.util.URLUtil.urlNoFragString(r1)     // Catch:{ all -> 0x003c }
            boolean r5 = r5.equals(r1)     // Catch:{ all -> 0x003c }
            if (r5 == 0) goto L_0x001e
            monitor-exit(r3)
            return
        L_0x001e:
            boolean r5 = DEBUG     // Catch:{ all -> 0x003c }
            if (r5 != 0) goto L_0x0026
            boolean r5 = DEBUG_LOOKUP_CACHE     // Catch:{ all -> 0x003c }
            if (r5 == 0) goto L_0x0037
        L_0x0026:
            java.io.PrintStream r5 = java.lang.System.out     // Catch:{ all -> 0x003c }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x003c }
            r1.<init>((java.lang.String) r0)     // Catch:{ all -> 0x003c }
            r1.append((int) r4)     // Catch:{ all -> 0x003c }
            java.lang.String r4 = r1.toString()     // Catch:{ all -> 0x003c }
            r5.println((java.lang.String) r4)     // Catch:{ all -> 0x003c }
        L_0x0037:
            disableAllLookupCaches()     // Catch:{ all -> 0x003c }
        L_0x003a:
            monitor-exit(r3)
            return
        L_0x003c:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.URLClassPath.validateLookupCache(int, java.lang.String):void");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003d, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized sun.misc.URLClassPath.Loader getNextLoader(int[] r5, int r6) {
        /*
            r4 = this;
            java.lang.String r0 = "HASCACHE: Loading from : "
            monitor-enter(r4)
            boolean r1 = r4.closed     // Catch:{ all -> 0x0046 }
            r2 = 0
            if (r1 == 0) goto L_0x000a
            monitor-exit(r4)
            return r2
        L_0x000a:
            if (r5 == 0) goto L_0x0040
            int r1 = r5.length     // Catch:{ all -> 0x0046 }
            if (r6 >= r1) goto L_0x003e
            java.util.ArrayList<sun.misc.URLClassPath$Loader> r1 = r4.loaders     // Catch:{ all -> 0x0046 }
            r2 = r5[r6]     // Catch:{ all -> 0x0046 }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x0046 }
            sun.misc.URLClassPath$Loader r1 = (sun.misc.URLClassPath.Loader) r1     // Catch:{ all -> 0x0046 }
            boolean r2 = DEBUG_LOOKUP_CACHE     // Catch:{ all -> 0x0046 }
            if (r2 == 0) goto L_0x003c
            java.io.PrintStream r2 = java.lang.System.out     // Catch:{ all -> 0x0046 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0046 }
            r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0046 }
            r5 = r5[r6]     // Catch:{ all -> 0x0046 }
            r3.append((int) r5)     // Catch:{ all -> 0x0046 }
            java.lang.String r5 = " = "
            r3.append((java.lang.String) r5)     // Catch:{ all -> 0x0046 }
            java.net.URL r5 = r1.getBaseURL()     // Catch:{ all -> 0x0046 }
            r3.append((java.lang.Object) r5)     // Catch:{ all -> 0x0046 }
            java.lang.String r5 = r3.toString()     // Catch:{ all -> 0x0046 }
            r2.println((java.lang.String) r5)     // Catch:{ all -> 0x0046 }
        L_0x003c:
            monitor-exit(r4)
            return r1
        L_0x003e:
            monitor-exit(r4)
            return r2
        L_0x0040:
            sun.misc.URLClassPath$Loader r5 = r4.getLoader((int) r6)     // Catch:{ all -> 0x0046 }
            monitor-exit(r4)
            return r5
        L_0x0046:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.URLClassPath.getNextLoader(int[], int):sun.misc.URLClassPath$Loader");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r0 = sun.net.util.URLUtil.urlNoFragString(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0033, code lost:
        if (r6.lmap.containsKey(r0) == false) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r3 = getLoader(r2);
        r4 = r3.getClassPath();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x003e, code lost:
        if (r4 == null) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0040, code lost:
        push(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        validateLookupCache(r6.loaders.size(), r0);
        r6.loaders.add(r3);
        r6.lmap.put(r0, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0057, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x005a, code lost:
        if (DEBUG != false) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005c, code lost:
        r3 = java.lang.System.err;
        r3.println("Failed to access " + r2 + ", " + r0);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0008 */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0008 A[DONT_GENERATE, LOOP:0: B:7:0x0008->B:48:0x0008, LOOP_START, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized sun.misc.URLClassPath.Loader getLoader(int r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            boolean r0 = r6.closed     // Catch:{ all -> 0x00a2 }
            r1 = 0
            if (r0 == 0) goto L_0x0008
            monitor-exit(r6)
            return r1
        L_0x0008:
            java.util.ArrayList<sun.misc.URLClassPath$Loader> r0 = r6.loaders     // Catch:{ all -> 0x00a2 }
            int r0 = r0.size()     // Catch:{ all -> 0x00a2 }
            int r2 = r7 + 1
            if (r0 >= r2) goto L_0x007e
            java.util.Stack<java.net.URL> r0 = r6.urls     // Catch:{ all -> 0x00a2 }
            monitor-enter(r0)     // Catch:{ all -> 0x00a2 }
            java.util.Stack<java.net.URL> r2 = r6.urls     // Catch:{ all -> 0x007b }
            boolean r2 = r2.empty()     // Catch:{ all -> 0x007b }
            if (r2 == 0) goto L_0x0020
            monitor-exit(r0)     // Catch:{ all -> 0x007b }
            monitor-exit(r6)
            return r1
        L_0x0020:
            java.util.Stack<java.net.URL> r2 = r6.urls     // Catch:{ all -> 0x007b }
            java.lang.Object r2 = r2.pop()     // Catch:{ all -> 0x007b }
            java.net.URL r2 = (java.net.URL) r2     // Catch:{ all -> 0x007b }
            monitor-exit(r0)     // Catch:{ all -> 0x007b }
            java.lang.String r0 = sun.net.util.URLUtil.urlNoFragString(r2)     // Catch:{ all -> 0x00a2 }
            java.util.HashMap<java.lang.String, sun.misc.URLClassPath$Loader> r3 = r6.lmap     // Catch:{ all -> 0x00a2 }
            boolean r3 = r3.containsKey(r0)     // Catch:{ all -> 0x00a2 }
            if (r3 == 0) goto L_0x0036
            goto L_0x0008
        L_0x0036:
            sun.misc.URLClassPath$Loader r3 = r6.getLoader((java.net.URL) r2)     // Catch:{ IOException -> 0x0008, SecurityException -> 0x0057 }
            java.net.URL[] r4 = r3.getClassPath()     // Catch:{ IOException -> 0x0008, SecurityException -> 0x0057 }
            if (r4 == 0) goto L_0x0043
            r6.push(r4)     // Catch:{ IOException -> 0x0008, SecurityException -> 0x0057 }
        L_0x0043:
            java.util.ArrayList<sun.misc.URLClassPath$Loader> r2 = r6.loaders     // Catch:{ all -> 0x00a2 }
            int r2 = r2.size()     // Catch:{ all -> 0x00a2 }
            r6.validateLookupCache(r2, r0)     // Catch:{ all -> 0x00a2 }
            java.util.ArrayList<sun.misc.URLClassPath$Loader> r2 = r6.loaders     // Catch:{ all -> 0x00a2 }
            r2.add(r3)     // Catch:{ all -> 0x00a2 }
            java.util.HashMap<java.lang.String, sun.misc.URLClassPath$Loader> r2 = r6.lmap     // Catch:{ all -> 0x00a2 }
            r2.put(r0, r3)     // Catch:{ all -> 0x00a2 }
            goto L_0x0008
        L_0x0057:
            r0 = move-exception
            boolean r3 = DEBUG     // Catch:{ all -> 0x00a2 }
            if (r3 == 0) goto L_0x0008
            java.io.PrintStream r3 = java.lang.System.err     // Catch:{ all -> 0x00a2 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a2 }
            r4.<init>()     // Catch:{ all -> 0x00a2 }
            java.lang.String r5 = "Failed to access "
            r4.append((java.lang.String) r5)     // Catch:{ all -> 0x00a2 }
            r4.append((java.lang.Object) r2)     // Catch:{ all -> 0x00a2 }
            java.lang.String r2 = ", "
            r4.append((java.lang.String) r2)     // Catch:{ all -> 0x00a2 }
            r4.append((java.lang.Object) r0)     // Catch:{ all -> 0x00a2 }
            java.lang.String r0 = r4.toString()     // Catch:{ all -> 0x00a2 }
            r3.println((java.lang.String) r0)     // Catch:{ all -> 0x00a2 }
            goto L_0x0008
        L_0x007b:
            r7 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x007b }
            throw r7     // Catch:{ all -> 0x00a2 }
        L_0x007e:
            boolean r0 = DEBUG_LOOKUP_CACHE     // Catch:{ all -> 0x00a2 }
            if (r0 == 0) goto L_0x0098
            java.io.PrintStream r0 = java.lang.System.out     // Catch:{ all -> 0x00a2 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a2 }
            r1.<init>()     // Catch:{ all -> 0x00a2 }
            java.lang.String r2 = "NOCACHE: Loading from : "
            r1.append((java.lang.String) r2)     // Catch:{ all -> 0x00a2 }
            r1.append((int) r7)     // Catch:{ all -> 0x00a2 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00a2 }
            r0.println((java.lang.String) r1)     // Catch:{ all -> 0x00a2 }
        L_0x0098:
            java.util.ArrayList<sun.misc.URLClassPath$Loader> r0 = r6.loaders     // Catch:{ all -> 0x00a2 }
            java.lang.Object r7 = r0.get(r7)     // Catch:{ all -> 0x00a2 }
            sun.misc.URLClassPath$Loader r7 = (sun.misc.URLClassPath.Loader) r7     // Catch:{ all -> 0x00a2 }
            monitor-exit(r6)
            return r7
        L_0x00a2:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.URLClassPath.getLoader(int):sun.misc.URLClassPath$Loader");
    }

    private Loader getLoader(final URL url) throws IOException {
        try {
            return (Loader) AccessController.doPrivileged(new PrivilegedExceptionAction<Loader>() {
                public Loader run() throws IOException {
                    String file = url.getFile();
                    if (file == null || !file.endsWith("/")) {
                        return new JarLoader(url, URLClassPath.this.jarHandler, URLClassPath.this.lmap, URLClassPath.this.acc);
                    }
                    if ("file".equals(url.getProtocol())) {
                        return new FileLoader(url);
                    }
                    return new Loader(url);
                }
            }, this.acc);
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }

    private void push(URL[] urlArr) {
        synchronized (this.urls) {
            for (int length = urlArr.length - 1; length >= 0; length--) {
                this.urls.push(urlArr[length]);
            }
        }
    }

    public static URL[] pathToURLs(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, File.pathSeparator);
        int countTokens = stringTokenizer.countTokens();
        URL[] urlArr = new URL[countTokens];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            File file = new File(stringTokenizer.nextToken());
            try {
                file = new File(file.getCanonicalPath());
            } catch (IOException unused) {
            }
            int i2 = i + 1;
            try {
                urlArr[i] = ParseUtil.fileToEncodedURL(file);
            } catch (IOException unused2) {
            }
            i = i2;
        }
        if (countTokens == i) {
            return urlArr;
        }
        URL[] urlArr2 = new URL[i];
        System.arraycopy((Object) urlArr, 0, (Object) urlArr2, 0, i);
        return urlArr2;
    }

    public URL checkURL(URL url) {
        try {
            check(url);
            return url;
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = r7.openConnection();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void check(java.net.URL r7) throws java.p026io.IOException {
        /*
            java.lang.SecurityManager r0 = java.lang.System.getSecurityManager()
            if (r0 == 0) goto L_0x0055
            java.net.URLConnection r1 = r7.openConnection()
            java.security.Permission r2 = r1.getPermission()
            if (r2 == 0) goto L_0x0055
            r0.checkPermission(r2)     // Catch:{ SecurityException -> 0x0014 }
            goto L_0x0055
        L_0x0014:
            r3 = move-exception
            boolean r4 = r2 instanceof java.p026io.FilePermission
            r5 = -1
            if (r4 == 0) goto L_0x002e
            java.lang.String r4 = r2.getActions()
            java.lang.String r6 = "read"
            int r4 = r4.indexOf((java.lang.String) r6)
            if (r4 == r5) goto L_0x002e
            java.lang.String r7 = r2.getName()
            r0.checkRead((java.lang.String) r7)
            goto L_0x0055
        L_0x002e:
            boolean r4 = r2 instanceof java.net.SocketPermission
            if (r4 == 0) goto L_0x0054
            java.lang.String r2 = r2.getActions()
            java.lang.String r4 = "connect"
            int r2 = r2.indexOf((java.lang.String) r4)
            if (r2 == r5) goto L_0x0054
            boolean r2 = r1 instanceof java.net.JarURLConnection
            if (r2 == 0) goto L_0x0048
            java.net.JarURLConnection r1 = (java.net.JarURLConnection) r1
            java.net.URL r7 = r1.getJarFileURL()
        L_0x0048:
            java.lang.String r1 = r7.getHost()
            int r7 = r7.getPort()
            r0.checkConnect(r1, r7)
            goto L_0x0055
        L_0x0054:
            throw r3
        L_0x0055:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.URLClassPath.check(java.net.URL):void");
    }

    private static class Loader implements Closeable {
        /* access modifiers changed from: private */
        public final URL base;
        private JarFile jarfile;

        /* access modifiers changed from: package-private */
        public URL[] getClassPath() throws IOException {
            return null;
        }

        Loader(URL url) {
            this.base = url;
        }

        /* access modifiers changed from: package-private */
        public URL getBaseURL() {
            return this.base;
        }

        /* access modifiers changed from: package-private */
        public URL findResource(String str, boolean z) {
            try {
                URL url = new URL(this.base, ParseUtil.encodePath(str, false));
                if (z) {
                    try {
                        URLClassPath.check(url);
                    } catch (Exception unused) {
                        return null;
                    }
                }
                URLConnection openConnection = url.openConnection();
                if (openConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
                    httpURLConnection.setRequestMethod("HEAD");
                    if (httpURLConnection.getResponseCode() >= 400) {
                        return null;
                    }
                } else {
                    openConnection.setUseCaches(false);
                    openConnection.getInputStream().close();
                }
                return url;
            } catch (MalformedURLException unused2) {
                throw new IllegalArgumentException(ZoneGetter.KEY_DISPLAYNAME);
            }
        }

        /* access modifiers changed from: package-private */
        public Resource getResource(final String str, boolean z) {
            try {
                final URL url = new URL(this.base, ParseUtil.encodePath(str, false));
                if (z) {
                    try {
                        URLClassPath.check(url);
                    } catch (Exception unused) {
                        return null;
                    }
                }
                final URLConnection openConnection = url.openConnection();
                openConnection.getInputStream();
                if (openConnection instanceof JarURLConnection) {
                    this.jarfile = JarLoader.checkJar(((JarURLConnection) openConnection).getJarFile());
                }
                return new Resource() {
                    public String getName() {
                        return str;
                    }

                    public URL getURL() {
                        return url;
                    }

                    public URL getCodeSourceURL() {
                        return Loader.this.base;
                    }

                    public InputStream getInputStream() throws IOException {
                        return openConnection.getInputStream();
                    }

                    public int getContentLength() throws IOException {
                        return openConnection.getContentLength();
                    }
                };
            } catch (MalformedURLException unused2) {
                throw new IllegalArgumentException(ZoneGetter.KEY_DISPLAYNAME);
            }
        }

        /* access modifiers changed from: package-private */
        public Resource getResource(String str) {
            return getResource(str, true);
        }

        public void close() throws IOException {
            JarFile jarFile = this.jarfile;
            if (jarFile != null) {
                jarFile.close();
            }
        }
    }

    static class JarLoader extends Loader {
        /* access modifiers changed from: private */
        public final AccessControlContext acc;
        private boolean closed = false;
        /* access modifiers changed from: private */
        public final URL csu;
        /* access modifiers changed from: private */
        public URLStreamHandler handler;
        /* access modifiers changed from: private */
        public JarIndex index;
        /* access modifiers changed from: private */
        public JarFile jar;
        /* access modifiers changed from: private */
        public final HashMap<String, Loader> lmap;
        /* access modifiers changed from: private */
        public MetaIndex metaIndex;

        private void parseExtensionsDependencies() throws IOException {
        }

        JarLoader(URL url, URLStreamHandler uRLStreamHandler, HashMap<String, Loader> hashMap, AccessControlContext accessControlContext) throws IOException {
            super(new URL("jar", "", -1, url + "!/", uRLStreamHandler));
            this.csu = url;
            this.handler = uRLStreamHandler;
            this.lmap = hashMap;
            this.acc = accessControlContext;
            if (!isOptimizable(url)) {
                ensureOpen();
                return;
            }
            String file = url.getFile();
            if (file != null) {
                File file2 = new File(ParseUtil.decode(file));
                MetaIndex forJar = MetaIndex.forJar(file2);
                this.metaIndex = forJar;
                if (forJar != null && !file2.exists()) {
                    this.metaIndex = null;
                }
            }
            if (this.metaIndex == null) {
                ensureOpen();
            }
        }

        public void close() throws IOException {
            if (!this.closed) {
                this.closed = true;
                ensureOpen();
                this.jar.close();
            }
        }

        /* access modifiers changed from: package-private */
        public JarFile getJarFile() {
            return this.jar;
        }

        private boolean isOptimizable(URL url) {
            return "file".equals(url.getProtocol());
        }

        private void ensureOpen() throws IOException {
            if (this.jar == null) {
                try {
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                        public Void run() throws IOException {
                            if (URLClassPath.DEBUG) {
                                PrintStream printStream = System.err;
                                printStream.println("Opening " + JarLoader.this.csu);
                                Thread.dumpStack();
                            }
                            JarLoader jarLoader = JarLoader.this;
                            jarLoader.jar = jarLoader.getJarFile(jarLoader.csu);
                            JarLoader jarLoader2 = JarLoader.this;
                            jarLoader2.index = JarIndex.getJarIndex(jarLoader2.jar, JarLoader.this.metaIndex);
                            if (JarLoader.this.index != null) {
                                String[] jarFiles = JarLoader.this.index.getJarFiles();
                                for (int i = 0; i < jarFiles.length; i++) {
                                    try {
                                        String urlNoFragString = URLUtil.urlNoFragString(new URL(JarLoader.this.csu, jarFiles[i]));
                                        if (!JarLoader.this.lmap.containsKey(urlNoFragString)) {
                                            JarLoader.this.lmap.put(urlNoFragString, null);
                                        }
                                    } catch (MalformedURLException unused) {
                                    }
                                }
                            }
                            return null;
                        }
                    }, this.acc);
                } catch (PrivilegedActionException e) {
                    throw ((IOException) e.getException());
                }
            }
        }

        static JarFile checkJar(JarFile jarFile) throws IOException {
            if (System.getSecurityManager() == null || URLClassPath.DISABLE_JAR_CHECKING || jarFile.startsWithLocHeader()) {
                return jarFile;
            }
            IOException iOException = new IOException("Invalid Jar file");
            try {
                jarFile.close();
            } catch (IOException e) {
                iOException.addSuppressed(e);
            }
            throw iOException;
        }

        /* access modifiers changed from: private */
        public JarFile getJarFile(URL url) throws IOException {
            if (isOptimizable(url)) {
                FileURLMapper fileURLMapper = new FileURLMapper(url);
                if (fileURLMapper.exists()) {
                    return checkJar(new JarFile(fileURLMapper.getPath()));
                }
                throw new FileNotFoundException(fileURLMapper.getPath());
            }
            URLConnection openConnection = getBaseURL().openConnection();
            openConnection.setRequestProperty(URLClassPath.USER_AGENT_JAVA_VERSION, URLClassPath.JAVA_VERSION);
            return checkJar(((JarURLConnection) openConnection).getJarFile());
        }

        /* access modifiers changed from: package-private */
        public JarIndex getIndex() {
            try {
                ensureOpen();
                return this.index;
            } catch (IOException e) {
                throw new InternalError((Throwable) e);
            }
        }

        /* access modifiers changed from: package-private */
        public Resource checkResource(final String str, boolean z, final JarEntry jarEntry) {
            try {
                final URL url = new URL(getBaseURL(), ParseUtil.encodePath(str, false));
                if (z) {
                    URLClassPath.check(url);
                }
                return new Resource() {
                    public String getName() {
                        return str;
                    }

                    public URL getURL() {
                        return url;
                    }

                    public URL getCodeSourceURL() {
                        return JarLoader.this.csu;
                    }

                    public InputStream getInputStream() throws IOException {
                        return JarLoader.this.jar.getInputStream(jarEntry);
                    }

                    public int getContentLength() {
                        return (int) jarEntry.getSize();
                    }

                    public Manifest getManifest() throws IOException {
                        return JarLoader.this.jar.getManifest();
                    }

                    public Certificate[] getCertificates() {
                        return jarEntry.getCertificates();
                    }

                    public CodeSigner[] getCodeSigners() {
                        return jarEntry.getCodeSigners();
                    }
                };
            } catch (IOException | MalformedURLException | AccessControlException unused) {
                return null;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean validIndex(String str) {
            int lastIndexOf = str.lastIndexOf("/");
            if (lastIndexOf != -1) {
                str = str.substring(0, lastIndexOf);
            }
            Enumeration<JarEntry> entries = this.jar.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                int lastIndexOf2 = name.lastIndexOf("/");
                if (lastIndexOf2 != -1) {
                    name = name.substring(0, lastIndexOf2);
                }
                if (name.equals(str)) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public URL findResource(String str, boolean z) {
            Resource resource = getResource(str, z);
            if (resource != null) {
                return resource.getURL();
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public Resource getResource(String str, boolean z) {
            MetaIndex metaIndex2 = this.metaIndex;
            if (metaIndex2 != null && !metaIndex2.mayContain(str)) {
                return null;
            }
            try {
                ensureOpen();
                JarEntry jarEntry = this.jar.getJarEntry(str);
                if (jarEntry != null) {
                    return checkResource(str, z, jarEntry);
                }
                if (this.index == null) {
                    return null;
                }
                return getResource(str, z, new HashSet());
            } catch (IOException e) {
                throw new InternalError((Throwable) e);
            }
        }

        /* access modifiers changed from: package-private */
        public Resource getResource(String str, boolean z, Set<String> set) {
            Resource resource;
            String str2;
            String str3 = str;
            boolean z2 = z;
            Set<String> set2 = set;
            LinkedList<String> linkedList = this.index.get(str3);
            if (linkedList == null) {
                return null;
            }
            int i = 0;
            while (true) {
                int size = linkedList.size();
                String[] strArr = (String[]) linkedList.toArray(new String[size]);
                while (i < size) {
                    int i2 = i + 1;
                    String str4 = strArr[i];
                    try {
                        final URL url = new URL(this.csu, str4);
                        String urlNoFragString = URLUtil.urlNoFragString(url);
                        JarLoader jarLoader = (JarLoader) this.lmap.get(urlNoFragString);
                        if (jarLoader == null) {
                            jarLoader = (JarLoader) AccessController.doPrivileged(new PrivilegedExceptionAction<JarLoader>() {
                                public JarLoader run() throws IOException {
                                    return new JarLoader(url, JarLoader.this.handler, JarLoader.this.lmap, JarLoader.this.acc);
                                }
                            }, this.acc);
                            JarIndex index2 = jarLoader.getIndex();
                            if (index2 != null) {
                                int lastIndexOf = str4.lastIndexOf("/");
                                JarIndex jarIndex = this.index;
                                if (lastIndexOf == -1) {
                                    str2 = null;
                                } else {
                                    str2 = str4.substring(0, lastIndexOf + 1);
                                }
                                index2.merge(jarIndex, str2);
                            }
                            this.lmap.put(urlNoFragString, jarLoader);
                        }
                        boolean z3 = !set2.add(URLUtil.urlNoFragString(url));
                        if (!z3) {
                            try {
                                jarLoader.ensureOpen();
                                JarEntry jarEntry = jarLoader.jar.getJarEntry(str3);
                                if (jarEntry != null) {
                                    return jarLoader.checkResource(str3, z2, jarEntry);
                                }
                                if (!jarLoader.validIndex(str3)) {
                                    throw new InvalidJarIndexException("Invalid index");
                                }
                            } catch (IOException e) {
                                throw new InternalError((Throwable) e);
                            }
                        }
                        if (!(z3 || jarLoader == this || jarLoader.getIndex() == null || (resource = jarLoader.getResource(str3, z2, set2)) == null)) {
                            return resource;
                        }
                    } catch (MalformedURLException | PrivilegedActionException unused) {
                    }
                    i = i2;
                }
                linkedList = this.index.get(str3);
                if (i >= linkedList.size()) {
                    return null;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public URL[] getClassPath() throws IOException {
            Manifest manifest;
            Attributes mainAttributes;
            String value;
            if (this.index != null || this.metaIndex != null) {
                return null;
            }
            ensureOpen();
            parseExtensionsDependencies();
            if (!this.jar.hasClassPathAttribute() || (manifest = this.jar.getManifest()) == null || (mainAttributes = manifest.getMainAttributes()) == null || (value = mainAttributes.getValue(Attributes.Name.CLASS_PATH)) == null) {
                return null;
            }
            return parseClassPath(this.csu, value);
        }

        private URL[] parseClassPath(URL url, String str) throws MalformedURLException {
            StringTokenizer stringTokenizer = new StringTokenizer(str);
            URL[] urlArr = new URL[stringTokenizer.countTokens()];
            int i = 0;
            while (stringTokenizer.hasMoreTokens()) {
                urlArr[i] = new URL(url, stringTokenizer.nextToken());
                i++;
            }
            return urlArr;
        }
    }

    private static class FileLoader extends Loader {
        private File dir;

        FileLoader(URL url) throws IOException {
            super(url);
            if ("file".equals(url.getProtocol())) {
                this.dir = new File(ParseUtil.decode(url.getFile().replace('/', File.separatorChar))).getCanonicalFile();
                return;
            }
            throw new IllegalArgumentException("url");
        }

        /* access modifiers changed from: package-private */
        public URL findResource(String str, boolean z) {
            Resource resource = getResource(str, z);
            if (resource != null) {
                return resource.getURL();
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public Resource getResource(final String str, boolean z) {
            final File file;
            try {
                URL url = new URL(getBaseURL(), BaseIconCache.EMPTY_CLASS_NAME);
                final URL url2 = new URL(getBaseURL(), ParseUtil.encodePath(str, false));
                if (!url2.getFile().startsWith(url.getFile())) {
                    return null;
                }
                if (z) {
                    URLClassPath.check(url2);
                }
                if (str.indexOf("..") != -1) {
                    file = new File(this.dir, str.replace('/', File.separatorChar)).getCanonicalFile();
                    if (!file.getPath().startsWith(this.dir.getPath())) {
                        return null;
                    }
                } else {
                    file = new File(this.dir, str.replace('/', File.separatorChar));
                }
                if (file.exists()) {
                    return new Resource() {
                        public String getName() {
                            return str;
                        }

                        public URL getURL() {
                            return url2;
                        }

                        public URL getCodeSourceURL() {
                            return FileLoader.this.getBaseURL();
                        }

                        public InputStream getInputStream() throws IOException {
                            return new FileInputStream(file);
                        }

                        public int getContentLength() throws IOException {
                            return (int) file.length();
                        }
                    };
                }
                return null;
            } catch (Exception unused) {
            }
        }
    }
}
