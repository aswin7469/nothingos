package java.util.stream;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.StreamSpliterators;
import java.util.stream.Streams;

public interface DoubleStream extends BaseStream<Double, DoubleStream> {
    boolean allMatch(DoublePredicate doublePredicate);

    boolean anyMatch(DoublePredicate doublePredicate);

    OptionalDouble average();

    Stream<Double> boxed();

    <R> R collect(Supplier<R> supplier, ObjDoubleConsumer<R> objDoubleConsumer, BiConsumer<R, R> biConsumer);

    long count();

    DoubleStream distinct();

    DoubleStream filter(DoublePredicate doublePredicate);

    OptionalDouble findAny();

    OptionalDouble findFirst();

    DoubleStream flatMap(DoubleFunction<? extends DoubleStream> doubleFunction);

    void forEach(DoubleConsumer doubleConsumer);

    void forEachOrdered(DoubleConsumer doubleConsumer);

    PrimitiveIterator.OfDouble iterator();

    DoubleStream limit(long j);

    DoubleStream map(DoubleUnaryOperator doubleUnaryOperator);

    IntStream mapToInt(DoubleToIntFunction doubleToIntFunction);

    LongStream mapToLong(DoubleToLongFunction doubleToLongFunction);

    <U> Stream<U> mapToObj(DoubleFunction<? extends U> doubleFunction);

    OptionalDouble max();

    OptionalDouble min();

    boolean noneMatch(DoublePredicate doublePredicate);

    DoubleStream parallel();

    DoubleStream peek(DoubleConsumer doubleConsumer);

    double reduce(double d, DoubleBinaryOperator doubleBinaryOperator);

    OptionalDouble reduce(DoubleBinaryOperator doubleBinaryOperator);

    DoubleStream sequential();

    DoubleStream skip(long j);

    DoubleStream sorted();

    Spliterator.OfDouble spliterator();

    double sum();

    DoubleSummaryStatistics summaryStatistics();

    double[] toArray();

    static Builder builder() {
        return new Streams.DoubleStreamBuilderImpl();
    }

    static DoubleStream empty() {
        return StreamSupport.doubleStream(Spliterators.emptyDoubleSpliterator(), false);
    }

    /* renamed from: of */
    static DoubleStream m1774of(double d) {
        return StreamSupport.doubleStream(new Streams.DoubleStreamBuilderImpl(d), false);
    }

    /* renamed from: of */
    static DoubleStream m1775of(double... dArr) {
        return Arrays.stream(dArr);
    }

    static DoubleStream iterate(double d, DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return StreamSupport.doubleStream(Spliterators.spliteratorUnknownSize((PrimitiveIterator.OfDouble) new PrimitiveIterator.OfDouble(d, doubleUnaryOperator) {

            /* renamed from: t */
            double f776t;
            final /* synthetic */ DoubleUnaryOperator val$f;
            final /* synthetic */ double val$seed;

            public boolean hasNext() {
                return true;
            }

            {
                this.val$seed = r1;
                this.val$f = r3;
                this.f776t = r1;
            }

            public double nextDouble() {
                double d = this.f776t;
                this.f776t = this.val$f.applyAsDouble(d);
                return d;
            }
        }, 1296), false);
    }

    static DoubleStream generate(DoubleSupplier doubleSupplier) {
        Objects.requireNonNull(doubleSupplier);
        return StreamSupport.doubleStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfDouble(Long.MAX_VALUE, doubleSupplier), false);
    }

    static DoubleStream concat(DoubleStream doubleStream, DoubleStream doubleStream2) {
        Objects.requireNonNull(doubleStream);
        Objects.requireNonNull(doubleStream2);
        return (DoubleStream) StreamSupport.doubleStream(new Streams.ConcatSpliterator.OfDouble(doubleStream.spliterator(), doubleStream2.spliterator()), doubleStream.isParallel() || doubleStream2.isParallel()).onClose(Streams.composedClose(doubleStream, doubleStream2));
    }

    public interface Builder extends DoubleConsumer {
        void accept(double d);

        DoubleStream build();

        Builder add(double d) {
            accept(d);
            return this;
        }
    }
}
