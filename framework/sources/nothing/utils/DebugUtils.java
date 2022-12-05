package nothing.utils;

import android.util.Log;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
/* loaded from: classes4.dex */
public class DebugUtils {
    /* JADX WARN: Removed duplicated region for block: B:11:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0010  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Class<?> getClassForName(PrintWriter pw, ClassLoader classLoader, String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            if (classLoader != null) {
                try {
                    return classLoader.loadClass(className);
                } catch (ClassNotFoundException e2) {
                    if (pw == null) {
                    }
                }
            }
            if (pw == null) {
                pw.println("ClassNotFoundException =" + className);
                return null;
            }
            Log.i("DebugOptions", "setDebugOptions: ClassNotFoundException =" + className);
            return null;
        }
    }

    public static void printDebugOptions(PrintWriter pw, ClassLoader classLoader, String className, String pattern) {
        Class<?> clazz = getClassForName(pw, classLoader, className);
        if (clazz != null) {
            printDebugOptions(pw, clazz, pattern);
        }
    }

    public static void printDebugOptions(PrintWriter pw, String className, String pattern) {
        printDebugOptions(pw, null, className, pattern);
    }

    public static void printDebugOptions(PrintWriter pw, Class<?> clazz, String pattern) {
        pw.println("class=" + clazz.getSimpleName());
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && Pattern.matches(pattern, field.getName()) && (field.getType() == Boolean.TYPE || field.getType() == Boolean.class || field.getType() == Integer.TYPE || field.getType() == Integer.class)) {
                try {
                    field.setAccessible(true);
                    pw.println("      " + field.getName() + "=" + field.get(null));
                } catch (IllegalAccessException e) {
                    pw.println(e.getMessage());
                }
            }
        }
    }

    public static List<String> setDebugOptions(PrintWriter pw, String className, String pattern, boolean on) {
        return setDebugOptions(pw, null, className, pattern, on);
    }

    public static List<String> setDebugOptions(PrintWriter pw, ClassLoader classLoader, String className, String pattern, boolean on) {
        Class<?> clazz = getClassForName(pw, classLoader, className);
        if (clazz != null) {
            return setDebugOptions(pw, clazz, pattern, on);
        }
        return null;
    }

    public static List<String> setDebugOptions(PrintWriter pw, Class<?> clazz, String pattern, boolean on) {
        Field[] fields = clazz.getDeclaredFields();
        if (pw != null) {
            pw.println("class=" + clazz.getSimpleName());
        } else {
            Log.i("DebugOptions", "setDebugOptions: class =" + clazz.getSimpleName());
        }
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && Pattern.matches(pattern, field.getName())) {
                field.setAccessible(true);
                if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
                    try {
                        field.setBoolean(null, on);
                        fieldNames.add(field.getName());
                        if (pw != null) {
                            pw.println("      " + field.getName() + "=" + on);
                        } else {
                            Log.i("DebugOptions", "setDebugOptions: " + field.getName() + "=" + on);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (field.getType() == Integer.TYPE || field.getType() == Integer.class) {
                    try {
                        field.setInt(null, on ? 1 : 0);
                        fieldNames.add(field.getName());
                        if (pw != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("      ");
                            sb.append(field.getName());
                            sb.append("=");
                            sb.append(on ? 1 : 0);
                            pw.println(sb.toString());
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("setDebugOptions: ");
                            sb2.append(field.getName());
                            sb2.append("=");
                            sb2.append(on ? 1 : 0);
                            Log.i("DebugOptions", sb2.toString());
                        }
                    } catch (IllegalAccessException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        return fieldNames;
    }
}
