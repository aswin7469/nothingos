package java.net;

import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.Set;

public abstract class DatagramSocketImpl implements SocketOptions {
    private static final Set<SocketOption<?>> dgSocketOptions = Set.m1760of(StandardSocketOptions.SO_SNDBUF, StandardSocketOptions.SO_RCVBUF, StandardSocketOptions.SO_REUSEADDR, StandardSocketOptions.IP_TOS);
    private static final Set<SocketOption<?>> mcSocketOptions = Set.m1763of(StandardSocketOptions.SO_SNDBUF, StandardSocketOptions.SO_RCVBUF, StandardSocketOptions.SO_REUSEADDR, StandardSocketOptions.IP_TOS, StandardSocketOptions.IP_MULTICAST_IF, StandardSocketOptions.IP_MULTICAST_TTL, StandardSocketOptions.IP_MULTICAST_LOOP);

    /* renamed from: fd */
    protected FileDescriptor f554fd;
    protected int localPort;
    DatagramSocket socket;

    /* access modifiers changed from: protected */
    public abstract void bind(int i, InetAddress inetAddress) throws SocketException;

    /* access modifiers changed from: protected */
    public abstract void close();

    /* access modifiers changed from: protected */
    public void connect(InetAddress inetAddress, int i) throws SocketException {
    }

    /* access modifiers changed from: protected */
    public abstract void create() throws SocketException;

    /* access modifiers changed from: package-private */
    public int dataAvailable() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void disconnect() {
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public abstract byte getTTL() throws IOException;

    /* access modifiers changed from: protected */
    public abstract int getTimeToLive() throws IOException;

    /* access modifiers changed from: protected */
    public abstract void join(InetAddress inetAddress) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void joinGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void leave(InetAddress inetAddress) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void leaveGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException;

    /* access modifiers changed from: protected */
    public abstract int peek(InetAddress inetAddress) throws IOException;

    /* access modifiers changed from: protected */
    public abstract int peekData(DatagramPacket datagramPacket) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void receive(DatagramPacket datagramPacket) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void send(DatagramPacket datagramPacket) throws IOException;

    /* access modifiers changed from: protected */
    @Deprecated
    public abstract void setTTL(byte b) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void setTimeToLive(int i) throws IOException;

    /* access modifiers changed from: package-private */
    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.socket = datagramSocket;
    }

    /* access modifiers changed from: package-private */
    public DatagramSocket getDatagramSocket() {
        return this.socket;
    }

    /* access modifiers changed from: protected */
    public int getLocalPort() {
        return this.localPort;
    }

    /* access modifiers changed from: protected */
    public FileDescriptor getFileDescriptor() {
        return this.f554fd;
    }

    /* access modifiers changed from: protected */
    public <T> void setOption(SocketOption<T> socketOption, T t) throws IOException {
        if (socketOption == StandardSocketOptions.SO_SNDBUF) {
            setOption(4097, t);
        } else if (socketOption == StandardSocketOptions.SO_RCVBUF) {
            setOption(4098, t);
        } else if (socketOption == StandardSocketOptions.SO_REUSEADDR) {
            setOption(4, t);
        } else if (socketOption == StandardSocketOptions.SO_REUSEPORT && supportedOptions().contains(socketOption)) {
            setOption(14, t);
        } else if (socketOption == StandardSocketOptions.IP_TOS) {
            setOption(3, t);
        } else if (socketOption == StandardSocketOptions.IP_MULTICAST_IF && (getDatagramSocket() instanceof MulticastSocket)) {
            setOption(31, t);
        } else if (socketOption != StandardSocketOptions.IP_MULTICAST_TTL || !(getDatagramSocket() instanceof MulticastSocket)) {
            if (socketOption != StandardSocketOptions.IP_MULTICAST_LOOP || !(getDatagramSocket() instanceof MulticastSocket)) {
                throw new UnsupportedOperationException("unsupported option");
            }
            setOption(18, t);
        } else if (t instanceof Integer) {
            setTimeToLive(((Integer) t).intValue());
        } else {
            throw new IllegalArgumentException("not an integer");
        }
    }

    /* access modifiers changed from: protected */
    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (socketOption == StandardSocketOptions.SO_SNDBUF) {
            return getOption(4097);
        }
        if (socketOption == StandardSocketOptions.SO_RCVBUF) {
            return getOption(4098);
        }
        if (socketOption == StandardSocketOptions.SO_REUSEADDR) {
            return getOption(4);
        }
        if (socketOption == StandardSocketOptions.SO_REUSEPORT && supportedOptions().contains(socketOption)) {
            return getOption(14);
        }
        if (socketOption == StandardSocketOptions.IP_TOS) {
            return getOption(3);
        }
        if (socketOption == StandardSocketOptions.IP_MULTICAST_IF && (getDatagramSocket() instanceof MulticastSocket)) {
            return getOption(31);
        }
        if (socketOption == StandardSocketOptions.IP_MULTICAST_TTL && (getDatagramSocket() instanceof MulticastSocket)) {
            return Integer.valueOf(getTimeToLive());
        }
        if (socketOption == StandardSocketOptions.IP_MULTICAST_LOOP && (getDatagramSocket() instanceof MulticastSocket)) {
            return getOption(18);
        }
        throw new UnsupportedOperationException("unsupported option");
    }

    /* access modifiers changed from: protected */
    public Set<SocketOption<?>> supportedOptions() {
        if (getDatagramSocket() instanceof MulticastSocket) {
            return mcSocketOptions;
        }
        return dgSocketOptions;
    }
}
