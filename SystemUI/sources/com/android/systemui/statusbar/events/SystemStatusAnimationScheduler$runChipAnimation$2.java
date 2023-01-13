package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.nothing.systemui.util.NTLogUtil;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/statusbar/events/SystemStatusAnimationScheduler$runChipAnimation$2", "Landroid/animation/AnimatorListenerAdapter;", "onAnimationEnd", "", "animation", "Landroid/animation/Animator;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SystemStatusAnimationScheduler.kt */
public final class SystemStatusAnimationScheduler$runChipAnimation$2 extends AnimatorListenerAdapter {
    final /* synthetic */ SystemStatusAnimationScheduler this$0;

    SystemStatusAnimationScheduler$runChipAnimation$2(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        this.this$0 = systemStatusAnimationScheduler;
    }

    public void onAnimationEnd(Animator animator) {
        NTLogUtil.m1686d("SystemStatusAnimationScheduler", "animSet onAnimationEnd");
        this.this$0.animationState = 3;
    }
}
