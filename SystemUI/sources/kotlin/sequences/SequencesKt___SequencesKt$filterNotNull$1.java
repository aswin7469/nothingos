package kotlin.sequences;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.Nullable;
/* compiled from: _Sequences.kt */
/* loaded from: classes2.dex */
final class SequencesKt___SequencesKt$filterNotNull$1 extends Lambda implements Function1<T, Boolean> {
    public static final SequencesKt___SequencesKt$filterNotNull$1 INSTANCE = new SequencesKt___SequencesKt$filterNotNull$1();

    SequencesKt___SequencesKt$filterNotNull$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo1949invoke(Object obj) {
        return Boolean.valueOf(mo1949invoke((SequencesKt___SequencesKt$filterNotNull$1) obj));
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke  reason: collision with other method in class */
    public final Boolean mo1949invoke(@Nullable T t) {
        return t == 0 ? 1 : null;
    }
}
