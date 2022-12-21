package com.android.systemui.controls.management;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00032\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001R\u001a\u0010\u0004\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\t¨\u0006\u0016"}, mo64987d2 = {"Lcom/android/systemui/controls/management/DividerWrapper;", "Lcom/android/systemui/controls/management/ElementWrapper;", "showNone", "", "showDivider", "(ZZ)V", "getShowDivider", "()Z", "setShowDivider", "(Z)V", "getShowNone", "setShowNone", "component1", "component2", "copy", "equals", "other", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsModel.kt */
public final class DividerWrapper extends ElementWrapper {
    private boolean showDivider;
    private boolean showNone;

    public DividerWrapper() {
        this(false, false, 3, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ DividerWrapper copy$default(DividerWrapper dividerWrapper, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = dividerWrapper.showNone;
        }
        if ((i & 2) != 0) {
            z2 = dividerWrapper.showDivider;
        }
        return dividerWrapper.copy(z, z2);
    }

    public final boolean component1() {
        return this.showNone;
    }

    public final boolean component2() {
        return this.showDivider;
    }

    public final DividerWrapper copy(boolean z, boolean z2) {
        return new DividerWrapper(z, z2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DividerWrapper)) {
            return false;
        }
        DividerWrapper dividerWrapper = (DividerWrapper) obj;
        return this.showNone == dividerWrapper.showNone && this.showDivider == dividerWrapper.showDivider;
    }

    public int hashCode() {
        boolean z = this.showNone;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (z ? 1 : 0) * true;
        boolean z3 = this.showDivider;
        if (!z3) {
            z2 = z3;
        }
        return i + (z2 ? 1 : 0);
    }

    public String toString() {
        return "DividerWrapper(showNone=" + this.showNone + ", showDivider=" + this.showDivider + ')';
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ DividerWrapper(boolean z, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? false : z, (i & 2) != 0 ? false : z2);
    }

    public final boolean getShowNone() {
        return this.showNone;
    }

    public final void setShowNone(boolean z) {
        this.showNone = z;
    }

    public final boolean getShowDivider() {
        return this.showDivider;
    }

    public final void setShowDivider(boolean z) {
        this.showDivider = z;
    }

    public DividerWrapper(boolean z, boolean z2) {
        super((DefaultConstructorMarker) null);
        this.showNone = z;
        this.showDivider = z2;
    }
}
