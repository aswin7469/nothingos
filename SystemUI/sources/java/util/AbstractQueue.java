package java.util;

public abstract class AbstractQueue<E> extends AbstractCollection<E> implements Queue<E> {
    protected AbstractQueue() {
    }

    public boolean add(E e) {
        if (offer(e)) {
            return true;
        }
        throw new IllegalStateException("Queue full");
    }

    public E remove() {
        E poll = poll();
        if (poll != null) {
            return poll;
        }
        throw new NoSuchElementException();
    }

    public E element() {
        E peek = peek();
        if (peek != null) {
            return peek;
        }
        throw new NoSuchElementException();
    }

    public void clear() {
        do {
        } while (poll() != null);
    }

    public boolean addAll(Collection<? extends E> collection) {
        collection.getClass();
        if (collection != this) {
            boolean z = false;
            for (Object add : collection) {
                if (add(add)) {
                    z = true;
                }
            }
            return z;
        }
        throw new IllegalArgumentException();
    }
}
