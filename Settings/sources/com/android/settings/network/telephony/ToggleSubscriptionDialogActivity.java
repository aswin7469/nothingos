package com.android.settings.network.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccSlotInfo;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$string;
import com.android.settings.SidecarFragment;
import com.android.settings.network.EnableMultiSimSidecar;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.SwitchToEuiccSubscriptionSidecar;
import com.android.settings.network.SwitchToRemovableSlotSidecar;
import com.android.settings.network.SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0;
import com.android.settings.network.UiccSlotUtil;
import com.android.settings.network.telephony.ConfirmDialogFragment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ToggleSubscriptionDialogActivity extends SubscriptionActionDialogActivity implements SidecarFragment.Listener, ConfirmDialogFragment.OnConfirmListener, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    @VisibleForTesting
    public static final String ARG_enable = "enable";
    private boolean isRtlMode;
    private List<SubscriptionInfo> mActiveSubInfos;
    private SubscriptionsChangeListener mChangeListener;
    private boolean mEnable;
    private EnableMultiSimSidecar mEnableMultiSimSidecar;
    private boolean mIsEsimOperation;
    private SubscriptionInfo mSubInfo;
    private SwitchToEuiccSubscriptionSidecar mSwitchToEuiccSubscriptionSidecar;
    private SwitchToRemovableSlotSidecar mSwitchToRemovableSlotSidecar;
    private TelephonyManager mTelMgr;

    public void onAirplaneModeChanged(boolean z) {
    }

    public static Intent getIntent(Context context, int i, boolean z) {
        Intent intent = new Intent(context, ToggleSubscriptionDialogActivity.class);
        intent.putExtra("sub_id", i);
        intent.putExtra(ARG_enable, z);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        int intExtra = intent.getIntExtra("sub_id", -1);
        this.mTelMgr = (TelephonyManager) getSystemService(TelephonyManager.class);
        if (!((UserManager) getSystemService(UserManager.class)).isAdminUser()) {
            Log.e("ToggleSubscriptionDialogActivity", "It is not the admin user. Unable to toggle subscription.");
            finish();
        } else if (!SubscriptionManager.isUsableSubscriptionId(intExtra)) {
            Log.e("ToggleSubscriptionDialogActivity", "The subscription id is not usable.");
            finish();
        } else {
            this.mActiveSubInfos = SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager);
            SubscriptionInfo subById = SubscriptionUtil.getSubById(this.mSubscriptionManager, intExtra);
            this.mSubInfo = subById;
            boolean z = false;
            this.mIsEsimOperation = subById != null && subById.isEmbedded();
            this.mSwitchToEuiccSubscriptionSidecar = SwitchToEuiccSubscriptionSidecar.get(getFragmentManager());
            this.mSwitchToRemovableSlotSidecar = SwitchToRemovableSlotSidecar.get(getFragmentManager());
            this.mEnableMultiSimSidecar = EnableMultiSimSidecar.get(getFragmentManager());
            this.mEnable = intent.getBooleanExtra(ARG_enable, true);
            if (getResources().getConfiguration().getLayoutDirection() == 1) {
                z = true;
            }
            this.isRtlMode = z;
            Log.i("ToggleSubscriptionDialogActivity", "isMultipleEnabledProfilesSupported():" + isMultipleEnabledProfilesSupported());
            if (bundle != null) {
                return;
            }
            if (this.mEnable) {
                showEnableSubDialog();
            } else {
                showDisableSimConfirmDialog();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mChangeListener == null) {
            this.mChangeListener = new SubscriptionsChangeListener(this, this);
        }
        this.mChangeListener.start();
        this.mSwitchToEuiccSubscriptionSidecar.addListener(this);
        this.mSwitchToRemovableSlotSidecar.addListener(this);
        this.mEnableMultiSimSidecar.addListener(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        SubscriptionsChangeListener subscriptionsChangeListener = this.mChangeListener;
        if (subscriptionsChangeListener != null) {
            subscriptionsChangeListener.stop();
        }
        this.mEnableMultiSimSidecar.removeListener(this);
        this.mSwitchToRemovableSlotSidecar.removeListener(this);
        this.mSwitchToEuiccSubscriptionSidecar.removeListener(this);
        super.onPause();
    }

    public void onSubscriptionsChanged() {
        SubscriptionInfo subscriptionInfo = this.mSubInfo;
        if ((subscriptionInfo == null || (!subscriptionInfo.isEmbedded() && !this.mSubscriptionManager.isActiveSubscriptionId(this.mSubInfo.getSubscriptionId()))) && !isFinishing()) {
            Log.i("ToggleSubscriptionDialogActivity", "Finish dialog for inactive sim");
            finish();
        }
    }

    public void onStateChange(SidecarFragment sidecarFragment) {
        if (sidecarFragment == this.mSwitchToEuiccSubscriptionSidecar) {
            handleSwitchToEuiccSubscriptionSidecarStateChange();
        } else if (sidecarFragment == this.mSwitchToRemovableSlotSidecar) {
            handleSwitchToRemovableSlotSidecarStateChange();
        } else if (sidecarFragment == this.mEnableMultiSimSidecar) {
            handleEnableMultiSimSidecarStateChange();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: boolean} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onConfirm(int r9, boolean r10, int r11) {
        /*
            r8 = this;
            r0 = 4
            r1 = 3
            if (r10 != 0) goto L_0x000c
            if (r9 == r1) goto L_0x000c
            if (r9 == r0) goto L_0x000c
            r8.finish()
            return
        L_0x000c:
            r2 = 0
            r3 = 0
            r4 = -1
            r5 = 1
            java.lang.String r6 = "ToggleSubscriptionDialogActivity"
            if (r9 == r5) goto L_0x00bd
            r7 = 2
            if (r9 == r7) goto L_0x008c
            if (r9 == r1) goto L_0x005c
            if (r9 == r0) goto L_0x0042
            r10 = 5
            if (r9 == r10) goto L_0x0034
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r10 = "Unrecognized confirmation dialog tag: "
            r8.append(r10)
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            android.util.Log.e(r6, r8)
            goto L_0x00e5
        L_0x0034:
            if (r11 == r4) goto L_0x008c
            java.util.List<android.telephony.SubscriptionInfo> r9 = r8.mActiveSubInfos
            if (r9 == 0) goto L_0x008c
            java.lang.Object r9 = r9.get(r11)
            android.telephony.SubscriptionInfo r9 = (android.telephony.SubscriptionInfo) r9
            r3 = r9
            goto L_0x008c
        L_0x0042:
            if (r10 != 0) goto L_0x004d
            java.lang.String r9 = "User cancel the dialog to reboot to enable DSDS."
            android.util.Log.i(r6, r9)
            r8.showEnableSimConfirmDialog()
            return
        L_0x004d:
            java.lang.String r9 = "User confirmed reboot to enable DSDS."
            android.util.Log.i(r6, r9)
            com.android.settings.sim.SimActivationNotifier.setShowSimSettingsNotification(r8, r5)
            android.telephony.TelephonyManager r8 = r8.mTelMgr
            r8.switchMultiSimConfig(r7)
            goto L_0x00e5
        L_0x005c:
            if (r10 != 0) goto L_0x0067
            java.lang.String r9 = "User cancel the dialog to enable DSDS."
            android.util.Log.i(r6, r9)
            r8.showEnableSimConfirmDialog()
            return
        L_0x0067:
            android.telephony.TelephonyManager r9 = r8.mTelMgr
            boolean r9 = r9.doesSwitchMultiSimConfigTriggerReboot()
            if (r9 == 0) goto L_0x0078
            java.lang.String r9 = "Device does not support reboot free DSDS."
            android.util.Log.i(r6, r9)
            r8.showRebootConfirmDialog()
            return
        L_0x0078:
            java.lang.String r9 = "Enabling DSDS without rebooting."
            android.util.Log.i(r6, r9)
            int r9 = com.android.settings.R$string.sim_action_enabling_sim_without_carrier_name
            java.lang.String r9 = r8.getString(r9)
            r8.showProgressDialog(r9)
            com.android.settings.network.EnableMultiSimSidecar r8 = r8.mEnableMultiSimSidecar
            r8.run(r7)
            goto L_0x00e5
        L_0x008c:
            java.lang.String r9 = "User confirmed to enable the subscription."
            android.util.Log.i(r6, r9)
            int r9 = com.android.settings.R$string.sim_action_switch_sub_dialog_progress
            java.lang.Object[] r10 = new java.lang.Object[r5]
            android.telephony.SubscriptionInfo r11 = r8.mSubInfo
            java.lang.CharSequence r11 = com.android.settings.network.SubscriptionUtil.getUniqueSubscriptionDisplayName((android.telephony.SubscriptionInfo) r11, (android.content.Context) r8)
            r10[r2] = r11
            java.lang.String r9 = r8.getString(r9, r10)
            if (r3 == 0) goto L_0x00a4
            r2 = r5
        L_0x00a4:
            r8.showProgressDialog(r9, r2)
            boolean r9 = r8.mIsEsimOperation
            if (r9 == 0) goto L_0x00b7
            com.android.settings.network.SwitchToEuiccSubscriptionSidecar r9 = r8.mSwitchToEuiccSubscriptionSidecar
            android.telephony.SubscriptionInfo r8 = r8.mSubInfo
            int r8 = r8.getSubscriptionId()
            r9.run(r8, r4, r3)
            return
        L_0x00b7:
            com.android.settings.network.SwitchToRemovableSlotSidecar r8 = r8.mSwitchToRemovableSlotSidecar
            r8.run(r4, r3)
            goto L_0x00e5
        L_0x00bd:
            boolean r9 = r8.mIsEsimOperation
            if (r9 == 0) goto L_0x00dd
            java.lang.String r9 = "Disabling the eSIM profile."
            android.util.Log.i(r6, r9)
            int r9 = com.android.settings.R$string.privileged_action_disable_sub_dialog_progress
            java.lang.String r9 = r8.getString(r9)
            r8.showProgressDialog(r9)
            android.telephony.SubscriptionInfo r9 = r8.mSubInfo
            if (r9 == 0) goto L_0x00d7
            int r2 = r9.getPortIndex()
        L_0x00d7:
            com.android.settings.network.SwitchToEuiccSubscriptionSidecar r8 = r8.mSwitchToEuiccSubscriptionSidecar
            r8.run(r4, r2, r3)
            return
        L_0x00dd:
            java.lang.String r9 = "Disabling the pSIM profile."
            android.util.Log.i(r6, r9)
            r8.handleTogglePsimAction()
        L_0x00e5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.telephony.ToggleSubscriptionDialogActivity.onConfirm(int, boolean, int):void");
    }

    private void handleSwitchToEuiccSubscriptionSidecarStateChange() {
        int state = this.mSwitchToEuiccSubscriptionSidecar.getState();
        String str = ARG_enable;
        if (state == 2) {
            Object[] objArr = new Object[1];
            if (!this.mEnable) {
                str = "disable";
            }
            objArr[0] = str;
            Log.i("ToggleSubscriptionDialogActivity", String.format("Successfully %s the eSIM profile.", objArr));
            this.mSwitchToEuiccSubscriptionSidecar.reset();
            dismissProgressDialog();
            finish();
        } else if (state == 3) {
            Object[] objArr2 = new Object[1];
            if (!this.mEnable) {
                str = "disable";
            }
            objArr2[0] = str;
            Log.i("ToggleSubscriptionDialogActivity", String.format("Failed to %s the eSIM profile.", objArr2));
            this.mSwitchToEuiccSubscriptionSidecar.reset();
            dismissProgressDialog();
            showErrorDialog(getString(R$string.privileged_action_disable_fail_title), getString(R$string.privileged_action_disable_fail_text));
        }
    }

    private void handleSwitchToRemovableSlotSidecarStateChange() {
        int state = this.mSwitchToRemovableSlotSidecar.getState();
        if (state == 2) {
            Log.i("ToggleSubscriptionDialogActivity", "Successfully switched to removable slot.");
            this.mSwitchToRemovableSlotSidecar.reset();
            handleTogglePsimAction();
            dismissProgressDialog();
            finish();
        } else if (state == 3) {
            Log.e("ToggleSubscriptionDialogActivity", "Failed switching to removable slot.");
            this.mSwitchToRemovableSlotSidecar.reset();
            dismissProgressDialog();
            showErrorDialog(getString(R$string.sim_action_enable_sim_fail_title), getString(R$string.sim_action_enable_sim_fail_text));
        }
    }

    private void handleEnableMultiSimSidecarStateChange() {
        int state = this.mEnableMultiSimSidecar.getState();
        if (state == 2) {
            this.mEnableMultiSimSidecar.reset();
            Log.i("ToggleSubscriptionDialogActivity", "Successfully switched to DSDS without reboot.");
            handleEnableSubscriptionAfterEnablingDsds();
        } else if (state == 3) {
            this.mEnableMultiSimSidecar.reset();
            Log.i("ToggleSubscriptionDialogActivity", "Failed to switch to DSDS without rebooting.");
            dismissProgressDialog();
            showErrorDialog(getString(R$string.dsds_activation_failure_title), getString(R$string.dsds_activation_failure_body_msg2));
        }
    }

    private void handleEnableSubscriptionAfterEnablingDsds() {
        if (this.mIsEsimOperation) {
            Log.i("ToggleSubscriptionDialogActivity", "DSDS enabled, start to enable profile: " + this.mSubInfo.getSubscriptionId());
            this.mSwitchToEuiccSubscriptionSidecar.run(this.mSubInfo.getSubscriptionId(), -1, (SubscriptionInfo) null);
            return;
        }
        Log.i("ToggleSubscriptionDialogActivity", "DSDS enabled, start to enable pSIM profile.");
        handleTogglePsimAction();
        dismissProgressDialog();
        finish();
    }

    private void handleTogglePsimAction() {
        SubscriptionInfo subscriptionInfo = this.mSubInfo;
        if (subscriptionInfo != null) {
            this.mSubscriptionManager.setUiccApplicationsEnabled(subscriptionInfo.getSubscriptionId(), this.mEnable);
            finish();
            return;
        }
        Log.i("ToggleSubscriptionDialogActivity", "The device does not support toggling pSIM. It is enough to just enable the removable slot.");
    }

    private void showEnableSubDialog() {
        Log.d("ToggleSubscriptionDialogActivity", "Handle subscription enabling.");
        if (isDsdsConditionSatisfied()) {
            showEnableDsdsConfirmDialog();
        } else if (this.mIsEsimOperation || !isRemovableSimEnabled()) {
            showEnableSimConfirmDialog();
        } else {
            Log.i("ToggleSubscriptionDialogActivity", "Toggle on pSIM, no dialog displayed.");
            handleTogglePsimAction();
            finish();
        }
    }

    private void showEnableDsdsConfirmDialog() {
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 3, getString(R$string.sim_action_enable_dsds_title), getString(R$string.sim_action_enable_dsds_text), getString(R$string.sim_action_yes), getString(R$string.sim_action_no_thanks));
    }

    private void showRebootConfirmDialog() {
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 4, getString(R$string.sim_action_restart_title), getString(R$string.sim_action_enable_dsds_text), getString(R$string.sim_action_reboot), getString(R$string.sim_action_cancel));
    }

    private void showDisableSimConfirmDialog() {
        String str;
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        if (this.mSubInfo == null || TextUtils.isEmpty(uniqueSubscriptionDisplayName)) {
            str = getString(R$string.privileged_action_disable_sub_dialog_title_without_carrier);
        } else {
            str = getString(R$string.privileged_action_disable_sub_dialog_title, new Object[]{uniqueSubscriptionDisplayName});
        }
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 1, str, (String) null, getString(R$string.yes), getString(R$string.sim_action_cancel));
    }

    private void showEnableSimConfirmDialog() {
        SubscriptionInfo subscriptionInfo;
        List<SubscriptionInfo> list = this.mActiveSubInfos;
        if (list == null || list.isEmpty()) {
            Log.i("ToggleSubscriptionDialogActivity", "No active subscriptions available.");
            showNonSwitchSimConfirmDialog();
            return;
        }
        Log.i("ToggleSubscriptionDialogActivity", "mActiveSubInfos:" + this.mActiveSubInfos);
        boolean z = this.mIsEsimOperation && this.mActiveSubInfos.stream().anyMatch(new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda2());
        boolean isMultiSimEnabled = this.mTelMgr.isMultiSimEnabled();
        if (isMultiSimEnabled && !isMultipleEnabledProfilesSupported() && !z) {
            showNonSwitchSimConfirmDialog();
        } else if (!isMultiSimEnabled || !isMultipleEnabledProfilesSupported()) {
            if (!isMultiSimEnabled || !z) {
                subscriptionInfo = this.mActiveSubInfos.get(0);
            } else {
                subscriptionInfo = (SubscriptionInfo) this.mActiveSubInfos.stream().filter(new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda3()).findFirst().get();
            }
            ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 2, getSwitchSubscriptionTitle(), getSwitchDialogBodyMsg(subscriptionInfo, z), getSwitchDialogPosBtnText(), getString(R$string.sim_action_cancel));
        } else if (this.mActiveSubInfos.size() < 2) {
            showNonSwitchSimConfirmDialog();
        } else {
            showMepSwitchSimConfirmDialog();
        }
    }

    private void showNonSwitchSimConfirmDialog() {
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 2, getEnableSubscriptionTitle(), (String) null, getString(R$string.yes), getString(R$string.sim_action_cancel));
    }

    private void showMepSwitchSimConfirmDialog() {
        Log.d("ToggleSubscriptionDialogActivity", "showMepSwitchSimConfirmDialog");
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        String string = getString(R$string.sim_action_switch_sub_dialog_mep_title, new Object[]{uniqueSubscriptionDisplayName});
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R$string.sim_action_switch_sub_dialog_mep_text, new Object[]{uniqueSubscriptionDisplayName}));
        if (this.isRtlMode) {
            sb.insert(0, "‏").insert(sb.length(), "‏");
        }
        ConfirmDialogFragment.show(this, ConfirmDialogFragment.OnConfirmListener.class, 5, string, sb.toString(), (String) null, (String) null, getSwitchDialogBodyList());
    }

    private String getSwitchDialogPosBtnText() {
        if (!this.mIsEsimOperation) {
            return getString(R$string.sim_switch_button);
        }
        return getString(R$string.sim_action_switch_sub_dialog_confirm, new Object[]{SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this)});
    }

    private String getEnableSubscriptionTitle() {
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        if (this.mSubInfo == null || TextUtils.isEmpty(uniqueSubscriptionDisplayName)) {
            return getString(R$string.sim_action_enable_sub_dialog_title_without_carrier_name);
        }
        return getString(R$string.sim_action_enable_sub_dialog_title, new Object[]{uniqueSubscriptionDisplayName});
    }

    private String getSwitchSubscriptionTitle() {
        if (!this.mIsEsimOperation) {
            return getString(R$string.sim_action_switch_psim_dialog_title);
        }
        return getString(R$string.sim_action_switch_sub_dialog_title, new Object[]{SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this)});
    }

    private String getSwitchDialogBodyMsg(SubscriptionInfo subscriptionInfo, boolean z) {
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(this.mSubInfo, (Context) this);
        CharSequence uniqueSubscriptionDisplayName2 = SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, (Context) this);
        StringBuilder sb = new StringBuilder();
        if (z && this.mIsEsimOperation) {
            sb.append(getString(R$string.sim_action_switch_sub_dialog_text_downloaded, new Object[]{uniqueSubscriptionDisplayName, uniqueSubscriptionDisplayName2}));
        } else if (this.mIsEsimOperation) {
            sb.append(getString(R$string.sim_action_switch_sub_dialog_text, new Object[]{uniqueSubscriptionDisplayName, uniqueSubscriptionDisplayName2}));
        } else {
            sb.append(getString(R$string.sim_action_switch_sub_dialog_text_single_sim, new Object[]{uniqueSubscriptionDisplayName2}));
        }
        if (this.isRtlMode) {
            sb.insert(0, "‏").insert(sb.indexOf("\n") - 1, "‏").insert(sb.indexOf("\n") + 2, "‏").insert(sb.length(), "‏");
        }
        return sb.toString();
    }

    private ArrayList<String> getSwitchDialogBodyList() {
        ArrayList<String> arrayList = new ArrayList<>((Collection) this.mActiveSubInfos.stream().map(new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda5(this)).collect(Collectors.toList()));
        arrayList.add(getString(R$string.sim_action_cancel));
        return arrayList;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getSwitchDialogBodyList$2(SubscriptionInfo subscriptionInfo) {
        CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, (Context) this);
        return getString(R$string.sim_action_switch_sub_dialog_carrier_list_item_for_turning_off, new Object[]{uniqueSubscriptionDisplayName});
    }

    private boolean isDsdsConditionSatisfied() {
        if (this.mTelMgr.isMultiSimEnabled()) {
            Log.d("ToggleSubscriptionDialogActivity", "DSDS is already enabled. Condition not satisfied.");
            return false;
        } else if (this.mTelMgr.isMultiSimSupported() != 0) {
            Log.d("ToggleSubscriptionDialogActivity", "Hardware does not support DSDS.");
            return false;
        } else {
            boolean z = SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager).size() > 0;
            if (!isMultipleEnabledProfilesSupported() || !z) {
                boolean isRemovableSimEnabled = isRemovableSimEnabled();
                if (!this.mIsEsimOperation || !isRemovableSimEnabled) {
                    boolean anyMatch = SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager).stream().anyMatch(new SwitchToRemovableSlotSidecar$$ExternalSyntheticLambda0());
                    if (this.mIsEsimOperation || !anyMatch) {
                        Log.d("ToggleSubscriptionDialogActivity", "DSDS condition not satisfied.");
                        return false;
                    }
                    Log.d("ToggleSubscriptionDialogActivity", "Removable SIM operation and eSIM profile is enabled. DSDS condition satisfied.");
                    return true;
                }
                Log.d("ToggleSubscriptionDialogActivity", "eSIM operation and removable SIM is enabled. DSDS condition satisfied.");
                return true;
            }
            Log.d("ToggleSubscriptionDialogActivity", "Device supports MEP and eSIM operation and eSIM profile is enabled. DSDS condition satisfied.");
            return true;
        }
    }

    private boolean isRemovableSimEnabled() {
        boolean anyMatch = UiccSlotUtil.getSlotInfos(this.mTelMgr).stream().anyMatch(new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda1());
        Log.i("ToggleSubscriptionDialogActivity", "isRemovableSimEnabled: " + anyMatch);
        return anyMatch;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isRemovableSimEnabled$4(UiccSlotInfo uiccSlotInfo) {
        return uiccSlotInfo != null && uiccSlotInfo.isRemovable() && uiccSlotInfo.getPorts().stream().anyMatch(new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda4()) && uiccSlotInfo.getCardStateInfo() == 2;
    }

    private boolean isMultipleEnabledProfilesSupported() {
        List<UiccCardInfo> uiccCardsInfo = this.mTelMgr.getUiccCardsInfo();
        if (uiccCardsInfo != null) {
            return uiccCardsInfo.stream().anyMatch(new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0());
        }
        Log.w("ToggleSubscriptionDialogActivity", "UICC cards info list is empty.");
        return false;
    }
}
