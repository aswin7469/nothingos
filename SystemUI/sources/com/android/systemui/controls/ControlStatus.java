package com.android.systemui.controls;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import android.service.controls.Control;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlStatus.kt */
/* loaded from: classes.dex */
public final class ControlStatus implements ControlInterface {
    @NotNull
    private final ComponentName component;
    @NotNull
    private final Control control;
    private boolean favorite;
    private final boolean removed;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlStatus)) {
            return false;
        }
        ControlStatus controlStatus = (ControlStatus) obj;
        return Intrinsics.areEqual(this.control, controlStatus.control) && Intrinsics.areEqual(getComponent(), controlStatus.getComponent()) && getFavorite() == controlStatus.getFavorite() && getRemoved() == controlStatus.getRemoved();
    }

    public int hashCode() {
        int hashCode = ((this.control.hashCode() * 31) + getComponent().hashCode()) * 31;
        boolean favorite = getFavorite();
        int i = 1;
        if (favorite) {
            favorite = true;
        }
        int i2 = favorite ? 1 : 0;
        int i3 = favorite ? 1 : 0;
        int i4 = (hashCode + i2) * 31;
        boolean removed = getRemoved();
        if (!removed) {
            i = removed;
        }
        return i4 + i;
    }

    @NotNull
    public String toString() {
        return "ControlStatus(control=" + this.control + ", component=" + getComponent() + ", favorite=" + getFavorite() + ", removed=" + getRemoved() + ')';
    }

    public ControlStatus(@NotNull Control control, @NotNull ComponentName component, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(control, "control");
        Intrinsics.checkNotNullParameter(component, "component");
        this.control = control;
        this.component = component;
        this.favorite = z;
        this.removed = z2;
    }

    public /* synthetic */ ControlStatus(Control control, ComponentName componentName, boolean z, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(control, componentName, z, (i & 8) != 0 ? false : z2);
    }

    @NotNull
    public final Control getControl() {
        return this.control;
    }

    @Override // com.android.systemui.controls.ControlInterface
    @NotNull
    public ComponentName getComponent() {
        return this.component;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean z) {
        this.favorite = z;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public boolean getRemoved() {
        return this.removed;
    }

    @Override // com.android.systemui.controls.ControlInterface
    @NotNull
    public String getControlId() {
        String controlId = this.control.getControlId();
        Intrinsics.checkNotNullExpressionValue(controlId, "control.controlId");
        return controlId;
    }

    @Override // com.android.systemui.controls.ControlInterface
    @NotNull
    public CharSequence getTitle() {
        CharSequence title = this.control.getTitle();
        Intrinsics.checkNotNullExpressionValue(title, "control.title");
        return title;
    }

    @Override // com.android.systemui.controls.ControlInterface
    @NotNull
    public CharSequence getSubtitle() {
        CharSequence subtitle = this.control.getSubtitle();
        Intrinsics.checkNotNullExpressionValue(subtitle, "control.subtitle");
        return subtitle;
    }

    @Override // com.android.systemui.controls.ControlInterface
    @Nullable
    public Icon getCustomIcon() {
        return this.control.getCustomIcon();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public int getDeviceType() {
        return this.control.getDeviceType();
    }
}
