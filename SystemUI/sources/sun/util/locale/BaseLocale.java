package sun.util.locale;

import java.lang.ref.SoftReference;

public final class BaseLocale {
    private static final Cache CACHE = new Cache();
    public static final String SEP = "_";
    private volatile int hash;
    private final String language;
    private final String region;
    private final String script;
    private final String variant;

    private BaseLocale(String str, String str2) {
        this.hash = 0;
        this.language = str;
        this.script = "";
        this.region = str2;
        this.variant = "";
    }

    private BaseLocale(String str, String str2, String str3, String str4) {
        this.hash = 0;
        String str5 = "";
        this.language = str != null ? LocaleUtils.toLowerString(str).intern() : str5;
        this.script = str2 != null ? LocaleUtils.toTitleString(str2).intern() : str5;
        this.region = str3 != null ? LocaleUtils.toUpperString(str3).intern() : str5;
        this.variant = str4 != null ? str4.intern() : str5;
    }

    public static BaseLocale createInstance(String str, String str2) {
        BaseLocale baseLocale = new BaseLocale(str, str2);
        CACHE.put(new Key(str, str2), baseLocale);
        return baseLocale;
    }

    public static BaseLocale getInstance(String str, String str2, String str3, String str4) {
        if (str != null) {
            if (LocaleUtils.caseIgnoreMatch(str, "he")) {
                str = "iw";
            } else if (LocaleUtils.caseIgnoreMatch(str, "yi")) {
                str = "ji";
            } else if (LocaleUtils.caseIgnoreMatch(str, "id")) {
                str = "in";
            }
        }
        return (BaseLocale) CACHE.get(new Key(str, str2, str3, str4));
    }

    public String getLanguage() {
        return this.language;
    }

    public String getScript() {
        return this.script;
    }

    public String getRegion() {
        return this.region;
    }

    public String getVariant() {
        return this.variant;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BaseLocale)) {
            return false;
        }
        BaseLocale baseLocale = (BaseLocale) obj;
        if (this.language == baseLocale.language && this.script == baseLocale.script && this.region == baseLocale.region && this.variant == baseLocale.variant) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.language.length() > 0) {
            sb.append("language=");
            sb.append(this.language);
        }
        if (this.script.length() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("script=");
            sb.append(this.script);
        }
        if (this.region.length() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("region=");
            sb.append(this.region);
        }
        if (this.variant.length() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("variant=");
            sb.append(this.variant);
        }
        return sb.toString();
    }

    public int hashCode() {
        int i = this.hash;
        if (i != 0) {
            return i;
        }
        int hashCode = (((((this.language.hashCode() * 31) + this.script.hashCode()) * 31) + this.region.hashCode()) * 31) + this.variant.hashCode();
        this.hash = hashCode;
        return hashCode;
    }

    public static void cleanCache() {
        CACHE.cleanStaleEntries();
    }

    private static final class Key {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final int hash;
        /* access modifiers changed from: private */
        public final SoftReference<String> lang;
        private final boolean normalized;
        /* access modifiers changed from: private */
        public final SoftReference<String> regn;
        /* access modifiers changed from: private */
        public final SoftReference<String> scrt;
        /* access modifiers changed from: private */
        public final SoftReference<String> vart;

        static {
            Class<BaseLocale> cls = BaseLocale.class;
        }

        private Key(String str, String str2) {
            this.lang = new SoftReference<>(str);
            this.scrt = new SoftReference<>("");
            this.regn = new SoftReference<>(str2);
            this.vart = new SoftReference<>("");
            this.normalized = true;
            int hashCode = str.hashCode();
            if (str2 != "") {
                int length = str2.length();
                for (int i = 0; i < length; i++) {
                    hashCode = (hashCode * 31) + LocaleUtils.toLower(str2.charAt(i));
                }
            }
            this.hash = hashCode;
        }

        public Key(String str, String str2, String str3, String str4) {
            this(str, str2, str3, str4, false);
        }

        private Key(String str, String str2, String str3, String str4, boolean z) {
            int i;
            if (str != null) {
                this.lang = new SoftReference<>(str);
                int length = str.length();
                i = 0;
                for (int i2 = 0; i2 < length; i2++) {
                    i = (i * 31) + LocaleUtils.toLower(str.charAt(i2));
                }
            } else {
                this.lang = new SoftReference<>("");
                i = 0;
            }
            if (str2 != null) {
                this.scrt = new SoftReference<>(str2);
                int length2 = str2.length();
                for (int i3 = 0; i3 < length2; i3++) {
                    i = (i * 31) + LocaleUtils.toLower(str2.charAt(i3));
                }
            } else {
                this.scrt = new SoftReference<>("");
            }
            if (str3 != null) {
                this.regn = new SoftReference<>(str3);
                int length3 = str3.length();
                for (int i4 = 0; i4 < length3; i4++) {
                    i = (i * 31) + LocaleUtils.toLower(str3.charAt(i4));
                }
            } else {
                this.regn = new SoftReference<>("");
            }
            if (str4 != null) {
                this.vart = new SoftReference<>(str4);
                int length4 = str4.length();
                for (int i5 = 0; i5 < length4; i5++) {
                    i = (i * 31) + str4.charAt(i5);
                }
            } else {
                this.vart = new SoftReference<>("");
            }
            this.hash = i;
            this.normalized = z;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Key) {
                Key key = (Key) obj;
                if (this.hash == key.hash) {
                    String str = this.lang.get();
                    String str2 = key.lang.get();
                    if (!(str == null || str2 == null || !LocaleUtils.caseIgnoreMatch(str2, str))) {
                        String str3 = this.scrt.get();
                        String str4 = key.scrt.get();
                        if (!(str3 == null || str4 == null || !LocaleUtils.caseIgnoreMatch(str4, str3))) {
                            String str5 = this.regn.get();
                            String str6 = key.regn.get();
                            if (!(str5 == null || str6 == null || !LocaleUtils.caseIgnoreMatch(str6, str5))) {
                                String str7 = this.vart.get();
                                String str8 = key.vart.get();
                                if (str8 == null || !str8.equals(str7)) {
                                    return false;
                                }
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        public int hashCode() {
            return this.hash;
        }

        public static Key normalize(Key key) {
            if (key.normalized) {
                return key;
            }
            return new Key(LocaleUtils.toLowerString(key.lang.get()).intern(), LocaleUtils.toTitleString(key.scrt.get()).intern(), LocaleUtils.toUpperString(key.regn.get()).intern(), key.vart.get().intern(), true);
        }
    }

    private static class Cache extends LocaleObjectCache<Key, BaseLocale> {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        static {
            Class<BaseLocale> cls = BaseLocale.class;
        }

        /* access modifiers changed from: protected */
        public Key normalizeKey(Key key) {
            return Key.normalize(key);
        }

        /* access modifiers changed from: protected */
        public BaseLocale createObject(Key key) {
            return new BaseLocale((String) key.lang.get(), (String) key.scrt.get(), (String) key.regn.get(), (String) key.vart.get());
        }
    }
}
