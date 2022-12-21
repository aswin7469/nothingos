package java.time;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.time.temporal.TemporalAmount;
import java.util.Objects;
import jdk.internal.misc.C4581VM;

public abstract class Clock {
    public abstract ZoneId getZone();

    public abstract Instant instant();

    public abstract Clock withZone(ZoneId zoneId);

    public static Clock systemUTC() {
        return SystemClock.UTC;
    }

    public static Clock systemDefaultZone() {
        return new SystemClock(ZoneId.systemDefault());
    }

    public static Clock system(ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        if (zoneId == ZoneOffset.UTC) {
            return SystemClock.UTC;
        }
        return new SystemClock(zoneId);
    }

    public static Clock tickMillis(ZoneId zoneId) {
        return new TickClock(system(zoneId), 1000000);
    }

    public static Clock tickSeconds(ZoneId zoneId) {
        return new TickClock(system(zoneId), 1000000000);
    }

    public static Clock tickMinutes(ZoneId zoneId) {
        return new TickClock(system(zoneId), 60000000000L);
    }

    public static Clock tick(Clock clock, Duration duration) {
        Objects.requireNonNull(clock, "baseClock");
        Objects.requireNonNull(duration, "tickDuration");
        if (!duration.isNegative()) {
            long nanos = duration.toNanos();
            if (nanos % 1000000 != 0 && 1000000000 % nanos != 0) {
                throw new IllegalArgumentException("Invalid tick duration");
            } else if (nanos <= 1) {
                return clock;
            } else {
                return new TickClock(clock, nanos);
            }
        } else {
            throw new IllegalArgumentException("Tick duration must not be negative");
        }
    }

    public static Clock fixed(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(instant, "fixedInstant");
        Objects.requireNonNull(zoneId, "zone");
        return new FixedClock(instant, zoneId);
    }

    public static Clock offset(Clock clock, Duration duration) {
        Objects.requireNonNull(clock, "baseClock");
        Objects.requireNonNull(duration, "offsetDuration");
        if (duration.equals(Duration.ZERO)) {
            return clock;
        }
        return new OffsetClock(clock, duration);
    }

    protected Clock() {
    }

