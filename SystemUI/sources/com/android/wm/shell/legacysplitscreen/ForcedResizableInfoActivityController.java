package com.android.wm.shell.legacysplitscreen;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.ArraySet;
import android.widget.Toast;
import com.android.wm.shell.R;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.legacysplitscreen.DividerView;
import java.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class ForcedResizableInfoActivityController implements DividerView.DividerCallbacks {
    private final Context mContext;
    private boolean mDividerDragging;
    private final Consumer<Boolean> mDockedStackExistsListener;
    private final ShellExecutor mMainExecutor;
    private final ArraySet<PendingTaskRecord> mPendingTasks = new ArraySet<>();
    private final ArraySet<String> mPackagesShownInSession = new ArraySet<>();
    private final Runnable mTimeoutRunnable = new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.ForcedResizableInfoActivityController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            ForcedResizableInfoActivityController.this.showPending();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Boolean bool) {
        if (!bool.booleanValue()) {
            this.mPackagesShownInSession.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class PendingTaskRecord {
        int mReason;
        int mTaskId;

        PendingTaskRecord(int i, int i2) {
            this.mTaskId = i;
            this.mReason = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ForcedResizableInfoActivityController(Context context, LegacySplitScreenController legacySplitScreenController, ShellExecutor shellExecutor) {
        Consumer<Boolean> consumer = new Consumer() { // from class: com.android.wm.shell.legacysplitscreen.ForcedResizableInfoActivityController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ForcedResizableInfoActivityController.this.lambda$new$0((Boolean) obj);
            }
        };
        this.mDockedStackExistsListener = consumer;
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        legacySplitScreenController.registerInSplitScreenListener(consumer);
    }

    @Override // com.android.wm.shell.legacysplitscreen.DividerView.DividerCallbacks
    public void onDraggingStart() {
        this.mDividerDragging = true;
        this.mMainExecutor.removeCallbacks(this.mTimeoutRunnable);
    }

    @Override // com.android.wm.shell.legacysplitscreen.DividerView.DividerCallbacks
    public void onDraggingEnd() {
        this.mDividerDragging = false;
        showPending();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppTransitionFinished() {
        if (!this.mDividerDragging) {
            showPending();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void activityForcedResizable(String str, int i, int i2) {
        if (debounce(str)) {
            return;
        }
        this.mPendingTasks.add(new PendingTaskRecord(i, i2));
        postTimeout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void activityDismissingSplitScreen() {
        Toast.makeText(this.mContext, R.string.dock_non_resizeble_failed_to_dock_text, 0).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void activityLaunchOnSecondaryDisplayFailed() {
        Toast.makeText(this.mContext, R.string.activity_launch_on_secondary_display_failed_text, 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPending() {
        this.mMainExecutor.removeCallbacks(this.mTimeoutRunnable);
        for (int size = this.mPendingTasks.size() - 1; size >= 0; size--) {
            PendingTaskRecord valueAt = this.mPendingTasks.valueAt(size);
            Intent intent = new Intent(this.mContext, ForcedResizableInfoActivity.class);
            ActivityOptions makeBasic = ActivityOptions.makeBasic();
            makeBasic.setLaunchTaskId(valueAt.mTaskId);
            makeBasic.setTaskOverlay(true, true);
            intent.putExtra("extra_forced_resizeable_reason", valueAt.mReason);
            this.mContext.startActivityAsUser(intent, makeBasic.toBundle(), UserHandle.CURRENT);
        }
        this.mPendingTasks.clear();
    }

    private void postTimeout() {
        this.mMainExecutor.removeCallbacks(this.mTimeoutRunnable);
        this.mMainExecutor.executeDelayed(this.mTimeoutRunnable, 1000L);
    }

    private boolean debounce(String str) {
        if (str == null) {
            return false;
        }
        if ("com.android.systemui".equals(str)) {
            return true;
        }
        boolean contains = this.mPackagesShownInSession.contains(str);
        this.mPackagesShownInSession.add(str);
        return contains;
    }
}
