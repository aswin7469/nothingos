package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import com.android.systemui.controls.CustomIconCache;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: FavoritesModel.kt */
/* loaded from: classes.dex */
/* synthetic */ class FavoritesModel$elements$1$1 extends FunctionReferenceImpl implements Function2<ComponentName, String, Icon> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public FavoritesModel$elements$1$1(CustomIconCache customIconCache) {
        super(2, customIconCache, CustomIconCache.class, "retrieve", "retrieve(Landroid/content/ComponentName;Ljava/lang/String;)Landroid/graphics/drawable/Icon;", 0);
    }

    @Override // kotlin.jvm.functions.Function2
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Icon mo1950invoke(@NotNull ComponentName p0, @NotNull String p1) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        Intrinsics.checkNotNullParameter(p1, "p1");
        return ((CustomIconCache) this.receiver).retrieve(p0, p1);
    }
}
