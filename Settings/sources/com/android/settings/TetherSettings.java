package com.android.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothPan;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.TetheringManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.util.FeatureFlagUtils;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.wifi.tether.WifiTetherPreferenceController;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.TetherUtil;
import com.nothing.p006ui.support.NtCustSwitchPreference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TetherSettings extends RestrictedSettingsFragment implements DataSaverBackend.Listener {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable("TetheringSettings", 3);
    static final String KEY_ENABLE_BLUETOOTH_TETHERING = "enable_bluetooth_tethering";
    static final String KEY_TETHER_PREFS_SCREEN = "tether_prefs_screen";
    static final String KEY_TETHER_PREFS_TOP_INTRO = "tether_prefs_top_intro";
    static final String KEY_USB_TETHER_SETTINGS = "usb_tether_settings";
    static final String KEY_WIFI_TETHER = "wifi_tether";
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = R$xml.tether_prefs;
            return Arrays.asList(new SearchIndexableResource[]{searchIndexableResource});
        }

        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return !FeatureFlagUtils.isEnabled(context, "settings_tether_all_in_one");
        }

        public List<String> getNonIndexableKeys(Context context) {
            List<String> nonIndexableKeys = super.getNonIndexableKeys(context);
            TetheringManager tetheringManager = (TetheringManager) context.getSystemService(TetheringManager.class);
            if (!TetherUtil.isTetherAvailable(context)) {
                nonIndexableKeys.add(TetherSettings.KEY_TETHER_PREFS_SCREEN);
                nonIndexableKeys.add(TetherSettings.KEY_WIFI_TETHER);
            }
            boolean z = true;
            if (!(tetheringManager.getTetherableUsbRegexs().length != 0) || Utils.isMonkeyRunning()) {
                nonIndexableKeys.add(TetherSettings.KEY_USB_TETHER_SETTINGS);
            }
            if (!(tetheringManager.getTetherableBluetoothRegexs().length != 0)) {
                nonIndexableKeys.add(TetherSettings.KEY_ENABLE_BLUETOOTH_TETHERING);
            }
            if (((EthernetManager) context.getSystemService(EthernetManager.class)) == null) {
                z = false;
            }
            if (!z) {
                nonIndexableKeys.add("enable_ethernet_tethering");
            }
            return nonIndexableKeys;
        }
    };
    /* access modifiers changed from: private */
    public final HashSet<String> mAvailableInterfaces = new HashSet<>();
    /* access modifiers changed from: private */
    public boolean mBluetoothEnableForTether;
    /* access modifiers changed from: private */
    public AtomicReference<BluetoothPan> mBluetoothPan = new AtomicReference<>();
    private String[] mBluetoothRegexs;
    private NtCustSwitchPreference mBluetoothTether;
    private ConnectivityManager mCm;
    Context mContext;
    private DataSaverBackend mDataSaverBackend;
    private boolean mDataSaverEnabled;
    private Preference mDataSaverFooter;
    private EthernetManager mEm;
    private EthernetListener mEthernetListener;
    private NtCustSwitchPreference mEthernetTether;
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public boolean mMassStorageActive;
    private BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            TetherSettings.this.mBluetoothPan.set((BluetoothPan) bluetoothProfile);
        }

        public void onServiceDisconnected(int i) {
            TetherSettings.this.mBluetoothPan.set((Object) null);
        }
    };
    private OnStartTetheringCallback mStartTetheringCallback;
    private BroadcastReceiver mTetherChangeReceiver;
    private TetheringEventCallback mTetheringEventCallback;
    TetheringManager mTm;
    private boolean mUnavailable;
    /* access modifiers changed from: private */
    public boolean mUsbConnected;
    String[] mUsbRegexs;
    private RestrictedSwitchPreference mUsbTether;
    private WifiTetherPreferenceController mWifiTetherPreferenceController;

    public int getMetricsCategory() {
        return 90;
    }

    public void onAllowlistStatusChanged(int i, boolean z) {
    }

    public void onDenylistStatusChanged(int i, boolean z) {
    }

    public TetherSettings() {
        super("no_config_tethering");
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mWifiTetherPreferenceController = new WifiTetherPreferenceController(context, getSettingsLifecycle());
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R$xml.tether_prefs);
        Context context = getContext();
        this.mContext = context;
        DataSaverBackend dataSaverBackend = new DataSaverBackend(context);
        this.mDataSaverBackend = dataSaverBackend;
        this.mDataSaverEnabled = dataSaverBackend.isDataSaverEnabled();
        this.mDataSaverFooter = findPreference("disabled_on_data_saver");
        setIfOnlyAvailableForAdmins(true);
        if (isUiRestricted()) {
            this.mUnavailable = true;
            getPreferenceScreen().removeAll();
            return;
        }
        FragmentActivity activity = getActivity();
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            defaultAdapter.getProfileProxy(activity.getApplicationContext(), this.mProfileServiceListener, 5);
        }
        setupTetherPreference();
        setTopIntroPreferenceTitle();
        this.mDataSaverBackend.addListener(this);
        this.mCm = (ConnectivityManager) getSystemService("connectivity");
        this.mTm = (TetheringManager) getSystemService("tethering");
        this.mEm = (EthernetManager) this.mContext.getSystemService(EthernetManager.class);
        this.mUsbRegexs = this.mTm.getTetherableUsbRegexs();
        String[] tetherableBluetoothRegexs = this.mTm.getTetherableBluetoothRegexs();
        this.mBluetoothRegexs = tetherableBluetoothRegexs;
        boolean z = this.mUsbRegexs.length != 0;
        boolean z2 = (defaultAdapter == null || tetherableBluetoothRegexs.length == 0) ? false : true;
        boolean z3 = this.mEm != null;
        if (!z || Utils.isMonkeyRunning()) {
            getPreferenceScreen().removePreference(this.mUsbTether);
        }
        this.mWifiTetherPreferenceController.displayPreference(getPreferenceScreen());
        if (!z2) {
            getPreferenceScreen().removePreference(this.mBluetoothTether);
        } else {
            BluetoothPan bluetoothPan = this.mBluetoothPan.get();
            if (bluetoothPan == null || !bluetoothPan.isTetheringOn()) {
                this.mBluetoothTether.setChecked(false);
            } else {
                this.mBluetoothTether.setChecked(true);
            }
        }
        if (!z3) {
            getPreferenceScreen().removePreference(this.mEthernetTether);
        }
        onDataSaverChanged(this.mDataSaverBackend.isDataSaverEnabled());
    }

    public void onDestroy() {
        this.mDataSaverBackend.remListener(this);
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothProfile andSet = this.mBluetoothPan.getAndSet((Object) null);
        if (!(andSet == null || defaultAdapter == null)) {
            defaultAdapter.closeProfileProxy(5, andSet);
        }
        super.onDestroy();
    }

    /* access modifiers changed from: package-private */
    public void setupTetherPreference() {
        this.mUsbTether = (RestrictedSwitchPreference) findPreference(KEY_USB_TETHER_SETTINGS);
        this.mBluetoothTether = (NtCustSwitchPreference) findPreference(KEY_ENABLE_BLUETOOTH_TETHERING);
        this.mEthernetTether = (NtCustSwitchPreference) findPreference("enable_ethernet_tethering");
    }

    public void onDataSaverChanged(boolean z) {
        this.mDataSaverEnabled = z;
        this.mUsbTether.setEnabled(!z);
        this.mBluetoothTether.setEnabled(!this.mDataSaverEnabled);
        this.mEthernetTether.setEnabled(!this.mDataSaverEnabled);
        this.mDataSaverFooter.setVisible(this.mDataSaverEnabled);
    }

    /* access modifiers changed from: package-private */
    public void setTopIntroPreferenceTitle() {
        Preference findPreference = findPreference(KEY_TETHER_PREFS_TOP_INTRO);
        if (((WifiManager) this.mContext.getSystemService(WifiManager.class)).isStaApConcurrencySupported()) {
            findPreference.setTitle(R$string.tethering_footer_info_sta_ap_concurrency);
        } else {
            findPreference.setTitle(R$string.tethering_footer_info);
        }
    }

    private class TetherChangeReceiver extends BroadcastReceiver {
        private TetherChangeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TetherSettings.DEBUG) {
                Log.d("TetheringSettings", "onReceive() action : " + action);
            }
            if (action.equals("android.net.conn.TETHER_STATE_CHANGED")) {
                ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("availableArray");
                ArrayList<String> stringArrayListExtra2 = intent.getStringArrayListExtra("tetherArray");
                TetherSettings.this.updateBluetoothState();
                TetherSettings.this.updateEthernetState((String[]) stringArrayListExtra.toArray(new String[stringArrayListExtra.size()]), (String[]) stringArrayListExtra2.toArray(new String[stringArrayListExtra2.size()]));
            } else if (action.equals("android.intent.action.MEDIA_SHARED")) {
                TetherSettings.this.mMassStorageActive = true;
                TetherSettings.this.updateBluetoothAndEthernetState();
                TetherSettings.this.updateUsbPreference();
            } else if (action.equals("android.intent.action.MEDIA_UNSHARED")) {
                TetherSettings.this.mMassStorageActive = false;
                TetherSettings.this.updateBluetoothAndEthernetState();
                TetherSettings.this.updateUsbPreference();
            } else if (action.equals("android.hardware.usb.action.USB_STATE")) {
                TetherSettings.this.mUsbConnected = intent.getBooleanExtra("connected", false);
                TetherSettings.this.updateBluetoothAndEthernetState();
                TetherSettings.this.updateUsbPreference();
            } else if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                if (TetherSettings.this.mBluetoothEnableForTether) {
                    int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                    if (intExtra == Integer.MIN_VALUE || intExtra == 10) {
                        TetherSettings.this.mBluetoothEnableForTether = false;
                    } else if (intExtra == 12) {
                        TetherSettings.this.startTethering(2);
                        TetherSettings.this.mBluetoothEnableForTether = false;
                    }
                }
                TetherSettings.this.updateBluetoothAndEthernetState();
            } else if (action.equals("android.bluetooth.action.TETHERING_STATE_CHANGED")) {
                TetherSettings.this.updateBluetoothAndEthernetState();
            }
        }
    }

    public void onStart() {
        super.onStart();
        if (this.mUnavailable) {
            if (!isUiRestrictedByOnlyAdmin()) {
                getEmptyTextView().setText(R$string.tethering_settings_not_available);
            }
            getPreferenceScreen().removeAll();
            return;
        }
        this.mStartTetheringCallback = new OnStartTetheringCallback(this);
        this.mTetheringEventCallback = new TetheringEventCallback();
        this.mTm.registerTetheringEventCallback(new TetherSettings$$ExternalSyntheticLambda0(this), this.mTetheringEventCallback);
        this.mMassStorageActive = "shared".equals(Environment.getExternalStorageState());
        registerReceiver();
        this.mEthernetListener = new EthernetListener();
        EthernetManager ethernetManager = this.mEm;
        if (ethernetManager != null) {
            ethernetManager.addInterfaceStateListener(new TetherSettings$$ExternalSyntheticLambda1(this), this.mEthernetListener);
        }
        updateUsbState();
        updateBluetoothAndEthernetState();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$1(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public void onStop() {
        super.onStop();
        if (!this.mUnavailable) {
            getActivity().unregisterReceiver(this.mTetherChangeReceiver);
            this.mTm.unregisterTetheringEventCallback(this.mTetheringEventCallback);
            EthernetManager ethernetManager = this.mEm;
            if (ethernetManager != null) {
                ethernetManager.removeInterfaceStateListener(this.mEthernetListener);
            }
            this.mTetherChangeReceiver = null;
            this.mStartTetheringCallback = null;
            this.mTetheringEventCallback = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void registerReceiver() {
        FragmentActivity activity = getActivity();
        this.mTetherChangeReceiver = new TetherChangeReceiver();
        Intent registerReceiver = activity.registerReceiver(this.mTetherChangeReceiver, new IntentFilter("android.net.conn.TETHER_STATE_CHANGED"));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.hardware.usb.action.USB_STATE");
        activity.registerReceiver(this.mTetherChangeReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.MEDIA_SHARED");
        intentFilter2.addAction("android.intent.action.MEDIA_UNSHARED");
        intentFilter2.addDataScheme("file");
        activity.registerReceiver(this.mTetherChangeReceiver, intentFilter2);
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter3.addAction("android.bluetooth.action.TETHERING_STATE_CHANGED");
        activity.registerReceiver(this.mTetherChangeReceiver, intentFilter3);
        if (registerReceiver != null) {
            this.mTetherChangeReceiver.onReceive(activity, registerReceiver);
        }
    }

    /* access modifiers changed from: private */
    public void updateBluetoothAndEthernetState() {
        updateBluetoothAndEthernetState(this.mTm.getTetheredIfaces());
    }

    /* access modifiers changed from: private */
    public void updateBluetoothAndEthernetState(String[] strArr) {
        String[] tetherableIfaces = this.mTm.getTetherableIfaces();
        updateBluetoothState();
        updateEthernetState(tetherableIfaces, strArr);
    }

    private void updateUsbState() {
        updateUsbState(this.mTm.getTetheredIfaces());
    }

    /* access modifiers changed from: package-private */
    public void updateUsbState(String[] strArr) {
        boolean z = false;
        for (String str : strArr) {
            for (String matches : this.mUsbRegexs) {
                if (str.matches(matches)) {
                    z = true;
                }
            }
        }
        if (DEBUG) {
            Log.d("TetheringSettings", "updateUsbState() mUsbConnected : " + this.mUsbConnected + ", mMassStorageActive : " + this.mMassStorageActive + ", usbTethered : " + z);
        }
        if (z) {
            this.mUsbTether.setEnabled(!this.mDataSaverEnabled);
            this.mUsbTether.setChecked(true);
            RestrictedLockUtils.EnforcedAdmin checkIfUsbDataSignalingIsDisabled = RestrictedLockUtilsInternal.checkIfUsbDataSignalingIsDisabled(this.mContext, UserHandle.myUserId());
            if (checkIfUsbDataSignalingIsDisabled != null) {
                this.mUsbTether.setDisabledByAdmin(checkIfUsbDataSignalingIsDisabled);
                return;
            }
            return;
        }
        this.mUsbTether.setChecked(false);
        updateUsbPreference();
    }

    /* access modifiers changed from: private */
    public void updateUsbPreference() {
        boolean z = this.mUsbConnected && !this.mMassStorageActive;
        RestrictedLockUtils.EnforcedAdmin checkIfUsbDataSignalingIsDisabled = RestrictedLockUtilsInternal.checkIfUsbDataSignalingIsDisabled(this.mContext, UserHandle.myUserId());
        if (checkIfUsbDataSignalingIsDisabled != null) {
            this.mUsbTether.setDisabledByAdmin(checkIfUsbDataSignalingIsDisabled);
        } else if (z) {
            this.mUsbTether.setEnabled(!this.mDataSaverEnabled);
        } else {
            this.mUsbTether.setEnabled(false);
        }
    }

    /* access modifiers changed from: package-private */
    public int getBluetoothState() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) {
            return Integer.MIN_VALUE;
        }
        return defaultAdapter.getState();
    }

    /* access modifiers changed from: package-private */
    public boolean isBluetoothTetheringOn() {
        BluetoothPan bluetoothPan = this.mBluetoothPan.get();
        return bluetoothPan != null && bluetoothPan.isTetheringOn();
    }

    /* access modifiers changed from: private */
    public void updateBluetoothState() {
        int bluetoothState = getBluetoothState();
        if (DEBUG) {
            Log.d("TetheringSettings", "updateBluetoothState() btState : " + bluetoothState);
        }
        if (bluetoothState == Integer.MIN_VALUE) {
            Log.w("TetheringSettings", "updateBluetoothState() Bluetooth state is error!");
        } else if (bluetoothState == 13) {
            this.mBluetoothTether.setEnabled(false);
        } else if (bluetoothState == 11) {
            this.mBluetoothTether.setEnabled(false);
        } else if (bluetoothState != 12 || !isBluetoothTetheringOn()) {
            this.mBluetoothTether.setEnabled(!this.mDataSaverEnabled);
            this.mBluetoothTether.setChecked(false);
        } else {
            this.mBluetoothTether.setChecked(true);
            this.mBluetoothTether.setEnabled(!this.mDataSaverEnabled);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateEthernetState(String[] strArr, String[] strArr2) {
        boolean z = false;
        for (String contains : strArr) {
            if (this.mAvailableInterfaces.contains(contains)) {
                z = true;
            }
        }
        boolean z2 = false;
        for (String contains2 : strArr2) {
            if (this.mAvailableInterfaces.contains(contains2)) {
                z2 = true;
            }
        }
        if (DEBUG) {
            Log.d("TetheringSettings", "updateEthernetState() isAvailable : " + z + ", isTethered : " + z2);
        }
        if (z2) {
            this.mEthernetTether.setEnabled(!this.mDataSaverEnabled);
            this.mEthernetTether.setChecked(true);
        } else if (this.mAvailableInterfaces.size() > 0) {
            this.mEthernetTether.setEnabled(!this.mDataSaverEnabled);
            this.mEthernetTether.setChecked(false);
        } else {
            this.mEthernetTether.setEnabled(false);
            this.mEthernetTether.setChecked(false);
        }
    }

    /* access modifiers changed from: private */
    public void startTethering(int i) {
        if (i == 2) {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter.getState() == 10) {
                this.mBluetoothEnableForTether = true;
                defaultAdapter.enable();
                this.mBluetoothTether.setEnabled(false);
                return;
            }
        }
        this.mCm.startTethering(i, true, this.mStartTetheringCallback, this.mHandler);
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        RestrictedSwitchPreference restrictedSwitchPreference = this.mUsbTether;
        if (preference != restrictedSwitchPreference) {
            NtCustSwitchPreference ntCustSwitchPreference = this.mBluetoothTether;
            if (preference != ntCustSwitchPreference) {
                NtCustSwitchPreference ntCustSwitchPreference2 = this.mEthernetTether;
                if (preference == ntCustSwitchPreference2) {
                    if (ntCustSwitchPreference2.isChecked()) {
                        startTethering(5);
                    } else {
                        this.mCm.stopTethering(5);
                    }
                }
            } else if (ntCustSwitchPreference.isChecked()) {
                startTethering(2);
            } else {
                this.mCm.stopTethering(2);
            }
        } else if (restrictedSwitchPreference.isChecked()) {
            startTethering(1);
        } else {
            this.mCm.stopTethering(1);
        }
        return super.onPreferenceTreeClick(preference);
    }

    public int getHelpResource() {
        return R$string.help_url_tether;
    }

    private static final class OnStartTetheringCallback extends ConnectivityManager.OnStartTetheringCallback {
        final WeakReference<TetherSettings> mTetherSettings;

        OnStartTetheringCallback(TetherSettings tetherSettings) {
            this.mTetherSettings = new WeakReference<>(tetherSettings);
        }

        public void onTetheringStarted() {
            update();
        }

        public void onTetheringFailed() {
            update();
        }

        private void update() {
            TetherSettings tetherSettings = (TetherSettings) this.mTetherSettings.get();
            if (tetherSettings != null) {
                tetherSettings.updateBluetoothAndEthernetState();
            }
        }
    }

    private final class TetheringEventCallback implements TetheringManager.TetheringEventCallback {
        private TetheringEventCallback() {
        }

        public void onTetheredInterfacesChanged(List<String> list) {
            Log.d("TetheringSettings", "onTetheredInterfacesChanged() interfaces : " + list.toString());
            String[] strArr = (String[]) list.toArray(new String[list.size()]);
            TetherSettings.this.updateUsbState(strArr);
            TetherSettings.this.updateBluetoothAndEthernetState(strArr);
        }
    }

    private final class EthernetListener implements EthernetManager.InterfaceStateListener {
        private EthernetListener() {
        }

        public void onInterfaceStateChanged(String str, int i, int i2, IpConfiguration ipConfiguration) {
            if (i == 2) {
                TetherSettings.this.mAvailableInterfaces.add(str);
            } else {
                TetherSettings.this.mAvailableInterfaces.remove(str);
            }
            TetherSettings.this.updateBluetoothAndEthernetState();
        }
    }
}
