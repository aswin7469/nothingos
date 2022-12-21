package jdk.internal.ref;

import dalvik.system.ZygoteHooks;
import java.lang.ref.Cleaner;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ThreadFactory;
import jdk.internal.misc.InnocuousThread;

public final class CleanerFactory {
    private static final Cleaner commonCleaner = Cleaner.create(new ThreadFactory() {
        public Thread newThread(final Runnable runnable) {
            if (!ZygoteHooks.inZygote()) {
                return (Thread) AccessController.doPrivileged(new PrivilegedAction<Thread>() {
                    public Thread run() {
                        Thread newSystemThread = InnocuousThread.newSystemThread("Common-Cleaner", runnable);
                        newSystemThread.setPriority(8);
                        return newSystemThread;
                    }
                });
            }
            throw new AssertionError((Object) "Erroneously trying to create Cleaner in zygote");
        }
    });

    public static Cleaner cleaner() {
        return commonCleaner;
    }
}
