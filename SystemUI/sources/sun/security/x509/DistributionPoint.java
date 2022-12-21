package sun.security.x509;

import java.p026io.IOException;
import java.util.Arrays;
import java.util.Objects;
import sun.security.util.BitArray;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class DistributionPoint {
    public static final int AA_COMPROMISE = 8;
    public static final int AFFILIATION_CHANGED = 3;
    public static final int CA_COMPROMISE = 2;
    public static final int CERTIFICATE_HOLD = 6;
    public static final int CESSATION_OF_OPERATION = 5;
    public static final int KEY_COMPROMISE = 1;
    public static final int PRIVILEGE_WITHDRAWN = 7;
    private static final String[] REASON_STRINGS = {null, "key compromise", "CA compromise", "affiliation changed", ReasonFlags.SUPERSEDED, "cessation of operation", "certificate hold", "privilege withdrawn", "AA compromise"};
    public static final int SUPERSEDED = 4;
    private static final byte TAG_DIST_PT = 0;
    private static final byte TAG_FULL_NAME = 0;
    private static final byte TAG_ISSUER = 2;
    private static final byte TAG_REASONS = 1;
    private static final byte TAG_REL_NAME = 1;
    private GeneralNames crlIssuer;
    private GeneralNames fullName;
    private volatile int hashCode;
    private boolean[] reasonFlags;
    private RDN relativeName;

    public DistributionPoint(GeneralNames generalNames, boolean[] zArr, GeneralNames generalNames2) {
        if (generalNames == null && generalNames2 == null) {
            throw new IllegalArgumentException("fullName and crlIssuer may not both be null");
        }
        this.fullName = generalNames;
        this.reasonFlags = zArr;
        this.crlIssuer = generalNames2;
    }

    public DistributionPoint(RDN rdn, boolean[] zArr, GeneralNames generalNames) {
        if (rdn == null && generalNames == null) {
            throw new IllegalArgumentException("relativeName and crlIssuer may not both be null");
        }
        this.relativeName = rdn;
        this.reasonFlags = zArr;
        this.crlIssuer = generalNames;
    }

    public DistributionPoint(DerValue derValue) throws IOException {
        if (derValue.tag == 48) {
            while (derValue.data != null && derValue.data.available() != 0) {
                DerValue derValue2 = derValue.data.getDerValue();
                if (!derValue2.isContextSpecific((byte) 0) || !derValue2.isConstructed()) {
                    if (!derValue2.isContextSpecific((byte) 1) || derValue2.isConstructed()) {
                        if (!derValue2.isContextSpecific((byte) 2) || !derValue2.isConstructed()) {
                            throw new IOException("Invalid encoding of DistributionPoint.");
                        } else if (this.crlIssuer == null) {
                            derValue2.resetTag((byte) 48);
                            this.crlIssuer = new GeneralNames(derValue2);
                        } else {
                            throw new IOException("Duplicate CRLIssuer in DistributionPoint.");
                        }
                    } else if (this.reasonFlags == null) {
                        derValue2.resetTag((byte) 3);
                        this.reasonFlags = derValue2.getUnalignedBitString().toBooleanArray();
                    } else {
                        throw new IOException("Duplicate Reasons in DistributionPoint.");
                    }
                } else if (this.fullName == null && this.relativeName == null) {
                    DerValue derValue3 = derValue2.data.getDerValue();
                    if (derValue3.isContextSpecific((byte) 0) && derValue3.isConstructed()) {
                        derValue3.resetTag((byte) 48);
                        this.fullName = new GeneralNames(derValue3);
                    } else if (!derValue3.isContextSpecific((byte) 1) || !derValue3.isConstructed()) {
                        throw new IOException("Invalid DistributionPointName in DistributionPoint");
                    } else {
                        derValue3.resetTag((byte) 49);
                        this.relativeName = new RDN(derValue3);
                    }
                } else {
                    throw new IOException("Duplicate DistributionPointName in DistributionPoint.");
                }
            }
            if (this.crlIssuer == null && this.fullName == null && this.relativeName == null) {
                throw new IOException("One of fullName, relativeName,  and crlIssuer has to be set");
            }
            return;
        }
        throw new IOException("Invalid encoding of DistributionPoint.");
    }

    public GeneralNames getFullName() {
        return this.fullName;
    }

    public RDN getRelativeName() {
        return this.relativeName;
    }

    public boolean[] getReasonFlags() {
        return this.reasonFlags;
    }

    public GeneralNames getCRLIssuer() {
        return this.crlIssuer;
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        if (!(this.fullName == null && this.relativeName == null)) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            if (this.fullName != null) {
                DerOutputStream derOutputStream4 = new DerOutputStream();
                this.fullName.encode(derOutputStream4);
                derOutputStream3.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream4);
            } else if (this.relativeName != null) {
                DerOutputStream derOutputStream5 = new DerOutputStream();
                this.relativeName.encode(derOutputStream5);
                derOutputStream3.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream5);
            }
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream3);
        }
        if (this.reasonFlags != null) {
            DerOutputStream derOutputStream6 = new DerOutputStream();
            derOutputStream6.putTruncatedUnalignedBitString(new BitArray(this.reasonFlags));
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 1), derOutputStream6);
        }
        if (this.crlIssuer != null) {
            DerOutputStream derOutputStream7 = new DerOutputStream();
            this.crlIssuer.encode(derOutputStream7);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream7);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DistributionPoint)) {
            return false;
        }
        DistributionPoint distributionPoint = (DistributionPoint) obj;
        if (!Objects.equals(this.fullName, distributionPoint.fullName) || !Objects.equals(this.relativeName, distributionPoint.relativeName) || !Objects.equals(this.crlIssuer, distributionPoint.crlIssuer) || !Arrays.equals(this.reasonFlags, distributionPoint.reasonFlags)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = this.hashCode;
        if (i != 0) {
            return i;
        }
        GeneralNames generalNames = this.fullName;
        int i2 = 1;
        if (generalNames != null) {
            i2 = 1 + generalNames.hashCode();
        }
        RDN rdn = this.relativeName;
        if (rdn != null) {
            i2 += rdn.hashCode();
        }
        GeneralNames generalNames2 = this.crlIssuer;
        if (generalNames2 != null) {
            i2 += generalNames2.hashCode();
        }
        if (this.reasonFlags != null) {
            int i3 = 0;
            while (true) {
                boolean[] zArr = this.reasonFlags;
                if (i3 >= zArr.length) {
                    break;
                }
                if (zArr[i3]) {
                    i2 += i3;
                }
                i3++;
            }
        }
        int i4 = i2;
        this.hashCode = i4;
        return i4;
    }

    private static String reasonToString(int i) {
        if (i > 0) {
            String[] strArr = REASON_STRINGS;
            if (i < strArr.length) {
                return strArr[i];
            }
        }
        return "Unknown reason " + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.fullName != null) {
            sb.append("DistributionPoint:\n     " + this.fullName + "\n");
        }
        if (this.relativeName != null) {
            sb.append("DistributionPoint:\n     " + this.relativeName + "\n");
        }
        if (this.reasonFlags != null) {
            sb.append("   ReasonFlags:\n");
            int i = 0;
            while (true) {
                boolean[] zArr = this.reasonFlags;
                if (i >= zArr.length) {
                    break;
                }
                if (zArr[i]) {
                    sb.append("    " + reasonToString(i) + "\n");
                }
                i++;
            }
        }
        if (this.crlIssuer != null) {
            sb.append("   CRLIssuer:" + this.crlIssuer + "\n");
        }
        return sb.toString();
    }
}
