package com.android.settings.notification.zen;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.service.notification.ZenPolicy;
import android.util.Pair;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.SelectorWithWidgetPreference;

public class ZenRuleVisEffectsAllPreferenceController extends AbstractZenCustomRulePreferenceController {
    private SelectorWithWidgetPreference mPreference;

    public /* bridge */ /* synthetic */ boolean isAvailable() {
        return super.isAvailable();
    }

    public /* bridge */ /* synthetic */ void onResume() {
        super.onResume();
    }

    public /* bridge */ /* synthetic */ void setIdAndRule(String str, AutomaticZenRule automaticZenRule) {
        super.setIdAndRule(str, automaticZenRule);
    }

    public ZenRuleVisEffectsAllPreferenceController(Context context, Lifecycle lifecycle, String str) {
        super(context, str, lifecycle);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        SelectorWithWidgetPreference selectorWithWidgetPreference = (SelectorWithWidgetPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = selectorWithWidgetPreference;
        selectorWithWidgetPreference.setOnClickListener(new C1246x33d5cca6(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(SelectorWithWidgetPreference selectorWithWidgetPreference) {
        this.mMetricsFeatureProvider.action(this.mContext, 1396, (Pair<Integer, Object>[]) new Pair[]{Pair.create(1603, this.mId)});
        this.mRule.setZenPolicy(new ZenPolicy.Builder(this.mRule.getZenPolicy()).showAllVisualEffects().build());
        this.mBackend.updateZenRule(this.mId, this.mRule);
    }

    public void updateState(Preference preference) {
        AutomaticZenRule automaticZenRule;
        super.updateState(preference);
        if (this.mId != null && (automaticZenRule = this.mRule) != null && automaticZenRule.getZenPolicy() != null) {
            this.mPreference.setChecked(this.mRule.getZenPolicy().shouldShowAllVisualEffects());
        }
    }
}
