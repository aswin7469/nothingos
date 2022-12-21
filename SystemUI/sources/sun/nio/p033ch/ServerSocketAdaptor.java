package sun.nio.p033ch;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.ServerSocketAdaptor */
public class ServerSocketAdaptor extends ServerSocket {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final ServerSocketChannelImpl ssc;
    private volatile int timeout = 0;

    public static ServerSocket create(ServerSocketChannelImpl serverSocketChannelImpl) {
        try {
            return new ServerSocketAdaptor(serverSocketChannelImpl);
        } catch (IOException e) {
            throw new Error((Throwable) e);
        }
    }

    private ServerSocketAdaptor(ServerSocketChannelImpl serverSocketChannelImpl) throws IOException {
        this.ssc = serverSocketChannelImpl;
    }

    public void bind(SocketAddress socketAddress) throws IOException {
        bind(socketAddress, 50);
    }

    public void bind(SocketAddress socketAddress, int i) throws IOException {
        if (socketAddress == null) {
            socketAddress = new InetSocketAddress(0);
        }
        try {
            this.ssc.bind(socketAddress, i);
        } catch (Exception e) {
            Net.translateException(e);
        }
    }

    public InetAddress getInetAddress() {
        if (!this.ssc.isBound()) {
            return null;
        }
        return Net.getRevealedLocalAddress(this.ssc.localAddress()).getAddress();
    }

