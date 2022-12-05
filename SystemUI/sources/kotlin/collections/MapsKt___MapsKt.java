package kotlin.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _Maps.kt */
/* loaded from: classes2.dex */
public class MapsKt___MapsKt extends MapsKt__MapsKt {
    @NotNull
    public static <K, V> List<Pair<K, V>> toList(@NotNull Map<? extends K, ? extends V> toList) {
        List<Pair<K, V>> listOf;
        List<Pair<K, V>> emptyList;
        List<Pair<K, V>> emptyList2;
        Intrinsics.checkNotNullParameter(toList, "$this$toList");
        if (toList.size() == 0) {
            emptyList2 = CollectionsKt__CollectionsKt.emptyList();
            return emptyList2;
        }
        Iterator<Map.Entry<? extends K, ? extends V>> it = toList.entrySet().iterator();
        if (!it.hasNext()) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            return emptyList;
        }
        Map.Entry<? extends K, ? extends V> next = it.next();
        if (!it.hasNext()) {
            listOf = CollectionsKt__CollectionsJVMKt.listOf(new Pair(next.getKey(), next.getValue()));
            return listOf;
        }
        ArrayList arrayList = new ArrayList(toList.size());
        arrayList.add(new Pair(next.getKey(), next.getValue()));
        do {
            Map.Entry<? extends K, ? extends V> next2 = it.next();
            arrayList.add(new Pair(next2.getKey(), next2.getValue()));
        } while (it.hasNext());
        return arrayList;
    }

    @NotNull
    public static <K, V> Sequence<Map.Entry<K, V>> asSequence(@NotNull Map<? extends K, ? extends V> asSequence) {
        Sequence<Map.Entry<K, V>> asSequence2;
        Intrinsics.checkNotNullParameter(asSequence, "$this$asSequence");
        asSequence2 = CollectionsKt___CollectionsKt.asSequence(asSequence.entrySet());
        return asSequence2;
    }
}
