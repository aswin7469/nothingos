package java.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.reflect.Array;
import java.p026io.Serializable;
import java.util.ArrayPrefixHelpers;
import java.util.ArraysParallelSortHelpers;
import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import jdk.internal.util.ArraysSupport;

public class Arrays {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int INSERTIONSORT_THRESHOLD = 7;
    public static final int MIN_ARRAY_SORT_GRAN = 8192;

    private Arrays() {
    }

    static final class NaturalOrder implements Comparator<Object> {
        static final NaturalOrder INSTANCE = new NaturalOrder();

        NaturalOrder() {
        }

        public int compare(Object obj, Object obj2) {
            return ((Comparable) obj).compareTo(obj2);
        }
    }

    static void rangeCheck(int i, int i2, int i3) {
        if (i2 > i3) {
            throw new IllegalArgumentException("fromIndex(" + i2 + ") > toIndex(" + i3 + NavigationBarInflaterView.KEY_CODE_END);
        } else if (i2 < 0) {
            throw new ArrayIndexOutOfBoundsException(i2);
        } else if (i3 > i) {
            throw new ArrayIndexOutOfBoundsException(i3);
        }
    }

    public static void sort(int[] iArr) {
        DualPivotQuicksort.sort(iArr, 0, iArr.length - 1, (int[]) null, 0, 0);
    }

    public static void sort(int[] iArr, int i, int i2) {
        rangeCheck(iArr.length, i, i2);
        DualPivotQuicksort.sort(iArr, i, i2 - 1, (int[]) null, 0, 0);
    }

    public static void sort(long[] jArr) {
        DualPivotQuicksort.sort(jArr, 0, jArr.length - 1, (long[]) null, 0, 0);
    }

    public static void sort(long[] jArr, int i, int i2) {
        rangeCheck(jArr.length, i, i2);
        DualPivotQuicksort.sort(jArr, i, i2 - 1, (long[]) null, 0, 0);
    }

    public static void sort(short[] sArr) {
        DualPivotQuicksort.sort(sArr, 0, sArr.length - 1, (short[]) null, 0, 0);
    }

    public static void sort(short[] sArr, int i, int i2) {
        rangeCheck(sArr.length, i, i2);
        DualPivotQuicksort.sort(sArr, i, i2 - 1, (short[]) null, 0, 0);
    }

    public static void sort(char[] cArr) {
        DualPivotQuicksort.sort(cArr, 0, cArr.length - 1, (char[]) null, 0, 0);
    }

    public static void sort(char[] cArr, int i, int i2) {
        rangeCheck(cArr.length, i, i2);
        DualPivotQuicksort.sort(cArr, i, i2 - 1, (char[]) null, 0, 0);
    }

    public static void sort(byte[] bArr) {
        DualPivotQuicksort.sort(bArr, 0, bArr.length - 1);
    }

    public static void sort(byte[] bArr, int i, int i2) {
        rangeCheck(bArr.length, i, i2);
        DualPivotQuicksort.sort(bArr, i, i2 - 1);
    }

    public static void sort(float[] fArr) {
        DualPivotQuicksort.sort(fArr, 0, fArr.length - 1, (float[]) null, 0, 0);
    }

    public static void sort(float[] fArr, int i, int i2) {
        rangeCheck(fArr.length, i, i2);
        DualPivotQuicksort.sort(fArr, i, i2 - 1, (float[]) null, 0, 0);
    }

    public static void sort(double[] dArr) {
        DualPivotQuicksort.sort(dArr, 0, dArr.length - 1, (double[]) null, 0, 0);
    }

    public static void sort(double[] dArr, int i, int i2) {
        rangeCheck(dArr.length, i, i2);
        DualPivotQuicksort.sort(dArr, i, i2 - 1, (double[]) null, 0, 0);
    }

