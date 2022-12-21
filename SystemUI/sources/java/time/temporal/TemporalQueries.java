package java.time.temporal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.Chronology;

public final class TemporalQueries {
    static final TemporalQuery<Chronology> CHRONO = new TemporalQuery<Chronology>() {
        public String toString() {
            return "Chronology";
        }

        public Chronology queryFrom(TemporalAccessor temporalAccessor) {
            return (Chronology) temporalAccessor.query(TemporalQueries.CHRONO);
        }
    };
    static final TemporalQuery<LocalDate> LOCAL_DATE = new TemporalQuery<LocalDate>() {
        public String toString() {
            return "LocalDate";
        }

        public LocalDate queryFrom(TemporalAccessor temporalAccessor) {
            if (temporalAccessor.isSupported(ChronoField.EPOCH_DAY)) {
                return LocalDate.ofEpochDay(temporalAccessor.getLong(ChronoField.EPOCH_DAY));
            }
            return null;
        }
    };
    static final TemporalQuery<LocalTime> LOCAL_TIME = new TemporalQuery<LocalTime>() {
        public String toString() {
            return "LocalTime";
        }

        public LocalTime queryFrom(TemporalAccessor temporalAccessor) {
            if (temporalAccessor.isSupported(ChronoField.NANO_OF_DAY)) {
                return LocalTime.ofNanoOfDay(temporalAccessor.getLong(ChronoField.NANO_OF_DAY));
            }
            return null;
        }
    };
    static final TemporalQuery<ZoneOffset> OFFSET = new TemporalQuery<ZoneOffset>() {
        public String toString() {
            return "ZoneOffset";
        }

        public ZoneOffset queryFrom(TemporalAccessor temporalAccessor) {
            if (temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS)) {
                return ZoneOffset.ofTotalSeconds(temporalAccessor.get(ChronoField.OFFSET_SECONDS));
            }
            return null;
        }
    };
    static final TemporalQuery<TemporalUnit> PRECISION = new TemporalQuery<TemporalUnit>() {
        public String toString() {
            return "Precision";
        }

        public TemporalUnit queryFrom(TemporalAccessor temporalAccessor) {
            return (TemporalUnit) temporalAccessor.query(TemporalQueries.PRECISION);
        }
    };
    static final TemporalQuery<ZoneId> ZONE = new TemporalQuery<ZoneId>() {
        public String toString() {
            return "Zone";
        }

        public ZoneId queryFrom(TemporalAccessor temporalAccessor) {
            ZoneId zoneId = (ZoneId) temporalAccessor.query(TemporalQueries.ZONE_ID);
            return zoneId != null ? zoneId : (ZoneId) temporalAccessor.query(TemporalQueries.OFFSET);
        }
    };
    static final TemporalQuery<ZoneId> ZONE_ID = new TemporalQuery<ZoneId>() {
        public String toString() {
            return "ZoneId";
        }

        public ZoneId queryFrom(TemporalAccessor temporalAccessor) {
            return (ZoneId) temporalAccessor.query(TemporalQueries.ZONE_ID);
        }
    };

    private TemporalQueries() {
    }

    public static TemporalQuery<ZoneId> zoneId() {
        return ZONE_ID;
    }

    public static TemporalQuery<Chronology> chronology() {
        return CHRONO;
    }

    public static TemporalQuery<TemporalUnit> precision() {
        return PRECISION;
    }

    public static TemporalQuery<ZoneId> zone() {
        return ZONE;
    }

    public static TemporalQuery<ZoneOffset> offset() {
        return OFFSET;
    }

    public static TemporalQuery<LocalDate> localDate() {
        return LOCAL_DATE;
    }

    public static TemporalQuery<LocalTime> localTime() {
        return LOCAL_TIME;
    }
}
