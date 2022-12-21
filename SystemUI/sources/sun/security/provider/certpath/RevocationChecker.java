package sun.security.provider.certpath;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CRLReason;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.Extension;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.provider.certpath.OCSP;
import sun.security.provider.certpath.OCSPResponse;
import sun.security.provider.certpath.PKIX;
import sun.security.util.Debug;
import sun.security.x509.AccessDescription;
import sun.security.x509.AuthorityInfoAccessExtension;
import sun.security.x509.CRLDistributionPointsExtension;
import sun.security.x509.DistributionPoint;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.GeneralNames;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.X500Name;
import sun.security.x509.X509CRLEntryImpl;
import sun.security.x509.X509CertImpl;

class RevocationChecker extends PKIXRevocationChecker {
    private static final boolean[] ALL_REASONS = {true, true, true, true, true, true, true, true, true};
    private static final boolean[] CRL_SIGN_USAGE = {false, false, false, false, false, false, true};
    private static final String HEX_DIGITS = "0123456789ABCDEFabcdef";
    private static final long MAX_CLOCK_SKEW = 900000;
    /* access modifiers changed from: private */
    public static final Debug debug = Debug.getInstance("certpath");
    private TrustAnchor anchor;
    private int certIndex;
    private List<CertStore> certStores;
    private boolean crlDP;
    private boolean crlSignFlag;
    private X509Certificate issuerCert;
    private boolean legacy = false;
    private Mode mode = Mode.PREFER_OCSP;
    private List<Extension> ocspExtensions;
    private Map<X509Certificate, byte[]> ocspResponses;
    private boolean onlyEE;
    private PKIX.ValidatorParams params;
    private PublicKey prevPubKey;
    private X509Certificate responderCert;
    private URI responderURI;
    private boolean softFail;
    private LinkedList<CertPathValidatorException> softFailExceptions = new LinkedList<>();

    private enum Mode {
        PREFER_OCSP,
        PREFER_CRLS,
        ONLY_CRLS,
        ONLY_OCSP
    }

    public Set<String> getSupportedExtensions() {
        return null;
    }

    public boolean isForwardCheckingSupported() {
        return false;
    }

    private static class RevocationProperties {
        boolean crlDPEnabled;
        boolean ocspEnabled;
        String ocspIssuer;
        String ocspSerial;
        String ocspSubject;
        String ocspUrl;
        boolean onlyEE;

        private RevocationProperties() {
        }
    }

    RevocationChecker() {
    }

    RevocationChecker(TrustAnchor trustAnchor, PKIX.ValidatorParams validatorParams) throws CertPathValidatorException {
        init(trustAnchor, validatorParams);
    }

