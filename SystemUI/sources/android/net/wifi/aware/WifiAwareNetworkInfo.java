package android.net.wifi.aware;

import android.net.TransportInfo;
import android.os.Parcel;
import android.os.Parcelable;
import java.net.Inet6Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class WifiAwareNetworkInfo implements TransportInfo, Parcelable {
    public static final Parcelable.Creator<WifiAwareNetworkInfo> CREATOR = new Parcelable.Creator<WifiAwareNetworkInfo>() {
        public WifiAwareNetworkInfo createFromParcel(Parcel parcel) {
            NetworkInterface networkInterface;
            byte[] createByteArray = parcel.createByteArray();
            String readString = parcel.readString();
            int readInt = parcel.readInt();
            int readInt2 = parcel.readInt();
            ArrayList arrayList = new ArrayList();
            parcel.readTypedList(arrayList, WifiAwareChannelInfo.CREATOR);
            if (readString != null) {
                try {
                    networkInterface = NetworkInterface.getByName(readString);
                } catch (SocketException e) {
                    try {
                        e.printStackTrace();
                    } catch (UnknownHostException e2) {
                        e2.printStackTrace();
                        return new WifiAwareNetworkInfo();
                    }
                }
                return new WifiAwareNetworkInfo(Inet6Address.getByAddress((String) null, createByteArray, networkInterface), readInt, readInt2, arrayList);
            }
            networkInterface = null;
            return new WifiAwareNetworkInfo(Inet6Address.getByAddress((String) null, createByteArray, networkInterface), readInt, readInt2, arrayList);
        }

        public WifiAwareNetworkInfo[] newArray(int i) {
            return new WifiAwareNetworkInfo[i];
        }
    };
    private final List<WifiAwareChannelInfo> mChannelInfos;
    private final Inet6Address mIpv6Addr;
    private final int mPort;
    private final int mTransportProtocol;

    public int describeContents() {
        return 0;
    }

    public WifiAwareNetworkInfo() {
        this.mIpv6Addr = null;
        this.mPort = 0;
        this.mTransportProtocol = -1;
        this.mChannelInfos = Collections.emptyList();
    }

    public WifiAwareNetworkInfo(Inet6Address inet6Address) {
        this.mIpv6Addr = inet6Address;
        this.mPort = 0;
        this.mTransportProtocol = -1;
        this.mChannelInfos = Collections.emptyList();
    }

    public WifiAwareNetworkInfo(Inet6Address inet6Address, int i, int i2, List<WifiAwareChannelInfo> list) {
        this.mIpv6Addr = inet6Address;
        this.mPort = i;
        this.mTransportProtocol = i2;
        this.mChannelInfos = list;
    }

    private WifiAwareNetworkInfo(WifiAwareNetworkInfo wifiAwareNetworkInfo) {
        this.mIpv6Addr = wifiAwareNetworkInfo != null ? wifiAwareNetworkInfo.mIpv6Addr : null;
        this.mPort = wifiAwareNetworkInfo != null ? wifiAwareNetworkInfo.mPort : 0;
        this.mTransportProtocol = wifiAwareNetworkInfo != null ? wifiAwareNetworkInfo.mTransportProtocol : -1;
        this.mChannelInfos = wifiAwareNetworkInfo != null ? wifiAwareNetworkInfo.mChannelInfos : Collections.emptyList();
    }

    public Inet6Address getPeerIpv6Addr() {
        return this.mIpv6Addr;
    }

    public int getPort() {
        return this.mPort;
    }

    public int getTransportProtocol() {
        return this.mTransportProtocol;
    }

    public List<WifiAwareChannelInfo> getChannelInfoList() {
        return this.mChannelInfos;
    }

    public void writeToParcel(Parcel parcel, int i) {
        String str;
        parcel.writeByteArray(this.mIpv6Addr.getAddress());
        NetworkInterface scopedInterface = this.mIpv6Addr.getScopedInterface();
        if (scopedInterface == null) {
            str = null;
        } else {
            str = scopedInterface.getName();
        }
        parcel.writeString(str);
        parcel.writeInt(this.mPort);
        parcel.writeInt(this.mTransportProtocol);
        parcel.writeTypedList(this.mChannelInfos);
    }

    public String toString() {
        return "AwareNetworkInfo: IPv6=" + this.mIpv6Addr + ", port=" + this.mPort + ", transportProtocol=" + this.mTransportProtocol + ", channelInfos=" + this.mChannelInfos;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiAwareNetworkInfo)) {
            return false;
        }
        WifiAwareNetworkInfo wifiAwareNetworkInfo = (WifiAwareNetworkInfo) obj;
        if (!Objects.equals(this.mIpv6Addr, wifiAwareNetworkInfo.mIpv6Addr) || this.mPort != wifiAwareNetworkInfo.mPort || this.mTransportProtocol != wifiAwareNetworkInfo.mTransportProtocol || !this.mChannelInfos.equals(wifiAwareNetworkInfo.mChannelInfos)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mIpv6Addr, Integer.valueOf(this.mPort), Integer.valueOf(this.mTransportProtocol), this.mChannelInfos);
    }
}
