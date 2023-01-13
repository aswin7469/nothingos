package com.android.p019wm.shell.common;

import android.graphics.Rect;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "Landroid/graphics/Rect;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.common.FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsAbove$2 */
/* compiled from: FloatingContentCoordinator.kt */
final class C3443xa8ad3931 extends Lambda implements Function0<Rect> {
    final /* synthetic */ Rect $contentRect;
    final /* synthetic */ Rect $newlyOverlappingRect;
    final /* synthetic */ Ref.ObjectRef<List<Rect>> $rectsToAvoidAbove;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C3443xa8ad3931(Rect rect, Ref.ObjectRef<List<Rect>> objectRef, Rect rect2) {
        super(0);
        this.$contentRect = rect;
        this.$rectsToAvoidAbove = objectRef;
        this.$newlyOverlappingRect = rect2;
    }

    public final Rect invoke() {
        return FloatingContentCoordinator.Companion.findAreaForContentAboveOrBelow(this.$contentRect, CollectionsKt.plus((Collection) this.$rectsToAvoidAbove.element, this.$newlyOverlappingRect), true);
    }
}
