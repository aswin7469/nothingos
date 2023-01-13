package java.awt.font;

import android.safetycenter.SafetyCenterEntry;
import androidx.core.view.PointerIconCompat;
import com.android.wifi.p018x.com.android.internal.util.AsyncChannel;
import com.nothing.p023os.device.DeviceConstant;
import java.p026io.IOException;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Set;

public final class NumericShaper implements Serializable {
    public static final int ALL_RANGES = 524287;
    public static final int ARABIC = 2;
    private static final int ARABIC_KEY = 1;
    public static final int BENGALI = 16;
    private static final int BENGALI_KEY = 4;
    private static final int BSEARCH_THRESHOLD = 3;
    private static final int CONTEXTUAL_MASK = Integer.MIN_VALUE;
    public static final int DEVANAGARI = 8;
    private static final int DEVANAGARI_KEY = 3;
    public static final int EASTERN_ARABIC = 4;
    private static final int EASTERN_ARABIC_KEY = 2;
    public static final int ETHIOPIC = 65536;
    private static final int ETHIOPIC_KEY = 16;
    public static final int EUROPEAN = 1;
    private static final int EUROPEAN_KEY = 0;
    public static final int GUJARATI = 64;
    private static final int GUJARATI_KEY = 6;
    public static final int GURMUKHI = 32;
    private static final int GURMUKHI_KEY = 5;
    public static final int KANNADA = 1024;
    private static final int KANNADA_KEY = 10;
    public static final int KHMER = 131072;
    private static final int KHMER_KEY = 17;
    public static final int LAO = 8192;
    private static final int LAO_KEY = 13;
    public static final int MALAYALAM = 2048;
    private static final int MALAYALAM_KEY = 11;
    public static final int MONGOLIAN = 262144;
    private static final int MONGOLIAN_KEY = 18;
    public static final int MYANMAR = 32768;
    private static final int MYANMAR_KEY = 15;
    private static final int NUM_KEYS = 19;
    public static final int ORIYA = 128;
    private static final int ORIYA_KEY = 7;
    public static final int TAMIL = 256;
    private static final int TAMIL_KEY = 8;
    public static final int TELUGU = 512;
    private static final int TELUGU_KEY = 9;
    public static final int THAI = 4096;
    private static final int THAI_KEY = 12;
    public static final int TIBETAN = 16384;
    private static final int TIBETAN_KEY = 14;
    private static final char[] bases = {0, 1584, 1728, 2358, 2486, 2614, 2742, 2870, 2998, 3126, 3254, 3382, 3616, 3744, 3824, 4112, 4920, 6064, 6112};
    private static final char[] contexts;
    private static int ctCache = 0;
    private static int ctCacheLimit = 0;
    private static final long serialVersionUID = -8022764705923730308L;
    private static int[] strongTable = {0, 65, 91, 97, 123, 170, 171, 181, 182, 186, 187, 192, 215, 216, 247, 248, 697, 699, 706, DeviceConstant.ORDER_FIND_DEVICE, 722, 736, 741, 750, 751, 880, 884, 886, 894, 902, 903, 904, PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 1155, 1162, 1418, 1470, 1471, 1472, 1473, 1475, 1476, 1478, 1479, 1488, 1536, 1544, 1545, 1547, 1548, 1549, 1550, 1563, 1611, 1645, 1648, 1649, 1750, 1765, 1767, 1774, 1776, 1786, 1809, 1810, 1840, 1869, 1958, 1969, 2027, 2036, 2038, 2042, 2070, 2074, 2075, 2084, 2085, 2088, 2089, 2096, 2137, 2142, 2276, 2307, 2362, 2363, 2364, 2365, 2369, 2377, 2381, 2382, 2385, 2392, 2402, 2404, 2433, 2434, 2492, 2493, 2497, 2503, 2509, 2510, 2530, 2534, 2546, 2548, 2555, 2563, 2620, 2622, 2625, 2649, 2672, 2674, 2677, 2691, 2748, 2749, 2753, 2761, 2765, 2768, 2786, 2790, 2801, 2818, 2876, 2877, 2879, 2880, 2881, 2887, 2893, 2903, 2914, 2918, 2946, 2947, 3008, 3009, 3021, 3024, 3059, 3073, 3134, 3137, 3142, 3160, 3170, 3174, 3192, 3199, 3260, 3261, 3276, 3285, 3298, 3302, 3393, 3398, 3405, 3406, 3426, 3430, 3530, 3535, 3538, 3544, 3633, 3634, 3636, 3648, 3655, 3663, 3761, 3762, 3764, 3773, 3784, 3792, 3864, 3866, 3893, 3894, 3895, 3896, 3897, 3902, 3953, 3967, 3968, 3973, 3974, 3976, 3981, 4030, 4038, 4039, 4141, 4145, 4146, 4152, 4153, 4155, 4157, 4159, 4184, 4186, 4190, 4193, 4209, 4213, 4226, 4227, 4229, 4231, 4237, 4238, 4253, 4254, 4957, 4960, 5008, 5024, 5120, 5121, 5760, 5761, 5787, 5792, 5906, 5920, 5938, 5941, 5970, 5984, 6002, 6016, 6068, 6070, 6071, 6078, 6086, 6087, 6089, 6100, 6107, 6108, 6109, 6112, 6128, 6160, 6313, 6314, 6432, 6435, 6439, 6441, 6450, 6451, 6457, 6470, 6622, 6656, 6679, 6681, 6742, 6743, 6744, 6753, 6754, 6755, 6757, 6765, 6771, 6784, 6912, 6916, 6964, 6965, 6966, 6971, 6972, 6973, 6978, 6979, 7019, 7028, 7040, 7042, 7074, 7078, 7080, 7082, 7083, 7084, 7142, 7143, 7144, 7146, 7149, 7150, 7151, 7154, 7212, 7220, 7222, 7227, 7376, 7379, 7380, 7393, 7394, 7401, 7405, 7406, 7412, 7413, 7616, 7680, 8125, 8126, 8127, 8130, 8141, 8144, 8157, 8160, 8173, 8178, 8189, 8206, 8208, 8305, 8308, 8319, 8320, 8336, 8352, 8450, 8451, 8455, 8456, 8458, 8468, 8469, 8470, 8473, 8478, 8484, 8485, 8486, 8487, 8488, 8489, 8490, 8494, 8495, 8506, 8508, 8512, 8517, 8522, 8526, 8528, 8544, 8585, 9014, 9083, 9109, 9110, 9372, 9450, 9900, 9901, 10240, 10496, 11264, 11493, 11499, 11503, 11506, 11513, 11520, 11647, 11648, 11744, 12293, 12296, 12321, 12330, 12337, 12342, 12344, 12349, 12353, 12441, 12445, 12448, 12449, 12539, 12540, 12736, 12784, 12829, 12832, 12880, 12896, 12924, 12927, 12977, 12992, 13004, 13008, 13175, 13179, 13278, 13280, 13311, 13312, 19904, 19968, 42128, 42192, 42509, 42512, 42607, 42624, 42655, 42656, 42736, 42738, 42752, 42786, 42888, 42889, 43010, 43011, 43014, 43015, 43019, 43020, 43045, 43047, 43048, 43056, 43064, 43072, 43124, 43136, 43204, 43214, 43232, 43250, 43302, 43310, 43335, 43346, 43392, 43395, 43443, 43444, 43446, 43450, 43452, 43453, 43561, 43567, 43569, 43571, 43573, 43584, 43587, 43588, 43596, 43597, 43696, 43697, 43698, 43701, 43703, 43705, 43710, 43712, 43713, 43714, 43756, 43758, 43766, 43777, 44005, 44006, 44008, 44009, 44013, 44016, 64286, 64287, 64297, 64298, 64830, 64848, 65021, 65136, 65279, 65313, 65339, 65345, 65371, 65382, 65504, 65536, 65793, 65794, 65856, 66000, 66045, 66176, 67871, 67872, 68097, 68112, 68152, 68160, 68409, 68416, 69216, 69632, AsyncChannel.CMD_CHANNEL_FULL_CONNECTION, AsyncChannel.CMD_CHANNEL_FULLY_CONNECTED, 69688, 69703, 69714, 69734, 69760, 69762, 69811, 69815, 69817, 69819, 69888, 69891, 69927, 69932, 69933, 69942, 70016, 70018, 70070, 70079, 71339, 71340, 71341, 71342, 71344, 71350, 71351, 71360, 94095, 94099, 119143, 119146, 119155, 119171, 119173, 119180, 119210, 119214, 119296, 119648, 120539, 120540, 120597, 120598, 120655, 120656, 120713, 120714, 120771, 120772, 120782, 126464, 126704, 127248, 127338, 127344, 127744, 128140, 128141, 128292, 128293, 131072, 917505, 983040, 1114110, 1114111};
    private volatile transient Range currentRange = Range.EUROPEAN;
    private int key;
    private int mask;
    private transient Range[] rangeArray;
    private transient Set<Range> rangeSet;
    private Range shapingRange;
    private volatile transient int stCache = 0;

