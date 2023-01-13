package com.android.systemui.controls;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import android.service.controls.Control;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\r\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0002\b\u0003\b\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0005HÆ\u0003J\t\u0010)\u001a\u00020\u0007HÆ\u0003J\t\u0010*\u001a\u00020\u0007HÆ\u0003J1\u0010+\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0007HÆ\u0001J\u0013\u0010,\u001a\u00020\u00072\b\u0010-\u001a\u0004\u0018\u00010.HÖ\u0003J\t\u0010/\u001a\u00020\u0017HÖ\u0001J\t\u00100\u001a\u00020\u000fHÖ\u0001R\u0014\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000f8VX\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0012\u001a\u0004\u0018\u00010\u00138VX\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u00178VX\u0004¢\u0006\f\u0012\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u001a\u0010\u001bR\u001a\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0014\u0010\b\u001a\u00020\u0007X\u0004¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u001dR\u0014\u0010!\u001a\u00020\"8VX\u0004¢\u0006\u0006\u001a\u0004\b#\u0010$R\u0014\u0010%\u001a\u00020\"8VX\u0004¢\u0006\u0006\u001a\u0004\b&\u0010$¨\u00061"}, mo65043d2 = {"Lcom/android/systemui/controls/ControlStatus;", "Lcom/android/systemui/controls/ControlInterface;", "control", "Landroid/service/controls/Control;", "component", "Landroid/content/ComponentName;", "favorite", "", "removed", "(Landroid/service/controls/Control;Landroid/content/ComponentName;ZZ)V", "getComponent", "()Landroid/content/ComponentName;", "getControl", "()Landroid/service/controls/Control;", "controlId", "", "getControlId", "()Ljava/lang/String;", "customIcon", "Landroid/graphics/drawable/Icon;", "getCustomIcon", "()Landroid/graphics/drawable/Icon;", "deviceType", "", "getDeviceType$annotations", "()V", "getDeviceType", "()I", "getFavorite", "()Z", "setFavorite", "(Z)V", "getRemoved", "subtitle", "", "getSubtitle", "()Ljava/lang/CharSequence;", "title", "getTitle", "component1", "component2", "component3", "component4", "copy", "equals", "other", "", "hashCode", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlStatus.kt */
public final class ControlStatus implements ControlInterface {
    private final ComponentName component;
    private final Control control;
    private boolean favorite;
    private final boolean removed;

    public static /* synthetic */ ControlStatus copy$default(ControlStatus controlStatus, Control control2, ComponentName componentName, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            control2 = controlStatus.control;
        }
        if ((i & 2) != 0) {
            componentName = controlStatus.getComponent();
        }
        if ((i & 4) != 0) {
            z = controlStatus.getFavorite();
        }
        if ((i & 8) != 0) {
            z2 = controlStatus.getRemoved();
        }
        return controlStatus.copy(control2, componentName, z, z2);
    }

    public static /* synthetic */ void getDeviceType$annotations() {
    }

    public final Control component1() {
        return this.control;
    }

    public final ComponentName component2() {
        return getComponent();
    }

    public final boolean component3() {
        return getFavorite();
    }

    public final boolean component4() {
        return getRemoved();
    }

    public final ControlStatus copy(Control control2, ComponentName componentName, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(control2, "control");
        Intrinsics.checkNotNullParameter(componentName, "component");
        return new ControlStatus(control2, componentName, z, z2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlStatus)) {
            return false;
        }
        ControlStatus controlStatus = (ControlStatus) obj;
        return Intrinsics.areEqual((Object) this.control, (Object) controlStatus.control) && Intrinsics.areEqual((Object) getComponent(), (Object) controlStatus.getComponent()) && getFavorite() == controlStatus.getFavorite() && getRemoved() == controlStatus.getRemoved();
    }

    public int hashCode() {
        int hashCode = ((this.control.hashCode() * 31) + getComponent().hashCode()) * 31;
        boolean favorite2 = getFavorite();
        boolean z = true;
        if (favorite2) {
            favorite2 = true;
        }
        int i = (hashCode + (favorite2 ? 1 : 0)) * 31;
        boolean removed2 = getRemoved();
        if (!removed2) {
            z = removed2;
        }
        return i + (z ? 1 : 0);
    }

    public String toString() {
        return "ControlStatus(control=" + this.control + ", component=" + getComponent() + ", favorite=" + getFavorite() + ", removed=" + getRemoved() + ')';
    }

    public ControlStatus(Control control2, ComponentName componentName, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(control2, "control");
        Intrinsics.checkNotNullParameter(componentName, "component");
        this.control = control2;
        this.component = componentName;
        this.favorite = z;
        this.removed = z2;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ControlStatus(Control control2, ComponentName componentName, boolean z, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(control2, componentName, z, (i & 8) != 0 ? false : z2);
    }

    public final Control getControl() {
        return this.control;
    }

    public ComponentName getComponent() {
        return this.component;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean z) {
        this.favorite = z;
    }

    public boolean getRemoved() {
        return this.removed;
    }

    public String getControlId() {
        String controlId = this.control.getControlId();
        Intrinsics.checkNotNullExpressionValue(controlId, "control.controlId");
        return controlId;
    }

    public CharSequence getTitle() {
        CharSequence title = this.control.getTitle();
        Intrinsics.checkNotNullExpressionValue(title, "control.title");
        return title;
    }

    public CharSequence getSubtitle() {
        CharSequence subtitle = this.control.getSubtitle();
        Intrinsics.checkNotNullExpressionValue(subtitle, "control.subtitle");
        return subtitle;
    }

    public Icon getCustomIcon() {
        return this.control.getCustomIcon();
    }

    public int getDeviceType() {
        return this.control.getDeviceType();
    }
}
