package java.util;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;

class ArrayPrefixHelpers {
    static final int CUMULATE = 1;
    static final int FINISHED = 4;
    static final int MIN_PARTITION = 16;
    static final int SUMMED = 2;

    private ArrayPrefixHelpers() {
    }

    static final class CumulateTask<T> extends CountedCompleter<Void> {
        private static final long serialVersionUID = 5293554502939613543L;
        final T[] array;
        final int fence;
        final BinaryOperator<T> function;

        /* renamed from: hi */
        final int f589hi;

        /* renamed from: in */
        T f590in;
        CumulateTask<T> left;

        /* renamed from: lo */
        final int f591lo;
        final int origin;
        T out;
        CumulateTask<T> right;
        final int threshold;

        public CumulateTask(CumulateTask<T> cumulateTask, BinaryOperator<T> binaryOperator, T[] tArr, int i, int i2) {
            super(cumulateTask);
            this.function = binaryOperator;
            this.array = tArr;
            this.origin = i;
            this.f591lo = i;
            this.fence = i2;
            this.f589hi = i2;
            int commonPoolParallelism = (i2 - i) / (ForkJoinPool.getCommonPoolParallelism() << 3);
            this.threshold = commonPoolParallelism <= 16 ? 16 : commonPoolParallelism;
        }

        CumulateTask(CumulateTask<T> cumulateTask, BinaryOperator<T> binaryOperator, T[] tArr, int i, int i2, int i3, int i4, int i5) {
            super(cumulateTask);
            this.function = binaryOperator;
            this.array = tArr;
            this.origin = i;
            this.fence = i2;
            this.threshold = i3;
            this.f591lo = i4;
            this.f589hi = i5;
        }

