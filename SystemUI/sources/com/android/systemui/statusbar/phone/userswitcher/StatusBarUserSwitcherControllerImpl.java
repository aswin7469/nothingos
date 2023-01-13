package com.android.systemui.statusbar.phone.userswitcher;

import android.content.Intent;
import android.os.UserHandle;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.flags.ResourceBooleanFlag;
import com.android.systemui.p012qs.user.UserSwitchDialogController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.user.UserSwitcherActivity;
import com.android.systemui.util.ViewController;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004*\u0002\u0013\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B?\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010¢\u0006\u0002\u0010\u0011J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u0019H\u0014J\b\u0010\u001b\u001a\u00020\u0019H\u0002J\b\u0010\u001c\u001a\u00020\u0019H\u0002R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0004\n\u0002\u0010\u0014R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0004\n\u0002\u0010\u0017R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherControllerImpl;", "Lcom/android/systemui/util/ViewController;", "Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherContainer;", "Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherController;", "view", "tracker", "Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserInfoTracker;", "featureController", "Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherFeatureController;", "userSwitcherDialogController", "Lcom/android/systemui/qs/user/UserSwitchDialogController;", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "(Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherContainer;Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserInfoTracker;Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherFeatureController;Lcom/android/systemui/qs/user/UserSwitchDialogController;Lcom/android/systemui/flags/FeatureFlags;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/plugins/FalsingManager;)V", "featureFlagListener", "com/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherControllerImpl$featureFlagListener$1", "Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherControllerImpl$featureFlagListener$1;", "listener", "com/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherControllerImpl$listener$1", "Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherControllerImpl$listener$1;", "onViewAttached", "", "onViewDetached", "updateChip", "updateEnabled", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusBarUserSwitcherController.kt */
public final class StatusBarUserSwitcherControllerImpl extends ViewController<StatusBarUserSwitcherContainer> implements StatusBarUserSwitcherController {
    private final ActivityStarter activityStarter;
    private final FalsingManager falsingManager;
    private final StatusBarUserSwitcherFeatureController featureController;
    private final StatusBarUserSwitcherControllerImpl$featureFlagListener$1 featureFlagListener = new StatusBarUserSwitcherControllerImpl$featureFlagListener$1(this);
    private final FeatureFlags featureFlags;
    private final StatusBarUserSwitcherControllerImpl$listener$1 listener = new StatusBarUserSwitcherControllerImpl$listener$1(this);
    private final StatusBarUserInfoTracker tracker;
    private final UserSwitchDialogController userSwitcherDialogController;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public StatusBarUserSwitcherControllerImpl(StatusBarUserSwitcherContainer statusBarUserSwitcherContainer, StatusBarUserInfoTracker statusBarUserInfoTracker, StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController, UserSwitchDialogController userSwitchDialogController, FeatureFlags featureFlags2, ActivityStarter activityStarter2, FalsingManager falsingManager2) {
        super(statusBarUserSwitcherContainer);
        Intrinsics.checkNotNullParameter(statusBarUserSwitcherContainer, "view");
        Intrinsics.checkNotNullParameter(statusBarUserInfoTracker, "tracker");
        Intrinsics.checkNotNullParameter(statusBarUserSwitcherFeatureController, "featureController");
        Intrinsics.checkNotNullParameter(userSwitchDialogController, "userSwitcherDialogController");
        Intrinsics.checkNotNullParameter(featureFlags2, "featureFlags");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(falsingManager2, "falsingManager");
        this.tracker = statusBarUserInfoTracker;
        this.featureController = statusBarUserSwitcherFeatureController;
        this.userSwitcherDialogController = userSwitchDialogController;
        this.featureFlags = featureFlags2;
        this.activityStarter = activityStarter2;
        this.falsingManager = falsingManager2;
    }

    public void onViewAttached() {
        this.tracker.addCallback((CurrentUserChipInfoUpdatedListener) this.listener);
        this.featureController.addCallback((OnUserSwitcherPreferenceChangeListener) this.featureFlagListener);
        ((StatusBarUserSwitcherContainer) this.mView).setOnClickListener(new StatusBarUserSwitcherControllerImpl$$ExternalSyntheticLambda0(this));
        updateEnabled();
    }

    /* access modifiers changed from: private */
    /* renamed from: onViewAttached$lambda-0  reason: not valid java name */
    public static final void m3226onViewAttached$lambda0(StatusBarUserSwitcherControllerImpl statusBarUserSwitcherControllerImpl, View view) {
        Intrinsics.checkNotNullParameter(statusBarUserSwitcherControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(view, "view");
        if (!statusBarUserSwitcherControllerImpl.falsingManager.isFalseTap(1)) {
            FeatureFlags featureFlags2 = statusBarUserSwitcherControllerImpl.featureFlags;
            ResourceBooleanFlag resourceBooleanFlag = Flags.FULL_SCREEN_USER_SWITCHER;
            Intrinsics.checkNotNullExpressionValue(resourceBooleanFlag, "FULL_SCREEN_USER_SWITCHER");
            if (featureFlags2.isEnabled(resourceBooleanFlag)) {
                Intent intent = new Intent(statusBarUserSwitcherControllerImpl.getContext(), UserSwitcherActivity.class);
                intent.addFlags(335544320);
                statusBarUserSwitcherControllerImpl.activityStarter.startActivity(intent, true, (ActivityLaunchAnimator.Controller) null, true, UserHandle.SYSTEM);
                return;
            }
            statusBarUserSwitcherControllerImpl.userSwitcherDialogController.showDialog(view);
        }
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.tracker.removeCallback((CurrentUserChipInfoUpdatedListener) this.listener);
        this.featureController.removeCallback((OnUserSwitcherPreferenceChangeListener) this.featureFlagListener);
        ((StatusBarUserSwitcherContainer) this.mView).setOnClickListener((View.OnClickListener) null);
    }

    /* access modifiers changed from: private */
    public final void updateChip() {
        ((StatusBarUserSwitcherContainer) this.mView).getText().setText(this.tracker.getCurrentUserName());
        ((StatusBarUserSwitcherContainer) this.mView).getAvatar().setImageDrawable(this.tracker.getCurrentUserAvatar());
    }

    /* access modifiers changed from: private */
    public final void updateEnabled() {
        if (!this.featureController.isStatusBarUserSwitcherFeatureEnabled() || !this.tracker.getUserSwitcherEnabled()) {
            ((StatusBarUserSwitcherContainer) this.mView).setVisibility(8);
            return;
        }
        ((StatusBarUserSwitcherContainer) this.mView).setVisibility(0);
        updateChip();
    }
}
