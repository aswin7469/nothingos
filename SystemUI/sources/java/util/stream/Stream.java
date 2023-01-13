package java.util.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.StreamSpliterators;
import java.util.stream.Streams;

public interface Stream<T> extends BaseStream<T, Stream<T>> {
    boolean allMatch(Predicate<? super T> predicate);

    boolean anyMatch(Predicate<? super T> predicate);

    <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> biConsumer, BiConsumer<R, R> biConsumer2);

    <R, A> R collect(Collector<? super T, A, R> collector);

    long count();

    Stream<T> distinct();

    Stream<T> filter(Predicate<? super T> predicate);

    Optional<T> findAny();

    Optional<T> findFirst();

    <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> function);

    DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> function);

    IntStream flatMapToInt(Function<? super T, ? extends IntStream> function);

    LongStream flatMapToLong(Function<? super T, ? extends LongStream> function);

    void forEach(Consumer<? super T> consumer);

    void forEachOrdered(Consumer<? super T> consumer);

    Stream<T> limit(long j);

    <R> Stream<R> map(Function<? super T, ? extends R> function);

    DoubleStream mapToDouble(ToDoubleFunction<? super T> toDoubleFunction);

    IntStream mapToInt(ToIntFunction<? super T> toIntFunction);

    LongStream mapToLong(ToLongFunction<? super T> toLongFunction);

    Optional<T> max(Comparator<? super T> comparator);

    Optional<T> min(Comparator<? super T> comparator);

    boolean noneMatch(Predicate<? super T> predicate);

    Stream<T> peek(Consumer<? super T> consumer);

    <U> U reduce(U u, BiFunction<U, ? super T, U> biFunction, BinaryOperator<U> binaryOperator);

    T reduce(T t, BinaryOperator<T> binaryOperator);

    Optional<T> reduce(BinaryOperator<T> binaryOperator);

    Stream<T> skip(long j);

    Stream<T> sorted();

    Stream<T> sorted(Comparator<? super T> comparator);

    Object[] toArray();

    <A> A[] toArray(IntFunction<A[]> intFunction);

    static <T> Builder<T> builder() {
        return new Streams.StreamBuilderImpl();
    }

    static <T> Stream<T> empty() {
        return StreamSupport.stream(Spliterators.emptySpliterator(), false);
    }

    /* renamed from: of */
    static <T> Stream<T> m1786of(T t) {
        return StreamSupport.stream(new Streams.StreamBuilderImpl(t), false);
    }

    @SafeVarargs
    /* renamed from: of */
    static <T> Stream<T> m1787of(T... tArr) {
        return Arrays.stream(tArr);
    }

    static <T> Stream<T> iterate(final T t, final UnaryOperator<T> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() {

            /* renamed from: t */
            T f793t = Streams.NONE;

            public boolean hasNext() {
                return true;
            }

            public T next() {
                T apply = this.f793t == Streams.NONE ? Object.this : unaryOperator.apply(this.f793t);
                this.f793t = apply;
                return apply;
            }
        }, 1040), false);
    }

    static <T> Stream<T> generate(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return StreamSupport.stream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfRef(Long.MAX_VALUE, supplier), false);
    }

    static <T> Stream<T> concat(Stream<? extends T> stream, Stream<? extends T> stream2) {
        Objects.requireNonNull(stream);
        Objects.requireNonNull(stream2);
        return (Stream) StreamSupport.stream(new Streams.ConcatSpliterator.OfRef(stream.spliterator(), stream2.spliterator()), stream.isParallel() || stream2.isParallel()).onClose(Streams.composedClose(stream, stream2));
    }

    public interface Builder<T> extends Consumer<T> {
        void accept(T t);

        Stream<T> build();

        Builder<T> add(T t) {
            accept(t);
            return this;
        }
    }
}
