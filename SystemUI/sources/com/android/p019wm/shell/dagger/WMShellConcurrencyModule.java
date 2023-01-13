package com.android.p019wm.shell.dagger;

import android.animation.AnimationHandler;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.common.HandlerExecutor;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.annotations.ChoreographerSfVsync;
import com.android.p019wm.shell.common.annotations.ExternalMainThread;
import com.android.p019wm.shell.common.annotations.ShellAnimationThread;
import com.android.p019wm.shell.common.annotations.ShellBackgroundThread;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import com.android.p019wm.shell.common.annotations.ShellSplashscreenThread;
import dagger.Module;
import dagger.Provides;

@Module
/* renamed from: com.android.wm.shell.dagger.WMShellConcurrencyModule */
public abstract class WMShellConcurrencyModule {
    private static final int MSGQ_SLOW_DELIVERY_THRESHOLD_MS = 30;
    private static final int MSGQ_SLOW_DISPATCH_THRESHOLD_MS = 30;

    public static boolean enableShellMainThread(Context context) {
        return context.getResources().getBoolean(C3353R.bool.config_enableShellMainThread);
    }

    @ExternalMainThread
    @Provides
    public static Handler provideMainHandler() {
        return new Handler(Looper.getMainLooper());
    }

    @WMSingleton
    @ExternalMainThread
    @Provides
    public static ShellExecutor provideSysUIMainExecutor(@ExternalMainThread Handler handler) {
        return new HandlerExecutor(handler);
    }

    public static HandlerThread createShellMainThread() {
        return new HandlerThread("wmshell.main", -4);
    }

    @WMSingleton
    @ShellMainThread
    @Provides
    public static Handler provideShellMainHandler(Context context, @ShellMainThread HandlerThread handlerThread, @ExternalMainThread Handler handler) {
        if (!enableShellMainThread(context)) {
            return handler;
        }
        if (handlerThread == null) {
            handlerThread = createShellMainThread();
            handlerThread.start();
        }
        if (Build.IS_DEBUGGABLE) {
            handlerThread.getLooper().setTraceTag(32);
            handlerThread.getLooper().setSlowLogThresholdMs(30, 30);
        }
        return Handler.createAsync(handlerThread.getLooper());
    }

    @WMSingleton
    @ShellMainThread
    @Provides
    public static ShellExecutor provideShellMainExecutor(Context context, @ShellMainThread Handler handler, @ExternalMainThread ShellExecutor shellExecutor) {
        return enableShellMainThread(context) ? new HandlerExecutor(handler) : shellExecutor;
    }

    @ShellAnimationThread
    @WMSingleton
    @Provides
    public static ShellExecutor provideShellAnimationExecutor() {
        HandlerThread handlerThread = new HandlerThread("wmshell.anim", -4);
        handlerThread.start();
        if (Build.IS_DEBUGGABLE) {
            handlerThread.getLooper().setTraceTag(32);
            handlerThread.getLooper().setSlowLogThresholdMs(30, 30);
        }
        return new HandlerExecutor(Handler.createAsync(handlerThread.getLooper()));
    }

    @WMSingleton
    @ShellSplashscreenThread
    @Provides
    public static ShellExecutor provideSplashScreenExecutor() {
        HandlerThread handlerThread = new HandlerThread("wmshell.splashscreen", -10);
        handlerThread.start();
        return new HandlerExecutor(handlerThread.getThreadHandler());
    }

    @ChoreographerSfVsync
    @WMSingleton
    @Provides
    public static AnimationHandler provideShellMainExecutorSfVsyncAnimationHandler(@ShellMainThread ShellExecutor shellExecutor) {
        try {
            AnimationHandler animationHandler = new AnimationHandler();
            shellExecutor.executeBlocking(new WMShellConcurrencyModule$$ExternalSyntheticLambda0(animationHandler));
            return animationHandler;
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to initialize SfVsync animation handler in 1s", e);
        }
    }

    @ShellBackgroundThread
    @WMSingleton
    @Provides
    public static Handler provideSharedBackgroundHandler() {
        HandlerThread handlerThread = new HandlerThread("wmshell.background", 10);
        handlerThread.start();
        return handlerThread.getThreadHandler();
    }

    @ShellBackgroundThread
    @WMSingleton
    @Provides
    public static ShellExecutor provideSharedBackgroundExecutor(@ShellBackgroundThread Handler handler) {
        return new HandlerExecutor(handler);
    }
}
