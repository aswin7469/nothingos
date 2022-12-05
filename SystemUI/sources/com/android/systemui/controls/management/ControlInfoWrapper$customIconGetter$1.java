package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
/* synthetic */ class ControlInfoWrapper$customIconGetter$1 extends FunctionReferenceImpl implements Function2<ComponentName, String, Icon> {
    public static final ControlInfoWrapper$customIconGetter$1 INSTANCE = new ControlInfoWrapper$customIconGetter$1();

    ControlInfoWrapper$customIconGetter$1() {
        super(2, ControlsModelKt.class, "nullIconGetter", "nullIconGetter(Landroid/content/ComponentName;Ljava/lang/String;)Landroid/graphics/drawable/Icon;", 1);
    }

    @Override // kotlin.jvm.functions.Function2
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Icon mo1950invoke(@NotNull ComponentName p0, @NotNull String p1) {
        Icon nullIconGetter;
        Intrinsics.checkNotNullParameter(p0, "p0");
        Intrinsics.checkNotNullParameter(p1, "p1");
        nullIconGetter = ControlsModelKt.nullIconGetter(p0, p1);
        return nullIconGetter;
    }
}
