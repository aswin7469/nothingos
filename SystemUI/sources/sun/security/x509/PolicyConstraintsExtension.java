package sun.security.x509;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.Enumeration;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class PolicyConstraintsExtension extends Extension implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.extensions.PolicyConstraints";
    public static final String INHIBIT = "inhibit";
    public static final String NAME = "PolicyConstraints";
    public static final String REQUIRE = "require";
    private static final byte TAG_INHIBIT = 1;
    private static final byte TAG_REQUIRE = 0;
    private int inhibit;
    private int require;

    public String getName() {
        return NAME;
    }

    private void encodeThis() throws IOException {
        if (this.require == -1 && this.inhibit == -1) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        if (this.require != -1) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putInteger(this.require);
            derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 0), derOutputStream3);
        }
        if (this.inhibit != -1) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putInteger(this.inhibit);
            derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 1), derOutputStream4);
        }
        derOutputStream2.write((byte) 48, derOutputStream);
        this.extensionValue = derOutputStream2.toByteArray();
    }

    public PolicyConstraintsExtension(int i, int i2) throws IOException {
        this(Boolean.FALSE, i, i2);
    }

    public PolicyConstraintsExtension(Boolean bool, int i, int i2) throws IOException {
        this.require = i;
        this.inhibit = i2;
        this.extensionId = PKIXExtensions.PolicyConstraints_Id;
        this.critical = bool.booleanValue();
        encodeThis();
    }

    public PolicyConstraintsExtension(Boolean bool, Object obj) throws IOException {
        this.require = -1;
        this.inhibit = -1;
        this.extensionId = PKIXExtensions.PolicyConstraints_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        DerValue derValue = new DerValue(this.extensionValue);
        if (derValue.tag == 48) {
            DerInputStream derInputStream = derValue.data;
            while (derInputStream != null && derInputStream.available() != 0) {
                DerValue derValue2 = derInputStream.getDerValue();
                if (!derValue2.isContextSpecific((byte) 0) || derValue2.isConstructed()) {
                    if (!derValue2.isContextSpecific((byte) 1) || derValue2.isConstructed()) {
                        throw new IOException("Invalid encoding of PolicyConstraint");
                    } else if (this.inhibit == -1) {
                        derValue2.resetTag((byte) 2);
                        this.inhibit = derValue2.getInteger();
                    } else {
                        throw new IOException("Duplicate inhibitPolicyMappingfound in the PolicyConstraintsExtension");
                    }
                } else if (this.require == -1) {
                    derValue2.resetTag((byte) 2);
                    this.require = derValue2.getInteger();
                } else {
                    throw new IOException("Duplicate requireExplicitPolicyfound in the PolicyConstraintsExtension");
                }
            }
            return;
        }
        throw new IOException("Sequence tag missing for PolicyConstraint.");
    }

    public String toString() {
        String str;
        String str2;
        String str3 = super.toString() + "PolicyConstraints: [  Require: ";
        if (this.require == -1) {
            str = str3 + "unspecified;";
        } else {
            str = str3 + this.require + NavigationBarInflaterView.GRAVITY_SEPARATOR;
        }
        String str4 = str + "\tInhibit: ";
        if (this.inhibit == -1) {
            str2 = str4 + "unspecified";
        } else {
            str2 = str4 + this.inhibit;
        }
        return str2 + " ]\n";
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.PolicyConstraints_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (obj instanceof Integer) {
            if (str.equalsIgnoreCase(REQUIRE)) {
                this.require = ((Integer) obj).intValue();
            } else if (str.equalsIgnoreCase(INHIBIT)) {
                this.inhibit = ((Integer) obj).intValue();
            } else {
                throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:PolicyConstraints.");
            }
            encodeThis();
            return;
        }
        throw new IOException("Attribute value should be of type Integer.");
    }

    public Integer get(String str) throws IOException {
        if (str.equalsIgnoreCase(REQUIRE)) {
            return new Integer(this.require);
        }
        if (str.equalsIgnoreCase(INHIBIT)) {
            return new Integer(this.inhibit);
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:PolicyConstraints.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(REQUIRE)) {
            this.require = -1;
        } else if (str.equalsIgnoreCase(INHIBIT)) {
            this.inhibit = -1;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:PolicyConstraints.");
        }
        encodeThis();
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(REQUIRE);
        attributeNameEnumeration.addElement(INHIBIT);
        return attributeNameEnumeration.elements();
    }
}
