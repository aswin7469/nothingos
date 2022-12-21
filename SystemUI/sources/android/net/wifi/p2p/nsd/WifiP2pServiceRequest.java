package android.net.wifi.p2p.nsd;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Locale;

public class WifiP2pServiceRequest implements Parcelable {
    public static final Parcelable.Creator<WifiP2pServiceRequest> CREATOR = new Parcelable.Creator<WifiP2pServiceRequest>() {
        public WifiP2pServiceRequest createFromParcel(Parcel parcel) {
            return new WifiP2pServiceRequest(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readString());
        }

        public WifiP2pServiceRequest[] newArray(int i) {
            return new WifiP2pServiceRequest[i];
        }
    };
    private int mLength;
    private int mProtocolType;
    private String mQuery;
    private int mTransId;

    public int describeContents() {
        return 0;
    }

    protected WifiP2pServiceRequest(int i, String str) {
        validateQuery(str);
        this.mProtocolType = i;
        this.mQuery = str;
        if (str != null) {
            this.mLength = (str.length() / 2) + 2;
        } else {
            this.mLength = 2;
        }
    }

    private WifiP2pServiceRequest(int i, int i2, int i3, String str) {
        this.mProtocolType = i;
        this.mLength = i2;
        this.mTransId = i3;
        this.mQuery = str;
    }

    public int getTransactionId() {
        return this.mTransId;
    }

    public void setTransactionId(int i) {
        this.mTransId = i;
    }

    public String getSupplicantQuery() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format(Locale.f700US, "%02x", Integer.valueOf(this.mLength & 255)));
        stringBuffer.append(String.format(Locale.f700US, "%02x", Integer.valueOf((this.mLength >> 8) & 255)));
        stringBuffer.append(String.format(Locale.f700US, "%02x", Integer.valueOf(this.mProtocolType)));
        stringBuffer.append(String.format(Locale.f700US, "%02x", Integer.valueOf(this.mTransId)));
        String str = this.mQuery;
        if (str != null) {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    private void validateQuery(String str) {
        if (str != null) {
            if (str.length() % 2 == 1) {
                throw new IllegalArgumentException("query size is invalid. query=" + str);
            } else if (str.length() / 2 <= 65535) {
                String lowerCase = str.toLowerCase(Locale.ROOT);
                for (char c : lowerCase.toCharArray()) {
                    if ((c < '0' || c > '9') && (c < 'a' || c > 'f')) {
                        throw new IllegalArgumentException("query should be hex string. query=" + lowerCase);
                    }
                }
            } else {
                throw new IllegalArgumentException("query size is too large. len=" + str.length());
            }
        }
    }

    public static WifiP2pServiceRequest newInstance(int i, String str) {
        return new WifiP2pServiceRequest(i, str);
    }

    public static WifiP2pServiceRequest newInstance(int i) {
        return new WifiP2pServiceRequest(i, (String) null);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WifiP2pServiceRequest)) {
            return false;
        }
        WifiP2pServiceRequest wifiP2pServiceRequest = (WifiP2pServiceRequest) obj;
        if (wifiP2pServiceRequest.mProtocolType == this.mProtocolType && wifiP2pServiceRequest.mLength == this.mLength) {
            String str = wifiP2pServiceRequest.mQuery;
            if (str == null && this.mQuery == null) {
                return true;
            }
            if (str != null) {
                return str.equals(this.mQuery);
            }
        }
        return false;
    }

    public int hashCode() {
        int i = (((527 + this.mProtocolType) * 31) + this.mLength) * 31;
        String str = this.mQuery;
        return i + (str == null ? 0 : str.hashCode());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mProtocolType);
        parcel.writeInt(this.mLength);
        parcel.writeInt(this.mTransId);
        parcel.writeString(this.mQuery);
    }
}
