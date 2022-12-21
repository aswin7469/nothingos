package sun.nio.p033ch;

import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.IllegalChannelGroupException;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.p026io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/* renamed from: sun.nio.ch.LinuxAsynchronousChannelProvider */
public class LinuxAsynchronousChannelProvider extends AsynchronousChannelProvider {
    private static volatile EPollPort defaultPort;

    private EPollPort defaultEventPort() throws IOException {
        if (defaultPort == null) {
            synchronized (LinuxAsynchronousChannelProvider.class) {
                if (defaultPort == null) {
                    defaultPort = new EPollPort(this, ThreadPool.getDefault()).start();
                }
            }
        }
        return defaultPort;
    }

    public AsynchronousChannelGroup openAsynchronousChannelGroup(int i, ThreadFactory threadFactory) throws IOException {
        return new EPollPort(this, ThreadPool.create(i, threadFactory)).start();
    }

    public AsynchronousChannelGroup openAsynchronousChannelGroup(ExecutorService executorService, int i) throws IOException {
        return new EPollPort(this, ThreadPool.wrap(executorService, i)).start();
    }

    private Port toPort(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException {
        if (asynchronousChannelGroup == null) {
            return defaultEventPort();
        }
        if (asynchronousChannelGroup instanceof EPollPort) {
            return (Port) asynchronousChannelGroup;
        }
        throw new IllegalChannelGroupException();
    }

    public AsynchronousServerSocketChannel openAsynchronousServerSocketChannel(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException {
        return new UnixAsynchronousServerSocketChannelImpl(toPort(asynchronousChannelGroup));
    }

    public AsynchronousSocketChannel openAsynchronousSocketChannel(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException {
        return new UnixAsynchronousSocketChannelImpl(toPort(asynchronousChannelGroup));
    }
}
