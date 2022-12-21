package java.time.format;

import android.net.wifi.WifiEnterpriseConfig;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Chronology;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

final class Parsed implements TemporalAccessor {
    Chronology chrono;
    private ChronoLocalDate date;
    Period excessDays = Period.ZERO;
    final Map<TemporalField, Long> fieldValues = new HashMap();
    boolean leapSecond;
    private ResolverStyle resolverStyle;
    private LocalTime time;
    ZoneId zone;

    Parsed() {
    }

    /* access modifiers changed from: package-private */
    public Parsed copy() {
        Parsed parsed = new Parsed();
        parsed.fieldValues.putAll(this.fieldValues);
        parsed.zone = this.zone;
        parsed.chrono = this.chrono;
        parsed.leapSecond = this.leapSecond;
        return parsed;
    }

    public boolean isSupported(TemporalField temporalField) {
        if (this.fieldValues.containsKey(temporalField)) {
            return true;
        }
        ChronoLocalDate chronoLocalDate = this.date;
        if (chronoLocalDate != null && chronoLocalDate.isSupported(temporalField)) {
            return true;
        }
        LocalTime localTime = this.time;
        if (localTime != null && localTime.isSupported(temporalField)) {
            return true;
        }
        if (temporalField == null || (temporalField instanceof ChronoField) || !temporalField.isSupportedBy(this)) {
            return false;
        }
        return true;
    }

