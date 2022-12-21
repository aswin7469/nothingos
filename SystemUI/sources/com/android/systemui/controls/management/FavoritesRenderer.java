package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.res.Resources;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005¢\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\u0006R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, mo64987d2 = {"Lcom/android/systemui/controls/management/FavoritesRenderer;", "", "resources", "Landroid/content/res/Resources;", "favoriteFunction", "Lkotlin/Function1;", "Landroid/content/ComponentName;", "", "(Landroid/content/res/Resources;Lkotlin/jvm/functions/Function1;)V", "renderFavoritesForComponent", "", "component", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AppAdapter.kt */
public final class FavoritesRenderer {
    private final Function1<ComponentName, Integer> favoriteFunction;
    private final Resources resources;

    public FavoritesRenderer(Resources resources2, Function1<? super ComponentName, Integer> function1) {
        Intrinsics.checkNotNullParameter(resources2, "resources");
        Intrinsics.checkNotNullParameter(function1, "favoriteFunction");
        this.resources = resources2;
        this.favoriteFunction = function1;
    }

    public final String renderFavoritesForComponent(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        int intValue = this.favoriteFunction.invoke(componentName).intValue();
        if (intValue == 0) {
            return null;
        }
        return this.resources.getQuantityString(C1893R.plurals.controls_number_of_favorites, intValue, new Object[]{Integer.valueOf(intValue)});
    }
}
