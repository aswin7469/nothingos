package com.android.wm.shell.sizecompatui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Binder;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.sizecompatui.SizeCompatUIController;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class SizeCompatUILayout {
    @VisibleForTesting
    SizeCompatRestartButton mButton;
    final int mButtonSize;
    @VisibleForTesting
    final SizeCompatUIWindowManager mButtonWindowManager;
    private final SizeCompatUIController.SizeCompatUICallback mCallback;
    private Context mContext;
    private final int mDisplayId;
    private DisplayLayout mDisplayLayout;
    @VisibleForTesting
    SizeCompatHintPopup mHint;
    @VisibleForTesting
    SizeCompatUIWindowManager mHintWindowManager;
    final int mPopupOffsetX;
    final int mPopupOffsetY;
    boolean mShouldShowHint;
    final SyncTransactionQueue mSyncQueue;
    private Configuration mTaskConfig;
    private final int mTaskId;
    private ShellTaskOrganizer.TaskListener mTaskListener;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SizeCompatUILayout(SyncTransactionQueue syncTransactionQueue, SizeCompatUIController.SizeCompatUICallback sizeCompatUICallback, Context context, Configuration configuration, int i, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout, boolean z) {
        this.mSyncQueue = syncTransactionQueue;
        this.mCallback = sizeCompatUICallback;
        Context createConfigurationContext = context.createConfigurationContext(configuration);
        this.mContext = createConfigurationContext;
        this.mTaskConfig = configuration;
        this.mDisplayId = createConfigurationContext.getDisplayId();
        this.mTaskId = i;
        this.mTaskListener = taskListener;
        this.mDisplayLayout = displayLayout;
        this.mShouldShowHint = !z;
        this.mButtonWindowManager = new SizeCompatUIWindowManager(this.mContext, configuration, this);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.size_compat_button_size);
        this.mButtonSize = dimensionPixelSize;
        this.mPopupOffsetX = dimensionPixelSize / 4;
        this.mPopupOffsetY = dimensionPixelSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createSizeCompatButton(boolean z) {
        if (z || this.mButton != null) {
            return;
        }
        this.mButton = this.mButtonWindowManager.createSizeCompatButton();
        updateButtonSurfacePosition();
        if (!this.mShouldShowHint) {
            return;
        }
        this.mShouldShowHint = false;
        createSizeCompatHint();
    }

    private void createSizeCompatHint() {
        if (this.mHint != null) {
            return;
        }
        SizeCompatUIWindowManager createHintWindowManager = createHintWindowManager();
        this.mHintWindowManager = createHintWindowManager;
        this.mHint = createHintWindowManager.createSizeCompatHint();
        updateHintSurfacePosition();
    }

    @VisibleForTesting
    SizeCompatUIWindowManager createHintWindowManager() {
        return new SizeCompatUIWindowManager(this.mContext, this.mTaskConfig, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dismissHint() {
        this.mHint = null;
        SizeCompatUIWindowManager sizeCompatUIWindowManager = this.mHintWindowManager;
        if (sizeCompatUIWindowManager != null) {
            sizeCompatUIWindowManager.release();
            this.mHintWindowManager = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void release() {
        dismissHint();
        this.mButton = null;
        this.mButtonWindowManager.release();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateSizeCompatInfo(Configuration configuration, ShellTaskOrganizer.TaskListener taskListener, boolean z) {
        Configuration configuration2 = this.mTaskConfig;
        ShellTaskOrganizer.TaskListener taskListener2 = this.mTaskListener;
        this.mTaskConfig = configuration;
        this.mTaskListener = taskListener;
        this.mContext = this.mContext.createConfigurationContext(configuration);
        this.mButtonWindowManager.setConfiguration(configuration);
        SizeCompatUIWindowManager sizeCompatUIWindowManager = this.mHintWindowManager;
        if (sizeCompatUIWindowManager != null) {
            sizeCompatUIWindowManager.setConfiguration(configuration);
        }
        if (this.mButton == null || taskListener2 != taskListener) {
            release();
            createSizeCompatButton(z);
            return;
        }
        if (!configuration.windowConfiguration.getBounds().equals(configuration2.windowConfiguration.getBounds())) {
            updateButtonSurfacePosition();
            updateHintSurfacePosition();
        }
        if (configuration.getLayoutDirection() == configuration2.getLayoutDirection()) {
            return;
        }
        this.mButton.setLayoutDirection(configuration.getLayoutDirection());
        updateButtonSurfacePosition();
        SizeCompatHintPopup sizeCompatHintPopup = this.mHint;
        if (sizeCompatHintPopup == null) {
            return;
        }
        sizeCompatHintPopup.setLayoutDirection(configuration.getLayoutDirection());
        updateHintSurfacePosition();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateDisplayLayout(DisplayLayout displayLayout) {
        if (displayLayout == this.mDisplayLayout) {
            return;
        }
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        this.mDisplayLayout.getStableBounds(rect);
        displayLayout.getStableBounds(rect2);
        this.mDisplayLayout = displayLayout;
        if (rect.equals(rect2)) {
            return;
        }
        updateButtonSurfacePosition();
        updateHintSurfacePosition();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateImeVisibility(boolean z) {
        SizeCompatRestartButton sizeCompatRestartButton = this.mButton;
        if (sizeCompatRestartButton == null) {
            createSizeCompatButton(z);
            return;
        }
        int i = z ? 8 : 0;
        if (sizeCompatRestartButton.getVisibility() != i) {
            this.mButton.setVisibility(i);
        }
        SizeCompatHintPopup sizeCompatHintPopup = this.mHint;
        if (sizeCompatHintPopup == null || sizeCompatHintPopup.getVisibility() == i) {
            return;
        }
        this.mHint.setVisibility(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowManager.LayoutParams getButtonWindowLayoutParams() {
        int i = this.mButtonSize;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i, 2038, 40, -3);
        layoutParams.token = new Binder();
        layoutParams.setTitle(SizeCompatRestartButton.class.getSimpleName() + getTaskId());
        layoutParams.privateFlags = layoutParams.privateFlags | 536870976;
        return layoutParams;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowManager.LayoutParams getHintWindowLayoutParams(SizeCompatHintPopup sizeCompatHintPopup) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(sizeCompatHintPopup.getMeasuredWidth(), sizeCompatHintPopup.getMeasuredHeight(), 2038, 40, -3);
        layoutParams.token = new Binder();
        layoutParams.setTitle(SizeCompatHintPopup.class.getSimpleName() + getTaskId());
        layoutParams.privateFlags = layoutParams.privateFlags | 536870976;
        layoutParams.windowAnimations = 16973910;
        return layoutParams;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void attachToParentSurface(SurfaceControl.Builder builder) {
        this.mTaskListener.attachChildSurfaceToTask(this.mTaskId, builder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onRestartButtonClicked() {
        this.mCallback.onSizeCompatRestartButtonClicked(this.mTaskId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onRestartButtonLongClicked() {
        createSizeCompatHint();
    }

    @VisibleForTesting
    void updateButtonSurfacePosition() {
        int i;
        int i2;
        if (this.mButton == null || this.mButtonWindowManager.getSurfaceControl() == null) {
            return;
        }
        SurfaceControl surfaceControl = this.mButtonWindowManager.getSurfaceControl();
        Rect bounds = this.mTaskConfig.windowConfiguration.getBounds();
        Rect rect = new Rect();
        this.mDisplayLayout.getStableBounds(rect);
        rect.intersect(bounds);
        if (getLayoutDirection() == 1) {
            i = rect.left;
            i2 = bounds.left;
        } else {
            i = rect.right - bounds.left;
            i2 = this.mButtonSize;
        }
        updateSurfacePosition(surfaceControl, i - i2, (rect.bottom - bounds.top) - this.mButtonSize);
    }

    void updateHintSurfacePosition() {
        SizeCompatUIWindowManager sizeCompatUIWindowManager;
        int measuredWidth;
        if (this.mHint == null || (sizeCompatUIWindowManager = this.mHintWindowManager) == null || sizeCompatUIWindowManager.getSurfaceControl() == null) {
            return;
        }
        SurfaceControl surfaceControl = this.mHintWindowManager.getSurfaceControl();
        Rect bounds = this.mTaskConfig.windowConfiguration.getBounds();
        Rect rect = new Rect();
        this.mDisplayLayout.getStableBounds(rect);
        rect.intersect(bounds);
        if (getLayoutDirection() == 1) {
            measuredWidth = (rect.left - bounds.left) + this.mPopupOffsetX;
        } else {
            measuredWidth = ((rect.right - bounds.left) - this.mPopupOffsetX) - this.mHint.getMeasuredWidth();
        }
        updateSurfacePosition(surfaceControl, measuredWidth, ((rect.bottom - bounds.top) - this.mPopupOffsetY) - this.mHint.getMeasuredHeight());
    }

    private void updateSurfacePosition(final SurfaceControl surfaceControl, final int i, final int i2) {
        this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.sizecompatui.SizeCompatUILayout$$ExternalSyntheticLambda0
            @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
            public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                SizeCompatUILayout.lambda$updateSurfacePosition$0(surfaceControl, i, i2, transaction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateSurfacePosition$0(SurfaceControl surfaceControl, int i, int i2, SurfaceControl.Transaction transaction) {
        if (!surfaceControl.isValid()) {
            Log.w("SizeCompatUILayout", "The leash has been released.");
            return;
        }
        transaction.setPosition(surfaceControl, i, i2);
        transaction.setLayer(surfaceControl, Integer.MAX_VALUE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDisplayId() {
        return this.mDisplayId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTaskId() {
        return this.mTaskId;
    }

    private int getLayoutDirection() {
        return this.mContext.getResources().getConfiguration().getLayoutDirection();
    }
}
