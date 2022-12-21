package javax.crypto;

final class JceSecurityManager extends SecurityManager {
    static final JceSecurityManager INSTANCE = null;

    /* access modifiers changed from: package-private */
    public CryptoPermission getCryptoPermission(String str) {
        return null;
    }

    private JceSecurityManager() {
    }
}
