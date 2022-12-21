package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class TestNetworkSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Parcelable.Creator<TestNetworkSpecifier> CREATOR = new Parcelable.Creator<TestNetworkSpecifier>() {
        public TestNetworkSpecifier createFromParcel(Parcel parcel) {
            return new TestNetworkSpecifier(parcel.readString());
        }

        public TestNetworkSpecifier[] newArray(int i) {
            return new TestNetworkSpecifier[i];
        }
    };
    private final String mInterfaceName;

    public int describeContents() {
        return 0;
    }

    public TestNetworkSpecifier(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mInterfaceName = str;
            return;
        }
        throw new IllegalArgumentException("Empty interfaceName");
    }

    public String getInterfaceName() {
        return this.mInterfaceName;
    }

    public boolean canBeSatisfiedBy(NetworkSpecifier networkSpecifier) {
        return equals(networkSpecifier);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TestNetworkSpecifier)) {
            return false;
        }
        return TextUtils.equals(this.mInterfaceName, ((TestNetworkSpecifier) obj).mInterfaceName);
    }

    public int hashCode() {
        return Objects.hashCode(this.mInterfaceName);
    }

    public String toString() {
        return "TestNetworkSpecifier (" + this.mInterfaceName + NavigationBarInflaterView.KEY_CODE_END;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mInterfaceName);
    }
}
