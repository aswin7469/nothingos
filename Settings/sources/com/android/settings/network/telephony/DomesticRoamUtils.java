package com.android.settings.network.telephony;

import android.content.Context;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R$array;
import com.android.settings.R$bool;
import com.android.settings.R$string;
import java.util.Arrays;

public class DomesticRoamUtils {
    private static final boolean DEBUG = Log.isLoggable(DomesticRoamUtils.class.getSimpleName(), 3);
    private static final String TAG = "DomesticRoamUtils";

    private enum OPERATOR_TYPE {
        CHINA_MOBILE,
        CHINA_UNION,
        CHINA_TELECOM,
        CHINA_BROADCAST,
        OTHERS
    }

    public static boolean isFeatureEnabled(Context context) {
        boolean z = context.getApplicationContext().getResources().getBoolean(R$bool.config_domestic_roam_customization);
        if (DEBUG) {
            String str = TAG;
            Log.d(str, "isFeatureEnabled: isCustomizeEnabled = " + z);
        }
        return z;
    }

    public static String getRegisteredOperatorName(Context context, int i) {
        if (DEBUG) {
            String str = TAG;
            Log.d(str, "getRegisteredOperatorName: context = " + context + " subId = " + i);
        }
        Context applicationContext = context.getApplicationContext();
        OPERATOR_TYPE homeOperatorType = getHomeOperatorType(applicationContext, i);
        OPERATOR_TYPE visitOperatorType = getVisitOperatorType(applicationContext, i);
        String str2 = TAG;
        Log.d(str2, "getRegisteredOperatorName: homeOperator = " + homeOperatorType + " visitOperator = " + visitOperatorType);
        OPERATOR_TYPE operator_type = OPERATOR_TYPE.OTHERS;
        if (homeOperatorType != operator_type && visitOperatorType != operator_type && homeOperatorType != visitOperatorType) {
            return combineOperatorNames(applicationContext, homeOperatorType, visitOperatorType);
        }
        Log.d(str2, "getRegisteredOperatorName: not domestic roam status, return empty");
        return "";
    }

    public static String getMPLMNOperatorName(Context context, int i, String str) {
        if (DEBUG) {
            String str2 = TAG;
            Log.d(str2, "getMPLMNOperatorName: context = " + context + " subId = " + i + " idMPLMN = " + str);
        }
        Context applicationContext = context.getApplicationContext();
        OPERATOR_TYPE homeOperatorType = getHomeOperatorType(applicationContext, i);
        OPERATOR_TYPE mPLMNOperatorType = getMPLMNOperatorType(applicationContext, i, str);
        String str3 = TAG;
        Log.d(str3, "getMPLMNOperatorName: homeOperator = " + homeOperatorType + " mplmnOperator = " + mPLMNOperatorType);
        OPERATOR_TYPE operator_type = OPERATOR_TYPE.OTHERS;
        if (homeOperatorType != operator_type && mPLMNOperatorType != operator_type && homeOperatorType != mPLMNOperatorType) {
            return combineOperatorNames(applicationContext, homeOperatorType, mPLMNOperatorType);
        }
        Log.d(str3, "getMPLMNOperatorName: not domestic roam status, return empty");
        return "";
    }

    private static OPERATOR_TYPE getMPLMNOperatorType(Context context, int i, String str) {
        boolean z = DEBUG;
        if (z) {
            String str2 = TAG;
            Log.d(str2, "getMPLMNOperatorType: idMPLMN = " + str + " subId = " + i);
        }
        if (TextUtils.isEmpty(str)) {
            if (z) {
                Log.d(TAG, "getMPLMNOperatorType: idMPLMN is empty");
            }
            return OPERATOR_TYPE.OTHERS;
        } else if (Arrays.asList(context.getResources().getStringArray(R$array.domestic_roaming_mplmns)).contains(str) || isMPLMNRegistered(context, i, str)) {
            return getOperatorType(context, str);
        } else {
            return OPERATOR_TYPE.OTHERS;
        }
    }

    private static boolean isMPLMNRegistered(Context context, int i, String str) {
        TelephonyManager createForSubscriptionId = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
        if (createForSubscriptionId == null) {
            return false;
        }
        String networkOperator = createForSubscriptionId.getNetworkOperator(i);
        if (!isValidDomesticOperatorId(networkOperator) || !networkOperator.equalsIgnoreCase(str)) {
            return false;
        }
        Log.d(TAG, "isMPLMNRegistered: return true");
        return true;
    }

