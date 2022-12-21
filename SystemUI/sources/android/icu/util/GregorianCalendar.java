package android.icu.util;

import java.util.Date;
import java.util.Locale;

public class GregorianCalendar extends Calendar {

    /* renamed from: AD */
    public static final int f28AD = 1;

    /* renamed from: BC */
    public static final int f29BC = 0;
    protected transient boolean invertGregorian;
    protected transient boolean isGregorian;

    public GregorianCalendar() {
        throw new RuntimeException("Stub!");
    }

    public GregorianCalendar(TimeZone timeZone) {
        throw new RuntimeException("Stub!");
    }

    public GregorianCalendar(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public GregorianCalendar(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public GregorianCalendar(TimeZone timeZone, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public GregorianCalendar(TimeZone timeZone, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public GregorianCalendar(int i, int i2, int i3) {
        throw new RuntimeException("Stub!");
    }

    public GregorianCalendar(int i, int i2, int i3, int i4, int i5) {
        throw new RuntimeException("Stub!");
    }

    public GregorianCalendar(int i, int i2, int i3, int i4, int i5, int i6) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleGetLimit(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void setGregorianChange(Date date) {
        throw new RuntimeException("Stub!");
    }

    public final Date getGregorianChange() {
        throw new RuntimeException("Stub!");
    }

    public boolean isLeapYear(int i) {
        throw new RuntimeException("Stub!");
    }

    public boolean isEquivalentTo(Calendar calendar) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public void roll(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public int getActualMinimum(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getActualMaximum(int i) {
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
    public void handleComputeFields(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleGetExtendedYear() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleComputeJulianDay(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleComputeMonthStart(int i, int i2, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public String getType() {
        throw new RuntimeException("Stub!");
    }
}
