package java.nio.file;

import java.p026io.Closeable;
import java.p026io.IOException;
import java.util.concurrent.TimeUnit;

public interface WatchService extends Closeable {
    void close() throws IOException;

    WatchKey poll();

    WatchKey poll(long j, TimeUnit timeUnit) throws InterruptedException;

    WatchKey take() throws InterruptedException;
}
