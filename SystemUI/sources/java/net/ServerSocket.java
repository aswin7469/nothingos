package java.net;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.channels.ServerSocketChannel;
import java.p026io.Closeable;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Set;
import sun.security.util.SecurityConstants;

public class ServerSocket implements Closeable {
    private static SocketImplFactory factory = null;
    private static Set<SocketOption<?>> options = null;
    private static boolean optionsSet = false;
    private boolean bound;
    private Object closeLock;
    private boolean closed;
    private boolean created;
    /* access modifiers changed from: private */
    public SocketImpl impl;
    private boolean oldImpl;

    private static Void checkPermission() {
        return null;
    }

    public ServerSocketChannel getChannel() {
        return null;
    }

    public void setPerformancePreferences(int i, int i2, int i3) {
    }

    ServerSocket(SocketImpl socketImpl) {
        this.created = false;
        this.bound = false;
        this.closed = false;
        this.closeLock = new Object();
        this.oldImpl = false;
        checkPermission();
        this.impl = socketImpl;
        socketImpl.setServerSocket(this);
    }

    public ServerSocket() throws IOException {
        this.created = false;
        this.bound = false;
        this.closed = false;
        this.closeLock = new Object();
        this.oldImpl = false;
        setImpl();
    }

    public ServerSocket(int i) throws IOException {
        this(i, 50, (InetAddress) null);
    }

    public ServerSocket(int i, int i2) throws IOException {
        this(i, i2, (InetAddress) null);
    }

    public ServerSocket(int i, int i2, InetAddress inetAddress) throws IOException {
        this.created = false;
        this.bound = false;
        this.closed = false;
        this.closeLock = new Object();
        this.oldImpl = false;
        setImpl();
        if (i < 0 || i > 65535) {
            throw new IllegalArgumentException("Port value out of range: " + i);
        }
        try {
            bind(new InetSocketAddress(inetAddress, i), i2 < 1 ? 50 : i2);
        } catch (SecurityException e) {
            close();
            throw e;
        } catch (IOException e2) {
            close();
            throw e2;
        }
    }

    public SocketImpl getImpl() throws SocketException {
        if (!this.created) {
            createImpl();
        }
        return this.impl;
    }