    public static void parallelSort(byte[] bArr) {
        int commonPoolParallelism;
        int length = bArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(bArr, 0, length - 1);
            return;
        }
        int i = length / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJByte.Sorter((CountedCompleter<?>) null, bArr, new byte[length], 0, length, 0, i <= 8192 ? 8192 : i).invoke();
    }

    public static void parallelSort(byte[] bArr, int i, int i2) {
        int commonPoolParallelism;
        rangeCheck(bArr.length, i, i2);
        int i3 = i2 - i;
        if (i3 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(bArr, i, i2 - 1);
            return;
        }
        int i4 = i3 / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJByte.Sorter((CountedCompleter<?>) null, bArr, new byte[i3], i, i3, 0, i4 <= 8192 ? 8192 : i4).invoke();
    }

    public static void parallelSort(char[] cArr) {
        int commonPoolParallelism;
        int length = cArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(cArr, 0, length - 1, (char[]) null, 0, 0);
            return;
        }
        int i = length / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJChar.Sorter((CountedCompleter<?>) null, cArr, new char[length], 0, length, 0, i <= 8192 ? 8192 : i).invoke();
    }

    public static void parallelSort(char[] cArr, int i, int i2) {
        int commonPoolParallelism;
        rangeCheck(cArr.length, i, i2);
        int i3 = i2 - i;
        if (i3 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(cArr, i, i2 - 1, (char[]) null, 0, 0);
            return;
        }
        int i4 = i3 / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJChar.Sorter((CountedCompleter<?>) null, cArr, new char[i3], i, i3, 0, i4 <= 8192 ? 8192 : i4).invoke();
    }

    public static void parallelSort(short[] sArr) {
        int commonPoolParallelism;
        int length = sArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(sArr, 0, length - 1, (short[]) null, 0, 0);
            return;
        }
        int i = length / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJShort.Sorter((CountedCompleter<?>) null, sArr, new short[length], 0, length, 0, i <= 8192 ? 8192 : i).invoke();
    }

    public static void parallelSort(short[] sArr, int i, int i2) {
        int commonPoolParallelism;
        rangeCheck(sArr.length, i, i2);
        int i3 = i2 - i;
        if (i3 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(sArr, i, i2 - 1, (short[]) null, 0, 0);
            return;
        }
        int i4 = i3 / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJShort.Sorter((CountedCompleter<?>) null, sArr, new short[i3], i, i3, 0, i4 <= 8192 ? 8192 : i4).invoke();
    }

    public static void parallelSort(int[] iArr) {
        int commonPoolParallelism;
        int length = iArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(iArr, 0, length - 1, (int[]) null, 0, 0);
            return;
        }
        int i = length / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJInt.Sorter((CountedCompleter<?>) null, iArr, new int[length], 0, length, 0, i <= 8192 ? 8192 : i).invoke();
    }

    public static void parallelSort(int[] iArr, int i, int i2) {
        int commonPoolParallelism;
        rangeCheck(iArr.length, i, i2);
        int i3 = i2 - i;
        if (i3 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(iArr, i, i2 - 1, (int[]) null, 0, 0);
            return;
        }
        int i4 = i3 / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJInt.Sorter((CountedCompleter<?>) null, iArr, new int[i3], i, i3, 0, i4 <= 8192 ? 8192 : i4).invoke();
    }

    public static void parallelSort(long[] jArr) {
        int commonPoolParallelism;
        int length = jArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(jArr, 0, length - 1, (long[]) null, 0, 0);
            return;
        }
        int i = length / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJLong.Sorter((CountedCompleter<?>) null, jArr, new long[length], 0, length, 0, i <= 8192 ? 8192 : i).invoke();
    }

    public static void parallelSort(long[] jArr, int i, int i2) {
        int commonPoolParallelism;
        rangeCheck(jArr.length, i, i2);
        int i3 = i2 - i;
        if (i3 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(jArr, i, i2 - 1, (long[]) null, 0, 0);
            return;
        }
        int i4 = i3 / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJLong.Sorter((CountedCompleter<?>) null, jArr, new long[i3], i, i3, 0, i4 <= 8192 ? 8192 : i4).invoke();
    }

    public static void parallelSort(float[] fArr) {
        int commonPoolParallelism;
        int length = fArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(fArr, 0, length - 1, (float[]) null, 0, 0);
            return;
        }
        int i = length / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJFloat.Sorter((CountedCompleter<?>) null, fArr, new float[length], 0, length, 0, i <= 8192 ? 8192 : i).invoke();
    }

    public static void parallelSort(float[] fArr, int i, int i2) {
        int commonPoolParallelism;
        rangeCheck(fArr.length, i, i2);
        int i3 = i2 - i;
        if (i3 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(fArr, i, i2 - 1, (float[]) null, 0, 0);
            return;
        }
        int i4 = i3 / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJFloat.Sorter((CountedCompleter<?>) null, fArr, new float[i3], i, i3, 0, i4 <= 8192 ? 8192 : i4).invoke();
    }

    public static void parallelSort(double[] dArr) {
        int commonPoolParallelism;
        int length = dArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(dArr, 0, length - 1, (double[]) null, 0, 0);
            return;
        }
        int i = length / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJDouble.Sorter((CountedCompleter<?>) null, dArr, new double[length], 0, length, 0, i <= 8192 ? 8192 : i).invoke();
    }

    public static void parallelSort(double[] dArr, int i, int i2) {
        int commonPoolParallelism;
        rangeCheck(dArr.length, i, i2);
        int i3 = i2 - i;
        if (i3 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(dArr, i, i2 - 1, (double[]) null, 0, 0);
            return;
        }
        int i4 = i3 / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJDouble.Sorter((CountedCompleter<?>) null, dArr, new double[i3], i, i3, 0, i4 <= 8192 ? 8192 : i4).invoke();
    }

    public static <T extends Comparable<? super T>> void parallelSort(T[] tArr) {
        int commonPoolParallelism;
        int length = tArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            TimSort.sort(tArr, 0, length, NaturalOrder.INSTANCE, (T[]) null, 0, 0);
            return;
        }
        int i = length / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJObject.Sorter((CountedCompleter<?>) null, tArr, (Comparable[]) Array.newInstance(tArr.getClass().getComponentType(), length), 0, length, 0, i <= 8192 ? 8192 : i, NaturalOrder.INSTANCE).invoke();
    }

    public static <T extends Comparable<? super T>> void parallelSort(T[] tArr, int i, int i2) {
        int commonPoolParallelism;
        rangeCheck(tArr.length, i, i2);
        int i3 = i2 - i;
        if (i3 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            TimSort.sort(tArr, i, i2, NaturalOrder.INSTANCE, (T[]) null, 0, 0);
            return;
        }
        int i4 = i3 / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJObject.Sorter((CountedCompleter<?>) null, tArr, (Comparable[]) Array.newInstance(tArr.getClass().getComponentType(), i3), i, i3, 0, i4 <= 8192 ? 8192 : i4, NaturalOrder.INSTANCE).invoke();
    }

    public static <T> void parallelSort(T[] tArr, Comparator<? super T> comparator) {
        int commonPoolParallelism;
        if (comparator == null) {
            comparator = NaturalOrder.INSTANCE;
        }
        int length = tArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            TimSort.sort(tArr, 0, length, comparator, (T[]) null, 0, 0);
            return;
        }
        int i = length / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJObject.Sorter((CountedCompleter<?>) null, tArr, (Object[]) Array.newInstance(tArr.getClass().getComponentType(), length), 0, length, 0, i <= 8192 ? 8192 : i, comparator).invoke();
    }

    public static <T> void parallelSort(T[] tArr, int i, int i2, Comparator<? super T> comparator) {
        int commonPoolParallelism;
        rangeCheck(tArr.length, i, i2);
        if (comparator == null) {
            comparator = NaturalOrder.INSTANCE;
        }
        int i3 = i2 - i;
        if (i3 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            TimSort.sort(tArr, i, i2, comparator, (T[]) null, 0, 0);
            return;
        }
        int i4 = i3 / (commonPoolParallelism << 2);
        new ArraysParallelSortHelpers.FJObject.Sorter((CountedCompleter<?>) null, tArr, (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i3), i, i3, 0, i4 <= 8192 ? 8192 : i4, comparator).invoke();
    }

    public static void sort(Object[] objArr) {
        ComparableTimSort.sort(objArr, 0, objArr.length, (Object[]) null, 0, 0);
    }

    public static void sort(Object[] objArr, int i, int i2) {
        rangeCheck(objArr.length, i, i2);
        ComparableTimSort.sort(objArr, i, i2, (Object[]) null, 0, 0);
    }

    private static void mergeSort(Object[] objArr, Object[] objArr2, int i, int i2, int i3) {
        int i4 = i2 - i;
        if (i4 < 7) {
            for (int i5 = i; i5 < i2; i5++) {
                for (int i6 = i5; i6 > i; i6--) {
                    int i7 = i6 - 1;
                    if (objArr2[i7].compareTo(objArr2[i6]) <= 0) {
                        break;
                    }
                    swap(objArr2, i6, i7);
                }
            }
            return;
        }
        int i8 = i + i3;
        int i9 = i2 + i3;
        int i10 = (i8 + i9) >>> 1;
        int i11 = -i3;
        mergeSort(objArr2, objArr, i8, i10, i11);
        mergeSort(objArr2, objArr, i10, i9, i11);
        if (objArr[i10 - 1].compareTo(objArr[i10]) <= 0) {
            System.arraycopy((Object) objArr, i8, (Object) objArr2, i, i4);
            return;
        }
        int i12 = i10;
        while (i < i2) {
            if (i12 >= i9 || (i8 < i10 && objArr[i8].compareTo(objArr[i12]) <= 0)) {
                objArr2[i] = objArr[i8];
                i8++;
            } else {
                objArr2[i] = objArr[i12];
                i12++;
            }
            i++;
        }
    }

    private static void swap(Object[] objArr, int i, int i2) {
        Object obj = objArr[i];
        objArr[i] = objArr[i2];
        objArr[i2] = obj;
    }

    public static <T> void sort(T[] tArr, Comparator<? super T> comparator) {
        if (comparator == null) {
            sort((Object[]) tArr);
            return;
        }
        TimSort.sort(tArr, 0, tArr.length, comparator, (T[]) null, 0, 0);
    }

    public static <T> void sort(T[] tArr, int i, int i2, Comparator<? super T> comparator) {
        if (comparator == null) {
            sort((Object[]) tArr, i, i2);
            return;
        }
        rangeCheck(tArr.length, i, i2);
        TimSort.sort(tArr, i, i2, comparator, (T[]) null, 0, 0);
    }

    public static <T> void parallelPrefix(T[] tArr, BinaryOperator<T> binaryOperator) {
        Objects.requireNonNull(binaryOperator);
        if (tArr.length > 0) {
            new ArrayPrefixHelpers.CumulateTask((ArrayPrefixHelpers.CumulateTask) null, binaryOperator, tArr, 0, tArr.length).invoke();
        }
    }

    public static <T> void parallelPrefix(T[] tArr, int i, int i2, BinaryOperator<T> binaryOperator) {
        Objects.requireNonNull(binaryOperator);
        rangeCheck(tArr.length, i, i2);
        if (i < i2) {
            new ArrayPrefixHelpers.CumulateTask((ArrayPrefixHelpers.CumulateTask) null, binaryOperator, tArr, i, i2).invoke();
        }
    }

    public static void parallelPrefix(long[] jArr, LongBinaryOperator longBinaryOperator) {
        Objects.requireNonNull(longBinaryOperator);
        if (jArr.length > 0) {
            new ArrayPrefixHelpers.LongCumulateTask((ArrayPrefixHelpers.LongCumulateTask) null, longBinaryOperator, jArr, 0, jArr.length).invoke();
        }
    }

    public static void parallelPrefix(long[] jArr, int i, int i2, LongBinaryOperator longBinaryOperator) {
        Objects.requireNonNull(longBinaryOperator);
        rangeCheck(jArr.length, i, i2);
        if (i < i2) {
            new ArrayPrefixHelpers.LongCumulateTask((ArrayPrefixHelpers.LongCumulateTask) null, longBinaryOperator, jArr, i, i2).invoke();
        }
    }

    public static void parallelPrefix(double[] dArr, DoubleBinaryOperator doubleBinaryOperator) {
        Objects.requireNonNull(doubleBinaryOperator);
        if (dArr.length > 0) {
            new ArrayPrefixHelpers.DoubleCumulateTask((ArrayPrefixHelpers.DoubleCumulateTask) null, doubleBinaryOperator, dArr, 0, dArr.length).invoke();
        }
    }

    public static void parallelPrefix(double[] dArr, int i, int i2, DoubleBinaryOperator doubleBinaryOperator) {
        Objects.requireNonNull(doubleBinaryOperator);
        rangeCheck(dArr.length, i, i2);
        if (i < i2) {
            new ArrayPrefixHelpers.DoubleCumulateTask((ArrayPrefixHelpers.DoubleCumulateTask) null, doubleBinaryOperator, dArr, i, i2).invoke();
        }
    }

    public static void parallelPrefix(int[] iArr, IntBinaryOperator intBinaryOperator) {
        Objects.requireNonNull(intBinaryOperator);
        if (iArr.length > 0) {
            new ArrayPrefixHelpers.IntCumulateTask((ArrayPrefixHelpers.IntCumulateTask) null, intBinaryOperator, iArr, 0, iArr.length).invoke();
        }
    }

    public static void parallelPrefix(int[] iArr, int i, int i2, IntBinaryOperator intBinaryOperator) {
        Objects.requireNonNull(intBinaryOperator);
        rangeCheck(iArr.length, i, i2);
        if (i < i2) {
            new ArrayPrefixHelpers.IntCumulateTask((ArrayPrefixHelpers.IntCumulateTask) null, intBinaryOperator, iArr, i, i2).invoke();
        }
    }

    public static int binarySearch(long[] jArr, long j) {
        return binarySearch0(jArr, 0, jArr.length, j);
    }

    public static int binarySearch(long[] jArr, int i, int i2, long j) {
        rangeCheck(jArr.length, i, i2);
        return binarySearch0(jArr, i, i2, j);
    }

    private static int binarySearch0(long[] jArr, int i, int i2, long j) {
        int i3 = i2 - 1;
        while (i <= i3) {
            int i4 = (i + i3) >>> 1;
            int i5 = (jArr[i4] > j ? 1 : (jArr[i4] == j ? 0 : -1));
            if (i5 < 0) {
                i = i4 + 1;
            } else if (i5 <= 0) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return -(i + 1);
    }

    public static int binarySearch(int[] iArr, int i) {
        return binarySearch0(iArr, 0, iArr.length, i);
    }

    public static int binarySearch(int[] iArr, int i, int i2, int i3) {
        rangeCheck(iArr.length, i, i2);
        return binarySearch0(iArr, i, i2, i3);
    }

    private static int binarySearch0(int[] iArr, int i, int i2, int i3) {
        int i4 = i2 - 1;
        while (i <= i4) {
            int i5 = (i + i4) >>> 1;
            int i6 = iArr[i5];
            if (i6 < i3) {
                i = i5 + 1;
            } else if (i6 <= i3) {
                return i5;
            } else {
                i4 = i5 - 1;
            }
        }
        return -(i + 1);
    }

    public static int binarySearch(short[] sArr, short s) {
        return binarySearch0(sArr, 0, sArr.length, s);
    }

    public static int binarySearch(short[] sArr, int i, int i2, short s) {
        rangeCheck(sArr.length, i, i2);
        return binarySearch0(sArr, i, i2, s);
    }

    private static int binarySearch0(short[] sArr, int i, int i2, short s) {
        int i3 = i2 - 1;
        while (i <= i3) {
            int i4 = (i + i3) >>> 1;
            short s2 = sArr[i4];
            if (s2 < s) {
                i = i4 + 1;
            } else if (s2 <= s) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return -(i + 1);
    }

    public static int binarySearch(char[] cArr, char c) {
        return binarySearch0(cArr, 0, cArr.length, c);
    }

    public static int binarySearch(char[] cArr, int i, int i2, char c) {
        rangeCheck(cArr.length, i, i2);
        return binarySearch0(cArr, i, i2, c);
    }

    private static int binarySearch0(char[] cArr, int i, int i2, char c) {
        int i3 = i2 - 1;
        while (i <= i3) {
            int i4 = (i + i3) >>> 1;
            char c2 = cArr[i4];
            if (c2 < c) {
                i = i4 + 1;
            } else if (c2 <= c) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return -(i + 1);
    }

    public static int binarySearch(byte[] bArr, byte b) {
        return binarySearch0(bArr, 0, bArr.length, b);
    }

    public static int binarySearch(byte[] bArr, int i, int i2, byte b) {
        rangeCheck(bArr.length, i, i2);
        return binarySearch0(bArr, i, i2, b);
    }

    private static int binarySearch0(byte[] bArr, int i, int i2, byte b) {
        int i3 = i2 - 1;
        while (i <= i3) {
            int i4 = (i + i3) >>> 1;
            byte b2 = bArr[i4];
            if (b2 < b) {
                i = i4 + 1;
            } else if (b2 <= b) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return -(i + 1);
    }

    public static int binarySearch(double[] dArr, double d) {
        return binarySearch0(dArr, 0, dArr.length, d);
    }

    public static int binarySearch(double[] dArr, int i, int i2, double d) {
        rangeCheck(dArr.length, i, i2);
        return binarySearch0(dArr, i, i2, d);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0027, code lost:
        if (r1 < 0) goto L_0x000e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int binarySearch0(double[] r5, int r6, int r7, double r8) {
        /*
            int r7 = r7 + -1
        L_0x0002:
            if (r6 > r7) goto L_0x002a
            int r0 = r6 + r7
            int r0 = r0 >>> 1
            r1 = r5[r0]
            int r3 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r3 >= 0) goto L_0x0012
        L_0x000e:
            int r0 = r0 + 1
            r6 = r0
            goto L_0x0002
        L_0x0012:
            int r3 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r3 <= 0) goto L_0x001a
        L_0x0016:
            int r0 = r0 + -1
            r7 = r0
            goto L_0x0002
        L_0x001a:
            long r1 = java.lang.Double.doubleToLongBits(r1)
            long r3 = java.lang.Double.doubleToLongBits(r8)
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 != 0) goto L_0x0027
            return r0
        L_0x0027:
            if (r1 >= 0) goto L_0x0016
            goto L_0x000e
        L_0x002a:
            int r6 = r6 + 1
            int r5 = -r6
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Arrays.binarySearch0(double[], int, int, double):int");
    }

    public static int binarySearch(float[] fArr, float f) {
        return binarySearch0(fArr, 0, fArr.length, f);
    }

    public static int binarySearch(float[] fArr, int i, int i2, float f) {
        rangeCheck(fArr.length, i, i2);
        return binarySearch0(fArr, i, i2, f);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0025, code lost:
        if (r1 < r2) goto L_0x000e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int binarySearch0(float[] r3, int r4, int r5, float r6) {
        /*
            int r5 = r5 + -1
        L_0x0002:
            if (r4 > r5) goto L_0x0028
            int r0 = r4 + r5
            int r0 = r0 >>> 1
            r1 = r3[r0]
            int r2 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r2 >= 0) goto L_0x0012
        L_0x000e:
            int r0 = r0 + 1
            r4 = r0
            goto L_0x0002
        L_0x0012:
            int r2 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r2 <= 0) goto L_0x001a
        L_0x0016:
            int r0 = r0 + -1
            r5 = r0
            goto L_0x0002
        L_0x001a:
            int r1 = java.lang.Float.floatToIntBits(r1)
            int r2 = java.lang.Float.floatToIntBits(r6)
            if (r1 != r2) goto L_0x0025
            return r0
        L_0x0025:
            if (r1 >= r2) goto L_0x0016
            goto L_0x000e
        L_0x0028:
            int r4 = r4 + 1
            int r3 = -r4
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Arrays.binarySearch0(float[], int, int, float):int");
    }

    public static int binarySearch(Object[] objArr, Object obj) {
        return binarySearch0(objArr, 0, objArr.length, obj);
    }

    public static int binarySearch(Object[] objArr, int i, int i2, Object obj) {
        rangeCheck(objArr.length, i, i2);
        return binarySearch0(objArr, i, i2, obj);
    }

    private static int binarySearch0(Object[] objArr, int i, int i2, Object obj) {
        int i3 = i2 - 1;
        while (i <= i3) {
            int i4 = (i + i3) >>> 1;
            int compareTo = objArr[i4].compareTo(obj);
            if (compareTo < 0) {
                i = i4 + 1;
            } else if (compareTo <= 0) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return -(i + 1);
    }

    public static <T> int binarySearch(T[] tArr, T t, Comparator<? super T> comparator) {
        return binarySearch0(tArr, 0, tArr.length, t, comparator);
    }

    public static <T> int binarySearch(T[] tArr, int i, int i2, T t, Comparator<? super T> comparator) {
        rangeCheck(tArr.length, i, i2);
        return binarySearch0(tArr, i, i2, t, comparator);
    }

    private static <T> int binarySearch0(T[] tArr, int i, int i2, T t, Comparator<? super T> comparator) {
        if (comparator == null) {
            return binarySearch0((Object[]) tArr, i, i2, (Object) t);
        }
        int i3 = i2 - 1;
        while (i <= i3) {
            int i4 = (i + i3) >>> 1;
            int compare = comparator.compare(tArr[i4], t);
            if (compare < 0) {
                i = i4 + 1;
            } else if (compare <= 0) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return -(i + 1);
    }

    public static boolean equals(long[] jArr, long[] jArr2) {
        int length;
        if (jArr == jArr2) {
            return true;
        }
        if (jArr == null || jArr2 == null || jArr2.length != (length = jArr.length)) {
            return false;
        }
        if (ArraysSupport.mismatch(jArr, jArr2, length) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(long[] jArr, int i, int i2, long[] jArr2, int i3, int i4) {
        rangeCheck(jArr.length, i, i2);
        rangeCheck(jArr2.length, i3, i4);
        int i5 = i2 - i;
        if (i5 == i4 - i3 && ArraysSupport.mismatch(jArr, i, jArr2, i3, i5) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(int[] iArr, int[] iArr2) {
        int length;
        if (iArr == iArr2) {
            return true;
        }
        if (iArr == null || iArr2 == null || iArr2.length != (length = iArr.length)) {
            return false;
        }
        if (ArraysSupport.mismatch(iArr, iArr2, length) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(int[] iArr, int i, int i2, int[] iArr2, int i3, int i4) {
        rangeCheck(iArr.length, i, i2);
        rangeCheck(iArr2.length, i3, i4);
        int i5 = i2 - i;
        if (i5 == i4 - i3 && ArraysSupport.mismatch(iArr, i, iArr2, i3, i5) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(short[] sArr, short[] sArr2) {
        int length;
        if (sArr == sArr2) {
            return true;
        }
        if (sArr == null || sArr2 == null || sArr2.length != (length = sArr.length)) {
            return false;
        }
        if (ArraysSupport.mismatch(sArr, sArr2, length) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(short[] sArr, int i, int i2, short[] sArr2, int i3, int i4) {
        rangeCheck(sArr.length, i, i2);
        rangeCheck(sArr2.length, i3, i4);
        int i5 = i2 - i;
        if (i5 == i4 - i3 && ArraysSupport.mismatch(sArr, i, sArr2, i3, i5) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(char[] cArr, char[] cArr2) {
        int length;
        if (cArr == cArr2) {
            return true;
        }
        if (cArr == null || cArr2 == null || cArr2.length != (length = cArr.length)) {
            return false;
        }
        if (ArraysSupport.mismatch(cArr, cArr2, length) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(char[] cArr, int i, int i2, char[] cArr2, int i3, int i4) {
        rangeCheck(cArr.length, i, i2);
        rangeCheck(cArr2.length, i3, i4);
        int i5 = i2 - i;
        if (i5 == i4 - i3 && ArraysSupport.mismatch(cArr, i, cArr2, i3, i5) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(byte[] bArr, byte[] bArr2) {
        int length;
        if (bArr == bArr2) {
            return true;
        }
        if (bArr == null || bArr2 == null || bArr2.length != (length = bArr.length)) {
            return false;
        }
        if (ArraysSupport.mismatch(bArr, bArr2, length) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        rangeCheck(bArr.length, i, i2);
        rangeCheck(bArr2.length, i3, i4);
        int i5 = i2 - i;
        if (i5 == i4 - i3 && ArraysSupport.mismatch(bArr, i, bArr2, i3, i5) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(boolean[] zArr, boolean[] zArr2) {
        int length;
        if (zArr == zArr2) {
            return true;
        }
        if (zArr == null || zArr2 == null || zArr2.length != (length = zArr.length)) {
            return false;
        }
        if (ArraysSupport.mismatch(zArr, zArr2, length) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(boolean[] zArr, int i, int i2, boolean[] zArr2, int i3, int i4) {
        rangeCheck(zArr.length, i, i2);
        rangeCheck(zArr2.length, i3, i4);
        int i5 = i2 - i;
        if (i5 == i4 - i3 && ArraysSupport.mismatch(zArr, i, zArr2, i3, i5) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(double[] dArr, double[] dArr2) {
        int length;
        if (dArr == dArr2) {
            return true;
        }
        if (dArr == null || dArr2 == null || dArr2.length != (length = dArr.length)) {
            return false;
        }
        if (ArraysSupport.mismatch(dArr, dArr2, length) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(double[] dArr, int i, int i2, double[] dArr2, int i3, int i4) {
        rangeCheck(dArr.length, i, i2);
        rangeCheck(dArr2.length, i3, i4);
        int i5 = i2 - i;
        if (i5 == i4 - i3 && ArraysSupport.mismatch(dArr, i, dArr2, i3, i5) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(float[] fArr, float[] fArr2) {
        int length;
        if (fArr == fArr2) {
            return true;
        }
        if (fArr == null || fArr2 == null || fArr2.length != (length = fArr.length)) {
            return false;
        }
        if (ArraysSupport.mismatch(fArr, fArr2, length) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(float[] fArr, int i, int i2, float[] fArr2, int i3, int i4) {
        rangeCheck(fArr.length, i, i2);
        rangeCheck(fArr2.length, i3, i4);
        int i5 = i2 - i;
        if (i5 == i4 - i3 && ArraysSupport.mismatch(fArr, i, fArr2, i3, i5) < 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(Object[] objArr, Object[] objArr2) {
        int length;
        if (objArr == objArr2) {
            return true;
        }
        if (objArr == null || objArr2 == null || objArr2.length != (length = objArr.length)) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!Objects.equals(objArr[i], objArr2[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(Object[] objArr, int i, int i2, Object[] objArr2, int i3, int i4) {
        rangeCheck(objArr.length, i, i2);
        rangeCheck(objArr2.length, i3, i4);
        int i5 = i2 - i;
        if (i5 != i4 - i3) {
            return false;
        }
        int i6 = 0;
        while (i6 < i5) {
            int i7 = i + 1;
            int i8 = i3 + 1;
            if (!Objects.equals(objArr[i], objArr2[i3])) {
                return false;
            }
            i6++;
            i = i7;
            i3 = i8;
        }
        return true;
    }

    public static <T> boolean equals(T[] tArr, T[] tArr2, Comparator<? super T> comparator) {
        int length;
        Objects.requireNonNull(comparator);
        if (tArr == tArr2) {
            return true;
        }
        if (tArr == null || tArr2 == null || tArr2.length != (length = tArr.length)) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (comparator.compare(tArr[i], tArr2[i]) != 0) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean equals(T[] tArr, int i, int i2, T[] tArr2, int i3, int i4, Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        rangeCheck(tArr.length, i, i2);
        rangeCheck(tArr2.length, i3, i4);
        int i5 = i2 - i;
        if (i5 != i4 - i3) {
            return false;
        }
        int i6 = 0;
        while (i6 < i5) {
            int i7 = i + 1;
            int i8 = i3 + 1;
            if (comparator.compare(tArr[i], tArr2[i3]) != 0) {
                return false;
            }
            i6++;
            i = i7;
            i3 = i8;
        }
        return true;
    }

    public static void fill(long[] jArr, long j) {
        int length = jArr.length;
        for (int i = 0; i < length; i++) {
            jArr[i] = j;
        }
    }

    public static void fill(long[] jArr, int i, int i2, long j) {
        rangeCheck(jArr.length, i, i2);
        while (i < i2) {
            jArr[i] = j;
            i++;
        }
    }

    public static void fill(int[] iArr, int i) {
        int length = iArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            iArr[i2] = i;
        }
    }

    public static void fill(int[] iArr, int i, int i2, int i3) {
        rangeCheck(iArr.length, i, i2);
        while (i < i2) {
            iArr[i] = i3;
            i++;
        }
    }

    public static void fill(short[] sArr, short s) {
        int length = sArr.length;
        for (int i = 0; i < length; i++) {
            sArr[i] = s;
        }
    }

    public static void fill(short[] sArr, int i, int i2, short s) {
        rangeCheck(sArr.length, i, i2);
        while (i < i2) {
            sArr[i] = s;
            i++;
        }
    }

    public static void fill(char[] cArr, char c) {
        int length = cArr.length;
        for (int i = 0; i < length; i++) {
            cArr[i] = c;
        }
    }

    public static void fill(char[] cArr, int i, int i2, char c) {
        rangeCheck(cArr.length, i, i2);
        while (i < i2) {
            cArr[i] = c;
            i++;
        }
    }

    public static void fill(byte[] bArr, byte b) {
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            bArr[i] = b;
        }
    }

    public static void fill(byte[] bArr, int i, int i2, byte b) {
        rangeCheck(bArr.length, i, i2);
        while (i < i2) {
            bArr[i] = b;
            i++;
        }
    }

    public static void fill(boolean[] zArr, boolean z) {
        int length = zArr.length;
        for (int i = 0; i < length; i++) {
            zArr[i] = z;
        }
    }

    public static void fill(boolean[] zArr, int i, int i2, boolean z) {
        rangeCheck(zArr.length, i, i2);
        while (i < i2) {
            zArr[i] = z;
            i++;
        }
    }

    public static void fill(double[] dArr, double d) {
        int length = dArr.length;
        for (int i = 0; i < length; i++) {
            dArr[i] = d;
        }
    }

    public static void fill(double[] dArr, int i, int i2, double d) {
        rangeCheck(dArr.length, i, i2);
        while (i < i2) {
            dArr[i] = d;
            i++;
        }
    }

    public static void fill(float[] fArr, float f) {
        int length = fArr.length;
        for (int i = 0; i < length; i++) {
            fArr[i] = f;
        }
    }

    public static void fill(float[] fArr, int i, int i2, float f) {
        rangeCheck(fArr.length, i, i2);
        while (i < i2) {
            fArr[i] = f;
            i++;
        }
    }

    public static void fill(Object[] objArr, Object obj) {
        int length = objArr.length;
        for (int i = 0; i < length; i++) {
            objArr[i] = obj;
        }
    }

    public static void fill(Object[] objArr, int i, int i2, Object obj) {
        rangeCheck(objArr.length, i, i2);
        while (i < i2) {
            objArr[i] = obj;
            i++;
        }
    }

    public static <T> T[] copyOf(T[] tArr, int i) {
        return copyOf(tArr, i, tArr.getClass());
    }

    public static <T, U> T[] copyOf(U[] uArr, int i, Class<? extends T[]> cls) {
        T[] tArr;
        if (cls == Object[].class) {
            tArr = new Object[i];
        } else {
            tArr = (Object[]) Array.newInstance(cls.getComponentType(), i);
        }
        System.arraycopy((Object) uArr, 0, (Object) tArr, 0, Math.min(uArr.length, i));
        return tArr;
    }

    public static byte[] copyOf(byte[] bArr, int i) {
        byte[] bArr2 = new byte[i];
        System.arraycopy((Object) bArr, 0, (Object) bArr2, 0, Math.min(bArr.length, i));
        return bArr2;
    }

    public static short[] copyOf(short[] sArr, int i) {
        short[] sArr2 = new short[i];
        System.arraycopy((Object) sArr, 0, (Object) sArr2, 0, Math.min(sArr.length, i));
        return sArr2;
    }

    public static int[] copyOf(int[] iArr, int i) {
        int[] iArr2 = new int[i];
        System.arraycopy((Object) iArr, 0, (Object) iArr2, 0, Math.min(iArr.length, i));
        return iArr2;
    }

    public static long[] copyOf(long[] jArr, int i) {
        long[] jArr2 = new long[i];
        System.arraycopy((Object) jArr, 0, (Object) jArr2, 0, Math.min(jArr.length, i));
        return jArr2;
    }

    public static char[] copyOf(char[] cArr, int i) {
        char[] cArr2 = new char[i];
        System.arraycopy((Object) cArr, 0, (Object) cArr2, 0, Math.min(cArr.length, i));
        return cArr2;
    }

    public static float[] copyOf(float[] fArr, int i) {
        float[] fArr2 = new float[i];
        System.arraycopy((Object) fArr, 0, (Object) fArr2, 0, Math.min(fArr.length, i));
        return fArr2;
    }

    public static double[] copyOf(double[] dArr, int i) {
        double[] dArr2 = new double[i];
        System.arraycopy((Object) dArr, 0, (Object) dArr2, 0, Math.min(dArr.length, i));
        return dArr2;
    }

    public static boolean[] copyOf(boolean[] zArr, int i) {
        boolean[] zArr2 = new boolean[i];
        System.arraycopy((Object) zArr, 0, (Object) zArr2, 0, Math.min(zArr.length, i));
        return zArr2;
    }

    public static <T> T[] copyOfRange(T[] tArr, int i, int i2) {
        return copyOfRange(tArr, i, i2, tArr.getClass());
    }

    public static <T, U> T[] copyOfRange(U[] uArr, int i, int i2, Class<? extends T[]> cls) {
        T[] tArr;
        int i3 = i2 - i;
        if (i3 >= 0) {
            if (cls == Object[].class) {
                tArr = new Object[i3];
            } else {
                tArr = (Object[]) Array.newInstance(cls.getComponentType(), i3);
            }
            System.arraycopy((Object) uArr, i, (Object) tArr, 0, Math.min(uArr.length - i, i3));
            return tArr;
        }
        throw new IllegalArgumentException(i + " > " + i2);
    }

    public static byte[] copyOfRange(byte[] bArr, int i, int i2) {
        int i3 = i2 - i;
        if (i3 >= 0) {
            byte[] bArr2 = new byte[i3];
            System.arraycopy((Object) bArr, i, (Object) bArr2, 0, Math.min(bArr.length - i, i3));
            return bArr2;
        }
        throw new IllegalArgumentException(i + " > " + i2);
    }

    public static short[] copyOfRange(short[] sArr, int i, int i2) {
        int i3 = i2 - i;
        if (i3 >= 0) {
            short[] sArr2 = new short[i3];
            System.arraycopy((Object) sArr, i, (Object) sArr2, 0, Math.min(sArr.length - i, i3));
            return sArr2;
        }
        throw new IllegalArgumentException(i + " > " + i2);
    }

    public static int[] copyOfRange(int[] iArr, int i, int i2) {
        int i3 = i2 - i;
        if (i3 >= 0) {
            int[] iArr2 = new int[i3];
            System.arraycopy((Object) iArr, i, (Object) iArr2, 0, Math.min(iArr.length - i, i3));
            return iArr2;
        }
        throw new IllegalArgumentException(i + " > " + i2);
    }

    public static long[] copyOfRange(long[] jArr, int i, int i2) {
        int i3 = i2 - i;
        if (i3 >= 0) {
            long[] jArr2 = new long[i3];
            System.arraycopy((Object) jArr, i, (Object) jArr2, 0, Math.min(jArr.length - i, i3));
            return jArr2;
        }
        throw new IllegalArgumentException(i + " > " + i2);
    }

    public static char[] copyOfRange(char[] cArr, int i, int i2) {
        int i3 = i2 - i;
        if (i3 >= 0) {
            char[] cArr2 = new char[i3];
            System.arraycopy((Object) cArr, i, (Object) cArr2, 0, Math.min(cArr.length - i, i3));
            return cArr2;
        }
        throw new IllegalArgumentException(i + " > " + i2);
    }

    public static float[] copyOfRange(float[] fArr, int i, int i2) {
        int i3 = i2 - i;
        if (i3 >= 0) {
            float[] fArr2 = new float[i3];
            System.arraycopy((Object) fArr, i, (Object) fArr2, 0, Math.min(fArr.length - i, i3));
            return fArr2;
        }
        throw new IllegalArgumentException(i + " > " + i2);
    }

    public static double[] copyOfRange(double[] dArr, int i, int i2) {
        int i3 = i2 - i;
        if (i3 >= 0) {
            double[] dArr2 = new double[i3];
            System.arraycopy((Object) dArr, i, (Object) dArr2, 0, Math.min(dArr.length - i, i3));
            return dArr2;
        }
        throw new IllegalArgumentException(i + " > " + i2);
    }

    public static boolean[] copyOfRange(boolean[] zArr, int i, int i2) {
        int i3 = i2 - i;
        if (i3 >= 0) {
            boolean[] zArr2 = new boolean[i3];
            System.arraycopy((Object) zArr, i, (Object) zArr2, 0, Math.min(zArr.length - i, i3));
            return zArr2;
        }
        throw new IllegalArgumentException(i + " > " + i2);
    }

    @SafeVarargs
    public static <T> List<T> asList(T... tArr) {
        return new ArrayList(tArr);
    }

    private static class ArrayList<E> extends AbstractList<E> implements RandomAccess, Serializable {
        private static final long serialVersionUID = -2764017481108945198L;

        /* renamed from: a */
        private final E[] f600a;

        ArrayList(E[] eArr) {
            this.f600a = (Object[]) Objects.requireNonNull(eArr);
        }

        public int size() {
            return this.f600a.length;
        }

        public Object[] toArray() {
            return (Object[]) this.f600a.clone();
        }

        public <T> T[] toArray(T[] tArr) {
            int size = size();
            if (tArr.length < size) {
                return Arrays.copyOf(this.f600a, size, tArr.getClass());
            }
            System.arraycopy((Object) this.f600a, 0, (Object) tArr, 0, size);
            if (tArr.length > size) {
                tArr[size] = null;
            }
            return tArr;
        }

        public E get(int i) {
            return this.f600a[i];
        }

        public E set(int i, E e) {
            E[] eArr = this.f600a;
            E e2 = eArr[i];
            eArr[i] = e;
            return e2;
        }

        public int indexOf(Object obj) {
            E[] eArr = this.f600a;
            int i = 0;
            if (obj == null) {
                while (i < eArr.length) {
                    if (eArr[i] == null) {
                        return i;
                    }
                    i++;
                }
                return -1;
            }
            while (i < eArr.length) {
                if (obj.equals(eArr[i])) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        public boolean contains(Object obj) {
            return indexOf(obj) >= 0;
        }

        public Spliterator<E> spliterator() {
            return Spliterators.spliterator((Object[]) this.f600a, 16);
        }

        public void forEach(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            for (E accept : this.f600a) {
                consumer.accept(accept);
            }
        }

        public void replaceAll(UnaryOperator<E> unaryOperator) {
            Objects.requireNonNull(unaryOperator);
            E[] eArr = this.f600a;
            for (int i = 0; i < eArr.length; i++) {
                eArr[i] = unaryOperator.apply(eArr[i]);
            }
        }

        public void sort(Comparator<? super E> comparator) {
            Arrays.sort(this.f600a, comparator);
        }

        public Iterator<E> iterator() {
            return new ArrayItr(this.f600a);
        }
    }

    private static class ArrayItr<E> implements Iterator<E> {

        /* renamed from: a */
        private final E[] f599a;
        private int cursor;

        ArrayItr(E[] eArr) {
            this.f599a = eArr;
        }

        public boolean hasNext() {
            return this.cursor < this.f599a.length;
        }

        public E next() {
            int i = this.cursor;
            E[] eArr = this.f599a;
            if (i < eArr.length) {
                this.cursor = i + 1;
                return eArr[i];
            }
            throw new NoSuchElementException();
        }
    }

    public static int hashCode(long[] jArr) {
        if (jArr == null) {
            return 0;
        }
        int i = 1;
        for (long j : jArr) {
            i = (i * 31) + ((int) (j ^ (j >>> 32)));
        }
        return i;
    }

    public static int hashCode(int[] iArr) {
        if (iArr == null) {
            return 0;
        }
        int i = 1;
        for (int i2 : iArr) {
            i = (i * 31) + i2;
        }
        return i;
    }

    public static int hashCode(short[] sArr) {
        if (sArr == null) {
            return 0;
        }
        int i = 1;
        for (short s : sArr) {
            i = (i * 31) + s;
        }
        return i;
    }

    public static int hashCode(char[] cArr) {
        if (cArr == null) {
            return 0;
        }
        int i = 1;
        for (char c : cArr) {
            i = (i * 31) + c;
        }
        return i;
    }

    public static int hashCode(byte[] bArr) {
        if (bArr == null) {
            return 0;
        }
        int i = 1;
        for (byte b : bArr) {
            i = (i * 31) + b;
        }
        return i;
    }

    public static int hashCode(boolean[] zArr) {
        if (zArr == null) {
            return 0;
        }
        int length = zArr.length;
        int i = 1;
        for (int i2 = 0; i2 < length; i2++) {
            i = (i * 31) + (zArr[i2] ? 1231 : 1237);
        }
        return i;
    }

    public static int hashCode(float[] fArr) {
        if (fArr == null) {
            return 0;
        }
        int i = 1;
        for (float floatToIntBits : fArr) {
            i = (i * 31) + Float.floatToIntBits(floatToIntBits);
        }
        return i;
    }

    public static int hashCode(double[] dArr) {
        if (dArr == null) {
            return 0;
        }
        int i = 1;
        for (double doubleToLongBits : dArr) {
            long doubleToLongBits2 = Double.doubleToLongBits(doubleToLongBits);
            i = (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        }
        return i;
    }

    public static int hashCode(Object[] objArr) {
        int i;
        if (objArr == null) {
            return 0;
        }
        int i2 = 1;
        for (Object obj : objArr) {
            int i3 = i2 * 31;
            if (obj == null) {
                i = 0;
            } else {
                i = obj.hashCode();
            }
            i2 = i3 + i;
        }
        return i2;
    }

    public static int deepHashCode(Object[] objArr) {
        int i;
        if (objArr == null) {
            return 0;
        }
        int i2 = 1;
        for (Object[] objArr2 : objArr) {
            if (objArr2 == null) {
                i = 0;
            } else {
                Class<?> componentType = objArr2.getClass().getComponentType();
                if (componentType == null) {
                    i = objArr2.hashCode();
                } else if (objArr2 instanceof Object[]) {
                    i = deepHashCode(objArr2);
                } else {
                    i = primitiveArrayHashCode(objArr2, componentType);
                }
            }
            i2 = (i2 * 31) + i;
        }
        return i2;
    }

    private static int primitiveArrayHashCode(Object obj, Class<?> cls) {
        if (cls == Byte.TYPE) {
            return hashCode((byte[]) obj);
        }
        if (cls == Integer.TYPE) {
            return hashCode((int[]) obj);
        }
        if (cls == Long.TYPE) {
            return hashCode((long[]) obj);
        }
        if (cls == Character.TYPE) {
            return hashCode((char[]) obj);
        }
        if (cls == Short.TYPE) {
            return hashCode((short[]) obj);
        }
        if (cls == Boolean.TYPE) {
            return hashCode((boolean[]) obj);
        }
        if (cls == Double.TYPE) {
            return hashCode((double[]) obj);
        }
        return hashCode((float[]) obj);
    }

    public static boolean deepEquals(Object[] objArr, Object[] objArr2) {
        int length;
        if (objArr == objArr2) {
            return true;
        }
        if (objArr == null || objArr2 == null || objArr2.length != (length = objArr.length)) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            Object obj = objArr[i];
            Object obj2 = objArr2[i];
            if (obj != obj2 && (obj == null || !deepEquals0(obj, obj2))) {
                return false;
            }
        }
        return true;
    }

    static boolean deepEquals0(Object obj, Object obj2) {
        if ((obj instanceof Object[]) && (obj2 instanceof Object[])) {
            return deepEquals((Object[]) obj, (Object[]) obj2);
        }
        if ((obj instanceof byte[]) && (obj2 instanceof byte[])) {
            return equals((byte[]) obj, (byte[]) obj2);
        }
        if ((obj instanceof short[]) && (obj2 instanceof short[])) {
            return equals((short[]) obj, (short[]) obj2);
        }
        if ((obj instanceof int[]) && (obj2 instanceof int[])) {
            return equals((int[]) obj, (int[]) obj2);
        }
        if ((obj instanceof long[]) && (obj2 instanceof long[])) {
            return equals((long[]) obj, (long[]) obj2);
        }
        if ((obj instanceof char[]) && (obj2 instanceof char[])) {
            return equals((char[]) obj, (char[]) obj2);
        }
        if ((obj instanceof float[]) && (obj2 instanceof float[])) {
            return equals((float[]) obj, (float[]) obj2);
        }
        if ((obj instanceof double[]) && (obj2 instanceof double[])) {
            return equals((double[]) obj, (double[]) obj2);
        }
        if (!(obj instanceof boolean[]) || !(obj2 instanceof boolean[])) {
            return obj.equals(obj2);
        }
        return equals((boolean[]) obj, (boolean[]) obj2);
    }

    public static String toString(long[] jArr) {
        if (jArr == null) {
            return "null";
        }
        int length = jArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        int i = 0;
        while (true) {
            sb.append(jArr[i]);
            if (i == length) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }

    public static String toString(int[] iArr) {
        if (iArr == null) {
            return "null";
        }
        int length = iArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        int i = 0;
        while (true) {
            sb.append(iArr[i]);
            if (i == length) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }

    public static String toString(short[] sArr) {
        if (sArr == null) {
            return "null";
        }
        int length = sArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        int i = 0;
        while (true) {
            sb.append((int) sArr[i]);
            if (i == length) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }

    public static String toString(char[] cArr) {
        if (cArr == null) {
            return "null";
        }
        int length = cArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        int i = 0;
        while (true) {
            sb.append(cArr[i]);
            if (i == length) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }

    public static String toString(byte[] bArr) {
        if (bArr == null) {
            return "null";
        }
        int length = bArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        int i = 0;
        while (true) {
            sb.append((int) bArr[i]);
            if (i == length) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }

    public static String toString(boolean[] zArr) {
        if (zArr == null) {
            return "null";
        }
        int length = zArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        int i = 0;
        while (true) {
            sb.append(zArr[i]);
            if (i == length) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }

    public static String toString(float[] fArr) {
        if (fArr == null) {
            return "null";
        }
        int length = fArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        int i = 0;
        while (true) {
            sb.append(fArr[i]);
            if (i == length) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }

    public static String toString(double[] dArr) {
        if (dArr == null) {
            return "null";
        }
        int length = dArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        int i = 0;
        while (true) {
            sb.append(dArr[i]);
            if (i == length) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }

    public static String toString(Object[] objArr) {
        if (objArr == null) {
            return "null";
        }
        int length = objArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        int i = 0;
        while (true) {
            sb.append(String.valueOf(objArr[i]));
            if (i == length) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }

    public static String deepToString(Object[] objArr) {
        if (objArr == null) {
            return "null";
        }
        int length = objArr.length * 20;
        if (objArr.length != 0 && length <= 0) {
            length = Integer.MAX_VALUE;
        }
        StringBuilder sb = new StringBuilder(length);
        deepToString(objArr, sb, new HashSet());
        return sb.toString();
    }

    private static void deepToString(Object[] objArr, StringBuilder sb, Set<Object[]> set) {
        if (objArr == null) {
            sb.append("null");
            return;
        }
        int length = objArr.length - 1;
        if (length == -1) {
            sb.append("[]");
            return;
        }
        set.add(objArr);
        sb.append('[');
        int i = 0;
        while (true) {
            boolean[] zArr = objArr[i];
            if (zArr == null) {
                sb.append("null");
            } else {
                Class<?> cls = zArr.getClass();
                if (!cls.isArray()) {
                    sb.append(zArr.toString());
                } else if (cls == byte[].class) {
                    sb.append(toString((byte[]) zArr));
                } else if (cls == short[].class) {
                    sb.append(toString((short[]) zArr));
                } else if (cls == int[].class) {
                    sb.append(toString((int[]) zArr));
                } else if (cls == long[].class) {
                    sb.append(toString((long[]) zArr));
                } else if (cls == char[].class) {
                    sb.append(toString((char[]) zArr));
                } else if (cls == float[].class) {
                    sb.append(toString((float[]) zArr));
                } else if (cls == double[].class) {
                    sb.append(toString((double[]) zArr));
                } else if (cls == boolean[].class) {
                    sb.append(toString(zArr));
                } else if (set.contains(zArr)) {
                    sb.append("[...]");
                } else {
                    deepToString((Object[]) zArr, sb, set);
                }
            }
            if (i == length) {
                sb.append(']');
                set.remove(objArr);
                return;
            }
            sb.append(", ");
            i++;
        }
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [java.lang.Object, java.util.function.IntFunction<? extends T>, java.util.function.IntFunction] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> void setAll(T[] r2, java.util.function.IntFunction<? extends T> r3) {
        /*
            java.util.Objects.requireNonNull(r3)
            r0 = 0
        L_0x0004:
            int r1 = r2.length
            if (r0 >= r1) goto L_0x0010
            java.lang.Object r1 = r3.apply(r0)
            r2[r0] = r1
            int r0 = r0 + 1
            goto L_0x0004
        L_0x0010:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Arrays.setAll(java.lang.Object[], java.util.function.IntFunction):void");
    }

    public static <T> void parallelSetAll(T[] tArr, IntFunction<? extends T> intFunction) {
        Objects.requireNonNull(intFunction);
        IntStream.range(0, tArr.length).parallel().forEach(new Arrays$$ExternalSyntheticLambda3(tArr, intFunction));
    }

    static /* synthetic */ void lambda$parallelSetAll$0(Object[] objArr, IntFunction intFunction, int i) {
        objArr[i] = intFunction.apply(i);
    }

    public static void setAll(int[] iArr, IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = intUnaryOperator.applyAsInt(i);
        }
    }

    public static void parallelSetAll(int[] iArr, IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        IntStream.range(0, iArr.length).parallel().forEach(new Arrays$$ExternalSyntheticLambda2(iArr, intUnaryOperator));
    }

    static /* synthetic */ void lambda$parallelSetAll$1(int[] iArr, IntUnaryOperator intUnaryOperator, int i) {
        iArr[i] = intUnaryOperator.applyAsInt(i);
    }

    public static void setAll(long[] jArr, IntToLongFunction intToLongFunction) {
        Objects.requireNonNull(intToLongFunction);
        for (int i = 0; i < jArr.length; i++) {
            jArr[i] = intToLongFunction.applyAsLong(i);
        }
    }

    public static void parallelSetAll(long[] jArr, IntToLongFunction intToLongFunction) {
        Objects.requireNonNull(intToLongFunction);
        IntStream.range(0, jArr.length).parallel().forEach(new Arrays$$ExternalSyntheticLambda0(jArr, intToLongFunction));
    }

    static /* synthetic */ void lambda$parallelSetAll$2(long[] jArr, IntToLongFunction intToLongFunction, int i) {
        jArr[i] = intToLongFunction.applyAsLong(i);
    }

    public static void setAll(double[] dArr, IntToDoubleFunction intToDoubleFunction) {
        Objects.requireNonNull(intToDoubleFunction);
        for (int i = 0; i < dArr.length; i++) {
            dArr[i] = intToDoubleFunction.applyAsDouble(i);
        }
    }

    public static void parallelSetAll(double[] dArr, IntToDoubleFunction intToDoubleFunction) {
        Objects.requireNonNull(intToDoubleFunction);
        IntStream.range(0, dArr.length).parallel().forEach(new Arrays$$ExternalSyntheticLambda1(dArr, intToDoubleFunction));
    }

    static /* synthetic */ void lambda$parallelSetAll$3(double[] dArr, IntToDoubleFunction intToDoubleFunction, int i) {
        dArr[i] = intToDoubleFunction.applyAsDouble(i);
    }

    public static <T> Spliterator<T> spliterator(T[] tArr) {
        return Spliterators.spliterator((Object[]) tArr, 1040);
    }

    public static <T> Spliterator<T> spliterator(T[] tArr, int i, int i2) {
        return Spliterators.spliterator((Object[]) tArr, i, i2, 1040);
    }

    public static Spliterator.OfInt spliterator(int[] iArr) {
        return Spliterators.spliterator(iArr, 1040);
    }

    public static Spliterator.OfInt spliterator(int[] iArr, int i, int i2) {
        return Spliterators.spliterator(iArr, i, i2, 1040);
    }

    public static Spliterator.OfLong spliterator(long[] jArr) {
        return Spliterators.spliterator(jArr, 1040);
    }

    public static Spliterator.OfLong spliterator(long[] jArr, int i, int i2) {
        return Spliterators.spliterator(jArr, i, i2, 1040);
    }

    public static Spliterator.OfDouble spliterator(double[] dArr) {
        return Spliterators.spliterator(dArr, 1040);
    }

    public static Spliterator.OfDouble spliterator(double[] dArr, int i, int i2) {
        return Spliterators.spliterator(dArr, i, i2, 1040);
    }

    public static <T> Stream<T> stream(T[] tArr) {
        return stream(tArr, 0, tArr.length);
    }

    public static <T> Stream<T> stream(T[] tArr, int i, int i2) {
        return StreamSupport.stream(spliterator(tArr, i, i2), false);
    }

    public static IntStream stream(int[] iArr) {
        return stream(iArr, 0, iArr.length);
    }

    public static IntStream stream(int[] iArr, int i, int i2) {
        return StreamSupport.intStream(spliterator(iArr, i, i2), false);
    }

    public static LongStream stream(long[] jArr) {
        return stream(jArr, 0, jArr.length);
    }

    public static LongStream stream(long[] jArr, int i, int i2) {
        return StreamSupport.longStream(spliterator(jArr, i, i2), false);
    }

    public static DoubleStream stream(double[] dArr) {
        return stream(dArr, 0, dArr.length);
    }

    public static DoubleStream stream(double[] dArr, int i, int i2) {
        return StreamSupport.doubleStream(spliterator(dArr, i, i2), false);
    }

    public static int compare(boolean[] zArr, boolean[] zArr2) {
        if (zArr == zArr2) {
            return 0;
        }
        if (zArr == null || zArr2 == null) {
            return zArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(zArr, zArr2, Math.min(zArr.length, zArr2.length));
        if (mismatch >= 0) {
            return Boolean.compare(zArr[mismatch], zArr2[mismatch]);
        }
        return zArr.length - zArr2.length;
    }

    public static int compare(boolean[] zArr, int i, int i2, boolean[] zArr2, int i3, int i4) {
        rangeCheck(zArr.length, i, i2);
        rangeCheck(zArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(zArr, i, zArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Boolean.compare(zArr[i + mismatch], zArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compare(byte[] bArr, byte[] bArr2) {
        if (bArr == bArr2) {
            return 0;
        }
        if (bArr == null || bArr2 == null) {
            return bArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(bArr, bArr2, Math.min(bArr.length, bArr2.length));
        if (mismatch >= 0) {
            return Byte.compare(bArr[mismatch], bArr2[mismatch]);
        }
        return bArr.length - bArr2.length;
    }

    public static int compare(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        rangeCheck(bArr.length, i, i2);
        rangeCheck(bArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(bArr, i, bArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Byte.compare(bArr[i + mismatch], bArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compareUnsigned(byte[] bArr, byte[] bArr2) {
        if (bArr == bArr2) {
            return 0;
        }
        if (bArr == null || bArr2 == null) {
            return bArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(bArr, bArr2, Math.min(bArr.length, bArr2.length));
        if (mismatch >= 0) {
            return Byte.compareUnsigned(bArr[mismatch], bArr2[mismatch]);
        }
        return bArr.length - bArr2.length;
    }

    public static int compareUnsigned(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        rangeCheck(bArr.length, i, i2);
        rangeCheck(bArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(bArr, i, bArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Byte.compareUnsigned(bArr[i + mismatch], bArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compare(short[] sArr, short[] sArr2) {
        if (sArr == sArr2) {
            return 0;
        }
        if (sArr == null || sArr2 == null) {
            return sArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(sArr, sArr2, Math.min(sArr.length, sArr2.length));
        if (mismatch >= 0) {
            return Short.compare(sArr[mismatch], sArr2[mismatch]);
        }
        return sArr.length - sArr2.length;
    }

    public static int compare(short[] sArr, int i, int i2, short[] sArr2, int i3, int i4) {
        rangeCheck(sArr.length, i, i2);
        rangeCheck(sArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(sArr, i, sArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Short.compare(sArr[i + mismatch], sArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compareUnsigned(short[] sArr, short[] sArr2) {
        if (sArr == sArr2) {
            return 0;
        }
        if (sArr == null || sArr2 == null) {
            return sArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(sArr, sArr2, Math.min(sArr.length, sArr2.length));
        if (mismatch >= 0) {
            return Short.compareUnsigned(sArr[mismatch], sArr2[mismatch]);
        }
        return sArr.length - sArr2.length;
    }

    public static int compareUnsigned(short[] sArr, int i, int i2, short[] sArr2, int i3, int i4) {
        rangeCheck(sArr.length, i, i2);
        rangeCheck(sArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(sArr, i, sArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Short.compareUnsigned(sArr[i + mismatch], sArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compare(char[] cArr, char[] cArr2) {
        if (cArr == cArr2) {
            return 0;
        }
        if (cArr == null || cArr2 == null) {
            return cArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(cArr, cArr2, Math.min(cArr.length, cArr2.length));
        if (mismatch >= 0) {
            return Character.compare(cArr[mismatch], cArr2[mismatch]);
        }
        return cArr.length - cArr2.length;
    }

    public static int compare(char[] cArr, int i, int i2, char[] cArr2, int i3, int i4) {
        rangeCheck(cArr.length, i, i2);
        rangeCheck(cArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(cArr, i, cArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Character.compare(cArr[i + mismatch], cArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compare(int[] iArr, int[] iArr2) {
        if (iArr == iArr2) {
            return 0;
        }
        if (iArr == null || iArr2 == null) {
            return iArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(iArr, iArr2, Math.min(iArr.length, iArr2.length));
        if (mismatch >= 0) {
            return Integer.compare(iArr[mismatch], iArr2[mismatch]);
        }
        return iArr.length - iArr2.length;
    }

    public static int compare(int[] iArr, int i, int i2, int[] iArr2, int i3, int i4) {
        rangeCheck(iArr.length, i, i2);
        rangeCheck(iArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(iArr, i, iArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Integer.compare(iArr[i + mismatch], iArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compareUnsigned(int[] iArr, int[] iArr2) {
        if (iArr == iArr2) {
            return 0;
        }
        if (iArr == null || iArr2 == null) {
            return iArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(iArr, iArr2, Math.min(iArr.length, iArr2.length));
        if (mismatch >= 0) {
            return Integer.compareUnsigned(iArr[mismatch], iArr2[mismatch]);
        }
        return iArr.length - iArr2.length;
    }

    public static int compareUnsigned(int[] iArr, int i, int i2, int[] iArr2, int i3, int i4) {
        rangeCheck(iArr.length, i, i2);
        rangeCheck(iArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(iArr, i, iArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Integer.compareUnsigned(iArr[i + mismatch], iArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compare(long[] jArr, long[] jArr2) {
        if (jArr == jArr2) {
            return 0;
        }
        if (jArr == null || jArr2 == null) {
            return jArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(jArr, jArr2, Math.min(jArr.length, jArr2.length));
        if (mismatch >= 0) {
            return Long.compare(jArr[mismatch], jArr2[mismatch]);
        }
        return jArr.length - jArr2.length;
    }

    public static int compare(long[] jArr, int i, int i2, long[] jArr2, int i3, int i4) {
        rangeCheck(jArr.length, i, i2);
        rangeCheck(jArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(jArr, i, jArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Long.compare(jArr[i + mismatch], jArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compareUnsigned(long[] jArr, long[] jArr2) {
        if (jArr == jArr2) {
            return 0;
        }
        if (jArr == null || jArr2 == null) {
            return jArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(jArr, jArr2, Math.min(jArr.length, jArr2.length));
        if (mismatch >= 0) {
            return Long.compareUnsigned(jArr[mismatch], jArr2[mismatch]);
        }
        return jArr.length - jArr2.length;
    }

    public static int compareUnsigned(long[] jArr, int i, int i2, long[] jArr2, int i3, int i4) {
        rangeCheck(jArr.length, i, i2);
        rangeCheck(jArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(jArr, i, jArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Long.compareUnsigned(jArr[i + mismatch], jArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compare(float[] fArr, float[] fArr2) {
        if (fArr == fArr2) {
            return 0;
        }
        if (fArr == null || fArr2 == null) {
            return fArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(fArr, fArr2, Math.min(fArr.length, fArr2.length));
        if (mismatch >= 0) {
            return Float.compare(fArr[mismatch], fArr2[mismatch]);
        }
        return fArr.length - fArr2.length;
    }

    public static int compare(float[] fArr, int i, int i2, float[] fArr2, int i3, int i4) {
        rangeCheck(fArr.length, i, i2);
        rangeCheck(fArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(fArr, i, fArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Float.compare(fArr[i + mismatch], fArr2[i3 + mismatch]) : i5 - i6;
    }

    public static int compare(double[] dArr, double[] dArr2) {
        if (dArr == dArr2) {
            return 0;
        }
        if (dArr == null || dArr2 == null) {
            return dArr == null ? -1 : 1;
        }
        int mismatch = ArraysSupport.mismatch(dArr, dArr2, Math.min(dArr.length, dArr2.length));
        if (mismatch >= 0) {
            return Double.compare(dArr[mismatch], dArr2[mismatch]);
        }
        return dArr.length - dArr2.length;
    }

    public static int compare(double[] dArr, int i, int i2, double[] dArr2, int i3, int i4) {
        rangeCheck(dArr.length, i, i2);
        rangeCheck(dArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int mismatch = ArraysSupport.mismatch(dArr, i, dArr2, i3, Math.min(i5, i6));
        return mismatch >= 0 ? Double.compare(dArr[i + mismatch], dArr2[i3 + mismatch]) : i5 - i6;
    }

    public static <T extends Comparable<? super T>> int compare(T[] tArr, T[] tArr2) {
        if (tArr == tArr2) {
            return 0;
        }
        if (tArr != null && tArr2 != null) {
            int min = Math.min(tArr.length, tArr2.length);
            for (int i = 0; i < min; i++) {
                T t = tArr[i];
                T t2 = tArr2[i];
                if (t != t2) {
                    if (t != null && t2 != null) {
                        int compareTo = t.compareTo(t2);
                        if (compareTo != 0) {
                            return compareTo;
                        }
                    } else if (t == null) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
            return tArr.length - tArr2.length;
        } else if (tArr == null) {
            return -1;
        } else {
            return 1;
        }
    }

    public static <T extends Comparable<? super T>> int compare(T[] tArr, int i, int i2, T[] tArr2, int i3, int i4) {
        rangeCheck(tArr.length, i, i2);
        rangeCheck(tArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int i7 = 0;
        while (i7 < min) {
            int i8 = i + 1;
            T t = tArr[i];
            int i9 = i3 + 1;
            T t2 = tArr2[i3];
            if (t != t2) {
                if (t == null || t2 == null) {
                    return t == null ? -1 : 1;
                }
                int compareTo = t.compareTo(t2);
                if (compareTo != 0) {
                    return compareTo;
                }
            }
            i7++;
            i = i8;
            i3 = i9;
        }
        return i5 - i6;
    }

    public static <T> int compare(T[] tArr, T[] tArr2, Comparator<? super T> comparator) {
        int compare;
        Objects.requireNonNull(comparator);
        if (tArr == tArr2) {
            return 0;
        }
        if (tArr == null || tArr2 == null) {
            return tArr == null ? -1 : 1;
        }
        int min = Math.min(tArr.length, tArr2.length);
        for (int i = 0; i < min; i++) {
            T t = tArr[i];
            T t2 = tArr2[i];
            if (t != t2 && (compare = comparator.compare(t, t2)) != 0) {
                return compare;
            }
        }
        return tArr.length - tArr2.length;
    }

    public static <T> int compare(T[] tArr, int i, int i2, T[] tArr2, int i3, int i4, Comparator<? super T> comparator) {
        int compare;
        Objects.requireNonNull(comparator);
        rangeCheck(tArr.length, i, i2);
        rangeCheck(tArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int i7 = 0;
        while (i7 < min) {
            int i8 = i + 1;
            T t = tArr[i];
            int i9 = i3 + 1;
            T t2 = tArr2[i3];
            if (t != t2 && (compare = comparator.compare(t, t2)) != 0) {
                return compare;
            }
            i7++;
            i = i8;
            i3 = i9;
        }
        return i5 - i6;
    }

    public static int mismatch(boolean[] zArr, boolean[] zArr2) {
        int min = Math.min(zArr.length, zArr2.length);
        if (zArr == zArr2) {
            return -1;
        }
        int mismatch = ArraysSupport.mismatch(zArr, zArr2, min);
        return (mismatch >= 0 || zArr.length == zArr2.length) ? mismatch : min;
    }

    public static int mismatch(boolean[] zArr, int i, int i2, boolean[] zArr2, int i3, int i4) {
        rangeCheck(zArr.length, i, i2);
        rangeCheck(zArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int mismatch = ArraysSupport.mismatch(zArr, i, zArr2, i3, min);
        return (mismatch >= 0 || i5 == i6) ? mismatch : min;
    }

    public static int mismatch(byte[] bArr, byte[] bArr2) {
        int min = Math.min(bArr.length, bArr2.length);
        if (bArr == bArr2) {
            return -1;
        }
        int mismatch = ArraysSupport.mismatch(bArr, bArr2, min);
        return (mismatch >= 0 || bArr.length == bArr2.length) ? mismatch : min;
    }

    public static int mismatch(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        rangeCheck(bArr.length, i, i2);
        rangeCheck(bArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int mismatch = ArraysSupport.mismatch(bArr, i, bArr2, i3, min);
        return (mismatch >= 0 || i5 == i6) ? mismatch : min;
    }

    public static int mismatch(char[] cArr, char[] cArr2) {
        int min = Math.min(cArr.length, cArr2.length);
        if (cArr == cArr2) {
            return -1;
        }
        int mismatch = ArraysSupport.mismatch(cArr, cArr2, min);
        return (mismatch >= 0 || cArr.length == cArr2.length) ? mismatch : min;
    }

    public static int mismatch(char[] cArr, int i, int i2, char[] cArr2, int i3, int i4) {
        rangeCheck(cArr.length, i, i2);
        rangeCheck(cArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int mismatch = ArraysSupport.mismatch(cArr, i, cArr2, i3, min);
        return (mismatch >= 0 || i5 == i6) ? mismatch : min;
    }

    public static int mismatch(short[] sArr, short[] sArr2) {
        int min = Math.min(sArr.length, sArr2.length);
        if (sArr == sArr2) {
            return -1;
        }
        int mismatch = ArraysSupport.mismatch(sArr, sArr2, min);
        return (mismatch >= 0 || sArr.length == sArr2.length) ? mismatch : min;
    }

    public static int mismatch(short[] sArr, int i, int i2, short[] sArr2, int i3, int i4) {
        rangeCheck(sArr.length, i, i2);
        rangeCheck(sArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int mismatch = ArraysSupport.mismatch(sArr, i, sArr2, i3, min);
        return (mismatch >= 0 || i5 == i6) ? mismatch : min;
    }

    public static int mismatch(int[] iArr, int[] iArr2) {
        int min = Math.min(iArr.length, iArr2.length);
        if (iArr == iArr2) {
            return -1;
        }
        int mismatch = ArraysSupport.mismatch(iArr, iArr2, min);
        return (mismatch >= 0 || iArr.length == iArr2.length) ? mismatch : min;
    }

    public static int mismatch(int[] iArr, int i, int i2, int[] iArr2, int i3, int i4) {
        rangeCheck(iArr.length, i, i2);
        rangeCheck(iArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int mismatch = ArraysSupport.mismatch(iArr, i, iArr2, i3, min);
        return (mismatch >= 0 || i5 == i6) ? mismatch : min;
    }

    public static int mismatch(long[] jArr, long[] jArr2) {
        int min = Math.min(jArr.length, jArr2.length);
        if (jArr == jArr2) {
            return -1;
        }
        int mismatch = ArraysSupport.mismatch(jArr, jArr2, min);
        return (mismatch >= 0 || jArr.length == jArr2.length) ? mismatch : min;
    }

    public static int mismatch(long[] jArr, int i, int i2, long[] jArr2, int i3, int i4) {
        rangeCheck(jArr.length, i, i2);
        rangeCheck(jArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int mismatch = ArraysSupport.mismatch(jArr, i, jArr2, i3, min);
        return (mismatch >= 0 || i5 == i6) ? mismatch : min;
    }

    public static int mismatch(float[] fArr, float[] fArr2) {
        int min = Math.min(fArr.length, fArr2.length);
        if (fArr == fArr2) {
            return -1;
        }
        int mismatch = ArraysSupport.mismatch(fArr, fArr2, min);
        return (mismatch >= 0 || fArr.length == fArr2.length) ? mismatch : min;
    }

    public static int mismatch(float[] fArr, int i, int i2, float[] fArr2, int i3, int i4) {
        rangeCheck(fArr.length, i, i2);
        rangeCheck(fArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int mismatch = ArraysSupport.mismatch(fArr, i, fArr2, i3, min);
        return (mismatch >= 0 || i5 == i6) ? mismatch : min;
    }

    public static int mismatch(double[] dArr, double[] dArr2) {
        int min = Math.min(dArr.length, dArr2.length);
        if (dArr == dArr2) {
            return -1;
        }
        int mismatch = ArraysSupport.mismatch(dArr, dArr2, min);
        return (mismatch >= 0 || dArr.length == dArr2.length) ? mismatch : min;
    }

    public static int mismatch(double[] dArr, int i, int i2, double[] dArr2, int i3, int i4) {
        rangeCheck(dArr.length, i, i2);
        rangeCheck(dArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int mismatch = ArraysSupport.mismatch(dArr, i, dArr2, i3, min);
        return (mismatch >= 0 || i5 == i6) ? mismatch : min;
    }

    public static int mismatch(Object[] objArr, Object[] objArr2) {
        int min = Math.min(objArr.length, objArr2.length);
        if (objArr == objArr2) {
            return -1;
        }
        for (int i = 0; i < min; i++) {
            if (!Objects.equals(objArr[i], objArr2[i])) {
                return i;
            }
        }
        if (objArr.length != objArr2.length) {
            return min;
        }
        return -1;
    }

    public static int mismatch(Object[] objArr, int i, int i2, Object[] objArr2, int i3, int i4) {
        rangeCheck(objArr.length, i, i2);
        rangeCheck(objArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int i7 = 0;
        while (i7 < min) {
            int i8 = i + 1;
            int i9 = i3 + 1;
            if (!Objects.equals(objArr[i], objArr2[i3])) {
                return i7;
            }
            i7++;
            i = i8;
            i3 = i9;
        }
        if (i5 != i6) {
            return min;
        }
        return -1;
    }

    public static <T> int mismatch(T[] tArr, T[] tArr2, Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        int min = Math.min(tArr.length, tArr2.length);
        if (tArr == tArr2) {
            return -1;
        }
        for (int i = 0; i < min; i++) {
            T t = tArr[i];
            T t2 = tArr2[i];
            if (t != t2 && comparator.compare(t, t2) != 0) {
                return i;
            }
        }
        if (tArr.length != tArr2.length) {
            return min;
        }
        return -1;
    }

    public static <T> int mismatch(T[] tArr, int i, int i2, T[] tArr2, int i3, int i4, Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        rangeCheck(tArr.length, i, i2);
        rangeCheck(tArr2.length, i3, i4);
        int i5 = i2 - i;
        int i6 = i4 - i3;
        int min = Math.min(i5, i6);
        int i7 = 0;
        while (i7 < min) {
            int i8 = i + 1;
            T t = tArr[i];
            int i9 = i3 + 1;
            T t2 = tArr2[i3];
            if (t != t2 && comparator.compare(t, t2) != 0) {
                return i7;
            }
            i7++;
            i = i8;
            i3 = i9;
        }
        if (i5 != i6) {
            return min;
        }
        return -1;
    }
}
