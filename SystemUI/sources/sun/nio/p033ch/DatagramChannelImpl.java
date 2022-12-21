package sun.nio.p033ch;

import dalvik.system.BlockGuard;
import dalvik.system.CloseGuard;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.PortUnreachableException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.channels.spi.SelectorProvider;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jdk.net.ExtendedSocketOptions;
import sun.net.ExtendedOptionsImpl;
import sun.net.ResourceManager;
import sun.nio.p033ch.MembershipKeyImpl;

/* renamed from: sun.nio.ch.DatagramChannelImpl */
class DatagramChannelImpl extends DatagramChannel implements SelChImpl {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ST_CONNECTED = 1;
    private static final int ST_KILLED = 2;
    private static final int ST_UNCONNECTED = 0;
    private static final int ST_UNINITIALIZED = -1;

    /* renamed from: nd */
    private static NativeDispatcher f879nd = new DatagramDispatcher();
    private InetAddress cachedSenderInetAddress;
    private int cachedSenderPort;
    private final ProtocolFamily family;

    /* renamed from: fd */
    final FileDescriptor f880fd;
    private final int fdVal;
    private final CloseGuard guard;
    private boolean isReuseAddress;
    private InetSocketAddress localAddress;
    private final Object readLock = new Object();
    private volatile long readerThread = 0;
    private MembershipRegistry registry;
    private InetSocketAddress remoteAddress;
    private boolean reuseAddressEmulated;
    private SocketAddress sender;
    private DatagramSocket socket;
    private int state = -1;
    private final Object stateLock = new Object();
    private final Object writeLock = new Object();
    private volatile long writerThread = 0;

    private static native void disconnect0(FileDescriptor fileDescriptor, boolean z) throws IOException;

    private static native void initIDs();

    private native int receive0(FileDescriptor fileDescriptor, long j, int i, boolean z) throws IOException;

    private native int send0(boolean z, FileDescriptor fileDescriptor, long j, int i, InetAddress inetAddress, int i2) throws IOException;

    static {
        initIDs();
    }

    public DatagramChannelImpl(SelectorProvider sp) throws IOException {
        super(sp);
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        ResourceManager.beforeUdpCreate();
        try {
            StandardProtocolFamily standardProtocolFamily = Net.isIPv6Available() ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET;
            this.family = standardProtocolFamily;
            FileDescriptor socket2 = Net.socket(standardProtocolFamily, false);
            this.f880fd = socket2;
            this.fdVal = IOUtil.fdVal(socket2);
            this.state = 0;
            if (socket2 != null && socket2.valid()) {
                closeGuard.open("close");
            }
        } catch (IOException e) {
            ResourceManager.afterUdpClose();
            throw e;
        }
    }

    public DatagramChannelImpl(SelectorProvider sp, ProtocolFamily family2) throws IOException {
        super(sp);
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        if (family2 == StandardProtocolFamily.INET || family2 == StandardProtocolFamily.INET6) {
            if (family2 != StandardProtocolFamily.INET6 || Net.isIPv6Available()) {
                this.family = family2;
                FileDescriptor socket2 = Net.socket(family2, false);
                this.f880fd = socket2;
                this.fdVal = IOUtil.fdVal(socket2);
                this.state = 0;
                if (socket2 != null && socket2.valid()) {
                    closeGuard.open("close");
                    return;
                }
                return;
            }
            throw new UnsupportedOperationException("IPv6 not available");
        } else if (family2 == null) {
            throw new NullPointerException("'family' is null");
        } else {
            throw new UnsupportedOperationException("Protocol family not supported");
        }
    }

    public DatagramChannelImpl(SelectorProvider sp, FileDescriptor fd) throws IOException {
        super(sp);
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.family = Net.isIPv6Available() ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET;
        this.f880fd = fd;
        this.fdVal = IOUtil.fdVal(fd);
        this.state = 0;
        this.localAddress = Net.localAddress(fd);
        if (fd != null && fd.valid()) {
            closeGuard.open("close");
        }
    }

    public DatagramSocket socket() {
        DatagramSocket datagramSocket;
        synchronized (this.stateLock) {
            if (this.socket == null) {
                this.socket = DatagramSocketAdaptor.create(this);
            }
            datagramSocket = this.socket;
        }
        return datagramSocket;
    }

    public SocketAddress getLocalAddress() throws IOException {
        InetSocketAddress revealedLocalAddress;
        synchronized (this.stateLock) {
            if (isOpen()) {
                revealedLocalAddress = Net.getRevealedLocalAddress(this.localAddress);
            } else {
                throw new ClosedChannelException();
            }
        }
        return revealedLocalAddress;
    }

