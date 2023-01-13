package sun.util.locale;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import sun.util.locale.InternalLocaleBuilder;

public class LocaleExtensions {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final LocaleExtensions CALENDAR_JAPANESE = new LocaleExtensions("u-ca-japanese", (Character) 'u', (Extension) UnicodeLocaleExtension.CA_JAPANESE);
    public static final LocaleExtensions NUMBER_THAI = new LocaleExtensions("u-nu-thai", (Character) 'u', (Extension) UnicodeLocaleExtension.NU_THAI);
    private final Map<Character, Extension> extensionMap;

    /* renamed from: id */
    private final String f938id;

    private LocaleExtensions(String str, Character ch, Extension extension) {
        this.f938id = str;
        this.extensionMap = Collections.singletonMap(ch, extension);
    }

    LocaleExtensions(Map<InternalLocaleBuilder.CaseInsensitiveChar, String> map, Set<InternalLocaleBuilder.CaseInsensitiveString> set, Map<InternalLocaleBuilder.CaseInsensitiveString, String> map2) {
        TreeSet treeSet;
        boolean z = !LocaleUtils.isEmpty((Map<?, ?>) map);
        boolean z2 = !LocaleUtils.isEmpty((Set<?>) set);
        boolean z3 = !LocaleUtils.isEmpty((Map<?, ?>) map2);
        if (z || z2 || z3) {
            TreeMap treeMap = new TreeMap();
            if (z) {
                for (Map.Entry next : map.entrySet()) {
                    char lower = LocaleUtils.toLower(((InternalLocaleBuilder.CaseInsensitiveChar) next.getKey()).value());
                    String str = (String) next.getValue();
                    if (!LanguageTag.isPrivateusePrefixChar(lower) || (str = InternalLocaleBuilder.removePrivateuseVariant(str)) != null) {
                        treeMap.put(Character.valueOf(lower), new Extension(lower, LocaleUtils.toLowerString(str)));
                    }
                }
            }
            if (z2 || z3) {
                TreeMap treeMap2 = null;
                if (z2) {
                    treeSet = new TreeSet();
                    for (InternalLocaleBuilder.CaseInsensitiveString value : set) {
                        treeSet.add(LocaleUtils.toLowerString(value.value()));
                    }
                } else {
                    treeSet = null;
                }
                if (z3) {
                    treeMap2 = new TreeMap();
                    for (Map.Entry next2 : map2.entrySet()) {
                        treeMap2.put(LocaleUtils.toLowerString(((InternalLocaleBuilder.CaseInsensitiveString) next2.getKey()).value()), LocaleUtils.toLowerString((String) next2.getValue()));
                    }
                }
                treeMap.put('u', new UnicodeLocaleExtension((SortedSet<String>) treeSet, (SortedMap<String, String>) treeMap2));
            }
            if (treeMap.isEmpty()) {
                this.f938id = "";
                this.extensionMap = Collections.emptyMap();
                return;
            }
            this.f938id = toID(treeMap);
            this.extensionMap = treeMap;
            return;
        }
        this.f938id = "";
        this.extensionMap = Collections.emptyMap();
    }

    public Set<Character> getKeys() {
        if (this.extensionMap.isEmpty()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(this.extensionMap.keySet());
    }

    public Extension getExtension(Character ch) {
        return this.extensionMap.get(Character.valueOf(LocaleUtils.toLower(ch.charValue())));
    }

    public String getExtensionValue(Character ch) {
        Extension extension = this.extensionMap.get(Character.valueOf(LocaleUtils.toLower(ch.charValue())));
        if (extension == null) {
            return null;
        }
        return extension.getValue();
    }

    public Set<String> getUnicodeLocaleAttributes() {
        Extension extension = this.extensionMap.get('u');
        if (extension == null) {
            return Collections.emptySet();
        }
        return ((UnicodeLocaleExtension) extension).getUnicodeLocaleAttributes();
    }

    public Set<String> getUnicodeLocaleKeys() {
        Extension extension = this.extensionMap.get('u');
        if (extension == null) {
            return Collections.emptySet();
        }
        return ((UnicodeLocaleExtension) extension).getUnicodeLocaleKeys();
    }

    public String getUnicodeLocaleType(String str) {
        Extension extension = this.extensionMap.get('u');
        if (extension == null) {
            return null;
        }
        return ((UnicodeLocaleExtension) extension).getUnicodeLocaleType(LocaleUtils.toLowerString(str));
    }

    public boolean isEmpty() {
        return this.extensionMap.isEmpty();
    }

    public static boolean isValidKey(char c) {
        return LanguageTag.isExtensionSingletonChar(c) || LanguageTag.isPrivateusePrefixChar(c);
    }

    public static boolean isValidUnicodeLocaleKey(String str) {
        return UnicodeLocaleExtension.isKey(str);
    }

    private static String toID(SortedMap<Character, Extension> sortedMap) {
        StringBuilder sb = new StringBuilder();
        Extension extension = null;
        for (Map.Entry next : sortedMap.entrySet()) {
            char charValue = ((Character) next.getKey()).charValue();
            Extension extension2 = (Extension) next.getValue();
            if (LanguageTag.isPrivateusePrefixChar(charValue)) {
                extension = extension2;
            } else {
                if (sb.length() > 0) {
                    sb.append(LanguageTag.SEP);
                }
                sb.append((Object) extension2);
            }
        }
        if (extension != null) {
            if (sb.length() > 0) {
                sb.append(LanguageTag.SEP);
            }
            sb.append((Object) extension);
        }
        return sb.toString();
    }

    public String toString() {
        return this.f938id;
    }

    public String getID() {
        return this.f938id;
    }

    public int hashCode() {
        return this.f938id.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocaleExtensions)) {
            return false;
        }
        return this.f938id.equals(((LocaleExtensions) obj).f938id);
    }
}
