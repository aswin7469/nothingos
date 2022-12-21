package com.android.systemui.power;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IThermalEventListener;
import android.os.IThermalService;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.Temperature;
import android.provider.Settings;
import android.util.Log;
import android.util.Slog;
import com.android.settingslib.fuelgauge.Estimate;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.C1893R;
import com.android.systemui.CoreStartable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.nothing.systemui.power.PowerUIEx;
import dagger.Lazy;
import java.p026io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Future;
import javax.inject.Inject;

@SysUISingleton
public class PowerUI extends CoreStartable implements CommandQueue.Callbacks {
    private static final String BOOT_COUNT_KEY = "boot_count";
    private static final int CHARGE_CYCLE_PERCENT_RESET = 30;
    static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int MAX_RECENT_TEMPS = 125;
    public static final int NO_ESTIMATE_AVAILABLE = -1;
    private static final String PREFS = "powerui_prefs";
    static final String TAG = "PowerUI";
    private static final long TEMPERATURE_INTERVAL = 30000;
    private static final long TEMPERATURE_LOGGING_INTERVAL = 3600000;
    static final long THREE_HOURS_IN_MILLIS = 10800000;
    int mBatteryLevel = 100;
    int mBatteryStatus = 1;
    /* access modifiers changed from: private */
    public final BroadcastDispatcher mBroadcastDispatcher;
    private final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    private final CommandQueue mCommandQueue;
    BatteryStateSnapshot mCurrentBatteryStateSnapshot;
    private boolean mEnableSkinTemperatureWarning;
    private boolean mEnableUsbTemperatureAlarm;
    private final EnhancedEstimates mEnhancedEstimates;
    /* access modifiers changed from: private */
    public final PowerUIEx mEx;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    public int mInvalidCharger = 0;
    BatteryStateSnapshot mLastBatteryStateSnapshot;
    private final Configuration mLastConfiguration = new Configuration();
    /* access modifiers changed from: private */
    public Future mLastShowWarningTask;
    /* access modifiers changed from: private */
    public int mLowBatteryAlertCloseLevel;
    /* access modifiers changed from: private */
    public final int[] mLowBatteryReminderLevels = new int[2];
    boolean mLowWarningShownThisChargeCycle;
    private InattentiveSleepWarningView mOverlayView;
    /* access modifiers changed from: private */
    public int mPlugType = 0;
    /* access modifiers changed from: private */
    public final PowerManager mPowerManager;
    final Receiver mReceiver = new Receiver();
    /* access modifiers changed from: private */
    public long mScreenOffTime = -1;
    boolean mSevereWarningShownThisChargeCycle;
    private IThermalEventListener mSkinThermalEventListener;
    IThermalService mThermalService;
    private IThermalEventListener mUsbThermalEventListener;
    /* access modifiers changed from: private */
    public final WarningsUI mWarnings;

    public interface WarningsUI extends PowerUIEx.WarningsUI {
        void dismissHighTemperatureWarning();

        void dismissInvalidChargerWarning();

        void dismissLowBatteryWarning();

        void dump(PrintWriter printWriter);

        boolean isInvalidChargerWarningShowing();

        void showHighTemperatureWarning();

        void showInvalidChargerWarning();

        void showLowBatteryWarning(boolean z);

        void showThermalShutdownWarning();

        void showUsbHighTemperatureAlarm();

        void update(int i, int i2, long j);

        void updateLowBatteryWarning();

        void updateSnapshot(BatteryStateSnapshot batteryStateSnapshot);

        void userSwitched();
    }

    @Inject
    public PowerUI(Context context, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Lazy<Optional<CentralSurfaces>> lazy, WarningsUI warningsUI, EnhancedEstimates enhancedEstimates, PowerManager powerManager) {
        super(context);
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mCommandQueue = commandQueue;
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mWarnings = warningsUI;
        this.mEnhancedEstimates = enhancedEstimates;
        this.mPowerManager = powerManager;
        this.mEx = new PowerUIEx(context, warningsUI, handler);
    }

