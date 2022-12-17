package com.nothing.settings.glyphs.picker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$dimen;
import com.android.settings.R$id;
import com.android.settings.R$menu;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.preference.PrimaryCheckBoxDeletePreference;
import com.nothing.settings.glyphs.preference.PrimaryCheckBoxPreference;
import com.nothing.settings.glyphs.preference.RingtoneSelectorPreference;
import com.nothing.settings.glyphs.utils.ContactsManager;
import com.nothing.settings.glyphs.utils.GlyphsLedHelper;
import com.nothing.settings.glyphs.utils.MusicUtils;
import com.nothing.settings.glyphs.utils.ResultPickHelper;
import com.nothing.settings.glyphs.utils.RingtoneHelper;
import com.nothing.settings.glyphs.utils.UrlUtil;
import com.nothing.settings.utils.NtUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class GlyphsMySoundFragment extends DashboardFragment implements OnActivityResultListener, Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.glyphs_my_sound);
    /* access modifiers changed from: private */
    public Preference mAddNewSound;
    /* access modifiers changed from: private */
    public Ringtone mCurrentRingtone;
    /* access modifiers changed from: private */
    public String mCurrentSelectedKey = null;
    /* access modifiers changed from: private */
    public Uri mDefaultRingtoneUri;
    private Future mFuture;
    private GlyphsLedHelper mGlyphsLedHelper;
    private MenuInflater mMenuInflater;
    private Menu mOptionsMenu;
    private String mPageTitle;
    /* access modifiers changed from: private */
    public PreferenceScreen mPreferenceScreen;
    private RingtoneManager mRingtoneManager;
    private int mRingtoneType = 1;
    /* access modifiers changed from: private */
    public List<RingtoneSelectorPreference.Ringtone> mRingtones;
    /* access modifiers changed from: private */
    public MenuItem mSaveMenu;
    private int mSoundOnly = 0;
    /* access modifiers changed from: private */
    public SwitchPreference mSoundOnlyPreference;

    public int getHelpResource() {
        return 0;
    }

    public String getLogTag() {
        return "MySound";
    }

    public int getMetricsCategory() {
        return 2000;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            this.mOptionsMenu = menu;
            this.mMenuInflater = menuInflater;
            menuInflater.inflate(R$menu.app_ringtone_selector_save, menu);
            MenuItem findItem = this.mOptionsMenu.findItem(R$id.add_save_menu);
            this.mSaveMenu = findItem;
            if (findItem != null) {
                findItem.setEnabled(!TextUtils.isEmpty(this.mCurrentSelectedKey));
            }
            Toolbar toolbar = (Toolbar) activity.findViewById(R$id.action_bar);
            if (toolbar != null) {
                toolbar.setPadding(0, 0, activity.getResources().getDimensionPixelSize(R$dimen.nt_actionbar_right_margin), 0);
            }
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        getActivity().setVolumeControlStream(getRingtoneType());
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R$id.add_save_menu) {
            if (TextUtils.isEmpty(this.mCurrentSelectedKey)) {
                return false;
            }
            Ringtone ringtone = this.mCurrentRingtone;
            if (ringtone != null) {
                this.mDefaultRingtoneUri = ringtone.getUri();
            }
            if (TextUtils.equals(this.mCurrentSelectedKey, "key_glyphs_ringtone_none")) {
                this.mDefaultRingtoneUri = null;
            }
            Log.d("GlyphsMySoundFragment", "mPageTitle:" + this.mPageTitle);
            if (getRingtoneType() == 2 && TextUtils.equals(this.mPageTitle, getContext().getString(R$string.nt_glyphs_default_notifications_title))) {
                Settings.Global.putInt(getActivity().getContentResolver(), "led_notification_mode", this.mSoundOnlyPreference.isChecked() ? 1 : 0);
            }
            ResultPickHelper.setResult(getActivity(), this.mDefaultRingtoneUri, this.mSoundOnlyPreference.isChecked() ? 1 : 0);
            Ringtone ringtone2 = this.mCurrentRingtone;
            if (ringtone2 != null) {
                ringtone2.stop();
                this.mCurrentRingtone = null;
            }
            NtUtils.trackRingtoneChanged(getActivity(), this.mRingtoneType, Ringtone.getTitle(getActivity(), this.mDefaultRingtoneUri, true, false), this.mSoundOnlyPreference.isChecked() ? 1 : 0);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public int getPreferenceScreenResId() {
        return R$xml.glyphs_my_sound;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public void onAttach(Context context) {
        initParams(getIntent());
        super.onAttach(context);
        updateData();
    }

    public void onDestroy() {
        super.onDestroy();
        Future future = this.mFuture;
        if (future != null && !future.isCancelled()) {
            this.mFuture.cancel(true);
        }
    }

    private void initParams(Intent intent) {
        if (intent != null) {
            this.mDefaultRingtoneUri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
            this.mRingtoneType = intent.getIntExtra("android.intent.extra.ringtone.TYPE", 1);
            this.mSoundOnly = intent.getIntExtra("key_sound_only", 0);
            this.mPageTitle = intent.getStringExtra("page_title");
        }
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        this.mAddNewSound = findPreference("glyphs_add_contact_ringtone");
        this.mSoundOnlyPreference = (SwitchPreference) findPreference("glyphs_sound_only");
        this.mPreferenceScreen = getPreferenceScreen();
        this.mAddNewSound.setOnPreferenceClickListener(this);
        this.mSoundOnlyPreference.setOnPreferenceChangeListener(this);
        initSoundOnlySwitch();
    }

    public GlyphsLedHelper getGlyphsLedHelper() {
        if (this.mGlyphsLedHelper == null) {
            this.mGlyphsLedHelper = new GlyphsLedHelper(getActivity(), getRingtoneType());
        }
        return this.mGlyphsLedHelper;
    }

    private void initRingtoneManager() {
        this.mRingtoneManager = new RingtoneManager(getActivity(), true);
        if (getRingtoneType() != -1) {
            this.mRingtoneManager.setType(getRingtoneType());
        }
    }

    public int getRingtoneType() {
        return this.mRingtoneType;
    }

    @SuppressLint({"InlinedApi"})
    private Intent getMediaFilePickerIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("audio/*");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{"audio/*", "application/ogg"});
        return intent;
    }

    private void startSelectSoundActivity() {
        getActivity().startActivityForResult(getMediaFilePickerIntent(), 1000);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1 && i == 1000) {
            saveRingtones(intent);
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        if (preference != this.mAddNewSound) {
            return false;
        }
        startSelectSoundActivity();
        return true;
    }

    /* access modifiers changed from: private */
    public List<RingtoneSelectorPreference.Ringtone> getContactRingtones() {
        RingtoneHelper ringtoneHelper = new RingtoneHelper(getActivity());
        ringtoneHelper.setType(this.mRingtoneType);
        Cursor mediaRingtones = ringtoneHelper.getMediaRingtones();
        ArrayList arrayList = new ArrayList();
        while (mediaRingtones.moveToNext()) {
            arrayList.add(new RingtoneSelectorPreference.Ringtone(mediaRingtones.getString(2) + "/" + mediaRingtones.getString(0) + "?title=" + mediaRingtones.getString(1), mediaRingtones.getString(1), "", mediaRingtones.getString(2) + "/" + mediaRingtones.getString(0), mediaRingtones.getString(0)));
        }
        return arrayList;
    }

    public void updateData() {
        this.mFuture = ThreadUtils.postOnBackgroundThread((Runnable) new Runnable() {
            public void run() {
                GlyphsMySoundFragment glyphsMySoundFragment = GlyphsMySoundFragment.this;
                glyphsMySoundFragment.mRingtones = glyphsMySoundFragment.getContactRingtones();
                String ringtonesId = MusicUtils.getRingtonesId(GlyphsMySoundFragment.this.getContext(), GlyphsMySoundFragment.this.mDefaultRingtoneUri);
                Log.d("GlyphsMySoundFragment", "currentRingtoneId " + ringtonesId);
                ThreadUtils.postOnMainThread(new UpdateDataTask(ringtonesId));
            }
        });
    }

    public class UpdateDataTask implements Runnable {
        final String currentRingtoneId;

        UpdateDataTask(String str) {
            this.currentRingtoneId = str;
        }

        public void run() {
            FragmentActivity activity = GlyphsMySoundFragment.this.getActivity();
            if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                GlyphsMySoundFragment.this.mRingtones.add(0, new RingtoneSelectorPreference.Ringtone((String) null, (String) null));
                GlyphsMySoundFragment.this.mPreferenceScreen.removeAll();
                GlyphsMySoundFragment.this.mPreferenceScreen.addPreference(GlyphsMySoundFragment.this.mSoundOnlyPreference);
                GlyphsMySoundFragment.this.mPreferenceScreen.addPreference(GlyphsMySoundFragment.this.mAddNewSound);
                Log.d("GlyphsMySoundFragment", "mDefaultRingtoneUri " + GlyphsMySoundFragment.this.mDefaultRingtoneUri);
                if (GlyphsMySoundFragment.this.mDefaultRingtoneUri == null) {
                    GlyphsMySoundFragment.this.mCurrentSelectedKey = "key_glyphs_ringtone_none";
                }
                for (int i = 0; i < GlyphsMySoundFragment.this.mRingtones.size(); i++) {
                    if (i == 0) {
                        PrimaryCheckBoxPreference primaryCheckBoxPreference = new PrimaryCheckBoxPreference(GlyphsMySoundFragment.this.getActivity());
                        primaryCheckBoxPreference.setKey("key_glyphs_ringtone_none");
                        if (TextUtils.isEmpty(((RingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getUri())) {
                            primaryCheckBoxPreference.setTitle((CharSequence) GlyphsMySoundFragment.this.getActivity().getString(17041421));
                        } else {
                            primaryCheckBoxPreference.setTitle((CharSequence) ((RingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getTitle());
                        }
                        GlyphsMySoundFragment.this.mPreferenceScreen.addPreference(primaryCheckBoxPreference);
                        primaryCheckBoxPreference.setOnSelectedListener(new GlyphsMySoundFragment$UpdateDataTask$$ExternalSyntheticLambda0(this));
                    } else {
                        PrimaryCheckBoxDeletePreference primaryCheckBoxDeletePreference = new PrimaryCheckBoxDeletePreference(GlyphsMySoundFragment.this.getActivity());
                        primaryCheckBoxDeletePreference.setKey("key_glyphs_ringtone_" + ((RingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getId());
                        if (TextUtils.isEmpty(((RingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getUri())) {
                            primaryCheckBoxDeletePreference.setTitle((CharSequence) GlyphsMySoundFragment.this.getActivity().getString(17041421));
                            primaryCheckBoxDeletePreference.deleteEnable(false);
                        } else {
                            if (TextUtils.equals(this.currentRingtoneId, ((RingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getId())) {
                                GlyphsMySoundFragment.this.mCurrentSelectedKey = primaryCheckBoxDeletePreference.getKey();
                            }
                            primaryCheckBoxDeletePreference.setTitle((CharSequence) ((RingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getTitle());
                        }
                        GlyphsMySoundFragment.this.mPreferenceScreen.addPreference(primaryCheckBoxDeletePreference);
                        primaryCheckBoxDeletePreference.setOnSelectedListener(new CheckBoxDeleteSelectedCallback());
                    }
                }
                GlyphsMySoundFragment glyphsMySoundFragment = GlyphsMySoundFragment.this;
                glyphsMySoundFragment.setPreferenceSwitch(glyphsMySoundFragment.mCurrentSelectedKey, true);
                if (GlyphsMySoundFragment.this.mSaveMenu != null) {
                    GlyphsMySoundFragment.this.mSaveMenu.setEnabled(!TextUtils.isEmpty(GlyphsMySoundFragment.this.mCurrentSelectedKey));
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(Preference preference, boolean z) {
            if (z) {
                if (GlyphsMySoundFragment.this.mSaveMenu != null) {
                    GlyphsMySoundFragment.this.mSaveMenu.setEnabled(true);
                }
                GlyphsMySoundFragment glyphsMySoundFragment = GlyphsMySoundFragment.this;
                glyphsMySoundFragment.setPreferenceSwitch(glyphsMySoundFragment.mCurrentSelectedKey, false);
                if (GlyphsMySoundFragment.this.mCurrentRingtone != null) {
                    GlyphsMySoundFragment.this.mCurrentRingtone.stop();
                    GlyphsMySoundFragment.this.getGlyphsLedHelper().stop();
                    Log.d("GlyphsMySoundFragment", "mCurrentRingtone stop " + GlyphsMySoundFragment.this.mCurrentRingtone);
                }
                GlyphsMySoundFragment glyphsMySoundFragment2 = GlyphsMySoundFragment.this;
                String key = preference.getKey();
                glyphsMySoundFragment2.mCurrentSelectedKey = key;
                glyphsMySoundFragment2.setPreferenceSwitch(key, true);
                GlyphsMySoundFragment.this.mCurrentRingtone = null;
                GlyphsMySoundFragment.this.mDefaultRingtoneUri = null;
            }
        }

        class CheckBoxDeleteSelectedCallback implements PrimaryCheckBoxDeletePreference.OnSelectedListener {
            CheckBoxDeleteSelectedCallback() {
            }

            public void onClickDelete(Preference preference) {
                ThreadUtils.postOnBackgroundThread((Runnable) new C2029xc616c731(this, preference));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onClickDelete$1(Preference preference) {
                RingtoneSelectorPreference.Ringtone ringtone = GlyphsMySoundFragment.this.getRingtone(preference.getKey());
                if (ringtone != null) {
                    Uri parse = Uri.parse(ringtone.getUri());
                    String ringtonesPath = MusicUtils.getRingtonesPath(GlyphsMySoundFragment.this.getActivity(), parse);
                    String id = ringtone.getId();
                    Log.d("GlyphsMySoundFragment", "parse:" + parse + ", ringtonesPath:" + ringtonesPath + ", id:" + id);
                    ThreadUtils.postOnMainThread(new C2030xc616c732(this, preference, parse, ringtonesPath, id));
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onClickDelete$0(Preference preference, Uri uri, String str, String str2) {
                GlyphsMySoundFragment.this.showDeleteDialog(preference, uri, str, str2);
            }

            public void onCheckChange(Preference preference, boolean z) {
                if (z) {
                    GlyphsMySoundFragment glyphsMySoundFragment = GlyphsMySoundFragment.this;
                    glyphsMySoundFragment.setPreferenceSwitch(glyphsMySoundFragment.mCurrentSelectedKey, false);
                    if (GlyphsMySoundFragment.this.mCurrentRingtone != null) {
                        GlyphsMySoundFragment.this.mCurrentRingtone.stop();
                        GlyphsMySoundFragment.this.getGlyphsLedHelper().stop();
                        Log.d("GlyphsMySoundFragment", "mCurrentRingtone stop " + GlyphsMySoundFragment.this.mCurrentRingtone);
                    }
                    if (GlyphsMySoundFragment.this.mSaveMenu != null) {
                        GlyphsMySoundFragment.this.mSaveMenu.setEnabled(true);
                    }
                    RingtoneSelectorPreference.Ringtone ringtone = GlyphsMySoundFragment.this.getRingtone(preference.getKey());
                    if (ringtone == null || TextUtils.isEmpty(ringtone.getUri())) {
                        GlyphsMySoundFragment.this.mCurrentRingtone = null;
                        Log.d("GlyphsMySoundFragment", "mCurrentRingtone " + GlyphsMySoundFragment.this.mCurrentRingtone);
                        return;
                    }
                    GlyphsMySoundFragment.this.mCurrentSelectedKey = preference.getKey();
                    GlyphsMySoundFragment glyphsMySoundFragment2 = GlyphsMySoundFragment.this;
                    glyphsMySoundFragment2.setPreferenceSwitch(glyphsMySoundFragment2.mCurrentSelectedKey, true);
                    GlyphsMySoundFragment.this.mDefaultRingtoneUri = Uri.parse(ringtone.getUri());
                    GlyphsMySoundFragment glyphsMySoundFragment3 = GlyphsMySoundFragment.this;
                    glyphsMySoundFragment3.mCurrentRingtone = RingtoneManager.getRingtone(glyphsMySoundFragment3.getActivity(), GlyphsMySoundFragment.this.mDefaultRingtoneUri);
                    Log.d("GlyphsMySoundFragment", "select mCurrentRingtone " + GlyphsMySoundFragment.this.mCurrentRingtone);
                    GlyphsMySoundFragment glyphsMySoundFragment4 = GlyphsMySoundFragment.this;
                    glyphsMySoundFragment4.playRingtoneAndLed(glyphsMySoundFragment4.mCurrentRingtone);
                }
            }
        }
    }

    public void setPreferenceSwitch(String str, boolean z) {
        if (str != null) {
            Log.d("GlyphsMySoundFragment", "setPreferenceSwitch " + str + " checked " + z);
            Preference findPreference = getPreferenceScreen().findPreference(str);
            if (findPreference == null) {
                return;
            }
            if (findPreference instanceof PrimaryCheckBoxPreference) {
                ((PrimaryCheckBoxPreference) findPreference).setChecked(z);
            } else if (findPreference instanceof PrimaryCheckBoxDeletePreference) {
                ((PrimaryCheckBoxDeletePreference) findPreference).setChecked(z);
            }
        }
    }

    private void saveRingtones(Intent intent) {
        if (this.mRingtoneManager == null) {
            initRingtoneManager();
        }
        ThreadUtils.postOnBackgroundThread((Runnable) new GlyphsMySoundFragment$$ExternalSyntheticLambda0(this, intent));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$saveRingtones$0(Intent intent) {
        Uri uri;
        if (intent.getData() != null) {
            try {
                uri = this.mRingtoneManager.addCustomExternalRingtone(intent.getData(), getRingtoneType());
            } catch (IOException e) {
                e.printStackTrace();
                uri = null;
            }
            if (uri != null) {
                updateData();
                return;
            }
            return;
        }
        ClipData clipData = intent.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                try {
                    this.mRingtoneManager.addCustomExternalRingtone(clipData.getItemAt(i).getUri(), getRingtoneType());
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            updateData();
        }
    }

    public void deleteRingtoneFile(String str) {
        if (!TextUtils.isEmpty(str)) {
            File file = new File("file:///" + str);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

    public void showDeleteDialog(Preference preference, Uri uri, String str, String str2) {
        onCreateDialog(preference, uri, str, str2).show();
    }

    public Dialog onCreateDialog(Preference preference, Uri uri, String str, String str2) {
        return new AlertDialog.Builder(getActivity()).setTitle(R$string.nt_glyphs_remove_custom_sound_title).setMessage(getActivity().getString(R$string.nt_glyphs_remove_custom_sound_message)).setNegativeButton(R$string.nt_glyphs_cancel, new GlyphsMySoundFragment$$ExternalSyntheticLambda1()).setPositiveButton(R$string.nt_glyphs_remove, new GlyphsMySoundFragment$$ExternalSyntheticLambda2(this, uri, preference, str)).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$3(Uri uri, Preference preference, String str, DialogInterface dialogInterface, int i) {
        Ringtone ringtone = this.mCurrentRingtone;
        if (ringtone != null && TextUtils.equals(ringtone.getUri().getPath(), uri.getPath())) {
            this.mCurrentRingtone.stop();
        }
        PreferenceScreen preferenceScreen = this.mPreferenceScreen;
        if (preferenceScreen != null) {
            preferenceScreen.removePreferenceRecursively(preference.getKey());
        }
        if (TextUtils.equals(this.mCurrentSelectedKey, preference.getKey())) {
            setPreferenceSwitch(this.mCurrentSelectedKey, false);
            this.mCurrentSelectedKey = null;
        }
        MenuItem menuItem = this.mSaveMenu;
        if (menuItem != null) {
            menuItem.setEnabled(!TextUtils.isEmpty(this.mCurrentSelectedKey));
        }
        ThreadUtils.postOnBackgroundThread((Runnable) new GlyphsMySoundFragment$$ExternalSyntheticLambda3(this, str, uri));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$2(String str, Uri uri) {
        deleteRingtoneFile(str);
        MusicUtils.deleteRingtone(getActivity(), uri);
        if (getRingtoneType() == 1) {
            ContactsManager.getInstance().updateAllContactRingtone(getActivity().getApplication(), UrlUtil.getUrlHost(uri.toString()));
        }
        String uri2 = uri != null ? uri.toString() : null;
        Uri actualDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getContext(), getRingtoneType());
        String uri3 = actualDefaultRingtoneUri != null ? actualDefaultRingtoneUri.toString() : null;
        Log.d("GlyphsMySoundFragment", "uriString:" + uri2 + ", defaultUriString:" + uri3);
        if (!TextUtils.isEmpty(uri3) && uri3.contains(uri2)) {
            RingtoneManager.setActualDefaultRingtoneUri(getContext(), getRingtoneType(), (Uri) null);
        }
    }

    public RingtoneSelectorPreference.Ringtone getRingtone(String str) {
        List<RingtoneSelectorPreference.Ringtone> list = this.mRingtones;
        if (list == null) {
            return null;
        }
        for (RingtoneSelectorPreference.Ringtone next : list) {
            if (TextUtils.equals(str, "key_glyphs_ringtone_" + next.getId())) {
                return next;
            }
        }
        return null;
    }

    public void initSoundOnlySwitch() {
        SwitchPreference switchPreference = this.mSoundOnlyPreference;
        boolean z = true;
        if (this.mSoundOnly != 1) {
            z = false;
        }
        switchPreference.setChecked(z);
    }

    public void onDetach() {
        super.onDetach();
        Ringtone ringtone = this.mCurrentRingtone;
        if (ringtone != null) {
            ringtone.stop();
        }
    }

    private boolean isSoundMute() {
        boolean z = true;
        if (this.mRingtoneType != 1 ? Settings.System.getInt(getContext().getContentResolver(), "notification_vibration_intensity", 0) != 0 : Settings.System.getInt(getContext().getContentResolver(), "vibrate_when_ringing", 0) != 0) {
            z = false;
        }
        Log.d("GlyphsMySoundFragment", "isSoundMute:" + z + ", mRingtoneType:" + this.mRingtoneType);
        return z;
    }

    public void playRingtoneAndLed(Ringtone ringtone) {
        Log.d("GlyphsMySoundFragment", "playRingtoneAndLed " + ringtone);
        if (ringtone != null) {
            ringtone.setAudioAttributes(new AudioAttributes.Builder().setInternalLegacyStreamType(this.mRingtoneType == 1 ? 2 : 5).setHapticChannelsMuted(isSoundMute()).build());
            getGlyphsLedHelper().stop();
            getGlyphsLedHelper().setSoundOnly(this.mSoundOnlyPreference.isChecked());
            getGlyphsLedHelper().play(ringtone.getUri(), ringtone.getTitle(getActivity()));
            ringtone.play();
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        SwitchPreference switchPreference = this.mSoundOnlyPreference;
        if (preference != switchPreference) {
            return false;
        }
        switchPreference.setChecked(((Boolean) obj).booleanValue());
        if (this.mCurrentRingtone == null) {
            return false;
        }
        Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), this.mCurrentRingtone.getUri());
        if (this.mCurrentRingtone.isPlaying()) {
            this.mCurrentRingtone.stop();
        }
        this.mCurrentRingtone = ringtone;
        playRingtoneAndLed(ringtone);
        return false;
    }

    public void onPause() {
        super.onPause();
        Ringtone ringtone = this.mCurrentRingtone;
        if (ringtone != null) {
            ringtone.stop();
        }
        getGlyphsLedHelper().stop();
    }
}
