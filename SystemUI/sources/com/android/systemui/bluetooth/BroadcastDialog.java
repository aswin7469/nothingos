package com.android.systemui.bluetooth;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1894R;
import com.android.systemui.media.MediaDataUtils;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.statusbar.phone.SystemUIDialog;

public class BroadcastDialog extends SystemUIDialog {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "BroadcastDialog";
    private Context mContext;
    protected View mDialogView;
    private MediaOutputDialogFactory mMediaOutputDialogFactory;
    private String mOutputPackageName;
    private String mSwitchBroadcastApp;
    private UiEventLogger mUiEventLogger;

    public BroadcastDialog(Context context, MediaOutputDialogFactory mediaOutputDialogFactory, String str, String str2, UiEventLogger uiEventLogger) {
        super(context);
        if (DEBUG) {
            Log.d(TAG, "Init BroadcastDialog");
        }
        this.mContext = getContext();
        this.mMediaOutputDialogFactory = mediaOutputDialogFactory;
        this.mSwitchBroadcastApp = str;
        this.mOutputPackageName = str2;
        this.mUiEventLogger = uiEventLogger;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (DEBUG) {
            Log.d(TAG, "onCreate");
        }
        this.mUiEventLogger.log(BroadcastDialogEvent.BROADCAST_DIALOG_SHOW);
        this.mDialogView = LayoutInflater.from(this.mContext).inflate(C1894R.layout.broadcast_dialog, (ViewGroup) null);
        getWindow().setContentView(this.mDialogView);
        Context context = this.mContext;
        ((TextView) this.mDialogView.requireViewById(C1894R.C1898id.dialog_title)).setText(context.getString(C1894R.string.bt_le_audio_broadcast_dialog_title, new Object[]{MediaDataUtils.getAppLabel(context, this.mOutputPackageName, context.getString(C1894R.string.bt_le_audio_broadcast_dialog_unknown_name))}));
        ((TextView) this.mDialogView.requireViewById(C1894R.C1898id.dialog_subtitle)).setText(this.mContext.getString(C1894R.string.bt_le_audio_broadcast_dialog_sub_title, new Object[]{this.mSwitchBroadcastApp}));
        ((Button) this.mDialogView.requireViewById(C1894R.C1898id.switch_broadcast)).setText(this.mContext.getString(C1894R.string.bt_le_audio_broadcast_dialog_switch_app, new Object[]{this.mSwitchBroadcastApp}), (TextView.BufferType) null);
        ((Button) this.mDialogView.requireViewById(C1894R.C1898id.change_output)).setOnClickListener(new BroadcastDialog$$ExternalSyntheticLambda0(this));
        ((Button) this.mDialogView.requireViewById(C1894R.C1898id.cancel)).setOnClickListener(new BroadcastDialog$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-android-systemui-bluetooth-BroadcastDialog  reason: not valid java name */
    public /* synthetic */ void m2595lambda$onCreate$0$comandroidsystemuibluetoothBroadcastDialog(View view) {
        this.mMediaOutputDialogFactory.create(this.mOutputPackageName, true, (View) null);
        dismiss();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$com-android-systemui-bluetooth-BroadcastDialog  reason: not valid java name */
    public /* synthetic */ void m2596lambda$onCreate$1$comandroidsystemuibluetoothBroadcastDialog(View view) {
        if (DEBUG) {
            Log.d(TAG, "BroadcastDialog dismiss.");
        }
        dismiss();
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!z && isShowing()) {
            dismiss();
        }
    }

    public enum BroadcastDialogEvent implements UiEventLogger.UiEventEnum {
        BROADCAST_DIALOG_SHOW(1062);
        
        private final int mId;

        private BroadcastDialogEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
