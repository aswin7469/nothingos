package sun.security.x509;

import java.p026io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class X400Address implements GeneralNameInterface {
    byte[] nameValue;

    public int getType() {
        return 3;
    }

    public String toString() {
        return "X400Address: <DER-encoded value>";
    }

    public X400Address(byte[] bArr) {
        this.nameValue = bArr;
    }

    public X400Address(DerValue derValue) throws IOException {
        this.nameValue = null;
        this.nameValue = derValue.toByteArray();
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putDerValue(new DerValue(this.nameValue));
    }

    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        if (generalNameInterface == null || generalNameInterface.getType() != 3) {
            return -1;
        }
        throw new UnsupportedOperationException("Narrowing, widening, and match are not supported for X400Address.");
    }

    public int subtreeDepth() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("subtreeDepth not supported for X400Address");
    }
}
