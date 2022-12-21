package jdk.internal.misc;

import android.net.wifi.WifiConfiguration;
import java.lang.Thread;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.concurrent.atomic.AtomicInteger;

public final class InnocuousThread extends Thread {
    private static final AccessControlContext ACC;
    private static final long CONTEXTCLASSLOADER;
    private static final long INHERITABLE_THREAD_LOCALS;
    private static final long INHERITEDACCESSCONTROLCONTEXT;
    /* access modifiers changed from: private */
    public static final ThreadGroup INNOCUOUSTHREADGROUP;
    private static final long THREAD_LOCALS;
    private static final Unsafe UNSAFE;
    private static final AtomicInteger threadNumber = new AtomicInteger(1);
    private volatile boolean hasRun;

    public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
    }

    static {
        try {
            ACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain((CodeSource) null, (PermissionCollection) null)});
            Unsafe unsafe = Unsafe.getUnsafe();
            UNSAFE = unsafe;
            Class<Thread> cls = Thread.class;
            THREAD_LOCALS = unsafe.objectFieldOffset(cls, "threadLocals");
            INHERITABLE_THREAD_LOCALS = unsafe.objectFieldOffset(cls, "inheritableThreadLocals");
            INHERITEDACCESSCONTROLCONTEXT = unsafe.objectFieldOffset(cls, "inheritedAccessControlContext");
            CONTEXTCLASSLOADER = unsafe.objectFieldOffset(cls, "contextClassLoader");
            long objectFieldOffset = unsafe.objectFieldOffset(cls, WifiConfiguration.GroupCipher.varName);
            long objectFieldOffset2 = unsafe.objectFieldOffset(ThreadGroup.class, "parent");
            final ThreadGroup threadGroup = (ThreadGroup) unsafe.getObject(Thread.currentThread(), objectFieldOffset);
            while (true) {
                if (threadGroup == null) {
                    break;
                }
                ThreadGroup threadGroup2 = (ThreadGroup) UNSAFE.getObject(threadGroup, objectFieldOffset2);
                if (threadGroup2 == null) {
                    break;
                }
                threadGroup = threadGroup2;
            }
            INNOCUOUSTHREADGROUP = (ThreadGroup) AccessController.doPrivileged(new PrivilegedAction<ThreadGroup>() {
                public ThreadGroup run() {
                    return new ThreadGroup(ThreadGroup.this, "InnocuousThreadGroup");
                }
            });
        } catch (Exception e) {
            throw new Error((Throwable) e);
        }
    }

    private static String newName() {
        return "InnocuousThread-" + threadNumber.getAndIncrement();
    }

    public static Thread newThread(Runnable runnable) {
        return newThread(newName(), runnable);
    }

    public static Thread newThread(final String str, final Runnable runnable) {
        return (Thread) AccessController.doPrivileged(new PrivilegedAction<Thread>() {
            public Thread run() {
                return new InnocuousThread(InnocuousThread.INNOCUOUSTHREADGROUP, Runnable.this, str, ClassLoader.getSystemClassLoader());
            }
        });
    }

    public static Thread newSystemThread(Runnable runnable) {
        return newSystemThread(newName(), runnable);
    }

    public static Thread newSystemThread(final String str, final Runnable runnable) {
        return (Thread) AccessController.doPrivileged(new PrivilegedAction<Thread>() {
            public Thread run() {
                return new InnocuousThread(InnocuousThread.INNOCUOUSTHREADGROUP, Runnable.this, str, (ClassLoader) null);
            }
        });
    }

    private InnocuousThread(ThreadGroup threadGroup, Runnable runnable, String str, ClassLoader classLoader) {
        super(threadGroup, runnable, str, 0, false);
        Unsafe unsafe = UNSAFE;
        unsafe.putObjectRelease(this, INHERITEDACCESSCONTROLCONTEXT, ACC);
        unsafe.putObjectRelease(this, CONTEXTCLASSLOADER, classLoader);
    }

    public void setContextClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            super.setContextClassLoader((ClassLoader) null);
            return;
        }
        throw new SecurityException("setContextClassLoader");
    }

    public final void eraseThreadLocals() {
        Unsafe unsafe = UNSAFE;
        unsafe.putObject(this, THREAD_LOCALS, (Object) null);
        unsafe.putObject(this, INHERITABLE_THREAD_LOCALS, (Object) null);
    }

    public void run() {
        if (Thread.currentThread() == this && !this.hasRun) {
            this.hasRun = true;
            super.run();
        }
    }
}
