package com.nt.settings.glyphs;

import android.app.Dialog;
import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.DialogCreatable;
import com.android.settings.R;
import com.android.settings.notification.SoundSettings;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nt.settings.glyphs.GlyphsFilpToGlyphSwitchPreferenceController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsFlipToGlyphPreferenceFragment extends GlyphsSwitchSettingsPreferenceFragment implements Preference.OnPreferenceClickListener, GlyphsFilpToGlyphSwitchPreferenceController.DialogHelper {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_glyphs_flip_settings) { // from class: com.nt.settings.glyphs.GlyphsFlipToGlyphPreferenceFragment.1
        @Override // com.android.settings.search.BaseSearchIndexProvider
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return GlyphsFlipToGlyphPreferenceFragment.buildPreferenceControllers(context, null, null);
        }
    };
    private DialogCreatable mDialogDelegate;

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment, com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment, com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "FlipToGlyph";
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment, com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1845;
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getSwitchKey() {
        return "led_effect_gestures_flip_ebable";
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        return true;
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.accessibility.MagnificationModePreferenceController.DialogHelper
    public void showDialog(int i) {
        throw null;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getFeatureString() {
        return getActivity().getString(R.string.nt_glyphs_flip_features);
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    String getSwitchTitle() {
        return getActivity().getString(R.string.nt_glyphs_flip_title);
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment
    int getLayoutId() {
        return R.xml.nt_glyphs_flip_settings;
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment, com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.nt.settings.glyphs.GlyphsSwitchSettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // com.nt.settings.glyphs.GlyphsFilpToGlyphSwitchPreferenceController.DialogHelper
    public void setDialogDelegate(DialogCreatable dialogCreatable) {
        this.mDialogDelegate = dialogCreatable;
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public int getDialogMetricsCategory(int i) {
        DialogCreatable dialogCreatable = this.mDialogDelegate;
        if (dialogCreatable != null) {
            return dialogCreatable.getDialogMetricsCategory(i);
        }
        return 0;
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public Dialog onCreateDialog(int i) {
        Dialog onCreateDialog;
        DialogCreatable dialogCreatable = this.mDialogDelegate;
        if (dialogCreatable == null || (onCreateDialog = dialogCreatable.onCreateDialog(i)) == null) {
            throw new IllegalArgumentException("Unsupported dialogId " + i);
        }
        return onCreateDialog;
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, SoundSettings soundSettings, Lifecycle lifecycle) {
        return new ArrayList();
    }
}
