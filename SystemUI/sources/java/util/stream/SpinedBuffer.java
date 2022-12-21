package java.util.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;

public class SpinedBuffer<E> extends AbstractSpinedBuffer implements Consumer<E>, Iterable<E> {
    private static final int SPLITERATOR_CHARACTERISTICS = 16464;
    protected E[] curChunk = new Object[(1 << this.initialChunkPower)];
    protected E[][] spine;

    public /* bridge */ /* synthetic */ long count() {
        return super.count();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public SpinedBuffer(int i) {
        super(i);
    }

    public SpinedBuffer() {
    }

    /* access modifiers changed from: protected */
    public long capacity() {
        if (this.spineIndex == 0) {
            return (long) this.curChunk.length;
        }
        return this.priorElementCount[this.spineIndex] + ((long) this.spine[this.spineIndex].length);
    }

    private void inflateSpine() {
        if (this.spine == null) {
            this.spine = new Object[8][];
            this.priorElementCount = new long[8];
            this.spine[0] = this.curChunk;
        }
    }

    /* access modifiers changed from: protected */
    public final void ensureCapacity(long j) {
        long capacity = capacity();
        if (j > capacity) {
            inflateSpine();
            int i = this.spineIndex;
            while (true) {
                i++;
                if (j > capacity) {
                    E[][] eArr = this.spine;
                    if (i >= eArr.length) {
                        int length = eArr.length * 2;
                        this.spine = (Object[][]) Arrays.copyOf((T[]) eArr, length);
                        this.priorElementCount = Arrays.copyOf(this.priorElementCount, length);
                    }
                    int chunkSize = chunkSize(i);
                    this.spine[i] = new Object[chunkSize];
                    int i2 = i - 1;
                    this.priorElementCount[i] = this.priorElementCount[i2] + ((long) this.spine[i2].length);
                    capacity += (long) chunkSize;
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void increaseCapacity() {
        ensureCapacity(capacity() + 1);
    }

    public E get(long j) {
        if (this.spineIndex == 0) {
            if (j < ((long) this.elementIndex)) {
                return this.curChunk[(int) j];
            }
            throw new IndexOutOfBoundsException(Long.toString(j));
        } else if (j < count()) {
            for (int i = 0; i <= this.spineIndex; i++) {
                long j2 = this.priorElementCount[i];
                E[] eArr = this.spine[i];
                if (j < j2 + ((long) eArr.length)) {
                    return eArr[(int) (j - this.priorElementCount[i])];
                }
            }
            throw new IndexOutOfBoundsException(Long.toString(j));
        } else {
            throw new IndexOutOfBoundsException(Long.toString(j));
        }
    }

    public void copyInto(E[] eArr, int i) {
        long j = (long) i;
        long count = count() + j;
        if (count > ((long) eArr.length) || count < j) {
            throw new IndexOutOfBoundsException("does not fit");
        } else if (this.spineIndex == 0) {
            System.arraycopy((Object) this.curChunk, 0, (Object) eArr, i, this.elementIndex);
        } else {
            for (int i2 = 0; i2 < this.spineIndex; i2++) {
                E[] eArr2 = this.spine[i2];
                System.arraycopy((Object) eArr2, 0, (Object) eArr, i, eArr2.length);
                i += this.spine[i2].length;
            }
            if (this.elementIndex > 0) {
                System.arraycopy((Object) this.curChunk, 0, (Object) eArr, i, this.elementIndex);
            }
        }
    }

    public E[] asArray(IntFunction<E[]> intFunction) {
        long count = count();
        if (count < 2147483639) {
            E[] eArr = (Object[]) intFunction.apply((int) count);
            copyInto(eArr, 0);
            return eArr;
        }
        throw new IllegalArgumentException("Stream size exceeds max array size");
    }

    public void clear() {
        E[][] eArr = this.spine;
        if (eArr != null) {
            this.curChunk = eArr[0];
            int i = 0;
            while (true) {
                E[] eArr2 = this.curChunk;
                if (i >= eArr2.length) {
                    break;
                }
                eArr2[i] = null;
                i++;
            }
            this.spine = null;
            this.priorElementCount = null;
        } else {
            for (int i2 = 0; i2 < this.elementIndex; i2++) {
                this.curChunk[i2] = null;
            }
        }
        this.elementIndex = 0;
        this.spineIndex = 0;
    }

    public Iterator<E> iterator() {
        return Spliterators.iterator(spliterator());
    }

    public void forEach(Consumer<? super E> consumer) {
        for (int i = 0; i < this.spineIndex; i++) {
            for (E accept : this.spine[i]) {
                consumer.accept(accept);
            }
        }
        for (int i2 = 0; i2 < this.elementIndex; i2++) {
            consumer.accept(this.curChunk[i2]);
        }
    }

    public void accept(E e) {
        if (this.elementIndex == this.curChunk.length) {
            inflateSpine();
            int i = this.spineIndex + 1;
            E[][] eArr = this.spine;
            if (i >= eArr.length || eArr[this.spineIndex + 1] == null) {
                increaseCapacity();
            }
            this.elementIndex = 0;
            this.spineIndex++;
            this.curChunk = this.spine[this.spineIndex];
        }
        E[] eArr2 = this.curChunk;
        int i2 = this.elementIndex;
        this.elementIndex = i2 + 1;
        eArr2[i2] = e;
    }

    public String toString() {
        ArrayList arrayList = new ArrayList();
        forEach(new SpinedBuffer$$ExternalSyntheticLambda0(arrayList));
        return "SpinedBuffer:" + arrayList.toString();
    }

    /* renamed from: java.util.stream.SpinedBuffer$1Splitr  reason: invalid class name */
    class AnonymousClass1Splitr implements Spliterator<E> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        final int lastSpineElementFence;
        final int lastSpineIndex;
        E[] splChunk;
        int splElementIndex;
        int splSpineIndex;

        public int characteristics() {
            return SpinedBuffer.SPLITERATOR_CHARACTERISTICS;
        }

        static {
            Class<SpinedBuffer> cls = SpinedBuffer.class;
        }

        {
            this.splSpineIndex = r2;
            this.lastSpineIndex = r3;
            this.splElementIndex = r4;
            this.lastSpineElementFence = r5;
            this.splChunk = SpinedBuffer.this.spine == null ? SpinedBuffer.this.curChunk : SpinedBuffer.this.spine[r2];
        }

        public long estimateSize() {
            int i;
            long j;
            if (this.splSpineIndex == this.lastSpineIndex) {
                j = (long) this.lastSpineElementFence;
                i = this.splElementIndex;
            } else {
                j = (SpinedBuffer.this.priorElementCount[this.lastSpineIndex] + ((long) this.lastSpineElementFence)) - SpinedBuffer.this.priorElementCount[this.splSpineIndex];
                i = this.splElementIndex;
            }
            return j - ((long) i);
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            int i = this.splSpineIndex;
            int i2 = this.lastSpineIndex;
            if (i >= i2 && (i != i2 || this.splElementIndex >= this.lastSpineElementFence)) {
                return false;
            }
            E[] eArr = this.splChunk;
            int i3 = this.splElementIndex;
            this.splElementIndex = i3 + 1;
            consumer.accept(eArr[i3]);
            if (this.splElementIndex == this.splChunk.length) {
                this.splElementIndex = 0;
                this.splSpineIndex++;
                if (SpinedBuffer.this.spine != null && this.splSpineIndex <= this.lastSpineIndex) {
                    this.splChunk = SpinedBuffer.this.spine[this.splSpineIndex];
                }
            }
            return true;
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            int i;
            Objects.requireNonNull(consumer);
            int i2 = this.splSpineIndex;
            int i3 = this.lastSpineIndex;
            if (i2 < i3 || (i2 == i3 && this.splElementIndex < this.lastSpineElementFence)) {
                int i4 = this.splElementIndex;
                while (true) {
                    i = this.lastSpineIndex;
                    if (i2 >= i) {
                        break;
                    }
                    E[] eArr = SpinedBuffer.this.spine[i2];
                    while (i4 < eArr.length) {
                        consumer.accept(eArr[i4]);
                        i4++;
                    }
                    i2++;
                    i4 = 0;
                }
                E[] eArr2 = this.splSpineIndex == i ? this.splChunk : SpinedBuffer.this.spine[this.lastSpineIndex];
                int i5 = this.lastSpineElementFence;
                while (i4 < i5) {
                    consumer.accept(eArr2[i4]);
                    i4++;
                }
                this.splSpineIndex = this.lastSpineIndex;
                this.splElementIndex = this.lastSpineElementFence;
            }
        }

        public Spliterator<E> trySplit() {
            int i = this.splSpineIndex;
            int i2 = this.lastSpineIndex;
            if (i < i2) {
                SpinedBuffer spinedBuffer = SpinedBuffer.this;
                AnonymousClass1Splitr r0 = new AnonymousClass1Splitr(i, i2 - 1, this.splElementIndex, spinedBuffer.spine[this.lastSpineIndex - 1].length);
                this.splSpineIndex = this.lastSpineIndex;
                this.splElementIndex = 0;
                this.splChunk = SpinedBuffer.this.spine[this.splSpineIndex];
                return r0;
            } else if (i != i2) {
                return null;
            } else {
                int i3 = this.lastSpineElementFence;
                int i4 = this.splElementIndex;
                int i5 = (i3 - i4) / 2;
                if (i5 == 0) {
                    return null;
                }
                Spliterator<E> spliterator = Arrays.spliterator((T[]) this.splChunk, i4, i4 + i5);
                this.splElementIndex += i5;
                return spliterator;
            }
        }
    }

    public Spliterator<E> spliterator() {
        return new AnonymousClass1Splitr(0, this.spineIndex, 0, this.elementIndex);
    }

    static abstract class OfPrimitive<E, T_ARR, T_CONS> extends AbstractSpinedBuffer implements Iterable<E> {
        T_ARR curChunk = newArray(1 << this.initialChunkPower);
        T_ARR[] spine;

        /* access modifiers changed from: protected */
        public abstract void arrayForEach(T_ARR t_arr, int i, int i2, T_CONS t_cons);

        /* access modifiers changed from: protected */
        public abstract int arrayLength(T_ARR t_arr);

        public abstract void forEach(Consumer<? super E> consumer);

        public abstract Iterator<E> iterator();

        public abstract T_ARR newArray(int i);

        /* access modifiers changed from: protected */
        public abstract T_ARR[] newArrayArray(int i);

        OfPrimitive(int i) {
            super(i);
        }

        OfPrimitive() {
        }

        /* access modifiers changed from: protected */
        public long capacity() {
            if (this.spineIndex == 0) {
                return (long) arrayLength(this.curChunk);
            }
            return this.priorElementCount[this.spineIndex] + ((long) arrayLength(this.spine[this.spineIndex]));
        }

        private void inflateSpine() {
            if (this.spine == null) {
                this.spine = newArrayArray(8);
                this.priorElementCount = new long[8];
                this.spine[0] = this.curChunk;
            }
        }

        /* access modifiers changed from: protected */
        public final void ensureCapacity(long j) {
            long capacity = capacity();
            if (j > capacity) {
                inflateSpine();
                int i = this.spineIndex;
                while (true) {
                    i++;
                    if (j > capacity) {
                        T_ARR[] t_arrArr = this.spine;
                        if (i >= t_arrArr.length) {
                            int length = t_arrArr.length * 2;
                            this.spine = Arrays.copyOf((T[]) t_arrArr, length);
                            this.priorElementCount = Arrays.copyOf(this.priorElementCount, length);
                        }
                        int chunkSize = chunkSize(i);
                        this.spine[i] = newArray(chunkSize);
                        int i2 = i - 1;
                        this.priorElementCount[i] = this.priorElementCount[i2] + ((long) arrayLength(this.spine[i2]));
                        capacity += (long) chunkSize;
                    } else {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public void increaseCapacity() {
            ensureCapacity(capacity() + 1);
        }

        /* access modifiers changed from: protected */
        public int chunkFor(long j) {
            if (this.spineIndex == 0) {
                if (j < ((long) this.elementIndex)) {
                    return 0;
                }
                throw new IndexOutOfBoundsException(Long.toString(j));
            } else if (j < count()) {
                for (int i = 0; i <= this.spineIndex; i++) {
                    if (j < this.priorElementCount[i] + ((long) arrayLength(this.spine[i]))) {
                        return i;
                    }
                }
                throw new IndexOutOfBoundsException(Long.toString(j));
            } else {
                throw new IndexOutOfBoundsException(Long.toString(j));
            }
        }

        public void copyInto(T_ARR t_arr, int i) {
            long j = (long) i;
            long count = count() + j;
            if (count > ((long) arrayLength(t_arr)) || count < j) {
                throw new IndexOutOfBoundsException("does not fit");
            } else if (this.spineIndex == 0) {
                System.arraycopy((Object) this.curChunk, 0, (Object) t_arr, i, this.elementIndex);
            } else {
                for (int i2 = 0; i2 < this.spineIndex; i2++) {
                    T_ARR t_arr2 = this.spine[i2];
                    System.arraycopy((Object) t_arr2, 0, (Object) t_arr, i, arrayLength(t_arr2));
                    i += arrayLength(this.spine[i2]);
                }
                if (this.elementIndex > 0) {
                    System.arraycopy((Object) this.curChunk, 0, (Object) t_arr, i, this.elementIndex);
                }
            }
        }

        public T_ARR asPrimitiveArray() {
            long count = count();
            if (count < 2147483639) {
                T_ARR newArray = newArray((int) count);
                copyInto(newArray, 0);
                return newArray;
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }

        /* access modifiers changed from: protected */
        public void preAccept() {
            if (this.elementIndex == arrayLength(this.curChunk)) {
                inflateSpine();
                int i = this.spineIndex + 1;
                T_ARR[] t_arrArr = this.spine;
                if (i >= t_arrArr.length || t_arrArr[this.spineIndex + 1] == null) {
                    increaseCapacity();
                }
                this.elementIndex = 0;
                this.spineIndex++;
                this.curChunk = this.spine[this.spineIndex];
            }
        }

        public void clear() {
            T_ARR[] t_arrArr = this.spine;
            if (t_arrArr != null) {
                this.curChunk = t_arrArr[0];
                this.spine = null;
                this.priorElementCount = null;
            }
            this.elementIndex = 0;
            this.spineIndex = 0;
        }

        public void forEach(T_CONS t_cons) {
            for (int i = 0; i < this.spineIndex; i++) {
                T_ARR t_arr = this.spine[i];
                arrayForEach(t_arr, 0, arrayLength(t_arr), t_cons);
            }
            arrayForEach(this.curChunk, 0, this.elementIndex, t_cons);
        }

        abstract class BaseSpliterator<T_SPLITR extends Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>> implements Spliterator.OfPrimitive<E, T_CONS, T_SPLITR> {
            static final /* synthetic */ boolean $assertionsDisabled = false;
            final int lastSpineElementFence;
            final int lastSpineIndex;
            T_ARR splChunk;
            int splElementIndex;
            int splSpineIndex;

            /* access modifiers changed from: package-private */
            public abstract void arrayForOne(T_ARR t_arr, int i, T_CONS t_cons);

            /* access modifiers changed from: package-private */
            public abstract T_SPLITR arraySpliterator(T_ARR t_arr, int i, int i2);

            public int characteristics() {
                return SpinedBuffer.SPLITERATOR_CHARACTERISTICS;
            }

            /* access modifiers changed from: package-private */
            public abstract T_SPLITR newSpliterator(int i, int i2, int i3, int i4);

            static {
                Class<SpinedBuffer> cls = SpinedBuffer.class;
            }

            BaseSpliterator(int i, int i2, int i3, int i4) {
                this.splSpineIndex = i;
                this.lastSpineIndex = i2;
                this.splElementIndex = i3;
                this.lastSpineElementFence = i4;
                this.splChunk = OfPrimitive.this.spine == null ? OfPrimitive.this.curChunk : OfPrimitive.this.spine[i];
            }

            public long estimateSize() {
                int i;
                long j;
                if (this.splSpineIndex == this.lastSpineIndex) {
                    j = (long) this.lastSpineElementFence;
                    i = this.splElementIndex;
                } else {
                    j = (OfPrimitive.this.priorElementCount[this.lastSpineIndex] + ((long) this.lastSpineElementFence)) - OfPrimitive.this.priorElementCount[this.splSpineIndex];
                    i = this.splElementIndex;
                }
                return j - ((long) i);
            }

            public boolean tryAdvance(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                int i = this.splSpineIndex;
                int i2 = this.lastSpineIndex;
                if (i >= i2 && (i != i2 || this.splElementIndex >= this.lastSpineElementFence)) {
                    return false;
                }
                T_ARR t_arr = this.splChunk;
                int i3 = this.splElementIndex;
                this.splElementIndex = i3 + 1;
                arrayForOne(t_arr, i3, t_cons);
                if (this.splElementIndex == OfPrimitive.this.arrayLength(this.splChunk)) {
                    this.splElementIndex = 0;
                    this.splSpineIndex++;
                    if (OfPrimitive.this.spine != null && this.splSpineIndex <= this.lastSpineIndex) {
                        this.splChunk = OfPrimitive.this.spine[this.splSpineIndex];
                    }
                }
                return true;
            }

            public void forEachRemaining(T_CONS t_cons) {
                int i;
                Objects.requireNonNull(t_cons);
                int i2 = this.splSpineIndex;
                int i3 = this.lastSpineIndex;
                if (i2 < i3 || (i2 == i3 && this.splElementIndex < this.lastSpineElementFence)) {
                    int i4 = this.splElementIndex;
                    while (true) {
                        i = this.lastSpineIndex;
                        if (i2 >= i) {
                            break;
                        }
                        T_ARR t_arr = OfPrimitive.this.spine[i2];
                        OfPrimitive ofPrimitive = OfPrimitive.this;
                        ofPrimitive.arrayForEach(t_arr, i4, ofPrimitive.arrayLength(t_arr), t_cons);
                        i2++;
                        i4 = 0;
                    }
                    OfPrimitive.this.arrayForEach(this.splSpineIndex == i ? this.splChunk : OfPrimitive.this.spine[this.lastSpineIndex], i4, this.lastSpineElementFence, t_cons);
                    this.splSpineIndex = this.lastSpineIndex;
                    this.splElementIndex = this.lastSpineElementFence;
                }
            }

            public T_SPLITR trySplit() {
                int i = this.splSpineIndex;
                int i2 = this.lastSpineIndex;
                if (i < i2) {
                    int i3 = this.splElementIndex;
                    OfPrimitive ofPrimitive = OfPrimitive.this;
                    T_SPLITR newSpliterator = newSpliterator(i, i2 - 1, i3, ofPrimitive.arrayLength(ofPrimitive.spine[this.lastSpineIndex - 1]));
                    this.splSpineIndex = this.lastSpineIndex;
                    this.splElementIndex = 0;
                    this.splChunk = OfPrimitive.this.spine[this.splSpineIndex];
                    return newSpliterator;
                } else if (i != i2) {
                    return null;
                } else {
                    int i4 = this.lastSpineElementFence;
                    int i5 = this.splElementIndex;
                    int i6 = (i4 - i5) / 2;
                    if (i6 == 0) {
                        return null;
                    }
                    T_SPLITR arraySpliterator = arraySpliterator(this.splChunk, i5, i6);
                    this.splElementIndex += i6;
                    return arraySpliterator;
                }
            }
        }
    }

    public static class OfInt extends OfPrimitive<Integer, int[], IntConsumer> implements IntConsumer {
        public /* bridge */ /* synthetic */ Object asPrimitiveArray() {
            return super.asPrimitiveArray();
        }

        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        public /* bridge */ /* synthetic */ void copyInto(Object obj, int i) {
            super.copyInto(obj, i);
        }

        public /* bridge */ /* synthetic */ long count() {
            return super.count();
        }

        public /* bridge */ /* synthetic */ void forEach(Object obj) {
            super.forEach(obj);
        }

        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        public OfInt() {
        }

        public OfInt(int i) {
            super(i);
        }

        public void forEach(Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                forEach((Object) (IntConsumer) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling SpinedBuffer.OfInt.forEach(Consumer)");
            }
            spliterator().forEachRemaining(consumer);
        }

        /* access modifiers changed from: protected */
        public int[][] newArrayArray(int i) {
            return new int[i][];
        }

        public int[] newArray(int i) {
            return new int[i];
        }

        /* access modifiers changed from: protected */
        public int arrayLength(int[] iArr) {
            return iArr.length;
        }

        /* access modifiers changed from: protected */
        public void arrayForEach(int[] iArr, int i, int i2, IntConsumer intConsumer) {
            while (i < i2) {
                intConsumer.accept(iArr[i]);
                i++;
            }
        }

        public void accept(int i) {
            preAccept();
            int i2 = this.elementIndex;
            this.elementIndex = i2 + 1;
            ((int[]) this.curChunk)[i2] = i;
        }

        public int get(long j) {
            int chunkFor = chunkFor(j);
            if (this.spineIndex == 0 && chunkFor == 0) {
                return ((int[]) this.curChunk)[(int) j];
            }
            return ((int[][]) this.spine)[chunkFor][(int) (j - this.priorElementCount[chunkFor])];
        }

        public PrimitiveIterator.OfInt iterator() {
            return Spliterators.iterator(spliterator());
        }

        public Spliterator.OfInt spliterator() {
            return new Spliterator.OfInt(0, this.spineIndex, 0, this.elementIndex) {
                public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                    super.forEachRemaining(intConsumer);
                }

                public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                    return super.tryAdvance(intConsumer);
                }

                public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                    return (Spliterator.OfInt) super.trySplit();
                }

                /* access modifiers changed from: package-private */
                public AnonymousClass1Splitr newSpliterator(int i, int i2, int i3, int i4) {
                    return 

                    public static class OfLong extends OfPrimitive<Long, long[], LongConsumer> implements LongConsumer {
                        public /* bridge */ /* synthetic */ Object asPrimitiveArray() {
                            return super.asPrimitiveArray();
                        }

                        public /* bridge */ /* synthetic */ void clear() {
                            super.clear();
                        }

                        public /* bridge */ /* synthetic */ void copyInto(Object obj, int i) {
                            super.copyInto(obj, i);
                        }

                        public /* bridge */ /* synthetic */ long count() {
                            return super.count();
                        }

                        public /* bridge */ /* synthetic */ void forEach(Object obj) {
                            super.forEach(obj);
                        }

                        public /* bridge */ /* synthetic */ boolean isEmpty() {
                            return super.isEmpty();
                        }

                        public OfLong() {
                        }

                        public OfLong(int i) {
                            super(i);
                        }

                        public void forEach(Consumer<? super Long> consumer) {
                            if (consumer instanceof LongConsumer) {
                                forEach((Object) (LongConsumer) consumer);
                                return;
                            }
                            if (Tripwire.ENABLED) {
                                Tripwire.trip(getClass(), "{0} calling SpinedBuffer.OfLong.forEach(Consumer)");
                            }
                            spliterator().forEachRemaining(consumer);
                        }

                        /* access modifiers changed from: protected */
                        public long[][] newArrayArray(int i) {
                            return new long[i][];
                        }

                        public long[] newArray(int i) {
                            return new long[i];
                        }

                        /* access modifiers changed from: protected */
                        public int arrayLength(long[] jArr) {
                            return jArr.length;
                        }

                        /* access modifiers changed from: protected */
                        public void arrayForEach(long[] jArr, int i, int i2, LongConsumer longConsumer) {
                            while (i < i2) {
                                longConsumer.accept(jArr[i]);
                                i++;
                            }
                        }

                        public void accept(long j) {
                            preAccept();
                            int i = this.elementIndex;
                            this.elementIndex = i + 1;
                            ((long[]) this.curChunk)[i] = j;
                        }

                        public long get(long j) {
                            int chunkFor = chunkFor(j);
                            if (this.spineIndex == 0 && chunkFor == 0) {
                                return ((long[]) this.curChunk)[(int) j];
                            }
                            return ((long[][]) this.spine)[chunkFor][(int) (j - this.priorElementCount[chunkFor])];
                        }

                        public PrimitiveIterator.OfLong iterator() {
                            return Spliterators.iterator(spliterator());
                        }

                        public Spliterator.OfLong spliterator() {
                            return new Spliterator.OfLong(0, this.spineIndex, 0, this.elementIndex) {
                                public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                                    super.forEachRemaining(longConsumer);
                                }

                                public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                                    return super.tryAdvance(longConsumer);
                                }

                                public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                                    return (Spliterator.OfLong) super.trySplit();
                                }

                                /* access modifiers changed from: package-private */
                                public AnonymousClass1Splitr newSpliterator(int i, int i2, int i3, int i4) {
                                    return 

                                    public static class OfDouble extends OfPrimitive<Double, double[], DoubleConsumer> implements DoubleConsumer {
                                        public /* bridge */ /* synthetic */ Object asPrimitiveArray() {
                                            return super.asPrimitiveArray();
                                        }

                                        public /* bridge */ /* synthetic */ void clear() {
                                            super.clear();
                                        }

                                        public /* bridge */ /* synthetic */ void copyInto(Object obj, int i) {
                                            super.copyInto(obj, i);
                                        }

                                        public /* bridge */ /* synthetic */ long count() {
                                            return super.count();
                                        }

                                        public /* bridge */ /* synthetic */ void forEach(Object obj) {
                                            super.forEach(obj);
                                        }

                                        public /* bridge */ /* synthetic */ boolean isEmpty() {
                                            return super.isEmpty();
                                        }

                                        public OfDouble() {
                                        }

                                        public OfDouble(int i) {
                                            super(i);
                                        }

                                        public void forEach(Consumer<? super Double> consumer) {
                                            if (consumer instanceof DoubleConsumer) {
                                                forEach((Object) (DoubleConsumer) consumer);
                                                return;
                                            }
                                            if (Tripwire.ENABLED) {
                                                Tripwire.trip(getClass(), "{0} calling SpinedBuffer.OfDouble.forEach(Consumer)");
                                            }
                                            spliterator().forEachRemaining(consumer);
                                        }

                                        /* access modifiers changed from: protected */
                                        public double[][] newArrayArray(int i) {
                                            return new double[i][];
                                        }

                                        public double[] newArray(int i) {
                                            return new double[i];
                                        }

                                        /* access modifiers changed from: protected */
                                        public int arrayLength(double[] dArr) {
                                            return dArr.length;
                                        }

                                        /* access modifiers changed from: protected */
                                        public void arrayForEach(double[] dArr, int i, int i2, DoubleConsumer doubleConsumer) {
                                            while (i < i2) {
                                                doubleConsumer.accept(dArr[i]);
                                                i++;
                                            }
                                        }

                                        public void accept(double d) {
                                            preAccept();
                                            int i = this.elementIndex;
                                            this.elementIndex = i + 1;
                                            ((double[]) this.curChunk)[i] = d;
                                        }

                                        public double get(long j) {
                                            int chunkFor = chunkFor(j);
                                            if (this.spineIndex == 0 && chunkFor == 0) {
                                                return ((double[]) this.curChunk)[(int) j];
                                            }
                                            return ((double[][]) this.spine)[chunkFor][(int) (j - this.priorElementCount[chunkFor])];
                                        }

                                        public PrimitiveIterator.OfDouble iterator() {
                                            return Spliterators.iterator(spliterator());
                                        }

                                        public Spliterator.OfDouble spliterator() {
                                            return new Spliterator.OfDouble(0, this.spineIndex, 0, this.elementIndex) {
                                                public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                                                    super.forEachRemaining(doubleConsumer);
                                                }

                                                public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                                                    return super.tryAdvance(doubleConsumer);
                                                }

                                                public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                                                    return (Spliterator.OfDouble) super.trySplit();
                                                }

                                                /* access modifiers changed from: package-private */
                                                public AnonymousClass1Splitr newSpliterator(int i, int i2, int i3, int i4) {
                                                    return 
                                                }
