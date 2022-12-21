package java.security.spec;

import java.math.BigInteger;

public class RSAOtherPrimeInfo {
    private BigInteger crtCoefficient;
    private BigInteger prime;
    private BigInteger primeExponent;

    public RSAOtherPrimeInfo(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        if (bigInteger == null) {
            throw new NullPointerException("the prime parameter must be non-null");
        } else if (bigInteger2 == null) {
            throw new NullPointerException("the primeExponent parameter must be non-null");
        } else if (bigInteger3 != null) {
            this.prime = bigInteger;
            this.primeExponent = bigInteger2;
            this.crtCoefficient = bigInteger3;
        } else {
            throw new NullPointerException("the crtCoefficient parameter must be non-null");
        }
    }

    public final BigInteger getPrime() {
        return this.prime;
    }

    public final BigInteger getExponent() {
        return this.primeExponent;
    }

    public final BigInteger getCrtCoefficient() {
        return this.crtCoefficient;
    }
}
