package com.android.systemui.statusbar.phone;

import android.view.View;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0017\b\u0007\u0018\u00002\u00020\u0001B%\b\u0007\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u0006\u0010\u000f\u001a\u00020\u0010J\u0018\u0010\u0011\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u0016\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010\u001c\u001a\u00020\u0010J\u0006\u0010\u001d\u001a\u00020\u0010J\u000e\u0010\u001e\u001a\u00020\u00172\u0006\u0010\u001f\u001a\u00020\u0010J\u000e\u0010 \u001a\u00020\u00172\u0006\u0010!\u001a\u00020\u0010J\b\u0010\"\u001a\u00020\u0017H\u0016J\u0006\u0010#\u001a\u00020\u0010J\u0006\u0010$\u001a\u00020\u0010J\u0006\u0010%\u001a\u00020\u0010J\u0006\u0010&\u001a\u00020\u0010J\u0006\u0010'\u001a\u00020\u0010J\u0006\u0010(\u001a\u00020\u0010J\u0006\u0010)\u001a\u00020\u0010J\u0006\u0010*\u001a\u00020\u0010J\u0006\u0010+\u001a\u00020\u0010J\u0006\u0010,\u001a\u00020\u0010J\u0006\u0010-\u001a\u00020\u0010J\u0006\u0010.\u001a\u00020\u0010J\u0006\u0010/\u001a\u00020\u0010J\u0006\u00100\u001a\u00020\u0010J\u0006\u00101\u001a\u00020\u0010R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u00062"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/ScreenOffAnimationController;", "Lcom/android/systemui/keyguard/WakefulnessLifecycle$Observer;", "sysUiUnfoldComponent", "Ljava/util/Optional;", "Lcom/android/systemui/unfold/SysUIUnfoldComponent;", "unlockedScreenOffAnimation", "Lcom/android/systemui/statusbar/phone/UnlockedScreenOffAnimationController;", "wakefulnessLifecycle", "Lcom/android/systemui/keyguard/WakefulnessLifecycle;", "(Ljava/util/Optional;Lcom/android/systemui/statusbar/phone/UnlockedScreenOffAnimationController;Lcom/android/systemui/keyguard/WakefulnessLifecycle;)V", "animations", "", "Lcom/android/systemui/statusbar/phone/ScreenOffAnimation;", "foldToAodAnimation", "Lcom/android/systemui/unfold/FoldAodAnimationController;", "allowWakeUpIfDozing", "", "animateInKeyguard", "keyguardView", "Landroid/view/View;", "after", "Ljava/lang/Runnable;", "initialize", "", "centralSurfaces", "Lcom/android/systemui/statusbar/phone/CentralSurfaces;", "lightRevealScrim", "Lcom/android/systemui/statusbar/LightRevealScrim;", "isKeyguardHideDelayed", "isKeyguardShowDelayed", "onAlwaysOnChanged", "alwaysOn", "onScrimOpaqueChanged", "isOpaque", "onStartedGoingToSleep", "overrideNotificationsFullyDozingOnKeyguard", "shouldAnimateAodIcons", "shouldAnimateClockChange", "shouldAnimateDozingChange", "shouldAnimateInKeyguard", "shouldClampDozeScreenBrightness", "shouldControlUnlockedScreenOff", "shouldDelayDisplayDozeTransition", "shouldDelayKeyguardShow", "shouldExpandNotifications", "shouldHideLightRevealScrimOnWakeUp", "shouldHideNotificationsFooter", "shouldIgnoreKeyguardTouches", "shouldShowAodIconsWhenShade", "shouldShowLightRevealScrim", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ScreenOffAnimationController.kt */
public final class ScreenOffAnimationController implements WakefulnessLifecycle.Observer {
    private final List<ScreenOffAnimation> animations;
    private final FoldAodAnimationController foldToAodAnimation;
    private final WakefulnessLifecycle wakefulnessLifecycle;

