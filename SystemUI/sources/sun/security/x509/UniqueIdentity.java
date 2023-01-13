package sun.security.x509;

import java.p026io.IOException;
import sun.security.util.BitArray;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class UniqueIdentity {

    /* renamed from: id */
    private BitArray f933id;

    public UniqueIdentity(BitArray bitArray) {
        this.f933id = bitArray;
    }

    public UniqueIdentity(byte[] bArr) {
        this.f933id = new BitArray(bArr.length * 8, bArr);
    }

    public UniqueIdentity(DerInputStream derInputStream) throws IOException {
        this.f933id = derInputStream.getDerValue().getUnalignedBitString(true);
    }

    public UniqueIdentity(DerValue derValue) throws IOException {
        this.f933id = derValue.getUnalignedBitString(true);
    }

    public String toString() {
        return "UniqueIdentity:" + this.f933id.toString() + "\n";
    }

    public void encode(DerOutputStream derOutputStream, byte b) throws IOException {
        byte[] byteArray = this.f933id.toByteArray();
        int length = (byteArray.length * 8) - this.f933id.length();
        derOutputStream.write(b);
        derOutputStream.putLength(byteArray.length + 1);
        derOutputStream.write(length);
        derOutputStream.write(byteArray);
    }

    public boolean[] getId() {
        BitArray bitArray = this.f933id;
        if (bitArray == null) {
            return null;
        }
        return bitArray.toBooleanArray();
    }
}
