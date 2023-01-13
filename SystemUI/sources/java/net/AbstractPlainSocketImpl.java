package java.net;

import dalvik.system.BlockGuard;
import dalvik.system.CloseGuard;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import sun.net.NetHooks;
import sun.net.ResourceManager;

abstract class AbstractPlainSocketImpl extends SocketImpl {
    public static final int SHUT_RD = 0;
    public static final int SHUT_WR = 1;
    private int CONNECTION_NOT_RESET = 0;
    private int CONNECTION_RESET = 2;
    private int CONNECTION_RESET_PENDING = 1;
    protected boolean closePending = false;
    protected final Object fdLock = new Object();
    protected int fdUseCount = 0;
    private final CloseGuard guard = CloseGuard.get();
    private final Object resetLock = new Object();
    private int resetState;
    private boolean shut_rd = false;
    private boolean shut_wr = false;
    private SocketInputStream socketInputStream = null;
    private SocketOutputStream socketOutputStream = null;
    protected boolean stream;
    int timeout;

    /* access modifiers changed from: package-private */
    public abstract void socketAccept(SocketImpl socketImpl) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract int socketAvailable() throws IOException;

    /* access modifiers changed from: package-private */
    public abstract void socketBind(InetAddress inetAddress, int i) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract void socketClose0(boolean z) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract void socketConnect(InetAddress inetAddress, int i, int i2) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract void socketCreate(boolean z) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract Object socketGetOption(int i) throws SocketException;

    /* access modifiers changed from: package-private */
    public abstract void socketListen(int i) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract void socketSendUrgentData(int i) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract void socketSetOption(int i, Object obj) throws SocketException;

    /* access modifiers changed from: package-private */
    public abstract void socketShutdown(int i) throws IOException;

    /* access modifiers changed from: protected */
    public boolean supportsUrgentData() {
        return true;
    }

    AbstractPlainSocketImpl() {
    }

    /* access modifiers changed from: protected */
    public synchronized void create(boolean stream2) throws IOException {
        this.stream = stream2;
        if (!stream2) {
            ResourceManager.beforeUdpCreate();
            try {
                socketCreate(false);
            } catch (IOException e) {
                ResourceManager.afterUdpClose();
                throw e;
            }
        } else {
            socketCreate(true);
        }
        if (this.socket != null) {
            this.socket.setCreated();
        }
        if (this.serverSocket != null) {
            this.serverSocket.setCreated();
        }
        if (this.f557fd != null && this.f557fd.valid()) {
            this.guard.open("close");
        }
    }

