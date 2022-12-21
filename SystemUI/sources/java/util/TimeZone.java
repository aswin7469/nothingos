package java.util;

import android.icu.text.TimeZoneNames;
import com.android.i18n.timezone.ZoneInfoDb;
import com.android.icu.util.ExtendedTimeZone;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.wifitrackerlib.WifiEntry;
import dalvik.system.RuntimeHooks;
import java.p026io.IOException;
import java.p026io.Serializable;
import java.time.ZoneId;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import libcore.p030io.IoUtils;

public abstract class TimeZone implements Serializable, Cloneable {
    private static final TimeZone GMT = new SimpleTimeZone(0, "GMT");
    public static final int LONG = 1;
    static final TimeZone NO_TIMEZONE = null;
    public static final int SHORT = 0;
    private static final TimeZone UTC = new SimpleTimeZone(0, "UTC");
    private static volatile TimeZone defaultTimeZone = null;
    static final long serialVersionUID = 3581463369166924961L;

    /* renamed from: ID */
    private String f710ID;

    private static native String getSystemGMTOffsetID();

    private static native String getSystemTimeZoneID(String str, String str2);

    public abstract int getOffset(int i, int i2, int i3, int i4, int i5, int i6);

    public abstract int getRawOffset();

    public abstract boolean inDaylightTime(Date date);

    public abstract void setRawOffset(int i);

    public abstract boolean useDaylightTime();

    private static class NoImagePreloadHolder {
        public static final Pattern CUSTOM_ZONE_ID_PATTERN = Pattern.compile("^GMT[-+](\\d{1,2})(:?(\\d\\d))?$");

        private NoImagePreloadHolder() {
        }
    }

    public int getOffset(long j) {
        if (inDaylightTime(new Date(j))) {
            return getRawOffset() + getDSTSavings();
        }
        return getRawOffset();
    }

    /* access modifiers changed from: package-private */
    public int getOffsets(long j, int[] iArr) {
        int rawOffset = getRawOffset();
        int dSTSavings = inDaylightTime(new Date(j)) ? getDSTSavings() : 0;
        if (iArr != null) {
            iArr[0] = rawOffset;
            iArr[1] = dSTSavings;
        }
        return rawOffset + dSTSavings;
    }

    public String getID() {
        return this.f710ID;
    }

    public void setID(String str) {
        str.getClass();
        this.f710ID = str;
    }

    public final String getDisplayName() {
        return getDisplayName(false, 1, Locale.getDefault(Locale.Category.DISPLAY));
    }

    public final String getDisplayName(Locale locale) {
        return getDisplayName(false, 1, locale);
    }

    public final String getDisplayName(boolean z, int i) {
        return getDisplayName(z, i, Locale.getDefault(Locale.Category.DISPLAY));
    }

    public String getDisplayName(boolean z, int i, Locale locale) {
        TimeZoneNames.NameType nameType;
        String displayName;
        if (i != 0) {
            if (i != 1) {
                throw new IllegalArgumentException("Illegal style: " + i);
            } else if (z) {
                nameType = TimeZoneNames.NameType.LONG_DAYLIGHT;
            } else {
                nameType = TimeZoneNames.NameType.LONG_STANDARD;
            }
        } else if (z) {
            nameType = TimeZoneNames.NameType.SHORT_DAYLIGHT;
        } else {
            nameType = TimeZoneNames.NameType.SHORT_STANDARD;
        }
        String canonicalID = android.icu.util.TimeZone.getCanonicalID(getID());
        if (canonicalID != null && (displayName = TimeZoneNames.getInstance(locale).getDisplayName(canonicalID, nameType, System.currentTimeMillis())) != null) {
            return displayName;
        }
        int rawOffset = getRawOffset();
        if (z) {
            rawOffset += getDSTSavings();
        }
        return createGmtOffsetString(true, true, rawOffset);
    }

