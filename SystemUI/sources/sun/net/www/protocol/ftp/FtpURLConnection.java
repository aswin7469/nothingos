package sun.net.www.protocol.ftp;

import android.icu.text.DateFormat;
import java.net.Proxy;
import java.net.SocketPermission;
import java.net.URL;
import java.p026io.BufferedInputStream;
import java.p026io.FilterInputStream;
import java.p026io.FilterOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.Permission;
import java.util.StringTokenizer;
import libcore.net.NetworkSecurityPolicy;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;
import sun.net.www.ParseUtil;
import sun.net.www.URLConnection;
import sun.security.util.SecurityConstants;

public class FtpURLConnection extends URLConnection {
    static final int ASCII = 1;
    static final int BIN = 2;
    static final int DIR = 3;
    static final int NONE = 0;
    private int connectTimeout;
    String filename;
    FtpClient ftp;
    String fullpath;
    String host;
    private Proxy instProxy;

    /* renamed from: is */
    InputStream f870is;

    /* renamed from: os */
    OutputStream f871os;
    String password;
    String pathname;
    Permission permission;
    int port;
    private int readTimeout;
    int type;
    String user;

    protected class FtpInputStream extends FilterInputStream {
        FtpClient ftp;

        FtpInputStream(FtpClient ftpClient, InputStream inputStream) {
            super(new BufferedInputStream(inputStream));
            this.ftp = ftpClient;
        }

        public void close() throws IOException {
            super.close();
            FtpClient ftpClient = this.ftp;
            if (ftpClient != null) {
                ftpClient.close();
            }
        }
    }

    protected class FtpOutputStream extends FilterOutputStream {
        FtpClient ftp;

        FtpOutputStream(FtpClient ftpClient, OutputStream outputStream) {
            super(outputStream);
            this.ftp = ftpClient;
        }

        public void close() throws IOException {
            super.close();
            FtpClient ftpClient = this.ftp;
            if (ftpClient != null) {
                ftpClient.close();
            }
        }
    }

    public FtpURLConnection(URL url) throws IOException {
        this(url, (Proxy) null);
    }

    FtpURLConnection(URL url, Proxy proxy) throws IOException {
        super(url);
        String str;
        this.f870is = null;
        this.f871os = null;
        this.ftp = null;
        this.type = 0;
        this.connectTimeout = -1;
        this.readTimeout = -1;
        this.instProxy = proxy;
        this.host = url.getHost();
        this.port = url.getPort();
        String userInfo = url.getUserInfo();
        if (!NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted()) {
            StringBuilder sb = new StringBuilder("Cleartext traffic not permitted: ");
            sb.append(url.getProtocol());
            sb.append("://");
            sb.append(this.host);
            if (url.getPort() >= 0) {
                str = ":" + url.getPort();
            } else {
                str = "";
            }
            sb.append(str);
            throw new IOException(sb.toString());
        } else if (userInfo != null) {
            int indexOf = userInfo.indexOf(58);
            if (indexOf == -1) {
                this.user = ParseUtil.decode(userInfo);
                this.password = null;
                return;
            }
            this.user = ParseUtil.decode(userInfo.substring(0, indexOf));
            this.password = ParseUtil.decode(userInfo.substring(indexOf + 1));
        }
    }

