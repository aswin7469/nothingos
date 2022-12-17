package com.nothing.settings.glyphs;

import android.app.Dialog;
import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.DialogCreatable;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.notification.SoundSettings;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nothing.settings.glyphs.GlyphsFilpToGlyphSwitchPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class GlyphsFlipToGlyphPreferenceFragment extends SwitchSettingsPreferenceFragment implements Preference.OnPreferenceClickListener, GlyphsFilpToGlyphSwitchPreferenceController.DialogHelper {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.nt_glyphs_flip_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return GlyphsFlipToGlyphPreferenceFragment.buildPreferenceControllers(context, (SoundSettings) null, (Lifecycle) null);
        }
    };
    private DialogCreatable mDialogDelegate;

    public int getHelpResource() {
        return 0;
    }

    public String getLogTag() {
        return "FlipToGlyph";
    }

    public int getMetricsCategory() {
        return 1845;
    }

    public String getSwitchKey() {
        return "led_effect_gestures_flip_ebable";
    }

    public boolean onPreferenceClick(Preference preference) {
        return true;
    }

    public void showDialog(int i) {
        throw null;
    }

    public String getFeatureString() {
        return getActivity().getString(R$string.nt_glyphs_flip_features);
    }

    public String getSwitchTitle() {
        return getActivity().getString(R$string.nt_glyphs_flip_title);
    }

    public int getLayoutId() {
        return R$xml.nt_glyphs_flip_settings;
    }

    public void setDialogDelegate(DialogCreatable dialogCreatable) {
        this.mDialogDelegate = dialogCreatable;
    }

    public int getDialogMetricsCategory(int i) {
        DialogCreatable dialogCreatable = this.mDialogDelegate;
        if (dialogCreatable != null) {
            return dialogCreatable.getDialogMetricsCategory(i);
        }
        return 0;
    }

    public Dialog onCreateDialog(int i) {
        Dialog onCreateDialog;
        DialogCreatable dialogCreatable = this.mDialogDelegate;
        if (dialogCreatable != null && (onCreateDialog = dialogCreatable.onCreateDialog(i)) != null) {
            return onCreateDialog;
        }
        throw new IllegalArgumentException("Unsupported dialogId " + i);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, (SoundSettings) null, (Lifecycle) null);
    }

    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, SoundSettings soundSettings, Lifecycle lifecycle) {
        return new ArrayList();
    }
}
