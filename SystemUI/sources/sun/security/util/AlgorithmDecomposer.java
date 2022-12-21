package sun.security.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import sun.util.locale.LanguageTag;

public class AlgorithmDecomposer {
    private static final Pattern pattern = Pattern.compile("with|and", 2);
    private static final Pattern transPattern = Pattern.compile("/");

    private static Set<String> decomposeImpl(String str) {
        String[] split = transPattern.split(str);
        HashSet hashSet = new HashSet();
        for (String str2 : split) {
            if (!(str2 == null || str2.length() == 0)) {
                for (String str3 : pattern.split(str2)) {
                    if (!(str3 == null || str3.length() == 0)) {
                        hashSet.add(str3);
                    }
                }
            }
        }
        return hashSet;
    }

    public Set<String> decompose(String str) {
        if (str == null || str.length() == 0) {
            return new HashSet();
        }
        Set<String> decomposeImpl = decomposeImpl(str);
        if (decomposeImpl.contains("SHA1") && !decomposeImpl.contains("SHA-1")) {
            decomposeImpl.add("SHA-1");
        }
        if (decomposeImpl.contains("SHA-1") && !decomposeImpl.contains("SHA1")) {
            decomposeImpl.add("SHA1");
        }
        if (decomposeImpl.contains("SHA224") && !decomposeImpl.contains("SHA-224")) {
            decomposeImpl.add("SHA-224");
        }
        if (decomposeImpl.contains("SHA-224") && !decomposeImpl.contains("SHA224")) {
            decomposeImpl.add("SHA224");
        }
        if (decomposeImpl.contains("SHA256") && !decomposeImpl.contains("SHA-256")) {
            decomposeImpl.add("SHA-256");
        }
        if (decomposeImpl.contains("SHA-256") && !decomposeImpl.contains("SHA256")) {
            decomposeImpl.add("SHA256");
        }
        if (decomposeImpl.contains("SHA384") && !decomposeImpl.contains("SHA-384")) {
            decomposeImpl.add("SHA-384");
        }
        if (decomposeImpl.contains("SHA-384") && !decomposeImpl.contains("SHA384")) {
            decomposeImpl.add("SHA384");
        }
        if (decomposeImpl.contains("SHA512") && !decomposeImpl.contains("SHA-512")) {
            decomposeImpl.add("SHA-512");
        }
        if (decomposeImpl.contains("SHA-512") && !decomposeImpl.contains("SHA512")) {
            decomposeImpl.add("SHA512");
        }
        return decomposeImpl;
    }

    private static void hasLoop(Set<String> set, String str, String str2) {
        if (set.contains(str)) {
            if (!set.contains(str2)) {
                set.add(str2);
            }
            set.remove(str);
        }
    }

    public static Set<String> decomposeOneHash(String str) {
        if (str == null || str.length() == 0) {
            return new HashSet();
        }
        Set<String> decomposeImpl = decomposeImpl(str);
        hasLoop(decomposeImpl, "SHA-1", "SHA1");
        hasLoop(decomposeImpl, "SHA-224", "SHA224");
        hasLoop(decomposeImpl, "SHA-256", "SHA256");
        hasLoop(decomposeImpl, "SHA-384", "SHA384");
        hasLoop(decomposeImpl, "SHA-512", "SHA512");
        return decomposeImpl;
    }

    public static String hashName(String str) {
        return str.replace((CharSequence) LanguageTag.SEP, (CharSequence) "");
    }
}
