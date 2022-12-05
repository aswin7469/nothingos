package android.telephony;

import android.hardware.camera2.impl.CameraDeviceImpl$9$$ExternalSyntheticLambda0;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.ArraySet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes3.dex */
public final class CellIdentityLte extends CellIdentity {
    private static final boolean DBG = false;
    private static final int MAX_BANDWIDTH = 20000;
    private static final int MAX_CI = 268435455;
    private static final int MAX_EARFCN = 262143;
    private static final int MAX_PCI = 503;
    private static final int MAX_TAC = 65535;
    private final ArraySet<String> mAdditionalPlmns;
    private final int[] mBands;
    private final int mBandwidth;
    private final int mCi;
    private ClosedSubscriberGroupInfo mCsgInfo;
    private final int mEarfcn;
    private final int mPci;
    private final int mTac;
    private static final String TAG = CellIdentityLte.class.getSimpleName();
    public static final Parcelable.Creator<CellIdentityLte> CREATOR = new Parcelable.Creator<CellIdentityLte>() { // from class: android.telephony.CellIdentityLte.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public CellIdentityLte mo3559createFromParcel(Parcel in) {
            in.readInt();
            return CellIdentityLte.createFromParcelBody(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public CellIdentityLte[] mo3560newArray(int size) {
            return new CellIdentityLte[size];
        }
    };

    public CellIdentityLte() {
        super(TAG, 3, null, null, null, null);
        this.mCi = Integer.MAX_VALUE;
        this.mPci = Integer.MAX_VALUE;
        this.mTac = Integer.MAX_VALUE;
        this.mEarfcn = Integer.MAX_VALUE;
        this.mBands = new int[0];
        this.mBandwidth = Integer.MAX_VALUE;
        this.mAdditionalPlmns = new ArraySet<>();
        this.mCsgInfo = null;
        this.mGlobalCellId = null;
    }

    public CellIdentityLte(int mcc, int mnc, int ci, int pci, int tac) {
        this(ci, pci, tac, Integer.MAX_VALUE, new int[0], Integer.MAX_VALUE, String.valueOf(mcc), String.valueOf(mnc), null, null, new ArraySet(), null);
    }

    public CellIdentityLte(int ci, int pci, int tac, int earfcn, int[] bands, int bandwidth, String mccStr, String mncStr, String alphal, String alphas, Collection<String> additionalPlmns, ClosedSubscriberGroupInfo csgInfo) {
        super(TAG, 3, mccStr, mncStr, alphal, alphas);
        this.mCi = inRangeOrUnavailable(ci, 0, (int) MAX_CI);
        this.mPci = inRangeOrUnavailable(pci, 0, 503);
        this.mTac = inRangeOrUnavailable(tac, 0, 65535);
        this.mEarfcn = inRangeOrUnavailable(earfcn, 0, (int) MAX_EARFCN);
        this.mBands = bands;
        this.mBandwidth = inRangeOrUnavailable(bandwidth, 0, 20000);
        this.mAdditionalPlmns = new ArraySet<>(additionalPlmns.size());
        for (String plmn : additionalPlmns) {
            if (isValidPlmn(plmn)) {
                this.mAdditionalPlmns.add(plmn);
            }
        }
        this.mCsgInfo = csgInfo;
        updateGlobalCellId();
    }

    public CellIdentityLte(android.hardware.radio.V1_0.CellIdentityLte cid) {
        this(cid.ci, cid.pci, cid.tac, cid.earfcn, new int[0], Integer.MAX_VALUE, cid.mcc, cid.mnc, "", "", new ArraySet(), null);
    }

    public CellIdentityLte(android.hardware.radio.V1_2.CellIdentityLte cid) {
        this(cid.base.ci, cid.base.pci, cid.base.tac, cid.base.earfcn, new int[0], cid.bandwidth, cid.base.mcc, cid.base.mnc, cid.operatorNames.alphaLong, cid.operatorNames.alphaShort, new ArraySet(), null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CellIdentityLte(android.hardware.radio.V1_5.CellIdentityLte cid) {
        this(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        ClosedSubscriberGroupInfo closedSubscriberGroupInfo;
        int i = cid.base.base.ci;
        int i2 = cid.base.base.pci;
        int i3 = cid.base.base.tac;
        int i4 = cid.base.base.earfcn;
        int[] array = cid.bands.stream().mapToInt(CameraDeviceImpl$9$$ExternalSyntheticLambda0.INSTANCE).toArray();
        int i5 = cid.base.bandwidth;
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

    private CellIdentityLte(CellIdentityLte cid) {
        this(cid.mCi, cid.mPci, cid.mTac, cid.mEarfcn, cid.mBands, cid.mBandwidth, cid.mMccStr, cid.mMncStr, cid.mAlphaLong, cid.mAlphaShort, cid.mAdditionalPlmns, cid.mCsgInfo);
    }

    @Override // android.telephony.CellIdentity
    /* renamed from: sanitizeLocationInfo  reason: collision with other method in class */
    public CellIdentityLte mo2458sanitizeLocationInfo() {
        return new CellIdentityLte(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, this.mBands, Integer.MAX_VALUE, this.mMccStr, this.mMncStr, this.mAlphaLong, this.mAlphaShort, this.mAdditionalPlmns, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CellIdentityLte copy() {
        return new CellIdentityLte(this);
    }

    @Override // android.telephony.CellIdentity
    protected void updateGlobalCellId() {
        this.mGlobalCellId = null;
        String plmn = getPlmn();
        if (plmn == null || this.mCi == Integer.MAX_VALUE) {
            return;
        }
        this.mGlobalCellId = plmn + TextUtils.formatSimple("%07x", Integer.valueOf(this.mCi));
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

    public int getCi() {
        return this.mCi;
    }

    public int getPci() {
        return this.mPci;
    }

    public int getTac() {
        return this.mTac;
    }

    public int getEarfcn() {
        return this.mEarfcn;
    }

    public int[] getBands() {
        int[] iArr = this.mBands;
        return Arrays.copyOf(iArr, iArr.length);
    }

    public int getBandwidth() {
        return this.mBandwidth;
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
    public int getChannelNumber() {
        return this.mEarfcn;
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
        int tac = this.mTac;
        int cid = -1;
        if (tac == Integer.MAX_VALUE) {
            tac = -1;
        }
        int i = this.mCi;
        if (i != Integer.MAX_VALUE) {
            cid = i;
        }
        cl.setLacAndCid(tac, cid);
        cl.setPsc(0);
        return cl;
    }

    @Override // android.telephony.CellIdentity
    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mCi), Integer.valueOf(this.mPci), Integer.valueOf(this.mTac), Integer.valueOf(this.mEarfcn), Integer.valueOf(Arrays.hashCode(this.mBands)), Integer.valueOf(this.mBandwidth), Integer.valueOf(this.mAdditionalPlmns.hashCode()), this.mCsgInfo, Integer.valueOf(super.hashCode()));
    }

    @Override // android.telephony.CellIdentity
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CellIdentityLte)) {
            return false;
        }
        CellIdentityLte o = (CellIdentityLte) other;
        return this.mCi == o.mCi && this.mPci == o.mPci && this.mTac == o.mTac && this.mEarfcn == o.mEarfcn && Arrays.equals(this.mBands, o.mBands) && this.mBandwidth == o.mBandwidth && TextUtils.equals(this.mMccStr, o.mMccStr) && TextUtils.equals(this.mMncStr, o.mMncStr) && this.mAdditionalPlmns.equals(o.mAdditionalPlmns) && Objects.equals(this.mCsgInfo, o.mCsgInfo) && super.equals(other);
    }

    public String toString() {
        return TAG + ":{ mCi=" + this.mCi + " mPci=" + this.mPci + " mTac=" + this.mTac + " mEarfcn=" + this.mEarfcn + " mBands=" + Arrays.toString(this.mBands) + " mBandwidth=" + this.mBandwidth + " mMcc=" + this.mMccStr + " mMnc=" + this.mMncStr + " mAlphaLong=" + this.mAlphaLong + " mAlphaShort=" + this.mAlphaShort + " mAdditionalPlmns=" + this.mAdditionalPlmns + " mCsgInfo=" + this.mCsgInfo + "}";
    }

    @Override // android.telephony.CellIdentity, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, 3);
        dest.writeInt(this.mCi);
        dest.writeInt(this.mPci);
        dest.writeInt(this.mTac);
        dest.writeInt(this.mEarfcn);
        dest.writeIntArray(this.mBands);
        dest.writeInt(this.mBandwidth);
        dest.writeArraySet(this.mAdditionalPlmns);
        dest.writeParcelable(this.mCsgInfo, flags);
    }

    private CellIdentityLte(Parcel in) {
        super(TAG, 3, in);
        this.mCi = in.readInt();
        this.mPci = in.readInt();
        this.mTac = in.readInt();
        this.mEarfcn = in.readInt();
        this.mBands = in.createIntArray();
        this.mBandwidth = in.readInt();
        this.mAdditionalPlmns = in.readArraySet(null);
        this.mCsgInfo = (ClosedSubscriberGroupInfo) in.readParcelable(null);
        updateGlobalCellId();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static CellIdentityLte createFromParcelBody(Parcel in) {
        return new CellIdentityLte(in);
    }
}
