package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.wifi.ScanResult;
import android.net.wifi.aware.PeerHandle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public final class ResponderConfig implements Parcelable {
    private static final int AWARE_BAND_2_DISCOVERY_CHANNEL = 2437;
    @SystemApi
    public static final int CHANNEL_WIDTH_160MHZ = 3;
    @SystemApi
    public static final int CHANNEL_WIDTH_20MHZ = 0;
    @SystemApi
    public static final int CHANNEL_WIDTH_320MHZ = 5;
    @SystemApi
    public static final int CHANNEL_WIDTH_40MHZ = 1;
    @SystemApi
    public static final int CHANNEL_WIDTH_80MHZ = 2;
    @SystemApi
    public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;
    public static final Parcelable.Creator<ResponderConfig> CREATOR = new Parcelable.Creator<ResponderConfig>() {
        public ResponderConfig[] newArray(int i) {
            return new ResponderConfig[i];
        }

        public ResponderConfig createFromParcel(Parcel parcel) {
            PeerHandle peerHandle = null;
            MacAddress createFromParcel = parcel.readBoolean() ? MacAddress.CREATOR.createFromParcel(parcel) : null;
            if (parcel.readBoolean()) {
                peerHandle = new PeerHandle(parcel.readInt());
            }
            PeerHandle peerHandle2 = peerHandle;
            int readInt = parcel.readInt();
            boolean z = parcel.readInt() == 1;
            int readInt2 = parcel.readInt();
            int readInt3 = parcel.readInt();
            int readInt4 = parcel.readInt();
            int readInt5 = parcel.readInt();
            int readInt6 = parcel.readInt();
            if (peerHandle2 == null) {
                return new ResponderConfig(createFromParcel, readInt, z, readInt2, readInt3, readInt4, readInt5, readInt6);
            }
            return new ResponderConfig(peerHandle2, readInt, z, readInt2, readInt3, readInt4, readInt5, readInt6);
        }
    };
    @SystemApi
    public static final int PREAMBLE_EHT = 4;
    @SystemApi
    public static final int PREAMBLE_HE = 3;
    @SystemApi
    public static final int PREAMBLE_HT = 1;
    @SystemApi
    public static final int PREAMBLE_LEGACY = 0;
    @SystemApi
    public static final int PREAMBLE_VHT = 2;
    @SystemApi
    public static final int RESPONDER_AP = 0;
    @SystemApi
    public static final int RESPONDER_AWARE = 4;
    @SystemApi
    public static final int RESPONDER_P2P_CLIENT = 3;
    @SystemApi
    public static final int RESPONDER_P2P_GO = 2;
    @SystemApi
    public static final int RESPONDER_STA = 1;
    private static final String TAG = "ResponderConfig";
    @SystemApi
    public final int centerFreq0;
    @SystemApi
    public final int centerFreq1;
    @SystemApi
    public final int channelWidth;
    @SystemApi
    public final int frequency;
    @SystemApi
    public final MacAddress macAddress;
    @SystemApi
    public final PeerHandle peerHandle;
    @SystemApi
    public final int preamble;
    @SystemApi
    public final int responderType;
    @SystemApi
    public final boolean supports80211mc;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ChannelWidth {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PreambleType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ResponderType {
    }

    public int describeContents() {
        return 0;
    }

    @SystemApi
    public ResponderConfig(MacAddress macAddress2, int i, boolean z, int i2, int i3, int i4, int i5, int i6) {
        if (macAddress2 != null) {
            this.macAddress = macAddress2;
            this.peerHandle = null;
            this.responderType = i;
            this.supports80211mc = z;
            this.channelWidth = i2;
            this.frequency = i3;
            this.centerFreq0 = i4;
            this.centerFreq1 = i5;
            this.preamble = i6;
            return;
        }
        throw new IllegalArgumentException("Invalid ResponderConfig - must specify a MAC address");
    }

    @SystemApi
    public ResponderConfig(PeerHandle peerHandle2, int i, boolean z, int i2, int i3, int i4, int i5, int i6) {
        this.macAddress = null;
        this.peerHandle = peerHandle2;
        this.responderType = i;
        this.supports80211mc = z;
        this.channelWidth = i2;
        this.frequency = i3;
        this.centerFreq0 = i4;
        this.centerFreq1 = i5;
        this.preamble = i6;
    }

    public ResponderConfig(MacAddress macAddress2, PeerHandle peerHandle2, int i, boolean z, int i2, int i3, int i4, int i5, int i6) {
        this.macAddress = macAddress2;
        this.peerHandle = peerHandle2;
        this.responderType = i;
        this.supports80211mc = z;
        this.channelWidth = i2;
        this.frequency = i3;
        this.centerFreq0 = i4;
        this.centerFreq1 = i5;
        this.preamble = i6;
    }

    public static ResponderConfig fromScanResult(ScanResult scanResult) {
        int i;
        ScanResult scanResult2 = scanResult;
        MacAddress fromString = MacAddress.fromString(scanResult2.BSSID);
        boolean is80211mcResponder = scanResult.is80211mcResponder();
        int translateFromScanResultToLocalChannelWidth = translateFromScanResultToLocalChannelWidth(scanResult2.channelWidth);
        int i2 = scanResult2.frequency;
        int i3 = scanResult2.centerFreq0;
        int i4 = scanResult2.centerFreq1;
        int i5 = 3;
        if (scanResult2.informationElements == null || scanResult2.informationElements.length == 0) {
            Log.e(TAG, "Scan Results do not contain IEs - using backup method to select preamble");
            i = translateFromScanResultToLocalChannelWidth == 5 ? 4 : (translateFromScanResultToLocalChannelWidth == 2 || translateFromScanResultToLocalChannelWidth == 3) ? 2 : 1;
        } else {
            boolean z = false;
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            for (ScanResult.InformationElement informationElement : scanResult2.informationElements) {
                if (informationElement.f54id == 45) {
                    z3 = true;
                } else if (informationElement.f54id == 191) {
                    z4 = true;
                } else if (informationElement.f54id == 255 && informationElement.idExt == 35) {
                    z2 = true;
                } else if (informationElement.f54id == 255 && informationElement.idExt == 108) {
                    z = true;
                }
            }
            if (z && ScanResult.is6GHz(i2)) {
                i5 = 4;
            } else if (!z2 || !ScanResult.is6GHz(i2)) {
                i5 = z4 ? 2 : z3 ? 1 : 0;
            }
            i = i5;
        }
        return new ResponderConfig(fromString, 0, is80211mcResponder, translateFromScanResultToLocalChannelWidth, i2, i3, i4, i);
    }

    @SystemApi
    public static ResponderConfig fromWifiAwarePeerMacAddressWithDefaults(MacAddress macAddress2) {
        return new ResponderConfig(macAddress2, 4, true, 0, (int) AWARE_BAND_2_DISCOVERY_CHANNEL, 0, 0, 1);
    }

    @SystemApi
    public static ResponderConfig fromWifiAwarePeerHandleWithDefaults(PeerHandle peerHandle2) {
        return new ResponderConfig(peerHandle2, 4, true, 0, (int) AWARE_BAND_2_DISCOVERY_CHANNEL, 0, 0, 1);
    }

    public boolean isValid(boolean z) {
        MacAddress macAddress2 = this.macAddress;
        if ((macAddress2 == null && this.peerHandle == null) || (macAddress2 != null && this.peerHandle != null)) {
            return false;
        }
        if (z || this.responderType != 4) {
            return true;
        }
        return false;
    }

    public MacAddress getMacAddress() {
        return this.macAddress;
    }

    public boolean is80211mcSupported() {
        return this.supports80211mc;
    }

    public int getChannelWidth() {
        return translateFromLocalToScanResultChannelWidth(this.channelWidth);
    }

    public int getFrequencyMhz() {
        return this.frequency;
    }

    public int getCenterFreq0Mhz() {
        return this.centerFreq0;
    }

    public int getCenterFreq1Mhz() {
        return this.centerFreq1;
    }

    public int getPreamble() {
        return translateFromLocalToScanResultPreamble(this.preamble);
    }

    public static final class Builder {
        private int mCenterFreq0 = 0;
        private int mCenterFreq1 = 0;
        private int mChannelWidth = 0;
        private int mFrequency = 0;
        private MacAddress mMacAddress;
        private int mPreamble = 0;
        private int mResponderType;
        private boolean mSupports80211Mc = true;

        public Builder setMacAddress(MacAddress macAddress) {
            this.mMacAddress = macAddress;
            return this;
        }

        public Builder set80211mcSupported(boolean z) {
            this.mSupports80211Mc = z;
            return this;
        }

        public Builder setChannelWidth(int i) {
            this.mChannelWidth = ResponderConfig.translateFromScanResultToLocalChannelWidth(i);
            return this;
        }

        public Builder setFrequencyMhz(int i) {
            this.mFrequency = i;
            return this;
        }

        public Builder setCenterFreq0Mhz(int i) {
            this.mCenterFreq0 = i;
            return this;
        }

        public Builder setCenterFreq1Mhz(int i) {
            this.mCenterFreq1 = i;
            return this;
        }

        public Builder setPreamble(int i) {
            this.mPreamble = ResponderConfig.translateFromScanResultToLocalPreamble(i);
            return this;
        }

        public ResponderConfig build() {
            if (this.mMacAddress != null) {
                this.mResponderType = 0;
                return new ResponderConfig(this.mMacAddress, this.mResponderType, this.mSupports80211Mc, this.mChannelWidth, this.mFrequency, this.mCenterFreq0, this.mCenterFreq1, this.mPreamble);
            }
            throw new IllegalArgumentException("Invalid ResponderConfig - must specify a MAC address");
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.macAddress == null) {
            parcel.writeBoolean(false);
        } else {
            parcel.writeBoolean(true);
            this.macAddress.writeToParcel(parcel, i);
        }
        if (this.peerHandle == null) {
            parcel.writeBoolean(false);
        } else {
            parcel.writeBoolean(true);
            parcel.writeInt(this.peerHandle.peerId);
        }
        parcel.writeInt(this.responderType);
        parcel.writeInt(this.supports80211mc ? 1 : 0);
        parcel.writeInt(this.channelWidth);
        parcel.writeInt(this.frequency);
        parcel.writeInt(this.centerFreq0);
        parcel.writeInt(this.centerFreq1);
        parcel.writeInt(this.preamble);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ResponderConfig)) {
            return false;
        }
        ResponderConfig responderConfig = (ResponderConfig) obj;
        if (Objects.equals(this.macAddress, responderConfig.macAddress) && Objects.equals(this.peerHandle, responderConfig.peerHandle) && this.responderType == responderConfig.responderType && this.supports80211mc == responderConfig.supports80211mc && this.channelWidth == responderConfig.channelWidth && this.frequency == responderConfig.frequency && this.centerFreq0 == responderConfig.centerFreq0 && this.centerFreq1 == responderConfig.centerFreq1 && this.preamble == responderConfig.preamble) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.macAddress, this.peerHandle, Integer.valueOf(this.responderType), Boolean.valueOf(this.supports80211mc), Integer.valueOf(this.channelWidth), Integer.valueOf(this.frequency), Integer.valueOf(this.centerFreq0), Integer.valueOf(this.centerFreq1), Integer.valueOf(this.preamble));
    }

    public String toString() {
        StringBuffer append = new StringBuffer("ResponderConfig: macAddress=").append((Object) this.macAddress).append(", peerHandle=");
        PeerHandle peerHandle2 = this.peerHandle;
        return append.append(peerHandle2 == null ? "<null>" : Integer.valueOf(peerHandle2.peerId)).append(", responderType=").append(this.responderType).append(", supports80211mc=").append(this.supports80211mc).append(", channelWidth=").append(this.channelWidth).append(", frequency=").append(this.frequency).append(", centerFreq0=").append(this.centerFreq0).append(", centerFreq1=").append(this.centerFreq1).append(", preamble=").append(this.preamble).toString();
    }

    static int translateFromScanResultToLocalChannelWidth(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    i2 = 4;
                    if (i != 4) {
                        if (i == 5) {
                            return 5;
                        }
                        throw new IllegalArgumentException("translateFromScanResultChannelWidth: bad " + i);
                    }
                }
            }
        }
        return i2;
    }

    static int translateFromLocalToScanResultChannelWidth(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    i2 = 4;
                    if (i != 4) {
                        if (i == 5) {
                            return 5;
                        }
                        throw new IllegalArgumentException("translateFromLocalChannelWidth: bad " + i);
                    }
                }
            }
        }
        return i2;
    }

    static int translateFromScanResultToLocalPreamble(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    if (i == 4) {
                        return 4;
                    }
                    throw new IllegalArgumentException("translateFromScanResultPreamble: bad " + i);
                }
            }
        }
        return i2;
    }

    static int translateFromLocalToScanResultPreamble(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    if (i == 4) {
                        return 4;
                    }
                    throw new IllegalArgumentException("translateFromLocalPreamble: bad " + i);
                }
            }
        }
        return i2;
    }
}
