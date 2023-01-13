package java.nio.file.attribute;

import com.android.settingslib.accessibility.AccessibilityUtils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import sun.util.locale.LanguageTag;

public final class FileTime implements Comparable<FileTime> {
    private static final long DAYS_PER_10000_YEARS = 3652425;
    private static final long HOURS_PER_DAY = 24;
    private static final long MAX_SECOND = 31556889864403199L;
    private static final long MICROS_PER_SECOND = 1000000;
    private static final long MILLIS_PER_SECOND = 1000;
    private static final long MINUTES_PER_HOUR = 60;
    private static final long MIN_SECOND = -31557014167219200L;
    private static final int NANOS_PER_MICRO = 1000;
    private static final int NANOS_PER_MILLI = 1000000;
    private static final long NANOS_PER_SECOND = 1000000000;
    private static final long SECONDS_0000_TO_1970 = 62167219200L;
    private static final long SECONDS_PER_10000_YEARS = 315569520000L;
    private static final long SECONDS_PER_DAY = 86400;
    private static final long SECONDS_PER_HOUR = 3600;
    private static final long SECONDS_PER_MINUTE = 60;
    private Instant instant;
    private final TimeUnit unit;
    private final long value;
    private String valueAsString;

    private static long scale(long j, long j2, long j3) {
        if (j > j3) {
            return Long.MAX_VALUE;
        }
        if (j < (-j3)) {
            return Long.MIN_VALUE;
        }
        return j * j2;
    }

    private FileTime(long j, TimeUnit timeUnit, Instant instant2) {
        this.value = j;
        this.unit = timeUnit;
        this.instant = instant2;
    }

