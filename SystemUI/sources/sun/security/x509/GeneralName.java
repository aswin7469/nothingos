package sun.security.x509;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class GeneralName {
    private GeneralNameInterface name;

    public GeneralName(GeneralNameInterface generalNameInterface) {
        this.name = null;
        if (generalNameInterface != null) {
            this.name = generalNameInterface;
            return;
        }
        throw new NullPointerException("GeneralName must not be null");
    }

    public GeneralName(DerValue derValue) throws IOException {
        this(derValue, false);
    }

    public GeneralName(DerValue derValue, boolean z) throws IOException {
        URIName uRIName;
        this.name = null;
        short s = (short) ((byte) (derValue.tag & 31));
        switch (s) {
            case 0:
                if (!derValue.isContextSpecific() || !derValue.isConstructed()) {
                    throw new IOException("Invalid encoding of Other-Name");
                }
                derValue.resetTag((byte) 48);
                this.name = new OtherName(derValue);
                return;
            case 1:
                if (!derValue.isContextSpecific() || derValue.isConstructed()) {
                    throw new IOException("Invalid encoding of RFC822 name");
                }
                derValue.resetTag((byte) 22);
                this.name = new RFC822Name(derValue);
                return;
            case 2:
                if (!derValue.isContextSpecific() || derValue.isConstructed()) {
                    throw new IOException("Invalid encoding of DNS name");
                }
                derValue.resetTag((byte) 22);
                this.name = new DNSName(derValue);
                return;
            case 4:
                if (!derValue.isContextSpecific() || !derValue.isConstructed()) {
                    throw new IOException("Invalid encoding of Directory name");
                }
                this.name = new X500Name(derValue.getData());
                return;
            case 5:
                if (!derValue.isContextSpecific() || !derValue.isConstructed()) {
                    throw new IOException("Invalid encoding of EDI name");
                }
                derValue.resetTag((byte) 48);
                this.name = new EDIPartyName(derValue);
                return;
            case 6:
                if (!derValue.isContextSpecific() || derValue.isConstructed()) {
                    throw new IOException("Invalid encoding of URI");
                }
                derValue.resetTag((byte) 22);
                if (z) {
                    uRIName = URIName.nameConstraint(derValue);
                } else {
                    uRIName = new URIName(derValue);
                }
                this.name = uRIName;
                return;
            case 7:
                if (!derValue.isContextSpecific() || derValue.isConstructed()) {
                    throw new IOException("Invalid encoding of IP address");
                }
                derValue.resetTag((byte) 4);
                this.name = new IPAddressName(derValue);
                return;
            case 8:
                if (!derValue.isContextSpecific() || derValue.isConstructed()) {
                    throw new IOException("Invalid encoding of OID name");
                }
                derValue.resetTag((byte) 6);
                this.name = new OIDName(derValue);
                return;
            default:
                throw new IOException("Unrecognized GeneralName tag, (" + s + NavigationBarInflaterView.KEY_CODE_END);
        }
    }

    public int getType() {
        return this.name.getType();
    }

    public GeneralNameInterface getName() {
        return this.name;
    }

    public String toString() {
        return this.name.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GeneralName)) {
            return false;
        }
        try {
            if (this.name.constrains(((GeneralName) obj).name) == 0) {
                return true;
            }
            return false;
        } catch (UnsupportedOperationException unused) {
            return false;
        }
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        this.name.encode(derOutputStream2);
        int type = this.name.getType();
        if (type == 0 || type == 3 || type == 5) {
            derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) type), derOutputStream2);
        } else if (type == 4) {
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) type), derOutputStream2);
        } else {
            derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) type), derOutputStream2);
        }
    }
}
