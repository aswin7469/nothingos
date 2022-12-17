package com.android.settings.enterprise;

import android.content.Context;
import android.provider.SearchIndexableResource;
import com.android.settings.R$xml;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.Collections;
import java.util.List;

public class PrivacySettingsFinancedPreference implements PrivacySettingsPreference {
    private final Context mContext;

    public PrivacySettingsFinancedPreference(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public int getPreferenceScreenResId() {
        return R$xml.financed_privacy_settings;
    }

    public List<SearchIndexableResource> getXmlResourcesToIndex() {
        SearchIndexableResource searchIndexableResource = new SearchIndexableResource(this.mContext);
        searchIndexableResource.xmlResId = getPreferenceScreenResId();
        return Collections.singletonList(searchIndexableResource);
    }

    public List<AbstractPreferenceController> createPreferenceControllers(boolean z) {
        return Collections.emptyList();
    }
}
