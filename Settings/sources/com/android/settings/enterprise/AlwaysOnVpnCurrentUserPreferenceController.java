package com.android.settings.enterprise;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.AbstractPreferenceController;

public class AlwaysOnVpnCurrentUserPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final DevicePolicyManager mDevicePolicyManager;
    private final EnterprisePrivacyFeatureProvider mFeatureProvider;

    public String getPreferenceKey() {
        return "always_on_vpn_primary_user";
    }

    public AlwaysOnVpnCurrentUserPreferenceController(Context context) {
        super(context);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context);
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
    }

    public void updateState(Preference preference) {
        if (this.mFeatureProvider.isInCompMode()) {
            preference.setTitle((CharSequence) this.mDevicePolicyManager.getResources().getString("Settings.ALWAYS_ON_VPN_PERSONAL_PROFILE", new C0952xe5445be4(this)));
        } else {
            preference.setTitle((CharSequence) this.mDevicePolicyManager.getResources().getString("Settings.ALWAYS_ON_VPN_DEVICE", new C0953xe5445be5(this)));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$updateState$0() {
        return this.mContext.getString(R$string.enterprise_privacy_always_on_vpn_personal);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$updateState$1() {
        return this.mContext.getString(R$string.enterprise_privacy_always_on_vpn_device);
    }

    public boolean isAvailable() {
        return this.mFeatureProvider.isAlwaysOnVpnSetInCurrentUser();
    }
}
