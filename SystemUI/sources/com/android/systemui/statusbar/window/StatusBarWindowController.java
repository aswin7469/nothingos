package com.android.systemui.statusbar.window;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Binder;
import android.os.RemoteException;
import android.os.Trace;
import android.view.DisplayCutout;
import android.view.IWindowManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.C1893R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DelegateLaunchAnimatorController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.util.JankMonitorTransitionProgressListener;
import java.util.Optional;
import javax.inject.Inject;

@SysUISingleton
public class StatusBarWindowController {
    private static final boolean DEBUG = false;
    private static final String TAG = "StatusBarWindowController";
    private int mBarHeight = -1;
    private final StatusBarContentInsetsProvider mContentInsetsProvider;
    private final Context mContext;
    private final State mCurrentState = new State();
    private final IWindowManager mIWindowManager;
    private boolean mIsAttached;
    private final ViewGroup mLaunchAnimationContainer;
    private WindowManager.LayoutParams mLp;
    private final WindowManager.LayoutParams mLpChanged;
    private final ViewGroup mStatusBarWindowView;
    private final WindowManager mWindowManager;

    @Inject
    public StatusBarWindowController(Context context, StatusBarWindowView statusBarWindowView, WindowManager windowManager, IWindowManager iWindowManager, StatusBarContentInsetsProvider statusBarContentInsetsProvider, @Main Resources resources, Optional<UnfoldTransitionProgressProvider> optional) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mIWindowManager = iWindowManager;
        this.mContentInsetsProvider = statusBarContentInsetsProvider;
        this.mStatusBarWindowView = statusBarWindowView;
        this.mLaunchAnimationContainer = (ViewGroup) statusBarWindowView.findViewById(C1893R.C1897id.status_bar_launch_animation_container);
        this.mLpChanged = new WindowManager.LayoutParams();
        if (this.mBarHeight < 0) {
            this.mBarHeight = SystemBarUtils.getStatusBarHeight(context);
        }
        optional.ifPresent(new StatusBarWindowController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-statusbar-window-StatusBarWindowController */
    public /* synthetic */ void mo46272xe1f6d108(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        unfoldTransitionProgressProvider.addCallback(new JankMonitorTransitionProgressListener(new StatusBarWindowController$$ExternalSyntheticLambda1(this)));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-window-StatusBarWindowController */
    public /* synthetic */ View mo46271x94375907() {
        return this.mStatusBarWindowView;
    }

    public int getStatusBarHeight() {
        return this.mBarHeight;
    }

    public void refreshStatusBarHeight() {
        int statusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
        if (this.mBarHeight != statusBarHeight) {
            this.mBarHeight = statusBarHeight;
            apply(this.mCurrentState);
        }
    }

    public void attach() {
        Trace.beginSection("StatusBarWindowController.getBarLayoutParams");
        this.mLp = getBarLayoutParams(this.mContext.getDisplay().getRotation());
        Trace.endSection();
        this.mWindowManager.addView(this.mStatusBarWindowView, this.mLp);
        this.mLpChanged.copyFrom(this.mLp);
        this.mContentInsetsProvider.addCallback((StatusBarContentInsetsChangedListener) new StatusBarWindowController$$ExternalSyntheticLambda2(this));
        calculateStatusBarLocationsForAllRotations();
        this.mIsAttached = true;
        apply(this.mCurrentState);
    }

    public void addViewToWindow(View view, ViewGroup.LayoutParams layoutParams) {
        this.mStatusBarWindowView.addView(view, layoutParams);
    }

    public View getBackgroundView() {
        return this.mStatusBarWindowView.findViewById(C1893R.C1897id.status_bar_container);
    }

    public FragmentHostManager getFragmentHostManager() {
        return FragmentHostManager.get(this.mStatusBarWindowView);
    }

    public Optional<ActivityLaunchAnimator.Controller> wrapAnimationControllerIfInStatusBar(View view, ActivityLaunchAnimator.Controller controller) {
        if (view != this.mStatusBarWindowView) {
            return Optional.empty();
        }
        controller.setLaunchContainer(this.mLaunchAnimationContainer);
        return Optional.m1745of(new DelegateLaunchAnimatorController(controller) {
            public void onLaunchAnimationStart(boolean z) {
                getDelegate().onLaunchAnimationStart(z);
                StatusBarWindowController.this.setLaunchAnimationRunning(true);
            }

            public void onLaunchAnimationEnd(boolean z) {
                getDelegate().onLaunchAnimationEnd(z);
                StatusBarWindowController.this.setLaunchAnimationRunning(false);
            }
        });
    }

    private WindowManager.LayoutParams getBarLayoutParams(int i) {
        WindowManager.LayoutParams barLayoutParamsForRotation = getBarLayoutParamsForRotation(i);
        barLayoutParamsForRotation.paramsForRotation = new WindowManager.LayoutParams[4];
        for (int i2 = 0; i2 <= 3; i2++) {
            barLayoutParamsForRotation.paramsForRotation[i2] = getBarLayoutParamsForRotation(i2);
        }
        return barLayoutParamsForRotation;
    }

    private WindowManager.LayoutParams getBarLayoutParamsForRotation(int i) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, SystemBarUtils.getStatusBarHeightForRotation(this.mContext, i), 2000, -2139095032, -3);
        layoutParams.privateFlags |= 16777216;
        layoutParams.token = new Binder();
        layoutParams.gravity = 48;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("StatusBar");
        layoutParams.packageName = this.mContext.getPackageName();
        layoutParams.layoutInDisplayCutoutMode = 3;
        return layoutParams;
    }

