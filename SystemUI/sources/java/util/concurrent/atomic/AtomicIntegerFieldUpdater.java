package java.util.concurrent.atomic;

import java.util.Objects;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import jdk.internal.misc.Unsafe;
import jdk.internal.reflect.CallerSensitive;
import jdk.internal.reflect.Reflection;

public abstract class AtomicIntegerFieldUpdater<T> {
    public abstract boolean compareAndSet(T t, int i, int i2);

    public abstract int get(T t);

    public abstract void lazySet(T t, int i);

    public abstract void set(T t, int i);

    public abstract boolean weakCompareAndSet(T t, int i, int i2);

    @CallerSensitive
    public static <U> AtomicIntegerFieldUpdater<U> newUpdater(Class<U> cls, String str) {
        return new AtomicIntegerFieldUpdaterImpl(cls, str, Reflection.getCallerClass());
    }

    protected AtomicIntegerFieldUpdater() {
    }

    public int getAndSet(T t, int i) {
        int i2;
        do {
            i2 = get(t);
        } while (!compareAndSet(t, i2, i));
        return i2;
    }

    public int getAndIncrement(T t) {
        int i;
        do {
            i = get(t);
        } while (!compareAndSet(t, i, i + 1));
        return i;
    }

    public int getAndDecrement(T t) {
        int i;
        do {
            i = get(t);
        } while (!compareAndSet(t, i, i - 1));
        return i;
    }

    public int getAndAdd(T t, int i) {
        int i2;
        do {
            i2 = get(t);
        } while (!compareAndSet(t, i2, i2 + i));
        return i2;
    }

    public int incrementAndGet(T t) {
        int i;
        int i2;
        do {
            i = get(t);
            i2 = i + 1;
        } while (!compareAndSet(t, i, i2));
        return i2;
    }

    public int decrementAndGet(T t) {
        int i;
        int i2;
        do {
            i = get(t);
            i2 = i - 1;
        } while (!compareAndSet(t, i, i2));
        return i2;
    }

    public int addAndGet(T t, int i) {
        int i2;
        int i3;
        do {
            i2 = get(t);
            i3 = i2 + i;
        } while (!compareAndSet(t, i2, i3));
        return i3;
    }

    public final int getAndUpdate(T t, IntUnaryOperator intUnaryOperator) {
        int i;
        do {
            i = get(t);
        } while (!compareAndSet(t, i, intUnaryOperator.applyAsInt(i)));
        return i;
    }

    public final int updateAndGet(T t, IntUnaryOperator intUnaryOperator) {
        int i;
        int applyAsInt;
        do {
            i = get(t);
            applyAsInt = intUnaryOperator.applyAsInt(i);
        } while (!compareAndSet(t, i, applyAsInt));
        return applyAsInt;
    }

    public final int getAndAccumulate(T t, int i, IntBinaryOperator intBinaryOperator) {
        int i2;
        do {
            i2 = get(t);
        } while (!compareAndSet(t, i2, intBinaryOperator.applyAsInt(i2, i)));
        return i2;
    }

    public final int accumulateAndGet(T t, int i, IntBinaryOperator intBinaryOperator) {
        int i2;
        int applyAsInt;
        do {
            i2 = get(t);
            applyAsInt = intBinaryOperator.applyAsInt(i2, i);
        } while (!compareAndSet(t, i2, applyAsInt));
        return applyAsInt;
    }

    private static final class AtomicIntegerFieldUpdaterImpl<T> extends AtomicIntegerFieldUpdater<T> {

        /* renamed from: U */
        private static final Unsafe f763U = Unsafe.getUnsafe();
        private final Class<?> cclass;
        private final long offset;
        private final Class<T> tclass;

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x002d, code lost:
            if (r0 == false) goto L_0x0031;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        AtomicIntegerFieldUpdaterImpl(java.lang.Class<T> r4, java.lang.String r5, java.lang.Class<?> r6) {
            /*
                r3 = this;
                r3.<init>()
                java.lang.reflect.Field r5 = r4.getDeclaredField(r5)     // Catch:{ Exception -> 0x004e }
                int r0 = r5.getModifiers()     // Catch:{ Exception -> 0x004e }
                r1 = 0
                sun.reflect.misc.ReflectUtil.ensureMemberAccess(r6, r4, r1, r0)     // Catch:{ Exception -> 0x004e }
                java.lang.Class r1 = r5.getType()
                java.lang.Class<java.lang.Integer> r2 = java.lang.Integer.TYPE
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
                java.lang.String r4 = "Must be integer type"
                r3.<init>((java.lang.String) r4)
                throw r3
            L_0x004e:
                r3 = move-exception
                java.lang.RuntimeException r4 = new java.lang.RuntimeException
                r4.<init>((java.lang.Throwable) r3)
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerFieldUpdater.AtomicIntegerFieldUpdaterImpl.<init>(java.lang.Class, java.lang.String, java.lang.Class):void");
        }

        private static boolean isSamePackage(Class<?> cls, Class<?> cls2) {
            return cls.getClassLoader() == cls2.getClassLoader() && Objects.equals(cls.getPackageName(), cls2.getPackageName());
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

        public final boolean compareAndSet(T t, int i, int i2) {
            accessCheck(t);
            return f763U.compareAndSetInt(t, this.offset, i, i2);
        }

        public final boolean weakCompareAndSet(T t, int i, int i2) {
            accessCheck(t);
            return f763U.compareAndSetInt(t, this.offset, i, i2);
        }

        public final void set(T t, int i) {
            accessCheck(t);
            f763U.putIntVolatile(t, this.offset, i);
        }

        public final void lazySet(T t, int i) {
            accessCheck(t);
            f763U.putIntRelease(t, this.offset, i);
        }

        public final int get(T t) {
            accessCheck(t);
            return f763U.getIntVolatile(t, this.offset);
        }

        public final int getAndSet(T t, int i) {
            accessCheck(t);
            return f763U.getAndSetInt(t, this.offset, i);
        }

        public final int getAndAdd(T t, int i) {
            accessCheck(t);
            return f763U.getAndAddInt(t, this.offset, i);
        }

        public final int getAndIncrement(T t) {
            return getAndAdd(t, 1);
        }

        public final int getAndDecrement(T t) {
            return getAndAdd(t, -1);
        }

        public final int incrementAndGet(T t) {
            return getAndAdd(t, 1) + 1;
        }

        public final int decrementAndGet(T t) {
            return getAndAdd(t, -1) - 1;
        }

        public final int addAndGet(T t, int i) {
            return getAndAdd(t, i) + i;
        }
    }
}
