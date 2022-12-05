package com.android.wm.shell.common;

import android.graphics.Rect;
import com.android.wm.shell.common.FloatingContentCoordinator;
import kotlin.Lazy;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FloatingContentCoordinator.kt */
/* loaded from: classes2.dex */
public final class FloatingContentCoordinator$Companion$findAreaForContentVertically$positionBelowInBounds$2 extends Lambda implements Function0<Boolean> {
    final /* synthetic */ Rect $allowedBounds;
    final /* synthetic */ Lazy<Rect> $newContentBoundsBelow$delegate;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FloatingContentCoordinator$Companion$findAreaForContentVertically$positionBelowInBounds$2(Rect rect, Lazy<Rect> lazy) {
        super(0);
        this.$allowedBounds = rect;
        this.$newContentBoundsBelow$delegate = lazy;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo1951invoke() {
        return Boolean.valueOf(mo1951invoke());
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [boolean, java.lang.Boolean] */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final Boolean mo1951invoke() {
        Rect m1696findAreaForContentVertically$lambda3;
        Rect rect = this.$allowedBounds;
        m1696findAreaForContentVertically$lambda3 = FloatingContentCoordinator.Companion.m1696findAreaForContentVertically$lambda3(this.$newContentBoundsBelow$delegate);
        return rect.contains(m1696findAreaForContentVertically$lambda3);
    }
}
