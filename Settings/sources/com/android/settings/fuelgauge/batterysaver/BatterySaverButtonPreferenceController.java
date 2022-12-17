package com.android.settings.fuelgauge.batterysaver;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.widget.Switch;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.fuelgauge.BatterySaverReceiver;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class BatterySaverButtonPreferenceController extends TogglePreferenceController implements OnMainSwitchChangeListener, LifecycleObserver, OnStart, OnStop, BatterySaverReceiver.BatterySaverListener {
    private static final long SWITCH_ANIMATION_DURATION = 350;
    private final BatterySaverReceiver mBatterySaverReceiver;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private final PowerManager mPowerManager;
    private MainSwitchPreference mPreference;

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

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BatterySaverButtonPreferenceController(Context context, String str) {
        super(context, str);
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        BatterySaverReceiver batterySaverReceiver = new BatterySaverReceiver(context);
        this.mBatterySaverReceiver = batterySaverReceiver;
        batterySaverReceiver.setBatterySaverListener(this);
    }

    public Uri getSliceUri() {
        return new Uri.Builder().scheme("content").authority("android.settings.slices").appendPath("action").appendPath("battery_saver").build();
    }

    public void onStart() {
        this.mBatterySaverReceiver.setListening(true);
    }

    public void onStop() {
        this.mBatterySaverReceiver.setListening(false);
        this.mHandler.removeCallbacksAndMessages((Object) null);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = mainSwitchPreference;
        mainSwitchPreference.addOnSwitchChangeListener(this);
        this.mPreference.updateStatus(isChecked());
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        if (z && Settings.Secure.getInt(this.mContext.getContentResolver(), "low_power_warning_acknowledged", 0) == 0) {
            this.mPreference.setChecked(false);
        }
        setChecked(z);
    }

    public boolean isChecked() {
        return this.mPowerManager.isPowerSaveMode();
    }

    public boolean setChecked(boolean z) {
        return BatterySaverUtils.setPowerSaveMode(this.mContext, z, true);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_battery;
    }

    public void onPowerSaveModeChanged() {
        this.mHandler.postDelayed(new BatterySaverButtonPreferenceController$$ExternalSyntheticLambda0(this), SWITCH_ANIMATION_DURATION);
    }

    /* access modifiers changed from: private */
    /* renamed from: onPowerSaveModeChangedInternal */
    public void lambda$onPowerSaveModeChanged$0() {
        boolean isChecked = isChecked();
        MainSwitchPreference mainSwitchPreference = this.mPreference;
        if (mainSwitchPreference != null && mainSwitchPreference.isChecked() != isChecked) {
            this.mPreference.setChecked(isChecked);
        }
    }

    public void onBatteryChanged(boolean z) {
        MainSwitchPreference mainSwitchPreference = this.mPreference;
        if (mainSwitchPreference != null) {
            mainSwitchPreference.setEnabled(!z);
        }
    }
}
