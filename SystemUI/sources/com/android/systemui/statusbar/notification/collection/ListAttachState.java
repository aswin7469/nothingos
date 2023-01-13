package com.android.systemui.statusbar.notification.collection;

import android.icu.text.PluralRules;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0005\b\b\u0018\u0000 <2\u00020\u0001:\u0001<BA\b\u0002\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u000e\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020\u0000J\u000b\u00100\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u00101\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u00102\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\u000b\u00103\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\u000bHÆ\u0003J\t\u00105\u001a\u00020\rHÆ\u0003JO\u00106\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\rHÆ\u0001J\u0013\u00107\u001a\u0002082\b\u0010/\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00109\u001a\u00020$HÖ\u0001J\u0006\u0010:\u001a\u00020.J\t\u0010;\u001a\u00020\u000bHÖ\u0001R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010#\u001a\u00020$X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u001a\u0010\f\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,¨\u0006="}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/ListAttachState;", "", "parent", "Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "section", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;", "excludingFilter", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifFilter;", "promoter", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifPromoter;", "groupPruneReason", "", "suppressedChanges", "Lcom/android/systemui/statusbar/notification/collection/SuppressedAttachState;", "(Lcom/android/systemui/statusbar/notification/collection/GroupEntry;Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifFilter;Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifPromoter;Ljava/lang/String;Lcom/android/systemui/statusbar/notification/collection/SuppressedAttachState;)V", "getExcludingFilter", "()Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifFilter;", "setExcludingFilter", "(Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifFilter;)V", "getGroupPruneReason", "()Ljava/lang/String;", "setGroupPruneReason", "(Ljava/lang/String;)V", "getParent", "()Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "setParent", "(Lcom/android/systemui/statusbar/notification/collection/GroupEntry;)V", "getPromoter", "()Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifPromoter;", "setPromoter", "(Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifPromoter;)V", "getSection", "()Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;", "setSection", "(Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;)V", "stableIndex", "", "getStableIndex", "()I", "setStableIndex", "(I)V", "getSuppressedChanges", "()Lcom/android/systemui/statusbar/notification/collection/SuppressedAttachState;", "setSuppressedChanges", "(Lcom/android/systemui/statusbar/notification/collection/SuppressedAttachState;)V", "clone", "", "other", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "hashCode", "reset", "toString", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ListAttachState.kt */
public final class ListAttachState {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private NotifFilter excludingFilter;
    private String groupPruneReason;
    private GroupEntry parent;
    private NotifPromoter promoter;
    private NotifSection section;
    private int stableIndex;
    private SuppressedAttachState suppressedChanges;

    public /* synthetic */ ListAttachState(GroupEntry groupEntry, NotifSection notifSection, NotifFilter notifFilter, NotifPromoter notifPromoter, String str, SuppressedAttachState suppressedAttachState, DefaultConstructorMarker defaultConstructorMarker) {
        this(groupEntry, notifSection, notifFilter, notifPromoter, str, suppressedAttachState);
    }

    public static /* synthetic */ ListAttachState copy$default(ListAttachState listAttachState, GroupEntry groupEntry, NotifSection notifSection, NotifFilter notifFilter, NotifPromoter notifPromoter, String str, SuppressedAttachState suppressedAttachState, int i, Object obj) {
        if ((i & 1) != 0) {
            groupEntry = listAttachState.parent;
        }
        if ((i & 2) != 0) {
            notifSection = listAttachState.section;
        }
        NotifSection notifSection2 = notifSection;
        if ((i & 4) != 0) {
            notifFilter = listAttachState.excludingFilter;
        }
        NotifFilter notifFilter2 = notifFilter;
        if ((i & 8) != 0) {
            notifPromoter = listAttachState.promoter;
        }
        NotifPromoter notifPromoter2 = notifPromoter;
        if ((i & 16) != 0) {
            str = listAttachState.groupPruneReason;
        }
        String str2 = str;
        if ((i & 32) != 0) {
            suppressedAttachState = listAttachState.suppressedChanges;
        }
        return listAttachState.copy(groupEntry, notifSection2, notifFilter2, notifPromoter2, str2, suppressedAttachState);
    }

    @JvmStatic
    public static final ListAttachState create() {
        return Companion.create();
    }

    public final GroupEntry component1() {
        return this.parent;
    }

    public final NotifSection component2() {
        return this.section;
    }

    public final NotifFilter component3() {
        return this.excludingFilter;
    }

    public final NotifPromoter component4() {
        return this.promoter;
    }

    public final String component5() {
        return this.groupPruneReason;
    }

    public final SuppressedAttachState component6() {
        return this.suppressedChanges;
    }

