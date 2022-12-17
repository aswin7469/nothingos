package com.android.settings.network.apn;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Telephony;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneStateListener;
import android.telephony.PreciseDataConnectionState;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.ims.ImsManager;
import com.android.settings.R$drawable;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.RestrictedSettingsFragment;
import com.android.settings.Utils;
import com.android.settings.network.SubscriptionUtil;
import com.android.settingslib.RestrictedLockUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ApnSettings extends RestrictedSettingsFragment implements Preference.OnPreferenceChangeListener {
    private static final String[] CARRIERS_PROJECTION = {"_id", "name", "apn", "type", "mvno_type", "mvno_match_data", "edited", "bearer", "bearer_bitmask"};
    /* access modifiers changed from: private */
    public static final Uri DEFAULTAPN_URI = Uri.parse("content://telephony/carriers/restore");
    private static final Uri PREFERAPN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private boolean mAllowAddingApns;
    private PersistableBundle mHideApnsGroupByIccid;
    private String[] mHideApnsWithIccidRule;
    private String[] mHideApnsWithRule;
    private boolean mHideImsApn;
    private boolean mHidePresetApnDetails;
    private IntentFilter mIntentFilter;
    private String mMvnoMatchData;
    private String mMvnoType;
    /* access modifiers changed from: private */
    public int mPhoneId;
    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState preciseDataConnectionState) {
            if (preciseDataConnectionState.getState() != 2) {
                return;
            }
            if (!ApnSettings.this.mRestoreDefaultApnMode) {
                ApnSettings.this.fillList();
            } else {
                ApnSettings.this.showRestoreDefaultApnDialog();
            }
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction()) && intent.getStringExtra("ss").equals("ABSENT")) {
                SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
                if (subscriptionManager != null && !subscriptionManager.isActiveSubscriptionId(ApnSettings.this.mSubId)) {
                    Log.d("ApnSettings", "Due to SIM absent, closes APN settings page");
                    ApnSettings.this.finish();
                }
            } else if (intent.getAction().equals("android.telephony.action.SUBSCRIPTION_CARRIER_IDENTITY_CHANGED")) {
                if (!ApnSettings.this.mRestoreDefaultApnMode) {
                    int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_ID", -1);
                    if (SubscriptionManager.isValidSubscriptionId(intExtra) && ApnSettings.this.mPhoneId == SubscriptionUtil.getPhoneId(context, intExtra) && intExtra != ApnSettings.this.mSubId) {
                        ApnSettings.this.mSubId = intExtra;
                        ApnSettings apnSettings = ApnSettings.this;
                        apnSettings.mSubscriptionInfo = apnSettings.getSubscriptionInfo(apnSettings.mSubId);
                        ApnSettings apnSettings2 = ApnSettings.this;
                        apnSettings2.restartPhoneStateListener(apnSettings2.mSubId);
                    }
                    ApnSettings.this.fillList();
                }
            } else if (!intent.getAction().equals("org.codeaurora.intent.action.ACTION_ENHANCE_4G_SWITCH")) {
            } else {
                if (!ApnSettings.this.mRestoreDefaultApnMode) {
                    ApnSettings.this.fillList();
                } else {
                    ApnSettings.this.showRestoreDefaultApnDialog();
                }
            }
        }
    };
    private RestoreApnProcessHandler mRestoreApnProcessHandler;
    private RestoreApnUiHandler mRestoreApnUiHandler;
    /* access modifiers changed from: private */
    public boolean mRestoreDefaultApnMode;
    private HandlerThread mRestoreDefaultApnThread;
    private String mSelectedKey;
    /* access modifiers changed from: private */
    public int mSubId;
    /* access modifiers changed from: private */
    public SubscriptionInfo mSubscriptionInfo;
    private TelephonyManager mTelephonyManager;
    private boolean mUnavailable;
    private UserManager mUserManager;

    private int networkTypeToRilRidioTechnology(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            case 5:
                return 7;
            case 6:
                return 8;
            case 7:
                return 6;
            case 8:
                return 9;
            case 9:
                return 10;
            case 10:
                return 11;
            case 12:
                return 12;
            case 13:
                return 14;
            case 14:
                return 13;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            default:
                return 0;
        }
    }

    public int getDialogMetricsCategory(int i) {
        return i == 1001 ? 579 : 0;
    }

    public int getMetricsCategory() {
        return 12;
    }

    public ApnSettings() {
        super("no_config_mobile_networks");
    }

    /* access modifiers changed from: private */
    public void restartPhoneStateListener(int i) {
        if (!this.mRestoreDefaultApnMode) {
            TelephonyManager createForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(i);
            this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
            this.mTelephonyManager = createForSubscriptionId;
            createForSubscriptionId.listen(this.mPhoneStateListener, 4096);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        int intExtra = activity.getIntent().getIntExtra("sub_id", -1);
        this.mSubId = intExtra;
        this.mPhoneId = SubscriptionUtil.getPhoneId(activity, intExtra);
        IntentFilter intentFilter = new IntentFilter();
        this.mIntentFilter = intentFilter;
        intentFilter.addAction("android.telephony.action.SUBSCRIPTION_CARRIER_IDENTITY_CHANGED");
        this.mIntentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        if (Utils.isSupportCTPA(getActivity().getApplicationContext())) {
            this.mIntentFilter.addAction("org.codeaurora.intent.action.ACTION_ENHANCE_4G_SWITCH");
        }
        setIfOnlyAvailableForAdmins(true);
        this.mSubscriptionInfo = getSubscriptionInfo(this.mSubId);
        this.mTelephonyManager = (TelephonyManager) activity.getSystemService(TelephonyManager.class);
        PersistableBundle configForSubId = ((CarrierConfigManager) getSystemService("carrier_config")).getConfigForSubId(this.mSubId);
        this.mHideImsApn = configForSubId.getBoolean("hide_ims_apn_bool");
        this.mAllowAddingApns = configForSubId.getBoolean("allow_adding_apns_bool");
        this.mHideApnsWithRule = configForSubId.getStringArray("apn_hide_rule_strings_array");
        this.mHideApnsWithIccidRule = configForSubId.getStringArray("apn_hide_rule_strings_with_iccids_array");
        SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
        if (subscriptionInfo != null) {
            String iccId = subscriptionInfo.getIccId();
            Log.d("ApnSettings", "iccid: " + iccId);
            this.mHideApnsGroupByIccid = configForSubId.getPersistableBundle(iccId);
        }
        if (this.mAllowAddingApns && ApnEditor.hasAllApns(configForSubId.getStringArray("read_only_apn_types_string_array"))) {
            Log.d("ApnSettings", "not allowing adding APN because all APN types are read only");
            this.mAllowAddingApns = false;
        }
        this.mHidePresetApnDetails = configForSubId.getBoolean("hide_preset_apn_details_bool");
        this.mUserManager = UserManager.get(activity);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getEmptyTextView().setText(R$string.apn_settings_not_available);
        boolean isUiRestricted = isUiRestricted();
        this.mUnavailable = isUiRestricted;
        setHasOptionsMenu(!isUiRestricted);
        if (this.mUnavailable) {
            addPreferencesFromResource(R$xml.placeholder_prefs);
        } else {
            addPreferencesFromResource(R$xml.apn_settings);
        }
    }

    public void onResume() {
        super.onResume();
        if (!this.mUnavailable) {
            getActivity().registerReceiver(this.mReceiver, this.mIntentFilter, 2);
            restartPhoneStateListener(this.mSubId);
            if (!this.mRestoreDefaultApnMode) {
                fillList();
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (!this.mUnavailable) {
            getActivity().unregisterReceiver(this.mReceiver);
            this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        HandlerThread handlerThread = this.mRestoreDefaultApnThread;
        if (handlerThread != null) {
            handlerThread.quit();
        }
    }

    public RestrictedLockUtils.EnforcedAdmin getRestrictionEnforcedAdmin() {
        UserHandle of = UserHandle.of(this.mUserManager.getProcessUserId());
        if (!this.mUserManager.hasUserRestriction("no_config_mobile_networks", of) || this.mUserManager.hasBaseUserRestriction("no_config_mobile_networks", of)) {
            return null;
        }
        return RestrictedLockUtils.EnforcedAdmin.MULTIPLE_ENFORCED_ADMIN;
    }

    /* access modifiers changed from: private */
    public SubscriptionInfo getSubscriptionInfo(int i) {
        return SubscriptionManager.from(getActivity()).getActiveSubscriptionInfo(i);
    }

    /* access modifiers changed from: private */
    public void fillList() {
        SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
        int subscriptionId = subscriptionInfo != null ? subscriptionInfo.getSubscriptionId() : -1;
        Uri withAppendedPath = Uri.withAppendedPath(Telephony.Carriers.SIM_APN_URI, String.valueOf(subscriptionId));
        StringBuilder sb = new StringBuilder("NOT (type='ia' AND (apn=\"\" OR apn IS NULL)) AND user_visible!=0");
        sb.append(" AND NOT (type='emergency')");
        int phoneId = SubscriptionManager.getPhoneId(subscriptionId);
        Context applicationContext = getActivity().getApplicationContext();
        boolean isEnhanced4gLteModeSettingEnabledByUser = ImsManager.getInstance(applicationContext, phoneId).isEnhanced4gLteModeSettingEnabledByUser();
        if (this.mHideImsApn || (Utils.isSupportCTPA(applicationContext) && !isEnhanced4gLteModeSettingEnabledByUser)) {
            sb.append(" AND NOT (type='ims')");
        }
        appendFilter(sb);
        Log.d("ApnSettings", "where = " + sb.toString());
        Cursor query = getContentResolver().query(withAppendedPath, CARRIERS_PROJECTION, sb.toString(), (String[]) null, "name ASC");
        if (query != null) {
            PreferenceGroup preferenceGroup = (PreferenceGroup) findPreference("apn_list");
            preferenceGroup.removeAll();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            String selectedApnKey = getSelectedApnKey();
            this.mSelectedKey = selectedApnKey;
            ApnPreference.setSelectedKey(selectedApnKey);
            query.moveToFirst();
            while (!query.isAfterLast()) {
                String string = query.getString(1);
                String string2 = query.getString(2);
                String string3 = query.getString(0);
                String string4 = query.getString(3);
                int i = query.getInt(6);
                this.mMvnoType = query.getString(4);
                this.mMvnoMatchData = query.getString(5);
                String localizedName = Utils.getLocalizedName(getActivity(), query.getString(1));
                if (!TextUtils.isEmpty(localizedName)) {
                    string = localizedName;
                }
                int i2 = query.getInt(7);
                int i3 = query.getInt(8);
                int bitmaskForTech = ServiceState.getBitmaskForTech(i2) | i3;
                PreferenceGroup preferenceGroup2 = preferenceGroup;
                int networkTypeToRilRidioTechnology = networkTypeToRilRidioTechnology(TelephonyManager.getDefault().getDataNetworkType(subscriptionId));
                if (ServiceState.bitmaskHasTech(bitmaskForTech, networkTypeToRilRidioTechnology) || ((i2 == 0 && i3 == 0) || (networkTypeToRilRidioTechnology == 0 && !(i2 == 0 && networkTypeToRilRidioTechnology == 0)))) {
                    ApnPreference apnPreference = new ApnPreference(getPrefContext());
                    apnPreference.setKey(string3);
                    apnPreference.setTitle((CharSequence) string);
                    boolean z = false;
                    apnPreference.setPersistent(false);
                    apnPreference.setOnPreferenceChangeListener(this);
                    apnPreference.setSubId(subscriptionId);
                    if (!this.mHidePresetApnDetails || i != 0) {
                        apnPreference.setSummary((CharSequence) string2);
                    } else {
                        apnPreference.setHideDetails();
                    }
                    boolean z2 = string4 == null || string4.contains("default");
                    if (isEnhanced4gLteModeSettingEnabledByUser && z2 && Utils.isSupportCTPA(applicationContext)) {
                        if (string4 == null || !string4.equals("ims")) {
                            z = true;
                        }
                        z2 = z;
                    }
                    apnPreference.setSelectable(z2);
                    if (z2) {
                        String str = this.mSelectedKey;
                        if (str != null && str.equals(string3)) {
                            apnPreference.setChecked();
                        }
                        arrayList.add(apnPreference);
                    } else {
                        arrayList2.add(apnPreference);
                    }
                    query.moveToNext();
                } else {
                    query.moveToNext();
                }
                preferenceGroup = preferenceGroup2;
            }
            PreferenceGroup preferenceGroup3 = preferenceGroup;
            query.close();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                preferenceGroup3.addPreference((Preference) it.next());
            }
            PreferenceGroup preferenceGroup4 = preferenceGroup3;
            Iterator it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                preferenceGroup4.addPreference((Preference) it2.next());
            }
        }
    }

    private void appendFilter(StringBuilder sb) {
        boolean z;
        String[] strArr;
        String string;
        PersistableBundle persistableBundle = this.mHideApnsGroupByIccid;
        boolean z2 = true;
        if (persistableBundle == null || persistableBundle.isEmpty()) {
            z = true;
        } else {
            z = this.mHideApnsGroupByIccid.getBoolean("include_common_rules", true);
            Log.d("ApnSettings", "apn hidden rules specified iccid, include common rule: " + z);
            for (String str : this.mHideApnsGroupByIccid.keySet()) {
                if (Utils.carrierTableFieldValidate(str) && (string = this.mHideApnsGroupByIccid.getString(str)) != null) {
                    sb.append(" AND " + str + " <> \"" + string + "\"");
                }
            }
        }
        String[] strArr2 = this.mHideApnsWithIccidRule;
        if (strArr2 != null) {
            HashMap<String, String> apnRuleMap = getApnRuleMap(strArr2);
            SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
            if (isOperatorIccid(apnRuleMap, subscriptionInfo == null ? "" : subscriptionInfo.getIccId())) {
                String str2 = apnRuleMap.get("include_common_rules");
                if (str2 != null && str2.equalsIgnoreCase(String.valueOf(false))) {
                    z2 = false;
                }
                Log.d("ApnSettings", "apn hidden rules in iccids, include common rule: " + z2);
                filterWithKey(apnRuleMap, sb);
                z = z2;
            }
        }
        if (z && (strArr = this.mHideApnsWithRule) != null) {
            filterWithKey(getApnRuleMap(strArr), sb);
        }
    }

    private void filterWithKey(Map<String, String> map, StringBuilder sb) {
        for (String next : map.keySet()) {
            if (Utils.carrierTableFieldValidate(next)) {
                String str = map.get(next);
                if (!TextUtils.isEmpty(str)) {
                    for (String str2 : str.split(",")) {
                        sb.append(" AND " + next + " <> \"" + str2 + "\"");
                    }
                }
            }
        }
    }

    private HashMap<String, String> getApnRuleMap(String[] strArr) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (strArr != null) {
            int length = strArr.length;
            Log.d("ApnSettings", "ruleArray size = " + length);
            if (length > 0 && length % 2 == 0) {
                for (int i = 0; i < length; i += 2) {
                    hashMap.put(strArr[i].toLowerCase(), strArr[i + 1]);
                }
            }
        }
        return hashMap;
    }

    private boolean isOperatorIccid(HashMap<String, String> hashMap, String str) {
        String str2 = hashMap.get("iccid");
        if (!TextUtils.isEmpty(str2)) {
            for (String trim : str2.split(",")) {
                if (str.startsWith(trim.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (!this.mUnavailable) {
            if (this.mAllowAddingApns) {
                menu.add(0, 1, 0, getResources().getString(R$string.menu_new)).setIcon(R$drawable.ic_add_24dp).setShowAsAction(1);
            }
            menu.add(0, 2, 0, getResources().getString(R$string.menu_restore)).setIcon(17301589);
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 1) {
            addNewApn();
            return true;
        } else if (itemId != 2) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            restoreDefaultApn();
            return true;
        }
    }

    private void addNewApn() {
        Intent intent = new Intent("android.intent.action.INSERT", Telephony.Carriers.CONTENT_URI);
        SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
        intent.putExtra("sub_id", subscriptionInfo != null ? subscriptionInfo.getSubscriptionId() : -1);
        intent.addFlags(1);
        if (!TextUtils.isEmpty(this.mMvnoType) && !TextUtils.isEmpty(this.mMvnoMatchData)) {
            intent.putExtra("mvno_type", this.mMvnoType);
            intent.putExtra("mvno_match_data", this.mMvnoMatchData);
        }
        startActivity(intent);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Log.d("ApnSettings", "onPreferenceChange(): Preference - " + preference + ", newValue - " + obj + ", newValue type - " + obj.getClass());
        if (!(obj instanceof String)) {
            return true;
        }
        setSelectedApnKey((String) obj);
        return true;
    }

    private void setSelectedApnKey(String str) {
        this.mSelectedKey = str;
        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put("apn_id", this.mSelectedKey);
        contentResolver.update(getUriForCurrSubId(PREFERAPN_URI), contentValues, (String) null, (String[]) null);
    }

    private String getSelectedApnKey() {
        String str;
        Cursor query = getContentResolver().query(getUriForCurrSubId(PREFERAPN_URI), new String[]{"_id"}, (String) null, (String[]) null, "name ASC");
        if (query.getCount() > 0) {
            query.moveToFirst();
            str = query.getString(0);
        } else {
            str = null;
        }
        query.close();
        return str;
    }

    private boolean restoreDefaultApn() {
        this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
        showRestoreDefaultApnDialog();
        this.mRestoreDefaultApnMode = true;
        if (this.mRestoreApnUiHandler == null) {
            this.mRestoreApnUiHandler = new RestoreApnUiHandler();
        }
        if (this.mRestoreApnProcessHandler == null || this.mRestoreDefaultApnThread == null) {
            HandlerThread handlerThread = new HandlerThread("Restore default APN Handler: Process Thread");
            this.mRestoreDefaultApnThread = handlerThread;
            handlerThread.start();
            this.mRestoreApnProcessHandler = new RestoreApnProcessHandler(this.mRestoreDefaultApnThread.getLooper(), this.mRestoreApnUiHandler);
        }
        this.mRestoreApnProcessHandler.sendEmptyMessage(1);
        return true;
    }

    /* access modifiers changed from: private */
    public Uri getUriForCurrSubId(Uri uri) {
        SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
        int subscriptionId = subscriptionInfo != null ? subscriptionInfo.getSubscriptionId() : -1;
        if (!SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
            return uri;
        }
        return Uri.withAppendedPath(uri, "subId/" + String.valueOf(subscriptionId));
    }

    private class RestoreApnUiHandler extends Handler {
        private RestoreApnUiHandler() {
        }

        public void handleMessage(Message message) {
            if (message.what == 2) {
                FragmentActivity activity = ApnSettings.this.getActivity();
                if (activity == null) {
                    ApnSettings.this.mRestoreDefaultApnMode = false;
                    return;
                }
                ApnSettings.this.fillList();
                ApnSettings.this.getPreferenceScreen().setEnabled(true);
                ApnSettings.this.mRestoreDefaultApnMode = false;
                ApnSettings.this.removeDialog(1001);
                Toast.makeText(activity, ApnSettings.this.getResources().getString(R$string.restore_default_apn_completed), 1).show();
                ApnSettings apnSettings = ApnSettings.this;
                apnSettings.restartPhoneStateListener(apnSettings.mSubId);
            }
        }
    }

    private class RestoreApnProcessHandler extends Handler {
        private Handler mRestoreApnUiHandler;

        RestoreApnProcessHandler(Looper looper, Handler handler) {
            super(looper);
            this.mRestoreApnUiHandler = handler;
        }

        public void handleMessage(Message message) {
            if (message.what == 1) {
                ApnSettings.this.getContentResolver().delete(ApnSettings.this.getUriForCurrSubId(ApnSettings.DEFAULTAPN_URI), (String) null, (String[]) null);
                this.mRestoreApnUiHandler.sendEmptyMessage(2);
            }
        }
    }

    public Dialog onCreateDialog(int i) {
        if (i != 1001) {
            return null;
        }
        C10903 r2 = new ProgressDialog(getActivity()) {
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return true;
            }
        };
        r2.setMessage(getResources().getString(R$string.restore_default_apn));
        r2.setCancelable(false);
        return r2;
    }

    /* access modifiers changed from: private */
    public void showRestoreDefaultApnDialog() {
        removeDialog(1001);
        showDialog(1001);
    }
}
