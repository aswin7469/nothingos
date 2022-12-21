package com.android.systemui.biometrics;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.biometrics.SensorLocationInternal;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.ISidefpsController;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.os.Handler;
import android.util.Log;
import android.util.RotationUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.android.systemui.C1893R;
import com.android.systemui.biometrics.BiometricDisplayListener;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001BU\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0001\u0010\u0010\u001a\u00020\u0011\u0012\b\b\u0001\u0010\u0012\u001a\u00020\u0013¢\u0006\u0002\u0010\u0014J\b\u00107\u001a\u000208H\u0002J\b\u00109\u001a\u000208H\u0002J\u001d\u0010:\u001a\u0002082\u0006\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020>H\u0001¢\u0006\u0002\b?J\u0010\u0010@\u001a\u0002082\u0006\u0010A\u001a\u00020'H\u0002R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0017\u001a\u00020\u00188\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u001b\u0010\u001cR\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u000e¢\u0006\u0002\n\u0000R$\u0010\u001f\u001a\u00020 8\u0000@\u0000X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b!\u0010\u001a\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\"\u0010(\u001a\u0004\u0018\u00010'2\b\u0010&\u001a\u0004\u0018\u00010'@BX\u000e¢\u0006\b\n\u0000\"\u0004\b)\u0010*R\u000e\u0010+\u001a\u00020,X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010-\u001a\u00020.8\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b/\u0010\u001a\u001a\u0004\b0\u00101R\u001c\u00102\u001a\u0002038\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b4\u0010\u001a\u001a\u0004\b5\u00106R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006B"}, mo64987d2 = {"Lcom/android/systemui/biometrics/SidefpsController;", "", "context", "Landroid/content/Context;", "layoutInflater", "Landroid/view/LayoutInflater;", "fingerprintManager", "Landroid/hardware/fingerprint/FingerprintManager;", "windowManager", "Landroid/view/WindowManager;", "activityTaskManager", "Landroid/app/ActivityTaskManager;", "overviewProxyService", "Lcom/android/systemui/recents/OverviewProxyService;", "displayManager", "Landroid/hardware/display/DisplayManager;", "mainExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "handler", "Landroid/os/Handler;", "(Landroid/content/Context;Landroid/view/LayoutInflater;Landroid/hardware/fingerprint/FingerprintManager;Landroid/view/WindowManager;Landroid/app/ActivityTaskManager;Lcom/android/systemui/recents/OverviewProxyService;Landroid/hardware/display/DisplayManager;Lcom/android/systemui/util/concurrency/DelayableExecutor;Landroid/os/Handler;)V", "animationDuration", "", "orientationListener", "Lcom/android/systemui/biometrics/BiometricDisplayListener;", "getOrientationListener$annotations", "()V", "getOrientationListener", "()Lcom/android/systemui/biometrics/BiometricDisplayListener;", "overlayHideAnimator", "Landroid/view/ViewPropertyAnimator;", "overlayOffsets", "Landroid/hardware/biometrics/SensorLocationInternal;", "getOverlayOffsets$SystemUI_nothingRelease$annotations", "getOverlayOffsets$SystemUI_nothingRelease", "()Landroid/hardware/biometrics/SensorLocationInternal;", "setOverlayOffsets$SystemUI_nothingRelease", "(Landroid/hardware/biometrics/SensorLocationInternal;)V", "value", "Landroid/view/View;", "overlayView", "setOverlayView", "(Landroid/view/View;)V", "overlayViewParams", "Landroid/view/WindowManager$LayoutParams;", "overviewProxyListener", "Lcom/android/systemui/recents/OverviewProxyService$OverviewProxyListener;", "getOverviewProxyListener$annotations", "getOverviewProxyListener", "()Lcom/android/systemui/recents/OverviewProxyService$OverviewProxyListener;", "sensorProps", "Landroid/hardware/fingerprint/FingerprintSensorPropertiesInternal;", "getSensorProps$annotations", "getSensorProps", "()Landroid/hardware/fingerprint/FingerprintSensorPropertiesInternal;", "createOverlayForDisplay", "", "onOrientationChanged", "updateOverlayParams", "display", "Landroid/view/Display;", "bounds", "Landroid/graphics/Rect;", "updateOverlayParams$SystemUI_nothingRelease", "updateOverlayVisibility", "view", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SidefpsController.kt */
public final class SidefpsController {
    /* access modifiers changed from: private */
    public final ActivityTaskManager activityTaskManager;
    private final long animationDuration;
    private final Context context;
    /* access modifiers changed from: private */
    public final Handler handler;
    private final LayoutInflater layoutInflater;
    private final BiometricDisplayListener orientationListener;
    /* access modifiers changed from: private */
    public ViewPropertyAnimator overlayHideAnimator;
    private SensorLocationInternal overlayOffsets;
    /* access modifiers changed from: private */
    public View overlayView;
    private final WindowManager.LayoutParams overlayViewParams;
    private final OverviewProxyService.OverviewProxyListener overviewProxyListener;
    private final FingerprintSensorPropertiesInternal sensorProps;
    private final WindowManager windowManager;

