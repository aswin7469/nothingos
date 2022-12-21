package java.lang;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import dalvik.system.VMStack;
import java.lang.ThreadLocal;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import libcore.util.EmptyArray;
import sun.nio.p033ch.Interruptible;
import sun.reflect.CallerSensitive;

public class Thread implements Runnable {
    private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
    public static final int MAX_PRIORITY = 10;
    public static final int MIN_PRIORITY = 1;
    public static final int NORM_PRIORITY = 5;
    private static final RuntimePermission SUBCLASS_IMPLEMENTATION_PERMISSION = new RuntimePermission("enableContextClassLoaderOverride");
    private static volatile UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private static int threadInitNumber;
    private static long threadSeqNumber;
    private static volatile UncaughtExceptionHandler uncaughtExceptionPreHandler;
    private volatile Interruptible blocker;
    private final Object blockerLock;
    private ClassLoader contextClassLoader;
    private boolean daemon;
    private long eetop;
    private ThreadGroup group;
    ThreadLocal.ThreadLocalMap inheritableThreadLocals;
    private AccessControlContext inheritedAccessControlContext;
    private final Object lock;
    private volatile String name;
    private volatile long nativePeer;
    volatile Object parkBlocker;
    private int priority;
    private boolean single_step;
    private final long stackSize;
    boolean started;
    private boolean stillborn;
    private boolean systemDaemon;
    private Runnable target;
    int threadLocalRandomProbe;
    int threadLocalRandomSecondarySeed;
    long threadLocalRandomSeed;
    ThreadLocal.ThreadLocalMap threadLocals;
    private final long tid;
    private volatile UncaughtExceptionHandler uncaughtExceptionHandler;
    private boolean unparkedBeforeStart;

    public enum State {
        NEW,
        RUNNABLE,
        BLOCKED,
        WAITING,
        TIMED_WAITING,
        TERMINATED
    }

    @FunctionalInterface
    public interface UncaughtExceptionHandler {
        void uncaughtException(Thread thread, Throwable th);
    }

    public static native Thread currentThread();

    public static native boolean holdsLock(Object obj);

    private native void interrupt0();

    public static native boolean interrupted();

    private static native void nativeCreate(Thread thread, long j, boolean z);

    private native int nativeGetStatus(boolean z);

    public static void onSpinWait() {
    }

    private native void setNativeName(String str);

    private native void setPriority0(int i);

    private static native void sleep(Object obj, long j, int i) throws InterruptedException;

    public static native void yield();

    public final void checkAccess() {
    }

    public native boolean isInterrupted();

    private static synchronized int nextThreadNum() {
        int i;
        synchronized (Thread.class) {
            i = threadInitNumber;
            threadInitNumber = i + 1;
        }
        return i;
    }

    private static synchronized long nextThreadID() {
        long j;
        synchronized (Thread.class) {
            j = threadSeqNumber + 1;
            threadSeqNumber = j;
        }
        return j;
    }

    public void blockedOn(Interruptible interruptible) {
        synchronized (this.blockerLock) {
            this.blocker = interruptible;
        }
    }

    public static void sleep(long j) throws InterruptedException {
        sleep(j, 0);
    }

