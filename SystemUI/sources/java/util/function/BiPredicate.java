package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface BiPredicate<T, U> {
    boolean test(T t, U u);

    BiPredicate<T, U> and(BiPredicate<? super T, ? super U> biPredicate) {
        Objects.requireNonNull(biPredicate);
        return new BiPredicate$$ExternalSyntheticLambda0(this, biPredicate);
    }

    static /* synthetic */ boolean lambda$and$0(BiPredicate _this, BiPredicate biPredicate, Object obj, Object obj2) {
        return _this.test(obj, obj2) && biPredicate.test(obj, obj2);
    }

    static /* synthetic */ boolean lambda$negate$1(BiPredicate _this, Object obj, Object obj2) {
        return !_this.test(obj, obj2);
    }

    BiPredicate<T, U> negate() {
        return new BiPredicate$$ExternalSyntheticLambda1(this);
    }

    /* renamed from: or */
    BiPredicate<T, U> mo63096or(BiPredicate<? super T, ? super U> biPredicate) {
        Objects.requireNonNull(biPredicate);
        return new BiPredicate$$ExternalSyntheticLambda2(this, biPredicate);
    }

    static /* synthetic */ boolean lambda$or$2(BiPredicate _this, BiPredicate biPredicate, Object obj, Object obj2) {
        return _this.test(obj, obj2) || biPredicate.test(obj, obj2);
    }
}
