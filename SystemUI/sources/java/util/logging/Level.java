package java.util.logging;

import java.p026io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class Level implements Serializable {
    public static final Level ALL = new Level("ALL", Integer.MIN_VALUE, defaultBundle);
    public static final Level CONFIG = new Level("CONFIG", 700, defaultBundle);
    public static final Level FINE = new Level("FINE", 500, defaultBundle);
    public static final Level FINER = new Level("FINER", 400, defaultBundle);
    public static final Level FINEST = new Level("FINEST", 300, defaultBundle);
    public static final Level INFO = new Level("INFO", 800, defaultBundle);
    public static final Level OFF = new Level("OFF", Integer.MAX_VALUE, defaultBundle);
    public static final Level SEVERE = new Level("SEVERE", 1000, defaultBundle);
    public static final Level WARNING = new Level("WARNING", 900, defaultBundle);
    private static final String defaultBundle = "sun.util.logging.resources.logging";
    private static final long serialVersionUID = -8176160795706313070L;
    private transient Locale cachedLocale;
    private transient String localizedLevelName;
    /* access modifiers changed from: private */
    public final String name;
    /* access modifiers changed from: private */
    public final String resourceBundleName;
    /* access modifiers changed from: private */
    public final int value;

    protected Level(String str, int i) {
        this(str, i, (String) null);
    }

    protected Level(String str, int i, String str2) {
        this(str, i, str2, true);
    }

    private Level(String str, int i, String str2, boolean z) {
        str.getClass();
        this.name = str;
        this.value = i;
        this.resourceBundleName = str2;
        this.localizedLevelName = str2 != null ? null : str;
        this.cachedLocale = null;
        if (z) {
            KnownLevel.add(this);
        }
    }

    public String getResourceBundleName() {
        return this.resourceBundleName;
    }

    public String getName() {
        return this.name;
    }

    public String getLocalizedName() {
        return getLocalizedLevelName();
    }

    /* access modifiers changed from: package-private */
    public final String getLevelName() {
        return this.name;
    }

    private String computeLocalizedLevelName(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(this.resourceBundleName, locale, Thread.currentThread().getContextClassLoader());
        String string = bundle.getString(this.name);
        if (!defaultBundle.equals(this.resourceBundleName)) {
            return string;
        }
        Locale locale2 = bundle.getLocale();
        if (Locale.ROOT.equals(locale2) || this.name.equals(string.toUpperCase(Locale.ROOT))) {
            locale2 = Locale.ROOT;
        }
        return Locale.ROOT.equals(locale2) ? this.name : string.toUpperCase(locale2);
    }

    /* access modifiers changed from: package-private */
    public final String getCachedLocalizedLevelName() {
        Locale locale;
        if (this.localizedLevelName != null && (locale = this.cachedLocale) != null && locale.equals(Locale.getDefault())) {
            return this.localizedLevelName;
        }
        if (this.resourceBundleName == null) {
            return this.name;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:6|7|8|9|10|11|12|13|14) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0014 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized java.lang.String getLocalizedLevelName() {
        /*
            r2 = this;
            monitor-enter(r2)
            java.lang.String r0 = r2.getCachedLocalizedLevelName()     // Catch:{ all -> 0x001e }
            if (r0 == 0) goto L_0x0009
            monitor-exit(r2)
            return r0
        L_0x0009:
            java.util.Locale r0 = java.util.Locale.getDefault()     // Catch:{ all -> 0x001e }
            java.lang.String r1 = r2.computeLocalizedLevelName(r0)     // Catch:{ Exception -> 0x0014 }
            r2.localizedLevelName = r1     // Catch:{ Exception -> 0x0014 }
            goto L_0x0018
        L_0x0014:
            java.lang.String r1 = r2.name     // Catch:{ all -> 0x001e }
            r2.localizedLevelName = r1     // Catch:{ all -> 0x001e }
        L_0x0018:
            r2.cachedLocale = r0     // Catch:{ all -> 0x001e }
            java.lang.String r0 = r2.localizedLevelName     // Catch:{ all -> 0x001e }
            monitor-exit(r2)
            return r0
        L_0x001e:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.Level.getLocalizedLevelName():java.lang.String");
    }

    static Level findLevel(String str) {
        str.getClass();
        KnownLevel findByName = KnownLevel.findByName(str);
        if (findByName != null) {
            return findByName.mirroredLevel;
        }
        try {
            int parseInt = Integer.parseInt(str);
            KnownLevel findByValue = KnownLevel.findByValue(parseInt);
            if (findByValue == null) {
                new Level(str, parseInt);
                findByValue = KnownLevel.findByValue(parseInt);
            }
            return findByValue.mirroredLevel;
        } catch (NumberFormatException unused) {
            KnownLevel findByLocalizedLevelName = KnownLevel.findByLocalizedLevelName(str);
            if (findByLocalizedLevelName != null) {
                return findByLocalizedLevelName.mirroredLevel;
            }
            return null;
        }
    }

    public final String toString() {
        return this.name;
    }

    public final int intValue() {
        return this.value;
    }

    private Object readResolve() {
        KnownLevel matches = KnownLevel.matches(this);
        if (matches != null) {
            return matches.levelObject;
        }
        return new Level(this.name, this.value, this.resourceBundleName);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:15|16|(3:18|19|20)(3:21|22|23)) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r2 = java.util.logging.Level.KnownLevel.findByLocalizedLevelName(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002d, code lost:
        if (r2 != null) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002f, code lost:
        r4 = r2.levelObject;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0032, code lost:
        return r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0049, code lost:
        throw new java.lang.IllegalArgumentException("Bad level \"" + r4 + "\"");
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0029 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.util.logging.Level parse(java.lang.String r4) throws java.lang.IllegalArgumentException {
        /*
            java.lang.String r0 = "Bad level \""
            java.lang.Class<java.util.logging.Level> r1 = java.util.logging.Level.class
            monitor-enter(r1)
            r4.length()     // Catch:{ all -> 0x004a }
            java.util.logging.Level$KnownLevel r2 = java.util.logging.Level.KnownLevel.findByName(r4)     // Catch:{ all -> 0x004a }
            if (r2 == 0) goto L_0x0012
            java.util.logging.Level r4 = r2.levelObject     // Catch:{ all -> 0x004a }
            monitor-exit(r1)
            return r4
        L_0x0012:
            int r2 = java.lang.Integer.parseInt(r4)     // Catch:{ NumberFormatException -> 0x0029 }
            java.util.logging.Level$KnownLevel r3 = java.util.logging.Level.KnownLevel.findByValue(r2)     // Catch:{ NumberFormatException -> 0x0029 }
            if (r3 != 0) goto L_0x0025
            java.util.logging.Level r3 = new java.util.logging.Level     // Catch:{ NumberFormatException -> 0x0029 }
            r3.<init>(r4, r2)     // Catch:{ NumberFormatException -> 0x0029 }
            java.util.logging.Level$KnownLevel r3 = java.util.logging.Level.KnownLevel.findByValue(r2)     // Catch:{ NumberFormatException -> 0x0029 }
        L_0x0025:
            java.util.logging.Level r4 = r3.levelObject     // Catch:{ NumberFormatException -> 0x0029 }
            monitor-exit(r1)
            return r4
        L_0x0029:
            java.util.logging.Level$KnownLevel r2 = java.util.logging.Level.KnownLevel.findByLocalizedLevelName(r4)     // Catch:{ all -> 0x004a }
            if (r2 == 0) goto L_0x0033
            java.util.logging.Level r4 = r2.levelObject     // Catch:{ all -> 0x004a }
            monitor-exit(r1)
            return r4
        L_0x0033:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x004a }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x004a }
            r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x004a }
            r3.append((java.lang.String) r4)     // Catch:{ all -> 0x004a }
            java.lang.String r4 = "\""
            r3.append((java.lang.String) r4)     // Catch:{ all -> 0x004a }
            java.lang.String r4 = r3.toString()     // Catch:{ all -> 0x004a }
            r2.<init>((java.lang.String) r4)     // Catch:{ all -> 0x004a }
            throw r2     // Catch:{ all -> 0x004a }
        L_0x004a:
            r4 = move-exception
            monitor-exit(r1)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.Level.parse(java.lang.String):java.util.logging.Level");
    }

    public boolean equals(Object obj) {
        try {
            if (((Level) obj).value == this.value) {
                return true;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public int hashCode() {
        return this.value;
    }

    static final class KnownLevel {
        private static Map<Integer, List<KnownLevel>> intToLevels = new HashMap();
        private static Map<String, List<KnownLevel>> nameToLevels = new HashMap();
        final Level levelObject;
        final Level mirroredLevel;

        KnownLevel(Level level) {
            this.levelObject = level;
            if (level.getClass() == Level.class) {
                this.mirroredLevel = level;
            } else {
                this.mirroredLevel = new Level(level.name, level.value, level.resourceBundleName, false);
            }
        }

        static synchronized void add(Level level) {
            synchronized (KnownLevel.class) {
                KnownLevel knownLevel = new KnownLevel(level);
                List list = nameToLevels.get(level.name);
                if (list == null) {
                    list = new ArrayList();
                    nameToLevels.put(level.name, list);
                }
                list.add(knownLevel);
                List list2 = intToLevels.get(Integer.valueOf(level.value));
                if (list2 == null) {
                    list2 = new ArrayList();
                    intToLevels.put(Integer.valueOf(level.value), list2);
                }
                list2.add(knownLevel);
            }
        }

        static synchronized KnownLevel findByName(String str) {
            synchronized (KnownLevel.class) {
                List list = nameToLevels.get(str);
                if (list == null) {
                    return null;
                }
                KnownLevel knownLevel = (KnownLevel) list.get(0);
                return knownLevel;
            }
        }

        static synchronized KnownLevel findByValue(int i) {
            synchronized (KnownLevel.class) {
                List list = intToLevels.get(Integer.valueOf(i));
                if (list == null) {
                    return null;
                }
                KnownLevel knownLevel = (KnownLevel) list.get(0);
                return knownLevel;
            }
        }

        static synchronized KnownLevel findByLocalizedLevelName(String str) {
            synchronized (KnownLevel.class) {
                for (List<KnownLevel> it : nameToLevels.values()) {
                    Iterator it2 = it.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            KnownLevel knownLevel = (KnownLevel) it2.next();
                            if (str.equals(knownLevel.levelObject.getLocalizedLevelName())) {
                                return knownLevel;
                            }
                        }
                    }
                }
                return null;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:21:0x005a, code lost:
            return null;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static synchronized java.util.logging.Level.KnownLevel matches(java.util.logging.Level r7) {
            /*
                java.lang.Class<java.util.logging.Level$KnownLevel> r0 = java.util.logging.Level.KnownLevel.class
                monitor-enter(r0)
                java.util.Map<java.lang.String, java.util.List<java.util.logging.Level$KnownLevel>> r1 = nameToLevels     // Catch:{ all -> 0x005c }
                java.lang.String r2 = r7.name     // Catch:{ all -> 0x005c }
                java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x005c }
                java.util.List r1 = (java.util.List) r1     // Catch:{ all -> 0x005c }
                if (r1 == 0) goto L_0x0059
                java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x005c }
            L_0x0015:
                boolean r2 = r1.hasNext()     // Catch:{ all -> 0x005c }
                if (r2 == 0) goto L_0x0059
                java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x005c }
                java.util.logging.Level$KnownLevel r2 = (java.util.logging.Level.KnownLevel) r2     // Catch:{ all -> 0x005c }
                java.util.logging.Level r3 = r2.mirroredLevel     // Catch:{ all -> 0x005c }
                java.util.logging.Level r4 = r2.levelObject     // Catch:{ all -> 0x005c }
                java.lang.Class r4 = r4.getClass()     // Catch:{ all -> 0x005c }
                int r5 = r7.value     // Catch:{ all -> 0x005c }
                int r6 = r3.value     // Catch:{ all -> 0x005c }
                if (r5 != r6) goto L_0x0015
                java.lang.String r5 = r7.resourceBundleName     // Catch:{ all -> 0x005c }
                java.lang.String r6 = r3.resourceBundleName     // Catch:{ all -> 0x005c }
                if (r5 == r6) goto L_0x0051
                java.lang.String r5 = r7.resourceBundleName     // Catch:{ all -> 0x005c }
                if (r5 == 0) goto L_0x0015
                java.lang.String r5 = r7.resourceBundleName     // Catch:{ all -> 0x005c }
                java.lang.String r3 = r3.resourceBundleName     // Catch:{ all -> 0x005c }
                boolean r3 = r5.equals(r3)     // Catch:{ all -> 0x005c }
                if (r3 == 0) goto L_0x0015
            L_0x0051:
                java.lang.Class r3 = r7.getClass()     // Catch:{ all -> 0x005c }
                if (r4 != r3) goto L_0x0015
                monitor-exit(r0)
                return r2
            L_0x0059:
                monitor-exit(r0)
                r7 = 0
                return r7
            L_0x005c:
                r7 = move-exception
                monitor-exit(r0)
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.logging.Level.KnownLevel.matches(java.util.logging.Level):java.util.logging.Level$KnownLevel");
        }
    }
}
