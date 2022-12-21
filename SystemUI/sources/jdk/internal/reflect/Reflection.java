package jdk.internal.reflect;

import dalvik.system.VMStack;
import java.lang.reflect.Modifier;

public class Reflection {
    public static Class<?> getCallerClass() {
        return VMStack.getStackClass2();
    }

    public static void ensureMemberAccess(Class<?> cls, Class<?> cls2, Object obj, int i) throws IllegalAccessException {
        if (cls == null || cls2 == null) {
            throw new InternalError();
        } else if (!verifyMemberAccess(cls, cls2, obj, i)) {
            throw new IllegalAccessException("Class " + cls.getName() + " can not access a member of class " + cls2.getName() + " with modifiers \"" + Modifier.toString(i) + "\"");
        }
    }

    public static boolean verifyMemberAccess(Class<?> cls, Class<?> cls2, Object obj, int i) {
        boolean z;
        boolean z2;
        Class<?> cls3;
        if (cls == cls2) {
            return true;
        }
        if (!Modifier.isPublic(cls2.getAccessFlags())) {
            z2 = isSameClassPackage(cls, cls2);
            if (!z2) {
                return false;
            }
            z = true;
        } else {
            z2 = false;
            z = false;
        }
        if (Modifier.isPublic(i)) {
            return true;
        }
        boolean z3 = Modifier.isProtected(i) && isSubclassOf(cls, cls2);
        if (!z3 && !Modifier.isPrivate(i)) {
            if (!z) {
                z2 = isSameClassPackage(cls, cls2);
                z = true;
            }
            if (z2) {
                z3 = true;
            }
        }
        if (!z3) {
            return false;
        }
        if (Modifier.isProtected(i)) {
            if (obj == null) {
                cls3 = cls2;
            } else {
                cls3 = obj.getClass();
            }
            if (cls3 != cls) {
                if (!z) {
                    z2 = isSameClassPackage(cls, cls2);
                }
                return z2 || isSubclassOf(cls3, cls);
            }
        }
    }

    private static boolean isSameClassPackage(Class<?> cls, Class<?> cls2) {
        return isSameClassPackage(cls.getClassLoader(), cls.getName(), cls2.getClassLoader(), cls2.getName());
    }

    private static boolean isSameClassPackage(ClassLoader classLoader, String str, ClassLoader classLoader2, String str2) {
        int i;
        int i2;
        if (classLoader != classLoader2) {
            return false;
        }
        int lastIndexOf = str.lastIndexOf(46);
        int lastIndexOf2 = str2.lastIndexOf(46);
        if (lastIndexOf != -1 && lastIndexOf2 != -1) {
            if (str.charAt(0) == '[') {
                int i3 = 0;
                do {
                    i3++;
                } while (str.charAt(i3) == '[');
                if (str.charAt(i3) == 'L') {
                    i = i3;
                } else {
                    throw new InternalError("Illegal class name " + str);
                }
            } else {
                i = 0;
            }
            if (str2.charAt(0) == '[') {
                int i4 = 0;
                do {
                    i4++;
                } while (str2.charAt(i4) == '[');
                if (str2.charAt(i4) == 'L') {
                    i2 = i4;
                } else {
                    throw new InternalError("Illegal class name " + str2);
                }
            } else {
                i2 = 0;
            }
            int i5 = lastIndexOf - i;
            if (i5 != lastIndexOf2 - i2) {
                return false;
            }
            return str.regionMatches(false, i, str2, i2, i5);
        } else if (lastIndexOf == lastIndexOf2) {
            return true;
        } else {
            return false;
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Class<?>, code=java.lang.Class, for r0v0, types: [java.lang.Class<?>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean isSubclassOf(java.lang.Class r0, java.lang.Class<?> r1) {
        /*
        L_0x0000:
            if (r0 == 0) goto L_0x000b
            if (r0 != r1) goto L_0x0006
            r0 = 1
            return r0
        L_0x0006:
            java.lang.Class r0 = r0.getSuperclass()
            goto L_0x0000
        L_0x000b:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.reflect.Reflection.isSubclassOf(java.lang.Class, java.lang.Class):boolean");
    }
}
