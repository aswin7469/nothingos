package android.icu.text;

public abstract class UnicodeFilter implements UnicodeMatcher {
    public abstract boolean contains(int i);

    UnicodeFilter() {
        throw new RuntimeException("Stub!");
    }

    public int matches(Replaceable replaceable, int[] iArr, int i, boolean z) {
        throw new RuntimeException("Stub!");
    }
}
