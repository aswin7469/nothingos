package com.android.settings.connecteddevice;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
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
/* loaded from: classes.dex */
public class BluetoothDashboardFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.bluetooth_screen);
    private static boolean mBroadcastEnabled = false;
    private static boolean mBroadcastPropertyChecked = false;
    private BluetoothSwitchPreferenceController mController;
    private FooterPreference mFooterPreference;
    private SettingsMainSwitchBar mSwitchBar;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "BluetoothDashboardFrag";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1390;
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return R.string.help_uri_bluetooth_screen;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.bluetooth_screen;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFooterPreference = (FooterPreference) findPreference("bluetooth_screen_footer");
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        ((BluetoothDeviceRenamePreferenceController) use(BluetoothDeviceRenamePreferenceController.class)).setFragment(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public void displayResourceTilesToScreen(PreferenceScreen preferenceScreen) {
        if (!mBroadcastEnabled) {
            preferenceScreen.removePreference(preferenceScreen.findPreference(BluetoothBroadcastEnableController.KEY_BROADCAST_ENABLE));
            preferenceScreen.removePreference(preferenceScreen.findPreference(BluetoothBroadcastPinController.KEY_BROADCAST_AUDIO_PIN));
        }
        super.displayResourceTilesToScreen(preferenceScreen);
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        if (!mBroadcastPropertyChecked) {
            mBroadcastEnabled = (SystemProperties.getInt("persist.vendor.service.bt.adv_audio_mask", 0) & 4) == 4 && SystemProperties.getBoolean("persist.bluetooth.broadcast_ui", true);
            mBroadcastPropertyChecked = true;
        }
        if (!mBroadcastEnabled) {
            return arrayList;
        }
        Log.d("BluetoothDashboardFrag", "createPreferenceControllers for Broadcast");
        try {
            try {
                Constructor declaredConstructor = BluetoothBroadcastPinController.class.getDeclaredConstructor(Context.class);
                Constructor declaredConstructor2 = BluetoothBroadcastEnableController.class.getDeclaredConstructor(Context.class, String.class);
                Object newInstance = declaredConstructor.newInstance(context);
                Object newInstance2 = declaredConstructor2.newInstance(context, new String(BluetoothBroadcastEnableController.KEY_BROADCAST_ENABLE));
                newInstance.getClass().getMethod("setFragment", Fragment.class).invoke(newInstance, this);
                arrayList.add((AbstractPreferenceController) newInstance);
                arrayList.add((AbstractPreferenceController) newInstance2);
                return arrayList;
            } catch (Fragment.InstantiationException | ClassNotFoundException | ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
                mBroadcastEnabled = false;
                e.printStackTrace();
                return arrayList;
            }
        } catch (Throwable unused) {
            return arrayList;
        }
    }

    @Override // com.android.settings.SettingsPreferenceFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        SettingsMainSwitchBar switchBar = settingsActivity.getSwitchBar();
        this.mSwitchBar = switchBar;
        switchBar.setTitle(getContext().getString(R.string.bluetooth_main_switch_title));
        this.mController = new BluetoothSwitchPreferenceController(settingsActivity, new MainSwitchBarController(this.mSwitchBar), this.mFooterPreference);
        Lifecycle settingsLifecycle = getSettingsLifecycle();
        if (settingsLifecycle != null) {
            settingsLifecycle.addObserver(this.mController);
        }
    }
}
