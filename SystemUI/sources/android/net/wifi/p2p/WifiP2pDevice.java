package android.net.wifi.p2p;

import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WifiP2pDevice implements Parcelable {
    public static final int AVAILABLE = 3;
    public static final int CONNECTED = 0;
    public static final Parcelable.Creator<WifiP2pDevice> CREATOR = new Parcelable.Creator<WifiP2pDevice>() {
        public WifiP2pDevice createFromParcel(Parcel parcel) {
            WifiP2pDevice wifiP2pDevice = new WifiP2pDevice();
            wifiP2pDevice.deviceName = parcel.readString();
            wifiP2pDevice.deviceAddress = parcel.readString();
            wifiP2pDevice.primaryDeviceType = parcel.readString();
            wifiP2pDevice.secondaryDeviceType = parcel.readString();
            wifiP2pDevice.wpsConfigMethodsSupported = parcel.readInt();
            wifiP2pDevice.deviceCapability = parcel.readInt();
            wifiP2pDevice.groupCapability = parcel.readInt();
            wifiP2pDevice.status = parcel.readInt();
            if (parcel.readInt() == 1) {
                wifiP2pDevice.wfdInfo = WifiP2pWfdInfo.CREATOR.createFromParcel(parcel);
            }
            wifiP2pDevice.mVendorElements = parcel.createTypedArrayList(ScanResult.InformationElement.CREATOR);
            return wifiP2pDevice;
        }

        public WifiP2pDevice[] newArray(int i) {
            return new WifiP2pDevice[i];
        }
    };
    private static final int DEVICE_CAPAB_CLIENT_DISCOVERABILITY = 2;
    private static final int DEVICE_CAPAB_CONCURRENT_OPER = 4;
    private static final int DEVICE_CAPAB_DEVICE_LIMIT = 16;
    private static final int DEVICE_CAPAB_INFRA_MANAGED = 8;
    private static final int DEVICE_CAPAB_INVITATION_PROCEDURE = 32;
    private static final int DEVICE_CAPAB_SERVICE_DISCOVERY = 1;
    public static final int FAILED = 2;
    private static final int GROUP_CAPAB_CROSS_CONN = 16;
    private static final int GROUP_CAPAB_GROUP_FORMATION = 64;
    private static final int GROUP_CAPAB_GROUP_LIMIT = 4;
    private static final int GROUP_CAPAB_GROUP_OWNER = 1;
    private static final int GROUP_CAPAB_INTRA_BSS_DIST = 8;
    private static final int GROUP_CAPAB_PERSISTENT_GROUP = 2;
    private static final int GROUP_CAPAB_PERSISTENT_RECONN = 32;
    public static final int INVITED = 1;
    private static final String TAG = "WifiP2pDevice";
    public static final int UNAVAILABLE = 4;
    private static final int WPS_CONFIG_DISPLAY = 8;
    private static final int WPS_CONFIG_KEYPAD = 256;
    private static final int WPS_CONFIG_PUSHBUTTON = 128;
    private static final Pattern detailedDevicePattern = Pattern.compile("((?:[0-9a-f]{2}:){5}[0-9a-f]{2}) (\\d+ )?p2p_dev_addr=((?:[0-9a-f]{2}:){5}[0-9a-f]{2}) pri_dev_type=(\\d+-[0-9a-fA-F]+-\\d+) name='(.*)' config_methods=(0x[0-9a-fA-F]+) dev_capab=(0x[0-9a-fA-F]+) group_capab=(0x[0-9a-fA-F]+)( wfd_dev_info=0x([0-9a-fA-F]{12}))?( wfd_r2_dev_info=0x([0-9a-fA-F]{4}))?");
    private static final Pattern threeTokenPattern = Pattern.compile("(?:[0-9a-f]{2}:){5}[0-9a-f]{2} p2p_dev_addr=((?:[0-9a-f]{2}:){5}[0-9a-f]{2})");
    private static final Pattern twoTokenPattern = Pattern.compile("(p2p_dev_addr=)?((?:[0-9a-f]{2}:){5}[0-9a-f]{2})");
    public String deviceAddress = "";
    public int deviceCapability;
    public String deviceName = "";
    public int groupCapability;
    /* access modifiers changed from: private */
    public List<ScanResult.InformationElement> mVendorElements;
    public String primaryDeviceType;
    public String secondaryDeviceType;
    public int status = 4;
    public WifiP2pWfdInfo wfdInfo;
    public int wpsConfigMethodsSupported;

    public int describeContents() {
        return 0;
    }

    public WifiP2pDevice() {
    }

    public WifiP2pDevice(String str) throws IllegalArgumentException {
        String group;
        String group2;
        String[] split = str.split("[ \n]");
        if (split.length >= 1) {
            int length = split.length;
            if (length == 1) {
                this.deviceAddress = str;
            } else if (length == 2) {
                Matcher matcher = twoTokenPattern.matcher(str);
                if (matcher.find()) {
                    this.deviceAddress = matcher.group(2);
                    return;
                }
                throw new IllegalArgumentException("Malformed supplicant event");
            } else if (length != 3) {
                Matcher matcher2 = detailedDevicePattern.matcher(str);
                if (matcher2.find()) {
                    this.deviceAddress = matcher2.group(3);
                    this.primaryDeviceType = matcher2.group(4);
                    this.deviceName = matcher2.group(5);
                    this.wpsConfigMethodsSupported = parseHex(matcher2.group(6));
                    this.deviceCapability = parseHex(matcher2.group(7));
                    this.groupCapability = parseHex(matcher2.group(8));
                    if (!(matcher2.group(9) == null || (group = matcher2.group(10)) == null)) {
                        this.wfdInfo = new WifiP2pWfdInfo(parseHex(group.substring(0, 4)), parseHex(group.substring(4, 8)), parseHex(group.substring(8, 12)));
                        if (!(matcher2.group(11) == null || !SdkLevel.isAtLeastS() || (group2 = matcher2.group(12)) == null)) {
                            this.wfdInfo.setR2DeviceType(parseHex(group2.substring(0, 4)));
                        }
                    }
                    if (split[0].startsWith("P2P-DEVICE-FOUND")) {
                        this.status = 3;
                        return;
                    }
                    return;
                }
                throw new IllegalArgumentException("Malformed supplicant event");
            } else {
                Matcher matcher3 = threeTokenPattern.matcher(str);
                if (matcher3.find()) {
                    this.deviceAddress = matcher3.group(1);
                    return;
                }
                throw new IllegalArgumentException("Malformed supplicant event");
            }
        } else {
            throw new IllegalArgumentException("Malformed supplicant event");
        }
    }

    public WifiP2pWfdInfo getWfdInfo() {
        return this.wfdInfo;
    }

    public boolean wpsPbcSupported() {
        return (this.wpsConfigMethodsSupported & 128) != 0;
    }

    public boolean wpsKeypadSupported() {
        return (this.wpsConfigMethodsSupported & 256) != 0;
    }

    public boolean wpsDisplaySupported() {
        return (this.wpsConfigMethodsSupported & 8) != 0;
    }

    public boolean isServiceDiscoveryCapable() {
        return (this.deviceCapability & 1) != 0;
    }

    public boolean isInvitationCapable() {
        return (this.deviceCapability & 32) != 0;
    }

    public boolean isDeviceLimit() {
        return (this.deviceCapability & 16) != 0;
    }

    public boolean isGroupOwner() {
        return (this.groupCapability & 1) != 0;
    }

    public boolean isGroupLimit() {
        return (this.groupCapability & 4) != 0;
    }

    public void update(WifiP2pDevice wifiP2pDevice) {
        updateSupplicantDetails(wifiP2pDevice);
        this.status = wifiP2pDevice.status;
    }

    public void updateSupplicantDetails(WifiP2pDevice wifiP2pDevice) {
        if (wifiP2pDevice != null) {
            String str = wifiP2pDevice.deviceAddress;
            if (str == null) {
                throw new IllegalArgumentException("deviceAddress is null");
            } else if (this.deviceAddress.equals(str)) {
                this.deviceName = wifiP2pDevice.deviceName;
                this.primaryDeviceType = wifiP2pDevice.primaryDeviceType;
                this.secondaryDeviceType = wifiP2pDevice.secondaryDeviceType;
                this.wpsConfigMethodsSupported = wifiP2pDevice.wpsConfigMethodsSupported;
                this.deviceCapability = wifiP2pDevice.deviceCapability;
                this.groupCapability = wifiP2pDevice.groupCapability;
                this.wfdInfo = wifiP2pDevice.wfdInfo;
            } else {
                throw new IllegalArgumentException("deviceAddress does not match");
            }
        } else {
            throw new IllegalArgumentException("device is null");
        }
    }

    public void setVendorElements(List<ScanResult.InformationElement> list) {
        if (list == null) {
            this.mVendorElements = null;
        } else {
            this.mVendorElements = new ArrayList(list);
        }
    }

    public List<ScanResult.InformationElement> getVendorElements() {
        if (this.mVendorElements == null) {
            return Collections.emptyList();
        }
        return new ArrayList(this.mVendorElements);
    }

    public boolean equals(Object obj) {
        String str;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiP2pDevice)) {
            return false;
        }
        WifiP2pDevice wifiP2pDevice = (WifiP2pDevice) obj;
        if (wifiP2pDevice != null && (str = wifiP2pDevice.deviceAddress) != null) {
            return str.equals(this.deviceAddress);
        }
        if (this.deviceAddress == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.deviceAddress);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("Device: ");
        stringBuffer.append(this.deviceName);
        stringBuffer.append("\n deviceAddress: ").append(this.deviceAddress);
        stringBuffer.append("\n primary type: ").append(this.primaryDeviceType);
        stringBuffer.append("\n secondary type: ").append(this.secondaryDeviceType);
        stringBuffer.append("\n wps: ").append(this.wpsConfigMethodsSupported);
        stringBuffer.append("\n grpcapab: ").append(this.groupCapability);
        stringBuffer.append("\n devcapab: ").append(this.deviceCapability);
        stringBuffer.append("\n status: ").append(this.status);
        stringBuffer.append("\n wfdInfo: ").append((Object) this.wfdInfo);
        stringBuffer.append("\n vendorElements: ").append((Object) this.mVendorElements);
        return stringBuffer.toString();
    }

    public WifiP2pDevice(WifiP2pDevice wifiP2pDevice) {
        if (wifiP2pDevice != null) {
            this.deviceName = wifiP2pDevice.deviceName;
            this.deviceAddress = wifiP2pDevice.deviceAddress;
            this.primaryDeviceType = wifiP2pDevice.primaryDeviceType;
            this.secondaryDeviceType = wifiP2pDevice.secondaryDeviceType;
            this.wpsConfigMethodsSupported = wifiP2pDevice.wpsConfigMethodsSupported;
            this.deviceCapability = wifiP2pDevice.deviceCapability;
            this.groupCapability = wifiP2pDevice.groupCapability;
            this.status = wifiP2pDevice.status;
            if (wifiP2pDevice.wfdInfo != null) {
                this.wfdInfo = new WifiP2pWfdInfo(wifiP2pDevice.wfdInfo);
            }
            if (wifiP2pDevice.mVendorElements != null) {
                this.mVendorElements = new ArrayList(wifiP2pDevice.mVendorElements);
            }
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.deviceName);
        parcel.writeString(this.deviceAddress);
        parcel.writeString(this.primaryDeviceType);
        parcel.writeString(this.secondaryDeviceType);
        parcel.writeInt(this.wpsConfigMethodsSupported);
        parcel.writeInt(this.deviceCapability);
        parcel.writeInt(this.groupCapability);
        parcel.writeInt(this.status);
        if (this.wfdInfo != null) {
            parcel.writeInt(1);
            this.wfdInfo.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeTypedList(this.mVendorElements);
    }

    private int parseHex(String str) {
        if (str.startsWith("0x") || str.startsWith("0X")) {
            str = str.substring(2);
        }
        try {
            return Integer.parseInt(str, 16);
        } catch (NumberFormatException unused) {
            Log.e(TAG, "Failed to parse hex string " + str);
            return 0;
        }
    }
}
