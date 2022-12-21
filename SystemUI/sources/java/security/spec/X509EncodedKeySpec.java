package java.security.spec;

public class X509EncodedKeySpec extends EncodedKeySpec {
    public final String getFormat() {
        return "X.509";
    }

    public X509EncodedKeySpec(byte[] bArr) {
        super(bArr);
    }

    public byte[] getEncoded() {
        return super.getEncoded();
    }
}
