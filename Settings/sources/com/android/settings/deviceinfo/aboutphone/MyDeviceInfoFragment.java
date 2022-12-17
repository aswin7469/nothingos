package com.android.settings.deviceinfo.aboutphone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.Process;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R$bool;
import com.android.settings.R$id;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.Utils;
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
import com.android.settings.widget.EntityHeaderController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.LayoutPreference;
import com.nothing.settings.deviceinfo.DeviceNamePreferenceController;
import com.nothing.settings.deviceinfo.aboutphone.DeviceSeeAllPreferenceController;
import com.nothing.settings.deviceinfo.aboutphone.DeviceSimPreferenceController;
import com.nothing.settings.deviceinfo.aboutphone.ImeiInfoPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class MyDeviceInfoFragment extends DashboardFragment implements DeviceNamePreferenceController.DeviceNamePreferenceHost {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.my_device_info) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return MyDeviceInfoFragment.buildPreferenceControllers(context, (MyDeviceInfoFragment) null, (Lifecycle) null);
        }
    };
    private final BroadcastReceiver mSimStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("ss");
                Log.d("MyDeviceInfoFragment", "Received ACTION_SIM_STATE_CHANGED: " + stringExtra);
                MyDeviceInfoFragment.this.updatePreferenceStates();
            }
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "MyDeviceInfoFragment";
    }

    public int getMetricsCategory() {
        return 40;
    }

    public int getHelpResource() {
        return R$string.help_uri_about;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((DeviceNamePreferenceController) use(DeviceNamePreferenceController.class)).setHost(this);
    }

    public void onStart() {
        super.onStart();
        initHeader();
    }

    public void onPause() {
        super.onPause();
        Context context = getContext();
        if (context != null) {
            context.unregisterReceiver(this.mSimStateReceiver);
        } else {
            Log.i("MyDeviceInfoFragment", "context already null, not unregistering SimStateReceiver");
        }
    }

    public void onResume() {
        super.onResume();
        Context context = getContext();
        if (context != null) {
            context.registerReceiver(this.mSimStateReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"));
        } else {
            Log.i("MyDeviceInfoFragment", "context is null, not registering SimStateReceiver");
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.nt_my_device_info;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, this, getSettingsLifecycle());
    }

    /* access modifiers changed from: private */
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
        arrayList.add(new ImeiInfoPreferenceController(context, myDeviceInfoFragment));
        arrayList.add(new DeviceSeeAllPreferenceController(context));
        arrayList.add(new DeviceSimPreferenceController(context, myDeviceInfoFragment));
        return arrayList;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    private void initHeader() {
        LayoutPreference layoutPreference = (LayoutPreference) getPreferenceScreen().findPreference("my_device_info_header");
        boolean z = getContext().getResources().getBoolean(R$bool.config_show_device_header_in_device_info);
        layoutPreference.setVisible(z);
        if (z) {
            View findViewById = layoutPreference.findViewById(R$id.entity_header);
            FragmentActivity activity = getActivity();
            Bundle arguments = getArguments();
            EntityHeaderController buttonActions = EntityHeaderController.newInstance(activity, this, findViewById).setRecyclerView(getListView(), getSettingsLifecycle()).setButtonActions(0, 0);
            if (arguments.getInt("icon_id", 0) == 0) {
                UserManager userManager = (UserManager) getActivity().getSystemService("user");
                UserInfo existingUser = Utils.getExistingUser(userManager, Process.myUserHandle());
                buttonActions.setLabel((CharSequence) existingUser.name);
                buttonActions.setIcon(com.android.settingslib.Utils.getUserIcon(getActivity(), userManager, existingUser));
            }
            buttonActions.done((Activity) activity, true);
        }
    }

    public void showDeviceNameWarningDialog(String str) {
        DeviceNameWarningDialog.show(this);
    }

    public void onSetDeviceNameConfirm(boolean z) {
        ((DeviceNamePreferenceController) use(DeviceNamePreferenceController.class)).updateDeviceName(z);
    }
}
