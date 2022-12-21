package com.android.p019wm.shell.common;

import android.graphics.Rect;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "Landroid/graphics/Rect;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.common.FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsBelow$2 */
/* compiled from: FloatingContentCoordinator.kt */
final class C3434xe284ccc5 extends Lambda implements Function0<Rect> {
    final /* synthetic */ Rect $contentRect;
    final /* synthetic */ Rect $newlyOverlappingRect;
    final /* synthetic */ Ref.ObjectRef<List<Rect>> $rectsToAvoidBelow;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C3434xe284ccc5(Rect rect, Ref.ObjectRef<List<Rect>> objectRef, Rect rect2) {
        super(0);
        this.$contentRect = rect;
        this.$rectsToAvoidBelow = objectRef;
        this.$newlyOverlappingRect = rect2;
    }

    public final Rect invoke() {
        return FloatingContentCoordinator.Companion.findAreaForContentAboveOrBelow(this.$contentRect, CollectionsKt.plus((Collection) this.$rectsToAvoidBelow.element, this.$newlyOverlappingRect), false);
    }
}
