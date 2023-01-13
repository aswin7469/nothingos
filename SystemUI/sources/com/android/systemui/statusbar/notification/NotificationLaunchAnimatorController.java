package com.android.systemui.statusbar.notification;

import android.view.View;
import android.view.ViewGroup;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.policy.HeadsUpUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u0006\u0018\u0000 /2\u00020\u0001:\u0001/B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\u0010\f\u001a\u0004\u0018\u00010\r¢\u0006\u0002\u0010\u000eJ\u0012\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0002J\b\u0010\u001f\u001a\u00020 H\u0016J\u0010\u0010!\u001a\u00020\u001c2\u0006\u0010\"\u001a\u00020#H\u0016J\b\u0010$\u001a\u00020\u001cH\u0016J\u0010\u0010%\u001a\u00020\u001c2\u0006\u0010&\u001a\u00020#H\u0016J \u0010'\u001a\u00020\u001c2\u0006\u0010(\u001a\u00020 2\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020*H\u0016J\u0010\u0010,\u001a\u00020\u001c2\u0006\u0010&\u001a\u00020#H\u0016J\u0010\u0010-\u001a\u00020\u001c2\u0006\u0010.\u001a\u00020#H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R$\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00108V@VX\u000e¢\u0006\f\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0018\u001a\n \u001a*\u0004\u0018\u00010\u00190\u0019X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0004¢\u0006\u0002\n\u0000¨\u00060"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/NotificationLaunchAnimatorController;", "Lcom/android/systemui/animation/ActivityLaunchAnimator$Controller;", "notificationShadeWindowViewController", "Lcom/android/systemui/statusbar/phone/NotificationShadeWindowViewController;", "notificationListContainer", "Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;", "headsUpManager", "Lcom/android/systemui/statusbar/phone/HeadsUpManagerPhone;", "notification", "Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;", "jankMonitor", "Lcom/android/internal/jank/InteractionJankMonitor;", "onFinishAnimationCallback", "Ljava/lang/Runnable;", "(Lcom/android/systemui/statusbar/phone/NotificationShadeWindowViewController;Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;Lcom/android/systemui/statusbar/phone/HeadsUpManagerPhone;Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;Lcom/android/internal/jank/InteractionJankMonitor;Ljava/lang/Runnable;)V", "ignored", "Landroid/view/ViewGroup;", "launchContainer", "getLaunchContainer", "()Landroid/view/ViewGroup;", "setLaunchContainer", "(Landroid/view/ViewGroup;)V", "notificationEntry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "notificationKey", "", "kotlin.jvm.PlatformType", "applyParams", "", "params", "Lcom/android/systemui/statusbar/notification/LaunchAnimationParameters;", "createAnimatorState", "Lcom/android/systemui/animation/LaunchAnimator$State;", "onIntentStarted", "willAnimate", "", "onLaunchAnimationCancelled", "onLaunchAnimationEnd", "isExpandingFullyAbove", "onLaunchAnimationProgress", "state", "progress", "", "linearProgress", "onLaunchAnimationStart", "removeHun", "animate", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationLaunchAnimatorController.kt */
public final class NotificationLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    public static final long ANIMATION_DURATION_TOP_ROUNDING = 100;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final HeadsUpManagerPhone headsUpManager;
    private final InteractionJankMonitor jankMonitor;
    private final ExpandableNotificationRow notification;
    private final NotificationEntry notificationEntry;
    private final String notificationKey;
    private final NotificationListContainer notificationListContainer;
    private final NotificationShadeWindowViewController notificationShadeWindowViewController;
    private final Runnable onFinishAnimationCallback;

    public void setLaunchContainer(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "ignored");
    }

    public NotificationLaunchAnimatorController(NotificationShadeWindowViewController notificationShadeWindowViewController2, NotificationListContainer notificationListContainer2, HeadsUpManagerPhone headsUpManagerPhone, ExpandableNotificationRow expandableNotificationRow, InteractionJankMonitor interactionJankMonitor, Runnable runnable) {
        Intrinsics.checkNotNullParameter(notificationShadeWindowViewController2, "notificationShadeWindowViewController");
        Intrinsics.checkNotNullParameter(notificationListContainer2, "notificationListContainer");
        Intrinsics.checkNotNullParameter(headsUpManagerPhone, "headsUpManager");
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "notification");
        Intrinsics.checkNotNullParameter(interactionJankMonitor, "jankMonitor");
        this.notificationShadeWindowViewController = notificationShadeWindowViewController2;
        this.notificationListContainer = notificationListContainer2;
        this.headsUpManager = headsUpManagerPhone;
        this.notification = expandableNotificationRow;
        this.jankMonitor = interactionJankMonitor;
        this.onFinishAnimationCallback = runnable;
        NotificationEntry entry = expandableNotificationRow.getEntry();
        Intrinsics.checkNotNullExpressionValue(entry, "notification.entry");
        this.notificationEntry = entry;
        this.notificationKey = entry.getSbn().getKey();
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/NotificationLaunchAnimatorController$Companion;", "", "()V", "ANIMATION_DURATION_TOP_ROUNDING", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotificationLaunchAnimatorController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public ViewGroup getLaunchContainer() {
        View rootView = this.notification.getRootView();
        if (rootView != null) {
            return (ViewGroup) rootView;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    public LaunchAnimator.State createAnimatorState() {
        float f;
        int max = Math.max(0, this.notification.getActualHeight() - this.notification.getClipBottomAmount());
        int[] locationOnScreen = this.notification.getLocationOnScreen();
        int topClippingStartLocation = this.notificationListContainer.getTopClippingStartLocation();
        int max2 = Math.max(topClippingStartLocation - locationOnScreen[1], 0);
        int i = locationOnScreen[1] + max2;
        if (max2 > 0) {
            f = 0.0f;
        } else {
            f = this.notification.getCurrentBackgroundRadiusTop();
        }
        int i2 = locationOnScreen[0];
        LaunchAnimationParameters launchAnimationParameters = new LaunchAnimationParameters(i, locationOnScreen[1] + max, i2, i2 + this.notification.getWidth(), f, this.notification.getCurrentBackgroundRadiusBottom());
        launchAnimationParameters.setStartTranslationZ(this.notification.getTranslationZ());
        launchAnimationParameters.setStartNotificationTop(this.notification.getTranslationY());
        launchAnimationParameters.setStartRoundedTopClipping(max2);
        launchAnimationParameters.setStartClipTopAmount(this.notification.getClipTopAmount());
        if (this.notification.isChildInGroup()) {
            launchAnimationParameters.setStartNotificationTop(launchAnimationParameters.getStartNotificationTop() + this.notification.getNotificationParent().getTranslationY());
            launchAnimationParameters.setParentStartRoundedTopClipping(Math.max(topClippingStartLocation - this.notification.getNotificationParent().getLocationOnScreen()[1], 0));
            int clipTopAmount = this.notification.getNotificationParent().getClipTopAmount();
            launchAnimationParameters.setParentStartClipTopAmount(clipTopAmount);
            if (clipTopAmount != 0) {
                float translationY = ((float) clipTopAmount) - this.notification.getTranslationY();
                if (translationY > 0.0f) {
                    launchAnimationParameters.setStartClipTopAmount((int) Math.ceil((double) translationY));
                }
            }
        }
        return launchAnimationParameters;
    }

    public void onIntentStarted(boolean z) {
        this.notificationShadeWindowViewController.setExpandAnimationRunning(z);
        this.notificationEntry.setExpandAnimationRunning(z);
        if (!z) {
            removeHun(true);
            Runnable runnable = this.onFinishAnimationCallback;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    private final void removeHun(boolean z) {
        if (this.headsUpManager.isAlerting(this.notificationKey)) {
            HeadsUpUtil.setNeedsHeadsUpDisappearAnimationAfterClick(this.notification, z);
            this.headsUpManager.removeNotification(this.notificationKey, true, z);
        }
    }

    public void onLaunchAnimationCancelled() {
        this.notificationShadeWindowViewController.setExpandAnimationRunning(false);
        this.notificationEntry.setExpandAnimationRunning(false);
        removeHun(true);
        Runnable runnable = this.onFinishAnimationCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    public void onLaunchAnimationStart(boolean z) {
        this.notification.setExpandAnimationRunning(true);
        this.notificationListContainer.setExpandingNotification(this.notification);
        this.jankMonitor.begin(this.notification, 16);
    }

    public void onLaunchAnimationEnd(boolean z) {
        this.jankMonitor.end(16);
        this.notification.setExpandAnimationRunning(false);
        this.notificationShadeWindowViewController.setExpandAnimationRunning(false);
        this.notificationEntry.setExpandAnimationRunning(false);
        this.notificationListContainer.setExpandingNotification((ExpandableNotificationRow) null);
        applyParams((LaunchAnimationParameters) null);
        removeHun(false);
        Runnable runnable = this.onFinishAnimationCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    private final void applyParams(LaunchAnimationParameters launchAnimationParameters) {
        this.notification.applyLaunchAnimationParams(launchAnimationParameters);
        this.notificationListContainer.applyLaunchAnimationParams(launchAnimationParameters);
    }

    public void onLaunchAnimationProgress(LaunchAnimator.State state, float f, float f2) {
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        LaunchAnimationParameters launchAnimationParameters = (LaunchAnimationParameters) state;
        launchAnimationParameters.setProgress(f);
        launchAnimationParameters.setLinearProgress(f2);
        applyParams(launchAnimationParameters);
    }
}
