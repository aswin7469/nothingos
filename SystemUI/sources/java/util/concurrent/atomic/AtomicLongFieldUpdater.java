package java.util.concurrent.atomic;

import java.util.Objects;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import jdk.internal.misc.Unsafe;
import jdk.internal.reflect.CallerSensitive;
import jdk.internal.reflect.Reflection;

public abstract class AtomicLongFieldUpdater<T> {
    public abstract boolean compareAndSet(T t, long j, long j2);

    public abstract long get(T t);

    public abstract void lazySet(T t, long j);

    public abstract void set(T t, long j);

    public abstract boolean weakCompareAndSet(T t, long j, long j2);

    @CallerSensitive
    public static <U> AtomicLongFieldUpdater<U> newUpdater(Class<U> cls, String str) {
        Class<?> callerClass = Reflection.getCallerClass();
        if (AtomicLong.VM_SUPPORTS_LONG_CAS) {
            return new CASUpdater(cls, str, callerClass);
        }
        return new LockedUpdater(cls, str, callerClass);
    }

    protected AtomicLongFieldUpdater() {
    }

    public long getAndSet(T t, long j) {
        long j2;
        do {
            j2 = get(t);
        } while (!compareAndSet(t, j2, j));
        return j2;
    }

    public long getAndIncrement(T t) {
        long j;
        do {
            j = get(t);
        } while (!compareAndSet(t, j, j + 1));
        return j;
    }

    public long getAndDecrement(T t) {
        long j;
        do {
            j = get(t);
        } while (!compareAndSet(t, j, j - 1));
        return j;
    }

    public long getAndAdd(T t, long j) {
        long j2;
        do {
            j2 = get(t);
        } while (!compareAndSet(t, j2, j2 + j));
        return j2;
    }

    public long incrementAndGet(T t) {
        long j;
        long j2;
        do {
            j = get(t);
            j2 = j + 1;
        } while (!compareAndSet(t, j, j2));
        return j2;
    }

    public long decrementAndGet(T t) {
        long j;
        long j2;
        do {
            j = get(t);
            j2 = j - 1;
        } while (!compareAndSet(t, j, j2));
        return j2;
    }

    public long addAndGet(T t, long j) {
        long j2;
        long j3;
        do {
            j2 = get(t);
            j3 = j2 + j;
        } while (!compareAndSet(t, j2, j3));
        return j3;
    }

    public final long getAndUpdate(T t, LongUnaryOperator longUnaryOperator) {
        long j;
        do {
            j = get(t);
        } while (!compareAndSet(t, j, longUnaryOperator.applyAsLong(j)));
        return j;
    }

    public final long updateAndGet(T t, LongUnaryOperator longUnaryOperator) {
        long j;
        long applyAsLong;
        do {
            j = get(t);
            applyAsLong = longUnaryOperator.applyAsLong(j);
        } while (!compareAndSet(t, j, applyAsLong));
        return applyAsLong;
    }

    public final long getAndAccumulate(T t, long j, LongBinaryOperator longBinaryOperator) {
        long j2;
        do {
            j2 = get(t);
        } while (!compareAndSet(t, j2, longBinaryOperator.applyAsLong(j2, j)));
        return j2;
    }

    public final long accumulateAndGet(T t, long j, LongBinaryOperator longBinaryOperator) {
        long j2;
        long applyAsLong;
        do {
            j2 = get(t);
            applyAsLong = longBinaryOperator.applyAsLong(j2, j);
        } while (!compareAndSet(t, j2, applyAsLong));
        return applyAsLong;
    }

    private static final class CASUpdater<T> extends AtomicLongFieldUpdater<T> {

