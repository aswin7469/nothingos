package com.android.systemui.animation;

import android.view.RemoteAnimationAdapter;
import com.android.systemui.animation.ActivityLaunchAnimator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ActivityLaunchAnimator.kt */
/* loaded from: classes.dex */
public final class ActivityLaunchAnimator$startPendingIntentWithAnimation$1 extends Lambda implements Function1<RemoteAnimationAdapter, Integer> {
    final /* synthetic */ ActivityLaunchAnimator.PendingIntentStarter $intentStarter;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ActivityLaunchAnimator$startPendingIntentWithAnimation$1(ActivityLaunchAnimator.PendingIntentStarter pendingIntentStarter) {
        super(1);
        this.$intentStarter = pendingIntentStarter;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Integer mo1949invoke(RemoteAnimationAdapter remoteAnimationAdapter) {
        return Integer.valueOf(invoke2(remoteAnimationAdapter));
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final int invoke2(@Nullable RemoteAnimationAdapter remoteAnimationAdapter) {
        return this.$intentStarter.startPendingIntent(remoteAnimationAdapter);
    }
}
