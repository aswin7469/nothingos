package com.android.okhttp.internal.p006io;

import com.android.okhttp.Address;
import com.android.okhttp.Connection;
import com.android.okhttp.ConnectionSpec;
import com.android.okhttp.Handshake;
import com.android.okhttp.HttpUrl;
import com.android.okhttp.Protocol;
import com.android.okhttp.Request;
import com.android.okhttp.Response;
import com.android.okhttp.Route;
import com.android.okhttp.internal.ConnectionSpecSelector;
import com.android.okhttp.internal.Platform;
import com.android.okhttp.internal.Util;
import com.android.okhttp.internal.Version;
import com.android.okhttp.internal.framed.FramedConnection;
import com.android.okhttp.internal.http.Http1xStream;
import com.android.okhttp.internal.http.OkHeaders;
import com.android.okhttp.internal.http.RouteException;
import com.android.okhttp.internal.http.StreamAllocation;
import com.android.okhttp.internal.tls.TrustRootIndex;
import com.android.okhttp.okio.BufferedSink;
import com.android.okhttp.okio.BufferedSource;
import com.android.okhttp.okio.Okio;
import com.android.okhttp.okio.Source;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownServiceException;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;

/* renamed from: com.android.okhttp.internal.io.RealConnection */
public final class RealConnection implements Connection {
    private static SSLSocketFactory lastSslSocketFactory;
    private static TrustRootIndex lastTrustRootIndex;
    public final List<Reference<StreamAllocation>> allocations = new ArrayList();
    public volatile FramedConnection framedConnection;
    private Handshake handshake;
    public long idleAtNanos = Long.MAX_VALUE;
    public boolean noNewStreams;
    private Protocol protocol;
    private Socket rawSocket;
    private final Route route;
    public BufferedSink sink;
    public Socket socket;
    public BufferedSource source;
    public int streamCount;

    public RealConnection(Route route2) {
        this.route = route2;
    }

    public void connect(int i, int i2, int i3, List<ConnectionSpec> list, boolean z) throws RouteException {
        Socket socket2;
        if (this.protocol == null) {
            ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector(list);
            Proxy proxy = this.route.getProxy();
            Address address = this.route.getAddress();
            if (this.route.getAddress().getSslSocketFactory() != null || list.contains(ConnectionSpec.CLEARTEXT)) {
                RouteException routeException = null;
                while (this.protocol == null) {
                    try {
                        if (proxy.type() != Proxy.Type.DIRECT) {
                            if (proxy.type() != Proxy.Type.HTTP) {
                                socket2 = new Socket(proxy);
                                this.rawSocket = socket2;
                                connectSocket(i, i2, i3, connectionSpecSelector);
                            }
                        }
                        socket2 = address.getSocketFactory().createSocket();
                        this.rawSocket = socket2;
                        connectSocket(i, i2, i3, connectionSpecSelector);
                    } catch (IOException e) {
                        Util.closeQuietly(this.socket);
                        Util.closeQuietly(this.rawSocket);
                        this.socket = null;
                        this.rawSocket = null;
                        this.source = null;
                        this.sink = null;
                        this.handshake = null;
                        this.protocol = null;
                        if (routeException == null) {
                            routeException = new RouteException(e);
                        } else {
                            routeException.addConnectException(e);
                        }
                        if (!z || !connectionSpecSelector.connectionFailed(e)) {
                            throw routeException;
                        }
                    }
                }
                return;
            }
            throw new RouteException(new UnknownServiceException("CLEARTEXT communication not supported: " + list));
        }
        throw new IllegalStateException("already connected");
    }

