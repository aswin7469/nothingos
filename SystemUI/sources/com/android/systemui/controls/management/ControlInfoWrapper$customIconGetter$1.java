package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsModel.kt */
/* synthetic */ class ControlInfoWrapper$customIconGetter$1 extends FunctionReferenceImpl implements Function2<ComponentName, String, Icon> {
    public static final ControlInfoWrapper$customIconGetter$1 INSTANCE = new ControlInfoWrapper$customIconGetter$1();

    ControlInfoWrapper$customIconGetter$1() {
        super(2, ControlsModelKt.class, "nullIconGetter", "nullIconGetter(Landroid/content/ComponentName;Ljava/lang/String;)Landroid/graphics/drawable/Icon;", 1);
    }

    public final Icon invoke(ComponentName componentName, String str) {
        Intrinsics.checkNotNullParameter(componentName, "p0");
        Intrinsics.checkNotNullParameter(str, "p1");
        return ControlsModelKt.nullIconGetter(componentName, str);
    }
}
