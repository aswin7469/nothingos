package com.android.systemui.biometrics.dagger;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.util.concurrency.ThreadFactory;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/biometrics/dagger/BiometricsModule;", "", "()V", "providesPluginExecutor", "Ljava/util/concurrent/Executor;", "threadFactory", "Lcom/android/systemui/util/concurrency/ThreadFactory;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@Module
/* compiled from: BiometricsModule.kt */
public final class BiometricsModule {
    public static final BiometricsModule INSTANCE = new BiometricsModule();

    private BiometricsModule() {
    }

    @SysUISingleton
    @BiometricsBackground
    @JvmStatic
    @Provides
    public static final Executor providesPluginExecutor(ThreadFactory threadFactory) {
        Intrinsics.checkNotNullParameter(threadFactory, "threadFactory");
        Executor buildExecutorOnNewThread = threadFactory.buildExecutorOnNewThread("biometrics");
        Intrinsics.checkNotNullExpressionValue(buildExecutorOnNewThread, "threadFactory.buildExecu…OnNewThread(\"biometrics\")");
        return buildExecutorOnNewThread;
    }
}
