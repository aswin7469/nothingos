package com.android.systemui.charging;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Slog;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import java.sql.Types;

public class WirelessChargingAnimation {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    public static final long DURATION = 1500;
    private static final String TAG = "WirelessChargingView";
    private static WirelessChargingView mPreviousWirelessChargingView;
    private final WirelessChargingView mCurrentWirelessChargingView;

    public interface Callback {
        void onAnimationEnded();

        void onAnimationStarting();
    }

    public WirelessChargingAnimation(Context context, Looper looper, int i, int i2, Callback callback, boolean z, UiEventLogger uiEventLogger) {
        this.mCurrentWirelessChargingView = new WirelessChargingView(context, looper, i, i2, callback, z, uiEventLogger);
    }

    public static WirelessChargingAnimation makeWirelessChargingAnimation(Context context, Looper looper, int i, int i2, Callback callback, boolean z, UiEventLogger uiEventLogger) {
        return new WirelessChargingAnimation(context, looper, i, i2, callback, z, uiEventLogger);
    }

    public static WirelessChargingAnimation makeChargingAnimationWithNoBatteryLevel(Context context, UiEventLogger uiEventLogger) {
        return makeWirelessChargingAnimation(context, (Looper) null, -1, -1, (Callback) null, false, uiEventLogger);
    }

    public void show(long j) {
        WirelessChargingView wirelessChargingView = this.mCurrentWirelessChargingView;
        if (wirelessChargingView == null || wirelessChargingView.mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }
        WirelessChargingView wirelessChargingView2 = mPreviousWirelessChargingView;
        if (wirelessChargingView2 != null) {
            wirelessChargingView2.hide(0);
        }
        WirelessChargingView wirelessChargingView3 = this.mCurrentWirelessChargingView;
        mPreviousWirelessChargingView = wirelessChargingView3;
        wirelessChargingView3.show(j);
        this.mCurrentWirelessChargingView.hide(j + DURATION);
    }

    private static class WirelessChargingView {
        private static final int HIDE = 1;
        private static final int SHOW = 0;
        private Callback mCallback;
        private int mGravity = 17;
        private final Handler mHandler;
        /* access modifiers changed from: private */
        public WirelessChargingLayout mNextView;
        private final WindowManager.LayoutParams mParams;
        private final UiEventLogger mUiEventLogger;
        private WirelessChargingLayout mView;
        private WindowManager mWM;

        public WirelessChargingView(Context context, Looper looper, int i, int i2, Callback callback, boolean z, UiEventLogger uiEventLogger) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            this.mParams = layoutParams;
            this.mCallback = callback;
            this.mNextView = new WirelessChargingLayout(context, i, i2, z);
            this.mUiEventLogger = uiEventLogger;
            layoutParams.height = -1;
            layoutParams.width = -1;
            layoutParams.format = -3;
            layoutParams.type = Types.SQLXML;
            layoutParams.setTitle("Charging Animation");
            layoutParams.layoutInDisplayCutoutMode = 3;
            layoutParams.setFitInsetsTypes(0);
            layoutParams.flags = 24;
            layoutParams.setTrustedOverlay();
            if (looper == null && (looper = Looper.myLooper()) == null) {
                throw new RuntimeException("Can't display wireless animation on a thread that has not called Looper.prepare()");
            }
            this.mHandler = new Handler(looper, (Handler.Callback) null) {
                public void handleMessage(Message message) {
                    int i = message.what;
                    if (i == 0) {
                        WirelessChargingView.this.handleShow();
                    } else if (i == 1) {
                        WirelessChargingView.this.handleHide();
                        WirelessChargingLayout unused = WirelessChargingView.this.mNextView = null;
                    }
                }
            };
        }

        public void show(long j) {
            if (WirelessChargingAnimation.DEBUG) {
                Slog.d(WirelessChargingAnimation.TAG, "SHOW: " + this);
            }
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(Message.obtain(handler, 0), j);
        }

        public void hide(long j) {
            this.mHandler.removeMessages(1);
            if (WirelessChargingAnimation.DEBUG) {
                Slog.d(WirelessChargingAnimation.TAG, "HIDE: " + this);
            }
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(Message.obtain(handler, 1), j);
        }

        /* access modifiers changed from: private */
        public void handleShow() {
            if (WirelessChargingAnimation.DEBUG) {
                Slog.d(WirelessChargingAnimation.TAG, "HANDLE SHOW: " + this + " mView=" + this.mView + " mNextView=" + this.mNextView);
            }
            if (this.mView != this.mNextView) {
                handleHide();
                WirelessChargingLayout wirelessChargingLayout = this.mNextView;
                this.mView = wirelessChargingLayout;
                Context applicationContext = wirelessChargingLayout.getContext().getApplicationContext();
                String opPackageName = this.mView.getContext().getOpPackageName();
                if (applicationContext == null) {
                    applicationContext = this.mView.getContext();
                }
                this.mWM = (WindowManager) applicationContext.getSystemService("window");
                this.mParams.packageName = opPackageName;
                this.mParams.hideTimeoutMilliseconds = WirelessChargingAnimation.DURATION;
                if (this.mView.getParent() != null) {
                    if (WirelessChargingAnimation.DEBUG) {
                        Slog.d(WirelessChargingAnimation.TAG, "REMOVE! " + this.mView + " in " + this);
                    }
                    this.mWM.removeView(this.mView);
                }
                if (WirelessChargingAnimation.DEBUG) {
                    Slog.d(WirelessChargingAnimation.TAG, "ADD! " + this.mView + " in " + this);
                }
                try {
                    Callback callback = this.mCallback;
                    if (callback != null) {
                        callback.onAnimationStarting();
                    }
                    this.mWM.addView(this.mView, this.mParams);
                    this.mUiEventLogger.log(WirelessChargingRippleEvent.WIRELESS_RIPPLE_PLAYED);
                } catch (WindowManager.BadTokenException e) {
                    Slog.d(WirelessChargingAnimation.TAG, "Unable to add wireless charging view. " + e);
                }
            }
        }

        /* access modifiers changed from: private */
        public void handleHide() {
            if (WirelessChargingAnimation.DEBUG) {
                Slog.d(WirelessChargingAnimation.TAG, "HANDLE HIDE: " + this + " mView=" + this.mView);
            }
            WirelessChargingLayout wirelessChargingLayout = this.mView;
            if (wirelessChargingLayout != null) {
                if (wirelessChargingLayout.getParent() != null) {
                    if (WirelessChargingAnimation.DEBUG) {
                        Slog.d(WirelessChargingAnimation.TAG, "REMOVE! " + this.mView + " in " + this);
                    }
                    Callback callback = this.mCallback;
                    if (callback != null) {
                        callback.onAnimationEnded();
                    }
                    this.mWM.removeViewImmediate(this.mView);
                }
                this.mView = null;
            }
        }

        enum WirelessChargingRippleEvent implements UiEventLogger.UiEventEnum {
            WIRELESS_RIPPLE_PLAYED(830);
            
            private final int mInt;

            private WirelessChargingRippleEvent(int i) {
                this.mInt = i;
            }

            public int getId() {
                return this.mInt;
            }
        }
    }
}
