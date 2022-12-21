package sun.security.util;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.settingslib.accessibility.AccessibilityUtils;
import java.math.BigInteger;
import java.p026io.PrintStream;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.util.locale.LanguageTag;

public class Debug {
    private static String args;
    private static final char[] hexDigits = "0123456789abcdef".toCharArray();
    private String prefix;

    public static Debug getInstance(String str) {
        return getInstance(str, str);
    }

    public static Debug getInstance(String str, String str2) {
        if (!isOn(str)) {
            return null;
        }
        Debug debug = new Debug();
        debug.prefix = str2;
        return debug;
    }

    public static boolean isOn(String str) {
        String str2 = args;
        if (str2 == null) {
            return false;
        }
        if (str2.indexOf("all") != -1) {
            return true;
        }
        if (args.indexOf(str) != -1) {
            return true;
        }
        return false;
    }

    public void println(String str) {
        PrintStream printStream = System.err;
        printStream.println(this.prefix + ": " + str);
    }

    public void println() {
        PrintStream printStream = System.err;
        printStream.println(this.prefix + ":");
    }

    public static String toHexString(BigInteger bigInteger) {
        String bigInteger2 = bigInteger.toString(16);
        StringBuffer stringBuffer = new StringBuffer(bigInteger2.length() * 2);
        if (bigInteger2.startsWith(LanguageTag.SEP)) {
            stringBuffer.append("   -");
            bigInteger2 = bigInteger2.substring(1);
        } else {
            stringBuffer.append("    ");
        }
        if (bigInteger2.length() % 2 != 0) {
            bigInteger2 = "0" + bigInteger2;
        }
        int i = 0;
        while (i < bigInteger2.length()) {
            int i2 = i + 2;
            stringBuffer.append(bigInteger2.substring(i, i2));
            if (i2 != bigInteger2.length()) {
                if (i2 % 64 == 0) {
                    stringBuffer.append("\n    ");
                } else if (i2 % 8 == 0) {
                    stringBuffer.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                }
            }
            i = i2;
        }
        return stringBuffer.toString();
    }

    private static String marshal(String str) {
        if (str == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        Matcher matcher = Pattern.compile("[Pp][Ee][Rr][Mm][Ii][Ss][Ss][Ii][Oo][Nn]=[a-zA-Z_$][a-zA-Z0-9_$]*([.][a-zA-Z_$][a-zA-Z0-9_$]*)*").matcher(new StringBuffer(str));
        StringBuffer stringBuffer2 = new StringBuffer();
        while (matcher.find()) {
            stringBuffer.append(matcher.group().replaceFirst("[Pp][Ee][Rr][Mm][Ii][Ss][Ss][Ii][Oo][Nn]=", "permission="));
            stringBuffer.append("  ");
            matcher.appendReplacement(stringBuffer2, "");
        }
        matcher.appendTail(stringBuffer2);
        Matcher matcher2 = Pattern.compile("[Cc][Oo][Dd][Ee][Bb][Aa][Ss][Ee]=[^, ;]*").matcher(stringBuffer2);
        StringBuffer stringBuffer3 = new StringBuffer();
        while (matcher2.find()) {
            stringBuffer.append(matcher2.group().replaceFirst("[Cc][Oo][Dd][Ee][Bb][Aa][Ss][Ee]=", "codebase="));
            stringBuffer.append("  ");
            matcher2.appendReplacement(stringBuffer3, "");
        }
        matcher2.appendTail(stringBuffer3);
        stringBuffer.append(stringBuffer3.toString().toLowerCase(Locale.ENGLISH));
        return stringBuffer.toString();
    }

    public static String toString(byte[] bArr) {
        if (bArr == null) {
            return "(null)";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 3);
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i] & 255;
            if (i != 0) {
                sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            }
            char[] cArr = hexDigits;
            sb.append(cArr[b >>> 4]);
            sb.append(cArr[b & 15]);
        }
        return sb.toString();
    }
}
