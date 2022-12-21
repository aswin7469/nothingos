package sun.nio.p033ch;

import dalvik.system.BlockGuard;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.DatagramDispatcher */
class DatagramDispatcher extends NativeDispatcher {
    static native int read0(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    static native long readv0(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    static native int write0(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    static native long writev0(FileDescriptor fileDescriptor, long j, int i) throws IOException;

    DatagramDispatcher() {
    }

    /* access modifiers changed from: package-private */
    public int read(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onNetwork();
        return read0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public long readv(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onNetwork();
        return readv0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public int write(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onNetwork();
        return write0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public long writev(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onNetwork();
        return writev0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public void close(FileDescriptor fileDescriptor) throws IOException {
        FileDispatcherImpl.close0(fileDescriptor);
    }

    /* access modifiers changed from: package-private */
    public void preClose(FileDescriptor fileDescriptor) throws IOException {
        FileDispatcherImpl.preClose0(fileDescriptor);
    }
}
