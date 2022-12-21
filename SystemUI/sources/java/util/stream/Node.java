package java.util.stream;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.stream.Sink;

public interface Node<T> {

    public interface Builder<T> extends Sink<T> {

        public interface OfDouble extends Builder<Double>, Sink.OfDouble {
            OfDouble build();
        }

        public interface OfInt extends Builder<Integer>, Sink.OfInt {
            OfInt build();
        }

        public interface OfLong extends Builder<Long>, Sink.OfLong {
            OfLong build();
        }

        Node<T> build();
    }

    static /* synthetic */ void lambda$truncate$0(Object obj) {
    }

    T[] asArray(IntFunction<T[]> intFunction);

    void copyInto(T[] tArr, int i);

    long count();

    void forEach(Consumer<? super T> consumer);

    int getChildCount() {
        return 0;
    }

    Spliterator<T> spliterator();

    Node<T> getChild(int i) {
        throw new IndexOutOfBoundsException();
    }

    Node<T> truncate(long j, long j2, IntFunction<T[]> intFunction) {
        if (j == 0 && j2 == count()) {
            return this;
        }
        Spliterator spliterator = spliterator();
        long j3 = j2 - j;
        Builder<T> builder = Nodes.builder(j3, intFunction);
        builder.begin(j3);
        for (int i = 0; ((long) i) < j && spliterator.tryAdvance(new Node$$ExternalSyntheticLambda0()); i++) {
        }
        for (int i2 = 0; ((long) i2) < j3 && spliterator.tryAdvance(builder); i2++) {
        }
        builder.end();
        return builder.build();
    }

    StreamShape getShape() {
        return StreamShape.REFERENCE;
    }

    public interface OfPrimitive<T, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, T_NODE extends OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>> extends Node<T> {
        T_ARR asPrimitiveArray();

        void copyInto(T_ARR t_arr, int i);

        void forEach(T_CONS t_cons);

        T_ARR newArray(int i);

        T_SPLITR spliterator();

        T_NODE truncate(long j, long j2, IntFunction<T[]> intFunction);

        T_NODE getChild(int i) {
            throw new IndexOutOfBoundsException();
        }

        T[] asArray(IntFunction<T[]> intFunction) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfPrimitive.asArray");
            }
            if (count() < 2147483639) {
                T[] tArr = (Object[]) intFunction.apply((int) count());
                copyInto(tArr, 0);
                return tArr;
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public interface OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, OfInt> {
        static /* synthetic */ void lambda$truncate$0(int i) {
        }

        void forEach(Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                forEach((IntConsumer) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfInt.forEachRemaining(Consumer)");
            }
            ((Spliterator.OfInt) spliterator()).forEachRemaining(consumer);
        }

        void copyInto(Integer[] numArr, int i) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfInt.copyInto(Integer[], int)");
            }
            int[] iArr = (int[]) asPrimitiveArray();
            for (int i2 = 0; i2 < iArr.length; i2++) {
                numArr[i + i2] = Integer.valueOf(iArr[i2]);
            }
        }

        OfInt truncate(long j, long j2, IntFunction<Integer[]> intFunction) {
            if (j == 0 && j2 == count()) {
                return this;
            }
            long j3 = j2 - j;
            Spliterator.OfInt ofInt = (Spliterator.OfInt) spliterator();
            Builder.OfInt intBuilder = Nodes.intBuilder(j3);
            intBuilder.begin(j3);
            for (int i = 0; ((long) i) < j && ofInt.tryAdvance((IntConsumer) new Node$OfInt$$ExternalSyntheticLambda0()); i++) {
            }
            for (int i2 = 0; ((long) i2) < j3 && ofInt.tryAdvance((IntConsumer) intBuilder); i2++) {
            }
            intBuilder.end();
            return intBuilder.build();
        }

        int[] newArray(int i) {
            return new int[i];
        }

        StreamShape getShape() {
            return StreamShape.INT_VALUE;
        }
    }

    public interface OfLong extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, OfLong> {
        static /* synthetic */ void lambda$truncate$0(long j) {
        }

        void forEach(Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                forEach((LongConsumer) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            }
            ((Spliterator.OfLong) spliterator()).forEachRemaining(consumer);
        }

        void copyInto(Long[] lArr, int i) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfInt.copyInto(Long[], int)");
            }
            long[] jArr = (long[]) asPrimitiveArray();
            for (int i2 = 0; i2 < jArr.length; i2++) {
                lArr[i + i2] = Long.valueOf(jArr[i2]);
            }
        }

        OfLong truncate(long j, long j2, IntFunction<Long[]> intFunction) {
            if (j == 0 && j2 == count()) {
                return this;
            }
            long j3 = j2 - j;
            Spliterator.OfLong ofLong = (Spliterator.OfLong) spliterator();
            Builder.OfLong longBuilder = Nodes.longBuilder(j3);
            longBuilder.begin(j3);
            for (int i = 0; ((long) i) < j && ofLong.tryAdvance((LongConsumer) new Node$OfLong$$ExternalSyntheticLambda0()); i++) {
            }
            for (int i2 = 0; ((long) i2) < j3 && ofLong.tryAdvance((LongConsumer) longBuilder); i2++) {
            }
            longBuilder.end();
            return longBuilder.build();
        }

        long[] newArray(int i) {
            return new long[i];
        }

        StreamShape getShape() {
            return StreamShape.LONG_VALUE;
        }
    }

    public interface OfDouble extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, OfDouble> {
        static /* synthetic */ void lambda$truncate$0(double d) {
        }

        void forEach(Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                forEach((DoubleConsumer) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            }
            ((Spliterator.OfDouble) spliterator()).forEachRemaining(consumer);
        }

        void copyInto(Double[] dArr, int i) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfDouble.copyInto(Double[], int)");
            }
            double[] dArr2 = (double[]) asPrimitiveArray();
            for (int i2 = 0; i2 < dArr2.length; i2++) {
                dArr[i + i2] = Double.valueOf(dArr2[i2]);
            }
        }

        OfDouble truncate(long j, long j2, IntFunction<Double[]> intFunction) {
            if (j == 0 && j2 == count()) {
                return this;
            }
            long j3 = j2 - j;
            Spliterator.OfDouble ofDouble = (Spliterator.OfDouble) spliterator();
            Builder.OfDouble doubleBuilder = Nodes.doubleBuilder(j3);
            doubleBuilder.begin(j3);
            for (int i = 0; ((long) i) < j && ofDouble.tryAdvance((DoubleConsumer) new Node$OfDouble$$ExternalSyntheticLambda0()); i++) {
            }
            for (int i2 = 0; ((long) i2) < j3 && ofDouble.tryAdvance((DoubleConsumer) doubleBuilder); i2++) {
            }
            doubleBuilder.end();
            return doubleBuilder.build();
        }

        double[] newArray(int i) {
            return new double[i];
        }

        StreamShape getShape() {
            return StreamShape.DOUBLE_VALUE;
        }
    }
}
