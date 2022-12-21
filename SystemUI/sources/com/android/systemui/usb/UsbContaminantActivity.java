package com.android.systemui.usb;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbPort;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.android.settingslib.media.MediaOutputConstants;
import com.android.systemui.C1893R;

public class UsbContaminantActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "UsbContaminantActivity";
    private TextView mEnableUsb;
    private TextView mGotIt;
    private TextView mLearnMore;
    private TextView mMessage;
    private TextView mTitle;
    private UsbPort mUsbPort;

    public void onCreate(Bundle bundle) {
        Window window = getWindow();
        window.addSystemFlags(524288);
        window.setType(2008);
        requestWindowFeature(1);
        super.onCreate(bundle);
        setContentView(C1893R.layout.contaminant_dialog);
        this.mUsbPort = getIntent().getParcelableExtra("port").getUsbPort((UsbManager) getSystemService(UsbManager.class));
        this.mLearnMore = (TextView) findViewById(C1893R.C1897id.learnMore);
        this.mEnableUsb = (TextView) findViewById(C1893R.C1897id.enableUsb);
        this.mGotIt = (TextView) findViewById(C1893R.C1897id.gotIt);
        this.mTitle = (TextView) findViewById(C1893R.C1897id.title);
        this.mMessage = (TextView) findViewById(C1893R.C1897id.message);
        this.mTitle.setText(getString(C1893R.string.usb_contaminant_title));
        this.mMessage.setText(getString(C1893R.string.usb_contaminant_message));
        this.mEnableUsb.setText(getString(C1893R.string.usb_disable_contaminant_detection));
        this.mGotIt.setText(getString(C1893R.string.got_it));
        this.mLearnMore.setText(getString(C1893R.string.learn_more));
        if (getResources().getBoolean(17891744)) {
            this.mLearnMore.setVisibility(0);
        }
        this.mEnableUsb.setOnClickListener(this);
        this.mGotIt.setOnClickListener(this);
        this.mLearnMore.setOnClickListener(this);
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {
        super.onWindowAttributesChanged(layoutParams);
    }

    public void onClick(View view) {
        if (view == this.mEnableUsb) {
            try {
                this.mUsbPort.enableContaminantDetection(false);
                Toast.makeText(this, C1893R.string.usb_port_enabled, 0).show();
            } catch (Exception e) {
                Log.e(TAG, "Unable to notify Usb service", e);
            }
        } else if (view == this.mLearnMore) {
            Intent intent = new Intent();
            intent.setClassName(MediaOutputConstants.SETTINGS_PACKAGE_NAME, "com.android.settings.HelpTrampoline");
            intent.putExtra("android.intent.extra.TEXT", "help_url_usb_contaminant_detected");
            startActivity(intent);
        }
        finish();
    }
}
