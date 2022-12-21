package android.icu.text;

import android.icu.util.ULocale;
import java.text.AttributedCharacterIterator;
import java.util.Collection;
import java.util.Locale;

public final class ListFormatter {

    public enum Type {
        AND,
        OR,
        UNITS
    }

    public enum Width {
        WIDE,
        SHORT,
        NARROW
    }

    ListFormatter() {
        throw new RuntimeException("Stub!");
    }

    public static ListFormatter getInstance(ULocale uLocale, Type type, Width width) {
        throw new RuntimeException("Stub!");
    }

    public static ListFormatter getInstance(Locale locale, Type type, Width width) {
        throw new RuntimeException("Stub!");
    }

    public static ListFormatter getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static ListFormatter getInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static ListFormatter getInstance() {
        throw new RuntimeException("Stub!");
    }

    public String format(Object... objArr) {
        throw new RuntimeException("Stub!");
    }

    public String format(Collection<?> collection) {
        throw new RuntimeException("Stub!");
    }

    public FormattedList formatToValue(Object... objArr) {
        throw new RuntimeException("Stub!");
    }

    public FormattedList formatToValue(Collection<?> collection) {
        throw new RuntimeException("Stub!");
    }

    public String getPatternForNumItems(int i) {
        throw new RuntimeException("Stub!");
    }

    public static final class FormattedList implements FormattedValue {
        FormattedList() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }

        public int length() {
            throw new RuntimeException("Stub!");
        }

        public char charAt(int i) {
            throw new RuntimeException("Stub!");
        }

        public CharSequence subSequence(int i, int i2) {
            throw new RuntimeException("Stub!");
        }

        public <A extends Appendable> A appendTo(A a) {
            throw new RuntimeException("Stub!");
        }

        public boolean nextPosition(ConstrainedFieldPosition constrainedFieldPosition) {
            throw new RuntimeException("Stub!");
        }

        public AttributedCharacterIterator toCharacterIterator() {
            throw new RuntimeException("Stub!");
        }
    }
}
