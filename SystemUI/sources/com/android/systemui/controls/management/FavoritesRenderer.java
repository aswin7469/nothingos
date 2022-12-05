package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.res.Resources;
import com.android.systemui.R$plurals;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: AppAdapter.kt */
/* loaded from: classes.dex */
public final class FavoritesRenderer {
    @NotNull
    private final Function1<ComponentName, Integer> favoriteFunction;
    @NotNull
    private final Resources resources;

    /* JADX WARN: Multi-variable type inference failed */
    public FavoritesRenderer(@NotNull Resources resources, @NotNull Function1<? super ComponentName, Integer> favoriteFunction) {
        Intrinsics.checkNotNullParameter(resources, "resources");
        Intrinsics.checkNotNullParameter(favoriteFunction, "favoriteFunction");
        this.resources = resources;
        this.favoriteFunction = favoriteFunction;
    }

    @Nullable
    public final String renderFavoritesForComponent(@NotNull ComponentName component) {
        Intrinsics.checkNotNullParameter(component, "component");
        int intValue = this.favoriteFunction.mo1949invoke(component).intValue();
        if (intValue != 0) {
            return this.resources.getQuantityString(R$plurals.controls_number_of_favorites, intValue, Integer.valueOf(intValue));
        }
        return null;
    }
}
