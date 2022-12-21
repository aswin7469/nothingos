package android.net.connectivity.com.android.net.module.util;

import android.text.TextUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ProxyUtils {
    private static final Pattern EXCLLIST_PATTERN = Pattern.compile(EXCLLIST_REGEXP);
    private static final String EXCLLIST_REGEXP = "^$|^[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*(\\.[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*)*(,[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*(\\.[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*)*)*$";
    private static final String EXCL_REGEX = "[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*(\\.[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*)*";
    private static final Pattern HOSTNAME_PATTERN = Pattern.compile(HOSTNAME_REGEXP);
    private static final String HOSTNAME_REGEXP = "^$|^[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*)*$";
    private static final String NAME_IP_REGEX = "[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*)*";
    public static final int PROXY_EXCLLIST_INVALID = 5;
    public static final int PROXY_HOSTNAME_EMPTY = 1;
    public static final int PROXY_HOSTNAME_INVALID = 2;
    public static final int PROXY_PORT_EMPTY = 3;
    public static final int PROXY_PORT_INVALID = 4;
    public static final int PROXY_VALID = 0;

    public static List<String> exclusionStringAsList(String str) {
        if (str == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.toLowerCase(Locale.ROOT).split(NavigationBarInflaterView.BUTTON_SEPARATOR));
    }

    public static String exclusionListAsString(String[] strArr) {
        return strArr == null ? "" : TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, strArr);
    }

    public static int validate(String str, String str2, String str3) {
        Matcher matcher = HOSTNAME_PATTERN.matcher(str);
        Matcher matcher2 = EXCLLIST_PATTERN.matcher(str3);
        if (!matcher.matches()) {
            return 2;
        }
        if (!matcher2.matches()) {
            return 5;
        }
        if (str.length() > 0 && str2.length() == 0) {
            return 3;
        }
        if (str2.length() <= 0) {
            return 0;
        }
        if (str.length() == 0) {
            return 1;
        }
        try {
            int parseInt = Integer.parseInt(str2);
            if (parseInt <= 0 || parseInt > 65535) {
                return 4;
            }
            return 0;
        } catch (NumberFormatException unused) {
        }
    }
}
