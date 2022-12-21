package java.text;

import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.util.Arrays;
import jdk.internal.math.DoubleConsts;
import kotlin.text.Typography;

public class ChoiceFormat extends NumberFormat {
    static final long EXPONENT = 9218868437227405312L;
    static final long POSITIVEINFINITY = 9218868437227405312L;
    static final long SIGN = Long.MIN_VALUE;
    private static final long serialVersionUID = 1795184449645032964L;
    private String[] choiceFormats;
    private double[] choiceLimits;

    public void applyPattern(String str) {
        int i;
        String str2 = str;
        StringBuffer[] stringBufferArr = new StringBuffer[2];
        for (int i2 = 0; i2 < 2; i2++) {
            stringBufferArr[i2] = new StringBuffer();
        }
        double[] dArr = new double[30];
        String[] strArr = new String[30];
        double d = 0.0d;
        double d2 = Double.NaN;
        int i3 = 0;
        char c = 0;
        int i4 = 0;
        boolean z = false;
        while (i3 < str.length()) {
            char charAt = str2.charAt(i3);
            if (charAt == '\'') {
                int i5 = i3 + 1;
                if (i5 >= str.length() || str2.charAt(i5) != charAt) {
                    z = !z;
                } else {
                    stringBufferArr[c].append(charAt);
                    i3 = i5;
                }
            } else if (z) {
                stringBufferArr[c].append(charAt);
            } else if (charAt == '<' || charAt == '#' || charAt == 8804) {
                if (stringBufferArr[0].length() != 0) {
                    String stringBuffer = stringBufferArr[0].toString();
                    if (stringBuffer.equals("∞")) {
                        d = Double.POSITIVE_INFINITY;
                    } else if (stringBuffer.equals("-∞")) {
                        d = Double.NEGATIVE_INFINITY;
                    } else {
                        d = Double.parseDouble(stringBuffer);
                    }
                    if (!(charAt != '<' || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY)) {
                        d = nextDouble(d);
                    }
                    if (d > d2) {
                        stringBufferArr[0].setLength(0);
                        i = 1;
                        c = 1;
                        i3 += i;
                    } else {
                        throw new IllegalArgumentException("Incorrect order of intervals, must be in ascending order");
                    }
                } else {
                    throw new IllegalArgumentException("Each interval must contain a number before a format");
                }
            } else if (charAt == '|') {
                if (i4 == dArr.length) {
                    dArr = doubleArraySize(dArr);
                    strArr = doubleArraySize(strArr);
                }
                dArr[i4] = d;
                strArr[i4] = stringBufferArr[1].toString();
                i4++;
                stringBufferArr[1].setLength(0);
                d2 = d;
                c = 0;
            } else {
                stringBufferArr[c].append(charAt);
            }
            i = 1;
            i3 += i;
        }
        if (c == 1) {
            if (i4 == dArr.length) {
                double[] doubleArraySize = doubleArraySize(dArr);
                strArr = doubleArraySize(strArr);
                dArr = doubleArraySize;
            }
            dArr[i4] = d;
            strArr[i4] = stringBufferArr[1].toString();
            i4++;
        }
        double[] dArr2 = new double[i4];
        this.choiceLimits = dArr2;
        System.arraycopy((Object) dArr, 0, (Object) dArr2, 0, i4);
        String[] strArr2 = new String[i4];
        this.choiceFormats = strArr2;
        System.arraycopy((Object) strArr, 0, (Object) strArr2, 0, i4);
    }

