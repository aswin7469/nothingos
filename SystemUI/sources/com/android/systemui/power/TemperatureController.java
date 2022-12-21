package com.android.systemui.power;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IThermalEventListener;
import android.os.IThermalService;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Temperature;
import android.util.Log;
import android.util.SparseIntArray;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.concurrent.CopyOnWriteArrayList;

@SysUISingleton
public class TemperatureController implements CallbackController<ShutdownTimeCallback> {
    public static final long DELAY_MS = 30000;
    public static final long INTERVAL_MS = 1000;
    private static final String TAG = "TemperatureController";
    /* access modifiers changed from: private */
    public boolean DEBUG = false;
    private final int DISMISS_DIALOG = 1;
    private final int SHOW_DIALOG = 0;
    private CountDownTimer mCountDownTimer = null;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.myLooper(), (Handler.Callback) null, true) {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                TemperatureController.this.mShutdownTimeCallback.onShutdownDialogShow((Temperature) message.obj);
                TemperatureController.this.startCountdown(TemperatureController.DELAY_MS, 1000);
            } else if (i == 1) {
                TemperatureController.this.cancelCountdown();
                TemperatureController.this.mShutdownTimeCallback.onCountdownEnd(TemperatureController.this.mIsCancel);
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsCancel;
    private CopyOnWriteArrayList<ShutdownTimeCallback> mListeners = new CopyOnWriteArrayList<>();
    /* access modifiers changed from: private */
    public ShutdownTimeCallback mShutdownTimeCallback;
    /* access modifiers changed from: private */
    public final SparseIntArray mTemperatureStatus = new SparseIntArray();
    private final IThermalEventListener mThermalEventListener = new ThermalEventListener();
    IThermalService mThermalService = IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice"));

    public interface ShutdownTimeCallback {
        void onCountdown(long j) {
        }

        void onCountdownEnd(boolean z) {
        }

        void onShutdownDialogShow(Temperature temperature) {
        }
    }

    final class ThermalEventListener extends IThermalEventListener.Stub {
        ThermalEventListener() {
        }

        public void notifyThrottling(Temperature temperature) throws RemoteException {
            Log.d(TemperatureController.TAG, "notifyThrottling name=" + temperature.getName() + ",value=" + temperature.getValue() + ",status=" + temperature.getStatus());
            if (temperature.getStatus() == 6 || (TemperatureController.this.DEBUG && "skin".equals(temperature.getName()))) {
                if (TemperatureController.this.mTemperatureStatus.size() == 0 && TemperatureController.this.mShutdownTimeCallback != null) {
                    TemperatureController.this.mHandler.sendMessage(TemperatureController.this.mHandler.obtainMessage(0, temperature));
                }
                TemperatureController.this.mTemperatureStatus.put(temperature.getType(), temperature.getStatus());
            } else if (TemperatureController.this.mTemperatureStatus.size() > 0) {
                TemperatureController.this.mTemperatureStatus.delete(temperature.getType());
                if (TemperatureController.this.mTemperatureStatus.size() == 0 && TemperatureController.this.mShutdownTimeCallback != null) {
                    boolean unused = TemperatureController.this.mIsCancel = true;
                    TemperatureController.this.mHandler.sendMessage(TemperatureController.this.mHandler.obtainMessage(1));
                }
            }
        }
    }

    public void startCountdown(long j, long j2) {
        C23091 r0 = new CountDownTimer(j, j2) {
            public void onTick(long j) {
                Log.e(TemperatureController.TAG, "onTick: " + j);
                TemperatureController.this.mShutdownTimeCallback.onCountdown(j / 1000);
            }

            public void onFinish() {
                TemperatureController.this.mShutdownTimeCallback.onCountdownEnd(TemperatureController.this.mIsCancel);
                if (TemperatureController.this.mIsCancel) {
                    boolean unused = TemperatureController.this.mIsCancel = false;
                }
            }
        };
        this.mCountDownTimer = r0;
        r0.start();
    }

    public void cancelCountdown() {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        } else {
            Log.e(TAG, "Timer was null");
        }
    }

    public void addCallback(ShutdownTimeCallback shutdownTimeCallback) {
        this.mShutdownTimeCallback = shutdownTimeCallback;
        try {
            this.mThermalService.registerThermalEventListener(this.mThermalEventListener);
            Log.d(TAG, "registerThermalEventListener");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removeCallback(ShutdownTimeCallback shutdownTimeCallback) {
        this.mShutdownTimeCallback = null;
        try {
            this.mThermalService.unregisterThermalEventListener(this.mThermalEventListener);
            Log.d(TAG, "registerThermalEventListener");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
