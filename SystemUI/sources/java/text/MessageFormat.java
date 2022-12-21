package java.text;

import java.p026io.InvalidObjectException;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import sun.security.x509.InvalidityDateExtension;

public class MessageFormat extends Format {
    private static final int[] DATE_TIME_MODIFIERS = {2, 3, 2, 1, 0};
    private static final String[] DATE_TIME_MODIFIER_KEYWORDS = {"", "short", "medium", "long", "full"};
    private static final int INITIAL_FORMATS = 10;
    private static final int MODIFIER_CURRENCY = 1;
    private static final int MODIFIER_DEFAULT = 0;
    private static final int MODIFIER_FULL = 4;
    private static final int MODIFIER_INTEGER = 3;
    private static final int MODIFIER_LONG = 3;
    private static final int MODIFIER_MEDIUM = 2;
    private static final int MODIFIER_PERCENT = 2;
    private static final int MODIFIER_SHORT = 1;
    private static final String[] NUMBER_MODIFIER_KEYWORDS = {"", "currency", "percent", "integer"};
    private static final int SEG_INDEX = 1;
    private static final int SEG_MODIFIER = 3;
    private static final int SEG_RAW = 0;
    private static final int SEG_TYPE = 2;
    private static final int TYPE_CHOICE = 4;
    private static final int TYPE_DATE = 2;
    private static final String[] TYPE_KEYWORDS = {"", "number", InvalidityDateExtension.DATE, "time", "choice"};
    private static final int TYPE_NULL = 0;
    private static final int TYPE_NUMBER = 1;
    private static final int TYPE_TIME = 3;
    private static final long serialVersionUID = 6479157306784022952L;
    private int[] argumentNumbers;
    private Format[] formats;
    private Locale locale;
    private int maxOffset;
    private int[] offsets;
    private String pattern;

    public MessageFormat(String str) {
        this.pattern = "";
        this.formats = new Format[10];
        this.offsets = new int[10];
        this.argumentNumbers = new int[10];
        this.maxOffset = -1;
        this.locale = Locale.getDefault(Locale.Category.FORMAT);
        applyPattern(str);
    }

    public MessageFormat(String str, Locale locale2) {
        this.pattern = "";
        this.formats = new Format[10];
        this.offsets = new int[10];
        this.argumentNumbers = new int[10];
        this.maxOffset = -1;
        this.locale = locale2;
        applyPattern(str);
    }

    public void setLocale(Locale locale2) {
        this.locale = locale2;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void applyPattern(String str) {
        StringBuilder[] sbArr = new StringBuilder[4];
        sbArr[0] = new StringBuilder();
        this.maxOffset = -1;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        boolean z = false;
        int i4 = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (i3 == 0) {
                if (charAt == '\'') {
                    int i5 = i + 1;
                    if (i5 >= str.length() || str.charAt(i5) != '\'') {
                        z = !z;
                    } else {
                        sbArr[i3].append(charAt);
                        i = i5;
                    }
                } else if (charAt != '{' || z) {
                    sbArr[i3].append(charAt);
                } else {
                    if (sbArr[1] == null) {
                        sbArr[1] = new StringBuilder();
                    }
                    i3 = 1;
                }
            } else if (z) {
                sbArr[i3].append(charAt);
                if (charAt == '\'') {
                    z = false;
                }
            } else if (charAt != ' ') {
                if (charAt == '\'') {
                    z = true;
                } else if (charAt != ',') {
                    if (charAt == '{') {
                        i2++;
                        sbArr[i3].append(charAt);
                    } else if (charAt == '}') {
                        if (i2 == 0) {
                            makeFormat(i, i4, sbArr);
                            i4++;
                            sbArr[1] = null;
                            sbArr[2] = null;
                            sbArr[3] = null;
                            i3 = 0;
                        } else {
                            i2--;
                            sbArr[i3].append(charAt);
                        }
                    }
                } else if (i3 < 3) {
                    i3++;
                    if (sbArr[i3] == null) {
                        sbArr[i3] = new StringBuilder();
                    }
                } else {
                    sbArr[i3].append(charAt);
                }
                sbArr[i3].append(charAt);
            } else if (i3 != 2 || sbArr[2].length() > 0) {
                sbArr[i3].append(charAt);
            }
            i++;
        }
        if (i2 != 0 || i3 == 0) {
            this.pattern = sbArr[0].toString();
        } else {
            this.maxOffset = -1;
            throw new IllegalArgumentException("Unmatched braces in the pattern.");
        }
    }

