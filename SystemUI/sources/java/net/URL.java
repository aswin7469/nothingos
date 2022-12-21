package java.net;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.Proxy;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamException;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.SeempLog;
import java.util.Set;
import sun.net.ApplicationProxy;
import sun.net.www.protocol.file.Handler;
import sun.security.util.SecurityConstants;

public final class URL implements Serializable {
    private static final Set<String> BUILTIN_HANDLER_CLASS_NAMES = createBuiltinHandlerClassNames();
    static URLStreamHandlerFactory factory = null;
    static Hashtable<String, URLStreamHandler> handlers = new Hashtable<>();
    private static final String protocolPathProp = "java.protocol.handler.pkgs";
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("protocol", String.class), new ObjectStreamField("host", String.class), new ObjectStreamField("port", Integer.TYPE), new ObjectStreamField("authority", String.class), new ObjectStreamField("file", String.class), new ObjectStreamField("ref", String.class)};
    static final long serialVersionUID = -7627629688361524110L;
    private static Object streamHandlerLock = new Object();
    private String authority;
    private String file;
    transient URLStreamHandler handler;
    private int hashCode;
    private String host;
    transient InetAddress hostAddress;
    private transient String path;
    private int port;
    private String protocol;
    private transient String query;
    private String ref;
    private transient UrlDeserializedState tempState;
    private transient String userInfo;

    public URL(String str, String str2, int i, String str3) throws MalformedURLException {
        this(str, str2, i, str3, (URLStreamHandler) null);
    }

    public URL(String str, String str2, String str3) throws MalformedURLException {
        this(str, str2, -1, str3);
    }

    public URL(String str, String str2, int i, String str3, URLStreamHandler uRLStreamHandler) throws MalformedURLException {
        String str4;
        SecurityManager securityManager;
        this.port = -1;
        this.hashCode = -1;
        if (!(uRLStreamHandler == null || (securityManager = System.getSecurityManager()) == null)) {
            checkSpecifyHandler(securityManager);
        }
        String lowerCase = str.toLowerCase();
        this.protocol = lowerCase;
        if (str2 != null) {
            if (str2.indexOf(58) >= 0 && !str2.startsWith(NavigationBarInflaterView.SIZE_MOD_START)) {
                str2 = NavigationBarInflaterView.SIZE_MOD_START + str2 + NavigationBarInflaterView.SIZE_MOD_END;
            }
            this.host = str2;
            if (i >= -1) {
                this.port = i;
                if (i == -1) {
                    str4 = str2;
                } else {
                    str4 = str2 + ":" + i;
                }
                this.authority = str4;
            } else {
                throw new MalformedURLException("Invalid port number :" + i);
            }
        }
        Parts parts = new Parts(str3, str2);
        this.path = parts.getPath();
        String query2 = parts.getQuery();
        this.query = query2;
        if (query2 != null) {
            this.file = this.path + "?" + this.query;
        } else {
            this.file = this.path;
        }
        this.ref = parts.getRef();
        if (uRLStreamHandler == null && (uRLStreamHandler = getURLStreamHandler(lowerCase)) == null) {
            throw new MalformedURLException("unknown protocol: " + lowerCase);
        }
        this.handler = uRLStreamHandler;
    }

    public URL(String str) throws MalformedURLException {
        this((URL) null, str);
    }

    public URL(URL url, String str) throws MalformedURLException {
        this(url, str, (URLStreamHandler) null);
    }

    public URL(URL url, String str, URLStreamHandler uRLStreamHandler) throws MalformedURLException {
        String str2;
        String str3;
        char charAt;
        SecurityManager securityManager;
        this.port = -1;
        this.hashCode = -1;
        if (!(uRLStreamHandler == null || (securityManager = System.getSecurityManager()) == null)) {
            checkSpecifyHandler(securityManager);
        }
        try {
            int length = str.length();
            while (length > 0 && str.charAt(length - 1) <= ' ') {
                length--;
            }
            boolean z = false;
            int i = 0;
            while (i < length && str.charAt(i) <= ' ') {
                i++;
            }
            i = str.regionMatches(true, i, "url:", 0, 4) ? i + 4 : i;
            boolean z2 = i < str.length() && str.charAt(i) == '#';
            int i2 = i;
            while (true) {
                str2 = null;
                if (z2 || i2 >= length || (charAt = str.charAt(i2)) == '/') {
                    break;
                } else if (charAt == ':') {
                    str3 = str.substring(i, i2).toLowerCase();
                    if (isValidProtocol(str3)) {
                        i = i2 + 1;
                    }
                } else {
                    i2++;
                }
            }
            str3 = null;
            this.protocol = str3;
            if (url != null && (str3 == null || str3.equalsIgnoreCase(url.protocol))) {
                uRLStreamHandler = uRLStreamHandler == null ? url.handler : uRLStreamHandler;
                String str4 = url.path;
                if (str4 == null || !str4.startsWith("/")) {
                    str2 = str3;
                }
                if (str2 == null) {
                    this.protocol = url.protocol;
                    this.authority = url.authority;
                    this.userInfo = url.userInfo;
                    this.host = url.host;
                    this.port = url.port;
                    this.file = url.file;
                    this.path = url.path;
                    z = true;
                }
            }
            String str5 = this.protocol;
            if (str5 != null) {
                if (uRLStreamHandler == null) {
                    uRLStreamHandler = getURLStreamHandler(str5);
                    if (uRLStreamHandler == null) {
                        throw new MalformedURLException("unknown protocol: " + this.protocol);
                    }
                }
                this.handler = uRLStreamHandler;
                int indexOf = str.indexOf(35, i);
                if (indexOf >= 0) {
                    this.ref = str.substring(indexOf + 1, length);
                    length = indexOf;
                }
                if (z && i == length) {
                    this.query = url.query;
                    if (this.ref == null) {
                        this.ref = url.ref;
                    }
                }
                uRLStreamHandler.parseURL(this, str, i, length);
                return;
            }
            throw new MalformedURLException("no protocol: " + str);
        } catch (MalformedURLException e) {
            throw e;
        } catch (Exception e2) {
            MalformedURLException malformedURLException = new MalformedURLException(e2.getMessage());
            malformedURLException.initCause(e2);
            throw malformedURLException;
        }
    }

    private boolean isValidProtocol(String str) {
        int length = str.length();
        if (length < 1 || !Character.isLetter(str.charAt(0))) {
            return false;
        }
        for (int i = 1; i < length; i++) {
            char charAt = str.charAt(i);
            if (!Character.isLetterOrDigit(charAt) && charAt != '.' && charAt != '+' && charAt != '-') {
                return false;
            }
        }
        return true;
    }

    private void checkSpecifyHandler(SecurityManager securityManager) {
        securityManager.checkPermission(SecurityConstants.SPECIFY_HANDLER_PERMISSION);
    }

    /* access modifiers changed from: package-private */
    public void set(String str, String str2, int i, String str3, String str4) {
        synchronized (this) {
            this.protocol = str;
            this.host = str2;
            if (i != -1) {
                str2 = str2 + ":" + i;
            }
            this.authority = str2;
            this.port = i;
            this.file = str3;
            this.ref = str4;
            this.hashCode = -1;
            this.hostAddress = null;
            int lastIndexOf = str3.lastIndexOf(63);
            if (lastIndexOf != -1) {
                this.query = str3.substring(lastIndexOf + 1);
                this.path = str3.substring(0, lastIndexOf);
            } else {
                this.path = str3;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void set(String str, String str2, int i, String str3, String str4, String str5, String str6, String str7) {
        String str8;
        synchronized (this) {
            this.protocol = str;
            this.host = str2;
            this.port = i;
            if (str6 != null) {
                if (!str6.isEmpty()) {
                    str8 = str5 + "?" + str6;
                    this.file = str8;
                    this.userInfo = str4;
                    this.path = str5;
                    this.ref = str7;
                    this.hashCode = -1;
                    this.hostAddress = null;
                    this.query = str6;
                    this.authority = str3;
                }
            }
            str8 = str5;
            this.file = str8;
            this.userInfo = str4;
            this.path = str5;
            this.ref = str7;
            this.hashCode = -1;
            this.hostAddress = null;
            this.query = str6;
            this.authority = str3;
        }
    }

    public String getQuery() {
        return this.query;
    }

    public String getPath() {
        return this.path;
    }

    public String getUserInfo() {
        return this.userInfo;
    }

    public String getAuthority() {
        return this.authority;
    }

    public int getPort() {
        return this.port;
    }

    public int getDefaultPort() {
        return this.handler.getDefaultPort();
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getHost() {
        return this.host;
    }

    public String getFile() {
        return this.file;
    }

    public String getRef() {
        return this.ref;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof URL)) {
            return false;
        }
        return this.handler.equals(this, (URL) obj);
    }

    public synchronized int hashCode() {
        int i = this.hashCode;
        if (i != -1) {
            return i;
        }
        int hashCode2 = this.handler.hashCode(this);
        this.hashCode = hashCode2;
        return hashCode2;
    }

    public boolean sameFile(URL url) {
        return this.handler.sameFile(this, url);
    }

    public String toString() {
        return toExternalForm();
    }

    public String toExternalForm() {
        return this.handler.toExternalForm(this);
    }

    public URI toURI() throws URISyntaxException {
        return new URI(toString());
    }

    public URLConnection openConnection() throws IOException {
        SeempLog.record_str(91, "URL:" + this.query);
        return this.handler.openConnection(this);
    }

    public URLConnection openConnection(Proxy proxy) throws IOException {
        if (proxy != null) {
            Proxy create = proxy == Proxy.NO_PROXY ? Proxy.NO_PROXY : ApplicationProxy.create(proxy);
            SecurityManager securityManager = System.getSecurityManager();
            if (!(create.type() == Proxy.Type.DIRECT || securityManager == null)) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) create.address();
                if (inetSocketAddress.isUnresolved()) {
                    securityManager.checkConnect(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
                } else {
                    securityManager.checkConnect(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
                }
            }
            return this.handler.openConnection(this, create);
        }
        throw new IllegalArgumentException("proxy can not be null");
    }

    public final InputStream openStream() throws IOException {
        return openConnection().getInputStream();
    }

    public final Object getContent() throws IOException {
        return openConnection().getContent();
    }

    public final Object getContent(Class[] clsArr) throws IOException {
        return openConnection().getContent(clsArr);
    }

    public static void setURLStreamHandlerFactory(URLStreamHandlerFactory uRLStreamHandlerFactory) {
        synchronized (streamHandlerLock) {
            if (factory == null) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkSetFactory();
                }
                handlers.clear();
                factory = uRLStreamHandlerFactory;
            } else {
                throw new Error("factory already defined");
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:17|18|(1:20)(1:21)) */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r5 = java.lang.Thread.currentThread().getContextClassLoader();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0062, code lost:
        if (r5 != null) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0064, code lost:
        r3 = java.lang.Class.forName(r3, true, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0069, code lost:
        r3 = null;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x005a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.net.URLStreamHandler getURLStreamHandler(java.lang.String r6) {
        /*
            java.util.Hashtable<java.lang.String, java.net.URLStreamHandler> r0 = handlers
            java.lang.Object r0 = r0.get(r6)
            java.net.URLStreamHandler r0 = (java.net.URLStreamHandler) r0
            if (r0 != 0) goto L_0x00aa
            java.net.URLStreamHandlerFactory r1 = factory
            r2 = 1
            if (r1 == 0) goto L_0x0015
            java.net.URLStreamHandler r0 = r1.createURLStreamHandler(r6)
            r1 = r2
            goto L_0x0016
        L_0x0015:
            r1 = 0
        L_0x0016:
            if (r0 != 0) goto L_0x0074
            java.lang.String r3 = "java.protocol.handler.pkgs"
            java.lang.String r4 = ""
            java.lang.String r3 = java.lang.System.getProperty(r3, r4)
            java.util.StringTokenizer r4 = new java.util.StringTokenizer
            java.lang.String r5 = "|"
            r4.<init>(r3, r5)
        L_0x0028:
            if (r0 != 0) goto L_0x0074
            boolean r3 = r4.hasMoreTokens()
            if (r3 == 0) goto L_0x0074
            java.lang.String r3 = r4.nextToken()
            java.lang.String r3 = r3.trim()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ ReflectiveOperationException -> 0x0028 }
            r5.<init>()     // Catch:{ ReflectiveOperationException -> 0x0028 }
            r5.append((java.lang.String) r3)     // Catch:{ ReflectiveOperationException -> 0x0028 }
            java.lang.String r3 = "."
            r5.append((java.lang.String) r3)     // Catch:{ ReflectiveOperationException -> 0x0028 }
            r5.append((java.lang.String) r6)     // Catch:{ ReflectiveOperationException -> 0x0028 }
            java.lang.String r3 = ".Handler"
            r5.append((java.lang.String) r3)     // Catch:{ ReflectiveOperationException -> 0x0028 }
            java.lang.String r3 = r5.toString()     // Catch:{ ReflectiveOperationException -> 0x0028 }
            java.lang.ClassLoader r5 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ ClassNotFoundException -> 0x005a }
            java.lang.Class r3 = java.lang.Class.forName(r3, r2, r5)     // Catch:{ ClassNotFoundException -> 0x005a }
            goto L_0x006a
        L_0x005a:
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ ReflectiveOperationException -> 0x0028 }
            java.lang.ClassLoader r5 = r5.getContextClassLoader()     // Catch:{ ReflectiveOperationException -> 0x0028 }
            if (r5 == 0) goto L_0x0069
            java.lang.Class r3 = java.lang.Class.forName(r3, r2, r5)     // Catch:{ ReflectiveOperationException -> 0x0028 }
            goto L_0x006a
        L_0x0069:
            r3 = 0
        L_0x006a:
            if (r3 == 0) goto L_0x0028
            java.lang.Object r3 = r3.newInstance()     // Catch:{ ReflectiveOperationException -> 0x0028 }
            java.net.URLStreamHandler r3 = (java.net.URLStreamHandler) r3     // Catch:{ ReflectiveOperationException -> 0x0028 }
            r0 = r3
            goto L_0x0028
        L_0x0074:
            if (r0 != 0) goto L_0x0082
            java.net.URLStreamHandler r0 = createBuiltinHandler(r6)     // Catch:{ Exception -> 0x007b }
            goto L_0x0082
        L_0x007b:
            r6 = move-exception
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r0.<init>((java.lang.Object) r6)
            throw r0
        L_0x0082:
            java.lang.Object r2 = streamHandlerLock
            monitor-enter(r2)
            java.util.Hashtable<java.lang.String, java.net.URLStreamHandler> r3 = handlers     // Catch:{ all -> 0x00a7 }
            java.lang.Object r3 = r3.get(r6)     // Catch:{ all -> 0x00a7 }
            java.net.URLStreamHandler r3 = (java.net.URLStreamHandler) r3     // Catch:{ all -> 0x00a7 }
            if (r3 == 0) goto L_0x0091
            monitor-exit(r2)     // Catch:{ all -> 0x00a7 }
            return r3
        L_0x0091:
            if (r1 != 0) goto L_0x009b
            java.net.URLStreamHandlerFactory r1 = factory     // Catch:{ all -> 0x00a7 }
            if (r1 == 0) goto L_0x009b
            java.net.URLStreamHandler r3 = r1.createURLStreamHandler(r6)     // Catch:{ all -> 0x00a7 }
        L_0x009b:
            if (r3 == 0) goto L_0x009e
            r0 = r3
        L_0x009e:
            if (r0 == 0) goto L_0x00a5
            java.util.Hashtable<java.lang.String, java.net.URLStreamHandler> r1 = handlers     // Catch:{ all -> 0x00a7 }
            r1.put(r6, r0)     // Catch:{ all -> 0x00a7 }
        L_0x00a5:
            monitor-exit(r2)     // Catch:{ all -> 0x00a7 }
            goto L_0x00aa
        L_0x00a7:
            r6 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x00a7 }
            throw r6
        L_0x00aa:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URL.getURLStreamHandler(java.lang.String):java.net.URLStreamHandler");
    }

    private static URLStreamHandler createBuiltinHandler(String str) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (str.equals("file")) {
            return new Handler();
        }
        if (str.equals("ftp")) {
            return new sun.net.www.protocol.ftp.Handler();
        }
        if (str.equals("jar")) {
            return new sun.net.www.protocol.jar.Handler();
        }
        if (str.equals("http")) {
            return (URLStreamHandler) Class.forName("com.android.okhttp.HttpHandler").newInstance();
        }
        if (str.equals("https")) {
            return (URLStreamHandler) Class.forName("com.android.okhttp.HttpsHandler").newInstance();
        }
        return null;
    }

    private static Set<String> createBuiltinHandlerClassNames() {
        HashSet hashSet = new HashSet();
        hashSet.add("sun.net.www.protocol.file.Handler");
        hashSet.add("sun.net.www.protocol.ftp.Handler");
        hashSet.add("sun.net.www.protocol.jar.Handler");
        hashSet.add("com.android.okhttp.HttpHandler");
        hashSet.add("com.android.okhttp.HttpsHandler");
        return Collections.unmodifiableSet(hashSet);
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String str;
        String str2;
        String str3;
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        String str4 = (String) readFields.get("protocol", (Object) null);
        if (getURLStreamHandler(str4) != null) {
            String str5 = (String) readFields.get("host", (Object) null);
            int i = readFields.get("port", -1);
            String str6 = (String) readFields.get("authority", (Object) null);
            String str7 = (String) readFields.get("file", (Object) null);
            String str8 = (String) readFields.get("ref", (Object) null);
            if (str6 != null || ((str5 == null || str5.length() <= 0) && i == -1)) {
                str2 = str5;
                str = str6;
            } else {
                if (str5 == null) {
                    str5 = "";
                }
                if (i == -1) {
                    str3 = str5;
                } else {
                    str3 = str5 + ":" + i;
                }
                str = str3;
                str2 = str5;
            }
            this.tempState = new UrlDeserializedState(str4, str2, i, str, str7, str8, -1);
        } else {
            throw new IOException("unknown protocol: " + str4);
        }
    }

    private Object readResolve() throws ObjectStreamException {
        URLStreamHandler uRLStreamHandler = getURLStreamHandler(this.tempState.getProtocol());
        if (isBuiltinStreamHandler(uRLStreamHandler.getClass().getName())) {
            return fabricateNewURL();
        }
        return setDeserializedFields(uRLStreamHandler);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x008d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.net.URL setDeserializedFields(java.net.URLStreamHandler r13) {
        /*
            r12 = this;
            java.net.UrlDeserializedState r0 = r12.tempState
            java.lang.String r0 = r0.getProtocol()
            java.net.UrlDeserializedState r1 = r12.tempState
            java.lang.String r1 = r1.getHost()
            java.net.UrlDeserializedState r2 = r12.tempState
            int r2 = r2.getPort()
            java.net.UrlDeserializedState r3 = r12.tempState
            java.lang.String r3 = r3.getAuthority()
            java.net.UrlDeserializedState r4 = r12.tempState
            java.lang.String r4 = r4.getFile()
            java.net.UrlDeserializedState r5 = r12.tempState
            java.lang.String r5 = r5.getRef()
            java.net.UrlDeserializedState r6 = r12.tempState
            int r6 = r6.getHashCode()
            r7 = 64
            r8 = 0
            r9 = -1
            r10 = 0
            if (r3 != 0) goto L_0x0068
            if (r1 == 0) goto L_0x0039
            int r11 = r1.length()
            if (r11 > 0) goto L_0x003b
        L_0x0039:
            if (r2 == r9) goto L_0x0068
        L_0x003b:
            if (r1 != 0) goto L_0x003f
            java.lang.String r1 = ""
        L_0x003f:
            if (r2 != r9) goto L_0x0043
            r3 = r1
            goto L_0x0057
        L_0x0043:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append((java.lang.String) r1)
            java.lang.String r11 = ":"
            r3.append((java.lang.String) r11)
            r3.append((int) r2)
            java.lang.String r3 = r3.toString()
        L_0x0057:
            int r7 = r1.lastIndexOf((int) r7)
            if (r7 == r9) goto L_0x0075
            java.lang.String r11 = r1.substring(r8, r7)
            int r7 = r7 + 1
            java.lang.String r1 = r1.substring(r7)
            goto L_0x0076
        L_0x0068:
            if (r3 == 0) goto L_0x0075
            int r7 = r3.indexOf((int) r7)
            if (r7 == r9) goto L_0x0075
            java.lang.String r11 = r3.substring(r8, r7)
            goto L_0x0076
        L_0x0075:
            r11 = r10
        L_0x0076:
            if (r4 == 0) goto L_0x008d
            r7 = 63
            int r7 = r4.lastIndexOf((int) r7)
            if (r7 == r9) goto L_0x008b
            int r9 = r7 + 1
            java.lang.String r10 = r4.substring(r9)
            java.lang.String r7 = r4.substring(r8, r7)
            goto L_0x008e
        L_0x008b:
            r7 = r4
            goto L_0x008e
        L_0x008d:
            r7 = r10
        L_0x008e:
            r12.protocol = r0
            r12.host = r1
            r12.port = r2
            r12.file = r4
            r12.authority = r3
            r12.ref = r5
            r12.hashCode = r6
            r12.handler = r13
            r12.query = r10
            r12.path = r7
            r12.userInfo = r11
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URL.setDeserializedFields(java.net.URLStreamHandler):java.net.URL");
    }

    private URL fabricateNewURL() throws InvalidObjectException {
        String reconstituteUrlString = this.tempState.reconstituteUrlString();
        try {
            URL url = new URL(reconstituteUrlString);
            url.setSerializedHashCode(this.tempState.getHashCode());
            resetState();
            return url;
        } catch (MalformedURLException e) {
            resetState();
            InvalidObjectException invalidObjectException = new InvalidObjectException("Malformed URL: " + reconstituteUrlString);
            invalidObjectException.initCause(e);
            throw invalidObjectException;
        }
    }

    private boolean isBuiltinStreamHandler(String str) {
        return BUILTIN_HANDLER_CLASS_NAMES.contains(str);
    }

    private void resetState() {
        this.protocol = null;
        this.host = null;
        this.port = -1;
        this.file = null;
        this.authority = null;
        this.ref = null;
        this.hashCode = -1;
        this.handler = null;
        this.query = null;
        this.path = null;
        this.userInfo = null;
        this.tempState = null;
    }

    private void setSerializedHashCode(int i) {
        this.hashCode = i;
    }
}
