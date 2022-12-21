package android.icu.text;

import android.icu.util.Calendar;
import android.icu.util.ULocale;
import java.p026io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

public class DateFormatSymbols implements Serializable, Cloneable {
    public static final int ABBREVIATED = 0;
    public static final int FORMAT = 0;
    public static final int NARROW = 2;
    public static final int SHORT = 3;
    public static final int STANDALONE = 1;
    public static final int WIDE = 1;

    public DateFormatSymbols() {
        throw new RuntimeException("Stub!");
    }

    public DateFormatSymbols(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public DateFormatSymbols(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public DateFormatSymbols(Calendar calendar, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public DateFormatSymbols(Calendar calendar, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public DateFormatSymbols(Class<? extends Calendar> cls, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public DateFormatSymbols(Class<? extends Calendar> cls, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public DateFormatSymbols(ResourceBundle resourceBundle, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public DateFormatSymbols(ResourceBundle resourceBundle, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static DateFormatSymbols getInstance() {
        throw new RuntimeException("Stub!");
    }

    public static DateFormatSymbols getInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static DateFormatSymbols getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static Locale[] getAvailableLocales() {
        throw new RuntimeException("Stub!");
    }

    public String[] getEras() {
        throw new RuntimeException("Stub!");
    }

    public void setEras(String[] strArr) {
        throw new RuntimeException("Stub!");
    }

    public String[] getEraNames() {
        throw new RuntimeException("Stub!");
    }

    public void setEraNames(String[] strArr) {
        throw new RuntimeException("Stub!");
    }

    public String[] getNarrowEras() {
        throw new RuntimeException("Stub!");
    }

    public void setNarrowEras(String[] strArr) {
        throw new RuntimeException("Stub!");
    }

    public String[] getMonths() {
        throw new RuntimeException("Stub!");
    }

    public String[] getMonths(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void setMonths(String[] strArr) {
        throw new RuntimeException("Stub!");
    }

    public void setMonths(String[] strArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public String[] getShortMonths() {
        throw new RuntimeException("Stub!");
    }

    public void setShortMonths(String[] strArr) {
        throw new RuntimeException("Stub!");
    }

    public String[] getWeekdays() {
        throw new RuntimeException("Stub!");
    }

    public String[] getWeekdays(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void setWeekdays(String[] strArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void setWeekdays(String[] strArr) {
        throw new RuntimeException("Stub!");
    }

    public String[] getShortWeekdays() {
        throw new RuntimeException("Stub!");
    }

    public void setShortWeekdays(String[] strArr) {
        throw new RuntimeException("Stub!");
    }

    public String[] getQuarters(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void setQuarters(String[] strArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public String[] getYearNames(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void setYearNames(String[] strArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public String[] getZodiacNames(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void setZodiacNames(String[] strArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public String[] getAmPmStrings() {
        throw new RuntimeException("Stub!");
    }

    public void setAmPmStrings(String[] strArr) {
        throw new RuntimeException("Stub!");
    }

    public String[][] getZoneStrings() {
        throw new RuntimeException("Stub!");
    }

    public void setZoneStrings(String[][] strArr) {
        throw new RuntimeException("Stub!");
    }

    public String getLocalPatternChars() {
        throw new RuntimeException("Stub!");
    }

    public void setLocalPatternChars(String str) {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void initializeData(ULocale uLocale, String str) {
        throw new RuntimeException("Stub!");
    }
}
