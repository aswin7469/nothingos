package sun.security.x509;

import java.math.BigInteger;
import java.p026io.IOException;
import java.p026io.OutputStream;
import sun.security.util.DerOutputStream;

public class DeltaCRLIndicatorExtension extends CRLNumberExtension {
    private static final String LABEL = "Base CRL Number";
    public static final String NAME = "DeltaCRLIndicator";

    public DeltaCRLIndicatorExtension(int i) throws IOException {
        super(PKIXExtensions.DeltaCRLIndicator_Id, true, BigInteger.valueOf((long) i), NAME, LABEL);
    }

    public DeltaCRLIndicatorExtension(BigInteger bigInteger) throws IOException {
        super(PKIXExtensions.DeltaCRLIndicator_Id, true, bigInteger, NAME, LABEL);
    }

    public DeltaCRLIndicatorExtension(Boolean bool, Object obj) throws IOException {
        super(PKIXExtensions.DeltaCRLIndicator_Id, Boolean.valueOf(bool.booleanValue()), obj, NAME, LABEL);
    }

    public void encode(OutputStream outputStream) throws IOException {
        new DerOutputStream();
        super.encode(outputStream, PKIXExtensions.DeltaCRLIndicator_Id, true);
    }
}
