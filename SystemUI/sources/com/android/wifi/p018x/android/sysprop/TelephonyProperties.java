package com.android.wifi.p018x.android.sysprop;

import android.os.SystemProperties;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

/* renamed from: com.android.wifi.x.android.sysprop.TelephonyProperties */
public final class TelephonyProperties {
    private TelephonyProperties() {
    }

    private static Boolean tryParseBoolean(String str) {
        String lowerCase = str.toLowerCase(Locale.f700US);
        lowerCase.hashCode();
        char c = 65535;
        switch (lowerCase.hashCode()) {
            case 48:
                if (lowerCase.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 49:
                if (lowerCase.equals("1")) {
                    c = 1;
                    break;
                }
                break;
            case 3569038:
                if (lowerCase.equals("true")) {
                    c = 2;
                    break;
                }
                break;
            case 97196323:
                if (lowerCase.equals("false")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 3:
                return Boolean.FALSE;
            case 1:
            case 2:
                return Boolean.TRUE;
            default:
                return null;
        }
    }

    private static Integer tryParseInteger(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private static Integer tryParseUInt(String str) {
        try {
            return Integer.valueOf(Integer.parseUnsignedInt(str));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private static Long tryParseLong(String str) {
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private static Long tryParseULong(String str) {
        try {
            return Long.valueOf(Long.parseUnsignedLong(str));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private static Double tryParseDouble(String str) {
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static String tryParseString(String str) {
        if ("".equals(str)) {
            return null;
        }
        return str;
    }

    private static <T extends Enum<T>> T tryParseEnum(Class<T> cls, String str) {
        try {
            return Enum.valueOf(cls, str.toUpperCase(Locale.f700US));
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    private static <T> List<T> tryParseList(Function<String, T> function, String str) {
        if ("".equals(str)) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            StringBuilder sb = new StringBuilder();
            while (i < str.length() && str.charAt(i) != ',') {
                if (str.charAt(i) == '\\') {
                    i++;
                }
                if (i == str.length()) {
                    break;
                }
                sb.append(str.charAt(i));
                i++;
            }
            arrayList.add(function.apply(sb.toString()));
            if (i == str.length()) {
                return arrayList;
            }
            i++;
        }
    }

    private static <T extends Enum<T>> List<T> tryParseEnumList(Class<T> cls, String str) {
        if ("".equals(str)) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (String tryParseEnum : str.split(NavigationBarInflaterView.BUTTON_SEPARATOR)) {
            arrayList.add(tryParseEnum(cls, tryParseEnum));
        }
        return arrayList;
    }

    private static String escape(String str) {
        return str.replaceAll("([\\\\,])", "\\\\$1");
    }

    private static <T> String formatList(List<T> list) {
        String str;
        StringJoiner stringJoiner = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR);
        for (T next : list) {
            if (next == null) {
                str = "";
            } else {
                str = escape(next.toString());
            }
            stringJoiner.add(str);
        }
        return stringJoiner.toString();
    }

    private static String formatUIntList(List<Integer> list) {
        String str;
        StringJoiner stringJoiner = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR);
        for (Integer next : list) {
            if (next == null) {
                str = "";
            } else {
                str = escape(Integer.toUnsignedString(next.intValue()));
            }
            stringJoiner.add(str);
        }
        return stringJoiner.toString();
    }

    private static String formatULongList(List<Long> list) {
        String str;
        StringJoiner stringJoiner = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR);
        for (Long next : list) {
            if (next == null) {
                str = "";
            } else {
                str = escape(Long.toUnsignedString(next.longValue()));
            }
            stringJoiner.add(str);
        }
        return stringJoiner.toString();
    }

    private static <T extends Enum<T>> String formatEnumList(List<T> list, Function<T, String> function) {
        CharSequence charSequence;
        StringJoiner stringJoiner = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR);
        for (T t : list) {
            if (t == null) {
                charSequence = "";
            } else {
                charSequence = function.apply(t);
            }
            stringJoiner.add(charSequence);
        }
        return stringJoiner.toString();
    }

    public static List<String> operator_iso_country() {
        return tryParseList(new TelephonyProperties$$ExternalSyntheticLambda0(), SystemProperties.get("gsm.operator.iso-country"));
    }

    public static void operator_iso_country(List<String> list) {
        SystemProperties.set("gsm.operator.iso-country", list == null ? "" : formatList(list));
    }

    public static Optional<Boolean> in_ecm_mode() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ril.cdma.inecmmode")));
    }

    public static void in_ecm_mode(Boolean bool) {
        SystemProperties.set("ril.cdma.inecmmode", bool == null ? "" : bool.toString());
    }

    public static Optional<Long> ecm_exit_timer() {
        return Optional.ofNullable(tryParseLong(SystemProperties.get("ro.cdma.ecmexittimer")));
    }

    public static Optional<String> operator_idp_string() {
        return Optional.ofNullable(tryParseString(SystemProperties.get("gsm.operator.idpstring")));
    }

    public static void operator_idp_string(String str) {
        SystemProperties.set("gsm.operator.idpstring", str == null ? "" : str.toString());
    }

    public static Optional<Boolean> disable_call() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.telephony.disable-call")));
    }

    public static Optional<Boolean> ril_sends_multiple_call_ring() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.telephony.call_ring.multiple")));
    }

    public static Optional<Integer> call_ring_delay() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.telephony.call_ring.delay")));
    }

    public static Optional<Integer> wake_lock_timeout() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.ril.wake_lock_timeout")));
    }

    public static Optional<Boolean> reset_on_radio_tech_change() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("persist.radio.reset_on_switch")));
    }

    public static Optional<Boolean> test_csim() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("persist.radio.test-csim")));
    }

    public static Optional<Boolean> ignore_nitz() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("telephony.test.ignore.nitz")));
    }

    public static Optional<String> multi_sim_config() {
        return Optional.ofNullable(tryParseString(SystemProperties.get("persist.radio.multisim.config")));
    }

    public static void multi_sim_config(String str) {
        SystemProperties.set("persist.radio.multisim.config", str == null ? "" : str.toString());
    }

    public static Optional<Boolean> mobile_data() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.com.android.mobiledata")));
    }

    public static Optional<Integer> max_active_modems() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("telephony.active_modems.max_count")));
    }

    public static Optional<Integer> sim_slots_count() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.telephony.sim_slots.count")));
    }
}
