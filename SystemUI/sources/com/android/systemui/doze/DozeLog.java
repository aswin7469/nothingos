package com.android.systemui.doze;

import android.util.TimeUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.p026io.PrintWriter;
import javax.inject.Inject;

@SysUISingleton
public class DozeLog implements Dumpable {
    public static final int PULSE_REASON_DOCKING = 6;
    public static final int PULSE_REASON_INTENT = 0;
    public static final int PULSE_REASON_NONE = -1;
    public static final int PULSE_REASON_NOTIFICATION = 1;
    public static final int PULSE_REASON_SENSOR_LONG_PRESS = 5;
    public static final int PULSE_REASON_SENSOR_SIGMOTION = 2;
    public static final int PULSE_REASON_SENSOR_WAKE_REACH = 8;
    public static final int REASON_SENSOR_DOUBLE_TAP = 4;
    public static final int REASON_SENSOR_PICKUP = 3;
    public static final int REASON_SENSOR_QUICK_PICKUP = 11;
    public static final int REASON_SENSOR_TAP = 9;
    public static final int REASON_SENSOR_UDFPS_LONG_PRESS = 10;
    public static final int REASON_SENSOR_WAKE_UP_PRESENCE = 7;
    public static final int TOTAL_REASONS = 12;
    private SummaryStats mEmergencyCallStats;
    private final KeyguardUpdateMonitorCallback mKeyguardCallback = new KeyguardUpdateMonitorCallback() {
        public void onEmergencyCallAction() {
            DozeLog.this.traceEmergencyCall();
        }

        public void onKeyguardBouncerFullyShowingChanged(boolean z) {
            DozeLog.this.traceKeyguardBouncerChanged(z);
        }

        public void onStartedWakingUp() {
            DozeLog.this.traceScreenOn();
        }

        public void onFinishedGoingToSleep(int i) {
            DozeLog.this.traceScreenOff(i);
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            DozeLog.this.traceKeyguard(z);
        }
    };
    private final DozeLogger mLogger;
    private SummaryStats mNotificationPulseStats;
    private SummaryStats mPickupPulseNearVibrationStats;
    private SummaryStats mPickupPulseNotNearVibrationStats;
    private SummaryStats[][] mProxStats;
    private boolean mPulsing;
    private SummaryStats mScreenOnNotPulsingStats;
    private SummaryStats mScreenOnPulsingStats;
    /* access modifiers changed from: private */
    public long mSince;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Reason {
    }

    @Inject
    public DozeLog(KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, DozeLogger dozeLogger) {
        this.mLogger = dozeLogger;
        this.mSince = System.currentTimeMillis();
        this.mPickupPulseNearVibrationStats = new SummaryStats();
        this.mPickupPulseNotNearVibrationStats = new SummaryStats();
        this.mNotificationPulseStats = new SummaryStats();
        this.mScreenOnPulsingStats = new SummaryStats();
        this.mScreenOnNotPulsingStats = new SummaryStats();
        this.mEmergencyCallStats = new SummaryStats();
        this.mProxStats = (SummaryStats[][]) Array.newInstance((Class<?>) SummaryStats.class, 12, 2);
        for (int i = 0; i < 12; i++) {
            this.mProxStats[i][0] = new SummaryStats();
            this.mProxStats[i][1] = new SummaryStats();
        }
        if (keyguardUpdateMonitor != null) {
            keyguardUpdateMonitor.registerCallback(this.mKeyguardCallback);
        }
        dumpManager.registerDumpable("DumpStats", this);
    }

    public void tracePickupWakeUp(boolean z) {
        SummaryStats summaryStats;
        this.mLogger.logPickupWakeup(z);
        if (z) {
            summaryStats = this.mPickupPulseNearVibrationStats;
        } else {
            summaryStats = this.mPickupPulseNotNearVibrationStats;
        }
        summaryStats.append();
    }

    public void tracePulseStart(int i) {
        this.mLogger.logPulseStart(i);
        this.mPulsing = true;
    }

    public void tracePulseFinish() {
        this.mLogger.logPulseFinish();
        this.mPulsing = false;
    }

    public void traceNotificationPulse() {
        this.mLogger.logNotificationPulse();
        this.mNotificationPulseStats.append();
    }

    public void traceDozing(boolean z) {
        this.mLogger.logDozing(z);
        this.mPulsing = false;
    }

    public void traceDozingChanged(boolean z) {
        this.mLogger.logDozingChanged(z);
    }

    public void traceFling(boolean z, boolean z2, boolean z3, boolean z4) {
        this.mLogger.logFling(z, z2, z3, z4);
    }

    public void traceEmergencyCall() {
        this.mLogger.logEmergencyCall();
        this.mEmergencyCallStats.append();
    }

    public void traceKeyguardBouncerChanged(boolean z) {
        this.mLogger.logKeyguardBouncerChanged(z);
    }

    public void traceScreenOn() {
        this.mLogger.logScreenOn(this.mPulsing);
        (this.mPulsing ? this.mScreenOnPulsingStats : this.mScreenOnNotPulsingStats).append();
        this.mPulsing = false;
    }

    public void traceScreenOff(int i) {
        this.mLogger.logScreenOff(i);
    }

