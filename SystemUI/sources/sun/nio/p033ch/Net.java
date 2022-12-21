package sun.nio.p033ch;

import dalvik.system.BlockGuard;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.UnresolvedAddressException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import jdk.net.NetworkPermission;
import jdk.net.SocketFlow;
import sun.net.ExtendedOptionsImpl;

/* renamed from: sun.nio.ch.Net */
public class Net {
    public static final short POLLCONN = pollconnValue();
    public static final short POLLERR = pollerrValue();
    public static final short POLLHUP = pollhupValue();
    public static final short POLLIN = pollinValue();
    public static final short POLLNVAL = pollnvalValue();
    public static final short POLLOUT = polloutValue();
    public static final int SHUT_RD = 0;
    public static final int SHUT_RDWR = 2;
    public static final int SHUT_WR = 1;
    static final ProtocolFamily UNSPEC = new ProtocolFamily() {
        public String name() {
            return "UNSPEC";
        }
    };
    private static volatile boolean checkedIPv6 = false;
    private static final boolean exclusiveBind;
    private static final boolean fastLoopback = isFastTcpLoopbackRequested();
    private static volatile boolean isIPv6Available;

    private static native void bind0(FileDescriptor fileDescriptor, boolean z, boolean z2, InetAddress inetAddress, int i) throws IOException;

    private static native int blockOrUnblock4(boolean z, FileDescriptor fileDescriptor, int i, int i2, int i3) throws IOException;

    static native int blockOrUnblock6(boolean z, FileDescriptor fileDescriptor, byte[] bArr, int i, byte[] bArr2) throws IOException;

    private static native boolean canIPv6SocketJoinIPv4Group0();

    private static native boolean canJoin6WithIPv4Group0();

