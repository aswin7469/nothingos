package com.nt.settings.glyphs.ringtone;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.core.SettingsBaseActivity;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nt.settings.glyphs.widget.GlyphFooterButtonPreference;
import com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public abstract class GlyphsRingtoneSettingsPreferenceFragment extends DashboardFragment implements GlyphFooterButtonPreference.OnConfirmListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_glyphs_ringtone_settings);
    private Menu mOptionsMenu;
    protected GlyphsRingtoneSelectorPreference mRingtoneSelectorPreference;
    protected SwitchPreference mSoundOnlySwitchPreference;

    protected abstract List<AbstractPreferenceController> getControllers();

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "RingtoneSettings";
    }

    public int getMetricsCategory() {
        return 1845;
    }

    protected abstract void onClickSetRingtone();

    @Override // com.nt.settings.glyphs.widget.GlyphFooterButtonPreference.OnConfirmListener
    public void onConfirm(View view) {
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.mRingtoneSelectorPreference.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_glyphs_ringtone_settings;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        this.mOptionsMenu = menu;
        menuInflater.inflate(R.menu.app_ringtone_selector_save, menu);
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.action_bar);
        if (toolbar == null) {
            return;
        }
        toolbar.setPadding(0, 0, activity.getResources().getDimensionPixelSize(R.dimen.nt_actionbar_right_margin), 0);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initTitle();
        initPreference();
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_save_menu) {
            onClickSetRingtone();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(getControllers());
        return arrayList;
    }

    private void initTitle() {
        ((SettingsBaseActivity) getActivity()).setTitle(getActivity().getString(R.string.nt_glyphs_nt_ringtones_title));
    }

    private void initPreference() {
        this.mRingtoneSelectorPreference = (GlyphsRingtoneSelectorPreference) findPreference("key_glyphs_ringtone_selector");
        this.mSoundOnlySwitchPreference = (SwitchPreference) findPreference("key_glyphs_switch_sound_only");
    }
}
