package android.net.wifi.p2p;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WifiP2pGroup implements Parcelable {
    public static final Parcelable.Creator<WifiP2pGroup> CREATOR = new Parcelable.Creator<WifiP2pGroup>() {
        public WifiP2pGroup createFromParcel(Parcel parcel) {
            WifiP2pGroup wifiP2pGroup = new WifiP2pGroup();
            wifiP2pGroup.setNetworkName(parcel.readString());
            wifiP2pGroup.setOwner((WifiP2pDevice) parcel.readParcelable((ClassLoader) null));
            boolean z = true;
            if (parcel.readByte() != 1) {
                z = false;
            }
            wifiP2pGroup.setIsGroupOwner(z);
            int readInt = parcel.readInt();
            for (int i = 0; i < readInt; i++) {
                wifiP2pGroup.addClient((WifiP2pDevice) parcel.readParcelable((ClassLoader) null));
            }
            wifiP2pGroup.setPassphrase(parcel.readString());
            wifiP2pGroup.setInterface(parcel.readString());
            wifiP2pGroup.setNetworkId(parcel.readInt());
            wifiP2pGroup.setFrequency(parcel.readInt());
            return wifiP2pGroup;
        }

        public WifiP2pGroup[] newArray(int i) {
            return new WifiP2pGroup[i];
        }
    };
    public static final int NETWORK_ID_PERSISTENT = -2;
    public static final int NETWORK_ID_TEMPORARY = -1;
    public static final int TEMPORARY_NET_ID = -1;
    private static final Pattern groupStartedPattern = Pattern.compile("ssid=\"(.+)\" freq=(\\d+) (?:psk=)?([0-9a-fA-F]{64})?(?:passphrase=)?(?:\"(.{0,63})\")? go_dev_addr=((?:[0-9a-f]{2}:){5}[0-9a-f]{2}) ?(\\[PERSISTENT\\])?");
    private List<WifiP2pDevice> mClients = new ArrayList();
    private int mFrequency;
    private String mInterface;
    private boolean mIsGroupOwner;
    private int mNetId;
    private String mNetworkName;
    private WifiP2pDevice mOwner;
    private String mPassphrase;

    public int describeContents() {
        return 0;
    }

    public WifiP2pGroup() {
    }

    public WifiP2pGroup(String str) throws IllegalArgumentException {
        String[] split = str.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        if (split.length < 3) {
            throw new IllegalArgumentException("Malformed supplicant event");
        } else if (split[0].startsWith("P2P-GROUP")) {
            this.mInterface = split[1];
            this.mIsGroupOwner = split[2].equals("GO");
            Matcher matcher = groupStartedPattern.matcher(str);
            if (matcher.find()) {
                this.mNetworkName = matcher.group(1);
                this.mFrequency = Integer.parseInt(matcher.group(2));
                this.mPassphrase = matcher.group(4);
                this.mOwner = new WifiP2pDevice(matcher.group(5));
                if (matcher.group(6) != null) {
                    this.mNetId = -2;
                } else {
                    this.mNetId = -1;
                }
            }
        } else if (split[0].equals("P2P-INVITATION-RECEIVED")) {
            this.mNetId = -2;
            for (String split2 : split) {
                String[] split3 = split2.split("=");
                if (split3.length == 2) {
                    if (split3[0].equals("sa")) {
                        String str2 = split3[1];
                        WifiP2pDevice wifiP2pDevice = new WifiP2pDevice();
                        wifiP2pDevice.deviceAddress = split3[1];
                        this.mClients.add(wifiP2pDevice);
                    } else if (split3[0].equals("go_dev_addr")) {
                        this.mOwner = new WifiP2pDevice(split3[1]);
                    } else if (split3[0].equals("persistent")) {
                        this.mNetId = Integer.parseInt(split3[1]);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Malformed supplicant event");
        }
    }

    public void setNetworkName(String str) {
        this.mNetworkName = str;
    }

    public String getNetworkName() {
        return this.mNetworkName;
    }

    public void setIsGroupOwner(boolean z) {
        this.mIsGroupOwner = z;
    }

    public boolean isGroupOwner() {
        return this.mIsGroupOwner;
    }

    public void setOwner(WifiP2pDevice wifiP2pDevice) {
        this.mOwner = wifiP2pDevice;
    }

    public WifiP2pDevice getOwner() {
        return this.mOwner;
    }

    public void addClient(String str) {
        addClient(new WifiP2pDevice(str));
    }

    public void addClient(WifiP2pDevice wifiP2pDevice) {
        for (WifiP2pDevice equals : this.mClients) {
            if (equals.equals(wifiP2pDevice)) {
                return;
            }
        }
        this.mClients.add(wifiP2pDevice);
    }

    public boolean removeClient(String str) {
        return this.mClients.remove((Object) new WifiP2pDevice(str));
    }

    public boolean removeClient(WifiP2pDevice wifiP2pDevice) {
        return this.mClients.remove((Object) wifiP2pDevice);
    }

    public boolean isClientListEmpty() {
        return this.mClients.size() == 0;
    }

    public boolean contains(WifiP2pDevice wifiP2pDevice) {
        return this.mOwner.equals(wifiP2pDevice) || this.mClients.contains(wifiP2pDevice);
    }

    public Collection<WifiP2pDevice> getClientList() {
        return Collections.unmodifiableCollection(this.mClients);
    }

    public void setPassphrase(String str) {
        this.mPassphrase = str;
    }

    public String getPassphrase() {
        return this.mPassphrase;
    }

    public void setInterface(String str) {
        this.mInterface = str;
    }

    public String getInterface() {
        return this.mInterface;
    }

    public int getNetworkId() {
        return this.mNetId;
    }

    public void setNetworkId(int i) {
        this.mNetId = i;
    }

    public int getFrequency() {
        return this.mFrequency;
    }

    public void setFrequency(int i) {
        this.mFrequency = i;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("network: ");
        stringBuffer.append(this.mNetworkName);
        stringBuffer.append("\n isGO: ").append(this.mIsGroupOwner);
        stringBuffer.append("\n GO: ").append((Object) this.mOwner);
        for (WifiP2pDevice append : this.mClients) {
            stringBuffer.append("\n Client: ").append((Object) append);
        }
        stringBuffer.append("\n interface: ").append(this.mInterface);
        stringBuffer.append("\n networkId: ").append(this.mNetId);
        stringBuffer.append("\n frequency: ").append(this.mFrequency);
        return stringBuffer.toString();
    }

    public WifiP2pGroup(WifiP2pGroup wifiP2pGroup) {
        if (wifiP2pGroup != null) {
            this.mNetworkName = wifiP2pGroup.getNetworkName();
            this.mOwner = new WifiP2pDevice(wifiP2pGroup.getOwner());
            this.mIsGroupOwner = wifiP2pGroup.mIsGroupOwner;
            for (WifiP2pDevice add : wifiP2pGroup.getClientList()) {
                this.mClients.add(add);
            }
            this.mPassphrase = wifiP2pGroup.getPassphrase();
            this.mInterface = wifiP2pGroup.getInterface();
            this.mNetId = wifiP2pGroup.getNetworkId();
            this.mFrequency = wifiP2pGroup.getFrequency();
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mNetworkName);
        parcel.writeParcelable(this.mOwner, i);
        parcel.writeByte(this.mIsGroupOwner ? (byte) 1 : 0);
        parcel.writeInt(this.mClients.size());
        for (WifiP2pDevice writeParcelable : this.mClients) {
            parcel.writeParcelable(writeParcelable, i);
        }
        parcel.writeString(this.mPassphrase);
        parcel.writeString(this.mInterface);
        parcel.writeInt(this.mNetId);
        parcel.writeInt(this.mFrequency);
    }
}
