package tech.nothing.settingslib.mobile;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;
import com.android.settingslib.Utils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;

public class SignalSmoothImpl extends Handler implements SignalSmooth {
    private static final boolean DBG = true;
    public static final SignalSmooth EMPTY_IMPL = new SignalSmooth() {
        public boolean delayNotifyOos(ServiceState serviceState) {
            return false;
        }

        public void setListening(boolean z) {
        }

        public boolean smoothSignal(SignalStrength signalStrength) {
            return false;
        }

        public int getSmoothLevel(SignalStrength signalStrength) {
            return signalStrength.getLevel();
        }
    };
    private static final int EVENT_OOS_DELAY_TIMEOUT = 1000;
    private static final int EVENT_SIGNAL_SMOOTH = 1001;
    private static final int EVENT_START = 1002;
    private static final int LEVEL_INVALID = -1;
    private static final int LEVEL_MIN = 1;
    private static final int OOS_DELAY_TIMEOUT_TIMER = 15000;
    private static final int SIGNAL_SMOOTH_TIMER = 15000;
    private static final String TAG = "SignalSmoothImpl";
    private static final int TIMER_TO_ENABLE_SMOOTH = 10000;
    private SignalStrength mLastSignalStrength;
    private ServiceState mLastSs;
    private final OnDelayTimeoutListener mListener;
    private int mSmoothLevel = -1;
    private State mState;
    private final int mSubId;

    public interface OnDelayTimeoutListener {
        void onDelayTimeout(ServiceState serviceState);

        void onSmoothSignal(SignalStrength signalStrength);
    }

    private enum State {
        IDLE,
        STARTING,
        STARTED,
        RADIO_POWER_OFF
    }

    private String eventToString(int i) {
        switch (i) {
            case 1000:
                return "OOS_DELAY_TIMEOUT_TIMER";
            case 1001:
                return "EVENT_SIGNAL_SMOOTH";
            case 1002:
                return "EVENT_START";
            default:
                return "UNKOWN EVENT";
        }
    }

    public SignalSmoothImpl(Looper looper, int i, OnDelayTimeoutListener onDelayTimeoutListener) {
        super(looper);
        this.mSubId = i;
        this.mListener = onDelayTimeoutListener;
        sendEmptyMessageDelayed(1002, 10000);
        this.mState = State.STARTING;
        log("create.");
    }

    public void handleMessage(Message message) {
        log("handleMessage: " + eventToString(message.what));
        switch (message.what) {
            case 1000:
                this.mListener.onDelayTimeout(this.mLastSs);
                return;
            case 1001:
                log("current smooth level: " + this.mSmoothLevel);
                SignalStrength signalStrength = this.mLastSignalStrength;
                int level = signalStrength != null ? signalStrength.getLevel() : 0;
                int i = this.mSmoothLevel - 1;
                this.mSmoothLevel = i;
                if (i > level) {
                    sendEmptyMessageDelayed(1001, 15000);
                } else {
                    log("smooth level == last level, stop smooth");
                    this.mSmoothLevel = -1;
                }
                this.mListener.onSmoothSignal(this.mLastSignalStrength);
                return;
            case 1002:
                this.mState = State.STARTED;
                return;
            default:
                return;
        }
    }

    public boolean delayNotifyOos(ServiceState serviceState) {
        int combinedServiceState = Utils.getCombinedServiceState(serviceState);
        int combinedServiceState2 = Utils.getCombinedServiceState(this.mLastSs);
        this.mLastSs = serviceState;
        boolean hasMessages = hasMessages(1000);
        log("delayNotifyOos: newState: " + combinedServiceState + " lastState: " + combinedServiceState2 + " delaying: " + hasMessages + " state: " + this.mState);
        if (combinedServiceState == 3) {
            log("delayNotifyOos: radio power off, stop oos delay and signal smooth");
            if (this.mState != State.RADIO_POWER_OFF) {
                this.mState = State.RADIO_POWER_OFF;
                reset("ss state power off.");
            }
            return false;
        } else if (this.mState != State.STARTED) {
            if (this.mState == State.RADIO_POWER_OFF) {
                log("delayNotifyOos: radio power leave off, delay 10s to start smooth");
                this.mState = State.STARTING;
                sendEmptyMessageDelayed(1002, 10000);
            }
            return false;
        } else if (!hasMessages) {
            if (combinedServiceState2 == 0 && combinedServiceState != 0) {
                log("delayNotifyOos: begin oos delay");
                sendEmptyMessageDelayed(1000, 15000);
                return true;
            }
            log("delayNotifyOos: ss not change, do nothing.");
            return false;
        } else if (combinedServiceState == 0) {
            log("delayNotifyOos: OOS -> SERVICE, stop oos delay");
            removeMessages(1000);
            return false;
        } else {
            log("delayNotifyOos: keep oos");
            return true;
        }
    }

    public boolean smoothSignal(SignalStrength signalStrength) {
        if (this.mState != State.STARTED) {
            log("smoothSignal: state not started.");
            return false;
        }
        SignalStrength signalStrength2 = this.mLastSignalStrength;
        int level = signalStrength2 != null ? signalStrength2.getLevel() : 0;
        int level2 = signalStrength != null ? signalStrength.getLevel() : 0;
        this.mLastSignalStrength = signalStrength;
        log("smoothSignal: last level: " + level + " smooth level: " + this.mSmoothLevel + " new level: " + level2);
        if (hasMessages(1001)) {
            if (level2 < this.mSmoothLevel) {
                log("smoothSignal: smoothing, new level < smooth level.");
                return true;
            }
            log("smoothSignal: smoothing, new level >= smooth level, stop");
            removeMessages(1001);
            this.mSmoothLevel = -1;
            return false;
        } else if (level2 < level) {
            log("smoothSignal: begin signal smooth");
            this.mSmoothLevel = level;
            sendEmptyMessageDelayed(1001, 15000);
            return true;
        } else {
            log("smoothSignal: new level >= last, no action.");
            return false;
        }
    }

    public int getSmoothLevel(SignalStrength signalStrength) {
        int i = this.mSmoothLevel;
        if (i == -1) {
            i = signalStrength.getLevel();
        }
        if (i < 1) {
            i = 1;
        }
        logv("getSmoothLevel: " + i + " mSmoothLevel " + this.mSmoothLevel);
        return i;
    }

    public void setListening(boolean z) {
        log("setListening: " + z + " state:" + this.mState);
        if (!z) {
            this.mState = State.IDLE;
            reset("un-listening");
        } else if (this.mState == State.IDLE) {
            sendEmptyMessageDelayed(1002, 10000);
            this.mState = State.STARTING;
        }
    }

    private void reset(String str) {
        log("reset, reason: " + str);
        removeCallbacksAndMessages((Object) null);
        this.mLastSs = null;
        this.mLastSignalStrength = null;
        this.mSmoothLevel = -1;
    }

    private void log(String str) {
        Log.d(TAG, NavigationBarInflaterView.SIZE_MOD_START + this.mSubId + "] " + str);
    }

    private void logv(String str) {
        log(str);
    }
}
