package java.nio.channels;

import java.nio.channels.spi.AsynchronousChannelProvider;
import java.p026io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public abstract class AsynchronousChannelGroup {
    private final AsynchronousChannelProvider provider;

    public abstract boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException;

    public abstract boolean isShutdown();

    public abstract boolean isTerminated();

    public abstract void shutdown();

    public abstract void shutdownNow() throws IOException;

    protected AsynchronousChannelGroup(AsynchronousChannelProvider asynchronousChannelProvider) {
        this.provider = asynchronousChannelProvider;
    }

    public final AsynchronousChannelProvider provider() {
        return this.provider;
    }

    public static AsynchronousChannelGroup withFixedThreadPool(int i, ThreadFactory threadFactory) throws IOException {
        return AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(i, threadFactory);
    }

    public static AsynchronousChannelGroup withCachedThreadPool(ExecutorService executorService, int i) throws IOException {
        return AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(executorService, i);
    }

    public static AsynchronousChannelGroup withThreadPool(ExecutorService executorService) throws IOException {
        return AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(executorService, 0);
    }
}
