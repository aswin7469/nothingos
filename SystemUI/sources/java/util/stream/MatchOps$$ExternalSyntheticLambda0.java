package java.util.stream;

import java.util.function.IntPredicate;
import java.util.function.Supplier;
import java.util.stream.MatchOps;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MatchOps$$ExternalSyntheticLambda0 implements Supplier {
    public final /* synthetic */ MatchOps.MatchKind f$0;
    public final /* synthetic */ IntPredicate f$1;

    public /* synthetic */ MatchOps$$ExternalSyntheticLambda0(MatchOps.MatchKind matchKind, IntPredicate intPredicate) {
        this.f$0 = matchKind;
        this.f$1 = intPredicate;
    }

    public final Object get() {
        return MatchOps.lambda$makeInt$1(this.f$0, this.f$1);
    }
}
