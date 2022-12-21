package com.nothing.systemui.facerecognition.manager;

import android.os.Handler;
import android.os.HandlerThread;

public class CameraWorkThreadManager {
    private static String TAG = "CameraWorkThreadManager";
    Handler mCurrentHandler = new Handler(this.mCurrentThread.getLooper());
    HandlerThread mCurrentThread;
    private Runnable mStartCamerePerviewRunnable;
    private Runnable mStopCamerePerviewRunnable;

    public CameraWorkThreadManager(Runnable runnable, Runnable runnable2) {
        this.mStopCamerePerviewRunnable = runnable;
        this.mStartCamerePerviewRunnable = runnable2;
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mCurrentThread = handlerThread;
        handlerThread.start();
    }

    public Handler getHandler() {
        return this.mCurrentHandler;
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

    public void releaseCurrentCameraThread() {
        this.mCurrentHandler = null;
        HandlerThread handlerThread = this.mCurrentThread;
        if (handlerThread != null && handlerThread.getLooper() != null) {
            this.mCurrentThread.quitSafely();
            this.mCurrentThread = null;
        }
    }
}
