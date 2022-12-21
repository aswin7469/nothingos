package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.ControlStatus;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\r\n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\u0003\b\b\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\t\u0010$\u001a\u00020\u0004HÆ\u0003J\u0013\u0010%\u001a\u00020\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u0004HÆ\u0001J\u0013\u0010&\u001a\u00020\u00192\b\u0010'\u001a\u0004\u0018\u00010(HÖ\u0003J\t\u0010)\u001a\u00020\u0015HÖ\u0001J\t\u0010*\u001a\u00020\u000bHÖ\u0001R\u0012\u0010\u0006\u001a\u00020\u0007X\u0005¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u000bX\u0005¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0005¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0012\u0010\u0014\u001a\u00020\u0015X\u0005¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0012\u0010\u0018\u001a\u00020\u0019X\u0005¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0014\u0010\u001c\u001a\u00020\u00198VX\u0005¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u001bR\u0012\u0010\u001e\u001a\u00020\u001fX\u0005¢\u0006\u0006\u001a\u0004\b \u0010!R\u0012\u0010\"\u001a\u00020\u001fX\u0005¢\u0006\u0006\u001a\u0004\b#\u0010!¨\u0006+"}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlStatusWrapper;", "Lcom/android/systemui/controls/management/ElementWrapper;", "Lcom/android/systemui/controls/ControlInterface;", "controlStatus", "Lcom/android/systemui/controls/ControlStatus;", "(Lcom/android/systemui/controls/ControlStatus;)V", "component", "Landroid/content/ComponentName;", "getComponent", "()Landroid/content/ComponentName;", "controlId", "", "getControlId", "()Ljava/lang/String;", "getControlStatus", "()Lcom/android/systemui/controls/ControlStatus;", "customIcon", "Landroid/graphics/drawable/Icon;", "getCustomIcon", "()Landroid/graphics/drawable/Icon;", "deviceType", "", "getDeviceType", "()I", "favorite", "", "getFavorite", "()Z", "removed", "getRemoved", "subtitle", "", "getSubtitle", "()Ljava/lang/CharSequence;", "title", "getTitle", "component1", "copy", "equals", "other", "", "hashCode", "toString", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsModel.kt */
public final class ControlStatusWrapper extends ElementWrapper implements ControlInterface {
    private final ControlStatus controlStatus;

    public static /* synthetic */ ControlStatusWrapper copy$default(ControlStatusWrapper controlStatusWrapper, ControlStatus controlStatus2, int i, Object obj) {
        if ((i & 1) != 0) {
            controlStatus2 = controlStatusWrapper.controlStatus;
        }
        return controlStatusWrapper.copy(controlStatus2);
    }

    public final ControlStatus component1() {
        return this.controlStatus;
    }

    public final ControlStatusWrapper copy(ControlStatus controlStatus2) {
        Intrinsics.checkNotNullParameter(controlStatus2, "controlStatus");
        return new ControlStatusWrapper(controlStatus2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ControlStatusWrapper) && Intrinsics.areEqual((Object) this.controlStatus, (Object) ((ControlStatusWrapper) obj).controlStatus);
    }

    public ComponentName getComponent() {
        return this.controlStatus.getComponent();
    }

    public String getControlId() {
        return this.controlStatus.getControlId();
    }

    public Icon getCustomIcon() {
        return this.controlStatus.getCustomIcon();
    }

    public int getDeviceType() {
        return this.controlStatus.getDeviceType();
    }

    public boolean getFavorite() {
        return this.controlStatus.getFavorite();
    }

    public boolean getRemoved() {
        return this.controlStatus.getRemoved();
    }

    public CharSequence getSubtitle() {
        return this.controlStatus.getSubtitle();
    }

    public CharSequence getTitle() {
        return this.controlStatus.getTitle();
    }

    public int hashCode() {
        return this.controlStatus.hashCode();
    }

    public String toString() {
        return "ControlStatusWrapper(controlStatus=" + this.controlStatus + ')';
    }

    public final ControlStatus getControlStatus() {
        return this.controlStatus;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlStatusWrapper(ControlStatus controlStatus2) {
        super((DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(controlStatus2, "controlStatus");
        this.controlStatus = controlStatus2;
    }
}
