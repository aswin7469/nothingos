package com.nothing.settings.utils;

import android.annotation.TargetApi;
import android.util.ArrayMap;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class CommonUtils {
    private static List<String> NDOT_FONT_SUPPORT_LOCAL_LIST;
    private static int sGestureMode;

    public static void updateGestureMode(int i) {
        if (sGestureMode != i) {
            sGestureMode = i;
            writeGestureNode("/sys/bus/spi/devices/spi0.0/fts_gesture_mode", i);
        }
    }

    private static void writeGestureNode(String str, int i) {
        OutputStream outputStream = null;
        try {
            Log.d("Settings.NtUtils", "writeGestureNode:" + str + ", data:" + i);
            outputStream = Files.newOutputStream(Paths.get(str, new String[0]), new OpenOption[0]);
            outputStream.write(String.valueOf(i).getBytes("US-ASCII"));
        } catch (IOException e) {
            Log.e("Settings.NtUtils", "Unable to write " + str + e.getMessage());
            e.printStackTrace();
            if (outputStream == null) {
                return;
            }
        } catch (Throwable th) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException unused) {
                }
            }
            throw th;
        }
        try {
            outputStream.close();
        } catch (IOException unused2) {
        }
    }

    @TargetApi(19)
    public static String getCountryFromMcc(String str) {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put("289", "GE-AB");
        arrayMap.put("412", "AF");
        arrayMap.put("276", "AL");
        arrayMap.put("603", "DZ");
        arrayMap.put("544", "AS");
        arrayMap.put("213", "AD");
        arrayMap.put("631", "AO");
        arrayMap.put("365", "AI");
        arrayMap.put("344", "AG");
        arrayMap.put("722", "AR");
        arrayMap.put("283", "AM");
        arrayMap.put("363", "AW");
        arrayMap.put("505", "AU");
        arrayMap.put("232", "AT");
        arrayMap.put("400", "AZ");
        arrayMap.put("364", "BS");
        arrayMap.put("426", "BH");
        arrayMap.put("470", "BD");
        arrayMap.put("342", "BB");
        arrayMap.put("257", "BY");
        arrayMap.put("206", "BE");
        arrayMap.put("702", "BZ");
        arrayMap.put("616", "BJ");
        arrayMap.put("350", "BM");
        arrayMap.put("402", "BT");
        arrayMap.put("736", "BO");
        arrayMap.put("362", "BQ/CW/SX");
        arrayMap.put("218", "BA");
        arrayMap.put("652", "BW");
        arrayMap.put("724", "BR");
        arrayMap.put("995", "IO");
        arrayMap.put("348", "VG");
        arrayMap.put("528", "BN");
        arrayMap.put("284", "BG");
        arrayMap.put("613", "BF");
        arrayMap.put("642", "BI");
        arrayMap.put("456", "KH");
        arrayMap.put("624", "CM");
        arrayMap.put("302", "CA");
        arrayMap.put("625", "CV");
        arrayMap.put("346", "KY");
        arrayMap.put("623", "CF");
        arrayMap.put("622", "TD");
        arrayMap.put("730", "CL");
        arrayMap.put("460", "CN");
        arrayMap.put("461", "CN");
        arrayMap.put("732", "CO");
        arrayMap.put("654", "KM");
        arrayMap.put("629", "CG");
        arrayMap.put("548", "CK");
        arrayMap.put("712", "CR");
        arrayMap.put("219", "HR");
        arrayMap.put("368", "CU");
        arrayMap.put("280", "CY");
        arrayMap.put("230", "CZ");
        arrayMap.put("630", "CD");
        arrayMap.put("238", "DK");
        arrayMap.put("638", "DJ");
        arrayMap.put("366", "DM");
        arrayMap.put("370", "DO");
        arrayMap.put("514", "TL");
        arrayMap.put("740", "EC");
        arrayMap.put("602", "EG");
        arrayMap.put("706", "SV");
        arrayMap.put("627", "GQ");
        arrayMap.put("657", "ER");
        arrayMap.put("248", "EE");
        arrayMap.put("636", "ET");
        arrayMap.put("750", "FK");
        arrayMap.put("288", "FO");
        arrayMap.put("542", "FJ");
        arrayMap.put("244", "FI");
        arrayMap.put("208", "FR");
        arrayMap.put("742", "GF");
        arrayMap.put("647", "RE/YT");
        arrayMap.put("547", "PF");
        arrayMap.put("628", "GA");
        arrayMap.put("607", "GM");
        arrayMap.put("282", "GE");
        arrayMap.put("262", "DE");
        arrayMap.put("620", "GH");
        arrayMap.put("266", "GI");
        arrayMap.put("202", "GR");
        arrayMap.put("290", "GL");
        arrayMap.put("352", "GD");
        arrayMap.put("340", "GP/MQ/BL/MF");
        arrayMap.put("704", "GT");
        arrayMap.put("611", "GN");
        arrayMap.put("632", "GW");
        arrayMap.put("738", "GY");
        arrayMap.put("372", "HT");
        arrayMap.put("708", "HN");
        arrayMap.put("454", "HK");
        arrayMap.put("216", "HU");
        arrayMap.put("274", "IS");
        arrayMap.put("404", "IN");
        arrayMap.put("405", "IN");
        arrayMap.put("406", "IN");
        arrayMap.put("510", "ID");
        arrayMap.put("432", "IR");
        arrayMap.put("418", "IQ");
        arrayMap.put("272", "IE");
        arrayMap.put("425", "IL");
        arrayMap.put("222", "IT");
        arrayMap.put("612", "CI");
        arrayMap.put("338", "JM");
        arrayMap.put("440", "JP");
        arrayMap.put("441", "JP");
        arrayMap.put("416", "JO");
        arrayMap.put("401", "KZ");
        arrayMap.put("639", "KE");
        arrayMap.put("545", "KI");
        arrayMap.put("467", "KP");
        arrayMap.put("450", "KR");
        arrayMap.put("221", "XK");
        arrayMap.put("419", "KW");
        arrayMap.put("437", "KG");
        arrayMap.put("457", "LA");
        arrayMap.put("247", "LV");
        arrayMap.put("415", "LB");
        arrayMap.put("651", "LS");
        arrayMap.put("618", "LR");
        arrayMap.put("606", "LY");
        arrayMap.put("295", "LI");
        arrayMap.put("246", "LT");
        arrayMap.put("270", "LU");
        arrayMap.put("455", "MO");
        arrayMap.put("294", "MK");
        arrayMap.put("646", "MG");
        arrayMap.put("650", "MW");
        arrayMap.put("502", "MY");
        arrayMap.put("472", "MV");
        arrayMap.put("610", "ML");
        arrayMap.put("278", "MT");
        arrayMap.put("551", "MH");
        arrayMap.put("609", "MR");
        arrayMap.put("617", "MU");
        arrayMap.put("334", "MX");
        arrayMap.put("550", "FM");
        arrayMap.put("259", "MD");
        arrayMap.put("212", "MC");
        arrayMap.put("428", "MN");
        arrayMap.put("297", "ME");
        arrayMap.put("354", "MS");
        arrayMap.put("604", "MA");
        arrayMap.put("643", "MZ");
        arrayMap.put("414", "MM");
        arrayMap.put("649", "NA");
        arrayMap.put("536", "NR");
        arrayMap.put("429", "NP");
        arrayMap.put("204", "NL");
        arrayMap.put("546", "NC");
        arrayMap.put("530", "NZ");
        arrayMap.put("710", "NI");
        arrayMap.put("614", "NE");
        arrayMap.put("621", "NG");
        arrayMap.put("555", "NU");
        arrayMap.put("242", "NO");
        arrayMap.put("422", "OM");
        arrayMap.put("410", "PK");
        arrayMap.put("552", "PW");
        arrayMap.put("714", "PA");
        arrayMap.put("537", "PG");
        arrayMap.put("744", "PY");
        arrayMap.put("716", "PE");
        arrayMap.put("515", "PH");
        arrayMap.put("260", "PL");
        arrayMap.put("268", "PT");
        arrayMap.put("330", "PR");
        arrayMap.put("427", "QA");
        arrayMap.put("226", "RO");
        arrayMap.put("250", "RU");
        arrayMap.put("635", "RW");
        arrayMap.put("658", "SH");
        arrayMap.put("356", "KN");
        arrayMap.put("358", "LC");
        arrayMap.put("308", "PM");
        arrayMap.put("360", "VC");
        arrayMap.put("549", "WS");
        arrayMap.put("292", "SM");
        arrayMap.put("626", "ST");
        arrayMap.put("420", "SA");
        arrayMap.put("608", "SN");
        arrayMap.put("220", "RS");
        arrayMap.put("633", "SC");
        arrayMap.put("619", "SL");
        arrayMap.put("525", "SG");
        arrayMap.put("231", "SK");
        arrayMap.put("293", "SI");
        arrayMap.put("540", "SB");
        arrayMap.put("637", "SO");
        arrayMap.put("655", "ZA");
        arrayMap.put("659", "SS");
        arrayMap.put("214", "ES");
        arrayMap.put("413", "LK");
        arrayMap.put("634", "SD");
        arrayMap.put("746", "SR");
        arrayMap.put("653", "SZ");
        arrayMap.put("240", "SE");
        arrayMap.put("228", "CH");
        arrayMap.put("417", "SY");
        arrayMap.put("466", "TW");
        arrayMap.put("436", "TJ");
        arrayMap.put("640", "TZ");
        arrayMap.put("520", "TH");
        arrayMap.put("615", "TG");
        arrayMap.put("554", "TK");
        arrayMap.put("539", "TO");
        arrayMap.put("374", "TT");
        arrayMap.put("605", "TN");
        arrayMap.put("286", "TR");
        arrayMap.put("438", "TM");
        arrayMap.put("376", "TC");
        arrayMap.put("553", "TV");
        arrayMap.put("641", "UG");
        arrayMap.put("255", "UA");
        arrayMap.put("424", "AE");
        arrayMap.put("430", "AE");
        arrayMap.put("431", "AE");
        arrayMap.put("234", "GB");
        arrayMap.put("235", "GB");
        arrayMap.put("310", "US");
        arrayMap.put("311", "US");
        arrayMap.put("312", "US");
        arrayMap.put("313", "US");
        arrayMap.put("314", "US");
        arrayMap.put("315", "US");
        arrayMap.put("316", "US");
        arrayMap.put("332", "VI");
        arrayMap.put("748", "UY");
        arrayMap.put("434", "UZ");
        arrayMap.put("541", "VU");
        arrayMap.put("734", "VE");
        arrayMap.put("452", "VN");
        arrayMap.put("543", "WF");
        arrayMap.put("421", "YE");
        arrayMap.put("645", "ZM");
        arrayMap.put("648", "ZW");
        String str2 = (String) arrayMap.get(str);
        return str2 != null ? str2 : "unknown";
    }

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
}
