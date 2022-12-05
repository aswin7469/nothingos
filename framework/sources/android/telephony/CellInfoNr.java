package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes3.dex */
public final class CellInfoNr extends CellInfo {
    public static final Parcelable.Creator<CellInfoNr> CREATOR = new Parcelable.Creator<CellInfoNr>() { // from class: android.telephony.CellInfoNr.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public CellInfoNr mo3559createFromParcel(Parcel in) {
            in.readInt();
            return new CellInfoNr(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public CellInfoNr[] mo3560newArray(int size) {
            return new CellInfoNr[size];
        }
    };
    private static final String TAG = "CellInfoNr";
    private CellIdentityNr mCellIdentity;
    private final CellSignalStrengthNr mCellSignalStrength;

    @Override // android.telephony.CellInfo
    /* renamed from: getCellIdentity  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ CellIdentityNr mo2483getCellIdentity() {
        return (CellIdentityNr) mo2483getCellIdentity();
    }

    @Override // android.telephony.CellInfo
    /* renamed from: getCellSignalStrength  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ CellSignalStrengthNr mo2484getCellSignalStrength() {
        return (CellSignalStrengthNr) mo2484getCellSignalStrength();
    }

    public CellInfoNr() {
        this.mCellIdentity = new CellIdentityNr();
        this.mCellSignalStrength = new CellSignalStrengthNr();
    }

    private CellInfoNr(Parcel in) {
        super(in);
        this.mCellIdentity = CellIdentityNr.CREATOR.mo3559createFromParcel(in);
        this.mCellSignalStrength = CellSignalStrengthNr.CREATOR.mo3559createFromParcel(in);
    }

    private CellInfoNr(CellInfoNr other, boolean sanitizeLocationInfo) {
        super(other);
        this.mCellIdentity = sanitizeLocationInfo ? other.mCellIdentity.mo2458sanitizeLocationInfo() : other.mCellIdentity;
        this.mCellSignalStrength = other.mCellSignalStrength;
    }

    public CellInfoNr(android.hardware.radio.V1_4.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_4.CellInfoNr cil = ci.info.nr();
        this.mCellIdentity = new CellIdentityNr(cil.cellidentity);
        this.mCellSignalStrength = new CellSignalStrengthNr(cil.signalStrength);
    }

    public CellInfoNr(android.hardware.radio.V1_5.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_5.CellInfoNr cil = ci.ratSpecificInfo.nr();
        this.mCellIdentity = new CellIdentityNr(cil.cellIdentityNr);
        this.mCellSignalStrength = new CellSignalStrengthNr(cil.signalStrengthNr);
    }

    public CellInfoNr(android.hardware.radio.V1_6.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_6.CellInfoNr cil = ci.ratSpecificInfo.nr();
        this.mCellIdentity = new CellIdentityNr(cil.cellIdentityNr);
        this.mCellSignalStrength = new CellSignalStrengthNr(cil.signalStrengthNr);
    }

    @Override // android.telephony.CellInfo
    /* renamed from: getCellIdentity */
    public CellIdentity mo2483getCellIdentity() {
        return this.mCellIdentity;
    }

    public void setCellIdentity(CellIdentityNr cid) {
        this.mCellIdentity = cid;
    }

    @Override // android.telephony.CellInfo
    /* renamed from: getCellSignalStrength */
    public CellSignalStrength mo2484getCellSignalStrength() {
        return this.mCellSignalStrength;
    }

    @Override // android.telephony.CellInfo
    public CellInfo sanitizeLocationInfo() {
        return new CellInfoNr(this, true);
    }

    @Override // android.telephony.CellInfo
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), this.mCellIdentity, this.mCellSignalStrength);
    }

    @Override // android.telephony.CellInfo
    public boolean equals(Object other) {
        if (!(other instanceof CellInfoNr)) {
            return false;
        }
        CellInfoNr o = (CellInfoNr) other;
        return super.equals(o) && this.mCellIdentity.equals(o.mCellIdentity) && this.mCellSignalStrength.equals(o.mCellSignalStrength);
    }

    @Override // android.telephony.CellInfo
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CellInfoNr:{");
        sb.append(" " + super.toString());
        sb.append(" " + this.mCellIdentity);
        sb.append(" " + this.mCellSignalStrength);
        sb.append(" }");
        return sb.toString();
    }

    @Override // android.telephony.CellInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 6);
        this.mCellIdentity.writeToParcel(dest, flags);
        this.mCellSignalStrength.writeToParcel(dest, flags);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static CellInfoNr createFromParcelBody(Parcel in) {
        return new CellInfoNr(in);
    }
}
