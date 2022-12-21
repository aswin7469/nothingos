package com.android.systemui.statusbar.phone;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.biometrics.AuthDialog;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\u0014\u001a\u00020\u0015H\u0001J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0006H\u0016J\b\u0010\u0019\u001a\u00020\u0017H\u0016J\u0010\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u0006H\u0016J \u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001fH\u0016J\u0010\u0010!\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u0006H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\u00068VX\u0005¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\n\u001a\u00020\u000bX\u000f¢\u0006\f\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0010\u001a\u0004\u0018\u00010\u00118VX\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u0006\""}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/StatusBarLaunchAnimatorController;", "Lcom/android/systemui/animation/ActivityLaunchAnimator$Controller;", "delegate", "centralSurfaces", "Lcom/android/systemui/statusbar/phone/CentralSurfaces;", "isLaunchForActivity", "", "(Lcom/android/systemui/animation/ActivityLaunchAnimator$Controller;Lcom/android/systemui/statusbar/phone/CentralSurfaces;Z)V", "isDialogLaunch", "()Z", "launchContainer", "Landroid/view/ViewGroup;", "getLaunchContainer", "()Landroid/view/ViewGroup;", "setLaunchContainer", "(Landroid/view/ViewGroup;)V", "openingWindowSyncView", "Landroid/view/View;", "getOpeningWindowSyncView", "()Landroid/view/View;", "createAnimatorState", "Lcom/android/systemui/animation/LaunchAnimator$State;", "onIntentStarted", "", "willAnimate", "onLaunchAnimationCancelled", "onLaunchAnimationEnd", "isExpandingFullyAbove", "onLaunchAnimationProgress", "state", "progress", "", "linearProgress", "onLaunchAnimationStart", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusBarLaunchAnimatorController.kt */
public final class StatusBarLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    private final CentralSurfaces centralSurfaces;
    private final ActivityLaunchAnimator.Controller delegate;
    private final boolean isLaunchForActivity;

    public LaunchAnimator.State createAnimatorState() {
        return this.delegate.createAnimatorState();
    }

    public ViewGroup getLaunchContainer() {
        return this.delegate.getLaunchContainer();
    }

    public boolean isDialogLaunch() {
        return this.delegate.isDialogLaunch();
    }

    public void setLaunchContainer(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<set-?>");
        this.delegate.setLaunchContainer(viewGroup);
    }

    public StatusBarLaunchAnimatorController(ActivityLaunchAnimator.Controller controller, CentralSurfaces centralSurfaces2, boolean z) {
        Intrinsics.checkNotNullParameter(controller, "delegate");
        Intrinsics.checkNotNullParameter(centralSurfaces2, "centralSurfaces");
        this.delegate = controller;
        this.centralSurfaces = centralSurfaces2;
        this.isLaunchForActivity = z;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ StatusBarLaunchAnimatorController(ActivityLaunchAnimator.Controller controller, CentralSurfaces centralSurfaces2, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(controller, centralSurfaces2, (i & 4) != 0 ? true : z);
    }

    public View getOpeningWindowSyncView() {
        return this.centralSurfaces.getNotificationShadeWindowView();
    }

    public void onIntentStarted(boolean z) {
        this.delegate.onIntentStarted(z);
        if (z) {
            this.centralSurfaces.getNotificationPanelViewController().setIsLaunchAnimationRunning(true);
        } else {
            this.centralSurfaces.collapsePanelOnMainThread();
        }
    }

    public void onLaunchAnimationStart(boolean z) {
        this.delegate.onLaunchAnimationStart(z);
        this.centralSurfaces.getNotificationPanelViewController().setIsLaunchAnimationRunning(true);
        if (!z) {
            this.centralSurfaces.collapsePanelWithDuration((int) ActivityLaunchAnimator.TIMINGS.getTotalDuration());
        }
    }

    public void onLaunchAnimationEnd(boolean z) {
        this.delegate.onLaunchAnimationEnd(z);
        this.centralSurfaces.getNotificationPanelViewController().setIsLaunchAnimationRunning(false);
        this.centralSurfaces.onLaunchAnimationEnd(z);
    }

    public void onLaunchAnimationProgress(LaunchAnimator.State state, float f, float f2) {
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        this.delegate.onLaunchAnimationProgress(state, f, f2);
        this.centralSurfaces.getNotificationPanelViewController().applyLaunchAnimationProgress(f2);
    }

    public void onLaunchAnimationCancelled() {
        this.delegate.onLaunchAnimationCancelled();
        this.centralSurfaces.getNotificationPanelViewController().setIsLaunchAnimationRunning(false);
        this.centralSurfaces.onLaunchAnimationCancelled(this.isLaunchForActivity);
    }
}
