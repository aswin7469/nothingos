package sun.nio.p033ch;

import android.net.wifi.WifiManager;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.ReadPendingException;
import java.nio.channels.WritePendingException;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import jdk.net.ExtendedSocketOptions;
import sun.net.ExtendedOptionsImpl;
import sun.net.NetHooks;

/* renamed from: sun.nio.ch.AsynchronousSocketChannelImpl */
abstract class AsynchronousSocketChannelImpl extends AsynchronousSocketChannel implements Cancellable, Groupable {
    static final int ST_CONNECTED = 2;
    static final int ST_PENDING = 1;
    static final int ST_UNCONNECTED = 0;
    static final int ST_UNINITIALIZED = -1;
    private final ReadWriteLock closeLock;

    /* renamed from: fd */
    protected final FileDescriptor f874fd;
    private boolean isReuseAddress;
    protected volatile InetSocketAddress localAddress;
    private volatile boolean open;
    private boolean readKilled;
    private final Object readLock;
    private boolean readShutdown;
    private boolean reading;
    protected volatile InetSocketAddress remoteAddress;
    protected volatile int state;
    protected final Object stateLock;
    private boolean writeKilled;
    private final Object writeLock;
    private boolean writeShutdown;
    private boolean writing;

    /* access modifiers changed from: package-private */
    public abstract void implClose() throws IOException;

    /* access modifiers changed from: package-private */
    public abstract <A> Future<Void> implConnect(SocketAddress socketAddress, A a, CompletionHandler<Void, ? super A> completionHandler);

    /* access modifiers changed from: package-private */
    public abstract <V extends Number, A> Future<V> implRead(boolean z, ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, long j, TimeUnit timeUnit, A a, CompletionHandler<V, ? super A> completionHandler);

    /* access modifiers changed from: package-private */
    public abstract <V extends Number, A> Future<V> implWrite(boolean z, ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, long j, TimeUnit timeUnit, A a, CompletionHandler<V, ? super A> completionHandler);

    AsynchronousSocketChannelImpl(AsynchronousChannelGroupImpl asynchronousChannelGroupImpl) throws IOException {
        super(asynchronousChannelGroupImpl.provider());
        this.stateLock = new Object();
        this.localAddress = null;
        this.remoteAddress = null;
        this.state = -1;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.closeLock = new ReentrantReadWriteLock();
        this.open = true;
        this.f874fd = Net.socket(true);
        this.state = 0;
    }

    AsynchronousSocketChannelImpl(AsynchronousChannelGroupImpl asynchronousChannelGroupImpl, FileDescriptor fileDescriptor, InetSocketAddress inetSocketAddress) throws IOException {
        super(asynchronousChannelGroupImpl.provider());
        this.stateLock = new Object();
        this.localAddress = null;
        this.remoteAddress = null;
        this.state = -1;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.closeLock = new ReentrantReadWriteLock();
        this.open = true;
        this.f874fd = fileDescriptor;
        this.state = 2;
        this.localAddress = Net.localAddress(fileDescriptor);
        this.remoteAddress = inetSocketAddress;
    }

    public final boolean isOpen() {
        return this.open;
    }

    /* access modifiers changed from: package-private */
    public final void begin() throws IOException {
        this.closeLock.readLock().lock();
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
    }

    /* access modifiers changed from: package-private */
    public final void end() {
        this.closeLock.readLock().unlock();
    }

