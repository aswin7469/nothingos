package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.ArraySet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes3.dex */
public final class CellIdentityWcdma extends CellIdentity {
    private static final boolean DBG = false;
    private static final int MAX_CID = 268435455;
    private static final int MAX_LAC = 65535;
    private static final int MAX_PSC = 511;
    private static final int MAX_UARFCN = 16383;
    private final ArraySet<String> mAdditionalPlmns;
    private final int mCid;
    private final ClosedSubscriberGroupInfo mCsgInfo;
    private final int mLac;
    private final int mPsc;
    private final int mUarfcn;
    private static final String TAG = CellIdentityWcdma.class.getSimpleName();
    public static final Parcelable.Creator<CellIdentityWcdma> CREATOR = new Parcelable.Creator<CellIdentityWcdma>() { // from class: android.telephony.CellIdentityWcdma.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public CellIdentityWcdma mo3559createFromParcel(Parcel in) {
            in.readInt();
            return CellIdentityWcdma.createFromParcelBody(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public CellIdentityWcdma[] mo3560newArray(int size) {
            return new CellIdentityWcdma[size];
        }
    };

    public CellIdentityWcdma() {
        super(TAG, 4, null, null, null, null);
        this.mLac = Integer.MAX_VALUE;
        this.mCid = Integer.MAX_VALUE;
        this.mPsc = Integer.MAX_VALUE;
        this.mUarfcn = Integer.MAX_VALUE;
        this.mAdditionalPlmns = new ArraySet<>();
        this.mCsgInfo = null;
        this.mGlobalCellId = null;
    }

    public CellIdentityWcdma(int lac, int cid, int psc, int uarfcn, String mccStr, String mncStr, String alphal, String alphas, Collection<String> additionalPlmns, ClosedSubscriberGroupInfo csgInfo) {
        super(TAG, 4, mccStr, mncStr, alphal, alphas);
        this.mLac = inRangeOrUnavailable(lac, 0, 65535);
        this.mCid = inRangeOrUnavailable(cid, 0, (int) MAX_CID);
        this.mPsc = inRangeOrUnavailable(psc, 0, 511);
        this.mUarfcn = inRangeOrUnavailable(uarfcn, 0, (int) MAX_UARFCN);
        this.mAdditionalPlmns = new ArraySet<>(additionalPlmns.size());
        for (String plmn : additionalPlmns) {
            if (isValidPlmn(plmn)) {
                this.mAdditionalPlmns.add(plmn);
            }
        }
        this.mCsgInfo = csgInfo;
        updateGlobalCellId();
    }

    public CellIdentityWcdma(android.hardware.radio.V1_0.CellIdentityWcdma cid) {
        this(cid.lac, cid.cid, cid.psc, cid.uarfcn, cid.mcc, cid.mnc, "", "", new ArraySet(), null);
    }

    public CellIdentityWcdma(android.hardware.radio.V1_2.CellIdentityWcdma cid) {
        this(cid.base.lac, cid.base.cid, cid.base.psc, cid.base.uarfcn, cid.base.mcc, cid.base.mnc, cid.operatorNames.alphaLong, cid.operatorNames.alphaShort, new ArraySet(), null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CellIdentityWcdma(android.hardware.radio.V1_5.CellIdentityWcdma cid) {
        this(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
        ClosedSubscriberGroupInfo closedSubscriberGroupInfo;
        int i = cid.base.base.lac;
        int i2 = cid.base.base.cid;
        int i3 = cid.base.base.psc;
        int i4 = cid.base.base.uarfcn;
        String str = cid.base.base.mcc;
        String str2 = cid.base.base.mnc;
        String str3 = cid.base.operatorNames.alphaLong;
        String str4 = cid.base.operatorNames.alphaShort;
        ArrayList<String> arrayList = cid.additionalPlmns;
        if (cid.optionalCsgInfo.getDiscriminator() == 1) {
            closedSubscriberGroupInfo = new ClosedSubscriberGroupInfo(cid.optionalCsgInfo.csgInfo());
        } else {
            closedSubscriberGroupInfo = null;
        }
    }

    private CellIdentityWcdma(CellIdentityWcdma cid) {
        this(cid.mLac, cid.mCid, cid.mPsc, cid.mUarfcn, cid.mMccStr, cid.mMncStr, cid.mAlphaLong, cid.mAlphaShort, cid.mAdditionalPlmns, cid.mCsgInfo);
    }

    @Override // android.telephony.CellIdentity
    /* renamed from: sanitizeLocationInfo  reason: collision with other method in class */
    public CellIdentityWcdma mo2458sanitizeLocationInfo() {
        return new CellIdentityWcdma(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, this.mMccStr, this.mMncStr, this.mAlphaLong, this.mAlphaShort, this.mAdditionalPlmns, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CellIdentityWcdma copy() {
        return new CellIdentityWcdma(this);
    }

    @Override // android.telephony.CellIdentity
    protected void updateGlobalCellId() {
        this.mGlobalCellId = null;
        String plmn = getPlmn();
        if (plmn == null || this.mLac == Integer.MAX_VALUE || this.mCid == Integer.MAX_VALUE) {
            return;
        }
        this.mGlobalCellId = plmn + TextUtils.formatSimple("%04x%04x", Integer.valueOf(this.mLac), Integer.valueOf(this.mCid));
    }

    @Deprecated
    public int getMcc() {
        if (this.mMccStr != null) {
            return Integer.valueOf(this.mMccStr).intValue();
        }
        return Integer.MAX_VALUE;
    }

    @Deprecated
    public int getMnc() {
        if (this.mMncStr != null) {
            return Integer.valueOf(this.mMncStr).intValue();
        }
        return Integer.MAX_VALUE;
    }

    public int getLac() {
        return this.mLac;
    }

    public int getCid() {
        return this.mCid;
    }

    public int getPsc() {
        return this.mPsc;
    }

    @Override // android.telephony.CellIdentity
    public String getMccString() {
        return this.mMccStr;
    }

    @Override // android.telephony.CellIdentity
    public String getMncString() {
        return this.mMncStr;
    }

    public String getMobileNetworkOperator() {
        if (this.mMccStr == null || this.mMncStr == null) {
            return null;
        }
        return this.mMccStr + this.mMncStr;
    }

    @Override // android.telephony.CellIdentity
    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mLac), Integer.valueOf(this.mCid), Integer.valueOf(this.mPsc), Integer.valueOf(this.mAdditionalPlmns.hashCode()), Integer.valueOf(super.hashCode()));
    }

    public int getUarfcn() {
        return this.mUarfcn;
    }

    @Override // android.telephony.CellIdentity
    public int getChannelNumber() {
        return this.mUarfcn;
    }

    public Set<String> getAdditionalPlmns() {
        return Collections.unmodifiableSet(this.mAdditionalPlmns);
    }

    public ClosedSubscriberGroupInfo getClosedSubscriberGroupInfo() {
        return this.mCsgInfo;
    }

    @Override // android.telephony.CellIdentity
    /* renamed from: asCellLocation  reason: collision with other method in class */
    public GsmCellLocation mo2457asCellLocation() {
        GsmCellLocation cl = new GsmCellLocation();
        int lac = this.mLac;
        int psc = -1;
        if (lac == Integer.MAX_VALUE) {
            lac = -1;
        }
        int cid = this.mCid;
        if (cid == Integer.MAX_VALUE) {
            cid = -1;
        }
        int i = this.mPsc;
        if (i != Integer.MAX_VALUE) {
            psc = i;
        }
        cl.setLacAndCid(lac, cid);
        cl.setPsc(psc);
        return cl;
    }

    @Override // android.telephony.CellIdentity
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CellIdentityWcdma)) {
            return false;
        }
        CellIdentityWcdma o = (CellIdentityWcdma) other;
        return this.mLac == o.mLac && this.mCid == o.mCid && this.mPsc == o.mPsc && this.mUarfcn == o.mUarfcn && TextUtils.equals(this.mMccStr, o.mMccStr) && TextUtils.equals(this.mMncStr, o.mMncStr) && this.mAdditionalPlmns.equals(o.mAdditionalPlmns) && Objects.equals(this.mCsgInfo, o.mCsgInfo) && super.equals(other);
    }

