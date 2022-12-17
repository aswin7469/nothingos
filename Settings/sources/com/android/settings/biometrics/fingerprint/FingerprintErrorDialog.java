package com.android.settings.biometrics.fingerprint;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R$string;
import com.android.settings.biometrics.BiometricEnrollBase;
import com.android.settings.biometrics.BiometricErrorDialog;

public class FingerprintErrorDialog extends BiometricErrorDialog {
    public int getMetricsCategory() {
        return 569;
    }

    public static void showErrorDialog(BiometricEnrollBase biometricEnrollBase, int i) {
        if (!biometricEnrollBase.isFinishing()) {
            FragmentManager supportFragmentManager = biometricEnrollBase.getSupportFragmentManager();
            if (!supportFragmentManager.isDestroyed() && !supportFragmentManager.isStateSaved()) {
                newInstance(biometricEnrollBase.getText(getErrorMessage(i)), i).show(supportFragmentManager, FingerprintErrorDialog.class.getName());
            }
        }
    }

    private static int getErrorMessage(int i) {
        if (i == 3) {
            return R$string.f154xe7f94de6;
        }
        if (i != 18) {
            return R$string.f153x537c56b0;
        }
        return R$string.security_settings_fingerprint_bad_calibration;
    }

    private static FingerprintErrorDialog newInstance(CharSequence charSequence, int i) {
        FingerprintErrorDialog fingerprintErrorDialog = new FingerprintErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putCharSequence("error_msg", charSequence);
        bundle.putInt("error_id", i);
        fingerprintErrorDialog.setArguments(bundle);
        return fingerprintErrorDialog;
    }

    public int getTitleResId() {
        Bundle arguments = getArguments();
        if (arguments == null || 3 != arguments.getInt("error_id")) {
            return R$string.security_settings_fingerprint_enroll_error_dialog_title;
        }
        return R$string.nt_security_settings_fingerprint_enroll_error_dialog_title;
    }

    public int getOkButtonTextResId() {
        return R$string.security_settings_fingerprint_enroll_dialog_ok;
    }
}
