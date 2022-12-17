package com.android.settings.accessibility;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$drawable;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.PrimarySwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnSaveInstanceState;

public abstract class AccessibilityQuickSettingsPrimarySwitchPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnCreate, OnSaveInstanceState {
    private static final String KEY_SAVED_QS_TOOLTIP_RESHOW = "qs_tooltip_reshow";
    private final Handler mHandler;
    private boolean mNeedsQSTooltipReshow = false;
    private PrimarySwitchPreference mPreference;
    private AccessibilityQuickSettingsTooltipWindow mTooltipWindow;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: package-private */
    public abstract ComponentName getTileComponentName();

    /* access modifiers changed from: package-private */
    public abstract CharSequence getTileTooltipContent();

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean isChecked() {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AccessibilityQuickSettingsPrimarySwitchPreferenceController(Context context, String str) {
        super(context, str);
        this.mHandler = new Handler(context.getMainLooper());
    }

    public void onCreate(Bundle bundle) {
        if (bundle != null && bundle.containsKey(KEY_SAVED_QS_TOOLTIP_RESHOW)) {
            this.mNeedsQSTooltipReshow = bundle.getBoolean(KEY_SAVED_QS_TOOLTIP_RESHOW);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        AccessibilityQuickSettingsTooltipWindow accessibilityQuickSettingsTooltipWindow = this.mTooltipWindow;
        if (accessibilityQuickSettingsTooltipWindow != null) {
            bundle.putBoolean(KEY_SAVED_QS_TOOLTIP_RESHOW, accessibilityQuickSettingsTooltipWindow.isShowing());
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (PrimarySwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        if (this.mNeedsQSTooltipReshow) {
            this.mHandler.post(new C0578xa5b9465a(this));
        }
    }

    public boolean setChecked(boolean z) {
        if (z) {
            showQuickSettingsTooltipIfNeeded();
        }
        return z;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_accessibility;
    }

    /* access modifiers changed from: private */
    public void showQuickSettingsTooltipIfNeeded() {
        ComponentName tileComponentName = getTileComponentName();
        if (tileComponentName != null) {
            if (this.mNeedsQSTooltipReshow || !AccessibilityQuickSettingUtils.hasValueInSharedPreferences(this.mContext, tileComponentName)) {
                AccessibilityQuickSettingsTooltipWindow accessibilityQuickSettingsTooltipWindow = new AccessibilityQuickSettingsTooltipWindow(this.mContext);
                this.mTooltipWindow = accessibilityQuickSettingsTooltipWindow;
                accessibilityQuickSettingsTooltipWindow.setup(getTileTooltipContent(), R$drawable.accessibility_auto_added_qs_tooltip_illustration);
                this.mTooltipWindow.showAtTopCenter(this.mPreference.getSwitch());
                AccessibilityQuickSettingUtils.optInValueToSharedPreferences(this.mContext, tileComponentName);
                this.mNeedsQSTooltipReshow = false;
            }
        }
    }
}
