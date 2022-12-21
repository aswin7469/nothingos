package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.util.DisplayMetrics;
import android.view.Display;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.LightRevealEffect;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.leak.RotationUtils;
import java.p026io.PrintWriter;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@CentralSurfacesComponent.CentralSurfacesScope
@Metadata(mo64986d1 = {"\u0000¶\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b*\u0004#(2D\b\u0007\u0018\u0000 Y2\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u00032\u00020\u0004:\u0002XYB\b\u0007\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0018\u0012\u0006\u0010\u0019\u001a\u00020\u001a\u0012\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c\u0012\u0006\u0010\u001e\u001a\u00020\u001f\u0012\b\u0010 \u001a\u0004\u0018\u00010\u0002¢\u0006\u0002\u0010!J\u0006\u0010H\u001a\u00020;J\b\u0010I\u001a\u00020JH\u0014J\b\u0010K\u001a\u00020JH\u0016J\b\u0010L\u001a\u00020JH\u0016J\b\u0010M\u001a\u00020JH\u0017J\b\u0010N\u001a\u00020JH\u0017J\b\u0010O\u001a\u00020JH\u0002J\u0010\u0010P\u001a\u00020J2\b\u0010Q\u001a\u0004\u0018\u00010RJ\b\u0010S\u001a\u00020JH\u0002J\b\u0010T\u001a\u00020JH\u0002J\b\u0010U\u001a\u00020JH\u0002J\u0006\u0010V\u001a\u00020JJ\b\u0010W\u001a\u00020JH\u0002R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u00020#X\u0004¢\u0006\u0004\n\u0002\u0010$R\u000e\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u0004\u0018\u00010&X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010'\u001a\u00020(X\u0004¢\u0006\u0004\n\u0002\u0010)R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u000e¢\u0006\u0002\n\u0000R\u001c\u0010,\u001a\u0004\u0018\u00010+X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u0010\u00101\u001a\u000202X\u0004¢\u0006\u0004\n\u0002\u00103R\u001c\u00104\u001a\u0004\u0018\u000105X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u00107\"\u0004\b8\u00109R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R$\u0010:\u001a\u00020;8\u0000@\u0000X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b<\u0010=\u001a\u0004\b>\u0010?\"\u0004\b@\u0010AR\u000e\u0010\u001e\u001a\u00020\u001fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010B\u001a\u0004\u0018\u00010\u001dX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010C\u001a\u00020DX\u0004¢\u0006\u0004\n\u0002\u0010ER\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020GX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000¨\u0006Z"}, mo64987d2 = {"Lcom/android/systemui/biometrics/AuthRippleController;", "Lcom/android/systemui/util/ViewController;", "Lcom/android/systemui/biometrics/AuthRippleView;", "Lcom/android/systemui/statusbar/policy/KeyguardStateController$Callback;", "Lcom/android/systemui/keyguard/WakefulnessLifecycle$Observer;", "centralSurfaces", "Lcom/android/systemui/statusbar/phone/CentralSurfaces;", "sysuiContext", "Landroid/content/Context;", "authController", "Lcom/android/systemui/biometrics/AuthController;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "keyguardUpdateMonitor", "Lcom/android/keyguard/KeyguardUpdateMonitor;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "wakefulnessLifecycle", "Lcom/android/systemui/keyguard/WakefulnessLifecycle;", "commandRegistry", "Lcom/android/systemui/statusbar/commandline/CommandRegistry;", "notificationShadeWindowController", "Lcom/android/systemui/statusbar/NotificationShadeWindowController;", "bypassController", "Lcom/android/systemui/statusbar/phone/KeyguardBypassController;", "biometricUnlockController", "Lcom/android/systemui/statusbar/phone/BiometricUnlockController;", "udfpsControllerProvider", "Ljavax/inject/Provider;", "Lcom/android/systemui/biometrics/UdfpsController;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "rippleView", "(Lcom/android/systemui/statusbar/phone/CentralSurfaces;Landroid/content/Context;Lcom/android/systemui/biometrics/AuthController;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/keyguard/KeyguardUpdateMonitor;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Lcom/android/systemui/keyguard/WakefulnessLifecycle;Lcom/android/systemui/statusbar/commandline/CommandRegistry;Lcom/android/systemui/statusbar/NotificationShadeWindowController;Lcom/android/systemui/statusbar/phone/KeyguardBypassController;Lcom/android/systemui/statusbar/phone/BiometricUnlockController;Ljavax/inject/Provider;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/biometrics/AuthRippleView;)V", "authControllerCallback", "com/android/systemui/biometrics/AuthRippleController$authControllerCallback$1", "Lcom/android/systemui/biometrics/AuthRippleController$authControllerCallback$1;", "circleReveal", "Lcom/android/systemui/statusbar/LightRevealEffect;", "configurationChangedListener", "com/android/systemui/biometrics/AuthRippleController$configurationChangedListener$1", "Lcom/android/systemui/biometrics/AuthRippleController$configurationChangedListener$1;", "faceSensorLocation", "Landroid/graphics/PointF;", "fingerprintSensorLocation", "getFingerprintSensorLocation", "()Landroid/graphics/PointF;", "setFingerprintSensorLocation", "(Landroid/graphics/PointF;)V", "keyguardUpdateMonitorCallback", "com/android/systemui/biometrics/AuthRippleController$keyguardUpdateMonitorCallback$1", "Lcom/android/systemui/biometrics/AuthRippleController$keyguardUpdateMonitorCallback$1;", "lightRevealScrimAnimator", "Landroid/animation/ValueAnimator;", "getLightRevealScrimAnimator", "()Landroid/animation/ValueAnimator;", "setLightRevealScrimAnimator", "(Landroid/animation/ValueAnimator;)V", "startLightRevealScrimOnKeyguardFadingAway", "", "getStartLightRevealScrimOnKeyguardFadingAway$SystemUI_nothingRelease$annotations", "()V", "getStartLightRevealScrimOnKeyguardFadingAway$SystemUI_nothingRelease", "()Z", "setStartLightRevealScrimOnKeyguardFadingAway$SystemUI_nothingRelease", "(Z)V", "udfpsController", "udfpsControllerCallback", "com/android/systemui/biometrics/AuthRippleController$udfpsControllerCallback$1", "Lcom/android/systemui/biometrics/AuthRippleController$udfpsControllerCallback$1;", "udfpsRadius", "", "isAnimatingLightRevealScrim", "onInit", "", "onKeyguardFadingAwayChanged", "onStartedGoingToSleep", "onViewAttached", "onViewDetached", "showDwellRipple", "showUnlockRipple", "biometricSourceType", "Landroid/hardware/biometrics/BiometricSourceType;", "showUnlockedRipple", "updateFingerprintLocation", "updateRippleColor", "updateSensorLocation", "updateUdfpsDependentParams", "AuthRippleCommand", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AuthRippleController.kt */
public final class AuthRippleController extends ViewController<AuthRippleView> implements KeyguardStateController.Callback, WakefulnessLifecycle.Observer {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final long RIPPLE_ANIMATION_DURATION = 1533;
    private final AuthController authController;
    private final AuthRippleController$authControllerCallback$1 authControllerCallback = new AuthRippleController$authControllerCallback$1(this);
    private final BiometricUnlockController biometricUnlockController;
    private final KeyguardBypassController bypassController;
    private final CentralSurfaces centralSurfaces;
    /* access modifiers changed from: private */
    public LightRevealEffect circleReveal;
    private final CommandRegistry commandRegistry;
    private final AuthRippleController$configurationChangedListener$1 configurationChangedListener = new AuthRippleController$configurationChangedListener$1(this);
    private final ConfigurationController configurationController;
    /* access modifiers changed from: private */
    public PointF faceSensorLocation;
    private PointF fingerprintSensorLocation;
    private final KeyguardStateController keyguardStateController;
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final AuthRippleController$keyguardUpdateMonitorCallback$1 keyguardUpdateMonitorCallback = new AuthRippleController$keyguardUpdateMonitorCallback$1(this);
    private ValueAnimator lightRevealScrimAnimator;
    private final NotificationShadeWindowController notificationShadeWindowController;
    private boolean startLightRevealScrimOnKeyguardFadingAway;
    private final StatusBarStateController statusBarStateController;
    private final Context sysuiContext;
    private UdfpsController udfpsController;
    private final AuthRippleController$udfpsControllerCallback$1 udfpsControllerCallback = new AuthRippleController$udfpsControllerCallback$1(this);
    private final Provider<UdfpsController> udfpsControllerProvider;
    /* access modifiers changed from: private */
    public float udfpsRadius = -1.0f;
    private final WakefulnessLifecycle wakefulnessLifecycle;

    /* renamed from: getStartLightRevealScrimOnKeyguardFadingAway$SystemUI_nothingRelease$annotations */
    public static /* synthetic */ void m327x1169c02d() {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public AuthRippleController(CentralSurfaces centralSurfaces2, Context context, AuthController authController2, ConfigurationController configurationController2, KeyguardUpdateMonitor keyguardUpdateMonitor2, KeyguardStateController keyguardStateController2, WakefulnessLifecycle wakefulnessLifecycle2, CommandRegistry commandRegistry2, NotificationShadeWindowController notificationShadeWindowController2, KeyguardBypassController keyguardBypassController, BiometricUnlockController biometricUnlockController2, Provider<UdfpsController> provider, StatusBarStateController statusBarStateController2, AuthRippleView authRippleView) {
        super(authRippleView);
        Intrinsics.checkNotNullParameter(centralSurfaces2, "centralSurfaces");
        Intrinsics.checkNotNullParameter(context, "sysuiContext");
        Intrinsics.checkNotNullParameter(authController2, "authController");
        Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor2, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle2, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(commandRegistry2, "commandRegistry");
        Intrinsics.checkNotNullParameter(notificationShadeWindowController2, "notificationShadeWindowController");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "bypassController");
        Intrinsics.checkNotNullParameter(biometricUnlockController2, "biometricUnlockController");
        Intrinsics.checkNotNullParameter(provider, "udfpsControllerProvider");
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        this.centralSurfaces = centralSurfaces2;
        this.sysuiContext = context;
        this.authController = authController2;
        this.configurationController = configurationController2;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.keyguardStateController = keyguardStateController2;
        this.wakefulnessLifecycle = wakefulnessLifecycle2;
        this.commandRegistry = commandRegistry2;
        this.notificationShadeWindowController = notificationShadeWindowController2;
        this.bypassController = keyguardBypassController;
        this.biometricUnlockController = biometricUnlockController2;
        this.udfpsControllerProvider = provider;
        this.statusBarStateController = statusBarStateController2;
    }

    /* renamed from: getStartLightRevealScrimOnKeyguardFadingAway$SystemUI_nothingRelease */
    public final boolean mo30706xe7cf5f2d() {
        return this.startLightRevealScrimOnKeyguardFadingAway;
    }

    /* renamed from: setStartLightRevealScrimOnKeyguardFadingAway$SystemUI_nothingRelease */
    public final void mo30710x3cf733a1(boolean z) {
        this.startLightRevealScrimOnKeyguardFadingAway = z;
    }

    public final ValueAnimator getLightRevealScrimAnimator() {
        return this.lightRevealScrimAnimator;
    }

    public final void setLightRevealScrimAnimator(ValueAnimator valueAnimator) {
        this.lightRevealScrimAnimator = valueAnimator;
    }

    public final PointF getFingerprintSensorLocation() {
        return this.fingerprintSensorLocation;
    }

    public final void setFingerprintSensorLocation(PointF pointF) {
        this.fingerprintSensorLocation = pointF;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        ((AuthRippleView) this.mView).setAlphaInDuration((long) this.sysuiContext.getResources().getInteger(C1893R.integer.auth_ripple_alpha_in_duration));
    }

    public void onViewAttached() {
        this.authController.addCallback(this.authControllerCallback);
        updateRippleColor();
        updateSensorLocation();
        updateUdfpsDependentParams();
        UdfpsController udfpsController2 = this.udfpsController;
        if (udfpsController2 != null) {
            udfpsController2.addCallback(this.udfpsControllerCallback);
        }
        this.configurationController.addCallback(this.configurationChangedListener);
        this.keyguardUpdateMonitor.registerCallback(this.keyguardUpdateMonitorCallback);
        this.keyguardStateController.addCallback(this);
        this.wakefulnessLifecycle.addObserver(this);
        this.commandRegistry.registerCommand("auth-ripple", new AuthRippleController$onViewAttached$1(this));
    }

    public void onViewDetached() {
        UdfpsController udfpsController2 = this.udfpsController;
        if (udfpsController2 != null) {
            udfpsController2.removeCallback(this.udfpsControllerCallback);
        }
        this.authController.removeCallback(this.authControllerCallback);
        this.keyguardUpdateMonitor.removeCallback(this.keyguardUpdateMonitorCallback);
        this.configurationController.removeCallback(this.configurationChangedListener);
        this.keyguardStateController.removeCallback(this);
        this.wakefulnessLifecycle.removeObserver(this);
        this.commandRegistry.unregisterCommand("auth-ripple");
        this.notificationShadeWindowController.setForcePluginOpen(false, this);
    }

    public final void showUnlockRipple(BiometricSourceType biometricSourceType) {
        if ((this.keyguardUpdateMonitor.isKeyguardVisible() || this.keyguardUpdateMonitor.isDreaming()) && !this.keyguardUpdateMonitor.userNeedsStrongAuth()) {
            updateSensorLocation();
            if (biometricSourceType == BiometricSourceType.FINGERPRINT && this.fingerprintSensorLocation != null) {
                PointF pointF = this.fingerprintSensorLocation;
                Intrinsics.checkNotNull(pointF);
                ((AuthRippleView) this.mView).setFingerprintSensorLocation(pointF, this.udfpsRadius);
                showUnlockedRipple();
            } else if (biometricSourceType == BiometricSourceType.FACE && this.faceSensorLocation != null && this.bypassController.canBypass()) {
                PointF pointF2 = this.faceSensorLocation;
                Intrinsics.checkNotNull(pointF2);
                ((AuthRippleView) this.mView).setSensorLocation(pointF2);
                showUnlockedRipple();
            }
        }
    }

    /* access modifiers changed from: private */
    public final void showUnlockedRipple() {
        LightRevealEffect lightRevealEffect;
        this.notificationShadeWindowController.setForcePluginOpen(true, this);
        LightRevealScrim lightRevealScrim = this.centralSurfaces.getLightRevealScrim();
        if ((this.statusBarStateController.isDozing() || this.biometricUnlockController.isWakeAndUnlock()) && (lightRevealEffect = this.circleReveal) != null) {
            if (lightRevealScrim != null) {
                lightRevealScrim.setRevealAmount(0.0f);
            }
            if (lightRevealScrim != null) {
                lightRevealScrim.setRevealEffect(lightRevealEffect);
            }
            this.startLightRevealScrimOnKeyguardFadingAway = true;
        }
        ((AuthRippleView) this.mView).startUnlockedRipple(new AuthRippleController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: showUnlockedRipple$lambda-1  reason: not valid java name */
    public static final void m2561showUnlockedRipple$lambda1(AuthRippleController authRippleController) {
        Intrinsics.checkNotNullParameter(authRippleController, "this$0");
        authRippleController.notificationShadeWindowController.setForcePluginOpen(false, authRippleController);
    }

    public void onKeyguardFadingAwayChanged() {
        if (this.keyguardStateController.isKeyguardFadingAway()) {
            LightRevealScrim lightRevealScrim = this.centralSurfaces.getLightRevealScrim();
            if (this.startLightRevealScrimOnKeyguardFadingAway && lightRevealScrim != null) {
                ValueAnimator valueAnimator = this.lightRevealScrimAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.1f, 1.0f});
                ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ofFloat.setDuration(RIPPLE_ANIMATION_DURATION);
                ofFloat.setStartDelay(this.keyguardStateController.getKeyguardFadingAwayDelay());
                ofFloat.addUpdateListener(new AuthRippleController$$ExternalSyntheticLambda0(lightRevealScrim, this, ofFloat));
                ofFloat.addListener(new AuthRippleController$onKeyguardFadingAwayChanged$1$2(lightRevealScrim, this));
                ofFloat.start();
                this.lightRevealScrimAnimator = ofFloat;
                this.startLightRevealScrimOnKeyguardFadingAway = false;
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onKeyguardFadingAwayChanged$lambda-3$lambda-2  reason: not valid java name */
    public static final void m2560onKeyguardFadingAwayChanged$lambda3$lambda2(LightRevealScrim lightRevealScrim, AuthRippleController authRippleController, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
        Intrinsics.checkNotNullParameter(authRippleController, "this$0");
        if (!Intrinsics.areEqual((Object) lightRevealScrim.getRevealEffect(), (Object) authRippleController.circleReveal)) {
            valueAnimator.cancel();
            return;
        }
        Object animatedValue = valueAnimator2.getAnimatedValue();
        if (animatedValue != null) {
            lightRevealScrim.setRevealAmount(((Float) animatedValue).floatValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public final boolean isAnimatingLightRevealScrim() {
        ValueAnimator valueAnimator = this.lightRevealScrimAnimator;
        if (valueAnimator != null) {
            return valueAnimator.isRunning();
        }
        return false;
    }

    public void onStartedGoingToSleep() {
        this.startLightRevealScrimOnKeyguardFadingAway = false;
    }

    public final void updateSensorLocation() {
        updateFingerprintLocation();
        this.faceSensorLocation = this.authController.getFaceAuthSensorLocation();
        PointF pointF = this.fingerprintSensorLocation;
        if (pointF != null) {
            this.circleReveal = new CircleReveal(pointF.x, pointF.y, 0.0f, Math.max(Math.max(pointF.x, this.centralSurfaces.getDisplayWidth() - pointF.x), Math.max(pointF.y, this.centralSurfaces.getDisplayHeight() - pointF.y)));
        }
    }

    private final void updateFingerprintLocation() {
        PointF pointF;
        PointF pointF2;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = this.sysuiContext.getDisplay();
        if (display != null) {
            display.getRealMetrics(displayMetrics);
        }
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        PointF fingerprintSensorLocation2 = this.authController.getFingerprintSensorLocation();
        if (fingerprintSensorLocation2 != null) {
            int rotation = RotationUtils.getRotation(this.sysuiContext);
            if (rotation != 1) {
                if (rotation == 2) {
                    pointF = new PointF(((float) i) - fingerprintSensorLocation2.x, ((float) i2) - fingerprintSensorLocation2.y);
                } else if (rotation != 3) {
                    pointF = new PointF(fingerprintSensorLocation2.x, fingerprintSensorLocation2.y);
                } else {
                    float f = (float) i;
                    float f2 = (float) i2;
                    pointF2 = new PointF(f * (((float) 1) - (fingerprintSensorLocation2.y / f)), f2 * (fingerprintSensorLocation2.x / f2));
                }
                this.fingerprintSensorLocation = pointF;
            }
            float f3 = (float) i;
            float f4 = (float) i2;
            pointF2 = new PointF(f3 * (fingerprintSensorLocation2.y / f3), f4 * (((float) 1) - (fingerprintSensorLocation2.x / f4)));
            pointF = pointF2;
            this.fingerprintSensorLocation = pointF;
        }
    }

    /* access modifiers changed from: private */
    public final void updateRippleColor() {
        ((AuthRippleView) this.mView).setLockScreenColor(Utils.getColorAttrDefaultColor(this.sysuiContext, C1893R.attr.wallpaperTextColorAccent));
    }

    /* access modifiers changed from: private */
    public final void showDwellRipple() {
        ((AuthRippleView) this.mView).startDwellRipple(this.statusBarStateController.isDozing());
    }

    /* access modifiers changed from: private */
    public final void updateUdfpsDependentParams() {
        UdfpsController udfpsController2;
        List<FingerprintSensorPropertiesInternal> udfpsProps = this.authController.getUdfpsProps();
        if (udfpsProps != null && udfpsProps.size() > 0) {
            this.udfpsController = this.udfpsControllerProvider.get();
            this.udfpsRadius = this.authController.getUdfpsRadius();
            if (((AuthRippleView) this.mView).isAttachedToWindow() && (udfpsController2 = this.udfpsController) != null) {
                udfpsController2.addCallback(this.udfpsControllerCallback);
            }
        }
    }

    @Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\f"}, mo64987d2 = {"Lcom/android/systemui/biometrics/AuthRippleController$AuthRippleCommand;", "Lcom/android/systemui/statusbar/commandline/Command;", "(Lcom/android/systemui/biometrics/AuthRippleController;)V", "execute", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "help", "invalidCommand", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: AuthRippleController.kt */
    public final class AuthRippleCommand implements Command {
        public AuthRippleCommand() {
        }

        public void execute(PrintWriter printWriter, List<String> list) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(list, "args");
            if (list.isEmpty()) {
                invalidCommand(printWriter);
                return;
            }
            String str = list.get(0);
            switch (str.hashCode()) {
                case -1375934236:
                    if (str.equals("fingerprint")) {
                        AuthRippleController.this.updateSensorLocation();
                        printWriter.println("fingerprint ripple sensorLocation=" + AuthRippleController.this.getFingerprintSensorLocation());
                        AuthRippleController.this.showUnlockRipple(BiometricSourceType.FINGERPRINT);
                        return;
                    }
                    break;
                case -1349088399:
                    if (str.equals("custom")) {
                        if (list.size() != 3 || StringsKt.toFloatOrNull(list.get(1)) == null || StringsKt.toFloatOrNull(list.get(2)) == null) {
                            invalidCommand(printWriter);
                            return;
                        }
                        printWriter.println("custom ripple sensorLocation=" + Float.parseFloat(list.get(1)) + ", " + Float.parseFloat(list.get(2)));
                        ((AuthRippleView) AuthRippleController.this.mView).setSensorLocation(new PointF(Float.parseFloat(list.get(1)), Float.parseFloat(list.get(2))));
                        AuthRippleController.this.showUnlockedRipple();
                        return;
                    }
                    break;
                case 3135069:
                    if (str.equals("face")) {
                        AuthRippleController.this.updateSensorLocation();
                        printWriter.println("face ripple sensorLocation=" + AuthRippleController.this.faceSensorLocation);
                        AuthRippleController.this.showUnlockRipple(BiometricSourceType.FACE);
                        return;
                    }
                    break;
                case 95997746:
                    if (str.equals("dwell")) {
                        AuthRippleController.this.showDwellRipple();
                        printWriter.println("lock screen dwell ripple: \n\tsensorLocation=" + AuthRippleController.this.getFingerprintSensorLocation() + "\n\tudfpsRadius=" + AuthRippleController.this.udfpsRadius);
                        return;
                    }
                    break;
            }
            invalidCommand(printWriter);
        }

        public void help(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("Usage: adb shell cmd statusbar auth-ripple <command>");
            printWriter.println("Available commands:");
            printWriter.println("  dwell");
            printWriter.println("  fingerprint");
            printWriter.println("  face");
            printWriter.println("  custom <x-location: int> <y-location: int>");
        }

        public final void invalidCommand(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("invalid command");
            help(printWriter);
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/biometrics/AuthRippleController$Companion;", "", "()V", "RIPPLE_ANIMATION_DURATION", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: AuthRippleController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
