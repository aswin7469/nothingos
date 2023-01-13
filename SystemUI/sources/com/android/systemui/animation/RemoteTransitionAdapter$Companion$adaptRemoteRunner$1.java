package com.android.systemui.animation;

import android.os.IBinder;
import android.view.IRemoteAnimationRunner;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.TransitionInfo;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000-\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH\u0016J(\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0016¨\u0006\u000e"}, mo65043d2 = {"com/android/systemui/animation/RemoteTransitionAdapter$Companion$adaptRemoteRunner$1", "Landroid/window/IRemoteTransition$Stub;", "mergeAnimation", "", "token", "Landroid/os/IBinder;", "info", "Landroid/window/TransitionInfo;", "t", "Landroid/view/SurfaceControl$Transaction;", "mergeTarget", "finishCallback", "Landroid/window/IRemoteTransitionFinishedCallback;", "startAnimation", "animation_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RemoteTransitionAdapter.kt */
public final class RemoteTransitionAdapter$Companion$adaptRemoteRunner$1 extends IRemoteTransition.Stub {
    final /* synthetic */ IRemoteAnimationRunner $runner;

    public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        Intrinsics.checkNotNullParameter(transitionInfo, "info");
        Intrinsics.checkNotNullParameter(transaction, "t");
        Intrinsics.checkNotNullParameter(iBinder2, "mergeTarget");
        Intrinsics.checkNotNullParameter(iRemoteTransitionFinishedCallback, "finishCallback");
    }

    RemoteTransitionAdapter$Companion$adaptRemoteRunner$1(IRemoteAnimationRunner iRemoteAnimationRunner) {
        this.$runner = iRemoteAnimationRunner;
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x00d2 A[LOOP:0: B:3:0x004d->B:26:0x00d2, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x00c9 A[EDGE_INSN: B:72:0x00c9->B:25:0x00c9 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startAnimation(android.os.IBinder r23, android.window.TransitionInfo r24, android.view.SurfaceControl.Transaction r25, android.window.IRemoteTransitionFinishedCallback r26) {
        /*
            r22 = this;
            r3 = r24
            r0 = r25
            java.lang.String r1 = "token"
            r2 = r23
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r1)
            java.lang.String r1 = "info"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r1)
            java.lang.String r1 = "t"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r1)
            java.lang.String r1 = "finishCallback"
            r10 = r26
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r1)
            android.util.ArrayMap r11 = new android.util.ArrayMap
            r11.<init>()
            com.android.systemui.animation.RemoteTransitionAdapter$Companion r1 = com.android.systemui.animation.RemoteTransitionAdapter.Companion
            r2 = 0
            android.view.RemoteAnimationTarget[] r12 = r1.wrapTargets(r3, r2, r0, r11)
            com.android.systemui.animation.RemoteTransitionAdapter$Companion r1 = com.android.systemui.animation.RemoteTransitionAdapter.Companion
            r4 = 1
            android.view.RemoteAnimationTarget[] r13 = r1.wrapTargets(r3, r4, r0, r11)
            android.view.RemoteAnimationTarget[] r14 = new android.view.RemoteAnimationTarget[r2]
            java.util.List r1 = r24.getChanges()
            int r1 = r1.size()
            r15 = -1
            int r1 = r1 + r15
            r9 = 3
            r8 = 2
            r5 = 0
            r6 = 0
            if (r1 < 0) goto L_0x00d8
            r17 = r2
            r18 = r17
            r19 = r18
            r7 = r6
            r16 = r7
            r6 = r5
        L_0x004d:
            int r20 = r1 + -1
            java.util.List r2 = r24.getChanges()
            java.lang.Object r2 = r2.get(r1)
            android.window.TransitionInfo$Change r2 = (android.window.TransitionInfo.Change) r2
            android.app.ActivityManager$RunningTaskInfo r21 = r2.getTaskInfo()
            if (r21 == 0) goto L_0x008a
            android.app.ActivityManager$RunningTaskInfo r21 = r2.getTaskInfo()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r21)
            int r15 = r21.getActivityType()
            if (r15 != r8) goto L_0x008a
            int r5 = r2.getMode()
            if (r5 == r4) goto L_0x007c
            int r5 = r2.getMode()
            if (r5 != r9) goto L_0x0079
            goto L_0x007c
        L_0x0079:
            r17 = 0
            goto L_0x007e
        L_0x007c:
            r17 = r4
        L_0x007e:
            java.util.List r5 = r24.getChanges()
            int r5 = r5.size()
            int r18 = r5 - r1
            r5 = r2
            goto L_0x0092
        L_0x008a:
            int r1 = r2.getFlags()
            r1 = r1 & r8
            if (r1 == 0) goto L_0x0092
            r6 = r2
        L_0x0092:
            android.window.WindowContainerToken r1 = r2.getParent()
            if (r1 != 0) goto L_0x00c7
            int r1 = r2.getEndRotation()
            if (r1 < 0) goto L_0x00c7
            int r1 = r2.getEndRotation()
            int r15 = r2.getStartRotation()
            if (r1 == r15) goto L_0x00c7
            int r1 = r2.getEndRotation()
            int r7 = r2.getStartRotation()
            int r19 = r1 - r7
            android.graphics.Rect r1 = r2.getEndAbsBounds()
            int r1 = r1.width()
            float r1 = (float) r1
            android.graphics.Rect r2 = r2.getEndAbsBounds()
            int r2 = r2.height()
            float r2 = (float) r2
            r7 = r1
            r16 = r2
        L_0x00c7:
            if (r20 >= 0) goto L_0x00d2
            r1 = r5
            r15 = r6
            r2 = r18
            r18 = r16
            r16 = r7
            goto L_0x00e3
        L_0x00d2:
            r1 = r20
            r2 = 0
            r15 = -1
            goto L_0x004d
        L_0x00d8:
            r1 = r5
            r15 = r1
            r16 = r6
            r18 = r16
            r2 = 0
            r17 = 0
            r19 = 0
        L_0x00e3:
            com.android.systemui.animation.RemoteTransitionAdapter$CounterRotator r7 = new com.android.systemui.animation.RemoteTransitionAdapter$CounterRotator
            r7.<init>()
            com.android.systemui.animation.RemoteTransitionAdapter$CounterRotator r6 = new com.android.systemui.animation.RemoteTransitionAdapter$CounterRotator
            r6.<init>()
            if (r1 == 0) goto L_0x0136
            if (r19 == 0) goto L_0x0136
            android.window.WindowContainerToken r4 = r1.getParent()
            if (r4 == 0) goto L_0x0136
            android.window.WindowContainerToken r4 = r1.getParent()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            android.window.TransitionInfo$Change r4 = r3.getChange(r4)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            android.view.SurfaceControl r5 = r4.getLeash()
            java.lang.String r4 = "info.getChange(launcherTask.parent!!)!!.leash"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, r4)
            r4 = r7
            r20 = r5
            r5 = r25
            r23 = r6
            r6 = r20
            r20 = r7
            r7 = r19
            r10 = r8
            r8 = r16
            r21 = r9
            r9 = r18
            r4.setup(r5, r6, r7, r8, r9)
            android.view.SurfaceControl r4 = r20.getSurface()
            if (r4 == 0) goto L_0x013d
            android.view.SurfaceControl r4 = r20.getSurface()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            r0.setLayer(r4, r2)
            goto L_0x013d
        L_0x0136:
            r23 = r6
            r20 = r7
            r10 = r8
            r21 = r9
        L_0x013d:
            if (r17 == 0) goto L_0x01d4
            android.view.SurfaceControl r1 = r20.getSurface()
            if (r1 == 0) goto L_0x0159
            android.view.SurfaceControl r1 = r20.getSurface()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            java.util.List r2 = r24.getChanges()
            int r2 = r2.size()
            int r2 = r2 * 3
            r0.setLayer(r1, r2)
        L_0x0159:
            java.util.List r1 = r24.getChanges()
            int r1 = r1.size()
            r2 = -1
            int r1 = r1 + r2
            if (r1 < 0) goto L_0x01b5
        L_0x0165:
            int r2 = r1 + -1
            java.util.List r4 = r24.getChanges()
            java.lang.Object r4 = r4.get(r1)
            android.window.TransitionInfo$Change r4 = (android.window.TransitionInfo.Change) r4
            android.view.SurfaceControl r5 = r4.getLeash()
            java.lang.Object r5 = r11.get(r5)
            android.view.SurfaceControl r5 = (android.view.SurfaceControl) r5
            java.util.List r6 = r24.getChanges()
            java.lang.Object r6 = r6.get(r1)
            android.window.TransitionInfo$Change r6 = (android.window.TransitionInfo.Change) r6
            int r6 = r6.getMode()
            boolean r4 = android.window.TransitionInfo.isIndependent(r4, r3)
            if (r4 == 0) goto L_0x01ac
            if (r6 == r10) goto L_0x0195
            r4 = 4
            if (r6 == r4) goto L_0x0195
            goto L_0x01ac
        L_0x0195:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r5)
            java.util.List r4 = r24.getChanges()
            int r4 = r4.size()
            int r4 = r4 * 3
            int r4 = r4 - r1
            r0.setLayer(r5, r4)
            r9 = r20
            r9.addChild(r0, r5)
            goto L_0x01ae
        L_0x01ac:
            r9 = r20
        L_0x01ae:
            if (r2 >= 0) goto L_0x01b1
            goto L_0x01b7
        L_0x01b1:
            r1 = r2
            r20 = r9
            goto L_0x0165
        L_0x01b5:
            r9 = r20
        L_0x01b7:
            int r1 = r13.length
            r2 = -1
            int r1 = r1 + r2
            if (r1 < 0) goto L_0x0238
        L_0x01bc:
            int r2 = r1 + -1
            r4 = r13[r1]
            android.view.SurfaceControl r4 = r4.leash
            r0.show(r4)
            r1 = r13[r1]
            android.view.SurfaceControl r1 = r1.leash
            r4 = 1065353216(0x3f800000, float:1.0)
            r0.setAlpha(r1, r4)
            if (r2 >= 0) goto L_0x01d2
            goto L_0x0238
        L_0x01d2:
            r1 = r2
            goto L_0x01bc
        L_0x01d4:
            r9 = r20
            if (r1 == 0) goto L_0x01e5
            android.view.SurfaceControl r1 = r1.getLeash()
            java.lang.Object r1 = r11.get(r1)
            android.view.SurfaceControl r1 = (android.view.SurfaceControl) r1
            r9.addChild(r0, r1)
        L_0x01e5:
            if (r15 == 0) goto L_0x0238
            if (r19 == 0) goto L_0x0238
            android.window.WindowContainerToken r1 = r15.getParent()
            if (r1 == 0) goto L_0x0238
            android.window.WindowContainerToken r1 = r15.getParent()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            android.window.TransitionInfo$Change r1 = r3.getChange(r1)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            android.view.SurfaceControl r6 = r1.getLeash()
            java.lang.String r1 = "info.getChange(wallpaper.parent!!)!!.leash"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, r1)
            r4 = r23
            r5 = r25
            r7 = r19
            r8 = r16
            r1 = r9
            r9 = r18
            r4.setup(r5, r6, r7, r8, r9)
            android.view.SurfaceControl r2 = r23.getSurface()
            if (r2 == 0) goto L_0x0235
            android.view.SurfaceControl r2 = r23.getSurface()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r2)
            r4 = -1
            r0.setLayer(r2, r4)
            android.view.SurfaceControl r2 = r15.getLeash()
            java.lang.Object r2 = r11.get(r2)
            android.view.SurfaceControl r2 = (android.view.SurfaceControl) r2
            r4 = r23
            r4.addChild(r0, r2)
            goto L_0x023b
        L_0x0235:
            r4 = r23
            goto L_0x023b
        L_0x0238:
            r4 = r23
            r1 = r9
        L_0x023b:
            r25.apply()
            com.android.systemui.animation.RemoteTransitionAdapter$Companion$adaptRemoteRunner$1$startAnimation$animationFinishedCallback$1 r6 = new com.android.systemui.animation.RemoteTransitionAdapter$Companion$adaptRemoteRunner$1$startAnimation$animationFinishedCallback$1
            r0 = r6
            r2 = r4
            r3 = r24
            r4 = r11
            r5 = r26
            r0.<init>(r1, r2, r3, r4, r5)
            r0 = r22
            android.view.IRemoteAnimationRunner r3 = r0.$runner
            r4 = 0
            r8 = r6
            android.view.IRemoteAnimationFinishedCallback r8 = (android.view.IRemoteAnimationFinishedCallback) r8
            r5 = r12
            r6 = r13
            r7 = r14
            r3.onAnimationStart(r4, r5, r6, r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.animation.RemoteTransitionAdapter$Companion$adaptRemoteRunner$1.startAnimation(android.os.IBinder, android.window.TransitionInfo, android.view.SurfaceControl$Transaction, android.window.IRemoteTransitionFinishedCallback):void");
    }
}
