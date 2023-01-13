package com.android.systemui.statusbar;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.SystemClock;
import android.os.Trace;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.MathUtils;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewRootImpl;
import androidx.core.app.NotificationCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ShadeInterpolation;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.LargeScreenUtils;
import com.android.systemui.util.WallpaperController;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.JvmDefault;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000Æ\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0013\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r*\u00028]\b\u0007\u0018\u0000 \u00012\u00020\u00012\u00020\u0002:\u0006\u0001\u0001\u0001B_\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0018¢\u0006\u0002\u0010\u0019J\u000e\u0010j\u001a\u00020k2\u0006\u0010l\u001a\u00020>J\u0018\u0010m\u001a\u00020k2\u0006\u0010n\u001a\u00020\u001d2\u0006\u0010o\u001a\u00020AH\u0002J%\u0010p\u001a\u00020k2\u0006\u0010q\u001a\u00020r2\u000e\u0010s\u001a\n\u0012\u0006\b\u0001\u0012\u00020u0tH\u0016¢\u0006\u0002\u0010vJ\u0010\u0010w\u001a\u00020k2\u0006\u0010x\u001a\u00020yH\u0016J\u000e\u0010z\u001a\u00020k2\u0006\u0010l\u001a\u00020>J\u0014\u0010{\u001a\u00020k2\n\b\u0002\u0010|\u001a\u0004\u0018\u00010\u001bH\u0002J\b\u0010}\u001a\u00020\u001dH\u0002J\b\u0010~\u001a\u00020kH\u0002J+\u0010\u001a\u00020k2\u0007\u0010\u0001\u001a\u00020A2\u0007\u0010\u0001\u001a\u00020\u001d2\u0006\u0010o\u001a\u00020A2\u0007\u0010\u0001\u001a\u00020;H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R$\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001c\u001a\u00020\u001d@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R$\u0010#\u001a\u00020\u001d2\u0006\u0010\u001c\u001a\u00020\u001d@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010 \"\u0004\b%\u0010\"R(\u0010&\u001a\u00060'R\u00020\u00008\u0006@\u0006X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b(\u0010)\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R$\u0010.\u001a\u00020\u001d2\u0006\u0010\u001c\u001a\u00020\u001d@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u0010 \"\u0004\b0\u0010\"R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R\u0010\u00105\u001a\u0004\u0018\u000106X\u000e¢\u0006\u0002\n\u0000R\u0010\u00107\u001a\u000208X\u0004¢\u0006\u0004\n\u0002\u00109R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020;X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010<\u001a\b\u0012\u0004\u0012\u00020>0=X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010?\u001a\u0004\u0018\u000106X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010@\u001a\u00020AX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bB\u0010C\"\u0004\bD\u0010ER\u000e\u0010F\u001a\u00020;X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020AX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020IX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R$\u0010K\u001a\u00020A2\u0006\u0010\u001c\u001a\u00020A@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bL\u0010C\"\u0004\bM\u0010ER\u001a\u0010N\u001a\u00020\u001bX.¢\u0006\u000e\n\u0000\u001a\u0004\bO\u0010P\"\u0004\bQ\u0010RR\u001e\u0010S\u001a\u00020\u001d2\u0006\u0010\u001c\u001a\u00020\u001d@BX\u000e¢\u0006\b\n\u0000\"\u0004\bT\u0010\"R\u001e\u0010U\u001a\u00060'R\u00020\u0000X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bV\u0010+\"\u0004\bW\u0010-R$\u0010X\u001a\u00020A8\u0006@\u0006X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\bY\u0010)\u001a\u0004\bZ\u0010C\"\u0004\b[\u0010ER\u0010\u0010\\\u001a\u00020]X\u0004¢\u0006\u0004\n\u0002\u0010^R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R$\u0010_\u001a\u00020A2\u0006\u0010\u001c\u001a\u00020A@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b`\u0010C\"\u0004\ba\u0010ER\u001c\u0010b\u001a\u00020c8\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\bd\u0010)\u001a\u0004\be\u0010fR\u000e\u0010g\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R\u001e\u0010h\u001a\u00020A2\u0006\u0010\u001c\u001a\u00020A@BX\u000e¢\u0006\b\n\u0000\"\u0004\bi\u0010ER\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/NotificationShadeDepthController;", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionListener;", "Lcom/android/systemui/Dumpable;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "blurUtils", "Lcom/android/systemui/statusbar/BlurUtils;", "biometricUnlockController", "Lcom/android/systemui/statusbar/phone/BiometricUnlockController;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "choreographer", "Landroid/view/Choreographer;", "wallpaperController", "Lcom/android/systemui/util/WallpaperController;", "notificationShadeWindowController", "Lcom/android/systemui/statusbar/NotificationShadeWindowController;", "dozeParameters", "Lcom/android/systemui/statusbar/phone/DozeParameters;", "context", "Landroid/content/Context;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "(Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/statusbar/BlurUtils;Lcom/android/systemui/statusbar/phone/BiometricUnlockController;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Landroid/view/Choreographer;Lcom/android/systemui/util/WallpaperController;Lcom/android/systemui/statusbar/NotificationShadeWindowController;Lcom/android/systemui/statusbar/phone/DozeParameters;Landroid/content/Context;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/statusbar/policy/ConfigurationController;)V", "blurRoot", "Landroid/view/View;", "value", "", "blursDisabledForAppLaunch", "getBlursDisabledForAppLaunch", "()Z", "setBlursDisabledForAppLaunch", "(Z)V", "blursDisabledForUnlock", "getBlursDisabledForUnlock", "setBlursDisabledForUnlock", "brightnessMirrorSpring", "Lcom/android/systemui/statusbar/NotificationShadeDepthController$DepthAnimation;", "getBrightnessMirrorSpring$annotations", "()V", "getBrightnessMirrorSpring", "()Lcom/android/systemui/statusbar/NotificationShadeDepthController$DepthAnimation;", "setBrightnessMirrorSpring", "(Lcom/android/systemui/statusbar/NotificationShadeDepthController$DepthAnimation;)V", "brightnessMirrorVisible", "getBrightnessMirrorVisible", "setBrightnessMirrorVisible", "inSplitShade", "isBlurred", "isClosed", "isOpen", "keyguardAnimator", "Landroid/animation/Animator;", "keyguardStateCallback", "com/android/systemui/statusbar/NotificationShadeDepthController$keyguardStateCallback$1", "Lcom/android/systemui/statusbar/NotificationShadeDepthController$keyguardStateCallback$1;", "lastAppliedBlur", "", "listeners", "", "Lcom/android/systemui/statusbar/NotificationShadeDepthController$DepthListener;", "notificationAnimator", "panelPullDownMinFraction", "", "getPanelPullDownMinFraction", "()F", "setPanelPullDownMinFraction", "(F)V", "prevShadeDirection", "prevShadeVelocity", "prevTimestamp", "", "prevTracking", "qsPanelExpansion", "getQsPanelExpansion", "setQsPanelExpansion", "root", "getRoot", "()Landroid/view/View;", "setRoot", "(Landroid/view/View;)V", "scrimsVisible", "setScrimsVisible", "shadeAnimation", "getShadeAnimation", "setShadeAnimation", "shadeExpansion", "getShadeExpansion$annotations", "getShadeExpansion", "setShadeExpansion", "statusBarStateCallback", "com/android/systemui/statusbar/NotificationShadeDepthController$statusBarStateCallback$1", "Lcom/android/systemui/statusbar/NotificationShadeDepthController$statusBarStateCallback$1;", "transitionToFullShadeProgress", "getTransitionToFullShadeProgress", "setTransitionToFullShadeProgress", "updateBlurCallback", "Landroid/view/Choreographer$FrameCallback;", "getUpdateBlurCallback$annotations", "getUpdateBlurCallback", "()Landroid/view/Choreographer$FrameCallback;", "updateScheduled", "wakeAndUnlockBlurRadius", "setWakeAndUnlockBlurRadius", "addListener", "", "listener", "animateBlur", "blur", "velocity", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "onPanelExpansionChanged", "event", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionChangeEvent;", "removeListener", "scheduleUpdate", "viewToBlur", "shouldApplyShadeBlur", "updateResources", "updateShadeAnimationBlur", "expansion", "tracking", "direction", "Companion", "DepthAnimation", "DepthListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationShadeDepthController.kt */
public final class NotificationShadeDepthController implements PanelExpansionListener, Dumpable {
    private static final float ANIMATION_BLUR_FRACTION = 0.19999999f;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final float INTERACTION_BLUR_FRACTION = 0.8f;
    private static final float MAX_VELOCITY = 3000.0f;
    private static final float MIN_VELOCITY = -3000.0f;
    private static final String TAG = "DepthController";
    private static final float VELOCITY_SCALE = 100.0f;
    private static final boolean WAKE_UP_ANIMATION_ENABLED = true;
    /* access modifiers changed from: private */
    public final BiometricUnlockController biometricUnlockController;
    private View blurRoot;
    /* access modifiers changed from: private */
    public final BlurUtils blurUtils;
    private boolean blursDisabledForAppLaunch;
    private boolean blursDisabledForUnlock;
    private DepthAnimation brightnessMirrorSpring = new DepthAnimation();
    private boolean brightnessMirrorVisible;
    private final Choreographer choreographer;
    private final Context context;
    /* access modifiers changed from: private */
    public final DozeParameters dozeParameters;
    private boolean inSplitShade;
    private boolean isBlurred;
    private boolean isClosed = true;
    private boolean isOpen;
    /* access modifiers changed from: private */
    public Animator keyguardAnimator;
    private final NotificationShadeDepthController$keyguardStateCallback$1 keyguardStateCallback;
    /* access modifiers changed from: private */
    public final KeyguardStateController keyguardStateController;
    private int lastAppliedBlur;
    private List<DepthListener> listeners = new ArrayList();
    /* access modifiers changed from: private */
    public Animator notificationAnimator;
    private final NotificationShadeWindowController notificationShadeWindowController;
    private float panelPullDownMinFraction;
    /* access modifiers changed from: private */
    public int prevShadeDirection;
    /* access modifiers changed from: private */
    public float prevShadeVelocity;
    private long prevTimestamp = -1;
    /* access modifiers changed from: private */
    public boolean prevTracking;
    private float qsPanelExpansion;
    public View root;
    private boolean scrimsVisible;
    private DepthAnimation shadeAnimation = new DepthAnimation();
    private float shadeExpansion;
    private final NotificationShadeDepthController$statusBarStateCallback$1 statusBarStateCallback;
    private final StatusBarStateController statusBarStateController;
    private float transitionToFullShadeProgress;
    private final Choreographer.FrameCallback updateBlurCallback = new NotificationShadeDepthController$$ExternalSyntheticLambda0(this);
    private boolean updateScheduled;
    private float wakeAndUnlockBlurRadius;
    private final WallpaperController wallpaperController;

