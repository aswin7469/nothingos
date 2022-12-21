package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class UnderlyingNetworkInfo implements Parcelable {
    public static final Parcelable.Creator<UnderlyingNetworkInfo> CREATOR = new Parcelable.Creator<UnderlyingNetworkInfo>() {
        public UnderlyingNetworkInfo createFromParcel(Parcel parcel) {
            return new UnderlyingNetworkInfo(parcel);
        }

        public UnderlyingNetworkInfo[] newArray(int i) {
            return new UnderlyingNetworkInfo[i];
        }
    };
    private final String mIface;
    private final int mOwnerUid;
    private final List<String> mUnderlyingIfaces;

    public int describeContents() {
        return 0;
    }

    public UnderlyingNetworkInfo(int i, String str, List<String> list) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(list);
        this.mOwnerUid = i;
        this.mIface = str;
        this.mUnderlyingIfaces = Collections.unmodifiableList(new ArrayList(list));
    }

    private UnderlyingNetworkInfo(Parcel parcel) {
        this.mOwnerUid = parcel.readInt();
        this.mIface = parcel.readString();
        ArrayList arrayList = new ArrayList();
        parcel.readList(arrayList, (ClassLoader) null, String.class);
        this.mUnderlyingIfaces = Collections.unmodifiableList(arrayList);
    }

    public int getOwnerUid() {
        return this.mOwnerUid;
    }

    public String getInterface() {
        return this.mIface;
    }

    public List<String> getUnderlyingInterfaces() {
        return this.mUnderlyingIfaces;
    }

    public String toString() {
        return "UnderlyingNetworkInfo{ownerUid=" + this.mOwnerUid + ", iface='" + this.mIface + "', underlyingIfaces='" + this.mUnderlyingIfaces.toString() + "'}";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mOwnerUid);
        parcel.writeString(this.mIface);
        parcel.writeList(this.mUnderlyingIfaces);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UnderlyingNetworkInfo)) {
            return false;
        }
        UnderlyingNetworkInfo underlyingNetworkInfo = (UnderlyingNetworkInfo) obj;
        if (this.mOwnerUid != underlyingNetworkInfo.getOwnerUid() || !Objects.equals(this.mIface, underlyingNetworkInfo.getInterface()) || !Objects.equals(this.mUnderlyingIfaces, underlyingNetworkInfo.getUnderlyingInterfaces())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mOwnerUid), this.mIface, this.mUnderlyingIfaces);
    }
}
