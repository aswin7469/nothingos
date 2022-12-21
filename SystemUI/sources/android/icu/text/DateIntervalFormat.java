package android.icu.text;

import android.icu.text.DisplayContext;
import android.icu.util.Calendar;
import android.icu.util.DateInterval;
import android.icu.util.TimeZone;
import android.icu.util.ULocale;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Locale;

public class DateIntervalFormat extends UFormat {
    DateIntervalFormat() {
        throw new RuntimeException("Stub!");
    }

    public static final DateIntervalFormat getInstance(String str) {
        throw new RuntimeException("Stub!");
    }

    public static final DateIntervalFormat getInstance(String str, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static final DateIntervalFormat getInstance(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static final DateIntervalFormat getInstance(String str, DateIntervalInfo dateIntervalInfo) {
        throw new RuntimeException("Stub!");
    }

    public static final DateIntervalFormat getInstance(String str, Locale locale, DateIntervalInfo dateIntervalInfo) {
        throw new RuntimeException("Stub!");
    }

    public static final DateIntervalFormat getInstance(String str, ULocale uLocale, DateIntervalInfo dateIntervalInfo) {
        throw new RuntimeException("Stub!");
    }

    public synchronized Object clone() {
        throw new RuntimeException("Stub!");
    }

    public final StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public final StringBuffer format(DateInterval dateInterval, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public FormattedDateInterval formatToValue(DateInterval dateInterval) {
        throw new RuntimeException("Stub!");
    }

    public final StringBuffer format(Calendar calendar, Calendar calendar2, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public FormattedDateInterval formatToValue(Calendar calendar, Calendar calendar2) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public Object parseObject(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public DateIntervalInfo getDateIntervalInfo() {
        throw new RuntimeException("Stub!");
    }

    public void setDateIntervalInfo(DateIntervalInfo dateIntervalInfo) {
        throw new RuntimeException("Stub!");
    }

    public TimeZone getTimeZone() {
        throw new RuntimeException("Stub!");
    }

    public void setTimeZone(TimeZone timeZone) {
        throw new RuntimeException("Stub!");
    }

    public void setContext(DisplayContext displayContext) {
        throw new RuntimeException("Stub!");
    }

    public DisplayContext getContext(DisplayContext.Type type) {
        throw new RuntimeException("Stub!");
    }

    public synchronized DateFormat getDateFormat() {
        throw new RuntimeException("Stub!");
    }

    public static final class FormattedDateInterval implements FormattedValue {
        FormattedDateInterval() {
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
