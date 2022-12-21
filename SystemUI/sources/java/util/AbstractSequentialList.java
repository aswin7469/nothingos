package java.util;

public abstract class AbstractSequentialList<E> extends AbstractList<E> {
    public abstract ListIterator<E> listIterator(int i);

    protected AbstractSequentialList() {
    }

    public E get(int i) {
        try {
            return listIterator(i).next();
        } catch (NoSuchElementException unused) {
            throw new IndexOutOfBoundsException("Index: " + i);
        }
    }

    public E set(int i, E e) {
        try {
            ListIterator listIterator = listIterator(i);
            E next = listIterator.next();
            listIterator.set(e);
            return next;
        } catch (NoSuchElementException unused) {
            throw new IndexOutOfBoundsException("Index: " + i);
        }
    }

    public void add(int i, E e) {
        try {
            listIterator(i).add(e);
        } catch (NoSuchElementException unused) {
            throw new IndexOutOfBoundsException("Index: " + i);
        }
    }

    public E remove(int i) {
        try {
            ListIterator listIterator = listIterator(i);
            E next = listIterator.next();
            listIterator.remove();
            return next;
        } catch (NoSuchElementException unused) {
            throw new IndexOutOfBoundsException("Index: " + i);
        }
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        try {
            ListIterator listIterator = listIterator(i);
            boolean z = false;
            for (Object add : collection) {
                listIterator.add(add);
                z = true;
            }
            return z;
        } catch (NoSuchElementException unused) {
            throw new IndexOutOfBoundsException("Index: " + i);
        }
    }

    public Iterator<E> iterator() {
        return listIterator();
    }
}
