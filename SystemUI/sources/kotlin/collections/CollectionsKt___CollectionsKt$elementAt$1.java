package kotlin.collections;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: _Collections.kt */
/* loaded from: classes2.dex */
final class CollectionsKt___CollectionsKt$elementAt$1 extends Lambda implements Function1<Integer, T> {
    final /* synthetic */ int $index;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CollectionsKt___CollectionsKt$elementAt$1(int i) {
        super(1);
        this.$index = i;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Object mo1949invoke(Integer num) {
        return invoke(num.intValue());
    }

    public final T invoke(int i) {
        throw new IndexOutOfBoundsException("Collection doesn't contain element at index " + this.$index + '.');
    }
}
