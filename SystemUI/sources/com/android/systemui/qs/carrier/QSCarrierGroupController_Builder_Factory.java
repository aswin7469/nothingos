package com.android.systemui.qs.carrier;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.keyguard.CarrierTextManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.util.CarrierConfigTracker;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSCarrierGroupController_Builder_Factory implements Factory<QSCarrierGroupController.Builder> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    private final Provider<CarrierTextManager.Builder> carrierTextControllerBuilderProvider;
    private final Provider<Context> contextProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<Looper> looperProvider;
    private final Provider<NetworkController> networkControllerProvider;
    private final Provider<QSCarrierGroupController.SlotIndexResolver> slotIndexResolverProvider;

    public QSCarrierGroupController_Builder_Factory(Provider<ActivityStarter> provider, Provider<Handler> provider2, Provider<Looper> provider3, Provider<NetworkController> provider4, Provider<CarrierTextManager.Builder> provider5, Provider<Context> provider6, Provider<CarrierConfigTracker> provider7, Provider<FeatureFlags> provider8, Provider<QSCarrierGroupController.SlotIndexResolver> provider9) {
        this.activityStarterProvider = provider;
        this.handlerProvider = provider2;
        this.looperProvider = provider3;
        this.networkControllerProvider = provider4;
        this.carrierTextControllerBuilderProvider = provider5;
        this.contextProvider = provider6;
        this.carrierConfigTrackerProvider = provider7;
        this.featureFlagsProvider = provider8;
        this.slotIndexResolverProvider = provider9;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public QSCarrierGroupController.Builder mo1933get() {
        return newInstance(this.activityStarterProvider.mo1933get(), this.handlerProvider.mo1933get(), this.looperProvider.mo1933get(), this.networkControllerProvider.mo1933get(), this.carrierTextControllerBuilderProvider.mo1933get(), this.contextProvider.mo1933get(), this.carrierConfigTrackerProvider.mo1933get(), this.featureFlagsProvider.mo1933get(), this.slotIndexResolverProvider.mo1933get());
    }

    public static QSCarrierGroupController_Builder_Factory create(Provider<ActivityStarter> provider, Provider<Handler> provider2, Provider<Looper> provider3, Provider<NetworkController> provider4, Provider<CarrierTextManager.Builder> provider5, Provider<Context> provider6, Provider<CarrierConfigTracker> provider7, Provider<FeatureFlags> provider8, Provider<QSCarrierGroupController.SlotIndexResolver> provider9) {
        return new QSCarrierGroupController_Builder_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static QSCarrierGroupController.Builder newInstance(ActivityStarter activityStarter, Handler handler, Looper looper, NetworkController networkController, CarrierTextManager.Builder builder, Context context, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, QSCarrierGroupController.SlotIndexResolver slotIndexResolver) {
        return new QSCarrierGroupController.Builder(activityStarter, handler, looper, networkController, builder, context, carrierConfigTracker, featureFlags, slotIndexResolver);
    }
}
