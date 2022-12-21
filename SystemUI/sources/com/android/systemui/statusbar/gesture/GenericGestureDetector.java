package com.android.systemui.statusbar.gesture;

import android.os.Looper;
import android.view.Choreographer;
import android.view.InputEvent;
import android.view.MotionEvent;
import com.android.systemui.shared.system.InputChannelCompat;
import com.android.systemui.shared.system.InputMonitorCompat;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\"\u0010\u000e\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007J\u0015\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\bH\u0000¢\u0006\u0002\b\u0012J\u0010\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\u0015H&J\u000e\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003J\r\u0010\u0017\u001a\u00020\tH\u0010¢\u0006\u0002\b\u0018J\r\u0010\u0019\u001a\u00020\tH\u0010¢\u0006\u0002\b\u001aR&\u0010\u0005\u001a\u001a\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u00070\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/gesture/GenericGestureDetector;", "", "tag", "", "(Ljava/lang/String;)V", "callbacks", "", "Lkotlin/Function1;", "Landroid/view/MotionEvent;", "", "inputMonitor", "Lcom/android/systemui/shared/system/InputMonitorCompat;", "inputReceiver", "Lcom/android/systemui/shared/system/InputChannelCompat$InputEventReceiver;", "addOnGestureDetectedCallback", "callback", "onGestureDetected", "e", "onGestureDetected$SystemUI_nothingRelease", "onInputEvent", "ev", "Landroid/view/InputEvent;", "removeOnGestureDetectedCallback", "startGestureListening", "startGestureListening$SystemUI_nothingRelease", "stopGestureListening", "stopGestureListening$SystemUI_nothingRelease", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: GenericGestureDetector.kt */
public abstract class GenericGestureDetector {
    private final Map<String, Function1<MotionEvent, Unit>> callbacks = new LinkedHashMap();
    private InputMonitorCompat inputMonitor;
    private InputChannelCompat.InputEventReceiver inputReceiver;
    private final String tag;

    public abstract void onInputEvent(InputEvent inputEvent);

    public GenericGestureDetector(String str) {
        Intrinsics.checkNotNullParameter(str, "tag");
        this.tag = str;
    }

    public final void addOnGestureDetectedCallback(String str, Function1<? super MotionEvent, Unit> function1) {
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(function1, "callback");
        boolean isEmpty = this.callbacks.isEmpty();
        this.callbacks.put(str, function1);
        if (isEmpty) {
            startGestureListening$SystemUI_nothingRelease();
        }
    }

    public final void removeOnGestureDetectedCallback(String str) {
        Intrinsics.checkNotNullParameter(str, "tag");
        this.callbacks.remove(str);
        if (this.callbacks.isEmpty()) {
            stopGestureListening$SystemUI_nothingRelease();
        }
    }

    public final void onGestureDetected$SystemUI_nothingRelease(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "e");
        for (Function1 invoke : this.callbacks.values()) {
            invoke.invoke(motionEvent);
        }
    }

    public void startGestureListening$SystemUI_nothingRelease() {
        stopGestureListening$SystemUI_nothingRelease();
        InputMonitorCompat inputMonitorCompat = new InputMonitorCompat(this.tag, 0);
        this.inputReceiver = inputMonitorCompat.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), new GenericGestureDetector$$ExternalSyntheticLambda0(this));
        this.inputMonitor = inputMonitorCompat;
    }

    public void stopGestureListening$SystemUI_nothingRelease() {
        InputMonitorCompat inputMonitorCompat = this.inputMonitor;
        if (inputMonitorCompat != null) {
            this.inputMonitor = null;
            inputMonitorCompat.dispose();
        }
        InputChannelCompat.InputEventReceiver inputEventReceiver = this.inputReceiver;
        if (inputEventReceiver != null) {
            this.inputReceiver = null;
            inputEventReceiver.dispose();
        }
    }
}
