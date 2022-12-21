package java.util;

public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {
    protected AbstractSet() {
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Collection collection = (Collection) obj;
        if (collection.size() != size()) {
            return false;
        }
        try {
            return containsAll(collection);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    public int hashCode() {
        Iterator it = iterator();
        int i = 0;
        while (it.hasNext()) {
            Object next = it.next();
            if (next != null) {
                i += next.hashCode();
            }
        }
        return i;
    }

    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        boolean z = false;
        if (size() > collection.size()) {
            for (Object remove : collection) {
                z |= remove(remove);
            }
        } else {
            Iterator it = iterator();
            while (it.hasNext()) {
                if (collection.contains(it.next())) {
                    it.remove();
                    z = true;
                }
            }
        }
        return z;
    }
}
