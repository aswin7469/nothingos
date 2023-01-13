package com.android.systemui.statusbar.phone;

import android.content.Intent;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.C1894R;
import com.android.systemui.DejankUtils;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.p012qs.FooterActionsView;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.p012qs.user.UserSwitchDialogController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.user.UserSwitcherActivity;
import com.android.systemui.util.ViewController;
import javax.inject.Inject;

public class MultiUserSwitchController extends ViewController<MultiUserSwitch> {
    /* access modifiers changed from: private */
    public final ActivityStarter mActivityStarter;
    /* access modifiers changed from: private */
    public final FalsingManager mFalsingManager;
    /* access modifiers changed from: private */
    public final FeatureFlags mFeatureFlags;
    private final View.OnClickListener mOnClickListener;
    private UserSwitcherController.BaseUserAdapter mUserListener;
    private final UserManager mUserManager;
    /* access modifiers changed from: private */
    public final UserSwitchDialogController mUserSwitchDialogController;
    private final UserSwitcherController mUserSwitcherController;

    @QSScope
    public static class Factory {
        private final ActivityStarter mActivityStarter;
        private final FalsingManager mFalsingManager;
        private final FeatureFlags mFeatureFlags;
        private final UserManager mUserManager;
        private final UserSwitchDialogController mUserSwitchDialogController;
        private final UserSwitcherController mUserSwitcherController;

        @Inject
        public Factory(UserManager userManager, UserSwitcherController userSwitcherController, FalsingManager falsingManager, UserSwitchDialogController userSwitchDialogController, FeatureFlags featureFlags, ActivityStarter activityStarter) {
            this.mUserManager = userManager;
            this.mUserSwitcherController = userSwitcherController;
            this.mFalsingManager = falsingManager;
            this.mUserSwitchDialogController = userSwitchDialogController;
            this.mActivityStarter = activityStarter;
            this.mFeatureFlags = featureFlags;
        }

        public MultiUserSwitchController create(FooterActionsView footerActionsView) {
            return new MultiUserSwitchController((MultiUserSwitch) footerActionsView.findViewById(C1894R.C1898id.multi_user_switch), this.mUserManager, this.mUserSwitcherController, this.mFalsingManager, this.mUserSwitchDialogController, this.mFeatureFlags, this.mActivityStarter);
        }
    }

    private MultiUserSwitchController(MultiUserSwitch multiUserSwitch, UserManager userManager, UserSwitcherController userSwitcherController, FalsingManager falsingManager, UserSwitchDialogController userSwitchDialogController, FeatureFlags featureFlags, ActivityStarter activityStarter) {
        super(multiUserSwitch);
        this.mOnClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (!MultiUserSwitchController.this.mFalsingManager.isFalseTap(1)) {
                    if (MultiUserSwitchController.this.mFeatureFlags.isEnabled(Flags.FULL_SCREEN_USER_SWITCHER)) {
                        Intent intent = new Intent(view.getContext(), UserSwitcherActivity.class);
                        intent.addFlags(335544320);
                        MultiUserSwitchController.this.mActivityStarter.startActivity(intent, true, ActivityLaunchAnimator.Controller.fromView(view, (Integer) null), true, UserHandle.SYSTEM);
                        return;
                    }
                    MultiUserSwitchController.this.mUserSwitchDialogController.showDialog(view);
                }
            }
        };
        this.mUserManager = userManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mFalsingManager = falsingManager;
        this.mUserSwitchDialogController = userSwitchDialogController;
        this.mFeatureFlags = featureFlags;
        this.mActivityStarter = activityStarter;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        registerListener();
        ((MultiUserSwitch) this.mView).refreshContentDescription(getCurrentUser());
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        ((MultiUserSwitch) this.mView).setOnClickListener(this.mOnClickListener);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        ((MultiUserSwitch) this.mView).setOnClickListener((View.OnClickListener) null);
    }

    private void registerListener() {
        UserSwitcherController userSwitcherController;
        if (this.mUserManager.isUserSwitcherEnabled() && this.mUserListener == null && (userSwitcherController = this.mUserSwitcherController) != null) {
            this.mUserListener = new UserSwitcherController.BaseUserAdapter(userSwitcherController) {
                public View getView(int i, View view, ViewGroup viewGroup) {
                    return null;
                }

                public void notifyDataSetChanged() {
                    ((MultiUserSwitch) MultiUserSwitchController.this.mView).refreshContentDescription(MultiUserSwitchController.this.getCurrentUser());
                }
            };
            ((MultiUserSwitch) this.mView).refreshContentDescription(getCurrentUser());
        }
    }

    /* access modifiers changed from: private */
    public String getCurrentUser() {
        if (((Boolean) DejankUtils.whitelistIpcs(new MultiUserSwitchController$$ExternalSyntheticLambda1(this))).booleanValue()) {
            return this.mUserSwitcherController.getCurrentUserName();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getCurrentUser$0$com-android-systemui-statusbar-phone-MultiUserSwitchController */
    public /* synthetic */ Boolean mo44456x1cd39df6() {
        return Boolean.valueOf(this.mUserManager.isUserSwitcherEnabled());
    }

    public boolean isMultiUserEnabled() {
        return ((Boolean) DejankUtils.whitelistIpcs(new MultiUserSwitchController$$ExternalSyntheticLambda0(this))).booleanValue();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$isMultiUserEnabled$1$com-android-systemui-statusbar-phone-MultiUserSwitchController */
    public /* synthetic */ Boolean mo44457x1fc8e3dc() {
        return Boolean.valueOf(this.mUserManager.isUserSwitcherEnabled(getResources().getBoolean(C1894R.bool.qs_show_user_switcher_for_single_user)));
    }
}
