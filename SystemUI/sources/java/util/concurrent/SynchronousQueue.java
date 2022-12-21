package java.util.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronousQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {
    static final int MAX_TIMED_SPINS;
    static final int MAX_UNTIMED_SPINS;
    static final long SPIN_FOR_TIMEOUT_THRESHOLD = 1000;
    private static final long serialVersionUID = -3223113410248163686L;
    private ReentrantLock qlock;
    private volatile transient Transferer<E> transferer;
    private WaitQueue waitingConsumers;
    private WaitQueue waitingProducers;

    public void clear() {
    }

    public boolean contains(Object obj) {
        return false;
    }

    public boolean isEmpty() {
        return true;
    }

    public E peek() {
        return null;
    }

    public int remainingCapacity() {
        return 0;
    }

    public boolean remove(Object obj) {
        return false;
    }

    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    public int size() {
        return 0;
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public String toString() {
        return "[]";
    }

    static abstract class Transferer<E> {
        /* access modifiers changed from: package-private */
        public abstract E transfer(E e, boolean z, long j);

        Transferer() {
        }
    }

    static {
        int i = Runtime.getRuntime().availableProcessors() < 2 ? 0 : 32;
        MAX_TIMED_SPINS = i;
        MAX_UNTIMED_SPINS = i * 16;
        Class<LockSupport> cls = LockSupport.class;
    }

    static final class TransferStack<E> extends Transferer<E> {
        static final int DATA = 1;
        static final int FULFILLING = 2;
        static final int REQUEST = 0;
        private static final VarHandle SHEAD;
        volatile SNode head;

        static boolean isFulfilling(int i) {
            return (i & 2) != 0;
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferStack.casHead(java.util.concurrent.SynchronousQueue$TransferStack$SNode, java.util.concurrent.SynchronousQueue$TransferStack$SNode):boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        boolean casHead(java.util.concurrent.SynchronousQueue.TransferStack.SNode r1, java.util.concurrent.SynchronousQueue.TransferStack.SNode r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferStack.casHead(java.util.concurrent.SynchronousQueue$TransferStack$SNode, java.util.concurrent.SynchronousQueue$TransferStack$SNode):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferStack.casHead(java.util.concurrent.SynchronousQueue$TransferStack$SNode, java.util.concurrent.SynchronousQueue$TransferStack$SNode):boolean");
        }

        TransferStack() {
        }

        static final class SNode {
            private static final VarHandle SMATCH;
            private static final VarHandle SNEXT;
            Object item;
            volatile SNode match;
            int mode;
            volatile SNode next;
            volatile Thread waiter;

            /*  JADX ERROR: Method load error
                jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferStack.SNode.casNext(java.util.concurrent.SynchronousQueue$TransferStack$SNode, java.util.concurrent.SynchronousQueue$TransferStack$SNode):boolean, dex: classes4.dex
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.ProcessClass.process(ProcessClass.java:36)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
                	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
                	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
                	... 7 more
                */
            boolean casNext(java.util.concurrent.SynchronousQueue.TransferStack.SNode r1, java.util.concurrent.SynchronousQueue.TransferStack.SNode r2) {
                /*
                // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferStack.SNode.casNext(java.util.concurrent.SynchronousQueue$TransferStack$SNode, java.util.concurrent.SynchronousQueue$TransferStack$SNode):boolean, dex: classes4.dex
                */
                throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferStack.SNode.casNext(java.util.concurrent.SynchronousQueue$TransferStack$SNode, java.util.concurrent.SynchronousQueue$TransferStack$SNode):boolean");
            }

            /*  JADX ERROR: Method load error
                jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferStack.SNode.tryCancel():void, dex: classes4.dex
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.ProcessClass.process(ProcessClass.java:36)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
                	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
                	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
                	... 7 more
                */
            void tryCancel() {
                /*
                // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferStack.SNode.tryCancel():void, dex: classes4.dex
                */
                throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferStack.SNode.tryCancel():void");
            }

            /*  JADX ERROR: Method load error
                jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferStack.SNode.tryMatch(java.util.concurrent.SynchronousQueue$TransferStack$SNode):boolean, dex: classes4.dex
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.ProcessClass.process(ProcessClass.java:36)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
                	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
                	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
                	... 7 more
                */
            boolean tryMatch(java.util.concurrent.SynchronousQueue.TransferStack.SNode r1) {
                /*
                // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferStack.SNode.tryMatch(java.util.concurrent.SynchronousQueue$TransferStack$SNode):boolean, dex: classes4.dex
                */
                throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferStack.SNode.tryMatch(java.util.concurrent.SynchronousQueue$TransferStack$SNode):boolean");
            }

            SNode(Object obj) {
                this.item = obj;
            }

            /* access modifiers changed from: package-private */
            public boolean isCancelled() {
                return this.match == this;
            }

            static {
                Class<SNode> cls = SNode.class;
                try {
                    MethodHandles.Lookup lookup = MethodHandles.lookup();
                    SMATCH = lookup.findVarHandle(cls, "match", cls);
                    SNEXT = lookup.findVarHandle(cls, "next", cls);
                } catch (ReflectiveOperationException e) {
                    throw new ExceptionInInitializerError((Throwable) e);
                }
            }
        }

        static SNode snode(SNode sNode, Object obj, SNode sNode2, int i) {
            if (sNode == null) {
                sNode = new SNode(obj);
            }
            sNode.mode = i;
            sNode.next = sNode2;
            return sNode;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
            r3 = r2.next;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
            if (r3 != null) goto L_0x0038;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0038, code lost:
            r4 = r3.next;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x003e, code lost:
            if (r3.tryMatch(r2) == false) goto L_0x004b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0040, code lost:
            casHead(r2, r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0043, code lost:
            if (r0 != 0) goto L_0x0048;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x004b, code lost:
            r2.casNext(r3, r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
            return r2.item;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
            return r3.item;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public E transfer(E r8, boolean r9, long r10) {
            /*
                r7 = this;
                if (r8 != 0) goto L_0x0004
                r0 = 0
                goto L_0x0005
            L_0x0004:
                r0 = 1
            L_0x0005:
                r1 = 0
            L_0x0006:
                r2 = r1
            L_0x0007:
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r3 = r7.head
                if (r3 == 0) goto L_0x0067
                int r4 = r3.mode
                if (r4 != r0) goto L_0x0010
                goto L_0x0067
            L_0x0010:
                int r4 = r3.mode
                boolean r4 = isFulfilling(r4)
                if (r4 != 0) goto L_0x004f
                boolean r4 = r3.isCancelled()
                if (r4 == 0) goto L_0x0024
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r4 = r3.next
                r7.casHead(r3, r4)
                goto L_0x0007
            L_0x0024:
                r4 = r0 | 2
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r2 = snode(r2, r8, r3, r4)
                boolean r3 = r7.casHead(r3, r2)
                if (r3 == 0) goto L_0x0007
            L_0x0030:
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r3 = r2.next
                if (r3 != 0) goto L_0x0038
                r7.casHead(r2, r1)
                goto L_0x0006
            L_0x0038:
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r4 = r3.next
                boolean r5 = r3.tryMatch(r2)
                if (r5 == 0) goto L_0x004b
                r7.casHead(r2, r4)
                if (r0 != 0) goto L_0x0048
                java.lang.Object r7 = r3.item
                goto L_0x004a
            L_0x0048:
                java.lang.Object r7 = r2.item
            L_0x004a:
                return r7
            L_0x004b:
                r2.casNext(r3, r4)
                goto L_0x0030
            L_0x004f:
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r4 = r3.next
                if (r4 != 0) goto L_0x0057
                r7.casHead(r3, r1)
                goto L_0x0007
            L_0x0057:
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r5 = r4.next
                boolean r6 = r4.tryMatch(r3)
                if (r6 == 0) goto L_0x0063
                r7.casHead(r3, r5)
                goto L_0x0007
            L_0x0063:
                r3.casNext(r4, r5)
                goto L_0x0007
            L_0x0067:
                if (r9 == 0) goto L_0x007e
                r4 = 0
                int r4 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
                if (r4 > 0) goto L_0x007e
                if (r3 == 0) goto L_0x007d
                boolean r4 = r3.isCancelled()
                if (r4 == 0) goto L_0x007d
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r4 = r3.next
                r7.casHead(r3, r4)
                goto L_0x0007
            L_0x007d:
                return r1
            L_0x007e:
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r2 = snode(r2, r8, r3, r0)
                boolean r3 = r7.casHead(r3, r2)
                if (r3 == 0) goto L_0x0007
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r8 = r7.awaitFulfill(r2, r9, r10)
                if (r8 != r2) goto L_0x0092
                r7.clean(r2)
                return r1
            L_0x0092:
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r9 = r7.head
                if (r9 == 0) goto L_0x009f
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r10 = r9.next
                if (r10 != r2) goto L_0x009f
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r10 = r2.next
                r7.casHead(r9, r10)
            L_0x009f:
                if (r0 != 0) goto L_0x00a4
                java.lang.Object r7 = r8.item
                goto L_0x00a6
            L_0x00a4:
                java.lang.Object r7 = r2.item
            L_0x00a6:
                return r7
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferStack.transfer(java.lang.Object, boolean, long):java.lang.Object");
        }

        /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
            	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
            */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0025  */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x002d  */
        java.util.concurrent.SynchronousQueue.TransferStack.SNode awaitFulfill(java.util.concurrent.SynchronousQueue.TransferStack.SNode r10, boolean r11, long r12) {
            /*
                r9 = this;
                r0 = 0
                if (r11 == 0) goto L_0x000a
                long r2 = java.lang.System.nanoTime()
                long r2 = r2 + r12
                goto L_0x000b
            L_0x000a:
                r2 = r0
            L_0x000b:
                java.lang.Thread r4 = java.lang.Thread.currentThread()
                boolean r5 = r9.shouldSpin(r10)
                r6 = 0
                if (r5 == 0) goto L_0x001e
                if (r11 == 0) goto L_0x001b
                int r5 = java.util.concurrent.SynchronousQueue.MAX_TIMED_SPINS
                goto L_0x001f
            L_0x001b:
                int r5 = java.util.concurrent.SynchronousQueue.MAX_UNTIMED_SPINS
                goto L_0x001f
            L_0x001e:
                r5 = r6
            L_0x001f:
                boolean r7 = r4.isInterrupted()
                if (r7 == 0) goto L_0x0028
                r10.tryCancel()
            L_0x0028:
                java.util.concurrent.SynchronousQueue$TransferStack$SNode r7 = r10.match
                if (r7 == 0) goto L_0x002d
                return r7
            L_0x002d:
                if (r11 == 0) goto L_0x003d
                long r12 = java.lang.System.nanoTime()
                long r12 = r2 - r12
                int r7 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r7 > 0) goto L_0x003d
                r10.tryCancel()
                goto L_0x001f
            L_0x003d:
                if (r5 <= 0) goto L_0x0048
                boolean r7 = r9.shouldSpin(r10)
                if (r7 == 0) goto L_0x001e
                int r5 = r5 + -1
                goto L_0x001f
            L_0x0048:
                java.lang.Thread r7 = r10.waiter
                if (r7 != 0) goto L_0x004f
                r10.waiter = r4
                goto L_0x001f
            L_0x004f:
                if (r11 != 0) goto L_0x0055
                java.util.concurrent.locks.LockSupport.park(r9)
                goto L_0x001f
            L_0x0055:
                r7 = 1000(0x3e8, double:4.94E-321)
                int r7 = (r12 > r7 ? 1 : (r12 == r7 ? 0 : -1))
                if (r7 <= 0) goto L_0x001f
                java.util.concurrent.locks.LockSupport.parkNanos(r9, r12)
                goto L_0x001f
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferStack.awaitFulfill(java.util.concurrent.SynchronousQueue$TransferStack$SNode, boolean, long):java.util.concurrent.SynchronousQueue$TransferStack$SNode");
        }

        /* access modifiers changed from: package-private */
        public boolean shouldSpin(SNode sNode) {
            SNode sNode2 = this.head;
            return sNode2 == sNode || sNode2 == null || isFulfilling(sNode2.mode);
        }

        /* access modifiers changed from: package-private */
        public void clean(SNode sNode) {
            SNode sNode2;
            sNode.item = null;
            sNode.waiter = null;
            SNode sNode3 = sNode.next;
            if (sNode3 != null && sNode3.isCancelled()) {
                sNode3 = sNode3.next;
            }
            while (true) {
                sNode2 = this.head;
                if (sNode2 != null && sNode2 != sNode3 && sNode2.isCancelled()) {
                    casHead(sNode2, sNode2.next);
                }
            }
            while (sNode2 != null && sNode2 != sNode3) {
                SNode sNode4 = sNode2.next;
                if (sNode4 == null || !sNode4.isCancelled()) {
                    sNode2 = sNode4;
                } else {
                    sNode2.casNext(sNode4, sNode4.next);
                }
            }
        }

        static {
            try {
                SHEAD = MethodHandles.lookup().findVarHandle(TransferStack.class, "head", SNode.class);
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError((Throwable) e);
            }
        }
    }

    static final class TransferQueue<E> extends Transferer<E> {
        private static final VarHandle QCLEANME;
        private static final VarHandle QHEAD;
        private static final VarHandle QTAIL;
        volatile transient QNode cleanMe;
        volatile transient QNode head;
        volatile transient QNode tail;

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.advanceHead(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        void advanceHead(java.util.concurrent.SynchronousQueue.TransferQueue.QNode r1, java.util.concurrent.SynchronousQueue.TransferQueue.QNode r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.advanceHead(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferQueue.advanceHead(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.advanceTail(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        void advanceTail(java.util.concurrent.SynchronousQueue.TransferQueue.QNode r1, java.util.concurrent.SynchronousQueue.TransferQueue.QNode r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.advanceTail(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferQueue.advanceTail(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.casCleanMe(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        boolean casCleanMe(java.util.concurrent.SynchronousQueue.TransferQueue.QNode r1, java.util.concurrent.SynchronousQueue.TransferQueue.QNode r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.casCleanMe(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferQueue.casCleanMe(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):boolean");
        }

        static final class QNode {
            private static final VarHandle QITEM;
            private static final VarHandle QNEXT;
            final boolean isData;
            volatile Object item;
            volatile QNode next;
            volatile Thread waiter;

            /*  JADX ERROR: Method load error
                jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.QNode.casItem(java.lang.Object, java.lang.Object):boolean, dex: classes4.dex
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.ProcessClass.process(ProcessClass.java:36)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
                	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
                	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
                	... 7 more
                */
            boolean casItem(java.lang.Object r1, java.lang.Object r2) {
                /*
                // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.QNode.casItem(java.lang.Object, java.lang.Object):boolean, dex: classes4.dex
                */
                throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferQueue.QNode.casItem(java.lang.Object, java.lang.Object):boolean");
            }

            /*  JADX ERROR: Method load error
                jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.QNode.casNext(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):boolean, dex: classes4.dex
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.ProcessClass.process(ProcessClass.java:36)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
                	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
                	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
                	... 7 more
                */
            boolean casNext(java.util.concurrent.SynchronousQueue.TransferQueue.QNode r1, java.util.concurrent.SynchronousQueue.TransferQueue.QNode r2) {
                /*
                // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.QNode.casNext(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):boolean, dex: classes4.dex
                */
                throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferQueue.QNode.casNext(java.util.concurrent.SynchronousQueue$TransferQueue$QNode, java.util.concurrent.SynchronousQueue$TransferQueue$QNode):boolean");
            }

            /*  JADX ERROR: Method load error
                jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.QNode.tryCancel(java.lang.Object):void, dex: classes4.dex
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
                	at jadx.core.ProcessClass.process(ProcessClass.java:36)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
                	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
                	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
                	... 7 more
                */
            void tryCancel(java.lang.Object r1) {
                /*
                // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SynchronousQueue.TransferQueue.QNode.tryCancel(java.lang.Object):void, dex: classes4.dex
                */
                throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SynchronousQueue.TransferQueue.QNode.tryCancel(java.lang.Object):void");
            }

            QNode(Object obj, boolean z) {
                this.item = obj;
                this.isData = z;
            }

            /* access modifiers changed from: package-private */
            public boolean isCancelled() {
                return this.item == this;
            }

            /* access modifiers changed from: package-private */
            public boolean isOffList() {
                return this.next == this;
            }

            static {
                Class<QNode> cls = QNode.class;
                try {
                    MethodHandles.Lookup lookup = MethodHandles.lookup();
                    QITEM = lookup.findVarHandle(cls, "item", Object.class);
                    QNEXT = lookup.findVarHandle(cls, "next", cls);
                } catch (ReflectiveOperationException e) {
                    throw new ExceptionInInitializerError((Throwable) e);
                }
            }
        }

        TransferQueue() {
            QNode qNode = new QNode((Object) null, false);
            this.head = qNode;
            this.tail = qNode;
        }

        /* access modifiers changed from: package-private */
        public E transfer(E e, boolean z, long j) {
            boolean z2 = e != null;
            E e2 = null;
            while (true) {
                QNode qNode = this.tail;
                QNode qNode2 = this.head;
                if (!(qNode == null || qNode2 == null)) {
                    if (qNode2 == qNode || qNode.isData == z2) {
                        QNode qNode3 = qNode.next;
                        if (qNode != this.tail) {
                            continue;
                        } else if (qNode3 != null) {
                            advanceTail(qNode, qNode3);
                        } else if (z && j <= 0) {
                            return null;
                        } else {
                            if (e2 == null) {
                                e2 = new QNode(e, z2);
                            }
                            E e3 = e2;
                            if (!qNode.casNext((QNode) null, e3)) {
                                e2 = e3;
                            } else {
                                advanceTail(qNode, e3);
                                E awaitFulfill = awaitFulfill(e3, e, z, j);
                                if (awaitFulfill == e3) {
                                    clean(qNode, e3);
                                    return null;
                                }
                                if (!e3.isOffList()) {
                                    advanceHead(qNode, e3);
                                    if (awaitFulfill != null) {
                                        e3.item = e3;
                                    }
                                    e3.waiter = null;
                                }
                                return awaitFulfill != null ? awaitFulfill : e;
                            }
                        }
                    } else {
                        E e4 = qNode2.next;
                        if (qNode == this.tail && e4 != null && qNode2 == this.head) {
                            E e5 = e4.item;
                            if (z2 == (e5 != null) || e5 == e4 || !e4.casItem(e5, e)) {
                                advanceHead(qNode2, e4);
                            } else {
                                advanceHead(qNode2, e4);
                                LockSupport.unpark(e4.waiter);
                                return e5 != null ? e5 : e;
                            }
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public Object awaitFulfill(QNode qNode, E e, boolean z, long j) {
            long nanoTime = z ? System.nanoTime() + j : 0;
            Thread currentThread = Thread.currentThread();
            int i = this.head.next == qNode ? z ? SynchronousQueue.MAX_TIMED_SPINS : SynchronousQueue.MAX_UNTIMED_SPINS : 0;
            while (true) {
                if (currentThread.isInterrupted()) {
                    qNode.tryCancel(e);
                }
                E e2 = qNode.item;
                if (e2 != e) {
                    return e2;
                }
                if (z) {
                    j = nanoTime - System.nanoTime();
                    if (j <= 0) {
                        qNode.tryCancel(e);
                    }
                }
                if (i > 0) {
                    i--;
                } else if (qNode.waiter == null) {
                    qNode.waiter = currentThread;
                } else if (!z) {
                    LockSupport.park(this);
                } else if (j > 1000) {
                    LockSupport.parkNanos(this, j);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void clean(QNode qNode, QNode qNode2) {
            QNode qNode3;
            QNode qNode4;
            qNode2.waiter = null;
            while (qNode.next == qNode2) {
                QNode qNode5 = this.head;
                QNode qNode6 = qNode5.next;
                if (qNode6 == null || !qNode6.isCancelled()) {
                    QNode qNode7 = this.tail;
                    if (qNode7 != qNode5) {
                        QNode qNode8 = qNode7.next;
                        if (qNode7 != this.tail) {
                            continue;
                        } else if (qNode8 != null) {
                            advanceTail(qNode7, qNode8);
                        } else if (qNode2 == qNode7 || ((qNode4 = qNode2.next) != qNode2 && !qNode.casNext(qNode2, qNode4))) {
                            QNode qNode9 = this.cleanMe;
                            if (qNode9 != null) {
                                QNode qNode10 = qNode9.next;
                                if (qNode10 == null || qNode10 == qNode9 || !qNode10.isCancelled() || !(qNode10 == qNode7 || (qNode3 = qNode10.next) == null || qNode3 == qNode10 || !qNode9.casNext(qNode10, qNode3))) {
                                    casCleanMe(qNode9, (QNode) null);
                                }
                                if (qNode9 == qNode) {
                                    return;
                                }
                            } else if (casCleanMe((QNode) null, qNode)) {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    advanceHead(qNode5, qNode6);
                }
            }
        }

        static {
            Class<TransferQueue> cls = TransferQueue.class;
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                QHEAD = lookup.findVarHandle(cls, "head", QNode.class);
                QTAIL = lookup.findVarHandle(cls, "tail", QNode.class);
                QCLEANME = lookup.findVarHandle(cls, "cleanMe", QNode.class);
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError((Throwable) e);
            }
        }
    }

    public SynchronousQueue() {
        this(false);
    }

    public SynchronousQueue(boolean z) {
        this.transferer = z ? new TransferQueue<>() : new TransferStack<>();
    }

    public void put(E e) throws InterruptedException {
        e.getClass();
        if (this.transferer.transfer(e, false, 0) == null) {
            Thread.interrupted();
            throw new InterruptedException();
        }
    }

    public boolean offer(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        e.getClass();
        if (this.transferer.transfer(e, true, timeUnit.toNanos(j)) != null) {
            return true;
        }
        if (!Thread.interrupted()) {
            return false;
        }
        throw new InterruptedException();
    }

    public boolean offer(E e) {
        e.getClass();
        return this.transferer.transfer(e, true, 0) != null;
    }

    public E take() throws InterruptedException {
        E transfer = this.transferer.transfer(null, false, 0);
        if (transfer != null) {
            return transfer;
        }
        Thread.interrupted();
        throw new InterruptedException();
    }

    public E poll(long j, TimeUnit timeUnit) throws InterruptedException {
        E transfer = this.transferer.transfer(null, true, timeUnit.toNanos(j));
        if (transfer != null || !Thread.interrupted()) {
            return transfer;
        }
        throw new InterruptedException();
    }

    public E poll() {
        return this.transferer.transfer(null, true, 0);
    }

    public boolean containsAll(Collection<?> collection) {
        return collection.isEmpty();
    }

    public Iterator<E> iterator() {
        return Collections.emptyIterator();
    }

    public Spliterator<E> spliterator() {
        return Spliterators.emptySpliterator();
    }

    public <T> T[] toArray(T[] tArr) {
        if (tArr.length > 0) {
            tArr[0] = null;
        }
        return tArr;
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

    static class WaitQueue implements Serializable {
        WaitQueue() {
        }
    }

    static class LifoWaitQueue extends WaitQueue {
        private static final long serialVersionUID = -3633113410248163686L;

        LifoWaitQueue() {
        }
    }

    static class FifoWaitQueue extends WaitQueue {
        private static final long serialVersionUID = -3623113410248163686L;

        FifoWaitQueue() {
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.transferer instanceof TransferQueue) {
            this.qlock = new ReentrantLock(true);
            this.waitingProducers = new FifoWaitQueue();
            this.waitingConsumers = new FifoWaitQueue();
        } else {
            this.qlock = new ReentrantLock();
            this.waitingProducers = new LifoWaitQueue();
            this.waitingConsumers = new LifoWaitQueue();
        }
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.waitingProducers instanceof FifoWaitQueue) {
            this.transferer = new TransferQueue();
        } else {
            this.transferer = new TransferStack();
        }
    }
}
