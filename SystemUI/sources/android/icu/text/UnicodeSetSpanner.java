package android.icu.text;

import android.icu.text.UnicodeSet;

public class UnicodeSetSpanner {

    public enum CountMethod {
        WHOLE_SPAN,
        MIN_ELEMENTS
    }

    public enum TrimOption {
        LEADING,
        BOTH,
        TRAILING
    }

    public UnicodeSetSpanner(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet getUnicodeSet() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public int countIn(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public int countIn(CharSequence charSequence, CountMethod countMethod) {
        throw new RuntimeException("Stub!");
    }

    public int countIn(CharSequence charSequence, CountMethod countMethod, UnicodeSet.SpanCondition spanCondition) {
        throw new RuntimeException("Stub!");
    }

    public String deleteFrom(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public String deleteFrom(CharSequence charSequence, UnicodeSet.SpanCondition spanCondition) {
        throw new RuntimeException("Stub!");
    }

    public String replaceFrom(CharSequence charSequence, CharSequence charSequence2) {
        throw new RuntimeException("Stub!");
    }

    public String replaceFrom(CharSequence charSequence, CharSequence charSequence2, CountMethod countMethod) {
        throw new RuntimeException("Stub!");
    }

    public String replaceFrom(CharSequence charSequence, CharSequence charSequence2, CountMethod countMethod, UnicodeSet.SpanCondition spanCondition) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence trim(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence trim(CharSequence charSequence, TrimOption trimOption) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence trim(CharSequence charSequence, TrimOption trimOption, UnicodeSet.SpanCondition spanCondition) {
        throw new RuntimeException("Stub!");
    }
}
