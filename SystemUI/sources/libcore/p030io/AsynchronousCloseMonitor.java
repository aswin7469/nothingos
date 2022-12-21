package libcore.p030io;

import java.p026io.FileDescriptor;

/* renamed from: libcore.io.AsynchronousCloseMonitor */
public final class AsynchronousCloseMonitor {
    public static native void signalBlockedThreads(FileDescriptor fileDescriptor);

    private AsynchronousCloseMonitor() {
    }
}
