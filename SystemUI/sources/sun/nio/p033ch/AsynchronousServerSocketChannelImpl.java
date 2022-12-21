package sun.nio.p033ch;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import sun.net.NetHooks;

/* renamed from: sun.nio.ch.AsynchronousServerSocketChannelImpl */
abstract class AsynchronousServerSocketChannelImpl extends AsynchronousServerSocketChannel implements Cancellable, Groupable {
    private volatile boolean acceptKilled;
    private ReadWriteLock closeLock = new ReentrantReadWriteLock();

    /* renamed from: fd */
    protected final FileDescriptor f873fd = Net.serverSocket(true);
    private boolean isReuseAddress;
    protected volatile InetSocketAddress localAddress = null;
    private volatile boolean open = true;
    private final Object stateLock = new Object();

    /* access modifiers changed from: package-private */
    public abstract Future<AsynchronousSocketChannel> implAccept(Object obj, CompletionHandler<AsynchronousSocketChannel, Object> completionHandler);

    /* access modifiers changed from: package-private */
    public abstract void implClose() throws IOException;

    AsynchronousServerSocketChannelImpl(AsynchronousChannelGroupImpl asynchronousChannelGroupImpl) {
        super(asynchronousChannelGroupImpl.provider());
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

    public final Future<AsynchronousSocketChannel> accept() {
        return implAccept((Object) null, (CompletionHandler<AsynchronousSocketChannel, Object>) null);
    }

    public final <A> void accept(A a, CompletionHandler<AsynchronousSocketChannel, ? super A> completionHandler) {
        if (completionHandler != null) {
            implAccept(a, completionHandler);
            return;
        }
        throw new NullPointerException("'handler' is null");
    }

    /* access modifiers changed from: package-private */
    public final boolean isAcceptKilled() {
        return this.acceptKilled;
    }

    public final void onCancel(PendingFuture<?, ?> pendingFuture) {
        this.acceptKilled = true;
    }

    public final AsynchronousServerSocketChannel bind(SocketAddress socketAddress, int i) throws IOException {
        InetSocketAddress inetSocketAddress;
        if (socketAddress == null) {
            inetSocketAddress = new InetSocketAddress(0);
        } else {
            inetSocketAddress = Net.checkAddress(socketAddress);
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkListen(inetSocketAddress.getPort());
        }
        try {
            begin();
            synchronized (this.stateLock) {
                if (this.localAddress == null) {
                    NetHooks.beforeTcpBind(this.f873fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                    Net.bind(this.f873fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                    FileDescriptor fileDescriptor = this.f873fd;
                    if (i < 1) {
                        i = 50;
                    }
                    Net.listen(fileDescriptor, i);
                    this.localAddress = Net.localAddress(this.f873fd);
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

    public final <T> AsynchronousServerSocketChannel setOption(SocketOption<T> socketOption, T t) throws IOException {
        socketOption.getClass();
        if (supportedOptions().contains(socketOption)) {
            try {
                begin();
                if (socketOption != StandardSocketOptions.SO_REUSEADDR || !Net.useExclusiveBind()) {
                    Net.setSocketOption(this.f873fd, Net.UNSPEC, socketOption, t);
                } else {
                    this.isReuseAddress = ((Boolean) t).booleanValue();
                }
                return this;
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
                T socketOption2 = Net.getSocketOption(this.f873fd, Net.UNSPEC, socketOption);
                end();
                return socketOption2;
            } finally {
                end();
            }
        } else {
            throw new UnsupportedOperationException("'" + socketOption + "' not supported");
        }
    }

    /* renamed from: sun.nio.ch.AsynchronousServerSocketChannelImpl$DefaultOptionsHolder */
    private static class DefaultOptionsHolder {
        static final Set<SocketOption<?>> defaultOptions = defaultOptions();

        private DefaultOptionsHolder() {
        }

        private static Set<SocketOption<?>> defaultOptions() {
            HashSet hashSet = new HashSet(2);
            hashSet.add(StandardSocketOptions.SO_RCVBUF);
            hashSet.add(StandardSocketOptions.SO_REUSEADDR);
            return Collections.unmodifiableSet(hashSet);
        }
    }

    public final Set<SocketOption<?>> supportedOptions() {
        return DefaultOptionsHolder.defaultOptions;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append('[');
        if (!isOpen()) {
            sb.append("closed");
        } else if (this.localAddress == null) {
            sb.append("unbound");
        } else {
            sb.append(Net.getRevealedLocalAddressAsString(this.localAddress));
        }
        sb.append(']');
        return sb.toString();
    }
}
