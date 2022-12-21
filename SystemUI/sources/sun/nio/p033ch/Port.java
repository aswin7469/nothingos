package sun.nio.p033ch;

import java.nio.channels.Channel;
import java.nio.channels.ShutdownChannelGroupException;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.p026io.Closeable;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* renamed from: sun.nio.ch.Port */
abstract class Port extends AsynchronousChannelGroupImpl {
    protected final Map<Integer, PollableChannel> fdToChannel = new HashMap();
    protected final ReadWriteLock fdToChannelLock = new ReentrantReadWriteLock();

    /* renamed from: sun.nio.ch.Port$PollableChannel */
    interface PollableChannel extends Closeable {
        void onEvent(int i, boolean z);
    }

    /* access modifiers changed from: protected */
    public void preUnregister(int i) {
    }

    /* access modifiers changed from: package-private */
    public abstract void startPoll(int i, int i2);

    Port(AsynchronousChannelProvider asynchronousChannelProvider, ThreadPool threadPool) {
        super(asynchronousChannelProvider, threadPool);
    }

    /* access modifiers changed from: package-private */
    public final void register(int i, PollableChannel pollableChannel) {
        this.fdToChannelLock.writeLock().lock();
        try {
            if (!isShutdown()) {
                this.fdToChannel.put(Integer.valueOf(i), pollableChannel);
                return;
            }
            throw new ShutdownChannelGroupException();
        } finally {
            this.fdToChannelLock.writeLock().unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public final void unregister(int i) {
        preUnregister(i);
        this.fdToChannelLock.writeLock().lock();
        try {
            this.fdToChannel.remove(Integer.valueOf(i));
            if (this.fdToChannel.isEmpty() && isShutdown()) {
                try {
                    shutdownNow();
                } catch (IOException unused) {
                }
            }
        } finally {
            this.fdToChannelLock.writeLock().unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean isEmpty() {
        this.fdToChannelLock.writeLock().lock();
        try {
            return this.fdToChannel.isEmpty();
        } finally {
            this.fdToChannelLock.writeLock().unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public final Object attachForeignChannel(final Channel channel, FileDescriptor fileDescriptor) {
        int fdVal = IOUtil.fdVal(fileDescriptor);
        register(fdVal, new PollableChannel() {
            public void onEvent(int i, boolean z) {
            }

            public void close() throws IOException {
                channel.close();
            }
        });
        return Integer.valueOf(fdVal);
    }

    /* access modifiers changed from: package-private */
    public final void detachForeignChannel(Object obj) {
        unregister(((Integer) obj).intValue());
    }

    /* access modifiers changed from: package-private */
    public final void closeAllChannels() {
        int i;
        PollableChannel[] pollableChannelArr = new PollableChannel[128];
        do {
            this.fdToChannelLock.writeLock().lock();
            try {
                Iterator<Integer> it = this.fdToChannel.keySet().iterator();
                i = 0;
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    int i2 = i + 1;
                    pollableChannelArr[i] = this.fdToChannel.get(it.next());
                    if (i2 >= 128) {
                        i = i2;
                        break;
                    }
                    i = i2;
                }
                for (int i3 = 0; i3 < i; i3++) {
                    try {
                        pollableChannelArr[i3].close();
                    } catch (IOException unused) {
                    }
                }
            } finally {
                this.fdToChannelLock.writeLock().unlock();
            }
        } while (i > 0);
    }
}
