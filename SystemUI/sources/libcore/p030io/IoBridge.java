package libcore.p030io;

import android.annotation.SystemApi;
import android.icu.text.DateFormat;
import android.system.C0308Os;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.system.StructGroupReq;
import android.system.StructLinger;
import android.system.StructPollfd;
import android.system.StructTimeval;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.BindException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.p026io.FileDescriptor;
import java.p026io.FileNotFoundException;
import java.p026io.IOException;
import java.util.concurrent.TimeUnit;
import libcore.util.ArrayUtils;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
/* renamed from: libcore.io.IoBridge */
public final class IoBridge {
    public static final int JAVA_IP_MULTICAST_TTL = 17;
    public static final int JAVA_IP_TTL = 25;
    public static final int JAVA_MCAST_JOIN_GROUP = 19;
    public static final int JAVA_MCAST_LEAVE_GROUP = 20;

    private static boolean booleanFromInt(int i) {
        return i != 0;
    }

    private static int booleanToInt(boolean z) {
        return z ? 1 : 0;
    }

    private IoBridge() {
    }

    public static int available(FileDescriptor fileDescriptor) throws IOException {
        try {
            int ioctlInt = Libcore.f857os.ioctlInt(fileDescriptor, OsConstants.FIONREAD);
            if (ioctlInt < 0) {
                return 0;
            }
            return ioctlInt;
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.ENOTTY) {
                return 0;
            }
            throw e.rethrowAsIOException();
        }
    }

    public static void bind(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws SocketException {
        if (inetAddress instanceof Inet6Address) {
            Inet6Address inet6Address = (Inet6Address) inetAddress;
            if (inet6Address.getScopeId() == 0 && inet6Address.isLinkLocalAddress()) {
                NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(inetAddress);
                if (byInetAddress != null) {
                    try {
                        inetAddress = Inet6Address.getByAddress(inetAddress.getHostName(), inetAddress.getAddress(), byInetAddress.getIndex());
                    } catch (UnknownHostException e) {
                        throw new AssertionError((Object) e);
                    }
                } else {
                    throw new SocketException("Can't bind to a link-local address without a scope id: " + inetAddress);
                }
            }
        }
        try {
            Libcore.f857os.bind(fileDescriptor, inetAddress, i);
        } catch (ErrnoException e2) {
            if (e2.errno == OsConstants.EADDRINUSE || e2.errno == OsConstants.EADDRNOTAVAIL || e2.errno == OsConstants.EPERM || e2.errno == OsConstants.EACCES) {
                throw new BindException(e2.getMessage(), e2);
            }
            throw new SocketException(e2.getMessage(), e2);
        }
    }

    public static void connect(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws SocketException {
        try {
            connect(fileDescriptor, inetAddress, i, 0);
        } catch (SocketTimeoutException e) {
            throw new AssertionError((Object) e);
        }
    }

    public static void connect(FileDescriptor fileDescriptor, InetAddress inetAddress, int i, int i2) throws SocketException, SocketTimeoutException {
        try {
            connectErrno(fileDescriptor, inetAddress, i, i2);
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.EHOSTUNREACH) {
                throw new NoRouteToHostException("Host unreachable");
            } else if (e.errno == OsConstants.EADDRNOTAVAIL) {
                throw new NoRouteToHostException("Address not available");
            } else {
                throw new ConnectException(createMessageForException(fileDescriptor, inetAddress, i, i2, e), e);
            }
        } catch (SocketException e2) {
            throw e2;
        } catch (SocketTimeoutException e3) {
            throw e3;
        } catch (IOException e4) {
            throw new SocketException((Throwable) e4);
        }
    }

    private static void connectErrno(FileDescriptor fileDescriptor, InetAddress inetAddress, int i, int i2) throws ErrnoException, IOException {
        int millis;
        if (i2 <= 0) {
            Libcore.f857os.connect(fileDescriptor, inetAddress, i);
            return;
        }
        IoUtils.setBlocking(fileDescriptor, false);
        long nanoTime = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos((long) i2);
        try {
            Libcore.f857os.connect(fileDescriptor, inetAddress, i);
            IoUtils.setBlocking(fileDescriptor, true);
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.EINPROGRESS) {
                do {
                    millis = (int) TimeUnit.NANOSECONDS.toMillis(nanoTime - System.nanoTime());
                    if (millis <= 0) {
                        throw new SocketTimeoutException(createMessageForException(fileDescriptor, inetAddress, i, i2, (Exception) null));
                    }
                } while (!isConnected(fileDescriptor, inetAddress, i, i2, millis));
                IoUtils.setBlocking(fileDescriptor, true);
                return;
            }
            throw e;
        }
    }

    private static String createMessageForException(FileDescriptor fileDescriptor, InetAddress inetAddress, int i, int i2, Exception exc) {
        InetSocketAddress inetSocketAddress;
        try {
            inetSocketAddress = getLocalInetSocketAddress(fileDescriptor);
        } catch (SocketException unused) {
            inetSocketAddress = null;
        }
        StringBuilder sb = new StringBuilder("failed to connect to ");
        sb.append((Object) inetAddress);
        sb.append(" (port ");
        sb.append(i);
        sb.append(NavigationBarInflaterView.KEY_CODE_END);
        if (inetSocketAddress != null) {
            sb.append(" from ");
            sb.append((Object) inetSocketAddress.getAddress());
            sb.append(" (port ");
            sb.append(inetSocketAddress.getPort());
            sb.append(NavigationBarInflaterView.KEY_CODE_END);
        }
        if (i2 > 0) {
            sb.append(" after ");
            sb.append(i2);
            sb.append(DateFormat.MINUTE_SECOND);
        }
        if (exc != null) {
            sb.append(": ");
            sb.append(exc.getMessage());
        }
        return sb.toString();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void closeAndSignalBlockedThreads(FileDescriptor fileDescriptor) throws IOException {
        if (fileDescriptor != null) {
            FileDescriptor release$ = fileDescriptor.release$();
            if (release$.valid()) {
                AsynchronousCloseMonitor.signalBlockedThreads(release$);
                try {
                    Libcore.f857os.close(release$);
                } catch (ErrnoException e) {
                    throw e.rethrowAsIOException();
                }
            }
        }
    }

    public static boolean isConnected(FileDescriptor fileDescriptor, InetAddress inetAddress, int i, int i2, int i3) throws IOException {
        try {
            StructPollfd structPollfd = new StructPollfd();
            StructPollfd[] structPollfdArr = {structPollfd};
            structPollfd.f59fd = fileDescriptor;
            structPollfdArr[0].events = (short) OsConstants.POLLOUT;
            if (Libcore.f857os.poll(structPollfdArr, i3) == 0) {
                return false;
            }
            int i4 = Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_ERROR);
            if (i4 == 0) {
                return true;
            }
            throw new ErrnoException("isConnected", i4);
        } catch (ErrnoException e) {
            if (fileDescriptor.valid()) {
                String createMessageForException = createMessageForException(fileDescriptor, inetAddress, i, i2, e);
                if (e.errno == OsConstants.ETIMEDOUT) {
                    SocketTimeoutException socketTimeoutException = new SocketTimeoutException(createMessageForException);
                    socketTimeoutException.initCause(e);
                    throw socketTimeoutException;
                }
                throw new ConnectException(createMessageForException, e);
            }
            throw new SocketException("Socket closed");
        }
    }

    public static Object getSocketOption(FileDescriptor fileDescriptor, int i) throws SocketException {
        try {
            return getSocketOptionErrno(fileDescriptor, i);
        } catch (ErrnoException e) {
            throw e.rethrowAsSocketException();
        }
    }

    private static Object getSocketOptionErrno(FileDescriptor fileDescriptor, int i) throws ErrnoException, SocketException {
        if (i == 1) {
            return Boolean.valueOf(booleanFromInt(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.IPPROTO_TCP, OsConstants.TCP_NODELAY)));
        }
        if (i == 8) {
            return Boolean.valueOf(booleanFromInt(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_KEEPALIVE)));
        }
        if (i == 25) {
            return Integer.valueOf(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_UNICAST_HOPS));
        }
        if (i == 128) {
            StructLinger structLinger = Libcore.f857os.getsockoptLinger(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_LINGER);
            if (!structLinger.isOn()) {
                return false;
            }
            return Integer.valueOf(structLinger.l_linger);
        } else if (i == 4102) {
            return Integer.valueOf((int) Libcore.f857os.getsockoptTimeval(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_RCVTIMEO).toMillis());
        } else {
            if (i == 3) {
                return Integer.valueOf(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_TCLASS));
            }
            if (i == 4) {
                return Boolean.valueOf(booleanFromInt(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_REUSEADDR)));
            }
            if (i != 31) {
                if (i == 32) {
                    return Boolean.valueOf(booleanFromInt(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_BROADCAST)));
                }
                switch (i) {
                    case 15:
                        return ((InetSocketAddress) Libcore.f857os.getsockname(fileDescriptor)).getAddress();
                    case 16:
                        break;
                    case 17:
                        return Integer.valueOf(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_MULTICAST_HOPS));
                    case 18:
                        return Boolean.valueOf(!booleanFromInt(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_MULTICAST_LOOP)));
                    default:
                        switch (i) {
                            case 4097:
                                return Integer.valueOf(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_SNDBUF));
                            case 4098:
                                return Integer.valueOf(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_RCVBUF));
                            case 4099:
                                return Boolean.valueOf(booleanFromInt(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_OOBINLINE)));
                            default:
                                throw new SocketException("Unknown socket option: " + i);
                        }
                }
            }
            return Integer.valueOf(Libcore.f857os.getsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_MULTICAST_IF));
        }
    }

    public static void setSocketOption(FileDescriptor fileDescriptor, int i, Object obj) throws SocketException {
        try {
            setSocketOptionErrno(fileDescriptor, i, obj);
        } catch (ErrnoException e) {
            throw e.rethrowAsSocketException();
        }
    }

    private static void setSocketOptionErrno(FileDescriptor fileDescriptor, int i, Object obj) throws ErrnoException, SocketException {
        int i2;
        boolean z = true;
        if (i == 1) {
            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.IPPROTO_TCP, OsConstants.TCP_NODELAY, booleanToInt(((Boolean) obj).booleanValue()));
        } else if (i == 8) {
            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_KEEPALIVE, booleanToInt(((Boolean) obj).booleanValue()));
        } else if (i == 25) {
            Integer num = (Integer) obj;
            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.IPPROTO_IP, OsConstants.IP_TTL, num.intValue());
            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_UNICAST_HOPS, num.intValue());
        } else if (i == 128) {
            if (obj instanceof Integer) {
                i2 = Math.min(((Integer) obj).intValue(), 65535);
            } else {
                z = false;
                i2 = 0;
            }
            Libcore.f857os.setsockoptLinger(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_LINGER, new StructLinger(booleanToInt(z), i2));
        } else if (i == 4102) {
            Libcore.f857os.setsockoptTimeval(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_RCVTIMEO, StructTimeval.fromMillis((long) ((Integer) obj).intValue()));
        } else if (i == 3) {
            Integer num2 = (Integer) obj;
            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.IPPROTO_IP, OsConstants.IP_TOS, num2.intValue());
            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_TCLASS, num2.intValue());
        } else if (i == 4) {
            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_REUSEADDR, booleanToInt(((Boolean) obj).booleanValue()));
        } else if (i == 31) {
            Integer num3 = (Integer) obj;
            Libcore.f857os.setsockoptIpMreqn(fileDescriptor, OsConstants.IPPROTO_IP, OsConstants.IP_MULTICAST_IF, num3.intValue());
            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_MULTICAST_IF, num3.intValue());
        } else if (i != 32) {
            switch (i) {
                case 16:
                    NetworkInterface byInetAddress = NetworkInterface.getByInetAddress((InetAddress) obj);
                    if (byInetAddress != null) {
                        Libcore.f857os.setsockoptIpMreqn(fileDescriptor, OsConstants.IPPROTO_IP, OsConstants.IP_MULTICAST_IF, byInetAddress.getIndex());
                        Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_MULTICAST_IF, byInetAddress.getIndex());
                        return;
                    }
                    throw new SocketException("bad argument for IP_MULTICAST_IF : address not bound to any interface");
                case 17:
                    Integer num4 = (Integer) obj;
                    Libcore.f857os.setsockoptByte(fileDescriptor, OsConstants.IPPROTO_IP, OsConstants.IP_MULTICAST_TTL, num4.intValue());
                    Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_MULTICAST_HOPS, num4.intValue());
                    return;
                case 18:
                    int booleanToInt = booleanToInt(!((Boolean) obj).booleanValue());
                    Libcore.f857os.setsockoptByte(fileDescriptor, OsConstants.IPPROTO_IP, OsConstants.IP_MULTICAST_LOOP, booleanToInt);
                    Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.IPPROTO_IPV6, OsConstants.IPV6_MULTICAST_LOOP, booleanToInt);
                    return;
                case 19:
                case 20:
                    StructGroupReq structGroupReq = (StructGroupReq) obj;
                    Libcore.f857os.setsockoptGroupReq(fileDescriptor, structGroupReq.gr_group instanceof Inet4Address ? OsConstants.IPPROTO_IP : OsConstants.IPPROTO_IPV6, i == 19 ? OsConstants.MCAST_JOIN_GROUP : OsConstants.MCAST_LEAVE_GROUP, structGroupReq);
                    return;
                default:
                    switch (i) {
                        case 4097:
                            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_SNDBUF, ((Integer) obj).intValue());
                            return;
                        case 4098:
                            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_RCVBUF, ((Integer) obj).intValue());
                            return;
                        case 4099:
                            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_OOBINLINE, booleanToInt(((Boolean) obj).booleanValue()));
                            return;
                        default:
                            throw new SocketException("Unknown socket option: " + i);
                    }
            }
        } else {
            Libcore.f857os.setsockoptInt(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_BROADCAST, booleanToInt(((Boolean) obj).booleanValue()));
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static FileDescriptor open(String str, int i) throws FileNotFoundException {
        try {
            FileDescriptor open = Libcore.f857os.open(str, i, 438);
            if (!OsConstants.S_ISDIR(Libcore.f857os.fstat(open).st_mode)) {
                return open;
            }
            throw new ErrnoException("open", OsConstants.EISDIR);
        } catch (ErrnoException e) {
            if (0 != 0) {
                try {
                    closeAndSignalBlockedThreads((FileDescriptor) null);
                } catch (IOException unused) {
                }
            }
            FileNotFoundException fileNotFoundException = new FileNotFoundException(str + ": " + e.getMessage());
            fileNotFoundException.initCause(e);
            throw fileNotFoundException;
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int read(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws IOException {
        ArrayUtils.throwsIfOutOfBounds(bArr.length, i, i2);
        if (i2 == 0) {
            return 0;
        }
        try {
            int read = Libcore.f857os.read(fileDescriptor, bArr, i, i2);
            if (read == 0) {
                return -1;
            }
            return read;
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.EAGAIN) {
                return 0;
            }
            throw e.rethrowAsIOException();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void write(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws IOException {
        ArrayUtils.throwsIfOutOfBounds(bArr.length, i, i2);
        if (i2 != 0) {
            while (i2 > 0) {
                try {
                    int write = Libcore.f857os.write(fileDescriptor, bArr, i, i2);
                    i2 -= write;
                    i += write;
                } catch (ErrnoException e) {
                    throw e.rethrowAsIOException();
                }
            }
        }
    }

    public static int sendto(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, InetAddress inetAddress, int i4) throws IOException {
        boolean z = inetAddress != null;
        if (!z && i2 <= 0) {
            return 0;
        }
        try {
            return Libcore.f857os.sendto(fileDescriptor, bArr, i, i2, i3, inetAddress, i4);
        } catch (ErrnoException e) {
            return maybeThrowAfterSendto(z, e);
        }
    }

    public static int sendto(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, InetAddress inetAddress, int i2) throws IOException {
        boolean z = inetAddress != null;
        if (!z && byteBuffer.remaining() == 0) {
            return 0;
        }
        try {
            return Libcore.f857os.sendto(fileDescriptor, byteBuffer, i, inetAddress, i2);
        } catch (ErrnoException e) {
            return maybeThrowAfterSendto(z, e);
        }
    }

    private static int maybeThrowAfterSendto(boolean z, ErrnoException errnoException) throws IOException {
        if (z) {
            if (errnoException.errno == OsConstants.ECONNREFUSED) {
                throw new PortUnreachableException("ICMP Port Unreachable");
            }
        } else if (errnoException.errno == OsConstants.EAGAIN) {
            return 0;
        }
        throw errnoException.rethrowAsIOException();
    }

    public static int recvfrom(boolean z, FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3, DatagramPacket datagramPacket, boolean z2) throws IOException {
        InetSocketAddress inetSocketAddress;
        if (datagramPacket != null) {
            try {
                inetSocketAddress = new InetSocketAddress();
            } catch (ErrnoException e) {
                return maybeThrowAfterRecvfrom(z, z2, e);
            }
        } else {
            inetSocketAddress = null;
        }
        return postRecvfrom(z, datagramPacket, inetSocketAddress, Libcore.f857os.recvfrom(fileDescriptor, bArr, i, i2, i3, inetSocketAddress));
    }

    public static int recvfrom(boolean z, FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i, DatagramPacket datagramPacket, boolean z2) throws IOException {
        InetSocketAddress inetSocketAddress;
        if (datagramPacket != null) {
            try {
                inetSocketAddress = new InetSocketAddress();
            } catch (ErrnoException e) {
                return maybeThrowAfterRecvfrom(z, z2, e);
            }
        } else {
            inetSocketAddress = null;
        }
        return postRecvfrom(z, datagramPacket, inetSocketAddress, Libcore.f857os.recvfrom(fileDescriptor, byteBuffer, i, inetSocketAddress));
    }

    private static int postRecvfrom(boolean z, DatagramPacket datagramPacket, InetSocketAddress inetSocketAddress, int i) {
        if (z && i == 0) {
            return -1;
        }
        if (datagramPacket != null) {
            datagramPacket.setReceivedLength(i);
            datagramPacket.setPort(inetSocketAddress.getPort());
            if (!inetSocketAddress.getAddress().equals(datagramPacket.getAddress())) {
                datagramPacket.setAddress(inetSocketAddress.getAddress());
            }
        }
        return i;
    }

    private static int maybeThrowAfterRecvfrom(boolean z, boolean z2, ErrnoException errnoException) throws SocketException, SocketTimeoutException {
        if (z) {
            if (errnoException.errno == OsConstants.EAGAIN) {
                return 0;
            }
            throw errnoException.rethrowAsSocketException();
        } else if (z2 && errnoException.errno == OsConstants.ECONNREFUSED) {
            throw new PortUnreachableException("ICMP Port Unreachable", errnoException);
        } else if (errnoException.errno == OsConstants.EAGAIN) {
            SocketTimeoutException socketTimeoutException = new SocketTimeoutException();
            socketTimeoutException.initCause(errnoException);
            throw socketTimeoutException;
        } else {
            throw errnoException.rethrowAsSocketException();
        }
    }

    public static FileDescriptor socket(int i, int i2, int i3) throws SocketException {
        try {
            return Libcore.f857os.socket(i, i2, i3);
        } catch (ErrnoException e) {
            throw e.rethrowAsSocketException();
        }
    }

    public static void poll(FileDescriptor fileDescriptor, int i, int i2) throws SocketException, SocketTimeoutException {
        StructPollfd structPollfd = new StructPollfd();
        StructPollfd[] structPollfdArr = {structPollfd};
        structPollfd.f59fd = fileDescriptor;
        structPollfdArr[0].events = (short) i;
        try {
            if (C0308Os.poll(structPollfdArr, i2) == 0) {
                throw new SocketTimeoutException("Poll timed out");
            }
        } catch (ErrnoException e) {
            e.rethrowAsSocketException();
        }
    }

    public static InetSocketAddress getLocalInetSocketAddress(FileDescriptor fileDescriptor) throws SocketException {
        try {
            SocketAddress socketAddress = Libcore.f857os.getsockname(fileDescriptor);
            if (socketAddress != null) {
                if (!(socketAddress instanceof InetSocketAddress)) {
                    throw new SocketException("Socket assumed to be pending closure: Expected sockname to be an InetSocketAddress, got " + socketAddress.getClass());
                }
            }
            return (InetSocketAddress) socketAddress;
        } catch (ErrnoException e) {
            throw e.rethrowAsSocketException();
        }
    }
}
