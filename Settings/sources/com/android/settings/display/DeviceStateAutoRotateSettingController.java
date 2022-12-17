package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.settingslib.search.SearchIndexableRaw;
import com.nothing.p006ui.support.NtCustSwitchPreference;
import java.util.List;

public class DeviceStateAutoRotateSettingController extends TogglePreferenceController implements LifecycleObserver {
    private final DeviceStateRotationLockSettingsManager mAutoRotateSettingsManager;
    private final int mDeviceState;
    private final String mDeviceStateDescription;
    private final DeviceStateRotationLockSettingsManager.DeviceStateRotationLockSettingsListener mDeviceStateRotationLockSettingsListener;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final int mOrder;
    private NtCustSwitchPreference mPreference;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public boolean isSliceable() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateState(this.mPreference);
    }

    DeviceStateAutoRotateSettingController(Context context, int i, String str, int i2, MetricsFeatureProvider metricsFeatureProvider) {
        super(context, getPreferenceKeyForDeviceState(i));
        this.mDeviceStateRotationLockSettingsListener = new DeviceStateAutoRotateSettingController$$ExternalSyntheticLambda0(this);
        this.mMetricsFeatureProvider = metricsFeatureProvider;
        this.mDeviceState = i;
        this.mDeviceStateDescription = str;
        this.mAutoRotateSettingsManager = DeviceStateRotationLockSettingsManager.getInstance(context);
        this.mOrder = i2;
    }

    public DeviceStateAutoRotateSettingController(Context context, int i, String str, int i2) {
        this(context, i, str, i2, FeatureFactory.getFactory(context).getMetricsFeatureProvider());
    }

    /* access modifiers changed from: package-private */
    public void init(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    /* access modifiers changed from: package-private */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mAutoRotateSettingsManager.registerListener(this.mDeviceStateRotationLockSettingsListener);
    }

    /* access modifiers changed from: package-private */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mAutoRotateSettingsManager.unregisterListener(this.mDeviceStateRotationLockSettingsListener);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        NtCustSwitchPreference ntCustSwitchPreference = new NtCustSwitchPreference(this.mContext);
        this.mPreference = ntCustSwitchPreference;
        ntCustSwitchPreference.setTitle((CharSequence) this.mDeviceStateDescription);
        this.mPreference.setKey(getPreferenceKey());
        this.mPreference.setOrder(this.mOrder);
        preferenceScreen.addPreference(this.mPreference);
        super.displayPreference(preferenceScreen);
    }

    public int getAvailabilityStatus() {
        return DeviceStateAutoRotationHelper.isDeviceStateRotationEnabled(this.mContext) ? 0 : 3;
    }

    public String getPreferenceKey() {
        return getPreferenceKeyForDeviceState(this.mDeviceState);
    }

    private static String getPreferenceKeyForDeviceState(int i) {
        return "auto_rotate_device_state_" + i;
    }

    public boolean isChecked() {
        return !this.mAutoRotateSettingsManager.isRotationLocked(this.mDeviceState);
    }

    public boolean setChecked(boolean z) {
        logSettingChanged(z);
        this.mAutoRotateSettingsManager.updateSetting(this.mDeviceState, !z);
        return true;
    }

    private void logSettingChanged(boolean z) {
        this.mMetricsFeatureProvider.action(this.mContext, 203, !z);
        this.mMetricsFeatureProvider.action(this.mContext, z ? 1790 : 1791, this.mDeviceState);
    }

    public void updateRawDataToIndex(List<SearchIndexableRaw> list) {
        SearchIndexableRaw searchIndexableRaw = new SearchIndexableRaw(this.mContext);
        searchIndexableRaw.key = getPreferenceKey();
        searchIndexableRaw.title = this.mDeviceStateDescription;
        searchIndexableRaw.screenTitle = this.mContext.getString(R$string.accelerometer_title);
        list.add(searchIndexableRaw);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_display;
    }
}
