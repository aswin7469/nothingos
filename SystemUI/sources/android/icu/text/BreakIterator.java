package android.icu.text;

import android.icu.util.ULocale;
import java.text.CharacterIterator;
import java.util.Locale;

public abstract class BreakIterator implements Cloneable {
    public static final int DONE = -1;
    public static final int KIND_CHARACTER = 0;
    public static final int KIND_LINE = 2;
    public static final int KIND_SENTENCE = 3;
    @Deprecated
    public static final int KIND_TITLE = 4;
    public static final int KIND_WORD = 1;
    public static final int WORD_IDEO = 400;
    public static final int WORD_IDEO_LIMIT = 500;
    public static final int WORD_KANA = 300;
    public static final int WORD_KANA_LIMIT = 400;
    public static final int WORD_LETTER = 200;
    public static final int WORD_LETTER_LIMIT = 300;
    public static final int WORD_NONE = 0;
    public static final int WORD_NONE_LIMIT = 100;
    public static final int WORD_NUMBER = 100;
    public static final int WORD_NUMBER_LIMIT = 200;

    public abstract int current();

    public abstract int first();

    public abstract int following(int i);

    public abstract CharacterIterator getText();

    public abstract int last();

    public abstract int next();

    public abstract int next(int i);

    public abstract int previous();

    public abstract void setText(CharacterIterator characterIterator);

    protected BreakIterator() {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public int preceding(int i) {
        throw new RuntimeException("Stub!");
    }

    public boolean isBoundary(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getRuleStatus() {
        throw new RuntimeException("Stub!");
    }

    public int getRuleStatusVec(int[] iArr) {
        throw new RuntimeException("Stub!");
    }

    public void setText(String str) {
        throw new RuntimeException("Stub!");
    }

    public void setText(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getWordInstance() {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getWordInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getWordInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getLineInstance() {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getLineInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getLineInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getCharacterInstance() {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getCharacterInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getCharacterInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getSentenceInstance() {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getSentenceInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static BreakIterator getSentenceInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static BreakIterator getTitleInstance() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static BreakIterator getTitleInstance(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static BreakIterator getTitleInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static synchronized Locale[] getAvailableLocales() {
        synchronized (BreakIterator.class) {
            throw new RuntimeException("Stub!");
        }
    }
}
