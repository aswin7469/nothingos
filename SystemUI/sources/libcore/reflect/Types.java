package libcore.reflect;

import androidx.exifinterface.media.ExifInterface;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import libcore.util.EmptyArray;

public final class Types {
    private static final Map<Class<?>, String> PRIMITIVE_TO_SIGNATURE;

    private Types() {
    }

    static {
        HashMap hashMap = new HashMap(9);
        PRIMITIVE_TO_SIGNATURE = hashMap;
        hashMap.put(Byte.TYPE, "B");
        hashMap.put(Character.TYPE, "C");
        hashMap.put(Short.TYPE, ExifInterface.LATITUDE_SOUTH);
        hashMap.put(Integer.TYPE, "I");
        hashMap.put(Long.TYPE, "J");
        hashMap.put(Float.TYPE, "F");
        hashMap.put(Double.TYPE, "D");
        hashMap.put(Void.TYPE, ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
        hashMap.put(Boolean.TYPE, "Z");
    }

    public static Type[] getTypeArray(ListOfTypes listOfTypes, boolean z) {
        if (listOfTypes.length() == 0) {
            return EmptyArray.TYPE;
        }
        Type[] resolvedTypes = listOfTypes.getResolvedTypes();
        return z ? (Type[]) resolvedTypes.clone() : resolvedTypes;
    }

    public static Type getType(Type type) {
        return type instanceof ParameterizedTypeImpl ? ((ParameterizedTypeImpl) type).getResolvedType() : type;
    }

    public static String getSignature(Class<?> cls) {
        String str = PRIMITIVE_TO_SIGNATURE.get(cls);
        if (str != null) {
            return str;
        }
        if (cls.isArray()) {
            return NavigationBarInflaterView.SIZE_MOD_START + getSignature(cls.getComponentType());
        }
        return "L" + cls.getName() + NavigationBarInflaterView.GRAVITY_SEPARATOR;
    }

    public static String toString(Class<?>[] clsArr) {
        if (clsArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        appendTypeName(sb, clsArr[0]);
        for (int i = 1; i < clsArr.length; i++) {
            sb.append(',');
            appendTypeName(sb, clsArr[i]);
        }
        return sb.toString();
    }

    public static void appendTypeName(StringBuilder sb, Class<?> cls) {
        int i = 0;
        while (cls.isArray()) {
            cls = cls.getComponentType();
            i++;
        }
        sb.append(cls.getName());
        for (int i2 = 0; i2 < i; i2++) {
            sb.append("[]");
        }
    }

    public static void appendArrayGenericType(StringBuilder sb, Type[] typeArr) {
        if (typeArr.length != 0) {
            appendGenericType(sb, typeArr[0]);
            for (int i = 1; i < typeArr.length; i++) {
                sb.append(',');
                appendGenericType(sb, typeArr[i]);
            }
        }
    }

    public static void appendGenericType(StringBuilder sb, Type type) {
        if (type instanceof TypeVariable) {
            sb.append(((TypeVariable) type).getName());
        } else if (type instanceof ParameterizedType) {
            sb.append(type.toString());
        } else if (type instanceof GenericArrayType) {
            appendGenericType(sb, ((GenericArrayType) type).getGenericComponentType());
            sb.append("[]");
        } else if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.isArray()) {
                String[] split = cls.getName().split("\\[");
                int length = split.length - 1;
                if (split[length].length() > 1) {
                    String str = split[length];
                    sb.append(str.substring(1, str.length() - 1));
                } else {
                    char charAt = split[length].charAt(0);
                    if (charAt == 'I') {
                        sb.append("int");
                    } else if (charAt == 'B') {
                        sb.append("byte");
                    } else if (charAt == 'J') {
                        sb.append("long");
                    } else if (charAt == 'F') {
                        sb.append("float");
                    } else if (charAt == 'D') {
                        sb.append("double");
                    } else if (charAt == 'S') {
                        sb.append("short");
                    } else if (charAt == 'C') {
                        sb.append("char");
                    } else if (charAt == 'Z') {
                        sb.append("boolean");
                    } else if (charAt == 'V') {
                        sb.append("void");
                    }
                }
                for (int i = 0; i < length; i++) {
                    sb.append("[]");
                }
                return;
            }
            sb.append(cls.getName());
        }
    }
}
