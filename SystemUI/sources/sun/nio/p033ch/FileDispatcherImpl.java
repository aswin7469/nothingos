package sun.nio.p033ch;

import dalvik.system.BlockGuard;
import java.nio.channels.SelectableChannel;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.FileDispatcherImpl */
class FileDispatcherImpl extends FileDispatcher {
    static native void close0(FileDescriptor fileDescriptor) throws IOException;

    static native void closeIntFD(int i) throws IOException;

    static native int force0(FileDescriptor fileDescriptor, boolean z) throws IOException;

    static native int lock0(FileDescriptor fileDescriptor, boolean z, long j, long j2, boolean z2) throws IOException;

    static native void preClose0(FileDescriptor fileDescriptor) throws IOException;

    static native int pread0(FileDescriptor fileDescriptor, long j, int i, long j2) throws IOException;

    static native int pwrite0(FileDescriptor fileDescriptor, long j, int i, long j2) throws IOException;

    static native int read0(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    static native long readv0(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    static native void release0(FileDescriptor fileDescriptor, long j, long j2) throws IOException;

    static native long size0(FileDescriptor fileDescriptor) throws IOException;

    static native int truncate0(FileDescriptor fileDescriptor, long j) throws IOException;

    static native int write0(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    static native long writev0(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    /* access modifiers changed from: package-private */
    public boolean canTransferToDirectly(SelectableChannel selectableChannel) {
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean transferToDirectlyNeedsPositionLock() {
        return false;
    }

    FileDispatcherImpl(boolean z) {
    }

    FileDispatcherImpl() {
    }

    /* access modifiers changed from: package-private */
    public int read(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return read0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public int pread(FileDescriptor fileDescriptor, long j, int i, long j2) throws IOException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return pread0(fileDescriptor, j, i, j2);
    }

    /* access modifiers changed from: package-private */
    public long readv(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return readv0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public int write(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return write0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public int pwrite(FileDescriptor fileDescriptor, long j, int i, long j2) throws IOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return pwrite0(fileDescriptor, j, i, j2);
    }

    /* access modifiers changed from: package-private */
    public long writev(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return writev0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public int force(FileDescriptor fileDescriptor, boolean z) throws IOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return force0(fileDescriptor, z);
    }

    /* access modifiers changed from: package-private */
    public int truncate(FileDescriptor fileDescriptor, long j) throws IOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return truncate0(fileDescriptor, j);
    }

    /* access modifiers changed from: package-private */
    public long size(FileDescriptor fileDescriptor) throws IOException {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        return size0(fileDescriptor);
    }

    /* access modifiers changed from: package-private */
    public int lock(FileDescriptor fileDescriptor, boolean z, long j, long j2, boolean z2) throws IOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        return lock0(fileDescriptor, z, j, j2, z2);
    }

    /* access modifiers changed from: package-private */
    public void release(FileDescriptor fileDescriptor, long j, long j2) throws IOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        release0(fileDescriptor, j, j2);
    }

    /* access modifiers changed from: package-private */
    public void close(FileDescriptor fileDescriptor) throws IOException {
        close0(fileDescriptor);
    }

    /* access modifiers changed from: package-private */
    public void preClose(FileDescriptor fileDescriptor) throws IOException {
        preClose0(fileDescriptor);
    }

    /* access modifiers changed from: package-private */
    public FileDescriptor duplicateForMapping(FileDescriptor fileDescriptor) {
        return new FileDescriptor();
    }
}
