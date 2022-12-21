package java.net;

import java.p026io.BufferedOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.security.action.GetPropertyAction;

class SocksSocketImpl extends PlainSocketImpl implements SocksConsts {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private boolean applicationSetProxy;
    /* access modifiers changed from: private */
    public InputStream cmdIn;
    /* access modifiers changed from: private */
    public OutputStream cmdOut;
    private Socket cmdsock;
    private InetSocketAddress external_address;
    /* access modifiers changed from: private */
    public String server;
    /* access modifiers changed from: private */
    public int serverPort;
    private boolean useV4;

    SocksSocketImpl() {
        this.server = null;
        this.serverPort = SocksConsts.DEFAULT_PORT;
        this.useV4 = false;
        this.cmdsock = null;
        this.cmdIn = null;
        this.cmdOut = null;
    }

    SocksSocketImpl(String str, int i) {
        this.serverPort = SocksConsts.DEFAULT_PORT;
        this.useV4 = false;
        this.cmdsock = null;
        this.cmdIn = null;
        this.cmdOut = null;
        this.server = str;
        this.serverPort = i == -1 ? 1080 : i;
    }

    SocksSocketImpl(Proxy proxy) {
        this.server = null;
        this.serverPort = SocksConsts.DEFAULT_PORT;
        this.useV4 = false;
        this.cmdsock = null;
        this.cmdIn = null;
        this.cmdOut = null;
        SocketAddress address = proxy.address();
        if (address instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
            this.server = inetSocketAddress.getHostString();
            this.serverPort = inetSocketAddress.getPort();
        }
    }

    /* access modifiers changed from: package-private */
    public void setV4() {
        this.useV4 = true;
    }

    private synchronized void privilegedConnect(final String str, final int i, final int i2) throws IOException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                public Void run() throws IOException {
                    SocksSocketImpl.this.superConnectServer(str, i, i2);
                    SocksSocketImpl socksSocketImpl = SocksSocketImpl.this;
                    socksSocketImpl.cmdIn = socksSocketImpl.getInputStream();
                    SocksSocketImpl socksSocketImpl2 = SocksSocketImpl.this;
                    socksSocketImpl2.cmdOut = socksSocketImpl2.getOutputStream();
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }

    /* access modifiers changed from: private */
    public void superConnectServer(String str, int i, int i2) throws IOException {
        super.connect((SocketAddress) new InetSocketAddress(str, i), i2);
    }

    private static int remainingMillis(long j) throws IOException {
        if (j == 0) {
            return 0;
        }
        long currentTimeMillis = j - System.currentTimeMillis();
        if (currentTimeMillis > 0) {
            return (int) currentTimeMillis;
        }
        throw new SocketTimeoutException();
    }

    private int readSocksReply(InputStream inputStream, byte[] bArr) throws IOException {
        return readSocksReply(inputStream, bArr, 0);
    }

