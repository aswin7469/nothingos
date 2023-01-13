package com.android.systemui.controls.p010ui;

import android.content.ComponentName;
import com.android.launcher3.icons.cache.BaseIconCache;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0014"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/ControlKey;", "", "componentName", "Landroid/content/ComponentName;", "controlId", "", "(Landroid/content/ComponentName;Ljava/lang/String;)V", "getComponentName", "()Landroid/content/ComponentName;", "getControlId", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlKey */
/* compiled from: ControlsUiControllerImpl.kt */
final class ControlKey {
    private final ComponentName componentName;
    private final String controlId;

    public static /* synthetic */ ControlKey copy$default(ControlKey controlKey, ComponentName componentName2, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            componentName2 = controlKey.componentName;
        }
        if ((i & 2) != 0) {
            str = controlKey.controlId;
        }
        return controlKey.copy(componentName2, str);
    }

    public final ComponentName component1() {
        return this.componentName;
    }

    public final String component2() {
        return this.controlId;
    }

    public final ControlKey copy(ComponentName componentName2, String str) {
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(str, "controlId");
        return new ControlKey(componentName2, str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlKey)) {
            return false;
        }
        ControlKey controlKey = (ControlKey) obj;
        return Intrinsics.areEqual((Object) this.componentName, (Object) controlKey.componentName) && Intrinsics.areEqual((Object) this.controlId, (Object) controlKey.controlId);
    }

    public int hashCode() {
        return (this.componentName.hashCode() * 31) + this.controlId.hashCode();
    }

    public String toString() {
        return "ControlKey(componentName=" + this.componentName + ", controlId=" + this.controlId + ')';
    }

    public ControlKey(ComponentName componentName2, String str) {
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(str, "controlId");
        this.componentName = componentName2;
        this.controlId = str;
    }

    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final String getControlId() {
        return this.controlId;
    }
}
