package com.android.systemui.statusbar.connectivity;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001BM\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\b\u0010\f\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\rJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u0013\u001a\u0004\u0018\u00010\nHÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\nHÆ\u0003Ja\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\nHÆ\u0001J\u0013\u0010\u0017\u001a\u00020\u00032\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001J\b\u0010\u001b\u001a\u00020\nH\u0016R\u0010\u0010\u0007\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u0004\u0018\u00010\n8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00058\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u0004\u0018\u00010\n8\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, mo65043d2 = {"Lcom/android/systemui/statusbar/connectivity/WifiIndicators;", "", "enabled", "", "statusIcon", "Lcom/android/systemui/statusbar/connectivity/IconState;", "qsIcon", "activityIn", "activityOut", "description", "", "isTransient", "statusLabel", "(ZLcom/android/systemui/statusbar/connectivity/IconState;Lcom/android/systemui/statusbar/connectivity/IconState;ZZLjava/lang/String;ZLjava/lang/String;)V", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SignalCallback.kt */
public final class WifiIndicators {
    public final boolean activityIn;
    public final boolean activityOut;
    public final String description;
    public final boolean enabled;
    public final boolean isTransient;
    public final IconState qsIcon;
    public final IconState statusIcon;
    public final String statusLabel;

    public static /* synthetic */ WifiIndicators copy$default(WifiIndicators wifiIndicators, boolean z, IconState iconState, IconState iconState2, boolean z2, boolean z3, String str, boolean z4, String str2, int i, Object obj) {
        WifiIndicators wifiIndicators2 = wifiIndicators;
        int i2 = i;
        return wifiIndicators.copy((i2 & 1) != 0 ? wifiIndicators2.enabled : z, (i2 & 2) != 0 ? wifiIndicators2.statusIcon : iconState, (i2 & 4) != 0 ? wifiIndicators2.qsIcon : iconState2, (i2 & 8) != 0 ? wifiIndicators2.activityIn : z2, (i2 & 16) != 0 ? wifiIndicators2.activityOut : z3, (i2 & 32) != 0 ? wifiIndicators2.description : str, (i2 & 64) != 0 ? wifiIndicators2.isTransient : z4, (i2 & 128) != 0 ? wifiIndicators2.statusLabel : str2);
    }

    public final boolean component1() {
        return this.enabled;
    }

    public final IconState component2() {
        return this.statusIcon;
    }

    public final IconState component3() {
        return this.qsIcon;
    }

    public final boolean component4() {
        return this.activityIn;
    }

    public final boolean component5() {
        return this.activityOut;
    }

    public final String component6() {
        return this.description;
    }

    public final boolean component7() {
        return this.isTransient;
    }

    public final String component8() {
        return this.statusLabel;
    }

    public final WifiIndicators copy(boolean z, IconState iconState, IconState iconState2, boolean z2, boolean z3, String str, boolean z4, String str2) {
        return new WifiIndicators(z, iconState, iconState2, z2, z3, str, z4, str2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiIndicators)) {
            return false;
        }
        WifiIndicators wifiIndicators = (WifiIndicators) obj;
        return this.enabled == wifiIndicators.enabled && Intrinsics.areEqual((Object) this.statusIcon, (Object) wifiIndicators.statusIcon) && Intrinsics.areEqual((Object) this.qsIcon, (Object) wifiIndicators.qsIcon) && this.activityIn == wifiIndicators.activityIn && this.activityOut == wifiIndicators.activityOut && Intrinsics.areEqual((Object) this.description, (Object) wifiIndicators.description) && this.isTransient == wifiIndicators.isTransient && Intrinsics.areEqual((Object) this.statusLabel, (Object) wifiIndicators.statusLabel);
    }

    public int hashCode() {
        boolean z = this.enabled;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (z ? 1 : 0) * true;
        IconState iconState = this.statusIcon;
        int i2 = 0;
        int hashCode = (i + (iconState == null ? 0 : iconState.hashCode())) * 31;
        IconState iconState2 = this.qsIcon;
        int hashCode2 = (hashCode + (iconState2 == null ? 0 : iconState2.hashCode())) * 31;
        boolean z3 = this.activityIn;
        if (z3) {
            z3 = true;
        }
        int i3 = (hashCode2 + (z3 ? 1 : 0)) * 31;
        boolean z4 = this.activityOut;
        if (z4) {
            z4 = true;
        }
        int i4 = (i3 + (z4 ? 1 : 0)) * 31;
        String str = this.description;
        int hashCode3 = (i4 + (str == null ? 0 : str.hashCode())) * 31;
        boolean z5 = this.isTransient;
        if (!z5) {
            z2 = z5;
        }
        int i5 = (hashCode3 + (z2 ? 1 : 0)) * 31;
        String str2 = this.statusLabel;
        if (str2 != null) {
            i2 = str2.hashCode();
        }
        return i5 + i2;
    }

    public WifiIndicators(boolean z, IconState iconState, IconState iconState2, boolean z2, boolean z3, String str, boolean z4, String str2) {
        this.enabled = z;
        this.statusIcon = iconState;
        this.qsIcon = iconState2;
        this.activityIn = z2;
        this.activityOut = z3;
        this.description = str;
        this.isTransient = z4;
        this.statusLabel = str2;
    }

    public String toString() {
        String str;
        String iconState;
        StringBuilder append = new StringBuilder("WifiIndicators[enabled=").append(this.enabled).append(",statusIcon=");
        IconState iconState2 = this.statusIcon;
        String str2 = "";
        if (iconState2 == null || (str = iconState2.toString()) == null) {
            str = str2;
        }
        StringBuilder append2 = append.append(str).append(",qsIcon=");
        IconState iconState3 = this.qsIcon;
        if (!(iconState3 == null || (iconState = iconState3.toString()) == null)) {
            str2 = iconState;
        }
        String sb = append2.append(str2).append(",activityIn=").append(this.activityIn).append(",activityOut=").append(this.activityOut).append(",qsDescription=").append(this.description).append(",isTransient=").append(this.isTransient).append(",statusLabel=").append(this.statusLabel).append(']').toString();
        Intrinsics.checkNotNullExpressionValue(sb, "StringBuilder(\"WifiIndic…  .append(']').toString()");
        return sb;
    }
}
