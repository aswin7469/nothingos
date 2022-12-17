package com.nothing.settings.glyphs.utils;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class UrlUtil {
    public static String addUrlParams(String str, String str2, String str3) {
        StringBuilder sb;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.trim().split("[?]");
        if (split.length <= 1 || split[1].length() <= 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(split[0]);
            sb2.append("?");
            sb2.append(str2);
            sb2.append("=");
            if (str3 == null) {
                str3 = "";
            }
            sb2.append(str3);
            return sb2.toString();
        }
        Map<String, String> parseUrlParams = parseUrlParams(str);
        parseUrlParams.put(str2, str3);
        String str4 = split[0] + "?";
        for (String next : parseUrlParams.keySet()) {
            if (str4.endsWith("?")) {
                sb = new StringBuilder();
            } else {
                sb = new StringBuilder();
                sb.append(str4);
                str4 = "&";
            }
            sb.append(str4);
            sb.append(next);
            sb.append("=");
            sb.append(parseUrlParams.get(next));
            str4 = sb.toString();
        }
        return str4;
    }

    public static String getParam(String str, String str2) {
        Map<String, String> parseUrlParams;
        if (TextUtils.isEmpty(str) || (parseUrlParams = parseUrlParams(str)) == null || !parseUrlParams.containsKey(str2)) {
            return null;
        }
        return parseUrlParams.get(str2);
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
        for (String split : truncateUrlPage.split("[&]")) {
            String[] split2 = split.split("[=]");
            if (split2.length > 1) {
                hashMap.put(split2[0], split2[1]);
            } else if (!split2[0].equals("")) {
                hashMap.put(split2[0], "");
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
