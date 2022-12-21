package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class TestNetworkInterface implements Parcelable {
    public static final Parcelable.Creator<TestNetworkInterface> CREATOR = new Parcelable.Creator<TestNetworkInterface>() {
        public TestNetworkInterface createFromParcel(Parcel parcel) {
            return new TestNetworkInterface(parcel);
        }

        public TestNetworkInterface[] newArray(int i) {
            return new TestNetworkInterface[i];
        }
    };
    private final ParcelFileDescriptor mFileDescriptor;
    private final String mInterfaceName;

    public int describeContents() {
        return this.mFileDescriptor != null ? 1 : 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mFileDescriptor, 1);
        parcel.writeString(this.mInterfaceName);
    }

    public TestNetworkInterface(ParcelFileDescriptor parcelFileDescriptor, String str) {
        this.mFileDescriptor = parcelFileDescriptor;
        this.mInterfaceName = str;
    }

    private TestNetworkInterface(Parcel parcel) {
        this.mFileDescriptor = (ParcelFileDescriptor) parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        this.mInterfaceName = parcel.readString();
    }

    public ParcelFileDescriptor getFileDescriptor() {
        return this.mFileDescriptor;
    }

    public String getInterfaceName() {
        return this.mInterfaceName;
    }
}
