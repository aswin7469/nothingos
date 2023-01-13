package com.android.systemui.statusbar.phone.panelstate;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003¢\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J1\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\f¨\u0006\u001a"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionChangeEvent;", "", "fraction", "", "expanded", "", "tracking", "dragDownPxAmount", "(FZZF)V", "getDragDownPxAmount", "()F", "getExpanded", "()Z", "getFraction", "getTracking", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PanelExpansionChangeEvent.kt */
public final class PanelExpansionChangeEvent {
    private final float dragDownPxAmount;
    private final boolean expanded;
    private final float fraction;
    private final boolean tracking;

    public static /* synthetic */ PanelExpansionChangeEvent copy$default(PanelExpansionChangeEvent panelExpansionChangeEvent, float f, boolean z, boolean z2, float f2, int i, Object obj) {
        if ((i & 1) != 0) {
            f = panelExpansionChangeEvent.fraction;
        }
        if ((i & 2) != 0) {
            z = panelExpansionChangeEvent.expanded;
        }
        if ((i & 4) != 0) {
            z2 = panelExpansionChangeEvent.tracking;
        }
        if ((i & 8) != 0) {
            f2 = panelExpansionChangeEvent.dragDownPxAmount;
        }
        return panelExpansionChangeEvent.copy(f, z, z2, f2);
    }

    public final float component1() {
        return this.fraction;
    }

    public final boolean component2() {
        return this.expanded;
    }

    public final boolean component3() {
        return this.tracking;
    }

    public final float component4() {
        return this.dragDownPxAmount;
    }

    public final PanelExpansionChangeEvent copy(float f, boolean z, boolean z2, float f2) {
        return new PanelExpansionChangeEvent(f, z, z2, f2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PanelExpansionChangeEvent)) {
            return false;
        }
        PanelExpansionChangeEvent panelExpansionChangeEvent = (PanelExpansionChangeEvent) obj;
        return Intrinsics.areEqual((Object) Float.valueOf(this.fraction), (Object) Float.valueOf(panelExpansionChangeEvent.fraction)) && this.expanded == panelExpansionChangeEvent.expanded && this.tracking == panelExpansionChangeEvent.tracking && Intrinsics.areEqual((Object) Float.valueOf(this.dragDownPxAmount), (Object) Float.valueOf(panelExpansionChangeEvent.dragDownPxAmount));
    }

    public int hashCode() {
        int hashCode = Float.hashCode(this.fraction) * 31;
        boolean z = this.expanded;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (hashCode + (z ? 1 : 0)) * 31;
        boolean z3 = this.tracking;
        if (!z3) {
            z2 = z3;
        }
        return ((i + (z2 ? 1 : 0)) * 31) + Float.hashCode(this.dragDownPxAmount);
    }

    public String toString() {
        return "PanelExpansionChangeEvent(fraction=" + this.fraction + ", expanded=" + this.expanded + ", tracking=" + this.tracking + ", dragDownPxAmount=" + this.dragDownPxAmount + ')';
    }

    public PanelExpansionChangeEvent(float f, boolean z, boolean z2, float f2) {
        this.fraction = f;
        this.expanded = z;
        this.tracking = z2;
        this.dragDownPxAmount = f2;
    }

    public final float getFraction() {
        return this.fraction;
    }

    public final boolean getExpanded() {
        return this.expanded;
    }

    public final boolean getTracking() {
        return this.tracking;
    }

    public final float getDragDownPxAmount() {
        return this.dragDownPxAmount;
    }
}
