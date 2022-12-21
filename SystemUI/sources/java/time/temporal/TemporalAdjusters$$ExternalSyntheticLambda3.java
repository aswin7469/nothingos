package java.time.temporal;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TemporalAdjusters$$ExternalSyntheticLambda3 implements TemporalAdjuster {
    public final Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.DAY_OF_MONTH, temporal.range(ChronoField.DAY_OF_MONTH).getMaximum());
    }
}
