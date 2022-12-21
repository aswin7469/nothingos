package com.android.launcher3.util;

public interface FlagOp {
    public static final FlagOp NO_OP = new FlagOp$$ExternalSyntheticLambda1();

    static /* synthetic */ int lambda$static$0(int i) {
        return i;
    }

    int apply(int i);

    static /* synthetic */ int lambda$addFlag$1(FlagOp _this, int i, int i2) {
        return _this.apply(i2) | i;
    }

    FlagOp addFlag(int i) {
        return new FlagOp$$ExternalSyntheticLambda0(this, i);
    }

    static /* synthetic */ int lambda$removeFlag$2(FlagOp _this, int i, int i2) {
        return _this.apply(i2) & (~i);
    }

    FlagOp removeFlag(int i) {
        return new FlagOp$$ExternalSyntheticLambda2(this, i);
    }

    FlagOp setFlag(int i, boolean z) {
        return z ? addFlag(i) : removeFlag(i);
    }
}
