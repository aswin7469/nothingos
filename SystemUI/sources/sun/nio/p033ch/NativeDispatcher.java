package sun.nio.p033ch;

import java.p026io.FileDescriptor;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.NativeDispatcher */
abstract class NativeDispatcher {
    /* access modifiers changed from: package-private */
    public abstract void close(FileDescriptor fileDescriptor) throws IOException;

    /* access modifiers changed from: package-private */
    public boolean needsPositionLock() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public void preClose(FileDescriptor fileDescriptor) throws IOException {
    }

    /* access modifiers changed from: package-private */
    public abstract int read(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract long readv(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract int write(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract long writev(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    NativeDispatcher() {
    }

    /* access modifiers changed from: package-private */
    public int pread(FileDescriptor fileDescriptor, long j, int i, long j2) throws IOException {
        throw new IOException("Operation Unsupported");
    }

    /* access modifiers changed from: package-private */
    public int pwrite(FileDescriptor fileDescriptor, long j, int i, long j2) throws IOException {
        throw new IOException("Operation Unsupported");
    }
}
