package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Objects;

public final class EthernetNetworkSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Parcelable.Creator<EthernetNetworkSpecifier> CREATOR = new Parcelable.Creator<EthernetNetworkSpecifier>() {
        public EthernetNetworkSpecifier createFromParcel(Parcel parcel) {
            return new EthernetNetworkSpecifier(parcel.readString());
        }

        public EthernetNetworkSpecifier[] newArray(int i) {
            return new EthernetNetworkSpecifier[i];
        }
    };
    private final String mInterfaceName;

    public int describeContents() {
        return 0;
    }

    public EthernetNetworkSpecifier(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mInterfaceName = str;
            return;
        }
        throw new IllegalArgumentException();
    }

    public String getInterfaceName() {
        return this.mInterfaceName;
    }

    public boolean canBeSatisfiedBy(NetworkSpecifier networkSpecifier) {
        return equals(networkSpecifier);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof EthernetNetworkSpecifier)) {
            return false;
        }
        return TextUtils.equals(this.mInterfaceName, ((EthernetNetworkSpecifier) obj).mInterfaceName);
    }

    public int hashCode() {
        return Objects.hashCode(this.mInterfaceName);
    }

    public String toString() {
        return "EthernetNetworkSpecifier (" + this.mInterfaceName + NavigationBarInflaterView.KEY_CODE_END;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mInterfaceName);
    }
}
