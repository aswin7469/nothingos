package com.nt.settings.glyphs.utils;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes2.dex */
public class UrlUtil {
    public static String addUrlParams(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.trim().split("[?]");
        if (split.length > 1 && split[1].length() > 0) {
            Map<String, String> parseUrlParams = parseUrlParams(str);
            parseUrlParams.put(str2, str3);
            String str4 = split[0] + "?";
            for (String str5 : parseUrlParams.keySet()) {
                str4 = str4.endsWith("?") ? str4 + str5 + "=" + parseUrlParams.get(str5) : str4 + "&" + str5 + "=" + parseUrlParams.get(str5);
            }
            return str4;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(split[0]);
        sb.append("?");
        sb.append(str2);
        sb.append("=");
        if (str3 == null) {
            str3 = "";
        }
        sb.append(str3);
        return sb.toString();
    }

    public static String getParam(String str, String str2) {
        Map<String, String> parseUrlParams;
        if (!TextUtils.isEmpty(str) && (parseUrlParams = parseUrlParams(str)) != null && parseUrlParams.containsKey(str2)) {
            return parseUrlParams.get(str2);
        }
        return null;
    }

    public static Map<String, String> parseUrlParams(String str) {
        HashMap hashMap = new HashMap();
        if (TextUtils.isEmpty(str)) {
            return hashMap;
        }
        String truncateUrlPage = truncateUrlPage(str);
        if (TextUtils.isEmpty(truncateUrlPage)) {
            return hashMap;
        }
        for (String str2 : truncateUrlPage.split("[&]")) {
            String[] split = str2.split("[=]");
            if (split.length > 1) {
                hashMap.put(split[0], split[1]);
            } else if (split[0] != "") {
                hashMap.put(split[0], "");
            }
        }
        return hashMap;
    }

    private static String truncateUrlPage(String str) {
        String[] split = str.trim().split("[?]");
        if (split.length < 2) {
            return null;
        }
        return split[1];
    }

    public static String getUrlHost(String str) {
        String[] split = str.trim().split("[?]");
        if (split.length == 0) {
            return null;
        }
        return split[0];
    }
}