    /* access modifiers changed from: private */
    public void calculateStatusBarLocationsForAllRotations() {
        DisplayCutout cutout = this.mContext.getDisplay().getCutout();
        try {
            this.mIWindowManager.updateStaticPrivacyIndicatorBounds(this.mContext.getDisplayId(), new Rect[]{this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(0, cutout), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(1, cutout), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(2, cutout), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(3, cutout)});
        } catch (RemoteException unused) {
        }
    }

    public void setForceStatusBarVisible(boolean z) {
        this.mCurrentState.mForceStatusBarVisible = z;
        apply(this.mCurrentState);
    }

    public void setOngoingProcessRequiresStatusBarVisible(boolean z) {
        this.mCurrentState.mOngoingProcessRequiresStatusBarVisible = z;
        apply(this.mCurrentState);
    }

    /* access modifiers changed from: private */
    public void setLaunchAnimationRunning(boolean z) {
        if (z != this.mCurrentState.mIsLaunchAnimationRunning) {
            this.mCurrentState.mIsLaunchAnimationRunning = z;
            apply(this.mCurrentState);
        }
    }

    private void applyHeight(State state) {
        int i;
        this.mLpChanged.height = state.mIsLaunchAnimationRunning ? -1 : this.mBarHeight;
        for (int i2 = 0; i2 <= 3; i2++) {
            WindowManager.LayoutParams layoutParams = this.mLpChanged.paramsForRotation[i2];
            if (state.mIsLaunchAnimationRunning) {
                i = -1;
            } else {
                i = SystemBarUtils.getStatusBarHeightForRotation(this.mContext, i2);
            }
            layoutParams.height = i;
        }
    }

    private void apply(State state) {
        if (this.mIsAttached) {
            applyForceStatusBarVisibleFlag(state);
            applyHeight(state);
            WindowManager.LayoutParams layoutParams = this.mLp;
            if (layoutParams != null && layoutParams.copyFrom(this.mLpChanged) != 0) {
                this.mWindowManager.updateViewLayout(this.mStatusBarWindowView, this.mLp);
            }
        }
    }

    private static class State {
        boolean mForceStatusBarVisible;
        boolean mIsLaunchAnimationRunning;
        boolean mOngoingProcessRequiresStatusBarVisible;

        private State() {
        }
    }

    private void applyForceStatusBarVisibleFlag(State state) {
        if (state.mForceStatusBarVisible || state.mIsLaunchAnimationRunning || state.mOngoingProcessRequiresStatusBarVisible) {
            this.mLpChanged.privateFlags |= 4096;
            return;
        }
        this.mLpChanged.privateFlags &= -4097;
    }
}
