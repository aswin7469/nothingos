package com.android.systemui.dreams.touch;

import android.os.Handler;
import android.util.Log;
import android.view.InputEvent;
import android.view.MotionEvent;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.touch.TouchInsetManager;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Named;

public class HideComplicationTouchHandler implements DreamTouchHandler {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "HideComplicationHandler";
    private final Executor mExecutor;
    private final Handler mHandler;
    private final Runnable mRestoreComplications = new Runnable() {
        public void run() {
            HideComplicationTouchHandler.this.mVisibilityController.setVisibility(0, true);
        }
    };
    private final int mRestoreTimeout;
    private final TouchInsetManager mTouchInsetManager;
    /* access modifiers changed from: private */
    public final Complication.VisibilityController mVisibilityController;

    @Inject
    HideComplicationTouchHandler(Complication.VisibilityController visibilityController, @Named("complication_restore_timeout") int i, TouchInsetManager touchInsetManager, @Main Executor executor, @Main Handler handler) {
        this.mVisibilityController = visibilityController;
        this.mRestoreTimeout = i;
        this.mHandler = handler;
        this.mTouchInsetManager = touchInsetManager;
        this.mExecutor = executor;
    }

    public void onSessionStart(DreamTouchHandler.TouchSession touchSession) {
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, "onSessionStart");
        }
        if (touchSession.getActiveSessionCount() > 1) {
            if (z) {
                Log.d(TAG, "multiple active touch sessions, not fading");
            }
            touchSession.pop();
            return;
        }
        touchSession.registerInputListener(new HideComplicationTouchHandler$$ExternalSyntheticLambda1(this, touchSession));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onSessionStart$1$com-android-systemui-dreams-touch-HideComplicationTouchHandler */
    public /* synthetic */ void mo32655x8bd2f205(DreamTouchHandler.TouchSession touchSession, InputEvent inputEvent) {
        if (inputEvent instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            if (motionEvent.getAction() == 0) {
                if (DEBUG) {
                    Log.d(TAG, "ACTION_DOWN received");
                }
                ListenableFuture<Boolean> checkWithinTouchRegion = this.mTouchInsetManager.checkWithinTouchRegion(Math.round(motionEvent.getX()), Math.round(motionEvent.getY()));
                checkWithinTouchRegion.addListener(new HideComplicationTouchHandler$$ExternalSyntheticLambda0(this, checkWithinTouchRegion, touchSession), this.mExecutor);
            } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                touchSession.pop();
                this.mHandler.postDelayed(this.mRestoreComplications, (long) this.mRestoreTimeout);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onSessionStart$0$com-android-systemui-dreams-touch-HideComplicationTouchHandler */
    public /* synthetic */ void mo32654xd71ee26(ListenableFuture listenableFuture, DreamTouchHandler.TouchSession touchSession) {
        try {
            if (!((Boolean) listenableFuture.get()).booleanValue()) {
                this.mHandler.removeCallbacks(this.mRestoreComplications);
                this.mVisibilityController.setVisibility(4, true);
                return;
            }
            touchSession.pop();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "could not check TouchInsetManager:" + e);
        }
    }
}
