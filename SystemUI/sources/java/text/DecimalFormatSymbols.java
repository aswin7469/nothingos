package java.text;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.util.Currency;
import java.util.Locale;
import libcore.icu.DecimalFormatData;
import libcore.icu.ICU;
import libcore.icu.LocaleData;

public class DecimalFormatSymbols implements Cloneable, Serializable {
    private static final int currentSerialVersion = 3;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("currencySymbol", String.class), new ObjectStreamField("decimalSeparator", Character.TYPE), new ObjectStreamField("digit", Character.TYPE), new ObjectStreamField("exponential", Character.TYPE), new ObjectStreamField("exponentialSeparator", String.class), new ObjectStreamField("groupingSeparator", Character.TYPE), new ObjectStreamField("infinity", String.class), new ObjectStreamField("intlCurrencySymbol", String.class), new ObjectStreamField("minusSign", Character.TYPE), new ObjectStreamField("monetarySeparator", Character.TYPE), new ObjectStreamField("NaN", String.class), new ObjectStreamField("patternSeparator", Character.TYPE), new ObjectStreamField("percent", Character.TYPE), new ObjectStreamField("perMill", Character.TYPE), new ObjectStreamField("serialVersionOnStream", Integer.TYPE), new ObjectStreamField("zeroDigit", Character.TYPE), new ObjectStreamField("locale", Locale.class), new ObjectStreamField("minusSignStr", String.class), new ObjectStreamField("percentStr", String.class)};
    static final long serialVersionUID = 5772796243397350300L;
    private String NaN;
    private transient android.icu.text.DecimalFormatSymbols cachedIcuDFS = null;
    private transient Currency currency;
    private volatile transient boolean currencyInitialized;
    private String currencySymbol;
    private char decimalSeparator;
    private char digit;
    private char exponential;
    private String exponentialSeparator;
    private char groupingSeparator;
    private String infinity;
    private String intlCurrencySymbol;
    private Locale locale;
    private char minusSign;
    private char monetarySeparator;
    private char patternSeparator;
    private char perMill;
    private char percent;
    private int serialVersionOnStream = 3;
    private char zeroDigit;

    public DecimalFormatSymbols() {
        initialize(Locale.getDefault(Locale.Category.FORMAT));
    }

    public DecimalFormatSymbols(Locale locale2) {
        initialize(locale2);
    }

    public static Locale[] getAvailableLocales() {
        return ICU.getAvailableLocales();
    }

    public static final DecimalFormatSymbols getInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DecimalFormatSymbols getInstance(Locale locale2) {
        return new DecimalFormatSymbols(locale2);
    }

    public char getZeroDigit() {
        return this.zeroDigit;
    }

    public void setZeroDigit(char c) {
        this.zeroDigit = c;
        this.cachedIcuDFS = null;
    }

    public char getGroupingSeparator() {
        return this.groupingSeparator;
    }

    public void setGroupingSeparator(char c) {
        this.groupingSeparator = c;
        this.cachedIcuDFS = null;
    }

    public char getDecimalSeparator() {
        return this.decimalSeparator;
    }

    public void setDecimalSeparator(char c) {
        this.decimalSeparator = c;
        this.cachedIcuDFS = null;
    }

    public char getPerMill() {
        return this.perMill;
    }

    public void setPerMill(char c) {
        this.perMill = c;
        this.cachedIcuDFS = null;
    }

    public char getPercent() {
        return this.percent;
    }

    public String getPercentString() {
        return String.valueOf(this.percent);
    }

    public void setPercent(char c) {
        this.percent = c;
        this.cachedIcuDFS = null;
    }

    public char getDigit() {
        return this.digit;
    }

    public void setDigit(char c) {
        this.digit = c;
        this.cachedIcuDFS = null;
    }

    public char getPatternSeparator() {
        return this.patternSeparator;
    }

    public void setPatternSeparator(char c) {
        this.patternSeparator = c;
        this.cachedIcuDFS = null;
    }

    public String getInfinity() {
        return this.infinity;
    }

    public void setInfinity(String str) {
        this.infinity = str;
        this.cachedIcuDFS = null;
    }

    public String getNaN() {
        return this.NaN;
    }

    public void setNaN(String str) {
        this.NaN = str;
        this.cachedIcuDFS = null;
    }

    public char getMinusSign() {
        return this.minusSign;
    }

    public String getMinusSignString() {
        return String.valueOf(this.minusSign);
    }

    public void setMinusSign(char c) {
        this.minusSign = c;
        this.cachedIcuDFS = null;
    }

    public String getCurrencySymbol() {
        initializeCurrency(this.locale);
        return this.currencySymbol;
    }

    public void setCurrencySymbol(String str) {
        initializeCurrency(this.locale);
        this.currencySymbol = str;
        this.cachedIcuDFS = null;
    }

    public String getInternationalCurrencySymbol() {
        initializeCurrency(this.locale);
        return this.intlCurrencySymbol;
    }

    public void setInternationalCurrencySymbol(String str) {
        initializeCurrency(this.locale);
        this.intlCurrencySymbol = str;
        this.currency = null;
        if (str != null) {
            try {
                Currency instance = Currency.getInstance(str);
                this.currency = instance;
                this.currencySymbol = instance.getSymbol(this.locale);
            } catch (IllegalArgumentException unused) {
            }
        }
        this.cachedIcuDFS = null;
    }

    public Currency getCurrency() {
        initializeCurrency(this.locale);
        return this.currency;
    }

    public void setCurrency(Currency currency2) {
        currency2.getClass();
        initializeCurrency(this.locale);
        this.currency = currency2;
        this.intlCurrencySymbol = currency2.getCurrencyCode();
        this.currencySymbol = currency2.getSymbol(this.locale);
        this.cachedIcuDFS = null;
    }

    public char getMonetaryDecimalSeparator() {
        return this.monetarySeparator;
    }

    public void setMonetaryDecimalSeparator(char c) {
        this.monetarySeparator = c;
        this.cachedIcuDFS = null;
    }

    /* access modifiers changed from: package-private */
    public char getExponentialSymbol() {
        return this.exponential;
    }

    public String getExponentSeparator() {
        return this.exponentialSeparator;
    }

    /* access modifiers changed from: package-private */
    public void setExponentialSymbol(char c) {
        this.exponential = c;
        this.cachedIcuDFS = null;
    }

    public void setExponentSeparator(String str) {
        str.getClass();
        this.exponentialSeparator = str;
    }

    public Object clone() {
        try {
            return (DecimalFormatSymbols) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
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
        DecimalFormatSymbols decimalFormatSymbols = (DecimalFormatSymbols) obj;
        if (this.zeroDigit == decimalFormatSymbols.zeroDigit && this.groupingSeparator == decimalFormatSymbols.groupingSeparator && this.decimalSeparator == decimalFormatSymbols.decimalSeparator && this.percent == decimalFormatSymbols.percent && this.perMill == decimalFormatSymbols.perMill && this.digit == decimalFormatSymbols.digit && this.minusSign == decimalFormatSymbols.minusSign && this.patternSeparator == decimalFormatSymbols.patternSeparator && this.infinity.equals(decimalFormatSymbols.infinity) && this.NaN.equals(decimalFormatSymbols.NaN) && getCurrencySymbol().equals(decimalFormatSymbols.getCurrencySymbol()) && this.intlCurrencySymbol.equals(decimalFormatSymbols.intlCurrencySymbol) && this.currency == decimalFormatSymbols.currency && this.monetarySeparator == decimalFormatSymbols.monetarySeparator && this.exponentialSeparator.equals(decimalFormatSymbols.exponentialSeparator) && this.locale.equals(decimalFormatSymbols.locale)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((((((((((((this.zeroDigit * '%') + this.groupingSeparator) * 37) + this.decimalSeparator) * 37) + this.percent) * 37) + this.perMill) * 37) + this.digit) * 37) + this.minusSign) * 37) + this.patternSeparator) * 37) + this.infinity.hashCode()) * 37) + this.NaN.hashCode()) * 37) + this.monetarySeparator) * 37) + this.exponentialSeparator.hashCode()) * 37) + this.locale.hashCode();
    }

    private void initialize(Locale locale2) {
        this.locale = locale2;
        if (locale2 != null) {
            DecimalFormatData instance = DecimalFormatData.getInstance(LocaleData.mapInvalidAndNullLocales(locale2));
            String[] strArr = {String.valueOf(instance.getDecimalSeparator()), String.valueOf(instance.getGroupingSeparator()), String.valueOf(instance.getPatternSeparator()), instance.getPercent(), String.valueOf(instance.getZeroDigit()), "#", instance.getMinusSign(), instance.getExponentSeparator(), instance.getPerMill(), instance.getInfinity(), instance.getNaN()};
            this.decimalSeparator = strArr[0].charAt(0);
            this.groupingSeparator = strArr[1].charAt(0);
            this.patternSeparator = strArr[2].charAt(0);
            this.percent = maybeStripMarkers(strArr[3], '%');
            this.zeroDigit = strArr[4].charAt(0);
            this.digit = strArr[5].charAt(0);
            this.minusSign = maybeStripMarkers(strArr[6], '-');
            this.exponential = strArr[7].charAt(0);
            this.exponentialSeparator = strArr[7];
            this.perMill = maybeStripMarkers(strArr[8], 8240);
            this.infinity = strArr[9];
            this.NaN = strArr[10];
            this.monetarySeparator = this.decimalSeparator;
            return;
        }
        throw new NullPointerException("locale");
    }

    private void initializeCurrency(Locale locale2) {
        if (!this.currencyInitialized) {
            if (!locale2.getCountry().isEmpty()) {
                try {
                    this.currency = Currency.getInstance(locale2);
                } catch (IllegalArgumentException unused) {
                }
            }
            Currency currency2 = this.currency;
            if (currency2 != null) {
                this.intlCurrencySymbol = currency2.getCurrencyCode();
                this.currencySymbol = this.currency.getSymbol(locale2);
            } else {
                this.intlCurrencySymbol = "XXX";
                try {
                    this.currency = Currency.getInstance("XXX");
                } catch (IllegalArgumentException unused2) {
                }
                this.currencySymbol = "¤";
            }
            this.currencyInitialized = true;
        }
    }

    public static char maybeStripMarkers(String str, char c) {
        int length = str.length();
        if (length >= 1) {
            boolean z = false;
            char c2 = 0;
            for (int i = 0; i < length; i++) {
                char charAt = str.charAt(i);
                if (!(charAt == 8206 || charAt == 8207 || charAt == 1564)) {
                    if (z) {
                        return c;
                    }
                    z = true;
                    c2 = charAt;
                }
            }
            if (z) {
                return c2;
            }
        }
        return c;
    }

    /* access modifiers changed from: protected */
    public android.icu.text.DecimalFormatSymbols getIcuDecimalFormatSymbols() {
        android.icu.text.DecimalFormatSymbols decimalFormatSymbols = this.cachedIcuDFS;
        if (decimalFormatSymbols != null) {
            return decimalFormatSymbols;
        }
        initializeCurrency(this.locale);
        android.icu.text.DecimalFormatSymbols decimalFormatSymbols2 = new android.icu.text.DecimalFormatSymbols(this.locale);
        this.cachedIcuDFS = decimalFormatSymbols2;
        decimalFormatSymbols2.setPlusSign('+');
        this.cachedIcuDFS.setZeroDigit(this.zeroDigit);
        this.cachedIcuDFS.setDigit(this.digit);
        this.cachedIcuDFS.setDecimalSeparator(this.decimalSeparator);
        this.cachedIcuDFS.setGroupingSeparator(this.groupingSeparator);
        this.cachedIcuDFS.setMonetaryGroupingSeparator(this.groupingSeparator);
        this.cachedIcuDFS.setPatternSeparator(this.patternSeparator);
        this.cachedIcuDFS.setPercent(this.percent);
        this.cachedIcuDFS.setPerMill(this.perMill);
        this.cachedIcuDFS.setMonetaryDecimalSeparator(this.monetarySeparator);
        this.cachedIcuDFS.setMinusSign(this.minusSign);
        this.cachedIcuDFS.setInfinity(this.infinity);
        this.cachedIcuDFS.setNaN(this.NaN);
        this.cachedIcuDFS.setExponentSeparator(this.exponentialSeparator);
        this.cachedIcuDFS.setPatternForCurrencySpacing(2, false, "");
        this.cachedIcuDFS.setPatternForCurrencySpacing(2, true, "");
        try {
            this.cachedIcuDFS.setCurrency(android.icu.util.Currency.getInstance(getCurrency().getCurrencyCode()));
        } catch (NullPointerException unused) {
            this.currency = Currency.getInstance("XXX");
        }
        this.cachedIcuDFS.setCurrencySymbol(this.currencySymbol);
        this.cachedIcuDFS.setInternationalCurrencySymbol(this.intlCurrencySymbol);
        return this.cachedIcuDFS;
    }

    protected static DecimalFormatSymbols fromIcuInstance(android.icu.text.DecimalFormatSymbols decimalFormatSymbols) {
        DecimalFormatSymbols decimalFormatSymbols2 = new DecimalFormatSymbols(decimalFormatSymbols.getLocale());
        decimalFormatSymbols2.setZeroDigit(decimalFormatSymbols.getZeroDigit());
        decimalFormatSymbols2.setDigit(decimalFormatSymbols.getDigit());
        decimalFormatSymbols2.setDecimalSeparator(decimalFormatSymbols.getDecimalSeparator());
        decimalFormatSymbols2.setGroupingSeparator(decimalFormatSymbols.getGroupingSeparator());
        decimalFormatSymbols2.setPatternSeparator(decimalFormatSymbols.getPatternSeparator());
        decimalFormatSymbols2.setPercent(decimalFormatSymbols.getPercent());
        decimalFormatSymbols2.setPerMill(decimalFormatSymbols.getPerMill());
        decimalFormatSymbols2.setMonetaryDecimalSeparator(decimalFormatSymbols.getMonetaryDecimalSeparator());
        decimalFormatSymbols2.setMinusSign(decimalFormatSymbols.getMinusSign());
        decimalFormatSymbols2.setInfinity(decimalFormatSymbols.getInfinity());
        decimalFormatSymbols2.setNaN(decimalFormatSymbols.getNaN());
        decimalFormatSymbols2.setExponentSeparator(decimalFormatSymbols.getExponentSeparator());
        try {
            if (decimalFormatSymbols.getCurrency() != null) {
                decimalFormatSymbols2.setCurrency(Currency.getInstance(decimalFormatSymbols.getCurrency().getCurrencyCode()));
            } else {
                decimalFormatSymbols2.setCurrency(Currency.getInstance("XXX"));
            }
        } catch (IllegalArgumentException unused) {
            decimalFormatSymbols2.setCurrency(Currency.getInstance("XXX"));
        }
        decimalFormatSymbols2.setInternationalCurrencySymbol(decimalFormatSymbols.getInternationalCurrencySymbol());
        decimalFormatSymbols2.setCurrencySymbol(decimalFormatSymbols.getCurrencySymbol());
        return decimalFormatSymbols2;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("currencySymbol", (Object) this.currencySymbol);
        putFields.put("decimalSeparator", getDecimalSeparator());
        putFields.put("digit", getDigit());
        putFields.put("exponential", this.exponentialSeparator.charAt(0));
        putFields.put("exponentialSeparator", (Object) this.exponentialSeparator);
        putFields.put("groupingSeparator", getGroupingSeparator());
        putFields.put("infinity", (Object) this.infinity);
        putFields.put("intlCurrencySymbol", (Object) this.intlCurrencySymbol);
        putFields.put("monetarySeparator", getMonetaryDecimalSeparator());
        putFields.put("NaN", (Object) this.NaN);
        putFields.put("patternSeparator", getPatternSeparator());
        putFields.put("perMill", getPerMill());
        putFields.put("serialVersionOnStream", 3);
        putFields.put("zeroDigit", getZeroDigit());
        putFields.put("locale", (Object) this.locale);
        putFields.put("minusSign", this.minusSign);
        putFields.put("percent", this.percent);
        putFields.put("minusSignStr", (Object) getMinusSignString());
        putFields.put("percentStr", (Object) getPercentString());
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        int i = readFields.get("serialVersionOnStream", 0);
        this.currencySymbol = (String) readFields.get("currencySymbol", (Object) "");
        setDecimalSeparator(readFields.get("decimalSeparator", '.'));
        setDigit(readFields.get("digit", '#'));
        setGroupingSeparator(readFields.get("groupingSeparator", ','));
        this.infinity = (String) readFields.get("infinity", (Object) "");
        this.intlCurrencySymbol = (String) readFields.get("intlCurrencySymbol", (Object) "");
        this.NaN = (String) readFields.get("NaN", (Object) "");
        setPatternSeparator(readFields.get("patternSeparator", ';'));
        String str = (String) readFields.get("minusSignStr", (Object) null);
        if (str != null) {
            this.minusSign = str.charAt(0);
        } else {
            setMinusSign(readFields.get("minusSign", '-'));
        }
        String str2 = (String) readFields.get("percentStr", (Object) null);
        if (str2 != null) {
            this.percent = str2.charAt(0);
        } else {
            setPercent(readFields.get("percent", '%'));
        }
        setPerMill(readFields.get("perMill", 8240));
        setZeroDigit(readFields.get("zeroDigit", '0'));
        this.locale = (Locale) readFields.get("locale", (Object) null);
        if (i == 0) {
            setMonetaryDecimalSeparator(getDecimalSeparator());
        } else {
            setMonetaryDecimalSeparator(readFields.get("monetarySeparator", '.'));
        }
        if (i == 0) {
            this.exponentialSeparator = "E";
        } else if (i < 3) {
            setExponentSeparator(String.valueOf(readFields.get("exponential", 'E')));
        } else {
            setExponentSeparator((String) readFields.get("exponentialSeparator", (Object) "E"));
        }
        String str3 = this.intlCurrencySymbol;
        if (str3 != null) {
            try {
                this.currency = Currency.getInstance(str3);
                this.currencyInitialized = true;
            } catch (IllegalArgumentException unused) {
                this.currency = null;
            }
        }
    }
}
