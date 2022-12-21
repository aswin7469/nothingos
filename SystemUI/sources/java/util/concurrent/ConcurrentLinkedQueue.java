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
import java.util.Objects;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConcurrentLinkedQueue<E> extends AbstractQueue<E> implements Queue<E>, Serializable {
    private static final VarHandle HEAD;
    static final VarHandle ITEM;
    private static final int MAX_HOPS = 8;
    static final VarHandle NEXT;
    private static final VarHandle TAIL;
    private static final long serialVersionUID = 196745693267521676L;
    volatile transient Node<E> head;
    private volatile transient Node<E> tail;

    static /* synthetic */ boolean lambda$clear$2(Object obj) {
        return true;
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.skipDeadNodes(java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node):java.util.concurrent.ConcurrentLinkedQueue$Node<E>, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private java.util.concurrent.ConcurrentLinkedQueue.Node<E> skipDeadNodes(java.util.concurrent.ConcurrentLinkedQueue.Node<E> r1, java.util.concurrent.ConcurrentLinkedQueue.Node<E> r2, java.util.concurrent.ConcurrentLinkedQueue.Node<E> r3, java.util.concurrent.ConcurrentLinkedQueue.Node<E> r4) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.skipDeadNodes(java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node):java.util.concurrent.ConcurrentLinkedQueue$Node<E>, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.skipDeadNodes(java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node):java.util.concurrent.ConcurrentLinkedQueue$Node");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.tryCasSuccessor(java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private boolean tryCasSuccessor(java.util.concurrent.ConcurrentLinkedQueue.Node<E> r1, java.util.concurrent.ConcurrentLinkedQueue.Node<E> r2, java.util.concurrent.ConcurrentLinkedQueue.Node<E> r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.tryCasSuccessor(java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.tryCasSuccessor(java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.addAll(java.util.Collection):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public boolean addAll(java.util.Collection<? extends E> r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.addAll(java.util.Collection):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.addAll(java.util.Collection):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.offer(java.lang.Object):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public boolean offer(E r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.offer(java.lang.Object):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.offer(java.lang.Object):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.updateHead(java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final void updateHead(java.util.concurrent.ConcurrentLinkedQueue.Node<E> r1, java.util.concurrent.ConcurrentLinkedQueue.Node<E> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.updateHead(java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.updateHead(java.util.concurrent.ConcurrentLinkedQueue$Node, java.util.concurrent.ConcurrentLinkedQueue$Node):void");
    }

    static final class Node<E> {
        volatile E item;
        volatile Node<E> next;

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.Node.<init>(java.lang.Object):void, dex: classes4.dex
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
        Node(E r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.Node.<init>(java.lang.Object):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.Node.<init>(java.lang.Object):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.Node.appendRelaxed(java.util.concurrent.ConcurrentLinkedQueue$Node):void, dex: classes4.dex
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
        void appendRelaxed(java.util.concurrent.ConcurrentLinkedQueue.Node<E> r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.Node.appendRelaxed(java.util.concurrent.ConcurrentLinkedQueue$Node):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.Node.appendRelaxed(java.util.concurrent.ConcurrentLinkedQueue$Node):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.Node.casItem(java.lang.Object, java.lang.Object):boolean, dex: classes4.dex
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
        boolean casItem(E r1, E r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.Node.casItem(java.lang.Object, java.lang.Object):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.Node.casItem(java.lang.Object, java.lang.Object):boolean");
        }

        Node() {
        }
    }

    public ConcurrentLinkedQueue() {
        Node<E> node = new Node<>();
        this.tail = node;
        this.head = node;
    }

    public ConcurrentLinkedQueue(Collection<? extends E> collection) {
        Node<E> node = null;
        Node<E> node2 = null;
        for (Object requireNonNull : collection) {
            Node<E> node3 = new Node<>(Objects.requireNonNull(requireNonNull));
            if (node == null) {
                node = node3;
            } else {
                node2.appendRelaxed(node3);
            }
            node2 = node3;
        }
        if (node == null) {
            node = new Node<>();
            node2 = node;
        }
        this.head = node;
        this.tail = node2;
    }

    public boolean add(E e) {
        return offer(e);
    }

    /* access modifiers changed from: package-private */
    public final Node<E> succ(Node<E> node) {
        Node<E> node2 = node.next;
        return node == node2 ? this.head : node2;
    }

    public E poll() {
        while (true) {
            Node<E> node = this.head;
            Node<E> node2 = node;
            while (true) {
                E e = node2.item;
                if (e == null || !node2.casItem(e, null)) {
                    Node<E> node3 = node2.next;
                    if (node3 == null) {
                        updateHead(node, node2);
                        return null;
                    } else if (node2 != node3) {
                        node2 = node3;
                    }
                } else {
                    if (node2 != node) {
                        Node<E> node4 = node2.next;
                        if (node4 != null) {
                            node2 = node4;
                        }
                        updateHead(node, node2);
                    }
                    return e;
                }
            }
        }
    }

    public E peek() {
        Node<E> node;
        Node<E> node2;
        E e;
        Node<E> node3;
        loop0:
        while (true) {
            node = this.head;
            node2 = node;
            while (true) {
                e = node2.item;
                if (e != null || (node3 = node2.next) == null) {
                    updateHead(node, node2);
                } else if (node2 != node3) {
                    node2 = node3;
                }
            }
        }
        updateHead(node, node2);
        return e;
    }

    /* access modifiers changed from: package-private */
    public Node<E> first() {
        Node<E> node;
        Node<E> node2;
        boolean z;
        Node<E> node3;
        loop0:
        while (true) {
            node = this.head;
            node2 = node;
            while (true) {
                z = node2.item != null;
                if (z || (node3 = node2.next) == null) {
                    updateHead(node, node2);
                } else if (node2 != node3) {
                    node2 = node3;
                }
            }
        }
        updateHead(node, node2);
        if (z) {
            return node2;
        }
        return null;
    }

    public boolean isEmpty() {
        return first() == null;
    }

    public int size() {
        int i;
        loop0:
        while (true) {
            Node<E> first = first();
            i = 0;
            while (true) {
                if (first == null || (first.item != null && (i = i + 1) == Integer.MAX_VALUE)) {
                    return i;
                }
                Node<E> node = first.next;
                if (first != node) {
                    first = node;
                }
            }
        }
        return i;
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        while (true) {
            Node<E> node = this.head;
            Node<E> node2 = null;
            while (true) {
                if (node == null) {
                    return false;
                }
                Node<E> node3 = node.next;
                E e = node.item;
                if (e == null) {
                    Node<E> node4 = node;
                    while (true) {
                        if (node3 == null || node3.item != null) {
                            node2 = skipDeadNodes(node2, node, node4, node3);
                        } else if (node4 != node3) {
                            node4 = node3;
                            node3 = node3.next;
                        }
                    }
                    node2 = skipDeadNodes(node2, node, node4, node3);
                } else if (obj.equals(e)) {
                    return true;
                } else {
                    node2 = node;
                }
                node = node3;
            }
        }
    }

    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        while (true) {
            Node<E> node = this.head;
            Node<E> node2 = null;
            while (true) {
                if (node == null) {
                    return false;
                }
                Node<E> node3 = node.next;
                E e = node.item;
                if (e == null) {
                    Node<E> node4 = node;
                    while (true) {
                        if (node3 == null || node3.item != null) {
                            node2 = skipDeadNodes(node2, node, node4, node3);
                        } else if (node4 != node3) {
                            node4 = node3;
                            node3 = node3.next;
                        }
                    }
                    node2 = skipDeadNodes(node2, node, node4, node3);
                } else if (!obj.equals(e) || !node.casItem(e, null)) {
                    node2 = node;
                } else {
                    skipDeadNodes(node2, node, node, node3);
                    return true;
                }
                node = node3;
            }
        }
    }

    public String toString() {
        int i;
        int i2;
        String[] strArr = null;
        loop0:
        while (true) {
            Node<E> first = first();
            i = 0;
            i2 = 0;
            while (true) {
                if (first == null) {
                    break loop0;
                }
                E e = first.item;
                if (e != null) {
                    if (strArr == null) {
                        strArr = new String[4];
                    } else if (i == strArr.length) {
                        strArr = (String[]) Arrays.copyOf((T[]) strArr, i * 2);
                    }
                    String obj = e.toString();
                    strArr[i] = obj;
                    i2 += obj.length();
                    i++;
                }
                Node<E> node = first.next;
                if (first != node) {
                    first = node;
                }
            }
        }
        if (i == 0) {
            return "[]";
        }
        return Helpers.toString(strArr, i, i2);
    }

    private Object[] toArrayInternal(Object[] objArr) {
        int i;
        Object[] objArr2 = objArr;
        loop0:
        while (true) {
            Node<E> first = first();
            i = 0;
            while (true) {
                if (first == null) {
                    break loop0;
                }
                E e = first.item;
                if (e != null) {
                    if (objArr2 == null) {
                        objArr2 = new Object[4];
                    } else if (i == objArr2.length) {
                        objArr2 = Arrays.copyOf((T[]) objArr2, (i + 4) * 2);
                    }
                    objArr2[i] = e;
                    i++;
                }
                Node<E> node = first.next;
                if (first != node) {
                    first = node;
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

    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        private Node<E> lastRet;
        private E nextItem;
        private Node<E> nextNode;

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.Itr.next():E, dex: classes4.dex
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
        public E next() {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedQueue.Itr.next():E, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.Itr.next():java.lang.Object");
        }

        Itr() {
            Node<E> node;
            Node<E> node2;
            loop0:
            while (true) {
                node = ConcurrentLinkedQueue.this.head;
                node2 = node;
                while (true) {
                    E e = node2.item;
                    if (e != null) {
                        this.nextNode = node2;
                        this.nextItem = e;
                        break loop0;
                    }
                    Node<E> node3 = node2.next;
                    if (node3 == null) {
                        break loop0;
                    } else if (node2 != node3) {
                        node2 = node3;
                    }
                }
            }
            ConcurrentLinkedQueue.this.updateHead(node, node2);
        }

        public boolean hasNext() {
            return this.nextItem != null;
        }

        public void remove() {
            Node<E> node = this.lastRet;
            if (node != null) {
                node.item = null;
                this.lastRet = null;
                return;
            }
            throw new IllegalStateException();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Node first = first();
        while (first != null) {
            E e = first.item;
            if (e != null) {
                objectOutputStream.writeObject(e);
            }
            first = succ(first);
        }
        objectOutputStream.writeObject((Object) null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Node<E> node = null;
        Node<E> node2 = null;
        while (true) {
            Object readObject = objectInputStream.readObject();
            if (readObject == null) {
                break;
            }
            Node<E> node3 = new Node<>(readObject);
            if (node == null) {
                node = node3;
            } else {
                node2.appendRelaxed(node3);
            }
            node2 = node3;
        }
        if (node == null) {
            node = new Node<>();
            node2 = node;
        }
        this.head = node;
        this.tail = node2;
    }

    final class CLQSpliterator implements Spliterator<E> {
        static final int MAX_BATCH = 33554432;
        int batch;
        Node<E> current;
        boolean exhausted;

        public int characteristics() {
            return 4368;
        }

        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        CLQSpliterator() {
        }

        public Spliterator<E> trySplit() {
            Node<E> node;
            Node<E> current2 = current();
            if (current2 == null || (node = current2.next) == null) {
                return null;
            }
            int min = Math.min(this.batch + 1, 33554432);
            this.batch = min;
            Object[] objArr = null;
            int i = 0;
            do {
                E e = current2.item;
                if (e != null) {
                    if (objArr == null) {
                        objArr = new Object[min];
                    }
                    objArr[i] = e;
                    i++;
                }
                current2 = current2 == node ? ConcurrentLinkedQueue.this.first() : node;
                if (current2 == null || (node = current2.next) == null) {
                    setCurrent(current2);
                }
            } while (i < min);
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
                ConcurrentLinkedQueue.this.forEachFrom(consumer, current2);
            }
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            E e;
            Objects.requireNonNull(consumer);
            Node<E> current2 = current();
            if (current2 == null) {
                return false;
            }
            do {
                e = current2.item;
                Node<E> node = current2.next;
                current2 = current2 == node ? ConcurrentLinkedQueue.this.first() : node;
                if (e != null) {
                    break;
                }
            } while (current2 != null);
            setCurrent(current2);
            if (e == null) {
                return false;
            }
            consumer.accept(e);
            return true;
        }

        private void setCurrent(Node<E> node) {
            this.current = node;
            if (node == null) {
                this.exhausted = true;
            }
        }

        private Node<E> current() {
            Node<E> node = this.current;
            if (node != null || this.exhausted) {
                return node;
            }
            Node<E> first = ConcurrentLinkedQueue.this.first();
            setCurrent(first);
            return first;
        }
    }

    public Spliterator<E> spliterator() {
        return new CLQSpliterator();
    }

    public boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return bulkRemove(predicate);
    }

    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new ConcurrentLinkedQueue$$ExternalSyntheticLambda2(collection));
    }

    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new ConcurrentLinkedQueue$$ExternalSyntheticLambda1(collection));
    }

    static /* synthetic */ boolean lambda$retainAll$1(Collection collection, Object obj) {
        return !collection.contains(obj);
    }

    public void clear() {
        bulkRemove(new ConcurrentLinkedQueue$$ExternalSyntheticLambda0());
    }

    private boolean bulkRemove(Predicate<? super E> predicate) {
        boolean z = false;
        while (true) {
            Node<E> node = this.head;
            Node<E> node2 = node;
            Node<E> node3 = null;
            int i = 8;
            while (true) {
                if (node == null) {
                    return z;
                }
                Node<E> node4 = node.next;
                E e = node.item;
                boolean z2 = e != null;
                if (z2 && predicate.test(e)) {
                    if (node.casItem(e, null)) {
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
    }

    /* access modifiers changed from: package-private */
    public void forEachFrom(Consumer<? super E> consumer, Node<E> node) {
        while (true) {
            Node<E> node2 = null;
            while (node != null) {
                Node<E> node3 = node.next;
                E e = node.item;
                if (e != null) {
                    consumer.accept(e);
                } else {
                    Node<E> node4 = node;
                    while (node3 != null && node3.item == null) {
                        if (node4 == node3) {
                            node = this.head;
                        } else {
                            node4 = node3;
                            node3 = node3.next;
                        }
                    }
                    node = skipDeadNodes(node2, node, node4, node3);
                }
                node2 = node;
                node = node3;
            }
            return;
        }
    }

    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        forEachFrom(consumer, this.head);
    }

    static {
        Class<ConcurrentLinkedQueue> cls = ConcurrentLinkedQueue.class;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            HEAD = lookup.findVarHandle(cls, "head", Node.class);
            TAIL = lookup.findVarHandle(cls, "tail", Node.class);
            ITEM = lookup.findVarHandle(Node.class, "item", Object.class);
            NEXT = lookup.findVarHandle(Node.class, "next", Node.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }
}
