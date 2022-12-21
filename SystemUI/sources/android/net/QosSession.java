package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;

@SystemApi
public final class QosSession implements Parcelable {
    public static final Parcelable.Creator<QosSession> CREATOR = new Parcelable.Creator<QosSession>() {
        public QosSession createFromParcel(Parcel parcel) {
            return new QosSession(parcel);
        }

        public QosSession[] newArray(int i) {
            return new QosSession[i];
        }
    };
    public static final int TYPE_EPS_BEARER = 1;
    public static final int TYPE_NR_BEARER = 2;
    private final int mSessionId;
    private final int mSessionType;

    @interface QosSessionType {
    }

    public int describeContents() {
        return 0;
    }

    public long getUniqueId() {
        return (((long) this.mSessionType) << 32) | ((long) this.mSessionId);
    }

    public int getSessionId() {
        return this.mSessionId;
    }

    public int getSessionType() {
        return this.mSessionType;
    }

    public QosSession(int i, int i2) {
        this.mSessionId = i;
        this.mSessionType = i2;
    }

    public String toString() {
        return "QosSession{mSessionId=" + this.mSessionId + ", mSessionType=" + this.mSessionType + '}';
    }

    private QosSession(Parcel parcel) {
        this.mSessionId = parcel.readInt();
        this.mSessionType = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mSessionId);
        parcel.writeInt(this.mSessionType);
    }
}