    private static int getHighBit(int i) {
        int i2;
        if (i <= 0) {
            return -32;
        }
        if (i >= 65536) {
            i >>= 16;
            i2 = 16;
        } else {
            i2 = 0;
        }
        if (i >= 256) {
            i >>= 8;
            i2 += 8;
        }
        if (i >= 16) {
            i >>= 4;
            i2 += 4;
        }
        if (i >= 4) {
            i >>= 2;
            i2 += 2;
        }
        return i >= 2 ? i2 + 1 : i2;
    }

    public enum Range {
        EUROPEAN(48, 0, 768),
        ARABIC(1632, 1536, 1920),
        EASTERN_ARABIC(1776, 1536, 1920),
        DEVANAGARI(2406, 2304, 2432),
        BENGALI(2534, 2432, 2560),
        GURMUKHI(2662, 2560, 2688),
        GUJARATI(2790, 2816, 2944),
        ORIYA(2918, 2816, 2944),
        TAMIL(3046, 2944, 3072),
        TELUGU(3174, 3072, SafetyCenterEntry.ENTRY_SEVERITY_LEVEL_OK),
        KANNADA(3302, SafetyCenterEntry.ENTRY_SEVERITY_LEVEL_OK, 3328),
        MALAYALAM(3430, 3328, 3456),
        THAI(3664, 3584, 3712),
        LAO(3792, 3712, 3840),
        TIBETAN(3872, 3840, 4096),
        MYANMAR(4160, 4096, 4224),
        ETHIOPIC(4969, 4608, 4992, (int) null) {
            /* access modifiers changed from: package-private */
            public char getNumericBase() {
                return 1;
            }
        },
        KHMER(6112, 6016, 6144),
        MONGOLIAN(6160, 6144, 6400),
        NKO(1984, 1984, 2048),
        MYANMAR_SHAN(4240, 4096, 4256),
        LIMBU(6470, 6400, 6480),
        NEW_TAI_LUE(6608, 6528, 6624),
        BALINESE(6992, 6912, 7040),
        SUNDANESE(7088, 7040, 7104),
        LEPCHA(7232, 7168, 7248),
        OL_CHIKI(7248, 7248, 7296),
        VAI(42528, 42240, 42560),
        SAURASHTRA(43216, 43136, 43232),
        KAYAH_LI(43264, 43264, 43312),
        CHAM(43600, 43520, 43616),
        TAI_THAM_HORA(6784, 6688, 6832),
        TAI_THAM_THAM(6800, 6688, 6832),
        JAVANESE(43472, 43392, 43488),
        MEETEI_MAYEK(44016, 43968, 44032);
        
