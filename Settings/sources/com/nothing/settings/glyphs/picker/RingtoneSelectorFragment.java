package com.nothing.settings.glyphs.picker;

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
import com.android.settings.R$dimen;
import com.android.settings.R$id;
import com.android.settings.R$menu;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nothing.settings.glyphs.utils.ResultPickHelper;
import com.nothing.settings.glyphs.utils.UrlUtil;
import java.util.ArrayList;
import java.util.List;

public class RingtoneSelectorFragment extends DashboardFragment implements OnActivityResultListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(mType == 4 ? R$xml.glyphs_alarm_ringtones_selector : R$xml.glyphs_ringtones_selector);
    private static int mType;
    private GlyphsAlarmListController mAlarmListController;
    private String mContactName;
    private List<AbstractPreferenceController> mControllers;
    private Uri mExistingUri;
    private boolean mHasDefaultItem;
    private MenuInflater mMenuInflater;
    private Menu mOptionsMenu;
    private String mPageTitle;
    private MenuItem mSaveMenu;
    private int mSoundOnly;
    private Uri mUriForDefaultItem;

    public int getHelpResource() {
        return 0;
    }

    public String getLogTag() {
        return "RingtoneSelector";
    }

    public int getMetricsCategory() {
        return 2000;
    }

    public void onAttach(Context context) {
        Uri uri;
        Intent intent = getIntent();
        mType = getIntent().getIntExtra("android.intent.extra.ringtone.TYPE", -1);
        this.mHasDefaultItem = intent.getBooleanExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
        this.mUriForDefaultItem = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
        this.mSoundOnly = intent.getIntExtra("key_sound_only", -1);
        this.mContactName = intent.getStringExtra("contact_name");
        this.mExistingUri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
        this.mPageTitle = intent.getStringExtra("page_title");
        Log.d("RingtoneSelector", "onAttach Ringtone type:" + mType + ", mExistingUri:" + this.mExistingUri);
        if (this.mSoundOnly == -1 && (uri = this.mUriForDefaultItem) != null) {
            String param = UrlUtil.getParam(uri.toString(), "soundOnly");
            if (!TextUtils.isEmpty(param)) {
                this.mSoundOnly = Integer.parseInt(param);
            }
        }
        super.onAttach(context);
    }

    public void onPause() {
        super.onPause();
        GlyphsAlarmListController glyphsAlarmListController = this.mAlarmListController;
        if (glyphsAlarmListController != null) {
            glyphsAlarmListController.release();
        }
    }

    public int getPreferenceScreenResId() {
        Log.d("RingtoneSelector", "createPreferenceControllers 1 " + mType);
        return mType == 4 ? R$xml.glyphs_alarm_ringtones_selector : R$xml.glyphs_ringtones_selector;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        this.mControllers = new ArrayList();
        Log.d("RingtoneSelector", "createPreferenceControllers " + mType + ", mExistingUri:" + this.mExistingUri);
        if (mType == 4) {
            List<AbstractPreferenceController> list = this.mControllers;
            GlyphsAlarmListController glyphsAlarmListController = new GlyphsAlarmListController(context, "glyphs_alarm_list", this.mExistingUri, new RingtoneSelectorFragment$$ExternalSyntheticLambda0(this));
            this.mAlarmListController = glyphsAlarmListController;
            list.add(glyphsAlarmListController);
            this.mControllers.add(new GlyphsAddAlarmController(context, "glyphs_add_alarm", new RingtoneSelectorFragment$$ExternalSyntheticLambda1(this)));
        } else {
            Context context2 = context;
            this.mControllers.add(new GlyphsCurrentRingtoneController(context2, "glyphs_current_ringtone", getRingtoneType(), this.mExistingUri, this.mSoundOnly, this.mContactName, this.mPageTitle));
            this.mControllers.add(new GlyphsNothingSoundController(context2, "glyphs_nt_sound", getRingtoneType(), this.mExistingUri, this.mSoundOnly, this.mContactName, this.mPageTitle));
            this.mControllers.add(new GlyphsMySoundController(context2, "glyphs_my_sound", getRingtoneType(), this.mExistingUri, this.mSoundOnly, this.mPageTitle));
        }
        return this.mControllers;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createPreferenceControllers$0(boolean z) {
        MenuItem menuItem = this.mSaveMenu;
        if (menuItem != null) {
            menuItem.setEnabled(z);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createPreferenceControllers$1() {
        GlyphsAlarmListController glyphsAlarmListController = this.mAlarmListController;
        if (glyphsAlarmListController != null) {
            glyphsAlarmListController.updateData();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (mType == 4) {
            setHasOptionsMenu(true);
            getActivity().setVolumeControlStream(4);
            return;
        }
        Log.d("RingtoneSelector", "onCreate Ringtone type:" + mType);
        setTitle();
    }

    private void setTitle() {
        String string = getActivity().getString(R$string.nt_glyphs_select_sound_title);
        int i = mType;
        if (i == 1) {
            string = getActivity().getString(R$string.nt_glyphs_select_ringtone_title);
        } else if (i == 4) {
            string = getActivity().getString(R$string.nt_glyphs_alarm_sound_title);
        }
        getActivity().setTitle(string);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        GlyphsAlarmListController glyphsAlarmListController;
        FragmentActivity activity = getActivity();
        if (activity != null && mType == 4) {
            this.mOptionsMenu = menu;
            this.mMenuInflater = menuInflater;
            menuInflater.inflate(R$menu.app_ringtone_selector_save, menu);
            MenuItem findItem = this.mOptionsMenu.findItem(R$id.add_save_menu);
            this.mSaveMenu = findItem;
            if (!(findItem == null || (glyphsAlarmListController = this.mAlarmListController) == null)) {
                findItem.setEnabled(glyphsAlarmListController.isChecked());
            }
            Toolbar toolbar = (Toolbar) activity.findViewById(R$id.action_bar);
            if (toolbar != null) {
                toolbar.setPadding(0, 0, activity.getResources().getDimensionPixelSize(R$dimen.nt_actionbar_right_margin), 0);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R$id.add_save_menu) {
            if (!this.mAlarmListController.isChecked()) {
                return false;
            }
            Uri currentRingtoneUri = this.mAlarmListController.getCurrentRingtoneUri();
            this.mAlarmListController.release();
            ResultPickHelper.setResult(getActivity(), currentRingtoneUri, 1);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private int getRingtoneType() {
        return mType;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        List<AbstractPreferenceController> list = this.mControllers;
        if (list != null) {
            for (AbstractPreferenceController next : list) {
                if (next instanceof BasePreferenceController) {
                    ((BasePreferenceController) next).onActivityControllerResult(i, i2, intent);
                }
            }
        }
    }
}
