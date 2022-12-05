package com.android.systemui.controls.ui;

import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsActivity_Factory implements Factory<ControlsActivity> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<ControlsUiController> uiControllerProvider;

    public ControlsActivity_Factory(Provider<ControlsUiController> provider, Provider<BroadcastDispatcher> provider2) {
        this.uiControllerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ControlsActivity mo1933get() {
        return newInstance(this.uiControllerProvider.mo1933get(), this.broadcastDispatcherProvider.mo1933get());
    }

    public static ControlsActivity_Factory create(Provider<ControlsUiController> provider, Provider<BroadcastDispatcher> provider2) {
        return new ControlsActivity_Factory(provider, provider2);
    }

    public static ControlsActivity newInstance(ControlsUiController controlsUiController, BroadcastDispatcher broadcastDispatcher) {
        return new ControlsActivity(controlsUiController, broadcastDispatcher);
    }
}
