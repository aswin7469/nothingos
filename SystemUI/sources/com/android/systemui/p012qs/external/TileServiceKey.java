package com.android.systemui.p012qs.external;

import android.content.ComponentName;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.settingslib.accessibility.AccessibilityUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0005HÖ\u0001J\b\u0010\u0014\u001a\u00020\nH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0015"}, mo65043d2 = {"Lcom/android/systemui/qs/external/TileServiceKey;", "", "componentName", "Landroid/content/ComponentName;", "user", "", "(Landroid/content/ComponentName;I)V", "getComponentName", "()Landroid/content/ComponentName;", "string", "", "getUser", "()I", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.external.TileServiceKey */
/* compiled from: CustomTileStatePersister.kt */
public final class TileServiceKey {
    private final ComponentName componentName;
    private final String string;
    private final int user;

    public static /* synthetic */ TileServiceKey copy$default(TileServiceKey tileServiceKey, ComponentName componentName2, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            componentName2 = tileServiceKey.componentName;
        }
        if ((i2 & 2) != 0) {
            i = tileServiceKey.user;
        }
        return tileServiceKey.copy(componentName2, i);
    }

    public final ComponentName component1() {
        return this.componentName;
    }

    public final int component2() {
        return this.user;
    }

    public final TileServiceKey copy(ComponentName componentName2, int i) {
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        return new TileServiceKey(componentName2, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TileServiceKey)) {
            return false;
        }
        TileServiceKey tileServiceKey = (TileServiceKey) obj;
        return Intrinsics.areEqual((Object) this.componentName, (Object) tileServiceKey.componentName) && this.user == tileServiceKey.user;
    }

    public int hashCode() {
        return (this.componentName.hashCode() * 31) + Integer.hashCode(this.user);
    }

    public TileServiceKey(ComponentName componentName2, int i) {
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        this.componentName = componentName2;
        this.user = i;
        this.string = componentName2.flattenToString() + AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + i;
    }

    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final int getUser() {
        return this.user;
    }

    public String toString() {
        return this.string;
    }
}
