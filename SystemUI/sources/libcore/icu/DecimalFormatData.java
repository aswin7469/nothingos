package libcore.icu;

import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.icu.text.NumberFormat;
import android.icu.text.NumberingSystem;
import android.icu.util.ULocale;
import com.android.icu.text.ExtendedDecimalFormatSymbols;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DecimalFormatData {
    private static final ConcurrentHashMap<String, DecimalFormatData> CACHE = new ConcurrentHashMap<>(3);
    private final String NaN;
    private final String currencyPattern;
    private final char decimalSeparator;
    private final String exponentSeparator;
    private final char groupingSeparator;
    private final String infinity;
    private final String minusSign;
    private final char monetarySeparator;
    private final String numberPattern;
    private final char patternSeparator;
    private final String perMill;
    private final String percent;
    private final String percentPattern;
    private final char zeroDigit;

    private DecimalFormatData(Locale locale) {
        DecimalFormatSymbols instance = DecimalFormatSymbols.getInstance(locale);
        this.decimalSeparator = instance.getDecimalSeparator();
        this.groupingSeparator = instance.getGroupingSeparator();
        this.percent = instance.getPercentString();
        this.perMill = instance.getPerMillString();
        this.monetarySeparator = instance.getMonetaryDecimalSeparator();
        this.minusSign = instance.getMinusSignString();
        this.exponentSeparator = instance.getExponentSeparator();
        this.infinity = instance.getInfinity();
        this.NaN = instance.getNaN();
        this.zeroDigit = instance.getZeroDigit();
        this.patternSeparator = loadPatternSeparator(locale);
        this.numberPattern = ((DecimalFormat) NumberFormat.getInstance(locale, 0)).toPattern();
        this.currencyPattern = ((DecimalFormat) NumberFormat.getInstance(locale, 1)).toPattern();
        this.percentPattern = ((DecimalFormat) NumberFormat.getInstance(locale, 2)).toPattern();
    }

    public static DecimalFormatData getInstance(Locale locale) {
        Objects.requireNonNull(locale, "locale can't be null");
        Locale compatibleLocaleForBug159514442 = LocaleData.getCompatibleLocaleForBug159514442(locale);
        String languageTag = compatibleLocaleForBug159514442.toLanguageTag();
        ConcurrentHashMap<String, DecimalFormatData> concurrentHashMap = CACHE;
        DecimalFormatData decimalFormatData = concurrentHashMap.get(languageTag);
        if (decimalFormatData != null) {
            return decimalFormatData;
        }
        DecimalFormatData decimalFormatData2 = new DecimalFormatData(compatibleLocaleForBug159514442);
        DecimalFormatData putIfAbsent = concurrentHashMap.putIfAbsent(languageTag, decimalFormatData2);
        return putIfAbsent != null ? putIfAbsent : decimalFormatData2;
    }

    public static void initializeCacheInZygote() {
        getInstance(Locale.ROOT);
        getInstance(Locale.f700US);
        getInstance(Locale.getDefault());
    }

    public char getZeroDigit() {
        return this.zeroDigit;
    }

    public char getDecimalSeparator() {
        return this.decimalSeparator;
    }

    public char getGroupingSeparator() {
        return this.groupingSeparator;
    }

    public char getPatternSeparator() {
        return this.patternSeparator;
    }

    public String getPercent() {
        return this.percent;
    }

    public String getPerMill() {
        return this.perMill;
    }

    public char getMonetarySeparator() {
        return this.monetarySeparator;
    }

    public String getMinusSign() {
        return this.minusSign;
    }

    public String getExponentSeparator() {
        return this.exponentSeparator;
    }

    public String getInfinity() {
        return this.infinity;
    }

    public String getNaN() {
        return this.NaN;
    }

    public String getNumberPattern() {
        return this.numberPattern;
    }

    public String getCurrencyPattern() {
        return this.currencyPattern;
    }

    public String getPercentPattern() {
        return this.percentPattern;
    }

    private static char loadPatternSeparator(Locale locale) {
        ULocale forLocale = ULocale.forLocale(locale);
        NumberingSystem instance = NumberingSystem.getInstance(forLocale);
        if (instance == null || instance.getRadix() != 10 || instance.isAlgorithmic()) {
            instance = NumberingSystem.LATIN;
        }
        String localizedPatternSeparator = ExtendedDecimalFormatSymbols.getInstance(forLocale, instance).getLocalizedPatternSeparator();
        if (localizedPatternSeparator == null || localizedPatternSeparator.isEmpty()) {
            localizedPatternSeparator = NavigationBarInflaterView.GRAVITY_SEPARATOR;
        }
        return localizedPatternSeparator.charAt(0);
    }
}
