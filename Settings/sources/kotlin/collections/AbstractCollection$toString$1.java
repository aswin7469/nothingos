package kotlin.collections;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: AbstractCollection.kt */
/* loaded from: classes2.dex */
final class AbstractCollection$toString$1 extends Lambda implements Function1<E, CharSequence> {
    final /* synthetic */ AbstractCollection this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AbstractCollection$toString$1(AbstractCollection abstractCollection) {
        super(1);
        this.this$0 = abstractCollection;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ CharSequence mo961invoke(Object obj) {
        return mo961invoke((AbstractCollection$toString$1) obj);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke */
    public final CharSequence mo961invoke(E e) {
        return e == this.this$0 ? "(this Collection)" : String.valueOf(e);
    }
}
