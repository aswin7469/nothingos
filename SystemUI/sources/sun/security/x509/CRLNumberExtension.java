package sun.security.x509;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.math.BigInteger;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.Enumeration;
import sun.security.util.Debug;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class CRLNumberExtension extends Extension implements CertAttrSet<String> {
    private static final String LABEL = "CRL Number";
    public static final String NAME = "CRLNumber";
    public static final String NUMBER = "value";
    private BigInteger crlNumber;
    private String extensionLabel;
    private String extensionName;

    private void encodeThis() throws IOException {
        if (this.crlNumber == null) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putInteger(this.crlNumber);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public CRLNumberExtension(int i) throws IOException {
        this(PKIXExtensions.CRLNumber_Id, false, BigInteger.valueOf((long) i), NAME, LABEL);
    }

    public CRLNumberExtension(BigInteger bigInteger) throws IOException {
        this(PKIXExtensions.CRLNumber_Id, false, bigInteger, NAME, LABEL);
    }

    protected CRLNumberExtension(ObjectIdentifier objectIdentifier, boolean z, BigInteger bigInteger, String str, String str2) throws IOException {
        this.crlNumber = null;
        this.extensionId = objectIdentifier;
        this.critical = z;
        this.crlNumber = bigInteger;
        this.extensionName = str;
        this.extensionLabel = str2;
        encodeThis();
    }

    public CRLNumberExtension(Boolean bool, Object obj) throws IOException {
        this(PKIXExtensions.CRLNumber_Id, bool, obj, NAME, LABEL);
    }

    protected CRLNumberExtension(ObjectIdentifier objectIdentifier, Boolean bool, Object obj, String str, String str2) throws IOException {
        this.crlNumber = null;
        this.extensionId = objectIdentifier;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        this.crlNumber = new DerValue(this.extensionValue).getBigInteger();
        this.extensionName = str;
        this.extensionLabel = str2;
    }

    public void set(String str, Object obj) throws IOException {
        if (!str.equalsIgnoreCase("value")) {
            throw new IOException("Attribute name not recognized by CertAttrSet:" + this.extensionName + BaseIconCache.EMPTY_CLASS_NAME);
        } else if (obj instanceof BigInteger) {
            this.crlNumber = (BigInteger) obj;
            encodeThis();
        } else {
            throw new IOException("Attribute must be of type BigInteger.");
        }
    }

    public BigInteger get(String str) throws IOException {
        if (str.equalsIgnoreCase("value")) {
            return this.crlNumber;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:" + this.extensionName + '.');
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("value")) {
            this.crlNumber = null;
            encodeThis();
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:" + this.extensionName + BaseIconCache.EMPTY_CLASS_NAME);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(this.extensionLabel);
        sb.append(": ");
        BigInteger bigInteger = this.crlNumber;
        sb.append(bigInteger == null ? "" : Debug.toHexString(bigInteger));
        sb.append("\n");
        return sb.toString();
    }

    public void encode(OutputStream outputStream) throws IOException {
        new DerOutputStream();
        encode(outputStream, PKIXExtensions.CRLNumber_Id, true);
    }

    /* access modifiers changed from: protected */
    public void encode(OutputStream outputStream, ObjectIdentifier objectIdentifier, boolean z) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = objectIdentifier;
            this.critical = z;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("value");
        return attributeNameEnumeration.elements();
    }

    public String getName() {
        return this.extensionName;
    }
}