    /* access modifiers changed from: package-private */
    public void init(TrustAnchor trustAnchor, PKIX.ValidatorParams validatorParams) throws CertPathValidatorException {
        RevocationProperties revocationProperties = getRevocationProperties();
        URI ocspResponder = getOcspResponder();
        if (ocspResponder == null) {
            ocspResponder = toURI(revocationProperties.ocspUrl);
        }
        this.responderURI = ocspResponder;
        X509Certificate ocspResponderCert = getOcspResponderCert();
        if (ocspResponderCert == null) {
            ocspResponderCert = getResponderCert(revocationProperties, validatorParams.trustAnchors(), validatorParams.certStores());
        }
        this.responderCert = ocspResponderCert;
        Set<PKIXRevocationChecker.Option> options = getOptions();
        for (PKIXRevocationChecker.Option next : options) {
            int i = C48112.$SwitchMap$java$security$cert$PKIXRevocationChecker$Option[next.ordinal()];
            if (i != 1 && i != 2 && i != 3 && i != 4) {
                throw new CertPathValidatorException("Unrecognized revocation parameter option: " + next);
            }
        }
        this.softFail = options.contains(PKIXRevocationChecker.Option.SOFT_FAIL);
        if (this.legacy) {
            this.mode = revocationProperties.ocspEnabled ? Mode.PREFER_OCSP : Mode.ONLY_CRLS;
            this.onlyEE = revocationProperties.onlyEE;
        } else {
            if (options.contains(PKIXRevocationChecker.Option.NO_FALLBACK)) {
                if (options.contains(PKIXRevocationChecker.Option.PREFER_CRLS)) {
                    this.mode = Mode.ONLY_CRLS;
                } else {
                    this.mode = Mode.ONLY_OCSP;
                }
            } else if (options.contains(PKIXRevocationChecker.Option.PREFER_CRLS)) {
                this.mode = Mode.PREFER_CRLS;
            }
            this.onlyEE = options.contains(PKIXRevocationChecker.Option.ONLY_END_ENTITY);
        }
        if (this.legacy) {
            this.crlDP = revocationProperties.crlDPEnabled;
        } else {
            this.crlDP = true;
        }
        this.ocspResponses = getOcspResponses();
        this.ocspExtensions = getOcspExtensions();
        this.anchor = trustAnchor;
        this.params = validatorParams;
        ArrayList arrayList = new ArrayList(validatorParams.certStores());
        this.certStores = arrayList;
        try {
            arrayList.add(CertStore.getInstance("Collection", new CollectionCertStoreParameters(validatorParams.certificates())));
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("RevocationChecker: error creating Collection CertStore: " + e);
            }
        }
    }

    private static URI toURI(String str) throws CertPathValidatorException {
        if (str == null) {
            return null;
        }
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            throw new CertPathValidatorException("cannot parse ocsp.responderURL property", e);
        }
    }

    private static RevocationProperties getRevocationProperties() {
        return (RevocationProperties) AccessController.doPrivileged(new PrivilegedAction<RevocationProperties>() {
            public RevocationProperties run() {
                RevocationProperties revocationProperties = new RevocationProperties();
                String property = Security.getProperty("com.sun.security.onlyCheckRevocationOfEECert");
                boolean z = true;
                revocationProperties.onlyEE = property != null && property.equalsIgnoreCase("true");
                String property2 = Security.getProperty("ocsp.enable");
                if (property2 == null || !property2.equalsIgnoreCase("true")) {
                    z = false;
                }
                revocationProperties.ocspEnabled = z;
                revocationProperties.ocspUrl = Security.getProperty("ocsp.responderURL");
                revocationProperties.ocspSubject = Security.getProperty("ocsp.responderCertSubjectName");
                revocationProperties.ocspIssuer = Security.getProperty("ocsp.responderCertIssuerName");
                revocationProperties.ocspSerial = Security.getProperty("ocsp.responderCertSerialNumber");
                revocationProperties.crlDPEnabled = Boolean.getBoolean("com.sun.security.enableCRLDP");
                return revocationProperties;
            }
        });
    }

    private static X509Certificate getResponderCert(RevocationProperties revocationProperties, Set<TrustAnchor> set, List<CertStore> list) throws CertPathValidatorException {
        if (revocationProperties.ocspSubject != null) {
            return getResponderCert(revocationProperties.ocspSubject, set, list);
        }
        if (revocationProperties.ocspIssuer != null && revocationProperties.ocspSerial != null) {
            return getResponderCert(revocationProperties.ocspIssuer, revocationProperties.ocspSerial, set, list);
        }
        if (revocationProperties.ocspIssuer == null && revocationProperties.ocspSerial == null) {
            return null;
        }
        throw new CertPathValidatorException("Must specify both ocsp.responderCertIssuerName and ocsp.responderCertSerialNumber properties");
    }

    private static X509Certificate getResponderCert(String str, Set<TrustAnchor> set, List<CertStore> list) throws CertPathValidatorException {
        X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(new X500Principal(str));
            return getResponderCert(x509CertSelector, set, list);
        } catch (IllegalArgumentException e) {
            throw new CertPathValidatorException("cannot parse ocsp.responderCertSubjectName property", e);
        }
    }

    private static X509Certificate getResponderCert(String str, String str2, Set<TrustAnchor> set, List<CertStore> list) throws CertPathValidatorException {
        X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setIssuer(new X500Principal(str));
            try {
                x509CertSelector.setSerialNumber(new BigInteger(stripOutSeparators(str2), 16));
                return getResponderCert(x509CertSelector, set, list);
            } catch (NumberFormatException e) {
                throw new CertPathValidatorException("cannot parse ocsp.responderCertSerialNumber property", e);
            }
        } catch (IllegalArgumentException e2) {
            throw new CertPathValidatorException("cannot parse ocsp.responderCertIssuerName property", e2);
        }
    }

    private static X509Certificate getResponderCert(X509CertSelector x509CertSelector, Set<TrustAnchor> set, List<CertStore> list) throws CertPathValidatorException {
        for (TrustAnchor trustedCert : set) {
            X509Certificate trustedCert2 = trustedCert.getTrustedCert();
            if (trustedCert2 != null && x509CertSelector.match(trustedCert2)) {
                return trustedCert2;
            }
        }
        for (CertStore certificates : list) {
            try {
                Collection<? extends Certificate> certificates2 = certificates.getCertificates(x509CertSelector);
                if (!certificates2.isEmpty()) {
                    return (X509Certificate) certificates2.iterator().next();
                }
            } catch (CertStoreException e) {
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("CertStore exception:" + e);
                }
            }
        }
        throw new CertPathValidatorException("Cannot find the responder's certificate (set using the OCSP security properties).");
    }

    public void init(boolean z) throws CertPathValidatorException {
        PublicKey publicKey;
        if (!z) {
            TrustAnchor trustAnchor = this.anchor;
            if (trustAnchor != null) {
                X509Certificate trustedCert = trustAnchor.getTrustedCert();
                this.issuerCert = trustedCert;
                if (trustedCert != null) {
                    publicKey = trustedCert.getPublicKey();
                } else {
                    publicKey = this.anchor.getCAPublicKey();
                }
                this.prevPubKey = publicKey;
            }
            this.crlSignFlag = true;
            PKIX.ValidatorParams validatorParams = this.params;
            if (validatorParams == null || validatorParams.certPath() == null) {
                this.certIndex = -1;
            } else {
                this.certIndex = this.params.certPath().getCertificates().size() - 1;
            }
            this.softFailExceptions.clear();
            return;
        }
        throw new CertPathValidatorException("forward checking not supported");
    }

    public List<CertPathValidatorException> getSoftFailExceptions() {
        return Collections.unmodifiableList(this.softFailExceptions);
    }

    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        check((X509Certificate) certificate, collection, this.prevPubKey, this.crlSignFlag);
    }

    private void check(X509Certificate x509Certificate, Collection<String> collection, PublicKey publicKey, boolean z) throws CertPathValidatorException {
        CertPathValidatorException certPathValidatorException;
        boolean isSoftFailException;
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("RevocationChecker.check: checking cert\n  SN: " + Debug.toHexString(x509Certificate.getSerialNumber()) + "\n  Subject: " + x509Certificate.getSubjectX500Principal() + "\n  Issuer: " + x509Certificate.getIssuerX500Principal());
        }
        try {
            if (!this.onlyEE || x509Certificate.getBasicConstraints() == -1) {
                int i = C48112.$SwitchMap$sun$security$provider$certpath$RevocationChecker$Mode[this.mode.ordinal()];
                if (i == 1 || i == 2) {
                    checkOCSP(x509Certificate, collection);
                    updateState(x509Certificate);
                    return;
                }
                if (i == 3 || i == 4) {
                    checkCRLs(x509Certificate, collection, (Set<X509Certificate>) null, publicKey, z);
                }
                updateState(x509Certificate);
                return;
            }
            if (debug2 != null) {
                debug2.println("Skipping revocation check; cert is not an end entity cert");
            }
            updateState(x509Certificate);
        } catch (CertPathValidatorException e) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("RevocationChecker.check() failover failed");
                debug3.println("RevocationChecker.check() " + e.getMessage());
            }
            if (e.getReason() == CertPathValidatorException.BasicReason.REVOKED) {
                throw e;
            } else if (!isSoftFailException(e)) {
                certPathValidatorException.addSuppressed(e);
                throw certPathValidatorException;
            } else if (!isSoftFailException) {
                throw certPathValidatorException;
            }
        } catch (CertPathValidatorException e2) {
            certPathValidatorException = e2;
            if (certPathValidatorException.getReason() != CertPathValidatorException.BasicReason.REVOKED) {
                isSoftFailException = isSoftFailException(certPathValidatorException);
                if (isSoftFailException) {
                    if (this.mode == Mode.ONLY_OCSP || this.mode == Mode.ONLY_CRLS) {
                        updateState(x509Certificate);
                        return;
                    }
                } else if (this.mode == Mode.ONLY_OCSP || this.mode == Mode.ONLY_CRLS) {
                    throw certPathValidatorException;
                }
                Debug debug4 = debug;
                if (debug4 != null) {
                    debug4.println("RevocationChecker.check() " + certPathValidatorException.getMessage());
                    debug4.println("RevocationChecker.check() preparing to failover");
                }
                int i2 = C48112.$SwitchMap$sun$security$provider$certpath$RevocationChecker$Mode[this.mode.ordinal()];
                if (i2 == 1) {
                    checkCRLs(x509Certificate, collection, (Set<X509Certificate>) null, publicKey, z);
                } else if (i2 == 3) {
                    checkOCSP(x509Certificate, collection);
                }
            } else {
                throw certPathValidatorException;
            }
        } catch (Throwable th) {
            updateState(x509Certificate);
            throw th;
        }
    }

    /* renamed from: sun.security.provider.certpath.RevocationChecker$2 */
    static /* synthetic */ class C48112 {
        static final /* synthetic */ int[] $SwitchMap$java$security$cert$PKIXRevocationChecker$Option;
        static final /* synthetic */ int[] $SwitchMap$sun$security$provider$certpath$RevocationChecker$Mode;

        /* JADX WARNING: Can't wrap try/catch for region: R(17:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|17|18|19|20|21|22|(3:23|24|26)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(21:0|1|2|3|(2:5|6)|7|9|10|11|13|14|15|17|18|19|20|21|22|23|24|26) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0044 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x004e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0058 */
        static {
            /*
                sun.security.provider.certpath.RevocationChecker$Mode[] r0 = sun.security.provider.certpath.RevocationChecker.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$sun$security$provider$certpath$RevocationChecker$Mode = r0
                r1 = 1
                sun.security.provider.certpath.RevocationChecker$Mode r2 = sun.security.provider.certpath.RevocationChecker.Mode.PREFER_OCSP     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$sun$security$provider$certpath$RevocationChecker$Mode     // Catch:{ NoSuchFieldError -> 0x001d }
                sun.security.provider.certpath.RevocationChecker$Mode r3 = sun.security.provider.certpath.RevocationChecker.Mode.ONLY_OCSP     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = $SwitchMap$sun$security$provider$certpath$RevocationChecker$Mode     // Catch:{ NoSuchFieldError -> 0x0028 }
                sun.security.provider.certpath.RevocationChecker$Mode r4 = sun.security.provider.certpath.RevocationChecker.Mode.PREFER_CRLS     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                r3 = 4
                int[] r4 = $SwitchMap$sun$security$provider$certpath$RevocationChecker$Mode     // Catch:{ NoSuchFieldError -> 0x0033 }
                sun.security.provider.certpath.RevocationChecker$Mode r5 = sun.security.provider.certpath.RevocationChecker.Mode.ONLY_CRLS     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                java.security.cert.PKIXRevocationChecker$Option[] r4 = java.security.cert.PKIXRevocationChecker.Option.values()
                int r4 = r4.length
                int[] r4 = new int[r4]
                $SwitchMap$java$security$cert$PKIXRevocationChecker$Option = r4
                java.security.cert.PKIXRevocationChecker$Option r5 = java.security.cert.PKIXRevocationChecker.Option.ONLY_END_ENTITY     // Catch:{ NoSuchFieldError -> 0x0044 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0044 }
                r4[r5] = r1     // Catch:{ NoSuchFieldError -> 0x0044 }
            L_0x0044:
                int[] r1 = $SwitchMap$java$security$cert$PKIXRevocationChecker$Option     // Catch:{ NoSuchFieldError -> 0x004e }
                java.security.cert.PKIXRevocationChecker$Option r4 = java.security.cert.PKIXRevocationChecker.Option.PREFER_CRLS     // Catch:{ NoSuchFieldError -> 0x004e }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x004e }
                r1[r4] = r0     // Catch:{ NoSuchFieldError -> 0x004e }
            L_0x004e:
                int[] r0 = $SwitchMap$java$security$cert$PKIXRevocationChecker$Option     // Catch:{ NoSuchFieldError -> 0x0058 }
                java.security.cert.PKIXRevocationChecker$Option r1 = java.security.cert.PKIXRevocationChecker.Option.SOFT_FAIL     // Catch:{ NoSuchFieldError -> 0x0058 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0058 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0058 }
            L_0x0058:
                int[] r0 = $SwitchMap$java$security$cert$PKIXRevocationChecker$Option     // Catch:{ NoSuchFieldError -> 0x0062 }
                java.security.cert.PKIXRevocationChecker$Option r1 = java.security.cert.PKIXRevocationChecker.Option.NO_FALLBACK     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.certpath.RevocationChecker.C48112.<clinit>():void");
        }
    }

    private boolean isSoftFailException(CertPathValidatorException certPathValidatorException) {
        if (!this.softFail || certPathValidatorException.getReason() != CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS) {
            return false;
        }
        this.softFailExceptions.addFirst(new CertPathValidatorException(certPathValidatorException.getMessage(), certPathValidatorException.getCause(), this.params.certPath(), this.certIndex, certPathValidatorException.getReason()));
        return true;
    }

    private void updateState(X509Certificate x509Certificate) throws CertPathValidatorException {
        this.issuerCert = x509Certificate;
        PublicKey publicKey = x509Certificate.getPublicKey();
        if (PKIX.isDSAPublicKeyWithoutParams(publicKey)) {
            publicKey = BasicChecker.makeInheritedParamsKey(publicKey, this.prevPubKey);
        }
        this.prevPubKey = publicKey;
        this.crlSignFlag = certCanSignCrl(x509Certificate);
        int i = this.certIndex;
        if (i > 0) {
            this.certIndex = i - 1;
        }
    }

    private void checkCRLs(X509Certificate x509Certificate, Collection<String> collection, Set<X509Certificate> set, PublicKey publicKey, boolean z) throws CertPathValidatorException {
        checkCRLs(x509Certificate, publicKey, (X509Certificate) null, z, true, set, this.params.trustAnchors());
    }

    private void checkCRLs(X509Certificate x509Certificate, PublicKey publicKey, X509Certificate x509Certificate2, boolean z, boolean z2, Set<X509Certificate> set, Set<TrustAnchor> set2) throws CertPathValidatorException {
        CertPathValidatorException certPathValidatorException;
        boolean[] zArr;
        X509Certificate x509Certificate3 = x509Certificate;
        Set<X509Certificate> set3 = set;
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("RevocationChecker.checkCRLs() ---checking revocation status ...");
        }
        if (set3 == null || !set3.contains(x509Certificate3)) {
            HashSet hashSet = new HashSet();
            HashSet hashSet2 = new HashSet();
            X509CRLSelector x509CRLSelector = new X509CRLSelector();
            x509CRLSelector.setCertificateChecking(x509Certificate3);
            CertPathHelper.setDateAndTime(x509CRLSelector, this.params.date(), MAX_CLOCK_SKEW);
            CertPathValidatorException certPathValidatorException2 = null;
            loop0:
            while (true) {
                certPathValidatorException = certPathValidatorException2;
                for (CertStore next : this.certStores) {
                    try {
                        for (CRL crl : next.getCRLs(x509CRLSelector)) {
                            hashSet.add((X509CRL) crl);
                        }
                    } catch (CertStoreException e) {
                        CertStoreException certStoreException = e;
                        Debug debug3 = debug;
                        if (debug3 != null) {
                            debug3.println("RevocationChecker.checkCRLs() CertStoreException: " + certStoreException.getMessage());
                        }
                        if (certPathValidatorException == null && CertStoreHelper.isCausedByNetworkIssue(next.getType(), certStoreException)) {
                            certPathValidatorException2 = new CertPathValidatorException("Unable to determine revocation status due to network error", certStoreException, (CertPath) null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
                        }
                    }
                }
                break loop0;
            }
            Debug debug4 = debug;
            if (debug4 != null) {
                debug4.println("RevocationChecker.checkCRLs() possible crls.size() = " + hashSet.size());
            }
            boolean[] zArr2 = new boolean[9];
            if (!hashSet.isEmpty()) {
                hashSet2.addAll(verifyPossibleCRLs(hashSet, x509Certificate, publicKey, z, zArr2, set2));
            }
            if (debug4 != null) {
                debug4.println("RevocationChecker.checkCRLs() approved crls.size() = " + hashSet2.size());
            }
            if (hashSet2.isEmpty() || !Arrays.equals(zArr2, ALL_REASONS)) {
                try {
                    if (this.crlDP) {
                        zArr = zArr2;
                        hashSet2.addAll(DistributionPointFetcher.getCRLs(x509CRLSelector, z, publicKey, x509Certificate2, this.params.sigProvider(), this.certStores, zArr, set2, (Date) null));
                    } else {
                        zArr = zArr2;
                    }
                    if (!hashSet2.isEmpty() && Arrays.equals(zArr, ALL_REASONS)) {
                        checkApprovedCRLs(x509Certificate3, hashSet2);
                    } else if (z2) {
                        try {
                            verifyWithSeparateSigningKey(x509Certificate3, publicKey, z, set3);
                        } catch (CertPathValidatorException e2) {
                            CertPathValidatorException certPathValidatorException3 = e2;
                            if (certPathValidatorException != null) {
                                throw certPathValidatorException;
                            }
                            throw certPathValidatorException3;
                        }
                    } else if (certPathValidatorException != null) {
                        throw certPathValidatorException;
                    } else {
                        throw new CertPathValidatorException("Could not determine revocation status", (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
                    }
                } catch (CertStoreException e3) {
                    if (!(e3 instanceof PKIX.CertStoreTypeException) || !CertStoreHelper.isCausedByNetworkIssue(((PKIX.CertStoreTypeException) e3).getType(), e3)) {
                        throw new CertPathValidatorException((Throwable) e3);
                    }
                    throw new CertPathValidatorException("Unable to determine revocation status due to network error", e3, (CertPath) null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
                }
            } else {
                checkApprovedCRLs(x509Certificate3, hashSet2);
            }
        } else {
            if (debug2 != null) {
                debug2.println("RevocationChecker.checkCRLs() circular dependency");
            }
            throw new CertPathValidatorException("Could not determine revocation status", (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
        }
    }

    private void checkApprovedCRLs(X509Certificate x509Certificate, Set<X509CRL> set) throws CertPathValidatorException {
        Debug debug2 = debug;
        if (debug2 != null) {
            BigInteger serialNumber = x509Certificate.getSerialNumber();
            debug2.println("RevocationChecker.checkApprovedCRLs() starting the final sweep...");
            debug2.println("RevocationChecker.checkApprovedCRLs() cert SN: " + serialNumber.toString());
        }
        CRLReason cRLReason = CRLReason.UNSPECIFIED;
        for (X509CRL next : set) {
            X509CRLEntry revokedCertificate = next.getRevokedCertificate(x509Certificate);
            if (revokedCertificate != null) {
                try {
                    X509CRLEntryImpl impl = X509CRLEntryImpl.toImpl(revokedCertificate);
                    Debug debug3 = debug;
                    if (debug3 != null) {
                        debug3.println("RevocationChecker.checkApprovedCRLs() CRL entry: " + impl.toString());
                    }
                    Set<String> criticalExtensionOIDs = impl.getCriticalExtensionOIDs();
                    if (criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty()) {
                        criticalExtensionOIDs.remove(PKIXExtensions.ReasonCode_Id.toString());
                        criticalExtensionOIDs.remove(PKIXExtensions.CertificateIssuer_Id.toString());
                        if (!criticalExtensionOIDs.isEmpty()) {
                            throw new CertPathValidatorException("Unrecognized critical extension(s) in revoked CRL entry");
                        }
                    }
                    CRLReason revocationReason = impl.getRevocationReason();
                    if (revocationReason == null) {
                        revocationReason = CRLReason.UNSPECIFIED;
                    }
                    Date revocationDate = impl.getRevocationDate();
                    if (revocationDate.before(this.params.date())) {
                        CertificateRevokedException certificateRevokedException = new CertificateRevokedException(revocationDate, revocationReason, next.getIssuerX500Principal(), impl.getExtensions());
                        throw new CertPathValidatorException(certificateRevokedException.getMessage(), certificateRevokedException, (CertPath) null, -1, CertPathValidatorException.BasicReason.REVOKED);
                    }
                } catch (CRLException e) {
                    throw new CertPathValidatorException((Throwable) e);
                }
            }
        }
    }

    private void checkOCSP(X509Certificate x509Certificate, Collection<String> collection) throws CertPathValidatorException {
        CertId certId;
        OCSPResponse oCSPResponse;
        byte[] bArr;
        try {
            X509CertImpl impl = X509CertImpl.toImpl(x509Certificate);
            try {
                if (this.issuerCert != null) {
                    certId = new CertId(this.issuerCert, impl.getSerialNumberObject());
                } else {
                    certId = new CertId(this.anchor.getCA(), this.anchor.getCAPublicKey(), impl.getSerialNumberObject());
                }
                byte[] bArr2 = this.ocspResponses.get(x509Certificate);
                byte[] bArr3 = null;
                if (bArr2 != null) {
                    Debug debug2 = debug;
                    if (debug2 != null) {
                        debug2.println("Found cached OCSP response");
                    }
                    oCSPResponse = new OCSPResponse(bArr2);
                    loop0:
                    while (true) {
                        bArr = bArr3;
                        for (Extension next : this.ocspExtensions) {
                            if (next.getId().equals("1.3.6.1.5.5.7.48.1.2")) {
                                bArr3 = next.getValue();
                            }
                        }
                        break loop0;
                    }
                    oCSPResponse.verify(Collections.singletonList(certId), this.issuerCert, this.responderCert, this.params.date(), bArr);
                } else {
                    URI uri = this.responderURI;
                    if (uri == null) {
                        uri = OCSP.getResponderURI(impl);
                    }
                    URI uri2 = uri;
                    if (uri2 != null) {
                        oCSPResponse = OCSP.check((List<CertId>) Collections.singletonList(certId), uri2, this.issuerCert, this.responderCert, (Date) null, this.ocspExtensions);
                    } else {
                        throw new CertPathValidatorException("Certificate does not specify OCSP responder", (Throwable) null, (CertPath) null, -1);
                    }
                }
                OCSPResponse.SingleResponse singleResponse = oCSPResponse.getSingleResponse(certId);
                OCSP.RevocationStatus.CertStatus certStatus = singleResponse.getCertStatus();
                if (certStatus == OCSP.RevocationStatus.CertStatus.REVOKED) {
                    Date revocationTime = singleResponse.getRevocationTime();
                    if (revocationTime.before(this.params.date())) {
                        CertificateRevokedException certificateRevokedException = new CertificateRevokedException(revocationTime, singleResponse.getRevocationReason(), oCSPResponse.getSignerCertificate().getSubjectX500Principal(), singleResponse.getSingleExtensions());
                        throw new CertPathValidatorException(certificateRevokedException.getMessage(), certificateRevokedException, (CertPath) null, -1, CertPathValidatorException.BasicReason.REVOKED);
                    }
                } else if (certStatus == OCSP.RevocationStatus.CertStatus.UNKNOWN) {
                    throw new CertPathValidatorException("Certificate's revocation status is unknown", (Throwable) null, this.params.certPath(), -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
                }
            } catch (IOException e) {
                throw new CertPathValidatorException("Unable to determine revocation status due to network error", e, (CertPath) null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
            }
        } catch (CertificateException e2) {
            throw new CertPathValidatorException((Throwable) e2);
        }
    }

    private static String stripOutSeparators(String str) {
        char[] charArray = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            if (HEX_DIGITS.indexOf((int) charArray[i]) != -1) {
                sb.append(charArray[i]);
            }
        }
        return sb.toString();
    }

    static boolean certCanSignCrl(X509Certificate x509Certificate) {
        boolean[] keyUsage = x509Certificate.getKeyUsage();
        if (keyUsage != null) {
            return keyUsage[6];
        }
        return false;
    }

    private Collection<X509CRL> verifyPossibleCRLs(Set<X509CRL> set, X509Certificate x509Certificate, PublicKey publicKey, boolean z, boolean[] zArr, Set<TrustAnchor> set2) throws CertPathValidatorException {
        List<DistributionPoint> list;
        RevocationChecker revocationChecker = this;
        try {
            X509CertImpl impl = X509CertImpl.toImpl(x509Certificate);
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("RevocationChecker.verifyPossibleCRLs: Checking CRLDPs for " + impl.getSubjectX500Principal());
            }
            CRLDistributionPointsExtension cRLDistributionPointsExtension = impl.getCRLDistributionPointsExtension();
            if (cRLDistributionPointsExtension == null) {
                list = Collections.singletonList(new DistributionPoint(new GeneralNames().add(new GeneralName((GeneralNameInterface) (X500Name) impl.getIssuerDN())), (boolean[]) null, (GeneralNames) null));
            } else {
                list = cRLDistributionPointsExtension.get(CRLDistributionPointsExtension.POINTS);
            }
            HashSet hashSet = new HashSet();
            for (DistributionPoint distributionPoint : list) {
                for (X509CRL next : set) {
                    String sigProvider = revocationChecker.params.sigProvider();
                    List<CertStore> list2 = revocationChecker.certStores;
                    Date date = revocationChecker.params.date();
                    X509CRL x509crl = next;
                    if (DistributionPointFetcher.verifyCRL(impl, distributionPoint, next, zArr, z, publicKey, (X509Certificate) null, sigProvider, set2, list2, date)) {
                        hashSet.add(x509crl);
                    }
                    revocationChecker = this;
                }
                if (Arrays.equals(zArr, ALL_REASONS)) {
                    break;
                }
                revocationChecker = this;
            }
            return hashSet;
        } catch (IOException | CRLException | CertificateException e) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("Exception while verifying CRL: " + e.getMessage());
                e.printStackTrace();
            }
            return Collections.emptySet();
        }
    }

    private void verifyWithSeparateSigningKey(X509Certificate x509Certificate, PublicKey publicKey, boolean z, Set<X509Certificate> set) throws CertPathValidatorException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("RevocationChecker.verifyWithSeparateSigningKey() ---checking revocation status...");
        }
        if (set != null && set.contains(x509Certificate)) {
            if (debug2 != null) {
                debug2.println("RevocationChecker.verifyWithSeparateSigningKey() circular dependency");
            }
            throw new CertPathValidatorException("Could not determine revocation status", (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
        } else if (!z) {
            buildToNewKey(x509Certificate, (PublicKey) null, set);
        } else {
            buildToNewKey(x509Certificate, publicKey, set);
        }
    }

    private void buildToNewKey(X509Certificate x509Certificate, PublicKey publicKey, Set<X509Certificate> set) throws CertPathValidatorException {
        Set<TrustAnchor> set2;
        CertPathBuilder certPathBuilder;
        Set<X509Certificate> set3;
        PublicKey publicKey2;
        X509Certificate x509Certificate2;
        X509CertImpl x509CertImpl;
        List<AccessDescription> accessDescriptions;
        PublicKey publicKey3 = publicKey;
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("RevocationChecker.buildToNewKey() starting work");
        }
        HashSet hashSet = new HashSet();
        if (publicKey3 != null) {
            hashSet.add(publicKey3);
        }
        RejectKeySelector rejectKeySelector = new RejectKeySelector(hashSet);
        rejectKeySelector.setSubject(x509Certificate.getIssuerX500Principal());
        rejectKeySelector.setKeyUsage(CRL_SIGN_USAGE);
        TrustAnchor trustAnchor = this.anchor;
        if (trustAnchor == null) {
            set2 = this.params.trustAnchors();
        } else {
            set2 = Collections.singleton(trustAnchor);
        }
        Set<TrustAnchor> set4 = set2;
        try {
            PKIXBuilderParameters pKIXBuilderParameters = new PKIXBuilderParameters(set4, (CertSelector) rejectKeySelector);
            pKIXBuilderParameters.setInitialPolicies(this.params.initialPolicies());
            pKIXBuilderParameters.setCertStores(this.certStores);
            pKIXBuilderParameters.setExplicitPolicyRequired(this.params.explicitPolicyRequired());
            pKIXBuilderParameters.setPolicyMappingInhibited(this.params.policyMappingInhibited());
            pKIXBuilderParameters.setAnyPolicyInhibited(this.params.anyPolicyInhibited());
            pKIXBuilderParameters.setDate(this.params.date());
            pKIXBuilderParameters.setCertPathCheckers(this.params.getPKIXParameters().getCertPathCheckers());
            pKIXBuilderParameters.setSigProvider(this.params.sigProvider());
            pKIXBuilderParameters.setRevocationEnabled(false);
            if (Builder.USE_AIA) {
                try {
                    x509CertImpl = X509CertImpl.toImpl(x509Certificate);
                } catch (CertificateException e) {
                    CertificateException certificateException = e;
                    if (debug != null) {
                        debug.println("RevocationChecker.buildToNewKey: error decoding cert: " + certificateException);
                    }
                    x509CertImpl = null;
                }
                AuthorityInfoAccessExtension authorityInfoAccessExtension = x509CertImpl != null ? x509CertImpl.getAuthorityInfoAccessExtension() : null;
                if (!(authorityInfoAccessExtension == null || (accessDescriptions = authorityInfoAccessExtension.getAccessDescriptions()) == null)) {
                    for (AccessDescription instance : accessDescriptions) {
                        CertStore instance2 = URICertStore.getInstance(instance);
                        if (instance2 != null) {
                            Debug debug3 = debug;
                            if (debug3 != null) {
                                debug3.println("adding AIAext CertStore");
                            }
                            pKIXBuilderParameters.addCertStore(instance2);
                        }
                    }
                }
            }
            try {
                CertPathBuilder instance3 = CertPathBuilder.getInstance("PKIX");
                Set<X509Certificate> set5 = set;
                while (true) {
                    try {
                        Debug debug4 = debug;
                        if (debug4 != null) {
                            debug4.println("RevocationChecker.buildToNewKey() about to try build ...");
                        }
                        PKIXCertPathBuilderResult pKIXCertPathBuilderResult = (PKIXCertPathBuilderResult) instance3.build(pKIXBuilderParameters);
                        if (debug4 != null) {
                            debug4.println("RevocationChecker.buildToNewKey() about to check revocation ...");
                        }
                        if (set5 == null) {
                            set5 = new HashSet<>();
                        }
                        Set<X509Certificate> set6 = set5;
                        set6.add(x509Certificate);
                        TrustAnchor trustAnchor2 = pKIXCertPathBuilderResult.getTrustAnchor();
                        PublicKey cAPublicKey = trustAnchor2.getCAPublicKey();
                        if (cAPublicKey == null) {
                            cAPublicKey = trustAnchor2.getTrustedCert().getPublicKey();
                        }
                        List<? extends Certificate> certificates = pKIXCertPathBuilderResult.getCertPath().getCertificates();
                        try {
                            PublicKey publicKey4 = cAPublicKey;
                            int size = certificates.size() - 1;
                            boolean z = true;
                            while (size >= 0) {
                                X509Certificate x509Certificate3 = (X509Certificate) certificates.get(size);
                                Debug debug5 = debug;
                                if (debug5 != null) {
                                    debug5.println("RevocationChecker.buildToNewKey() index " + size + " checking " + x509Certificate3);
                                }
                                X509Certificate x509Certificate4 = x509Certificate3;
                                int i = size;
                                boolean z2 = z;
                                set3 = set6;
                                certPathBuilder = instance3;
                                try {
                                    checkCRLs(x509Certificate3, publicKey4, (X509Certificate) null, z2, true, set6, set4);
                                    boolean certCanSignCrl = certCanSignCrl(x509Certificate4);
                                    publicKey4 = x509Certificate4.getPublicKey();
                                    size = i - 1;
                                    X509Certificate x509Certificate5 = x509Certificate;
                                    set6 = set3;
                                    instance3 = certPathBuilder;
                                    z = certCanSignCrl;
                                } catch (CertPathValidatorException unused) {
                                    hashSet.add(pKIXCertPathBuilderResult.getPublicKey());
                                    set5 = set3;
                                    instance3 = certPathBuilder;
                                }
                            }
                            set3 = set6;
                            certPathBuilder = instance3;
                            Debug debug6 = debug;
                            if (debug6 != null) {
                                debug6.println("RevocationChecker.buildToNewKey() got key " + pKIXCertPathBuilderResult.getPublicKey());
                            }
                            publicKey2 = pKIXCertPathBuilderResult.getPublicKey();
                            if (certificates.isEmpty()) {
                                x509Certificate2 = null;
                            } else {
                                x509Certificate2 = (X509Certificate) certificates.get(0);
                            }
                            checkCRLs(x509Certificate, publicKey2, x509Certificate2, true, false, (Set<X509Certificate>) null, this.params.trustAnchors());
                            return;
                        } catch (CertPathValidatorException unused2) {
                            set3 = set6;
                            certPathBuilder = instance3;
                            hashSet.add(pKIXCertPathBuilderResult.getPublicKey());
                            set5 = set3;
                            instance3 = certPathBuilder;
                        }
                    } catch (CertPathValidatorException e2) {
                        if (e2.getReason() != CertPathValidatorException.BasicReason.REVOKED) {
                            hashSet.add(publicKey2);
                        } else {
                            throw e2;
                        }
                    } catch (InvalidAlgorithmParameterException e3) {
                        throw new CertPathValidatorException((Throwable) e3);
                    } catch (CertPathBuilderException unused3) {
                        throw new CertPathValidatorException("Could not determine revocation status", (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
                    }
                    set5 = set3;
                    instance3 = certPathBuilder;
                }
            } catch (NoSuchAlgorithmException e4) {
                throw new CertPathValidatorException((Throwable) e4);
            }
        } catch (InvalidAlgorithmParameterException e5) {
            throw new RuntimeException((Throwable) e5);
        }
    }

    public RevocationChecker clone() {
        RevocationChecker revocationChecker = (RevocationChecker) super.clone();
        revocationChecker.softFailExceptions = new LinkedList<>(this.softFailExceptions);
        return revocationChecker;
    }

    private static class RejectKeySelector extends X509CertSelector {
        private final Set<PublicKey> badKeySet;

        RejectKeySelector(Set<PublicKey> set) {
            this.badKeySet = set;
        }

        public boolean match(Certificate certificate) {
            if (!super.match(certificate)) {
                return false;
            }
            if (this.badKeySet.contains(certificate.getPublicKey())) {
                if (RevocationChecker.debug != null) {
                    RevocationChecker.debug.println("RejectKeySelector.match: bad key");
                }
                return false;
            } else if (RevocationChecker.debug == null) {
                return true;
            } else {
                RevocationChecker.debug.println("RejectKeySelector.match: returning true");
                return true;
            }
        }

        public String toString() {
            return "RejectKeySelector: [\n" + super.toString() + this.badKeySet + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }
}