    public String toPattern() {
        int[] iArr;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (int i2 = 0; i2 <= this.maxOffset; i2++) {
            copyAndFixQuotes(this.pattern, i, this.offsets[i2], sb);
            i = this.offsets[i2];
            sb.append('{');
            sb.append(this.argumentNumbers[i2]);
            Format format = this.formats[i2];
            if (format != null) {
                if (format instanceof NumberFormat) {
                    if (format.equals(NumberFormat.getInstance(this.locale))) {
                        sb.append(",number");
                    } else if (format.equals(NumberFormat.getCurrencyInstance(this.locale))) {
                        sb.append(",number,currency");
                    } else if (format.equals(NumberFormat.getPercentInstance(this.locale))) {
                        sb.append(",number,percent");
                    } else if (format.equals(NumberFormat.getIntegerInstance(this.locale))) {
                        sb.append(",number,integer");
                    } else if (format instanceof DecimalFormat) {
                        sb.append(",number,");
                        sb.append(((DecimalFormat) format).toPattern());
                    } else if (format instanceof ChoiceFormat) {
                        sb.append(",choice,");
                        sb.append(((ChoiceFormat) format).toPattern());
                    }
                } else if (format instanceof DateFormat) {
                    int i3 = 0;
                    while (true) {
                        iArr = DATE_TIME_MODIFIERS;
                        if (i3 >= iArr.length) {
                            break;
                        } else if (format.equals(DateFormat.getDateInstance(iArr[i3], this.locale))) {
                            sb.append(",date");
                            break;
                        } else if (format.equals(DateFormat.getTimeInstance(iArr[i3], this.locale))) {
                            sb.append(",time");
                            break;
                        } else {
                            i3++;
                        }
                    }
                    if (i3 >= iArr.length) {
                        if (format instanceof SimpleDateFormat) {
                            sb.append(",date,");
                            sb.append(((SimpleDateFormat) format).toPattern());
                        }
                    } else if (i3 != 0) {
                        sb.append(',');
                        sb.append(DATE_TIME_MODIFIER_KEYWORDS[i3]);
                    }
                }
            }
            sb.append('}');
        }
        String str = this.pattern;
        copyAndFixQuotes(str, i, str.length(), sb);
        return sb.toString();
    }

    public void setFormatsByArgumentIndex(Format[] formatArr) {
        for (int i = 0; i <= this.maxOffset; i++) {
            int i2 = this.argumentNumbers[i];
            if (i2 < formatArr.length) {
                this.formats[i] = formatArr[i2];
            }
        }
    }

    public void setFormats(Format[] formatArr) {
        int length = formatArr.length;
        int i = this.maxOffset;
        if (length > i + 1) {
            length = i + 1;
        }
        for (int i2 = 0; i2 < length; i2++) {
            this.formats[i2] = formatArr[i2];
        }
    }

    public void setFormatByArgumentIndex(int i, Format format) {
        for (int i2 = 0; i2 <= this.maxOffset; i2++) {
            if (this.argumentNumbers[i2] == i) {
                this.formats[i2] = format;
            }
        }
    }

    public void setFormat(int i, Format format) {
        if (i <= this.maxOffset) {
            this.formats[i] = format;
            return;
        }
        throw new ArrayIndexOutOfBoundsException(i);
    }

    public Format[] getFormatsByArgumentIndex() {
        int i = -1;
        for (int i2 = 0; i2 <= this.maxOffset; i2++) {
            int i3 = this.argumentNumbers[i2];
            if (i3 > i) {
                i = i3;
            }
        }
        Format[] formatArr = new Format[(i + 1)];
        for (int i4 = 0; i4 <= this.maxOffset; i4++) {
            formatArr[this.argumentNumbers[i4]] = this.formats[i4];
        }
        return formatArr;
    }

    public Format[] getFormats() {
        int i = this.maxOffset;
        Format[] formatArr = new Format[(i + 1)];
        System.arraycopy((Object) this.formats, 0, (Object) formatArr, 0, i + 1);
        return formatArr;
    }

    public final StringBuffer format(Object[] objArr, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        return subformat(objArr, stringBuffer, fieldPosition, (List<AttributedCharacterIterator>) null);
    }

    public static String format(String str, Object... objArr) {
        return new MessageFormat(str).format(objArr);
    }

