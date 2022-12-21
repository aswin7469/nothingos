package com.nothing.systemui.biometrics;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.Settings;
import com.android.systemui.C1893R;
import com.android.systemui.biometrics.UdfpsController;
import com.nothing.systemui.util.NTLogUtil;

public class NTFingerprintBrightnessController {
    private static final String TAG = "FpBrightnessController";
    /* access modifiers changed from: private */
    public static final Uri URI_SCREEN_BRIGHTNESS = Settings.System.getUriFor("screen_brightness");
    private final float BRIGHTNESS_SCALE_RATE;
    /* access modifiers changed from: private */
    public AlphaCallback mAlphaCallback;
    private int[] mAlphaMap;
    protected ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public Context mContext;
    private int mCurrentBrightness = -2;
    private boolean mDelayDraw;
    private DimLayerRunnable mDimLayerRunnable = new DimLayerRunnable(0.0f);
    private DimLayerUpdateAlphaRunnable mDimLayerUpdateAlphaRunnable = new DimLayerUpdateAlphaRunnable(0.0f);
    private final HandlerThread mFpThread;
    private final Handler mHandler;
    private final LocalObserver mLocalObserver;
    /* access modifiers changed from: private */
    public NTFingerprintDimLayer mNTFingerprintDimLayer;
    private UdfpsController mUdfpsController;
    private final Handler mWorkerHandler;

    public interface AlphaCallback {
        void onAlpha(float f);
    }

