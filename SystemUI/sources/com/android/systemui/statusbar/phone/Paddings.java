package com.android.systemui.statusbar.phone;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J'\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b¨\u0006\u0015"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/Paddings;", "", "containerPadding", "", "notificationsMargin", "qsContainerPadding", "(III)V", "getContainerPadding", "()I", "getNotificationsMargin", "getQsContainerPadding", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationsQSContainerController.kt */
final class Paddings {
    private final int containerPadding;
    private final int notificationsMargin;
    private final int qsContainerPadding;

    public static /* synthetic */ Paddings copy$default(Paddings paddings, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            i = paddings.containerPadding;
        }
        if ((i4 & 2) != 0) {
            i2 = paddings.notificationsMargin;
        }
        if ((i4 & 4) != 0) {
            i3 = paddings.qsContainerPadding;
        }
        return paddings.copy(i, i2, i3);
    }

    public final int component1() {
        return this.containerPadding;
    }

    public final int component2() {
        return this.notificationsMargin;
    }

    public final int component3() {
        return this.qsContainerPadding;
    }

    public final Paddings copy(int i, int i2, int i3) {
        return new Paddings(i, i2, i3);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Paddings)) {
            return false;
        }
        Paddings paddings = (Paddings) obj;
        return this.containerPadding == paddings.containerPadding && this.notificationsMargin == paddings.notificationsMargin && this.qsContainerPadding == paddings.qsContainerPadding;
    }

    public int hashCode() {
        return (((Integer.hashCode(this.containerPadding) * 31) + Integer.hashCode(this.notificationsMargin)) * 31) + Integer.hashCode(this.qsContainerPadding);
    }

    public String toString() {
        return "Paddings(containerPadding=" + this.containerPadding + ", notificationsMargin=" + this.notificationsMargin + ", qsContainerPadding=" + this.qsContainerPadding + ')';
    }

    public Paddings(int i, int i2, int i3) {
        this.containerPadding = i;
        this.notificationsMargin = i2;
        this.qsContainerPadding = i3;
    }

    public final int getContainerPadding() {
        return this.containerPadding;
    }

    public final int getNotificationsMargin() {
        return this.notificationsMargin;
    }

    public final int getQsContainerPadding() {
        return this.qsContainerPadding;
    }
}
