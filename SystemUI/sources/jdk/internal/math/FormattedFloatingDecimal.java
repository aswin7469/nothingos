package jdk.internal.math;

import java.util.Arrays;
import jdk.internal.math.FloatingDecimal;

public class FormattedFloatingDecimal {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final ThreadLocal<Object> threadLocalCharBuffer = new ThreadLocal<Object>() {
        /* access modifiers changed from: protected */
        public Object initialValue() {
            return new char[20];
        }
    };
    private int decExponentRounded;
    private char[] exponent;
    private char[] mantissa;

    public enum Form {
        SCIENTIFIC,
        COMPATIBLE,
        DECIMAL_FLOAT,
        GENERAL
    }

    public static FormattedFloatingDecimal valueOf(double d, int i, Form form) {
        return new FormattedFloatingDecimal(i, form, FloatingDecimal.getBinaryToASCIIConverter(d, form == Form.COMPATIBLE));
    }

    private static char[] getBuffer() {
        return (char[]) threadLocalCharBuffer.get();
    }

    private FormattedFloatingDecimal(int i, Form form, FloatingDecimal.BinaryToASCIIConverter binaryToASCIIConverter) {
        if (binaryToASCIIConverter.isExceptional()) {
            this.mantissa = binaryToASCIIConverter.toJavaFormatString().toCharArray();
            this.exponent = null;
            return;
        }
        char[] buffer = getBuffer();
        int digits = binaryToASCIIConverter.getDigits(buffer);
        int decimalExponent = binaryToASCIIConverter.getDecimalExponent();
        boolean isNegative = binaryToASCIIConverter.isNegative();
        int i2 = C45892.$SwitchMap$jdk$internal$math$FormattedFloatingDecimal$Form[form.ordinal()];
        if (i2 == 1) {
            this.decExponentRounded = decimalExponent;
            fillCompatible(i, buffer, digits, decimalExponent, isNegative);
        } else if (i2 == 2) {
            int applyPrecision = applyPrecision(decimalExponent, buffer, digits, decimalExponent + i);
            fillDecimal(i, buffer, digits, applyPrecision, isNegative);
            this.decExponentRounded = applyPrecision;
        } else if (i2 == 3) {
            int applyPrecision2 = applyPrecision(decimalExponent, buffer, digits, i + 1);
            fillScientific(i, buffer, digits, applyPrecision2, isNegative);
            this.decExponentRounded = applyPrecision2;
        } else if (i2 == 4) {
            int applyPrecision3 = applyPrecision(decimalExponent, buffer, digits, i);
            int i3 = applyPrecision3 - 1;
            if (i3 < -4 || i3 >= i) {
                fillScientific(i - 1, buffer, digits, applyPrecision3, isNegative);
            } else {
                fillDecimal(i - applyPrecision3, buffer, digits, applyPrecision3, isNegative);
            }
            this.decExponentRounded = applyPrecision3;
        }
    }

