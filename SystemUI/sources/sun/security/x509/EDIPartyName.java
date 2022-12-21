package sun.security.x509;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class EDIPartyName implements GeneralNameInterface {
    private static final byte TAG_ASSIGNER = 0;
    private static final byte TAG_PARTYNAME = 1;
    private String assigner;
    private int myhash;
    private String party;

    public int getType() {
        return 5;
    }

    public EDIPartyName(String str, String str2) {
        this.myhash = -1;
        this.assigner = str;
        this.party = str2;
    }

    public EDIPartyName(String str) {
        this.assigner = null;
        this.myhash = -1;
        this.party = str;
    }

    public EDIPartyName(DerValue derValue) throws IOException {
        this.assigner = null;
        this.party = null;
        this.myhash = -1;
        DerValue[] sequence = new DerInputStream(derValue.toByteArray()).getSequence(2);
        int length = sequence.length;
        if (length < 1 || length > 2) {
            throw new IOException("Invalid encoding of EDIPartyName");
        }
        for (int i = 0; i < length; i++) {
            DerValue derValue2 = sequence[i];
            if (derValue2.isContextSpecific((byte) 0) && !derValue2.isConstructed()) {
                if (this.assigner == null) {
                    derValue2 = derValue2.data.getDerValue();
                    this.assigner = derValue2.getAsString();
                } else {
                    throw new IOException("Duplicate nameAssigner found in EDIPartyName");
                }
            }
            if (derValue2.isContextSpecific((byte) 1) && !derValue2.isConstructed()) {
                if (this.party == null) {
                    this.party = derValue2.data.getDerValue().getAsString();
                } else {
                    throw new IOException("Duplicate partyName found in EDIPartyName");
                }
            }
        }
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        DerOutputStream derOutputStream3 = new DerOutputStream();
        if (this.assigner != null) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putPrintableString(this.assigner);
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 0), derOutputStream4);
        }
        String str = this.party;
        if (str != null) {
            derOutputStream3.putPrintableString(str);
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 1), derOutputStream3);
            derOutputStream.write((byte) 48, derOutputStream2);
            return;
        }
        throw new IOException("Cannot have null partyName");
    }

    public String getAssignerName() {
        return this.assigner;
    }

    public String getPartyName() {
        return this.party;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof EDIPartyName)) {
            return false;
        }
        EDIPartyName eDIPartyName = (EDIPartyName) obj;
        String str = eDIPartyName.assigner;
        String str2 = this.assigner;
        if (str2 == null) {
            if (str != null) {
                return false;
            }
        } else if (!str2.equals(str)) {
            return false;
        }
        String str3 = eDIPartyName.party;
        String str4 = this.party;
        if (str4 == null) {
            if (str3 != null) {
                return false;
            }
            return true;
        } else if (!str4.equals(str3)) {
            return false;
        } else {
            return true;
        }
    }

    public int hashCode() {
        if (this.myhash == -1) {
            String str = this.party;
            int hashCode = (str == null ? 1 : str.hashCode()) + 37;
            this.myhash = hashCode;
            String str2 = this.assigner;
            if (str2 != null) {
                this.myhash = (hashCode * 37) + str2.hashCode();
            }
        }
        return this.myhash;
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder("EDIPartyName: ");
        if (this.assigner == null) {
            str = "";
        } else {
            str = "  nameAssigner = " + this.assigner + NavigationBarInflaterView.BUTTON_SEPARATOR;
        }
        sb.append(str);
        sb.append("  partyName = ");
        sb.append(this.party);
        return sb.toString();
    }

    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        if (generalNameInterface == null || generalNameInterface.getType() != 5) {
            return -1;
        }
        throw new UnsupportedOperationException("Narrowing, widening, and matching of names not supported for EDIPartyName");
    }

    public int subtreeDepth() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("subtreeDepth() not supported for EDIPartyName");
    }
}
