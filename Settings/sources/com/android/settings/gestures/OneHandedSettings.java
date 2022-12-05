package com.android.settings.gestures;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import androidx.fragment.app.FragmentActivity;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.settings.R;
import com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment;
import com.android.settings.gestures.OneHandedSettingsUtils;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.widget.IllustrationPreference;
/* loaded from: classes.dex */
public class OneHandedSettings extends AccessibilityShortcutPreferenceFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.one_handed_settings) { // from class: com.android.settings.gestures.OneHandedSettings.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.settings.search.BaseSearchIndexProvider
        public boolean isPageSearchEnabled(Context context) {
            return OneHandedSettingsUtils.isSupportOneHandedMode();
        }
    };
    private String mFeatureName;
    private OneHandedSettingsUtils mUtils;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return null;
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1841;
    }

    @Override // com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment
    protected String getShortcutPreferenceKey() {
        return "one_handed_shortcuts_preference";
    }

    @Override // com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment
    protected boolean showGeneralCategory() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    /* renamed from: updatePreferenceStates */
    public void lambda$onStart$0() {
        int i;
        OneHandedSettingsUtils.setUserId(UserHandle.myUserId());
        super.updatePreferenceStates();
        IllustrationPreference illustrationPreference = (IllustrationPreference) getPreferenceScreen().findPreference("one_handed_header");
        if (illustrationPreference != null) {
            if (OneHandedSettingsUtils.isSwipeDownNotificationEnabled(getContext())) {
                i = R.raw.lottie_swipe_for_notifications;
            } else {
                i = R.raw.lottie_one_hand_mode;
            }
            illustrationPreference.setLottieAnimationResId(i);
        }
    }

    @Override // com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public int getDialogMetricsCategory(int i) {
        int dialogMetricsCategory = super.getDialogMetricsCategory(i);
        if (dialogMetricsCategory == 0) {
            return 1841;
        }
        return dialogMetricsCategory;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        OneHandedSettingsUtils oneHandedSettingsUtils = new OneHandedSettingsUtils(getContext());
        this.mUtils = oneHandedSettingsUtils;
        oneHandedSettingsUtils.registerToggleAwareObserver(new OneHandedSettingsUtils.TogglesCallback() { // from class: com.android.settings.gestures.OneHandedSettings$$ExternalSyntheticLambda0
            @Override // com.android.settings.gestures.OneHandedSettingsUtils.TogglesCallback
            public final void onChange(Uri uri) {
                OneHandedSettings.this.lambda$onStart$1(uri);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$1(Uri uri) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() { // from class: com.android.settings.gestures.OneHandedSettings$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    OneHandedSettings.this.lambda$onStart$0();
                }
            });
        }
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.mUtils.unregisterToggleAwareObserver();
    }

    @Override // com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment
    protected ComponentName getComponentName() {
        return AccessibilityShortcutController.ONE_HANDED_COMPONENT_NAME;
    }

    @Override // com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment
    protected CharSequence getLabelName() {
        return this.mFeatureName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.one_handed_settings;
    }

    @Override // com.android.settings.accessibility.AccessibilityShortcutPreferenceFragment, com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        this.mFeatureName = getContext().getString(R.string.one_handed_title);
        super.onCreate(bundle);
    }
}
