package com.android.settings.accessibility;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.Preference;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.settings.R$array;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.accessibility.DaltonizerRadioButtonPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ToggleDaltonizerPreferenceFragment extends ToggleFeaturePreferenceFragment implements DaltonizerRadioButtonPreferenceController.OnChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.accessibility_daltonizer_settings);
    private static final List<AbstractPreferenceController> sControllers = new ArrayList();

    public int getMetricsCategory() {
        return 5;
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle) {
        if (sControllers.size() == 0) {
            String[] stringArray = context.getResources().getStringArray(R$array.daltonizer_mode_keys);
            for (String daltonizerRadioButtonPreferenceController : stringArray) {
                sControllers.add(new DaltonizerRadioButtonPreferenceController(context, lifecycle, daltonizerRadioButtonPreferenceController));
            }
        }
        return sControllers;
    }

    public void onCheckedChanged(Preference preference) {
        for (AbstractPreferenceController updateState : sControllers) {
            updateState.updateState(preference);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mComponentName = AccessibilityShortcutController.DALTONIZER_COMPONENT_NAME;
        this.mPackageName = getText(R$string.accessibility_display_daltonizer_preference_title);
        this.mHtmlDescription = getText(R$string.accessibility_display_daltonizer_preference_subtitle);
        this.mTopIntroTitle = getText(R$string.accessibility_daltonizer_about_intro_text);
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        updateFooterPreference();
        return onCreateView;
    }

    /* access modifiers changed from: protected */
    public void registerKeysToObserverCallback(AccessibilitySettingsContentObserver accessibilitySettingsContentObserver) {
        super.registerKeysToObserverCallback(accessibilitySettingsContentObserver);
        ArrayList arrayList = new ArrayList(1);
        arrayList.add("accessibility_display_daltonizer_enabled");
        accessibilitySettingsContentObserver.registerKeysToObserverCallback(arrayList, new ToggleDaltonizerPreferenceFragment$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerKeysToObserverCallback$0(String str) {
        updateSwitchBarToggleSwitch();
    }

    private void updateFooterPreference() {
        String string = getPrefContext().getString(R$string.accessibility_daltonizer_about_title);
        String string2 = getPrefContext().getString(R$string.accessibility_daltonizer_footer_learn_more_content_description);
        this.mFooterPreferenceController.setIntroductionTitle(string);
        this.mFooterPreferenceController.setupHelpLink(getHelpResource(), string2);
        this.mFooterPreferenceController.displayPreference(getPreferenceScreen());
    }

    /* access modifiers changed from: protected */
    public List<String> getPreferenceOrderList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("top_intro");
        arrayList.add("daltonizer_preview");
        arrayList.add("use_service");
        arrayList.add("daltonizer_mode_deuteranomaly");
        arrayList.add("daltonizer_mode_protanomaly");
        arrayList.add("daltonizer_mode_tritanomaly");
        arrayList.add("daltonizer_mode_grayscale");
        arrayList.add("general_categories");
        arrayList.add("html_description");
        return arrayList;
    }

    public void onResume() {
        super.onResume();
        updateSwitchBarToggleSwitch();
        Iterator<AbstractPreferenceController> it = buildPreferenceControllers(getPrefContext(), getSettingsLifecycle()).iterator();
        while (it.hasNext()) {
            DaltonizerRadioButtonPreferenceController daltonizerRadioButtonPreferenceController = (DaltonizerRadioButtonPreferenceController) it.next();
            daltonizerRadioButtonPreferenceController.setOnChangeListener(this);
            daltonizerRadioButtonPreferenceController.displayPreference(getPreferenceScreen());
        }
    }

    public void onPause() {
        Iterator<AbstractPreferenceController> it = buildPreferenceControllers(getPrefContext(), getSettingsLifecycle()).iterator();
        while (it.hasNext()) {
            ((DaltonizerRadioButtonPreferenceController) it.next()).setOnChangeListener((DaltonizerRadioButtonPreferenceController.OnChangeListener) null);
        }
        super.onPause();
    }

    public int getHelpResource() {
        return R$string.help_url_color_correction;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.accessibility_daltonizer_settings;
    }

    /* access modifiers changed from: protected */
    public void onPreferenceToggled(String str, boolean z) {
        boolean z2 = false;
        if (Settings.Secure.getInt(getContentResolver(), "accessibility_display_daltonizer_enabled", 0) == 1) {
            z2 = true;
        }
        if (z != z2) {
            if (z) {
                showQuickSettingsTooltipIfNeeded(1);
            }
            AccessibilityStatsLogUtils.logAccessibilityServiceEnabled(this.mComponentName, z);
            Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer_enabled", z ? 1 : 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onRemoveSwitchPreferenceToggleSwitch() {
        super.onRemoveSwitchPreferenceToggleSwitch();
        this.mToggleServiceSwitchPreference.setOnPreferenceClickListener((Preference.OnPreferenceClickListener) null);
    }

    /* access modifiers changed from: protected */
    public void updateToggleServiceTitle(SettingsMainSwitchPreference settingsMainSwitchPreference) {
        settingsMainSwitchPreference.setTitle(R$string.accessibility_daltonizer_primary_switch_title);
    }

    /* access modifiers changed from: protected */
    public CharSequence getShortcutTitle() {
        return getText(R$string.accessibility_daltonizer_shortcut_title);
    }

    /* access modifiers changed from: package-private */
    public int getUserShortcutTypes() {
        return AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), this.mComponentName);
    }

    /* access modifiers changed from: package-private */
    public ComponentName getTileComponentName() {
        return AccessibilityShortcutController.DALTONIZER_TILE_COMPONENT_NAME;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getTileTooltipContent(int i) {
        int i2;
        if (i == 0) {
            i2 = R$string.accessibility_color_correction_qs_tooltip_content;
        } else {
            i2 = R$string.accessibility_color_correction_auto_added_qs_tooltip_content;
        }
        return getText(i2);
    }

    /* access modifiers changed from: protected */
    public void updateSwitchBarToggleSwitch() {
        boolean z = false;
        if (Settings.Secure.getInt(getContentResolver(), "accessibility_display_daltonizer_enabled", 0) == 1) {
            z = true;
        }
        if (this.mToggleServiceSwitchPreference.isChecked() != z) {
            this.mToggleServiceSwitchPreference.setChecked(z);
        }
    }
}
