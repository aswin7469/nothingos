package java.time.chrono;

import android.net.wifi.WifiEnterpriseConfig;
import androidx.exifinterface.media.ExifInterface;
import com.android.systemui.demomode.DemoMode;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class IsoChronology extends AbstractChronology implements Serializable {
    private static final long DAYS_0000_TO_1970 = 719528;
    public static final IsoChronology INSTANCE = new IsoChronology();
    private static final long serialVersionUID = -1440403870442975015L;

    public String getCalendarType() {
        return "iso8601";
    }

    public String getId() {
        return ExifInterface.TAG_RW2_ISO;
    }

    private IsoChronology() {
    }

    public LocalDate date(Era era, int i, int i2, int i3) {
        return date(prolepticYear(era, i), i2, i3);
    }

    public LocalDate date(int i, int i2, int i3) {
        return LocalDate.m906of(i, i2, i3);
    }

    public LocalDate dateYearDay(Era era, int i, int i2) {
        return dateYearDay(prolepticYear(era, i), i2);
    }

    public LocalDate dateYearDay(int i, int i2) {
        return LocalDate.ofYearDay(i, i2);
    }

    public LocalDate dateEpochDay(long j) {
        return LocalDate.ofEpochDay(j);
    }

    public LocalDate date(TemporalAccessor temporalAccessor) {
        return LocalDate.from(temporalAccessor);
    }

    public long epochSecond(int i, int i2, int i3, int i4, int i5, int i6, ZoneOffset zoneOffset) {
        long j;
        int i7 = i;
        int i8 = i2;
        int i9 = i3;
        int i10 = i4;
        int i11 = i5;
        int i12 = i6;
        long j2 = (long) i7;
        ChronoField.YEAR.checkValidValue(j2);
        ChronoField.MONTH_OF_YEAR.checkValidValue((long) i8);
        ChronoField.DAY_OF_MONTH.checkValidValue((long) i9);
        ChronoField.HOUR_OF_DAY.checkValidValue((long) i10);
        ChronoField.MINUTE_OF_HOUR.checkValidValue((long) i11);
        ChronoField.SECOND_OF_MINUTE.checkValidValue((long) i12);
        Objects.requireNonNull(zoneOffset, "zoneOffset");
        if (i9 <= 28 || i9 <= numberOfDaysOfMonth(i, i2)) {
            long j3 = (365 * j2) + 0;
            if (i7 >= 0) {
                j = j3 + (((3 + j2) / 4) - ((99 + j2) / 100)) + ((399 + j2) / 400);
            } else {
                j = j3 - ((long) (((i7 / -4) - (i7 / -100)) + (i7 / -400)));
            }
            long j4 = j + ((long) (((i8 * 367) - 362) / 12)) + ((long) (i9 - 1));
            if (i8 > 2) {
                j4--;
                if (!INSTANCE.isLeapYear(j2)) {
                    j4--;
                }
            }
            return Math.addExact(Math.multiplyExact(j4 - DAYS_0000_TO_1970, 86400), (long) (((((i10 * 60) + i11) * 60) + i12) - zoneOffset.getTotalSeconds()));
        } else if (i9 == 29) {
            throw new DateTimeException("Invalid date 'February 29' as '" + i7 + "' is not a leap year");
        } else {
            throw new DateTimeException("Invalid date '" + Month.m918of(i2).name() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + i9 + "'");
        }
    }

    private int numberOfDaysOfMonth(int i, int i2) {
        return i2 != 2 ? (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) ? 30 : 31 : INSTANCE.isLeapYear((long) i) ? 29 : 28;
    }

    public LocalDateTime localDateTime(TemporalAccessor temporalAccessor) {
        return LocalDateTime.from(temporalAccessor);
    }

    public ZonedDateTime zonedDateTime(TemporalAccessor temporalAccessor) {
        return ZonedDateTime.from(temporalAccessor);
    }

    public ZonedDateTime zonedDateTime(Instant instant, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(instant, zoneId);
    }

    public LocalDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    public LocalDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    public LocalDate dateNow(Clock clock) {
        Objects.requireNonNull(clock, DemoMode.COMMAND_CLOCK);
        return date((TemporalAccessor) LocalDate.now(clock));
    }

    public boolean isLeapYear(long j) {
        return (3 & j) == 0 && (j % 100 != 0 || j % 400 == 0);
    }

    public int prolepticYear(Era era, int i) {
        if (era instanceof IsoEra) {
            return era == IsoEra.CE ? i : 1 - i;
        }
        throw new ClassCastException("Era must be IsoEra");
    }

    public IsoEra eraOf(int i) {
        return IsoEra.m943of(i);
    }

    public List<Era> eras() {
        return List.m1739of((E[]) IsoEra.values());
    }

    public LocalDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        return (LocalDate) super.resolveDate(map, resolverStyle);
    }

    /* access modifiers changed from: package-private */
    public void resolveProlepticMonth(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        Long remove = map.remove(ChronoField.PROLEPTIC_MONTH);
        if (remove != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.PROLEPTIC_MONTH.checkValidValue(remove.longValue());
            }
            addFieldValue(map, ChronoField.MONTH_OF_YEAR, (long) (Math.floorMod(remove.longValue(), 12) + 1));
            addFieldValue(map, ChronoField.YEAR, Math.floorDiv(remove.longValue(), 12));
        }
    }

    /* access modifiers changed from: package-private */
    public LocalDate resolveYearOfEra(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        Long remove = map.remove(ChronoField.YEAR_OF_ERA);
        if (remove != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.YEAR_OF_ERA.checkValidValue(remove.longValue());
            }
            Long remove2 = map.remove(ChronoField.ERA);
            if (remove2 == null) {
                Long l = map.get(ChronoField.YEAR);
                if (resolverStyle != ResolverStyle.STRICT) {
                    addFieldValue(map, ChronoField.YEAR, (l == null || l.longValue() > 0) ? remove.longValue() : Math.subtractExact(1, remove.longValue()));
                    return null;
                } else if (l != null) {
                    addFieldValue(map, ChronoField.YEAR, l.longValue() > 0 ? remove.longValue() : Math.subtractExact(1, remove.longValue()));
                    return null;
                } else {
                    map.put(ChronoField.YEAR_OF_ERA, remove);
                    return null;
                }
            } else if (remove2.longValue() == 1) {
                addFieldValue(map, ChronoField.YEAR, remove.longValue());
                return null;
            } else if (remove2.longValue() == 0) {
                addFieldValue(map, ChronoField.YEAR, Math.subtractExact(1, remove.longValue()));
                return null;
            } else {
                throw new DateTimeException("Invalid value for era: " + remove2);
            }
        } else if (!map.containsKey(ChronoField.ERA)) {
            return null;
        } else {
            ChronoField.ERA.checkValidValue(map.get(ChronoField.ERA).longValue());
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public LocalDate resolveYMD(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        int checkValidIntValue = ChronoField.YEAR.checkValidIntValue(map.remove(ChronoField.YEAR).longValue());
        if (resolverStyle == ResolverStyle.LENIENT) {
            long subtractExact = Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR).longValue(), 1);
            return LocalDate.m906of(checkValidIntValue, 1, 1).plusMonths(subtractExact).plusDays(Math.subtractExact(map.remove(ChronoField.DAY_OF_MONTH).longValue(), 1));
        }
        int checkValidIntValue2 = ChronoField.MONTH_OF_YEAR.checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR).longValue());
        int checkValidIntValue3 = ChronoField.DAY_OF_MONTH.checkValidIntValue(map.remove(ChronoField.DAY_OF_MONTH).longValue());
        if (resolverStyle == ResolverStyle.SMART) {
            if (checkValidIntValue2 == 4 || checkValidIntValue2 == 6 || checkValidIntValue2 == 9 || checkValidIntValue2 == 11) {
                checkValidIntValue3 = Math.min(checkValidIntValue3, 30);
            } else if (checkValidIntValue2 == 2) {
                checkValidIntValue3 = Math.min(checkValidIntValue3, Month.FEBRUARY.length(Year.isLeap((long) checkValidIntValue)));
            }
        }
        return LocalDate.m906of(checkValidIntValue, checkValidIntValue2, checkValidIntValue3);
    }

    public ValueRange range(ChronoField chronoField) {
        return chronoField.range();
    }

    public Period period(int i, int i2, int i3) {
        return Period.m926of(i, i2, i3);
    }

    /* access modifiers changed from: package-private */
    public Object writeReplace() {
        return super.writeReplace();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
