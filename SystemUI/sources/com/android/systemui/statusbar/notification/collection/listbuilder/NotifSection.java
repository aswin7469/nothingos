package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0004\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010!\u001a\u00020\u0005HÖ\u0001J\t\u0010\"\u001a\u00020\u0016HÖ\u0001R\u0017\u0010\u0007\u001a\u00020\u0005¢\u0006\u000e\n\u0000\u0012\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000bR\u0013\u0010\f\u001a\u0004\u0018\u00010\r¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0011¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000bR\u0011\u0010\u0015\u001a\u00020\u00168F¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a¨\u0006#"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;", "", "sectioner", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "index", "", "(Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;I)V", "bucket", "getBucket$annotations", "()V", "getBucket", "()I", "comparator", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifComparator;", "getComparator", "()Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifComparator;", "headerController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "getHeaderController", "()Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "getIndex", "label", "", "getLabel", "()Ljava/lang/String;", "getSectioner", "()Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifSection.kt */
public final class NotifSection {
    private final int bucket;
    private final NotifComparator comparator;
    private final NodeController headerController;
    private final int index;
    private final NotifSectioner sectioner;

    public static /* synthetic */ NotifSection copy$default(NotifSection notifSection, NotifSectioner notifSectioner, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            notifSectioner = notifSection.sectioner;
        }
        if ((i2 & 2) != 0) {
            i = notifSection.index;
        }
        return notifSection.copy(notifSectioner, i);
    }

    public static /* synthetic */ void getBucket$annotations() {
    }

    public final NotifSectioner component1() {
        return this.sectioner;
    }

    public final int component2() {
        return this.index;
    }

    public final NotifSection copy(NotifSectioner notifSectioner, int i) {
        Intrinsics.checkNotNullParameter(notifSectioner, "sectioner");
        return new NotifSection(notifSectioner, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NotifSection)) {
            return false;
        }
        NotifSection notifSection = (NotifSection) obj;
        return Intrinsics.areEqual((Object) this.sectioner, (Object) notifSection.sectioner) && this.index == notifSection.index;
    }

    public int hashCode() {
        return (this.sectioner.hashCode() * 31) + Integer.hashCode(this.index);
    }

    public String toString() {
        return "NotifSection(sectioner=" + this.sectioner + ", index=" + this.index + ')';
    }

    public NotifSection(NotifSectioner notifSectioner, int i) {
        Intrinsics.checkNotNullParameter(notifSectioner, "sectioner");
        this.sectioner = notifSectioner;
        this.index = i;
        this.headerController = notifSectioner.getHeaderNodeController();
        this.comparator = notifSectioner.getComparator();
        this.bucket = notifSectioner.getBucket();
    }

    public final NotifSectioner getSectioner() {
        return this.sectioner;
    }

    public final int getIndex() {
        return this.index;
    }

    public final String getLabel() {
        return "Section(" + this.index + ", " + this.bucket + ", \"" + this.sectioner.getName() + "\")";
    }

    public final NodeController getHeaderController() {
        return this.headerController;
    }

    public final NotifComparator getComparator() {
        return this.comparator;
    }

    public final int getBucket() {
        return this.bucket;
    }
}
