package com.android.systemui.keyguard;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Log;
import com.android.internal.policy.IKeyguardDrawnCallback;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.nothing.systemui.NTDependencyEx;
import javax.inject.Inject;

@SysUISingleton
public class KeyguardLifecyclesDispatcher {
    static final int FINISHED_GOING_TO_SLEEP = 7;
    static final int FINISHED_WAKING_UP = 5;
    static final int SCREEN_TURNED_OFF = 3;
    static final int SCREEN_TURNED_ON = 1;
    static final int SCREEN_TURNING_OFF = 2;
    static final int SCREEN_TURNING_ON = 0;
    static final int STARTED_GOING_TO_SLEEP = 6;
    static final int STARTED_WAKING_UP = 4;
    private static final String TAG = "KeyguardLifecyclesDispatcher";
    private ConfigurationController.ConfigurationListener mConfigListener = new ConfigurationController.ConfigurationListener() {
        public void onOrientationChanged(int i) {
            if (KeyguardLifecyclesDispatcher.this.mIsScreenTurningOn && i == 1) {
                boolean unused = KeyguardLifecyclesDispatcher.this.mIsScreenTurningOn = false;
                KeyguardLifecyclesDispatcher.this.mHandler.sendMessage(KeyguardLifecyclesDispatcher.this.mHandler.obtainMessage(0));
            }
        }
    };
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            final Object obj = message.obj;
            switch (message.what) {
                case 0:
                    if (((ConfigurationControllerImpl) NTDependencyEx.get(ConfigurationControllerImpl.class)).getOrientation() == 2) {
                        boolean unused = KeyguardLifecyclesDispatcher.this.mIsScreenTurningOn = true;
                        return;
                    }
                    Trace.beginSection("KeyguardLifecyclesDispatcher#SCREEN_TURNING_ON");
                    final int identityHashCode = System.identityHashCode(message);
                    Trace.beginAsyncSection("Waiting for KeyguardDrawnCallback#onDrawn", identityHashCode);
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurningOn(new Runnable() {
                        boolean mInvoked;

                        public void run() {
                            if (obj != null) {
                                if (!this.mInvoked) {
                                    this.mInvoked = true;
                                    try {
                                        Trace.endAsyncSection("Waiting for KeyguardDrawnCallback#onDrawn", identityHashCode);
                                        ((IKeyguardDrawnCallback) obj).onDrawn();
                                    } catch (RemoteException e) {
                                        Log.w(KeyguardLifecyclesDispatcher.TAG, "Exception calling onDrawn():", e);
                                    }
                                } else {
                                    Log.w(KeyguardLifecyclesDispatcher.TAG, "KeyguardDrawnCallback#onDrawn() invoked > 1 times");
                                }
                            }
                        }
                    });
                    Trace.endSection();
                    return;
                case 1:
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurnedOn();
                    return;
                case 2:
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurningOff();
                    return;
                case 3:
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurnedOff();
                    return;
                case 4:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchStartedWakingUp(message.arg1);
                    return;
                case 5:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchFinishedWakingUp();
                    return;
                case 6:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchStartedGoingToSleep(message.arg1);
                    return;
                case 7:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchFinishedGoingToSleep();
                    return;
                default:
                    throw new IllegalArgumentException("Unknown message: " + message);
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsScreenTurningOn = false;
    /* access modifiers changed from: private */
    public final ScreenLifecycle mScreenLifecycle;
    /* access modifiers changed from: private */
    public final WakefulnessLifecycle mWakefulnessLifecycle;

    @Inject
    public KeyguardLifecyclesDispatcher(ScreenLifecycle screenLifecycle, WakefulnessLifecycle wakefulnessLifecycle) {
        this.mScreenLifecycle = screenLifecycle;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        ((ConfigurationControllerImpl) NTDependencyEx.get(ConfigurationControllerImpl.class)).addCallback(this.mConfigListener);
    }

    /* access modifiers changed from: package-private */
    public void dispatch(int i) {
        this.mHandler.obtainMessage(i).sendToTarget();
    }

    /* access modifiers changed from: package-private */
    public void dispatch(int i, int i2) {
        Message obtainMessage = this.mHandler.obtainMessage(i);
        obtainMessage.arg1 = i2;
        obtainMessage.sendToTarget();
    }

    /* access modifiers changed from: package-private */
    public void dispatch(int i, Object obj) {
        this.mHandler.obtainMessage(i, obj).sendToTarget();
    }
}
