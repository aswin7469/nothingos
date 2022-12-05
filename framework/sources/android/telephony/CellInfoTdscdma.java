package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes3.dex */
public final class CellInfoTdscdma extends CellInfo implements Parcelable {
    public static final Parcelable.Creator<CellInfoTdscdma> CREATOR = new Parcelable.Creator<CellInfoTdscdma>() { // from class: android.telephony.CellInfoTdscdma.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public CellInfoTdscdma mo3559createFromParcel(Parcel in) {
            in.readInt();
            return CellInfoTdscdma.createFromParcelBody(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public CellInfoTdscdma[] mo3560newArray(int size) {
            return new CellInfoTdscdma[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellInfoTdscdma";
    private CellIdentityTdscdma mCellIdentityTdscdma;
    private CellSignalStrengthTdscdma mCellSignalStrengthTdscdma;

    public CellInfoTdscdma() {
        this.mCellIdentityTdscdma = new CellIdentityTdscdma();
        this.mCellSignalStrengthTdscdma = new CellSignalStrengthTdscdma();
    }

    public CellInfoTdscdma(CellInfoTdscdma ci) {
        super(ci);
        this.mCellIdentityTdscdma = ci.mCellIdentityTdscdma.copy();
        this.mCellSignalStrengthTdscdma = ci.mCellSignalStrengthTdscdma.mo2502copy();
    }

    public CellInfoTdscdma(android.hardware.radio.V1_0.CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_0.CellInfoTdscdma cit = ci.tdscdma.get(0);
        this.mCellIdentityTdscdma = new CellIdentityTdscdma(cit.cellIdentityTdscdma);
        this.mCellSignalStrengthTdscdma = new CellSignalStrengthTdscdma(cit.signalStrengthTdscdma);
    }

    public CellInfoTdscdma(android.hardware.radio.V1_2.CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_2.CellInfoTdscdma cit = ci.tdscdma.get(0);
        this.mCellIdentityTdscdma = new CellIdentityTdscdma(cit.cellIdentityTdscdma);
        this.mCellSignalStrengthTdscdma = new CellSignalStrengthTdscdma(cit.signalStrengthTdscdma);
    }

    public CellInfoTdscdma(android.hardware.radio.V1_4.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_2.CellInfoTdscdma cit = ci.info.tdscdma();
        this.mCellIdentityTdscdma = new CellIdentityTdscdma(cit.cellIdentityTdscdma);
        this.mCellSignalStrengthTdscdma = new CellSignalStrengthTdscdma(cit.signalStrengthTdscdma);
    }

    public CellInfoTdscdma(android.hardware.radio.V1_5.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_5.CellInfoTdscdma cit = ci.ratSpecificInfo.tdscdma();
        this.mCellIdentityTdscdma = new CellIdentityTdscdma(cit.cellIdentityTdscdma);
        this.mCellSignalStrengthTdscdma = new CellSignalStrengthTdscdma(cit.signalStrengthTdscdma);
    }

    public CellInfoTdscdma(android.hardware.radio.V1_6.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_5.CellInfoTdscdma cit = ci.ratSpecificInfo.tdscdma();
        this.mCellIdentityTdscdma = new CellIdentityTdscdma(cit.cellIdentityTdscdma);
        this.mCellSignalStrengthTdscdma = new CellSignalStrengthTdscdma(cit.signalStrengthTdscdma);
    }

    @Override // android.telephony.CellInfo
    /* renamed from: getCellIdentity  reason: collision with other method in class */
    public CellIdentityTdscdma mo2483getCellIdentity() {
        return this.mCellIdentityTdscdma;
    }

    public void setCellIdentity(CellIdentityTdscdma cid) {
        this.mCellIdentityTdscdma = cid;
    }

    @Override // android.telephony.CellInfo
    /* renamed from: getCellSignalStrength  reason: collision with other method in class */
    public CellSignalStrengthTdscdma mo2484getCellSignalStrength() {
        return this.mCellSignalStrengthTdscdma;
    }

    @Override // android.telephony.CellInfo
    public CellInfo sanitizeLocationInfo() {
        CellInfoTdscdma result = new CellInfoTdscdma(this);
        result.mCellIdentityTdscdma = this.mCellIdentityTdscdma.mo2458sanitizeLocationInfo();
        return result;
    }

    public void setCellSignalStrength(CellSignalStrengthTdscdma css) {
        this.mCellSignalStrengthTdscdma = css;
    }

    @Override // android.telephony.CellInfo
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), this.mCellIdentityTdscdma, this.mCellSignalStrengthTdscdma);
    }

    @Override // android.telephony.CellInfo
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        try {
            CellInfoTdscdma o = (CellInfoTdscdma) other;
            if (!this.mCellIdentityTdscdma.equals(o.mCellIdentityTdscdma)) {
                return false;
            }
            return this.mCellSignalStrengthTdscdma.equals(o.mCellSignalStrengthTdscdma);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // android.telephony.CellInfo
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CellInfoTdscdma:{");
        sb.append(super.toString());
        sb.append(" ");
        sb.append(this.mCellIdentityTdscdma);
        sb.append(" ");
        sb.append(this.mCellSignalStrengthTdscdma);
        sb.append("}");
        return sb.toString();
    }

    @Override // android.telephony.CellInfo, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.telephony.CellInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 5);
        this.mCellIdentityTdscdma.writeToParcel(dest, flags);
        this.mCellSignalStrengthTdscdma.writeToParcel(dest, flags);
    }

    private CellInfoTdscdma(Parcel in) {
        super(in);
        this.mCellIdentityTdscdma = CellIdentityTdscdma.CREATOR.mo3559createFromParcel(in);
        this.mCellSignalStrengthTdscdma = CellSignalStrengthTdscdma.CREATOR.mo3559createFromParcel(in);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static CellInfoTdscdma createFromParcelBody(Parcel in) {
        return new CellInfoTdscdma(in);
    }

    private static void log(String s) {
        com.android.telephony.Rlog.w(LOG_TAG, s);
    }
}
