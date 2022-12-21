package java.security.spec;

import java.math.BigInteger;

public class RSAKeyGenParameterSpec implements AlgorithmParameterSpec {

    /* renamed from: F0 */
    public static final BigInteger f226F0 = BigInteger.valueOf(3);

    /* renamed from: F4 */
    public static final BigInteger f227F4 = BigInteger.valueOf(65537);
    private int keysize;
    private BigInteger publicExponent;

    public RSAKeyGenParameterSpec(int i, BigInteger bigInteger) {
        this.keysize = i;
        this.publicExponent = bigInteger;
    }

    public int getKeysize() {
        return this.keysize;
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
}