    /* access modifiers changed from: protected */
    public void connect(String host, int port) throws UnknownHostException, IOException {
        boolean z = false;
        try {
            InetAddress byName = InetAddress.getByName(host);
            this.port = port;
            this.address = byName;
            connectToAddress(byName, port, this.timeout);
            z = true;
        } finally {
            if (!z) {
                try {
                    close();
                } catch (IOException e) {
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void connect(InetAddress address, int port) throws IOException {
        this.port = port;
        this.address = address;
        try {
            connectToAddress(address, port, this.timeout);
        } catch (IOException e) {
            close();
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    public void connect(SocketAddress address, int timeout2) throws IOException {
        boolean z = false;
        if (address != null) {
            try {
                if (address instanceof InetSocketAddress) {
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
                    if (!inetSocketAddress.isUnresolved()) {
                        this.port = inetSocketAddress.getPort();
                        this.address = inetSocketAddress.getAddress();
                        connectToAddress(this.address, this.port, timeout2);
                        z = true;
                        if (!z) {
                            try {
                                return;
                            } catch (IOException e) {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        throw new UnknownHostException(inetSocketAddress.getHostName());
                    }
                }
            } finally {
                if (!z) {
                    try {
                        close();
                    } catch (IOException e2) {
                    }
                }
            }
        }
        throw new IllegalArgumentException("unsupported address type");
    }

    private void connectToAddress(InetAddress address, int port, int timeout2) throws IOException {
        if (address.isAnyLocalAddress()) {
            doConnect(InetAddress.getLocalHost(), port, timeout2);
        } else {
            doConnect(address, port, timeout2);
        }
    }

    public void setOption(int opt, Object val) throws SocketException {
        if (!isClosedOrPending()) {
            if (opt == 4102) {
                this.timeout = ((Integer) val).intValue();
            }
            socketSetOption(opt, val);
            return;
        }
        throw new SocketException("Socket Closed");
    }

    public Object getOption(int opt) throws SocketException {
        if (!isClosedOrPending()) {
            return opt == 4102 ? new Integer(this.timeout) : socketGetOption(opt);
        }
        throw new SocketException("Socket Closed");
    }

    /* access modifiers changed from: package-private */
    public synchronized void doConnect(InetAddress address, int port, int timeout2) throws IOException {
        synchronized (this.fdLock) {
            if (!this.closePending && (this.socket == null || !this.socket.isBound())) {
                NetHooks.beforeTcpConnect(this.f557fd, address, port);
            }
        }
        try {
            acquireFD();
            try {
                BlockGuard.getThreadPolicy().onNetwork();
                socketConnect(address, port, timeout2);
                synchronized (this.fdLock) {
                    if (this.closePending) {
                        throw new SocketException("Socket closed");
                    }
                }
                if (this.socket != null) {
                    this.socket.setBound();
                    this.socket.setConnected();
                }
                releaseFD();
            } catch (Throwable th) {
                releaseFD();
                throw th;
            }
        } catch (IOException e) {
            close();
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void bind(InetAddress address, int lport) throws IOException {
        synchronized (this.fdLock) {
            if (!this.closePending && (this.socket == null || !this.socket.isBound())) {
                NetHooks.beforeTcpBind(this.f557fd, address, lport);
            }
        }
        socketBind(address, lport);
        if (this.socket != null) {
            this.socket.setBound();
        }
        if (this.serverSocket != null) {
            this.serverSocket.setBound();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void listen(int count) throws IOException {
        socketListen(count);
    }

    /* access modifiers changed from: protected */
    public void accept(SocketImpl s) throws IOException {
        acquireFD();
        try {
            BlockGuard.getThreadPolicy().onNetwork();
            socketAccept(s);
        } finally {
            releaseFD();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized InputStream getInputStream() throws IOException {
        synchronized (this.fdLock) {
            if (isClosedOrPending()) {
                throw new IOException("Socket Closed");
            } else if (this.shut_rd) {
                throw new IOException("Socket input is shutdown");
            } else if (this.socketInputStream == null) {
                this.socketInputStream = new SocketInputStream(this);
            }
        }
        return this.socketInputStream;
    }

    /* access modifiers changed from: package-private */
    public void setInputStream(SocketInputStream in) {
        this.socketInputStream = in;
    }

    /* access modifiers changed from: protected */
    public synchronized OutputStream getOutputStream() throws IOException {
        synchronized (this.fdLock) {
            if (isClosedOrPending()) {
                throw new IOException("Socket Closed");
            } else if (this.shut_wr) {
                throw new IOException("Socket output is shutdown");
            } else if (this.socketOutputStream == null) {
                this.socketOutputStream = new SocketOutputStream(this);
            }
        }
        return this.socketOutputStream;
    }

    /* access modifiers changed from: package-private */
    public void setAddress(InetAddress address) {
        this.address = address;
    }

    /* access modifiers changed from: package-private */
    public void setPort(int port) {
        this.port = port;
    }

    /* access modifiers changed from: package-private */
    public void setLocalPort(int localport) {
        this.localport = localport;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0039, code lost:
        return 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int available() throws java.p026io.IOException {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = r3.isClosedOrPending()     // Catch:{ all -> 0x0043 }
            if (r0 != 0) goto L_0x003b
            boolean r0 = r3.isConnectionReset()     // Catch:{ all -> 0x0043 }
            if (r0 != 0) goto L_0x0038
            boolean r0 = r3.shut_rd     // Catch:{ all -> 0x0043 }
            if (r0 == 0) goto L_0x0012
            goto L_0x0038
        L_0x0012:
            r0 = 0
            int r1 = r3.socketAvailable()     // Catch:{ ConnectionResetException -> 0x0025 }
            r0 = r1
            if (r0 != 0) goto L_0x0023
            boolean r1 = r3.isConnectionResetPending()     // Catch:{ ConnectionResetException -> 0x0025 }
            if (r1 == 0) goto L_0x0023
            r3.setConnectionReset()     // Catch:{ ConnectionResetException -> 0x0025 }
        L_0x0023:
        L_0x0024:
            goto L_0x0036
        L_0x0025:
            r1 = move-exception
            r3.setConnectionResetPending()     // Catch:{ all -> 0x0043 }
            int r2 = r3.socketAvailable()     // Catch:{ ConnectionResetException -> 0x0034 }
            r0 = r2
            if (r0 != 0) goto L_0x0023
            r3.setConnectionReset()     // Catch:{ ConnectionResetException -> 0x0034 }
            goto L_0x0023
        L_0x0034:
            r2 = move-exception
            goto L_0x0024
        L_0x0036:
            monitor-exit(r3)
            return r0
        L_0x0038:
            monitor-exit(r3)
            r0 = 0
            return r0
        L_0x003b:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0043 }
            java.lang.String r1 = "Stream closed."
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0043 }
            throw r0     // Catch:{ all -> 0x0043 }
        L_0x0043:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.AbstractPlainSocketImpl.available():int");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x003d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.p026io.IOException {
        /*
            r3 = this;
            java.lang.Object r0 = r3.fdLock
            monitor-enter(r0)
            java.io.FileDescriptor r1 = r3.f557fd     // Catch:{ all -> 0x003e }
            if (r1 == 0) goto L_0x003b
            java.io.FileDescriptor r1 = r3.f557fd     // Catch:{ all -> 0x003e }
            boolean r1 = r1.valid()     // Catch:{ all -> 0x003e }
            if (r1 == 0) goto L_0x003b
            boolean r1 = r3.stream     // Catch:{ all -> 0x003e }
            if (r1 != 0) goto L_0x0016
            sun.net.ResourceManager.afterUdpClose()     // Catch:{ all -> 0x003e }
        L_0x0016:
            boolean r1 = r3.closePending     // Catch:{ all -> 0x003e }
            if (r1 != 0) goto L_0x003b
            r1 = 1
            r3.closePending = r1     // Catch:{ all -> 0x003e }
            dalvik.system.CloseGuard r2 = r3.guard     // Catch:{ all -> 0x003e }
            r2.close()     // Catch:{ all -> 0x003e }
            int r2 = r3.fdUseCount     // Catch:{ all -> 0x003e }
            if (r2 != 0) goto L_0x0035
            r3.socketPreClose()     // Catch:{ all -> 0x0030 }
            r3.socketClose()     // Catch:{ all -> 0x003e }
            monitor-exit(r0)     // Catch:{ all -> 0x003e }
            return
        L_0x0030:
            r1 = move-exception
            r3.socketClose()     // Catch:{ all -> 0x003e }
            throw r1     // Catch:{ all -> 0x003e }
        L_0x0035:
            int r2 = r2 - r1
            r3.fdUseCount = r2     // Catch:{ all -> 0x003e }
            r3.socketPreClose()     // Catch:{ all -> 0x003e }
        L_0x003b:
            monitor-exit(r0)     // Catch:{ all -> 0x003e }
            return
        L_0x003e:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x003e }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.AbstractPlainSocketImpl.close():void");
    }

    /* access modifiers changed from: package-private */
    public void reset() throws IOException {
        if (this.f557fd != null && this.f557fd.valid()) {
            socketClose();
            this.guard.close();
        }
        super.reset();
    }

    /* access modifiers changed from: protected */
    public void shutdownInput() throws IOException {
        if (this.f557fd != null && this.f557fd.valid()) {
            socketShutdown(0);
            SocketInputStream socketInputStream2 = this.socketInputStream;
            if (socketInputStream2 != null) {
                socketInputStream2.setEOF(true);
            }
            this.shut_rd = true;
        }
    }

    /* access modifiers changed from: protected */
    public void shutdownOutput() throws IOException {
        if (this.f557fd != null && this.f557fd.valid()) {
            socketShutdown(1);
            this.shut_wr = true;
        }
    }

    /* access modifiers changed from: protected */
    public void sendUrgentData(int data) throws IOException {
        if (this.f557fd == null || !this.f557fd.valid()) {
            throw new IOException("Socket Closed");
        }
        socketSendUrgentData(data);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws IOException {
        CloseGuard closeGuard = this.guard;
        if (closeGuard != null) {
            closeGuard.warnIfOpen();
        }
        close();
    }

    /* access modifiers changed from: package-private */
    public FileDescriptor acquireFD() {
        FileDescriptor fileDescriptor;
        synchronized (this.fdLock) {
            this.fdUseCount++;
            fileDescriptor = this.f557fd;
        }
        return fileDescriptor;
    }

    /* access modifiers changed from: package-private */
    public void releaseFD() {
        synchronized (this.fdLock) {
            int i = this.fdUseCount - 1;
            this.fdUseCount = i;
            if (i == -1 && this.f557fd != null) {
                try {
                    socketClose();
                } catch (IOException e) {
                }
            }
        }
    }

    public boolean isConnectionReset() {
        boolean z;
        synchronized (this.resetLock) {
            z = this.resetState == this.CONNECTION_RESET;
        }
        return z;
    }

    public boolean isConnectionResetPending() {
        boolean z;
        synchronized (this.resetLock) {
            z = this.resetState == this.CONNECTION_RESET_PENDING;
        }
        return z;
    }

    public void setConnectionReset() {
        synchronized (this.resetLock) {
            this.resetState = this.CONNECTION_RESET;
        }
    }

    public void setConnectionResetPending() {
        synchronized (this.resetLock) {
            if (this.resetState == this.CONNECTION_NOT_RESET) {
                this.resetState = this.CONNECTION_RESET_PENDING;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0018, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isClosedOrPending() {
        /*
            r2 = this;
            java.lang.Object r0 = r2.fdLock
            monitor-enter(r0)
            boolean r1 = r2.closePending     // Catch:{ all -> 0x001a }
            if (r1 != 0) goto L_0x0017
            java.io.FileDescriptor r1 = r2.f557fd     // Catch:{ all -> 0x001a }
            if (r1 == 0) goto L_0x0017
            java.io.FileDescriptor r1 = r2.f557fd     // Catch:{ all -> 0x001a }
            boolean r1 = r1.valid()     // Catch:{ all -> 0x001a }
            if (r1 != 0) goto L_0x0014
            goto L_0x0017
        L_0x0014:
            monitor-exit(r0)     // Catch:{ all -> 0x001a }
            r0 = 0
            return r0
        L_0x0017:
            monitor-exit(r0)     // Catch:{ all -> 0x001a }
            r0 = 1
            return r0
        L_0x001a:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x001a }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.AbstractPlainSocketImpl.isClosedOrPending():boolean");
    }

    public int getTimeout() {
        return this.timeout;
    }

    private void socketPreClose() throws IOException {
        socketClose0(true);
    }

    /* access modifiers changed from: protected */
    public void socketClose() throws IOException {
        socketClose0(false);
    }
}
