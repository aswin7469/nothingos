package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.Enumeration;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class IssuingDistributionPointExtension extends Extension implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.extensions.IssuingDistributionPoint";
    public static final String INDIRECT_CRL = "indirect_crl";
    public static final String NAME = "IssuingDistributionPoint";
    public static final String ONLY_ATTRIBUTE_CERTS = "only_attribute_certs";
    public static final String ONLY_CA_CERTS = "only_ca_certs";
    public static final String ONLY_USER_CERTS = "only_user_certs";
    public static final String POINT = "point";
    public static final String REASONS = "reasons";
    private static final byte TAG_DISTRIBUTION_POINT = 0;
    private static final byte TAG_INDIRECT_CRL = 4;
    private static final byte TAG_ONLY_ATTRIBUTE_CERTS = 5;
    private static final byte TAG_ONLY_CA_CERTS = 2;
    private static final byte TAG_ONLY_SOME_REASONS = 3;
    private static final byte TAG_ONLY_USER_CERTS = 1;
    private DistributionPointName distributionPoint = null;
    private boolean hasOnlyAttributeCerts = false;
    private boolean hasOnlyCACerts = false;
    private boolean hasOnlyUserCerts = false;
    private boolean isIndirectCRL = false;
    private ReasonFlags revocationReasons = null;

    public String getName() {
        return NAME;
    }

    public IssuingDistributionPointExtension(DistributionPointName distributionPointName, ReasonFlags reasonFlags, boolean z, boolean z2, boolean z3, boolean z4) throws IOException {
        if ((!z || (!z2 && !z3)) && ((!z2 || (!z && !z3)) && (!z3 || (!z && !z2)))) {
            this.extensionId = PKIXExtensions.IssuingDistributionPoint_Id;
            this.critical = true;
            this.distributionPoint = distributionPointName;
            this.revocationReasons = reasonFlags;
            this.hasOnlyUserCerts = z;
            this.hasOnlyCACerts = z2;
            this.hasOnlyAttributeCerts = z3;
            this.isIndirectCRL = z4;
            encodeThis();
            return;
        }
        throw new IllegalArgumentException("Only one of hasOnlyUserCerts, hasOnlyCACerts, hasOnlyAttributeCerts may be set to true");
    }

    public IssuingDistributionPointExtension(Boolean bool, Object obj) throws IOException {
        this.extensionId = PKIXExtensions.IssuingDistributionPoint_Id;
        this.critical = bool.booleanValue();
        if (obj instanceof byte[]) {
            this.extensionValue = (byte[]) obj;
            DerValue derValue = new DerValue(this.extensionValue);
            if (derValue.tag != 48) {
                throw new IOException("Invalid encoding for IssuingDistributionPointExtension.");
            } else if (derValue.data != null && derValue.data.available() != 0) {
                DerInputStream derInputStream = derValue.data;
                while (derInputStream != null && derInputStream.available() != 0) {
                    DerValue derValue2 = derInputStream.getDerValue();
                    if (derValue2.isContextSpecific((byte) 0) && derValue2.isConstructed()) {
                        this.distributionPoint = new DistributionPointName(derValue2.data.getDerValue());
                    } else if (derValue2.isContextSpecific((byte) 1) && !derValue2.isConstructed()) {
                        derValue2.resetTag((byte) 1);
                        this.hasOnlyUserCerts = derValue2.getBoolean();
                    } else if (derValue2.isContextSpecific((byte) 2) && !derValue2.isConstructed()) {
                        derValue2.resetTag((byte) 1);
                        this.hasOnlyCACerts = derValue2.getBoolean();
                    } else if (derValue2.isContextSpecific((byte) 3) && !derValue2.isConstructed()) {
                        this.revocationReasons = new ReasonFlags(derValue2);
                    } else if (derValue2.isContextSpecific((byte) 4) && !derValue2.isConstructed()) {
                        derValue2.resetTag((byte) 1);
                        this.isIndirectCRL = derValue2.getBoolean();
                    } else if (!derValue2.isContextSpecific((byte) 5) || derValue2.isConstructed()) {
                        throw new IOException("Invalid encoding of IssuingDistributionPoint");
                    } else {
                        derValue2.resetTag((byte) 1);
                        this.hasOnlyAttributeCerts = derValue2.getBoolean();
                    }
                }
            }
        } else {
            throw new IOException("Illegal argument type");
        }
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.IssuingDistributionPoint_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (str.equalsIgnoreCase(POINT)) {
            if (obj instanceof DistributionPointName) {
                this.distributionPoint = (DistributionPointName) obj;
            } else {
                throw new IOException("Attribute value should be of type DistributionPointName.");
            }
        } else if (str.equalsIgnoreCase(REASONS)) {
            if (obj instanceof ReasonFlags) {
                this.revocationReasons = (ReasonFlags) obj;
            } else {
                throw new IOException("Attribute value should be of type ReasonFlags.");
            }
        } else if (str.equalsIgnoreCase(INDIRECT_CRL)) {
            if (obj instanceof Boolean) {
                this.isIndirectCRL = ((Boolean) obj).booleanValue();
            } else {
                throw new IOException("Attribute value should be of type Boolean.");
            }
        } else if (str.equalsIgnoreCase(ONLY_USER_CERTS)) {
            if (obj instanceof Boolean) {
                this.hasOnlyUserCerts = ((Boolean) obj).booleanValue();
            } else {
                throw new IOException("Attribute value should be of type Boolean.");
            }
        } else if (str.equalsIgnoreCase(ONLY_CA_CERTS)) {
            if (obj instanceof Boolean) {
                this.hasOnlyCACerts = ((Boolean) obj).booleanValue();
            } else {
                throw new IOException("Attribute value should be of type Boolean.");
            }
        } else if (!str.equalsIgnoreCase(ONLY_ATTRIBUTE_CERTS)) {
            throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:IssuingDistributionPointExtension.");
        } else if (obj instanceof Boolean) {
            this.hasOnlyAttributeCerts = ((Boolean) obj).booleanValue();
        } else {
            throw new IOException("Attribute value should be of type Boolean.");
        }
        encodeThis();
    }

    public Object get(String str) throws IOException {
        if (str.equalsIgnoreCase(POINT)) {
            return this.distributionPoint;
        }
        if (str.equalsIgnoreCase(INDIRECT_CRL)) {
            return Boolean.valueOf(this.isIndirectCRL);
        }
        if (str.equalsIgnoreCase(REASONS)) {
            return this.revocationReasons;
        }
        if (str.equalsIgnoreCase(ONLY_USER_CERTS)) {
            return Boolean.valueOf(this.hasOnlyUserCerts);
        }
        if (str.equalsIgnoreCase(ONLY_CA_CERTS)) {
            return Boolean.valueOf(this.hasOnlyCACerts);
        }
        if (str.equalsIgnoreCase(ONLY_ATTRIBUTE_CERTS)) {
            return Boolean.valueOf(this.hasOnlyAttributeCerts);
        }
        throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:IssuingDistributionPointExtension.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(POINT)) {
            this.distributionPoint = null;
        } else if (str.equalsIgnoreCase(INDIRECT_CRL)) {
            this.isIndirectCRL = false;
        } else if (str.equalsIgnoreCase(REASONS)) {
            this.revocationReasons = null;
        } else if (str.equalsIgnoreCase(ONLY_USER_CERTS)) {
            this.hasOnlyUserCerts = false;
        } else if (str.equalsIgnoreCase(ONLY_CA_CERTS)) {
            this.hasOnlyCACerts = false;
        } else if (str.equalsIgnoreCase(ONLY_ATTRIBUTE_CERTS)) {
            this.hasOnlyAttributeCerts = false;
        } else {
            throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:IssuingDistributionPointExtension.");
        }
        encodeThis();
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(POINT);
        attributeNameEnumeration.addElement(REASONS);
        attributeNameEnumeration.addElement(ONLY_USER_CERTS);
        attributeNameEnumeration.addElement(ONLY_CA_CERTS);
        attributeNameEnumeration.addElement(ONLY_ATTRIBUTE_CERTS);
        attributeNameEnumeration.addElement(INDIRECT_CRL);
        return attributeNameEnumeration.elements();
    }

    private void encodeThis() throws IOException {
        if (this.distributionPoint != null || this.revocationReasons != null || this.hasOnlyUserCerts || this.hasOnlyCACerts || this.hasOnlyAttributeCerts || this.isIndirectCRL) {
            DerOutputStream derOutputStream = new DerOutputStream();
            if (this.distributionPoint != null) {
                DerOutputStream derOutputStream2 = new DerOutputStream();
                this.distributionPoint.encode(derOutputStream2);
                derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
            }
            if (this.hasOnlyUserCerts) {
                DerOutputStream derOutputStream3 = new DerOutputStream();
                derOutputStream3.putBoolean(this.hasOnlyUserCerts);
                derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 1), derOutputStream3);
            }
            if (this.hasOnlyCACerts) {
                DerOutputStream derOutputStream4 = new DerOutputStream();
                derOutputStream4.putBoolean(this.hasOnlyCACerts);
                derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 2), derOutputStream4);
            }
            if (this.revocationReasons != null) {
                DerOutputStream derOutputStream5 = new DerOutputStream();
                this.revocationReasons.encode(derOutputStream5);
                derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 3), derOutputStream5);
            }
            if (this.isIndirectCRL) {
                DerOutputStream derOutputStream6 = new DerOutputStream();
                derOutputStream6.putBoolean(this.isIndirectCRL);
                derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 4), derOutputStream6);
            }
            if (this.hasOnlyAttributeCerts) {
                DerOutputStream derOutputStream7 = new DerOutputStream();
                derOutputStream7.putBoolean(this.hasOnlyAttributeCerts);
                derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 5), derOutputStream7);
            }
            DerOutputStream derOutputStream8 = new DerOutputStream();
            derOutputStream8.write((byte) 48, derOutputStream);
            this.extensionValue = derOutputStream8.toByteArray();
            return;
        }
        this.extensionValue = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("IssuingDistributionPoint [\n  ");
        DistributionPointName distributionPointName = this.distributionPoint;
        if (distributionPointName != null) {
            sb.append((Object) distributionPointName);
        }
        ReasonFlags reasonFlags = this.revocationReasons;
        if (reasonFlags != null) {
            sb.append((Object) reasonFlags);
        }
        sb.append(this.hasOnlyUserCerts ? "  Only contains user certs: true" : "  Only contains user certs: false");
        sb.append("\n");
        sb.append(this.hasOnlyCACerts ? "  Only contains CA certs: true" : "  Only contains CA certs: false");
        sb.append("\n");
        sb.append(this.hasOnlyAttributeCerts ? "  Only contains attribute certs: true" : "  Only contains attribute certs: false");
        sb.append("\n");
        sb.append(this.isIndirectCRL ? "  Indirect CRL: true" : "  Indirect CRL: false");
        sb.append("\n]\n");
        return sb.toString();
    }
}
