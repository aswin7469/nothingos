package sun.security.x509;

import java.p026io.IOException;
import java.util.Objects;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class DistributionPointName {
    private static final byte TAG_FULL_NAME = 0;
    private static final byte TAG_RELATIVE_NAME = 1;
    private GeneralNames fullName = null;
    private volatile int hashCode;
    private RDN relativeName = null;

    public DistributionPointName(GeneralNames generalNames) {
        if (generalNames != null) {
            this.fullName = generalNames;
            return;
        }
        throw new IllegalArgumentException("fullName must not be null");
    }

    public DistributionPointName(RDN rdn) {
        if (rdn != null) {
            this.relativeName = rdn;
            return;
        }
        throw new IllegalArgumentException("relativeName must not be null");
    }

    public DistributionPointName(DerValue derValue) throws IOException {
        if (derValue.isContextSpecific((byte) 0) && derValue.isConstructed()) {
            derValue.resetTag((byte) 48);
            this.fullName = new GeneralNames(derValue);
        } else if (!derValue.isContextSpecific((byte) 1) || !derValue.isConstructed()) {
            throw new IOException("Invalid encoding for DistributionPointName");
        } else {
            derValue.resetTag((byte) 49);
            this.relativeName = new RDN(derValue);
        }
    }

    public GeneralNames getFullName() {
        return this.fullName;
    }

    public RDN getRelativeName() {
        return this.relativeName;
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        GeneralNames generalNames = this.fullName;
        if (generalNames != null) {
            generalNames.encode(derOutputStream2);
            derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
            return;
        }
        this.relativeName.encode(derOutputStream2);
        derOutputStream.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DistributionPointName)) {
            return false;
        }
        DistributionPointName distributionPointName = (DistributionPointName) obj;
        if (!Objects.equals(this.fullName, distributionPointName.fullName) || !Objects.equals(this.relativeName, distributionPointName.relativeName)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2 = this.hashCode;
        if (i2 != 0) {
            return i2;
        }
        GeneralNames generalNames = this.fullName;
        if (generalNames != null) {
            i = generalNames.hashCode();
        } else {
            i = this.relativeName.hashCode();
        }
        int i3 = i + 1;
        this.hashCode = i3;
        return i3;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.fullName != null) {
            sb.append("DistributionPointName:\n     " + this.fullName + "\n");
        } else {
            sb.append("DistributionPointName:\n     " + this.relativeName + "\n");
        }
        return sb.toString();
    }
}
