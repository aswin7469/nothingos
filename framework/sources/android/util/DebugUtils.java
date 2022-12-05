package android.util;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes3.dex */
public class DebugUtils {
    public static boolean isObjectSelected(Object object) {
        Method declaredMethod;
        boolean match = false;
        String s = System.getenv("ANDROID_OBJECT_FILTER");
        if (s != null && s.length() > 0) {
            String[] selectors = s.split("@");
            if (object.getClass().getSimpleName().matches(selectors[0])) {
                for (int i = 1; i < selectors.length; i++) {
                    String[] pair = selectors[i].split("=");
                    Class<?> klass = object.getClass();
                    Class<?> parent = klass;
                    do {
                        try {
                            declaredMethod = parent.getDeclaredMethod("get" + pair[0].substring(0, 1).toUpperCase(Locale.ROOT) + pair[0].substring(1), null);
                            Class<?> superclass = klass.getSuperclass();
                            parent = superclass;
                            if (superclass == null) {
                                break;
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e2) {
                            e2.printStackTrace();
                        } catch (InvocationTargetException e3) {
                            e3.printStackTrace();
                        }
                    } while (declaredMethod == null);
                    if (declaredMethod != null) {
                        Object value = declaredMethod.invoke(object, null);
                        match |= (value != null ? value.toString() : "null").matches(pair[1]);
                    }
                }
            }
        }
        return match;
    }

    public static void buildShortClassTag(Object cls, StringBuilder out) {
        int end;
        if (cls == null) {
            out.append("null");
            return;
        }
        String simpleName = cls.getClass().getSimpleName();
        if ((simpleName == null || simpleName.isEmpty()) && (end = (simpleName = cls.getClass().getName()).lastIndexOf(46)) > 0) {
            simpleName = simpleName.substring(end + 1);
        }
        out.append(simpleName);
        out.append('{');
        out.append(Integer.toHexString(System.identityHashCode(cls)));
    }

    public static void printSizeValue(PrintWriter pw, long number) {
        String value;
        float result = (float) number;
        String suffix = "";
        if (result > 900.0f) {
            suffix = "KB";
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = "MB";
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = "GB";
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = "TB";
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = "PB";
            result /= 1024.0f;
        }
        if (result < 1.0f) {
            value = String.format("%.2f", Float.valueOf(result));
        } else if (result < 10.0f) {
            value = String.format("%.1f", Float.valueOf(result));
        } else {
            value = result < 100.0f ? String.format("%.0f", Float.valueOf(result)) : String.format("%.0f", Float.valueOf(result));
        }
        pw.print(value);
        pw.print(suffix);
    }

    public static String sizeValueToString(long number, StringBuilder outBuilder) {
        String value;
        if (outBuilder == null) {
            outBuilder = new StringBuilder(32);
        }
        float result = (float) number;
        String suffix = "";
        if (result > 900.0f) {
            suffix = "KB";
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = "MB";
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = "GB";
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = "TB";
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = "PB";
            result /= 1024.0f;
        }
        if (result < 1.0f) {
            value = String.format("%.2f", Float.valueOf(result));
        } else if (result < 10.0f) {
            value = String.format("%.1f", Float.valueOf(result));
        } else {
            value = result < 100.0f ? String.format("%.0f", Float.valueOf(result)) : String.format("%.0f", Float.valueOf(result));
        }
        outBuilder.append(value);
        outBuilder.append(suffix);
        return outBuilder.toString();
    }

    public static String valueToString(Class<?> clazz, String prefix, int value) {
        Field[] declaredFields = clazz.getDeclaredFields();
        int length = declaredFields.length;
        for (int i = 0; i < length; i++) {
            Field field = declaredFields[i];
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && field.getType().equals(Integer.TYPE) && field.getName().startsWith(prefix)) {
                try {
                    if (value == field.getInt(null)) {
                        return constNameWithoutPrefix(prefix, field);
                    }
                    continue;
                } catch (IllegalAccessException e) {
                }
            }
        }
        return Integer.toString(value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v10, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v7, types: [int] */
    /* JADX WARN: Type inference failed for: r1v9 */
    public static String flagsToString(Class<?> clazz, String prefix, int flags) {
        StringBuilder res = new StringBuilder();
        ?? r1 = 0;
        boolean flagsWasZero = flags == 0;
        Field[] declaredFields = clazz.getDeclaredFields();
        int length = declaredFields.length;
        while (r1 < length) {
            Field field = declaredFields[r1];
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && field.getType().equals(Integer.TYPE) && field.getName().startsWith(prefix)) {
                try {
                    int value = field.getInt(null);
                    if (value == 0 && flagsWasZero) {
                        r1 = constNameWithoutPrefix(prefix, field);
                        return r1;
                    } else if (value != 0 && (flags & value) == value) {
                        flags &= ~value;
                        res.append(constNameWithoutPrefix(prefix, field));
                        res.append('|');
                    }
                } catch (IllegalAccessException e) {
                }
            }
            r1++;
        }
        if (flags != 0 || res.length() == 0) {
            res.append(Integer.toHexString(flags));
        } else {
            res.deleteCharAt(res.length() - 1);
        }
        return res.toString();
    }

    public static String constantToString(Class<?> clazz, String prefix, int value) {
        Field[] declaredFields = clazz.getDeclaredFields();
        int length = declaredFields.length;
        for (int i = 0; i < length; i++) {
            Field field = declaredFields[i];
            int modifiers = field.getModifiers();
            try {
                if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && field.getType().equals(Integer.TYPE) && field.getName().startsWith(prefix) && field.getInt(null) == value) {
                    return constNameWithoutPrefix(prefix, field);
                }
            } catch (IllegalAccessException e) {
            }
        }
        return prefix + Integer.toString(value);
    }

    private static String constNameWithoutPrefix(String prefix, Field field) {
        return field.getName().substring(prefix.length());
    }

    public static List<String> callersWithin(final Class<?> cls, int offset) {
        List<String> result = (List) Arrays.stream(Thread.currentThread().getStackTrace()).skip(offset + 3).filter(new Predicate() { // from class: android.util.DebugUtils$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean startsWith;
                startsWith = ((StackTraceElement) obj).getClassName().startsWith(cls.getName());
                return startsWith;
            }
        }).map(DebugUtils$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList());
        Collections.reverse(result);
        return result;
    }
}