    private int readSocksReply(InputStream inputStream, byte[] bArr, long j) throws IOException {
        int length = bArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length && i2 < 3) {
            try {
                int read = ((SocketInputStream) inputStream).read(bArr, i, length - i, remainingMillis(j));
                if (read >= 0) {
                    i += read;
                    i2++;
                } else {
                    throw new SocketException("Malformed reply from SOCKS server");
                }
            } catch (SocketTimeoutException unused) {
                throw new SocketTimeoutException("Connect timed out");
            }
        }
        return i;
    }

    private boolean authenticate(byte b, InputStream inputStream, BufferedOutputStream bufferedOutputStream) throws IOException {
        return authenticate(b, inputStream, bufferedOutputStream, 0);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v11, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean authenticate(byte r7, java.p026io.InputStream r8, java.p026io.BufferedOutputStream r9, long r10) throws java.p026io.IOException {
        /*
            r6 = this;
            java.lang.String r0 = "ISO-8859-1"
            r1 = 1
            if (r7 != 0) goto L_0x0006
            return r1
        L_0x0006:
            r2 = 2
            r3 = 0
            if (r7 != r2) goto L_0x007a
            java.lang.String r7 = r6.server
            java.net.InetAddress r7 = java.net.InetAddress.getByName(r7)
            java.net.SocksSocketImpl$2 r4 = new java.net.SocksSocketImpl$2
            r4.<init>(r7)
            java.lang.Object r7 = java.security.AccessController.doPrivileged(r4)
            java.net.PasswordAuthentication r7 = (java.net.PasswordAuthentication) r7
            if (r7 == 0) goto L_0x002b
            java.lang.String r4 = r7.getUserName()
            java.lang.String r5 = new java.lang.String
            char[] r7 = r7.getPassword()
            r5.<init>((char[]) r7)
            goto L_0x003b
        L_0x002b:
            sun.security.action.GetPropertyAction r7 = new sun.security.action.GetPropertyAction
            java.lang.String r4 = "user.name"
            r7.<init>(r4)
            java.lang.Object r7 = java.security.AccessController.doPrivileged(r7)
            r4 = r7
            java.lang.String r4 = (java.lang.String) r4
            r5 = 0
        L_0x003b:
            if (r4 != 0) goto L_0x003e
            return r3
        L_0x003e:
            r9.write(r1)
            int r7 = r4.length()
            r9.write(r7)
            byte[] r7 = r4.getBytes((java.lang.String) r0)     // Catch:{ UnsupportedEncodingException -> 0x004f }
            r9.write((byte[]) r7)     // Catch:{ UnsupportedEncodingException -> 0x004f }
        L_0x004f:
            if (r5 == 0) goto L_0x0060
            int r7 = r5.length()
            r9.write(r7)
            byte[] r7 = r5.getBytes((java.lang.String) r0)     // Catch:{ UnsupportedEncodingException -> 0x0063 }
            r9.write((byte[]) r7)     // Catch:{ UnsupportedEncodingException -> 0x0063 }
            goto L_0x0063
        L_0x0060:
            r9.write(r3)
        L_0x0063:
            r9.flush()
            byte[] r7 = new byte[r2]
            int r6 = r6.readSocksReply(r8, r7, r10)
            if (r6 != r2) goto L_0x0074
            byte r6 = r7[r1]
            if (r6 == 0) goto L_0x0073
            goto L_0x0074
        L_0x0073:
            return r1
        L_0x0074:
            r9.close()
            r8.close()
        L_0x007a:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.SocksSocketImpl.authenticate(byte, java.io.InputStream, java.io.BufferedOutputStream, long):boolean");
    }

    private void connectV4(InputStream inputStream, OutputStream outputStream, InetSocketAddress inetSocketAddress, long j) throws IOException {
        SocketException socketException;
        if (inetSocketAddress.getAddress() instanceof Inet4Address) {
            outputStream.write(4);
            outputStream.write(1);
            outputStream.write((inetSocketAddress.getPort() >> 8) & 255);
            outputStream.write((inetSocketAddress.getPort() >> 0) & 255);
            outputStream.write(inetSocketAddress.getAddress().getAddress());
            try {
                outputStream.write(getUserName().getBytes("ISO-8859-1"));
            } catch (UnsupportedEncodingException unused) {
            }
            outputStream.write(0);
            outputStream.flush();
            byte[] bArr = new byte[8];
            int readSocksReply = readSocksReply(inputStream, bArr, j);
            if (readSocksReply == 8) {
                byte b = bArr[0];
                if (b == 0 || b == 4) {
                    switch (bArr[1]) {
                        case 90:
                            this.external_address = inetSocketAddress;
                            socketException = null;
                            break;
                        case 91:
                            socketException = new SocketException("SOCKS request rejected");
                            break;
                        case 92:
                            socketException = new SocketException("SOCKS server couldn't reach destination");
                            break;
                        case 93:
                            socketException = new SocketException("SOCKS authentication failed");
                            break;
                        default:
                            socketException = new SocketException("Reply from SOCKS server contains bad status");
                            break;
                    }
                    if (socketException != null) {
                        inputStream.close();
                        outputStream.close();
                        throw socketException;
                    }
                    return;
                }
                throw new SocketException("Reply from SOCKS server has bad version");
            }
            throw new SocketException("Reply from SOCKS server has bad length: " + readSocksReply);
        }
        throw new SocketException("SOCKS V4 requires IPv4 only addresses");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(java.net.SocketAddress r14, int r15) throws java.p026io.IOException {
        /*
            r13 = this;
            r0 = 0
            if (r15 != 0) goto L_0x0005
            goto L_0x0016
        L_0x0005:
            long r2 = java.lang.System.currentTimeMillis()
            long r4 = (long) r15
            long r2 = r2 + r4
            int r15 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r15 >= 0) goto L_0x0015
            r0 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            goto L_0x0016
        L_0x0015:
            r0 = r2
        L_0x0016:
            java.lang.SecurityManager r15 = java.lang.System.getSecurityManager()
            if (r14 == 0) goto L_0x024e
            boolean r2 = r14 instanceof java.net.InetSocketAddress
            if (r2 == 0) goto L_0x024e
            java.net.InetSocketAddress r14 = (java.net.InetSocketAddress) r14
            if (r15 == 0) goto L_0x0045
            boolean r2 = r14.isUnresolved()
            if (r2 == 0) goto L_0x0036
            java.lang.String r2 = r14.getHostName()
            int r3 = r14.getPort()
            r15.checkConnect(r2, r3)
            goto L_0x0045
        L_0x0036:
            java.net.InetAddress r2 = r14.getAddress()
            java.lang.String r2 = r2.getHostAddress()
            int r3 = r14.getPort()
            r15.checkConnect(r2, r3)
        L_0x0045:
            java.lang.String r15 = r13.server
            if (r15 != 0) goto L_0x0051
            int r15 = remainingMillis(r0)
            super.connect((java.net.SocketAddress) r14, (int) r15)
            return
        L_0x0051:
            int r2 = r13.serverPort     // Catch:{ IOException -> 0x0243 }
            int r3 = remainingMillis(r0)     // Catch:{ IOException -> 0x0243 }
            r13.privilegedConnect(r15, r2, r3)     // Catch:{ IOException -> 0x0243 }
            java.io.BufferedOutputStream r15 = new java.io.BufferedOutputStream
            java.io.OutputStream r2 = r13.cmdOut
            r3 = 512(0x200, float:7.175E-43)
            r15.<init>(r2, r3)
            java.io.InputStream r8 = r13.cmdIn
            boolean r2 = r13.useV4
            if (r2 == 0) goto L_0x0082
            boolean r2 = r14.isUnresolved()
            if (r2 != 0) goto L_0x0078
            r2 = r13
            r3 = r8
            r4 = r15
            r5 = r14
            r6 = r0
            r2.connectV4(r3, r4, r5, r6)
            return
        L_0x0078:
            java.net.UnknownHostException r13 = new java.net.UnknownHostException
            java.lang.String r14 = r14.toString()
            r13.<init>(r14)
            throw r13
        L_0x0082:
            r9 = 5
            r15.write(r9)
            r10 = 2
            r15.write(r10)
            r11 = 0
            r15.write(r11)
            r15.write(r10)
            r15.flush()
            byte[] r2 = new byte[r10]
            int r3 = r13.readSocksReply(r8, r2, r0)
            if (r3 != r10) goto L_0x022a
            byte r3 = r2[r11]
            if (r3 == r9) goto L_0x00a2
            goto L_0x022a
        L_0x00a2:
            r12 = 1
            byte r3 = r2[r12]
            r2 = -1
            if (r3 == r2) goto L_0x0222
            r2 = r13
            r4 = r8
            r5 = r15
            r6 = r0
            boolean r2 = r2.authenticate(r3, r4, r5, r6)
            if (r2 == 0) goto L_0x021a
            r15.write(r9)
            r15.write(r12)
            r15.write(r11)
            boolean r2 = r14.isUnresolved()
            r3 = 3
            r4 = 4
            if (r2 == 0) goto L_0x00f4
            r15.write(r3)
            java.lang.String r2 = r14.getHostName()
            int r2 = r2.length()
            r15.write(r2)
            java.lang.String r2 = r14.getHostName()     // Catch:{ UnsupportedEncodingException -> 0x00de }
            java.lang.String r5 = "ISO-8859-1"
            byte[] r2 = r2.getBytes((java.lang.String) r5)     // Catch:{ UnsupportedEncodingException -> 0x00de }
            r15.write((byte[]) r2)     // Catch:{ UnsupportedEncodingException -> 0x00de }
        L_0x00de:
            int r2 = r14.getPort()
            int r2 = r2 >> 8
            r2 = r2 & 255(0xff, float:3.57E-43)
            r15.write(r2)
            int r2 = r14.getPort()
            int r2 = r2 >> r11
            r2 = r2 & 255(0xff, float:3.57E-43)
            r15.write(r2)
            goto L_0x0143
        L_0x00f4:
            java.net.InetAddress r2 = r14.getAddress()
            boolean r2 = r2 instanceof java.net.Inet6Address
            if (r2 == 0) goto L_0x0120
            r15.write(r4)
            java.net.InetAddress r2 = r14.getAddress()
            byte[] r2 = r2.getAddress()
            r15.write((byte[]) r2)
            int r2 = r14.getPort()
            int r2 = r2 >> 8
            r2 = r2 & 255(0xff, float:3.57E-43)
            r15.write(r2)
            int r2 = r14.getPort()
            int r2 = r2 >> r11
            r2 = r2 & 255(0xff, float:3.57E-43)
            r15.write(r2)
            goto L_0x0143
        L_0x0120:
            r15.write(r12)
            java.net.InetAddress r2 = r14.getAddress()
            byte[] r2 = r2.getAddress()
            r15.write((byte[]) r2)
            int r2 = r14.getPort()
            int r2 = r2 >> 8
            r2 = r2 & 255(0xff, float:3.57E-43)
            r15.write(r2)
            int r2 = r14.getPort()
            int r2 = r2 >> r11
            r2 = r2 & 255(0xff, float:3.57E-43)
            r15.write(r2)
        L_0x0143:
            r15.flush()
            byte[] r2 = new byte[r4]
            int r5 = r13.readSocksReply(r8, r2, r0)
            if (r5 != r4) goto L_0x0212
            byte r5 = r2[r12]
            switch(r5) {
                case 0: goto L_0x019c;
                case 1: goto L_0x0194;
                case 2: goto L_0x018b;
                case 3: goto L_0x0182;
                case 4: goto L_0x0179;
                case 5: goto L_0x0170;
                case 6: goto L_0x0167;
                case 7: goto L_0x015e;
                case 8: goto L_0x0155;
                default: goto L_0x0153;
            }
        L_0x0153:
            goto L_0x0205
        L_0x0155:
            java.net.SocketException r0 = new java.net.SocketException
            java.lang.String r1 = "SOCKS: address type not supported"
            r0.<init>((java.lang.String) r1)
            goto L_0x0206
        L_0x015e:
            java.net.SocketException r0 = new java.net.SocketException
            java.lang.String r1 = "SOCKS: Command not supported"
            r0.<init>((java.lang.String) r1)
            goto L_0x0206
        L_0x0167:
            java.net.SocketException r0 = new java.net.SocketException
            java.lang.String r1 = "SOCKS: TTL expired"
            r0.<init>((java.lang.String) r1)
            goto L_0x0206
        L_0x0170:
            java.net.SocketException r0 = new java.net.SocketException
            java.lang.String r1 = "SOCKS: Connection refused"
            r0.<init>((java.lang.String) r1)
            goto L_0x0206
        L_0x0179:
            java.net.SocketException r0 = new java.net.SocketException
            java.lang.String r1 = "SOCKS: Host unreachable"
            r0.<init>((java.lang.String) r1)
            goto L_0x0206
        L_0x0182:
            java.net.SocketException r0 = new java.net.SocketException
            java.lang.String r1 = "SOCKS: Network unreachable"
            r0.<init>((java.lang.String) r1)
            goto L_0x0206
        L_0x018b:
            java.net.SocketException r0 = new java.net.SocketException
            java.lang.String r1 = "SOCKS: Connection not allowed by ruleset"
            r0.<init>((java.lang.String) r1)
            goto L_0x0206
        L_0x0194:
            java.net.SocketException r0 = new java.net.SocketException
            java.lang.String r1 = "SOCKS server general failure"
            r0.<init>((java.lang.String) r1)
            goto L_0x0206
        L_0x019c:
            byte r2 = r2[r3]
            java.lang.String r6 = "Reply from SOCKS server badly formatted"
            if (r2 == r12) goto L_0x01e8
            if (r2 == r3) goto L_0x01cb
            if (r2 == r4) goto L_0x01ae
            java.net.SocketException r0 = new java.net.SocketException
            java.lang.String r1 = "Reply from SOCKS server contains wrong code"
            r0.<init>((java.lang.String) r1)
            goto L_0x0206
        L_0x01ae:
            byte[] r2 = new byte[r5]
            int r2 = r13.readSocksReply(r8, r2, r0)
            if (r2 != r5) goto L_0x01c5
            byte[] r2 = new byte[r10]
            int r0 = r13.readSocksReply(r8, r2, r0)
            if (r0 != r10) goto L_0x01bf
            goto L_0x0205
        L_0x01bf:
            java.net.SocketException r13 = new java.net.SocketException
            r13.<init>((java.lang.String) r6)
            throw r13
        L_0x01c5:
            java.net.SocketException r13 = new java.net.SocketException
            r13.<init>((java.lang.String) r6)
            throw r13
        L_0x01cb:
            byte[] r2 = new byte[r5]
            int r2 = r13.readSocksReply(r8, r2, r0)
            if (r2 != r5) goto L_0x01e2
            byte[] r2 = new byte[r10]
            int r0 = r13.readSocksReply(r8, r2, r0)
            if (r0 != r10) goto L_0x01dc
            goto L_0x0205
        L_0x01dc:
            java.net.SocketException r13 = new java.net.SocketException
            r13.<init>((java.lang.String) r6)
            throw r13
        L_0x01e2:
            java.net.SocketException r13 = new java.net.SocketException
            r13.<init>((java.lang.String) r6)
            throw r13
        L_0x01e8:
            byte[] r2 = new byte[r4]
            int r2 = r13.readSocksReply(r8, r2, r0)
            if (r2 != r4) goto L_0x01ff
            byte[] r2 = new byte[r10]
            int r0 = r13.readSocksReply(r8, r2, r0)
            if (r0 != r10) goto L_0x01f9
            goto L_0x0205
        L_0x01f9:
            java.net.SocketException r13 = new java.net.SocketException
            r13.<init>((java.lang.String) r6)
            throw r13
        L_0x01ff:
            java.net.SocketException r13 = new java.net.SocketException
            r13.<init>((java.lang.String) r6)
            throw r13
        L_0x0205:
            r0 = 0
        L_0x0206:
            if (r0 != 0) goto L_0x020b
            r13.external_address = r14
            return
        L_0x020b:
            r8.close()
            r15.close()
            throw r0
        L_0x0212:
            java.net.SocketException r13 = new java.net.SocketException
            java.lang.String r14 = "Reply from SOCKS server has bad length"
            r13.<init>((java.lang.String) r14)
            throw r13
        L_0x021a:
            java.net.SocketException r13 = new java.net.SocketException
            java.lang.String r14 = "SOCKS : authentication failed"
            r13.<init>((java.lang.String) r14)
            throw r13
        L_0x0222:
            java.net.SocketException r13 = new java.net.SocketException
            java.lang.String r14 = "SOCKS : No acceptable methods"
            r13.<init>((java.lang.String) r14)
            throw r13
        L_0x022a:
            boolean r2 = r14.isUnresolved()
            if (r2 != 0) goto L_0x0239
            r2 = r13
            r3 = r8
            r4 = r15
            r5 = r14
            r6 = r0
            r2.connectV4(r3, r4, r5, r6)
            return
        L_0x0239:
            java.net.UnknownHostException r13 = new java.net.UnknownHostException
            java.lang.String r14 = r14.toString()
            r13.<init>(r14)
            throw r13
        L_0x0243:
            r13 = move-exception
            java.net.SocketException r14 = new java.net.SocketException
            java.lang.String r13 = r13.getMessage()
            r14.<init>((java.lang.String) r13)
            throw r14
        L_0x024e:
            java.lang.IllegalArgumentException r13 = new java.lang.IllegalArgumentException
            java.lang.String r14 = "Unsupported address type"
            r13.<init>((java.lang.String) r14)
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.SocksSocketImpl.connect(java.net.SocketAddress, int):void");
    }

    /* access modifiers changed from: protected */
    public InetAddress getInetAddress() {
        InetSocketAddress inetSocketAddress = this.external_address;
        if (inetSocketAddress != null) {
            return inetSocketAddress.getAddress();
        }
        return super.getInetAddress();
    }

    /* access modifiers changed from: protected */
    public int getPort() {
        InetSocketAddress inetSocketAddress = this.external_address;
        if (inetSocketAddress != null) {
            return inetSocketAddress.getPort();
        }
        return super.getPort();
    }

    /* access modifiers changed from: protected */
    public int getLocalPort() {
        if (this.socket != null) {
            return super.getLocalPort();
        }
        InetSocketAddress inetSocketAddress = this.external_address;
        if (inetSocketAddress != null) {
            return inetSocketAddress.getPort();
        }
        return super.getLocalPort();
    }

    /* access modifiers changed from: protected */
    public void close() throws IOException {
        Socket socket = this.cmdsock;
        if (socket != null) {
            socket.close();
        }
        this.cmdsock = null;
        super.close();
    }

    private String getUserName() {
        if (!this.applicationSetProxy) {
            return (String) AccessController.doPrivileged(new GetPropertyAction("user.name"));
        }
        try {
            return System.getProperty("user.name");
        } catch (SecurityException unused) {
            return "";
        }
    }
}
