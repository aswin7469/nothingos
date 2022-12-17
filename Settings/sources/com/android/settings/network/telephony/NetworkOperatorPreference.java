package com.android.settings.network.telephony;

import android.content.Context;
import android.telephony.CagInfo;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.SnpnInfo;
import android.util.Log;
import androidx.preference.Preference;
import com.android.internal.telephony.OperatorInfo;
import com.android.settings.R$drawable;
import com.android.settings.R$string;
import com.qti.extphone.ExtTelephonyManager;
import java.util.List;
import java.util.Objects;

public class NetworkOperatorPreference extends Preference {
    private int mAccessMode;
    private CellIdentity mCellId;
    private CellInfo mCellInfo;
    private ExtTelephonyManager mExtTelephonyManager;
    private List<String> mForbiddenPlmns;
    private boolean mIsAdvancedScanSupported;
    private int mLevel;
    private boolean mShow4GForLTE;
    private int mSubId;

    public NetworkOperatorPreference(Context context, CellInfo cellInfo, List<String> list, boolean z, int i) {
        this(context, list, z, i);
        if (!DomesticRoamUtils.isFeatureEnabled(context)) {
            updateCell(cellInfo);
        }
    }

    public NetworkOperatorPreference(Context context, CellIdentity cellIdentity, List<String> list, boolean z, int i) {
        this(context, list, z, i);
        updateCell((CellInfo) null, cellIdentity);
    }

    private NetworkOperatorPreference(Context context, List<String> list, boolean z, int i) {
        super(context);
        this.mLevel = -1;
        this.mSubId = -1;
        this.mForbiddenPlmns = list;
        this.mShow4GForLTE = z;
        this.mIsAdvancedScanSupported = TelephonyUtils.isAdvancedPlmnScanSupported(context);
        Log.d("NetworkOperatorPref", "mIsAdvancedScanSupported: " + this.mIsAdvancedScanSupported);
        this.mAccessMode = i;
    }

    public void updateCell(CellInfo cellInfo) {
        updateCell(cellInfo, CellInfoUtil.getCellIdentity(cellInfo));
    }

    /* access modifiers changed from: protected */
    public void updateCell(CellInfo cellInfo, CellIdentity cellIdentity) {
        this.mCellInfo = cellInfo;
        this.mCellId = cellIdentity;
        this.mExtTelephonyManager = ExtTelephonyManager.getInstance(getContext());
        refresh();
    }

    public boolean isSameCell(CellInfo cellInfo) {
        if (cellInfo == null) {
            return false;
        }
        return this.mCellId.equals(CellInfoUtil.getCellIdentity(cellInfo));
    }

    public boolean isForbiddenNetwork() {
        List<String> list = this.mForbiddenPlmns;
        return list != null && list.contains(getOperatorNumeric());
    }

    public void refresh() {
        int i;
        String mPLMNOperatorName;
        String operatorName = getOperatorName();
        if (DomesticRoamUtils.isFeatureEnabled(getContext()) && "" != (mPLMNOperatorName = DomesticRoamUtils.getMPLMNOperatorName(getContext(), this.mSubId, getOperatorNumeric()))) {
            operatorName = mPLMNOperatorName;
        }
        if (MobileNetworkUtils.isCagSnpnEnabled(getContext()) && (this.mCellId instanceof CellIdentityNr)) {
            String networkInfo = getNetworkInfo();
            Log.d("NetworkOperatorPref", "networkInfo: " + networkInfo);
            operatorName = operatorName + " " + networkInfo;
        }
        if (isForbiddenNetwork()) {
            operatorName = operatorName + " " + getContext().getResources().getString(R$string.forbidden_network);
        }
        setTitle((CharSequence) Objects.toString(operatorName, ""));
        if (this.mCellInfo != null) {
            if (MobileNetworkUtils.isCagSnpnEnabled(getContext())) {
                CellIdentity cellIdentity = this.mCellId;
                if ((cellIdentity instanceof CellIdentityNr) && ((CellIdentityNr) cellIdentity).getSnpnInfo() != null) {
                    i = ((CellIdentityNr) this.mCellId).getSnpnInfo().getLevel();
                    this.mLevel = i;
                    updateIcon(i);
                }
            }
            CellSignalStrength cellSignalStrength = getCellSignalStrength(this.mCellInfo);
            i = cellSignalStrength != null ? cellSignalStrength.getLevel() : -1;
            this.mLevel = i;
            updateIcon(i);
        }
    }

    public void setIcon(int i) {
        updateIcon(i);
    }

