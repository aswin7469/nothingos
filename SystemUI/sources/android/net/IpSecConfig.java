package android.net;

import android.os.Parcel;
import android.os.Parcelable;

public final class IpSecConfig implements Parcelable {
    public static final Parcelable.Creator<IpSecConfig> CREATOR = new Parcelable.Creator<IpSecConfig>() {
        public IpSecConfig createFromParcel(Parcel parcel) {
            return new IpSecConfig(parcel);
        }

        public IpSecConfig[] newArray(int i) {
            return new IpSecConfig[i];
        }
    };
    private static final String TAG = "IpSecConfig";
    private IpSecAlgorithm mAuthenticatedEncryption;
    private IpSecAlgorithm mAuthentication;
    private String mDestinationAddress;
    private int mEncapRemotePort;
    private int mEncapSocketResourceId;
    private int mEncapType;
    private IpSecAlgorithm mEncryption;
    private int mMarkMask;
    private int mMarkValue;
    private int mMode;
    private int mNattKeepaliveInterval;
    private Network mNetwork;
    private String mSourceAddress;
    private int mSpiResourceId;
    private int mXfrmInterfaceId;

    public int describeContents() {
        return 0;
    }

    public void setMode(int i) {
        this.mMode = i;
    }

    public void setSourceAddress(String str) {
        this.mSourceAddress = str;
    }

    public void setDestinationAddress(String str) {
        this.mDestinationAddress = str;
    }

    public void setSpiResourceId(int i) {
        this.mSpiResourceId = i;
    }

    public void setEncryption(IpSecAlgorithm ipSecAlgorithm) {
        this.mEncryption = ipSecAlgorithm;
    }

    public void setAuthentication(IpSecAlgorithm ipSecAlgorithm) {
        this.mAuthentication = ipSecAlgorithm;
    }

    public void setAuthenticatedEncryption(IpSecAlgorithm ipSecAlgorithm) {
        this.mAuthenticatedEncryption = ipSecAlgorithm;
    }

    public void setNetwork(Network network) {
        this.mNetwork = network;
    }

    public void setEncapType(int i) {
        this.mEncapType = i;
    }

    public void setEncapSocketResourceId(int i) {
        this.mEncapSocketResourceId = i;
    }

    public void setEncapRemotePort(int i) {
        this.mEncapRemotePort = i;
    }

    public void setNattKeepaliveInterval(int i) {
        this.mNattKeepaliveInterval = i;
    }

    public void setMarkValue(int i) {
        this.mMarkValue = i;
    }

    public void setMarkMask(int i) {
        this.mMarkMask = i;
    }

    public void setXfrmInterfaceId(int i) {
        this.mXfrmInterfaceId = i;
    }

    public int getMode() {
        return this.mMode;
    }

    public String getSourceAddress() {
        return this.mSourceAddress;
    }

    public int getSpiResourceId() {
        return this.mSpiResourceId;
    }

    public String getDestinationAddress() {
        return this.mDestinationAddress;
    }

    public IpSecAlgorithm getEncryption() {
        return this.mEncryption;
    }

    public IpSecAlgorithm getAuthentication() {
        return this.mAuthentication;
    }

    public IpSecAlgorithm getAuthenticatedEncryption() {
        return this.mAuthenticatedEncryption;
    }

    public Network getNetwork() {
        return this.mNetwork;
    }

    public int getEncapType() {
        return this.mEncapType;
    }

    public int getEncapSocketResourceId() {
        return this.mEncapSocketResourceId;
    }

    public int getEncapRemotePort() {
        return this.mEncapRemotePort;
    }

    public int getNattKeepaliveInterval() {
        return this.mNattKeepaliveInterval;
    }

    public int getMarkValue() {
        return this.mMarkValue;
    }

    public int getMarkMask() {
        return this.mMarkMask;
    }

