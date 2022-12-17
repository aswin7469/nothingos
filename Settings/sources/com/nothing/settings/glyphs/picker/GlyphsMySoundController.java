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
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.glyphs.preference.GlyphsBackgroundPreference;
import com.nothing.settings.glyphs.utils.MusicUtils;
import com.nothing.settings.glyphs.utils.ResultPickHelper;
import java.util.concurrent.Future;

public class GlyphsMySoundController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final int SELECT_MY_SOUND_REQUEST_CODE = 1003;
    private static final String TAG = "MySoundController";
    private Future mFuture;
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

    public GlyphsMySoundController(Context context, String str, int i, Uri uri, int i2, String str2) {
        super(context, str);
        this.mRingtoneType = i;
        this.mUriForDefaultItem = uri;
        this.mSoundOnly = i2;
        this.mPageTitle = str2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        GlyphsBackgroundPreference glyphsBackgroundPreference = (GlyphsBackgroundPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = glyphsBackgroundPreference;
        if (glyphsBackgroundPreference != null) {
            glyphsBackgroundPreference.setOnPreferenceClickListener(this);
            this.mPreference.setBackground(this.mContext.getDrawable(R$drawable.bg_guide_bottom));
            Uri uri = this.mUriForDefaultItem;
            this.mPreference.setWidgetFrameVisibility((uri == null || !isInternalRingtone(uri)) ? 0 : 8);
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

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mFuture = ThreadUtils.postOnBackgroundThread((Runnable) new GlyphsMySoundController$$ExternalSyntheticLambda0(this, preference));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateState$0(final Preference preference) {
        Context context = this.mContext;
        if (context != null && !((Activity) context).isFinishing() && !((Activity) this.mContext).isDestroyed()) {
            final int externalRingtoneCount = MusicUtils.getExternalRingtoneCount(this.mContext, this.mRingtoneType);
            ThreadUtils.postOnMainThread(new Runnable() {
                public void run() {
                    if (externalRingtoneCount > 0) {
                        preference.setSummary((CharSequence) ((Activity) GlyphsMySoundController.this.mContext).getString(R$string.nt_glyphs_sound_count_summary, new Object[]{String.valueOf(externalRingtoneCount)}));
                        return;
                    }
                    preference.setSummary((CharSequence) ((Activity) GlyphsMySoundController.this.mContext).getString(R$string.nt_glyphs_add_own_sound_summary));
                }
            });
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        ResultPickHelper.startMySoundSelector((Activity) this.mContext, this.mPageTitle, this.mUriForDefaultItem, this.mRingtoneType, this.mSoundOnly, SELECT_MY_SOUND_REQUEST_CODE);
        return true;
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
