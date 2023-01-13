package java.util.stream;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.StreamSpliterators;
import java.util.stream.Streams;

public interface IntStream extends BaseStream<Integer, IntStream> {
    boolean allMatch(IntPredicate intPredicate);

    boolean anyMatch(IntPredicate intPredicate);

    DoubleStream asDoubleStream();

    LongStream asLongStream();

    OptionalDouble average();

    Stream<Integer> boxed();

    <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> objIntConsumer, BiConsumer<R, R> biConsumer);

    long count();

    IntStream distinct();

    IntStream filter(IntPredicate intPredicate);

    OptionalInt findAny();

    OptionalInt findFirst();

    IntStream flatMap(IntFunction<? extends IntStream> intFunction);

    void forEach(IntConsumer intConsumer);

    void forEachOrdered(IntConsumer intConsumer);

    PrimitiveIterator.OfInt iterator();

    IntStream limit(long j);

    IntStream map(IntUnaryOperator intUnaryOperator);

    DoubleStream mapToDouble(IntToDoubleFunction intToDoubleFunction);

    LongStream mapToLong(IntToLongFunction intToLongFunction);

    <U> Stream<U> mapToObj(IntFunction<? extends U> intFunction);

    OptionalInt max();

    OptionalInt min();

    boolean noneMatch(IntPredicate intPredicate);

    IntStream parallel();

    IntStream peek(IntConsumer intConsumer);

    int reduce(int i, IntBinaryOperator intBinaryOperator);

    OptionalInt reduce(IntBinaryOperator intBinaryOperator);

    IntStream sequential();

    IntStream skip(long j);

    IntStream sorted();

    Spliterator.OfInt spliterator();

    int sum();

    IntSummaryStatistics summaryStatistics();

    int[] toArray();

    static Builder builder() {
        return new Streams.IntStreamBuilderImpl();
    }

    static IntStream empty() {
        return StreamSupport.intStream(Spliterators.emptyIntSpliterator(), false);
    }

    /* renamed from: of */
    static IntStream m1782of(int i) {
        return StreamSupport.intStream(new Streams.IntStreamBuilderImpl(i), false);
    }

    /* renamed from: of */
    static IntStream m1783of(int... iArr) {
        return Arrays.stream(iArr);
    }

    static IntStream iterate(int i, IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return StreamSupport.intStream(Spliterators.spliteratorUnknownSize((PrimitiveIterator.OfInt) new PrimitiveIterator.OfInt(i, intUnaryOperator) {

            /* renamed from: t */
            int f776t;
            final /* synthetic */ IntUnaryOperator val$f;
            final /* synthetic */ int val$seed;

            public boolean hasNext() {
                return true;
            }

            {
                this.val$seed = r1;
                this.val$f = r2;
                this.f776t = r1;
            }

            public int nextInt() {
                int i = this.f776t;
                this.f776t = this.val$f.applyAsInt(i);
                return i;
            }
        }, 1296), false);
    }

    static IntStream generate(IntSupplier intSupplier) {
        Objects.requireNonNull(intSupplier);
        return StreamSupport.intStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfInt(Long.MAX_VALUE, intSupplier), false);
    }

    static IntStream range(int i, int i2) {
        if (i >= i2) {
            return empty();
        }
        return StreamSupport.intStream(new Streams.RangeIntSpliterator(i, i2, false), false);
    }

    static IntStream rangeClosed(int i, int i2) {
        if (i > i2) {
            return empty();
        }
        return StreamSupport.intStream(new Streams.RangeIntSpliterator(i, i2, true), false);
    }

    static IntStream concat(IntStream intStream, IntStream intStream2) {
        Objects.requireNonNull(intStream);
        Objects.requireNonNull(intStream2);
        return (IntStream) StreamSupport.intStream(new Streams.ConcatSpliterator.OfInt(intStream.spliterator(), intStream2.spliterator()), intStream.isParallel() || intStream2.isParallel()).onClose(Streams.composedClose(intStream, intStream2));
    }

    public interface Builder extends IntConsumer {
        void accept(int i);

        IntStream build();

        Builder add(int i) {
            accept(i);
            return this;
        }
    }
}
