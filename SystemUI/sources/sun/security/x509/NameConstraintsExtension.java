package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class NameConstraintsExtension extends Extension implements CertAttrSet<String>, Cloneable {
    public static final String EXCLUDED_SUBTREES = "excluded_subtrees";
    public static final String IDENT = "x509.info.extensions.NameConstraints";
    public static final String NAME = "NameConstraints";
    public static final String PERMITTED_SUBTREES = "permitted_subtrees";
    private static final byte TAG_EXCLUDED = 1;
    private static final byte TAG_PERMITTED = 0;
    private GeneralSubtrees excluded;
    private boolean hasMax;
    private boolean hasMin;
    private boolean minMaxValid;
    private GeneralSubtrees permitted;

    public String getName() {
        return NAME;
    }

    private void calcMinMax() throws IOException {
        this.hasMin = false;
        this.hasMax = false;
        if (this.excluded != null) {
            for (int i = 0; i < this.excluded.size(); i++) {
                GeneralSubtree generalSubtree = this.excluded.get(i);
                if (generalSubtree.getMinimum() != 0) {
                    this.hasMin = true;
                }
                if (generalSubtree.getMaximum() != -1) {
                    this.hasMax = true;
                }
            }
        }
        if (this.permitted != null) {
            for (int i2 = 0; i2 < this.permitted.size(); i2++) {
                GeneralSubtree generalSubtree2 = this.permitted.get(i2);
                if (generalSubtree2.getMinimum() != 0) {
                    this.hasMin = true;
                }
                if (generalSubtree2.getMaximum() != -1) {
                    this.hasMax = true;
                }
            }
        }
        this.minMaxValid = true;
    }

    private void encodeThis() throws IOException {
        this.minMaxValid = false;
        if (this.permitted == null && this.excluded == null) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        if (this.permitted != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            this.permitted.encode(derOutputStream3);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream3);
        }
        if (this.excluded != null) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            this.excluded.encode(derOutputStream4);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream4);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public NameConstraintsExtension(GeneralSubtrees generalSubtrees, GeneralSubtrees generalSubtrees2) throws IOException {
        this.minMaxValid = false;
        this.permitted = generalSubtrees;
        this.excluded = generalSubtrees2;
        this.extensionId = PKIXExtensions.NameConstraints_Id;
        this.critical = true;
        encodeThis();
    }

    public NameConstraintsExtension(Boolean bool, Object obj) throws IOException {
        this.permitted = null;
        this.excluded = null;
        this.minMaxValid = false;
        this.extensionId = PKIXExtensions.NameConstraints_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        DerValue derValue = new DerValue(this.extensionValue);
        if (derValue.tag != 48) {
            throw new IOException("Invalid encoding for NameConstraintsExtension.");
        } else if (derValue.data != null) {
            while (derValue.data.available() != 0) {
                DerValue derValue2 = derValue.data.getDerValue();
                if (!derValue2.isContextSpecific((byte) 0) || !derValue2.isConstructed()) {
                    if (!derValue2.isContextSpecific((byte) 1) || !derValue2.isConstructed()) {
                        throw new IOException("Invalid encoding of NameConstraintsExtension.");
                    } else if (this.excluded == null) {
                        derValue2.resetTag((byte) 48);
                        this.excluded = new GeneralSubtrees(derValue2);
                    } else {
                        throw new IOException("Duplicate excluded GeneralSubtrees in NameConstraintsExtension.");
                    }
                } else if (this.permitted == null) {
                    derValue2.resetTag((byte) 48);
                    this.permitted = new GeneralSubtrees(derValue2);
                } else {
                    throw new IOException("Duplicate permitted GeneralSubtrees in NameConstraintsExtension.");
                }
            }
            this.minMaxValid = false;
        }
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("NameConstraints: [");
        String str2 = "";
        if (this.permitted == null) {
            str = str2;
        } else {
            str = "\n    Permitted:" + this.permitted.toString();
        }
        sb.append(str);
        if (this.excluded != null) {
            str2 = "\n    Excluded:" + this.excluded.toString();
        }
        sb.append(str2);
        sb.append("   ]\n");
        return sb.toString();
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.NameConstraints_Id;
            this.critical = true;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (str.equalsIgnoreCase(PERMITTED_SUBTREES)) {
            if (obj instanceof GeneralSubtrees) {
                this.permitted = (GeneralSubtrees) obj;
            } else {
                throw new IOException("Attribute value should be of type GeneralSubtrees.");
            }
        } else if (!str.equalsIgnoreCase(EXCLUDED_SUBTREES)) {
            throw new IOException("Attribute name not recognized by CertAttrSet:NameConstraintsExtension.");
        } else if (obj instanceof GeneralSubtrees) {
            this.excluded = (GeneralSubtrees) obj;
        } else {
            throw new IOException("Attribute value should be of type GeneralSubtrees.");
        }
        encodeThis();
    }

    public GeneralSubtrees get(String str) throws IOException {
        if (str.equalsIgnoreCase(PERMITTED_SUBTREES)) {
            return this.permitted;
        }
        if (str.equalsIgnoreCase(EXCLUDED_SUBTREES)) {
            return this.excluded;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:NameConstraintsExtension.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(PERMITTED_SUBTREES)) {
            this.permitted = null;
        } else if (str.equalsIgnoreCase(EXCLUDED_SUBTREES)) {
            this.excluded = null;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:NameConstraintsExtension.");
        }
        encodeThis();
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(PERMITTED_SUBTREES);
        attributeNameEnumeration.addElement(EXCLUDED_SUBTREES);
        return attributeNameEnumeration.elements();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: sun.security.x509.GeneralSubtrees} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void merge(sun.security.x509.NameConstraintsExtension r4) throws java.p026io.IOException {
        /*
            r3 = this;
            if (r4 != 0) goto L_0x0003
            return
        L_0x0003:
            java.lang.String r0 = "excluded_subtrees"
            sun.security.x509.GeneralSubtrees r0 = r4.get((java.lang.String) r0)
            sun.security.x509.GeneralSubtrees r1 = r3.excluded
            r2 = 0
            if (r1 != 0) goto L_0x001b
            if (r0 == 0) goto L_0x0017
            java.lang.Object r0 = r0.clone()
            sun.security.x509.GeneralSubtrees r0 = (sun.security.x509.GeneralSubtrees) r0
            goto L_0x0018
        L_0x0017:
            r0 = r2
        L_0x0018:
            r3.excluded = r0
            goto L_0x0020
        L_0x001b:
            if (r0 == 0) goto L_0x0020
            r1.union(r0)
        L_0x0020:
            java.lang.String r0 = "permitted_subtrees"
            sun.security.x509.GeneralSubtrees r4 = r4.get((java.lang.String) r0)
            sun.security.x509.GeneralSubtrees r0 = r3.permitted
            if (r0 != 0) goto L_0x0036
            if (r4 == 0) goto L_0x0033
            java.lang.Object r4 = r4.clone()
            r2 = r4
            sun.security.x509.GeneralSubtrees r2 = (sun.security.x509.GeneralSubtrees) r2
        L_0x0033:
            r3.permitted = r2
            goto L_0x004e
        L_0x0036:
            if (r4 == 0) goto L_0x004e
            sun.security.x509.GeneralSubtrees r4 = r0.intersect(r4)
            if (r4 == 0) goto L_0x004e
            sun.security.x509.GeneralSubtrees r0 = r3.excluded
            if (r0 == 0) goto L_0x0046
            r0.union(r4)
            goto L_0x004e
        L_0x0046:
            java.lang.Object r4 = r4.clone()
            sun.security.x509.GeneralSubtrees r4 = (sun.security.x509.GeneralSubtrees) r4
            r3.excluded = r4
        L_0x004e:
            sun.security.x509.GeneralSubtrees r4 = r3.permitted
            if (r4 == 0) goto L_0x0057
            sun.security.x509.GeneralSubtrees r0 = r3.excluded
            r4.reduce(r0)
        L_0x0057:
            r3.encodeThis()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.NameConstraintsExtension.merge(sun.security.x509.NameConstraintsExtension):void");
    }

    public boolean verify(X509Certificate x509Certificate) throws IOException {
        GeneralNames generalNames;
        if (x509Certificate != null) {
            if (!this.minMaxValid) {
                calcMinMax();
            }
            if (this.hasMin) {
                throw new IOException("Non-zero minimum BaseDistance in name constraints not supported");
            } else if (!this.hasMax) {
                X500Name asX500Name = X500Name.asX500Name(x509Certificate.getSubjectX500Principal());
                if (!asX500Name.isEmpty() && !verify((GeneralNameInterface) asX500Name)) {
                    return false;
                }
                try {
                    SubjectAlternativeNameExtension subjectAlternativeNameExtension = X509CertImpl.toImpl(x509Certificate).getSubjectAlternativeNameExtension();
                    if (subjectAlternativeNameExtension != null) {
                        generalNames = subjectAlternativeNameExtension.get(SubjectAlternativeNameExtension.SUBJECT_NAME);
                    } else {
                        generalNames = null;
                    }
                    if (generalNames == null) {
                        return verifyRFC822SpecialCase(asX500Name);
                    }
                    for (int i = 0; i < generalNames.size(); i++) {
                        if (!verify(generalNames.get(i).getName())) {
                            return false;
                        }
                    }
                    return true;
                } catch (CertificateException e) {
                    throw new IOException("Unable to extract extensions from certificate: " + e.getMessage());
                }
            } else {
                throw new IOException("Maximum BaseDistance in name constraints not supported");
            }
        } else {
            throw new IOException("Certificate is null");
        }
    }

    public boolean verify(GeneralNameInterface generalNameInterface) throws IOException {
        GeneralName name;
        GeneralNameInterface name2;
        GeneralName name3;
        GeneralNameInterface name4;
        int constrains;
        if (generalNameInterface != null) {
            GeneralSubtrees generalSubtrees = this.excluded;
            if (generalSubtrees != null && generalSubtrees.size() > 0) {
                for (int i = 0; i < this.excluded.size(); i++) {
                    GeneralSubtree generalSubtree = this.excluded.get(i);
                    if (generalSubtree != null && (name3 = generalSubtree.getName()) != null && (name4 = name3.getName()) != null && ((constrains = name4.constrains(generalNameInterface)) == 0 || constrains == 1)) {
                        return false;
                    }
                }
            }
            GeneralSubtrees generalSubtrees2 = this.permitted;
            if (generalSubtrees2 != null && generalSubtrees2.size() > 0) {
                boolean z = false;
                for (int i2 = 0; i2 < this.permitted.size(); i2++) {
                    GeneralSubtree generalSubtree2 = this.permitted.get(i2);
                    if (!(generalSubtree2 == null || (name = generalSubtree2.getName()) == null || (name2 = name.getName()) == null)) {
                        int constrains2 = name2.constrains(generalNameInterface);
                        if (constrains2 == 0 || constrains2 == 1) {
                            return true;
                        }
                        if (constrains2 == 2 || constrains2 == 3) {
                            z = true;
                        }
                    }
                }
                if (z) {
                    return false;
                }
            }
            return true;
        }
        throw new IOException("name is null");
    }

    public boolean verifyRFC822SpecialCase(X500Name x500Name) throws IOException {
        String valueString;
        for (AVA next : x500Name.allAvas()) {
            if (next.getObjectIdentifier().equals((Object) PKCS9Attribute.EMAIL_ADDRESS_OID) && (valueString = next.getValueString()) != null) {
                try {
                    if (!verify((GeneralNameInterface) new RFC822Name(valueString))) {
                        return false;
                    }
                } catch (IOException unused) {
                    continue;
                }
            }
        }
        return true;
    }

    public Object clone() {
        try {
            NameConstraintsExtension nameConstraintsExtension = (NameConstraintsExtension) super.clone();
            GeneralSubtrees generalSubtrees = this.permitted;
            if (generalSubtrees != null) {
                nameConstraintsExtension.permitted = (GeneralSubtrees) generalSubtrees.clone();
            }
            GeneralSubtrees generalSubtrees2 = this.excluded;
            if (generalSubtrees2 != null) {
                nameConstraintsExtension.excluded = (GeneralSubtrees) generalSubtrees2.clone();
            }
            return nameConstraintsExtension;
        } catch (CloneNotSupportedException unused) {
            throw new RuntimeException("CloneNotSupportedException while cloning NameConstraintsException. This should never happen.");
        }
    }
}
