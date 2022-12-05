package com.nt.settings.glyphs;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.settings.core.lifecycle.events.OnResume;
import com.nt.settings.glyphs.utils.MusicUtils;
import com.nt.settings.glyphs.widget.GlyphsRingtoneListPreference;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsRingtoneListPreferenceController extends BasePreferenceController implements OnResume, OnPause, LifecycleObserver {
    private GlyphsRingtoneListPreference mPreference;
    private int mRingtoneType;

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

    @Override // com.nt.settings.core.lifecycle.events.OnResume
    public void onResume() {
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public final void updateState(Preference preference) {
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsRingtoneListPreferenceController(Context context, String str) {
        super(context, str);
        this.mRingtoneType = 1;
    }

    public GlyphsRingtoneListPreferenceController(Context context, String str, int i) {
        super(context, str);
        this.mRingtoneType = 1;
        this.mRingtoneType = i;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (GlyphsRingtoneListPreference) preferenceScreen.findPreference(getPreferenceKey());
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsRingtoneListPreferenceController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsRingtoneListPreferenceController.this.lambda$displayPreference$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$1() {
        final List<MusicUtils.Song> musicData = MusicUtils.getMusicData(this.mContext);
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsRingtoneListPreferenceController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsRingtoneListPreferenceController.this.lambda$displayPreference$0(musicData);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(List list) {
        this.mPreference.setRingtonesList(list);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
        GlyphsRingtoneListPreference glyphsRingtoneListPreference = this.mPreference;
        if (glyphsRingtoneListPreference != null) {
            glyphsRingtoneListPreference.release();
        }
    }
}
