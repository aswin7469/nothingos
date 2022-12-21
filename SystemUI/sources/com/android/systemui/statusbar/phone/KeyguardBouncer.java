package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Handler;
import android.os.Trace;
import android.os.UserManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowInsets;
import com.android.internal.policy.SystemBarUtils;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.ViewMediatorCallback;
import com.android.keyguard.dagger.KeyguardBouncerComponent;
import com.android.systemui.DejankUtils;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ListenerSet;
import com.nothing.systemui.util.SystemUIEventUtils;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;

public class KeyguardBouncer {
    public static final float ALPHA_EXPANSION_THRESHOLD = 0.95f;
    static final long BOUNCER_FACE_DELAY = 1200;
    public static final float EXPANSION_HIDDEN = 1.0f;
    public static final float EXPANSION_VISIBLE = 0.0f;
    private static final String TAG = "KeyguardBouncer";
    /* access modifiers changed from: private */
    public int mBouncerPromptReason;
    protected final ViewMediatorCallback mCallback;
    protected final ViewGroup mContainer;
    protected final Context mContext;
    private final DismissCallbackRegistry mDismissCallbackRegistry;
    /* access modifiers changed from: private */
    public float mExpansion;
    private final List<BouncerExpansionCallback> mExpansionCallbacks;
    private final FalsingCollector mFalsingCollector;
    private final Handler mHandler;
    private boolean mInitialized;
    private boolean mIsAnimatingAway;
    private boolean mIsScrimmed;
    private final KeyguardBouncerComponent.Factory mKeyguardBouncerComponentFactory;
    private final KeyguardBypassController mKeyguardBypassController;
    private final KeyguardSecurityModel mKeyguardSecurityModel;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public KeyguardHostViewController mKeyguardViewController;
    private final Runnable mRemoveViewRunnable;
    private final ListenerSet<KeyguardResetCallback> mResetCallbacks;
    private final Runnable mResetRunnable;
    private final Runnable mShowRunnable;
    /* access modifiers changed from: private */
    public boolean mShowingSoon;
    /* access modifiers changed from: private */
    public int mStatusBarHeight;
    private final KeyguardUpdateMonitorCallback mUpdateMonitorCallback;

    public interface BouncerExpansionCallback {
        void onExpansionChanged(float f) {
        }

        void onFullyHidden() {
        }

        void onFullyShown() {
        }

        void onStartingToHide() {
        }

        void onStartingToShow() {
        }

        void onVisibilityChanged(boolean z) {
        }
    }

