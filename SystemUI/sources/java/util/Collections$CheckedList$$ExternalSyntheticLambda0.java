package java.util;

import java.util.Collections;
import java.util.function.UnaryOperator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collections$CheckedList$$ExternalSyntheticLambda0 implements UnaryOperator {
    public final /* synthetic */ Collections.CheckedList f$0;
    public final /* synthetic */ UnaryOperator f$1;

    public /* synthetic */ Collections$CheckedList$$ExternalSyntheticLambda0(Collections.CheckedList checkedList, UnaryOperator unaryOperator) {
        this.f$0 = checkedList;
        this.f$1 = unaryOperator;
    }

    public final Object apply(Object obj) {
        return this.f$0.m3735lambda$replaceAll$0$javautilCollections$CheckedList(this.f$1, obj);
    }
}
