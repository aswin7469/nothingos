package com.android.systemui.doze;

import android.view.Display;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.DozeLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u001f\n\u0002\u0010\t\n\u0002\b\u0017\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\bJ\u000e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0011J\u000e\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0011J\u000e\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\rJ\u000e\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\rJ\u0006\u0010\u001a\u001a\u00020\u0006J&\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\r2\u0006\u0010\u001e\u001a\u00020\r2\u0006\u0010\u001f\u001a\u00020\rJ\u000e\u0010 \u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\rJ\u000e\u0010#\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\rJ\u000e\u0010$\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\nJ\u0006\u0010&\u001a\u00020\u0006J\u000e\u0010'\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\rJ\u0016\u0010)\u001a\u00020\u00062\u0006\u0010*\u001a\u00020\u00112\u0006\u0010+\u001a\u00020\nJ\u0016\u0010,\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\bJ\u001e\u0010.\u001a\u00020\u00062\u0006\u0010/\u001a\u00020\r2\u0006\u00100\u001a\u0002012\u0006\u0010\t\u001a\u00020\u0011J\u001e\u00102\u001a\u00020\u00062\u0006\u00103\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u00104\u001a\u00020\rJ\u000e\u00102\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nJ\u0006\u00105\u001a\u00020\u0006J\u000e\u00106\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0011J\u000e\u00107\u001a\u00020\u00062\u0006\u00108\u001a\u00020\rJ\u000e\u00109\u001a\u00020\u00062\u0006\u0010:\u001a\u00020\u0011J\u000e\u0010;\u001a\u00020\u00062\u0006\u0010<\u001a\u00020\rJ\u0016\u0010=\u001a\u00020\u00062\u0006\u0010>\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010?\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0011J\u000e\u0010@\u001a\u00020\u00062\u0006\u0010A\u001a\u000201J\u000e\u0010B\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010C\u001a\u00020\u00062\u0006\u0010D\u001a\u0002012\u0006\u0010E\u001a\u000201J\u0016\u0010F\u001a\u00020\u00062\u0006\u0010G\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006H"}, mo64987d2 = {"Lcom/android/systemui/doze/DozeLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logAlwaysOnSuppressed", "", "state", "Lcom/android/systemui/doze/DozeMachine$State;", "reason", "", "logAlwaysOnSuppressedChange", "isAodSuppressed", "", "nextState", "logDisplayStateChanged", "displayState", "", "logDisplayStateDelayedByUdfps", "delayedDisplayState", "logDozeScreenBrightness", "brightness", "logDozeStateChanged", "logDozing", "isDozing", "logDozingChanged", "logEmergencyCall", "logFling", "expand", "aboveThreshold", "thresholdNeeded", "screenOnFromTouch", "logImmediatelyEndDoze", "logKeyguardBouncerChanged", "isShowing", "logKeyguardVisibilityChange", "logMissedTick", "delay", "logNotificationPulse", "logPickupWakeup", "isWithinVibrationThreshold", "logPostureChanged", "posture", "partUpdated", "logPowerSaveChanged", "powerSaveActive", "logProximityResult", "isNear", "millis", "", "logPulseDropped", "pulsePending", "blocked", "logPulseFinish", "logPulseStart", "logPulseTouchDisabledByProx", "disabled", "logScreenOff", "why", "logScreenOn", "isPulsing", "logSensorEventDropped", "sensorEvent", "logSensorTriggered", "logSetAodDimmingScrim", "scrimOpacity", "logStateChangedSent", "logTimeTickScheduled", "whenAt", "triggerAt", "logWakeDisplay", "isAwake", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DozeLogger.kt */
public final class DozeLogger {
    private final LogBuffer buffer;

    @Inject
    public DozeLogger(@DozeLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logPickupWakeup(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logPickupWakeup$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logPulseStart(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPulseStart$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logPulseFinish() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPulseFinish$2.INSTANCE));
    }

    public final void logNotificationPulse() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logNotificationPulse$2.INSTANCE));
    }

    public final void logDozing(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDozing$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logDozingChanged(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDozingChanged$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logPowerSaveChanged(boolean z, DozeMachine.State state) {
        Intrinsics.checkNotNullParameter(state, "nextState");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPowerSaveChanged$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setStr1(state.name());
        logBuffer.commit(obtain);
    }

    public final void logAlwaysOnSuppressedChange(boolean z, DozeMachine.State state) {
        Intrinsics.checkNotNullParameter(state, "nextState");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logAlwaysOnSuppressedChange$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setStr1(state.name());
        logBuffer.commit(obtain);
    }

    public final void logFling(boolean z, boolean z2, boolean z3, boolean z4) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logFling$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        obtain.setBool3(z3);
        obtain.setBool4(z4);
        logBuffer.commit(obtain);
    }

    public final void logEmergencyCall() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logEmergencyCall$2.INSTANCE));
    }

    public final void logKeyguardBouncerChanged(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logKeyguardBouncerChanged$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logScreenOn(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logScreenOn$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logScreenOff(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logScreenOff$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logMissedTick(String str) {
        Intrinsics.checkNotNullParameter(str, "delay");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.ERROR, DozeLogger$logMissedTick$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logTimeTickScheduled(long j, long j2) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logTimeTickScheduled$2.INSTANCE);
        obtain.setLong1(j);
        obtain.setLong2(j2);
        logBuffer.commit(obtain);
    }

    public final void logKeyguardVisibilityChange(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logKeyguardVisibilityChange$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logDozeStateChanged(DozeMachine.State state) {
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDozeStateChanged$2.INSTANCE);
        obtain.setStr1(state.name());
        logBuffer.commit(obtain);
    }

    public final void logStateChangedSent(DozeMachine.State state) {
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logStateChangedSent$2.INSTANCE);
        obtain.setStr1(state.name());
        logBuffer.commit(obtain);
    }

    public final void logDisplayStateDelayedByUdfps(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDisplayStateDelayedByUdfps$2.INSTANCE);
        obtain.setStr1(Display.stateToString(i));
        logBuffer.commit(obtain);
    }

    public final void logDisplayStateChanged(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDisplayStateChanged$2.INSTANCE);
        obtain.setStr1(Display.stateToString(i));
        logBuffer.commit(obtain);
    }

    public final void logWakeDisplay(boolean z, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logWakeDisplay$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logProximityResult(boolean z, long j, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logProximityResult$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setLong1(j);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logPostureChanged(int i, String str) {
        Intrinsics.checkNotNullParameter(str, "partUpdated");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPostureChanged$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPulseDropped(boolean z, DozeMachine.State state, boolean z2) {
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPulseDropped$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setStr1(state.name());
        obtain.setBool2(z2);
        logBuffer.commit(obtain);
    }

    public final void logSensorEventDropped(int i, String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logSensorEventDropped$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPulseDropped(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logPulseDropped$4.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPulseTouchDisabledByProx(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logPulseTouchDisabledByProx$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logSensorTriggered(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.DEBUG, DozeLogger$logSensorTriggered$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logAlwaysOnSuppressed(DozeMachine.State state, String str) {
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logAlwaysOnSuppressed$2.INSTANCE);
        obtain.setStr1(state.name());
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logImmediatelyEndDoze(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logImmediatelyEndDoze$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logDozeScreenBrightness(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logDozeScreenBrightness$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logSetAodDimmingScrim(long j) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("DozeLog", LogLevel.INFO, DozeLogger$logSetAodDimmingScrim$2.INSTANCE);
        obtain.setLong1(j);
        logBuffer.commit(obtain);
    }
}