    public final StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        return subformat((Object[]) obj, stringBuffer, fieldPosition, (List<AttributedCharacterIterator>) null);
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList arrayList = new ArrayList();
        if (obj != null) {
            subformat((Object[]) obj, stringBuffer, (FieldPosition) null, arrayList);
            if (arrayList.size() == 0) {
                return createAttributedCharacterIterator("");
            }
            return createAttributedCharacterIterator((AttributedCharacterIterator[]) arrayList.toArray(new AttributedCharacterIterator[arrayList.size()]));
        }
        throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
    }

    public Object[] parse(String str, ParsePosition parsePosition) {
        int i;
        int i2 = 0;
        if (str == null) {
            return new Object[0];
        }
        int i3 = -1;
        for (int i4 = 0; i4 <= this.maxOffset; i4++) {
            int i5 = this.argumentNumbers[i4];
            if (i5 > i3) {
                i3 = i5;
            }
        }
        Object[] objArr = new Object[(i3 + 1)];
        int i6 = parsePosition.index;
        ParsePosition parsePosition2 = new ParsePosition(0);
        int i7 = i6;
        int i8 = 0;
        while (i2 <= this.maxOffset) {
            int i9 = this.offsets[i2] - i8;
            if (i9 == 0 || this.pattern.regionMatches(i8, str, i7, i9)) {
                int i10 = i7 + i9;
                i8 += i9;
                if (this.formats[i2] == null) {
                    int length = i2 != this.maxOffset ? this.offsets[i2 + 1] : this.pattern.length();
                    if (i8 >= length) {
                        i = str.length();
                    } else {
                        i = str.indexOf(this.pattern.substring(i8, length), i10);
                    }
                    if (i < 0) {
                        parsePosition.errorIndex = i10;
                        return null;
                    }
                    if (!str.substring(i10, i).equals("{" + this.argumentNumbers[i2] + "}")) {
                        objArr[this.argumentNumbers[i2]] = str.substring(i10, i);
                    }
                    i7 = i;
                } else {
                    parsePosition2.index = i10;
                    objArr[this.argumentNumbers[i2]] = this.formats[i2].parseObject(str, parsePosition2);
                    if (parsePosition2.index == i10) {
                        parsePosition.errorIndex = i10;
                        return null;
                    }
                    i7 = parsePosition2.index;
                }
                i2++;
            } else {
                parsePosition.errorIndex = i7;
                return null;
            }
        }
        int length2 = this.pattern.length() - i8;
        if (length2 == 0 || this.pattern.regionMatches(i8, str, i7, length2)) {
            parsePosition.index = i7 + length2;
            return objArr;
        }
        parsePosition.errorIndex = i7;
        return null;
    }

    public Object[] parse(String str) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Object[] parse = parse(str, parsePosition);
        if (parsePosition.index != 0) {
            return parse;
        }
        throw new ParseException("MessageFormat parse error!", parsePosition.errorIndex);
    }

    public Object parseObject(String str, ParsePosition parsePosition) {
        return parse(str, parsePosition);
    }

    public Object clone() {
        MessageFormat messageFormat = (MessageFormat) super.clone();
        messageFormat.formats = (Format[]) this.formats.clone();
        int i = 0;
        while (true) {
            Format[] formatArr = this.formats;
            if (i < formatArr.length) {
                Format format = formatArr[i];
                if (format != null) {
                    messageFormat.formats[i] = (Format) format.clone();
                }
                i++;
            } else {
                messageFormat.offsets = (int[]) this.offsets.clone();
                messageFormat.argumentNumbers = (int[]) this.argumentNumbers.clone();
                return messageFormat;
            }
        }
    }

    public boolean equals(Object obj) {
        Locale locale2;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MessageFormat messageFormat = (MessageFormat) obj;
        if (this.maxOffset != messageFormat.maxOffset || !this.pattern.equals(messageFormat.pattern) || ((((locale2 = this.locale) == null || !locale2.equals(messageFormat.locale)) && (this.locale != null || messageFormat.locale != null)) || !Arrays.equals(this.offsets, messageFormat.offsets) || !Arrays.equals(this.argumentNumbers, messageFormat.argumentNumbers) || !Arrays.equals((Object[]) this.formats, (Object[]) messageFormat.formats))) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.pattern.hashCode();
    }

    public static class Field extends Format.Field {
        public static final Field ARGUMENT = new Field("message argument field");
        private static final long serialVersionUID = 7899943957617360810L;

        protected Field(String str) {
            super(str);
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws InvalidObjectException {
            if (getClass() == Field.class) {
                return ARGUMENT;
            }
            throw new InvalidObjectException("subclass didn't correctly implement readResolve");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00ca  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.StringBuffer subformat(java.lang.Object[] r10, java.lang.StringBuffer r11, java.text.FieldPosition r12, java.util.List<java.text.AttributedCharacterIterator> r13) {
        /*
            r9 = this;
            int r0 = r11.length()
            r1 = 0
            r2 = r1
        L_0x0006:
            int r3 = r9.maxOffset
            if (r1 > r3) goto L_0x0107
            java.lang.String r3 = r9.pattern
            int[] r4 = r9.offsets
            r4 = r4[r1]
            r11.append((java.lang.CharSequence) r3, (int) r2, (int) r4)
            int[] r2 = r9.offsets
            r2 = r2[r1]
            int[] r3 = r9.argumentNumbers
            r3 = r3[r1]
            r4 = 123(0x7b, float:1.72E-43)
            if (r10 == 0) goto L_0x00f6
            int r5 = r10.length
            if (r3 < r5) goto L_0x0024
            goto L_0x00f6
        L_0x0024:
            r5 = r10[r3]
            java.lang.String r6 = "null"
            r7 = 0
            if (r5 != 0) goto L_0x002e
        L_0x002c:
            r8 = r7
            goto L_0x0074
        L_0x002e:
            java.text.Format[] r8 = r9.formats
            r8 = r8[r1]
            if (r8 == 0) goto L_0x004c
            boolean r6 = r8 instanceof java.text.ChoiceFormat
            if (r6 == 0) goto L_0x004a
            java.lang.String r6 = r8.format(r5)
            int r4 = r6.indexOf((int) r4)
            if (r4 < 0) goto L_0x0074
            java.text.MessageFormat r8 = new java.text.MessageFormat
            java.util.Locale r4 = r9.locale
            r8.<init>(r6, r4)
            r5 = r10
        L_0x004a:
            r6 = r7
            goto L_0x0074
        L_0x004c:
            boolean r4 = r5 instanceof java.lang.Number
            if (r4 == 0) goto L_0x0057
            java.util.Locale r4 = r9.locale
            java.text.NumberFormat r8 = java.text.NumberFormat.getInstance(r4)
            goto L_0x004a
        L_0x0057:
            boolean r4 = r5 instanceof java.util.Date
            if (r4 == 0) goto L_0x0063
            java.util.Locale r4 = r9.locale
            r6 = 3
            java.text.DateFormat r8 = java.text.DateFormat.getDateTimeInstance(r6, r6, r4)
            goto L_0x004a
        L_0x0063:
            boolean r4 = r5 instanceof java.lang.String
            if (r4 == 0) goto L_0x006b
            r6 = r5
            java.lang.String r6 = (java.lang.String) r6
            goto L_0x002c
        L_0x006b:
            java.lang.String r4 = r5.toString()
            if (r4 != 0) goto L_0x0072
            goto L_0x002c
        L_0x0072:
            r6 = r4
            goto L_0x002c
        L_0x0074:
            if (r13 == 0) goto L_0x00ca
            int r4 = r11.length()
            if (r0 == r4) goto L_0x008b
            java.lang.String r0 = r11.substring(r0)
            java.text.AttributedCharacterIterator r0 = r9.createAttributedCharacterIterator((java.lang.String) r0)
            r13.add(r0)
            int r0 = r11.length()
        L_0x008b:
            if (r8 == 0) goto L_0x00ac
            java.text.AttributedCharacterIterator r4 = r8.formatToCharacterIterator(r5)
            r9.append(r11, r4)
            int r5 = r11.length()
            if (r0 == r5) goto L_0x00ad
            java.text.MessageFormat$Field r0 = java.text.MessageFormat.Field.ARGUMENT
            java.lang.Integer r5 = java.lang.Integer.valueOf((int) r3)
            java.text.AttributedCharacterIterator r0 = r9.createAttributedCharacterIterator((java.text.AttributedCharacterIterator) r4, (java.text.AttributedCharacterIterator.Attribute) r0, (java.lang.Object) r5)
            r13.add(r0)
            int r0 = r11.length()
            goto L_0x00ad
        L_0x00ac:
            r7 = r6
        L_0x00ad:
            if (r7 == 0) goto L_0x0103
            boolean r4 = r7.isEmpty()
            if (r4 != 0) goto L_0x0103
            r11.append((java.lang.String) r7)
            java.text.MessageFormat$Field r0 = java.text.MessageFormat.Field.ARGUMENT
            java.lang.Integer r3 = java.lang.Integer.valueOf((int) r3)
            java.text.AttributedCharacterIterator r0 = r9.createAttributedCharacterIterator((java.lang.String) r7, (java.text.AttributedCharacterIterator.Attribute) r0, (java.lang.Object) r3)
            r13.add(r0)
            int r0 = r11.length()
            goto L_0x0103
        L_0x00ca:
            if (r8 == 0) goto L_0x00d0
            java.lang.String r6 = r8.format(r5)
        L_0x00d0:
            int r0 = r11.length()
            r11.append((java.lang.String) r6)
            if (r1 != 0) goto L_0x00f1
            if (r12 == 0) goto L_0x00f1
            java.text.MessageFormat$Field r3 = java.text.MessageFormat.Field.ARGUMENT
            java.text.Format$Field r4 = r12.getFieldAttribute()
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L_0x00f1
            r12.setBeginIndex(r0)
            int r0 = r11.length()
            r12.setEndIndex(r0)
        L_0x00f1:
            int r0 = r11.length()
            goto L_0x0103
        L_0x00f6:
            java.lang.StringBuffer r4 = r11.append((char) r4)
            java.lang.StringBuffer r3 = r4.append((int) r3)
            r4 = 125(0x7d, float:1.75E-43)
            r3.append((char) r4)
        L_0x0103:
            int r1 = r1 + 1
            goto L_0x0006
        L_0x0107:
            java.lang.String r10 = r9.pattern
            int r12 = r10.length()
            r11.append((java.lang.CharSequence) r10, (int) r2, (int) r12)
            if (r13 == 0) goto L_0x0123
            int r10 = r11.length()
            if (r0 == r10) goto L_0x0123
            java.lang.String r10 = r11.substring(r0)
            java.text.AttributedCharacterIterator r9 = r9.createAttributedCharacterIterator((java.lang.String) r10)
            r13.add(r9)
        L_0x0123:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.MessageFormat.subformat(java.lang.Object[], java.lang.StringBuffer, java.text.FieldPosition, java.util.List):java.lang.StringBuffer");
    }

    private void append(StringBuffer stringBuffer, CharacterIterator characterIterator) {
        if (characterIterator.first() != 65535) {
            stringBuffer.append(characterIterator.first());
            while (true) {
                char next = characterIterator.next();
                if (next != 65535) {
                    stringBuffer.append(next);
                } else {
                    return;
                }
            }
        }
    }

    private void makeFormat(int i, int i2, StringBuilder[] sbArr) {
        Format format;
        int findKeyword;
        String[] strArr = new String[sbArr.length];
        for (int i3 = 0; i3 < sbArr.length; i3++) {
            StringBuilder sb = sbArr[i3];
            strArr[i3] = sb != null ? sb.toString() : "";
        }
        try {
            int parseInt = Integer.parseInt(strArr[1]);
            if (parseInt >= 0) {
                Format[] formatArr = this.formats;
                if (i2 >= formatArr.length) {
                    int length = formatArr.length * 2;
                    Format[] formatArr2 = new Format[length];
                    int[] iArr = new int[length];
                    int[] iArr2 = new int[length];
                    System.arraycopy((Object) formatArr, 0, (Object) formatArr2, 0, this.maxOffset + 1);
                    System.arraycopy((Object) this.offsets, 0, (Object) iArr, 0, this.maxOffset + 1);
                    System.arraycopy((Object) this.argumentNumbers, 0, (Object) iArr2, 0, this.maxOffset + 1);
                    this.formats = formatArr2;
                    this.offsets = iArr;
                    this.argumentNumbers = iArr2;
                }
                int i4 = this.maxOffset;
                this.maxOffset = i2;
                this.offsets[i2] = strArr[0].length();
                this.argumentNumbers[i2] = parseInt;
                if (strArr[2].isEmpty() || (findKeyword = findKeyword(strArr[2], TYPE_KEYWORDS)) == 0) {
                    format = null;
                } else if (findKeyword == 1) {
                    int findKeyword2 = findKeyword(strArr[3], NUMBER_MODIFIER_KEYWORDS);
                    if (findKeyword2 == 0) {
                        format = NumberFormat.getInstance(this.locale);
                    } else if (findKeyword2 == 1) {
                        format = NumberFormat.getCurrencyInstance(this.locale);
                    } else if (findKeyword2 == 2) {
                        format = NumberFormat.getPercentInstance(this.locale);
                    } else if (findKeyword2 != 3) {
                        try {
                            format = new DecimalFormat(strArr[3], DecimalFormatSymbols.getInstance(this.locale));
                        } catch (IllegalArgumentException e) {
                            this.maxOffset = i4;
                            throw e;
                        }
                    } else {
                        format = NumberFormat.getIntegerInstance(this.locale);
                    }
                } else if (findKeyword == 2 || findKeyword == 3) {
                    String str = strArr[3];
                    String[] strArr2 = DATE_TIME_MODIFIER_KEYWORDS;
                    int findKeyword3 = findKeyword(str, strArr2);
                    if (findKeyword3 < 0 || findKeyword3 >= strArr2.length) {
                        try {
                            format = new SimpleDateFormat(strArr[3], this.locale);
                        } catch (IllegalArgumentException e2) {
                            this.maxOffset = i4;
                            throw e2;
                        }
                    } else {
                        format = findKeyword == 2 ? DateFormat.getDateInstance(DATE_TIME_MODIFIERS[findKeyword3], this.locale) : DateFormat.getTimeInstance(DATE_TIME_MODIFIERS[findKeyword3], this.locale);
                    }
                } else if (findKeyword == 4) {
                    try {
                        format = new ChoiceFormat(strArr[3]);
                    } catch (Exception e3) {
                        this.maxOffset = i4;
                        throw new IllegalArgumentException("Choice Pattern incorrect: " + strArr[3], e3);
                    }
                } else {
                    this.maxOffset = i4;
                    throw new IllegalArgumentException("unknown format type: " + strArr[2]);
                }
                this.formats[i2] = format;
                return;
            }
            throw new IllegalArgumentException("negative argument number: " + parseInt);
        } catch (NumberFormatException e4) {
            throw new IllegalArgumentException("can't parse argument number: " + strArr[1], e4);
        }
    }

    private static final int findKeyword(String str, String[] strArr) {
        for (int i = 0; i < strArr.length; i++) {
            if (str.equals(strArr[i])) {
                return i;
            }
        }
        String lowerCase = str.trim().toLowerCase(Locale.ROOT);
        if (lowerCase == str) {
            return -1;
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (lowerCase.equals(strArr[i2])) {
                return i2;
            }
        }
        return -1;
    }

    private static final void copyAndFixQuotes(String str, int i, int i2, StringBuilder sb) {
        boolean z = false;
        while (i < i2) {
            char charAt = str.charAt(i);
            if (charAt == '{') {
                if (!z) {
                    sb.append('\'');
                    z = true;
                }
                sb.append(charAt);
            } else if (charAt == '\'') {
                sb.append("''");
            } else {
                if (z) {
                    sb.append('\'');
                    z = false;
                }
                sb.append(charAt);
            }
            i++;
        }
        if (z) {
            sb.append('\'');
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0039 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readObject(java.p026io.ObjectInputStream r5) throws java.p026io.IOException, java.lang.ClassNotFoundException {
        /*
            r4 = this;
            r5.defaultReadObject()
            int r5 = r4.maxOffset
            r0 = -1
            r1 = 1
            r2 = 0
            if (r5 < r0) goto L_0x001b
            java.text.Format[] r0 = r4.formats
            int r0 = r0.length
            if (r0 <= r5) goto L_0x001b
            int[] r0 = r4.offsets
            int r0 = r0.length
            if (r0 <= r5) goto L_0x001b
            int[] r0 = r4.argumentNumbers
            int r0 = r0.length
            if (r0 <= r5) goto L_0x001b
            r5 = r1
            goto L_0x001c
        L_0x001b:
            r5 = r2
        L_0x001c:
            if (r5 == 0) goto L_0x0036
            java.lang.String r0 = r4.pattern
            int r0 = r0.length()
            int r0 = r0 + r1
            int r1 = r4.maxOffset
        L_0x0027:
            if (r1 < 0) goto L_0x0036
            int[] r3 = r4.offsets
            r3 = r3[r1]
            if (r3 < 0) goto L_0x0037
            if (r3 <= r0) goto L_0x0032
            goto L_0x0037
        L_0x0032:
            int r1 = r1 + -1
            r0 = r3
            goto L_0x0027
        L_0x0036:
            r2 = r5
        L_0x0037:
            if (r2 == 0) goto L_0x003a
            return
        L_0x003a:
            java.io.InvalidObjectException r4 = new java.io.InvalidObjectException
            java.lang.String r5 = "Could not reconstruct MessageFormat from corrupt stream."
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.MessageFormat.readObject(java.io.ObjectInputStream):void");
    }
}
