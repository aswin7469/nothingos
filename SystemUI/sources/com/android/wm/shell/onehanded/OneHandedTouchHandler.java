package com.android.wm.shell.onehanded;

import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.os.Looper;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.InputMonitor;
import android.view.MotionEvent;
import com.android.wm.shell.common.ShellExecutor;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class OneHandedTouchHandler implements OneHandedTransitionCallback {
    InputEventReceiver mInputEventReceiver;
    InputMonitor mInputMonitor;
    private boolean mIsEnabled;
    private boolean mIsInOutsideRegion;
    private boolean mIsOnStopTransitioning;
    private final Rect mLastUpdatedBounds = new Rect();
    private final ShellExecutor mMainExecutor;
    private final OneHandedTimeoutHandler mTimeoutHandler;
    OneHandedTouchEventCallback mTouchEventCallback;

    /* loaded from: classes2.dex */
    public interface OneHandedTouchEventCallback {
        void onStop();
    }

    public OneHandedTouchHandler(OneHandedTimeoutHandler oneHandedTimeoutHandler, ShellExecutor shellExecutor) {
        this.mTimeoutHandler = oneHandedTimeoutHandler;
        this.mMainExecutor = shellExecutor;
        updateIsEnabled();
    }

    public void onOneHandedEnabled(boolean z) {
        this.mIsEnabled = z;
        updateIsEnabled();
    }

    public void registerTouchEventListener(OneHandedTouchEventCallback oneHandedTouchEventCallback) {
        this.mTouchEventCallback = oneHandedTouchEventCallback;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x001b, code lost:
        if (r3 != 3) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean onMotionEvent(MotionEvent motionEvent) {
        this.mIsInOutsideRegion = isWithinTouchOutsideRegion(motionEvent.getX(), motionEvent.getY());
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                }
            }
            this.mTimeoutHandler.resetTimer();
            if (this.mIsInOutsideRegion && !this.mIsOnStopTransitioning) {
                this.mTouchEventCallback.onStop();
                this.mIsOnStopTransitioning = true;
            }
            this.mIsInOutsideRegion = false;
            return true;
        }
        if (!this.mIsInOutsideRegion) {
            this.mTimeoutHandler.resetTimer();
        }
        return true;
    }

    private void disposeInputChannel() {
        InputEventReceiver inputEventReceiver = this.mInputEventReceiver;
        if (inputEventReceiver != null) {
            inputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
        InputMonitor inputMonitor = this.mInputMonitor;
        if (inputMonitor != null) {
            inputMonitor.dispose();
            this.mInputMonitor = null;
        }
    }

    private boolean isWithinTouchOutsideRegion(float f, float f2) {
        return Math.round(f2) < this.mLastUpdatedBounds.top;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInputEvent(InputEvent inputEvent) {
        if (inputEvent instanceof MotionEvent) {
            onMotionEvent((MotionEvent) inputEvent);
        }
    }

    private void updateIsEnabled() {
        disposeInputChannel();
        if (this.mIsEnabled) {
            this.mInputMonitor = InputManager.getInstance().monitorGestureInput("onehanded-touch", 0);
            try {
                this.mMainExecutor.executeBlocking(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedTouchHandler$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        OneHandedTouchHandler.this.lambda$updateIsEnabled$0();
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to create input event receiver", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateIsEnabled$0() {
        this.mInputEventReceiver = new EventReceiver(this.mInputMonitor.getInputChannel(), Looper.myLooper());
    }

    @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
    public void onStartFinished(Rect rect) {
        this.mLastUpdatedBounds.set(rect);
    }

    @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
    public void onStopFinished(Rect rect) {
        this.mLastUpdatedBounds.set(rect);
        this.mIsOnStopTransitioning = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("OneHandedTouchHandler");
        printWriter.print("  mLastUpdatedBounds=");
        printWriter.println(this.mLastUpdatedBounds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class EventReceiver extends InputEventReceiver {
        EventReceiver(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
        }

        public void onInputEvent(InputEvent inputEvent) {
            OneHandedTouchHandler.this.onInputEvent(inputEvent);
            finishInputEvent(inputEvent, true);
        }
    }
}
