package kotlin.collections;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Maps.kt */
/* loaded from: classes2.dex */
public class MapsKt__MapsKt extends MapsKt__MapsJVMKt {
    @NotNull
    public static <K, V> Map<K, V> emptyMap() {
        EmptyMap emptyMap = EmptyMap.INSTANCE;
        Objects.requireNonNull(emptyMap, "null cannot be cast to non-null type kotlin.collections.Map<K, V>");
        return emptyMap;
    }

    @NotNull
    public static <K, V> Map<K, V> mapOf(@NotNull Pair<? extends K, ? extends V>... pairs) {
        Map<K, V> emptyMap;
        int mapCapacity;
        Intrinsics.checkNotNullParameter(pairs, "pairs");
        if (pairs.length > 0) {
            mapCapacity = MapsKt__MapsJVMKt.mapCapacity(pairs.length);
            return toMap(pairs, new LinkedHashMap(mapCapacity));
        }
        emptyMap = emptyMap();
        return emptyMap;
    }

    @NotNull
    public static <K, V> Map<K, V> mutableMapOf(@NotNull Pair<? extends K, ? extends V>... pairs) {
        int mapCapacity;
        Intrinsics.checkNotNullParameter(pairs, "pairs");
        mapCapacity = MapsKt__MapsJVMKt.mapCapacity(pairs.length);
        LinkedHashMap linkedHashMap = new LinkedHashMap(mapCapacity);
        putAll(linkedHashMap, pairs);
        return linkedHashMap;
    }

    public static <K, V> V getValue(@NotNull Map<K, ? extends V> getValue, K k) {
        Intrinsics.checkNotNullParameter(getValue, "$this$getValue");
        return (V) MapsKt__MapWithDefaultKt.getOrImplicitDefaultNullable(getValue, k);
    }

    public static final <K, V> void putAll(@NotNull Map<? super K, ? super V> putAll, @NotNull Pair<? extends K, ? extends V>[] pairs) {
        Intrinsics.checkNotNullParameter(putAll, "$this$putAll");
        Intrinsics.checkNotNullParameter(pairs, "pairs");
        for (Pair<? extends K, ? extends V> pair : pairs) {
            putAll.put((K) pair.component1(), (V) pair.component2());
        }
    }

    public static final <K, V> void putAll(@NotNull Map<? super K, ? super V> putAll, @NotNull Iterable<? extends Pair<? extends K, ? extends V>> pairs) {
        Intrinsics.checkNotNullParameter(putAll, "$this$putAll");
        Intrinsics.checkNotNullParameter(pairs, "pairs");
        for (Pair<? extends K, ? extends V> pair : pairs) {
            putAll.put((K) pair.component1(), (V) pair.component2());
        }
    }

    public static final <K, V> void putAll(@NotNull Map<? super K, ? super V> putAll, @NotNull Sequence<? extends Pair<? extends K, ? extends V>> pairs) {
        Intrinsics.checkNotNullParameter(putAll, "$this$putAll");
        Intrinsics.checkNotNullParameter(pairs, "pairs");
        for (Pair<? extends K, ? extends V> pair : pairs) {
            putAll.put((K) pair.component1(), (V) pair.component2());
        }
    }

    @NotNull
    public static <K, V> Map<K, V> toMap(@NotNull Iterable<? extends Pair<? extends K, ? extends V>> toMap) {
        Map<K, V> emptyMap;
        Map<K, V> mapOf;
        int mapCapacity;
        Intrinsics.checkNotNullParameter(toMap, "$this$toMap");
        if (toMap instanceof Collection) {
            Collection collection = (Collection) toMap;
            int size = collection.size();
            if (size == 0) {
                emptyMap = emptyMap();
                return emptyMap;
            } else if (size == 1) {
                mapOf = MapsKt__MapsJVMKt.mapOf(toMap instanceof List ? (Pair<? extends K, ? extends V>) ((List) toMap).get(0) : toMap.iterator().next());
                return mapOf;
            } else {
                mapCapacity = MapsKt__MapsJVMKt.mapCapacity(collection.size());
                return toMap(toMap, new LinkedHashMap(mapCapacity));
            }
        }
        return optimizeReadOnlyMap(toMap(toMap, new LinkedHashMap()));
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull Iterable<? extends Pair<? extends K, ? extends V>> toMap, @NotNull M destination) {
        Intrinsics.checkNotNullParameter(toMap, "$this$toMap");
        Intrinsics.checkNotNullParameter(destination, "destination");
        putAll(destination, toMap);
        return destination;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull Pair<? extends K, ? extends V>[] toMap, @NotNull M destination) {
        Intrinsics.checkNotNullParameter(toMap, "$this$toMap");
        Intrinsics.checkNotNullParameter(destination, "destination");
        putAll(destination, toMap);
        return destination;
    }

    @NotNull
    public static <K, V> Map<K, V> toMap(@NotNull Sequence<? extends Pair<? extends K, ? extends V>> toMap) {
        Intrinsics.checkNotNullParameter(toMap, "$this$toMap");
        return optimizeReadOnlyMap(toMap(toMap, new LinkedHashMap()));
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull Sequence<? extends Pair<? extends K, ? extends V>> toMap, @NotNull M destination) {
        Intrinsics.checkNotNullParameter(toMap, "$this$toMap");
        Intrinsics.checkNotNullParameter(destination, "destination");
        putAll(destination, toMap);
        return destination;
    }

    @NotNull
    public static <K, V> Map<K, V> toMutableMap(@NotNull Map<? extends K, ? extends V> toMutableMap) {
        Intrinsics.checkNotNullParameter(toMutableMap, "$this$toMutableMap");
        return new LinkedHashMap(toMutableMap);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public static final <K, V> Map<K, V> optimizeReadOnlyMap(@NotNull Map<K, ? extends V> optimizeReadOnlyMap) {
        Map<K, V> emptyMap;
        Intrinsics.checkNotNullParameter(optimizeReadOnlyMap, "$this$optimizeReadOnlyMap");
        int size = optimizeReadOnlyMap.size();
        if (size != 0) {
            return size != 1 ? optimizeReadOnlyMap : MapsKt__MapsJVMKt.toSingletonMap(optimizeReadOnlyMap);
        }
        emptyMap = emptyMap();
        return emptyMap;
    }
}
