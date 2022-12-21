package dalvik.system;

import android.annotation.SystemApi;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.HashMap;
import java.util.Map;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class VMDebug {
    private static final int KIND_ALLOCATED_BYTES = 2;
    private static final int KIND_ALLOCATED_OBJECTS = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_ALL_COUNTS = -1;
    private static final int KIND_CLASS_INIT_COUNT = 32;
    private static final int KIND_CLASS_INIT_TIME = 64;
    private static final int KIND_EXT_ALLOCATED_BYTES = 8192;
    private static final int KIND_EXT_ALLOCATED_OBJECTS = 4096;
    private static final int KIND_EXT_FREED_BYTES = 32768;
    private static final int KIND_EXT_FREED_OBJECTS = 16384;
    private static final int KIND_FREED_BYTES = 8;
    private static final int KIND_FREED_OBJECTS = 4;
    private static final int KIND_GC_INVOCATIONS = 16;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_GLOBAL_ALLOCATED_BYTES = 2;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_GLOBAL_ALLOCATED_OBJECTS = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_GLOBAL_CLASS_INIT_COUNT = 32;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_GLOBAL_CLASS_INIT_TIME = 64;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_GLOBAL_FREED_BYTES = 8;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_GLOBAL_FREED_OBJECTS = 4;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_GLOBAL_GC_INVOCATIONS = 16;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_THREAD_ALLOCATED_BYTES = 131072;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_THREAD_ALLOCATED_OBJECTS = 65536;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int KIND_THREAD_GC_INVOCATIONS = 1048576;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int TRACE_COUNT_ALLOCS = 1;
    private static final HashMap<String, Integer> runtimeStatsMap;

    public static native void allowHiddenApiReflectionFrom(Class<?> cls);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native long countInstancesOfClass(Class cls, boolean z);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native long[] countInstancesOfClasses(Class[] clsArr, boolean z);

    private static native void dumpHprofData(String str, int i) throws IOException;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void dumpHprofDataDdms();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void dumpReferenceTables();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native int getAllocCount(int i);

    public static native Object[][] getInstancesOfClasses(Class[] clsArr, boolean z);

    @Deprecated
    public static void getInstructionCount(int[] iArr) {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native int getLoadedClassCount();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native int getMethodTracingMode();

    private static native String getRuntimeStatInternal(int i);

    private static native String[] getRuntimeStatsInternal();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native String[] getVmFeatureList();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native boolean isDebuggerConnected();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native boolean isDebuggingEnabled();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native long lastDebuggerActivity();

    private static native void nativeAttachAgent(String str, ClassLoader classLoader) throws IOException;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void printLoadedClasses(int i);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void resetAllocCount(int i);

    @Deprecated
    public static void resetInstructionCount() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void setAllocTrackerStackDepth(int i);

    @Deprecated
    public static int setAllocationLimit(int i) {
        return -1;
    }

    @Deprecated
    public static int setGlobalAllocationLimit(int i) {
        return -1;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void startAllocCounting();

    @Deprecated
    public static void startInstructionCounting() {
    }

    private static native void startMethodTracingDdmsImpl(int i, int i2, boolean z, int i3);

    private static native void startMethodTracingFd(String str, int i, int i2, int i3, boolean z, int i4, boolean z2);

    private static native void startMethodTracingFilename(String str, int i, int i2, boolean z, int i3);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void stopAllocCounting();

    @Deprecated
    public static void stopInstructionCounting() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void stopMethodTracing();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native long threadCpuTimeNanos();

    private VMDebug() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void startMethodTracing(String str, int i, int i2, boolean z, int i3) {
        startMethodTracingFilename(str, checkBufferSize(i), i2, z, i3);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void startMethodTracing(String str, FileDescriptor fileDescriptor, int i, int i2, boolean z, int i3, boolean z2) {
        if (fileDescriptor != null) {
            startMethodTracingFd(str, fileDescriptor.getInt$(), checkBufferSize(i), i2, z, i3, z2);
            return;
        }
        throw new NullPointerException("fd == null");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void startMethodTracingDdms(int i, int i2, boolean z, int i3) {
        startMethodTracingDdmsImpl(checkBufferSize(i), i2, z, i3);
    }

    private static int checkBufferSize(int i) {
        if (i == 0) {
            i = 8388608;
        }
        if (i >= 1024) {
            return i;
        }
        throw new IllegalArgumentException("buffer size < 1024: " + i);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void dumpHprofData(String str) throws IOException {
        if (str != null) {
            dumpHprofData(str, (FileDescriptor) null);
            return;
        }
        throw new NullPointerException("filename == null");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void dumpHprofData(String str, FileDescriptor fileDescriptor) throws IOException {
        dumpHprofData(str, fileDescriptor != null ? fileDescriptor.getInt$() : -1);
    }

    static {
        HashMap<String, Integer> hashMap = new HashMap<>();
        runtimeStatsMap = hashMap;
        hashMap.put("art.gc.gc-count", 0);
        hashMap.put("art.gc.gc-time", 1);
        hashMap.put("art.gc.bytes-allocated", 2);
        hashMap.put("art.gc.bytes-freed", 3);
        hashMap.put("art.gc.blocking-gc-count", 4);
        hashMap.put("art.gc.blocking-gc-time", 5);
        hashMap.put("art.gc.gc-count-rate-histogram", 6);
        hashMap.put("art.gc.blocking-gc-count-rate-histogram", 7);
        hashMap.put("art.gc.objects-allocated", 8);
        hashMap.put("art.gc.total-time-waiting-for-gc", 9);
        hashMap.put("art.gc.pre-oome-gc-count", 10);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static String getRuntimeStat(String str) {
        if (str != null) {
            Integer num = runtimeStatsMap.get(str);
            if (num != null) {
                return getRuntimeStatInternal(num.intValue());
            }
            return null;
        }
        throw new NullPointerException("statName == null");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static Map<String, String> getRuntimeStats() {
        HashMap hashMap = new HashMap();
        String[] runtimeStatsInternal = getRuntimeStatsInternal();
        for (String next : runtimeStatsMap.keySet()) {
            hashMap.put(next, runtimeStatsInternal[runtimeStatsMap.get(next).intValue()]);
        }
        return hashMap;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void attachAgent(String str, ClassLoader classLoader) throws IOException {
        nativeAttachAgent(str, classLoader);
    }
}
