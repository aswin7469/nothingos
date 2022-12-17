package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class MagnificationFollowTypingPreferenceController extends TogglePreferenceController implements LifecycleObserver {
    static final String PREF_KEY = "magnification_follow_typing";
    private static final String TAG = "MagnificationFollowTypingPreferenceController";
    private SwitchPreference mFollowTypingPreference;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public MagnificationFollowTypingPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "accessibility_magnification_follow_typing_enabled", 1) == 1;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "accessibility_magnification_follow_typing_enabled", z ? 1 : 0);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_accessibility;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mFollowTypingPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    /* access modifiers changed from: package-private */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        updateState();
    }

    /* access modifiers changed from: package-private */
    public void updateState() {
        updateState(this.mFollowTypingPreference);
    }
}
