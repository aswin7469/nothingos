package com.android.systemui.tuner;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.core.app.NotificationCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeAvailabilityTracker;
import com.android.systemui.demomode.DemoModeController;
import com.nothing.p023os.device.DeviceConstant;
import com.nothing.systemui.p024qs.tiles.settings.panel.SettingContentRegistry;

public class DemoModeFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private static final String[] STATUS_ICONS = {"volume", SettingContentRegistry.BLUETOOTH_ITEM, "location", NotificationCompat.CATEGORY_ALARM, "zen", "sync", "tty", "eri", "mute", "speakerphone", "managed_profile"};
    private DemoModeController mDemoModeController;
    private Tracker mDemoModeTracker;
    private SwitchPreference mEnabledSwitch;
    private SwitchPreference mOnSwitch;

    public DemoModeFragment(DemoModeController demoModeController) {
        this.mDemoModeController = demoModeController;
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        Context context = getContext();
        SwitchPreference switchPreference = new SwitchPreference(context);
        this.mEnabledSwitch = switchPreference;
        switchPreference.setTitle((int) C1894R.string.enable_demo_mode);
        this.mEnabledSwitch.setOnPreferenceChangeListener(this);
        SwitchPreference switchPreference2 = new SwitchPreference(context);
        this.mOnSwitch = switchPreference2;
        switchPreference2.setTitle((int) C1894R.string.show_demo_mode);
        this.mOnSwitch.setEnabled(false);
        this.mOnSwitch.setOnPreferenceChangeListener(this);
        PreferenceScreen createPreferenceScreen = getPreferenceManager().createPreferenceScreen(context);
        createPreferenceScreen.addPreference(this.mEnabledSwitch);
        createPreferenceScreen.addPreference(this.mOnSwitch);
        setPreferenceScreen(createPreferenceScreen);
        Tracker tracker = new Tracker(context);
        this.mDemoModeTracker = tracker;
        tracker.startTracking();
        updateDemoModeEnabled();
        updateDemoModeOn();
        setHasOptionsMenu(true);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            getFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onResume() {
        super.onResume();
        MetricsLogger.visibility(getContext(), 229, true);
    }

    public void onPause() {
        super.onPause();
        MetricsLogger.visibility(getContext(), 229, false);
    }

    public void onDestroy() {
        this.mDemoModeTracker.stopTracking();
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void updateDemoModeEnabled() {
        this.mEnabledSwitch.setChecked(this.mDemoModeTracker.isDemoModeAvailable());
        this.mOnSwitch.setEnabled(this.mDemoModeTracker.isDemoModeAvailable());
    }

    /* access modifiers changed from: private */
    public void updateDemoModeOn() {
        this.mOnSwitch.setChecked(this.mDemoModeTracker.isInDemoMode());
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        boolean z = obj == Boolean.TRUE;
        if (preference == this.mEnabledSwitch) {
            if (!z) {
                this.mOnSwitch.setChecked(false);
                stopDemoMode();
            }
            MetricsLogger.action(getContext(), 235, z);
            this.mDemoModeController.requestSetDemoModeAllowed(z);
        } else if (preference != this.mOnSwitch) {
            return false;
        } else {
            MetricsLogger.action(getContext(), 236, z);
            if (z) {
                startDemoMode();
            } else {
                stopDemoMode();
            }
        }
        return true;
    }

    private void startDemoMode() {
        String str;
        Intent intent = new Intent(DemoMode.ACTION_DEMO);
        this.mDemoModeController.requestStartDemoMode();
        intent.putExtra(DemoMode.EXTRA_COMMAND, DemoMode.COMMAND_CLOCK);
        try {
            str = String.format("%02d00", Integer.valueOf(Integer.valueOf(Build.VERSION.RELEASE_OR_CODENAME.split("\\.")[0]).intValue() % 24));
        } catch (IllegalArgumentException unused) {
            str = "1010";
        }
        intent.putExtra("hhmm", str);
        getContext().sendBroadcast(intent);
        intent.putExtra(DemoMode.EXTRA_COMMAND, DemoMode.COMMAND_NETWORK);
        intent.putExtra("wifi", "show");
        intent.putExtra("mobile", "show");
        intent.putExtra("sims", "1");
        intent.putExtra("nosim", "false");
        intent.putExtra("level", DeviceConstant.NOISE_CANCELLATION_ADAPTIVE);
        intent.putExtra("datatype", "lte");
        getContext().sendBroadcast(intent);
        intent.putExtra("fully", "true");
        getContext().sendBroadcast(intent);
        intent.putExtra(DemoMode.EXTRA_COMMAND, DemoMode.COMMAND_BATTERY);
        intent.putExtra("level", "100");
        intent.putExtra("plugged", "false");
        getContext().sendBroadcast(intent);
        intent.putExtra(DemoMode.EXTRA_COMMAND, "status");
        for (String putExtra : STATUS_ICONS) {
            intent.putExtra(putExtra, "hide");
        }
        getContext().sendBroadcast(intent);
        intent.putExtra(DemoMode.EXTRA_COMMAND, DemoMode.COMMAND_NOTIFICATIONS);
        intent.putExtra("visible", "false");
        getContext().sendBroadcast(intent);
    }

    private void stopDemoMode() {
        this.mDemoModeController.requestFinishDemoMode();
    }

    private class Tracker extends DemoModeAvailabilityTracker {
        Tracker(Context context) {
            super(context);
        }

        public void onDemoModeAvailabilityChanged() {
            DemoModeFragment.this.updateDemoModeEnabled();
            DemoModeFragment.this.updateDemoModeOn();
        }

        public void onDemoModeStarted() {
            DemoModeFragment.this.updateDemoModeOn();
        }

        public void onDemoModeFinished() {
            DemoModeFragment.this.updateDemoModeOn();
        }
    }
}
