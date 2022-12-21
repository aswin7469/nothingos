package java.time.temporal;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TemporalAdjusters$$ExternalSyntheticLambda9 implements TemporalAdjuster {
    public final Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.DAY_OF_YEAR, 1).plus(1, ChronoUnit.YEARS);
    }
}
