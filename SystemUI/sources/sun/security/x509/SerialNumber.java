package sun.security.x509;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.math.BigInteger;
import java.p026io.IOException;
import java.p026io.InputStream;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class SerialNumber {
    private BigInteger serialNum;

    private void construct(DerValue derValue) throws IOException {
        this.serialNum = derValue.getBigInteger();
        if (derValue.data.available() != 0) {
            throw new IOException("Excess SerialNumber data");
        }
    }

    public SerialNumber(BigInteger bigInteger) {
        this.serialNum = bigInteger;
    }

    public SerialNumber(int i) {
        this.serialNum = BigInteger.valueOf((long) i);
    }

    public SerialNumber(DerInputStream derInputStream) throws IOException {
        construct(derInputStream.getDerValue());
    }

    public SerialNumber(DerValue derValue) throws IOException {
        construct(derValue);
    }

    public SerialNumber(InputStream inputStream) throws IOException {
        construct(new DerValue(inputStream));
    }

    public String toString() {
        return "SerialNumber: [" + Debug.toHexString(this.serialNum) + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putInteger(this.serialNum);
    }

    public BigInteger getNumber() {
        return this.serialNum;
    }
}
