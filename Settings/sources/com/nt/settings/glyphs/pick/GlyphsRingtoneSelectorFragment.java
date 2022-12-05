package com.nt.settings.glyphs.pick;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.core.SettingsBaseActivity;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nt.settings.glyphs.pick.GlyphsAddAlarmController;
import com.nt.settings.glyphs.pick.GlyphsAlarmListController;
import com.nt.settings.glyphs.utils.UrlUtil;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsRingtoneSelectorFragment extends DashboardFragment implements OnActivityResultListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER;
    private static int mType;
    private GlyphsAlarmListController mAlarmListController;
    private String mContactName;
    private List<AbstractPreferenceController> mControllers;
    private Uri mExistingUri;
    private boolean mHasDefaultItem;
    private MenuInflater mMenuInflater;
    private Menu mOptionsMenu;
    private MenuItem mSaveMenu;
    private int mSoundOnly;
    private Uri mUriForDefaultItem;

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "RingtoneSelector";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 2000;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        Uri uri;
        Intent intent = getIntent();
        mType = getIntent().getIntExtra("android.intent.extra.ringtone.TYPE", -1);
        this.mHasDefaultItem = intent.getBooleanExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
        this.mUriForDefaultItem = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
        this.mSoundOnly = intent.getIntExtra("key_sound_only", -1);
        this.mContactName = intent.getStringExtra("contact_name");
        this.mExistingUri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
        Log.d("RingtoneSelector", "mHasDefaultItem " + this.mUriForDefaultItem + "  " + mType + "  " + this.mSoundOnly + "  " + this.mContactName);
        if (this.mSoundOnly == -1 && (uri = this.mUriForDefaultItem) != null) {
            String param = UrlUtil.getParam(uri.toString(), "soundOnly");
            if (!TextUtils.isEmpty(param)) {
                this.mSoundOnly = Integer.parseInt(param);
            }
        }
        super.onAttach(context);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        GlyphsAlarmListController glyphsAlarmListController = this.mAlarmListController;
        if (glyphsAlarmListController != null) {
            glyphsAlarmListController.release();
        }
    }

    static {
        SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(mType == 4 ? R.xml.nt_glyphs_alarm_ringtones_selector : R.xml.nt_glyphs_ringtones_selector);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        Log.d("RingtoneSelector", "createPreferenceControllers 1 " + mType);
        return mType == 4 ? R.xml.nt_glyphs_alarm_ringtones_selector : R.xml.nt_glyphs_ringtones_selector;
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        this.mControllers = new ArrayList();
        Log.d("RingtoneSelector", "createPreferenceControllers " + mType);
        if (mType == 4) {
            List<AbstractPreferenceController> list = this.mControllers;
            GlyphsAlarmListController glyphsAlarmListController = new GlyphsAlarmListController(context, "glyphs_alarm_list", this.mExistingUri, new GlyphsAlarmListController.OnChangeListener() { // from class: com.nt.settings.glyphs.pick.GlyphsRingtoneSelectorFragment.1
                @Override // com.nt.settings.glyphs.pick.GlyphsAlarmListController.OnChangeListener
                public void onChecked(boolean z) {
                    if (GlyphsRingtoneSelectorFragment.this.mSaveMenu != null) {
                        GlyphsRingtoneSelectorFragment.this.mSaveMenu.setEnabled(z);
                    }
                }
            });
            this.mAlarmListController = glyphsAlarmListController;
            list.add(glyphsAlarmListController);
            this.mControllers.add(new GlyphsAddAlarmController(context, "glyphs_add_alarm", new GlyphsAddAlarmController.OnChangeListener() { // from class: com.nt.settings.glyphs.pick.GlyphsRingtoneSelectorFragment.2
                @Override // com.nt.settings.glyphs.pick.GlyphsAddAlarmController.OnChangeListener
                public void onChange() {
                    if (GlyphsRingtoneSelectorFragment.this.mAlarmListController != null) {
                        GlyphsRingtoneSelectorFragment.this.mAlarmListController.updateData();
                    }
                }
            }));
        } else {
            this.mControllers.add(new GlyphsCurrentRingtoneController(context, "glyphs_current_ringtone", getRingtoneType(), this.mExistingUri, this.mSoundOnly, this.mContactName));
            this.mControllers.add(new GlyphsNtSoundController(context, "glyphs_nt_sound", getRingtoneType(), this.mExistingUri, this.mSoundOnly, this.mContactName));
            this.mControllers.add(new GlyphsMySoundController(context, "glyphs_my_sound", getRingtoneType(), this.mExistingUri, this.mSoundOnly));
        }
        return this.mControllers;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (mType == 4) {
            setHasOptionsMenu(true);
        } else {
            setTitle();
        }
    }

    private void setTitle() {
        String string = getActivity().getString(R.string.nt_glyphs_select_sound_title);
        int i = mType;
        if (i == 1) {
            string = getActivity().getString(R.string.nt_glyphs_select_ringtone_title);
        } else if (i == 4) {
            string = getActivity().getString(R.string.nt_glyphs_alarm_sound_title);
        }
        ((SettingsBaseActivity) getActivity()).setTitle(string);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        GlyphsAlarmListController glyphsAlarmListController;
        FragmentActivity activity = getActivity();
        if (activity != null && mType == 4) {
            this.mOptionsMenu = menu;
            this.mMenuInflater = menuInflater;
            menuInflater.inflate(R.menu.app_ringtone_selector_save, menu);
            MenuItem findItem = this.mOptionsMenu.findItem(R.id.add_save_menu);
            this.mSaveMenu = findItem;
            if (findItem != null && (glyphsAlarmListController = this.mAlarmListController) != null) {
                findItem.setEnabled(glyphsAlarmListController.isChecked());
            }
            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.action_bar);
            if (toolbar == null) {
                return;
            }
            toolbar.setPadding(0, 0, activity.getResources().getDimensionPixelSize(R.dimen.nt_actionbar_right_margin), 0);
        }
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_save_menu) {
            if (!this.mAlarmListController.isChecked()) {
                return false;
            }
            Uri currentRingtoneUri = this.mAlarmListController.getCurrentRingtoneUri();
            this.mAlarmListController.release();
            GlyphsPickResultHelper.setResult(getActivity(), currentRingtoneUri, 1);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private int getRingtoneType() {
        return mType;
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
}
