package com.android.systemui.statusbar.notification.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes.dex */
public class GroupEntry extends ListEntry {
    public static final GroupEntry ROOT_ENTRY = new GroupEntry("<root>", 0);
    private final List<NotificationEntry> mChildren;
    private NotificationEntry mSummary;
    private final List<NotificationEntry> mUnmodifiableChildren;
    private int mUntruncatedChildCount;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GroupEntry(String str, long j) {
        super(str, j);
        ArrayList arrayList = new ArrayList();
        this.mChildren = arrayList;
        this.mUnmodifiableChildren = Collections.unmodifiableList(arrayList);
    }

    @Override // com.android.systemui.statusbar.notification.collection.ListEntry
    public NotificationEntry getRepresentativeEntry() {
        return this.mSummary;
    }

    public NotificationEntry getSummary() {
        return this.mSummary;
    }

    public List<NotificationEntry> getChildren() {
        return this.mUnmodifiableChildren;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSummary(NotificationEntry notificationEntry) {
        this.mSummary = notificationEntry;
    }

    public void setUntruncatedChildCount(int i) {
        this.mUntruncatedChildCount = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearChildren() {
        this.mChildren.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addChild(NotificationEntry notificationEntry) {
        this.mChildren.add(notificationEntry);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sortChildren(Comparator<? super NotificationEntry> comparator) {
        this.mChildren.sort(comparator);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<NotificationEntry> getRawChildren() {
        return this.mChildren;
    }
}
