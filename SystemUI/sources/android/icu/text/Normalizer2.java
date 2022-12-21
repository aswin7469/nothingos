package android.icu.text;

import android.icu.text.Normalizer;
import java.p026io.InputStream;

public abstract class Normalizer2 {

    public enum Mode {
        COMPOSE,
        DECOMPOSE,
        FCD,
        COMPOSE_CONTIGUOUS
    }

    public abstract StringBuilder append(StringBuilder sb, CharSequence charSequence);

    public abstract String getDecomposition(int i);

    public abstract boolean hasBoundaryAfter(int i);

    public abstract boolean hasBoundaryBefore(int i);

    public abstract boolean isInert(int i);

    public abstract boolean isNormalized(CharSequence charSequence);

    public abstract Appendable normalize(CharSequence charSequence, Appendable appendable);

    public abstract StringBuilder normalize(CharSequence charSequence, StringBuilder sb);

    public abstract StringBuilder normalizeSecondAndAppend(StringBuilder sb, CharSequence charSequence);

    public abstract Normalizer.QuickCheckResult quickCheck(CharSequence charSequence);

    public abstract int spanQuickCheckYes(CharSequence charSequence);

    Normalizer2() {
        throw new RuntimeException("Stub!");
    }

    public static Normalizer2 getNFCInstance() {
        throw new RuntimeException("Stub!");
    }

    public static Normalizer2 getNFDInstance() {
        throw new RuntimeException("Stub!");
    }

    public static Normalizer2 getNFKCInstance() {
        throw new RuntimeException("Stub!");
    }

    public static Normalizer2 getNFKDInstance() {
        throw new RuntimeException("Stub!");
    }

    public static Normalizer2 getNFKCCasefoldInstance() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static Normalizer2 getInstance(InputStream inputStream, String str, Mode mode) {
        throw new RuntimeException("Stub!");
    }

    public String normalize(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public String getRawDecomposition(int i) {
        throw new RuntimeException("Stub!");
    }

    public int composePair(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public int getCombiningClass(int i) {
        throw new RuntimeException("Stub!");
    }
}
