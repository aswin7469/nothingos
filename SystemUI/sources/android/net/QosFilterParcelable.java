package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.Objects;

public final class QosFilterParcelable implements Parcelable {
    public static final Parcelable.Creator<QosFilterParcelable> CREATOR = new Parcelable.Creator<QosFilterParcelable>() {
        public QosFilterParcelable createFromParcel(Parcel parcel) {
            return new QosFilterParcelable(parcel);
        }

        public QosFilterParcelable[] newArray(int i) {
            return new QosFilterParcelable[i];
        }
    };
    private static final String LOG_TAG = "QosFilterParcelable";
    private static final int NO_FILTER_PRESENT = 0;
    private static final int QOS_SOCKET_FILTER = 1;
    private final QosFilter mQosFilter;

    public int describeContents() {
        return 0;
    }

    public QosFilter getQosFilter() {
        return this.mQosFilter;
    }

    public QosFilterParcelable(QosFilter qosFilter) {
        Objects.requireNonNull(qosFilter, "qosFilter must be non-null");
        this.mQosFilter = qosFilter;
    }

    private QosFilterParcelable(Parcel parcel) {
        if (parcel.readInt() != 1) {
            this.mQosFilter = null;
        } else {
            this.mQosFilter = new QosSocketFilter(QosSocketInfo.CREATOR.createFromParcel(parcel));
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.mQosFilter instanceof QosSocketFilter) {
            parcel.writeInt(1);
            ((QosSocketFilter) this.mQosFilter).getQosSocketInfo().writeToParcel(parcel, 0);
            return;
        }
        parcel.writeInt(0);
        String str = LOG_TAG;
        Log.e(str, "Parceling failed, unknown type of filter present: " + this.mQosFilter);
    }
}
