package kotlin.sequences;

import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: _Sequences.kt */
/* loaded from: classes2.dex */
final class SequencesKt___SequencesKt$zipWithNext$1 extends Lambda implements Function2<T, T, Pair<? extends T, ? extends T>> {
    public static final SequencesKt___SequencesKt$zipWithNext$1 INSTANCE = new SequencesKt___SequencesKt$zipWithNext$1();

    SequencesKt___SequencesKt$zipWithNext$1() {
        super(2);
    }

    @Override // kotlin.jvm.functions.Function2
    @NotNull
    /* renamed from: invoke  reason: collision with other method in class */
    public final Pair<T, T> mo1950invoke(T t, T t2) {
        return TuplesKt.to(t, t2);
    }
}
