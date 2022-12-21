package java.math;

import android.icu.lang.UCharacter;
import android.net.wifi.WifiManager;
import com.airbnb.lottie.utils.Utils;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.p026io.StreamCorruptedException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import jdk.internal.math.DoubleConsts;
import jdk.internal.math.FloatConsts;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import libcore.math.NativeBN;
import sun.misc.Unsafe;

public class BigInteger extends Number implements Comparable<BigInteger> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int BORINGSSL_DIV_OFFSET = 20;
    private static final int BORINGSSL_DIV_THRESHOLD = 40;
    static final int BURNIKEL_ZIEGLER_OFFSET = 40;
    static final int BURNIKEL_ZIEGLER_THRESHOLD = 80;
    private static final int DEFAULT_PRIME_CERTAINTY = 100;
    private static final int KARATSUBA_SQUARE_THRESHOLD = 128;
    private static final int KARATSUBA_THRESHOLD = 80;
    private static final double LOG_TWO = Math.log(2.0d);
    static final long LONG_MASK = 4294967295L;
    private static final int MAX_CONSTANT = 16;
    private static final int MAX_MAG_LENGTH = 67108864;
    private static final int MONTGOMERY_INTRINSIC_THRESHOLD = 512;
    private static final int MULTIPLY_SQUARE_THRESHOLD = 20;
    private static final BigInteger NEGATIVE_ONE = valueOf(-1);
    public static final BigInteger ONE = valueOf(1);
    private static final int PRIME_SEARCH_BIT_LENGTH_LIMIT = 500000000;
    private static final int SCHOENHAGE_BASE_CONVERSION_THRESHOLD = 20;
    private static final BigInteger SMALL_PRIME_PRODUCT = valueOf(152125131763605L);
    private static final int SMALL_PRIME_THRESHOLD = 95;
    public static final BigInteger TEN = valueOf(10);
    private static final int TOOM_COOK_SQUARE_THRESHOLD = 216;
    private static final int TOOM_COOK_THRESHOLD = 240;
    public static final BigInteger TWO = valueOf(2);
    public static final BigInteger ZERO = new BigInteger(new int[0], 0);
    private static long[] bitsPerDigit = {0, 0, 1024, 1624, 2048, 2378, 2648, 2875, 3072, 3247, 3402, 3543, 3672, 3790, 3899, 4001, 4096, 4186, 4271, 4350, 4426, 4498, 4567, 4633, 4696, 4756, 4814, 4870, 4923, 4975, 5025, 5074, 5120, 5166, 5210, 5253, 5295};
    static int[] bnExpModThreshTable = {7, 25, 81, UCharacter.UnicodeBlock.OLD_PERMIC_ID, 673, 1793, Integer.MAX_VALUE};
    private static int[] digitsPerInt = {0, 0, 30, 19, 15, 13, 11, 11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5};
    private static int[] digitsPerLong = {0, 0, 62, 39, 31, 27, 24, 22, 20, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12};
    private static int[] intRadix = {0, 0, 1073741824, 1162261467, 1073741824, 1220703125, 362797056, 1977326743, 1073741824, 387420489, Utils.SECOND_IN_NANOS, 214358881, 429981696, 815730721, 1475789056, 170859375, 268435456, 410338673, 612220032, 893871739, 1280000000, 1801088541, 113379904, 148035889, 191102976, 244140625, 308915776, 387420489, 481890304, 594823321, 729000000, 887503681, 1073741824, 1291467969, 1544804416, 1838265625, 60466176};
    private static final double[] logCache = new double[37];
    private static BigInteger[] longRadix = {null, null, valueOf(4611686018427387904L), valueOf(4052555153018976267L), valueOf(4611686018427387904L), valueOf(7450580596923828125L), valueOf(4738381338321616896L), valueOf(3909821048582988049L), valueOf((long) LockFreeTaskQueueCore.FROZEN_MASK), valueOf(1350851717672992089L), valueOf(1000000000000000000L), valueOf(5559917313492231481L), valueOf(2218611106740436992L), valueOf(8650415919381337933L), valueOf(2177953337809371136L), valueOf(6568408355712890625L), valueOf((long) LockFreeTaskQueueCore.FROZEN_MASK), valueOf(2862423051509815793L), valueOf(6746640616477458432L), valueOf(799006685782884121L), valueOf(1638400000000000000L), valueOf(3243919932521508681L), valueOf(6221821273427820544L), valueOf(504036361936467383L), valueOf(876488338465357824L), valueOf(1490116119384765625L), valueOf(2481152873203736576L), valueOf(4052555153018976267L), valueOf(6502111422497947648L), valueOf(353814783205469041L), valueOf(531441000000000000L), valueOf(787662783788549761L), valueOf((long) LockFreeTaskQueueCore.FROZEN_MASK), valueOf(1667889514952984961L), valueOf(2386420683693101056L), valueOf(3379220508056640625L), valueOf(4738381338321616896L)};
    private static BigInteger[] negConst = new BigInteger[17];
    private static BigInteger[] posConst = new BigInteger[17];
    private static volatile BigInteger[][] powerCache = new BigInteger[37][];
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("signum", Integer.TYPE), new ObjectStreamField("magnitude", byte[].class), new ObjectStreamField("bitCount", Integer.TYPE), new ObjectStreamField("bitLength", Integer.TYPE), new ObjectStreamField("firstNonzeroByteNum", Integer.TYPE), new ObjectStreamField("lowestSetBit", Integer.TYPE)};
    private static final long serialVersionUID = -8287574255936472291L;
    private static String[] zeros;
    private int bitCountPlusOne;
    private int bitLengthPlusOne;
    private int firstNonzeroIntNumPlusTwo;
    private int lowestSetBitPlusTwo;
    final int[] mag;
    final int signum;

    public BigInteger(byte[] bArr, int i, int i2) {
        if (bArr.length != 0) {
            Objects.checkFromIndexSize(i, i2, bArr.length);
            if (bArr[i] < 0) {
                this.mag = makePositive(bArr, i, i2);
                this.signum = -1;
            } else {
                int[] stripLeadingZeroBytes = stripLeadingZeroBytes(bArr, i, i2);
                this.mag = stripLeadingZeroBytes;
                this.signum = stripLeadingZeroBytes.length == 0 ? 0 : 1;
            }
            if (this.mag.length >= MAX_MAG_LENGTH) {
                checkRange();
                return;
            }
            return;
        }
        throw new NumberFormatException("Zero length BigInteger");
    }

    public BigInteger(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    private BigInteger(int[] iArr) {
        if (iArr.length != 0) {
            int i = 0;
            if (iArr[0] < 0) {
                this.mag = makePositive(iArr);
                this.signum = -1;
            } else {
                int[] trustedStripLeadingZeroInts = trustedStripLeadingZeroInts(iArr);
                this.mag = trustedStripLeadingZeroInts;
                this.signum = trustedStripLeadingZeroInts.length != 0 ? 1 : i;
            }
            if (this.mag.length >= MAX_MAG_LENGTH) {
                checkRange();
                return;
            }
            return;
        }
        throw new NumberFormatException("Zero length BigInteger");
    }

    public BigInteger(int i, byte[] bArr, int i2, int i3) {
        if (i < -1 || i > 1) {
            throw new NumberFormatException("Invalid signum value");
        }
        Objects.checkFromIndexSize(i2, i3, bArr.length);
        int[] stripLeadingZeroBytes = stripLeadingZeroBytes(bArr, i2, i3);
        this.mag = stripLeadingZeroBytes;
        if (stripLeadingZeroBytes.length == 0) {
            this.signum = 0;
        } else if (i != 0) {
            this.signum = i;
        } else {
            throw new NumberFormatException("signum-magnitude mismatch");
        }
        if (stripLeadingZeroBytes.length >= MAX_MAG_LENGTH) {
            checkRange();
        }
    }

    public BigInteger(int i, byte[] bArr) {
        this(i, bArr, 0, bArr.length);
    }

    private BigInteger(int i, int[] iArr) {
        int[] stripLeadingZeroInts = stripLeadingZeroInts(iArr);
        this.mag = stripLeadingZeroInts;
        if (i < -1 || i > 1) {
            throw new NumberFormatException("Invalid signum value");
        }
        if (stripLeadingZeroInts.length == 0) {
            this.signum = 0;
        } else if (i != 0) {
            this.signum = i;
        } else {
            throw new NumberFormatException("signum-magnitude mismatch");
        }
        if (stripLeadingZeroInts.length >= MAX_MAG_LENGTH) {
            checkRange();
        }
    }

    public BigInteger(String str, int i) {
        int i2;
        int i3;
        int length = str.length();
        if (i < 2 || i > 36) {
            throw new NumberFormatException("Radix out of range");
        } else if (length != 0) {
            int lastIndexOf = str.lastIndexOf(45);
            int lastIndexOf2 = str.lastIndexOf(43);
            if (lastIndexOf >= 0) {
                if (lastIndexOf != 0 || lastIndexOf2 >= 0) {
                    throw new NumberFormatException("Illegal embedded sign character");
                }
                i3 = -1;
                i2 = 1;
            } else if (lastIndexOf2 < 0) {
                i3 = 1;
                i2 = 0;
            } else if (lastIndexOf2 == 0) {
                i3 = 1;
                i2 = 1;
            } else {
                throw new NumberFormatException("Illegal embedded sign character");
            }
            if (i2 != length) {
                while (i2 < length && Character.digit(str.charAt(i2), i) == 0) {
                    i2++;
                }
                if (i2 == length) {
                    this.signum = 0;
                    this.mag = ZERO.mag;
                    return;
                }
                int i4 = length - i2;
                this.signum = i3;
                long j = ((((long) i4) * bitsPerDigit[i]) >>> 10) + 1 + 31;
                if (j >= WifiManager.WIFI_FEATURE_P2P_RAND_MAC) {
                    reportOverflow();
                }
                int i5 = ((int) j) >>> 5;
                int[] iArr = new int[i5];
                int i6 = digitsPerInt[i];
                int i7 = i4 % i6;
                int i8 = (i7 != 0 ? i7 : i6) + i2;
                int parseInt = Integer.parseInt(str.substring(i2, i8), i);
                iArr[i5 - 1] = parseInt;
                if (parseInt >= 0) {
                    int i9 = intRadix[i];
                    while (i8 < length) {
                        int i10 = digitsPerInt[i] + i8;
                        int parseInt2 = Integer.parseInt(str.substring(i8, i10), i);
                        if (parseInt2 >= 0) {
                            destructiveMulAdd(iArr, i9, parseInt2);
                            i8 = i10;
                        } else {
                            throw new NumberFormatException("Illegal digit");
                        }
                    }
                    int[] trustedStripLeadingZeroInts = trustedStripLeadingZeroInts(iArr);
                    this.mag = trustedStripLeadingZeroInts;
                    if (trustedStripLeadingZeroInts.length >= MAX_MAG_LENGTH) {
                        checkRange();
                        return;
                    }
                    return;
                }
                throw new NumberFormatException("Illegal digit");
            }
            throw new NumberFormatException("Zero length BigInteger");
        } else {
            throw new NumberFormatException("Zero length BigInteger");
        }
    }

    BigInteger(char[] cArr, int i, int i2) {
        int i3;
        int i4 = 0;
        while (i4 < i2 && Character.digit(cArr[i4], 10) == 0) {
            i4++;
        }
        if (i4 == i2) {
            this.signum = 0;
            this.mag = ZERO.mag;
            return;
        }
        int i5 = i2 - i4;
        this.signum = i;
        if (i2 < 10) {
            i3 = 1;
        } else {
            long j = ((((long) i5) * bitsPerDigit[10]) >>> 10) + 1 + 31;
            if (j >= WifiManager.WIFI_FEATURE_P2P_RAND_MAC) {
                reportOverflow();
            }
            i3 = ((int) j) >>> 5;
        }
        int[] iArr = new int[i3];
        int i6 = digitsPerInt[10];
        int i7 = i5 % i6;
        int i8 = (i7 != 0 ? i7 : i6) + i4;
        iArr[i3 - 1] = parseInt(cArr, i4, i8);
        while (i8 < i2) {
            int i9 = digitsPerInt[10] + i8;
            destructiveMulAdd(iArr, intRadix[10], parseInt(cArr, i8, i9));
            i8 = i9;
        }
        int[] trustedStripLeadingZeroInts = trustedStripLeadingZeroInts(iArr);
        this.mag = trustedStripLeadingZeroInts;
        if (trustedStripLeadingZeroInts.length >= MAX_MAG_LENGTH) {
            checkRange();
        }
    }

    private int parseInt(char[] cArr, int i, int i2) {
        int i3 = i + 1;
        int digit = Character.digit(cArr[i], 10);
        if (digit != -1) {
            while (i3 < i2) {
                int digit2 = Character.digit(cArr[i3], 10);
                if (digit2 != -1) {
                    digit = (digit * 10) + digit2;
                    i3++;
                } else {
                    throw new NumberFormatException(new String(cArr));
                }
            }
            return digit;
        }
        throw new NumberFormatException(new String(cArr));
    }

    static {
        for (int i = 1; i <= 16; i++) {
            int[] iArr = {i};
            posConst[i] = new BigInteger(iArr, 1);
            negConst[i] = new BigInteger(iArr, -1);
        }
        for (int i2 = 2; i2 <= 36; i2++) {
            powerCache[i2] = new BigInteger[]{valueOf((long) i2)};
            logCache[i2] = Math.log((double) i2);
        }
        String[] strArr = new String[64];
        zeros = strArr;
        strArr[63] = "000000000000000000000000000000000000000000000000000000000000000";
        for (int i3 = 0; i3 < 63; i3++) {
            String[] strArr2 = zeros;
            strArr2[i3] = strArr2[63].substring(0, i3);
        }
    }

    private static void destructiveMulAdd(int[] iArr, int i, int i2) {
        long j = ((long) i) & 4294967295L;
        long j2 = ((long) i2) & 4294967295L;
        int length = iArr.length;
        int i3 = length - 1;
        long j3 = 0;
        for (int i4 = i3; i4 >= 0; i4--) {
            long j4 = ((((long) iArr[i4]) & 4294967295L) * j) + j3;
            iArr[i4] = (int) j4;
            j3 = j4 >>> 32;
        }
        long j5 = (((long) iArr[i3]) & 4294967295L) + j2;
        iArr[i3] = (int) j5;
        long j6 = j5 >>> 32;
        for (int i5 = length - 2; i5 >= 0; i5--) {
            long j7 = (((long) iArr[i5]) & 4294967295L) + j6;
            iArr[i5] = (int) j7;
            j6 = j7 >>> 32;
        }
    }

    public BigInteger(String str) {
        this(str, 10);
    }

    public BigInteger(int i, Random random) {
        this(1, randomBits(i, random));
    }

    private static byte[] randomBits(int i, Random random) {
        if (i >= 0) {
            int i2 = (int) ((((long) i) + 7) / 8);
            byte[] bArr = new byte[i2];
            if (i2 > 0) {
                random.nextBytes(bArr);
                bArr[0] = (byte) (bArr[0] & ((1 << (8 - ((i2 * 8) - i))) - 1));
            }
            return bArr;
        }
        throw new IllegalArgumentException("numBits must be non-negative");
    }

    public BigInteger(int i, int i2, Random random) {
        BigInteger bigInteger;
        if (i >= 2) {
            if (i < 95) {
                bigInteger = smallPrime(i, i2, random);
            } else {
                bigInteger = largePrime(i, i2, random);
            }
            this.signum = 1;
            this.mag = bigInteger.mag;
            return;
        }
        throw new ArithmeticException("bitLength < 2");
    }

    public static BigInteger probablePrime(int i, Random random) {
        if (i < 2) {
            throw new ArithmeticException("bitLength < 2");
        } else if (i < 95) {
            return smallPrime(i, 100, random);
        } else {
            return largePrime(i, 100, random);
        }
    }

    private static BigInteger smallPrime(int i, int i2, Random random) {
        int i3 = i + 31;
        int i4 = i3 >>> 5;
        int[] iArr = new int[i4];
        int i5 = 1 << (i3 & 31);
        int i6 = (i5 << 1) - 1;
        while (true) {
            for (int i7 = 0; i7 < i4; i7++) {
                iArr[i7] = random.nextInt();
            }
            iArr[0] = (iArr[0] & i6) | i5;
            if (i > 2) {
                int i8 = i4 - 1;
                iArr[i8] = iArr[i8] | 1;
            }
            BigInteger bigInteger = new BigInteger(iArr, 1);
            if (i > 6) {
                long longValue = bigInteger.remainder(SMALL_PRIME_PRODUCT).longValue();
                if (longValue % 3 == 0) {
                    continue;
                } else if (longValue % 5 == 0) {
                    continue;
                } else if (longValue % 7 == 0) {
                    continue;
                } else if (longValue % 11 == 0) {
                    continue;
                } else if (longValue % 13 == 0) {
                    continue;
                } else if (longValue % 17 == 0) {
                    continue;
                } else if (longValue % 19 == 0) {
                    continue;
                } else if (longValue % 23 == 0) {
                    continue;
                } else if (longValue % 29 == 0) {
                    continue;
                } else if (longValue % 31 == 0) {
                    continue;
                } else if (longValue % 37 == 0) {
                    continue;
                } else if (longValue % 41 == 0) {
                    continue;
                }
            }
            if (i < 4 || bigInteger.primeToCertainty(i2, random)) {
                return bigInteger;
            }
        }
    }

    private static BigInteger largePrime(int i, int i2, Random random) {
        int i3 = i - 1;
        BigInteger bit = new BigInteger(i, random).setBit(i3);
        int[] iArr = bit.mag;
        int length = iArr.length - 1;
        iArr[length] = iArr[length] & -2;
        int primeSearchLen = getPrimeSearchLen(i);
        BigInteger retrieve = new BitSieve(bit, primeSearchLen).retrieve(bit, i2, random);
        while (true) {
            if (retrieve != null && retrieve.bitLength() == i) {
                return retrieve;
            }
            bit = bit.add(valueOf((long) (primeSearchLen * 2)));
            if (bit.bitLength() != i) {
                bit = new BigInteger(i, random).setBit(i3);
            }
            int[] iArr2 = bit.mag;
            int length2 = iArr2.length - 1;
            iArr2[length2] = iArr2[length2] & -2;
            retrieve = new BitSieve(bit, primeSearchLen).retrieve(bit, i2, random);
        }
    }

    public BigInteger nextProbablePrime() {
        BigInteger bigInteger;
        int i = this.signum;
        if (i >= 0) {
            if (i != 0) {
                BigInteger bigInteger2 = ONE;
                if (!equals(bigInteger2)) {
                    BigInteger add = add(bigInteger2);
                    if (add.bitLength() < 95) {
                        if (!add.testBit(0)) {
                            add = add.add(bigInteger2);
                        }
                        while (true) {
                            if (bigInteger.bitLength() > 6) {
                                long longValue = bigInteger.remainder(SMALL_PRIME_PRODUCT).longValue();
                                if (longValue % 3 == 0 || longValue % 5 == 0 || longValue % 7 == 0 || longValue % 11 == 0 || longValue % 13 == 0 || longValue % 17 == 0 || longValue % 19 == 0 || longValue % 23 == 0 || longValue % 29 == 0 || longValue % 31 == 0 || longValue % 37 == 0 || longValue % 41 == 0) {
                                    bigInteger = bigInteger.add(TWO);
                                }
                            }
                            if (bigInteger.bitLength() < 4 || bigInteger.primeToCertainty(100, (Random) null)) {
                                return bigInteger;
                            }
                            bigInteger = bigInteger.add(TWO);
                        }
                    } else {
                        if (add.testBit(0)) {
                            add = add.subtract(bigInteger2);
                        }
                        int primeSearchLen = getPrimeSearchLen(add.bitLength());
                        while (true) {
                            BigInteger retrieve = new BitSieve(add, primeSearchLen).retrieve(add, 100, (Random) null);
                            if (retrieve != null) {
                                return retrieve;
                            }
                            add = add.add(valueOf((long) (primeSearchLen * 2)));
                        }
                    }
                }
            }
            return TWO;
        }
        throw new ArithmeticException("start < 0: " + this);
    }

    private static int getPrimeSearchLen(int i) {
        if (i <= 500000001) {
            return (i / 20) * 64;
        }
        throw new ArithmeticException("Prime search implementation restriction on bitLength");
    }

    /* access modifiers changed from: package-private */
    public boolean primeToCertainty(int i, Random random) {
        int i2 = 2;
        int min = (Math.min(i, 2147483646) + 1) / 2;
        int bitLength = bitLength();
        if (bitLength < 100) {
            if (min >= 50) {
                min = 50;
            }
            return passesMillerRabin(min, random);
        }
        if (bitLength < 256) {
            i2 = 27;
        } else if (bitLength < 512) {
            i2 = 15;
        } else if (bitLength < 768) {
            i2 = 8;
        } else if (bitLength < 1024) {
            i2 = 4;
        }
        if (min >= i2) {
            min = i2;
        }
        if (!passesMillerRabin(min, random) || !passesLucasLehmer()) {
            return false;
        }
        return true;
    }

    private boolean passesLucasLehmer() {
        BigInteger add = add(ONE);
        int i = 5;
        while (jacobiSymbol(i, this) != -1) {
            i = i < 0 ? Math.abs(i) + 2 : -(i + 2);
        }
        return lucasLehmerSequence(i, add, this).mod(this).equals(ZERO);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x001d A[LOOP:0: B:11:0x0019->B:13:0x001d, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0030 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0031  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int jacobiSymbol(int r7, java.math.BigInteger r8) {
        /*
            r0 = 0
            if (r7 != 0) goto L_0x0004
            return r0
        L_0x0004:
            int[] r1 = r8.mag
            int r2 = r1.length
            r3 = 1
            int r2 = r2 - r3
            r1 = r1[r2]
            if (r7 >= 0) goto L_0x0018
            int r7 = -r7
            r2 = r1 & 7
            r4 = 3
            if (r2 == r4) goto L_0x0016
            r4 = 7
            if (r2 != r4) goto L_0x0018
        L_0x0016:
            r2 = -1
            goto L_0x0019
        L_0x0018:
            r2 = r3
        L_0x0019:
            r4 = r7 & 3
            if (r4 != 0) goto L_0x0020
            int r7 = r7 >> 2
            goto L_0x0019
        L_0x0020:
            r4 = r7 & 1
            if (r4 != 0) goto L_0x002e
            int r7 = r7 >> 1
            int r4 = r1 >> 1
            r4 = r4 ^ r1
            r4 = r4 & 2
            if (r4 == 0) goto L_0x002e
            int r2 = -r2
        L_0x002e:
            if (r7 != r3) goto L_0x0031
            return r2
        L_0x0031:
            r1 = r1 & r7
            r1 = r1 & 2
            if (r1 == 0) goto L_0x0037
            int r2 = -r2
        L_0x0037:
            long r4 = (long) r7
            java.math.BigInteger r1 = valueOf((long) r4)
            java.math.BigInteger r8 = r8.mod(r1)
            int r8 = r8.intValue()
        L_0x0044:
            if (r8 == 0) goto L_0x006b
        L_0x0046:
            r1 = r8 & 3
            if (r1 != 0) goto L_0x004d
            int r8 = r8 >> 2
            goto L_0x0046
        L_0x004d:
            r1 = r8 & 1
            if (r1 != 0) goto L_0x005b
            int r8 = r8 >> 1
            int r1 = r7 >> 1
            r1 = r1 ^ r7
            r1 = r1 & 2
            if (r1 == 0) goto L_0x005b
            int r2 = -r2
        L_0x005b:
            if (r8 != r3) goto L_0x005e
            return r2
        L_0x005e:
            r1 = r7 & r8
            r1 = r1 & 2
            if (r1 == 0) goto L_0x0066
            int r1 = -r2
            r2 = r1
        L_0x0066:
            int r7 = r7 % r8
            r6 = r8
            r8 = r7
            r7 = r6
            goto L_0x0044
        L_0x006b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigInteger.jacobiSymbol(int, java.math.BigInteger):int");
    }

    private static BigInteger lucasLehmerSequence(int i, BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger valueOf = valueOf((long) i);
        BigInteger bigInteger3 = ONE;
        BigInteger bigInteger4 = bigInteger3;
        for (int bitLength = bigInteger.bitLength() - 2; bitLength >= 0; bitLength--) {
            BigInteger mod = bigInteger3.multiply(bigInteger4).mod(bigInteger2);
            BigInteger mod2 = bigInteger4.square().add(valueOf.multiply(bigInteger3.square())).mod(bigInteger2);
            if (mod2.testBit(0)) {
                mod2 = mod2.subtract(bigInteger2);
            }
            BigInteger shiftRight = mod2.shiftRight(1);
            if (bigInteger.testBit(bitLength)) {
                BigInteger mod3 = mod.add(shiftRight).mod(bigInteger2);
                if (mod3.testBit(0)) {
                    mod3 = mod3.subtract(bigInteger2);
                }
                BigInteger shiftRight2 = mod3.shiftRight(1);
                BigInteger mod4 = shiftRight.add(valueOf.multiply(mod)).mod(bigInteger2);
                if (mod4.testBit(0)) {
                    mod4 = mod4.subtract(bigInteger2);
                }
                bigInteger4 = mod4.shiftRight(1);
                bigInteger3 = shiftRight2;
            } else {
                bigInteger4 = shiftRight;
                bigInteger3 = mod;
            }
        }
        return bigInteger3;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005a, code lost:
        r4 = r4 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean passesMillerRabin(int r9, java.util.Random r10) {
        /*
            r8 = this;
            java.math.BigInteger r0 = ONE
            java.math.BigInteger r0 = r8.subtract(r0)
            int r1 = r0.getLowestSetBit()
            java.math.BigInteger r2 = r0.shiftRight(r1)
            if (r10 != 0) goto L_0x0014
            java.util.concurrent.ThreadLocalRandom r10 = java.util.concurrent.ThreadLocalRandom.current()
        L_0x0014:
            r3 = 0
            r4 = r3
        L_0x0016:
            if (r4 >= r9) goto L_0x005d
        L_0x0018:
            java.math.BigInteger r5 = new java.math.BigInteger
            int r6 = r8.bitLength()
            r5.<init>((int) r6, (java.util.Random) r10)
            java.math.BigInteger r6 = ONE
            int r6 = r5.compareTo((java.math.BigInteger) r6)
            if (r6 <= 0) goto L_0x0018
            int r6 = r5.compareTo((java.math.BigInteger) r8)
            if (r6 >= 0) goto L_0x0018
            java.math.BigInteger r5 = r5.modPow(r2, r8)
            r6 = r3
        L_0x0034:
            if (r6 != 0) goto L_0x003e
            java.math.BigInteger r7 = ONE
            boolean r7 = r5.equals(r7)
            if (r7 != 0) goto L_0x005a
        L_0x003e:
            boolean r7 = r5.equals(r0)
            if (r7 != 0) goto L_0x005a
            if (r6 <= 0) goto L_0x004e
            java.math.BigInteger r7 = ONE
            boolean r7 = r5.equals(r7)
            if (r7 != 0) goto L_0x0052
        L_0x004e:
            int r6 = r6 + 1
            if (r6 != r1) goto L_0x0053
        L_0x0052:
            return r3
        L_0x0053:
            java.math.BigInteger r7 = TWO
            java.math.BigInteger r5 = r5.modPow(r7, r8)
            goto L_0x0034
        L_0x005a:
            int r4 = r4 + 1
            goto L_0x0016
        L_0x005d:
            r8 = 1
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigInteger.passesMillerRabin(int, java.util.Random):boolean");
    }

    BigInteger(int[] iArr, int i) {
        this.signum = iArr.length == 0 ? 0 : i;
        this.mag = iArr;
        if (iArr.length >= MAX_MAG_LENGTH) {
            checkRange();
        }
    }

    private BigInteger(byte[] bArr, int i) {
        this.signum = bArr.length == 0 ? 0 : i;
        int[] stripLeadingZeroBytes = stripLeadingZeroBytes(bArr, 0, bArr.length);
        this.mag = stripLeadingZeroBytes;
        if (stripLeadingZeroBytes.length >= MAX_MAG_LENGTH) {
            checkRange();
        }
    }

    private void checkRange() {
        int[] iArr = this.mag;
        if (iArr.length > MAX_MAG_LENGTH || (iArr.length == MAX_MAG_LENGTH && iArr[0] < 0)) {
            reportOverflow();
        }
    }

    private static void reportOverflow() {
        throw new ArithmeticException("BigInteger would overflow supported range");
    }

    public static BigInteger valueOf(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i == 0) {
            return ZERO;
        }
        if (i > 0 && j <= 16) {
            return posConst[(int) j];
        }
        if (i >= 0 || j < -16) {
            return new BigInteger(j);
        }
        return negConst[(int) (-j)];
    }

    private BigInteger(long j) {
        if (j < 0) {
            j = -j;
            this.signum = -1;
        } else {
            this.signum = 1;
        }
        int i = (int) (j >>> 32);
        if (i == 0) {
            int[] iArr = new int[1];
            this.mag = iArr;
            iArr[0] = (int) j;
            return;
        }
        int[] iArr2 = new int[2];
        this.mag = iArr2;
        iArr2[0] = i;
        iArr2[1] = (int) j;
    }

    private static BigInteger valueOf(int[] iArr) {
        return iArr[0] > 0 ? new BigInteger(iArr, 1) : new BigInteger(iArr);
    }

    public BigInteger add(BigInteger bigInteger) {
        int[] iArr;
        int i = bigInteger.signum;
        if (i == 0) {
            return this;
        }
        int i2 = this.signum;
        if (i2 == 0) {
            return bigInteger;
        }
        if (i == i2) {
            return new BigInteger(add(this.mag, bigInteger.mag), this.signum);
        }
        int compareMagnitude = compareMagnitude(bigInteger);
        if (compareMagnitude == 0) {
            return ZERO;
        }
        if (compareMagnitude > 0) {
            iArr = subtract(this.mag, bigInteger.mag);
        } else {
            iArr = subtract(bigInteger.mag, this.mag);
        }
        return new BigInteger(trustedStripLeadingZeroInts(iArr), compareMagnitude == this.signum ? 1 : -1);
    }

    /* access modifiers changed from: package-private */
    public BigInteger add(long j) {
        if (j == 0) {
            return this;
        }
        if (this.signum == 0) {
            return valueOf(j);
        }
        if (Long.signum(j) == this.signum) {
            return new BigInteger(add(this.mag, Math.abs(j)), this.signum);
        }
        int compareMagnitude = compareMagnitude(j);
        if (compareMagnitude == 0) {
            return ZERO;
        }
        return new BigInteger(trustedStripLeadingZeroInts(compareMagnitude > 0 ? subtract(this.mag, Math.abs(j)) : subtract(Math.abs(j), this.mag)), compareMagnitude == this.signum ? 1 : -1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0056, code lost:
        r13 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0057, code lost:
        if (r0 <= 0) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0059, code lost:
        if (r13 == false) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005b, code lost:
        r0 = r0 - 1;
        r13 = r12[r0] + 1;
        r2[r0] = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0062, code lost:
        if (r13 != 0) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0065, code lost:
        if (r0 <= 0) goto L_0x006e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0067, code lost:
        r0 = r0 - 1;
        r2[r0] = r12[r0];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x006e, code lost:
        if (r13 == false) goto L_0x007b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0070, code lost:
        r12 = new int[(r2.length + 1)];
        java.lang.System.arraycopy((java.lang.Object) r2, 0, (java.lang.Object) r12, 1, r2.length);
        r12[0] = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x007a, code lost:
        return r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x007b, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0052, code lost:
        if ((r4 >>> 32) != 0) goto L_0x0054;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0054, code lost:
        r13 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int[] add(int[] r12, long r13) {
        /*
            int r0 = r12.length
            r1 = 32
            long r2 = r13 >>> r1
            int r2 = (int) r2
            r3 = 0
            r4 = 4294967295(0xffffffff, double:2.1219957905E-314)
            r6 = 1
            if (r2 != 0) goto L_0x001c
            int[] r2 = new int[r0]
            int r0 = r0 + -1
            r7 = r12[r0]
            long r7 = (long) r7
            long r4 = r4 & r7
            long r4 = r4 + r13
            int r13 = (int) r4
            r2[r0] = r13
            goto L_0x004c
        L_0x001c:
            if (r0 != r6) goto L_0x002f
            r0 = 2
            int[] r0 = new int[r0]
            r12 = r12[r3]
            long r7 = (long) r12
            long r4 = r4 & r7
            long r13 = r13 + r4
            int r12 = (int) r13
            r0[r6] = r12
            long r12 = r13 >>> r1
            int r12 = (int) r12
            r0[r3] = r12
            return r0
        L_0x002f:
            int[] r7 = new int[r0]
            int r0 = r0 + -1
            r8 = r12[r0]
            long r8 = (long) r8
            long r8 = r8 & r4
            long r13 = r13 & r4
            long r8 = r8 + r13
            int r13 = (int) r8
            r7[r0] = r13
            int r0 = r0 + -1
            r13 = r12[r0]
            long r13 = (long) r13
            long r13 = r13 & r4
            long r10 = (long) r2
            long r4 = r4 & r10
            long r13 = r13 + r4
            long r4 = r8 >>> r1
            long r4 = r4 + r13
            int r13 = (int) r4
            r7[r0] = r13
            r2 = r7
        L_0x004c:
            long r13 = r4 >>> r1
            r4 = 0
            int r13 = (r13 > r4 ? 1 : (r13 == r4 ? 0 : -1))
            if (r13 == 0) goto L_0x0056
        L_0x0054:
            r13 = r6
            goto L_0x0057
        L_0x0056:
            r13 = r3
        L_0x0057:
            if (r0 <= 0) goto L_0x0065
            if (r13 == 0) goto L_0x0065
            int r0 = r0 + -1
            r13 = r12[r0]
            int r13 = r13 + r6
            r2[r0] = r13
            if (r13 != 0) goto L_0x0056
            goto L_0x0054
        L_0x0065:
            if (r0 <= 0) goto L_0x006e
            int r0 = r0 + -1
            r14 = r12[r0]
            r2[r0] = r14
            goto L_0x0065
        L_0x006e:
            if (r13 == 0) goto L_0x007b
            int r12 = r2.length
            int r12 = r12 + r6
            int[] r12 = new int[r12]
            int r13 = r2.length
            java.lang.System.arraycopy((java.lang.Object) r2, (int) r3, (java.lang.Object) r12, (int) r6, (int) r13)
            r12[r3] = r6
            return r12
        L_0x007b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigInteger.add(int[], long):int[]");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004f, code lost:
        if ((r12 >>> 32) != 0) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0051, code lost:
        r2 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0053, code lost:
        r2 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0054, code lost:
        if (r3 <= 0) goto L_0x0063;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0056, code lost:
        if (r2 == false) goto L_0x0063;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0058, code lost:
        r3 = r3 - 1;
        r2 = r0[r3] + 1;
        r4[r3] = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0060, code lost:
        if (r2 != 0) goto L_0x0053;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0063, code lost:
        if (r3 <= 0) goto L_0x006c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0065, code lost:
        r3 = r3 - 1;
        r4[r3] = r0[r3];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006c, code lost:
        if (r2 == false) goto L_0x007a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006e, code lost:
        r0 = new int[(r1 + 1)];
        java.lang.System.arraycopy((java.lang.Object) r4, 0, (java.lang.Object) r0, 1, r1);
        r0[0] = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0079, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007a, code lost:
        return r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int[] add(int[] r17, int[] r18) {
        /*
            r0 = r17
            int r1 = r0.length
            r2 = r18
            int r3 = r2.length
            if (r1 >= r3) goto L_0x000d
            r16 = r2
            r2 = r0
            r0 = r16
        L_0x000d:
            int r1 = r0.length
            int r3 = r2.length
            int[] r4 = new int[r1]
            r5 = 32
            r8 = 4294967295(0xffffffff, double:2.1219957905E-314)
            r10 = 0
            r11 = 1
            if (r3 != r11) goto L_0x002b
            int r3 = r1 + -1
            r12 = r0[r3]
            long r12 = (long) r12
            long r12 = r12 & r8
            r2 = r2[r10]
            long r14 = (long) r2
            long r8 = r8 & r14
            long r12 = r12 + r8
            int r2 = (int) r12
            r4[r3] = r2
            goto L_0x0049
        L_0x002b:
            r12 = r1
            r13 = 0
        L_0x002e:
            if (r3 <= 0) goto L_0x0047
            int r12 = r12 + -1
            r15 = r0[r12]
            long r10 = (long) r15
            long r10 = r10 & r8
            int r3 = r3 + -1
            r15 = r2[r3]
            long r6 = (long) r15
            long r6 = r6 & r8
            long r10 = r10 + r6
            long r6 = r13 >>> r5
            long r13 = r10 + r6
            int r6 = (int) r13
            r4[r12] = r6
            r10 = 0
            r11 = 1
            goto L_0x002e
        L_0x0047:
            r3 = r12
            r12 = r13
        L_0x0049:
            long r5 = r12 >>> r5
            r7 = 0
            int r2 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r2 == 0) goto L_0x0053
        L_0x0051:
            r2 = 1
            goto L_0x0054
        L_0x0053:
            r2 = 0
        L_0x0054:
            if (r3 <= 0) goto L_0x0063
            if (r2 == 0) goto L_0x0063
            int r3 = r3 + -1
            r2 = r0[r3]
            r5 = 1
            int r2 = r2 + r5
            r4[r3] = r2
            if (r2 != 0) goto L_0x0053
            goto L_0x0051
        L_0x0063:
            if (r3 <= 0) goto L_0x006c
            int r3 = r3 + -1
            r5 = r0[r3]
            r4[r3] = r5
            goto L_0x0063
        L_0x006c:
            if (r2 == 0) goto L_0x007a
            int r0 = r1 + 1
            int[] r0 = new int[r0]
            r2 = 0
            r3 = 1
            java.lang.System.arraycopy((java.lang.Object) r4, (int) r2, (java.lang.Object) r0, (int) r3, (int) r1)
            r0[r2] = r3
            return r0
        L_0x007a:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigInteger.add(int[], int[]):int[]");
    }

    private static int[] subtract(long j, int[] iArr) {
        int i = (int) (j >>> 32);
        if (i == 0) {
            return new int[]{(int) (j - (((long) iArr[0]) & 4294967295L))};
        }
        int[] iArr2 = new int[2];
        if (iArr.length == 1) {
            long j2 = (((long) ((int) j)) & 4294967295L) - (4294967295L & ((long) iArr[0]));
            iArr2[1] = (int) j2;
            if ((j2 >> 32) != 0) {
                iArr2[0] = i - 1;
            } else {
                iArr2[0] = i;
            }
            return iArr2;
        }
        long j3 = (((long) ((int) j)) & 4294967295L) - (((long) iArr[1]) & 4294967295L);
        iArr2[1] = (int) j3;
        iArr2[0] = (int) (((((long) i) & 4294967295L) - (((long) iArr[0]) & 4294967295L)) + (j3 >> 32));
        return iArr2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0043, code lost:
        r2 = r2 - 1;
        r12 = r11[r2] - 1;
        r3[r2] = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004a, code lost:
        if (r12 != -1) goto L_0x003e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004d, code lost:
        if (r2 <= 0) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x004f, code lost:
        r2 = r2 - 1;
        r3[r2] = r11[r2];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0056, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x003a, code lost:
        if ((r4 >> 32) != 0) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x003c, code lost:
        r12 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x003e, code lost:
        r12 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003f, code lost:
        if (r2 <= 0) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0041, code lost:
        if (r12 == false) goto L_0x004d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int[] subtract(int[] r11, long r12) {
        /*
            r0 = 32
            long r1 = r12 >>> r0
            int r1 = (int) r1
            int r2 = r11.length
            int[] r3 = new int[r2]
            r4 = 4294967295(0xffffffff, double:2.1219957905E-314)
            r6 = -1
            if (r1 != 0) goto L_0x001a
            int r2 = r2 + r6
            r1 = r11[r2]
            long r7 = (long) r1
            long r4 = r4 & r7
            long r4 = r4 - r12
            int r12 = (int) r4
            r3[r2] = r12
            goto L_0x0032
        L_0x001a:
            int r2 = r2 + r6
            r7 = r11[r2]
            long r7 = (long) r7
            long r7 = r7 & r4
            long r12 = r12 & r4
            long r7 = r7 - r12
            int r12 = (int) r7
            r3[r2] = r12
            int r2 = r2 + r6
            r12 = r11[r2]
            long r12 = (long) r12
            long r12 = r12 & r4
            long r9 = (long) r1
            long r4 = r4 & r9
            long r12 = r12 - r4
            long r4 = r7 >> r0
            long r4 = r4 + r12
            int r12 = (int) r4
            r3[r2] = r12
        L_0x0032:
            long r12 = r4 >> r0
            r0 = 0
            int r12 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            r13 = 0
            r0 = 1
            if (r12 == 0) goto L_0x003e
        L_0x003c:
            r12 = r0
            goto L_0x003f
        L_0x003e:
            r12 = r13
        L_0x003f:
            if (r2 <= 0) goto L_0x004d
            if (r12 == 0) goto L_0x004d
            int r2 = r2 + -1
            r12 = r11[r2]
            int r12 = r12 - r0
            r3[r2] = r12
            if (r12 != r6) goto L_0x003e
            goto L_0x003c
        L_0x004d:
            if (r2 <= 0) goto L_0x0056
            int r2 = r2 + -1
            r12 = r11[r2]
            r3[r2] = r12
            goto L_0x004d
        L_0x0056:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigInteger.subtract(int[], long):int[]");
    }

    public BigInteger subtract(BigInteger bigInteger) {
        int[] iArr;
        int i = bigInteger.signum;
        if (i == 0) {
            return this;
        }
        int i2 = this.signum;
        if (i2 == 0) {
            return bigInteger.negate();
        }
        if (i != i2) {
            return new BigInteger(add(this.mag, bigInteger.mag), this.signum);
        }
        int compareMagnitude = compareMagnitude(bigInteger);
        if (compareMagnitude == 0) {
            return ZERO;
        }
        if (compareMagnitude > 0) {
            iArr = subtract(this.mag, bigInteger.mag);
        } else {
            iArr = subtract(bigInteger.mag, this.mag);
        }
        return new BigInteger(trustedStripLeadingZeroInts(iArr), compareMagnitude == this.signum ? 1 : -1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0031, code lost:
        r0 = r0 - 1;
        r15 = r14[r0] - 1;
        r1[r0] = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0039, code lost:
        if (r15 != -1) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003c, code lost:
        if (r0 <= 0) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003e, code lost:
        r0 = r0 - 1;
        r1[r0] = r14[r0];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0045, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0028, code lost:
        if ((r5 >> 32) != 0) goto L_0x002a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x002a, code lost:
        r15 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x002c, code lost:
        r15 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002d, code lost:
        if (r0 <= 0) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002f, code lost:
        if (r15 == false) goto L_0x003c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int[] subtract(int[] r14, int[] r15) {
        /*
            int r0 = r14.length
            int[] r1 = new int[r0]
            int r2 = r15.length
            r3 = 0
            r5 = r3
        L_0x0007:
            r7 = 32
            if (r2 <= 0) goto L_0x0023
            int r0 = r0 + -1
            r8 = r14[r0]
            long r8 = (long) r8
            r10 = 4294967295(0xffffffff, double:2.1219957905E-314)
            long r8 = r8 & r10
            int r2 = r2 + -1
            r12 = r15[r2]
            long r12 = (long) r12
            long r10 = r10 & r12
            long r8 = r8 - r10
            long r5 = r5 >> r7
            long r5 = r5 + r8
            int r7 = (int) r5
            r1[r0] = r7
            goto L_0x0007
        L_0x0023:
            long r5 = r5 >> r7
            int r15 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            r2 = 0
            r3 = 1
            if (r15 == 0) goto L_0x002c
        L_0x002a:
            r15 = r3
            goto L_0x002d
        L_0x002c:
            r15 = r2
        L_0x002d:
            if (r0 <= 0) goto L_0x003c
            if (r15 == 0) goto L_0x003c
            int r0 = r0 + -1
            r15 = r14[r0]
            int r15 = r15 - r3
            r1[r0] = r15
            r4 = -1
            if (r15 != r4) goto L_0x002c
            goto L_0x002a
        L_0x003c:
            if (r0 <= 0) goto L_0x0045
            int r0 = r0 + -1
            r15 = r14[r0]
            r1[r0] = r15
            goto L_0x003c
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigInteger.subtract(int[], int[]):int[]");
    }

    public BigInteger multiply(BigInteger bigInteger) {
        return multiply(bigInteger, false);
    }

    private BigInteger multiply(BigInteger bigInteger, boolean z) {
        int i;
        long j;
        long j2;
        int i2 = bigInteger.signum;
        if (i2 == 0 || (i = this.signum) == 0) {
            return ZERO;
        }
        int[] iArr = this.mag;
        int length = iArr.length;
        if (bigInteger == this && length > 20 && length < 50) {
            return square();
        }
        int[] iArr2 = bigInteger.mag;
        int length2 = iArr2.length;
        int i3 = i == i2 ? 1 : -1;
        if (length >= 50 && length2 >= 50) {
            long j3 = 0;
            try {
                long bigEndInts2NewBN = bigEndInts2NewBN(iArr, false);
                try {
                    j2 = bigEndInts2NewBN(bigInteger.mag, false);
                    try {
                        j3 = NativeBN.BN_new();
                        NativeBN.BN_mul(j3, bigEndInts2NewBN, j2);
                        BigInteger bigInteger2 = new BigInteger(i3, bn2BigEndInts(j3));
                        NativeBN.BN_free(bigEndInts2NewBN);
                        NativeBN.BN_free(j2);
                        NativeBN.BN_free(j3);
                        return bigInteger2;
                    } catch (Throwable th) {
                        th = th;
                        j = j3;
                        j3 = bigEndInts2NewBN;
                        NativeBN.BN_free(j3);
                        NativeBN.BN_free(j2);
                        NativeBN.BN_free(j);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    j = 0;
                    j2 = 0;
                    j3 = bigEndInts2NewBN;
                    NativeBN.BN_free(j3);
                    NativeBN.BN_free(j2);
                    NativeBN.BN_free(j);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                j = 0;
                j2 = 0;
                NativeBN.BN_free(j3);
                NativeBN.BN_free(j2);
                NativeBN.BN_free(j);
                throw th;
            }
        } else if (iArr2.length == 1) {
            return multiplyByInt(iArr, iArr2[0], i3);
        } else {
            if (iArr.length == 1) {
                return multiplyByInt(iArr2, iArr[0], i3);
            }
            return new BigInteger(trustedStripLeadingZeroInts(multiplyToLen(iArr, length, iArr2, length2, (int[]) null)), i3);
        }
    }

    private static BigInteger multiplyByInt(int[] iArr, int i, int i2) {
        int[] iArr2 = iArr;
        int i3 = i2;
        if (Integer.bitCount(i) == 1) {
            return new BigInteger(shiftLeft(iArr2, Integer.numberOfTrailingZeros(i)), i3);
        }
        int length = iArr2.length;
        long j = ((long) i) & 4294967295L;
        int i4 = ((((((long) iArr2[0]) & 4294967295L) + 1) * j) >>> 32) == 0 ? length : length + 1;
        int[] iArr3 = new int[i4];
        int i5 = i4 - 1;
        int i6 = length - 1;
        int i7 = i4;
        long j2 = 0;
        while (i6 >= 0) {
            long j3 = ((((long) iArr2[i6]) & 4294967295L) * j) + j2;
            iArr3[i5] = (int) j3;
            j2 = j3 >>> 32;
            i6--;
            i5--;
        }
        if (i5 != -1) {
            if (j2 == 0) {
                iArr3 = Arrays.copyOfRange(iArr3, 1, i7);
            } else {
                iArr3[0] = (int) j2;
            }
        }
        return new BigInteger(iArr3, i3);
    }

    /* access modifiers changed from: package-private */
    public BigInteger multiply(long j) {
        int i;
        long j2;
        long j3 = j;
        long j4 = 0;
        int i2 = (j3 > 0 ? 1 : (j3 == 0 ? 0 : -1));
        if (i2 == 0 || (i = this.signum) == 0) {
            return ZERO;
        }
        if (j3 == Long.MIN_VALUE) {
            return multiply(valueOf(j));
        }
        if (i2 <= 0) {
            i = -i;
        }
        if (i2 < 0) {
            j3 = -j3;
        }
        long j5 = j3 >>> 32;
        long j6 = j3 & 4294967295L;
        int[] iArr = this.mag;
        int length = iArr.length;
        int i3 = (j5 > 0 ? 1 : (j5 == 0 ? 0 : -1));
        int[] iArr2 = new int[(i3 == 0 ? length + 1 : length + 2)];
        int length2 = iArr2.length - 1;
        int i4 = length - 1;
        int i5 = i4;
        while (i5 >= 0) {
            long j7 = ((((long) iArr[i5]) & 4294967295L) * j6) + j2;
            iArr2[length2] = (int) j7;
            i5--;
            length2--;
            j4 = j7 >>> 32;
            i = i;
        }
        int i6 = i;
        iArr2[length2] = (int) j2;
        if (i3 != 0) {
            int length3 = iArr2.length - 2;
            j2 = 0;
            while (i4 >= 0) {
                long j8 = ((((long) iArr[i4]) & 4294967295L) * j5) + (((long) iArr2[length3]) & 4294967295L) + j2;
                iArr2[length3] = (int) j8;
                j2 = j8 >>> 32;
                i4--;
                length3--;
            }
            iArr2[0] = (int) j2;
        }
        if (j2 == 0) {
            iArr2 = Arrays.copyOfRange(iArr2, 1, iArr2.length);
        }
        return new BigInteger(iArr2, i6);
    }

    private static int[] multiplyToLen(int[] iArr, int i, int[] iArr2, int i2, int[] iArr3) {
        multiplyToLenCheck(iArr, i);
        multiplyToLenCheck(iArr2, i2);
        return implMultiplyToLen(iArr, i, iArr2, i2, iArr3);
    }

    private static int[] implMultiplyToLen(int[] iArr, int i, int[] iArr2, int i2, int[] iArr3) {
        int[] iArr4 = iArr3;
        int i3 = i - 1;
        int i4 = i2 - 1;
        if (iArr4 == null || iArr4.length < i + i2) {
            iArr4 = new int[(i + i2)];
        }
        int i5 = i4 + 1;
        int i6 = i5 + i3;
        int i7 = i4;
        long j = 0;
        while (i7 >= 0) {
            long j2 = ((((long) iArr2[i7]) & 4294967295L) * (((long) iArr[i3]) & 4294967295L)) + j;
            iArr4[i6] = (int) j2;
            j = j2 >>> 32;
            i7--;
            i6--;
        }
        iArr4[i3] = (int) j;
        for (int i8 = i3 - 1; i8 >= 0; i8--) {
            int i9 = i5 + i8;
            int i10 = i4;
            long j3 = 0;
            while (i10 >= 0) {
                long j4 = ((((long) iArr2[i10]) & 4294967295L) * (((long) iArr[i8]) & 4294967295L)) + (((long) iArr4[i9]) & 4294967295L) + j3;
                iArr4[i9] = (int) j4;
                j3 = j4 >>> 32;
                i10--;
                i9--;
            }
            iArr4[i8] = (int) j3;
        }
        return iArr4;
    }

    private static void multiplyToLenCheck(int[] iArr, int i) {
        if (i > 0) {
            Objects.requireNonNull(iArr);
            if (i > iArr.length) {
                throw new ArrayIndexOutOfBoundsException(i - 1);
            }
        }
    }

    private static BigInteger multiplyKaratsuba(BigInteger bigInteger, BigInteger bigInteger2) {
        int max = (Math.max(bigInteger.mag.length, bigInteger2.mag.length) + 1) / 2;
        BigInteger lower = bigInteger.getLower(max);
        BigInteger upper = bigInteger.getUpper(max);
        BigInteger lower2 = bigInteger2.getLower(max);
        BigInteger upper2 = bigInteger2.getUpper(max);
        BigInteger multiply = upper.multiply(upper2);
        BigInteger multiply2 = lower.multiply(lower2);
        int i = max * 32;
        BigInteger add = multiply.shiftLeft(i).add(upper.add(lower).multiply(upper2.add(lower2)).subtract(multiply).subtract(multiply2)).shiftLeft(i).add(multiply2);
        return bigInteger.signum != bigInteger2.signum ? add.negate() : add;
    }

    private static BigInteger multiplyToomCook3(BigInteger bigInteger, BigInteger bigInteger2) {
        int max = Math.max(bigInteger.mag.length, bigInteger2.mag.length);
        int i = (max + 2) / 3;
        int i2 = max - (i * 2);
        BigInteger toomSlice = bigInteger.getToomSlice(i, i2, 0, max);
        BigInteger toomSlice2 = bigInteger.getToomSlice(i, i2, 1, max);
        BigInteger toomSlice3 = bigInteger.getToomSlice(i, i2, 2, max);
        BigInteger toomSlice4 = bigInteger2.getToomSlice(i, i2, 0, max);
        BigInteger toomSlice5 = bigInteger2.getToomSlice(i, i2, 1, max);
        BigInteger toomSlice6 = bigInteger2.getToomSlice(i, i2, 2, max);
        BigInteger multiply = toomSlice3.multiply(toomSlice6, true);
        BigInteger add = toomSlice.add(toomSlice3);
        BigInteger add2 = toomSlice4.add(toomSlice6);
        BigInteger multiply2 = add.subtract(toomSlice2).multiply(add2.subtract(toomSlice5), true);
        BigInteger add3 = add.add(toomSlice2);
        BigInteger add4 = add2.add(toomSlice5);
        BigInteger multiply3 = add3.multiply(add4, true);
        BigInteger multiply4 = add3.add(toomSlice).shiftLeft(1).subtract(toomSlice3).multiply(add4.add(toomSlice4).shiftLeft(1).subtract(toomSlice6), true);
        BigInteger multiply5 = toomSlice.multiply(toomSlice4, true);
        BigInteger exactDivideBy3 = multiply4.subtract(multiply2).exactDivideBy3();
        BigInteger shiftRight = multiply3.subtract(multiply2).shiftRight(1);
        BigInteger subtract = multiply3.subtract(multiply);
        BigInteger shiftRight2 = exactDivideBy3.subtract(subtract).shiftRight(1);
        BigInteger subtract2 = subtract.subtract(shiftRight).subtract(multiply5);
        BigInteger subtract3 = shiftRight2.subtract(multiply5.shiftLeft(1));
        int i3 = i * 32;
        BigInteger add5 = multiply5.shiftLeft(i3).add(subtract3).shiftLeft(i3).add(subtract2).shiftLeft(i3).add(shiftRight.subtract(subtract3)).shiftLeft(i3).add(multiply);
        return bigInteger.signum != bigInteger2.signum ? add5.negate() : add5;
    }

    private BigInteger getToomSlice(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int[] iArr = this.mag;
        int length = iArr.length;
        int i7 = i4 - length;
        if (i3 == 0) {
            i6 = 0 - i7;
            i5 = (i2 - 1) - i7;
        } else {
            int i8 = (i2 + ((i3 - 1) * i)) - i7;
            int i9 = i8;
            i5 = (i + i8) - 1;
            i6 = i9;
        }
        if (i6 < 0) {
            i6 = 0;
        }
        if (i5 < 0) {
            return ZERO;
        }
        int i10 = (i5 - i6) + 1;
        if (i10 <= 0) {
            return ZERO;
        }
        if (i6 == 0 && i10 >= length) {
            return abs();
        }
        int[] iArr2 = new int[i10];
        System.arraycopy((Object) iArr, i6, (Object) iArr2, 0, i10);
        return new BigInteger(trustedStripLeadingZeroInts(iArr2), 1);
    }

    private BigInteger exactDivideBy3() {
        int length = this.mag.length;
        int[] iArr = new int[length];
        long j = 0;
        for (int i = length - 1; i >= 0; i--) {
            long j2 = ((long) this.mag[i]) & 4294967295L;
            long j3 = j2 - j;
            long j4 = j > j2 ? 1 : 0;
            long j5 = (j3 * 2863311531L) & 4294967295L;
            iArr[i] = (int) j5;
            if (j5 >= 1431655766) {
                j4++;
                if (j5 >= 2863311531L) {
                    j4++;
                }
            }
            j = j4;
        }
        return new BigInteger(trustedStripLeadingZeroInts(iArr), this.signum);
    }

    private BigInteger getLower(int i) {
        int[] iArr = this.mag;
        int length = iArr.length;
        if (length <= i) {
            return abs();
        }
        int[] iArr2 = new int[i];
        System.arraycopy((Object) iArr, length - i, (Object) iArr2, 0, i);
        return new BigInteger(trustedStripLeadingZeroInts(iArr2), 1);
    }

    private BigInteger getUpper(int i) {
        int[] iArr = this.mag;
        int length = iArr.length;
        if (length <= i) {
            return ZERO;
        }
        int i2 = length - i;
        int[] iArr2 = new int[i2];
        System.arraycopy((Object) iArr, 0, (Object) iArr2, 0, i2);
        return new BigInteger(trustedStripLeadingZeroInts(iArr2), 1);
    }

    private BigInteger square() {
        return square(false);
    }

    private BigInteger square(boolean z) {
        if (this.signum == 0) {
            return ZERO;
        }
        int[] iArr = this.mag;
        int length = iArr.length;
        if (length < 128) {
            return new BigInteger(trustedStripLeadingZeroInts(squareToLen(iArr, length, (int[]) null)), 1);
        }
        if (length < 216) {
            return squareKaratsuba();
        }
        if (!z && ((long) bitLength(iArr, iArr.length)) > 1073741824) {
            reportOverflow();
        }
        return squareToomCook3();
    }

    private static final int[] squareToLen(int[] iArr, int i, int[] iArr2) {
        int i2 = i << 1;
        if (iArr2 == null || iArr2.length < i2) {
            iArr2 = new int[i2];
        }
        implSquareToLenChecks(iArr, i, iArr2, i2);
        return implSquareToLen(iArr, i, iArr2, i2);
    }

    private static void implSquareToLenChecks(int[] iArr, int i, int[] iArr2, int i2) throws RuntimeException {
        if (i < 1) {
            throw new IllegalArgumentException("invalid input length: " + i);
        } else if (i <= iArr.length) {
            int i3 = i * 2;
            if (i3 > iArr2.length) {
                throw new IllegalArgumentException("input length out of bound: " + i3 + " > " + iArr2.length);
            } else if (i2 < 1) {
                throw new IllegalArgumentException("invalid input length: " + i2);
            } else if (i2 > iArr2.length) {
                throw new IllegalArgumentException("input length out of bound: " + i + " > " + iArr2.length);
            }
        } else {
            throw new IllegalArgumentException("input length out of bound: " + i + " > " + iArr.length);
        }
    }

    private static final int[] implSquareToLen(int[] iArr, int i, int[] iArr2, int i2) {
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < i; i5++) {
            long j = ((long) iArr[i5]) & 4294967295L;
            long j2 = j * j;
            int i6 = i3 + 1;
            iArr2[i3] = (i4 << 31) | ((int) (j2 >>> 33));
            i3 = i6 + 1;
            iArr2[i6] = (int) (j2 >>> 1);
            i4 = (int) j2;
        }
        int i7 = i;
        int i8 = 1;
        while (i7 > 0) {
            int i9 = i7 - 1;
            addOne(iArr2, i8 - 1, i7, mulAdd(iArr2, iArr, i8, i9, iArr[i9]));
            i7--;
            i8 += 2;
        }
        primitiveLeftShift(iArr2, i2, 1);
        int i10 = i2 - 1;
        iArr2[i10] = (iArr[i - 1] & 1) | iArr2[i10];
        return iArr2;
    }

    private BigInteger squareKaratsuba() {
        int length = (this.mag.length + 1) / 2;
        BigInteger lower = getLower(length);
        BigInteger upper = getUpper(length);
        BigInteger square = upper.square();
        BigInteger square2 = lower.square();
        int i = length * 32;
        return square.shiftLeft(i).add(lower.add(upper).square().subtract(square.add(square2))).shiftLeft(i).add(square2);
    }

    private BigInteger squareToomCook3() {
        int length = this.mag.length;
        int i = (length + 2) / 3;
        int i2 = length - (i * 2);
        BigInteger toomSlice = getToomSlice(i, i2, 0, length);
        BigInteger toomSlice2 = getToomSlice(i, i2, 1, length);
        BigInteger toomSlice3 = getToomSlice(i, i2, 2, length);
        BigInteger square = toomSlice3.square(true);
        BigInteger add = toomSlice.add(toomSlice3);
        BigInteger square2 = add.subtract(toomSlice2).square(true);
        BigInteger add2 = add.add(toomSlice2);
        BigInteger square3 = add2.square(true);
        BigInteger square4 = toomSlice.square(true);
        BigInteger exactDivideBy3 = add2.add(toomSlice).shiftLeft(1).subtract(toomSlice3).square(true).subtract(square2).exactDivideBy3();
        BigInteger shiftRight = square3.subtract(square2).shiftRight(1);
        BigInteger subtract = square3.subtract(square);
        BigInteger shiftRight2 = exactDivideBy3.subtract(subtract).shiftRight(1);
        BigInteger subtract2 = subtract.subtract(shiftRight).subtract(square4);
        BigInteger subtract3 = shiftRight2.subtract(square4.shiftLeft(1));
        int i3 = i * 32;
        return square4.shiftLeft(i3).add(subtract3).shiftLeft(i3).add(subtract2).shiftLeft(i3).add(shiftRight.subtract(subtract3)).shiftLeft(i3).add(square);
    }

    public BigInteger divide(BigInteger bigInteger) {
        int[] iArr = this.mag;
        if (iArr.length < 40 || iArr.length - bigInteger.mag.length < 20) {
            return divideKnuth(bigInteger);
        }
        return divideAndRemainder(bigInteger)[0];
    }

    private BigInteger divideKnuth(BigInteger bigInteger) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger();
        new MutableBigInteger(this.mag).divideKnuth(new MutableBigInteger(bigInteger.mag), mutableBigInteger, false);
        return mutableBigInteger.toBigInteger(this.signum * bigInteger.signum);
    }

    public BigInteger[] divideAndRemainder(BigInteger bigInteger) {
        long j;
        long j2;
        Throwable th;
        long j3;
        long BN_new;
        BigInteger bigInteger2 = bigInteger;
        int[] iArr = bigInteger2.mag;
        if (iArr.length >= 40) {
            int[] iArr2 = this.mag;
            if (iArr2.length >= 20 && iArr2.length - iArr.length >= 20) {
                int i = this.signum == bigInteger2.signum ? 1 : -1;
                long j4 = 0;
                try {
                    long bigEndInts2NewBN = bigEndInts2NewBN(iArr2, false);
                    try {
                        j = bigEndInts2NewBN(bigInteger2.mag, false);
                        try {
                            BN_new = NativeBN.BN_new();
                        } catch (Throwable th2) {
                            th = th2;
                            j3 = 0;
                            j2 = 0;
                            j4 = bigEndInts2NewBN;
                            NativeBN.BN_free(j4);
                            NativeBN.BN_free(j);
                            NativeBN.BN_free(j2);
                            NativeBN.BN_free(j3);
                            throw th;
                        }
                        try {
                            j4 = NativeBN.BN_new();
                            j2 = BN_new;
                            try {
                                NativeBN.BN_div(BN_new, j4, bigEndInts2NewBN, j);
                                BigInteger[] bigIntegerArr = {new BigInteger(i, bn2BigEndInts(j2)), new BigInteger(this.signum, bn2BigEndInts(j4))};
                                NativeBN.BN_free(bigEndInts2NewBN);
                                NativeBN.BN_free(j);
                                NativeBN.BN_free(j2);
                                NativeBN.BN_free(j4);
                                return bigIntegerArr;
                            } catch (Throwable th3) {
                                th = th3;
                                th = th;
                                j3 = j4;
                                j4 = bigEndInts2NewBN;
                                NativeBN.BN_free(j4);
                                NativeBN.BN_free(j);
                                NativeBN.BN_free(j2);
                                NativeBN.BN_free(j3);
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            j2 = BN_new;
                            th = th;
                            j3 = j4;
                            j4 = bigEndInts2NewBN;
                            NativeBN.BN_free(j4);
                            NativeBN.BN_free(j);
                            NativeBN.BN_free(j2);
                            NativeBN.BN_free(j3);
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        j3 = 0;
                        j2 = 0;
                        j = 0;
                        j4 = bigEndInts2NewBN;
                        NativeBN.BN_free(j4);
                        NativeBN.BN_free(j);
                        NativeBN.BN_free(j2);
                        NativeBN.BN_free(j3);
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    j3 = 0;
                    j2 = 0;
                    j = 0;
                    NativeBN.BN_free(j4);
                    NativeBN.BN_free(j);
                    NativeBN.BN_free(j2);
                    NativeBN.BN_free(j3);
                    throw th;
                }
            }
        }
        return divideAndRemainderKnuth(bigInteger);
    }

    private BigInteger[] divideAndRemainderKnuth(BigInteger bigInteger) {
        BigInteger[] bigIntegerArr = new BigInteger[2];
        MutableBigInteger mutableBigInteger = new MutableBigInteger();
        MutableBigInteger divideKnuth = new MutableBigInteger(this.mag).divideKnuth(new MutableBigInteger(bigInteger.mag), mutableBigInteger);
        bigIntegerArr[0] = mutableBigInteger.toBigInteger(this.signum == bigInteger.signum ? 1 : -1);
        bigIntegerArr[1] = divideKnuth.toBigInteger(this.signum);
        return bigIntegerArr;
    }

    public BigInteger remainder(BigInteger bigInteger) {
        int[] iArr = bigInteger.mag;
        if (iArr.length < 40 || this.mag.length - iArr.length < 40) {
            return remainderKnuth(bigInteger);
        }
        return divideAndRemainder(bigInteger)[1];
    }

    private BigInteger remainderKnuth(BigInteger bigInteger) {
        return new MutableBigInteger(this.mag).divideKnuth(new MutableBigInteger(bigInteger.mag), new MutableBigInteger()).toBigInteger(this.signum);
    }

    private BigInteger divideBurnikelZiegler(BigInteger bigInteger) {
        return divideAndRemainderBurnikelZiegler(bigInteger)[0];
    }

    private BigInteger remainderBurnikelZiegler(BigInteger bigInteger) {
        return divideAndRemainderBurnikelZiegler(bigInteger)[1];
    }

    private BigInteger[] divideAndRemainderBurnikelZiegler(BigInteger bigInteger) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger();
        MutableBigInteger divideAndRemainderBurnikelZiegler = new MutableBigInteger(this).divideAndRemainderBurnikelZiegler(new MutableBigInteger(bigInteger), mutableBigInteger);
        return new BigInteger[]{mutableBigInteger.isZero() ? ZERO : mutableBigInteger.toBigInteger(this.signum * bigInteger.signum), divideAndRemainderBurnikelZiegler.isZero() ? ZERO : divideAndRemainderBurnikelZiegler.toBigInteger(this.signum)};
    }

    public BigInteger pow(int i) {
        int i2;
        if (i < 0) {
            throw new ArithmeticException("Negative exponent");
        } else if (this.signum == 0) {
            return i == 0 ? ONE : this;
        } else {
            BigInteger abs = abs();
            int lowestSetBit = abs.getLowestSetBit();
            long j = (long) i;
            long j2 = ((long) lowestSetBit) * j;
            if (j2 > 2147483647L) {
                reportOverflow();
            }
            int i3 = (int) j2;
            if (lowestSetBit > 0) {
                abs = abs.shiftRight(lowestSetBit);
                i2 = abs.bitLength();
                if (i2 == 1) {
                    if (this.signum >= 0 || (i & 1) != 1) {
                        return ONE.shiftLeft(i3);
                    }
                    return NEGATIVE_ONE.shiftLeft(i3);
                }
            } else {
                i2 = abs.bitLength();
                if (i2 == 1) {
                    if (this.signum >= 0 || (i & 1) != 1) {
                        return ONE;
                    }
                    return NEGATIVE_ONE;
                }
            }
            long j3 = ((long) i2) * j;
            int[] iArr = abs.mag;
            if (iArr.length != 1 || j3 > 62) {
                if ((((long) bitLength()) * j) / 32 > WifiManager.WIFI_FEATURE_TX_POWER_LIMIT) {
                    reportOverflow();
                }
                BigInteger bigInteger = ONE;
                int i4 = i;
                while (i4 != 0) {
                    if ((i4 & 1) == 1) {
                        bigInteger = bigInteger.multiply(abs);
                    }
                    i4 >>>= 1;
                    if (i4 != 0) {
                        abs = abs.square();
                    }
                }
                if (lowestSetBit > 0) {
                    bigInteger = bigInteger.shiftLeft(i3);
                }
                if (this.signum >= 0 || (i & 1) != 1) {
                    return bigInteger;
                }
                return bigInteger.negate();
            }
            int i5 = (this.signum >= 0 || (i & 1) != 1) ? 1 : -1;
            long j4 = ((long) iArr[0]) & 4294967295L;
            long j5 = 1;
            while (i != 0) {
                if ((i & 1) == 1) {
                    j5 *= j4;
                }
                i >>>= 1;
                if (i != 0) {
                    j4 *= j4;
                }
            }
            if (lowestSetBit <= 0) {
                return valueOf(j5 * ((long) i5));
            }
            if (((long) i3) + j3 <= 62) {
                return valueOf((j5 << i3) * ((long) i5));
            }
            return valueOf(j5 * ((long) i5)).shiftLeft(i3);
        }
    }

    public BigInteger sqrt() {
        if (this.signum >= 0) {
            return new MutableBigInteger(this.mag).sqrt().toBigInteger();
        }
        throw new ArithmeticException("Negative BigInteger");
    }

    public BigInteger[] sqrtAndRemainder() {
        BigInteger sqrt = sqrt();
        return new BigInteger[]{sqrt, subtract(sqrt.square())};
    }

    public BigInteger gcd(BigInteger bigInteger) {
        if (bigInteger.signum == 0) {
            return abs();
        }
        if (this.signum == 0) {
            return bigInteger.abs();
        }
        return new MutableBigInteger(this).hybridGCD(new MutableBigInteger(bigInteger)).toBigInteger(1);
    }

    static int bitLengthForInt(int i) {
        return 32 - Integer.numberOfLeadingZeros(i);
    }

    private static int[] leftShift(int[] iArr, int i, int i2) {
        int i3 = i2 >>> 5;
        int i4 = i2 & 31;
        int bitLengthForInt = 32 - bitLengthForInt(iArr[0]);
        if (i2 <= bitLengthForInt) {
            primitiveLeftShift(iArr, i, i4);
            return iArr;
        } else if (i4 <= bitLengthForInt) {
            int i5 = i3 + i;
            int[] iArr2 = new int[i5];
            System.arraycopy((Object) iArr, 0, (Object) iArr2, 0, i);
            primitiveLeftShift(iArr2, i5, i4);
            return iArr2;
        } else {
            int i6 = i3 + i + 1;
            int[] iArr3 = new int[i6];
            System.arraycopy((Object) iArr, 0, (Object) iArr3, 0, i);
            primitiveRightShift(iArr3, i6, 32 - i4);
            return iArr3;
        }
    }

    static void primitiveRightShift(int[] iArr, int i, int i2) {
        int i3 = 32 - i2;
        int i4 = i - 1;
        int i5 = iArr[i4];
        while (i4 > 0) {
            int i6 = iArr[i4 - 1];
            iArr[i4] = (i5 >>> i2) | (i6 << i3);
            i4--;
            i5 = i6;
        }
        iArr[0] = iArr[0] >>> i2;
    }

    static void primitiveLeftShift(int[] iArr, int i, int i2) {
        if (i != 0 && i2 != 0) {
            int i3 = 32 - i2;
            int i4 = 0;
            int i5 = iArr[0];
            int i6 = (i + 0) - 1;
            while (i4 < i6) {
                int i7 = i4 + 1;
                int i8 = iArr[i7];
                iArr[i4] = (i5 << i2) | (i8 >>> i3);
                i4 = i7;
                i5 = i8;
            }
            int i9 = i - 1;
            iArr[i9] = iArr[i9] << i2;
        }
    }

    private static int bitLength(int[] iArr, int i) {
        if (i == 0) {
            return 0;
        }
        return ((i - 1) << 5) + bitLengthForInt(iArr[0]);
    }

    public BigInteger abs() {
        return this.signum >= 0 ? this : negate();
    }

    public BigInteger negate() {
        return new BigInteger(this.mag, -this.signum);
    }

    public int signum() {
        return this.signum;
    }

    public BigInteger mod(BigInteger bigInteger) {
        if (bigInteger.signum > 0) {
            BigInteger remainder = remainder(bigInteger);
            return remainder.signum >= 0 ? remainder : remainder.add(bigInteger);
        }
        throw new ArithmeticException("BigInteger: modulus not positive");
    }

    private static int[] reverse(int[] iArr) {
        int length = iArr.length;
        int[] iArr2 = new int[length];
        for (int i = 0; i < length; i++) {
            iArr2[i] = iArr[(length - i) - 1];
        }
        return iArr2;
    }

    private static long bigEndInts2NewBN(int[] iArr, boolean z) {
        int[] reverse = reverse(iArr);
        long BN_new = NativeBN.BN_new();
        NativeBN.litEndInts2bn(reverse, reverse.length, z, BN_new);
        return BN_new;
    }

    private int[] bn2BigEndInts(long j) {
        return reverse(NativeBN.bn2litEndInts(j));
    }

    public BigInteger modPow(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigInteger3;
        BigInteger bigInteger4;
        long j;
        long j2;
        Throwable th;
        long j3;
        BigInteger bigInteger5 = this;
        BigInteger bigInteger6 = bigInteger;
        BigInteger bigInteger7 = bigInteger2;
        if (bigInteger7.signum <= 0) {
            throw new ArithmeticException("BigInteger: modulus not positive");
        } else if (bigInteger6.signum == 0) {
            BigInteger bigInteger8 = ONE;
            return bigInteger7.equals(bigInteger8) ? ZERO : bigInteger8;
        } else {
            BigInteger bigInteger9 = ONE;
            if (bigInteger5.equals(bigInteger9)) {
                return bigInteger7.equals(bigInteger9) ? ZERO : bigInteger9;
            }
            BigInteger bigInteger10 = ZERO;
            if (bigInteger5.equals(bigInteger10) && bigInteger6.signum >= 0) {
                return bigInteger10;
            }
            if (bigInteger5.equals(negConst[1]) && !bigInteger6.testBit(0)) {
                return bigInteger7.equals(bigInteger9) ? bigInteger10 : bigInteger9;
            }
            boolean z = bigInteger6.signum < 0;
            if (z) {
                bigInteger6 = bigInteger.negate();
            }
            if (bigInteger5.signum < 0 || bigInteger5.compareTo(bigInteger7) >= 0) {
                bigInteger3 = bigInteger5.mod(bigInteger7);
            } else {
                bigInteger3 = bigInteger5;
            }
            if (bigInteger7.mag.length >= 3) {
                long j4 = 0;
                try {
                    long bigEndInts2NewBN = bigEndInts2NewBN(bigInteger3.mag, false);
                    try {
                        j = bigEndInts2NewBN(bigInteger6.mag, false);
                    } catch (Throwable th2) {
                        th = th2;
                        j3 = 0;
                        j2 = 0;
                        j = 0;
                        j4 = bigEndInts2NewBN;
                        NativeBN.BN_free(j4);
                        NativeBN.BN_free(j);
                        NativeBN.BN_free(j2);
                        NativeBN.BN_free(j3);
                        throw th;
                    }
                    try {
                        j2 = bigEndInts2NewBN(bigInteger7.mag, false);
                    } catch (Throwable th3) {
                        th = th3;
                        j3 = 0;
                        j2 = 0;
                        j4 = bigEndInts2NewBN;
                        NativeBN.BN_free(j4);
                        NativeBN.BN_free(j);
                        NativeBN.BN_free(j2);
                        NativeBN.BN_free(j3);
                        throw th;
                    }
                    try {
                        long BN_new = NativeBN.BN_new();
                        NativeBN.BN_mod_exp(BN_new, bigEndInts2NewBN, j, j2);
                        BigInteger bigInteger11 = new BigInteger(1, bigInteger5.bn2BigEndInts(BN_new));
                        if (z) {
                            bigInteger11 = bigInteger11.modInverse(bigInteger7);
                        }
                        NativeBN.BN_free(bigEndInts2NewBN);
                        NativeBN.BN_free(j);
                        NativeBN.BN_free(j2);
                        NativeBN.BN_free(BN_new);
                        return bigInteger11;
                    } catch (Throwable th4) {
                        th = th4;
                        j3 = 0;
                        j4 = bigEndInts2NewBN;
                        NativeBN.BN_free(j4);
                        NativeBN.BN_free(j);
                        NativeBN.BN_free(j2);
                        NativeBN.BN_free(j3);
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    j3 = 0;
                    j2 = 0;
                    j = 0;
                    NativeBN.BN_free(j4);
                    NativeBN.BN_free(j);
                    NativeBN.BN_free(j2);
                    NativeBN.BN_free(j3);
                    throw th;
                }
            } else {
                if (bigInteger7.testBit(0)) {
                    bigInteger4 = bigInteger3.oddModPow(bigInteger6, bigInteger7);
                } else {
                    int lowestSetBit = bigInteger2.getLowestSetBit();
                    BigInteger shiftRight = bigInteger7.shiftRight(lowestSetBit);
                    BigInteger shiftLeft = bigInteger9.shiftLeft(lowestSetBit);
                    if (bigInteger5.signum < 0 || bigInteger5.compareTo(shiftRight) >= 0) {
                        bigInteger5 = bigInteger5.mod(shiftRight);
                    }
                    if (!shiftRight.equals(bigInteger9)) {
                        bigInteger10 = bigInteger5.oddModPow(bigInteger6, shiftRight);
                    }
                    BigInteger modPow2 = bigInteger3.modPow2(bigInteger6, lowestSetBit);
                    BigInteger modInverse = shiftLeft.modInverse(shiftRight);
                    BigInteger modInverse2 = shiftRight.modInverse(shiftLeft);
                    if (bigInteger7.mag.length < 33554432) {
                        bigInteger4 = bigInteger10.multiply(shiftLeft).multiply(modInverse).add(modPow2.multiply(shiftRight).multiply(modInverse2)).mod(bigInteger7);
                    } else {
                        MutableBigInteger mutableBigInteger = new MutableBigInteger();
                        new MutableBigInteger(bigInteger10.multiply(shiftLeft)).multiply(new MutableBigInteger(modInverse), mutableBigInteger);
                        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
                        new MutableBigInteger(modPow2.multiply(shiftRight)).multiply(new MutableBigInteger(modInverse2), mutableBigInteger2);
                        mutableBigInteger.add(mutableBigInteger2);
                        bigInteger4 = mutableBigInteger.divide(new MutableBigInteger(bigInteger7), new MutableBigInteger()).toBigInteger();
                    }
                }
                if (z) {
                    return bigInteger4.modInverse(bigInteger7);
                }
                return bigInteger4;
            }
        }
    }

    private static int[] montgomeryMultiply(int[] iArr, int[] iArr2, int[] iArr3, int i, long j, int[] iArr4) {
        implMontgomeryMultiplyChecks(iArr, iArr2, iArr3, i, iArr4);
        if (i > 512) {
            return montReduce(multiplyToLen(iArr, i, iArr2, i, iArr4), iArr3, i, (int) j);
        }
        return implMontgomeryMultiply(iArr, iArr2, iArr3, i, j, materialize(iArr4, i));
    }

    private static int[] montgomerySquare(int[] iArr, int[] iArr2, int i, long j, int[] iArr3) {
        implMontgomeryMultiplyChecks(iArr, iArr, iArr2, i, iArr3);
        if (i > 512) {
            return montReduce(squareToLen(iArr, i, iArr3), iArr2, i, (int) j);
        }
        return implMontgomerySquare(iArr, iArr2, i, j, materialize(iArr3, i));
    }

    private static void implMontgomeryMultiplyChecks(int[] iArr, int[] iArr2, int[] iArr3, int i, int[] iArr4) throws RuntimeException {
        if (i % 2 != 0) {
            throw new IllegalArgumentException("input array length must be even: " + i);
        } else if (i < 1) {
            throw new IllegalArgumentException("invalid input length: " + i);
        } else if (i > iArr.length || i > iArr2.length || i > iArr3.length || (iArr4 != null && i > iArr4.length)) {
            throw new IllegalArgumentException("input array length out of bound: " + i);
        }
    }

    private static int[] materialize(int[] iArr, int i) {
        return (iArr == null || iArr.length < i) ? new int[i] : iArr;
    }

    private static int[] implMontgomeryMultiply(int[] iArr, int[] iArr2, int[] iArr3, int i, long j, int[] iArr4) {
        return montReduce(multiplyToLen(iArr, i, iArr2, i, iArr4), iArr3, i, (int) j);
    }

    private static int[] implMontgomerySquare(int[] iArr, int[] iArr2, int i, long j, int[] iArr3) {
        return montReduce(squareToLen(iArr, i, iArr3), iArr2, i, (int) j);
    }

    /* JADX WARNING: Removed duplicated region for block: B:59:0x0145  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0159  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x015e  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x017d  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x019f  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0187 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.math.BigInteger oddModPow(java.math.BigInteger r29, java.math.BigInteger r30) {
        /*
            r28 = this;
            r0 = r28
            r1 = r29
            java.math.BigInteger r2 = ONE
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x000d
            return r0
        L_0x000d:
            int r2 = r0.signum
            if (r2 != 0) goto L_0x0014
            java.math.BigInteger r0 = ZERO
            return r0
        L_0x0014:
            int[] r0 = r0.mag
            java.lang.Object r0 = r0.clone()
            int[] r0 = (int[]) r0
            int[] r1 = r1.mag
            r2 = r30
            int[] r2 = r2.mag
            int r3 = r2.length
            r4 = r3 & 1
            r5 = 0
            r6 = 1
            if (r4 == 0) goto L_0x0032
            int r4 = r3 + 1
            int[] r7 = new int[r4]
            java.lang.System.arraycopy((java.lang.Object) r2, (int) r5, (java.lang.Object) r7, (int) r6, (int) r3)
            r3 = r4
            r2 = r7
        L_0x0032:
            int r4 = r1.length
            int r4 = bitLength(r1, r4)
            r7 = 17
            if (r4 != r7) goto L_0x0045
            r7 = r1[r5]
            r8 = 65537(0x10001, float:9.1837E-41)
            if (r7 == r8) goto L_0x0043
            goto L_0x0045
        L_0x0043:
            r7 = r5
            goto L_0x004f
        L_0x0045:
            r7 = r5
        L_0x0046:
            int[] r8 = bnExpModThreshTable
            r8 = r8[r7]
            if (r4 <= r8) goto L_0x004f
            int r7 = r7 + 1
            goto L_0x0046
        L_0x004f:
            int r15 = r6 << r7
            int[][] r14 = new int[r15][]
            r8 = r5
        L_0x0054:
            if (r8 >= r15) goto L_0x005d
            int[] r9 = new int[r3]
            r14[r8] = r9
            int r8 = r8 + 1
            goto L_0x0054
        L_0x005d:
            int r8 = r3 + -1
            r8 = r2[r8]
            long r8 = (long) r8
            r10 = 4294967295(0xffffffff, double:2.1219957905E-314)
            long r8 = r8 & r10
            int r12 = r3 + -2
            r12 = r2[r12]
            long r12 = (long) r12
            long r10 = r10 & r12
            r12 = 32
            long r10 = r10 << r12
            long r8 = r8 + r10
            long r8 = java.math.MutableBigInteger.inverseMod64(r8)
            long r11 = -r8
            int r8 = r0.length
            int r9 = r3 << 5
            int[] r0 = leftShift(r0, r8, r9)
            java.math.MutableBigInteger r8 = new java.math.MutableBigInteger
            r8.<init>()
            java.math.MutableBigInteger r9 = new java.math.MutableBigInteger
            r9.<init>((int[]) r0)
            java.math.MutableBigInteger r10 = new java.math.MutableBigInteger
            r10.<init>((int[]) r2)
            r10.normalize()
            java.math.MutableBigInteger r8 = r9.divide((java.math.MutableBigInteger) r10, (java.math.MutableBigInteger) r8)
            int[] r8 = r8.toIntArray()
            r14[r5] = r8
            int r9 = r8.length
            if (r9 >= r3) goto L_0x00a8
            int r9 = r8.length
            int r9 = r3 - r9
            int[] r10 = new int[r3]
            int r13 = r8.length
            java.lang.System.arraycopy((java.lang.Object) r8, (int) r5, (java.lang.Object) r10, (int) r9, (int) r13)
            r14[r5] = r10
        L_0x00a8:
            r8 = r14[r5]
            r13 = 0
            r9 = r2
            r10 = r3
            r28 = r11
            int[] r12 = montgomerySquare(r8, r9, r10, r11, r13)
            int[] r16 = java.util.Arrays.copyOf((int[]) r12, (int) r3)
            r13 = r6
        L_0x00b8:
            if (r13 >= r15) goto L_0x00db
            int r8 = r13 + -1
            r9 = r14[r8]
            r17 = 0
            r8 = r16
            r10 = r2
            r11 = r3
            r18 = r12
            r19 = r13
            r12 = r28
            r20 = r14
            r14 = r17
            int[] r8 = montgomeryMultiply(r8, r9, r10, r11, r12, r14)
            r20[r19] = r8
            int r13 = r19 + 1
            r12 = r18
            r14 = r20
            goto L_0x00b8
        L_0x00db:
            r18 = r12
            r20 = r14
            int r8 = r4 + -1
            r8 = r8 & 31
            int r8 = r6 << r8
            int r9 = r1.length
            r10 = r5
            r11 = r10
            r12 = r11
        L_0x00e9:
            r16 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r10 > r7) goto L_0x0105
            int r11 = r11 << 1
            r13 = r1[r12]
            r13 = r13 & r8
            if (r13 == 0) goto L_0x00f6
            r13 = r6
            goto L_0x00f7
        L_0x00f6:
            r13 = r5
        L_0x00f7:
            r11 = r11 | r13
            int r8 = r8 >>> 1
            if (r8 != 0) goto L_0x0102
            int r12 = r12 + 1
            int r9 = r9 + -1
            r8 = r16
        L_0x0102:
            int r10 = r10 + 1
            goto L_0x00e9
        L_0x0105:
            int r4 = r4 + -1
            int r10 = r4 - r7
        L_0x0109:
            r13 = r11 & 1
            if (r13 != 0) goto L_0x0112
            int r11 = r11 >>> 1
            int r10 = r10 + 1
            goto L_0x0109
        L_0x0112:
            int r11 = r11 >>> r6
            r11 = r20[r11]
            r14 = r0
            r0 = r5
            if (r10 != r4) goto L_0x011c
            r17 = r0
            goto L_0x011e
        L_0x011c:
            r17 = r6
        L_0x011e:
            int r4 = r4 + -1
            int r0 = r0 << r6
            if (r9 == 0) goto L_0x013b
            r13 = r1[r12]
            r13 = r13 & r8
            if (r13 == 0) goto L_0x012a
            r13 = r6
            goto L_0x012b
        L_0x012a:
            r13 = r5
        L_0x012b:
            r0 = r0 | r13
            int r8 = r8 >>> 1
            if (r8 != 0) goto L_0x013b
            int r12 = r12 + 1
            int r9 = r9 + -1
            r21 = r9
            r22 = r12
            r19 = r16
            goto L_0x0141
        L_0x013b:
            r19 = r8
            r21 = r9
            r22 = r12
        L_0x0141:
            r8 = r0 & r15
            if (r8 == 0) goto L_0x0159
            int r8 = r4 - r7
        L_0x0147:
            r9 = r0 & 1
            if (r9 != 0) goto L_0x0150
            int r0 = r0 >>> 1
            int r8 = r8 + 1
            goto L_0x0147
        L_0x0150:
            int r0 = r0 >>> 1
            r0 = r20[r0]
            r23 = r0
            r0 = r5
            r12 = r8
            goto L_0x015c
        L_0x0159:
            r12 = r10
            r23 = r11
        L_0x015c:
            if (r4 != r12) goto L_0x017d
            if (r17 == 0) goto L_0x016d
            java.lang.Object r8 = r23.clone()
            int[] r8 = (int[]) r8
            r17 = r5
            r24 = r12
            r18 = r14
            goto L_0x017b
        L_0x016d:
            r8 = r18
            r9 = r23
            r10 = r2
            r11 = r3
            r24 = r12
            r12 = r28
            int[] r8 = montgomeryMultiply(r8, r9, r10, r11, r12, r14)
        L_0x017b:
            r14 = r8
            goto L_0x0185
        L_0x017d:
            r24 = r12
            r27 = r18
            r18 = r14
            r14 = r27
        L_0x0185:
            if (r4 != 0) goto L_0x019f
            int r0 = r3 * 2
            int[] r0 = new int[r0]
            java.lang.System.arraycopy((java.lang.Object) r14, (int) r5, (java.lang.Object) r0, (int) r3, (int) r3)
            r11 = r28
            int r1 = (int) r11
            int[] r0 = montReduce(r0, r2, r3, r1)
            int[] r0 = java.util.Arrays.copyOf((int[]) r0, (int) r3)
            java.math.BigInteger r1 = new java.math.BigInteger
            r1.<init>((int) r6, (int[]) r0)
            return r1
        L_0x019f:
            r11 = r28
            if (r17 != 0) goto L_0x01bc
            r8 = r14
            r9 = r2
            r10 = r3
            r25 = r11
            r13 = r18
            int[] r18 = montgomerySquare(r8, r9, r10, r11, r13)
            r8 = r19
            r9 = r21
            r12 = r22
            r11 = r23
            r10 = r24
            r28 = r25
            goto L_0x011e
        L_0x01bc:
            r28 = r11
            r8 = r19
            r9 = r21
            r12 = r22
            r11 = r23
            r10 = r24
            r27 = r18
            r18 = r14
            r14 = r27
            goto L_0x011e
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigInteger.oddModPow(java.math.BigInteger, java.math.BigInteger):java.math.BigInteger");
    }

    private static int[] montReduce(int[] iArr, int[] iArr2, int i, int i2) {
        int i3 = 0;
        int i4 = i;
        int i5 = 0;
        do {
            i5 += addOne(iArr, i3, i, mulAdd(iArr, iArr2, i3, i, iArr[(iArr.length - 1) - i3] * i2));
            i3++;
            i4--;
        } while (i4 > 0);
        while (i5 > 0) {
            i5 += subN(iArr, iArr2, i);
        }
        while (intArrayCmpToLen(iArr, iArr2, i) >= 0) {
            subN(iArr, iArr2, i);
        }
        return iArr;
    }

    private static int intArrayCmpToLen(int[] iArr, int[] iArr2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = ((((long) iArr[i2]) & 4294967295L) > (4294967295L & ((long) iArr2[i2])) ? 1 : ((((long) iArr[i2]) & 4294967295L) == (4294967295L & ((long) iArr2[i2])) ? 0 : -1));
            if (i3 < 0) {
                return -1;
            }
            if (i3 > 0) {
                return 1;
            }
        }
        return 0;
    }

    private static int subN(int[] iArr, int[] iArr2, int i) {
        long j = 0;
        while (true) {
            i--;
            if (i < 0) {
                return (int) (j >> 32);
            }
            j = (j >> 32) + ((((long) iArr[i]) & 4294967295L) - (4294967295L & ((long) iArr2[i])));
            iArr[i] = (int) j;
        }
    }

    static int mulAdd(int[] iArr, int[] iArr2, int i, int i2, int i3) {
        implMulAddCheck(iArr, iArr2, i, i2, i3);
        return implMulAdd(iArr, iArr2, i, i2, i3);
    }

    private static void implMulAddCheck(int[] iArr, int[] iArr2, int i, int i2, int i3) {
        if (i2 > iArr2.length) {
            throw new IllegalArgumentException("input length is out of bound: " + i2 + " > " + iArr2.length);
        } else if (i < 0) {
            throw new IllegalArgumentException("input offset is invalid: " + i);
        } else if (i > iArr.length - 1) {
            StringBuilder sb = new StringBuilder("input offset is out of bound: ");
            sb.append(i);
            sb.append(" > ");
            sb.append(iArr.length - 1);
            throw new IllegalArgumentException(sb.toString());
        } else if (i2 > iArr.length - i) {
            throw new IllegalArgumentException("input len is out of bound: " + i2 + " > " + (iArr.length - i));
        }
    }

    private static int implMulAdd(int[] iArr, int[] iArr2, int i, int i2, int i3) {
        long j = ((long) i3) & 4294967295L;
        int length = (iArr.length - i) - 1;
        int i4 = i2 - 1;
        long j2 = 0;
        while (i4 >= 0) {
            long j3 = ((((long) iArr2[i4]) & 4294967295L) * j) + (((long) iArr[length]) & 4294967295L) + j2;
            iArr[length] = (int) j3;
            j2 = j3 >>> 32;
            i4--;
            length--;
        }
        return (int) j2;
    }

    static int addOne(int[] iArr, int i, int i2, int i3) {
        int i4;
        int length = ((iArr.length - 1) - i2) - i;
        long j = (((long) iArr[length]) & 4294967295L) + (4294967295L & ((long) i3));
        iArr[length] = (int) j;
        if ((j >>> 32) == 0) {
            return 0;
        }
        do {
            i2--;
            if (i2 < 0 || length - 1 < 0) {
                return 1;
            }
            i4 = iArr[length] + 1;
            iArr[length] = i4;
        } while (i4 == 0);
        return 0;
    }

    private BigInteger modPow2(BigInteger bigInteger, int i) {
        int i2;
        BigInteger bigInteger2 = ONE;
        BigInteger mod2 = mod2(i);
        int bitLength = bigInteger.bitLength();
        int i3 = 0;
        if (testBit(0) && i - 1 < bitLength) {
            bitLength = i2;
        }
        while (i3 < bitLength) {
            if (bigInteger.testBit(i3)) {
                bigInteger2 = bigInteger2.multiply(mod2).mod2(i);
            }
            i3++;
            if (i3 < bitLength) {
                mod2 = mod2.square().mod2(i);
            }
        }
        return bigInteger2;
    }

    private BigInteger mod2(int i) {
        if (bitLength() <= i) {
            return this;
        }
        int i2 = (i + 31) >>> 5;
        int[] iArr = new int[i2];
        int[] iArr2 = this.mag;
        System.arraycopy((Object) iArr2, iArr2.length - i2, (Object) iArr, 0, i2);
        int i3 = (int) (((1 << (32 - ((i2 << 5) - i))) - 1) & ((long) iArr[0]));
        iArr[0] = i3;
        return i3 == 0 ? new BigInteger(1, iArr) : new BigInteger(iArr, 1);
    }

    public BigInteger modInverse(BigInteger bigInteger) {
        if (bigInteger.signum == 1) {
            BigInteger bigInteger2 = ONE;
            if (bigInteger.equals(bigInteger2)) {
                return ZERO;
            }
            if (this.signum < 0 || compareMagnitude(bigInteger) >= 0) {
                this = mod(bigInteger);
            }
            if (this.equals(bigInteger2)) {
                return bigInteger2;
            }
            return new MutableBigInteger(this).mutableModInverse(new MutableBigInteger(bigInteger)).toBigInteger(1);
        }
        throw new ArithmeticException("BigInteger: modulus not positive");
    }

    public BigInteger shiftLeft(int i) {
        if (this.signum == 0) {
            return ZERO;
        }
        if (i > 0) {
            return new BigInteger(shiftLeft(this.mag, i), this.signum);
        }
        if (i == 0) {
            return this;
        }
        return shiftRightImpl(-i);
    }

    private static int[] shiftLeft(int[] iArr, int i) {
        int i2;
        int[] iArr2;
        int i3 = i >>> 5;
        int i4 = i & 31;
        int length = iArr.length;
        int i5 = 0;
        if (i4 == 0) {
            int[] iArr3 = new int[(i3 + length)];
            System.arraycopy((Object) iArr, 0, (Object) iArr3, 0, length);
            return iArr3;
        }
        int i6 = 32 - i4;
        int i7 = iArr[0] >>> i6;
        if (i7 != 0) {
            iArr2 = new int[(i3 + length + 1)];
            iArr2[0] = i7;
            i2 = 1;
        } else {
            iArr2 = new int[(i3 + length)];
            i2 = 0;
        }
        while (i5 < length - 1) {
            int i8 = i5 + 1;
            iArr2[i2] = (iArr[i5] << i4) | (iArr[i8] >>> i6);
            i2++;
            i5 = i8;
        }
        iArr2[i2] = iArr[i5] << i4;
        return iArr2;
    }

    public BigInteger shiftRight(int i) {
        if (this.signum == 0) {
            return ZERO;
        }
        if (i > 0) {
            return shiftRightImpl(i);
        }
        if (i == 0) {
            return this;
        }
        return new BigInteger(shiftLeft(this.mag, -i), this.signum);
    }

    private BigInteger shiftRightImpl(int i) {
        int[] iArr;
        int i2;
        int i3 = i >>> 5;
        int i4 = i & 31;
        int[] iArr2 = this.mag;
        int length = iArr2.length;
        boolean z = true;
        if (i3 >= length) {
            return this.signum >= 0 ? ZERO : negConst[1];
        }
        if (i4 == 0) {
            iArr = Arrays.copyOf(iArr2, length - i3);
        } else {
            int i5 = iArr2[0] >>> i4;
            if (i5 != 0) {
                int[] iArr3 = new int[(length - i3)];
                iArr3[0] = i5;
                iArr = iArr3;
                i2 = 1;
            } else {
                iArr = new int[((length - i3) - 1)];
                i2 = 0;
            }
            int i6 = 32 - i4;
            int i7 = 0;
            while (i7 < (length - i3) - 1) {
                int[] iArr4 = this.mag;
                int i8 = i7 + 1;
                iArr[i2] = (iArr4[i7] << i6) | (iArr4[i8] >>> i4);
                i2++;
                i7 = i8;
            }
        }
        if (this.signum < 0) {
            int i9 = length - i3;
            boolean z2 = false;
            for (int i10 = length - 1; i10 >= i9 && !z2; i10--) {
                z2 = this.mag[i10] != 0;
            }
            if (!z2 && i4 != 0) {
                if ((this.mag[i9 - 1] << (32 - i4)) == 0) {
                    z = false;
                }
                z2 = z;
            }
            if (z2) {
                iArr = javaIncrement(iArr);
            }
        }
        return new BigInteger(iArr, this.signum);
    }

    /* access modifiers changed from: package-private */
    public int[] javaIncrement(int[] iArr) {
        int i = 0;
        for (int length = iArr.length - 1; length >= 0 && i == 0; length--) {
            i = iArr[length] + 1;
            iArr[length] = i;
        }
        if (i != 0) {
            return iArr;
        }
        int[] iArr2 = new int[(iArr.length + 1)];
        iArr2[0] = 1;
        return iArr2;
    }

    public BigInteger and(BigInteger bigInteger) {
        int max = Math.max(intLength(), bigInteger.intLength());
        int[] iArr = new int[max];
        for (int i = 0; i < max; i++) {
            int i2 = (max - i) - 1;
            iArr[i] = bigInteger.getInt(i2) & getInt(i2);
        }
        return valueOf(iArr);
    }

    /* renamed from: or */
    public BigInteger mo60203or(BigInteger bigInteger) {
        int max = Math.max(intLength(), bigInteger.intLength());
        int[] iArr = new int[max];
        for (int i = 0; i < max; i++) {
            int i2 = (max - i) - 1;
            iArr[i] = bigInteger.getInt(i2) | getInt(i2);
        }
        return valueOf(iArr);
    }

    public BigInteger xor(BigInteger bigInteger) {
        int max = Math.max(intLength(), bigInteger.intLength());
        int[] iArr = new int[max];
        for (int i = 0; i < max; i++) {
            int i2 = (max - i) - 1;
            iArr[i] = bigInteger.getInt(i2) ^ getInt(i2);
        }
        return valueOf(iArr);
    }

    public BigInteger not() {
        int intLength = intLength();
        int[] iArr = new int[intLength];
        for (int i = 0; i < intLength; i++) {
            iArr[i] = ~getInt((intLength - i) - 1);
        }
        return valueOf(iArr);
    }

    public BigInteger andNot(BigInteger bigInteger) {
        int max = Math.max(intLength(), bigInteger.intLength());
        int[] iArr = new int[max];
        for (int i = 0; i < max; i++) {
            int i2 = (max - i) - 1;
            iArr[i] = (~bigInteger.getInt(i2)) & getInt(i2);
        }
        return valueOf(iArr);
    }

    public boolean testBit(int i) {
        if (i >= 0) {
            return (getInt(i >>> 5) & (1 << (i & 31))) != 0;
        }
        throw new ArithmeticException("Negative bit address");
    }

    public BigInteger setBit(int i) {
        if (i >= 0) {
            int i2 = i >>> 5;
            int max = Math.max(intLength(), i2 + 2);
            int[] iArr = new int[max];
            for (int i3 = 0; i3 < max; i3++) {
                iArr[(max - i3) - 1] = getInt(i3);
            }
            int i4 = (max - i2) - 1;
            iArr[i4] = iArr[i4] | (1 << (i & 31));
            return valueOf(iArr);
        }
        throw new ArithmeticException("Negative bit address");
    }

    public BigInteger clearBit(int i) {
        if (i >= 0) {
            int i2 = i >>> 5;
            int max = Math.max(intLength(), ((i + 1) >>> 5) + 1);
            int[] iArr = new int[max];
            for (int i3 = 0; i3 < max; i3++) {
                iArr[(max - i3) - 1] = getInt(i3);
            }
            int i4 = (max - i2) - 1;
            iArr[i4] = iArr[i4] & (~(1 << (i & 31)));
            return valueOf(iArr);
        }
        throw new ArithmeticException("Negative bit address");
    }

    public BigInteger flipBit(int i) {
        if (i >= 0) {
            int i2 = i >>> 5;
            int max = Math.max(intLength(), i2 + 2);
            int[] iArr = new int[max];
            for (int i3 = 0; i3 < max; i3++) {
                iArr[(max - i3) - 1] = getInt(i3);
            }
            int i4 = (max - i2) - 1;
            iArr[i4] = iArr[i4] ^ (1 << (i & 31));
            return valueOf(iArr);
        }
        throw new ArithmeticException("Negative bit address");
    }

    public int getLowestSetBit() {
        int i;
        int i2 = this.lowestSetBitPlusTwo - 2;
        if (i2 == -2) {
            if (this.signum == 0) {
                i2 = -1;
            } else {
                int i3 = 0;
                while (true) {
                    i = getInt(i3);
                    if (i != 0) {
                        break;
                    }
                    i3++;
                }
                i2 = 0 + (i3 << 5) + Integer.numberOfTrailingZeros(i);
            }
            this.lowestSetBitPlusTwo = i2 + 2;
        }
        return i2;
    }

    public int bitLength() {
        int i = this.bitLengthPlusOne - 1;
        if (i == -1) {
            int[] iArr = this.mag;
            int length = iArr.length;
            if (length == 0) {
                i = 0;
            } else {
                int bitLengthForInt = ((length - 1) << 5) + bitLengthForInt(iArr[0]);
                if (this.signum < 0) {
                    boolean z = Integer.bitCount(this.mag[0]) == 1;
                    for (int i2 = 1; i2 < length && z; i2++) {
                        z = this.mag[i2] == 0;
                    }
                    if (z) {
                        bitLengthForInt--;
                    }
                }
                i = bitLengthForInt;
            }
            this.bitLengthPlusOne = i + 1;
        }
        return i;
    }

    public int bitCount() {
        int[] iArr;
        int i;
        int i2 = this.bitCountPlusOne - 1;
        if (i2 != -1) {
            return i2;
        }
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (true) {
            iArr = this.mag;
            if (i4 >= iArr.length) {
                break;
            }
            i5 += Integer.bitCount(iArr[i4]);
            i4++;
        }
        if (this.signum < 0) {
            int length = iArr.length;
            while (true) {
                length--;
                i = this.mag[length];
                if (i != 0) {
                    break;
                }
                i3 += 32;
            }
            i5 += (i3 + Integer.numberOfTrailingZeros(i)) - 1;
        }
        int i6 = i5;
        this.bitCountPlusOne = i6 + 1;
        return i6;
    }

    public boolean isProbablePrime(int i) {
        if (i <= 0) {
            return true;
        }
        BigInteger abs = abs();
        if (abs.equals(TWO)) {
            return true;
        }
        if (!abs.testBit(0) || abs.equals(ONE)) {
            return false;
        }
        return abs.primeToCertainty(i, (Random) null);
    }

    public int compareTo(BigInteger bigInteger) {
        int i = this.signum;
        int i2 = bigInteger.signum;
        if (i == i2) {
            if (i == -1) {
                return bigInteger.compareMagnitude(this);
            }
            if (i != 1) {
                return 0;
            }
            return compareMagnitude(bigInteger);
        } else if (i > i2) {
            return 1;
        } else {
            return -1;
        }
    }

    /* access modifiers changed from: package-private */
    public final int compareMagnitude(BigInteger bigInteger) {
        int[] iArr = this.mag;
        int length = iArr.length;
        int[] iArr2 = bigInteger.mag;
        int length2 = iArr2.length;
        if (length < length2) {
            return -1;
        }
        if (length > length2) {
            return 1;
        }
        int i = 0;
        while (i < length) {
            int i2 = iArr[i];
            int i3 = iArr2[i];
            if (i2 == i3) {
                i++;
            } else if ((((long) i2) & 4294967295L) < (4294967295L & ((long) i3))) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public final int compareMagnitude(long j) {
        int[] iArr = this.mag;
        int length = iArr.length;
        if (length > 2) {
            return 1;
        }
        if (j < 0) {
            j = -j;
        }
        int i = (int) (j >>> 32);
        if (i == 0) {
            if (length < 1) {
                return -1;
            }
            if (length > 1) {
                return 1;
            }
            int i2 = iArr[0];
            int i3 = (int) j;
            if (i2 != i3) {
                return (((long) i2) & 4294967295L) < (((long) i3) & 4294967295L) ? -1 : 1;
            }
            return 0;
        } else if (length < 2) {
            return -1;
        } else {
            int i4 = iArr[0];
            if (i4 == i) {
                int i5 = iArr[1];
                int i6 = (int) j;
                if (i5 == i6) {
                    return 0;
                }
                if ((((long) i5) & 4294967295L) < (((long) i6) & 4294967295L)) {
                    return -1;
                }
                return 1;
            } else if ((((long) i4) & 4294967295L) < (((long) i) & 4294967295L)) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BigInteger)) {
            return false;
        }
        BigInteger bigInteger = (BigInteger) obj;
        if (bigInteger.signum != this.signum) {
            return false;
        }
        int[] iArr = this.mag;
        int length = iArr.length;
        int[] iArr2 = bigInteger.mag;
        if (length != iArr2.length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (iArr2[i] != iArr[i]) {
                return false;
            }
        }
        return true;
    }

    public BigInteger min(BigInteger bigInteger) {
        return compareTo(bigInteger) < 0 ? this : bigInteger;
    }

    public BigInteger max(BigInteger bigInteger) {
        return compareTo(bigInteger) > 0 ? this : bigInteger;
    }

    public int hashCode() {
        int i = 0;
        int i2 = 0;
        while (true) {
            int[] iArr = this.mag;
            if (i >= iArr.length) {
                return i2 * this.signum;
            }
            i2 = (int) (((long) (i2 * 31)) + (((long) iArr[i]) & 4294967295L));
            i++;
        }
    }

    public String toString(int i) {
        if (this.signum == 0) {
            return "0";
        }
        if (i < 2 || i > 36) {
            i = 10;
        }
        if (this.mag.length <= 20) {
            return smallToString(i);
        }
        StringBuilder sb = new StringBuilder();
        if (this.signum < 0) {
            toString(negate(), sb, i, 0);
            sb.insert(0, '-');
        } else {
            toString(this, sb, i, 0);
        }
        return sb.toString();
    }

    private String smallToString(int i) {
        if (this.signum == 0) {
            return "0";
        }
        String[] strArr = new String[(((this.mag.length * 4) + 6) / 7)];
        BigInteger abs = abs();
        int i2 = 0;
        while (abs.signum != 0) {
            BigInteger bigInteger = longRadix[i];
            MutableBigInteger mutableBigInteger = new MutableBigInteger();
            MutableBigInteger divide = new MutableBigInteger(abs.mag).divide(new MutableBigInteger(bigInteger.mag), mutableBigInteger);
            BigInteger bigInteger2 = mutableBigInteger.toBigInteger(abs.signum * bigInteger.signum);
            strArr[i2] = Long.toString(divide.toBigInteger(abs.signum * bigInteger.signum).longValue(), i);
            i2++;
            abs = bigInteger2;
        }
        StringBuilder sb = new StringBuilder((digitsPerLong[i] * i2) + 1);
        if (this.signum < 0) {
            sb.append('-');
        }
        sb.append(strArr[i2 - 1]);
        for (int i3 = i2 - 2; i3 >= 0; i3--) {
            int length = digitsPerLong[i] - strArr[i3].length();
            if (length != 0) {
                sb.append(zeros[length]);
            }
            sb.append(strArr[i3]);
        }
        return sb.toString();
    }

    private static void toString(BigInteger bigInteger, StringBuilder sb, int i, int i2) {
        if (bigInteger.mag.length <= 20) {
            String smallToString = bigInteger.smallToString(i);
            if (smallToString.length() < i2 && sb.length() > 0) {
                for (int length = smallToString.length(); length < i2; length++) {
                    sb.append('0');
                }
            }
            sb.append(smallToString);
            return;
        }
        double d = LOG_TWO;
        int round = (int) Math.round((Math.log((((double) bigInteger.bitLength()) * d) / logCache[i]) / d) - 1.0d);
        BigInteger[] divideAndRemainder = bigInteger.divideAndRemainder(getRadixConversionCache(i, round));
        int i3 = 1 << round;
        toString(divideAndRemainder[0], sb, i, i2 - i3);
        toString(divideAndRemainder[1], sb, i, i3);
    }

    private static BigInteger getRadixConversionCache(int i, int i2) {
        BigInteger[] bigIntegerArr = powerCache[i];
        if (i2 < bigIntegerArr.length) {
            return bigIntegerArr[i2];
        }
        BigInteger[] bigIntegerArr2 = (BigInteger[]) Arrays.copyOf((T[]) bigIntegerArr, i2 + 1);
        for (int length = bigIntegerArr.length; length <= i2; length++) {
            bigIntegerArr2[length] = bigIntegerArr2[length - 1].pow(2);
        }
        BigInteger[][] bigIntegerArr3 = powerCache;
        if (i2 >= bigIntegerArr3[i].length) {
            BigInteger[][] bigIntegerArr4 = (BigInteger[][]) bigIntegerArr3.clone();
            bigIntegerArr4[i] = bigIntegerArr2;
            powerCache = bigIntegerArr4;
        }
        return bigIntegerArr2[i2];
    }

    public String toString() {
        return toString(10);
    }

    public byte[] toByteArray() {
        int bitLength = (bitLength() / 8) + 1;
        byte[] bArr = new byte[bitLength];
        int i = 0;
        int i2 = 4;
        int i3 = 0;
        for (int i4 = bitLength - 1; i4 >= 0; i4--) {
            if (i2 == 4) {
                i2 = 1;
                int i5 = getInt(i3);
                i3++;
                i = i5;
            } else {
                i >>>= 8;
                i2++;
            }
            bArr[i4] = (byte) i;
        }
        return bArr;
    }

    public int intValue() {
        return getInt(0);
    }

    public long longValue() {
        long j = 0;
        for (int i = 1; i >= 0; i--) {
            j = (j << 32) + (((long) getInt(i)) & 4294967295L);
        }
        return j;
    }

    public float floatValue() {
        int i;
        if (this.signum == 0) {
            return 0.0f;
        }
        int[] iArr = this.mag;
        boolean z = true;
        int length = (((iArr.length - 1) << 5) + bitLengthForInt(iArr[0])) - 1;
        if (length < 63) {
            return (float) longValue();
        }
        if (length > 127) {
            return this.signum > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        int i2 = length - 24;
        int i3 = i2 & 31;
        int i4 = 32 - i3;
        if (i3 == 0) {
            i = this.mag[0];
        } else {
            int[] iArr2 = this.mag;
            int i5 = iArr2[0];
            int i6 = i5 >>> i3;
            i = i6 == 0 ? (iArr2[1] >>> i3) | (i5 << i4) : i6;
        }
        int i7 = (i >> 1) & FloatConsts.SIGNIF_BIT_MASK;
        if ((i & 1) == 0 || ((i7 & 1) == 0 && abs().getLowestSetBit() >= i2)) {
            z = false;
        }
        if (z) {
            i7++;
        }
        return Float.intBitsToFloat((this.signum & Integer.MIN_VALUE) | (((length + 127) << 23) + i7));
    }

    public double doubleValue() {
        int i;
        int i2;
        if (this.signum == 0) {
            return LOG_TWO;
        }
        int[] iArr = this.mag;
        boolean z = true;
        int length = (((iArr.length - 1) << 5) + bitLengthForInt(iArr[0])) - 1;
        if (length < 63) {
            return (double) longValue();
        }
        if (length > 1023) {
            return this.signum > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        int i3 = length - 53;
        int i4 = i3 & 31;
        int i5 = 32 - i4;
        if (i4 == 0) {
            int[] iArr2 = this.mag;
            i = iArr2[0];
            i2 = iArr2[1];
        } else {
            int[] iArr3 = this.mag;
            int i6 = iArr3[0];
            int i7 = i6 >>> i4;
            int i8 = iArr3[1];
            int i9 = (i6 << i5) | (i8 >>> i4);
            if (i7 == 0) {
                i2 = (iArr3[2] >>> i4) | (i8 << i5);
                i = i9;
            } else {
                i2 = i9;
                i = i7;
            }
        }
        long j = ((((long) i) & 4294967295L) << 32) | (4294967295L & ((long) i2));
        long j2 = (j >> 1) & DoubleConsts.SIGNIF_BIT_MASK;
        if ((j & 1) == 0 || ((j2 & 1) == 0 && abs().getLowestSetBit() >= i3)) {
            z = false;
        }
        if (z) {
            j2++;
        }
        return Double.longBitsToDouble(((((long) (length + 1023)) << 52) + j2) | (((long) this.signum) & Long.MIN_VALUE));
    }

    private static int[] stripLeadingZeroInts(int[] iArr) {
        int length = iArr.length;
        int i = 0;
        while (i < length && iArr[i] == 0) {
            i++;
        }
        return Arrays.copyOfRange(iArr, i, length);
    }

    private static int[] trustedStripLeadingZeroInts(int[] iArr) {
        int length = iArr.length;
        int i = 0;
        while (i < length && iArr[i] == 0) {
            i++;
        }
        return i == 0 ? iArr : Arrays.copyOfRange(iArr, i, length);
    }

    private static int[] stripLeadingZeroBytes(byte[] bArr, int i, int i2) {
        int i3 = i2 + i;
        while (i < i3 && bArr[i] == 0) {
            i++;
        }
        int i4 = ((i3 - i) + 3) >>> 2;
        int[] iArr = new int[i4];
        int i5 = i3 - 1;
        int i6 = i4 - 1;
        while (i6 >= 0) {
            int i7 = i5 - 1;
            iArr[i6] = bArr[i5] & 255;
            int min = Math.min(3, (i7 - i) + 1);
            int i8 = 8;
            while (i8 <= (min << 3)) {
                iArr[i6] = ((bArr[i7] & 255) << i8) | iArr[i6];
                i8 += 8;
                i7--;
            }
            i6--;
            i5 = i7;
        }
        return iArr;
    }

    private static int[] makePositive(byte[] bArr, int i, int i2) {
        int i3 = i2 + i;
        while (i < i3 && bArr[i] == -1) {
            i++;
        }
        int i4 = i;
        while (i4 < i3 && bArr[i4] == 0) {
            i4++;
        }
        int i5 = (((i3 - i) + (i4 == i3 ? 1 : 0)) + 3) >>> 2;
        int[] iArr = new int[i5];
        int i6 = i3 - 1;
        int i7 = i5 - 1;
        int i8 = i7;
        while (i8 >= 0) {
            int i9 = i6 - 1;
            iArr[i8] = bArr[i6] & 255;
            int min = Math.min(3, (i9 - i) + 1);
            if (min < 0) {
                min = 0;
            }
            int i10 = 8;
            while (i10 <= min * 8) {
                iArr[i8] = ((bArr[i9] & 255) << i10) | iArr[i8];
                i10 += 8;
                i9--;
            }
            iArr[i8] = (-1 >>> ((3 - min) * 8)) & (~iArr[i8]);
            i8--;
            i6 = i9;
        }
        while (i7 >= 0) {
            int i11 = (int) ((((long) iArr[i7]) & 4294967295L) + 1);
            iArr[i7] = i11;
            if (i11 != 0) {
                break;
            }
            i7--;
        }
        return iArr;
    }

    private static int[] makePositive(int[] iArr) {
        int i = 0;
        int i2 = 0;
        while (i2 < iArr.length && iArr[i2] == -1) {
            i2++;
        }
        int i3 = i2;
        while (i3 < iArr.length && iArr[i3] == 0) {
            i3++;
        }
        if (i3 == iArr.length) {
            i = 1;
        }
        int length = (iArr.length - i2) + i;
        int[] iArr2 = new int[length];
        for (int i4 = i2; i4 < iArr.length; i4++) {
            iArr2[(i4 - i2) + i] = ~iArr[i4];
        }
        int i5 = length - 1;
        while (true) {
            int i6 = iArr2[i5] + 1;
            iArr2[i5] = i6;
            if (i6 != 0) {
                return iArr2;
            }
            i5--;
        }
    }

    private int intLength() {
        return (bitLength() >>> 5) + 1;
    }

    private int signBit() {
        return this.signum < 0 ? 1 : 0;
    }

    private int signInt() {
        return this.signum < 0 ? -1 : 0;
    }

    private int getInt(int i) {
        if (i < 0) {
            return 0;
        }
        int[] iArr = this.mag;
        if (i >= iArr.length) {
            return signInt();
        }
        int i2 = iArr[(iArr.length - i) - 1];
        if (this.signum >= 0) {
            return i2;
        }
        return i <= firstNonzeroIntNum() ? -i2 : ~i2;
    }

    private int firstNonzeroIntNum() {
        int i = this.firstNonzeroIntNumPlusTwo - 2;
        if (i != -2) {
            return i;
        }
        int length = this.mag.length;
        int i2 = length - 1;
        while (i2 >= 0 && this.mag[i2] == 0) {
            i2--;
        }
        int i3 = (length - i2) - 1;
        this.firstNonzeroIntNumPlusTwo = i3 + 2;
        return i3;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        int i = readFields.get("signum", -2);
        byte[] bArr = (byte[]) readFields.get("magnitude", (Object) null);
        if (i >= -1) {
            boolean z = true;
            if (i <= 1) {
                int[] stripLeadingZeroBytes = stripLeadingZeroBytes(bArr, 0, bArr.length);
                boolean z2 = stripLeadingZeroBytes.length == 0;
                if (i != 0) {
                    z = false;
                }
                if (z2 != z) {
                    throw new StreamCorruptedException(readFields.defaulted("magnitude") ? "BigInteger: Magnitude not present in stream" : "BigInteger: signum-magnitude mismatch");
                }
                UnsafeHolder.putSign(this, i);
                UnsafeHolder.putMag(this, stripLeadingZeroBytes);
                if (stripLeadingZeroBytes.length >= MAX_MAG_LENGTH) {
                    try {
                        checkRange();
                        return;
                    } catch (ArithmeticException unused) {
                        throw new StreamCorruptedException("BigInteger: Out of the supported range");
                    }
                } else {
                    return;
                }
            }
        }
        throw new StreamCorruptedException(readFields.defaulted("signum") ? "BigInteger: Signum not present in stream" : "BigInteger: Invalid signum value");
    }

    private static class UnsafeHolder {
        private static final long magOffset;
        private static final long signumOffset;
        private static final Unsafe unsafe;

        private UnsafeHolder() {
        }

        static {
            try {
                Unsafe unsafe2 = Unsafe.getUnsafe();
                unsafe = unsafe2;
                signumOffset = unsafe2.objectFieldOffset(BigInteger.class.getDeclaredField("signum"));
                magOffset = unsafe2.objectFieldOffset(BigInteger.class.getDeclaredField("mag"));
            } catch (Exception e) {
                throw new ExceptionInInitializerError((Throwable) e);
            }
        }

        static void putSign(BigInteger bigInteger, int i) {
            unsafe.putIntVolatile(bigInteger, signumOffset, i);
        }

        static void putMag(BigInteger bigInteger, int[] iArr) {
            unsafe.putObjectVolatile(bigInteger, magOffset, iArr);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("signum", this.signum);
        putFields.put("magnitude", (Object) magSerializedForm());
        objectOutputStream.writeFields();
    }

    private byte[] magSerializedForm() {
        int i;
        int[] iArr = this.mag;
        int length = iArr.length;
        int i2 = 0;
        if (length == 0) {
            i = 0;
        } else {
            i = ((length - 1) << 5) + bitLengthForInt(iArr[0]);
        }
        int i3 = (i + 7) >>> 3;
        byte[] bArr = new byte[i3];
        int i4 = length - 1;
        int i5 = 4;
        for (int i6 = i3 - 1; i6 >= 0; i6--) {
            if (i5 == 4) {
                i2 = this.mag[i4];
                i4--;
                i5 = 1;
            } else {
                i2 >>>= 8;
                i5++;
            }
            bArr[i6] = (byte) i2;
        }
        return bArr;
    }

    public long longValueExact() {
        if (this.mag.length <= 2 && bitLength() <= 63) {
            return longValue();
        }
        throw new ArithmeticException("BigInteger out of long range");
    }

    public int intValueExact() {
        if (this.mag.length <= 1 && bitLength() <= 31) {
            return intValue();
        }
        throw new ArithmeticException("BigInteger out of int range");
    }

    public short shortValueExact() {
        int intValue;
        if (this.mag.length <= 1 && bitLength() <= 31 && (intValue = intValue()) >= -32768 && intValue <= 32767) {
            return shortValue();
        }
        throw new ArithmeticException("BigInteger out of short range");
    }

    public byte byteValueExact() {
        int intValue;
        if (this.mag.length <= 1 && bitLength() <= 31 && (intValue = intValue()) >= -128 && intValue <= 127) {
            return byteValue();
        }
        throw new ArithmeticException("BigInteger out of byte range");
    }
}
