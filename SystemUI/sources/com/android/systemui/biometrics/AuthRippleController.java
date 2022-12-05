package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.R$integer;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.LightRevealEffect;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import javax.inject.Provider;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringNumberConversionsJVMKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: AuthRippleController.kt */
/* loaded from: classes.dex */
public final class AuthRippleController extends ViewController<AuthRippleView> implements KeyguardStateController.Callback, WakefulnessLifecycle.Observer {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final AuthController authController;
    @NotNull
    private final BiometricUnlockController biometricUnlockController;
    @NotNull
    private final KeyguardBypassController bypassController;
    @Nullable
    private LightRevealEffect circleReveal;
    @NotNull
    private final CommandRegistry commandRegistry;
    @NotNull
    private final ConfigurationController configurationController;
    @Nullable
    private PointF faceSensorLocation;
    @Nullable
    private PointF fingerprintSensorLocation;
    @NotNull
    private final KeyguardStateController keyguardStateController;
    @NotNull
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    @NotNull
    private final NotificationShadeWindowController notificationShadeWindowController;
    private boolean startLightRevealScrimOnKeyguardFadingAway;
    @NotNull
    private final StatusBar statusBar;
    @NotNull
    private final StatusBarStateController statusBarStateController;
    @NotNull
    private final Context sysuiContext;
    @Nullable
    private UdfpsController udfpsController;
    @NotNull
    private final Provider<UdfpsController> udfpsControllerProvider;
    @NotNull
    private final WakefulnessLifecycle wakefulnessLifecycle;
    private float dwellScale = 2.0f;
    private float expandedDwellScale = 2.5f;
    private float aodDwellScale = 1.9f;
    private float aodExpandedDwellScale = 2.3f;
    private float udfpsRadius = -1.0f;
    @NotNull
    private final AuthRippleController$keyguardUpdateMonitorCallback$1 keyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.biometrics.AuthRippleController$keyguardUpdateMonitorCallback$1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthenticated(int i, @Nullable BiometricSourceType biometricSourceType, boolean z) {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthFailed(@Nullable BiometricSourceType biometricSourceType) {
            ((AuthRippleView) ((ViewController) AuthRippleController.this).mView).retractRipple();
        }
    };
    @NotNull
    private final AuthRippleController$configurationChangedListener$1 configurationChangedListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.biometrics.AuthRippleController$configurationChangedListener$1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onConfigChanged(@Nullable Configuration configuration) {
            AuthRippleController.this.updateSensorLocation();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onUiModeChanged() {
            AuthRippleController.this.updateRippleColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onThemeChanged() {
            AuthRippleController.this.updateRippleColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onOverlayChanged() {
            AuthRippleController.this.updateRippleColor();
        }
    };
    @NotNull
    private final AuthRippleController$udfpsControllerCallback$1 udfpsControllerCallback = new UdfpsController.Callback() { // from class: com.android.systemui.biometrics.AuthRippleController$udfpsControllerCallback$1
        @Override // com.android.systemui.biometrics.UdfpsController.Callback
        public void onFingerDown() {
            if (AuthRippleController.this.getFingerprintSensorLocation() != null) {
                PointF fingerprintSensorLocation = AuthRippleController.this.getFingerprintSensorLocation();
                Intrinsics.checkNotNull(fingerprintSensorLocation);
                ((AuthRippleView) ((ViewController) AuthRippleController.this).mView).setSensorLocation(fingerprintSensorLocation);
                return;
            }
            Log.e("AuthRipple", "fingerprintSensorLocation=null onFingerDown. Skip showing dwell ripple");
        }

        @Override // com.android.systemui.biometrics.UdfpsController.Callback
        public void onFingerUp() {
            ((AuthRippleView) ((ViewController) AuthRippleController.this).mView).retractRipple();
        }
    };
    @NotNull
    private final AuthController.Callback authControllerCallback = new AuthController.Callback() { // from class: com.android.systemui.biometrics.AuthRippleController$authControllerCallback$1
        @Override // com.android.systemui.biometrics.AuthController.Callback
        public final void onAllAuthenticatorsRegistered() {
            AuthRippleController.this.updateSensorLocation();
            AuthRippleController.this.updateUdfpsDependentParams();
        }
    };

    public static /* synthetic */ void getStartLightRevealScrimOnKeyguardFadingAway$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Type inference failed for: r2v6, types: [com.android.systemui.biometrics.AuthRippleController$keyguardUpdateMonitorCallback$1] */
    /* JADX WARN: Type inference failed for: r2v7, types: [com.android.systemui.biometrics.AuthRippleController$configurationChangedListener$1] */
    /* JADX WARN: Type inference failed for: r2v8, types: [com.android.systemui.biometrics.AuthRippleController$udfpsControllerCallback$1] */
    public AuthRippleController(@NotNull StatusBar statusBar, @NotNull Context sysuiContext, @NotNull AuthController authController, @NotNull ConfigurationController configurationController, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor, @NotNull KeyguardStateController keyguardStateController, @NotNull WakefulnessLifecycle wakefulnessLifecycle, @NotNull CommandRegistry commandRegistry, @NotNull NotificationShadeWindowController notificationShadeWindowController, @NotNull KeyguardBypassController bypassController, @NotNull BiometricUnlockController biometricUnlockController, @NotNull Provider<UdfpsController> udfpsControllerProvider, @NotNull StatusBarStateController statusBarStateController, @Nullable AuthRippleView authRippleView) {
        super(authRippleView);
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        Intrinsics.checkNotNullParameter(sysuiContext, "sysuiContext");
        Intrinsics.checkNotNullParameter(authController, "authController");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        Intrinsics.checkNotNullParameter(notificationShadeWindowController, "notificationShadeWindowController");
        Intrinsics.checkNotNullParameter(bypassController, "bypassController");
        Intrinsics.checkNotNullParameter(biometricUnlockController, "biometricUnlockController");
        Intrinsics.checkNotNullParameter(udfpsControllerProvider, "udfpsControllerProvider");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        this.statusBar = statusBar;
        this.sysuiContext = sysuiContext;
        this.authController = authController;
        this.configurationController = configurationController;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        this.keyguardStateController = keyguardStateController;
        this.wakefulnessLifecycle = wakefulnessLifecycle;
        this.commandRegistry = commandRegistry;
        this.notificationShadeWindowController = notificationShadeWindowController;
        this.bypassController = bypassController;
        this.biometricUnlockController = biometricUnlockController;
        this.udfpsControllerProvider = udfpsControllerProvider;
        this.statusBarStateController = statusBarStateController;
    }

    @Nullable
    public final PointF getFingerprintSensorLocation() {
        return this.fingerprintSensorLocation;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        ((AuthRippleView) this.mView).setAlphaInDuration(this.sysuiContext.getResources().getInteger(R$integer.auth_ripple_alpha_in_duration));
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
        this.authController.addCallback(this.authControllerCallback);
        updateRippleColor();
        updateSensorLocation();
        updateUdfpsDependentParams();
        UdfpsController udfpsController = this.udfpsController;
        if (udfpsController != null) {
            udfpsController.addCallback(this.udfpsControllerCallback);
        }
        this.configurationController.addCallback(this.configurationChangedListener);
        this.keyguardUpdateMonitor.registerCallback(this.keyguardUpdateMonitorCallback);
        this.keyguardStateController.addCallback(this);
        this.wakefulnessLifecycle.addObserver(this);
        this.commandRegistry.registerCommand("auth-ripple", new AuthRippleController$onViewAttached$1(this));
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewDetached() {
        UdfpsController udfpsController = this.udfpsController;
        if (udfpsController != null) {
            udfpsController.removeCallback(this.udfpsControllerCallback);
        }
        this.authController.removeCallback(this.authControllerCallback);
        this.keyguardUpdateMonitor.removeCallback(this.keyguardUpdateMonitorCallback);
        this.configurationController.removeCallback(this.configurationChangedListener);
        this.keyguardStateController.removeCallback(this);
        this.wakefulnessLifecycle.removeObserver(this);
        this.commandRegistry.unregisterCommand("auth-ripple");
        this.notificationShadeWindowController.setForcePluginOpen(false, this);
    }

    public final void showRipple(@Nullable BiometricSourceType biometricSourceType) {
        PointF pointF;
        if (!this.keyguardUpdateMonitor.isKeyguardVisible() || this.keyguardUpdateMonitor.userNeedsStrongAuth()) {
            return;
        }
        if (biometricSourceType == BiometricSourceType.FINGERPRINT && (pointF = this.fingerprintSensorLocation) != null) {
            Intrinsics.checkNotNull(pointF);
            ((AuthRippleView) this.mView).setSensorLocation(pointF);
            showUnlockedRipple();
        } else if (biometricSourceType != BiometricSourceType.FACE || this.faceSensorLocation == null || !this.bypassController.canBypass()) {
        } else {
            PointF pointF2 = this.faceSensorLocation;
            Intrinsics.checkNotNull(pointF2);
            ((AuthRippleView) this.mView).setSensorLocation(pointF2);
            showUnlockedRipple();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showUnlockedRipple() {
        this.notificationShadeWindowController.setForcePluginOpen(true, this);
        boolean z = this.circleReveal != null && this.biometricUnlockController.isWakeAndUnlock();
        LightRevealScrim lightRevealScrim = this.statusBar.getLightRevealScrim();
        if (z) {
            if (lightRevealScrim != null) {
                LightRevealEffect lightRevealEffect = this.circleReveal;
                Intrinsics.checkNotNull(lightRevealEffect);
                lightRevealScrim.setRevealEffect(lightRevealEffect);
            }
            this.startLightRevealScrimOnKeyguardFadingAway = true;
        }
        ((AuthRippleView) this.mView).startUnlockedRipple(new Runnable() { // from class: com.android.systemui.biometrics.AuthRippleController$showUnlockedRipple$1
            @Override // java.lang.Runnable
            public final void run() {
                NotificationShadeWindowController notificationShadeWindowController;
                notificationShadeWindowController = AuthRippleController.this.notificationShadeWindowController;
                notificationShadeWindowController.setForcePluginOpen(false, AuthRippleController.this);
            }
        });
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public void onKeyguardFadingAwayChanged() {
        if (this.keyguardStateController.isKeyguardFadingAway()) {
            final LightRevealScrim lightRevealScrim = this.statusBar.getLightRevealScrim();
            if (!this.startLightRevealScrimOnKeyguardFadingAway || lightRevealScrim == null) {
                return;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.1f, 1.0f);
            ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
            ofFloat.setDuration(1533L);
            ofFloat.setStartDelay(this.keyguardStateController.getKeyguardFadingAwayDelay());
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleController$onKeyguardFadingAwayChanged$revealAnimator$1$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LightRevealEffect lightRevealEffect;
                    LightRevealEffect revealEffect = LightRevealScrim.this.getRevealEffect();
                    lightRevealEffect = this.circleReveal;
                    if (!Intrinsics.areEqual(revealEffect, lightRevealEffect)) {
                        return;
                    }
                    LightRevealScrim lightRevealScrim2 = LightRevealScrim.this;
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    lightRevealScrim2.setRevealAmount(((Float) animatedValue).floatValue());
                }
            });
            ofFloat.start();
            this.startLightRevealScrimOnKeyguardFadingAway = false;
        }
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onStartedGoingToSleep() {
        this.startLightRevealScrimOnKeyguardFadingAway = false;
    }

    public final void updateSensorLocation() {
        this.fingerprintSensorLocation = this.authController.getFingerprintSensorLocation();
        this.faceSensorLocation = this.authController.getFaceAuthSensorLocation();
        PointF pointF = this.fingerprintSensorLocation;
        if (pointF == null) {
            return;
        }
        float f = pointF.x;
        this.circleReveal = new CircleReveal(f, pointF.y, 0.0f, Math.max(Math.max(f, this.statusBar.getDisplayWidth() - pointF.x), Math.max(pointF.y, this.statusBar.getDisplayHeight() - pointF.y)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateRippleColor() {
        ((AuthRippleView) this.mView).setColor(com.android.settingslib.Utils.getColorAttr(this.sysuiContext, 16843829).getDefaultColor());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showDwellRipple() {
        if (this.statusBarStateController.isDozing()) {
            float f = this.udfpsRadius;
            ((AuthRippleView) this.mView).startDwellRipple(f, this.aodDwellScale * f, this.aodExpandedDwellScale * f, true);
            return;
        }
        float f2 = this.udfpsRadius;
        ((AuthRippleView) this.mView).startDwellRipple(f2, this.dwellScale * f2, this.expandedDwellScale * f2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateUdfpsDependentParams() {
        UdfpsController udfpsController;
        List<FingerprintSensorPropertiesInternal> udfpsProps = this.authController.getUdfpsProps();
        if (udfpsProps != null && udfpsProps.size() > 0) {
            this.udfpsRadius = udfpsProps.get(0).sensorRadius;
            this.udfpsController = this.udfpsControllerProvider.mo1933get();
            if (!((AuthRippleView) this.mView).isAttachedToWindow() || (udfpsController = this.udfpsController) == null) {
                return;
            }
            udfpsController.addCallback(this.udfpsControllerCallback);
        }
    }

    /* compiled from: AuthRippleController.kt */
    /* loaded from: classes.dex */
    public final class AuthRippleCommand implements Command {
        final /* synthetic */ AuthRippleController this$0;

        public AuthRippleCommand(AuthRippleController this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        public final void printLockScreenDwellInfo(@NotNull PrintWriter pw) {
            Intrinsics.checkNotNullParameter(pw, "pw");
            pw.println("lock screen dwell ripple: \n\tsensorLocation=" + this.this$0.getFingerprintSensorLocation() + "\n\tdwellScale=" + this.this$0.dwellScale + "\n\tdwellExpand=" + this.this$0.expandedDwellScale);
        }

        public final void printAodDwellInfo(@NotNull PrintWriter pw) {
            Intrinsics.checkNotNullParameter(pw, "pw");
            pw.println("aod dwell ripple: \n\tsensorLocation=" + this.this$0.getFingerprintSensorLocation() + "\n\tdwellScale=" + this.this$0.aodDwellScale + "\n\tdwellExpand=" + this.this$0.aodExpandedDwellScale);
        }

        @Override // com.android.systemui.statusbar.commandline.Command
        public void execute(@NotNull PrintWriter pw, @NotNull List<String> args) {
            Float floatOrNull;
            Float floatOrNull2;
            Intrinsics.checkNotNullParameter(pw, "pw");
            Intrinsics.checkNotNullParameter(args, "args");
            if (args.isEmpty()) {
                invalidCommand(pw);
                return;
            }
            String str = args.get(0);
            switch (str.hashCode()) {
                case -1375934236:
                    if (str.equals("fingerprint")) {
                        pw.println(Intrinsics.stringPlus("fingerprint ripple sensorLocation=", this.this$0.getFingerprintSensorLocation()));
                        this.this$0.showRipple(BiometricSourceType.FINGERPRINT);
                        return;
                    }
                    break;
                case -1349088399:
                    if (str.equals("custom")) {
                        if (args.size() == 3) {
                            floatOrNull = StringsKt__StringNumberConversionsJVMKt.toFloatOrNull(args.get(1));
                            if (floatOrNull != null) {
                                floatOrNull2 = StringsKt__StringNumberConversionsJVMKt.toFloatOrNull(args.get(2));
                                if (floatOrNull2 != null) {
                                    pw.println("custom ripple sensorLocation=" + Float.parseFloat(args.get(1)) + ", " + Float.parseFloat(args.get(2)));
                                    ((AuthRippleView) ((ViewController) this.this$0).mView).setSensorLocation(new PointF(Float.parseFloat(args.get(1)), Float.parseFloat(args.get(2))));
                                    this.this$0.showUnlockedRipple();
                                    return;
                                }
                            }
                        }
                        invalidCommand(pw);
                        return;
                    }
                    break;
                case 3135069:
                    if (str.equals("face")) {
                        pw.println(Intrinsics.stringPlus("face ripple sensorLocation=", this.this$0.faceSensorLocation));
                        this.this$0.showRipple(BiometricSourceType.FACE);
                        return;
                    }
                    break;
                case 95997746:
                    if (str.equals("dwell")) {
                        this.this$0.showDwellRipple();
                        if (this.this$0.statusBarStateController.isDozing()) {
                            printAodDwellInfo(pw);
                            return;
                        } else {
                            printLockScreenDwellInfo(pw);
                            return;
                        }
                    }
                    break;
            }
            invalidCommand(pw);
        }

        public void help(@NotNull PrintWriter pw) {
            Intrinsics.checkNotNullParameter(pw, "pw");
            pw.println("Usage: adb shell cmd statusbar auth-ripple <command>");
            pw.println("Available commands:");
            pw.println("  dwell");
            pw.println("  fingerprint");
            pw.println("  face");
            pw.println("  custom <x-location: int> <y-location: int>");
        }

        public final void invalidCommand(@NotNull PrintWriter pw) {
            Intrinsics.checkNotNullParameter(pw, "pw");
            pw.println("invalid command");
            help(pw);
        }
    }

    /* compiled from: AuthRippleController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
