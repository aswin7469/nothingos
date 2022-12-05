package com.android.systemui.doze.dagger;

import com.android.systemui.doze.DozeAuthRemover;
import com.android.systemui.doze.DozeDockHandler;
import com.android.systemui.doze.DozeFalsingManagerAdapter;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozePauser;
import com.android.systemui.doze.DozeScreenBrightness;
import com.android.systemui.doze.DozeScreenState;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.doze.DozeUi;
import com.android.systemui.doze.DozeWallpaperState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeModule_ProvidesDozeMachinePartesFactory implements Factory<DozeMachine.Part[]> {
    private final Provider<DozeAuthRemover> dozeAuthRemoverProvider;
    private final Provider<DozeDockHandler> dozeDockHandlerProvider;
    private final Provider<DozeFalsingManagerAdapter> dozeFalsingManagerAdapterProvider;
    private final Provider<DozePauser> dozePauserProvider;
    private final Provider<DozeScreenBrightness> dozeScreenBrightnessProvider;
    private final Provider<DozeScreenState> dozeScreenStateProvider;
    private final Provider<DozeTriggers> dozeTriggersProvider;
    private final Provider<DozeUi> dozeUiProvider;
    private final Provider<DozeWallpaperState> dozeWallpaperStateProvider;

    public DozeModule_ProvidesDozeMachinePartesFactory(Provider<DozePauser> provider, Provider<DozeFalsingManagerAdapter> provider2, Provider<DozeTriggers> provider3, Provider<DozeUi> provider4, Provider<DozeScreenState> provider5, Provider<DozeScreenBrightness> provider6, Provider<DozeWallpaperState> provider7, Provider<DozeDockHandler> provider8, Provider<DozeAuthRemover> provider9) {
        this.dozePauserProvider = provider;
        this.dozeFalsingManagerAdapterProvider = provider2;
        this.dozeTriggersProvider = provider3;
        this.dozeUiProvider = provider4;
        this.dozeScreenStateProvider = provider5;
        this.dozeScreenBrightnessProvider = provider6;
        this.dozeWallpaperStateProvider = provider7;
        this.dozeDockHandlerProvider = provider8;
        this.dozeAuthRemoverProvider = provider9;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public DozeMachine.Part[] mo1933get() {
        return providesDozeMachinePartes(this.dozePauserProvider.mo1933get(), this.dozeFalsingManagerAdapterProvider.mo1933get(), this.dozeTriggersProvider.mo1933get(), this.dozeUiProvider.mo1933get(), this.dozeScreenStateProvider.mo1933get(), this.dozeScreenBrightnessProvider.mo1933get(), this.dozeWallpaperStateProvider.mo1933get(), this.dozeDockHandlerProvider.mo1933get(), this.dozeAuthRemoverProvider.mo1933get());
    }

    public static DozeModule_ProvidesDozeMachinePartesFactory create(Provider<DozePauser> provider, Provider<DozeFalsingManagerAdapter> provider2, Provider<DozeTriggers> provider3, Provider<DozeUi> provider4, Provider<DozeScreenState> provider5, Provider<DozeScreenBrightness> provider6, Provider<DozeWallpaperState> provider7, Provider<DozeDockHandler> provider8, Provider<DozeAuthRemover> provider9) {
        return new DozeModule_ProvidesDozeMachinePartesFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static DozeMachine.Part[] providesDozeMachinePartes(DozePauser dozePauser, DozeFalsingManagerAdapter dozeFalsingManagerAdapter, DozeTriggers dozeTriggers, DozeUi dozeUi, DozeScreenState dozeScreenState, DozeScreenBrightness dozeScreenBrightness, DozeWallpaperState dozeWallpaperState, DozeDockHandler dozeDockHandler, DozeAuthRemover dozeAuthRemover) {
        return (DozeMachine.Part[]) Preconditions.checkNotNullFromProvides(DozeModule.providesDozeMachinePartes(dozePauser, dozeFalsingManagerAdapter, dozeTriggers, dozeUi, dozeScreenState, dozeScreenBrightness, dozeWallpaperState, dozeDockHandler, dozeAuthRemover));
    }
}
