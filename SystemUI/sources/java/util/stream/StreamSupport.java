package java.util.stream;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.DoublePipeline;
import java.util.stream.IntPipeline;
import java.util.stream.LongPipeline;
import java.util.stream.ReferencePipeline;

public final class StreamSupport {
    private StreamSupport() {
    }

    public static <T> Stream<T> stream(Spliterator<T> spliterator, boolean z) {
        Objects.requireNonNull(spliterator);
        return new ReferencePipeline.Head((Spliterator<?>) spliterator, StreamOpFlag.fromCharacteristics((Spliterator<?>) spliterator), z);
    }

    public static <T> Stream<T> stream(Supplier<? extends Spliterator<T>> supplier, int i, boolean z) {
        Objects.requireNonNull(supplier);
        return new ReferencePipeline.Head((Supplier<? extends Spliterator<?>>) supplier, StreamOpFlag.fromCharacteristics(i), z);
    }

    public static IntStream intStream(Spliterator.OfInt ofInt, boolean z) {
        return new IntPipeline.Head((Spliterator<Integer>) ofInt, StreamOpFlag.fromCharacteristics((Spliterator<?>) ofInt), z);
    }

    public static IntStream intStream(Supplier<? extends Spliterator.OfInt> supplier, int i, boolean z) {
        return new IntPipeline.Head((Supplier<? extends Spliterator<Integer>>) supplier, StreamOpFlag.fromCharacteristics(i), z);
    }

    public static LongStream longStream(Spliterator.OfLong ofLong, boolean z) {
        return new LongPipeline.Head((Spliterator<Long>) ofLong, StreamOpFlag.fromCharacteristics((Spliterator<?>) ofLong), z);
    }

    public static LongStream longStream(Supplier<? extends Spliterator.OfLong> supplier, int i, boolean z) {
        return new LongPipeline.Head((Supplier<? extends Spliterator<Long>>) supplier, StreamOpFlag.fromCharacteristics(i), z);
    }

    public static DoubleStream doubleStream(Spliterator.OfDouble ofDouble, boolean z) {
        return new DoublePipeline.Head((Spliterator<Double>) ofDouble, StreamOpFlag.fromCharacteristics((Spliterator<?>) ofDouble), z);
    }

    public static DoubleStream doubleStream(Supplier<? extends Spliterator.OfDouble> supplier, int i, boolean z) {
        return new DoublePipeline.Head((Supplier<? extends Spliterator<Double>>) supplier, StreamOpFlag.fromCharacteristics(i), z);
    }
}
