package java.util.concurrent.atomic;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import jdk.internal.misc.Unsafe;
import jdk.internal.reflect.CallerSensitive;
import jdk.internal.reflect.Reflection;

public abstract class AtomicReferenceFieldUpdater<T, V> {
    public abstract boolean compareAndSet(T t, V v, V v2);

    public abstract V get(T t);

    public abstract void lazySet(T t, V v);

    public abstract void set(T t, V v);

    public abstract boolean weakCompareAndSet(T t, V v, V v2);

    @CallerSensitive
    public static <U, W> AtomicReferenceFieldUpdater<U, W> newUpdater(Class<U> cls, Class<W> cls2, String str) {
        return new AtomicReferenceFieldUpdaterImpl(cls, cls2, str, Reflection.getCallerClass());
    }

    protected AtomicReferenceFieldUpdater() {
    }

    public V getAndSet(T t, V v) {
        V v2;
        do {
            v2 = get(t);
        } while (!compareAndSet(t, v2, v));
        return v2;
    }

    public final V getAndUpdate(T t, UnaryOperator<V> unaryOperator) {
        V v;
        do {
            v = get(t);
        } while (!compareAndSet(t, v, unaryOperator.apply(v)));
        return v;
    }

    public final V updateAndGet(T t, UnaryOperator<V> unaryOperator) {
        Object obj;
        V apply;
        do {
            obj = get(t);
            apply = unaryOperator.apply(obj);
        } while (!compareAndSet(t, obj, apply));
        return apply;
    }

    public final V getAndAccumulate(T t, V v, BinaryOperator<V> binaryOperator) {
        V v2;
        do {
            v2 = get(t);
        } while (!compareAndSet(t, v2, binaryOperator.apply(v2, v)));
        return v2;
    }

    public final V accumulateAndGet(T t, V v, BinaryOperator<V> binaryOperator) {
        Object obj;
        V apply;
        do {
            obj = get(t);
            apply = binaryOperator.apply(obj, v);
        } while (!compareAndSet(t, obj, apply));
        return apply;
    }

    private static final class AtomicReferenceFieldUpdaterImpl<T, V> extends AtomicReferenceFieldUpdater<T, V> {

        /* renamed from: U */
        private static final Unsafe f766U = Unsafe.getUnsafe();
        private final Class<?> cclass;
        private final long offset;
        private final Class<T> tclass;
        private final Class<V> vclass;

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0031, code lost:
            if (r0 == false) goto L_0x0035;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        AtomicReferenceFieldUpdaterImpl(java.lang.Class<T> r3, java.lang.Class<V> r4, java.lang.String r5, java.lang.Class<?> r6) {
            /*
                r2 = this;
                r2.<init>()
                java.lang.reflect.Field r5 = r3.getDeclaredField(r5)     // Catch:{ Exception -> 0x005a }
                int r0 = r5.getModifiers()     // Catch:{ Exception -> 0x005a }
                r1 = 0
                sun.reflect.misc.ReflectUtil.ensureMemberAccess(r6, r3, r1, r0)     // Catch:{ Exception -> 0x005a }
                java.lang.Class r1 = r5.getType()     // Catch:{ Exception -> 0x005a }
                if (r4 != r1) goto L_0x0054
                boolean r1 = r4.isPrimitive()
                if (r1 != 0) goto L_0x004c
                boolean r1 = java.lang.reflect.Modifier.isVolatile(r0)
                if (r1 == 0) goto L_0x0044
                boolean r0 = java.lang.reflect.Modifier.isProtected(r0)
                if (r0 == 0) goto L_0x0034
                boolean r0 = r3.isAssignableFrom(r6)
                if (r0 == 0) goto L_0x0034
                boolean r0 = isSamePackage(r3, r6)
                if (r0 != 0) goto L_0x0034
                goto L_0x0035
            L_0x0034:
                r6 = r3
            L_0x0035:
                r2.cclass = r6
                r2.tclass = r3
                r2.vclass = r4
                jdk.internal.misc.Unsafe r3 = f766U
                long r3 = r3.objectFieldOffset(r5)
                r2.offset = r3
                return
            L_0x0044:
                java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
                java.lang.String r3 = "Must be volatile type"
                r2.<init>((java.lang.String) r3)
                throw r2
            L_0x004c:
                java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
                java.lang.String r3 = "Must be reference type"
                r2.<init>((java.lang.String) r3)
                throw r2
            L_0x0054:
                java.lang.ClassCastException r2 = new java.lang.ClassCastException
                r2.<init>()
                throw r2
            L_0x005a:
                r2 = move-exception
                java.lang.RuntimeException r3 = new java.lang.RuntimeException
                r3.<init>((java.lang.Throwable) r2)
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicReferenceFieldUpdater.AtomicReferenceFieldUpdaterImpl.<init>(java.lang.Class, java.lang.Class, java.lang.String, java.lang.Class):void");
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

        private final void valueCheck(V v) {
            if (v != null && !this.vclass.isInstance(v)) {
                throwCCE();
            }
        }

        static void throwCCE() {
            throw new ClassCastException();
        }

        public final boolean compareAndSet(T t, V v, V v2) {
            accessCheck(t);
            valueCheck(v2);
            return f766U.compareAndSetObject(t, this.offset, v, v2);
        }

        public final boolean weakCompareAndSet(T t, V v, V v2) {
            accessCheck(t);
            valueCheck(v2);
            return f766U.compareAndSetObject(t, this.offset, v, v2);
        }

        public final void set(T t, V v) {
            accessCheck(t);
            valueCheck(v);
            f766U.putObjectVolatile(t, this.offset, v);
        }

        public final void lazySet(T t, V v) {
            accessCheck(t);
            valueCheck(v);
            f766U.putObjectRelease(t, this.offset, v);
        }

        public final V get(T t) {
            accessCheck(t);
            return f766U.getObjectVolatile(t, this.offset);
        }

        public final V getAndSet(T t, V v) {
            accessCheck(t);
            valueCheck(v);
            return f766U.getAndSetObject(t, this.offset, v);
        }
    }
}
