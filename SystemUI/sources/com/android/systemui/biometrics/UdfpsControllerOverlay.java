package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import android.icu.lang.UCharacter;
import android.icu.text.DateFormat;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.RemoteException;
import android.util.Log;
import android.util.RotationUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.C1894R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.time.SystemClock;
import com.nothing.systemui.util.NTLogUtil;
import java.sql.Types;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000ô\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001BÕ\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017\u0012\u0006\u0010\u0018\u001a\u00020\u0019\u0012\u0006\u0010\u001a\u001a\u00020\u001b\u0012\u0006\u0010\u001c\u001a\u00020\u001d\u0012\u0006\u0010\u001e\u001a\u00020\u001f\u0012\u0006\u0010 \u001a\u00020!\u0012\u0006\u0010\"\u001a\u00020#\u0012\u0006\u0010$\u001a\u00020%\u0012\u0006\u0010&\u001a\u00020'\u0012\u0006\u0010(\u001a\u00020)\u0012\u0006\u0010*\u001a\u00020+\u0012\u001e\u0010,\u001a\u001a\u0012\u0004\u0012\u00020.\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020#0-\u0012\u0006\u00100\u001a\u000201¢\u0006\u0002\u00102J\u0006\u0010O\u001a\u00020PJ\u0006\u0010Q\u001a\u00020#J\u001c\u0010R\u001a\b\u0012\u0002\b\u0003\u0018\u0001052\u0006\u0010S\u001a\u00020F2\u0006\u0010T\u001a\u00020UJ\u000e\u0010V\u001a\u00020#2\u0006\u0010W\u001a\u00020'J\u0006\u0010X\u001a\u00020PJ\u000e\u0010Y\u001a\u00020P2\u0006\u0010Z\u001a\u00020#J\u0006\u0010[\u001a\u00020PJ\u000e\u0010\\\u001a\u00020P2\u0006\u0010]\u001a\u00020)J.\u0010^\u001a\u00020P2\u0006\u0010_\u001a\u00020`2\u0006\u0010a\u001a\u00020`2\u0006\u0010b\u001a\u00020`2\u0006\u0010c\u001a\u00020`2\u0006\u0010d\u001a\u00020)J.\u0010e\u001a\u00020f2\u0006\u0010_\u001a\u00020`2\u0006\u0010a\u001a\u00020`2\u0006\u0010b\u001a\u00020`2\u0006\u0010c\u001a\u00020`2\u0006\u0010d\u001a\u00020)J\u000e\u0010g\u001a\u00020P2\u0006\u0010h\u001a\u00020fJ\u0016\u0010i\u001a\u00020#2\f\u0010j\u001a\b\u0012\u0002\b\u0003\u0018\u000105H\u0002J\u0018\u0010k\u001a\u00020#2\u0006\u0010T\u001a\u00020U2\u0006\u0010l\u001a\u00020BH\u0007J\u0010\u0010m\u001a\u00020P2\u0006\u0010S\u001a\u00020.H\u0002JC\u0010n\u001a\u0002Ho\"\n\b\u0000\u0010o\u0018\u0001*\u00020.*\u00020F2\b\b\u0001\u0010W\u001a\u00020)2\u0019\b\u0002\u0010p\u001a\u0013\u0012\u0004\u0012\u0002Ho\u0012\u0004\u0012\u00020P0q¢\u0006\u0002\brH\b¢\u0006\u0002\u0010sJ\u001a\u0010t\u001a\u000209*\u0002092\f\u0010j\u001a\b\u0012\u0002\b\u0003\u0018\u000105H\u0002R\u000e\u00103\u001a\u00020)XD¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u000201X\u0004¢\u0006\u0002\n\u0000R\u0017\u00104\u001a\b\u0012\u0002\b\u0003\u0018\u0001058F¢\u0006\u0006\u001a\u0004\b6\u00107R\u000e\u0010\u001a\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0004¢\u0006\u0002\n\u0000R\u000e\u00108\u001a\u000209X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u0013\u0010:\u001a\u0004\u0018\u00010;¢\u0006\b\n\u0000\u001a\u0004\b<\u0010=R\u000e\u0010\"\u001a\u00020#X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010>\u001a\u00020#8F¢\u0006\u0006\u001a\u0004\b>\u0010?R\u0011\u0010@\u001a\u00020#8F¢\u0006\u0006\u001a\u0004\b@\u0010?R\u000e\u0010\u001e\u001a\u00020\u001fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R&\u0010,\u001a\u001a\u0012\u0004\u0012\u00020.\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020#0-X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020BX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010C\u001a\u0004\u0018\u00010DX\u000e¢\u0006\u0002\n\u0000R\"\u0010G\u001a\u0004\u0018\u00010F2\b\u0010E\u001a\u0004\u0018\u00010F@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\bH\u0010IR\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010&\u001a\u00020'¢\u0006\b\n\u0000\u001a\u0004\bJ\u0010KR\u0011\u0010(\u001a\u00020)¢\u0006\b\n\u0000\u001a\u0004\bL\u0010MR\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020#X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006u"}, mo65043d2 = {"Lcom/android/systemui/biometrics/UdfpsControllerOverlay;", "", "context", "Landroid/content/Context;", "fingerprintManager", "Landroid/hardware/fingerprint/FingerprintManager;", "inflater", "Landroid/view/LayoutInflater;", "windowManager", "Landroid/view/WindowManager;", "accessibilityManager", "Landroid/view/accessibility/AccessibilityManager;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "panelExpansionStateManager", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionStateManager;", "statusBarKeyguardViewManager", "Lcom/android/systemui/statusbar/phone/StatusBarKeyguardViewManager;", "keyguardUpdateMonitor", "Lcom/android/keyguard/KeyguardUpdateMonitor;", "dialogManager", "Lcom/android/systemui/statusbar/phone/SystemUIDialogManager;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "transitionController", "Lcom/android/systemui/statusbar/LockscreenShadeTransitionController;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "unlockedScreenOffAnimationController", "Lcom/android/systemui/statusbar/phone/UnlockedScreenOffAnimationController;", "halControlsIllumination", "", "hbmProvider", "Lcom/android/systemui/biometrics/UdfpsHbmProvider;", "requestId", "", "requestReason", "", "controllerCallback", "Landroid/hardware/fingerprint/IUdfpsOverlayControllerCallback;", "onTouch", "Lkotlin/Function3;", "Landroid/view/View;", "Landroid/view/MotionEvent;", "activityLaunchAnimator", "Lcom/android/systemui/animation/ActivityLaunchAnimator;", "(Landroid/content/Context;Landroid/hardware/fingerprint/FingerprintManager;Landroid/view/LayoutInflater;Landroid/view/WindowManager;Landroid/view/accessibility/AccessibilityManager;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionStateManager;Lcom/android/systemui/statusbar/phone/StatusBarKeyguardViewManager;Lcom/android/keyguard/KeyguardUpdateMonitor;Lcom/android/systemui/statusbar/phone/SystemUIDialogManager;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/statusbar/LockscreenShadeTransitionController;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Lcom/android/systemui/statusbar/phone/UnlockedScreenOffAnimationController;ZLcom/android/systemui/biometrics/UdfpsHbmProvider;JILandroid/hardware/fingerprint/IUdfpsOverlayControllerCallback;Lkotlin/jvm/functions/Function3;Lcom/android/systemui/animation/ActivityLaunchAnimator;)V", "FPS_SCNNING_ANIM_SIZE", "animationViewController", "Lcom/android/systemui/biometrics/UdfpsAnimationViewController;", "getAnimationViewController", "()Lcom/android/systemui/biometrics/UdfpsAnimationViewController;", "coreLayoutParams", "Landroid/view/WindowManager$LayoutParams;", "enrollHelper", "Lcom/android/systemui/biometrics/UdfpsEnrollHelper;", "getEnrollHelper", "()Lcom/android/systemui/biometrics/UdfpsEnrollHelper;", "isHiding", "()Z", "isShowing", "overlayParams", "Lcom/android/systemui/biometrics/UdfpsOverlayParams;", "overlayTouchListener", "Landroid/view/accessibility/AccessibilityManager$TouchExplorationStateChangeListener;", "<set-?>", "Lcom/android/systemui/biometrics/UdfpsView;", "overlayView", "getOverlayView", "()Lcom/android/systemui/biometrics/UdfpsView;", "getRequestId", "()J", "getRequestReason", "()I", "touchExplorationEnabled", "cancel", "", "hide", "inflateUdfpsAnimation", "view", "controller", "Lcom/android/systemui/biometrics/UdfpsController;", "matchesRequestId", "id", "onAcquiredGood", "onDozingChanged", "dozing", "onEnrollmentHelp", "onEnrollmentProgress", "remaining", "onTouchOutsideOfSensorArea", "touchX", "", "touchY", "sensorX", "sensorY", "rotation", "onTouchOutsideOfSensorAreaImpl", "", "reattachToWM", "reason", "shouldRotate", "animation", "show", "params", "updateInnerViewLayoutParams", "addUdfpsView", "T", "init", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Lcom/android/systemui/biometrics/UdfpsView;ILkotlin/jvm/functions/Function1;)Landroid/view/View;", "updateDimensions", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UdfpsControllerOverlay.kt */
public final class UdfpsControllerOverlay {
    private final int FPS_SCNNING_ANIM_SIZE;
    private final AccessibilityManager accessibilityManager;
    private final ActivityLaunchAnimator activityLaunchAnimator;
    private final ConfigurationController configurationController;
    private final Context context;
    private final IUdfpsOverlayControllerCallback controllerCallback;
    private final WindowManager.LayoutParams coreLayoutParams;
    private final SystemUIDialogManager dialogManager;
    private final DumpManager dumpManager;
    private final UdfpsEnrollHelper enrollHelper;
    private final boolean halControlsIllumination;
    private UdfpsHbmProvider hbmProvider;
    /* access modifiers changed from: private */
    public final LayoutInflater inflater;
    private final KeyguardStateController keyguardStateController;
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final Function3<View, MotionEvent, Boolean, Boolean> onTouch;
    private UdfpsOverlayParams overlayParams = new UdfpsOverlayParams((Rect) null, 0, 0, 0.0f, 0, 31, (DefaultConstructorMarker) null);
    private AccessibilityManager.TouchExplorationStateChangeListener overlayTouchListener;
    private UdfpsView overlayView;
    private final PanelExpansionStateManager panelExpansionStateManager;
    private final long requestId;
    private final int requestReason;
    private final StatusBarKeyguardViewManager statusBarKeyguardViewManager;
    private final StatusBarStateController statusBarStateController;
    private final SystemClock systemClock;
    private boolean touchExplorationEnabled;
    private final LockscreenShadeTransitionController transitionController;
    private final UnlockedScreenOffAnimationController unlockedScreenOffAnimationController;
    private final WindowManager windowManager;