    public String getOperatorNumeric() {
        CellIdentity cellIdentity = this.mCellId;
        if (cellIdentity == null) {
            return null;
        }
        if (cellIdentity instanceof CellIdentityGsm) {
            return ((CellIdentityGsm) cellIdentity).getMobileNetworkOperator();
        }
        if (cellIdentity instanceof CellIdentityWcdma) {
            return ((CellIdentityWcdma) cellIdentity).getMobileNetworkOperator();
        }
        if (cellIdentity instanceof CellIdentityTdscdma) {
            return ((CellIdentityTdscdma) cellIdentity).getMobileNetworkOperator();
        }
        if (cellIdentity instanceof CellIdentityLte) {
            return ((CellIdentityLte) cellIdentity).getMobileNetworkOperator();
        }
        if (!(cellIdentity instanceof CellIdentityNr)) {
            return null;
        }
        if (MobileNetworkUtils.isCagSnpnEnabled(getContext())) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            if (cellIdentityNr.getSnpnInfo() != null) {
                return cellIdentityNr.getSnpnInfo().getOperatorNumeric();
            }
        }
        CellIdentityNr cellIdentityNr2 = (CellIdentityNr) cellIdentity;
        String mccString = cellIdentityNr2.getMccString();
        if (mccString == null) {
            return null;
        }
        return mccString.concat(cellIdentityNr2.getMncString());
    }

    public String getOperatorName() {
        return CellInfoUtil.getNetworkTitle(this.mCellId, getOperatorNumeric());
    }

    public String getNetworkInfo() {
        return CellInfoUtil.getNetworkInfo((CellIdentityNr) this.mCellId);
    }

    public OperatorInfo getOperatorInfo() {
        if (!MobileNetworkUtils.isCagSnpnEnabled(getContext())) {
            return new OperatorInfo(Objects.toString(this.mCellId.getOperatorAlphaLong(), ""), Objects.toString(this.mCellId.getOperatorAlphaShort(), ""), getOperatorNumeric(), getAccessNetworkTypeFromCellInfo(this.mCellInfo));
        }
        if (this.mCellId instanceof CellIdentityNr) {
            return new OperatorInfo(Objects.toString(this.mCellId.getOperatorAlphaLong(), ""), Objects.toString(this.mCellId.getOperatorAlphaShort(), ""), getOperatorNumeric(), getAccessNetworkTypeFromCellInfo(this.mCellInfo), this.mAccessMode, ((CellIdentityNr) this.mCellId).getCagInfo(), ((CellIdentityNr) this.mCellId).getSnpnInfo());
        }
        return new OperatorInfo(Objects.toString(this.mCellId.getOperatorAlphaLong(), ""), Objects.toString(this.mCellId.getOperatorAlphaShort(), ""), getOperatorNumeric(), getAccessNetworkTypeFromCellInfo(this.mCellInfo), this.mAccessMode, (CagInfo) null, (SnpnInfo) null);
    }

    private int getIconIdForCell(CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoGsm) {
            return R$drawable.signal_strength_g;
        }
        if (cellInfo instanceof CellInfoCdma) {
            return R$drawable.signal_strength_1x;
        }
        if ((cellInfo instanceof CellInfoWcdma) || (cellInfo instanceof CellInfoTdscdma)) {
            return R$drawable.signal_strength_3g;
        }
        if (cellInfo instanceof CellInfoLte) {
            return this.mShow4GForLTE ? R$drawable.ic_signal_strength_4g : R$drawable.signal_strength_lte;
        }
        if (cellInfo instanceof CellInfoNr) {
            return R$drawable.signal_strength_5g;
        }
        return 0;
    }

    private CellSignalStrength getCellSignalStrength(CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoGsm) {
            return ((CellInfoGsm) cellInfo).getCellSignalStrength();
        }
        if (cellInfo instanceof CellInfoCdma) {
            return ((CellInfoCdma) cellInfo).getCellSignalStrength();
        }
        if (cellInfo instanceof CellInfoWcdma) {
            return ((CellInfoWcdma) cellInfo).getCellSignalStrength();
        }
        if (cellInfo instanceof CellInfoTdscdma) {
            return ((CellInfoTdscdma) cellInfo).getCellSignalStrength();
        }
        if (cellInfo instanceof CellInfoLte) {
            return ((CellInfoLte) cellInfo).getCellSignalStrength();
        }
        if (cellInfo instanceof CellInfoNr) {
            return ((CellInfoNr) cellInfo).getCellSignalStrength();
        }
        return null;
    }

    private int getAccessNetworkTypeFromCellInfo(CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoGsm) {
            return 1;
        }
        if (cellInfo instanceof CellInfoCdma) {
            return 4;
        }
        if ((cellInfo instanceof CellInfoWcdma) || (cellInfo instanceof CellInfoTdscdma)) {
            return 2;
        }
        if (cellInfo instanceof CellInfoLte) {
            return 3;
        }
        return cellInfo instanceof CellInfoNr ? 6 : 0;
    }

    private void updateIcon(int i) {
        if (this.mIsAdvancedScanSupported && i >= 0 && i < 5) {
            setIcon(MobileNetworkUtils.getSignalStrengthIcon(getContext(), i, 5, getIconIdForCell(this.mCellInfo), false));
        }
    }

    public void setSubId(int i) {
        this.mSubId = i;
    }
}