    public SocketAddress getRemoteAddress() throws IOException {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            if (isOpen()) {
                inetSocketAddress = this.remoteAddress;
            } else {
                throw new ClosedChannelException();
            }
        }
        return inetSocketAddress;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0064, code lost:
        return r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> java.nio.channels.DatagramChannel setOption(java.net.SocketOption<T> r7, T r8) throws java.p026io.IOException {
        /*
            r6 = this;
            if (r7 == 0) goto L_0x00c4
            java.util.Set r0 = r6.supportedOptions()
            boolean r0 = r0.contains(r7)
            if (r0 == 0) goto L_0x00a5
            java.lang.Object r0 = r6.stateLock
            monitor-enter(r0)
            r6.ensureOpen()     // Catch:{ all -> 0x00a2 }
            java.net.SocketOption<java.lang.Integer> r1 = java.net.StandardSocketOptions.IP_TOS     // Catch:{ all -> 0x00a2 }
            if (r7 == r1) goto L_0x0099
            java.net.SocketOption<java.lang.Integer> r1 = java.net.StandardSocketOptions.IP_MULTICAST_TTL     // Catch:{ all -> 0x00a2 }
            if (r7 == r1) goto L_0x0099
            java.net.SocketOption<java.lang.Boolean> r1 = java.net.StandardSocketOptions.IP_MULTICAST_LOOP     // Catch:{ all -> 0x00a2 }
            if (r7 != r1) goto L_0x0020
            goto L_0x0099
        L_0x0020:
            java.net.SocketOption<java.net.NetworkInterface> r1 = java.net.StandardSocketOptions.IP_MULTICAST_IF     // Catch:{ all -> 0x00a2 }
            if (r7 != r1) goto L_0x0075
            if (r8 == 0) goto L_0x006d
            r1 = r8
            java.net.NetworkInterface r1 = (java.net.NetworkInterface) r1     // Catch:{ all -> 0x00a2 }
            java.net.ProtocolFamily r2 = r6.family     // Catch:{ all -> 0x00a2 }
            java.net.StandardProtocolFamily r3 = java.net.StandardProtocolFamily.INET6     // Catch:{ all -> 0x00a2 }
            if (r2 != r3) goto L_0x0053
            int r2 = r1.getIndex()     // Catch:{ all -> 0x00a2 }
            r3 = -1
            if (r2 == r3) goto L_0x004b
            java.io.FileDescriptor r3 = r6.f880fd     // Catch:{ all -> 0x00a2 }
            sun.nio.p033ch.Net.setInterface6(r3, r2)     // Catch:{ all -> 0x00a2 }
            java.net.Inet4Address r3 = sun.nio.p033ch.Net.anyInet4Address(r1)     // Catch:{ all -> 0x00a2 }
            if (r3 == 0) goto L_0x004a
            int r4 = sun.nio.p033ch.Net.inet4AsInt(r3)     // Catch:{ all -> 0x00a2 }
            java.io.FileDescriptor r5 = r6.f880fd     // Catch:{ all -> 0x00a2 }
            sun.nio.p033ch.Net.setInterface4(r5, r4)     // Catch:{ all -> 0x00a2 }
        L_0x004a:
            goto L_0x0062
        L_0x004b:
            java.io.IOException r3 = new java.io.IOException     // Catch:{ all -> 0x00a2 }
            java.lang.String r4 = "Network interface cannot be identified"
            r3.<init>((java.lang.String) r4)     // Catch:{ all -> 0x00a2 }
            throw r3     // Catch:{ all -> 0x00a2 }
        L_0x0053:
            java.net.Inet4Address r2 = sun.nio.p033ch.Net.anyInet4Address(r1)     // Catch:{ all -> 0x00a2 }
            if (r2 == 0) goto L_0x0065
            int r3 = sun.nio.p033ch.Net.inet4AsInt(r2)     // Catch:{ all -> 0x00a2 }
            java.io.FileDescriptor r4 = r6.f880fd     // Catch:{ all -> 0x00a2 }
            sun.nio.p033ch.Net.setInterface4(r4, r3)     // Catch:{ all -> 0x00a2 }
        L_0x0062:
            monitor-exit(r0)     // Catch:{ all -> 0x00a2 }
            return r6
        L_0x0065:
            java.io.IOException r3 = new java.io.IOException     // Catch:{ all -> 0x00a2 }
            java.lang.String r4 = "Network interface not configured for IPv4"
            r3.<init>((java.lang.String) r4)     // Catch:{ all -> 0x00a2 }
            throw r3     // Catch:{ all -> 0x00a2 }
        L_0x006d:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x00a2 }
            java.lang.String r2 = "Cannot set IP_MULTICAST_IF to 'null'"
            r1.<init>((java.lang.String) r2)     // Catch:{ all -> 0x00a2 }
            throw r1     // Catch:{ all -> 0x00a2 }
        L_0x0075:
            java.net.SocketOption<java.lang.Boolean> r1 = java.net.StandardSocketOptions.SO_REUSEADDR     // Catch:{ all -> 0x00a2 }
            if (r7 != r1) goto L_0x008f
            boolean r1 = sun.nio.p033ch.Net.useExclusiveBind()     // Catch:{ all -> 0x00a2 }
            if (r1 == 0) goto L_0x008f
            java.net.InetSocketAddress r1 = r6.localAddress     // Catch:{ all -> 0x00a2 }
            if (r1 == 0) goto L_0x008f
            r1 = 1
            r6.reuseAddressEmulated = r1     // Catch:{ all -> 0x00a2 }
            r1 = r8
            java.lang.Boolean r1 = (java.lang.Boolean) r1     // Catch:{ all -> 0x00a2 }
            boolean r1 = r1.booleanValue()     // Catch:{ all -> 0x00a2 }
            r6.isReuseAddress = r1     // Catch:{ all -> 0x00a2 }
        L_0x008f:
            java.io.FileDescriptor r1 = r6.f880fd     // Catch:{ all -> 0x00a2 }
            java.net.ProtocolFamily r2 = sun.nio.p033ch.Net.UNSPEC     // Catch:{ all -> 0x00a2 }
            sun.nio.p033ch.Net.setSocketOption(r1, r2, r7, r8)     // Catch:{ all -> 0x00a2 }
            monitor-exit(r0)     // Catch:{ all -> 0x00a2 }
            return r6
        L_0x0099:
            java.io.FileDescriptor r1 = r6.f880fd     // Catch:{ all -> 0x00a2 }
            java.net.ProtocolFamily r2 = r6.family     // Catch:{ all -> 0x00a2 }
            sun.nio.p033ch.Net.setSocketOption(r1, r2, r7, r8)     // Catch:{ all -> 0x00a2 }
            monitor-exit(r0)     // Catch:{ all -> 0x00a2 }
            return r6
        L_0x00a2:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00a2 }
            throw r1
        L_0x00a5:
            java.lang.UnsupportedOperationException r0 = new java.lang.UnsupportedOperationException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "'"
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            java.lang.StringBuilder r1 = r1.append((java.lang.Object) r7)
            java.lang.String r2 = "' not supported"
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            java.lang.String r1 = r1.toString()
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x00c4:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.DatagramChannelImpl.setOption(java.net.SocketOption, java.lang.Object):java.nio.channels.DatagramChannel");
    }

    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (socketOption == null) {
            throw new NullPointerException();
        } else if (supportedOptions().contains(socketOption)) {
            synchronized (this.stateLock) {
                ensureOpen();
                if (!(socketOption == StandardSocketOptions.IP_TOS || socketOption == StandardSocketOptions.IP_MULTICAST_TTL)) {
                    if (socketOption != StandardSocketOptions.IP_MULTICAST_LOOP) {
                        if (socketOption == StandardSocketOptions.IP_MULTICAST_IF) {
                            if (this.family == StandardProtocolFamily.INET) {
                                int interface4 = Net.getInterface4(this.f880fd);
                                if (interface4 == 0) {
                                    return null;
                                }
                                T byInetAddress = NetworkInterface.getByInetAddress(Net.inet4FromInt(interface4));
                                if (byInetAddress != null) {
                                    return byInetAddress;
                                }
                                throw new IOException("Unable to map address to interface");
                            }
                            int interface6 = Net.getInterface6(this.f880fd);
                            if (interface6 == 0) {
                                return null;
                            }
                            T byIndex = NetworkInterface.getByIndex(interface6);
                            if (byIndex != null) {
                                return byIndex;
                            }
                            throw new IOException("Unable to map index to interface");
                        } else if (socketOption != StandardSocketOptions.SO_REUSEADDR || !this.reuseAddressEmulated) {
                            T socketOption2 = Net.getSocketOption(this.f880fd, Net.UNSPEC, socketOption);
                            return socketOption2;
                        } else {
                            T valueOf = Boolean.valueOf(this.isReuseAddress);
                            return valueOf;
                        }
                    }
                }
                T socketOption3 = Net.getSocketOption(this.f880fd, this.family, socketOption);
                return socketOption3;
            }
        } else {
            throw new UnsupportedOperationException("'" + socketOption + "' not supported");
        }
    }

    /* renamed from: sun.nio.ch.DatagramChannelImpl$DefaultOptionsHolder */
    private static class DefaultOptionsHolder {
        static final Set<SocketOption<?>> defaultOptions = defaultOptions();

        private DefaultOptionsHolder() {
        }

        private static Set<SocketOption<?>> defaultOptions() {
            HashSet hashSet = new HashSet(8);
            hashSet.add(StandardSocketOptions.SO_SNDBUF);
            hashSet.add(StandardSocketOptions.SO_RCVBUF);
            hashSet.add(StandardSocketOptions.SO_REUSEADDR);
            hashSet.add(StandardSocketOptions.SO_BROADCAST);
            hashSet.add(StandardSocketOptions.IP_TOS);
            hashSet.add(StandardSocketOptions.IP_MULTICAST_IF);
            hashSet.add(StandardSocketOptions.IP_MULTICAST_TTL);
            hashSet.add(StandardSocketOptions.IP_MULTICAST_LOOP);
            if (ExtendedOptionsImpl.flowSupported()) {
                hashSet.add(ExtendedSocketOptions.SO_FLOW_SLA);
            }
            return Collections.unmodifiableSet(hashSet);
        }
    }

    public final Set<SocketOption<?>> supportedOptions() {
        return DefaultOptionsHolder.defaultOptions;
    }

    private void ensureOpen() throws ClosedChannelException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006a, code lost:
        if (r3 == null) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        sun.nio.p033ch.Util.releaseTemporaryDirectBuffer(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x006f, code lost:
        r14.readerThread = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0072, code lost:
        if (r1 > 0) goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0074, code lost:
        if (r1 != -2) goto L_0x0077;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0076, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0077, code lost:
        end(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x007b, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        r3.flip();
        r15.put(r3);
     */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00b2  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x00c9 A[SYNTHETIC, Splitter:B:71:0x00c9] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.net.SocketAddress receive(java.nio.ByteBuffer r15) throws java.p026io.IOException {
        /*
            r14 = this;
            boolean r0 = r15.isReadOnly()
            if (r0 != 0) goto L_0x00f5
            if (r15 == 0) goto L_0x00ef
            java.lang.Object r0 = r14.readLock
            monitor-enter(r0)
            r14.ensureOpen()     // Catch:{ all -> 0x00ec }
            java.net.SocketAddress r1 = r14.localAddress()     // Catch:{ all -> 0x00ec }
            r2 = 0
            if (r1 != 0) goto L_0x0017
            monitor-exit(r0)     // Catch:{ all -> 0x00ec }
            return r2
        L_0x0017:
            r1 = 0
            r3 = 0
            r4 = 0
            r5 = 1
            r6 = 0
            r8 = -2
            r14.begin()     // Catch:{ all -> 0x00d9 }
            boolean r9 = r14.isOpen()     // Catch:{ all -> 0x00d9 }
            if (r9 != 0) goto L_0x003a
            if (r3 == 0) goto L_0x002d
            sun.nio.p033ch.Util.releaseTemporaryDirectBuffer(r3)     // Catch:{ all -> 0x00ec }
        L_0x002d:
            r14.readerThread = r6     // Catch:{ all -> 0x00ec }
            if (r1 > 0) goto L_0x0034
            if (r1 != r8) goto L_0x0035
        L_0x0034:
            r4 = r5
        L_0x0035:
            r14.end(r4)     // Catch:{ all -> 0x00ec }
            monitor-exit(r0)     // Catch:{ all -> 0x00ec }
            return r2
        L_0x003a:
            java.lang.SecurityManager r9 = java.lang.System.getSecurityManager()     // Catch:{ all -> 0x00d9 }
            long r10 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x00d9 }
            r14.readerThread = r10     // Catch:{ all -> 0x00d9 }
            boolean r10 = r14.isConnected()     // Catch:{ all -> 0x00d9 }
            r11 = -3
            if (r10 != 0) goto L_0x00ae
            if (r9 != 0) goto L_0x004e
            goto L_0x00ae
        L_0x004e:
            int r10 = r15.remaining()     // Catch:{ all -> 0x00d9 }
            java.nio.ByteBuffer r10 = sun.nio.p033ch.Util.getTemporaryDirectBuffer(r10)     // Catch:{ all -> 0x00d9 }
            r3 = r10
        L_0x0057:
            java.io.FileDescriptor r10 = r14.f880fd     // Catch:{ all -> 0x00d9 }
            int r10 = r14.receive(r10, r3)     // Catch:{ all -> 0x00d9 }
            r1 = r10
            if (r1 != r11) goto L_0x0067
            boolean r10 = r14.isOpen()     // Catch:{ all -> 0x00d9 }
            if (r10 != 0) goto L_0x0057
        L_0x0067:
            if (r1 != r8) goto L_0x007c
            if (r3 == 0) goto L_0x006f
            sun.nio.p033ch.Util.releaseTemporaryDirectBuffer(r3)     // Catch:{ all -> 0x00ec }
        L_0x006f:
            r14.readerThread = r6     // Catch:{ all -> 0x00ec }
            if (r1 > 0) goto L_0x0076
            if (r1 != r8) goto L_0x0077
        L_0x0076:
            r4 = r5
        L_0x0077:
            r14.end(r4)     // Catch:{ all -> 0x00ec }
            monitor-exit(r0)     // Catch:{ all -> 0x00ec }
            return r2
        L_0x007c:
            java.net.SocketAddress r10 = r14.sender     // Catch:{ all -> 0x00d9 }
            java.net.InetSocketAddress r10 = (java.net.InetSocketAddress) r10     // Catch:{ all -> 0x00d9 }
            java.net.InetAddress r12 = r10.getAddress()     // Catch:{ SecurityException -> 0x0098 }
            java.lang.String r12 = r12.getHostAddress()     // Catch:{ SecurityException -> 0x0098 }
            int r13 = r10.getPort()     // Catch:{ SecurityException -> 0x0098 }
            r9.checkAccept(r12, r13)     // Catch:{ SecurityException -> 0x0098 }
            r3.flip()     // Catch:{ all -> 0x00d9 }
            r15.put((java.nio.ByteBuffer) r3)     // Catch:{ all -> 0x00d9 }
            goto L_0x00c4
        L_0x0098:
            r12 = move-exception
            r3.clear()     // Catch:{ all -> 0x00d9 }
            r1 = 0
            goto L_0x0057
        L_0x009e:
            java.io.FileDescriptor r10 = r14.f880fd     // Catch:{ all -> 0x00d9 }
            int r10 = r14.receive(r10, r15)     // Catch:{ all -> 0x00d9 }
            r1 = r10
            if (r1 != r11) goto L_0x00af
            boolean r10 = r14.isOpen()     // Catch:{ all -> 0x00d9 }
            if (r10 != 0) goto L_0x00ae
            goto L_0x00af
        L_0x00ae:
            goto L_0x009e
        L_0x00af:
            if (r1 != r8) goto L_0x00c4
            if (r3 == 0) goto L_0x00b7
            sun.nio.p033ch.Util.releaseTemporaryDirectBuffer(r3)     // Catch:{ all -> 0x00ec }
        L_0x00b7:
            r14.readerThread = r6     // Catch:{ all -> 0x00ec }
            if (r1 > 0) goto L_0x00be
            if (r1 != r8) goto L_0x00bf
        L_0x00be:
            r4 = r5
        L_0x00bf:
            r14.end(r4)     // Catch:{ all -> 0x00ec }
            monitor-exit(r0)     // Catch:{ all -> 0x00ec }
            return r2
        L_0x00c4:
            java.net.SocketAddress r2 = r14.sender     // Catch:{ all -> 0x00d9 }
            if (r3 == 0) goto L_0x00cc
            sun.nio.p033ch.Util.releaseTemporaryDirectBuffer(r3)     // Catch:{ all -> 0x00ec }
        L_0x00cc:
            r14.readerThread = r6     // Catch:{ all -> 0x00ec }
            if (r1 > 0) goto L_0x00d3
            if (r1 != r8) goto L_0x00d4
        L_0x00d3:
            r4 = r5
        L_0x00d4:
            r14.end(r4)     // Catch:{ all -> 0x00ec }
            monitor-exit(r0)     // Catch:{ all -> 0x00ec }
            return r2
        L_0x00d9:
            r2 = move-exception
            if (r3 == 0) goto L_0x00df
            sun.nio.p033ch.Util.releaseTemporaryDirectBuffer(r3)     // Catch:{ all -> 0x00ec }
        L_0x00df:
            r14.readerThread = r6     // Catch:{ all -> 0x00ec }
            if (r1 > 0) goto L_0x00e6
            if (r1 != r8) goto L_0x00e7
        L_0x00e6:
            r4 = r5
        L_0x00e7:
            r14.end(r4)     // Catch:{ all -> 0x00ec }
            throw r2     // Catch:{ all -> 0x00ec }
        L_0x00ec:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00ec }
            throw r1
        L_0x00ef:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            r0.<init>()
            throw r0
        L_0x00f5:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Read-only buffer"
            r0.<init>((java.lang.String) r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.DatagramChannelImpl.receive(java.nio.ByteBuffer):java.net.SocketAddress");
    }

    private int receive(FileDescriptor fd, ByteBuffer dst) throws IOException {
        int position = dst.position();
        int limit = dst.limit();
        int i = position <= limit ? limit - position : 0;
        if ((dst instanceof DirectBuffer) && i > 0) {
            return receiveIntoNativeBuffer(fd, dst, i, position);
        }
        int max = Math.max(i, 1);
        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(max);
        try {
            BlockGuard.getThreadPolicy().onNetwork();
            int receiveIntoNativeBuffer = receiveIntoNativeBuffer(fd, temporaryDirectBuffer, max, 0);
            temporaryDirectBuffer.flip();
            if (receiveIntoNativeBuffer > 0 && i > 0) {
                dst.put(temporaryDirectBuffer);
            }
            return receiveIntoNativeBuffer;
        } finally {
            Util.releaseTemporaryDirectBuffer(temporaryDirectBuffer);
        }
    }

    private int receiveIntoNativeBuffer(FileDescriptor fd, ByteBuffer bb, int rem, int pos) throws IOException {
        int receive0 = receive0(fd, ((DirectBuffer) bb).address() + ((long) pos), rem, isConnected());
        if (receive0 > 0) {
            bb.position(pos + receive0);
        }
        return receive0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003b, code lost:
        r3 = 0;
        r5 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        begin();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0048, code lost:
        if (isOpen() != false) goto L_0x0058;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r11.writerThread = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004d, code lost:
        if (0 > 0) goto L_0x0053;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004f, code lost:
        if (0 != -2) goto L_0x0052;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0052, code lost:
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0053, code lost:
        end(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0057, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r11.writerThread = sun.nio.p033ch.NativeThread.current();
        dalvik.system.BlockGuard.getThreadPolicy().onNetwork();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0065, code lost:
        r3 = send(r11.f880fd, r12, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006e, code lost:
        if (r3 != -3) goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0074, code lost:
        if (isOpen() != false) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0076, code lost:
        r9 = r11.stateLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0078, code lost:
        monitor-enter(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007d, code lost:
        if (isOpen() == false) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0081, code lost:
        if (r11.localAddress != null) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0083, code lost:
        r11.localAddress = sun.nio.p033ch.Net.localAddress(r11.f880fd);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x008c, code lost:
        monitor-exit(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        r9 = sun.nio.p033ch.IOStatus.normalize(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        r11.writerThread = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0093, code lost:
        if (r3 > 0) goto L_0x0099;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0095, code lost:
        if (r3 != -2) goto L_0x0098;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0098, code lost:
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0099, code lost:
        end(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x009d, code lost:
        return r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00a1, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        r11.writerThread = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00a4, code lost:
        if (r3 <= 0) goto L_0x00a6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00a6, code lost:
        if (r3 == -2) goto L_0x00a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00a9, code lost:
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00aa, code lost:
        end(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00ae, code lost:
        throw r9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int send(java.nio.ByteBuffer r12, java.net.SocketAddress r13) throws java.p026io.IOException {
        /*
            r11 = this;
            if (r12 == 0) goto L_0x00da
            java.lang.Object r0 = r11.writeLock
            monitor-enter(r0)
            r11.ensureOpen()     // Catch:{ all -> 0x00d7 }
            java.net.InetSocketAddress r1 = sun.nio.p033ch.Net.checkAddress(r13)     // Catch:{ all -> 0x00d7 }
            java.net.InetAddress r2 = r1.getAddress()     // Catch:{ all -> 0x00d7 }
            if (r2 == 0) goto L_0x00cf
            java.lang.Object r3 = r11.stateLock     // Catch:{ all -> 0x00d7 }
            monitor-enter(r3)     // Catch:{ all -> 0x00d7 }
            boolean r4 = r11.isConnected()     // Catch:{ all -> 0x00cc }
            if (r4 != 0) goto L_0x00b5
            if (r13 == 0) goto L_0x00af
            java.lang.SecurityManager r4 = java.lang.System.getSecurityManager()     // Catch:{ all -> 0x00cc }
            if (r4 == 0) goto L_0x0038
            boolean r5 = r2.isMulticastAddress()     // Catch:{ all -> 0x00cc }
            if (r5 == 0) goto L_0x002d
            r4.checkMulticast(r2)     // Catch:{ all -> 0x00cc }
            goto L_0x0038
        L_0x002d:
            java.lang.String r5 = r2.getHostAddress()     // Catch:{ all -> 0x00cc }
            int r6 = r1.getPort()     // Catch:{ all -> 0x00cc }
            r4.checkConnect(r5, r6)     // Catch:{ all -> 0x00cc }
        L_0x0038:
            monitor-exit(r3)     // Catch:{ all -> 0x00cc }
            r3 = 0
            r4 = -2
            r5 = 1
            r6 = 0
            r8 = 0
            r11.begin()     // Catch:{ all -> 0x00a1 }
            boolean r9 = r11.isOpen()     // Catch:{ all -> 0x00a1 }
            if (r9 != 0) goto L_0x0058
            r11.writerThread = r6     // Catch:{ all -> 0x00d7 }
            if (r3 > 0) goto L_0x0053
            if (r3 != r4) goto L_0x0052
            goto L_0x0053
        L_0x0052:
            r5 = r8
        L_0x0053:
            r11.end(r5)     // Catch:{ all -> 0x00d7 }
            monitor-exit(r0)     // Catch:{ all -> 0x00d7 }
            return r8
        L_0x0058:
            long r9 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x00a1 }
            r11.writerThread = r9     // Catch:{ all -> 0x00a1 }
            dalvik.system.BlockGuard$Policy r9 = dalvik.system.BlockGuard.getThreadPolicy()     // Catch:{ all -> 0x00a1 }
            r9.onNetwork()     // Catch:{ all -> 0x00a1 }
        L_0x0065:
            java.io.FileDescriptor r9 = r11.f880fd     // Catch:{ all -> 0x00a1 }
            int r9 = r11.send(r9, r12, r1)     // Catch:{ all -> 0x00a1 }
            r3 = r9
            r9 = -3
            if (r3 != r9) goto L_0x0076
            boolean r9 = r11.isOpen()     // Catch:{ all -> 0x00a1 }
            if (r9 != 0) goto L_0x0065
        L_0x0076:
            java.lang.Object r9 = r11.stateLock     // Catch:{ all -> 0x00a1 }
            monitor-enter(r9)     // Catch:{ all -> 0x00a1 }
            boolean r10 = r11.isOpen()     // Catch:{ all -> 0x009e }
            if (r10 == 0) goto L_0x008b
            java.net.InetSocketAddress r10 = r11.localAddress     // Catch:{ all -> 0x009e }
            if (r10 != 0) goto L_0x008b
            java.io.FileDescriptor r10 = r11.f880fd     // Catch:{ all -> 0x009e }
            java.net.InetSocketAddress r10 = sun.nio.p033ch.Net.localAddress(r10)     // Catch:{ all -> 0x009e }
            r11.localAddress = r10     // Catch:{ all -> 0x009e }
        L_0x008b:
            monitor-exit(r9)     // Catch:{ all -> 0x009e }
            int r9 = sun.nio.p033ch.IOStatus.normalize((int) r3)     // Catch:{ all -> 0x00a1 }
            r11.writerThread = r6     // Catch:{ all -> 0x00d7 }
            if (r3 > 0) goto L_0x0099
            if (r3 != r4) goto L_0x0098
            goto L_0x0099
        L_0x0098:
            r5 = r8
        L_0x0099:
            r11.end(r5)     // Catch:{ all -> 0x00d7 }
            monitor-exit(r0)     // Catch:{ all -> 0x00d7 }
            return r9
        L_0x009e:
            r10 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x009e }
            throw r10     // Catch:{ all -> 0x00a1 }
        L_0x00a1:
            r9 = move-exception
            r11.writerThread = r6     // Catch:{ all -> 0x00d7 }
            if (r3 > 0) goto L_0x00aa
            if (r3 != r4) goto L_0x00a9
            goto L_0x00aa
        L_0x00a9:
            r5 = r8
        L_0x00aa:
            r11.end(r5)     // Catch:{ all -> 0x00d7 }
            throw r9     // Catch:{ all -> 0x00d7 }
        L_0x00af:
            java.lang.NullPointerException r4 = new java.lang.NullPointerException     // Catch:{ all -> 0x00cc }
            r4.<init>()     // Catch:{ all -> 0x00cc }
            throw r4     // Catch:{ all -> 0x00cc }
        L_0x00b5:
            java.net.InetSocketAddress r4 = r11.remoteAddress     // Catch:{ all -> 0x00cc }
            boolean r4 = r13.equals(r4)     // Catch:{ all -> 0x00cc }
            if (r4 == 0) goto L_0x00c4
            int r4 = r11.write(r12)     // Catch:{ all -> 0x00cc }
            monitor-exit(r3)     // Catch:{ all -> 0x00cc }
            monitor-exit(r0)     // Catch:{ all -> 0x00d7 }
            return r4
        L_0x00c4:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x00cc }
            java.lang.String r5 = "Connected address not equal to target address"
            r4.<init>((java.lang.String) r5)     // Catch:{ all -> 0x00cc }
            throw r4     // Catch:{ all -> 0x00cc }
        L_0x00cc:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x00cc }
            throw r4     // Catch:{ all -> 0x00d7 }
        L_0x00cf:
            java.io.IOException r3 = new java.io.IOException     // Catch:{ all -> 0x00d7 }
            java.lang.String r4 = "Target address not resolved"
            r3.<init>((java.lang.String) r4)     // Catch:{ all -> 0x00d7 }
            throw r3     // Catch:{ all -> 0x00d7 }
        L_0x00d7:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00d7 }
            throw r1
        L_0x00da:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.DatagramChannelImpl.send(java.nio.ByteBuffer, java.net.SocketAddress):int");
    }

    private int send(FileDescriptor fd, ByteBuffer src, InetSocketAddress target) throws IOException {
        if (src instanceof DirectBuffer) {
            return sendFromNativeBuffer(fd, src, target);
        }
        int position = src.position();
        int limit = src.limit();
        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(position <= limit ? limit - position : 0);
        try {
            temporaryDirectBuffer.put(src);
            temporaryDirectBuffer.flip();
            src.position(position);
            int sendFromNativeBuffer = sendFromNativeBuffer(fd, temporaryDirectBuffer, target);
            if (sendFromNativeBuffer > 0) {
                src.position(position + sendFromNativeBuffer);
            }
            return sendFromNativeBuffer;
        } finally {
            Util.releaseTemporaryDirectBuffer(temporaryDirectBuffer);
        }
    }

    private int sendFromNativeBuffer(FileDescriptor fd, ByteBuffer bb, InetSocketAddress target) throws IOException {
        int i;
        int position = bb.position();
        int limit = bb.limit();
        boolean z = false;
        int i2 = position <= limit ? limit - position : 0;
        if (this.family != StandardProtocolFamily.INET) {
            z = true;
        }
        try {
            i = send0(z, fd, ((DirectBuffer) bb).address() + ((long) position), i2, target.getAddress(), target.getPort());
        } catch (PortUnreachableException e) {
            if (!isConnected()) {
                i = i2;
            } else {
                throw e;
            }
        }
        if (i > 0) {
            bb.position(position + i);
        }
        return i;
    }

    public int read(ByteBuffer buf) throws IOException {
        if (buf != null) {
            synchronized (this.readLock) {
                synchronized (this.stateLock) {
                    ensureOpen();
                    if (!isConnected()) {
                        throw new NotYetConnectedException();
                    }
                }
                char c = 0;
                boolean z = true;
                try {
                    begin();
                    if (!isOpen()) {
                        this.readerThread = 0;
                        if (c <= 0) {
                            if (c != 65534) {
                                z = false;
                            }
                        }
                        end(z);
                        return 0;
                    }
                    this.readerThread = NativeThread.current();
                    do {
                        c = IOUtil.read(this.f880fd, buf, -1, f879nd);
                        if (c != -3 || !isOpen()) {
                            int normalize = IOStatus.normalize(c);
                        }
                        c = IOUtil.read(this.f880fd, buf, -1, f879nd);
                        break;
                    } while (!isOpen());
                    int normalize2 = IOStatus.normalize(c);
                    if (c <= 0) {
                        if (c != -2) {
                            z = false;
                        }
                    }
                    return normalize2;
                } finally {
                    this.readerThread = 0;
                    if (c <= 0) {
                        if (c != 65534) {
                            z = false;
                        }
                    }
                    end(z);
                }
            }
        } else {
            throw new NullPointerException();
        }
    }

    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        if (offset < 0 || length < 0 || offset > dsts.length - length) {
            throw new IndexOutOfBoundsException();
        }
        synchronized (this.readLock) {
            synchronized (this.stateLock) {
                ensureOpen();
                if (!isConnected()) {
                    throw new NotYetConnectedException();
                }
            }
            long j = 0;
            boolean z = false;
            try {
                begin();
                if (!isOpen()) {
                    this.readerThread = 0;
                    if (j > 0 || j == -2) {
                        z = true;
                    }
                    end(z);
                    return 0;
                }
                this.readerThread = NativeThread.current();
                do {
                    j = IOUtil.read(this.f880fd, dsts, offset, length, f879nd);
                    if (j != -3 || !isOpen()) {
                        long normalize = IOStatus.normalize(j);
                    }
                    j = IOUtil.read(this.f880fd, dsts, offset, length, f879nd);
                    break;
                } while (!isOpen());
                long normalize2 = IOStatus.normalize(j);
                return normalize2;
            } finally {
                this.readerThread = 0;
                if (j > 0 || j == -2) {
                    z = true;
                }
                end(z);
            }
        }
    }

    public int write(ByteBuffer buf) throws IOException {
        if (buf != null) {
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    ensureOpen();
                    if (!isConnected()) {
                        throw new NotYetConnectedException();
                    }
                }
                char c = 0;
                boolean z = true;
                try {
                    begin();
                    if (!isOpen()) {
                        this.writerThread = 0;
                        if (c <= 0) {
                            if (c != 65534) {
                                z = false;
                            }
                        }
                        end(z);
                        return 0;
                    }
                    this.writerThread = NativeThread.current();
                    do {
                        c = IOUtil.write(this.f880fd, buf, -1, f879nd);
                        if (c != -3 || !isOpen()) {
                            int normalize = IOStatus.normalize(c);
                        }
                        c = IOUtil.write(this.f880fd, buf, -1, f879nd);
                        break;
                    } while (!isOpen());
                    int normalize2 = IOStatus.normalize(c);
                    if (c <= 0) {
                        if (c != -2) {
                            z = false;
                        }
                    }
                    return normalize2;
                } finally {
                    this.writerThread = 0;
                    if (c <= 0) {
                        if (c != 65534) {
                            z = false;
                        }
                    }
                    end(z);
                }
            }
        } else {
            throw new NullPointerException();
        }
    }

    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        if (offset < 0 || length < 0 || offset > srcs.length - length) {
            throw new IndexOutOfBoundsException();
        }
        synchronized (this.writeLock) {
            synchronized (this.stateLock) {
                ensureOpen();
                if (!isConnected()) {
                    throw new NotYetConnectedException();
                }
            }
            long j = 0;
            boolean z = false;
            try {
                begin();
                if (!isOpen()) {
                    this.writerThread = 0;
                    if (j > 0 || j == -2) {
                        z = true;
                    }
                    end(z);
                    return 0;
                }
                this.writerThread = NativeThread.current();
                do {
                    j = IOUtil.write(this.f880fd, srcs, offset, length, f879nd);
                    if (j != -3 || !isOpen()) {
                        long normalize = IOStatus.normalize(j);
                    }
                    j = IOUtil.write(this.f880fd, srcs, offset, length, f879nd);
                    break;
                } while (!isOpen());
                long normalize2 = IOStatus.normalize(j);
                return normalize2;
            } finally {
                this.writerThread = 0;
                if (j > 0 || j == -2) {
                    z = true;
                }
                end(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void implConfigureBlocking(boolean block) throws IOException {
        IOUtil.configureBlocking(this.f880fd, block);
    }

    public SocketAddress localAddress() {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            inetSocketAddress = this.localAddress;
        }
        return inetSocketAddress;
    }

    public SocketAddress remoteAddress() {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            inetSocketAddress = this.remoteAddress;
        }
        return inetSocketAddress;
    }

    public DatagramChannel bind(SocketAddress local) throws IOException {
        InetSocketAddress checkAddress;
        synchronized (this.readLock) {
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    ensureOpen();
                    if (this.localAddress == null) {
                        if (local == null) {
                            checkAddress = this.family == StandardProtocolFamily.INET ? new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 0) : new InetSocketAddress(0);
                        } else {
                            checkAddress = Net.checkAddress(local);
                            if (this.family == StandardProtocolFamily.INET) {
                                if (!(checkAddress.getAddress() instanceof Inet4Address)) {
                                    throw new UnsupportedAddressTypeException();
                                }
                            }
                        }
                        SecurityManager securityManager = System.getSecurityManager();
                        if (securityManager != null) {
                            securityManager.checkListen(checkAddress.getPort());
                        }
                        Net.bind(this.family, this.f880fd, checkAddress.getAddress(), checkAddress.getPort());
                        this.localAddress = Net.localAddress(this.f880fd);
                    } else {
                        throw new AlreadyBoundException();
                    }
                }
            }
        }
        return this;
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.stateLock) {
            z = true;
            if (this.state != 1) {
                z = false;
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public void ensureOpenAndUnconnected() throws IOException {
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            } else if (this.state != 0) {
                throw new IllegalStateException("Connect already invoked");
            }
        }
    }

    public DatagramChannel connect(SocketAddress sa) throws IOException {
        synchronized (this.readLock) {
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    ensureOpenAndUnconnected();
                    InetSocketAddress checkAddress = Net.checkAddress(sa);
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkConnect(checkAddress.getAddress().getHostAddress(), checkAddress.getPort());
                    }
                    if (Net.connect(this.family, this.f880fd, checkAddress.getAddress(), checkAddress.getPort()) > 0) {
                        this.state = 1;
                        this.remoteAddress = checkAddress;
                        this.sender = checkAddress;
                        this.cachedSenderInetAddress = checkAddress.getAddress();
                        this.cachedSenderPort = checkAddress.getPort();
                        this.localAddress = Net.localAddress(this.f880fd);
                        synchronized (blockingLock()) {
                            try {
                                boolean isBlocking = isBlocking();
                                ByteBuffer allocate = ByteBuffer.allocate(1);
                                if (isBlocking) {
                                    configureBlocking(false);
                                }
                                do {
                                    allocate.clear();
                                } while (receive(allocate) != null);
                                if (isBlocking) {
                                    configureBlocking(true);
                                }
                            } catch (Throwable th) {
                                if (0 != 0) {
                                    configureBlocking(true);
                                }
                                throw th;
                            }
                        }
                    } else {
                        throw new Error();
                    }
                }
            }
        }
        return this;
    }

    public DatagramChannel disconnect() throws IOException {
        synchronized (this.readLock) {
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    if (isConnected()) {
                        if (isOpen()) {
                            InetSocketAddress inetSocketAddress = this.remoteAddress;
                            SecurityManager securityManager = System.getSecurityManager();
                            if (securityManager != null) {
                                securityManager.checkConnect(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
                            }
                            disconnect0(this.f880fd, this.family == StandardProtocolFamily.INET6);
                            this.remoteAddress = null;
                            this.state = 0;
                            this.localAddress = Net.localAddress(this.f880fd);
                            return this;
                        }
                    }
                    return this;
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: sun.nio.ch.MembershipKeyImpl$Type4} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v12, resolved type: sun.nio.ch.MembershipKeyImpl$Type4} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v21, resolved type: sun.nio.ch.MembershipKeyImpl$Type6} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v15, resolved type: sun.nio.ch.MembershipKeyImpl$Type4} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.nio.channels.MembershipKey innerJoin(java.net.InetAddress r21, java.net.NetworkInterface r22, java.net.InetAddress r23) throws java.p026io.IOException {
        /*
            r20 = this;
            r9 = r20
            r10 = r21
            r11 = r23
            boolean r0 = r21.isMulticastAddress()
            if (r0 == 0) goto L_0x0161
            boolean r0 = r10 instanceof java.net.Inet4Address
            if (r0 == 0) goto L_0x0025
            java.net.ProtocolFamily r0 = r9.family
            java.net.StandardProtocolFamily r1 = java.net.StandardProtocolFamily.INET6
            if (r0 != r1) goto L_0x002f
            boolean r0 = sun.nio.p033ch.Net.canIPv6SocketJoinIPv4Group()
            if (r0 == 0) goto L_0x001d
            goto L_0x002f
        L_0x001d:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "IPv6 socket cannot join IPv4 multicast group"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0025:
            boolean r0 = r10 instanceof java.net.Inet6Address
            if (r0 == 0) goto L_0x0157
            java.net.ProtocolFamily r0 = r9.family
            java.net.StandardProtocolFamily r1 = java.net.StandardProtocolFamily.INET6
            if (r0 != r1) goto L_0x014d
        L_0x002f:
            if (r11 == 0) goto L_0x0060
            boolean r0 = r23.isAnyLocalAddress()
            if (r0 != 0) goto L_0x0058
            boolean r0 = r23.isMulticastAddress()
            if (r0 != 0) goto L_0x0050
            java.lang.Class r0 = r23.getClass()
            java.lang.Class r1 = r21.getClass()
            if (r0 != r1) goto L_0x0048
            goto L_0x0060
        L_0x0048:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Source address is different type to group"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0050:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Source address is multicast address"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0058:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Source address is a wildcard address"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0060:
            java.lang.SecurityManager r12 = java.lang.System.getSecurityManager()
            if (r12 == 0) goto L_0x0069
            r12.checkMulticast(r10)
        L_0x0069:
            java.lang.Object r13 = r9.stateLock
            monitor-enter(r13)
            boolean r0 = r20.isOpen()     // Catch:{ all -> 0x0146 }
            if (r0 == 0) goto L_0x013e
            sun.nio.ch.MembershipRegistry r0 = r9.registry     // Catch:{ all -> 0x0146 }
            if (r0 != 0) goto L_0x0080
            sun.nio.ch.MembershipRegistry r0 = new sun.nio.ch.MembershipRegistry     // Catch:{ all -> 0x0146 }
            r0.<init>()     // Catch:{ all -> 0x0146 }
            r9.registry = r0     // Catch:{ all -> 0x0146 }
            r14 = r22
            goto L_0x008a
        L_0x0080:
            r14 = r22
            java.nio.channels.MembershipKey r0 = r0.checkMembership(r10, r14, r11)     // Catch:{ all -> 0x014b }
            if (r0 == 0) goto L_0x008a
            monitor-exit(r13)     // Catch:{ all -> 0x014b }
            return r0
        L_0x008a:
            java.net.ProtocolFamily r0 = r9.family     // Catch:{ all -> 0x014b }
            java.net.StandardProtocolFamily r1 = java.net.StandardProtocolFamily.INET6     // Catch:{ all -> 0x014b }
            r2 = -2
            if (r0 != r1) goto L_0x00e4
            boolean r0 = r10 instanceof java.net.Inet6Address     // Catch:{ all -> 0x014b }
            if (r0 != 0) goto L_0x009b
            boolean r0 = sun.nio.p033ch.Net.canJoin6WithIPv4Group()     // Catch:{ all -> 0x014b }
            if (r0 == 0) goto L_0x00e4
        L_0x009b:
            int r0 = r22.getIndex()     // Catch:{ all -> 0x014b }
            r1 = -1
            if (r0 == r1) goto L_0x00dc
            byte[] r1 = sun.nio.p033ch.Net.inet6AsByteArray(r21)     // Catch:{ all -> 0x014b }
            r15 = r1
            if (r11 != 0) goto L_0x00ac
            r1 = 0
            goto L_0x00b0
        L_0x00ac:
            byte[] r1 = sun.nio.p033ch.Net.inet6AsByteArray(r23)     // Catch:{ all -> 0x014b }
        L_0x00b0:
            r8 = r1
            java.io.FileDescriptor r1 = r9.f880fd     // Catch:{ all -> 0x014b }
            int r1 = sun.nio.p033ch.Net.join6(r1, r15, r0, r8)     // Catch:{ all -> 0x014b }
            r7 = r1
            if (r7 == r2) goto L_0x00d2
            sun.nio.ch.MembershipKeyImpl$Type6 r16 = new sun.nio.ch.MembershipKeyImpl$Type6     // Catch:{ all -> 0x014b }
            r1 = r16
            r2 = r20
            r3 = r21
            r4 = r22
            r5 = r23
            r6 = r15
            r17 = r7
            r7 = r0
            r18 = r8
            r1.<init>(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x014b }
            r0 = r16
            goto L_0x0123
        L_0x00d2:
            r17 = r7
            r18 = r8
            java.lang.UnsupportedOperationException r1 = new java.lang.UnsupportedOperationException     // Catch:{ all -> 0x014b }
            r1.<init>()     // Catch:{ all -> 0x014b }
            throw r1     // Catch:{ all -> 0x014b }
        L_0x00dc:
            java.io.IOException r1 = new java.io.IOException     // Catch:{ all -> 0x014b }
            java.lang.String r2 = "Network interface cannot be identified"
            r1.<init>((java.lang.String) r2)     // Catch:{ all -> 0x014b }
            throw r1     // Catch:{ all -> 0x014b }
        L_0x00e4:
            java.net.Inet4Address r0 = sun.nio.p033ch.Net.anyInet4Address(r22)     // Catch:{ all -> 0x014b }
            if (r0 == 0) goto L_0x0136
            int r1 = sun.nio.p033ch.Net.inet4AsInt(r21)     // Catch:{ all -> 0x014b }
            r15 = r1
            int r1 = sun.nio.p033ch.Net.inet4AsInt(r0)     // Catch:{ all -> 0x014b }
            r8 = r1
            if (r11 != 0) goto L_0x00f9
            r1 = 0
            goto L_0x00fd
        L_0x00f9:
            int r1 = sun.nio.p033ch.Net.inet4AsInt(r23)     // Catch:{ all -> 0x014b }
        L_0x00fd:
            r7 = r1
            java.io.FileDescriptor r1 = r9.f880fd     // Catch:{ all -> 0x014b }
            int r1 = sun.nio.p033ch.Net.join4(r1, r15, r8, r7)     // Catch:{ all -> 0x014b }
            r6 = r1
            if (r6 == r2) goto L_0x012a
            sun.nio.ch.MembershipKeyImpl$Type4 r16 = new sun.nio.ch.MembershipKeyImpl$Type4     // Catch:{ all -> 0x014b }
            r1 = r16
            r2 = r20
            r3 = r21
            r4 = r22
            r5 = r23
            r17 = r6
            r6 = r15
            r18 = r7
            r7 = r8
            r19 = r8
            r8 = r18
            r1.<init>(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x014b }
            r1 = r16
            r0 = r1
        L_0x0123:
            sun.nio.ch.MembershipRegistry r1 = r9.registry     // Catch:{ all -> 0x014b }
            r1.add(r0)     // Catch:{ all -> 0x014b }
            monitor-exit(r13)     // Catch:{ all -> 0x014b }
            return r0
        L_0x012a:
            r17 = r6
            r18 = r7
            r19 = r8
            java.lang.UnsupportedOperationException r1 = new java.lang.UnsupportedOperationException     // Catch:{ all -> 0x014b }
            r1.<init>()     // Catch:{ all -> 0x014b }
            throw r1     // Catch:{ all -> 0x014b }
        L_0x0136:
            java.io.IOException r1 = new java.io.IOException     // Catch:{ all -> 0x014b }
            java.lang.String r2 = "Network interface not configured for IPv4"
            r1.<init>((java.lang.String) r2)     // Catch:{ all -> 0x014b }
            throw r1     // Catch:{ all -> 0x014b }
        L_0x013e:
            r14 = r22
            java.nio.channels.ClosedChannelException r0 = new java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x014b }
            r0.<init>()     // Catch:{ all -> 0x014b }
            throw r0     // Catch:{ all -> 0x014b }
        L_0x0146:
            r0 = move-exception
            r14 = r22
        L_0x0149:
            monitor-exit(r13)     // Catch:{ all -> 0x014b }
            throw r0
        L_0x014b:
            r0 = move-exception
            goto L_0x0149
        L_0x014d:
            r14 = r22
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Only IPv6 sockets can join IPv6 multicast group"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0157:
            r14 = r22
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Address type not supported"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0161:
            r14 = r22
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Group not a multicast address"
            r0.<init>((java.lang.String) r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.DatagramChannelImpl.innerJoin(java.net.InetAddress, java.net.NetworkInterface, java.net.InetAddress):java.nio.channels.MembershipKey");
    }

    public MembershipKey join(InetAddress group, NetworkInterface interf) throws IOException {
        return innerJoin(group, interf, (InetAddress) null);
    }

    public MembershipKey join(InetAddress group, NetworkInterface interf, InetAddress source) throws IOException {
        if (source != null) {
            return innerJoin(group, interf, source);
        }
        throw new NullPointerException("source address is null");
    }

    /* access modifiers changed from: package-private */
    public void drop(MembershipKeyImpl key) {
        synchronized (this.stateLock) {
            if (key.isValid()) {
                try {
                    if (key instanceof MembershipKeyImpl.Type6) {
                        MembershipKeyImpl.Type6 type6 = (MembershipKeyImpl.Type6) key;
                        Net.drop6(this.f880fd, type6.groupAddress(), type6.index(), type6.source());
                    } else {
                        MembershipKeyImpl.Type4 type4 = (MembershipKeyImpl.Type4) key;
                        Net.drop4(this.f880fd, type4.groupAddress(), type4.interfaceAddress(), type4.source());
                    }
                    key.invalidate();
                    this.registry.remove(key);
                } catch (IOException e) {
                    throw new AssertionError((Object) e);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void block(MembershipKeyImpl key, InetAddress source) throws IOException {
        int i;
        synchronized (this.stateLock) {
            if (!key.isValid()) {
                throw new IllegalStateException("key is no longer valid");
            } else if (source.isAnyLocalAddress()) {
                throw new IllegalArgumentException("Source address is a wildcard address");
            } else if (source.isMulticastAddress()) {
                throw new IllegalArgumentException("Source address is multicast address");
            } else if (source.getClass() == key.group().getClass()) {
                if (key instanceof MembershipKeyImpl.Type6) {
                    MembershipKeyImpl.Type6 type6 = (MembershipKeyImpl.Type6) key;
                    i = Net.block6(this.f880fd, type6.groupAddress(), type6.index(), Net.inet6AsByteArray(source));
                } else {
                    MembershipKeyImpl.Type4 type4 = (MembershipKeyImpl.Type4) key;
                    i = Net.block4(this.f880fd, type4.groupAddress(), type4.interfaceAddress(), Net.inet4AsInt(source));
                }
                if (i == -2) {
                    throw new UnsupportedOperationException();
                }
            } else {
                throw new IllegalArgumentException("Source address is different type to group");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void unblock(MembershipKeyImpl key, InetAddress source) {
        synchronized (this.stateLock) {
            if (key.isValid()) {
                try {
                    if (key instanceof MembershipKeyImpl.Type6) {
                        MembershipKeyImpl.Type6 type6 = (MembershipKeyImpl.Type6) key;
                        Net.unblock6(this.f880fd, type6.groupAddress(), type6.index(), Net.inet6AsByteArray(source));
                    } else {
                        MembershipKeyImpl.Type4 type4 = (MembershipKeyImpl.Type4) key;
                        Net.unblock4(this.f880fd, type4.groupAddress(), type4.interfaceAddress(), Net.inet4AsInt(source));
                    }
                } catch (IOException e) {
                    throw new AssertionError((Object) e);
                }
            } else {
                throw new IllegalStateException("key is no longer valid");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void implCloseSelectableChannel() throws IOException {
        synchronized (this.stateLock) {
            this.guard.close();
            if (this.state != 2) {
                f879nd.preClose(this.f880fd);
            }
            ResourceManager.afterUdpClose();
            MembershipRegistry membershipRegistry = this.registry;
            if (membershipRegistry != null) {
                membershipRegistry.invalidateAll();
            }
            long j = this.readerThread;
            long j2 = j;
            if (j != 0) {
                NativeThread.signal(j2);
            }
            long j3 = this.writerThread;
            long j4 = j3;
            if (j3 != 0) {
                NativeThread.signal(j4);
            }
            if (!isRegistered()) {
                kill();
            }
        }
    }

    public void kill() throws IOException {
        synchronized (this.stateLock) {
            int i = this.state;
            if (i != 2) {
                if (i == -1) {
                    this.state = 2;
                    return;
                }
                f879nd.close(this.f880fd);
                this.state = 2;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            CloseGuard closeGuard = this.guard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            if (this.f880fd != null) {
                close();
            }
        } finally {
            super.finalize();
        }
    }

    public boolean translateReadyOps(int ops, int initialOps, SelectionKeyImpl sk) {
        int nioInterestOps = sk.nioInterestOps();
        int nioReadyOps = sk.nioReadyOps();
        int i = initialOps;
        if ((Net.POLLNVAL & ops) != 0) {
            return false;
        }
        if (((Net.POLLERR | Net.POLLHUP) & ops) != 0) {
            int i2 = nioInterestOps;
            sk.nioReadyOps(i2);
            if (((~nioReadyOps) & i2) != 0) {
                return true;
            }
            return false;
        }
        if (!((Net.POLLIN & ops) == 0 || (nioInterestOps & 1) == 0)) {
            i |= 1;
        }
        if (!((Net.POLLOUT & ops) == 0 || (nioInterestOps & 4) == 0)) {
            i |= 4;
        }
        sk.nioReadyOps(i);
        if (((~nioReadyOps) & i) != 0) {
            return true;
        }
        return false;
    }

    public boolean translateAndUpdateReadyOps(int ops, SelectionKeyImpl sk) {
        return translateReadyOps(ops, sk.nioReadyOps(), sk);
    }

    public boolean translateAndSetReadyOps(int ops, SelectionKeyImpl sk) {
        return translateReadyOps(ops, 0, sk);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r9.readerThread = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0018, code lost:
        if (0 <= 0) goto L_0x001b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001b, code lost:
        r2 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001c, code lost:
        end(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0020, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002e, code lost:
        r1 = sun.nio.p033ch.Net.poll(r9.f880fd, r10, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r9.readerThread = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0031, code lost:
        if (r1 <= 0) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0034, code lost:
        r2 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0035, code lost:
        end(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x003a, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int poll(int r10, long r11) throws java.p026io.IOException {
        /*
            r9 = this;
            java.lang.Object r0 = r9.readLock
            monitor-enter(r0)
            r1 = 0
            r2 = 1
            r3 = 0
            r5 = 0
            r9.begin()     // Catch:{ all -> 0x003e }
            java.lang.Object r6 = r9.stateLock     // Catch:{ all -> 0x003e }
            monitor-enter(r6)     // Catch:{ all -> 0x003e }
            boolean r7 = r9.isOpen()     // Catch:{ all -> 0x003b }
            if (r7 != 0) goto L_0x0021
            monitor-exit(r6)     // Catch:{ all -> 0x003b }
            r9.readerThread = r3     // Catch:{ all -> 0x0049 }
            if (r1 <= 0) goto L_0x001b
            goto L_0x001c
        L_0x001b:
            r2 = r5
        L_0x001c:
            r9.end(r2)     // Catch:{ all -> 0x0049 }
            monitor-exit(r0)     // Catch:{ all -> 0x0049 }
            return r5
        L_0x0021:
            long r7 = sun.nio.p033ch.NativeThread.current()     // Catch:{ all -> 0x003b }
            r9.readerThread = r7     // Catch:{ all -> 0x003b }
            monitor-exit(r6)     // Catch:{ all -> 0x003b }
            java.io.FileDescriptor r6 = r9.f880fd     // Catch:{ all -> 0x003e }
            int r6 = sun.nio.p033ch.Net.poll(r6, r10, r11)     // Catch:{ all -> 0x003e }
            r1 = r6
            r9.readerThread = r3     // Catch:{ all -> 0x0049 }
            if (r1 <= 0) goto L_0x0034
            goto L_0x0035
        L_0x0034:
            r2 = r5
        L_0x0035:
            r9.end(r2)     // Catch:{ all -> 0x0049 }
            monitor-exit(r0)     // Catch:{ all -> 0x0049 }
            return r1
        L_0x003b:
            r7 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x003b }
            throw r7     // Catch:{ all -> 0x003e }
        L_0x003e:
            r6 = move-exception
            r9.readerThread = r3     // Catch:{ all -> 0x0049 }
            if (r1 <= 0) goto L_0x0044
            goto L_0x0045
        L_0x0044:
            r2 = r5
        L_0x0045:
            r9.end(r2)     // Catch:{ all -> 0x0049 }
            throw r6     // Catch:{ all -> 0x0049 }
        L_0x0049:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0049 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.DatagramChannelImpl.poll(int, long):int");
    }

    public void translateAndSetInterestOps(int ops, SelectionKeyImpl sk) {
        short s = 0;
        if ((ops & 1) != 0) {
            s = 0 | Net.POLLIN;
        }
        if ((ops & 4) != 0) {
            s |= Net.POLLOUT;
        }
        if ((ops & 8) != 0) {
            s |= Net.POLLIN;
        }
        sk.selector.putEventOps(sk, s);
    }

    public FileDescriptor getFD() {
        return this.f880fd;
    }

    public int getFDVal() {
        return this.fdVal;
    }
}
