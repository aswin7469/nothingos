package java.lang;

import java.p026io.Serializable;

public final class Boolean implements Serializable, Comparable<Boolean> {
    public static final Boolean FALSE = new Boolean(false);
    public static final Boolean TRUE = new Boolean(true);
    public static final Class<Boolean> TYPE = Class.getPrimitiveClass("boolean");
    private static final long serialVersionUID = -3665804199014368530L;
    private final boolean value;

    public static int compare(boolean z, boolean z2) {
        if (z == z2) {
            return 0;
        }
        return z ? 1 : -1;
    }

    public static int hashCode(boolean z) {
        return z ? 1231 : 1237;
    }

    public static boolean logicalAnd(boolean z, boolean z2) {
        return z && z2;
    }

    public static boolean logicalOr(boolean z, boolean z2) {
        return z || z2;
    }

    public static boolean logicalXor(boolean z, boolean z2) {
        return z ^ z2;
    }

    public static String toString(boolean z) {
        return z ? "true" : "false";
    }

    @Deprecated(since = "9")
    public Boolean(boolean z) {
        this.value = z;
    }

    @Deprecated(since = "9")
    public Boolean(String str) {
        this(parseBoolean(str));
    }

    public static boolean parseBoolean(String str) {
        return "true".equalsIgnoreCase(str);
    }

    public boolean booleanValue() {
        return this.value;
    }

    public static Boolean valueOf(boolean z) {
        return z ? TRUE : FALSE;
    }

    public static Boolean valueOf(String str) {
        return parseBoolean(str) ? TRUE : FALSE;
    }

    public String toString() {
        return this.value ? "true" : "false";
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Boolean) || this.value != ((Boolean) obj).booleanValue()) {
            return false;
        }
        return true;
    }

    public static boolean getBoolean(String str) {
        try {
            return parseBoolean(System.getProperty(str));
        } catch (IllegalArgumentException | NullPointerException unused) {
            return false;
        }
    }

    public int compareTo(Boolean bool) {
        return compare(this.value, bool.value);
    }
}
