package com.android.systemui.usb;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.settingslib.SliceBroadcastRelay;
import com.android.systemui.C1894R;

public class UsbAccessoryUriActivity extends AlertActivity implements DialogInterface.OnClickListener {
    private static final String TAG = "UsbAccessoryUriActivity";
    private UsbAccessory mAccessory;
    private Uri mUri;

    public void onCreate(Bundle bundle) {
        Uri uri;
        getWindow().addSystemFlags(524288);
        UsbAccessoryUriActivity.super.onCreate(bundle);
        Intent intent = getIntent();
        this.mAccessory = (UsbAccessory) intent.getParcelableExtra("accessory");
        String stringExtra = intent.getStringExtra(SliceBroadcastRelay.EXTRA_URI);
        if (stringExtra == null) {
            uri = null;
        } else {
            uri = Uri.parse(stringExtra);
        }
        this.mUri = uri;
        if (uri == null) {
            Log.e(TAG, "could not parse Uri " + stringExtra);
            finish();
            return;
        }
        String scheme = uri.getScheme();
        if ("http".equals(scheme) || "https".equals(scheme)) {
            AlertController.AlertParams alertParams = this.mAlertParams;
            alertParams.mTitle = this.mAccessory.getDescription();
            if (alertParams.mTitle == null || alertParams.mTitle.length() == 0) {
                alertParams.mTitle = getString(C1894R.string.title_usb_accessory);
            }
            alertParams.mMessage = getString(C1894R.string.usb_accessory_uri_prompt, new Object[]{this.mUri});
            alertParams.mPositiveButtonText = getString(C1894R.string.label_view);
            alertParams.mNegativeButtonText = getString(17039360);
            alertParams.mPositiveButtonListener = this;
            alertParams.mNegativeButtonListener = this;
            setupAlert();
            return;
        }
        Log.e(TAG, "Uri not http or https: " + this.mUri);
        finish();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            Intent intent = new Intent("android.intent.action.VIEW", this.mUri);
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.addFlags(268435456);
            try {
                startActivityAsUser(intent, UserHandle.CURRENT);
            } catch (ActivityNotFoundException unused) {
                Log.e(TAG, "startActivity failed for " + this.mUri);
            }
        }
        finish();
    }
}