    /* renamed from: jdk.internal.math.FormattedFloatingDecimal$2 */
    static /* synthetic */ class C45892 {
        static final /* synthetic */ int[] $SwitchMap$jdk$internal$math$FormattedFloatingDecimal$Form;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                jdk.internal.math.FormattedFloatingDecimal$Form[] r0 = jdk.internal.math.FormattedFloatingDecimal.Form.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$jdk$internal$math$FormattedFloatingDecimal$Form = r0
                jdk.internal.math.FormattedFloatingDecimal$Form r1 = jdk.internal.math.FormattedFloatingDecimal.Form.COMPATIBLE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$jdk$internal$math$FormattedFloatingDecimal$Form     // Catch:{ NoSuchFieldError -> 0x001d }
                jdk.internal.math.FormattedFloatingDecimal$Form r1 = jdk.internal.math.FormattedFloatingDecimal.Form.DECIMAL_FLOAT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$jdk$internal$math$FormattedFloatingDecimal$Form     // Catch:{ NoSuchFieldError -> 0x0028 }
                jdk.internal.math.FormattedFloatingDecimal$Form r1 = jdk.internal.math.FormattedFloatingDecimal.Form.SCIENTIFIC     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$jdk$internal$math$FormattedFloatingDecimal$Form     // Catch:{ NoSuchFieldError -> 0x0033 }
                jdk.internal.math.FormattedFloatingDecimal$Form r1 = jdk.internal.math.FormattedFloatingDecimal.Form.GENERAL     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: jdk.internal.math.FormattedFloatingDecimal.C45892.<clinit>():void");
        }
    }

    public int getExponentRounded() {
        return this.decExponentRounded - 1;
    }

    public char[] getMantissa() {
        return this.mantissa;
    }

    public char[] getExponent() {
        return this.exponent;
    }

    private static int applyPrecision(int i, char[] cArr, int i2, int i3) {
        if (i3 < i2 && i3 >= 0) {
            if (i3 == 0) {
                if (cArr[0] >= '5') {
                    cArr[0] = '1';
                    Arrays.fill(cArr, 1, i2, '0');
                } else {
                    Arrays.fill(cArr, 0, i2, '0');
                    return i;
                }
            } else if (cArr[i3] >= '5') {
                int i4 = i3 - 1;
                char c = cArr[i4];
                if (c == '9') {
                    while (c == '9' && i4 > 0) {
                        i4--;
                        c = cArr[i4];
                    }
                    if (c == '9') {
                        cArr[0] = '1';
                        Arrays.fill(cArr, 1, i2, '0');
                    }
                }
                cArr[i4] = (char) (c + 1);
                Arrays.fill(cArr, i4 + 1, i2, '0');
            } else {
                Arrays.fill(cArr, i3, i2, '0');
            }
            return i + 1;
        }
        return i;
    }

    /* JADX WARNING: type inference failed for: r12v0, types: [int, boolean] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void fillCompatible(int r8, char[] r9, int r10, int r11, boolean r12) {
        /*
            r7 = this;
            r0 = 46
            r1 = 2
            r2 = 0
            r3 = 48
            r4 = 1
            if (r11 <= 0) goto L_0x005f
            r5 = 8
            if (r11 >= r5) goto L_0x005f
            if (r10 >= r11) goto L_0x002c
            int r11 = r11 - r10
            int r8 = r10 + r11
            int r8 = r8 + r1
            char[] r8 = create(r12, r8)
            r7.mantissa = r8
            java.lang.System.arraycopy((java.lang.Object) r9, (int) r2, (java.lang.Object) r8, (int) r12, (int) r10)
            char[] r8 = r7.mantissa
            int r12 = r12 + r10
            int r11 = r11 + r12
            java.util.Arrays.fill((char[]) r8, (int) r12, (int) r11, (char) r3)
            char[] r7 = r7.mantissa
            r7[r11] = r0
            int r11 = r11 + r4
            r7[r11] = r3
            goto L_0x0130
        L_0x002c:
            if (r11 >= r10) goto L_0x004a
            int r10 = r10 - r11
            int r8 = java.lang.Math.min((int) r10, (int) r8)
            int r10 = r11 + 1
            int r10 = r10 + r8
            char[] r10 = create(r12, r10)
            r7.mantissa = r10
            java.lang.System.arraycopy((java.lang.Object) r9, (int) r2, (java.lang.Object) r10, (int) r12, (int) r11)
            char[] r7 = r7.mantissa
            int r12 = r12 + r11
            r7[r12] = r0
            int r12 = r12 + r4
            java.lang.System.arraycopy((java.lang.Object) r9, (int) r11, (java.lang.Object) r7, (int) r12, (int) r8)
            goto L_0x0130
        L_0x004a:
            int r8 = r10 + 2
            char[] r8 = create(r12, r8)
            r7.mantissa = r8
            java.lang.System.arraycopy((java.lang.Object) r9, (int) r2, (java.lang.Object) r8, (int) r12, (int) r10)
            char[] r7 = r7.mantissa
            int r12 = r12 + r10
            r7[r12] = r0
            int r12 = r12 + r4
            r7[r12] = r3
            goto L_0x0130
        L_0x005f:
            if (r11 > 0) goto L_0x00b5
            r5 = -3
            if (r11 <= r5) goto L_0x00b5
            int r5 = -r11
            int r5 = java.lang.Math.min((int) r5, (int) r8)
            int r5 = java.lang.Math.max((int) r2, (int) r5)
            int r8 = r8 + r11
            int r8 = java.lang.Math.min((int) r10, (int) r8)
            int r8 = java.lang.Math.max((int) r2, (int) r8)
            if (r5 <= 0) goto L_0x0095
            int r10 = r5 + 2
            int r10 = r10 + r8
            char[] r10 = create(r12, r10)
            r7.mantissa = r10
            r10[r12] = r3
            int r11 = r12 + 1
            r10[r11] = r0
            int r12 = r12 + r1
            int r5 = r5 + r12
            java.util.Arrays.fill((char[]) r10, (int) r12, (int) r5, (char) r3)
            if (r8 <= 0) goto L_0x0130
            char[] r7 = r7.mantissa
            java.lang.System.arraycopy((java.lang.Object) r9, (int) r2, (java.lang.Object) r7, (int) r5, (int) r8)
            goto L_0x0130
        L_0x0095:
            if (r8 <= 0) goto L_0x00ab
            int r5 = r5 + r1
            int r5 = r5 + r8
            char[] r10 = create(r12, r5)
            r7.mantissa = r10
            r10[r12] = r3
            int r7 = r12 + 1
            r10[r7] = r0
            int r12 = r12 + r1
            java.lang.System.arraycopy((java.lang.Object) r9, (int) r2, (java.lang.Object) r10, (int) r12, (int) r8)
            goto L_0x0130
        L_0x00ab:
            char[] r8 = create(r12, r4)
            r7.mantissa = r8
            r8[r12] = r3
            goto L_0x0130
        L_0x00b5:
            r8 = 3
            if (r10 <= r4) goto L_0x00ce
            int r5 = r10 + 1
            char[] r5 = create(r12, r5)
            r7.mantissa = r5
            char r6 = r9[r2]
            r5[r12] = r6
            int r6 = r12 + 1
            r5[r6] = r0
            int r12 = r12 + r1
            int r10 = r10 - r4
            java.lang.System.arraycopy((java.lang.Object) r9, (int) r4, (java.lang.Object) r5, (int) r12, (int) r10)
            goto L_0x00df
        L_0x00ce:
            char[] r10 = create(r12, r8)
            r7.mantissa = r10
            char r9 = r9[r2]
            r10[r12] = r9
            int r9 = r12 + 1
            r10[r9] = r0
            int r12 = r12 + r1
            r10[r12] = r3
        L_0x00df:
            if (r11 > 0) goto L_0x00e3
            r9 = r4
            goto L_0x00e4
        L_0x00e3:
            r9 = r2
        L_0x00e4:
            if (r9 == 0) goto L_0x00ea
            int r10 = -r11
            int r10 = r10 + r4
            r2 = r4
            goto L_0x00ec
        L_0x00ea:
            int r10 = r11 + -1
        L_0x00ec:
            r11 = 9
            if (r10 > r11) goto L_0x00fb
            char[] r8 = create(r9, r4)
            r7.exponent = r8
            int r10 = r10 + r3
            char r7 = (char) r10
            r8[r2] = r7
            goto L_0x0130
        L_0x00fb:
            r11 = 99
            if (r10 > r11) goto L_0x0113
            char[] r8 = create(r9, r1)
            r7.exponent = r8
            int r7 = r10 / 10
            int r7 = r7 + r3
            char r7 = (char) r7
            r8[r2] = r7
            int r2 = r2 + r4
            int r10 = r10 % 10
            int r10 = r10 + r3
            char r7 = (char) r10
            r8[r2] = r7
            goto L_0x0130
        L_0x0113:
            char[] r8 = create(r9, r8)
            r7.exponent = r8
            int r7 = r10 / 100
            int r7 = r7 + r3
            char r7 = (char) r7
            r8[r2] = r7
            int r10 = r10 % 100
            int r7 = r2 + 1
            int r9 = r10 / 10
            int r9 = r9 + r3
            char r9 = (char) r9
            r8[r7] = r9
            int r2 = r2 + r1
            int r10 = r10 % 10
            int r10 = r10 + r3
            char r7 = (char) r10
            r8[r2] = r7
        L_0x0130:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.math.FormattedFloatingDecimal.fillCompatible(int, char[], int, int, boolean):void");
    }

    private static char[] create(boolean z, int i) {
        if (!z) {
            return new char[i];
        }
        char[] cArr = new char[(i + 1)];
        cArr[0] = '-';
        return cArr;
    }

    /* JADX WARNING: type inference failed for: r10v0, types: [int, boolean] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void fillDecimal(int r6, char[] r7, int r8, int r9, boolean r10) {
        /*
            r5 = this;
            r0 = 46
            r1 = 1
            r2 = 48
            r3 = 0
            if (r9 <= 0) goto L_0x003d
            if (r8 >= r9) goto L_0x001c
            char[] r6 = create(r10, r9)
            r5.mantissa = r6
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r3, (java.lang.Object) r6, (int) r10, (int) r8)
            char[] r5 = r5.mantissa
            int r8 = r8 + r10
            int r10 = r10 + r9
            java.util.Arrays.fill((char[]) r5, (int) r8, (int) r10, (char) r2)
            goto L_0x008f
        L_0x001c:
            int r8 = r8 - r9
            int r6 = java.lang.Math.min((int) r8, (int) r6)
            if (r6 <= 0) goto L_0x0026
            int r8 = r6 + 1
            goto L_0x0027
        L_0x0026:
            r8 = r3
        L_0x0027:
            int r8 = r8 + r9
            char[] r8 = create(r10, r8)
            r5.mantissa = r8
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r3, (java.lang.Object) r8, (int) r10, (int) r9)
            if (r6 <= 0) goto L_0x008f
            char[] r5 = r5.mantissa
            int r10 = r10 + r9
            r5[r10] = r0
            int r10 = r10 + r1
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r9, (java.lang.Object) r5, (int) r10, (int) r6)
            goto L_0x008f
        L_0x003d:
            if (r9 > 0) goto L_0x008f
            int r4 = -r9
            int r4 = java.lang.Math.min((int) r4, (int) r6)
            int r4 = java.lang.Math.max((int) r3, (int) r4)
            int r6 = r6 + r9
            int r6 = java.lang.Math.min((int) r8, (int) r6)
            int r6 = java.lang.Math.max((int) r3, (int) r6)
            if (r4 <= 0) goto L_0x0070
            int r8 = r4 + 2
            int r8 = r8 + r6
            char[] r8 = create(r10, r8)
            r5.mantissa = r8
            r8[r10] = r2
            int r9 = r10 + 1
            r8[r9] = r0
            int r10 = r10 + 2
            int r4 = r4 + r10
            java.util.Arrays.fill((char[]) r8, (int) r10, (int) r4, (char) r2)
            if (r6 <= 0) goto L_0x008f
            char[] r5 = r5.mantissa
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r3, (java.lang.Object) r5, (int) r4, (int) r6)
            goto L_0x008f
        L_0x0070:
            if (r6 <= 0) goto L_0x0087
            int r4 = r4 + 2
            int r4 = r4 + r6
            char[] r8 = create(r10, r4)
            r5.mantissa = r8
            r8[r10] = r2
            int r5 = r10 + 1
            r8[r5] = r0
            int r10 = r10 + 2
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r3, (java.lang.Object) r8, (int) r10, (int) r6)
            goto L_0x008f
        L_0x0087:
            char[] r6 = create(r10, r1)
            r5.mantissa = r6
            r6[r10] = r2
        L_0x008f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.math.FormattedFloatingDecimal.fillDecimal(int, char[], int, int, boolean):void");
    }

    /* JADX WARNING: type inference failed for: r10v0, types: [int, boolean] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void fillScientific(int r6, char[] r7, int r8, int r9, boolean r10) {
        /*
            r5 = this;
            r0 = 1
            int r8 = r8 - r0
            int r6 = java.lang.Math.min((int) r8, (int) r6)
            r8 = 0
            int r6 = java.lang.Math.max((int) r8, (int) r6)
            r1 = 2
            if (r6 <= 0) goto L_0x0025
            int r2 = r6 + 2
            char[] r2 = create(r10, r2)
            r5.mantissa = r2
            char r3 = r7[r8]
            r2[r10] = r3
            int r3 = r10 + 1
            r4 = 46
            r2[r3] = r4
            int r10 = r10 + r1
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r0, (java.lang.Object) r2, (int) r10, (int) r6)
            goto L_0x002f
        L_0x0025:
            char[] r6 = create(r10, r0)
            r5.mantissa = r6
            char r7 = r7[r8]
            r6[r10] = r7
        L_0x002f:
            if (r9 > 0) goto L_0x0036
            int r6 = -r9
            int r6 = r6 + r0
            r7 = 45
            goto L_0x003a
        L_0x0036:
            int r6 = r9 + -1
            r7 = 43
        L_0x003a:
            r9 = 9
            r10 = 3
            r2 = 48
            if (r6 > r9) goto L_0x004e
            char[] r9 = new char[r10]
            r9[r8] = r7
            r9[r0] = r2
            int r6 = r6 + r2
            char r6 = (char) r6
            r9[r1] = r6
            r5.exponent = r9
            goto L_0x0080
        L_0x004e:
            r9 = 99
            if (r6 > r9) goto L_0x0065
            char[] r9 = new char[r10]
            r9[r8] = r7
            int r7 = r6 / 10
            int r7 = r7 + r2
            char r7 = (char) r7
            r9[r0] = r7
            int r6 = r6 % 10
            int r6 = r6 + r2
            char r6 = (char) r6
            r9[r1] = r6
            r5.exponent = r9
            goto L_0x0080
        L_0x0065:
            int r9 = r6 / 100
            int r9 = r9 + r2
            char r9 = (char) r9
            int r6 = r6 % 100
            r3 = 4
            char[] r3 = new char[r3]
            r3[r8] = r7
            r3[r0] = r9
            int r7 = r6 / 10
            int r7 = r7 + r2
            char r7 = (char) r7
            r3[r1] = r7
            int r6 = r6 % 10
            int r6 = r6 + r2
            char r6 = (char) r6
            r3[r10] = r6
            r5.exponent = r3
        L_0x0080:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.math.FormattedFloatingDecimal.fillScientific(int, char[], int, int, boolean):void");
    }
}
