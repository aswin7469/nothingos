package sun.security.provider.certpath;

import java.net.URI;
import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import sun.security.action.GetIntegerAction;
import sun.security.util.Cache;
import sun.security.util.Debug;
import sun.security.x509.AccessDescription;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.URIName;

class URICertStore extends CertStoreSpi {
    private static final int CACHE_SIZE = 185;
    private static final int CHECK_INTERVAL = 30000;
    private static final int CRL_CONNECT_TIMEOUT = initializeTimeout();
    private static final int DEFAULT_CRL_CONNECT_TIMEOUT = 15000;
    private static final Cache<URICertStoreParameters, CertStore> certStoreCache = Cache.newSoftMemoryCache(185);
    private static final Debug debug = Debug.getInstance("certpath");
    private Collection<X509Certificate> certs = Collections.emptySet();
    private X509CRL crl;
    private final CertificateFactory factory;
    private long lastChecked;
    private long lastModified;
    private boolean ldap = false;
    private CertStore ldapCertStore;
    private CertStoreHelper ldapHelper;
    private String ldapPath;
    private URI uri;

    private static int initializeTimeout() {
        Integer num = (Integer) AccessController.doPrivileged(new GetIntegerAction("com.sun.security.crl.timeout"));
        return (num == null || num.intValue() < 0) ? DEFAULT_CRL_CONNECT_TIMEOUT : num.intValue() * 1000;
    }

