package java.util;

import java.p026io.Serializable;

class Comparators {
    private Comparators() {
        throw new AssertionError((Object) "no instances");
    }

    enum NaturalOrderComparator implements Comparator<Comparable<Object>> {
        INSTANCE;

        public int compare(Comparable<Object> comparable, Comparable<Object> comparable2) {
            return comparable.compareTo(comparable2);
        }

        public Comparator<Comparable<Object>> reversed() {
            return Comparator.reverseOrder();
        }
    }

    static final class NullComparator<T> implements Comparator<T>, Serializable {
        private static final long serialVersionUID = -7569533591570686392L;
        private final boolean nullFirst;
        private final Comparator<T> real;

        NullComparator(boolean z, Comparator<? super T> comparator) {
            this.nullFirst = z;
            this.real = comparator;
        }

        public int compare(T t, T t2) {
            if (t == null) {
                if (t2 == null) {
                    return 0;
                }
                return this.nullFirst ? -1 : 1;
            } else if (t2 != null) {
                Comparator<T> comparator = this.real;
                if (comparator == null) {
                    return 0;
                }
                return comparator.compare(t, t2);
            } else if (this.nullFirst) {
                return 1;
            } else {
                return -1;
            }
        }

        public Comparator<T> thenComparing(Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator);
            boolean z = this.nullFirst;
            Comparator comparator2 = this.real;
            if (comparator2 != null) {
                comparator = comparator2.thenComparing(comparator);
            }
            return new NullComparator(z, comparator);
        }

        public Comparator<T> reversed() {
            boolean z = !this.nullFirst;
            Comparator<T> comparator = this.real;
            return new NullComparator(z, comparator == null ? null : comparator.reversed());
        }
    }
}