    public void traceMissedTick(String str) {
        this.mLogger.logMissedTick(str);
    }

    public void traceTimeTickScheduled(long j, long j2) {
        this.mLogger.logTimeTickScheduled(j, j2);
    }

    public void traceKeyguard(boolean z) {
        this.mLogger.logKeyguardVisibilityChange(z);
        if (!z) {
            this.mPulsing = false;
        }
    }

    public void traceState(DozeMachine.State state) {
        this.mLogger.logDozeStateChanged(state);
    }

    public void traceDozeStateSendComplete(DozeMachine.State state) {
        this.mLogger.logStateChangedSent(state);
    }

    public void traceDisplayStateDelayedByUdfps(int i) {
        this.mLogger.logDisplayStateDelayedByUdfps(i);
    }

    public void traceDisplayState(int i) {
        this.mLogger.logDisplayStateChanged(i);
    }

    public void traceWakeDisplay(boolean z, int i) {
        this.mLogger.logWakeDisplay(z, i);
    }

    public void traceProximityResult(boolean z, long j, int i) {
        this.mLogger.logProximityResult(z, j, i);
        this.mProxStats[i][!z].append();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        synchronized (DozeLog.class) {
            printWriter.print("  Doze summary stats (for ");
            TimeUtils.formatDuration(System.currentTimeMillis() - this.mSince, printWriter);
            printWriter.println("):");
            this.mPickupPulseNearVibrationStats.dump(printWriter, "Pickup pulse (near vibration)");
            this.mPickupPulseNotNearVibrationStats.dump(printWriter, "Pickup pulse (not near vibration)");
            this.mNotificationPulseStats.dump(printWriter, "Notification pulse");
            this.mScreenOnPulsingStats.dump(printWriter, "Screen on (pulsing)");
            this.mScreenOnNotPulsingStats.dump(printWriter, "Screen on (not pulsing)");
            this.mEmergencyCallStats.dump(printWriter, "Emergency call");
            for (int i = 0; i < 12; i++) {
                String reasonToString = reasonToString(i);
                this.mProxStats[i][0].dump(printWriter, "Proximity near (" + reasonToString + NavigationBarInflaterView.KEY_CODE_END);
                this.mProxStats[i][1].dump(printWriter, "Proximity far (" + reasonToString + NavigationBarInflaterView.KEY_CODE_END);
            }
        }
    }

    public void tracePostureChanged(int i, String str) {
        this.mLogger.logPostureChanged(i, str);
    }

    public void tracePulseDropped(boolean z, DozeMachine.State state, boolean z2) {
        this.mLogger.logPulseDropped(z, state, z2);
    }

    public void traceSensorEventDropped(int i, String str) {
        this.mLogger.logSensorEventDropped(i, str);
    }

    public void tracePulseDropped(String str) {
        this.mLogger.logPulseDropped(str);
    }

    public void tracePulseTouchDisabledByProx(boolean z) {
        this.mLogger.logPulseTouchDisabledByProx(z);
    }

    public void traceSensor(int i) {
        this.mLogger.logSensorTriggered(i);
    }

    public void traceAlwaysOnSuppressed(DozeMachine.State state, String str) {
        this.mLogger.logAlwaysOnSuppressed(state, str);
    }

    public void traceImmediatelyEndDoze(String str) {
        this.mLogger.logImmediatelyEndDoze(str);
    }

    public void tracePowerSaveChanged(boolean z, DozeMachine.State state) {
        this.mLogger.logPowerSaveChanged(z, state);
    }

    public void traceAlwaysOnSuppressedChange(boolean z, DozeMachine.State state) {
        this.mLogger.logAlwaysOnSuppressedChange(z, state);
    }

    public void traceDozeScreenBrightness(int i) {
        this.mLogger.logDozeScreenBrightness(i);
    }

    public void traceSetAodDimmingScrim(float f) {
        this.mLogger.logSetAodDimmingScrim((long) f);
    }

    private class SummaryStats {
        private int mCount;

        private SummaryStats() {
        }

        public void append() {
            this.mCount++;
        }

        public void dump(PrintWriter printWriter, String str) {
            if (this.mCount != 0) {
                printWriter.print("    ");
                printWriter.print(str);
                printWriter.print(": n=");
                printWriter.print(this.mCount);
                printWriter.print(" (");
                printWriter.print((((double) this.mCount) / ((double) (System.currentTimeMillis() - DozeLog.this.mSince))) * 1000.0d * 60.0d * 60.0d);
                printWriter.print("/hr)");
                printWriter.println();
            }
        }
    }

    public static String reasonToString(int i) {
        switch (i) {
            case 0:
                return "intent";
            case 1:
                return "notification";
            case 2:
                return "sigmotion";
            case 3:
                return "pickup";
            case 4:
                return "doubletap";
            case 5:
                return "longpress";
            case 6:
                return "docking";
            case 7:
                return "presence-wakeup";
            case 8:
                return "reach-wakelockscreen";
            case 9:
                return "tap";
            case 10:
                return "udfps";
            case 11:
                return "quickPickup";
            default:
                throw new IllegalArgumentException("invalid reason: " + i);
        }
    }
}
