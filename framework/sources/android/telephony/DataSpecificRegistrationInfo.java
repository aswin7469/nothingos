package android.telephony;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
@SystemApi
/* loaded from: classes3.dex */
public final class DataSpecificRegistrationInfo implements Parcelable {
    public static final Parcelable.Creator<DataSpecificRegistrationInfo> CREATOR = new Parcelable.Creator<DataSpecificRegistrationInfo>() { // from class: android.telephony.DataSpecificRegistrationInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DataSpecificRegistrationInfo mo3559createFromParcel(Parcel source) {
            return new DataSpecificRegistrationInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DataSpecificRegistrationInfo[] mo3560newArray(int size) {
            return new DataSpecificRegistrationInfo[size];
        }
    };
    public final boolean isDcNrRestricted;
    public final boolean isEnDcAvailable;
    public final boolean isNrAvailable;
    private final VopsSupportInfo mVopsSupportInfo;
    public final int maxDataCalls;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DataSpecificRegistrationInfo(int maxDataCalls, boolean isDcNrRestricted, boolean isNrAvailable, boolean isEnDcAvailable, VopsSupportInfo vops) {
        this.maxDataCalls = maxDataCalls;
        this.isDcNrRestricted = isDcNrRestricted;
        this.isNrAvailable = isNrAvailable;
        this.isEnDcAvailable = isEnDcAvailable;
        this.mVopsSupportInfo = vops;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DataSpecificRegistrationInfo(DataSpecificRegistrationInfo dsri) {
        this.maxDataCalls = dsri.maxDataCalls;
        this.isDcNrRestricted = dsri.isDcNrRestricted;
        this.isNrAvailable = dsri.isNrAvailable;
        this.isEnDcAvailable = dsri.isEnDcAvailable;
        this.mVopsSupportInfo = dsri.mVopsSupportInfo;
    }

    private DataSpecificRegistrationInfo(Parcel source) {
        this.maxDataCalls = source.readInt();
        this.isDcNrRestricted = source.readBoolean();
        this.isNrAvailable = source.readBoolean();
        this.isEnDcAvailable = source.readBoolean();
        this.mVopsSupportInfo = (VopsSupportInfo) source.readParcelable(VopsSupportInfo.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxDataCalls);
        dest.writeBoolean(this.isDcNrRestricted);
        dest.writeBoolean(this.isNrAvailable);
        dest.writeBoolean(this.isEnDcAvailable);
        dest.writeParcelable(this.mVopsSupportInfo, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" :{");
        sb.append(" maxDataCalls = " + this.maxDataCalls);
        sb.append(" isDcNrRestricted = " + this.isDcNrRestricted);
        sb.append(" isNrAvailable = " + this.isNrAvailable);
        sb.append(" isEnDcAvailable = " + this.isEnDcAvailable);
        sb.append(" " + this.mVopsSupportInfo);
        sb.append(" }");
        return sb.toString();
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.maxDataCalls), Boolean.valueOf(this.isDcNrRestricted), Boolean.valueOf(this.isNrAvailable), Boolean.valueOf(this.isEnDcAvailable), this.mVopsSupportInfo);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSpecificRegistrationInfo)) {
            return false;
        }
        DataSpecificRegistrationInfo other = (DataSpecificRegistrationInfo) o;
        return this.maxDataCalls == other.maxDataCalls && this.isDcNrRestricted == other.isDcNrRestricted && this.isNrAvailable == other.isNrAvailable && this.isEnDcAvailable == other.isEnDcAvailable && Objects.equals(this.mVopsSupportInfo, other.mVopsSupportInfo);
    }

    @Deprecated
    public LteVopsSupportInfo getLteVopsSupportInfo() {
        VopsSupportInfo vopsSupportInfo = this.mVopsSupportInfo;
        if (vopsSupportInfo instanceof LteVopsSupportInfo) {
            return (LteVopsSupportInfo) vopsSupportInfo;
        }
        return new LteVopsSupportInfo(1, 1);
    }

    public VopsSupportInfo getVopsSupportInfo() {
        return this.mVopsSupportInfo;
    }
}
