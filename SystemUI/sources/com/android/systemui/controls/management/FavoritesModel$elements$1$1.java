package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import com.android.systemui.controls.CustomIconCache;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FavoritesModel.kt */
/* synthetic */ class FavoritesModel$elements$1$1 extends FunctionReferenceImpl implements Function2<ComponentName, String, Icon> {
    FavoritesModel$elements$1$1(Object obj) {
        super(2, obj, CustomIconCache.class, "retrieve", "retrieve(Landroid/content/ComponentName;Ljava/lang/String;)Landroid/graphics/drawable/Icon;", 0);
    }

    public final Icon invoke(ComponentName componentName, String str) {
        Intrinsics.checkNotNullParameter(componentName, "p0");
        Intrinsics.checkNotNullParameter(str, "p1");
        return ((CustomIconCache) this.receiver).retrieve(componentName, str);
    }
}
