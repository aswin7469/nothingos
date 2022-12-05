package com.android.wm.shell.common;

import android.graphics.Rect;
import com.android.wm.shell.common.FloatingContentCoordinator;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref$ObjectRef;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FloatingContentCoordinator.kt */
/* loaded from: classes2.dex */
public final class FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsAbove$2 extends Lambda implements Function0<Rect> {
    final /* synthetic */ Rect $contentRect;
    final /* synthetic */ Rect $newlyOverlappingRect;
    final /* synthetic */ Ref$ObjectRef<List<Rect>> $rectsToAvoidAbove;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsAbove$2(Rect rect, Ref$ObjectRef<List<Rect>> ref$ObjectRef, Rect rect2) {
        super(0);
        this.$contentRect = rect;
        this.$rectsToAvoidAbove = ref$ObjectRef;
        this.$newlyOverlappingRect = rect2;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    @NotNull
    /* renamed from: invoke */
    public final Rect mo1951invoke() {
        List plus;
        FloatingContentCoordinator.Companion companion = FloatingContentCoordinator.Companion;
        Rect rect = this.$contentRect;
        plus = CollectionsKt___CollectionsKt.plus(this.$rectsToAvoidAbove.element, this.$newlyOverlappingRect);
        return companion.findAreaForContentAboveOrBelow(rect, plus, true);
    }
}
