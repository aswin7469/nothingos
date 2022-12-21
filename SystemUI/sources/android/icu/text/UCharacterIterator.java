package android.icu.text;

import java.text.CharacterIterator;

public abstract class UCharacterIterator implements Cloneable {
    public static final int DONE = -1;

    public abstract int current();

    public abstract int getIndex();

    public abstract int getLength();

    public abstract int getText(char[] cArr, int i);

    public abstract int next();

    public abstract int previous();

    public abstract void setIndex(int i);

    protected UCharacterIterator() {
        throw new RuntimeException("Stub!");
    }

    public static final UCharacterIterator getInstance(Replaceable replaceable) {
        throw new RuntimeException("Stub!");
    }

    public static final UCharacterIterator getInstance(String str) {
        throw new RuntimeException("Stub!");
    }

    public static final UCharacterIterator getInstance(char[] cArr) {
        throw new RuntimeException("Stub!");
    }

    public static final UCharacterIterator getInstance(char[] cArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public static final UCharacterIterator getInstance(StringBuffer stringBuffer) {
        throw new RuntimeException("Stub!");
    }

    public static final UCharacterIterator getInstance(CharacterIterator characterIterator) {
        throw new RuntimeException("Stub!");
    }

    public CharacterIterator getCharacterIterator() {
        throw new RuntimeException("Stub!");
    }

    public int currentCodePoint() {
        throw new RuntimeException("Stub!");
    }

    public int nextCodePoint() {
        throw new RuntimeException("Stub!");
    }

    public int previousCodePoint() {
        throw new RuntimeException("Stub!");
    }

    public void setToLimit() {
        throw new RuntimeException("Stub!");
    }

    public void setToStart() {
        throw new RuntimeException("Stub!");
    }

    public final int getText(char[] cArr) {
        throw new RuntimeException("Stub!");
    }

    public String getText() {
        throw new RuntimeException("Stub!");
    }

    public int moveIndex(int i) {
        throw new RuntimeException("Stub!");
    }

    public int moveCodePointIndex(int i) {
        throw new RuntimeException("Stub!");
    }

    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
}
