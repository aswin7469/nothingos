package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import com.android.settingslib.fuelgauge.Estimate;
import com.android.settingslib.utils.PowerUtil;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.statusbar.policy.BatteryController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class BatteryControllerImpl extends BroadcastReceiver implements BatteryController {
    private static final boolean DEBUG = Log.isLoggable("BatteryController", 3);
    private boolean mAodPowerSave;
    private final Handler mBgHandler;
    protected final BroadcastDispatcher mBroadcastDispatcher;
    private boolean mCharged;
    protected boolean mCharging;
    protected final Context mContext;
    private final DemoModeController mDemoModeController;
    private Estimate mEstimate;
    private final EnhancedEstimates mEstimates;
    protected int mLevel;
    private final Handler mMainHandler;
    protected boolean mPluggedIn;
    private boolean mPluggedInWireless;
    private final PowerManager mPowerManager;
    protected boolean mPowerSave;
    private boolean mWirelessCharging;
    protected final ArrayList<BatteryController.BatteryStateChangeCallback> mChangeCallbacks = new ArrayList<>();
    private final ArrayList<BatteryController.EstimateFetchCompletion> mFetchCallbacks = new ArrayList<>();
    private boolean mStateUnknown = false;
    private boolean mTestMode = false;
    @VisibleForTesting
    boolean mHasReceivedBattery = false;
    private boolean mFetchingEstimate = false;
    private final BatteryController.BatteryStateExt mBatterStateExt = new BatteryController.BatteryStateExt();

    @VisibleForTesting
    public BatteryControllerImpl(Context context, EnhancedEstimates enhancedEstimates, PowerManager powerManager, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, Handler handler, Handler handler2) {
        this.mContext = context;
        this.mMainHandler = handler;
        this.mBgHandler = handler2;
        this.mPowerManager = powerManager;
        this.mEstimates = enhancedEstimates;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDemoModeController = demoModeController;
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
        intentFilter.addAction("com.android.systemui.BATTERY_LEVEL_TEST");
        this.mBroadcastDispatcher.registerReceiver(this, intentFilter);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public void init() {
        Intent registerReceiver;
        registerReceiver();
        if (!this.mHasReceivedBattery && (registerReceiver = this.mContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) != null && !this.mHasReceivedBattery) {
            onReceive(this.mContext, registerReceiver);
        }
        this.mDemoModeController.addCallback((DemoMode) this);
        updatePowerSave();
        updateEstimate();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("BatteryController state:");
        printWriter.print("  mLevel=");
        printWriter.println(this.mLevel);
        printWriter.print("  mPluggedIn=");
        printWriter.println(this.mPluggedIn);
        printWriter.print("  mCharging=");
        printWriter.println(this.mCharging);
        printWriter.print("  mCharged=");
        printWriter.println(this.mCharged);
        printWriter.print("  mPowerSave=");
        printWriter.println(this.mPowerSave);
        printWriter.print("  mStateUnknown=");
        printWriter.println(this.mStateUnknown);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public void setPowerSaveMode(boolean z) {
        BatterySaverUtils.setPowerSaveMode(this.mContext, z, true);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(BatteryController.BatteryStateChangeCallback batteryStateChangeCallback) {
        synchronized (this.mChangeCallbacks) {
            this.mChangeCallbacks.add(batteryStateChangeCallback);
        }
        if (!this.mHasReceivedBattery) {
            return;
        }
        notifyCallbackForLevelChanged(batteryStateChangeCallback);
        batteryStateChangeCallback.onPowerSaveChanged(this.mPowerSave);
        batteryStateChangeCallback.onBatteryUnknownStateChanged(this.mStateUnknown);
        batteryStateChangeCallback.onWirelessChargingChanged(this.mWirelessCharging);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(BatteryController.BatteryStateChangeCallback batteryStateChangeCallback) {
        synchronized (this.mChangeCallbacks) {
            this.mChangeCallbacks.remove(batteryStateChangeCallback);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.intent.action.BATTERY_CHANGED")) {
            boolean z = false;
            if (this.mTestMode && !intent.getBooleanExtra("testmode", false)) {
                return;
            }
            this.mHasReceivedBattery = true;
            this.mLevel = (int) ((intent.getIntExtra("level", 0) * 100.0f) / intent.getIntExtra("scale", 100));
            this.mPluggedIn = intent.getIntExtra("plugged", 0) != 0;
            this.mPluggedInWireless = intent.getIntExtra("plugged", 0) == 4;
            updateBatterStateExt(intent);
            int intExtra = intent.getIntExtra("status", 1);
            boolean z2 = intExtra == 5;
            this.mCharged = z2;
            boolean z3 = z2 || intExtra == 2;
            this.mCharging = z3;
            boolean z4 = this.mWirelessCharging;
            if (z3 && intent.getIntExtra("plugged", 0) == 4) {
                z = true;
            }
            if (z4 != z) {
                this.mWirelessCharging = !this.mWirelessCharging;
                fireWirelessChargingChanged();
            }
            boolean z5 = !intent.getBooleanExtra("present", true);
            if (z5 != this.mStateUnknown) {
                this.mStateUnknown = z5;
                fireBatteryUnknownStateChanged();
            }
            fireBatteryLevelChanged();
        } else if (action.equals("android.os.action.POWER_SAVE_MODE_CHANGED")) {
            updatePowerSave();
        } else if (!action.equals("com.android.systemui.BATTERY_LEVEL_TEST")) {
        } else {
            this.mTestMode = true;
            this.mMainHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.BatteryControllerImpl.1
                int mSavedLevel;
                boolean mSavedPluggedIn;
                int mCurrentLevel = 0;
                int mIncrement = 1;
                Intent mTestIntent = new Intent("android.intent.action.BATTERY_CHANGED");

                {
                    this.mSavedLevel = BatteryControllerImpl.this.mLevel;
                    this.mSavedPluggedIn = BatteryControllerImpl.this.mPluggedIn;
                }

                @Override // java.lang.Runnable
                public void run() {
                    int i = this.mCurrentLevel;
                    int i2 = 0;
                    if (i < 0) {
                        BatteryControllerImpl.this.mTestMode = false;
                        this.mTestIntent.putExtra("level", this.mSavedLevel);
                        this.mTestIntent.putExtra("plugged", this.mSavedPluggedIn);
                        this.mTestIntent.putExtra("testmode", false);
                    } else {
                        this.mTestIntent.putExtra("level", i);
                        Intent intent2 = this.mTestIntent;
                        if (this.mIncrement > 0) {
                            i2 = 1;
                        }
                        intent2.putExtra("plugged", i2);
                        this.mTestIntent.putExtra("testmode", true);
                    }
                    context.sendBroadcast(this.mTestIntent);
                    if (!BatteryControllerImpl.this.mTestMode) {
                        return;
                    }
                    int i3 = this.mCurrentLevel;
                    int i4 = this.mIncrement;
                    int i5 = i3 + i4;
                    this.mCurrentLevel = i5;
                    if (i5 == 100) {
                        this.mIncrement = i4 * (-1);
                    }
                    BatteryControllerImpl.this.mMainHandler.postDelayed(this, 200L);
                }
            });
        }
    }

    private void fireWirelessChargingChanged() {
        synchronized (this.mChangeCallbacks) {
            this.mChangeCallbacks.forEach(new Consumer() { // from class: com.android.systemui.statusbar.policy.BatteryControllerImpl$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BatteryControllerImpl.this.lambda$fireWirelessChargingChanged$0((BatteryController.BatteryStateChangeCallback) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fireWirelessChargingChanged$0(BatteryController.BatteryStateChangeCallback batteryStateChangeCallback) {
        batteryStateChangeCallback.onWirelessChargingChanged(this.mWirelessCharging);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public boolean isPluggedIn() {
        return this.mPluggedIn;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public boolean isPowerSave() {
        return this.mPowerSave;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public boolean isAodPowerSave() {
        return this.mAodPowerSave;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public boolean isWirelessCharging() {
        return this.mWirelessCharging;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public boolean isPluggedInWireless() {
        return this.mPluggedInWireless;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public void getEstimatedTimeRemainingString(BatteryController.EstimateFetchCompletion estimateFetchCompletion) {
        synchronized (this.mFetchCallbacks) {
            this.mFetchCallbacks.add(estimateFetchCompletion);
        }
        updateEstimateInBackground();
    }

    private String generateTimeRemainingString() {
        synchronized (this.mFetchCallbacks) {
            Estimate estimate = this.mEstimate;
            if (estimate == null) {
                return null;
            }
            return PowerUtil.getBatteryRemainingShortStringFormatted(this.mContext, estimate.getEstimateMillis());
        }
    }

    private void updateEstimateInBackground() {
        if (this.mFetchingEstimate) {
            return;
        }
        this.mFetchingEstimate = true;
        this.mBgHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.BatteryControllerImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BatteryControllerImpl.this.lambda$updateEstimateInBackground$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEstimateInBackground$1() {
        synchronized (this.mFetchCallbacks) {
            this.mEstimate = null;
            if (this.mEstimates.isHybridNotificationEnabled()) {
                updateEstimate();
            }
        }
        this.mFetchingEstimate = false;
        this.mMainHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.BatteryControllerImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BatteryControllerImpl.this.notifyEstimateFetchCallbacks();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyEstimateFetchCallbacks() {
        synchronized (this.mFetchCallbacks) {
            String generateTimeRemainingString = generateTimeRemainingString();
            Iterator<BatteryController.EstimateFetchCompletion> it = this.mFetchCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onBatteryRemainingEstimateRetrieved(generateTimeRemainingString);
            }
            this.mFetchCallbacks.clear();
        }
    }

    private void updateEstimate() {
        Estimate cachedEstimateIfAvailable = Estimate.getCachedEstimateIfAvailable(this.mContext);
        this.mEstimate = cachedEstimateIfAvailable;
        if (cachedEstimateIfAvailable == null) {
            Estimate estimate = this.mEstimates.getEstimate();
            this.mEstimate = estimate;
            if (estimate == null) {
                return;
            }
            Estimate.storeCachedEstimate(this.mContext, estimate);
        }
    }

    private void updatePowerSave() {
        setPowerSave(this.mPowerManager.isPowerSaveMode());
    }

    private void setPowerSave(boolean z) {
        if (z == this.mPowerSave) {
            return;
        }
        this.mPowerSave = z;
        this.mAodPowerSave = this.mPowerManager.getPowerSaveState(14).batterySaverEnabled;
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("Power save is ");
            sb.append(this.mPowerSave ? "on" : "off");
            Log.d("BatteryController", sb.toString());
        }
        firePowerSaveChanged();
    }

    protected void fireBatteryLevelChanged() {
        synchronized (this.mChangeCallbacks) {
            int size = this.mChangeCallbacks.size();
            for (int i = 0; i < size; i++) {
                notifyCallbackForLevelChanged(this.mChangeCallbacks.get(i));
            }
        }
    }

    private void fireBatteryUnknownStateChanged() {
        synchronized (this.mChangeCallbacks) {
            int size = this.mChangeCallbacks.size();
            for (int i = 0; i < size; i++) {
                this.mChangeCallbacks.get(i).onBatteryUnknownStateChanged(this.mStateUnknown);
            }
        }
    }

    private void firePowerSaveChanged() {
        synchronized (this.mChangeCallbacks) {
            int size = this.mChangeCallbacks.size();
            for (int i = 0; i < size; i++) {
                this.mChangeCallbacks.get(i).onPowerSaveChanged(this.mPowerSave);
            }
        }
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void dispatchDemoCommand(String str, Bundle bundle) {
        if (!this.mDemoModeController.isInDemoMode()) {
            return;
        }
        String string = bundle.getString("level");
        String string2 = bundle.getString("plugged");
        String string3 = bundle.getString("powersave");
        String string4 = bundle.getString("present");
        if (string != null) {
            this.mLevel = Math.min(Math.max(Integer.parseInt(string), 0), 100);
        }
        if (string2 != null) {
            this.mPluggedIn = Boolean.parseBoolean(string2);
        }
        if (string3 != null) {
            this.mPowerSave = string3.equals("true");
            firePowerSaveChanged();
        }
        if (string4 != null) {
            this.mStateUnknown = !string4.equals("true");
            fireBatteryUnknownStateChanged();
        }
        fireBatteryLevelChanged();
    }

    @Override // com.android.systemui.demomode.DemoMode
    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("battery");
        return arrayList;
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void onDemoModeStarted() {
        this.mBroadcastDispatcher.unregisterReceiver(this);
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void onDemoModeFinished() {
        registerReceiver();
        updatePowerSave();
    }

    private void notifyCallbackForLevelChanged(BatteryController.BatteryStateChangeCallback batteryStateChangeCallback) {
        if (batteryStateChangeCallback instanceof BatteryController.BatteryStateChangeCallbackExt) {
            ((BatteryController.BatteryStateChangeCallbackExt) batteryStateChangeCallback).onBatteryLevelChangedExt(this.mLevel, this.mPluggedIn, this.mCharging, this.mBatterStateExt);
        } else {
            batteryStateChangeCallback.onBatteryLevelChanged(this.mLevel, this.mPluggedIn, this.mCharging);
        }
    }

    private void updateBatterStateExt(Intent intent) {
        int intExtra = intent.getIntExtra("status", 1);
        int intExtra2 = intent.getIntExtra("temperature", -1);
        int intExtra3 = intent.getIntExtra("voltage", -1);
        BatteryController.BatteryStateExt batteryStateExt = this.mBatterStateExt;
        batteryStateExt.status = intExtra;
        batteryStateExt.temperature = intExtra2;
        batteryStateExt.voltage = intExtra3;
        Log.i("BatteryController", "updateBatteryStateExt: " + this.mBatterStateExt);
    }
}
