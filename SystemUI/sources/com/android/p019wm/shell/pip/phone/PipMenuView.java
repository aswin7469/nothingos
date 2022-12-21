package com.android.p019wm.shell.pip.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.Property;
import android.util.Size;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.C3343R;
import com.android.p019wm.shell.animation.Interpolators;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import com.android.p019wm.shell.pip.PipUtils;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/* renamed from: com.android.wm.shell.pip.phone.PipMenuView */
public class PipMenuView extends FrameLayout {
    private static final int ANIMATION_HIDE_DURATION_MS = 125;
    private static final int ANIMATION_NONE_DURATION_MS = 0;
    public static final int ANIM_TYPE_DISMISS = 2;
    public static final int ANIM_TYPE_HIDE = 1;
    public static final int ANIM_TYPE_NONE = 0;
    private static final float DISABLED_ACTION_ALPHA = 0.54f;
    private static final int INITIAL_DISMISS_DELAY = 3500;
    private static final float MENU_BACKGROUND_ALPHA = 0.3f;
    private static final long MENU_SHOW_ON_EXPAND_START_DELAY = 30;
    private static final int POST_INTERACTION_DISMISS_DELAY = 2000;
    private static final String TAG = "PipMenuView";
    private AccessibilityManager mAccessibilityManager;
    private final List<RemoteAction> mActions = new ArrayList();
    private LinearLayout mActionsGroup;
    private boolean mAllowMenuTimeout = true;
    /* access modifiers changed from: private */
    public boolean mAllowTouches = true;
    /* access modifiers changed from: private */
    public Drawable mBackgroundDrawable;
    private int mBetweenActionPaddingLand;
    private RemoteAction mCloseAction;
    /* access modifiers changed from: private */
    public final PhonePipMenuController mController;
    private boolean mDidLastShowMenuResize;
    protected View mDismissButton;
    private int mDismissFadeOutDurationMs;
    protected View mEnterSplitButton;
    private boolean mFocusedTaskAllowSplitScreen;
    private final Runnable mHideMenuRunnable = new PipMenuView$$ExternalSyntheticLambda0(this);
    private ShellExecutor mMainExecutor;
    private Handler mMainHandler;
    private ValueAnimator.AnimatorUpdateListener mMenuBgUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            PipMenuView.this.mBackgroundDrawable.setAlpha((int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 0.3f * 255.0f));
        }
    };
    private View mMenuContainer;
    private AnimatorSet mMenuContainerAnimator;
    /* access modifiers changed from: private */
    public int mMenuState;
    private final int mPipForceCloseDelay;
    protected PipMenuIconsAlgorithm mPipMenuIconsAlgorithm;
    private final PipUiEventLogger mPipUiEventLogger;
    protected View mSettingsButton;
    private final Optional<SplitScreenController> mSplitScreenControllerOptional;
    protected View mTopEndContainer;
    protected View mViewRoot;

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: com.android.wm.shell.pip.phone.PipMenuView$AnimationType */
    public @interface AnimationType {
    }

    static /* synthetic */ boolean lambda$updateActionViews$5(View view, MotionEvent motionEvent) {
        return true;
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public PipMenuView(Context context, PhonePipMenuController phonePipMenuController, ShellExecutor shellExecutor, Handler handler, Optional<SplitScreenController> optional, PipUiEventLogger pipUiEventLogger) {
        super(context, (AttributeSet) null, 0);
        this.mContext = context;
        this.mController = phonePipMenuController;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
        this.mSplitScreenControllerOptional = optional;
        this.mPipUiEventLogger = pipUiEventLogger;
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        inflate(context, C3343R.layout.pip_menu, this);
        this.mPipForceCloseDelay = context.getResources().getInteger(C3343R.integer.config_pipForceCloseDelay);
        Drawable drawable = this.mContext.getDrawable(C3343R.C3345drawable.pip_menu_background);
        this.mBackgroundDrawable = drawable;
        drawable.setAlpha(0);
        View findViewById = findViewById(C3343R.C3346id.background);
        this.mViewRoot = findViewById;
        findViewById.setBackground(this.mBackgroundDrawable);
        View findViewById2 = findViewById(C3343R.C3346id.menu_container);
        this.mMenuContainer = findViewById2;
        findViewById2.setAlpha(0.0f);
        this.mTopEndContainer = findViewById(C3343R.C3346id.top_end_container);
        View findViewById3 = findViewById(C3343R.C3346id.settings);
        this.mSettingsButton = findViewById3;
        findViewById3.setAlpha(0.0f);
        this.mSettingsButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda3(this));
        View findViewById4 = findViewById(C3343R.C3346id.dismiss);
        this.mDismissButton = findViewById4;
        findViewById4.setAlpha(0.0f);
        this.mDismissButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda4(this));
        findViewById(C3343R.C3346id.expand_button).setOnClickListener(new PipMenuView$$ExternalSyntheticLambda5(this));
        View findViewById5 = findViewById(C3343R.C3346id.enter_split);
        this.mEnterSplitButton = findViewById5;
        findViewById5.setAlpha(0.0f);
        this.mEnterSplitButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda6(this));
        findViewById(C3343R.C3346id.resize_handle).setAlpha(0.0f);
        this.mActionsGroup = (LinearLayout) findViewById(C3343R.C3346id.actions_group);
        this.mBetweenActionPaddingLand = getResources().getDimensionPixelSize(C3343R.dimen.pip_between_action_padding_land);
        PipMenuIconsAlgorithm pipMenuIconsAlgorithm = new PipMenuIconsAlgorithm(this.mContext);
        this.mPipMenuIconsAlgorithm = pipMenuIconsAlgorithm;
        pipMenuIconsAlgorithm.bindViews((ViewGroup) this.mViewRoot, (ViewGroup) this.mTopEndContainer, findViewById(C3343R.C3346id.resize_handle), this.mEnterSplitButton, this.mSettingsButton, this.mDismissButton);
        this.mDismissFadeOutDurationMs = context.getResources().getInteger(C3343R.integer.config_pipExitAnimationDuration);
        initAccessibility();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-wm-shell-pip-phone-PipMenuView  reason: not valid java name */
    public /* synthetic */ void m3463lambda$new$0$comandroidwmshellpipphonePipMenuView(View view) {
        if (view.getAlpha() != 0.0f) {
            showSettings();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-wm-shell-pip-phone-PipMenuView  reason: not valid java name */
    public /* synthetic */ void m3464lambda$new$1$comandroidwmshellpipphonePipMenuView(View view) {
        dismissPip();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-wm-shell-pip-phone-PipMenuView  reason: not valid java name */
    public /* synthetic */ void m3465lambda$new$2$comandroidwmshellpipphonePipMenuView(View view) {
        if (this.mMenuContainer.getAlpha() != 0.0f) {
            expandPip();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-wm-shell-pip-phone-PipMenuView  reason: not valid java name */
    public /* synthetic */ void m3466lambda$new$3$comandroidwmshellpipphonePipMenuView(View view) {
        if (this.mEnterSplitButton.getAlpha() != 0.0f) {
            enterSplit();
        }
    }

    private void initAccessibility() {
        setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, PipMenuView.this.getResources().getString(C3343R.string.pip_menu_title)));
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (i == 16 && PipMenuView.this.mMenuState != 1) {
                    PipMenuView.this.mController.showMenu();
                }
                return super.performAccessibilityAction(view, i, bundle);
            }
        });
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 111) {
            return super.onKeyUp(i, keyEvent);
        }
        hideMenu();
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.mAllowTouches) {
            return false;
        }
        if (this.mAllowMenuTimeout) {
            repostDelayedHide(2000);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if (this.mAllowMenuTimeout) {
            repostDelayedHide(2000);
        }
        return super.dispatchGenericMotionEvent(motionEvent);
    }

    public void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        boolean z = false;
        if ((this.mSplitScreenControllerOptional.isPresent() && this.mSplitScreenControllerOptional.get().isTaskInSplitScreen(runningTaskInfo.taskId)) || (runningTaskInfo.getWindowingMode() == 1 && runningTaskInfo.supportsSplitScreenMultiWindow && runningTaskInfo.topActivityType != 2)) {
            z = true;
        }
        this.mFocusedTaskAllowSplitScreen = z;
    }

    /* access modifiers changed from: package-private */
    public void showMenu(int i, Rect rect, boolean z, boolean z2, boolean z3, boolean z4) {
        final int i2 = i;
        final boolean z5 = z;
        boolean z6 = z2;
        this.mAllowMenuTimeout = z5;
        this.mDidLastShowMenuResize = z6;
        boolean z7 = this.mContext.getResources().getBoolean(C3343R.bool.config_pipEnableEnterSplitButton);
        int i3 = this.mMenuState;
        if (i3 != i2) {
            this.mAllowTouches = !(z6 && (i3 == 1 || i2 == 1));
            cancelDelayedHide();
            AnimatorSet animatorSet = this.mMenuContainerAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.mMenuContainerAnimator = new AnimatorSet();
            float f = 1.0f;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mMenuContainer, View.ALPHA, new float[]{this.mMenuContainer.getAlpha(), 1.0f});
            ofFloat.addUpdateListener(this.mMenuBgUpdateListener);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mSettingsButton, View.ALPHA, new float[]{this.mSettingsButton.getAlpha(), 1.0f});
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.mDismissButton, View.ALPHA, new float[]{this.mDismissButton.getAlpha(), 1.0f});
            View view = this.mEnterSplitButton;
            Property property = View.ALPHA;
            float[] fArr = new float[2];
            fArr[0] = this.mEnterSplitButton.getAlpha();
            if (!z7 || !this.mFocusedTaskAllowSplitScreen) {
                f = 0.0f;
            }
            fArr[1] = f;
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(view, property, fArr);
            if (i2 == 1) {
                this.mMenuContainerAnimator.playTogether(new Animator[]{ofFloat, ofFloat2, ofFloat3, ofFloat4});
            } else {
                this.mMenuContainerAnimator.playTogether(new Animator[]{ofFloat4});
            }
            this.mMenuContainerAnimator.setInterpolator(Interpolators.ALPHA_IN);
            this.mMenuContainerAnimator.setDuration(125);
            this.mMenuContainerAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    boolean unused = PipMenuView.this.mAllowTouches = true;
                    PipMenuView.this.notifyMenuStateChangeFinish(i2);
                    if (z5) {
                        PipMenuView.this.repostDelayedHide(PipMenuView.INITIAL_DISMISS_DELAY);
                    }
                }

                public void onAnimationCancel(Animator animator) {
                    boolean unused = PipMenuView.this.mAllowTouches = true;
                }
            });
            if (z3) {
                notifyMenuStateChangeStart(i2, z6, new PipMenuView$$ExternalSyntheticLambda1(this));
            } else {
                notifyMenuStateChangeStart(i2, z6, (Runnable) null);
                setVisibility(0);
                this.mMenuContainerAnimator.start();
            }
            updateActionViews(i, rect);
        } else if (z5) {
            repostDelayedHide(2000);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showMenu$4$com-android-wm-shell-pip-phone-PipMenuView  reason: not valid java name */
    public /* synthetic */ void m3467lambda$showMenu$4$comandroidwmshellpipphonePipMenuView() {
        AnimatorSet animatorSet = this.mMenuContainerAnimator;
        if (animatorSet != null) {
            animatorSet.setStartDelay(MENU_SHOW_ON_EXPAND_START_DELAY);
            setVisibility(0);
            this.mMenuContainerAnimator.start();
        }
    }

    /* access modifiers changed from: package-private */
    public void fadeOutMenu() {
        this.mMenuContainer.setAlpha(0.0f);
        this.mSettingsButton.setAlpha(0.0f);
        this.mDismissButton.setAlpha(0.0f);
        this.mEnterSplitButton.setAlpha(0.0f);
    }

    /* access modifiers changed from: package-private */
    public void pokeMenu() {
        cancelDelayedHide();
    }

    /* access modifiers changed from: package-private */
    public void updateMenuLayout(Rect rect) {
        this.mPipMenuIconsAlgorithm.onBoundsChanged(rect);
    }

    /* access modifiers changed from: package-private */
    public void hideMenu() {
        hideMenu((Runnable) null);
    }

    /* access modifiers changed from: package-private */
    public void hideMenu(Runnable runnable) {
        hideMenu(runnable, true, this.mDidLastShowMenuResize, 1);
    }

    /* access modifiers changed from: package-private */
    public void hideMenu(boolean z, int i) {
        hideMenu((Runnable) null, true, z, i);
    }

    /* access modifiers changed from: package-private */
    public void hideMenu(final Runnable runnable, final boolean z, boolean z2, int i) {
        if (this.mMenuState != 0) {
            cancelDelayedHide();
            if (z) {
                notifyMenuStateChangeStart(0, z2, (Runnable) null);
            }
            this.mMenuContainerAnimator = new AnimatorSet();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mMenuContainer, View.ALPHA, new float[]{this.mMenuContainer.getAlpha(), 0.0f});
            ofFloat.addUpdateListener(this.mMenuBgUpdateListener);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mSettingsButton, View.ALPHA, new float[]{this.mSettingsButton.getAlpha(), 0.0f});
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.mDismissButton, View.ALPHA, new float[]{this.mDismissButton.getAlpha(), 0.0f});
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.mEnterSplitButton, View.ALPHA, new float[]{this.mEnterSplitButton.getAlpha(), 0.0f});
            this.mMenuContainerAnimator.playTogether(new Animator[]{ofFloat, ofFloat2, ofFloat3, ofFloat4});
            this.mMenuContainerAnimator.setInterpolator(Interpolators.ALPHA_OUT);
            this.mMenuContainerAnimator.setDuration(getFadeOutDuration(i));
            this.mMenuContainerAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    PipMenuView.this.setVisibility(8);
                    if (z) {
                        PipMenuView.this.notifyMenuStateChangeFinish(0);
                    }
                    Runnable runnable = runnable;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            this.mMenuContainerAnimator.start();
        }
    }

    /* access modifiers changed from: package-private */
    public Size getEstimatedMinMenuSize() {
        return new Size(Math.max(2, this.mActions.size()) * getResources().getDimensionPixelSize(C3343R.dimen.pip_action_size), getResources().getDimensionPixelSize(C3343R.dimen.pip_expand_action_size) + getResources().getDimensionPixelSize(C3343R.dimen.pip_action_padding) + getResources().getDimensionPixelSize(C3343R.dimen.pip_expand_container_edge_margin));
    }

    /* access modifiers changed from: package-private */
    public void setActions(Rect rect, List<RemoteAction> list, RemoteAction remoteAction) {
        this.mActions.clear();
        if (list != null && !list.isEmpty()) {
            this.mActions.addAll(list);
        }
        this.mCloseAction = remoteAction;
        int i = this.mMenuState;
        if (i == 1) {
            updateActionViews(i, rect);
        }
    }

    private void updateActionViews(int i, Rect rect) {
        ViewGroup viewGroup = (ViewGroup) findViewById(C3343R.C3346id.expand_container);
        ViewGroup viewGroup2 = (ViewGroup) findViewById(C3343R.C3346id.actions_container);
        viewGroup2.setOnTouchListener(new PipMenuView$$ExternalSyntheticLambda7());
        viewGroup.setVisibility(i == 1 ? 0 : 4);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
        if (this.mActions.isEmpty() || i == 0) {
            viewGroup2.setVisibility(4);
            layoutParams.topMargin = 0;
            layoutParams.bottomMargin = 0;
        } else {
            viewGroup2.setVisibility(0);
            if (this.mActionsGroup != null) {
                LayoutInflater from = LayoutInflater.from(this.mContext);
                while (this.mActionsGroup.getChildCount() < this.mActions.size()) {
                    this.mActionsGroup.addView((PipMenuActionView) from.inflate(C3343R.layout.pip_menu_action, this.mActionsGroup, false));
                }
                int i2 = 0;
                while (true) {
                    int i3 = 8;
                    if (i2 >= this.mActionsGroup.getChildCount()) {
                        break;
                    }
                    View childAt = this.mActionsGroup.getChildAt(i2);
                    if (i2 < this.mActions.size()) {
                        i3 = 0;
                    }
                    childAt.setVisibility(i3);
                    i2++;
                }
                boolean z = rect != null && rect.width() > rect.height();
                int i4 = 0;
                while (i4 < this.mActions.size()) {
                    RemoteAction remoteAction = this.mActions.get(i4);
                    PipMenuActionView pipMenuActionView = (PipMenuActionView) this.mActionsGroup.getChildAt(i4);
                    RemoteAction remoteAction2 = this.mCloseAction;
                    boolean z2 = remoteAction2 != null && Objects.equals(remoteAction2.getActionIntent(), remoteAction.getActionIntent());
                    remoteAction.getIcon().loadDrawableAsync(this.mContext, new PipMenuView$$ExternalSyntheticLambda8(pipMenuActionView), this.mMainHandler);
                    pipMenuActionView.setCustomCloseBackgroundVisibility(z2 ? 0 : 8);
                    pipMenuActionView.setContentDescription(remoteAction.getContentDescription());
                    if (remoteAction.isEnabled()) {
                        pipMenuActionView.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda9(this, remoteAction, z2));
                    }
                    pipMenuActionView.setEnabled(remoteAction.isEnabled());
                    pipMenuActionView.setAlpha(remoteAction.isEnabled() ? 1.0f : 0.54f);
                    ((LinearLayout.LayoutParams) pipMenuActionView.getLayoutParams()).leftMargin = (!z || i4 <= 0) ? 0 : this.mBetweenActionPaddingLand;
                    i4++;
                }
            }
            layoutParams.topMargin = getResources().getDimensionPixelSize(C3343R.dimen.pip_action_padding);
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(C3343R.dimen.pip_expand_container_edge_margin);
        }
        viewGroup.requestLayout();
    }

    static /* synthetic */ void lambda$updateActionViews$6(PipMenuActionView pipMenuActionView, Drawable drawable) {
        if (drawable != null) {
            drawable.setTint(-1);
            pipMenuActionView.setImageDrawable(drawable);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateActionViews$7$com-android-wm-shell-pip-phone-PipMenuView */
    public /* synthetic */ void mo50397xdc89797b(RemoteAction remoteAction, boolean z, View view) {
        onActionViewClicked(remoteAction.getActionIntent(), z);
    }

    private void notifyMenuStateChangeStart(int i, boolean z, Runnable runnable) {
        this.mController.onMenuStateChangeStart(i, z, runnable);
    }

    /* access modifiers changed from: private */
    public void notifyMenuStateChangeFinish(int i) {
        this.mMenuState = i;
        this.mController.onMenuStateChangeFinish(i);
    }

    private void expandPip() {
        PhonePipMenuController phonePipMenuController = this.mController;
        Objects.requireNonNull(phonePipMenuController);
        hideMenu(new PipMenuView$$ExternalSyntheticLambda2(phonePipMenuController), false, true, 1);
        this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_EXPAND_TO_FULLSCREEN);
    }

    private void dismissPip() {
        if (this.mMenuState != 0) {
            this.mController.onPipDismiss();
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_TAP_TO_REMOVE);
        }
    }

    private void onActionViewClicked(PendingIntent pendingIntent, boolean z) {
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Failed to send action, %s", new Object[]{"PipMenuView", e});
        }
        if (z) {
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_CUSTOM_CLOSE);
            this.mAllowTouches = false;
            this.mMainExecutor.executeDelayed(new PipMenuView$$ExternalSyntheticLambda11(this), (long) this.mPipForceCloseDelay);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onActionViewClicked$8$com-android-wm-shell-pip-phone-PipMenuView */
    public /* synthetic */ void mo50395x37ac381c() {
        hideMenu();
        this.mController.onPipDismiss();
        this.mAllowTouches = true;
    }

    private void enterSplit() {
        PhonePipMenuController phonePipMenuController = this.mController;
        Objects.requireNonNull(phonePipMenuController);
        hideMenu(new PipMenuView$$ExternalSyntheticLambda10(phonePipMenuController), false, true, 1);
    }

    private void showSettings() {
        Pair<ComponentName, Integer> topPipActivity = PipUtils.getTopPipActivity(this.mContext);
        if (topPipActivity.first != null) {
            Intent intent = new Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS", Uri.fromParts("package", ((ComponentName) topPipActivity.first).getPackageName(), (String) null));
            intent.setFlags(268468224);
            this.mContext.startActivityAsUser(intent, UserHandle.of(((Integer) topPipActivity.second).intValue()));
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_SHOW_SETTINGS);
        }
    }

    private void cancelDelayedHide() {
        this.mMainExecutor.removeCallbacks(this.mHideMenuRunnable);
    }

    /* access modifiers changed from: private */
    public void repostDelayedHide(int i) {
        int recommendedTimeoutMillis = this.mAccessibilityManager.getRecommendedTimeoutMillis(i, 5);
        this.mMainExecutor.removeCallbacks(this.mHideMenuRunnable);
        this.mMainExecutor.executeDelayed(this.mHideMenuRunnable, (long) recommendedTimeoutMillis);
    }

    private long getFadeOutDuration(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 125;
        }
        if (i == 2) {
            return (long) this.mDismissFadeOutDurationMs;
        }
        throw new IllegalStateException("Invalid animation type " + i);
    }
}
