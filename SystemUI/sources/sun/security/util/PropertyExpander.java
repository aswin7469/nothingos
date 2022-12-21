package sun.security.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.p026io.File;
import java.security.GeneralSecurityException;
import sun.net.www.ParseUtil;

public class PropertyExpander {

    public static class ExpandException extends GeneralSecurityException {
        private static final long serialVersionUID = -7941948581406161702L;

        public ExpandException(String str) {
            super(str);
        }
    }

    public static String expand(String str) throws ExpandException {
        return expand(str, false);
    }

    public static String expand(String str, boolean z) throws ExpandException {
        int i;
        if (str == null) {
            return null;
        }
        int i2 = 0;
        int indexOf = str.indexOf("${", 0);
        if (indexOf == -1) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(str.length());
        int length = str.length();
        while (true) {
            if (indexOf >= length) {
                break;
            }
            if (indexOf > i2) {
                stringBuffer.append(str.substring(i2, indexOf));
            }
            int i3 = indexOf + 2;
            if (i3 >= length || str.charAt(i3) != '{') {
                int i4 = i3;
                while (i4 < length && str.charAt(i4) != '}') {
                    i4++;
                }
                if (i4 == length) {
                    stringBuffer.append(str.substring(indexOf, i4));
                    break;
                }
                String substring = str.substring(i3, i4);
                if (substring.equals("/")) {
                    stringBuffer.append(File.separatorChar);
                } else {
                    String property = System.getProperty(substring);
                    if (property != null) {
                        if (z) {
                            try {
                                if (stringBuffer.length() > 0 || !new URI(property).isAbsolute()) {
                                    property = ParseUtil.encodePath(property);
                                }
                            } catch (URISyntaxException unused) {
                                property = ParseUtil.encodePath(property);
                            }
                        }
                        stringBuffer.append(property);
                    } else {
                        throw new ExpandException("unable to expand property " + substring);
                    }
                }
                i = i4;
            } else {
                int indexOf2 = str.indexOf("}}", i3);
                if (indexOf2 == -1 || indexOf2 + 2 == length) {
                    stringBuffer.append(str.substring(indexOf));
                } else {
                    i = indexOf2 + 1;
                    stringBuffer.append(str.substring(indexOf, i + 1));
                }
            }
            i2 = i + 1;
            indexOf = str.indexOf("${", i2);
            if (indexOf == -1) {
                if (i2 < length) {
                    stringBuffer.append(str.substring(i2, length));
                }
            }
        }
        stringBuffer.append(str.substring(indexOf));
        return stringBuffer.toString();
    }
}
