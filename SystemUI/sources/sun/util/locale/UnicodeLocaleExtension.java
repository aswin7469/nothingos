package sun.util.locale;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

public class UnicodeLocaleExtension extends Extension {
    public static final UnicodeLocaleExtension CA_JAPANESE = new UnicodeLocaleExtension("ca", "japanese");
    public static final UnicodeLocaleExtension NU_THAI = new UnicodeLocaleExtension("nu", "thai");
    public static final char SINGLETON = 'u';
    private final Set<String> attributes;
    private final Map<String, String> keywords;

    public /* bridge */ /* synthetic */ String getID() {
        return super.getID();
    }

    public /* bridge */ /* synthetic */ char getKey() {
        return super.getKey();
    }

    public /* bridge */ /* synthetic */ String getValue() {
        return super.getValue();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    private UnicodeLocaleExtension(String str, String str2) {
        super('u', str + LanguageTag.SEP + str2);
        this.attributes = Collections.emptySet();
        this.keywords = Collections.singletonMap(str, str2);
    }

    UnicodeLocaleExtension(SortedSet<String> sortedSet, SortedMap<String, String> sortedMap) {
        super('u');
        if (sortedSet != null) {
            this.attributes = sortedSet;
        } else {
            this.attributes = Collections.emptySet();
        }
        if (sortedMap != null) {
            this.keywords = sortedMap;
        } else {
            this.keywords = Collections.emptyMap();
        }
        if (!this.attributes.isEmpty() || !this.keywords.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String append : this.attributes) {
                sb.append(LanguageTag.SEP);
                sb.append(append);
            }
            for (Map.Entry next : this.keywords.entrySet()) {
                String str = (String) next.getValue();
                sb.append(LanguageTag.SEP);
                sb.append((String) next.getKey());
                if (str.length() > 0) {
                    sb.append(LanguageTag.SEP);
                    sb.append(str);
                }
            }
            setValue(sb.substring(1));
        }
    }

    public Set<String> getUnicodeLocaleAttributes() {
        if (this.attributes == Collections.EMPTY_SET) {
            return this.attributes;
        }
        return Collections.unmodifiableSet(this.attributes);
    }

    public Set<String> getUnicodeLocaleKeys() {
        if (this.keywords == Collections.EMPTY_MAP) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(this.keywords.keySet());
    }

    public String getUnicodeLocaleType(String str) {
        return this.keywords.get(str);
    }

    public static boolean isSingletonChar(char c) {
        return 'u' == LocaleUtils.toLower(c);
    }

    public static boolean isAttribute(String str) {
        int length = str.length();
        return length >= 3 && length <= 8 && LocaleUtils.isAlphaNumericString(str);
    }

    public static boolean isKey(String str) {
        return str.length() == 2 && LocaleUtils.isAlphaNumericString(str);
    }

    public static boolean isTypeSubtag(String str) {
        int length = str.length();
        return length >= 3 && length <= 8 && LocaleUtils.isAlphaNumericString(str);
    }
}
