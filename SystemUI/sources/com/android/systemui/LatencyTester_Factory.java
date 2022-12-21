package com.android.systemui;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LatencyTester_Factory implements Factory<LatencyTester> {
    private final Provider<BiometricUnlockController> biometricUnlockControllerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DeviceConfigProxy> deviceConfigProxyProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;

    public LatencyTester_Factory(Provider<Context> provider, Provider<BiometricUnlockController> provider2, Provider<BroadcastDispatcher> provider3, Provider<DeviceConfigProxy> provider4, Provider<DelayableExecutor> provider5) {
        this.contextProvider = provider;
        this.biometricUnlockControllerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.deviceConfigProxyProvider = provider4;
        this.mainExecutorProvider = provider5;
    }

    public LatencyTester get() {
        return newInstance(this.contextProvider.get(), this.biometricUnlockControllerProvider.get(), this.broadcastDispatcherProvider.get(), this.deviceConfigProxyProvider.get(), this.mainExecutorProvider.get());
    }

    public static LatencyTester_Factory create(Provider<Context> provider, Provider<BiometricUnlockController> provider2, Provider<BroadcastDispatcher> provider3, Provider<DeviceConfigProxy> provider4, Provider<DelayableExecutor> provider5) {
        return new LatencyTester_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static LatencyTester newInstance(Context context, BiometricUnlockController biometricUnlockController, BroadcastDispatcher broadcastDispatcher, DeviceConfigProxy deviceConfigProxy, DelayableExecutor delayableExecutor) {
        return new LatencyTester(context, biometricUnlockController, broadcastDispatcher, deviceConfigProxy, delayableExecutor);
    }
}
