package com.nothing.sdk.utils;

import java.lang.reflect.Constructor;
import java.util.Map;

public class ClassReflection {
    private static final Map<String, Class> DECLARED_CLASS_CACHE = new ConcurrentReferenceHashMap(32);
    private static final Map<String, Constructor> DECLARED_CONSTRUCTOR_CACHE = new ConcurrentReferenceHashMap(32);

    public static Class findClass(String str) {
        Map<String, Class> map = DECLARED_CLASS_CACHE;
        Class<?> cls = map.get(str);
        if (cls != null) {
            return cls;
        }
        try {
            cls = Class.forName(str);
            map.put(str, cls);
            return cls;
        } catch (ReflectiveOperationException e) {
            ExceptionUtil.handleReflectionException(e);
            return cls;
        }
    }

    public static Class findInnerClass(Class cls, String str) {
        Class cls2 = DECLARED_CLASS_CACHE.get(cls.getName() + str);
        if (cls2 != null) {
            return cls2;
        }
        for (Class cls3 : cls.getDeclaredClasses()) {
            if (str.equals(cls3.getSimpleName())) {
                DECLARED_CLASS_CACHE.put(cls.getName() + str, cls3);
                return cls3;
            }
        }
        return null;
    }

    public static Constructor<?> findConstructor(String str, Class<?>... clsArr) {
        Map<String, Constructor> map = DECLARED_CONSTRUCTOR_CACHE;
        Constructor<?> constructor = map.get(str);
        if (constructor != null) {
            return constructor;
        }
        Class findClass = findClass(str);
        if (findClass == null) {
            return null;
        }
        try {
            Constructor<?> constructor2 = findClass.getConstructor(clsArr);
            map.put(str, constructor2);
            return constructor2;
        } catch (NoSuchMethodException | SecurityException e) {
            ExceptionUtil.handleReflectionException(e);
            return null;
        }
    }
}
