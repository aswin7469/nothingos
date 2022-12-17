package com.android.settings.display;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.SensorPrivacyManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$array;
import com.android.settings.R$drawable;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.support.actionbar.HelpResourceProvider;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.search.SearchIndexableRaw;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.FooterPreference;
import com.android.settingslib.widget.SelectorWithWidgetPreference;
import java.util.ArrayList;
import java.util.List;

public class ScreenTimeoutSettings extends RadioButtonPickerFragment implements HelpResourceProvider {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.screen_timeout_settings) {
        public List<SearchIndexableRaw> getRawDataToIndex(Context context, boolean z) {
            if (!ScreenTimeoutSettings.isScreenAttentionAvailable(context)) {
                return null;
            }
            Resources resources = context.getResources();
            SearchIndexableRaw searchIndexableRaw = new SearchIndexableRaw(context);
            int i = R$string.adaptive_sleep_title;
            searchIndexableRaw.title = resources.getString(i);
            searchIndexableRaw.key = "adaptive_sleep";
            searchIndexableRaw.keywords = resources.getString(i);
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(searchIndexableRaw);
            return arrayList;
        }
    };
    AdaptiveSleepBatterySaverPreferenceController mAdaptiveSleepBatterySaverPreferenceController;
    AdaptiveSleepCameraStatePreferenceController mAdaptiveSleepCameraStatePreferenceController;
    AdaptiveSleepPreferenceController mAdaptiveSleepController;
    AdaptiveSleepPermissionPreferenceController mAdaptiveSleepPermissionController;
    RestrictedLockUtils.EnforcedAdmin mAdmin;
    Context mContext;
    private DevicePolicyManager mDevicePolicyManager;
    FooterPreference mDisableOptionsPreference;
    private CharSequence[] mInitialEntries;
    private CharSequence[] mInitialValues;
    private final MetricsFeatureProvider mMetricsFeatureProvider = FeatureFactory.getFactory(getContext()).getMetricsFeatureProvider();
    private SensorPrivacyManager mPrivacyManager;
    private FooterPreference mPrivacyPreference;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            ScreenTimeoutSettings.this.mAdaptiveSleepBatterySaverPreferenceController.updateVisibility();
            ScreenTimeoutSettings.this.mAdaptiveSleepController.updatePreference();
        }
    };

    public int getMetricsCategory() {
        return 1852;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        this.mInitialEntries = getResources().getStringArray(R$array.screen_timeout_entries);
        this.mInitialValues = getResources().getStringArray(R$array.screen_timeout_values);
        this.mAdaptiveSleepController = new AdaptiveSleepPreferenceController(context);
        this.mAdaptiveSleepPermissionController = new AdaptiveSleepPermissionPreferenceController(context);
        this.mAdaptiveSleepCameraStatePreferenceController = new AdaptiveSleepCameraStatePreferenceController(context);
        this.mAdaptiveSleepBatterySaverPreferenceController = new AdaptiveSleepBatterySaverPreferenceController(context);
        FooterPreference footerPreference = new FooterPreference(context);
        this.mPrivacyPreference = footerPreference;
        footerPreference.setIcon(R$drawable.ic_privacy_shield_24dp);
        this.mPrivacyPreference.setTitle(R$string.adaptive_sleep_privacy);
        this.mPrivacyPreference.setSelectable(false);
        this.mPrivacyPreference.setLayoutResource(R$layout.preference_footer);
        SensorPrivacyManager instance = SensorPrivacyManager.getInstance(context);
        this.mPrivacyManager = instance;
        instance.addSensorPrivacyListener(2, new ScreenTimeoutSettings$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onAttach$0(int i, boolean z) {
        this.mAdaptiveSleepController.updatePreference();
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        long longValue = getMaxScreenTimeout(getContext()).longValue();
        if (this.mInitialValues != null) {
            int i = 0;
            while (true) {
                CharSequence[] charSequenceArr = this.mInitialValues;
                if (i >= charSequenceArr.length) {
                    break;
                }
                if (Long.parseLong(charSequenceArr[i].toString()) <= longValue) {
                    arrayList.add(new TimeoutCandidateInfo(this.mInitialEntries[i], this.mInitialValues[i].toString(), true));
                }
                i++;
            }
        } else {
            Log.e("ScreenTimeout", "Screen timeout options do not exist.");
        }
        return arrayList;
    }

    public void onStart() {
        super.onStart();
        this.mAdaptiveSleepPermissionController.updateVisibility();
        this.mAdaptiveSleepCameraStatePreferenceController.updateVisibility();
        this.mAdaptiveSleepBatterySaverPreferenceController.updateVisibility();
        this.mAdaptiveSleepController.updatePreference();
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED"));
    }

    public void onStop() {
        super.onStop();
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public void updateCandidates() {
        String defaultKey = getDefaultKey();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removeAll();
        List<? extends CandidateInfo> candidates = getCandidates();
        if (candidates != null) {
            for (CandidateInfo candidateInfo : candidates) {
                SelectorWithWidgetPreference selectorWithWidgetPreference = new SelectorWithWidgetPreference(getPrefContext());
                bindPreference(selectorWithWidgetPreference, candidateInfo.getKey(), candidateInfo, defaultKey);
                preferenceScreen.addPreference(selectorWithWidgetPreference);
            }
            long parseLong = Long.parseLong(defaultKey);
            long longValue = getMaxScreenTimeout(getContext()).longValue();
            if (!candidates.isEmpty() && parseLong > longValue) {
                ((SelectorWithWidgetPreference) preferenceScreen.getPreference(candidates.size() - 1)).setChecked(true);
            }
            FooterPreference footerPreference = new FooterPreference(this.mContext);
            this.mPrivacyPreference = footerPreference;
            footerPreference.setIcon(R$drawable.ic_privacy_shield_24dp);
            this.mPrivacyPreference.setTitle(R$string.adaptive_sleep_privacy);
            this.mPrivacyPreference.setSelectable(false);
            this.mPrivacyPreference.setLayoutResource(R$layout.preference_footer);
            if (isScreenAttentionAvailable(getContext())) {
                this.mAdaptiveSleepPermissionController.addToScreen(preferenceScreen);
                this.mAdaptiveSleepCameraStatePreferenceController.addToScreen(preferenceScreen);
                this.mAdaptiveSleepController.addToScreen(preferenceScreen);
                this.mAdaptiveSleepBatterySaverPreferenceController.addToScreen(preferenceScreen);
                preferenceScreen.addPreference(this.mPrivacyPreference);
            }
            if (this.mAdmin != null) {
                setupDisabledFooterPreference();
                preferenceScreen.addPreference(this.mDisableOptionsPreference);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setupDisabledFooterPreference() {
        String string = this.mDevicePolicyManager.getResources().getString("Settings.OTHER_OPTIONS_DISABLED_BY_ADMIN", new ScreenTimeoutSettings$$ExternalSyntheticLambda0(this));
        String string2 = getResources().getString(R$string.admin_more_details);
        FooterPreference footerPreference = new FooterPreference(getContext());
        this.mDisableOptionsPreference = footerPreference;
        footerPreference.setTitle((CharSequence) string);
        this.mDisableOptionsPreference.setSelectable(false);
        this.mDisableOptionsPreference.setLearnMoreText(string2);
        this.mDisableOptionsPreference.setLearnMoreAction(new ScreenTimeoutSettings$$ExternalSyntheticLambda1(this));
        this.mDisableOptionsPreference.setIcon(R$drawable.ic_info_outline_24dp);
        this.mDisableOptionsPreference.setOrder(2147483646);
        this.mPrivacyPreference.setOrder(2147483645);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$setupDisabledFooterPreference$1() {
        return getResources().getString(R$string.admin_disabled_other_options);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupDisabledFooterPreference$2(View view) {
        RestrictedLockUtils.sendShowAdminSupportDetailsIntent(getContext(), this.mAdmin);
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        return getCurrentSystemScreenTimeout(getContext());
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        setCurrentSystemScreenTimeout(getContext(), str);
        return true;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.screen_timeout_settings;
    }

    public int getHelpResource() {
        return R$string.help_url_adaptive_sleep;
    }

    private Long getMaxScreenTimeout(Context context) {
        DevicePolicyManager devicePolicyManager;
        if (context == null || (devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)) == null) {
            return Long.MAX_VALUE;
        }
        RestrictedLockUtils.EnforcedAdmin checkIfMaximumTimeToLockIsSet = RestrictedLockUtilsInternal.checkIfMaximumTimeToLockIsSet(context);
        this.mAdmin = checkIfMaximumTimeToLockIsSet;
        if (checkIfMaximumTimeToLockIsSet != null) {
            return Long.valueOf(devicePolicyManager.getMaximumTimeToLock((ComponentName) null, UserHandle.myUserId()));
        }
        return Long.MAX_VALUE;
    }

    private String getCurrentSystemScreenTimeout(Context context) {
        if (context == null) {
            return Long.toString(30000);
        }
        return Long.toString(Settings.System.getLong(context.getContentResolver(), "screen_off_timeout", 30000));
    }

    private void setCurrentSystemScreenTimeout(Context context, String str) {
        if (context != null) {
            try {
                long parseLong = Long.parseLong(str);
                this.mMetricsFeatureProvider.action(context, 1754, (int) parseLong);
                Settings.System.putLong(context.getContentResolver(), "screen_off_timeout", parseLong);
            } catch (NumberFormatException e) {
                Log.e("ScreenTimeout", "could not persist screen timeout setting", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public static boolean isScreenAttentionAvailable(Context context) {
        return AdaptiveSleepPreferenceController.isAdaptiveSleepSupported(context);
    }

    private static class TimeoutCandidateInfo extends CandidateInfo {
        private final String mKey;
        private final CharSequence mLabel;

        public Drawable loadIcon() {
            return null;
        }

        TimeoutCandidateInfo(CharSequence charSequence, String str, boolean z) {
            super(z);
            this.mLabel = charSequence;
            this.mKey = str;
        }

        public CharSequence loadLabel() {
            return this.mLabel;
        }

        public String getKey() {
            return this.mKey;
        }
    }
}
