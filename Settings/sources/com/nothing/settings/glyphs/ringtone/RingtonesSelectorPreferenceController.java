package com.nothing.settings.glyphs.ringtone;

import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.preference.RingtoneSelectorPreference;
import com.nothing.settings.glyphs.utils.RingtoneHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class RingtonesSelectorPreferenceController extends RingtoneSelectorPreferenceController {
    private static final String TAG = "RingtonesSelector";
    private String mContactName;
    private Future mFuture;
    private final int mRingtoneType;
    private int mSoundOnly;
    private final Uri mUriForDefaultItem;

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public RingtonesSelectorPreferenceController(Context context, String str, int i, Uri uri, int i2, String str2) {
        super(context, str);
        this.mRingtoneType = i;
        this.mUriForDefaultItem = uri;
        this.mSoundOnly = i2;
        this.mContactName = str2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        init((RingtoneSelectorPreference) preferenceScreen.findPreference(getPreferenceKey()));
    }

    private List<RingtoneSelectorPreference.Ringtone> getContactRingtones() {
        RingtoneHelper ringtoneHelper = new RingtoneHelper(this.mContext);
        ringtoneHelper.setType(this.mRingtoneType);
        Cursor cursor = ringtoneHelper.getCursor();
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            arrayList.add(new RingtoneSelectorPreference.Ringtone(cursor.getString(2) + "/" + cursor.getString(0) + "?title=" + cursor.getString(1), cursor.getString(1)));
        }
        return arrayList;
    }

    public Uri getCurrentUri() {
        RingtoneSelectorPreference.Ringtone currentRingtone = this.mPreference.getCurrentRingtone();
        if (currentRingtone == null) {
            return null;
        }
        try {
            return Uri.parse(currentRingtone.getUri());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDefaultRingtone(int i, int i2) {
        RingtoneSelectorPreference.Ringtone currentRingtone = this.mPreference.getCurrentRingtone();
        if (currentRingtone != null) {
            RingtoneManager.setActualDefaultRingtoneUri(this.mContext, i, Uri.parse(currentRingtone.getUri()));
            Settings.Global.putInt(this.mContext.getContentResolver(), "led_default_ringtone_sound_mode", i2);
        }
    }

    public void setSoundOnly(boolean z) {
        this.mSoundOnly = z ? 1 : 0;
        RingtoneSelectorPreference ringtoneSelectorPreference = this.mPreference;
        if (ringtoneSelectorPreference != null) {
            ringtoneSelectorPreference.setSoundOnly(z);
        }
    }

    private void init(RingtoneSelectorPreference ringtoneSelectorPreference) {
        boolean z = true;
        if (this.mSoundOnly != 1) {
            z = false;
        }
        ringtoneSelectorPreference.setSoundOnly(z);
        this.mFuture = ThreadUtils.postOnBackgroundThread((Runnable) new RingtonesSelectorPreferenceController$$ExternalSyntheticLambda1(this, ringtoneSelectorPreference));
    }

    /* renamed from: initInner */
    public void lambda$init$0(RingtoneSelectorPreference ringtoneSelectorPreference) {
        String str;
        List<RingtoneSelectorPreference.Ringtone> contactRingtones = getContactRingtones();
        if (contactRingtones.size() > 0) {
            Uri uri = this.mUriForDefaultItem;
            int i = 0;
            if (uri != null) {
                try {
                    str = Ringtone.getTitle(this.mContext, uri, false, true);
                } catch (IllegalArgumentException unused) {
                    str = "";
                }
            } else {
                Context context = this.mContext;
                str = Ringtone.getTitle(context, RingtoneManager.getActualDefaultRingtoneUri(context, this.mRingtoneType), false, true);
            }
            int i2 = 0;
            while (true) {
                if (i2 >= contactRingtones.size()) {
                    break;
                } else if (TextUtils.equals(contactRingtones.get(i2).getTitle(), str)) {
                    i = i2;
                    break;
                } else {
                    i2++;
                }
            }
            ThreadUtils.postOnMainThread(new RingtonesSelectorPreferenceController$$ExternalSyntheticLambda0(this, ringtoneSelectorPreference, contactRingtones, i));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initInner$1(RingtoneSelectorPreference ringtoneSelectorPreference, List list, int i) {
        ringtoneSelectorPreference.setRingtoneType(this.mRingtoneType);
        ringtoneSelectorPreference.setIsLoop(true);
        ringtoneSelectorPreference.setData(list, i);
    }

    public void onDestroy() {
        super.onDestroy();
        Future future = this.mFuture;
        if (future != null) {
            future.cancel(true);
        }
    }

    public void onStop() {
        super.onStop();
        Future future = this.mFuture;
        if (future != null) {
            future.cancel(true);
        }
    }
}
