package java.util;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import jdk.internal.util.Preconditions;

public final class Objects {
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean nonNull(Object obj) {
        return obj != null;
    }

    private Objects() {
        throw new AssertionError((Object) "No java.util.Objects instances for you!");
    }

    public static boolean equals(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static boolean deepEquals(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        return Arrays.deepEquals0(obj, obj2);
    }

    public static int hashCode(Object obj) {
        if (obj != null) {
            return obj.hashCode();
        }
        return 0;
    }

    public static int hash(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static String toString(Object obj) {
        return String.valueOf(obj);
    }

    public static String toString(Object obj, String str) {
        return obj != null ? obj.toString() : str;
    }

    public static <T> int compare(T t, T t2, Comparator<? super T> comparator) {
        if (t == t2) {
            return 0;
        }
        return comparator.compare(t, t2);
    }

    public static <T> T requireNonNull(T t) {
        if (t != null) {
            return t;
        }
        throw null;
    }

    public static <T> T requireNonNull(T t, String str) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(str);
    }

    public static <T> T requireNonNullElse(T t, T t2) {
        return t != null ? t : requireNonNull(t2, "defaultObj");
    }

    public static <T> T requireNonNullElseGet(T t, Supplier<? extends T> supplier) {
        return t != null ? t : requireNonNull(((Supplier) requireNonNull(supplier, "supplier")).get(), "supplier.get()");
    }

    public static <T> T requireNonNull(T t, Supplier<String> supplier) {
        String str;
        if (t != null) {
            return t;
        }
        if (supplier == null) {
            str = null;
        } else {
            str = supplier.get();
        }
        throw new NullPointerException(str);
    }

    public static int checkIndex(int i, int i2) {
        return Preconditions.checkIndex(i, i2, (BiFunction) null);
    }

    public static int checkFromToIndex(int i, int i2, int i3) {
        return Preconditions.checkFromToIndex(i, i2, i3, (BiFunction) null);
    }

    public static int checkFromIndexSize(int i, int i2, int i3) {
        return Preconditions.checkFromIndexSize(i, i2, i3, (BiFunction) null);
    }
}
