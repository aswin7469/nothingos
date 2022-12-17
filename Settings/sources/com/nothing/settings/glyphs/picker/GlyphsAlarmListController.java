package com.nothing.settings.glyphs.picker;

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
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.preference.PrimaryCheckBoxDeletePreference;
import com.nothing.settings.glyphs.preference.PrimaryCheckBoxPreference;
import com.nothing.settings.glyphs.preference.RingtoneSelectorPreference;
import com.nothing.settings.glyphs.utils.MusicUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class GlyphsAlarmListController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final String KEY_GLYPHS_ADD_ALARM = "glyphs_add_alarm";
    private static final String KEY_ITEM_PREFIX = "key_glyphs_ringtone_";
    private static final int SELECT_MY_SOUND_REQUEST_CODE = 1003;
    private static final String TAG = "AlarmListController";
    private Preference mAddAlarmPreference;
    private String mCurrentKey;
    private Ringtone mCurrentRingtone;
    private Future mFuture;
    private final OnChangeListener mListener;
    private PreferenceScreen mPreferenceScreen;
    private final List<RingtoneSelectorPreference.Ringtone> mRingtones = new ArrayList();
    private Uri mUriForDefaultItem;

    public interface OnChangeListener {
        void onChecked(boolean z);
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public boolean onPreferenceClick(Preference preference) {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsAlarmListController(Context context, String str, Uri uri, OnChangeListener onChangeListener) {
        super(context, str);
        this.mUriForDefaultItem = uri;
        this.mListener = onChangeListener;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceScreen = preferenceScreen;
        this.mAddAlarmPreference = preferenceScreen.findPreference(KEY_GLYPHS_ADD_ALARM);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        updateData();
    }

    private List<RingtoneSelectorPreference.Ringtone> getContactRingtones() {
        RingtoneManager ringtoneManager = new RingtoneManager(this.mContext);
        ringtoneManager.setType(4);
        Cursor cursor = ringtoneManager.getCursor();
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            arrayList.add(new RingtoneSelectorPreference.Ringtone(cursor.getString(2) + "/" + cursor.getString(0) + "?title=" + cursor.getString(1), cursor.getString(1), "", cursor.getString(2) + "/" + cursor.getString(0), cursor.getString(0)));
        }
        return arrayList;
    }

    public void setPreferenceSwitch(String str, boolean z) {
        if (str != null) {
            Log.d(TAG, "setPreferenceSwitch " + str + " checked " + z);
            Preference findPreference = this.mPreferenceScreen.findPreference(str);
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

    public void updateData() {
        this.mFuture = ThreadUtils.postOnBackgroundThread((Runnable) new GlyphsAlarmListController$$ExternalSyntheticLambda2(this));
    }

    public void updateDataInner() {
        ThreadUtils.postOnMainThread(new GlyphsAlarmListController$$ExternalSyntheticLambda1(this, getContactRingtones()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDataInner$1(List list) {
        this.mRingtones.clear();
        this.mRingtones.addAll(list);
        String ringtonesId = MusicUtils.getRingtonesId(this.mContext, this.mUriForDefaultItem);
        this.mAddAlarmPreference = this.mPreferenceScreen.findPreference(KEY_GLYPHS_ADD_ALARM);
        this.mPreferenceScreen.removeAll();
        this.mPreferenceScreen.addPreference(this.mAddAlarmPreference);
        this.mRingtones.add(0, new RingtoneSelectorPreference.Ringtone((String) null, (String) null));
        Log.d(TAG, "mDefaultRingtoneUri " + this.mUriForDefaultItem);
        if (this.mUriForDefaultItem == null) {
            this.mCurrentKey = "key_glyphs_ringtone_none";
        }
        for (int i = 0; i < this.mRingtones.size(); i++) {
            PrimaryCheckBoxPreference primaryCheckBoxPreference = new PrimaryCheckBoxPreference(this.mContext);
            if (TextUtils.isEmpty(this.mRingtones.get(i).getUri())) {
                primaryCheckBoxPreference.setTitle((CharSequence) this.mContext.getString(R$string.nt_glyphs_ringtone_none_title));
                primaryCheckBoxPreference.setKey("key_glyphs_ringtone_none");
            } else {
                primaryCheckBoxPreference.setKey(KEY_ITEM_PREFIX + this.mRingtones.get(i).getId());
                primaryCheckBoxPreference.setTitle((CharSequence) this.mRingtones.get(i).getTitle());
            }
            if (TextUtils.equals(ringtonesId, this.mRingtones.get(i).getId())) {
                this.mCurrentKey = primaryCheckBoxPreference.getKey();
            }
            primaryCheckBoxPreference.setOnSelectedListener(new GlyphsAlarmListController$$ExternalSyntheticLambda0(this, i));
            this.mPreferenceScreen.addPreference(primaryCheckBoxPreference);
        }
        setPreferenceSwitch(this.mCurrentKey, true);
        OnChangeListener onChangeListener = this.mListener;
        if (onChangeListener != null) {
            onChangeListener.onChecked(!TextUtils.isEmpty(this.mCurrentKey));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDataInner$0(int i, Preference preference, boolean z) {
        setPreferenceSwitch(this.mCurrentKey, false);
        OnChangeListener onChangeListener = this.mListener;
        if (onChangeListener != null) {
            onChangeListener.onChecked(true);
        }
        Ringtone ringtone = this.mCurrentRingtone;
        if (ringtone != null) {
            ringtone.stop();
            Log.d(TAG, "mCurrentRingtone stop " + this.mCurrentRingtone);
        }
        String key = preference.getKey();
        this.mCurrentKey = key;
        setPreferenceSwitch(key, true);
        if (TextUtils.isEmpty(this.mRingtones.get(i).getUri())) {
            this.mCurrentRingtone = null;
            this.mUriForDefaultItem = null;
            Log.d(TAG, "mCurrentRingtone " + this.mCurrentRingtone);
            return;
        }
        Ringtone ringtone2 = RingtoneManager.getRingtone(this.mContext, Uri.parse(this.mRingtones.get(i).getUri()));
        this.mCurrentRingtone = ringtone2;
        if (ringtone2 != null) {
            ringtone2.setAudioAttributes(new AudioAttributes.Builder().setInternalLegacyStreamType(4).setHapticChannelsMuted(false).build());
            this.mCurrentRingtone.play();
        }
        this.mUriForDefaultItem = Uri.parse(this.mRingtones.get(i).getUri());
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

    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
        if (i == SELECT_MY_SOUND_REQUEST_CODE) {
            ((Activity) this.mContext).setResult(i2, intent);
            if (i2 == -1) {
                ((Activity) this.mContext).finish();
            }
        }
    }
}
