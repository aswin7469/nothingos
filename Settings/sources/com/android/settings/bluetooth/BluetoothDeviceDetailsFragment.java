package com.android.settings.bluetooth;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.BlockingSlicePrefController;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class BluetoothDeviceDetailsFragment extends RestrictedDashboardFragment {
    static int EDIT_DEVICE_NAME_ITEM_ID = 1;
    private static boolean mBAEnabled = false;
    private static boolean mBAPropertyChecked = false;
    static TestDataFactory sTestDataFactory;
    CachedBluetoothDevice mCachedDevice;
    String mDeviceAddress;
    LocalBluetoothManager mManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface TestDataFactory {
        CachedBluetoothDevice getDevice(String str);

        LocalBluetoothManager getManager(Context context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "BTDeviceDetailsFrg";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1009;
    }

    public BluetoothDeviceDetailsFragment() {
        super("no_config_bluetooth");
    }

    LocalBluetoothManager getLocalBluetoothManager(Context context) {
        TestDataFactory testDataFactory = sTestDataFactory;
        if (testDataFactory != null) {
            return testDataFactory.getManager(context);
        }
        return Utils.getLocalBtManager(context);
    }

    CachedBluetoothDevice getCachedDevice(String str) {
        TestDataFactory testDataFactory = sTestDataFactory;
        if (testDataFactory != null) {
            return testDataFactory.getDevice(str);
        }
        return this.mManager.getCachedDeviceManager().findDevice(this.mManager.getBluetoothAdapter().getRemoteDevice(str));
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        this.mDeviceAddress = getArguments().getString("device_address");
        this.mManager = getLocalBluetoothManager(context);
        this.mCachedDevice = getCachedDevice(this.mDeviceAddress);
        super.onAttach(context);
        if (this.mCachedDevice == null) {
            Log.w("BTDeviceDetailsFrg", "onAttach() CachedDevice is null!");
            finish();
            return;
        }
        ((AdvancedBluetoothDetailsHeaderController) use(AdvancedBluetoothDetailsHeaderController.class)).init(this.mCachedDevice);
        BluetoothFeatureProvider bluetoothFeatureProvider = FeatureFactory.getFactory(context).getBluetoothFeatureProvider(context);
        ((BlockingSlicePrefController) use(BlockingSlicePrefController.class)).setSliceUri(DeviceConfig.getBoolean("settings_ui", "bt_slice_settings_enabled", true) ? bluetoothFeatureProvider.getBluetoothDeviceSettingsUri(this.mCachedDevice.getDevice()) : null);
        ((BADeviceVolumeController) use(BADeviceVolumeController.class)).init(this, this.mManager, this.mCachedDevice);
    }

    @Override // com.android.settings.dashboard.RestrictedDashboardFragment, com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        finishFragmentIfNecessary();
    }

    void finishFragmentIfNecessary() {
        if (this.mCachedDevice.getBondState() == 10) {
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.bluetooth_device_details_fragment;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        MenuItem add = menu.add(0, EDIT_DEVICE_NAME_ITEM_ID, 0, R.string.bluetooth_rename_button);
        add.setIcon(17302768);
        add.setShowAsAction(2);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == EDIT_DEVICE_NAME_ITEM_ID) {
            RemoteDeviceNameDialogFragment.newInstance(this.mCachedDevice).show(getFragmentManager(), "RemoteDeviceName");
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public void displayResourceTilesToScreen(PreferenceScreen preferenceScreen) {
        if (!mBAEnabled) {
            preferenceScreen.removePreference(preferenceScreen.findPreference("sync_helper_buttons"));
            preferenceScreen.removePreference(preferenceScreen.findPreference("added_sources"));
        }
        super.displayResourceTilesToScreen(preferenceScreen);
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        if (this.mCachedDevice == null) {
            return arrayList;
        }
        Lifecycle settingsLifecycle = getSettingsLifecycle();
        arrayList.add(new BluetoothDetailsHeaderController(context, this, this.mCachedDevice, settingsLifecycle, this.mManager));
        arrayList.add(new BluetoothDetailsButtonsController(context, this, this.mCachedDevice, settingsLifecycle));
        arrayList.add(new BluetoothDetailsCompanionAppsController(context, this, this.mCachedDevice, settingsLifecycle));
        arrayList.add(new BluetoothDetailsProfilesController(context, this, this.mManager, this.mCachedDevice, settingsLifecycle));
        arrayList.add(new BluetoothDetailsMacAddressController(context, this, this.mCachedDevice, settingsLifecycle));
        if (!mBAPropertyChecked) {
            mBAEnabled = (SystemProperties.getInt("persist.vendor.service.bt.adv_audio_mask", 0) & 2) == 2 && SystemProperties.getBoolean("persist.bluetooth.broadcast_ui", true);
            mBAPropertyChecked = true;
        }
        if (!mBAEnabled) {
            return arrayList;
        }
        Log.d("BTDeviceDetailsFrg", "createPreferenceControllers for BA");
        try {
            try {
                if (this.mCachedDevice.isBASeeker()) {
                    Constructor declaredConstructor = BluetoothDetailsAddSourceButtonController.class.getDeclaredConstructor(Context.class, PreferenceFragmentCompat.class, CachedBluetoothDevice.class, Lifecycle.class);
                    Constructor declaredConstructor2 = BADevicePreferenceController.class.getDeclaredConstructor(Context.class, Lifecycle.class, String.class);
                    Object newInstance = declaredConstructor.newInstance(context, this, this.mCachedDevice, settingsLifecycle);
                    Object newInstance2 = declaredConstructor2.newInstance(context, settingsLifecycle, "added_sources");
                    newInstance2.getClass().getMethod("init", DashboardFragment.class, CachedBluetoothDevice.class).invoke(newInstance2, this, this.mCachedDevice);
                    arrayList.add((AbstractPreferenceController) newInstance);
                    arrayList.add((AbstractPreferenceController) newInstance2);
                }
                return arrayList;
            } catch (Fragment.InstantiationException | ClassNotFoundException | ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                mBAEnabled = false;
                return arrayList;
            }
        } catch (Throwable unused) {
            return arrayList;
        }
    }
}
