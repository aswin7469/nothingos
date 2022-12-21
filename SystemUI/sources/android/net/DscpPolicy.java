package android.net;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.net.module.util.InetAddressUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Range;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Objects;

@SystemApi
public final class DscpPolicy implements Parcelable {
    @SystemApi
    public static final Parcelable.Creator<DscpPolicy> CREATOR = new Parcelable.Creator<DscpPolicy>() {
        public DscpPolicy[] newArray(int i) {
            return new DscpPolicy[i];
        }

        public DscpPolicy createFromParcel(Parcel parcel) {
            return new DscpPolicy(parcel);
        }
    };
    public static final int PROTOCOL_ANY = -1;
    public static final int SOURCE_PORT_ANY = -1;
    private final int mDscp;
    private final InetAddress mDstAddr;
    private final Range<Integer> mDstPortRange;
    private final int mPolicyId;
    private final int mProtocol;
    private final InetAddress mSrcAddr;
    private final int mSrcPort;

    public int describeContents() {
        return 0;
    }

    DscpPolicy(int i, int i2, InetAddress inetAddress, InetAddress inetAddress2, int i3, int i4, Range<Integer> range) {
        this.mPolicyId = i;
        this.mDscp = i2;
        this.mSrcAddr = inetAddress;
        this.mDstAddr = inetAddress2;
        this.mSrcPort = i3;
        this.mProtocol = i4;
        this.mDstPortRange = range;
        if (i < 1 || i > 255) {
            throw new IllegalArgumentException("Policy ID not in valid range: " + i);
        } else if (i2 < 0 || i2 > 63) {
            throw new IllegalArgumentException("DSCP value not in valid range: " + i2);
        } else if (i3 < -1 || i3 > 65535) {
            throw new IllegalArgumentException("Source port not in valid range: " + i3);
        } else if (range != null && ((range.getLower().intValue() < 0 || range.getLower().intValue() > 65535) && (range.getUpper().intValue() < 0 || range.getUpper().intValue() > 65535))) {
            throw new IllegalArgumentException("Destination port not in valid range");
        } else if (inetAddress != null && inetAddress2 != null && (inetAddress instanceof Inet6Address) != (inetAddress2 instanceof Inet6Address)) {
            throw new IllegalArgumentException("Source/destination address of different family");
        }
    }

    public int getPolicyId() {
        return this.mPolicyId;
    }

    public int getDscpValue() {
        return this.mDscp;
    }

    public InetAddress getSourceAddress() {
        return this.mSrcAddr;
    }

    public InetAddress getDestinationAddress() {
        return this.mDstAddr;
    }

    public int getSourcePort() {
        return this.mSrcPort;
    }

    public int getProtocol() {
        return this.mProtocol;
    }

    public Range<Integer> getDestinationPortRange() {
        return this.mDstPortRange;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("DscpPolicy { policyId = ");
        sb.append(this.mPolicyId);
        sb.append(", dscp = ");
        sb.append(this.mDscp);
        sb.append(", srcAddr = ");
        sb.append((Object) this.mSrcAddr);
        sb.append(", dstAddr = ");
        sb.append((Object) this.mDstAddr);
        sb.append(", srcPort = ");
        sb.append(this.mSrcPort);
        sb.append(", protocol = ");
        sb.append(this.mProtocol);
        sb.append(", dstPortRange = ");
        Range<Integer> range = this.mDstPortRange;
        sb.append(range == null ? "none" : range.toString());
        sb.append(" }");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DscpPolicy)) {
            return false;
        }
        DscpPolicy dscpPolicy = (DscpPolicy) obj;
        if (this.mPolicyId == dscpPolicy.mPolicyId && this.mDscp == dscpPolicy.mDscp && Objects.equals(this.mSrcAddr, dscpPolicy.mSrcAddr) && Objects.equals(this.mDstAddr, dscpPolicy.mDstAddr) && this.mSrcPort == dscpPolicy.mSrcPort && this.mProtocol == dscpPolicy.mProtocol && Objects.equals(this.mDstPortRange, dscpPolicy.mDstPortRange)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mPolicyId), Integer.valueOf(this.mDscp), Integer.valueOf(this.mSrcAddr.hashCode()), Integer.valueOf(this.mDstAddr.hashCode()), Integer.valueOf(this.mSrcPort), Integer.valueOf(this.mProtocol), Integer.valueOf(this.mDstPortRange.hashCode()));
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPolicyId);
        parcel.writeInt(this.mDscp);
        InetAddressUtils.parcelInetAddress(parcel, this.mSrcAddr, i);
        InetAddressUtils.parcelInetAddress(parcel, this.mDstAddr, i);
        parcel.writeInt(this.mSrcPort);
        parcel.writeInt(this.mProtocol);
        parcel.writeBoolean(this.mDstPortRange != null);
        Range<Integer> range = this.mDstPortRange;
        if (range != null) {
            parcel.writeInt(range.getLower().intValue());
            parcel.writeInt(this.mDstPortRange.getUpper().intValue());
        }
    }

    DscpPolicy(Parcel parcel) {
        this.mPolicyId = parcel.readInt();
        this.mDscp = parcel.readInt();
        this.mSrcAddr = InetAddressUtils.unparcelInetAddress(parcel);
        this.mDstAddr = InetAddressUtils.unparcelInetAddress(parcel);
        this.mSrcPort = parcel.readInt();
        this.mProtocol = parcel.readInt();
        if (parcel.readBoolean()) {
            this.mDstPortRange = new Range<>(Integer.valueOf(parcel.readInt()), Integer.valueOf(parcel.readInt()));
        } else {
            this.mDstPortRange = null;
        }
    }

    public static final class Builder {
        private long mBuilderFieldsSet = 0;
        private final int mDscp;
        private InetAddress mDstAddr;
        private Range<Integer> mDstPortRange;
        private final int mPolicyId;
        private int mProtocol = -1;
        private InetAddress mSrcAddr;
        private int mSrcPort = -1;

        public Builder(int i, int i2) {
            this.mPolicyId = i;
            this.mDscp = i2;
        }

        public Builder setSourceAddress(InetAddress inetAddress) {
            this.mSrcAddr = inetAddress;
            return this;
        }

        public Builder setDestinationAddress(InetAddress inetAddress) {
            this.mDstAddr = inetAddress;
            return this;
        }

        public Builder setSourcePort(int i) {
            this.mSrcPort = i;
            return this;
        }

        public Builder setProtocol(int i) {
            this.mProtocol = i;
            return this;
        }

        public Builder setDestinationPortRange(Range<Integer> range) {
            this.mDstPortRange = range;
            return this;
        }

        public DscpPolicy build() {
            return new DscpPolicy(this.mPolicyId, this.mDscp, this.mSrcAddr, this.mDstAddr, this.mSrcPort, this.mProtocol, this.mDstPortRange);
        }
    }
}
