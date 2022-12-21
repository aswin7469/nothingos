package android.icu.util;

import java.util.Date;
import java.util.Locale;

public class IslamicCalendar extends Calendar {
    public static final int DHU_AL_HIJJAH = 11;
    public static final int DHU_AL_QIDAH = 10;
    public static final int JUMADA_1 = 4;
    public static final int JUMADA_2 = 5;
    public static final int MUHARRAM = 0;
    public static final int RABI_1 = 2;
    public static final int RABI_2 = 3;
    public static final int RAJAB = 6;
    public static final int RAMADAN = 8;
    public static final int SAFAR = 1;
    public static final int SHABAN = 7;
    public static final int SHAWWAL = 9;

    public enum CalculationType {
        ISLAMIC,
        ISLAMIC_CIVIL,
        ISLAMIC_UMALQURA,
        ISLAMIC_TBLA
    }

    public IslamicCalendar() {
        throw new RuntimeException("Stub!");
    }

    public IslamicCalendar(TimeZone timeZone) {
        throw new RuntimeException("Stub!");
    }

    public IslamicCalendar(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public IslamicCalendar(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public IslamicCalendar(TimeZone timeZone, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public IslamicCalendar(TimeZone timeZone, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public IslamicCalendar(Date date) {
        throw new RuntimeException("Stub!");
    }

    public IslamicCalendar(int i, int i2, int i3) {
        throw new RuntimeException("Stub!");
    }

    public IslamicCalendar(int i, int i2, int i3, int i4, int i5, int i6) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleGetLimit(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleGetMonthLength(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleGetYearLength(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleComputeMonthStart(int i, int i2, boolean z) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleGetExtendedYear() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void handleComputeFields(int i) {
        throw new RuntimeException("Stub!");
    }

    public void setCalculationType(CalculationType calculationType) {
        throw new RuntimeException("Stub!");
    }

    public CalculationType getCalculationType() {
        throw new RuntimeException("Stub!");
    }

    public String getType() {
        throw new RuntimeException("Stub!");
    }
}
