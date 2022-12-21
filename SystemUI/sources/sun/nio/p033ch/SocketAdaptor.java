package sun.nio.p033ch;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/* renamed from: sun.nio.ch.SocketAdaptor */
public class SocketAdaptor extends Socket {
    /* access modifiers changed from: private */

    /* renamed from: sc */
    public final SocketChannelImpl f893sc;
    private InputStream socketInputStream = null;
    /* access modifiers changed from: private */
    public volatile int timeout = 0;

    private SocketAdaptor(SocketChannelImpl socketChannelImpl) throws SocketException {
        super((SocketImpl) new FileDescriptorHolderSocketImpl(socketChannelImpl.getFD()));
        this.f893sc = socketChannelImpl;
    }

    public static Socket create(SocketChannelImpl socketChannelImpl) {
        try {
            return new SocketAdaptor(socketChannelImpl);
        } catch (SocketException unused) {
            throw new InternalError("Should not reach here");
        }
    }

    public SocketChannel getChannel() {
        return this.f893sc;
    }

    public void connect(SocketAddress socketAddress) throws IOException {
        connect(socketAddress, 0);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x008a, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0091, code lost:
        if (r6.f893sc.isOpen() != false) goto L_0x0093;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0093, code lost:
        r6.f893sc.configureBlocking(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0098, code lost:
        throw r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0099, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
        sun.nio.p033ch.Net.translateException(r6, true);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:12:0x001d, B:30:0x0040] */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:47:0x007e */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:14:0x0020=Splitter:B:14:0x0020, B:26:0x003d=Splitter:B:26:0x003d} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(java.net.SocketAddress r7, int r8) throws java.p026io.IOException {
        /*
            r6 = this;
            if (r7 == 0) goto L_0x00b0
            if (r8 < 0) goto L_0x00a8
            sun.nio.ch.SocketChannelImpl r0 = r6.f893sc
            java.lang.Object r0 = r0.blockingLock()
            monitor-enter(r0)
            sun.nio.ch.SocketChannelImpl r1 = r6.f893sc     // Catch:{ all -> 0x00a5 }
            boolean r1 = r1.isBlocking()     // Catch:{ all -> 0x00a5 }
            if (r1 == 0) goto L_0x009f
            r1 = 1
            if (r8 != 0) goto L_0x0022
            sun.nio.ch.SocketChannelImpl r6 = r6.f893sc     // Catch:{ Exception -> 0x001c }
            r6.connect(r7)     // Catch:{ Exception -> 0x001c }
            goto L_0x0020
        L_0x001c:
            r6 = move-exception
            sun.nio.p033ch.Net.translateException(r6)     // Catch:{ Exception -> 0x0099 }
        L_0x0020:
            monitor-exit(r0)     // Catch:{ all -> 0x00a5 }
            return
        L_0x0022:
            sun.nio.ch.SocketChannelImpl r2 = r6.f893sc     // Catch:{ Exception -> 0x0099 }
            r3 = 0
            r2.configureBlocking(r3)     // Catch:{ Exception -> 0x0099 }
            sun.nio.ch.SocketChannelImpl r2 = r6.f893sc     // Catch:{ all -> 0x008a }
            boolean r7 = r2.connect(r7)     // Catch:{ all -> 0x008a }
            if (r7 == 0) goto L_0x003f
            sun.nio.ch.SocketChannelImpl r7 = r6.f893sc     // Catch:{ Exception -> 0x0099 }
            boolean r7 = r7.isOpen()     // Catch:{ Exception -> 0x0099 }
            if (r7 == 0) goto L_0x003d
            sun.nio.ch.SocketChannelImpl r6 = r6.f893sc     // Catch:{ Exception -> 0x0099 }
            r6.configureBlocking(r1)     // Catch:{ Exception -> 0x0099 }
        L_0x003d:
            monitor-exit(r0)     // Catch:{ all -> 0x00a5 }
            return
        L_0x003f:
            long r7 = (long) r8
        L_0x0040:
            sun.nio.ch.SocketChannelImpl r2 = r6.f893sc     // Catch:{ all -> 0x008a }
            boolean r2 = r2.isOpen()     // Catch:{ all -> 0x008a }
            if (r2 == 0) goto L_0x0084
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x008a }
            sun.nio.ch.SocketChannelImpl r4 = r6.f893sc     // Catch:{ all -> 0x008a }
            short r5 = sun.nio.p033ch.Net.POLLCONN     // Catch:{ all -> 0x008a }
            int r4 = r4.poll(r5, r7)     // Catch:{ all -> 0x008a }
            if (r4 <= 0) goto L_0x006c
            sun.nio.ch.SocketChannelImpl r4 = r6.f893sc     // Catch:{ all -> 0x008a }
            boolean r4 = r4.finishConnect()     // Catch:{ all -> 0x008a }
            if (r4 == 0) goto L_0x006c
            sun.nio.ch.SocketChannelImpl r7 = r6.f893sc     // Catch:{ Exception -> 0x0099 }
            boolean r7 = r7.isOpen()     // Catch:{ Exception -> 0x0099 }
            if (r7 == 0) goto L_0x009d
            sun.nio.ch.SocketChannelImpl r6 = r6.f893sc     // Catch:{ Exception -> 0x0099 }
            r6.configureBlocking(r1)     // Catch:{ Exception -> 0x0099 }
            goto L_0x009d
        L_0x006c:
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x008a }
            long r4 = r4 - r2
            long r7 = r7 - r4
            r2 = 0
            int r2 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x0079
            goto L_0x0040
        L_0x0079:
            sun.nio.ch.SocketChannelImpl r7 = r6.f893sc     // Catch:{ IOException -> 0x007e }
            r7.close()     // Catch:{ IOException -> 0x007e }
        L_0x007e:
            java.net.SocketTimeoutException r7 = new java.net.SocketTimeoutException     // Catch:{ all -> 0x008a }
            r7.<init>()     // Catch:{ all -> 0x008a }
            throw r7     // Catch:{ all -> 0x008a }
        L_0x0084:
            java.nio.channels.ClosedChannelException r7 = new java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x008a }
            r7.<init>()     // Catch:{ all -> 0x008a }
            throw r7     // Catch:{ all -> 0x008a }
        L_0x008a:
            r7 = move-exception
            sun.nio.ch.SocketChannelImpl r8 = r6.f893sc     // Catch:{ Exception -> 0x0099 }
            boolean r8 = r8.isOpen()     // Catch:{ Exception -> 0x0099 }
            if (r8 == 0) goto L_0x0098
            sun.nio.ch.SocketChannelImpl r6 = r6.f893sc     // Catch:{ Exception -> 0x0099 }
            r6.configureBlocking(r1)     // Catch:{ Exception -> 0x0099 }
        L_0x0098:
            throw r7     // Catch:{ Exception -> 0x0099 }
        L_0x0099:
            r6 = move-exception
            sun.nio.p033ch.Net.translateException(r6, r1)     // Catch:{ all -> 0x00a5 }
        L_0x009d:
            monitor-exit(r0)     // Catch:{ all -> 0x00a5 }
            return
        L_0x009f:
            java.nio.channels.IllegalBlockingModeException r6 = new java.nio.channels.IllegalBlockingModeException     // Catch:{ all -> 0x00a5 }
            r6.<init>()     // Catch:{ all -> 0x00a5 }
            throw r6     // Catch:{ all -> 0x00a5 }
        L_0x00a5:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00a5 }
            throw r6
        L_0x00a8:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "connect: timeout can't be negative"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x00b0:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "connect: The address can't be null"
            r6.<init>((java.lang.String) r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketAdaptor.connect(java.net.SocketAddress, int):void");
    }

    public void bind(SocketAddress socketAddress) throws IOException {
        try {
            this.f893sc.bind(socketAddress);
        } catch (Exception e) {
            Net.translateException(e);
        }
    }

    public InetAddress getInetAddress() {
        SocketAddress remoteAddress;
        if (isConnected() && (remoteAddress = this.f893sc.remoteAddress()) != null) {
            return ((InetSocketAddress) remoteAddress).getAddress();
        }
        return null;
    }

    public InetAddress getLocalAddress() {
        InetSocketAddress localAddress;
        if (!this.f893sc.isOpen() || (localAddress = this.f893sc.localAddress()) == null) {
            return new InetSocketAddress(0).getAddress();
        }
        return Net.getRevealedLocalAddress(localAddress).getAddress();
    }

    public int getPort() {
        SocketAddress remoteAddress;
        if (isConnected() && (remoteAddress = this.f893sc.remoteAddress()) != null) {
            return ((InetSocketAddress) remoteAddress).getPort();
        }
        return 0;
    }

    public int getLocalPort() {
        InetSocketAddress localAddress = this.f893sc.localAddress();
        if (localAddress == null) {
            return -1;
        }
        InetSocketAddress inetSocketAddress = localAddress;
        return localAddress.getPort();
    }

    /* renamed from: sun.nio.ch.SocketAdaptor$SocketInputStream */
    private class SocketInputStream extends ChannelInputStream {
        private SocketInputStream() {
            super(SocketAdaptor.this.f893sc);
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0058, code lost:
            return r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x00a0, code lost:
            return r6;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int read(java.nio.ByteBuffer r9) throws java.p026io.IOException {
            /*
                r8 = this;
                sun.nio.ch.SocketAdaptor r0 = sun.nio.p033ch.SocketAdaptor.this
                sun.nio.ch.SocketChannelImpl r0 = r0.f893sc
                java.lang.Object r0 = r0.blockingLock()
                monitor-enter(r0)
                sun.nio.ch.SocketAdaptor r1 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00d7 }
                sun.nio.ch.SocketChannelImpl r1 = r1.f893sc     // Catch:{ all -> 0x00d7 }
                boolean r1 = r1.isBlocking()     // Catch:{ all -> 0x00d7 }
                if (r1 == 0) goto L_0x00d1
                sun.nio.ch.SocketAdaptor r1 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00d7 }
                int r1 = r1.timeout     // Catch:{ all -> 0x00d7 }
                if (r1 != 0) goto L_0x002b
                sun.nio.ch.SocketAdaptor r8 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00d7 }
                sun.nio.ch.SocketChannelImpl r8 = r8.f893sc     // Catch:{ all -> 0x00d7 }
                int r8 = r8.read(r9)     // Catch:{ all -> 0x00d7 }
                monitor-exit(r0)     // Catch:{ all -> 0x00d7 }
                return r8
            L_0x002b:
                sun.nio.ch.SocketAdaptor r1 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00d7 }
                sun.nio.ch.SocketChannelImpl r1 = r1.f893sc     // Catch:{ all -> 0x00d7 }
                r2 = 0
                r1.configureBlocking(r2)     // Catch:{ all -> 0x00d7 }
                r1 = 1
                sun.nio.ch.SocketAdaptor r2 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00ba }
                sun.nio.ch.SocketChannelImpl r2 = r2.f893sc     // Catch:{ all -> 0x00ba }
                int r2 = r2.read(r9)     // Catch:{ all -> 0x00ba }
                if (r2 == 0) goto L_0x0059
                sun.nio.ch.SocketAdaptor r9 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00d7 }
                sun.nio.ch.SocketChannelImpl r9 = r9.f893sc     // Catch:{ all -> 0x00d7 }
                boolean r9 = r9.isOpen()     // Catch:{ all -> 0x00d7 }
                if (r9 == 0) goto L_0x0057
                sun.nio.ch.SocketAdaptor r8 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00d7 }
                sun.nio.ch.SocketChannelImpl r8 = r8.f893sc     // Catch:{ all -> 0x00d7 }
                r8.configureBlocking(r1)     // Catch:{ all -> 0x00d7 }
            L_0x0057:
                monitor-exit(r0)     // Catch:{ all -> 0x00d7 }
                return r2
            L_0x0059:
                sun.nio.ch.SocketAdaptor r2 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00ba }
                int r2 = r2.timeout     // Catch:{ all -> 0x00ba }
                long r2 = (long) r2     // Catch:{ all -> 0x00ba }
            L_0x0060:
                sun.nio.ch.SocketAdaptor r4 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00ba }
                sun.nio.ch.SocketChannelImpl r4 = r4.f893sc     // Catch:{ all -> 0x00ba }
                boolean r4 = r4.isOpen()     // Catch:{ all -> 0x00ba }
                if (r4 == 0) goto L_0x00b4
                long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00ba }
                sun.nio.ch.SocketAdaptor r6 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00ba }
                sun.nio.ch.SocketChannelImpl r6 = r6.f893sc     // Catch:{ all -> 0x00ba }
                short r7 = sun.nio.p033ch.Net.POLLIN     // Catch:{ all -> 0x00ba }
                int r6 = r6.poll(r7, r2)     // Catch:{ all -> 0x00ba }
                if (r6 <= 0) goto L_0x00a1
                sun.nio.ch.SocketAdaptor r6 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00ba }
                sun.nio.ch.SocketChannelImpl r6 = r6.f893sc     // Catch:{ all -> 0x00ba }
                int r6 = r6.read(r9)     // Catch:{ all -> 0x00ba }
                if (r6 == 0) goto L_0x00a1
                sun.nio.ch.SocketAdaptor r9 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00d7 }
                sun.nio.ch.SocketChannelImpl r9 = r9.f893sc     // Catch:{ all -> 0x00d7 }
                boolean r9 = r9.isOpen()     // Catch:{ all -> 0x00d7 }
                if (r9 == 0) goto L_0x009f
                sun.nio.ch.SocketAdaptor r8 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00d7 }
                sun.nio.ch.SocketChannelImpl r8 = r8.f893sc     // Catch:{ all -> 0x00d7 }
                r8.configureBlocking(r1)     // Catch:{ all -> 0x00d7 }
            L_0x009f:
                monitor-exit(r0)     // Catch:{ all -> 0x00d7 }
                return r6
            L_0x00a1:
                long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00ba }
                long r6 = r6 - r4
                long r2 = r2 - r6
                r4 = 0
                int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r4 <= 0) goto L_0x00ae
                goto L_0x0060
            L_0x00ae:
                java.net.SocketTimeoutException r9 = new java.net.SocketTimeoutException     // Catch:{ all -> 0x00ba }
                r9.<init>()     // Catch:{ all -> 0x00ba }
                throw r9     // Catch:{ all -> 0x00ba }
            L_0x00b4:
                java.nio.channels.ClosedChannelException r9 = new java.nio.channels.ClosedChannelException     // Catch:{ all -> 0x00ba }
                r9.<init>()     // Catch:{ all -> 0x00ba }
                throw r9     // Catch:{ all -> 0x00ba }
            L_0x00ba:
                r9 = move-exception
                sun.nio.ch.SocketAdaptor r2 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00d7 }
                sun.nio.ch.SocketChannelImpl r2 = r2.f893sc     // Catch:{ all -> 0x00d7 }
                boolean r2 = r2.isOpen()     // Catch:{ all -> 0x00d7 }
                if (r2 == 0) goto L_0x00d0
                sun.nio.ch.SocketAdaptor r8 = sun.nio.p033ch.SocketAdaptor.this     // Catch:{ all -> 0x00d7 }
                sun.nio.ch.SocketChannelImpl r8 = r8.f893sc     // Catch:{ all -> 0x00d7 }
                r8.configureBlocking(r1)     // Catch:{ all -> 0x00d7 }
            L_0x00d0:
                throw r9     // Catch:{ all -> 0x00d7 }
            L_0x00d1:
                java.nio.channels.IllegalBlockingModeException r8 = new java.nio.channels.IllegalBlockingModeException     // Catch:{ all -> 0x00d7 }
                r8.<init>()     // Catch:{ all -> 0x00d7 }
                throw r8     // Catch:{ all -> 0x00d7 }
            L_0x00d7:
                r8 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x00d7 }
                throw r8
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketAdaptor.SocketInputStream.read(java.nio.ByteBuffer):int");
        }
    }

    public InputStream getInputStream() throws IOException {
        if (!this.f893sc.isOpen()) {
            throw new SocketException("Socket is closed");
        } else if (!this.f893sc.isConnected()) {
            throw new SocketException("Socket is not connected");
        } else if (this.f893sc.isInputOpen()) {
            if (this.socketInputStream == null) {
                try {
                    this.socketInputStream = (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
                        public InputStream run() throws IOException {
                            return new SocketInputStream();
                        }
                    });
                } catch (PrivilegedActionException e) {
                    throw ((IOException) e.getException());
                }
            }
            return this.socketInputStream;
        } else {
            throw new SocketException("Socket input is shutdown");
        }
    }

    public OutputStream getOutputStream() throws IOException {
        if (!this.f893sc.isOpen()) {
            throw new SocketException("Socket is closed");
        } else if (!this.f893sc.isConnected()) {
            throw new SocketException("Socket is not connected");
        } else if (this.f893sc.isOutputOpen()) {
            try {
                return (OutputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<OutputStream>() {
                    public OutputStream run() throws IOException {
                        return Channels.newOutputStream((WritableByteChannel) SocketAdaptor.this.f893sc);
                    }
                });
            } catch (PrivilegedActionException e) {
                throw ((IOException) e.getException());
            }
        } else {
            throw new SocketException("Socket output is shutdown");
        }
    }

    private void setBooleanOption(SocketOption<Boolean> socketOption, boolean z) throws SocketException {
        try {
            this.f893sc.setOption((SocketOption) socketOption, (Object) Boolean.valueOf(z));
        } catch (IOException e) {
            Net.translateToSocketException(e);
        }
    }

    private void setIntOption(SocketOption<Integer> socketOption, int i) throws SocketException {
        try {
            this.f893sc.setOption((SocketOption) socketOption, (Object) Integer.valueOf(i));
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
            sun.nio.ch.SocketChannelImpl r0 = r0.f893sc     // Catch:{ IOException -> 0x000d }
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
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketAdaptor.getBooleanOption(java.net.SocketOption):boolean");
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.net.SocketOption<java.lang.Integer>, java.net.SocketOption] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getIntOption(java.net.SocketOption<java.lang.Integer> r1) throws java.net.SocketException {
        /*
            r0 = this;
            sun.nio.ch.SocketChannelImpl r0 = r0.f893sc     // Catch:{ IOException -> 0x000d }
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
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.SocketAdaptor.getIntOption(java.net.SocketOption):int");
    }

    public void setTcpNoDelay(boolean z) throws SocketException {
        setBooleanOption(StandardSocketOptions.TCP_NODELAY, z);
    }

    public boolean getTcpNoDelay() throws SocketException {
        return getBooleanOption(StandardSocketOptions.TCP_NODELAY);
    }

    public void setSoLinger(boolean z, int i) throws SocketException {
        if (!z) {
            i = -1;
        }
        setIntOption(StandardSocketOptions.SO_LINGER, i);
    }

    public int getSoLinger() throws SocketException {
        return getIntOption(StandardSocketOptions.SO_LINGER);
    }

    public void sendUrgentData(int i) throws IOException {
        if (this.f893sc.sendOutOfBandData((byte) i) == 0) {
            throw new IOException("Socket buffer full");
        }
    }

    public void setOOBInline(boolean z) throws SocketException {
        setBooleanOption(ExtendedSocketOption.SO_OOBINLINE, z);
    }

    public boolean getOOBInline() throws SocketException {
        return getBooleanOption(ExtendedSocketOption.SO_OOBINLINE);
    }

    public void setSoTimeout(int i) throws SocketException {
        if (i >= 0) {
            this.timeout = i;
            return;
        }
        throw new IllegalArgumentException("timeout can't be negative");
    }

    public int getSoTimeout() throws SocketException {
        return this.timeout;
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

    public void setKeepAlive(boolean z) throws SocketException {
        setBooleanOption(StandardSocketOptions.SO_KEEPALIVE, z);
    }

    public boolean getKeepAlive() throws SocketException {
        return getBooleanOption(StandardSocketOptions.SO_KEEPALIVE);
    }

    public void setTrafficClass(int i) throws SocketException {
        setIntOption(StandardSocketOptions.IP_TOS, i);
    }

    public int getTrafficClass() throws SocketException {
        return getIntOption(StandardSocketOptions.IP_TOS);
    }

    public void setReuseAddress(boolean z) throws SocketException {
        setBooleanOption(StandardSocketOptions.SO_REUSEADDR, z);
    }

    public boolean getReuseAddress() throws SocketException {
        return getBooleanOption(StandardSocketOptions.SO_REUSEADDR);
    }

    public void close() throws IOException {
        this.f893sc.close();
    }

    public void shutdownInput() throws IOException {
        try {
            this.f893sc.shutdownInput();
        } catch (Exception e) {
            Net.translateException(e);
        }
    }

    public void shutdownOutput() throws IOException {
        try {
            this.f893sc.shutdownOutput();
        } catch (Exception e) {
            Net.translateException(e);
        }
    }

    public String toString() {
        if (!this.f893sc.isConnected()) {
            return "Socket[unconnected]";
        }
        return "Socket[addr=" + getInetAddress() + ",port=" + getPort() + ",localport=" + getLocalPort() + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public boolean isConnected() {
        return this.f893sc.isConnected();
    }

    public boolean isBound() {
        return this.f893sc.localAddress() != null;
    }

    public boolean isClosed() {
        return !this.f893sc.isOpen();
    }

    public boolean isInputShutdown() {
        return !this.f893sc.isInputOpen();
    }

    public boolean isOutputShutdown() {
        return !this.f893sc.isOutputOpen();
    }

    public FileDescriptor getFileDescriptor$() {
        return this.f893sc.getFD();
    }
}
