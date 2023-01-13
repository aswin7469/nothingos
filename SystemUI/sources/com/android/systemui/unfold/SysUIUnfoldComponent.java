package com.android.systemui.unfold;

import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import dagger.BindsInstance;
import dagger.Subcomponent;
import kotlin.Metadata;

@Subcomponent
@Metadata(mo65042d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001:\u0001\u000eJ\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\u000bH&J\b\u0010\f\u001a\u00020\rH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000fÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/unfold/SysUIUnfoldComponent;", "", "getFoldAodAnimationController", "Lcom/android/systemui/unfold/FoldAodAnimationController;", "getKeyguardUnfoldTransition", "Lcom/android/keyguard/KeyguardUnfoldTransition;", "getNotificationPanelUnfoldAnimationController", "Lcom/android/systemui/statusbar/phone/NotificationPanelUnfoldAnimationController;", "getStatusBarMoveFromCenterAnimationController", "Lcom/android/systemui/statusbar/phone/StatusBarMoveFromCenterAnimationController;", "getUnfoldLightRevealOverlayAnimation", "Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation;", "getUnfoldTransitionWallpaperController", "Lcom/android/systemui/unfold/UnfoldTransitionWallpaperController;", "Factory", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@SysUIUnfoldScope
/* compiled from: SysUIUnfoldModule.kt */
public interface SysUIUnfoldComponent {

    @Subcomponent.Factory
    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J&\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00072\b\b\u0001\u0010\b\u001a\u00020\tH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/unfold/SysUIUnfoldComponent$Factory;", "", "create", "Lcom/android/systemui/unfold/SysUIUnfoldComponent;", "p1", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;", "p2", "Lcom/android/systemui/unfold/util/NaturalRotationUnfoldProgressProvider;", "p3", "Lcom/android/systemui/unfold/util/ScopedUnfoldTransitionProgressProvider;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: SysUIUnfoldModule.kt */
    public interface Factory {
        SysUIUnfoldComponent create(@BindsInstance UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, @BindsInstance NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, @BindsInstance ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider);
    }

    FoldAodAnimationController getFoldAodAnimationController();

    KeyguardUnfoldTransition getKeyguardUnfoldTransition();

    NotificationPanelUnfoldAnimationController getNotificationPanelUnfoldAnimationController();

    StatusBarMoveFromCenterAnimationController getStatusBarMoveFromCenterAnimationController();

    UnfoldLightRevealOverlayAnimation getUnfoldLightRevealOverlayAnimation();

    UnfoldTransitionWallpaperController getUnfoldTransitionWallpaperController();
}
