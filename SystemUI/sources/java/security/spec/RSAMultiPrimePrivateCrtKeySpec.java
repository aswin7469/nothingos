package java.security.spec;

import java.math.BigInteger;

public class RSAMultiPrimePrivateCrtKeySpec extends RSAPrivateKeySpec {
    private final BigInteger crtCoefficient;
    private final RSAOtherPrimeInfo[] otherPrimeInfo;
    private final BigInteger primeExponentP;
    private final BigInteger primeExponentQ;
    private final BigInteger primeP;
    private final BigInteger primeQ;
    private final BigInteger publicExponent;

    public RSAMultiPrimePrivateCrtKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6, BigInteger bigInteger7, BigInteger bigInteger8, RSAOtherPrimeInfo[] rSAOtherPrimeInfoArr) {
        super(bigInteger, bigInteger3);
        if (bigInteger == null) {
            throw new NullPointerException("the modulus parameter must be non-null");
        } else if (bigInteger2 == null) {
            throw new NullPointerException("the publicExponent parameter must be non-null");
        } else if (bigInteger3 == null) {
            throw new NullPointerException("the privateExponent parameter must be non-null");
        } else if (bigInteger4 == null) {
            throw new NullPointerException("the primeP parameter must be non-null");
        } else if (bigInteger5 == null) {
            throw new NullPointerException("the primeQ parameter must be non-null");
        } else if (bigInteger6 == null) {
            throw new NullPointerException("the primeExponentP parameter must be non-null");
        } else if (bigInteger7 == null) {
            throw new NullPointerException("the primeExponentQ parameter must be non-null");
        } else if (bigInteger8 != null) {
            this.publicExponent = bigInteger2;
            this.primeP = bigInteger4;
            this.primeQ = bigInteger5;
            this.primeExponentP = bigInteger6;
            this.primeExponentQ = bigInteger7;
            this.crtCoefficient = bigInteger8;
            if (rSAOtherPrimeInfoArr == null) {
                this.otherPrimeInfo = null;
            } else if (rSAOtherPrimeInfoArr.length != 0) {
                this.otherPrimeInfo = (RSAOtherPrimeInfo[]) rSAOtherPrimeInfoArr.clone();
            } else {
                throw new IllegalArgumentException("the otherPrimeInfo parameter must not be empty");
            }
        } else {
            throw new NullPointerException("the crtCoefficient parameter must be non-null");
        }
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }

    public BigInteger getPrimeP() {
        return this.primeP;
    }

    public BigInteger getPrimeQ() {
        return this.primeQ;
    }

    public BigInteger getPrimeExponentP() {
        return this.primeExponentP;
    }

    public BigInteger getPrimeExponentQ() {
        return this.primeExponentQ;
    }

    public BigInteger getCrtCoefficient() {
        return this.crtCoefficient;
    }

    public RSAOtherPrimeInfo[] getOtherPrimeInfo() {
        RSAOtherPrimeInfo[] rSAOtherPrimeInfoArr = this.otherPrimeInfo;
        if (rSAOtherPrimeInfoArr == null) {
            return null;
        }
        return (RSAOtherPrimeInfo[]) rSAOtherPrimeInfoArr.clone();
    }
}
