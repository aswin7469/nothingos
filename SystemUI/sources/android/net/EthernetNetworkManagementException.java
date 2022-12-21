package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

@SystemApi
public final class EthernetNetworkManagementException extends RuntimeException implements Parcelable {
    public static final Parcelable.Creator<EthernetNetworkManagementException> CREATOR = new Parcelable.Creator<EthernetNetworkManagementException>() {
        public EthernetNetworkManagementException[] newArray(int i) {
            return new EthernetNetworkManagementException[i];
        }

        public EthernetNetworkManagementException createFromParcel(Parcel parcel) {
            return new EthernetNetworkManagementException(parcel.readString());
        }
    };

    public int describeContents() {
        return 0;
    }

    public EthernetNetworkManagementException(String str) {
        super(str);
    }

    public int hashCode() {
        return Objects.hash(getMessage());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(getMessage(), ((EthernetNetworkManagementException) obj).getMessage());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getMessage());
    }
}
