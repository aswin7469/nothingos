package java.util;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public interface Spliterator<T> {
    public static final int CONCURRENT = 4096;
    public static final int DISTINCT = 1;
    public static final int IMMUTABLE = 1024;
    public static final int NONNULL = 256;
    public static final int ORDERED = 16;
    public static final int SIZED = 64;
    public static final int SORTED = 4;
    public static final int SUBSIZED = 16384;

    int characteristics();

    long estimateSize();

    boolean tryAdvance(Consumer<? super T> consumer);

    Spliterator<T> trySplit();

    void forEachRemaining(Consumer<? super T> consumer) {
        do {
        } while (tryAdvance(consumer));
    }

    long getExactSizeIfKnown() {
        if ((characteristics() & 64) == 0) {
            return -1;
        }
        return estimateSize();
    }

    boolean hasCharacteristics(int i) {
        return (characteristics() & i) == i;
    }

    Comparator<? super T> getComparator() {
        throw new IllegalStateException();
    }

    public interface OfPrimitive<T, T_CONS, T_SPLITR extends OfPrimitive<T, T_CONS, T_SPLITR>> extends Spliterator<T> {
        boolean tryAdvance(T_CONS t_cons);

        T_SPLITR trySplit();

        void forEachRemaining(T_CONS t_cons) {
            do {
            } while (tryAdvance(t_cons));
        }
    }

    public interface OfInt extends OfPrimitive<Integer, IntConsumer, OfInt> {
        boolean tryAdvance(IntConsumer intConsumer);

        OfInt trySplit();

        void forEachRemaining(IntConsumer intConsumer) {
            do {
            } while (tryAdvance(intConsumer));
        }

        boolean tryAdvance(Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                return tryAdvance((IntConsumer) consumer);
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
            }
            Objects.requireNonNull(consumer);
            return tryAdvance((IntConsumer) new PrimitiveIterator$OfInt$$ExternalSyntheticLambda0(consumer));
        }

        void forEachRemaining(Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                forEachRemaining((IntConsumer) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfInt.forEachRemaining((IntConsumer) action::accept)");
            }
            Objects.requireNonNull(consumer);
            forEachRemaining((IntConsumer) new PrimitiveIterator$OfInt$$ExternalSyntheticLambda0(consumer));
        }
    }

    public interface OfLong extends OfPrimitive<Long, LongConsumer, OfLong> {
        boolean tryAdvance(LongConsumer longConsumer);

        OfLong trySplit();

        void forEachRemaining(LongConsumer longConsumer) {
            do {
            } while (tryAdvance(longConsumer));
        }

        boolean tryAdvance(Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                return tryAdvance((LongConsumer) consumer);
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfLong.tryAdvance((LongConsumer) action::accept)");
            }
            Objects.requireNonNull(consumer);
            return tryAdvance((LongConsumer) new PrimitiveIterator$OfLong$$ExternalSyntheticLambda0(consumer));
        }

        void forEachRemaining(Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                forEachRemaining((LongConsumer) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfLong.forEachRemaining((LongConsumer) action::accept)");
            }
            Objects.requireNonNull(consumer);
            forEachRemaining((LongConsumer) new PrimitiveIterator$OfLong$$ExternalSyntheticLambda0(consumer));
        }
    }

    public interface OfDouble extends OfPrimitive<Double, DoubleConsumer, OfDouble> {
        boolean tryAdvance(DoubleConsumer doubleConsumer);

        OfDouble trySplit();

        void forEachRemaining(DoubleConsumer doubleConsumer) {
            do {
            } while (tryAdvance(doubleConsumer));
        }

        boolean tryAdvance(Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                return tryAdvance((DoubleConsumer) consumer);
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfDouble.tryAdvance((DoubleConsumer) action::accept)");
            }
            Objects.requireNonNull(consumer);
            return tryAdvance((DoubleConsumer) new PrimitiveIterator$OfDouble$$ExternalSyntheticLambda0(consumer));
        }

        void forEachRemaining(Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                forEachRemaining((DoubleConsumer) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfDouble.forEachRemaining((DoubleConsumer) action::accept)");
            }
            Objects.requireNonNull(consumer);
            forEachRemaining((DoubleConsumer) new PrimitiveIterator$OfDouble$$ExternalSyntheticLambda0(consumer));
        }
    }
}
