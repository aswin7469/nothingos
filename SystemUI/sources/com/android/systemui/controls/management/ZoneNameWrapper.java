package com.android.systemui.controls.management;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ZoneNameWrapper;", "Lcom/android/systemui/controls/management/ElementWrapper;", "zoneName", "", "(Ljava/lang/CharSequence;)V", "getZoneName", "()Ljava/lang/CharSequence;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsModel.kt */
public final class ZoneNameWrapper extends ElementWrapper {
    private final CharSequence zoneName;

    public static /* synthetic */ ZoneNameWrapper copy$default(ZoneNameWrapper zoneNameWrapper, CharSequence charSequence, int i, Object obj) {
        if ((i & 1) != 0) {
            charSequence = zoneNameWrapper.zoneName;
        }
        return zoneNameWrapper.copy(charSequence);
    }

    public final CharSequence component1() {
        return this.zoneName;
    }

    public final ZoneNameWrapper copy(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "zoneName");
        return new ZoneNameWrapper(charSequence);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ZoneNameWrapper) && Intrinsics.areEqual((Object) this.zoneName, (Object) ((ZoneNameWrapper) obj).zoneName);
    }

    public int hashCode() {
        return this.zoneName.hashCode();
    }

    public String toString() {
        return "ZoneNameWrapper(zoneName=" + this.zoneName + ')';
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ZoneNameWrapper(CharSequence charSequence) {
        super((DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(charSequence, "zoneName");
        this.zoneName = charSequence;
    }

    public final CharSequence getZoneName() {
        return this.zoneName;
    }
}
