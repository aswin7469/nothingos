package com.android.settings.bluetooth;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R$string;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.R$id;
import com.android.settingslib.R$layout;
import com.android.settingslib.R$style;
import com.android.settingslib.bluetooth.LocalBluetoothLeBroadcast;
import com.android.settingslib.bluetooth.LocalBluetoothManager;

public class BluetoothBroadcastDialog extends InstrumentedDialogFragment {
    private static final CharSequence UNKNOWN_APP_LABEL = "unknown";
    private AlertDialog mAlertDialog;
    private Context mContext;
    private CharSequence mCurrentAppLabel = UNKNOWN_APP_LABEL;
    private String mDeviceAddress;
    private boolean mIsMediaStreaming;
    private LocalBluetoothManager mLocalBluetoothManager;

    public int getMetricsCategory() {
        return 0;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = getActivity();
        this.mCurrentAppLabel = getActivity().getIntent().getCharSequenceExtra("app_label");
        this.mDeviceAddress = getActivity().getIntent().getStringExtra("device_address");
        this.mIsMediaStreaming = getActivity().getIntent().getBooleanExtra("media_streaming", false);
        this.mLocalBluetoothManager = Utils.getLocalBtManager(this.mContext);
        setShowsDialog(true);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = View.inflate(this.mContext, R$layout.broadcast_dialog, (ViewGroup) null);
        Context context = this.mContext;
        int i = R$string.bluetooth_broadcast_dialog_title;
        ((TextView) inflate.findViewById(R$id.dialog_title)).setText(context.getString(i));
        ((TextView) inflate.findViewById(R$id.dialog_subtitle)).setText(this.mContext.getString(R$string.bluetooth_broadcast_dialog_broadcast_message));
        Button button = (Button) inflate.findViewById(R$id.positive_btn);
        if (!isBroadcastSupported() || !this.mIsMediaStreaming) {
            button.setVisibility(8);
        } else {
            button.setVisibility(0);
            if (TextUtils.isEmpty(this.mCurrentAppLabel)) {
                button.setText(this.mContext.getString(i));
            } else {
                button.setText(this.mContext.getString(R$string.bluetooth_broadcast_dialog_broadcast_app, new Object[]{String.valueOf(this.mCurrentAppLabel)}));
            }
            button.setOnClickListener(new BluetoothBroadcastDialog$$ExternalSyntheticLambda0(this));
        }
        Button button2 = (Button) inflate.findViewById(R$id.negative_btn);
        button2.setText(this.mContext.getString(R$string.bluetooth_find_broadcast));
        button2.setOnClickListener(new BluetoothBroadcastDialog$$ExternalSyntheticLambda1(this));
        ((Button) inflate.findViewById(R$id.neutral_btn)).setOnClickListener(new BluetoothBroadcastDialog$$ExternalSyntheticLambda2(this));
        AlertDialog create = new AlertDialog.Builder(this.mContext, R$style.Theme_AlertDialog_SettingsLib).setView(inflate).create();
        this.mAlertDialog = create;
        return create;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(View view) {
        launchMediaOutputBroadcastDialog();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(View view) {
        launchFindBroadcastsActivity();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$2(View view) {
        dismiss();
        getActivity().finish();
    }

    public void onStart() {
        super.onStart();
    }

    private void launchFindBroadcastsActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("device_address", this.mDeviceAddress);
        new SubSettingLauncher(this.mContext).setTitleRes(R$string.bluetooth_find_broadcast_title).setDestination(BluetoothFindBroadcastsFragment.class.getName()).setArguments(bundle).setSourceMetricsCategory(0).launch();
        dismissVolumePanel();
    }

    private void launchMediaOutputBroadcastDialog() {
        if (startBroadcast()) {
            this.mContext.sendBroadcast(new Intent().setPackage("com.android.systemui").setAction("com.android.systemui.action.LAUNCH_MEDIA_OUTPUT_BROADCAST_DIALOG").putExtra("package_name", getActivity().getPackageName()));
            dismissVolumePanel();
        }
    }

    private LocalBluetoothLeBroadcast getLEAudioBroadcastProfile() {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile;
        LocalBluetoothManager localBluetoothManager = this.mLocalBluetoothManager;
        if (localBluetoothManager != null && localBluetoothManager.getProfileManager() != null && (leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile()) != null) {
            return leAudioBroadcastProfile;
        }
        Log.d("BTBroadcastsDialog", "Can not get LE Audio Broadcast Profile");
        return null;
    }

    private boolean startBroadcast() {
        LocalBluetoothLeBroadcast lEAudioBroadcastProfile = getLEAudioBroadcastProfile();
        if (lEAudioBroadcastProfile != null) {
            lEAudioBroadcastProfile.startBroadcast(String.valueOf(this.mCurrentAppLabel), (String) null);
            return true;
        }
        Log.d("BTBroadcastsDialog", "Can not broadcast successfully");
        return false;
    }

    private void dismissVolumePanel() {
        this.mContext.sendBroadcast(new Intent().setPackage("com.android.settings").setAction("com.android.settings.panel.action.CLOSE_PANEL"));
    }

    /* access modifiers changed from: package-private */
    public boolean isBroadcastSupported() {
        return this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile() != null;
    }
}