        /* access modifiers changed from: private */
        public final int base;
        /* access modifiers changed from: private */
        public final int end;
        /* access modifiers changed from: private */
        public final int start;

        /* access modifiers changed from: package-private */
        public char getNumericBase() {
            return 0;
        }

        /* access modifiers changed from: private */
        public static int toRangeIndex(Range range) {
            int ordinal = range.ordinal();
            if (ordinal < 19) {
                return ordinal;
            }
            return -1;
        }

        /* access modifiers changed from: private */
        public static Range indexToRange(int i) {
            if (i < 19) {
                return values()[i];
            }
            return null;
        }

        /* access modifiers changed from: private */
        public static int toRangeMask(Set<Range> set) {
            int i = 0;
            for (Range ordinal : set) {
                int ordinal2 = ordinal.ordinal();
                if (ordinal2 < 19) {
                    i |= 1 << ordinal2;
                }
            }
            return i;
        }

        /* access modifiers changed from: private */
        public static Set<Range> maskToRangeSet(int i) {
            EnumSet<E> noneOf = EnumSet.noneOf(Range.class);
            Range[] values = values();
            for (int i2 = 0; i2 < 19; i2++) {
                if (((1 << i2) & i) != 0) {
                    noneOf.add(values[i2]);
                }
            }
            return noneOf;
        }

