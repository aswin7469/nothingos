package libcore.util;

import android.annotation.SystemApi;
import dalvik.system.VMRuntime;
import java.lang.ref.Reference;
import sun.misc.Cleaner;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class NativeAllocationRegistry {
    private static final long IS_MALLOCED = 1;
    private final ClassLoader classLoader;
    /* access modifiers changed from: private */
    public final long freeFunction;
    /* access modifiers changed from: private */
    public final long size;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void applyFreeFunction(long j, long j2);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static NativeAllocationRegistry createNonmalloced(ClassLoader classLoader2, long j, long j2) {
        return new NativeAllocationRegistry(classLoader2, j, j2, false);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static NativeAllocationRegistry createMalloced(ClassLoader classLoader2, long j, long j2) {
        return new NativeAllocationRegistry(classLoader2, j, j2, true);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static NativeAllocationRegistry createMalloced(ClassLoader classLoader2, long j) {
        return new NativeAllocationRegistry(classLoader2, j, 0, true);
    }

    private NativeAllocationRegistry(ClassLoader classLoader2, long j, long j2, boolean z) {
        if (j2 >= 0) {
            this.classLoader = classLoader2;
            this.freeFunction = j;
            this.size = z ? 1 | j2 : -2 & j2;
            return;
        }
        throw new IllegalArgumentException("Invalid native allocation size: " + j2);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public NativeAllocationRegistry(ClassLoader classLoader2, long j, long j2) {
        this(classLoader2, j, j2, j2 == 0);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Runnable registerNativeAllocation(Object obj, long j) {
        if (obj == null) {
            throw new IllegalArgumentException("referent is null");
        } else if (j != 0) {
            try {
                CleanerThunk cleanerThunk = new CleanerThunk();
                CleanerRunner cleanerRunner = new CleanerRunner(Cleaner.create(obj, cleanerThunk));
                registerNativeAllocation(this.size);
                cleanerThunk.setNativePtr(j);
                Reference.reachabilityFence(obj);
                return cleanerRunner;
            } catch (VirtualMachineError e) {
                applyFreeFunction(this.freeFunction, j);
                throw e;
            }
        } else {
            throw new IllegalArgumentException("nativePtr is null");
        }
    }

    private class CleanerThunk implements Runnable {
        private long nativePtr = 0;

        public CleanerThunk() {
        }

        public void run() {
            if (this.nativePtr != 0) {
                NativeAllocationRegistry.applyFreeFunction(NativeAllocationRegistry.this.freeFunction, this.nativePtr);
                NativeAllocationRegistry.registerNativeFree(NativeAllocationRegistry.this.size);
            }
        }

        public void setNativePtr(long j) {
            this.nativePtr = j;
        }
    }

    private static class CleanerRunner implements Runnable {
        private final Cleaner cleaner;

        public CleanerRunner(Cleaner cleaner2) {
            this.cleaner = cleaner2;
        }

        public void run() {
            this.cleaner.clean();
        }
    }

    private static void registerNativeAllocation(long j) {
        VMRuntime runtime = VMRuntime.getRuntime();
        if ((1 & j) == 0) {
            runtime.registerNativeAllocation(j);
        } else if (j >= 300000) {
            runtime.notifyNativeAllocationsInternal();
        } else {
            runtime.notifyNativeAllocation();
        }
    }

    /* access modifiers changed from: private */
    public static void registerNativeFree(long j) {
        if ((1 & j) == 0) {
            VMRuntime.getRuntime().registerNativeFree(j);
        }
    }
}
