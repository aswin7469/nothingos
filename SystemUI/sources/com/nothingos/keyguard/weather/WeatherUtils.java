package com.nothingos.keyguard.weather;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
/* loaded from: classes2.dex */
public class WeatherUtils {
    private static List<String> NDOT_FONT_SUPPORT_LOCAL_LIST;

    static {
        LinkedList linkedList = new LinkedList();
        NDOT_FONT_SUPPORT_LOCAL_LIST = linkedList;
        linkedList.add(Locale.ENGLISH.getLanguage());
        NDOT_FONT_SUPPORT_LOCAL_LIST.add(Locale.FRANCE.getLanguage());
        NDOT_FONT_SUPPORT_LOCAL_LIST.add(Locale.GERMAN.getLanguage());
        NDOT_FONT_SUPPORT_LOCAL_LIST.add(Locale.ITALIAN.getLanguage());
        NDOT_FONT_SUPPORT_LOCAL_LIST.add("af");
        NDOT_FONT_SUPPORT_LOCAL_LIST.add("da");
        NDOT_FONT_SUPPORT_LOCAL_LIST.add("es");
        NDOT_FONT_SUPPORT_LOCAL_LIST.add("et");
        NDOT_FONT_SUPPORT_LOCAL_LIST.add("fi");
        NDOT_FONT_SUPPORT_LOCAL_LIST.add("tl");
        NDOT_FONT_SUPPORT_LOCAL_LIST.add("nb");
        NDOT_FONT_SUPPORT_LOCAL_LIST.add("sv");
    }

    public static boolean isNdotFontSupport() {
        return NDOT_FONT_SUPPORT_LOCAL_LIST.contains(Locale.getDefault().getLanguage());
    }
}
