package java.util;

import java.p026io.Serializable;

public class LinkedHashSet<E> extends HashSet<E> implements Set<E>, Cloneable, Serializable {
    private static final long serialVersionUID = -2851667679971038690L;

    public LinkedHashSet(int i, float f) {
        super(i, f, true);
    }

    public LinkedHashSet(int i) {
        super(i, 0.75f, true);
    }

    public LinkedHashSet() {
        super(16, 0.75f, true);
    }

    public LinkedHashSet(Collection<? extends E> collection) {
        super(Math.max(collection.size() * 2, 11), 0.75f, true);
        addAll(collection);
    }

    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 17);
    }
}
