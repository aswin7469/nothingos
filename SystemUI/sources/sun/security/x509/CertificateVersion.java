package sun.security.x509;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.Enumeration;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class CertificateVersion implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.version";
    public static final String NAME = "version";

    /* renamed from: V1 */
    public static final int f930V1 = 0;

    /* renamed from: V2 */
    public static final int f931V2 = 1;

    /* renamed from: V3 */
    public static final int f932V3 = 2;
    public static final String VERSION = "number";
    int version = 0;

    public String getName() {
        return "version";
    }

    private int getVersion() {
        return this.version;
    }

    private void construct(DerValue derValue) throws IOException {
        if (derValue.isConstructed() && derValue.isContextSpecific()) {
            DerValue derValue2 = derValue.data.getDerValue();
            this.version = derValue2.getInteger();
            if (derValue2.data.available() != 0) {
                throw new IOException("X.509 version, bad format");
            }
        }
    }

    public CertificateVersion() {
    }

    public CertificateVersion(int i) throws IOException {
        if (i == 0 || i == 1 || i == 2) {
            this.version = i;
            return;
        }
        throw new IOException("X.509 Certificate version " + i + " not supported.\n");
    }

    public CertificateVersion(DerInputStream derInputStream) throws IOException {
        construct(derInputStream.getDerValue());
    }

    public CertificateVersion(InputStream inputStream) throws IOException {
        construct(new DerValue(inputStream));
    }

    public CertificateVersion(DerValue derValue) throws IOException {
        construct(derValue);
    }

    public String toString() {
        return "Version: V" + (this.version + 1);
    }

    public void encode(OutputStream outputStream) throws IOException {
        if (this.version != 0) {
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStream.putInteger(this.version);
            DerOutputStream derOutputStream2 = new DerOutputStream();
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream);
            outputStream.write(derOutputStream2.toByteArray());
        }
    }

    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof Integer)) {
            throw new IOException("Attribute must be of type Integer.");
        } else if (str.equalsIgnoreCase("number")) {
            this.version = ((Integer) obj).intValue();
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet: CertificateVersion.");
        }
    }

    public Integer get(String str) throws IOException {
        if (str.equalsIgnoreCase("number")) {
            return new Integer(getVersion());
        }
        throw new IOException("Attribute name not recognized by CertAttrSet: CertificateVersion.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("number")) {
            this.version = 0;
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet: CertificateVersion.");
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("number");
        return attributeNameEnumeration.elements();
    }

    public int compare(int i) {
        return this.version - i;
    }
}
