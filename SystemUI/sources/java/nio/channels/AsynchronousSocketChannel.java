package java.nio.channels;

import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.p026io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class AsynchronousSocketChannel implements AsynchronousByteChannel, NetworkChannel {
    private final AsynchronousChannelProvider provider;

    public abstract AsynchronousSocketChannel bind(SocketAddress socketAddress) throws IOException;

    public abstract Future<Void> connect(SocketAddress socketAddress);

    public abstract <A> void connect(SocketAddress socketAddress, A a, CompletionHandler<Void, ? super A> completionHandler);

    public abstract SocketAddress getLocalAddress() throws IOException;

    public abstract SocketAddress getRemoteAddress() throws IOException;

    public abstract Future<Integer> read(ByteBuffer byteBuffer);

    public abstract <A> void read(ByteBuffer byteBuffer, long j, TimeUnit timeUnit, A a, CompletionHandler<Integer, ? super A> completionHandler);

    public abstract <A> void read(ByteBuffer[] byteBufferArr, int i, int i2, long j, TimeUnit timeUnit, A a, CompletionHandler<Long, ? super A> completionHandler);

    public abstract <T> AsynchronousSocketChannel setOption(SocketOption<T> socketOption, T t) throws IOException;

    public abstract AsynchronousSocketChannel shutdownInput() throws IOException;

    public abstract AsynchronousSocketChannel shutdownOutput() throws IOException;

    public abstract Future<Integer> write(ByteBuffer byteBuffer);

    public abstract <A> void write(ByteBuffer byteBuffer, long j, TimeUnit timeUnit, A a, CompletionHandler<Integer, ? super A> completionHandler);

    public abstract <A> void write(ByteBuffer[] byteBufferArr, int i, int i2, long j, TimeUnit timeUnit, A a, CompletionHandler<Long, ? super A> completionHandler);

    protected AsynchronousSocketChannel(AsynchronousChannelProvider asynchronousChannelProvider) {
        this.provider = asynchronousChannelProvider;
    }

    public final AsynchronousChannelProvider provider() {
        return this.provider;
    }

    public static AsynchronousSocketChannel open(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException {
        return (asynchronousChannelGroup == null ? AsynchronousChannelProvider.provider() : asynchronousChannelGroup.provider()).openAsynchronousSocketChannel(asynchronousChannelGroup);
    }

    public static AsynchronousSocketChannel open() throws IOException {
        return open((AsynchronousChannelGroup) null);
    }

    public final <A> void read(ByteBuffer byteBuffer, A a, CompletionHandler<Integer, ? super A> completionHandler) {
        read(byteBuffer, 0, TimeUnit.MILLISECONDS, a, completionHandler);
    }

    public final <A> void write(ByteBuffer byteBuffer, A a, CompletionHandler<Integer, ? super A> completionHandler) {
        write(byteBuffer, 0, TimeUnit.MILLISECONDS, a, completionHandler);
    }
}
