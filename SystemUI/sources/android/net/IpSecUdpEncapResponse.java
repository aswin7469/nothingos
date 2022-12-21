package android.net;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

public final class IpSecUdpEncapResponse implements Parcelable {
    public static final Parcelable.Creator<IpSecUdpEncapResponse> CREATOR = new Parcelable.Creator<IpSecUdpEncapResponse>() {
        public IpSecUdpEncapResponse createFromParcel(Parcel parcel) {
            return new IpSecUdpEncapResponse(parcel);
        }

        public IpSecUdpEncapResponse[] newArray(int i) {
            return new IpSecUdpEncapResponse[i];
        }
    };
    private static final String TAG = "IpSecUdpEncapResponse";
    public final ParcelFileDescriptor fileDescriptor;
    public final int port;
    public final int resourceId;
    public final int status;

    public int describeContents() {
        return this.fileDescriptor != null ? 1 : 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.status);
        parcel.writeInt(this.resourceId);
        parcel.writeInt(this.port);
        parcel.writeParcelable(this.fileDescriptor, 1);
    }

    public IpSecUdpEncapResponse(int i) {
        if (i != 0) {
            this.status = i;
            this.resourceId = -1;
            this.port = -1;
            this.fileDescriptor = null;
            return;
        }
        throw new IllegalArgumentException("Valid status implies other args must be provided");
    }

    public IpSecUdpEncapResponse(int i, int i2, int i3, FileDescriptor fileDescriptor2) throws IOException {
        if (i == 0 && fileDescriptor2 == null) {
            throw new IllegalArgumentException("Valid status implies FD must be non-null");
        }
        this.status = i;
        this.resourceId = i2;
        this.port = i3;
        this.fileDescriptor = i == 0 ? ParcelFileDescriptor.dup(fileDescriptor2) : null;
    }

    private IpSecUdpEncapResponse(Parcel parcel) {
        this.status = parcel.readInt();
        this.resourceId = parcel.readInt();
        this.port = parcel.readInt();
        this.fileDescriptor = (ParcelFileDescriptor) parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader(), ParcelFileDescriptor.class);
    }
}
