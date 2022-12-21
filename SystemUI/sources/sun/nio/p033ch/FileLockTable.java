package sun.nio.p033ch;

import java.nio.channels.Channel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.List;

/* renamed from: sun.nio.ch.FileLockTable */
abstract class FileLockTable {
    public abstract void add(FileLock fileLock) throws OverlappingFileLockException;

    public abstract void remove(FileLock fileLock);

    public abstract List<FileLock> removeAll();

    public abstract void replace(FileLock fileLock, FileLock fileLock2);

    protected FileLockTable() {
    }

    public static FileLockTable newSharedFileLockTable(Channel channel, FileDescriptor fileDescriptor) throws IOException {
        return new SharedFileLockTable(channel, fileDescriptor);
    }
}
