package java.security.spec;

import java.math.BigInteger;
import java.util.Arrays;

public class ECFieldF2m implements ECField {

    /* renamed from: ks */
    private int[] f212ks;

    /* renamed from: m */
    private int f213m;

    /* renamed from: rp */
    private BigInteger f214rp;

    public ECFieldF2m(int i) {
        if (i > 0) {
            this.f213m = i;
            this.f212ks = null;
            this.f214rp = null;
            return;
        }
        throw new IllegalArgumentException("m is not positive");
    }

    public ECFieldF2m(int i, BigInteger bigInteger) {
        this.f213m = i;
        this.f214rp = bigInteger;
        if (i > 0) {
            int bitCount = bigInteger.bitCount();
            if (!this.f214rp.testBit(0) || !this.f214rp.testBit(i) || !(bitCount == 3 || bitCount == 5)) {
                throw new IllegalArgumentException("rp does not represent a valid reduction polynomial");
            }
            BigInteger clearBit = this.f214rp.clearBit(0).clearBit(i);
            int[] iArr = new int[(bitCount - 2)];
            this.f212ks = iArr;
            for (int length = iArr.length - 1; length >= 0; length--) {
                int lowestSetBit = clearBit.getLowestSetBit();
                this.f212ks[length] = lowestSetBit;
                clearBit = clearBit.clearBit(lowestSetBit);
            }
            return;
        }
        throw new IllegalArgumentException("m is not positive");
    }

    public ECFieldF2m(int i, int[] iArr) {
        this.f213m = i;
        int[] iArr2 = (int[]) iArr.clone();
        this.f212ks = iArr2;
        if (i <= 0) {
            throw new IllegalArgumentException("m is not positive");
        } else if (iArr2.length == 1 || iArr2.length == 3) {
            int i2 = 0;
            int i3 = 0;
            while (true) {
                int[] iArr3 = this.f212ks;
                if (i3 < iArr3.length) {
                    int i4 = iArr3[i3];
                    if (i4 < 1 || i4 > i - 1) {
                    } else if (i3 == 0 || i4 < iArr3[i3 - 1]) {
                        i3++;
                    } else {
                        throw new IllegalArgumentException("values in ks are not in descending order");
                    }
                } else {
                    BigInteger bigInteger = BigInteger.ONE;
                    this.f214rp = bigInteger;
                    this.f214rp = bigInteger.setBit(i);
                    while (true) {
                        int[] iArr4 = this.f212ks;
                        if (i2 < iArr4.length) {
                            this.f214rp = this.f214rp.setBit(iArr4[i2]);
                            i2++;
                        } else {
                            return;
                        }
                    }
                }
            }
            throw new IllegalArgumentException("ks[" + i3 + "] is out of range");
        } else {
            throw new IllegalArgumentException("length of ks is neither 1 nor 3");
        }
    }

    public int getFieldSize() {
        return this.f213m;
    }

    public int getM() {
        return this.f213m;
    }

    public BigInteger getReductionPolynomial() {
        return this.f214rp;
    }

    public int[] getMidTermsOfReductionPolynomial() {
        int[] iArr = this.f212ks;
        if (iArr == null) {
            return null;
        }
        return (int[]) iArr.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ECFieldF2m)) {
            return false;
        }
        ECFieldF2m eCFieldF2m = (ECFieldF2m) obj;
        if (this.f213m != eCFieldF2m.f213m || !Arrays.equals(this.f212ks, eCFieldF2m.f212ks)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = this.f213m << 5;
        BigInteger bigInteger = this.f214rp;
        return i + (bigInteger == null ? 0 : bigInteger.hashCode());
    }
}
