package com.android.systemui.globalactions;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.view.RotationPolicy;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.GlobalActionsPanelPlugin;
import com.android.systemui.util.leak.RotationUtils;
import javax.inject.Provider;
@VisibleForTesting
/* loaded from: classes.dex */
class GlobalActionsDialog$ActionsDialog extends GlobalActionsDialogLite.ActionsDialogLite {
    private TextView mLockMessage;
    @VisibleForTesting
    ViewGroup mLockMessageContainer;
    private ResetOrientationData mResetOrientationData;
    private final Provider<GlobalActionsPanelPlugin.PanelViewController> mWalletFactory;
    private GlobalActionsPanelPlugin.PanelViewController mWalletViewController;

    private boolean isWalletViewAvailable() {
        GlobalActionsPanelPlugin.PanelViewController panelViewController = this.mWalletViewController;
        return (panelViewController == null || panelViewController.getPanelContent() == null) ? false : true;
    }

    private void initializeWalletView() {
        Provider<GlobalActionsPanelPlugin.PanelViewController> provider = this.mWalletFactory;
        if (provider == null) {
            return;
        }
        this.mWalletViewController = provider.mo1933get();
        if (!isWalletViewAvailable()) {
            return;
        }
        boolean z = this.mContext.getResources().getBoolean(R$bool.global_actions_show_landscape_wallet_view);
        int rotation = RotationUtils.getRotation(this.mContext);
        boolean isRotationLocked = RotationPolicy.isRotationLocked(this.mContext);
        if (rotation == 0) {
            if (!isRotationLocked && this.mResetOrientationData == null) {
                ResetOrientationData resetOrientationData = new ResetOrientationData(null);
                this.mResetOrientationData = resetOrientationData;
                resetOrientationData.locked = false;
            }
            final boolean z2 = !z;
            if (isRotationLocked != z2) {
                this.mGlobalActionsLayout.post(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialog$ActionsDialog$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        GlobalActionsDialog$ActionsDialog.this.lambda$initializeWalletView$1(z2);
                    }
                });
            }
        } else if (isRotationLocked) {
            if (this.mResetOrientationData == null) {
                ResetOrientationData resetOrientationData2 = new ResetOrientationData(null);
                this.mResetOrientationData = resetOrientationData2;
                resetOrientationData2.locked = true;
                resetOrientationData2.rotation = rotation;
            }
            this.mGlobalActionsLayout.post(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialog$ActionsDialog$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    GlobalActionsDialog$ActionsDialog.this.lambda$initializeWalletView$0();
                }
            });
            if (!z) {
                return;
            }
        }
        setRotationSuggestionsEnabled(false);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.topMargin = this.mContext.getResources().getDimensionPixelSize(R$dimen.global_actions_wallet_top_margin);
        View panelContent = this.mWalletViewController.getPanelContent();
        ((FrameLayout) findViewById(R$id.global_actions_wallet)).addView(panelContent, layoutParams);
        final ViewGroup viewGroup = (ViewGroup) findViewById(R$id.global_actions_grid_root);
        if (viewGroup == null) {
            return;
        }
        panelContent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialog$ActionsDialog$$ExternalSyntheticLambda2
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                GlobalActionsDialog$ActionsDialog.lambda$initializeWalletView$2(viewGroup, view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeWalletView$0() {
        RotationPolicy.setRotationLockAtAngle(this.mContext, false, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeWalletView$1(boolean z) {
        RotationPolicy.setRotationLockAtAngle(this.mContext, z, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$initializeWalletView$2(ViewGroup viewGroup, View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9 = i8 - i6;
        int i10 = i4 - i2;
        if (i9 <= 0 || i9 == i10) {
            return;
        }
        TransitionManager.beginDelayedTransition(viewGroup, new AutoTransition().setDuration(250L).setOrdering(0));
    }

    @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite
    protected int getLayoutResource() {
        return R$layout.global_actions_grid_v2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite
    public void initializeLayout() {
        super.initializeLayout();
        this.mLockMessageContainer = (ViewGroup) requireViewById(R$id.global_actions_lock_message_container);
        this.mLockMessage = (TextView) requireViewById(R$id.global_actions_lock_message);
        initializeWalletView();
        getWindow().setBackgroundDrawable(this.mBackgroundDrawable);
    }

    @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite
    protected void showDialog() {
        this.mShowing = true;
        this.mNotificationShadeWindowController.setRequestTopUi(true, "GlobalActionsDialog");
        this.mSysUiState.setFlag(32768, true).commitUpdate(this.mContext.getDisplayId());
        final ViewGroup viewGroup = (ViewGroup) this.mGlobalActionsLayout.getRootView();
        viewGroup.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialog$ActionsDialog$$ExternalSyntheticLambda1
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                WindowInsets lambda$showDialog$3;
                lambda$showDialog$3 = GlobalActionsDialog$ActionsDialog.lambda$showDialog$3(viewGroup, view, windowInsets);
                return lambda$showDialog$3;
            }
        });
        this.mBackgroundDrawable.setAlpha(0);
        float animationOffsetX = this.mGlobalActionsLayout.getAnimationOffsetX();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mContainer, "alpha", 0.0f, 1.0f);
        Interpolator interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
        ofFloat.setInterpolator(interpolator);
        ofFloat.setDuration(183L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialog$ActionsDialog$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                GlobalActionsDialog$ActionsDialog.this.lambda$showDialog$4(valueAnimator);
            }
        });
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mContainer, "translationX", animationOffsetX, 0.0f);
        ofFloat2.setInterpolator(interpolator);
        ofFloat2.setDuration(350L);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WindowInsets lambda$showDialog$3(ViewGroup viewGroup, View view, WindowInsets windowInsets) {
        viewGroup.setPadding(windowInsets.getStableInsetLeft(), windowInsets.getStableInsetTop(), windowInsets.getStableInsetRight(), windowInsets.getStableInsetBottom());
        return WindowInsets.CONSUMED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDialog$4(ValueAnimator valueAnimator) {
        this.mBackgroundDrawable.setAlpha((int) (valueAnimator.getAnimatedFraction() * this.mScrimAlpha * 255.0f));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite
    public void dismissInternal() {
        super.lambda$dismiss$7();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite
    public void completeDismiss() {
        dismissWallet();
        resetOrientation();
        super.completeDismiss();
    }

    private void dismissWallet() {
        GlobalActionsPanelPlugin.PanelViewController panelViewController = this.mWalletViewController;
        if (panelViewController != null) {
            panelViewController.onDismissed();
            this.mWalletViewController = null;
        }
    }

    private void resetOrientation() {
        ResetOrientationData resetOrientationData = this.mResetOrientationData;
        if (resetOrientationData != null) {
            RotationPolicy.setRotationLockAtAngle(this.mContext, resetOrientationData.locked, resetOrientationData.rotation);
        }
        setRotationSuggestionsEnabled(true);
    }

    @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite
    public void refreshDialog() {
        dismissWallet();
        super.refreshDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ResetOrientationData {
        public boolean locked;
        public int rotation;

        private ResetOrientationData() {
        }

        /* synthetic */ ResetOrientationData(GlobalActionsDialog$1 globalActionsDialog$1) {
            this();
        }
    }
}
