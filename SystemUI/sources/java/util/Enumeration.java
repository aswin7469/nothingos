package java.util;

public interface Enumeration<E> {
    boolean hasMoreElements();

    E nextElement();

    Iterator<E> asIterator() {
        return new Iterator<E>() {
            public boolean hasNext() {
                return Enumeration.this.hasMoreElements();
            }

            public E next() {
                return Enumeration.this.nextElement();
            }
        };
    }
}
