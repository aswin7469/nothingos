package android.icu.text;

import android.icu.util.ULocale;
import java.text.CharacterIterator;
import java.util.Locale;

public final class StringSearch extends SearchIterator {
    public StringSearch(String str, CharacterIterator characterIterator, RuleBasedCollator ruleBasedCollator, BreakIterator breakIterator) {
        super((CharacterIterator) null, (BreakIterator) null);
        throw new RuntimeException("Stub!");
    }

    public StringSearch(String str, CharacterIterator characterIterator, RuleBasedCollator ruleBasedCollator) {
        super((CharacterIterator) null, (BreakIterator) null);
        throw new RuntimeException("Stub!");
    }

    public StringSearch(String str, CharacterIterator characterIterator, Locale locale) {
        super((CharacterIterator) null, (BreakIterator) null);
        throw new RuntimeException("Stub!");
    }

    public StringSearch(String str, CharacterIterator characterIterator, ULocale uLocale) {
        super((CharacterIterator) null, (BreakIterator) null);
        throw new RuntimeException("Stub!");
    }

    public StringSearch(String str, String str2) {
        super((CharacterIterator) null, (BreakIterator) null);
        throw new RuntimeException("Stub!");
    }

    public RuleBasedCollator getCollator() {
        throw new RuntimeException("Stub!");
    }

    public void setCollator(RuleBasedCollator ruleBasedCollator) {
        throw new RuntimeException("Stub!");
    }

    public String getPattern() {
        throw new RuntimeException("Stub!");
    }

    public void setPattern(String str) {
        throw new RuntimeException("Stub!");
    }

    public boolean isCanonical() {
        throw new RuntimeException("Stub!");
    }

    public void setCanonical(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public void setTarget(CharacterIterator characterIterator) {
        throw new RuntimeException("Stub!");
    }

    public int getIndex() {
        throw new RuntimeException("Stub!");
    }

    public void setIndex(int i) {
        throw new RuntimeException("Stub!");
    }

    public void reset() {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handleNext(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public int handlePrevious(int i) {
        throw new RuntimeException("Stub!");
    }
}
