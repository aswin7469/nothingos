package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$string;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.ims.WifiCallingQueryImsState;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.ServiceCallback;
import java.util.List;
import java.util.Objects;

public class BackupCallingPreferenceController extends TelephonyTogglePreferenceController {
    private static final String DIALOG_TAG = "BackupCallingDialog";
    private static final String LOG_TAG = "BackupCallingPrefCtrl";
    private final String PREFERENCE_KEY = "backup_calling_key";
    private Context mContext;
    boolean mDialogNeeded = false;
    private int mDialogType;
    private ServiceCallback mExtTelManagerServiceCallback = new ServiceCallback() {
        public void onConnected() {
            Log.d(BackupCallingPreferenceController.LOG_TAG, "mExtTelManagerServiceCallback: service connected");
            BackupCallingPreferenceController.this.mServiceConnected = true;
            BackupCallingPreferenceController backupCallingPreferenceController = BackupCallingPreferenceController.this;
            backupCallingPreferenceController.displayPreference(backupCallingPreferenceController.mScreen);
            if (BackupCallingPreferenceController.this.mScreen != null) {
                BackupCallingPreferenceController backupCallingPreferenceController2 = BackupCallingPreferenceController.this;
                backupCallingPreferenceController2.updateState((SwitchPreference) backupCallingPreferenceController2.mScreen.findPreference("backup_calling_key"));
            }
        }

        public void onDisconnected() {
            Log.d(BackupCallingPreferenceController.LOG_TAG, "mExtTelManagerServiceCallback: service disconnected");
            BackupCallingPreferenceController.this.mServiceConnected = false;
        }
    };
    private ExtTelephonyManager mExtTelephonyManager;
    private FragmentManager mFragmentManager;
    private Preference mPreference;
    /* access modifiers changed from: private */
    public PreferenceScreen mScreen;
    /* access modifiers changed from: private */
    public boolean mServiceConnected = false;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BackupCallingPreferenceController(Context context, String str) {
        super(context, str);
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        ExtTelephonyManager instance = ExtTelephonyManager.getInstance(applicationContext);
        this.mExtTelephonyManager = instance;
        instance.connectService(this.mExtTelManagerServiceCallback);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
    }

    public BackupCallingPreferenceController init(FragmentManager fragmentManager, int i) {
        this.mFragmentManager = fragmentManager;
        this.mSubId = i;
        this.mTelephonyManager = getTelephonyManager();
        return this;
    }

