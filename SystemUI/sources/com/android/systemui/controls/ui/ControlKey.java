package com.android.systemui.controls.ui;

import android.content.ComponentName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlKey {
    @NotNull
    private final ComponentName componentName;
    @NotNull
    private final String controlId;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlKey)) {
            return false;
        }
        ControlKey controlKey = (ControlKey) obj;
        return Intrinsics.areEqual(this.componentName, controlKey.componentName) && Intrinsics.areEqual(this.controlId, controlKey.controlId);
    }

    public int hashCode() {
        return (this.componentName.hashCode() * 31) + this.controlId.hashCode();
    }

    @NotNull
    public String toString() {
        return "ControlKey(componentName=" + this.componentName + ", controlId=" + this.controlId + ')';
    }

    public ControlKey(@NotNull ComponentName componentName, @NotNull String controlId) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        this.componentName = componentName;
        this.controlId = controlId;
    }
}
