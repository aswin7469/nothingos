package android.nearby;

import android.annotation.SystemApi;
import android.os.Parcel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

@SystemApi
public abstract class BroadcastRequest {
    public static final int BROADCAST_TYPE_NEARBY_PRESENCE = 3;
    public static final int BROADCAST_TYPE_UNKNOWN = -1;
    public static final int MEDIUM_BLE = 1;
    public static final int PRESENCE_VERSION_UNKNOWN = -1;
    public static final int PRESENCE_VERSION_V0 = 0;
    public static final int PRESENCE_VERSION_V1 = 1;
    public static final int UNKNOWN_TX_POWER = -127;
    private final List<Integer> mMediums;
    private final int mTxPower;
    private final int mType;
    private final int mVersion;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BroadcastType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BroadcastVersion {
    }

    public @interface Medium {
    }

    public static BroadcastRequest createFromParcel(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt == 3) {
            return PresenceBroadcastRequest.createFromParcelBody(parcel);
        }
        throw new IllegalStateException("Unexpected broadcast type (value " + readInt + ") in parcel.");
    }

    BroadcastRequest(int i, int i2, int i3, List<Integer> list) {
        this.mType = i;
        this.mVersion = i2;
        this.mTxPower = i3;
        this.mMediums = list;
    }

    BroadcastRequest(int i, Parcel parcel) {
        this.mType = i;
        this.mVersion = parcel.readInt();
        this.mTxPower = parcel.readInt();
        ArrayList arrayList = new ArrayList();
        this.mMediums = arrayList;
        parcel.readList(arrayList, Integer.class.getClassLoader(), Integer.class);
    }

    public int getType() {
        return this.mType;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public int getTxPower() {
        return this.mTxPower;
    }

    public List<Integer> getMediums() {
        return this.mMediums;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
        parcel.writeInt(this.mVersion);
        parcel.writeInt(this.mTxPower);
        parcel.writeList(this.mMediums);
    }
}
