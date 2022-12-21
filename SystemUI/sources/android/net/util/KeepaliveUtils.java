package android.net.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityResources;
import android.net.NetworkCapabilities;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import com.android.systemui.navigationbar.NavigationBarInflaterView;

public final class KeepaliveUtils {
    public static final String TAG = "KeepaliveUtils";

    public static class KeepaliveDeviceConfigurationException extends AndroidRuntimeException {
        public KeepaliveDeviceConfigurationException(String str) {
            super(str);
        }
    }

    public static int[] getSupportedKeepalives(Context context) {
        String[] strArr;
        try {
            ConnectivityResources connectivityResources = new ConnectivityResources(context);
            strArr = new ConnectivityResources(context).get().getStringArray(connectivityResources.get().getIdentifier("config_networkSupportedKeepaliveCount", "array", connectivityResources.getResourcesContext().getPackageName()));
        } catch (Resources.NotFoundException unused) {
            strArr = null;
        }
        if (strArr != null) {
            int[] iArr = new int[9];
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                String str = strArr[i];
                if (!TextUtils.isEmpty(str)) {
                    String[] split = str.split(NavigationBarInflaterView.BUTTON_SEPARATOR);
                    if (split.length == 2) {
                        try {
                            int parseInt = Integer.parseInt(split[0]);
                            int parseInt2 = Integer.parseInt(split[1]);
                            if (!NetworkCapabilities.isValidTransport(parseInt)) {
                                throw new KeepaliveDeviceConfigurationException("Invalid transport " + parseInt);
                            } else if (parseInt2 >= 0) {
                                iArr[parseInt] = parseInt2;
                                i++;
                            } else {
                                throw new KeepaliveDeviceConfigurationException("Invalid supported count " + parseInt2 + " for " + NetworkCapabilities.transportNameOf(parseInt));
                            }
                        } catch (NumberFormatException unused2) {
                            throw new KeepaliveDeviceConfigurationException("Invalid number format");
                        }
                    } else {
                        throw new KeepaliveDeviceConfigurationException("Invalid parameter length");
                    }
                } else {
                    throw new KeepaliveDeviceConfigurationException("Empty string");
                }
            }
            return iArr;
        }
        throw new KeepaliveDeviceConfigurationException("invalid resource");
    }

    public static int getSupportedKeepalivesForNetworkCapabilities(int[] iArr, NetworkCapabilities networkCapabilities) {
        int[] transportTypes = networkCapabilities.getTransportTypes();
        if (transportTypes.length == 0) {
            return 0;
        }
        int i = iArr[transportTypes[0]];
        for (int i2 : transportTypes) {
            int i3 = iArr[i2];
            if (i > i3) {
                i = i3;
            }
        }
        return i;
    }
}
