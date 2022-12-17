package com.android.settings.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$id;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothLeBroadcastAssistant;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.LayoutPreference;

public class BluetoothFindBroadcastsHeaderController extends BluetoothDetailsController {
    BluetoothFindBroadcastsFragment mBluetoothFindBroadcastsFragment;
    PreferenceCategory mBroadcastSourceList;
    LinearLayout mBtnBroadcastLayout;
    Button mBtnFindBroadcast;
    Button mBtnLeaveBroadcast;
    Button mBtnScanQrCode;
    LayoutPreference mLayoutPreference;
    TextView mSummary;
    TextView mTitle;

    public String getPreferenceKey() {
        return "bluetooth_find_broadcast_header";
    }

    public BluetoothFindBroadcastsHeaderController(Context context, BluetoothFindBroadcastsFragment bluetoothFindBroadcastsFragment, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle, LocalBluetoothManager localBluetoothManager) {
        super(context, bluetoothFindBroadcastsFragment, cachedBluetoothDevice, lifecycle);
        this.mBluetoothFindBroadcastsFragment = bluetoothFindBroadcastsFragment;
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        this.mLayoutPreference = (LayoutPreference) preferenceScreen.findPreference("bluetooth_find_broadcast_header");
        this.mBroadcastSourceList = (PreferenceCategory) preferenceScreen.findPreference("broadcast_source_list");
        refresh();
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        LayoutPreference layoutPreference = this.mLayoutPreference;
        if (layoutPreference != null && this.mCachedDevice != null) {
            TextView textView = (TextView) layoutPreference.findViewById(R$id.entity_header_title);
            this.mTitle = textView;
            textView.setText(this.mCachedDevice.getName());
            TextView textView2 = (TextView) this.mLayoutPreference.findViewById(R$id.entity_header_summary);
            this.mSummary = textView2;
            textView2.setText("");
            Button button = (Button) this.mLayoutPreference.findViewById(R$id.button_find_broadcast);
            this.mBtnFindBroadcast = button;
            button.setOnClickListener(new C0792x84dd2f11(this));
            this.mBtnBroadcastLayout = (LinearLayout) this.mLayoutPreference.findViewById(R$id.button_broadcast_layout);
            Button button2 = (Button) this.mLayoutPreference.findViewById(R$id.button_leave_broadcast);
            this.mBtnLeaveBroadcast = button2;
            button2.setOnClickListener(new C0793x84dd2f12(this));
            Button button3 = (Button) this.mLayoutPreference.findViewById(R$id.button_scan_qr_code);
            this.mBtnScanQrCode = button3;
            button3.setOnClickListener(new C0794x84dd2f13(this));
            updateHeaderLayout();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$0(View view) {
        scanBroadcastSource();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$1(View view) {
        leaveBroadcastSession();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$2(View view) {
        launchQrCodeScanner();
    }

    private boolean isBroadcastSourceExist() {
        return this.mBroadcastSourceList.getPreferenceCount() > 0;
    }

    private void updateHeaderLayout() {
        LocalBluetoothLeBroadcastAssistant leBroadcastAssistant;
        if (isBroadcastSourceExist()) {
            this.mBtnFindBroadcast.setVisibility(8);
            this.mBtnBroadcastLayout.setVisibility(0);
        } else {
            this.mBtnFindBroadcast.setVisibility(0);
            this.mBtnBroadcastLayout.setVisibility(8);
        }
        this.mBtnLeaveBroadcast.setEnabled(false);
        BluetoothFindBroadcastsFragment bluetoothFindBroadcastsFragment = this.mBluetoothFindBroadcastsFragment;
        if (bluetoothFindBroadcastsFragment != null && this.mCachedDevice != null && (leBroadcastAssistant = bluetoothFindBroadcastsFragment.getLeBroadcastAssistant()) != null && leBroadcastAssistant.getConnectionStatus(this.mCachedDevice.getDevice()) == 2) {
            this.mBtnLeaveBroadcast.setEnabled(true);
        }
    }

    private void scanBroadcastSource() {
        BluetoothFindBroadcastsFragment bluetoothFindBroadcastsFragment = this.mBluetoothFindBroadcastsFragment;
        if (bluetoothFindBroadcastsFragment != null) {
            bluetoothFindBroadcastsFragment.scanBroadcastSource();
        }
    }

    private void leaveBroadcastSession() {
        BluetoothFindBroadcastsFragment bluetoothFindBroadcastsFragment = this.mBluetoothFindBroadcastsFragment;
        if (bluetoothFindBroadcastsFragment != null) {
            bluetoothFindBroadcastsFragment.leaveBroadcastSession();
        }
    }

    private void launchQrCodeScanner() {
        Intent intent = new Intent(this.mContext, QrCodeScanModeActivity.class);
        intent.setAction("android.settings.BLUETOOTH_LE_AUDIO_QR_CODE_SCANNER").putExtra("bluetooth_sink_is_group", true).putExtra("bluetooth_device_sink", this.mCachedDevice.getDevice());
        this.mContext.startActivity(intent);
    }

    public void onDeviceAttributesChanged() {
        if (this.mCachedDevice != null) {
            refresh();
        }
    }

    public void refreshUi() {
        updateHeaderLayout();
    }
}
