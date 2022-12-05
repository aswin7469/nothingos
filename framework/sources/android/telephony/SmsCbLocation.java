package android.telephony;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
@SystemApi
/* loaded from: classes3.dex */
public final class SmsCbLocation implements Parcelable {
    public static final Parcelable.Creator<SmsCbLocation> CREATOR = new Parcelable.Creator<SmsCbLocation>() { // from class: android.telephony.SmsCbLocation.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public SmsCbLocation mo3559createFromParcel(Parcel in) {
            return new SmsCbLocation(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public SmsCbLocation[] mo3560newArray(int size) {
            return new SmsCbLocation[size];
        }
    };
    private final int mCid;
    private final int mLac;
    private final String mPlmn;

    public SmsCbLocation() {
        this.mPlmn = "";
        this.mLac = -1;
        this.mCid = -1;
    }

    public SmsCbLocation(String plmn) {
        this.mPlmn = plmn;
        this.mLac = -1;
        this.mCid = -1;
    }

    public SmsCbLocation(String plmn, int lac, int cid) {
        this.mPlmn = plmn;
        this.mLac = lac;
        this.mCid = cid;
    }

    public SmsCbLocation(Parcel in) {
        this.mPlmn = in.readString();
        this.mLac = in.readInt();
        this.mCid = in.readInt();
    }

    public String getPlmn() {
        return this.mPlmn;
    }

    public int getLac() {
        return this.mLac;
    }

    public int getCid() {
        return this.mCid;
    }

    public int hashCode() {
        int hash = this.mPlmn.hashCode();
        return (((hash * 31) + this.mLac) * 31) + this.mCid;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof SmsCbLocation)) {
            return false;
        }
        SmsCbLocation other = (SmsCbLocation) o;
        if (this.mPlmn.equals(other.mPlmn) && this.mLac == other.mLac && this.mCid == other.mCid) {
            return true;
        }
        return false;
    }

    public String toString() {
        return '[' + this.mPlmn + ',' + this.mLac + ',' + this.mCid + ']';
    }

    public boolean isInLocationArea(SmsCbLocation area) {
        int i = this.mCid;
        if (i == -1 || i == area.mCid) {
            int i2 = this.mLac;
            if (i2 != -1 && i2 != area.mLac) {
                return false;
            }
            return this.mPlmn.equals(area.mPlmn);
        }
        return false;
    }

    public boolean isInLocationArea(String plmn, int lac, int cid) {
        if (!this.mPlmn.equals(plmn)) {
            return false;
        }
        int i = this.mLac;
        if (i != -1 && i != lac) {
            return false;
        }
        int i2 = this.mCid;
        return i2 == -1 || i2 == cid;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPlmn);
        dest.writeInt(this.mLac);
        dest.writeInt(this.mCid);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