    public String toPattern() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.choiceLimits.length; i++) {
            if (i != 0) {
                sb.append('|');
            }
            double previousDouble = previousDouble(this.choiceLimits[i]);
            if (Math.abs(Math.IEEEremainder(this.choiceLimits[i], 1.0d)) < Math.abs(Math.IEEEremainder(previousDouble, 1.0d))) {
                sb.append(this.choiceLimits[i]);
                sb.append('#');
            } else {
                double d = this.choiceLimits[i];
                if (d == Double.POSITIVE_INFINITY) {
                    sb.append("∞");
                } else if (d == Double.NEGATIVE_INFINITY) {
                    sb.append("-∞");
                } else {
                    sb.append(previousDouble);
                }
                sb.append((char) Typography.less);
            }
            String str = this.choiceFormats[i];
            boolean z = str.indexOf(60) >= 0 || str.indexOf(35) >= 0 || str.indexOf(8804) >= 0 || str.indexOf(124) >= 0;
            if (z) {
                sb.append('\'');
            }
            if (str.indexOf(39) < 0) {
                sb.append(str);
            } else {
                for (int i2 = 0; i2 < str.length(); i2++) {
                    char charAt = str.charAt(i2);
                    sb.append(charAt);
                    if (charAt == '\'') {
                        sb.append(charAt);
                    }
                }
            }
            if (z) {
                sb.append('\'');
            }
        }
        return sb.toString();
    }

    public ChoiceFormat(String str) {
        applyPattern(str);
    }

    public ChoiceFormat(double[] dArr, String[] strArr) {
        setChoices(dArr, strArr);
    }

    public void setChoices(double[] dArr, String[] strArr) {
        if (dArr.length == strArr.length) {
            this.choiceLimits = Arrays.copyOf(dArr, dArr.length);
            this.choiceFormats = (String[]) Arrays.copyOf((T[]) strArr, strArr.length);
            return;
        }
        throw new IllegalArgumentException("Array and limit arrays must be of the same length.");
    }

    public double[] getLimits() {
        double[] dArr = this.choiceLimits;
        return Arrays.copyOf(dArr, dArr.length);
    }

    public Object[] getFormats() {
        String[] strArr = this.choiceFormats;
        return Arrays.copyOf((T[]) strArr, strArr.length);
    }

    public StringBuffer format(long j, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        return format((double) j, stringBuffer, fieldPosition);
    }

    public StringBuffer format(double d, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        int i = 0;
        int i2 = 0;
        while (true) {
            double[] dArr = this.choiceLimits;
            if (i2 >= dArr.length || d < dArr[i2]) {
                int i3 = i2 - 1;
            } else {
                i2++;
            }
        }
        int i32 = i2 - 1;
        if (i32 >= 0) {
            i = i32;
        }
        return stringBuffer.append(this.choiceFormats[i]);
    }

    public Number parse(String str, ParsePosition parsePosition) {
        int i = parsePosition.index;
        double d = Double.NaN;
        int i2 = i;
        int i3 = 0;
        while (true) {
            String[] strArr = this.choiceFormats;
            if (i3 >= strArr.length) {
                break;
            }
            String str2 = strArr[i3];
            if (str.regionMatches(i, str2, 0, str2.length())) {
                parsePosition.index = str2.length() + i;
                double d2 = this.choiceLimits[i3];
                if (parsePosition.index <= i2) {
                    continue;
                } else {
                    i2 = parsePosition.index;
                    if (i2 == str.length()) {
                        d = d2;
                        break;
                    }
                    d = d2;
                }
            }
            i3++;
        }
        parsePosition.index = i2;
        if (parsePosition.index == i) {
            parsePosition.errorIndex = i2;
        }
        return Double.valueOf(d);
    }

    public static final double nextDouble(double d) {
        return nextDouble(d, true);
    }

    public static final double previousDouble(double d) {
        return nextDouble(d, false);
    }

    public Object clone() {
        ChoiceFormat choiceFormat = (ChoiceFormat) super.clone();
        choiceFormat.choiceLimits = (double[]) this.choiceLimits.clone();
        choiceFormat.choiceFormats = (String[]) this.choiceFormats.clone();
        return choiceFormat;
    }

    public int hashCode() {
        int length = this.choiceLimits.length;
        String[] strArr = this.choiceFormats;
        return strArr.length > 0 ? length ^ strArr[strArr.length - 1].hashCode() : length;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ChoiceFormat choiceFormat = (ChoiceFormat) obj;
        if (!Arrays.equals(this.choiceLimits, choiceFormat.choiceLimits) || !Arrays.equals((Object[]) this.choiceFormats, (Object[]) choiceFormat.choiceFormats)) {
            return false;
        }
        return true;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.choiceLimits.length != this.choiceFormats.length) {
            throw new InvalidObjectException("limits and format arrays of different length.");
        }
    }

    public static double nextDouble(double d, boolean z) {
        if (Double.isNaN(d)) {
            return d;
        }
        if (d == 0.0d) {
            double longBitsToDouble = Double.longBitsToDouble(1);
            return z ? longBitsToDouble : -longBitsToDouble;
        }
        long doubleToLongBits = Double.doubleToLongBits(d);
        long j = Long.MAX_VALUE & doubleToLongBits;
        if ((doubleToLongBits > 0) != z) {
            j--;
        } else if (j != DoubleConsts.EXP_BIT_MASK) {
            j++;
        }
        return Double.longBitsToDouble((doubleToLongBits & Long.MIN_VALUE) | j);
    }

    private static double[] doubleArraySize(double[] dArr) {
        int length = dArr.length;
        double[] dArr2 = new double[(length * 2)];
        System.arraycopy((Object) dArr, 0, (Object) dArr2, 0, length);
        return dArr2;
    }

    private String[] doubleArraySize(String[] strArr) {
        int length = strArr.length;
        String[] strArr2 = new String[(length * 2)];
        System.arraycopy((Object) strArr, 0, (Object) strArr2, 0, length);
        return strArr2;
    }
}
