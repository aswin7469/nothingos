package java.util;

public class Stack<E> extends Vector<E> {
    private static final long serialVersionUID = 1224463164541339165L;

    public E push(E e) {
        addElement(e);
        return e;
    }

    public synchronized E pop() {
        E peek;
        int size = size();
        peek = peek();
        removeElementAt(size - 1);
        return peek;
    }

    public synchronized E peek() {
        int size;
        size = size();
        if (size != 0) {
        } else {
            throw new EmptyStackException();
        }
        return elementAt(size - 1);
    }

    public boolean empty() {
        return size() == 0;
    }

    public synchronized int search(Object obj) {
        int lastIndexOf = lastIndexOf(obj);
        if (lastIndexOf < 0) {
            return -1;
        }
        return size() - lastIndexOf;
    }
}
