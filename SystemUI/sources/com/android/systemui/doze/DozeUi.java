package com.android.systemui.doze;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.dagger.DozeScope;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.AlarmTimeout;
import com.android.systemui.util.wakelock.WakeLock;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import java.util.Calendar;
import java.util.Objects;
import javax.inject.Inject;

@DozeScope
public class DozeUi implements DozeMachine.Part {
    private static final boolean BURN_IN_TESTING_ENABLED = false;
    private static final long TIME_TICK_DEADLINE_MILLIS = 90000;
    private final boolean mCanAnimateTransition;
    private final Context mContext;
    private final DozeLog mDozeLog;
    private final DozeParameters mDozeParameters;
    private final Handler mHandler;
    private final DozeHost mHost;
    private final KeyguardUpdateMonitorCallback mKeyguardVisibilityCallback;
    private long mLastTimeTickElapsed = 0;
    /* access modifiers changed from: private */
    public DozeMachine mMachine;
    private final StatusBarStateController mStatusBarStateController;
    private final AlarmTimeout mTimeTicker;
    private final WakeLock mWakeLock;

    static /* synthetic */ void lambda$onTimeTick$0() {
    }

    @Inject
    public DozeUi(Context context, AlarmManager alarmManager, WakeLock wakeLock, DozeHost dozeHost, @Main Handler handler, DozeParameters dozeParameters, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarStateController statusBarStateController, DozeLog dozeLog) {
        C20701 r0 = new KeyguardUpdateMonitorCallback() {
            private static /* synthetic */ void lambda$onTimeChanged$0() {
            }

            public void onTimeChanged() {
            }
        };
        this.mKeyguardVisibilityCallback = r0;
        this.mContext = context;
        this.mWakeLock = wakeLock;
        this.mHost = dozeHost;
        this.mHandler = handler;
        this.mCanAnimateTransition = !dozeParameters.getDisplayNeedsBlanking();
        this.mDozeParameters = dozeParameters;
        this.mTimeTicker = new AlarmTimeout(alarmManager, new DozeUi$$ExternalSyntheticLambda2(this), "doze_time_tick", handler);
        keyguardUpdateMonitor.registerCallback(r0);
        this.mDozeLog = dozeLog;
        this.mStatusBarStateController = statusBarStateController;
    }

    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    private void pulseWhileDozing(final int i) {
        this.mHost.pulseWhileDozing(new DozeHost.PulseCallback() {
            public void onPulseStarted() {
                DozeMachine.State state;
                try {
                    DozeMachine access$000 = DozeUi.this.mMachine;
                    if (i == 8) {
                        state = DozeMachine.State.DOZE_PULSING_BRIGHT;
                    } else {
                        state = DozeMachine.State.DOZE_PULSING;
                    }
                    access$000.requestState(state);
                } catch (IllegalStateException unused) {
                }
            }

            public void onPulseFinished() {
                DozeUi.this.mMachine.requestState(DozeMachine.State.DOZE_PULSE_DONE);
            }
        }, i);
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        switch (C20723.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()]) {
            case 1:
            case 2:
                if (state == DozeMachine.State.DOZE_AOD_PAUSED || state == DozeMachine.State.DOZE) {
                    this.mHost.dozeTimeTick();
                    Handler handler = this.mHandler;
                    WakeLock wakeLock = this.mWakeLock;
                    DozeHost dozeHost = this.mHost;
                    Objects.requireNonNull(dozeHost);
                    handler.postDelayed(wakeLock.wrap(new DozeUi$$ExternalSyntheticLambda0(dozeHost)), 500);
                }
                scheduleTimeTick();
                break;
            case 3:
                scheduleTimeTick();
                break;
            case 4:
            case 5:
                unscheduleTimeTick();
                break;
            case 6:
                scheduleTimeTick();
                pulseWhileDozing(this.mMachine.getPulseReason());
                break;
            case 7:
                this.mHost.startDozing();
                break;
            case 8:
                this.mHost.stopDozing();
                unscheduleTimeTick();
                break;
        }
        updateAnimateWakeup(state2);
    }

    private void updateAnimateWakeup(DozeMachine.State state) {
        boolean z = true;
        switch (state) {
            case DOZE_REQUEST_PULSE:
            case DOZE_PULSING:
            case DOZE_PULSING_BRIGHT:
            case DOZE_PULSE_DONE:
                this.mHost.setAnimateWakeup(true);
                return;
            case FINISH:
                return;
            default:
                DozeHost dozeHost = this.mHost;
                if (!this.mCanAnimateTransition || (!this.mDozeParameters.getAlwaysOn() && !((CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class)).shouldPlayOnOffAnimation())) {
                    z = false;
                }
                dozeHost.setAnimateWakeup(z);
                return;
        }
    }

    private void scheduleTimeTick() {
        if (!this.mTimeTicker.isScheduled()) {
            long currentTimeMillis = System.currentTimeMillis();
            long roundToNextMinute = roundToNextMinute(currentTimeMillis) - System.currentTimeMillis();
            if (this.mTimeTicker.schedule(roundToNextMinute, 1)) {
                this.mDozeLog.traceTimeTickScheduled(currentTimeMillis, roundToNextMinute + currentTimeMillis);
            }
            this.mLastTimeTickElapsed = SystemClock.elapsedRealtime();
        }
    }

    private void unscheduleTimeTick() {
        if (this.mTimeTicker.isScheduled()) {
            verifyLastTimeTick();
            this.mTimeTicker.cancel();
        }
    }

    private void verifyLastTimeTick() {
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mLastTimeTickElapsed;
        if (elapsedRealtime > TIME_TICK_DEADLINE_MILLIS) {
            String formatShortElapsedTime = Formatter.formatShortElapsedTime(this.mContext, elapsedRealtime);
            this.mDozeLog.traceMissedTick(formatShortElapsedTime);
            Log.e("DozeMachine", "Missed AOD time tick by " + formatShortElapsedTime);
        }
    }

    private long roundToNextMinute(long j) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        instance.set(14, 0);
        instance.set(13, 0);
        instance.add(12, 1);
        return instance.getTimeInMillis();
    }

    /* access modifiers changed from: private */
    public void onTimeTick() {
        verifyLastTimeTick();
        this.mHost.dozeTimeTick();
        this.mHandler.post(this.mWakeLock.wrap(new DozeUi$$ExternalSyntheticLambda1()));
        scheduleTimeTick();
    }
}
