package com.android.p019wm.shell.compatui;

import android.app.TaskInfo;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.compatui.CompatUIController;

/* renamed from: com.android.wm.shell.compatui.CompatUIWindowManager */
class CompatUIWindowManager extends CompatUIWindowManagerAbstract {
    private static final int Z_ORDER = 2147483646;
    private final CompatUIController.CompatUICallback mCallback;
    int mCameraCompatControlState = 0;
    CompatUIHintsState mCompatUIHintsState;
    boolean mHasSizeCompat;
    CompatUILayout mLayout;

    /* access modifiers changed from: protected */
    public int getZOrder() {
        return Z_ORDER;
    }

    CompatUIWindowManager(Context context, TaskInfo taskInfo, SyncTransactionQueue syncTransactionQueue, CompatUIController.CompatUICallback compatUICallback, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout, CompatUIHintsState compatUIHintsState) {
        super(context, taskInfo, syncTransactionQueue, taskListener, displayLayout);
        this.mCallback = compatUICallback;
        this.mHasSizeCompat = taskInfo.topActivityInSizeCompat;
        this.mCameraCompatControlState = taskInfo.cameraCompatControlState;
        this.mCompatUIHintsState = compatUIHintsState;
    }

    /* access modifiers changed from: protected */
    public View getLayout() {
        return this.mLayout;
    }

    /* access modifiers changed from: protected */
    public void removeLayout() {
        this.mLayout = null;
    }

    /* access modifiers changed from: protected */
    public boolean eligibleToShowLayout() {
        return this.mHasSizeCompat || shouldShowCameraControl();
    }

    /* access modifiers changed from: protected */
    public View createLayout() {
        CompatUILayout inflateLayout = inflateLayout();
        this.mLayout = inflateLayout;
        inflateLayout.inject(this);
        updateVisibilityOfViews();
        if (this.mHasSizeCompat) {
            this.mCallback.onSizeCompatRestartButtonAppeared(this.mTaskId);
        }
        return this.mLayout;
    }

    /* access modifiers changed from: package-private */
    public CompatUILayout inflateLayout() {
        return (CompatUILayout) LayoutInflater.from(this.mContext).inflate(C3353R.layout.compat_ui_layout, (ViewGroup) null);
    }

    public boolean updateCompatInfo(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener, boolean z) {
        boolean z2 = this.mHasSizeCompat;
        int i = this.mCameraCompatControlState;
        this.mHasSizeCompat = taskInfo.topActivityInSizeCompat;
        this.mCameraCompatControlState = taskInfo.cameraCompatControlState;
        if (!super.updateCompatInfo(taskInfo, taskListener, z)) {
            return false;
        }
        if (z2 == this.mHasSizeCompat && i == this.mCameraCompatControlState) {
            return true;
        }
        updateVisibilityOfViews();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void onRestartButtonClicked() {
        this.mCallback.onSizeCompatRestartButtonClicked(this.mTaskId);
    }

    /* access modifiers changed from: package-private */
    public void onCameraTreatmentButtonClicked() {
        if (!shouldShowCameraControl()) {
            Log.w(getTag(), "Camera compat shouldn't receive clicks in the hidden state.");
            return;
        }
        int i = 1;
        if (this.mCameraCompatControlState == 1) {
            i = 2;
        }
        this.mCameraCompatControlState = i;
        this.mCallback.onCameraControlStateUpdated(this.mTaskId, this.mCameraCompatControlState);
        this.mLayout.updateCameraTreatmentButton(this.mCameraCompatControlState);
    }

    /* access modifiers changed from: package-private */
    public void onCameraDismissButtonClicked() {
        if (!shouldShowCameraControl()) {
            Log.w(getTag(), "Camera compat shouldn't receive clicks in the hidden state.");
            return;
        }
        this.mCameraCompatControlState = 3;
        this.mCallback.onCameraControlStateUpdated(this.mTaskId, 3);
        this.mLayout.setCameraControlVisibility(false);
    }

    /* access modifiers changed from: package-private */
    public void onRestartButtonLongClicked() {
        CompatUILayout compatUILayout = this.mLayout;
        if (compatUILayout != null) {
            compatUILayout.setSizeCompatHintVisibility(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void onCameraButtonLongClicked() {
        CompatUILayout compatUILayout = this.mLayout;
        if (compatUILayout != null) {
            compatUILayout.setCameraCompatHintVisibility(true);
        }
    }

    public void updateSurfacePosition() {
        int i;
        int i2;
        if (this.mLayout != null) {
            Rect taskBounds = getTaskBounds();
            Rect taskStableBounds = getTaskStableBounds();
            if (getLayoutDirection() == 1) {
                i2 = taskStableBounds.left;
                i = taskBounds.left;
            } else {
                i2 = taskStableBounds.right - taskBounds.left;
                i = this.mLayout.getMeasuredWidth();
            }
            updateSurfacePosition(i2 - i, (taskStableBounds.bottom - taskBounds.top) - this.mLayout.getMeasuredHeight());
        }
    }

    private void updateVisibilityOfViews() {
        CompatUILayout compatUILayout = this.mLayout;
        if (compatUILayout != null) {
            compatUILayout.setRestartButtonVisibility(this.mHasSizeCompat);
            if (this.mHasSizeCompat && !this.mCompatUIHintsState.mHasShownSizeCompatHint) {
                this.mLayout.setSizeCompatHintVisibility(true);
                this.mCompatUIHintsState.mHasShownSizeCompatHint = true;
            }
            this.mLayout.setCameraControlVisibility(shouldShowCameraControl());
            if (shouldShowCameraControl() && !this.mCompatUIHintsState.mHasShownCameraCompatHint) {
                this.mLayout.setCameraCompatHintVisibility(true);
                this.mCompatUIHintsState.mHasShownCameraCompatHint = true;
            }
            if (shouldShowCameraControl()) {
                this.mLayout.updateCameraTreatmentButton(this.mCameraCompatControlState);
            }
        }
    }

    private boolean shouldShowCameraControl() {
        int i = this.mCameraCompatControlState;
        return (i == 0 || i == 3) ? false : true;
    }

    /* renamed from: com.android.wm.shell.compatui.CompatUIWindowManager$CompatUIHintsState */
    static class CompatUIHintsState {
        boolean mHasShownCameraCompatHint;
        boolean mHasShownSizeCompatHint;

        CompatUIHintsState() {
        }
    }
}
