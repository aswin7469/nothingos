package com.nt.settings.glyphs.ringtone;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.utils.ThreadUtils;
/* loaded from: classes2.dex */
public class GlyphsRingtonesSelectPreferenceController extends BasePreferenceController implements LifecycleObserver, OnResume, OnPause, Preference.OnPreferenceClickListener {
    private Preference mPreference;

    private int getRingtoneType() {
        return 1;
    }

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

    @Override // com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GlyphsRingtonesSelectPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = findPreference;
        findPreference.setOnPreferenceClickListener(this);
    }

    private void refreshSummary() {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsRingtonesSelectPreferenceController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsRingtonesSelectPreferenceController.this.lambda$refreshSummary$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshSummary$1() {
        try {
            final String title = Ringtone.getTitle(this.mContext, RingtoneManager.getActualDefaultRingtoneUri(this.mContext, getRingtoneType()), false, true);
            if (title == null) {
                return;
            }
            ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.ringtone.GlyphsRingtonesSelectPreferenceController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    GlyphsRingtonesSelectPreferenceController.this.lambda$refreshSummary$0(title);
                }
            });
        } catch (IllegalArgumentException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshSummary$0(CharSequence charSequence) {
        this.mPreference.setSummary(charSequence);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnResume
    public void onResume() {
        refreshSummary();
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        startRingtoneSelectorActivity();
        return true;
    }

    private void startRingtoneSelectorActivity() {
        Intent intent = new Intent("android.settings.ACTION_CONTACT_RINGTONE_SETTINGS");
        intent.putExtra("page_title", this.mContext.getString(R.string.nt_default_ringtones_title));
        this.mContext.startActivity(intent);
    }
}
