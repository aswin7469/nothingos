package javax.net.ssl;

import java.security.Principal;

public abstract class X509ExtendedKeyManager implements X509KeyManager {
    public String chooseEngineClientAlias(String[] strArr, Principal[] principalArr, SSLEngine sSLEngine) {
        return null;
    }

    public String chooseEngineServerAlias(String str, Principal[] principalArr, SSLEngine sSLEngine) {
        return null;
    }

    protected X509ExtendedKeyManager() {
    }
}
