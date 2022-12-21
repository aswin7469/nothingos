package android.icu.text;

import android.icu.util.CurrencyAmount;
import android.icu.util.ULocale;
import java.text.ParsePosition;
import java.util.Locale;

public class CompactDecimalFormat extends DecimalFormat {

    public enum CompactStyle {
        SHORT,
        LONG
    }

    CompactDecimalFormat() {
        throw new RuntimeException("Stub!");
    }

    public static CompactDecimalFormat getInstance(ULocale uLocale, CompactStyle compactStyle) {
        throw new RuntimeException("Stub!");
    }

    public static CompactDecimalFormat getInstance(Locale locale, CompactStyle compactStyle) {
        throw new RuntimeException("Stub!");
    }

    public Number parse(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public CurrencyAmount parseCurrency(CharSequence charSequence, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }
}
