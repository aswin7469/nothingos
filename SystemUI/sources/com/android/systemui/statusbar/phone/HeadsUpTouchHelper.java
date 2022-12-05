package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
/* loaded from: classes.dex */
public class HeadsUpTouchHelper implements Gefingerpoken {
    private Callback mCallback;
    private boolean mCollapseSnoozes;
    private HeadsUpManagerPhone mHeadsUpManager;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private NotificationPanelViewController mPanel;
    private ExpandableNotificationRow mPickedChild;
    private float mTouchSlop;
    private boolean mTouchingHeadsUpView;
    private boolean mTrackingHeadsUp;
    private int mTrackingPointer;

    /* loaded from: classes.dex */
    public interface Callback {
        ExpandableView getChildAtRawPosition(float f, float f2);

        Context getContext();

        boolean isExpanded();
    }

    public HeadsUpTouchHelper(HeadsUpManagerPhone headsUpManagerPhone, Callback callback, NotificationPanelViewController notificationPanelViewController) {
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mCallback = callback;
        this.mPanel = notificationPanelViewController;
        this.mTouchSlop = ViewConfiguration.get(callback.getContext()).getScaledTouchSlop();
    }

    public boolean isTrackingHeadsUp() {
        return this.mTrackingHeadsUp;
    }

    @Override // com.android.systemui.Gefingerpoken
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        NotificationEntry topEntry;
        int pointerId;
        boolean z = false;
        if (this.mTouchingHeadsUpView || motionEvent.getActionMasked() == 0) {
            int findPointerIndex = motionEvent.findPointerIndex(this.mTrackingPointer);
            if (findPointerIndex < 0) {
                this.mTrackingPointer = motionEvent.getPointerId(0);
                findPointerIndex = 0;
            }
            float x = motionEvent.getX(findPointerIndex);
            float y = motionEvent.getY(findPointerIndex);
            int actionMasked = motionEvent.getActionMasked();
            boolean z2 = true;
            int i = 1;
            if (actionMasked == 0) {
                this.mInitialTouchY = y;
                this.mInitialTouchX = x;
                setTrackingHeadsUp(false);
                ExpandableView childAtRawPosition = this.mCallback.getChildAtRawPosition(x, y);
                this.mTouchingHeadsUpView = false;
                if (childAtRawPosition instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAtRawPosition;
                    if (this.mCallback.isExpanded() || !expandableNotificationRow.isHeadsUp() || !expandableNotificationRow.isPinned()) {
                        z2 = false;
                    }
                    this.mTouchingHeadsUpView = z2;
                    if (z2) {
                        this.mPickedChild = expandableNotificationRow;
                    }
                } else if (childAtRawPosition == null && !this.mCallback.isExpanded() && (topEntry = this.mHeadsUpManager.getTopEntry()) != null && topEntry.isRowPinned()) {
                    this.mPickedChild = topEntry.getRow();
                    this.mTouchingHeadsUpView = true;
                }
            } else {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        float f = y - this.mInitialTouchY;
                        if (this.mTouchingHeadsUpView && Math.abs(f) > this.mTouchSlop && Math.abs(f) > Math.abs(x - this.mInitialTouchX)) {
                            setTrackingHeadsUp(true);
                            float f2 = 0.0f;
                            if (f < 0.0f) {
                                z = true;
                            }
                            this.mCollapseSnoozes = z;
                            this.mInitialTouchX = x;
                            this.mInitialTouchY = y;
                            int actualHeight = (int) (this.mPickedChild.getActualHeight() + this.mPickedChild.getTranslationY());
                            float maxPanelHeight = this.mPanel.getMaxPanelHeight();
                            NotificationPanelViewController notificationPanelViewController = this.mPanel;
                            if (maxPanelHeight > 0.0f) {
                                f2 = actualHeight / maxPanelHeight;
                            }
                            notificationPanelViewController.setPanelScrimMinFraction(f2);
                            this.mPanel.startExpandMotion(x, y, true, actualHeight);
                            this.mHeadsUpManager.unpinAll(true);
                            this.mPanel.clearNotificationEffects();
                            endMotion();
                            return true;
                        }
                    } else if (actionMasked != 3) {
                        if (actionMasked == 6 && this.mTrackingPointer == (pointerId = motionEvent.getPointerId(motionEvent.getActionIndex()))) {
                            if (motionEvent.getPointerId(0) != pointerId) {
                                i = 0;
                            }
                            this.mTrackingPointer = motionEvent.getPointerId(i);
                            this.mInitialTouchX = motionEvent.getX(i);
                            this.mInitialTouchY = motionEvent.getY(i);
                        }
                    }
                }
                ExpandableNotificationRow expandableNotificationRow2 = this.mPickedChild;
                if (expandableNotificationRow2 != null && this.mTouchingHeadsUpView && this.mHeadsUpManager.shouldSwallowClick(expandableNotificationRow2.getEntry().getSbn().getKey())) {
                    endMotion();
                    return true;
                }
                endMotion();
            }
            return false;
        }
        return false;
    }

    private void setTrackingHeadsUp(boolean z) {
        this.mTrackingHeadsUp = z;
        this.mHeadsUpManager.setTrackingHeadsUp(z);
        this.mPanel.setTrackedHeadsUp(z ? this.mPickedChild : null);
    }

    public void notifyFling(boolean z) {
        if (z && this.mCollapseSnoozes) {
            this.mHeadsUpManager.snooze();
        }
        this.mCollapseSnoozes = false;
    }

    @Override // com.android.systemui.Gefingerpoken
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mTrackingHeadsUp) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            endMotion();
            setTrackingHeadsUp(false);
        }
        return true;
    }

    private void endMotion() {
        this.mTrackingPointer = -1;
        this.mPickedChild = null;
        this.mTouchingHeadsUpView = false;
    }
}
