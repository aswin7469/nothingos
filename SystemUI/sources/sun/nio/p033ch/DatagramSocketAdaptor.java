package sun.nio.p033ch;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.DatagramSocketImpl;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.DatagramSocketAdaptor */
public class DatagramSocketAdaptor extends DatagramSocket {
    private static final DatagramSocketImpl dummyDatagramSocket = new DatagramSocketImpl() {
        /* access modifiers changed from: protected */
        public void bind(int i, InetAddress inetAddress) throws SocketException {
        }

        /* access modifiers changed from: protected */
        public void close() {
        }

        /* access modifiers changed from: protected */
        public void create() throws SocketException {
        }

        public Object getOption(int i) throws SocketException {
            return null;
        }

        /* access modifiers changed from: protected */
        @Deprecated
        public byte getTTL() throws IOException {
            return 0;
        }

        /* access modifiers changed from: protected */
        public int getTimeToLive() throws IOException {
            return 0;
        }

        /* access modifiers changed from: protected */
        public void join(InetAddress inetAddress) throws IOException {
        }

        /* access modifiers changed from: protected */
        public void joinGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException {
        }

        /* access modifiers changed from: protected */
        public void leave(InetAddress inetAddress) throws IOException {
        }

        /* access modifiers changed from: protected */
        public void leaveGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException {
        }

        /* access modifiers changed from: protected */
        public int peek(InetAddress inetAddress) throws IOException {
            return 0;
        }

        /* access modifiers changed from: protected */
        public int peekData(DatagramPacket datagramPacket) throws IOException {
            return 0;
        }

        /* access modifiers changed from: protected */
        public void receive(DatagramPacket datagramPacket) throws IOException {
        }

        /* access modifiers changed from: protected */
        public void send(DatagramPacket datagramPacket) throws IOException {
        }

        public void setOption(int i, Object obj) throws SocketException {
        }

        /* access modifiers changed from: protected */
        @Deprecated
        public void setTTL(byte b) throws IOException {
        }

        /* access modifiers changed from: protected */
        public void setTimeToLive(int i) throws IOException {
        }
    };

    /* renamed from: dc */
    private final DatagramChannelImpl f881dc;
    private volatile int timeout = 0;

    private DatagramSocketAdaptor(DatagramChannelImpl datagramChannelImpl) throws IOException {
        super(dummyDatagramSocket);
        this.f881dc = datagramChannelImpl;
    }

    public static DatagramSocket create(DatagramChannelImpl datagramChannelImpl) {
        try {
            return new DatagramSocketAdaptor(datagramChannelImpl);
        } catch (IOException e) {
            throw new Error((Throwable) e);
        }
    }

