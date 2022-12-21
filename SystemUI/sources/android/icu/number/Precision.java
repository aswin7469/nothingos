package android.icu.number;

import android.icu.util.Currency;
import java.math.BigDecimal;

public abstract class Precision {
    Precision() {
        throw new RuntimeException("Stub!");
    }

    public static Precision unlimited() {
        throw new RuntimeException("Stub!");
    }

    public static FractionPrecision integer() {
        throw new RuntimeException("Stub!");
    }

    public static FractionPrecision fixedFraction(int i) {
        throw new RuntimeException("Stub!");
    }

    public static FractionPrecision minFraction(int i) {
        throw new RuntimeException("Stub!");
    }

    public static FractionPrecision maxFraction(int i) {
        throw new RuntimeException("Stub!");
    }

    public static FractionPrecision minMaxFraction(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public static Precision fixedSignificantDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public static Precision minSignificantDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public static Precision maxSignificantDigits(int i) {
        throw new RuntimeException("Stub!");
    }

    public static Precision minMaxSignificantDigits(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public static Precision increment(BigDecimal bigDecimal) {
        throw new RuntimeException("Stub!");
    }

    public static CurrencyPrecision currency(Currency.CurrencyUsage currencyUsage) {
        throw new RuntimeException("Stub!");
    }
}
