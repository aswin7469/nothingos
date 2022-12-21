package com.android.systemui.util.concurrency;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.LongRunning;
import com.android.systemui.dagger.qualifiers.Main;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import javax.inject.Named;

@Module
public abstract class SysUIConcurrencyModule {
    @SysUISingleton
    @Background
    @Provides
    public static Looper provideBgLooper() {
        HandlerThread handlerThread = new HandlerThread("SysUiBg", 10);
        handlerThread.start();
        return handlerThread.getLooper();
    }

    @SysUISingleton
    @LongRunning
    @Provides
    public static Looper provideLongRunningLooper() {
        HandlerThread handlerThread = new HandlerThread("SysUiLng", 10);
        handlerThread.start();
        return handlerThread.getLooper();
    }

    @Background
    @Provides
    public static Handler provideBgHandler(@Background Looper looper) {
        return new Handler(looper);
    }

    @SysUISingleton
    @Provides
    public static Executor provideExecutor(@Background Looper looper) {
        return new ExecutorImpl(looper);
    }

    @SysUISingleton
    @LongRunning
    @Provides
    public static Executor provideLongRunningExecutor(@LongRunning Looper looper) {
        return new ExecutorImpl(looper);
    }

    @SysUISingleton
    @Background
    @Provides
    public static Executor provideBackgroundExecutor(@Background Looper looper) {
        return new ExecutorImpl(looper);
    }

    @SysUISingleton
    @Provides
    public static DelayableExecutor provideDelayableExecutor(@Background Looper looper) {
        return new ExecutorImpl(looper);
    }

    @SysUISingleton
    @Background
    @Provides
    public static DelayableExecutor provideBackgroundDelayableExecutor(@Background Looper looper) {
        return new ExecutorImpl(looper);
    }

    @SysUISingleton
    @Provides
    public static RepeatableExecutor provideRepeatableExecutor(@Background DelayableExecutor delayableExecutor) {
        return new RepeatableExecutorImpl(delayableExecutor);
    }

    @SysUISingleton
    @Background
    @Provides
    public static RepeatableExecutor provideBackgroundRepeatableExecutor(@Background DelayableExecutor delayableExecutor) {
        return new RepeatableExecutorImpl(delayableExecutor);
    }

    @SysUISingleton
    @Main
    @Provides
    public static RepeatableExecutor provideMainRepeatableExecutor(@Main DelayableExecutor delayableExecutor) {
        return new RepeatableExecutorImpl(delayableExecutor);
    }

    @Main
    @Provides
    public static MessageRouter providesMainMessageRouter(@Main DelayableExecutor delayableExecutor) {
        return new MessageRouterImpl(delayableExecutor);
    }

    @Background
    @Provides
    public static MessageRouter providesBackgroundMessageRouter(@Background DelayableExecutor delayableExecutor) {
        return new MessageRouterImpl(delayableExecutor);
    }

    @SysUISingleton
    @Provides
    @Named("time_tick_handler")
    public static Handler provideTimeTickHandler() {
        HandlerThread handlerThread = new HandlerThread("TimeTick");
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }
}
