package java.time;

import com.android.settingslib.datetime.ZoneGetter;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.zone.ZoneRules;
import java.time.zone.ZoneRulesException;
import java.time.zone.ZoneRulesProvider;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import sun.util.locale.LanguageTag;

public abstract class ZoneId implements Serializable {
    public static final Map<String, String> SHORT_IDS = Map.ofEntries(Map.entry("ACT", "Australia/Darwin"), Map.entry("AET", "Australia/Sydney"), Map.entry("AGT", "America/Argentina/Buenos_Aires"), Map.entry("ART", "Africa/Cairo"), Map.entry("AST", "America/Anchorage"), Map.entry("BET", "America/Sao_Paulo"), Map.entry("BST", "Asia/Dhaka"), Map.entry("CAT", "Africa/Harare"), Map.entry("CNT", "America/St_Johns"), Map.entry("CST", "America/Chicago"), Map.entry("CTT", "Asia/Shanghai"), Map.entry("EAT", "Africa/Addis_Ababa"), Map.entry("ECT", "Europe/Paris"), Map.entry("IET", "America/Indiana/Indianapolis"), Map.entry("IST", "Asia/Kolkata"), Map.entry("JST", "Asia/Tokyo"), Map.entry("MIT", "Pacific/Apia"), Map.entry("NET", "Asia/Yerevan"), Map.entry("NST", "Pacific/Auckland"), Map.entry("PLT", "Asia/Karachi"), Map.entry("PNT", "America/Phoenix"), Map.entry("PRT", "America/Puerto_Rico"), Map.entry("PST", "America/Los_Angeles"), Map.entry("SST", "Pacific/Guadalcanal"), Map.entry("VST", "Asia/Ho_Chi_Minh"), Map.entry("EST", "-05:00"), Map.entry("MST", "-07:00"), Map.entry("HST", "-10:00"));
    private static final long serialVersionUID = 8352817235686L;

    public abstract String getId();

    public abstract ZoneRules getRules();

    /* access modifiers changed from: package-private */
    public abstract void write(DataOutput dataOutput) throws IOException;

    public static ZoneId systemDefault() {
        return TimeZone.getDefault().toZoneId();
    }

    public static Set<String> getAvailableZoneIds() {
        return new HashSet(ZoneRulesProvider.getAvailableZoneIds());
    }

    /* renamed from: of */
    public static ZoneId m931of(String str, Map<String, String> map) {
        Objects.requireNonNull(str, "zoneId");
        Objects.requireNonNull(map, "aliasMap");
        return m930of((String) Objects.requireNonNullElse(map.get(str), str));
    }

    /* renamed from: of */
    public static ZoneId m930of(String str) {
        return m932of(str, true);
    }

    public static ZoneId ofOffset(String str, ZoneOffset zoneOffset) {
        Objects.requireNonNull(str, "prefix");
        Objects.requireNonNull(zoneOffset, ZoneGetter.KEY_OFFSET);
        if (str.isEmpty()) {
            return zoneOffset;
        }
        if (str.equals("GMT") || str.equals("UTC") || str.equals("UT")) {
            if (zoneOffset.getTotalSeconds() != 0) {
                str = str.concat(zoneOffset.getId());
            }
            return new ZoneRegion(str, zoneOffset.getRules());
        }
        throw new IllegalArgumentException("prefix should be GMT, UTC or UT, is: " + str);
    }

    /* renamed from: of */
    static ZoneId m932of(String str, boolean z) {
        Objects.requireNonNull(str, "zoneId");
        if (str.length() <= 1 || str.startsWith("+") || str.startsWith(LanguageTag.SEP)) {
            return ZoneOffset.m933of(str);
        }
        if (str.startsWith("UTC") || str.startsWith("GMT")) {
            return ofWithPrefix(str, 3, z);
        }
        if (str.startsWith("UT")) {
            return ofWithPrefix(str, 2, z);
        }
        return ZoneRegion.ofId(str, z);
    }

    private static ZoneId ofWithPrefix(String str, int i, boolean z) {
        String substring = str.substring(0, i);
        if (str.length() == i) {
            return ofOffset(substring, ZoneOffset.UTC);
        }
        if (str.charAt(i) != '+' && str.charAt(i) != '-') {
            return ZoneRegion.ofId(str, z);
        }
        try {
            ZoneOffset of = ZoneOffset.m933of(str.substring(i));
            if (of == ZoneOffset.UTC) {
                return ofOffset(substring, of);
            }
            return ofOffset(substring, of);
        } catch (DateTimeException e) {
            throw new DateTimeException("Invalid ID for offset-based ZoneId: " + str, e);
        }
    }

    public static ZoneId from(TemporalAccessor temporalAccessor) {
        ZoneId zoneId = (ZoneId) temporalAccessor.query(TemporalQueries.zone());
        if (zoneId != null) {
            return zoneId;
        }
        throw new DateTimeException("Unable to obtain ZoneId from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName());
    }

    ZoneId() {
        if (getClass() != ZoneOffset.class && getClass() != ZoneRegion.class) {
            throw new AssertionError((Object) "Invalid subclass");
        }
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        return new DateTimeFormatterBuilder().appendZoneText(textStyle).toFormatter(locale).format(toTemporal());
    }

    private TemporalAccessor toTemporal() {
        return new TemporalAccessor() {
            public boolean isSupported(TemporalField temporalField) {
                return false;
            }

            public long getLong(TemporalField temporalField) {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }

            public <R> R query(TemporalQuery<R> temporalQuery) {
                if (temporalQuery == TemporalQueries.zoneId()) {
                    return ZoneId.this;
                }
                return super.query(temporalQuery);
            }
        };
    }

    public ZoneId normalized() {
        try {
            ZoneRules rules = getRules();
            return rules.isFixedOffset() ? rules.getOffset(Instant.EPOCH) : this;
        } catch (ZoneRulesException unused) {
            return this;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZoneId) {
            return getId().equals(((ZoneId) obj).getId());
        }
        return false;
    }

    public int hashCode() {
        return getId().hashCode();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    public String toString() {
        return getId();
    }

    private Object writeReplace() {
        return new Ser((byte) 7, this);
    }
}
