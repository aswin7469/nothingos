package com.nt.settings.glyphs.pick;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import com.android.settings.R;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.glyphs.utils.ContactsManager;
import com.nt.settings.glyphs.utils.GlyphsLedHelper;
import com.nt.settings.glyphs.utils.MusicUtils;
import com.nt.settings.glyphs.utils.RingtoneHelper;
import com.nt.settings.glyphs.utils.UrlUtil;
import com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference;
import com.nt.settings.glyphs.widget.PrimaryCheckBoxDeletePreference;
import com.nt.settings.glyphs.widget.PrimaryCheckBoxPreference;
import com.nt.settings.utils.NtUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
/* loaded from: classes2.dex */
public class GlyphsMySoundFragment extends DashboardFragment implements OnActivityResultListener, Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_glyphs_my_sound);
    private Preference mAddNewSound;
    private Ringtone mCurrentRingtone;
    private Uri mDefaultRingtoneUri;
    private Future mFuture;
    private GlyphsLedHelper mGlyphsLedHelper;
    private MenuInflater mMenuInflater;
    private Menu mOptionsMenu;
    private PreferenceScreen mPreferenceScreen;
    private RingtoneManager mRingtoneManager;
    private List<GlyphsRingtoneSelectorPreference.Ringtone> mRingtones;
    private MenuItem mSaveMenu;
    private SwitchPreference mSoundOnlyPreference;
    private String mCurrentSelectedKey = null;
    private int mRingtoneType = 1;
    private int mSoundOnly = 0;

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "MySound";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 2000;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        this.mOptionsMenu = menu;
        this.mMenuInflater = menuInflater;
        menuInflater.inflate(R.menu.app_ringtone_selector_save, menu);
        MenuItem findItem = this.mOptionsMenu.findItem(R.id.add_save_menu);
        this.mSaveMenu = findItem;
        if (findItem != null) {
            findItem.setEnabled(!TextUtils.isEmpty(this.mCurrentSelectedKey));
        }
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.action_bar);
        if (toolbar == null) {
            return;
        }
        toolbar.setPadding(0, 0, activity.getResources().getDimensionPixelSize(R.dimen.nt_actionbar_right_margin), 0);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_save_menu) {
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
            if (getRingtoneType() == 2) {
                Settings.Global.putInt(getActivity().getContentResolver(), "led_notification_mode", this.mSoundOnlyPreference.isChecked() ? 1 : 0);
            }
            GlyphsPickResultHelper.setResult(getActivity(), this.mDefaultRingtoneUri, this.mSoundOnlyPreference.isChecked() ? 1 : 0);
            Ringtone ringtone2 = this.mCurrentRingtone;
            if (ringtone2 != null) {
                ringtone2.stop();
                this.mCurrentRingtone = null;
            }
            NtUtils.trackRingtoneChanged(getActivity(), this.mRingtoneType, Ringtone.getTitle(getActivity(), this.mDefaultRingtoneUri, true, false), this.mSoundOnlyPreference.isChecked() ? 1 : 0);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_glyphs_my_sound;
    }

    @Override // com.android.settings.SettingsPreferenceFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override // com.android.settings.SettingsPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        initParams(getIntent());
        super.onAttach(context);
        updateData();
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        Future future = this.mFuture;
        if (future == null || future.isCancelled()) {
            return;
        }
        this.mFuture.cancel(true);
    }

    private void initParams(Intent intent) {
        if (intent == null) {
            return;
        }
        this.mDefaultRingtoneUri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
        this.mRingtoneType = intent.getIntExtra("android.intent.extra.ringtone.TYPE", 1);
        this.mSoundOnly = intent.getIntExtra("key_sound_only", 0);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        this.mAddNewSound = findPreference("glyphs_add_contact_ringtone");
        this.mSoundOnlyPreference = (SwitchPreference) findPreference("glyphs_sound_only");
        this.mPreferenceScreen = getPreferenceScreen();
        this.mAddNewSound.setOnPreferenceClickListener(this);
        this.mSoundOnlyPreference.setOnPreferenceChangeListener(this);
        initSoundOnlySwitch();
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
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

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1 && i == 1000) {
            saveRingtones(intent);
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        if (preference == this.mAddNewSound) {
            startSelectSoundActivity();
            return true;
        }
        return false;
    }

    private List<GlyphsRingtoneSelectorPreference.Ringtone> getContactRingtones() {
        RingtoneHelper ringtoneHelper = new RingtoneHelper(getActivity());
        ringtoneHelper.setType(this.mRingtoneType);
        Cursor mediaRingtones = ringtoneHelper.getMediaRingtones();
        ArrayList arrayList = new ArrayList();
        while (mediaRingtones.moveToNext()) {
            arrayList.add(new GlyphsRingtoneSelectorPreference.Ringtone(mediaRingtones.getString(2) + "/" + mediaRingtones.getString(0) + "?title=" + mediaRingtones.getString(1), mediaRingtones.getString(1), "", mediaRingtones.getString(2) + "/" + mediaRingtones.getString(0), mediaRingtones.getString(0)));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateData() {
        this.mFuture = ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsMySoundFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsMySoundFragment.this.lambda$updateData$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateData$0() {
        this.mRingtones = getContactRingtones();
        String ringtonesId = MusicUtils.getRingtonesId(getContext(), this.mDefaultRingtoneUri);
        Log.d("MySound", "currentRingtoneId " + ringtonesId);
        ThreadUtils.postOnMainThread(new AnonymousClass1(ringtonesId));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.nt.settings.glyphs.pick.GlyphsMySoundFragment$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ String val$currentRingtoneId;

        AnonymousClass1(String str) {
            this.val$currentRingtoneId = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentActivity activity = GlyphsMySoundFragment.this.getActivity();
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            GlyphsMySoundFragment.this.mRingtones.add(0, new GlyphsRingtoneSelectorPreference.Ringtone(null, null));
            GlyphsMySoundFragment.this.mPreferenceScreen.removeAll();
            GlyphsMySoundFragment.this.mPreferenceScreen.addPreference(GlyphsMySoundFragment.this.mSoundOnlyPreference);
            GlyphsMySoundFragment.this.mPreferenceScreen.addPreference(GlyphsMySoundFragment.this.mAddNewSound);
            Log.d("MySound", "mDefaultRingtoneUri " + GlyphsMySoundFragment.this.mDefaultRingtoneUri);
            if (GlyphsMySoundFragment.this.mDefaultRingtoneUri == null) {
                GlyphsMySoundFragment.this.mCurrentSelectedKey = "key_glyphs_ringtone_none";
            }
            for (int i = 0; i < GlyphsMySoundFragment.this.mRingtones.size(); i++) {
                Log.d("MySound", ((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getUri() + "");
                if (i == 0) {
                    PrimaryCheckBoxPreference primaryCheckBoxPreference = new PrimaryCheckBoxPreference(GlyphsMySoundFragment.this.getActivity());
                    primaryCheckBoxPreference.setKey("key_glyphs_ringtone_none");
                    if (!TextUtils.isEmpty(((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getUri())) {
                        primaryCheckBoxPreference.setTitle(((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getTitle());
                    } else {
                        primaryCheckBoxPreference.setTitle(GlyphsMySoundFragment.this.getActivity().getString(17041302));
                    }
                    GlyphsMySoundFragment.this.mPreferenceScreen.addPreference(primaryCheckBoxPreference);
                    primaryCheckBoxPreference.setOnSelectedListener(new PrimaryCheckBoxPreference.OnSelectedListener() { // from class: com.nt.settings.glyphs.pick.GlyphsMySoundFragment.1.1
                        @Override // com.nt.settings.glyphs.widget.PrimaryCheckBoxPreference.OnSelectedListener
                        public void onCheckChange(Preference preference, boolean z) {
                            if (!z) {
                                return;
                            }
                            if (GlyphsMySoundFragment.this.mSaveMenu != null) {
                                GlyphsMySoundFragment.this.mSaveMenu.setEnabled(true);
                            }
                            GlyphsMySoundFragment glyphsMySoundFragment = GlyphsMySoundFragment.this;
                            glyphsMySoundFragment.setPreferenceSwitch(glyphsMySoundFragment.mCurrentSelectedKey, false);
                            if (GlyphsMySoundFragment.this.mCurrentRingtone != null) {
                                GlyphsMySoundFragment.this.mCurrentRingtone.stop();
                                GlyphsMySoundFragment.this.getGlyphsLedHelper().stop();
                                Log.d("MySound", "mCurrentRingtone stop " + GlyphsMySoundFragment.this.mCurrentRingtone);
                            }
                            GlyphsMySoundFragment glyphsMySoundFragment2 = GlyphsMySoundFragment.this;
                            glyphsMySoundFragment2.setPreferenceSwitch(glyphsMySoundFragment2.mCurrentSelectedKey = preference.getKey(), true);
                            GlyphsMySoundFragment.this.mCurrentRingtone = null;
                            GlyphsMySoundFragment.this.mDefaultRingtoneUri = null;
                        }
                    });
                } else {
                    PrimaryCheckBoxDeletePreference primaryCheckBoxDeletePreference = new PrimaryCheckBoxDeletePreference(GlyphsMySoundFragment.this.getActivity());
                    primaryCheckBoxDeletePreference.setKey("key_glyphs_ringtone_" + ((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getId());
                    if (!TextUtils.isEmpty(((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getUri())) {
                        Log.d("MySound", "currentRingtoneId " + this.val$currentRingtoneId + "   " + ((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getPath());
                        if (TextUtils.equals(this.val$currentRingtoneId, ((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getId())) {
                            GlyphsMySoundFragment.this.mCurrentSelectedKey = primaryCheckBoxDeletePreference.getKey();
                        }
                        primaryCheckBoxDeletePreference.setTitle(((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsMySoundFragment.this.mRingtones.get(i)).getTitle());
                    } else {
                        primaryCheckBoxDeletePreference.setTitle(GlyphsMySoundFragment.this.getActivity().getString(17041302));
                        primaryCheckBoxDeletePreference.deleteEnable(false);
                    }
                    GlyphsMySoundFragment.this.mPreferenceScreen.addPreference(primaryCheckBoxDeletePreference);
                    primaryCheckBoxDeletePreference.setOnSelectedListener(new AnonymousClass2());
                }
            }
            GlyphsMySoundFragment glyphsMySoundFragment = GlyphsMySoundFragment.this;
            glyphsMySoundFragment.setPreferenceSwitch(glyphsMySoundFragment.mCurrentSelectedKey, true);
            if (GlyphsMySoundFragment.this.mSaveMenu == null) {
                return;
            }
            GlyphsMySoundFragment.this.mSaveMenu.setEnabled(!TextUtils.isEmpty(GlyphsMySoundFragment.this.mCurrentSelectedKey));
        }

        /* renamed from: com.nt.settings.glyphs.pick.GlyphsMySoundFragment$1$2  reason: invalid class name */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements PrimaryCheckBoxDeletePreference.OnSelectedListener {
            AnonymousClass2() {
            }

            @Override // com.nt.settings.glyphs.widget.PrimaryCheckBoxDeletePreference.OnSelectedListener
            public void onClickDelete(final Preference preference) {
                ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsMySoundFragment.1.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        GlyphsRingtoneSelectorPreference.Ringtone ringtone = GlyphsMySoundFragment.this.getRingtone(preference.getKey());
                        if (ringtone == null) {
                            return;
                        }
                        final Uri parse = Uri.parse(ringtone.getUri());
                        final String ringtonesPath = MusicUtils.getRingtonesPath(GlyphsMySoundFragment.this.getActivity(), parse);
                        final String id = ringtone.getId();
                        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsMySoundFragment.1.2.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                RunnableC00161 runnableC00161 = RunnableC00161.this;
                                GlyphsMySoundFragment.this.showDeleteDialog(preference, parse, ringtonesPath, id);
                            }
                        });
                    }
                });
            }

            @Override // com.nt.settings.glyphs.widget.PrimaryCheckBoxDeletePreference.OnSelectedListener
            public void onCheckChange(Preference preference, boolean z) {
                if (!z) {
                    return;
                }
                GlyphsMySoundFragment glyphsMySoundFragment = GlyphsMySoundFragment.this;
                glyphsMySoundFragment.setPreferenceSwitch(glyphsMySoundFragment.mCurrentSelectedKey, false);
                if (GlyphsMySoundFragment.this.mCurrentRingtone != null) {
                    GlyphsMySoundFragment.this.mCurrentRingtone.stop();
                    GlyphsMySoundFragment.this.getGlyphsLedHelper().stop();
                    Log.d("MySound", "mCurrentRingtone stop " + GlyphsMySoundFragment.this.mCurrentRingtone);
                }
                if (GlyphsMySoundFragment.this.mSaveMenu != null) {
                    GlyphsMySoundFragment.this.mSaveMenu.setEnabled(true);
                }
                GlyphsRingtoneSelectorPreference.Ringtone ringtone = GlyphsMySoundFragment.this.getRingtone(preference.getKey());
                if (ringtone == null || TextUtils.isEmpty(ringtone.getUri())) {
                    GlyphsMySoundFragment.this.mCurrentRingtone = null;
                    Log.d("MySound", "mCurrentRingtone " + GlyphsMySoundFragment.this.mCurrentRingtone);
                    return;
                }
                GlyphsMySoundFragment.this.mCurrentSelectedKey = preference.getKey();
                GlyphsMySoundFragment glyphsMySoundFragment2 = GlyphsMySoundFragment.this;
                glyphsMySoundFragment2.setPreferenceSwitch(glyphsMySoundFragment2.mCurrentSelectedKey, true);
                GlyphsMySoundFragment.this.mDefaultRingtoneUri = Uri.parse(ringtone.getUri());
                GlyphsMySoundFragment glyphsMySoundFragment3 = GlyphsMySoundFragment.this;
                glyphsMySoundFragment3.mCurrentRingtone = RingtoneManager.getRingtone(glyphsMySoundFragment3.getActivity(), GlyphsMySoundFragment.this.mDefaultRingtoneUri);
                Log.d("MySound", "select mCurrentRingtone " + GlyphsMySoundFragment.this.mCurrentRingtone);
                GlyphsMySoundFragment glyphsMySoundFragment4 = GlyphsMySoundFragment.this;
                glyphsMySoundFragment4.playRingtoneAndLed(glyphsMySoundFragment4.mCurrentRingtone);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPreferenceSwitch(String str, boolean z) {
        if (str == null) {
            return;
        }
        Log.d("MySound", "setPreferenceSwitch " + str + " checked " + z);
        Preference findPreference = getPreferenceScreen().findPreference(str);
        if (findPreference == null) {
            return;
        }
        if (findPreference instanceof PrimaryCheckBoxPreference) {
            ((PrimaryCheckBoxPreference) findPreference).setChecked(z);
        } else if (!(findPreference instanceof PrimaryCheckBoxDeletePreference)) {
        } else {
            ((PrimaryCheckBoxDeletePreference) findPreference).setChecked(z);
        }
    }

    private void saveRingtones(final Intent intent) {
        if (this.mRingtoneManager == null) {
            initRingtoneManager();
        }
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsMySoundFragment.2
            @Override // java.lang.Runnable
            public void run() {
                ClipData.Item itemAt;
                Uri uri;
                Uri uri2 = null;
                if (intent.getData() != null) {
                    try {
                        uri2 = GlyphsMySoundFragment.this.mRingtoneManager.addCustomExternalRingtone(intent.getData(), GlyphsMySoundFragment.this.getRingtoneType());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (uri2 == null) {
                        return;
                    }
                    GlyphsMySoundFragment.this.updateData();
                    return;
                }
                ClipData clipData = intent.getClipData();
                if (clipData == null) {
                    return;
                }
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    try {
                        uri = GlyphsMySoundFragment.this.mRingtoneManager.addCustomExternalRingtone(clipData.getItemAt(i).getUri(), GlyphsMySoundFragment.this.getRingtoneType());
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        uri = null;
                    }
                    if (uri == null) {
                        Log.d("MySound", "FAIL Uri " + itemAt.getUri());
                    }
                }
                GlyphsMySoundFragment.this.updateData();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteRingtoneFile(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        File file = new File("file:///" + str);
        if (!file.exists() || !file.isFile()) {
            return;
        }
        file.delete();
    }

    public void showDeleteDialog(Preference preference, Uri uri, String str, String str2) {
        onCreateDialog(preference, uri, str, str2).show();
    }

    public Dialog onCreateDialog(final Preference preference, final Uri uri, final String str, String str2) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.nt_glyphs_remove_custom_sound_title).setMessage(getActivity().getString(R.string.nt_glyphs_remove_custom_sound_message)).setNegativeButton(R.string.nt_glyphs_cancel, new DialogInterface.OnClickListener() { // from class: com.nt.settings.glyphs.pick.GlyphsMySoundFragment.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton(R.string.nt_glyphs_remove, new DialogInterface.OnClickListener() { // from class: com.nt.settings.glyphs.pick.GlyphsMySoundFragment.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (GlyphsMySoundFragment.this.mCurrentRingtone != null && TextUtils.equals(GlyphsMySoundFragment.this.mCurrentRingtone.getUri().getPath(), uri.getPath())) {
                    GlyphsMySoundFragment.this.mCurrentRingtone.stop();
                }
                boolean removePreferenceRecursively = GlyphsMySoundFragment.this.mPreferenceScreen != null ? GlyphsMySoundFragment.this.mPreferenceScreen.removePreferenceRecursively(preference.getKey()) : false;
                if (TextUtils.equals(GlyphsMySoundFragment.this.mCurrentSelectedKey, preference.getKey())) {
                    GlyphsMySoundFragment glyphsMySoundFragment = GlyphsMySoundFragment.this;
                    glyphsMySoundFragment.setPreferenceSwitch(glyphsMySoundFragment.mCurrentSelectedKey, false);
                    GlyphsMySoundFragment.this.mCurrentSelectedKey = null;
                }
                if (GlyphsMySoundFragment.this.mSaveMenu != null) {
                    GlyphsMySoundFragment.this.mSaveMenu.setEnabled(!TextUtils.isEmpty(GlyphsMySoundFragment.this.mCurrentSelectedKey));
                }
                ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsMySoundFragment.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AnonymousClass3 anonymousClass3 = AnonymousClass3.this;
                        GlyphsMySoundFragment.this.deleteRingtoneFile(str);
                        MusicUtils.deleteRingtone(GlyphsMySoundFragment.this.getActivity(), uri);
                        if (GlyphsMySoundFragment.this.getRingtoneType() == 1) {
                            ContactsManager.getInstance().updateAllContactRingtone(GlyphsMySoundFragment.this.getActivity().getApplication(), UrlUtil.getUrlHost(uri.toString()));
                        }
                    }
                });
                Log.d("MySound", "result " + removePreferenceRecursively + " key " + preference.getKey());
            }
        }).create();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public GlyphsRingtoneSelectorPreference.Ringtone getRingtone(String str) {
        List<GlyphsRingtoneSelectorPreference.Ringtone> list = this.mRingtones;
        if (list == null) {
            return null;
        }
        for (GlyphsRingtoneSelectorPreference.Ringtone ringtone : list) {
            if (TextUtils.equals(str, "key_glyphs_ringtone_" + ringtone.getId())) {
                return ringtone;
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

    @Override // com.android.settings.SettingsPreferenceFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        Ringtone ringtone = this.mCurrentRingtone;
        if (ringtone != null) {
            ringtone.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playRingtoneAndLed(Ringtone ringtone) {
        Log.d("MySound", "playRingtoneAndLed " + ringtone);
        if (ringtone == null) {
            return;
        }
        ringtone.play();
        getGlyphsLedHelper().stop();
        getGlyphsLedHelper().setSoundOnly(this.mSoundOnlyPreference.isChecked());
        getGlyphsLedHelper().play(ringtone.getUri(), ringtone.getTitle(getActivity()));
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        SwitchPreference switchPreference = this.mSoundOnlyPreference;
        if (preference == switchPreference) {
            switchPreference.setChecked(((Boolean) obj).booleanValue());
            Ringtone ringtone = this.mCurrentRingtone;
            if (ringtone == null) {
                return false;
            }
            playRingtoneAndLed(ringtone);
            return false;
        }
        return false;
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        Ringtone ringtone = this.mCurrentRingtone;
        if (ringtone != null) {
            ringtone.stop();
        }
        getGlyphsLedHelper().stop();
    }
}