    public static void sleep(long j, int i) throws InterruptedException {
        int i2 = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i2 < 0) {
            throw new IllegalArgumentException("millis < 0: " + j);
        } else if (i < 0) {
            throw new IllegalArgumentException("nanos < 0: " + i);
        } else if (i > 999999) {
            throw new IllegalArgumentException("nanos > 999999: " + i);
        } else if (i2 != 0 || i != 0) {
            long j2 = j >= 9223372036853L ? Long.MAX_VALUE : (j * 1000000) + ((long) i);
            long nanoTime = System.nanoTime();
            Object obj = currentThread().lock;
            synchronized (obj) {
                for (long j3 = 0; j3 < j2; j3 = System.nanoTime() - nanoTime) {
                    long j4 = j2 - j3;
                    sleep(obj, j4 / 1000000, (int) (j4 % 1000000));
                }
            }
        } else if (interrupted()) {
            throw new InterruptedException();
        }
    }

    private Thread(ThreadGroup threadGroup, Runnable runnable, String str, long j, AccessControlContext accessControlContext, boolean z) {
        this.lock = new Object();
        this.daemon = false;
        this.stillborn = false;
        this.threadLocals = null;
        this.inheritableThreadLocals = null;
        this.systemDaemon = false;
        this.started = false;
        this.blockerLock = new Object();
        if (str != null) {
            this.name = str;
            Thread currentThread = currentThread();
            threadGroup = threadGroup == null ? currentThread.getThreadGroup() : threadGroup;
            threadGroup.addUnstarted();
            this.group = threadGroup;
            this.daemon = currentThread.isDaemon();
            this.priority = currentThread.getPriority();
            this.target = runnable;
            init2(currentThread, z);
            this.stackSize = j;
            this.tid = nextThreadID();
            return;
        }
        throw new NullPointerException("name cannot be null");
    }

    /* access modifiers changed from: protected */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Thread() {
        this((ThreadGroup) null, (Runnable) null, "Thread-" + nextThreadNum(), 0);
    }

    public Thread(Runnable runnable) {
        this((ThreadGroup) null, runnable, "Thread-" + nextThreadNum(), 0);
    }

    Thread(Runnable runnable, AccessControlContext accessControlContext) {
        this((ThreadGroup) null, runnable, "Thread-" + nextThreadNum(), 0, accessControlContext, false);
    }

    public Thread(ThreadGroup threadGroup, Runnable runnable) {
        this(threadGroup, runnable, "Thread-" + nextThreadNum(), 0);
    }

    public Thread(String str) {
        this((ThreadGroup) null, (Runnable) null, str, 0);
    }

    public Thread(ThreadGroup threadGroup, String str) {
        this(threadGroup, (Runnable) null, str, 0);
    }

    Thread(ThreadGroup threadGroup, String str, int i, boolean z) {
        this.lock = new Object();
        this.daemon = false;
        this.stillborn = false;
        this.threadLocals = null;
        this.inheritableThreadLocals = null;
        this.systemDaemon = false;
        this.started = false;
        this.blockerLock = new Object();
        this.group = threadGroup;
        threadGroup.addUnstarted();
        if (str == null) {
            str = "Thread-" + nextThreadNum();
        }
        this.name = str;
        this.priority = i;
        this.daemon = z;
        init2(currentThread(), true);
        this.stackSize = 0;
        this.tid = nextThreadID();
    }

    private void init2(Thread thread, boolean z) {
        ThreadLocal.ThreadLocalMap threadLocalMap;
        this.contextClassLoader = thread.getContextClassLoader();
        this.inheritedAccessControlContext = AccessController.getContext();
        if (z && (threadLocalMap = thread.inheritableThreadLocals) != null) {
            this.inheritableThreadLocals = ThreadLocal.createInheritedMap(threadLocalMap);
        }
    }

    public Thread(Runnable runnable, String str) {
        this((ThreadGroup) null, runnable, str, 0);
    }

    public Thread(ThreadGroup threadGroup, Runnable runnable, String str) {
        this(threadGroup, runnable, str, 0);
    }

    public Thread(ThreadGroup threadGroup, Runnable runnable, String str, long j) {
        this(threadGroup, runnable, str, j, (AccessControlContext) null, true);
    }

    public Thread(ThreadGroup threadGroup, Runnable runnable, String str, long j, boolean z) {
        this(threadGroup, runnable, str, j, (AccessControlContext) null, z);
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0023 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void start() {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = r3.started     // Catch:{ all -> 0x002a }
            if (r0 != 0) goto L_0x0024
            java.lang.ThreadGroup r0 = r3.group     // Catch:{ all -> 0x002a }
            r0.add((java.lang.Thread) r3)     // Catch:{ all -> 0x002a }
            r0 = 0
            r3.started = r0     // Catch:{ all -> 0x002a }
            long r0 = r3.stackSize     // Catch:{ all -> 0x0019 }
            boolean r2 = r3.daemon     // Catch:{ all -> 0x0019 }
            nativeCreate(r3, r0, r2)     // Catch:{ all -> 0x0019 }
            r0 = 1
            r3.started = r0     // Catch:{ all -> 0x0019 }
            monitor-exit(r3)
            return
        L_0x0019:
            r0 = move-exception
            boolean r1 = r3.started     // Catch:{ all -> 0x0023 }
            if (r1 != 0) goto L_0x0023
            java.lang.ThreadGroup r1 = r3.group     // Catch:{ all -> 0x0023 }
            r1.threadStartFailed(r3)     // Catch:{ all -> 0x0023 }
        L_0x0023:
            throw r0     // Catch:{ all -> 0x002a }
        L_0x0024:
            java.lang.IllegalThreadStateException r0 = new java.lang.IllegalThreadStateException     // Catch:{ all -> 0x002a }
            r0.<init>()     // Catch:{ all -> 0x002a }
            throw r0     // Catch:{ all -> 0x002a }
        L_0x002a:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.Thread.start():void");
    }

    public void run() {
        Runnable runnable = this.target;
        if (runnable != null) {
            runnable.run();
        }
    }

    private void exit() {
        ThreadGroup threadGroup = this.group;
        if (threadGroup != null) {
            threadGroup.threadTerminated(this);
            this.group = null;
        }
        this.target = null;
        this.threadLocals = null;
        this.inheritableThreadLocals = null;
        this.inheritedAccessControlContext = null;
        this.blocker = null;
        this.uncaughtExceptionHandler = null;
    }

    @Deprecated(since = "1.2")
    public final void stop() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final synchronized void stop(Throwable th) {
        throw new UnsupportedOperationException();
    }

    public void interrupt() {
        if (this != currentThread()) {
            checkAccess();
            synchronized (this.blockerLock) {
                Interruptible interruptible = this.blocker;
                if (interruptible != null) {
                    interrupt0();
                    interruptible.interrupt(this);
                    return;
                }
            }
        }
        interrupt0();
    }

    @Deprecated
    public void destroy() {
        throw new UnsupportedOperationException();
    }

    public final boolean isAlive() {
        return this.nativePeer != 0;
    }

    @Deprecated(since = "1.2")
    public final void suspend() {
        throw new UnsupportedOperationException();
    }

    @Deprecated(since = "1.2")
    public final void resume() {
        throw new UnsupportedOperationException();
    }

    public final void setPriority(int i) {
        checkAccess();
        if (i > 10 || i < 1) {
            throw new IllegalArgumentException("Priority out of range: " + i);
        }
        ThreadGroup threadGroup = getThreadGroup();
        if (threadGroup != null) {
            if (i > threadGroup.getMaxPriority()) {
                i = threadGroup.getMaxPriority();
            }
            synchronized (this) {
                this.priority = i;
                if (isAlive()) {
                    setPriority0(i);
                }
            }
        }
    }

    public final int getPriority() {
        return this.priority;
    }

    public final synchronized void setName(String str) {
        checkAccess();
        if (str != null) {
            this.name = str;
            if (isAlive()) {
                setNativeName(str);
            }
        } else {
            throw new NullPointerException("name cannot be null");
        }
    }

    public final String getName() {
        return this.name;
    }

    public final ThreadGroup getThreadGroup() {
        if (getState() == State.TERMINATED) {
            return null;
        }
        return this.group;
    }

    public static int activeCount() {
        return currentThread().getThreadGroup().activeCount();
    }

    public static int enumerate(Thread[] threadArr) {
        return currentThread().getThreadGroup().enumerate(threadArr);
    }

    @Deprecated(forRemoval = true, since = "1.2")
    public int countStackFrames() {
        return getStackTrace().length;
    }

    public final void join(long j) throws InterruptedException {
        synchronized (this.lock) {
            long currentTimeMillis = System.currentTimeMillis();
            int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
            if (i >= 0) {
                if (i != 0) {
                    long j2 = 0;
                    while (true) {
                        if (!isAlive()) {
                            break;
                        }
                        long j3 = j - j2;
                        if (j3 <= 0) {
                            break;
                        }
                        this.lock.wait(j3);
                        j2 = System.currentTimeMillis() - currentTimeMillis;
                    }
                } else {
                    while (isAlive()) {
                        this.lock.wait(0);
                    }
                }
            } else {
                throw new IllegalArgumentException("timeout value is negative");
            }
        }
    }

    public final void join(long j, int i) throws InterruptedException {
        synchronized (this.lock) {
            int i2 = (j > 0 ? 1 : (j == 0 ? 0 : -1));
            if (i2 < 0) {
                throw new IllegalArgumentException("timeout value is negative");
            } else if (i < 0 || i > 999999) {
                throw new IllegalArgumentException("nanosecond timeout value out of range");
            } else {
                if (i >= 500000 || (i != 0 && i2 == 0)) {
                    j++;
                }
                join(j);
            }
        }
    }

    public final void join() throws InterruptedException {
        join(0);
    }

    public static void dumpStack() {
        new Exception("Stack trace").printStackTrace();
    }

    public final void setDaemon(boolean z) {
        checkAccess();
        if (!isAlive()) {
            this.daemon = z;
            return;
        }
        throw new IllegalThreadStateException();
    }

    public final boolean isDaemon() {
        return this.daemon;
    }

    public String toString() {
        ThreadGroup threadGroup = getThreadGroup();
        if (threadGroup != null) {
            return "Thread[" + getName() + NavigationBarInflaterView.BUTTON_SEPARATOR + getPriority() + NavigationBarInflaterView.BUTTON_SEPARATOR + threadGroup.getName() + NavigationBarInflaterView.SIZE_MOD_END;
        }
        return "Thread[" + getName() + NavigationBarInflaterView.BUTTON_SEPARATOR + getPriority() + ",]";
    }

    @CallerSensitive
    public ClassLoader getContextClassLoader() {
        return this.contextClassLoader;
    }

    public void setContextClassLoader(ClassLoader classLoader) {
        this.contextClassLoader = classLoader;
    }

    public StackTraceElement[] getStackTrace() {
        StackTraceElement[] threadStackTrace = VMStack.getThreadStackTrace(this);
        return threadStackTrace != null ? threadStackTrace : EmptyArray.STACK_TRACE_ELEMENT;
    }

    public static Map<Thread, StackTraceElement[]> getAllStackTraces() {
        int activeCount = ThreadGroup.systemThreadGroup.activeCount();
        Thread[] threadArr = new Thread[(activeCount + (activeCount / 2))];
        int enumerate = ThreadGroup.systemThreadGroup.enumerate(threadArr);
        HashMap hashMap = new HashMap();
        for (int i = 0; i < enumerate; i++) {
            hashMap.put(threadArr[i], threadArr[i].getStackTrace());
        }
        return hashMap;
    }

    private static class Caches {
        static final ConcurrentMap<WeakClassKey, Boolean> subclassAudits = new ConcurrentHashMap();
        static final ReferenceQueue<Class<?>> subclassAuditsQueue = new ReferenceQueue<>();

        private Caches() {
        }
    }

    private static boolean isCCLOverridden(Class<?> cls) {
        if (cls == Thread.class) {
            return false;
        }
        processQueue(Caches.subclassAuditsQueue, Caches.subclassAudits);
        WeakClassKey weakClassKey = new WeakClassKey(cls, Caches.subclassAuditsQueue);
        Boolean bool = Caches.subclassAudits.get(weakClassKey);
        if (bool == null) {
            bool = Boolean.valueOf(auditSubclass(cls));
            Caches.subclassAudits.putIfAbsent(weakClassKey, bool);
        }
        return bool.booleanValue();
    }

    private static boolean auditSubclass(final Class<?> cls) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                Class<? super Thread> cls = Class.this;
                while (cls != Thread.class) {
                    try {
                        cls.getDeclaredMethod("getContextClassLoader", new Class[0]);
                        return Boolean.TRUE;
                    } catch (NoSuchMethodException unused) {
                        try {
                            cls.getDeclaredMethod("setContextClassLoader", ClassLoader.class);
                            return Boolean.TRUE;
                        } catch (NoSuchMethodException unused2) {
                            cls = cls.getSuperclass();
                        }
                    }
                }
                return Boolean.FALSE;
            }
        })).booleanValue();
    }

    public long getId() {
        return this.tid;
    }

    public State getState() {
        return State.values()[nativeGetStatus(this.started)];
    }

    public static void setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler2) {
        defaultUncaughtExceptionHandler = uncaughtExceptionHandler2;
    }

    public static UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
        return defaultUncaughtExceptionHandler;
    }

    public static void setUncaughtExceptionPreHandler(UncaughtExceptionHandler uncaughtExceptionHandler2) {
        uncaughtExceptionPreHandler = uncaughtExceptionHandler2;
    }

    public static UncaughtExceptionHandler getUncaughtExceptionPreHandler() {
        return uncaughtExceptionPreHandler;
    }

    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return this.uncaughtExceptionHandler != null ? this.uncaughtExceptionHandler : this.group;
    }

    public void setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler2) {
        checkAccess();
        this.uncaughtExceptionHandler = uncaughtExceptionHandler2;
    }

    public final void dispatchUncaughtException(Throwable th) {
        UncaughtExceptionHandler uncaughtExceptionPreHandler2 = getUncaughtExceptionPreHandler();
        if (uncaughtExceptionPreHandler2 != null) {
            try {
                uncaughtExceptionPreHandler2.uncaughtException(this, th);
            } catch (Error | RuntimeException unused) {
            }
        }
        getUncaughtExceptionHandler().uncaughtException(this, th);
    }

    /* access modifiers changed from: package-private */
    public final void setSystemDaemon(boolean z) {
        checkAccess();
        if (isAlive() || !isDaemon()) {
            throw new IllegalThreadStateException();
        }
        this.systemDaemon = z;
    }

    static void processQueue(ReferenceQueue<Class<?>> referenceQueue, ConcurrentMap<? extends WeakReference<Class<?>>, ?> concurrentMap) {
        while (true) {
            Reference<? extends Class<?>> poll = referenceQueue.poll();
            if (poll != null) {
                concurrentMap.remove(poll);
            } else {
                return;
            }
        }
    }

    static class WeakClassKey extends WeakReference<Class<?>> {
        private final int hash;

        WeakClassKey(Class<?> cls, ReferenceQueue<Class<?>> referenceQueue) {
            super(cls, referenceQueue);
            this.hash = System.identityHashCode(cls);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof WeakClassKey)) {
                return false;
            }
            Object obj2 = get();
            if (obj2 == null || obj2 != ((WeakClassKey) obj).get()) {
                return false;
            }
            return true;
        }
    }
}
