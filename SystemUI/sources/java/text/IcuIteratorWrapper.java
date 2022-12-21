package java.text;

import android.icu.text.BreakIterator;

class IcuIteratorWrapper extends BreakIterator {
    private BreakIterator wrapped;

    IcuIteratorWrapper(BreakIterator breakIterator) {
        this.wrapped = breakIterator;
    }

    public Object clone() {
        IcuIteratorWrapper icuIteratorWrapper = (IcuIteratorWrapper) super.clone();
        icuIteratorWrapper.wrapped = (BreakIterator) this.wrapped.clone();
        return icuIteratorWrapper;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof IcuIteratorWrapper)) {
            return false;
        }
        return this.wrapped.equals(((IcuIteratorWrapper) obj).wrapped);
    }

    public String toString() {
        return this.wrapped.toString();
    }

    public int hashCode() {
        return this.wrapped.hashCode();
    }

    public int first() {
        return this.wrapped.first();
    }

    public int last() {
        return this.wrapped.last();
    }

    public int next(int i) {
        return this.wrapped.next(i);
    }

    public int next() {
        return this.wrapped.next();
    }

    public int previous() {
        return this.wrapped.previous();
    }

    protected static final void checkOffset(int i, CharacterIterator characterIterator) {
        if (i < characterIterator.getBeginIndex() || i > characterIterator.getEndIndex()) {
            throw new IllegalArgumentException("offset out of bounds");
        }
    }

    public int following(int i) {
        checkOffset(i, getText());
        return this.wrapped.following(i);
    }

    public int preceding(int i) {
        checkOffset(i, getText());
        return this.wrapped.preceding(i);
    }

    public boolean isBoundary(int i) {
        checkOffset(i, getText());
        return this.wrapped.isBoundary(i);
    }

    public int current() {
        return this.wrapped.current();
    }

    public CharacterIterator getText() {
        return this.wrapped.getText();
    }

    public void setText(String str) {
        this.wrapped.setText(str);
    }

    public void setText(CharacterIterator characterIterator) {
        characterIterator.current();
        this.wrapped.setText(characterIterator);
    }
}
