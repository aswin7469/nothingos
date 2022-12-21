package sun.util.locale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageTag {
    private static final Map<String, String[]> GRANDFATHERED = new HashMap();
    public static final String PRIVATEUSE = "x";
    public static final String PRIVUSE_VARIANT_PREFIX = "lvariant";
    public static final String SEP = "-";
    public static final String UNDETERMINED = "und";
    private List<String> extensions = Collections.emptyList();
    private List<String> extlangs = Collections.emptyList();
    private String language = "";
    private String privateuse = "";
    private String region = "";
    private String script = "";
    private List<String> variants = Collections.emptyList();

    static {
        String[][] strArr = {new String[]{"art-lojban", "jbo"}, new String[]{"cel-gaulish", "xtg-x-cel-gaulish"}, new String[]{"en-GB-oed", "en-GB-x-oed"}, new String[]{"i-ami", "ami"}, new String[]{"i-bnn", "bnn"}, new String[]{"i-default", "en-x-i-default"}, new String[]{"i-enochian", "und-x-i-enochian"}, new String[]{"i-hak", "hak"}, new String[]{"i-klingon", "tlh"}, new String[]{"i-lux", "lb"}, new String[]{"i-mingo", "see-x-i-mingo"}, new String[]{"i-navajo", "nv"}, new String[]{"i-pwn", "pwn"}, new String[]{"i-tao", "tao"}, new String[]{"i-tay", "tay"}, new String[]{"i-tsu", "tsu"}, new String[]{"no-bok", "nb"}, new String[]{"no-nyn", "nn"}, new String[]{"sgn-BE-FR", "sfb"}, new String[]{"sgn-BE-NL", "vgt"}, new String[]{"sgn-CH-DE", "sgg"}, new String[]{"zh-guoyu", "cmn"}, new String[]{"zh-hakka", "hak"}, new String[]{"zh-min", "nan-x-zh-min"}, new String[]{"zh-min-nan", "nan"}, new String[]{"zh-xiang", "hsn"}};
        for (int i = 0; i < 26; i++) {
            String[] strArr2 = strArr[i];
            GRANDFATHERED.put(LocaleUtils.toLowerString(strArr2[0]), strArr2);
        }
    }

    private LanguageTag() {
    }

    public static LanguageTag parse(String str, ParseStatus parseStatus) {
        StringTokenIterator stringTokenIterator;
        if (parseStatus == null) {
            parseStatus = new ParseStatus();
        } else {
            parseStatus.reset();
        }
        String[] strArr = GRANDFATHERED.get(LocaleUtils.toLowerString(str));
        if (strArr != null) {
            stringTokenIterator = new StringTokenIterator(strArr[1], SEP);
        } else {
            stringTokenIterator = new StringTokenIterator(str, SEP);
        }
        LanguageTag languageTag = new LanguageTag();
        if (languageTag.parseLanguage(stringTokenIterator, parseStatus)) {
            languageTag.parseExtlangs(stringTokenIterator, parseStatus);
            languageTag.parseScript(stringTokenIterator, parseStatus);
            languageTag.parseRegion(stringTokenIterator, parseStatus);
            languageTag.parseVariants(stringTokenIterator, parseStatus);
            languageTag.parseExtensions(stringTokenIterator, parseStatus);
        }
        languageTag.parsePrivateuse(stringTokenIterator, parseStatus);
        if (!stringTokenIterator.isDone() && !parseStatus.isError()) {
            String current = stringTokenIterator.current();
            parseStatus.errorIndex = stringTokenIterator.currentStart();
            if (current.length() == 0) {
                parseStatus.errorMsg = "Empty subtag";
            } else {
                parseStatus.errorMsg = "Invalid subtag: " + current;
            }
        }
        return languageTag;
    }

    private boolean parseLanguage(StringTokenIterator stringTokenIterator, ParseStatus parseStatus) {
        if (stringTokenIterator.isDone() || parseStatus.isError()) {
            return false;
        }
        String current = stringTokenIterator.current();
        if (!isLanguage(current)) {
            return false;
        }
        this.language = current;
        parseStatus.parseLength = stringTokenIterator.currentEnd();
        stringTokenIterator.next();
        return true;
    }

    private boolean parseExtlangs(StringTokenIterator stringTokenIterator, ParseStatus parseStatus) {
        boolean z = false;
        if (!stringTokenIterator.isDone() && !parseStatus.isError()) {
            while (!stringTokenIterator.isDone()) {
                String current = stringTokenIterator.current();
                if (!isExtlang(current)) {
                    break;
                }
                if (this.extlangs.isEmpty()) {
                    this.extlangs = new ArrayList(3);
                }
                this.extlangs.add(current);
                parseStatus.parseLength = stringTokenIterator.currentEnd();
                stringTokenIterator.next();
                z = true;
                if (this.extlangs.size() == 3) {
                    break;
                }
            }
        }
        return z;
    }

    private boolean parseScript(StringTokenIterator stringTokenIterator, ParseStatus parseStatus) {
        if (stringTokenIterator.isDone() || parseStatus.isError()) {
            return false;
        }
        String current = stringTokenIterator.current();
        if (!isScript(current)) {
            return false;
        }
        this.script = current;
        parseStatus.parseLength = stringTokenIterator.currentEnd();
        stringTokenIterator.next();
        return true;
    }

    private boolean parseRegion(StringTokenIterator stringTokenIterator, ParseStatus parseStatus) {
        if (stringTokenIterator.isDone() || parseStatus.isError()) {
            return false;
        }
        String current = stringTokenIterator.current();
        if (!isRegion(current)) {
            return false;
        }
        this.region = current;
        parseStatus.parseLength = stringTokenIterator.currentEnd();
        stringTokenIterator.next();
        return true;
    }

    private boolean parseVariants(StringTokenIterator stringTokenIterator, ParseStatus parseStatus) {
        boolean z = false;
        if (!stringTokenIterator.isDone() && !parseStatus.isError()) {
            while (!stringTokenIterator.isDone()) {
                String current = stringTokenIterator.current();
                if (!isVariant(current)) {
                    break;
                }
                if (this.variants.isEmpty()) {
                    this.variants = new ArrayList(3);
                }
                this.variants.add(current);
                parseStatus.parseLength = stringTokenIterator.currentEnd();
                stringTokenIterator.next();
                z = true;
            }
        }
        return z;
    }

    private boolean parseExtensions(StringTokenIterator stringTokenIterator, ParseStatus parseStatus) {
        boolean z = false;
        if (!stringTokenIterator.isDone() && !parseStatus.isError()) {
            while (true) {
                if (stringTokenIterator.isDone()) {
                    break;
                }
                String current = stringTokenIterator.current();
                if (!isExtensionSingleton(current)) {
                    break;
                }
                int currentStart = stringTokenIterator.currentStart();
                StringBuilder sb = new StringBuilder(current);
                stringTokenIterator.next();
                while (!stringTokenIterator.isDone()) {
                    String current2 = stringTokenIterator.current();
                    if (!isExtensionSubtag(current2)) {
                        break;
                    }
                    sb.append(SEP);
                    sb.append(current2);
                    parseStatus.parseLength = stringTokenIterator.currentEnd();
                    stringTokenIterator.next();
                }
                if (parseStatus.parseLength <= currentStart) {
                    parseStatus.errorIndex = currentStart;
                    parseStatus.errorMsg = "Incomplete extension '" + current + "'";
                    break;
                }
                if (this.extensions.isEmpty()) {
                    this.extensions = new ArrayList(4);
                }
                this.extensions.add(sb.toString());
                z = true;
            }
        }
        return z;
    }

    private boolean parsePrivateuse(StringTokenIterator stringTokenIterator, ParseStatus parseStatus) {
        if (stringTokenIterator.isDone() || parseStatus.isError()) {
            return false;
        }
        String current = stringTokenIterator.current();
        if (!isPrivateusePrefix(current)) {
            return false;
        }
        int currentStart = stringTokenIterator.currentStart();
        StringBuilder sb = new StringBuilder(current);
        stringTokenIterator.next();
        while (!stringTokenIterator.isDone()) {
            String current2 = stringTokenIterator.current();
            if (!isPrivateuseSubtag(current2)) {
                break;
            }
            sb.append(SEP);
            sb.append(current2);
            parseStatus.parseLength = stringTokenIterator.currentEnd();
            stringTokenIterator.next();
        }
        if (parseStatus.parseLength <= currentStart) {
            parseStatus.errorIndex = currentStart;
            parseStatus.errorMsg = "Incomplete privateuse";
            return false;
        }
        this.privateuse = sb.toString();
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:54:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0138  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x013b  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x013e  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x016c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static sun.util.locale.LanguageTag parseLocale(sun.util.locale.BaseLocale r11, sun.util.locale.LocaleExtensions r12) {
        /*
            sun.util.locale.LanguageTag r0 = new sun.util.locale.LanguageTag
            r0.<init>()
            java.lang.String r1 = r11.getLanguage()
            java.lang.String r2 = r11.getScript()
            java.lang.String r3 = r11.getRegion()
            java.lang.String r11 = r11.getVariant()
            boolean r4 = isLanguage(r1)
            if (r4 == 0) goto L_0x003d
            java.lang.String r4 = "iw"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0026
            java.lang.String r1 = "he"
            goto L_0x003b
        L_0x0026:
            java.lang.String r4 = "ji"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0031
            java.lang.String r1 = "yi"
            goto L_0x003b
        L_0x0031:
            java.lang.String r4 = "in"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x003b
            java.lang.String r1 = "id"
        L_0x003b:
            r0.language = r1
        L_0x003d:
            boolean r1 = isScript(r2)
            r4 = 1
            if (r1 == 0) goto L_0x004c
            java.lang.String r1 = canonicalizeScript(r2)
            r0.script = r1
            r1 = r4
            goto L_0x004d
        L_0x004c:
            r1 = 0
        L_0x004d:
            boolean r2 = isRegion(r3)
            if (r2 == 0) goto L_0x005a
            java.lang.String r1 = canonicalizeRegion(r3)
            r0.region = r1
            r1 = r4
        L_0x005a:
            java.lang.String r2 = r0.language
            java.lang.String r3 = "no"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007c
            java.lang.String r2 = r0.region
            java.lang.String r3 = "NO"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007c
            java.lang.String r2 = "NY"
            boolean r2 = r11.equals(r2)
            if (r2 == 0) goto L_0x007c
            java.lang.String r11 = "nn"
            r0.language = r11
            java.lang.String r11 = ""
        L_0x007c:
            int r2 = r11.length()
            java.lang.String r3 = "_"
            java.lang.String r5 = "-"
            r6 = 0
            if (r2 <= 0) goto L_0x00e8
            sun.util.locale.StringTokenIterator r2 = new sun.util.locale.StringTokenIterator
            r2.<init>(r11, r3)
            r11 = r6
        L_0x008d:
            boolean r7 = r2.isDone()
            if (r7 != 0) goto L_0x00ac
            java.lang.String r7 = r2.current()
            boolean r8 = isVariant(r7)
            if (r8 != 0) goto L_0x009e
            goto L_0x00ac
        L_0x009e:
            if (r11 != 0) goto L_0x00a5
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
        L_0x00a5:
            r11.add(r7)
            r2.next()
            goto L_0x008d
        L_0x00ac:
            if (r11 == 0) goto L_0x00b1
            r0.variants = r11
            r1 = r4
        L_0x00b1:
            boolean r11 = r2.isDone()
            if (r11 != 0) goto L_0x00e8
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
        L_0x00bc:
            boolean r7 = r2.isDone()
            if (r7 != 0) goto L_0x00dd
            java.lang.String r7 = r2.current()
            boolean r8 = isPrivateuseSubtag(r7)
            if (r8 != 0) goto L_0x00cd
            goto L_0x00dd
        L_0x00cd:
            int r8 = r11.length()
            if (r8 <= 0) goto L_0x00d6
            r11.append((java.lang.String) r5)
        L_0x00d6:
            r11.append((java.lang.String) r7)
            r2.next()
            goto L_0x00bc
        L_0x00dd:
            int r2 = r11.length()
            if (r2 <= 0) goto L_0x00e8
            java.lang.String r11 = r11.toString()
            goto L_0x00e9
        L_0x00e8:
            r11 = r6
        L_0x00e9:
            if (r12 == 0) goto L_0x0138
            java.util.Set r2 = r12.getKeys()
            java.util.Iterator r2 = r2.iterator()
            r7 = r6
        L_0x00f4:
            boolean r8 = r2.hasNext()
            if (r8 == 0) goto L_0x0139
            java.lang.Object r8 = r2.next()
            java.lang.Character r8 = (java.lang.Character) r8
            sun.util.locale.Extension r9 = r12.getExtension(r8)
            char r10 = r8.charValue()
            boolean r10 = isPrivateusePrefixChar(r10)
            if (r10 == 0) goto L_0x0113
            java.lang.String r7 = r9.getValue()
            goto L_0x00f4
        L_0x0113:
            if (r6 != 0) goto L_0x011a
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
        L_0x011a:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r8 = r8.toString()
            r10.append((java.lang.String) r8)
            r10.append((java.lang.String) r5)
            java.lang.String r8 = r9.getValue()
            r10.append((java.lang.String) r8)
            java.lang.String r8 = r10.toString()
            r6.add(r8)
            goto L_0x00f4
        L_0x0138:
            r7 = r6
        L_0x0139:
            if (r6 == 0) goto L_0x013e
            r0.extensions = r6
            goto L_0x013f
        L_0x013e:
            r4 = r1
        L_0x013f:
            if (r11 == 0) goto L_0x016a
            if (r7 != 0) goto L_0x0152
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r1 = "lvariant-"
            r12.<init>((java.lang.String) r1)
            r12.append((java.lang.String) r11)
            java.lang.String r7 = r12.toString()
            goto L_0x016a
        L_0x0152:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r12.append((java.lang.String) r7)
            java.lang.String r1 = "-lvariant-"
            r12.append((java.lang.String) r1)
            java.lang.String r11 = r11.replace((java.lang.CharSequence) r3, (java.lang.CharSequence) r5)
            r12.append((java.lang.String) r11)
            java.lang.String r7 = r12.toString()
        L_0x016a:
            if (r7 == 0) goto L_0x016e
            r0.privateuse = r7
        L_0x016e:
            java.lang.String r11 = r0.language
            int r11 = r11.length()
            if (r11 != 0) goto L_0x017e
            if (r4 != 0) goto L_0x017a
            if (r7 != 0) goto L_0x017e
        L_0x017a:
            java.lang.String r11 = "und"
            r0.language = r11
        L_0x017e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.LanguageTag.parseLocale(sun.util.locale.BaseLocale, sun.util.locale.LocaleExtensions):sun.util.locale.LanguageTag");
    }

    public String getLanguage() {
        return this.language;
    }

    public List<String> getExtlangs() {
        if (this.extlangs.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.extlangs);
    }

    public String getScript() {
        return this.script;
    }

    public String getRegion() {
        return this.region;
    }

    public List<String> getVariants() {
        if (this.variants.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.variants);
    }

    public List<String> getExtensions() {
        if (this.extensions.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.extensions);
    }

    public String getPrivateuse() {
        return this.privateuse;
    }

    public static boolean isLanguage(String str) {
        int length = str.length();
        return length >= 2 && length <= 8 && LocaleUtils.isAlphaString(str);
    }

    public static boolean isExtlang(String str) {
        return str.length() == 3 && LocaleUtils.isAlphaString(str);
    }

    public static boolean isScript(String str) {
        return str.length() == 4 && LocaleUtils.isAlphaString(str);
    }

    public static boolean isRegion(String str) {
        return (str.length() == 2 && LocaleUtils.isAlphaString(str)) || (str.length() == 3 && LocaleUtils.isNumericString(str));
    }

    public static boolean isVariant(String str) {
        int length = str.length();
        if (length >= 5 && length <= 8) {
            return LocaleUtils.isAlphaNumericString(str);
        }
        if (length != 4 || !LocaleUtils.isNumeric(str.charAt(0)) || !LocaleUtils.isAlphaNumeric(str.charAt(1)) || !LocaleUtils.isAlphaNumeric(str.charAt(2)) || !LocaleUtils.isAlphaNumeric(str.charAt(3))) {
            return false;
        }
        return true;
    }

    public static boolean isExtensionSingleton(String str) {
        if (str.length() != 1 || !LocaleUtils.isAlphaString(str) || LocaleUtils.caseIgnoreMatch(PRIVATEUSE, str)) {
            return false;
        }
        return true;
    }

    public static boolean isExtensionSingletonChar(char c) {
        return isExtensionSingleton(String.valueOf(c));
    }

    public static boolean isExtensionSubtag(String str) {
        int length = str.length();
        return length >= 2 && length <= 8 && LocaleUtils.isAlphaNumericString(str);
    }

    public static boolean isPrivateusePrefix(String str) {
        if (str.length() != 1 || !LocaleUtils.caseIgnoreMatch(PRIVATEUSE, str)) {
            return false;
        }
        return true;
    }

    public static boolean isPrivateusePrefixChar(char c) {
        return LocaleUtils.caseIgnoreMatch(PRIVATEUSE, String.valueOf(c));
    }

    public static boolean isPrivateuseSubtag(String str) {
        int length = str.length();
        if (length < 1 || length > 8 || !LocaleUtils.isAlphaNumericString(str)) {
            return false;
        }
        return true;
    }

    public static String canonicalizeLanguage(String str) {
        return LocaleUtils.toLowerString(str);
    }

    public static String canonicalizeExtlang(String str) {
        return LocaleUtils.toLowerString(str);
    }

    public static String canonicalizeScript(String str) {
        return LocaleUtils.toTitleString(str);
    }

    public static String canonicalizeRegion(String str) {
        return LocaleUtils.toUpperString(str);
    }

    public static String canonicalizeVariant(String str) {
        return LocaleUtils.toLowerString(str);
    }

    public static String canonicalizeExtension(String str) {
        return LocaleUtils.toLowerString(str);
    }

    public static String canonicalizeExtensionSingleton(String str) {
        return LocaleUtils.toLowerString(str);
    }

    public static String canonicalizeExtensionSubtag(String str) {
        return LocaleUtils.toLowerString(str);
    }

    public static String canonicalizePrivateuse(String str) {
        return LocaleUtils.toLowerString(str);
    }

    public static String canonicalizePrivateuseSubtag(String str) {
        return LocaleUtils.toLowerString(str);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.language.length() > 0) {
            sb.append(this.language);
            for (String append : this.extlangs) {
                sb.append(SEP);
                sb.append(append);
            }
            if (this.script.length() > 0) {
                sb.append(SEP);
                sb.append(this.script);
            }
            if (this.region.length() > 0) {
                sb.append(SEP);
                sb.append(this.region);
            }
            for (String append2 : this.variants) {
                sb.append(SEP);
                sb.append(append2);
            }
            for (String append3 : this.extensions) {
                sb.append(SEP);
                sb.append(append3);
            }
        }
        if (this.privateuse.length() > 0) {
            if (sb.length() > 0) {
                sb.append(SEP);
            }
            sb.append(this.privateuse);
        }
        return sb.toString();
    }
}
