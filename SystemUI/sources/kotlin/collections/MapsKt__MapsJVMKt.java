package kotlin.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MapsJVM.kt */
/* loaded from: classes2.dex */
public class MapsKt__MapsJVMKt extends MapsKt__MapWithDefaultKt {
    public static int mapCapacity(int i) {
        if (i < 0) {
            return i;
        }
        if (i < 3) {
            return i + 1;
        }
        if (i >= 1073741824) {
            return Integer.MAX_VALUE;
        }
        return (int) ((i / 0.75f) + 1.0f);
    }

    @NotNull
    public static <K, V> Map<K, V> mapOf(@NotNull Pair<? extends K, ? extends V> pair) {
        Intrinsics.checkNotNullParameter(pair, "pair");
        Map<K, V> singletonMap = Collections.singletonMap(pair.getFirst(), pair.getSecond());
        Intrinsics.checkNotNullExpressionValue(singletonMap, "java.util.Collections.si…(pair.first, pair.second)");
        return singletonMap;
    }

    @NotNull
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> toSortedMap(@NotNull Map<? extends K, ? extends V> toSortedMap) {
        Intrinsics.checkNotNullParameter(toSortedMap, "$this$toSortedMap");
        return new TreeMap(toSortedMap);
    }

    @NotNull
    public static <K, V> SortedMap<K, V> toSortedMap(@NotNull Map<? extends K, ? extends V> toSortedMap, @NotNull Comparator<? super K> comparator) {
        Intrinsics.checkNotNullParameter(toSortedMap, "$this$toSortedMap");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        TreeMap treeMap = new TreeMap(comparator);
        treeMap.putAll(toSortedMap);
        return treeMap;
    }

    @NotNull
    public static final <K, V> Map<K, V> toSingletonMap(@NotNull Map<? extends K, ? extends V> toSingletonMap) {
        Intrinsics.checkNotNullParameter(toSingletonMap, "$this$toSingletonMap");
        Map.Entry<? extends K, ? extends V> next = toSingletonMap.entrySet().iterator().next();
        Map<K, V> singletonMap = Collections.singletonMap(next.getKey(), next.getValue());
        Intrinsics.checkNotNullExpressionValue(singletonMap, "with(entries.iterator().…ingletonMap(key, value) }");
        return singletonMap;
    }
}
