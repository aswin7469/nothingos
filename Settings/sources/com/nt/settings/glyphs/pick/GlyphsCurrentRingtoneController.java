package com.nt.settings.glyphs.pick;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.glyphs.widget.GlyphsBackgroundPreference;
/* loaded from: classes2.dex */
public class GlyphsCurrentRingtoneController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1001;
    private static final String TAG = "CurrentRingtoneCtr";
    private String mContactName;
    private GlyphsBackgroundPreference mPreference;
    private int mRingtoneType;
    private int mSoundOnly;
    private Uri mUriForDefaultItem;

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

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsCurrentRingtoneController(Context context, String str, int i, Uri uri, int i2, String str2) {
        super(context, str);
        this.mRingtoneType = i;
        this.mUriForDefaultItem = uri;
        this.mSoundOnly = i2;
        this.mContactName = str2;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        GlyphsBackgroundPreference glyphsBackgroundPreference = (GlyphsBackgroundPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = glyphsBackgroundPreference;
        if (glyphsBackgroundPreference != null) {
            glyphsBackgroundPreference.setOnPreferenceClickListener(this);
            this.mPreference.setBackground(this.mContext.getDrawable(R.drawable.bg_guide));
        }
    }

    private boolean isInternalRingtone(Uri uri) {
        return isRingtoneUriInStorage(uri, MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
    }

    private static boolean isRingtoneUriInStorage(Uri uri, Uri uri2) {
        Uri uriWithoutUserId = ContentProvider.getUriWithoutUserId(uri);
        if (uriWithoutUserId == null) {
            return false;
        }
        return uriWithoutUserId.toString().startsWith(uri2.toString());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(final Preference preference) {
        super.updateState(preference);
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsCurrentRingtoneController.1
            @Override // java.lang.Runnable
            public void run() {
                final String defaultRingtoneTitle = GlyphsCurrentRingtoneController.this.getDefaultRingtoneTitle();
                ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsCurrentRingtoneController.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Preference preference2 = preference;
                        if (preference2 == null) {
                            return;
                        }
                        preference2.setTitle(defaultRingtoneTitle);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDefaultRingtoneTitle() {
        try {
            return Ringtone.getTitle(this.mContext, this.mUriForDefaultItem, false, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        if (isInternalRingtone(this.mUriForDefaultItem)) {
            Context context = this.mContext;
            GlyphsPickResultHelper.startNtSoundSelector((Activity) context, context.getString(R.string.nt_glyphs_set_contact_ringtone_title), this.mContactName, this.mUriForDefaultItem, this.mRingtoneType, this.mSoundOnly, SELECT_RINGTONE_REQUEST_CODE);
            return true;
        }
        Context context2 = this.mContext;
        GlyphsPickResultHelper.startMySoundSelector((Activity) context2, context2.getString(R.string.nt_glyphs_set_contact_ringtone_title), this.mUriForDefaultItem, this.mRingtoneType, this.mSoundOnly, SELECT_RINGTONE_REQUEST_CODE);
        return true;
    }

    @Override // com.android.settings.core.BasePreferenceController
    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
        if (i == SELECT_RINGTONE_REQUEST_CODE) {
            ((Activity) this.mContext).setResult(i2, intent);
            if (i2 != -1) {
                return;
            }
            ((Activity) this.mContext).finish();
        }
    }
}
