package jdk.internal.math;

import java.util.Arrays;
import java.util.regex.Pattern;

public class FloatingDecimal {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final ASCIIToBinaryConverter A2BC_NEGATIVE_INFINITY = new PreparedASCIIToBinaryBuffer(Double.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
    static final ASCIIToBinaryConverter A2BC_NEGATIVE_ZERO = new PreparedASCIIToBinaryBuffer(-0.0d, -0.0f);
    static final ASCIIToBinaryConverter A2BC_NOT_A_NUMBER = new PreparedASCIIToBinaryBuffer(Double.NaN, Float.NaN);
    static final ASCIIToBinaryConverter A2BC_POSITIVE_INFINITY = new PreparedASCIIToBinaryBuffer(Double.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    static final ASCIIToBinaryConverter A2BC_POSITIVE_ZERO = new PreparedASCIIToBinaryBuffer(0.0d, 0.0f);
    private static final BinaryToASCIIConverter B2AC_NEGATIVE_INFINITY = new ExceptionalBinaryToASCIIBuffer("-Infinity", true);
    private static final BinaryToASCIIConverter B2AC_NEGATIVE_ZERO = new BinaryToASCIIBuffer(true, new char[]{'0'});
    private static final BinaryToASCIIConverter B2AC_NOT_A_NUMBER = new ExceptionalBinaryToASCIIBuffer(NAN_REP, false);
    private static final BinaryToASCIIConverter B2AC_POSITIVE_INFINITY = new ExceptionalBinaryToASCIIBuffer(INFINITY_REP, false);
    private static final BinaryToASCIIConverter B2AC_POSITIVE_ZERO = new BinaryToASCIIBuffer(false, new char[]{'0'});
    static final int BIG_DECIMAL_EXPONENT = 324;
    static final long EXP_ONE = 4607182418800017408L;
    static final int EXP_SHIFT = 52;
    static final long FRACT_HOB = 4503599627370496L;
    private static final int INFINITY_LENGTH = 8;
    private static final String INFINITY_REP = "Infinity";
    static final int INT_DECIMAL_DIGITS = 9;
    static final int MAX_DECIMAL_DIGITS = 15;
    static final int MAX_DECIMAL_EXPONENT = 308;
    static final int MAX_NDIGITS = 1100;
    static final int MAX_SMALL_BIN_EXP = 62;
    static final int MIN_DECIMAL_EXPONENT = -324;
    static final int MIN_SMALL_BIN_EXP = -21;
    private static final int NAN_LENGTH = 3;
    private static final String NAN_REP = "NaN";
    static final int SINGLE_EXP_SHIFT = 23;
    static final int SINGLE_FRACT_HOB = 8388608;
    static final int SINGLE_MAX_DECIMAL_DIGITS = 7;
    static final int SINGLE_MAX_DECIMAL_EXPONENT = 38;
    static final int SINGLE_MAX_NDIGITS = 200;
    static final int SINGLE_MIN_DECIMAL_EXPONENT = -45;
    private static final ThreadLocal<BinaryToASCIIBuffer> threadLocalBinaryToASCIIBuffer = new ThreadLocal<BinaryToASCIIBuffer>() {
        /* access modifiers changed from: protected */
        public BinaryToASCIIBuffer initialValue() {
            return new BinaryToASCIIBuffer();
        }
    };

    interface ASCIIToBinaryConverter {
        double doubleValue();

        float floatValue();
    }

    public interface BinaryToASCIIConverter {
        void appendTo(Appendable appendable);

        boolean decimalDigitsExact();

        boolean digitsRoundedUp();

        int getDecimalExponent();

        int getDigits(char[] cArr);

        boolean isExceptional();

        boolean isNegative();

        String toJavaFormatString();
    }

    public static String toJavaFormatString(double d) {
        return getBinaryToASCIIConverter(d).toJavaFormatString();
    }

    public static String toJavaFormatString(float f) {
        return getBinaryToASCIIConverter(f).toJavaFormatString();
    }

    public static void appendTo(double d, Appendable appendable) {
        getBinaryToASCIIConverter(d).appendTo(appendable);
    }

    public static void appendTo(float f, Appendable appendable) {
        getBinaryToASCIIConverter(f).appendTo(appendable);
    }

    public static double parseDouble(String str) throws NumberFormatException {
        return readJavaFormatString(str).doubleValue();
    }

    public static float parseFloat(String str) throws NumberFormatException {
        return readJavaFormatString(str).floatValue();
    }

    private static class ExceptionalBinaryToASCIIBuffer implements BinaryToASCIIConverter {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final String image;
        private boolean isNegative;

        public boolean isExceptional() {
            return true;
        }

        static {
            Class<FloatingDecimal> cls = FloatingDecimal.class;
        }

        public ExceptionalBinaryToASCIIBuffer(String str, boolean z) {
            this.image = str;
            this.isNegative = z;
        }

        public String toJavaFormatString() {
            return this.image;
        }

        public void appendTo(Appendable appendable) {
            if (appendable instanceof StringBuilder) {
                ((StringBuilder) appendable).append(this.image);
            } else if (appendable instanceof StringBuffer) {
                ((StringBuffer) appendable).append(this.image);
            }
        }

        public int getDecimalExponent() {
            throw new IllegalArgumentException("Exceptional value does not have an exponent");
        }

        public int getDigits(char[] cArr) {
            throw new IllegalArgumentException("Exceptional value does not have digits");
        }

        public boolean isNegative() {
            return this.isNegative;
        }

        public boolean digitsRoundedUp() {
            throw new IllegalArgumentException("Exceptional value is not rounded");
        }

        public boolean decimalDigitsExact() {
            throw new IllegalArgumentException("Exceptional value is not exact");
        }
    }

    static class BinaryToASCIIBuffer implements BinaryToASCIIConverter {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final int[] N_5_BITS = {0, 3, 5, 7, 10, 12, 14, 17, 19, 21, 24, 26, 28, 31, 33, 35, 38, 40, 42, 45, 47, 49, 52, 54, 56, 59, 61};
        private static int[] insignificantDigitsNumber = {0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 17, 17, 17, 18, 18, 18, 19};
        private final char[] buffer;
        private int decExponent;
        private boolean decimalDigitsRoundedUp;
        private final char[] digits;
        private boolean exactDecimalConversion;
        private int firstDigitIndex;
        private boolean isNegative;
        private int nDigits;

        public boolean isExceptional() {
            return false;
        }

        static {
            Class<FloatingDecimal> cls = FloatingDecimal.class;
        }

        BinaryToASCIIBuffer() {
            this.buffer = new char[26];
            this.exactDecimalConversion = false;
            this.decimalDigitsRoundedUp = false;
            this.digits = new char[20];
        }

        BinaryToASCIIBuffer(boolean z, char[] cArr) {
            this.buffer = new char[26];
            this.exactDecimalConversion = false;
            this.decimalDigitsRoundedUp = false;
            this.isNegative = z;
            this.decExponent = 0;
            this.digits = cArr;
            this.firstDigitIndex = 0;
            this.nDigits = cArr.length;
        }

        public String toJavaFormatString() {
            return new String(this.buffer, 0, getChars(this.buffer));
        }

        public void appendTo(Appendable appendable) {
            int chars = getChars(this.buffer);
            if (appendable instanceof StringBuilder) {
                ((StringBuilder) appendable).append(this.buffer, 0, chars);
            } else if (appendable instanceof StringBuffer) {
                ((StringBuffer) appendable).append(this.buffer, 0, chars);
            }
        }

        public int getDecimalExponent() {
            return this.decExponent;
        }

        public int getDigits(char[] cArr) {
            System.arraycopy((Object) this.digits, this.firstDigitIndex, (Object) cArr, 0, this.nDigits);
            return this.nDigits;
        }

        public boolean isNegative() {
            return this.isNegative;
        }

        public boolean digitsRoundedUp() {
            return this.decimalDigitsRoundedUp;
        }

        public boolean decimalDigitsExact() {
            return this.exactDecimalConversion;
        }

        /* access modifiers changed from: private */
        public void setSign(boolean z) {
            this.isNegative = z;
        }

        private void developLongDigits(int i, long j, int i2) {
            if (i2 != 0) {
                long j2 = FDBigInteger.LONG_5_POW[i2] << i2;
                long j3 = j % j2;
                j /= j2;
                i += i2;
                if (j3 >= (j2 >> 1)) {
                    j++;
                }
            }
            int length = this.digits.length - 1;
            if (j <= 2147483647L) {
                int i3 = (int) j;
                int i4 = i3 % 10;
                int i5 = i3 / 10;
                while (i4 == 0) {
                    i++;
                    i4 = i5 % 10;
                    i5 /= 10;
                }
                while (i5 != 0) {
                    this.digits[length] = (char) (i4 + 48);
                    i++;
                    i4 = i5 % 10;
                    i5 /= 10;
                    length--;
                }
                this.digits[length] = (char) (i4 + 48);
            } else {
                int i6 = (int) (j % 10);
                long j4 = j / 10;
                while (i6 == 0) {
                    i++;
                    i6 = (int) (j4 % 10);
                    j4 /= 10;
                }
                while (j4 != 0) {
                    this.digits[length] = (char) (i6 + 48);
                    i++;
                    i6 = (int) (j4 % 10);
                    j4 /= 10;
                    length--;
                }
                this.digits[length] = (char) (i6 + 48);
            }
            this.decExponent = i + 1;
            this.firstDigitIndex = length;
            this.nDigits = this.digits.length - length;
        }

        /* access modifiers changed from: private */
        public void dtoa(int i, long j, int i2, boolean z) {
            boolean z2;
            boolean z3;
            int i3;
            long j2;
            int i4;
            boolean z4;
            boolean z5;
            int i5;
            int i6;
            boolean z6;
            boolean z7;
            int i7;
            long j3;
            boolean z8;
            boolean z9;
            boolean z10;
            boolean z11;
            int i8 = i;
            long j4 = j;
            int i9 = i2;
            int numberOfTrailingZeros = Long.numberOfTrailingZeros(j);
            int i10 = 53 - numberOfTrailingZeros;
            this.decimalDigitsRoundedUp = false;
            this.exactDecimalConversion = false;
            int max = Math.max(0, (i10 - i8) - 1);
            if (i8 > 62 || i8 < -21 || max >= FDBigInteger.LONG_5_POW.length || N_5_BITS[max] + i10 >= 64 || max != 0) {
                int estimateDecExp = estimateDecExp(j4, i8);
                int max2 = Math.max(0, -estimateDecExp);
                int i11 = max2 + max + i8;
                int max3 = Math.max(0, estimateDecExp);
                int i12 = max + max3;
                int i13 = i11 - i9;
                long j5 = j4 >>> numberOfTrailingZeros;
                int i14 = i11 - (i10 - 1);
                int min = Math.min(i14, i12);
                int i15 = i14 - min;
                int i16 = i12 - min;
                int i17 = i13 - min;
                if (i10 == 1) {
                    i17--;
                }
                if (i17 < 0) {
                    i15 -= i17;
                    i16 -= i17;
                    i17 = 0;
                }
                int i18 = i10 + i15;
                int[] iArr = N_5_BITS;
                int i19 = i18 + (max2 < iArr.length ? iArr[max2] : max2 * 3);
                int i20 = max3 + 1;
                int i21 = i16 + 1 + (i20 < iArr.length ? iArr[i20] : i20 * 3);
                if (i19 >= 64 || i21 >= 64) {
                    FDBigInteger valueOfPow52 = FDBigInteger.valueOfPow52(max3, i16);
                    int normalizationBias = valueOfPow52.getNormalizationBias();
                    FDBigInteger leftShift = valueOfPow52.leftShift(normalizationBias);
                    FDBigInteger valueOfMulPow52 = FDBigInteger.valueOfMulPow52(j5, max2, i15 + normalizationBias);
                    FDBigInteger valueOfPow522 = FDBigInteger.valueOfPow52(max2 + 1, i17 + normalizationBias + 1);
                    FDBigInteger valueOfPow523 = FDBigInteger.valueOfPow52(i20, i16 + normalizationBias + 1);
                    int quoRemIteration = valueOfMulPow52.quoRemIteration(leftShift);
                    boolean z12 = valueOfMulPow52.cmp(valueOfPow522) < 0;
                    boolean z13 = valueOfPow523.addAndCmp(valueOfMulPow52, valueOfPow522) <= 0;
                    if (quoRemIteration != 0 || z13) {
                        this.digits[0] = (char) (quoRemIteration + 48);
                        i4 = 1;
                    } else {
                        estimateDecExp--;
                        i4 = 0;
                    }
                    if (!z || estimateDecExp < -3 || estimateDecExp >= 8) {
                        i5 = i4;
                        z5 = false;
                        z4 = false;
                    } else {
                        z4 = z13;
                        z5 = z12;
                        i5 = i4;
                    }
                    while (!z3 && !z2) {
                        int quoRemIteration2 = valueOfMulPow52.quoRemIteration(leftShift);
                        valueOfPow522 = valueOfPow522.multBy10();
                        z5 = valueOfMulPow52.cmp(valueOfPow522) < 0;
                        z4 = valueOfPow523.addAndCmp(valueOfMulPow52, valueOfPow522) <= 0;
                        this.digits[i3] = (char) (quoRemIteration2 + 48);
                        i5 = i3 + 1;
                    }
                    if (!z2 || !z3) {
                        j2 = 0;
                    } else {
                        valueOfMulPow52 = valueOfMulPow52.leftShift(1);
                        j2 = (long) valueOfMulPow52.cmp(valueOfPow523);
                    }
                    this.exactDecimalConversion = valueOfMulPow52.cmp(FDBigInteger.ZERO) == 0;
                } else if (i19 >= 32 || i21 >= 32) {
                    long j6 = (j5 * FDBigInteger.LONG_5_POW[max2]) << i15;
                    long j7 = FDBigInteger.LONG_5_POW[max3] << i16;
                    long j8 = j7 * 10;
                    int i22 = (int) (j6 / j7);
                    long j9 = (j6 % j7) * 10;
                    long j10 = (FDBigInteger.LONG_5_POW[max2] << i17) * 10;
                    boolean z14 = j9 < j10;
                    boolean z15 = j9 + j10 > j8;
                    if (i22 != 0 || z15) {
                        this.digits[0] = (char) (i22 + 48);
                        i6 = 1;
                    } else {
                        estimateDecExp--;
                        i6 = 0;
                    }
                    if (!z || estimateDecExp < -3 || estimateDecExp >= 8) {
                        i7 = i6;
                        z7 = false;
                        j3 = j10;
                        z6 = false;
                    } else {
                        i7 = i6;
                        z7 = z14;
                        long j11 = j10;
                        z6 = z15;
                        j3 = j11;
                    }
                    while (!z3 && !z2) {
                        int i23 = (int) (j9 / j7);
                        j9 = (j9 % j7) * 10;
                        j3 *= 10;
                        if (j3 > 0) {
                            z8 = j9 < j3;
                            z9 = j9 + j3 > j8;
                        } else {
                            z8 = true;
                            z9 = true;
                        }
                        this.digits[i3] = (char) (i23 + 48);
                        i7 = i3 + 1;
                    }
                    long j12 = (j9 << 1) - j8;
                    this.exactDecimalConversion = j9 == 0;
                    j2 = j12;
                } else {
                    int i24 = (((int) j5) * FDBigInteger.SMALL_5_POW[max2]) << i15;
                    int i25 = FDBigInteger.SMALL_5_POW[max3] << i16;
                    int i26 = FDBigInteger.SMALL_5_POW[max2] << i17;
                    int i27 = i25 * 10;
                    int i28 = i24 / i25;
                    int i29 = (i24 % i25) * 10;
                    int i30 = i26 * 10;
                    z3 = i29 < i30;
                    z2 = i29 + i30 > i27;
                    if (i28 != 0 || z2) {
                        this.digits[0] = (char) (i28 + 48);
                        i3 = 1;
                    } else {
                        estimateDecExp--;
                        i3 = 0;
                    }
                    if (!z || estimateDecExp < -3 || estimateDecExp >= 8) {
                        z3 = false;
                        z2 = false;
                    }
                    while (!z3 && !z2) {
                        int i31 = i29 / i25;
                        i29 = (i29 % i25) * 10;
                        i30 *= 10;
                        if (((long) i30) > 0) {
                            z10 = i29 < i30;
                            z11 = i29 + i30 > i27;
                        } else {
                            z10 = true;
                            z11 = true;
                        }
                        this.digits[i3] = (char) (i31 + 48);
                        i3++;
                    }
                    j2 = (long) ((i29 << 1) - i27);
                    this.exactDecimalConversion = i29 == 0;
                }
                this.decExponent = estimateDecExp + 1;
                this.firstDigitIndex = 0;
                this.nDigits = i3;
                if (!z2) {
                    return;
                }
                if (z3) {
                    int i32 = (j2 > 0 ? 1 : (j2 == 0 ? 0 : -1));
                    if (i32 == 0) {
                        if ((1 & this.digits[(0 + i3) - 1]) != 0) {
                            roundup();
                        }
                    } else if (i32 > 0) {
                        roundup();
                    }
                } else {
                    roundup();
                }
            } else {
                developLongDigits(0, i8 >= 52 ? j4 << (i8 - 52) : j4 >>> (52 - i8), i8 > i9 ? insignificantDigitsForPow2((i8 - i9) - 1) : 0);
            }
        }

        private void roundup() {
            int i = (this.firstDigitIndex + this.nDigits) - 1;
            char c = this.digits[i];
            if (c == '9') {
                while (c == '9' && i > this.firstDigitIndex) {
                    char[] cArr = this.digits;
                    cArr[i] = '0';
                    i--;
                    c = cArr[i];
                }
                if (c == '9') {
                    this.decExponent++;
                    this.digits[this.firstDigitIndex] = '1';
                    return;
                }
            }
            this.digits[i] = (char) (c + 1);
            this.decimalDigitsRoundedUp = true;
        }

        static int estimateDecExp(long j, int i) {
            double longBitsToDouble = ((Double.longBitsToDouble((j & DoubleConsts.SIGNIF_BIT_MASK) | FloatingDecimal.EXP_ONE) - 1.5d) * 0.289529654d) + 0.176091259d + (((double) i) * 0.301029995663981d);
            long doubleToRawLongBits = Double.doubleToRawLongBits(longBitsToDouble);
            int i2 = ((int) ((DoubleConsts.EXP_BIT_MASK & doubleToRawLongBits) >> 52)) - 1023;
            boolean z = (Long.MIN_VALUE & doubleToRawLongBits) != 0;
            if (i2 >= 0 && i2 < 52) {
                long j2 = DoubleConsts.SIGNIF_BIT_MASK >> i2;
                int i3 = (int) (((DoubleConsts.SIGNIF_BIT_MASK & doubleToRawLongBits) | 4503599627370496L) >> (52 - i2));
                if (z) {
                    return (j2 & doubleToRawLongBits) == 0 ? -i3 : (-i3) - 1;
                }
                return i3;
            } else if (i2 < 0) {
                return ((Long.MAX_VALUE & doubleToRawLongBits) != 0 && z) ? -1 : 0;
            } else {
                return (int) longBitsToDouble;
            }
        }

        private static int insignificantDigits(int i) {
            int i2 = 0;
            while (true) {
                long j = (long) i;
                if (j < 10) {
                    return i2;
                }
                i = (int) (j / 10);
                i2++;
            }
        }

        private static int insignificantDigitsForPow2(int i) {
            if (i <= 1) {
                return 0;
            }
            int[] iArr = insignificantDigitsNumber;
            if (i < iArr.length) {
                return iArr[i];
            }
            return 0;
        }

        private int getChars(char[] cArr) {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            if (this.isNegative) {
                cArr[0] = '-';
                i4 = 1;
            }
            int i5 = this.decExponent;
            if (i5 > 0 && i5 < 8) {
                int min = Math.min(this.nDigits, i5);
                System.arraycopy((Object) this.digits, this.firstDigitIndex, (Object) cArr, i4, min);
                int i6 = i4 + min;
                int i7 = this.decExponent;
                if (min < i7) {
                    int i8 = (i7 - min) + i6;
                    Arrays.fill(cArr, i6, i8, '0');
                    int i9 = i8 + 1;
                    cArr[i8] = '.';
                    int i10 = i9 + 1;
                    cArr[i9] = '0';
                    return i10;
                }
                int i11 = i6 + 1;
                cArr[i6] = '.';
                int i12 = this.nDigits;
                if (min < i12) {
                    int i13 = i12 - min;
                    System.arraycopy((Object) this.digits, this.firstDigitIndex + min, (Object) cArr, i11, i13);
                    return i11 + i13;
                }
                int i14 = i11 + 1;
                cArr[i11] = '0';
                return i14;
            } else if (i5 > 0 || i5 <= -3) {
                int i15 = i4 + 1;
                char[] cArr2 = this.digits;
                int i16 = this.firstDigitIndex;
                cArr[i4] = cArr2[i16];
                int i17 = i15 + 1;
                cArr[i15] = '.';
                int i18 = this.nDigits;
                if (i18 > 1) {
                    System.arraycopy((Object) cArr2, i16 + 1, (Object) cArr, i17, i18 - 1);
                    i = i17 + (this.nDigits - 1);
                } else {
                    cArr[i17] = '0';
                    i = i17 + 1;
                }
                int i19 = i + 1;
                cArr[i] = 'E';
                int i20 = this.decExponent;
                if (i20 <= 0) {
                    cArr[i19] = '-';
                    i2 = (-i20) + 1;
                    i19++;
                } else {
                    i2 = i20 - 1;
                }
                if (i2 <= 9) {
                    i3 = i19 + 1;
                    cArr[i19] = (char) (i2 + 48);
                } else if (i2 <= 99) {
                    int i21 = i19 + 1;
                    cArr[i19] = (char) ((i2 / 10) + 48);
                    int i22 = i21 + 1;
                    cArr[i21] = (char) ((i2 % 10) + 48);
                    return i22;
                } else {
                    int i23 = i19 + 1;
                    cArr[i19] = (char) ((i2 / 100) + 48);
                    int i24 = i2 % 100;
                    int i25 = i23 + 1;
                    cArr[i23] = (char) ((i24 / 10) + 48);
                    i3 = i25 + 1;
                    cArr[i25] = (char) ((i24 % 10) + 48);
                }
                return i3;
            } else {
                int i26 = i4 + 1;
                cArr[i4] = '0';
                int i27 = i26 + 1;
                cArr[i26] = '.';
                if (i5 != 0) {
                    Arrays.fill(cArr, i27, i27 - i5, '0');
                    i27 -= this.decExponent;
                }
                System.arraycopy((Object) this.digits, this.firstDigitIndex, (Object) cArr, i27, this.nDigits);
                return i27 + this.nDigits;
            }
        }
    }

    private static BinaryToASCIIBuffer getBinaryToASCIIBuffer() {
        return threadLocalBinaryToASCIIBuffer.get();
    }

    static class PreparedASCIIToBinaryBuffer implements ASCIIToBinaryConverter {
        private final double doubleVal;
        private final float floatVal;

        public PreparedASCIIToBinaryBuffer(double d, float f) {
            this.doubleVal = d;
            this.floatVal = f;
        }

        public double doubleValue() {
            return this.doubleVal;
        }

        public float floatValue() {
            return this.floatVal;
        }
    }

    static class ASCIIToBinaryBuffer implements ASCIIToBinaryConverter {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final double[] BIG_10_POW = {1.0E16d, 1.0E32d, 1.0E64d, 1.0E128d, 1.0E256d};
        private static final int MAX_SMALL_TEN;
        private static final int SINGLE_MAX_SMALL_TEN;
        private static final float[] SINGLE_SMALL_10_POW;
        private static final double[] SMALL_10_POW;
        private static final double[] TINY_10_POW = {1.0E-16d, 1.0E-32d, 1.0E-64d, 1.0E-128d, 1.0E-256d};
        int decExponent;
        char[] digits;
        boolean isNegative;
        int nDigits;

        static {
            Class<FloatingDecimal> cls = FloatingDecimal.class;
            double[] dArr = {1.0d, 10.0d, 100.0d, 1000.0d, 10000.0d, 100000.0d, 1000000.0d, 1.0E7d, 1.0E8d, 1.0E9d, 1.0E10d, 1.0E11d, 1.0E12d, 1.0E13d, 1.0E14d, 1.0E15d, 1.0E16d, 1.0E17d, 1.0E18d, 1.0E19d, 1.0E20d, 1.0E21d, 1.0E22d};
            SMALL_10_POW = dArr;
            float[] fArr = {1.0f, 10.0f, 100.0f, 1000.0f, 10000.0f, 100000.0f, 1000000.0f, 1.0E7f, 1.0E8f, 1.0E9f, 1.0E10f};
            SINGLE_SMALL_10_POW = fArr;
            MAX_SMALL_TEN = dArr.length - 1;
            SINGLE_MAX_SMALL_TEN = fArr.length - 1;
        }

        ASCIIToBinaryBuffer(boolean z, int i, char[] cArr, int i2) {
            this.isNegative = z;
            this.decExponent = i;
            this.digits = cArr;
            this.nDigits = i2;
        }

        public double doubleValue() {
            long j;
            int i;
            int i2;
            boolean z;
            FDBigInteger fDBigInteger;
            boolean z2;
            int min = Math.min(this.nDigits, 16);
            int i3 = this.digits[0] - '0';
            int min2 = Math.min(min, 9);
            for (int i4 = 1; i4 < min2; i4++) {
                i3 = ((i3 * 10) + this.digits[i4]) - 48;
            }
            long j2 = (long) i3;
            while (min2 < min) {
                j2 = (j2 * 10) + ((long) (this.digits[min2] - '0'));
                min2++;
            }
            double d = (double) j2;
            int i5 = this.decExponent;
            int i6 = i5 - min;
            if (this.nDigits <= 15) {
                if (i6 == 0 || d == 0.0d) {
                    return this.isNegative ? -d : d;
                }
                if (i6 >= 0) {
                    int i7 = MAX_SMALL_TEN;
                    if (i6 <= i7) {
                        double d2 = d * SMALL_10_POW[i6];
                        return this.isNegative ? -d2 : d2;
                    }
                    int i8 = 15 - min;
                    if (i6 <= i7 + i8) {
                        double[] dArr = SMALL_10_POW;
                        double d3 = d * dArr[i8] * dArr[i6 - i8];
                        return this.isNegative ? -d3 : d3;
                    }
                } else if (i6 >= (-MAX_SMALL_TEN)) {
                    double d4 = d / SMALL_10_POW[-i6];
                    return this.isNegative ? -d4 : d4;
                }
            }
            if (i6 > 0) {
                if (i5 > 309) {
                    return this.isNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                }
                int i9 = i6 & 15;
                if (i9 != 0) {
                    d *= SMALL_10_POW[i9];
                }
                int i10 = i6 >> 4;
                if (i10 != 0) {
                    int i11 = 0;
                    while (i10 > 1) {
                        if ((i10 & 1) != 0) {
                            d *= BIG_10_POW[i11];
                        }
                        i11++;
                        i10 >>= 1;
                    }
                    double[] dArr2 = BIG_10_POW;
                    double d5 = dArr2[i11] * d;
                    if (!Double.isInfinite(d5)) {
                        d = d5;
                    } else if (!Double.isInfinite((d / 2.0d) * dArr2[i11])) {
                        d = Double.MAX_VALUE;
                    } else if (this.isNegative) {
                        return Double.NEGATIVE_INFINITY;
                    } else {
                        return Double.POSITIVE_INFINITY;
                    }
                }
            } else if (i6 < 0) {
                int i12 = -i6;
                if (i5 >= -325) {
                    int i13 = i12 & 15;
                    if (i13 != 0) {
                        d /= SMALL_10_POW[i13];
                    }
                    int i14 = i12 >> 4;
                    if (i14 != 0) {
                        int i15 = 0;
                        while (i14 > 1) {
                            if ((i14 & 1) != 0) {
                                d *= TINY_10_POW[i15];
                            }
                            i15++;
                            i14 >>= 1;
                        }
                        double d6 = TINY_10_POW[i15];
                        double d7 = d * d6;
                        if (d7 != 0.0d) {
                            d = d7;
                        } else if (d * 2.0d * d6 != 0.0d) {
                            d = Double.MIN_VALUE;
                        } else if (this.isNegative) {
                            return -0.0d;
                        } else {
                            return 0.0d;
                        }
                    }
                } else if (this.isNegative) {
                    return -0.0d;
                } else {
                    return 0.0d;
                }
            }
            if (this.nDigits > 1100) {
                this.nDigits = 1101;
                this.digits[1100] = '1';
            }
            FDBigInteger fDBigInteger2 = new FDBigInteger(j2, this.digits, min, this.nDigits);
            int i16 = this.decExponent - this.nDigits;
            long doubleToRawLongBits = Double.doubleToRawLongBits(d);
            int max = Math.max(0, -i16);
            int max2 = Math.max(0, i16);
            FDBigInteger multByPow52 = fDBigInteger2.multByPow52(max2, 0);
            multByPow52.makeImmutable();
            FDBigInteger fDBigInteger3 = null;
            int i17 = 0;
            while (true) {
                int i18 = (int) (doubleToRawLongBits >>> 52);
                long j3 = DoubleConsts.SIGNIF_BIT_MASK & doubleToRawLongBits;
                if (i18 > 0) {
                    j = j3 | 4503599627370496L;
                } else {
                    int numberOfLeadingZeros = Long.numberOfLeadingZeros(j3) - 11;
                    j = j3 << numberOfLeadingZeros;
                    i18 = 1 - numberOfLeadingZeros;
                }
                int i19 = i18 - 1023;
                int numberOfTrailingZeros = Long.numberOfTrailingZeros(j);
                long j4 = j >>> numberOfTrailingZeros;
                int i20 = (i19 - 52) + numberOfTrailingZeros;
                int i21 = 53 - numberOfTrailingZeros;
                if (i20 >= 0) {
                    i2 = max + i20;
                    i = max2;
                } else {
                    i = max2 - i20;
                    i2 = max;
                }
                int i22 = i19 <= -1023 ? i19 + numberOfTrailingZeros + 1023 : numberOfTrailingZeros + 1;
                int i23 = i2 + i22;
                int i24 = i + i22;
                int min3 = Math.min(i23, Math.min(i24, i2));
                int i25 = i24 - min3;
                int i26 = i2 - min3;
                FDBigInteger valueOfMulPow52 = FDBigInteger.valueOfMulPow52(j4, max, i23 - min3);
                if (fDBigInteger3 == null || i17 != i25) {
                    fDBigInteger3 = multByPow52.leftShift(i25);
                    i17 = i25;
                }
                int cmp = valueOfMulPow52.cmp(fDBigInteger3);
                if (cmp <= 0) {
                    z = true;
                    if (cmp >= 0) {
                        break;
                    }
                    fDBigInteger = fDBigInteger3.rightInplaceSub(valueOfMulPow52);
                    z2 = false;
                } else {
                    fDBigInteger = valueOfMulPow52.leftInplaceSub(fDBigInteger3);
                    z = true;
                    if (i21 != 1 || i20 <= -1022 || i26 - 1 >= 0) {
                        z2 = true;
                    } else {
                        fDBigInteger = fDBigInteger.leftShift(1);
                        z2 = true;
                        i26 = 0;
                    }
                }
                int cmpPow52 = fDBigInteger.cmpPow52(max, i26);
                if (cmpPow52 >= 0) {
                    long j5 = -1;
                    if (cmpPow52 != 0) {
                        if (!z2) {
                            j5 = 1;
                        }
                        doubleToRawLongBits += j5;
                        if (doubleToRawLongBits == 0 || doubleToRawLongBits == DoubleConsts.EXP_BIT_MASK) {
                            break;
                        }
                        boolean z3 = z;
                    } else if ((doubleToRawLongBits & 1) != 0) {
                        if (!z2) {
                            j5 = 1;
                        }
                        doubleToRawLongBits += j5;
                    }
                } else {
                    break;
                }
            }
            if (this.isNegative) {
                doubleToRawLongBits |= Long.MIN_VALUE;
            }
            return Double.longBitsToDouble(doubleToRawLongBits);
        }

        public float floatValue() {
            int i;
            int i2;
            int i3;
            int i4;
            boolean z;
            FDBigInteger fDBigInteger;
            int i5;
            int i6 = 8;
            int min = Math.min(this.nDigits, 8);
            int i7 = this.digits[0] - '0';
            for (int i8 = 1; i8 < min; i8++) {
                i7 = ((i7 * 10) + this.digits[i8]) - 48;
            }
            float f = (float) i7;
            int i9 = this.decExponent;
            int i10 = i9 - min;
            int i11 = this.nDigits;
            if (i11 <= 7) {
                if (i10 == 0 || f == 0.0f) {
                    return this.isNegative ? -f : f;
                }
                if (i10 >= 0) {
                    int i12 = SINGLE_MAX_SMALL_TEN;
                    if (i10 <= i12) {
                        float f2 = f * SINGLE_SMALL_10_POW[i10];
                        return this.isNegative ? -f2 : f2;
                    }
                    int i13 = 7 - min;
                    if (i10 <= i12 + i13) {
                        float[] fArr = SINGLE_SMALL_10_POW;
                        float f3 = f * fArr[i13] * fArr[i10 - i13];
                        return this.isNegative ? -f3 : f3;
                    }
                } else if (i10 >= (-SINGLE_MAX_SMALL_TEN)) {
                    float f4 = f / SINGLE_SMALL_10_POW[-i10];
                    return this.isNegative ? -f4 : f4;
                }
            } else if (i9 >= i11 && i11 + i9 <= 15) {
                long j = (long) i7;
                while (true) {
                    i5 = this.nDigits;
                    if (min >= i5) {
                        break;
                    }
                    j = (j * 10) + ((long) (this.digits[min] - '0'));
                    min++;
                }
                float f5 = (float) (((double) j) * SMALL_10_POW[this.decExponent - i5]);
                return this.isNegative ? -f5 : f5;
            }
            double d = (double) f;
            if (i10 > 0) {
                if (i9 > 39) {
                    return this.isNegative ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
                }
                int i14 = i10 & 15;
                if (i14 != 0) {
                    d *= SMALL_10_POW[i14];
                }
                int i15 = i10 >> 4;
                if (i15 != 0) {
                    int i16 = 0;
                    while (i15 > 0) {
                        if ((i15 & 1) != 0) {
                            d *= BIG_10_POW[i16];
                        }
                        i16++;
                        i15 >>= 1;
                    }
                }
            } else if (i10 < 0) {
                int i17 = -i10;
                if (i9 >= -46) {
                    int i18 = i17 & 15;
                    if (i18 != 0) {
                        d /= SMALL_10_POW[i18];
                    }
                    int i19 = i17 >> 4;
                    if (i19 != 0) {
                        int i20 = 0;
                        while (i19 > 0) {
                            if ((i19 & 1) != 0) {
                                d *= TINY_10_POW[i20];
                            }
                            i20++;
                            i19 >>= 1;
                        }
                    }
                } else if (this.isNegative) {
                    return -0.0f;
                } else {
                    return 0.0f;
                }
            }
            float max = Math.max(Float.MIN_VALUE, Math.min(Float.MAX_VALUE, (float) d));
            if (this.nDigits > 200) {
                this.nDigits = 201;
                this.digits[200] = '1';
            }
            FDBigInteger fDBigInteger2 = new FDBigInteger((long) i7, this.digits, min, this.nDigits);
            int i21 = this.decExponent - this.nDigits;
            int floatToRawIntBits = Float.floatToRawIntBits(max);
            int max2 = Math.max(0, -i21);
            int max3 = Math.max(0, i21);
            FDBigInteger multByPow52 = fDBigInteger2.multByPow52(max3, 0);
            multByPow52.makeImmutable();
            FDBigInteger fDBigInteger3 = null;
            int i22 = 0;
            while (true) {
                int i23 = floatToRawIntBits >>> 23;
                int i24 = 8388607 & floatToRawIntBits;
                if (i23 > 0) {
                    i = i24 | 8388608;
                } else {
                    int numberOfLeadingZeros = Integer.numberOfLeadingZeros(i24) - i6;
                    i = i24 << numberOfLeadingZeros;
                    i23 = 1 - numberOfLeadingZeros;
                }
                int i25 = i23 - 127;
                int numberOfTrailingZeros = Integer.numberOfTrailingZeros(i);
                int i26 = i >>> numberOfTrailingZeros;
                int i27 = (i25 - 23) + numberOfTrailingZeros;
                int i28 = 24 - numberOfTrailingZeros;
                if (i27 >= 0) {
                    i3 = max2 + i27;
                    i2 = max3;
                } else {
                    i2 = max3 - i27;
                    i3 = max2;
                }
                int i29 = i25 <= -127 ? i25 + numberOfTrailingZeros + 127 : numberOfTrailingZeros + 1;
                int i30 = i3 + i29;
                int i31 = i2 + i29;
                int min2 = Math.min(i30, Math.min(i31, i3));
                int i32 = i31 - min2;
                int i33 = i3 - min2;
                FDBigInteger valueOfMulPow52 = FDBigInteger.valueOfMulPow52((long) i26, max2, i30 - min2);
                if (fDBigInteger3 == null || i22 != i32) {
                    fDBigInteger3 = multByPow52.leftShift(i32);
                    i22 = i32;
                }
                int cmp = valueOfMulPow52.cmp(fDBigInteger3);
                if (cmp <= 0) {
                    i4 = 1;
                    if (cmp >= 0) {
                        break;
                    }
                    fDBigInteger = fDBigInteger3.rightInplaceSub(valueOfMulPow52);
                    z = false;
                } else {
                    fDBigInteger = valueOfMulPow52.leftInplaceSub(fDBigInteger3);
                    i4 = 1;
                    if (i28 != 1 || i27 <= -126 || i33 - 1 >= 0) {
                        z = true;
                    } else {
                        fDBigInteger = fDBigInteger.leftShift(1);
                        z = true;
                        i33 = 0;
                    }
                }
                int cmpPow52 = fDBigInteger.cmpPow52(max2, i33);
                if (cmpPow52 >= 0) {
                    int i34 = -1;
                    if (cmpPow52 != 0) {
                        if (!z) {
                            i34 = i4;
                        }
                        floatToRawIntBits += i34;
                        if (floatToRawIntBits == 0 || floatToRawIntBits == 2139095040) {
                            break;
                        }
                        i6 = 8;
                    } else if ((floatToRawIntBits & 1) != 0) {
                        if (z) {
                            i4 = -1;
                        }
                        floatToRawIntBits += i4;
                    }
                } else {
                    break;
                }
            }
            if (this.isNegative) {
                floatToRawIntBits |= Integer.MIN_VALUE;
            }
            return Float.intBitsToFloat(floatToRawIntBits);
        }
    }

    public static BinaryToASCIIConverter getBinaryToASCIIConverter(double d) {
        return getBinaryToASCIIConverter(d, true);
    }

    static BinaryToASCIIConverter getBinaryToASCIIConverter(double d, boolean z) {
        int i;
        long j;
        long doubleToRawLongBits = Double.doubleToRawLongBits(d);
        boolean z2 = (Long.MIN_VALUE & doubleToRawLongBits) != 0;
        long j2 = DoubleConsts.SIGNIF_BIT_MASK & doubleToRawLongBits;
        int i2 = (int) ((doubleToRawLongBits & DoubleConsts.EXP_BIT_MASK) >> 52);
        if (i2 != 2047) {
            if (i2 != 0) {
                j = j2 | 4503599627370496L;
                i = 53;
            } else if (j2 == 0) {
                return z2 ? B2AC_NEGATIVE_ZERO : B2AC_POSITIVE_ZERO;
            } else {
                int numberOfLeadingZeros = Long.numberOfLeadingZeros(j2);
                int i3 = numberOfLeadingZeros - 11;
                j = j2 << i3;
                i = 64 - numberOfLeadingZeros;
                i2 = 1 - i3;
            }
            long j3 = j;
            int i4 = i2 - 1023;
            BinaryToASCIIBuffer binaryToASCIIBuffer = getBinaryToASCIIBuffer();
            binaryToASCIIBuffer.setSign(z2);
            binaryToASCIIBuffer.dtoa(i4, j3, i, z);
            return binaryToASCIIBuffer;
        } else if (j2 == 0) {
            return z2 ? B2AC_NEGATIVE_INFINITY : B2AC_POSITIVE_INFINITY;
        } else {
            return B2AC_NOT_A_NUMBER;
        }
    }

    private static BinaryToASCIIConverter getBinaryToASCIIConverter(float f) {
        int i;
        int i2;
        int floatToRawIntBits = Float.floatToRawIntBits(f);
        boolean z = (Integer.MIN_VALUE & floatToRawIntBits) != 0;
        int i3 = 8388607 & floatToRawIntBits;
        int i4 = (floatToRawIntBits & FloatConsts.EXP_BIT_MASK) >> 23;
        if (i4 != 255) {
            if (i4 != 0) {
                i2 = i3 | 8388608;
                i = 24;
            } else if (i3 == 0) {
                return z ? B2AC_NEGATIVE_ZERO : B2AC_POSITIVE_ZERO;
            } else {
                int numberOfLeadingZeros = Integer.numberOfLeadingZeros(i3);
                int i5 = numberOfLeadingZeros - 8;
                i2 = i3 << i5;
                i = 32 - numberOfLeadingZeros;
                i4 = 1 - i5;
            }
            BinaryToASCIIBuffer binaryToASCIIBuffer = getBinaryToASCIIBuffer();
            binaryToASCIIBuffer.setSign(z);
            binaryToASCIIBuffer.dtoa(i4 - 127, ((long) i2) << 29, i, true);
            return binaryToASCIIBuffer;
        } else if (((long) i3) == 0) {
            return z ? B2AC_NEGATIVE_INFINITY : B2AC_POSITIVE_INFINITY;
        } else {
            return B2AC_NOT_A_NUMBER;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:109:0x012a A[Catch:{ StringIndexOutOfBoundsException -> 0x0181 }] */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x0140 A[Catch:{ StringIndexOutOfBoundsException -> 0x0181 }] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0028 A[Catch:{ StringIndexOutOfBoundsException -> 0x0181 }] */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x0123 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0038 A[Catch:{ StringIndexOutOfBoundsException -> 0x0181 }] */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x010a A[Catch:{ StringIndexOutOfBoundsException -> 0x0181 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static jdk.internal.math.FloatingDecimal.ASCIIToBinaryConverter readJavaFormatString(java.lang.String r17) throws java.lang.NumberFormatException {
        /*
            java.lang.String r0 = r17.trim()     // Catch:{ StringIndexOutOfBoundsException -> 0x017f }
            int r1 = r0.length()     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            if (r1 == 0) goto L_0x0177
            r2 = 0
            char r3 = r0.charAt(r2)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r4 = 45
            r5 = 43
            if (r3 == r5) goto L_0x001d
            if (r3 == r4) goto L_0x001b
            r3 = r2
            r7 = r3
            r8 = r7
            goto L_0x0020
        L_0x001b:
            r3 = 1
            goto L_0x001e
        L_0x001d:
            r3 = r2
        L_0x001e:
            r7 = 1
            r8 = 1
        L_0x0020:
            char r9 = r0.charAt(r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r10 = 78
            if (r9 != r10) goto L_0x0038
            int r1 = r1 - r7
            int r2 = NAN_LENGTH     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            if (r1 != r2) goto L_0x0181
            java.lang.String r1 = "NaN"
            int r1 = r0.indexOf((java.lang.String) r1, (int) r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            if (r1 != r7) goto L_0x0181
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_NOT_A_NUMBER     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            return r0
        L_0x0038:
            r10 = 73
            if (r9 != r10) goto L_0x0051
            int r1 = r1 - r7
            int r2 = INFINITY_LENGTH     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            if (r1 != r2) goto L_0x0181
            java.lang.String r1 = "Infinity"
            int r1 = r0.indexOf((java.lang.String) r1, (int) r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            if (r1 != r7) goto L_0x0181
            if (r3 == 0) goto L_0x004e
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_NEGATIVE_INFINITY     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            goto L_0x0050
        L_0x004e:
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_POSITIVE_INFINITY     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
        L_0x0050:
            return r0
        L_0x0051:
            r10 = 48
            if (r9 != r10) goto L_0x006a
            int r9 = r7 + 1
            if (r1 <= r9) goto L_0x006a
            char r9 = r0.charAt(r9)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r11 = 120(0x78, float:1.68E-43)
            if (r9 == r11) goto L_0x0065
            r11 = 88
            if (r9 != r11) goto L_0x006a
        L_0x0065:
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = parseHexString(r0)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            return r0
        L_0x006a:
            char[] r9 = new char[r1]     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r11 = r2
            r12 = r11
            r13 = r12
        L_0x006f:
            java.lang.String r14 = "multiple points"
            r15 = 46
            if (r7 >= r1) goto L_0x0094
            char r2 = r0.charAt(r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            if (r2 != r10) goto L_0x007e
            int r11 = r11 + 1
            goto L_0x008a
        L_0x007e:
            if (r2 != r15) goto L_0x0094
            if (r12 != 0) goto L_0x008e
            if (r8 == 0) goto L_0x0088
            int r2 = r7 + -1
            r13 = r2
            goto L_0x0089
        L_0x0088:
            r13 = r7
        L_0x0089:
            r12 = 1
        L_0x008a:
            int r7 = r7 + 1
            r2 = 0
            goto L_0x006f
        L_0x008e:
            java.lang.NumberFormatException r1 = new java.lang.NumberFormatException     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r1.<init>(r14)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            throw r1     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
        L_0x0094:
            r2 = 0
            r16 = 0
        L_0x0097:
            r6 = 57
            if (r7 >= r1) goto L_0x00d0
            char r4 = r0.charAt(r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r5 = 49
            if (r4 < r5) goto L_0x00ad
            if (r4 > r6) goto L_0x00ad
            int r5 = r2 + 1
            r9[r2] = r4     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r2 = r5
            r16 = 0
            goto L_0x00c3
        L_0x00ad:
            if (r4 != r10) goto L_0x00b7
            int r5 = r2 + 1
            r9[r2] = r4     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            int r16 = r16 + 1
            r2 = r5
            goto L_0x00c3
        L_0x00b7:
            if (r4 != r15) goto L_0x00d0
            if (r12 != 0) goto L_0x00ca
            if (r8 == 0) goto L_0x00c0
            int r4 = r7 + -1
            goto L_0x00c1
        L_0x00c0:
            r4 = r7
        L_0x00c1:
            r13 = r4
            r12 = 1
        L_0x00c3:
            int r7 = r7 + 1
            r4 = 45
            r5 = 43
            goto L_0x0097
        L_0x00ca:
            java.lang.NumberFormatException r1 = new java.lang.NumberFormatException     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r1.<init>(r14)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            throw r1     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
        L_0x00d0:
            int r2 = r2 - r16
            if (r2 != 0) goto L_0x00d6
            r4 = 1
            goto L_0x00d7
        L_0x00d6:
            r4 = 0
        L_0x00d7:
            if (r4 == 0) goto L_0x00db
            if (r11 == 0) goto L_0x0181
        L_0x00db:
            if (r12 == 0) goto L_0x00df
            int r13 = r13 - r11
            goto L_0x00e1
        L_0x00df:
            int r13 = r2 + r16
        L_0x00e1:
            if (r7 >= r1) goto L_0x0141
            char r5 = r0.charAt(r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r8 = 101(0x65, float:1.42E-43)
            if (r5 == r8) goto L_0x00ef
            r8 = 69
            if (r5 != r8) goto L_0x0141
        L_0x00ef:
            int r7 = r7 + 1
            char r5 = r0.charAt(r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r8 = -1
            r11 = 43
            if (r5 == r11) goto L_0x0102
            r11 = 45
            if (r5 == r11) goto L_0x0100
            r5 = 1
            goto L_0x0105
        L_0x0100:
            r5 = r8
            goto L_0x0103
        L_0x0102:
            r5 = 1
        L_0x0103:
            int r7 = r7 + 1
        L_0x0105:
            r14 = r7
            r11 = 0
            r12 = 0
        L_0x0108:
            if (r14 >= r1) goto L_0x0123
            r15 = 214748364(0xccccccc, float:3.1554434E-31)
            if (r11 < r15) goto L_0x0110
            r12 = 1
        L_0x0110:
            int r15 = r14 + 1
            char r14 = r0.charAt(r14)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            if (r14 < r10) goto L_0x0121
            if (r14 > r6) goto L_0x0121
            int r11 = r11 * 10
            int r14 = r14 + -48
            int r11 = r11 + r14
            r14 = r15
            goto L_0x0108
        L_0x0121:
            int r15 = r15 + r8
            goto L_0x0124
        L_0x0123:
            r15 = r14
        L_0x0124:
            int r6 = r2 + 324
            int r6 = r6 + r16
            if (r12 != 0) goto L_0x0130
            if (r11 <= r6) goto L_0x012d
            goto L_0x0130
        L_0x012d:
            int r5 = r5 * r11
            int r13 = r13 + r5
            goto L_0x013e
        L_0x0130:
            if (r12 != 0) goto L_0x013c
            r8 = 1
            if (r5 != r8) goto L_0x013c
            if (r13 >= 0) goto L_0x013c
            int r11 = r11 + r13
            if (r11 >= r6) goto L_0x013c
            r13 = r11
            goto L_0x013e
        L_0x013c:
            int r5 = r5 * r6
            r13 = r5
        L_0x013e:
            if (r15 == r7) goto L_0x0181
            r7 = r15
        L_0x0141:
            if (r7 >= r1) goto L_0x0167
            r5 = 1
            int r1 = r1 - r5
            if (r7 != r1) goto L_0x0181
            char r1 = r0.charAt(r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r5 = 102(0x66, float:1.43E-43)
            if (r1 == r5) goto L_0x0167
            char r1 = r0.charAt(r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r5 = 70
            if (r1 == r5) goto L_0x0167
            char r1 = r0.charAt(r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r5 = 100
            if (r1 == r5) goto L_0x0167
            char r1 = r0.charAt(r7)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r5 = 68
            if (r1 != r5) goto L_0x0181
        L_0x0167:
            if (r4 == 0) goto L_0x0171
            if (r3 == 0) goto L_0x016e
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_NEGATIVE_ZERO     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            goto L_0x0170
        L_0x016e:
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_POSITIVE_ZERO     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
        L_0x0170:
            return r0
        L_0x0171:
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryBuffer r1 = new jdk.internal.math.FloatingDecimal$ASCIIToBinaryBuffer     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            r1.<init>(r3, r13, r9, r2)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            return r1
        L_0x0177:
            java.lang.NumberFormatException r1 = new java.lang.NumberFormatException     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            java.lang.String r2 = "empty String"
            r1.<init>(r2)     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
            throw r1     // Catch:{ StringIndexOutOfBoundsException -> 0x0181 }
        L_0x017f:
            r0 = r17
        L_0x0181:
            java.lang.NumberFormatException r1 = new java.lang.NumberFormatException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "For input string: \""
            r2.<init>((java.lang.String) r3)
            r2.append((java.lang.String) r0)
            java.lang.String r0 = "\""
            r2.append((java.lang.String) r0)
            java.lang.String r0 = r2.toString()
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.math.FloatingDecimal.readJavaFormatString(java.lang.String):jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter");
    }

    private static class HexFloatPattern {
        /* access modifiers changed from: private */
        public static final Pattern VALUE = Pattern.compile("([-+])?0[xX](((\\p{XDigit}+)\\.?)|((\\p{XDigit}*)\\.(\\p{XDigit}+)))[pP]([-+])?(\\p{Digit}+)[fFdD]?");

        private HexFloatPattern() {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:75:0x013f, code lost:
        if ((r3 & 1) != 0) goto L_0x0157;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0155, code lost:
        if ((r3 & 3) != 0) goto L_0x0157;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0159, code lost:
        r3 = false;
     */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x017f  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x01f2  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x01fa  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x016f A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x017d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static jdk.internal.math.FloatingDecimal.ASCIIToBinaryConverter parseHexString(java.lang.String r29) {
        /*
            r0 = r29
            java.util.regex.Pattern r1 = jdk.internal.math.FloatingDecimal.HexFloatPattern.VALUE
            java.util.regex.Matcher r1 = r1.matcher(r0)
            boolean r2 = r1.matches()
            if (r2 == 0) goto L_0x0295
            r0 = 1
            java.lang.String r2 = r1.group((int) r0)
            r3 = 0
            if (r2 == 0) goto L_0x0022
            java.lang.String r4 = "-"
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x0022
            r2 = r0
            goto L_0x0023
        L_0x0022:
            r2 = r3
        L_0x0023:
            r4 = 4
            java.lang.String r5 = r1.group((int) r4)
            if (r5 == 0) goto L_0x0034
            java.lang.String r5 = stripLeadingZeros(r5)
            int r6 = r5.length()
            r8 = r3
            goto L_0x005d
        L_0x0034:
            r5 = 6
            java.lang.String r5 = r1.group((int) r5)
            java.lang.String r5 = stripLeadingZeros(r5)
            int r6 = r5.length()
            r7 = 7
            java.lang.String r7 = r1.group((int) r7)
            int r8 = r7.length()
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            if (r5 != 0) goto L_0x0053
            java.lang.String r5 = ""
        L_0x0053:
            r9.append((java.lang.String) r5)
            r9.append((java.lang.String) r7)
            java.lang.String r5 = r9.toString()
        L_0x005d:
            java.lang.String r5 = stripLeadingZeros(r5)
            int r7 = r5.length()
            r9 = -4
            if (r6 < r0) goto L_0x006b
            int r6 = r6 - r0
            int r6 = r6 * r4
            goto L_0x006f
        L_0x006b:
            int r8 = r8 - r7
            int r8 = r8 + r0
            int r6 = r8 * -4
        L_0x006f:
            if (r7 != 0) goto L_0x0079
            if (r2 == 0) goto L_0x0076
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_NEGATIVE_ZERO
            goto L_0x0078
        L_0x0076:
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_POSITIVE_ZERO
        L_0x0078:
            return r0
        L_0x0079:
            r4 = 8
            java.lang.String r4 = r1.group((int) r4)
            if (r4 == 0) goto L_0x008c
            java.lang.String r8 = "+"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x008a
            goto L_0x008c
        L_0x008a:
            r4 = r3
            goto L_0x008d
        L_0x008c:
            r4 = r0
        L_0x008d:
            r8 = 9
            java.lang.String r1 = r1.group((int) r8)     // Catch:{ NumberFormatException -> 0x0283 }
            int r1 = java.lang.Integer.parseInt(r1)     // Catch:{ NumberFormatException -> 0x0283 }
            long r10 = (long) r1
            r14 = 1
            if (r4 == 0) goto L_0x009f
            r16 = r14
            goto L_0x00a1
        L_0x009f:
            r16 = -1
        L_0x00a1:
            long r16 = r16 * r10
            long r10 = (long) r6
            long r16 = r16 + r10
            int r1 = getHexDigit(r5, r3)
            long r10 = (long) r1
            int r1 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
            r18 = 2
            r20 = 7
            r4 = 52
            r22 = 3
            r24 = 0
            if (r1 != 0) goto L_0x00c1
            long r10 = r10 << r4
            long r10 = r10 | r24
            r1 = 48
        L_0x00be:
            r12 = r16
            goto L_0x00ed
        L_0x00c1:
            int r1 = (r10 > r22 ? 1 : (r10 == r22 ? 0 : -1))
            if (r1 > 0) goto L_0x00cf
            r1 = 51
            long r10 = r10 << r1
            long r10 = r10 | r24
            long r16 = r16 + r14
            r1 = 47
            goto L_0x00be
        L_0x00cf:
            int r1 = (r10 > r20 ? 1 : (r10 == r20 ? 0 : -1))
            if (r1 > 0) goto L_0x00dd
            r1 = 50
            long r10 = r10 << r1
            long r10 = r10 | r24
            long r16 = r16 + r18
            r1 = 46
            goto L_0x00be
        L_0x00dd:
            r26 = 15
            int r1 = (r10 > r26 ? 1 : (r10 == r26 ? 0 : -1))
            if (r1 > 0) goto L_0x027b
            r1 = 49
            long r10 = r10 << r1
            long r10 = r10 | r24
            long r16 = r16 + r22
            r1 = 45
            goto L_0x00be
        L_0x00ed:
            r6 = r0
        L_0x00ee:
            if (r6 >= r7) goto L_0x0101
            if (r1 < 0) goto L_0x0101
            int r8 = getHexDigit(r5, r6)
            long r3 = (long) r8
            long r3 = r3 << r1
            long r10 = r10 | r3
            int r1 = r1 + -4
            int r6 = r6 + 1
            r3 = 0
            r4 = 52
            goto L_0x00ee
        L_0x0101:
            if (r6 >= r7) goto L_0x0183
            int r3 = getHexDigit(r5, r6)
            long r3 = (long) r3
            r27 = 8
            if (r1 == r9) goto L_0x015c
            r8 = -3
            if (r1 == r8) goto L_0x0142
            r8 = -2
            if (r1 == r8) goto L_0x012d
            r8 = -1
            if (r1 != r8) goto L_0x0125
            r8 = 14
            long r8 = r8 & r3
            long r8 = r8 >> r0
            long r8 = r8 | r10
            long r3 = r3 & r14
            int r1 = (r3 > r24 ? 1 : (r3 == r24 ? 0 : -1))
            if (r1 == 0) goto L_0x0121
            r1 = r0
            goto L_0x0122
        L_0x0121:
            r1 = 0
        L_0x0122:
            r10 = r8
        L_0x0123:
            r3 = 0
            goto L_0x016c
        L_0x0125:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            java.lang.String r1 = "Unexpected shift distance remainder."
            r0.<init>((java.lang.Object) r1)
            throw r0
        L_0x012d:
            r8 = 12
            long r8 = r8 & r3
            r1 = 2
            long r8 = r8 >> r1
            long r8 = r8 | r10
            long r10 = r3 & r18
            int r1 = (r10 > r24 ? 1 : (r10 == r24 ? 0 : -1))
            if (r1 == 0) goto L_0x013b
            r1 = r0
            goto L_0x013c
        L_0x013b:
            r1 = 0
        L_0x013c:
            long r3 = r3 & r14
            int r3 = (r3 > r24 ? 1 : (r3 == r24 ? 0 : -1))
            if (r3 == 0) goto L_0x0159
            goto L_0x0157
        L_0x0142:
            long r8 = r3 & r27
            r1 = 3
            long r8 = r8 >> r1
            long r8 = r8 | r10
            r10 = 4
            long r10 = r10 & r3
            int r1 = (r10 > r24 ? 1 : (r10 == r24 ? 0 : -1))
            if (r1 == 0) goto L_0x0150
            r1 = r0
            goto L_0x0151
        L_0x0150:
            r1 = 0
        L_0x0151:
            long r3 = r3 & r22
            int r3 = (r3 > r24 ? 1 : (r3 == r24 ? 0 : -1))
            if (r3 == 0) goto L_0x0159
        L_0x0157:
            r3 = r0
            goto L_0x015a
        L_0x0159:
            r3 = 0
        L_0x015a:
            r10 = r8
            goto L_0x016c
        L_0x015c:
            long r8 = r3 & r27
            int r1 = (r8 > r24 ? 1 : (r8 == r24 ? 0 : -1))
            if (r1 == 0) goto L_0x0164
            r1 = r0
            goto L_0x0165
        L_0x0164:
            r1 = 0
        L_0x0165:
            long r3 = r3 & r20
            int r3 = (r3 > r24 ? 1 : (r3 == r24 ? 0 : -1))
            if (r3 == 0) goto L_0x0123
            r3 = r0
        L_0x016c:
            int r6 = r6 + r0
        L_0x016d:
            if (r6 >= r7) goto L_0x0185
            if (r3 != 0) goto L_0x0185
            int r4 = getHexDigit(r5, r6)
            long r8 = (long) r4
            if (r3 != 0) goto L_0x017f
            int r3 = (r8 > r24 ? 1 : (r8 == r24 ? 0 : -1))
            if (r3 == 0) goto L_0x017d
            goto L_0x017f
        L_0x017d:
            r3 = 0
            goto L_0x0180
        L_0x017f:
            r3 = r0
        L_0x0180:
            int r6 = r6 + 1
            goto L_0x016d
        L_0x0183:
            r1 = 0
            r3 = 0
        L_0x0185:
            if (r2 == 0) goto L_0x018a
            r4 = -2147483648(0xffffffff80000000, float:-0.0)
            goto L_0x018b
        L_0x018a:
            r4 = 0
        L_0x018b:
            r5 = -126(0xffffffffffffff82, double:NaN)
            int r5 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r5 < 0) goto L_0x01c0
            r5 = 127(0x7f, double:6.27E-322)
            int r5 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x019b
            r5 = 2139095040(0x7f800000, float:Infinity)
        L_0x0199:
            r4 = r4 | r5
            goto L_0x01e8
        L_0x019b:
            r5 = 268435455(0xfffffff, double:1.326247364E-315)
            long r5 = r5 & r10
            int r5 = (r5 > r24 ? 1 : (r5 == r24 ? 0 : -1))
            if (r5 != 0) goto L_0x01aa
            if (r1 != 0) goto L_0x01aa
            if (r3 == 0) goto L_0x01a8
            goto L_0x01aa
        L_0x01a8:
            r5 = 0
            goto L_0x01ab
        L_0x01aa:
            r5 = r0
        L_0x01ab:
            r6 = 28
            long r6 = r10 >>> r6
            int r6 = (int) r6
            r7 = r6 & 3
            if (r7 != r0) goto L_0x01b6
            if (r5 == 0) goto L_0x01b8
        L_0x01b6:
            int r6 = r6 + 1
        L_0x01b8:
            int r5 = (int) r12
            int r5 = r5 + 126
            int r5 = r5 << 23
            int r6 = r6 >> r0
            int r5 = r5 + r6
            goto L_0x0199
        L_0x01c0:
            r5 = -150(0xffffffffffffff6a, double:NaN)
            int r5 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r5 >= 0) goto L_0x01c7
            goto L_0x01e8
        L_0x01c7:
            r5 = -98
            long r5 = r5 - r12
            int r5 = (int) r5
            long r6 = r14 << r5
            long r6 = r6 - r14
            long r6 = r6 & r10
            int r6 = (r6 > r24 ? 1 : (r6 == r24 ? 0 : -1))
            if (r6 != 0) goto L_0x01da
            if (r1 != 0) goto L_0x01da
            if (r3 == 0) goto L_0x01d8
            goto L_0x01da
        L_0x01d8:
            r6 = 0
            goto L_0x01db
        L_0x01da:
            r6 = r0
        L_0x01db:
            long r7 = r10 >>> r5
            int r5 = (int) r7
            r7 = r5 & 3
            if (r7 != r0) goto L_0x01e4
            if (r6 == 0) goto L_0x01e6
        L_0x01e4:
            int r5 = r5 + 1
        L_0x01e6:
            int r5 = r5 >> r0
            goto L_0x0199
        L_0x01e8:
            float r4 = java.lang.Float.intBitsToFloat(r4)
            r5 = 1023(0x3ff, double:5.054E-321)
            int r7 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x01fa
            if (r2 == 0) goto L_0x01f7
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_NEGATIVE_INFINITY
            goto L_0x01f9
        L_0x01f7:
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_POSITIVE_INFINITY
        L_0x01f9:
            return r0
        L_0x01fa:
            r8 = 4503599627370495(0xfffffffffffff, double:2.225073858507201E-308)
            if (r7 > 0) goto L_0x0213
            r18 = -1022(0xfffffffffffffc02, double:NaN)
            int r7 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1))
            if (r7 < 0) goto L_0x0213
            long r12 = r12 + r5
            r5 = 52
            long r5 = r12 << r5
            r12 = 9218868437227405312(0x7ff0000000000000, double:Infinity)
            long r5 = r5 & r12
            long r7 = r10 & r8
            long r5 = r5 | r7
            goto L_0x0254
        L_0x0213:
            r5 = -1075(0xfffffffffffffbcd, double:NaN)
            int r5 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r5 >= 0) goto L_0x0221
            if (r2 == 0) goto L_0x021e
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_NEGATIVE_ZERO
            goto L_0x0220
        L_0x021e:
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_POSITIVE_ZERO
        L_0x0220:
            return r0
        L_0x0221:
            if (r3 != 0) goto L_0x0228
            if (r1 == 0) goto L_0x0226
            goto L_0x0228
        L_0x0226:
            r1 = 0
            goto L_0x0229
        L_0x0228:
            r1 = r0
        L_0x0229:
            int r3 = (int) r12
            int r3 = r3 + 1074
            int r3 = r3 + r0
            int r3 = 53 - r3
            int r5 = r3 + -1
            long r6 = r14 << r5
            long r6 = r6 & r10
            int r6 = (r6 > r24 ? 1 : (r6 == r24 ? 0 : -1))
            if (r6 == 0) goto L_0x023a
            r6 = r0
            goto L_0x023b
        L_0x023a:
            r6 = 0
        L_0x023b:
            if (r3 <= r0) goto L_0x024c
            r12 = -1
            long r12 = r12 << r5
            long r12 = ~r12
            if (r1 != 0) goto L_0x024b
            long r12 = r12 & r10
            int r1 = (r12 > r24 ? 1 : (r12 == r24 ? 0 : -1))
            if (r1 == 0) goto L_0x0249
            goto L_0x024b
        L_0x0249:
            r1 = 0
            goto L_0x024c
        L_0x024b:
            r1 = r0
        L_0x024c:
            long r10 = r10 >> r3
            long r7 = r10 & r8
            long r7 = r7 | r24
            r3 = r1
            r1 = r6
            r5 = r7
        L_0x0254:
            long r7 = r5 & r14
            int r7 = (r7 > r24 ? 1 : (r7 == r24 ? 0 : -1))
            if (r7 != 0) goto L_0x025b
            goto L_0x025c
        L_0x025b:
            r0 = 0
        L_0x025c:
            if (r0 == 0) goto L_0x0262
            if (r1 == 0) goto L_0x0262
            if (r3 != 0) goto L_0x0266
        L_0x0262:
            if (r0 != 0) goto L_0x0267
            if (r1 == 0) goto L_0x0267
        L_0x0266:
            long r5 = r5 + r14
        L_0x0267:
            if (r2 == 0) goto L_0x0271
            r0 = -9223372036854775808
            long r0 = r0 | r5
            double r0 = java.lang.Double.longBitsToDouble(r0)
            goto L_0x0275
        L_0x0271:
            double r0 = java.lang.Double.longBitsToDouble(r5)
        L_0x0275:
            jdk.internal.math.FloatingDecimal$PreparedASCIIToBinaryBuffer r2 = new jdk.internal.math.FloatingDecimal$PreparedASCIIToBinaryBuffer
            r2.<init>(r0, r4)
            return r2
        L_0x027b:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            java.lang.String r1 = "Result from digit conversion too large!"
            r0.<init>((java.lang.Object) r1)
            throw r0
        L_0x0283:
            if (r2 == 0) goto L_0x028d
            if (r4 == 0) goto L_0x028a
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_NEGATIVE_INFINITY
            goto L_0x0294
        L_0x028a:
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_NEGATIVE_ZERO
            goto L_0x0294
        L_0x028d:
            if (r4 == 0) goto L_0x0292
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_POSITIVE_INFINITY
            goto L_0x0294
        L_0x0292:
            jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter r0 = A2BC_POSITIVE_ZERO
        L_0x0294:
            return r0
        L_0x0295:
            java.lang.NumberFormatException r1 = new java.lang.NumberFormatException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "For input string: \""
            r2.<init>((java.lang.String) r3)
            r2.append((java.lang.String) r0)
            java.lang.String r0 = "\""
            r2.append((java.lang.String) r0)
            java.lang.String r0 = r2.toString()
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.math.FloatingDecimal.parseHexString(java.lang.String):jdk.internal.math.FloatingDecimal$ASCIIToBinaryConverter");
    }

    static String stripLeadingZeros(String str) {
        if (str.isEmpty() || str.charAt(0) != '0') {
            return str;
        }
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) != '0') {
                return str.substring(i);
            }
        }
        return "";
    }

    static int getHexDigit(String str, int i) {
        int digit = Character.digit(str.charAt(i), 16);
        if (digit > -1 && digit < 16) {
            return digit;
        }
        throw new AssertionError((Object) "Unexpected failure of digit conversion of " + str.charAt(i));
    }
}