    public NTFingerprintBrightnessController(Context context, UdfpsController udfpsController) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.BRIGHTNESS_SCALE_RATE = 13.784314f;
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mNTFingerprintDimLayer = new NTFingerprintDimLayer(context, udfpsController);
        HandlerThread handlerThread = new HandlerThread(TAG, -2);
        this.mFpThread = handlerThread;
        handlerThread.start();
        this.mWorkerHandler = new Handler(handlerThread.getLooper());
        this.mUdfpsController = udfpsController;
        this.mAlphaMap = context.getResources().getIntArray(C1893R.array.nt_udfps_alpha_array);
        this.mLocalObserver = new LocalObserver(handler);
    }

    public void dismiss() {
        NTLogUtil.m1680d(TAG, "=dismiss=");
        this.mWorkerHandler.removeCallbacks(this.mDimLayerRunnable);
        this.mWorkerHandler.post(new Runnable() {
            public void run() {
                NTLogUtil.m1680d(NTFingerprintBrightnessController.TAG, "===dismiss dimlayer===");
                NTFingerprintBrightnessController.this.mNTFingerprintDimLayer.dismiss();
            }
        });
        this.mLocalObserver.unregister();
        this.mCurrentBrightness = -2;
    }

    public boolean needUpdateAlpha() {
        boolean z = this.mCurrentBrightness != getScreenBrightnessInt();
        NTLogUtil.m1680d(TAG, "=needUpdateAlpha=" + z);
        return z;
    }

    public void updateDimLayerAlphaIfNeed() {
        if (needUpdateAlpha()) {
            int screenBrightnessInt = getScreenBrightnessInt();
            float f = 1.0f;
            if (screenBrightnessInt == -1) {
                this.mDelayDraw = true;
            } else if (screenBrightnessInt != -2) {
                f = getAlpha(screenBrightnessInt);
            }
            NTLogUtil.m1680d(TAG, "updateDimLayerAlphaIfNeed brightness=" + screenBrightnessInt + ", alpha=" + f);
            this.mCurrentBrightness = screenBrightnessInt;
            this.mDimLayerUpdateAlphaRunnable.setAlpha(f);
            this.mWorkerHandler.removeCallbacks(this.mDimLayerUpdateAlphaRunnable);
            this.mWorkerHandler.post(this.mDimLayerUpdateAlphaRunnable);
        }
    }

    public void drawDimLayer(boolean z) {
        int screenBrightnessInt = getScreenBrightnessInt();
        if (this.mCurrentBrightness != screenBrightnessInt || z) {
            this.mCurrentBrightness = screenBrightnessInt;
            try {
                if (this.mDelayDraw) {
                    NTLogUtil.m1680d(TAG, "delay waking up 16ms for drawing fingerprint dimlayer.");
                    Thread.sleep(16);
                    this.mDelayDraw = false;
                }
            } catch (InterruptedException unused) {
            }
            float f = 1.0f;
            if (screenBrightnessInt == -1) {
                this.mDelayDraw = true;
            } else if (screenBrightnessInt != -2) {
                f = getAlpha(screenBrightnessInt);
            }
            NTLogUtil.m1680d(TAG, "drawDimLayer brightness=" + screenBrightnessInt + ", alpha=" + f);
            this.mDimLayerRunnable.setAlpha(f);
            this.mWorkerHandler.removeCallbacks(this.mDimLayerRunnable);
            this.mWorkerHandler.post(this.mDimLayerRunnable);
            this.mLocalObserver.register();
            return;
        }
        NTLogUtil.m1680d(TAG, "don't draw fingerprint dimlayer again, brightness=" + screenBrightnessInt);
    }

    private float getAlpha(int i) {
        int i2 = (int) (((float) i) * 13.784314f);
        NTLogUtil.m1680d(TAG, "get brightness = " + i + ", mAlphaMap.length=" + this.mAlphaMap.length + ", brightnessIndex=" + i2);
        if (i2 < 0) {
            return 0.0f;
        }
        int[] iArr = this.mAlphaMap;
        if (i2 < iArr.length) {
            return 1.0f - (((float) iArr[i2]) / ((float) iArr.length));
        }
        return 0.0f;
    }

    /* access modifiers changed from: private */
    public int getScreenBrightnessInt() {
        return Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness", -1, -2);
    }

    private final class LocalObserver extends ContentObserver {
        private boolean mRegistered;
        private final ContentResolver mResolver;

        public LocalObserver(Handler handler) {
            super(handler);
            this.mResolver = NTFingerprintBrightnessController.this.mContext.getContentResolver();
        }

        public void register() {
            if (this.mRegistered) {
                this.mResolver.unregisterContentObserver(this);
            }
            NTLogUtil.m1682i(NTFingerprintBrightnessController.TAG, "register");
            this.mResolver.registerContentObserver(NTFingerprintBrightnessController.URI_SCREEN_BRIGHTNESS, true, this);
            this.mRegistered = true;
        }

        public void unregister() {
            NTLogUtil.m1682i(NTFingerprintBrightnessController.TAG, "unregister");
            if (this.mRegistered) {
                this.mResolver.unregisterContentObserver(this);
            }
            this.mRegistered = false;
        }

        public void onChange(boolean z, Uri uri) {
            NTLogUtil.m1682i(NTFingerprintBrightnessController.TAG, "onChange uri=" + uri);
            if (NTFingerprintBrightnessController.URI_SCREEN_BRIGHTNESS.equals(uri)) {
                NTLogUtil.m1682i(NTFingerprintBrightnessController.TAG, "newScreenBrightness=" + NTFingerprintBrightnessController.this.getScreenBrightnessInt());
                NTFingerprintBrightnessController.this.updateDimLayerAlphaIfNeed();
            }
        }
    }

    private class DimLayerRunnable implements Runnable {
        float mAlpha;

        DimLayerRunnable(float f) {
            this.mAlpha = f;
        }

        public void run() {
            NTLogUtil.m1680d(NTFingerprintBrightnessController.TAG, "drawDimLayer alpha = " + this.mAlpha);
            if (NTFingerprintBrightnessController.this.mAlphaCallback != null) {
                NTFingerprintBrightnessController.this.mAlphaCallback.onAlpha(this.mAlpha);
            }
            NTFingerprintBrightnessController.this.mNTFingerprintDimLayer.draw(this.mAlpha);
        }

        public void setAlpha(float f) {
            this.mAlpha = f;
        }

        public float getAlpha() {
            return this.mAlpha;
        }
    }

    private class DimLayerUpdateAlphaRunnable implements Runnable {
        float mAlpha;

        DimLayerUpdateAlphaRunnable(float f) {
            this.mAlpha = f;
        }

        public void run() {
            NTLogUtil.m1680d(NTFingerprintBrightnessController.TAG, "DimLayerUpdateAlphaRunnable alpha = " + this.mAlpha);
            if (NTFingerprintBrightnessController.this.mAlphaCallback != null) {
                NTFingerprintBrightnessController.this.mAlphaCallback.onAlpha(this.mAlpha);
            }
            NTFingerprintBrightnessController.this.mNTFingerprintDimLayer.updateAlpha(this.mAlpha);
        }

        public void setAlpha(float f) {
            this.mAlpha = f;
        }

        public float getAlpha() {
            return this.mAlpha;
        }
    }

    public void setAlphaCallback(AlphaCallback alphaCallback) {
        this.mAlphaCallback = alphaCallback;
    }
}
