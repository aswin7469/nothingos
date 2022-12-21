package dalvik.system;

import android.annotation.SystemApi;
import java.lang.ref.FinalizerReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class VMRuntime {
    private static final Map<String, String> ABI_TO_INSTRUCTION_SET_MAP;
    private static final long ALLOW_TEST_API_ACCESS = 166236554;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int CODE_PATH_TYPE_PRIMARY_APK = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int CODE_PATH_TYPE_SECONDARY_DEX = 4;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int CODE_PATH_TYPE_SPLIT_APK = 2;
    private static final long HIDE_MAXTARGETSDK_P_HIDDEN_APIS = 149997251;
    private static final long HIDE_MAXTARGETSDK_Q_HIDDEN_APIS = 149994052;
    private static final long PREVENT_META_REFLECTION_BLOCKLIST_ACCESS = 142365358;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int SDK_VERSION_CUR_DEVELOPMENT = 10000;
    private static final VMRuntime THE_ONE = new VMRuntime();
    static HiddenApiUsageLogger hiddenApiUsageLogger;
    private static Consumer<String> nonSdkApiUsageConsumer = null;
    private final AtomicInteger allocationCount = new AtomicInteger(0);
    private long[] disabledCompatChanges = new long[0];
    private int notifyNativeInterval;
    private int targetSdkVersion = 10000;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public interface HiddenApiUsageLogger {
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public static final int ACCESS_METHOD_JNI = 2;
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public static final int ACCESS_METHOD_LINKING = 3;
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public static final int ACCESS_METHOD_NONE = 0;
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public static final int ACCESS_METHOD_REFLECTION = 1;

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        void hiddenApiUsed(int i, String str, String str2, int i2, boolean z);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void bootCompleted();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native String getCurrentInstructionSet();

    private static native int getNotifyNativeInterval();

    public static native boolean isBootClassPathOnDisk(String str);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native boolean isValidClassLoaderContext(String str);

    private native void nativeSetTargetHeapUtilization(float f);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void registerAppInfo(String str, String str2, String str3, String[] strArr, int i);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void registerSensitiveThread();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void resetJitCounters();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void setDedupeHiddenApiWarnings(boolean z);

    private native void setDisabledCompatChangesNative(long[] jArr);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void setProcessDataDirectory(String str);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void setProcessPackageName(String str);

    public static native void setSystemDaemonThreadPriority();

    private native void setTargetSdkVersionNative(int i);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native long addressOf(Object obj);

    public native String bootClassPath();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native void clampGrowthLimit();

    public native String classPath();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native void clearGrowthLimit();

    @Deprecated
    public void gcSoftReferences() {
    }

    @Deprecated
    public long getExternalBytesAllocated() {
        return 0;
    }

    public native long getFinalizerTimeoutMs();

    @Deprecated
    public long getMinimumHeapSize() {
        return 0;
    }

    public native float getTargetHeapUtilization();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native boolean is64Bit();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native boolean isCheckJniEnabled();

    public native boolean isJavaDebuggable();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native boolean isNativeDebuggable();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native Object newNonMovableArray(Class<?> cls, int i);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native Object newUnpaddedArray(Class<?> cls, int i);

    public native void notifyNativeAllocationsInternal();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native void notifyStartupCompleted();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native void preloadDexCaches();

    public native String[] properties();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native void registerNativeAllocation(long j);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native void registerNativeFree(long j);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native void requestConcurrentGC();

    public native void requestHeapTrim();

    public native void runHeapTasks();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native void setHiddenApiAccessLogSamplingRate(int i);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native void setHiddenApiExemptions(String[] strArr);

    @Deprecated
    public long setMinimumHeapSize(long j) {
        return 0;
    }

    public native void startHeapTaskProcessor();

    public native void stopHeapTaskProcessor();

    @Deprecated
    public boolean trackExternalAllocation(long j) {
        return true;
    }

    @Deprecated
    public void trackExternalFree(long j) {
    }

    public native void trimHeap();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native void updateProcessState(int i);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native String vmInstructionSet();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public native String vmLibrary();

    public native String vmVersion();

    static {
        HashMap hashMap = new HashMap(16);
        ABI_TO_INSTRUCTION_SET_MAP = hashMap;
        hashMap.put("armeabi", "arm");
        hashMap.put("armeabi-v7a", "arm");
        hashMap.put("mips", "mips");
        hashMap.put("mips64", "mips64");
        hashMap.put("x86", "x86");
        hashMap.put("x86_64", "x86_64");
        hashMap.put("arm64-v8a", "arm64");
        hashMap.put("arm64-v8a-hwasan", "arm64");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setHiddenApiUsageLogger(HiddenApiUsageLogger hiddenApiUsageLogger2) {
        hiddenApiUsageLogger = hiddenApiUsageLogger2;
    }

    private static void hiddenApiUsed(int i, String str, String str2, int i2, boolean z) {
        HiddenApiUsageLogger hiddenApiUsageLogger2 = hiddenApiUsageLogger;
        if (hiddenApiUsageLogger2 != null) {
            hiddenApiUsageLogger2.hiddenApiUsed(i, str, str2, i2, z);
        }
    }

    private VMRuntime() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static VMRuntime getRuntime() {
        return THE_ONE;
    }

    public float setTargetHeapUtilization(float f) {
        float targetHeapUtilization;
        if (f <= 0.0f || f >= 1.0f) {
            throw new IllegalArgumentException(f + " out of range (0,1)");
        }
        if (f < 0.1f) {
            f = 0.1f;
        }
        synchronized (this) {
            targetHeapUtilization = getTargetHeapUtilization();
            nativeSetTargetHeapUtilization(f);
        }
        return targetHeapUtilization;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public synchronized void setTargetSdkVersion(int i) {
        this.targetSdkVersion = i;
        setTargetSdkVersionNative(i);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public synchronized void setDisabledCompatChanges(long[] jArr) {
        this.disabledCompatChanges = jArr;
        setDisabledCompatChangesNative(jArr);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public synchronized int getTargetSdkVersion() {
        return this.targetSdkVersion;
    }

    @Deprecated
    public void runFinalizationSync() {
        System.runFinalization();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    @Deprecated
    public void registerNativeAllocation(int i) {
        registerNativeAllocation((long) i);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    @Deprecated
    public void registerNativeFree(int i) {
        registerNativeFree((long) i);
    }

    public void notifyNativeAllocation() {
        int i = this.notifyNativeInterval;
        if (i == 0) {
            i = getNotifyNativeInterval();
            this.notifyNativeInterval = i;
        }
        if (this.allocationCount.addAndGet(1) % i == 0) {
            notifyNativeAllocationsInternal();
        }
    }

    public static void runFinalization(long j) {
        try {
            FinalizerReference.finalizeAllEnqueued(j);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static String getInstructionSet(String str) {
        String str2 = ABI_TO_INSTRUCTION_SET_MAP.get(str);
        if (str2 != null) {
            return str2;
        }
        throw new IllegalArgumentException("Unsupported ABI: " + str);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean is64BitInstructionSet(String str) {
        return "arm64".equals(str) || "x86_64".equals(str) || "mips64".equals(str);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean is64BitAbi(String str) {
        return is64BitInstructionSet(getInstructionSet(str));
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setNonSdkApiUsageConsumer(Consumer<String> consumer) {
        nonSdkApiUsageConsumer = consumer;
    }
}
