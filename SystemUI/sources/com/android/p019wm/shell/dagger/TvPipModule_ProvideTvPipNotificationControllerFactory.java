package com.android.p019wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsState;
import com.android.p019wm.shell.pip.p020tv.TvPipNotificationController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvideTvPipNotificationControllerFactory */
public final class TvPipModule_ProvideTvPipNotificationControllerFactory implements Factory<TvPipNotificationController> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<PipMediaController> pipMediaControllerProvider;
    private final Provider<PipParamsChangedForwarder> pipParamsChangedForwarderProvider;
    private final Provider<TvPipBoundsState> tvPipBoundsStateProvider;

    public TvPipModule_ProvideTvPipNotificationControllerFactory(Provider<Context> provider, Provider<PipMediaController> provider2, Provider<PipParamsChangedForwarder> provider3, Provider<TvPipBoundsState> provider4, Provider<Handler> provider5) {
        this.contextProvider = provider;
        this.pipMediaControllerProvider = provider2;
        this.pipParamsChangedForwarderProvider = provider3;
        this.tvPipBoundsStateProvider = provider4;
        this.mainHandlerProvider = provider5;
    }

    public TvPipNotificationController get() {
        return provideTvPipNotificationController(this.contextProvider.get(), this.pipMediaControllerProvider.get(), this.pipParamsChangedForwarderProvider.get(), this.tvPipBoundsStateProvider.get(), this.mainHandlerProvider.get());
    }

    public static TvPipModule_ProvideTvPipNotificationControllerFactory create(Provider<Context> provider, Provider<PipMediaController> provider2, Provider<PipParamsChangedForwarder> provider3, Provider<TvPipBoundsState> provider4, Provider<Handler> provider5) {
        return new TvPipModule_ProvideTvPipNotificationControllerFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static TvPipNotificationController provideTvPipNotificationController(Context context, PipMediaController pipMediaController, PipParamsChangedForwarder pipParamsChangedForwarder, TvPipBoundsState tvPipBoundsState, Handler handler) {
        return (TvPipNotificationController) Preconditions.checkNotNullFromProvides(TvPipModule.provideTvPipNotificationController(context, pipMediaController, pipParamsChangedForwarder, tvPipBoundsState, handler));
    }
}
