package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface DoublePredicate {
    boolean test(double d);

    DoublePredicate and(DoublePredicate doublePredicate) {
        Objects.requireNonNull(doublePredicate);
        return new DoublePredicate$$ExternalSyntheticLambda1(this, doublePredicate);
    }

    static /* synthetic */ boolean lambda$and$0(DoublePredicate _this, DoublePredicate doublePredicate, double d) {
        return _this.test(d) && doublePredicate.test(d);
    }

    static /* synthetic */ boolean lambda$negate$1(DoublePredicate _this, double d) {
        return !_this.test(d);
    }

    DoublePredicate negate() {
        return new DoublePredicate$$ExternalSyntheticLambda2(this);
    }

    /* renamed from: or */
    DoublePredicate mo63160or(DoublePredicate doublePredicate) {
        Objects.requireNonNull(doublePredicate);
        return new DoublePredicate$$ExternalSyntheticLambda0(this, doublePredicate);
    }

    static /* synthetic */ boolean lambda$or$2(DoublePredicate _this, DoublePredicate doublePredicate, double d) {
        return _this.test(d) || doublePredicate.test(d);
    }
}
