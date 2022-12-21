package android.nearby;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.internal.util.Preconditions;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.WorkSource;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class ScanRequest implements Parcelable {
    public static final Parcelable.Creator<ScanRequest> CREATOR = new Parcelable.Creator<ScanRequest>() {
        public ScanRequest createFromParcel(Parcel parcel) {
            Builder workSource = new Builder().setScanType(parcel.readInt()).setScanMode(parcel.readInt()).setBleEnabled(parcel.readBoolean()).setWorkSource((WorkSource) parcel.readTypedObject(WorkSource.CREATOR));
            int readInt = parcel.readInt();
            for (int i = 0; i < readInt; i++) {
                workSource.addScanFilter(ScanFilter.createFromParcel(parcel));
            }
            return workSource.build();
        }

        public ScanRequest[] newArray(int i) {
            return new ScanRequest[i];
        }
    };
    public static final int SCAN_MODE_BALANCED = 1;
    public static final int SCAN_MODE_LOW_LATENCY = 2;
    public static final int SCAN_MODE_LOW_POWER = 0;
    public static final int SCAN_MODE_NO_POWER = -1;
    public static final int SCAN_TYPE_FAST_PAIR = 1;
    public static final int SCAN_TYPE_NEARBY_PRESENCE = 2;
    private final boolean mBleEnabled;
    private final List<ScanFilter> mScanFilters;
    private final int mScanMode;
    private final int mScanType;
    private final WorkSource mWorkSource;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScanMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScanType {
    }

    public static boolean isValidScanMode(int i) {
        return i == 2 || i == 1 || i == 0 || i == -1;
    }

    public static boolean isValidScanType(int i) {
        return i == 1 || i == 2;
    }

    public static String scanModeToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? i != 2 ? "SCAN_MODE_INVALID" : "SCAN_MODE_LOW_LATENCY" : "SCAN_MODE_BALANCED" : "SCAN_MODE_LOW_POWER" : "SCAN_MODE_NO_POWER";
    }

    public int describeContents() {
        return 0;
    }

    private ScanRequest(int i, int i2, boolean z, WorkSource workSource, List<ScanFilter> list) {
        this.mScanType = i;
        this.mScanMode = i2;
        this.mBleEnabled = z;
        this.mWorkSource = workSource;
        this.mScanFilters = list;
    }

    public int getScanType() {
        return this.mScanType;
    }

    public int getScanMode() {
        return this.mScanMode;
    }

    public boolean isBleEnabled() {
        return this.mBleEnabled;
    }

    public List<ScanFilter> getScanFilters() {
        return this.mScanFilters;
    }

    @SystemApi
    public WorkSource getWorkSource() {
        return this.mWorkSource;
    }

    public String toString() {
        return "Request[scanType=" + this.mScanType + ", scanMode=" + scanModeToString(this.mScanMode) + ", enableBle=" + this.mBleEnabled + ", workSource=" + this.mWorkSource + ", scanFilters=" + this.mScanFilters + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mScanType);
        parcel.writeInt(this.mScanMode);
        parcel.writeBoolean(this.mBleEnabled);
        parcel.writeTypedObject(this.mWorkSource, 0);
        int size = this.mScanFilters.size();
        parcel.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            this.mScanFilters.get(i2).writeToParcel(parcel, i);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ScanRequest)) {
            return false;
        }
        ScanRequest scanRequest = (ScanRequest) obj;
        if (this.mScanType == scanRequest.mScanType && this.mScanMode == scanRequest.mScanMode && this.mBleEnabled == scanRequest.mBleEnabled && Objects.equals(this.mWorkSource, scanRequest.mWorkSource)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mScanType), Integer.valueOf(this.mScanMode), Boolean.valueOf(this.mBleEnabled), this.mWorkSource);
    }

    public static final class Builder {
        private static final int INVALID_SCAN_TYPE = -1;
        private boolean mBleEnabled = true;
        private List<ScanFilter> mScanFilters = new ArrayList();
        private int mScanMode;
        private int mScanType = -1;
        private WorkSource mWorkSource = new WorkSource();

        public Builder setScanType(int i) {
            this.mScanType = i;
            return this;
        }

        public Builder setScanMode(int i) {
            this.mScanMode = i;
            return this;
        }

        public Builder setBleEnabled(boolean z) {
            this.mBleEnabled = z;
            return this;
        }

        @SystemApi
        public Builder setWorkSource(WorkSource workSource) {
            if (workSource == null) {
                this.mWorkSource = new WorkSource();
            } else {
                this.mWorkSource = workSource;
            }
            return this;
        }

        public Builder addScanFilter(ScanFilter scanFilter) {
            Objects.requireNonNull(scanFilter);
            this.mScanFilters.add(scanFilter);
            return this;
        }

        public ScanRequest build() {
            boolean isValidScanType = ScanRequest.isValidScanType(this.mScanType);
            Preconditions.checkState(isValidScanType, "invalid scan type : " + this.mScanType + ", scan type must be one of ScanRequest#SCAN_TYPE_");
            boolean isValidScanMode = ScanRequest.isValidScanMode(this.mScanMode);
            Preconditions.checkState(isValidScanMode, "invalid scan mode : " + this.mScanMode + ", scan mode must be one of ScanMode#SCAN_MODE_");
            return new ScanRequest(this.mScanType, this.mScanMode, this.mBleEnabled, this.mWorkSource, this.mScanFilters);
        }
    }
}
