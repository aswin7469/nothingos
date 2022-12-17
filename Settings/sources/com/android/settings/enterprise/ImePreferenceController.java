package com.android.settings.enterprise;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.AbstractPreferenceController;

public class ImePreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final EnterprisePrivacyFeatureProvider mFeatureProvider;

    public String getPreferenceKey() {
        return "input_method";
    }

    public ImePreferenceController(Context context) {
        super(context);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context);
    }

    public void updateState(Preference preference) {
        preference.setSummary((CharSequence) ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getResources().getString("Settings.ADMIN_ACTION_SET_INPUT_METHOD_NAME", new ImePreferenceController$$ExternalSyntheticLambda0(this), new Object[]{this.mFeatureProvider.getImeLabelIfOwnerSet()}));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$updateState$0() {
        return this.mContext.getResources().getString(R$string.enterprise_privacy_input_method_name, new Object[]{this.mFeatureProvider.getImeLabelIfOwnerSet()});
    }

    public boolean isAvailable() {
        return this.mFeatureProvider.getImeLabelIfOwnerSet() != null;
    }
}