    public int getLocalPort() {
        if (!this.ssc.isBound()) {
            return -1;
        }
        return Net.asInetSocketAddress(this.ssc.localAddress()).getPort();
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:30:0x0050=Splitter:B:30:0x0050, B:46:0x0084=Splitter:B:46:0x0084} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.net.Socket accept() throws java.p026io.IOException {
        /*
            r8 = this;
            sun.nio.ch.ServerSocketChannelImpl r0 = r8.ssc
            java.lang.Object r0 = r0.blockingLock()
            monitor-enter(r0)
            sun.nio.ch.ServerSocketChannelImpl r1 = r8.ssc     // Catch:{ all -> 0x00bb }
            boolean r1 = r1.isBound()     // Catch:{ all -> 0x00bb }
            if (r1 == 0) goto L_0x00b5
            int r1 = r8.timeout     // Catch:{ Exception -> 0x00ae }
            if (r1 != 0) goto L_0x0030
            sun.nio.ch.ServerSocketChannelImpl r1 = r8.ssc     // Catch:{ Exception -> 0x00ae }
            java.nio.channels.SocketChannel r1 = r1.accept()     // Catch:{ Exception -> 0x00ae }
            if (r1 != 0) goto L_0x002a
            sun.nio.ch.ServerSocketChannelImpl r8 = r8.ssc     // Catch:{ Exception -> 0x00ae }
            boolean r8 = r8.isBlocking()     // Catch:{ Exception -> 0x00ae }
            if (r8 == 0) goto L_0x0024
            goto L_0x002a
        L_0x0024:
            java.nio.channels.IllegalBlockingModeException r8 = new java.nio.channels.IllegalBlockingModeException     // Catch:{ Exception -> 0x00ae }
            r8.<init>()     // Catch:{ Exception -> 0x00ae }
            throw r8     // Catch:{ Exception -> 0x00ae }
        L_0x002a:
            java.net.Socket r8 = r1.socket()     // Catch:{ Exception -> 0x00ae }
            monitor-exit(r0)     // Catch:{ all -> 0x00bb }
            return r8
        L_0x0030:
            sun.nio.ch.ServerSocketChannelImpl r1 = r8.ssc     // Catch:{ Exception -> 0x00ae }
            r2 = 0
            r1.configureBlocking(r2)     // Catch:{ Exception -> 0x00ae }
            r1 = 1
            sun.nio.ch.ServerSocketChannelImpl r2 = r8.ssc     // Catch:{ all -> 0x009f }
            java.nio.channels.SocketChannel r2 = r2.accept()     // Catch:{ all -> 0x009f }
            if (r2 == 0) goto L_0x0052
            java.net.Socket r2 = r2.socket()     // Catch:{ all -> 0x009f }
            sun.nio.ch.ServerSocketChannelImpl r3 = r8.ssc     // Catch:{ Exception -> 0x00ae }
            boolean r3 = r3.isOpen()     // Catch:{ Exception -> 0x00ae }
            if (r3 == 0) goto L_0x0050
            sun.nio.ch.ServerSocketChannelImpl r8 = r8.ssc     // Catch:{ Exception -> 0x00ae }
            r8.configureBlocking(r1)     // Catch:{ Exception -> 0x00ae }
        L_0x0050:
            monitor-exit(r0)     // Catch:{ all -> 0x00bb }
            return r2
        L_0x0052:
            int r2 = r8.timeout     // Catch:{ all -> 0x009f }
            long r2 = (long) r2     // Catch:{ all -> 0x009f }
        L_0x0055:
            sun.nio.ch.ServerSocketChannelImpl r4 = r8.ssc     // Catch:{ all -> 0x009f }
            boolean r4 = r4.isOpen()     // Catch:{ all -> 0x009f }
            if (r4 == 0) goto L_0x0099
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x009f }
            sun.nio.ch.ServerSocketChannelImpl r6 = r8.ssc     // Catch:{ all -> 0x009f }
            short r7 = sun.nio.p033ch.Net.POLLIN     // Catch:{ all -> 0x009f }
            int r6 = r6.poll(r7, r2)     // Catch:{ all -> 0x009f }
            if (r6 <= 0) goto L_0x0086
            sun.nio.ch.ServerSocketChannelImpl r6 = r8.ssc     // Catch:{ all -> 0x009f }
            java.nio.channels.SocketChannel r6 = r6.accept()     // Catch:{ all -> 0x009f }
            if (r6 == 0) goto L_0x0086
            java.net.Socket r2 = r6.socket()     // Catch:{ all -> 0x009f }
            sun.nio.ch.ServerSocketChannelImpl r3 = r8.ssc     // Catch:{ Exception -> 0x00ae }
            boolean r3 = r3.isOpen()     // Catch:{ Exception -> 0x00ae }
            if (r3 == 0) goto L_0x0084
            sun.nio.ch.ServerSocketChannelImpl r8 = r8.ssc     // Catch:{ Exception -> 0x00ae }
            r8.configureBlocking(r1)     // Catch:{ Exception -> 0x00ae }
        L_0x0084:
            monitor-exit(r0)     // Catch:{ all -> 0x00bb }
            return r2
        L_0x0086:
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x009f }
            long r6 = r6 - r4
            long r2 = r2 - r6
            r4 = 0
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x0093
            goto L_0x0055
        L_0x0093:
            java.net.SocketTimeoutException r2 = new java.net.SocketTimeoutException     // Catch:{ all -> 0x009f }
            r2.<init>()     // Catch:{ all -> 0x009f }
            throw r2     // Catch:{ all -> 0x009f }
        L_0x0099:
            java.nio.channels.ClosedChannelException r2 = new java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x009f }
            r2.<init>()     // Catch:{ all -> 0x009f }
            throw r2     // Catch:{ all -> 0x009f }
        L_0x009f:
            r2 = move-exception
            sun.nio.ch.ServerSocketChannelImpl r3 = r8.ssc     // Catch:{ Exception -> 0x00ae }
            boolean r3 = r3.isOpen()     // Catch:{ Exception -> 0x00ae }
            if (r3 == 0) goto L_0x00ad
            sun.nio.ch.ServerSocketChannelImpl r8 = r8.ssc     // Catch:{ Exception -> 0x00ae }
            r8.configureBlocking(r1)     // Catch:{ Exception -> 0x00ae }
        L_0x00ad:
            throw r2     // Catch:{ Exception -> 0x00ae }
        L_0x00ae:
            r8 = move-exception
            sun.nio.p033ch.Net.translateException(r8)     // Catch:{ all -> 0x00bb }
            monitor-exit(r0)     // Catch:{ all -> 0x00bb }
            r8 = 0
            return r8
        L_0x00b5:
            java.nio.channels.IllegalBlockingModeException r8 = new java.nio.channels.IllegalBlockingModeException     // Catch:{ all -> 0x00bb }
            r8.<init>()     // Catch:{ all -> 0x00bb }
            throw r8     // Catch:{ all -> 0x00bb }
        L_0x00bb:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00bb }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.ServerSocketAdaptor.accept():java.net.Socket");
    }

    public void close() throws IOException {
        this.ssc.close();
    }

    public ServerSocketChannel getChannel() {
        return this.ssc;
    }

    public boolean isBound() {
        return this.ssc.isBound();
    }

    public boolean isClosed() {
        return !this.ssc.isOpen();
    }

    public void setSoTimeout(int i) throws SocketException {
        this.timeout = i;
    }

    public int getSoTimeout() throws SocketException {
        return this.timeout;
    }

    public void setReuseAddress(boolean z) throws SocketException {
        try {
            this.ssc.setOption((SocketOption) StandardSocketOptions.SO_REUSEADDR, (Object) Boolean.valueOf(z));
        } catch (IOException e) {
            Net.translateToSocketException(e);
        }
    }

    public boolean getReuseAddress() throws SocketException {
        try {
            return ((Boolean) this.ssc.getOption(StandardSocketOptions.SO_REUSEADDR)).booleanValue();
        } catch (IOException e) {
            Net.translateToSocketException(e);
            return false;
        }
    }

    public String toString() {
        if (!isBound()) {
            return "ServerSocket[unbound]";
        }
        return "ServerSocket[addr=" + getInetAddress() + ",localport=" + getLocalPort() + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public void setReceiveBufferSize(int i) throws SocketException {
        if (i > 0) {
            try {
                this.ssc.setOption((SocketOption) StandardSocketOptions.SO_RCVBUF, (Object) Integer.valueOf(i));
            } catch (IOException e) {
                Net.translateToSocketException(e);
            }
        } else {
            throw new IllegalArgumentException("size cannot be 0 or negative");
        }
    }

    public int getReceiveBufferSize() throws SocketException {
        try {
            return ((Integer) this.ssc.getOption(StandardSocketOptions.SO_RCVBUF)).intValue();
        } catch (IOException e) {
            Net.translateToSocketException(e);
            return -1;
        }
    }
}
