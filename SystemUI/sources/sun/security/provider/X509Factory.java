package sun.security.provider;

import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import sun.security.util.Cache;
import sun.security.x509.X509CRLImpl;
import sun.security.x509.X509CertImpl;

public class X509Factory {
    private static final int ENC_MAX_LENGTH = 4194304;
    private static final Cache<Object, X509CertImpl> certCache = Cache.newSoftMemoryCache(750);
    private static final Cache<Object, X509CRLImpl> crlCache = Cache.newSoftMemoryCache(750);

    public static synchronized X509CertImpl intern(X509Certificate x509Certificate) throws CertificateException {
        byte[] bArr;
        X509CertImpl x509CertImpl;
        synchronized (X509Factory.class) {
            if (x509Certificate == null) {
                return null;
            }
            boolean z = x509Certificate instanceof X509CertImpl;
            if (z) {
                bArr = ((X509CertImpl) x509Certificate).getEncodedInternal();
            } else {
                bArr = x509Certificate.getEncoded();
            }
            Cache cache = certCache;
            X509CertImpl x509CertImpl2 = (X509CertImpl) getFromCache(cache, bArr);
            if (x509CertImpl2 != null) {
                return x509CertImpl2;
            }
            if (z) {
                x509CertImpl = (X509CertImpl) x509Certificate;
            } else {
                x509CertImpl = new X509CertImpl(bArr);
                bArr = x509CertImpl.getEncodedInternal();
            }
            addToCache(cache, bArr, x509CertImpl);
            return x509CertImpl;
        }
    }

    public static synchronized X509CRLImpl intern(X509CRL x509crl) throws CRLException {
        byte[] bArr;
        X509CRLImpl x509CRLImpl;
        synchronized (X509Factory.class) {
            if (x509crl == null) {
                return null;
            }
            boolean z = x509crl instanceof X509CRLImpl;
            if (z) {
                bArr = ((X509CRLImpl) x509crl).getEncodedInternal();
            } else {
                bArr = x509crl.getEncoded();
            }
            Cache cache = crlCache;
            X509CRLImpl x509CRLImpl2 = (X509CRLImpl) getFromCache(cache, bArr);
            if (x509CRLImpl2 != null) {
                return x509CRLImpl2;
            }
            if (z) {
                x509CRLImpl = (X509CRLImpl) x509crl;
            } else {
                x509CRLImpl = new X509CRLImpl(bArr);
                bArr = x509CRLImpl.getEncodedInternal();
            }
            addToCache(cache, bArr, x509CRLImpl);
            return x509CRLImpl;
        }
    }

    private static synchronized <K, V> V getFromCache(Cache<K, V> cache, byte[] bArr) {
        V v;
        synchronized (X509Factory.class) {
            v = cache.get(new Cache.EqualByteArray(bArr));
        }
        return v;
    }

    private static synchronized <V> void addToCache(Cache<Object, V> cache, byte[] bArr, V v) {
        synchronized (X509Factory.class) {
            if (bArr.length <= 4194304) {
                cache.put(new Cache.EqualByteArray(bArr), v);
            }
        }
    }
}
