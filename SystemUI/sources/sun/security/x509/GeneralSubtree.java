package sun.security.x509;

import java.p026io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class GeneralSubtree {
    private static final int MIN_DEFAULT = 0;
    private static final byte TAG_MAX = 1;
    private static final byte TAG_MIN = 0;
    private int maximum;
    private int minimum;
    private int myhash;
    private GeneralName name;

    public GeneralSubtree(GeneralName generalName, int i, int i2) {
        this.myhash = -1;
        this.name = generalName;
        this.minimum = i;
        this.maximum = i2;
    }

    public GeneralSubtree(DerValue derValue) throws IOException {
        this.minimum = 0;
        this.maximum = -1;
        this.myhash = -1;
        if (derValue.tag == 48) {
            this.name = new GeneralName(derValue.data.getDerValue(), true);
            while (derValue.data.available() != 0) {
                DerValue derValue2 = derValue.data.getDerValue();
                if (derValue2.isContextSpecific((byte) 0) && !derValue2.isConstructed()) {
                    derValue2.resetTag((byte) 2);
                    this.minimum = derValue2.getInteger();
                } else if (!derValue2.isContextSpecific((byte) 1) || derValue2.isConstructed()) {
                    throw new IOException("Invalid encoding of GeneralSubtree.");
                } else {
                    derValue2.resetTag((byte) 2);
                    this.maximum = derValue2.getInteger();
                }
            }
            return;
        }
        throw new IOException("Invalid encoding for GeneralSubtree.");
    }

    public GeneralName getName() {
        return this.name;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public int getMaximum() {
        return this.maximum;
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder("\n   GeneralSubtree: [\n    GeneralName: ");
        GeneralName generalName = this.name;
        sb.append(generalName == null ? "" : generalName.toString());
        sb.append("\n    Minimum: ");
        sb.append(this.minimum);
        String sb2 = sb.toString();
        if (this.maximum == -1) {
            str = sb2 + "\t    Maximum: undefined";
        } else {
            str = sb2 + "\t    Maximum: " + this.maximum;
        }
        return str + "    ]\n";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GeneralSubtree)) {
            return false;
        }
        GeneralSubtree generalSubtree = (GeneralSubtree) obj;
        GeneralName generalName = this.name;
        if (generalName == null) {
            if (generalSubtree.name != null) {
                return false;
            }
        } else if (!generalName.equals(generalSubtree.name)) {
            return false;
        }
        if (this.minimum == generalSubtree.minimum && this.maximum == generalSubtree.maximum) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.myhash == -1) {
            this.myhash = 17;
            GeneralName generalName = this.name;
            if (generalName != null) {
                this.myhash = (17 * 37) + generalName.hashCode();
            }
            int i = this.minimum;
            if (i != 0) {
                this.myhash = (this.myhash * 37) + i;
            }
            int i2 = this.maximum;
            if (i2 != -1) {
                this.myhash = (this.myhash * 37) + i2;
            }
        }
        return this.myhash;
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        this.name.encode(derOutputStream2);
        if (this.minimum != 0) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putInteger(this.minimum);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 0), derOutputStream3);
        }
        if (this.maximum != -1) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putInteger(this.maximum);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 1), derOutputStream4);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }
}