    private void checkOldImpl() {
        if (this.impl != null) {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                    public Void run() throws NoSuchMethodException {
                        ServerSocket.this.impl.getClass().getDeclaredMethod(SecurityConstants.SOCKET_CONNECT_ACTION, SocketAddress.class, Integer.TYPE);
                        return null;
                    }
                });
            } catch (PrivilegedActionException unused) {
                this.oldImpl = true;
            }
        }
    }

    private void setImpl() {
        SocketImplFactory socketImplFactory = factory;
        if (socketImplFactory != null) {
            this.impl = socketImplFactory.createSocketImpl();
            checkOldImpl();
        } else {
            this.impl = new SocksSocketImpl();
        }
        SocketImpl socketImpl = this.impl;
        if (socketImpl != null) {
            socketImpl.setServerSocket(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void createImpl() throws SocketException {
        if (this.impl == null) {
            setImpl();
        }
        try {
            this.impl.create(true);
            this.created = true;
        } catch (IOException e) {
            throw new SocketException(e.getMessage());
        }
    }

    public void bind(SocketAddress socketAddress) throws IOException {
        bind(socketAddress, 50);
    }

    public void bind(SocketAddress socketAddress, int i) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (this.oldImpl || !isBound()) {
            if (socketAddress == null) {
                socketAddress = new InetSocketAddress(0);
            }
            if (socketAddress instanceof InetSocketAddress) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
                if (!inetSocketAddress.isUnresolved()) {
                    if (i < 1) {
                        i = 50;
                    }
                    try {
                        SecurityManager securityManager = System.getSecurityManager();
                        if (securityManager != null) {
                            securityManager.checkListen(inetSocketAddress.getPort());
                        }
                        getImpl().bind(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                        getImpl().listen(i);
                        this.bound = true;
                    } catch (SecurityException e) {
                        this.bound = false;
                        throw e;
                    } catch (IOException e2) {
                        this.bound = false;
                        throw e2;
                    }
                } else {
                    throw new SocketException("Unresolved address");
                }
            } else {
                throw new IllegalArgumentException("Unsupported address type");
            }
        } else {
            throw new SocketException("Already bound");
        }
    }

    public InetAddress getInetAddress() {
        if (!isBound()) {
            return null;
        }
        try {
            InetAddress inetAddress = getImpl().getInetAddress();
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkConnect(inetAddress.getHostAddress(), -1);
            }
            return inetAddress;
        } catch (SecurityException unused) {
            return InetAddress.getLoopbackAddress();
        } catch (SocketException unused2) {
            return null;
        }
    }

    public int getLocalPort() {
        if (!isBound()) {
            return -1;
        }
        try {
            return getImpl().getLocalPort();
        } catch (SocketException unused) {
            return -1;
        }
    }

    public SocketAddress getLocalSocketAddress() {
        if (!isBound()) {
            return null;
        }
        return new InetSocketAddress(getInetAddress(), getLocalPort());
    }

    public Socket accept() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (isBound()) {
            SocketImpl socketImpl = null;
            Socket socket = new Socket((SocketImpl) null);
            implAccept(socket);
            return socket;
        } else {
            throw new SocketException("Socket is not bound yet");
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0054  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void implAccept(java.net.Socket r4) throws java.p026io.IOException {
        /*
            r3 = this;
            r0 = 0
            java.net.SocketImpl r1 = r4.impl     // Catch:{ IOException -> 0x0051, SecurityException -> 0x0048 }
            if (r1 != 0) goto L_0x0009
            r4.setImpl()     // Catch:{ IOException -> 0x0051, SecurityException -> 0x0048 }
            goto L_0x000e
        L_0x0009:
            java.net.SocketImpl r1 = r4.impl     // Catch:{ IOException -> 0x0051, SecurityException -> 0x0048 }
            r1.reset()     // Catch:{ IOException -> 0x0051, SecurityException -> 0x0048 }
        L_0x000e:
            java.net.SocketImpl r1 = r4.impl     // Catch:{ IOException -> 0x0051, SecurityException -> 0x0048 }
            r4.impl = r0     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            java.net.InetAddress r0 = new java.net.InetAddress     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            r0.<init>()     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            r1.address = r0     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            java.io.FileDescriptor r0 = new java.io.FileDescriptor     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            r0.<init>()     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            r1.f557fd = r0     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            java.net.SocketImpl r3 = r3.getImpl()     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            r3.accept(r1)     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            java.lang.SecurityManager r3 = java.lang.System.getSecurityManager()     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            if (r3 == 0) goto L_0x003c
            java.net.InetAddress r0 = r1.getInetAddress()     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            java.lang.String r0 = r0.getHostAddress()     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            int r2 = r1.getPort()     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
            r3.checkAccept(r0, r2)     // Catch:{ IOException -> 0x0045, SecurityException -> 0x0042 }
        L_0x003c:
            r4.impl = r1
            r4.postAccept()
            return
        L_0x0042:
            r3 = move-exception
            r0 = r1
            goto L_0x0049
        L_0x0045:
            r3 = move-exception
            r0 = r1
            goto L_0x0052
        L_0x0048:
            r3 = move-exception
        L_0x0049:
            if (r0 == 0) goto L_0x004e
            r0.reset()
        L_0x004e:
            r4.impl = r0
            throw r3
        L_0x0051:
            r3 = move-exception
        L_0x0052:
            if (r0 == 0) goto L_0x0057
            r0.reset()
        L_0x0057:
            r4.impl = r0
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.ServerSocket.implAccept(java.net.Socket):void");
    }

    public void close() throws IOException {
        synchronized (this.closeLock) {
            if (!isClosed()) {
                if (this.created) {
                    this.impl.close();
                }
                this.closed = true;
            }
        }
    }

    public boolean isBound() {
        return this.bound || this.oldImpl;
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this.closeLock) {
            z = this.closed;
        }
        return z;
    }

    public synchronized void setSoTimeout(int i) throws SocketException {
        if (!isClosed()) {
            getImpl().setOption(4102, Integer.valueOf(i));
        } else {
            throw new SocketException("Socket is closed");
        }
    }

    public synchronized int getSoTimeout() throws IOException {
        if (!isClosed()) {
            Object option = getImpl().getOption(4102);
            if (!(option instanceof Integer)) {
                return 0;
            }
            return ((Integer) option).intValue();
        }
        throw new SocketException("Socket is closed");
    }

    public void setReuseAddress(boolean z) throws SocketException {
        if (!isClosed()) {
            getImpl().setOption(4, Boolean.valueOf(z));
            return;
        }
        throw new SocketException("Socket is closed");
    }

    public boolean getReuseAddress() throws SocketException {
        if (!isClosed()) {
            return ((Boolean) getImpl().getOption(4)).booleanValue();
        }
        throw new SocketException("Socket is closed");
    }

    public String toString() {
        InetAddress inetAddress;
        if (!isBound()) {
            return "ServerSocket[unbound]";
        }
        if (System.getSecurityManager() != null) {
            inetAddress = InetAddress.getLoopbackAddress();
        } else {
            inetAddress = this.impl.getInetAddress();
        }
        return "ServerSocket[addr=" + inetAddress + ",localport=" + this.impl.getLocalPort() + NavigationBarInflaterView.SIZE_MOD_END;
    }

    /* access modifiers changed from: package-private */
    public void setBound() {
        this.bound = true;
    }

    /* access modifiers changed from: package-private */
    public void setCreated() {
        this.created = true;
    }

    public static synchronized void setSocketFactory(SocketImplFactory socketImplFactory) throws IOException {
        synchronized (ServerSocket.class) {
            if (factory == null) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkSetFactory();
                }
                factory = socketImplFactory;
            } else {
                throw new SocketException("factory already defined");
            }
        }
    }

    public synchronized void setReceiveBufferSize(int i) throws SocketException {
        if (i > 0) {
            try {
                if (!isClosed()) {
                    getImpl().setOption(4098, Integer.valueOf(i));
                } else {
                    throw new SocketException("Socket is closed");
                }
            } catch (Throwable th) {
                throw th;
            }
        } else {
            throw new IllegalArgumentException("negative receive size");
        }
    }

    public synchronized int getReceiveBufferSize() throws SocketException {
        Object option;
        if (!isClosed()) {
            option = getImpl().getOption(4098);
        } else {
            throw new SocketException("Socket is closed");
        }
        return option instanceof Integer ? ((Integer) option).intValue() : 0;
    }

    public <T> ServerSocket setOption(SocketOption<T> socketOption, T t) throws IOException {
        getImpl().setOption(socketOption, t);
        return this;
    }

    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        return getImpl().getOption(socketOption);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:8|9|10|11|12|13|14) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x001a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Set<java.net.SocketOption<?>> supportedOptions() {
        /*
            r2 = this;
            java.lang.Class<java.net.ServerSocket> r0 = java.net.ServerSocket.class
            monitor-enter(r0)
            boolean r1 = optionsSet     // Catch:{ all -> 0x0027 }
            if (r1 == 0) goto L_0x000b
            java.util.Set<java.net.SocketOption<?>> r2 = options     // Catch:{ all -> 0x0027 }
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            return r2
        L_0x000b:
            java.net.SocketImpl r2 = r2.getImpl()     // Catch:{ IOException -> 0x001a }
            java.util.Set r2 = r2.supportedOptions()     // Catch:{ IOException -> 0x001a }
            java.util.Set r2 = java.util.Collections.unmodifiableSet(r2)     // Catch:{ IOException -> 0x001a }
            options = r2     // Catch:{ IOException -> 0x001a }
            goto L_0x0020
        L_0x001a:
            java.util.Set r2 = java.util.Collections.emptySet()     // Catch:{ all -> 0x0027 }
            options = r2     // Catch:{ all -> 0x0027 }
        L_0x0020:
            r2 = 1
            optionsSet = r2     // Catch:{ all -> 0x0027 }
            java.util.Set<java.net.SocketOption<?>> r2 = options     // Catch:{ all -> 0x0027 }
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            return r2
        L_0x0027:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.ServerSocket.supportedOptions():java.util.Set");
    }

    public FileDescriptor getFileDescriptor$() {
        return this.impl.getFileDescriptor();
    }
}
