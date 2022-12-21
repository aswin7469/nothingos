package java.net;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.AccessController;
import java.security.Permission;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import sun.net.www.MessageHeader;
import sun.security.action.GetPropertyAction;
import sun.security.util.SecurityConstants;
import sun.security.x509.InvalidityDateExtension;

public abstract class URLConnection {
    private static final String contentClassPrefix = "sun.net.www.content";
    private static final String contentPathProp = "java.content.handler.pkgs";
    private static boolean defaultAllowUserInteraction = false;
    private static boolean defaultUseCaches = true;
    static ContentHandlerFactory factory;
    private static FileNameMap fileNameMap;
    private static Hashtable<String, ContentHandler> handlers = new Hashtable<>();
    protected boolean allowUserInteraction = defaultAllowUserInteraction;
    private int connectTimeout;
    protected boolean connected = false;
    protected boolean doInput = true;
    protected boolean doOutput = false;
    protected long ifModifiedSince = 0;
    private int readTimeout;
    private MessageHeader requests;
    protected URL url;
    protected boolean useCaches = defaultUseCaches;

    @Deprecated
    public static String getDefaultRequestProperty(String str) {
        return null;
    }

    @Deprecated
    public static void setDefaultRequestProperty(String str, String str2) {
    }

    public abstract void connect() throws IOException;

    public String getHeaderField(int i) {
        return null;
    }

    public String getHeaderField(String str) {
        return null;
    }

    public String getHeaderFieldKey(int i) {
        return null;
    }

    public static synchronized FileNameMap getFileNameMap() {
        FileNameMap fileNameMap2;
        synchronized (URLConnection.class) {
            if (fileNameMap == null) {
                fileNameMap = new DefaultFileNameMap();
            }
            fileNameMap2 = fileNameMap;
        }
        return fileNameMap2;
    }

    public static void setFileNameMap(FileNameMap fileNameMap2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        fileNameMap = fileNameMap2;
    }

    public void setConnectTimeout(int i) {
        if (i >= 0) {
            this.connectTimeout = i;
            return;
        }
        throw new IllegalArgumentException("timeout can not be negative");
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setReadTimeout(int i) {
        if (i >= 0) {
            this.readTimeout = i;
            return;
        }
        throw new IllegalArgumentException("timeout can not be negative");
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    protected URLConnection(URL url2) {
        this.url = url2;
    }

    public URL getURL() {
        return this.url;
    }

    public int getContentLength() {
        long contentLengthLong = getContentLengthLong();
        if (contentLengthLong > 2147483647L) {
            return -1;
        }
        return (int) contentLengthLong;
    }

    public long getContentLengthLong() {
        return getHeaderFieldLong("content-length", -1);
    }

    public String getContentType() {
        return getHeaderField("content-type");
    }

    public String getContentEncoding() {
        return getHeaderField("content-encoding");
    }

    public long getExpiration() {
        return getHeaderFieldDate("expires", 0);
    }

    public long getDate() {
        return getHeaderFieldDate(InvalidityDateExtension.DATE, 0);
    }

    public long getLastModified() {
        return getHeaderFieldDate("last-modified", 0);
    }

    public Map<String, List<String>> getHeaderFields() {
        return Collections.emptyMap();
    }

    public int getHeaderFieldInt(String str, int i) {
        try {
            return Integer.parseInt(getHeaderField(str));
        } catch (Exception unused) {
            return i;
        }
    }

    public long getHeaderFieldLong(String str, long j) {
        try {
            return Long.parseLong(getHeaderField(str));
        } catch (Exception unused) {
            return j;
        }
    }

    public long getHeaderFieldDate(String str, long j) {
        try {
            return Date.parse(getHeaderField(str));
        } catch (Exception unused) {
            return j;
        }
    }

    public Object getContent() throws IOException {
        getInputStream();
        return getContentHandler().getContent(this);
    }

    public Object getContent(Class[] clsArr) throws IOException {
        getInputStream();
        return getContentHandler().getContent(this, clsArr);
    }

    public Permission getPermission() throws IOException {
        return SecurityConstants.ALL_PERMISSION;
    }

    public InputStream getInputStream() throws IOException {
        throw new UnknownServiceException("protocol doesn't support input");
    }

    public OutputStream getOutputStream() throws IOException {
        throw new UnknownServiceException("protocol doesn't support output");
    }

    public String toString() {
        return getClass().getName() + ":" + this.url;
    }

    public void setDoInput(boolean z) {
        if (!this.connected) {
            this.doInput = z;
            return;
        }
        throw new IllegalStateException("Already connected");
    }

    public boolean getDoInput() {
        return this.doInput;
    }

    public void setDoOutput(boolean z) {
        if (!this.connected) {
            this.doOutput = z;
            return;
        }
        throw new IllegalStateException("Already connected");
    }

    public boolean getDoOutput() {
        return this.doOutput;
    }

    public void setAllowUserInteraction(boolean z) {
        if (!this.connected) {
            this.allowUserInteraction = z;
            return;
        }
        throw new IllegalStateException("Already connected");
    }

    public boolean getAllowUserInteraction() {
        return this.allowUserInteraction;
    }

    public static void setDefaultAllowUserInteraction(boolean z) {
        defaultAllowUserInteraction = z;
    }

    public static boolean getDefaultAllowUserInteraction() {
        return defaultAllowUserInteraction;
    }

    public void setUseCaches(boolean z) {
        if (!this.connected) {
            this.useCaches = z;
            return;
        }
        throw new IllegalStateException("Already connected");
    }

    public boolean getUseCaches() {
        return this.useCaches;
    }

    public void setIfModifiedSince(long j) {
        if (!this.connected) {
            this.ifModifiedSince = j;
            return;
        }
        throw new IllegalStateException("Already connected");
    }

    public long getIfModifiedSince() {
        return this.ifModifiedSince;
    }

    public boolean getDefaultUseCaches() {
        return defaultUseCaches;
    }

    public void setDefaultUseCaches(boolean z) {
        defaultUseCaches = z;
    }

    public void setRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        } else if (str != null) {
            if (this.requests == null) {
                this.requests = new MessageHeader();
            }
            this.requests.set(str, str2);
        } else {
            throw new NullPointerException("key is null");
        }
    }