    public static String createGmtOffsetString(boolean z, boolean z2, int i) {
        char c;
        int i2 = i / WifiEntry.FREQUENCY_60_GHZ;
        if (i2 < 0) {
            i2 = -i2;
            c = '-';
        } else {
            c = '+';
        }
        StringBuilder sb = new StringBuilder(9);
        if (z) {
            sb.append("GMT");
        }
        sb.append(c);
        appendNumber(sb, 2, i2 / 60);
        if (z2) {
            sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        }
        appendNumber(sb, 2, i2 % 60);
        return sb.toString();
    }

    private static void appendNumber(StringBuilder sb, int i, int i2) {
        String num = Integer.toString(i2);
        for (int i3 = 0; i3 < i - num.length(); i3++) {
            sb.append('0');
        }
        sb.append(num);
    }

    public int getDSTSavings() {
        return useDaylightTime() ? 3600000 : 0;
    }

    public boolean observesDaylightTime() {
        return useDaylightTime() || inDaylightTime(new Date());
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.util.TimeZone} */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0061, code lost:
        return r1;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.util.TimeZone getTimeZone(java.lang.String r4) {
        /*
            java.lang.Class<java.util.TimeZone> r0 = java.util.TimeZone.class
            monitor-enter(r0)
            if (r4 == 0) goto L_0x0064
            int r1 = r4.length()     // Catch:{ all -> 0x0062 }
            r2 = 3
            if (r1 != r2) goto L_0x0030
            java.lang.String r1 = "GMT"
            boolean r1 = r4.equals(r1)     // Catch:{ all -> 0x0062 }
            if (r1 == 0) goto L_0x001e
            java.util.TimeZone r4 = GMT     // Catch:{ all -> 0x0062 }
            java.lang.Object r4 = r4.clone()     // Catch:{ all -> 0x0062 }
            java.util.TimeZone r4 = (java.util.TimeZone) r4     // Catch:{ all -> 0x0062 }
            monitor-exit(r0)
            return r4
        L_0x001e:
            java.lang.String r1 = "UTC"
            boolean r1 = r4.equals(r1)     // Catch:{ all -> 0x0062 }
            if (r1 == 0) goto L_0x0030
            java.util.TimeZone r4 = UTC     // Catch:{ all -> 0x0062 }
            java.lang.Object r4 = r4.clone()     // Catch:{ all -> 0x0062 }
            java.util.TimeZone r4 = (java.util.TimeZone) r4     // Catch:{ all -> 0x0062 }
            monitor-exit(r0)
            return r4
        L_0x0030:
            com.android.i18n.timezone.ZoneInfoDb r1 = com.android.i18n.timezone.ZoneInfoDb.getInstance()     // Catch:{ all -> 0x0062 }
            com.android.i18n.timezone.ZoneInfoData r1 = r1.makeZoneInfoData(r4)     // Catch:{ all -> 0x0062 }
            if (r1 != 0) goto L_0x003c
            r1 = 0
            goto L_0x0040
        L_0x003c:
            libcore.util.ZoneInfo r1 = libcore.util.ZoneInfo.createZoneInfo(r1)     // Catch:{ all -> 0x0062 }
        L_0x0040:
            if (r1 != 0) goto L_0x0054
            int r3 = r4.length()     // Catch:{ all -> 0x0062 }
            if (r3 <= r2) goto L_0x0054
            java.lang.String r2 = "GMT"
            boolean r2 = r4.startsWith(r2)     // Catch:{ all -> 0x0062 }
            if (r2 == 0) goto L_0x0054
            java.util.TimeZone r1 = getCustomTimeZone(r4)     // Catch:{ all -> 0x0062 }
        L_0x0054:
            if (r1 == 0) goto L_0x0057
            goto L_0x0060
        L_0x0057:
            java.util.TimeZone r4 = GMT     // Catch:{ all -> 0x0062 }
            java.lang.Object r4 = r4.clone()     // Catch:{ all -> 0x0062 }
            r1 = r4
            java.util.TimeZone r1 = (java.util.TimeZone) r1     // Catch:{ all -> 0x0062 }
        L_0x0060:
            monitor-exit(r0)
            return r1
        L_0x0062:
            r4 = move-exception
            goto L_0x006c
        L_0x0064:
            java.lang.NullPointerException r4 = new java.lang.NullPointerException     // Catch:{ all -> 0x0062 }
            java.lang.String r1 = "id == null"
            r4.<init>(r1)     // Catch:{ all -> 0x0062 }
            throw r4     // Catch:{ all -> 0x0062 }
        L_0x006c:
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TimeZone.getTimeZone(java.lang.String):java.util.TimeZone");
    }

    public static TimeZone getTimeZone(ZoneId zoneId) {
        String id = zoneId.getId();
        char charAt = id.charAt(0);
        if (charAt == '+' || charAt == '-') {
            id = "GMT" + id;
        } else if (charAt == 'Z' && id.length() == 1) {
            id = "UTC";
        }
        return getTimeZone(id);
    }

    public ZoneId toZoneId() {
        return ZoneId.m933of(getID(), ZoneId.SHORT_IDS);
    }

    private static TimeZone getCustomTimeZone(String str) {
        Matcher matcher = NoImagePreloadHolder.CUSTOM_ZONE_ID_PATTERN.matcher(str);
        if (!matcher.matches()) {
            return null;
        }
        try {
            int parseInt = Integer.parseInt(matcher.group(1));
            int parseInt2 = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;
            if (parseInt < 0 || parseInt > 23 || parseInt2 < 0 || parseInt2 > 59) {
                return null;
            }
            char charAt = str.charAt(3);
            int i = (3600000 * parseInt) + (WifiEntry.FREQUENCY_60_GHZ * parseInt2);
            if (charAt == '-') {
                i = -i;
            }
            return new SimpleTimeZone(i, String.format(Locale.ROOT, "GMT%c%02d:%02d", Character.valueOf(charAt), Integer.valueOf(parseInt), Integer.valueOf(parseInt2)));
        } catch (NumberFormatException e) {
            throw new AssertionError((Object) e);
        }
    }

    public static synchronized String[] getAvailableIDs(int i) {
        String[] availableIDs;
        synchronized (TimeZone.class) {
            availableIDs = ZoneInfoDb.getInstance().getAvailableIDs(i);
        }
        return availableIDs;
    }

    public static synchronized String[] getAvailableIDs() {
        String[] availableIDs;
        synchronized (TimeZone.class) {
            availableIDs = ZoneInfoDb.getInstance().getAvailableIDs();
        }
        return availableIDs;
    }

    public static TimeZone getDefault() {
        return (TimeZone) getDefaultRef().clone();
    }

    static synchronized TimeZone getDefaultRef() {
        TimeZone timeZone;
        synchronized (TimeZone.class) {
            if (defaultTimeZone == null) {
                Supplier<String> timeZoneIdSupplier = RuntimeHooks.getTimeZoneIdSupplier();
                String str = timeZoneIdSupplier != null ? timeZoneIdSupplier.get() : null;
                if (str != null) {
                    str = str.trim();
                }
                if (str == null || str.isEmpty()) {
                    try {
                        str = IoUtils.readFileAsString("/etc/timezone");
                    } catch (IOException unused) {
                        str = "GMT";
                    }
                }
                defaultTimeZone = getTimeZone(str);
            }
            timeZone = defaultTimeZone;
        }
        return timeZone;
    }

    public static synchronized void setDefault(TimeZone timeZone) {
        synchronized (TimeZone.class) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new PropertyPermission("user.timezone", "write"));
            }
            defaultTimeZone = timeZone != null ? (TimeZone) timeZone.clone() : null;
            ExtendedTimeZone.clearDefaultTimeZone();
        }
    }

    public boolean hasSameRules(TimeZone timeZone) {
        return timeZone != null && getRawOffset() == timeZone.getRawOffset() && useDaylightTime() == timeZone.useDaylightTime();
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }
}
