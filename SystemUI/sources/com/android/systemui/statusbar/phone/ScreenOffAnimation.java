package com.android.systemui.statusbar.phone;

import android.view.View;
import com.android.systemui.statusbar.LightRevealScrim;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0012\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0018\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u000eH\u0016J\b\u0010\u0010\u001a\u00020\u000eH\u0016J\u0010\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u000eH\u0016J\u0010\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u000eH\u0016J\b\u0010\u0015\u001a\u00020\u000eH\u0016J\b\u0010\u0016\u001a\u00020\u000eH\u0016J\b\u0010\u0017\u001a\u00020\u000eH\u0016J\b\u0010\u0018\u001a\u00020\u000eH\u0016J\b\u0010\u0019\u001a\u00020\u000eH\u0016J\b\u0010\u001a\u001a\u00020\u000eH\u0016J\b\u0010\u001b\u001a\u00020\u000eH\u0016J\b\u0010\u001c\u001a\u00020\u000eH\u0016J\b\u0010\u001d\u001a\u00020\u000eH\u0016J\b\u0010\u001e\u001a\u00020\u000eH\u0016J\b\u0010\u001f\u001a\u00020\u000eH\u0016ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006 À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/ScreenOffAnimation;", "", "animateInKeyguard", "", "keyguardView", "Landroid/view/View;", "after", "Ljava/lang/Runnable;", "initialize", "centralSurfaces", "Lcom/android/systemui/statusbar/phone/CentralSurfaces;", "lightRevealScrim", "Lcom/android/systemui/statusbar/LightRevealScrim;", "isAnimationPlaying", "", "isKeyguardHideDelayed", "isKeyguardShowDelayed", "onAlwaysOnChanged", "alwaysOn", "onScrimOpaqueChanged", "isOpaque", "overrideNotificationsDozeAmount", "shouldAnimateAodIcons", "shouldAnimateClockChange", "shouldAnimateDozingChange", "shouldAnimateInKeyguard", "shouldDelayDisplayDozeTransition", "shouldDelayKeyguardShow", "shouldHideScrimOnWakeUp", "shouldPlayAnimation", "shouldShowAodIconsWhenShade", "startAnimation", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ScreenOffAnimationController.kt */
public interface ScreenOffAnimation {
    void initialize(CentralSurfaces centralSurfaces, LightRevealScrim lightRevealScrim) {
        Intrinsics.checkNotNullParameter(centralSurfaces, "centralSurfaces");
        Intrinsics.checkNotNullParameter(lightRevealScrim, "lightRevealScrim");
    }

    boolean isAnimationPlaying() {
        return false;
    }

    boolean isKeyguardHideDelayed() {
        return false;
    }

    boolean isKeyguardShowDelayed() {
        return false;
    }

    void onAlwaysOnChanged(boolean z) {
    }

    void onScrimOpaqueChanged(boolean z) {
    }

    boolean overrideNotificationsDozeAmount() {
        return false;
    }

    boolean shouldAnimateAodIcons() {
        return true;
    }

    boolean shouldAnimateClockChange() {
        return true;
    }

    boolean shouldAnimateDozingChange() {
        return true;
    }

    boolean shouldAnimateInKeyguard() {
        return false;
    }

    boolean shouldDelayDisplayDozeTransition() {
        return false;
    }

    boolean shouldDelayKeyguardShow() {
        return false;
    }

    boolean shouldHideScrimOnWakeUp() {
        return false;
    }

    boolean shouldPlayAnimation() {
        return false;
    }

    boolean shouldShowAodIconsWhenShade() {
        return false;
    }

    boolean startAnimation() {
        return false;
    }

    void animateInKeyguard(View view, Runnable runnable) {
        Intrinsics.checkNotNullParameter(view, "keyguardView");
        Intrinsics.checkNotNullParameter(runnable, "after");
        runnable.run();
    }
}
