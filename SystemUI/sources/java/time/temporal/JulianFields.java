package java.time.temporal;

import android.net.wifi.WifiEnterpriseConfig;
import java.time.DateTimeException;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.ResolverStyle;
import java.util.Map;

public final class JulianFields {
    public static final TemporalField JULIAN_DAY = Field.JULIAN_DAY;
    private static final long JULIAN_DAY_OFFSET = 2440588;
    public static final TemporalField MODIFIED_JULIAN_DAY = Field.MODIFIED_JULIAN_DAY;
    public static final TemporalField RATA_DIE = Field.RATA_DIE;

    private JulianFields() {
        throw new AssertionError((Object) "Not instantiable");
    }

    private enum Field implements TemporalField {
        JULIAN_DAY("JulianDay", ChronoUnit.DAYS, ChronoUnit.FOREVER, JulianFields.JULIAN_DAY_OFFSET),
        MODIFIED_JULIAN_DAY("ModifiedJulianDay", ChronoUnit.DAYS, ChronoUnit.FOREVER, 40587),
        RATA_DIE("RataDie", ChronoUnit.DAYS, ChronoUnit.FOREVER, 719163);
        
        private static final long serialVersionUID = -7501623920830201812L;
        private final transient TemporalUnit baseUnit;
        private final transient String name;
        private final transient long offset;
        private final transient ValueRange range;
        private final transient TemporalUnit rangeUnit;

        public boolean isDateBased() {
            return true;
        }

        public boolean isTimeBased() {
            return false;
        }

        private Field(String str, TemporalUnit temporalUnit, TemporalUnit temporalUnit2, long j) {
            this.name = str;
            this.baseUnit = temporalUnit;
            this.rangeUnit = temporalUnit2;
            this.range = ValueRange.m953of(-365243219162L + j, 365241780471L + j);
            this.offset = j;
        }

        public TemporalUnit getBaseUnit() {
            return this.baseUnit;
        }

        public TemporalUnit getRangeUnit() {
            return this.rangeUnit;
        }

        public ValueRange range() {
            return this.range;
        }

        public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
            return temporalAccessor.isSupported(ChronoField.EPOCH_DAY);
        }

        public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
            if (isSupportedBy(temporalAccessor)) {
                return range();
            }
            throw new DateTimeException("Unsupported field: " + this);
        }

        public long getFrom(TemporalAccessor temporalAccessor) {
            return temporalAccessor.getLong(ChronoField.EPOCH_DAY) + this.offset;
        }

        public <R extends Temporal> R adjustInto(R r, long j) {
            if (range().isValidValue(j)) {
                return r.with(ChronoField.EPOCH_DAY, Math.subtractExact(j, this.offset));
            }
            throw new DateTimeException("Invalid value: " + this.name + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + j);
        }

        public ChronoLocalDate resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
            long longValue = map.remove(this).longValue();
            Chronology from = Chronology.from(temporalAccessor);
            if (resolverStyle == ResolverStyle.LENIENT) {
                return from.dateEpochDay(Math.subtractExact(longValue, this.offset));
            }
            range().checkValidValue(longValue, this);
            return from.dateEpochDay(longValue - this.offset);
        }

        public String toString() {
            return this.name;
        }
    }
}