    public String toString() {
        return TAG + ":{ mLac=" + this.mLac + " mCid=" + this.mCid + " mPsc=" + this.mPsc + " mUarfcn=" + this.mUarfcn + " mMcc=" + this.mMccStr + " mMnc=" + this.mMncStr + " mAlphaLong=" + this.mAlphaLong + " mAlphaShort=" + this.mAlphaShort + " mAdditionalPlmns=" + this.mAdditionalPlmns + " mCsgInfo=" + this.mCsgInfo + "}";
    }

    @Override // android.telephony.CellIdentity, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, 4);
        dest.writeInt(this.mLac);
        dest.writeInt(this.mCid);
        dest.writeInt(this.mPsc);
        dest.writeInt(this.mUarfcn);
        dest.writeArraySet(this.mAdditionalPlmns);
        dest.writeParcelable(this.mCsgInfo, flags);
    }

    private CellIdentityWcdma(Parcel in) {
        super(TAG, 4, in);
        this.mLac = in.readInt();
        this.mCid = in.readInt();
        this.mPsc = in.readInt();
        this.mUarfcn = in.readInt();
        this.mAdditionalPlmns = in.readArraySet(null);
        this.mCsgInfo = (ClosedSubscriberGroupInfo) in.readParcelable(null);
        updateGlobalCellId();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static CellIdentityWcdma createFromParcelBody(Parcel in) {
        return new CellIdentityWcdma(in);
    }
}
