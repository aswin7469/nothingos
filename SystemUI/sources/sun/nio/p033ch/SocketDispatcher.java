package sun.nio.p033ch;

import dalvik.system.BlockGuard;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.SocketDispatcher */
class SocketDispatcher extends NativeDispatcher {
    SocketDispatcher() {
    }

    /* access modifiers changed from: package-private */
    public int read(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onNetwork();
        return FileDispatcherImpl.read0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public long readv(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onNetwork();
        return FileDispatcherImpl.readv0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public int write(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onNetwork();
        return FileDispatcherImpl.write0(fileDescriptor, j, i);
    }

    /* access modifiers changed from: package-private */
    public long writev(FileDescriptor fileDescriptor, long j, int i) throws IOException {
        BlockGuard.getThreadPolicy().onNetwork();
        return FileDispatcherImpl.writev0(fileDescriptor, j, i);
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