    @Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\tÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/NotificationShadeDepthController$DepthListener;", "", "onBlurRadiusChanged", "", "blurRadius", "", "onWallpaperZoomOutChanged", "zoomOut", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotificationShadeDepthController.kt */
    public interface DepthListener {
        @JvmDefault
        void onBlurRadiusChanged(int i) {
        }

        void onWallpaperZoomOutChanged(float f);
    }

    public static /* synthetic */ void getBrightnessMirrorSpring$annotations() {
    }

    public static /* synthetic */ void getShadeExpansion$annotations() {
    }

    public static /* synthetic */ void getUpdateBlurCallback$annotations() {
    }

    @Inject
    public NotificationShadeDepthController(StatusBarStateController statusBarStateController2, BlurUtils blurUtils2, BiometricUnlockController biometricUnlockController2, KeyguardStateController keyguardStateController2, Choreographer choreographer2, WallpaperController wallpaperController2, NotificationShadeWindowController notificationShadeWindowController2, DozeParameters dozeParameters2, Context context2, DumpManager dumpManager, ConfigurationController configurationController) {
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        Intrinsics.checkNotNullParameter(blurUtils2, "blurUtils");
        Intrinsics.checkNotNullParameter(biometricUnlockController2, "biometricUnlockController");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(choreographer2, "choreographer");
        Intrinsics.checkNotNullParameter(wallpaperController2, "wallpaperController");
        Intrinsics.checkNotNullParameter(notificationShadeWindowController2, "notificationShadeWindowController");
        Intrinsics.checkNotNullParameter(dozeParameters2, "dozeParameters");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        this.statusBarStateController = statusBarStateController2;
        this.blurUtils = blurUtils2;
        this.biometricUnlockController = biometricUnlockController2;
        this.keyguardStateController = keyguardStateController2;
        this.choreographer = choreographer2;
        this.wallpaperController = wallpaperController2;
        this.notificationShadeWindowController = notificationShadeWindowController2;
        this.dozeParameters = dozeParameters2;
        this.context = context2;
        NotificationShadeDepthController$keyguardStateCallback$1 notificationShadeDepthController$keyguardStateCallback$1 = new NotificationShadeDepthController$keyguardStateCallback$1(this);
        this.keyguardStateCallback = notificationShadeDepthController$keyguardStateCallback$1;
        NotificationShadeDepthController$statusBarStateCallback$1 notificationShadeDepthController$statusBarStateCallback$1 = new NotificationShadeDepthController$statusBarStateCallback$1(this);
        this.statusBarStateCallback = notificationShadeDepthController$statusBarStateCallback$1;
        String name = getClass().getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
        keyguardStateController2.addCallback(notificationShadeDepthController$keyguardStateCallback$1);
        statusBarStateController2.addCallback(notificationShadeDepthController$statusBarStateCallback$1);
        notificationShadeWindowController2.setScrimsVisibilityListener(new NotificationShadeDepthController$$ExternalSyntheticLambda1(this));
        this.shadeAnimation.setStiffness(200.0f);
        this.shadeAnimation.setDampingRatio(1.0f);
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ NotificationShadeDepthController this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(Configuration configuration) {
                this.this$0.updateResources();
            }
        });
    }

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tXT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fXT¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/statusbar/NotificationShadeDepthController$Companion;", "", "()V", "ANIMATION_BLUR_FRACTION", "", "INTERACTION_BLUR_FRACTION", "MAX_VELOCITY", "MIN_VELOCITY", "TAG", "", "VELOCITY_SCALE", "WAKE_UP_ANIMATION_ENABLED", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotificationShadeDepthController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final View getRoot() {
        View view = this.root;
        if (view != null) {
            return view;
        }
        Intrinsics.throwUninitializedPropertyAccessException("root");
        return null;
    }

    public final void setRoot(View view) {
        Intrinsics.checkNotNullParameter(view, "<set-?>");
        this.root = view;
    }

    public final float getShadeExpansion() {
        return this.shadeExpansion;
    }

    public final void setShadeExpansion(float f) {
        this.shadeExpansion = f;
    }

    public final float getPanelPullDownMinFraction() {
        return this.panelPullDownMinFraction;
    }

    public final void setPanelPullDownMinFraction(float f) {
        this.panelPullDownMinFraction = f;
    }

    public final DepthAnimation getShadeAnimation() {
        return this.shadeAnimation;
    }

    public final void setShadeAnimation(DepthAnimation depthAnimation) {
        Intrinsics.checkNotNullParameter(depthAnimation, "<set-?>");
        this.shadeAnimation = depthAnimation;
    }

    public final DepthAnimation getBrightnessMirrorSpring() {
        return this.brightnessMirrorSpring;
    }

    public final void setBrightnessMirrorSpring(DepthAnimation depthAnimation) {
        Intrinsics.checkNotNullParameter(depthAnimation, "<set-?>");
        this.brightnessMirrorSpring = depthAnimation;
    }

    public final boolean getBrightnessMirrorVisible() {
        return this.brightnessMirrorVisible;
    }

    public final void setBrightnessMirrorVisible(boolean z) {
        this.brightnessMirrorVisible = z;
        DepthAnimation.animateTo$default(this.brightnessMirrorSpring, z ? (int) this.blurUtils.blurRadiusOfRatio(1.0f) : 0, (View) null, 2, (Object) null);
    }

    public final float getQsPanelExpansion() {
        return this.qsPanelExpansion;
    }

    public final void setQsPanelExpansion(float f) {
        if (Float.isNaN(f)) {
            Log.w(TAG, "Invalid qs expansion");
            return;
        }
        if (!(this.qsPanelExpansion == f)) {
            this.qsPanelExpansion = f;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
        }
    }

    public final float getTransitionToFullShadeProgress() {
        return this.transitionToFullShadeProgress;
    }

    public final void setTransitionToFullShadeProgress(float f) {
        if (!(this.transitionToFullShadeProgress == f)) {
            this.transitionToFullShadeProgress = f;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
        }
    }

    public final boolean getBlursDisabledForAppLaunch() {
        return this.blursDisabledForAppLaunch;
    }

    public final void setBlursDisabledForAppLaunch(boolean z) {
        if (this.blursDisabledForAppLaunch != z) {
            this.blursDisabledForAppLaunch = z;
            boolean z2 = true;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
            if (this.shadeExpansion == 0.0f) {
                if (this.shadeAnimation.getRadius() != 0.0f) {
                    z2 = false;
                }
                if (z2) {
                    return;
                }
            }
            if (z) {
                DepthAnimation.animateTo$default(this.shadeAnimation, 0, (View) null, 2, (Object) null);
                this.shadeAnimation.finishIfRunning();
            }
        }
    }

    public final boolean getBlursDisabledForUnlock() {
        return this.blursDisabledForUnlock;
    }

    public final void setBlursDisabledForUnlock(boolean z) {
        if (this.blursDisabledForUnlock != z) {
            this.blursDisabledForUnlock = z;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
        }
    }

    private final void setScrimsVisible(boolean z) {
        if (this.scrimsVisible != z) {
            this.scrimsVisible = z;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
        }
    }

    /* access modifiers changed from: private */
    public final void setWakeAndUnlockBlurRadius(float f) {
        if (!(this.wakeAndUnlockBlurRadius == f)) {
            this.wakeAndUnlockBlurRadius = f;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
        }
    }

    public final Choreographer.FrameCallback getUpdateBlurCallback() {
        return this.updateBlurCallback;
    }

    /* access modifiers changed from: private */
    /* renamed from: updateBlurCallback$lambda-1  reason: not valid java name */
    public static final void m3042updateBlurCallback$lambda1(NotificationShadeDepthController notificationShadeDepthController, long j) {
        ViewRootImpl viewRootImpl;
        Intrinsics.checkNotNullParameter(notificationShadeDepthController, "this$0");
        boolean z = false;
        notificationShadeDepthController.updateScheduled = false;
        float f = 0.0f;
        float max = Math.max(Math.max(Math.max((notificationShadeDepthController.blurUtils.blurRadiusOfRatio(ShadeInterpolation.getNotificationScrimAlpha(notificationShadeDepthController.shouldApplyShadeBlur() ? notificationShadeDepthController.shadeExpansion : 0.0f)) * 0.8f) + (MathUtils.constrain(notificationShadeDepthController.shadeAnimation.getRadius(), (float) notificationShadeDepthController.blurUtils.getMinBlurRadius(), (float) notificationShadeDepthController.blurUtils.getMaxBlurRadius()) * ANIMATION_BLUR_FRACTION), notificationShadeDepthController.blurUtils.blurRadiusOfRatio(ShadeInterpolation.getNotificationScrimAlpha(notificationShadeDepthController.qsPanelExpansion) * notificationShadeDepthController.shadeExpansion)), notificationShadeDepthController.blurUtils.blurRadiusOfRatio(notificationShadeDepthController.transitionToFullShadeProgress)), notificationShadeDepthController.wakeAndUnlockBlurRadius);
        if (notificationShadeDepthController.blursDisabledForAppLaunch || notificationShadeDepthController.blursDisabledForUnlock) {
            max = 0.0f;
        }
        float saturate = MathUtils.saturate(notificationShadeDepthController.blurUtils.ratioOfBlurRadius(max));
        int i = (int) max;
        if (notificationShadeDepthController.inSplitShade) {
            saturate = 0.0f;
        }
        if (notificationShadeDepthController.scrimsVisible) {
            i = 0;
        } else {
            f = saturate;
        }
        if (!notificationShadeDepthController.blurUtils.supportsBlursOnWindows()) {
            i = 0;
        }
        int ratio = (int) (((float) i) * (1.0f - notificationShadeDepthController.brightnessMirrorSpring.getRatio()));
        if (notificationShadeDepthController.scrimsVisible && !notificationShadeDepthController.blursDisabledForAppLaunch) {
            z = true;
        }
        Trace.traceCounter(4096, "shade_blur_radius", ratio);
        BlurUtils blurUtils2 = notificationShadeDepthController.blurUtils;
        View view = notificationShadeDepthController.blurRoot;
        if (view == null || (viewRootImpl = view.getViewRootImpl()) == null) {
            viewRootImpl = notificationShadeDepthController.getRoot().getViewRootImpl();
        }
        blurUtils2.applyBlur(viewRootImpl, ratio, z);
        notificationShadeDepthController.lastAppliedBlur = ratio;
        for (DepthListener depthListener : notificationShadeDepthController.listeners) {
            depthListener.onWallpaperZoomOutChanged(f);
            depthListener.onBlurRadiusChanged(ratio);
        }
        notificationShadeDepthController.notificationShadeWindowController.setBackgroundBlurRadius(ratio);
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-2  reason: not valid java name */
    public static final void m3041_init_$lambda2(NotificationShadeDepthController notificationShadeDepthController, Integer num) {
        Intrinsics.checkNotNullParameter(notificationShadeDepthController, "this$0");
        notificationShadeDepthController.setScrimsVisible(num != null && num.intValue() == 2);
    }

    /* access modifiers changed from: private */
    public final void updateResources() {
        Resources resources = this.context.getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "context.resources");
        this.inSplitShade = LargeScreenUtils.shouldUseSplitNotificationShade(resources);
    }

    public final void addListener(DepthListener depthListener) {
        Intrinsics.checkNotNullParameter(depthListener, "listener");
        this.listeners.add(depthListener);
    }

    public final void removeListener(DepthListener depthListener) {
        Intrinsics.checkNotNullParameter(depthListener, "listener");
        this.listeners.remove((Object) depthListener);
    }

    public void onPanelExpansionChanged(PanelExpansionChangeEvent panelExpansionChangeEvent) {
        Intrinsics.checkNotNullParameter(panelExpansionChangeEvent, NotificationCompat.CATEGORY_EVENT);
        float fraction = panelExpansionChangeEvent.getFraction();
        boolean tracking = panelExpansionChangeEvent.getTracking();
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        float f = this.panelPullDownMinFraction;
        float f2 = 1.0f;
        float saturate = MathUtils.saturate((fraction - f) / (1.0f - f));
        if (!(this.shadeExpansion == saturate) || this.prevTracking != tracking) {
            long j = this.prevTimestamp;
            if (j < 0) {
                this.prevTimestamp = elapsedRealtimeNanos;
            } else {
                f2 = MathUtils.constrain((float) (((double) (elapsedRealtimeNanos - j)) / 1.0E9d), 1.0E-5f, 1.0f);
            }
            float f3 = saturate - this.shadeExpansion;
            int signum = (int) Math.signum(f3);
            float constrain = MathUtils.constrain((f3 * VELOCITY_SCALE) / f2, MIN_VELOCITY, MAX_VELOCITY);
            updateShadeAnimationBlur(saturate, tracking, constrain, signum);
            this.prevShadeDirection = signum;
            this.prevShadeVelocity = constrain;
            this.shadeExpansion = saturate;
            this.prevTracking = tracking;
            this.prevTimestamp = elapsedRealtimeNanos;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
            return;
        }
        this.prevTimestamp = elapsedRealtimeNanos;
    }

    /* access modifiers changed from: private */
    public final void updateShadeAnimationBlur(float f, boolean z, float f2, int i) {
        if (!shouldApplyShadeBlur()) {
            animateBlur(false, 0.0f);
            this.isClosed = true;
            this.isOpen = false;
        } else if (f > 0.0f) {
            if (this.isClosed) {
                animateBlur(true, f2);
                this.isClosed = false;
            }
            if (z && !this.isBlurred) {
                animateBlur(true, 0.0f);
            }
            if (!z && i < 0 && this.isBlurred) {
                animateBlur(false, f2);
            }
            if (!(f == 1.0f)) {
                this.isOpen = false;
            } else if (!this.isOpen) {
                this.isOpen = true;
                if (!this.isBlurred) {
                    animateBlur(true, f2);
                }
            }
        } else if (!this.isClosed) {
            this.isClosed = true;
            if (this.isBlurred) {
                animateBlur(false, f2);
            }
        }
    }

    private final void animateBlur(boolean z, float f) {
        this.isBlurred = z;
        float f2 = (!z || !shouldApplyShadeBlur()) ? 0.0f : 1.0f;
        this.shadeAnimation.setStartVelocity(f);
        DepthAnimation.animateTo$default(this.shadeAnimation, (int) this.blurUtils.blurRadiusOfRatio(f2), (View) null, 2, (Object) null);
    }

    static /* synthetic */ void scheduleUpdate$default(NotificationShadeDepthController notificationShadeDepthController, View view, int i, Object obj) {
        if ((i & 1) != 0) {
            view = null;
        }
        notificationShadeDepthController.scheduleUpdate(view);
    }

    /* access modifiers changed from: private */
    public final void scheduleUpdate(View view) {
        if (!this.updateScheduled) {
            this.updateScheduled = true;
            this.blurRoot = view;
            this.choreographer.postFrameCallback(this.updateBlurCallback);
        }
    }

    private final boolean shouldApplyShadeBlur() {
        int state = this.statusBarStateController.getState();
        return (state == 0 || state == 2) && !this.keyguardStateController.isKeyguardFadingAway();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("StatusBarWindowBlurController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("shadeExpansion: " + this.shadeExpansion);
        indentingPrintWriter.println("shouldApplyShadeBlur: " + shouldApplyShadeBlur());
        indentingPrintWriter.println("shadeAnimation: " + this.shadeAnimation.getRadius());
        indentingPrintWriter.println("brightnessMirrorRadius: " + this.brightnessMirrorSpring.getRadius());
        indentingPrintWriter.println("wakeAndUnlockBlur: " + this.wakeAndUnlockBlurRadius);
        indentingPrintWriter.println("blursDisabledForAppLaunch: " + this.blursDisabledForAppLaunch);
        indentingPrintWriter.println("qsPanelExpansion: " + this.qsPanelExpansion);
        indentingPrintWriter.println("transitionToFullShadeProgress: " + this.transitionToFullShadeProgress);
        indentingPrintWriter.println("lastAppliedBlur: " + this.lastAppliedBlur);
    }

    @Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\n\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001a\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00042\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0010J\u0006\u0010\u0015\u001a\u00020\u0012J\u000e\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0006J\u000e\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u0006J\u000e\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\f\u0010\bR\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001c"}, mo65043d2 = {"Lcom/android/systemui/statusbar/NotificationShadeDepthController$DepthAnimation;", "", "(Lcom/android/systemui/statusbar/NotificationShadeDepthController;)V", "pendingRadius", "", "radius", "", "getRadius", "()F", "setRadius", "(F)V", "ratio", "getRatio", "springAnimation", "Landroidx/dynamicanimation/animation/SpringAnimation;", "view", "Landroid/view/View;", "animateTo", "", "newRadius", "viewToBlur", "finishIfRunning", "setDampingRatio", "dampingRation", "setStartVelocity", "velocity", "setStiffness", "stiffness", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotificationShadeDepthController.kt */
    public final class DepthAnimation {
        private int pendingRadius = -1;
        private float radius;
        private SpringAnimation springAnimation;
        /* access modifiers changed from: private */
        public View view;

        public DepthAnimation() {
            SpringAnimation springAnimation2 = new SpringAnimation(this, new C2588x870e2248(this, NotificationShadeDepthController.this));
            this.springAnimation = springAnimation2;
            springAnimation2.setSpring(new SpringForce(0.0f));
            this.springAnimation.getSpring().setDampingRatio(1.0f);
            this.springAnimation.getSpring().setStiffness(10000.0f);
            this.springAnimation.addEndListener(new C2586xc13cfbf7(this));
        }

        public final float getRadius() {
            return this.radius;
        }

        public final void setRadius(float f) {
            this.radius = f;
        }

        public final float getRatio() {
            return NotificationShadeDepthController.this.blurUtils.ratioOfBlurRadius(this.radius);
        }

        /* access modifiers changed from: private */
        /* renamed from: _init_$lambda-0  reason: not valid java name */
        public static final void m3043_init_$lambda0(DepthAnimation depthAnimation, DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            Intrinsics.checkNotNullParameter(depthAnimation, "this$0");
            depthAnimation.pendingRadius = -1;
        }

        public static /* synthetic */ void animateTo$default(DepthAnimation depthAnimation, int i, View view2, int i2, Object obj) {
            if ((i2 & 2) != 0) {
                view2 = null;
            }
            depthAnimation.animateTo(i, view2);
        }

        public final void animateTo(int i, View view2) {
            if (this.pendingRadius != i || !Intrinsics.areEqual((Object) this.view, (Object) view2)) {
                this.view = view2;
                this.pendingRadius = i;
                this.springAnimation.animateToFinalPosition((float) i);
            }
        }

        public final void finishIfRunning() {
            if (this.springAnimation.isRunning()) {
                this.springAnimation.skipToEnd();
            }
        }

        public final void setStiffness(float f) {
            this.springAnimation.getSpring().setStiffness(f);
        }

        public final void setDampingRatio(float f) {
            this.springAnimation.getSpring().setDampingRatio(f);
        }

        public final void setStartVelocity(float f) {
            this.springAnimation.setStartVelocity(f);
        }
    }
}
