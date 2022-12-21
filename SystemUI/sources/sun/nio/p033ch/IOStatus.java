package sun.nio.p033ch;

/* renamed from: sun.nio.ch.IOStatus */
public final class IOStatus {
    public static final int EOF = -1;
    public static final int INTERRUPTED = -3;
    public static final int THROWN = -5;
    public static final int UNAVAILABLE = -2;
    public static final int UNSUPPORTED = -4;
    public static final int UNSUPPORTED_CASE = -6;

    public static boolean check(int i) {
        return i >= -2;
    }

    public static boolean check(long j) {
        return j >= -2;
    }

    public static boolean checkAll(long j) {
        return j > -1 || j < -6;
    }

    public static int normalize(int i) {
        if (i == -2) {
            return 0;
        }
        return i;
    }

    public static long normalize(long j) {
        if (j == -2) {
            return 0;
        }
        return j;
    }

    private IOStatus() {
    }
}
