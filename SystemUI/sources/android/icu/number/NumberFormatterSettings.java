package android.icu.number;

import android.icu.number.NumberFormatter;
import android.icu.number.NumberFormatterSettings;
import android.icu.text.DecimalFormatSymbols;
import android.icu.text.NumberingSystem;
import android.icu.util.MeasureUnit;
import java.math.RoundingMode;

public abstract class NumberFormatterSettings<T extends NumberFormatterSettings<?>> {
    NumberFormatterSettings() {
        throw new RuntimeException("Stub!");
    }

    public T notation(Notation notation) {
        throw new RuntimeException("Stub!");
    }

    public T unit(MeasureUnit measureUnit) {
        throw new RuntimeException("Stub!");
    }

    public T perUnit(MeasureUnit measureUnit) {
        throw new RuntimeException("Stub!");
    }

    public T precision(Precision precision) {
        throw new RuntimeException("Stub!");
    }

    public T roundingMode(RoundingMode roundingMode) {
        throw new RuntimeException("Stub!");
    }

    public T grouping(NumberFormatter.GroupingStrategy groupingStrategy) {
        throw new RuntimeException("Stub!");
    }

    public T integerWidth(IntegerWidth integerWidth) {
        throw new RuntimeException("Stub!");
    }

    public T symbols(DecimalFormatSymbols decimalFormatSymbols) {
        throw new RuntimeException("Stub!");
    }

    public T symbols(NumberingSystem numberingSystem) {
        throw new RuntimeException("Stub!");
    }

    public T unitWidth(NumberFormatter.UnitWidth unitWidth) {
        throw new RuntimeException("Stub!");
    }

    public T sign(NumberFormatter.SignDisplay signDisplay) {
        throw new RuntimeException("Stub!");
    }

    public T decimal(NumberFormatter.DecimalSeparatorDisplay decimalSeparatorDisplay) {
        throw new RuntimeException("Stub!");
    }

    public T scale(Scale scale) {
        throw new RuntimeException("Stub!");
    }

    public T usage(String str) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }
}
