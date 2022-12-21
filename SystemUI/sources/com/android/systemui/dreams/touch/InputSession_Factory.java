package com.android.systemui.dreams.touch;

import android.view.GestureDetector;
import com.android.systemui.shared.system.InputChannelCompat;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class InputSession_Factory implements Factory<InputSession> {
    private final Provider<GestureDetector.OnGestureListener> gestureListenerProvider;
    private final Provider<InputChannelCompat.InputEventListener> inputEventListenerProvider;
    private final Provider<Boolean> pilferOnGestureConsumeProvider;
    private final Provider<String> sessionNameProvider;

    public InputSession_Factory(Provider<String> provider, Provider<InputChannelCompat.InputEventListener> provider2, Provider<GestureDetector.OnGestureListener> provider3, Provider<Boolean> provider4) {
        this.sessionNameProvider = provider;
        this.inputEventListenerProvider = provider2;
        this.gestureListenerProvider = provider3;
        this.pilferOnGestureConsumeProvider = provider4;
    }

    public InputSession get() {
        return newInstance(this.sessionNameProvider.get(), this.inputEventListenerProvider.get(), this.gestureListenerProvider.get(), this.pilferOnGestureConsumeProvider.get().booleanValue());
    }

    public static InputSession_Factory create(Provider<String> provider, Provider<InputChannelCompat.InputEventListener> provider2, Provider<GestureDetector.OnGestureListener> provider3, Provider<Boolean> provider4) {
        return new InputSession_Factory(provider, provider2, provider3, provider4);
    }

    public static InputSession newInstance(String str, InputChannelCompat.InputEventListener inputEventListener, GestureDetector.OnGestureListener onGestureListener, boolean z) {
        return new InputSession(str, inputEventListener, onGestureListener, z);
    }
}
