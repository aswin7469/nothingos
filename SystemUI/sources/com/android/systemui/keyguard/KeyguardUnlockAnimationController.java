package com.android.systemui.keyguard;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import androidx.core.math.MathUtils;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.flags.BooleanFlag;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.shared.recents.utilities.Utilities;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController;
import com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController;
import com.android.systemui.shared.system.smartspace.SmartspaceState;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import java.util.ArrayList;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.JvmDefault;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000®\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b$\b\u0007\u0018\u0000 r2\u00020\u00012\u00020\u0002:\u0002rsBS\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\b\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013¢\u0006\u0002\u0010\u0014J\u000e\u0010O\u001a\u00020P2\u0006\u0010Q\u001a\u00020!J\u0010\u0010R\u001a\u00020P2\u0006\u0010S\u001a\u00020AH\u0002J\u0006\u0010T\u001a\u00020*J\b\u0010U\u001a\u00020PH\u0002J\b\u0010V\u001a\u00020PH\u0002J\b\u0010W\u001a\u00020PH\u0002J\u0006\u0010X\u001a\u00020PJ\u0006\u0010Y\u001a\u00020*J\u0006\u0010Z\u001a\u00020*J\u0006\u0010[\u001a\u00020*J\u000e\u0010\\\u001a\u00020P2\u0006\u0010]\u001a\u00020*J\u001e\u0010^\u001a\u00020P2\u0006\u0010_\u001a\u00020E2\u0006\u0010`\u001a\u00020C2\u0006\u0010a\u001a\u00020*J\b\u0010b\u001a\u00020PH\u0016J\b\u0010c\u001a\u00020PH\u0016J\u0012\u0010d\u001a\u00020P2\b\u0010e\u001a\u0004\u0018\u00010\u0018H\u0016J\b\u0010f\u001a\u00020PH\u0002J\u0006\u0010g\u001a\u00020PJ\u000e\u0010h\u001a\u00020P2\u0006\u0010Q\u001a\u00020!J\u0012\u0010i\u001a\u00020P2\b\u0010j\u001a\u0004\u0018\u00010\u001eH\u0016J\u000e\u0010k\u001a\u00020P2\u0006\u0010l\u001a\u000200J\b\u0010m\u001a\u00020*H\u0002J\b\u0010n\u001a\u00020PH\u0002J\b\u0010o\u001a\u00020PH\u0002J\b\u0010p\u001a\u00020PH\u0002J\u0006\u0010q\u001a\u00020*R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u00020!0 j\b\u0012\u0004\u0012\u00020!`\"X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010#\u001a\u0004\u0018\u00010$X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010)\u001a\u00020*X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u000e\u0010/\u001a\u000200X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u000200X\u000e¢\u0006\u0002\n\u0000R,\u00102\u001a\n 4*\u0004\u0018\u000103038\u0006@\u0006X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b5\u00106\u001a\u0004\b7\u00108\"\u0004\b9\u0010:R$\u0010;\u001a\n 4*\u0004\u0018\u000103038\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b<\u00106\u001a\u0004\b=\u00108R\u000e\u0010>\u001a\u00020?X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010@\u001a\u0004\u0018\u00010AX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020CX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010D\u001a\u0004\u0018\u00010EX\u000e¢\u0006\u0002\n\u0000R&\u0010F\u001a\u0004\u0018\u00010G8\u0006@\u0006X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\bH\u00106\u001a\u0004\bI\u0010J\"\u0004\bK\u0010LR\u000e\u0010M\u001a\u00020*X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020*X\u000e¢\u0006\u0002\n\u0000¨\u0006t"}, mo65043d2 = {"Lcom/android/systemui/keyguard/KeyguardUnlockAnimationController;", "Lcom/android/systemui/statusbar/policy/KeyguardStateController$Callback;", "Lcom/android/systemui/shared/system/smartspace/ISysuiUnlockAnimationController$Stub;", "context", "Landroid/content/Context;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "keyguardViewMediator", "Ldagger/Lazy;", "Lcom/android/systemui/keyguard/KeyguardViewMediator;", "keyguardViewController", "Lcom/android/keyguard/KeyguardViewController;", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "biometricUnlockControllerLazy", "Lcom/android/systemui/statusbar/phone/BiometricUnlockController;", "statusBarStateController", "Lcom/android/systemui/statusbar/SysuiStatusBarStateController;", "notificationShadeWindowController", "Lcom/android/systemui/statusbar/NotificationShadeWindowController;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Ldagger/Lazy;Lcom/android/keyguard/KeyguardViewController;Lcom/android/systemui/flags/FeatureFlags;Ldagger/Lazy;Lcom/android/systemui/statusbar/SysuiStatusBarStateController;Lcom/android/systemui/statusbar/NotificationShadeWindowController;)V", "handler", "Landroid/os/Handler;", "launcherSmartspaceState", "Lcom/android/systemui/shared/system/smartspace/SmartspaceState;", "getLauncherSmartspaceState", "()Lcom/android/systemui/shared/system/smartspace/SmartspaceState;", "setLauncherSmartspaceState", "(Lcom/android/systemui/shared/system/smartspace/SmartspaceState;)V", "launcherUnlockController", "Lcom/android/systemui/shared/system/smartspace/ILauncherUnlockAnimationController;", "listeners", "Ljava/util/ArrayList;", "Lcom/android/systemui/keyguard/KeyguardUnlockAnimationController$KeyguardUnlockAnimationListener;", "Lkotlin/collections/ArrayList;", "lockscreenSmartspace", "Landroid/view/View;", "getLockscreenSmartspace", "()Landroid/view/View;", "setLockscreenSmartspace", "(Landroid/view/View;)V", "playingCannedUnlockAnimation", "", "getPlayingCannedUnlockAnimation", "()Z", "setPlayingCannedUnlockAnimation", "(Z)V", "roundedCornerRadius", "", "surfaceBehindAlpha", "surfaceBehindAlphaAnimator", "Landroid/animation/ValueAnimator;", "kotlin.jvm.PlatformType", "getSurfaceBehindAlphaAnimator$annotations", "()V", "getSurfaceBehindAlphaAnimator", "()Landroid/animation/ValueAnimator;", "setSurfaceBehindAlphaAnimator", "(Landroid/animation/ValueAnimator;)V", "surfaceBehindEntryAnimator", "getSurfaceBehindEntryAnimator$annotations", "getSurfaceBehindEntryAnimator", "surfaceBehindMatrix", "Landroid/graphics/Matrix;", "surfaceBehindParams", "Landroid/view/SyncRtSurfaceTransactionApplier$SurfaceParams;", "surfaceBehindRemoteAnimationStartTime", "", "surfaceBehindRemoteAnimationTarget", "Landroid/view/RemoteAnimationTarget;", "surfaceTransactionApplier", "Landroid/view/SyncRtSurfaceTransactionApplier;", "getSurfaceTransactionApplier$annotations", "getSurfaceTransactionApplier", "()Landroid/view/SyncRtSurfaceTransactionApplier;", "setSurfaceTransactionApplier", "(Landroid/view/SyncRtSurfaceTransactionApplier;)V", "willUnlockWithInWindowLauncherAnimations", "willUnlockWithSmartspaceTransition", "addKeyguardUnlockAnimationListener", "", "listener", "applyParamsToSurface", "params", "canPerformInWindowLauncherAnimations", "fadeInSurfaceBehind", "fadeOutSurfaceBehind", "finishKeyguardExitRemoteAnimationIfReachThreshold", "hideKeyguardViewAfterRemoteAnimation", "isAnimatingBetweenKeyguardAndSurfaceBehind", "isPlayingCannedUnlockAnimation", "isUnlockingWithSmartSpaceTransition", "notifyFinishedKeyguardExitAnimation", "cancelled", "notifyStartSurfaceBehindRemoteAnimation", "target", "startTime", "requestedShowSurfaceBehindKeyguard", "onKeyguardDismissAmountChanged", "onKeyguardGoingAwayChanged", "onLauncherSmartspaceStateUpdated", "state", "playCannedUnlockAnimation", "prepareForInWindowLauncherAnimations", "removeKeyguardUnlockAnimationListener", "setLauncherUnlockController", "callback", "setSurfaceBehindAppearAmount", "amount", "shouldPerformSmartspaceTransition", "showOrHideSurfaceIfDismissAmountThresholdsReached", "unlockToLauncherWithInWindowAnimations", "updateSurfaceBehindAppearAmount", "willHandleUnlockAnimation", "Companion", "KeyguardUnlockAnimationListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardUnlockAnimationController.kt */
public final class KeyguardUnlockAnimationController extends ISysuiUnlockAnimationController.Stub implements KeyguardStateController.Callback {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final Lazy<BiometricUnlockController> biometricUnlockControllerLazy;
    private final Context context;
    private final FeatureFlags featureFlags;
    private final Handler handler;
    private final KeyguardStateController keyguardStateController;
    private final KeyguardViewController keyguardViewController;
    /* access modifiers changed from: private */
    public final Lazy<KeyguardViewMediator> keyguardViewMediator;
    private SmartspaceState launcherSmartspaceState;
    private ILauncherUnlockAnimationController launcherUnlockController;
    private final ArrayList<KeyguardUnlockAnimationListener> listeners = new ArrayList<>();
    private View lockscreenSmartspace;
    private final NotificationShadeWindowController notificationShadeWindowController;
    private boolean playingCannedUnlockAnimation;
    private float roundedCornerRadius;
    private final SysuiStatusBarStateController statusBarStateController;
    /* access modifiers changed from: private */
    public float surfaceBehindAlpha = 1.0f;
    private ValueAnimator surfaceBehindAlphaAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
    private final ValueAnimator surfaceBehindEntryAnimator;
    private final Matrix surfaceBehindMatrix = new Matrix();
    private SyncRtSurfaceTransactionApplier.SurfaceParams surfaceBehindParams;
    private long surfaceBehindRemoteAnimationStartTime;
    private RemoteAnimationTarget surfaceBehindRemoteAnimationTarget;
    private SyncRtSurfaceTransactionApplier surfaceTransactionApplier;
    private boolean willUnlockWithInWindowLauncherAnimations;
    private boolean willUnlockWithSmartspaceTransition;

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0017J(\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0017ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000bÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/keyguard/KeyguardUnlockAnimationController$KeyguardUnlockAnimationListener;", "", "onUnlockAnimationFinished", "", "onUnlockAnimationStarted", "playingCannedAnimation", "", "fromWakeAndUnlock", "unlockAnimationStartDelay", "", "unlockAnimationDuration", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: KeyguardUnlockAnimationController.kt */
    public interface KeyguardUnlockAnimationListener {
        @JvmDefault
        void onUnlockAnimationFinished() {
        }

        @JvmDefault
        void onUnlockAnimationStarted(boolean z, boolean z2, long j, long j2) {
        }
    }

    public static /* synthetic */ void getSurfaceBehindAlphaAnimator$annotations() {
    }

    public static /* synthetic */ void getSurfaceBehindEntryAnimator$annotations() {
    }

    public static /* synthetic */ void getSurfaceTransactionApplier$annotations() {
    }

    @Inject
    public KeyguardUnlockAnimationController(Context context2, KeyguardStateController keyguardStateController2, Lazy<KeyguardViewMediator> lazy, KeyguardViewController keyguardViewController2, FeatureFlags featureFlags2, Lazy<BiometricUnlockController> lazy2, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationShadeWindowController notificationShadeWindowController2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(lazy, "keyguardViewMediator");
        Intrinsics.checkNotNullParameter(keyguardViewController2, "keyguardViewController");
        Intrinsics.checkNotNullParameter(featureFlags2, "featureFlags");
        Intrinsics.checkNotNullParameter(lazy2, "biometricUnlockControllerLazy");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(notificationShadeWindowController2, "notificationShadeWindowController");
        this.context = context2;
        this.keyguardStateController = keyguardStateController2;
        this.keyguardViewMediator = lazy;
        this.keyguardViewController = keyguardViewController2;
        this.featureFlags = featureFlags2;
        this.biometricUnlockControllerLazy = lazy2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.notificationShadeWindowController = notificationShadeWindowController2;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.surfaceBehindEntryAnimator = ofFloat;
        this.handler = new Handler();
        ValueAnimator valueAnimator = this.surfaceBehindAlphaAnimator;
        valueAnimator.setDuration(175);
        valueAnimator.setInterpolator(Interpolators.LINEAR);
        valueAnimator.addUpdateListener(new KeyguardUnlockAnimationController$$ExternalSyntheticLambda0(this));
        valueAnimator.addListener(new KeyguardUnlockAnimationController$1$2(this));
        ofFloat.setDuration(200);
        ofFloat.setStartDelay(75);
        ofFloat.setInterpolator(Interpolators.STANDARD_DECELERATE);
        ofFloat.addUpdateListener(new KeyguardUnlockAnimationController$$ExternalSyntheticLambda1(this));
        ofFloat.addListener(new KeyguardUnlockAnimationController$2$2(this));
        keyguardStateController2.addCallback(this);
        this.roundedCornerRadius = (float) context2.getResources().getDimensionPixelSize(17105509);
    }

    public final View getLockscreenSmartspace() {
        return this.lockscreenSmartspace;
    }

    public final void setLockscreenSmartspace(View view) {
        this.lockscreenSmartspace = view;
    }

    public final SmartspaceState getLauncherSmartspaceState() {
        return this.launcherSmartspaceState;
    }

    public final void setLauncherSmartspaceState(SmartspaceState smartspaceState) {
        this.launcherSmartspaceState = smartspaceState;
    }

    public final boolean getPlayingCannedUnlockAnimation() {
        return this.playingCannedUnlockAnimation;
    }

    public final void setPlayingCannedUnlockAnimation(boolean z) {
        this.playingCannedUnlockAnimation = z;
    }

    public void setLauncherUnlockController(ILauncherUnlockAnimationController iLauncherUnlockAnimationController) {
        this.launcherUnlockController = iLauncherUnlockAnimationController;
    }

    public void onLauncherSmartspaceStateUpdated(SmartspaceState smartspaceState) {
        this.launcherSmartspaceState = smartspaceState;
    }

    public final SyncRtSurfaceTransactionApplier getSurfaceTransactionApplier() {
        return this.surfaceTransactionApplier;
    }

    public final void setSurfaceTransactionApplier(SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier) {
        this.surfaceTransactionApplier = syncRtSurfaceTransactionApplier;
    }

    public final ValueAnimator getSurfaceBehindAlphaAnimator() {
        return this.surfaceBehindAlphaAnimator;
    }

    public final void setSurfaceBehindAlphaAnimator(ValueAnimator valueAnimator) {
        this.surfaceBehindAlphaAnimator = valueAnimator;
    }

    public final ValueAnimator getSurfaceBehindEntryAnimator() {
        return this.surfaceBehindEntryAnimator;
    }

    /* access modifiers changed from: private */
    /* renamed from: lambda-1$lambda-0  reason: not valid java name */
    public static final void m2761lambda1$lambda0(KeyguardUnlockAnimationController keyguardUnlockAnimationController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(keyguardUnlockAnimationController, "this$0");
        Intrinsics.checkNotNullParameter(valueAnimator, "valueAnimator");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            keyguardUnlockAnimationController.surfaceBehindAlpha = ((Float) animatedValue).floatValue();
            keyguardUnlockAnimationController.updateSurfaceBehindAppearAmount();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    /* access modifiers changed from: private */
    /* renamed from: lambda-3$lambda-2  reason: not valid java name */
    public static final void m2762lambda3$lambda2(KeyguardUnlockAnimationController keyguardUnlockAnimationController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(keyguardUnlockAnimationController, "this$0");
        Intrinsics.checkNotNullParameter(valueAnimator, "valueAnimator");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            keyguardUnlockAnimationController.surfaceBehindAlpha = ((Float) animatedValue).floatValue();
            Object animatedValue2 = valueAnimator.getAnimatedValue();
            if (animatedValue2 != null) {
                keyguardUnlockAnimationController.setSurfaceBehindAppearAmount(((Float) animatedValue2).floatValue());
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public final void addKeyguardUnlockAnimationListener(KeyguardUnlockAnimationListener keyguardUnlockAnimationListener) {
        Intrinsics.checkNotNullParameter(keyguardUnlockAnimationListener, "listener");
        this.listeners.add(keyguardUnlockAnimationListener);
    }

    public final void removeKeyguardUnlockAnimationListener(KeyguardUnlockAnimationListener keyguardUnlockAnimationListener) {
        Intrinsics.checkNotNullParameter(keyguardUnlockAnimationListener, "listener");
        this.listeners.remove((Object) keyguardUnlockAnimationListener);
    }

    public final boolean canPerformInWindowLauncherAnimations() {
        Companion companion = Companion;
        return companion.isNexusLauncherUnderneath() && !this.notificationShadeWindowController.isLaunchingActivity() && this.launcherUnlockController != null && !this.keyguardStateController.isDismissingFromSwipe() && !companion.isFoldable(this.context);
    }

    public void onKeyguardGoingAwayChanged() {
        if (this.keyguardStateController.isKeyguardGoingAway() && !this.statusBarStateController.leaveOpenOnKeyguardHide()) {
            prepareForInWindowLauncherAnimations();
        }
    }

    public final void prepareForInWindowLauncherAnimations() {
        boolean canPerformInWindowLauncherAnimations = canPerformInWindowLauncherAnimations();
        this.willUnlockWithInWindowLauncherAnimations = canPerformInWindowLauncherAnimations;
        if (canPerformInWindowLauncherAnimations) {
            this.willUnlockWithSmartspaceTransition = shouldPerformSmartspaceTransition();
            Rect rect = new Rect();
            if (this.willUnlockWithSmartspaceTransition) {
                rect = new Rect();
                View view = this.lockscreenSmartspace;
                Intrinsics.checkNotNull(view);
                view.getBoundsOnScreen(rect);
                View view2 = this.lockscreenSmartspace;
                Intrinsics.checkNotNull(view2);
                int paddingLeft = view2.getPaddingLeft();
                View view3 = this.lockscreenSmartspace;
                Intrinsics.checkNotNull(view3);
                rect.offset(paddingLeft, view3.getPaddingTop());
                View view4 = this.lockscreenSmartspace;
                BcSmartspaceDataPlugin.SmartspaceView smartspaceView = view4 instanceof BcSmartspaceDataPlugin.SmartspaceView ? (BcSmartspaceDataPlugin.SmartspaceView) view4 : null;
                rect.offset(0, smartspaceView != null ? smartspaceView.getCurrentCardTopPadding() : 0);
            }
            BcSmartspaceDataPlugin.SmartspaceView smartspaceView2 = (BcSmartspaceDataPlugin.SmartspaceView) this.lockscreenSmartspace;
            int selectedPage = smartspaceView2 != null ? smartspaceView2.getSelectedPage() : -1;
            try {
                ILauncherUnlockAnimationController iLauncherUnlockAnimationController = this.launcherUnlockController;
                if (iLauncherUnlockAnimationController != null) {
                    iLauncherUnlockAnimationController.prepareForUnlock(this.willUnlockWithSmartspaceTransition, rect, selectedPage);
                }
            } catch (RemoteException e) {
                Log.e(KeyguardUnlockAnimationControllerKt.TAG, "Remote exception in prepareForInWindowUnlockAnimations.", e);
            }
        }
    }

    public final void notifyStartSurfaceBehindRemoteAnimation(RemoteAnimationTarget remoteAnimationTarget, long j, boolean z) {
        Intrinsics.checkNotNullParameter(remoteAnimationTarget, "target");
        if (this.surfaceTransactionApplier == null) {
            this.surfaceTransactionApplier = new SyncRtSurfaceTransactionApplier(this.keyguardViewController.getViewRootImpl().getView());
        }
        this.surfaceBehindParams = null;
        this.surfaceBehindRemoteAnimationTarget = remoteAnimationTarget;
        this.surfaceBehindRemoteAnimationStartTime = j;
        if (!z) {
            playCannedUnlockAnimation();
        } else if (!this.keyguardStateController.isFlingingToDismissKeyguard()) {
            fadeInSurfaceBehind();
        } else {
            playCannedUnlockAnimation();
        }
        for (KeyguardUnlockAnimationListener onUnlockAnimationStarted : this.listeners) {
            onUnlockAnimationStarted.onUnlockAnimationStarted(this.playingCannedUnlockAnimation, this.biometricUnlockControllerLazy.get().isWakeAndUnlock(), 100, 633);
        }
        finishKeyguardExitRemoteAnimationIfReachThreshold();
    }

    private final void playCannedUnlockAnimation() {
        this.playingCannedUnlockAnimation = true;
        if (this.willUnlockWithInWindowLauncherAnimations) {
            unlockToLauncherWithInWindowAnimations();
        } else if (this.biometricUnlockControllerLazy.get().isWakeAndUnlock()) {
            setSurfaceBehindAppearAmount(1.0f);
            this.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
        } else {
            this.surfaceBehindEntryAnimator.start();
        }
    }

    private final void unlockToLauncherWithInWindowAnimations() {
        setSurfaceBehindAppearAmount(1.0f);
        ILauncherUnlockAnimationController iLauncherUnlockAnimationController = this.launcherUnlockController;
        if (iLauncherUnlockAnimationController != null) {
            iLauncherUnlockAnimationController.playUnlockAnimation(true, 633, 100);
        }
        View view = this.lockscreenSmartspace;
        Intrinsics.checkNotNull(view);
        view.setVisibility(4);
        this.handler.postDelayed(new KeyguardUnlockAnimationController$$ExternalSyntheticLambda2(this), 100);
    }

    /* access modifiers changed from: private */
    /* renamed from: unlockToLauncherWithInWindowAnimations$lambda-6  reason: not valid java name */
    public static final void m2763unlockToLauncherWithInWindowAnimations$lambda6(KeyguardUnlockAnimationController keyguardUnlockAnimationController) {
        Intrinsics.checkNotNullParameter(keyguardUnlockAnimationController, "this$0");
        if (!keyguardUnlockAnimationController.keyguardViewMediator.get().isShowingAndNotOccluded() || keyguardUnlockAnimationController.keyguardStateController.isKeyguardGoingAway()) {
            keyguardUnlockAnimationController.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
        } else {
            Log.e(KeyguardUnlockAnimationControllerKt.TAG, "Finish keyguard exit animation delayed Runnable ran, but we are showing and not going away.");
        }
    }

    private final void updateSurfaceBehindAppearAmount() {
        if (this.surfaceBehindRemoteAnimationTarget == null || this.playingCannedUnlockAnimation) {
            return;
        }
        if (this.keyguardStateController.isFlingingToDismissKeyguard()) {
            setSurfaceBehindAppearAmount(this.keyguardStateController.getDismissAmount());
        } else if (this.keyguardStateController.isDismissingFromSwipe() || this.keyguardStateController.isSnappingKeyguardBackAfterSwipe()) {
            setSurfaceBehindAppearAmount((this.keyguardStateController.getDismissAmount() - 0.15f) / 0.15f);
        }
    }

    public void onKeyguardDismissAmountChanged() {
        if (willHandleUnlockAnimation() && this.keyguardViewController.isShowing() && !this.playingCannedUnlockAnimation) {
            showOrHideSurfaceIfDismissAmountThresholdsReached();
            if ((this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard() || this.keyguardViewMediator.get().isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe()) && !this.playingCannedUnlockAnimation) {
                updateSurfaceBehindAppearAmount();
            }
        }
    }

    private final void showOrHideSurfaceIfDismissAmountThresholdsReached() {
        FeatureFlags featureFlags2 = this.featureFlags;
        BooleanFlag booleanFlag = Flags.NEW_UNLOCK_SWIPE_ANIMATION;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "NEW_UNLOCK_SWIPE_ANIMATION");
        if (featureFlags2.isEnabled(booleanFlag) && !this.playingCannedUnlockAnimation && this.keyguardStateController.isShowing()) {
            float dismissAmount = this.keyguardStateController.getDismissAmount();
            if (dismissAmount >= 0.15f && !this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard()) {
                this.keyguardViewMediator.get().showSurfaceBehindKeyguard();
            } else if (dismissAmount < 0.15f && this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard()) {
                this.keyguardViewMediator.get().hideSurfaceBehindKeyguard();
                fadeOutSurfaceBehind();
            }
            finishKeyguardExitRemoteAnimationIfReachThreshold();
        }
    }

    private final void finishKeyguardExitRemoteAnimationIfReachThreshold() {
        if (KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation && this.keyguardViewController.isShowing() && this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard() && this.keyguardViewMediator.get().isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe()) {
            float dismissAmount = this.keyguardStateController.getDismissAmount();
            if (dismissAmount >= 1.0f || (this.keyguardStateController.isDismissingFromSwipe() && !this.keyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture() && dismissAmount >= 0.3f)) {
                setSurfaceBehindAppearAmount(1.0f);
                this.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
            }
        }
    }

    public final void setSurfaceBehindAppearAmount(float f) {
        RemoteAnimationTarget remoteAnimationTarget = this.surfaceBehindRemoteAnimationTarget;
        if (remoteAnimationTarget != null) {
            Intrinsics.checkNotNull(remoteAnimationTarget);
            int height = remoteAnimationTarget.screenSpaceBounds.height();
            float clamp = (MathUtils.clamp(f, 0.0f, 1.0f) * 0.050000012f) + 0.95f;
            Matrix matrix = this.surfaceBehindMatrix;
            RemoteAnimationTarget remoteAnimationTarget2 = this.surfaceBehindRemoteAnimationTarget;
            Intrinsics.checkNotNull(remoteAnimationTarget2);
            float f2 = (float) height;
            matrix.setScale(clamp, clamp, ((float) remoteAnimationTarget2.screenSpaceBounds.width()) / 2.0f, 0.66f * f2);
            this.surfaceBehindMatrix.postTranslate(0.0f, f2 * 0.05f * (1.0f - f));
            if (!this.keyguardStateController.isSnappingKeyguardBackAfterSwipe()) {
                f = this.surfaceBehindAlpha;
            }
            RemoteAnimationTarget remoteAnimationTarget3 = this.surfaceBehindRemoteAnimationTarget;
            Intrinsics.checkNotNull(remoteAnimationTarget3);
            SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget3.leash).withMatrix(this.surfaceBehindMatrix).withCornerRadius(this.roundedCornerRadius).withAlpha(f).build();
            Intrinsics.checkNotNullExpressionValue(build, "Builder(\n               …\n                .build()");
            applyParamsToSurface(build);
        }
    }

    public final void notifyFinishedKeyguardExitAnimation(boolean z) {
        this.handler.removeCallbacksAndMessages((Object) null);
        setSurfaceBehindAppearAmount(1.0f);
        ILauncherUnlockAnimationController iLauncherUnlockAnimationController = this.launcherUnlockController;
        if (iLauncherUnlockAnimationController != null) {
            iLauncherUnlockAnimationController.setUnlockAmount(1.0f, false);
        }
        this.surfaceBehindRemoteAnimationTarget = null;
        this.surfaceBehindParams = null;
        this.playingCannedUnlockAnimation = false;
        this.willUnlockWithInWindowLauncherAnimations = false;
        this.willUnlockWithSmartspaceTransition = false;
        View view = this.lockscreenSmartspace;
        if (view != null) {
            view.setVisibility(0);
        }
        for (KeyguardUnlockAnimationListener onUnlockAnimationFinished : this.listeners) {
            onUnlockAnimationFinished.onUnlockAnimationFinished();
        }
    }

    public final void hideKeyguardViewAfterRemoteAnimation() {
        if (this.keyguardViewController.isShowing()) {
            this.keyguardViewController.hide(this.surfaceBehindRemoteAnimationStartTime, 0);
        } else {
            Log.e(KeyguardUnlockAnimationControllerKt.TAG, "#hideKeyguardViewAfterRemoteAnimation called when keyguard view is not showing. Ignoring...");
        }
    }

    private final void applyParamsToSurface(SyncRtSurfaceTransactionApplier.SurfaceParams surfaceParams) {
        SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier = this.surfaceTransactionApplier;
        Intrinsics.checkNotNull(syncRtSurfaceTransactionApplier);
        syncRtSurfaceTransactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{surfaceParams});
        this.surfaceBehindParams = surfaceParams;
    }

    private final void fadeInSurfaceBehind() {
        this.surfaceBehindAlphaAnimator.cancel();
        this.surfaceBehindAlphaAnimator.start();
    }

    private final void fadeOutSurfaceBehind() {
        this.surfaceBehindAlphaAnimator.cancel();
        this.surfaceBehindAlphaAnimator.reverse();
    }

    private final boolean shouldPerformSmartspaceTransition() {
        SmartspaceState smartspaceState;
        FeatureFlags featureFlags2 = this.featureFlags;
        BooleanFlag booleanFlag = Flags.SMARTSPACE_SHARED_ELEMENT_TRANSITION_ENABLED;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "SMARTSPACE_SHARED_ELEMENT_TRANSITION_ENABLED");
        if (!featureFlags2.isEnabled(booleanFlag) || this.launcherUnlockController == null || this.lockscreenSmartspace == null || (smartspaceState = this.launcherSmartspaceState) == null) {
            return false;
        }
        if (!(smartspaceState != null && smartspaceState.getVisibleOnScreen()) || !Companion.isNexusLauncherUnderneath() || this.biometricUnlockControllerLazy.get().isWakeAndUnlock()) {
            return false;
        }
        if ((this.keyguardStateController.canDismissLockScreen() || this.biometricUnlockControllerLazy.get().isBiometricUnlock()) && !this.keyguardStateController.isBouncerShowing() && !this.keyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture() && !Utilities.isTablet(this.context)) {
            return true;
        }
        return false;
    }