    public long getLong(TemporalField temporalField) {
        Objects.requireNonNull(temporalField, "field");
        Long l = this.fieldValues.get(temporalField);
        if (l != null) {
            return l.longValue();
        }
        ChronoLocalDate chronoLocalDate = this.date;
        if (chronoLocalDate != null && chronoLocalDate.isSupported(temporalField)) {
            return this.date.getLong(temporalField);
        }
        LocalTime localTime = this.time;
        if (localTime != null && localTime.isSupported(temporalField)) {
            return this.time.getLong(temporalField);
        }
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId()) {
            return this.zone;
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return this.chrono;
        }
        if (temporalQuery == TemporalQueries.localDate()) {
            ChronoLocalDate chronoLocalDate = this.date;
            if (chronoLocalDate != null) {
                return LocalDate.from(chronoLocalDate);
            }
            return null;
        } else if (temporalQuery == TemporalQueries.localTime()) {
            return this.time;
        } else {
            if (temporalQuery == TemporalQueries.offset()) {
                Long l = this.fieldValues.get(ChronoField.OFFSET_SECONDS);
                if (l != null) {
                    return ZoneOffset.ofTotalSeconds(l.intValue());
                }
                R r = this.zone;
                if (r instanceof ZoneOffset) {
                    return r;
                }
                return temporalQuery.queryFrom(this);
            } else if (temporalQuery == TemporalQueries.zone()) {
                return temporalQuery.queryFrom(this);
            } else {
                if (temporalQuery == TemporalQueries.precision()) {
                    return null;
                }
                return temporalQuery.queryFrom(this);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public TemporalAccessor resolve(ResolverStyle resolverStyle2, Set<TemporalField> set) {
        if (set != null) {
            this.fieldValues.keySet().retainAll(set);
        }
        this.resolverStyle = resolverStyle2;
        resolveFields();
        resolveTimeLenient();
        crossCheck();
        resolvePeriod();
        resolveFractional();
        resolveInstant();
        return this;
    }

    private void resolveFields() {
        resolveInstantFields();
        resolveDateFields();
        resolveTimeFields();
        if (this.fieldValues.size() > 0) {
            int i = 0;
            loop0:
            while (i < 50) {
                for (Map.Entry<TemporalField, Long> key : this.fieldValues.entrySet()) {
                    TemporalField temporalField = (TemporalField) key.getKey();
                    Object resolve = temporalField.resolve(this.fieldValues, this, this.resolverStyle);
                    if (resolve != null) {
                        if (resolve instanceof ChronoZonedDateTime) {
                            ChronoZonedDateTime chronoZonedDateTime = (ChronoZonedDateTime) resolve;
                            ZoneId zoneId = this.zone;
                            if (zoneId == null) {
                                this.zone = chronoZonedDateTime.getZone();
                            } else if (!zoneId.equals(chronoZonedDateTime.getZone())) {
                                throw new DateTimeException("ChronoZonedDateTime must use the effective parsed zone: " + this.zone);
                            }
                            resolve = chronoZonedDateTime.toLocalDateTime();
                        }
                        if (resolve instanceof ChronoLocalDateTime) {
                            ChronoLocalDateTime chronoLocalDateTime = (ChronoLocalDateTime) resolve;
                            updateCheckConflict(chronoLocalDateTime.toLocalTime(), Period.ZERO);
                            updateCheckConflict(chronoLocalDateTime.toLocalDate());
                        } else if (resolve instanceof ChronoLocalDate) {
                            updateCheckConflict((ChronoLocalDate) resolve);
                        } else if (resolve instanceof LocalTime) {
                            updateCheckConflict((LocalTime) resolve, Period.ZERO);
                        } else {
                            throw new DateTimeException("Method resolve() can only return ChronoZonedDateTime, ChronoLocalDateTime, ChronoLocalDate or LocalTime");
                        }
                    } else if (!this.fieldValues.containsKey(temporalField)) {
                    }
                    i++;
                }
            }
            if (i == 50) {
                throw new DateTimeException("One of the parsed fields has an incorrectly implemented resolve method");
            } else if (i > 0) {
                resolveInstantFields();
                resolveDateFields();
                resolveTimeFields();
            }
        }
    }

    private void updateCheckConflict(TemporalField temporalField, TemporalField temporalField2, Long l) {
        Long put = this.fieldValues.put(temporalField2, l);
        if (put != null && put.longValue() != l.longValue()) {
            throw new DateTimeException("Conflict found: " + temporalField2 + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + put + " differs from " + temporalField2 + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + l + " while resolving  " + temporalField);
        }
    }

    private void resolveInstantFields() {
        if (this.fieldValues.containsKey(ChronoField.INSTANT_SECONDS)) {
            ZoneId zoneId = this.zone;
            if (zoneId != null) {
                resolveInstantFields0(zoneId);
                return;
            }
            Long l = this.fieldValues.get(ChronoField.OFFSET_SECONDS);
            if (l != null) {
                resolveInstantFields0(ZoneOffset.ofTotalSeconds(l.intValue()));
            }
        }
    }

    private void resolveInstantFields0(ZoneId zoneId) {
        ChronoZonedDateTime zonedDateTime = this.chrono.zonedDateTime(Instant.ofEpochSecond(this.fieldValues.remove(ChronoField.INSTANT_SECONDS).longValue()), zoneId);
        updateCheckConflict(zonedDateTime.toLocalDate());
        updateCheckConflict(ChronoField.INSTANT_SECONDS, ChronoField.SECOND_OF_DAY, Long.valueOf((long) zonedDateTime.toLocalTime().toSecondOfDay()));
    }

    private void resolveDateFields() {
        updateCheckConflict(this.chrono.resolveDate(this.fieldValues, this.resolverStyle));
    }

    private void updateCheckConflict(ChronoLocalDate chronoLocalDate) {
        ChronoLocalDate chronoLocalDate2 = this.date;
        if (chronoLocalDate2 != null) {
            if (chronoLocalDate != null && !chronoLocalDate2.equals(chronoLocalDate)) {
                throw new DateTimeException("Conflict found: Fields resolved to two different dates: " + this.date + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + chronoLocalDate);
            }
        } else if (chronoLocalDate == null) {
        } else {
            if (this.chrono.equals(chronoLocalDate.getChronology())) {
                this.date = chronoLocalDate;
                return;
            }
            throw new DateTimeException("ChronoLocalDate must use the effective parsed chronology: " + this.chrono);
        }
    }

    private void resolveTimeFields() {
        long j = 0;
        if (this.fieldValues.containsKey(ChronoField.CLOCK_HOUR_OF_DAY)) {
            long longValue = this.fieldValues.remove(ChronoField.CLOCK_HOUR_OF_DAY).longValue();
            if (this.resolverStyle == ResolverStyle.STRICT || (this.resolverStyle == ResolverStyle.SMART && longValue != 0)) {
                ChronoField.CLOCK_HOUR_OF_DAY.checkValidValue(longValue);
            }
            ChronoField chronoField = ChronoField.CLOCK_HOUR_OF_DAY;
            ChronoField chronoField2 = ChronoField.HOUR_OF_DAY;
            if (longValue == 24) {
                longValue = 0;
            }
            updateCheckConflict(chronoField, chronoField2, Long.valueOf(longValue));
        }
        if (this.fieldValues.containsKey(ChronoField.CLOCK_HOUR_OF_AMPM)) {
            long longValue2 = this.fieldValues.remove(ChronoField.CLOCK_HOUR_OF_AMPM).longValue();
            if (this.resolverStyle == ResolverStyle.STRICT || (this.resolverStyle == ResolverStyle.SMART && longValue2 != 0)) {
                ChronoField.CLOCK_HOUR_OF_AMPM.checkValidValue(longValue2);
            }
            ChronoField chronoField3 = ChronoField.CLOCK_HOUR_OF_AMPM;
            ChronoField chronoField4 = ChronoField.HOUR_OF_AMPM;
            if (longValue2 != 12) {
                j = longValue2;
            }
            updateCheckConflict(chronoField3, chronoField4, Long.valueOf(j));
        }
        if (this.fieldValues.containsKey(ChronoField.AMPM_OF_DAY) && this.fieldValues.containsKey(ChronoField.HOUR_OF_AMPM)) {
            long longValue3 = this.fieldValues.remove(ChronoField.AMPM_OF_DAY).longValue();
            long longValue4 = this.fieldValues.remove(ChronoField.HOUR_OF_AMPM).longValue();
            if (this.resolverStyle == ResolverStyle.LENIENT) {
                updateCheckConflict(ChronoField.AMPM_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf(Math.addExact(Math.multiplyExact(longValue3, 12), longValue4)));
            } else {
                ChronoField.AMPM_OF_DAY.checkValidValue(longValue3);
                ChronoField.HOUR_OF_AMPM.checkValidValue(longValue3);
                updateCheckConflict(ChronoField.AMPM_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf((longValue3 * 12) + longValue4));
            }
        }
        if (this.fieldValues.containsKey(ChronoField.NANO_OF_DAY)) {
            long longValue5 = this.fieldValues.remove(ChronoField.NANO_OF_DAY).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.NANO_OF_DAY.checkValidValue(longValue5);
            }
            updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf(longValue5 / 3600000000000L));
            updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.MINUTE_OF_HOUR, Long.valueOf((longValue5 / 60000000000L) % 60));
            updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.SECOND_OF_MINUTE, Long.valueOf((longValue5 / 1000000000) % 60));
            updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.NANO_OF_SECOND, Long.valueOf(longValue5 % 1000000000));
        }
        if (this.fieldValues.containsKey(ChronoField.MICRO_OF_DAY)) {
            long longValue6 = this.fieldValues.remove(ChronoField.MICRO_OF_DAY).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MICRO_OF_DAY.checkValidValue(longValue6);
            }
            updateCheckConflict(ChronoField.MICRO_OF_DAY, ChronoField.SECOND_OF_DAY, Long.valueOf(longValue6 / 1000000));
            updateCheckConflict(ChronoField.MICRO_OF_DAY, ChronoField.MICRO_OF_SECOND, Long.valueOf(longValue6 % 1000000));
        }
        if (this.fieldValues.containsKey(ChronoField.MILLI_OF_DAY)) {
            long longValue7 = this.fieldValues.remove(ChronoField.MILLI_OF_DAY).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MILLI_OF_DAY.checkValidValue(longValue7);
            }
            updateCheckConflict(ChronoField.MILLI_OF_DAY, ChronoField.SECOND_OF_DAY, Long.valueOf(longValue7 / 1000));
            updateCheckConflict(ChronoField.MILLI_OF_DAY, ChronoField.MILLI_OF_SECOND, Long.valueOf(longValue7 % 1000));
        }
        if (this.fieldValues.containsKey(ChronoField.SECOND_OF_DAY)) {
            long longValue8 = this.fieldValues.remove(ChronoField.SECOND_OF_DAY).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.SECOND_OF_DAY.checkValidValue(longValue8);
            }
            updateCheckConflict(ChronoField.SECOND_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf(longValue8 / 3600));
            updateCheckConflict(ChronoField.SECOND_OF_DAY, ChronoField.MINUTE_OF_HOUR, Long.valueOf((longValue8 / 60) % 60));
            updateCheckConflict(ChronoField.SECOND_OF_DAY, ChronoField.SECOND_OF_MINUTE, Long.valueOf(longValue8 % 60));
        }
        if (this.fieldValues.containsKey(ChronoField.MINUTE_OF_DAY)) {
            long longValue9 = this.fieldValues.remove(ChronoField.MINUTE_OF_DAY).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MINUTE_OF_DAY.checkValidValue(longValue9);
            }
            updateCheckConflict(ChronoField.MINUTE_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf(longValue9 / 60));
            updateCheckConflict(ChronoField.MINUTE_OF_DAY, ChronoField.MINUTE_OF_HOUR, Long.valueOf(longValue9 % 60));
        }
        if (this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND)) {
            long longValue10 = this.fieldValues.get(ChronoField.NANO_OF_SECOND).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.NANO_OF_SECOND.checkValidValue(longValue10);
            }
            if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND)) {
                long longValue11 = this.fieldValues.remove(ChronoField.MICRO_OF_SECOND).longValue();
                if (this.resolverStyle != ResolverStyle.LENIENT) {
                    ChronoField.MICRO_OF_SECOND.checkValidValue(longValue11);
                }
                longValue10 = (longValue10 % 1000) + (longValue11 * 1000);
                updateCheckConflict(ChronoField.MICRO_OF_SECOND, ChronoField.NANO_OF_SECOND, Long.valueOf(longValue10));
            }
            if (this.fieldValues.containsKey(ChronoField.MILLI_OF_SECOND)) {
                long longValue12 = this.fieldValues.remove(ChronoField.MILLI_OF_SECOND).longValue();
                if (this.resolverStyle != ResolverStyle.LENIENT) {
                    ChronoField.MILLI_OF_SECOND.checkValidValue(longValue12);
                }
                updateCheckConflict(ChronoField.MILLI_OF_SECOND, ChronoField.NANO_OF_SECOND, Long.valueOf((longValue12 * 1000000) + (longValue10 % 1000000)));
            }
        }
        if (this.fieldValues.containsKey(ChronoField.HOUR_OF_DAY) && this.fieldValues.containsKey(ChronoField.MINUTE_OF_HOUR) && this.fieldValues.containsKey(ChronoField.SECOND_OF_MINUTE) && this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND)) {
            resolveTime(this.fieldValues.remove(ChronoField.HOUR_OF_DAY).longValue(), this.fieldValues.remove(ChronoField.MINUTE_OF_HOUR).longValue(), this.fieldValues.remove(ChronoField.SECOND_OF_MINUTE).longValue(), this.fieldValues.remove(ChronoField.NANO_OF_SECOND).longValue());
        }
    }

    private void resolveTimeLenient() {
        if (this.time == null) {
            if (this.fieldValues.containsKey(ChronoField.MILLI_OF_SECOND)) {
                long longValue = this.fieldValues.remove(ChronoField.MILLI_OF_SECOND).longValue();
                if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND)) {
                    long longValue2 = (longValue * 1000) + (this.fieldValues.get(ChronoField.MICRO_OF_SECOND).longValue() % 1000);
                    updateCheckConflict(ChronoField.MILLI_OF_SECOND, ChronoField.MICRO_OF_SECOND, Long.valueOf(longValue2));
                    this.fieldValues.remove(ChronoField.MICRO_OF_SECOND);
                    this.fieldValues.put(ChronoField.NANO_OF_SECOND, Long.valueOf(longValue2 * 1000));
                } else {
                    this.fieldValues.put(ChronoField.NANO_OF_SECOND, Long.valueOf(longValue * 1000000));
                }
            } else if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND)) {
                this.fieldValues.put(ChronoField.NANO_OF_SECOND, Long.valueOf(this.fieldValues.remove(ChronoField.MICRO_OF_SECOND).longValue() * 1000));
            }
            Long l = this.fieldValues.get(ChronoField.HOUR_OF_DAY);
            if (l != null) {
                Long l2 = this.fieldValues.get(ChronoField.MINUTE_OF_HOUR);
                Long l3 = this.fieldValues.get(ChronoField.SECOND_OF_MINUTE);
                Long l4 = this.fieldValues.get(ChronoField.NANO_OF_SECOND);
                if (l2 == null && (l3 != null || l4 != null)) {
                    return;
                }
                if (l2 == null || l3 != null || l4 == null) {
                    resolveTime(l.longValue(), l2 != null ? l2.longValue() : 0, l3 != null ? l3.longValue() : 0, l4 != null ? l4.longValue() : 0);
                    this.fieldValues.remove(ChronoField.HOUR_OF_DAY);
                    this.fieldValues.remove(ChronoField.MINUTE_OF_HOUR);
                    this.fieldValues.remove(ChronoField.SECOND_OF_MINUTE);
                    this.fieldValues.remove(ChronoField.NANO_OF_SECOND);
                } else {
                    return;
                }
            }
        }
        if (this.resolverStyle != ResolverStyle.LENIENT && this.fieldValues.size() > 0) {
            for (Map.Entry next : this.fieldValues.entrySet()) {
                TemporalField temporalField = (TemporalField) next.getKey();
                if ((temporalField instanceof ChronoField) && temporalField.isTimeBased()) {
                    ((ChronoField) temporalField).checkValidValue(((Long) next.getValue()).longValue());
                }
            }
        }
    }

    private void resolveTime(long j, long j2, long j3, long j4) {
        if (this.resolverStyle == ResolverStyle.LENIENT) {
            long addExact = Math.addExact(Math.addExact(Math.addExact(Math.multiplyExact(j, 3600000000000L), Math.multiplyExact(j2, 60000000000L)), Math.multiplyExact(j3, 1000000000)), j4);
            updateCheckConflict(LocalTime.ofNanoOfDay(Math.floorMod(addExact, 86400000000000L)), Period.ofDays((int) Math.floorDiv(addExact, 86400000000000L)));
            return;
        }
        int checkValidIntValue = ChronoField.MINUTE_OF_HOUR.checkValidIntValue(j2);
        int checkValidIntValue2 = ChronoField.NANO_OF_SECOND.checkValidIntValue(j4);
        if (this.resolverStyle == ResolverStyle.SMART && j == 24 && checkValidIntValue == 0 && j3 == 0 && checkValidIntValue2 == 0) {
            updateCheckConflict(LocalTime.MIDNIGHT, Period.ofDays(1));
        } else {
            updateCheckConflict(LocalTime.m919of(ChronoField.HOUR_OF_DAY.checkValidIntValue(j), checkValidIntValue, ChronoField.SECOND_OF_MINUTE.checkValidIntValue(j3), checkValidIntValue2), Period.ZERO);
        }
    }

    private void resolvePeriod() {
        if (this.date != null && this.time != null && !this.excessDays.isZero()) {
            this.date = this.date.plus((TemporalAmount) this.excessDays);
            this.excessDays = Period.ZERO;
        }
    }

    private void resolveFractional() {
        if (this.time != null) {
            return;
        }
        if (!this.fieldValues.containsKey(ChronoField.INSTANT_SECONDS) && !this.fieldValues.containsKey(ChronoField.SECOND_OF_DAY) && !this.fieldValues.containsKey(ChronoField.SECOND_OF_MINUTE)) {
            return;
        }
        if (this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND)) {
            long longValue = this.fieldValues.get(ChronoField.NANO_OF_SECOND).longValue();
            this.fieldValues.put(ChronoField.MICRO_OF_SECOND, Long.valueOf(longValue / 1000));
            this.fieldValues.put(ChronoField.MILLI_OF_SECOND, Long.valueOf(longValue / 1000000));
            return;
        }
        this.fieldValues.put(ChronoField.NANO_OF_SECOND, 0L);
        this.fieldValues.put(ChronoField.MICRO_OF_SECOND, 0L);
        this.fieldValues.put(ChronoField.MILLI_OF_SECOND, 0L);
    }

    private void resolveInstant() {
        if (this.date != null && this.time != null) {
            Long l = this.fieldValues.get(ChronoField.OFFSET_SECONDS);
            if (l != null) {
                this.fieldValues.put(ChronoField.INSTANT_SECONDS, Long.valueOf(this.date.atTime(this.time).atZone(ZoneOffset.ofTotalSeconds(l.intValue())).toEpochSecond()));
            } else if (this.zone != null) {
                this.fieldValues.put(ChronoField.INSTANT_SECONDS, Long.valueOf(this.date.atTime(this.time).atZone(this.zone).toEpochSecond()));
            }
        }
    }

    private void updateCheckConflict(LocalTime localTime, Period period) {
        LocalTime localTime2 = this.time;
        if (localTime2 == null) {
            this.time = localTime;
            this.excessDays = period;
        } else if (!localTime2.equals(localTime)) {
            throw new DateTimeException("Conflict found: Fields resolved to different times: " + this.time + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + localTime);
        } else if (this.excessDays.isZero() || period.isZero() || this.excessDays.equals(period)) {
            this.excessDays = period;
        } else {
            throw new DateTimeException("Conflict found: Fields resolved to different excess periods: " + this.excessDays + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + period);
        }
    }

    private void crossCheck() {
        ChronoLocalDate chronoLocalDate = this.date;
        if (chronoLocalDate != null) {
            crossCheck(chronoLocalDate);
        }
        LocalTime localTime = this.time;
        if (localTime != null) {
            crossCheck(localTime);
            if (this.date != null && this.fieldValues.size() > 0) {
                crossCheck(this.date.atTime(this.time));
            }
        }
    }

    private void crossCheck(TemporalAccessor temporalAccessor) {
        Iterator<Map.Entry<TemporalField, Long>> it = this.fieldValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry next = it.next();
            TemporalField temporalField = (TemporalField) next.getKey();
            if (temporalAccessor.isSupported(temporalField)) {
                try {
                    long j = temporalAccessor.getLong(temporalField);
                    long longValue = ((Long) next.getValue()).longValue();
                    if (j == longValue) {
                        it.remove();
                    } else {
                        throw new DateTimeException("Conflict found: Field " + temporalField + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + j + " differs from " + temporalField + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + longValue + " derived from " + temporalAccessor);
                    }
                } catch (RuntimeException unused) {
                    continue;
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append((Object) this.fieldValues);
        sb.append(',');
        sb.append((Object) this.chrono);
        if (this.zone != null) {
            sb.append(',');
            sb.append((Object) this.zone);
        }
        if (!(this.date == null && this.time == null)) {
            sb.append(" resolved to ");
            ChronoLocalDate chronoLocalDate = this.date;
            if (chronoLocalDate != null) {
                sb.append((Object) chronoLocalDate);
                if (this.time != null) {
                    sb.append('T');
                    sb.append((Object) this.time);
                }
            } else {
                sb.append((Object) this.time);
            }
        }
        return sb.toString();
    }
}
