package kotlin.comparisons;

import java.util.Comparator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Comparisons.kt */
/* loaded from: classes2.dex */
public class ComparisonsKt__ComparisonsKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final <T> int compareValuesByImpl$ComparisonsKt__ComparisonsKt(T t, T t2, Function1<? super T, ? extends Comparable<?>>[] function1Arr) {
        for (Function1<? super T, ? extends Comparable<?>> function1 : function1Arr) {
            int compareValues = ComparisonsKt.compareValues(function1.mo1949invoke(t), function1.mo1949invoke(t2));
            if (compareValues != 0) {
                return compareValues;
            }
        }
        return 0;
    }

    public static <T extends Comparable<?>> int compareValues(@Nullable T t, @Nullable T t2) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return -1;
        }
        if (t2 != null) {
            return t.compareTo(t2);
        }
        return 1;
    }

    @NotNull
    public static <T> Comparator<T> compareBy(@NotNull final Function1<? super T, ? extends Comparable<?>>... selectors) {
        Intrinsics.checkNotNullParameter(selectors, "selectors");
        if (!(selectors.length > 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        return new Comparator<T>() { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$compareBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValuesByImpl$ComparisonsKt__ComparisonsKt;
                compareValuesByImpl$ComparisonsKt__ComparisonsKt = ComparisonsKt__ComparisonsKt.compareValuesByImpl$ComparisonsKt__ComparisonsKt(t, t2, selectors);
                return compareValuesByImpl$ComparisonsKt__ComparisonsKt;
            }
        };
    }
}
