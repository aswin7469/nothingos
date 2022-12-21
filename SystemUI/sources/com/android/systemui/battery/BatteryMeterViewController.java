package com.android.systemui.battery;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.ViewController;
import java.util.Objects;
import javax.inject.Inject;

public class BatteryMeterViewController extends ViewController<BatteryMeterView> {
    private final BatteryController mBatteryController;
    private final BatteryController.BatteryStateChangeCallback mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() {
        public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            ((BatteryMeterView) BatteryMeterViewController.this.mView).onBatteryLevelChanged(i, z);
        }

        public void onPowerSaveChanged(boolean z) {
            ((BatteryMeterView) BatteryMeterViewController.this.mView).onPowerSaveChanged(z);
        }

        public void onBatteryUnknownStateChanged(boolean z) {
            ((BatteryMeterView) BatteryMeterViewController.this.mView).onBatteryUnknownStateChanged(z);
        }
    };
    private final ConfigurationController mConfigurationController;
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onDensityOrFontScaleChanged() {
            ((BatteryMeterView) BatteryMeterViewController.this.mView).scaleBatteryMeterViews();
        }
    };
    private final ContentResolver mContentResolver;
    private final CurrentUserTracker mCurrentUserTracker;
    private boolean mIgnoreTunerUpdates;
    private boolean mIsSubscribedForTunerUpdates;
    /* access modifiers changed from: private */
    public final SettingObserver mSettingObserver;
    /* access modifiers changed from: private */
    public final String mSlotBattery;
    private final TunerService.Tunable mTunable = new TunerService.Tunable() {
        public void onTuningChanged(String str, String str2) {
            if (StatusBarIconController.ICON_HIDE_LIST.equals(str)) {
                ((BatteryMeterView) BatteryMeterViewController.this.mView).setVisibility(StatusBarIconController.getIconHideList(BatteryMeterViewController.this.getContext(), str2).contains(BatteryMeterViewController.this.mSlotBattery) ? 8 : 0);
            }
        }
    };
    private final TunerService mTunerService;

    @Inject
    public BatteryMeterViewController(BatteryMeterView batteryMeterView, ConfigurationController configurationController, TunerService tunerService, BroadcastDispatcher broadcastDispatcher, @Main Handler handler, final ContentResolver contentResolver, BatteryController batteryController) {
        super(batteryMeterView);
        this.mConfigurationController = configurationController;
        this.mTunerService = tunerService;
        this.mContentResolver = contentResolver;
        this.mBatteryController = batteryController;
        Objects.requireNonNull(batteryController);
        ((BatteryMeterView) this.mView).setBatteryEstimateFetcher(new BatteryMeterViewController$$ExternalSyntheticLambda0(batteryController));
        this.mSlotBattery = getResources().getString(17041555);
        this.mSettingObserver = new SettingObserver(handler);
        this.mCurrentUserTracker = new CurrentUserTracker(broadcastDispatcher) {
            public void onUserSwitched(int i) {
                contentResolver.unregisterContentObserver(BatteryMeterViewController.this.mSettingObserver);
                BatteryMeterViewController.this.registerShowBatteryPercentObserver(i);
                ((BatteryMeterView) BatteryMeterViewController.this.mView).updateShowPercent();
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        subscribeForTunerUpdates();
        this.mBatteryController.addCallback(this.mBatteryStateChangeCallback);
        registerShowBatteryPercentObserver(ActivityManager.getCurrentUser());
        registerGlobalBatteryUpdateObserver();
        this.mCurrentUserTracker.startTracking();
        ((BatteryMeterView) this.mView).updateShowPercent();
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        unsubscribeFromTunerUpdates();
        this.mBatteryController.removeCallback(this.mBatteryStateChangeCallback);
        this.mCurrentUserTracker.stopTracking();
        this.mContentResolver.unregisterContentObserver(this.mSettingObserver);
    }

    public void ignoreTunerUpdates() {
        this.mIgnoreTunerUpdates = true;
        unsubscribeFromTunerUpdates();
    }

    private void subscribeForTunerUpdates() {
        if (!this.mIsSubscribedForTunerUpdates && !this.mIgnoreTunerUpdates) {
            this.mTunerService.addTunable(this.mTunable, StatusBarIconController.ICON_HIDE_LIST);
            this.mIsSubscribedForTunerUpdates = true;
        }
    }

    private void unsubscribeFromTunerUpdates() {
        if (this.mIsSubscribedForTunerUpdates) {
            this.mTunerService.removeTunable(this.mTunable);
            this.mIsSubscribedForTunerUpdates = false;
        }
    }

    /* access modifiers changed from: private */
    public void registerShowBatteryPercentObserver(int i) {
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor("status_bar_show_battery_percent"), false, this.mSettingObserver, i);
    }

    private void registerGlobalBatteryUpdateObserver() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("battery_estimates_last_update_time"), false, this.mSettingObserver);
    }

    private final class SettingObserver extends ContentObserver {
        public SettingObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            ((BatteryMeterView) BatteryMeterViewController.this.mView).updateShowPercent();
            if (TextUtils.equals(uri.getLastPathSegment(), "battery_estimates_last_update_time")) {
                ((BatteryMeterView) BatteryMeterViewController.this.mView).updatePercentText();
            }
        }
    }
}