    private void setTimeouts() {
        FtpClient ftpClient = this.ftp;
        if (ftpClient != null) {
            int i = this.connectTimeout;
            if (i >= 0) {
                ftpClient.setConnectTimeout(i);
            }
            int i2 = this.readTimeout;
            if (i2 >= 0) {
                this.ftp.setReadTimeout(i2);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        r8.ftp.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0102, code lost:
        throw new sun.net.ftp.FtpLoginException("Invalid username/password");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0103, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0109, code lost:
        throw new java.p026io.IOException((java.lang.Throwable) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x010a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x010b, code lost:
        throw r0;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:52:0x00f6 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void connect() throws java.p026io.IOException {
        /*
            r8 = this;
            monitor-enter(r8)
            boolean r0 = r8.connected     // Catch:{ all -> 0x010c }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r8)
            return
        L_0x0007:
            java.net.Proxy r0 = r8.instProxy     // Catch:{ all -> 0x010c }
            r1 = 0
            if (r0 != 0) goto L_0x0075
            sun.net.www.protocol.ftp.FtpURLConnection$1 r0 = new sun.net.www.protocol.ftp.FtpURLConnection$1     // Catch:{ all -> 0x010c }
            r0.<init>()     // Catch:{ all -> 0x010c }
            java.lang.Object r0 = java.security.AccessController.doPrivileged(r0)     // Catch:{ all -> 0x010c }
            java.net.ProxySelector r0 = (java.net.ProxySelector) r0     // Catch:{ all -> 0x010c }
            if (r0 == 0) goto L_0x0074
            java.net.URL r2 = r8.url     // Catch:{ all -> 0x010c }
            java.net.URI r2 = sun.net.www.ParseUtil.toURI(r2)     // Catch:{ all -> 0x010c }
            java.util.List r3 = r0.select(r2)     // Catch:{ all -> 0x010c }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x010c }
            r4 = r1
        L_0x0028:
            boolean r5 = r3.hasNext()     // Catch:{ all -> 0x010c }
            if (r5 == 0) goto L_0x0072
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x010c }
            java.net.Proxy r4 = (java.net.Proxy) r4     // Catch:{ all -> 0x010c }
            if (r4 == 0) goto L_0x0072
            java.net.Proxy r5 = java.net.Proxy.NO_PROXY     // Catch:{ all -> 0x010c }
            if (r4 == r5) goto L_0x0072
            java.net.Proxy$Type r5 = r4.type()     // Catch:{ all -> 0x010c }
            java.net.Proxy$Type r6 = java.net.Proxy.Type.SOCKS     // Catch:{ all -> 0x010c }
            if (r5 != r6) goto L_0x0043
            goto L_0x0072
        L_0x0043:
            java.net.Proxy$Type r5 = r4.type()     // Catch:{ all -> 0x010c }
            java.net.Proxy$Type r6 = java.net.Proxy.Type.HTTP     // Catch:{ all -> 0x010c }
            if (r5 != r6) goto L_0x0063
            java.net.SocketAddress r5 = r4.address()     // Catch:{ all -> 0x010c }
            boolean r5 = r5 instanceof java.net.InetSocketAddress     // Catch:{ all -> 0x010c }
            if (r5 != 0) goto L_0x0054
            goto L_0x0063
        L_0x0054:
            java.net.SocketAddress r5 = r4.address()     // Catch:{ all -> 0x010c }
            java.io.IOException r6 = new java.io.IOException     // Catch:{ all -> 0x010c }
            java.lang.String r7 = "FTP connections over HTTP proxy not supported"
            r6.<init>((java.lang.String) r7)     // Catch:{ all -> 0x010c }
            r0.connectFailed(r2, r5, r6)     // Catch:{ all -> 0x010c }
            goto L_0x0028
        L_0x0063:
            java.net.SocketAddress r5 = r4.address()     // Catch:{ all -> 0x010c }
            java.io.IOException r6 = new java.io.IOException     // Catch:{ all -> 0x010c }
            java.lang.String r7 = "Wrong proxy type"
            r6.<init>((java.lang.String) r7)     // Catch:{ all -> 0x010c }
            r0.connectFailed(r2, r5, r6)     // Catch:{ all -> 0x010c }
            goto L_0x0028
        L_0x0072:
            r0 = r4
            goto L_0x0075
        L_0x0074:
            r0 = r1
        L_0x0075:
            java.lang.String r2 = r8.user     // Catch:{ all -> 0x010c }
            if (r2 != 0) goto L_0x00af
            java.lang.String r2 = "anonymous"
            r8.user = r2     // Catch:{ all -> 0x010c }
            sun.security.action.GetPropertyAction r2 = new sun.security.action.GetPropertyAction     // Catch:{ all -> 0x010c }
            java.lang.String r3 = "java.version"
            r2.<init>(r3)     // Catch:{ all -> 0x010c }
            java.lang.Object r2 = java.security.AccessController.doPrivileged(r2)     // Catch:{ all -> 0x010c }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x010c }
            sun.security.action.GetPropertyAction r3 = new sun.security.action.GetPropertyAction     // Catch:{ all -> 0x010c }
            java.lang.String r4 = "ftp.protocol.user"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x010c }
            r5.<init>()     // Catch:{ all -> 0x010c }
            java.lang.String r6 = "Java"
            r5.append((java.lang.String) r6)     // Catch:{ all -> 0x010c }
            r5.append((java.lang.String) r2)     // Catch:{ all -> 0x010c }
            java.lang.String r2 = "@"
            r5.append((java.lang.String) r2)     // Catch:{ all -> 0x010c }
            java.lang.String r2 = r5.toString()     // Catch:{ all -> 0x010c }
            r3.<init>(r4, r2)     // Catch:{ all -> 0x010c }
            java.lang.Object r2 = java.security.AccessController.doPrivileged(r3)     // Catch:{ all -> 0x010c }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x010c }
            r8.password = r2     // Catch:{ all -> 0x010c }
        L_0x00af:
            sun.net.ftp.FtpClient r2 = sun.net.ftp.FtpClient.create()     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            r8.ftp = r2     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            if (r0 == 0) goto L_0x00ba
            r2.setProxy(r0)     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
        L_0x00ba:
            r8.setTimeouts()     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            int r0 = r8.port     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            r2 = -1
            if (r0 == r2) goto L_0x00d1
            sun.net.ftp.FtpClient r0 = r8.ftp     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            java.net.InetSocketAddress r2 = new java.net.InetSocketAddress     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            java.lang.String r3 = r8.host     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            int r4 = r8.port     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            r2.<init>((java.lang.String) r3, (int) r4)     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            r0.connect(r2)     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            goto L_0x00e1
        L_0x00d1:
            sun.net.ftp.FtpClient r0 = r8.ftp     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            java.net.InetSocketAddress r2 = new java.net.InetSocketAddress     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            java.lang.String r3 = r8.host     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            int r4 = sun.net.ftp.FtpClient.defaultPort()     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            r2.<init>((java.lang.String) r3, (int) r4)     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
            r0.connect(r2)     // Catch:{ UnknownHostException -> 0x010a, FtpProtocolException -> 0x0103 }
        L_0x00e1:
            sun.net.ftp.FtpClient r0 = r8.ftp     // Catch:{ FtpProtocolException -> 0x00f6 }
            java.lang.String r2 = r8.user     // Catch:{ FtpProtocolException -> 0x00f6 }
            java.lang.String r3 = r8.password     // Catch:{ FtpProtocolException -> 0x00f6 }
            if (r3 != 0) goto L_0x00ea
            goto L_0x00ee
        L_0x00ea:
            char[] r1 = r3.toCharArray()     // Catch:{ FtpProtocolException -> 0x00f6 }
        L_0x00ee:
            r0.login(r2, r1)     // Catch:{ FtpProtocolException -> 0x00f6 }
            r0 = 1
            r8.connected = r0     // Catch:{ all -> 0x010c }
            monitor-exit(r8)
            return
        L_0x00f6:
            sun.net.ftp.FtpClient r0 = r8.ftp     // Catch:{ all -> 0x010c }
            r0.close()     // Catch:{ all -> 0x010c }
            sun.net.ftp.FtpLoginException r0 = new sun.net.ftp.FtpLoginException     // Catch:{ all -> 0x010c }
            java.lang.String r1 = "Invalid username/password"
            r0.<init>(r1)     // Catch:{ all -> 0x010c }
            throw r0     // Catch:{ all -> 0x010c }
        L_0x0103:
            r0 = move-exception
            java.io.IOException r1 = new java.io.IOException     // Catch:{ all -> 0x010c }
            r1.<init>((java.lang.Throwable) r0)     // Catch:{ all -> 0x010c }
            throw r1     // Catch:{ all -> 0x010c }
        L_0x010a:
            r0 = move-exception
            throw r0     // Catch:{ all -> 0x010c }
        L_0x010c:
            r0 = move-exception
            monitor-exit(r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.protocol.ftp.FtpURLConnection.connect():void");
    }

    private void decodePath(String str) {
        int indexOf = str.indexOf(";type=");
        if (indexOf >= 0) {
            String substring = str.substring(indexOf + 6, str.length());
            if ("i".equalsIgnoreCase(substring)) {
                this.type = 2;
            }
            if ("a".equalsIgnoreCase(substring)) {
                this.type = 1;
            }
            if (DateFormat.DAY.equalsIgnoreCase(substring)) {
                this.type = 3;
            }
            str = str.substring(0, indexOf);
        }
        if (str != null && str.length() > 1 && str.charAt(0) == '/') {
            str = str.substring(1);
        }
        if (str == null || str.length() == 0) {
            str = "./";
        }
        if (!str.endsWith("/")) {
            int lastIndexOf = str.lastIndexOf(47);
            if (lastIndexOf > 0) {
                String substring2 = str.substring(lastIndexOf + 1, str.length());
                this.filename = substring2;
                this.filename = ParseUtil.decode(substring2);
                this.pathname = str.substring(0, lastIndexOf);
            } else {
                this.filename = ParseUtil.decode(str);
                this.pathname = null;
            }
        } else {
            this.pathname = str.substring(0, str.length() - 1);
            this.filename = null;
        }
        if (this.pathname != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.pathname);
            sb.append("/");
            String str2 = this.filename;
            if (str2 == null) {
                str2 = "";
            }
            sb.append(str2);
            this.fullpath = sb.toString();
            return;
        }
        this.fullpath = this.filename;
    }

    /* renamed from: cd */
    private void m1818cd(String str) throws FtpProtocolException, IOException {
        if (str != null && !str.isEmpty()) {
            if (str.indexOf(47) == -1) {
                this.ftp.changeDirectory(ParseUtil.decode(str));
                return;
            }
            StringTokenizer stringTokenizer = new StringTokenizer(str, "/");
            while (stringTokenizer.hasMoreTokens()) {
                this.ftp.changeDirectory(ParseUtil.decode(stringTokenizer.nextToken()));
            }
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x00de */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0090 A[Catch:{ Exception -> 0x00b2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00d3 A[Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.p026io.InputStream getInputStream() throws java.p026io.IOException {
        /*
            r9 = this;
            java.lang.String r0 = "GET"
            java.lang.String r1 = "access-type"
            java.lang.String r2 = "content-type"
            boolean r3 = r9.connected
            if (r3 != 0) goto L_0x000d
            r9.connect()
        L_0x000d:
            java.io.OutputStream r3 = r9.f871os
            if (r3 != 0) goto L_0x0115
            java.io.InputStream r3 = r9.f870is
            if (r3 == 0) goto L_0x0016
            return r3
        L_0x0016:
            sun.net.www.MessageHeader r3 = new sun.net.www.MessageHeader
            r3.<init>()
            r4 = 0
            java.net.URL r5 = r9.url     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.lang.String r5 = r5.getPath()     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r9.decodePath(r5)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.lang.String r5 = r9.filename     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            if (r5 == 0) goto L_0x0052
            int r5 = r9.type     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r6 = 3
            if (r5 != r6) goto L_0x002f
            goto L_0x0052
        L_0x002f:
            r6 = 1
            if (r5 != r6) goto L_0x0038
            sun.net.ftp.FtpClient r5 = r9.ftp     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r5.setAsciiType()     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            goto L_0x003d
        L_0x0038:
            sun.net.ftp.FtpClient r5 = r9.ftp     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r5.setBinaryType()     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
        L_0x003d:
            java.lang.String r5 = r9.pathname     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r9.m1818cd(r5)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            sun.net.www.protocol.ftp.FtpURLConnection$FtpInputStream r5 = new sun.net.www.protocol.ftp.FtpURLConnection$FtpInputStream     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            sun.net.ftp.FtpClient r6 = r9.ftp     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.lang.String r7 = r9.filename     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.io.InputStream r7 = r6.getFileStream(r7)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r5.<init>(r6, r7)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r9.f870is = r5     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            goto L_0x007b
        L_0x0052:
            sun.net.ftp.FtpClient r5 = r9.ftp     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r5.setAsciiType()     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.lang.String r5 = r9.pathname     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r9.m1818cd(r5)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.lang.String r5 = r9.filename     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            if (r5 != 0) goto L_0x006e
            sun.net.www.protocol.ftp.FtpURLConnection$FtpInputStream r5 = new sun.net.www.protocol.ftp.FtpURLConnection$FtpInputStream     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            sun.net.ftp.FtpClient r6 = r9.ftp     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.io.InputStream r7 = r6.list(r4)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r5.<init>(r6, r7)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r9.f870is = r5     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            goto L_0x007b
        L_0x006e:
            sun.net.www.protocol.ftp.FtpURLConnection$FtpInputStream r6 = new sun.net.www.protocol.ftp.FtpURLConnection$FtpInputStream     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            sun.net.ftp.FtpClient r7 = r9.ftp     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.io.InputStream r5 = r7.nameList(r5)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r6.<init>(r7, r5)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            r9.f870is = r6     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
        L_0x007b:
            sun.net.ftp.FtpClient r5 = r9.ftp     // Catch:{ Exception -> 0x00b2 }
            long r5 = r5.getLastTransferSize()     // Catch:{ Exception -> 0x00b2 }
            java.lang.String r7 = "content-length"
            java.lang.String r8 = java.lang.Long.toString(r5)     // Catch:{ Exception -> 0x00b2 }
            r3.add(r7, r8)     // Catch:{ Exception -> 0x00b2 }
            r7 = 0
            int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r7 <= 0) goto L_0x00b6
            sun.net.ProgressMonitor r7 = sun.net.ProgressMonitor.getDefault()     // Catch:{ Exception -> 0x00b2 }
            java.net.URL r8 = r9.url     // Catch:{ Exception -> 0x00b2 }
            boolean r7 = r7.shouldMeterInput(r8, r0)     // Catch:{ Exception -> 0x00b2 }
            if (r7 == 0) goto L_0x00a7
            sun.net.ProgressSource r7 = new sun.net.ProgressSource     // Catch:{ Exception -> 0x00b2 }
            java.net.URL r8 = r9.url     // Catch:{ Exception -> 0x00b2 }
            r7.<init>(r8, r0, r5)     // Catch:{ Exception -> 0x00b2 }
            r7.beginTracking()     // Catch:{ Exception -> 0x00b2 }
            goto L_0x00a8
        L_0x00a7:
            r7 = r4
        L_0x00a8:
            sun.net.www.MeteredStream r0 = new sun.net.www.MeteredStream     // Catch:{ Exception -> 0x00b2 }
            java.io.InputStream r8 = r9.f870is     // Catch:{ Exception -> 0x00b2 }
            r0.<init>(r8, r7, r5)     // Catch:{ Exception -> 0x00b2 }
            r9.f870is = r0     // Catch:{ Exception -> 0x00b2 }
            goto L_0x00b6
        L_0x00b2:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
        L_0x00b6:
            java.lang.String r0 = "file"
            r3.add(r1, r0)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.lang.String r0 = r9.fullpath     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.lang.String r0 = guessContentTypeFromName(r0)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            if (r0 != 0) goto L_0x00d1
            java.io.InputStream r5 = r9.f870is     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            boolean r5 = r5.markSupported()     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            if (r5 == 0) goto L_0x00d1
            java.io.InputStream r0 = r9.f870is     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            java.lang.String r0 = guessContentTypeFromStream(r0)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
        L_0x00d1:
            if (r0 == 0) goto L_0x00ff
            r3.add(r2, r0)     // Catch:{ FileNotFoundException -> 0x00de, FtpProtocolException -> 0x00d7 }
            goto L_0x00ff
        L_0x00d7:
            r9 = move-exception
            java.io.IOException r0 = new java.io.IOException
            r0.<init>((java.lang.Throwable) r9)
            throw r0
        L_0x00de:
            java.lang.String r0 = r9.fullpath     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
            r9.m1818cd(r0)     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
            sun.net.ftp.FtpClient r0 = r9.ftp     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
            r0.setAsciiType()     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
            sun.net.www.protocol.ftp.FtpURLConnection$FtpInputStream r0 = new sun.net.www.protocol.ftp.FtpURLConnection$FtpInputStream     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
            sun.net.ftp.FtpClient r5 = r9.ftp     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
            java.io.InputStream r4 = r5.list(r4)     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
            r0.<init>(r5, r4)     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
            r9.f870is = r0     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
            java.lang.String r0 = "text/plain"
            r3.add(r2, r0)     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
            java.lang.String r0 = "directory"
            r3.add(r1, r0)     // Catch:{ IOException -> 0x010d, FtpProtocolException -> 0x0105 }
        L_0x00ff:
            r9.setProperties(r3)
            java.io.InputStream r9 = r9.f870is
            return r9
        L_0x0105:
            java.io.FileNotFoundException r0 = new java.io.FileNotFoundException
            java.lang.String r9 = r9.fullpath
            r0.<init>(r9)
            throw r0
        L_0x010d:
            java.io.FileNotFoundException r0 = new java.io.FileNotFoundException
            java.lang.String r9 = r9.fullpath
            r0.<init>(r9)
            throw r0
        L_0x0115:
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r0 = "Already opened for output"
            r9.<init>((java.lang.String) r0)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.protocol.ftp.FtpURLConnection.getInputStream():java.io.InputStream");
    }

    public OutputStream getOutputStream() throws IOException {
        if (!this.connected) {
            connect();
        }
        if (this.f870is == null) {
            OutputStream outputStream = this.f871os;
            if (outputStream != null) {
                return outputStream;
            }
            decodePath(this.url.getPath());
            String str = this.filename;
            if (str == null || str.length() == 0) {
                throw new IOException("illegal filename for a PUT");
            }
            try {
                String str2 = this.pathname;
                if (str2 != null) {
                    m1818cd(str2);
                }
                if (this.type == 1) {
                    this.ftp.setAsciiType();
                } else {
                    this.ftp.setBinaryType();
                }
                FtpClient ftpClient = this.ftp;
                FtpOutputStream ftpOutputStream = new FtpOutputStream(ftpClient, ftpClient.putFileStream(this.filename, false));
                this.f871os = ftpOutputStream;
                return ftpOutputStream;
            } catch (FtpProtocolException e) {
                throw new IOException((Throwable) e);
            }
        } else {
            throw new IOException("Already opened for input");
        }
    }

    /* access modifiers changed from: package-private */
    public String guessContentTypeFromFilename(String str) {
        return guessContentTypeFromName(str);
    }

    public Permission getPermission() {
        if (this.permission == null) {
            int port2 = this.url.getPort();
            if (port2 < 0) {
                port2 = FtpClient.defaultPort();
            }
            this.permission = new SocketPermission(this.host + ":" + port2, SecurityConstants.SOCKET_CONNECT_ACTION);
        }
        return this.permission;
    }

    public void setRequestProperty(String str, String str2) {
        super.setRequestProperty(str, str2);
        if (!"type".equals(str)) {
            return;
        }
        if ("i".equalsIgnoreCase(str2)) {
            this.type = 2;
        } else if ("a".equalsIgnoreCase(str2)) {
            this.type = 1;
        } else if (DateFormat.DAY.equalsIgnoreCase(str2)) {
            this.type = 3;
        } else {
            throw new IllegalArgumentException("Value of '" + str + "' request property was '" + str2 + "' when it must be either 'i', 'a' or 'd'");
        }
    }

    public String getRequestProperty(String str) {
        String requestProperty = super.getRequestProperty(str);
        if (requestProperty != null || !"type".equals(str)) {
            return requestProperty;
        }
        int i = this.type;
        return i == 1 ? "a" : i == 3 ? DateFormat.DAY : "i";
    }

    public void setConnectTimeout(int i) {
        if (i >= 0) {
            this.connectTimeout = i;
            return;
        }
        throw new IllegalArgumentException("timeouts can't be negative");
    }

    public int getConnectTimeout() {
        int i = this.connectTimeout;
        if (i < 0) {
            return 0;
        }
        return i;
    }

    public void setReadTimeout(int i) {
        if (i >= 0) {
            this.readTimeout = i;
            return;
        }
        throw new IllegalArgumentException("timeouts can't be negative");
    }

    public int getReadTimeout() {
        int i = this.readTimeout;
        if (i < 0) {
            return 0;
        }
        return i;
    }
}
