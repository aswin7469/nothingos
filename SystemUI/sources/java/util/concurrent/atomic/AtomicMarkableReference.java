package java.util.concurrent.atomic;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class AtomicMarkableReference<V> {
    private static final VarHandle PAIR;
    private volatile Pair<V> pair;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicMarkableReference.casPair(java.util.concurrent.atomic.AtomicMarkableReference$Pair, java.util.concurrent.atomic.AtomicMarkableReference$Pair):boolean, dex: classes4.dex
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
    private boolean casPair(java.util.concurrent.atomic.AtomicMarkableReference.Pair<V> r1, java.util.concurrent.atomic.AtomicMarkableReference.Pair<V> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicMarkableReference.casPair(java.util.concurrent.atomic.AtomicMarkableReference$Pair, java.util.concurrent.atomic.AtomicMarkableReference$Pair):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicMarkableReference.casPair(java.util.concurrent.atomic.AtomicMarkableReference$Pair, java.util.concurrent.atomic.AtomicMarkableReference$Pair):boolean");
    }

    private static class Pair<T> {
        final boolean mark;
        final T reference;

        private Pair(T t, boolean z) {
            this.reference = t;
            this.mark = z;
        }

        /* renamed from: of */
        static <T> Pair<T> m1764of(T t, boolean z) {
            return new Pair<>(t, z);
        }
    }

    public AtomicMarkableReference(V v, boolean z) {
        this.pair = Pair.m1764of(v, z);
    }

    public V getReference() {
        return this.pair.reference;
    }

    public boolean isMarked() {
        return this.pair.mark;
    }

    public V get(boolean[] zArr) {
        Pair<V> pair2 = this.pair;
        zArr[0] = pair2.mark;
        return pair2.reference;
    }

    public boolean weakCompareAndSet(V v, V v2, boolean z, boolean z2) {
        return compareAndSet(v, v2, z, z2);
    }

    public boolean compareAndSet(V v, V v2, boolean z, boolean z2) {
        Pair<V> pair2 = this.pair;
        return v == pair2.reference && z == pair2.mark && ((v2 == pair2.reference && z2 == pair2.mark) || casPair(pair2, Pair.m1764of(v2, z2)));
    }

    public void set(V v, boolean z) {
        Pair<V> pair2 = this.pair;
        if (v != pair2.reference || z != pair2.mark) {
            this.pair = Pair.m1764of(v, z);
        }
    }

    public boolean attemptMark(V v, boolean z) {
        Pair<V> pair2 = this.pair;
        return v == pair2.reference && (z == pair2.mark || casPair(pair2, Pair.m1764of(v, z)));
    }

    static {
        try {
            PAIR = MethodHandles.lookup().findVarHandle(AtomicMarkableReference.class, "pair", Pair.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }
}
