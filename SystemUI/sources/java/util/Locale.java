package java.util;

import com.android.icu.util.LocaleNative;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamException;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.text.MessageFormat;
import libcore.icu.ICU;
import sun.util.locale.BaseLocale;
import sun.util.locale.InternalLocaleBuilder;
import sun.util.locale.LanguageTag;
import sun.util.locale.LocaleExtensions;
import sun.util.locale.LocaleMatcher;
import sun.util.locale.LocaleObjectCache;
import sun.util.locale.LocaleSyntaxException;
import sun.util.locale.LocaleUtils;
import sun.util.locale.ParseStatus;

public final class Locale implements Cloneable, Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final Locale CANADA = createConstant("en", "CA");
    public static final Locale CANADA_FRENCH = createConstant("fr", "CA");
    public static final Locale CHINA;
    public static final Locale CHINESE = createConstant("zh", "");
    private static final int DISPLAY_COUNTRY = 1;
    private static final int DISPLAY_LANGUAGE = 0;
    private static final int DISPLAY_SCRIPT = 3;
    private static final int DISPLAY_VARIANT = 2;
    public static final Locale ENGLISH = createConstant("en", "");
    public static final Locale FRANCE = createConstant("fr", "FR");
    public static final Locale FRENCH = createConstant("fr", "");
    public static final Locale GERMAN = createConstant("de", "");
    public static final Locale GERMANY = createConstant("de", "DE");
    public static final Locale ITALIAN = createConstant("it", "");
    public static final Locale ITALY = createConstant("it", "IT");
    public static final Locale JAPAN = createConstant("ja", "JP");
    public static final Locale JAPANESE = createConstant("ja", "");
    public static final Locale KOREA = createConstant("ko", "KR");
    public static final Locale KOREAN = createConstant("ko", "");
    private static final Cache LOCALECACHE = new Cache();
    public static final Locale PRC;
    public static final char PRIVATE_USE_EXTENSION = 'x';
    public static final Locale ROOT = createConstant("", "");
    public static final Locale SIMPLIFIED_CHINESE;
    public static final Locale TAIWAN;
    public static final Locale TRADITIONAL_CHINESE;

    /* renamed from: UK */
    public static final Locale f697UK = createConstant("en", "GB");
    private static final String UNDETERMINED_LANGUAGE = "und";
    public static final char UNICODE_LOCALE_EXTENSION = 'u';

    /* renamed from: US */
    public static final Locale f698US = createConstant("en", "US");
    private static volatile Locale defaultDisplayLocale = null;
    private static volatile Locale defaultFormatLocale = null;
    private static volatile String[] isoCountries = null;
    private static volatile String[] isoLanguages = null;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("language", String.class), new ObjectStreamField("country", String.class), new ObjectStreamField("variant", String.class), new ObjectStreamField("hashcode", Integer.TYPE), new ObjectStreamField("script", String.class), new ObjectStreamField("extensions", String.class)};
    static final long serialVersionUID = 9149081749638150636L;
    /* access modifiers changed from: private */
    public transient BaseLocale baseLocale;
    private volatile transient int hashCodeValue;
    private volatile transient String languageTag;
    /* access modifiers changed from: private */
    public transient LocaleExtensions localeExtensions;

    public enum FilteringMode {
        AUTOSELECT_FILTERING,
        EXTENDED_FILTERING,
        IGNORE_EXTENDED_RANGES,
        MAP_EXTENDED_RANGES,
        REJECT_EXTENDED_RANGES
    }

    static {
        Locale createConstant = createConstant("zh", "CN");
        SIMPLIFIED_CHINESE = createConstant;
        Locale createConstant2 = createConstant("zh", "TW");
        TRADITIONAL_CHINESE = createConstant2;
        CHINA = createConstant;
        PRC = createConstant;
        TAIWAN = createConstant2;
    }

    private Locale(BaseLocale baseLocale2, LocaleExtensions localeExtensions2) {
        this.hashCodeValue = 0;
        this.baseLocale = baseLocale2;
        this.localeExtensions = localeExtensions2;
    }

    public Locale(String str, String str2, String str3) {
        this.hashCodeValue = 0;
        if (str == null || str2 == null || str3 == null) {
            throw null;
        }
        this.baseLocale = BaseLocale.getInstance(convertOldISOCodes(str), "", str2, str3);
        this.localeExtensions = getCompatibilityExtensions(str, "", str2, str3);
    }

    public Locale(String str, String str2) {
        this(str, str2, "");
    }

    public Locale(String str) {
        this(str, "", "");
    }

    private static Locale createConstant(String str, String str2) {
        return getInstance(BaseLocale.createInstance(str, str2), (LocaleExtensions) null);
    }

    static Locale getInstance(String str, String str2, String str3) {
        return getInstance(str, "", str2, str3, (LocaleExtensions) null);
    }

    static Locale getInstance(String str, String str2, String str3, String str4, LocaleExtensions localeExtensions2) {
        if (str == null || str2 == null || str3 == null || str4 == null) {
            throw null;
        }
        if (localeExtensions2 == null) {
            localeExtensions2 = getCompatibilityExtensions(str, str2, str3, str4);
        }
        return getInstance(BaseLocale.getInstance(str, str2, str3, str4), localeExtensions2);
    }

    static Locale getInstance(BaseLocale baseLocale2, LocaleExtensions localeExtensions2) {
        return (Locale) LOCALECACHE.get(new LocaleKey(baseLocale2, localeExtensions2));
    }

    public static void cleanCache() {
        LOCALECACHE.cleanStaleEntries();
    }

    private static class Cache extends LocaleObjectCache<LocaleKey, Locale> {
        private Cache() {
        }

        /* access modifiers changed from: protected */
        public Locale createObject(LocaleKey localeKey) {
            return new Locale(localeKey.base, localeKey.exts);
        }
    }

    private static final class LocaleKey {
        /* access modifiers changed from: private */
        public final BaseLocale base;
        /* access modifiers changed from: private */
        public final LocaleExtensions exts;
        private final int hash;

        private LocaleKey(BaseLocale baseLocale, LocaleExtensions localeExtensions) {
            this.base = baseLocale;
            this.exts = localeExtensions;
            int hashCode = baseLocale.hashCode();
            this.hash = localeExtensions != null ? hashCode ^ localeExtensions.hashCode() : hashCode;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof LocaleKey)) {
                return false;
            }
            LocaleKey localeKey = (LocaleKey) obj;
            if (this.hash != localeKey.hash || !this.base.equals(localeKey.base)) {
                return false;
            }
            LocaleExtensions localeExtensions = this.exts;
            if (localeExtensions != null) {
                return localeExtensions.equals(localeKey.exts);
            }
            if (localeKey.exts == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.hash;
        }
    }

    public static Locale getDefault() {
        return NoImagePreloadHolder.defaultLocale;
    }

    /* renamed from: java.util.Locale$1 */
    static /* synthetic */ class C44021 {
        static final /* synthetic */ int[] $SwitchMap$java$util$Locale$Category;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                java.util.Locale$Category[] r0 = java.util.Locale.Category.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$util$Locale$Category = r0
                java.util.Locale$Category r1 = java.util.Locale.Category.DISPLAY     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$util$Locale$Category     // Catch:{ NoSuchFieldError -> 0x001d }
                java.util.Locale$Category r1 = java.util.Locale.Category.FORMAT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Locale.C44021.<clinit>():void");
        }
    }

    public static Locale getDefault(Category category) {
        int i = C44021.$SwitchMap$java$util$Locale$Category[category.ordinal()];
        if (i == 1) {
            if (defaultDisplayLocale == null) {
                synchronized (Locale.class) {
                    if (defaultDisplayLocale == null) {
                        defaultDisplayLocale = initDefault(category);
                    }
                }
            }
            return defaultDisplayLocale;
        } else if (i != 2) {
            return getDefault();
        } else {
            if (defaultFormatLocale == null) {
                synchronized (Locale.class) {
                    if (defaultFormatLocale == null) {
                        defaultFormatLocale = initDefault(category);
                    }
                }
            }
            return defaultFormatLocale;
        }
    }

    public static Locale initDefault() {
        String str;
        String str2 = "";
        String property = System.getProperty("user.locale", str2);
        if (!property.isEmpty()) {
            return forLanguageTag(property);
        }
        String property2 = System.getProperty("user.language", "en");
        String property3 = System.getProperty("user.region");
        if (property3 != null) {
            int indexOf = property3.indexOf(95);
            if (indexOf >= 0) {
                String substring = property3.substring(0, indexOf);
                str = property3.substring(indexOf + 1);
                property3 = substring;
            } else {
                str = str2;
            }
        } else {
            String property4 = System.getProperty("user.script", str2);
            String property5 = System.getProperty("user.country", str2);
            str = System.getProperty("user.variant", str2);
            str2 = property4;
            property3 = property5;
        }
        return getInstance(property2, str2, property3, str, (LocaleExtensions) null);
    }

    private static Locale initDefault(Category category) {
        Locale locale = NoImagePreloadHolder.defaultLocale;
        return getInstance(System.getProperty(category.languageKey, locale.getLanguage()), System.getProperty(category.scriptKey, locale.getScript()), System.getProperty(category.countryKey, locale.getCountry()), System.getProperty(category.variantKey, locale.getVariant()), (LocaleExtensions) null);
    }

    public static synchronized void setDefault(Locale locale) {
        synchronized (Locale.class) {
            setDefault(Category.DISPLAY, locale);
            setDefault(Category.FORMAT, locale);
            NoImagePreloadHolder.defaultLocale = locale;
            ICU.setDefaultLocale(locale.toLanguageTag());
        }
    }

    public static synchronized void setDefault(Category category, Locale locale) {
        synchronized (Locale.class) {
            if (category == null) {
                throw new NullPointerException("Category cannot be NULL");
            } else if (locale != null) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkPermission(new PropertyPermission("user.language", "write"));
                }
                int i = C44021.$SwitchMap$java$util$Locale$Category[category.ordinal()];
                if (i == 1) {
                    defaultDisplayLocale = locale;
                } else if (i == 2) {
                    defaultFormatLocale = locale;
                }
            } else {
                throw new NullPointerException("Can't set default locale to NULL");
            }
        }
    }

    public static Locale[] getAvailableLocales() {
        return ICU.getAvailableLocales();
    }

    public static String[] getISOCountries() {
        return ICU.getISOCountries();
    }

    public static String[] getISOLanguages() {
        return ICU.getISOLanguages();
    }

    public String getLanguage() {
        return this.baseLocale.getLanguage();
    }

    public String getScript() {
        return this.baseLocale.getScript();
    }

    public String getCountry() {
        return this.baseLocale.getRegion();
    }

    public String getVariant() {
        return this.baseLocale.getVariant();
    }

    public boolean hasExtensions() {
        return this.localeExtensions != null;
    }

    public Locale stripExtensions() {
        return hasExtensions() ? getInstance(this.baseLocale, (LocaleExtensions) null) : this;
    }

    public String getExtension(char c) {
        if (!LocaleExtensions.isValidKey(c)) {
            throw new IllegalArgumentException("Ill-formed extension key: " + c);
        } else if (hasExtensions()) {
            return this.localeExtensions.getExtensionValue(Character.valueOf(c));
        } else {
            return null;
        }
    }

    public Set<Character> getExtensionKeys() {
        if (!hasExtensions()) {
            return Collections.emptySet();
        }
        return this.localeExtensions.getKeys();
    }

    public Set<String> getUnicodeLocaleAttributes() {
        if (!hasExtensions()) {
            return Collections.emptySet();
        }
        return this.localeExtensions.getUnicodeLocaleAttributes();
    }

    public String getUnicodeLocaleType(String str) {
        if (!isUnicodeExtensionKey(str)) {
            throw new IllegalArgumentException("Ill-formed Unicode locale key: " + str);
        } else if (hasExtensions()) {
            return this.localeExtensions.getUnicodeLocaleType(str);
        } else {
            return null;
        }
    }

    public Set<String> getUnicodeLocaleKeys() {
        LocaleExtensions localeExtensions2 = this.localeExtensions;
        if (localeExtensions2 == null) {
            return Collections.emptySet();
        }
        return localeExtensions2.getUnicodeLocaleKeys();
    }

    /* access modifiers changed from: package-private */
    public BaseLocale getBaseLocale() {
        return this.baseLocale;
    }

    /* access modifiers changed from: package-private */
    public LocaleExtensions getLocaleExtensions() {
        return this.localeExtensions;
    }

    public final String toString() {
        boolean z = true;
        boolean z2 = this.baseLocale.getLanguage().length() != 0;
        boolean z3 = this.baseLocale.getScript().length() != 0;
        boolean z4 = this.baseLocale.getRegion().length() != 0;
        boolean z5 = this.baseLocale.getVariant().length() != 0;
        LocaleExtensions localeExtensions2 = this.localeExtensions;
        if (localeExtensions2 == null || localeExtensions2.getID().length() == 0) {
            z = false;
        }
        StringBuilder sb = new StringBuilder(this.baseLocale.getLanguage());
        if (z4 || (z2 && (z5 || z3 || z))) {
            sb.append('_');
            sb.append(this.baseLocale.getRegion());
        }
        if (z5 && (z2 || z4)) {
            sb.append('_');
            sb.append(this.baseLocale.getVariant());
        }
        if (z3 && (z2 || z4)) {
            sb.append("_#");
            sb.append(this.baseLocale.getScript());
        }
        if (z && (z2 || z4)) {
            sb.append('_');
            if (!z3) {
                sb.append('#');
            }
            sb.append(this.localeExtensions.getID());
        }
        return sb.toString();
    }

    public String toLanguageTag() {
        if (this.languageTag != null) {
            return this.languageTag;
        }
        LanguageTag parseLocale = LanguageTag.parseLocale(this.baseLocale, this.localeExtensions);
        StringBuilder sb = new StringBuilder();
        String language = parseLocale.getLanguage();
        if (language.length() > 0) {
            sb.append(LanguageTag.canonicalizeLanguage(language));
        }
        String script = parseLocale.getScript();
        if (script.length() > 0) {
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizeScript(script));
        }
        String region = parseLocale.getRegion();
        if (region.length() > 0) {
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizeRegion(region));
        }
        for (String append : parseLocale.getVariants()) {
            sb.append(LanguageTag.SEP);
            sb.append(append);
        }
        for (String canonicalizeExtension : parseLocale.getExtensions()) {
            sb.append(LanguageTag.SEP);
            sb.append(LanguageTag.canonicalizeExtension(canonicalizeExtension));
        }
        String privateuse = parseLocale.getPrivateuse();
        if (privateuse.length() > 0) {
            if (sb.length() > 0) {
                sb.append(LanguageTag.SEP);
            }
            sb.append(LanguageTag.PRIVATEUSE);
            sb.append(LanguageTag.SEP);
            sb.append(privateuse);
        }
        String sb2 = sb.toString();
        synchronized (this) {
            if (this.languageTag == null) {
                this.languageTag = sb2;
            }
        }
        return this.languageTag;
    }

    public static Locale forLanguageTag(String str) {
        LanguageTag parse = LanguageTag.parse(str, (ParseStatus) null);
        InternalLocaleBuilder internalLocaleBuilder = new InternalLocaleBuilder();
        internalLocaleBuilder.setLanguageTag(parse);
        BaseLocale baseLocale2 = internalLocaleBuilder.getBaseLocale();
        LocaleExtensions localeExtensions2 = internalLocaleBuilder.getLocaleExtensions();
        if (localeExtensions2 == null && baseLocale2.getVariant().length() > 0) {
            localeExtensions2 = getCompatibilityExtensions(baseLocale2.getLanguage(), baseLocale2.getScript(), baseLocale2.getRegion(), baseLocale2.getVariant());
        }
        return getInstance(baseLocale2, localeExtensions2);
    }

    public String getISO3Language() throws MissingResourceException {
        String language = this.baseLocale.getLanguage();
        if (language.length() == 3) {
            return language;
        }
        if (language.isEmpty()) {
            return "";
        }
        String iSO3Language = ICU.getISO3Language(language);
        if (language.isEmpty() || !iSO3Language.isEmpty()) {
            return iSO3Language;
        }
        throw new MissingResourceException("Couldn't find 3-letter language code for " + language, "FormatData_" + toString(), "ShortLanguage");
    }

    public String getISO3Country() throws MissingResourceException {
        String region = this.baseLocale.getRegion();
        if (region.length() == 3) {
            return this.baseLocale.getRegion();
        }
        if (region.isEmpty()) {
            return "";
        }
        String iSO3Country = ICU.getISO3Country("en-" + region);
        if (region.isEmpty() || !iSO3Country.isEmpty()) {
            return iSO3Country;
        }
        throw new MissingResourceException("Couldn't find 3-letter country code for " + this.baseLocale.getRegion(), "FormatData_" + toString(), "ShortCountry");
    }

    public final String getDisplayLanguage() {
        return getDisplayLanguage(getDefault(Category.DISPLAY));
    }

    public String getDisplayLanguage(Locale locale) {
        String language = this.baseLocale.getLanguage();
        if (language.isEmpty()) {
            return "";
        }
        if ("und".equals(normalizeAndValidateLanguage(language, false))) {
            return language;
        }
        String displayLanguage = LocaleNative.getDisplayLanguage(this, locale);
        return displayLanguage == null ? LocaleNative.getDisplayLanguage(this, getDefault()) : displayLanguage;
    }

    private static String normalizeAndValidateLanguage(String str, boolean z) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        String lowerCase = str.toLowerCase(ROOT);
        if (isValidBcp47Alpha(lowerCase, 2, 3)) {
            return lowerCase;
        }
        if (!z) {
            return "und";
        }
        throw new IllformedLocaleException("Invalid language: " + str);
    }

    private static boolean isAsciiAlphaNum(String str) {
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && (charAt < '0' || charAt > '9'))) {
                return false;
            }
        }
        return true;
    }

    public String getDisplayScript() {
        return getDisplayScript(getDefault(Category.DISPLAY));
    }

    public String getDisplayScript(Locale locale) {
        if (this.baseLocale.getScript().isEmpty()) {
            return "";
        }
        String displayScript = LocaleNative.getDisplayScript(this, locale);
        return displayScript == null ? LocaleNative.getDisplayScript(this, getDefault(Category.DISPLAY)) : displayScript;
    }

    public final String getDisplayCountry() {
        return getDisplayCountry(getDefault(Category.DISPLAY));
    }

    public String getDisplayCountry(Locale locale) {
        String region = this.baseLocale.getRegion();
        if (region.isEmpty()) {
            return "";
        }
        if (normalizeAndValidateRegion(region, false).isEmpty()) {
            return region;
        }
        String displayCountry = LocaleNative.getDisplayCountry(this, locale);
        return displayCountry == null ? LocaleNative.getDisplayCountry(this, getDefault()) : displayCountry;
    }

    private static String normalizeAndValidateRegion(String str, boolean z) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        String upperCase = str.toUpperCase(ROOT);
        if (isValidBcp47Alpha(upperCase, 2, 2) || isUnM49AreaCode(upperCase)) {
            return upperCase;
        }
        if (!z) {
            return "";
        }
        throw new IllformedLocaleException("Invalid region: " + str);
    }

    private static boolean isValidBcp47Alpha(String str, int i, int i2) {
        int length = str.length();
        if (length < i || length > i2) {
            return false;
        }
        for (int i3 = 0; i3 < length; i3++) {
            char charAt = str.charAt(i3);
            if ((charAt < 'a' || charAt > 'z') && (charAt < 'A' || charAt > 'Z')) {
                return false;
            }
        }
        return true;
    }

    private static boolean isUnM49AreaCode(String str) {
        if (str.length() != 3) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            char charAt = str.charAt(i);
            if (charAt < '0' || charAt > '9') {
                return false;
            }
        }
        return true;
    }

    public final String getDisplayVariant() {
        return getDisplayVariant(getDefault(Category.DISPLAY));
    }

    public String getDisplayVariant(Locale locale) {
        String variant = this.baseLocale.getVariant();
        if (variant.isEmpty()) {
            return "";
        }
        try {
            normalizeAndValidateVariant(variant);
            String displayVariant = LocaleNative.getDisplayVariant(this, locale);
            if (displayVariant == null) {
                displayVariant = LocaleNative.getDisplayVariant(this, getDefault());
            }
            return displayVariant.isEmpty() ? variant : displayVariant;
        } catch (IllformedLocaleException unused) {
            return variant;
        }
    }

    private static String normalizeAndValidateVariant(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        String replace = str.replace('-', '_');
        String[] split = replace.split(BaseLocale.SEP);
        int length = split.length;
        int i = 0;
        while (i < length) {
            if (isValidVariantSubtag(split[i])) {
                i++;
            } else {
                throw new IllformedLocaleException("Invalid variant: " + str);
            }
        }
        return replace;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001f, code lost:
        r0 = r4.charAt(0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isValidVariantSubtag(java.lang.String r4) {
        /*
            int r0 = r4.length()
            r1 = 5
            r2 = 1
            r3 = 0
            if (r0 < r1) goto L_0x0018
            int r0 = r4.length()
            r1 = 8
            if (r0 > r1) goto L_0x0018
            boolean r4 = isAsciiAlphaNum(r4)
            if (r4 == 0) goto L_0x0032
            return r2
        L_0x0018:
            int r0 = r4.length()
            r1 = 4
            if (r0 != r1) goto L_0x0032
            char r0 = r4.charAt(r3)
            r1 = 48
            if (r0 < r1) goto L_0x0032
            r1 = 57
            if (r0 > r1) goto L_0x0032
            boolean r4 = isAsciiAlphaNum(r4)
            if (r4 == 0) goto L_0x0032
            return r2
        L_0x0032:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Locale.isValidVariantSubtag(java.lang.String):boolean");
    }

    public final String getDisplayName() {
        return getDisplayName(getDefault(Category.DISPLAY));
    }

    public String getDisplayName(Locale locale) {
        int i;
        StringBuilder sb = new StringBuilder();
        String language = this.baseLocale.getLanguage();
        if (!language.isEmpty()) {
            String displayLanguage = getDisplayLanguage(locale);
            if (!displayLanguage.isEmpty()) {
                language = displayLanguage;
            }
            sb.append(language);
            i = 1;
        } else {
            i = 0;
        }
        String script = this.baseLocale.getScript();
        if (!script.isEmpty()) {
            if (i == 1) {
                sb.append(" (");
            }
            String displayScript = getDisplayScript(locale);
            if (!displayScript.isEmpty()) {
                script = displayScript;
            }
            sb.append(script);
            i++;
        }
        String region = this.baseLocale.getRegion();
        if (!region.isEmpty()) {
            if (i == 1) {
                sb.append(" (");
            } else if (i == 2) {
                sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            }
            String displayCountry = getDisplayCountry(locale);
            if (!displayCountry.isEmpty()) {
                region = displayCountry;
            }
            sb.append(region);
            i++;
        }
        String variant = this.baseLocale.getVariant();
        if (!variant.isEmpty()) {
            if (i == 1) {
                sb.append(" (");
            } else if (i == 2 || i == 3) {
                sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            }
            String displayVariant = getDisplayVariant(locale);
            if (!displayVariant.isEmpty()) {
                variant = displayVariant;
            }
            sb.append(variant);
            i++;
        }
        if (i > 1) {
            sb.append(NavigationBarInflaterView.KEY_CODE_END);
        }
        return sb.toString();
    }

    public Object clone() {
        try {
            return (Locale) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    public int hashCode() {
        int i = this.hashCodeValue;
        if (i == 0) {
            i = this.baseLocale.hashCode();
            LocaleExtensions localeExtensions2 = this.localeExtensions;
            if (localeExtensions2 != null) {
                i ^= localeExtensions2.hashCode();
            }
            this.hashCodeValue = i;
        }
        return i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Locale)) {
            return false;
        }
        Locale locale = (Locale) obj;
        if (!this.baseLocale.equals(locale.baseLocale)) {
            return false;
        }
        LocaleExtensions localeExtensions2 = this.localeExtensions;
        if (localeExtensions2 != null) {
            return localeExtensions2.equals(locale.localeExtensions);
        }
        if (locale.localeExtensions == null) {
            return true;
        }
        return false;
    }

    private static class NoImagePreloadHolder {
        public static volatile Locale defaultLocale = Locale.initDefault();

        private NoImagePreloadHolder() {
        }
    }

    private static String formatList(String[] strArr, String str, String str2) {
        if (str == null || str2 == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < strArr.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(strArr[i]);
            }
            return sb.toString();
        }
        if (strArr.length > 3) {
            strArr = composeList(new MessageFormat(str2), strArr);
        }
        Object[] objArr = new Object[(strArr.length + 1)];
        System.arraycopy((Object) strArr, 0, (Object) objArr, 1, strArr.length);
        objArr[0] = new Integer(strArr.length);
        return new MessageFormat(str).format(objArr);
    }

    private static String[] composeList(MessageFormat messageFormat, String[] strArr) {
        if (strArr.length <= 3) {
            return strArr;
        }
        String format = messageFormat.format(new String[]{strArr[0], strArr[1]});
        int length = strArr.length - 1;
        String[] strArr2 = new String[length];
        System.arraycopy((Object) strArr, 2, (Object) strArr2, 1, length - 1);
        strArr2[0] = format;
        return composeList(messageFormat, strArr2);
    }

    private static boolean isUnicodeExtensionKey(String str) {
        return str.length() == 2 && LocaleUtils.isAlphaNumericString(str);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("language", (Object) this.baseLocale.getLanguage());
        putFields.put("script", (Object) this.baseLocale.getScript());
        putFields.put("country", (Object) this.baseLocale.getRegion());
        putFields.put("variant", (Object) this.baseLocale.getVariant());
        LocaleExtensions localeExtensions2 = this.localeExtensions;
        putFields.put("extensions", (Object) localeExtensions2 == null ? "" : localeExtensions2.getID());
        putFields.put("hashcode", -1);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        String str = (String) readFields.get("extensions", (Object) "");
        this.baseLocale = BaseLocale.getInstance(convertOldISOCodes((String) readFields.get("language", (Object) "")), (String) readFields.get("script", (Object) ""), (String) readFields.get("country", (Object) ""), (String) readFields.get("variant", (Object) ""));
        if (str == null || str.length() <= 0) {
            this.localeExtensions = null;
            return;
        }
        try {
            InternalLocaleBuilder internalLocaleBuilder = new InternalLocaleBuilder();
            internalLocaleBuilder.setExtensions(str);
            this.localeExtensions = internalLocaleBuilder.getLocaleExtensions();
        } catch (LocaleSyntaxException e) {
            throw new IllformedLocaleException(e.getMessage());
        }
    }

    private Object readResolve() throws ObjectStreamException {
        return getInstance(this.baseLocale.getLanguage(), this.baseLocale.getScript(), this.baseLocale.getRegion(), this.baseLocale.getVariant(), this.localeExtensions);
    }

    private static String convertOldISOCodes(String str) {
        String intern = LocaleUtils.toLowerString(str).intern();
        if (intern == "he") {
            return "iw";
        }
        if (intern == "yi") {
            return "ji";
        }
        return intern == "id" ? "in" : intern;
    }

    /* access modifiers changed from: private */
    public static LocaleExtensions getCompatibilityExtensions(String str, String str2, String str3, String str4) {
        if (LocaleUtils.caseIgnoreMatch(str, "ja") && str2.length() == 0 && LocaleUtils.caseIgnoreMatch(str3, "jp") && "JP".equals(str4)) {
            return LocaleExtensions.CALENDAR_JAPANESE;
        }
        if (!LocaleUtils.caseIgnoreMatch(str, "th") || str2.length() != 0 || !LocaleUtils.caseIgnoreMatch(str3, "th") || !"TH".equals(str4)) {
            return null;
        }
        return LocaleExtensions.NUMBER_THAI;
    }

    public enum Category {
        DISPLAY("user.language.display", "user.script.display", "user.country.display", "user.variant.display"),
        FORMAT("user.language.format", "user.script.format", "user.country.format", "user.variant.format");
        
        final String countryKey;
        final String languageKey;
        final String scriptKey;
        final String variantKey;

        private Category(String str, String str2, String str3, String str4) {
            this.languageKey = str;
            this.scriptKey = str2;
            this.countryKey = str3;
            this.variantKey = str4;
        }
    }

    public static final class Builder {
        private final InternalLocaleBuilder localeBuilder = new InternalLocaleBuilder();

        public Builder setLocale(Locale locale) {
            try {
                this.localeBuilder.setLocale(locale.baseLocale, locale.localeExtensions);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setLanguageTag(String str) {
            ParseStatus parseStatus = new ParseStatus();
            LanguageTag parse = LanguageTag.parse(str, parseStatus);
            if (!parseStatus.isError()) {
                this.localeBuilder.setLanguageTag(parse);
                return this;
            }
            throw new IllformedLocaleException(parseStatus.getErrorMessage(), parseStatus.getErrorIndex());
        }

        public Builder setLanguage(String str) {
            try {
                this.localeBuilder.setLanguage(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setScript(String str) {
            try {
                this.localeBuilder.setScript(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setRegion(String str) {
            try {
                this.localeBuilder.setRegion(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setVariant(String str) {
            try {
                this.localeBuilder.setVariant(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setExtension(char c, String str) {
            try {
                this.localeBuilder.setExtension(c, str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder setUnicodeLocaleKeyword(String str, String str2) {
            try {
                this.localeBuilder.setUnicodeLocaleKeyword(str, str2);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder addUnicodeLocaleAttribute(String str) {
            try {
                this.localeBuilder.addUnicodeLocaleAttribute(str);
                return this;
            } catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
        }

        public Builder removeUnicodeLocaleAttribute(String str) {
            if (str != null) {
                try {
                    this.localeBuilder.removeUnicodeLocaleAttribute(str);
                    return this;
                } catch (LocaleSyntaxException e) {
                    throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
                }
            } else {
                throw new NullPointerException("attribute == null");
            }
        }

        public Builder clear() {
            this.localeBuilder.clear();
            return this;
        }

        public Builder clearExtensions() {
            this.localeBuilder.clearExtensions();
            return this;
        }

        public Locale build() {
            BaseLocale baseLocale = this.localeBuilder.getBaseLocale();
            LocaleExtensions localeExtensions = this.localeBuilder.getLocaleExtensions();
            if (localeExtensions == null && baseLocale.getVariant().length() > 0) {
                localeExtensions = Locale.getCompatibilityExtensions(baseLocale.getLanguage(), baseLocale.getScript(), baseLocale.getRegion(), baseLocale.getVariant());
            }
            return Locale.getInstance(baseLocale, localeExtensions);
        }
    }

    public static final class LanguageRange {
        public static final double MAX_WEIGHT = 1.0d;
        public static final double MIN_WEIGHT = 0.0d;
        private volatile int hash;
        private final String range;
        private final double weight;

        public LanguageRange(String str) {
            this(str, 1.0d);
        }

        public LanguageRange(String str, double d) {
            boolean z = false;
            this.hash = 0;
            str.getClass();
            if (d < 0.0d || d > 1.0d) {
                throw new IllegalArgumentException("weight=" + d);
            }
            String lowerCase = str.toLowerCase();
            String[] split = lowerCase.split(LanguageTag.SEP);
            if (!isSubtagIllFormed(split[0], true) && !lowerCase.endsWith(LanguageTag.SEP)) {
                int i = 1;
                while (true) {
                    if (i >= split.length) {
                        break;
                    } else if (isSubtagIllFormed(split[i], false)) {
                        break;
                    } else {
                        i++;
                    }
                }
            }
            z = true;
            if (!z) {
                this.range = lowerCase;
                this.weight = d;
                return;
            }
            throw new IllegalArgumentException("range=" + lowerCase);
        }

        private static boolean isSubtagIllFormed(String str, boolean z) {
            if (str.equals("") || str.length() > 8) {
                return true;
            }
            if (str.equals("*")) {
                return false;
            }
            char[] charArray = str.toCharArray();
            if (z) {
                for (char c : charArray) {
                    if (c < 'a' || c > 'z') {
                        return true;
                    }
                }
            } else {
                for (char c2 : charArray) {
                    if (c2 < '0' || ((c2 > '9' && c2 < 'a') || c2 > 'z')) {
                        return true;
                    }
                }
            }
            return false;
        }

        public String getRange() {
            return this.range;
        }

        public double getWeight() {
            return this.weight;
        }

        public static List<LanguageRange> parse(String str) {
            return LocaleMatcher.parse(str);
        }

        public static List<LanguageRange> parse(String str, Map<String, List<String>> map) {
            return mapEquivalents(parse(str), map);
        }

        public static List<LanguageRange> mapEquivalents(List<LanguageRange> list, Map<String, List<String>> map) {
            return LocaleMatcher.mapEquivalents(list, map);
        }

        public int hashCode() {
            if (this.hash == 0) {
                long doubleToLongBits = Double.doubleToLongBits(this.weight);
                this.hash = ((629 + this.range.hashCode()) * 37) + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
            }
            return this.hash;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof LanguageRange)) {
                return false;
            }
            LanguageRange languageRange = (LanguageRange) obj;
            if (this.hash == languageRange.hash && this.range.equals(languageRange.range) && this.weight == languageRange.weight) {
                return true;
            }
            return false;
        }
    }

    public static List<Locale> filter(List<LanguageRange> list, Collection<Locale> collection, FilteringMode filteringMode) {
        return LocaleMatcher.filter(list, collection, filteringMode);
    }

    public static List<Locale> filter(List<LanguageRange> list, Collection<Locale> collection) {
        return filter(list, collection, FilteringMode.AUTOSELECT_FILTERING);
    }

    public static List<String> filterTags(List<LanguageRange> list, Collection<String> collection, FilteringMode filteringMode) {
        return LocaleMatcher.filterTags(list, collection, filteringMode);
    }

    public static List<String> filterTags(List<LanguageRange> list, Collection<String> collection) {
        return filterTags(list, collection, FilteringMode.AUTOSELECT_FILTERING);
    }

    public static Locale lookup(List<LanguageRange> list, Collection<Locale> collection) {
        return LocaleMatcher.lookup(list, collection);
    }

    public static String lookupTag(List<LanguageRange> list, Collection<String> collection) {
        return LocaleMatcher.lookupTag(list, collection);
    }
}
