package com.nothingos.systemui.facerecognition.manager;

import android.os.Handler;
import android.os.HandlerThread;
/* loaded from: classes2.dex */
public class CameraWorkThreadManager {
    private static String TAG = "CameraWorkThreadManager";
    Handler mCurrentHandler;
    HandlerThread mCurrentThread;
    private Runnable mStartCamerePerviewRunnable;
    private Runnable mStopCamerePerviewRunnable;

    public CameraWorkThreadManager(Runnable runnable, Runnable runnable2) {
        this.mStopCamerePerviewRunnable = runnable;
        this.mStartCamerePerviewRunnable = runnable2;
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mCurrentThread = handlerThread;
        handlerThread.start();
        this.mCurrentHandler = new Handler(this.mCurrentThread.getLooper());
    }

    public synchronized void startCamera() {
        Handler handler = this.mCurrentHandler;
        if (handler != null) {
            handler.post(this.mStartCamerePerviewRunnable);
        }
    }

    public synchronized void stopCamera() {
        Handler handler = this.mCurrentHandler;
        if (handler != null) {
            handler.post(this.mStopCamerePerviewRunnable);
        }
    }
}
