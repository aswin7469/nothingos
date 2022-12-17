package com.android.settings.network.telephony;

import android.telephony.CellIdentity;
import android.telephony.CellIdentityNr;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return BidiFormatter.getInstance().unicodeWrap(str, TextDirectionHeuristics.LTR);
    }

    public static String getNetworkInfo(CellIdentityNr cellIdentityNr) {
        if (cellIdentityNr == null) {
            return "";
        }
        if (cellIdentityNr.getSnpnInfo() != null) {
            String str = "" + "SNPN: ";
            for (byte b : cellIdentityNr.getSnpnInfo().getNid()) {
                str = str + String.format("%02X", new Object[]{Byte.valueOf(b)});
            }
            return str;
        } else if (cellIdentityNr.getCagInfo() == null) {
            return "";
        } else {
            if (cellIdentityNr.getCagInfo().getCagName() == null || cellIdentityNr.getCagInfo().getCagName().isEmpty()) {
                return "" + "CAG: " + cellIdentityNr.getCagInfo().getCagId();
            }
            return "" + "CAG: " + cellIdentityNr.getCagInfo().getCagName();
        }
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
        if (cellInfo instanceof CellInfoNr) {
            return ((CellInfoNr) cellInfo).getCellIdentity();
        }
        return null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v3, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v6, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: android.telephony.CellInfoWcdma} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: android.telephony.CellInfoLte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: android.telephony.CellInfoNr} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v7, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: android.telephony.CellInfoGsm} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v20, resolved type: android.telephony.CellInfoGsm} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0174  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x01c3  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x01d8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.telephony.CellInfo convertLegacyIncrScanOperatorInfoToCellInfo(com.android.internal.telephony.OperatorInfo r20) {
        /*
            java.lang.String r0 = r20.getOperatorNumeric()
            r1 = 0
            java.lang.String r2 = java.lang.String.valueOf(r1)
            r3 = 1
            r4 = 3
            r5 = 0
            if (r0 == 0) goto L_0x0042
            java.lang.String r6 = "^[0-9]{5,6}$"
            boolean r6 = r0.matches(r6)
            if (r6 == 0) goto L_0x001f
            java.lang.String r1 = r0.substring(r1, r4)
            java.lang.String r0 = r0.substring(r4)
            goto L_0x0044
        L_0x001f:
            java.lang.String r6 = "^[0-9]{5,6}[+][0-9]{1,2}$"
            boolean r6 = r0.matches(r6)
            if (r6 == 0) goto L_0x0042
            java.lang.String r2 = "\\+"
            java.lang.String[] r0 = r0.split(r2)
            r2 = r0[r1]
            java.lang.String r2 = r2.substring(r1, r4)
            r1 = r0[r1]
            java.lang.String r1 = r1.substring(r4)
            r0 = r0[r3]
            r19 = r2
            r2 = r0
            r0 = r1
            r1 = r19
            goto L_0x0044
        L_0x0042:
            r0 = r5
            r1 = r0
        L_0x0044:
            int r2 = java.lang.Integer.parseInt(r2)
            int r2 = android.telephony.AccessNetworkConstants.AccessNetworkType.convertRanToAnt(r2)
            if (r2 == r3) goto L_0x0174
            r6 = 2
            if (r2 == r6) goto L_0x0125
            if (r2 == r4) goto L_0x00d4
            r4 = 6
            if (r2 == r4) goto L_0x0084
            android.telephony.CellIdentityGsm r2 = new android.telephony.CellIdentityGsm
            r7 = 2147483647(0x7fffffff, float:NaN)
            r8 = 2147483647(0x7fffffff, float:NaN)
            r9 = 2147483647(0x7fffffff, float:NaN)
            r10 = 2147483647(0x7fffffff, float:NaN)
            java.lang.String r13 = r20.getOperatorAlphaLong()
            java.lang.String r14 = r20.getOperatorAlphaShort()
            java.util.List r15 = java.util.Collections.emptyList()
            r6 = r2
            r11 = r1
            r12 = r0
            r6.<init>(r7, r8, r9, r10, r11, r12, r13, r14, r15)
            android.telephony.CellInfoGsm r0 = new android.telephony.CellInfoGsm
            r0.<init>()
            r0.setCellIdentity(r2)
            r4 = r0
            r0 = r5
            r1 = r0
            r2 = r1
            goto L_0x01c0
        L_0x0084:
            android.telephony.CellIdentityNr r2 = new android.telephony.CellIdentityNr
            r7 = 2147483647(0x7fffffff, float:NaN)
            r8 = 2147483647(0x7fffffff, float:NaN)
            r9 = 2147483647(0x7fffffff, float:NaN)
            r10 = 0
            r13 = 2147483647(0x7fffffff, double:1.060997895E-314)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = r20.getOperatorAlphaLong()
            r4.append(r6)
            java.lang.String r6 = " 5G"
            r4.append(r6)
            java.lang.String r15 = r4.toString()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r11 = r20.getOperatorAlphaShort()
            r4.append(r11)
            r4.append(r6)
            java.lang.String r16 = r4.toString()
            java.util.List r17 = java.util.Collections.emptyList()
            r6 = r2
            r11 = r1
            r12 = r0
            r6.<init>(r7, r8, r9, r10, r11, r12, r13, r15, r16, r17)
            android.telephony.CellInfoNr r0 = new android.telephony.CellInfoNr
            r0.<init>()
            r0.setCellIdentity(r2)
            r1 = r5
            r2 = r1
            r4 = r2
            r5 = r0
            r0 = r4
            goto L_0x01c0
        L_0x00d4:
            android.telephony.CellIdentityLte r2 = new android.telephony.CellIdentityLte
            r7 = 2147483647(0x7fffffff, float:NaN)
            r8 = 2147483647(0x7fffffff, float:NaN)
            r9 = 2147483647(0x7fffffff, float:NaN)
            r10 = 2147483647(0x7fffffff, float:NaN)
            r11 = 0
            r12 = 2147483647(0x7fffffff, float:NaN)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = r20.getOperatorAlphaLong()
            r4.append(r6)
            java.lang.String r6 = " 4G"
            r4.append(r6)
            java.lang.String r15 = r4.toString()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r13 = r20.getOperatorAlphaShort()
            r4.append(r13)
            r4.append(r6)
            java.lang.String r16 = r4.toString()
            java.util.List r17 = java.util.Collections.emptyList()
            r18 = 0
            r6 = r2
            r13 = r1
            r14 = r0
            r6.<init>(r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)
            android.telephony.CellInfoLte r0 = new android.telephony.CellInfoLte
            r0.<init>()
            r0.setCellIdentity(r2)
            r1 = r5
            r2 = r1
            goto L_0x0172
        L_0x0125:
            android.telephony.CellIdentityWcdma r2 = new android.telephony.CellIdentityWcdma
            r7 = 2147483647(0x7fffffff, float:NaN)
            r8 = 2147483647(0x7fffffff, float:NaN)
            r9 = 2147483647(0x7fffffff, float:NaN)
            r10 = 2147483647(0x7fffffff, float:NaN)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = r20.getOperatorAlphaLong()
            r4.append(r6)
            java.lang.String r6 = " 3G"
            r4.append(r6)
            java.lang.String r13 = r4.toString()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r11 = r20.getOperatorAlphaShort()
            r4.append(r11)
            r4.append(r6)
            java.lang.String r14 = r4.toString()
            java.util.List r15 = java.util.Collections.emptyList()
            r16 = 0
            r6 = r2
            r11 = r1
            r12 = r0
            r6.<init>(r7, r8, r9, r10, r11, r12, r13, r14, r15, r16)
            android.telephony.CellInfoWcdma r0 = new android.telephony.CellInfoWcdma
            r0.<init>()
            r0.setCellIdentity(r2)
            r1 = r0
            r0 = r5
            r2 = r0
        L_0x0172:
            r4 = r2
            goto L_0x01c0
        L_0x0174:
            android.telephony.CellIdentityGsm r2 = new android.telephony.CellIdentityGsm
            r7 = 2147483647(0x7fffffff, float:NaN)
            r8 = 2147483647(0x7fffffff, float:NaN)
            r9 = 2147483647(0x7fffffff, float:NaN)
            r10 = 2147483647(0x7fffffff, float:NaN)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = r20.getOperatorAlphaLong()
            r4.append(r6)
            java.lang.String r6 = " 2G"
            r4.append(r6)
            java.lang.String r13 = r4.toString()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r11 = r20.getOperatorAlphaShort()
            r4.append(r11)
            r4.append(r6)
            java.lang.String r14 = r4.toString()
            java.util.List r15 = java.util.Collections.emptyList()
            r6 = r2
            r11 = r1
            r12 = r0
            r6.<init>(r7, r8, r9, r10, r11, r12, r13, r14, r15)
            android.telephony.CellInfoGsm r0 = new android.telephony.CellInfoGsm
            r0.<init>()
            r0.setCellIdentity(r2)
            r2 = r0
            r0 = r5
            r1 = r0
            r4 = r1
        L_0x01c0:
            if (r5 == 0) goto L_0x01c3
            goto L_0x01d0
        L_0x01c3:
            if (r0 == 0) goto L_0x01c7
            r5 = r0
            goto L_0x01d0
        L_0x01c7:
            if (r1 == 0) goto L_0x01cb
            r5 = r1
            goto L_0x01d0
        L_0x01cb:
            if (r2 == 0) goto L_0x01cf
            r5 = r2
            goto L_0x01d0
        L_0x01cf:
            r5 = r4
        L_0x01d0:
            com.android.internal.telephony.OperatorInfo$State r0 = r20.getState()
            com.android.internal.telephony.OperatorInfo$State r1 = com.android.internal.telephony.OperatorInfo.State.CURRENT
            if (r0 != r1) goto L_0x01db
            r5.setRegistered(r3)
        L_0x01db:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.telephony.CellInfoUtil.convertLegacyIncrScanOperatorInfoToCellInfo(com.android.internal.telephony.OperatorInfo):android.telephony.CellInfo");
    }

    public static String cellInfoListToString(List<CellInfo> list) {
        return (String) list.stream().map(new CellInfoUtil$$ExternalSyntheticLambda0()).collect(Collectors.joining(", "));
    }

    public static String cellInfoToString(CellInfo cellInfo) {
        CharSequence charSequence;
        String simpleName = cellInfo.getClass().getSimpleName();
        CellIdentity cellIdentity = getCellIdentity(cellInfo);
        String cellIdentityMcc = getCellIdentityMcc(cellIdentity);
        String cellIdentityMnc = getCellIdentityMnc(cellIdentity);
        CharSequence charSequence2 = null;
        if (cellIdentity != null) {
            charSequence2 = cellIdentity.getOperatorAlphaLong();
            charSequence = cellIdentity.getOperatorAlphaShort();
        } else {
            charSequence = null;
        }
        return String.format("{CellType = %s, isRegistered = %b, mcc = %s, mnc = %s, alphaL = %s, alphaS = %s}", new Object[]{simpleName, Boolean.valueOf(cellInfo.isRegistered()), cellIdentityMcc, cellIdentityMnc, charSequence2, charSequence});
    }

    public static String getCellIdentityMccMnc(CellIdentity cellIdentity) {
        String cellIdentityMcc = getCellIdentityMcc(cellIdentity);
        String cellIdentityMnc = getCellIdentityMnc(cellIdentity);
        if (cellIdentityMcc == null || cellIdentityMnc == null) {
            return null;
        }
        return cellIdentityMcc + cellIdentityMnc;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCellIdentityMcc(android.telephony.CellIdentity r2) {
        /*
            r0 = 0
            if (r2 == 0) goto L_0x003a
            boolean r1 = r2 instanceof android.telephony.CellIdentityGsm
            if (r1 == 0) goto L_0x000e
            android.telephony.CellIdentityGsm r2 = (android.telephony.CellIdentityGsm) r2
            java.lang.String r2 = r2.getMccString()
            goto L_0x003b
        L_0x000e:
            boolean r1 = r2 instanceof android.telephony.CellIdentityWcdma
            if (r1 == 0) goto L_0x0019
            android.telephony.CellIdentityWcdma r2 = (android.telephony.CellIdentityWcdma) r2
            java.lang.String r2 = r2.getMccString()
            goto L_0x003b
        L_0x0019:
            boolean r1 = r2 instanceof android.telephony.CellIdentityTdscdma
            if (r1 == 0) goto L_0x0024
            android.telephony.CellIdentityTdscdma r2 = (android.telephony.CellIdentityTdscdma) r2
            java.lang.String r2 = r2.getMccString()
            goto L_0x003b
        L_0x0024:
            boolean r1 = r2 instanceof android.telephony.CellIdentityLte
            if (r1 == 0) goto L_0x002f
            android.telephony.CellIdentityLte r2 = (android.telephony.CellIdentityLte) r2
            java.lang.String r2 = r2.getMccString()
            goto L_0x003b
        L_0x002f:
            boolean r1 = r2 instanceof android.telephony.CellIdentityNr
            if (r1 == 0) goto L_0x003a
            android.telephony.CellIdentityNr r2 = (android.telephony.CellIdentityNr) r2
            java.lang.String r2 = r2.getMccString()
            goto L_0x003b
        L_0x003a:
            r2 = r0
        L_0x003b:
            if (r2 != 0) goto L_0x003e
            goto L_0x003f
        L_0x003e:
            r0 = r2
        L_0x003f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.telephony.CellInfoUtil.getCellIdentityMcc(android.telephony.CellIdentity):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCellIdentityMnc(android.telephony.CellIdentity r2) {
        /*
            r0 = 0
            if (r2 == 0) goto L_0x003a
            boolean r1 = r2 instanceof android.telephony.CellIdentityGsm
            if (r1 == 0) goto L_0x000e
            android.telephony.CellIdentityGsm r2 = (android.telephony.CellIdentityGsm) r2
            java.lang.String r2 = r2.getMncString()
            goto L_0x003b
        L_0x000e:
            boolean r1 = r2 instanceof android.telephony.CellIdentityWcdma
            if (r1 == 0) goto L_0x0019
            android.telephony.CellIdentityWcdma r2 = (android.telephony.CellIdentityWcdma) r2
            java.lang.String r2 = r2.getMncString()
            goto L_0x003b
        L_0x0019:
            boolean r1 = r2 instanceof android.telephony.CellIdentityTdscdma
            if (r1 == 0) goto L_0x0024
            android.telephony.CellIdentityTdscdma r2 = (android.telephony.CellIdentityTdscdma) r2
            java.lang.String r2 = r2.getMncString()
            goto L_0x003b
        L_0x0024:
            boolean r1 = r2 instanceof android.telephony.CellIdentityLte
            if (r1 == 0) goto L_0x002f
            android.telephony.CellIdentityLte r2 = (android.telephony.CellIdentityLte) r2
            java.lang.String r2 = r2.getMncString()
            goto L_0x003b
        L_0x002f:
            boolean r1 = r2 instanceof android.telephony.CellIdentityNr
            if (r1 == 0) goto L_0x003a
            android.telephony.CellIdentityNr r2 = (android.telephony.CellIdentityNr) r2
            java.lang.String r2 = r2.getMncString()
            goto L_0x003b
        L_0x003a:
            r2 = r0
        L_0x003b:
            if (r2 != 0) goto L_0x003e
            goto L_0x003f
        L_0x003e:
            r0 = r2
        L_0x003f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.telephony.CellInfoUtil.getCellIdentityMnc(android.telephony.CellIdentity):java.lang.String");
    }
}
