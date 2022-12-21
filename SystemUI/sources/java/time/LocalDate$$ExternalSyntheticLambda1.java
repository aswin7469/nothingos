package java.time;

import java.util.function.LongFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LocalDate$$ExternalSyntheticLambda1 implements LongFunction {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ LocalDate$$ExternalSyntheticLambda1(long j, long j2) {
        this.f$0 = j;
        this.f$1 = j2;
    }

    public final Object apply(long j) {
        return LocalDate.ofEpochDay(this.f$0 + (j * this.f$1));
    }
}
