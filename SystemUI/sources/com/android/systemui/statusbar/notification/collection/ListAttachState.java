package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ListAttachState.kt */
/* loaded from: classes.dex */
public final class ListAttachState {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private NotifFilter excludingFilter;
    @Nullable
    private GroupEntry parent;
    @Nullable
    private NotifPromoter promoter;
    @Nullable
    private NotifSection section;
    @NotNull
    private SuppressedAttachState suppressedChanges;

    public /* synthetic */ ListAttachState(GroupEntry groupEntry, NotifSection notifSection, NotifFilter notifFilter, NotifPromoter notifPromoter, SuppressedAttachState suppressedAttachState, DefaultConstructorMarker defaultConstructorMarker) {
        this(groupEntry, notifSection, notifFilter, notifPromoter, suppressedAttachState);
    }

    @NotNull
    public static final ListAttachState create() {
        return Companion.create();
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ListAttachState)) {
            return false;
        }
        ListAttachState listAttachState = (ListAttachState) obj;
        return Intrinsics.areEqual(this.parent, listAttachState.parent) && Intrinsics.areEqual(this.section, listAttachState.section) && Intrinsics.areEqual(this.excludingFilter, listAttachState.excludingFilter) && Intrinsics.areEqual(this.promoter, listAttachState.promoter) && Intrinsics.areEqual(this.suppressedChanges, listAttachState.suppressedChanges);
    }

    public int hashCode() {
        GroupEntry groupEntry = this.parent;
        int i = 0;
        int hashCode = (groupEntry == null ? 0 : groupEntry.hashCode()) * 31;
        NotifSection notifSection = this.section;
        int hashCode2 = (hashCode + (notifSection == null ? 0 : notifSection.hashCode())) * 31;
        NotifFilter notifFilter = this.excludingFilter;
        int hashCode3 = (hashCode2 + (notifFilter == null ? 0 : notifFilter.hashCode())) * 31;
        NotifPromoter notifPromoter = this.promoter;
        if (notifPromoter != null) {
            i = notifPromoter.hashCode();
        }
        return ((hashCode3 + i) * 31) + this.suppressedChanges.hashCode();
    }

    @NotNull
    public String toString() {
        return "ListAttachState(parent=" + this.parent + ", section=" + this.section + ", excludingFilter=" + this.excludingFilter + ", promoter=" + this.promoter + ", suppressedChanges=" + this.suppressedChanges + ')';
    }

    private ListAttachState(GroupEntry groupEntry, NotifSection notifSection, NotifFilter notifFilter, NotifPromoter notifPromoter, SuppressedAttachState suppressedAttachState) {
        this.parent = groupEntry;
        this.section = notifSection;
        this.excludingFilter = notifFilter;
        this.promoter = notifPromoter;
        this.suppressedChanges = suppressedAttachState;
    }

    @Nullable
    public final GroupEntry getParent() {
        return this.parent;
    }

    public final void setParent(@Nullable GroupEntry groupEntry) {
        this.parent = groupEntry;
    }

    @Nullable
    public final NotifSection getSection() {
        return this.section;
    }

    public final void setSection(@Nullable NotifSection notifSection) {
        this.section = notifSection;
    }

    @Nullable
    public final NotifFilter getExcludingFilter() {
        return this.excludingFilter;
    }

    public final void setExcludingFilter(@Nullable NotifFilter notifFilter) {
        this.excludingFilter = notifFilter;
    }

    @Nullable
    public final NotifPromoter getPromoter() {
        return this.promoter;
    }

    public final void setPromoter(@Nullable NotifPromoter notifPromoter) {
        this.promoter = notifPromoter;
    }

    @NotNull
    public final SuppressedAttachState getSuppressedChanges() {
        return this.suppressedChanges;
    }

    public final void clone(@NotNull ListAttachState other) {
        Intrinsics.checkNotNullParameter(other, "other");
        this.parent = other.parent;
        this.section = other.section;
        this.excludingFilter = other.excludingFilter;
        this.promoter = other.promoter;
        this.suppressedChanges.clone(other.suppressedChanges);
    }

    public final void reset() {
        this.parent = null;
        this.section = null;
        this.excludingFilter = null;
        this.promoter = null;
        this.suppressedChanges.reset();
    }

    /* compiled from: ListAttachState.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final ListAttachState create() {
            return new ListAttachState(null, null, null, null, SuppressedAttachState.Companion.create(), null);
        }
    }
}
