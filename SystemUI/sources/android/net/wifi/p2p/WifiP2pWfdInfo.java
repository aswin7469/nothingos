package android.net.wifi.p2p;

import android.net.TrafficStats;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public final class WifiP2pWfdInfo implements Parcelable {
    public static final Parcelable.Creator<WifiP2pWfdInfo> CREATOR = new Parcelable.Creator<WifiP2pWfdInfo>() {
        public WifiP2pWfdInfo createFromParcel(Parcel parcel) {
            WifiP2pWfdInfo wifiP2pWfdInfo = new WifiP2pWfdInfo();
            wifiP2pWfdInfo.readFromParcel(parcel);
            return wifiP2pWfdInfo;
        }

        public WifiP2pWfdInfo[] newArray(int i) {
            return new WifiP2pWfdInfo[i];
        }
    };
    public static final int DEVICE_INFO_AUDIO_ONLY_SUPPORT_AT_SOURCE = 2048;
    public static final int DEVICE_INFO_AUDIO_UNSUPPORTED_AT_PRIMARY_SINK = 1024;
    public static final int DEVICE_INFO_CONTENT_PROTECTION_SUPPORT = 256;
    public static final int DEVICE_INFO_COUPLED_SINK_SUPPORT_AT_SINK = 8;
    public static final int DEVICE_INFO_COUPLED_SINK_SUPPORT_AT_SOURCE = 4;
    public static final int DEVICE_INFO_DEVICE_TYPE_MASK = 3;
    public static final int DEVICE_INFO_PREFERRED_CONNECTIVITY_MASK = 128;
    public static final int DEVICE_INFO_SESSION_AVAILABLE_MASK = 48;
    public static final int DEVICE_INFO_TDLS_PERSISTENT_GROUP = 4096;
    public static final int DEVICE_INFO_TDLS_PERSISTENT_GROUP_REINVOKE = 8192;
    public static final int DEVICE_INFO_TIME_SYNCHRONIZATION_SUPPORT = 512;
    public static final int DEVICE_INFO_WFD_SERVICE_DISCOVERY_SUPPORT = 64;
    public static final int DEVICE_TYPE_PRIMARY_SINK = 1;
    public static final int DEVICE_TYPE_SECONDARY_SINK = 2;
    public static final int DEVICE_TYPE_SOURCE_OR_PRIMARY_SINK = 3;
    public static final int DEVICE_TYPE_WFD_SOURCE = 0;
    public static final int PREFERRED_CONNECTIVITY_P2P = 0;
    public static final int PREFERRED_CONNECTIVITY_TDLS = 1;
    private static final int SESSION_AVAILABLE_BIT1 = 16;
    private static final int SESSION_AVAILABLE_BIT2 = 32;
    private int mCtrlPort;
    private int mDeviceInfo;
    private boolean mEnabled;
    private int mMaxThroughput;
    private int mR2DeviceInfo;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DeviceInfoMask {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DeviceType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PreferredConnectivity {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface R2DeviceInfoMask {
    }

    public int describeContents() {
        return 0;
    }

    public WifiP2pWfdInfo() {
        this.mR2DeviceInfo = -1;
    }

    public WifiP2pWfdInfo(int i, int i2, int i3) {
        this.mEnabled = true;
        this.mDeviceInfo = i;
        this.mCtrlPort = i2;
        this.mMaxThroughput = i3;
        this.mR2DeviceInfo = -1;
    }

    public int getDeviceInfo() {
        return this.mDeviceInfo;
    }

    public void setR2DeviceInfo(int i) {
        this.mR2DeviceInfo = i;
    }

    public int getR2DeviceInfo() {
        return this.mR2DeviceInfo;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isR2Supported() {
        return this.mR2DeviceInfo >= 0;
    }

    public void setEnabled(boolean z) {
        this.mEnabled = z;
    }

    public boolean setR2DeviceType(int i) {
        if (!SdkLevel.isAtLeastS()) {
            throw new UnsupportedOperationException();
        } else if (i != 0 && 1 != i && 3 != i) {
            return false;
        } else {
            if (!isR2Supported()) {
                this.mR2DeviceInfo = 0;
            }
            this.mR2DeviceInfo = i | (this.mR2DeviceInfo & -4);
            return true;
        }
    }

    public int getDeviceType() {
        return this.mDeviceInfo & 3;
    }

    public int getR2DeviceType() {
        return this.mR2DeviceInfo & 3;
    }

    public boolean setDeviceType(int i) {
        if (i < 0 || i > 3) {
            return false;
        }
        this.mDeviceInfo = i | (this.mDeviceInfo & -4);
        return true;
    }

    public boolean isSessionAvailable() {
        return (this.mDeviceInfo & 48) != 0;
    }

    public void setSessionAvailable(boolean z) {
        if (z) {
            this.mDeviceInfo = (this.mDeviceInfo | 16) & -33;
        } else {
            this.mDeviceInfo &= -49;
        }
    }

    public boolean isContentProtectionSupported() {
        return (this.mDeviceInfo & 256) != 0;
    }

    public void setContentProtectionSupported(boolean z) {
        if (z) {
            this.mDeviceInfo |= 256;
        } else {
            this.mDeviceInfo &= TrafficStats.TAG_NETWORK_STACK_RANGE_END;
        }
    }

    public boolean isCoupledSinkSupportedAtSource() {
        return (this.mDeviceInfo & 4) != 0;
    }

    public void setCoupledSinkSupportAtSource(boolean z) {
        if (z) {
            this.mDeviceInfo |= 4;
        } else {
            this.mDeviceInfo &= -5;
        }
    }

    public boolean isCoupledSinkSupportedAtSink() {
        return (this.mDeviceInfo & 8) != 0;
    }

    public void setCoupledSinkSupportAtSink(boolean z) {
        if (z) {
            this.mDeviceInfo |= 8;
        } else {
            this.mDeviceInfo &= -9;
        }
    }

    public int getControlPort() {
        return this.mCtrlPort;
    }

    public void setControlPort(int i) {
        this.mCtrlPort = i;
    }

    public void setMaxThroughput(int i) {
        this.mMaxThroughput = i;
    }

    public int getMaxThroughput() {
        return this.mMaxThroughput;
    }

    public String getDeviceInfoHex() {
        return String.format(Locale.f698US, "%04x%04x%04x", Integer.valueOf(this.mDeviceInfo), Integer.valueOf(this.mCtrlPort), Integer.valueOf(this.mMaxThroughput));
    }

    public String getR2DeviceInfoHex() {
        return String.format(Locale.f698US, "%04x%04x", 2, Integer.valueOf(this.mR2DeviceInfo));
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("WFD enabled: ");
        stringBuffer.append(this.mEnabled);
        stringBuffer.append("WFD DeviceInfo: ").append(this.mDeviceInfo);
        stringBuffer.append("\n WFD CtrlPort: ").append(this.mCtrlPort);
        stringBuffer.append("\n WFD MaxThroughput: ").append(this.mMaxThroughput);
        stringBuffer.append("\n WFD R2 DeviceInfo: ").append(this.mR2DeviceInfo);
        return stringBuffer.toString();
    }

    public WifiP2pWfdInfo(WifiP2pWfdInfo wifiP2pWfdInfo) {
        this.mR2DeviceInfo = -1;
        if (wifiP2pWfdInfo != null) {
            this.mEnabled = wifiP2pWfdInfo.mEnabled;
            this.mDeviceInfo = wifiP2pWfdInfo.mDeviceInfo;
            this.mCtrlPort = wifiP2pWfdInfo.mCtrlPort;
            this.mMaxThroughput = wifiP2pWfdInfo.mMaxThroughput;
            this.mR2DeviceInfo = wifiP2pWfdInfo.mR2DeviceInfo;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mEnabled ? 1 : 0);
        parcel.writeInt(this.mDeviceInfo);
        parcel.writeInt(this.mCtrlPort);
        parcel.writeInt(this.mMaxThroughput);
        parcel.writeInt(this.mR2DeviceInfo);
    }

    /* access modifiers changed from: private */
    public void readFromParcel(Parcel parcel) {
        boolean z = true;
        if (parcel.readInt() != 1) {
            z = false;
        }
        this.mEnabled = z;
        this.mDeviceInfo = parcel.readInt();
        this.mCtrlPort = parcel.readInt();
        this.mMaxThroughput = parcel.readInt();
        this.mR2DeviceInfo = parcel.readInt();
    }
}
