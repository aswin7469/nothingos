package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

public class DHParameterSpec implements AlgorithmParameterSpec {

    /* renamed from: g */
    private BigInteger f811g;

    /* renamed from: l */
    private int f812l;

    /* renamed from: p */
    private BigInteger f813p;

    public DHParameterSpec(BigInteger bigInteger, BigInteger bigInteger2) {
        this.f813p = bigInteger;
        this.f811g = bigInteger2;
        this.f812l = 0;
    }

    public DHParameterSpec(BigInteger bigInteger, BigInteger bigInteger2, int i) {
        this.f813p = bigInteger;
        this.f811g = bigInteger2;
        this.f812l = i;
    }

    public BigInteger getP() {
        return this.f813p;
    }

    public BigInteger getG() {
        return this.f811g;
    }

    public int getL() {
        return this.f812l;
    }
}
