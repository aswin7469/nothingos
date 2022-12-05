package com.android.systemui.statusbar.phone;

import android.os.UserManager;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$bool;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.qs.QSDetailDisplayer;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.ViewController;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class MultiUserSwitchController extends ViewController<MultiUserSwitch> {
    private final FalsingManager mFalsingManager;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.android.systemui.statusbar.phone.MultiUserSwitchController.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (MultiUserSwitchController.this.mFalsingManager.isFalseTap(1)) {
                return;
            }
            View childAt = ((MultiUserSwitch) ((ViewController) MultiUserSwitchController.this).mView).getChildCount() > 0 ? ((MultiUserSwitch) ((ViewController) MultiUserSwitchController.this).mView).getChildAt(0) : ((ViewController) MultiUserSwitchController.this).mView;
            childAt.getLocationInWindow(r3);
            int[] iArr = {iArr[0] + (childAt.getWidth() / 2), iArr[1] + (childAt.getHeight() / 2)};
            MultiUserSwitchController.this.mQsDetailDisplayer.showDetailAdapter(MultiUserSwitchController.this.getUserDetailAdapter(), iArr[0], iArr[1]);
        }
    };
    private final QSDetailDisplayer mQsDetailDisplayer;
    private UserSwitcherController.BaseUserAdapter mUserListener;
    private final UserManager mUserManager;
    private final UserSwitcherController mUserSwitcherController;

    public MultiUserSwitchController(MultiUserSwitch multiUserSwitch, UserManager userManager, UserSwitcherController userSwitcherController, QSDetailDisplayer qSDetailDisplayer, FalsingManager falsingManager) {
        super(multiUserSwitch);
        this.mUserManager = userManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mQsDetailDisplayer = qSDetailDisplayer;
        this.mFalsingManager = falsingManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        registerListener();
        ((MultiUserSwitch) this.mView).refreshContentDescription(getCurrentUser());
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        ((MultiUserSwitch) this.mView).setOnClickListener(this.mOnClickListener);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        ((MultiUserSwitch) this.mView).setOnClickListener(null);
    }

    protected DetailAdapter getUserDetailAdapter() {
        return this.mUserSwitcherController.mUserDetailAdapter;
    }

    private void registerListener() {
        UserSwitcherController userSwitcherController;
        if (!this.mUserManager.isUserSwitcherEnabled() || this.mUserListener != null || (userSwitcherController = this.mUserSwitcherController) == null) {
            return;
        }
        this.mUserListener = new UserSwitcherController.BaseUserAdapter(userSwitcherController) { // from class: com.android.systemui.statusbar.phone.MultiUserSwitchController.2
            @Override // android.widget.Adapter
            public View getView(int i, View view, ViewGroup viewGroup) {
                return null;
            }

            @Override // android.widget.BaseAdapter
            public void notifyDataSetChanged() {
                ((MultiUserSwitch) ((ViewController) MultiUserSwitchController.this).mView).refreshContentDescription(MultiUserSwitchController.this.getCurrentUser());
            }
        };
        ((MultiUserSwitch) this.mView).refreshContentDescription(getCurrentUser());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCurrentUser() {
        if (((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.statusbar.phone.MultiUserSwitchController$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                Boolean lambda$getCurrentUser$0;
                lambda$getCurrentUser$0 = MultiUserSwitchController.this.lambda$getCurrentUser$0();
                return lambda$getCurrentUser$0;
            }
        })).booleanValue()) {
            return this.mUserSwitcherController.getCurrentUserName();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$getCurrentUser$0() {
        return Boolean.valueOf(this.mUserManager.isUserSwitcherEnabled());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isMultiUserEnabled$1() {
        return Boolean.valueOf(this.mUserManager.isUserSwitcherEnabled(getResources().getBoolean(R$bool.qs_show_user_switcher_for_single_user)));
    }

    public boolean isMultiUserEnabled() {
        return ((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.statusbar.phone.MultiUserSwitchController$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                Boolean lambda$isMultiUserEnabled$1;
                lambda$isMultiUserEnabled$1 = MultiUserSwitchController.this.lambda$isMultiUserEnabled$1();
                return lambda$isMultiUserEnabled$1;
            }
        })).booleanValue();
    }
}
