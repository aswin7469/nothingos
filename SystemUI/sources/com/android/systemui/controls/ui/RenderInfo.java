package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.util.SparseArray;
import com.android.systemui.R$drawable;
import java.util.Map;
import kotlin.Pair;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: RenderInfo.kt */
/* loaded from: classes.dex */
public final class RenderInfo {
    private final int enabledBackground;
    private final int foreground;
    @NotNull
    private final Drawable icon;
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final SparseArray<Drawable> iconMap = new SparseArray<>();
    @NotNull
    private static final ArrayMap<ComponentName, Drawable> appIconMap = new ArrayMap<>();

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RenderInfo)) {
            return false;
        }
        RenderInfo renderInfo = (RenderInfo) obj;
        return Intrinsics.areEqual(this.icon, renderInfo.icon) && this.foreground == renderInfo.foreground && this.enabledBackground == renderInfo.enabledBackground;
    }

    public int hashCode() {
        return (((this.icon.hashCode() * 31) + Integer.hashCode(this.foreground)) * 31) + Integer.hashCode(this.enabledBackground);
    }

    @NotNull
    public String toString() {
        return "RenderInfo(icon=" + this.icon + ", foreground=" + this.foreground + ", enabledBackground=" + this.enabledBackground + ')';
    }

    public RenderInfo(@NotNull Drawable icon, int i, int i2) {
        Intrinsics.checkNotNullParameter(icon, "icon");
        this.icon = icon;
        this.foreground = i;
        this.enabledBackground = i2;
    }

    @NotNull
    public final Drawable getIcon() {
        return this.icon;
    }

    public final int getForeground() {
        return this.foreground;
    }

    public final int getEnabledBackground() {
        return this.enabledBackground;
    }

    /* compiled from: RenderInfo.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public static /* synthetic */ RenderInfo lookup$default(Companion companion, Context context, ComponentName componentName, int i, int i2, int i3, Object obj) {
            if ((i3 & 8) != 0) {
                i2 = 0;
            }
            return companion.lookup(context, componentName, i, i2);
        }

        @NotNull
        public final RenderInfo lookup(@NotNull Context context, @NotNull ComponentName componentName, int i, int i2) {
            Map map;
            Map map2;
            Drawable drawable;
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            if (i2 > 0) {
                i = (i * 1000) + i2;
            }
            map = RenderInfoKt.deviceColorMap;
            Pair pair = (Pair) MapsKt.getValue(map, Integer.valueOf(i));
            int intValue = ((Number) pair.component1()).intValue();
            int intValue2 = ((Number) pair.component2()).intValue();
            map2 = RenderInfoKt.deviceIconMap;
            int intValue3 = ((Number) MapsKt.getValue(map2, Integer.valueOf(i))).intValue();
            if (intValue3 == -1) {
                drawable = (Drawable) RenderInfo.appIconMap.get(componentName);
                if (drawable == null) {
                    drawable = context.getResources().getDrawable(R$drawable.ic_device_unknown_on, null);
                    RenderInfo.appIconMap.put(componentName, drawable);
                }
            } else {
                Drawable drawable2 = (Drawable) RenderInfo.iconMap.get(intValue3);
                if (drawable2 == null) {
                    drawable2 = context.getResources().getDrawable(intValue3, null);
                    RenderInfo.iconMap.put(intValue3, drawable2);
                }
                drawable = drawable2;
            }
            Intrinsics.checkNotNull(drawable);
            Drawable newDrawable = drawable.getConstantState().newDrawable(context.getResources());
            Intrinsics.checkNotNullExpressionValue(newDrawable, "!!.constantState.newDrawable(context.resources)");
            return new RenderInfo(newDrawable, intValue, intValue2);
        }

        public final void registerComponentIcon(@NotNull ComponentName componentName, @NotNull Drawable icon) {
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            Intrinsics.checkNotNullParameter(icon, "icon");
            RenderInfo.appIconMap.put(componentName, icon);
        }

        public final void clearCache() {
            RenderInfo.iconMap.clear();
            RenderInfo.appIconMap.clear();
        }
    }
}
