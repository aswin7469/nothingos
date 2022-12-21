package java.util;

import java.lang.reflect.Array;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.function.Consumer;

public class LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, Serializable {
    private static final long serialVersionUID = 876323262645176354L;
    transient Node<E> first;
    transient Node<E> last;
    transient int size;

    public LinkedList() {
        this.size = 0;
    }

    public LinkedList(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    private void linkFirst(E e) {
        Node<E> node = this.first;
        Node<E> node2 = new Node<>((Node) null, e, node);
        this.first = node2;
        if (node == null) {
            this.last = node2;
        } else {
            node.prev = node2;
        }
        this.size++;
        this.modCount++;
    }

    /* access modifiers changed from: package-private */
    public void linkLast(E e) {
        Node<E> node = this.last;
        Node<E> node2 = new Node<>(node, e, (Node<E>) null);
        this.last = node2;
        if (node == null) {
            this.first = node2;
        } else {
            node.next = node2;
        }
        this.size++;
        this.modCount++;
    }

    /* access modifiers changed from: package-private */
    public void linkBefore(E e, Node<E> node) {
        Node<E> node2 = node.prev;
        Node<E> node3 = new Node<>(node2, e, node);
        node.prev = node3;
        if (node2 == null) {
            this.first = node3;
        } else {
            node2.next = node3;
        }
        this.size++;
        this.modCount++;
    }

    private E unlinkFirst(Node<E> node) {
        E e = node.item;
        Node<E> node2 = node.next;
        node.item = null;
        node.next = null;
        this.first = node2;
        if (node2 == null) {
            this.last = null;
        } else {
            node2.prev = null;
        }
        this.size--;
        this.modCount++;
        return e;
    }

    private E unlinkLast(Node<E> node) {
        E e = node.item;
        Node<E> node2 = node.prev;
        node.item = null;
        node.prev = null;
        this.last = node2;
        if (node2 == null) {
            this.first = null;
        } else {
            node2.next = null;
        }
        this.size--;
        this.modCount++;
        return e;
    }

    /* access modifiers changed from: package-private */
    public E unlink(Node<E> node) {
        E e = node.item;
        Node<E> node2 = node.next;
        Node<E> node3 = node.prev;
        if (node3 == null) {
            this.first = node2;
        } else {
            node3.next = node2;
            node.prev = null;
        }
        if (node2 == null) {
            this.last = node3;
        } else {
            node2.prev = node3;
            node.next = null;
        }
        node.item = null;
        this.size--;
        this.modCount++;
        return e;
    }

    public E getFirst() {
        Node<E> node = this.first;
        if (node != null) {
            return node.item;
        }
        throw new NoSuchElementException();
    }

    public E getLast() {
        Node<E> node = this.last;
        if (node != null) {
            return node.item;
        }
        throw new NoSuchElementException();
    }

    public E removeFirst() {
        Node<E> node = this.first;
        if (node != null) {
            return unlinkFirst(node);
        }
        throw new NoSuchElementException();
    }

    public E removeLast() {
        Node<E> node = this.last;
        if (node != null) {
            return unlinkLast(node);
        }
        throw new NoSuchElementException();
    }

    public void addFirst(E e) {
        linkFirst(e);
    }

    public void addLast(E e) {
        linkLast(e);
    }

    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    public int size() {
        return this.size;
    }

    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    public boolean remove(Object obj) {
        if (obj == null) {
            for (Node<E> node = this.first; node != null; node = node.next) {
                if (node.item == null) {
                    unlink(node);
                    return true;
                }
            }
            return false;
        }
        for (Node<E> node2 = this.first; node2 != null; node2 = node2.next) {
            if (obj.equals(node2.item)) {
                unlink(node2);
                return true;
            }
        }
        return false;
    }

    public boolean addAll(Collection<? extends E> collection) {
        return addAll(this.size, collection);
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        Node<E> node;
        Node<E> node2;
        checkPositionIndex(i);
        Object[] array = collection.toArray();
        int length = array.length;
        int i2 = 0;
        if (length == 0) {
            return false;
        }
        if (i == this.size) {
            node = this.last;
            node2 = null;
        } else {
            Node<E> node3 = node(i);
            node2 = node3;
            node = node3.prev;
        }
        int length2 = array.length;
        while (i2 < length2) {
            Node<E> node4 = new Node<>(node, array[i2], (Node<E>) null);
            if (node == null) {
                this.first = node4;
            } else {
                node.next = node4;
            }
            i2++;
            node = node4;
        }
        if (node2 == null) {
            this.last = node;
        } else {
            node.next = node2;
            node2.prev = node;
        }
        this.size += length;
        this.modCount++;
        return true;
    }

    public void clear() {
        Node<E> node = this.first;
        while (node != null) {
            Node<E> node2 = node.next;
            node.item = null;
            node.next = null;
            node.prev = null;
            node = node2;
        }
        this.last = null;
        this.first = null;
        this.size = 0;
        this.modCount++;
    }

    public E get(int i) {
        checkElementIndex(i);
        return node(i).item;
    }

    public E set(int i, E e) {
        checkElementIndex(i);
        Node node = node(i);
        E e2 = node.item;
        node.item = e;
        return e2;
    }

    public void add(int i, E e) {
        checkPositionIndex(i);
        if (i == this.size) {
            linkLast(e);
        } else {
            linkBefore(e, node(i));
        }
    }

    public E remove(int i) {
        checkElementIndex(i);
        return unlink(node(i));
    }

    private boolean isElementIndex(int i) {
        return i >= 0 && i < this.size;
    }

    private boolean isPositionIndex(int i) {
        return i >= 0 && i <= this.size;
    }

    private String outOfBoundsMsg(int i) {
        return "Index: " + i + ", Size: " + this.size;
    }

    private void checkElementIndex(int i) {
        if (!isElementIndex(i)) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
        }
    }