    URICertStore(CertStoreParameters certStoreParameters) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        super(certStoreParameters);
        if (certStoreParameters instanceof URICertStoreParameters) {
            URI r4 = ((URICertStoreParameters) certStoreParameters).uri;
            this.uri = r4;
            if (r4.getScheme().toLowerCase(Locale.ENGLISH).equals("ldap")) {
                this.ldap = true;
                CertStoreHelper instance = CertStoreHelper.getInstance("LDAP");
                this.ldapHelper = instance;
                this.ldapCertStore = instance.getCertStore(this.uri);
                String path = this.uri.getPath();
                this.ldapPath = path;
                if (path.charAt(0) == '/') {
                    this.ldapPath = this.ldapPath.substring(1);
                }
            }
            try {
                this.factory = CertificateFactory.getInstance("X.509");
            } catch (CertificateException unused) {
                throw new RuntimeException();
            }
        } else {
            throw new InvalidAlgorithmParameterException("params must be instanceof URICertStoreParameters");
        }
    }

    static synchronized CertStore getInstance(URICertStoreParameters uRICertStoreParameters) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        CertStore certStore;
        synchronized (URICertStore.class) {
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("CertStore URI:" + uRICertStoreParameters.uri);
            }
            Cache<URICertStoreParameters, CertStore> cache = certStoreCache;
            certStore = cache.get(uRICertStoreParameters);
            if (certStore == null) {
                certStore = new UCS(new URICertStore(uRICertStoreParameters), (Provider) null, "URI", uRICertStoreParameters);
                cache.put(uRICertStoreParameters, certStore);
            } else if (debug2 != null) {
                debug2.println("URICertStore.getInstance: cache hit");
            }
        }
        return certStore;
    }

    static CertStore getInstance(AccessDescription accessDescription) {
        if (!accessDescription.getAccessMethod().equals((Object) AccessDescription.Ad_CAISSUERS_Id)) {
            return null;
        }
        GeneralNameInterface name = accessDescription.getAccessLocation().getName();
        if (!(name instanceof URIName)) {
            return null;
        }
        try {
            return getInstance(new URICertStoreParameters(((URIName) name).getURI()));
        } catch (Exception e) {
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("exception creating CertStore: " + e);
                e.printStackTrace();
            }
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0081, code lost:
        return r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00a5, code lost:
        return r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.Collection<java.security.cert.X509Certificate> engineGetCertificates(java.security.cert.CertSelector r10) throws java.security.cert.CertStoreException {
        /*
            r9 = this;
            monitor-enter(r9)
            boolean r0 = r9.ldap     // Catch:{ all -> 0x00e7 }
            if (r0 == 0) goto L_0x0022
            java.security.cert.X509CertSelector r10 = (java.security.cert.X509CertSelector) r10     // Catch:{ all -> 0x00e7 }
            sun.security.provider.certpath.CertStoreHelper r0 = r9.ldapHelper     // Catch:{ IOException -> 0x001b }
            javax.security.auth.x500.X500Principal r1 = r10.getSubject()     // Catch:{ IOException -> 0x001b }
            java.lang.String r2 = r9.ldapPath     // Catch:{ IOException -> 0x001b }
            java.security.cert.X509CertSelector r10 = r0.wrap((java.security.cert.X509CertSelector) r10, (javax.security.auth.x500.X500Principal) r1, (java.lang.String) r2)     // Catch:{ IOException -> 0x001b }
            java.security.cert.CertStore r0 = r9.ldapCertStore     // Catch:{ all -> 0x00e7 }
            java.util.Collection r10 = r0.getCertificates(r10)     // Catch:{ all -> 0x00e7 }
            monitor-exit(r9)
            return r10
        L_0x001b:
            r10 = move-exception
            java.security.cert.CertStoreException r0 = new java.security.cert.CertStoreException     // Catch:{ all -> 0x00e7 }
            r0.<init>((java.lang.Throwable) r10)     // Catch:{ all -> 0x00e7 }
            throw r0     // Catch:{ all -> 0x00e7 }
        L_0x0022:
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00e7 }
            long r2 = r9.lastChecked     // Catch:{ all -> 0x00e7 }
            long r2 = r0 - r2
            r4 = 30000(0x7530, double:1.4822E-319)
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 >= 0) goto L_0x0041
            sun.security.util.Debug r0 = debug     // Catch:{ all -> 0x00e7 }
            if (r0 == 0) goto L_0x0039
            java.lang.String r1 = "Returning certificates from cache"
            r0.println(r1)     // Catch:{ all -> 0x00e7 }
        L_0x0039:
            java.util.Collection<java.security.cert.X509Certificate> r0 = r9.certs     // Catch:{ all -> 0x00e7 }
            java.util.Collection r10 = getMatchingCerts(r0, r10)     // Catch:{ all -> 0x00e7 }
            monitor-exit(r9)
            return r10
        L_0x0041:
            r9.lastChecked = r0     // Catch:{ all -> 0x00e7 }
            r0 = 0
            java.net.URI r2 = r9.uri     // Catch:{ IOException | CertificateException -> 0x00d0 }
            java.net.URL r2 = r2.toURL()     // Catch:{ IOException | CertificateException -> 0x00d0 }
            java.net.URLConnection r2 = r2.openConnection()     // Catch:{ IOException | CertificateException -> 0x00d0 }
            long r3 = r9.lastModified     // Catch:{ IOException | CertificateException -> 0x00d0 }
            int r5 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r5 == 0) goto L_0x0058
            r2.setIfModifiedSince(r3)     // Catch:{ IOException | CertificateException -> 0x00d0 }
        L_0x0058:
            long r3 = r9.lastModified     // Catch:{ IOException | CertificateException -> 0x00d0 }
            java.io.InputStream r5 = r2.getInputStream()     // Catch:{ IOException | CertificateException -> 0x00d0 }
            long r6 = r2.getLastModified()     // Catch:{ all -> 0x00c4 }
            r9.lastModified = r6     // Catch:{ all -> 0x00c4 }
            int r8 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r8 == 0) goto L_0x00a6
            int r3 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
            if (r3 != 0) goto L_0x0082
            sun.security.util.Debug r2 = debug     // Catch:{ all -> 0x00c4 }
            if (r2 == 0) goto L_0x0075
            java.lang.String r3 = "Not modified, using cached copy"
            r2.println(r3)     // Catch:{ all -> 0x00c4 }
        L_0x0075:
            java.util.Collection<java.security.cert.X509Certificate> r2 = r9.certs     // Catch:{ all -> 0x00c4 }
            java.util.Collection r10 = getMatchingCerts(r2, r10)     // Catch:{ all -> 0x00c4 }
            if (r5 == 0) goto L_0x0080
            r5.close()     // Catch:{ IOException | CertificateException -> 0x00d0 }
        L_0x0080:
            monitor-exit(r9)
            return r10
        L_0x0082:
            boolean r3 = r2 instanceof java.net.HttpURLConnection     // Catch:{ all -> 0x00c4 }
            if (r3 == 0) goto L_0x00a6
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch:{ all -> 0x00c4 }
            int r2 = r2.getResponseCode()     // Catch:{ all -> 0x00c4 }
            r3 = 304(0x130, float:4.26E-43)
            if (r2 != r3) goto L_0x00a6
            sun.security.util.Debug r2 = debug     // Catch:{ all -> 0x00c4 }
            if (r2 == 0) goto L_0x0099
            java.lang.String r3 = "Not modified, using cached copy"
            r2.println(r3)     // Catch:{ all -> 0x00c4 }
        L_0x0099:
            java.util.Collection<java.security.cert.X509Certificate> r2 = r9.certs     // Catch:{ all -> 0x00c4 }
            java.util.Collection r10 = getMatchingCerts(r2, r10)     // Catch:{ all -> 0x00c4 }
            if (r5 == 0) goto L_0x00a4
            r5.close()     // Catch:{ IOException | CertificateException -> 0x00d0 }
        L_0x00a4:
            monitor-exit(r9)
            return r10
        L_0x00a6:
            sun.security.util.Debug r2 = debug     // Catch:{ all -> 0x00c4 }
            if (r2 == 0) goto L_0x00af
            java.lang.String r3 = "Downloading new certificates..."
            r2.println(r3)     // Catch:{ all -> 0x00c4 }
        L_0x00af:
            java.security.cert.CertificateFactory r2 = r9.factory     // Catch:{ all -> 0x00c4 }
            java.util.Collection r2 = r2.generateCertificates(r5)     // Catch:{ all -> 0x00c4 }
            r9.certs = r2     // Catch:{ all -> 0x00c4 }
            if (r5 == 0) goto L_0x00bc
            r5.close()     // Catch:{ IOException | CertificateException -> 0x00d0 }
        L_0x00bc:
            java.util.Collection<java.security.cert.X509Certificate> r2 = r9.certs     // Catch:{ IOException | CertificateException -> 0x00d0 }
            java.util.Collection r10 = getMatchingCerts(r2, r10)     // Catch:{ IOException | CertificateException -> 0x00d0 }
            monitor-exit(r9)
            return r10
        L_0x00c4:
            r10 = move-exception
            if (r5 == 0) goto L_0x00cf
            r5.close()     // Catch:{ all -> 0x00cb }
            goto L_0x00cf
        L_0x00cb:
            r2 = move-exception
            r10.addSuppressed(r2)     // Catch:{ IOException | CertificateException -> 0x00d0 }
        L_0x00cf:
            throw r10     // Catch:{ IOException | CertificateException -> 0x00d0 }
        L_0x00d0:
            r10 = move-exception
            sun.security.util.Debug r2 = debug     // Catch:{ all -> 0x00e7 }
            if (r2 == 0) goto L_0x00dd
            java.lang.String r3 = "Exception fetching certificates:"
            r2.println(r3)     // Catch:{ all -> 0x00e7 }
            r10.printStackTrace()     // Catch:{ all -> 0x00e7 }
        L_0x00dd:
            r9.lastModified = r0     // Catch:{ all -> 0x00e7 }
            java.util.Set r10 = java.util.Collections.emptySet()     // Catch:{ all -> 0x00e7 }
            r9.certs = r10     // Catch:{ all -> 0x00e7 }
            monitor-exit(r9)
            return r10
        L_0x00e7:
            r10 = move-exception
            monitor-exit(r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.certpath.URICertStore.engineGetCertificates(java.security.cert.CertSelector):java.util.Collection");
    }

    private static Collection<X509Certificate> getMatchingCerts(Collection<X509Certificate> collection, CertSelector certSelector) {
        if (certSelector == null) {
            return collection;
        }
        ArrayList arrayList = new ArrayList(collection.size());
        for (X509Certificate next : collection) {
            if (certSelector.match(next)) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:47:0x008c, code lost:
        return r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00b0, code lost:
        return r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.Collection<java.security.cert.X509CRL> engineGetCRLs(java.security.cert.CRLSelector r11) throws java.security.cert.CertStoreException {
        /*
            r10 = this;
            monitor-enter(r10)
            boolean r0 = r10.ldap     // Catch:{ all -> 0x00fb }
            r1 = 0
            if (r0 == 0) goto L_0x0028
            java.security.cert.X509CRLSelector r11 = (java.security.cert.X509CRLSelector) r11     // Catch:{ all -> 0x00fb }
            sun.security.provider.certpath.CertStoreHelper r0 = r10.ldapHelper     // Catch:{ IOException -> 0x0021 }
            java.lang.String r2 = r10.ldapPath     // Catch:{ IOException -> 0x0021 }
            java.security.cert.X509CRLSelector r11 = r0.wrap((java.security.cert.X509CRLSelector) r11, (java.util.Collection<javax.security.auth.x500.X500Principal>) r1, (java.lang.String) r2)     // Catch:{ IOException -> 0x0021 }
            java.security.cert.CertStore r0 = r10.ldapCertStore     // Catch:{ CertStoreException -> 0x0018 }
            java.util.Collection r11 = r0.getCRLs(r11)     // Catch:{ CertStoreException -> 0x0018 }
            monitor-exit(r10)
            return r11
        L_0x0018:
            r11 = move-exception
            sun.security.provider.certpath.PKIX$CertStoreTypeException r0 = new sun.security.provider.certpath.PKIX$CertStoreTypeException     // Catch:{ all -> 0x00fb }
            java.lang.String r1 = "LDAP"
            r0.<init>(r1, r11)     // Catch:{ all -> 0x00fb }
            throw r0     // Catch:{ all -> 0x00fb }
        L_0x0021:
            r11 = move-exception
            java.security.cert.CertStoreException r0 = new java.security.cert.CertStoreException     // Catch:{ all -> 0x00fb }
            r0.<init>((java.lang.Throwable) r11)     // Catch:{ all -> 0x00fb }
            throw r0     // Catch:{ all -> 0x00fb }
        L_0x0028:
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00fb }
            long r4 = r10.lastChecked     // Catch:{ all -> 0x00fb }
            long r4 = r2 - r4
            r6 = 30000(0x7530, double:1.4822E-319)
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 >= 0) goto L_0x0047
            sun.security.util.Debug r0 = debug     // Catch:{ all -> 0x00fb }
            if (r0 == 0) goto L_0x003f
            java.lang.String r1 = "Returning CRL from cache"
            r0.println(r1)     // Catch:{ all -> 0x00fb }
        L_0x003f:
            java.security.cert.X509CRL r0 = r10.crl     // Catch:{ all -> 0x00fb }
            java.util.Collection r11 = getMatchingCRLs(r0, r11)     // Catch:{ all -> 0x00fb }
            monitor-exit(r10)
            return r11
        L_0x0047:
            r10.lastChecked = r2     // Catch:{ all -> 0x00fb }
            r2 = 0
            java.net.URI r0 = r10.uri     // Catch:{ IOException | CRLException -> 0x00dd }
            java.net.URL r0 = r0.toURL()     // Catch:{ IOException | CRLException -> 0x00dd }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ IOException | CRLException -> 0x00dd }
            long r4 = r10.lastModified     // Catch:{ IOException | CRLException -> 0x00dd }
            int r6 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r6 == 0) goto L_0x005e
            r0.setIfModifiedSince(r4)     // Catch:{ IOException | CRLException -> 0x00dd }
        L_0x005e:
            long r4 = r10.lastModified     // Catch:{ IOException | CRLException -> 0x00dd }
            int r6 = CRL_CONNECT_TIMEOUT     // Catch:{ IOException | CRLException -> 0x00dd }
            r0.setConnectTimeout(r6)     // Catch:{ IOException | CRLException -> 0x00dd }
            java.io.InputStream r6 = r0.getInputStream()     // Catch:{ IOException | CRLException -> 0x00dd }
            long r7 = r0.getLastModified()     // Catch:{ all -> 0x00d1 }
            r10.lastModified = r7     // Catch:{ all -> 0x00d1 }
            int r9 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r9 == 0) goto L_0x00b1
            int r4 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
            if (r4 != 0) goto L_0x008d
            sun.security.util.Debug r0 = debug     // Catch:{ all -> 0x00d1 }
            if (r0 == 0) goto L_0x0080
            java.lang.String r4 = "Not modified, using cached copy"
            r0.println(r4)     // Catch:{ all -> 0x00d1 }
        L_0x0080:
            java.security.cert.X509CRL r0 = r10.crl     // Catch:{ all -> 0x00d1 }
            java.util.Collection r11 = getMatchingCRLs(r0, r11)     // Catch:{ all -> 0x00d1 }
            if (r6 == 0) goto L_0x008b
            r6.close()     // Catch:{ IOException | CRLException -> 0x00dd }
        L_0x008b:
            monitor-exit(r10)
            return r11
        L_0x008d:
            boolean r4 = r0 instanceof java.net.HttpURLConnection     // Catch:{ all -> 0x00d1 }
            if (r4 == 0) goto L_0x00b1
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ all -> 0x00d1 }
            int r0 = r0.getResponseCode()     // Catch:{ all -> 0x00d1 }
            r4 = 304(0x130, float:4.26E-43)
            if (r0 != r4) goto L_0x00b1
            sun.security.util.Debug r0 = debug     // Catch:{ all -> 0x00d1 }
            if (r0 == 0) goto L_0x00a4
            java.lang.String r4 = "Not modified, using cached copy"
            r0.println(r4)     // Catch:{ all -> 0x00d1 }
        L_0x00a4:
            java.security.cert.X509CRL r0 = r10.crl     // Catch:{ all -> 0x00d1 }
            java.util.Collection r11 = getMatchingCRLs(r0, r11)     // Catch:{ all -> 0x00d1 }
            if (r6 == 0) goto L_0x00af
            r6.close()     // Catch:{ IOException | CRLException -> 0x00dd }
        L_0x00af:
            monitor-exit(r10)
            return r11
        L_0x00b1:
            sun.security.util.Debug r0 = debug     // Catch:{ all -> 0x00d1 }
            if (r0 == 0) goto L_0x00ba
            java.lang.String r4 = "Downloading new CRL..."
            r0.println(r4)     // Catch:{ all -> 0x00d1 }
        L_0x00ba:
            java.security.cert.CertificateFactory r0 = r10.factory     // Catch:{ all -> 0x00d1 }
            java.security.cert.CRL r0 = r0.generateCRL(r6)     // Catch:{ all -> 0x00d1 }
            java.security.cert.X509CRL r0 = (java.security.cert.X509CRL) r0     // Catch:{ all -> 0x00d1 }
            r10.crl = r0     // Catch:{ all -> 0x00d1 }
            if (r6 == 0) goto L_0x00c9
            r6.close()     // Catch:{ IOException | CRLException -> 0x00dd }
        L_0x00c9:
            java.security.cert.X509CRL r0 = r10.crl     // Catch:{ IOException | CRLException -> 0x00dd }
            java.util.Collection r11 = getMatchingCRLs(r0, r11)     // Catch:{ IOException | CRLException -> 0x00dd }
            monitor-exit(r10)
            return r11
        L_0x00d1:
            r11 = move-exception
            if (r6 == 0) goto L_0x00dc
            r6.close()     // Catch:{ all -> 0x00d8 }
            goto L_0x00dc
        L_0x00d8:
            r0 = move-exception
            r11.addSuppressed(r0)     // Catch:{ IOException | CRLException -> 0x00dd }
        L_0x00dc:
            throw r11     // Catch:{ IOException | CRLException -> 0x00dd }
        L_0x00dd:
            r11 = move-exception
            sun.security.util.Debug r0 = debug     // Catch:{ all -> 0x00fb }
            if (r0 == 0) goto L_0x00ea
            java.lang.String r4 = "Exception fetching CRL:"
            r0.println(r4)     // Catch:{ all -> 0x00fb }
            r11.printStackTrace()     // Catch:{ all -> 0x00fb }
        L_0x00ea:
            r10.lastModified = r2     // Catch:{ all -> 0x00fb }
            r10.crl = r1     // Catch:{ all -> 0x00fb }
            sun.security.provider.certpath.PKIX$CertStoreTypeException r0 = new sun.security.provider.certpath.PKIX$CertStoreTypeException     // Catch:{ all -> 0x00fb }
            java.lang.String r1 = "URI"
            java.security.cert.CertStoreException r2 = new java.security.cert.CertStoreException     // Catch:{ all -> 0x00fb }
            r2.<init>((java.lang.Throwable) r11)     // Catch:{ all -> 0x00fb }
            r0.<init>(r1, r2)     // Catch:{ all -> 0x00fb }
            throw r0     // Catch:{ all -> 0x00fb }
        L_0x00fb:
            r11 = move-exception
            monitor-exit(r10)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.certpath.URICertStore.engineGetCRLs(java.security.cert.CRLSelector):java.util.Collection");
    }

    private static Collection<X509CRL> getMatchingCRLs(X509CRL x509crl, CRLSelector cRLSelector) {
        if (cRLSelector == null || (x509crl != null && cRLSelector.match(x509crl))) {
            return Collections.singletonList(x509crl);
        }
        return Collections.emptyList();
    }

    static class URICertStoreParameters implements CertStoreParameters {
        private volatile int hashCode = 0;
        /* access modifiers changed from: private */
        public final URI uri;

        URICertStoreParameters(URI uri2) {
            this.uri = uri2;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof URICertStoreParameters)) {
                return false;
            }
            return this.uri.equals(((URICertStoreParameters) obj).uri);
        }

        public int hashCode() {
            if (this.hashCode == 0) {
                this.hashCode = 629 + this.uri.hashCode();
            }
            return this.hashCode;
        }

        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                throw new InternalError(e.toString(), e);
            }
        }
    }

    private static class UCS extends CertStore {
        protected UCS(CertStoreSpi certStoreSpi, Provider provider, String str, CertStoreParameters certStoreParameters) {
            super(certStoreSpi, provider, str, certStoreParameters);
        }
    }
}
