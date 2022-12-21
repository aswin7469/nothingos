package com.android.systemui.controls.p010ui;

import java.util.Comparator;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\b\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u000e\u0010\u0004\u001a\n \u0005*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0006\u001a\n \u0005*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0007\u0010\b¨\u0006\t"}, mo64987d2 = {"<anonymous>", "", "T", "K", "a", "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", "kotlin/comparisons/ComparisonsKt__ComparisonsKt$compareBy$3"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlsUiControllerImpl$special$$inlined$compareBy$1 */
/* compiled from: Comparisons.kt */
public final class ControlsUiControllerImpl$special$$inlined$compareBy$1<T> implements Comparator {
    final /* synthetic */ Comparator $comparator;

    public ControlsUiControllerImpl$special$$inlined$compareBy$1(Comparator comparator) {
        this.$comparator = comparator;
    }

    public final int compare(T t, T t2) {
        return this.$comparator.compare(((SelectionItem) t).getTitle(), ((SelectionItem) t2).getTitle());
    }
}
