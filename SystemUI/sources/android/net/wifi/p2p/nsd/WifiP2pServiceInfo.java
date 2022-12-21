package android.net.wifi.p2p.nsd;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class WifiP2pServiceInfo implements Parcelable {
    public static final Parcelable.Creator<WifiP2pServiceInfo> CREATOR = new Parcelable.Creator<WifiP2pServiceInfo>() {
        public WifiP2pServiceInfo createFromParcel(Parcel parcel) {
            ArrayList arrayList = new ArrayList();
            parcel.readStringList(arrayList);
            return new WifiP2pServiceInfo(arrayList);
        }

        public WifiP2pServiceInfo[] newArray(int i) {
            return new WifiP2pServiceInfo[i];
        }
    };
    public static final int SERVICE_TYPE_ALL = 0;
    public static final int SERVICE_TYPE_BONJOUR = 1;
    public static final int SERVICE_TYPE_UPNP = 2;
    public static final int SERVICE_TYPE_VENDOR_SPECIFIC = 255;
    public static final int SERVICE_TYPE_WS_DISCOVERY = 3;
    private List<String> mQueryList;

    public int describeContents() {
        return 0;
    }

    protected WifiP2pServiceInfo(List<String> list) {
        if (list != null) {
            this.mQueryList = list;
            return;
        }
        throw new IllegalArgumentException("query list cannot be null");
    }

    public List<String> getSupplicantQueryList() {
        return this.mQueryList;
    }

    static String bin2HexStr(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        int length = bArr.length;
        int i = 0;
        while (i < length) {
            try {
                String hexString = Integer.toHexString(bArr[i] & 255);
                if (hexString.length() == 1) {
                    stringBuffer.append('0');
                }
                stringBuffer.append(hexString);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return stringBuffer.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WifiP2pServiceInfo)) {
            return false;
        }
        return this.mQueryList.equals(((WifiP2pServiceInfo) obj).mQueryList);
    }

    public int hashCode() {
        List<String> list = this.mQueryList;
        return 527 + (list == null ? 0 : list.hashCode());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(this.mQueryList);
    }
}
