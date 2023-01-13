package java.util.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;
import java.util.function.Predicate;

/*  JADX ERROR: NullPointerException in pass: ExtractFieldInit
    java.lang.NullPointerException
    	at jadx.core.utils.BlockUtils.isAllBlocksEmpty(BlockUtils.java:564)
    	at jadx.core.dex.visitors.ExtractFieldInit.getConstructorsList(ExtractFieldInit.java:245)
    	at jadx.core.dex.visitors.ExtractFieldInit.moveCommonFieldsInit(ExtractFieldInit.java:126)
    	at jadx.core.dex.visitors.ExtractFieldInit.visit(ExtractFieldInit.java:46)
    	at jadx.core.dex.visitors.ExtractFieldInit.visit(ExtractFieldInit.java:42)
    */
public class LinkedTransferQueue<E> extends AbstractQueue<E> implements TransferQueue<E>, Serializable {
    private static final int ASYNC = 1;
    private static final int CHAINED_SPINS = 64;
    private static final int FRONT_SPINS = 128;
    private static final VarHandle HEAD = null;
    static final VarHandle ITEM = null;
    private static final int MAX_HOPS = 8;

    /* renamed from: MP */
    private static final boolean f756MP = false;
    static final VarHandle NEXT = null;
    private static final int NOW = 0;
    private static final VarHandle SWEEPVOTES = null;
    static final int SWEEP_THRESHOLD = 32;
    private static final int SYNC = 2;
    private static final VarHandle TAIL = null;
    private static final int TIMED = 3;
    static final VarHandle WAITER = null;
    private static final long serialVersionUID = -3223113410248163686L;
    volatile transient Node head;
    private volatile transient int sweepVotes;
    private volatile transient Node tail;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.casHead(java.util.concurrent.LinkedTransferQueue$Node, java.util.concurrent.LinkedTransferQueue$Node):boolean, dex: classes4.dex
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
    private boolean casHead(java.util.concurrent.LinkedTransferQueue.Node r1, java.util.concurrent.LinkedTransferQueue.Node r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.casHead(java.util.concurrent.LinkedTransferQueue$Node, java.util.concurrent.LinkedTransferQueue$Node):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.casHead(java.util.concurrent.LinkedTransferQueue$Node, java.util.concurrent.LinkedTransferQueue$Node):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.casTail(java.util.concurrent.LinkedTransferQueue$Node, java.util.concurrent.LinkedTransferQueue$Node):boolean, dex: classes4.dex
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
    private boolean casTail(java.util.concurrent.LinkedTransferQueue.Node r1, java.util.concurrent.LinkedTransferQueue.Node r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.casTail(java.util.concurrent.LinkedTransferQueue$Node, java.util.concurrent.LinkedTransferQueue$Node):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.casTail(java.util.concurrent.LinkedTransferQueue$Node, java.util.concurrent.LinkedTransferQueue$Node):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.incSweepVotes():int, dex: classes4.dex
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
    private int incSweepVotes() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.incSweepVotes():int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.incSweepVotes():int");
    }

    static /* synthetic */ boolean lambda$clear$2(Object obj) {
        return true;
    }

    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    static {
        Class<LinkedTransferQueue> cls = LinkedTransferQueue.class;
        boolean z = true;
        if (Runtime.getRuntime().availableProcessors() <= 1) {
            z = false;
        }
        f756MP = z;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            HEAD = lookup.findVarHandle(cls, "head", Node.class);
            TAIL = lookup.findVarHandle(cls, "tail", Node.class);
            SWEEPVOTES = lookup.findVarHandle(cls, "sweepVotes", Integer.TYPE);
            ITEM = lookup.findVarHandle(Node.class, "item", Object.class);
            NEXT = lookup.findVarHandle(Node.class, "next", Node.class);
            WAITER = lookup.findVarHandle(Node.class, "waiter", Thread.class);
            Class<LockSupport> cls2 = LockSupport.class;
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }

    static final class Node {
        private static final long serialVersionUID = -3375979862319811754L;
        final boolean isData;
        volatile Object item;
        volatile Node next;
        volatile Thread waiter;

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.<init>(java.lang.Object):void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        Node(java.lang.Object r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.<init>(java.lang.Object):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.Node.<init>(java.lang.Object):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.appendRelaxed(java.util.concurrent.LinkedTransferQueue$Node):void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final void appendRelaxed(java.util.concurrent.LinkedTransferQueue.Node r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.appendRelaxed(java.util.concurrent.LinkedTransferQueue$Node):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.Node.appendRelaxed(java.util.concurrent.LinkedTransferQueue$Node):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.casItem(java.lang.Object, java.lang.Object):boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final boolean casItem(java.lang.Object r1, java.lang.Object r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.casItem(java.lang.Object, java.lang.Object):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.Node.casItem(java.lang.Object, java.lang.Object):boolean");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.casNext(java.util.concurrent.LinkedTransferQueue$Node, java.util.concurrent.LinkedTransferQueue$Node):boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final boolean casNext(java.util.concurrent.LinkedTransferQueue.Node r1, java.util.concurrent.LinkedTransferQueue.Node r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.casNext(java.util.concurrent.LinkedTransferQueue$Node, java.util.concurrent.LinkedTransferQueue$Node):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.Node.casNext(java.util.concurrent.LinkedTransferQueue$Node, java.util.concurrent.LinkedTransferQueue$Node):boolean");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.forgetContents():void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final void forgetContents() {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.forgetContents():void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.Node.forgetContents():void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.selfLink():void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final void selfLink() {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.LinkedTransferQueue.Node.selfLink():void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.Node.selfLink():void");
        }

        Node() {
            this.isData = true;
        }

        /* access modifiers changed from: package-private */
        public final boolean isMatched() {
            return this.isData == (this.item == null);
        }

        /* access modifiers changed from: package-private */
        public final boolean tryMatch(Object obj, Object obj2) {
            if (!casItem(obj, obj2)) {
                return false;
            }
            LockSupport.unpark(this.waiter);
            return true;
        }

        /* access modifiers changed from: package-private */
        public final boolean cannotPrecede(boolean z) {
            boolean z2 = this.isData;
            if (z2 == z) {
                return false;
            }
            if (z2 != (this.item == null)) {
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: private */
    public boolean tryCasSuccessor(Node node, Node node2, Node node3) {
        if (node != null) {
            return node.casNext(node2, node3);
        }
        if (!casHead(node2, node3)) {
            return false;
        }
        node2.selfLink();
        return true;
    }

    private Node skipDeadNodes(Node node, Node node2, Node node3, Node node4) {
        if (node4 == null) {
            if (node2 == node3) {
                return node;
            }
            node4 = node3;
        }
        return (!tryCasSuccessor(node, node2, node4) || (node != null && node.isMatched())) ? node3 : node;
    }

    private void skipDeadNodesNearHead(Node node, Node node2) {
        while (true) {
            Node node3 = node2.next;
            if (node3 == null) {
                break;
            } else if (!node3.isMatched()) {
                node2 = node3;
                break;
            } else if (node2 != node3) {
                node2 = node3;
            } else {
                return;
            }
        }
        if (casHead(node, node2)) {
            node.selfLink();
        }
    }

    private E xfer(E e, boolean z, int i, long j) {
        Node node;
        Node node2;
        E e2 = e;
        boolean z2 = z;
        int i2 = i;
        if (z2) {
            e.getClass();
        }
        Node node3 = null;
        Node node4 = null;
        Node node5 = null;
        while (true) {
            Node node6 = this.tail;
            if (node3 == node6 || node6.isData != z2) {
                node = this.head;
                node2 = node;
            } else {
                node2 = node4;
                node = node6;
            }
            while (true) {
                if (node.isData != z2) {
                    E e3 = node.item;
                    if (z2 == (e3 == null)) {
                        if (node2 == null) {
                            node2 = this.head;
                        }
                        if (node.tryMatch(e3, e)) {
                            if (node2 != node) {
                                skipDeadNodesNearHead(node2, node);
                            }
                            return e3;
                        }
                    }
                }
                Node node7 = node.next;
                if (node7 == null) {
                    if (i2 == 0) {
                        return e2;
                    }
                    if (node5 == null) {
                        node5 = new Node(e);
                    }
                    if (node.casNext((Node) null, node5)) {
                        if (node != node6) {
                            casTail(node6, node5);
                        }
                        if (i2 == 1) {
                            return e2;
                        }
                        return awaitMatch(node5, node, e, i2 == 3, j);
                    }
                } else if (node == node7) {
                    break;
                } else {
                    node = node7;
                }
            }
            node4 = node2;
            node3 = node6;
        }
    }

    private E awaitMatch(Node node, Node node2, E e, boolean z, long j) {
        long nanoTime = z ? System.nanoTime() + j : 0;
        Thread currentThread = Thread.currentThread();
        int i = -1;
        ThreadLocalRandom threadLocalRandom = null;
        while (true) {
            E e2 = node.item;
            if (e2 != e) {
                node.forgetContents();
                return e2;
            } else if (currentThread.isInterrupted() || (z && j <= 0)) {
                if (node.casItem(e, node.isData ? null : node)) {
                    unsplice(node2, node);
                    return e;
                }
            } else if (i < 0) {
                i = spinsFor(node2, node.isData);
                if (i > 0) {
                    threadLocalRandom = ThreadLocalRandom.current();
                }
            } else if (i > 0) {
                i--;
                if (threadLocalRandom.nextInt(64) == 0) {
                    Thread.yield();
                }
            } else if (node.waiter == null) {
                node.waiter = currentThread;
            } else if (z) {
                j = nanoTime - System.nanoTime();
                if (j > 0) {
                    LockSupport.parkNanos(this, j);
                }
            } else {
                LockSupport.park(this);
            }
        }
    }

    private static int spinsFor(Node node, boolean z) {
        if (!f756MP || node == null) {
            return 0;
        }
        if (node.isData != z) {
            return 192;
        }
        if (node.isMatched()) {
            return 128;
        }
        return node.waiter == null ? 64 : 0;
    }

    /* access modifiers changed from: package-private */
    public final Node firstDataNode() {
        Node node;
        Node node2;
        Node node3;
        loop0:
        while (true) {
            node = this.head;
            node2 = node;
            while (true) {
                if (node2 == null) {
                    break loop0;
                }
                if (node2.item == null) {
                    if (!node2.isData) {
                        break loop0;
                    }
                } else if (node2.isData) {
                    node3 = node2;
                    break loop0;
                }
                Node node4 = node2.next;
                if (node4 == null) {
                    break loop0;
                } else if (node2 != node4) {
                    node2 = node4;
                }
            }
        }
        node3 = null;
        if (node2 != node && casHead(node, node2)) {
            node.selfLink();
        }
        return node3;
    }

    private int countOfMode(boolean z) {
        int i;
        loop0:
        while (true) {
            Node node = this.head;
            i = 0;
            while (true) {
                if (node == null) {
                    break loop0;
                }
                if (!node.isMatched()) {
                    if (node.isData == z) {
                        i++;
                        if (i == Integer.MAX_VALUE) {
                            break loop0;
                        }
                    } else {
                        return 0;
                    }
                }
                Node node2 = node.next;
                if (node != node2) {
                    node = node2;
                }
            }
        }
        return i;
    }

    public String toString() {
        int i;
        int i2;
        String[] strArr = null;
        loop0:
        while (true) {
            Node node = this.head;
            i = 0;
            i2 = 0;
            while (true) {
                if (node == null) {
                    break loop0;
                }
                Object obj = node.item;
                if (!node.isData) {
                    if (obj == null) {
                        break loop0;
                    }
                } else if (obj != null) {
                    if (strArr == null) {
                        strArr = new String[4];
                    } else if (i2 == strArr.length) {
                        strArr = (String[]) Arrays.copyOf((T[]) strArr, i2 * 2);
                    }
                    String obj2 = obj.toString();
                    strArr[i2] = obj2;
                    i += obj2.length();
                    i2++;
                }
                Node node2 = node.next;
                if (node != node2) {
                    node = node2;
                }
            }
        }
        if (i2 == 0) {
            return "[]";
        }
        return Helpers.toString(strArr, i2, i);
    }

    private Object[] toArrayInternal(Object[] objArr) {
        int i;
        Object[] objArr2 = objArr;
        loop0:
        while (true) {
            Node node = this.head;
            i = 0;
            while (true) {
                if (node == null) {
                    break loop0;
                }
                Object obj = node.item;
                if (!node.isData) {
                    if (obj == null) {
                        break loop0;
                    }
                } else if (obj != null) {
                    if (objArr2 == null) {
                        objArr2 = new Object[4];
                    } else if (i == objArr2.length) {
                        objArr2 = Arrays.copyOf((T[]) objArr2, (i + 4) * 2);
                    }
                    objArr2[i] = obj;
                    i++;
                }
                Node node2 = node.next;
                if (node != node2) {
                    node = node2;
                }
            }
        }
        if (objArr2 == null) {
            return new Object[0];
        }
        if (objArr == null || i > objArr.length) {
            return i == objArr2.length ? objArr2 : Arrays.copyOf((T[]) objArr2, i);
        }
        if (objArr != objArr2) {
            System.arraycopy((Object) objArr2, 0, (Object) objArr, 0, i);
        }
        if (i < objArr.length) {
            objArr[i] = null;
        }
        return objArr;
    }

    public Object[] toArray() {
        return toArrayInternal((Object[]) null);
    }

    public <T> T[] toArray(T[] tArr) {
        Objects.requireNonNull(tArr);
        return toArrayInternal(tArr);
    }

    final class Itr implements Iterator<E> {
        private Node ancestor;
        private Node lastRet;
        private E nextItem;
        private Node nextNode;

        private void advance(Node node) {
            Node node2;
            Node node3 = node == null ? LinkedTransferQueue.this.head : node.next;
            Node node4 = node3;
            while (node3 != null) {
                E e = node3.item;
                if (e == null || !node3.isData) {
                    if (!node3.isData && e == null) {
                        break;
                    }
                    if (node4 != node3) {
                        if (!LinkedTransferQueue.this.tryCasSuccessor(node, node4, node3)) {
                            node4 = node3.next;
                            node = node3;
                            node2 = node4;
                        } else {
                            node4 = node3;
                        }
                    }
                    Node node5 = node3.next;
                    if (node3 == node5) {
                        node2 = LinkedTransferQueue.this.head;
                        node4 = node2;
                        node = null;
                    } else {
                        node2 = node5;
                    }
                } else {
                    this.nextNode = node3;
                    this.nextItem = e;
                    if (node4 != node3) {
                        boolean unused = LinkedTransferQueue.this.tryCasSuccessor(node, node4, node3);
                        return;
                    }
                    return;
                }
            }
            this.nextItem = null;
            this.nextNode = null;
        }

        Itr() {
            advance((Node) null);
        }

        public final boolean hasNext() {
            return this.nextNode != null;
        }

        public final E next() {
            Node node = this.nextNode;
            if (node != null) {
                E e = this.nextItem;
                this.lastRet = node;
                advance(node);
                return e;
            }
            throw new NoSuchElementException();
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            Node node = null;
            while (true) {
                Node node2 = this.nextNode;
                if (node2 == null) {
                    break;
                }
                consumer.accept(this.nextItem);
                advance(node2);
                node = node2;
            }
            if (node != null) {
                this.lastRet = node;
            }
        }

        public final void remove() {
            Node node;
            Node node2 = this.lastRet;
            if (node2 != null) {
                this.lastRet = null;
                if (node2.item != null) {
                    Node node3 = this.ancestor;
                    Node node4 = node3 == null ? LinkedTransferQueue.this.head : node3.next;
                    Node node5 = node4;
                    while (node4 != null) {
                        if (node4 == node2) {
                            Object obj = node4.item;
                            if (obj != null) {
                                node4.tryMatch(obj, (Object) null);
                            }
                            Node node6 = node4.next;
                            if (node6 != null) {
                                node4 = node6;
                            }
                            if (node5 != node4) {
                                boolean unused = LinkedTransferQueue.this.tryCasSuccessor(node3, node5, node4);
                            }
                            this.ancestor = node3;
                            return;
                        }
                        Object obj2 = node4.item;
                        boolean z = obj2 != null && node4.isData;
                        if (z || node4.isData || obj2 != null) {
                            if (node5 != node4) {
                                if (LinkedTransferQueue.this.tryCasSuccessor(node3, node5, node4)) {
                                    node5 = node4;
                                }
                                node5 = node4.next;
                                node3 = node4;
                                node = node5;
                            }
                            if (!z) {
                                Node node7 = node4.next;
                                if (node4 == node7) {
                                    node = LinkedTransferQueue.this.head;
                                    node5 = node;
                                    node3 = null;
                                } else {
                                    node = node7;
                                }
                            }
                            node5 = node4.next;
                            node3 = node4;
                            node = node5;
                        } else {
                            return;
                        }
                    }
                    return;
                }
                return;
            }
            throw new IllegalStateException();
        }
    }

    final class LTQSpliterator implements Spliterator<E> {
        static final int MAX_BATCH = 33554432;
        int batch;
        Node current;
        boolean exhausted;

        public int characteristics() {
            return 4368;
        }

        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        LTQSpliterator() {
        }

        public Spliterator<E> trySplit() {
            Node node;
            Node current2 = current();
            if (current2 == null || (node = current2.next) == null) {
                return null;
            }
            int min = Math.min(this.batch + 1, 33554432);
            this.batch = min;
            Object[] objArr = null;
            int i = 0;
            while (true) {
                Object obj = current2.item;
                if (!current2.isData) {
                    if (obj == null) {
                        current2 = null;
                        break;
                    }
                } else if (obj != null) {
                    if (objArr == null) {
                        objArr = new Object[min];
                    }
                    objArr[i] = obj;
                    i++;
                }
                current2 = current2 == node ? LinkedTransferQueue.this.firstDataNode() : node;
                if (current2 != null && (node = current2.next) != null) {
                    if (i >= min) {
                        break;
                    }
                } else {
                    break;
                }
            }
            setCurrent(current2);
            if (i == 0) {
                return null;
            }
            return Spliterators.spliterator(objArr, 0, i, 4368);
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            Node current2 = current();
            if (current2 != null) {
                this.current = null;
                this.exhausted = true;
                LinkedTransferQueue.this.forEachFrom(consumer, current2);
            }
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            Object obj;
            Node node;
            Objects.requireNonNull(consumer);
            Node current2 = current();
            if (current2 == null) {
                return false;
            }
            while (true) {
                obj = current2.item;
                boolean z = current2.isData;
                node = current2.next;
                if (current2 == node) {
                    node = LinkedTransferQueue.this.head;
                }
                if (z) {
                    if (obj != null) {
                        break;
                    }
                } else if (obj == null) {
                    node = null;
                }
                if (node == null) {
                    obj = null;
                    break;
                }
                current2 = node;
            }
            setCurrent(node);
            if (obj == null) {
                return false;
            }
            consumer.accept(obj);
            return true;
        }

        private void setCurrent(Node node) {
            this.current = node;
            if (node == null) {
                this.exhausted = true;
            }
        }

        private Node current() {
            Node node = this.current;
            if (node != null || this.exhausted) {
                return node;
            }
            Node firstDataNode = LinkedTransferQueue.this.firstDataNode();
            setCurrent(firstDataNode);
            return firstDataNode;
        }
    }

    public Spliterator<E> spliterator() {
        return new LTQSpliterator();
    }

    /* access modifiers changed from: package-private */
    public final void unsplice(Node node, Node node2) {
        node2.waiter = null;
        if (node != null && node.next == node2) {
            Node node3 = node2.next;
            if (node3 == null || (node3 != node2 && node.casNext(node2, node3) && node.isMatched())) {
                while (true) {
                    Node node4 = this.head;
                    if (node4 != node && node4 != node2) {
                        if (node4.isMatched()) {
                            Node node5 = node4.next;
                            if (node5 != null) {
                                if (node5 != node4 && casHead(node4, node5)) {
                                    node4.selfLink();
                                }
                            } else {
                                return;
                            }
                        } else if (node.next != node && node2.next != node2 && (incSweepVotes() & 31) == 0) {
                            sweep();
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private void sweep() {
        Node node = this.head;
        while (node != null) {
            Node node2 = node.next;
            if (node2 == null) {
                return;
            }
            if (!node2.isMatched()) {
                node = node2;
            } else {
                Node node3 = node2.next;
                if (node3 != null) {
                    if (node2 == node3) {
                        node = this.head;
                    } else {
                        node.casNext(node2, node3);
                    }
                } else {
                    return;
                }
            }
        }
    }

    public LinkedTransferQueue() {
        Node node = new Node();
        this.tail = node;
        this.head = node;
    }

    public LinkedTransferQueue(Collection<? extends E> collection) {
        Node node = null;
        Node node2 = null;
        for (Object requireNonNull : collection) {
            Node node3 = new Node(Objects.requireNonNull(requireNonNull));
            if (node == null) {
                node = node3;
            } else {
                node2.appendRelaxed(node3);
            }
            node2 = node3;
        }
        if (node == null) {
            node = new Node();
            node2 = node;
        }
        this.head = node;
        this.tail = node2;
    }

    public void put(E e) {
        xfer(e, true, 1, 0);
    }

    public boolean offer(E e, long j, TimeUnit timeUnit) {
        xfer(e, true, 1, 0);
        return true;
    }

    public boolean offer(E e) {
        xfer(e, true, 1, 0);
        return true;
    }

    public boolean add(E e) {
        xfer(e, true, 1, 0);
        return true;
    }

    public boolean tryTransfer(E e) {
        return xfer(e, true, 0, 0) == null;
    }

    public void transfer(E e) throws InterruptedException {
        if (xfer(e, true, 2, 0) != null) {
            Thread.interrupted();
            throw new InterruptedException();
        }
    }

    public boolean tryTransfer(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        if (xfer(e, true, 3, timeUnit.toNanos(j)) == null) {
            return true;
        }
        if (!Thread.interrupted()) {
            return false;
        }
        throw new InterruptedException();
    }

    public E take() throws InterruptedException {
        E xfer = xfer((Object) null, false, 2, 0);
        if (xfer != null) {
            return xfer;
        }
        Thread.interrupted();
        throw new InterruptedException();
    }

    public E poll(long j, TimeUnit timeUnit) throws InterruptedException {
        E xfer = xfer((Object) null, false, 3, timeUnit.toNanos(j));
        if (xfer != null || !Thread.interrupted()) {
            return xfer;
        }
        throw new InterruptedException();
    }

    public E poll() {
        return xfer((Object) null, false, 0, 0);
    }

    public int drainTo(Collection<? super E> collection) {
        Objects.requireNonNull(collection);
        if (collection != this) {
            int i = 0;
            while (true) {
                Object poll = poll();
                if (poll == null) {
                    return i;
                }
                collection.add(poll);
                i++;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int drainTo(Collection<? super E> collection, int i) {
        Objects.requireNonNull(collection);
        if (collection != this) {
            int i2 = 0;
            while (i2 < i) {
                Object poll = poll();
                if (poll == null) {
                    break;
                }
                collection.add(poll);
                i2++;
            }
            return i2;
        }
        throw new IllegalArgumentException();
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    public E peek() {
        while (true) {
            Node node = this.head;
            while (true) {
                if (node == null) {
                    return null;
                }
                E e = node.item;
                if (node.isData) {
                    if (e != null) {
                        return e;
                    }
                } else if (e == null) {
                    return null;
                }
                Node node2 = node.next;
                if (node != node2) {
                    node = node2;
                }
            }
        }
    }

    public boolean isEmpty() {
        return firstDataNode() == null;
    }

    public boolean hasWaitingConsumer() {
        while (true) {
            Node node = this.head;
            while (true) {
                if (node == null) {
                    return false;
                }
                Object obj = node.item;
                if (node.isData) {
                    if (obj != null) {
                        return false;
                    }
                } else if (obj == null) {
                    return true;
                }
                Node node2 = node.next;
                if (node != node2) {
                    node = node2;
                }
            }
        }
    }

    public int size() {
        return countOfMode(true);
    }

    public int getWaitingConsumerCount() {
        return countOfMode(false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0040 A[EDGE_INSN: B:33:0x0040->B:25:0x0040 ?: BREAK  
    EDGE_INSN: B:34:0x0040->B:25:0x0040 ?: BREAK  ] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean remove(java.lang.Object r9) {
        /*
            r8 = this;
            r0 = 0
            if (r9 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.util.concurrent.LinkedTransferQueue$Node r1 = r8.head
            r2 = 0
            r3 = r2
        L_0x0008:
            if (r1 == 0) goto L_0x0045
            java.util.concurrent.LinkedTransferQueue$Node r4 = r1.next
            java.lang.Object r5 = r1.item
            if (r5 == 0) goto L_0x0028
            boolean r6 = r1.isData
            if (r6 == 0) goto L_0x002d
            boolean r6 = r9.equals(r5)
            if (r6 == 0) goto L_0x0025
            boolean r5 = r1.tryMatch(r5, r2)
            if (r5 == 0) goto L_0x0025
            r8.skipDeadNodes(r3, r1, r1, r4)
            r8 = 1
            return r8
        L_0x0025:
            r3 = r1
        L_0x0026:
            r1 = r4
            goto L_0x0008
        L_0x0028:
            boolean r5 = r1.isData
            if (r5 != 0) goto L_0x002d
            goto L_0x0045
        L_0x002d:
            r5 = r1
        L_0x002e:
            if (r4 == 0) goto L_0x0040
            boolean r6 = r4.isMatched()
            if (r6 != 0) goto L_0x0037
            goto L_0x0040
        L_0x0037:
            if (r5 != r4) goto L_0x003a
            goto L_0x0004
        L_0x003a:
            java.util.concurrent.LinkedTransferQueue$Node r5 = r4.next
            r7 = r5
            r5 = r4
            r4 = r7
            goto L_0x002e
        L_0x0040:
            java.util.concurrent.LinkedTransferQueue$Node r3 = r8.skipDeadNodes(r3, r1, r5, r4)
            goto L_0x0026
        L_0x0045:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.remove(java.lang.Object):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0036 A[EDGE_INSN: B:30:0x0036->B:22:0x0036 ?: BREAK  
    EDGE_INSN: B:31:0x0036->B:22:0x0036 ?: BREAK  ] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean contains(java.lang.Object r8) {
        /*
            r7 = this;
            r0 = 0
            if (r8 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.util.concurrent.LinkedTransferQueue$Node r1 = r7.head
            r2 = 0
        L_0x0007:
            if (r1 == 0) goto L_0x003b
            java.util.concurrent.LinkedTransferQueue$Node r3 = r1.next
            java.lang.Object r4 = r1.item
            if (r4 == 0) goto L_0x001e
            boolean r5 = r1.isData
            if (r5 == 0) goto L_0x0023
            boolean r2 = r8.equals(r4)
            if (r2 == 0) goto L_0x001b
            r7 = 1
            return r7
        L_0x001b:
            r2 = r1
        L_0x001c:
            r1 = r3
            goto L_0x0007
        L_0x001e:
            boolean r4 = r1.isData
            if (r4 != 0) goto L_0x0023
            goto L_0x003b
        L_0x0023:
            r4 = r1
        L_0x0024:
            if (r3 == 0) goto L_0x0036
            boolean r5 = r3.isMatched()
            if (r5 != 0) goto L_0x002d
            goto L_0x0036
        L_0x002d:
            if (r4 != r3) goto L_0x0030
            goto L_0x0004
        L_0x0030:
            java.util.concurrent.LinkedTransferQueue$Node r4 = r3.next
            r6 = r4
            r4 = r3
            r3 = r6
            goto L_0x0024
        L_0x0036:
            java.util.concurrent.LinkedTransferQueue$Node r2 = r7.skipDeadNodes(r2, r1, r4, r3)
            goto L_0x001c
        L_0x003b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.contains(java.lang.Object):boolean");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Iterator it = iterator();
        while (it.hasNext()) {
            objectOutputStream.writeObject(it.next());
        }
        objectOutputStream.writeObject((Object) null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Node node = null;
        Node node2 = null;
        while (true) {
            Object readObject = objectInputStream.readObject();
            if (readObject == null) {
                break;
            }
            Node node3 = new Node(readObject);
            if (node == null) {
                node = node3;
            } else {
                node2.appendRelaxed(node3);
            }
            node2 = node3;
        }
        if (node == null) {
            node = new Node();
            node2 = node;
        }
        this.head = node;
        this.tail = node2;
    }

    public boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return bulkRemove(predicate);
    }

    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new LinkedTransferQueue$$ExternalSyntheticLambda1(collection));
    }

    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new LinkedTransferQueue$$ExternalSyntheticLambda2(collection));
    }

    static /* synthetic */ boolean lambda$retainAll$1(Collection collection, Object obj) {
        return !collection.contains(obj);
    }

    public void clear() {
        bulkRemove(new LinkedTransferQueue$$ExternalSyntheticLambda0());
    }

    private boolean bulkRemove(Predicate<? super E> predicate) {
        boolean z = false;
        loop0:
        while (true) {
            Node node = this.head;
            Node node2 = node;
            Node node3 = null;
            int i = 8;
            while (true) {
                if (node == null) {
                    break loop0;
                }
                Node node4 = node.next;
                Object obj = node.item;
                boolean z2 = obj != null && node.isData;
                if (!z2) {
                    if (!node.isData && obj == null) {
                        break loop0;
                    }
                } else if (predicate.test(obj)) {
                    if (node.tryMatch(obj, (Object) null)) {
                        z = true;
                    }
                    z2 = false;
                }
                if (z2 || node4 == null || i - 1 == 0) {
                    if (node2 != node) {
                        if (tryCasSuccessor(node3, node2, node)) {
                            node2 = node;
                        }
                        node3 = node;
                        i = 8;
                        node2 = node4;
                    }
                    if (!z2) {
                    }
                    node3 = node;
                    i = 8;
                    node2 = node4;
                } else if (node == node4) {
                }
                node = node4;
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0023  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void forEachFrom(java.util.function.Consumer<? super E> r7, java.util.concurrent.LinkedTransferQueue.Node r8) {
        /*
            r6 = this;
            r0 = 0
        L_0x0001:
            r1 = r0
        L_0x0002:
            if (r8 == 0) goto L_0x0033
            java.util.concurrent.LinkedTransferQueue$Node r2 = r8.next
            java.lang.Object r3 = r8.item
            if (r3 == 0) goto L_0x0014
            boolean r4 = r8.isData
            if (r4 == 0) goto L_0x0019
            r7.accept(r3)
        L_0x0011:
            r1 = r8
            r8 = r2
            goto L_0x0002
        L_0x0014:
            boolean r3 = r8.isData
            if (r3 != 0) goto L_0x0019
            goto L_0x0033
        L_0x0019:
            r3 = r8
        L_0x001a:
            if (r2 == 0) goto L_0x002e
            boolean r4 = r2.isMatched()
            if (r4 != 0) goto L_0x0023
            goto L_0x002e
        L_0x0023:
            if (r3 != r2) goto L_0x0028
            java.util.concurrent.LinkedTransferQueue$Node r8 = r6.head
            goto L_0x0001
        L_0x0028:
            java.util.concurrent.LinkedTransferQueue$Node r3 = r2.next
            r5 = r3
            r3 = r2
            r2 = r5
            goto L_0x001a
        L_0x002e:
            java.util.concurrent.LinkedTransferQueue$Node r8 = r6.skipDeadNodes(r1, r8, r3, r2)
            goto L_0x0011
        L_0x0033:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.forEachFrom(java.util.function.Consumer, java.util.concurrent.LinkedTransferQueue$Node):void");
    }

    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        forEachFrom(consumer, this.head);
    }
}
