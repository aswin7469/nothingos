package com.nothing.settings.glyphs.picker;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$drawable;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.nothing.settings.glyphs.preference.GlyphsBackgroundPreference;
import com.nothing.settings.glyphs.utils.ResultPickHelper;

public class GlyphsNothingSoundController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final int SELECT_NT_RINGTONE_REQUEST_CODE = 1002;
    private static final String TAG = "NtSoundController";
    private final String mContactName;
    private String mPageTitle;
    private GlyphsBackgroundPreference mPreference;
    private final int mRingtoneType;
    private final int mSoundOnly;
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

    public GlyphsNothingSoundController(Context context, String str, int i, Uri uri, int i2, String str2, String str3) {
        super(context, str);
        this.mRingtoneType = i;
        this.mUriForDefaultItem = uri;
        this.mContactName = str2;
        this.mSoundOnly = i2;
        this.mPageTitle = str3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        initPreference(preferenceScreen.findPreference(getPreferenceKey()));
    }

    private void initPreference(Preference preference) {
        if (preference != null) {
            this.mPreference = (GlyphsBackgroundPreference) preference;
            preference.setOnPreferenceClickListener(this);
            this.mPreference.setBackground(this.mContext.getDrawable(R$drawable.bg_guide_top));
            int i = 0;
            preference.setSummary((CharSequence) ((Activity) this.mContext).getString(R$string.nt_glyphs_sound_count_summary, new Object[]{"10"}));
            Uri uri = this.mUriForDefaultItem;
            if (uri == null || !isInternalRingtone(uri)) {
                i = 8;
            }
            this.mPreference.setWidgetFrameVisibility(i);
        }
    }

    private boolean isInternalRingtone(Uri uri) {
        return isRingtoneUriInStorage(uri, MediaStore.Audio.Media.INTERNAL_CONTENT_URI) || RingtoneManager.isDefault(uri);
    }

    private static boolean isRingtoneUriInStorage(Uri uri, Uri uri2) {
        Uri uriWithoutUserId = ContentProvider.getUriWithoutUserId(uri);
        if (uriWithoutUserId == null) {
            return false;
        }
        return uriWithoutUserId.toString().startsWith(uri2.toString());
    }

    public boolean onPreferenceClick(Preference preference) {
        ResultPickHelper.startNtSoundSelector((Activity) this.mContext, this.mPageTitle, this.mContactName, this.mUriForDefaultItem, this.mRingtoneType, this.mSoundOnly, SELECT_NT_RINGTONE_REQUEST_CODE);
        return true;
    }

    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
        if (i == SELECT_NT_RINGTONE_REQUEST_CODE) {
            ((Activity) this.mContext).setResult(i2, intent);
            if (i2 == -1) {
                ((Activity) this.mContext).finish();
            }
        }
    }
}
