package android.net.wifi;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.telephony.SubscriptionManager;
import android.util.Log;
import java.util.IllegalFormatException;

public class WifiStringResourceWrapper {
    static final String CARRIER_ID_RESOURCE_NAME_SUFFIX = "_carrier_overrides";
    static final String CARRIER_ID_RESOURCE_SEPARATOR = ":::";
    private static final String TAG = "WifiStringResourceWrapper";
    private final int mCarrierId;
    private final String mCarrierIdPrefix;
    private final WifiContext mContext;
    private final Resources mResources = getResourcesForSubId();
    private final int mSubId;

    public WifiStringResourceWrapper(WifiContext wifiContext, int i, int i2) {
        this.mContext = wifiContext;
        this.mSubId = i;
        this.mCarrierId = i2;
        this.mCarrierIdPrefix = CARRIER_ID_RESOURCE_SEPARATOR + i2 + CARRIER_ID_RESOURCE_SEPARATOR;
    }

    public String getString(String str, Object... objArr) {
        int identifier;
        Resources resources = this.mResources;
        if (resources == null || (identifier = resources.getIdentifier(str, "string", this.mContext.getWifiOverlayApkPkgName())) == 0) {
            return null;
        }
        if (this.mCarrierId != -1) {
            Resources resources2 = this.mResources;
            int identifier2 = resources2.getIdentifier(str + CARRIER_ID_RESOURCE_NAME_SUFFIX, "array", this.mContext.getWifiOverlayApkPkgName());
            if (identifier2 != 0) {
                String[] stringArray = this.mResources.getStringArray(identifier2);
                int length = stringArray.length;
                int i = 0;
                while (i < length) {
                    String str2 = stringArray[i];
                    if (str2.indexOf(this.mCarrierIdPrefix) != 0) {
                        i++;
                    } else {
                        try {
                            return String.format(str2.substring(this.mCarrierIdPrefix.length()), objArr);
                        } catch (IllegalFormatException e) {
                            Log.e(TAG, "Resource formatting error - '" + str + "' - " + e);
                            return null;
                        }
                    }
                }
            }
        }
        try {
            return this.mResources.getString(identifier, objArr);
        } catch (IllegalFormatException e2) {
            Log.e(TAG, "Resource formatting error - '" + str + "' - " + e2);
            return null;
        }
    }

    private Resources getResourcesForSubId() {
        try {
            WifiContext wifiContext = this.mContext;
            return SubscriptionManager.getResourcesForSubId(wifiContext.createPackageContext(wifiContext.getWifiOverlayApkPkgName(), 0), this.mSubId);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }
}
