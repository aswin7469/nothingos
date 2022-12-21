package sun.misc;

import java.p026io.PrintStream;

public class MessageUtils {
    public static String subst(String str, String str2) {
        return subst(str, new String[]{str2});
    }

    public static String subst(String str, String str2, String str3) {
        return subst(str, new String[]{str2, str3});
    }

    public static String subst(String str, String str2, String str3, String str4) {
        return subst(str, new String[]{str2, str3, str4});
    }

    public static String subst(String str, String[] strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        int length = str.length();
        int i = 0;
        while (i >= 0 && i < length) {
            char charAt = str.charAt(i);
            if (charAt != '%') {
                stringBuffer.append(charAt);
            } else if (i != length) {
                int i2 = i + 1;
                int digit = Character.digit(str.charAt(i2), 10);
                if (digit == -1) {
                    stringBuffer.append(str.charAt(i2));
                } else if (digit < strArr.length) {
                    stringBuffer.append(strArr[digit]);
                }
                i = i2;
            }
            i++;
        }
        return stringBuffer.toString();
    }

    public static String substProp(String str, String str2) {
        return subst(System.getProperty(str), str2);
    }

    public static String substProp(String str, String str2, String str3) {
        return subst(System.getProperty(str), str2, str3);
    }

    public static String substProp(String str, String str2, String str3, String str4) {
        return subst(System.getProperty(str), str2, str3, str4);
    }

    public static void err(String str) {
        System.err.println(str);
    }

    public static void out(String str) {
        System.out.println(str);
    }

    public static void where() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        for (int i = 1; i < stackTrace.length; i++) {
            PrintStream printStream = System.err;
            printStream.println("\t" + stackTrace[i].toString());
        }
    }
}