    private void connectInternal(SocketAddress socketAddress) throws SocketException {
        int port = Net.asInetSocketAddress(socketAddress).getPort();
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("connect: " + port);
        } else if (socketAddress == null) {
            throw new IllegalArgumentException("connect: null address");
        } else if (!isClosed()) {
            try {
                this.f881dc.connect(socketAddress);
            } catch (Exception e) {
                Net.translateToSocketException(e);
            }
        }
    }

    public void bind(SocketAddress socketAddress) throws SocketException {
        if (socketAddress == null) {
            try {
                socketAddress = new InetSocketAddress(0);
            } catch (Exception e) {
                Net.translateToSocketException(e);
                return;
            }
        }
        this.f881dc.bind(socketAddress);
    }

    public void connect(InetAddress inetAddress, int i) {
        try {
            connectInternal(new InetSocketAddress(inetAddress, i));
        } catch (SocketException unused) {
        }
    }

    public void connect(SocketAddress socketAddress) throws SocketException {
        if (socketAddress != null) {
            connectInternal(socketAddress);
            return;
        }
        throw new IllegalArgumentException("Address can't be null");
    }

    public void disconnect() {
        try {
            this.f881dc.disconnect();
        } catch (IOException e) {
            throw new Error((Throwable) e);
        }
    }

    public boolean isBound() {
        return this.f881dc.localAddress() != null;
    }

    public boolean isConnected() {
        return this.f881dc.remoteAddress() != null;
    }

    public InetAddress getInetAddress() {
        if (isConnected()) {
            return Net.asInetSocketAddress(this.f881dc.remoteAddress()).getAddress();
        }
        return null;
    }

    public int getPort() {
        if (isConnected()) {
            return Net.asInetSocketAddress(this.f881dc.remoteAddress()).getPort();
        }
        return -1;
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:458)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public void send(java.net.DatagramPacket r5) throws java.p026io.IOException {
        /*
            r4 = this;
            sun.nio.ch.DatagramChannelImpl r0 = r4.f881dc
            java.lang.Object r0 = r0.blockingLock()
            monitor-enter(r0)
            sun.nio.ch.DatagramChannelImpl r1 = r4.f881dc     // Catch:{ all -> 0x006e }
            boolean r1 = r1.isBlocking()     // Catch:{ all -> 0x006e }
            if (r1 == 0) goto L_0x0068
            monitor-enter(r5)     // Catch:{ IOException -> 0x0062 }
            byte[] r1 = r5.getData()     // Catch:{ all -> 0x005f }
            int r2 = r5.getOffset()     // Catch:{ all -> 0x005f }
            int r3 = r5.getLength()     // Catch:{ all -> 0x005f }
            java.nio.ByteBuffer r1 = java.nio.ByteBuffer.wrap(r1, r2, r3)     // Catch:{ all -> 0x005f }
            sun.nio.ch.DatagramChannelImpl r2 = r4.f881dc     // Catch:{ all -> 0x005f }
            boolean r2 = r2.isConnected()     // Catch:{ all -> 0x005f }
            if (r2 == 0) goto L_0x0054
            java.net.InetAddress r2 = r5.getAddress()     // Catch:{ all -> 0x005f }
            if (r2 != 0) goto L_0x004a
            sun.nio.ch.DatagramChannelImpl r2 = r4.f881dc     // Catch:{ all -> 0x005f }
            java.net.SocketAddress r2 = r2.remoteAddress()     // Catch:{ all -> 0x005f }
            java.net.InetSocketAddress r2 = (java.net.InetSocketAddress) r2     // Catch:{ all -> 0x005f }
            int r3 = r2.getPort()     // Catch:{ all -> 0x005f }
            r5.setPort(r3)     // Catch:{ all -> 0x005f }
            java.net.InetAddress r2 = r2.getAddress()     // Catch:{ all -> 0x005f }
            r5.setAddress(r2)     // Catch:{ all -> 0x005f }
            sun.nio.ch.DatagramChannelImpl r4 = r4.f881dc     // Catch:{ all -> 0x005f }
            r4.write(r1)     // Catch:{ all -> 0x005f }
            goto L_0x005d
        L_0x004a:
            sun.nio.ch.DatagramChannelImpl r4 = r4.f881dc     // Catch:{ all -> 0x005f }
            java.net.SocketAddress r2 = r5.getSocketAddress()     // Catch:{ all -> 0x005f }
            r4.send(r1, r2)     // Catch:{ all -> 0x005f }
            goto L_0x005d
        L_0x0054:
            sun.nio.ch.DatagramChannelImpl r4 = r4.f881dc     // Catch:{ all -> 0x005f }
            java.net.SocketAddress r2 = r5.getSocketAddress()     // Catch:{ all -> 0x005f }
            r4.send(r1, r2)     // Catch:{ all -> 0x005f }
        L_0x005d:
            monitor-exit(r5)     // Catch:{ all -> 0x005f }
            goto L_0x0066
        L_0x005f:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x005f }
            throw r4     // Catch:{ IOException -> 0x0062 }
        L_0x0062:
            r4 = move-exception
            sun.nio.p033ch.Net.translateException(r4)     // Catch:{ all -> 0x006e }
        L_0x0066:
            monitor-exit(r0)     // Catch:{ all -> 0x006e }
            return
        L_0x0068:
            java.nio.channels.IllegalBlockingModeException r4 = new java.nio.channels.IllegalBlockingModeException     // Catch:{ all -> 0x006e }
            r4.<init>()     // Catch:{ all -> 0x006e }
            throw r4     // Catch:{ all -> 0x006e }
        L_0x006e:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x006e }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.DatagramSocketAdaptor.send(java.net.DatagramPacket):void");
    }

    private SocketAddress receive(ByteBuffer byteBuffer) throws IOException {
        SocketAddress receive;
        if (this.timeout == 0) {
            return this.f881dc.receive(byteBuffer);
        }
        this.f881dc.configureBlocking(false);
        try {
            SocketAddress receive2 = this.f881dc.receive(byteBuffer);
            if (receive2 != null) {
                return receive2;
            }
            long j = (long) this.timeout;
            while (this.f881dc.isOpen()) {
                long currentTimeMillis = System.currentTimeMillis();
                int poll = this.f881dc.poll(Net.POLLIN, j);
                if (poll <= 0 || (poll & Net.POLLIN) == 0 || (receive = this.f881dc.receive(byteBuffer)) == null) {
                    j -= System.currentTimeMillis() - currentTimeMillis;
                    if (j <= 0) {
                        throw new SocketTimeoutException();
                    }
                } else {
                    if (this.f881dc.isOpen()) {
                        this.f881dc.configureBlocking(true);
                    }
                    return receive;
                }
            }
            throw new ClosedChannelException();
        } finally {
            if (this.f881dc.isOpen()) {
                this.f881dc.configureBlocking(true);
            }
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:458)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public void receive(java.net.DatagramPacket r5) throws java.p026io.IOException {
        /*
            r4 = this;
            sun.nio.ch.DatagramChannelImpl r0 = r4.f881dc
            java.lang.Object r0 = r0.blockingLock()
            monitor-enter(r0)
            sun.nio.ch.DatagramChannelImpl r1 = r4.f881dc     // Catch:{ all -> 0x0044 }
            boolean r1 = r1.isBlocking()     // Catch:{ all -> 0x0044 }
            if (r1 == 0) goto L_0x003e
            monitor-enter(r5)     // Catch:{ IOException -> 0x0038 }
            byte[] r1 = r5.getData()     // Catch:{ all -> 0x0035 }
            int r2 = r5.getOffset()     // Catch:{ all -> 0x0035 }
            int r3 = r5.getLength()     // Catch:{ all -> 0x0035 }
            java.nio.ByteBuffer r1 = java.nio.ByteBuffer.wrap(r1, r2, r3)     // Catch:{ all -> 0x0035 }
            java.net.SocketAddress r4 = r4.receive((java.nio.ByteBuffer) r1)     // Catch:{ all -> 0x0035 }
            r5.setSocketAddress(r4)     // Catch:{ all -> 0x0035 }
            int r4 = r1.position()     // Catch:{ all -> 0x0035 }
            int r1 = r5.getOffset()     // Catch:{ all -> 0x0035 }
            int r4 = r4 - r1
            r5.setLength(r4)     // Catch:{ all -> 0x0035 }
            monitor-exit(r5)     // Catch:{ all -> 0x0035 }
            goto L_0x003c
        L_0x0035:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0035 }
            throw r4     // Catch:{ IOException -> 0x0038 }
        L_0x0038:
            r4 = move-exception
            sun.nio.p033ch.Net.translateException(r4)     // Catch:{ all -> 0x0044 }
        L_0x003c:
            monitor-exit(r0)     // Catch:{ all -> 0x0044 }
            return
        L_0x003e:
            java.nio.channels.IllegalBlockingModeException r4 = new java.nio.channels.IllegalBlockingModeException     // Catch:{ all -> 0x0044 }
            r4.<init>()     // Catch:{ all -> 0x0044 }
            throw r4     // Catch:{ all -> 0x0044 }
        L_0x0044:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0044 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.DatagramSocketAdaptor.receive(java.net.DatagramPacket):void");
    }

    public InetAddress getLocalAddress() {
        if (isClosed()) {
            return null;
        }
        Object localAddress = this.f881dc.localAddress();
        if (localAddress == null) {
            localAddress = new InetSocketAddress(0);
        }
        InetAddress address = ((InetSocketAddress) localAddress).getAddress();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return address;
        }
        try {
            securityManager.checkConnect(address.getHostAddress(), -1);
            return address;
        } catch (SecurityException unused) {
            return new InetSocketAddress(0).getAddress();
        }
    }

    public int getLocalPort() {
        if (isClosed()) {
            return -1;
        }
        try {
            SocketAddress localAddress = this.f881dc.getLocalAddress();
            if (localAddress != null) {
                return ((InetSocketAddress) localAddress).getPort();
            }
            return 0;
        } catch (Exception unused) {
            return 0;
        }
    }

    public void setSoTimeout(int i) throws SocketException {
        this.timeout = i;
    }

    public int getSoTimeout() throws SocketException {
        return this.timeout;
    }

    private void setBooleanOption(SocketOption<Boolean> socketOption, boolean z) throws SocketException {
        try {
            this.f881dc.setOption(socketOption, Boolean.valueOf(z));
        } catch (IOException e) {
            Net.translateToSocketException(e);
        }
    }

    private void setIntOption(SocketOption<Integer> socketOption, int i) throws SocketException {
        try {
            this.f881dc.setOption(socketOption, Integer.valueOf(i));
        } catch (IOException e) {
            Net.translateToSocketException(e);
        }
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.net.SocketOption<java.lang.Boolean>, java.net.SocketOption] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean getBooleanOption(java.net.SocketOption<java.lang.Boolean> r1) throws java.net.SocketException {
        /*
            r0 = this;
            sun.nio.ch.DatagramChannelImpl r0 = r0.f881dc     // Catch:{ IOException -> 0x000d }
            java.lang.Object r0 = r0.getOption(r1)     // Catch:{ IOException -> 0x000d }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ IOException -> 0x000d }
            boolean r0 = r0.booleanValue()     // Catch:{ IOException -> 0x000d }
            return r0
        L_0x000d:
            r0 = move-exception
            sun.nio.p033ch.Net.translateToSocketException(r0)
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.DatagramSocketAdaptor.getBooleanOption(java.net.SocketOption):boolean");
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.net.SocketOption<java.lang.Integer>, java.net.SocketOption] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getIntOption(java.net.SocketOption<java.lang.Integer> r1) throws java.net.SocketException {
        /*
            r0 = this;
            sun.nio.ch.DatagramChannelImpl r0 = r0.f881dc     // Catch:{ IOException -> 0x000d }
            java.lang.Object r0 = r0.getOption(r1)     // Catch:{ IOException -> 0x000d }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ IOException -> 0x000d }
            int r0 = r0.intValue()     // Catch:{ IOException -> 0x000d }
            return r0
        L_0x000d:
            r0 = move-exception
            sun.nio.p033ch.Net.translateToSocketException(r0)
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.DatagramSocketAdaptor.getIntOption(java.net.SocketOption):int");
    }

    public void setSendBufferSize(int i) throws SocketException {
        if (i > 0) {
            setIntOption(StandardSocketOptions.SO_SNDBUF, i);
            return;
        }
        throw new IllegalArgumentException("Invalid send size");
    }

    public int getSendBufferSize() throws SocketException {
        return getIntOption(StandardSocketOptions.SO_SNDBUF);
    }

    public void setReceiveBufferSize(int i) throws SocketException {
        if (i > 0) {
            setIntOption(StandardSocketOptions.SO_RCVBUF, i);
            return;
        }
        throw new IllegalArgumentException("Invalid receive size");
    }

    public int getReceiveBufferSize() throws SocketException {
        return getIntOption(StandardSocketOptions.SO_RCVBUF);
    }

    public void setReuseAddress(boolean z) throws SocketException {
        setBooleanOption(StandardSocketOptions.SO_REUSEADDR, z);
    }

    public boolean getReuseAddress() throws SocketException {
        return getBooleanOption(StandardSocketOptions.SO_REUSEADDR);
    }

    public void setBroadcast(boolean z) throws SocketException {
        setBooleanOption(StandardSocketOptions.SO_BROADCAST, z);
    }

    public boolean getBroadcast() throws SocketException {
        return getBooleanOption(StandardSocketOptions.SO_BROADCAST);
    }

    public void setTrafficClass(int i) throws SocketException {
        setIntOption(StandardSocketOptions.IP_TOS, i);
    }

    public int getTrafficClass() throws SocketException {
        return getIntOption(StandardSocketOptions.IP_TOS);
    }

    public void close() {
        try {
            this.f881dc.close();
        } catch (IOException e) {
            throw new Error((Throwable) e);
        }
    }

    public boolean isClosed() {
        return !this.f881dc.isOpen();
    }

    public DatagramChannel getChannel() {
        return this.f881dc;
    }

    public final FileDescriptor getFileDescriptor$() {
        return this.f881dc.f880fd;
    }
}
