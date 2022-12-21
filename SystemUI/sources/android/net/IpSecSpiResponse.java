package android.net;

import android.os.Parcel;
import android.os.Parcelable;

public final class IpSecSpiResponse implements Parcelable {
    public static final Parcelable.Creator<IpSecSpiResponse> CREATOR = new Parcelable.Creator<IpSecSpiResponse>() {
        public IpSecSpiResponse createFromParcel(Parcel parcel) {
            return new IpSecSpiResponse(parcel);
        }

        public IpSecSpiResponse[] newArray(int i) {
            return new IpSecSpiResponse[i];
        }
    };
    private static final String TAG = "IpSecSpiResponse";
    public final int resourceId;
    public final int spi;
    public final int status;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.status);
        parcel.writeInt(this.resourceId);
        parcel.writeInt(this.spi);
    }

    public IpSecSpiResponse(int i, int i2, int i3) {
        this.status = i;
        this.resourceId = i2;
        this.spi = i3;
    }

    public IpSecSpiResponse(int i) {
        if (i != 0) {
            this.status = i;
            this.resourceId = -1;
            this.spi = 0;
            return;
        }
        throw new IllegalArgumentException("Valid status implies other args must be provided");
    }

    private IpSecSpiResponse(Parcel parcel) {
        this.status = parcel.readInt();
        this.resourceId = parcel.readInt();
        this.spi = parcel.readInt();
    }
}