    public interface KeyguardResetCallback {
        void onKeyguardReset();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-phone-KeyguardBouncer */
    public /* synthetic */ void mo44190x93953a0e() {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.resetSecurityContainer();
            Iterator<KeyguardResetCallback> it = this.mResetCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onKeyguardReset();
            }
        }
    }

    private KeyguardBouncer(Context context, ViewMediatorCallback viewMediatorCallback, ViewGroup viewGroup, DismissCallbackRegistry dismissCallbackRegistry, FalsingCollector falsingCollector, BouncerExpansionCallback bouncerExpansionCallback, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardBypassController keyguardBypassController, @Main Handler handler, KeyguardSecurityModel keyguardSecurityModel, KeyguardBouncerComponent.Factory factory) {
        ArrayList arrayList = new ArrayList();
        this.mExpansionCallbacks = arrayList;
        C29721 r1 = new KeyguardUpdateMonitorCallback() {
            public void onStrongAuthStateChanged(int i) {
                KeyguardBouncer keyguardBouncer = KeyguardBouncer.this;
                int unused = keyguardBouncer.mBouncerPromptReason = keyguardBouncer.mCallback.getBouncerPromptReason();
            }

            public void onLockedOutStateChanged(BiometricSourceType biometricSourceType) {
                if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
                    KeyguardBouncer keyguardBouncer = KeyguardBouncer.this;
                    int unused = keyguardBouncer.mBouncerPromptReason = keyguardBouncer.mCallback.getBouncerPromptReason();
                }
            }
        };
        this.mUpdateMonitorCallback = r1;
        this.mRemoveViewRunnable = new KeyguardBouncer$$ExternalSyntheticLambda0(this);
        this.mResetCallbacks = new ListenerSet<>();
        this.mResetRunnable = new KeyguardBouncer$$ExternalSyntheticLambda1(this);
        this.mExpansion = 1.0f;
        this.mShowRunnable = new Runnable() {
            public void run() {
                KeyguardBouncer.this.setVisibility(0);
                KeyguardBouncer keyguardBouncer = KeyguardBouncer.this;
                keyguardBouncer.showPromptReason(keyguardBouncer.mBouncerPromptReason);
                CharSequence consumeCustomMessage = KeyguardBouncer.this.mCallback.consumeCustomMessage();
                if (consumeCustomMessage != null) {
                    KeyguardBouncer.this.mKeyguardViewController.showErrorMessage(consumeCustomMessage);
                }
                KeyguardBouncer.this.mKeyguardViewController.appear(KeyguardBouncer.this.mStatusBarHeight);
                boolean unused = KeyguardBouncer.this.mShowingSoon = false;
                if (KeyguardBouncer.this.mExpansion == 0.0f) {
                    KeyguardBouncer.this.mKeyguardViewController.onResume();
                    KeyguardBouncer.this.mKeyguardViewController.resetSecurityContainer();
                    KeyguardBouncer keyguardBouncer2 = KeyguardBouncer.this;
                    keyguardBouncer2.showPromptReason(keyguardBouncer2.mBouncerPromptReason);
                }
            }
        };
        this.mContext = context;
        this.mCallback = viewMediatorCallback;
        this.mContainer = viewGroup;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mFalsingCollector = falsingCollector;
        this.mDismissCallbackRegistry = dismissCallbackRegistry;
        this.mHandler = handler;
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardSecurityModel = keyguardSecurityModel;
        this.mKeyguardBouncerComponentFactory = factory;
        keyguardUpdateMonitor.registerCallback(r1);
        this.mKeyguardBypassController = keyguardBypassController;
        arrayList.add(bouncerExpansionCallback);
    }

    public void setBackButtonEnabled(boolean z) {
        int systemUiVisibility = this.mContainer.getSystemUiVisibility();
        this.mContainer.setSystemUiVisibility(z ? -4194305 & systemUiVisibility : 4194304 | systemUiVisibility);
    }

    public void show(boolean z) {
        show(z, true);
    }

    public void show(boolean z, boolean z2) {
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        if (currentUser != 0 || !UserManager.isSplitSystemUser()) {
            try {
                Trace.beginSection("KeyguardBouncer#show");
                ensureView();
                this.mIsScrimmed = z2;
                if (z2) {
                    setExpansion(0.0f);
                }
                if (z) {
                    showPrimarySecurityScreen();
                }
                if (this.mContainer.getVisibility() != 0) {
                    if (!this.mShowingSoon) {
                        int currentUser2 = KeyguardUpdateMonitor.getCurrentUser();
                        boolean z3 = false;
                        if (!(UserManager.isSplitSystemUser() && currentUser2 == 0) && currentUser2 == currentUser) {
                            z3 = true;
                        }
                        if (!z3 || !this.mKeyguardViewController.dismiss(currentUser2)) {
                            if (!z3) {
                                Log.w(TAG, "User can't dismiss keyguard: " + currentUser2 + " != " + currentUser);
                            }
                            this.mShowingSoon = true;
                            DejankUtils.removeCallbacks(this.mResetRunnable);
                            if (!this.mKeyguardStateController.isFaceAuthEnabled() || needsFullscreenBouncer() || this.mKeyguardUpdateMonitor.userNeedsStrongAuth() || this.mKeyguardBypassController.getBypassEnabled()) {
                                DejankUtils.postAfterTraversal(this.mShowRunnable);
                            } else {
                                this.mHandler.postDelayed(this.mShowRunnable, 1200);
                            }
                            this.mKeyguardStateController.notifyBouncerShowing(true);
                            dispatchStartingToShow();
                            Trace.endSection();
                            return;
                        }
                        SystemUIEventUtils.collectUnLockResults(this.mContext, SystemUIEventUtils.EVENT_PROPERTY_KEY_UNLOCK_SUCCESS, 1);
                        Trace.endSection();
                        return;
                    }
                }
                if (needsFullscreenBouncer()) {
                    this.mKeyguardViewController.onResume();
                }
            } finally {
                Trace.endSection();
            }
        }
    }

    public boolean isScrimmed() {
        return this.mIsScrimmed;
    }

    private void onFullyShown() {
        this.mFalsingCollector.onBouncerShown();
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController == null) {
            Log.wtf(TAG, "onFullyShown when view was null");
            return;
        }
        keyguardHostViewController.onResume();
        this.mContainer.announceForAccessibility(this.mKeyguardViewController.getAccessibilityTitleForCurrentMode());
    }

    private void onFullyHidden() {
        cancelShowRunnable();
        setVisibility(4);
        this.mFalsingCollector.onBouncerHidden();
        DejankUtils.postAfterTraversal(this.mResetRunnable);
    }

    /* access modifiers changed from: private */
    public void setVisibility(int i) {
        this.mContainer.setVisibility(i);
        dispatchVisibilityChanged();
    }

    public void showPromptReason(int i) {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.showPromptReason(i);
        } else {
            Log.w(TAG, "Trying to show prompt reason on empty bouncer");
        }
    }

    public void showMessage(String str, ColorStateList colorStateList) {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.showMessage(str, colorStateList);
        } else {
            Log.w(TAG, "Trying to show message on empty bouncer");
        }
    }

    private void cancelShowRunnable() {
        DejankUtils.removeCallbacks(this.mShowRunnable);
        this.mHandler.removeCallbacks(this.mShowRunnable);
        this.mShowingSoon = false;
    }

    public void showWithDismissAction(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable) {
        ensureView();
        setDismissAction(onDismissAction, runnable);
        show(false);
    }

    public void setDismissAction(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable) {
        this.mKeyguardViewController.setOnDismissAction(onDismissAction, runnable);
    }

    public void hide(boolean z) {
        Trace.beginSection("KeyguardBouncer#hide");
        if (isShowing()) {
            SysUiStatsLog.write(63, 1);
            this.mDismissCallbackRegistry.notifyDismissCancelled();
        }
        this.mIsScrimmed = false;
        this.mFalsingCollector.onBouncerHidden();
        this.mKeyguardStateController.notifyBouncerShowing(false);
        cancelShowRunnable();
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.cancelDismissAction();
            this.mKeyguardViewController.cleanUp();
        }
        this.mIsAnimatingAway = false;
        setVisibility(4);
        if (z) {
            this.mHandler.postDelayed(this.mRemoveViewRunnable, 50);
        }
        Trace.endSection();
    }

    public void startPreHideAnimation(Runnable runnable) {
        this.mIsAnimatingAway = true;
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.startDisappearAnimation(runnable);
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public void reset() {
        cancelShowRunnable();
        inflateView();
        this.mFalsingCollector.onBouncerHidden();
    }

    public void onScreenTurnedOff() {
        if (this.mKeyguardViewController != null && this.mContainer.getVisibility() == 0) {
            this.mKeyguardViewController.onPause();
        }
    }

    public boolean isShowing() {
        return (this.mShowingSoon || this.mContainer.getVisibility() == 0) && this.mExpansion == 0.0f && !isAnimatingAway();
    }

    public boolean inTransit() {
        if (!this.mShowingSoon) {
            float f = this.mExpansion;
            if (f == 1.0f || f == 0.0f) {
                return false;
            }
        }
        return true;
    }

    public boolean getShowingSoon() {
        return this.mShowingSoon;
    }

    public boolean isAnimatingAway() {
        return this.mIsAnimatingAway;
    }

    public void prepare() {
        boolean z = this.mInitialized;
        ensureView();
        if (z) {
            showPrimarySecurityScreen();
        }
        this.mBouncerPromptReason = this.mCallback.getBouncerPromptReason();
    }

    private void showPrimarySecurityScreen() {
        this.mKeyguardViewController.showPrimarySecurityScreen();
    }

    public void setExpansion(float f) {
        float f2 = this.mExpansion;
        boolean z = f2 != f;
        this.mExpansion = f;
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null && !this.mIsAnimatingAway) {
            keyguardHostViewController.setExpansion(f);
        }
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i == 0 && f2 != 0.0f) {
            onFullyShown();
            dispatchFullyShown();
        } else if (f == 1.0f && f2 != 1.0f) {
            onFullyHidden();
            dispatchFullyHidden();
        } else if (i != 0 && f2 == 0.0f) {
            dispatchStartingToHide();
            KeyguardHostViewController keyguardHostViewController2 = this.mKeyguardViewController;
            if (keyguardHostViewController2 != null) {
                keyguardHostViewController2.onStartingToHide();
            }
        }
        if (z) {
            dispatchExpansionChanged();
        }
    }

    public boolean willDismissWithAction() {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        return keyguardHostViewController != null && keyguardHostViewController.hasDismissActions();
    }

    public int getTop() {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController == null) {
            return 0;
        }
        return keyguardHostViewController.getTop();
    }

    /* access modifiers changed from: protected */
    public void ensureView() {
        boolean hasCallbacks = this.mHandler.hasCallbacks(this.mRemoveViewRunnable);
        if (!this.mInitialized || hasCallbacks) {
            inflateView();
        }
    }

    /* access modifiers changed from: protected */
    public void inflateView() {
        removeView();
        this.mHandler.removeCallbacks(this.mRemoveViewRunnable);
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardBouncerComponentFactory.create(this.mContainer).getKeyguardHostViewController();
        this.mKeyguardViewController = keyguardHostViewController;
        keyguardHostViewController.init();
        this.mStatusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
        setVisibility(4);
        WindowInsets rootWindowInsets = this.mContainer.getRootWindowInsets();
        if (rootWindowInsets != null) {
            this.mContainer.dispatchApplyWindowInsets(rootWindowInsets);
        }
        this.mInitialized = true;
    }

    /* access modifiers changed from: protected */
    public void removeView() {
        this.mContainer.removeAllViews();
        this.mInitialized = false;
    }

    public boolean needsFullscreenBouncer() {
        KeyguardSecurityModel.SecurityMode securityMode = this.mKeyguardSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser());
        return securityMode == KeyguardSecurityModel.SecurityMode.SimPin || securityMode == KeyguardSecurityModel.SecurityMode.SimPuk;
    }

    public boolean isFullscreenBouncer() {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController == null) {
            return false;
        }
        KeyguardSecurityModel.SecurityMode currentSecurityMode = keyguardHostViewController.getCurrentSecurityMode();
        if (currentSecurityMode == KeyguardSecurityModel.SecurityMode.SimPin || currentSecurityMode == KeyguardSecurityModel.SecurityMode.SimPuk) {
            return true;
        }
        return false;
    }

    public boolean isSecure() {
        return this.mKeyguardSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser()) != KeyguardSecurityModel.SecurityMode.None;
    }

    public boolean shouldDismissOnMenuPressed() {
        return this.mKeyguardViewController.shouldEnableMenuKey();
    }

    public boolean interceptMediaKey(KeyEvent keyEvent) {
        ensureView();
        return this.mKeyguardViewController.interceptMediaKey(keyEvent);
    }

    public boolean dispatchBackKeyEventPreIme() {
        ensureView();
        return this.mKeyguardViewController.dispatchBackKeyEventPreIme();
    }

    public void notifyKeyguardAuthenticated(boolean z) {
        ensureView();
        this.mKeyguardViewController.finish(z, KeyguardUpdateMonitor.getCurrentUser());
    }

    private void dispatchFullyShown() {
        for (BouncerExpansionCallback onFullyShown : this.mExpansionCallbacks) {
            onFullyShown.onFullyShown();
        }
    }

    private void dispatchStartingToHide() {
        for (BouncerExpansionCallback onStartingToHide : this.mExpansionCallbacks) {
            onStartingToHide.onStartingToHide();
        }
    }

    private void dispatchStartingToShow() {
        for (BouncerExpansionCallback onStartingToShow : this.mExpansionCallbacks) {
            onStartingToShow.onStartingToShow();
        }
    }

    private void dispatchFullyHidden() {
        for (BouncerExpansionCallback onFullyHidden : this.mExpansionCallbacks) {
            onFullyHidden.onFullyHidden();
        }
    }

    private void dispatchExpansionChanged() {
        for (BouncerExpansionCallback onExpansionChanged : this.mExpansionCallbacks) {
            onExpansionChanged.onExpansionChanged(this.mExpansion);
        }
    }

    private void dispatchVisibilityChanged() {
        for (BouncerExpansionCallback onVisibilityChanged : this.mExpansionCallbacks) {
            onVisibilityChanged.onVisibilityChanged(this.mContainer.getVisibility() == 0);
        }
    }

    public void updateResources() {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.updateResources();
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(TAG);
        printWriter.println("  isShowing(): " + isShowing());
        printWriter.println("  mStatusBarHeight: " + this.mStatusBarHeight);
        printWriter.println("  mExpansion: " + this.mExpansion);
        printWriter.println("  mKeyguardViewController; " + this.mKeyguardViewController);
        printWriter.println("  mShowingSoon: " + this.mShowingSoon);
        printWriter.println("  mBouncerPromptReason: " + this.mBouncerPromptReason);
        printWriter.println("  mIsAnimatingAway: " + this.mIsAnimatingAway);
        printWriter.println("  mInitialized: " + this.mInitialized);
    }

    public void updateKeyguardPosition(float f) {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.updateKeyguardPosition(f);
        }
    }

    public void addKeyguardResetCallback(KeyguardResetCallback keyguardResetCallback) {
        this.mResetCallbacks.addIfAbsent(keyguardResetCallback);
    }

    public void removeKeyguardResetCallback(KeyguardResetCallback keyguardResetCallback) {
        this.mResetCallbacks.remove(keyguardResetCallback);
    }

    public void addBouncerExpansionCallback(BouncerExpansionCallback bouncerExpansionCallback) {
        if (!this.mExpansionCallbacks.contains(bouncerExpansionCallback)) {
            this.mExpansionCallbacks.add(bouncerExpansionCallback);
        }
    }

    public void removeBouncerExpansionCallback(BouncerExpansionCallback bouncerExpansionCallback) {
        this.mExpansionCallbacks.remove((Object) bouncerExpansionCallback);
    }

    public static class Factory {
        private final ViewMediatorCallback mCallback;
        private final Context mContext;
        private final DismissCallbackRegistry mDismissCallbackRegistry;
        private final FalsingCollector mFalsingCollector;
        private final Handler mHandler;
        private final KeyguardBouncerComponent.Factory mKeyguardBouncerComponentFactory;
        private final KeyguardBypassController mKeyguardBypassController;
        private final KeyguardSecurityModel mKeyguardSecurityModel;
        private final KeyguardStateController mKeyguardStateController;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

        @Inject
        public Factory(Context context, ViewMediatorCallback viewMediatorCallback, DismissCallbackRegistry dismissCallbackRegistry, FalsingCollector falsingCollector, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardBypassController keyguardBypassController, @Main Handler handler, KeyguardSecurityModel keyguardSecurityModel, KeyguardBouncerComponent.Factory factory) {
            this.mContext = context;
            this.mCallback = viewMediatorCallback;
            this.mDismissCallbackRegistry = dismissCallbackRegistry;
            this.mFalsingCollector = falsingCollector;
            this.mKeyguardStateController = keyguardStateController;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mKeyguardBypassController = keyguardBypassController;
            this.mHandler = handler;
            this.mKeyguardSecurityModel = keyguardSecurityModel;
            this.mKeyguardBouncerComponentFactory = factory;
        }

        public KeyguardBouncer create(ViewGroup viewGroup, BouncerExpansionCallback bouncerExpansionCallback) {
            return new KeyguardBouncer(this.mContext, this.mCallback, viewGroup, this.mDismissCallbackRegistry, this.mFalsingCollector, bouncerExpansionCallback, this.mKeyguardStateController, this.mKeyguardUpdateMonitor, this.mKeyguardBypassController, this.mHandler, this.mKeyguardSecurityModel, this.mKeyguardBouncerComponentFactory);
        }
    }

    public void notifyKeyguardAuthenticatedForFaceRecognition(boolean z) {
        ensureView();
        this.mKeyguardViewController.finish(z, KeyguardUpdateMonitor.getCurrentUser());
    }
}
