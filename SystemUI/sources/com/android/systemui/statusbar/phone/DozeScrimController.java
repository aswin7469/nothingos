package com.android.systemui.statusbar.phone;

import android.os.Handler;
import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.ScrimController;
import javax.inject.Inject;

@SysUISingleton
public class DozeScrimController implements StatusBarStateController.StateListener {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "DozeScrimController";
    private final DozeLog mDozeLog;
    /* access modifiers changed from: private */
    public final DozeParameters mDozeParameters;
    /* access modifiers changed from: private */
    public boolean mDozing;
    /* access modifiers changed from: private */
    public boolean mFullyPulsing;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    private DozeHost.PulseCallback mPulseCallback;
    /* access modifiers changed from: private */
    public final Runnable mPulseOut = new Runnable() {
        public void run() {
            boolean unused = DozeScrimController.this.mFullyPulsing = false;
            DozeScrimController.this.mHandler.removeCallbacks(DozeScrimController.this.mPulseOut);
            DozeScrimController.this.mHandler.removeCallbacks(DozeScrimController.this.mPulseOutExtended);
            if (DozeScrimController.DEBUG) {
                Log.d(DozeScrimController.TAG, "Pulse out, mDozing=" + DozeScrimController.this.mDozing);
            }
            if (DozeScrimController.this.mDozing) {
                DozeScrimController.this.pulseFinished();
            }
        }
    };
    /* access modifiers changed from: private */
    public final Runnable mPulseOutExtended = new Runnable() {
        public void run() {
            DozeScrimController.this.mHandler.removeCallbacks(DozeScrimController.this.mPulseOut);
            DozeScrimController.this.mPulseOut.run();
        }
    };
    /* access modifiers changed from: private */
    public int mPulseReason;
    /* access modifiers changed from: private */
    public boolean mRequestPulsing;
    private final ScrimController.Callback mScrimCallback = new ScrimController.Callback() {
        public void onDisplayBlanked() {
            if (DozeScrimController.DEBUG) {
                Log.d(DozeScrimController.TAG, "Pulse in, mDozing=" + DozeScrimController.this.mDozing + " mPulseReason=" + DozeLog.reasonToString(DozeScrimController.this.mPulseReason) + ", requestPulsing=" + DozeScrimController.this.mRequestPulsing);
            }
            if (DozeScrimController.this.mDozing && DozeScrimController.this.mRequestPulsing) {
                DozeScrimController.this.pulseStarted();
            }
        }

        public void onFinished() {
            if (DozeScrimController.DEBUG) {
                Log.d(DozeScrimController.TAG, "Pulse in finished, mDozing=" + DozeScrimController.this.mDozing);
            }
            if (DozeScrimController.this.mDozing) {
                if (!(DozeScrimController.this.mPulseReason == 1 || DozeScrimController.this.mPulseReason == 6)) {
                    DozeScrimController.this.mHandler.postDelayed(DozeScrimController.this.mPulseOut, (long) DozeScrimController.this.mDozeParameters.getPulseVisibleDuration());
                    DozeScrimController.this.mHandler.postDelayed(DozeScrimController.this.mPulseOutExtended, (long) DozeScrimController.this.mDozeParameters.getPulseVisibleDurationExtended());
                }
                boolean unused = DozeScrimController.this.mFullyPulsing = true;
            }
        }

        public void onCancelled() {
            DozeScrimController.this.pulseFinished();
        }
    };

    public void onStateChanged(int i) {
    }

    @Inject
    public DozeScrimController(DozeParameters dozeParameters, DozeLog dozeLog, StatusBarStateController statusBarStateController) {
        this.mDozeParameters = dozeParameters;
        statusBarStateController.addCallback(this);
        this.mDozeLog = dozeLog;
    }

    public void setDozing(boolean z) {
        if (this.mDozing != z) {
            this.mDozing = z;
            if (!z) {
                cancelPulsing();
            }
        }
    }

    public void pulse(DozeHost.PulseCallback pulseCallback, int i) {
        if (pulseCallback != null) {
            boolean z = true;
            if (!this.mDozing || this.mPulseCallback != null) {
                if (DEBUG) {
                    StringBuilder append = new StringBuilder("Pulse suppressed. Dozing: ").append((Object) this.mDozeParameters).append(" had callback? ");
                    if (this.mPulseCallback == null) {
                        z = false;
                    }
                    Log.d(TAG, append.append(z).toString());
                }
                pulseCallback.onPulseFinished();
                if (!this.mDozing) {
                    this.mDozeLog.tracePulseDropped("device isn't dozing");
                } else {
                    this.mDozeLog.tracePulseDropped("already has pulse callback mPulseCallback=" + this.mPulseCallback);
                }
            } else {
                this.mRequestPulsing = true;
                this.mPulseCallback = pulseCallback;
                this.mPulseReason = i;
            }
        } else {
            throw new IllegalArgumentException("callback must not be null");
        }
    }

    public void pulseOutNow() {
        if (this.mPulseCallback != null && this.mFullyPulsing) {
            this.mPulseOut.run();
        }
    }

    public boolean isPulsing() {
        return this.mPulseCallback != null;
    }

    public boolean isDozing() {
        return this.mDozing;
    }

    public void extendPulse() {
        this.mHandler.removeCallbacks(this.mPulseOut);
    }

    public void cancelPendingPulseTimeout() {
        this.mHandler.removeCallbacks(this.mPulseOut);
        this.mHandler.removeCallbacks(this.mPulseOutExtended);
    }

    private void cancelPulsing() {
        if (this.mPulseCallback != null) {
            if (DEBUG) {
                Log.d(TAG, "Cancel pulsing");
            }
            this.mFullyPulsing = false;
            this.mHandler.removeCallbacks(this.mPulseOut);
            this.mHandler.removeCallbacks(this.mPulseOutExtended);
            pulseFinished();
        }
    }

    /* access modifiers changed from: private */
    public void pulseStarted() {
        this.mDozeLog.tracePulseStart(this.mPulseReason);
        DozeHost.PulseCallback pulseCallback = this.mPulseCallback;
        if (pulseCallback != null) {
            pulseCallback.onPulseStarted();
        }
    }

    /* access modifiers changed from: private */
    public void pulseFinished() {
        this.mRequestPulsing = false;
        this.mDozeLog.tracePulseFinish();
        DozeHost.PulseCallback pulseCallback = this.mPulseCallback;
        if (pulseCallback != null) {
            pulseCallback.onPulseFinished();
            this.mPulseCallback = null;
        }
    }

    public ScrimController.Callback getScrimCallback() {
        return this.mScrimCallback;
    }

    public void onDozingChanged(boolean z) {
        if (this.mDozing != z) {
            this.mDozeLog.traceDozingChanged(z);
        }
        setDozing(z);
    }
}
