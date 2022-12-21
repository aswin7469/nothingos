package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

@SystemApi
public final class EthernetNetworkUpdateRequest implements Parcelable {
    public static final Parcelable.Creator<EthernetNetworkUpdateRequest> CREATOR = new Parcelable.Creator<EthernetNetworkUpdateRequest>() {
        public EthernetNetworkUpdateRequest[] newArray(int i) {
            return new EthernetNetworkUpdateRequest[i];
        }

        public EthernetNetworkUpdateRequest createFromParcel(Parcel parcel) {
            return new EthernetNetworkUpdateRequest(parcel);
        }
    };
    /* access modifiers changed from: private */
    public final IpConfiguration mIpConfig;
    /* access modifiers changed from: private */
    public final NetworkCapabilities mNetworkCapabilities;

    public int describeContents() {
        return 0;
    }

    public IpConfiguration getIpConfiguration() {
        if (this.mIpConfig == null) {
            return null;
        }
        return new IpConfiguration(this.mIpConfig);
    }

    public NetworkCapabilities getNetworkCapabilities() {
        if (this.mNetworkCapabilities == null) {
            return null;
        }
        return new NetworkCapabilities(this.mNetworkCapabilities);
    }

    private EthernetNetworkUpdateRequest(IpConfiguration ipConfiguration, NetworkCapabilities networkCapabilities) {
        this.mIpConfig = ipConfiguration;
        this.mNetworkCapabilities = networkCapabilities;
    }

    private EthernetNetworkUpdateRequest(Parcel parcel) {
        Objects.requireNonNull(parcel);
        this.mIpConfig = (IpConfiguration) parcel.readParcelable(IpConfiguration.class.getClassLoader(), IpConfiguration.class);
        this.mNetworkCapabilities = (NetworkCapabilities) parcel.readParcelable(NetworkCapabilities.class.getClassLoader(), NetworkCapabilities.class);
    }

    public static final class Builder {
        private IpConfiguration mBuilderIpConfig;
        private NetworkCapabilities mBuilderNetworkCapabilities;

        public Builder() {
        }

        public Builder(EthernetNetworkUpdateRequest ethernetNetworkUpdateRequest) {
            IpConfiguration ipConfiguration;
            Objects.requireNonNull(ethernetNetworkUpdateRequest);
            NetworkCapabilities networkCapabilities = null;
            if (ethernetNetworkUpdateRequest.mIpConfig == null) {
                ipConfiguration = null;
            } else {
                ipConfiguration = new IpConfiguration(ethernetNetworkUpdateRequest.mIpConfig);
            }
            this.mBuilderIpConfig = ipConfiguration;
            this.mBuilderNetworkCapabilities = ethernetNetworkUpdateRequest.mNetworkCapabilities != null ? new NetworkCapabilities(ethernetNetworkUpdateRequest.mNetworkCapabilities) : networkCapabilities;
        }

        public Builder setIpConfiguration(IpConfiguration ipConfiguration) {
            this.mBuilderIpConfig = ipConfiguration == null ? null : new IpConfiguration(ipConfiguration);
            return this;
        }

        public Builder setNetworkCapabilities(NetworkCapabilities networkCapabilities) {
            this.mBuilderNetworkCapabilities = networkCapabilities == null ? null : new NetworkCapabilities(networkCapabilities);
            return this;
        }

        public EthernetNetworkUpdateRequest build() {
            if (this.mBuilderIpConfig != null || this.mBuilderNetworkCapabilities != null) {
                return new EthernetNetworkUpdateRequest(this.mBuilderIpConfig, this.mBuilderNetworkCapabilities);
            }
            throw new IllegalStateException("Cannot construct an empty EthernetNetworkUpdateRequest");
        }
    }

    public String toString() {
        return "EthernetNetworkUpdateRequest{mIpConfig=" + this.mIpConfig + ", mNetworkCapabilities=" + this.mNetworkCapabilities + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        EthernetNetworkUpdateRequest ethernetNetworkUpdateRequest = (EthernetNetworkUpdateRequest) obj;
        if (!Objects.equals(ethernetNetworkUpdateRequest.getIpConfiguration(), this.mIpConfig) || !Objects.equals(ethernetNetworkUpdateRequest.getNetworkCapabilities(), this.mNetworkCapabilities)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mIpConfig, this.mNetworkCapabilities);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mIpConfig, i);
        parcel.writeParcelable(this.mNetworkCapabilities, i);
    }
}
