package kotlin.collections;

import java.util.Map;
import kotlin.Pair;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class MapsKt extends MapsKt___MapsKt {
    @NotNull
    public static /* bridge */ /* synthetic */ <K, V> Sequence<Map.Entry<K, V>> asSequence(@NotNull Map<? extends K, ? extends V> map) {
        return MapsKt___MapsKt.asSequence(map);
    }

    public static /* bridge */ /* synthetic */ <K, V> V getValue(@NotNull Map<K, ? extends V> map, K k) {
        return (V) MapsKt__MapsKt.getValue(map, k);
    }

    public static /* bridge */ /* synthetic */ int mapCapacity(int i) {
        return MapsKt__MapsJVMKt.mapCapacity(i);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <K, V> Map<K, V> toMap(@NotNull Sequence<? extends Pair<? extends K, ? extends V>> sequence) {
        return MapsKt__MapsKt.toMap(sequence);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <K, V> Map<K, V> withDefault(@NotNull Map<K, ? extends V> map, @NotNull Function1<? super K, ? extends V> function1) {
        return MapsKt__MapWithDefaultKt.withDefault(map, function1);
    }
}