    public int getXfrmInterfaceId() {
        return this.mXfrmInterfaceId;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mMode);
        parcel.writeString(this.mSourceAddress);
        parcel.writeString(this.mDestinationAddress);
        parcel.writeParcelable(this.mNetwork, i);
        parcel.writeInt(this.mSpiResourceId);
        parcel.writeParcelable(this.mEncryption, i);
        parcel.writeParcelable(this.mAuthentication, i);
        parcel.writeParcelable(this.mAuthenticatedEncryption, i);
        parcel.writeInt(this.mEncapType);
        parcel.writeInt(this.mEncapSocketResourceId);
        parcel.writeInt(this.mEncapRemotePort);
        parcel.writeInt(this.mNattKeepaliveInterval);
        parcel.writeInt(this.mMarkValue);
        parcel.writeInt(this.mMarkMask);
        parcel.writeInt(this.mXfrmInterfaceId);
    }

    public IpSecConfig() {
        this.mMode = 0;
        this.mSourceAddress = "";
        this.mDestinationAddress = "";
        this.mSpiResourceId = -1;
        this.mEncapType = 0;
        this.mEncapSocketResourceId = -1;
    }

    public IpSecConfig(IpSecConfig ipSecConfig) {
        this.mMode = 0;
        this.mSourceAddress = "";
        this.mDestinationAddress = "";
        this.mSpiResourceId = -1;
        this.mEncapType = 0;
        this.mEncapSocketResourceId = -1;
        this.mMode = ipSecConfig.mMode;
        this.mSourceAddress = ipSecConfig.mSourceAddress;
        this.mDestinationAddress = ipSecConfig.mDestinationAddress;
        this.mNetwork = ipSecConfig.mNetwork;
        this.mSpiResourceId = ipSecConfig.mSpiResourceId;
        this.mEncryption = ipSecConfig.mEncryption;
        this.mAuthentication = ipSecConfig.mAuthentication;
        this.mAuthenticatedEncryption = ipSecConfig.mAuthenticatedEncryption;
        this.mEncapType = ipSecConfig.mEncapType;
        this.mEncapSocketResourceId = ipSecConfig.mEncapSocketResourceId;
        this.mEncapRemotePort = ipSecConfig.mEncapRemotePort;
        this.mNattKeepaliveInterval = ipSecConfig.mNattKeepaliveInterval;
        this.mMarkValue = ipSecConfig.mMarkValue;
        this.mMarkMask = ipSecConfig.mMarkMask;
        this.mXfrmInterfaceId = ipSecConfig.mXfrmInterfaceId;
    }

    private IpSecConfig(Parcel parcel) {
        this.mMode = 0;
        this.mSourceAddress = "";
        this.mDestinationAddress = "";
        this.mSpiResourceId = -1;
        this.mEncapType = 0;
        this.mEncapSocketResourceId = -1;
        this.mMode = parcel.readInt();
        this.mSourceAddress = parcel.readString();
        this.mDestinationAddress = parcel.readString();
        this.mNetwork = (Network) parcel.readParcelable(Network.class.getClassLoader(), Network.class);
        this.mSpiResourceId = parcel.readInt();
        this.mEncryption = (IpSecAlgorithm) parcel.readParcelable(IpSecAlgorithm.class.getClassLoader(), IpSecAlgorithm.class);
        this.mAuthentication = (IpSecAlgorithm) parcel.readParcelable(IpSecAlgorithm.class.getClassLoader(), IpSecAlgorithm.class);
        this.mAuthenticatedEncryption = (IpSecAlgorithm) parcel.readParcelable(IpSecAlgorithm.class.getClassLoader(), IpSecAlgorithm.class);
        this.mEncapType = parcel.readInt();
        this.mEncapSocketResourceId = parcel.readInt();
        this.mEncapRemotePort = parcel.readInt();
        this.mNattKeepaliveInterval = parcel.readInt();
        this.mMarkValue = parcel.readInt();
        this.mMarkMask = parcel.readInt();
        this.mXfrmInterfaceId = parcel.readInt();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{mMode=");
        sb.append(this.mMode == 1 ? "TUNNEL" : "TRANSPORT");
        sb.append(", mSourceAddress=");
        sb.append(this.mSourceAddress);
        sb.append(", mDestinationAddress=");
        sb.append(this.mDestinationAddress);
        sb.append(", mNetwork=");
        sb.append((Object) this.mNetwork);
        sb.append(", mEncapType=");
        sb.append(this.mEncapType);
        sb.append(", mEncapSocketResourceId=");
        sb.append(this.mEncapSocketResourceId);
        sb.append(", mEncapRemotePort=");
        sb.append(this.mEncapRemotePort);
        sb.append(", mNattKeepaliveInterval=");
        sb.append(this.mNattKeepaliveInterval);
        sb.append("{mSpiResourceId=");
        sb.append(this.mSpiResourceId);
        sb.append(", mEncryption=");
        sb.append((Object) this.mEncryption);
        sb.append(", mAuthentication=");
        sb.append((Object) this.mAuthentication);
        sb.append(", mAuthenticatedEncryption=");
        sb.append((Object) this.mAuthenticatedEncryption);
        sb.append(", mMarkValue=");
        sb.append(this.mMarkValue);
        sb.append(", mMarkMask=");
        sb.append(this.mMarkMask);
        sb.append(", mXfrmInterfaceId=");
        sb.append(this.mXfrmInterfaceId);
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof IpSecConfig)) {
            return false;
        }
        IpSecConfig ipSecConfig = (IpSecConfig) obj;
        if (this.mMode != ipSecConfig.mMode || !this.mSourceAddress.equals(ipSecConfig.mSourceAddress) || !this.mDestinationAddress.equals(ipSecConfig.mDestinationAddress)) {
            return false;
        }
        Network network = this.mNetwork;
        if (((network != null && network.equals(ipSecConfig.mNetwork)) || this.mNetwork == ipSecConfig.mNetwork) && this.mEncapType == ipSecConfig.mEncapType && this.mEncapSocketResourceId == ipSecConfig.mEncapSocketResourceId && this.mEncapRemotePort == ipSecConfig.mEncapRemotePort && this.mNattKeepaliveInterval == ipSecConfig.mNattKeepaliveInterval && this.mSpiResourceId == ipSecConfig.mSpiResourceId && IpSecAlgorithm.equals(this.mEncryption, ipSecConfig.mEncryption) && IpSecAlgorithm.equals(this.mAuthenticatedEncryption, ipSecConfig.mAuthenticatedEncryption) && IpSecAlgorithm.equals(this.mAuthentication, ipSecConfig.mAuthentication) && this.mMarkValue == ipSecConfig.mMarkValue && this.mMarkMask == ipSecConfig.mMarkMask && this.mXfrmInterfaceId == ipSecConfig.mXfrmInterfaceId) {
            return true;
        }
        return false;
    }
}
