package com.android.settings.deviceinfo.imei;

import android.content.Context;
import android.content.res.Resources;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R$bool;
import com.android.settings.R$id;
import com.android.settings.R$string;
import com.android.settings.network.telephony.TelephonyUtils;
import com.qti.extphone.QtiImeiInfo;

public class ImeiInfoDialogController {
    static final int ID_CDMA_SETTINGS = R$id.cdma_settings;
    static final int ID_GSM_SETTINGS = R$id.gsm_settings;
    static final int ID_IMEI_SV_VALUE = R$id.imei_sv_value;
    static final int ID_IMEI_VALUE = R$id.imei_value;
    static final int ID_MEID_NUMBER_VALUE = R$id.meid_number_value;
    private static final int ID_MIN_NUMBER_LABEL = R$id.min_number_label;
    static final int ID_MIN_NUMBER_VALUE = R$id.min_number_value;
    static final int ID_PRL_VERSION_VALUE = R$id.prl_version_value;
    private final ImeiInfoDialogFragment mDialog;
    private QtiImeiInfo[] mQtiImeiInfo;
    private final int mSlotId;
    private final SubscriptionInfo mSubscriptionInfo;
    private final TelephonyManager mTelephonyManager;

    public ImeiInfoDialogController(ImeiInfoDialogFragment imeiInfoDialogFragment, int i) {
        this.mDialog = imeiInfoDialogFragment;
        this.mSlotId = i;
        Context context = imeiInfoDialogFragment.getContext();
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = ((SubscriptionManager) context.getSystemService(SubscriptionManager.class)).getActiveSubscriptionInfoForSimSlotIndex(i);
        this.mSubscriptionInfo = activeSubscriptionInfoForSimSlotIndex;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        if (activeSubscriptionInfoForSimSlotIndex != null) {
            this.mTelephonyManager = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(activeSubscriptionInfoForSimSlotIndex.getSubscriptionId());
        } else if (isValidSlotIndex(i, telephonyManager)) {
            this.mTelephonyManager = telephonyManager;
        } else {
            this.mTelephonyManager = null;
        }
        this.mQtiImeiInfo = TelephonyUtils.getImeiInfo();
    }

    private String getImei(int i) {
        String str;
        if (this.mQtiImeiInfo != null) {
            int i2 = 0;
            while (true) {
                QtiImeiInfo[] qtiImeiInfoArr = this.mQtiImeiInfo;
                if (i2 >= qtiImeiInfoArr.length) {
                    break;
                }
                QtiImeiInfo qtiImeiInfo = qtiImeiInfoArr[i2];
                if (qtiImeiInfo != null && qtiImeiInfo.getSlotId() == i) {
                    str = this.mQtiImeiInfo[i2].getImei();
                    break;
                }
                i2++;
            }
        }
        str = null;
        return TextUtils.isEmpty(str) ? this.mTelephonyManager.getImei(i) : str;
    }

    public void populateImeiInfo() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            Log.w("ImeiInfoDialog", "TelephonyManager for this slot is null. Invalid slot? id=" + this.mSlotId);
        } else if (telephonyManager.getPhoneType() == 2) {
            updateDialogForCdmaPhone();
        } else {
            updateDialogForGsmPhone();
        }
    }

    private void updateDialogForCdmaPhone() {
        Resources resources = this.mDialog.getContext().getResources();
        this.mDialog.setText(ID_MEID_NUMBER_VALUE, getMeid());
        ImeiInfoDialogFragment imeiInfoDialogFragment = this.mDialog;
        int i = ID_MIN_NUMBER_VALUE;
        SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
        imeiInfoDialogFragment.setText(i, subscriptionInfo != null ? this.mTelephonyManager.getCdmaMin(subscriptionInfo.getSubscriptionId()) : "");
        if (resources.getBoolean(R$bool.config_msid_enable)) {
            this.mDialog.setText(ID_MIN_NUMBER_LABEL, resources.getString(R$string.status_msid_number));
        }
        this.mDialog.setText(ID_PRL_VERSION_VALUE, getCdmaPrlVersion());
        if ((this.mSubscriptionInfo == null || !isCdmaLteEnabled()) && (this.mSubscriptionInfo != null || !isSimPresent(this.mSlotId))) {
            this.mDialog.removeViewFromScreen(ID_GSM_SETTINGS);
            return;
        }
        this.mDialog.setText(ID_IMEI_VALUE, getImei(this.mSlotId));
        this.mDialog.setText(ID_IMEI_SV_VALUE, this.mTelephonyManager.getDeviceSoftwareVersion(this.mSlotId));
    }

    private void updateDialogForGsmPhone() {
        this.mDialog.setText(ID_IMEI_VALUE, getImei(this.mSlotId));
        this.mDialog.setText(ID_IMEI_SV_VALUE, this.mTelephonyManager.getDeviceSoftwareVersion(this.mSlotId));
        this.mDialog.removeViewFromScreen(ID_CDMA_SETTINGS);
    }

    /* access modifiers changed from: package-private */
    public String getCdmaPrlVersion() {
        return this.mSubscriptionInfo != null ? this.mTelephonyManager.getCdmaPrlVersion() : "";
    }

    /* access modifiers changed from: package-private */
    public boolean isCdmaLteEnabled() {
        return this.mTelephonyManager.isLteCdmaEvdoGsmWcdmaEnabled();
    }

    /* access modifiers changed from: package-private */
    public boolean isSimPresent(int i) {
        int simState = this.mTelephonyManager.getSimState(i);
        return (simState == 1 || simState == 0) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public String getMeid() {
        return this.mTelephonyManager.getMeid(this.mSlotId);
    }

    private boolean isValidSlotIndex(int i, TelephonyManager telephonyManager) {
        return i >= 0 && i < telephonyManager.getPhoneCount();
    }
}
