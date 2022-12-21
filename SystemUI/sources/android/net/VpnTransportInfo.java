package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class VpnTransportInfo implements TransportInfo, Parcelable {
    public static final Parcelable.Creator<VpnTransportInfo> CREATOR = new Parcelable.Creator<VpnTransportInfo>() {
        public VpnTransportInfo createFromParcel(Parcel parcel) {
            return new VpnTransportInfo(parcel.readInt(), parcel.readString());
        }

        public VpnTransportInfo[] newArray(int i) {
            return new VpnTransportInfo[i];
        }
    };
    private final String mSessionId;
    private final int mType;

    public int describeContents() {
        return 0;
    }

    public long getApplicableRedactions() {
        return 4;
    }

    public VpnTransportInfo makeCopy(long j) {
        String str;
        int i = this.mType;
        if ((j & 4) != 0) {
            str = null;
        } else {
            str = this.mSessionId;
        }
        return new VpnTransportInfo(i, str);
    }

    public VpnTransportInfo(int i, String str) {
        this.mType = i;
        this.mSessionId = str;
    }

    public String getSessionId() {
        return this.mSessionId;
    }

    public int getType() {
        return this.mType;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof VpnTransportInfo)) {
            return false;
        }
        VpnTransportInfo vpnTransportInfo = (VpnTransportInfo) obj;
        if (this.mType != vpnTransportInfo.mType || !TextUtils.equals(this.mSessionId, vpnTransportInfo.mSessionId)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mType), this.mSessionId);
    }

    public String toString() {
        return String.format("VpnTransportInfo{type=%d, sessionId=%s}", Integer.valueOf(this.mType), this.mSessionId);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
        parcel.writeString(this.mSessionId);
    }
}
