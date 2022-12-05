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
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.glyphs.utils.MusicUtils;
import com.nt.settings.glyphs.widget.GlyphsBackgroundPreference;
import java.util.concurrent.Future;
/* loaded from: classes2.dex */
public class GlyphsMySoundController extends BasePreferenceController implements Preference.OnPreferenceClickListener {
    private static final int SELECT_MY_SOUND_REQUEST_CODE = 1003;
    private static final String TAG = "MySoundController";
    private Future mFuture;
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

    public GlyphsMySoundController(Context context, String str, int i, Uri uri, int i2) {
        super(context, str);
        this.mRingtoneType = i;
        this.mUriForDefaultItem = uri;
        this.mSoundOnly = i2;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        GlyphsBackgroundPreference glyphsBackgroundPreference = (GlyphsBackgroundPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = glyphsBackgroundPreference;
        if (glyphsBackgroundPreference != null) {
            glyphsBackgroundPreference.setOnPreferenceClickListener(this);
            this.mPreference.setBackground(this.mContext.getDrawable(R.drawable.bg_guide_bottom));
            GlyphsBackgroundPreference glyphsBackgroundPreference2 = this.mPreference;
            Uri uri = this.mUriForDefaultItem;
            glyphsBackgroundPreference2.setWidgetFrameVisibility((uri == null || !isInternalRingtone(uri)) ? 0 : 8);
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
        this.mFuture = ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsMySoundController.1
            @Override // java.lang.Runnable
            public void run() {
                if (((AbstractPreferenceController) GlyphsMySoundController.this).mContext == null || ((Activity) ((AbstractPreferenceController) GlyphsMySoundController.this).mContext).isFinishing() || ((Activity) ((AbstractPreferenceController) GlyphsMySoundController.this).mContext).isDestroyed()) {
                    return;
                }
                final int externalRingtoneCount = MusicUtils.getExternalRingtoneCount(((AbstractPreferenceController) GlyphsMySoundController.this).mContext, GlyphsMySoundController.this.mRingtoneType);
                ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.pick.GlyphsMySoundController.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (externalRingtoneCount > 0) {
                            AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                            preference.setSummary(((Activity) ((AbstractPreferenceController) GlyphsMySoundController.this).mContext).getString(R.string.nt_glyphs_sound_count_summary, new Object[]{String.valueOf(externalRingtoneCount)}));
                            return;
                        }
                        AnonymousClass1 anonymousClass12 = AnonymousClass1.this;
                        preference.setSummary(((Activity) ((AbstractPreferenceController) GlyphsMySoundController.this).mContext).getString(R.string.nt_glyphs_add_own_sound_summary));
                    }
                });
            }
        });
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        Context context = this.mContext;
        GlyphsPickResultHelper.startMySoundSelector((Activity) context, context.getString(R.string.nt_glyphs_my_sound_title), this.mUriForDefaultItem, this.mRingtoneType, this.mSoundOnly, SELECT_MY_SOUND_REQUEST_CODE);
        return true;
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
