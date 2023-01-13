package com.android.systemui.unfold;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Handler;
import com.android.systemui.unfold.UnfoldSharedComponent;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider_Factory;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener_Factory;
import com.android.systemui.unfold.util.C4842ScaleAwareTransitionProgressProvider_Factory;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider_Factory_Impl;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.Preconditions;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DaggerUnfoldSharedComponent {
    private DaggerUnfoldSharedComponent() {
    }

    public static UnfoldSharedComponent.Factory factory() {
        return new Factory();
    }

    private static final class Factory implements UnfoldSharedComponent.Factory {
        private Factory() {
        }

        public UnfoldSharedComponent create(Context context, UnfoldTransitionConfig unfoldTransitionConfig, ScreenStatusProvider screenStatusProvider, DeviceStateManager deviceStateManager, ActivityManager activityManager, SensorManager sensorManager, Handler handler, Executor executor, Executor executor2, String str, ContentResolver contentResolver) {
            Preconditions.checkNotNull(context);
            Preconditions.checkNotNull(unfoldTransitionConfig);
            Preconditions.checkNotNull(screenStatusProvider);
            Preconditions.checkNotNull(deviceStateManager);
            Preconditions.checkNotNull(activityManager);
            Preconditions.checkNotNull(sensorManager);
            Preconditions.checkNotNull(handler);
            Preconditions.checkNotNull(executor);
            Preconditions.checkNotNull(executor2);
            Preconditions.checkNotNull(str);
            Preconditions.checkNotNull(contentResolver);
            return new UnfoldSharedComponentImpl(new UnfoldSharedModule(), context, unfoldTransitionConfig, screenStatusProvider, deviceStateManager, activityManager, sensorManager, handler, executor, executor2, str, contentResolver);
        }
    }

    private static final class UnfoldSharedComponentImpl implements UnfoldSharedComponent {
        private Provider<ATraceLoggerTransitionProgressListener> aTraceLoggerTransitionProgressListenerProvider;
        private Provider<ActivityManager> activityManagerProvider;
        private Provider<Executor> backgroundExecutorProvider;
        private Provider<UnfoldTransitionConfig> configProvider;
        private Provider<ContentResolver> contentResolverProvider;
        private Provider<Context> contextProvider;
        private Provider<DeviceFoldStateProvider> deviceFoldStateProvider;
        private Provider<DeviceStateManager> deviceStateManagerProvider;
        private Provider<Executor> executorProvider;
        private Provider<ScaleAwareTransitionProgressProvider.Factory> factoryProvider;
        private Provider<Handler> handlerProvider;
        private Provider<HingeAngleProvider> hingeAngleProvider;
        private Provider<FoldStateProvider> provideFoldStateProvider;
        private C4842ScaleAwareTransitionProgressProvider_Factory scaleAwareTransitionProgressProvider;
        private Provider<ScreenStatusProvider> screenStatusProvider;
        private Provider<SensorManager> sensorManagerProvider;
        private Provider<String> tracingTagPrefixProvider;
        private final UnfoldSharedComponentImpl unfoldSharedComponentImpl;
        private Provider<Optional<UnfoldTransitionProgressProvider>> unfoldTransitionProgressProvider;

        private UnfoldSharedComponentImpl(UnfoldSharedModule unfoldSharedModule, Context context, UnfoldTransitionConfig unfoldTransitionConfig, ScreenStatusProvider screenStatusProvider2, DeviceStateManager deviceStateManager, ActivityManager activityManager, SensorManager sensorManager, Handler handler, Executor executor, Executor executor2, String str, ContentResolver contentResolver) {
            this.unfoldSharedComponentImpl = this;
            initialize(unfoldSharedModule, context, unfoldTransitionConfig, screenStatusProvider2, deviceStateManager, activityManager, sensorManager, handler, executor, executor2, str, contentResolver);
        }

        private void initialize(UnfoldSharedModule unfoldSharedModule, Context context, UnfoldTransitionConfig unfoldTransitionConfig, ScreenStatusProvider screenStatusProvider2, DeviceStateManager deviceStateManager, ActivityManager activityManager, SensorManager sensorManager, Handler handler, Executor executor, Executor executor2, String str, ContentResolver contentResolver) {
            this.configProvider = InstanceFactory.create(unfoldTransitionConfig);
            dagger.internal.Factory create = InstanceFactory.create(contentResolver);
            this.contentResolverProvider = create;
            C4842ScaleAwareTransitionProgressProvider_Factory create2 = C4842ScaleAwareTransitionProgressProvider_Factory.create(create);
            this.scaleAwareTransitionProgressProvider = create2;
            this.factoryProvider = ScaleAwareTransitionProgressProvider_Factory_Impl.create(create2);
            dagger.internal.Factory create3 = InstanceFactory.create(str);
            this.tracingTagPrefixProvider = create3;
            this.aTraceLoggerTransitionProgressListenerProvider = ATraceLoggerTransitionProgressListener_Factory.create(create3);
            this.contextProvider = InstanceFactory.create(context);
            this.sensorManagerProvider = InstanceFactory.create(sensorManager);
            dagger.internal.Factory create4 = InstanceFactory.create(executor2);
            this.backgroundExecutorProvider = create4;
            this.hingeAngleProvider = UnfoldSharedModule_HingeAngleProviderFactory.create(unfoldSharedModule, this.configProvider, this.sensorManagerProvider, create4);
            this.screenStatusProvider = InstanceFactory.create(screenStatusProvider2);
            this.deviceStateManagerProvider = InstanceFactory.create(deviceStateManager);
            this.activityManagerProvider = InstanceFactory.create(activityManager);
            this.executorProvider = InstanceFactory.create(executor);
            dagger.internal.Factory create5 = InstanceFactory.create(handler);
            this.handlerProvider = create5;
            DeviceFoldStateProvider_Factory create6 = DeviceFoldStateProvider_Factory.create(this.contextProvider, this.hingeAngleProvider, this.screenStatusProvider, this.deviceStateManagerProvider, this.activityManagerProvider, this.executorProvider, create5);
            this.deviceFoldStateProvider = create6;
            Provider<FoldStateProvider> provider = DoubleCheck.provider(UnfoldSharedModule_ProvideFoldStateProviderFactory.create(unfoldSharedModule, create6));
            this.provideFoldStateProvider = provider;
            this.unfoldTransitionProgressProvider = DoubleCheck.provider(UnfoldSharedModule_UnfoldTransitionProgressProviderFactory.create(unfoldSharedModule, this.configProvider, this.factoryProvider, this.aTraceLoggerTransitionProgressListenerProvider, provider));
        }

        public Optional<UnfoldTransitionProgressProvider> getUnfoldTransitionProvider() {
            return this.unfoldTransitionProgressProvider.get();
        }
    }
}
