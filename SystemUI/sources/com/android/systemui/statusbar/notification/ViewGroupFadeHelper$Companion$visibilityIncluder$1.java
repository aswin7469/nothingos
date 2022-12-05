package com.android.systemui.statusbar.notification;

import android.view.View;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: ViewGroupFadeHelper.kt */
/* loaded from: classes.dex */
final class ViewGroupFadeHelper$Companion$visibilityIncluder$1 extends Lambda implements Function1<View, Boolean> {
    public static final ViewGroupFadeHelper$Companion$visibilityIncluder$1 INSTANCE = new ViewGroupFadeHelper$Companion$visibilityIncluder$1();

    ViewGroupFadeHelper$Companion$visibilityIncluder$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo1949invoke(View view) {
        return Boolean.valueOf(invoke2(view));
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final boolean invoke2(@NotNull View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        return view.getVisibility() == 0;
    }
}
