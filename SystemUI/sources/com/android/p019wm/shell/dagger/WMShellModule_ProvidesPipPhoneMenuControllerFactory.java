package com.android.p019wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SystemWindows;
import com.android.p019wm.shell.pip.PipBoundsState;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import com.android.p019wm.shell.pip.phone.PhonePipMenuController;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellModule_ProvidesPipPhoneMenuControllerFactory */
public final class WMShellModule_ProvidesPipPhoneMenuControllerFactory implements Factory<PhonePipMenuController> {
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PipMediaController> pipMediaControllerProvider;
    private final Provider<PipUiEventLogger> pipUiEventLoggerProvider;
    private final Provider<Optional<SplitScreenController>> splitScreenOptionalProvider;
    private final Provider<SystemWindows> systemWindowsProvider;

    public WMShellModule_ProvidesPipPhoneMenuControllerFactory(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipMediaController> provider3, Provider<SystemWindows> provider4, Provider<Optional<SplitScreenController>> provider5, Provider<PipUiEventLogger> provider6, Provider<ShellExecutor> provider7, Provider<Handler> provider8) {
        this.contextProvider = provider;
        this.pipBoundsStateProvider = provider2;
        this.pipMediaControllerProvider = provider3;
        this.systemWindowsProvider = provider4;
        this.splitScreenOptionalProvider = provider5;
        this.pipUiEventLoggerProvider = provider6;
        this.mainExecutorProvider = provider7;
        this.mainHandlerProvider = provider8;
    }

    public PhonePipMenuController get() {
        return providesPipPhoneMenuController(this.contextProvider.get(), this.pipBoundsStateProvider.get(), this.pipMediaControllerProvider.get(), this.systemWindowsProvider.get(), this.splitScreenOptionalProvider.get(), this.pipUiEventLoggerProvider.get(), this.mainExecutorProvider.get(), this.mainHandlerProvider.get());
    }

    public static WMShellModule_ProvidesPipPhoneMenuControllerFactory create(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipMediaController> provider3, Provider<SystemWindows> provider4, Provider<Optional<SplitScreenController>> provider5, Provider<PipUiEventLogger> provider6, Provider<ShellExecutor> provider7, Provider<Handler> provider8) {
        return new WMShellModule_ProvidesPipPhoneMenuControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static PhonePipMenuController providesPipPhoneMenuController(Context context, PipBoundsState pipBoundsState, PipMediaController pipMediaController, SystemWindows systemWindows, Optional<SplitScreenController> optional, PipUiEventLogger pipUiEventLogger, ShellExecutor shellExecutor, Handler handler) {
        return (PhonePipMenuController) Preconditions.checkNotNullFromProvides(WMShellModule.providesPipPhoneMenuController(context, pipBoundsState, pipMediaController, systemWindows, optional, pipUiEventLogger, shellExecutor, handler));
    }
}
