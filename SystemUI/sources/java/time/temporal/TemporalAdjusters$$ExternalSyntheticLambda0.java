package java.time.temporal;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TemporalAdjusters$$ExternalSyntheticLambda0 implements TemporalAdjuster {
    public final Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.DAY_OF_YEAR, temporal.range(ChronoField.DAY_OF_YEAR).getMaximum());
    }
}