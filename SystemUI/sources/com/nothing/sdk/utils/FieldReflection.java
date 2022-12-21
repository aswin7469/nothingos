package com.nothing.sdk.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class FieldReflection {
    private static final Map<String, Field> DECLARED_FIELDS_CACHE = new ConcurrentReferenceHashMap(32);

    public static Field findField(Class<?> cls, String str) {
        return findField(cls, str, (Class<?>) null);
    }

    public static Field findField(Class<?> cls, String str, Class<?> cls2) {
        Assert.notNull(cls, "Class must not be null");
        Assert.isTrue((str == null && cls2 == null) ? false : true, "Either name or type of the field must be specified");
        Field field = DECLARED_FIELDS_CACHE.get(cls.getName() + str);
        if (field != null) {
            return field;
        }
        Class<?> cls3 = cls;
        while (!Object.class.equals(cls3) && cls3 != null) {
            for (Field field2 : cls3.getDeclaredFields()) {
                if ((str == null || str.equals(field2.getName())) && (cls2 == null || cls2.equals(field2.getType()))) {
                    DECLARED_FIELDS_CACHE.put(cls.getName() + str, field2);
                    return field2;
                }
            }
            cls3 = cls3.getSuperclass();
        }
        return null;
    }

    public static void setField(Field field, Object obj, Object obj2) {
        try {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }
            field.set(obj, obj2);
        } catch (IllegalAccessException e) {
            ExceptionUtil.handleReflectionException(e);
        }
    }

    public static Object getField(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            ExceptionUtil.handleReflectionException(e);
            return null;
        }
    }
}
