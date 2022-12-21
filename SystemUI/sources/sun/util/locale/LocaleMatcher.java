package sun.util.locale;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class LocaleMatcher {
    public static List<Locale> filter(List<Locale.LanguageRange> list, Collection<Locale> collection, Locale.FilteringMode filteringMode) {
        if (list.isEmpty() || collection.isEmpty()) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (Locale languageTag : collection) {
            arrayList.add(languageTag.toLanguageTag());
        }
        List<String> filterTags = filterTags(list, arrayList, filteringMode);
        ArrayList arrayList2 = new ArrayList(filterTags.size());
        for (String forLanguageTag : filterTags) {
            arrayList2.add(Locale.forLanguageTag(forLanguageTag));
        }
        return arrayList2;
    }

    public static List<String> filterTags(List<Locale.LanguageRange> list, Collection<String> collection, Locale.FilteringMode filteringMode) {
        String str;
        if (list.isEmpty() || collection.isEmpty()) {
            return new ArrayList();
        }
        if (filteringMode == Locale.FilteringMode.EXTENDED_FILTERING) {
            return filterExtended(list, collection);
        }
        ArrayList arrayList = new ArrayList();
        for (Locale.LanguageRange next : list) {
            String range = next.getRange();
            if (!range.startsWith("*-") && range.indexOf("-*") == -1) {
                arrayList.add(next);
            } else if (filteringMode == Locale.FilteringMode.AUTOSELECT_FILTERING) {
                return filterExtended(list, collection);
            } else {
                if (filteringMode == Locale.FilteringMode.MAP_EXTENDED_RANGES) {
                    if (range.charAt(0) == '*') {
                        str = "*";
                    } else {
                        str = range.replaceAll("-[*]", "");
                    }
                    arrayList.add(new Locale.LanguageRange(str, next.getWeight()));
                } else if (filteringMode == Locale.FilteringMode.REJECT_EXTENDED_RANGES) {
                    throw new IllegalArgumentException("An extended range \"" + range + "\" found in REJECT_EXTENDED_RANGES mode.");
                }
            }
        }
        return filterBasic(arrayList, collection);
    }

    private static List<String> filterBasic(List<Locale.LanguageRange> list, Collection<String> collection) {
        int length;
        ArrayList arrayList = new ArrayList();
        for (Locale.LanguageRange range : list) {
            String range2 = range.getRange();
            if (range2.equals("*")) {
                return new ArrayList(collection);
            }
            for (String lowerCase : collection) {
                String lowerCase2 = lowerCase.toLowerCase();
                if (lowerCase2.startsWith(range2) && ((lowerCase2.length() == (length = range2.length()) || lowerCase2.charAt(length) == '-') && !arrayList.contains(lowerCase2))) {
                    arrayList.add(lowerCase2);
                }
            }
        }
        return arrayList;
    }

    private static List<String> filterExtended(List<Locale.LanguageRange> list, Collection<String> collection) {
        ArrayList arrayList = new ArrayList();
        for (Locale.LanguageRange range : list) {
            String range2 = range.getRange();
            if (range2.equals("*")) {
                return new ArrayList(collection);
            }
            String[] split = range2.split(LanguageTag.SEP);
            for (String lowerCase : collection) {
                String lowerCase2 = lowerCase.toLowerCase();
                String[] split2 = lowerCase2.split(LanguageTag.SEP);
                if (split[0].equals(split2[0]) || split[0].equals("*")) {
                    int i = 1;
                    int i2 = 1;
                    while (i < split.length && i2 < split2.length) {
                        if (split[i].equals("*")) {
                            i++;
                        } else {
                            if (!split[i].equals(split2[i2])) {
                                if (split2[i2].length() == 1 && !split2[i2].equals("*")) {
                                    break;
                                }
                            } else {
                                i++;
                            }
                            i2++;
                        }
                    }
                    if (split.length == i && !arrayList.contains(lowerCase2)) {
                        arrayList.add(lowerCase2);
                    }
                }
            }
        }
        return arrayList;
    }

    public static Locale lookup(List<Locale.LanguageRange> list, Collection<Locale> collection) {
        if (list.isEmpty() || collection.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (Locale languageTag : collection) {
            arrayList.add(languageTag.toLanguageTag());
        }
        String lookupTag = lookupTag(list, arrayList);
        if (lookupTag == null) {
            return null;
        }
        return Locale.forLanguageTag(lookupTag);
    }

    public static String lookupTag(List<Locale.LanguageRange> list, Collection<String> collection) {
        if (!list.isEmpty() && !collection.isEmpty()) {
            for (Locale.LanguageRange range : list) {
                String range2 = range.getRange();
                if (!range2.equals("*")) {
                    String replace = range2.replace((CharSequence) "*", (CharSequence) "\\p{Alnum}*");
                    while (replace.length() > 0) {
                        for (String lowerCase : collection) {
                            String lowerCase2 = lowerCase.toLowerCase();
                            if (lowerCase2.matches(replace)) {
                                return lowerCase2;
                            }
                        }
                        int lastIndexOf = replace.lastIndexOf(45);
                        if (lastIndexOf >= 0) {
                            replace = replace.substring(0, lastIndexOf);
                            if (replace.lastIndexOf(45) == replace.length() - 2) {
                                replace = replace.substring(0, replace.length() - 2);
                            }
                        } else {
                            replace = "";
                        }
                    }
                    continue;
                }
            }
        }
        return null;
    }

    public static List<Locale.LanguageRange> parse(String str) {
        String lowerCase = str.replace((CharSequence) WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, (CharSequence) "").toLowerCase();
        if (lowerCase.startsWith("accept-language:")) {
            lowerCase = lowerCase.substring(16);
        }
        String[] split = lowerCase.split(NavigationBarInflaterView.BUTTON_SEPARATOR);
        ArrayList arrayList = new ArrayList(split.length);
        ArrayList arrayList2 = new ArrayList();
        int length = split.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            String str2 = split[i2];
            int indexOf = str2.indexOf(";q=");
            double d = 1.0d;
            if (indexOf != -1) {
                String substring = str2.substring(0, indexOf);
                int i3 = indexOf + 3;
                try {
                    double parseDouble = Double.parseDouble(str2.substring(i3));
                    if (parseDouble < 0.0d || parseDouble > 1.0d) {
                        throw new IllegalArgumentException("weight=" + parseDouble + " for language range \"" + substring + "\". It must be between 0.0 and 1.0.");
                    }
                    d = parseDouble;
                    str2 = substring;
                } catch (Exception unused) {
                    throw new IllegalArgumentException("weight=\"" + str2.substring(i3) + "\" for language range \"" + substring + "\"");
                }
            }
            if (!arrayList2.contains(str2)) {
                Locale.LanguageRange languageRange = new Locale.LanguageRange(str2, d);
                int i4 = 0;
                while (true) {
                    if (i4 >= i) {
                        i4 = i;
                        break;
                    } else if (((Locale.LanguageRange) arrayList.get(i4)).getWeight() < d) {
                        break;
                    } else {
                        i4++;
                    }
                }
                arrayList.add(i4, languageRange);
                i++;
                arrayList2.add(str2);
                String equivalentForRegionAndVariant = getEquivalentForRegionAndVariant(str2);
                if (equivalentForRegionAndVariant != null && !arrayList2.contains(equivalentForRegionAndVariant)) {
                    arrayList.add(i4 + 1, new Locale.LanguageRange(equivalentForRegionAndVariant, d));
                    i++;
                    arrayList2.add(equivalentForRegionAndVariant);
                }
                String[] equivalentsForLanguage = getEquivalentsForLanguage(str2);
                if (equivalentsForLanguage != null) {
                    for (String str3 : equivalentsForLanguage) {
                        if (!arrayList2.contains(str3)) {
                            arrayList.add(i4 + 1, new Locale.LanguageRange(str3, d));
                            i++;
                            arrayList2.add(str3);
                        }
                        String equivalentForRegionAndVariant2 = getEquivalentForRegionAndVariant(str3);
                        if (equivalentForRegionAndVariant2 != null && !arrayList2.contains(equivalentForRegionAndVariant2)) {
                            arrayList.add(i4 + 1, new Locale.LanguageRange(equivalentForRegionAndVariant2, d));
                            i++;
                            arrayList2.add(equivalentForRegionAndVariant2);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    private static String replaceFirstSubStringMatch(String str, String str2, String str3) {
        int indexOf = str.indexOf(str2);
        if (indexOf == -1) {
            return str;
        }
        return str.substring(0, indexOf) + str3 + str.substring(indexOf + str2.length());
    }

    private static String[] getEquivalentsForLanguage(String str) {
        String str2 = str;
        while (str2.length() > 0) {
            if (LocaleEquivalentMaps.singleEquivMap.containsKey(str2)) {
                return new String[]{replaceFirstSubStringMatch(str, str2, LocaleEquivalentMaps.singleEquivMap.get(str2))};
            } else if (LocaleEquivalentMaps.multiEquivsMap.containsKey(str2)) {
                String[] strArr = LocaleEquivalentMaps.multiEquivsMap.get(str2);
                String[] strArr2 = new String[strArr.length];
                for (int i = 0; i < strArr.length; i++) {
                    strArr2[i] = replaceFirstSubStringMatch(str, str2, strArr[i]);
                }
                return strArr2;
            } else {
                int lastIndexOf = str2.lastIndexOf(45);
                if (lastIndexOf == -1) {
                    return null;
                }
                str2 = str2.substring(0, lastIndexOf);
            }
        }
        return null;
    }

    private static String getEquivalentForRegionAndVariant(String str) {
        int length;
        int extentionKeyIndex = getExtentionKeyIndex(str);
        for (String next : LocaleEquivalentMaps.regionVariantEquivMap.keySet()) {
            int indexOf = str.indexOf(next);
            if (indexOf != -1 && ((extentionKeyIndex == Integer.MIN_VALUE || indexOf <= extentionKeyIndex) && (str.length() == (length = indexOf + next.length()) || str.charAt(length) == '-'))) {
                return replaceFirstSubStringMatch(str, next, LocaleEquivalentMaps.regionVariantEquivMap.get(next));
            }
        }
        return null;
    }

    private static int getExtentionKeyIndex(String str) {
        char[] charArray = str.toCharArray();
        int i = Integer.MIN_VALUE;
        for (int i2 = 1; i2 < charArray.length; i2++) {
            if (charArray[i2] == '-') {
                if (i2 - i == 2) {
                    return i;
                }
                i = i2;
            }
        }
        return Integer.MIN_VALUE;
    }

    public static List<Locale.LanguageRange> mapEquivalents(List<Locale.LanguageRange> list, Map<String, List<String>> map) {
        boolean z;
        if (list.isEmpty()) {
            return new ArrayList();
        }
        if (map == null || map.isEmpty()) {
            return new ArrayList(list);
        }
        HashMap hashMap = new HashMap();
        for (String next : map.keySet()) {
            hashMap.put(next.toLowerCase(), next);
        }
        ArrayList arrayList = new ArrayList();
        for (Locale.LanguageRange next2 : list) {
            String range = next2.getRange();
            String str = range;
            while (true) {
                z = false;
                if (str.length() <= 0) {
                    break;
                } else if (hashMap.containsKey(str)) {
                    List<String> list2 = map.get(hashMap.get(str));
                    if (list2 != null) {
                        int length = str.length();
                        for (String lowerCase : list2) {
                            arrayList.add(new Locale.LanguageRange(lowerCase.toLowerCase() + range.substring(length), next2.getWeight()));
                        }
                    }
                    z = true;
                } else {
                    int lastIndexOf = str.lastIndexOf(45);
                    if (lastIndexOf == -1) {
                        break;
                    }
                    str = str.substring(0, lastIndexOf);
                }
            }
            if (!z) {
                arrayList.add(next2);
            }
        }
        return arrayList;
    }

    private LocaleMatcher() {
    }
}
