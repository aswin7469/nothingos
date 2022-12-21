package java.util.stream;

import java.util.function.LongPredicate;
import java.util.function.Supplier;
import java.util.stream.MatchOps;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MatchOps$$ExternalSyntheticLambda3 implements Supplier {
    public final /* synthetic */ MatchOps.MatchKind f$0;
    public final /* synthetic */ LongPredicate f$1;

    public /* synthetic */ MatchOps$$ExternalSyntheticLambda3(MatchOps.MatchKind matchKind, LongPredicate longPredicate) {
        this.f$0 = matchKind;
        this.f$1 = longPredicate;
    }

    public final Object get() {
        return MatchOps.lambda$makeLong$2(this.f$0, this.f$1);
    }
}
