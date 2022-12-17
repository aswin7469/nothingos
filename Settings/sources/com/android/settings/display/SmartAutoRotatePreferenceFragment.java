package com.android.settings.display;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.SettingsActivity;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.HelpUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.search.Indexable$SearchIndexProvider;
import com.android.settingslib.search.SearchIndexableRaw;
import com.android.settingslib.widget.FooterPreference;
import java.util.List;

public class SmartAutoRotatePreferenceFragment extends DashboardFragment {
    static final String AUTO_ROTATE_MAIN_SWITCH_PREFERENCE_KEY = "auto_rotate_main_switch";
    static final String AUTO_ROTATE_SWITCH_PREFERENCE_KEY = "auto_rotate_switch";
    public static final Indexable$SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.auto_rotate_settings) {
        public List<SearchIndexableRaw> getRawDataToIndex(Context context, boolean z) {
            return DeviceStateAutoRotationHelper.getRawDataToIndex(context, z);
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SmartAutoRotatePreferenceFragment";
    }

    public int getMetricsCategory() {
        return 1867;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.auto_rotate_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        DeviceStateAutoRotationHelper.initControllers(getLifecycle(), useAll(DeviceStateAutoRotateSettingController.class));
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return DeviceStateAutoRotationHelper.createPreferenceControllers(context);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        createHeader(settingsActivity);
        Preference findPreference = findPreference("auto_rotate_footer_preference");
        if (findPreference != null) {
            findPreference.setVisible(SmartAutoRotateController.isRotationResolverServiceAvailable(settingsActivity));
            setupFooter();
        }
        return onCreateView;
    }

    /* access modifiers changed from: package-private */
    public void createHeader(SettingsActivity settingsActivity) {
        boolean isDeviceStateRotationEnabled = DeviceStateAutoRotationHelper.isDeviceStateRotationEnabled(settingsActivity);
        if (!SmartAutoRotateController.isRotationResolverServiceAvailable(settingsActivity) || isDeviceStateRotationEnabled) {
            findPreference(AUTO_ROTATE_MAIN_SWITCH_PREFERENCE_KEY).setVisible(false);
        } else {
            findPreference(AUTO_ROTATE_SWITCH_PREFERENCE_KEY).setVisible(false);
        }
    }

    public int getHelpResource() {
        return R$string.help_url_auto_rotate_settings;
    }

    /* access modifiers changed from: package-private */
    public void setupFooter() {
        if (!TextUtils.isEmpty(getString(getHelpResource()))) {
            addHelpLink();
        }
    }

    /* access modifiers changed from: package-private */
    public void addHelpLink() {
        FooterPreference footerPreference = (FooterPreference) findPreference("auto_rotate_footer_preference");
        if (footerPreference != null) {
            footerPreference.setLearnMoreAction(new SmartAutoRotatePreferenceFragment$$ExternalSyntheticLambda0(this));
            footerPreference.setLearnMoreText(getString(R$string.auto_rotate_link_a11y));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addHelpLink$0(View view) {
        startActivityForResult(HelpUtils.getHelpIntent(getContext(), getString(getHelpResource()), ""), 0);
    }
}
