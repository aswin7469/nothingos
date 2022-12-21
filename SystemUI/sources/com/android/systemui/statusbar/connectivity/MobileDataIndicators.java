package com.android.systemui.statusbar.connectivity;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\u001a\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u0012\b\u0010\f\u001a\u0004\u0018\u00010\r\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\r\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\r\u0012\u0006\u0010\u0010\u001a\u00020\u0006\u0012\u0006\u0010\u0011\u001a\u00020\t\u0012\u0006\u0010\u0012\u001a\u00020\t\u0012\u0006\u0010\u0013\u001a\u00020\t¢\u0006\u0002\u0010\u0014J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\rHÆ\u0003J\t\u0010\u0017\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0018\u001a\u00020\tHÆ\u0003J\t\u0010\u0019\u001a\u00020\tHÆ\u0003J\t\u0010\u001a\u001a\u00020\tHÆ\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001e\u001a\u00020\tHÆ\u0003J\t\u0010\u001f\u001a\u00020\tHÆ\u0003J\t\u0010 \u001a\u00020\u0006HÆ\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\rHÆ\u0003J\u000b\u0010\"\u001a\u0004\u0018\u00010\rHÆ\u0003J\u0001\u0010#\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\u00062\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\r2\b\b\u0002\u0010\u0010\u001a\u00020\u00062\b\b\u0002\u0010\u0011\u001a\u00020\t2\b\b\u0002\u0010\u0012\u001a\u00020\t2\b\b\u0002\u0010\u0013\u001a\u00020\tHÆ\u0001J\u0013\u0010$\u001a\u00020\t2\b\u0010%\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010&\u001a\u00020\u0006HÖ\u0001J\b\u0010'\u001a\u00020(H\u0016R\u0010\u0010\b\u001a\u00020\t8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\t8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u0004\u0018\u00010\r8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\t8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u00020\t8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u00020\t8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u0004\u0018\u00010\r8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u0004\u0018\u00010\r8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006)"}, mo64987d2 = {"Lcom/android/systemui/statusbar/connectivity/MobileDataIndicators;", "", "statusIcon", "Lcom/android/systemui/statusbar/connectivity/IconState;", "qsIcon", "statusType", "", "qsType", "activityIn", "", "activityOut", "volteIcon", "typeContentDescription", "", "typeContentDescriptionHtml", "qsDescription", "subId", "roaming", "showTriangle", "showRat", "(Lcom/android/systemui/statusbar/connectivity/IconState;Lcom/android/systemui/statusbar/connectivity/IconState;IIZZILjava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;IZZZ)V", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SignalCallback.kt */
public final class MobileDataIndicators {
    public final boolean activityIn;
    public final boolean activityOut;
    public final CharSequence qsDescription;
    public final IconState qsIcon;
    public final int qsType;
    public final boolean roaming;
    public final boolean showRat;
    public final boolean showTriangle;
    public final IconState statusIcon;
    public final int statusType;
    public final int subId;
    public final CharSequence typeContentDescription;
    public final CharSequence typeContentDescriptionHtml;
    public final int volteIcon;

    public static /* synthetic */ MobileDataIndicators copy$default(MobileDataIndicators mobileDataIndicators, IconState iconState, IconState iconState2, int i, int i2, boolean z, boolean z2, int i3, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i4, boolean z3, boolean z4, boolean z5, int i5, Object obj) {
        MobileDataIndicators mobileDataIndicators2 = mobileDataIndicators;
        int i6 = i5;
        return mobileDataIndicators.copy((i6 & 1) != 0 ? mobileDataIndicators2.statusIcon : iconState, (i6 & 2) != 0 ? mobileDataIndicators2.qsIcon : iconState2, (i6 & 4) != 0 ? mobileDataIndicators2.statusType : i, (i6 & 8) != 0 ? mobileDataIndicators2.qsType : i2, (i6 & 16) != 0 ? mobileDataIndicators2.activityIn : z, (i6 & 32) != 0 ? mobileDataIndicators2.activityOut : z2, (i6 & 64) != 0 ? mobileDataIndicators2.volteIcon : i3, (i6 & 128) != 0 ? mobileDataIndicators2.typeContentDescription : charSequence, (i6 & 256) != 0 ? mobileDataIndicators2.typeContentDescriptionHtml : charSequence2, (i6 & 512) != 0 ? mobileDataIndicators2.qsDescription : charSequence3, (i6 & 1024) != 0 ? mobileDataIndicators2.subId : i4, (i6 & 2048) != 0 ? mobileDataIndicators2.roaming : z3, (i6 & 4096) != 0 ? mobileDataIndicators2.showTriangle : z4, (i6 & 8192) != 0 ? mobileDataIndicators2.showRat : z5);
    }

    public final IconState component1() {
        return this.statusIcon;
    }

    public final CharSequence component10() {
        return this.qsDescription;
    }

    public final int component11() {
        return this.subId;
    }

    public final boolean component12() {
        return this.roaming;
    }

    public final boolean component13() {
        return this.showTriangle;
    }

    public final boolean component14() {
        return this.showRat;
    }

    public final IconState component2() {
        return this.qsIcon;
    }

    public final int component3() {
        return this.statusType;
    }

    public final int component4() {
        return this.qsType;
    }

    public final boolean component5() {
        return this.activityIn;
    }

    public final boolean component6() {
        return this.activityOut;
    }

    public final int component7() {
        return this.volteIcon;
    }

    public final CharSequence component8() {
        return this.typeContentDescription;
    }

    public final CharSequence component9() {
        return this.typeContentDescriptionHtml;
    }

    public final MobileDataIndicators copy(IconState iconState, IconState iconState2, int i, int i2, boolean z, boolean z2, int i3, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i4, boolean z3, boolean z4, boolean z5) {
        return new MobileDataIndicators(iconState, iconState2, i, i2, z, z2, i3, charSequence, charSequence2, charSequence3, i4, z3, z4, z5);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MobileDataIndicators)) {
            return false;
        }
        MobileDataIndicators mobileDataIndicators = (MobileDataIndicators) obj;
        return Intrinsics.areEqual((Object) this.statusIcon, (Object) mobileDataIndicators.statusIcon) && Intrinsics.areEqual((Object) this.qsIcon, (Object) mobileDataIndicators.qsIcon) && this.statusType == mobileDataIndicators.statusType && this.qsType == mobileDataIndicators.qsType && this.activityIn == mobileDataIndicators.activityIn && this.activityOut == mobileDataIndicators.activityOut && this.volteIcon == mobileDataIndicators.volteIcon && Intrinsics.areEqual((Object) this.typeContentDescription, (Object) mobileDataIndicators.typeContentDescription) && Intrinsics.areEqual((Object) this.typeContentDescriptionHtml, (Object) mobileDataIndicators.typeContentDescriptionHtml) && Intrinsics.areEqual((Object) this.qsDescription, (Object) mobileDataIndicators.qsDescription) && this.subId == mobileDataIndicators.subId && this.roaming == mobileDataIndicators.roaming && this.showTriangle == mobileDataIndicators.showTriangle && this.showRat == mobileDataIndicators.showRat;
    }

    public int hashCode() {
        IconState iconState = this.statusIcon;
        int i = 0;
        int hashCode = (iconState == null ? 0 : iconState.hashCode()) * 31;
        IconState iconState2 = this.qsIcon;
        int hashCode2 = (((((hashCode + (iconState2 == null ? 0 : iconState2.hashCode())) * 31) + Integer.hashCode(this.statusType)) * 31) + Integer.hashCode(this.qsType)) * 31;
        boolean z = this.activityIn;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i2 = (hashCode2 + (z ? 1 : 0)) * 31;
        boolean z3 = this.activityOut;
        if (z3) {
            z3 = true;
        }
        int hashCode3 = (((i2 + (z3 ? 1 : 0)) * 31) + Integer.hashCode(this.volteIcon)) * 31;
        CharSequence charSequence = this.typeContentDescription;
        int hashCode4 = (hashCode3 + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
        CharSequence charSequence2 = this.typeContentDescriptionHtml;
        int hashCode5 = (hashCode4 + (charSequence2 == null ? 0 : charSequence2.hashCode())) * 31;
        CharSequence charSequence3 = this.qsDescription;
        if (charSequence3 != null) {
            i = charSequence3.hashCode();
        }
        int hashCode6 = (((hashCode5 + i) * 31) + Integer.hashCode(this.subId)) * 31;
        boolean z4 = this.roaming;
        if (z4) {
            z4 = true;
        }
        int i3 = (hashCode6 + (z4 ? 1 : 0)) * 31;
        boolean z5 = this.showTriangle;
        if (z5) {
            z5 = true;
        }
        int i4 = (i3 + (z5 ? 1 : 0)) * 31;
        boolean z6 = this.showRat;
        if (!z6) {
            z2 = z6;
        }
        return i4 + (z2 ? 1 : 0);
    }

    public MobileDataIndicators(IconState iconState, IconState iconState2, int i, int i2, boolean z, boolean z2, int i3, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i4, boolean z3, boolean z4, boolean z5) {
        this.statusIcon = iconState;
        this.qsIcon = iconState2;
        this.statusType = i;
        this.qsType = i2;
        this.activityIn = z;
        this.activityOut = z2;
        this.volteIcon = i3;
        this.typeContentDescription = charSequence;
        this.typeContentDescriptionHtml = charSequence2;
        this.qsDescription = charSequence3;
        this.subId = i4;
        this.roaming = z3;
        this.showTriangle = z4;
        this.showRat = z5;
    }

    public String toString() {
        String str;
        String iconState;
        StringBuilder sb = new StringBuilder("MobileDataIndicators[statusIcon=");
        IconState iconState2 = this.statusIcon;
        String str2 = "";
        if (iconState2 == null || (str = iconState2.toString()) == null) {
            str = str2;
        }
        StringBuilder append = sb.append(str).append(",qsIcon=");
        IconState iconState3 = this.qsIcon;
        if (!(iconState3 == null || (iconState = iconState3.toString()) == null)) {
            str2 = iconState;
        }
        String sb2 = append.append(str2).append(",statusType=").append(this.statusType).append(",qsType=").append(this.qsType).append(",activityIn=").append(this.activityIn).append(",activityOut=").append(this.activityOut).append(",volteIcon=").append(this.volteIcon).append(",typeContentDescription=").append(this.typeContentDescription).append(",typeContentDescriptionHtml=").append(this.typeContentDescriptionHtml).append(",description=").append(this.qsDescription).append(",subId=").append(this.subId).append(",roaming=").append(this.roaming).append(",showTriangle=").append(this.showTriangle).append(",showRat=").append(this.showRat).append(']').toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder(\"MobileDat…  .append(']').toString()");
        return sb2;
    }
}
