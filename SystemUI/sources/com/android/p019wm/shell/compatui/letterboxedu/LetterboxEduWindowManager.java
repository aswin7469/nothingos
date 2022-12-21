package com.android.p019wm.shell.compatui.letterboxedu;

import android.app.TaskInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.p019wm.shell.C3343R;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.compatui.CompatUIWindowManagerAbstract;
import com.android.p019wm.shell.transition.Transitions;

/* renamed from: com.android.wm.shell.compatui.letterboxedu.LetterboxEduWindowManager */
public class LetterboxEduWindowManager extends CompatUIWindowManagerAbstract {
    static final String HAS_SEEN_LETTERBOX_EDUCATION_PREF_NAME = "has_seen_letterbox_education";
    public static final int Z_ORDER = Integer.MAX_VALUE;
    private final LetterboxEduAnimationController mAnimationController;
    private final int mDialogVerticalMargin;
    private boolean mEligibleForLetterboxEducation;
    LetterboxEduDialogLayout mLayout;
    private final Runnable mOnDismissCallback;
    private final SharedPreferences mSharedPreferences;
    private final Transitions mTransitions;
    private final int mUserId;

    /* access modifiers changed from: protected */
    public int getZOrder() {
        return Integer.MAX_VALUE;
    }

    /* access modifiers changed from: protected */
    public void updateSurfacePosition() {
    }

    public LetterboxEduWindowManager(Context context, TaskInfo taskInfo, SyncTransactionQueue syncTransactionQueue, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout, Transitions transitions, Runnable runnable) {
        this(context, taskInfo, syncTransactionQueue, taskListener, displayLayout, transitions, runnable, new LetterboxEduAnimationController(context));
    }

    LetterboxEduWindowManager(Context context, TaskInfo taskInfo, SyncTransactionQueue syncTransactionQueue, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout, Transitions transitions, Runnable runnable, LetterboxEduAnimationController letterboxEduAnimationController) {
        super(context, taskInfo, syncTransactionQueue, taskListener, displayLayout);
        this.mTransitions = transitions;
        this.mOnDismissCallback = runnable;
        this.mAnimationController = letterboxEduAnimationController;
        this.mUserId = taskInfo.userId;
        this.mEligibleForLetterboxEducation = taskInfo.topActivityEligibleForLetterboxEducation;
        this.mSharedPreferences = this.mContext.getSharedPreferences(HAS_SEEN_LETTERBOX_EDUCATION_PREF_NAME, 0);
        this.mDialogVerticalMargin = (int) this.mContext.getResources().getDimension(C3343R.dimen.letterbox_education_dialog_margin);
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
        return this.mEligibleForLetterboxEducation && !isTaskbarEduShowing() && (this.mLayout != null || !getHasSeenLetterboxEducation());
    }

    /* access modifiers changed from: protected */
    public View createLayout() {
        this.mLayout = inflateLayout();
        updateDialogMargins();
        this.mTransitions.runOnIdle(new LetterboxEduWindowManager$$ExternalSyntheticLambda0(this));
        return this.mLayout;
    }

    private void updateDialogMargins() {
        LetterboxEduDialogLayout letterboxEduDialogLayout = this.mLayout;
        if (letterboxEduDialogLayout != null) {
            View dialogContainer = letterboxEduDialogLayout.getDialogContainer();
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) dialogContainer.getLayoutParams();
            Rect taskBounds = getTaskBounds();
            Rect taskStableBounds = getTaskStableBounds();
            marginLayoutParams.topMargin = (taskStableBounds.top - taskBounds.top) + this.mDialogVerticalMargin;
            marginLayoutParams.bottomMargin = (taskBounds.bottom - taskStableBounds.bottom) + this.mDialogVerticalMargin;
            dialogContainer.setLayoutParams(marginLayoutParams);
        }
    }

    private LetterboxEduDialogLayout inflateLayout() {
        return (LetterboxEduDialogLayout) LayoutInflater.from(this.mContext).inflate(C3343R.layout.letterbox_education_dialog_layout, (ViewGroup) null);
    }

    /* access modifiers changed from: private */
    public void startEnterAnimation() {
        LetterboxEduDialogLayout letterboxEduDialogLayout = this.mLayout;
        if (letterboxEduDialogLayout != null) {
            this.mAnimationController.startEnterAnimation(letterboxEduDialogLayout, new LetterboxEduWindowManager$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: private */
    public void onDialogEnterAnimationEnded() {
        if (this.mLayout != null) {
            setSeenLetterboxEducation();
            this.mLayout.setDismissOnClickListener(new LetterboxEduWindowManager$$ExternalSyntheticLambda3(this));
            this.mLayout.getDialogTitle().sendAccessibilityEvent(8);
        }
    }

    /* access modifiers changed from: private */
    public void onDismiss() {
        LetterboxEduDialogLayout letterboxEduDialogLayout = this.mLayout;
        if (letterboxEduDialogLayout != null) {
            letterboxEduDialogLayout.setDismissOnClickListener((Runnable) null);
            this.mAnimationController.startExitAnimation(this.mLayout, new LetterboxEduWindowManager$$ExternalSyntheticLambda2(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onDismiss$0$com-android-wm-shell-compatui-letterboxedu-LetterboxEduWindowManager */
    public /* synthetic */ void mo49458xb7d877c7() {
        release();
        this.mOnDismissCallback.run();
    }

    public void release() {
        this.mAnimationController.cancelAnimation();
        super.release();
    }

    public boolean updateCompatInfo(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener, boolean z) {
        this.mEligibleForLetterboxEducation = taskInfo.topActivityEligibleForLetterboxEducation;
        return super.updateCompatInfo(taskInfo, taskListener, z);
    }

    /* access modifiers changed from: protected */
    public void onParentBoundsChanged() {
        if (this.mLayout != null) {
            WindowManager.LayoutParams windowLayoutParams = getWindowLayoutParams();
            this.mLayout.setLayoutParams(windowLayoutParams);
            updateDialogMargins();
            relayout(windowLayoutParams);
        }
    }

    /* access modifiers changed from: protected */
    public WindowManager.LayoutParams getWindowLayoutParams() {
        Rect taskBounds = getTaskBounds();
        return getWindowLayoutParams(taskBounds.width(), taskBounds.height());
    }

    private boolean getHasSeenLetterboxEducation() {
        return this.mSharedPreferences.getBoolean(getPrefKey(), false);
    }

    private void setSeenLetterboxEducation() {
        this.mSharedPreferences.edit().putBoolean(getPrefKey(), true).apply();
    }

    private String getPrefKey() {
        return String.valueOf(this.mUserId);
    }

    /* access modifiers changed from: package-private */
    public boolean isTaskbarEduShowing() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "launcher_taskbar_education_showing", 0) == 1;
    }
}
