package android.net.wifi;

import android.net.MacAddress;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public final class MloLink implements Parcelable {
    public static final Parcelable.Creator<MloLink> CREATOR = new Parcelable.Creator<MloLink>() {
        public MloLink createFromParcel(Parcel parcel) {
            MloLink mloLink = new MloLink();
            mloLink.mBand = parcel.readInt();
            mloLink.mChannel = parcel.readInt();
            mloLink.mLinkId = parcel.readInt();
            mloLink.mState = parcel.readInt();
            mloLink.mApMacAddress = (MacAddress) parcel.readParcelable(MacAddress.class.getClassLoader());
            mloLink.mStaMacAddress = (MacAddress) parcel.readParcelable(MacAddress.class.getClassLoader());
            return mloLink;
        }

        public MloLink[] newArray(int i) {
            return new MloLink[i];
        }
    };
    public static final int INVALID_MLO_LINK_ID = -1;
    public static final int MAX_MLO_LINK_ID = 15;
    public static final int MIN_MLO_LINK_ID = 0;
    public static final int MLO_LINK_STATE_ACTIVE = 3;
    public static final int MLO_LINK_STATE_IDLE = 2;
    public static final int MLO_LINK_STATE_INVALID = 0;
    public static final int MLO_LINK_STATE_UNASSOCIATED = 1;
    /* access modifiers changed from: private */
    public MacAddress mApMacAddress;
    /* access modifiers changed from: private */
    public int mBand;
    /* access modifiers changed from: private */
    public int mChannel;
    /* access modifiers changed from: private */
    public int mLinkId;
    /* access modifiers changed from: private */
    public MacAddress mStaMacAddress;
    /* access modifiers changed from: private */
    public int mState;

    @Retention(RetentionPolicy.SOURCE)
    public @interface MloLinkState {
    }

    private String getStateString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "Unknown MLO link state" : "MLO_LINK_STATE_ACTIVE" : "MLO_LINK_STATE_IDLE" : "MLO_LINK_STATE_UNASSOCIATED" : "MLO_LINK_STATE_INVALID";
    }

    public static boolean isValidState(int i) {
        return i == 0 || i == 1 || i == 2 || i == 3;
    }

    public int describeContents() {
        return 0;
    }

    public MloLink() {
        this.mBand = 0;
        this.mChannel = 0;
        this.mState = 1;
        this.mApMacAddress = null;
        this.mStaMacAddress = null;
        this.mLinkId = -1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x001d, code lost:
        r0 = r7.mStaMacAddress;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MloLink(android.net.wifi.MloLink r7, long r8) {
        /*
            r6 = this;
            r6.<init>()
            int r0 = r7.mBand
            r6.mBand = r0
            int r0 = r7.mChannel
            r6.mChannel = r0
            int r0 = r7.mLinkId
            r6.mLinkId = r0
            int r0 = r7.mState
            r6.mState = r0
            r0 = 2
            long r0 = r0 & r8
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            r1 = 0
            if (r0 != 0) goto L_0x002b
            android.net.MacAddress r0 = r7.mStaMacAddress
            if (r0 != 0) goto L_0x0022
            goto L_0x002b
        L_0x0022:
            java.lang.String r0 = r0.toString()
            android.net.MacAddress r0 = android.net.MacAddress.fromString(r0)
            goto L_0x002c
        L_0x002b:
            r0 = r1
        L_0x002c:
            r6.mStaMacAddress = r0
            r4 = 1
            long r8 = r8 & r4
            int r8 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
            if (r8 != 0) goto L_0x0042
            android.net.MacAddress r7 = r7.mApMacAddress
            if (r7 != 0) goto L_0x003a
            goto L_0x0042
        L_0x003a:
            java.lang.String r7 = r7.toString()
            android.net.MacAddress r1 = android.net.MacAddress.fromString(r7)
        L_0x0042:
            r6.mApMacAddress = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.MloLink.<init>(android.net.wifi.MloLink, long):void");
    }

    public int getBand() {
        return this.mBand;
    }

    public int getChannel() {
        return this.mChannel;
    }

    public int getLinkId() {
        return this.mLinkId;
    }

    public int getState() {
        return this.mState;
    }

    public MacAddress getApMacAddress() {
        return this.mApMacAddress;
    }

    public MacAddress getStaMacAddress() {
        return this.mStaMacAddress;
    }

    public void setChannel(int i) {
        this.mChannel = i;
    }

    public void setBand(int i) {
        this.mBand = i;
    }

    public void setLinkId(int i) {
        this.mLinkId = i;
    }

    public void setState(int i) {
        this.mState = i;
    }

    public void setApMacAddress(MacAddress macAddress) {
        this.mApMacAddress = macAddress;
    }

    public void setStaMacAddress(MacAddress macAddress) {
        this.mStaMacAddress = macAddress;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MloLink mloLink = (MloLink) obj;
        if (this.mBand == mloLink.mBand && this.mChannel == mloLink.mChannel && this.mLinkId == mloLink.mLinkId && Objects.equals(this.mApMacAddress, mloLink.mApMacAddress) && Objects.equals(this.mStaMacAddress, mloLink.mStaMacAddress) && this.mState == mloLink.mState) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mBand), Integer.valueOf(this.mChannel), Integer.valueOf(this.mLinkId), this.mApMacAddress, this.mStaMacAddress, Integer.valueOf(this.mState));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("MloLink{");
        int i = this.mBand;
        if (i == 1) {
            sb.append("2.4GHz");
        } else if (i == 2) {
            sb.append("5GHz");
        } else if (i == 8) {
            sb.append("6GHz");
        } else {
            sb.append("UNKNOWN BAND");
        }
        sb.append(", channel: ");
        sb.append(this.mChannel);
        sb.append(", id: ");
        sb.append(this.mLinkId);
        sb.append(", state: ");
        sb.append(getStateString(this.mState));
        if (this.mApMacAddress != null) {
            sb.append(", AP MAC Address: ");
            sb.append(this.mApMacAddress.toString());
        }
        if (this.mStaMacAddress != null) {
            sb.append(", STA MAC Address: ");
            sb.append(this.mStaMacAddress.toString());
        }
        sb.append('}');
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mBand);
        parcel.writeInt(this.mChannel);
        parcel.writeInt(this.mLinkId);
        parcel.writeInt(this.mState);
        parcel.writeParcelable(this.mApMacAddress, i);
        parcel.writeParcelable(this.mStaMacAddress, i);
    }
}