    public long millis() {
        return instant().toEpochMilli();
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    static final class SystemClock extends Clock implements Serializable {
        private static final long OFFSET_SEED = ((System.currentTimeMillis() / 1000) - 1024);
        static final SystemClock UTC = new SystemClock(ZoneOffset.UTC);
        private static final long serialVersionUID = 6740630888130243051L;
        private transient long offset = OFFSET_SEED;
        private final ZoneId zone;

        SystemClock(ZoneId zoneId) {
            this.zone = zoneId;
        }

        public ZoneId getZone() {
            return this.zone;
        }

        public Clock withZone(ZoneId zoneId) {
            if (zoneId.equals(this.zone)) {
                return this;
            }
            return new SystemClock(zoneId);
        }

        public long millis() {
            return System.currentTimeMillis();
        }

        public Instant instant() {
            long j = this.offset;
            long nanoTimeAdjustment = C4581VM.getNanoTimeAdjustment(j);
            if (nanoTimeAdjustment == -1) {
                j = (System.currentTimeMillis() / 1000) - 1024;
                nanoTimeAdjustment = C4581VM.getNanoTimeAdjustment(j);
                if (nanoTimeAdjustment != -1) {
                    this.offset = j;
                } else {
                    throw new InternalError("Offset " + j + " is not in range");
                }
            }
            return Instant.ofEpochSecond(j, nanoTimeAdjustment);
        }

        public boolean equals(Object obj) {
            if (obj instanceof SystemClock) {
                return this.zone.equals(((SystemClock) obj).zone);
            }
            return false;
        }

        public int hashCode() {
            return this.zone.hashCode() + 1;
        }

        public String toString() {
            return "SystemClock[" + this.zone + NavigationBarInflaterView.SIZE_MOD_END;
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.offset = OFFSET_SEED;
        }
    }

    static final class FixedClock extends Clock implements Serializable {
        private static final long serialVersionUID = 7430389292664866958L;
        private final Instant instant;
        private final ZoneId zone;

        FixedClock(Instant instant2, ZoneId zoneId) {
            this.instant = instant2;
            this.zone = zoneId;
        }

        public ZoneId getZone() {
            return this.zone;
        }

        public Clock withZone(ZoneId zoneId) {
            if (zoneId.equals(this.zone)) {
                return this;
            }
            return new FixedClock(this.instant, zoneId);
        }

        public long millis() {
            return this.instant.toEpochMilli();
        }

        public Instant instant() {
            return this.instant;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof FixedClock)) {
                return false;
            }
            FixedClock fixedClock = (FixedClock) obj;
            if (!this.instant.equals(fixedClock.instant) || !this.zone.equals(fixedClock.zone)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.zone.hashCode() ^ this.instant.hashCode();
        }

        public String toString() {
            return "FixedClock[" + this.instant + NavigationBarInflaterView.BUTTON_SEPARATOR + this.zone + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    static final class OffsetClock extends Clock implements Serializable {
        private static final long serialVersionUID = 2007484719125426256L;
        private final Clock baseClock;
        private final Duration offset;

        OffsetClock(Clock clock, Duration duration) {
            this.baseClock = clock;
            this.offset = duration;
        }

        public ZoneId getZone() {
            return this.baseClock.getZone();
        }

        public Clock withZone(ZoneId zoneId) {
            if (zoneId.equals(this.baseClock.getZone())) {
                return this;
            }
            return new OffsetClock(this.baseClock.withZone(zoneId), this.offset);
        }

        public long millis() {
            return Math.addExact(this.baseClock.millis(), this.offset.toMillis());
        }

        public Instant instant() {
            return this.baseClock.instant().plus((TemporalAmount) this.offset);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof OffsetClock)) {
                return false;
            }
            OffsetClock offsetClock = (OffsetClock) obj;
            if (!this.baseClock.equals(offsetClock.baseClock) || !this.offset.equals(offsetClock.offset)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.offset.hashCode() ^ this.baseClock.hashCode();
        }

        public String toString() {
            return "OffsetClock[" + this.baseClock + NavigationBarInflaterView.BUTTON_SEPARATOR + this.offset + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    static final class TickClock extends Clock implements Serializable {
        private static final long serialVersionUID = 6504659149906368850L;
        private final Clock baseClock;
        private final long tickNanos;

        TickClock(Clock clock, long j) {
            this.baseClock = clock;
            this.tickNanos = j;
        }

        public ZoneId getZone() {
            return this.baseClock.getZone();
        }

        public Clock withZone(ZoneId zoneId) {
            if (zoneId.equals(this.baseClock.getZone())) {
                return this;
            }
            return new TickClock(this.baseClock.withZone(zoneId), this.tickNanos);
        }

        public long millis() {
            long millis = this.baseClock.millis();
            return millis - Math.floorMod(millis, this.tickNanos / 1000000);
        }

        public Instant instant() {
            if (this.tickNanos % 1000000 == 0) {
                long millis = this.baseClock.millis();
                return Instant.ofEpochMilli(millis - Math.floorMod(millis, this.tickNanos / 1000000));
            }
            Instant instant = this.baseClock.instant();
            return instant.minusNanos(Math.floorMod((long) instant.getNano(), this.tickNanos));
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof TickClock)) {
                return false;
            }
            TickClock tickClock = (TickClock) obj;
            if (!this.baseClock.equals(tickClock.baseClock) || this.tickNanos != tickClock.tickNanos) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int hashCode = this.baseClock.hashCode();
            long j = this.tickNanos;
            return ((int) (j ^ (j >>> 32))) ^ hashCode;
        }

        public String toString() {
            return "TickClock[" + this.baseClock + NavigationBarInflaterView.BUTTON_SEPARATOR + Duration.ofNanos(this.tickNanos) + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }
}
