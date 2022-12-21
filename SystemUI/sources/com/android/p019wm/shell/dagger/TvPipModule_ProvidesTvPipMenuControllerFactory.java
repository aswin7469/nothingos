package com.android.p019wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.p019wm.shell.common.SystemWindows;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsState;
import com.android.p019wm.shell.pip.p020tv.TvPipMenuController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.TvPipModule_ProvidesTvPipMenuControllerFactory */
public final class TvPipModule_ProvidesTvPipMenuControllerFactory implements Factory<TvPipMenuController> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<PipMediaController> pipMediaControllerProvider;
    private final Provider<SystemWindows> systemWindowsProvider;
    private final Provider<TvPipBoundsState> tvPipBoundsStateProvider;

    public TvPipModule_ProvidesTvPipMenuControllerFactory(Provider<Context> provider, Provider<TvPipBoundsState> provider2, Provider<SystemWindows> provider3, Provider<PipMediaController> provider4, Provider<Handler> provider5) {
        this.contextProvider = provider;
        this.tvPipBoundsStateProvider = provider2;
        this.systemWindowsProvider = provider3;
        this.pipMediaControllerProvider = provider4;
        this.mainHandlerProvider = provider5;
    }

    public TvPipMenuController get() {
        return providesTvPipMenuController(this.contextProvider.get(), this.tvPipBoundsStateProvider.get(), this.systemWindowsProvider.get(), this.pipMediaControllerProvider.get(), this.mainHandlerProvider.get());
    }

    public static TvPipModule_ProvidesTvPipMenuControllerFactory create(Provider<Context> provider, Provider<TvPipBoundsState> provider2, Provider<SystemWindows> provider3, Provider<PipMediaController> provider4, Provider<Handler> provider5) {
        return new TvPipModule_ProvidesTvPipMenuControllerFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static TvPipMenuController providesTvPipMenuController(Context context, TvPipBoundsState tvPipBoundsState, SystemWindows systemWindows, PipMediaController pipMediaController, Handler handler) {
        return (TvPipMenuController) Preconditions.checkNotNullFromProvides(TvPipModule.providesTvPipMenuController(context, tvPipBoundsState, systemWindows, pipMediaController, handler));
    }
}
