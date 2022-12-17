package com.nothing.settings.glyphs.picker;

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
import com.android.settings.R$drawable;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.preference.GlyphsBackgroundPreference;
import com.nothing.settings.glyphs.utils.ResultPickHelper;

public class GlyphsCurrentRingtoneController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final int SELECT_RINGTONE_REQUEST_CODE = 1001;
    private static final String TAG = "CurrentRingtoneCtr";
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

    public GlyphsCurrentRingtoneController(Context context, String str, int i, Uri uri, int i2, String str2, String str3) {
        super(context, str);
        this.mRingtoneType = i;
        this.mUriForDefaultItem = uri;
        this.mSoundOnly = i2;
        this.mContactName = str2;
        this.mPageTitle = str3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        GlyphsBackgroundPreference glyphsBackgroundPreference = (GlyphsBackgroundPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = glyphsBackgroundPreference;
        if (glyphsBackgroundPreference != null) {
            glyphsBackgroundPreference.setOnPreferenceClickListener(this);
            this.mPreference.setBackground(this.mContext.getDrawable(R$drawable.bg_guide));
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

    public void updateState(final Preference preference) {
        super.updateState(preference);
        ThreadUtils.postOnBackgroundThread((Runnable) new Runnable() {
            public void run() {
                ThreadUtils.postOnMainThread(new GlyphsCurrentRingtoneController$1$$ExternalSyntheticLambda0(preference, GlyphsCurrentRingtoneController.this.getDefaultRingtoneTitle()));
            }

            /* access modifiers changed from: private */
            public static /* synthetic */ void lambda$run$0(Preference preference, String str) {
                if (preference != null) {
                    preference.setTitle((CharSequence) str);
                }
            }
        });
    }

    public String getDefaultRingtoneTitle() {
        try {
            return Ringtone.getTitle(this.mContext, this.mUriForDefaultItem, true, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        if (isInternalRingtone(this.mUriForDefaultItem)) {
            ResultPickHelper.startNtSoundSelector((Activity) this.mContext, this.mPageTitle, this.mContactName, this.mUriForDefaultItem, this.mRingtoneType, this.mSoundOnly, SELECT_RINGTONE_REQUEST_CODE);
            return true;
        }
        ResultPickHelper.startMySoundSelector((Activity) this.mContext, this.mPageTitle, this.mUriForDefaultItem, this.mRingtoneType, this.mSoundOnly, SELECT_RINGTONE_REQUEST_CODE);
        return true;
    }

    public void onActivityControllerResult(int i, int i2, Intent intent) {
        super.onActivityControllerResult(i, i2, intent);
        if (i == SELECT_RINGTONE_REQUEST_CODE) {
            ((Activity) this.mContext).setResult(i2, intent);
            if (i2 == -1) {
                ((Activity) this.mContext).finish();
            }
        }
    }
}