    public static FileTime from(long j, TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit, "unit");
        return new FileTime(j, timeUnit, (Instant) null);
    }

    public static FileTime fromMillis(long j) {
        return new FileTime(j, TimeUnit.MILLISECONDS, (Instant) null);
    }

    public static FileTime from(Instant instant2) {
        Objects.requireNonNull(instant2, "instant");
        return new FileTime(0, (TimeUnit) null, instant2);
    }

    /* renamed from: to */
    public long mo61354to(TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit, "unit");
        TimeUnit timeUnit2 = this.unit;
        if (timeUnit2 != null) {
            return timeUnit.convert(this.value, timeUnit2);
        }
        long convert = timeUnit.convert(this.instant.getEpochSecond(), TimeUnit.SECONDS);
        if (convert == Long.MIN_VALUE || convert == Long.MAX_VALUE) {
            return convert;
        }
        long convert2 = timeUnit.convert((long) this.instant.getNano(), TimeUnit.NANOSECONDS);
        long j = convert + convert2;
        if (((convert2 ^ j) & (convert ^ j)) >= 0) {
            return j;
        }
        if (convert < 0) {
            return Long.MIN_VALUE;
        }
        return Long.MAX_VALUE;
    }

    public long toMillis() {
        TimeUnit timeUnit = this.unit;
        if (timeUnit != null) {
            return timeUnit.toMillis(this.value);
        }
        long epochSecond = this.instant.getEpochSecond();
        int nano = this.instant.getNano();
        long j = epochSecond * 1000;
        if (((Math.abs(epochSecond) | 1000) >>> 31) == 0 || j / 1000 == epochSecond) {
            return j + ((long) (nano / 1000000));
        }
        return epochSecond < 0 ? Long.MIN_VALUE : Long.MAX_VALUE;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x007f, code lost:
        r10 = r2;
        r2 = 0;
        r0 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0089, code lost:
        if (r0 > MIN_SECOND) goto L_0x0090;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x008b, code lost:
        r12.instant = java.time.Instant.MIN;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0097, code lost:
        if (r0 < MAX_SECOND) goto L_0x009e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0099, code lost:
        r12.instant = java.time.Instant.MAX;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x009e, code lost:
        r12.instant = java.time.Instant.ofEpochSecond(r0, (long) r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.time.Instant toInstant() {
        /*
            r12 = this;
            java.time.Instant r0 = r12.instant
            if (r0 != 0) goto L_0x00a5
            int[] r0 = java.nio.file.attribute.FileTime.C43771.$SwitchMap$java$util$concurrent$TimeUnit
            java.util.concurrent.TimeUnit r1 = r12.unit
            int r1 = r1.ordinal()
            r0 = r0[r1]
            r1 = 0
            switch(r0) {
                case 1: goto L_0x0071;
                case 2: goto L_0x0063;
                case 3: goto L_0x0055;
                case 4: goto L_0x0052;
                case 5: goto L_0x003e;
                case 6: goto L_0x002b;
                case 7: goto L_0x001a;
                default: goto L_0x0012;
            }
        L_0x0012:
            java.lang.AssertionError r12 = new java.lang.AssertionError
            java.lang.String r0 = "Unit not handled"
            r12.<init>((java.lang.Object) r0)
            throw r12
        L_0x001a:
            long r0 = r12.value
            r2 = 1000000000(0x3b9aca00, double:4.94065646E-315)
            long r0 = java.lang.Math.floorDiv((long) r0, (long) r2)
            long r4 = r12.value
            long r2 = java.lang.Math.floorMod((long) r4, (long) r2)
            int r2 = (int) r2
            goto L_0x0082
        L_0x002b:
            long r0 = r12.value
            r2 = 1000000(0xf4240, double:4.940656E-318)
            long r0 = java.lang.Math.floorDiv((long) r0, (long) r2)
            long r4 = r12.value
            long r2 = java.lang.Math.floorMod((long) r4, (long) r2)
            int r2 = (int) r2
            int r2 = r2 * 1000
            goto L_0x0082
        L_0x003e:
            long r0 = r12.value
            r2 = 1000(0x3e8, double:4.94E-321)
            long r0 = java.lang.Math.floorDiv((long) r0, (long) r2)
            long r4 = r12.value
            long r2 = java.lang.Math.floorMod((long) r4, (long) r2)
            int r2 = (int) r2
            r3 = 1000000(0xf4240, float:1.401298E-39)
            int r2 = r2 * r3
            goto L_0x0082
        L_0x0052:
            long r2 = r12.value
            goto L_0x007f
        L_0x0055:
            long r4 = r12.value
            r6 = 60
            r8 = 153722867280912930(0x222222222222222, double:2.166167076120538E-298)
            long r2 = scale(r4, r6, r8)
            goto L_0x007f
        L_0x0063:
            long r4 = r12.value
            r6 = 3600(0xe10, double:1.7786E-320)
            r8 = 2562047788015215(0x91a2b3c4d5e6f, double:1.2658197950618743E-308)
            long r2 = scale(r4, r6, r8)
            goto L_0x007f
        L_0x0071:
            long r4 = r12.value
            r6 = 86400(0x15180, double:4.26873E-319)
            r8 = 106751991167300(0x611722833944, double:5.2742491460911E-310)
            long r2 = scale(r4, r6, r8)
        L_0x007f:
            r10 = r2
            r2 = r1
            r0 = r10
        L_0x0082:
            r3 = -31557014167219200(0xff8fe31014641400, double:-2.7989734602046733E306)
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 > 0) goto L_0x0090
            java.time.Instant r0 = java.time.Instant.MIN
            r12.instant = r0
            goto L_0x00a5
        L_0x0090:
            r3 = 31556889864403199(0x701cd2fa9578ff, double:1.434068493154717E-306)
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 < 0) goto L_0x009e
            java.time.Instant r0 = java.time.Instant.MAX
            r12.instant = r0
            goto L_0x00a5
        L_0x009e:
            long r2 = (long) r2
            java.time.Instant r0 = java.time.Instant.ofEpochSecond(r0, r2)
            r12.instant = r0
        L_0x00a5:
            java.time.Instant r12 = r12.instant
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.file.attribute.FileTime.toInstant():java.time.Instant");
    }

    /* renamed from: java.nio.file.attribute.FileTime$1 */
    static /* synthetic */ class C43771 {
        static final /* synthetic */ int[] $SwitchMap$java$util$concurrent$TimeUnit;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                java.util.concurrent.TimeUnit[] r0 = java.util.concurrent.TimeUnit.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$util$concurrent$TimeUnit = r0
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.DAYS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x001d }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.HOURS     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.MINUTES     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x003e }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.MICROSECONDS     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x0054 }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.NANOSECONDS     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.nio.file.attribute.FileTime.C43771.<clinit>():void");
        }
    }

    public boolean equals(Object obj) {
        return (obj instanceof FileTime) && compareTo((FileTime) obj) == 0;
    }

    public int hashCode() {
        return toInstant().hashCode();
    }

    private long toDays() {
        TimeUnit timeUnit = this.unit;
        if (timeUnit != null) {
            return timeUnit.toDays(this.value);
        }
        return TimeUnit.SECONDS.toDays(toInstant().getEpochSecond());
    }

    private long toExcessNanos(long j) {
        TimeUnit timeUnit = this.unit;
        if (timeUnit != null) {
            return timeUnit.toNanos(this.value - timeUnit.convert(j, TimeUnit.DAYS));
        }
        return TimeUnit.SECONDS.toNanos(toInstant().getEpochSecond() - TimeUnit.DAYS.toSeconds(j));
    }

    public int compareTo(FileTime fileTime) {
        TimeUnit timeUnit = this.unit;
        if (timeUnit != null && timeUnit == fileTime.unit) {
            return Long.compare(this.value, fileTime.value);
        }
        long epochSecond = toInstant().getEpochSecond();
        int compare = Long.compare(epochSecond, fileTime.toInstant().getEpochSecond());
        if (compare != 0) {
            return compare;
        }
        int compare2 = Long.compare((long) toInstant().getNano(), (long) fileTime.toInstant().getNano());
        if (compare2 != 0) {
            return compare2;
        }
        if (epochSecond != MAX_SECOND && epochSecond != MIN_SECOND) {
            return 0;
        }
        long days = toDays();
        long days2 = fileTime.toDays();
        if (days == days2) {
            return Long.compare(toExcessNanos(days), fileTime.toExcessNanos(days2));
        }
        return Long.compare(days, days2);
    }

    private StringBuilder append(StringBuilder sb, int i, int i2) {
        while (i > 0) {
            sb.append((char) ((i2 / i) + 48));
            i2 %= i;
            i /= 10;
        }
        return sb;
    }

    public String toString() {
        int i;
        long j;
        long j2;
        int i2;
        LocalDateTime localDateTime;
        if (this.valueAsString == null) {
            if (this.instant != null || this.unit.compareTo(TimeUnit.SECONDS) < 0) {
                j = toInstant().getEpochSecond();
                i = toInstant().getNano();
            } else {
                j = this.unit.toSeconds(this.value);
                i = 0;
            }
            if (j >= -62167219200L) {
                long j3 = (j - SECONDS_PER_10000_YEARS) + SECONDS_0000_TO_1970;
                j2 = Math.floorDiv(j3, (long) SECONDS_PER_10000_YEARS) + 1;
                localDateTime = LocalDateTime.ofEpochSecond(Math.floorMod(j3, (long) SECONDS_PER_10000_YEARS) - SECONDS_0000_TO_1970, i, ZoneOffset.UTC);
                i2 = localDateTime.getYear();
            } else {
                long j4 = j + SECONDS_0000_TO_1970;
                j2 = j4 / SECONDS_PER_10000_YEARS;
                localDateTime = LocalDateTime.ofEpochSecond((j4 % SECONDS_PER_10000_YEARS) - SECONDS_0000_TO_1970, i, ZoneOffset.UTC);
                i2 = localDateTime.getYear();
            }
            int i3 = i2 + (((int) j2) * 10000);
            if (i3 <= 0) {
                i3--;
            }
            int nano = localDateTime.getNano();
            StringBuilder sb = new StringBuilder(64);
            sb.append(i3 < 0 ? LanguageTag.SEP : "");
            int abs = Math.abs(i3);
            if (abs < 10000) {
                append(sb, 1000, Math.abs(abs));
            } else {
                sb.append(String.valueOf(abs));
            }
            sb.append('-');
            append(sb, 10, localDateTime.getMonthValue());
            sb.append('-');
            append(sb, 10, localDateTime.getDayOfMonth());
            sb.append('T');
            append(sb, 10, localDateTime.getHour());
            sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            append(sb, 10, localDateTime.getMinute());
            sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            append(sb, 10, localDateTime.getSecond());
            if (nano != 0) {
                sb.append('.');
                int i4 = 100000000;
                while (nano % 10 == 0) {
                    nano /= 10;
                    i4 /= 10;
                }
                append(sb, i4, nano);
            }
            sb.append('Z');
            this.valueAsString = sb.toString();
        }
        return this.valueAsString;
    }
}
