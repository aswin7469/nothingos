package com.android.systemui.statusbar.notification.collection;

import android.icu.text.PluralRules;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\b\u0018\u0000 \"2\u00020\u0001:\u0001\"B#\b\u0002\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0000J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0007HÆ\u0003J+\u0010\u001b\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u001c\u001a\u00020\u00072\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u001eHÖ\u0001J\u0006\u0010\u001f\u001a\u00020\u0016J\t\u0010 \u001a\u00020!HÖ\u0001R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u0006#"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/SuppressedAttachState;", "", "section", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;", "parent", "Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "wasPruneSuppressed", "", "(Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;Lcom/android/systemui/statusbar/notification/collection/GroupEntry;Z)V", "getParent", "()Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "setParent", "(Lcom/android/systemui/statusbar/notification/collection/GroupEntry;)V", "getSection", "()Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;", "setSection", "(Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;)V", "getWasPruneSuppressed", "()Z", "setWasPruneSuppressed", "(Z)V", "clone", "", "other", "component1", "component2", "component3", "copy", "equals", "hashCode", "", "reset", "toString", "", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SuppressedAttachState.kt */
public final class SuppressedAttachState {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private GroupEntry parent;
    private NotifSection section;
    private boolean wasPruneSuppressed;

    public /* synthetic */ SuppressedAttachState(NotifSection notifSection, GroupEntry groupEntry, boolean z, DefaultConstructorMarker defaultConstructorMarker) {
        this(notifSection, groupEntry, z);
    }

    public static /* synthetic */ SuppressedAttachState copy$default(SuppressedAttachState suppressedAttachState, NotifSection notifSection, GroupEntry groupEntry, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            notifSection = suppressedAttachState.section;
        }
        if ((i & 2) != 0) {
            groupEntry = suppressedAttachState.parent;
        }
        if ((i & 4) != 0) {
            z = suppressedAttachState.wasPruneSuppressed;
        }
        return suppressedAttachState.copy(notifSection, groupEntry, z);
    }

    @JvmStatic
    public static final SuppressedAttachState create() {
        return Companion.create();
    }

    public final NotifSection component1() {
        return this.section;
    }

    public final GroupEntry component2() {
        return this.parent;
    }

    public final boolean component3() {
        return this.wasPruneSuppressed;
    }

    public final SuppressedAttachState copy(NotifSection notifSection, GroupEntry groupEntry, boolean z) {
        return new SuppressedAttachState(notifSection, groupEntry, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SuppressedAttachState)) {
            return false;
        }
        SuppressedAttachState suppressedAttachState = (SuppressedAttachState) obj;
        return Intrinsics.areEqual((Object) this.section, (Object) suppressedAttachState.section) && Intrinsics.areEqual((Object) this.parent, (Object) suppressedAttachState.parent) && this.wasPruneSuppressed == suppressedAttachState.wasPruneSuppressed;
    }

    public int hashCode() {
        NotifSection notifSection = this.section;
        int i = 0;
        int hashCode = (notifSection == null ? 0 : notifSection.hashCode()) * 31;
        GroupEntry groupEntry = this.parent;
        if (groupEntry != null) {
            i = groupEntry.hashCode();
        }
        int i2 = (hashCode + i) * 31;
        boolean z = this.wasPruneSuppressed;
        if (z) {
            z = true;
        }
        return i2 + (z ? 1 : 0);
    }

    public String toString() {
        return "SuppressedAttachState(section=" + this.section + ", parent=" + this.parent + ", wasPruneSuppressed=" + this.wasPruneSuppressed + ')';
    }

    private SuppressedAttachState(NotifSection notifSection, GroupEntry groupEntry, boolean z) {
        this.section = notifSection;
        this.parent = groupEntry;
        this.wasPruneSuppressed = z;
    }

    public final NotifSection getSection() {
        return this.section;
    }

    public final void setSection(NotifSection notifSection) {
        this.section = notifSection;
    }

    public final GroupEntry getParent() {
        return this.parent;
    }

    public final void setParent(GroupEntry groupEntry) {
        this.parent = groupEntry;
    }

    public final boolean getWasPruneSuppressed() {
        return this.wasPruneSuppressed;
    }

    public final void setWasPruneSuppressed(boolean z) {
        this.wasPruneSuppressed = z;
    }

    public final void clone(SuppressedAttachState suppressedAttachState) {
        Intrinsics.checkNotNullParameter(suppressedAttachState, PluralRules.KEYWORD_OTHER);
        this.parent = suppressedAttachState.parent;
        this.section = suppressedAttachState.section;
        this.wasPruneSuppressed = suppressedAttachState.wasPruneSuppressed;
    }

    public final void reset() {
        this.parent = null;
        this.section = null;
        this.wasPruneSuppressed = false;
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/SuppressedAttachState$Companion;", "", "()V", "create", "Lcom/android/systemui/statusbar/notification/collection/SuppressedAttachState;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SuppressedAttachState.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final SuppressedAttachState create() {
            return new SuppressedAttachState((NotifSection) null, (GroupEntry) null, false, (DefaultConstructorMarker) null);
        }
    }
}
