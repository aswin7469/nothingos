package com.android.settings.connecteddevice;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.sysprop.BluetoothProperties;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.SettingsActivity;
import com.android.settings.bluetooth.BluetoothBroadcastEnableController;
import com.android.settings.bluetooth.BluetoothBroadcastPinController;
import com.android.settings.bluetooth.BluetoothDeviceRenamePreferenceController;
import com.android.settings.bluetooth.BluetoothSwitchPreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.MainSwitchBarController;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.FooterPreference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BluetoothDashboardFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.bluetooth_screen);
    private static boolean mBroadcastEnabled = false;
    private static boolean mBroadcastPropertyChecked = false;
    private BluetoothSwitchPreferenceController mController;
    private FooterPreference mFooterPreference;
    private SettingsMainSwitchBar mSwitchBar;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "BluetoothDashboardFrag";
    }

    public int getMetricsCategory() {
        return 1390;
    }

    public BluetoothDashboardFragment() {
        Optional isProfileBapBroadcastSourceEnabled = BluetoothProperties.isProfileBapBroadcastSourceEnabled();
        Boolean bool = Boolean.FALSE;
        if (((Boolean) isProfileBapBroadcastSourceEnabled.orElse(bool)).booleanValue() || ((Boolean) BluetoothProperties.isProfileBapBroadcastAssistEnabled().orElse(bool)).booleanValue()) {
            SystemProperties.set("persist.bluetooth.broadcast_ui", "false");
            return;
        }
        Log.d("BluetoothDashboardFrag", "Use legacy broadcast if available");
        SystemProperties.set("persist.bluetooth.broadcast_ui", "true");
    }

    public int getHelpResource() {
        return R$string.help_uri_bluetooth_screen;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.bluetooth_screen;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFooterPreference = (FooterPreference) findPreference("bluetooth_screen_footer");
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((BluetoothDeviceRenamePreferenceController) use(BluetoothDeviceRenamePreferenceController.class)).setFragment(this);
    }

    /* access modifiers changed from: protected */
    public void displayResourceTilesToScreen(PreferenceScreen preferenceScreen) {
        if (!mBroadcastEnabled) {
            preferenceScreen.removePreference(preferenceScreen.findPreference(BluetoothBroadcastEnableController.KEY_BROADCAST_ENABLE));
            preferenceScreen.removePreference(preferenceScreen.findPreference(BluetoothBroadcastPinController.KEY_BROADCAST_AUDIO_PIN));
        }
        super.displayResourceTilesToScreen(preferenceScreen);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        Class<Lifecycle> cls = Lifecycle.class;
        ArrayList arrayList = new ArrayList();
        if (!mBroadcastPropertyChecked) {
            mBroadcastEnabled = (SystemProperties.getInt("persist.vendor.service.bt.adv_audio_mask", 0) & 4) == 4 && SystemProperties.getBoolean("persist.bluetooth.broadcast_ui", true);
            mBroadcastPropertyChecked = true;
        }
        if (!mBroadcastEnabled) {
            return arrayList;
        }
        Log.d("BluetoothDashboardFrag", "createPreferenceControllers for Broadcast");
        Class<BluetoothBroadcastEnableController> cls2 = BluetoothBroadcastEnableController.class;
        try {
            Constructor<BluetoothBroadcastPinController> declaredConstructor = BluetoothBroadcastPinController.class.getDeclaredConstructor(new Class[]{Context.class, cls});
            Constructor<BluetoothBroadcastEnableController> declaredConstructor2 = cls2.getDeclaredConstructor(new Class[]{Context.class, String.class, cls});
            BluetoothBroadcastPinController newInstance = declaredConstructor.newInstance(new Object[]{context, getSettingsLifecycle()});
            BluetoothBroadcastEnableController newInstance2 = declaredConstructor2.newInstance(new Object[]{context, new String(BluetoothBroadcastEnableController.KEY_BROADCAST_ENABLE), getSettingsLifecycle()});
            newInstance.getClass().getMethod("setFragment", new Class[]{Fragment.class}).invoke(newInstance, new Object[]{this});
            arrayList.add(newInstance);
            arrayList.add(newInstance2);
            return arrayList;
        } catch (Fragment.InstantiationException | ClassNotFoundException | ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
            mBroadcastEnabled = false;
            e.printStackTrace();
        } catch (Throwable unused) {
        }
        return arrayList;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        SettingsMainSwitchBar switchBar = settingsActivity.getSwitchBar();
        this.mSwitchBar = switchBar;
        switchBar.setTitle(getContext().getString(R$string.bluetooth_main_switch_title));
        this.mController = new BluetoothSwitchPreferenceController(settingsActivity, new MainSwitchBarController(this.mSwitchBar), this.mFooterPreference);
        Lifecycle settingsLifecycle = getSettingsLifecycle();
        if (settingsLifecycle != null) {
            settingsLifecycle.addObserver(this.mController);
        }
    }
}
