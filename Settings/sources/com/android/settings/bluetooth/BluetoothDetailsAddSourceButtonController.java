package com.android.settings.bluetooth;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.bluetooth.BCProfile;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.ActionButtonsPreference;
/* loaded from: classes.dex */
public class BluetoothDetailsAddSourceButtonController extends BluetoothDetailsController {
    private ActionButtonsPreference mActionButtons;
    private LocalBluetoothManager mLocalBluetoothManager;
    protected LocalBluetoothProfileManager mProfileManager;
    private boolean mIsConnected = false;
    private BCProfile mBCProfile = null;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "sync_helper_buttons";
    }

    public BluetoothDetailsAddSourceButtonController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
        cachedBluetoothDevice.registerCallback(this);
    }

    private void onAddLESourcePressed() {
        Bundle bundle = new Bundle();
        bundle.putString("device_address", this.mCachedDevice.getDevice().getAddress());
        bundle.putShort("group_op", (short) 0);
        new SubSettingLauncher(((BluetoothDetailsController) this).mContext).setDestination(BluetoothSADetail.class.getName()).setArguments(bundle).setTitleRes(R.string.bluetooth_search_broadcasters).setSourceMetricsCategory(25).launch();
    }

    @Override // com.android.settings.bluetooth.BluetoothDetailsController, com.android.settingslib.bluetooth.CachedBluetoothDevice.Callback
    public void onDeviceAttributesChanged() {
        refresh();
    }

    @Override // com.android.settings.bluetooth.BluetoothDetailsController
    protected void init(PreferenceScreen preferenceScreen) {
        BroadcastScanAssistanceUtils.debug("BluetoothDetailsAddSourceButtonController", "init");
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(((BluetoothDetailsController) this).mContext);
        this.mLocalBluetoothManager = localBtManager;
        LocalBluetoothProfileManager profileManager = localBtManager.getProfileManager();
        this.mProfileManager = profileManager;
        this.mBCProfile = (BCProfile) profileManager.getBCProfile();
        this.mActionButtons = ((ActionButtonsPreference) preferenceScreen.findPreference(getPreferenceKey())).setButton1Text(R.string.add_source_button_text).setButton1Icon(R.drawable.ic_add_24dp).setButton1OnClickListener(new View.OnClickListener() { // from class: com.android.settings.bluetooth.BluetoothDetailsAddSourceButtonController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BluetoothDetailsAddSourceButtonController.this.lambda$init$0(view);
            }
        }).setButton1Enabled(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(View view) {
        onAddLESourcePressed();
    }

    @Override // com.android.settings.bluetooth.BluetoothDetailsController
    protected void refresh() {
        BroadcastScanAssistanceUtils.debug("BluetoothDetailsAddSourceButtonController", "refresh");
        BCProfile bCProfile = this.mBCProfile;
        if (bCProfile != null) {
            this.mIsConnected = bCProfile.getConnectionStatus(this.mCachedDevice.getDevice()) == 2;
        }
        if (this.mIsConnected) {
            this.mActionButtons.setButton1Enabled(true);
            return;
        }
        BroadcastScanAssistanceUtils.debug("BluetoothDetailsAddSourceButtonController", "Bass is not connected for thsi device>>");
        this.mActionButtons.setButton1Enabled(false);
    }
}
