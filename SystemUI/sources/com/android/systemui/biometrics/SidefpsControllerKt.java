package com.android.systemui.biometrics;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.hardware.biometrics.SensorLocationInternal;
import android.view.Display;
import android.view.WindowInsets;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000D\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0014\u0010\u0002\u001a\u00020\u0003*\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002\u001a\u0014\u0010\u0007\u001a\u00020\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0003\u001a\u0014\u0010\f\u001a\u00020\r*\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002\u001a\f\u0010\u000e\u001a\u00020\u000b*\u00020\u000fH\u0002\u001a\f\u0010\u0010\u001a\u00020\u000b*\u00020\tH\u0002\u001a\u0014\u0010\u0011\u001a\u00020\u000b*\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0013H\u0002\u001a\f\u0010\u0014\u001a\u00020\u000b*\u00020\u0015H\u0002\u001a\f\u0010\u0016\u001a\u00020\u0001*\u00020\u0013H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u0017"}, mo64987d2 = {"TAG", "", "addOverlayDynamicColor", "", "Lcom/airbnb/lottie/LottieAnimationView;", "context", "Landroid/content/Context;", "asSideFpsAnimation", "", "Landroid/view/Display;", "yAligned", "", "asSideFpsAnimationRotation", "", "hasBigNavigationBar", "Landroid/view/WindowInsets;", "isNaturalOrientation", "isReasonToShow", "activityTaskManager", "Landroid/app/ActivityTaskManager;", "isYAligned", "Landroid/hardware/biometrics/SensorLocationInternal;", "topClass", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SidefpsController.kt */
public final class SidefpsControllerKt {
    private static final String TAG = "SidefpsController";

    /* access modifiers changed from: private */
    public static final boolean isReasonToShow(int i, ActivityTaskManager activityTaskManager) {
        if (i != 4) {
            return i != 6 || !Intrinsics.areEqual((Object) topClass(activityTaskManager), (Object) "com.android.settings.biometrics.fingerprint.FingerprintSettings");
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0012, code lost:
        r1 = r1.topActivity;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final java.lang.String topClass(android.app.ActivityTaskManager r1) {
        /*
            r0 = 1
            java.util.List r1 = r1.getTasks(r0)
            java.lang.String r0 = "getTasks(1)"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r0)
            java.lang.Object r1 = kotlin.collections.CollectionsKt.firstOrNull(r1)
            android.app.ActivityManager$RunningTaskInfo r1 = (android.app.ActivityManager.RunningTaskInfo) r1
            if (r1 == 0) goto L_0x001b
            android.content.ComponentName r1 = r1.topActivity
            if (r1 == 0) goto L_0x001b
            java.lang.String r1 = r1.getClassName()
            goto L_0x001c
        L_0x001b:
            r1 = 0
        L_0x001c:
            if (r1 != 0) goto L_0x0020
            java.lang.String r1 = ""
        L_0x0020:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.SidefpsControllerKt.topClass(android.app.ActivityTaskManager):java.lang.String");
    }

    /* access modifiers changed from: private */
    public static final int asSideFpsAnimation(Display display, boolean z) {
        int rotation = display.getRotation();
        if (rotation != 0) {
            if (rotation != 2) {
                if (!z) {
                    return C1893R.raw.sfps_pulse;
                }
            } else if (z) {
                return C1893R.raw.sfps_pulse;
            }
        } else if (z) {
            return C1893R.raw.sfps_pulse;
        }
        return C1893R.raw.sfps_pulse_landscape;
    }

    /* access modifiers changed from: private */
    public static final float asSideFpsAnimationRotation(Display display, boolean z) {
        int rotation = display.getRotation();
        if (rotation != 1) {
            if (rotation != 2 && (rotation != 3 || !z)) {
                return 0.0f;
            }
        } else if (z) {
            return 0.0f;
        }
        return 180.0f;
    }

    /* access modifiers changed from: private */
    public static final boolean isYAligned(SensorLocationInternal sensorLocationInternal) {
        return sensorLocationInternal.sensorLocationY != 0;
    }

    /* access modifiers changed from: private */
    public static final boolean isNaturalOrientation(Display display) {
        return display.getRotation() == 0 || display.getRotation() == 2;
    }

    /* access modifiers changed from: private */
    public static final boolean hasBigNavigationBar(WindowInsets windowInsets) {
        return windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom >= 70;
    }

    private static final void addOverlayDynamicColor$update(Context context, LottieAnimationView lottieAnimationView) {
        int color = context.getColor(C1893R.C1894color.biometric_dialog_accent);
        for (String str : CollectionsKt.listOf(".blue600", ".blue400")) {
            lottieAnimationView.addValueCallback(new KeyPath(str, "**"), LottieProperty.COLOR_FILTER, new SidefpsControllerKt$$ExternalSyntheticLambda0(color));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: addOverlayDynamicColor$update$lambda-0  reason: not valid java name */
    public static final ColorFilter m2578addOverlayDynamicColor$update$lambda0(int i, LottieFrameInfo lottieFrameInfo) {
        return new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_ATOP);
    }

    /* access modifiers changed from: private */
    public static final void addOverlayDynamicColor(LottieAnimationView lottieAnimationView, Context context) {
        if (lottieAnimationView.getComposition() != null) {
            addOverlayDynamicColor$update(context, lottieAnimationView);
        } else {
            lottieAnimationView.addLottieOnCompositionLoadedListener(new SidefpsControllerKt$$ExternalSyntheticLambda1(context, lottieAnimationView));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: addOverlayDynamicColor$lambda-1  reason: not valid java name */
    public static final void m2577addOverlayDynamicColor$lambda1(Context context, LottieAnimationView lottieAnimationView, LottieComposition lottieComposition) {
        Intrinsics.checkNotNullParameter(context, "$context");
        Intrinsics.checkNotNullParameter(lottieAnimationView, "$this_addOverlayDynamicColor");
        addOverlayDynamicColor$update(context, lottieAnimationView);
    }
}
