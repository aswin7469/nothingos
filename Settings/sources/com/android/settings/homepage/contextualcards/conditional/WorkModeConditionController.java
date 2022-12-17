package com.android.settings.homepage.contextualcards.conditional;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import com.android.settings.R$drawable;
import com.android.settings.R$string;
import com.android.settings.Settings;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.conditional.ConditionalContextualCard;
import java.util.List;
import java.util.Objects;

public class WorkModeConditionController implements ConditionalCardController {
    private static final IntentFilter FILTER;

    /* renamed from: ID */
    static final int f200ID = Objects.hash(new Object[]{"WorkModeConditionController"});
    private final Context mAppContext;
    /* access modifiers changed from: private */
    public final ConditionManager mConditionManager;
    private final DevicePolicyManager mDpm;
    private final Receiver mReceiver = new Receiver();
    private final UserManager mUm;
    private UserHandle mUserHandle;

    static {
        IntentFilter intentFilter = new IntentFilter();
        FILTER = intentFilter;
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
    }

    public WorkModeConditionController(Context context, ConditionManager conditionManager) {
        this.mAppContext = context;
        this.mUm = (UserManager) context.getSystemService(UserManager.class);
        this.mDpm = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        this.mConditionManager = conditionManager;
    }

    public long getId() {
        return (long) f200ID;
    }

    public boolean isDisplayable() {
        updateUserHandle();
        UserHandle userHandle = this.mUserHandle;
        return userHandle != null && this.mUm.isQuietModeEnabled(userHandle);
    }

    public void onPrimaryClick(Context context) {
        context.startActivity(new Intent(context, Settings.AccountDashboardActivity.class));
    }

    public void onActionClick() {
        UserHandle userHandle = this.mUserHandle;
        if (userHandle != null) {
            this.mUm.requestQuietModeEnabled(false, userHandle);
        }
    }

    public ContextualCard buildContextualCard() {
        String string = this.mDpm.getResources().getString("Settings.WORK_PROFILE_OFF_CONDITION_TITLE", new WorkModeConditionController$$ExternalSyntheticLambda0(this));
        ConditionalContextualCard.Builder actionText = new ConditionalContextualCard.Builder().setConditionId((long) f200ID).setMetricsConstant(383).setActionText(this.mAppContext.getText(R$string.condition_turn_on));
        return actionText.setName(this.mAppContext.getPackageName() + "/" + string).setTitleText(string).setSummaryText(this.mAppContext.getText(R$string.condition_work_summary).toString()).setIconDrawable(this.mAppContext.getDrawable(R$drawable.ic_signal_workmode_enable)).setViewType(ConditionContextualCardRenderer.VIEW_TYPE_HALF_WIDTH).build();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$buildContextualCard$0() {
        return this.mAppContext.getString(R$string.condition_work_title);
    }

    public void startMonitoringStateChange() {
        this.mAppContext.registerReceiver(this.mReceiver, FILTER);
    }

    public void stopMonitoringStateChange() {
        this.mAppContext.unregisterReceiver(this.mReceiver);
    }

    private void updateUserHandle() {
        List profiles = this.mUm.getProfiles(UserHandle.myUserId());
        int size = profiles.size();
        this.mUserHandle = null;
        for (int i = 0; i < size; i++) {
            UserInfo userInfo = (UserInfo) profiles.get(i);
            if (userInfo.isManagedProfile()) {
                this.mUserHandle = userInfo.getUserHandle();
                return;
            }
        }
    }

    public class Receiver extends BroadcastReceiver {
        public Receiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, "android.intent.action.MANAGED_PROFILE_AVAILABLE") || TextUtils.equals(action, "android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                WorkModeConditionController.this.mConditionManager.onConditionChanged();
            }
        }
    }
}
