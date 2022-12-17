package com.android.settings.biometrics.fingerprint;

import android.content.DialogInterface;
import com.android.settings.biometrics.fingerprint.FingerprintSettings;

/* renamed from: com.android.settings.biometrics.fingerprint.FingerprintSettings$FingerprintSettingsFragment$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0760xd2803eaa implements DialogInterface.OnDismissListener {
    public final /* synthetic */ FingerprintSettings.FingerprintSettingsFragment f$0;

    public /* synthetic */ C0760xd2803eaa(FingerprintSettings.FingerprintSettingsFragment fingerprintSettingsFragment) {
        this.f$0 = fingerprintSettingsFragment;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$showRenameDialog$0(dialogInterface);
    }
}
