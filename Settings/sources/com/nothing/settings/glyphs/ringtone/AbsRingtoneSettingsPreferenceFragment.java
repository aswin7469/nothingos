package com.nothing.settings.glyphs.ringtone;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.SwitchPreference;
import com.android.settings.R$dimen;
import com.android.settings.R$id;
import com.android.settings.R$menu;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nothing.settings.glyphs.preference.GlyphFooterButtonPreference;
import com.nothing.settings.glyphs.preference.RingtoneSelectorPreference;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsRingtoneSettingsPreferenceFragment extends DashboardFragment implements GlyphFooterButtonPreference.OnConfirmListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_ringtone_settings);
    private Menu mOptionsMenu;
    protected RingtoneSelectorPreference mRingtoneSelectorPreference;
    protected SwitchPreference mSoundOnlySwitchPreference;

    /* access modifiers changed from: protected */
    public abstract List<AbstractPreferenceController> getControllers();

    public int getHelpResource() {
        return 0;
    }

    public String getLogTag() {
        return "RingtoneSettings";
    }

    public int getMetricsCategory() {
        return 1845;
    }

    /* access modifiers changed from: protected */
    public abstract void onClickSetRingtone();

    public void onConfirm(View view) {
    }

    public void onPause() {
        super.onPause();
        RingtoneSelectorPreference ringtoneSelectorPreference = this.mRingtoneSelectorPreference;
        if (ringtoneSelectorPreference != null) {
            ringtoneSelectorPreference.onPause();
        }
    }

    public int getPreferenceScreenResId() {
        return R$xml.glyphs_ringtone_settings;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            this.mOptionsMenu = menu;
            menuInflater.inflate(R$menu.app_ringtone_selector_save, menu);
            Toolbar toolbar = (Toolbar) activity.findViewById(R$id.action_bar);
            if (toolbar != null) {
                toolbar.setPadding(0, 0, activity.getResources().getDimensionPixelSize(R$dimen.nt_actionbar_right_margin), 0);
            }
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initTitle();
        initPreference();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R$id.add_save_menu) {
            onClickSetRingtone();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(getControllers());
        return arrayList;
    }

    private void initTitle() {
        getActivity().setTitle(getActivity().getString(R$string.nt_glyphs_nt_ringtones_title));
    }

    private void initPreference() {
        this.mRingtoneSelectorPreference = (RingtoneSelectorPreference) findPreference("key_glyphs_ringtone_selector");
        this.mSoundOnlySwitchPreference = (SwitchPreference) findPreference("key_glyphs_switch_sound_only");
    }
}
