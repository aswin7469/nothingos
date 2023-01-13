package java.util.concurrent.atomic;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class AtomicStampedReference<V> {
    private static final VarHandle PAIR;
    private volatile Pair<V> pair;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicStampedReference.casPair(java.util.concurrent.atomic.AtomicStampedReference$Pair, java.util.concurrent.atomic.AtomicStampedReference$Pair):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 4 more
        */
    private boolean casPair(java.util.concurrent.atomic.AtomicStampedReference.Pair<V> r1, java.util.concurrent.atomic.AtomicStampedReference.Pair<V> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicStampedReference.casPair(java.util.concurrent.atomic.AtomicStampedReference$Pair, java.util.concurrent.atomic.AtomicStampedReference$Pair):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicStampedReference.casPair(java.util.concurrent.atomic.AtomicStampedReference$Pair, java.util.concurrent.atomic.AtomicStampedReference$Pair):boolean");
    }

    private static class Pair<T> {
        final T reference;
        final int stamp;

        private Pair(T t, int i) {
            this.reference = t;
            this.stamp = i;
        }

        /* renamed from: of */
        static <T> Pair<T> m1771of(T t, int i) {
            return new Pair<>(t, i);
        }
    }

    public AtomicStampedReference(V v, int i) {
        this.pair = Pair.m1771of(v, i);
    }

    public V getReference() {
        return this.pair.reference;
    }

    public int getStamp() {
        return this.pair.stamp;
    }

    public V get(int[] iArr) {
        Pair<V> pair2 = this.pair;
        iArr[0] = pair2.stamp;
        return pair2.reference;
    }

    public boolean weakCompareAndSet(V v, V v2, int i, int i2) {
        return compareAndSet(v, v2, i, i2);
    }

    public boolean compareAndSet(V v, V v2, int i, int i2) {
        Pair<V> pair2 = this.pair;
        return v == pair2.reference && i == pair2.stamp && ((v2 == pair2.reference && i2 == pair2.stamp) || casPair(pair2, Pair.m1771of(v2, i2)));
    }

    public void set(V v, int i) {
        Pair<V> pair2 = this.pair;
        if (v != pair2.reference || i != pair2.stamp) {
            this.pair = Pair.m1771of(v, i);
        }
    }

    public boolean attemptStamp(V v, int i) {
        Pair<V> pair2 = this.pair;
        return v == pair2.reference && (i == pair2.stamp || casPair(pair2, Pair.m1771of(v, i)));
    }

    static {
        try {
            PAIR = MethodHandles.lookup().findVarHandle(AtomicStampedReference.class, "pair", Pair.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }
}