    private static native int connect0(boolean z, FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws IOException;

    private static native int getIntOption0(FileDescriptor fileDescriptor, boolean z, int i, int i2) throws IOException;

    static native int getInterface4(FileDescriptor fileDescriptor) throws IOException;

    static native int getInterface6(FileDescriptor fileDescriptor) throws IOException;

    private static native int isExclusiveBindAvailable();

    private static native boolean isIPv6Available0();

    private static native int joinOrDrop4(boolean z, FileDescriptor fileDescriptor, int i, int i2, int i3) throws IOException;

    private static native int joinOrDrop6(boolean z, FileDescriptor fileDescriptor, byte[] bArr, int i, byte[] bArr2) throws IOException;

    static native void listen(FileDescriptor fileDescriptor, int i) throws IOException;

    private static native InetAddress localInetAddress(FileDescriptor fileDescriptor) throws IOException;

    private static native int localPort(FileDescriptor fileDescriptor) throws IOException;

    static native int poll(FileDescriptor fileDescriptor, int i, long j) throws IOException;

    static native short pollconnValue();

    static native short pollerrValue();

    static native short pollhupValue();

    static native short pollinValue();

    static native short pollnvalValue();

    static native short polloutValue();

    private static native InetAddress remoteInetAddress(FileDescriptor fileDescriptor) throws IOException;

    private static native int remotePort(FileDescriptor fileDescriptor) throws IOException;

    private static native void setIntOption0(FileDescriptor fileDescriptor, boolean z, int i, int i2, int i3, boolean z2) throws IOException;

    static native void setInterface4(FileDescriptor fileDescriptor, int i) throws IOException;

    static native void setInterface6(FileDescriptor fileDescriptor, int i) throws IOException;

    static native void shutdown(FileDescriptor fileDescriptor, int i) throws IOException;

    private static native int socket0(boolean z, boolean z2, boolean z3, boolean z4);

    private Net() {
    }

    static {
        int isExclusiveBindAvailable = isExclusiveBindAvailable();
        if (isExclusiveBindAvailable >= 0) {
            String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    return System.getProperty("sun.net.useExclusiveBind");
                }
            });
            boolean z = true;
            if (str != null) {
                if (str.length() != 0) {
                    z = Boolean.parseBoolean(str);
                }
                exclusiveBind = z;
            } else if (isExclusiveBindAvailable == 1) {
                exclusiveBind = true;
            } else {
                exclusiveBind = false;
            }
        } else {
            exclusiveBind = false;
        }
    }

    static boolean isIPv6Available() {
        if (!checkedIPv6) {
            isIPv6Available = isIPv6Available0();
            checkedIPv6 = true;
        }
        return isIPv6Available;
    }

    static boolean useExclusiveBind() {
        return exclusiveBind;
    }

    static boolean canIPv6SocketJoinIPv4Group() {
        return canIPv6SocketJoinIPv4Group0();
    }

    static boolean canJoin6WithIPv4Group() {
        return canJoin6WithIPv4Group0();
    }

    public static InetSocketAddress checkAddress(SocketAddress socketAddress) {
        if (socketAddress == null) {
            throw new IllegalArgumentException("sa == null");
        } else if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            if (!inetSocketAddress.isUnresolved()) {
                InetAddress address = inetSocketAddress.getAddress();
                if ((address instanceof Inet4Address) || (address instanceof Inet6Address)) {
                    return inetSocketAddress;
                }
                throw new IllegalArgumentException("Invalid address type");
            }
            throw new UnresolvedAddressException();
        } else {
            throw new UnsupportedAddressTypeException();
        }
    }

    static InetSocketAddress asInetSocketAddress(SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            return (InetSocketAddress) socketAddress;
        }
        throw new UnsupportedAddressTypeException();
    }

    static void translateToSocketException(Exception exc) throws SocketException {
        Exception exc2;
        if (!(exc instanceof SocketException)) {
            if (exc instanceof ClosedChannelException) {
                exc2 = new SocketException("Socket is closed");
            } else if (exc instanceof NotYetConnectedException) {
                exc2 = new SocketException("Socket is not connected");
            } else if (exc instanceof AlreadyBoundException) {
                exc2 = new SocketException("Already bound");
            } else if (exc instanceof NotYetBoundException) {
                exc2 = new SocketException("Socket is not bound yet");
            } else if (exc instanceof UnsupportedAddressTypeException) {
                exc2 = new SocketException("Unsupported address type");
            } else if (exc instanceof UnresolvedAddressException) {
                exc2 = new SocketException("Unresolved address");
            } else {
                exc2 = exc instanceof AlreadyConnectedException ? new SocketException("Already connected") : exc;
            }
            if (exc2 != exc) {
                exc2.initCause(exc);
            }
            if (exc2 instanceof SocketException) {
                throw ((SocketException) exc2);
            } else if (exc2 instanceof RuntimeException) {
                throw ((RuntimeException) exc2);
            } else {
                throw new Error("Untranslated exception", exc2);
            }
        } else {
            throw ((SocketException) exc);
        }
    }

    static void translateException(Exception exc, boolean z) throws IOException {
        if (exc instanceof IOException) {
            throw ((IOException) exc);
        } else if (!z || !(exc instanceof UnresolvedAddressException)) {
            translateToSocketException(exc);
        } else {
            throw new UnknownHostException();
        }
    }

    static void translateException(Exception exc) throws IOException {
        translateException(exc, false);
    }

    static InetSocketAddress getRevealedLocalAddress(InetSocketAddress inetSocketAddress) {
        SecurityManager securityManager = System.getSecurityManager();
        if (inetSocketAddress == null || securityManager == null) {
            return inetSocketAddress;
        }
        try {
            securityManager.checkConnect(inetSocketAddress.getAddress().getHostAddress(), -1);
            return inetSocketAddress;
        } catch (SecurityException unused) {
            return getLoopbackAddress(inetSocketAddress.getPort());
        }
    }

    static String getRevealedLocalAddressAsString(InetSocketAddress inetSocketAddress) {
        if (System.getSecurityManager() == null) {
            return inetSocketAddress.toString();
        }
        return getLoopbackAddress(inetSocketAddress.getPort()).toString();
    }

    private static InetSocketAddress getLoopbackAddress(int i) {
        return new InetSocketAddress(InetAddress.getLoopbackAddress(), i);
    }

    static Inet4Address anyInet4Address(final NetworkInterface networkInterface) {
        return (Inet4Address) AccessController.doPrivileged(new PrivilegedAction<Inet4Address>() {
            public Inet4Address run() {
                Enumeration<InetAddress> inetAddresses = NetworkInterface.this.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress nextElement = inetAddresses.nextElement();
                    if (nextElement instanceof Inet4Address) {
                        return (Inet4Address) nextElement;
                    }
                }
                return null;
            }
        });
    }

    static int inet4AsInt(InetAddress inetAddress) {
        if (inetAddress instanceof Inet4Address) {
            byte[] address = inetAddress.getAddress();
            return ((address[0] << 24) & -16777216) | (address[3] & 255) | ((address[2] << 8) & 65280) | ((address[1] << 16) & 16711680);
        }
        throw new AssertionError((Object) "Should not reach here");
    }

    static InetAddress inet4FromInt(int i) {
        try {
            return InetAddress.getByAddress(new byte[]{(byte) ((i >>> 24) & 255), (byte) ((i >>> 16) & 255), (byte) ((i >>> 8) & 255), (byte) (i & 255)});
        } catch (UnknownHostException unused) {
            throw new AssertionError((Object) "Should not reach here");
        }
    }

    static byte[] inet6AsByteArray(InetAddress inetAddress) {
        if (inetAddress instanceof Inet6Address) {
            return inetAddress.getAddress();
        }
        if (inetAddress instanceof Inet4Address) {
            byte[] address = inetAddress.getAddress();
            byte[] bArr = new byte[16];
            bArr[10] = -1;
            bArr[11] = -1;
            bArr[12] = address[0];
            bArr[13] = address[1];
            bArr[14] = address[2];
            bArr[15] = address[3];
            return bArr;
        }
        throw new AssertionError((Object) "Should not reach here");
    }

    static void setSocketOption(FileDescriptor fileDescriptor, ProtocolFamily protocolFamily, SocketOption<?> socketOption, Object obj) throws IOException {
        int booleanValue;
        int intValue;
        int intValue2;
        if (obj != null) {
            Class<?> type = socketOption.type();
            if (type == SocketFlow.class) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkPermission(new NetworkPermission("setOption.SO_FLOW_SLA"));
                }
                ExtendedOptionsImpl.setFlowOption(fileDescriptor, (SocketFlow) obj);
            } else if (type != Integer.class && type != Boolean.class) {
                throw new AssertionError((Object) "Should not reach here");
            } else if ((socketOption == StandardSocketOptions.SO_RCVBUF || socketOption == StandardSocketOptions.SO_SNDBUF) && ((Integer) obj).intValue() < 0) {
                throw new IllegalArgumentException("Invalid send/receive buffer size");
            } else {
                if (socketOption == StandardSocketOptions.SO_LINGER) {
                    int intValue3 = ((Integer) obj).intValue();
                    if (intValue3 < 0) {
                        obj = -1;
                    }
                    if (intValue3 > 65535) {
                        obj = 65535;
                    }
                }
                if (socketOption == StandardSocketOptions.IP_TOS && ((intValue2 = ((Integer) obj).intValue()) < 0 || intValue2 > 255)) {
                    throw new IllegalArgumentException("Invalid IP_TOS value");
                } else if (socketOption != StandardSocketOptions.IP_MULTICAST_TTL || ((intValue = ((Integer) obj).intValue()) >= 0 && intValue <= 255)) {
                    OptionKey findOption = SocketOptionRegistry.findOption(socketOption, protocolFamily);
                    if (findOption != null) {
                        if (type == Integer.class) {
                            booleanValue = ((Integer) obj).intValue();
                        } else {
                            booleanValue = ((Boolean) obj).booleanValue();
                        }
                        setIntOption0(fileDescriptor, protocolFamily == UNSPEC, findOption.level(), findOption.name(), booleanValue, protocolFamily == StandardProtocolFamily.INET6);
                        return;
                    }
                    throw new AssertionError((Object) "Option not found");
                } else {
                    throw new IllegalArgumentException("Invalid TTL/hop value");
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid option value");
        }
    }

    static Object getSocketOption(FileDescriptor fileDescriptor, ProtocolFamily protocolFamily, SocketOption<?> socketOption) throws IOException {
        Class<?> type = socketOption.type();
        if (type == SocketFlow.class) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new NetworkPermission("getOption.SO_FLOW_SLA"));
            }
            SocketFlow create = SocketFlow.create();
            ExtendedOptionsImpl.getFlowOption(fileDescriptor, create);
            return create;
        } else if (type == Integer.class || type == Boolean.class) {
            OptionKey findOption = SocketOptionRegistry.findOption(socketOption, protocolFamily);
            if (findOption != null) {
                int intOption0 = getIntOption0(fileDescriptor, protocolFamily == UNSPEC, findOption.level(), findOption.name());
                if (type == Integer.class) {
                    return Integer.valueOf(intOption0);
                }
                if (intOption0 == 0) {
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            }
            throw new AssertionError((Object) "Option not found");
        } else {
            throw new AssertionError((Object) "Should not reach here");
        }
    }

    public static boolean isFastTcpLoopbackRequested() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return System.getProperty("jdk.net.useFastTcpLoopback");
            }
        });
        if ("".equals(str)) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }

    static FileDescriptor socket(boolean z) throws IOException {
        return socket(UNSPEC, z);
    }

    static FileDescriptor socket(ProtocolFamily protocolFamily, boolean z) throws IOException {
        return IOUtil.newFD(socket0(isIPv6Available() && protocolFamily != StandardProtocolFamily.INET, z, false, fastLoopback));
    }

    static FileDescriptor serverSocket(boolean z) {
        return IOUtil.newFD(socket0(isIPv6Available(), z, true, fastLoopback));
    }

    public static void bind(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws IOException {
        bind(UNSPEC, fileDescriptor, inetAddress, i);
    }

    static void bind(ProtocolFamily protocolFamily, FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws IOException {
        bind0(fileDescriptor, isIPv6Available() && protocolFamily != StandardProtocolFamily.INET, exclusiveBind, inetAddress, i);
    }

    static int connect(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws IOException {
        return connect(UNSPEC, fileDescriptor, inetAddress, i);
    }

    static int connect(ProtocolFamily protocolFamily, FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws IOException {
        BlockGuard.getThreadPolicy().onNetwork();
        return connect0(isIPv6Available() && protocolFamily != StandardProtocolFamily.INET, fileDescriptor, inetAddress, i);
    }

    public static InetSocketAddress localAddress(FileDescriptor fileDescriptor) throws IOException {
        return new InetSocketAddress(localInetAddress(fileDescriptor), localPort(fileDescriptor));
    }

    static InetSocketAddress remoteAddress(FileDescriptor fileDescriptor) throws IOException {
        return new InetSocketAddress(remoteInetAddress(fileDescriptor), remotePort(fileDescriptor));
    }

    static int join4(FileDescriptor fileDescriptor, int i, int i2, int i3) throws IOException {
        return joinOrDrop4(true, fileDescriptor, i, i2, i3);
    }

    static void drop4(FileDescriptor fileDescriptor, int i, int i2, int i3) throws IOException {
        joinOrDrop4(false, fileDescriptor, i, i2, i3);
    }

    static int block4(FileDescriptor fileDescriptor, int i, int i2, int i3) throws IOException {
        return blockOrUnblock4(true, fileDescriptor, i, i2, i3);
    }

    static void unblock4(FileDescriptor fileDescriptor, int i, int i2, int i3) throws IOException {
        blockOrUnblock4(false, fileDescriptor, i, i2, i3);
    }

    static int join6(FileDescriptor fileDescriptor, byte[] bArr, int i, byte[] bArr2) throws IOException {
        return joinOrDrop6(true, fileDescriptor, bArr, i, bArr2);
    }

    static void drop6(FileDescriptor fileDescriptor, byte[] bArr, int i, byte[] bArr2) throws IOException {
        joinOrDrop6(false, fileDescriptor, bArr, i, bArr2);
    }

    static int block6(FileDescriptor fileDescriptor, byte[] bArr, int i, byte[] bArr2) throws IOException {
        return blockOrUnblock6(true, fileDescriptor, bArr, i, bArr2);
    }

    static void unblock6(FileDescriptor fileDescriptor, byte[] bArr, int i, byte[] bArr2) throws IOException {
        blockOrUnblock6(false, fileDescriptor, bArr, i, bArr2);
    }
}
