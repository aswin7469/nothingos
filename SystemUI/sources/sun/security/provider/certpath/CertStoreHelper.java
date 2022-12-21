package sun.security.provider.certpath;

import java.net.URI;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.x500.X500Principal;
import sun.security.util.Cache;

public abstract class CertStoreHelper {
    private static final int NUM_TYPES = 2;
    /* access modifiers changed from: private */
    public static Cache<String, CertStoreHelper> cache = Cache.newSoftMemoryCache(2);
    private static final Map<String, String> classMap;

    public abstract CertStore getCertStore(URI uri) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

    public abstract boolean isCausedByNetworkIssue(CertStoreException certStoreException);

    public abstract X509CRLSelector wrap(X509CRLSelector x509CRLSelector, Collection<X500Principal> collection, String str) throws IOException;

    public abstract X509CertSelector wrap(X509CertSelector x509CertSelector, X500Principal x500Principal, String str) throws IOException;

    static {
        HashMap hashMap = new HashMap(2);
        classMap = hashMap;
        hashMap.put("LDAP", "sun.security.provider.certpath.ldap.LDAPCertStoreHelper");
        hashMap.put("SSLServer", "sun.security.provider.certpath.ssl.SSLServerCertStoreHelper");
    }

    public static CertStoreHelper getInstance(final String str) throws NoSuchAlgorithmException {
        CertStoreHelper certStoreHelper = cache.get(str);
        if (certStoreHelper != null) {
            return certStoreHelper;
        }
        final String str2 = classMap.get(str);
        if (str2 != null) {
            try {
                return (CertStoreHelper) AccessController.doPrivileged(new PrivilegedExceptionAction<CertStoreHelper>() {
                    public CertStoreHelper run() throws ClassNotFoundException {
                        try {
                            CertStoreHelper certStoreHelper = (CertStoreHelper) Class.forName(String.this, true, (ClassLoader) null).newInstance();
                            CertStoreHelper.cache.put(str, certStoreHelper);
                            return certStoreHelper;
                        } catch (IllegalAccessException | InstantiationException e) {
                            throw new AssertionError(e);
                        }
                    }
                });
            } catch (PrivilegedActionException e) {
                throw new NoSuchAlgorithmException(str + " not available", e.getException());
            }
        } else {
            throw new NoSuchAlgorithmException(str + " not available");
        }
    }

    static boolean isCausedByNetworkIssue(String str, CertStoreException certStoreException) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 84300:
                if (str.equals("URI")) {
                    c = 0;
                    break;
                }
                break;
            case 2331559:
                if (str.equals("LDAP")) {
                    c = 1;
                    break;
                }
                break;
            case 133315663:
                if (str.equals("SSLServer")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                Throwable cause = certStoreException.getCause();
                if (cause == null || !(cause instanceof IOException)) {
                    return false;
                }
                return true;
            case 1:
            case 2:
                try {
                    return getInstance(str).isCausedByNetworkIssue(certStoreException);
                } catch (NoSuchAlgorithmException unused) {
                    return false;
                }
            default:
                return false;
        }
    }
}
