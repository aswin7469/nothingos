package com.nt.settings.glyphs.pick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.glyphs.utils.MusicUtils;
import com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference;
import com.nt.settings.glyphs.widget.PrimaryCheckBoxDeletePreference;
import com.nt.settings.glyphs.widget.PrimaryCheckBoxPreference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
/* loaded from: classes2.dex */
public class GlyphsAlarmListController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final String KEY_GLYPHS_ADD_ALARM = "glyphs_add_alarm";
    private static final String KEY_ITEM_PREFIX = "key_glyphs_ringtone_";
    private static final int SELECT_MY_SOUND_REQUEST_CODE = 1003;
    private static final String TAG = "AlarmListController";
    private Preference mAddAlarmPreference;
    private String mCurrentKey;
    private Ringtone mCurrentRingtone;
    private Future mFuture;
    private OnChangeListener mListener;
    private PreferenceScreen mPreferenceScreen;
    private List<GlyphsRingtoneSelectorPreference.Ringtone> mRingtones;
    private Uri mUriForDefaultItem;

    /* loaded from: classes2.dex */
    public interface OnChangeListener {
        void onChecked(boolean z);
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        return true;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsAlarmListController(Context context, String str, Uri uri, OnChangeListener onChangeListener) {
        super(context, str);
        this.mUriForDefaultItem = uri;
        this.mListener = onChangeListener;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceScreen = preferenceScreen;
        this.mAddAlarmPreference = preferenceScreen.findPreference(KEY_GLYPHS_ADD_ALARM);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        updateData();
    }

    private List<GlyphsRingtoneSelectorPreference.Ringtone> getContactRingtones() {
        RingtoneManager ringtoneManager = new RingtoneManager(this.mContext);
        ringtoneManager.setType(4);
        Cursor cursor = ringtoneManager.getCursor();
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            arrayList.add(new GlyphsRingtoneSelectorPreference.Ringtone(cursor.getString(2) + "/" + cursor.getString(0) + "?title=" + cursor.getString(1), cursor.getString(1), "", cursor.getString(2) + "/" + cursor.getString(0), cursor.getString(0)));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPreferenceSwitch(String str, boolean z) {
        if (str == null) {
            return;
        }
        Log.d(TAG, "setPreferenceSwitch " + str + " checked " + z);
        Preference findPreference = this.mPreferenceScreen.findPreference(str);
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

    public void updateData() {
        this.mFuture = ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsAlarmListController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsAlarmListController.this.lambda$updateData$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateData$0() {
        this.mRingtones = getContactRingtones();
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsAlarmListController.1
            @Override // java.lang.Runnable
            public void run() {
                String ringtonesId = MusicUtils.getRingtonesId(((AbstractPreferenceController) GlyphsAlarmListController.this).mContext, GlyphsAlarmListController.this.mUriForDefaultItem);
                GlyphsAlarmListController glyphsAlarmListController = GlyphsAlarmListController.this;
                glyphsAlarmListController.mAddAlarmPreference = glyphsAlarmListController.mPreferenceScreen.findPreference(GlyphsAlarmListController.KEY_GLYPHS_ADD_ALARM);
                GlyphsAlarmListController.this.mPreferenceScreen.removeAll();
                GlyphsAlarmListController.this.mPreferenceScreen.addPreference(GlyphsAlarmListController.this.mAddAlarmPreference);
                GlyphsAlarmListController.this.mRingtones.add(0, new GlyphsRingtoneSelectorPreference.Ringtone(null, null));
                Log.d(GlyphsAlarmListController.TAG, "mDefaultRingtoneUri " + GlyphsAlarmListController.this.mUriForDefaultItem);
                if (GlyphsAlarmListController.this.mUriForDefaultItem == null) {
                    GlyphsAlarmListController.this.mCurrentKey = "key_glyphs_ringtone_none";
                }
                for (final int i = 0; i < GlyphsAlarmListController.this.mRingtones.size(); i++) {
                    PrimaryCheckBoxPreference primaryCheckBoxPreference = new PrimaryCheckBoxPreference(((AbstractPreferenceController) GlyphsAlarmListController.this).mContext);
                    if (TextUtils.isEmpty(((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsAlarmListController.this.mRingtones.get(i)).getUri())) {
                        primaryCheckBoxPreference.setTitle(((AbstractPreferenceController) GlyphsAlarmListController.this).mContext.getString(17041302));
                        primaryCheckBoxPreference.setKey("key_glyphs_ringtone_none");
                    } else {
                        primaryCheckBoxPreference.setKey(GlyphsAlarmListController.KEY_ITEM_PREFIX + ((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsAlarmListController.this.mRingtones.get(i)).getId());
                        primaryCheckBoxPreference.setTitle(((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsAlarmListController.this.mRingtones.get(i)).getTitle());
                    }
                    if (TextUtils.equals(ringtonesId, ((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsAlarmListController.this.mRingtones.get(i)).getId())) {
                        GlyphsAlarmListController.this.mCurrentKey = primaryCheckBoxPreference.getKey();
                    }
                    primaryCheckBoxPreference.setOnSelectedListener(new PrimaryCheckBoxPreference.OnSelectedListener() { // from class: com.nt.settings.glyphs.pick.GlyphsAlarmListController.1.1
                        @Override // com.nt.settings.glyphs.widget.PrimaryCheckBoxPreference.OnSelectedListener
                        public void onCheckChange(Preference preference, boolean z) {
                            GlyphsAlarmListController glyphsAlarmListController2 = GlyphsAlarmListController.this;
                            glyphsAlarmListController2.setPreferenceSwitch(glyphsAlarmListController2.mCurrentKey, false);
                            if (GlyphsAlarmListController.this.mListener != null) {
                                GlyphsAlarmListController.this.mListener.onChecked(true);
                            }
                            if (GlyphsAlarmListController.this.mCurrentRingtone != null) {
                                GlyphsAlarmListController.this.mCurrentRingtone.stop();
                                Log.d(GlyphsAlarmListController.TAG, "mCurrentRingtone stop " + GlyphsAlarmListController.this.mCurrentRingtone);
                            }
                            GlyphsAlarmListController.this.mCurrentKey = preference.getKey();
                            GlyphsAlarmListController glyphsAlarmListController3 = GlyphsAlarmListController.this;
                            glyphsAlarmListController3.setPreferenceSwitch(glyphsAlarmListController3.mCurrentKey, true);
                            if (TextUtils.isEmpty(((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsAlarmListController.this.mRingtones.get(i)).getUri())) {
                                GlyphsAlarmListController.this.mCurrentRingtone = null;
                                GlyphsAlarmListController.this.mUriForDefaultItem = null;
                                Log.d(GlyphsAlarmListController.TAG, "mCurrentRingtone " + GlyphsAlarmListController.this.mCurrentRingtone);
                                return;
                            }
                            GlyphsAlarmListController glyphsAlarmListController4 = GlyphsAlarmListController.this;
                            glyphsAlarmListController4.mCurrentRingtone = RingtoneManager.getRingtone(((AbstractPreferenceController) glyphsAlarmListController4).mContext, Uri.parse(((GlyphsRingtoneSelectorPreference.Ringtone) GlyphsAlarmListController.this.mRingtones.get(i)).getUri()));
                            if (GlyphsAlarmListController.this.mCurrentRingtone != null) {
                                GlyphsAlarmListController.this.mCurrentRingtone.setAudioAttributes(new AudioAttributes.Builder().setInternalLegacyStreamType(4).setHapticChannelsMuted(false).build());
                                GlyphsAlarmListController.this.mCurrentRingtone.play();
                            }
                            GlyphsAlarmListController glyphsAlarmListController5 = GlyphsAlarmListController.this;
                            glyphsAlarmListController5.mUriForDefaultItem = Uri.parse(((GlyphsRingtoneSelectorPreference.Ringtone) glyphsAlarmListController5.mRingtones.get(i)).getUri());
                        }
                    });
                    GlyphsAlarmListController.this.mPreferenceScreen.addPreference(primaryCheckBoxPreference);
                }
                GlyphsAlarmListController glyphsAlarmListController2 = GlyphsAlarmListController.this;
                glyphsAlarmListController2.setPreferenceSwitch(glyphsAlarmListController2.mCurrentKey, true);
                if (GlyphsAlarmListController.this.mListener != null) {
                    GlyphsAlarmListController.this.mListener.onChecked(!TextUtils.isEmpty(GlyphsAlarmListController.this.mCurrentKey));
                }
            }
        });
    }

    public void release() {
        Ringtone ringtone = this.mCurrentRingtone;
        if (ringtone != null) {
            try {
                ringtone.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getCurrentRingtoneUri() {
        if (TextUtils.equals(this.mCurrentKey, "key_glyphs_ringtone_none")) {
            this.mUriForDefaultItem = null;
        }
        return this.mUriForDefaultItem;
    }

    public boolean isChecked() {
        return !TextUtils.isEmpty(this.mCurrentKey);
    }

    @Override // com.android.settings.core.BasePreferenceController
    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
        if (i == SELECT_MY_SOUND_REQUEST_CODE) {
            ((Activity) this.mContext).setResult(i2, intent);
            if (i2 != -1) {
                return;
            }
            ((Activity) this.mContext).finish();
        }
    }
}
