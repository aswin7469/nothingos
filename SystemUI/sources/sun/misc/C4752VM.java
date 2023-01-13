package sun.misc;

import java.lang.Thread;
import java.util.Properties;

/* renamed from: sun.misc.VM */
public class C4752VM {
    private static final int JVMTI_THREAD_STATE_ALIVE = 1;
    private static final int JVMTI_THREAD_STATE_BLOCKED_ON_MONITOR_ENTER = 1024;
    private static final int JVMTI_THREAD_STATE_RUNNABLE = 4;
    private static final int JVMTI_THREAD_STATE_TERMINATED = 2;
    private static final int JVMTI_THREAD_STATE_WAITING_INDEFINITELY = 16;
    private static final int JVMTI_THREAD_STATE_WAITING_WITH_TIMEOUT = 32;
    @Deprecated
    public static final int STATE_GREEN = 1;
    @Deprecated
    public static final int STATE_RED = 3;
    @Deprecated
    public static final int STATE_YELLOW = 2;
    private static boolean allowArraySyntax = false;
    private static volatile boolean booted = false;
    private static boolean defaultAllowArraySyntax = false;
    private static long directMemory = 67108864;
    private static volatile int finalRefCount = 0;
    private static final Object lock = new Object();
    private static boolean pageAlignDirectMemory = false;
    private static volatile int peakFinalRefCount = 0;
    private static final Properties savedProps = new Properties();
    private static boolean suspended = false;

    @Deprecated
    public static void asChange(int i, int i2) {
    }

    @Deprecated
    public static void asChange_otherthread(int i, int i2) {
    }

    @Deprecated
    public static final int getState() {
        return 1;
    }

    public static void initializeOSEnvironment() {
    }

    @Deprecated
    public static void unsuspendSomeThreads() {
    }

    @Deprecated
    public static boolean threadsSuspended() {
        return suspended;
    }

    public static boolean allowThreadSuspension(ThreadGroup threadGroup, boolean z) {
        return threadGroup.allowThreadSuspension(z);
    }

    @Deprecated
    public static boolean suspendThreads() {
        suspended = true;
        return true;
    }

    @Deprecated
    public static void unsuspendThreads() {
        suspended = false;
    }

    public static void booted() {
        Object obj = lock;
        synchronized (obj) {
            booted = true;
            obj.notifyAll();
        }
    }

    public static boolean isBooted() {
        return booted;
    }

    public static void awaitBooted() throws InterruptedException {
        synchronized (lock) {
            while (!booted) {
                lock.wait();
            }
        }
    }

    public static long maxDirectMemory() {
        return directMemory;
    }

    public static boolean isDirectMemoryPageAligned() {
        return pageAlignDirectMemory;
    }

    public static boolean allowArraySyntax() {
        return allowArraySyntax;
    }

    public static String getSavedProperty(String str) {
        return savedProps.getProperty(str);
    }

    public static void saveAndRemoveProperties(Properties properties) {
        boolean z;
        if (!booted) {
            savedProps.putAll(properties);
            String str = (String) properties.remove("sun.nio.MaxDirectMemorySize");
            if (str != null) {
                if (str.equals("-1")) {
                    directMemory = Runtime.getRuntime().maxMemory();
                } else {
                    long parseLong = Long.parseLong(str);
                    if (parseLong > -1) {
                        directMemory = parseLong;
                    }
                }
            }
            if ("true".equals((String) properties.remove("sun.nio.PageAlignDirectMemory"))) {
                pageAlignDirectMemory = true;
            }
            String property = properties.getProperty("sun.lang.ClassLoader.allowArraySyntax");
            if (property == null) {
                z = defaultAllowArraySyntax;
            } else {
                z = Boolean.parseBoolean(property);
            }
            allowArraySyntax = z;
            properties.remove("java.lang.Integer.IntegerCache.high");
            properties.remove("sun.zip.disableMemoryMapping");
            properties.remove("sun.java.launcher.diag");
            properties.remove("sun.cds.enableSharedLookupCache");
            return;
        }
        throw new IllegalStateException("System initialization has completed");
    }

    public static int getFinalRefCount() {
        return finalRefCount;
    }

    public static int getPeakFinalRefCount() {
        return peakFinalRefCount;
    }

    public static void addFinalRefCount(int i) {
        finalRefCount += i;
        if (finalRefCount > peakFinalRefCount) {
            peakFinalRefCount = finalRefCount;
        }
    }

    public static Thread.State toThreadState(int i) {
        if ((i & 4) != 0) {
            return Thread.State.RUNNABLE;
        }
        if ((i & 1024) != 0) {
            return Thread.State.BLOCKED;
        }
        if ((i & 16) != 0) {
            return Thread.State.WAITING;
        }
        if ((i & 32) != 0) {
            return Thread.State.TIMED_WAITING;
        }
        if ((i & 2) != 0) {
            return Thread.State.TERMINATED;
        }
        if ((i & 1) == 0) {
            return Thread.State.NEW;
        }
        return Thread.State.RUNNABLE;
    }
}
