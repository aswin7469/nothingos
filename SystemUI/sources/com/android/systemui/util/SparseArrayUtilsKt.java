package com.android.systemui.util;

import android.util.SparseArray;
import java.util.Iterator;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.Grouping;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

@Metadata(mo65042d1 = {"\u0000:\n\u0000\n\u0002\u0010$\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\"\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004\u001a=\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0004\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00062\u0014\b\u0004\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u00020\bH\bø\u0001\u0000¢\u0006\u0002\u0010\t\u001aa\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0004\"\u0004\b\u0000\u0010\u0003\"\u0004\b\u0001\u0010\u000b*\u000e\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u00020\f2\u0006\u0010\r\u001a\u0002H\u000b2\b\b\u0002\u0010\u000e\u001a\u00020\u00022\u001a\b\u0004\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u000b0\u0010H\bø\u0001\u0000¢\u0006\u0002\u0010\u0011\u001a2\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0004\"\u0004\b\u0000\u0010\u0003*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H\u00030\u00140\u00132\b\b\u0002\u0010\u000e\u001a\u00020\u0002\u0002\u0007\n\u0005\b20\u0001¨\u0006\u0015"}, mo65043d2 = {"asMap", "", "", "T", "Landroid/util/SparseArray;", "associateByToSparseArray", "", "keySelector", "Lkotlin/Function1;", "([Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Landroid/util/SparseArray;", "foldToSparseArray", "R", "Lkotlin/collections/Grouping;", "initial", "size", "operation", "Lkotlin/Function2;", "(Lkotlin/collections/Grouping;Ljava/lang/Object;ILkotlin/jvm/functions/Function2;)Landroid/util/SparseArray;", "toSparseArray", "Lkotlin/sequences/Sequence;", "Lkotlin/Pair;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SparseArrayUtils.kt */
public final class SparseArrayUtilsKt {
    public static /* synthetic */ SparseArray toSparseArray$default(Sequence sequence, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = -1;
        }
        return toSparseArray(sequence, i);
    }

    public static final <T> SparseArray<T> toSparseArray(Sequence<? extends Pair<Integer, ? extends T>> sequence, int i) {
        SparseArray<T> sparseArray;
        Intrinsics.checkNotNullParameter(sequence, "<this>");
        if (i < 0) {
            sparseArray = new SparseArray<>();
        } else {
            sparseArray = new SparseArray<>(i);
        }
        for (Pair pair : sequence) {
            sparseArray.put(((Number) pair.component1()).intValue(), pair.component2());
        }
        return sparseArray;
    }

    public static final <T> SparseArray<T> associateByToSparseArray(T[] tArr, Function1<? super T, Integer> function1) {
        Intrinsics.checkNotNullParameter(tArr, "<this>");
        Intrinsics.checkNotNullParameter(function1, "keySelector");
        SparseArray<T> sparseArray = new SparseArray<>(tArr.length);
        for (T t : tArr) {
            sparseArray.put(function1.invoke(t).intValue(), t);
        }
        return sparseArray;
    }

    public static /* synthetic */ SparseArray foldToSparseArray$default(Grouping grouping, Object obj, int i, Function2 function2, int i2, Object obj2) {
        SparseArray sparseArray;
        if ((i2 & 2) != 0) {
            i = -1;
        }
        Intrinsics.checkNotNullParameter(grouping, "<this>");
        Intrinsics.checkNotNullParameter(function2, "operation");
        if (i < 0) {
            sparseArray = new SparseArray();
        } else {
            sparseArray = new SparseArray(i);
        }
        Iterator sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            Object next = sourceIterator.next();
            int intValue = ((Number) grouping.keyOf(next)).intValue();
            Object obj3 = sparseArray.get(intValue);
            if (obj3 == null) {
                obj3 = obj;
            }
            sparseArray.put(intValue, function2.invoke(obj3, next));
        }
        return sparseArray;
    }

    public static final <T, R> SparseArray<R> foldToSparseArray(Grouping<T, Integer> grouping, R r, int i, Function2<? super R, ? super T, ? extends R> function2) {
        SparseArray<R> sparseArray;
        Intrinsics.checkNotNullParameter(grouping, "<this>");
        Intrinsics.checkNotNullParameter(function2, "operation");
        if (i < 0) {
            sparseArray = new SparseArray<>();
        } else {
            sparseArray = new SparseArray<>(i);
        }
        Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            T next = sourceIterator.next();
            int intValue = grouping.keyOf(next).intValue();
            R r2 = sparseArray.get(intValue);
            if (r2 == null) {
                r2 = r;
            }
            sparseArray.put(intValue, function2.invoke(r2, next));
        }
        return sparseArray;
    }

    public static final <T> Map<Integer, T> asMap(SparseArray<T> sparseArray) {
        Intrinsics.checkNotNullParameter(sparseArray, "<this>");
        return new SparseArrayMapWrapper<>(sparseArray);
    }
}
