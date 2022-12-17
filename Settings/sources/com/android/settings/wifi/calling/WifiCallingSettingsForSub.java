package com.android.settings.wifi.calling;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ProvisioningManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$array;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.SettingsActivity;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.network.ims.WifiCallingQueryImsState;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import java.util.List;

public class WifiCallingSettingsForSub extends SettingsPreferenceFragment implements OnMainSwitchChangeListener, Preference.OnPreferenceChangeListener {
    @VisibleForTesting
    static final int REQUEST_CHECK_WFC_DISCLAIMER = 2;
    @VisibleForTesting
    static final int REQUEST_CHECK_WFC_EMERGENCY_ADDRESS = 1;
    private ListWithEntrySummaryPreference mButtonWfcMode;
    private ListWithEntrySummaryPreference mButtonWfcRoamingMode;
    private boolean mEditableWfcMode = true;
    private boolean mEditableWfcRoamingMode = true;
    private ImsMmTelManager mImsMmTelManager;
    private IntentFilter mIntentFilter;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.telephony.ims.action.WFC_IMS_REGISTRATION_ERROR")) {
                setResultCode(0);
                WifiCallingSettingsForSub.this.showAlert(intent);
            }
        }
    };
    private final ProvisioningManager.Callback mProvisioningCallback = new ProvisioningManager.Callback() {
        public void onProvisioningIntChanged(int i, int i2) {
            if (i == 28 || i == 10) {
                WifiCallingSettingsForSub.this.updateBody();
            }
        }
    };
    private ProvisioningManager mProvisioningManager;
    /* access modifiers changed from: private */
    public int mSubId = -1;
    private SettingsMainSwitchPreference mSwitchBar;
    private final PhoneTelephonyCallback mTelephonyCallback = new PhoneTelephonyCallback();
    private TelephonyManager mTelephonyManager;
    private Preference mUpdateAddress;
    private final Preference.OnPreferenceClickListener mUpdateAddressListener = new WifiCallingSettingsForSub$$ExternalSyntheticLambda0(this);
    private boolean mUseWfcHomeModeForRoaming = false;
    private boolean mValidListener = false;

    public int getHelpResource() {
        return 0;
    }

    public int getMetricsCategory() {
        return 1230;
    }

    private class PhoneTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private PhoneTelephonyCallback() {
        }

        public void onCallStateChanged(int i) {
            boolean z;
            boolean z2;
            boolean z3;
            PersistableBundle configForSubId;
            boolean z4;
            SettingsActivity settingsActivity = (SettingsActivity) WifiCallingSettingsForSub.this.getActivity();
            SettingsMainSwitchPreference settingsMainSwitchPreference = (SettingsMainSwitchPreference) WifiCallingSettingsForSub.this.getPreferenceScreen().findPreference("wifi_calling_switch_bar");
            boolean z5 = true;
            boolean z6 = false;
            if (settingsMainSwitchPreference != null) {
                boolean isChecked = settingsMainSwitchPreference.isChecked();
                WifiCallingSettingsForSub wifiCallingSettingsForSub = WifiCallingSettingsForSub.this;
                z = wifiCallingSettingsForSub.getTelephonyManagerForSub(wifiCallingSettingsForSub.mSubId).getCallState() == 0;
                if (isChecked || z) {
                    WifiCallingSettingsForSub wifiCallingSettingsForSub2 = WifiCallingSettingsForSub.this;
                    z4 = wifiCallingSettingsForSub2.queryImsState(wifiCallingSettingsForSub2.mSubId).isAllowUserControl();
                } else {
                    z4 = true;
                }
                z2 = isChecked && z4;
                settingsMainSwitchPreference.setEnabled(z && z4);
            } else {
                z2 = false;
                z = false;
            }
            if (!z2 || !z) {
                z3 = false;
            } else {
                CarrierConfigManager carrierConfigManager = (CarrierConfigManager) settingsActivity.getSystemService("carrier_config");
                if (!(carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(WifiCallingSettingsForSub.this.mSubId)) == null)) {
                    z5 = configForSubId.getBoolean("editable_wfc_mode_bool");
                    z6 = configForSubId.getBoolean("editable_wfc_roaming_mode_bool");
                }
                z3 = z6;
                z6 = z5;
            }
            Preference findPreference = WifiCallingSettingsForSub.this.getPreferenceScreen().findPreference("wifi_calling_mode");
            if (findPreference != null) {
                findPreference.setEnabled(z6);
            }
            Preference findPreference2 = WifiCallingSettingsForSub.this.getPreferenceScreen().findPreference("wifi_calling_roaming_mode");
            if (findPreference2 != null) {
                findPreference2.setEnabled(z3);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(Preference preference) {
        Intent carrierActivityIntent = getCarrierActivityIntent();
        if (carrierActivityIntent != null) {
            carrierActivityIntent.putExtra("EXTRA_LAUNCH_CARRIER_APP", 1);
            startActivity(carrierActivityIntent);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void showAlert(Intent intent) {
        FragmentActivity activity = getActivity();
        CharSequence charSequenceExtra = intent.getCharSequenceExtra("android.telephony.ims.extra.WFC_REGISTRATION_FAILURE_TITLE");
        CharSequence charSequenceExtra2 = intent.getCharSequenceExtra("android.telephony.ims.extra.WFC_REGISTRATION_FAILURE_MESSAGE");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(charSequenceExtra2).setTitle(charSequenceExtra).setIcon(17301543).setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
        builder.create().show();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public TelephonyManager getTelephonyManagerForSub(int i) {
        if (this.mTelephonyManager == null) {
            this.mTelephonyManager = (TelephonyManager) getContext().getSystemService(TelephonyManager.class);
        }
        return this.mTelephonyManager.createForSubscriptionId(i);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public WifiCallingQueryImsState queryImsState(int i) {
        return new WifiCallingQueryImsState(getContext(), i);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public ProvisioningManager getImsProvisioningManager() {
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            return null;
        }
        return ProvisioningManager.createForSubscriptionId(this.mSubId);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public ImsMmTelManager getImsMmTelManager() {
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            return null;
        }
        return ImsMmTelManager.createForSubscriptionId(this.mSubId);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R$xml.wifi_calling_settings);
        if (getArguments() != null && getArguments().containsKey("subId")) {
            this.mSubId = getArguments().getInt("subId");
        } else if (bundle != null) {
            this.mSubId = bundle.getInt("subId", -1);
        }
        this.mProvisioningManager = getImsProvisioningManager();
        this.mImsMmTelManager = getImsMmTelManager();
        this.mSwitchBar = (SettingsMainSwitchPreference) findPreference("wifi_calling_switch_bar");
        ListWithEntrySummaryPreference listWithEntrySummaryPreference = (ListWithEntrySummaryPreference) findPreference("wifi_calling_mode");
        this.mButtonWfcMode = listWithEntrySummaryPreference;
        listWithEntrySummaryPreference.setOnPreferenceChangeListener(this);
        ListWithEntrySummaryPreference listWithEntrySummaryPreference2 = (ListWithEntrySummaryPreference) findPreference("wifi_calling_roaming_mode");
        this.mButtonWfcRoamingMode = listWithEntrySummaryPreference2;
        listWithEntrySummaryPreference2.setOnPreferenceChangeListener(this);
        Preference findPreference = findPreference("emergency_address_key");
        this.mUpdateAddress = findPreference;
        findPreference.setOnPreferenceClickListener(this.mUpdateAddressListener);
        IntentFilter intentFilter = new IntentFilter();
        this.mIntentFilter = intentFilter;
        intentFilter.addAction("android.telephony.ims.action.WFC_IMS_REGISTRATION_ERROR");
        updateDescriptionForOptions(List.of(this.mButtonWfcMode, this.mButtonWfcRoamingMode, this.mUpdateAddress));
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("subId", this.mSubId);
        super.onSaveInstanceState(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R$layout.wifi_calling_settings_preferences, viewGroup, false);
        ViewGroup viewGroup2 = (ViewGroup) inflate.findViewById(16908305);
        Utils.prepareCustomPreferencesList(viewGroup, inflate, viewGroup2, false);
        viewGroup2.addView(super.onCreateView(layoutInflater, viewGroup2, bundle));
        return inflate;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isWfcProvisionedOnDevice() {
        return queryImsState(this.mSubId).isWifiCallingProvisioned();
    }

    /* access modifiers changed from: private */
    public void updateBody() {
        boolean z;
        boolean z2;
        PersistableBundle configForSubId;
        if (!isWfcProvisionedOnDevice()) {
            finish();
            return;
        }
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) getSystemService("carrier_config");
        boolean z3 = true;
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(this.mSubId)) == null) {
            z = true;
            z2 = false;
        } else {
            this.mEditableWfcMode = configForSubId.getBoolean("editable_wfc_mode_bool");
            this.mEditableWfcRoamingMode = configForSubId.getBoolean("editable_wfc_roaming_mode_bool");
            this.mUseWfcHomeModeForRoaming = configForSubId.getBoolean("use_wfc_home_network_mode_in_roaming_network_bool", false);
            z = configForSubId.getBoolean("carrier_wfc_supports_wifi_only_bool", true);
            z2 = configForSubId.getBoolean("carrier_wfc_supports_ims_preferred_bool", false);
        }
        Resources resourcesForSubId = getResourcesForSubId();
        this.mButtonWfcMode.setTitle((CharSequence) resourcesForSubId.getString(R$string.wifi_calling_mode_title));
        this.mButtonWfcMode.setDialogTitle(resourcesForSubId.getString(R$string.wifi_calling_mode_dialog_title));
        this.mButtonWfcRoamingMode.setTitle((CharSequence) resourcesForSubId.getString(R$string.wifi_calling_roaming_mode_title));
        this.mButtonWfcRoamingMode.setDialogTitle(resourcesForSubId.getString(R$string.wifi_calling_roaming_mode_dialog_title));
        Log.d("WifiCallingForSub", "isWifiOnlySupported = " + z + " isImsPreferredSupported = " + z2);
        if (z) {
            if (z2) {
                this.mButtonWfcMode.setEntries((CharSequence[]) resourcesForSubId.getStringArray(R$array.wifi_calling_mode_choices_with_ims_preferred));
                ListWithEntrySummaryPreference listWithEntrySummaryPreference = this.mButtonWfcMode;
                int i = R$array.wifi_calling_mode_values_with_ims_preferred;
                listWithEntrySummaryPreference.setEntryValues(resourcesForSubId.getStringArray(i));
                ListWithEntrySummaryPreference listWithEntrySummaryPreference2 = this.mButtonWfcMode;
                int i2 = R$array.wifi_calling_mode_summaries_with_ims_preferred;
                listWithEntrySummaryPreference2.setEntrySummaries(resourcesForSubId.getStringArray(i2));
                this.mButtonWfcRoamingMode.setEntries((CharSequence[]) resourcesForSubId.getStringArray(R$array.wifi_calling_mode_choices_v2_with_ims_preferred));
                this.mButtonWfcRoamingMode.setEntryValues(resourcesForSubId.getStringArray(i));
                this.mButtonWfcRoamingMode.setEntrySummaries(resourcesForSubId.getStringArray(i2));
            } else {
                this.mButtonWfcMode.setEntries((CharSequence[]) resourcesForSubId.getStringArray(R$array.wifi_calling_mode_choices));
                ListWithEntrySummaryPreference listWithEntrySummaryPreference3 = this.mButtonWfcMode;
                int i3 = R$array.wifi_calling_mode_values;
                listWithEntrySummaryPreference3.setEntryValues(resourcesForSubId.getStringArray(i3));
                ListWithEntrySummaryPreference listWithEntrySummaryPreference4 = this.mButtonWfcMode;
                int i4 = R$array.wifi_calling_mode_summaries;
                listWithEntrySummaryPreference4.setEntrySummaries(resourcesForSubId.getStringArray(i4));
                this.mButtonWfcRoamingMode.setEntries((CharSequence[]) resourcesForSubId.getStringArray(R$array.wifi_calling_mode_choices_v2));
                this.mButtonWfcRoamingMode.setEntryValues(resourcesForSubId.getStringArray(i3));
                this.mButtonWfcRoamingMode.setEntrySummaries(resourcesForSubId.getStringArray(i4));
            }
        } else if (z2) {
            this.mButtonWfcMode.setEntries((CharSequence[]) resourcesForSubId.getStringArray(R$array.wifi_calling_mode_choices_without_wifi_only_with_ims_preferred));
            ListWithEntrySummaryPreference listWithEntrySummaryPreference5 = this.mButtonWfcMode;
            int i5 = R$array.wifi_calling_mode_values_without_wifi_only_with_ims_preferred;
            listWithEntrySummaryPreference5.setEntryValues(resourcesForSubId.getStringArray(i5));
            ListWithEntrySummaryPreference listWithEntrySummaryPreference6 = this.mButtonWfcMode;
            int i6 = R$array.wifi_calling_mode_summaries_without_wifi_only_with_ims_preferred;
            listWithEntrySummaryPreference6.setEntrySummaries(resourcesForSubId.getStringArray(i6));
            this.mButtonWfcRoamingMode.setEntries((CharSequence[]) resourcesForSubId.getStringArray(R$array.f135xcc03337));
            this.mButtonWfcRoamingMode.setEntryValues(resourcesForSubId.getStringArray(i5));
            this.mButtonWfcRoamingMode.setEntrySummaries(resourcesForSubId.getStringArray(i6));
        } else {
            this.mButtonWfcMode.setEntries((CharSequence[]) resourcesForSubId.getStringArray(R$array.wifi_calling_mode_choices_without_wifi_only));
            ListWithEntrySummaryPreference listWithEntrySummaryPreference7 = this.mButtonWfcMode;
            int i7 = R$array.wifi_calling_mode_values_without_wifi_only;
            listWithEntrySummaryPreference7.setEntryValues(resourcesForSubId.getStringArray(i7));
            ListWithEntrySummaryPreference listWithEntrySummaryPreference8 = this.mButtonWfcMode;
            int i8 = R$array.wifi_calling_mode_summaries_without_wifi_only;
            listWithEntrySummaryPreference8.setEntrySummaries(resourcesForSubId.getStringArray(i8));
            this.mButtonWfcRoamingMode.setEntries((CharSequence[]) resourcesForSubId.getStringArray(R$array.wifi_calling_mode_choices_v2_without_wifi_only));
            this.mButtonWfcRoamingMode.setEntryValues(resourcesForSubId.getStringArray(i7));
            this.mButtonWfcRoamingMode.setEntrySummaries(resourcesForSubId.getStringArray(i8));
        }
        WifiCallingQueryImsState queryImsState = queryImsState(this.mSubId);
        if (!queryImsState.isEnabledByUser() || !queryImsState.isAllowUserControl()) {
            z3 = false;
        }
        this.mSwitchBar.setChecked(z3);
        int voWiFiModeSetting = this.mImsMmTelManager.getVoWiFiModeSetting();
        int voWiFiRoamingModeSetting = this.mImsMmTelManager.getVoWiFiRoamingModeSetting();
        this.mButtonWfcMode.setValue(Integer.toString(voWiFiModeSetting));
        this.mButtonWfcRoamingMode.setValue(Integer.toString(voWiFiRoamingModeSetting));
        updateButtonWfcMode(z3, voWiFiModeSetting, voWiFiRoamingModeSetting);
    }

    public void onResume() {
        super.onResume();
        updateBody();
        FragmentActivity activity = getActivity();
        if (queryImsState(this.mSubId).isWifiCallingSupported()) {
            getTelephonyManagerForSub(this.mSubId).registerTelephonyCallback(activity.getMainExecutor(), this.mTelephonyCallback);
            this.mSwitchBar.addOnSwitchChangeListener(this);
            this.mValidListener = true;
        }
        activity.registerReceiver(this.mIntentReceiver, this.mIntentFilter, 2);
        Intent intent = getActivity().getIntent();
        if (intent.getBooleanExtra("alertShow", false)) {
            showAlert(intent);
        }
        registerProvisioningChangedCallback();
    }

    public void onPause() {
        super.onPause();
        FragmentActivity activity = getActivity();
        if (this.mValidListener) {
            this.mValidListener = false;
            getTelephonyManagerForSub(this.mSubId).unregisterTelephonyCallback(this.mTelephonyCallback);
            this.mSwitchBar.removeOnSwitchChangeListener(this);
        }
        activity.unregisterReceiver(this.mIntentReceiver);
        unregisterProvisioningChangedCallback();
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        Log.d("WifiCallingForSub", "onSwitchChanged(" + z + ")");
        if (!z) {
            updateWfcMode(false);
            return;
        }
        FragmentActivity activity = getActivity();
        Bundle bundle = new Bundle();
        bundle.putInt("EXTRA_SUB_ID", this.mSubId);
        new SubSettingLauncher(activity).setDestination(WifiCallingDisclaimerFragment.class.getName()).setArguments(bundle).setTitleRes(R$string.wifi_calling_settings_title).setSourceMetricsCategory(getMetricsCategory()).setResultListener(this, 2).launch();
    }

    private Intent getCarrierActivityIntent() {
        PersistableBundle configForSubId;
        ComponentName unflattenFromString;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) getActivity().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(this.mSubId)) == null) {
            return null;
        }
        String string = configForSubId.getString("wfc_emergency_address_carrier_app_string");
        if (TextUtils.isEmpty(string) || (unflattenFromString = ComponentName.unflattenFromString(string)) == null) {
            return null;
        }
        Intent intent = new Intent();
        intent.setComponent(unflattenFromString);
        intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", this.mSubId);
        return intent;
    }

    private void updateWfcMode(boolean z) {
        Log.i("WifiCallingForSub", "updateWfcMode(" + z + ")");
        this.mImsMmTelManager.setVoWiFiSettingEnabled(z);
        int voWiFiModeSetting = this.mImsMmTelManager.getVoWiFiModeSetting();
        updateButtonWfcMode(z, voWiFiModeSetting, this.mImsMmTelManager.getVoWiFiRoamingModeSetting());
        if (z) {
            this.mMetricsFeatureProvider.action((Context) getActivity(), getMetricsCategory(), voWiFiModeSetting);
        } else {
            this.mMetricsFeatureProvider.action((Context) getActivity(), getMetricsCategory(), -1);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Log.d("WifiCallingForSub", "WFC activity request = " + i + " result = " + i2);
        if (i != 1) {
            if (i != 2) {
                Log.e("WifiCallingForSub", "Unexpected request: " + i);
            } else if (i2 == -1) {
                Intent carrierActivityIntent = getCarrierActivityIntent();
                if (carrierActivityIntent != null) {
                    carrierActivityIntent.putExtra("EXTRA_LAUNCH_CARRIER_APP", 0);
                    startActivityForResult(carrierActivityIntent, 1);
                    return;
                }
                updateWfcMode(true);
            }
        } else if (i2 == -1) {
            updateWfcMode(true);
        }
    }

    private void updateButtonWfcMode(boolean z, int i, int i2) {
        this.mButtonWfcMode.setSummary(getWfcModeSummary(i));
        boolean z2 = true;
        this.mButtonWfcMode.setEnabled(z && this.mEditableWfcMode);
        this.mButtonWfcRoamingMode.setEnabled(z && this.mEditableWfcRoamingMode);
        getPreferenceScreen();
        boolean z3 = getCarrierActivityIntent() != null;
        if (z) {
            this.mButtonWfcMode.setVisible(this.mEditableWfcMode);
            ListWithEntrySummaryPreference listWithEntrySummaryPreference = this.mButtonWfcRoamingMode;
            if (!this.mEditableWfcRoamingMode || this.mUseWfcHomeModeForRoaming) {
                z2 = false;
            }
            listWithEntrySummaryPreference.setVisible(z2);
            this.mUpdateAddress.setVisible(z3);
        } else {
            this.mButtonWfcMode.setVisible(false);
            this.mButtonWfcRoamingMode.setVisible(false);
            this.mUpdateAddress.setVisible(false);
        }
        updateDescriptionForOptions(List.of(this.mButtonWfcMode, this.mButtonWfcRoamingMode, this.mUpdateAddress));
    }

    private void updateDescriptionForOptions(List<Preference> list) {
        LinkifyDescriptionPreference linkifyDescriptionPreference = (LinkifyDescriptionPreference) findPreference("no_options_description");
        if (linkifyDescriptionPreference != null) {
            boolean anyMatch = list.stream().anyMatch(new WifiCallingSettingsForSub$$ExternalSyntheticLambda1());
            if (!anyMatch) {
                Resources resourcesForSubId = getResourcesForSubId();
                linkifyDescriptionPreference.setSummary((CharSequence) resourcesForSubId.getString(R$string.wifi_calling_off_explanation, new Object[]{resourcesForSubId.getString(R$string.wifi_calling_off_explanation_2)}));
            }
            linkifyDescriptionPreference.setVisible(!anyMatch);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference == this.mButtonWfcMode) {
            Log.d("WifiCallingForSub", "onPreferenceChange mButtonWfcMode " + obj);
            String str = (String) obj;
            this.mButtonWfcMode.setValue(str);
            int intValue = Integer.valueOf(str).intValue();
            if (intValue == this.mImsMmTelManager.getVoWiFiModeSetting()) {
                return true;
            }
            this.mImsMmTelManager.setVoWiFiModeSetting(intValue);
            this.mButtonWfcMode.setSummary(getWfcModeSummary(intValue));
            this.mMetricsFeatureProvider.action((Context) getActivity(), getMetricsCategory(), intValue);
            if (!this.mUseWfcHomeModeForRoaming) {
                return true;
            }
            this.mImsMmTelManager.setVoWiFiRoamingModeSetting(intValue);
            return true;
        }
        ListWithEntrySummaryPreference listWithEntrySummaryPreference = this.mButtonWfcRoamingMode;
        if (preference != listWithEntrySummaryPreference) {
            return true;
        }
        String str2 = (String) obj;
        listWithEntrySummaryPreference.setValue(str2);
        int intValue2 = Integer.valueOf(str2).intValue();
        if (intValue2 == this.mImsMmTelManager.getVoWiFiRoamingModeSetting()) {
            return true;
        }
        this.mImsMmTelManager.setVoWiFiRoamingModeSetting(intValue2);
        this.mMetricsFeatureProvider.action((Context) getActivity(), getMetricsCategory(), intValue2);
        return true;
    }

    private CharSequence getWfcModeSummary(int i) {
        int i2;
        if (queryImsState(this.mSubId).isEnabledByUser()) {
            if (i == 0) {
                i2 = 17041739;
            } else if (i == 1) {
                i2 = 17041737;
            } else if (i == 2) {
                i2 = 17041740;
            } else if (i != 10) {
                Log.e("WifiCallingForSub", "Unexpected WFC mode value: " + i);
            } else {
                i2 = 17041738;
            }
            return getResourcesForSubId().getString(i2);
        }
        i2 = 17041770;
        return getResourcesForSubId().getString(i2);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public Resources getResourcesForSubId() {
        return SubscriptionManager.getResourcesForSubId(getContext(), this.mSubId);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void registerProvisioningChangedCallback() {
        ProvisioningManager provisioningManager = this.mProvisioningManager;
        if (provisioningManager != null) {
            try {
                provisioningManager.registerProvisioningChangedCallback(getContext().getMainExecutor(), this.mProvisioningCallback);
            } catch (Exception unused) {
                Log.w("WifiCallingForSub", "onResume: Unable to register callback for provisioning changes.");
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void unregisterProvisioningChangedCallback() {
        ProvisioningManager provisioningManager = this.mProvisioningManager;
        if (provisioningManager != null) {
            provisioningManager.unregisterProvisioningChangedCallback(this.mProvisioningCallback);
        }
    }
}
