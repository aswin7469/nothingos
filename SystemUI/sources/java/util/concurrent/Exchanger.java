package java.util.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class Exchanger<V> {

    /* renamed from: AA */
    private static final VarHandle f750AA;
    private static final int ASHIFT = 5;
    private static final VarHandle BOUND;
    static final int FULL;
    private static final VarHandle MATCH;
    private static final int MMASK = 255;
    private static final int NCPU;
    private static final Object NULL_ITEM = new Object();
    private static final int SEQ = 256;
    private static final VarHandle SLOT;
    private static final int SPINS = 1024;
    private static final Object TIMED_OUT = new Object();
    private volatile Node[] arena;
    private volatile int bound;
    private final Participant participant = new Participant();
    private volatile Node slot;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.Exchanger.arenaExchange(java.lang.Object, boolean, long):java.lang.Object, dex: classes4.dex
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
    private final java.lang.Object arenaExchange(java.lang.Object r1, boolean r2, long r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.Exchanger.arenaExchange(java.lang.Object, boolean, long):java.lang.Object, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Exchanger.arenaExchange(java.lang.Object, boolean, long):java.lang.Object");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.Exchanger.slotExchange(java.lang.Object, boolean, long):java.lang.Object, dex: classes4.dex
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
    private final java.lang.Object slotExchange(java.lang.Object r1, boolean r2, long r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.Exchanger.slotExchange(java.lang.Object, boolean, long):java.lang.Object, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Exchanger.slotExchange(java.lang.Object, boolean, long):java.lang.Object");
    }

    static {
        Class<Exchanger> cls = Exchanger.class;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        NCPU = availableProcessors;
        FULL = availableProcessors >= 510 ? 255 : availableProcessors >>> 1;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            BOUND = lookup.findVarHandle(cls, "bound", Integer.TYPE);
            SLOT = lookup.findVarHandle(cls, "slot", Node.class);
            MATCH = lookup.findVarHandle(Node.class, "match", Object.class);
            f750AA = MethodHandles.arrayElementVarHandle(Node[].class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }

    static final class Node {
        int bound;
        int collides;
        int hash;
        int index;
        Object item;
        volatile Object match;
        volatile Thread parked;

        Node() {
        }
    }

    static final class Participant extends ThreadLocal<Node> {
        Participant() {
        }

        public Node initialValue() {
            return new Node();
        }
    }

    public V exchange(V v) throws InterruptedException {
        V v2;
        if (v == null) {
            v = NULL_ITEM;
        }
        if ((this.arena != null || (v2 = slotExchange(v, false, 0)) == null) && (Thread.interrupted() || (v2 = arenaExchange(v, false, 0)) == null)) {
            throw new InterruptedException();
        } else if (v2 == NULL_ITEM) {
            return null;
        } else {
            return v2;
        }
    }

    public V exchange(V v, long j, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        V v2;
        if (v == null) {
            v = NULL_ITEM;
        }
        long nanos = timeUnit.toNanos(j);
        if ((this.arena != null || (v2 = slotExchange(v, true, nanos)) == null) && (Thread.interrupted() || (v2 = arenaExchange(v, true, nanos)) == null)) {
            throw new InterruptedException();
        } else if (v2 == TIMED_OUT) {
            throw new TimeoutException();
        } else if (v2 == NULL_ITEM) {
            return null;
        } else {
            return v2;
        }
    }
}
