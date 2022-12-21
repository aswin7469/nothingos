package java.time.temporal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.UnaryOperator;

public final class TemporalAdjusters {
    private TemporalAdjusters() {
    }

    public static TemporalAdjuster ofDateAdjuster(UnaryOperator<LocalDate> unaryOperator) {
        Objects.requireNonNull(unaryOperator, "dateBasedAdjuster");
        return new TemporalAdjusters$$ExternalSyntheticLambda10(unaryOperator);
    }

    public static TemporalAdjuster firstDayOfMonth() {
        return new TemporalAdjusters$$ExternalSyntheticLambda5();
    }

    public static TemporalAdjuster lastDayOfMonth() {
        return new TemporalAdjusters$$ExternalSyntheticLambda3();
    }

    public static TemporalAdjuster firstDayOfNextMonth() {
        return new TemporalAdjusters$$ExternalSyntheticLambda11();
    }

    public static TemporalAdjuster firstDayOfYear() {
        return new TemporalAdjusters$$ExternalSyntheticLambda7();
    }

    public static TemporalAdjuster lastDayOfYear() {
        return new TemporalAdjusters$$ExternalSyntheticLambda0();
    }

    public static TemporalAdjuster firstDayOfNextYear() {
        return new TemporalAdjusters$$ExternalSyntheticLambda9();
    }

    public static TemporalAdjuster firstInMonth(DayOfWeek dayOfWeek) {
        return dayOfWeekInMonth(1, dayOfWeek);
    }

    public static TemporalAdjuster lastInMonth(DayOfWeek dayOfWeek) {
        return dayOfWeekInMonth(-1, dayOfWeek);
    }

    public static TemporalAdjuster dayOfWeekInMonth(int i, DayOfWeek dayOfWeek) {
        Objects.requireNonNull(dayOfWeek, "dayOfWeek");
        int value = dayOfWeek.getValue();
        if (i >= 0) {
            return new TemporalAdjusters$$ExternalSyntheticLambda12(value, i);
        }
        return new TemporalAdjusters$$ExternalSyntheticLambda1(value, i);
    }

    static /* synthetic */ Temporal lambda$dayOfWeekInMonth$8(int i, int i2, Temporal temporal) {
        Temporal with = temporal.with(ChronoField.DAY_OF_MONTH, temporal.range(ChronoField.DAY_OF_MONTH).getMaximum());
        int i3 = i - with.get(ChronoField.DAY_OF_WEEK);
        if (i3 == 0) {
            i3 = 0;
        } else if (i3 > 0) {
            i3 -= 7;
        }
        return with.plus((long) ((int) (((long) i3) - ((((long) (-i2)) - 1) * 7))), ChronoUnit.DAYS);
    }

    public static TemporalAdjuster next(DayOfWeek dayOfWeek) {
        return new TemporalAdjusters$$ExternalSyntheticLambda4(dayOfWeek.getValue());
    }

    static /* synthetic */ Temporal lambda$next$9(int i, Temporal temporal) {
        int i2 = temporal.get(ChronoField.DAY_OF_WEEK) - i;
        return temporal.plus((long) (i2 >= 0 ? 7 - i2 : -i2), ChronoUnit.DAYS);
    }

    public static TemporalAdjuster nextOrSame(DayOfWeek dayOfWeek) {
        return new TemporalAdjusters$$ExternalSyntheticLambda2(dayOfWeek.getValue());
    }

    static /* synthetic */ Temporal lambda$nextOrSame$10(int i, Temporal temporal) {
        int i2 = temporal.get(ChronoField.DAY_OF_WEEK);
        if (i2 == i) {
            return temporal;
        }
        int i3 = i2 - i;
        return temporal.plus((long) (i3 >= 0 ? 7 - i3 : -i3), ChronoUnit.DAYS);
    }

    public static TemporalAdjuster previous(DayOfWeek dayOfWeek) {
        return new TemporalAdjusters$$ExternalSyntheticLambda8(dayOfWeek.getValue());
    }

    static /* synthetic */ Temporal lambda$previous$11(int i, Temporal temporal) {
        int i2 = i - temporal.get(ChronoField.DAY_OF_WEEK);
        return temporal.minus((long) (i2 >= 0 ? 7 - i2 : -i2), ChronoUnit.DAYS);
    }

    public static TemporalAdjuster previousOrSame(DayOfWeek dayOfWeek) {
        return new TemporalAdjusters$$ExternalSyntheticLambda6(dayOfWeek.getValue());
    }

    static /* synthetic */ Temporal lambda$previousOrSame$12(int i, Temporal temporal) {
        int i2 = temporal.get(ChronoField.DAY_OF_WEEK);
        if (i2 == i) {
            return temporal;
        }
        int i3 = i - i2;
        return temporal.minus((long) (i3 >= 0 ? 7 - i3 : -i3), ChronoUnit.DAYS);
    }
}
