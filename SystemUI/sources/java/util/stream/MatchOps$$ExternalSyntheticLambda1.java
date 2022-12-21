package java.util.stream;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.MatchOps;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MatchOps$$ExternalSyntheticLambda1 implements Supplier {
    public final /* synthetic */ MatchOps.MatchKind f$0;
    public final /* synthetic */ Predicate f$1;

    public /* synthetic */ MatchOps$$ExternalSyntheticLambda1(MatchOps.MatchKind matchKind, Predicate predicate) {
        this.f$0 = matchKind;
        this.f$1 = predicate;
    }

    public final Object get() {
        return MatchOps.lambda$makeRef$0(this.f$0, this.f$1);
    }
}
