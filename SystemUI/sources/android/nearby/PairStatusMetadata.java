package android.nearby;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Objects;

public final class PairStatusMetadata implements Parcelable {
    public static final Parcelable.Creator<PairStatusMetadata> CREATOR = new Parcelable.Creator<PairStatusMetadata>() {
        public PairStatusMetadata createFromParcel(Parcel parcel) {
            return new PairStatusMetadata(parcel.readInt());
        }

        public PairStatusMetadata[] newArray(int i) {
            return new PairStatusMetadata[i];
        }
    };
    private final int mStatus;

    public @interface Status {
        public static final int DISMISS = 1003;
        public static final int FAIL = 1002;
        public static final int SUCCESS = 1001;
        public static final int UNKNOWN = 1000;
    }

    public static String statusToString(int i) {
        switch (i) {
            case 1001:
                return "SUCCESS";
            case 1002:
                return "FAIL";
            case 1003:
                return "DISMISS";
            default:
                return "UNKNOWN";
        }
    }

    public int describeContents() {
        return 0;
    }

    public int getStability() {
        return 0;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public String toString() {
        return "PairStatusMetadata[ status=" + statusToString(this.mStatus) + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PairStatusMetadata) || this.mStatus != ((PairStatusMetadata) obj).mStatus) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mStatus));
    }

    public PairStatusMetadata(int i) {
        this.mStatus = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mStatus);
    }
}
