package com.android.systemui.statusbar.connectivity;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0005HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0007HÆ\u0003J'\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\r\u001a\u00020\u00032\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÖ\u0001J\b\u0010\u0010\u001a\u00020\u0007H\u0016R\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/statusbar/connectivity/IconState;", "", "visible", "", "icon", "", "contentDescription", "", "(ZILjava/lang/String;)V", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SignalCallback.kt */
public final class IconState {
    public final String contentDescription;
    public final int icon;
    public final boolean visible;

    public static /* synthetic */ IconState copy$default(IconState iconState, boolean z, int i, String str, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            z = iconState.visible;
        }
        if ((i2 & 2) != 0) {
            i = iconState.icon;
        }
        if ((i2 & 4) != 0) {
            str = iconState.contentDescription;
        }
        return iconState.copy(z, i, str);
    }

    public final boolean component1() {
        return this.visible;
    }

    public final int component2() {
        return this.icon;
    }

    public final String component3() {
        return this.contentDescription;
    }

    public final IconState copy(boolean z, int i, String str) {
        Intrinsics.checkNotNullParameter(str, "contentDescription");
        return new IconState(z, i, str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IconState)) {
            return false;
        }
        IconState iconState = (IconState) obj;
        return this.visible == iconState.visible && this.icon == iconState.icon && Intrinsics.areEqual((Object) this.contentDescription, (Object) iconState.contentDescription);
    }

    public int hashCode() {
        boolean z = this.visible;
        if (z) {
            z = true;
        }
        return ((((z ? 1 : 0) * true) + Integer.hashCode(this.icon)) * 31) + this.contentDescription.hashCode();
    }

    public IconState(boolean z, int i, String str) {
        Intrinsics.checkNotNullParameter(str, "contentDescription");
        this.visible = z;
        this.icon = i;
        this.contentDescription = str;
    }

    public String toString() {
        String str = "[visible=" + this.visible + ",icon=" + this.icon + ",contentDescription=" + this.contentDescription + ']';
        Intrinsics.checkNotNullExpressionValue(str, "builder.append(\"[visible…              .toString()");
        return str;
    }
}
