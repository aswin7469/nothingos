package java.text;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.text.Format;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import libcore.icu.DecimalFormatData;
import libcore.icu.ICU;

public abstract class NumberFormat extends Format {
    private static final int CURRENCYSTYLE = 1;
    public static final int FRACTION_FIELD = 1;
    private static final int INTEGERSTYLE = 3;
    public static final int INTEGER_FIELD = 0;
    private static final int NUMBERSTYLE = 0;
    private static final int PERCENTSTYLE = 2;
    static final int currentSerialVersion = 1;
    static final long serialVersionUID = -2308460125733713944L;
    private boolean groupingUsed = true;
    private byte maxFractionDigits = 3;
    private byte maxIntegerDigits = 40;
    private int maximumFractionDigits = 3;
    private int maximumIntegerDigits = 40;
    private byte minFractionDigits = 0;
    private byte minIntegerDigits = 1;
    private int minimumFractionDigits = 0;
    private int minimumIntegerDigits = 1;
    private boolean parseIntegerOnly = false;
    private int serialVersionOnStream = 1;

    public abstract StringBuffer format(double d, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract StringBuffer format(long j, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract Number parse(String str, ParsePosition parsePosition);

    protected NumberFormat() {
    }

    public StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        if ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte) || (obj instanceof AtomicInteger) || (obj instanceof AtomicLong) || ((obj instanceof BigInteger) && ((BigInteger) obj).bitLength() < 64)) {
            return format(((Number) obj).longValue(), stringBuffer, fieldPosition);
        }
        if (obj instanceof Number) {
            return format(((Number) obj).doubleValue(), stringBuffer, fieldPosition);
        }
        throw new IllegalArgumentException("Cannot format given Object as a Number");
    }

    public final Object parseObject(String str, ParsePosition parsePosition) {
        return parse(str, parsePosition);
    }

    public final String format(double d) {
        return format(d, new StringBuffer(), DontCareFieldPosition.INSTANCE).toString();
    }

    public final String format(long j) {
        return format(j, new StringBuffer(), DontCareFieldPosition.INSTANCE).toString();
    }

    public Number parse(String str) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Number parse = parse(str, parsePosition);
        if (parsePosition.index != 0) {
            return parse;
        }
        throw new ParseException("Unparseable number: \"" + str + "\"", parsePosition.errorIndex);
    }

    public boolean isParseIntegerOnly() {
        return this.parseIntegerOnly;
    }

    public void setParseIntegerOnly(boolean z) {
        this.parseIntegerOnly = z;
    }

    public static final NumberFormat getInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 0);
    }

    public static NumberFormat getInstance(Locale locale) {
        return getInstance(locale, 0);
    }

    public static final NumberFormat getNumberInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 0);
    }

    public static NumberFormat getNumberInstance(Locale locale) {
        return getInstance(locale, 0);
    }

    public static final NumberFormat getIntegerInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 3);
    }

    public static NumberFormat getIntegerInstance(Locale locale) {
        return getInstance(locale, 3);
    }

    public static final NumberFormat getCurrencyInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 1);
    }

    public static NumberFormat getCurrencyInstance(Locale locale) {
        return getInstance(locale, 1);
    }

    public static final NumberFormat getPercentInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT), 2);
    }

    public static NumberFormat getPercentInstance(Locale locale) {
        return getInstance(locale, 2);
    }

    public static Locale[] getAvailableLocales() {
        return ICU.getAvailableLocales();
    }

    public int hashCode() {
        return (this.maximumIntegerDigits * 37) + this.maxFractionDigits;
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
        NumberFormat numberFormat = (NumberFormat) obj;
        if (this.maximumIntegerDigits == numberFormat.maximumIntegerDigits && this.minimumIntegerDigits == numberFormat.minimumIntegerDigits && this.maximumFractionDigits == numberFormat.maximumFractionDigits && this.minimumFractionDigits == numberFormat.minimumFractionDigits && this.groupingUsed == numberFormat.groupingUsed && this.parseIntegerOnly == numberFormat.parseIntegerOnly) {
            return true;
        }
        return false;
    }

    public Object clone() {
        return (NumberFormat) super.clone();
    }

    public boolean isGroupingUsed() {
        return this.groupingUsed;
    }

    public void setGroupingUsed(boolean z) {
        this.groupingUsed = z;
    }

    public int getMaximumIntegerDigits() {
        return this.maximumIntegerDigits;
    }

    public void setMaximumIntegerDigits(int i) {
        int max = Math.max(0, i);
        this.maximumIntegerDigits = max;
        if (this.minimumIntegerDigits > max) {
            this.minimumIntegerDigits = max;
        }
    }

    public int getMinimumIntegerDigits() {
        return this.minimumIntegerDigits;
    }

    public void setMinimumIntegerDigits(int i) {
        int max = Math.max(0, i);
        this.minimumIntegerDigits = max;
        if (max > this.maximumIntegerDigits) {
            this.maximumIntegerDigits = max;
        }
    }

    public int getMaximumFractionDigits() {
        return this.maximumFractionDigits;
    }

    public void setMaximumFractionDigits(int i) {
        int max = Math.max(0, i);
        this.maximumFractionDigits = max;
        if (max < this.minimumFractionDigits) {
            this.minimumFractionDigits = max;
        }
    }

    public int getMinimumFractionDigits() {
        return this.minimumFractionDigits;
    }

    public void setMinimumFractionDigits(int i) {
        int max = Math.max(0, i);
        this.minimumFractionDigits = max;
        if (this.maximumFractionDigits < max) {
            this.maximumFractionDigits = max;
        }
    }

    public Currency getCurrency() {
        throw new UnsupportedOperationException();
    }

    public void setCurrency(Currency currency) {
        throw new UnsupportedOperationException();
    }

    public RoundingMode getRoundingMode() {
        throw new UnsupportedOperationException();
    }

    public void setRoundingMode(RoundingMode roundingMode) {
        throw new UnsupportedOperationException();
    }

    private static NumberFormat getInstance(Locale locale, int i) {
        DecimalFormatData instance = DecimalFormatData.getInstance(locale);
        DecimalFormat decimalFormat = new DecimalFormat(new String[]{instance.getNumberPattern(), instance.getCurrencyPattern(), instance.getPercentPattern()}[i == 3 ? 0 : i], DecimalFormatSymbols.getInstance(locale));
        if (i == 3) {
            decimalFormat.setMaximumFractionDigits(0);
            decimalFormat.setDecimalSeparatorAlwaysShown(false);
            decimalFormat.setParseIntegerOnly(true);
        } else if (i == 1) {
            decimalFormat.adjustForCurrencyDefaultFractionDigits();
        }
        return decimalFormat;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int i;
        objectInputStream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            this.maximumIntegerDigits = this.maxIntegerDigits;
            this.minimumIntegerDigits = this.minIntegerDigits;
            this.maximumFractionDigits = this.maxFractionDigits;
            this.minimumFractionDigits = this.minFractionDigits;
        }
        int i2 = this.minimumIntegerDigits;
        if (i2 > this.maximumIntegerDigits || (i = this.minimumFractionDigits) > this.maximumFractionDigits || i2 < 0 || i < 0) {
            throw new InvalidObjectException("Digit count range invalid");
        }
        this.serialVersionOnStream = 1;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int i = this.maximumIntegerDigits;
        byte b = Byte.MAX_VALUE;
        this.maxIntegerDigits = i > 127 ? Byte.MAX_VALUE : (byte) i;
        int i2 = this.minimumIntegerDigits;
        this.minIntegerDigits = i2 > 127 ? Byte.MAX_VALUE : (byte) i2;
        int i3 = this.maximumFractionDigits;
        this.maxFractionDigits = i3 > 127 ? Byte.MAX_VALUE : (byte) i3;
        int i4 = this.minimumFractionDigits;
        if (i4 <= 127) {
            b = (byte) i4;
        }
        this.minFractionDigits = b;
        objectOutputStream.defaultWriteObject();
    }

    public static class Field extends Format.Field {
        public static final Field CURRENCY = new Field("currency");
        public static final Field DECIMAL_SEPARATOR = new Field("decimal separator");
        public static final Field EXPONENT = new Field("exponent");
        public static final Field EXPONENT_SIGN = new Field("exponent sign");
        public static final Field EXPONENT_SYMBOL = new Field("exponent symbol");
        public static final Field FRACTION = new Field("fraction");
        public static final Field GROUPING_SEPARATOR = new Field("grouping separator");
        public static final Field INTEGER = new Field("integer");
        public static final Field PERCENT = new Field("percent");
        public static final Field PERMILLE = new Field("per mille");
        public static final Field SIGN = new Field("sign");
        private static final Map<String, Field> instanceMap = new HashMap(11);
        private static final long serialVersionUID = 7494728892700160890L;

        protected Field(String str) {
            super(str);
            if (getClass() == Field.class) {
                instanceMap.put(str, this);
            }
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws InvalidObjectException {
            if (getClass() == Field.class) {
                Field field = instanceMap.get(getName());
                if (field != null) {
                    return field;
                }
                throw new InvalidObjectException("unknown attribute name");
            }
            throw new InvalidObjectException("subclass didn't correctly implement readResolve");
        }
    }
}
