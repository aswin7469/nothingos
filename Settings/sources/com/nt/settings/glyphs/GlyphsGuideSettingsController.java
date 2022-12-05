package com.nt.settings.glyphs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.nt.settings.glyphs.utils.GlyphsSettings;
import com.nt.settings.glyphs.widget.GlyphsGuidePreference;
/* loaded from: classes2.dex */
public class GlyphsGuideSettingsController extends BasePreferenceController implements GlyphsGuidePreference.OnConfirmListener {
    private static final String ACTION_CONTACT_RINGTONES_LEST = "android.settings.ACTION_CONTACT_RINGTONES_LEST";
    private GlyphsGuidePreference mPreference;

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

    @TargetApi(14)
    public GlyphsGuideSettingsController(Context context, String str) {
        super(context, str);
        ((Activity) this.mContext).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() { // from class: com.nt.settings.glyphs.GlyphsGuideSettingsController.1
            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityDestroyed(Activity activity) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStarted(Activity activity) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStopped(Activity activity) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityResumed(Activity activity) {
                if (GlyphsGuideSettingsController.this.mPreference != null) {
                    GlyphsGuideSettingsController.this.mPreference.play("glyphs/beetle.csv");
                }
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPaused(Activity activity) {
                if (GlyphsGuideSettingsController.this.mPreference != null) {
                    GlyphsGuideSettingsController.this.mPreference.release();
                }
            }
        });
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        GlyphsGuidePreference glyphsGuidePreference = (GlyphsGuidePreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = glyphsGuidePreference;
        glyphsGuidePreference.setOnConfirmListener(this);
    }

    @Override // com.nt.settings.glyphs.widget.GlyphsGuidePreference.OnConfirmListener
    public void onConfirm(View view) {
        new GlyphsSettings(this.mContext).setShowGuideSettings(false);
        startContactRingtonesActivity();
    }

    private void startContactRingtonesActivity() {
        Intent intent = new Intent();
        intent.setAction(ACTION_CONTACT_RINGTONES_LEST);
        this.mContext.startActivity(intent);
        ((Activity) this.mContext).finish();
    }
}
