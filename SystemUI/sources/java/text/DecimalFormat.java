package java.text;

import android.icu.text.NumberFormat;
import com.android.icu.text.CompatibleDecimalFormatFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.text.AttributedCharacterIterator;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import libcore.icu.DecimalFormatData;
import sun.util.locale.LanguageTag;

public class DecimalFormat extends NumberFormat {
    static final int DOUBLE_FRACTION_DIGITS = 340;
    static final int DOUBLE_INTEGER_DIGITS = 309;
    static final int MAXIMUM_FRACTION_DIGITS = Integer.MAX_VALUE;
    static final int MAXIMUM_INTEGER_DIGITS = Integer.MAX_VALUE;
    static final int currentSerialVersion = 4;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("positivePrefix", String.class), new ObjectStreamField("positiveSuffix", String.class), new ObjectStreamField("negativePrefix", String.class), new ObjectStreamField("negativeSuffix", String.class), new ObjectStreamField("posPrefixPattern", String.class), new ObjectStreamField("posSuffixPattern", String.class), new ObjectStreamField("negPrefixPattern", String.class), new ObjectStreamField("negSuffixPattern", String.class), new ObjectStreamField("multiplier", Integer.TYPE), new ObjectStreamField("groupingSize", Byte.TYPE), new ObjectStreamField("groupingUsed", Boolean.TYPE), new ObjectStreamField("decimalSeparatorAlwaysShown", Boolean.TYPE), new ObjectStreamField("parseBigDecimal", Boolean.TYPE), new ObjectStreamField("roundingMode", RoundingMode.class), new ObjectStreamField("symbols", DecimalFormatSymbols.class), new ObjectStreamField("useExponentialNotation", Boolean.TYPE), new ObjectStreamField("minExponentDigits", Byte.TYPE), new ObjectStreamField("maximumIntegerDigits", Integer.TYPE), new ObjectStreamField("minimumIntegerDigits", Integer.TYPE), new ObjectStreamField("maximumFractionDigits", Integer.TYPE), new ObjectStreamField("minimumFractionDigits", Integer.TYPE), new ObjectStreamField("serialVersionOnStream", Integer.TYPE)};
    static final long serialVersionUID = 864413376551465018L;
    private transient android.icu.text.DecimalFormat icuDecimalFormat;
    private int maximumFractionDigits;
    private int maximumIntegerDigits;
    private int minimumFractionDigits;
    private int minimumIntegerDigits;
    private RoundingMode roundingMode;
    private DecimalFormatSymbols symbols;

    public DecimalFormat() {
        this.symbols = null;
        this.roundingMode = RoundingMode.HALF_EVEN;
        Locale locale = Locale.getDefault(Locale.Category.FORMAT);
        String numberPattern = DecimalFormatData.getInstance(locale).getNumberPattern();
        this.symbols = DecimalFormatSymbols.getInstance(locale);
        initPattern(numberPattern);
    }

    public DecimalFormat(String str) {
        this.symbols = null;
        this.roundingMode = RoundingMode.HALF_EVEN;
        this.symbols = DecimalFormatSymbols.getInstance(Locale.getDefault(Locale.Category.FORMAT));
        initPattern(str);
    }

    public DecimalFormat(String str, DecimalFormatSymbols decimalFormatSymbols) {
        this.symbols = null;
        this.roundingMode = RoundingMode.HALF_EVEN;
        this.symbols = (DecimalFormatSymbols) decimalFormatSymbols.clone();
        initPattern(str);
    }

    private void initPattern(String str) {
        this.icuDecimalFormat = CompatibleDecimalFormatFactory.create(str, this.symbols.getIcuDecimalFormatSymbols());
        updateFieldsFromIcu();
    }

    private void updateFieldsFromIcu() {
        if (this.icuDecimalFormat.getMaximumIntegerDigits() == 309) {
            this.icuDecimalFormat.setMaximumIntegerDigits(2000000000);
        }
        this.maximumIntegerDigits = this.icuDecimalFormat.getMaximumIntegerDigits();
        this.minimumIntegerDigits = this.icuDecimalFormat.getMinimumIntegerDigits();
        this.maximumFractionDigits = this.icuDecimalFormat.getMaximumFractionDigits();
        this.minimumFractionDigits = this.icuDecimalFormat.getMinimumFractionDigits();
    }

    private static FieldPosition getIcuFieldPosition(FieldPosition fieldPosition) {
        NumberFormat.Field field;
        Format.Field fieldAttribute = fieldPosition.getFieldAttribute();
        if (fieldAttribute == null) {
            return fieldPosition;
        }
        if (fieldAttribute == NumberFormat.Field.INTEGER) {
            field = NumberFormat.Field.INTEGER;
        } else if (fieldAttribute == NumberFormat.Field.FRACTION) {
            field = NumberFormat.Field.FRACTION;
        } else if (fieldAttribute == NumberFormat.Field.DECIMAL_SEPARATOR) {
            field = NumberFormat.Field.DECIMAL_SEPARATOR;
        } else if (fieldAttribute == NumberFormat.Field.EXPONENT_SYMBOL) {
            field = NumberFormat.Field.EXPONENT_SYMBOL;
        } else if (fieldAttribute == NumberFormat.Field.EXPONENT_SIGN) {
            field = NumberFormat.Field.EXPONENT_SIGN;
        } else if (fieldAttribute == NumberFormat.Field.EXPONENT) {
            field = NumberFormat.Field.EXPONENT;
        } else if (fieldAttribute == NumberFormat.Field.GROUPING_SEPARATOR) {
            field = NumberFormat.Field.GROUPING_SEPARATOR;
        } else if (fieldAttribute == NumberFormat.Field.CURRENCY) {
            field = NumberFormat.Field.CURRENCY;
        } else if (fieldAttribute == NumberFormat.Field.PERCENT) {
            field = NumberFormat.Field.PERCENT;
        } else if (fieldAttribute == NumberFormat.Field.PERMILLE) {
            field = NumberFormat.Field.PERMILLE;
        } else if (fieldAttribute == NumberFormat.Field.SIGN) {
            field = NumberFormat.Field.SIGN;
        } else {
            throw new IllegalArgumentException("Unexpected field position attribute type.");
        }
        FieldPosition fieldPosition2 = new FieldPosition((Format.Field) field);
        fieldPosition2.setBeginIndex(fieldPosition.getBeginIndex());
        fieldPosition2.setEndIndex(fieldPosition.getEndIndex());
        return fieldPosition2;
    }

    private static NumberFormat.Field toJavaFieldAttribute(AttributedCharacterIterator.Attribute attribute) {
        String name = attribute.getName();
        if (name.equals(NumberFormat.Field.INTEGER.getName())) {
            return NumberFormat.Field.INTEGER;
        }
        if (name.equals(NumberFormat.Field.CURRENCY.getName())) {
            return NumberFormat.Field.CURRENCY;
        }
        if (name.equals(NumberFormat.Field.DECIMAL_SEPARATOR.getName())) {
            return NumberFormat.Field.DECIMAL_SEPARATOR;
        }
        if (name.equals(NumberFormat.Field.EXPONENT.getName())) {
            return NumberFormat.Field.EXPONENT;
        }
        if (name.equals(NumberFormat.Field.EXPONENT_SIGN.getName())) {
            return NumberFormat.Field.EXPONENT_SIGN;
        }
        if (name.equals(NumberFormat.Field.EXPONENT_SYMBOL.getName())) {
            return NumberFormat.Field.EXPONENT_SYMBOL;
        }
        if (name.equals(NumberFormat.Field.FRACTION.getName())) {
            return NumberFormat.Field.FRACTION;
        }
        if (name.equals(NumberFormat.Field.GROUPING_SEPARATOR.getName())) {
            return NumberFormat.Field.GROUPING_SEPARATOR;
        }
        if (name.equals(NumberFormat.Field.SIGN.getName())) {
            return NumberFormat.Field.SIGN;
        }
        if (name.equals(NumberFormat.Field.PERCENT.getName())) {
            return NumberFormat.Field.PERCENT;
        }
        if (name.equals(NumberFormat.Field.PERMILLE.getName())) {
            return NumberFormat.Field.PERMILLE;
        }
        throw new IllegalArgumentException("Unrecognized attribute: " + name);
    }

    public final StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        boolean z;
        if ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte) || (obj instanceof AtomicInteger) || (obj instanceof AtomicLong) || (((z = obj instanceof BigInteger)) && ((BigInteger) obj).bitLength() < 64)) {
            return format(((Number) obj).longValue(), stringBuffer, fieldPosition);
        }
        if (obj instanceof BigDecimal) {
            return format((BigDecimal) obj, stringBuffer, fieldPosition);
        }
        if (z) {
            return format((BigInteger) obj, stringBuffer, fieldPosition);
        }
        if (obj instanceof Number) {
            return format(((Number) obj).doubleValue(), stringBuffer, fieldPosition);
        }
        throw new IllegalArgumentException("Cannot format given Object as a Number");
    }

    public StringBuffer format(double d, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        FieldPosition icuFieldPosition = getIcuFieldPosition(fieldPosition);
        this.icuDecimalFormat.format(d, stringBuffer, icuFieldPosition);
        fieldPosition.setBeginIndex(icuFieldPosition.getBeginIndex());
        fieldPosition.setEndIndex(icuFieldPosition.getEndIndex());
        return stringBuffer;
    }

    public StringBuffer format(long j, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        FieldPosition icuFieldPosition = getIcuFieldPosition(fieldPosition);
        this.icuDecimalFormat.format(j, stringBuffer, icuFieldPosition);
        fieldPosition.setBeginIndex(icuFieldPosition.getBeginIndex());
        fieldPosition.setEndIndex(icuFieldPosition.getEndIndex());
        return stringBuffer;
    }

    private StringBuffer format(BigDecimal bigDecimal, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        FieldPosition icuFieldPosition = getIcuFieldPosition(fieldPosition);
        this.icuDecimalFormat.format(bigDecimal, stringBuffer, icuFieldPosition);
        fieldPosition.setBeginIndex(icuFieldPosition.getBeginIndex());
        fieldPosition.setEndIndex(icuFieldPosition.getEndIndex());
        return stringBuffer;
    }

    private StringBuffer format(BigInteger bigInteger, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        FieldPosition icuFieldPosition = getIcuFieldPosition(fieldPosition);
        this.icuDecimalFormat.format(bigInteger, stringBuffer, icuFieldPosition);
        fieldPosition.setBeginIndex(icuFieldPosition.getBeginIndex());
        fieldPosition.setEndIndex(icuFieldPosition.getEndIndex());
        return stringBuffer;
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        if (obj != null) {
            AttributedCharacterIterator formatToCharacterIterator = this.icuDecimalFormat.formatToCharacterIterator(obj);
            StringBuilder sb = new StringBuilder(formatToCharacterIterator.getEndIndex() - formatToCharacterIterator.getBeginIndex());
            for (int beginIndex = formatToCharacterIterator.getBeginIndex(); beginIndex < formatToCharacterIterator.getEndIndex(); beginIndex++) {
                sb.append(formatToCharacterIterator.current());
                formatToCharacterIterator.next();
            }
            AttributedString attributedString = new AttributedString(sb.toString());
            for (int beginIndex2 = formatToCharacterIterator.getBeginIndex(); beginIndex2 < formatToCharacterIterator.getEndIndex(); beginIndex2++) {
                formatToCharacterIterator.setIndex(beginIndex2);
                for (AttributedCharacterIterator.Attribute javaFieldAttribute : formatToCharacterIterator.getAttributes().keySet()) {
                    int runStart = formatToCharacterIterator.getRunStart();
                    int runLimit = formatToCharacterIterator.getRunLimit();
                    NumberFormat.Field javaFieldAttribute2 = toJavaFieldAttribute(javaFieldAttribute);
                    attributedString.addAttribute(javaFieldAttribute2, javaFieldAttribute2, runStart, runLimit);
                }
            }
            return attributedString.getIterator();
        }
        throw new NullPointerException("object == null");
    }

    public Number parse(String str, ParsePosition parsePosition) {
        Number parse;
        if (parsePosition.index < 0 || parsePosition.index >= str.length() || (parse = this.icuDecimalFormat.parse(str, parsePosition)) == null) {
            return null;
        }
        if (isParseBigDecimal()) {
            if (parse instanceof Long) {
                return new BigDecimal(parse.longValue());
            }
            boolean z = parse instanceof Double;
            if (z) {
                Double d = (Double) parse;
                if (!d.isInfinite() && !d.isNaN()) {
                    return new BigDecimal(parse.toString());
                }
            }
            if (z) {
                Double d2 = (Double) parse;
                if (d2.isNaN() || d2.isInfinite()) {
                    return parse;
                }
            }
            if (parse instanceof android.icu.math.BigDecimal) {
                return ((android.icu.math.BigDecimal) parse).toBigDecimal();
            }
        }
        if ((parse instanceof android.icu.math.BigDecimal) || (parse instanceof BigInteger)) {
            return Double.valueOf(parse.doubleValue());
        }
        if (!isParseIntegerOnly() || !parse.equals(new Double(-0.0d))) {
            return parse;
        }
        return 0L;
    }

    public DecimalFormatSymbols getDecimalFormatSymbols() {
        return DecimalFormatSymbols.fromIcuInstance(this.icuDecimalFormat.getDecimalFormatSymbols());
    }

    public void setDecimalFormatSymbols(DecimalFormatSymbols decimalFormatSymbols) {
        try {
            DecimalFormatSymbols decimalFormatSymbols2 = (DecimalFormatSymbols) decimalFormatSymbols.clone();
            this.symbols = decimalFormatSymbols2;
            this.icuDecimalFormat.setDecimalFormatSymbols(decimalFormatSymbols2.getIcuDecimalFormatSymbols());
        } catch (Exception unused) {
        }
    }

    public String getPositivePrefix() {
        return this.icuDecimalFormat.getPositivePrefix();
    }

    public void setPositivePrefix(String str) {
        this.icuDecimalFormat.setPositivePrefix(str);
    }

    public String getNegativePrefix() {
        return this.icuDecimalFormat.getNegativePrefix();
    }

    public void setNegativePrefix(String str) {
        this.icuDecimalFormat.setNegativePrefix(str);
    }

    public String getPositiveSuffix() {
        return this.icuDecimalFormat.getPositiveSuffix();
    }

    public void setPositiveSuffix(String str) {
        this.icuDecimalFormat.setPositiveSuffix(str);
    }

    public String getNegativeSuffix() {
        return this.icuDecimalFormat.getNegativeSuffix();
    }

    public void setNegativeSuffix(String str) {
        this.icuDecimalFormat.setNegativeSuffix(str);
    }

    public int getMultiplier() {
        return this.icuDecimalFormat.getMultiplier();
    }

    public void setMultiplier(int i) {
        this.icuDecimalFormat.setMultiplier(i);
    }

    public void setGroupingUsed(boolean z) {
        this.icuDecimalFormat.setGroupingUsed(z);
    }

    public boolean isGroupingUsed() {
        return this.icuDecimalFormat.isGroupingUsed();
    }

    public int getGroupingSize() {
        return this.icuDecimalFormat.getGroupingSize();
    }

    public void setGroupingSize(int i) {
        this.icuDecimalFormat.setGroupingSize(i);
    }

    public boolean isDecimalSeparatorAlwaysShown() {
        return this.icuDecimalFormat.isDecimalSeparatorAlwaysShown();
    }

    public void setDecimalSeparatorAlwaysShown(boolean z) {
        this.icuDecimalFormat.setDecimalSeparatorAlwaysShown(z);
    }

    public boolean isParseBigDecimal() {
        return this.icuDecimalFormat.isParseBigDecimal();
    }

    public void setParseBigDecimal(boolean z) {
        this.icuDecimalFormat.setParseBigDecimal(z);
    }

    public boolean isParseIntegerOnly() {
        return this.icuDecimalFormat.isParseIntegerOnly();
    }

    public void setParseIntegerOnly(boolean z) {
        super.setParseIntegerOnly(z);
        this.icuDecimalFormat.setParseIntegerOnly(z);
    }

    public Object clone() {
        try {
            DecimalFormat decimalFormat = (DecimalFormat) super.clone();
            decimalFormat.icuDecimalFormat = (android.icu.text.DecimalFormat) this.icuDecimalFormat.clone();
            decimalFormat.symbols = (DecimalFormatSymbols) this.symbols.clone();
            return decimalFormat;
        } catch (Exception unused) {
            throw new InternalError();
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DecimalFormat)) {
            return false;
        }
        DecimalFormat decimalFormat = (DecimalFormat) obj;
        if (!this.icuDecimalFormat.equals(decimalFormat.icuDecimalFormat) || !compareIcuRoundingIncrement(decimalFormat.icuDecimalFormat)) {
            return false;
        }
        return true;
    }

    private boolean compareIcuRoundingIncrement(android.icu.text.DecimalFormat decimalFormat) {
        BigDecimal roundingIncrement = this.icuDecimalFormat.getRoundingIncrement();
        if (roundingIncrement != null) {
            if (decimalFormat.getRoundingIncrement() == null || !roundingIncrement.equals(decimalFormat.getRoundingIncrement())) {
                return false;
            }
            return true;
        } else if (decimalFormat.getRoundingIncrement() == null) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (super.hashCode() * 37) + getPositivePrefix().hashCode();
    }

    public String toPattern() {
        return this.icuDecimalFormat.toPattern();
    }

    public String toLocalizedPattern() {
        return this.icuDecimalFormat.toLocalizedPattern();
    }

    public void applyPattern(String str) {
        this.icuDecimalFormat.applyPattern(str);
        updateFieldsFromIcu();
    }

    public void applyLocalizedPattern(String str) {
        this.icuDecimalFormat.applyLocalizedPattern(str);
        updateFieldsFromIcu();
    }

    public void setMaximumIntegerDigits(int i) {
        int min = Math.min(Math.max(0, i), Integer.MAX_VALUE);
        this.maximumIntegerDigits = min;
        int i2 = 309;
        if (min > 309) {
            min = 309;
        }
        super.setMaximumIntegerDigits(min);
        int i3 = this.minimumIntegerDigits;
        int i4 = this.maximumIntegerDigits;
        if (i3 > i4) {
            this.minimumIntegerDigits = i4;
            if (i4 <= 309) {
                i2 = i4;
            }
            super.setMinimumIntegerDigits(i2);
        }
        this.icuDecimalFormat.setMaximumIntegerDigits(getMaximumIntegerDigits());
    }

    public void setMinimumIntegerDigits(int i) {
        int min = Math.min(Math.max(0, i), Integer.MAX_VALUE);
        this.minimumIntegerDigits = min;
        int i2 = 309;
        if (min > 309) {
            min = 309;
        }
        super.setMinimumIntegerDigits(min);
        int i3 = this.minimumIntegerDigits;
        if (i3 > this.maximumIntegerDigits) {
            this.maximumIntegerDigits = i3;
            if (i3 <= 309) {
                i2 = i3;
            }
            super.setMaximumIntegerDigits(i2);
        }
        this.icuDecimalFormat.setMinimumIntegerDigits(getMinimumIntegerDigits());
    }

    public void setMaximumFractionDigits(int i) {
        int min = Math.min(Math.max(0, i), Integer.MAX_VALUE);
        this.maximumFractionDigits = min;
        int i2 = 340;
        if (min > 340) {
            min = 340;
        }
        super.setMaximumFractionDigits(min);
        int i3 = this.minimumFractionDigits;
        int i4 = this.maximumFractionDigits;
        if (i3 > i4) {
            this.minimumFractionDigits = i4;
            if (i4 <= 340) {
                i2 = i4;
            }
            super.setMinimumFractionDigits(i2);
        }
        this.icuDecimalFormat.setMaximumFractionDigits(getMaximumFractionDigits());
    }

    public void setMinimumFractionDigits(int i) {
        int min = Math.min(Math.max(0, i), Integer.MAX_VALUE);
        this.minimumFractionDigits = min;
        int i2 = 340;
        if (min > 340) {
            min = 340;
        }
        super.setMinimumFractionDigits(min);
        int i3 = this.minimumFractionDigits;
        if (i3 > this.maximumFractionDigits) {
            this.maximumFractionDigits = i3;
            if (i3 <= 340) {
                i2 = i3;
            }
            super.setMaximumFractionDigits(i2);
        }
        this.icuDecimalFormat.setMinimumFractionDigits(getMinimumFractionDigits());
    }

    public int getMaximumIntegerDigits() {
        return this.maximumIntegerDigits;
    }

    public int getMinimumIntegerDigits() {
        return this.minimumIntegerDigits;
    }

    public int getMaximumFractionDigits() {
        return this.maximumFractionDigits;
    }

    public int getMinimumFractionDigits() {
        return this.minimumFractionDigits;
    }

    public Currency getCurrency() {
        return this.symbols.getCurrency();
    }

    public void setCurrency(Currency currency) {
        if (currency != this.symbols.getCurrency() || !currency.getSymbol().equals(this.symbols.getCurrencySymbol())) {
            this.symbols.setCurrency(currency);
            this.icuDecimalFormat.setDecimalFormatSymbols(this.symbols.getIcuDecimalFormatSymbols());
            this.icuDecimalFormat.setMinimumFractionDigits(this.minimumFractionDigits);
            this.icuDecimalFormat.setMaximumFractionDigits(this.maximumFractionDigits);
        }
    }

    public RoundingMode getRoundingMode() {
        return this.roundingMode;
    }

    /* renamed from: java.text.DecimalFormat$1 */
    static /* synthetic */ class C28471 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode;

        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
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
                java.math.RoundingMode r1 = java.math.RoundingMode.UP     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x001d }
                java.math.RoundingMode r1 = java.math.RoundingMode.DOWN     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.math.RoundingMode r1 = java.math.RoundingMode.CEILING     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.math.RoundingMode r1 = java.math.RoundingMode.FLOOR     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x003e }
                java.math.RoundingMode r1 = java.math.RoundingMode.HALF_UP     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.math.RoundingMode r1 = java.math.RoundingMode.HALF_DOWN     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x0054 }
                java.math.RoundingMode r1 = java.math.RoundingMode.HALF_EVEN     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$java$math$RoundingMode     // Catch:{ NoSuchFieldError -> 0x0060 }
                java.math.RoundingMode r1 = java.math.RoundingMode.UNNECESSARY     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.text.DecimalFormat.C28471.<clinit>():void");
        }
    }

    private static int convertRoundingMode(RoundingMode roundingMode2) {
        switch (C28471.$SwitchMap$java$math$RoundingMode[roundingMode2.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            case 8:
                return 7;
            default:
                throw new IllegalArgumentException("Invalid rounding mode specified");
        }
    }

    public void setRoundingMode(RoundingMode roundingMode2) {
        roundingMode2.getClass();
        this.roundingMode = roundingMode2;
        this.icuDecimalFormat.setRoundingMode(convertRoundingMode(roundingMode2));
    }

    /* access modifiers changed from: package-private */
    public void adjustForCurrencyDefaultFractionDigits() {
        int defaultFractionDigits;
        Currency currency = this.symbols.getCurrency();
        if (currency == null) {
            try {
                currency = Currency.getInstance(this.symbols.getInternationalCurrencySymbol());
            } catch (IllegalArgumentException unused) {
            }
        }
        if (currency != null && (defaultFractionDigits = currency.getDefaultFractionDigits()) != -1) {
            int minimumFractionDigits2 = getMinimumFractionDigits();
            if (minimumFractionDigits2 == getMaximumFractionDigits()) {
                setMinimumFractionDigits(defaultFractionDigits);
                setMaximumFractionDigits(defaultFractionDigits);
                return;
            }
            setMinimumFractionDigits(Math.min(defaultFractionDigits, minimumFractionDigits2));
            setMaximumFractionDigits(defaultFractionDigits);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("positivePrefix", (Object) this.icuDecimalFormat.getPositivePrefix());
        putFields.put("positiveSuffix", (Object) this.icuDecimalFormat.getPositiveSuffix());
        putFields.put("negativePrefix", (Object) this.icuDecimalFormat.getNegativePrefix());
        putFields.put("negativeSuffix", (Object) this.icuDecimalFormat.getNegativeSuffix());
        String str = null;
        putFields.put("posPrefixPattern", (Object) null);
        putFields.put("posSuffixPattern", (Object) null);
        putFields.put("negPrefixPattern", (Object) null);
        putFields.put("negSuffixPattern", (Object) null);
        putFields.put("multiplier", this.icuDecimalFormat.getMultiplier());
        putFields.put("groupingSize", (byte) this.icuDecimalFormat.getGroupingSize());
        putFields.put("groupingUsed", this.icuDecimalFormat.isGroupingUsed());
        putFields.put("decimalSeparatorAlwaysShown", this.icuDecimalFormat.isDecimalSeparatorAlwaysShown());
        putFields.put("parseBigDecimal", this.icuDecimalFormat.isParseBigDecimal());
        putFields.put("roundingMode", (Object) this.roundingMode);
        putFields.put("symbols", (Object) this.symbols);
        putFields.put("useExponentialNotation", false);
        putFields.put("minExponentDigits", (byte) 0);
        putFields.put("maximumIntegerDigits", this.icuDecimalFormat.getMaximumIntegerDigits());
        putFields.put("minimumIntegerDigits", this.icuDecimalFormat.getMinimumIntegerDigits());
        putFields.put("maximumFractionDigits", this.icuDecimalFormat.getMaximumFractionDigits());
        putFields.put("minimumFractionDigits", this.icuDecimalFormat.getMinimumFractionDigits());
        putFields.put("serialVersionOnStream", 4);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        this.symbols = (DecimalFormatSymbols) readFields.get("symbols", (Object) null);
        initPattern("#");
        String str = (String) readFields.get("positivePrefix", (Object) "");
        if (!Objects.equals(str, this.icuDecimalFormat.getPositivePrefix())) {
            this.icuDecimalFormat.setPositivePrefix(str);
        }
        String str2 = (String) readFields.get("positiveSuffix", (Object) "");
        if (!Objects.equals(str2, this.icuDecimalFormat.getPositiveSuffix())) {
            this.icuDecimalFormat.setPositiveSuffix(str2);
        }
        String str3 = (String) readFields.get("negativePrefix", (Object) LanguageTag.SEP);
        if (!Objects.equals(str3, this.icuDecimalFormat.getNegativePrefix())) {
            this.icuDecimalFormat.setNegativePrefix(str3);
        }
        String str4 = (String) readFields.get("negativeSuffix", (Object) "");
        if (!Objects.equals(str4, this.icuDecimalFormat.getNegativeSuffix())) {
            this.icuDecimalFormat.setNegativeSuffix(str4);
        }
        int i = readFields.get("multiplier", 1);
        if (i != this.icuDecimalFormat.getMultiplier()) {
            this.icuDecimalFormat.setMultiplier(i);
        }
        boolean z = readFields.get("groupingUsed", true);
        if (z != this.icuDecimalFormat.isGroupingUsed()) {
            this.icuDecimalFormat.setGroupingUsed(z);
        }
        byte b = readFields.get("groupingSize", (byte) 3);
        if (b != this.icuDecimalFormat.getGroupingSize()) {
            this.icuDecimalFormat.setGroupingSize(b);
        }
        boolean z2 = readFields.get("decimalSeparatorAlwaysShown", false);
        if (z2 != this.icuDecimalFormat.isDecimalSeparatorAlwaysShown()) {
            this.icuDecimalFormat.setDecimalSeparatorAlwaysShown(z2);
        }
        RoundingMode roundingMode2 = (RoundingMode) readFields.get("roundingMode", (Object) RoundingMode.HALF_EVEN);
        if (convertRoundingMode(roundingMode2) != this.icuDecimalFormat.getRoundingMode()) {
            setRoundingMode(roundingMode2);
        }
        int i2 = readFields.get("maximumIntegerDigits", 309);
        if (i2 != this.icuDecimalFormat.getMaximumIntegerDigits()) {
            this.icuDecimalFormat.setMaximumIntegerDigits(i2);
        }
        int i3 = readFields.get("minimumIntegerDigits", 309);
        if (i3 != this.icuDecimalFormat.getMinimumIntegerDigits()) {
            this.icuDecimalFormat.setMinimumIntegerDigits(i3);
        }
        int i4 = readFields.get("maximumFractionDigits", 340);
        if (i4 != this.icuDecimalFormat.getMaximumFractionDigits()) {
            this.icuDecimalFormat.setMaximumFractionDigits(i4);
        }
        int i5 = readFields.get("minimumFractionDigits", 340);
        if (i5 != this.icuDecimalFormat.getMinimumFractionDigits()) {
            this.icuDecimalFormat.setMinimumFractionDigits(i5);
        }
        boolean z3 = readFields.get("parseBigDecimal", true);
        if (z3 != this.icuDecimalFormat.isParseBigDecimal()) {
            this.icuDecimalFormat.setParseBigDecimal(z3);
        }
        updateFieldsFromIcu();
        if (readFields.get("serialVersionOnStream", 0) < 3) {
            setMaximumIntegerDigits(super.getMaximumIntegerDigits());
            setMinimumIntegerDigits(super.getMinimumIntegerDigits());
            setMaximumFractionDigits(super.getMaximumFractionDigits());
            setMinimumFractionDigits(super.getMinimumFractionDigits());
        }
    }
}
