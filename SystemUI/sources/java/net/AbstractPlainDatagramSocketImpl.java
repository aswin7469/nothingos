package java.net;

import dalvik.system.BlockGuard;
import dalvik.system.CloseGuard;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.security.AccessController;
import java.util.Enumeration;
import libcore.p030io.IoBridge;
import libcore.p030io.IoUtils;
import sun.net.ResourceManager;
import sun.security.action.GetPropertyAction;

abstract class AbstractPlainDatagramSocketImpl extends DatagramSocketImpl {
    private static final boolean connectDisabled;

    /* renamed from: os */
    private static final String f555os;
    boolean connected = false;
    protected InetAddress connectedAddress = null;
    private int connectedPort = -1;
    private final CloseGuard guard = CloseGuard.get();
    int timeout = 0;
    private int trafficClass = 0;

    /* access modifiers changed from: protected */
    public abstract void bind0(int i, InetAddress inetAddress) throws SocketException;

    /* access modifiers changed from: protected */
    public abstract void connect0(InetAddress inetAddress, int i) throws SocketException;

    /* access modifiers changed from: protected */
    public abstract void datagramSocketClose();

    /* access modifiers changed from: protected */
    public abstract void datagramSocketCreate() throws SocketException;

    /* access modifiers changed from: protected */
    public abstract void disconnect0(int i);

    /* access modifiers changed from: protected */
    @Deprecated
    public abstract byte getTTL() throws IOException;

    /* access modifiers changed from: protected */
    public abstract int getTimeToLive() throws IOException;