    public final ListAttachState copy(GroupEntry groupEntry, NotifSection notifSection, NotifFilter notifFilter, NotifPromoter notifPromoter, String str, SuppressedAttachState suppressedAttachState) {
        Intrinsics.checkNotNullParameter(suppressedAttachState, "suppressedChanges");
        return new ListAttachState(groupEntry, notifSection, notifFilter, notifPromoter, str, suppressedAttachState);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ListAttachState)) {
            return false;
        }
        ListAttachState listAttachState = (ListAttachState) obj;
        return Intrinsics.areEqual((Object) this.parent, (Object) listAttachState.parent) && Intrinsics.areEqual((Object) this.section, (Object) listAttachState.section) && Intrinsics.areEqual((Object) this.excludingFilter, (Object) listAttachState.excludingFilter) && Intrinsics.areEqual((Object) this.promoter, (Object) listAttachState.promoter) && Intrinsics.areEqual((Object) this.groupPruneReason, (Object) listAttachState.groupPruneReason) && Intrinsics.areEqual((Object) this.suppressedChanges, (Object) listAttachState.suppressedChanges);
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
        int hashCode4 = (hashCode3 + (notifPromoter == null ? 0 : notifPromoter.hashCode())) * 31;
        String str = this.groupPruneReason;
        if (str != null) {
            i = str.hashCode();
        }
        return ((hashCode4 + i) * 31) + this.suppressedChanges.hashCode();
    }

    public String toString() {
        return "ListAttachState(parent=" + this.parent + ", section=" + this.section + ", excludingFilter=" + this.excludingFilter + ", promoter=" + this.promoter + ", groupPruneReason=" + this.groupPruneReason + ", suppressedChanges=" + this.suppressedChanges + ')';
    }

    private ListAttachState(GroupEntry groupEntry, NotifSection notifSection, NotifFilter notifFilter, NotifPromoter notifPromoter, String str, SuppressedAttachState suppressedAttachState) {
        this.parent = groupEntry;
        this.section = notifSection;
        this.excludingFilter = notifFilter;
        this.promoter = notifPromoter;
        this.groupPruneReason = str;
        this.suppressedChanges = suppressedAttachState;
        this.stableIndex = -1;
    }

    public final GroupEntry getParent() {
        return this.parent;
    }

    public final void setParent(GroupEntry groupEntry) {
        this.parent = groupEntry;
    }

    public final NotifSection getSection() {
        return this.section;
    }

    public final void setSection(NotifSection notifSection) {
        this.section = notifSection;
    }

    public final NotifFilter getExcludingFilter() {
        return this.excludingFilter;
    }

    public final void setExcludingFilter(NotifFilter notifFilter) {
        this.excludingFilter = notifFilter;
    }

    public final NotifPromoter getPromoter() {
        return this.promoter;
    }

    public final void setPromoter(NotifPromoter notifPromoter) {
        this.promoter = notifPromoter;
    }

    public final String getGroupPruneReason() {
        return this.groupPruneReason;
    }

    public final void setGroupPruneReason(String str) {
        this.groupPruneReason = str;
    }

    public final SuppressedAttachState getSuppressedChanges() {
        return this.suppressedChanges;
    }

    public final void setSuppressedChanges(SuppressedAttachState suppressedAttachState) {
        Intrinsics.checkNotNullParameter(suppressedAttachState, "<set-?>");
        this.suppressedChanges = suppressedAttachState;
    }

    public final int getStableIndex() {
        return this.stableIndex;
    }

    public final void setStableIndex(int i) {
        this.stableIndex = i;
    }

    public final void clone(ListAttachState listAttachState) {
        Intrinsics.checkNotNullParameter(listAttachState, PluralRules.KEYWORD_OTHER);
        this.parent = listAttachState.parent;
        this.section = listAttachState.section;
        this.excludingFilter = listAttachState.excludingFilter;
        this.promoter = listAttachState.promoter;
        this.groupPruneReason = listAttachState.groupPruneReason;
        this.suppressedChanges.clone(listAttachState.suppressedChanges);
        this.stableIndex = listAttachState.stableIndex;
    }

    public final void reset() {
        this.parent = null;
        this.section = null;
        this.excludingFilter = null;
        this.promoter = null;
        this.groupPruneReason = null;
        this.suppressedChanges.reset();
        this.stableIndex = -1;
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/ListAttachState$Companion;", "", "()V", "create", "Lcom/android/systemui/statusbar/notification/collection/ListAttachState;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ListAttachState.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final ListAttachState create() {
            return new ListAttachState((GroupEntry) null, (NotifSection) null, (NotifFilter) null, (NotifPromoter) null, (String) null, SuppressedAttachState.Companion.create(), (DefaultConstructorMarker) null);
        }
    }
}
