package android.net;

import android.os.Parcel;
import android.os.Parcelable;

public final class IpSecTransformResponse implements Parcelable {
    public static final Parcelable.Creator<IpSecTransformResponse> CREATOR = new Parcelable.Creator<IpSecTransformResponse>() {
        public IpSecTransformResponse createFromParcel(Parcel parcel) {
            return new IpSecTransformResponse(parcel);
        }

        public IpSecTransformResponse[] newArray(int i) {
            return new IpSecTransformResponse[i];
        }
    };
    private static final String TAG = "IpSecTransformResponse";
    public final int resourceId;
    public final int status;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.status);
        parcel.writeInt(this.resourceId);
    }

    public IpSecTransformResponse(int i) {
        if (i != 0) {
            this.status = i;
            this.resourceId = -1;
            return;
        }
        throw new IllegalArgumentException("Valid status implies other args must be provided");
    }

    public IpSecTransformResponse(int i, int i2) {
        this.status = i;
        this.resourceId = i2;
    }

    private IpSecTransformResponse(Parcel parcel) {
        this.status = parcel.readInt();
        this.resourceId = parcel.readInt();
    }
}
