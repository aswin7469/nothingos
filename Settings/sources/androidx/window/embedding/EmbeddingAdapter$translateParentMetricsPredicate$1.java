package androidx.window.embedding;

import android.view.WindowMetrics;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: EmbeddingAdapter.kt */
final class EmbeddingAdapter$translateParentMetricsPredicate$1 extends Lambda implements Function1<WindowMetrics, Boolean> {
    final /* synthetic */ SplitRule $splitRule;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    EmbeddingAdapter$translateParentMetricsPredicate$1(SplitRule splitRule) {
        super(1);
        this.$splitRule = splitRule;
    }

    @NotNull
    public final Boolean invoke(@NotNull WindowMetrics windowMetrics) {
        Intrinsics.checkNotNullParameter(windowMetrics, "windowMetrics");
        return Boolean.valueOf(this.$splitRule.checkParentMetrics(windowMetrics));
    }
}