    /* access modifiers changed from: protected */
    public abstract void join(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void leave(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException;

    /* access modifiers changed from: protected */
    public abstract int peek(InetAddress inetAddress) throws IOException;

    /* access modifiers changed from: protected */
    public abstract int peekData(DatagramPacket datagramPacket) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void receive0(DatagramPacket datagramPacket) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void send(DatagramPacket datagramPacket) throws IOException;

    /* access modifiers changed from: protected */
    @Deprecated
    public abstract void setTTL(byte b) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void setTimeToLive(int i) throws IOException;

    /* access modifiers changed from: protected */
    public abstract Object socketGetOption(int i) throws SocketException;

    /* access modifiers changed from: protected */
    public abstract void socketSetOption(int i, Object obj) throws SocketException;

    AbstractPlainDatagramSocketImpl() {
    }

    static {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("os.name"));
        f555os = str;
        connectDisabled = str.contains("OS X");
    }

    /* access modifiers changed from: protected */
    public synchronized void create() throws SocketException {
        ResourceManager.beforeUdpCreate();
        this.f556fd = new FileDescriptor();
        try {
            datagramSocketCreate();
            if (this.f556fd != null && this.f556fd.valid()) {
                this.guard.open("close");
                IoUtils.setFdOwner(this.f556fd, this);
            }
        } catch (SocketException e) {
            ResourceManager.afterUdpClose();
            this.f556fd = null;
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void bind(int i, InetAddress inetAddress) throws SocketException {
        bind0(i, inetAddress);
    }

    /* access modifiers changed from: protected */
    public void connect(InetAddress inetAddress, int i) throws SocketException {
        BlockGuard.getThreadPolicy().onNetwork();
        connect0(inetAddress, i);
        this.connectedAddress = inetAddress;
        this.connectedPort = i;
        this.connected = true;
    }

    /* access modifiers changed from: protected */
    public void disconnect() {
        disconnect0(this.connectedAddress.holder().getFamily());
        this.connected = false;
        this.connectedAddress = null;
        this.connectedPort = -1;
    }

    /* access modifiers changed from: protected */
    public synchronized void receive(DatagramPacket datagramPacket) throws IOException {
        receive0(datagramPacket);
    }

    /* access modifiers changed from: protected */
    public void join(InetAddress inetAddress) throws IOException {
        join(inetAddress, (NetworkInterface) null);
    }

    /* access modifiers changed from: protected */
    public void leave(InetAddress inetAddress) throws IOException {
        leave(inetAddress, (NetworkInterface) null);
    }

    /* access modifiers changed from: protected */
    public void joinGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException {
        if (socketAddress == null || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        join(((InetSocketAddress) socketAddress).getAddress(), networkInterface);
    }

    /* access modifiers changed from: protected */
    public void leaveGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException {
        if (socketAddress == null || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        leave(((InetSocketAddress) socketAddress).getAddress(), networkInterface);
    }

    /* access modifiers changed from: protected */
    public void close() {
        this.guard.close();
        if (this.f556fd != null) {
            datagramSocketClose();
            ResourceManager.afterUdpClose();
            this.f556fd = null;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isClosed() {
        return this.f556fd == null;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        CloseGuard closeGuard = this.guard;
        if (closeGuard != null) {
            closeGuard.warnIfOpen();
        }
        close();
    }

    public void setOption(int i, Object obj) throws SocketException {
        if (!isClosed()) {
            if (i != 3) {
                if (i != 4) {
                    if (i == 15) {
                        throw new SocketException("Cannot re-bind Socket");
                    } else if (i != 16) {
                        if (i != 18) {
                            if (i != 4102) {
                                if (i != 31) {
                                    if (i != 32) {
                                        if (i != 4097 && i != 4098) {
                                            throw new SocketException("invalid option: " + i);
                                        } else if (obj == null || !(obj instanceof Integer) || ((Integer) obj).intValue() < 0) {
                                            throw new SocketException("bad argument for SO_SNDBUF or SO_RCVBUF");
                                        }
                                    } else if (obj == null || !(obj instanceof Boolean)) {
                                        throw new SocketException("bad argument for SO_BROADCAST");
                                    }
                                } else if (obj == null || (!(obj instanceof Integer) && !(obj instanceof NetworkInterface))) {
                                    throw new SocketException("bad argument for IP_MULTICAST_IF2");
                                } else if (obj instanceof NetworkInterface) {
                                    obj = new Integer(((NetworkInterface) obj).getIndex());
                                }
                            } else if (obj == null || !(obj instanceof Integer)) {
                                throw new SocketException("bad argument for SO_TIMEOUT");
                            } else {
                                int intValue = ((Integer) obj).intValue();
                                if (intValue >= 0) {
                                    this.timeout = intValue;
                                    return;
                                }
                                throw new IllegalArgumentException("timeout < 0");
                            }
                        } else if (obj == null || !(obj instanceof Boolean)) {
                            throw new SocketException("bad argument for IP_MULTICAST_LOOP");
                        }
                    } else if (obj == null || !(obj instanceof InetAddress)) {
                        throw new SocketException("bad argument for IP_MULTICAST_IF");
                    }
                } else if (obj == null || !(obj instanceof Boolean)) {
                    throw new SocketException("bad argument for SO_REUSEADDR");
                }
            } else if (obj == null || !(obj instanceof Integer)) {
                throw new SocketException("bad argument for IP_TOS");
            } else {
                this.trafficClass = ((Integer) obj).intValue();
            }
            socketSetOption(i, obj);
            return;
        }
        throw new SocketException("Socket Closed");
    }

    public Object getOption(int i) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket Closed");
        } else if (i != 3) {
            if (!(i == 4 || i == 15 || i == 16 || i == 18)) {
                if (i == 4102) {
                    return new Integer(this.timeout);
                }
                if (!(i == 31 || i == 32 || i == 4097 || i == 4098)) {
                    throw new SocketException("invalid option: " + i);
                }
            }
            Object socketGetOption = socketGetOption(i);
            return i == 16 ? getNIFirstAddress(((Integer) socketGetOption).intValue()) : socketGetOption;
        } else {
            Object socketGetOption2 = socketGetOption(i);
            return ((Integer) socketGetOption2).intValue() == -1 ? new Integer(this.trafficClass) : socketGetOption2;
        }
    }

    static InetAddress getNIFirstAddress(int i) throws SocketException {
        if (i > 0) {
            Enumeration<InetAddress> inetAddresses = NetworkInterface.getByIndex(i).getInetAddresses();
            if (inetAddresses.hasMoreElements()) {
                return inetAddresses.nextElement();
            }
        }
        return InetAddress.anyLocalAddress();
    }

    /* access modifiers changed from: protected */
    public boolean nativeConnectDisabled() {
        return connectDisabled;
    }

    /* access modifiers changed from: package-private */
    public int dataAvailable() {
        try {
            return IoBridge.available(this.f556fd);
        } catch (IOException unused) {
            return -1;
        }
    }
}
