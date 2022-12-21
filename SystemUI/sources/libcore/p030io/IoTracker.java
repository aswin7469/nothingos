package libcore.p030io;

import dalvik.system.BlockGuard;

/* renamed from: libcore.io.IoTracker */
public final class IoTracker {
    private boolean isOpen = true;
    private Mode mode = Mode.READ;
    private int opCount;
    private int totalByteCount;

    /* renamed from: libcore.io.IoTracker$Mode */
    public enum Mode {
        READ,
        WRITE
    }

    public void trackIo(int i) {
        int i2 = this.opCount + 1;
        this.opCount = i2;
        int i3 = this.totalByteCount + i;
        this.totalByteCount = i3;
        if (this.isOpen && i2 > 10 && i3 < 5120) {
            BlockGuard.getThreadPolicy().onUnbufferedIO();
            this.isOpen = false;
        }
    }

    public void trackIo(int i, Mode mode2) {
        if (this.mode != mode2) {
            reset();
            this.mode = mode2;
        }
        trackIo(i);
    }

    public void reset() {
        this.opCount = 0;
        this.totalByteCount = 0;
    }
}
