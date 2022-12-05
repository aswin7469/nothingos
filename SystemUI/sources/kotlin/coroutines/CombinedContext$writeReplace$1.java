package kotlin.coroutines;

import kotlin.Unit;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref$IntRef;
import org.jetbrains.annotations.NotNull;
/* compiled from: CoroutineContextImpl.kt */
/* loaded from: classes2.dex */
final class CombinedContext$writeReplace$1 extends Lambda implements Function2<Unit, CoroutineContext.Element, Unit> {
    final /* synthetic */ CoroutineContext[] $elements;
    final /* synthetic */ Ref$IntRef $index;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CombinedContext$writeReplace$1(CoroutineContext[] coroutineContextArr, Ref$IntRef ref$IntRef) {
        super(2);
        this.$elements = coroutineContextArr;
        this.$index = ref$IntRef;
    }

    @Override // kotlin.jvm.functions.Function2
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1950invoke(Unit unit, CoroutineContext.Element element) {
        invoke2(unit, element);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(@NotNull Unit unit, @NotNull CoroutineContext.Element element) {
        Intrinsics.checkNotNullParameter(unit, "<anonymous parameter 0>");
        Intrinsics.checkNotNullParameter(element, "element");
        CoroutineContext[] coroutineContextArr = this.$elements;
        Ref$IntRef ref$IntRef = this.$index;
        int i = ref$IntRef.element;
        ref$IntRef.element = i + 1;
        coroutineContextArr[i] = element;
    }
}
