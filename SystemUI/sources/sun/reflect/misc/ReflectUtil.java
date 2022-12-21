package sun.reflect.misc;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import sun.reflect.Reflection;

public final class ReflectUtil {
    private ReflectUtil() {
    }

    public static Class<?> forName(String str) throws ClassNotFoundException {
        checkPackageAccess(str);
        return Class.forName(str);
    }

    public static Object newInstance(Class<?> cls) throws InstantiationException, IllegalAccessException {
        checkPackageAccess(cls);
        return cls.newInstance();
    }

    public static void ensureMemberAccess(Class<?> cls, Class<?> cls2, Object obj, int i) throws IllegalAccessException {
        if (obj != null || !Modifier.isProtected(i)) {
            Reflection.ensureMemberAccess(cls, cls2, obj, i);
            return;
        }
        int i2 = (i & -5) | 1;
        Reflection.ensureMemberAccess(cls, cls2, obj, i2);
        try {
            Reflection.ensureMemberAccess(cls, cls2, obj, i2 & -2);
        } catch (IllegalAccessException e) {
            if (!isSubclassOf(cls, cls2)) {
                throw e;
            }
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Class<?>, code=java.lang.Class, for r0v0, types: [java.lang.Class<?>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isSubclassOf(java.lang.Class r0, java.lang.Class<?> r1) {
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
        throw new UnsupportedOperationException("Method not decompiled: sun.reflect.misc.ReflectUtil.isSubclassOf(java.lang.Class, java.lang.Class):boolean");
    }

    public static void checkPackageAccess(Class<?> cls) {
        checkPackageAccess(cls.getName());
        if (isNonPublicProxyClass(cls)) {
            checkProxyPackageAccess(cls);
        }
    }

    public static void checkPackageAccess(String str) {
        int lastIndexOf;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            String replace = str.replace('/', '.');
            if (replace.startsWith(NavigationBarInflaterView.SIZE_MOD_START) && (lastIndexOf = replace.lastIndexOf(91) + 2) > 1 && lastIndexOf < replace.length()) {
                replace = replace.substring(lastIndexOf);
            }
            int lastIndexOf2 = replace.lastIndexOf(46);
            if (lastIndexOf2 != -1) {
                securityManager.checkPackageAccess(replace.substring(0, lastIndexOf2));
            }
        }
    }

    public static boolean isPackageAccessible(Class<?> cls) {
        try {
            checkPackageAccess(cls);
            return true;
        } catch (SecurityException unused) {
            return false;
        }
    }

    private static boolean isAncestor(ClassLoader classLoader, ClassLoader classLoader2) {
        do {
            classLoader2 = classLoader2.getParent();
            if (classLoader == classLoader2) {
                return true;
            }
        } while (classLoader2 != null);
        return false;
    }

    public static boolean needsPackageAccessCheck(ClassLoader classLoader, ClassLoader classLoader2) {
        if (classLoader == null || classLoader == classLoader2) {
            return false;
        }
        if (classLoader2 == null) {
            return true;
        }
        return !isAncestor(classLoader, classLoader2);
    }

    public static void checkProxyPackageAccess(Class<?> cls) {
        if (System.getSecurityManager() != null && Proxy.isProxyClass(cls)) {
            for (Class checkPackageAccess : cls.getInterfaces()) {
                checkPackageAccess((Class<?>) checkPackageAccess);
            }
        }
    }

    public static void checkProxyPackageAccess(ClassLoader classLoader, Class<?>... clsArr) {
        if (System.getSecurityManager() != null) {
            for (Class<?> cls : clsArr) {
                if (needsPackageAccessCheck(classLoader, cls.getClassLoader())) {
                    checkPackageAccess(cls);
                }
            }
        }
    }

    public static boolean isNonPublicProxyClass(Class<?> cls) {
        String name = cls.getName();
        int lastIndexOf = name.lastIndexOf(46);
        String substring = lastIndexOf != -1 ? name.substring(0, lastIndexOf) : "";
        if (!Proxy.isProxyClass(cls) || substring.isEmpty()) {
            return false;
        }
        return true;
    }
}