        public final void compute() {
            T[] tArr;
            int i;
            int pendingCount;
            int i2;
            T t;
            CumulateTask<T> cumulateTask;
            int i3;
            CumulateTask<T> cumulateTask2;
            BinaryOperator<T> binaryOperator = this.function;
            if (binaryOperator == null || (tArr = this.array) == null) {
                throw null;
            }
            int i4 = this.threshold;
            int i5 = this.origin;
            int i6 = this.fence;
            CumulateTask<T> cumulateTask3 = this;
            while (true) {
                int i7 = cumulateTask3.f591lo;
                if (i7 >= 0 && (i = cumulateTask3.f589hi) <= tArr.length) {
                    if (i - i7 > i4) {
                        CumulateTask<T> cumulateTask4 = cumulateTask3.left;
                        CumulateTask<T> cumulateTask5 = cumulateTask3.right;
                        if (cumulateTask4 == null) {
                            int i8 = (i7 + i) >>> 1;
                            CumulateTask cumulateTask6 = cumulateTask3;
                            BinaryOperator<T> binaryOperator2 = binaryOperator;
                            T[] tArr2 = tArr;
                            int i9 = i5;
                            int i10 = i6;
                            CumulateTask<T> cumulateTask7 = r0;
                            int i11 = i4;
                            CumulateTask<T> cumulateTask8 = new CumulateTask<>(cumulateTask6, binaryOperator2, tArr2, i9, i10, i11, i8, i);
                            cumulateTask3.right = cumulateTask7;
                            CumulateTask<T> cumulateTask9 = cumulateTask7;
                            CumulateTask<T> cumulateTask10 = r0;
                            CumulateTask<T> cumulateTask11 = new CumulateTask<>(cumulateTask6, binaryOperator2, tArr2, i9, i10, i11, i7, i8);
                            cumulateTask3.left = cumulateTask10;
                            cumulateTask3 = cumulateTask10;
                            cumulateTask2 = cumulateTask9;
                        } else {
                            int i12 = i7;
                            T t2 = cumulateTask3.f590in;
                            cumulateTask4.f590in = t2;
                            if (cumulateTask5 != null) {
                                T t3 = cumulateTask4.out;
                                if (i12 != i5) {
                                    t3 = binaryOperator.apply(t2, t3);
                                }
                                cumulateTask5.f590in = t3;
                                while (true) {
                                    int pendingCount2 = cumulateTask5.getPendingCount();
                                    if ((pendingCount2 & 1) == 0) {
                                        if (cumulateTask5.compareAndSetPendingCount(pendingCount2, pendingCount2 | 1)) {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            cumulateTask5 = null;
                            while (true) {
                                int pendingCount3 = cumulateTask4.getPendingCount();
                                if ((pendingCount3 & 1) != 0) {
                                    cumulateTask4 = cumulateTask5;
                                    cumulateTask2 = null;
                                    break;
                                } else if (cumulateTask4.compareAndSetPendingCount(pendingCount3, pendingCount3 | 1)) {
                                    if (cumulateTask5 == null) {
                                        cumulateTask5 = null;
                                    }
                                    cumulateTask2 = cumulateTask5;
                                }
                            }
                            if (cumulateTask4 != null) {
                                cumulateTask3 = cumulateTask4;
                            } else {
                                return;
                            }
                        }
                        if (cumulateTask2 != null) {
                            cumulateTask2.fork();
                        }
                    } else {
                        int i13 = i;
                        int i14 = i7;
                        do {
                            pendingCount = cumulateTask3.getPendingCount();
                            if ((pendingCount & 4) == 0) {
                                i2 = (pendingCount & 1) != 0 ? 4 : i14 > i5 ? 2 : 6;
                            } else {
                                return;
                            }
                        } while (!cumulateTask3.compareAndSetPendingCount(pendingCount, pendingCount | i2));
                        if (i2 != 2) {
                            if (i14 == i5) {
                                t = tArr[i5];
                                i3 = i5 + 1;
                            } else {
                                t = cumulateTask3.f590in;
                                i3 = i14;
                            }
                            int i15 = i13;
                            while (i3 < i15) {
                                t = binaryOperator.apply(t, tArr[i3]);
                                tArr[i3] = t;
                                i3++;
                            }
                        } else {
                            int i16 = i13;
                            if (i16 < i6) {
                                T t4 = tArr[i14];
                                for (int i17 = i14 + 1; i17 < i16; i17++) {
                                    t4 = binaryOperator.apply(t, tArr[i17]);
                                }
                            } else {
                                t = cumulateTask3.f590in;
                            }
                        }
                        cumulateTask3.out = t;
                        while (true) {
                            CumulateTask cumulateTask12 = (CumulateTask) cumulateTask3.getCompleter();
                            if (cumulateTask12 != null) {
                                int pendingCount4 = cumulateTask12.getPendingCount();
                                int i18 = pendingCount4 & i2;
                                if ((i18 & 4) != 0) {
                                    cumulateTask3 = cumulateTask12;
                                } else if ((i18 & 2) != 0) {
                                    CumulateTask<T> cumulateTask13 = cumulateTask12.left;
                                    if (!(cumulateTask13 == null || (cumulateTask = cumulateTask12.right) == null)) {
                                        T t5 = cumulateTask13.out;
                                        if (cumulateTask.f589hi != i6) {
                                            t5 = binaryOperator.apply(t5, cumulateTask.out);
                                        }
                                        cumulateTask12.out = t5;
                                    }
                                    int i19 = ((pendingCount4 & 1) == 0 && cumulateTask12.f591lo == i5) ? 1 : 0;
                                    int i20 = pendingCount4 | i2 | i19;
                                    if (i20 == pendingCount4 || cumulateTask12.compareAndSetPendingCount(pendingCount4, i20)) {
                                        if (i19 != 0) {
                                            cumulateTask12.fork();
                                        }
                                        cumulateTask3 = cumulateTask12;
                                        i2 = 2;
                                    }
                                } else if (cumulateTask12.compareAndSetPendingCount(pendingCount4, pendingCount4 | i2)) {
                                    return;
                                }
                            } else if ((i2 & 4) != 0) {
                                cumulateTask3.quietlyComplete();
                                return;
                            } else {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    static final class LongCumulateTask extends CountedCompleter<Void> {
        private static final long serialVersionUID = -5074099945909284273L;
        final long[] array;
        final int fence;
        final LongBinaryOperator function;

        /* renamed from: hi */
        final int f598hi;

        /* renamed from: in */
        long f599in;
        LongCumulateTask left;

        /* renamed from: lo */
        final int f600lo;
        final int origin;
        long out;
        LongCumulateTask right;
        final int threshold;

        public LongCumulateTask(LongCumulateTask longCumulateTask, LongBinaryOperator longBinaryOperator, long[] jArr, int i, int i2) {
            super(longCumulateTask);
            this.function = longBinaryOperator;
            this.array = jArr;
            this.origin = i;
            this.f600lo = i;
            this.fence = i2;
            this.f598hi = i2;
            int commonPoolParallelism = (i2 - i) / (ForkJoinPool.getCommonPoolParallelism() << 3);
            this.threshold = commonPoolParallelism <= 16 ? 16 : commonPoolParallelism;
        }

        LongCumulateTask(LongCumulateTask longCumulateTask, LongBinaryOperator longBinaryOperator, long[] jArr, int i, int i2, int i3, int i4, int i5) {
            super(longCumulateTask);
            this.function = longBinaryOperator;
            this.array = jArr;
            this.origin = i;
            this.fence = i2;
            this.threshold = i3;
            this.f600lo = i4;
            this.f598hi = i5;
        }

        public final void compute() {
            long[] jArr;
            int i;
            int pendingCount;
            int i2;
            long j;
            LongCumulateTask longCumulateTask;
            int i3;
            LongCumulateTask longCumulateTask2;
            LongBinaryOperator longBinaryOperator = this.function;
            if (longBinaryOperator == null || (jArr = this.array) == null) {
                throw null;
            }
            int i4 = this.threshold;
            int i5 = this.origin;
            int i6 = this.fence;
            LongCumulateTask longCumulateTask3 = this;
            while (true) {
                int i7 = longCumulateTask3.f600lo;
                if (i7 >= 0 && (i = longCumulateTask3.f598hi) <= jArr.length) {
                    if (i - i7 > i4) {
                        LongCumulateTask longCumulateTask4 = longCumulateTask3.left;
                        LongCumulateTask longCumulateTask5 = longCumulateTask3.right;
                        if (longCumulateTask4 == null) {
                            int i8 = (i7 + i) >>> 1;
                            LongCumulateTask longCumulateTask6 = longCumulateTask3;
                            LongBinaryOperator longBinaryOperator2 = longBinaryOperator;
                            long[] jArr2 = jArr;
                            int i9 = i5;
                            int i10 = i6;
                            LongCumulateTask longCumulateTask7 = r0;
                            int i11 = i4;
                            LongCumulateTask longCumulateTask8 = new LongCumulateTask(longCumulateTask6, longBinaryOperator2, jArr2, i9, i10, i11, i8, i);
                            longCumulateTask3.right = longCumulateTask7;
                            LongCumulateTask longCumulateTask9 = longCumulateTask7;
                            LongCumulateTask longCumulateTask10 = r0;
                            LongCumulateTask longCumulateTask11 = new LongCumulateTask(longCumulateTask6, longBinaryOperator2, jArr2, i9, i10, i11, i7, i8);
                            longCumulateTask3.left = longCumulateTask10;
                            longCumulateTask3 = longCumulateTask10;
                            longCumulateTask2 = longCumulateTask9;
                        } else {
                            int i12 = i7;
                            long j2 = longCumulateTask3.f599in;
                            longCumulateTask4.f599in = j2;
                            if (longCumulateTask5 != null) {
                                long j3 = longCumulateTask4.out;
                                if (i12 != i5) {
                                    j3 = longBinaryOperator.applyAsLong(j2, j3);
                                }
                                longCumulateTask5.f599in = j3;
                                while (true) {
                                    int pendingCount2 = longCumulateTask5.getPendingCount();
                                    if ((pendingCount2 & 1) == 0) {
                                        if (longCumulateTask5.compareAndSetPendingCount(pendingCount2, pendingCount2 | 1)) {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            longCumulateTask5 = null;
                            while (true) {
                                int pendingCount3 = longCumulateTask4.getPendingCount();
                                if ((pendingCount3 & 1) != 0) {
                                    longCumulateTask4 = longCumulateTask5;
                                    longCumulateTask2 = null;
                                    break;
                                } else if (longCumulateTask4.compareAndSetPendingCount(pendingCount3, pendingCount3 | 1)) {
                                    if (longCumulateTask5 == null) {
                                        longCumulateTask5 = null;
                                    }
                                    longCumulateTask2 = longCumulateTask5;
                                }
                            }
                            if (longCumulateTask4 != null) {
                                longCumulateTask3 = longCumulateTask4;
                            } else {
                                return;
                            }
                        }
                        if (longCumulateTask2 != null) {
                            longCumulateTask2.fork();
                        }
                    } else {
                        int i13 = i;
                        int i14 = i7;
                        do {
                            pendingCount = longCumulateTask3.getPendingCount();
                            if ((pendingCount & 4) == 0) {
                                i2 = (pendingCount & 1) != 0 ? 4 : i14 > i5 ? 2 : 6;
                            } else {
                                return;
                            }
                        } while (!longCumulateTask3.compareAndSetPendingCount(pendingCount, pendingCount | i2));
                        if (i2 != 2) {
                            if (i14 == i5) {
                                j = jArr[i5];
                                i3 = i5 + 1;
                            } else {
                                j = longCumulateTask3.f599in;
                                i3 = i14;
                            }
                            int i15 = i13;
                            while (i3 < i15) {
                                j = longBinaryOperator.applyAsLong(j, jArr[i3]);
                                jArr[i3] = j;
                                i3++;
                            }
                        } else {
                            int i16 = i13;
                            if (i16 < i6) {
                                long j4 = jArr[i14];
                                for (int i17 = i14 + 1; i17 < i16; i17++) {
                                    j4 = longBinaryOperator.applyAsLong(j, jArr[i17]);
                                }
                            } else {
                                j = longCumulateTask3.f599in;
                            }
                        }
                        longCumulateTask3.out = j;
                        while (true) {
                            LongCumulateTask longCumulateTask12 = (LongCumulateTask) longCumulateTask3.getCompleter();
                            if (longCumulateTask12 != null) {
                                int pendingCount4 = longCumulateTask12.getPendingCount();
                                int i18 = pendingCount4 & i2;
                                if ((i18 & 4) != 0) {
                                    longCumulateTask3 = longCumulateTask12;
                                } else if ((i18 & 2) != 0) {
                                    LongCumulateTask longCumulateTask13 = longCumulateTask12.left;
                                    if (!(longCumulateTask13 == null || (longCumulateTask = longCumulateTask12.right) == null)) {
                                        long j5 = longCumulateTask13.out;
                                        if (longCumulateTask.f598hi != i6) {
                                            j5 = longBinaryOperator.applyAsLong(j5, longCumulateTask.out);
                                        }
                                        longCumulateTask12.out = j5;
                                    }
                                    int i19 = ((pendingCount4 & 1) == 0 && longCumulateTask12.f600lo == i5) ? 1 : 0;
                                    int i20 = pendingCount4 | i2 | i19;
                                    if (i20 == pendingCount4 || longCumulateTask12.compareAndSetPendingCount(pendingCount4, i20)) {
                                        if (i19 != 0) {
                                            longCumulateTask12.fork();
                                        }
                                        longCumulateTask3 = longCumulateTask12;
                                        i2 = 2;
                                    }
                                } else if (longCumulateTask12.compareAndSetPendingCount(pendingCount4, pendingCount4 | i2)) {
                                    return;
                                }
                            } else if ((i2 & 4) != 0) {
                                longCumulateTask3.quietlyComplete();
                                return;
                            } else {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    static final class DoubleCumulateTask extends CountedCompleter<Void> {
        private static final long serialVersionUID = -586947823794232033L;
        final double[] array;
        final int fence;
        final DoubleBinaryOperator function;

        /* renamed from: hi */
        final int f592hi;

        /* renamed from: in */
        double f593in;
        DoubleCumulateTask left;

        /* renamed from: lo */
        final int f594lo;
        final int origin;
        double out;
        DoubleCumulateTask right;
        final int threshold;

        public DoubleCumulateTask(DoubleCumulateTask doubleCumulateTask, DoubleBinaryOperator doubleBinaryOperator, double[] dArr, int i, int i2) {
            super(doubleCumulateTask);
            this.function = doubleBinaryOperator;
            this.array = dArr;
            this.origin = i;
            this.f594lo = i;
            this.fence = i2;
            this.f592hi = i2;
            int commonPoolParallelism = (i2 - i) / (ForkJoinPool.getCommonPoolParallelism() << 3);
            this.threshold = commonPoolParallelism <= 16 ? 16 : commonPoolParallelism;
        }

        DoubleCumulateTask(DoubleCumulateTask doubleCumulateTask, DoubleBinaryOperator doubleBinaryOperator, double[] dArr, int i, int i2, int i3, int i4, int i5) {
            super(doubleCumulateTask);
            this.function = doubleBinaryOperator;
            this.array = dArr;
            this.origin = i;
            this.fence = i2;
            this.threshold = i3;
            this.f594lo = i4;
            this.f592hi = i5;
        }

        public final void compute() {
            double[] dArr;
            int i;
            int pendingCount;
            int i2;
            double d;
            DoubleCumulateTask doubleCumulateTask;
            int i3;
            DoubleCumulateTask doubleCumulateTask2;
            DoubleBinaryOperator doubleBinaryOperator = this.function;
            if (doubleBinaryOperator == null || (dArr = this.array) == null) {
                throw null;
            }
            int i4 = this.threshold;
            int i5 = this.origin;
            int i6 = this.fence;
            DoubleCumulateTask doubleCumulateTask3 = this;
            while (true) {
                int i7 = doubleCumulateTask3.f594lo;
                if (i7 >= 0 && (i = doubleCumulateTask3.f592hi) <= dArr.length) {
                    if (i - i7 > i4) {
                        DoubleCumulateTask doubleCumulateTask4 = doubleCumulateTask3.left;
                        DoubleCumulateTask doubleCumulateTask5 = doubleCumulateTask3.right;
                        if (doubleCumulateTask4 == null) {
                            int i8 = (i7 + i) >>> 1;
                            DoubleCumulateTask doubleCumulateTask6 = doubleCumulateTask3;
                            DoubleBinaryOperator doubleBinaryOperator2 = doubleBinaryOperator;
                            double[] dArr2 = dArr;
                            int i9 = i5;
                            int i10 = i6;
                            DoubleCumulateTask doubleCumulateTask7 = r0;
                            int i11 = i4;
                            DoubleCumulateTask doubleCumulateTask8 = new DoubleCumulateTask(doubleCumulateTask6, doubleBinaryOperator2, dArr2, i9, i10, i11, i8, i);
                            doubleCumulateTask3.right = doubleCumulateTask7;
                            DoubleCumulateTask doubleCumulateTask9 = doubleCumulateTask7;
                            DoubleCumulateTask doubleCumulateTask10 = r0;
                            DoubleCumulateTask doubleCumulateTask11 = new DoubleCumulateTask(doubleCumulateTask6, doubleBinaryOperator2, dArr2, i9, i10, i11, i7, i8);
                            doubleCumulateTask3.left = doubleCumulateTask10;
                            doubleCumulateTask3 = doubleCumulateTask10;
                            doubleCumulateTask2 = doubleCumulateTask9;
                        } else {
                            int i12 = i7;
                            double d2 = doubleCumulateTask3.f593in;
                            doubleCumulateTask4.f593in = d2;
                            if (doubleCumulateTask5 != null) {
                                double d3 = doubleCumulateTask4.out;
                                if (i12 != i5) {
                                    d3 = doubleBinaryOperator.applyAsDouble(d2, d3);
                                }
                                doubleCumulateTask5.f593in = d3;
                                while (true) {
                                    int pendingCount2 = doubleCumulateTask5.getPendingCount();
                                    if ((pendingCount2 & 1) == 0) {
                                        if (doubleCumulateTask5.compareAndSetPendingCount(pendingCount2, pendingCount2 | 1)) {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            doubleCumulateTask5 = null;
                            while (true) {
                                int pendingCount3 = doubleCumulateTask4.getPendingCount();
                                if ((pendingCount3 & 1) != 0) {
                                    doubleCumulateTask4 = doubleCumulateTask5;
                                    doubleCumulateTask2 = null;
                                    break;
                                } else if (doubleCumulateTask4.compareAndSetPendingCount(pendingCount3, pendingCount3 | 1)) {
                                    if (doubleCumulateTask5 == null) {
                                        doubleCumulateTask5 = null;
                                    }
                                    doubleCumulateTask2 = doubleCumulateTask5;
                                }
                            }
                            if (doubleCumulateTask4 != null) {
                                doubleCumulateTask3 = doubleCumulateTask4;
                            } else {
                                return;
                            }
                        }
                        if (doubleCumulateTask2 != null) {
                            doubleCumulateTask2.fork();
                        }
                    } else {
                        int i13 = i;
                        int i14 = i7;
                        do {
                            pendingCount = doubleCumulateTask3.getPendingCount();
                            if ((pendingCount & 4) == 0) {
                                i2 = (pendingCount & 1) != 0 ? 4 : i14 > i5 ? 2 : 6;
                            } else {
                                return;
                            }
                        } while (!doubleCumulateTask3.compareAndSetPendingCount(pendingCount, pendingCount | i2));
                        if (i2 != 2) {
                            if (i14 == i5) {
                                d = dArr[i5];
                                i3 = i5 + 1;
                            } else {
                                d = doubleCumulateTask3.f593in;
                                i3 = i14;
                            }
                            int i15 = i13;
                            while (i3 < i15) {
                                d = doubleBinaryOperator.applyAsDouble(d, dArr[i3]);
                                dArr[i3] = d;
                                i3++;
                            }
                        } else {
                            int i16 = i13;
                            if (i16 < i6) {
                                double d4 = dArr[i14];
                                for (int i17 = i14 + 1; i17 < i16; i17++) {
                                    d4 = doubleBinaryOperator.applyAsDouble(d, dArr[i17]);
                                }
                            } else {
                                d = doubleCumulateTask3.f593in;
                            }
                        }
                        doubleCumulateTask3.out = d;
                        while (true) {
                            DoubleCumulateTask doubleCumulateTask12 = (DoubleCumulateTask) doubleCumulateTask3.getCompleter();
                            if (doubleCumulateTask12 != null) {
                                int pendingCount4 = doubleCumulateTask12.getPendingCount();
                                int i18 = pendingCount4 & i2;
                                if ((i18 & 4) != 0) {
                                    doubleCumulateTask3 = doubleCumulateTask12;
                                } else if ((i18 & 2) != 0) {
                                    DoubleCumulateTask doubleCumulateTask13 = doubleCumulateTask12.left;
                                    if (!(doubleCumulateTask13 == null || (doubleCumulateTask = doubleCumulateTask12.right) == null)) {
                                        double d5 = doubleCumulateTask13.out;
                                        if (doubleCumulateTask.f592hi != i6) {
                                            d5 = doubleBinaryOperator.applyAsDouble(d5, doubleCumulateTask.out);
                                        }
                                        doubleCumulateTask12.out = d5;
                                    }
                                    int i19 = ((pendingCount4 & 1) == 0 && doubleCumulateTask12.f594lo == i5) ? 1 : 0;
                                    int i20 = pendingCount4 | i2 | i19;
                                    if (i20 == pendingCount4 || doubleCumulateTask12.compareAndSetPendingCount(pendingCount4, i20)) {
                                        if (i19 != 0) {
                                            doubleCumulateTask12.fork();
                                        }
                                        doubleCumulateTask3 = doubleCumulateTask12;
                                        i2 = 2;
                                    }
                                } else if (doubleCumulateTask12.compareAndSetPendingCount(pendingCount4, pendingCount4 | i2)) {
                                    return;
                                }
                            } else if ((i2 & 4) != 0) {
                                doubleCumulateTask3.quietlyComplete();
                                return;
                            } else {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    static final class IntCumulateTask extends CountedCompleter<Void> {
        private static final long serialVersionUID = 3731755594596840961L;
        final int[] array;
        final int fence;
        final IntBinaryOperator function;

        /* renamed from: hi */
        final int f595hi;

        /* renamed from: in */
        int f596in;
        IntCumulateTask left;

        /* renamed from: lo */
        final int f597lo;
        final int origin;
        int out;
        IntCumulateTask right;
        final int threshold;

        public IntCumulateTask(IntCumulateTask intCumulateTask, IntBinaryOperator intBinaryOperator, int[] iArr, int i, int i2) {
            super(intCumulateTask);
            this.function = intBinaryOperator;
            this.array = iArr;
            this.origin = i;
            this.f597lo = i;
            this.fence = i2;
            this.f595hi = i2;
            int commonPoolParallelism = (i2 - i) / (ForkJoinPool.getCommonPoolParallelism() << 3);
            this.threshold = commonPoolParallelism <= 16 ? 16 : commonPoolParallelism;
        }

        IntCumulateTask(IntCumulateTask intCumulateTask, IntBinaryOperator intBinaryOperator, int[] iArr, int i, int i2, int i3, int i4, int i5) {
            super(intCumulateTask);
            this.function = intBinaryOperator;
            this.array = iArr;
            this.origin = i;
            this.fence = i2;
            this.threshold = i3;
            this.f597lo = i4;
            this.f595hi = i5;
        }

        public final void compute() {
            int[] iArr;
            int i;
            int pendingCount;
            int i2;
            int i3;
            IntCumulateTask intCumulateTask;
            int i4;
            IntCumulateTask intCumulateTask2;
            IntBinaryOperator intBinaryOperator = this.function;
            if (intBinaryOperator == null || (iArr = this.array) == null) {
                throw null;
            }
            int i5 = this.threshold;
            int i6 = this.origin;
            int i7 = this.fence;
            IntCumulateTask intCumulateTask3 = this;
            while (true) {
                int i8 = intCumulateTask3.f597lo;
                if (i8 >= 0 && (i = intCumulateTask3.f595hi) <= iArr.length) {
                    if (i - i8 > i5) {
                        IntCumulateTask intCumulateTask4 = intCumulateTask3.left;
                        IntCumulateTask intCumulateTask5 = intCumulateTask3.right;
                        if (intCumulateTask4 == null) {
                            int i9 = (i8 + i) >>> 1;
                            IntCumulateTask intCumulateTask6 = intCumulateTask3;
                            IntBinaryOperator intBinaryOperator2 = intBinaryOperator;
                            int[] iArr2 = iArr;
                            int i10 = i6;
                            int i11 = i7;
                            IntCumulateTask intCumulateTask7 = r0;
                            int i12 = i5;
                            IntCumulateTask intCumulateTask8 = new IntCumulateTask(intCumulateTask6, intBinaryOperator2, iArr2, i10, i11, i12, i9, i);
                            intCumulateTask3.right = intCumulateTask7;
                            IntCumulateTask intCumulateTask9 = intCumulateTask7;
                            IntCumulateTask intCumulateTask10 = r0;
                            IntCumulateTask intCumulateTask11 = new IntCumulateTask(intCumulateTask6, intBinaryOperator2, iArr2, i10, i11, i12, i8, i9);
                            intCumulateTask3.left = intCumulateTask10;
                            intCumulateTask3 = intCumulateTask10;
                            intCumulateTask2 = intCumulateTask9;
                        } else {
                            int i13 = i8;
                            int i14 = intCumulateTask3.f596in;
                            intCumulateTask4.f596in = i14;
                            if (intCumulateTask5 != null) {
                                int i15 = intCumulateTask4.out;
                                if (i13 != i6) {
                                    i15 = intBinaryOperator.applyAsInt(i14, i15);
                                }
                                intCumulateTask5.f596in = i15;
                                while (true) {
                                    int pendingCount2 = intCumulateTask5.getPendingCount();
                                    if ((pendingCount2 & 1) == 0) {
                                        if (intCumulateTask5.compareAndSetPendingCount(pendingCount2, pendingCount2 | 1)) {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            intCumulateTask5 = null;
                            while (true) {
                                int pendingCount3 = intCumulateTask4.getPendingCount();
                                if ((pendingCount3 & 1) != 0) {
                                    intCumulateTask4 = intCumulateTask5;
                                    intCumulateTask2 = null;
                                    break;
                                } else if (intCumulateTask4.compareAndSetPendingCount(pendingCount3, pendingCount3 | 1)) {
                                    if (intCumulateTask5 == null) {
                                        intCumulateTask5 = null;
                                    }
                                    intCumulateTask2 = intCumulateTask5;
                                }
                            }
                            if (intCumulateTask4 != null) {
                                intCumulateTask3 = intCumulateTask4;
                            } else {
                                return;
                            }
                        }
                        if (intCumulateTask2 != null) {
                            intCumulateTask2.fork();
                        }
                    } else {
                        int i16 = i;
                        int i17 = i8;
                        do {
                            pendingCount = intCumulateTask3.getPendingCount();
                            if ((pendingCount & 4) == 0) {
                                i2 = (pendingCount & 1) != 0 ? 4 : i17 > i6 ? 2 : 6;
                            } else {
                                return;
                            }
                        } while (!intCumulateTask3.compareAndSetPendingCount(pendingCount, pendingCount | i2));
                        if (i2 != 2) {
                            if (i17 == i6) {
                                i3 = iArr[i6];
                                i4 = i6 + 1;
                            } else {
                                i3 = intCumulateTask3.f596in;
                                i4 = i17;
                            }
                            int i18 = i16;
                            while (i4 < i18) {
                                i3 = intBinaryOperator.applyAsInt(i3, iArr[i4]);
                                iArr[i4] = i3;
                                i4++;
                            }
                        } else {
                            int i19 = i16;
                            if (i19 < i7) {
                                int i20 = iArr[i17];
                                for (int i21 = i17 + 1; i21 < i19; i21++) {
                                    i20 = intBinaryOperator.applyAsInt(i3, iArr[i21]);
                                }
                            } else {
                                i3 = intCumulateTask3.f596in;
                            }
                        }
                        intCumulateTask3.out = i3;
                        while (true) {
                            IntCumulateTask intCumulateTask12 = (IntCumulateTask) intCumulateTask3.getCompleter();
                            if (intCumulateTask12 != null) {
                                int pendingCount4 = intCumulateTask12.getPendingCount();
                                int i22 = pendingCount4 & i2;
                                if ((i22 & 4) != 0) {
                                    intCumulateTask3 = intCumulateTask12;
                                } else if ((i22 & 2) != 0) {
                                    IntCumulateTask intCumulateTask13 = intCumulateTask12.left;
                                    if (!(intCumulateTask13 == null || (intCumulateTask = intCumulateTask12.right) == null)) {
                                        int i23 = intCumulateTask13.out;
                                        if (intCumulateTask.f595hi != i7) {
                                            i23 = intBinaryOperator.applyAsInt(i23, intCumulateTask.out);
                                        }
                                        intCumulateTask12.out = i23;
                                    }
                                    int i24 = ((pendingCount4 & 1) == 0 && intCumulateTask12.f597lo == i6) ? 1 : 0;
                                    int i25 = pendingCount4 | i2 | i24;
                                    if (i25 == pendingCount4 || intCumulateTask12.compareAndSetPendingCount(pendingCount4, i25)) {
                                        if (i24 != 0) {
                                            intCumulateTask12.fork();
                                        }
                                        intCumulateTask3 = intCumulateTask12;
                                        i2 = 2;
                                    }
                                } else if (intCumulateTask12.compareAndSetPendingCount(pendingCount4, pendingCount4 | i2)) {
                                    return;
                                }
                            } else if ((i2 & 4) != 0) {
                                intCumulateTask3.quietlyComplete();
                                return;
                            } else {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }
}
