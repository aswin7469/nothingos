package com.android.systemui.usb.p015tv;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.android.systemui.p014tv.TvBottomSheetActivity;
import com.android.systemui.usb.UsbDialogHelper;

/* renamed from: com.android.systemui.usb.tv.TvUsbDialogActivity */
abstract class TvUsbDialogActivity extends TvBottomSheetActivity implements View.OnClickListener {
    private static final String TAG = "TvUsbDialogActivity";
    UsbDialogHelper mDialogHelper;

    /* access modifiers changed from: package-private */
    public abstract void onConfirm();

    TvUsbDialogActivity() {
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addPrivateFlags(524288);
        try {
            this.mDialogHelper = new UsbDialogHelper(getApplicationContext(), getIntent());
        } catch (IllegalStateException e) {
            Log.e(TAG, "unable to initialize", e);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mDialogHelper.registerUsbDisconnectedReceiver(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        UsbDialogHelper usbDialogHelper = this.mDialogHelper;
        if (usbDialogHelper != null) {
            usbDialogHelper.unregisterUsbDisconnectedReceiver(this);
        }
        super.onPause();
    }

    public void onClick(View view) {
        if (view.getId() == C1894R.C1898id.bottom_sheet_positive_button) {
            onConfirm();
        } else {
            finish();
        }
    }

    /* access modifiers changed from: package-private */
    public void initUI(CharSequence charSequence, CharSequence charSequence2) {
        Button button = (Button) findViewById(C1894R.C1898id.bottom_sheet_positive_button);
        Button button2 = (Button) findViewById(C1894R.C1898id.bottom_sheet_negative_button);
        ((TextView) findViewById(C1894R.C1898id.bottom_sheet_title)).setText(charSequence);
        ((TextView) findViewById(C1894R.C1898id.bottom_sheet_body)).setText(charSequence2);
        ((ImageView) findViewById(C1894R.C1898id.bottom_sheet_icon)).setImageResource(17302883);
        ((ImageView) findViewById(C1894R.C1898id.bottom_sheet_second_icon)).setVisibility(8);
        button.setText(17039370);
        button.setOnClickListener(this);
        button2.setText(17039360);
        button2.setOnClickListener(this);
        button2.requestFocus();
    }
}