    private static OPERATOR_TYPE getHomeOperatorType(Context context, int i) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        if (subscriptionManager == null || subscriptionManager.isActiveSubscriptionId(i)) {
            TelephonyManager createForSubscriptionId = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
            if (createForSubscriptionId != null) {
                return getOperatorType(context, createForSubscriptionId.getSimOperator(i));
            }
            return OPERATOR_TYPE.OTHERS;
        }
        if (DEBUG) {
            Log.d(TAG, "getHomeOperatorType: invalid sub");
        }
        return OPERATOR_TYPE.OTHERS;
    }

    private static OPERATOR_TYPE getVisitOperatorType(Context context, int i) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        if (subscriptionManager == null || subscriptionManager.isActiveSubscriptionId(i)) {
            TelephonyManager createForSubscriptionId = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
            if (createForSubscriptionId != null) {
                return getOperatorType(context, createForSubscriptionId.getNetworkOperator(i));
            }
            return OPERATOR_TYPE.OTHERS;
        }
        if (DEBUG) {
            Log.d(TAG, "getVisitOperatorType: invalid sub");
        }
        return OPERATOR_TYPE.OTHERS;
    }

    private static OPERATOR_TYPE getOperatorType(Context context, String str) {
        boolean z = DEBUG;
        if (z) {
            String str2 = TAG;
            Log.d(str2, "getOperatorType: operatorId = " + str);
        }
        if (!isValidDomesticOperatorId(str)) {
            if (z) {
                Log.d(TAG, "getOperatorType: invalid domestic operator id");
            }
            return OPERATOR_TYPE.OTHERS;
        } else if (Arrays.asList(context.getResources().getStringArray(R$array.china_mobile_ids)).contains(str)) {
            return OPERATOR_TYPE.CHINA_MOBILE;
        } else {
            if (Arrays.asList(context.getResources().getStringArray(R$array.china_union_ids)).contains(str)) {
                return OPERATOR_TYPE.CHINA_UNION;
            }
            if (Arrays.asList(context.getResources().getStringArray(R$array.china_telecom_ids)).contains(str)) {
                return OPERATOR_TYPE.CHINA_TELECOM;
            }
            if (Arrays.asList(context.getResources().getStringArray(R$array.china_broadcast_ids)).contains(str)) {
                return OPERATOR_TYPE.CHINA_BROADCAST;
            }
            return OPERATOR_TYPE.OTHERS;
        }
    }

    private static boolean isValidDomesticOperatorId(String str) {
        if (TextUtils.isEmpty(str)) {
            if (DEBUG) {
                Log.d(TAG, "isValidDomesticOperatorId: operatorId is empty");
            }
            return false;
        }
        int length = str.length();
        if (length == 5 || length == 6) {
            String substring = str.substring(0, 3);
            if ("460".equalsIgnoreCase(substring)) {
                return true;
            }
            if (DEBUG) {
                String str2 = TAG;
                Log.d(str2, "isValidDomesticOperatorId: mcc = " + substring);
            }
            return false;
        }
        if (DEBUG) {
            String str3 = TAG;
            Log.d(str3, "isValidDomesticOperatorId: idLength = " + length);
        }
        return false;
    }

    /* renamed from: com.android.settings.network.telephony.DomesticRoamUtils$1 */
    static /* synthetic */ class C10961 {

        /* renamed from: $SwitchMap$com$android$settings$network$telephony$DomesticRoamUtils$OPERATOR_TYPE */
        static final /* synthetic */ int[] f203x4886b0b0;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.settings.network.telephony.DomesticRoamUtils$OPERATOR_TYPE[] r0 = com.android.settings.network.telephony.DomesticRoamUtils.OPERATOR_TYPE.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f203x4886b0b0 = r0
                com.android.settings.network.telephony.DomesticRoamUtils$OPERATOR_TYPE r1 = com.android.settings.network.telephony.DomesticRoamUtils.OPERATOR_TYPE.CHINA_MOBILE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f203x4886b0b0     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.settings.network.telephony.DomesticRoamUtils$OPERATOR_TYPE r1 = com.android.settings.network.telephony.DomesticRoamUtils.OPERATOR_TYPE.CHINA_UNION     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f203x4886b0b0     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.settings.network.telephony.DomesticRoamUtils$OPERATOR_TYPE r1 = com.android.settings.network.telephony.DomesticRoamUtils.OPERATOR_TYPE.CHINA_BROADCAST     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f203x4886b0b0     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.settings.network.telephony.DomesticRoamUtils$OPERATOR_TYPE r1 = com.android.settings.network.telephony.DomesticRoamUtils.OPERATOR_TYPE.CHINA_TELECOM     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f203x4886b0b0     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.settings.network.telephony.DomesticRoamUtils$OPERATOR_TYPE r1 = com.android.settings.network.telephony.DomesticRoamUtils.OPERATOR_TYPE.OTHERS     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.telephony.DomesticRoamUtils.C10961.<clinit>():void");
        }
    }

    private static String combineOperatorNames(Context context, OPERATOR_TYPE operator_type, OPERATOR_TYPE operator_type2) {
        StringBuilder sb = new StringBuilder();
        int[] iArr = C10961.f203x4886b0b0;
        int i = iArr[operator_type.ordinal()];
        if (i == 1) {
            sb.append(context.getResources().getString(R$string.china_mobile));
        } else if (i == 2) {
            sb.append(context.getResources().getString(R$string.china_union));
        } else if (i == 3) {
            sb.append(context.getResources().getString(R$string.china_broadcast));
        } else if (i == 4) {
            sb.append(context.getResources().getString(R$string.china_telecom));
        } else if (i == 5) {
            Log.w(TAG, "combineOperatorNames: home invalid case");
            return "";
        }
        sb.append(context.getResources().getString(R$string.separator));
        int i2 = iArr[operator_type2.ordinal()];
        if (i2 == 1) {
            sb.append(context.getResources().getString(R$string.china_mobile));
        } else if (i2 == 2) {
            sb.append(context.getResources().getString(R$string.china_union));
        } else if (i2 == 3) {
            sb.append(context.getResources().getString(R$string.china_broadcast));
        } else if (i2 == 4) {
            sb.append(context.getResources().getString(R$string.china_telecom));
        } else if (i2 == 5) {
            Log.w(TAG, "combineOperatorNames: visit invalid case");
            return "";
        }
        String str = TAG;
        Log.d(str, "combineOperatorNames: outputBuilder = " + sb);
        return sb.toString();
    }
}
