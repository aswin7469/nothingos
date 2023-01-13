package java.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.function.Consumer;

public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    protected transient int modCount = 0;

    public abstract E get(int i);

    protected AbstractList() {
    }

    public boolean add(E e) {
        add(size(), e);
        return true;
    }

    public E set(int i, E e) {
        throw new UnsupportedOperationException();
    }

    public void add(int i, E e) {
        throw new UnsupportedOperationException();
    }

    public E remove(int i) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object obj) {
        ListIterator listIterator = listIterator();
        if (obj == null) {
            while (listIterator.hasNext()) {
                if (listIterator.next() == null) {
                    return listIterator.previousIndex();
                }
            }
            return -1;
        }
        while (listIterator.hasNext()) {
            if (obj.equals(listIterator.next())) {
                return listIterator.previousIndex();
            }
        }
        return -1;
    }

    public int lastIndexOf(Object obj) {
        ListIterator listIterator = listIterator(size());
        if (obj == null) {
            while (listIterator.hasPrevious()) {
                if (listIterator.previous() == null) {
                    return listIterator.nextIndex();
                }
            }
            return -1;
        }
        while (listIterator.hasPrevious()) {
            if (obj.equals(listIterator.previous())) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }

    public void clear() {
        removeRange(0, size());
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        rangeCheckForAdd(i);
        boolean z = false;
        for (Object add : collection) {
            add(i, add);
            z = true;
            i++;
        }
        return z;
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    public ListIterator<E> listIterator(int i) {
        rangeCheckForAdd(i);
        return new ListItr(i);
    }

    private class Itr implements Iterator<E> {
        int cursor;
        int expectedModCount;
        int lastRet;

        private Itr() {
            this.cursor = 0;
            this.lastRet = -1;
            this.expectedModCount = AbstractList.this.modCount;
        }

        public boolean hasNext() {
            return this.cursor != AbstractList.this.size();
        }

        public E next() {
            checkForComodification();
            try {
                int i = this.cursor;
                E e = AbstractList.this.get(i);
                this.lastRet = i;
                this.cursor = i + 1;
                return e;
            } catch (IndexOutOfBoundsException unused) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (this.lastRet >= 0) {
                checkForComodification();
                try {
                    AbstractList.this.remove(this.lastRet);
                    int i = this.lastRet;
                    int i2 = this.cursor;
                    if (i < i2) {
                        this.cursor = i2 - 1;
                    }
                    this.lastRet = -1;
                    this.expectedModCount = AbstractList.this.modCount;
                } catch (IndexOutOfBoundsException unused) {
                    throw new ConcurrentModificationException();
                }
            } else {
                throw new IllegalStateException();
            }
        }

        /* access modifiers changed from: package-private */
        public final void checkForComodification() {
            if (AbstractList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class ListItr extends AbstractList<E>.Itr implements ListIterator<E> {
        ListItr(int i) {
            super();
            this.cursor = i;
        }

        public boolean hasPrevious() {
            return this.cursor != 0;
        }

        public E previous() {
            checkForComodification();
            try {
                int i = this.cursor - 1;
                E e = AbstractList.this.get(i);
                this.cursor = i;
                this.lastRet = i;
                return e;
            } catch (IndexOutOfBoundsException unused) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public int nextIndex() {
            return this.cursor;
        }

        public int previousIndex() {
            return this.cursor - 1;
        }

        public void set(E e) {
            if (this.lastRet >= 0) {
                checkForComodification();
                try {
                    AbstractList.this.set(this.lastRet, e);
                    this.expectedModCount = AbstractList.this.modCount;
                } catch (IndexOutOfBoundsException unused) {
                    throw new ConcurrentModificationException();
                }
            } else {
                throw new IllegalStateException();
            }
        }

        public void add(E e) {
            checkForComodification();
            try {
                int i = this.cursor;
                AbstractList.this.add(i, e);
                this.lastRet = -1;
                this.cursor = i + 1;
                this.expectedModCount = AbstractList.this.modCount;
            } catch (IndexOutOfBoundsException unused) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public List<E> subList(int i, int i2) {
        subListRangeCheck(i, i2, size());
        if (this instanceof RandomAccess) {
            return new RandomAccessSubList(this, i, i2);
        }
        return new SubList(this, i, i2);
    }

    static void subListRangeCheck(int i, int i2, int i3) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("fromIndex = " + i);
        } else if (i2 > i3) {
            throw new IndexOutOfBoundsException("toIndex = " + i2);
        } else if (i > i2) {
            throw new IllegalArgumentException("fromIndex(" + i + ") > toIndex(" + i2 + NavigationBarInflaterView.KEY_CODE_END);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        ListIterator listIterator = listIterator();
        ListIterator listIterator2 = ((List) obj).listIterator();
        while (listIterator.hasNext() && listIterator2.hasNext()) {
            Object next = listIterator.next();
            Object next2 = listIterator2.next();
            if (next == null) {
                if (next2 == null) {
                }
            } else if (!next.equals(next2)) {
            }
            return false;
        }
        if (listIterator.hasNext() || listIterator2.hasNext()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        Iterator it = iterator();
        int i2 = 1;
        while (it.hasNext()) {
            Object next = it.next();
            int i3 = i2 * 31;
            if (next == null) {
                i = 0;
            } else {
                i = next.hashCode();
            }
            i2 = i3 + i;
        }
        return i2;
    }

    /* access modifiers changed from: protected */
    public void removeRange(int i, int i2) {
        ListIterator listIterator = listIterator(i);
        int i3 = i2 - i;
        for (int i4 = 0; i4 < i3; i4++) {
            listIterator.next();
            listIterator.remove();
        }
    }

    private void rangeCheckForAdd(int i) {
        if (i < 0 || i > size()) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
        }
    }

    private String outOfBoundsMsg(int i) {
        return "Index: " + i + ", Size: " + size();
    }

    static final class RandomAccessSpliterator<E> implements Spliterator<E> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final AbstractList<E> alist;
        private int expectedModCount;
        private int fence;
        private int index;
        private final List<E> list;

        public int characteristics() {
            return 16464;
        }

        static {
            Class<AbstractList> cls = AbstractList.class;
        }

        RandomAccessSpliterator(List<E> list2) {
            this.list = list2;
            int i = 0;
            this.index = 0;
            this.fence = -1;
            AbstractList<E> abstractList = list2 instanceof AbstractList ? (AbstractList) list2 : null;
            this.alist = abstractList;
            this.expectedModCount = abstractList != null ? abstractList.modCount : i;
        }

        private RandomAccessSpliterator(RandomAccessSpliterator<E> randomAccessSpliterator, int i, int i2) {
            this.list = randomAccessSpliterator.list;
            this.index = i;
            this.fence = i2;
            this.alist = randomAccessSpliterator.alist;
            this.expectedModCount = randomAccessSpliterator.expectedModCount;
        }

        private int getFence() {
            List<E> list2 = this.list;
            int i = this.fence;
            if (i >= 0) {
                return i;
            }
            AbstractList<E> abstractList = this.alist;
            if (abstractList != null) {
                this.expectedModCount = abstractList.modCount;
            }
            int size = list2.size();
            this.fence = size;
            return size;
        }

        public Spliterator<E> trySplit() {
            int fence2 = getFence();
            int i = this.index;
            int i2 = (fence2 + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            this.index = i2;
            return new RandomAccessSpliterator(this, i, i2);
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            consumer.getClass();
            int fence2 = getFence();
            int i = this.index;
            if (i >= fence2) {
                return false;
            }
            this.index = i + 1;
            consumer.accept(get(this.list, i));
            checkAbstractListModCount(this.alist, this.expectedModCount);
            return true;
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            List<E> list2 = this.list;
            int fence2 = getFence();
            this.index = fence2;
            for (int i = this.index; i < fence2; i++) {
                consumer.accept(get(list2, i));
            }
            checkAbstractListModCount(this.alist, this.expectedModCount);
        }

        public long estimateSize() {
            return (long) (getFence() - this.index);
        }

        private static <E> E get(List<E> list2, int i) {
            try {
                return list2.get(i);
            } catch (IndexOutOfBoundsException unused) {
                throw new ConcurrentModificationException();
            }
        }

        static void checkAbstractListModCount(AbstractList<?> abstractList, int i) {
            if (abstractList != null && abstractList.modCount != i) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private static class SubList<E> extends AbstractList<E> {
        /* access modifiers changed from: private */
        public final int offset;
        private final SubList<E> parent;
        /* access modifiers changed from: private */
        public final AbstractList<E> root;
        protected int size;

        public SubList(AbstractList<E> abstractList, int i, int i2) {
            this.root = abstractList;
            this.parent = null;
            this.offset = i;
            this.size = i2 - i;
            this.modCount = abstractList.modCount;
        }

        protected SubList(SubList<E> subList, int i, int i2) {
            AbstractList<E> abstractList = subList.root;
            this.root = abstractList;
            this.parent = subList;
            this.offset = subList.offset + i;
            this.size = i2 - i;
            this.modCount = abstractList.modCount;
        }

        public E set(int i, E e) {
            Objects.checkIndex(i, this.size);
            checkForComodification();
            return this.root.set(this.offset + i, e);
        }

        public E get(int i) {
            Objects.checkIndex(i, this.size);
            checkForComodification();
            return this.root.get(this.offset + i);
        }

        public int size() {
            checkForComodification();
            return this.size;
        }

        public void add(int i, E e) {
            rangeCheckForAdd(i);
            checkForComodification();
            this.root.add(this.offset + i, e);
            updateSizeAndModCount(1);
        }

        public E remove(int i) {
            Objects.checkIndex(i, this.size);
            checkForComodification();
            E remove = this.root.remove(this.offset + i);
            updateSizeAndModCount(-1);
            return remove;
        }

        /* access modifiers changed from: protected */
        public void removeRange(int i, int i2) {
            checkForComodification();
            AbstractList<E> abstractList = this.root;
            int i3 = this.offset;
            abstractList.removeRange(i3 + i, i3 + i2);
            updateSizeAndModCount(i - i2);
        }

        public boolean addAll(Collection<? extends E> collection) {
            return addAll(this.size, collection);
        }

        public boolean addAll(int i, Collection<? extends E> collection) {
            rangeCheckForAdd(i);
            int size2 = collection.size();
            if (size2 == 0) {
                return false;
            }
            checkForComodification();
            this.root.addAll(this.offset + i, collection);
            updateSizeAndModCount(size2);
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(int i) {
            checkForComodification();
            rangeCheckForAdd(i);
            return new ListIterator<E>(i) {

                /* renamed from: i */
                private final ListIterator<E> f584i;
                final /* synthetic */ int val$index;

                {
                    this.val$index = r3;
                    this.f584i = SubList.this.root.listIterator(SubList.this.offset + r3);
                }

                public boolean hasNext() {
                    return nextIndex() < SubList.this.size;
                }

                public E next() {
                    if (hasNext()) {
                        return this.f584i.next();
                    }
                    throw new NoSuchElementException();
                }

                public boolean hasPrevious() {
                    return previousIndex() >= 0;
                }

                public E previous() {
                    if (hasPrevious()) {
                        return this.f584i.previous();
                    }
                    throw new NoSuchElementException();
                }

                public int nextIndex() {
                    return this.f584i.nextIndex() - SubList.this.offset;
                }

                public int previousIndex() {
                    return this.f584i.previousIndex() - SubList.this.offset;
                }

                public void remove() {
                    this.f584i.remove();
                    SubList.this.updateSizeAndModCount(-1);
                }

                public void set(E e) {
                    this.f584i.set(e);
                }

                public void add(E e) {
                    this.f584i.add(e);
                    SubList.this.updateSizeAndModCount(1);
                }
            };
        }

        public List<E> subList(int i, int i2) {
            subListRangeCheck(i, i2, this.size);
            return new SubList(this, i, i2);
        }

        private void rangeCheckForAdd(int i) {
            if (i < 0 || i > this.size) {
                throw new IndexOutOfBoundsException(outOfBoundsMsg(i));
            }
        }

        private String outOfBoundsMsg(int i) {
            return "Index: " + i + ", Size: " + this.size;
        }

        private void checkForComodification() {
            if (this.root.modCount != this.modCount) {
                throw new ConcurrentModificationException();
            }
        }

        /* access modifiers changed from: private */
        public void updateSizeAndModCount(int i) {
            SubList subList = this;
            do {
                subList.size += i;
                subList.modCount = this.root.modCount;
                subList = subList.parent;
            } while (subList != null);
        }
    }

    private static class RandomAccessSubList<E> extends SubList<E> implements RandomAccess {
        RandomAccessSubList(AbstractList<E> abstractList, int i, int i2) {
            super(abstractList, i, i2);
        }

        RandomAccessSubList(RandomAccessSubList<E> randomAccessSubList, int i, int i2) {
            super(randomAccessSubList, i, i2);
        }

        public List<E> subList(int i, int i2) {
            subListRangeCheck(i, i2, this.size);
            return new RandomAccessSubList(this, i, i2);
        }
    }
}
