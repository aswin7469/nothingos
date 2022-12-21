package android.nearby;

import android.annotation.SystemApi;
import android.os.Parcel;

@SystemApi
public abstract class ScanFilter {
    private final int mMaxPathLoss;
    private final int mType;

    public static ScanFilter createFromParcel(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt == 2) {
            return PresenceScanFilter.createFromParcelBody(parcel);
        }
        throw new IllegalStateException("Unexpected scan type (value " + readInt + ") in parcel.");
    }

    ScanFilter(int i, int i2) {
        this.mType = i;
        this.mMaxPathLoss = i2;
    }

    ScanFilter(int i, Parcel parcel) {
        this.mType = i;
        this.mMaxPathLoss = parcel.readInt();
    }

    public int getType() {
        return this.mType;
    }

    public int getMaxPathLoss() {
        return this.mMaxPathLoss;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
        parcel.writeInt(this.mMaxPathLoss);
    }
}
