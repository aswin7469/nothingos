package android.icu.math;

import java.p026io.Serializable;

public final class MathContext implements Serializable {
    public static final MathContext DEFAULT = null;
    public static final int ENGINEERING = 2;
    public static final int PLAIN = 0;
    public static final int ROUND_CEILING = 2;
    public static final int ROUND_DOWN = 1;
    public static final int ROUND_FLOOR = 3;
    public static final int ROUND_HALF_DOWN = 5;
    public static final int ROUND_HALF_EVEN = 6;
    public static final int ROUND_HALF_UP = 4;
    public static final int ROUND_UNNECESSARY = 7;
    public static final int ROUND_UP = 0;
    public static final int SCIENTIFIC = 1;

    public MathContext(int i) {
        throw new RuntimeException("Stub!");
    }

    public MathContext(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public MathContext(int i, int i2, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public MathContext(int i, int i2, boolean z, int i3) {
        throw new RuntimeException("Stub!");
    }

    public int getDigits() {
        throw new RuntimeException("Stub!");
    }

    public int getForm() {
        throw new RuntimeException("Stub!");
    }

    public boolean getLostDigits() {
        throw new RuntimeException("Stub!");
    }

    public int getRoundingMode() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
