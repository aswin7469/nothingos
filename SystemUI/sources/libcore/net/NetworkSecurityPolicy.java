package libcore.net;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public abstract class NetworkSecurityPolicy {
    private static volatile NetworkSecurityPolicy instance = new DefaultNetworkSecurityPolicy();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public abstract boolean isCertificateTransparencyVerificationRequired(String str);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public abstract boolean isCleartextTrafficPermitted();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public abstract boolean isCleartextTrafficPermitted(String str);

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static NetworkSecurityPolicy getInstance() {
        return instance;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setInstance(NetworkSecurityPolicy networkSecurityPolicy) {
        if (networkSecurityPolicy != null) {
            instance = networkSecurityPolicy;
            return;
        }
        throw new NullPointerException("policy == null");
    }

    public static final class DefaultNetworkSecurityPolicy extends NetworkSecurityPolicy {
        public boolean isCertificateTransparencyVerificationRequired(String str) {
            return false;
        }

        public boolean isCleartextTrafficPermitted() {
            return true;
        }

        public boolean isCleartextTrafficPermitted(String str) {
            return isCleartextTrafficPermitted();
        }
    }
}
