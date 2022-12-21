package com.android.systemui.assist;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.slice.Clock;
import com.android.internal.app.AssistUtils;
import com.android.systemui.dagger.SysUISingleton;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module
public abstract class AssistModule {
    static final String ASSIST_HANDLE_THREAD_NAME = "assist_handle_thread";
    static final String UPTIME_NAME = "uptime";

    @SysUISingleton
    @Provides
    @Named("assist_handle_thread")
    static Handler provideBackgroundHandler() {
        HandlerThread handlerThread = new HandlerThread("AssistHandleThread");
        handlerThread.start();
        return handlerThread.getThreadHandler();
    }

    @SysUISingleton
    @Provides
    static AssistUtils provideAssistUtils(Context context) {
        return new AssistUtils(context);
    }

    @SysUISingleton
    @Provides
    @Named("uptime")
    static Clock provideSystemClock() {
        return new AssistModule$$ExternalSyntheticLambda0();
    }
}
