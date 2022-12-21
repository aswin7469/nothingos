package sun.security.pkcs;

import java.p026io.IOException;
import sun.security.util.DerValue;

public class SigningCertificateInfo {
    private byte[] ber = null;
    private ESSCertId[] certId = null;

    public SigningCertificateInfo(byte[] bArr) throws IOException {
        parse(bArr);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("[\n");
        int i = 0;
        while (true) {
            ESSCertId[] eSSCertIdArr = this.certId;
            if (i < eSSCertIdArr.length) {
                stringBuffer.append(eSSCertIdArr[i].toString());
                i++;
            } else {
                stringBuffer.append("\n]");
                return stringBuffer.toString();
            }
        }
    }

    public void parse(byte[] bArr) throws IOException {
        DerValue derValue = new DerValue(bArr);
        if (derValue.tag == 48) {
            DerValue[] sequence = derValue.data.getSequence(1);
            this.certId = new ESSCertId[sequence.length];
            for (int i = 0; i < sequence.length; i++) {
                this.certId[i] = new ESSCertId(sequence[i]);
            }
            if (derValue.data.available() > 0) {
                for (int i2 = 0; i2 < derValue.data.getSequence(1).length; i2++) {
                }
                return;
            }
            return;
        }
        throw new IOException("Bad encoding for signingCertificate");
    }
}
