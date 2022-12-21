package java.security.spec;

public class PKCS8EncodedKeySpec extends EncodedKeySpec {
    public final String getFormat() {
        return "PKCS#8";
    }

    public PKCS8EncodedKeySpec(byte[] bArr) {
        super(bArr);
    }

    public byte[] getEncoded() {
        return super.getEncoded();
    }
}
