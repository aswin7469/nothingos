package androidx.leanback.util;

public final class MathUtil {
    private MathUtil() {
    }

    public static int safeLongToInt(long j) {
        int i = (int) j;
        if (((long) i) == j) {
            return i;
        }
        throw new ArithmeticException("Input overflows int.\n");
    }
}
