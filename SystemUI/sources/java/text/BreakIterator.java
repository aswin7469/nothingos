package java.text;

import java.util.Locale;

public abstract class BreakIterator implements Cloneable {
    public static final int DONE = -1;

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
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    public int preceding(int i) {
        int following = following(i);
        while (following >= i && following != -1) {
            following = previous();
        }
        return following;
    }

    public boolean isBoundary(int i) {
        if (i == 0) {
            return true;
        }
        int following = following(i - 1);
        if (following != -1) {
            return following == i;
        }
        throw new IllegalArgumentException();
    }

    public void setText(String str) {
        setText((CharacterIterator) new StringCharacterIterator(str));
    }

    public static BreakIterator getWordInstance() {
        return getWordInstance(Locale.getDefault());
    }

    public static BreakIterator getWordInstance(Locale locale) {
        return new IcuIteratorWrapper(android.icu.text.BreakIterator.getWordInstance(locale));
    }

    public static BreakIterator getLineInstance() {
        return getLineInstance(Locale.getDefault());
    }

    public static BreakIterator getLineInstance(Locale locale) {
        return new IcuIteratorWrapper(android.icu.text.BreakIterator.getLineInstance(locale));
    }

    public static BreakIterator getCharacterInstance() {
        return getCharacterInstance(Locale.getDefault());
    }

    public static BreakIterator getCharacterInstance(Locale locale) {
        return new IcuIteratorWrapper(android.icu.text.BreakIterator.getCharacterInstance(locale));
    }

    public static BreakIterator getSentenceInstance() {
        return getSentenceInstance(Locale.getDefault());
    }

    public static BreakIterator getSentenceInstance(Locale locale) {
        return new IcuIteratorWrapper(android.icu.text.BreakIterator.getSentenceInstance(locale));
    }

    public static synchronized Locale[] getAvailableLocales() {
        Locale[] availableLocales;
        synchronized (BreakIterator.class) {
            availableLocales = android.icu.text.BreakIterator.getAvailableLocales();
        }
        return availableLocales;
    }
}