    private TelephonyManager getTelephonyManager() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager != null) {
            return telephonyManager;
        }
        TelephonyManager telephonyManager2 = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        int i = this.mSubId;
        if (i != -1) {
            telephonyManager2 = telephonyManager2.createForSubscriptionId(i);
        }
        this.mTelephonyManager = telephonyManager2;
        return telephonyManager2;
    }

    public int getAvailabilityStatus(int i) {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        Log.d(LOG_TAG, "getActiveModemCount: " + telephonyManager.getActiveModemCount());
        if (telephonyManager.getActiveModemCount() > 1 || !hasBackupCallingFeature(i) || getSubscriptionInfoFromList(getActiveSubscriptionList(), i) == null) {
            return 2;
        }
        return 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mScreen = preferenceScreen;
        if (preferenceScreen != null) {
            super.displayPreference(preferenceScreen);
        }
    }

    public boolean setChecked(boolean z) {
        ImsMmTelManager imsMmTelManager;
        boolean isDialogNeeded = isDialogNeeded(z);
        this.mDialogNeeded = isDialogNeeded;
        if (isDialogNeeded || (imsMmTelManager = getImsMmTelManager(this.mSubId)) == null) {
            return false;
        }
        try {
            imsMmTelManager.setCrossSimCallingEnabled(z);
            return true;
        } catch (ImsException e) {
            Log.w(LOG_TAG, "fail to change cross SIM calling configuration: " + z, e);
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x002a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isDialogNeeded(boolean r8) {
        /*
            r7 = this;
            java.lang.String r0 = "BackupCallingPrefCtrl"
            android.telephony.TelephonyManager r1 = r7.mTelephonyManager
            int r2 = r7.mSubId
            int r2 = android.telephony.SubscriptionManager.getSlotIndex(r2)
            r3 = 1
            android.telephony.ims.aidl.IImsRegistration r1 = r1.getImsRegistration(r2, r3)
            r2 = 0
            int r1 = r1.getRegistrationTechnology()     // Catch:{ RemoteException -> 0x0019 }
            r4 = 2
            if (r1 != r4) goto L_0x001f
            r1 = r3
            goto L_0x0020
        L_0x0019:
            r1 = move-exception
            java.lang.String r4 = "getRegistrationTechnology failed"
            android.util.Log.e(r0, r4, r1)
        L_0x001f:
            r1 = r2
        L_0x0020:
            android.telephony.TelephonyManager r4 = r7.mTelephonyManager
            int r4 = r4.getCallState()
            if (r4 != 0) goto L_0x002a
            r4 = r3
            goto L_0x002b
        L_0x002a:
            r4 = r2
        L_0x002b:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "isDialogNeeded: isChecked="
            r5.append(r6)
            r5.append(r8)
            java.lang.String r6 = ", isCallIdle="
            r5.append(r6)
            r5.append(r4)
            java.lang.String r6 = ", isImsRegisteredOverCiwlan="
            r5.append(r6)
            r5.append(r1)
            java.lang.String r5 = r5.toString()
            android.util.Log.d(r0, r5)
            if (r8 != 0) goto L_0x0058
            if (r4 != 0) goto L_0x0058
            if (r1 == 0) goto L_0x0058
            r7.mDialogType = r2
            return r3
        L_0x0058:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.telephony.BackupCallingPreferenceController.isDialogNeeded(boolean):boolean");
    }

    private void showDialog(int i) {
        BackupCallingDialogFragment.newInstance(i, this.mSubId).show(this.mFragmentManager, DIALOG_TAG);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        if (!this.mDialogNeeded) {
            return true;
        }
        showDialog(this.mDialogType);
        return true;
    }

    public boolean isChecked() {
        ImsMmTelManager imsMmTelManager = getImsMmTelManager(this.mSubId);
        if (imsMmTelManager == null) {
            return false;
        }
        try {
            return imsMmTelManager.isCrossSimCallingEnabled();
        } catch (ImsException e) {
            Log.w(LOG_TAG, "fail to get cross SIM calling configuration", e);
            return false;
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null && (preference instanceof SwitchPreference)) {
            SubscriptionInfo subscriptionInfoFromActiveList = getSubscriptionInfoFromActiveList(this.mSubId);
            this.mPreference = preference;
            ((SwitchPreference) preference).setChecked(subscriptionInfoFromActiveList != null ? isChecked() : false);
            updateSummary(getLatestSummary(subscriptionInfoFromActiveList));
        }
    }

    private String getLatestSummary(SubscriptionInfo subscriptionInfo) {
        return Objects.toString(subscriptionInfo == null ? null : SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, this.mContext), "");
    }

    private void updateSummary(String str) {
        Preference preference = this.mPreference;
        if (preference != null) {
            preference.setSummary((CharSequence) String.format(getResourcesForSubId().getString(R$string.backup_calling_setting_summary), new Object[]{str}).toString());
        }
    }

    private boolean hasBackupCallingFeature(int i) {
        return isCrossSimEnabledByPlatform(this.mContext, i);
    }

    /* access modifiers changed from: protected */
    public boolean isCrossSimEnabledByPlatform(Context context, int i) {
        if (!this.mServiceConnected) {
            Log.d(LOG_TAG, "ExtTelephony service is not connected");
            return false;
        }
        try {
            if (!this.mExtTelephonyManager.isEpdgOverCellularDataSupported(SubscriptionManager.getPhoneId(i))) {
                Log.d(LOG_TAG, "Not supported by platform. subId = " + i);
                return false;
            } else if (new WifiCallingQueryImsState(context, i).isWifiCallingSupported()) {
                PersistableBundle carrierConfigForSubId = getCarrierConfigForSubId(i);
                if (carrierConfigForSubId == null || !carrierConfigForSubId.getBoolean("carrier_cross_sim_ims_available_bool", false)) {
                    return false;
                }
                return true;
            } else {
                Log.d(LOG_TAG, "Not supported by framework. subId = " + i);
                return false;
            }
        } catch (RemoteException e) {
            Log.d(LOG_TAG, "isEpdgOverCellularDataSupported Exception" + e);
            return false;
        }
    }

    private ImsMmTelManager getImsMmTelManager(int i) {
        ImsManager imsManager;
        if (SubscriptionManager.isUsableSubscriptionId(i) && (imsManager = (ImsManager) this.mContext.getSystemService(ImsManager.class)) != null) {
            return imsManager.getImsMmTelManager(i);
        }
        return null;
    }

    private List<SubscriptionInfo> getActiveSubscriptionList() {
        return SubscriptionUtil.getActiveSubscriptions((SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class));
    }

    private SubscriptionInfo getSubscriptionInfoFromList(List<SubscriptionInfo> list, int i) {
        for (SubscriptionInfo next : list) {
            if (next != null && next.getSubscriptionId() == i) {
                return next;
            }
        }
        return null;
    }

    private SubscriptionInfo getSubscriptionInfoFromActiveList(int i) {
        if (!SubscriptionManager.isUsableSubscriptionId(i)) {
            return null;
        }
        return getSubscriptionInfoFromList(getActiveSubscriptionList(), i);
    }
}
