package java.net;

import java.nio.channels.DatagramChannel;
import java.p026io.Closeable;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Set;
import sun.security.util.SecurityConstants;

public class DatagramSocket implements Closeable {
    static final int ST_CONNECTED = 1;
    static final int ST_CONNECTED_NO_IMPL = 2;
    static final int ST_NOT_CONNECTED = 0;
    static DatagramSocketImplFactory factory = null;
    static Class<?> implClass = null;
    private static Set<SocketOption<?>> options = null;
    private static boolean optionsSet = false;
    private boolean bound;
    private int bytesLeftToFilter;
    private Object closeLock;
    private boolean closed;
    int connectState;
    InetAddress connectedAddress;
    int connectedPort;
    private boolean created;
    private boolean explicitFilter;
    DatagramSocketImpl impl;
    boolean oldImpl;
    private SocketException pendingConnectException;

    public DatagramChannel getChannel() {
        return null;
    }

    private synchronized void connectInternal(InetAddress inetAddress, int i) throws SocketException {
        if (i < 0 || i > 65535) {
            throw new IllegalArgumentException("connect: " + i);
        } else if (inetAddress != null) {
            checkAddress(inetAddress, SecurityConstants.SOCKET_CONNECT_ACTION);
            if (!isClosed()) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    if (inetAddress.isMulticastAddress()) {
                        securityManager.checkMulticast(inetAddress);
                    } else {
                        securityManager.checkConnect(inetAddress.getHostAddress(), i);
                        securityManager.checkAccept(inetAddress.getHostAddress(), i);
                    }
                }
                boolean z = false;
                if (!isBound()) {
                    bind(new InetSocketAddress(0));
                }
                try {
                    if (!this.oldImpl) {
                        DatagramSocketImpl datagramSocketImpl = this.impl;
                        if (!(datagramSocketImpl instanceof AbstractPlainDatagramSocketImpl) || !((AbstractPlainDatagramSocketImpl) datagramSocketImpl).nativeConnectDisabled()) {
                            getImpl().connect(inetAddress, i);
                            this.connectState = 1;
                            int dataAvailable = getImpl().dataAvailable();
                            if (dataAvailable != -1) {
                                if (dataAvailable > 0) {
                                    z = true;
                                }
                                this.explicitFilter = z;
                                if (z) {
                                    this.bytesLeftToFilter = getReceiveBufferSize();
                                }
                                this.connectedAddress = inetAddress;
                                this.connectedPort = i;
                            }
                            throw new SocketException();
                        }
                    }
                    this.connectState = 2;
                    this.connectedAddress = inetAddress;
                    this.connectedPort = i;
                } catch (SocketException e) {
                    this.connectState = 2;
                    throw e;
                } catch (Throwable th) {
                    this.connectedAddress = inetAddress;
                    this.connectedPort = i;
                    throw th;
                }
            }
        } else {
            throw new IllegalArgumentException("connect: null address");
        }
    }

    public DatagramSocket() throws SocketException {
        this((SocketAddress) new InetSocketAddress(0));
    }

    protected DatagramSocket(DatagramSocketImpl datagramSocketImpl) {
        this.created = false;
        this.bound = false;
        this.closed = false;
        this.closeLock = new Object();
        this.oldImpl = false;
        this.explicitFilter = false;
        this.connectState = 0;
        this.connectedAddress = null;
        this.connectedPort = -1;
        datagramSocketImpl.getClass();
        this.impl = datagramSocketImpl;
        checkOldImpl();
    }

    public DatagramSocket(SocketAddress socketAddress) throws SocketException {
        this.created = false;
        this.bound = false;
        this.closed = false;
        this.closeLock = new Object();
        this.oldImpl = false;
        this.explicitFilter = false;
        this.connectState = 0;
        this.connectedAddress = null;
        this.connectedPort = -1;
        createImpl();
        if (socketAddress != null) {
            try {
                bind(socketAddress);
            } finally {
                if (!isBound()) {
                    close();
                }
            }
        }
    }

    public DatagramSocket(int i) throws SocketException {
        this(i, (InetAddress) null);
    }

    public DatagramSocket(int i, InetAddress inetAddress) throws SocketException {
        this((SocketAddress) new InetSocketAddress(inetAddress, i));
    }

    private void checkOldImpl() {
        if (this.impl != null) {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                    public Void run() throws NoSuchMethodException {
                        DatagramSocket.this.impl.getClass().getDeclaredMethod("peekData", DatagramPacket.class);
                        return null;
                    }
                });
            } catch (PrivilegedActionException unused) {
                this.oldImpl = true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void createImpl() throws SocketException {
        if (this.impl == null) {
            DatagramSocketImplFactory datagramSocketImplFactory = factory;
            if (datagramSocketImplFactory != null) {
                this.impl = datagramSocketImplFactory.createDatagramSocketImpl();
                checkOldImpl();
            } else {
                this.impl = DefaultDatagramSocketImplFactory.createDatagramSocketImpl(this instanceof MulticastSocket);
                checkOldImpl();
            }
        }
        this.impl.create();
        this.impl.setDatagramSocket(this);
        this.created = true;
    }

    /* access modifiers changed from: package-private */
    public DatagramSocketImpl getImpl() throws SocketException {
        if (!this.created) {
            createImpl();
        }
        return this.impl;
    }

    public synchronized void bind(SocketAddress socketAddress) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (!isBound()) {
            if (socketAddress == null) {
                socketAddress = new InetSocketAddress(0);
            }
            if (socketAddress instanceof InetSocketAddress) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
                if (!inetSocketAddress.isUnresolved()) {
                    InetAddress address = inetSocketAddress.getAddress();
                    int port = inetSocketAddress.getPort();
                    checkAddress(address, "bind");
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkListen(port);
                    }
                    try {
                        getImpl().bind(port, address);
                        this.bound = true;
                    } catch (SocketException e) {
                        getImpl().close();
                        throw e;
                    }
                } else {
                    throw new SocketException("Unresolved address");
                }
            } else {
                throw new IllegalArgumentException("Unsupported address type!");
            }
        } else {
            throw new SocketException("already bound");
        }
    }

    /* access modifiers changed from: package-private */
    public void checkAddress(InetAddress inetAddress, String str) {
        if (inetAddress != null && !(inetAddress instanceof Inet4Address) && !(inetAddress instanceof Inet6Address)) {
            throw new IllegalArgumentException(str + ": invalid address type");
        }
    }

    public void connect(InetAddress inetAddress, int i) {
        try {
            connectInternal(inetAddress, i);
        } catch (SocketException e) {
            this.pendingConnectException = e;
        }
    }

    public void connect(SocketAddress socketAddress) throws SocketException {
        if (socketAddress == null) {
            throw new IllegalArgumentException("Address can't be null");
        } else if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            if (!inetSocketAddress.isUnresolved()) {
                connectInternal(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                return;
            }
            throw new SocketException("Unresolved address");
        } else {
            throw new IllegalArgumentException("Unsupported address type");
        }
    }

    public void disconnect() {
        synchronized (this) {
            if (!isClosed()) {
                if (this.connectState == 1) {
                    this.impl.disconnect();
                }
                this.connectedAddress = null;
                this.connectedPort = -1;
                this.connectState = 0;
                this.explicitFilter = false;
            }
        }
    }

    public boolean isBound() {
        return this.bound;
    }

    public boolean isConnected() {
        return this.connectState != 0;
    }

    public InetAddress getInetAddress() {
        return this.connectedAddress;
    }

    public int getPort() {
        return this.connectedPort;
    }

    public SocketAddress getRemoteSocketAddress() {
        if (!isConnected()) {
            return null;
        }
        return new InetSocketAddress(getInetAddress(), getPort());
    }

    public SocketAddress getLocalSocketAddress() {
        if (!isClosed() && isBound()) {
            return new InetSocketAddress(getLocalAddress(), getLocalPort());
        }
        return null;
    }

    public void send(DatagramPacket datagramPacket) throws IOException {
        synchronized (datagramPacket) {
            SocketException socketException = this.pendingConnectException;
            if (socketException != null) {
                throw new SocketException("Pending connect failure", socketException);
            } else if (!isClosed()) {
                checkAddress(datagramPacket.getAddress(), "send");
                if (this.connectState == 0) {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        if (datagramPacket.getAddress().isMulticastAddress()) {
                            securityManager.checkMulticast(datagramPacket.getAddress());
                        } else {
                            securityManager.checkConnect(datagramPacket.getAddress().getHostAddress(), datagramPacket.getPort());
                        }
                    }
                } else {
                    InetAddress address = datagramPacket.getAddress();
                    if (address == null) {
                        datagramPacket.setAddress(this.connectedAddress);
                        datagramPacket.setPort(this.connectedPort);
                    } else if (!address.equals(this.connectedAddress) || datagramPacket.getPort() != this.connectedPort) {
                        throw new IllegalArgumentException("connected address and packet address differ");
                    }
                }
                if (!isBound()) {
                    bind(new InetSocketAddress(0));
                }
                getImpl().send(datagramPacket);
            } else {
                throw new SocketException("Socket is closed");
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:13|(1:15)(1:16)|17|18|19|20) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0051 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void receive(java.net.DatagramPacket r7) throws java.p026io.IOException {
        /*
            r6 = this;
            monitor-enter(r6)
            monitor-enter(r7)     // Catch:{ all -> 0x00d9 }
            boolean r0 = r6.isBound()     // Catch:{ all -> 0x00d6 }
            r1 = 0
            if (r0 != 0) goto L_0x0011
            java.net.InetSocketAddress r0 = new java.net.InetSocketAddress     // Catch:{ all -> 0x00d6 }
            r0.<init>(r1)     // Catch:{ all -> 0x00d6 }
            r6.bind(r0)     // Catch:{ all -> 0x00d6 }
        L_0x0011:
            java.net.SocketException r0 = r6.pendingConnectException     // Catch:{ all -> 0x00d6 }
            if (r0 != 0) goto L_0x00ce
            int r0 = r6.connectState     // Catch:{ all -> 0x00d6 }
            r2 = 1
            if (r0 != 0) goto L_0x0060
            java.lang.SecurityManager r0 = java.lang.System.getSecurityManager()     // Catch:{ all -> 0x00d6 }
            if (r0 == 0) goto L_0x0060
        L_0x0020:
            boolean r3 = r6.oldImpl     // Catch:{ all -> 0x00d6 }
            if (r3 != 0) goto L_0x003c
            java.net.DatagramPacket r3 = new java.net.DatagramPacket     // Catch:{ all -> 0x00d6 }
            byte[] r4 = new byte[r2]     // Catch:{ all -> 0x00d6 }
            r3.<init>(r4, r2)     // Catch:{ all -> 0x00d6 }
            java.net.DatagramSocketImpl r4 = r6.getImpl()     // Catch:{ all -> 0x00d6 }
            int r4 = r4.peekData(r3)     // Catch:{ all -> 0x00d6 }
            java.net.InetAddress r3 = r3.getAddress()     // Catch:{ all -> 0x00d6 }
            java.lang.String r3 = r3.getHostAddress()     // Catch:{ all -> 0x00d6 }
            goto L_0x004d
        L_0x003c:
            java.net.InetAddress r3 = new java.net.InetAddress     // Catch:{ all -> 0x00d6 }
            r3.<init>()     // Catch:{ all -> 0x00d6 }
            java.net.DatagramSocketImpl r4 = r6.getImpl()     // Catch:{ all -> 0x00d6 }
            int r4 = r4.peek(r3)     // Catch:{ all -> 0x00d6 }
            java.lang.String r3 = r3.getHostAddress()     // Catch:{ all -> 0x00d6 }
        L_0x004d:
            r0.checkAccept(r3, r4)     // Catch:{ SecurityException -> 0x0051 }
            goto L_0x0060
        L_0x0051:
            java.net.DatagramPacket r3 = new java.net.DatagramPacket     // Catch:{ all -> 0x00d6 }
            byte[] r4 = new byte[r2]     // Catch:{ all -> 0x00d6 }
            r3.<init>(r4, r2)     // Catch:{ all -> 0x00d6 }
            java.net.DatagramSocketImpl r4 = r6.getImpl()     // Catch:{ all -> 0x00d6 }
            r4.receive(r3)     // Catch:{ all -> 0x00d6 }
            goto L_0x0020
        L_0x0060:
            int r0 = r6.connectState     // Catch:{ all -> 0x00d6 }
            r3 = 2
            r4 = 0
            if (r0 == r3) goto L_0x006a
            boolean r0 = r6.explicitFilter     // Catch:{ all -> 0x00d6 }
            if (r0 == 0) goto L_0x00bb
        L_0x006a:
            if (r1 != 0) goto L_0x00bb
            boolean r0 = r6.oldImpl     // Catch:{ all -> 0x00d6 }
            if (r0 != 0) goto L_0x0084
            java.net.DatagramPacket r0 = new java.net.DatagramPacket     // Catch:{ all -> 0x00d6 }
            byte[] r3 = new byte[r2]     // Catch:{ all -> 0x00d6 }
            r0.<init>(r3, r2)     // Catch:{ all -> 0x00d6 }
            java.net.DatagramSocketImpl r3 = r6.getImpl()     // Catch:{ all -> 0x00d6 }
            int r3 = r3.peekData(r0)     // Catch:{ all -> 0x00d6 }
            java.net.InetAddress r0 = r0.getAddress()     // Catch:{ all -> 0x00d6 }
            goto L_0x0091
        L_0x0084:
            java.net.InetAddress r0 = new java.net.InetAddress     // Catch:{ all -> 0x00d6 }
            r0.<init>()     // Catch:{ all -> 0x00d6 }
            java.net.DatagramSocketImpl r3 = r6.getImpl()     // Catch:{ all -> 0x00d6 }
            int r3 = r3.peek(r0)     // Catch:{ all -> 0x00d6 }
        L_0x0091:
            java.net.InetAddress r5 = r6.connectedAddress     // Catch:{ all -> 0x00d6 }
            boolean r0 = r5.equals(r0)     // Catch:{ all -> 0x00d6 }
            if (r0 == 0) goto L_0x00a0
            int r0 = r6.connectedPort     // Catch:{ all -> 0x00d6 }
            if (r0 == r3) goto L_0x009e
            goto L_0x00a0
        L_0x009e:
            r1 = r2
            goto L_0x006a
        L_0x00a0:
            java.net.DatagramPacket r4 = new java.net.DatagramPacket     // Catch:{ all -> 0x00d6 }
            r0 = 1024(0x400, float:1.435E-42)
            byte[] r3 = new byte[r0]     // Catch:{ all -> 0x00d6 }
            r4.<init>(r3, r0)     // Catch:{ all -> 0x00d6 }
            java.net.DatagramSocketImpl r0 = r6.getImpl()     // Catch:{ all -> 0x00d6 }
            r0.receive(r4)     // Catch:{ all -> 0x00d6 }
            boolean r0 = r6.explicitFilter     // Catch:{ all -> 0x00d6 }
            if (r0 == 0) goto L_0x006a
            boolean r0 = r6.checkFiltering(r4)     // Catch:{ all -> 0x00d6 }
            if (r0 == 0) goto L_0x006a
            goto L_0x009e
        L_0x00bb:
            java.net.DatagramSocketImpl r0 = r6.getImpl()     // Catch:{ all -> 0x00d6 }
            r0.receive(r7)     // Catch:{ all -> 0x00d6 }
            boolean r0 = r6.explicitFilter     // Catch:{ all -> 0x00d6 }
            if (r0 == 0) goto L_0x00cb
            if (r4 != 0) goto L_0x00cb
            r6.checkFiltering(r7)     // Catch:{ all -> 0x00d6 }
        L_0x00cb:
            monitor-exit(r7)     // Catch:{ all -> 0x00d6 }
            monitor-exit(r6)
            return
        L_0x00ce:
            java.net.SocketException r1 = new java.net.SocketException     // Catch:{ all -> 0x00d6 }
            java.lang.String r2 = "Pending connect failure"
            r1.<init>(r2, r0)     // Catch:{ all -> 0x00d6 }
            throw r1     // Catch:{ all -> 0x00d6 }
        L_0x00d6:
            r0 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x00d6 }
            throw r0     // Catch:{ all -> 0x00d9 }
        L_0x00d9:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.DatagramSocket.receive(java.net.DatagramPacket):void");
    }

    private boolean checkFiltering(DatagramPacket datagramPacket) throws SocketException {
        int length = this.bytesLeftToFilter - datagramPacket.getLength();
        this.bytesLeftToFilter = length;
        if (length > 0 && getImpl().dataAvailable() > 0) {
            return false;
        }
        this.explicitFilter = false;
        return true;
    }

    public InetAddress getLocalAddress() {
        if (isClosed()) {
            return null;
        }
        try {
            InetAddress inetAddress = (InetAddress) getImpl().getOption(15);
            if (inetAddress.isAnyLocalAddress()) {
                inetAddress = InetAddress.anyLocalAddress();
            }
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager == null) {
                return inetAddress;
            }
            securityManager.checkConnect(inetAddress.getHostAddress(), -1);
            return inetAddress;
        } catch (Exception unused) {
            return InetAddress.anyLocalAddress();
        }
    }

    public int getLocalPort() {
        if (isClosed()) {
            return -1;
        }
        try {
            return getImpl().getLocalPort();
        } catch (Exception unused) {
            return 0;
        }
    }

    public synchronized void setSoTimeout(int i) throws SocketException {
        if (!isClosed()) {
            getImpl().setOption(4102, Integer.valueOf(i));
        } else {
            throw new SocketException("Socket is closed");
        }
    }

    public synchronized int getSoTimeout() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (getImpl() == null) {
            return 0;
        } else {
            Object option = getImpl().getOption(4102);
            if (!(option instanceof Integer)) {
                return 0;
            }
            return ((Integer) option).intValue();
        }
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

    public synchronized void setReuseAddress(boolean z) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (this.oldImpl) {
            getImpl().setOption(4, Integer.valueOf(z ? -1 : 0));
        } else {
            getImpl().setOption(4, Boolean.valueOf(z));
        }
    }

    public synchronized boolean getReuseAddress() throws SocketException {
        if (!isClosed()) {
        } else {
            throw new SocketException("Socket is closed");
        }
        return ((Boolean) getImpl().getOption(4)).booleanValue();
    }

    public synchronized void setBroadcast(boolean z) throws SocketException {
        if (!isClosed()) {
            getImpl().setOption(32, Boolean.valueOf(z));
        } else {
            throw new SocketException("Socket is closed");
        }
    }

    public synchronized boolean getBroadcast() throws SocketException {
        if (!isClosed()) {
        } else {
            throw new SocketException("Socket is closed");
        }
        return ((Boolean) getImpl().getOption(32)).booleanValue();
    }

    public synchronized void setTrafficClass(int i) throws SocketException {
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

    public synchronized int getTrafficClass() throws SocketException {
        if (!isClosed()) {
        } else {
            throw new SocketException("Socket is closed");
        }
        return ((Integer) getImpl().getOption(3)).intValue();
    }

    public void close() {
        synchronized (this.closeLock) {
            if (!isClosed()) {
                this.impl.close();
                this.closed = true;
            }
        }
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this.closeLock) {
            z = this.closed;
        }
        return z;
    }

    public static synchronized void setDatagramSocketImplFactory(DatagramSocketImplFactory datagramSocketImplFactory) throws IOException {
        synchronized (DatagramSocket.class) {
            if (factory == null) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkSetFactory();
                }
                factory = datagramSocketImplFactory;
            } else {
                throw new SocketException("factory already defined");
            }
        }
    }

    public <T> DatagramSocket setOption(SocketOption<T> socketOption, T t) throws IOException {
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
            java.lang.Class<java.net.DatagramSocket> r0 = java.net.DatagramSocket.class
            monitor-enter(r0)
            boolean r1 = optionsSet     // Catch:{ all -> 0x0027 }
            if (r1 == 0) goto L_0x000b
            java.util.Set<java.net.SocketOption<?>> r2 = options     // Catch:{ all -> 0x0027 }
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            return r2
        L_0x000b:
            java.net.DatagramSocketImpl r2 = r2.getImpl()     // Catch:{ IOException -> 0x001a }
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
        throw new UnsupportedOperationException("Method not decompiled: java.net.DatagramSocket.supportedOptions():java.util.Set");
    }

    public FileDescriptor getFileDescriptor$() {
        return this.impl.f554fd;
    }
}