        /* renamed from: U */
        private static final Unsafe f763U = Unsafe.getUnsafe();
        private final Class<?> cclass;
        private final long offset;
        private final Class<T> tclass;

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x002d, code lost:
            if (r0 == false) goto L_0x0031;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        CASUpdater(java.lang.Class<T> r4, java.lang.String r5, java.lang.Class<?> r6) {
            /*
                r3 = this;
                r3.<init>()
                java.lang.reflect.Field r5 = r4.getDeclaredField(r5)     // Catch:{ Exception -> 0x004e }
                int r0 = r5.getModifiers()     // Catch:{ Exception -> 0x004e }
                r1 = 0
                sun.reflect.misc.ReflectUtil.ensureMemberAccess(r6, r4, r1, r0)     // Catch:{ Exception -> 0x004e }
                java.lang.Class r1 = r5.getType()
                java.lang.Class<java.lang.Long> r2 = java.lang.Long.TYPE
                if (r1 != r2) goto L_0x0046
                boolean r1 = java.lang.reflect.Modifier.isVolatile(r0)
                if (r1 == 0) goto L_0x003e
                boolean r0 = java.lang.reflect.Modifier.isProtected(r0)
                if (r0 == 0) goto L_0x0030
                boolean r0 = r4.isAssignableFrom(r6)
                if (r0 == 0) goto L_0x0030
                boolean r0 = isSamePackage(r4, r6)
                if (r0 != 0) goto L_0x0030
                goto L_0x0031
            L_0x0030:
                r6 = r4
            L_0x0031:
                r3.cclass = r6
                r3.tclass = r4
                jdk.internal.misc.Unsafe r4 = f763U
                long r4 = r4.objectFieldOffset(r5)
                r3.offset = r4
                return
            L_0x003e:
                java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
                java.lang.String r4 = "Must be volatile type"
                r3.<init>((java.lang.String) r4)
                throw r3
            L_0x0046:
                java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
                java.lang.String r4 = "Must be long type"
                r3.<init>((java.lang.String) r4)
                throw r3
            L_0x004e:
                r3 = move-exception
                java.lang.RuntimeException r4 = new java.lang.RuntimeException
                r4.<init>((java.lang.Throwable) r3)
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicLongFieldUpdater.CASUpdater.<init>(java.lang.Class, java.lang.String, java.lang.Class):void");
        }

        private final void accessCheck(T t) {
            if (!this.cclass.isInstance(t)) {
                throwAccessCheckException(t);
            }
        }

        private final void throwAccessCheckException(T t) {
            if (this.cclass == this.tclass) {
                throw new ClassCastException();
            }
            throw new RuntimeException((Throwable) new IllegalAccessException("Class " + this.cclass.getName() + " can not access a protected member of class " + this.tclass.getName() + " using an instance of " + t.getClass().getName()));
        }

        public final boolean compareAndSet(T t, long j, long j2) {
            accessCheck(t);
            return f763U.compareAndSetLong(t, this.offset, j, j2);
        }

        public final boolean weakCompareAndSet(T t, long j, long j2) {
            accessCheck(t);
            return f763U.compareAndSetLong(t, this.offset, j, j2);
        }

        public final void set(T t, long j) {
            accessCheck(t);
            f763U.putLongVolatile(t, this.offset, j);
        }

        public final void lazySet(T t, long j) {
            accessCheck(t);
            f763U.putLongRelease(t, this.offset, j);
        }

        public final long get(T t) {
            accessCheck(t);
            return f763U.getLongVolatile(t, this.offset);
        }

        public final long getAndSet(T t, long j) {
            accessCheck(t);
            return f763U.getAndSetLong(t, this.offset, j);
        }

        public final long getAndAdd(T t, long j) {
            accessCheck(t);
            return f763U.getAndAddLong(t, this.offset, j);
        }

        public final long getAndIncrement(T t) {
            return getAndAdd(t, 1);
        }

        public final long getAndDecrement(T t) {
            return getAndAdd(t, -1);
        }

        public final long incrementAndGet(T t) {
            return getAndAdd(t, 1) + 1;
        }

        public final long decrementAndGet(T t) {
            return getAndAdd(t, -1) - 1;
        }

        public final long addAndGet(T t, long j) {
            return getAndAdd(t, j) + j;
        }
    }

    private static final class LockedUpdater<T> extends AtomicLongFieldUpdater<T> {

        /* renamed from: U */
        private static final Unsafe f764U = Unsafe.getUnsafe();
        private final Class<?> cclass;
        private final long offset;
        private final Class<T> tclass;

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x002d, code lost:
            if (r0 == false) goto L_0x0031;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        LockedUpdater(java.lang.Class<T> r4, java.lang.String r5, java.lang.Class<?> r6) {
            /*
                r3 = this;
                r3.<init>()
                java.lang.reflect.Field r5 = r4.getDeclaredField(r5)     // Catch:{ Exception -> 0x004e }
                int r0 = r5.getModifiers()     // Catch:{ Exception -> 0x004e }
                r1 = 0
                sun.reflect.misc.ReflectUtil.ensureMemberAccess(r6, r4, r1, r0)     // Catch:{ Exception -> 0x004e }
                java.lang.Class r1 = r5.getType()
                java.lang.Class<java.lang.Long> r2 = java.lang.Long.TYPE
                if (r1 != r2) goto L_0x0046
                boolean r1 = java.lang.reflect.Modifier.isVolatile(r0)
                if (r1 == 0) goto L_0x003e
                boolean r0 = java.lang.reflect.Modifier.isProtected(r0)
                if (r0 == 0) goto L_0x0030
                boolean r0 = r4.isAssignableFrom(r6)
                if (r0 == 0) goto L_0x0030
                boolean r0 = isSamePackage(r4, r6)
                if (r0 != 0) goto L_0x0030
                goto L_0x0031
            L_0x0030:
                r6 = r4
            L_0x0031:
                r3.cclass = r6
                r3.tclass = r4
                jdk.internal.misc.Unsafe r4 = f764U
                long r4 = r4.objectFieldOffset(r5)
                r3.offset = r4
                return
            L_0x003e:
                java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
                java.lang.String r4 = "Must be volatile type"
                r3.<init>((java.lang.String) r4)
                throw r3
            L_0x0046:
                java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
                java.lang.String r4 = "Must be long type"
                r3.<init>((java.lang.String) r4)
                throw r3
            L_0x004e:
                r3 = move-exception
                java.lang.RuntimeException r4 = new java.lang.RuntimeException
                r4.<init>((java.lang.Throwable) r3)
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicLongFieldUpdater.LockedUpdater.<init>(java.lang.Class, java.lang.String, java.lang.Class):void");
        }

        private final void accessCheck(T t) {
            if (!this.cclass.isInstance(t)) {
                throw accessCheckException(t);
            }
        }

        private final RuntimeException accessCheckException(T t) {
            if (this.cclass == this.tclass) {
                return new ClassCastException();
            }
            return new RuntimeException((Throwable) new IllegalAccessException("Class " + this.cclass.getName() + " can not access a protected member of class " + this.tclass.getName() + " using an instance of " + t.getClass().getName()));
        }

        public final boolean compareAndSet(T t, long j, long j2) {
            accessCheck(t);
            synchronized (this) {
                Unsafe unsafe = f764U;
                if (unsafe.getLong(t, this.offset) != j) {
                    return false;
                }
                unsafe.putLong(t, this.offset, j2);
                return true;
            }
        }

        public final boolean weakCompareAndSet(T t, long j, long j2) {
            return compareAndSet(t, j, j2);
        }

        public final void set(T t, long j) {
            accessCheck(t);
            synchronized (this) {
                f764U.putLong(t, this.offset, j);
            }
        }

        public final void lazySet(T t, long j) {
            set(t, j);
        }

        public final long get(T t) {
            long j;
            accessCheck(t);
            synchronized (this) {
                j = f764U.getLong(t, this.offset);
            }
            return j;
        }
    }

    static boolean isSamePackage(Class<?> cls, Class<?> cls2) {
        return cls.getClassLoader() == cls2.getClassLoader() && Objects.equals(cls.getPackageName(), cls2.getPackageName());
    }
}
