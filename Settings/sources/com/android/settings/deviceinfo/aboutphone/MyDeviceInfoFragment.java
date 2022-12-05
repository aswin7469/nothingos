package com.android.settings.deviceinfo.aboutphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.deviceinfo.BluetoothAddressPreferenceController;
import com.android.settings.deviceinfo.FccEquipmentIdPreferenceController;
import com.android.settings.deviceinfo.FeedbackPreferenceController;
import com.android.settings.deviceinfo.IpAddressPreferenceController;
import com.android.settings.deviceinfo.ManualPreferenceController;
import com.android.settings.deviceinfo.RegulatoryInfoPreferenceController;
import com.android.settings.deviceinfo.SafetyInfoPreferenceController;
import com.android.settings.deviceinfo.SoftwareVersionPreferenceController;
import com.android.settings.deviceinfo.StorageSizePreferenceController;
import com.android.settings.deviceinfo.UptimePreferenceController;
import com.android.settings.deviceinfo.WifiMacAddressPreferenceController;
import com.android.settings.deviceinfo.simstatus.SimStatusPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nt.settings.about.NtDeviceNamePreferenceController;
import com.nt.settings.about.NtDeviceSeeAllPreferenceController;
import com.nt.settings.about.NtImeiInfoPreferenceController;
import com.nt.settings.about.NtSimsPreferenceController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class MyDeviceInfoFragment extends DashboardFragment implements NtDeviceNamePreferenceController.DeviceNamePreferenceHost {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_my_device_info) { // from class: com.android.settings.deviceinfo.aboutphone.MyDeviceInfoFragment.2
        @Override // com.android.settings.search.BaseSearchIndexProvider
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return MyDeviceInfoFragment.buildPreferenceControllers(context, null, null);
        }
    };
    private final BroadcastReceiver mSimStateReceiver = new BroadcastReceiver() { // from class: com.android.settings.deviceinfo.aboutphone.MyDeviceInfoFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("ss");
                Log.d("MyDeviceInfoFragment", "Received ACTION_SIM_STATE_CHANGED: " + stringExtra);
                MyDeviceInfoFragment.this.updatePreferenceStates();
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "MyDeviceInfoFragment";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 40;
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return R.string.help_uri_about;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        ((NtDeviceNamePreferenceController) use(NtDeviceNamePreferenceController.class)).setHost(this);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        Context context = getContext();
        if (context != null) {
            context.unregisterReceiver(this.mSimStateReceiver);
        } else {
            Log.i("MyDeviceInfoFragment", "context already null, not unregistering SimStateReceiver");
        }
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        Context context = getContext();
        if (context != null) {
            context.registerReceiver(this.mSimStateReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"));
        } else {
            Log.i("MyDeviceInfoFragment", "context is null, not registering SimStateReceiver");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_my_device_info;
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, this, getSettingsLifecycle());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, MyDeviceInfoFragment myDeviceInfoFragment, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new SimStatusPreferenceController(context, myDeviceInfoFragment));
        arrayList.add(new IpAddressPreferenceController(context, lifecycle));
        arrayList.add(new WifiMacAddressPreferenceController(context, lifecycle));
        arrayList.add(new BluetoothAddressPreferenceController(context, lifecycle));
        arrayList.add(new RegulatoryInfoPreferenceController(context));
        arrayList.add(new SafetyInfoPreferenceController(context));
        arrayList.add(new ManualPreferenceController(context));
        arrayList.add(new FeedbackPreferenceController(myDeviceInfoFragment, context));
        arrayList.add(new FccEquipmentIdPreferenceController(context));
        arrayList.add(new UptimePreferenceController(context, lifecycle));
        arrayList.add(new SoftwareVersionPreferenceController(context));
        arrayList.add(new StorageSizePreferenceController(context));
        arrayList.add(new NtSimsPreferenceController(context, myDeviceInfoFragment));
        arrayList.add(new NtImeiInfoPreferenceController(context, myDeviceInfoFragment));
        arrayList.add(new NtDeviceSeeAllPreferenceController(context));
        return arrayList;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    @Override // com.nt.settings.about.NtDeviceNamePreferenceController.DeviceNamePreferenceHost
    public void showDeviceNameWarningDialog(String str) {
        DeviceNameWarningDialog.show(this);
    }

    public void onSetDeviceNameConfirm(boolean z) {
        ((NtDeviceNamePreferenceController) use(NtDeviceNamePreferenceController.class)).updateDeviceName(z);
    }
}
