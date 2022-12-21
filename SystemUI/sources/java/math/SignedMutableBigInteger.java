package java.math;

class SignedMutableBigInteger extends MutableBigInteger {
    int sign = 1;

    SignedMutableBigInteger() {
    }

    SignedMutableBigInteger(int i) {
        super(i);
    }

    SignedMutableBigInteger(MutableBigInteger mutableBigInteger) {
        super(mutableBigInteger);
    }

    /* access modifiers changed from: package-private */
    public void signedAdd(SignedMutableBigInteger signedMutableBigInteger) {
        int i = this.sign;
        if (i == signedMutableBigInteger.sign) {
            add(signedMutableBigInteger);
        } else {
            this.sign = i * subtract(signedMutableBigInteger);
        }
    }

    /* access modifiers changed from: package-private */
    public void signedAdd(MutableBigInteger mutableBigInteger) {
        int i = this.sign;
        if (i == 1) {
            add(mutableBigInteger);
        } else {
            this.sign = i * subtract(mutableBigInteger);
        }
    }

    /* access modifiers changed from: package-private */
    public void signedSubtract(SignedMutableBigInteger signedMutableBigInteger) {
        int i = this.sign;
        if (i == signedMutableBigInteger.sign) {
            this.sign = i * subtract(signedMutableBigInteger);
        } else {
            add(signedMutableBigInteger);
        }
    }

    /* access modifiers changed from: package-private */
    public void signedSubtract(MutableBigInteger mutableBigInteger) {
        int i = this.sign;
        if (i == 1) {
            this.sign = i * subtract(mutableBigInteger);
        } else {
            add(mutableBigInteger);
        }
        if (this.intLen == 0) {
            this.sign = 1;
        }
    }

    public String toString() {
        return toBigInteger(this.sign).toString();
    }
}
