package java.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class SeempLog {
    private static Method seemp_record_method = null;
    private static boolean seemp_record_method_looked_up = false;

    private SeempLog() {
    }

    public static int record_str(int i, String str) {
        if (seemp_record_method == null) {
            if (!seemp_record_method_looked_up) {
                try {
                    Class<?> cls = Class.forName("android.util.SeempLog");
                    if (cls != null) {
                        seemp_record_method = cls.getDeclaredMethod("record_str", Integer.TYPE, String.class);
                    }
                } catch (ClassNotFoundException unused) {
                    seemp_record_method = null;
                } catch (NoSuchMethodException unused2) {
                    seemp_record_method = null;
                }
            }
            seemp_record_method_looked_up = true;
        }
        Method method = seemp_record_method;
        if (method != null) {
            try {
                return ((Integer) method.invoke((Object) null, Integer.valueOf(i), str)).intValue();
            } catch (IllegalAccessException | InvocationTargetException unused3) {
            }
        }
        return 0;
    }
}
