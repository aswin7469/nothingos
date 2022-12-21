package sun.invoke.util;

import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BytecodeDescriptor {
    private BytecodeDescriptor() {
    }

    public static List<Class<?>> parseMethod(String str, ClassLoader classLoader) {
        return parseMethod(str, 0, str.length(), classLoader);
    }

    static List<Class<?>> parseMethod(String str, int i, int i2, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        int[] iArr = {i};
        ArrayList arrayList = new ArrayList();
        int i3 = iArr[0];
        if (i3 >= i2 || str.charAt(i3) != '(') {
            parseError(str, "not a method type");
        } else {
            iArr[0] = iArr[0] + 1;
            while (true) {
                int i4 = iArr[0];
                if (i4 >= i2 || str.charAt(i4) == ')') {
                    iArr[0] = iArr[0] + 1;
                } else {
                    Class<?> parseSig = parseSig(str, iArr, i2, classLoader);
                    if (parseSig == null || parseSig == Void.TYPE) {
                        parseError(str, "bad argument type");
                    }
                    arrayList.add(parseSig);
                }
            }
            iArr[0] = iArr[0] + 1;
        }
        Class<?> parseSig2 = parseSig(str, iArr, i2, classLoader);
        if (parseSig2 == null || iArr[0] != i2) {
            parseError(str, "bad return type");
        }
        arrayList.add(parseSig2);
        return arrayList;
    }

    private static void parseError(String str, String str2) {
        throw new IllegalArgumentException("bad signature: " + str + ": " + str2);
    }

    private static Class<?> parseSig(String str, int[] iArr, int i, ClassLoader classLoader) {
        int i2 = iArr[0];
        if (i2 == i) {
            return null;
        }
        iArr[0] = i2 + 1;
        char charAt = str.charAt(i2);
        if (charAt == 'L') {
            int i3 = iArr[0];
            int indexOf = str.indexOf(59, i3);
            if (indexOf < 0) {
                return null;
            }
            iArr[0] = indexOf + 1;
            String replace = str.substring(i3, indexOf).replace('/', '.');
            try {
                return classLoader.loadClass(replace);
            } catch (ClassNotFoundException e) {
                throw new TypeNotPresentException(replace, e);
            }
        } else if (charAt != '[') {
            return Wrapper.forBasicType(charAt).primitiveType();
        } else {
            Class<?> parseSig = parseSig(str, iArr, i, classLoader);
            return parseSig != null ? Array.newInstance(parseSig, 0).getClass() : parseSig;
        }
    }

    public static String unparse(Class<?> cls) {
        StringBuilder sb = new StringBuilder();
        unparseSig(cls, sb);
        return sb.toString();
    }

    public static String unparse(MethodType methodType) {
        return unparseMethod(methodType.returnType(), methodType.parameterList());
    }

    public static String unparse(Object obj) {
        if (obj instanceof Class) {
            return unparse((Class<?>) (Class) obj);
        }
        if (obj instanceof MethodType) {
            return unparse((MethodType) obj);
        }
        return (String) obj;
    }

    public static String unparseMethod(Class<?> cls, List<Class<?>> list) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Class<?> unparseSig : list) {
            unparseSig(unparseSig, sb);
        }
        sb.append(')');
        unparseSig(cls, sb);
        return sb.toString();
    }

    private static void unparseSig(Class<?> cls, StringBuilder sb) {
        char basicTypeChar = Wrapper.forBasicType(cls).basicTypeChar();
        if (basicTypeChar != 'L') {
            sb.append(basicTypeChar);
            return;
        }
        boolean z = !cls.isArray();
        if (z) {
            sb.append('L');
        }
        sb.append(cls.getName().replace('.', '/'));
        if (z) {
            sb.append(';');
        }
    }
}
