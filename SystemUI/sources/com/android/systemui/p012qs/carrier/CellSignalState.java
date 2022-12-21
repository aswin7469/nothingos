package com.android.systemui.p012qs.carrier;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0011\b\b\u0018\u00002\u00020\u0001BE\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003¢\u0006\u0002\u0010\u000bJ\u000e\u0010\f\u001a\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0003J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0005HÆ\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003JI\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00032\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0007HÖ\u0001R\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u0004\u0018\u00010\u00078\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, mo64987d2 = {"Lcom/android/systemui/qs/carrier/CellSignalState;", "", "visible", "", "mobileSignalIconId", "", "contentDescription", "", "typeContentDescription", "roaming", "providerModelBehavior", "(ZILjava/lang/String;Ljava/lang/String;ZZ)V", "changeVisibility", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.carrier.CellSignalState */
/* compiled from: CellSignalState.kt */
public final class CellSignalState {
    public final String contentDescription;
    public final int mobileSignalIconId;
    public final boolean providerModelBehavior;
    public final boolean roaming;
    public final String typeContentDescription;
    public final boolean visible;

    public CellSignalState() {
        this(false, 0, (String) null, (String) null, false, false, 63, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ CellSignalState copy$default(CellSignalState cellSignalState, boolean z, int i, String str, String str2, boolean z2, boolean z3, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            z = cellSignalState.visible;
        }
        if ((i2 & 2) != 0) {
            i = cellSignalState.mobileSignalIconId;
        }
        int i3 = i;
        if ((i2 & 4) != 0) {
            str = cellSignalState.contentDescription;
        }
        String str3 = str;
        if ((i2 & 8) != 0) {
            str2 = cellSignalState.typeContentDescription;
        }
        String str4 = str2;
        if ((i2 & 16) != 0) {
            z2 = cellSignalState.roaming;
        }
        boolean z4 = z2;
        if ((i2 & 32) != 0) {
            z3 = cellSignalState.providerModelBehavior;
        }
        return cellSignalState.copy(z, i3, str3, str4, z4, z3);
    }

    public final boolean component1() {
        return this.visible;
    }

    public final int component2() {
        return this.mobileSignalIconId;
    }

    public final String component3() {
        return this.contentDescription;
    }

    public final String component4() {
        return this.typeContentDescription;
    }

    public final boolean component5() {
        return this.roaming;
    }

    public final boolean component6() {
        return this.providerModelBehavior;
    }

    public final CellSignalState copy(boolean z, int i, String str, String str2, boolean z2, boolean z3) {
        return new CellSignalState(z, i, str, str2, z2, z3);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CellSignalState)) {
            return false;
        }
        CellSignalState cellSignalState = (CellSignalState) obj;
        return this.visible == cellSignalState.visible && this.mobileSignalIconId == cellSignalState.mobileSignalIconId && Intrinsics.areEqual((Object) this.contentDescription, (Object) cellSignalState.contentDescription) && Intrinsics.areEqual((Object) this.typeContentDescription, (Object) cellSignalState.typeContentDescription) && this.roaming == cellSignalState.roaming && this.providerModelBehavior == cellSignalState.providerModelBehavior;
    }

    public int hashCode() {
        boolean z = this.visible;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int hashCode = (((z ? 1 : 0) * true) + Integer.hashCode(this.mobileSignalIconId)) * 31;
        String str = this.contentDescription;
        int i = 0;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.typeContentDescription;
        if (str2 != null) {
            i = str2.hashCode();
        }
        int i2 = (hashCode2 + i) * 31;
        boolean z3 = this.roaming;
        if (z3) {
            z3 = true;
        }
        int i3 = (i2 + (z3 ? 1 : 0)) * 31;
        boolean z4 = this.providerModelBehavior;
        if (!z4) {
            z2 = z4;
        }
        return i3 + (z2 ? 1 : 0);
    }

    public String toString() {
        return "CellSignalState(visible=" + this.visible + ", mobileSignalIconId=" + this.mobileSignalIconId + ", contentDescription=" + this.contentDescription + ", typeContentDescription=" + this.typeContentDescription + ", roaming=" + this.roaming + ", providerModelBehavior=" + this.providerModelBehavior + ')';
    }

    public CellSignalState(boolean z, int i, String str, String str2, boolean z2, boolean z3) {
        this.visible = z;
        this.mobileSignalIconId = i;
        this.contentDescription = str;
        this.typeContentDescription = str2;
        this.roaming = z2;
        this.providerModelBehavior = z3;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ CellSignalState(boolean z, int i, String str, String str2, boolean z2, boolean z3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? false : z, (i2 & 2) != 0 ? 0 : i, (i2 & 4) != 0 ? null : str, (i2 & 8) != 0 ? null : str2, (i2 & 16) != 0 ? false : z2, (i2 & 32) != 0 ? false : z3);
    }

    public final CellSignalState changeVisibility(boolean z) {
        if (this.visible == z) {
            return this;
        }
        return copy$default(this, z, 0, (String) null, (String) null, false, false, 62, (Object) null);
    }
}
