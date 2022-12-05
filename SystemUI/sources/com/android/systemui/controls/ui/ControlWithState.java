package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.service.controls.Control;
import com.android.systemui.controls.controller.ControlInfo;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlWithState.kt */
/* loaded from: classes.dex */
public final class ControlWithState {
    @NotNull
    private final ControlInfo ci;
    @NotNull
    private final ComponentName componentName;
    @Nullable
    private final Control control;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlWithState)) {
            return false;
        }
        ControlWithState controlWithState = (ControlWithState) obj;
        return Intrinsics.areEqual(this.componentName, controlWithState.componentName) && Intrinsics.areEqual(this.ci, controlWithState.ci) && Intrinsics.areEqual(this.control, controlWithState.control);
    }

    public int hashCode() {
        int hashCode = ((this.componentName.hashCode() * 31) + this.ci.hashCode()) * 31;
        Control control = this.control;
        return hashCode + (control == null ? 0 : control.hashCode());
    }

    @NotNull
    public String toString() {
        return "ControlWithState(componentName=" + this.componentName + ", ci=" + this.ci + ", control=" + this.control + ')';
    }

    public ControlWithState(@NotNull ComponentName componentName, @NotNull ControlInfo ci, @Nullable Control control) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(ci, "ci");
        this.componentName = componentName;
        this.ci = ci;
        this.control = control;
    }

    @NotNull
    public final ComponentName getComponentName() {
        return this.componentName;
    }

    @NotNull
    public final ControlInfo getCi() {
        return this.ci;
    }

    @Nullable
    public final Control getControl() {
        return this.control;
    }
}
