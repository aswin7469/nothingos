package java.security.spec;

import java.math.BigInteger;
import java.security.interfaces.DSAParams;

public class DSAParameterSpec implements AlgorithmParameterSpec, DSAParams {

    /* renamed from: g */
    BigInteger f201g;

    /* renamed from: p */
    BigInteger f202p;

    /* renamed from: q */
    BigInteger f203q;

    public DSAParameterSpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f202p = bigInteger;
        this.f203q = bigInteger2;
        this.f201g = bigInteger3;
    }

    public BigInteger getP() {
        return this.f202p;
    }

    public BigInteger getQ() {
        return this.f203q;
    }

    public BigInteger getG() {
        return this.f201g;
    }
}
