package com.nt.settings.glyphs.ringtone;

import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.PreferenceScreen;
import com.android.settings.slices.SliceBackgroundWorker;
import com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController;
import com.nt.settings.glyphs.utils.RingtoneHelper;
import com.nt.settings.glyphs.utils.ThreadUtils;
import com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
/* loaded from: classes2.dex */
public class GlyphsRingtonesSelectorPreferenceController extends GlyphsRingtoneSelectorPreferenceController {
    private static final String TAG = "RingtonesSelector";
    private String mContactName;
    private Future mFuture;
    private int mRingtoneType;
    private int mSoundOnly;
    private Uri mUriForDefaultItem;

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsRingtonesSelectorPreferenceController(Context context, String str, int i, Uri uri, int i2, String str2) {
        super(context, str);
        this.mRingtoneType = 1;
        this.mRingtoneType = i;
        this.mUriForDefaultItem = uri;
        this.mSoundOnly = i2;
        this.mContactName = str2;
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        init((GlyphsRingtoneSelectorPreference) preferenceScreen.findPreference(getPreferenceKey()));
    }

    private List<GlyphsRingtoneSelectorPreference.Ringtone> getContactRingtones() {
        RingtoneHelper ringtoneHelper = new RingtoneHelper(this.mContext);
        ringtoneHelper.setType(this.mRingtoneType);
        Cursor cursor = ringtoneHelper.getCursor();
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            arrayList.add(new GlyphsRingtoneSelectorPreference.Ringtone(cursor.getString(2) + "/" + cursor.getString(0) + "?title=" + cursor.getString(1), cursor.getString(1)));
        }
        return arrayList;
    }

    public Uri getCurrentUri() {
        GlyphsRingtoneSelectorPreference.Ringtone currentRingtone = this.mPreference.getCurrentRingtone();
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
        GlyphsRingtoneSelectorPreference.Ringtone currentRingtone = this.mPreference.getCurrentRingtone();
        if (currentRingtone == null) {
            return;
        }
        RingtoneManager.setActualDefaultRingtoneUri(this.mContext, i, Uri.parse(currentRingtone.getUri()));
        Settings.Global.putInt(this.mContext.getContentResolver(), "led_default_ringtone_sound_mode", i2);
    }

    public void setSoundOnly(boolean z) {
        this.mSoundOnly = z ? 1 : 0;
        GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference = this.mPreference;
        if (glyphsRingtoneSelectorPreference != null) {
            glyphsRingtoneSelectorPreference.setSoundOnly(z);
        }
    }

    private void init(final GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference) {
        boolean z = true;
        if (this.mSoundOnly != 1) {
            z = false;
        }
        glyphsRingtoneSelectorPreference.setSoundOnly(z);
        this.mFuture = ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsRingtonesSelectorPreferenceController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsRingtonesSelectorPreferenceController.this.lambda$init$1(glyphsRingtoneSelectorPreference);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(final GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference) {
        String str;
        final List<GlyphsRingtoneSelectorPreference.Ringtone> contactRingtones = getContactRingtones();
        if (contactRingtones == null || contactRingtones.size() <= 0) {
            return;
        }
        Uri uri = this.mUriForDefaultItem;
        final int i = 0;
        try {
            if (uri != null) {
                str = Ringtone.getTitle(this.mContext, uri, false, true);
            } else {
                Context context = this.mContext;
                str = Ringtone.getTitle(context, RingtoneManager.getActualDefaultRingtoneUri(context, this.mRingtoneType), false, true);
            }
        } catch (IllegalArgumentException unused) {
            str = "";
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
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsRingtonesSelectorPreferenceController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsRingtonesSelectorPreferenceController.this.lambda$init$0(glyphsRingtoneSelectorPreference, contactRingtones, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference, List list, int i) {
        glyphsRingtoneSelectorPreference.setRingtoneType(this.mRingtoneType);
        glyphsRingtoneSelectorPreference.setIsLoop(true);
        glyphsRingtoneSelectorPreference.setData(list, i);
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settingslib.core.lifecycle.events.OnDestroy
    public void onDestroy() {
        super.onDestroy();
        Future future = this.mFuture;
        if (future != null) {
            future.cancel(true);
        }
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        super.onStop();
        Future future = this.mFuture;
        if (future != null) {
            future.cancel(true);
        }
    }
}
