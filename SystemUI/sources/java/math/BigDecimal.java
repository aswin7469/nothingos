package java.math;

import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import com.android.p019wm.shell.bubbles.BubbleOverflow;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.StreamCorruptedException;
import java.util.Arrays;
import jdk.internal.math.DoubleConsts;
import sun.misc.Unsafe;

public class BigDecimal extends Number implements Comparable<BigDecimal> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static volatile BigInteger[] BIG_TEN_POWERS_TABLE = {BigInteger.ONE, BigInteger.valueOf(10), BigInteger.valueOf(100), BigInteger.valueOf(1000), BigInteger.valueOf(10000), BigInteger.valueOf(100000), BigInteger.valueOf(1000000), BigInteger.valueOf(10000000), BigInteger.valueOf(100000000), BigInteger.valueOf(1000000000), BigInteger.valueOf(10000000000L), BigInteger.valueOf(100000000000L), BigInteger.valueOf(1000000000000L), BigInteger.valueOf(10000000000000L), BigInteger.valueOf(100000000000000L), BigInteger.valueOf(1000000000000000L), BigInteger.valueOf(10000000000000000L), BigInteger.valueOf(100000000000000000L), BigInteger.valueOf(1000000000000000000L)};
    private static final int BIG_TEN_POWERS_TABLE_INITLEN;
    private static final int BIG_TEN_POWERS_TABLE_MAX;
    private static final long DIV_NUM_BASE = 4294967296L;
    private static final double[] DOUBLE_10_POW = {1.0d, 10.0d, 100.0d, 1000.0d, 10000.0d, 100000.0d, 1000000.0d, 1.0E7d, 1.0E8d, 1.0E9d, 1.0E10d, 1.0E11d, 1.0E12d, 1.0E13d, 1.0E14d, 1.0E15d, 1.0E16d, 1.0E17d, 1.0E18d, 1.0E19d, 1.0E20d, 1.0E21d, 1.0E22d};
    private static final float[] FLOAT_10_POW = {1.0f, 10.0f, 100.0f, 1000.0f, 10000.0f, 100000.0f, 1000000.0f, 1.0E7f, 1.0E8f, 1.0E9f, 1.0E10f};
    private static final long HALF_LONG_MAX_VALUE = 4611686018427387903L;
    private static final long HALF_LONG_MIN_VALUE = -4611686018427387904L;
    static final long INFLATED = Long.MIN_VALUE;
    private static final BigInteger INFLATED_BIGINT = BigInteger.valueOf(Long.MIN_VALUE);
    private static final long[][] LONGLONG_TEN_POWERS_TABLE = {new long[]{0, -8446744073709551616L}, new long[]{5, 7766279631452241920L}, new long[]{54, 3875820019684212736L}, new long[]{542, 1864712049423024128L}, new long[]{5421, 200376420520689664L}, new long[]{54210, 2003764205206896640L}, new long[]{542101, 1590897978359414784L}, new long[]{5421010, -2537764290115403776L}, new long[]{54210108, -6930898827444486144L}, new long[]{542101086, 4477988020393345024L}, new long[]{5421010862L, 7886392056514347008L}, new long[]{54210108624L, 5076944270305263616L}, new long[]{542101086242L, -4570789518076018688L}, new long[]{5421010862427L, -8814407033341083648L}, new long[]{54210108624275L, 4089650035136921600L}, new long[]{542101086242752L, 4003012203950112768L}, new long[]{5421010862427522L, 3136633892082024448L}, new long[]{54210108624275221L, -5527149226598858752L}, new long[]{542101086242752217L, 68739955140067328L}, new long[]{5421010862427522170L, 687399551400673280L}};
    private static final long[] LONG_TEN_POWERS_TABLE = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};
    private static final int MAX_COMPACT_DIGITS = 18;
    public static final BigDecimal ONE;
    private static final BigDecimal ONE_HALF = valueOf(5, 1);
    private static final BigDecimal ONE_TENTH = valueOf(1, 1);
    @Deprecated(since = "9")
    public static final int ROUND_CEILING = 2;
    @Deprecated(since = "9")
    public static final int ROUND_DOWN = 1;
    @Deprecated(since = "9")
    public static final int ROUND_FLOOR = 3;
    @Deprecated(since = "9")
    public static final int ROUND_HALF_DOWN = 5;
    @Deprecated(since = "9")
    public static final int ROUND_HALF_EVEN = 6;
    @Deprecated(since = "9")
    public static final int ROUND_HALF_UP = 4;
    @Deprecated(since = "9")
    public static final int ROUND_UNNECESSARY = 7;
    @Deprecated(since = "9")
    public static final int ROUND_UP = 0;
    public static final BigDecimal TEN;
    private static final long[] THRESHOLDS_TABLE = {Long.MAX_VALUE, 922337203685477580L, 92233720368547758L, 9223372036854775L, 922337203685477L, 92233720368547L, 9223372036854L, 922337203685L, 92233720368L, 9223372036L, 922337203, 92233720, 9223372, 922337, 92233, 9223, 922, 92, 9};
    public static final BigDecimal ZERO;
    private static final BigDecimal[] ZERO_SCALED_BY;
    private static final BigDecimal[] ZERO_THROUGH_TEN;
    private static final long serialVersionUID = 6108874887143696463L;
    private static final ThreadLocal<StringBuilderHelper> threadLocalStringBuilderHelper = new ThreadLocal<StringBuilderHelper>() {
        /* access modifiers changed from: protected */
        public StringBuilderHelper initialValue() {
            return new StringBuilderHelper();
        }
    };
    private final transient long intCompact;
    private final BigInteger intVal;
    private transient int precision;
    private final int scale;
    private transient String stringCache;

    private static long add(long j, long j2) {
        long j3 = j + j2;
        if (((j ^ j3) & (j2 ^ j3)) >= 0) {
            return j3;
        }
        return Long.MIN_VALUE;
    }

    private static int longCompareMagnitude(long j, long j2) {
        if (j < 0) {
            j = -j;
        }
        if (j2 < 0) {
            j2 = -j2;
        }
        int i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
        if (i < 0) {
            return -1;
        }
        return i == 0 ? 0 : 1;
    }

    private static boolean longLongCompareMagnitude(long j, long j2, long j3, long j4) {
        int i = (j > j3 ? 1 : (j == j3 ? 0 : -1));
        return i != 0 ? i < 0 : j2 + Long.MIN_VALUE < j4 + Long.MIN_VALUE;
    }

    private static long make64(long j, long j2) {
        return (j << 32) | j2;
    }

    private static int saturateLong(long j) {
        int i = (int) j;
        return j == ((long) i) ? i : j < 0 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }

    private static boolean unsignedLongCompare(long j, long j2) {
        return j + Long.MIN_VALUE > j2 + Long.MIN_VALUE;
    }

    private static boolean unsignedLongCompareEq(long j, long j2) {
        return j + Long.MIN_VALUE >= j2 + Long.MIN_VALUE;
    }

    public BigDecimal plus() {
        return this;
    }

    static {
        BigDecimal[] bigDecimalArr = {new BigDecimal(BigInteger.ZERO, 0, 0, 1), new BigDecimal(BigInteger.ONE, 1, 0, 1), new BigDecimal(BigInteger.TWO, 2, 0, 1), new BigDecimal(BigInteger.valueOf(3), 3, 0, 1), new BigDecimal(BigInteger.valueOf(4), 4, 0, 1), new BigDecimal(BigInteger.valueOf(5), 5, 0, 1), new BigDecimal(BigInteger.valueOf(6), 6, 0, 1), new BigDecimal(BigInteger.valueOf(7), 7, 0, 1), new BigDecimal(BigInteger.valueOf(8), 8, 0, 1), new BigDecimal(BigInteger.valueOf(9), 9, 0, 1), new BigDecimal(BigInteger.TEN, 10, 0, 2)};
        ZERO_THROUGH_TEN = bigDecimalArr;
        ZERO_SCALED_BY = new BigDecimal[]{bigDecimalArr[0], new BigDecimal(BigInteger.ZERO, 0, 1, 1), new BigDecimal(BigInteger.ZERO, 0, 2, 1), new BigDecimal(BigInteger.ZERO, 0, 3, 1), new BigDecimal(BigInteger.ZERO, 0, 4, 1), new BigDecimal(BigInteger.ZERO, 0, 5, 1), new BigDecimal(BigInteger.ZERO, 0, 6, 1), new BigDecimal(BigInteger.ZERO, 0, 7, 1), new BigDecimal(BigInteger.ZERO, 0, 8, 1), new BigDecimal(BigInteger.ZERO, 0, 9, 1), new BigDecimal(BigInteger.ZERO, 0, 10, 1), new BigDecimal(BigInteger.ZERO, 0, 11, 1), new BigDecimal(BigInteger.ZERO, 0, 12, 1), new BigDecimal(BigInteger.ZERO, 0, 13, 1), new BigDecimal(BigInteger.ZERO, 0, 14, 1), new BigDecimal(BigInteger.ZERO, 0, 15, 1)};
        ZERO = bigDecimalArr[0];
        ONE = bigDecimalArr[1];
        TEN = bigDecimalArr[10];
        int length = BIG_TEN_POWERS_TABLE.length;
        BIG_TEN_POWERS_TABLE_INITLEN = length;
        BIG_TEN_POWERS_TABLE_MAX = length * 16;
    }

    BigDecimal(BigInteger bigInteger, long j, int i, int i2) {
        this.scale = i;
        this.precision = i2;
        this.intCompact = j;
        this.intVal = bigInteger;
    }

    public BigDecimal(char[] cArr, int i, int i2) {
        this(cArr, i, i2, MathContext.UNLIMITED);
    }

    /* JADX WARNING: Removed duplicated region for block: B:115:0x0172 A[Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public BigDecimal(char[] r26, int r27, int r28, java.math.MathContext r29) {
        /*
            r25 = this;
            r0 = r25
            r1 = r26
            r2 = r27
            r3 = r28
            r4 = r29
            r25.<init>()
            int r5 = r1.length     // Catch:{ IndexOutOfBoundsException -> 0x0238 }
            java.util.Objects.checkFromIndexSize(r2, r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x0238 }
            char r5 = r1[r2]     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r6 = 45
            if (r5 != r6) goto L_0x001d
            int r2 = r2 + 1
            int r3 = r3 + -1
            r5 = 1
            goto L_0x0026
        L_0x001d:
            r6 = 43
            if (r5 != r6) goto L_0x0025
            int r2 = r2 + 1
            int r3 = r3 + -1
        L_0x0025:
            r5 = 0
        L_0x0026:
            r6 = 18
            if (r3 > r6) goto L_0x002c
            r6 = 1
            goto L_0x002d
        L_0x002c:
            r6 = 0
        L_0x002d:
            java.lang.String r9 = "Exponent overflow."
            java.lang.String r11 = "Character array contains more than one decimal point."
            r14 = 57
            java.lang.String r7 = "No digits found."
            r17 = 0
            r10 = 48
            r18 = 0
            if (r6 == 0) goto L_0x012e
            r21 = r9
            r8 = r18
            r6 = 0
            r13 = 0
            r16 = 0
        L_0x0045:
            if (r3 <= 0) goto L_0x00f1
            char r12 = r1[r2]     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r23 = 10
            if (r12 != r10) goto L_0x005e
            if (r6 != 0) goto L_0x0051
            r6 = 1
            goto L_0x0059
        L_0x0051:
            int r12 = (r8 > r18 ? 1 : (r8 == r18 ? 0 : -1))
            if (r12 == 0) goto L_0x0059
            long r8 = r8 * r23
            int r6 = r6 + 1
        L_0x0059:
            if (r16 == 0) goto L_0x00b0
        L_0x005b:
            int r13 = r13 + 1
            goto L_0x00b0
        L_0x005e:
            r10 = 49
            if (r12 < r10) goto L_0x0076
            if (r12 > r14) goto L_0x0076
            int r12 = r12 + -48
            r10 = 1
            if (r6 != r10) goto L_0x006d
            int r10 = (r8 > r18 ? 1 : (r8 == r18 ? 0 : -1))
            if (r10 == 0) goto L_0x006f
        L_0x006d:
            int r6 = r6 + 1
        L_0x006f:
            long r8 = r8 * r23
            long r14 = (long) r12     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r8 = r8 + r14
            if (r16 == 0) goto L_0x00b0
            goto L_0x005b
        L_0x0076:
            r14 = 46
            if (r12 != r14) goto L_0x0085
            if (r16 != 0) goto L_0x007f
            r16 = 1
            goto L_0x00b0
        L_0x007f:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r0.<init>(r11)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            throw r0     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
        L_0x0085:
            boolean r14 = java.lang.Character.isDigit((char) r12)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            if (r14 == 0) goto L_0x00b9
            r14 = 10
            int r12 = java.lang.Character.digit((char) r12, (int) r14)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            if (r12 != 0) goto L_0x00a0
            if (r6 != 0) goto L_0x0097
            r6 = 1
            goto L_0x00ad
        L_0x0097:
            int r12 = (r8 > r18 ? 1 : (r8 == r18 ? 0 : -1))
            if (r12 == 0) goto L_0x00ad
            long r8 = r8 * r23
            int r6 = r6 + 1
            goto L_0x00ad
        L_0x00a0:
            r14 = 1
            if (r6 != r14) goto L_0x00a7
            int r14 = (r8 > r18 ? 1 : (r8 == r18 ? 0 : -1))
            if (r14 == 0) goto L_0x00a9
        L_0x00a7:
            int r6 = r6 + 1
        L_0x00a9:
            long r8 = r8 * r23
            long r14 = (long) r12     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r8 = r8 + r14
        L_0x00ad:
            if (r16 == 0) goto L_0x00b0
            goto L_0x005b
        L_0x00b0:
            int r2 = r2 + 1
            int r3 = r3 + -1
            r10 = 48
            r14 = 57
            goto L_0x0045
        L_0x00b9:
            r10 = 101(0x65, float:1.42E-43)
            if (r12 == r10) goto L_0x00de
            r10 = 69
            if (r12 != r10) goto L_0x00c2
            goto L_0x00de
        L_0x00c2:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r1.<init>()     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            java.lang.String r2 = "Character "
            r1.append((java.lang.String) r2)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r1.append((char) r12)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            java.lang.String r2 = " is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark."
            r1.append((java.lang.String) r2)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            java.lang.String r1 = r1.toString()     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r0.<init>(r1)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            throw r0     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
        L_0x00de:
            long r1 = parseExp(r1, r2, r3)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r3 = (int) r1     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r10 = (long) r3     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r3 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r3 != 0) goto L_0x00e9
            goto L_0x00f3
        L_0x00e9:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r6 = r21
            r0.<init>(r6)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            throw r0     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
        L_0x00f1:
            r1 = r18
        L_0x00f3:
            if (r6 == 0) goto L_0x0128
            int r3 = (r1 > r18 ? 1 : (r1 == r18 ? 0 : -1))
            if (r3 == 0) goto L_0x00fd
            int r13 = r0.adjustScale(r13, r1)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
        L_0x00fd:
            if (r5 == 0) goto L_0x0100
            long r8 = -r8
        L_0x0100:
            int r1 = r4.precision     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r2 = r6 - r1
            if (r1 <= 0) goto L_0x0124
            if (r2 <= 0) goto L_0x0124
        L_0x0108:
            if (r2 <= 0) goto L_0x0124
            long r5 = (long) r13     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r10 = (long) r2     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r5 = r5 - r10
            int r13 = checkScaleNonZero(r5)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long[] r3 = LONG_TEN_POWERS_TABLE     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r2 = r3[r2]     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            java.math.RoundingMode r5 = r4.roundingMode     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r5 = r5.oldMode     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r8 = divideAndRound((long) r8, (long) r2, (int) r5)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r6 = longDigitLength(r8)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r2 = r6 - r1
            goto L_0x0108
        L_0x0124:
            r1 = r17
            goto L_0x021f
        L_0x0128:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r0.<init>(r7)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            throw r0     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
        L_0x012e:
            r6 = r9
            char[] r8 = new char[r3]     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r9 = 0
            r12 = 0
            r13 = 0
            r16 = 0
        L_0x0136:
            if (r3 <= 0) goto L_0x01ac
            char r14 = r1[r2]     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r15 = 48
            r10 = 57
            if (r14 < r15) goto L_0x0142
            if (r14 <= r10) goto L_0x0148
        L_0x0142:
            boolean r21 = java.lang.Character.isDigit((char) r14)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            if (r21 == 0) goto L_0x0177
        L_0x0148:
            if (r14 == r15) goto L_0x015f
            r10 = 10
            int r22 = java.lang.Character.digit((char) r14, (int) r10)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            if (r22 != 0) goto L_0x0153
            goto L_0x015f
        L_0x0153:
            r10 = 1
            if (r9 != r10) goto L_0x0158
            if (r12 == 0) goto L_0x015a
        L_0x0158:
            int r9 = r9 + 1
        L_0x015a:
            int r20 = r12 + 1
            r8[r12] = r14     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            goto L_0x016e
        L_0x015f:
            r10 = 1
            if (r9 != 0) goto L_0x0166
            r8[r12] = r14     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r9 = r10
            goto L_0x0170
        L_0x0166:
            if (r12 == 0) goto L_0x0170
            int r20 = r12 + 1
            r8[r12] = r14     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r9 = r9 + 1
        L_0x016e:
            r12 = r20
        L_0x0170:
            if (r16 == 0) goto L_0x0174
            int r13 = r13 + 1
        L_0x0174:
            r10 = 46
            goto L_0x017f
        L_0x0177:
            r10 = 46
            if (r14 != r10) goto L_0x018a
            if (r16 != 0) goto L_0x0184
            r16 = 1
        L_0x017f:
            int r2 = r2 + 1
            int r3 = r3 + -1
            goto L_0x0136
        L_0x0184:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r0.<init>(r11)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            throw r0     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
        L_0x018a:
            r10 = 101(0x65, float:1.42E-43)
            if (r14 == r10) goto L_0x019b
            r10 = 69
            if (r14 != r10) goto L_0x0193
            goto L_0x019b
        L_0x0193:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            java.lang.String r1 = "Character array is missing \"e\" notation exponential mark."
            r0.<init>(r1)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            throw r0     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
        L_0x019b:
            long r1 = parseExp(r1, r2, r3)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r3 = (int) r1     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r10 = (long) r3     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r3 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r3 != 0) goto L_0x01a6
            goto L_0x01ae
        L_0x01a6:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r0.<init>(r6)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            throw r0     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
        L_0x01ac:
            r1 = r18
        L_0x01ae:
            if (r9 == 0) goto L_0x0228
            int r3 = (r1 > r18 ? 1 : (r1 == r18 ? 0 : -1))
            if (r3 == 0) goto L_0x01b9
            int r1 = r0.adjustScale(r13, r1)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r13 = r1
        L_0x01b9:
            java.math.BigInteger r1 = new java.math.BigInteger     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            if (r5 == 0) goto L_0x01bf
            r2 = -1
            goto L_0x01c0
        L_0x01bf:
            r2 = 1
        L_0x01c0:
            r1.<init>((char[]) r8, (int) r2, (int) r9)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r2 = compactValFor(r1)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r5 = r4.precision     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            if (r5 <= 0) goto L_0x021d
            if (r9 <= r5) goto L_0x021d
            r6 = -9223372036854775808
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 != 0) goto L_0x01f9
        L_0x01d3:
            int r8 = r9 - r5
            if (r8 <= 0) goto L_0x01f9
            long r2 = (long) r13     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r9 = (long) r8     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r2 = r2 - r9
            int r13 = checkScaleNonZero(r2)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            java.math.RoundingMode r2 = r4.roundingMode     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r2 = r2.oldMode     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            java.math.BigInteger r1 = divideAndRoundByTenPow(r1, r8, r2)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r2 = compactValFor(r1)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 == 0) goto L_0x01f4
            int r8 = longDigitLength(r2)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r9 = r8
            goto L_0x01f9
        L_0x01f4:
            int r9 = bigDigitLength(r1)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            goto L_0x01d3
        L_0x01f9:
            int r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r6 == 0) goto L_0x021d
            int r1 = r9 - r5
            r6 = r9
            r8 = r2
        L_0x0201:
            if (r1 <= 0) goto L_0x0124
            long r2 = (long) r13     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r6 = (long) r1     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r2 = r2 - r6
            int r13 = checkScaleNonZero(r2)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long[] r2 = LONG_TEN_POWERS_TABLE     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r1 = r2[r1]     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            java.math.RoundingMode r3 = r4.roundingMode     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r3 = r3.oldMode     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            long r8 = divideAndRound((long) r8, (long) r1, (int) r3)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r6 = longDigitLength(r8)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            int r1 = r6 - r5
            goto L_0x0201
        L_0x021d:
            r6 = r9
            r8 = r2
        L_0x021f:
            r0.scale = r13
            r0.precision = r6
            r0.intCompact = r8
            r0.intVal = r1
            return
        L_0x0228:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            r0.<init>(r7)     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
            throw r0     // Catch:{ ArrayIndexOutOfBoundsException | NegativeArraySizeException -> 0x022e }
        L_0x022e:
            r0 = move-exception
            java.lang.NumberFormatException r1 = new java.lang.NumberFormatException
            r1.<init>()
            r1.initCause(r0)
            throw r1
        L_0x0238:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.String r1 = "Bad offset or len arguments for char[] input."
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigDecimal.<init>(char[], int, int, java.math.MathContext):void");
    }

    private int adjustScale(int i, long j) {
        long j2 = ((long) i) - j;
        if (j2 <= 2147483647L && j2 >= -2147483648L) {
            return (int) j2;
        }
        throw new NumberFormatException("Scale out of range.");
    }

    private static long parseExp(char[] cArr, int i, int i2) {
        int i3;
        int i4 = i + 1;
        char c = cArr[i4];
        int i5 = i2 - 1;
        boolean z = c == '-';
        if (z || c == '+') {
            i4++;
            c = cArr[i4];
            i5--;
        }
        if (i5 > 0) {
            while (i5 > 10 && (c == '0' || Character.digit(c, 10) == 0)) {
                i4++;
                c = cArr[i4];
                i5--;
            }
            if (i5 <= 10) {
                long j = 0;
                while (true) {
                    if (c < '0' || c > '9') {
                        i3 = Character.digit(c, 10);
                        if (i3 < 0) {
                            throw new NumberFormatException("Not a digit.");
                        }
                    } else {
                        i3 = c - '0';
                    }
                    j = (j * 10) + ((long) i3);
                    if (i5 == 1) {
                        return z ? -j : j;
                    }
                    i4++;
                    c = cArr[i4];
                    i5--;
                }
            } else {
                throw new NumberFormatException("Too many nonzero exponent digits.");
            }
        } else {
            throw new NumberFormatException("No exponent digits.");
        }
    }

    public BigDecimal(char[] cArr) {
        this(cArr, 0, cArr.length);
    }

    public BigDecimal(char[] cArr, MathContext mathContext) {
        this(cArr, 0, cArr.length, mathContext);
    }

    public BigDecimal(String str) {
        this(str.toCharArray(), 0, str.length());
    }

    public BigDecimal(String str, MathContext mathContext) {
        this(str.toCharArray(), 0, str.length(), mathContext);
    }

    public BigDecimal(double d) {
        this(d, MathContext.UNLIMITED);
    }

    public BigDecimal(double d, MathContext mathContext) {
        BigInteger bigInteger;
        int i;
        BigInteger multiply;
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new NumberFormatException("Infinite or NaN");
        }
        long doubleToLongBits = Double.doubleToLongBits(d);
        int i2 = (doubleToLongBits >> 63) == 0 ? 1 : -1;
        int i3 = (int) ((doubleToLongBits >> 52) & 2047);
        long j = i3 == 0 ? (doubleToLongBits & DoubleConsts.SIGNIF_BIT_MASK) << 1 : (doubleToLongBits & DoubleConsts.SIGNIF_BIT_MASK) | WifiManager.WIFI_FEATURE_TRUST_ON_FIRST_USE;
        int i4 = i3 - 1075;
        int i5 = 0;
        if (j == 0) {
            this.intVal = BigInteger.ZERO;
            this.scale = 0;
            this.intCompact = 0;
            this.precision = 1;
            return;
        }
        while ((1 & j) == 0) {
            j >>= 1;
            i4++;
        }
        long j2 = ((long) i2) * j;
        BigInteger bigInteger2 = null;
        if (i4 == 0) {
            bigInteger = j2 == Long.MIN_VALUE ? INFLATED_BIGINT : null;
            i = 0;
        } else {
            if (i4 < 0) {
                i = -i4;
                multiply = BigInteger.valueOf(5).pow(i).multiply(j2);
            } else {
                multiply = BigInteger.TWO.pow(i4).multiply(j2);
                i = 0;
            }
            j2 = compactValFor(bigInteger);
        }
        int i6 = mathContext.precision;
        if (i6 > 0) {
            int i7 = mathContext.roundingMode.oldMode;
            if (j2 == Long.MIN_VALUE) {
                i5 = bigDigitLength(bigInteger);
                while (true) {
                    int i8 = i5 - i6;
                    if (i8 <= 0) {
                        break;
                    }
                    i = checkScaleNonZero(((long) i) - ((long) i8));
                    bigInteger = divideAndRoundByTenPow(bigInteger, i8, i7);
                    j2 = compactValFor(bigInteger);
                    if (j2 != Long.MIN_VALUE) {
                        break;
                    }
                    i5 = bigDigitLength(bigInteger);
                }
            }
            if (j2 != Long.MIN_VALUE) {
                int longDigitLength = longDigitLength(j2);
                int i9 = longDigitLength - i6;
                int i10 = longDigitLength;
                while (i9 > 0) {
                    i = checkScaleNonZero(((long) i) - ((long) i9));
                    j2 = divideAndRound(j2, LONG_TEN_POWERS_TABLE[i9], mathContext.roundingMode.oldMode);
                    i10 = longDigitLength(j2);
                    i9 = i10 - i6;
                }
                this.intVal = bigInteger2;
                this.intCompact = j2;
                this.scale = i;
                this.precision = i5;
            }
        }
        bigInteger2 = bigInteger;
        this.intVal = bigInteger2;
        this.intCompact = j2;
        this.scale = i;
        this.precision = i5;
    }

    public BigDecimal(BigInteger bigInteger) {
        this.scale = 0;
        this.intVal = bigInteger;
        this.intCompact = compactValFor(bigInteger);
    }

    public BigDecimal(BigInteger bigInteger, MathContext mathContext) {
        this(bigInteger, 0, mathContext);
    }

    public BigDecimal(BigInteger bigInteger, int i) {
        this.intVal = bigInteger;
        this.intCompact = compactValFor(bigInteger);
        this.scale = i;
    }

    public BigDecimal(BigInteger bigInteger, int i, MathContext mathContext) {
        long compactValFor = compactValFor(bigInteger);
        int i2 = mathContext.precision;
        int i3 = 0;
        if (i2 > 0) {
            int i4 = mathContext.roundingMode.oldMode;
            if (compactValFor == Long.MIN_VALUE) {
                i3 = bigDigitLength(bigInteger);
                while (true) {
                    int i5 = i3 - i2;
                    if (i5 <= 0) {
                        break;
                    }
                    i = checkScaleNonZero(((long) i) - ((long) i5));
                    bigInteger = divideAndRoundByTenPow(bigInteger, i5, i4);
                    compactValFor = compactValFor(bigInteger);
                    if (compactValFor != Long.MIN_VALUE) {
                        break;
                    }
                    i3 = bigDigitLength(bigInteger);
                }
            }
            if (compactValFor != Long.MIN_VALUE) {
                int longDigitLength = longDigitLength(compactValFor);
                while (true) {
                    int i6 = longDigitLength - i2;
                    if (i6 <= 0) {
                        break;
                    }
                    i = checkScaleNonZero(((long) i) - ((long) i6));
                    compactValFor = divideAndRound(compactValFor, LONG_TEN_POWERS_TABLE[i6], i4);
                    longDigitLength = longDigitLength(compactValFor);
                }
                i3 = longDigitLength;
                bigInteger = null;
            }
        }
        this.intVal = bigInteger;
        this.intCompact = compactValFor;
        this.scale = i;
        this.precision = i3;
    }

    public BigDecimal(int i) {
        this.intCompact = (long) i;
        this.scale = 0;
        this.intVal = null;
    }

    public BigDecimal(int i, MathContext mathContext) {
        int i2;
        int i3 = mathContext.precision;
        long j = (long) i;
        int i4 = 0;
        if (i3 > 0) {
            i2 = longDigitLength(j);
            while (true) {
                int i5 = i2 - i3;
                if (i5 <= 0) {
                    break;
                }
                i4 = checkScaleNonZero(((long) i4) - ((long) i5));
                j = divideAndRound(j, LONG_TEN_POWERS_TABLE[i5], mathContext.roundingMode.oldMode);
                i2 = longDigitLength(j);
            }
        } else {
            i2 = 0;
        }
        this.intVal = null;
        this.intCompact = j;
        this.scale = i4;
        this.precision = i2;
    }

    public BigDecimal(long j) {
        this.intCompact = j;
        this.intVal = j == Long.MIN_VALUE ? INFLATED_BIGINT : null;
        this.scale = 0;
    }

    public BigDecimal(long j, MathContext mathContext) {
        int i;
        int i2;
        int i3;
        int i4 = mathContext.precision;
        int i5 = mathContext.roundingMode.oldMode;
        int i6 = (j > Long.MIN_VALUE ? 1 : (j == Long.MIN_VALUE ? 0 : -1));
        BigInteger bigInteger = null;
        BigInteger bigInteger2 = i6 == 0 ? INFLATED_BIGINT : null;
        int i7 = 0;
        if (i4 > 0) {
            if (i6 == 0) {
                int i8 = 19 - i4;
                int i9 = 19;
                while (i8 > 0) {
                    i7 = checkScaleNonZero(((long) i7) - ((long) i8));
                    bigInteger2 = divideAndRoundByTenPow(bigInteger2, i8, i5);
                    j = compactValFor(bigInteger2);
                    if (j != Long.MIN_VALUE) {
                        break;
                    }
                    i9 = bigDigitLength(bigInteger2);
                    i8 = i9 - i4;
                }
                i3 = i7;
                i7 = i9;
            } else {
                i3 = 0;
            }
            if (j != Long.MIN_VALUE) {
                int longDigitLength = longDigitLength(j);
                int i10 = longDigitLength - i4;
                i2 = longDigitLength;
                while (i10 > 0) {
                    i3 = checkScaleNonZero(((long) i3) - ((long) i10));
                    j = divideAndRound(j, LONG_TEN_POWERS_TABLE[i10], mathContext.roundingMode.oldMode);
                    i2 = longDigitLength(j);
                    i10 = i2 - i4;
                }
            } else {
                bigInteger = bigInteger2;
            }
            i = i2;
            i7 = i3;
        } else {
            bigInteger = bigInteger2;
            i = 0;
        }
        this.intVal = bigInteger;
        this.intCompact = j;
        this.scale = i7;
        this.precision = i;
    }

    public static BigDecimal valueOf(long j, int i) {
        if (i == 0) {
            return valueOf(j);
        }
        if (j == 0) {
            return zeroValueOf(i);
        }
        return new BigDecimal(j == Long.MIN_VALUE ? INFLATED_BIGINT : null, j, i, 0);
    }

    public static BigDecimal valueOf(long j) {
        if (j >= 0) {
            BigDecimal[] bigDecimalArr = ZERO_THROUGH_TEN;
            if (j < ((long) bigDecimalArr.length)) {
                return bigDecimalArr[(int) j];
            }
        }
        if (j != Long.MIN_VALUE) {
            return new BigDecimal((BigInteger) null, j, 0, 0);
        }
        return new BigDecimal(INFLATED_BIGINT, j, 0, 0);
    }

    static BigDecimal valueOf(long j, int i, int i2) {
        if (i == 0 && j >= 0) {
            BigDecimal[] bigDecimalArr = ZERO_THROUGH_TEN;
            if (j < ((long) bigDecimalArr.length)) {
                return bigDecimalArr[(int) j];
            }
        }
        if (j == 0) {
            return zeroValueOf(i);
        }
        return new BigDecimal(j == Long.MIN_VALUE ? INFLATED_BIGINT : null, j, i, i2);
    }

    static BigDecimal valueOf(BigInteger bigInteger, int i, int i2) {
        long compactValFor = compactValFor(bigInteger);
        int i3 = (compactValFor > 0 ? 1 : (compactValFor == 0 ? 0 : -1));
        if (i3 == 0) {
            return zeroValueOf(i);
        }
        if (i == 0 && i3 >= 0) {
            BigDecimal[] bigDecimalArr = ZERO_THROUGH_TEN;
            if (compactValFor < ((long) bigDecimalArr.length)) {
                return bigDecimalArr[(int) compactValFor];
            }
        }
        return new BigDecimal(bigInteger, compactValFor, i, i2);
    }

    static BigDecimal zeroValueOf(int i) {
        if (i >= 0) {
            BigDecimal[] bigDecimalArr = ZERO_SCALED_BY;
            if (i < bigDecimalArr.length) {
                return bigDecimalArr[i];
            }
        }
        return new BigDecimal(BigInteger.ZERO, 0, i, 1);
    }

    public static BigDecimal valueOf(double d) {
        return new BigDecimal(Double.toString(d));
    }

    public BigDecimal add(BigDecimal bigDecimal) {
        long j = this.intCompact;
        if (j != Long.MIN_VALUE) {
            long j2 = bigDecimal.intCompact;
            if (j2 != Long.MIN_VALUE) {
                return add(j, this.scale, j2, bigDecimal.scale);
            }
            return add(j, this.scale, bigDecimal.intVal, bigDecimal.scale);
        }
        long j3 = bigDecimal.intCompact;
        if (j3 != Long.MIN_VALUE) {
            return add(j3, bigDecimal.scale, this.intVal, this.scale);
        }
        return add(this.intVal, this.scale, bigDecimal.intVal, bigDecimal.scale);
    }

    public BigDecimal add(BigDecimal bigDecimal, MathContext mathContext) {
        BigDecimal bigDecimal2;
        BigDecimal bigDecimal3;
        if (mathContext.precision == 0) {
            return add(bigDecimal);
        }
        boolean z = signum() == 0;
        boolean z2 = bigDecimal.signum() == 0;
        if (z || z2) {
            int max = Math.max(scale(), bigDecimal.scale());
            if (z && z2) {
                return zeroValueOf(max);
            }
            BigDecimal doRound = z ? doRound(bigDecimal, mathContext) : doRound(this, mathContext);
            if (doRound.scale() == max) {
                return doRound;
            }
            if (doRound.scale() > max) {
                return stripZerosToMatchScale(doRound.intVal, doRound.intCompact, doRound.scale, max);
            }
            int precision2 = mathContext.precision - doRound.precision();
            if (precision2 >= max - doRound.scale()) {
                return doRound.setScale(max);
            }
            return doRound.setScale(doRound.scale() + precision2);
        }
        long j = ((long) this.scale) - ((long) bigDecimal.scale);
        if (j != 0) {
            BigDecimal[] preAlign = preAlign(this, bigDecimal, j, mathContext);
            matchScale(preAlign);
            bigDecimal2 = preAlign[0];
            bigDecimal3 = preAlign[1];
        } else {
            bigDecimal2 = this;
            bigDecimal3 = bigDecimal;
        }
        return doRound(bigDecimal2.inflated().add(bigDecimal3.inflated()), bigDecimal2.scale, mathContext);
    }

    private BigDecimal[] preAlign(BigDecimal bigDecimal, BigDecimal bigDecimal2, long j, MathContext mathContext) {
        if (j >= 0) {
            BigDecimal bigDecimal3 = bigDecimal2;
            bigDecimal2 = bigDecimal;
            bigDecimal = bigDecimal3;
        }
        long precision2 = (((long) bigDecimal.scale) - ((long) bigDecimal.precision())) + ((long) mathContext.precision);
        long precision3 = (((long) bigDecimal2.scale) - ((long) bigDecimal2.precision())) + 1;
        if (precision3 > ((long) (bigDecimal.scale + 2)) && precision3 > 2 + precision2) {
            bigDecimal2 = valueOf((long) bigDecimal2.signum(), checkScale(Math.max((long) bigDecimal.scale, precision2) + 3));
        }
        return new BigDecimal[]{bigDecimal, bigDecimal2};
    }

    public BigDecimal subtract(BigDecimal bigDecimal) {
        long j = this.intCompact;
        if (j != Long.MIN_VALUE) {
            long j2 = bigDecimal.intCompact;
            if (j2 != Long.MIN_VALUE) {
                return add(j, this.scale, -j2, bigDecimal.scale);
            }
            return add(j, this.scale, bigDecimal.intVal.negate(), bigDecimal.scale);
        }
        long j3 = bigDecimal.intCompact;
        if (j3 != Long.MIN_VALUE) {
            return add(-j3, bigDecimal.scale, this.intVal, this.scale);
        }
        return add(this.intVal, this.scale, bigDecimal.intVal.negate(), bigDecimal.scale);
    }

    public BigDecimal subtract(BigDecimal bigDecimal, MathContext mathContext) {
        if (mathContext.precision == 0) {
            return subtract(bigDecimal);
        }
        return add(bigDecimal.negate(), mathContext);
    }

    public BigDecimal multiply(BigDecimal bigDecimal) {
        int checkScale = checkScale(((long) this.scale) + ((long) bigDecimal.scale));
        long j = this.intCompact;
        if (j != Long.MIN_VALUE) {
            long j2 = bigDecimal.intCompact;
            if (j2 != Long.MIN_VALUE) {
                return multiply(j, j2, checkScale);
            }
            return multiply(j, bigDecimal.intVal, checkScale);
        }
        long j3 = bigDecimal.intCompact;
        if (j3 != Long.MIN_VALUE) {
            return multiply(j3, this.intVal, checkScale);
        }
        return multiply(this.intVal, bigDecimal.intVal, checkScale);
    }

    public BigDecimal multiply(BigDecimal bigDecimal, MathContext mathContext) {
        if (mathContext.precision == 0) {
            return multiply(bigDecimal);
        }
        int checkScale = checkScale(((long) this.scale) + ((long) bigDecimal.scale));
        long j = this.intCompact;
        if (j != Long.MIN_VALUE) {
            long j2 = bigDecimal.intCompact;
            if (j2 != Long.MIN_VALUE) {
                return multiplyAndRound(j, j2, checkScale, mathContext);
            }
            return multiplyAndRound(j, bigDecimal.intVal, checkScale, mathContext);
        }
        long j3 = bigDecimal.intCompact;
        if (j3 != Long.MIN_VALUE) {
            return multiplyAndRound(j3, this.intVal, checkScale, mathContext);
        }
        return multiplyAndRound(this.intVal, bigDecimal.intVal, checkScale, mathContext);
    }

    @Deprecated(since = "9")
    public BigDecimal divide(BigDecimal bigDecimal, int i, int i2) {
        if (i2 < 0 || i2 > 7) {
            throw new IllegalArgumentException("Invalid rounding mode");
        }
        long j = this.intCompact;
        if (j != Long.MIN_VALUE) {
            long j2 = bigDecimal.intCompact;
            if (j2 != Long.MIN_VALUE) {
                return divide(j, this.scale, j2, bigDecimal.scale, i, i2);
            }
            return divide(j, this.scale, bigDecimal.intVal, bigDecimal.scale, i, i2);
        }
        long j3 = bigDecimal.intCompact;
        if (j3 == Long.MIN_VALUE) {
            return divide(this.intVal, this.scale, bigDecimal.intVal, bigDecimal.scale, i, i2);
        }
        return divide(this.intVal, this.scale, j3, bigDecimal.scale, i, i2);
    }

    public BigDecimal divide(BigDecimal bigDecimal, int i, RoundingMode roundingMode) {
        return divide(bigDecimal, i, roundingMode.oldMode);
    }

    @Deprecated(since = "9")
    public BigDecimal divide(BigDecimal bigDecimal, int i) {
        return divide(bigDecimal, this.scale, i);
    }

    public BigDecimal divide(BigDecimal bigDecimal, RoundingMode roundingMode) {
        return divide(bigDecimal, this.scale, roundingMode.oldMode);
    }

    public BigDecimal divide(BigDecimal bigDecimal) {
        if (bigDecimal.signum() != 0) {
            int saturateLong = saturateLong(((long) this.scale) - ((long) bigDecimal.scale));
            if (signum() == 0) {
                return zeroValueOf(saturateLong);
            }
            try {
                BigDecimal divide = divide(bigDecimal, new MathContext((int) Math.min(((long) precision()) + ((long) Math.ceil((((double) bigDecimal.precision()) * 10.0d) / 3.0d)), 2147483647L), RoundingMode.UNNECESSARY));
                if (saturateLong > divide.scale()) {
                    return divide.setScale(saturateLong, 7);
                }
                return divide;
            } catch (ArithmeticException unused) {
                throw new ArithmeticException("Non-terminating decimal expansion; no exact representable decimal result.");
            }
        } else if (signum() == 0) {
            throw new ArithmeticException("Division undefined");
        } else {
            throw new ArithmeticException("Division by zero");
        }
    }

    public BigDecimal divide(BigDecimal bigDecimal, MathContext mathContext) {
        if (mathContext.precision == 0) {
            return divide(bigDecimal);
        }
        long j = ((long) this.scale) - ((long) bigDecimal.scale);
        if (bigDecimal.signum() == 0) {
            if (signum() == 0) {
                throw new ArithmeticException("Division undefined");
            }
            throw new ArithmeticException("Division by zero");
        } else if (signum() == 0) {
            return zeroValueOf(saturateLong(j));
        } else {
            int precision2 = precision();
            int precision3 = bigDecimal.precision();
            long j2 = this.intCompact;
            if (j2 != Long.MIN_VALUE) {
                long j3 = bigDecimal.intCompact;
                if (j3 != Long.MIN_VALUE) {
                    return divide(j2, precision2, j3, precision3, j, mathContext);
                }
                return divide(j2, precision2, bigDecimal.intVal, precision3, j, mathContext);
            }
            long j4 = bigDecimal.intCompact;
            if (j4 != Long.MIN_VALUE) {
                return divide(this.intVal, precision2, j4, precision3, j, mathContext);
            }
            return divide(this.intVal, precision2, bigDecimal.intVal, precision3, j, mathContext);
        }
    }

    public BigDecimal divideToIntegralValue(BigDecimal bigDecimal) {
        int saturateLong = saturateLong(((long) this.scale) - ((long) bigDecimal.scale));
        if (compareMagnitude(bigDecimal) < 0) {
            return zeroValueOf(saturateLong);
        }
        if (signum() == 0 && bigDecimal.signum() != 0) {
            return setScale(saturateLong, 7);
        }
        BigDecimal divide = divide(bigDecimal, new MathContext((int) Math.min(((long) precision()) + ((long) Math.ceil((((double) bigDecimal.precision()) * 10.0d) / 3.0d)) + Math.abs(((long) scale()) - ((long) bigDecimal.scale())) + 2, 2147483647L), RoundingMode.DOWN));
        if (divide.scale > 0) {
            BigDecimal scale2 = divide.setScale(0, RoundingMode.DOWN);
            divide = stripZerosToMatchScale(scale2.intVal, scale2.intCompact, scale2.scale, saturateLong);
        }
        return divide.scale < saturateLong ? divide.setScale(saturateLong, 7) : divide;
    }

    public BigDecimal divideToIntegralValue(BigDecimal bigDecimal, MathContext mathContext) {
        int precision2;
        if (mathContext.precision == 0 || compareMagnitude(bigDecimal) < 0) {
            return divideToIntegralValue(bigDecimal);
        }
        int saturateLong = saturateLong(((long) this.scale) - ((long) bigDecimal.scale));
        BigDecimal divide = divide(bigDecimal, new MathContext(mathContext.precision, RoundingMode.DOWN));
        if (divide.scale() < 0) {
            if (subtract(divide.multiply(bigDecimal)).compareMagnitude(bigDecimal) >= 0) {
                throw new ArithmeticException("Division impossible");
            }
        } else if (divide.scale() > 0) {
            divide = divide.setScale(0, RoundingMode.DOWN);
        }
        if (saturateLong <= divide.scale() || (precision2 = mathContext.precision - divide.precision()) <= 0) {
            return stripZerosToMatchScale(divide.intVal, divide.intCompact, divide.scale, saturateLong);
        }
        return divide.setScale(divide.scale() + Math.min(precision2, saturateLong - divide.scale));
    }

    public BigDecimal remainder(BigDecimal bigDecimal) {
        return divideAndRemainder(bigDecimal)[1];
    }

    public BigDecimal remainder(BigDecimal bigDecimal, MathContext mathContext) {
        return divideAndRemainder(bigDecimal, mathContext)[1];
    }

    public BigDecimal[] divideAndRemainder(BigDecimal bigDecimal) {
        BigDecimal divideToIntegralValue = divideToIntegralValue(bigDecimal);
        return new BigDecimal[]{divideToIntegralValue, subtract(divideToIntegralValue.multiply(bigDecimal))};
    }

    public BigDecimal[] divideAndRemainder(BigDecimal bigDecimal, MathContext mathContext) {
        if (mathContext.precision == 0) {
            return divideAndRemainder(bigDecimal);
        }
        BigDecimal divideToIntegralValue = divideToIntegralValue(bigDecimal, mathContext);
        return new BigDecimal[]{divideToIntegralValue, subtract(divideToIntegralValue.multiply(bigDecimal))};
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x0110  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0137  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0147  */
    /* JADX WARNING: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.math.BigDecimal sqrt(java.math.MathContext r15) {
        /*
            r14 = this;
            int r0 = r14.signum()
            r1 = 0
            r3 = 2
            r4 = 1
            if (r0 != r4) goto L_0x014f
            int r0 = r14.scale()
            int r5 = r0 / 2
            java.math.BigDecimal r6 = valueOf(r1, r5)
            java.math.BigDecimal r0 = r14.stripTrailingZeros()
            int r1 = r0.scale()
            boolean r2 = r0.isPowerOfTen()
            if (r2 == 0) goto L_0x0038
            int r2 = r1 % 2
            if (r2 != 0) goto L_0x0038
            r7 = 1
            int r1 = r1 / r3
            java.math.BigDecimal r14 = valueOf(r7, r1)
            int r0 = r14.scale()
            if (r0 == r5) goto L_0x0037
            java.math.BigDecimal r14 = r14.add((java.math.BigDecimal) r6, (java.math.MathContext) r15)
        L_0x0037:
            return r14
        L_0x0038:
            int r1 = r0.scale()
            int r2 = r0.precision()
            int r1 = r1 - r2
            int r1 = r1 + r4
            int r2 = r1 % 2
            if (r2 != 0) goto L_0x0047
            goto L_0x0049
        L_0x0047:
            int r1 = r1 + -1
        L_0x0049:
            r7 = r1
            java.math.BigDecimal r8 = r0.scaleByPowerOfTen(r7)
            java.math.BigDecimal r1 = new java.math.BigDecimal
            double r9 = r8.doubleValue()
            double r9 = java.lang.Math.sqrt(r9)
            r1.<init>((double) r9)
            int r9 = r15.getPrecision()
            if (r9 != 0) goto L_0x0069
            int r0 = r0.precision()
            int r0 = r0 / r3
            int r0 = r0 + r4
        L_0x0067:
            r4 = r0
            goto L_0x0086
        L_0x0069:
            int[] r0 = java.math.BigDecimal.C43162.$SwitchMap$java$math$RoundingMode
            java.math.RoundingMode r2 = r15.getRoundingMode()
            int r2 = r2.ordinal()
            r0 = r0[r2]
            if (r0 == r4) goto L_0x007e
            if (r0 == r3) goto L_0x007e
            r2 = 3
            if (r0 == r2) goto L_0x007e
            r4 = r9
            goto L_0x0086
        L_0x007e:
            int r0 = r9 * 2
            if (r0 >= 0) goto L_0x0067
            r0 = 2147483645(0x7ffffffd, float:NaN)
            goto L_0x0067
        L_0x0086:
            int r10 = r8.precision()
            r0 = 15
        L_0x008c:
            int r2 = r4 + 2
            int r11 = java.lang.Math.max((int) r0, (int) r2)
            int r11 = java.lang.Math.max((int) r11, (int) r10)
            java.math.MathContext r12 = new java.math.MathContext
            java.math.RoundingMode r13 = java.math.RoundingMode.HALF_EVEN
            r12.<init>(r11, r13)
            java.math.BigDecimal r11 = ONE_HALF
            java.math.BigDecimal r13 = r8.divide((java.math.BigDecimal) r1, (java.math.MathContext) r12)
            java.math.BigDecimal r1 = r1.add((java.math.BigDecimal) r13, (java.math.MathContext) r12)
            java.math.BigDecimal r1 = r11.multiply(r1)
            int r0 = r0 * r3
            if (r0 < r2) goto L_0x008c
            java.math.RoundingMode r0 = r15.getRoundingMode()
            java.math.RoundingMode r2 = java.math.RoundingMode.UNNECESSARY
            if (r0 == r2) goto L_0x010c
            if (r9 != 0) goto L_0x00b9
            goto L_0x010c
        L_0x00b9:
            int r2 = -r7
            int r2 = r2 / r3
            java.math.BigDecimal r2 = r1.scaleByPowerOfTen(r2)
            java.math.BigDecimal r15 = r2.round(r15)
            int[] r2 = java.math.BigDecimal.C43162.$SwitchMap$java$math$RoundingMode
            int r0 = r0.ordinal()
            r0 = r2[r0]
            r2 = 4
            if (r0 == r2) goto L_0x00eb
            r2 = 5
            if (r0 == r2) goto L_0x00eb
            r1 = 6
            if (r0 == r1) goto L_0x00d8
            r1 = 7
            if (r0 == r1) goto L_0x00d8
            goto L_0x0131
        L_0x00d8:
            java.math.BigDecimal r0 = r15.square()
            int r14 = r0.compareTo((java.math.BigDecimal) r14)
            if (r14 >= 0) goto L_0x0131
            java.math.BigDecimal r14 = r15.ulp()
            java.math.BigDecimal r15 = r15.add(r14)
            goto L_0x0131
        L_0x00eb:
            java.math.BigDecimal r0 = r15.square()
            int r14 = r0.compareTo((java.math.BigDecimal) r14)
            if (r14 <= 0) goto L_0x0131
            java.math.BigDecimal r14 = r15.ulp()
            java.math.BigDecimal r0 = ONE
            int r0 = r1.compareTo((java.math.BigDecimal) r0)
            if (r0 != 0) goto L_0x0107
            java.math.BigDecimal r0 = ONE_TENTH
            java.math.BigDecimal r14 = r14.multiply(r0)
        L_0x0107:
            java.math.BigDecimal r15 = r15.subtract(r14)
            goto L_0x0131
        L_0x010c:
            java.math.RoundingMode r15 = java.math.RoundingMode.UNNECESSARY
            if (r0 != r15) goto L_0x0112
            java.math.RoundingMode r0 = java.math.RoundingMode.DOWN
        L_0x0112:
            java.math.MathContext r15 = new java.math.MathContext
            r15.<init>(r4, r0)
            int r0 = -r7
            int r0 = r0 / r3
            java.math.BigDecimal r0 = r1.scaleByPowerOfTen(r0)
            java.math.BigDecimal r15 = r0.round(r15)
            java.math.BigDecimal r0 = r15.square()
            java.math.BigDecimal r14 = r14.subtract(r0)
            java.math.BigDecimal r0 = ZERO
            int r14 = r14.compareTo((java.math.BigDecimal) r0)
            if (r14 != 0) goto L_0x0147
        L_0x0131:
            int r14 = r15.scale()
            if (r14 == r5) goto L_0x0146
            java.math.BigDecimal r14 = r15.stripTrailingZeros()
            java.math.MathContext r15 = new java.math.MathContext
            java.math.RoundingMode r0 = java.math.RoundingMode.UNNECESSARY
            r15.<init>(r9, r0)
            java.math.BigDecimal r15 = r14.add((java.math.BigDecimal) r6, (java.math.MathContext) r15)
        L_0x0146:
            return r15
        L_0x0147:
            java.lang.ArithmeticException r14 = new java.lang.ArithmeticException
            java.lang.String r15 = "Computed square root not exact."
            r14.<init>(r15)
            throw r14
        L_0x014f:
            r15 = -1
            if (r0 == r15) goto L_0x0166
            if (r0 != 0) goto L_0x015e
            int r14 = r14.scale()
            int r14 = r14 / r3
            java.math.BigDecimal r14 = valueOf(r1, r14)
            return r14
        L_0x015e:
            java.lang.AssertionError r14 = new java.lang.AssertionError
            java.lang.String r15 = "Bad value from signum"
            r14.<init>((java.lang.Object) r15)
            throw r14
        L_0x0166:
            java.lang.ArithmeticException r14 = new java.lang.ArithmeticException
            java.lang.String r15 = "Attempted square root of negative BigDecimal"
            r14.<init>(r15)
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigDecimal.sqrt(java.math.MathContext):java.math.BigDecimal");
    }

    /* renamed from: java.math.BigDecimal$2 */
    static /* synthetic */ class C43162 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                java.math.RoundingMode[] r0 = java.math.RoundingMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$math$RoundingMode = r0
                java.math.RoundingMode r1 = java.math.RoundingMode.HALF_UP     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x001d }
                java.math.RoundingMode r1 = java.math.RoundingMode.HALF_DOWN     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.math.RoundingMode r1 = java.math.RoundingMode.HALF_EVEN     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.math.RoundingMode r1 = java.math.RoundingMode.DOWN     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x003e }
                java.math.RoundingMode r1 = java.math.RoundingMode.FLOOR     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.math.RoundingMode r1 = java.math.RoundingMode.UP     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x0054 }
                java.math.RoundingMode r1 = java.math.RoundingMode.CEILING     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.math.BigDecimal.C43162.<clinit>():void");
        }
    }

    private BigDecimal square() {
        return multiply(this);
    }

    private boolean isPowerOfTen() {
        return BigInteger.ONE.equals(unscaledValue());
    }

    private boolean squareRootResultAssertions(BigDecimal bigDecimal, MathContext mathContext) {
        if (bigDecimal.signum() == 0) {
            return squareRootZeroResultAssertions(bigDecimal, mathContext);
        }
        RoundingMode roundingMode = mathContext.getRoundingMode();
        BigDecimal ulp = bigDecimal.ulp();
        BigDecimal add = bigDecimal.add(ulp);
        if (bigDecimal.isPowerOfTen()) {
            ulp = ulp.divide(TEN);
        }
        BigDecimal subtract = bigDecimal.subtract(ulp);
        switch (C43162.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
            case 2:
            case 3:
                BigDecimal abs = bigDecimal.square().subtract(this).abs();
                BigDecimal subtract2 = add.square().subtract(this);
                BigDecimal subtract3 = subtract(subtract.square());
                abs.compareTo(subtract2);
                abs.compareTo(subtract3);
                return true;
            default:
                return true;
        }
    }

    private boolean squareRootZeroResultAssertions(BigDecimal bigDecimal, MathContext mathContext) {
        return compareTo(ZERO) == 0;
    }

    public BigDecimal pow(int i) {
        if (i < 0 || i > 999999999) {
            throw new ArithmeticException("Invalid operation");
        }
        return new BigDecimal(inflated().pow(i), checkScale(((long) this.scale) * ((long) i)));
    }

    public BigDecimal pow(int i, MathContext mathContext) {
        MathContext mathContext2;
        if (mathContext.precision == 0) {
            return pow(i);
        }
        if (i < -999999999 || i > 999999999) {
            throw new ArithmeticException("Invalid operation");
        } else if (i == 0) {
            return ONE;
        } else {
            int abs = Math.abs(i);
            if (mathContext.precision > 0) {
                int longDigitLength = longDigitLength((long) abs);
                if (longDigitLength <= mathContext.precision) {
                    mathContext2 = new MathContext(mathContext.precision + longDigitLength + 1, mathContext.roundingMode);
                } else {
                    throw new ArithmeticException("Invalid operation");
                }
            } else {
                mathContext2 = mathContext;
            }
            BigDecimal bigDecimal = ONE;
            boolean z = false;
            int i2 = 1;
            while (true) {
                abs += abs;
                if (abs < 0) {
                    bigDecimal = bigDecimal.multiply(this, mathContext2);
                    z = true;
                }
                if (i2 == 31) {
                    break;
                }
                if (z) {
                    bigDecimal = bigDecimal.multiply(bigDecimal, mathContext2);
                }
                i2++;
            }
            if (i < 0) {
                bigDecimal = ONE.divide(bigDecimal, mathContext2);
            }
            return doRound(bigDecimal, mathContext);
        }
    }

    public BigDecimal abs() {
        return signum() < 0 ? negate() : this;
    }

    public BigDecimal abs(MathContext mathContext) {
        return signum() < 0 ? negate(mathContext) : plus(mathContext);
    }

    public BigDecimal negate() {
        long j = this.intCompact;
        if (j == Long.MIN_VALUE) {
            return new BigDecimal(this.intVal.negate(), Long.MIN_VALUE, this.scale, this.precision);
        }
        return valueOf(-j, this.scale, this.precision);
    }

    public BigDecimal negate(MathContext mathContext) {
        return negate().plus(mathContext);
    }

    public BigDecimal plus(MathContext mathContext) {
        if (mathContext.precision == 0) {
            return this;
        }
        return doRound(this, mathContext);
    }

    public int signum() {
        long j = this.intCompact;
        if (j != Long.MIN_VALUE) {
            return Long.signum(j);
        }
        return this.intVal.signum();
    }

    public int scale() {
        return this.scale;
    }

    public int precision() {
        int i = this.precision;
        if (i == 0) {
            long j = this.intCompact;
            if (j != Long.MIN_VALUE) {
                i = longDigitLength(j);
            } else {
                i = bigDigitLength(this.intVal);
            }
            this.precision = i;
        }
        return i;
    }

    public BigInteger unscaledValue() {
        return inflated();
    }

    public BigDecimal round(MathContext mathContext) {
        return plus(mathContext);
    }

    public BigDecimal setScale(int i, RoundingMode roundingMode) {
        return setScale(i, roundingMode.oldMode);
    }

    @Deprecated(since = "9")
    public BigDecimal setScale(int i, int i2) {
        if (i2 < 0 || i2 > 7) {
            throw new IllegalArgumentException("Invalid rounding mode");
        }
        int i3 = this.scale;
        if (i == i3) {
            return this;
        }
        if (signum() == 0) {
            return zeroValueOf(i);
        }
        long j = this.intCompact;
        int i4 = 0;
        if (j != Long.MIN_VALUE) {
            if (i > i3) {
                int checkScale = checkScale(((long) i) - ((long) i3));
                long longMultiplyPowerTen = longMultiplyPowerTen(j, checkScale);
                if (longMultiplyPowerTen != Long.MIN_VALUE) {
                    return valueOf(longMultiplyPowerTen, i);
                }
                BigInteger bigMultiplyPowerTen = bigMultiplyPowerTen(checkScale);
                int i5 = this.precision;
                if (i5 > 0) {
                    i4 = i5 + checkScale;
                }
                return new BigDecimal(bigMultiplyPowerTen, Long.MIN_VALUE, i, i4);
            }
            int checkScale2 = checkScale(((long) i3) - ((long) i));
            long[] jArr = LONG_TEN_POWERS_TABLE;
            if (checkScale2 >= jArr.length) {
                return divideAndRound(inflated(), bigTenToThe(checkScale2), i, i2, i);
            }
            return divideAndRound(j, jArr[checkScale2], i, i2, i);
        } else if (i > i3) {
            int checkScale3 = checkScale(((long) i) - ((long) i3));
            BigInteger bigMultiplyPowerTen2 = bigMultiplyPowerTen(this.intVal, checkScale3);
            int i6 = this.precision;
            if (i6 > 0) {
                i4 = i6 + checkScale3;
            }
            return new BigDecimal(bigMultiplyPowerTen2, Long.MIN_VALUE, i, i4);
        } else {
            int checkScale4 = checkScale(((long) i3) - ((long) i));
            long[] jArr2 = LONG_TEN_POWERS_TABLE;
            if (checkScale4 < jArr2.length) {
                return divideAndRound(this.intVal, jArr2[checkScale4], i, i2, i);
            }
            return divideAndRound(this.intVal, bigTenToThe(checkScale4), i, i2, i);
        }
    }

    public BigDecimal setScale(int i) {
        return setScale(i, 7);
    }

    public BigDecimal movePointLeft(int i) {
        BigDecimal bigDecimal = new BigDecimal(this.intVal, this.intCompact, checkScale(((long) this.scale) + ((long) i)), 0);
        return bigDecimal.scale < 0 ? bigDecimal.setScale(0, 7) : bigDecimal;
    }

    public BigDecimal movePointRight(int i) {
        BigDecimal bigDecimal = new BigDecimal(this.intVal, this.intCompact, checkScale(((long) this.scale) - ((long) i)), 0);
        return bigDecimal.scale < 0 ? bigDecimal.setScale(0, 7) : bigDecimal;
    }

    public BigDecimal scaleByPowerOfTen(int i) {
        return new BigDecimal(this.intVal, this.intCompact, checkScale(((long) this.scale) - ((long) i)), this.precision);
    }

    public BigDecimal stripTrailingZeros() {
        BigInteger bigInteger;
        if (this.intCompact == 0 || ((bigInteger = this.intVal) != null && bigInteger.signum() == 0)) {
            return ZERO;
        }
        long j = this.intCompact;
        if (j != Long.MIN_VALUE) {
            return createAndStripZerosToMatchScale(j, this.scale, Long.MIN_VALUE);
        }
        return createAndStripZerosToMatchScale(this.intVal, this.scale, Long.MIN_VALUE);
    }

    public int compareTo(BigDecimal bigDecimal) {
        if (this.scale == bigDecimal.scale) {
            long j = this.intCompact;
            long j2 = bigDecimal.intCompact;
            if (!(j == Long.MIN_VALUE || j2 == Long.MIN_VALUE)) {
                int i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
                if (i == 0) {
                    return 0;
                }
                if (i > 0) {
                    return 1;
                }
                return -1;
            }
        }
        int signum = signum();
        int signum2 = bigDecimal.signum();
        if (signum != signum2) {
            if (signum > signum2) {
                return 1;
            }
            return -1;
        } else if (signum == 0) {
            return 0;
        } else {
            int compareMagnitude = compareMagnitude(bigDecimal);
            return signum > 0 ? compareMagnitude : -compareMagnitude;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005c, code lost:
        if (r4 == Long.MIN_VALUE) goto L_0x005e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0088, code lost:
        if (r2 == Long.MIN_VALUE) goto L_0x008a;
     */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00a7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int compareMagnitude(java.math.BigDecimal r19) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            long r2 = r1.intCompact
            long r4 = r0.intCompact
            r6 = 0
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            r9 = -1
            if (r8 != 0) goto L_0x0015
            int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x0014
            r9 = 0
        L_0x0014:
            return r9
        L_0x0015:
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            r10 = 1
            if (r8 != 0) goto L_0x001b
            return r10
        L_0x001b:
            int r8 = r0.scale
            long r11 = (long) r8
            int r8 = r1.scale
            long r13 = (long) r8
            long r11 = r11 - r13
            int r6 = (r11 > r6 ? 1 : (r11 == r6 ? 0 : -1))
            if (r6 == 0) goto L_0x006f
            int r13 = r18.precision()
            long r13 = (long) r13
            int r15 = r0.scale
            long r7 = (long) r15
            long r13 = r13 - r7
            int r7 = r19.precision()
            long r7 = (long) r7
            int r15 = r1.scale
            r16 = r11
            long r10 = (long) r15
            long r7 = r7 - r10
            int r7 = (r13 > r7 ? 1 : (r13 == r7 ? 0 : -1))
            if (r7 >= 0) goto L_0x003f
            return r9
        L_0x003f:
            if (r7 <= 0) goto L_0x0043
            r7 = 1
            return r7
        L_0x0043:
            if (r6 >= 0) goto L_0x0072
            r6 = -2147483648(0xffffffff80000000, double:NaN)
            int r6 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1))
            if (r6 <= 0) goto L_0x006f
            r6 = -9223372036854775808
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            r11 = r16
            if (r8 == 0) goto L_0x005e
            long r13 = -r11
            int r8 = (int) r13
            long r4 = longMultiplyPowerTen(r4, r8)
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r8 != 0) goto L_0x009a
        L_0x005e:
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 != 0) goto L_0x006f
            long r2 = -r11
            int r2 = (int) r2
            java.math.BigInteger r0 = r0.bigMultiplyPowerTen(r2)
            java.math.BigInteger r1 = r1.intVal
            int r0 = r0.compareMagnitude((java.math.BigInteger) r1)
            return r0
        L_0x006f:
            r6 = -9223372036854775808
            goto L_0x009a
        L_0x0072:
            r11 = r16
            r6 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r6 = (r11 > r6 ? 1 : (r11 == r6 ? 0 : -1))
            if (r6 > 0) goto L_0x006f
            r6 = -9223372036854775808
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 == 0) goto L_0x008a
            int r8 = (int) r11
            long r2 = longMultiplyPowerTen(r2, r8)
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 != 0) goto L_0x009a
        L_0x008a:
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r8 != 0) goto L_0x009a
            int r2 = (int) r11
            java.math.BigInteger r1 = r1.bigMultiplyPowerTen(r2)
            java.math.BigInteger r0 = r0.intVal
            int r0 = r0.compareMagnitude((java.math.BigInteger) r1)
            return r0
        L_0x009a:
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r8 == 0) goto L_0x00a7
            int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r0 == 0) goto L_0x00a6
            int r9 = longCompareMagnitude(r4, r2)
        L_0x00a6:
            return r9
        L_0x00a7:
            int r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r2 == 0) goto L_0x00ad
            r2 = 1
            return r2
        L_0x00ad:
            java.math.BigInteger r0 = r0.intVal
            java.math.BigInteger r1 = r1.intVal
            int r0 = r0.compareMagnitude((java.math.BigInteger) r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigDecimal.compareMagnitude(java.math.BigDecimal):int");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BigDecimal)) {
            return false;
        }
        BigDecimal bigDecimal = (BigDecimal) obj;
        if (obj == this) {
            return true;
        }
        if (this.scale != bigDecimal.scale) {
            return false;
        }
        long j = this.intCompact;
        long j2 = bigDecimal.intCompact;
        if (j != Long.MIN_VALUE) {
            if (j2 == Long.MIN_VALUE) {
                j2 = compactValFor(bigDecimal.intVal);
            }
            if (j2 == j) {
                return true;
            }
            return false;
        } else if (j2 == Long.MIN_VALUE) {
            return inflated().equals(bigDecimal.inflated());
        } else {
            if (j2 == compactValFor(this.intVal)) {
                return true;
            }
            return false;
        }
    }

    public BigDecimal min(BigDecimal bigDecimal) {
        return compareTo(bigDecimal) <= 0 ? this : bigDecimal;
    }

    public BigDecimal max(BigDecimal bigDecimal) {
        return compareTo(bigDecimal) >= 0 ? this : bigDecimal;
    }

    public int hashCode() {
        long j = this.intCompact;
        if (j == Long.MIN_VALUE) {
            return (this.intVal.hashCode() * 31) + this.scale;
        }
        long j2 = j < 0 ? -j : j;
        int i = (int) (((long) (((int) (j2 >>> 32)) * 31)) + (j2 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER));
        if (j < 0) {
            i = -i;
        }
        return (i * 31) + this.scale;
    }

    public String toString() {
        String str = this.stringCache;
        if (str != null) {
            return str;
        }
        String layoutChars = layoutChars(true);
        this.stringCache = layoutChars;
        return layoutChars;
    }

    public String toEngineeringString() {
        return layoutChars(false);
    }

    public String toPlainString() {
        String str;
        StringBuilder sb;
        int i = this.scale;
        if (i == 0) {
            long j = this.intCompact;
            if (j != Long.MIN_VALUE) {
                return Long.toString(j);
            }
            return this.intVal.toString();
        } else if (i >= 0) {
            long j2 = this.intCompact;
            if (j2 != Long.MIN_VALUE) {
                str = Long.toString(Math.abs(j2));
            } else {
                str = this.intVal.abs().toString();
            }
            return getValueString(signum(), str, this.scale);
        } else if (signum() == 0) {
            return "0";
        } else {
            int checkScaleNonZero = checkScaleNonZero(-((long) this.scale));
            if (this.intCompact != Long.MIN_VALUE) {
                sb = new StringBuilder(checkScaleNonZero + 20);
                sb.append(this.intCompact);
            } else {
                String bigInteger = this.intVal.toString();
                sb = new StringBuilder(bigInteger.length() + checkScaleNonZero);
                sb.append(bigInteger);
            }
            for (int i2 = 0; i2 < checkScaleNonZero; i2++) {
                sb.append('0');
            }
            return sb.toString();
        }
    }

    private String getValueString(int i, String str, int i2) {
        StringBuilder sb;
        int length = str.length() - i2;
        String str2 = "-0.";
        if (length == 0) {
            StringBuilder sb2 = new StringBuilder();
            if (i >= 0) {
                str2 = "0.";
            }
            sb2.append(str2);
            sb2.append(str);
            return sb2.toString();
        }
        if (length > 0) {
            sb = new StringBuilder(str);
            sb.insert(length, '.');
            if (i < 0) {
                sb.insert(0, '-');
            }
        } else {
            StringBuilder sb3 = new StringBuilder((3 - length) + str.length());
            if (i >= 0) {
                str2 = "0.";
            }
            sb3.append(str2);
            for (int i3 = 0; i3 < (-length); i3++) {
                sb3.append('0');
            }
            sb3.append(str);
            sb = sb3;
        }
        return sb.toString();
    }

    public BigInteger toBigInteger() {
        return setScale(0, 1).inflated();
    }

    public BigInteger toBigIntegerExact() {
        return setScale(0, 7).inflated();
    }

    public long longValue() {
        long j = this.intCompact;
        if (j != Long.MIN_VALUE && this.scale == 0) {
            return j;
        }
        if (signum() == 0 || fractionOnly() || this.scale <= -64) {
            return 0;
        }
        return toBigInteger().longValue();
    }

    private boolean fractionOnly() {
        return precision() - this.scale <= 0;
    }

    public long longValueExact() {
        long j = this.intCompact;
        if (j != Long.MIN_VALUE && this.scale == 0) {
            return j;
        }
        if (signum() == 0) {
            return 0;
        }
        if (fractionOnly()) {
            throw new ArithmeticException("Rounding necessary");
        } else if (precision() - this.scale <= 19) {
            BigDecimal scale2 = setScale(0, 7);
            if (scale2.precision() >= 19) {
                LongOverflow.check(scale2);
            }
            return scale2.inflated().longValue();
        } else {
            throw new ArithmeticException(BubbleOverflow.KEY);
        }
    }

    private static class LongOverflow {
        private static final BigInteger LONGMAX = BigInteger.valueOf(Long.MAX_VALUE);
        private static final BigInteger LONGMIN = BigInteger.valueOf(Long.MIN_VALUE);

        private LongOverflow() {
        }

        public static void check(BigDecimal bigDecimal) {
            BigInteger r1 = bigDecimal.inflated();
            if (r1.compareTo(LONGMIN) < 0 || r1.compareTo(LONGMAX) > 0) {
                throw new ArithmeticException(BubbleOverflow.KEY);
            }
        }
    }

    public int intValue() {
        long j = this.intCompact;
        if (j == Long.MIN_VALUE || this.scale != 0) {
            j = longValue();
        }
        return (int) j;
    }

    public int intValueExact() {
        long longValueExact = longValueExact();
        int i = (int) longValueExact;
        if (((long) i) == longValueExact) {
            return i;
        }
        throw new ArithmeticException(BubbleOverflow.KEY);
    }

    public short shortValueExact() {
        long longValueExact = longValueExact();
        short s = (short) ((int) longValueExact);
        if (((long) s) == longValueExact) {
            return s;
        }
        throw new ArithmeticException(BubbleOverflow.KEY);
    }

    public byte byteValueExact() {
        long longValueExact = longValueExact();
        byte b = (byte) ((int) longValueExact);
        if (((long) b) == longValueExact) {
            return b;
        }
        throw new ArithmeticException(BubbleOverflow.KEY);
    }

    public float floatValue() {
        long j = this.intCompact;
        if (j != Long.MIN_VALUE) {
            if (this.scale == 0) {
                return (float) j;
            }
            if (Math.abs(j) < 4194304) {
                int i = this.scale;
                if (i > 0) {
                    float[] fArr = FLOAT_10_POW;
                    if (i < fArr.length) {
                        return ((float) this.intCompact) / fArr[i];
                    }
                }
                if (i < 0) {
                    float[] fArr2 = FLOAT_10_POW;
                    if (i > (-fArr2.length)) {
                        return ((float) this.intCompact) * fArr2[-i];
                    }
                }
            }
        }
        return Float.parseFloat(toString());
    }

    public double doubleValue() {
        long j = this.intCompact;
        if (j != Long.MIN_VALUE) {
            if (this.scale == 0) {
                return (double) j;
            }
            if (Math.abs(j) < WifiManager.WIFI_FEATURE_TRUST_ON_FIRST_USE) {
                int i = this.scale;
                if (i > 0) {
                    double[] dArr = DOUBLE_10_POW;
                    if (i < dArr.length) {
                        return ((double) this.intCompact) / dArr[i];
                    }
                }
                if (i < 0) {
                    double[] dArr2 = DOUBLE_10_POW;
                    if (i > (-dArr2.length)) {
                        return ((double) this.intCompact) * dArr2[-i];
                    }
                }
            }
        }
        return Double.parseDouble(toString());
    }

    public BigDecimal ulp() {
        return valueOf(1, scale(), 1);
    }

    static class StringBuilderHelper {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        static final char[] DIGIT_ONES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        static final char[] DIGIT_TENS = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        final char[] cmpCharArray = new char[19];

        /* renamed from: sb */
        final StringBuilder f553sb = new StringBuilder();

        static {
            Class<BigDecimal> cls = BigDecimal.class;
        }

        StringBuilderHelper() {
        }

        /* access modifiers changed from: package-private */
        public StringBuilder getStringBuilder() {
            this.f553sb.setLength(0);
            return this.f553sb;
        }

        /* access modifiers changed from: package-private */
        public char[] getCompactCharArray() {
            return this.cmpCharArray;
        }

        /* access modifiers changed from: package-private */
        public int putIntCompact(long j) {
            int length = this.cmpCharArray.length;
            while (j > 2147483647L) {
                long j2 = j / 100;
                int i = (int) (j - (100 * j2));
                char[] cArr = this.cmpCharArray;
                int i2 = length - 1;
                cArr[i2] = DIGIT_ONES[i];
                length = i2 - 1;
                cArr[length] = DIGIT_TENS[i];
                j = j2;
            }
            int i3 = (int) j;
            while (i3 >= 100) {
                int i4 = i3 / 100;
                int i5 = i3 - (i4 * 100);
                char[] cArr2 = this.cmpCharArray;
                int i6 = length - 1;
                cArr2[i6] = DIGIT_ONES[i5];
                length = i6 - 1;
                cArr2[length] = DIGIT_TENS[i5];
                i3 = i4;
            }
            char[] cArr3 = this.cmpCharArray;
            int i7 = length - 1;
            cArr3[i7] = DIGIT_ONES[i3];
            if (i3 < 10) {
                return i7;
            }
            int i8 = i7 - 1;
            cArr3[i8] = DIGIT_TENS[i3];
            return i8;
        }
    }

    private String layoutChars(boolean z) {
        char[] cArr;
        int i;
        long j;
        int i2 = this.scale;
        if (i2 == 0) {
            long j2 = this.intCompact;
            if (j2 != Long.MIN_VALUE) {
                return Long.toString(j2);
            }
            return this.intVal.toString();
        }
        if (i2 == 2) {
            long j3 = this.intCompact;
            if (j3 >= 0 && j3 < 2147483647L) {
                int i3 = ((int) j3) % 100;
                return Integer.toString(((int) j3) / 100) + '.' + StringBuilderHelper.DIGIT_TENS[i3] + StringBuilderHelper.DIGIT_ONES[i3];
            }
        }
        StringBuilderHelper stringBuilderHelper = threadLocalStringBuilderHelper.get();
        long j4 = this.intCompact;
        if (j4 != Long.MIN_VALUE) {
            i = stringBuilderHelper.putIntCompact(Math.abs(j4));
            cArr = stringBuilderHelper.getCompactCharArray();
        } else {
            cArr = this.intVal.abs().toString().toCharArray();
            i = 0;
        }
        StringBuilder stringBuilder = stringBuilderHelper.getStringBuilder();
        if (signum() < 0) {
            stringBuilder.append('-');
        }
        int length = cArr.length - i;
        int i4 = this.scale;
        int i5 = length - 1;
        long j5 = (-((long) i4)) + ((long) i5);
        if (i4 < 0 || j5 < -6) {
            if (z) {
                stringBuilder.append(cArr[i]);
                if (length > 1) {
                    stringBuilder.append('.');
                    stringBuilder.append(cArr, i + 1, i5);
                }
                j = 0;
            } else {
                int i6 = (int) (j5 % 3);
                if (i6 < 0) {
                    i6 += 3;
                }
                int i7 = length;
                j5 -= (long) i6;
                int i8 = i6 + 1;
                if (signum() == 0) {
                    if (i8 != 1) {
                        if (i8 == 2) {
                            stringBuilder.append("0.00");
                        } else if (i8 == 3) {
                            stringBuilder.append("0.0");
                        } else {
                            throw new AssertionError((Object) "Unexpected sig value " + i8);
                        }
                        j5 += 3;
                    } else {
                        stringBuilder.append('0');
                    }
                } else if (i8 >= i7) {
                    stringBuilder.append(cArr, i, i7);
                    for (int i9 = i8 - i7; i9 > 0; i9--) {
                        stringBuilder.append('0');
                    }
                } else {
                    stringBuilder.append(cArr, i, i8);
                    stringBuilder.append('.');
                    stringBuilder.append(cArr, i + i8, i7 - i8);
                }
                j = 0;
            }
            int i10 = (j5 > j ? 1 : (j5 == j ? 0 : -1));
            if (i10 != 0) {
                stringBuilder.append('E');
                if (i10 > 0) {
                    stringBuilder.append('+');
                }
                stringBuilder.append(j5);
            }
        } else {
            int i11 = i4 - length;
            if (i11 >= 0) {
                stringBuilder.append('0');
                stringBuilder.append('.');
                while (i11 > 0) {
                    stringBuilder.append('0');
                    i11--;
                }
                stringBuilder.append(cArr, i, length);
            } else {
                int i12 = -i11;
                stringBuilder.append(cArr, i, i12);
                stringBuilder.append('.');
                stringBuilder.append(cArr, i12 + i, this.scale);
            }
        }
        return stringBuilder.toString();
    }

    private static BigInteger bigTenToThe(int i) {
        if (i < 0) {
            return BigInteger.ZERO;
        }
        if (i >= BIG_TEN_POWERS_TABLE_MAX) {
            return BigInteger.TEN.pow(i);
        }
        BigInteger[] bigIntegerArr = BIG_TEN_POWERS_TABLE;
        if (i < bigIntegerArr.length) {
            return bigIntegerArr[i];
        }
        return expandBigIntegerTenPowers(i);
    }

    private static BigInteger expandBigIntegerTenPowers(int i) {
        BigInteger bigInteger;
        synchronized (BigDecimal.class) {
            BigInteger[] bigIntegerArr = BIG_TEN_POWERS_TABLE;
            int length = bigIntegerArr.length;
            if (length <= i) {
                int i2 = length << 1;
                while (i2 <= i) {
                    i2 <<= 1;
                }
                bigIntegerArr = (BigInteger[]) Arrays.copyOf((T[]) bigIntegerArr, i2);
                while (length < i2) {
                    bigIntegerArr[length] = bigIntegerArr[length - 1].multiply(BigInteger.TEN);
                    length++;
                }
                BIG_TEN_POWERS_TABLE = bigIntegerArr;
            }
            bigInteger = bigIntegerArr[i];
        }
        return bigInteger;
    }

    private static long longMultiplyPowerTen(long j, int i) {
        if (j == 0 || i <= 0) {
            return j;
        }
        long[] jArr = LONG_TEN_POWERS_TABLE;
        long[] jArr2 = THRESHOLDS_TABLE;
        if (i < jArr.length && i < jArr2.length) {
            long j2 = jArr[i];
            if (j == 1) {
                return j2;
            }
            if (Math.abs(j) <= jArr2[i]) {
                return j * j2;
            }
        }
        return Long.MIN_VALUE;
    }

    private BigInteger bigMultiplyPowerTen(int i) {
        if (i <= 0) {
            return inflated();
        }
        if (this.intCompact != Long.MIN_VALUE) {
            return bigTenToThe(i).multiply(this.intCompact);
        }
        return this.intVal.multiply(bigTenToThe(i));
    }

    /* access modifiers changed from: private */
    public BigInteger inflated() {
        BigInteger bigInteger = this.intVal;
        return bigInteger == null ? BigInteger.valueOf(this.intCompact) : bigInteger;
    }

    private static void matchScale(BigDecimal[] bigDecimalArr) {
        BigDecimal bigDecimal = bigDecimalArr[0];
        int i = bigDecimal.scale;
        BigDecimal bigDecimal2 = bigDecimalArr[1];
        int i2 = bigDecimal2.scale;
        if (i < i2) {
            bigDecimalArr[0] = bigDecimal.setScale(i2, 7);
        } else if (i2 < i) {
            bigDecimalArr[1] = bigDecimal2.setScale(i, 7);
        }
    }

    private static class UnsafeHolder {
        private static final long intCompactOffset;
        private static final long intValOffset;
        private static final Unsafe unsafe;

        private UnsafeHolder() {
        }

        static {
            try {
                Unsafe unsafe2 = Unsafe.getUnsafe();
                unsafe = unsafe2;
                intCompactOffset = unsafe2.objectFieldOffset(BigDecimal.class.getDeclaredField("intCompact"));
                intValOffset = unsafe2.objectFieldOffset(BigDecimal.class.getDeclaredField("intVal"));
            } catch (Exception e) {
                throw new ExceptionInInitializerError((Throwable) e);
            }
        }

        static void setIntCompactVolatile(BigDecimal bigDecimal, long j) {
            unsafe.putLongVolatile(bigDecimal, intCompactOffset, j);
        }

        static void setIntValVolatile(BigDecimal bigDecimal, BigInteger bigInteger) {
            unsafe.putObjectVolatile(bigDecimal, intValOffset, bigInteger);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        BigInteger bigInteger = this.intVal;
        if (bigInteger != null) {
            UnsafeHolder.setIntCompactVolatile(this, compactValFor(bigInteger));
            return;
        }
        throw new StreamCorruptedException("BigDecimal: null intVal in stream");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.intVal == null) {
            UnsafeHolder.setIntValVolatile(this, BigInteger.valueOf(this.intCompact));
        }
        objectOutputStream.defaultWriteObject();
    }

    static int longDigitLength(long j) {
        if (j < 0) {
            j = -j;
        }
        if (j < 10) {
            return 1;
        }
        int numberOfLeadingZeros = (((64 - Long.numberOfLeadingZeros(j)) + 1) * 1233) >>> 12;
        long[] jArr = LONG_TEN_POWERS_TABLE;
        return (numberOfLeadingZeros >= jArr.length || j < jArr[numberOfLeadingZeros]) ? numberOfLeadingZeros : numberOfLeadingZeros + 1;
    }

    private static int bigDigitLength(BigInteger bigInteger) {
        if (bigInteger.signum == 0) {
            return 1;
        }
        int bitLength = (int) (((((long) bigInteger.bitLength()) + 1) * 646456993) >>> 31);
        return bigInteger.compareMagnitude(bigTenToThe(bitLength)) < 0 ? bitLength : bitLength + 1;
    }

    private int checkScale(long j) {
        BigInteger bigInteger;
        int i = (int) j;
        if (((long) i) != j) {
            i = j > 2147483647L ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            if (this.intCompact != 0 && ((bigInteger = this.intVal) == null || bigInteger.signum() != 0)) {
                throw new ArithmeticException(i > 0 ? "Underflow" : BubbleOverflow.KEY);
            }
        }
        return i;
    }

    private static long compactValFor(BigInteger bigInteger) {
        int[] iArr = bigInteger.mag;
        int length = iArr.length;
        if (length == 0) {
            return 0;
        }
        int i = iArr[0];
        if (length > 2) {
            return Long.MIN_VALUE;
        }
        if (length == 2 && i < 0) {
            return Long.MIN_VALUE;
        }
        long j = length == 2 ? (((long) iArr[1]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + (((long) i) << 32) : ((long) i) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        return bigInteger.signum < 0 ? -j : j;
    }

    private static void print(String str, BigDecimal bigDecimal) {
        System.err.format("%s:\tintCompact %d\tintVal %d\tscale %d\tprecision %d%n", str, Long.valueOf(bigDecimal.intCompact), bigDecimal.intVal, Integer.valueOf(bigDecimal.scale), Integer.valueOf(bigDecimal.precision));
    }

    private BigDecimal audit() {
        if (this.intCompact == Long.MIN_VALUE) {
            BigInteger bigInteger = this.intVal;
            if (bigInteger != null) {
                int i = this.precision;
                if (i > 0 && i != bigDigitLength(bigInteger)) {
                    print("audit", this);
                    throw new AssertionError((Object) "precision mismatch");
                }
            } else {
                print("audit", this);
                throw new AssertionError((Object) "null intVal");
            }
        } else {
            BigInteger bigInteger2 = this.intVal;
            if (bigInteger2 != null) {
                long longValue = bigInteger2.longValue();
                if (longValue != this.intCompact) {
                    print("audit", this);
                    throw new AssertionError((Object) "Inconsistent state, intCompact=" + this.intCompact + "\t intVal=" + longValue);
                }
            }
            int i2 = this.precision;
            if (i2 > 0 && i2 != longDigitLength(this.intCompact)) {
                print("audit", this);
                throw new AssertionError((Object) "precision mismatch");
            }
        }
        return this;
    }

    private static int checkScaleNonZero(long j) {
        int i = (int) j;
        if (((long) i) == j) {
            return i;
        }
        throw new ArithmeticException(i > 0 ? "Underflow" : BubbleOverflow.KEY);
    }

    private static int checkScale(long j, long j2) {
        int i = (int) j2;
        if (((long) i) != j2) {
            i = j2 > 2147483647L ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            if (j != 0) {
                throw new ArithmeticException(i > 0 ? "Underflow" : BubbleOverflow.KEY);
            }
        }
        return i;
    }

    private static int checkScale(BigInteger bigInteger, long j) {
        int i = (int) j;
        if (((long) i) != j) {
            i = j > 2147483647L ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            if (bigInteger.signum() != 0) {
                throw new ArithmeticException(i > 0 ? "Underflow" : BubbleOverflow.KEY);
            }
        }
        return i;
    }

    private static BigDecimal doRound(BigDecimal bigDecimal, MathContext mathContext) {
        BigDecimal bigDecimal2 = bigDecimal;
        MathContext mathContext2 = mathContext;
        int i = mathContext2.precision;
        if (i <= 0) {
            return bigDecimal2;
        }
        BigInteger bigInteger = bigDecimal2.intVal;
        long j = bigDecimal2.intCompact;
        int i2 = bigDecimal2.scale;
        int precision2 = bigDecimal.precision();
        int i3 = mathContext2.roundingMode.oldMode;
        boolean z = false;
        if (j == Long.MIN_VALUE) {
            int i4 = precision2 - i;
            while (true) {
                if (i4 <= 0) {
                    break;
                }
                i2 = checkScaleNonZero(((long) i2) - ((long) i4));
                bigInteger = divideAndRoundByTenPow(bigInteger, i4, i3);
                j = compactValFor(bigInteger);
                if (j != Long.MIN_VALUE) {
                    precision2 = longDigitLength(j);
                    z = true;
                    break;
                }
                precision2 = bigDigitLength(bigInteger);
                i4 = precision2 - i;
                z = true;
            }
        }
        if (j != Long.MIN_VALUE) {
            int i5 = precision2 - i;
            while (i5 > 0) {
                i2 = checkScaleNonZero(((long) i2) - ((long) i5));
                j = divideAndRound(j, LONG_TEN_POWERS_TABLE[i5], mathContext2.roundingMode.oldMode);
                precision2 = longDigitLength(j);
                i5 = precision2 - i;
                bigInteger = null;
                z = true;
            }
        }
        return z ? new BigDecimal(bigInteger, j, i2, precision2) : bigDecimal2;
    }

    private static BigDecimal doRound(long j, int i, MathContext mathContext) {
        int i2 = mathContext.precision;
        if (i2 <= 0 || i2 >= 19) {
            return valueOf(j, i);
        }
        int longDigitLength = longDigitLength(j);
        while (true) {
            int i3 = longDigitLength - i2;
            if (i3 <= 0) {
                return valueOf(j, i, longDigitLength);
            }
            i = checkScaleNonZero(((long) i) - ((long) i3));
            j = divideAndRound(j, LONG_TEN_POWERS_TABLE[i3], mathContext.roundingMode.oldMode);
            longDigitLength = longDigitLength(j);
        }
    }

    private static BigDecimal doRound(BigInteger bigInteger, int i, MathContext mathContext) {
        int i2 = mathContext.precision;
        int i3 = 0;
        if (i2 > 0) {
            long compactValFor = compactValFor(bigInteger);
            int i4 = mathContext.roundingMode.oldMode;
            if (compactValFor == Long.MIN_VALUE) {
                i3 = bigDigitLength(bigInteger);
                while (true) {
                    int i5 = i3 - i2;
                    if (i5 <= 0) {
                        break;
                    }
                    i = checkScaleNonZero(((long) i) - ((long) i5));
                    bigInteger = divideAndRoundByTenPow(bigInteger, i5, i4);
                    compactValFor = compactValFor(bigInteger);
                    if (compactValFor != Long.MIN_VALUE) {
                        break;
                    }
                    i3 = bigDigitLength(bigInteger);
                }
            }
            if (compactValFor != Long.MIN_VALUE) {
                int longDigitLength = longDigitLength(compactValFor);
                while (true) {
                    int i6 = longDigitLength - i2;
                    if (i6 <= 0) {
                        return valueOf(compactValFor, i, longDigitLength);
                    }
                    i = checkScaleNonZero(((long) i) - ((long) i6));
                    compactValFor = divideAndRound(compactValFor, LONG_TEN_POWERS_TABLE[i6], mathContext.roundingMode.oldMode);
                    longDigitLength = longDigitLength(compactValFor);
                }
            }
        }
        int i7 = i3;
        return new BigDecimal(bigInteger, Long.MIN_VALUE, i, i7);
    }

    private static BigInteger divideAndRoundByTenPow(BigInteger bigInteger, int i, int i2) {
        long[] jArr = LONG_TEN_POWERS_TABLE;
        if (i < jArr.length) {
            return divideAndRound(bigInteger, jArr[i], i2);
        }
        return divideAndRound(bigInteger, bigTenToThe(i), i2);
    }

    private static BigDecimal divideAndRound(long j, long j2, int i, int i2, int i3) {
        int i4 = i;
        int i5 = i3;
        long j3 = j / j2;
        int i6 = 1;
        if (i2 == 1 && i4 == i5) {
            return valueOf(j3, i4);
        }
        long j4 = j % j2;
        boolean z = false;
        boolean z2 = j < 0;
        if (j2 < 0) {
            z = true;
        }
        if (z2 != z) {
            i6 = -1;
        }
        int i7 = i6;
        if (j4 != 0) {
            if (needIncrement(j2, i2, i7, j3, j4)) {
                j3 += (long) i7;
            }
            return valueOf(j3, i4);
        } else if (i5 != i4) {
            return createAndStripZerosToMatchScale(j3, i4, (long) i5);
        } else {
            return valueOf(j3, i4);
        }
    }

    private static long divideAndRound(long j, long j2, int i) {
        long j3 = j / j2;
        int i2 = 1;
        if (i == 1) {
            return j3;
        }
        long j4 = j % j2;
        boolean z = false;
        boolean z2 = j < 0;
        if (j2 < 0) {
            z = true;
        }
        if (z2 != z) {
            i2 = -1;
        }
        int i3 = i2;
        return (j4 == 0 || !needIncrement(j2, i, i3, j3, j4)) ? j3 : j3 + ((long) i3);
    }

    private static boolean commonNeedIncrement(int i, int i2, int i3, boolean z) {
        if (i == 0) {
            return true;
        }
        if (i == 1) {
            return false;
        }
        if (i != 2) {
            if (i != 3) {
                if (i == 7) {
                    throw new ArithmeticException("Rounding necessary");
                } else if (i3 < 0) {
                    return false;
                } else {
                    if (i3 > 0 || i == 4) {
                        return true;
                    }
                    if (i == 5) {
                        return false;
                    }
                    if (i == 6) {
                        return z;
                    }
                    throw new AssertionError((Object) "Unexpected rounding mode" + i);
                }
            } else if (i2 < 0) {
                return true;
            } else {
                return false;
            }
        } else if (i2 > 0) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean needIncrement(long j, int i, int i2, long j2, long j3) {
        boolean z = true;
        int longCompareMagnitude = (j3 <= HALF_LONG_MIN_VALUE || j3 > 4611686018427387903L) ? 1 : longCompareMagnitude(j3 * 2, j);
        if ((j2 & 1) == 0) {
            z = false;
        }
        return commonNeedIncrement(i, i2, longCompareMagnitude, z);
    }

    private static BigInteger divideAndRound(BigInteger bigInteger, long j, int i) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger(bigInteger.mag);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        long divide = mutableBigInteger.divide(j, mutableBigInteger2);
        boolean z = divide == 0;
        int i2 = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        int i3 = bigInteger.signum;
        if (i2 < 0) {
            i3 = -i3;
        }
        if (!z && needIncrement(j, i, i3, mutableBigInteger2, divide)) {
            mutableBigInteger2.add(MutableBigInteger.ONE);
        }
        return mutableBigInteger2.toBigInteger(i3);
    }

    private static BigDecimal divideAndRound(BigInteger bigInteger, long j, int i, int i2, int i3) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger(bigInteger.mag);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        long divide = mutableBigInteger.divide(j, mutableBigInteger2);
        boolean z = divide == 0;
        int i4 = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        int i5 = bigInteger.signum;
        if (i4 < 0) {
            i5 = -i5;
        }
        if (!z) {
            if (needIncrement(j, i2, i5, mutableBigInteger2, divide)) {
                mutableBigInteger2.add(MutableBigInteger.ONE);
            }
            return mutableBigInteger2.toBigDecimal(i5, i);
        } else if (i3 == i) {
            return mutableBigInteger2.toBigDecimal(i5, i);
        } else {
            long compactValue = mutableBigInteger2.toCompactValue(i5);
            if (compactValue != Long.MIN_VALUE) {
                return createAndStripZerosToMatchScale(compactValue, i, (long) i3);
            }
            return createAndStripZerosToMatchScale(mutableBigInteger2.toBigInteger(i5), i, (long) i3);
        }
    }

    private static boolean needIncrement(long j, int i, int i2, MutableBigInteger mutableBigInteger, long j2) {
        return commonNeedIncrement(i, i2, (j2 <= HALF_LONG_MIN_VALUE || j2 > 4611686018427387903L) ? 1 : longCompareMagnitude(j2 * 2, j), mutableBigInteger.isOdd());
    }

    private static BigInteger divideAndRound(BigInteger bigInteger, BigInteger bigInteger2, int i) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger(bigInteger.mag);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(bigInteger2.mag);
        MutableBigInteger divide = mutableBigInteger.divide(mutableBigInteger3, mutableBigInteger2);
        boolean isZero = divide.isZero();
        int i2 = bigInteger.signum != bigInteger2.signum ? -1 : 1;
        if (!isZero && needIncrement(mutableBigInteger3, i, i2, mutableBigInteger2, divide)) {
            mutableBigInteger2.add(MutableBigInteger.ONE);
        }
        return mutableBigInteger2.toBigInteger(i2);
    }

    private static BigDecimal divideAndRound(BigInteger bigInteger, BigInteger bigInteger2, int i, int i2, int i3) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger(bigInteger.mag);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(bigInteger2.mag);
        MutableBigInteger divide = mutableBigInteger.divide(mutableBigInteger3, mutableBigInteger2);
        boolean isZero = divide.isZero();
        int i4 = bigInteger.signum != bigInteger2.signum ? -1 : 1;
        if (!isZero) {
            if (needIncrement(mutableBigInteger3, i2, i4, mutableBigInteger2, divide)) {
                mutableBigInteger2.add(MutableBigInteger.ONE);
            }
            return mutableBigInteger2.toBigDecimal(i4, i);
        } else if (i3 == i) {
            return mutableBigInteger2.toBigDecimal(i4, i);
        } else {
            long compactValue = mutableBigInteger2.toCompactValue(i4);
            if (compactValue != Long.MIN_VALUE) {
                return createAndStripZerosToMatchScale(compactValue, i, (long) i3);
            }
            return createAndStripZerosToMatchScale(mutableBigInteger2.toBigInteger(i4), i, (long) i3);
        }
    }

    private static boolean needIncrement(MutableBigInteger mutableBigInteger, int i, int i2, MutableBigInteger mutableBigInteger2, MutableBigInteger mutableBigInteger3) {
        return commonNeedIncrement(i, i2, mutableBigInteger3.compareHalf(mutableBigInteger), mutableBigInteger2.isOdd());
    }

    private static BigDecimal createAndStripZerosToMatchScale(BigInteger bigInteger, int i, long j) {
        while (bigInteger.compareMagnitude(BigInteger.TEN) >= 0) {
            long j2 = (long) i;
            if (j2 <= j || bigInteger.testBit(0)) {
                break;
            }
            BigInteger[] divideAndRemainder = bigInteger.divideAndRemainder(BigInteger.TEN);
            if (divideAndRemainder[1].signum() != 0) {
                break;
            }
            bigInteger = divideAndRemainder[0];
            i = checkScale(bigInteger, j2 - 1);
        }
        return valueOf(bigInteger, i, 0);
    }

    private static BigDecimal createAndStripZerosToMatchScale(long j, int i, long j2) {
        while (Math.abs(j) >= 10) {
            long j3 = (long) i;
            if (j3 <= j2 || (j & 1) != 0 || j % 10 != 0) {
                break;
            }
            j /= 10;
            i = checkScale(j, j3 - 1);
        }
        return valueOf(j, i);
    }

    private static BigDecimal stripZerosToMatchScale(BigInteger bigInteger, long j, int i, int i2) {
        if (j != Long.MIN_VALUE) {
            return createAndStripZerosToMatchScale(j, i, (long) i2);
        }
        if (bigInteger == null) {
            bigInteger = INFLATED_BIGINT;
        }
        return createAndStripZerosToMatchScale(bigInteger, i, (long) i2);
    }

    private static BigDecimal add(long j, long j2, int i) {
        long add = add(j, j2);
        if (add != Long.MIN_VALUE) {
            return valueOf(add, i);
        }
        return new BigDecimal(BigInteger.valueOf(j).add(j2), i);
    }

    private static BigDecimal add(long j, int i, long j2, int i2) {
        long j3 = j;
        int i3 = i;
        long j4 = j2;
        int i4 = i2;
        long j5 = ((long) i3) - ((long) i4);
        int i5 = (j5 > 0 ? 1 : (j5 == 0 ? 0 : -1));
        if (i5 == 0) {
            return add(j, j4, i3);
        }
        if (i5 < 0) {
            int checkScale = checkScale(j, -j5);
            long longMultiplyPowerTen = longMultiplyPowerTen(j, checkScale);
            if (longMultiplyPowerTen != Long.MIN_VALUE) {
                return add(longMultiplyPowerTen, j4, i4);
            }
            BigInteger add = bigMultiplyPowerTen(j, checkScale).add(j4);
            if ((j3 ^ j4) >= 0) {
                return new BigDecimal(add, Long.MIN_VALUE, i2, 0);
            }
            return valueOf(add, i4, 0);
        }
        int checkScale2 = checkScale(j4, j5);
        long longMultiplyPowerTen2 = longMultiplyPowerTen(j4, checkScale2);
        if (longMultiplyPowerTen2 != Long.MIN_VALUE) {
            return add(j, longMultiplyPowerTen2, i3);
        }
        BigInteger add2 = bigMultiplyPowerTen(j4, checkScale2).add(j);
        if ((j3 ^ j4) >= 0) {
            return new BigDecimal(add2, Long.MIN_VALUE, i, 0);
        }
        return valueOf(add2, i3, 0);
    }

    private static BigDecimal add(long j, int i, BigInteger bigInteger, int i2) {
        int i3;
        BigInteger bigInteger2;
        BigInteger bigInteger3;
        long j2 = ((long) i) - ((long) i2);
        boolean z = Long.signum(j) == bigInteger.signum;
        if (j2 < 0) {
            int checkScale = checkScale(j, -j2);
            long longMultiplyPowerTen = longMultiplyPowerTen(j, checkScale);
            if (longMultiplyPowerTen == Long.MIN_VALUE) {
                bigInteger3 = bigInteger.add(bigMultiplyPowerTen(j, checkScale));
            } else {
                bigInteger3 = bigInteger.add(longMultiplyPowerTen);
            }
            bigInteger2 = bigInteger3;
            i3 = i2;
        } else {
            bigInteger2 = bigMultiplyPowerTen(bigInteger, checkScale(bigInteger, j2)).add(j);
            i3 = i;
        }
        if (z) {
            return new BigDecimal(bigInteger2, Long.MIN_VALUE, i3, 0);
        }
        return valueOf(bigInteger2, i3, 0);
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0033  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.math.BigDecimal add(java.math.BigInteger r6, int r7, java.math.BigInteger r8, int r9) {
        /*
            long r0 = (long) r7
            long r2 = (long) r9
            long r0 = r0 - r2
            r2 = 0
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 == 0) goto L_0x001e
            if (r2 >= 0) goto L_0x0016
            long r0 = -r0
            int r7 = checkScale((java.math.BigInteger) r6, (long) r0)
            java.math.BigInteger r6 = bigMultiplyPowerTen((java.math.BigInteger) r6, (int) r7)
            r4 = r9
            goto L_0x001f
        L_0x0016:
            int r9 = checkScale((java.math.BigInteger) r8, (long) r0)
            java.math.BigInteger r8 = bigMultiplyPowerTen((java.math.BigInteger) r8, (int) r9)
        L_0x001e:
            r4 = r7
        L_0x001f:
            java.math.BigInteger r1 = r6.add((java.math.BigInteger) r8)
            int r6 = r6.signum
            int r7 = r8.signum
            if (r6 != r7) goto L_0x0033
            java.math.BigDecimal r6 = new java.math.BigDecimal
            r2 = -9223372036854775808
            r5 = 0
            r0 = r6
            r0.<init>((java.math.BigInteger) r1, (long) r2, (int) r4, (int) r5)
            goto L_0x0038
        L_0x0033:
            r6 = 0
            java.math.BigDecimal r6 = valueOf((java.math.BigInteger) r1, (int) r4, (int) r6)
        L_0x0038:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigDecimal.add(java.math.BigInteger, int, java.math.BigInteger, int):java.math.BigDecimal");
    }

    private static BigInteger bigMultiplyPowerTen(long j, int i) {
        if (i <= 0) {
            return BigInteger.valueOf(j);
        }
        return bigTenToThe(i).multiply(j);
    }

    private static BigInteger bigMultiplyPowerTen(BigInteger bigInteger, int i) {
        if (i <= 0) {
            return bigInteger;
        }
        long[] jArr = LONG_TEN_POWERS_TABLE;
        if (i < jArr.length) {
            return bigInteger.multiply(jArr[i]);
        }
        return bigInteger.multiply(bigTenToThe(i));
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0068  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.math.BigDecimal divideSmallFastPath(long r19, int r21, long r22, int r24, long r25, java.math.MathContext r27) {
        /*
            r0 = r19
            r2 = r21
            r9 = r22
            r3 = r24
            r11 = r27
            int r12 = r11.precision
            java.math.RoundingMode r4 = r11.roundingMode
            int r13 = r4.oldMode
            int r4 = r3 - r2
            if (r4 != 0) goto L_0x0016
            r14 = r0
            goto L_0x001b
        L_0x0016:
            long r4 = longMultiplyPowerTen(r0, r4)
            r14 = r4
        L_0x001b:
            int r4 = longCompareMagnitude(r14, r9)
            r6 = -1
            r16 = 1
            if (r4 <= 0) goto L_0x00d4
            int r3 = r3 + r6
            long r5 = (long) r3
            long r17 = r25 + r5
            long r7 = (long) r2
            long r17 = r17 - r7
            long r9 = (long) r12
            long r17 = r17 + r9
            int r4 = checkScaleNonZero(r17)
            long r17 = r9 + r5
            long r17 = r17 - r7
            int r2 = checkScaleNonZero(r17)
            if (r2 <= 0) goto L_0x0088
            int r2 = checkScaleNonZero(r17)
            long r0 = longMultiplyPowerTen(r0, r2)
            r2 = -9223372036854775808
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 != 0) goto L_0x007a
            int r12 = r12 + -1
            if (r12 < 0) goto L_0x0064
            long[] r0 = LONG_TEN_POWERS_TABLE
            int r1 = r0.length
            if (r12 >= r1) goto L_0x0064
            r0 = r0[r12]
            int r8 = checkScaleNonZero(r25)
            r2 = r14
            r9 = r4
            r4 = r22
            r6 = r9
            r7 = r13
            java.math.BigDecimal r5 = multiplyDivideAndRound(r0, r2, r4, r6, r7, r8)
            goto L_0x0066
        L_0x0064:
            r9 = r4
            r5 = 0
        L_0x0066:
            if (r5 != 0) goto L_0x0148
            java.math.BigInteger r0 = bigMultiplyPowerTen((long) r14, (int) r12)
            int r5 = checkScaleNonZero(r25)
            r1 = r22
            r3 = r9
            r4 = r13
            java.math.BigDecimal r5 = divideAndRound((java.math.BigInteger) r0, (long) r1, (int) r3, (int) r4, (int) r5)
            goto L_0x0148
        L_0x007a:
            r9 = r4
            int r6 = checkScaleNonZero(r25)
            r2 = r22
            r5 = r13
            java.math.BigDecimal r5 = divideAndRound((long) r0, (long) r2, (int) r4, (int) r5, (int) r6)
            goto L_0x0148
        L_0x0088:
            long r7 = r7 - r9
            int r2 = checkScaleNonZero(r7)
            if (r2 != r3) goto L_0x009e
            int r6 = checkScaleNonZero(r25)
            r0 = r19
            r2 = r22
            r5 = r13
            java.math.BigDecimal r5 = divideAndRound((long) r0, (long) r2, (int) r4, (int) r5, (int) r6)
            goto L_0x0148
        L_0x009e:
            long r2 = (long) r2
            long r2 = r2 - r5
            int r2 = checkScaleNonZero(r2)
            r9 = r22
            long r5 = longMultiplyPowerTen(r9, r2)
            r7 = -9223372036854775808
            int r3 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r3 != 0) goto L_0x00c2
            java.math.BigInteger r2 = bigMultiplyPowerTen((long) r9, (int) r2)
            java.math.BigInteger r0 = java.math.BigInteger.valueOf((long) r19)
            int r1 = checkScaleNonZero(r25)
            java.math.BigDecimal r5 = divideAndRound((java.math.BigInteger) r0, (java.math.BigInteger) r2, (int) r4, (int) r13, (int) r1)
            goto L_0x0148
        L_0x00c2:
            int r2 = checkScaleNonZero(r25)
            r21 = r5
            r23 = r4
            r24 = r13
            r25 = r2
            java.math.BigDecimal r5 = divideAndRound((long) r19, (long) r21, (int) r23, (int) r24, (int) r25)
            goto L_0x0148
        L_0x00d4:
            long r0 = (long) r3
            long r0 = r25 + r0
            long r2 = (long) r2
            long r0 = r0 - r2
            long r2 = (long) r12
            long r0 = r0 + r2
            int r8 = checkScaleNonZero(r0)
            if (r4 != 0) goto L_0x00ff
            r0 = 0
            int r2 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            r3 = 0
            if (r2 >= 0) goto L_0x00eb
            r2 = r16
            goto L_0x00ec
        L_0x00eb:
            r2 = r3
        L_0x00ec:
            int r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r0 >= 0) goto L_0x00f2
            r3 = r16
        L_0x00f2:
            if (r2 != r3) goto L_0x00f6
            r6 = r16
        L_0x00f6:
            int r0 = checkScaleNonZero(r25)
            java.math.BigDecimal r5 = roundedTenPower(r6, r12, r8, r0)
            goto L_0x0148
        L_0x00ff:
            long r0 = longMultiplyPowerTen(r14, r12)
            r2 = -9223372036854775808
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 != 0) goto L_0x0139
            long[] r0 = LONG_TEN_POWERS_TABLE
            int r1 = r0.length
            if (r12 >= r1) goto L_0x0122
            r0 = r0[r12]
            int r16 = checkScaleNonZero(r25)
            r2 = r14
            r4 = r22
            r6 = r8
            r7 = r13
            r17 = r8
            r8 = r16
            java.math.BigDecimal r5 = multiplyDivideAndRound(r0, r2, r4, r6, r7, r8)
            goto L_0x0125
        L_0x0122:
            r17 = r8
            r5 = 0
        L_0x0125:
            if (r5 != 0) goto L_0x0148
            java.math.BigInteger r0 = bigMultiplyPowerTen((long) r14, (int) r12)
            int r5 = checkScaleNonZero(r25)
            r1 = r22
            r3 = r17
            r4 = r13
            java.math.BigDecimal r5 = divideAndRound((java.math.BigInteger) r0, (long) r1, (int) r3, (int) r4, (int) r5)
            goto L_0x0148
        L_0x0139:
            r17 = r8
            int r6 = checkScaleNonZero(r25)
            r2 = r22
            r4 = r17
            r5 = r13
            java.math.BigDecimal r5 = divideAndRound((long) r0, (long) r2, (int) r4, (int) r5, (int) r6)
        L_0x0148:
            java.math.BigDecimal r0 = doRound(r5, r11)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigDecimal.divideSmallFastPath(long, int, long, int, long, java.math.MathContext):java.math.BigDecimal");
    }

    private static BigDecimal divide(long j, int i, long j2, int i2, long j3, MathContext mathContext) {
        BigDecimal bigDecimal;
        long j4 = j;
        int i3 = i;
        long j5 = j2;
        int i4 = i2;
        MathContext mathContext2 = mathContext;
        int i5 = mathContext2.precision;
        if (i3 <= i4 && i4 < 18 && i5 < 18) {
            return divideSmallFastPath(j, i, j2, i2, j3, mathContext);
        }
        if (compareMagnitudeNormalized(j, i, j2, i2) > 0) {
            i4--;
        }
        int i6 = mathContext2.roundingMode.oldMode;
        long j6 = (long) i4;
        long j7 = (long) i3;
        long j8 = (long) i5;
        int checkScaleNonZero = checkScaleNonZero(((j3 + j6) - j7) + j8);
        long j9 = (j8 + j6) - j7;
        if (checkScaleNonZero(j9) > 0) {
            int checkScaleNonZero2 = checkScaleNonZero(j9);
            long longMultiplyPowerTen = longMultiplyPowerTen(j4, checkScaleNonZero2);
            if (longMultiplyPowerTen == Long.MIN_VALUE) {
                bigDecimal = divideAndRound(bigMultiplyPowerTen(j4, checkScaleNonZero2), j2, checkScaleNonZero, i6, checkScaleNonZero(j3));
            } else {
                bigDecimal = divideAndRound(longMultiplyPowerTen, j2, checkScaleNonZero, i6, checkScaleNonZero(j3));
            }
        } else {
            int checkScaleNonZero3 = checkScaleNonZero(j7 - j8);
            if (checkScaleNonZero3 == i4) {
                bigDecimal = divideAndRound(j, j2, checkScaleNonZero, i6, checkScaleNonZero(j3));
            } else {
                int checkScaleNonZero4 = checkScaleNonZero(((long) checkScaleNonZero3) - j6);
                long longMultiplyPowerTen2 = longMultiplyPowerTen(j5, checkScaleNonZero4);
                if (longMultiplyPowerTen2 == Long.MIN_VALUE) {
                    bigDecimal = divideAndRound(BigInteger.valueOf(j), bigMultiplyPowerTen(j5, checkScaleNonZero4), checkScaleNonZero, i6, checkScaleNonZero(j3));
                } else {
                    bigDecimal = divideAndRound(j, longMultiplyPowerTen2, checkScaleNonZero, i6, checkScaleNonZero(j3));
                }
            }
        }
        return doRound(bigDecimal, mathContext);
    }

    private static BigDecimal divide(BigInteger bigInteger, int i, long j, int i2, long j2, MathContext mathContext) {
        BigDecimal bigDecimal;
        BigInteger bigInteger2 = bigInteger;
        int i3 = i;
        long j3 = j;
        int i4 = i2;
        MathContext mathContext2 = mathContext;
        if ((-compareMagnitudeNormalized(j3, i4, bigInteger2, i3)) > 0) {
            i4--;
        }
        int i5 = mathContext2.precision;
        int i6 = mathContext2.roundingMode.oldMode;
        long j4 = (long) i4;
        long j5 = (long) i3;
        long j6 = (long) i5;
        int checkScaleNonZero = checkScaleNonZero(((j2 + j4) - j5) + j6);
        long j7 = (j6 + j4) - j5;
        if (checkScaleNonZero(j7) > 0) {
            bigDecimal = divideAndRound(bigMultiplyPowerTen(bigInteger2, checkScaleNonZero(j7)), j, checkScaleNonZero, i6, checkScaleNonZero(j2));
        } else {
            int checkScaleNonZero2 = checkScaleNonZero(j5 - j6);
            if (checkScaleNonZero2 == i4) {
                bigDecimal = divideAndRound(bigInteger, j, checkScaleNonZero, i6, checkScaleNonZero(j2));
            } else {
                int checkScaleNonZero3 = checkScaleNonZero(((long) checkScaleNonZero2) - j4);
                long longMultiplyPowerTen = longMultiplyPowerTen(j3, checkScaleNonZero3);
                if (longMultiplyPowerTen == Long.MIN_VALUE) {
                    bigDecimal = divideAndRound(bigInteger2, bigMultiplyPowerTen(j3, checkScaleNonZero3), checkScaleNonZero, i6, checkScaleNonZero(j2));
                } else {
                    bigDecimal = divideAndRound(bigInteger, longMultiplyPowerTen, checkScaleNonZero, i6, checkScaleNonZero(j2));
                }
            }
        }
        return doRound(bigDecimal, mathContext2);
    }

    private static BigDecimal divide(long j, int i, BigInteger bigInteger, int i2, long j2, MathContext mathContext) {
        BigDecimal bigDecimal;
        BigInteger bigInteger2 = bigInteger;
        MathContext mathContext2 = mathContext;
        int i3 = compareMagnitudeNormalized(j, i, bigInteger, i2) > 0 ? i2 - 1 : i2;
        int i4 = mathContext2.precision;
        int i5 = mathContext2.roundingMode.oldMode;
        long j3 = (long) i3;
        long j4 = (long) i;
        long j5 = (long) i4;
        int checkScaleNonZero = checkScaleNonZero(((j2 + j3) - j4) + j5);
        long j6 = (j5 + j3) - j4;
        if (checkScaleNonZero(j6) > 0) {
            int checkScaleNonZero2 = checkScaleNonZero(j6);
            long j7 = j;
            bigDecimal = divideAndRound(bigMultiplyPowerTen(j, checkScaleNonZero2), bigInteger2, checkScaleNonZero, i5, checkScaleNonZero(j2));
        } else {
            long j8 = j;
            bigDecimal = divideAndRound(BigInteger.valueOf(j), bigMultiplyPowerTen(bigInteger2, checkScaleNonZero(((long) checkScaleNonZero(j4 - j5)) - j3)), checkScaleNonZero, i5, checkScaleNonZero(j2));
        }
        return doRound(bigDecimal, mathContext2);
    }

    private static BigDecimal divide(BigInteger bigInteger, int i, BigInteger bigInteger2, int i2, long j, MathContext mathContext) {
        BigDecimal bigDecimal;
        BigInteger bigInteger3 = bigInteger;
        BigInteger bigInteger4 = bigInteger2;
        MathContext mathContext2 = mathContext;
        int i3 = compareMagnitudeNormalized(bigInteger, i, bigInteger2, i2) > 0 ? i2 - 1 : i2;
        int i4 = mathContext2.precision;
        int i5 = mathContext2.roundingMode.oldMode;
        long j2 = (long) i3;
        long j3 = (long) i;
        long j4 = (long) i4;
        int checkScaleNonZero = checkScaleNonZero(((j + j2) - j3) + j4);
        long j5 = (j4 + j2) - j3;
        if (checkScaleNonZero(j5) > 0) {
            bigDecimal = divideAndRound(bigMultiplyPowerTen(bigInteger, checkScaleNonZero(j5)), bigInteger4, checkScaleNonZero, i5, checkScaleNonZero(j));
        } else {
            bigDecimal = divideAndRound(bigInteger, bigMultiplyPowerTen(bigInteger4, checkScaleNonZero(((long) checkScaleNonZero(j3 - j4)) - j2)), checkScaleNonZero, i5, checkScaleNonZero(j));
        }
        return doRound(bigDecimal, mathContext2);
    }

    private static BigDecimal multiplyDivideAndRound(long j, long j2, long j3, int i, int i2, int i3) {
        int signum = Long.signum(j) * Long.signum(j2) * Long.signum(j3);
        long abs = Math.abs(j);
        long abs2 = Math.abs(j2);
        long abs3 = Math.abs(j3);
        long j4 = abs >>> 32;
        long j5 = abs & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j6 = abs2 >>> 32;
        long j7 = abs2 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j8 = j5 * j7;
        long j9 = j8 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j10 = (j7 * j4) + (j8 >>> 32);
        long j11 = (j5 * j6) + (j10 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
        long j12 = j11 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j13 = (j10 >>> 32) + (j11 >>> 32);
        long j14 = j13 >>> 32;
        long j15 = (j4 * j6) + (j13 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
        return divideAndRound128(make64(((j15 >>> 32) + j14) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER, j15 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER), make64(j12, j9), abs3, signum, i, i2, i3);
    }

    private static BigDecimal divideAndRound128(long j, long j2, long j3, int i, int i2, int i3, int i4) {
        long j4;
        int i5;
        long j5;
        long j6;
        int i6;
        int i7;
        int i8;
        long j7;
        long j8;
        int i9 = i;
        int i10 = i2;
        int i11 = i3;
        int i12 = i4;
        if (j >= j3) {
            return null;
        }
        int numberOfLeadingZeros = Long.numberOfLeadingZeros(j3);
        long j9 = j3 << numberOfLeadingZeros;
        long j10 = j9 >>> 32;
        long j11 = j9 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j12 = j2 << numberOfLeadingZeros;
        long j13 = j12 >>> 32;
        long j14 = j12 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j15 = (j << numberOfLeadingZeros) | (j2 >>> (64 - numberOfLeadingZeros));
        long j16 = j15 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        int i13 = (j10 > 1 ? 1 : (j10 == 1 ? 0 : -1));
        long j17 = j9;
        if (i13 == 0) {
            j4 = 0;
        } else if (j15 >= 0) {
            long j18 = j15 / j10;
            j4 = j15 - (j18 * j10);
            j15 = j18;
        } else {
            long[] divRemNegativeLong = divRemNegativeLong(j15, j10);
            j15 = divRemNegativeLong[1];
            j4 = divRemNegativeLong[0];
        }
        while (true) {
            i5 = numberOfLeadingZeros;
            if (j15 < 4294967296L && !unsignedLongCompare(j15 * j11, make64(j4, j13))) {
                break;
            }
            j15--;
            j4 += j10;
            if (j4 >= 4294967296L) {
                break;
            }
            long j19 = j10;
            int i14 = i4;
            numberOfLeadingZeros = i5;
            int i15 = i;
            int i16 = i2;
        }
        long j20 = j15;
        long j21 = j10;
        long mulsub = mulsub(j16, j13, j10, j11, j20);
        long j22 = mulsub & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        if (i13 == 0) {
            j5 = 0;
        } else {
            if (mulsub >= 0) {
                j7 = mulsub / j21;
                j8 = mulsub - (j7 * j21);
            } else {
                long[] divRemNegativeLong2 = divRemNegativeLong(mulsub, j21);
                j7 = divRemNegativeLong2[1];
                j8 = divRemNegativeLong2[0];
            }
            long j23 = j8;
            mulsub = j7;
            j5 = j23;
        }
        while (true) {
            if (mulsub < 4294967296L) {
                j6 = j22;
                if (!unsignedLongCompare(mulsub * j11, make64(j5, j14))) {
                    break;
                }
            } else {
                j6 = j22;
            }
            mulsub--;
            j5 += j21;
            if (j5 >= 4294967296L) {
                break;
            }
            j22 = j6;
        }
        int i17 = (int) j20;
        if (i17 < 0) {
            MutableBigInteger mutableBigInteger = new MutableBigInteger(new int[]{i17, (int) mulsub});
            if (i11 == 1) {
                i8 = i2;
                i6 = i4;
                i7 = i;
                if (i8 == i6) {
                    return mutableBigInteger.toBigDecimal(i7, i8);
                }
            } else {
                i7 = i;
                i8 = i2;
                i6 = i4;
            }
            long j24 = j14;
            int i18 = i7;
            int i19 = i6;
            long mulsub2 = mulsub(j6, j24, j21, j11, mulsub) >>> i5;
            if (mulsub2 != 0) {
                MutableBigInteger mutableBigInteger2 = mutableBigInteger;
                int i20 = i8;
                int i21 = i18;
                if (needIncrement(j17 >>> i5, i3, i, mutableBigInteger2, mulsub2)) {
                    mutableBigInteger2.add(MutableBigInteger.ONE);
                }
                return mutableBigInteger2.toBigDecimal(i21, i20);
            }
            MutableBigInteger mutableBigInteger3 = mutableBigInteger;
            int i22 = i8;
            int i23 = i18;
            if (i19 != i22) {
                return createAndStripZerosToMatchScale(mutableBigInteger3.toBigInteger(i23), i22, (long) i19);
            }
            return mutableBigInteger3.toBigDecimal(i23, i22);
        }
        long j25 = j17;
        int i24 = i2;
        int i25 = i4;
        long j26 = (long) i;
        long make64 = make64(j20, mulsub) * j26;
        if (i11 == 1 && i24 == i25) {
            return valueOf(make64, i24);
        }
        long j27 = j26;
        long j28 = j14;
        int i26 = i25;
        int i27 = i24;
        long mulsub3 = mulsub(j6, j28, j21, j11, mulsub) >>> i5;
        if (mulsub3 != 0) {
            long j29 = make64;
            return valueOf(needIncrement(j25 >>> i5, i3, i, j29, mulsub3) ? j29 + j27 : j29, i27);
        }
        long j30 = make64;
        if (i26 != i27) {
            return createAndStripZerosToMatchScale(j30, i27, (long) i26);
        }
        return valueOf(j30, i27);
    }

    private static BigDecimal roundedTenPower(int i, int i2, int i3, int i4) {
        if (i3 <= i4) {
            return scaledTenPow(i2, i, i3);
        }
        int i5 = i3 - i4;
        if (i5 < i2) {
            return scaledTenPow(i2 - i5, i, i4);
        }
        return valueOf((long) i, i3 - i2);
    }

    static BigDecimal scaledTenPow(int i, int i2, int i3) {
        long[] jArr = LONG_TEN_POWERS_TABLE;
        if (i < jArr.length) {
            return valueOf(((long) i2) * jArr[i], i3);
        }
        BigInteger bigTenToThe = bigTenToThe(i);
        if (i2 == -1) {
            bigTenToThe = bigTenToThe.negate();
        }
        return new BigDecimal(bigTenToThe, Long.MIN_VALUE, i3, i + 1);
    }

    private static long[] divRemNegativeLong(long j, long j2) {
        long j3 = (j >>> 1) / (j2 >>> 1);
        long j4 = j - (j3 * j2);
        while (j4 < 0) {
            j4 += j2;
            j3--;
        }
        while (j4 >= j2) {
            j4 -= j2;
            j3++;
        }
        return new long[]{j4, j3};
    }

    private static long mulsub(long j, long j2, long j3, long j4, long j5) {
        long j6 = j2 - (j4 * j5);
        return make64((j + (j6 >>> 32)) - (j5 * j3), j6 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
    }

    private static int compareMagnitudeNormalized(long j, int i, long j2, int i2) {
        int i3 = i - i2;
        if (i3 != 0) {
            if (i3 < 0) {
                j = longMultiplyPowerTen(j, -i3);
            } else {
                j2 = longMultiplyPowerTen(j2, i3);
            }
        }
        if (j == Long.MIN_VALUE) {
            return 1;
        }
        if (j2 != Long.MIN_VALUE) {
            return longCompareMagnitude(j, j2);
        }
        return -1;
    }

    private static int compareMagnitudeNormalized(long j, int i, BigInteger bigInteger, int i2) {
        int i3;
        if (j != 0 && (i3 = i - i2) < 0) {
            int i4 = -i3;
            if (longMultiplyPowerTen(j, i4) == Long.MIN_VALUE) {
                return bigMultiplyPowerTen(j, i4).compareMagnitude(bigInteger);
            }
        }
        return -1;
    }

    private static int compareMagnitudeNormalized(BigInteger bigInteger, int i, BigInteger bigInteger2, int i2) {
        int i3 = i - i2;
        if (i3 < 0) {
            return bigMultiplyPowerTen(bigInteger, -i3).compareMagnitude(bigInteger2);
        }
        return bigInteger.compareMagnitude(bigMultiplyPowerTen(bigInteger2, i3));
    }

    private static long multiply(long j, long j2) {
        long j3 = j * j2;
        if (((Math.abs(j) | Math.abs(j2)) >>> 31) == 0 || j2 == 0 || j3 / j2 == j) {
            return j3;
        }
        return Long.MIN_VALUE;
    }

    private static BigDecimal multiply(long j, long j2, int i) {
        long multiply = multiply(j, j2);
        if (multiply != Long.MIN_VALUE) {
            return valueOf(multiply, i);
        }
        return new BigDecimal(BigInteger.valueOf(j).multiply(j2), Long.MIN_VALUE, i, 0);
    }

    private static BigDecimal multiply(long j, BigInteger bigInteger, int i) {
        if (j == 0) {
            return zeroValueOf(i);
        }
        return new BigDecimal(bigInteger.multiply(j), Long.MIN_VALUE, i, 0);
    }

    private static BigDecimal multiply(BigInteger bigInteger, BigInteger bigInteger2, int i) {
        return new BigDecimal(bigInteger.multiply(bigInteger2), Long.MIN_VALUE, i, 0);
    }

    private static BigDecimal multiplyAndRound(long j, long j2, int i, MathContext mathContext) {
        int i2;
        long j3;
        long j4 = j;
        long j5 = j2;
        MathContext mathContext2 = mathContext;
        long multiply = multiply(j, j2);
        if (multiply != Long.MIN_VALUE) {
            return doRound(multiply, i, mathContext2);
        }
        int i3 = i;
        if (j4 < 0) {
            j4 = -j4;
            i2 = -1;
        } else {
            i2 = 1;
        }
        long j6 = j4;
        if (j5 < 0) {
            i2 *= -1;
            j3 = -j5;
        } else {
            j3 = j5;
        }
        int i4 = i2;
        long j7 = j6 >>> 32;
        long j8 = j6 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j9 = j3 >>> 32;
        long j10 = j3 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j11 = j8 * j10;
        long j12 = j3;
        long j13 = j11 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j14 = (j10 * j7) + (j11 >>> 32);
        long j15 = (j8 * j9) + (j14 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
        long j16 = j6;
        long j17 = j15 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j18 = (j14 >>> 32) + (j15 >>> 32);
        long j19 = (j7 * j9) + (j18 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
        long j20 = j19 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        BigDecimal doRound128 = doRound128(make64(((j19 >>> 32) + (j18 >>> 32)) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER, j20), make64(j17, j13), i4, i, mathContext);
        if (doRound128 != null) {
            return doRound128;
        }
        return doRound(new BigDecimal(BigInteger.valueOf(j16).multiply(j12 * ((long) i4)), Long.MIN_VALUE, i, 0), mathContext);
    }

    private static BigDecimal multiplyAndRound(long j, BigInteger bigInteger, int i, MathContext mathContext) {
        if (j == 0) {
            return zeroValueOf(i);
        }
        return doRound(bigInteger.multiply(j), i, mathContext);
    }

    private static BigDecimal multiplyAndRound(BigInteger bigInteger, BigInteger bigInteger2, int i, MathContext mathContext) {
        return doRound(bigInteger.multiply(bigInteger2), i, mathContext);
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0034 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.math.BigDecimal doRound128(long r16, long r18, int r20, int r21, java.math.MathContext r22) {
        /*
            r0 = r22
            int r1 = r0.precision
            int r2 = precision(r16, r18)
            int r2 = r2 - r1
            r1 = 0
            if (r2 <= 0) goto L_0x002c
            long[] r3 = LONG_TEN_POWERS_TABLE
            int r4 = r3.length
            if (r2 >= r4) goto L_0x002c
            r4 = r21
            long r4 = (long) r4
            long r6 = (long) r2
            long r4 = r4 - r6
            int r15 = checkScaleNonZero(r4)
            r10 = r3[r2]
            java.math.RoundingMode r2 = r0.roundingMode
            int r14 = r2.oldMode
            r6 = r16
            r8 = r18
            r12 = r20
            r13 = r15
            java.math.BigDecimal r2 = divideAndRound128(r6, r8, r10, r12, r13, r14, r15)
            goto L_0x002d
        L_0x002c:
            r2 = r1
        L_0x002d:
            if (r2 == 0) goto L_0x0034
            java.math.BigDecimal r0 = doRound(r2, r0)
            return r0
        L_0x0034:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigDecimal.doRound128(long, long, int, int, java.math.MathContext):java.math.BigDecimal");
    }

    private static int precision(long j, long j2) {
        if (j != 0) {
            int numberOfLeadingZeros = (((128 - Long.numberOfLeadingZeros(j)) + 1) * 1233) >>> 12;
            int i = numberOfLeadingZeros - 19;
            long[][] jArr = LONGLONG_TEN_POWERS_TABLE;
            if (i >= jArr.length) {
                return numberOfLeadingZeros;
            }
            long[] jArr2 = jArr[i];
            return longLongCompareMagnitude(j, j2, jArr2[0], jArr2[1]) ? numberOfLeadingZeros : numberOfLeadingZeros + 1;
        } else if (j2 >= 0) {
            return longDigitLength(j2);
        } else {
            return unsignedLongCompareEq(j2, LONGLONG_TEN_POWERS_TABLE[0][1]) ? 20 : 19;
        }
    }

    private static BigDecimal divide(long j, int i, long j2, int i2, int i3, int i4) {
        long j3 = j;
        int i5 = i;
        long j4 = j2;
        int i6 = i2;
        int i7 = i3;
        long j5 = (long) i7;
        if (checkScale(j3, ((long) i6) + j5) > i5) {
            int i8 = (i6 + i7) - i5;
            long[] jArr = LONG_TEN_POWERS_TABLE;
            if (i8 < jArr.length) {
                long longMultiplyPowerTen = longMultiplyPowerTen(j3, i8);
                if (longMultiplyPowerTen != Long.MIN_VALUE) {
                    return divideAndRound(longMultiplyPowerTen, j2, i3, i4, i3);
                }
                BigDecimal multiplyDivideAndRound = multiplyDivideAndRound(jArr[i8], j, j2, i3, i4, i3);
                if (multiplyDivideAndRound != null) {
                    return multiplyDivideAndRound;
                }
            }
            return divideAndRound(bigMultiplyPowerTen(j3, i8), j2, i3, i4, i3);
        }
        int checkScale = checkScale(j4, ((long) i5) - j5) - i6;
        if (checkScale < LONG_TEN_POWERS_TABLE.length) {
            long longMultiplyPowerTen2 = longMultiplyPowerTen(j4, checkScale);
            if (longMultiplyPowerTen2 != Long.MIN_VALUE) {
                return divideAndRound(j, longMultiplyPowerTen2, i3, i4, i3);
            }
        }
        return divideAndRound(BigInteger.valueOf(j), bigMultiplyPowerTen(j4, checkScale), i7, i4, i7);
    }

    private static BigDecimal divide(BigInteger bigInteger, int i, long j, int i2, int i3, int i4) {
        long j2 = (long) i3;
        if (checkScale(bigInteger, ((long) i2) + j2) > i) {
            return divideAndRound(bigMultiplyPowerTen(bigInteger, (i2 + i3) - i), j, i3, i4, i3);
        }
        int checkScale = checkScale(j, ((long) i) - j2) - i2;
        if (checkScale < LONG_TEN_POWERS_TABLE.length) {
            long longMultiplyPowerTen = longMultiplyPowerTen(j, checkScale);
            if (longMultiplyPowerTen != Long.MIN_VALUE) {
                return divideAndRound(bigInteger, longMultiplyPowerTen, i3, i4, i3);
            }
        }
        return divideAndRound(bigInteger, bigMultiplyPowerTen(j, checkScale), i3, i4, i3);
    }

    private static BigDecimal divide(long j, int i, BigInteger bigInteger, int i2, int i3, int i4) {
        long j2 = (long) i3;
        if (checkScale(j, ((long) i2) + j2) > i) {
            return divideAndRound(bigMultiplyPowerTen(j, (i2 + i3) - i), bigInteger, i3, i4, i3);
        }
        return divideAndRound(BigInteger.valueOf(j), bigMultiplyPowerTen(bigInteger, checkScale(bigInteger, ((long) i) - j2) - i2), i3, i4, i3);
    }

    private static BigDecimal divide(BigInteger bigInteger, int i, BigInteger bigInteger2, int i2, int i3, int i4) {
        long j = (long) i3;
        if (checkScale(bigInteger, ((long) i2) + j) > i) {
            return divideAndRound(bigMultiplyPowerTen(bigInteger, (i2 + i3) - i), bigInteger2, i3, i4, i3);
        }
        return divideAndRound(bigInteger, bigMultiplyPowerTen(bigInteger2, checkScale(bigInteger2, ((long) i) - j) - i2), i3, i4, i3);
    }
}
