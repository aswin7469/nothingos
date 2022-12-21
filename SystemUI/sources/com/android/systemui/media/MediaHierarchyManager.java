package com.android.systemui.media;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Trace;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.LargeScreenUtils;
import com.android.systemui.util.animation.UniqueObjectHostView;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000°\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b&\b\u0007\u0018\u0000 \u00012\u00020\u0001:\u0002\u0001BW\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015¢\u0006\u0002\u0010\u0016J\u0018\u0010d\u001a\u00020e2\u0006\u00108\u001a\u00020/2\u0006\u0010R\u001a\u00020/H\u0002J,\u0010f\u001a\u00020e2\u0006\u0010g\u001a\u00020\u001d2\u0006\u0010h\u001a\u00020\u00182\b\b\u0002\u0010i\u001a\u00020\u001a2\b\b\u0002\u0010j\u001a\u00020\u001dH\u0002J\b\u0010k\u001a\u00020eH\u0002J\b\u0010l\u001a\u00020\u001aH\u0002J\u0018\u0010m\u001a\u00020\u00182\u0006\u0010n\u001a\u00020\u00182\u0006\u0010o\u001a\u00020\u001aH\u0002J\b\u0010p\u001a\u00020/H\u0002J\b\u0010q\u001a\u00020/H\u0007J\b\u0010r\u001a\u00020eH\u0002J\u0006\u0010s\u001a\u00020eJ\b\u0010t\u001a\u00020uH\u0002J$\u0010v\u001a\u000e\u0012\u0004\u0012\u00020x\u0012\u0004\u0012\u00020x0w2\u0006\u0010R\u001a\u00020/2\u0006\u00108\u001a\u00020/H\u0002J\u0006\u0010y\u001a\u00020/J\u0012\u0010z\u001a\u0004\u0018\u00010P2\u0006\u0010{\u001a\u00020/H\u0002J\b\u0010|\u001a\u00020\u0018H\u0002J\b\u0010}\u001a\u00020\u0018H\u0002J\b\u0010~\u001a\u00020\u001aH\u0002J0\u0010\u001a\u00020\u001d2\u0007\u0010\u0001\u001a\u00020\u001d2\u0007\u0010\u0001\u001a\u00020\u001d2\u0007\u0010\u0001\u001a\u00020\u00182\u000b\b\u0002\u0010\u0001\u001a\u0004\u0018\u00010\u001dH\u0002J\t\u0010\u0001\u001a\u00020\u001aH\u0002J\u0007\u0010\u0001\u001a\u00020\u001aJ\t\u0010\u0001\u001a\u00020\u001aH\u0002J\t\u0010\u0001\u001a\u00020\u001aH\u0002J\t\u0010\u0001\u001a\u00020\u001aH\u0002J\t\u0010\u0001\u001a\u00020\u001aH\u0002J\t\u0010\u0001\u001a\u00020\u001aH\u0002J\t\u0010\u0001\u001a\u00020\u001aH\u0002J\t\u0010\u0001\u001a\u00020\u001aH\u0002J\u001b\u0010\u0001\u001a\u00020e2\u0007\u0010\u0001\u001a\u00020\u001a2\u0007\u0010\u0001\u001a\u00020\u001aH\u0002J\u0010\u0010\u0001\u001a\u00020u2\u0007\u0010\u0001\u001a\u00020PJ\u0012\u0010\u0001\u001a\u00020e2\u0007\u0010\u0001\u001a\u00020\u001dH\u0002J\t\u0010\u0001\u001a\u00020/H\u0002J\u000f\u0010\u0001\u001a\u00020e2\u0006\u0010&\u001a\u00020\u0018J\u001a\u0010\u0001\u001a\u00020\u001a2\u0007\u0010\u0001\u001a\u00020/2\u0006\u0010R\u001a\u00020/H\u0002J\t\u0010\u0001\u001a\u00020eH\u0002J\u001f\u0010\u0001\u001a\u00020e2\t\b\u0002\u0010\u0001\u001a\u00020\u001a2\t\b\u0002\u0010\u0001\u001a\u00020\u001aH\u0002J\t\u0010\u0001\u001a\u00020eH\u0002J\t\u0010\u0001\u001a\u00020eH\u0002R\u000e\u0010\u0017\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010 \u001a\n \"*\u0004\u0018\u00010!0!X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010#\u001a\u00020\u001a8BX\u0004¢\u0006\u0006\u001a\u0004\b$\u0010%R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010'\u001a\u00020\u00182\u0006\u0010&\u001a\u00020\u0018@BX\u000e¢\u0006\b\n\u0000\"\u0004\b(\u0010)R$\u0010*\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\u001a@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010%\"\u0004\b,\u0010-R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010.\u001a\u00020/X\u000e¢\u0006\b\n\u0000\u0012\u0004\b0\u00101R\u0014\u00102\u001a\u00020/X\u000e¢\u0006\b\n\u0000\u0012\u0004\b3\u00101R\u0014\u00104\u001a\u00020/X\u000e¢\u0006\b\n\u0000\u0012\u0004\b5\u00101R\u000e\u00106\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R\u0014\u00108\u001a\u00020/X\u000e¢\u0006\b\n\u0000\u0012\u0004\b9\u00101R\u000e\u0010:\u001a\u00020/X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010;\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\u001a@BX\u000e¢\u0006\b\n\u0000\"\u0004\b<\u0010-R\u001e\u0010=\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\u001a@BX\u000e¢\u0006\b\n\u0000\"\u0004\b>\u0010-R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010?\u001a\u00020\u00182\u0006\u0010&\u001a\u00020\u0018@BX\u000e¢\u0006\b\n\u0000\"\u0004\b@\u0010)R\u001e\u0010A\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\u001a@BX\u000e¢\u0006\b\n\u0000\"\u0004\bB\u0010-R\u001e\u0010C\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\u001a@BX\u000e¢\u0006\b\n\u0000\"\u0004\bD\u0010-R\u0016\u0010E\u001a\u00020\u001a8BX\u000e¢\u0006\b\n\u0000\u001a\u0004\bF\u0010%R\u000e\u0010G\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010I\u001a\u00020\u001a8BX\u0004¢\u0006\u0006\u001a\u0004\bI\u0010%R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010J\u001a\u00020K8BX\u0004¢\u0006\u0006\u001a\u0004\bL\u0010MR\u0018\u0010N\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010P0OX\u0004¢\u0006\u0004\n\u0002\u0010QR\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010R\u001a\u00020/X\u000e¢\u0006\b\n\u0000\u0012\u0004\bS\u00101R$\u0010T\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\u001a@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bU\u0010%\"\u0004\bV\u0010-R$\u0010W\u001a\u00020\u00182\u0006\u0010&\u001a\u00020\u0018@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bX\u0010Y\"\u0004\bZ\u0010)R\u0010\u0010[\u001a\u0004\u0018\u00010\\X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010]\u001a\u0004\u0018\u00010^X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010_\u001a\u00020`X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010a\u001a\u00020/X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010b\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010c\u001a\u00020\u001dX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/media/MediaHierarchyManager;", "", "context", "Landroid/content/Context;", "statusBarStateController", "Lcom/android/systemui/statusbar/SysuiStatusBarStateController;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "bypassController", "Lcom/android/systemui/statusbar/phone/KeyguardBypassController;", "mediaCarouselController", "Lcom/android/systemui/media/MediaCarouselController;", "notifLockscreenUserManager", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "wakefulnessLifecycle", "Lcom/android/systemui/keyguard/WakefulnessLifecycle;", "keyguardViewController", "Lcom/android/keyguard/KeyguardViewController;", "dreamOverlayStateController", "Lcom/android/systemui/dreams/DreamOverlayStateController;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/SysuiStatusBarStateController;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Lcom/android/systemui/statusbar/phone/KeyguardBypassController;Lcom/android/systemui/media/MediaCarouselController;Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/keyguard/WakefulnessLifecycle;Lcom/android/keyguard/KeyguardViewController;Lcom/android/systemui/dreams/DreamOverlayStateController;)V", "animationCrossFadeProgress", "", "animationPending", "", "animationStartAlpha", "animationStartBounds", "Landroid/graphics/Rect;", "animationStartClipping", "animationStartCrossFadeProgress", "animator", "Landroid/animation/ValueAnimator;", "kotlin.jvm.PlatformType", "blockLocationChanges", "getBlockLocationChanges", "()Z", "value", "carouselAlpha", "setCarouselAlpha", "(F)V", "collapsingShadeFromQS", "getCollapsingShadeFromQS", "setCollapsingShadeFromQS", "(Z)V", "crossFadeAnimationEndLocation", "", "getCrossFadeAnimationEndLocation$annotations", "()V", "crossFadeAnimationStartLocation", "getCrossFadeAnimationStartLocation$annotations", "currentAttachmentLocation", "getCurrentAttachmentLocation$annotations", "currentBounds", "currentClipping", "desiredLocation", "getDesiredLocation$annotations", "distanceForFullShadeTransition", "dozeAnimationRunning", "setDozeAnimationRunning", "dreamOverlayActive", "setDreamOverlayActive", "fullShadeTransitionProgress", "setFullShadeTransitionProgress", "fullyAwake", "setFullyAwake", "goingToSleep", "setGoingToSleep", "hasActiveMedia", "getHasActiveMedia", "inSplitShade", "isCrossFadeAnimatorRunning", "isTransitioningToFullShade", "mediaFrame", "Landroid/view/ViewGroup;", "getMediaFrame", "()Landroid/view/ViewGroup;", "mediaHosts", "", "Lcom/android/systemui/media/MediaHost;", "[Lcom/android/systemui/media/MediaHost;", "previousLocation", "getPreviousLocation$annotations", "qsExpanded", "getQsExpanded", "setQsExpanded", "qsExpansion", "getQsExpansion", "()F", "setQsExpansion", "rootOverlay", "Landroid/view/ViewGroupOverlay;", "rootView", "Landroid/view/View;", "startAnimation", "Ljava/lang/Runnable;", "statusbarState", "targetBounds", "targetClipping", "adjustAnimatorForTransition", "", "applyState", "bounds", "alpha", "immediately", "clipBounds", "applyTargetStateIfNotAnimating", "areGuidedTransitionHostsVisible", "calculateAlphaFromCrossFade", "crossFadeProgress", "instantlyShowAtEnd", "calculateLocation", "calculateTransformationType", "cancelAnimationAndApplyDesiredState", "closeGuts", "createUniqueObjectHost", "Lcom/android/systemui/util/animation/UniqueObjectHostView;", "getAnimationParams", "Lkotlin/Pair;", "", "getGuidedTransformationTranslationY", "getHost", "location", "getQSTransformationProgress", "getTransformationProgress", "hasValidStartAndEndLocations", "interpolateBounds", "startBounds", "endBounds", "progress", "result", "isCurrentlyFading", "isCurrentlyInGuidedTransformation", "isHomeScreenShadeVisibleToUser", "isLockScreenShadeVisibleToUser", "isLockScreenVisibleToUser", "isSplitShadeExpanding", "isTransformingToFullShadeAndInQQS", "isTransitionRunning", "isVisibleToUser", "performTransitionToNewLocation", "isNewView", "animate", "register", "mediaObject", "resolveClipping", "resolveLocationForFading", "setTransitionToFullShadeAmount", "shouldAnimateTransition", "currentLocation", "updateConfiguration", "updateDesiredLocation", "forceNoAnimation", "forceStateUpdate", "updateHostAttachment", "updateTargetState", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaHierarchyManager.kt */
public final class MediaHierarchyManager {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int IN_OVERLAY = -1000;
    public static final int LOCATION_DREAM_OVERLAY = 3;
    public static final int LOCATION_LOCKSCREEN = 2;
    public static final int LOCATION_QQS = 1;
    public static final int LOCATION_QS = 0;
    public static final int TRANSFORMATION_TYPE_FADE = 1;
    public static final int TRANSFORMATION_TYPE_TRANSITION = 0;
    private float animationCrossFadeProgress;
    /* access modifiers changed from: private */
    public boolean animationPending;
    private float animationStartAlpha;
    private Rect animationStartBounds = new Rect();
    private Rect animationStartClipping = new Rect();
    private float animationStartCrossFadeProgress;
    private ValueAnimator animator;
    private final KeyguardBypassController bypassController;
    private float carouselAlpha;
    private boolean collapsingShadeFromQS;
    private final Context context;
    private int crossFadeAnimationEndLocation = -1;
    private int crossFadeAnimationStartLocation = -1;
    private int currentAttachmentLocation;
    private Rect currentBounds = new Rect();
    private Rect currentClipping = new Rect();
    private int desiredLocation;
    private int distanceForFullShadeTransition;
    private boolean dozeAnimationRunning;
    private boolean dreamOverlayActive;
    /* access modifiers changed from: private */
    public final DreamOverlayStateController dreamOverlayStateController;
    private float fullShadeTransitionProgress;
    private boolean fullyAwake;
    private boolean goingToSleep;
    private boolean hasActiveMedia;
    private boolean inSplitShade;
    /* access modifiers changed from: private */
    public boolean isCrossFadeAnimatorRunning;
    private final KeyguardStateController keyguardStateController;
    private final KeyguardViewController keyguardViewController;
    /* access modifiers changed from: private */
    public final MediaCarouselController mediaCarouselController;
    private final MediaHost[] mediaHosts;
    private final NotificationLockscreenUserManager notifLockscreenUserManager;
    private int previousLocation;
    private boolean qsExpanded;
    private float qsExpansion;
    /* access modifiers changed from: private */
    public ViewGroupOverlay rootOverlay;
    /* access modifiers changed from: private */
    public View rootView;
    /* access modifiers changed from: private */
    public final Runnable startAnimation;
    private final SysuiStatusBarStateController statusBarStateController;
    /* access modifiers changed from: private */
    public int statusbarState;
    private Rect targetBounds = new Rect();
    private Rect targetClipping = new Rect();

    private final float calculateAlphaFromCrossFade(float f, boolean z) {
        if (f <= 0.5f) {
            return 1.0f - (f / 0.5f);
        }
        if (z) {
            return 1.0f;
        }
        return (f - 0.5f) / 0.5f;
    }

    private static /* synthetic */ void getCrossFadeAnimationEndLocation$annotations() {
    }

    private static /* synthetic */ void getCrossFadeAnimationStartLocation$annotations() {
    }

    private static /* synthetic */ void getCurrentAttachmentLocation$annotations() {
    }

    private static /* synthetic */ void getDesiredLocation$annotations() {
    }

    private static /* synthetic */ void getPreviousLocation$annotations() {
    }

    @Inject
    public MediaHierarchyManager(Context context2, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController2, KeyguardBypassController keyguardBypassController, MediaCarouselController mediaCarouselController2, NotificationLockscreenUserManager notificationLockscreenUserManager, ConfigurationController configurationController, WakefulnessLifecycle wakefulnessLifecycle, KeyguardViewController keyguardViewController2, DreamOverlayStateController dreamOverlayStateController2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "bypassController");
        Intrinsics.checkNotNullParameter(mediaCarouselController2, "mediaCarouselController");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "notifLockscreenUserManager");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(keyguardViewController2, "keyguardViewController");
        Intrinsics.checkNotNullParameter(dreamOverlayStateController2, "dreamOverlayStateController");
        this.context = context2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.keyguardStateController = keyguardStateController2;
        this.bypassController = keyguardBypassController;
        this.mediaCarouselController = mediaCarouselController2;
        this.notifLockscreenUserManager = notificationLockscreenUserManager;
        this.keyguardViewController = keyguardViewController2;
        this.dreamOverlayStateController = dreamOverlayStateController2;
        this.statusbarState = sysuiStatusBarStateController.getState();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.addUpdateListener(new MediaHierarchyManager$$ExternalSyntheticLambda0(this, ofFloat));
        ofFloat.addListener(new MediaHierarchyManager$animator$1$2(this));
        this.animator = ofFloat;
        this.mediaHosts = new MediaHost[4];
        this.previousLocation = -1;
        this.desiredLocation = -1;
        this.currentAttachmentLocation = -1;
        this.startAnimation = new MediaHierarchyManager$$ExternalSyntheticLambda1(this);
        this.animationCrossFadeProgress = 1.0f;
        this.carouselAlpha = 1.0f;
        updateConfiguration();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(Configuration configuration) {
                this.this$0.updateConfiguration();
                this.this$0.updateDesiredLocation(true, true);
            }
        });
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener(this) {
            final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            public void onStatePreChange(int i, int i2) {
                this.this$0.statusbarState = i2;
                MediaHierarchyManager.updateDesiredLocation$default(this.this$0, false, false, 3, (Object) null);
            }

            public void onStateChanged(int i) {
                this.this$0.updateTargetState();
                if (i == 2 && this.this$0.isLockScreenShadeVisibleToUser()) {
                    this.this$0.mediaCarouselController.logSmartspaceImpression(this.this$0.getQsExpanded());
                }
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }

            /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
                if ((r3 == 1.0f) == false) goto L_0x001b;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onDozeAmountChanged(float r3, float r4) {
                /*
                    r2 = this;
                    com.android.systemui.media.MediaHierarchyManager r2 = r2.this$0
                    r4 = 0
                    int r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                    r0 = 1
                    r1 = 0
                    if (r4 != 0) goto L_0x000b
                    r4 = r0
                    goto L_0x000c
                L_0x000b:
                    r4 = r1
                L_0x000c:
                    if (r4 != 0) goto L_0x001a
                    r4 = 1065353216(0x3f800000, float:1.0)
                    int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                    if (r3 != 0) goto L_0x0016
                    r3 = r0
                    goto L_0x0017
                L_0x0016:
                    r3 = r1
                L_0x0017:
                    if (r3 != 0) goto L_0x001a
                    goto L_0x001b
                L_0x001a:
                    r0 = r1
                L_0x001b:
                    r2.setDozeAnimationRunning(r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.C21962.onDozeAmountChanged(float, float):void");
            }

            public void onDozingChanged(boolean z) {
                if (!z) {
                    this.this$0.setDozeAnimationRunning(false);
                    if (this.this$0.isLockScreenVisibleToUser()) {
                        this.this$0.mediaCarouselController.logSmartspaceImpression(this.this$0.getQsExpanded());
                    }
                } else {
                    MediaHierarchyManager.updateDesiredLocation$default(this.this$0, false, false, 3, (Object) null);
                    this.this$0.setQsExpanded(false);
                    this.this$0.closeGuts();
                }
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }

            public void onExpandedChanged(boolean z) {
                if (this.this$0.isHomeScreenShadeVisibleToUser()) {
                    this.this$0.mediaCarouselController.logSmartspaceImpression(this.this$0.getQsExpanded());
                }
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }
        });
        dreamOverlayStateController2.addCallback((DreamOverlayStateController.Callback) new DreamOverlayStateController.Callback(this) {
            final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            public void onStateChanged() {
                this.this$0.setDreamOverlayActive(this.this$0.dreamOverlayStateController.isOverlayActive());
            }
        });
        wakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer(this) {
            final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            public void onFinishedGoingToSleep() {
                this.this$0.setGoingToSleep(false);
            }

            public void onStartedGoingToSleep() {
                this.this$0.setGoingToSleep(true);
                this.this$0.setFullyAwake(false);
            }

            public void onFinishedWakingUp() {
                this.this$0.setGoingToSleep(false);
                this.this$0.setFullyAwake(true);
            }

            public void onStartedWakingUp() {
                this.this$0.setGoingToSleep(false);
            }
        });
        mediaCarouselController2.setUpdateUserVisibility(new Function0<Unit>(this) {
            final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            public final void invoke() {
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }
        });
    }

    private final ViewGroup getMediaFrame() {
        return this.mediaCarouselController.getMediaFrame();
    }

    /* access modifiers changed from: private */
    /* renamed from: animator$lambda-1$lambda-0  reason: not valid java name */
    public static final void m2809animator$lambda1$lambda0(MediaHierarchyManager mediaHierarchyManager, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
        float f;
        Intrinsics.checkNotNullParameter(mediaHierarchyManager, "this$0");
        mediaHierarchyManager.updateTargetState();
        float animatedFraction = valueAnimator.getAnimatedFraction();
        if (mediaHierarchyManager.isCrossFadeAnimatorRunning) {
            float lerp = MathUtils.lerp(mediaHierarchyManager.animationStartCrossFadeProgress, 1.0f, valueAnimator.getAnimatedFraction());
            mediaHierarchyManager.animationCrossFadeProgress = lerp;
            animatedFraction = lerp < 0.5f ? 0.0f : 1.0f;
            f = mediaHierarchyManager.calculateAlphaFromCrossFade(lerp, false);
        } else {
            f = MathUtils.lerp(mediaHierarchyManager.animationStartAlpha, 1.0f, valueAnimator.getAnimatedFraction());
        }
        mediaHierarchyManager.interpolateBounds(mediaHierarchyManager.animationStartBounds, mediaHierarchyManager.targetBounds, animatedFraction, mediaHierarchyManager.currentBounds);
        mediaHierarchyManager.resolveClipping(mediaHierarchyManager.currentClipping);
        applyState$default(mediaHierarchyManager, mediaHierarchyManager.currentBounds, f, false, mediaHierarchyManager.currentClipping, 4, (Object) null);
    }

    private final void resolveClipping(Rect rect) {
        if (this.animationStartClipping.isEmpty()) {
            rect.set(this.targetClipping);
        } else if (this.targetClipping.isEmpty()) {
            rect.set(this.animationStartClipping);
        } else {
            rect.setIntersect(this.animationStartClipping, this.targetClipping);
        }
    }

    private final boolean getHasActiveMedia() {
        MediaHost mediaHost = this.mediaHosts[1];
        return mediaHost != null && mediaHost.getVisible();
    }

    /* access modifiers changed from: private */
    /* renamed from: startAnimation$lambda-2  reason: not valid java name */
    public static final void m2810startAnimation$lambda2(MediaHierarchyManager mediaHierarchyManager) {
        Intrinsics.checkNotNullParameter(mediaHierarchyManager, "this$0");
        mediaHierarchyManager.animator.start();
    }

    public final float getQsExpansion() {
        return this.qsExpansion;
    }

    public final void setQsExpansion(float f) {
        if (!(this.qsExpansion == f)) {
            this.qsExpansion = f;
            updateDesiredLocation$default(this, false, false, 3, (Object) null);
            if (getQSTransformationProgress() >= 0.0f) {
                updateTargetState();
                applyTargetStateIfNotAnimating();
            }
        }
    }

    public final boolean getQsExpanded() {
        return this.qsExpanded;
    }

    public final void setQsExpanded(boolean z) {
        if (this.qsExpanded != z) {
            this.qsExpanded = z;
            this.mediaCarouselController.getMediaCarouselScrollHandler().setQsExpanded(z);
        }
        if (z && (isLockScreenShadeVisibleToUser() || isHomeScreenShadeVisibleToUser())) {
            this.mediaCarouselController.logSmartspaceImpression(z);
        }
        this.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(isVisibleToUser());
    }

    private final void setFullShadeTransitionProgress(float f) {
        if (!(this.fullShadeTransitionProgress == f)) {
            this.fullShadeTransitionProgress = f;
            if (!this.bypassController.getBypassEnabled() && this.statusbarState == 1) {
                updateDesiredLocation$default(this, isCurrentlyFading(), false, 2, (Object) null);
                if (f >= 0.0f) {
                    updateTargetState();
                    setCarouselAlpha(calculateAlphaFromCrossFade(this.fullShadeTransitionProgress, true));
                    applyTargetStateIfNotAnimating();
                }
            }
        }
    }

    private final boolean isTransitioningToFullShade() {
        if ((this.fullShadeTransitionProgress == 0.0f) || this.bypassController.getBypassEnabled() || this.statusbarState != 1) {
            return false;
        }
        return true;
    }

    public final void setTransitionToFullShadeAmount(float f) {
        setFullShadeTransitionProgress(MathUtils.saturate(f / ((float) this.distanceForFullShadeTransition)));
    }

    public final int getGuidedTransformationTranslationY() {
        if (!isCurrentlyInGuidedTransformation()) {
            return -1;
        }
        MediaHost host = getHost(this.previousLocation);
        if (host == null) {
            return 0;
        }
        return this.targetBounds.top - host.getCurrentBounds().top;
    }

    public final boolean getCollapsingShadeFromQS() {
        return this.collapsingShadeFromQS;
    }

    public final void setCollapsingShadeFromQS(boolean z) {
        if (this.collapsingShadeFromQS != z) {
            this.collapsingShadeFromQS = z;
            updateDesiredLocation$default(this, true, false, 2, (Object) null);
        }
    }

    private final boolean getBlockLocationChanges() {
        return this.goingToSleep || this.dozeAnimationRunning;
    }

    /* access modifiers changed from: private */
    public final void setGoingToSleep(boolean z) {
        if (this.goingToSleep != z) {
            this.goingToSleep = z;
            if (!z) {
                updateDesiredLocation$default(this, false, false, 3, (Object) null);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void setFullyAwake(boolean z) {
        if (this.fullyAwake != z) {
            this.fullyAwake = z;
            if (z) {
                updateDesiredLocation$default(this, true, false, 2, (Object) null);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void setDozeAnimationRunning(boolean z) {
        if (this.dozeAnimationRunning != z) {
            this.dozeAnimationRunning = z;
            if (!z) {
                updateDesiredLocation$default(this, false, false, 3, (Object) null);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void setDreamOverlayActive(boolean z) {
        if (this.dreamOverlayActive != z) {
            this.dreamOverlayActive = z;
            updateDesiredLocation$default(this, true, false, 2, (Object) null);
        }
    }

    private final void setCarouselAlpha(float f) {
        if (!(this.carouselAlpha == f)) {
            this.carouselAlpha = f;
            CrossFadeHelper.fadeIn(getMediaFrame(), f);
        }
    }

    /* access modifiers changed from: private */
    public final void updateConfiguration() {
        this.distanceForFullShadeTransition = this.context.getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_media_transition_distance);
        Resources resources = this.context.getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "context.resources");
        this.inSplitShade = LargeScreenUtils.shouldUseSplitNotificationShade(resources);
    }

    public final UniqueObjectHostView register(MediaHost mediaHost) {
        Intrinsics.checkNotNullParameter(mediaHost, "mediaObject");
        UniqueObjectHostView createUniqueObjectHost = createUniqueObjectHost();
        mediaHost.setHostView(createUniqueObjectHost);
        mediaHost.addVisibilityChangeListener(new MediaHierarchyManager$register$1(mediaHost, this));
        this.mediaHosts[mediaHost.getLocation()] = mediaHost;
        if (mediaHost.getLocation() == this.desiredLocation) {
            this.desiredLocation = -1;
        }
        if (mediaHost.getLocation() == this.currentAttachmentLocation) {
            this.currentAttachmentLocation = -1;
        }
        updateDesiredLocation$default(this, false, false, 3, (Object) null);
        return createUniqueObjectHost;
    }

    public final void closeGuts() {
        MediaCarouselController.closeGuts$default(this.mediaCarouselController, false, 1, (Object) null);
    }

    private final UniqueObjectHostView createUniqueObjectHost() {
        UniqueObjectHostView uniqueObjectHostView = new UniqueObjectHostView(this.context);
        uniqueObjectHostView.addOnAttachStateChangeListener(new MediaHierarchyManager$createUniqueObjectHost$1(this, uniqueObjectHostView));
        return uniqueObjectHostView;
    }

    static /* synthetic */ void updateDesiredLocation$default(MediaHierarchyManager mediaHierarchyManager, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        if ((i & 2) != 0) {
            z2 = false;
        }
        mediaHierarchyManager.updateDesiredLocation(z, z2);
    }

    private final boolean shouldAnimateTransition(int i, int i2) {
        if (isCurrentlyInGuidedTransformation()) {
            return false;
        }
        if (i2 == 2 && this.desiredLocation == 1 && this.statusbarState == 0) {
            return false;
        }
        if (i == 1 && i2 == 2 && (this.statusBarStateController.leaveOpenOnKeyguardHide() || this.statusbarState == 2)) {
            return true;
        }
        if (this.statusbarState == 1 && (i == 2 || i2 == 2)) {
            return false;
        }
        if (MediaHierarchyManagerKt.isShownNotFaded(getMediaFrame()) || this.animator.isRunning() || this.animationPending) {
            return true;
        }
        return false;
    }

    private final void adjustAnimatorForTransition(int i, int i2) {
        Pair<Long, Long> animationParams = getAnimationParams(i2, i);
        long longValue = animationParams.component1().longValue();
        long longValue2 = animationParams.component2().longValue();
        ValueAnimator valueAnimator = this.animator;
        valueAnimator.setDuration(longValue);
        valueAnimator.setStartDelay(longValue2);
    }

    private final Pair<Long, Long> getAnimationParams(int i, int i2) {
        long j;
        long j2 = 0;
        if (i == 2 && i2 == 1) {
            if (this.statusbarState == 0 && this.keyguardStateController.isKeyguardFadingAway()) {
                j2 = this.keyguardStateController.getKeyguardFadingAwayDelay();
            }
            j = 224;
        } else {
            j = (i == 1 && i2 == 2) ? 464 : 200;
        }
        return TuplesKt.m1796to(Long.valueOf(j), Long.valueOf(j2));
    }

    /* access modifiers changed from: private */
    public final void applyTargetStateIfNotAnimating() {
        if (!this.animator.isRunning()) {
            applyState$default(this, this.targetBounds, this.carouselAlpha, false, this.targetClipping, 4, (Object) null);
        }
    }

    /* access modifiers changed from: private */
    public final void updateTargetState() {
        MediaHost host = getHost(this.previousLocation);
        MediaHost host2 = getHost(this.desiredLocation);
        if (isCurrentlyInGuidedTransformation() && !isCurrentlyFading() && host != null && host2 != null) {
            float transformationProgress = getTransformationProgress();
            if (!host2.getVisible()) {
                host2 = host;
            } else if (!host.getVisible()) {
                host = host2;
            } else {
                MediaHost mediaHost = host2;
                host2 = host;
                host = mediaHost;
            }
            this.targetBounds = interpolateBounds$default(this, host2.getCurrentBounds(), host.getCurrentBounds(), transformationProgress, (Rect) null, 8, (Object) null);
            this.targetClipping = host.getCurrentClipping();
        } else if (host2 != null) {
            this.targetBounds.set(host2.getCurrentBounds());
            this.targetClipping = host2.getCurrentClipping();
        }
    }

    static /* synthetic */ Rect interpolateBounds$default(MediaHierarchyManager mediaHierarchyManager, Rect rect, Rect rect2, float f, Rect rect3, int i, Object obj) {
        if ((i & 8) != 0) {
            rect3 = null;
        }
        return mediaHierarchyManager.interpolateBounds(rect, rect2, f, rect3);
    }

    private final Rect interpolateBounds(Rect rect, Rect rect2, float f, Rect rect3) {
        int lerp = (int) MathUtils.lerp((float) rect.left, (float) rect2.left, f);
        int lerp2 = (int) MathUtils.lerp((float) rect.top, (float) rect2.top, f);
        int lerp3 = (int) MathUtils.lerp((float) rect.right, (float) rect2.right, f);
        int lerp4 = (int) MathUtils.lerp((float) rect.bottom, (float) rect2.bottom, f);
        if (rect3 == null) {
            rect3 = new Rect();
        }
        rect3.set(lerp, lerp2, lerp3, lerp4);
        return rect3;
    }

    public final boolean isCurrentlyInGuidedTransformation() {
        return hasValidStartAndEndLocations() && getTransformationProgress() >= 0.0f && areGuidedTransitionHostsVisible();
    }

    private final boolean hasValidStartAndEndLocations() {
        return (this.previousLocation == -1 || this.desiredLocation == -1) ? false : true;
    }

    public final int calculateTransformationType() {
        if (isTransitioningToFullShade()) {
            return (!this.inSplitShade || !areGuidedTransitionHostsVisible()) ? 1 : 0;
        }
        int i = this.previousLocation;
        if ((i == 2 && this.desiredLocation == 0) || (i == 0 && this.desiredLocation == 2)) {
            return 1;
        }
        return (i == 2 && this.desiredLocation == 1) ? 1 : 0;
    }

    private final boolean areGuidedTransitionHostsVisible() {
        MediaHost host = getHost(this.previousLocation);
        if (host != null && host.getVisible()) {
            MediaHost host2 = getHost(this.desiredLocation);
            if (host2 != null && host2.getVisible()) {
                return true;
            }
        }
        return false;
    }

    private final float getTransformationProgress() {
        float qSTransformationProgress = getQSTransformationProgress();
        if (this.statusbarState != 1 && qSTransformationProgress >= 0.0f) {
            return qSTransformationProgress;
        }
        if (isTransitioningToFullShade()) {
            return this.fullShadeTransitionProgress;
        }
        return -1.0f;
    }

    private final float getQSTransformationProgress() {
        MediaHost host = getHost(this.desiredLocation);
        MediaHost host2 = getHost(this.previousLocation);
        if (!getHasActiveMedia()) {
            return -1.0f;
        }
        boolean z = false;
        if (!(host != null && host.getLocation() == 0) || this.inSplitShade) {
            return -1.0f;
        }
        if (host2 != null && host2.getLocation() == 1) {
            z = true;
        }
        if (!z) {
            return -1.0f;
        }
        if (host2.getVisible() || this.statusbarState != 1) {
            return this.qsExpansion;
        }
        return -1.0f;
    }

    private final MediaHost getHost(int i) {
        if (i < 0) {
            return null;
        }
        return this.mediaHosts[i];
    }

    private final void cancelAnimationAndApplyDesiredState() {
        this.animator.cancel();
        MediaHost host = getHost(this.desiredLocation);
        if (host != null) {
            applyState$default(this, host.getCurrentBounds(), 1.0f, true, (Rect) null, 8, (Object) null);
        }
    }

    static /* synthetic */ void applyState$default(MediaHierarchyManager mediaHierarchyManager, Rect rect, float f, boolean z, Rect rect2, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        if ((i & 8) != 0) {
            rect2 = MediaHierarchyManagerKt.EMPTY_RECT;
        }
        mediaHierarchyManager.applyState(rect, f, z, rect2);
    }

    private final int resolveLocationForFading() {
        if (!this.isCrossFadeAnimatorRunning) {
            return this.desiredLocation;
        }
        if (((double) this.animationCrossFadeProgress) > 0.5d || this.previousLocation == -1) {
            return this.crossFadeAnimationEndLocation;
        }
        return this.crossFadeAnimationStartLocation;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0015, code lost:
        if ((getTransformationProgress() == 1.0f) != false) goto L_0x0017;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean isTransitionRunning() {
        /*
            r4 = this;
            boolean r0 = r4.isCurrentlyInGuidedTransformation()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0017
            float r0 = r4.getTransformationProgress()
            r3 = 1065353216(0x3f800000, float:1.0)
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x0014
            r0 = r2
            goto L_0x0015
        L_0x0014:
            r0 = r1
        L_0x0015:
            if (r0 == 0) goto L_0x0023
        L_0x0017:
            android.animation.ValueAnimator r0 = r4.animator
            boolean r0 = r0.isRunning()
            if (r0 != 0) goto L_0x0023
            boolean r4 = r4.animationPending
            if (r4 == 0) goto L_0x0024
        L_0x0023:
            r1 = r2
        L_0x0024:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.isTransitionRunning():boolean");
    }

    private final int calculateLocation() {
        int i;
        if (getBlockLocationChanges()) {
            return this.desiredLocation;
        }
        boolean z = true;
        boolean z2 = !this.bypassController.getBypassEnabled() && this.statusbarState == 1;
        boolean shouldShowLockscreenNotifications = this.notifLockscreenUserManager.shouldShowLockscreenNotifications();
        if (this.dreamOverlayActive) {
            i = 3;
        } else {
            float f = this.qsExpansion;
            if (((f > 0.0f || this.inSplitShade) && !z2) || ((f > 0.4f && z2) || !getHasActiveMedia() || (z2 && isSplitShadeExpanding()))) {
                i = 0;
            } else {
                i = ((!z2 || !isTransformingToFullShadeAndInQQS()) && z2 && shouldShowLockscreenNotifications) ? 2 : 1;
            }
        }
        if (i == 2) {
            MediaHost host = getHost(i);
            if (host == null || !host.getVisible()) {
                z = false;
            }
            if (!z && !this.statusBarStateController.isDozing()) {
                return 0;
            }
        }
        if (i == 2 && this.desiredLocation == 0 && this.collapsingShadeFromQS) {
            return 0;
        }
        if (i == 2 || this.desiredLocation != 2 || this.fullyAwake) {
            return i;
        }
        return 2;
    }

    private final boolean isSplitShadeExpanding() {
        return this.inSplitShade && isTransitioningToFullShade();
    }

    private final boolean isTransformingToFullShadeAndInQQS() {
        if (isTransitioningToFullShade() && !this.inSplitShade && this.fullShadeTransitionProgress > 0.5f) {
            return true;
        }
        return false;
    }

    private final boolean isCurrentlyFading() {
        if (isSplitShadeExpanding()) {
            return false;
        }
        if (isTransitioningToFullShade()) {
            return true;
        }
        return this.isCrossFadeAnimatorRunning;
    }

    /* access modifiers changed from: private */
    public final boolean isVisibleToUser() {
        return isLockScreenVisibleToUser() || isLockScreenShadeVisibleToUser() || isHomeScreenShadeVisibleToUser();
    }

    /* access modifiers changed from: private */
    public final boolean isLockScreenVisibleToUser() {
        if (this.statusBarStateController.isDozing() || this.keyguardViewController.isBouncerShowing() || this.statusBarStateController.getState() != 1 || !this.notifLockscreenUserManager.shouldShowLockscreenNotifications() || !this.statusBarStateController.isExpanded() || this.qsExpanded) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public final boolean isLockScreenShadeVisibleToUser() {
        if (!this.statusBarStateController.isDozing() && !this.keyguardViewController.isBouncerShowing()) {
            if (this.statusBarStateController.getState() == 2) {
                return true;
            }
            if (this.statusBarStateController.getState() != 1 || !this.qsExpanded) {
                return false;
            }
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public final boolean isHomeScreenShadeVisibleToUser() {
        return !this.statusBarStateController.isDozing() && this.statusBarStateController.getState() == 0 && this.statusBarStateController.isExpanded();
    }

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/media/MediaHierarchyManager$Companion;", "", "()V", "IN_OVERLAY", "", "LOCATION_DREAM_OVERLAY", "LOCATION_LOCKSCREEN", "LOCATION_QQS", "LOCATION_QS", "TRANSFORMATION_TYPE_FADE", "TRANSFORMATION_TYPE_TRANSITION", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: MediaHierarchyManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* access modifiers changed from: private */
    public final void updateDesiredLocation(boolean z, boolean z2) {
        Trace.beginSection("MediaHierarchyManager#updateDesiredLocation");
        try {
            int calculateLocation = calculateLocation();
            int i = this.desiredLocation;
            if (calculateLocation != i || z2) {
                boolean z3 = false;
                if (i >= 0 && calculateLocation != i) {
                    this.previousLocation = i;
                } else if (z2) {
                    boolean z4 = !this.bypassController.getBypassEnabled() && this.statusbarState == 1;
                    if (calculateLocation == 0 && this.previousLocation == 2 && !z4) {
                        this.previousLocation = 1;
                    }
                }
                boolean z5 = this.desiredLocation == -1;
                this.desiredLocation = calculateLocation;
                boolean z6 = !z && shouldAnimateTransition(calculateLocation, this.previousLocation);
                Pair<Long, Long> animationParams = getAnimationParams(this.previousLocation, calculateLocation);
                long longValue = animationParams.component1().longValue();
                long longValue2 = animationParams.component2().longValue();
                MediaHost host = getHost(calculateLocation);
                if (calculateTransformationType() == 1) {
                    z3 = true;
                }
                if (!z3 || isCurrentlyInGuidedTransformation() || !z6) {
                    this.mediaCarouselController.onDesiredLocationChanged(calculateLocation, host, z6, longValue, longValue2);
                }
                performTransitionToNewLocation(z5, z6);
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0071 A[Catch:{ all -> 0x00d4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0073 A[Catch:{ all -> 0x00d4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x007a A[Catch:{ all -> 0x00d4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0090 A[Catch:{ all -> 0x00d4 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:48:0x00c6=Splitter:B:48:0x00c6, B:52:0x00cd=Splitter:B:52:0x00cd} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void performTransitionToNewLocation(boolean r6, boolean r7) {
        /*
            r5 = this;
            java.lang.String r0 = "MediaHierarchyManager#performTransitionToNewLocation"
            android.os.Trace.beginSection(r0)
            int r0 = r5.previousLocation     // Catch:{ all -> 0x00d4 }
            if (r0 < 0) goto L_0x00cd
            if (r6 == 0) goto L_0x000d
            goto L_0x00cd
        L_0x000d:
            int r6 = r5.desiredLocation     // Catch:{ all -> 0x00d4 }
            com.android.systemui.media.MediaHost r6 = r5.getHost(r6)     // Catch:{ all -> 0x00d4 }
            int r0 = r5.previousLocation     // Catch:{ all -> 0x00d4 }
            com.android.systemui.media.MediaHost r0 = r5.getHost(r0)     // Catch:{ all -> 0x00d4 }
            if (r6 == 0) goto L_0x00c6
            if (r0 != 0) goto L_0x001f
            goto L_0x00c6
        L_0x001f:
            r5.updateTargetState()     // Catch:{ all -> 0x00d4 }
            boolean r6 = r5.isCurrentlyInGuidedTransformation()     // Catch:{ all -> 0x00d4 }
            if (r6 == 0) goto L_0x002d
            r5.applyTargetStateIfNotAnimating()     // Catch:{ all -> 0x00d4 }
            goto L_0x00c0
        L_0x002d:
            if (r7 == 0) goto L_0x00bd
            boolean r6 = r5.isCrossFadeAnimatorRunning     // Catch:{ all -> 0x00d4 }
            float r7 = r5.animationCrossFadeProgress     // Catch:{ all -> 0x00d4 }
            android.animation.ValueAnimator r1 = r5.animator     // Catch:{ all -> 0x00d4 }
            r1.cancel()     // Catch:{ all -> 0x00d4 }
            int r1 = r5.currentAttachmentLocation     // Catch:{ all -> 0x00d4 }
            int r2 = r5.previousLocation     // Catch:{ all -> 0x00d4 }
            if (r1 != r2) goto L_0x005c
            com.android.systemui.util.animation.UniqueObjectHostView r1 = r0.getHostView()     // Catch:{ all -> 0x00d4 }
            boolean r1 = r1.isAttachedToWindow()     // Catch:{ all -> 0x00d4 }
            if (r1 != 0) goto L_0x0049
            goto L_0x005c
        L_0x0049:
            android.graphics.Rect r1 = r5.animationStartBounds     // Catch:{ all -> 0x00d4 }
            android.graphics.Rect r2 = r0.getCurrentBounds()     // Catch:{ all -> 0x00d4 }
            r1.set(r2)     // Catch:{ all -> 0x00d4 }
            android.graphics.Rect r1 = r5.animationStartClipping     // Catch:{ all -> 0x00d4 }
            android.graphics.Rect r0 = r0.getCurrentClipping()     // Catch:{ all -> 0x00d4 }
            r1.set(r0)     // Catch:{ all -> 0x00d4 }
            goto L_0x006a
        L_0x005c:
            android.graphics.Rect r0 = r5.animationStartBounds     // Catch:{ all -> 0x00d4 }
            android.graphics.Rect r1 = r5.currentBounds     // Catch:{ all -> 0x00d4 }
            r0.set(r1)     // Catch:{ all -> 0x00d4 }
            android.graphics.Rect r0 = r5.animationStartClipping     // Catch:{ all -> 0x00d4 }
            android.graphics.Rect r1 = r5.currentClipping     // Catch:{ all -> 0x00d4 }
            r0.set(r1)     // Catch:{ all -> 0x00d4 }
        L_0x006a:
            int r0 = r5.calculateTransformationType()     // Catch:{ all -> 0x00d4 }
            r1 = 1
            if (r0 != r1) goto L_0x0073
            r0 = r1
            goto L_0x0074
        L_0x0073:
            r0 = 0
        L_0x0074:
            int r2 = r5.previousLocation     // Catch:{ all -> 0x00d4 }
            r3 = 1065353216(0x3f800000, float:1.0)
            if (r6 == 0) goto L_0x0090
            int r6 = r5.currentAttachmentLocation     // Catch:{ all -> 0x00d4 }
            int r4 = r5.crossFadeAnimationEndLocation     // Catch:{ all -> 0x00d4 }
            if (r6 != r4) goto L_0x0085
            if (r0 == 0) goto L_0x009a
            float r7 = r3 - r7
            goto L_0x009b
        L_0x0085:
            int r6 = r5.crossFadeAnimationStartLocation     // Catch:{ all -> 0x00d4 }
            int r4 = r5.desiredLocation     // Catch:{ all -> 0x00d4 }
            if (r6 != r4) goto L_0x008e
            float r7 = r3 - r7
            goto L_0x009c
        L_0x008e:
            r0 = r1
            goto L_0x009c
        L_0x0090:
            if (r0 == 0) goto L_0x009a
            float r6 = r5.carouselAlpha     // Catch:{ all -> 0x00d4 }
            float r3 = r3 - r6
            r6 = 1073741824(0x40000000, float:2.0)
            float r7 = r3 / r6
            goto L_0x009b
        L_0x009a:
            r7 = 0
        L_0x009b:
            r6 = r2
        L_0x009c:
            r5.isCrossFadeAnimatorRunning = r0     // Catch:{ all -> 0x00d4 }
            r5.crossFadeAnimationStartLocation = r6     // Catch:{ all -> 0x00d4 }
            int r6 = r5.desiredLocation     // Catch:{ all -> 0x00d4 }
            r5.crossFadeAnimationEndLocation = r6     // Catch:{ all -> 0x00d4 }
            float r0 = r5.carouselAlpha     // Catch:{ all -> 0x00d4 }
            r5.animationStartAlpha = r0     // Catch:{ all -> 0x00d4 }
            r5.animationStartCrossFadeProgress = r7     // Catch:{ all -> 0x00d4 }
            r5.adjustAnimatorForTransition(r6, r2)     // Catch:{ all -> 0x00d4 }
            boolean r6 = r5.animationPending     // Catch:{ all -> 0x00d4 }
            if (r6 != 0) goto L_0x00c0
            android.view.View r6 = r5.rootView     // Catch:{ all -> 0x00d4 }
            if (r6 == 0) goto L_0x00c0
            r5.animationPending = r1     // Catch:{ all -> 0x00d4 }
            java.lang.Runnable r5 = r5.startAnimation     // Catch:{ all -> 0x00d4 }
            r6.postOnAnimation(r5)     // Catch:{ all -> 0x00d4 }
            goto L_0x00c0
        L_0x00bd:
            r5.cancelAnimationAndApplyDesiredState()     // Catch:{ all -> 0x00d4 }
        L_0x00c0:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00d4 }
            android.os.Trace.endSection()
            return
        L_0x00c6:
            r5.cancelAnimationAndApplyDesiredState()     // Catch:{ all -> 0x00d4 }
            android.os.Trace.endSection()
            return
        L_0x00cd:
            r5.cancelAnimationAndApplyDesiredState()     // Catch:{ all -> 0x00d4 }
            android.os.Trace.endSection()
            return
        L_0x00d4:
            r5 = move-exception
            android.os.Trace.endSection()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.performTransitionToNewLocation(boolean, boolean):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x002b A[Catch:{ all -> 0x0074 }] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002d A[Catch:{ all -> 0x0074 }] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0031 A[Catch:{ all -> 0x0074 }] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0032 A[Catch:{ all -> 0x0074 }] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0048 A[Catch:{ all -> 0x0074 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void applyState(android.graphics.Rect r2, float r3, boolean r4, android.graphics.Rect r5) {
        /*
            r1 = this;
            java.lang.String r0 = "MediaHierarchyManager#applyState"
            android.os.Trace.beginSection(r0)
            android.graphics.Rect r0 = r1.currentBounds     // Catch:{ all -> 0x0074 }
            r0.set(r2)     // Catch:{ all -> 0x0074 }
            r1.currentClipping = r5     // Catch:{ all -> 0x0074 }
            boolean r2 = r1.isCurrentlyFading()     // Catch:{ all -> 0x0074 }
            r5 = 1065353216(0x3f800000, float:1.0)
            if (r2 == 0) goto L_0x0015
            goto L_0x0016
        L_0x0015:
            r3 = r5
        L_0x0016:
            r1.setCarouselAlpha(r3)     // Catch:{ all -> 0x0074 }
            boolean r2 = r1.isCurrentlyInGuidedTransformation()     // Catch:{ all -> 0x0074 }
            if (r2 == 0) goto L_0x0028
            boolean r2 = r1.isCurrentlyFading()     // Catch:{ all -> 0x0074 }
            if (r2 == 0) goto L_0x0026
            goto L_0x0028
        L_0x0026:
            r2 = 0
            goto L_0x0029
        L_0x0028:
            r2 = 1
        L_0x0029:
            if (r2 == 0) goto L_0x002d
            r3 = -1
            goto L_0x002f
        L_0x002d:
            int r3 = r1.previousLocation     // Catch:{ all -> 0x0074 }
        L_0x002f:
            if (r2 == 0) goto L_0x0032
            goto L_0x0036
        L_0x0032:
            float r5 = r1.getTransformationProgress()     // Catch:{ all -> 0x0074 }
        L_0x0036:
            int r2 = r1.resolveLocationForFading()     // Catch:{ all -> 0x0074 }
            com.android.systemui.media.MediaCarouselController r0 = r1.mediaCarouselController     // Catch:{ all -> 0x0074 }
            r0.setCurrentState(r3, r2, r5, r4)     // Catch:{ all -> 0x0074 }
            r1.updateHostAttachment()     // Catch:{ all -> 0x0074 }
            int r2 = r1.currentAttachmentLocation     // Catch:{ all -> 0x0074 }
            r3 = -1000(0xfffffffffffffc18, float:NaN)
            if (r2 != r3) goto L_0x006e
            android.graphics.Rect r2 = r1.currentClipping     // Catch:{ all -> 0x0074 }
            boolean r2 = r2.isEmpty()     // Catch:{ all -> 0x0074 }
            if (r2 != 0) goto L_0x0057
            android.graphics.Rect r2 = r1.currentBounds     // Catch:{ all -> 0x0074 }
            android.graphics.Rect r3 = r1.currentClipping     // Catch:{ all -> 0x0074 }
            r2.intersect(r3)     // Catch:{ all -> 0x0074 }
        L_0x0057:
            android.view.ViewGroup r2 = r1.getMediaFrame()     // Catch:{ all -> 0x0074 }
            android.graphics.Rect r3 = r1.currentBounds     // Catch:{ all -> 0x0074 }
            int r3 = r3.left     // Catch:{ all -> 0x0074 }
            android.graphics.Rect r4 = r1.currentBounds     // Catch:{ all -> 0x0074 }
            int r4 = r4.top     // Catch:{ all -> 0x0074 }
            android.graphics.Rect r5 = r1.currentBounds     // Catch:{ all -> 0x0074 }
            int r5 = r5.right     // Catch:{ all -> 0x0074 }
            android.graphics.Rect r1 = r1.currentBounds     // Catch:{ all -> 0x0074 }
            int r1 = r1.bottom     // Catch:{ all -> 0x0074 }
            r2.setLeftTopRightBottom(r3, r4, r5, r1)     // Catch:{ all -> 0x0074 }
        L_0x006e:
            kotlin.Unit r1 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0074 }
            android.os.Trace.endSection()
            return
        L_0x0074:
            r1 = move-exception
            android.os.Trace.endSection()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.applyState(android.graphics.Rect, float, boolean, android.graphics.Rect):void");
    }

    private final void updateHostAttachment() {
        UniqueObjectHostView hostView;
        Trace.beginSection("MediaHierarchyManager#updateHostAttachment");
        try {
            int resolveLocationForFading = resolveLocationForFading();
            boolean z = true;
            boolean z2 = !isCurrentlyFading();
            if (this.isCrossFadeAnimatorRunning) {
                MediaHost host = getHost(resolveLocationForFading);
                if (host != null && host.getVisible()) {
                    MediaHost host2 = getHost(resolveLocationForFading);
                    if (((host2 == null || (hostView = host2.getHostView()) == null || hostView.isShown()) ? false : true) && resolveLocationForFading != this.desiredLocation) {
                        z2 = true;
                    }
                }
            }
            if (!isTransitionRunning() || this.rootOverlay == null || !z2) {
                z = false;
            }
            if (z) {
                resolveLocationForFading = -1000;
            }
            int i = resolveLocationForFading;
            if (this.currentAttachmentLocation != i) {
                this.currentAttachmentLocation = i;
                ViewGroup viewGroup = (ViewGroup) getMediaFrame().getParent();
                if (viewGroup != null) {
                    viewGroup.removeView(getMediaFrame());
                }
                if (z) {
                    ViewGroupOverlay viewGroupOverlay = this.rootOverlay;
                    Intrinsics.checkNotNull(viewGroupOverlay);
                    viewGroupOverlay.add(getMediaFrame());
                } else {
                    MediaHost host3 = getHost(i);
                    Intrinsics.checkNotNull(host3);
                    UniqueObjectHostView hostView2 = host3.getHostView();
                    hostView2.addView(getMediaFrame());
                    int paddingLeft = hostView2.getPaddingLeft();
                    int paddingTop = hostView2.getPaddingTop();
                    getMediaFrame().setLeftTopRightBottom(paddingLeft, paddingTop, this.currentBounds.width() + paddingLeft, this.currentBounds.height() + paddingTop);
                }
                if (this.isCrossFadeAnimatorRunning) {
                    MediaCarouselController.onDesiredLocationChanged$default(this.mediaCarouselController, i, getHost(i), false, 0, 0, 24, (Object) null);
                }
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }
}
