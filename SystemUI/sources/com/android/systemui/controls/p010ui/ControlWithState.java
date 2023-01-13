package com.android.systemui.controls.p010ui;

import android.content.ComponentName;
import android.service.controls.Control;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0007HÆ\u0003J)\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001a"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/ControlWithState;", "", "componentName", "Landroid/content/ComponentName;", "ci", "Lcom/android/systemui/controls/controller/ControlInfo;", "control", "Landroid/service/controls/Control;", "(Landroid/content/ComponentName;Lcom/android/systemui/controls/controller/ControlInfo;Landroid/service/controls/Control;)V", "getCi", "()Lcom/android/systemui/controls/controller/ControlInfo;", "getComponentName", "()Landroid/content/ComponentName;", "getControl", "()Landroid/service/controls/Control;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlWithState */
/* compiled from: ControlWithState.kt */
public final class ControlWithState {

    /* renamed from: ci */
    private final ControlInfo f299ci;
    private final ComponentName componentName;
    private final Control control;

    public static /* synthetic */ ControlWithState copy$default(ControlWithState controlWithState, ComponentName componentName2, ControlInfo controlInfo, Control control2, int i, Object obj) {
        if ((i & 1) != 0) {
            componentName2 = controlWithState.componentName;
        }
        if ((i & 2) != 0) {
            controlInfo = controlWithState.f299ci;
        }
        if ((i & 4) != 0) {
            control2 = controlWithState.control;
        }
        return controlWithState.copy(componentName2, controlInfo, control2);
    }

    public final ComponentName component1() {
        return this.componentName;
    }

    public final ControlInfo component2() {
        return this.f299ci;
    }

    public final Control component3() {
        return this.control;
    }

    public final ControlWithState copy(ComponentName componentName2, ControlInfo controlInfo, Control control2) {
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(controlInfo, "ci");
        return new ControlWithState(componentName2, controlInfo, control2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlWithState)) {
            return false;
        }
        ControlWithState controlWithState = (ControlWithState) obj;
        return Intrinsics.areEqual((Object) this.componentName, (Object) controlWithState.componentName) && Intrinsics.areEqual((Object) this.f299ci, (Object) controlWithState.f299ci) && Intrinsics.areEqual((Object) this.control, (Object) controlWithState.control);
    }

    public int hashCode() {
        int hashCode = ((this.componentName.hashCode() * 31) + this.f299ci.hashCode()) * 31;
        Control control2 = this.control;
        return hashCode + (control2 == null ? 0 : control2.hashCode());
    }

    public String toString() {
        return "ControlWithState(componentName=" + this.componentName + ", ci=" + this.f299ci + ", control=" + this.control + ')';
    }

    public ControlWithState(ComponentName componentName2, ControlInfo controlInfo, Control control2) {
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(controlInfo, "ci");
        this.componentName = componentName2;
        this.f299ci = controlInfo;
        this.control = control2;
    }

    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final ControlInfo getCi() {
        return this.f299ci;
    }

    public final Control getControl() {
        return this.control;
    }
}
