package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

public class DHParameterSpec implements AlgorithmParameterSpec {

    /* renamed from: g */
    private BigInteger f813g;

    /* renamed from: l */
    private int f814l;

    /* renamed from: p */
    private BigInteger f815p;

    public DHParameterSpec(BigInteger bigInteger, BigInteger bigInteger2) {
        this.f815p = bigInteger;
        this.f813g = bigInteger2;
        this.f814l = 0;
    }

    public DHParameterSpec(BigInteger bigInteger, BigInteger bigInteger2, int i) {
        this.f815p = bigInteger;
        this.f813g = bigInteger2;
        this.f814l = i;
    }

    public BigInteger getP() {
        return this.f815p;
    }

    public BigInteger getG() {
        return this.f813g;
    }

    public int getL() {
        return this.f814l;
    }
}
