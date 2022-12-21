package com.android.settingslib.enterprise;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import com.android.settingslib.RestrictedLockUtils;

public class BiometricActionDisabledByAdminController extends BaseActionDisabledByAdminController {
    private static final String TAG = "BiometricActionDisabledByAdminController";

    public void setupLearnMoreButton(Context context) {
    }

    BiometricActionDisabledByAdminController(DeviceAdminStringProvider deviceAdminStringProvider) {
        super(deviceAdminStringProvider);
    }

    public String getAdminSupportTitle(String str) {
        return this.mStringProvider.getDisabledBiometricsParentConsentTitle();
    }

    public CharSequence getAdminSupportContentString(Context context, CharSequence charSequence) {
        return this.mStringProvider.getDisabledBiometricsParentConsentContent();
    }

    public DialogInterface.OnClickListener getPositiveButtonListener(Context context, RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        return new C1829xfa36ded9(enforcedAdmin, context);
    }

    static /* synthetic */ void lambda$getPositiveButtonListener$0(RestrictedLockUtils.EnforcedAdmin enforcedAdmin, Context context, DialogInterface dialogInterface, int i) {
        Log.d(TAG, "Positive button clicked, component: " + enforcedAdmin.component);
        context.startActivity(new Intent("android.settings.MANAGE_SUPERVISOR_RESTRICTED_SETTING").putExtra("android.provider.extra.SUPERVISOR_RESTRICTED_SETTING_KEY", 1).addFlags(268435456).setPackage(enforcedAdmin.component.getPackageName()));
    }
}
