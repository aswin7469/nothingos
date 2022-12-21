package com.android.systemui.statusbar.notification.collection.render;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0010\u000e\n\u0002\b\u0002\b\b\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005¢\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0005HÆ\u0003J;\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0017\u001a\u00020\u00052\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001d"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NotifStats;", "", "numActiveNotifs", "", "hasNonClearableAlertingNotifs", "", "hasClearableAlertingNotifs", "hasNonClearableSilentNotifs", "hasClearableSilentNotifs", "(IZZZZ)V", "getHasClearableAlertingNotifs", "()Z", "getHasClearableSilentNotifs", "getHasNonClearableAlertingNotifs", "getHasNonClearableSilentNotifs", "getNumActiveNotifs", "()I", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifStackController.kt */
public final class NotifStats {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final NotifStats empty = new NotifStats(0, false, false, false, false);
    private final boolean hasClearableAlertingNotifs;
    private final boolean hasClearableSilentNotifs;
    private final boolean hasNonClearableAlertingNotifs;
    private final boolean hasNonClearableSilentNotifs;
    private final int numActiveNotifs;

    public static /* synthetic */ NotifStats copy$default(NotifStats notifStats, int i, boolean z, boolean z2, boolean z3, boolean z4, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = notifStats.numActiveNotifs;
        }
        if ((i2 & 2) != 0) {
            z = notifStats.hasNonClearableAlertingNotifs;
        }
        boolean z5 = z;
        if ((i2 & 4) != 0) {
            z2 = notifStats.hasClearableAlertingNotifs;
        }
        boolean z6 = z2;
        if ((i2 & 8) != 0) {
            z3 = notifStats.hasNonClearableSilentNotifs;
        }
        boolean z7 = z3;
        if ((i2 & 16) != 0) {
            z4 = notifStats.hasClearableSilentNotifs;
        }
        return notifStats.copy(i, z5, z6, z7, z4);
    }

    public static final NotifStats getEmpty() {
        return Companion.getEmpty();
    }

    public final int component1() {
        return this.numActiveNotifs;
    }

    public final boolean component2() {
        return this.hasNonClearableAlertingNotifs;
    }

    public final boolean component3() {
        return this.hasClearableAlertingNotifs;
    }

    public final boolean component4() {
        return this.hasNonClearableSilentNotifs;
    }

    public final boolean component5() {
        return this.hasClearableSilentNotifs;
    }

    public final NotifStats copy(int i, boolean z, boolean z2, boolean z3, boolean z4) {
        return new NotifStats(i, z, z2, z3, z4);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NotifStats)) {
            return false;
        }
        NotifStats notifStats = (NotifStats) obj;
        return this.numActiveNotifs == notifStats.numActiveNotifs && this.hasNonClearableAlertingNotifs == notifStats.hasNonClearableAlertingNotifs && this.hasClearableAlertingNotifs == notifStats.hasClearableAlertingNotifs && this.hasNonClearableSilentNotifs == notifStats.hasNonClearableSilentNotifs && this.hasClearableSilentNotifs == notifStats.hasClearableSilentNotifs;
    }

    public int hashCode() {
        int hashCode = Integer.hashCode(this.numActiveNotifs) * 31;
        boolean z = this.hasNonClearableAlertingNotifs;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (hashCode + (z ? 1 : 0)) * 31;
        boolean z3 = this.hasClearableAlertingNotifs;
        if (z3) {
            z3 = true;
        }
        int i2 = (i + (z3 ? 1 : 0)) * 31;
        boolean z4 = this.hasNonClearableSilentNotifs;
        if (z4) {
            z4 = true;
        }
        int i3 = (i2 + (z4 ? 1 : 0)) * 31;
        boolean z5 = this.hasClearableSilentNotifs;
        if (!z5) {
            z2 = z5;
        }
        return i3 + (z2 ? 1 : 0);
    }

    public String toString() {
        return "NotifStats(numActiveNotifs=" + this.numActiveNotifs + ", hasNonClearableAlertingNotifs=" + this.hasNonClearableAlertingNotifs + ", hasClearableAlertingNotifs=" + this.hasClearableAlertingNotifs + ", hasNonClearableSilentNotifs=" + this.hasNonClearableSilentNotifs + ", hasClearableSilentNotifs=" + this.hasClearableSilentNotifs + ')';
    }

    public NotifStats(int i, boolean z, boolean z2, boolean z3, boolean z4) {
        this.numActiveNotifs = i;
        this.hasNonClearableAlertingNotifs = z;
        this.hasClearableAlertingNotifs = z2;
        this.hasNonClearableSilentNotifs = z3;
        this.hasClearableSilentNotifs = z4;
    }

    public final int getNumActiveNotifs() {
        return this.numActiveNotifs;
    }

    public final boolean getHasNonClearableAlertingNotifs() {
        return this.hasNonClearableAlertingNotifs;
    }

    public final boolean getHasClearableAlertingNotifs() {
        return this.hasClearableAlertingNotifs;
    }

    public final boolean getHasNonClearableSilentNotifs() {
        return this.hasNonClearableSilentNotifs;
    }

    public final boolean getHasClearableSilentNotifs() {
        return this.hasClearableSilentNotifs;
    }

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u00020\u00048\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NotifStats$Companion;", "", "()V", "empty", "Lcom/android/systemui/statusbar/notification/collection/render/NotifStats;", "getEmpty$annotations", "getEmpty", "()Lcom/android/systemui/statusbar/notification/collection/render/NotifStats;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NotifStackController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public static /* synthetic */ void getEmpty$annotations() {
        }

        private Companion() {
        }

        public final NotifStats getEmpty() {
            return NotifStats.empty;
        }
    }
}