    @Inject
    public ScreenOffAnimationController(Optional<SysUIUnfoldComponent> optional, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, WakefulnessLifecycle wakefulnessLifecycle2) {
        Intrinsics.checkNotNullParameter(optional, "sysUiUnfoldComponent");
        Intrinsics.checkNotNullParameter(unlockedScreenOffAnimationController, "unlockedScreenOffAnimation");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle2, "wakefulnessLifecycle");
        this.wakefulnessLifecycle = wakefulnessLifecycle2;
        FoldAodAnimationController foldAodAnimationController = null;
        SysUIUnfoldComponent orElse = optional.orElse(null);
        foldAodAnimationController = orElse != null ? orElse.getFoldAodAnimationController() : foldAodAnimationController;
        this.foldToAodAnimation = foldAodAnimationController;
        this.animations = CollectionsKt.listOfNotNull((T[]) new ScreenOffAnimation[]{foldAodAnimationController, unlockedScreenOffAnimationController});
    }

    public final void initialize(CentralSurfaces centralSurfaces, LightRevealScrim lightRevealScrim) {
        Intrinsics.checkNotNullParameter(centralSurfaces, "centralSurfaces");
        Intrinsics.checkNotNullParameter(lightRevealScrim, "lightRevealScrim");
        for (ScreenOffAnimation initialize : this.animations) {
            initialize.initialize(centralSurfaces, lightRevealScrim);
        }
        this.wakefulnessLifecycle.addObserver(this);
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0008 A[LOOP:0: B:1:0x0008->B:4:0x0018, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onStartedGoingToSleep() {
        /*
            r1 = this;
            java.util.List<com.android.systemui.statusbar.phone.ScreenOffAnimation> r1 = r1.animations
            java.lang.Iterable r1 = (java.lang.Iterable) r1
            java.util.Iterator r1 = r1.iterator()
        L_0x0008:
            boolean r0 = r1.hasNext()
            if (r0 == 0) goto L_0x001a
            java.lang.Object r0 = r1.next()
            com.android.systemui.statusbar.phone.ScreenOffAnimation r0 = (com.android.systemui.statusbar.phone.ScreenOffAnimation) r0
            boolean r0 = r0.startAnimation()
            if (r0 == 0) goto L_0x0008
        L_0x001a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.ScreenOffAnimationController.onStartedGoingToSleep():void");
    }

    public final void onScrimOpaqueChanged(boolean z) {
        for (ScreenOffAnimation onScrimOpaqueChanged : this.animations) {
            onScrimOpaqueChanged.onScrimOpaqueChanged(z);
        }
    }

    public final void onAlwaysOnChanged(boolean z) {
        for (ScreenOffAnimation onAlwaysOnChanged : this.animations) {
            onAlwaysOnChanged.onAlwaysOnChanged(z);
        }
    }

    public final boolean shouldControlUnlockedScreenOff() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldPlayAnimation : iterable) {
            if (shouldPlayAnimation.shouldPlayAnimation()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldExpandNotifications() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation isAnimationPlaying : iterable) {
            if (isAnimationPlaying.isAnimationPlaying()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldAnimateInKeyguard() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldAnimateInKeyguard : iterable) {
            if (shouldAnimateInKeyguard.shouldAnimateInKeyguard()) {
                return true;
            }
        }
        return false;
    }

    public final ScreenOffAnimation animateInKeyguard(View view, Runnable runnable) {
        Object obj;
        boolean z;
        Intrinsics.checkNotNullParameter(view, "keyguardView");
        Intrinsics.checkNotNullParameter(runnable, "after");
        Iterator it = this.animations.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            ScreenOffAnimation screenOffAnimation = (ScreenOffAnimation) obj;
            if (screenOffAnimation.shouldAnimateInKeyguard()) {
                screenOffAnimation.animateInKeyguard(view, runnable);
                z = true;
                continue;
            } else {
                z = false;
                continue;
            }
            if (z) {
                break;
            }
        }
        return (ScreenOffAnimation) obj;
    }

    public final boolean shouldIgnoreKeyguardTouches() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation isAnimationPlaying : iterable) {
            if (isAnimationPlaying.isAnimationPlaying()) {
                return true;
            }
        }
        return false;
    }

    public final boolean allowWakeUpIfDozing() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return true;
        }
        for (ScreenOffAnimation isAnimationPlaying : iterable) {
            if (!(!isAnimationPlaying.isAnimationPlaying())) {
                return false;
            }
        }
        return true;
    }

    public final boolean shouldDelayKeyguardShow() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldDelayKeyguardShow : iterable) {
            if (shouldDelayKeyguardShow.shouldDelayKeyguardShow()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isKeyguardShowDelayed() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation isKeyguardShowDelayed : iterable) {
            if (isKeyguardShowDelayed.isKeyguardShowDelayed()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isKeyguardHideDelayed() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation isKeyguardHideDelayed : iterable) {
            if (isKeyguardHideDelayed.isKeyguardHideDelayed()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldShowLightRevealScrim() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldPlayAnimation : iterable) {
            if (shouldPlayAnimation.shouldPlayAnimation()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldHideLightRevealScrimOnWakeUp() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldHideScrimOnWakeUp : iterable) {
            if (shouldHideScrimOnWakeUp.shouldHideScrimOnWakeUp()) {
                return true;
            }
        }
        return false;
    }

    public final boolean overrideNotificationsFullyDozingOnKeyguard() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation overrideNotificationsDozeAmount : iterable) {
            if (overrideNotificationsDozeAmount.overrideNotificationsDozeAmount()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldHideNotificationsFooter() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation isAnimationPlaying : iterable) {
            if (isAnimationPlaying.isAnimationPlaying()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldClampDozeScreenBrightness() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldPlayAnimation : iterable) {
            if (shouldPlayAnimation.shouldPlayAnimation()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldShowAodIconsWhenShade() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldShowAodIconsWhenShade : iterable) {
            if (shouldShowAodIconsWhenShade.shouldShowAodIconsWhenShade()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldAnimateAodIcons() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return true;
        }
        for (ScreenOffAnimation shouldAnimateAodIcons : iterable) {
            if (!shouldAnimateAodIcons.shouldAnimateAodIcons()) {
                return false;
            }
        }
        return true;
    }

    public final boolean shouldAnimateDozingChange() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return true;
        }
        for (ScreenOffAnimation shouldAnimateDozingChange : iterable) {
            if (!shouldAnimateDozingChange.shouldAnimateDozingChange()) {
                return false;
            }
        }
        return true;
    }

    public final boolean shouldDelayDisplayDozeTransition() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldDelayDisplayDozeTransition : iterable) {
            if (shouldDelayDisplayDozeTransition.shouldDelayDisplayDozeTransition()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldAnimateClockChange() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return true;
        }
        for (ScreenOffAnimation shouldAnimateClockChange : iterable) {
            if (!shouldAnimateClockChange.shouldAnimateClockChange()) {
                return false;
            }
        }
        return true;
    }
}
