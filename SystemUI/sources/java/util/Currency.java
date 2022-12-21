package java.util;

import java.p026io.Serializable;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import libcore.icu.ICU;
import sun.util.locale.BaseLocale;

public final class Currency implements Serializable {
    private static HashSet<Currency> available = null;
    private static ConcurrentMap<String, Currency> instances = new ConcurrentHashMap(7);
    private static final long serialVersionUID = -158308464356906721L;
    private final String currencyCode;
    private final transient android.icu.util.Currency icuCurrency;

    private Currency(android.icu.util.Currency currency) {
        this.icuCurrency = currency;
        this.currencyCode = currency.getCurrencyCode();
    }

    public static Currency getInstance(String str) {
        Currency currency = instances.get(str);
        if (currency != null) {
            return currency;
        }
        android.icu.util.Currency instance = android.icu.util.Currency.getInstance(str);
        if (instance == null) {
            return null;
        }
        Currency currency2 = new Currency(instance);
        Currency putIfAbsent = instances.putIfAbsent(str, currency2);
        return putIfAbsent != null ? putIfAbsent : currency2;
    }

    public static Currency getInstance(Locale locale) {
        String country = locale.getCountry();
        country.getClass();
        android.icu.util.Currency instance = android.icu.util.Currency.getInstance(locale);
        String variant = locale.getVariant();
        if (!variant.isEmpty() && (variant.equals("EURO") || variant.equals("HK") || variant.equals("PREEURO"))) {
            country = country + BaseLocale.SEP + variant;
        }
        if (ICU.isIsoCountry(country)) {
            String currencyCode2 = ICU.getCurrencyCode(country);
            if (currencyCode2 == null || instance == null || instance.getCurrencyCode().equals("XXX")) {
                return null;
            }
            return getInstance(currencyCode2);
        }
        throw new IllegalArgumentException("Unsupported ISO 3166 country: " + locale);
    }

    public static Set<Currency> getAvailableCurrencies() {
        synchronized (Currency.class) {
            if (available == null) {
                available = new HashSet<>();
                for (android.icu.util.Currency next : android.icu.util.Currency.getAvailableCurrencies()) {
                    Currency instance = getInstance(next.getCurrencyCode());
                    if (instance == null) {
                        instance = new Currency(next);
                        instances.put(instance.currencyCode, instance);
                    }
                    available.add(instance);
                }
            }
        }
        return (Set) available.clone();
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public String getSymbol() {
        return getSymbol(Locale.getDefault(Locale.Category.DISPLAY));
    }

    public String getSymbol(Locale locale) {
        if (locale != null) {
            return this.icuCurrency.getSymbol(locale);
        }
        throw new NullPointerException("locale == null");
    }

    public int getDefaultFractionDigits() {
        if (this.icuCurrency.getCurrencyCode().equals("XXX")) {
            return -1;
        }
        return this.icuCurrency.getDefaultFractionDigits();
    }

    public int getNumericCode() {
        return this.icuCurrency.getNumericCode();
    }

    public String getDisplayName() {
        return getDisplayName(Locale.getDefault(Locale.Category.DISPLAY));
    }

    public String getDisplayName(Locale locale) {
        return this.icuCurrency.getDisplayName((Locale) Objects.requireNonNull(locale));
    }

    public String toString() {
        return this.icuCurrency.toString();
    }

    private Object readResolve() {
        return getInstance(this.currencyCode);
    }
}
