package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;

public abstract class ListEntry {
    private final ListAttachState mAttachState = ListAttachState.create();
    private final long mCreationTime;
    private final String mKey;
    private final ListAttachState mPreviousAttachState = ListAttachState.create();

    public abstract NotificationEntry getRepresentativeEntry();

    protected ListEntry(String str, long j) {
        this.mKey = str;
        this.mCreationTime = j;
    }

    public String getKey() {
        return this.mKey;
    }

    public long getCreationTime() {
        return this.mCreationTime;
    }

    public GroupEntry getParent() {
        return this.mAttachState.getParent();
    }

    /* access modifiers changed from: package-private */
    public void setParent(GroupEntry groupEntry) {
        this.mAttachState.setParent(groupEntry);
    }

    public GroupEntry getPreviousParent() {
        return this.mPreviousAttachState.getParent();
    }

    public NotifSection getSection() {
        return this.mAttachState.getSection();
    }

    public int getSectionIndex() {
        if (this.mAttachState.getSection() != null) {
            return this.mAttachState.getSection().getIndex();
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public ListAttachState getAttachState() {
        return this.mAttachState;
    }

    /* access modifiers changed from: package-private */
    public ListAttachState getPreviousAttachState() {
        return this.mPreviousAttachState;
    }

    /* access modifiers changed from: package-private */
    public void beginNewAttachState() {
        this.mPreviousAttachState.clone(this.mAttachState);
        this.mAttachState.reset();
    }

    public boolean wasAttachedInPreviousPass() {
        return getPreviousAttachState().getParent() != null;
    }
}
