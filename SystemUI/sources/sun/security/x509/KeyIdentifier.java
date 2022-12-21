package sun.security.x509;

import java.p026io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;
import sun.misc.HexDumpEncoder;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class KeyIdentifier {
    private byte[] octetString;

    public KeyIdentifier(byte[] bArr) {
        this.octetString = (byte[]) bArr.clone();
    }

    public KeyIdentifier(DerValue derValue) throws IOException {
        this.octetString = derValue.getOctetString();
    }

    public KeyIdentifier(PublicKey publicKey) throws IOException {
        DerValue derValue = new DerValue(publicKey.getEncoded());
        if (derValue.tag == 48) {
            AlgorithmId.parse(derValue.data.getDerValue());
            byte[] byteArray = derValue.data.getUnalignedBitString().toByteArray();
            try {
                MessageDigest instance = MessageDigest.getInstance("SHA1");
                instance.update(byteArray);
                this.octetString = instance.digest();
            } catch (NoSuchAlgorithmException unused) {
                throw new IOException("SHA1 not supported");
            }
        } else {
            throw new IOException("PublicKey value is not a valid X.509 public key");
        }
    }

    public byte[] getIdentifier() {
        return (byte[]) this.octetString.clone();
    }

    public String toString() {
        HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
        return ("KeyIdentifier [\n" + hexDumpEncoder.encodeBuffer(this.octetString)) + "]\n";
    }

    /* access modifiers changed from: package-private */
    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putOctetString(this.octetString);
    }

    public int hashCode() {
        int i = 0;
        int i2 = 0;
        while (true) {
            byte[] bArr = this.octetString;
            if (i >= bArr.length) {
                return i2;
            }
            i2 += bArr[i] * i;
            i++;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyIdentifier)) {
            return false;
        }
        return Arrays.equals(this.octetString, ((KeyIdentifier) obj).octetString);
    }
}
