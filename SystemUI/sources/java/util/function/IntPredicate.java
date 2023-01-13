package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface IntPredicate {
    boolean test(int i);

    IntPredicate and(IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        return new IntPredicate$$ExternalSyntheticLambda2(this, intPredicate);
    }

    static /* synthetic */ boolean lambda$and$0(IntPredicate _this, IntPredicate intPredicate, int i) {
        return _this.test(i) && intPredicate.test(i);
    }

    static /* synthetic */ boolean lambda$negate$1(IntPredicate _this, int i) {
        return !_this.test(i);
    }

    IntPredicate negate() {
        return new IntPredicate$$ExternalSyntheticLambda0(this);
    }

    /* renamed from: or */
    IntPredicate mo63173or(IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        return new IntPredicate$$ExternalSyntheticLambda1(this, intPredicate);
    }

    static /* synthetic */ boolean lambda$or$2(IntPredicate _this, IntPredicate intPredicate, int i) {
        return _this.test(i) || intPredicate.test(i);
    }
}
