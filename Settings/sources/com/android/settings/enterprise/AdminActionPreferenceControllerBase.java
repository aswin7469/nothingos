package com.android.settings.enterprise;

import android.content.Context;
import android.text.format.DateUtils;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.Date;
/* loaded from: classes.dex */
public abstract class AdminActionPreferenceControllerBase extends AbstractPreferenceController implements PreferenceControllerMixin {
    protected final EnterprisePrivacyFeatureProvider mFeatureProvider;

    protected abstract Date getAdminActionTimestamp();

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return true;
    }

    public AdminActionPreferenceControllerBase(Context context) {
        super(context);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        String formatDateTime;
        Date adminActionTimestamp = getAdminActionTimestamp();
        if (adminActionTimestamp == null) {
            formatDateTime = this.mContext.getString(R.string.enterprise_privacy_none);
        } else {
            formatDateTime = DateUtils.formatDateTime(this.mContext, adminActionTimestamp.getTime(), 17);
        }
        preference.setSummary(formatDateTime);
    }
}