    public static /* synthetic */ void getOrientationListener$annotations() {
    }

    public static /* synthetic */ void getOverlayOffsets$SystemUI_nothingRelease$annotations() {
    }

    public static /* synthetic */ void getOverviewProxyListener$annotations() {
    }

    public static /* synthetic */ void getSensorProps$annotations() {
    }

    @Inject
    public SidefpsController(Context context2, LayoutInflater layoutInflater2, FingerprintManager fingerprintManager, WindowManager windowManager2, ActivityTaskManager activityTaskManager2, OverviewProxyService overviewProxyService, DisplayManager displayManager, @Main final DelayableExecutor delayableExecutor, @Main Handler handler2) {
        List sensorPropertiesInternal;
        Object obj;
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(layoutInflater2, "layoutInflater");
        Intrinsics.checkNotNullParameter(windowManager2, "windowManager");
        Intrinsics.checkNotNullParameter(activityTaskManager2, "activityTaskManager");
        Intrinsics.checkNotNullParameter(overviewProxyService, "overviewProxyService");
        Intrinsics.checkNotNullParameter(displayManager, "displayManager");
        Intrinsics.checkNotNullParameter(delayableExecutor, "mainExecutor");
        Intrinsics.checkNotNullParameter(handler2, "handler");
        this.context = context2;
        this.layoutInflater = layoutInflater2;
        this.windowManager = windowManager2;
        this.activityTaskManager = activityTaskManager2;
        this.handler = handler2;
        if (!(fingerprintManager == null || (sensorPropertiesInternal = fingerprintManager.getSensorPropertiesInternal()) == null)) {
            Iterator it = sensorPropertiesInternal.iterator();
            while (true) {
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                if (((FingerprintSensorPropertiesInternal) obj).isAnySidefpsType()) {
                    break;
                }
            }
            FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = (FingerprintSensorPropertiesInternal) obj;
            if (fingerprintSensorPropertiesInternal != null) {
                this.sensorProps = fingerprintSensorPropertiesInternal;
                this.orientationListener = new BiometricDisplayListener(this.context, displayManager, this.handler, new BiometricDisplayListener.SensorType.SideFingerprint(fingerprintSensorPropertiesInternal), new SidefpsController$orientationListener$1(this));
                OverviewProxyService.OverviewProxyListener sidefpsController$overviewProxyListener$1 = new SidefpsController$overviewProxyListener$1(this);
                this.overviewProxyListener = sidefpsController$overviewProxyListener$1;
                this.animationDuration = (long) this.context.getResources().getInteger(17694721);
                SensorLocationInternal sensorLocationInternal = SensorLocationInternal.DEFAULT;
                Intrinsics.checkNotNullExpressionValue(sensorLocationInternal, "DEFAULT");
                this.overlayOffsets = sensorLocationInternal;
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2010, Utils.FINGERPRINT_OVERLAY_LAYOUT_PARAM_FLAGS, -3);
                layoutParams.setTitle("SidefpsController");
                layoutParams.setFitInsetsTypes(0);
                layoutParams.gravity = 51;
                layoutParams.layoutInDisplayCutoutMode = 3;
                layoutParams.privateFlags = NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE;
                this.overlayViewParams = layoutParams;
                fingerprintManager.setSidefpsController(new ISidefpsController.Stub(this) {
                    final /* synthetic */ SidefpsController this$0;

                    {
                        this.this$0 = r1;
                    }

                    public void show(int i, int i2) {
                        if (SidefpsControllerKt.isReasonToShow(i2, this.this$0.activityTaskManager)) {
                            doShow();
                        } else {
                            hide(i);
                        }
                    }

                    private final void doShow() {
                        delayableExecutor.execute(new SidefpsController$1$$ExternalSyntheticLambda0(this.this$0));
                    }

                    /* access modifiers changed from: private */
                    /* renamed from: doShow$lambda-0  reason: not valid java name */
                    public static final void m2574doShow$lambda0(SidefpsController sidefpsController) {
                        Intrinsics.checkNotNullParameter(sidefpsController, "this$0");
                        if (sidefpsController.overlayView == null) {
                            sidefpsController.createOverlayForDisplay();
                        } else {
                            Log.v("SidefpsController", "overlay already shown");
                        }
                    }

                    /* access modifiers changed from: private */
                    /* renamed from: hide$lambda-1  reason: not valid java name */
                    public static final void m2575hide$lambda1(SidefpsController sidefpsController) {
                        Intrinsics.checkNotNullParameter(sidefpsController, "this$0");
                        sidefpsController.setOverlayView((View) null);
                    }

                    public void hide(int i) {
                        delayableExecutor.execute(new SidefpsController$1$$ExternalSyntheticLambda1(this.this$0));
                    }
                });
                overviewProxyService.addCallback(sidefpsController$overviewProxyListener$1);
                return;
            }
        }
        throw new IllegalStateException("no side fingerprint sensor");
    }

    public final FingerprintSensorPropertiesInternal getSensorProps() {
        return this.sensorProps;
    }

    public final BiometricDisplayListener getOrientationListener() {
        return this.orientationListener;
    }

    public final OverviewProxyService.OverviewProxyListener getOverviewProxyListener() {
        return this.overviewProxyListener;
    }

    /* access modifiers changed from: private */
    public final void setOverlayView(View view) {
        View view2 = this.overlayView;
        if (view2 != null) {
            this.windowManager.removeView(view2);
            this.orientationListener.disable();
        }
        ViewPropertyAnimator viewPropertyAnimator = this.overlayHideAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        this.overlayHideAnimator = null;
        this.overlayView = view;
        if (view != null) {
            this.windowManager.addView(view, this.overlayViewParams);
            updateOverlayVisibility(view);
            this.orientationListener.enable();
        }
    }

    public final SensorLocationInternal getOverlayOffsets$SystemUI_nothingRelease() {
        return this.overlayOffsets;
    }

    public final void setOverlayOffsets$SystemUI_nothingRelease(SensorLocationInternal sensorLocationInternal) {
        Intrinsics.checkNotNullParameter(sensorLocationInternal, "<set-?>");
        this.overlayOffsets = sensorLocationInternal;
    }

    /* access modifiers changed from: private */
    public final void onOrientationChanged() {
        if (this.overlayView != null) {
            createOverlayForDisplay();
        }
    }

    /* access modifiers changed from: private */
    public final void createOverlayForDisplay() {
        View inflate = this.layoutInflater.inflate(C1893R.layout.sidefps_view, (ViewGroup) null, false);
        setOverlayView(inflate);
        Display display = this.context.getDisplay();
        Intrinsics.checkNotNull(display);
        SensorLocationInternal location = this.sensorProps.getLocation(display.getUniqueId());
        if (location == null) {
            Log.w("SidefpsController", "No location specified for display: " + display.getUniqueId());
        }
        if (location == null) {
            location = this.sensorProps.getLocation();
        }
        Intrinsics.checkNotNullExpressionValue(location, "offsets");
        this.overlayOffsets = location;
        View findViewById = inflate.findViewById(C1893R.C1897id.sidefps_animation);
        if (findViewById != null) {
            LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById;
            inflate.setRotation(SidefpsControllerKt.asSideFpsAnimationRotation(display, SidefpsControllerKt.isYAligned(location)));
            lottieAnimationView.setAnimation(SidefpsControllerKt.asSideFpsAnimation(display, SidefpsControllerKt.isYAligned(location)));
            lottieAnimationView.addLottieOnCompositionLoadedListener(new SidefpsController$$ExternalSyntheticLambda0(this, inflate, display));
            SidefpsControllerKt.addOverlayDynamicColor(lottieAnimationView, this.context);
            inflate.setAccessibilityDelegate(new SidefpsController$createOverlayForDisplay$2());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.airbnb.lottie.LottieAnimationView");
    }

    /* access modifiers changed from: private */
    /* renamed from: createOverlayForDisplay$lambda-5  reason: not valid java name */
    public static final void m2572createOverlayForDisplay$lambda5(SidefpsController sidefpsController, View view, Display display, LottieComposition lottieComposition) {
        Intrinsics.checkNotNullParameter(sidefpsController, "this$0");
        Intrinsics.checkNotNullParameter(display, "$display");
        View view2 = sidefpsController.overlayView;
        if (view2 != null && Intrinsics.areEqual((Object) view2, (Object) view)) {
            Rect bounds = lottieComposition.getBounds();
            Intrinsics.checkNotNullExpressionValue(bounds, "it.bounds");
            sidefpsController.updateOverlayParams$SystemUI_nothingRelease(display, bounds);
        }
    }

    public final void updateOverlayParams$SystemUI_nothingRelease(Display display, Rect rect) {
        Rect rect2;
        Intrinsics.checkNotNullParameter(display, "display");
        Intrinsics.checkNotNullParameter(rect, "bounds");
        boolean access$isNaturalOrientation = SidefpsControllerKt.isNaturalOrientation(display);
        Rect bounds = this.windowManager.getMaximumWindowMetrics().getBounds();
        int width = access$isNaturalOrientation ? bounds.width() : bounds.height();
        int height = access$isNaturalOrientation ? bounds.height() : bounds.width();
        int width2 = access$isNaturalOrientation ? rect.width() : rect.height();
        int height2 = access$isNaturalOrientation ? rect.height() : rect.width();
        if (SidefpsControllerKt.isYAligned(this.overlayOffsets)) {
            rect2 = new Rect(width - width2, this.overlayOffsets.sensorLocationY, width, this.overlayOffsets.sensorLocationY + height2);
        } else {
            rect2 = new Rect(this.overlayOffsets.sensorLocationX, 0, this.overlayOffsets.sensorLocationX + width2, height2);
        }
        RotationUtils.rotateBounds(rect2, new Rect(0, 0, width, height), display.getRotation());
        this.overlayViewParams.x = rect2.left;
        this.overlayViewParams.y = rect2.top;
        this.windowManager.updateViewLayout(this.overlayView, this.overlayViewParams);
    }

    /* access modifiers changed from: private */
    public final void updateOverlayVisibility(View view) {
        if (Intrinsics.areEqual((Object) view, (Object) this.overlayView)) {
            Display display = this.context.getDisplay();
            Integer valueOf = display != null ? Integer.valueOf(display.getRotation()) : null;
            WindowInsets windowInsets = this.windowManager.getCurrentWindowMetrics().getWindowInsets();
            Intrinsics.checkNotNullExpressionValue(windowInsets, "windowManager.currentWindowMetrics.windowInsets");
            if (!SidefpsControllerKt.hasBigNavigationBar(windowInsets) || ((valueOf == null || valueOf.intValue() != 3 || !SidefpsControllerKt.isYAligned(this.overlayOffsets)) && (valueOf == null || valueOf.intValue() != 2 || SidefpsControllerKt.isYAligned(this.overlayOffsets)))) {
                ViewPropertyAnimator viewPropertyAnimator = this.overlayHideAnimator;
                if (viewPropertyAnimator != null) {
                    viewPropertyAnimator.cancel();
                }
                this.overlayHideAnimator = null;
                view.setAlpha(1.0f);
                view.setVisibility(0);
                return;
            }
            this.overlayHideAnimator = view.animate().alpha(0.0f).setStartDelay(3000).setDuration(this.animationDuration).setListener(new SidefpsController$updateOverlayVisibility$1(view, this));
        }
    }
}
