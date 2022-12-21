package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

@SystemApi
public final class TetheringInterface implements Parcelable {
    public static final Parcelable.Creator<TetheringInterface> CREATOR = new Parcelable.Creator<TetheringInterface>() {
        public TetheringInterface createFromParcel(Parcel parcel) {
            return new TetheringInterface(parcel);
        }

        public TetheringInterface[] newArray(int i) {
            return new TetheringInterface[i];
        }
    };
    private final String mInterface;
    private final int mType;

    public int describeContents() {
        return 0;
    }

    public TetheringInterface(int i, String str) {
        Objects.requireNonNull(str);
        this.mType = i;
        this.mInterface = str;
    }

    private TetheringInterface(Parcel parcel) {
        this(parcel.readInt(), parcel.readString());
    }

    public int getType() {
        return this.mType;
    }

    public String getInterface() {
        return this.mInterface;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
        parcel.writeString(this.mInterface);
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mType), this.mInterface);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TetheringInterface)) {
            return false;
        }
        TetheringInterface tetheringInterface = (TetheringInterface) obj;
        if (this.mType != tetheringInterface.mType || !this.mInterface.equals(tetheringInterface.mInterface)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "TetheringInterface {mType=" + this.mType + ", mInterface=" + this.mInterface + "}";
    }
}
