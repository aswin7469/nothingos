package java.util.function;

import java.util.Objects;

@FunctionalInterface
public interface LongPredicate {
    boolean test(long j);

    LongPredicate and(LongPredicate longPredicate) {
        Objects.requireNonNull(longPredicate);
        return new LongPredicate$$ExternalSyntheticLambda1(this, longPredicate);
    }

    static /* synthetic */ boolean lambda$and$0(LongPredicate _this, LongPredicate longPredicate, long j) {
        return _this.test(j) && longPredicate.test(j);
    }

    static /* synthetic */ boolean lambda$negate$1(LongPredicate _this, long j) {
        return !_this.test(j);
    }

    LongPredicate negate() {
        return new LongPredicate$$ExternalSyntheticLambda2(this);
    }

    /* renamed from: or */
    LongPredicate mo63183or(LongPredicate longPredicate) {
        Objects.requireNonNull(longPredicate);
        return new LongPredicate$$ExternalSyntheticLambda0(this, longPredicate);
    }

    static /* synthetic */ boolean lambda$or$2(LongPredicate _this, LongPredicate longPredicate, long j) {
        return _this.test(j) || longPredicate.test(j);
    }
}
