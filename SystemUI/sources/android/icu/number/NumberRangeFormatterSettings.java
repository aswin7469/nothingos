package android.icu.number;

import android.icu.number.NumberRangeFormatter;
import android.icu.number.NumberRangeFormatterSettings;

public abstract class NumberRangeFormatterSettings<T extends NumberRangeFormatterSettings<?>> {
    NumberRangeFormatterSettings() {
        throw new RuntimeException("Stub!");
    }

    public T numberFormatterBoth(UnlocalizedNumberFormatter unlocalizedNumberFormatter) {
        throw new RuntimeException("Stub!");
    }

    public T numberFormatterFirst(UnlocalizedNumberFormatter unlocalizedNumberFormatter) {
        throw new RuntimeException("Stub!");
    }

    public T numberFormatterSecond(UnlocalizedNumberFormatter unlocalizedNumberFormatter) {
        throw new RuntimeException("Stub!");
    }

    public T collapse(NumberRangeFormatter.RangeCollapse rangeCollapse) {
        throw new RuntimeException("Stub!");
    }

    public T identityFallback(NumberRangeFormatter.RangeIdentityFallback rangeIdentityFallback) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }
}
