package android.net.wifi.p2p.nsd;

import com.android.launcher3.icons.cache.BaseIconCache;

public class WifiP2pDnsSdServiceRequest extends WifiP2pServiceRequest {
    private WifiP2pDnsSdServiceRequest(String str) {
        super(1, str);
    }

    private WifiP2pDnsSdServiceRequest() {
        super(1, (String) null);
    }

    private WifiP2pDnsSdServiceRequest(String str, int i, int i2) {
        super(1, WifiP2pDnsSdServiceInfo.createRequest(str, i, i2));
    }

    public static WifiP2pDnsSdServiceRequest newInstance() {
        return new WifiP2pDnsSdServiceRequest();
    }

    public static WifiP2pDnsSdServiceRequest newInstance(String str) {
        if (str != null) {
            return new WifiP2pDnsSdServiceRequest(str + ".local.", 12, 1);
        }
        throw new IllegalArgumentException("service type cannot be null");
    }

    public static WifiP2pDnsSdServiceRequest newInstance(String str, String str2) {
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("instance name or service type cannot be null");
        }
        return new WifiP2pDnsSdServiceRequest(str + BaseIconCache.EMPTY_CLASS_NAME + str2 + ".local.", 16, 1);
    }
}
