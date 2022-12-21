package java.time;

import androidx.exifinterface.media.ExifInterface;
import com.android.settingslib.utils.StringUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.time.DurationKt;
import sun.util.locale.LanguageTag;

public final class Duration implements TemporalAmount, Comparable<Duration>, Serializable {
    private static final BigInteger BI_NANOS_PER_SECOND = BigInteger.valueOf(1000000000);
    private static final Pattern PATTERN = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)D)?(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?", 2);
    public static final Duration ZERO = new Duration(0, 0);
    private static final long serialVersionUID = 3078945930695997490L;
    private final int nanos;
    private final long seconds;

    public static Duration ofDays(long j) {
        return create(Math.multiplyExact(j, (int) StringUtil.SECONDS_PER_DAY), 0);
    }

    public static Duration ofHours(long j) {
        return create(Math.multiplyExact(j, 3600), 0);
    }

    public static Duration ofMinutes(long j) {
        return create(Math.multiplyExact(j, 60), 0);
    }

    public static Duration ofSeconds(long j) {
        return create(j, 0);
    }

    public static Duration ofSeconds(long j, long j2) {
        return create(Math.addExact(j, Math.floorDiv(j2, 1000000000)), (int) Math.floorMod(j2, 1000000000));
    }

    public static Duration ofMillis(long j) {
        long j2 = j / 1000;
        int i = (int) (j % 1000);
        if (i < 0) {
            i += 1000;
            j2--;
        }
        return create(j2, i * DurationKt.NANOS_IN_MILLIS);
    }

    public static Duration ofNanos(long j) {
        long j2 = j / 1000000000;
        int i = (int) (j % 1000000000);
        if (i < 0) {
            i = (int) (((long) i) + 1000000000);
            j2--;
        }
        return create(j2, i);
    }

    /* renamed from: of */
    public static Duration m907of(long j, TemporalUnit temporalUnit) {
        return ZERO.plus(j, temporalUnit);
    }

    public static Duration from(TemporalAmount temporalAmount) {
        Objects.requireNonNull(temporalAmount, "amount");
        Duration duration = ZERO;
        for (TemporalUnit next : temporalAmount.getUnits()) {
            duration = duration.plus(temporalAmount.get(next), next);
        }
        return duration;
    }

    public static Duration parse(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        Objects.requireNonNull(charSequence2, "text");
        Matcher matcher = PATTERN.matcher(charSequence2);
        if (matcher.matches() && !ExifInterface.GPS_DIRECTION_TRUE.equals(matcher.group(3))) {
            int i = 1;
            boolean equals = LanguageTag.SEP.equals(matcher.group(1));
            String group = matcher.group(2);
            String group2 = matcher.group(4);
            String group3 = matcher.group(5);
            String group4 = matcher.group(6);
            String group5 = matcher.group(7);
            if (!(group == null && group2 == null && group3 == null && group4 == null)) {
                long parseNumber = parseNumber(charSequence2, group, StringUtil.SECONDS_PER_DAY, "days");
                long parseNumber2 = parseNumber(charSequence2, group2, 3600, "hours");
                long parseNumber3 = parseNumber(charSequence2, group3, 60, "minutes");
                long parseNumber4 = parseNumber(charSequence2, group4, 1, "seconds");
                if (parseNumber4 < 0) {
                    i = -1;
                }
                try {
                    return create(equals, parseNumber, parseNumber2, parseNumber3, parseNumber4, parseFraction(charSequence2, group5, i));
                } catch (ArithmeticException e) {
                    throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Duration: overflow", charSequence2, 0).initCause(e));
                }
            }
        }
        throw new DateTimeParseException("Text cannot be parsed to a Duration", charSequence2, 0);
    }

    private static long parseNumber(CharSequence charSequence, String str, int i, String str2) {
        if (str == null) {
            return 0;
        }
        try {
            return Math.multiplyExact(Long.parseLong(str), i);
        } catch (ArithmeticException | NumberFormatException e) {
            throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Duration: " + str2, charSequence, 0).initCause(e));
        }
    }

    private static int parseFraction(CharSequence charSequence, String str, int i) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        try {
            return Integer.parseInt((str + "000000000").substring(0, 9)) * i;
        } catch (ArithmeticException | NumberFormatException e) {
            throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Duration: fraction", charSequence, 0).initCause(e));
        }
    }

    private static Duration create(boolean z, long j, long j2, long j3, long j4, int i) {
        long addExact = Math.addExact(j, Math.addExact(j2, Math.addExact(j3, j4)));
        if (z) {
            return ofSeconds(addExact, (long) i).negated();
        }
        return ofSeconds(addExact, (long) i);
    }

    public static Duration between(Temporal temporal, Temporal temporal2) {
        try {
            return ofNanos(temporal.until(temporal2, ChronoUnit.NANOS));
        } catch (ArithmeticException | DateTimeException unused) {
            long until = temporal.until(temporal2, ChronoUnit.SECONDS);
            long j = 0;
            try {
                long j2 = temporal2.getLong(ChronoField.NANO_OF_SECOND) - temporal.getLong(ChronoField.NANO_OF_SECOND);
                int i = (until > 0 ? 1 : (until == 0 ? 0 : -1));
                if (i > 0 && j2 < 0) {
                    until++;
                } else if (i < 0 && j2 > 0) {
                    until--;
                }
                j = j2;
            } catch (DateTimeException unused2) {
            }
            return ofSeconds(until, j);
        }
    }

    private static Duration create(long j, int i) {
        if ((((long) i) | j) == 0) {
            return ZERO;
        }
        return new Duration(j, i);
    }

    private Duration(long j, int i) {
        this.seconds = j;
        this.nanos = i;
    }

    public long get(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.SECONDS) {
            return this.seconds;
        }
        if (temporalUnit == ChronoUnit.NANOS) {
            return (long) this.nanos;
        }
        throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
    }

    public List<TemporalUnit> getUnits() {
        return DurationUnits.UNITS;
    }

    private static class DurationUnits {
        static final List<TemporalUnit> UNITS = Collections.unmodifiableList(Arrays.asList(ChronoUnit.SECONDS, ChronoUnit.NANOS));

        private DurationUnits() {
        }
    }

    public boolean isZero() {
        return (this.seconds | ((long) this.nanos)) == 0;
    }

    public boolean isNegative() {
        return this.seconds < 0;
    }

    public long getSeconds() {
        return this.seconds;
    }

    public int getNano() {
        return this.nanos;
    }

    public Duration withSeconds(long j) {
        return create(j, this.nanos);
    }

    public Duration withNanos(int i) {
        ChronoField.NANO_OF_SECOND.checkValidIntValue((long) i);
        return create(this.seconds, i);
    }

    public Duration plus(Duration duration) {
        return plus(duration.getSeconds(), (long) duration.getNano());
    }

    public Duration plus(long j, TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporalUnit, "unit");
        if (temporalUnit == ChronoUnit.DAYS) {
            return plus(Math.multiplyExact(j, (int) StringUtil.SECONDS_PER_DAY), 0);
        }
        if (temporalUnit.isDurationEstimated()) {
            throw new UnsupportedTemporalTypeException("Unit must not have an estimated duration");
        } else if (j == 0) {
            return this;
        } else {
            if (temporalUnit instanceof ChronoUnit) {
                int i = C28491.$SwitchMap$java$time$temporal$ChronoUnit[((ChronoUnit) temporalUnit).ordinal()];
                if (i == 1) {
                    return plusNanos(j);
                }
                if (i == 2) {
                    return plusSeconds((j / 1000000000) * 1000).plusNanos((j % 1000000000) * 1000);
                }
                if (i == 3) {
                    return plusMillis(j);
                }
                if (i != 4) {
                    return plusSeconds(Math.multiplyExact(temporalUnit.getDuration().seconds, j));
                }
                return plusSeconds(j);
            }
            Duration multipliedBy = temporalUnit.getDuration().multipliedBy(j);
            return plusSeconds(multipliedBy.getSeconds()).plusNanos((long) multipliedBy.getNano());
        }
    }

    /* renamed from: java.time.Duration$1 */
    static /* synthetic */ class C28491 {
        static final /* synthetic */ int[] $SwitchMap$java$time$temporal$ChronoUnit;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                java.time.temporal.ChronoUnit[] r0 = java.time.temporal.ChronoUnit.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$time$temporal$ChronoUnit = r0
                java.time.temporal.ChronoUnit r1 = java.time.temporal.ChronoUnit.NANOS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x001d }
                java.time.temporal.ChronoUnit r1 = java.time.temporal.ChronoUnit.MICROS     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.time.temporal.ChronoUnit r1 = java.time.temporal.ChronoUnit.MILLIS     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$time$temporal$ChronoUnit     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.time.temporal.ChronoUnit r1 = java.time.temporal.ChronoUnit.SECONDS     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.Duration.C28491.<clinit>():void");
        }
    }

    public Duration plusDays(long j) {
        return plus(Math.multiplyExact(j, (int) StringUtil.SECONDS_PER_DAY), 0);
    }

    public Duration plusHours(long j) {
        return plus(Math.multiplyExact(j, 3600), 0);
    }

    public Duration plusMinutes(long j) {
        return plus(Math.multiplyExact(j, 60), 0);
    }

    public Duration plusSeconds(long j) {
        return plus(j, 0);
    }

    public Duration plusMillis(long j) {
        return plus(j / 1000, (j % 1000) * 1000000);
    }

    public Duration plusNanos(long j) {
        return plus(0, j);
    }

    private Duration plus(long j, long j2) {
        if ((j | j2) == 0) {
            return this;
        }
        return ofSeconds(Math.addExact(Math.addExact(this.seconds, j), j2 / 1000000000), ((long) this.nanos) + (j2 % 1000000000));
    }

    public Duration minus(Duration duration) {
        long seconds2 = duration.getSeconds();
        int nano = duration.getNano();
        if (seconds2 == Long.MIN_VALUE) {
            return plus(Long.MAX_VALUE, (long) (-nano)).plus(1, 0);
        }
        return plus(-seconds2, (long) (-nano));
    }

    public Duration minus(long j, TemporalUnit temporalUnit) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plus(Long.MAX_VALUE, temporalUnit);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plus(j2, temporalUnit);
    }

    public Duration minusDays(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusDays(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusDays(j2);
    }

    public Duration minusHours(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusHours(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusHours(j2);
    }

    public Duration minusMinutes(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusMinutes(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusMinutes(j2);
    }

    public Duration minusSeconds(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusSeconds(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusSeconds(j2);
    }

    public Duration minusMillis(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusMillis(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusMillis(j2);
    }

    public Duration minusNanos(long j) {
        long j2;
        if (j == Long.MIN_VALUE) {
            this = plusNanos(Long.MAX_VALUE);
            j2 = 1;
        } else {
            j2 = -j;
        }
        return this.plusNanos(j2);
    }

    public Duration multipliedBy(long j) {
        if (j == 0) {
            return ZERO;
        }
        if (j == 1) {
            return this;
        }
        return create(toBigDecimalSeconds().multiply(BigDecimal.valueOf(j)));
    }

    public Duration dividedBy(long j) {
        if (j == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        } else if (j == 1) {
            return this;
        } else {
            return create(toBigDecimalSeconds().divide(BigDecimal.valueOf(j), RoundingMode.DOWN));
        }
    }

    public long dividedBy(Duration duration) {
        Objects.requireNonNull(duration, "divisor");
        return toBigDecimalSeconds().divideToIntegralValue(duration.toBigDecimalSeconds()).longValueExact();
    }

    private BigDecimal toBigDecimalSeconds() {
        return BigDecimal.valueOf(this.seconds).add(BigDecimal.valueOf((long) this.nanos, 9));
    }

    private static Duration create(BigDecimal bigDecimal) {
        BigInteger bigIntegerExact = bigDecimal.movePointRight(9).toBigIntegerExact();
        BigInteger[] divideAndRemainder = bigIntegerExact.divideAndRemainder(BI_NANOS_PER_SECOND);
        if (divideAndRemainder[0].bitLength() <= 63) {
            return ofSeconds(divideAndRemainder[0].longValue(), (long) divideAndRemainder[1].intValue());
        }
        throw new ArithmeticException("Exceeds capacity of Duration: " + bigIntegerExact);
    }

    public Duration negated() {
        return multipliedBy(-1);
    }

    public Duration abs() {
        return isNegative() ? negated() : this;
    }

    public Temporal addTo(Temporal temporal) {
        long j = this.seconds;
        if (j != 0) {
            temporal = temporal.plus(j, ChronoUnit.SECONDS);
        }
        int i = this.nanos;
        return i != 0 ? temporal.plus((long) i, ChronoUnit.NANOS) : temporal;
    }

    public Temporal subtractFrom(Temporal temporal) {
        long j = this.seconds;
        if (j != 0) {
            temporal = temporal.minus(j, ChronoUnit.SECONDS);
        }
        int i = this.nanos;
        return i != 0 ? temporal.minus((long) i, ChronoUnit.NANOS) : temporal;
    }

    public long toDays() {
        return this.seconds / 86400;
    }

    public long toHours() {
        return this.seconds / 3600;
    }

    public long toMinutes() {
        return this.seconds / 60;
    }

    public long toSeconds() {
        return this.seconds;
    }

    public long toMillis() {
        return Math.addExact(Math.multiplyExact(this.seconds, 1000), (long) (this.nanos / DurationKt.NANOS_IN_MILLIS));
    }

    public long toNanos() {
        return Math.addExact(Math.multiplyExact(this.seconds, 1000000000), (long) this.nanos);
    }

    public long toDaysPart() {
        return this.seconds / 86400;
    }

    public int toHoursPart() {
        return (int) (toHours() % 24);
    }

    public int toMinutesPart() {
        return (int) (toMinutes() % 60);
    }

    public int toSecondsPart() {
        return (int) (this.seconds % 60);
    }

    public int toMillisPart() {
        return this.nanos / DurationKt.NANOS_IN_MILLIS;
    }

    public int toNanosPart() {
        return this.nanos;
    }

    public Duration truncatedTo(TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporalUnit, "unit");
        if (temporalUnit == ChronoUnit.SECONDS) {
            long j = this.seconds;
            if (j >= 0 || this.nanos == 0) {
                return new Duration(j, 0);
            }
        }
        if (temporalUnit == ChronoUnit.NANOS) {
            return this;
        }
        Duration duration = temporalUnit.getDuration();
        if (duration.getSeconds() <= 86400) {
            long nanos2 = duration.toNanos();
            if (86400000000000L % nanos2 == 0) {
                long j2 = ((this.seconds % 86400) * 1000000000) + ((long) this.nanos);
                return plusNanos(((j2 / nanos2) * nanos2) - j2);
            }
            throw new UnsupportedTemporalTypeException("Unit must divide into a standard day without remainder");
        }
        throw new UnsupportedTemporalTypeException("Unit is too large to be used for truncation");
    }

    public int compareTo(Duration duration) {
        int compare = Long.compare(this.seconds, duration.seconds);
        if (compare != 0) {
            return compare;
        }
        return this.nanos - duration.nanos;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Duration)) {
            return false;
        }
        Duration duration = (Duration) obj;
        if (this.seconds == duration.seconds && this.nanos == duration.nanos) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long j = this.seconds;
        return ((int) (j ^ (j >>> 32))) + (this.nanos * 51);
    }

    public String toString() {
        if (this == ZERO) {
            return "PT0S";
        }
        long j = this.seconds;
        long j2 = j / 3600;
        int i = (int) ((j % 3600) / 60);
        int i2 = (int) (j % 60);
        StringBuilder sb = new StringBuilder(24);
        sb.append("PT");
        if (j2 != 0) {
            sb.append(j2);
            sb.append('H');
        }
        if (i != 0) {
            sb.append(i);
            sb.append('M');
        }
        if (i2 == 0 && this.nanos == 0 && sb.length() > 2) {
            return sb.toString();
        }
        if (i2 >= 0 || this.nanos <= 0) {
            sb.append(i2);
        } else if (i2 == -1) {
            sb.append("-0");
        } else {
            sb.append(i2 + 1);
        }
        if (this.nanos > 0) {
            int length = sb.length();
            if (i2 < 0) {
                sb.append(2000000000 - ((long) this.nanos));
            } else {
                sb.append(((long) this.nanos) + 1000000000);
            }
            while (sb.charAt(sb.length() - 1) == '0') {
                sb.setLength(sb.length() - 1);
            }
            sb.setCharAt(length, '.');
        }
        sb.append('S');
        return sb.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 1, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    /* access modifiers changed from: package-private */
    public void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(this.seconds);
        dataOutput.writeInt(this.nanos);
    }

    static Duration readExternal(DataInput dataInput) throws IOException {
        return ofSeconds(dataInput.readLong(), (long) dataInput.readInt());
    }
}
