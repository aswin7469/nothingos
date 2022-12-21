package com.android.systemui.usb;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.debug.IAdbManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import javax.inject.Inject;

public class UsbDebuggingSecondaryUserActivity extends AlertActivity implements DialogInterface.OnClickListener {
    private static final String TAG = "UsbDebuggingSecondaryUserActivity";
    private final BroadcastDispatcher mBroadcastDispatcher;
    private UsbDisconnectedReceiver mDisconnectedReceiver;

    @Inject
    public UsbDebuggingSecondaryUserActivity(BroadcastDispatcher broadcastDispatcher) {
        this.mBroadcastDispatcher = broadcastDispatcher;
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [android.content.DialogInterface$OnClickListener, com.android.systemui.usb.UsbDebuggingSecondaryUserActivity, com.android.internal.app.AlertActivity, android.app.Activity] */
    public void onCreate(Bundle bundle) {
        getWindow().addSystemFlags(524288);
        UsbDebuggingSecondaryUserActivity.super.onCreate(bundle);
        if (SystemProperties.getInt("service.adb.tcp.port", 0) == 0) {
            this.mDisconnectedReceiver = new UsbDisconnectedReceiver(this);
        }
        AlertController.AlertParams alertParams = this.mAlertParams;
        alertParams.mTitle = getString(C1893R.string.usb_debugging_secondary_user_title);
        alertParams.mMessage = getString(C1893R.string.usb_debugging_secondary_user_message);
        alertParams.mPositiveButtonText = getString(17039370);
        alertParams.mPositiveButtonListener = this;
        setupAlert();
    }

    private class UsbDisconnectedReceiver extends BroadcastReceiver {
        private final Activity mActivity;

        UsbDisconnectedReceiver(Activity activity) {
            this.mActivity = activity;
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.hardware.usb.action.USB_STATE".equals(intent.getAction()) && !intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                this.mActivity.finish();
            }
        }
    }

    public void onStart() {
        UsbDebuggingSecondaryUserActivity.super.onStart();
        if (this.mDisconnectedReceiver != null) {
            this.mBroadcastDispatcher.registerReceiver(this.mDisconnectedReceiver, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        UsbDisconnectedReceiver usbDisconnectedReceiver = this.mDisconnectedReceiver;
        if (usbDisconnectedReceiver != null) {
            this.mBroadcastDispatcher.unregisterReceiver(usbDisconnectedReceiver);
        }
        try {
            IAdbManager.Stub.asInterface(ServiceManager.getService("adb")).denyDebugging();
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to notify Usb service", e);
        }
        UsbDebuggingSecondaryUserActivity.super.onStop();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        finish();
    }
}