    public void addRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        } else if (str != null) {
            if (this.requests == null) {
                this.requests = new MessageHeader();
            }
            this.requests.add(str, str2);
        } else {
            throw new NullPointerException("key is null");
        }
    }

    public String getRequestProperty(String str) {
        if (!this.connected) {
            MessageHeader messageHeader = this.requests;
            if (messageHeader == null) {
                return null;
            }
            return messageHeader.findValue(str);
        }
        throw new IllegalStateException("Already connected");
    }

    public Map<String, List<String>> getRequestProperties() {
        if (!this.connected) {
            MessageHeader messageHeader = this.requests;
            if (messageHeader == null) {
                return Collections.emptyMap();
            }
            return messageHeader.getHeaders((String[]) null);
        }
        throw new IllegalStateException("Already connected");
    }

    public static synchronized void setContentHandlerFactory(ContentHandlerFactory contentHandlerFactory) {
        synchronized (URLConnection.class) {
            if (factory == null) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkSetFactory();
                }
                factory = contentHandlerFactory;
            } else {
                throw new Error("factory already defined");
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x004d, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.net.ContentHandler getContentHandler() throws java.p026io.IOException {
        /*
            r3 = this;
            monitor-enter(r3)
            java.lang.String r0 = r3.getContentType()     // Catch:{ all -> 0x004e }
            java.lang.String r0 = r3.stripOffParameters(r0)     // Catch:{ all -> 0x004e }
            if (r0 != 0) goto L_0x001f
            java.net.URL r0 = r3.url     // Catch:{ all -> 0x004e }
            java.lang.String r0 = r0.getFile()     // Catch:{ all -> 0x004e }
            java.lang.String r0 = guessContentTypeFromName(r0)     // Catch:{ all -> 0x004e }
            if (r0 != 0) goto L_0x001f
            java.io.InputStream r0 = r3.getInputStream()     // Catch:{ all -> 0x004e }
            java.lang.String r0 = guessContentTypeFromStream(r0)     // Catch:{ all -> 0x004e }
        L_0x001f:
            if (r0 != 0) goto L_0x0025
            java.net.ContentHandler r0 = java.net.UnknownContentHandler.INSTANCE     // Catch:{ all -> 0x004e }
            monitor-exit(r3)
            return r0
        L_0x0025:
            java.util.Hashtable<java.lang.String, java.net.ContentHandler> r1 = handlers     // Catch:{ Exception -> 0x0031 }
            java.lang.Object r1 = r1.get(r0)     // Catch:{ Exception -> 0x0031 }
            java.net.ContentHandler r1 = (java.net.ContentHandler) r1     // Catch:{ Exception -> 0x0031 }
            if (r1 == 0) goto L_0x0032
            monitor-exit(r3)
            return r1
        L_0x0031:
            r1 = 0
        L_0x0032:
            java.net.ContentHandlerFactory r2 = factory     // Catch:{ all -> 0x004e }
            if (r2 == 0) goto L_0x003a
            java.net.ContentHandler r1 = r2.createContentHandler(r0)     // Catch:{ all -> 0x004e }
        L_0x003a:
            if (r1 != 0) goto L_0x004c
            java.net.ContentHandler r1 = r3.lookupContentHandlerClassFor(r0)     // Catch:{ Exception -> 0x0041 }
            goto L_0x0047
        L_0x0041:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x004e }
            java.net.ContentHandler r1 = java.net.UnknownContentHandler.INSTANCE     // Catch:{ all -> 0x004e }
        L_0x0047:
            java.util.Hashtable<java.lang.String, java.net.ContentHandler> r2 = handlers     // Catch:{ all -> 0x004e }
            r2.put(r0, r1)     // Catch:{ all -> 0x004e }
        L_0x004c:
            monitor-exit(r3)
            return r1
        L_0x004e:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URLConnection.getContentHandler():java.net.ContentHandler");
    }

    private String stripOffParameters(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(59);
        return indexOf > 0 ? str.substring(0, indexOf) : str;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:9|10|(1:12)(1:13)) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        r1 = java.lang.ClassLoader.getSystemClassLoader();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003b, code lost:
        if (r1 != null) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003d, code lost:
        r2 = r1.loadClass(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0042, code lost:
        r2 = null;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0037 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.net.ContentHandler lookupContentHandlerClassFor(java.lang.String r3) throws java.lang.InstantiationException, java.lang.IllegalAccessException, java.lang.ClassNotFoundException {
        /*
            r2 = this;
            java.lang.String r3 = r2.typeToPackageName(r3)
            java.lang.String r2 = r2.getContentHandlerPkgPrefixes()
            java.util.StringTokenizer r0 = new java.util.StringTokenizer
            java.lang.String r1 = "|"
            r0.<init>(r2, r1)
        L_0x0010:
            boolean r2 = r0.hasMoreTokens()
            if (r2 == 0) goto L_0x004c
            java.lang.String r2 = r0.nextToken()
            java.lang.String r2 = r2.trim()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0010 }
            r1.<init>()     // Catch:{ Exception -> 0x0010 }
            r1.append((java.lang.String) r2)     // Catch:{ Exception -> 0x0010 }
            java.lang.String r2 = "."
            r1.append((java.lang.String) r2)     // Catch:{ Exception -> 0x0010 }
            r1.append((java.lang.String) r3)     // Catch:{ Exception -> 0x0010 }
            java.lang.String r2 = r1.toString()     // Catch:{ Exception -> 0x0010 }
            java.lang.Class r2 = java.lang.Class.forName(r2)     // Catch:{ ClassNotFoundException -> 0x0037 }
            goto L_0x0043
        L_0x0037:
            java.lang.ClassLoader r1 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ Exception -> 0x0010 }
            if (r1 == 0) goto L_0x0042
            java.lang.Class r2 = r1.loadClass(r2)     // Catch:{ Exception -> 0x0010 }
            goto L_0x0043
        L_0x0042:
            r2 = 0
        L_0x0043:
            if (r2 == 0) goto L_0x0010
            java.lang.Object r2 = r2.newInstance()     // Catch:{ Exception -> 0x0010 }
            java.net.ContentHandler r2 = (java.net.ContentHandler) r2     // Catch:{ Exception -> 0x0010 }
            return r2
        L_0x004c:
            java.net.ContentHandler r2 = java.net.UnknownContentHandler.INSTANCE
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URLConnection.lookupContentHandlerClassFor(java.lang.String):java.net.ContentHandler");
    }

    private String typeToPackageName(String str) {
        String lowerCase = str.toLowerCase();
        int length = lowerCase.length();
        char[] cArr = new char[length];
        lowerCase.getChars(0, length, cArr, 0);
        for (int i = 0; i < length; i++) {
            char c = cArr[i];
            if (c == '/') {
                cArr[i] = '.';
            } else if (('A' > c || c > 'Z') && (('a' > c || c > 'z') && ('0' > c || c > '9'))) {
                cArr[i] = '_';
            }
        }
        return new String(cArr);
    }

    private String getContentHandlerPkgPrefixes() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction(contentPathProp, ""));
        if (str != "") {
            str = str + "|";
        }
        return str + contentClassPrefix;
    }

    public static String guessContentTypeFromName(String str) {
        return getFileNameMap().getContentTypeFor(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00b1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String guessContentTypeFromStream(java.p026io.InputStream r20) throws java.p026io.IOException {
        /*
            boolean r0 = r20.markSupported()
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            r0 = 16
            r2 = r20
            r2.mark(r0)
            int r0 = r20.read()
            int r3 = r20.read()
            int r4 = r20.read()
            int r5 = r20.read()
            int r6 = r20.read()
            int r7 = r20.read()
            int r8 = r20.read()
            int r9 = r20.read()
            int r10 = r20.read()
            int r11 = r20.read()
            int r12 = r20.read()
            int r13 = r20.read()
            int r14 = r20.read()
            int r15 = r20.read()
            int r16 = r20.read()
            int r1 = r20.read()
            r20.reset()
            r2 = 202(0xca, float:2.83E-43)
            r17 = r1
            r1 = 254(0xfe, float:3.56E-43)
            if (r0 != r2) goto L_0x0067
            if (r3 != r1) goto L_0x0067
            r2 = 186(0xba, float:2.6E-43)
            if (r4 != r2) goto L_0x0067
            r2 = 190(0xbe, float:2.66E-43)
            if (r5 != r2) goto L_0x0067
            java.lang.String r0 = "application/java-vm"
            return r0
        L_0x0067:
            r2 = 172(0xac, float:2.41E-43)
            if (r0 != r2) goto L_0x0072
            r2 = 237(0xed, float:3.32E-43)
            if (r3 != r2) goto L_0x0072
            java.lang.String r0 = "application/x-java-serialized-object"
            return r0
        L_0x0072:
            r1 = 33
            java.lang.String r18 = "application/xml"
            r2 = 60
            r19 = r15
            if (r0 != r2) goto L_0x00ed
            if (r3 == r1) goto L_0x00e9
            r1 = 104(0x68, float:1.46E-43)
            r2 = 108(0x6c, float:1.51E-43)
            r15 = 109(0x6d, float:1.53E-43)
            if (r3 != r1) goto L_0x009b
            r1 = 116(0x74, float:1.63E-43)
            if (r4 != r1) goto L_0x008e
            if (r5 != r15) goto L_0x008e
            if (r6 == r2) goto L_0x00e9
        L_0x008e:
            r1 = 101(0x65, float:1.42E-43)
            if (r4 != r1) goto L_0x009b
            r1 = 97
            if (r5 != r1) goto L_0x009b
            r1 = 100
            if (r6 == r1) goto L_0x00e9
            goto L_0x009d
        L_0x009b:
            r1 = 100
        L_0x009d:
            r2 = 98
            if (r3 != r2) goto L_0x00ab
            r2 = 111(0x6f, float:1.56E-43)
            if (r4 != r2) goto L_0x00ab
            if (r5 != r1) goto L_0x00ab
            r1 = 121(0x79, float:1.7E-43)
            if (r6 == r1) goto L_0x00e9
        L_0x00ab:
            r1 = 72
            r2 = 68
            if (r3 != r1) goto L_0x00c7
            r1 = 84
            if (r4 != r1) goto L_0x00bd
            r1 = 77
            if (r5 != r1) goto L_0x00bd
            r1 = 76
            if (r6 == r1) goto L_0x00e9
        L_0x00bd:
            r1 = 69
            if (r4 != r1) goto L_0x00c7
            r1 = 65
            if (r5 != r1) goto L_0x00c7
            if (r6 == r2) goto L_0x00e9
        L_0x00c7:
            r1 = 66
            if (r3 != r1) goto L_0x00d6
            r1 = 79
            if (r4 != r1) goto L_0x00d6
            if (r5 != r2) goto L_0x00d6
            r1 = 89
            if (r6 != r1) goto L_0x00d6
            goto L_0x00e9
        L_0x00d6:
            r1 = 63
            if (r3 != r1) goto L_0x00ed
            r1 = 120(0x78, float:1.68E-43)
            if (r4 != r1) goto L_0x00ed
            if (r5 != r15) goto L_0x00ed
            r1 = 108(0x6c, float:1.51E-43)
            if (r6 != r1) goto L_0x00ed
            r1 = 32
            if (r7 != r1) goto L_0x00ed
            return r18
        L_0x00e9:
            java.lang.String r0 = "text/html"
            return r0
        L_0x00ed:
            r1 = 239(0xef, float:3.35E-43)
            if (r0 != r1) goto L_0x0106
            r1 = 187(0xbb, float:2.62E-43)
            if (r3 != r1) goto L_0x0106
            r1 = 191(0xbf, float:2.68E-43)
            if (r4 != r1) goto L_0x0106
            r1 = 60
            if (r5 != r1) goto L_0x0106
            r1 = 63
            if (r6 != r1) goto L_0x0106
            r1 = 120(0x78, float:1.68E-43)
            if (r7 != r1) goto L_0x0106
            return r18
        L_0x0106:
            r1 = 255(0xff, float:3.57E-43)
            r2 = 254(0xfe, float:3.56E-43)
            if (r0 != r2) goto L_0x0121
            if (r3 != r1) goto L_0x0121
            if (r4 != 0) goto L_0x0121
            r2 = 60
            if (r5 != r2) goto L_0x0121
            if (r6 != 0) goto L_0x0121
            r2 = 63
            if (r7 != r2) goto L_0x0121
            if (r8 != 0) goto L_0x0121
            r2 = 120(0x78, float:1.68E-43)
            if (r9 != r2) goto L_0x0121
            return r18
        L_0x0121:
            if (r0 != r1) goto L_0x013a
            r2 = 254(0xfe, float:3.56E-43)
            if (r3 != r2) goto L_0x013a
            r2 = 60
            if (r4 != r2) goto L_0x013a
            if (r5 != 0) goto L_0x013a
            r2 = 63
            if (r6 != r2) goto L_0x013a
            if (r7 != 0) goto L_0x013a
            r2 = 120(0x78, float:1.68E-43)
            if (r8 != r2) goto L_0x013a
            if (r9 != 0) goto L_0x013a
            return r18
        L_0x013a:
            if (r0 != 0) goto L_0x0165
            if (r3 != 0) goto L_0x0165
            r2 = 254(0xfe, float:3.56E-43)
            if (r4 != r2) goto L_0x0165
            if (r5 != r1) goto L_0x0165
            if (r6 != 0) goto L_0x0165
            if (r7 != 0) goto L_0x0165
            if (r8 != 0) goto L_0x0165
            r2 = 60
            if (r9 != r2) goto L_0x0165
            if (r10 != 0) goto L_0x0165
            if (r11 != 0) goto L_0x0165
            if (r12 != 0) goto L_0x0165
            r2 = 63
            if (r13 != r2) goto L_0x0165
            if (r14 != 0) goto L_0x0165
            if (r19 != 0) goto L_0x0165
            if (r16 != 0) goto L_0x0165
            r2 = r17
            r15 = 120(0x78, float:1.68E-43)
            if (r2 != r15) goto L_0x0167
            return r18
        L_0x0165:
            r2 = r17
        L_0x0167:
            if (r0 != r1) goto L_0x0190
            r15 = 254(0xfe, float:3.56E-43)
            if (r3 != r15) goto L_0x0190
            if (r4 != 0) goto L_0x0190
            if (r5 != 0) goto L_0x0190
            r15 = 60
            if (r6 != r15) goto L_0x0190
            if (r7 != 0) goto L_0x0190
            if (r8 != 0) goto L_0x0190
            if (r9 != 0) goto L_0x0190
            r15 = 63
            if (r10 != r15) goto L_0x0190
            if (r11 != 0) goto L_0x0190
            if (r12 != 0) goto L_0x0190
            if (r13 != 0) goto L_0x0190
            r13 = 120(0x78, float:1.68E-43)
            if (r14 != r13) goto L_0x0190
            if (r19 != 0) goto L_0x0190
            if (r16 != 0) goto L_0x0190
            if (r2 != 0) goto L_0x0190
            return r18
        L_0x0190:
            r2 = 73
            r13 = 71
            r14 = 70
            if (r0 != r13) goto L_0x01a3
            if (r3 != r2) goto L_0x01a3
            if (r4 != r14) goto L_0x01a3
            r15 = 56
            if (r5 != r15) goto L_0x01a3
            java.lang.String r0 = "image/gif"
            return r0
        L_0x01a3:
            r15 = 35
            r14 = 102(0x66, float:1.43E-43)
            if (r0 != r15) goto L_0x01b6
            r15 = 100
            if (r3 != r15) goto L_0x01b6
            r15 = 101(0x65, float:1.42E-43)
            if (r4 != r15) goto L_0x01b6
            if (r5 != r14) goto L_0x01b6
            java.lang.String r0 = "image/x-bitmap"
            return r0
        L_0x01b6:
            r15 = 80
            r2 = 33
            if (r0 != r2) goto L_0x01d1
            r2 = 32
            if (r3 != r2) goto L_0x01d1
            r2 = 88
            if (r4 != r2) goto L_0x01d1
            if (r5 != r15) goto L_0x01d1
            r2 = 77
            if (r6 != r2) goto L_0x01d1
            r2 = 50
            if (r7 != r2) goto L_0x01d1
            java.lang.String r0 = "image/x-pixmap"
            return r0
        L_0x01d1:
            r2 = 137(0x89, float:1.92E-43)
            if (r0 != r2) goto L_0x01f0
            if (r3 != r15) goto L_0x01f0
            r2 = 78
            if (r4 != r2) goto L_0x01f0
            if (r5 != r13) goto L_0x01f0
            r2 = 13
            if (r6 != r2) goto L_0x01f0
            r2 = 10
            if (r7 != r2) goto L_0x01f0
            r2 = 26
            if (r8 != r2) goto L_0x01f0
            r2 = 10
            if (r9 != r2) goto L_0x01f0
            java.lang.String r0 = "image/png"
            return r0
        L_0x01f0:
            if (r0 != r1) goto L_0x021b
            r2 = 216(0xd8, float:3.03E-43)
            if (r3 != r2) goto L_0x021b
            if (r4 != r1) goto L_0x021b
            r1 = 224(0xe0, float:3.14E-43)
            if (r5 == r1) goto L_0x0218
            r1 = 238(0xee, float:3.34E-43)
            if (r5 != r1) goto L_0x0201
            goto L_0x0218
        L_0x0201:
            r1 = 225(0xe1, float:3.15E-43)
            if (r5 != r1) goto L_0x021b
            r1 = 69
            if (r8 != r1) goto L_0x021b
            r1 = 120(0x78, float:1.68E-43)
            if (r9 != r1) goto L_0x021b
            r1 = 105(0x69, float:1.47E-43)
            if (r10 != r1) goto L_0x021b
            if (r11 != r14) goto L_0x021b
            if (r12 != 0) goto L_0x021b
            java.lang.String r0 = "image/jpeg"
            return r0
        L_0x0218:
            java.lang.String r0 = "image/jpeg"
            return r0
        L_0x021b:
            r1 = 208(0xd0, float:2.91E-43)
            if (r0 != r1) goto L_0x0244
            r1 = 207(0xcf, float:2.9E-43)
            if (r3 != r1) goto L_0x0244
            r1 = 17
            if (r4 != r1) goto L_0x0244
            r1 = 224(0xe0, float:3.14E-43)
            if (r5 != r1) goto L_0x0244
            r1 = 161(0xa1, float:2.26E-43)
            if (r6 != r1) goto L_0x0244
            r1 = 177(0xb1, float:2.48E-43)
            if (r7 != r1) goto L_0x0244
            r1 = 26
            if (r8 != r1) goto L_0x0244
            r1 = 225(0xe1, float:3.15E-43)
            if (r9 != r1) goto L_0x0244
            boolean r1 = checkfpx(r20)
            if (r1 == 0) goto L_0x0244
            java.lang.String r0 = "image/vnd.fpx"
            return r0
        L_0x0244:
            r1 = 46
            if (r0 != r1) goto L_0x0257
            r2 = 115(0x73, float:1.61E-43)
            if (r3 != r2) goto L_0x0257
            r2 = 110(0x6e, float:1.54E-43)
            if (r4 != r2) goto L_0x0257
            r2 = 100
            if (r5 != r2) goto L_0x0259
            java.lang.String r0 = "audio/basic"
            return r0
        L_0x0257:
            r2 = 100
        L_0x0259:
            if (r0 != r2) goto L_0x0268
            r2 = 110(0x6e, float:1.54E-43)
            if (r3 != r2) goto L_0x0268
            r2 = 115(0x73, float:1.61E-43)
            if (r4 != r2) goto L_0x0268
            if (r5 != r1) goto L_0x0268
            java.lang.String r0 = "audio/basic"
            return r0
        L_0x0268:
            r1 = 82
            if (r0 != r1) goto L_0x0279
            r0 = 73
            if (r3 != r0) goto L_0x0279
            r0 = 70
            if (r4 != r0) goto L_0x0279
            if (r5 != r0) goto L_0x0279
            java.lang.String r0 = "audio/x-wav"
            return r0
        L_0x0279:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URLConnection.guessContentTypeFromStream(java.io.InputStream):java.lang.String");
    }

    private static boolean checkfpx(InputStream inputStream) throws IOException {
        int i;
        int i2;
        int i3;
        int i4;
        InputStream inputStream2 = inputStream;
        inputStream2.mark(256);
        long skipForward = skipForward(inputStream2, 28);
        if (skipForward < 28) {
            inputStream.reset();
            return false;
        }
        int[] iArr = new int[16];
        if (readBytes(iArr, 2, inputStream2) < 0) {
            inputStream.reset();
            return false;
        }
        int i5 = iArr[0];
        long j = skipForward + 2;
        if (readBytes(iArr, 2, inputStream2) < 0) {
            inputStream.reset();
            return false;
        }
        if (i5 == 254) {
            i2 = iArr[0];
            i = iArr[1] << 8;
        } else {
            i2 = iArr[0] << 8;
            i = iArr[1];
        }
        int i6 = i2 + i;
        long j2 = 48 - (j + 2);
        if (skipForward(inputStream2, j2) < j2) {
            inputStream.reset();
            return false;
        } else if (readBytes(iArr, 4, inputStream2) < 0) {
            inputStream.reset();
            return false;
        } else {
            if (i5 == 254) {
                i4 = iArr[0] + (iArr[1] << 8) + (iArr[2] << 16);
                i3 = iArr[3] << 24;
            } else {
                i4 = (iArr[0] << 24) + (iArr[1] << 16) + (iArr[2] << 8);
                i3 = iArr[3];
            }
            inputStream.reset();
            long j3 = (((long) (1 << i6)) * ((long) (i4 + i3))) + 512 + 80;
            if (j3 < 0) {
                return false;
            }
            inputStream2.mark(((int) j3) + 48);
            if (skipForward(inputStream2, j3) < j3) {
                inputStream.reset();
                return false;
            } else if (readBytes(iArr, 16, inputStream2) < 0) {
                inputStream.reset();
                return false;
            } else if (i5 == 254 && iArr[0] == 0 && iArr[2] == 97 && iArr[3] == 86 && iArr[4] == 84 && iArr[5] == 193 && iArr[6] == 206 && iArr[7] == 17 && iArr[8] == 133 && iArr[9] == 83 && iArr[10] == 0 && iArr[11] == 170 && iArr[12] == 0 && iArr[13] == 161 && iArr[14] == 249 && iArr[15] == 91) {
                inputStream.reset();
                return true;
            } else if (iArr[3] == 0 && iArr[1] == 97 && iArr[0] == 86 && iArr[5] == 84 && iArr[4] == 193 && iArr[7] == 206 && iArr[6] == 17 && iArr[8] == 133 && iArr[9] == 83 && iArr[10] == 0 && iArr[11] == 170 && iArr[12] == 0 && iArr[13] == 161 && iArr[14] == 249 && iArr[15] == 91) {
                inputStream.reset();
                return true;
            } else {
                inputStream.reset();
                return false;
            }
        }
    }

    private static int readBytes(int[] iArr, int i, InputStream inputStream) throws IOException {
        byte[] bArr = new byte[i];
        if (inputStream.read(bArr, 0, i) < i) {
            return -1;
        }
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = bArr[i2] & 255;
        }
        return 0;
    }

    private static long skipForward(InputStream inputStream, long j) throws IOException {
        long j2 = 0;
        while (j2 != j) {
            long skip = inputStream.skip(j - j2);
            if (skip <= 0) {
                if (inputStream.read() == -1) {
                    return j2;
                }
                j2++;
            }
            j2 += skip;
        }
        return j2;
    }
}