    private void checkPositionIndex(int i) {
        if (!isPositionIndex(i)) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
        }
    }

    /* access modifiers changed from: package-private */
    public Node<E> node(int i) {
        int i2 = this.size;
        if (i < (i2 >> 1)) {
            Node<E> node = this.first;
            for (int i3 = 0; i3 < i; i3++) {
                node = node.next;
            }
            return node;
        }
        Node<E> node2 = this.last;
        for (int i4 = i2 - 1; i4 > i; i4--) {
            node2 = node2.prev;
        }
        return node2;
    }

    public int indexOf(Object obj) {
        int i = 0;
        if (obj == null) {
            for (Node<E> node = this.first; node != null; node = node.next) {
                if (node.item == null) {
                    return i;
                }
                i++;
            }
            return -1;
        }
        for (Node<E> node2 = this.first; node2 != null; node2 = node2.next) {
            if (obj.equals(node2.item)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int lastIndexOf(Object obj) {
        int i = this.size;
        if (obj == null) {
            for (Node<E> node = this.last; node != null; node = node.prev) {
                i--;
                if (node.item == null) {
                    return i;
                }
            }
        } else {
            for (Node<E> node2 = this.last; node2 != null; node2 = node2.prev) {
                i--;
                if (obj.equals(node2.item)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public E peek() {
        Node<E> node = this.first;
        if (node == null) {
            return null;
        }
        return node.item;
    }

    public E element() {
        return getFirst();
    }

    public E poll() {
        Node<E> node = this.first;
        if (node == null) {
            return null;
        }
        return unlinkFirst(node);
    }

    public E remove() {
        return removeFirst();
    }

    public boolean offer(E e) {
        return add(e);
    }

    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    public E peekFirst() {
        Node<E> node = this.first;
        if (node == null) {
            return null;
        }
        return node.item;
    }

    public E peekLast() {
        Node<E> node = this.last;
        if (node == null) {
            return null;
        }
        return node.item;
    }

    public E pollFirst() {
        Node<E> node = this.first;
        if (node == null) {
            return null;
        }
        return unlinkFirst(node);
    }

    public E pollLast() {
        Node<E> node = this.last;
        if (node == null) {
            return null;
        }
        return unlinkLast(node);
    }

    public void push(E e) {
        addFirst(e);
    }

    public E pop() {
        return removeFirst();
    }

    public boolean removeFirstOccurrence(Object obj) {
        return remove(obj);
    }

    public boolean removeLastOccurrence(Object obj) {
        if (obj == null) {
            for (Node<E> node = this.last; node != null; node = node.prev) {
                if (node.item == null) {
                    unlink(node);
                    return true;
                }
            }
            return false;
        }
        for (Node<E> node2 = this.last; node2 != null; node2 = node2.prev) {
            if (obj.equals(node2.item)) {
                unlink(node2);
                return true;
            }
        }
        return false;
    }

    public ListIterator<E> listIterator(int i) {
        checkPositionIndex(i);
        return new ListItr(i);
    }

    private class ListItr implements ListIterator<E> {
        private int expectedModCount;
        private Node<E> lastReturned;
        private Node<E> next;
        private int nextIndex;

        ListItr(int i) {
            this.expectedModCount = LinkedList.this.modCount;
            this.next = i == LinkedList.this.size ? null : LinkedList.this.node(i);
            this.nextIndex = i;
        }

        public boolean hasNext() {
            return this.nextIndex < LinkedList.this.size;
        }

        public E next() {
            checkForComodification();
            if (hasNext()) {
                Node<E> node = this.next;
                this.lastReturned = node;
                this.next = node.next;
                this.nextIndex++;
                return this.lastReturned.item;
            }
            throw new NoSuchElementException();
        }

        public boolean hasPrevious() {
            return this.nextIndex > 0;
        }

        public E previous() {
            checkForComodification();
            if (hasPrevious()) {
                Node<E> node = this.next;
                Node<E> node2 = node == null ? LinkedList.this.last : node.prev;
                this.next = node2;
                this.lastReturned = node2;
                this.nextIndex--;
                return node2.item;
            }
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return this.nextIndex;
        }

        public int previousIndex() {
            return this.nextIndex - 1;
        }

        public void remove() {
            checkForComodification();
            Node<E> node = this.lastReturned;
            if (node != null) {
                Node<E> node2 = node.next;
                LinkedList.this.unlink(this.lastReturned);
                if (this.next == this.lastReturned) {
                    this.next = node2;
                } else {
                    this.nextIndex--;
                }
                this.lastReturned = null;
                this.expectedModCount++;
                return;
            }
            throw new IllegalStateException();
        }

        public void set(E e) {
            if (this.lastReturned != null) {
                checkForComodification();
                this.lastReturned.item = e;
                return;
            }
            throw new IllegalStateException();
        }

        public void add(E e) {
            checkForComodification();
            this.lastReturned = null;
            Node<E> node = this.next;
            if (node == null) {
                LinkedList.this.linkLast(e);
            } else {
                LinkedList.this.linkBefore(e, node);
            }
            this.nextIndex++;
            this.expectedModCount++;
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            while (LinkedList.this.modCount == this.expectedModCount && this.nextIndex < LinkedList.this.size) {
                consumer.accept(this.next.item);
                Node<E> node = this.next;
                this.lastReturned = node;
                this.next = node.next;
                this.nextIndex++;
            }
            checkForComodification();
        }

        /* access modifiers changed from: package-private */
        public final void checkForComodification() {
            if (LinkedList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> node, E e, Node<E> node2) {
            this.item = e;
            this.next = node2;
            this.prev = node;
        }
    }

    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    private class DescendingIterator implements Iterator<E> {
        private final LinkedList<E>.ListItr itr;

        private DescendingIterator() {
            this.itr = new ListItr(LinkedList.this.size());
        }

        public boolean hasNext() {
            return this.itr.hasPrevious();
        }

        public E next() {
            return this.itr.previous();
        }

        public void remove() {
            this.itr.remove();
        }
    }

    private LinkedList<E> superClone() {
        try {
            return (LinkedList) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    public Object clone() {
        LinkedList superClone = superClone();
        superClone.last = null;
        superClone.first = null;
        superClone.size = 0;
        superClone.modCount = 0;
        for (Node<E> node = this.first; node != null; node = node.next) {
            superClone.add(node.item);
        }
        return superClone;
    }

    public Object[] toArray() {
        Object[] objArr = new Object[this.size];
        Node<E> node = this.first;
        int i = 0;
        while (node != null) {
            objArr[i] = node.item;
            node = node.next;
            i++;
        }
        return objArr;
    }

    public <T> T[] toArray(T[] tArr) {
        int length = tArr.length;
        T[] tArr2 = tArr;
        if (length < this.size) {
            tArr2 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.size);
        }
        Node<E> node = this.first;
        int i = 0;
        while (node != null) {
            tArr2[i] = node.item;
            node = node.next;
            i++;
        }
        int length2 = tArr2.length;
        int i2 = this.size;
        if (length2 > i2) {
            tArr2[i2] = null;
        }
        return tArr2;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.size);
        for (Node<E> node = this.first; node != null; node = node.next) {
            objectOutputStream.writeObject(node.item);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        for (int i = 0; i < readInt; i++) {
            linkLast(objectInputStream.readObject());
        }
    }

    public Spliterator<E> spliterator() {
        return new LLSpliterator(this, -1, 0);
    }

    static final class LLSpliterator<E> implements Spliterator<E> {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        int batch;
        Node<E> current;
        int est;
        int expectedModCount;
        final LinkedList<E> list;

        public int characteristics() {
            return 16464;
        }

        LLSpliterator(LinkedList<E> linkedList, int i, int i2) {
            this.list = linkedList;
            this.est = i;
            this.expectedModCount = i2;
        }

        /* access modifiers changed from: package-private */
        public final int getEst() {
            int i = this.est;
            if (i >= 0) {
                return i;
            }
            LinkedList<E> linkedList = this.list;
            if (linkedList == null) {
                this.est = 0;
                return 0;
            }
            this.expectedModCount = linkedList.modCount;
            this.current = linkedList.first;
            int i2 = linkedList.size;
            this.est = i2;
            return i2;
        }

        public long estimateSize() {
            return (long) getEst();
        }

        public Spliterator<E> trySplit() {
            Node<E> node;
            int i;
            int est2 = getEst();
            if (est2 <= 1 || (node = this.current) == null) {
                return null;
            }
            int i2 = this.batch + 1024;
            if (i2 > est2) {
                i2 = est2;
            }
            if (i2 > 33554432) {
                i2 = 33554432;
            }
            Object[] objArr = new Object[i2];
            int i3 = 0;
            while (true) {
                i = i3 + 1;
                objArr[i3] = node.item;
                node = node.next;
                if (node == null || i >= i2) {
                    this.current = node;
                    this.batch = i;
                    this.est = est2 - i;
                } else {
                    i3 = i;
                }
            }
            this.current = node;
            this.batch = i;
            this.est = est2 - i;
            return Spliterators.spliterator(objArr, 0, i, 16);
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            consumer.getClass();
            int est2 = getEst();
            if (est2 > 0 && (r1 = this.current) != null) {
                this.current = null;
                this.est = 0;
                do {
                    E e = r1.item;
                    Node<E> node = node.next;
                    consumer.accept(e);
                    if (node == null) {
                        break;
                    }
                    est2--;
                } while (est2 <= 0);
            }
            if (this.list.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            Node<E> node;
            consumer.getClass();
            if (getEst() <= 0 || (node = this.current) == null) {
                return false;
            }
            this.est--;
            E e = node.item;
            this.current = node.next;
            consumer.accept(e);
            if (this.list.modCount == this.expectedModCount) {
                return true;
            }
            throw new ConcurrentModificationException();
        }
    }
}
