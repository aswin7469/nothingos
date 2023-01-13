package com.android.settingslib.bluetooth;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.android.settingslib.C1757R;

public class BroadcastDialog extends AlertDialog {
    private static final String TAG = "BroadcastDialog";
    private Context mContext;
    private String mCurrentApp;
    private String mSwitchApp;

    public BroadcastDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public void onCreate(Bundle bundle) {
        View inflate = View.inflate(this.mContext, C1757R.layout.broadcast_dialog, (ViewGroup) null);
        Window window = getWindow();
        window.setContentView(inflate);
        window.setWindowAnimations(C1757R.style.Theme_AlertDialog_SettingsLib);
        ((TextView) inflate.findViewById(C1757R.C1760id.dialog_title)).setText(this.mContext.getString(C1757R.string.bt_le_audio_broadcast_dialog_title, new Object[]{this.mCurrentApp}));
        ((TextView) inflate.findViewById(C1757R.C1760id.dialog_subtitle)).setText(this.mContext.getString(C1757R.string.bt_le_audio_broadcast_dialog_sub_title, new Object[]{this.mSwitchApp}));
        Button button = (Button) inflate.findViewById(C1757R.C1760id.negative_btn);
        ((Button) inflate.findViewById(C1757R.C1760id.positive_btn)).setText(this.mContext.getString(C1757R.string.bt_le_audio_broadcast_dialog_switch_app, new Object[]{this.mSwitchApp}), (TextView.BufferType) null);
        ((Button) inflate.findViewById(C1757R.C1760id.neutral_btn)).setOnClickListener(new BroadcastDialog$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-android-settingslib-bluetooth-BroadcastDialog */
    public /* synthetic */ void mo28114x30e21355(View view) {
        Log.d(TAG, "BroadcastDialog dismiss.");
        dismiss();
    }
}
