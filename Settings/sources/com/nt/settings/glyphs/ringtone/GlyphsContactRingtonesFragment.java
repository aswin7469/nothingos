package com.nt.settings.glyphs.ringtone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsContactRingtonesFragment extends DashboardFragment implements OnActivityResultListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_glyphs_contact_ringtones);
    private GlyphsAddContactRingtoneController mAddContactRingtoneController = null;
    private List<AbstractPreferenceController> mControllers;
    private GlyphsCustomContactRingtoneController mCustomContactRingtoneController;

    private void initPreference() {
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "AppNotificationHome";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 2000;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initPreference();
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_glyphs_contact_ringtones;
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        this.mControllers = arrayList;
        arrayList.add(new GlyphsRingtoneDefaultController(context, "glyphs_default_ringtone"));
        List<AbstractPreferenceController> list = this.mControllers;
        GlyphsAddContactRingtoneController glyphsAddContactRingtoneController = new GlyphsAddContactRingtoneController(context, "glyphs_add_contact_ringtone");
        this.mAddContactRingtoneController = glyphsAddContactRingtoneController;
        list.add(glyphsAddContactRingtoneController);
        List<AbstractPreferenceController> list2 = this.mControllers;
        GlyphsCustomContactRingtoneController glyphsCustomContactRingtoneController = new GlyphsCustomContactRingtoneController(context, "glyphs_contact_ringtones_list");
        this.mCustomContactRingtoneController = glyphsCustomContactRingtoneController;
        list2.add(glyphsCustomContactRingtoneController);
        return this.mControllers;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        List<AbstractPreferenceController> list = this.mControllers;
        if (list != null) {
            for (AbstractPreferenceController abstractPreferenceController : list) {
                if (abstractPreferenceController instanceof BasePreferenceController) {
                    ((BasePreferenceController) abstractPreferenceController).onActivityControllerResult(i, i2, intent);
                }
            }
        }
    }

    @Override // com.android.settings.SettingsPreferenceFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        GlyphsCustomContactRingtoneController glyphsCustomContactRingtoneController = this.mCustomContactRingtoneController;
        if (glyphsCustomContactRingtoneController != null) {
            glyphsCustomContactRingtoneController.onDestroy();
        }
    }
}
