package libcore.math;

public final class NativeBN {
    public static native void BN_div(long j, long j2, long j3, long j4);

    public static native void BN_free(long j);

    public static native void BN_mod_exp(long j, long j2, long j3, long j4);

    public static native void BN_mul(long j, long j2, long j3);

    public static native long BN_new();

    public static native int[] bn2litEndInts(long j);

    public static native void litEndInts2bn(int[] iArr, int i, boolean z, long j);
}
