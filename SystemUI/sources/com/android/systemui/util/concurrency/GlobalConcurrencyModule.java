package com.android.systemui.util.concurrency;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dagger.qualifiers.UiBackground;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Singleton;

@Module
public abstract class GlobalConcurrencyModule {
    public static final String PRE_HANDLER = "pre_handler";

    @Binds
    public abstract ThreadFactory bindExecutorFactory(ThreadFactoryImpl threadFactoryImpl);

    @Singleton
    @Binds
    public abstract Execution provideExecution(ExecutionImpl executionImpl);

    @Main
    @Provides
    public static Looper provideMainLooper() {
        return Looper.getMainLooper();
    }

    @Main
    @Provides
    public static Handler provideMainHandler(@Main Looper looper) {
        return new Handler(looper);
    }

    @Deprecated
    @Provides
    public static Handler provideHandler() {
        return new Handler();
    }

    @Singleton
    @UiBackground
    @Provides
    public static Executor provideUiBackgroundExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Singleton
    @Main
    @Provides
    public static Executor provideMainExecutor(Context context) {
        return context.getMainExecutor();
    }

    @Singleton
    @Main
    @Provides
    public static DelayableExecutor provideMainDelayableExecutor(@Main Looper looper) {
        return new ExecutorImpl(looper);
    }
}
