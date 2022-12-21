package android.icu.text;

public final class Normalizer implements Cloneable {
    public static final int COMPARE_CODE_POINT_ORDER = 32768;
    public static final int COMPARE_IGNORE_CASE = 65536;
    public static final int FOLD_CASE_DEFAULT = 0;
    public static final int FOLD_CASE_EXCLUDE_SPECIAL_I = 1;
    public static final int INPUT_IS_FCD = 131072;
    public static final QuickCheckResult MAYBE = null;

    /* renamed from: NO */
    public static final QuickCheckResult f24NO = null;
    public static final QuickCheckResult YES = null;

    Normalizer() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public static int compare(char[] cArr, int i, int i2, char[] cArr2, int i3, int i4, int i5) {
        throw new RuntimeException("Stub!");
    }

    public static int compare(String str, String str2, int i) {
        throw new RuntimeException("Stub!");
    }

    public static int compare(char[] cArr, char[] cArr2, int i) {
        throw new RuntimeException("Stub!");
    }

    public static int compare(int i, int i2, int i3) {
        throw new RuntimeException("Stub!");
    }

    public static int compare(int i, String str, int i2) {
        throw new RuntimeException("Stub!");
    }

    public static final class QuickCheckResult {
        private QuickCheckResult() {
            throw new RuntimeException("Stub!");
        }
    }
}
