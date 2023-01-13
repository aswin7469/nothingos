package java.net;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.Set;

public abstract class SocketImpl implements SocketOptions {
    private static final Set<SocketOption<?>> serverSocketOptions = Set.m1759of(StandardSocketOptions.SO_RCVBUF, StandardSocketOptions.SO_REUSEADDR, StandardSocketOptions.IP_TOS);
    private static final Set<SocketOption<?>> socketOptions = Set.m1763of(StandardSocketOptions.SO_KEEPALIVE, StandardSocketOptions.SO_SNDBUF, StandardSocketOptions.SO_RCVBUF, StandardSocketOptions.SO_REUSEADDR, StandardSocketOptions.SO_LINGER, StandardSocketOptions.IP_TOS, StandardSocketOptions.TCP_NODELAY);
    protected InetAddress address;

    /* renamed from: fd */
    protected FileDescriptor f557fd;
    protected int localport;
    protected int port;
    ServerSocket serverSocket = null;
    Socket socket = null;

    /* access modifiers changed from: protected */
    public abstract void accept(SocketImpl socketImpl) throws IOException;

    /* access modifiers changed from: protected */
    public abstract int available() throws IOException;

    /* access modifiers changed from: protected */
    public abstract void bind(InetAddress inetAddress, int i) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void close() throws IOException;

    /* access modifiers changed from: protected */
    public abstract void connect(String str, int i) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void connect(InetAddress inetAddress, int i) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void connect(SocketAddress socketAddress, int i) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void create(boolean z) throws IOException;

    /* access modifiers changed from: protected */
    public abstract InputStream getInputStream() throws IOException;

    /* access modifiers changed from: protected */
    public abstract OutputStream getOutputStream() throws IOException;

    /* access modifiers changed from: protected */
    public abstract void listen(int i) throws IOException;

    /* access modifiers changed from: protected */
    public abstract void sendUrgentData(int i) throws IOException;

    /* access modifiers changed from: protected */
    public void setPerformancePreferences(int i, int i2, int i3) {
    }

    /* access modifiers changed from: protected */
    public boolean supportsUrgentData() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void shutdownInput() throws IOException {
        throw new IOException("Method not implemented!");
    }

    /* access modifiers changed from: protected */
    public void shutdownOutput() throws IOException {
        throw new IOException("Method not implemented!");
    }

    /* access modifiers changed from: protected */
    public FileDescriptor getFileDescriptor() {
        return this.f557fd;
    }

    public FileDescriptor getFD$() {
        return this.f557fd;
    }

    /* access modifiers changed from: protected */
    public InetAddress getInetAddress() {
        return this.address;
    }

    /* access modifiers changed from: protected */
    public int getPort() {
        return this.port;
    }

    /* access modifiers changed from: protected */
    public int getLocalPort() {
        return this.localport;
    }

    /* access modifiers changed from: package-private */
    public void setSocket(Socket socket2) {
        this.socket = socket2;
    }

    /* access modifiers changed from: package-private */
    public Socket getSocket() {
        return this.socket;
    }

    /* access modifiers changed from: package-private */
    public void setServerSocket(ServerSocket serverSocket2) {
        this.serverSocket = serverSocket2;
    }

    /* access modifiers changed from: package-private */
    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public String toString() {
        return "Socket[addr=" + getInetAddress() + ",port=" + getPort() + ",localport=" + getLocalPort() + NavigationBarInflaterView.SIZE_MOD_END;
    }

    /* access modifiers changed from: package-private */
    public void reset() throws IOException {
        this.address = null;
        this.port = 0;
        this.localport = 0;
    }

    /* access modifiers changed from: protected */
    public <T> void setOption(SocketOption<T> socketOption, T t) throws IOException {
        if (socketOption == StandardSocketOptions.SO_KEEPALIVE && getSocket() != null) {
            setOption(8, t);
        } else if (socketOption == StandardSocketOptions.SO_SNDBUF && getSocket() != null) {
            setOption(4097, t);
        } else if (socketOption == StandardSocketOptions.SO_RCVBUF) {
            setOption(4098, t);
        } else if (socketOption == StandardSocketOptions.SO_REUSEADDR) {
            setOption(4, t);
        } else if (socketOption == StandardSocketOptions.SO_REUSEPORT && supportedOptions().contains(socketOption)) {
            setOption(14, t);
        } else if (socketOption == StandardSocketOptions.SO_LINGER && getSocket() != null) {
            setOption(128, t);
        } else if (socketOption == StandardSocketOptions.IP_TOS) {
            setOption(3, t);
        } else if (socketOption != StandardSocketOptions.TCP_NODELAY || getSocket() == null) {
            throw new UnsupportedOperationException("unsupported option");
        } else {
            setOption(1, t);
        }
    }

    /* access modifiers changed from: protected */
    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (socketOption == StandardSocketOptions.SO_KEEPALIVE && getSocket() != null) {
            return getOption(8);
        }
        if (socketOption == StandardSocketOptions.SO_SNDBUF && getSocket() != null) {
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
        if (socketOption == StandardSocketOptions.SO_LINGER && getSocket() != null) {
            return getOption(128);
        }
        if (socketOption == StandardSocketOptions.IP_TOS) {
            return getOption(3);
        }
        if (socketOption == StandardSocketOptions.TCP_NODELAY && getSocket() != null) {
            return getOption(1);
        }
        throw new UnsupportedOperationException("unsupported option");
    }

    /* access modifiers changed from: protected */
    public Set<SocketOption<?>> supportedOptions() {
        if (getSocket() != null) {
            return socketOptions;
        }
        return serverSocketOptions;
    }
}