        private Range(int i, int i2, int i3) {
            this.base = i - (getNumericBase() + '0');
            this.start = i2;
            this.end = i3;
        }

        /* access modifiers changed from: private */
        public int getDigitBase() {
            return this.base;
        }

        /* access modifiers changed from: private */
        public boolean inRange(int i) {
            return this.start <= i && i < this.end;
        }
    }

    static {
        char[] cArr = {0, 768, 1536, 1920, 1536, 1920, 2304, 2432, 2432, 2560, 2560, 2688, 2688, 2816, 2816, 2944, 2944, 3072, 3072, 3200, 3200, 3328, 3328, 3456, 3584, 3712, 3712, 3840, 3840, 4096, 4096, 4224, 4608, 4992, 6016, 6144, 6144, 6400, 65535};
        contexts = cArr;
        ctCacheLimit = cArr.length - 2;
    }

    private static int getContextKey(char c) {
        char[] cArr = contexts;
        int i = ctCache;
        if (c < cArr[i]) {
            while (true) {
                int i2 = ctCache;
                if (i2 <= 0 || c >= contexts[i2]) {
                    break;
                }
                ctCache = i2 - 1;
            }
        } else if (c >= cArr[i + 1]) {
            while (true) {
                int i3 = ctCache;
                if (i3 >= ctCacheLimit || c < contexts[i3 + 1]) {
                    break;
                }
                ctCache = i3 + 1;
            }
        }
        int i4 = ctCache;
        if ((i4 & 1) == 0) {
            return i4 / 2;
        }
        return 0;
    }

    private Range rangeForCodePoint(int i) {
        if (this.currentRange.inRange(i)) {
            return this.currentRange;
        }
        Range[] rangeArr = this.rangeArray;
        int i2 = 0;
        if (rangeArr.length > 3) {
            int length = rangeArr.length - 1;
            while (i2 <= length) {
                int i3 = (i2 + length) / 2;
                Range range = rangeArr[i3];
                if (i < range.start) {
                    length = i3 - 1;
                } else if (i >= range.end) {
                    i2 = i3 + 1;
                } else {
                    this.currentRange = range;
                    return range;
                }
            }
        } else {
            while (i2 < rangeArr.length) {
                if (rangeArr[i2].inRange(i)) {
                    return rangeArr[i2];
                }
                i2++;
            }
        }
        return Range.EUROPEAN;
    }

    private boolean isStrongDirectional(char c) {
        int i = this.stCache;
        int[] iArr = strongTable;
        boolean z = false;
        if (c < iArr[i]) {
            i = search(c, iArr, 0, i);
        } else {
            int i2 = i + 1;
            if (c >= iArr[i2]) {
                i = search(c, iArr, i2, (iArr.length - i) - 1);
            }
        }
        if ((i & 1) == 1) {
            z = true;
        }
        this.stCache = i;
        return z;
    }

    private static int getKeyFromMask(int i) {
        int i2 = 0;
        while (i2 < 19 && ((1 << i2) & i) == 0) {
            i2++;
        }
        if (i2 != 19 && ((~(1 << i2)) & i) == 0) {
            return i2;
        }
        throw new IllegalArgumentException("invalid shaper: " + Integer.toHexString(i));
    }

    public static NumericShaper getShaper(int i) {
        return new NumericShaper(getKeyFromMask(i), i);
    }

    public static NumericShaper getShaper(Range range) {
        return new NumericShaper(range, (Set<Range>) EnumSet.m1722of(range));
    }

    public static NumericShaper getContextualShaper(int i) {
        return new NumericShaper(0, i | Integer.MIN_VALUE);
    }

