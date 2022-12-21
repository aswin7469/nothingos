package java.lang;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ThreadLocal<T> {
    private static final int HASH_INCREMENT = 1640531527;
    private static AtomicInteger nextHashCode = new AtomicInteger();
    /* access modifiers changed from: private */
    public final int threadLocalHashCode = nextHashCode();

    /* access modifiers changed from: protected */
    public T initialValue() {
        return null;
    }

    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal(supplier);
    }

    public T get() {
        ThreadLocalMap.Entry r0;
        ThreadLocalMap map = getMap(Thread.currentThread());
        if (map == null || (r0 = map.getEntry(this)) == null) {
            return setInitialValue();
        }
        return r0.value;
    }

    private T setInitialValue() {
        T initialValue = initialValue();
        Thread currentThread = Thread.currentThread();
        ThreadLocalMap map = getMap(currentThread);
        if (map != null) {
            map.set(this, initialValue);
        } else {
            createMap(currentThread, initialValue);
        }
        return initialValue;
    }

    public void set(T t) {
        Thread currentThread = Thread.currentThread();
        ThreadLocalMap map = getMap(currentThread);
        if (map != null) {
            map.set(this, t);
        } else {
            createMap(currentThread, t);
        }
    }

    public void remove() {
        ThreadLocalMap map = getMap(Thread.currentThread());
        if (map != null) {
            map.remove(this);
        }
    }

    /* access modifiers changed from: package-private */
    public ThreadLocalMap getMap(Thread thread) {
        return thread.threadLocals;
    }

    /* access modifiers changed from: package-private */
    public void createMap(Thread thread, T t) {
        thread.threadLocals = new ThreadLocalMap((ThreadLocal<?>) this, (Object) t);
    }

    static ThreadLocalMap createInheritedMap(ThreadLocalMap threadLocalMap) {
        return new ThreadLocalMap(threadLocalMap);
    }

    /* access modifiers changed from: package-private */
    public T childValue(T t) {
        throw new UnsupportedOperationException();
    }

    static final class SuppliedThreadLocal<T> extends ThreadLocal<T> {
        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier2) {
            this.supplier = (Supplier) Objects.requireNonNull(supplier2);
        }

        /* access modifiers changed from: protected */
        public T initialValue() {
            return this.supplier.get();
        }
    }

    static class ThreadLocalMap {
        private static final int INITIAL_CAPACITY = 16;
        private int size;
        private Entry[] table;
        private int threshold;

        private static int nextIndex(int i, int i2) {
            int i3 = i + 1;
            if (i3 < i2) {
                return i3;
            }
            return 0;
        }

        private static int prevIndex(int i, int i2) {
            int i3 = i - 1;
            return i3 >= 0 ? i3 : i2 - 1;
        }

        static class Entry extends WeakReference<ThreadLocal<?>> {
            Object value;

            Entry(ThreadLocal<?> threadLocal, Object obj) {
                super(threadLocal);
                this.value = obj;
            }
        }

        private void setThreshold(int i) {
            this.threshold = (i * 2) / 3;
        }

        ThreadLocalMap(ThreadLocal<?> threadLocal, Object obj) {
            this.size = 0;
            this.table = new Entry[16];
            this.table[threadLocal.threadLocalHashCode & 15] = new Entry(threadLocal, obj);
            this.size = 1;
            setThreshold(16);
        }

        private ThreadLocalMap(ThreadLocalMap threadLocalMap) {
            ThreadLocal threadLocal;
            Entry[] entryArr;
            this.size = 0;
            setThreshold(r1);
            this.table = new Entry[r1];
            for (Entry entry : threadLocalMap.table) {
                if (!(entry == null || (threadLocal = (ThreadLocal) entry.get()) == null)) {
                    Entry entry2 = new Entry(threadLocal, threadLocal.childValue(entry.value));
                    int r2 = threadLocal.threadLocalHashCode & (r1 - 1);
                    while (true) {
                        entryArr = this.table;
                        if (entryArr[r2] == null) {
                            break;
                        }
                        r2 = nextIndex(r2, r1);
                    }
                    entryArr[r2] = entry2;
                    this.size++;
                }
            }
        }

        /* access modifiers changed from: private */
        public Entry getEntry(ThreadLocal<?> threadLocal) {
            int r0 = threadLocal.threadLocalHashCode;
            Entry[] entryArr = this.table;
            int length = r0 & (entryArr.length - 1);
            Entry entry = entryArr[length];
            if (entry == null || !entry.refersTo(threadLocal)) {
                return getEntryAfterMiss(threadLocal, length, entry);
            }
            return entry;
        }

        private Entry getEntryAfterMiss(ThreadLocal<?> threadLocal, int i, Entry entry) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            while (entry != null) {
                if (entry.refersTo(threadLocal)) {
                    return entry;
                }
                if (entry.refersTo(null)) {
                    expungeStaleEntry(i);
                } else {
                    i = nextIndex(i, length);
                }
                entry = entryArr[i];
            }
            return null;
        }

        /* access modifiers changed from: private */
        public void set(ThreadLocal<?> threadLocal, Object obj) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            int r2 = threadLocal.threadLocalHashCode & (length - 1);
            Entry entry = entryArr[r2];
            while (entry != null) {
                if (entry.refersTo(threadLocal)) {
                    entry.value = obj;
                    return;
                } else if (entry.refersTo(null)) {
                    replaceStaleEntry(threadLocal, obj, r2);
                    return;
                } else {
                    r2 = nextIndex(r2, length);
                    entry = entryArr[r2];
                }
            }
            entryArr[r2] = new Entry(threadLocal, obj);
            int i = this.size + 1;
            this.size = i;
            if (!cleanSomeSlots(r2, i) && i >= this.threshold) {
                rehash();
            }
        }

        /* access modifiers changed from: private */
        public void remove(ThreadLocal<?> threadLocal) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            int r2 = threadLocal.threadLocalHashCode & (length - 1);
            Entry entry = entryArr[r2];
            while (entry != null) {
                if (entry.refersTo(threadLocal)) {
                    entry.clear();
                    expungeStaleEntry(r2);
                    return;
                }
                r2 = nextIndex(r2, length);
                entry = entryArr[r2];
            }
        }

        private void replaceStaleEntry(ThreadLocal<?> threadLocal, Object obj, int i) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            int prevIndex = prevIndex(i, length);
            int i2 = i;
            while (true) {
                Entry entry = entryArr[prevIndex];
                if (entry == null) {
                    break;
                }
                if (entry.refersTo(null)) {
                    i2 = prevIndex;
                }
                prevIndex = prevIndex(prevIndex, length);
            }
            int nextIndex = nextIndex(i, length);
            while (true) {
                Entry entry2 = entryArr[nextIndex];
                if (entry2 == null) {
                    entryArr[i].value = null;
                    entryArr[i] = new Entry(threadLocal, obj);
                    if (i2 != i) {
                        cleanSomeSlots(expungeStaleEntry(i2), length);
                        return;
                    }
                    return;
                } else if (entry2.refersTo(threadLocal)) {
                    entry2.value = obj;
                    entryArr[nextIndex] = entryArr[i];
                    entryArr[i] = entry2;
                    if (i2 != i) {
                        nextIndex = i2;
                    }
                    cleanSomeSlots(expungeStaleEntry(nextIndex), length);
                    return;
                } else {
                    if (entry2.refersTo(null) && i2 == i) {
                        i2 = nextIndex;
                    }
                    nextIndex = nextIndex(nextIndex, length);
                }
            }
        }

        private int expungeStaleEntry(int i) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            entryArr[i].value = null;
            entryArr[i] = null;
            this.size--;
            int nextIndex = nextIndex(i, length);
            while (true) {
                Entry entry = entryArr[nextIndex];
                if (entry == null) {
                    return nextIndex;
                }
                ThreadLocal threadLocal = (ThreadLocal) entry.get();
                if (threadLocal == null) {
                    entry.value = null;
                    entryArr[nextIndex] = null;
                    this.size--;
                } else {
                    int r4 = threadLocal.threadLocalHashCode & (length - 1);
                    if (r4 != nextIndex) {
                        entryArr[nextIndex] = null;
                        while (entryArr[r4] != null) {
                            r4 = nextIndex(r4, length);
                        }
                        entryArr[r4] = entry;
                    }
                }
                nextIndex = nextIndex(nextIndex, length);
            }
        }

        private boolean cleanSomeSlots(int i, int i2) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            boolean z = false;
            do {
                i = nextIndex(i, length);
                Entry entry = entryArr[i];
                if (entry != null && entry.refersTo(null)) {
                    i = expungeStaleEntry(i);
                    i2 = length;
                    z = true;
                }
                i2 >>>= 1;
            } while (i2 != 0);
            return z;
        }

        private void rehash() {
            expungeStaleEntries();
            int i = this.size;
            int i2 = this.threshold;
            if (i >= i2 - (i2 / 4)) {
                resize();
            }
        }

        private void resize() {
            int i = r1 * 2;
            Entry[] entryArr = new Entry[i];
            int i2 = 0;
            for (Entry entry : this.table) {
                if (entry != null) {
                    ThreadLocal threadLocal = (ThreadLocal) entry.get();
                    if (threadLocal == null) {
                        entry.value = null;
                    } else {
                        int r7 = threadLocal.threadLocalHashCode & (i - 1);
                        while (entryArr[r7] != null) {
                            r7 = nextIndex(r7, i);
                        }
                        entryArr[r7] = entry;
                        i2++;
                    }
                }
            }
            setThreshold(i);
            this.size = i2;
            this.table = entryArr;
        }

        private void expungeStaleEntries() {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            for (int i = 0; i < length; i++) {
                Entry entry = entryArr[i];
                if (entry != null && entry.refersTo(null)) {
                    expungeStaleEntry(i);
                }
            }
        }
    }
}
