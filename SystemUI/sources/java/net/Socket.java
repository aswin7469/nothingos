package java.net;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.Proxy;
import java.nio.channels.SocketChannel;
import java.p026io.Closeable;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Set;
import sun.net.ApplicationProxy;
import sun.security.util.SecurityConstants;

public class Socket implements Closeable {
    private static SocketImplFactory factory = null;
    private static Set<SocketOption<?>> options = null;
    private static boolean optionsSet = false;
    private boolean bound;
    private Object closeLock;
    private boolean closed;
    private boolean connected;
    private boolean created;
    SocketImpl impl;
    private boolean oldImpl;
    private boolean shutIn;
    private boolean shutOut;

    private static Void checkPermission(SocketImpl socketImpl) {
        return null;
    }

    public SocketChannel getChannel() {
        return null;
    }

    public void setPerformancePreferences(int i, int i2, int i3) {
    }

    public Socket() {
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        setImpl();
    }

    public Socket(Proxy proxy) {
        Proxy proxy2;
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        if (proxy != null) {
            if (proxy == Proxy.NO_PROXY) {
                proxy2 = Proxy.NO_PROXY;
            } else {
                proxy2 = ApplicationProxy.create(proxy);
            }
            if (proxy2.type() == Proxy.Type.SOCKS) {
                SecurityManager securityManager = System.getSecurityManager();
                InetSocketAddress inetSocketAddress = (InetSocketAddress) proxy2.address();
                if (inetSocketAddress.getAddress() != null) {
                    checkAddress(inetSocketAddress.getAddress(), "Socket");
                }
                if (securityManager != null) {
                    inetSocketAddress = inetSocketAddress.isUnresolved() ? new InetSocketAddress(inetSocketAddress.getHostName(), inetSocketAddress.getPort()) : inetSocketAddress;
                    if (inetSocketAddress.isUnresolved()) {
                        securityManager.checkConnect(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
                    } else {
                        securityManager.checkConnect(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
                    }
                }
                SocksSocketImpl socksSocketImpl = new SocksSocketImpl(proxy2);
                this.impl = socksSocketImpl;
                socksSocketImpl.setSocket(this);
            } else if (proxy2 != Proxy.NO_PROXY) {
                throw new IllegalArgumentException("Invalid Proxy");
            } else if (factory == null) {
                PlainSocketImpl plainSocketImpl = new PlainSocketImpl();
                this.impl = plainSocketImpl;
                plainSocketImpl.setSocket(this);
            } else {
                setImpl();
            }
        } else {
            throw new IllegalArgumentException("Invalid Proxy");
        }
    }

    protected Socket(SocketImpl socketImpl) throws SocketException {
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        checkPermission(socketImpl);
        this.impl = socketImpl;
        if (socketImpl != null) {
            checkOldImpl();
            this.impl.setSocket(this);
        }
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public Socket(String str, int i) throws UnknownHostException, IOException {
        this(InetAddress.getAllByName(str), i, (SocketAddress) null, true);
        SocketAddress socketAddress = null;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public Socket(InetAddress inetAddress, int i) throws IOException {
        this(nonNullAddress(inetAddress), i, (SocketAddress) null, true);
        SocketAddress socketAddress = null;
    }

    public Socket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        this(InetAddress.getAllByName(str), i, (SocketAddress) new InetSocketAddress(inetAddress, i2), true);
    }

    public Socket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        this(nonNullAddress(inetAddress), i, (SocketAddress) new InetSocketAddress(inetAddress2, i2), true);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    @Deprecated
    public Socket(String str, int i, boolean z) throws IOException {
        this(InetAddress.getAllByName(str), i, (SocketAddress) null, z);
        SocketAddress socketAddress = null;
    }

    @Deprecated
    public Socket(InetAddress inetAddress, int i, boolean z) throws IOException {
        this(nonNullAddress(inetAddress), i, (SocketAddress) new InetSocketAddress(0), z);
    }

    private static InetAddress[] nonNullAddress(InetAddress inetAddress) {
        inetAddress.getClass();
        return new InetAddress[]{inetAddress};
    }

    private Socket(InetAddress[] inetAddressArr, int i, SocketAddress socketAddress, boolean z) throws IOException {
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        if (inetAddressArr == null || inetAddressArr.length == 0) {
            throw new SocketException("Impossible: empty address list");
        }
        int i2 = 0;
        while (i2 < inetAddressArr.length) {
            setImpl();
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddressArr[i2], i);
                createImpl(z);
                if (socketAddress != null) {
                    bind(socketAddress);
                }
                connect(inetSocketAddress);
                return;
            } catch (IOException | IllegalArgumentException | SecurityException e) {
                try {
                    this.impl.close();
                    this.closed = true;
                } catch (IOException e2) {
                    e.addSuppressed(e2);
                }
                if (i2 != inetAddressArr.length - 1) {
                    this.impl = null;
                    this.created = false;
                    this.bound = false;
                    this.closed = false;
                    i2++;
                } else {
                    throw e;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void createImpl(boolean z) throws SocketException {
        if (this.impl == null) {
            setImpl();
        }
        try {
            this.impl.create(z);
            this.created = true;
        } catch (IOException e) {
            throw new SocketException(e.getMessage());
        }
    }

    private void checkOldImpl() {
        if (this.impl != null) {
            this.oldImpl = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                public Boolean run() {
                    Class cls = Socket.this.impl.getClass();
                    do {
                        try {
                            cls.getDeclaredMethod(SecurityConstants.SOCKET_CONNECT_ACTION, SocketAddress.class, Integer.TYPE);
                            return Boolean.FALSE;
                        } catch (NoSuchMethodException unused) {
                            cls = cls.getSuperclass();
                            if (cls.equals(SocketImpl.class)) {
                                return Boolean.TRUE;
                            }
                        }
                    } while (cls.equals(SocketImpl.class));
                    return Boolean.TRUE;
                }
            })).booleanValue();
        }
    }

    /* access modifiers changed from: package-private */
    public void setImpl() {
        SocketImplFactory socketImplFactory = factory;
        if (socketImplFactory != null) {
            this.impl = socketImplFactory.createSocketImpl();
            checkOldImpl();
        } else {
            this.impl = new SocksSocketImpl();
        }
        SocketImpl socketImpl = this.impl;
        if (socketImpl != null) {
            socketImpl.setSocket(this);
        }
    }

    /* access modifiers changed from: package-private */
    public SocketImpl getImpl() throws SocketException {
        if (!this.created) {
            createImpl(true);
        }
        return this.impl;
    }

    public void connect(SocketAddress socketAddress) throws IOException {
        connect(socketAddress, 0);
    }

    public void connect(SocketAddress socketAddress, int i) throws IOException {
        if (socketAddress == null) {
            throw new IllegalArgumentException("connect: The address can't be null");
        } else if (i < 0) {
            throw new IllegalArgumentException("connect: timeout can't be negative");
        } else if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!this.oldImpl && isConnected()) {
            throw new SocketException("already connected");
        } else if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            InetAddress address = inetSocketAddress.getAddress();
            int port = inetSocketAddress.getPort();
            checkAddress(address, SecurityConstants.SOCKET_CONNECT_ACTION);
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                if (inetSocketAddress.isUnresolved()) {
                    securityManager.checkConnect(inetSocketAddress.getHostName(), port);
                } else {
                    securityManager.checkConnect(address.getHostAddress(), port);
                }
            }
            if (!this.created) {
                createImpl(true);
            }
            if (!this.oldImpl) {
                this.impl.connect((SocketAddress) inetSocketAddress, i);
            } else if (i != 0) {
                throw new UnsupportedOperationException("SocketImpl.connect(addr, timeout)");
            } else if (inetSocketAddress.isUnresolved()) {
                this.impl.connect(address.getHostName(), port);
            } else {
                this.impl.connect(address, port);
            }
            this.connected = true;
            this.bound = true;
        } else {
            throw new IllegalArgumentException("Unsupported address type");
        }
    }

    public void bind(SocketAddress socketAddress) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!this.oldImpl && isBound()) {
            throw new SocketException("Already bound");
        } else if (socketAddress == null || (socketAddress instanceof InetSocketAddress)) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            if (inetSocketAddress == null || !inetSocketAddress.isUnresolved()) {
                if (inetSocketAddress == null) {
                    inetSocketAddress = new InetSocketAddress(0);
                }
                InetAddress address = inetSocketAddress.getAddress();
                int port = inetSocketAddress.getPort();
                checkAddress(address, "bind");
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkListen(port);
                }
                getImpl().bind(address, port);
                this.bound = true;
                return;
            }
            throw new SocketException("Unresolved address");
        } else {
            throw new IllegalArgumentException("Unsupported address type");
        }
    }

    private void checkAddress(InetAddress inetAddress, String str) {
        if (inetAddress != null && !(inetAddress instanceof Inet4Address) && !(inetAddress instanceof Inet6Address)) {
            throw new IllegalArgumentException(str + ": invalid address type");
        }
    }

    /* access modifiers changed from: package-private */
    public final void postAccept() {
        this.connected = true;
        this.created = true;
        this.bound = true;
    }

    /* access modifiers changed from: package-private */
    public void setCreated() {
        this.created = true;
    }

    /* access modifiers changed from: package-private */
    public void setBound() {
        this.bound = true;
    }

    /* access modifiers changed from: package-private */
    public void setConnected() {
        this.connected = true;
    }

    public InetAddress getInetAddress() {
        if (!isConnected()) {
            return null;
        }
        try {
            return getImpl().getInetAddress();
        } catch (SocketException unused) {
            return null;
        }
    }

    public InetAddress getLocalAddress() {
        if (!isBound()) {
            return InetAddress.anyLocalAddress();
        }
        try {
            InetAddress inetAddress = (InetAddress) getImpl().getOption(15);
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkConnect(inetAddress.getHostAddress(), -1);
            }
            if (inetAddress.isAnyLocalAddress()) {
                return InetAddress.anyLocalAddress();
            }
            return inetAddress;
        } catch (SecurityException unused) {
            return InetAddress.getLoopbackAddress();
        } catch (Exception unused2) {
            return InetAddress.anyLocalAddress();
        }
    }

    public int getPort() {
        if (!isConnected()) {
            return 0;
        }
        try {
            return getImpl().getPort();
        } catch (SocketException unused) {
            return -1;
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

    public SocketAddress getRemoteSocketAddress() {
        if (!isConnected()) {
            return null;
        }
        return new InetSocketAddress(getInetAddress(), getPort());
    }

    public SocketAddress getLocalSocketAddress() {
        if (!isBound()) {
            return null;
        }
        return new InetSocketAddress(getLocalAddress(), getLocalPort());
    }

    public InputStream getInputStream() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        } else if (!isInputShutdown()) {
            try {
                return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
                    public InputStream run() throws IOException {
                        return Socket.this.impl.getInputStream();
                    }
                });
            } catch (PrivilegedActionException e) {
                throw ((IOException) e.getException());
            }
        } else {
            throw new SocketException("Socket input is shutdown");
        }
    }

    public OutputStream getOutputStream() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        } else if (!isOutputShutdown()) {
            try {
                return (OutputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<OutputStream>() {
                    public OutputStream run() throws IOException {
                        return Socket.this.impl.getOutputStream();
                    }
                });
            } catch (PrivilegedActionException e) {
                throw ((IOException) e.getException());
            }
        } else {
            throw new SocketException("Socket output is shutdown");
        }
    }

    public void setTcpNoDelay(boolean z) throws SocketException {
        if (!isClosed()) {
            getImpl().setOption(1, Boolean.valueOf(z));
            return;
        }
        throw new SocketException("Socket is closed");
    }

    public boolean getTcpNoDelay() throws SocketException {
        if (!isClosed()) {
            return ((Boolean) getImpl().getOption(1)).booleanValue();
        }
        throw new SocketException("Socket is closed");
    }

    public void setSoLinger(boolean z, int i) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!z) {
            getImpl().setOption(128, Boolean.valueOf(z));
        } else if (i >= 0) {
            if (i > 65535) {
                i = 65535;
            }
            getImpl().setOption(128, Integer.valueOf(i));
        } else {
            throw new IllegalArgumentException("invalid value for SO_LINGER");
        }
    }

    public int getSoLinger() throws SocketException {
        if (!isClosed()) {
            Object option = getImpl().getOption(128);
            if (option instanceof Integer) {
                return ((Integer) option).intValue();
            }
            return -1;
        }
        throw new SocketException("Socket is closed");
    }

    public void sendUrgentData(int i) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (getImpl().supportsUrgentData()) {
            getImpl().sendUrgentData(i);
        } else {
            throw new SocketException("Urgent data not supported");
        }
    }

    public void setOOBInline(boolean z) throws SocketException {
        if (!isClosed()) {
            getImpl().setOption(4099, Boolean.valueOf(z));
            return;
        }
        throw new SocketException("Socket is closed");
    }

    public boolean getOOBInline() throws SocketException {
        if (!isClosed()) {
            return ((Boolean) getImpl().getOption(4099)).booleanValue();
        }
        throw new SocketException("Socket is closed");
    }

    public synchronized void setSoTimeout(int i) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (i >= 0) {
            getImpl().setOption(4102, Integer.valueOf(i));
        } else {
            throw new IllegalArgumentException("timeout can't be negative");
        }
    }

    public synchronized int getSoTimeout() throws SocketException {
        if (!isClosed()) {
            Object option = getImpl().getOption(4102);
            if (!(option instanceof Integer)) {
                return 0;
            }
            return ((Integer) option).intValue();
        }
        throw new SocketException("Socket is closed");
    }

    public synchronized void setSendBufferSize(int i) throws SocketException {
        if (i > 0) {
            try {
                if (!isClosed()) {
                    getImpl().setOption(4097, Integer.valueOf(i));
                } else {
                    throw new SocketException("Socket is closed");
                }
            } catch (Throwable th) {
                throw th;
            }
        } else {
            throw new IllegalArgumentException("negative send size");
        }
    }

    public synchronized int getSendBufferSize() throws SocketException {
        Object option;
        if (!isClosed()) {
            option = getImpl().getOption(4097);
        } else {
            throw new SocketException("Socket is closed");
        }
        return option instanceof Integer ? ((Integer) option).intValue() : 0;
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
            throw new IllegalArgumentException("invalid receive size");
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

    public void setKeepAlive(boolean z) throws SocketException {
        if (!isClosed()) {
            getImpl().setOption(8, Boolean.valueOf(z));
            return;
        }
        throw new SocketException("Socket is closed");
    }

    public boolean getKeepAlive() throws SocketException {
        if (!isClosed()) {
            return ((Boolean) getImpl().getOption(8)).booleanValue();
        }
        throw new SocketException("Socket is closed");
    }

    public void setTrafficClass(int i) throws SocketException {
        if (i < 0 || i > 255) {
            throw new IllegalArgumentException("tc is not in range 0 -- 255");
        } else if (!isClosed()) {
            try {
                getImpl().setOption(3, Integer.valueOf(i));
            } catch (SocketException e) {
                if (!isConnected()) {
                    throw e;
                }
            }
        } else {
            throw new SocketException("Socket is closed");
        }
    }

    public int getTrafficClass() throws SocketException {
        if (!isClosed()) {
            return ((Integer) getImpl().getOption(3)).intValue();
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

    public synchronized void close() throws IOException {
        synchronized (this.closeLock) {
            if (!isClosed()) {
                if (this.created) {
                    this.impl.close();
                }
                this.closed = true;
            }
        }
    }

    public void shutdownInput() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        } else if (!isInputShutdown()) {
            getImpl().shutdownInput();
            this.shutIn = true;
        } else {
            throw new SocketException("Socket input is already shutdown");
        }
    }

    public void shutdownOutput() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        } else if (!isOutputShutdown()) {
            getImpl().shutdownOutput();
            this.shutOut = true;
        } else {
            throw new SocketException("Socket output is already shutdown");
        }
    }

    public String toString() {
        try {
            if (!isConnected()) {
                return "Socket[unconnected]";
            }
            return "Socket[address=" + getImpl().getInetAddress() + ",port=" + getImpl().getPort() + ",localPort=" + getImpl().getLocalPort() + NavigationBarInflaterView.SIZE_MOD_END;
        } catch (SocketException unused) {
            return "Socket[unconnected]";
        }
    }

    public boolean isConnected() {
        return this.connected || this.oldImpl;
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

    public boolean isInputShutdown() {
        return this.shutIn;
    }

    public boolean isOutputShutdown() {
        return this.shutOut;
    }

    public static synchronized void setSocketImplFactory(SocketImplFactory socketImplFactory) throws IOException {
        synchronized (Socket.class) {
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

    public <T> Socket setOption(SocketOption<T> socketOption, T t) throws IOException {
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
            java.lang.Class<java.net.Socket> r0 = java.net.Socket.class
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
        throw new UnsupportedOperationException("Method not decompiled: java.net.Socket.supportedOptions():java.util.Set");
    }

    public FileDescriptor getFileDescriptor$() {
        return this.impl.getFileDescriptor();
    }
}
