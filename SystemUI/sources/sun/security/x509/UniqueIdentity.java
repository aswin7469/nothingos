package sun.security.x509;

import java.p026io.IOException;
import sun.security.util.BitArray;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class UniqueIdentity {

    /* renamed from: id */
    private BitArray f935id;

    public UniqueIdentity(BitArray bitArray) {
        this.f935id = bitArray;
    }

    public UniqueIdentity(byte[] bArr) {
        this.f935id = new BitArray(bArr.length * 8, bArr);
    }

    public UniqueIdentity(DerInputStream derInputStream) throws IOException {
        this.f935id = derInputStream.getDerValue().getUnalignedBitString(true);
    }

    public UniqueIdentity(DerValue derValue) throws IOException {
        this.f935id = derValue.getUnalignedBitString(true);
    }

    public String toString() {
        return "UniqueIdentity:" + this.f935id.toString() + "\n";
    }

    public void encode(DerOutputStream derOutputStream, byte b) throws IOException {
        byte[] byteArray = this.f935id.toByteArray();
        int length = (byteArray.length * 8) - this.f935id.length();
        derOutputStream.write(b);
        derOutputStream.putLength(byteArray.length + 1);
        derOutputStream.write(length);
        derOutputStream.write(byteArray);
    }

    public boolean[] getId() {
        BitArray bitArray = this.f935id;
        if (bitArray == null) {
            return null;
        }
        return bitArray.toBooleanArray();
    }
}