    public void start() {
        this.mScreenOffTime = this.mPowerManager.isScreenOn() ? -1 : SystemClock.elapsedRealtime();
        this.mLastConfiguration.setTo(this.mContext.getResources().getConfiguration());
        C23061 r0 = new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                PowerUI.this.updateBatteryWarningLevels();
            }
        };
        ContentResolver contentResolver = this.mContext.getContentResolver();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("low_power_trigger_level"), false, r0, -1);
        updateBatteryWarningLevels();
        this.mReceiver.init();
        showWarnOnThermalShutdown();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("show_temperature_warning"), false, new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                PowerUI.this.doSkinThermalEventListenerRegistration();
            }
        });
        contentResolver.registerContentObserver(Settings.Global.getUriFor("show_usb_temperature_alarm"), false, new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                PowerUI.this.doUsbThermalEventListenerRegistration();
            }
        });
        initThermalEventListeners();
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        if ((this.mLastConfiguration.updateFrom(configuration) & 3) != 0) {
            this.mHandler.post(new PowerUI$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: package-private */
    public void updateBatteryWarningLevels() {
        int integer = this.mContext.getResources().getInteger(C1893R.integer.config_criticalBatteryWarningLevel);
        int integer2 = this.mContext.getResources().getInteger(C1893R.integer.config_lowBatteryWarningLevel);
        if (integer2 < integer) {
            integer2 = integer;
        }
        int[] iArr = this.mLowBatteryReminderLevels;
        iArr[0] = integer2;
        iArr[1] = integer;
        this.mLowBatteryAlertCloseLevel = integer2 + this.mContext.getResources().getInteger(17694858);
    }

    /* access modifiers changed from: private */
    public int findBatteryLevelBucket(int i) {
        if (i >= this.mLowBatteryAlertCloseLevel) {
            return 1;
        }
        int[] iArr = this.mLowBatteryReminderLevels;
        if (i > iArr[0]) {
            return 0;
        }
        for (int length = iArr.length - 1; length >= 0; length--) {
            if (i <= this.mLowBatteryReminderLevels[length]) {
                return -1 - length;
            }
        }
        throw new RuntimeException("not possible!");
    }

    final class Receiver extends BroadcastReceiver {
        private boolean mHasReceivedBattery = false;

        Receiver() {
        }

        public void init() {
            Intent registerReceiver;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
            intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            PowerUI.this.mBroadcastDispatcher.registerReceiverWithHandler(this, intentFilter, PowerUI.this.mHandler);
            if (!this.mHasReceivedBattery && (registerReceiver = PowerUI.this.mContext.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) != null) {
                onReceive(PowerUI.this.mContext, registerReceiver);
            }
        }

        public void onReceive(Context context, Intent intent) {
            PowerUI.this.mEx.onReceive(context, intent);
            String action = intent.getAction();
            if ("android.os.action.POWER_SAVE_MODE_CHANGED".equals(action)) {
                ThreadUtils.postOnBackgroundThread((Runnable) new PowerUI$Receiver$$ExternalSyntheticLambda0(this));
            } else if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                this.mHasReceivedBattery = true;
                int i = PowerUI.this.mBatteryLevel;
                PowerUI.this.mBatteryLevel = intent.getIntExtra("level", 100);
                int i2 = PowerUI.this.mBatteryStatus;
                PowerUI.this.mBatteryStatus = intent.getIntExtra("status", 1);
                int access$500 = PowerUI.this.mPlugType;
                int unused = PowerUI.this.mPlugType = intent.getIntExtra("plugged", 1);
                int access$600 = PowerUI.this.mInvalidCharger;
                int unused2 = PowerUI.this.mInvalidCharger = intent.getIntExtra("invalid_charger", 0);
                PowerUI powerUI = PowerUI.this;
                powerUI.mLastBatteryStateSnapshot = powerUI.mCurrentBatteryStateSnapshot;
                boolean z = PowerUI.this.mPlugType != 0;
                boolean z2 = access$500 != 0;
                int access$700 = PowerUI.this.findBatteryLevelBucket(i);
                PowerUI powerUI2 = PowerUI.this;
                int access$7002 = powerUI2.findBatteryLevelBucket(powerUI2.mBatteryLevel);
                if (PowerUI.DEBUG) {
                    Slog.d(PowerUI.TAG, "buckets   ....." + PowerUI.this.mLowBatteryAlertCloseLevel + " .. " + PowerUI.this.mLowBatteryReminderLevels[0] + " .. " + PowerUI.this.mLowBatteryReminderLevels[1]);
                    Slog.d(PowerUI.TAG, "level          " + i + " --> " + PowerUI.this.mBatteryLevel);
                    Slog.d(PowerUI.TAG, "status         " + i2 + " --> " + PowerUI.this.mBatteryStatus);
                    Slog.d(PowerUI.TAG, "plugType       " + access$500 + " --> " + PowerUI.this.mPlugType);
                    Slog.d(PowerUI.TAG, "invalidCharger " + access$600 + " --> " + PowerUI.this.mInvalidCharger);
                    Slog.d(PowerUI.TAG, "bucket         " + access$700 + " --> " + access$7002);
                    Slog.d(PowerUI.TAG, "plugged        " + z2 + " --> " + z);
                }
                PowerUI.this.mWarnings.update(PowerUI.this.mBatteryLevel, access$7002, PowerUI.this.mScreenOffTime);
                if (access$600 != 0 || PowerUI.this.mInvalidCharger == 0) {
                    if (access$600 != 0 && PowerUI.this.mInvalidCharger == 0) {
                        PowerUI.this.mWarnings.dismissInvalidChargerWarning();
                    } else if (PowerUI.this.mWarnings.isInvalidChargerWarningShowing()) {
                        if (PowerUI.DEBUG) {
                            Slog.d(PowerUI.TAG, "Bad Charger");
                            return;
                        }
                        return;
                    }
                    if (PowerUI.this.mLastShowWarningTask != null) {
                        PowerUI.this.mLastShowWarningTask.cancel(true);
                        if (PowerUI.DEBUG) {
                            Slog.d(PowerUI.TAG, "cancelled task");
                        }
                    }
                    Future unused3 = PowerUI.this.mLastShowWarningTask = ThreadUtils.postOnBackgroundThread((Runnable) new PowerUI$Receiver$$ExternalSyntheticLambda1(this, z, access$7002));
                    return;
                }
                Slog.d(PowerUI.TAG, "showing invalid charger warning");
                PowerUI.this.mWarnings.showInvalidChargerWarning();
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                long unused4 = PowerUI.this.mScreenOffTime = SystemClock.elapsedRealtime();
            } else if ("android.intent.action.SCREEN_ON".equals(action)) {
                long unused5 = PowerUI.this.mScreenOffTime = -1;
            } else if ("android.intent.action.USER_SWITCHED".equals(action)) {
                PowerUI.this.mWarnings.userSwitched();
            } else {
                Slog.w(PowerUI.TAG, "unknown intent: " + intent);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onReceive$0$com-android-systemui-power-PowerUI$Receiver  reason: not valid java name */
        public /* synthetic */ void m2860lambda$onReceive$0$comandroidsystemuipowerPowerUI$Receiver() {
            if (PowerUI.this.mPowerManager.isPowerSaveMode()) {
                PowerUI.this.mWarnings.dismissLowBatteryWarning();
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onReceive$1$com-android-systemui-power-PowerUI$Receiver  reason: not valid java name */
        public /* synthetic */ void m2861lambda$onReceive$1$comandroidsystemuipowerPowerUI$Receiver(boolean z, int i) {
            PowerUI.this.maybeShowBatteryWarningV2(z, i);
        }
    }

    /* access modifiers changed from: protected */
    public void maybeShowBatteryWarningV2(boolean z, int i) {
        boolean isHybridNotificationEnabled = this.mEnhancedEstimates.isHybridNotificationEnabled();
        boolean isPowerSaveMode = this.mPowerManager.isPowerSaveMode();
        boolean z2 = DEBUG;
        if (z2) {
            Slog.d(TAG, "evaluating which notification to show");
        }
        if (isHybridNotificationEnabled) {
            if (z2) {
                Slog.d(TAG, "using hybrid");
            }
            Estimate refreshEstimateIfNeeded = refreshEstimateIfNeeded();
            int i2 = this.mBatteryLevel;
            int i3 = this.mBatteryStatus;
            int[] iArr = this.mLowBatteryReminderLevels;
            this.mCurrentBatteryStateSnapshot = new BatteryStateSnapshot(i2, isPowerSaveMode, z, i, i3, iArr[1], iArr[0], refreshEstimateIfNeeded.getEstimateMillis(), refreshEstimateIfNeeded.getAverageDischargeTime(), this.mEnhancedEstimates.getSevereWarningThreshold(), this.mEnhancedEstimates.getLowWarningThreshold(), refreshEstimateIfNeeded.isBasedOnUsage(), this.mEnhancedEstimates.getLowWarningEnabled());
        } else {
            if (z2) {
                Slog.d(TAG, "using standard");
            }
            int i4 = this.mBatteryLevel;
            int i5 = this.mBatteryStatus;
            int[] iArr2 = this.mLowBatteryReminderLevels;
            this.mCurrentBatteryStateSnapshot = new BatteryStateSnapshot(i4, isPowerSaveMode, z, i, i5, iArr2[1], iArr2[0]);
        }
        this.mWarnings.updateSnapshot(this.mCurrentBatteryStateSnapshot);
        maybeShowHybridWarning(this.mCurrentBatteryStateSnapshot, this.mLastBatteryStateSnapshot);
    }

    /* access modifiers changed from: package-private */
    public Estimate refreshEstimateIfNeeded() {
        BatteryStateSnapshot batteryStateSnapshot = this.mLastBatteryStateSnapshot;
        if (batteryStateSnapshot != null && batteryStateSnapshot.getTimeRemainingMillis() != -1 && this.mBatteryLevel == this.mLastBatteryStateSnapshot.getBatteryLevel()) {
            return new Estimate(this.mLastBatteryStateSnapshot.getTimeRemainingMillis(), this.mLastBatteryStateSnapshot.isBasedOnUsage(), this.mLastBatteryStateSnapshot.getAverageTimeToDischargeMillis());
        }
        Estimate estimate = this.mEnhancedEstimates.getEstimate();
        if (DEBUG) {
            Slog.d(TAG, "updated estimate: " + estimate.getEstimateMillis());
        }
        return estimate;
    }

    /* access modifiers changed from: package-private */
    public void maybeShowHybridWarning(BatteryStateSnapshot batteryStateSnapshot, BatteryStateSnapshot batteryStateSnapshot2) {
        boolean z = false;
        if (batteryStateSnapshot.getBatteryLevel() >= 30) {
            this.mLowWarningShownThisChargeCycle = false;
            this.mSevereWarningShownThisChargeCycle = false;
            if (DEBUG) {
                Slog.d(TAG, "Charge cycle reset! Can show warnings again");
            }
        }
        if (batteryStateSnapshot.getBucket() != batteryStateSnapshot2.getBucket() || batteryStateSnapshot2.getPlugged()) {
            z = true;
        }
        if (shouldShowHybridWarning(batteryStateSnapshot)) {
            this.mWarnings.showLowBatteryWarning(z);
            if (batteryStateSnapshot.getBatteryLevel() <= batteryStateSnapshot.getSevereLevelThreshold()) {
                this.mSevereWarningShownThisChargeCycle = true;
                this.mLowWarningShownThisChargeCycle = true;
                if (DEBUG) {
                    Slog.d(TAG, "Severe warning marked as shown this cycle");
                    return;
                }
                return;
            }
            Slog.d(TAG, "Low warning marked as shown this cycle");
            this.mLowWarningShownThisChargeCycle = true;
        } else if (shouldDismissHybridWarning(batteryStateSnapshot)) {
            if (DEBUG) {
                Slog.d(TAG, "Dismissing warning");
            }
            this.mWarnings.dismissLowBatteryWarning();
        } else {
            if (DEBUG) {
                Slog.d(TAG, "Updating warning");
            }
            this.mWarnings.updateLowBatteryWarning();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean shouldShowHybridWarning(BatteryStateSnapshot batteryStateSnapshot) {
        boolean z = false;
        boolean z2 = true;
        if (batteryStateSnapshot.getPlugged() || batteryStateSnapshot.getBatteryStatus() == 1) {
            StringBuilder append = new StringBuilder("can't show warning due to - plugged: ").append(batteryStateSnapshot.getPlugged()).append(" status unknown: ");
            if (batteryStateSnapshot.getBatteryStatus() != 1) {
                z2 = false;
            }
            Slog.d(TAG, append.append(z2).toString());
            return false;
        }
        boolean z3 = !this.mLowWarningShownThisChargeCycle && !batteryStateSnapshot.isPowerSaver() && batteryStateSnapshot.getBatteryLevel() <= batteryStateSnapshot.getLowLevelThreshold();
        boolean z4 = !this.mSevereWarningShownThisChargeCycle && batteryStateSnapshot.getBatteryLevel() <= batteryStateSnapshot.getSevereLevelThreshold();
        if (z3 || z4) {
            z = true;
        }
        if (DEBUG) {
            Slog.d(TAG, "Enhanced trigger is: " + z + "\nwith battery snapshot: mLowWarningShownThisChargeCycle: " + this.mLowWarningShownThisChargeCycle + " mSevereWarningShownThisChargeCycle: " + this.mSevereWarningShownThisChargeCycle + "\n" + batteryStateSnapshot.toString());
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldDismissHybridWarning(BatteryStateSnapshot batteryStateSnapshot) {
        return batteryStateSnapshot.getPlugged() || batteryStateSnapshot.getBatteryLevel() > batteryStateSnapshot.getLowLevelThreshold();
    }

    /* access modifiers changed from: protected */
    public void maybeShowBatteryWarning(BatteryStateSnapshot batteryStateSnapshot, BatteryStateSnapshot batteryStateSnapshot2) {
        boolean z = batteryStateSnapshot.getBucket() != batteryStateSnapshot2.getBucket() || batteryStateSnapshot2.getPlugged();
        if (shouldShowLowBatteryWarning(batteryStateSnapshot, batteryStateSnapshot2)) {
            this.mWarnings.showLowBatteryWarning(z);
        } else if (shouldDismissLowBatteryWarning(batteryStateSnapshot, batteryStateSnapshot2)) {
            this.mWarnings.dismissLowBatteryWarning();
        } else {
            this.mWarnings.updateLowBatteryWarning();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean shouldShowLowBatteryWarning(BatteryStateSnapshot batteryStateSnapshot, BatteryStateSnapshot batteryStateSnapshot2) {
        return !batteryStateSnapshot.getPlugged() && !batteryStateSnapshot.isPowerSaver() && (batteryStateSnapshot.getBucket() < batteryStateSnapshot2.getBucket() || batteryStateSnapshot2.getPlugged()) && batteryStateSnapshot.getBucket() < 0 && batteryStateSnapshot.getBatteryStatus() != 1;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldDismissLowBatteryWarning(BatteryStateSnapshot batteryStateSnapshot, BatteryStateSnapshot batteryStateSnapshot2) {
        return batteryStateSnapshot.isPowerSaver() || batteryStateSnapshot.getPlugged() || (batteryStateSnapshot.getBucket() > batteryStateSnapshot2.getBucket() && batteryStateSnapshot.getBucket() > 0);
    }

    /* access modifiers changed from: private */
    public void initThermalEventListeners() {
        doSkinThermalEventListenerRegistration();
        doUsbThermalEventListenerRegistration();
    }

    /* access modifiers changed from: package-private */
    public synchronized void doSkinThermalEventListenerRegistration() {
        boolean z;
        boolean z2 = this.mEnableSkinTemperatureWarning;
        boolean z3 = true;
        boolean z4 = Settings.Global.getInt(this.mContext.getContentResolver(), "show_temperature_warning", this.mContext.getResources().getInteger(C1893R.integer.config_showTemperatureWarning)) != 0;
        this.mEnableSkinTemperatureWarning = z4;
        if (z4 != z2) {
            try {
                if (this.mSkinThermalEventListener == null) {
                    this.mSkinThermalEventListener = new SkinThermalEventListener();
                }
                if (this.mThermalService == null) {
                    this.mThermalService = IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice"));
                }
                if (this.mEnableSkinTemperatureWarning) {
                    z = this.mThermalService.registerThermalEventListenerWithType(this.mSkinThermalEventListener, 3);
                } else {
                    z = this.mThermalService.unregisterThermalEventListener(this.mSkinThermalEventListener);
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Exception while (un)registering skin thermal event listener.", e);
                z = false;
            }
            if (!z) {
                if (this.mEnableSkinTemperatureWarning) {
                    z3 = false;
                }
                this.mEnableSkinTemperatureWarning = z3;
                Slog.e(TAG, "Failed to register or unregister skin thermal event listener.");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void doUsbThermalEventListenerRegistration() {
        boolean z;
        boolean z2 = this.mEnableUsbTemperatureAlarm;
        boolean z3 = true;
        boolean z4 = Settings.Global.getInt(this.mContext.getContentResolver(), "show_usb_temperature_alarm", this.mContext.getResources().getInteger(C1893R.integer.config_showUsbPortAlarm)) != 0;
        this.mEnableUsbTemperatureAlarm = z4;
        if (z4 != z2) {
            try {
                if (this.mUsbThermalEventListener == null) {
                    this.mUsbThermalEventListener = new UsbThermalEventListener();
                }
                if (this.mThermalService == null) {
                    this.mThermalService = IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice"));
                }
                if (this.mEnableUsbTemperatureAlarm) {
                    z = this.mThermalService.registerThermalEventListenerWithType(this.mUsbThermalEventListener, 4);
                } else {
                    z = this.mThermalService.unregisterThermalEventListener(this.mUsbThermalEventListener);
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Exception while (un)registering usb thermal event listener.", e);
                z = false;
            }
            if (!z) {
                if (this.mEnableUsbTemperatureAlarm) {
                    z3 = false;
                }
                this.mEnableUsbTemperatureAlarm = z3;
                Slog.e(TAG, "Failed to register or unregister usb thermal event listener.");
            }
        }
    }

    private void showWarnOnThermalShutdown() {
        int i = -1;
        int i2 = this.mContext.getSharedPreferences(PREFS, 0).getInt(BOOT_COUNT_KEY, -1);
        try {
            i = Settings.Global.getInt(this.mContext.getContentResolver(), BOOT_COUNT_KEY);
        } catch (Settings.SettingNotFoundException unused) {
            Slog.e(TAG, "Failed to read system boot count from Settings.Global.BOOT_COUNT");
        }
        if (i > i2) {
            this.mContext.getSharedPreferences(PREFS, 0).edit().putInt(BOOT_COUNT_KEY, i).apply();
            if (this.mPowerManager.getLastShutdownReason() == 4) {
                this.mWarnings.showThermalShutdownWarning();
            }
        }
    }

    public void showInattentiveSleepWarning() {
        if (this.mOverlayView == null) {
            this.mOverlayView = new InattentiveSleepWarningView(this.mContext);
        }
        this.mOverlayView.show();
    }

    public void dismissInattentiveSleepWarning(boolean z) {
        InattentiveSleepWarningView inattentiveSleepWarningView = this.mOverlayView;
        if (inattentiveSleepWarningView != null) {
            inattentiveSleepWarningView.dismiss(z);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("mLowBatteryAlertCloseLevel=");
        printWriter.println(this.mLowBatteryAlertCloseLevel);
        printWriter.print("mLowBatteryReminderLevels=");
        printWriter.println(Arrays.toString(this.mLowBatteryReminderLevels));
        printWriter.print("mBatteryLevel=");
        printWriter.println(Integer.toString(this.mBatteryLevel));
        printWriter.print("mBatteryStatus=");
        printWriter.println(Integer.toString(this.mBatteryStatus));
        printWriter.print("mPlugType=");
        printWriter.println(Integer.toString(this.mPlugType));
        printWriter.print("mInvalidCharger=");
        printWriter.println(Integer.toString(this.mInvalidCharger));
        printWriter.print("mScreenOffTime=");
        printWriter.print(this.mScreenOffTime);
        if (this.mScreenOffTime >= 0) {
            printWriter.print(" (");
            printWriter.print(SystemClock.elapsedRealtime() - this.mScreenOffTime);
            printWriter.print(" ago)");
        }
        printWriter.println();
        printWriter.print("soundTimeout=");
        printWriter.println(Settings.Global.getInt(this.mContext.getContentResolver(), "low_battery_sound_timeout", 0));
        printWriter.print("bucket: ");
        printWriter.println(Integer.toString(findBatteryLevelBucket(this.mBatteryLevel)));
        printWriter.print("mEnableSkinTemperatureWarning=");
        printWriter.println(this.mEnableSkinTemperatureWarning);
        printWriter.print("mEnableUsbTemperatureAlarm=");
        printWriter.println(this.mEnableUsbTemperatureAlarm);
        this.mWarnings.dump(printWriter);
    }

    final class SkinThermalEventListener extends IThermalEventListener.Stub {
        SkinThermalEventListener() {
        }

        public void notifyThrottling(Temperature temperature) {
            PowerUI.this.mEx.notifyThrottling(temperature);
        }
    }

    final class UsbThermalEventListener extends IThermalEventListener.Stub {
        UsbThermalEventListener() {
        }

        public void notifyThrottling(Temperature temperature) {
            int status = temperature.getStatus();
            if (status >= 5) {
                PowerUI.this.mWarnings.showUsbHighTemperatureAlarm();
                Slog.d(PowerUI.TAG, "UsbThermalEventListener: notifyThrottling was called , current usb port status = " + status + ", temperature = " + temperature.getValue());
            }
        }
    }
}
