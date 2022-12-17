package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import com.android.internal.view.RotationPolicy;
import com.android.settings.R$string;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.SettingsMainSwitchPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class AutoRotateSwitchBarController extends SettingsMainSwitchPreferenceController implements LifecycleObserver, OnStart, OnStop {
    private final MetricsFeatureProvider mMetricsFeatureProvider;
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

    public AutoRotateSwitchBarController(Context context, String str) {
        super(context, str);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public int getAvailabilityStatus() {
        return (!RotationPolicy.isRotationLockToggleVisible(this.mContext) || DeviceStateAutoRotationHelper.isDeviceStateRotationEnabled(this.mContext)) ? 3 : 0;
    }

    public void onStart() {
        if (this.mRotationPolicyListener == null) {
            this.mRotationPolicyListener = new RotationPolicy.RotationPolicyListener() {
                public void onChange() {
                    if (AutoRotateSwitchBarController.this.mSwitchPreference != null) {
                        AutoRotateSwitchBarController autoRotateSwitchBarController = AutoRotateSwitchBarController.this;
                        autoRotateSwitchBarController.updateState(autoRotateSwitchBarController.mSwitchPreference);
                    }
                }
            };
        }
        RotationPolicy.registerRotationPolicyListener(this.mContext, this.mRotationPolicyListener);
    }

    public void onStop() {
        RotationPolicy.RotationPolicyListener rotationPolicyListener = this.mRotationPolicyListener;
        if (rotationPolicyListener != null) {
            RotationPolicy.unregisterRotationPolicyListener(this.mContext, rotationPolicyListener);
        }
    }

    public boolean isChecked() {
        return !RotationPolicy.isRotationLocked(this.mContext);
    }

    public boolean setChecked(boolean z) {
        boolean z2 = !z;
        this.mMetricsFeatureProvider.action(this.mContext, 1753, z2);
        RotationPolicy.setRotationLock(this.mContext, z2);
        return true;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_display;
    }
}
