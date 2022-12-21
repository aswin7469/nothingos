package com.android.systemui.media.taptotransfer.receiver;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.view.WindowManager;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.gesture.TapGestureDetector;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.view.ViewUtil;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaTttChipControllerReceiver_Factory implements Factory<MediaTttChipControllerReceiver> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<MediaTttLogger> loggerProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<PowerManager> powerManagerProvider;
    private final Provider<TapGestureDetector> tapGestureDetectorProvider;
    private final Provider<MediaTttReceiverUiEventLogger> uiEventLoggerProvider;
    private final Provider<ViewUtil> viewUtilProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public MediaTttChipControllerReceiver_Factory(Provider<CommandQueue> provider, Provider<Context> provider2, Provider<MediaTttLogger> provider3, Provider<WindowManager> provider4, Provider<ViewUtil> provider5, Provider<DelayableExecutor> provider6, Provider<TapGestureDetector> provider7, Provider<PowerManager> provider8, Provider<Handler> provider9, Provider<MediaTttReceiverUiEventLogger> provider10) {
        this.commandQueueProvider = provider;
        this.contextProvider = provider2;
        this.loggerProvider = provider3;
        this.windowManagerProvider = provider4;
        this.viewUtilProvider = provider5;
        this.mainExecutorProvider = provider6;
        this.tapGestureDetectorProvider = provider7;
        this.powerManagerProvider = provider8;
        this.mainHandlerProvider = provider9;
        this.uiEventLoggerProvider = provider10;
    }

    public MediaTttChipControllerReceiver get() {
        return newInstance(this.commandQueueProvider.get(), this.contextProvider.get(), this.loggerProvider.get(), this.windowManagerProvider.get(), this.viewUtilProvider.get(), this.mainExecutorProvider.get(), this.tapGestureDetectorProvider.get(), this.powerManagerProvider.get(), this.mainHandlerProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static MediaTttChipControllerReceiver_Factory create(Provider<CommandQueue> provider, Provider<Context> provider2, Provider<MediaTttLogger> provider3, Provider<WindowManager> provider4, Provider<ViewUtil> provider5, Provider<DelayableExecutor> provider6, Provider<TapGestureDetector> provider7, Provider<PowerManager> provider8, Provider<Handler> provider9, Provider<MediaTttReceiverUiEventLogger> provider10) {
        return new MediaTttChipControllerReceiver_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static MediaTttChipControllerReceiver newInstance(CommandQueue commandQueue, Context context, MediaTttLogger mediaTttLogger, WindowManager windowManager, ViewUtil viewUtil, DelayableExecutor delayableExecutor, TapGestureDetector tapGestureDetector, PowerManager powerManager, Handler handler, MediaTttReceiverUiEventLogger mediaTttReceiverUiEventLogger) {
        return new MediaTttChipControllerReceiver(commandQueue, context, mediaTttLogger, windowManager, viewUtil, delayableExecutor, tapGestureDetector, powerManager, handler, mediaTttReceiverUiEventLogger);
    }
}
