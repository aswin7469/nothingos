package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Objects;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class PrivateKeyUsageExtension extends Extension implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.extensions.PrivateKeyUsage";
    public static final String NAME = "PrivateKeyUsage";
    public static final String NOT_AFTER = "not_after";
    public static final String NOT_BEFORE = "not_before";
    private static final byte TAG_AFTER = 1;
    private static final byte TAG_BEFORE = 0;
    private Date notAfter;
    private Date notBefore;

    public String getName() {
        return NAME;
    }

    private void encodeThis() throws IOException {
        if (this.notBefore == null && this.notAfter == null) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        if (this.notBefore != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putGeneralizedTime(this.notBefore);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 0), derOutputStream3);
        }
        if (this.notAfter != null) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putGeneralizedTime(this.notAfter);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 1), derOutputStream4);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public PrivateKeyUsageExtension(Date date, Date date2) throws IOException {
        this.notBefore = date;
        this.notAfter = date2;
        this.extensionId = PKIXExtensions.PrivateKeyUsage_Id;
        this.critical = false;
        encodeThis();
    }

    public PrivateKeyUsageExtension(Boolean bool, Object obj) throws CertificateException, IOException {
        this.notBefore = null;
        this.notAfter = null;
        this.extensionId = PKIXExtensions.PrivateKeyUsage_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        DerValue[] sequence = new DerInputStream(this.extensionValue).getSequence(2);
        for (DerValue derValue : sequence) {
            if (!derValue.isContextSpecific((byte) 0) || derValue.isConstructed()) {
                if (!derValue.isContextSpecific((byte) 1) || derValue.isConstructed()) {
                    throw new IOException("Invalid encoding of PrivateKeyUsageExtension");
                } else if (this.notAfter == null) {
                    derValue.resetTag((byte) 24);
                    this.notAfter = new DerInputStream(derValue.toByteArray()).getGeneralizedTime();
                } else {
                    throw new CertificateParsingException("Duplicate notAfter in PrivateKeyUsage.");
                }
            } else if (this.notBefore == null) {
                derValue.resetTag((byte) 24);
                this.notBefore = new DerInputStream(derValue.toByteArray()).getGeneralizedTime();
            } else {
                throw new CertificateParsingException("Duplicate notBefore in PrivateKeyUsage.");
            }
        }
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("PrivateKeyUsage: [\n");
        String str2 = "";
        if (this.notBefore == null) {
            str = str2;
        } else {
            str = "From: " + this.notBefore.toString() + ", ";
        }
        sb.append(str);
        if (this.notAfter != null) {
            str2 = "To: " + this.notAfter.toString();
        }
        sb.append(str2);
        sb.append("]\n");
        return sb.toString();
    }

    public void valid() throws CertificateNotYetValidException, CertificateExpiredException {
        valid(new Date());
    }

    public void valid(Date date) throws CertificateNotYetValidException, CertificateExpiredException {
        Objects.requireNonNull(date);
        Date date2 = this.notBefore;
        if (date2 == null || !date2.after(date)) {
            Date date3 = this.notAfter;
            if (date3 != null && date3.before(date)) {
                throw new CertificateExpiredException("NotAfter: " + this.notAfter.toString());
            }
            return;
        }
        throw new CertificateNotYetValidException("NotBefore: " + this.notBefore.toString());
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.PrivateKeyUsage_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws CertificateException, IOException {
        if (obj instanceof Date) {
            if (str.equalsIgnoreCase(NOT_BEFORE)) {
                this.notBefore = (Date) obj;
            } else if (str.equalsIgnoreCase(NOT_AFTER)) {
                this.notAfter = (Date) obj;
            } else {
                throw new CertificateException("Attribute name not recognized by CertAttrSet:PrivateKeyUsage.");
            }
            encodeThis();
            return;
        }
        throw new CertificateException("Attribute must be of type Date.");
    }

    public Date get(String str) throws CertificateException {
        if (str.equalsIgnoreCase(NOT_BEFORE)) {
            return new Date(this.notBefore.getTime());
        }
        if (str.equalsIgnoreCase(NOT_AFTER)) {
            return new Date(this.notAfter.getTime());
        }
        throw new CertificateException("Attribute name not recognized by CertAttrSet:PrivateKeyUsage.");
    }

    public void delete(String str) throws CertificateException, IOException {
        if (str.equalsIgnoreCase(NOT_BEFORE)) {
            this.notBefore = null;
        } else if (str.equalsIgnoreCase(NOT_AFTER)) {
            this.notAfter = null;
        } else {
            throw new CertificateException("Attribute name not recognized by CertAttrSet:PrivateKeyUsage.");
        }
        encodeThis();
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(NOT_BEFORE);
        attributeNameEnumeration.addElement(NOT_AFTER);
        return attributeNameEnumeration.elements();
    }
}