    public UdfpsControllerOverlay(Context context2, FingerprintManager fingerprintManager, LayoutInflater layoutInflater, WindowManager windowManager2, AccessibilityManager accessibilityManager2, StatusBarStateController statusBarStateController2, PanelExpansionStateManager panelExpansionStateManager2, StatusBarKeyguardViewManager statusBarKeyguardViewManager2, KeyguardUpdateMonitor keyguardUpdateMonitor2, SystemUIDialogManager systemUIDialogManager, DumpManager dumpManager2, LockscreenShadeTransitionController lockscreenShadeTransitionController, ConfigurationController configurationController2, SystemClock systemClock2, KeyguardStateController keyguardStateController2, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController2, boolean z, UdfpsHbmProvider udfpsHbmProvider, long j, int i, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback, Function3<? super View, ? super MotionEvent, ? super Boolean, Boolean> function3, ActivityLaunchAnimator activityLaunchAnimator2) {
        UdfpsEnrollHelper udfpsEnrollHelper;
        Context context3 = context2;
        LayoutInflater layoutInflater2 = layoutInflater;
        WindowManager windowManager3 = windowManager2;
        AccessibilityManager accessibilityManager3 = accessibilityManager2;
        StatusBarStateController statusBarStateController3 = statusBarStateController2;
        PanelExpansionStateManager panelExpansionStateManager3 = panelExpansionStateManager2;
        StatusBarKeyguardViewManager statusBarKeyguardViewManager3 = statusBarKeyguardViewManager2;
        KeyguardUpdateMonitor keyguardUpdateMonitor3 = keyguardUpdateMonitor2;
        SystemUIDialogManager systemUIDialogManager2 = systemUIDialogManager;
        DumpManager dumpManager3 = dumpManager2;
        LockscreenShadeTransitionController lockscreenShadeTransitionController2 = lockscreenShadeTransitionController;
        ConfigurationController configurationController3 = configurationController2;
        SystemClock systemClock3 = systemClock2;
        KeyguardStateController keyguardStateController3 = keyguardStateController2;
        UnlockedScreenOffAnimationController unlockedScreenOffAnimationController3 = unlockedScreenOffAnimationController2;
        Intrinsics.checkNotNullParameter(context3, "context");
        Intrinsics.checkNotNullParameter(fingerprintManager, "fingerprintManager");
        Intrinsics.checkNotNullParameter(layoutInflater2, "inflater");
        Intrinsics.checkNotNullParameter(windowManager3, "windowManager");
        Intrinsics.checkNotNullParameter(accessibilityManager3, "accessibilityManager");
        Intrinsics.checkNotNullParameter(statusBarStateController3, "statusBarStateController");
        Intrinsics.checkNotNullParameter(panelExpansionStateManager3, "panelExpansionStateManager");
        Intrinsics.checkNotNullParameter(statusBarKeyguardViewManager3, "statusBarKeyguardViewManager");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor3, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(systemUIDialogManager2, "dialogManager");
        Intrinsics.checkNotNullParameter(dumpManager3, "dumpManager");
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController2, "transitionController");
        Intrinsics.checkNotNullParameter(configurationController3, "configurationController");
        Intrinsics.checkNotNullParameter(systemClock3, "systemClock");
        Intrinsics.checkNotNullParameter(keyguardStateController3, "keyguardStateController");
        Intrinsics.checkNotNullParameter(unlockedScreenOffAnimationController2, "unlockedScreenOffAnimationController");
        Intrinsics.checkNotNullParameter(udfpsHbmProvider, "hbmProvider");
        Intrinsics.checkNotNullParameter(iUdfpsOverlayControllerCallback, "controllerCallback");
        Intrinsics.checkNotNullParameter(function3, "onTouch");
        Intrinsics.checkNotNullParameter(activityLaunchAnimator2, "activityLaunchAnimator");
        this.context = context3;
        this.inflater = layoutInflater2;
        this.windowManager = windowManager3;
        this.accessibilityManager = accessibilityManager3;
        this.statusBarStateController = statusBarStateController3;
        this.panelExpansionStateManager = panelExpansionStateManager3;
        this.statusBarKeyguardViewManager = statusBarKeyguardViewManager3;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor3;
        this.dialogManager = systemUIDialogManager2;
        this.dumpManager = dumpManager3;
        this.transitionController = lockscreenShadeTransitionController2;
        this.configurationController = configurationController3;
        this.systemClock = systemClock3;
        this.keyguardStateController = keyguardStateController3;
        this.unlockedScreenOffAnimationController = unlockedScreenOffAnimationController2;
        this.halControlsIllumination = z;
        this.hbmProvider = udfpsHbmProvider;
        this.requestId = j;
        int i2 = i;
        this.requestReason = i2;
        this.controllerCallback = iUdfpsOverlayControllerCallback;
        this.onTouch = function3;
        this.activityLaunchAnimator = activityLaunchAnimator2;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(Types.SQLXML, 0, -3);
        layoutParams.setTitle("UdfpsControllerOverlay");
        layoutParams.setFitInsetsTypes(0);
        layoutParams.gravity = 51;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.flags = 25166120;
        layoutParams.privateFlags = NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE;
        layoutParams.accessibilityTitle = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        this.coreLayoutParams = layoutParams;
        if (UdfpsControllerOverlayKt.isEnrollmentReason(i)) {
            udfpsEnrollHelper = new UdfpsEnrollHelper(context3, fingerprintManager, i2);
        } else {
            udfpsEnrollHelper = null;
            UdfpsEnrollHelper udfpsEnrollHelper2 = null;
        }
        this.enrollHelper = udfpsEnrollHelper;
        this.FPS_SCNNING_ANIM_SIZE = UCharacter.UnicodeBlock.NYIAKENG_PUACHUE_HMONG_ID;
    }

    public final long getRequestId() {
        return this.requestId;
    }

    public final int getRequestReason() {
        return this.requestReason;
    }

    public final UdfpsView getOverlayView() {
        return this.overlayView;
    }

    public final UdfpsEnrollHelper getEnrollHelper() {
        return this.enrollHelper;
    }

    public final boolean isShowing() {
        return this.overlayView != null;
    }

    public final boolean isHiding() {
        return this.overlayView == null;
    }

    public final UdfpsAnimationViewController<?> getAnimationViewController() {
        UdfpsView udfpsView = this.overlayView;
        if (udfpsView != null) {
            return udfpsView.getAnimationViewController();
        }
        return null;
    }

    public final boolean show(UdfpsController udfpsController, UdfpsOverlayParams udfpsOverlayParams) {
        Intrinsics.checkNotNullParameter(udfpsController, "controller");
        Intrinsics.checkNotNullParameter(udfpsOverlayParams, "params");
        boolean z = false;
        if (this.overlayView == null) {
            this.overlayParams = udfpsOverlayParams;
            try {
                View inflate = this.inflater.inflate(C1894R.layout.udfps_view, (ViewGroup) null, false);
                if (inflate != null) {
                    UdfpsView udfpsView = (UdfpsView) inflate;
                    udfpsView.setOverlayParams(udfpsOverlayParams);
                    udfpsView.setHalControlsIllumination(this.halControlsIllumination);
                    udfpsView.setHbmProvider(this.hbmProvider);
                    UdfpsAnimationViewController<?> inflateUdfpsAnimation = inflateUdfpsAnimation(udfpsView, udfpsController);
                    if (this.requestReason == 4) {
                        z = true;
                    }
                    udfpsView.enableScanningAnim(z);
                    if (inflateUdfpsAnimation != null) {
                        inflateUdfpsAnimation.init();
                        udfpsView.setAnimationViewController(inflateUdfpsAnimation);
                    }
                    if (UdfpsControllerOverlayKt.isImportantForAccessibility(this.requestReason)) {
                        udfpsView.setImportantForAccessibility(2);
                    }
                    this.windowManager.addView(udfpsView, updateDimensions(this.coreLayoutParams, inflateUdfpsAnimation));
                    this.touchExplorationEnabled = this.accessibilityManager.isTouchExplorationEnabled();
                    UdfpsControllerOverlay$$ExternalSyntheticLambda2 udfpsControllerOverlay$$ExternalSyntheticLambda2 = new UdfpsControllerOverlay$$ExternalSyntheticLambda2(this, udfpsView);
                    this.overlayTouchListener = udfpsControllerOverlay$$ExternalSyntheticLambda2;
                    AccessibilityManager accessibilityManager2 = this.accessibilityManager;
                    Intrinsics.checkNotNull(udfpsControllerOverlay$$ExternalSyntheticLambda2);
                    accessibilityManager2.addTouchExplorationStateChangeListener(udfpsControllerOverlay$$ExternalSyntheticLambda2);
                    AccessibilityManager.TouchExplorationStateChangeListener touchExplorationStateChangeListener = this.overlayTouchListener;
                    if (touchExplorationStateChangeListener != null) {
                        touchExplorationStateChangeListener.onTouchExplorationStateChanged(true);
                    }
                    this.overlayView = udfpsView;
                    return true;
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsView");
            } catch (RuntimeException e) {
                Log.e("UdfpsControllerOverlay", "showUdfpsOverlay | failed to add window", e);
            }
        } else {
            Log.v("UdfpsControllerOverlay", "showUdfpsOverlay | the overlay is already showing");
            return false;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: show$lambda-4$lambda-3  reason: not valid java name */
    public static final void m2592show$lambda4$lambda3(UdfpsControllerOverlay udfpsControllerOverlay, UdfpsView udfpsView, boolean z) {
        Intrinsics.checkNotNullParameter(udfpsControllerOverlay, "this$0");
        Intrinsics.checkNotNullParameter(udfpsView, "$this_apply");
        if (udfpsControllerOverlay.accessibilityManager.isTouchExplorationEnabled()) {
            udfpsView.setOnHoverListener(new UdfpsControllerOverlay$$ExternalSyntheticLambda0(udfpsControllerOverlay));
            udfpsView.setOnTouchListener((View.OnTouchListener) null);
            udfpsControllerOverlay.touchExplorationEnabled = true;
            return;
        }
        udfpsView.setOnHoverListener((View.OnHoverListener) null);
        udfpsView.setOnTouchListener(new UdfpsControllerOverlay$$ExternalSyntheticLambda1(udfpsControllerOverlay));
        udfpsControllerOverlay.touchExplorationEnabled = false;
    }

    /* access modifiers changed from: private */
    /* renamed from: show$lambda-4$lambda-3$lambda-1  reason: not valid java name */
    public static final boolean m2593show$lambda4$lambda3$lambda1(UdfpsControllerOverlay udfpsControllerOverlay, View view, MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(udfpsControllerOverlay, "this$0");
        Function3<View, MotionEvent, Boolean, Boolean> function3 = udfpsControllerOverlay.onTouch;
        Intrinsics.checkNotNullExpressionValue(view, DateFormat.ABBR_GENERIC_TZ);
        Intrinsics.checkNotNullExpressionValue(motionEvent, NotificationCompat.CATEGORY_EVENT);
        return function3.invoke(view, motionEvent, true).booleanValue();
    }

    /* access modifiers changed from: private */
    /* renamed from: show$lambda-4$lambda-3$lambda-2  reason: not valid java name */
    public static final boolean m2594show$lambda4$lambda3$lambda2(UdfpsControllerOverlay udfpsControllerOverlay, View view, MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(udfpsControllerOverlay, "this$0");
        Function3<View, MotionEvent, Boolean, Boolean> function3 = udfpsControllerOverlay.onTouch;
        Intrinsics.checkNotNullExpressionValue(view, DateFormat.ABBR_GENERIC_TZ);
        Intrinsics.checkNotNullExpressionValue(motionEvent, NotificationCompat.CATEGORY_EVENT);
        return function3.invoke(view, motionEvent, true).booleanValue();
    }

    public final UdfpsAnimationViewController<?> inflateUdfpsAnimation(UdfpsView udfpsView, UdfpsController udfpsController) {
        UdfpsView udfpsView2 = udfpsView;
        Intrinsics.checkNotNullParameter(udfpsView2, "view");
        Intrinsics.checkNotNullParameter(udfpsController, "controller");
        switch (this.requestReason) {
            case 1:
            case 2:
                View inflate = this.inflater.inflate(C1894R.layout.udfps_enroll_view, (ViewGroup) null);
                if (inflate != null) {
                    View view = (UdfpsEnrollView) inflate;
                    udfpsView2.addView(view);
                    UdfpsEnrollView udfpsEnrollView = (UdfpsEnrollView) view;
                    udfpsEnrollView.updateSensorLocation(this.overlayParams.getSensorBounds());
                    updateInnerViewLayoutParams(view);
                    UdfpsEnrollHelper udfpsEnrollHelper = this.enrollHelper;
                    if (udfpsEnrollHelper != null) {
                        return new UdfpsEnrollViewController(udfpsEnrollView, udfpsEnrollHelper, this.statusBarStateController, this.panelExpansionStateManager, this.dialogManager, this.dumpManager, this.overlayParams.getScaleFactor());
                    }
                    throw new IllegalStateException("no enrollment helper");
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsEnrollView");
            case 3:
                View inflate2 = this.inflater.inflate(C1894R.layout.udfps_bp_view, (ViewGroup) null);
                if (inflate2 != null) {
                    View view2 = (UdfpsBpView) inflate2;
                    udfpsView2.addView(view2);
                    updateInnerViewLayoutParams(view2);
                    return new UdfpsBpViewController((UdfpsBpView) view2, this.statusBarStateController, this.panelExpansionStateManager, this.dialogManager, this.dumpManager);
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsBpView");
            case 4:
                View inflate3 = this.inflater.inflate(C1894R.layout.udfps_keyguard_view, (ViewGroup) null);
                if (inflate3 != null) {
                    View view3 = (UdfpsKeyguardView) inflate3;
                    udfpsView2.addView(view3);
                    updateInnerViewLayoutParams(view3);
                    return new UdfpsKeyguardViewController((UdfpsKeyguardView) view3, this.statusBarStateController, this.panelExpansionStateManager, this.statusBarKeyguardViewManager, this.keyguardUpdateMonitor, this.dumpManager, this.transitionController, this.configurationController, this.systemClock, this.keyguardStateController, this.unlockedScreenOffAnimationController, this.dialogManager, udfpsController, this.activityLaunchAnimator);
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsKeyguardView");
            case 5:
            case 6:
                View inflate4 = this.inflater.inflate(C1894R.layout.udfps_fpm_other_view, (ViewGroup) null);
                if (inflate4 != null) {
                    View view4 = (UdfpsFpmOtherView) inflate4;
                    udfpsView2.addView(view4);
                    updateInnerViewLayoutParams(view4);
                    return new UdfpsFpmOtherViewController((UdfpsFpmOtherView) view4, this.statusBarStateController, this.panelExpansionStateManager, this.dialogManager, this.dumpManager);
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.biometrics.UdfpsFpmOtherView");
            default:
                Log.e("UdfpsControllerOverlay", "Animation for reason " + this.requestReason + " not supported yet");
                UdfpsAnimationViewController udfpsAnimationViewController = null;
                return null;
        }
    }

    public final boolean hide() {
        boolean isShowing = isShowing();
        UdfpsView udfpsView = this.overlayView;
        if (udfpsView != null) {
            if (udfpsView.isIlluminationRequested()) {
                udfpsView.stopIllumination();
            }
            this.windowManager.removeView(udfpsView);
            udfpsView.setOnTouchListener((View.OnTouchListener) null);
            udfpsView.setOnHoverListener((View.OnHoverListener) null);
            udfpsView.setAnimationViewController((UdfpsAnimationViewController<?>) null);
            AccessibilityManager.TouchExplorationStateChangeListener touchExplorationStateChangeListener = this.overlayTouchListener;
            if (touchExplorationStateChangeListener != null) {
                this.accessibilityManager.removeTouchExplorationStateChangeListener(touchExplorationStateChangeListener);
            }
        }
        this.overlayView = null;
        this.overlayTouchListener = null;
        return isShowing;
    }

    public final void onEnrollmentProgress(int i) {
        UdfpsEnrollHelper udfpsEnrollHelper = this.enrollHelper;
        if (udfpsEnrollHelper != null) {
            udfpsEnrollHelper.onEnrollmentProgress(i);
        }
    }

    public final void onAcquiredGood() {
        UdfpsEnrollHelper udfpsEnrollHelper = this.enrollHelper;
        if (udfpsEnrollHelper != null) {
            udfpsEnrollHelper.animateIfLastStep();
        }
    }

    public final void onEnrollmentHelp() {
        UdfpsEnrollHelper udfpsEnrollHelper = this.enrollHelper;
        if (udfpsEnrollHelper != null) {
            udfpsEnrollHelper.onEnrollmentHelp();
        }
    }

    public final void onTouchOutsideOfSensorArea(float f, float f2, float f3, float f4, int i) {
        if (this.touchExplorationEnabled) {
            String[] stringArray = this.context.getResources().getStringArray(C1894R.array.udfps_accessibility_touch_hints);
            if (stringArray.length != 4) {
                Log.e("UdfpsControllerOverlay", "expected exactly 4 touch hints, got " + stringArray + ".size?");
                return;
            }
            String onTouchOutsideOfSensorAreaImpl = onTouchOutsideOfSensorAreaImpl(f, f2, f3, f4, i);
            Log.v("UdfpsControllerOverlay", "Announcing touch outside : " + onTouchOutsideOfSensorAreaImpl);
            UdfpsAnimationViewController<?> animationViewController = getAnimationViewController();
            if (animationViewController != null) {
                animationViewController.doAnnounceForAccessibility(onTouchOutsideOfSensorAreaImpl);
            }
        }
    }

    public final String onTouchOutsideOfSensorAreaImpl(float f, float f2, float f3, float f4, int i) {
        String[] stringArray = this.context.getResources().getStringArray(C1894R.array.udfps_accessibility_touch_hints);
        double atan2 = Math.atan2((double) (f4 - f2), (double) (f - f3));
        if (atan2 < 0.0d) {
            atan2 += 6.283185307179586d;
        }
        double degrees = Math.toDegrees(atan2);
        double length = 360.0d / ((double) stringArray.length);
        int length2 = ((int) (((degrees + (length / 2.0d)) % ((double) StackStateAnimator.ANIMATION_DURATION_STANDARD)) / length)) % stringArray.length;
        if (i == 1) {
            length2 = (length2 + 1) % stringArray.length;
        }
        if (i == 3) {
            length2 = (length2 + 3) % stringArray.length;
        }
        String str = stringArray[length2];
        Intrinsics.checkNotNullExpressionValue(str, "touchHints[index]");
        return str;
    }

    public final void cancel() {
        try {
            this.controllerCallback.onUserCanceled();
        } catch (RemoteException e) {
            Log.e("UdfpsControllerOverlay", "Remote exception", e);
        }
    }

    public final boolean matchesRequestId(long j) {
        long j2 = this.requestId;
        return j2 == -1 || j2 == j;
    }

    private final WindowManager.LayoutParams updateDimensions(WindowManager.LayoutParams layoutParams, UdfpsAnimationViewController<?> udfpsAnimationViewController) {
        int paddingX = udfpsAnimationViewController != null ? udfpsAnimationViewController.getPaddingX() : 0;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.getPaddingY();
        }
        if (udfpsAnimationViewController != null && udfpsAnimationViewController.listenForTouchesOutsideView()) {
            layoutParams.flags |= 262144;
        }
        NTLogUtil.m1688i("UdfpsControllerOverlay", "computeLayoutParams sensorBounds=" + this.overlayParams.getSensorBounds() + ", padding=" + paddingX);
        Rect rect = new Rect(this.overlayParams.getSensorBounds());
        int rotation = this.overlayParams.getRotation();
        if (rotation == 1 || rotation == 3) {
            if (!shouldRotate(udfpsAnimationViewController)) {
                Log.v("UdfpsControllerOverlay", "Skip rotating UDFPS bounds " + Surface.rotationToString(rotation) + " animation=" + udfpsAnimationViewController + " isGoingToSleep=" + this.keyguardUpdateMonitor.isGoingToSleep() + " isOccluded=" + this.keyguardStateController.isOccluded());
            } else {
                Log.v("UdfpsControllerOverlay", "Rotate UDFPS bounds " + Surface.rotationToString(rotation));
                RotationUtils.rotateBounds(rect, this.overlayParams.getNaturalDisplayWidth(), this.overlayParams.getNaturalDisplayHeight(), rotation);
            }
        }
        int width = rect.width() / 2;
        layoutParams.x = (rect.left + width) - (this.FPS_SCNNING_ANIM_SIZE / 2);
        layoutParams.y = (rect.top + width) - (this.FPS_SCNNING_ANIM_SIZE / 2);
        layoutParams.height = this.FPS_SCNNING_ANIM_SIZE;
        layoutParams.width = this.FPS_SCNNING_ANIM_SIZE;
        return layoutParams;
    }

    private final boolean shouldRotate(UdfpsAnimationViewController<?> udfpsAnimationViewController) {
        if (!(udfpsAnimationViewController instanceof UdfpsKeyguardViewController)) {
            return true;
        }
        if (this.keyguardUpdateMonitor.isGoingToSleep() || !this.keyguardStateController.isOccluded()) {
            return false;
        }
        return true;
    }

    static /* synthetic */ View addUdfpsView$default(UdfpsControllerOverlay udfpsControllerOverlay, UdfpsView udfpsView, int i, Function1 function1, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            Intrinsics.needClassReification();
            function1 = UdfpsControllerOverlay$addUdfpsView$1.INSTANCE;
        }
        View inflate = udfpsControllerOverlay.inflater.inflate(i, (ViewGroup) null);
        Intrinsics.reifiedOperationMarker(1, ExifInterface.GPS_DIRECTION_TRUE);
        View view = inflate;
        udfpsView.addView(inflate);
        function1.invoke(inflate);
        udfpsControllerOverlay.updateInnerViewLayoutParams(inflate);
        return inflate;
    }

    private final /* synthetic */ <T extends View> T addUdfpsView(UdfpsView udfpsView, int i, Function1<? super T, Unit> function1) {
        T inflate = this.inflater.inflate(i, (ViewGroup) null);
        Intrinsics.reifiedOperationMarker(1, ExifInterface.GPS_DIRECTION_TRUE);
        View view = (View) inflate;
        udfpsView.addView(inflate);
        function1.invoke(inflate);
        updateInnerViewLayoutParams(inflate);
        return inflate;
    }

    /* access modifiers changed from: private */
    public final void updateInnerViewLayoutParams(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
            layoutParams2.width = this.overlayParams.getSensorBounds().width();
            layoutParams2.height = this.overlayParams.getSensorBounds().height();
            layoutParams2.topMargin = (this.FPS_SCNNING_ANIM_SIZE / 2) - (this.overlayParams.getSensorBounds().height() / 2);
            layoutParams2.setMarginStart(layoutParams2.topMargin);
            view.setLayoutParams(layoutParams2);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
    }

    public final void onDozingChanged(boolean z) {
        if (z) {
            NTLogUtil.m1688i("UdfpsControllerOverlay", "onDozingChanged update icon Alpha");
            UdfpsAnimationViewController<?> animationViewController = getAnimationViewController();
            if (animationViewController != null) {
                animationViewController.updateAlpha(1.0f);
            }
            reattachToWM("dozing");
        }
    }

    public final void reattachToWM(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        UdfpsView udfpsView = this.overlayView;
        if (udfpsView != null && udfpsView.isAttachedToWindow()) {
            Log.d("UdfpsControllerOverlay", "reAttachToWM " + str);
            View view = udfpsView;
            this.windowManager.removeView(view);
            this.windowManager.addView(view, this.coreLayoutParams);
        }
    }
}
