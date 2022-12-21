package com.android.systemui.dreams.touch;

import android.os.Looper;
import android.view.Choreographer;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.MotionEvent;
import com.android.systemui.shared.system.InputChannelCompat;
import com.android.systemui.shared.system.InputMonitorCompat;
import javax.inject.Inject;
import javax.inject.Named;

public class InputSession {
    private final GestureDetector mGestureDetector;
    private final InputChannelCompat.InputEventReceiver mInputEventReceiver;
    private final InputMonitorCompat mInputMonitor;

    @Inject
    public InputSession(@Named("INPUT_SESSION_NAME") String str, InputChannelCompat.InputEventListener inputEventListener, GestureDetector.OnGestureListener onGestureListener, @Named("PILFER_ON_GESTURE_CONSUME") boolean z) {
        InputMonitorCompat inputMonitorCompat = new InputMonitorCompat(str, 0);
        this.mInputMonitor = inputMonitorCompat;
        this.mGestureDetector = new GestureDetector(onGestureListener);
        this.mInputEventReceiver = inputMonitorCompat.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), new InputSession$$ExternalSyntheticLambda0(this, inputEventListener, z));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-dreams-touch-InputSession  reason: not valid java name */
    public /* synthetic */ void m2746lambda$new$0$comandroidsystemuidreamstouchInputSession(InputChannelCompat.InputEventListener inputEventListener, boolean z, InputEvent inputEvent) {
        inputEventListener.onInputEvent(inputEvent);
        if ((inputEvent instanceof MotionEvent) && this.mGestureDetector.onTouchEvent((MotionEvent) inputEvent) && z) {
            this.mInputMonitor.pilferPointers();
        }
    }

    public void dispose() {
        InputChannelCompat.InputEventReceiver inputEventReceiver = this.mInputEventReceiver;
        if (inputEventReceiver != null) {
            inputEventReceiver.dispose();
        }
        InputMonitorCompat inputMonitorCompat = this.mInputMonitor;
        if (inputMonitorCompat != null) {
            inputMonitorCompat.dispose();
        }
    }
}
