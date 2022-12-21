package dalvik.system;

import android.annotation.SystemApi;
import java.lang.reflect.Method;
import java.p026io.File;
import java.p026io.FileDescriptor;
import java.util.Locale;
import libcore.icu.DecimalFormatData;
import libcore.icu.ICU;
import libcore.icu.SimpleDateFormatData;
import sun.util.locale.BaseLocale;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class ZygoteHooks {
    private static Method enableMemoryMappedDataMethod = null;
    private static boolean inZygoteProcess = true;
    private static long token;

    private static native void nativePostForkChild(long j, int i, boolean z, boolean z2, String str);

    private static native void nativePostForkSystemServer(int i);

    private static native void nativePostZygoteFork();

    private static native long nativePreFork();

    private static native boolean nativeZygoteLongSuspendOk();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void startZygoteNoThreadCreation();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void stopZygoteNoThreadCreation();

    private ZygoteHooks() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void onBeginPreload() {
        com.android.i18n.system.ZygoteHooks.onBeginPreload();
        ICU.initializeCacheInZygote();
        DecimalFormatData.initializeCacheInZygote();
        SimpleDateFormatData.initializeCacheInZygote();
        try {
            enableMemoryMappedDataMethod = Class.forName("org.jacoco.agent.rt.internal.Offline").getMethod("enableMemoryMappedData", new Class[0]);
        } catch (ClassNotFoundException unused) {
        } catch (NoSuchMethodException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void onEndPreload() {
        com.android.i18n.system.ZygoteHooks.onEndPreload();
        FileDescriptor.f518in.cloneForFork();
        FileDescriptor.out.cloneForFork();
        FileDescriptor.err.cloneForFork();
    }

    private static void cleanLocaleCaches() {
        BaseLocale.cleanCache();
        Locale.cleanCache();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void gcAndFinalize() {
        VMRuntime runtime = VMRuntime.getRuntime();
        System.m1693gc();
        runtime.runFinalizationSync();
        cleanLocaleCaches();
        System.m1693gc();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void preFork() {
        Daemons.stop();
        token = nativePreFork();
        waitUntilAllThreadsStopped();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void postForkSystemServer(int i) {
        nativePostForkSystemServer(i);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void postForkChild(int i, boolean z, boolean z2, String str) {
        Method method;
        nativePostForkChild(token, i, z, z2, str);
        if (!z2) {
            inZygoteProcess = false;
        }
        Math.setRandomSeedInternal(System.currentTimeMillis());
        if (!z && (method = enableMemoryMappedDataMethod) != null) {
            try {
                method.invoke((Object) null, new Object[0]);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException((Throwable) e);
            }
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void postForkCommon() {
        nativePostZygoteFork();
        Daemons.startPostZygoteFork();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean isIndefiniteThreadSuspensionSafe() {
        return nativeZygoteLongSuspendOk();
    }

    public static boolean inZygote() {
        return inZygoteProcess;
    }

    private static void waitUntilAllThreadsStopped() {
        File file = new File("/proc/self/task");
        while (file.list().length > 1) {
            Thread.yield();
        }
    }
}
