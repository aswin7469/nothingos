package sun.invoke.util;

import java.lang.reflect.Modifier;

public class VerifyAccess {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final boolean ALLOW_NESTMATE_ACCESS = false;
    private static final int ALL_ACCESS_MODES = 7;
    private static final int PACKAGE_ALLOWED = 8;
    private static final int PACKAGE_ONLY = 0;
    private static final int PROTECTED_OR_PACKAGE_ALLOWED = 12;

    private VerifyAccess() {
    }

    public static boolean isMemberAccessible(Class<?> cls, Class<?> cls2, int i, Class<?> cls3, int i2) {
        if (i2 == 0 || !isClassAccessible(cls, cls3, i2)) {
            return false;
        }
        if (cls2 == cls3 && (i2 & 2) != 0) {
            return true;
        }
        int i3 = i & 7;
        if (i3 != 0) {
            if (i3 == 1) {
                return true;
            }
            if (i3 == 2) {
                return false;
            }
            if (i3 != 4) {
                throw new IllegalArgumentException("bad modifiers: " + Modifier.toString(i));
            } else if ((i2 & 12) != 0 && isSamePackage(cls2, cls3)) {
                return true;
            } else {
                int i4 = i2 & 4;
                if (i4 == 0) {
                    return false;
                }
                if (((i & 8) == 0 || isRelatedClass(cls, cls3)) && i4 != 0 && isSubClass(cls3, cls2)) {
                    return true;
                }
                return false;
            }
        } else if ((i2 & 8) == 0 || !isSamePackage(cls2, cls3)) {
            return false;
        } else {
            return true;
        }
    }

    static boolean isRelatedClass(Class<?> cls, Class<?> cls2) {
        return cls == cls2 || isSubClass(cls, cls2) || isSubClass(cls2, cls);
    }

    static boolean isSubClass(Class<?> cls, Class<?> cls2) {
        return cls2.isAssignableFrom(cls) && !cls.isInterface();
    }

    public static boolean isClassAccessible(Class<?> cls, Class<?> cls2, int i) {
        if (i == 0) {
            return false;
        }
        if (Modifier.isPublic(cls.getModifiers())) {
            return true;
        }
        return (i & 8) != 0 && isSamePackage(cls2, cls);
    }

    public static boolean isSamePackage(Class<?> cls, Class<?> cls2) {
        if (cls.isArray() || cls2.isArray()) {
            throw new IllegalArgumentException();
        } else if (cls == cls2) {
            return true;
        } else {
            if (cls.getClassLoader() != cls2.getClassLoader()) {
                return false;
            }
            String name = cls.getName();
            String name2 = cls2.getName();
            int lastIndexOf = name.lastIndexOf(46);
            if (lastIndexOf != name2.lastIndexOf(46)) {
                return false;
            }
            for (int i = 0; i < lastIndexOf; i++) {
                if (name.charAt(i) != name2.charAt(i)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isSamePackageMember(Class<?> cls, Class<?> cls2) {
        if (cls == cls2) {
            return true;
        }
        return isSamePackage(cls, cls2) && getOutermostEnclosingClass(cls) == getOutermostEnclosingClass(cls2);
    }

    private static Class<?> getOutermostEnclosingClass(Class<?> cls) {
        while (true) {
            Class<?> enclosingClass = cls.getEnclosingClass();
            if (enclosingClass == null) {
                return cls;
            }
            cls = enclosingClass;
        }
    }
}