    public static NumericShaper getContextualShaper(Set<Range> set) {
        NumericShaper numericShaper = new NumericShaper(Range.EUROPEAN, set);
        numericShaper.mask = Integer.MIN_VALUE;
        return numericShaper;
    }

    public static NumericShaper getContextualShaper(int i, int i2) {
        return new NumericShaper(getKeyFromMask(i2), i | Integer.MIN_VALUE);
    }

    public static NumericShaper getContextualShaper(Set<Range> set, Range range) {
        range.getClass();
        NumericShaper numericShaper = new NumericShaper(range, set);
        numericShaper.mask = Integer.MIN_VALUE;
        return numericShaper;
    }

    private NumericShaper(int i, int i2) {
        this.key = i;
        this.mask = i2;
    }

    private NumericShaper(Range range, Set<Range> set) {
        this.shapingRange = range;
        EnumSet<Range> copyOf = EnumSet.copyOf(set);
        this.rangeSet = copyOf;
        if (copyOf.contains(Range.EASTERN_ARABIC) && this.rangeSet.contains(Range.ARABIC)) {
            this.rangeSet.remove(Range.ARABIC);
        }
        if (this.rangeSet.contains(Range.TAI_THAM_THAM) && this.rangeSet.contains(Range.TAI_THAM_HORA)) {
            this.rangeSet.remove(Range.TAI_THAM_HORA);
        }
        Set<Range> set2 = this.rangeSet;
        Range[] rangeArr = (Range[]) set2.toArray(new Range[set2.size()]);
        this.rangeArray = rangeArr;
        if (rangeArr.length > 3) {
            Arrays.sort(rangeArr, new Comparator<Range>() {
                public int compare(Range range, Range range2) {
                    if (range.base > range2.base) {
                        return 1;
                    }
                    return range.base == range2.base ? 0 : -1;
                }
            });
        }
    }

    public void shape(char[] cArr, int i, int i2) {
        checkParams(cArr, i, i2);
        if (!isContextual()) {
            shapeNonContextually(cArr, i, i2);
        } else if (this.rangeSet == null) {
            shapeContextually(cArr, i, i2, this.key);
        } else {
            shapeContextually(cArr, i, i2, this.shapingRange);
        }
    }

    public void shape(char[] cArr, int i, int i2, int i3) {
        checkParams(cArr, i, i2);
        if (isContextual()) {
            int keyFromMask = getKeyFromMask(i3);
            if (this.rangeSet == null) {
                shapeContextually(cArr, i, i2, keyFromMask);
            } else {
                shapeContextually(cArr, i, i2, Range.values()[keyFromMask]);
            }
        } else {
            shapeNonContextually(cArr, i, i2);
        }
    }

    public void shape(char[] cArr, int i, int i2, Range range) {
        checkParams(cArr, i, i2);
        if (range == null) {
            throw new NullPointerException("context is null");
        } else if (!isContextual()) {
            shapeNonContextually(cArr, i, i2);
        } else if (this.rangeSet != null) {
            shapeContextually(cArr, i, i2, range);
        } else {
            int r5 = Range.toRangeIndex(range);
            if (r5 >= 0) {
                shapeContextually(cArr, i, i2, r5);
            } else {
                shapeContextually(cArr, i, i2, this.shapingRange);
            }
        }
    }

    private void checkParams(char[] cArr, int i, int i2) {
        int i3;
        if (cArr == null) {
            throw new NullPointerException("text is null");
        } else if (i < 0 || i > cArr.length || (i3 = i + i2) < 0 || i3 > cArr.length) {
            throw new IndexOutOfBoundsException("bad start or count for text of length " + cArr.length);
        }
    }

    public boolean isContextual() {
        return (this.mask & Integer.MIN_VALUE) != 0;
    }

    public int getRanges() {
        return this.mask & Integer.MAX_VALUE;
    }