    private void connectSocket(int i, int i2, int i3, ConnectionSpecSelector connectionSpecSelector) throws IOException {
        this.rawSocket.setSoTimeout(i2);
        try {
            Platform.get().connectSocket(this.rawSocket, this.route.getSocketAddress(), i);
            this.source = Okio.buffer(Okio.source(this.rawSocket));
            this.sink = Okio.buffer(Okio.sink(this.rawSocket));
            if (this.route.getAddress().getSslSocketFactory() != null) {
                connectTls(i2, i3, connectionSpecSelector);
            } else {
                this.protocol = Protocol.HTTP_1_1;
                this.socket = this.rawSocket;
            }
            if (this.protocol == Protocol.SPDY_3 || this.protocol == Protocol.HTTP_2) {
                this.socket.setSoTimeout(0);
                FramedConnection build = new FramedConnection.Builder(true).socket(this.socket, this.route.getAddress().url().host(), this.source, this.sink).protocol(this.protocol).build();
                build.sendConnectionPreface();
                this.framedConnection = build;
            }
        } catch (ConnectException unused) {
            throw new ConnectException("Failed to connect to " + this.route.getSocketAddress());
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: javax.net.ssl.SSLSocket} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v3, resolved type: javax.net.ssl.SSLSocket} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: javax.net.ssl.SSLSocket} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: javax.net.ssl.SSLSocket} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x011c A[Catch:{ all -> 0x0113 }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0122 A[Catch:{ all -> 0x0113 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0125  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void connectTls(int r7, int r8, com.android.okhttp.internal.ConnectionSpecSelector r9) throws java.p026io.IOException {
        /*
            r6 = this;
            java.lang.String r0 = "Hostname "
            com.android.okhttp.Route r1 = r6.route
            boolean r1 = r1.requiresTunnel()
            if (r1 == 0) goto L_0x000d
            r6.createTunnel(r7, r8)
        L_0x000d:
            com.android.okhttp.Route r7 = r6.route
            com.android.okhttp.Address r7 = r7.getAddress()
            javax.net.ssl.SSLSocketFactory r8 = r7.getSslSocketFactory()
            r1 = 0
            java.net.Socket r2 = r6.rawSocket     // Catch:{ AssertionError -> 0x0115 }
            java.lang.String r3 = r7.getUriHost()     // Catch:{ AssertionError -> 0x0115 }
            int r4 = r7.getUriPort()     // Catch:{ AssertionError -> 0x0115 }
            r5 = 1
            java.net.Socket r8 = r8.createSocket(r2, r3, r4, r5)     // Catch:{ AssertionError -> 0x0115 }
            javax.net.ssl.SSLSocket r8 = (javax.net.ssl.SSLSocket) r8     // Catch:{ AssertionError -> 0x0115 }
            com.android.okhttp.ConnectionSpec r9 = r9.configureSecureSocket(r8)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            boolean r2 = r9.supportsTlsExtensions()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            if (r2 == 0) goto L_0x0042
            com.android.okhttp.internal.Platform r2 = com.android.okhttp.internal.Platform.get()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r3 = r7.getUriHost()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.util.List r4 = r7.getProtocols()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r2.configureTlsExtensions(r8, r3, r4)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
        L_0x0042:
            r8.startHandshake()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            javax.net.ssl.SSLSession r2 = r8.getSession()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            com.android.okhttp.Handshake r2 = com.android.okhttp.Handshake.get(r2)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            javax.net.ssl.HostnameVerifier r3 = r7.getHostnameVerifier()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r4 = r7.getUriHost()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            javax.net.ssl.SSLSession r5 = r8.getSession()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            boolean r3 = r3.verify(r4, r5)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            if (r3 == 0) goto L_0x00c4
            com.android.okhttp.CertificatePinner r0 = r7.getCertificatePinner()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            com.android.okhttp.CertificatePinner r3 = com.android.okhttp.CertificatePinner.DEFAULT     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            if (r0 == r3) goto L_0x0087
            javax.net.ssl.SSLSocketFactory r0 = r7.getSslSocketFactory()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            com.android.okhttp.internal.tls.TrustRootIndex r0 = trustRootIndex(r0)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            com.android.okhttp.internal.tls.CertificateChainCleaner r3 = new com.android.okhttp.internal.tls.CertificateChainCleaner     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r3.<init>(r0)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.util.List r0 = r2.peerCertificates()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.util.List r0 = r3.clean(r0)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            com.android.okhttp.CertificatePinner r3 = r7.getCertificatePinner()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r7 = r7.getUriHost()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r3.check((java.lang.String) r7, (java.util.List<java.security.cert.Certificate>) r0)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
        L_0x0087:
            boolean r7 = r9.supportsTlsExtensions()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            if (r7 == 0) goto L_0x0095
            com.android.okhttp.internal.Platform r7 = com.android.okhttp.internal.Platform.get()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r1 = r7.getSelectedProtocol(r8)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
        L_0x0095:
            r6.socket = r8     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            com.android.okhttp.okio.Source r7 = com.android.okhttp.okio.Okio.source((java.net.Socket) r8)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            com.android.okhttp.okio.BufferedSource r7 = com.android.okhttp.okio.Okio.buffer((com.android.okhttp.okio.Source) r7)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r6.source = r7     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.net.Socket r7 = r6.socket     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            com.android.okhttp.okio.Sink r7 = com.android.okhttp.okio.Okio.sink((java.net.Socket) r7)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            com.android.okhttp.okio.BufferedSink r7 = com.android.okhttp.okio.Okio.buffer((com.android.okhttp.okio.Sink) r7)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r6.sink = r7     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r6.handshake = r2     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            if (r1 == 0) goto L_0x00b6
            com.android.okhttp.Protocol r7 = com.android.okhttp.Protocol.get(r1)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            goto L_0x00b8
        L_0x00b6:
            com.android.okhttp.Protocol r7 = com.android.okhttp.Protocol.HTTP_1_1     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
        L_0x00b8:
            r6.protocol = r7     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            if (r8 == 0) goto L_0x00c3
            com.android.okhttp.internal.Platform r6 = com.android.okhttp.internal.Platform.get()
            r6.afterHandshake(r8)
        L_0x00c3:
            return
        L_0x00c4:
            java.util.List r6 = r2.peerCertificates()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r9 = 0
            java.lang.Object r6 = r6.get(r9)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.security.cert.X509Certificate r6 = (java.security.cert.X509Certificate) r6     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            javax.net.ssl.SSLPeerUnverifiedException r9 = new javax.net.ssl.SSLPeerUnverifiedException     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r1.<init>((java.lang.String) r0)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r7 = r7.getUriHost()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r1.append((java.lang.String) r7)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r7 = " not verified:\n    certificate: "
            r1.append((java.lang.String) r7)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r7 = com.android.okhttp.CertificatePinner.pin(r6)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r1.append((java.lang.String) r7)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r7 = "\n    DN: "
            r1.append((java.lang.String) r7)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.security.Principal r7 = r6.getSubjectDN()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r7 = r7.getName()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r1.append((java.lang.String) r7)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r7 = "\n    subjectAltNames: "
            r1.append((java.lang.String) r7)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.util.List r6 = com.android.okhttp.internal.tls.OkHostnameVerifier.allSubjectAltNames(r6)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r1.append((java.lang.Object) r6)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            java.lang.String r6 = r1.toString()     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            r9.<init>(r6)     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
            throw r9     // Catch:{ AssertionError -> 0x0110, all -> 0x010d }
        L_0x010d:
            r6 = move-exception
            r1 = r8
            goto L_0x0123
        L_0x0110:
            r6 = move-exception
            r1 = r8
            goto L_0x0116
        L_0x0113:
            r6 = move-exception
            goto L_0x0123
        L_0x0115:
            r6 = move-exception
        L_0x0116:
            boolean r7 = com.android.okhttp.internal.Util.isAndroidGetsocknameError(r6)     // Catch:{ all -> 0x0113 }
            if (r7 == 0) goto L_0x0122
            java.io.IOException r7 = new java.io.IOException     // Catch:{ all -> 0x0113 }
            r7.<init>((java.lang.Throwable) r6)     // Catch:{ all -> 0x0113 }
            throw r7     // Catch:{ all -> 0x0113 }
        L_0x0122:
            throw r6     // Catch:{ all -> 0x0113 }
        L_0x0123:
            if (r1 == 0) goto L_0x012c
            com.android.okhttp.internal.Platform r7 = com.android.okhttp.internal.Platform.get()
            r7.afterHandshake(r1)
        L_0x012c:
            com.android.okhttp.internal.Util.closeQuietly((java.net.Socket) r1)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.p006io.RealConnection.connectTls(int, int, com.android.okhttp.internal.ConnectionSpecSelector):void");
    }

    private static synchronized TrustRootIndex trustRootIndex(SSLSocketFactory sSLSocketFactory) {
        TrustRootIndex trustRootIndex;
        synchronized (RealConnection.class) {
            if (sSLSocketFactory != lastSslSocketFactory) {
                lastTrustRootIndex = Platform.get().trustRootIndex(Platform.get().trustManager(sSLSocketFactory));
                lastSslSocketFactory = sSLSocketFactory;
            }
            trustRootIndex = lastTrustRootIndex;
        }
        return trustRootIndex;
    }

    private void createTunnel(int i, int i2) throws IOException {
        Request createTunnelRequest = createTunnelRequest();
        HttpUrl httpUrl = createTunnelRequest.httpUrl();
        String str = "CONNECT " + Util.hostHeader(httpUrl, true) + " HTTP/1.1";
        do {
            Http1xStream http1xStream = new Http1xStream((StreamAllocation) null, this.source, this.sink);
            this.source.timeout().timeout((long) i, TimeUnit.MILLISECONDS);
            this.sink.timeout().timeout((long) i2, TimeUnit.MILLISECONDS);
            http1xStream.writeRequest(createTunnelRequest.headers(), str);
            http1xStream.finishRequest();
            Response build = http1xStream.readResponse().request(createTunnelRequest).build();
            long contentLength = OkHeaders.contentLength(build);
            if (contentLength == -1) {
                contentLength = 0;
            }
            Source newFixedLengthSource = http1xStream.newFixedLengthSource(contentLength);
            Util.skipAll(newFixedLengthSource, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            newFixedLengthSource.close();
            int code = build.code();
            if (code != 200) {
                if (code == 407) {
                    createTunnelRequest = OkHeaders.processAuthHeader(this.route.getAddress().getAuthenticator(), build, this.route.getProxy());
                } else {
                    throw new IOException("Unexpected response code for CONNECT: " + build.code());
                }
            } else if (!this.source.buffer().exhausted() || !this.sink.buffer().exhausted()) {
                throw new IOException("TLS tunnel buffered too many bytes!");
            } else {
                return;
            }
        } while (createTunnelRequest != null);
        throw new IOException("Failed to authenticate with proxy");
    }

    private Request createTunnelRequest() throws IOException {
        return new Request.Builder().url(this.route.getAddress().url()).header("Host", Util.hostHeader(this.route.getAddress().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
    }

    /* access modifiers changed from: package-private */
    public boolean isConnected() {
        return this.protocol != null;
    }

    public Route getRoute() {
        return this.route;
    }

    public void cancel() {
        Util.closeQuietly(this.rawSocket);
    }

    public Socket getSocket() {
        return this.socket;
    }

    public int allocationLimit() {
        FramedConnection framedConnection2 = this.framedConnection;
        if (framedConnection2 != null) {
            return framedConnection2.maxConcurrentStreams();
        }
        return 1;
    }

    public boolean isHealthy(boolean z) {
        int soTimeout;
        if (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown()) {
            return false;
        }
        if (this.framedConnection == null && z) {
            try {
                soTimeout = this.socket.getSoTimeout();
                this.socket.setSoTimeout(1);
                if (this.source.exhausted()) {
                    this.socket.setSoTimeout(soTimeout);
                    return false;
                }
                this.socket.setSoTimeout(soTimeout);
                return true;
            } catch (SocketTimeoutException unused) {
            } catch (IOException unused2) {
                return false;
            } catch (Throwable th) {
                this.socket.setSoTimeout(soTimeout);
                throw th;
            }
        }
        return true;
    }

    public Handshake getHandshake() {
        return this.handshake;
    }

    public boolean isMultiplexed() {
        return this.framedConnection != null;
    }

    public Protocol getProtocol() {
        Protocol protocol2 = this.protocol;
        return protocol2 != null ? protocol2 : Protocol.HTTP_1_1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Connection{");
        sb.append(this.route.getAddress().url().host());
        sb.append(":");
        sb.append(this.route.getAddress().url().port());
        sb.append(", proxy=");
        sb.append((Object) this.route.getProxy());
        sb.append(" hostAddress=");
        sb.append((Object) this.route.getSocketAddress());
        sb.append(" cipherSuite=");
        Handshake handshake2 = this.handshake;
        sb.append(handshake2 != null ? handshake2.cipherSuite() : "none");
        sb.append(" protocol=");
        sb.append((Object) this.protocol);
        sb.append('}');
        return sb.toString();
    }
}
