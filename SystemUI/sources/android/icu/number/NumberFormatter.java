package android.icu.number;

import android.icu.util.ULocale;
import java.util.Locale;

public final class NumberFormatter {

    public enum DecimalSeparatorDisplay {
        AUTO,
        ALWAYS
    }

    public enum GroupingStrategy {
        OFF,
        MIN2,
        AUTO,
        ON_ALIGNED,
        THOUSANDS
    }

    public enum SignDisplay {
        AUTO,
        ALWAYS,
        NEVER,
        ACCOUNTING,
        ACCOUNTING_ALWAYS,
        EXCEPT_ZERO,
        ACCOUNTING_EXCEPT_ZERO
    }

    public enum UnitWidth {
        NARROW,
        SHORT,
        FULL_NAME,
        ISO_CODE,
        FORMAL,
        VARIANT,
        HIDDEN
    }

    private NumberFormatter() {
        throw new RuntimeException("Stub!");
    }

    public static UnlocalizedNumberFormatter with() {
        throw new RuntimeException("Stub!");
    }

    public static LocalizedNumberFormatter withLocale(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static LocalizedNumberFormatter withLocale(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }
}
