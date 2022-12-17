package androidx.window.embedding;

import android.app.Activity;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: EmbeddingAdapter.kt */
final class EmbeddingAdapter$translateActivityPredicates$1 extends Lambda implements Function1<Activity, Boolean> {
    final /* synthetic */ Set<ActivityFilter> $activityFilters;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    EmbeddingAdapter$translateActivityPredicates$1(Set<ActivityFilter> set) {
        super(1);
        this.$activityFilters = set;
    }

    @NotNull
    public final Boolean invoke(@NotNull Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        Iterable iterable = this.$activityFilters;
        boolean z = false;
        if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
            Iterator it = iterable.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((ActivityFilter) it.next()).matchesActivity(activity)) {
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
