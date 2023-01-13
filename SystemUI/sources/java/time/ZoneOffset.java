package java.time;

import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.zone.ZoneRules;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.util.locale.LanguageTag;

public final class ZoneOffset extends ZoneId implements TemporalAccessor, TemporalAdjuster, Comparable<ZoneOffset>, Serializable {
    private static final ConcurrentMap<String, ZoneOffset> ID_CACHE = new ConcurrentHashMap(16, 0.75f, 4);
    public static final ZoneOffset MAX = ofTotalSeconds(MAX_SECONDS);
    private static final int MAX_SECONDS = 64800;
    public static final ZoneOffset MIN = ofTotalSeconds(-64800);
    private static final ConcurrentMap<Integer, ZoneOffset> SECONDS_CACHE = new ConcurrentHashMap(16, 0.75f, 4);
    public static final ZoneOffset UTC = ofTotalSeconds(0);
    private static final long serialVersionUID = 2357656521762053153L;

    /* renamed from: id */
    private final transient String f377id;
    private final int totalSeconds;

    private static int totalSeconds(int i, int i2, int i3) {
        return (i * 3600) + (i2 * 60) + i3;
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0098 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00b9  */
    /* renamed from: of */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.time.ZoneOffset m933of(java.lang.String r7) {
        /*
            java.lang.String r0 = "offsetId"
            java.util.Objects.requireNonNull(r7, (java.lang.String) r0)
            java.util.concurrent.ConcurrentMap<java.lang.String, java.time.ZoneOffset> r0 = ID_CACHE
            java.lang.Object r0 = r0.get(r7)
            java.time.ZoneOffset r0 = (java.time.ZoneOffset) r0
            if (r0 == 0) goto L_0x0011
            return r0
        L_0x0011:
            int r0 = r7.length()
            r1 = 2
            r2 = 1
            r3 = 0
            if (r0 == r1) goto L_0x006c
            r1 = 3
            if (r0 == r1) goto L_0x0088
            r4 = 5
            if (r0 == r4) goto L_0x0062
            r5 = 6
            r6 = 4
            if (r0 == r5) goto L_0x0059
            r5 = 7
            if (r0 == r5) goto L_0x004c
            r1 = 9
            if (r0 != r1) goto L_0x0038
            int r0 = parseNumber(r7, r2, r3)
            int r1 = parseNumber(r7, r6, r2)
            int r2 = parseNumber(r7, r5, r2)
            goto L_0x008e
        L_0x0038:
            java.time.DateTimeException r0 = new java.time.DateTimeException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Invalid ID for ZoneOffset, invalid format: "
            r1.<init>((java.lang.String) r2)
            r1.append((java.lang.String) r7)
            java.lang.String r7 = r1.toString()
            r0.<init>(r7)
            throw r0
        L_0x004c:
            int r0 = parseNumber(r7, r2, r3)
            int r1 = parseNumber(r7, r1, r3)
            int r2 = parseNumber(r7, r4, r3)
            goto L_0x008e
        L_0x0059:
            int r0 = parseNumber(r7, r2, r3)
            int r1 = parseNumber(r7, r6, r2)
            goto L_0x006a
        L_0x0062:
            int r0 = parseNumber(r7, r2, r3)
            int r1 = parseNumber(r7, r1, r3)
        L_0x006a:
            r2 = r3
            goto L_0x008e
        L_0x006c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            char r1 = r7.charAt(r3)
            r0.append((char) r1)
            java.lang.String r1 = "0"
            r0.append((java.lang.String) r1)
            char r7 = r7.charAt(r2)
            r0.append((char) r7)
            java.lang.String r7 = r0.toString()
        L_0x0088:
            int r0 = parseNumber(r7, r2, r3)
            r1 = r3
            r2 = r1
        L_0x008e:
            char r3 = r7.charAt(r3)
            r4 = 43
            r5 = 45
            if (r3 == r4) goto L_0x00af
            if (r3 != r5) goto L_0x009b
            goto L_0x00af
        L_0x009b:
            java.time.DateTimeException r0 = new java.time.DateTimeException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Invalid ID for ZoneOffset, plus/minus not found when expected: "
            r1.<init>((java.lang.String) r2)
            r1.append((java.lang.String) r7)
            java.lang.String r7 = r1.toString()
            r0.<init>(r7)
            throw r0
        L_0x00af:
            if (r3 != r5) goto L_0x00b9
            int r7 = -r0
            int r0 = -r1
            int r1 = -r2
            java.time.ZoneOffset r7 = ofHoursMinutesSeconds(r7, r0, r1)
            return r7
        L_0x00b9:
            java.time.ZoneOffset r7 = ofHoursMinutesSeconds(r0, r1, r2)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.ZoneOffset.m933of(java.lang.String):java.time.ZoneOffset");
    }

    private static int parseNumber(CharSequence charSequence, int i, boolean z) {
        if (!z || charSequence.charAt(i - 1) == ':') {
            char charAt = charSequence.charAt(i);
            char charAt2 = charSequence.charAt(i + 1);
            if (charAt >= '0' && charAt <= '9' && charAt2 >= '0' && charAt2 <= '9') {
                return ((charAt - '0') * 10) + (charAt2 - '0');
            }
            throw new DateTimeException("Invalid ID for ZoneOffset, non numeric characters found: " + charSequence);
        }
        throw new DateTimeException("Invalid ID for ZoneOffset, colon not found when expected: " + charSequence);
    }

    public static ZoneOffset ofHours(int i) {
        return ofHoursMinutesSeconds(i, 0, 0);
    }

    public static ZoneOffset ofHoursMinutes(int i, int i2) {
        return ofHoursMinutesSeconds(i, i2, 0);
    }

    public static ZoneOffset ofHoursMinutesSeconds(int i, int i2, int i3) {
        validate(i, i2, i3);
        return ofTotalSeconds(totalSeconds(i, i2, i3));
    }

    public static ZoneOffset from(TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        ZoneOffset zoneOffset = (ZoneOffset) temporalAccessor.query(TemporalQueries.offset());
        if (zoneOffset != null) {
            return zoneOffset;
        }
        throw new DateTimeException("Unable to obtain ZoneOffset from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName());
    }

    private static void validate(int i, int i2, int i3) {
        if (i < -18 || i > 18) {
            throw new DateTimeException("Zone offset hours not in valid range: value " + i + " is not in the range -18 to 18");
        }
        if (i > 0) {
            if (i2 < 0 || i3 < 0) {
                throw new DateTimeException("Zone offset minutes and seconds must be positive because hours is positive");
            }
        } else if (i < 0) {
            if (i2 > 0 || i3 > 0) {
                throw new DateTimeException("Zone offset minutes and seconds must be negative because hours is negative");
            }
        } else if ((i2 > 0 && i3 < 0) || (i2 < 0 && i3 > 0)) {
            throw new DateTimeException("Zone offset minutes and seconds must have the same sign");
        }
        if (i2 < -59 || i2 > 59) {
            throw new DateTimeException("Zone offset minutes not in valid range: value " + i2 + " is not in the range -59 to 59");
        } else if (i3 < -59 || i3 > 59) {
            throw new DateTimeException("Zone offset seconds not in valid range: value " + i3 + " is not in the range -59 to 59");
        } else if (Math.abs(i) == 18 && (i2 | i3) != 0) {
            throw new DateTimeException("Zone offset not in valid range: -18:00 to +18:00");
        }
    }

    public static ZoneOffset ofTotalSeconds(int i) {
        if (i < -64800 || i > MAX_SECONDS) {
            throw new DateTimeException("Zone offset not in valid range: -18:00 to +18:00");
        } else if (i % 900 != 0) {
            return new ZoneOffset(i);
        } else {
            Integer valueOf = Integer.valueOf(i);
            ConcurrentMap<Integer, ZoneOffset> concurrentMap = SECONDS_CACHE;
            ZoneOffset zoneOffset = concurrentMap.get(valueOf);
            if (zoneOffset != null) {
                return zoneOffset;
            }
            concurrentMap.putIfAbsent(valueOf, new ZoneOffset(i));
            ZoneOffset zoneOffset2 = concurrentMap.get(valueOf);
            ID_CACHE.putIfAbsent(zoneOffset2.getId(), zoneOffset2);
            return zoneOffset2;
        }
    }

    private ZoneOffset(int i) {
        this.totalSeconds = i;
        this.f377id = buildId(i);
    }

    private static String buildId(int i) {
        if (i == 0) {
            return "Z";
        }
        int abs = Math.abs(i);
        StringBuilder sb = new StringBuilder();
        int i2 = abs / 3600;
        int i3 = (abs / 60) % 60;
        sb.append(i < 0 ? LanguageTag.SEP : "+");
        sb.append(i2 < 10 ? "0" : "");
        sb.append(i2);
        String str = ":0";
        sb.append(i3 < 10 ? str : ":");
        sb.append(i3);
        int i4 = abs % 60;
        if (i4 != 0) {
            if (i4 >= 10) {
                str = ":";
            }
            sb.append(str);
            sb.append(i4);
        }
        return sb.toString();
    }

    public int getTotalSeconds() {
        return this.totalSeconds;
    }

    public String getId() {
        return this.f377id;
    }

    public ZoneRules getRules() {
        return ZoneRules.m960of(this);
    }

    public boolean isSupported(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.OFFSET_SECONDS) {
                return true;
            }
            return false;
        } else if (temporalField == null || !temporalField.isSupportedBy(this)) {
            return false;
        } else {
            return true;
        }
    }

    public ValueRange range(TemporalField temporalField) {
        return super.range(temporalField);
    }

    public int get(TemporalField temporalField) {
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return this.totalSeconds;
        }
        if (!(temporalField instanceof ChronoField)) {
            return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
        }
        throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return (long) this.totalSeconds;
        }
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        return (temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.zone()) ? this : super.query(temporalQuery);
    }

    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.OFFSET_SECONDS, (long) this.totalSeconds);
    }

    public int compareTo(ZoneOffset zoneOffset) {
        return zoneOffset.totalSeconds - this.totalSeconds;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ZoneOffset)) {
            return false;
        }
        if (this.totalSeconds == ((ZoneOffset) obj).totalSeconds) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.totalSeconds;
    }

    public String toString() {
        return this.f377id;
    }

    private Object writeReplace() {
        return new Ser((byte) 8, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    /* access modifiers changed from: package-private */
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(8);
        writeExternal(dataOutput);
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        int i = this.totalSeconds;
        int i2 = i % 900 == 0 ? i / 900 : 127;
        dataOutput.writeByte(i2);
        if (i2 == 127) {
            dataOutput.writeInt(i);
        }
    }

    static ZoneOffset readExternal(DataInput dataInput) throws IOException {
        byte readByte = dataInput.readByte();
        return readByte == Byte.MAX_VALUE ? ofTotalSeconds(dataInput.readInt()) : ofTotalSeconds(readByte * 900);
    }
}
