package libcore.reflect;

import androidx.exifinterface.media.ExifInterface;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.reflect.Array;

public final class InternalNames {
    private InternalNames() {
    }

    public static Class<?> getClass(ClassLoader classLoader, String str) {
        if (str.startsWith(NavigationBarInflaterView.SIZE_MOD_START)) {
            return Array.newInstance(getClass(classLoader, str.substring(1)), 0).getClass();
        }
        if (str.equals("Z")) {
            return Boolean.TYPE;
        }
        if (str.equals("B")) {
            return Byte.TYPE;
        }
        if (str.equals(ExifInterface.LATITUDE_SOUTH)) {
            return Short.TYPE;
        }
        if (str.equals("I")) {
            return Integer.TYPE;
        }
        if (str.equals("J")) {
            return Long.TYPE;
        }
        if (str.equals("F")) {
            return Float.TYPE;
        }
        if (str.equals("D")) {
            return Double.TYPE;
        }
        if (str.equals("C")) {
            return Character.TYPE;
        }
        if (str.equals(ExifInterface.GPS_MEASUREMENT_INTERRUPTED)) {
            return Void.TYPE;
        }
        String replace = str.substring(1, str.length() - 1).replace('/', '.');
        try {
            return classLoader.loadClass(replace);
        } catch (ClassNotFoundException e) {
            NoClassDefFoundError noClassDefFoundError = new NoClassDefFoundError(replace);
            noClassDefFoundError.initCause(e);
            throw noClassDefFoundError;
        }
    }

    public static String getInternalName(Class<?> cls) {
        if (cls.isArray()) {
            return NavigationBarInflaterView.SIZE_MOD_START + getInternalName(cls.getComponentType());
        } else if (cls == Boolean.TYPE) {
            return "Z";
        } else {
            if (cls == Byte.TYPE) {
                return "B";
            }
            if (cls == Short.TYPE) {
                return ExifInterface.LATITUDE_SOUTH;
            }
            if (cls == Integer.TYPE) {
                return "I";
            }
            if (cls == Long.TYPE) {
                return "J";
            }
            if (cls == Float.TYPE) {
                return "F";
            }
            if (cls == Double.TYPE) {
                return "D";
            }
            if (cls == Character.TYPE) {
                return "C";
            }
            if (cls == Void.TYPE) {
                return ExifInterface.GPS_MEASUREMENT_INTERRUPTED;
            }
            return "L" + cls.getName().replace('.', '/') + ';';
        }
    }
}
