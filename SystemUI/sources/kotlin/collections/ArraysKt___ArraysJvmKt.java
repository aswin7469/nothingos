package kotlin.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: _ArraysJvm.kt */
/* loaded from: classes2.dex */
public class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt {
    @NotNull
    public static <T> List<T> asList(@NotNull T[] asList) {
        Intrinsics.checkNotNullParameter(asList, "$this$asList");
        List<T> asList2 = ArraysUtilJVM.asList(asList);
        Intrinsics.checkNotNullExpressionValue(asList2, "ArraysUtilJVM.asList(this)");
        return asList2;
    }

    public static /* synthetic */ Object[] copyInto$default(Object[] objArr, Object[] objArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = objArr.length;
        }
        return copyInto(objArr, objArr2, i, i2, i3);
    }

    @NotNull
    public static final <T> T[] copyInto(@NotNull T[] copyInto, @NotNull T[] destination, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(copyInto, "$this$copyInto");
        Intrinsics.checkNotNullParameter(destination, "destination");
        System.arraycopy(copyInto, i2, destination, i, i3 - i2);
        return destination;
    }

    public static /* synthetic */ byte[] copyInto$default(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, Object obj) {
        byte[] copyInto;
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = bArr.length;
        }
        copyInto = copyInto(bArr, bArr2, i, i2, i3);
        return copyInto;
    }

    @NotNull
    public static byte[] copyInto(@NotNull byte[] copyInto, @NotNull byte[] destination, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(copyInto, "$this$copyInto");
        Intrinsics.checkNotNullParameter(destination, "destination");
        System.arraycopy(copyInto, i2, destination, i, i3 - i2);
        return destination;
    }

    public static /* synthetic */ float[] copyInto$default(float[] fArr, float[] fArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = fArr.length;
        }
        return copyInto(fArr, fArr2, i, i2, i3);
    }

    @NotNull
    public static final float[] copyInto(@NotNull float[] copyInto, @NotNull float[] destination, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(copyInto, "$this$copyInto");
        Intrinsics.checkNotNullParameter(destination, "destination");
        System.arraycopy(copyInto, i2, destination, i, i3 - i2);
        return destination;
    }

    @NotNull
    public static byte[] copyOfRange(@NotNull byte[] copyOfRangeImpl, int i, int i2) {
        Intrinsics.checkNotNullParameter(copyOfRangeImpl, "$this$copyOfRangeImpl");
        ArraysKt__ArraysJVMKt.copyOfRangeToIndexCheck(i2, copyOfRangeImpl.length);
        byte[] copyOfRange = Arrays.copyOfRange(copyOfRangeImpl, i, i2);
        Intrinsics.checkNotNullExpressionValue(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    public static final <T> void fill(@NotNull T[] fill, T t, int i, int i2) {
        Intrinsics.checkNotNullParameter(fill, "$this$fill");
        Arrays.fill(fill, i, i2, t);
    }

    @NotNull
    public static int[] plus(@NotNull int[] plus, @NotNull int[] elements) {
        Intrinsics.checkNotNullParameter(plus, "$this$plus");
        Intrinsics.checkNotNullParameter(elements, "elements");
        int length = plus.length;
        int length2 = elements.length;
        int[] result = Arrays.copyOf(plus, length + length2);
        System.arraycopy(elements, 0, result, length, length2);
        Intrinsics.checkNotNullExpressionValue(result, "result");
        return result;
    }

    public static final <T> void sort(@NotNull T[] sort) {
        Intrinsics.checkNotNullParameter(sort, "$this$sort");
        if (sort.length > 1) {
            Arrays.sort(sort);
        }
    }

    public static <T> void sortWith(@NotNull T[] sortWith, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(sortWith, "$this$sortWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        if (sortWith.length > 1) {
            Arrays.sort(sortWith, comparator);
        }
    }
}
