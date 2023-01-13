package java.util.concurrent.locks;

import java.util.concurrent.ThreadLocalRandom;
import jdk.internal.misc.Unsafe;

public class LockSupport {
    private static final long PARKBLOCKER;
    private static final long SECONDARY;
    private static final long TID;

    /* renamed from: U */
    private static final Unsafe f767U;

    private LockSupport() {
    }

    private static void setBlocker(Thread thread, Object obj) {
        f767U.putObject(thread, PARKBLOCKER, obj);
    }

    public static void unpark(Thread thread) {
        if (thread != null) {
            f767U.unpark(thread);
        }
    }

    public static void park(Object obj) {
        Thread currentThread = Thread.currentThread();
        setBlocker(currentThread, obj);
        f767U.park(false, 0);
        setBlocker(currentThread, (Object) null);
    }

    public static void parkNanos(Object obj, long j) {
        if (j > 0) {
            Thread currentThread = Thread.currentThread();
            setBlocker(currentThread, obj);
            f767U.park(false, j);
            setBlocker(currentThread, (Object) null);
        }
    }

    public static void parkUntil(Object obj, long j) {
        Thread currentThread = Thread.currentThread();
        setBlocker(currentThread, obj);
        f767U.park(true, j);
        setBlocker(currentThread, (Object) null);
    }

    public static Object getBlocker(Thread thread) {
        thread.getClass();
        return f767U.getObjectVolatile(thread, PARKBLOCKER);
    }

    public static void park() {
        f767U.park(false, 0);
    }

    public static void parkNanos(long j) {
        if (j > 0) {
            f767U.park(false, j);
        }
    }

    public static void parkUntil(long j) {
        f767U.park(true, j);
    }

    static final int nextSecondarySeed() {
        int i;
        Thread currentThread = Thread.currentThread();
        Unsafe unsafe = f767U;
        long j = SECONDARY;
        int i2 = unsafe.getInt(currentThread, j);
        if (i2 != 0) {
            int i3 = i2 ^ (i2 << 13);
            int i4 = i3 ^ (i3 >>> 17);
            i = i4 ^ (i4 << 5);
        } else {
            i = ThreadLocalRandom.current().nextInt();
            if (i == 0) {
                i = 1;
            }
        }
        unsafe.putInt(currentThread, j, i);
        return i;
    }

    static final long getThreadId(Thread thread) {
        return f767U.getLong(thread, TID);
    }

    static {
        Unsafe unsafe = Unsafe.getUnsafe();
        f767U = unsafe;
        PARKBLOCKER = unsafe.objectFieldOffset(Thread.class, "parkBlocker");
        SECONDARY = unsafe.objectFieldOffset(Thread.class, "threadLocalRandomSecondarySeed");
        TID = unsafe.objectFieldOffset(Thread.class, "tid");
    }
}
