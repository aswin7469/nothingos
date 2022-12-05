package com.nt.settings.glyphs;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.PreferenceScreen;
import com.android.settings.Utils;
import com.android.settings.slices.SliceBackgroundWorker;
import com.nt.settings.glyphs.utils.ThreadUtils;
import com.nt.settings.glyphs.widget.GlyphsRingtoneSelectorPreference;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsNotificationSelectorPreferenceController extends GlyphsRingtoneSelectorPreferenceController {
    private GlyphsRingtoneSelectorPreference mPreference;
    private boolean mSoundOnly;

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

    public GlyphsNotificationSelectorPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.nt.settings.glyphs.GlyphsRingtoneSelectorPreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference = (GlyphsRingtoneSelectorPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = glyphsRingtoneSelectorPreference;
        init(glyphsRingtoneSelectorPreference);
    }

    public void setSoundOnly(boolean z) {
        if (this.mSoundOnly == z) {
            return;
        }
        this.mSoundOnly = z;
        GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference = this.mPreference;
        if (glyphsRingtoneSelectorPreference == null) {
            return;
        }
        glyphsRingtoneSelectorPreference.setSoundOnly(z);
    }

    protected Uri getSelectedRingtone() {
        return RingtoneManager.getActualDefaultRingtoneUri(Utils.createPackageContextAsUser(this.mContext, UserHandle.myUserId()), 2);
    }

    public List<GlyphsRingtoneSelectorPreference.Ringtone> getNotificationRingtones() {
        RingtoneManager ringtoneManager = new RingtoneManager(this.mContext);
        ringtoneManager.setType(2);
        Cursor cursor = ringtoneManager.getCursor();
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            arrayList.add(new GlyphsRingtoneSelectorPreference.Ringtone(cursor.getString(2) + "/" + cursor.getString(0), cursor.getString(1)));
        }
        return arrayList;
    }

    private void init(final GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference) {
        boolean z = false;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "led_notification_mode", 0) == 1) {
            z = true;
        }
        this.mSoundOnly = z;
        glyphsRingtoneSelectorPreference.setSoundOnly(z);
        glyphsRingtoneSelectorPreference.setRingtoneType(2);
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsNotificationSelectorPreferenceController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsNotificationSelectorPreferenceController.this.lambda$init$1(glyphsRingtoneSelectorPreference);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(final GlyphsRingtoneSelectorPreference glyphsRingtoneSelectorPreference) {
        String str;
        final List<GlyphsRingtoneSelectorPreference.Ringtone> notificationRingtones = getNotificationRingtones();
        Uri selectedRingtone = getSelectedRingtone();
        if (notificationRingtones == null || notificationRingtones.size() <= 0) {
            return;
        }
        final int i = 0;
        if (selectedRingtone != null) {
            try {
                str = Ringtone.getTitle(this.mContext, selectedRingtone, false, true);
            } catch (IllegalArgumentException unused) {
                str = "";
            }
            int i2 = 0;
            while (true) {
                if (i2 >= notificationRingtones.size()) {
                    break;
                } else if (TextUtils.equals(notificationRingtones.get(i2).getTitle(), str)) {
                    i = i2;
                    break;
                } else {
                    i2++;
                }
            }
        }
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsNotificationSelectorPreferenceController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsRingtoneSelectorPreference.this.setData(notificationRingtones, i);
            }
        });
    }

    public void setDefaultNotificationUri(int i) {
        GlyphsRingtoneSelectorPreference.Ringtone currentRingtone = this.mPreference.getCurrentRingtone();
        if (currentRingtone == null || TextUtils.isEmpty(currentRingtone.getUri())) {
            return;
        }
        RingtoneManager.setActualDefaultRingtoneUri(Utils.createPackageContextAsUser(this.mContext, UserHandle.myUserId()), 2, Uri.parse(currentRingtone.getUri()));
        saveNotificationMode(i);
    }

    private void saveNotificationMode(int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "led_notification_mode", i);
        ((Activity) this.mContext).finish();
    }
}
