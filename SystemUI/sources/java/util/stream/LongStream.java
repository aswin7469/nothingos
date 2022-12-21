package java.util.stream;

import java.util.Arrays;
import java.util.LongSummaryStatistics;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.StreamSpliterators;
import java.util.stream.Streams;

public interface LongStream extends BaseStream<Long, LongStream> {
    boolean allMatch(LongPredicate longPredicate);

    boolean anyMatch(LongPredicate longPredicate);

    DoubleStream asDoubleStream();

    OptionalDouble average();

    Stream<Long> boxed();

    <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> objLongConsumer, BiConsumer<R, R> biConsumer);

    long count();

    LongStream distinct();

    LongStream filter(LongPredicate longPredicate);

    OptionalLong findAny();

    OptionalLong findFirst();

    LongStream flatMap(LongFunction<? extends LongStream> longFunction);

    void forEach(LongConsumer longConsumer);

    void forEachOrdered(LongConsumer longConsumer);

    PrimitiveIterator.OfLong iterator();

    LongStream limit(long j);

    LongStream map(LongUnaryOperator longUnaryOperator);

    DoubleStream mapToDouble(LongToDoubleFunction longToDoubleFunction);

    IntStream mapToInt(LongToIntFunction longToIntFunction);

    <U> Stream<U> mapToObj(LongFunction<? extends U> longFunction);

    OptionalLong max();

    OptionalLong min();

    boolean noneMatch(LongPredicate longPredicate);

    LongStream parallel();

    LongStream peek(LongConsumer longConsumer);

    long reduce(long j, LongBinaryOperator longBinaryOperator);

    OptionalLong reduce(LongBinaryOperator longBinaryOperator);

    LongStream sequential();

    LongStream skip(long j);

    LongStream sorted();

    Spliterator.OfLong spliterator();

    long sum();

    LongSummaryStatistics summaryStatistics();

    long[] toArray();

    static Builder builder() {
        return new Streams.LongStreamBuilderImpl();
    }

    static LongStream empty() {
        return StreamSupport.longStream(Spliterators.emptyLongSpliterator(), false);
    }

    /* renamed from: of */
    static LongStream m1778of(long j) {
        return StreamSupport.longStream(new Streams.LongStreamBuilderImpl(j), false);
    }

    /* renamed from: of */
    static LongStream m1779of(long... jArr) {
        return Arrays.stream(jArr);
    }

    static LongStream iterate(long j, LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return StreamSupport.longStream(Spliterators.spliteratorUnknownSize((PrimitiveIterator.OfLong) new PrimitiveIterator.OfLong(j, longUnaryOperator) {

            /* renamed from: t */
            long f779t;
            final /* synthetic */ LongUnaryOperator val$f;
            final /* synthetic */ long val$seed;

            public boolean hasNext() {
                return true;
            }

            {
                this.val$seed = r1;
                this.val$f = r3;
                this.f779t = r1;
            }

            public long nextLong() {
                long j = this.f779t;
                this.f779t = this.val$f.applyAsLong(j);
                return j;
            }
        }, 1296), false);
    }

    static LongStream generate(LongSupplier longSupplier) {
        Objects.requireNonNull(longSupplier);
        return StreamSupport.longStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfLong(Long.MAX_VALUE, longSupplier), false);
    }

    static LongStream range(long j, long j2) {
        if (j >= j2) {
            return empty();
        }
        long j3 = j2 - j;
        if (j3 >= 0) {
            return StreamSupport.longStream(new Streams.RangeLongSpliterator(j, j2, false), false);
        }
        long divideUnsigned = Long.divideUnsigned(j3, 2) + j + 1;
        return concat(range(j, divideUnsigned), range(divideUnsigned, j2));
    }

    static LongStream rangeClosed(long j, long j2) {
        if (j > j2) {
            return empty();
        }
        long j3 = j2 - j;
        if (j3 + 1 > 0) {
            return StreamSupport.longStream(new Streams.RangeLongSpliterator(j, j2, true), false);
        }
        long divideUnsigned = Long.divideUnsigned(j3, 2) + j + 1;
        return concat(range(j, divideUnsigned), rangeClosed(divideUnsigned, j2));
    }

    static LongStream concat(LongStream longStream, LongStream longStream2) {
        Objects.requireNonNull(longStream);
        Objects.requireNonNull(longStream2);
        return (LongStream) StreamSupport.longStream(new Streams.ConcatSpliterator.OfLong(longStream.spliterator(), longStream2.spliterator()), longStream.isParallel() || longStream2.isParallel()).onClose(Streams.composedClose(longStream, longStream2));
    }

    public interface Builder extends LongConsumer {
        void accept(long j);

        LongStream build();

        Builder add(long j) {
            accept(j);
            return this;
        }
    }
}
