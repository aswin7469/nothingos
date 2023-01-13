package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.nothing.systemui.util.NTLogUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/statusbar/events/SystemStatusAnimationScheduler$runChipAnimation$3$1", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "onAnimationStart", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SystemStatusAnimationScheduler.kt */
public final class SystemStatusAnimationScheduler$runChipAnimation$3$1 extends AnimatorListenerAdapter {
    final /* synthetic */ SystemStatusAnimationScheduler this$0;

    SystemStatusAnimationScheduler$runChipAnimation$3$1(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        this.this$0 = systemStatusAnimationScheduler;
    }

    public void onAnimationStart(Animator animator) {
        super.onAnimationStart(animator);
        NTLogUtil.m1686d("SystemStatusAnimationScheduler", "animSet2 onAnimationStart");
        this.this$0.executor.executeDelayed(new C2642x7ed7a1c4(this.this$0), 50);
    }

    /* access modifiers changed from: private */
    /* renamed from: onAnimationStart$lambda-0  reason: not valid java name */
    public static final void m3082onAnimationStart$lambda0(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        Intrinsics.checkNotNullParameter(systemStatusAnimationScheduler, "this$0");
        systemStatusAnimationScheduler.statusBarWindowController.setForceStatusBarVisible(false);
    }

    public void onAnimationEnd(Animator animator) {
        NTLogUtil.m1686d("SystemStatusAnimationScheduler", "animSet2 onAnimationEnd, hasPersistentDot: " + this.this$0.getHasPersistentDot());
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.this$0;
        systemStatusAnimationScheduler.animationState = systemStatusAnimationScheduler.getHasPersistentDot() ? 5 : 0;
    }
}
