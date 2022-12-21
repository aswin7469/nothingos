package java.util.concurrent;

import com.airbnb.lottie.utils.Utils;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public enum TimeUnit {
    NANOSECONDS(1),
    MICROSECONDS(1000),
    MILLISECONDS(MILLI_SCALE),
    SECONDS(SECOND_SCALE),
    MINUTES(MINUTE_SCALE),
    HOURS(HOUR_SCALE),
    DAYS(DAY_SCALE);
    
    private static final long DAY_SCALE = 86400000000000L;
    private static final long HOUR_SCALE = 3600000000000L;
    private static final long MICRO_SCALE = 1000;
    private static final long MILLI_SCALE = 1000000;
    private static final long MINUTE_SCALE = 60000000000L;
    private static final long NANO_SCALE = 1;
    private static final long SECOND_SCALE = 1000000000;
    private final long maxMicros;
    private final long maxMillis;
    private final long maxNanos;
    private final long maxSecs;
    private final long microRatio;
    private final int milliRatio;
    private final long scale;
    private final int secRatio;

    private TimeUnit(long j) {
        this.scale = j;
        this.maxNanos = Long.MAX_VALUE / j;
        long j2 = j >= 1000 ? j / 1000 : 1000 / j;
        this.microRatio = j2;
        this.maxMicros = Long.MAX_VALUE / j2;
        long j3 = j >= MILLI_SCALE ? j / MILLI_SCALE : MILLI_SCALE / j;
        this.milliRatio = (int) j3;
        this.maxMillis = Long.MAX_VALUE / j3;
        long j4 = j >= SECOND_SCALE ? j / SECOND_SCALE : SECOND_SCALE / j;
        this.secRatio = (int) j4;
        this.maxSecs = Long.MAX_VALUE / j4;
    }

    private static long cvt(long j, long j2, long j3) {
        int i = (j3 > j2 ? 1 : (j3 == j2 ? 0 : -1));
        if (i == 0) {
            return j;
        }
        if (i < 0) {
            return j / (j2 / j3);
        }
        long j4 = j3 / j2;
        long j5 = Long.MAX_VALUE / j4;
        if (j > j5) {
            return Long.MAX_VALUE;
        }
        if (j < (-j5)) {
            return Long.MIN_VALUE;
        }
        return j * j4;
    }

    public long convert(long j, TimeUnit timeUnit) {
        int i = C44111.$SwitchMap$java$util$concurrent$TimeUnit[ordinal()];
        if (i == 1) {
            return timeUnit.toNanos(j);
        }
        if (i == 2) {
            return timeUnit.toMicros(j);
        }
        if (i == 3) {
            return timeUnit.toMillis(j);
        }
        if (i == 4) {
            return timeUnit.toSeconds(j);
        }
        return cvt(j, this.scale, timeUnit.scale);
    }

    public long convert(Duration duration) {
        long j;
        long seconds = duration.getSeconds();
        int nano = duration.getNano();
        if (seconds < 0 && nano > 0) {
            seconds++;
            nano -= Utils.SECOND_IN_NANOS;
        }
        if (this == NANOSECONDS) {
            j = (long) nano;
        } else {
            long j2 = this.scale;
            if (j2 < SECOND_SCALE) {
                j = ((long) nano) / j2;
            } else if (this == SECONDS) {
                return seconds;
            } else {
                return seconds / ((long) this.secRatio);
            }
        }
        long j3 = (((long) this.secRatio) * seconds) + j;
        long j4 = this.maxSecs;
        if (seconds < j4 && seconds > (-j4)) {
            return j3;
        }
        if (seconds == j4 && j3 > 0) {
            return j3;
        }
        if (seconds != (-j4) || j3 >= 0) {
            return seconds > 0 ? Long.MAX_VALUE : Long.MIN_VALUE;
        }
        return j3;
    }

    public long toNanos(long j) {
        long j2 = this.scale;
        if (j2 == 1) {
            return j;
        }
        long j3 = this.maxNanos;
        if (j > j3) {
            return Long.MAX_VALUE;
        }
        if (j < (-j3)) {
            return Long.MIN_VALUE;
        }
        return j * j2;
    }

    public long toMicros(long j) {
        int i = (this.scale > 1000 ? 1 : (this.scale == 1000 ? 0 : -1));
        if (i <= 0) {
            return i == 0 ? j : j / this.microRatio;
        }
        long j2 = this.maxMicros;
        if (j > j2) {
            return Long.MAX_VALUE;
        }
        if (j < (-j2)) {
            return Long.MIN_VALUE;
        }
        return j * this.microRatio;
    }

    public long toMillis(long j) {
        int i = (this.scale > MILLI_SCALE ? 1 : (this.scale == MILLI_SCALE ? 0 : -1));
        if (i <= 0) {
            return i == 0 ? j : j / ((long) this.milliRatio);
        }
        long j2 = this.maxMillis;
        if (j > j2) {
            return Long.MAX_VALUE;
        }
        if (j < (-j2)) {
            return Long.MIN_VALUE;
        }
        return j * ((long) this.milliRatio);
    }

    public long toSeconds(long j) {
        int i = (this.scale > SECOND_SCALE ? 1 : (this.scale == SECOND_SCALE ? 0 : -1));
        if (i <= 0) {
            return i == 0 ? j : j / ((long) this.secRatio);
        }
        long j2 = this.maxSecs;
        if (j > j2) {
            return Long.MAX_VALUE;
        }
        if (j < (-j2)) {
            return Long.MIN_VALUE;
        }
        return j * ((long) this.secRatio);
    }

    public long toMinutes(long j) {
        return cvt(j, MINUTE_SCALE, this.scale);
    }

    public long toHours(long j) {
        return cvt(j, HOUR_SCALE, this.scale);
    }

    public long toDays(long j) {
        return cvt(j, DAY_SCALE, this.scale);
    }

    private int excessNanos(long j, long j2) {
        long j3 = this.scale;
        if (j3 != 1) {
            if (j3 != 1000) {
                return 0;
            }
            j *= 1000;
        }
        return (int) (j - (j2 * MILLI_SCALE));
    }

    public void timedWait(Object obj, long j) throws InterruptedException {
        if (j > 0) {
            long millis = toMillis(j);
            obj.wait(millis, excessNanos(j, millis));
        }
    }

    public void timedJoin(Thread thread, long j) throws InterruptedException {
        if (j > 0) {
            long millis = toMillis(j);
            thread.join(millis, excessNanos(j, millis));
        }
    }

    public void sleep(long j) throws InterruptedException {
        if (j > 0) {
            long millis = toMillis(j);
            Thread.sleep(millis, excessNanos(j, millis));
        }
    }

    public ChronoUnit toChronoUnit() {
        switch (C44111.$SwitchMap$java$util$concurrent$TimeUnit[ordinal()]) {
            case 1:
                return ChronoUnit.NANOS;
            case 2:
                return ChronoUnit.MICROS;
            case 3:
                return ChronoUnit.MILLIS;
            case 4:
                return ChronoUnit.SECONDS;
            case 5:
                return ChronoUnit.MINUTES;
            case 6:
                return ChronoUnit.HOURS;
            case 7:
                return ChronoUnit.DAYS;
            default:
                throw new AssertionError();
        }
    }

    /* renamed from: java.util.concurrent.TimeUnit$1 */
    static /* synthetic */ class C44111 {
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$ChronoUnit = null;
        static final /* synthetic */ int[] $SwitchMap$java$util$concurrent$TimeUnit = null;

        /* JADX WARNING: Can't wrap try/catch for region: R(30:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|(2:17|18)|19|21|22|23|(2:25|26)|27|29|30|31|32|33|34|35|36|37|38|39|40|(3:41|42|44)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(34:0|(2:1|2)|3|5|6|7|9|10|11|(2:13|14)|15|17|18|19|21|22|23|(2:25|26)|27|29|30|31|32|33|34|35|36|37|38|39|40|41|42|44) */
        /* JADX WARNING: Can't wrap try/catch for region: R(37:0|1|2|3|5|6|7|9|10|11|13|14|15|17|18|19|21|22|23|25|26|27|29|30|31|32|33|34|35|36|37|38|39|40|41|42|44) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0065 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x006f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x0079 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x0083 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x008d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x0097 */
        static {
            /*
                java.time.temporal.ChronoUnit[] r0 = java.time.temporal.ChronoUnit.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$time$temporal$ChronoUnit = r0
                r1 = 1
                java.time.temporal.ChronoUnit r2 = java.time.temporal.ChronoUnit.NANOS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x001d }
                java.time.temporal.ChronoUnit r3 = java.time.temporal.ChronoUnit.MICROS     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.time.temporal.ChronoUnit r4 = java.time.temporal.ChronoUnit.MILLIS     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                r3 = 4
                int[] r4 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.time.temporal.ChronoUnit r5 = java.time.temporal.ChronoUnit.SECONDS     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                r4 = 5
                int[] r5 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x003e }
                java.time.temporal.ChronoUnit r6 = java.time.temporal.ChronoUnit.MINUTES     // Catch:{ NoSuchFieldError -> 0x003e }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r5[r6] = r4     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                r5 = 6
                int[] r6 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.time.temporal.ChronoUnit r7 = java.time.temporal.ChronoUnit.HOURS     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r7 = r7.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r6[r7] = r5     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                r6 = 7
                int[] r7 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0054 }
                java.time.temporal.ChronoUnit r8 = java.time.temporal.ChronoUnit.DAYS     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r8 = r8.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r7[r8] = r6     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                java.util.concurrent.TimeUnit[] r7 = java.util.concurrent.TimeUnit.values()
                int r7 = r7.length
                int[] r7 = new int[r7]
                $SwitchMap$java$util$concurrent$TimeUnit = r7
                java.util.concurrent.TimeUnit r8 = java.util.concurrent.TimeUnit.NANOSECONDS     // Catch:{ NoSuchFieldError -> 0x0065 }
                int r8 = r8.ordinal()     // Catch:{ NoSuchFieldError -> 0x0065 }
                r7[r8] = r1     // Catch:{ NoSuchFieldError -> 0x0065 }
            L_0x0065:
                int[] r1 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x006f }
                java.util.concurrent.TimeUnit r7 = java.util.concurrent.TimeUnit.MICROSECONDS     // Catch:{ NoSuchFieldError -> 0x006f }
                int r7 = r7.ordinal()     // Catch:{ NoSuchFieldError -> 0x006f }
                r1[r7] = r0     // Catch:{ NoSuchFieldError -> 0x006f }
            L_0x006f:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x0079 }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ NoSuchFieldError -> 0x0079 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0079 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0079 }
            L_0x0079:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x0083 }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ NoSuchFieldError -> 0x0083 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0083 }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x0083 }
            L_0x0083:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x008d }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.MINUTES     // Catch:{ NoSuchFieldError -> 0x008d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x008d }
                r0[r1] = r4     // Catch:{ NoSuchFieldError -> 0x008d }
            L_0x008d:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x0097 }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.HOURS     // Catch:{ NoSuchFieldError -> 0x0097 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0097 }
                r0[r1] = r5     // Catch:{ NoSuchFieldError -> 0x0097 }
            L_0x0097:
                int[] r0 = $SwitchMap$java$util$concurrent$TimeUnit     // Catch:{ NoSuchFieldError -> 0x00a1 }
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.DAYS     // Catch:{ NoSuchFieldError -> 0x00a1 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a1 }
                r0[r1] = r6     // Catch:{ NoSuchFieldError -> 0x00a1 }
            L_0x00a1:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.TimeUnit.C44111.<clinit>():void");
        }
    }

    /* renamed from: of */
    public static TimeUnit m1763of(ChronoUnit chronoUnit) {
        switch (C44111.$SwitchMap$java$time$temporal$ChronoUnit[((ChronoUnit) Objects.requireNonNull(chronoUnit, "chronoUnit")).ordinal()]) {
            case 1:
                return NANOSECONDS;
            case 2:
                return MICROSECONDS;
            case 3:
                return MILLISECONDS;
            case 4:
                return SECONDS;
            case 5:
                return MINUTES;
            case 6:
                return HOURS;
            case 7:
                return DAYS;
            default:
                throw new IllegalArgumentException("No TimeUnit equivalent for " + chronoUnit);
        }
    }
}
