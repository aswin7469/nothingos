package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.view.RotationPolicy;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.display.DeviceStateAutoRotationHelper;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class LockScreenRotationPreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    /* access modifiers changed from: private */
    public Preference mPreference;
    private RotationPolicy.RotationPolicyListener mRotationPolicyListener;

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

    public LockScreenRotationPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return !RotationPolicy.isRotationLocked(this.mContext);
    }

    public boolean setChecked(boolean z) {
        RotationPolicy.setRotationLockForAccessibility(this.mContext, !z);
        return true;
    }

    public int getAvailabilityStatus() {
        return (!RotationPolicy.isRotationSupported(this.mContext) || DeviceStateAutoRotationHelper.isDeviceStateRotationEnabledForA11y(this.mContext)) ? 3 : 0;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_accessibility;
    }

    public void onStop() {
        RotationPolicy.RotationPolicyListener rotationPolicyListener = this.mRotationPolicyListener;
        if (rotationPolicyListener != null) {
            RotationPolicy.unregisterRotationPolicyListener(this.mContext, rotationPolicyListener);
        }
    }

    public void onStart() {
        if (this.mRotationPolicyListener == null) {
            this.mRotationPolicyListener = new RotationPolicy.RotationPolicyListener() {
                public void onChange() {
                    if (LockScreenRotationPreferenceController.this.mPreference != null) {
                        LockScreenRotationPreferenceController lockScreenRotationPreferenceController = LockScreenRotationPreferenceController.this;
                        lockScreenRotationPreferenceController.updateState(lockScreenRotationPreferenceController.mPreference);
                    }
                }
            };
        }
        RotationPolicy.registerRotationPolicyListener(this.mContext, this.mRotationPolicyListener);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
        super.displayPreference(preferenceScreen);
    }
}
