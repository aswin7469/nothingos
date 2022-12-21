package com.android.p019wm.shell.dagger;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.draganddrop.DragAndDropController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropControllerFactory */
public final class WMShellBaseModule_ProvideDragAndDropControllerFactory implements Factory<DragAndDropController> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<IconProvider> iconProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public WMShellBaseModule_ProvideDragAndDropControllerFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<UiEventLogger> provider3, Provider<IconProvider> provider4, Provider<ShellExecutor> provider5) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
        this.iconProvider = provider4;
        this.mainExecutorProvider = provider5;
    }

    public DragAndDropController get() {
        return provideDragAndDropController(this.contextProvider.get(), this.displayControllerProvider.get(), this.uiEventLoggerProvider.get(), this.iconProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideDragAndDropControllerFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<UiEventLogger> provider3, Provider<IconProvider> provider4, Provider<ShellExecutor> provider5) {
        return new WMShellBaseModule_ProvideDragAndDropControllerFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static DragAndDropController provideDragAndDropController(Context context, DisplayController displayController, UiEventLogger uiEventLogger, IconProvider iconProvider2, ShellExecutor shellExecutor) {
        return (DragAndDropController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDragAndDropController(context, displayController, uiEventLogger, iconProvider2, shellExecutor));
    }
}
