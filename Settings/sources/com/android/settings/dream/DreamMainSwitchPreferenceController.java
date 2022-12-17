package com.android.settings.dream;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.settings.widget.SettingsMainSwitchPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.dream.DreamBackend;

public class DreamMainSwitchPreferenceController extends SettingsMainSwitchPreferenceController implements LifecycleObserver {
    static final String MAIN_SWITCH_PREF_KEY = "dream_main_settings_switch";
    private final DreamBackend mBackend;
    private final ContentObserver mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z) {
            DreamMainSwitchPreferenceController dreamMainSwitchPreferenceController = DreamMainSwitchPreferenceController.this;
            dreamMainSwitchPreferenceController.updateState(dreamMainSwitchPreferenceController.mSwitchPreference);
        }
    };

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return 0;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean isSliceable() {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DreamMainSwitchPreferenceController(Context context, String str) {
        super(context, str);
        this.mBackend = DreamBackend.getInstance(context);
    }

    public boolean isChecked() {
        return this.mBackend.isEnabled();
    }

    public boolean setChecked(boolean z) {
        this.mBackend.setEnabled(z);
        return true;
    }

    /* access modifiers changed from: package-private */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("screensaver_enabled"), false, this.mObserver);
    }

    /* access modifiers changed from: package-private */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mObserver);
    }
}
