package com.android.p019wm.shell.pip.p020tv;

import android.graphics.Rect;
import com.android.p019wm.shell.pip.p020tv.TvPipKeepClearAlgorithm;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\n¢\u0006\u0002\b\u0006"}, mo64987d2 = {"<anonymous>", "Lkotlin/Function1;", "Landroid/graphics/Rect;", "", "unrestricted", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1 */
/* compiled from: TvPipKeepClearAlgorithm.kt */
final class TvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1 extends Lambda implements Function1<Boolean, Function1<? super Rect, ? extends Unit>> {
    final /* synthetic */ List<TvPipKeepClearAlgorithm.SweepLineEvent> $events;
    final /* synthetic */ Rect $pipBounds;
    final /* synthetic */ TvPipKeepClearAlgorithm this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    TvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1(TvPipKeepClearAlgorithm tvPipKeepClearAlgorithm, Rect rect, List<TvPipKeepClearAlgorithm.SweepLineEvent> list) {
        super(1);
        this.this$0 = tvPipKeepClearAlgorithm;
        this.$pipBounds = rect;
        this.$events = list;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Boolean) obj).booleanValue());
    }

    public final Function1<Rect, Unit> invoke(final boolean z) {
        final TvPipKeepClearAlgorithm tvPipKeepClearAlgorithm = this.this$0;
        final Rect rect = this.$pipBounds;
        final List<TvPipKeepClearAlgorithm.SweepLineEvent> list = this.$events;
        return new Function1<Rect, Unit>() {
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((Rect) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(Rect rect) {
                Intrinsics.checkNotNullParameter(rect, "area");
                if (tvPipKeepClearAlgorithm.intersectsX(rect, rect)) {
                    list.add(new TvPipKeepClearAlgorithm.SweepLineEvent(true, -rect.top, z, false, 8, (DefaultConstructorMarker) null));
                    list.add(new TvPipKeepClearAlgorithm.SweepLineEvent(false, -rect.bottom, z, false, 8, (DefaultConstructorMarker) null));
                }
            }
        };
    }
}
