package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.Enumeration;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class BasicConstraintsExtension extends Extension implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.extensions.BasicConstraints";
    public static final String IS_CA = "is_ca";
    public static final String NAME = "BasicConstraints";
    public static final String PATH_LEN = "path_len";

    /* renamed from: ca */
    private boolean f928ca;
    private int pathLen;

    public String getName() {
        return NAME;
    }

    private void encodeThis() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        boolean z = this.f928ca;
        if (z) {
            derOutputStream2.putBoolean(z);
            int i = this.pathLen;
            if (i >= 0) {
                derOutputStream2.putInteger(i);
            }
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public BasicConstraintsExtension(boolean z, int i) throws IOException {
        this(Boolean.valueOf(z), z, i);
    }

    public BasicConstraintsExtension(Boolean bool, boolean z, int i) throws IOException {
        this.f928ca = z;
        this.pathLen = i;
        this.extensionId = PKIXExtensions.BasicConstraints_Id;
        this.critical = bool.booleanValue();
        encodeThis();
    }

    public BasicConstraintsExtension(Boolean bool, Object obj) throws IOException {
        this.f928ca = false;
        this.pathLen = -1;
        this.extensionId = PKIXExtensions.BasicConstraints_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        DerValue derValue = new DerValue(this.extensionValue);
        if (derValue.tag != 48) {
            throw new IOException("Invalid encoding of BasicConstraints");
        } else if (derValue.data != null && derValue.data.available() != 0) {
            DerValue derValue2 = derValue.data.getDerValue();
            if (derValue2.tag == 1) {
                this.f928ca = derValue2.getBoolean();
                if (derValue.data.available() == 0) {
                    this.pathLen = Integer.MAX_VALUE;
                    return;
                }
                DerValue derValue3 = derValue.data.getDerValue();
                if (derValue3.tag == 2) {
                    this.pathLen = derValue3.getInteger();
                    return;
                }
                throw new IOException("Invalid encoding of BasicConstraints");
            }
        }
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString() + "BasicConstraints:[\n");
        sb.append(this.f928ca ? "  CA:true" : "  CA:false");
        sb.append("\n");
        String sb2 = sb.toString();
        if (this.pathLen >= 0) {
            str = sb2 + "  PathLen:" + this.pathLen + "\n";
        } else {
            str = sb2 + "  PathLen: undefined\n";
        }
        return str + "]\n";
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.BasicConstraints_Id;
            if (this.f928ca) {
                this.critical = true;
            } else {
                this.critical = false;
            }
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (str.equalsIgnoreCase(IS_CA)) {
            if (obj instanceof Boolean) {
                this.f928ca = ((Boolean) obj).booleanValue();
            } else {
                throw new IOException("Attribute value should be of type Boolean.");
            }
        } else if (!str.equalsIgnoreCase(PATH_LEN)) {
            throw new IOException("Attribute name not recognized by CertAttrSet:BasicConstraints.");
        } else if (obj instanceof Integer) {
            this.pathLen = ((Integer) obj).intValue();
        } else {
            throw new IOException("Attribute value should be of type Integer.");
        }
        encodeThis();
    }

    public Object get(String str) throws IOException {
        if (str.equalsIgnoreCase(IS_CA)) {
            return Boolean.valueOf(this.f928ca);
        }
        if (str.equalsIgnoreCase(PATH_LEN)) {
            return Integer.valueOf(this.pathLen);
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:BasicConstraints.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(IS_CA)) {
            this.f928ca = false;
        } else if (str.equalsIgnoreCase(PATH_LEN)) {
            this.pathLen = -1;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:BasicConstraints.");
        }
        encodeThis();
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(IS_CA);
        attributeNameEnumeration.addElement(PATH_LEN);
        return attributeNameEnumeration.elements();
    }
}
