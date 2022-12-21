package android.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class WifiP2pInfo implements Parcelable {
    public static final Parcelable.Creator<WifiP2pInfo> CREATOR = new Parcelable.Creator<WifiP2pInfo>() {
        public WifiP2pInfo createFromParcel(Parcel parcel) {
            WifiP2pInfo wifiP2pInfo = new WifiP2pInfo();
            boolean z = false;
            wifiP2pInfo.groupFormed = parcel.readByte() == 1;
            if (parcel.readByte() == 1) {
                z = true;
            }
            wifiP2pInfo.isGroupOwner = z;
            if (parcel.readByte() == 1) {
                try {
                    wifiP2pInfo.groupOwnerAddress = InetAddress.getByAddress(parcel.createByteArray());
                } catch (UnknownHostException unused) {
                }
            }
            return wifiP2pInfo;
        }

        public WifiP2pInfo[] newArray(int i) {
            return new WifiP2pInfo[i];
        }
    };
    public boolean groupFormed;
    public InetAddress groupOwnerAddress;
    public boolean isGroupOwner;

    public int describeContents() {
        return 0;
    }

    public WifiP2pInfo() {
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("groupFormed: ");
        stringBuffer.append(this.groupFormed).append(" isGroupOwner: ").append(this.isGroupOwner).append(" groupOwnerAddress: ").append((Object) this.groupOwnerAddress);
        return stringBuffer.toString();
    }

    public WifiP2pInfo(WifiP2pInfo wifiP2pInfo) {
        if (wifiP2pInfo != null) {
            this.groupFormed = wifiP2pInfo.groupFormed;
            this.isGroupOwner = wifiP2pInfo.isGroupOwner;
            this.groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.groupFormed ? (byte) 1 : 0);
        parcel.writeByte(this.isGroupOwner ? (byte) 1 : 0);
        if (this.groupOwnerAddress != null) {
            parcel.writeByte((byte) 1);
            parcel.writeByteArray(this.groupOwnerAddress.getAddress());
            return;
        }
        parcel.writeByte((byte) 0);
    }
}