    public final void close() throws IOException {
        this.closeLock.writeLock().lock();
        try {
            if (this.open) {
                this.open = false;
                this.closeLock.writeLock().unlock();
                implClose();
            }
        } finally {
            this.closeLock.writeLock().unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public final void enableReading(boolean z) {
        synchronized (this.readLock) {
            this.reading = false;
            if (z) {
                this.readKilled = true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void enableReading() {
        enableReading(false);
    }

    /* access modifiers changed from: package-private */
    public final void enableWriting(boolean z) {
        synchronized (this.writeLock) {
            this.writing = false;
            if (z) {
                this.writeKilled = true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void enableWriting() {
        enableWriting(false);
    }

    /* access modifiers changed from: package-private */
    public final void killReading() {
        synchronized (this.readLock) {
            this.readKilled = true;
        }
    }

    /* access modifiers changed from: package-private */
    public final void killWriting() {
        synchronized (this.writeLock) {
            this.writeKilled = true;
        }
    }

    /* access modifiers changed from: package-private */
    public final void killConnect() {
        killReading();
        killWriting();
    }

    public final Future<Void> connect(SocketAddress socketAddress) {
        return implConnect(socketAddress, (Object) null, (CompletionHandler) null);
    }

    public final <A> void connect(SocketAddress socketAddress, A a, CompletionHandler<Void, ? super A> completionHandler) {
        if (completionHandler != null) {
            implConnect(socketAddress, a, completionHandler);
            return;
        }
        throw new NullPointerException("'handler' is null");
    }

    private <V extends Number, A> Future<V> read(boolean z, ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, long j, TimeUnit timeUnit, A a, CompletionHandler<V, ? super A> completionHandler) {
        Object obj;
        if (!isOpen()) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invoke(this, completionHandler, a, null, closedChannelException);
            return null;
        } else if (this.remoteAddress != null) {
            int i = 0;
            boolean z2 = true;
            boolean z3 = z || byteBuffer.hasRemaining();
            synchronized (this.readLock) {
                if (this.readKilled) {
                    throw new IllegalStateException("Reading not allowed due to timeout or cancellation");
                } else if (this.reading) {
                    throw new ReadPendingException();
                } else if (!this.readShutdown) {
                    if (z3) {
                        this.reading = true;
                    }
                    z2 = false;
                }
            }
            if (!z2 && z3) {
                return implRead(z, byteBuffer, byteBufferArr, j, timeUnit, a, completionHandler);
            }
            if (z) {
                obj = Long.valueOf(z2 ? -1 : 0);
            } else {
                if (z2) {
                    i = -1;
                }
                obj = Integer.valueOf(i);
            }
            if (completionHandler == null) {
                return CompletedFuture.withResult(obj);
            }
            Invoker.invoke(this, completionHandler, a, obj, (Throwable) null);
            return null;
        } else {
            throw new NotYetConnectedException();
        }
    }

    public final Future<Integer> read(ByteBuffer byteBuffer) {
        if (!byteBuffer.isReadOnly()) {
            return read(false, byteBuffer, (ByteBuffer[]) null, 0, TimeUnit.MILLISECONDS, (Object) null, (CompletionHandler) null);
        }
        throw new IllegalArgumentException("Read-only buffer");
    }

    public final <A> void read(ByteBuffer byteBuffer, long j, TimeUnit timeUnit, A a, CompletionHandler<Integer, ? super A> completionHandler) {
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        } else if (!byteBuffer.isReadOnly()) {
            read(false, byteBuffer, (ByteBuffer[]) null, j, timeUnit, a, completionHandler);
        } else {
            throw new IllegalArgumentException("Read-only buffer");
        }
    }

    public final <A> void read(ByteBuffer[] byteBufferArr, int i, int i2, long j, TimeUnit timeUnit, A a, CompletionHandler<Long, ? super A> completionHandler) {
        int i3 = i;
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        } else if (i3 < 0 || i2 < 0 || i3 > byteBufferArr.length - i2) {
            throw new IndexOutOfBoundsException();
        } else {
            ByteBuffer[] subsequence = Util.subsequence(byteBufferArr, i, i2);
            int i4 = 0;
            while (i4 < subsequence.length) {
                if (!subsequence[i4].isReadOnly()) {
                    i4++;
                } else {
                    throw new IllegalArgumentException("Read-only buffer");
                }
            }
            read(true, (ByteBuffer) null, subsequence, j, timeUnit, a, completionHandler);
        }
    }

    private <V extends Number, A> Future<V> write(boolean z, ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, long j, TimeUnit timeUnit, A a, CompletionHandler<V, ? super A> completionHandler) {
        boolean z2 = true;
        boolean z3 = z || byteBuffer.hasRemaining();
        if (isOpen()) {
            if (this.remoteAddress != null) {
                synchronized (this.writeLock) {
                    if (this.writeKilled) {
                        throw new IllegalStateException("Writing not allowed due to timeout or cancellation");
                    } else if (this.writing) {
                        throw new WritePendingException();
                    } else if (!this.writeShutdown) {
                        if (z3) {
                            this.writing = true;
                        }
                        z2 = false;
                    }
                }
            } else {
                throw new NotYetConnectedException();
            }
        }
        if (z2) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invoke(this, completionHandler, a, null, closedChannelException);
            return null;
        } else if (z3) {
            return implWrite(z, byteBuffer, byteBufferArr, j, timeUnit, a, completionHandler);
        } else {
            int i = z ? 0L : 0;
            if (completionHandler == null) {
                return CompletedFuture.withResult(i);
            }
            Invoker.invoke(this, completionHandler, a, i, (Throwable) null);
            return null;
        }
    }

    public final Future<Integer> write(ByteBuffer byteBuffer) {
        return write(false, byteBuffer, (ByteBuffer[]) null, 0, TimeUnit.MILLISECONDS, (Object) null, (CompletionHandler) null);
    }

    public final <A> void write(ByteBuffer byteBuffer, long j, TimeUnit timeUnit, A a, CompletionHandler<Integer, ? super A> completionHandler) {
        if (completionHandler != null) {
            write(false, byteBuffer, (ByteBuffer[]) null, j, timeUnit, a, completionHandler);
            return;
        }
        throw new NullPointerException("'handler' is null");
    }

    public final <A> void write(ByteBuffer[] byteBufferArr, int i, int i2, long j, TimeUnit timeUnit, A a, CompletionHandler<Long, ? super A> completionHandler) {
        int i3 = i;
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        } else if (i3 < 0 || i2 < 0 || i3 > byteBufferArr.length - i2) {
            throw new IndexOutOfBoundsException();
        } else {
            write(true, (ByteBuffer) null, Util.subsequence(byteBufferArr, i, i2), j, timeUnit, a, completionHandler);
        }
    }

    public final AsynchronousSocketChannel bind(SocketAddress socketAddress) throws IOException {
        try {
            begin();
            synchronized (this.stateLock) {
                if (this.state == 1) {
                    throw new ConnectionPendingException();
                } else if (this.localAddress == null) {
                    InetSocketAddress inetSocketAddress = socketAddress == null ? new InetSocketAddress(0) : Net.checkAddress(socketAddress);
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkListen(inetSocketAddress.getPort());
                    }
                    NetHooks.beforeTcpBind(this.f874fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                    Net.bind(this.f874fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                    this.localAddress = Net.localAddress(this.f874fd);
                } else {
                    throw new AlreadyBoundException();
                }
            }
            end();
            return this;
        } catch (Throwable th) {
            end();
            throw th;
        }
    }

    public final SocketAddress getLocalAddress() throws IOException {
        if (isOpen()) {
            return Net.getRevealedLocalAddress(this.localAddress);
        }
        throw new ClosedChannelException();
    }

    public final <T> AsynchronousSocketChannel setOption(SocketOption<T> socketOption, T t) throws IOException {
        socketOption.getClass();
        if (supportedOptions().contains(socketOption)) {
            try {
                begin();
                if (!this.writeShutdown) {
                    if (socketOption != StandardSocketOptions.SO_REUSEADDR || !Net.useExclusiveBind()) {
                        Net.setSocketOption(this.f874fd, Net.UNSPEC, socketOption, t);
                    } else {
                        this.isReuseAddress = ((Boolean) t).booleanValue();
                    }
                    return this;
                }
                throw new IOException("Connection has been shutdown for writing");
            } finally {
                end();
            }
        } else {
            throw new UnsupportedOperationException("'" + socketOption + "' not supported");
        }
    }

    public final <T> T getOption(SocketOption<T> socketOption) throws IOException {
        socketOption.getClass();
        if (supportedOptions().contains(socketOption)) {
            try {
                begin();
                if (socketOption == StandardSocketOptions.SO_REUSEADDR && Net.useExclusiveBind()) {
                    return Boolean.valueOf(this.isReuseAddress);
                }
                T socketOption2 = Net.getSocketOption(this.f874fd, Net.UNSPEC, socketOption);
                end();
                return socketOption2;
            } finally {
                end();
            }
        } else {
            throw new UnsupportedOperationException("'" + socketOption + "' not supported");
        }
    }

    /* renamed from: sun.nio.ch.AsynchronousSocketChannelImpl$DefaultOptionsHolder */
    private static class DefaultOptionsHolder {
        static final Set<SocketOption<?>> defaultOptions = defaultOptions();

        private DefaultOptionsHolder() {
        }

        private static Set<SocketOption<?>> defaultOptions() {
            HashSet hashSet = new HashSet(5);
            hashSet.add(StandardSocketOptions.SO_SNDBUF);
            hashSet.add(StandardSocketOptions.SO_RCVBUF);
            hashSet.add(StandardSocketOptions.SO_KEEPALIVE);
            hashSet.add(StandardSocketOptions.SO_REUSEADDR);
            hashSet.add(StandardSocketOptions.TCP_NODELAY);
            if (ExtendedOptionsImpl.flowSupported()) {
                hashSet.add(ExtendedSocketOptions.SO_FLOW_SLA);
            }
            return Collections.unmodifiableSet(hashSet);
        }
    }

    public final Set<SocketOption<?>> supportedOptions() {
        return DefaultOptionsHolder.defaultOptions;
    }

    public final SocketAddress getRemoteAddress() throws IOException {
        if (isOpen()) {
            return this.remoteAddress;
        }
        throw new ClosedChannelException();
    }

    public final AsynchronousSocketChannel shutdownInput() throws IOException {
        try {
            begin();
            if (this.remoteAddress != null) {
                synchronized (this.readLock) {
                    if (!this.readShutdown) {
                        Net.shutdown(this.f874fd, 0);
                        this.readShutdown = true;
                    }
                }
                end();
                return this;
            }
            throw new NotYetConnectedException();
        } catch (Throwable th) {
            end();
            throw th;
        }
    }

    public final AsynchronousSocketChannel shutdownOutput() throws IOException {
        try {
            begin();
            if (this.remoteAddress != null) {
                synchronized (this.writeLock) {
                    if (!this.writeShutdown) {
                        Net.shutdown(this.f874fd, 1);
                        this.writeShutdown = true;
                    }
                }
                end();
                return this;
            }
            throw new NotYetConnectedException();
        } catch (Throwable th) {
            end();
            throw th;
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append('[');
        synchronized (this.stateLock) {
            if (!isOpen()) {
                sb.append("closed");
            } else {
                int i = this.state;
                if (i == 0) {
                    sb.append("unconnected");
                } else if (i == 1) {
                    sb.append("connection-pending");
                } else if (i == 2) {
                    sb.append(WifiManager.EXTRA_SUPPLICANT_CONNECTED);
                    if (this.readShutdown) {
                        sb.append(" ishut");
                    }
                    if (this.writeShutdown) {
                        sb.append(" oshut");
                    }
                }
                if (this.localAddress != null) {
                    sb.append(" local=");
                    sb.append(Net.getRevealedLocalAddressAsString(this.localAddress));
                }
                if (this.remoteAddress != null) {
                    sb.append(" remote=");
                    sb.append(this.remoteAddress.toString());
                }
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
