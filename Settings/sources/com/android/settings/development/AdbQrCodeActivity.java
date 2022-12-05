package com.android.settings.development;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.android.settings.R;
import com.android.settings.wifi.dpp.WifiDppBaseActivity;
/* loaded from: classes.dex */
public class AdbQrCodeActivity extends WifiDppBaseActivity {
    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1831;
    }

    @Override // com.android.settings.wifi.dpp.WifiDppBaseActivity
    protected void handleIntent(Intent intent) {
    }

    @Override // com.android.settings.wifi.dpp.WifiDppBaseActivity, com.android.settings.core.InstrumentedActivity, com.android.settingslib.core.lifecycle.ObservableActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        AdbQrcodeScannerFragment adbQrcodeScannerFragment = (AdbQrcodeScannerFragment) this.mFragmentManager.findFragmentByTag("adb_qr_code_scanner_fragment");
        if (adbQrcodeScannerFragment == null) {
            AdbQrcodeScannerFragment adbQrcodeScannerFragment2 = new AdbQrcodeScannerFragment();
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            beginTransaction.replace(R.id.fragment_container, adbQrcodeScannerFragment2, "adb_qr_code_scanner_fragment");
            beginTransaction.commit();
        } else if (adbQrcodeScannerFragment.isVisible()) {
        } else {
            this.mFragmentManager.popBackStackImmediate();
        }
    }
}
