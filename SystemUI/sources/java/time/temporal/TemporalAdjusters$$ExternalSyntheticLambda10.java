package java.time.temporal;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TemporalAdjusters$$ExternalSyntheticLambda10 implements TemporalAdjuster {
    public final /* synthetic */ UnaryOperator f$0;

    public /* synthetic */ TemporalAdjusters$$ExternalSyntheticLambda10(UnaryOperator unaryOperator) {
        this.f$0 = unaryOperator;
    }

    public final Temporal adjustInto(Temporal temporal) {
        return temporal.with((LocalDate) this.f$0.apply(LocalDate.from(temporal)));
    }
}
