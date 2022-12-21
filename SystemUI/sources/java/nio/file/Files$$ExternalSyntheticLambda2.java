package java.nio.file;

import java.nio.file.FileTreeWalker;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Files$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ BiPredicate f$0;

    public /* synthetic */ Files$$ExternalSyntheticLambda2(BiPredicate biPredicate) {
        this.f$0 = biPredicate;
    }

    public final boolean test(Object obj) {
        return this.f$0.test(((FileTreeWalker.Event) obj).file(), ((FileTreeWalker.Event) obj).attributes());
    }
}
