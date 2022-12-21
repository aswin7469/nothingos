package com.nothing.sdk.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class MethodReflection {
    private static final Map<String, Method> DECLARED_METHOD_CACHE = new ConcurrentReferenceHashMap(32);

    public static Method findMethod(Class<?> cls, String str) {
        return findMethod(cls, str, new Class[0]);
    }

    public static Method findMethod(Class<?> cls, String str, Class<?>... clsArr) {
        Assert.notNull(cls, "Class must not be null");
        Assert.notNull(str, "Method name must not be null");
        Method method = DECLARED_METHOD_CACHE.get(cls.getName() + str + Arrays.toString((Object[]) clsArr));
        if (method != null) {
            return method;
        }
        for (Class<?> cls2 = cls; cls2 != null; cls2 = cls2.getSuperclass()) {
            Method[] methods = cls2.isInterface() ? cls2.getMethods() : cls2.getDeclaredMethods();
            int length = methods.length;
            int i = 0;
            while (i < length) {
                Method method2 = methods[i];
                if (!str.equals(method2.getName()) || (clsArr != null && !Arrays.equals((Object[]) clsArr, (Object[]) method2.getParameterTypes()))) {
                    i++;
                } else {
                    DECLARED_METHOD_CACHE.put(cls.getName() + str + Arrays.toString((Object[]) clsArr), method2);
                    return method2;
                }
            }
        }
        return null;
    }

    public static Object invokeMethod(Method method, Object obj) {
        return invokeMethod(method, obj, new Object[0]);
    }

    public static Object invokeMethod(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (ReflectiveOperationException e) {
            ExceptionUtil.handleReflectionException(e);
            return null;
        }
    }
}
