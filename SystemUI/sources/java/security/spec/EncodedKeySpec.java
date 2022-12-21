package java.security.spec;

public abstract class EncodedKeySpec implements KeySpec {
    private byte[] encodedKey;

    public abstract String getFormat();

    public EncodedKeySpec(byte[] bArr) {
        this.encodedKey = (byte[]) bArr.clone();
    }

    public byte[] getEncoded() {
        return (byte[]) this.encodedKey.clone();
    }
}
