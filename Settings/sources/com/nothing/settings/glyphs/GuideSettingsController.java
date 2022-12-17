package com.nothing.settings.glyphs;

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
import com.nothing.settings.glyphs.preference.GuidePreference;
import com.nothing.settings.glyphs.utils.GlyphsSettings;

public class GuideSettingsController extends BasePreferenceController implements GuidePreference.OnConfirmListener {
    private static final String ACTION_CONTACT_RINGTONES_LEST = "android.settings.ACTION_CONTACT_RINGTONES_LEST";
    /* access modifiers changed from: private */
    public GuidePreference mPreference;

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

    @TargetApi(14)
    public GuideSettingsController(Context context, String str) {
        super(context, str);
        ((Activity) this.mContext).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            public void onActivityDestroyed(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
                if (GuideSettingsController.this.mPreference != null) {
                    GuideSettingsController.this.mPreference.play("glyphs/beetle.csv");
                }
            }

            public void onActivityPaused(Activity activity) {
                if (GuideSettingsController.this.mPreference != null) {
                    GuideSettingsController.this.mPreference.release();
                }
            }
        });
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        GuidePreference guidePreference = (GuidePreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = guidePreference;
        guidePreference.setOnConfirmListener(this);
    }

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
