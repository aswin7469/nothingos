package com.android.systemui.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Handler;
import android.util.MathUtils;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.R$drawable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SystemSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: FaceAuthScreenBrightnessController.kt */
/* loaded from: classes.dex */
public class FaceAuthScreenBrightnessController implements Dumpable {
    private final long brightnessAnimationDuration;
    @Nullable
    private ValueAnimator brightnessAnimator;
    @NotNull
    private final DumpManager dumpManager;
    private final boolean enabled;
    @NotNull
    private final GlobalSettings globalSettings;
    @NotNull
    private final FaceAuthScreenBrightnessController$keyguardUpdateCallback$1 keyguardUpdateCallback;
    @NotNull
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    @NotNull
    private final Handler mainHandler;
    private final float maxScreenBrightness;
    private final float maxScrimOpacity;
    @NotNull
    private final NotificationShadeWindowController notificationShadeWindowController;
    private boolean overridingBrightness;
    @NotNull
    private final Resources resources;
    @NotNull
    private final SystemSettings systemSettings;
    private boolean useFaceAuthWallpaper;
    private float userDefinedBrightness = 1.0f;
    private View whiteOverlay;

    @VisibleForTesting
    public static /* synthetic */ void getUseFaceAuthWallpaper$annotations() {
    }

    /* JADX WARN: Type inference failed for: r2v14, types: [com.android.systemui.keyguard.FaceAuthScreenBrightnessController$keyguardUpdateCallback$1] */
    public FaceAuthScreenBrightnessController(@NotNull NotificationShadeWindowController notificationShadeWindowController, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor, @NotNull Resources resources, @NotNull GlobalSettings globalSettings, @NotNull SystemSettings systemSettings, @NotNull Handler mainHandler, @NotNull DumpManager dumpManager, boolean z) {
        Intrinsics.checkNotNullParameter(notificationShadeWindowController, "notificationShadeWindowController");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(resources, "resources");
        Intrinsics.checkNotNullParameter(globalSettings, "globalSettings");
        Intrinsics.checkNotNullParameter(systemSettings, "systemSettings");
        Intrinsics.checkNotNullParameter(mainHandler, "mainHandler");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.notificationShadeWindowController = notificationShadeWindowController;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        this.resources = resources;
        this.globalSettings = globalSettings;
        this.systemSettings = systemSettings;
        this.mainHandler = mainHandler;
        this.dumpManager = dumpManager;
        this.enabled = z;
        this.useFaceAuthWallpaper = globalSettings.getInt("sysui.use_face_auth_wallpaper", FaceAuthScreenBrightnessControllerKt.getDEFAULT_USE_FACE_WALLPAPER() ? 1 : 0) != 1 ? false : true;
        this.brightnessAnimationDuration = globalSettings.getLong("sysui.face_brightness_anim_duration", FaceAuthScreenBrightnessControllerKt.getDEFAULT_ANIMATION_DURATION());
        this.maxScreenBrightness = globalSettings.getInt("sysui.face_max_brightness", FaceAuthScreenBrightnessControllerKt.getMAX_SCREEN_BRIGHTNESS()) / 100.0f;
        this.maxScrimOpacity = globalSettings.getInt("sysui.face_max_scrim_opacity", FaceAuthScreenBrightnessControllerKt.getMAX_SCRIM_OPACTY()) / 100.0f;
        this.keyguardUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$keyguardUpdateCallback$1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onBiometricRunningStateChanged(boolean z2, @Nullable BiometricSourceType biometricSourceType) {
                boolean z3;
                if (biometricSourceType != BiometricSourceType.FACE) {
                    return;
                }
                FaceAuthScreenBrightnessController faceAuthScreenBrightnessController = FaceAuthScreenBrightnessController.this;
                z3 = faceAuthScreenBrightnessController.enabled;
                if (!z3) {
                    z2 = false;
                }
                faceAuthScreenBrightnessController.setOverridingBrightness(z2);
            }
        };
    }

    public final boolean getUseFaceAuthWallpaper() {
        return this.useFaceAuthWallpaper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setOverridingBrightness(boolean z) {
        if (this.overridingBrightness == z) {
            return;
        }
        this.overridingBrightness = z;
        ValueAnimator valueAnimator = this.brightnessAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (!z) {
            this.notificationShadeWindowController.setFaceAuthDisplayBrightness(-1.0f);
            View view = this.whiteOverlay;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                throw null;
            } else if (view.getAlpha() <= 0.0f) {
                return;
            } else {
                View view2 = this.whiteOverlay;
                if (view2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                    throw null;
                }
                ValueAnimator createAnimator = createAnimator(view2.getAlpha(), 0.0f);
                createAnimator.setDuration(200L);
                createAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$overridingBrightness$1$1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        View view3;
                        view3 = FaceAuthScreenBrightnessController.this.whiteOverlay;
                        if (view3 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                            throw null;
                        }
                        Object animatedValue = valueAnimator2.getAnimatedValue();
                        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                        view3.setAlpha(((Float) animatedValue).floatValue());
                    }
                });
                createAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$overridingBrightness$1$2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(@Nullable Animator animator) {
                        View view3;
                        view3 = FaceAuthScreenBrightnessController.this.whiteOverlay;
                        if (view3 != null) {
                            view3.setVisibility(4);
                            FaceAuthScreenBrightnessController.this.brightnessAnimator = null;
                            return;
                        }
                        Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                        throw null;
                    }
                });
                createAnimator.start();
                Unit unit = Unit.INSTANCE;
                this.brightnessAnimator = createAnimator;
                return;
            }
        }
        final float max = Float.max(this.maxScreenBrightness, this.userDefinedBrightness);
        View view3 = this.whiteOverlay;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
            throw null;
        }
        view3.setVisibility(0);
        ValueAnimator createAnimator2 = createAnimator(0.0f, 1.0f);
        createAnimator2.setDuration(this.brightnessAnimationDuration);
        createAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$overridingBrightness$2$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                float f;
                float f2;
                NotificationShadeWindowController notificationShadeWindowController;
                View view4;
                Object animatedValue = valueAnimator2.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                float floatValue = ((Float) animatedValue).floatValue();
                f = FaceAuthScreenBrightnessController.this.userDefinedBrightness;
                float constrainedMap = MathUtils.constrainedMap(f, max, 0.0f, 0.5f, floatValue);
                f2 = FaceAuthScreenBrightnessController.this.maxScrimOpacity;
                float constrainedMap2 = MathUtils.constrainedMap(0.0f, f2, 0.5f, 1.0f, floatValue);
                notificationShadeWindowController = FaceAuthScreenBrightnessController.this.notificationShadeWindowController;
                notificationShadeWindowController.setFaceAuthDisplayBrightness(constrainedMap);
                view4 = FaceAuthScreenBrightnessController.this.whiteOverlay;
                if (view4 != null) {
                    view4.setAlpha(constrainedMap2);
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                    throw null;
                }
            }
        });
        createAnimator2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$overridingBrightness$2$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@Nullable Animator animator) {
                FaceAuthScreenBrightnessController.this.brightnessAnimator = null;
            }
        });
        createAnimator2.start();
        Unit unit2 = Unit.INSTANCE;
        this.brightnessAnimator = createAnimator2;
    }

    @VisibleForTesting
    public ValueAnimator createAnimator(float f, float f2) {
        return ValueAnimator.ofFloat(f, f2);
    }

    @Nullable
    public final Bitmap getFaceAuthWallpaper() {
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        if (!this.useFaceAuthWallpaper || !this.keyguardUpdateMonitor.isFaceAuthEnabledForUser(currentUser)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return BitmapFactory.decodeResource(this.resources, R$drawable.face_auth_wallpaper, options);
    }

    public final void attach(@NotNull View overlayView) {
        Intrinsics.checkNotNullParameter(overlayView, "overlayView");
        this.whiteOverlay = overlayView;
        if (overlayView != null) {
            overlayView.setFocusable(8);
            View view = this.whiteOverlay;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                throw null;
            }
            view.setBackground(new ColorDrawable(-1));
            View view2 = this.whiteOverlay;
            if (view2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                throw null;
            }
            view2.setEnabled(false);
            View view3 = this.whiteOverlay;
            if (view3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                throw null;
            }
            view3.setAlpha(0.0f);
            View view4 = this.whiteOverlay;
            if (view4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
                throw null;
            }
            view4.setVisibility(4);
            DumpManager dumpManager = this.dumpManager;
            String name = FaceAuthScreenBrightnessController.class.getName();
            Intrinsics.checkNotNullExpressionValue(name, "this.javaClass.name");
            dumpManager.registerDumpable(name, this);
            this.keyguardUpdateMonitor.registerCallback(this.keyguardUpdateCallback);
            SystemSettings systemSettings = this.systemSettings;
            final Handler handler = this.mainHandler;
            systemSettings.registerContentObserver("screen_brightness_float", new ContentObserver(handler) { // from class: com.android.systemui.keyguard.FaceAuthScreenBrightnessController$attach$1
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    SystemSettings systemSettings2;
                    FaceAuthScreenBrightnessController faceAuthScreenBrightnessController = FaceAuthScreenBrightnessController.this;
                    systemSettings2 = faceAuthScreenBrightnessController.systemSettings;
                    faceAuthScreenBrightnessController.userDefinedBrightness = systemSettings2.getFloat("screen_brightness_float");
                }
            });
            this.userDefinedBrightness = this.systemSettings.getFloat("screen_brightness_float", 1.0f);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("whiteOverlay");
        throw null;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println(Intrinsics.stringPlus("overridingBrightness: ", Boolean.valueOf(this.overridingBrightness)));
        pw.println(Intrinsics.stringPlus("useFaceAuthWallpaper: ", Boolean.valueOf(getUseFaceAuthWallpaper())));
        pw.println(Intrinsics.stringPlus("brightnessAnimator: ", this.brightnessAnimator));
        pw.println(Intrinsics.stringPlus("brightnessAnimationDuration: ", Long.valueOf(this.brightnessAnimationDuration)));
        pw.println(Intrinsics.stringPlus("maxScreenBrightness: ", Float.valueOf(this.maxScreenBrightness)));
        pw.println(Intrinsics.stringPlus("userDefinedBrightness: ", Float.valueOf(this.userDefinedBrightness)));
        pw.println(Intrinsics.stringPlus("enabled: ", Boolean.valueOf(this.enabled)));
    }
}
