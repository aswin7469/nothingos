package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.security.cert.CRLReason;
import java.util.Enumeration;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class CRLReasonCodeExtension extends Extension implements CertAttrSet<String> {
    public static final String NAME = "CRLReasonCode";
    public static final String REASON = "reason";
    private static CRLReason[] values = CRLReason.values();
    private int reasonCode;

    public String getName() {
        return NAME;
    }

    private void encodeThis() throws IOException {
        if (this.reasonCode == 0) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putEnumerated(this.reasonCode);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public CRLReasonCodeExtension(int i) throws IOException {
        this(false, i);
    }

    public CRLReasonCodeExtension(boolean z, int i) throws IOException {
        this.reasonCode = 0;
        this.extensionId = PKIXExtensions.ReasonCode_Id;
        this.critical = z;
        this.reasonCode = i;
        encodeThis();
    }

    public CRLReasonCodeExtension(Boolean bool, Object obj) throws IOException {
        this.reasonCode = 0;
        this.extensionId = PKIXExtensions.ReasonCode_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        this.reasonCode = new DerValue(this.extensionValue).getEnumerated();
    }

    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof Integer)) {
            throw new IOException("Attribute must be of type Integer.");
        } else if (str.equalsIgnoreCase("reason")) {
            this.reasonCode = ((Integer) obj).intValue();
            encodeThis();
        } else {
            throw new IOException("Name not supported by CRLReasonCodeExtension");
        }
    }

    public Integer get(String str) throws IOException {
        if (str.equalsIgnoreCase("reason")) {
            return new Integer(this.reasonCode);
        }
        throw new IOException("Name not supported by CRLReasonCodeExtension");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("reason")) {
            this.reasonCode = 0;
            encodeThis();
            return;
        }
        throw new IOException("Name not supported by CRLReasonCodeExtension");
    }

    public String toString() {
        return super.toString() + "    Reason Code: " + getReasonCode();
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.ReasonCode_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("reason");
        return attributeNameEnumeration.elements();
    }

    public CRLReason getReasonCode() {
        int i = this.reasonCode;
        if (i > 0) {
            CRLReason[] cRLReasonArr = values;
            if (i < cRLReasonArr.length) {
                return cRLReasonArr[i];
            }
        }
        return CRLReason.UNSPECIFIED;
    }
}
