package jdk.internal.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class Cleaner extends PhantomReference<Object> {
    private static final ReferenceQueue<Object> dummyQueue = new ReferenceQueue<>();
    private static Cleaner first = null;
    private Cleaner next = null;
    private Cleaner prev = null;
    private final Runnable thunk;

    private static synchronized Cleaner add(Cleaner cleaner) {
        synchronized (Cleaner.class) {
            Cleaner cleaner2 = first;
            if (cleaner2 != null) {
                cleaner.next = cleaner2;
                cleaner2.prev = cleaner;
            }
            first = cleaner;
        }
        return cleaner;
    }

    private static synchronized boolean remove(Cleaner cleaner) {
        synchronized (Cleaner.class) {
            Cleaner cleaner2 = cleaner.next;
            if (cleaner2 == cleaner) {
                return false;
            }
            if (first == cleaner) {
                if (cleaner2 != null) {
                    first = cleaner2;
                } else {
                    first = cleaner.prev;
                }
            }
            if (cleaner2 != null) {
                cleaner2.prev = cleaner.prev;
            }
            Cleaner cleaner3 = cleaner.prev;
            if (cleaner3 != null) {
                cleaner3.next = cleaner2;
            }
            cleaner.next = cleaner;
            cleaner.prev = cleaner;
            return true;
        }
    }

    private Cleaner(Object obj, Runnable runnable) {
        super(obj, dummyQueue);
        this.thunk = runnable;
    }

    public static Cleaner create(Object obj, Runnable runnable) {
        if (runnable == null) {
            return null;
        }
        return add(new Cleaner(obj, runnable));
    }

    public void clean() {
        if (remove(this)) {
            try {
                this.thunk.run();
            } catch (Throwable th) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Void run() {
                        if (System.err != null) {
                            new Error("Cleaner terminated abnormally", th).printStackTrace();
                        }
                        System.exit(1);
                        return null;
                    }
                });
            }
        }
    }
}
