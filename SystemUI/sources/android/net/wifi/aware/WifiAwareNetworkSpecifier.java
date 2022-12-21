package android.net.wifi.aware;

import android.net.NetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.util.Arrays;
import java.util.Objects;

public final class WifiAwareNetworkSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Parcelable.Creator<WifiAwareNetworkSpecifier> CREATOR = new Parcelable.Creator<WifiAwareNetworkSpecifier>() {
        public WifiAwareNetworkSpecifier createFromParcel(Parcel parcel) {
            return new WifiAwareNetworkSpecifier(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readBoolean(), (WifiAwareDataPathSecurityConfig) parcel.readParcelable(WifiAwareDataPathSecurityConfig.class.getClassLoader()));
        }

        public WifiAwareNetworkSpecifier[] newArray(int i) {
            return new WifiAwareNetworkSpecifier[i];
        }
    };
    public static final int NETWORK_SPECIFIER_TYPE_IB = 0;
    public static final int NETWORK_SPECIFIER_TYPE_IB_ANY_PEER = 1;
    public static final int NETWORK_SPECIFIER_TYPE_MAX_VALID = 3;
    public static final int NETWORK_SPECIFIER_TYPE_OOB = 2;
    public static final int NETWORK_SPECIFIER_TYPE_OOB_ANY_PEER = 3;
    public final int clientId;
    private final int mChannelInMhz;
    private final boolean mForcedChannel;
    private final WifiAwareDataPathSecurityConfig mSecurityConfig;
    public final String passphrase;
    public final int peerId;
    public final byte[] peerMac;
    public final byte[] pmk;
    public final int port;
    public final int role;
    public final int sessionId;
    public final int transportProtocol;
    public final int type;

    public int describeContents() {
        return 0;
    }

    public int getChannelFrequencyMhz() {
        return this.mChannelInMhz;
    }

    public boolean isChannelRequired() {
        return this.mForcedChannel;
    }

    public WifiAwareDataPathSecurityConfig getWifiAwareDataPathSecurityConfig() {
        return this.mSecurityConfig;
    }

    public WifiAwareNetworkSpecifier(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7, int i8, boolean z, WifiAwareDataPathSecurityConfig wifiAwareDataPathSecurityConfig) {
        String str;
        this.type = i;
        this.role = i2;
        this.clientId = i3;
        this.sessionId = i4;
        this.peerId = i5;
        this.peerMac = bArr;
        this.port = i6;
        this.transportProtocol = i7;
        this.mChannelInMhz = i8;
        this.mForcedChannel = z;
        this.mSecurityConfig = wifiAwareDataPathSecurityConfig;
        byte[] bArr2 = null;
        if (wifiAwareDataPathSecurityConfig == null) {
            str = null;
        } else {
            str = wifiAwareDataPathSecurityConfig.getPskPassphrase();
        }
        this.passphrase = str;
        this.pmk = wifiAwareDataPathSecurityConfig != null ? wifiAwareDataPathSecurityConfig.getPmk() : bArr2;
    }

    public WifiAwareNetworkSpecifier(int i, int i2, int i3, int i4, int i5, byte[] bArr, byte[] bArr2, String str, int i6, int i7) {
        this.type = i;
        this.role = i2;
        this.clientId = i3;
        this.sessionId = i4;
        this.peerId = i5;
        this.peerMac = bArr;
        this.port = i6;
        this.transportProtocol = i7;
        this.mChannelInMhz = 0;
        this.mForcedChannel = false;
        this.pmk = bArr2;
        this.passphrase = str;
        if (bArr2 == null && str == null) {
            this.mSecurityConfig = null;
        } else {
            this.mSecurityConfig = new WifiAwareDataPathSecurityConfig(1, bArr2, (byte[]) null, str);
        }
    }

    public boolean isOutOfBand() {
        int i = this.type;
        return i == 2 || i == 3;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.type);
        parcel.writeInt(this.role);
        parcel.writeInt(this.clientId);
        parcel.writeInt(this.sessionId);
        parcel.writeInt(this.peerId);
        parcel.writeByteArray(this.peerMac);
        parcel.writeInt(this.port);
        parcel.writeInt(this.transportProtocol);
        parcel.writeInt(this.mChannelInMhz);
        parcel.writeBoolean(this.mForcedChannel);
        parcel.writeParcelable(this.mSecurityConfig, i);
    }

    public boolean canBeSatisfiedBy(NetworkSpecifier networkSpecifier) {
        if (networkSpecifier instanceof WifiAwareAgentNetworkSpecifier) {
            return ((WifiAwareAgentNetworkSpecifier) networkSpecifier).satisfiesAwareNetworkSpecifier(this);
        }
        return equals(networkSpecifier);
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.type), Integer.valueOf(this.role), Integer.valueOf(this.clientId), Integer.valueOf(this.sessionId), Integer.valueOf(this.peerId), Integer.valueOf(Arrays.hashCode(this.peerMac)), Integer.valueOf(this.port), Integer.valueOf(this.transportProtocol), Integer.valueOf(this.mChannelInMhz), Boolean.valueOf(this.mForcedChannel), this.mSecurityConfig);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiAwareNetworkSpecifier)) {
            return false;
        }
        WifiAwareNetworkSpecifier wifiAwareNetworkSpecifier = (WifiAwareNetworkSpecifier) obj;
        if (this.type == wifiAwareNetworkSpecifier.type && this.role == wifiAwareNetworkSpecifier.role && this.clientId == wifiAwareNetworkSpecifier.clientId && this.sessionId == wifiAwareNetworkSpecifier.sessionId && this.peerId == wifiAwareNetworkSpecifier.peerId && Arrays.equals(this.peerMac, wifiAwareNetworkSpecifier.peerMac) && this.port == wifiAwareNetworkSpecifier.port && this.transportProtocol == wifiAwareNetworkSpecifier.transportProtocol && this.mChannelInMhz == wifiAwareNetworkSpecifier.mChannelInMhz && this.mForcedChannel == wifiAwareNetworkSpecifier.mForcedChannel && Objects.equals(this.mSecurityConfig, wifiAwareNetworkSpecifier.mSecurityConfig)) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("WifiAwareNetworkSpecifier [type=");
        sb.append(this.type);
        sb.append(", role=");
        sb.append(this.role);
        sb.append(", clientId=");
        sb.append(this.clientId);
        sb.append(", sessionId=");
        sb.append(this.sessionId);
        sb.append(", peerId=");
        sb.append(this.peerId);
        sb.append(", peerMac=");
        sb.append(this.peerMac == null ? "<null>" : "<non-null>");
        sb.append(", securityConfig=");
        sb.append((Object) this.mSecurityConfig);
        sb.append(", port=");
        sb.append(this.port);
        sb.append(", transportProtocol=");
        sb.append(this.transportProtocol);
        sb.append(", channel=");
        sb.append(this.mChannelInMhz);
        sb.append(", forceChannel=");
        sb.append(this.mForcedChannel);
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    public static final class Builder {
        private int mChannel = 0;
        private DiscoverySession mDiscoverySession;
        private boolean mIsRequired = false;
        private PeerHandle mPeerHandle;
        private byte[] mPmk;
        private int mPort = 0;
        private String mPskPassphrase;
        private WifiAwareDataPathSecurityConfig mSecurityConfig;
        private int mTransportProtocol = -1;

        public Builder(DiscoverySession discoverySession, PeerHandle peerHandle) {
            if (discoverySession == null) {
                throw new IllegalArgumentException("Non-null discoverySession required");
            } else if (peerHandle != null) {
                this.mDiscoverySession = discoverySession;
                this.mPeerHandle = peerHandle;
            } else {
                throw new IllegalArgumentException("Non-null peerHandle required");
            }
        }

        public Builder(PublishDiscoverySession publishDiscoverySession) {
            if (!SdkLevel.isAtLeastS()) {
                throw new UnsupportedOperationException();
            } else if (publishDiscoverySession != null) {
                this.mDiscoverySession = publishDiscoverySession;
                this.mPeerHandle = null;
            } else {
                throw new IllegalArgumentException("Non-null publishDiscoverySession required");
            }
        }

        public Builder setPskPassphrase(String str) {
            if (WifiAwareUtils.validatePassphrase(str)) {
                this.mPskPassphrase = str;
                return this;
            }
            throw new IllegalArgumentException("Passphrase must meet length requirements");
        }

        public Builder setPmk(byte[] bArr) {
            if (WifiAwareUtils.validatePmk(bArr)) {
                this.mPmk = bArr;
                return this;
            }
            throw new IllegalArgumentException("PMK must 32 bytes");
        }

        public Builder setPort(int i) {
            if (i <= 0 || i > 65535) {
                throw new IllegalArgumentException("The port must be a positive value (0, 65535]");
            }
            this.mPort = i;
            return this;
        }

        public Builder setTransportProtocol(int i) {
            if (i < 0 || i > 255) {
                throw new IllegalArgumentException("The transport protocol must be in range [0, 255]");
            }
            this.mTransportProtocol = i;
            return this;
        }

        public Builder setChannelFrequencyMhz(int i, boolean z) {
            this.mChannel = i;
            this.mIsRequired = z;
            return this;
        }

        public Builder setDataPathSecurityConfig(WifiAwareDataPathSecurityConfig wifiAwareDataPathSecurityConfig) {
            if (wifiAwareDataPathSecurityConfig == null) {
                throw new IllegalArgumentException("The WifiAwareDataPathSecurityConfig should be non-null");
            } else if (wifiAwareDataPathSecurityConfig.isValid()) {
                this.mSecurityConfig = wifiAwareDataPathSecurityConfig;
                return this;
            } else {
                throw new IllegalArgumentException("The WifiAwareDataPathSecurityConfig is invalid");
            }
        }

        public WifiAwareNetworkSpecifier build() {
            if (this.mDiscoverySession != null) {
                String str = this.mPskPassphrase;
                if (str == null || this.mPmk == null) {
                    WifiAwareDataPathSecurityConfig wifiAwareDataPathSecurityConfig = this.mSecurityConfig;
                    if (!(str == null && this.mPmk == null)) {
                        if (wifiAwareDataPathSecurityConfig == null) {
                            wifiAwareDataPathSecurityConfig = new WifiAwareDataPathSecurityConfig(1, this.mPmk, (byte[]) null, this.mPskPassphrase);
                        } else {
                            throw new IllegalStateException("Can only specify a SecurityConfig or a PMK(Passphrase) - not both!");
                        }
                    }
                    WifiAwareDataPathSecurityConfig wifiAwareDataPathSecurityConfig2 = wifiAwareDataPathSecurityConfig;
                    boolean z = !(this.mDiscoverySession instanceof SubscribeDiscoverySession);
                    if (!(this.mPort == 0 && this.mTransportProtocol == -1)) {
                        if (!z) {
                            throw new IllegalStateException("Port and transport protocol information can only be specified on the Publisher device (which is the server");
                        } else if (wifiAwareDataPathSecurityConfig2 == null) {
                            throw new IllegalStateException("Port and transport protocol information can only be specified on a secure link");
                        }
                    }
                    int i = 0;
                    int i2 = this.mPeerHandle == null ? 1 : 0;
                    int i3 = this.mDiscoverySession.mClientId;
                    int i4 = this.mDiscoverySession.mSessionId;
                    PeerHandle peerHandle = this.mPeerHandle;
                    if (peerHandle != null) {
                        i = peerHandle.peerId;
                    }
                    return new WifiAwareNetworkSpecifier(i2, z ? 1 : 0, i3, i4, i, (byte[]) null, this.mPort, this.mTransportProtocol, this.mChannel, this.mIsRequired, wifiAwareDataPathSecurityConfig2);
                }
                throw new IllegalStateException("Can only specify a Passphrase or a PMK - not both!");
            }
            throw new IllegalStateException("Null discovery session!?");
        }
    }
}
