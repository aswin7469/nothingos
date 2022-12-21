package libcore.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.reflect.Field;
import java.util.Arrays;
import kotlin.text.Typography;

public final class Objects {
    private Objects() {
    }

    public static String toString(Object obj) {
        Class<?> cls = obj.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append(cls.getSimpleName());
        sb.append('[');
        int i = 0;
        for (Field field : cls.getDeclaredFields()) {
            if ((field.getModifiers() & 136) == 0) {
                field.setAccessible(true);
                try {
                    Object obj2 = field.get(obj);
                    int i2 = i + 1;
                    if (i > 0) {
                        sb.append(',');
                    }
                    sb.append(field.getName());
                    sb.append('=');
                    if (obj2.getClass().isArray()) {
                        if (obj2.getClass() == boolean[].class) {
                            sb.append(Arrays.toString((boolean[]) obj2));
                        } else if (obj2.getClass() == byte[].class) {
                            sb.append(Arrays.toString((byte[]) obj2));
                        } else if (obj2.getClass() == char[].class) {
                            sb.append(Arrays.toString((char[]) obj2));
                        } else if (obj2.getClass() == double[].class) {
                            sb.append(Arrays.toString((double[]) obj2));
                        } else if (obj2.getClass() == float[].class) {
                            sb.append(Arrays.toString((float[]) obj2));
                        } else if (obj2.getClass() == int[].class) {
                            sb.append(Arrays.toString((int[]) obj2));
                        } else if (obj2.getClass() == long[].class) {
                            sb.append(Arrays.toString((long[]) obj2));
                        } else if (obj2.getClass() == short[].class) {
                            sb.append(Arrays.toString((short[]) obj2));
                        } else {
                            sb.append(Arrays.toString((Object[]) obj2));
                        }
                    } else if (obj2.getClass() == Character.class) {
                        sb.append('\'');
                        sb.append(obj2);
                        sb.append('\'');
                    } else if (obj2.getClass() == String.class) {
                        sb.append((char) Typography.quote);
                        sb.append(obj2);
                        sb.append((char) Typography.quote);
                    } else {
                        sb.append(obj2);
                    }
                    i = i2;
                } catch (IllegalAccessException e) {
                    throw new AssertionError((Object) e);
                }
            }
        }
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }
}
