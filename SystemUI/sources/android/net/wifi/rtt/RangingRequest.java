package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.wifi.ScanResult;
import android.net.wifi.aware.PeerHandle;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public final class RangingRequest implements Parcelable {
    public static final Parcelable.Creator<RangingRequest> CREATOR = new Parcelable.Creator<RangingRequest>() {
        public RangingRequest[] newArray(int i) {
            return new RangingRequest[i];
        }

        public RangingRequest createFromParcel(Parcel parcel) {
            return new RangingRequest(parcel.readArrayList((ClassLoader) null), parcel.readInt());
        }
    };
    private static final int DEFAULT_RTT_BURST_SIZE = 8;
    private static final int MAX_PEERS = 10;
    private static final int MAX_RTT_BURST_SIZE = 31;
    private static final int MIN_RTT_BURST_SIZE = 2;
    public final int mRttBurstSize;
    public final List<ResponderConfig> mRttPeers;

    public static int getDefaultRttBurstSize() {
        return 8;
    }

    public static int getMaxPeers() {
        return 10;
    }

    public static int getMaxRttBurstSize() {
        return 31;
    }

    public static int getMinRttBurstSize() {
        return 2;
    }

    public int describeContents() {
        return 0;
    }

    private RangingRequest(List<ResponderConfig> list, int i) {
        this.mRttPeers = list;
        this.mRttBurstSize = i;
    }

    @SystemApi
    public List<ResponderConfig> getRttResponders() {
        return this.mRttPeers;
    }

    public int getRttBurstSize() {
        return this.mRttBurstSize;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.mRttPeers);
        parcel.writeInt(this.mRttBurstSize);
    }

    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ", "RangingRequest: mRttPeers=[", NavigationBarInflaterView.SIZE_MOD_END);
        for (ResponderConfig responderConfig : this.mRttPeers) {
            stringJoiner.add(responderConfig.toString());
        }
        return stringJoiner.toString();
    }

    public void enforceValidity(boolean z) {
        if (this.mRttPeers.size() <= 10) {
            for (ResponderConfig isValid : this.mRttPeers) {
                if (!isValid.isValid(z)) {
                    throw new IllegalArgumentException("Invalid Responder specification");
                }
            }
            if (this.mRttBurstSize < getMinRttBurstSize() || this.mRttBurstSize > getMaxRttBurstSize()) {
                throw new IllegalArgumentException("RTT burst size is out of range");
            }
            return;
        }
        throw new IllegalArgumentException("Ranging to too many peers requested. Use getMaxPeers() API to get limit.");
    }

    public static final class Builder {
        private int mRttBurstSize = 8;
        private List<ResponderConfig> mRttPeers = new ArrayList();

        public Builder setRttBurstSize(int i) {
            if (i < 2 || i > 31) {
                throw new IllegalArgumentException("RTT burst size out of range.");
            }
            this.mRttBurstSize = i;
            return this;
        }

        public Builder addAccessPoint(ScanResult scanResult) {
            if (scanResult != null) {
                return addResponder(ResponderConfig.fromScanResult(scanResult));
            }
            throw new IllegalArgumentException("Null ScanResult!");
        }

        public Builder addAccessPoints(List<ScanResult> list) {
            if (list != null) {
                for (ScanResult addAccessPoint : list) {
                    addAccessPoint(addAccessPoint);
                }
                return this;
            }
            throw new IllegalArgumentException("Null list of ScanResults!");
        }

        public Builder addResponder(ResponderConfig responderConfig) {
            if (responderConfig != null) {
                this.mRttPeers.add(responderConfig);
                return this;
            }
            throw new IllegalArgumentException("Null Responder!");
        }

        public Builder addResponders(List<ResponderConfig> list) {
            if (list != null) {
                for (ResponderConfig addResponder : list) {
                    addResponder(addResponder);
                }
                return this;
            }
            throw new IllegalArgumentException("Null list of Responders");
        }

        public Builder addNon80211mcCapableAccessPoint(ScanResult scanResult) {
            if (scanResult == null) {
                throw new IllegalArgumentException("Null ScanResult!");
            } else if (!scanResult.is80211mcResponder()) {
                return addResponder(ResponderConfig.fromScanResult(scanResult));
            } else {
                throw new IllegalArgumentException("AP supports the 802.11mc protocol.");
            }
        }

        public Builder addNon80211mcCapableAccessPoints(List<ScanResult> list) {
            if (list != null) {
                for (ScanResult next : list) {
                    if (!next.is80211mcResponder()) {
                        addAccessPoint(next);
                    } else {
                        throw new IllegalArgumentException("At least one AP supports the 802.11mc protocol.");
                    }
                }
                return this;
            }
            throw new IllegalArgumentException("Null list of ScanResults!");
        }

        public Builder addWifiAwarePeer(MacAddress macAddress) {
            if (macAddress != null) {
                return addResponder(ResponderConfig.fromWifiAwarePeerMacAddressWithDefaults(macAddress));
            }
            throw new IllegalArgumentException("Null peer MAC address");
        }

        public Builder addWifiAwarePeer(PeerHandle peerHandle) {
            if (peerHandle != null) {
                return addResponder(ResponderConfig.fromWifiAwarePeerHandleWithDefaults(peerHandle));
            }
            throw new IllegalArgumentException("Null peer handler (identifier)");
        }

        public RangingRequest build() {
            return new RangingRequest(this.mRttPeers, this.mRttBurstSize);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RangingRequest)) {
            return false;
        }
        RangingRequest rangingRequest = (RangingRequest) obj;
        if (this.mRttPeers.size() == rangingRequest.mRttPeers.size() && this.mRttPeers.containsAll(rangingRequest.mRttPeers) && this.mRttBurstSize == rangingRequest.mRttBurstSize) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mRttPeers, Integer.valueOf(this.mRttBurstSize));
    }
}
