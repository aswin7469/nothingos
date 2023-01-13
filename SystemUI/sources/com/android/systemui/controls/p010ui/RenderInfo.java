package com.android.systemui.controls.p010ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.util.SparseArray;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\b\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0005HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J'\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0018"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/RenderInfo;", "", "icon", "Landroid/graphics/drawable/Drawable;", "foreground", "", "enabledBackground", "(Landroid/graphics/drawable/Drawable;II)V", "getEnabledBackground", "()I", "getForeground", "getIcon", "()Landroid/graphics/drawable/Drawable;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.RenderInfo */
/* compiled from: RenderInfo.kt */
public final class RenderInfo {
    public static final int APP_ICON_ID = -1;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int ERROR_ICON = -1000;
    /* access modifiers changed from: private */
    public static final ArrayMap<ComponentName, Drawable> appIconMap = new ArrayMap<>();
    /* access modifiers changed from: private */
    public static final SparseArray<Drawable> iconMap = new SparseArray<>();
    private final int enabledBackground;
    private final int foreground;
    private final Drawable icon;

    public static /* synthetic */ RenderInfo copy$default(RenderInfo renderInfo, Drawable drawable, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            drawable = renderInfo.icon;
        }
        if ((i3 & 2) != 0) {
            i = renderInfo.foreground;
        }
        if ((i3 & 4) != 0) {
            i2 = renderInfo.enabledBackground;
        }
        return renderInfo.copy(drawable, i, i2);
    }

    public final Drawable component1() {
        return this.icon;
    }

    public final int component2() {
        return this.foreground;
    }

    public final int component3() {
        return this.enabledBackground;
    }

    public final RenderInfo copy(Drawable drawable, int i, int i2) {
        Intrinsics.checkNotNullParameter(drawable, BaseIconCache.IconDB.COLUMN_ICON);
        return new RenderInfo(drawable, i, i2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RenderInfo)) {
            return false;
        }
        RenderInfo renderInfo = (RenderInfo) obj;
        return Intrinsics.areEqual((Object) this.icon, (Object) renderInfo.icon) && this.foreground == renderInfo.foreground && this.enabledBackground == renderInfo.enabledBackground;
    }

    public int hashCode() {
        return (((this.icon.hashCode() * 31) + Integer.hashCode(this.foreground)) * 31) + Integer.hashCode(this.enabledBackground);
    }

    public String toString() {
        return "RenderInfo(icon=" + this.icon + ", foreground=" + this.foreground + ", enabledBackground=" + this.enabledBackground + ')';
    }

    public RenderInfo(Drawable drawable, int i, int i2) {
        Intrinsics.checkNotNullParameter(drawable, BaseIconCache.IconDB.COLUMN_ICON);
        this.icon = drawable;
        this.foreground = i;
        this.enabledBackground = i2;
    }

    public final Drawable getIcon() {
        return this.icon;
    }

    public final int getForeground() {
        return this.foreground;
    }

    public final int getEnabledBackground() {
        return this.enabledBackground;
    }

    @Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\f\u001a\u00020\rJ(\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00042\b\b\u0002\u0010\u0014\u001a\u00020\u0004J\u0016\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\tR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/RenderInfo$Companion;", "", "()V", "APP_ICON_ID", "", "ERROR_ICON", "appIconMap", "Landroid/util/ArrayMap;", "Landroid/content/ComponentName;", "Landroid/graphics/drawable/Drawable;", "iconMap", "Landroid/util/SparseArray;", "clearCache", "", "lookup", "Lcom/android/systemui/controls/ui/RenderInfo;", "context", "Landroid/content/Context;", "componentName", "deviceType", "offset", "registerComponentIcon", "icon", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.controls.ui.RenderInfo$Companion */
    /* compiled from: RenderInfo.kt */
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

        public final RenderInfo lookup(Context context, ComponentName componentName, int i, int i2) {
            Drawable drawable;
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
            if (i2 > 0) {
                i = (i * 1000) + i2;
            }
            Pair pair = (Pair) MapsKt.getValue(RenderInfoKt.deviceColorMap, Integer.valueOf(i));
            int intValue = ((Number) pair.component1()).intValue();
            int intValue2 = ((Number) pair.component2()).intValue();
            int intValue3 = ((Number) MapsKt.getValue(RenderInfoKt.deviceIconMap, Integer.valueOf(i))).intValue();
            if (intValue3 == -1) {
                drawable = (Drawable) RenderInfo.appIconMap.get(componentName);
                if (drawable == null) {
                    drawable = context.getResources().getDrawable(C1894R.C1896drawable.ic_device_unknown_on, (Resources.Theme) null);
                    RenderInfo.appIconMap.put(componentName, drawable);
                }
            } else {
                Drawable drawable2 = (Drawable) RenderInfo.iconMap.get(intValue3);
                if (drawable2 == null) {
                    drawable2 = context.getResources().getDrawable(intValue3, (Resources.Theme) null);
                    RenderInfo.iconMap.put(intValue3, drawable2);
                }
                drawable = drawable2;
            }
            Intrinsics.checkNotNull(drawable);
            Drawable newDrawable = drawable.getConstantState().newDrawable(context.getResources());
            Intrinsics.checkNotNullExpressionValue(newDrawable, "icon!!.constantState.new…awable(context.resources)");
            return new RenderInfo(newDrawable, intValue, intValue2);
        }

        public final void registerComponentIcon(ComponentName componentName, Drawable drawable) {
            Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
            Intrinsics.checkNotNullParameter(drawable, BaseIconCache.IconDB.COLUMN_ICON);
            RenderInfo.appIconMap.put(componentName, drawable);
        }

        public final void clearCache() {
            RenderInfo.iconMap.clear();
            RenderInfo.appIconMap.clear();
        }
    }
}
