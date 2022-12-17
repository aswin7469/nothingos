package androidx.window.embedding;

import android.app.Activity;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: EmbeddingAdapter.kt */
final class EmbeddingAdapter$translateActivityPairPredicates$1 extends Lambda implements Function2<Activity, Activity, Boolean> {
    final /* synthetic */ Set<SplitPairFilter> $splitPairFilters;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    EmbeddingAdapter$translateActivityPairPredicates$1(Set<SplitPairFilter> set) {
        super(2);
        this.$splitPairFilters = set;
    }

    @NotNull
    public final Boolean invoke(@NotNull Activity activity, @NotNull Activity activity2) {
        Intrinsics.checkNotNullParameter(activity, "first");
        Intrinsics.checkNotNullParameter(activity2, "second");
        Iterable iterable = this.$splitPairFilters;
        boolean z = false;
        if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
            Iterator it = iterable.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((SplitPairFilter) it.next()).matchesActivityPair(activity, activity2)) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return Boolean.valueOf(z);
    }
}
