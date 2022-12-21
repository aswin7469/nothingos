package java.util.stream;

import java.util.function.DoublePredicate;
import java.util.function.Supplier;
import java.util.stream.MatchOps;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MatchOps$$ExternalSyntheticLambda2 implements Supplier {
    public final /* synthetic */ MatchOps.MatchKind f$0;
    public final /* synthetic */ DoublePredicate f$1;

    public /* synthetic */ MatchOps$$ExternalSyntheticLambda2(MatchOps.MatchKind matchKind, DoublePredicate doublePredicate) {
        this.f$0 = matchKind;
        this.f$1 = doublePredicate;
    }

    public final Object get() {
        return MatchOps.lambda$makeDouble$3(this.f$0, this.f$1);
    }
}
