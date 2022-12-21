package com.android.systemui.dreams.touch;

import android.os.Handler;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.touch.TouchInsetManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class HideComplicationTouchHandler_Factory implements Factory<HideComplicationTouchHandler> {
    private final Provider<Executor> executorProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<Integer> restoreTimeoutProvider;
    private final Provider<TouchInsetManager> touchInsetManagerProvider;
    private final Provider<Complication.VisibilityController> visibilityControllerProvider;

    public HideComplicationTouchHandler_Factory(Provider<Complication.VisibilityController> provider, Provider<Integer> provider2, Provider<TouchInsetManager> provider3, Provider<Executor> provider4, Provider<Handler> provider5) {
        this.visibilityControllerProvider = provider;
        this.restoreTimeoutProvider = provider2;
        this.touchInsetManagerProvider = provider3;
        this.executorProvider = provider4;
        this.handlerProvider = provider5;
    }

    public HideComplicationTouchHandler get() {
        return newInstance(this.visibilityControllerProvider.get(), this.restoreTimeoutProvider.get().intValue(), this.touchInsetManagerProvider.get(), this.executorProvider.get(), this.handlerProvider.get());
    }

    public static HideComplicationTouchHandler_Factory create(Provider<Complication.VisibilityController> provider, Provider<Integer> provider2, Provider<TouchInsetManager> provider3, Provider<Executor> provider4, Provider<Handler> provider5) {
        return new HideComplicationTouchHandler_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static HideComplicationTouchHandler newInstance(Complication.VisibilityController visibilityController, int i, TouchInsetManager touchInsetManager, Executor executor, Handler handler) {
        return new HideComplicationTouchHandler(visibilityController, i, touchInsetManager, executor, handler);
    }
}
