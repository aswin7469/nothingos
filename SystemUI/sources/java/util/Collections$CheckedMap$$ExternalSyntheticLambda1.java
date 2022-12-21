package java.util;

import java.util.Collections;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collections$CheckedMap$$ExternalSyntheticLambda1 implements Function {
    public final /* synthetic */ Collections.CheckedMap f$0;
    public final /* synthetic */ Function f$1;

    public /* synthetic */ Collections$CheckedMap$$ExternalSyntheticLambda1(Collections.CheckedMap checkedMap, Function function) {
        this.f$0 = checkedMap;
        this.f$1 = function;
    }

    public final Object apply(Object obj) {
        return this.f$0.m3736lambda$computeIfAbsent$1$javautilCollections$CheckedMap(this.f$1, obj);
    }
}
