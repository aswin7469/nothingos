package sun.util.locale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class InternalLocaleBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final CaseInsensitiveChar PRIVATEUSE_KEY = new CaseInsensitiveChar(LanguageTag.PRIVATEUSE);
    private Map<CaseInsensitiveChar, String> extensions;
    private String language = "";
    private String region = "";
    private String script = "";
    private Set<CaseInsensitiveString> uattributes;
    private Map<CaseInsensitiveString, String> ukeywords;
    private String variant = "";

    public InternalLocaleBuilder setLanguage(String str) throws LocaleSyntaxException {
        if (LocaleUtils.isEmpty(str)) {
            this.language = "";
        } else if (LanguageTag.isLanguage(str)) {
            this.language = str;
        } else {
            throw new LocaleSyntaxException("Ill-formed language: " + str, 0);
        }
        return this;
    }

    public InternalLocaleBuilder setScript(String str) throws LocaleSyntaxException {
        if (LocaleUtils.isEmpty(str)) {
            this.script = "";
        } else if (LanguageTag.isScript(str)) {
            this.script = str;
        } else {
            throw new LocaleSyntaxException("Ill-formed script: " + str, 0);
        }
        return this;
    }

    public InternalLocaleBuilder setRegion(String str) throws LocaleSyntaxException {
        if (LocaleUtils.isEmpty(str)) {
            this.region = "";
        } else if (LanguageTag.isRegion(str)) {
            this.region = str;
        } else {
            throw new LocaleSyntaxException("Ill-formed region: " + str, 0);
        }
        return this;
    }

    public InternalLocaleBuilder setVariant(String str) throws LocaleSyntaxException {
        if (LocaleUtils.isEmpty(str)) {
            this.variant = "";
        } else {
            String replaceAll = str.replaceAll(LanguageTag.SEP, BaseLocale.SEP);
            int checkVariants = checkVariants(replaceAll, BaseLocale.SEP);
            if (checkVariants == -1) {
                this.variant = replaceAll;
            } else {
                throw new LocaleSyntaxException("Ill-formed variant: " + str, checkVariants);
            }
        }
        return this;
    }

    public InternalLocaleBuilder addUnicodeLocaleAttribute(String str) throws LocaleSyntaxException {
        if (UnicodeLocaleExtension.isAttribute(str)) {
            if (this.uattributes == null) {
                this.uattributes = new HashSet(4);
            }
            this.uattributes.add(new CaseInsensitiveString(str));
            return this;
        }
        throw new LocaleSyntaxException("Ill-formed Unicode locale attribute: " + str);
    }

    public InternalLocaleBuilder removeUnicodeLocaleAttribute(String str) throws LocaleSyntaxException {
        if (str == null || !UnicodeLocaleExtension.isAttribute(str)) {
            throw new LocaleSyntaxException("Ill-formed Unicode locale attribute: " + str);
        }
        Set<CaseInsensitiveString> set = this.uattributes;
        if (set != null) {
            set.remove(new CaseInsensitiveString(str));
        }
        return this;
    }

    public InternalLocaleBuilder setUnicodeLocaleKeyword(String str, String str2) throws LocaleSyntaxException {
        if (UnicodeLocaleExtension.isKey(str)) {
            CaseInsensitiveString caseInsensitiveString = new CaseInsensitiveString(str);
            if (str2 == null) {
                Map<CaseInsensitiveString, String> map = this.ukeywords;
                if (map != null) {
                    map.remove(caseInsensitiveString);
                }
            } else {
                if (str2.length() != 0) {
                    StringTokenIterator stringTokenIterator = new StringTokenIterator(str2.replaceAll(BaseLocale.SEP, LanguageTag.SEP), LanguageTag.SEP);
                    while (!stringTokenIterator.isDone()) {
                        if (UnicodeLocaleExtension.isTypeSubtag(stringTokenIterator.current())) {
                            stringTokenIterator.next();
                        } else {
                            throw new LocaleSyntaxException("Ill-formed Unicode locale keyword type: " + str2, stringTokenIterator.currentStart());
                        }
                    }
                }
                if (this.ukeywords == null) {
                    this.ukeywords = new HashMap(4);
                }
                this.ukeywords.put(caseInsensitiveString, str2);
            }
            return this;
        }
        throw new LocaleSyntaxException("Ill-formed Unicode locale keyword key: " + str);
    }

    public InternalLocaleBuilder setExtension(char c, String str) throws LocaleSyntaxException {
        boolean z;
        boolean isPrivateusePrefixChar = LanguageTag.isPrivateusePrefixChar(c);
        if (isPrivateusePrefixChar || LanguageTag.isExtensionSingletonChar(c)) {
            boolean isEmpty = LocaleUtils.isEmpty(str);
            CaseInsensitiveChar caseInsensitiveChar = new CaseInsensitiveChar(c);
            if (!isEmpty) {
                String replaceAll = str.replaceAll(BaseLocale.SEP, LanguageTag.SEP);
                StringTokenIterator stringTokenIterator = new StringTokenIterator(replaceAll, LanguageTag.SEP);
                while (!stringTokenIterator.isDone()) {
                    String current = stringTokenIterator.current();
                    if (isPrivateusePrefixChar) {
                        z = LanguageTag.isPrivateuseSubtag(current);
                    } else {
                        z = LanguageTag.isExtensionSubtag(current);
                    }
                    if (z) {
                        stringTokenIterator.next();
                    } else {
                        throw new LocaleSyntaxException("Ill-formed extension value: " + current, stringTokenIterator.currentStart());
                    }
                }
                if (UnicodeLocaleExtension.isSingletonChar(caseInsensitiveChar.value())) {
                    setUnicodeLocaleExtension(replaceAll);
                } else {
                    if (this.extensions == null) {
                        this.extensions = new HashMap(4);
                    }
                    this.extensions.put(caseInsensitiveChar, replaceAll);
                }
            } else if (UnicodeLocaleExtension.isSingletonChar(caseInsensitiveChar.value())) {
                Set<CaseInsensitiveString> set = this.uattributes;
                if (set != null) {
                    set.clear();
                }
                Map<CaseInsensitiveString, String> map = this.ukeywords;
                if (map != null) {
                    map.clear();
                }
            } else {
                Map<CaseInsensitiveChar, String> map2 = this.extensions;
                if (map2 != null && map2.containsKey(caseInsensitiveChar)) {
                    this.extensions.remove(caseInsensitiveChar);
                }
            }
            return this;
        }
        throw new LocaleSyntaxException("Ill-formed extension key: " + c);
    }

    public InternalLocaleBuilder setExtensions(String str) throws LocaleSyntaxException {
        if (LocaleUtils.isEmpty(str)) {
            clearExtensions();
            return this;
        }
        String replaceAll = str.replaceAll(BaseLocale.SEP, LanguageTag.SEP);
        StringTokenIterator stringTokenIterator = new StringTokenIterator(replaceAll, LanguageTag.SEP);
        String str2 = null;
        int i = 0;
        ArrayList arrayList = null;
        while (!stringTokenIterator.isDone()) {
            String current = stringTokenIterator.current();
            if (!LanguageTag.isExtensionSingleton(current)) {
                break;
            }
            int currentStart = stringTokenIterator.currentStart();
            StringBuilder sb = new StringBuilder(current);
            stringTokenIterator.next();
            while (!stringTokenIterator.isDone()) {
                String current2 = stringTokenIterator.current();
                if (!LanguageTag.isExtensionSubtag(current2)) {
                    break;
                }
                sb.append(LanguageTag.SEP);
                sb.append(current2);
                i = stringTokenIterator.currentEnd();
                stringTokenIterator.next();
            }
            if (i >= currentStart) {
                if (arrayList == null) {
                    arrayList = new ArrayList(4);
                }
                arrayList.add(sb.toString());
            } else {
                throw new LocaleSyntaxException("Incomplete extension '" + current + "'", currentStart);
            }
        }
        if (!stringTokenIterator.isDone()) {
            String current3 = stringTokenIterator.current();
            if (LanguageTag.isPrivateusePrefix(current3)) {
                int currentStart2 = stringTokenIterator.currentStart();
                StringBuilder sb2 = new StringBuilder(current3);
                stringTokenIterator.next();
                while (!stringTokenIterator.isDone()) {
                    String current4 = stringTokenIterator.current();
                    if (!LanguageTag.isPrivateuseSubtag(current4)) {
                        break;
                    }
                    sb2.append(LanguageTag.SEP);
                    sb2.append(current4);
                    i = stringTokenIterator.currentEnd();
                    stringTokenIterator.next();
                }
                if (i > currentStart2) {
                    str2 = sb2.toString();
                } else {
                    throw new LocaleSyntaxException("Incomplete privateuse:" + replaceAll.substring(currentStart2), currentStart2);
                }
            }
        }
        if (stringTokenIterator.isDone()) {
            return setExtensions(arrayList, str2);
        }
        throw new LocaleSyntaxException("Ill-formed extension subtags:" + replaceAll.substring(stringTokenIterator.currentStart()), stringTokenIterator.currentStart());
    }

    private InternalLocaleBuilder setExtensions(List<String> list, String str) {
        clearExtensions();
        if (!LocaleUtils.isEmpty((List<?>) list)) {
            HashSet hashSet = new HashSet(list.size());
            for (String next : list) {
                CaseInsensitiveChar caseInsensitiveChar = new CaseInsensitiveChar(next);
                if (!hashSet.contains(caseInsensitiveChar)) {
                    if (UnicodeLocaleExtension.isSingletonChar(caseInsensitiveChar.value())) {
                        setUnicodeLocaleExtension(next.substring(2));
                    } else {
                        if (this.extensions == null) {
                            this.extensions = new HashMap(4);
                        }
                        this.extensions.put(caseInsensitiveChar, next.substring(2));
                    }
                }
                hashSet.add(caseInsensitiveChar);
            }
        }
        if (str != null && !str.isEmpty()) {
            if (this.extensions == null) {
                this.extensions = new HashMap(1);
            }
            this.extensions.put(new CaseInsensitiveChar(str), str.substring(2));
        }
        return this;
    }

    public InternalLocaleBuilder setLanguageTag(LanguageTag languageTag) {
        clear();
        if (!languageTag.getExtlangs().isEmpty()) {
            this.language = languageTag.getExtlangs().get(0);
        } else {
            String language2 = languageTag.getLanguage();
            if (!language2.equals(LanguageTag.UNDETERMINED)) {
                this.language = language2;
            }
        }
        this.script = languageTag.getScript();
        this.region = languageTag.getRegion();
        List<String> variants = languageTag.getVariants();
        if (!variants.isEmpty()) {
            StringBuilder sb = new StringBuilder(variants.get(0));
            int size = variants.size();
            for (int i = 1; i < size; i++) {
                sb.append(BaseLocale.SEP);
                sb.append(variants.get(i));
            }
            this.variant = sb.toString();
        }
        setExtensions(languageTag.getExtensions(), languageTag.getPrivateuse());
        return this;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00c5, code lost:
        r7 = r7.replaceAll(sun.util.locale.LanguageTag.SEP, sun.util.locale.BaseLocale.SEP);
     */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x007d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public sun.util.locale.InternalLocaleBuilder setLocale(sun.util.locale.BaseLocale r7, sun.util.locale.LocaleExtensions r8) throws sun.util.locale.LocaleSyntaxException {
        /*
            r6 = this;
            java.lang.String r0 = r7.getLanguage()
            java.lang.String r1 = r7.getScript()
            java.lang.String r2 = r7.getRegion()
            java.lang.String r7 = r7.getVariant()
            java.lang.String r3 = "ja"
            boolean r3 = r0.equals(r3)
            java.lang.String r4 = ""
            if (r3 == 0) goto L_0x002a
            java.lang.String r3 = "JP"
            boolean r5 = r2.equals(r3)
            if (r5 == 0) goto L_0x002a
            boolean r3 = r7.equals(r3)
            if (r3 == 0) goto L_0x002a
        L_0x0028:
            r7 = r4
            goto L_0x005c
        L_0x002a:
            java.lang.String r3 = "th"
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L_0x0041
            java.lang.String r3 = "TH"
            boolean r5 = r2.equals(r3)
            if (r5 == 0) goto L_0x0041
            boolean r3 = r7.equals(r3)
            if (r3 == 0) goto L_0x0041
            goto L_0x0028
        L_0x0041:
            java.lang.String r3 = "no"
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L_0x005c
            java.lang.String r3 = "NO"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L_0x005c
            java.lang.String r3 = "NY"
            boolean r3 = r7.equals(r3)
            if (r3 == 0) goto L_0x005c
            java.lang.String r0 = "nn"
            goto L_0x0028
        L_0x005c:
            boolean r3 = r0.isEmpty()
            if (r3 != 0) goto L_0x007d
            boolean r3 = sun.util.locale.LanguageTag.isLanguage(r0)
            if (r3 == 0) goto L_0x0069
            goto L_0x007d
        L_0x0069:
            sun.util.locale.LocaleSyntaxException r6 = new sun.util.locale.LocaleSyntaxException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = "Ill-formed language: "
            r7.<init>((java.lang.String) r8)
            r7.append((java.lang.String) r0)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            throw r6
        L_0x007d:
            boolean r3 = r1.isEmpty()
            if (r3 != 0) goto L_0x009e
            boolean r3 = sun.util.locale.LanguageTag.isScript(r1)
            if (r3 == 0) goto L_0x008a
            goto L_0x009e
        L_0x008a:
            sun.util.locale.LocaleSyntaxException r6 = new sun.util.locale.LocaleSyntaxException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = "Ill-formed script: "
            r7.<init>((java.lang.String) r8)
            r7.append((java.lang.String) r1)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            throw r6
        L_0x009e:
            boolean r3 = r2.isEmpty()
            if (r3 != 0) goto L_0x00bf
            boolean r3 = sun.util.locale.LanguageTag.isRegion(r2)
            if (r3 == 0) goto L_0x00ab
            goto L_0x00bf
        L_0x00ab:
            sun.util.locale.LocaleSyntaxException r6 = new sun.util.locale.LocaleSyntaxException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = "Ill-formed region: "
            r7.<init>((java.lang.String) r8)
            r7.append((java.lang.String) r2)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            throw r6
        L_0x00bf:
            boolean r3 = r7.isEmpty()
            if (r3 != 0) goto L_0x00e9
            java.lang.String r3 = "-"
            java.lang.String r4 = "_"
            java.lang.String r7 = r7.replaceAll(r3, r4)
            int r3 = r6.checkVariants(r7, r4)
            r4 = -1
            if (r3 != r4) goto L_0x00d5
            goto L_0x00e9
        L_0x00d5:
            sun.util.locale.LocaleSyntaxException r6 = new sun.util.locale.LocaleSyntaxException
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r0 = "Ill-formed variant: "
            r8.<init>((java.lang.String) r0)
            r8.append((java.lang.String) r7)
            java.lang.String r7 = r8.toString()
            r6.<init>(r7, r3)
            throw r6
        L_0x00e9:
            r6.language = r0
            r6.script = r1
            r6.region = r2
            r6.variant = r7
            r6.clearExtensions()
            if (r8 != 0) goto L_0x00f8
            r7 = 0
            goto L_0x00fc
        L_0x00f8:
            java.util.Set r7 = r8.getKeys()
        L_0x00fc:
            if (r7 == 0) goto L_0x0190
            java.util.Iterator r7 = r7.iterator()
        L_0x0102:
            boolean r0 = r7.hasNext()
            if (r0 == 0) goto L_0x0190
            java.lang.Object r0 = r7.next()
            java.lang.Character r0 = (java.lang.Character) r0
            sun.util.locale.Extension r1 = r8.getExtension(r0)
            boolean r2 = r1 instanceof sun.util.locale.UnicodeLocaleExtension
            r3 = 4
            if (r2 == 0) goto L_0x0171
            sun.util.locale.UnicodeLocaleExtension r1 = (sun.util.locale.UnicodeLocaleExtension) r1
            java.util.Set r0 = r1.getUnicodeLocaleAttributes()
            java.util.Iterator r0 = r0.iterator()
        L_0x0121:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0143
            java.lang.Object r2 = r0.next()
            java.lang.String r2 = (java.lang.String) r2
            java.util.Set<sun.util.locale.InternalLocaleBuilder$CaseInsensitiveString> r4 = r6.uattributes
            if (r4 != 0) goto L_0x0138
            java.util.HashSet r4 = new java.util.HashSet
            r4.<init>((int) r3)
            r6.uattributes = r4
        L_0x0138:
            java.util.Set<sun.util.locale.InternalLocaleBuilder$CaseInsensitiveString> r4 = r6.uattributes
            sun.util.locale.InternalLocaleBuilder$CaseInsensitiveString r5 = new sun.util.locale.InternalLocaleBuilder$CaseInsensitiveString
            r5.<init>(r2)
            r4.add(r5)
            goto L_0x0121
        L_0x0143:
            java.util.Set r0 = r1.getUnicodeLocaleKeys()
            java.util.Iterator r0 = r0.iterator()
        L_0x014b:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0102
            java.lang.Object r2 = r0.next()
            java.lang.String r2 = (java.lang.String) r2
            java.util.Map<sun.util.locale.InternalLocaleBuilder$CaseInsensitiveString, java.lang.String> r4 = r6.ukeywords
            if (r4 != 0) goto L_0x0162
            java.util.HashMap r4 = new java.util.HashMap
            r4.<init>((int) r3)
            r6.ukeywords = r4
        L_0x0162:
            java.util.Map<sun.util.locale.InternalLocaleBuilder$CaseInsensitiveString, java.lang.String> r4 = r6.ukeywords
            sun.util.locale.InternalLocaleBuilder$CaseInsensitiveString r5 = new sun.util.locale.InternalLocaleBuilder$CaseInsensitiveString
            r5.<init>(r2)
            java.lang.String r2 = r1.getUnicodeLocaleType(r2)
            r4.put(r5, r2)
            goto L_0x014b
        L_0x0171:
            java.util.Map<sun.util.locale.InternalLocaleBuilder$CaseInsensitiveChar, java.lang.String> r2 = r6.extensions
            if (r2 != 0) goto L_0x017c
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>((int) r3)
            r6.extensions = r2
        L_0x017c:
            java.util.Map<sun.util.locale.InternalLocaleBuilder$CaseInsensitiveChar, java.lang.String> r2 = r6.extensions
            sun.util.locale.InternalLocaleBuilder$CaseInsensitiveChar r3 = new sun.util.locale.InternalLocaleBuilder$CaseInsensitiveChar
            char r0 = r0.charValue()
            r3.<init>((char) r0)
            java.lang.String r0 = r1.getValue()
            r2.put(r3, r0)
            goto L_0x0102
        L_0x0190:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.InternalLocaleBuilder.setLocale(sun.util.locale.BaseLocale, sun.util.locale.LocaleExtensions):sun.util.locale.InternalLocaleBuilder");
    }

    public InternalLocaleBuilder clear() {
        this.language = "";
        this.script = "";
        this.region = "";
        this.variant = "";
        clearExtensions();
        return this;
    }

    public InternalLocaleBuilder clearExtensions() {
        Map<CaseInsensitiveChar, String> map = this.extensions;
        if (map != null) {
            map.clear();
        }
        Set<CaseInsensitiveString> set = this.uattributes;
        if (set != null) {
            set.clear();
        }
        Map<CaseInsensitiveString, String> map2 = this.ukeywords;
        if (map2 != null) {
            map2.clear();
        }
        return this;
    }

    public BaseLocale getBaseLocale() {
        String str;
        int i;
        String str2 = this.language;
        String str3 = this.script;
        String str4 = this.region;
        String str5 = this.variant;
        Map<CaseInsensitiveChar, String> map = this.extensions;
        if (!(map == null || (str = map.get(PRIVATEUSE_KEY)) == null)) {
            StringTokenIterator stringTokenIterator = new StringTokenIterator(str, LanguageTag.SEP);
            boolean z = false;
            while (true) {
                if (stringTokenIterator.isDone()) {
                    i = -1;
                    break;
                } else if (z) {
                    i = stringTokenIterator.currentStart();
                    break;
                } else {
                    if (LocaleUtils.caseIgnoreMatch(stringTokenIterator.current(), LanguageTag.PRIVUSE_VARIANT_PREFIX)) {
                        z = true;
                    }
                    stringTokenIterator.next();
                }
            }
            if (i != -1) {
                StringBuilder sb = new StringBuilder(str5);
                if (sb.length() != 0) {
                    sb.append(BaseLocale.SEP);
                }
                sb.append(str.substring(i).replaceAll(LanguageTag.SEP, BaseLocale.SEP));
                str5 = sb.toString();
            }
        }
        return BaseLocale.getInstance(str2, str3, str4, str5);
    }

    public LocaleExtensions getLocaleExtensions() {
        if (LocaleUtils.isEmpty((Map<?, ?>) this.extensions) && LocaleUtils.isEmpty((Set<?>) this.uattributes) && LocaleUtils.isEmpty((Map<?, ?>) this.ukeywords)) {
            return null;
        }
        LocaleExtensions localeExtensions = new LocaleExtensions(this.extensions, this.uattributes, this.ukeywords);
        if (localeExtensions.isEmpty()) {
            return null;
        }
        return localeExtensions;
    }

    static String removePrivateuseVariant(String str) {
        boolean z;
        StringTokenIterator stringTokenIterator = new StringTokenIterator(str, LanguageTag.SEP);
        int i = -1;
        while (true) {
            if (stringTokenIterator.isDone()) {
                z = false;
                break;
            } else if (i != -1) {
                z = true;
                break;
            } else {
                if (LocaleUtils.caseIgnoreMatch(stringTokenIterator.current(), LanguageTag.PRIVUSE_VARIANT_PREFIX)) {
                    i = stringTokenIterator.currentStart();
                }
                stringTokenIterator.next();
            }
        }
        if (!z) {
            return str;
        }
        if (i == 0) {
            return null;
        }
        return str.substring(0, i - 1);
    }

    private int checkVariants(String str, String str2) {
        StringTokenIterator stringTokenIterator = new StringTokenIterator(str, str2);
        while (!stringTokenIterator.isDone()) {
            if (!LanguageTag.isVariant(stringTokenIterator.current())) {
                return stringTokenIterator.currentStart();
            }
            stringTokenIterator.next();
        }
        return -1;
    }

    private void setUnicodeLocaleExtension(String str) {
        String str2;
        Set<CaseInsensitiveString> set = this.uattributes;
        if (set != null) {
            set.clear();
        }
        Map<CaseInsensitiveString, String> map = this.ukeywords;
        if (map != null) {
            map.clear();
        }
        StringTokenIterator stringTokenIterator = new StringTokenIterator(str, LanguageTag.SEP);
        while (!stringTokenIterator.isDone() && UnicodeLocaleExtension.isAttribute(stringTokenIterator.current())) {
            if (this.uattributes == null) {
                this.uattributes = new HashSet(4);
            }
            this.uattributes.add(new CaseInsensitiveString(stringTokenIterator.current()));
            stringTokenIterator.next();
        }
        CaseInsensitiveString caseInsensitiveString = null;
        int i = -1;
        int i2 = -1;
        while (!stringTokenIterator.isDone()) {
            String str3 = "";
            if (caseInsensitiveString != null) {
                if (UnicodeLocaleExtension.isKey(stringTokenIterator.current())) {
                    if (i == -1) {
                        str2 = str3;
                    } else {
                        str2 = str.substring(i, i2);
                    }
                    if (this.ukeywords == null) {
                        this.ukeywords = new HashMap(4);
                    }
                    this.ukeywords.put(caseInsensitiveString, str2);
                    caseInsensitiveString = new CaseInsensitiveString(stringTokenIterator.current());
                    if (this.ukeywords.containsKey(caseInsensitiveString)) {
                        caseInsensitiveString = null;
                    }
                    i = -1;
                    i2 = -1;
                } else {
                    if (i == -1) {
                        i = stringTokenIterator.currentStart();
                    }
                    i2 = stringTokenIterator.currentEnd();
                }
            } else if (UnicodeLocaleExtension.isKey(stringTokenIterator.current())) {
                caseInsensitiveString = new CaseInsensitiveString(stringTokenIterator.current());
                Map<CaseInsensitiveString, String> map2 = this.ukeywords;
                if (map2 != null && map2.containsKey(caseInsensitiveString)) {
                    caseInsensitiveString = null;
                }
            }
            if (stringTokenIterator.hasNext()) {
                stringTokenIterator.next();
            } else if (caseInsensitiveString != null) {
                if (i != -1) {
                    str3 = str.substring(i, i2);
                }
                if (this.ukeywords == null) {
                    this.ukeywords = new HashMap(4);
                }
                this.ukeywords.put(caseInsensitiveString, str3);
                return;
            } else {
                return;
            }
        }
    }

    static final class CaseInsensitiveString {
        private final String lowerStr;
        private final String str;

        CaseInsensitiveString(String str2) {
            this.str = str2;
            this.lowerStr = LocaleUtils.toLowerString(str2);
        }

        public String value() {
            return this.str;
        }

        public int hashCode() {
            return this.lowerStr.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CaseInsensitiveString)) {
                return false;
            }
            return this.lowerStr.equals(((CaseInsensitiveString) obj).lowerStr);
        }
    }

    static final class CaseInsensitiveChar {

        /* renamed from: ch */
        private final char f937ch;
        private final char lowerCh;

        private CaseInsensitiveChar(String str) {
            this(str.charAt(0));
        }

        CaseInsensitiveChar(char c) {
            this.f937ch = c;
            this.lowerCh = LocaleUtils.toLower(c);
        }

        public char value() {
            return this.f937ch;
        }

        public int hashCode() {
            return this.lowerCh;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CaseInsensitiveChar)) {
                return false;
            }
            if (this.lowerCh == ((CaseInsensitiveChar) obj).lowerCh) {
                return true;
            }
            return false;
        }
    }
}