    public Set<Range> getRangeSet() {
        Set<Range> set = this.rangeSet;
        if (set != null) {
            return EnumSet.copyOf(set);
        }
        return Range.maskToRangeSet(this.mask);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: char} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0023  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void shapeNonContextually(char[] r4, int r5, int r6) {
        /*
            r3 = this;
            java.awt.font.NumericShaper$Range r0 = r3.shapingRange
            r1 = 48
            if (r0 == 0) goto L_0x0013
            int r0 = r0.getDigitBase()
            java.awt.font.NumericShaper$Range r3 = r3.shapingRange
            char r3 = r3.getNumericBase()
            int r3 = r3 + r1
        L_0x0011:
            char r1 = (char) r3
            goto L_0x0020
        L_0x0013:
            char[] r0 = bases
            int r3 = r3.key
            char r0 = r0[r3]
            r2 = 16
            if (r3 != r2) goto L_0x0020
            r3 = 49
            goto L_0x0011
        L_0x0020:
            int r6 = r6 + r5
        L_0x0021:
            if (r5 >= r6) goto L_0x0032
            char r3 = r4[r5]
            if (r3 < r1) goto L_0x002f
            r2 = 57
            if (r3 > r2) goto L_0x002f
            int r3 = r3 + r0
            char r3 = (char) r3
            r4[r5] = r3
        L_0x002f:
            int r5 = r5 + 1
            goto L_0x0021
        L_0x0032:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.font.NumericShaper.shapeNonContextually(char[], int, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x005f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void shapeContextually(char[] r11, int r12, int r13, int r14) {
        /*
            r10 = this;
            monitor-enter(r10)
            int r0 = r10.mask     // Catch:{ all -> 0x006c }
            r1 = 1
            int r2 = r1 << r14
            r0 = r0 & r2
            r2 = 0
            if (r0 != 0) goto L_0x000b
            r14 = r2
        L_0x000b:
            char[] r0 = bases     // Catch:{ all -> 0x006c }
            char r0 = r0[r14]     // Catch:{ all -> 0x006c }
            r3 = 49
            r4 = 48
            r5 = 16
            if (r14 != r5) goto L_0x0019
            r6 = r3
            goto L_0x001a
        L_0x0019:
            r6 = r4
        L_0x001a:
            java.lang.Class<java.awt.font.NumericShaper> r7 = java.awt.font.NumericShaper.class
            monitor-enter(r7)     // Catch:{ all -> 0x006c }
            int r13 = r13 + r12
        L_0x001e:
            if (r12 >= r13) goto L_0x0066
            char r8 = r11[r12]     // Catch:{ all -> 0x0069 }
            if (r8 < r6) goto L_0x002d
            r9 = 57
            if (r8 > r9) goto L_0x002d
            int r9 = r8 + r0
            char r9 = (char) r9     // Catch:{ all -> 0x0069 }
            r11[r12] = r9     // Catch:{ all -> 0x0069 }
        L_0x002d:
            boolean r9 = r10.isStrongDirectional(r8)     // Catch:{ all -> 0x0069 }
            if (r9 == 0) goto L_0x0063
            int r8 = getContextKey(r8)     // Catch:{ all -> 0x0069 }
            if (r8 == r14) goto L_0x0063
            int r14 = r10.mask     // Catch:{ all -> 0x0069 }
            r0 = r14 & 4
            r6 = 2
            if (r0 == 0) goto L_0x0045
            if (r8 == r1) goto L_0x0057
            if (r8 != r6) goto L_0x0045
            goto L_0x0057
        L_0x0045:
            r0 = r14 & 2
            if (r0 == 0) goto L_0x004f
            if (r8 == r1) goto L_0x004d
            if (r8 != r6) goto L_0x004f
        L_0x004d:
            r6 = r1
            goto L_0x0057
        L_0x004f:
            int r0 = r1 << r8
            r14 = r14 & r0
            if (r14 != 0) goto L_0x0056
            r6 = r2
            goto L_0x0057
        L_0x0056:
            r6 = r8
        L_0x0057:
            char[] r14 = bases     // Catch:{ all -> 0x0069 }
            char r14 = r14[r6]     // Catch:{ all -> 0x0069 }
            if (r6 != r5) goto L_0x005f
            r0 = r3
            goto L_0x0060
        L_0x005f:
            r0 = r4
        L_0x0060:
            r6 = r0
            r0 = r14
            r14 = r8
        L_0x0063:
            int r12 = r12 + 1
            goto L_0x001e
        L_0x0066:
            monitor-exit(r7)     // Catch:{ all -> 0x0069 }
            monitor-exit(r10)
            return
        L_0x0069:
            r11 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0069 }
            throw r11     // Catch:{ all -> 0x006c }
        L_0x006c:
            r11 = move-exception
            monitor-exit(r10)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.font.NumericShaper.shapeContextually(char[], int, int, int):void");
    }

    private void shapeContextually(char[] cArr, int i, int i2, Range range) {
        Range rangeForCodePoint;
        if (range == null || !this.rangeSet.contains(range)) {
            range = Range.EUROPEAN;
        }
        int r0 = range.getDigitBase();
        char numericBase = (char) (range.getNumericBase() + '0');
        int i3 = i2 + i;
        while (i < i3) {
            char c = cArr[i];
            if (c >= numericBase && c <= '9') {
                cArr[i] = (char) (c + r0);
            } else if (isStrongDirectional(c) && (rangeForCodePoint = rangeForCodePoint(c)) != range) {
                int r8 = rangeForCodePoint.getDigitBase();
                numericBase = (char) (rangeForCodePoint.getNumericBase() + '0');
                r0 = r8;
                range = rangeForCodePoint;
            }
            i++;
        }
    }

    public int hashCode() {
        int i = this.mask;
        Set<Range> set = this.rangeSet;
        return set != null ? (i & Integer.MIN_VALUE) ^ set.hashCode() : i;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            NumericShaper numericShaper = (NumericShaper) obj;
            if (this.rangeSet != null) {
                if (numericShaper.rangeSet != null) {
                    if (isContextual() == numericShaper.isContextual() && this.rangeSet.equals(numericShaper.rangeSet) && this.shapingRange == numericShaper.shapingRange) {
                        return true;
                    }
                    return false;
                } else if (isContextual() == numericShaper.isContextual() && this.rangeSet.equals(Range.maskToRangeSet(numericShaper.mask)) && this.shapingRange == Range.indexToRange(numericShaper.key)) {
                    return true;
                } else {
                    return false;
                }
            } else if (numericShaper.rangeSet != null) {
                Set r1 = Range.maskToRangeSet(this.mask);
                Range r3 = Range.indexToRange(this.key);
                if (isContextual() == numericShaper.isContextual() && r1.equals(numericShaper.rangeSet) && r3 == numericShaper.shapingRange) {
                    return true;
                }
                return false;
            } else if (numericShaper.mask == this.mask && numericShaper.key == this.key) {
                return true;
            } else {
                return false;
            }
        } catch (ClassCastException unused) {
            return false;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("[contextual:");
        sb.append(isContextual());
        if (isContextual()) {
            sb.append(", context:");
            Range range = this.shapingRange;
            if (range == null) {
                range = Range.values()[this.key];
            }
            sb.append((Object) range);
        }
        if (this.rangeSet == null) {
            sb.append(", range(s): ");
            boolean z = true;
            for (int i = 0; i < 19; i++) {
                if ((this.mask & (1 << i)) != 0) {
                    if (z) {
                        z = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append((Object) Range.values()[i]);
                }
            }
        } else {
            sb.append(", range set: ");
            sb.append((Object) this.rangeSet);
        }
        sb.append(']');
        return sb.toString();
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0010  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0019 A[SYNTHETIC] */
    private static int search(int r3, int[] r4, int r5, int r6) {
        /*
            int r0 = getHighBit(r6)
            r1 = 1
            int r0 = r1 << r0
            int r6 = r6 - r0
            int r6 = r6 + r5
            r2 = r4[r6]
            if (r3 < r2) goto L_0x000e
        L_0x000d:
            r5 = r6
        L_0x000e:
            if (r0 <= r1) goto L_0x0019
            int r0 = r0 >> 1
            int r6 = r5 + r0
            r2 = r4[r6]
            if (r3 < r2) goto L_0x000e
            goto L_0x000d
        L_0x0019:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.font.NumericShaper.search(int, int[], int, int):int");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int r0;
        Range range = this.shapingRange;
        if (range != null && (r0 = Range.toRangeIndex(range)) >= 0) {
            this.key = r0;
        }
        Set<Range> set = this.rangeSet;
        if (set != null) {
            this.mask = Range.toRangeMask(set) | this.mask;
        }
        objectOutputStream.defaultWriteObject();
    }
}
