package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.statusbar.LightRevealScrim;
import com.nothing.systemui.util.NTLogUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0007\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/statusbar/phone/UnlockedScreenOffAnimationController$lightRevealAnimator$1$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationCancel", "", "animation", "Landroid/animation/Animator;", "onAnimationEnd", "onAnimationStart", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UnlockedScreenOffAnimationController.kt */
public final class UnlockedScreenOffAnimationController$lightRevealAnimator$1$2 extends AnimatorListenerAdapter {
    final /* synthetic */ UnlockedScreenOffAnimationController this$0;

    UnlockedScreenOffAnimationController$lightRevealAnimator$1$2(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        this.this$0 = unlockedScreenOffAnimationController;
    }

    public void onAnimationCancel(Animator animator) {
        NTLogUtil.m1686d("UnlockedScreenOffAnimationController", "onAnimationCancel");
        LightRevealScrim access$getLightRevealScrim$p = this.this$0.lightRevealScrim;
        CentralSurfaces centralSurfaces = null;
        if (access$getLightRevealScrim$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lightRevealScrim");
            access$getLightRevealScrim$p = null;
        }
        if (access$getLightRevealScrim$p.isScrimAlmostOccludes()) {
            LightRevealScrim access$getLightRevealScrim$p2 = this.this$0.lightRevealScrim;
            if (access$getLightRevealScrim$p2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("lightRevealScrim");
                access$getLightRevealScrim$p2 = null;
            }
            access$getLightRevealScrim$p2.setRevealAmount(0.0f);
        } else {
            LightRevealScrim access$getLightRevealScrim$p3 = this.this$0.lightRevealScrim;
            if (access$getLightRevealScrim$p3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("lightRevealScrim");
                access$getLightRevealScrim$p3 = null;
            }
            access$getLightRevealScrim$p3.setRevealAmount(1.0f);
        }
        CentralSurfaces access$getMCentralSurfaces$p = this.this$0.mCentralSurfaces;
        if (access$getMCentralSurfaces$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
        } else {
            centralSurfaces = access$getMCentralSurfaces$p;
        }
        centralSurfaces.unlockedScreenOffAnimationCancel();
    }

    public void onAnimationEnd(Animator animator) {
        NTLogUtil.m1686d("UnlockedScreenOffAnimationController", "onAnimationEnd");
        this.this$0.lightRevealAnimationPlaying = false;
        this.this$0.interactionJankMonitor.end(40);
    }

    public void onAnimationStart(Animator animator) {
        NTLogUtil.m1686d("UnlockedScreenOffAnimationController", "onAnimationStart");
        InteractionJankMonitor access$getInteractionJankMonitor$p = this.this$0.interactionJankMonitor;
        CentralSurfaces access$getMCentralSurfaces$p = this.this$0.mCentralSurfaces;
        if (access$getMCentralSurfaces$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
            access$getMCentralSurfaces$p = null;
        }
        access$getInteractionJankMonitor$p.begin(access$getMCentralSurfaces$p.getNotificationShadeWindowView(), 40);
    }
}
