package com.android.settings.network.telephony;

import android.telephony.AccessNetworkConstants;
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
import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import com.android.internal.telephony.OperatorInfo;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public final class CellInfoUtil {
    public static String getNetworkTitle(CellIdentity cellIdentity, String str) {
        if (cellIdentity != null) {
            String objects = Objects.toString(cellIdentity.getOperatorAlphaLong(), "");
            if (TextUtils.isEmpty(objects)) {
                objects = Objects.toString(cellIdentity.getOperatorAlphaShort(), "");
            }
            if (!TextUtils.isEmpty(objects)) {
                return objects;
            }
        }
        return TextUtils.isEmpty(str) ? "" : BidiFormatter.getInstance().unicodeWrap(str, TextDirectionHeuristics.LTR);
    }

    public static CellIdentity getCellIdentity(CellInfo cellInfo) {
        if (cellInfo == null) {
            return null;
        }
        if (cellInfo instanceof CellInfoGsm) {
            return ((CellInfoGsm) cellInfo).getCellIdentity();
        }
        if (cellInfo instanceof CellInfoCdma) {
            return ((CellInfoCdma) cellInfo).getCellIdentity();
        }
        if (cellInfo instanceof CellInfoWcdma) {
            return ((CellInfoWcdma) cellInfo).getCellIdentity();
        }
        if (cellInfo instanceof CellInfoTdscdma) {
            return ((CellInfoTdscdma) cellInfo).getCellIdentity();
        }
        if (cellInfo instanceof CellInfoLte) {
            return ((CellInfoLte) cellInfo).getCellIdentity();
        }
        if (!(cellInfo instanceof CellInfoNr)) {
            return null;
        }
        return ((CellInfoNr) cellInfo).getCellIdentity();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:15:0x01c3  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x01d8  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0174  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x004e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static CellInfo convertLegacyIncrScanOperatorInfoToCellInfo(OperatorInfo operatorInfo) {
        String str;
        String str2;
        int convertRanToAnt;
        CellInfoGsm cellInfoGsm;
        CellInfoGsm cellInfoGsm2;
        CellInfoGsm cellInfoGsm3;
        CellInfoGsm cellInfoGsm4;
        CellInfoGsm cellInfoGsm5;
        String operatorNumeric = operatorInfo.getOperatorNumeric();
        String valueOf = String.valueOf(0);
        CellInfoGsm cellInfoGsm6 = null;
        if (operatorNumeric != null) {
            if (operatorNumeric.matches("^[0-9]{5,6}$")) {
                str2 = operatorNumeric.substring(0, 3);
                str = operatorNumeric.substring(3);
            } else if (operatorNumeric.matches("^[0-9]{5,6}[+][0-9]{1,2}$")) {
                String[] split = operatorNumeric.split("\\+");
                String substring = split[0].substring(0, 3);
                String substring2 = split[0].substring(3);
                valueOf = split[1];
                str = substring2;
                str2 = substring;
            }
            convertRanToAnt = AccessNetworkConstants.AccessNetworkType.convertRanToAnt(Integer.parseInt(valueOf));
            if (convertRanToAnt == 1) {
                if (convertRanToAnt == 2) {
                    CellIdentityWcdma cellIdentityWcdma = new CellIdentityWcdma(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, str2, str, operatorInfo.getOperatorAlphaLong() + " 3G", operatorInfo.getOperatorAlphaShort() + " 3G", Collections.emptyList(), null);
                    CellInfoWcdma cellInfoWcdma = new CellInfoWcdma();
                    cellInfoWcdma.setCellIdentity(cellIdentityWcdma);
                    cellInfoGsm3 = cellInfoWcdma;
                    cellInfoGsm5 = null;
                    cellInfoGsm = null;
                } else if (convertRanToAnt == 3) {
                    CellIdentityLte cellIdentityLte = new CellIdentityLte(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, null, Integer.MAX_VALUE, str2, str, operatorInfo.getOperatorAlphaLong() + " 4G", operatorInfo.getOperatorAlphaShort() + " 4G", Collections.emptyList(), null);
                    CellInfoLte cellInfoLte = new CellInfoLte();
                    cellInfoLte.setCellIdentity(cellIdentityLte);
                    cellInfoGsm3 = null;
                    cellInfoGsm = null;
                    cellInfoGsm5 = cellInfoLte;
                } else if (convertRanToAnt == 6) {
                    CellIdentityNr cellIdentityNr = new CellIdentityNr(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, null, str2, str, 2147483647L, operatorInfo.getOperatorAlphaLong() + " 5G", operatorInfo.getOperatorAlphaShort() + " 5G", Collections.emptyList());
                    CellInfoNr cellInfoNr = new CellInfoNr();
                    cellInfoNr.setCellIdentity(cellIdentityNr);
                    cellInfoGsm3 = null;
                    cellInfoGsm = null;
                    cellInfoGsm4 = null;
                    cellInfoGsm6 = cellInfoNr;
                    cellInfoGsm2 = null;
                } else {
                    CellIdentityGsm cellIdentityGsm = new CellIdentityGsm(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, str2, str, operatorInfo.getOperatorAlphaLong(), operatorInfo.getOperatorAlphaShort(), Collections.emptyList());
                    CellInfoGsm cellInfoGsm7 = new CellInfoGsm();
                    cellInfoGsm7.setCellIdentity(cellIdentityGsm);
                    cellInfoGsm4 = cellInfoGsm7;
                    cellInfoGsm2 = null;
                    cellInfoGsm3 = null;
                    cellInfoGsm = null;
                }
                cellInfoGsm4 = cellInfoGsm;
                cellInfoGsm2 = cellInfoGsm5;
            } else {
                CellIdentityGsm cellIdentityGsm2 = new CellIdentityGsm(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, str2, str, operatorInfo.getOperatorAlphaLong() + " 2G", operatorInfo.getOperatorAlphaShort() + " 2G", Collections.emptyList());
                CellInfoGsm cellInfoGsm8 = new CellInfoGsm();
                cellInfoGsm8.setCellIdentity(cellIdentityGsm2);
                cellInfoGsm = cellInfoGsm8;
                cellInfoGsm2 = null;
                cellInfoGsm3 = null;
                cellInfoGsm4 = null;
            }
            if (cellInfoGsm6 == null) {
                cellInfoGsm6 = cellInfoGsm2 != null ? cellInfoGsm2 : cellInfoGsm3 != null ? cellInfoGsm3 : cellInfoGsm != null ? cellInfoGsm : cellInfoGsm4;
            }
            if (operatorInfo.getState() == OperatorInfo.State.CURRENT) {
                cellInfoGsm6.setRegistered(true);
            }
            return cellInfoGsm6;
        }
        str = null;
        str2 = null;
        convertRanToAnt = AccessNetworkConstants.AccessNetworkType.convertRanToAnt(Integer.parseInt(valueOf));
        if (convertRanToAnt == 1) {
        }
        if (cellInfoGsm6 == null) {
        }
        if (operatorInfo.getState() == OperatorInfo.State.CURRENT) {
        }
        return cellInfoGsm6;
    }

    public static String cellInfoListToString(List<CellInfo> list) {
        return (String) list.stream().map(CellInfoUtil$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.joining(", "));
    }

    public static String cellInfoToString(CellInfo cellInfo) {
        String simpleName = cellInfo.getClass().getSimpleName();
        CellIdentity cellIdentity = getCellIdentity(cellInfo);
        return String.format("{CellType = %s, isRegistered = %b, mcc = %s, mnc = %s, alphaL = %s, alphaS = %s}", simpleName, Boolean.valueOf(cellInfo.isRegistered()), getCellIdentityMcc(cellIdentity), getCellIdentityMnc(cellIdentity), cellIdentity.getOperatorAlphaLong(), cellIdentity.getOperatorAlphaShort());
    }

    public static String getCellIdentityMccMnc(CellIdentity cellIdentity) {
        String cellIdentityMcc = getCellIdentityMcc(cellIdentity);
        String cellIdentityMnc = getCellIdentityMnc(cellIdentity);
        if (cellIdentityMcc == null || cellIdentityMnc == null) {
            return null;
        }
        return cellIdentityMcc + cellIdentityMnc;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String getCellIdentityMcc(CellIdentity cellIdentity) {
        String str;
        if (cellIdentity != null) {
            if (cellIdentity instanceof CellIdentityGsm) {
                str = ((CellIdentityGsm) cellIdentity).getMccString();
            } else if (cellIdentity instanceof CellIdentityWcdma) {
                str = ((CellIdentityWcdma) cellIdentity).getMccString();
            } else if (cellIdentity instanceof CellIdentityTdscdma) {
                str = ((CellIdentityTdscdma) cellIdentity).getMccString();
            } else if (cellIdentity instanceof CellIdentityLte) {
                str = ((CellIdentityLte) cellIdentity).getMccString();
            } else if (cellIdentity instanceof CellIdentityNr) {
                str = ((CellIdentityNr) cellIdentity).getMccString();
            }
            if (str == null) {
                return str;
            }
            return null;
        }
        str = null;
        if (str == null) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String getCellIdentityMnc(CellIdentity cellIdentity) {
        String str;
        if (cellIdentity != null) {
            if (cellIdentity instanceof CellIdentityGsm) {
                str = ((CellIdentityGsm) cellIdentity).getMncString();
            } else if (cellIdentity instanceof CellIdentityWcdma) {
                str = ((CellIdentityWcdma) cellIdentity).getMncString();
            } else if (cellIdentity instanceof CellIdentityTdscdma) {
                str = ((CellIdentityTdscdma) cellIdentity).getMncString();
            } else if (cellIdentity instanceof CellIdentityLte) {
                str = ((CellIdentityLte) cellIdentity).getMncString();
            } else if (cellIdentity instanceof CellIdentityNr) {
                str = ((CellIdentityNr) cellIdentity).getMncString();
            }
            if (str == null) {
                return str;
            }
            return null;
        }
        str = null;
        if (str == null) {
        }
    }
}
