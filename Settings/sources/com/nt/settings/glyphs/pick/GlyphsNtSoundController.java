package com.nt.settings.glyphs.pick;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.nt.settings.glyphs.widget.GlyphsBackgroundPreference;
/* loaded from: classes2.dex */
public class GlyphsNtSoundController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final int SELECT_NT_RINGTONE_REQUEST_CODE = 1002;
    private static final String TAG = "NtSoundController";
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

    public GlyphsNtSoundController(Context context, String str, int i, Uri uri, int i2, String str2) {
        super(context, str);
        this.mRingtoneType = i;
        this.mUriForDefaultItem = uri;
        this.mContactName = str2;
        this.mSoundOnly = i2;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        initPreference(preferenceScreen.findPreference(getPreferenceKey()));
    }

    private void initPreference(Preference preference) {
        if (preference == null) {
            return;
        }
        this.mPreference = (GlyphsBackgroundPreference) preference;
        preference.setOnPreferenceClickListener(this);
        this.mPreference.setBackground(this.mContext.getDrawable(R.drawable.bg_guide_top));
        int i = 0;
        preference.setSummary(((Activity) this.mContext).getString(R.string.nt_glyphs_sound_count_summary, new Object[]{"10"}));
        GlyphsBackgroundPreference glyphsBackgroundPreference = this.mPreference;
        Uri uri = this.mUriForDefaultItem;
        if (uri == null || !isInternalRingtone(uri)) {
            i = 8;
        }
        glyphsBackgroundPreference.setWidgetFrameVisibility(i);
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

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        Context context = this.mContext;
        GlyphsPickResultHelper.startNtSoundSelector((Activity) context, context.getString(R.string.nt_glyphs_my_sound_title), this.mContactName, this.mUriForDefaultItem, this.mRingtoneType, this.mSoundOnly, SELECT_NT_RINGTONE_REQUEST_CODE);
        return true;
    }

    @Override // com.android.settings.core.BasePreferenceController
    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
        if (i == SELECT_NT_RINGTONE_REQUEST_CODE) {
            ((Activity) this.mContext).setResult(i2, intent);
            if (i2 != -1) {
                return;
            }
            ((Activity) this.mContext).finish();
        }
    }
}
