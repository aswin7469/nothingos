package android.safetycenter.config;

import java.util.Objects;

final class BuilderUtils {
    private BuilderUtils() {
    }

    private static void validateAttribute(Object obj, String str, boolean z, boolean z2, Object obj2) {
        if (obj != null || !z) {
            boolean z3 = z2 && (Objects.equals(obj, obj2) ^ true);
            if (obj != null && z3) {
                throw new IllegalStateException(String.format("Prohibited attribute %s present", str));
            }
            return;
        }
        throw new IllegalStateException(String.format("Required attribute %s missing", str));
    }

    static void validateAttribute(Object obj, String str, boolean z, boolean z2) {
        validateAttribute(obj, str, z, z2, (Object) null);
    }

    static int validateResId(Integer num, String str, boolean z, boolean z2) {
        validateAttribute(num, str, z, z2, 0);
        if (num == null) {
            return 0;
        }
        if (!z || num.intValue() != 0) {
            return num.intValue();
        }
        throw new IllegalStateException(String.format("Required attribute %s invalid", str));
    }

    static int validateIntDef(Integer num, String str, boolean z, boolean z2, int i, int... iArr) {
        validateAttribute(num, str, z, z2, Integer.valueOf(i));
        if (num == null) {
            return i;
        }
        int i2 = 0;
        boolean z3 = false;
        while (true) {
            boolean z4 = true;
            if (i2 >= iArr.length) {
                break;
            }
            if (num.intValue() != iArr[i2]) {
                z4 = false;
            }
            z3 |= z4;
            i2++;
        }
        if (z3) {
            return num.intValue();
        }
        throw new IllegalStateException(String.format("Attribute %s invalid", str));
    }

    static int validateInteger(Integer num, String str, boolean z, boolean z2, int i) {
        validateAttribute(num, str, z, z2, Integer.valueOf(i));
        if (num == null) {
            return i;
        }
        return num.intValue();
    }

    static boolean validateBoolean(Boolean bool, String str, boolean z, boolean z2, boolean z3) {
        validateAttribute(bool, str, z, z2, Boolean.valueOf(z3));
        if (bool == null) {
            return z3;
        }
        return bool.booleanValue();
    }
}