    public final boolean isUnlockingWithSmartSpaceTransition() {
        return this.willUnlockWithSmartspaceTransition;
    }

    public final boolean willHandleUnlockAnimation() {
        return KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation;
    }

    public final boolean isAnimatingBetweenKeyguardAndSurfaceBehind() {
        return this.keyguardViewMediator.get().isAnimatingBetweenKeyguardAndSurfaceBehind();
    }

    public final boolean isPlayingCannedUnlockAnimation() {
        return this.playingCannedUnlockAnimation;
    }

    @Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\u0004¨\u0006\b"}, mo65043d2 = {"Lcom/android/systemui/keyguard/KeyguardUnlockAnimationController$Companion;", "", "()V", "isFoldable", "", "context", "Landroid/content/Context;", "isNexusLauncherUnderneath", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: KeyguardUnlockAnimationController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean isNexusLauncherUnderneath() {
            ComponentName componentName;
            String className;
            ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.getInstance().getRunningTask();
            if (runningTask == null || (componentName = runningTask.topActivity) == null || (className = componentName.getClassName()) == null) {
                return false;
            }
            return className.equals(QuickStepContract.LAUNCHER_ACTIVITY_CLASS_NAME);
        }

        public final boolean isFoldable(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            int[] intArray = context.getResources().getIntArray(17236068);
            Intrinsics.checkNotNullExpressionValue(intArray, "context.resources.getInt…onfig_foldedDeviceStates)");
            return !(intArray.length == 0);
        }
    }
}
