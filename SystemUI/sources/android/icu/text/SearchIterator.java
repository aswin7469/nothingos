package android.icu.text;

import java.text.CharacterIterator;

public abstract class SearchIterator {
    public static final int DONE = -1;
    protected BreakIterator breakIterator;
    protected int matchLength;
    protected CharacterIterator targetText;

    public enum ElementComparisonType {
        STANDARD_ELEMENT_COMPARISON,
        PATTERN_BASE_WEIGHT_IS_WILDCARD,
        ANY_BASE_WEIGHT_IS_WILDCARD
    }

    public abstract int getIndex();

    /* access modifiers changed from: protected */
    public abstract int handleNext(int i);

    /* access modifiers changed from: protected */
    public abstract int handlePrevious(int i);

    protected SearchIterator(CharacterIterator characterIterator, BreakIterator breakIterator2) {
        throw new RuntimeException("Stub!");
    }

    public void setIndex(int i) {
        throw new RuntimeException("Stub!");
    }

    public void setOverlapping(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public void setBreakIterator(BreakIterator breakIterator2) {
        throw new RuntimeException("Stub!");
    }

    public void setTarget(CharacterIterator characterIterator) {
        throw new RuntimeException("Stub!");
    }

    public int getMatchStart() {
        throw new RuntimeException("Stub!");
    }

    public int getMatchLength() {
        throw new RuntimeException("Stub!");
    }

    public BreakIterator getBreakIterator() {
        throw new RuntimeException("Stub!");
    }

    public CharacterIterator getTarget() {
        throw new RuntimeException("Stub!");
    }

    public String getMatchedText() {
        throw new RuntimeException("Stub!");
    }

    public int next() {
        throw new RuntimeException("Stub!");
    }

    public int previous() {
        throw new RuntimeException("Stub!");
    }

    public boolean isOverlapping() {
        throw new RuntimeException("Stub!");
    }

    public void reset() {
        throw new RuntimeException("Stub!");
    }

    public final int first() {
        throw new RuntimeException("Stub!");
    }

    public final int following(int i) {
        throw new RuntimeException("Stub!");
    }

    public final int last() {
        throw new RuntimeException("Stub!");
    }

    public final int preceding(int i) {
        throw new RuntimeException("Stub!");
    }

    /* access modifiers changed from: protected */
    public void setMatchLength(int i) {
        throw new RuntimeException("Stub!");
    }

    public void setElementComparisonType(ElementComparisonType elementComparisonType) {
        throw new RuntimeException("Stub!");
    }

    public ElementComparisonType getElementComparisonType() {
        throw new RuntimeException("Stub!");
    }
}
