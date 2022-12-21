package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.util.Objects;

@SystemApi
public final class CoexUnsafeChannel implements Parcelable {
    public static final Parcelable.Creator<CoexUnsafeChannel> CREATOR = new Parcelable.Creator<CoexUnsafeChannel>() {
        public CoexUnsafeChannel createFromParcel(Parcel parcel) {
            return new CoexUnsafeChannel(parcel.readInt(), parcel.readInt(), parcel.readInt());
        }

        public CoexUnsafeChannel[] newArray(int i) {
            return new CoexUnsafeChannel[i];
        }
    };
    public static final int POWER_CAP_NONE = Integer.MAX_VALUE;
    private int mBand;
    private int mChannel;
    private int mPowerCapDbm;

    public int describeContents() {
        return 0;
    }

    public CoexUnsafeChannel(int i, int i2) {
        if (SdkLevel.isAtLeastS()) {
            this.mBand = i;
            this.mChannel = i2;
            this.mPowerCapDbm = Integer.MAX_VALUE;
            return;
        }
        throw new UnsupportedOperationException();
    }

    public CoexUnsafeChannel(int i, int i2, int i3) {
        if (SdkLevel.isAtLeastS()) {
            this.mBand = i;
            this.mChannel = i2;
            this.mPowerCapDbm = i3;
            return;
        }
        throw new UnsupportedOperationException();
    }

    public int getBand() {
        return this.mBand;
    }

    public int getChannel() {
        return this.mChannel;
    }

    public int getPowerCapDbm() {
        return this.mPowerCapDbm;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CoexUnsafeChannel coexUnsafeChannel = (CoexUnsafeChannel) obj;
        if (this.mBand == coexUnsafeChannel.mBand && this.mChannel == coexUnsafeChannel.mChannel && this.mPowerCapDbm == coexUnsafeChannel.mPowerCapDbm) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mBand), Integer.valueOf(this.mChannel), Integer.valueOf(this.mPowerCapDbm));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CoexUnsafeChannel{");
        int i = this.mBand;
        if (i == 1) {
            sb.append("2.4GHz");
        } else if (i == 2) {
            sb.append("5GHz");
        } else if (i == 8) {
            sb.append("6GHz");
        } else {
            sb.append("UNKNOWN BAND");
        }
        sb.append(", ");
        sb.append(this.mChannel);
        if (this.mPowerCapDbm != Integer.MAX_VALUE) {
            sb.append(", ");
            sb.append(this.mPowerCapDbm);
            sb.append("dBm");
        }
        sb.append('}');
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mBand);
        parcel.writeInt(this.mChannel);
        parcel.writeInt(this.mPowerCapDbm);
    }
}
