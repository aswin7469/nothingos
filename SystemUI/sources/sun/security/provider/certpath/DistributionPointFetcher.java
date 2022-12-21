package sun.security.provider.certpath;

import java.net.URI;
import java.p026io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CRLSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.provider.certpath.PKIX;
import sun.security.provider.certpath.URICertStore;
import sun.security.util.Debug;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.CRLDistributionPointsExtension;
import sun.security.x509.DistributionPoint;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.GeneralNames;
import sun.security.x509.RDN;
import sun.security.x509.URIName;
import sun.security.x509.X500Name;
import sun.security.x509.X509CRLImpl;
import sun.security.x509.X509CertImpl;

public class DistributionPointFetcher {
    private static final boolean[] ALL_REASONS = {true, true, true, true, true, true, true, true, true};
    private static final Debug debug = Debug.getInstance("certpath");

    private DistributionPointFetcher() {
    }

    public static Collection<X509CRL> getCRLs(X509CRLSelector x509CRLSelector, boolean z, PublicKey publicKey, String str, List<CertStore> list, boolean[] zArr, Set<TrustAnchor> set, Date date) throws CertStoreException {
        return getCRLs(x509CRLSelector, z, publicKey, (X509Certificate) null, str, list, zArr, set, date);
    }

    public static Collection<X509CRL> getCRLs(X509CRLSelector x509CRLSelector, boolean z, PublicKey publicKey, X509Certificate x509Certificate, String str, List<CertStore> list, boolean[] zArr, Set<TrustAnchor> set, Date date) throws CertStoreException {
        X509Certificate certificateChecking = x509CRLSelector.getCertificateChecking();
        if (certificateChecking == null) {
            return Collections.emptySet();
        }
        try {
            X509CertImpl impl = X509CertImpl.toImpl(certificateChecking);
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("DistributionPointFetcher.getCRLs: Checking CRLDPs for " + impl.getSubjectX500Principal());
            }
            CRLDistributionPointsExtension cRLDistributionPointsExtension = impl.getCRLDistributionPointsExtension();
            if (cRLDistributionPointsExtension == null) {
                if (debug2 != null) {
                    debug2.println("No CRLDP ext");
                }
                return Collections.emptySet();
            }
            List<DistributionPoint> list2 = cRLDistributionPointsExtension.get(CRLDistributionPointsExtension.POINTS);
            HashSet hashSet = new HashSet();
            for (DistributionPoint cRLs : list2) {
                if (Arrays.equals(zArr, ALL_REASONS)) {
                    break;
                }
                hashSet.addAll(getCRLs(x509CRLSelector, impl, cRLs, zArr, z, publicKey, x509Certificate, str, list, set, date));
            }
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("Returning " + hashSet.size() + " CRLs");
            }
            return hashSet;
        } catch (IOException | CertificateException unused) {
            return Collections.emptySet();
        }
    }

    private static Collection<X509CRL> getCRLs(X509CRLSelector x509CRLSelector, X509CertImpl x509CertImpl, DistributionPoint distributionPoint, boolean[] zArr, boolean z, PublicKey publicKey, X509Certificate x509Certificate, String str, List<CertStore> list, Set<TrustAnchor> set, Date date) throws CertStoreException {
        X509CRL crl;
        X509CRLSelector x509CRLSelector2 = x509CRLSelector;
        GeneralNames fullName = distributionPoint.getFullName();
        if (fullName == null) {
            RDN relativeName = distributionPoint.getRelativeName();
            if (relativeName == null) {
                return Collections.emptySet();
            }
            try {
                GeneralNames cRLIssuer = distributionPoint.getCRLIssuer();
                if (cRLIssuer == null) {
                    fullName = getFullNames((X500Name) x509CertImpl.getIssuerDN(), relativeName);
                } else if (cRLIssuer.size() != 1) {
                    return Collections.emptySet();
                } else {
                    fullName = getFullNames((X500Name) cRLIssuer.get(0).getName(), relativeName);
                }
            } catch (IOException unused) {
                return Collections.emptySet();
            }
        }
        ArrayList<X509CRL> arrayList = new ArrayList<>();
        Iterator<GeneralName> it = fullName.iterator();
        CertStoreException e = null;
        while (it.hasNext()) {
            try {
                GeneralName next = it.next();
                if (next.getType() == 4) {
                    try {
                        arrayList.addAll(getCRLs((X500Name) next.getName(), x509CertImpl.getIssuerX500Principal(), list));
                    } catch (CertStoreException e2) {
                        e = e2;
                    }
                } else {
                    List<CertStore> list2 = list;
                    if (next.getType() == 6 && (crl = getCRL((URIName) next.getName())) != null) {
                        arrayList.add(crl);
                    }
                }
            } catch (CertStoreException e3) {
                e = e3;
                List<CertStore> list3 = list;
            }
        }
        List<CertStore> list4 = list;
        if (!arrayList.isEmpty() || e == null) {
            ArrayList arrayList2 = new ArrayList(2);
            for (X509CRL x509crl : arrayList) {
                try {
                    x509CRLSelector2.setIssuerNames((Collection<?>) null);
                    if (x509CRLSelector2.match(x509crl) && verifyCRL(x509CertImpl, distributionPoint, x509crl, zArr, z, publicKey, x509Certificate, str, set, list, date)) {
                        arrayList2.add(x509crl);
                    }
                } catch (IOException | CRLException e4) {
                    Debug debug2 = debug;
                    if (debug2 != null) {
                        debug2.println("Exception verifying CRL: " + e4.getMessage());
                        e4.printStackTrace();
                    }
                }
                List<CertStore> list5 = list;
            }
            return arrayList2;
        }
        throw e;
    }

    private static X509CRL getCRL(URIName uRIName) throws CertStoreException {
        URI uri = uRIName.getURI();
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Trying to fetch CRL from DP " + uri);
        }
        try {
            Collection<? extends CRL> cRLs = URICertStore.getInstance(new URICertStore.URICertStoreParameters(uri)).getCRLs((CRLSelector) null);
            if (cRLs.isEmpty()) {
                return null;
            }
            return (X509CRL) cRLs.iterator().next();
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("Can't create URICertStore: " + e.getMessage());
            }
            return null;
        }
    }

    private static Collection<X509CRL> getCRLs(X500Name x500Name, X500Principal x500Principal, List<CertStore> list) throws CertStoreException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Trying to fetch CRL from DP " + x500Name);
        }
        X509CRLSelector x509CRLSelector = new X509CRLSelector();
        x509CRLSelector.addIssuer(x500Name.asX500Principal());
        x509CRLSelector.addIssuer(x500Principal);
        ArrayList arrayList = new ArrayList();
        PKIX.CertStoreTypeException certStoreTypeException = null;
        for (CertStore next : list) {
            try {
                for (CRL crl : next.getCRLs(x509CRLSelector)) {
                    arrayList.add((X509CRL) crl);
                }
            } catch (CertStoreException e) {
                Debug debug3 = debug;
                if (debug3 != null) {
                    debug3.println("Exception while retrieving CRLs: " + e);
                    e.printStackTrace();
                }
                certStoreTypeException = new PKIX.CertStoreTypeException(next.getType(), e);
            }
        }
        if (!arrayList.isEmpty() || certStoreTypeException == null) {
            return arrayList;
        }
        throw certStoreTypeException;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v27, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v18, resolved type: boolean[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v29, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v20, resolved type: boolean[]} */
    /* JADX WARNING: type inference failed for: r11v8, types: [sun.security.x509.GeneralNameInterface, java.lang.Object] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean verifyCRL(sun.security.x509.X509CertImpl r17, sun.security.x509.DistributionPoint r18, java.security.cert.X509CRL r19, boolean[] r20, boolean r21, java.security.PublicKey r22, java.security.cert.X509Certificate r23, java.lang.String r24, java.util.Set<java.security.cert.TrustAnchor> r25, java.util.List<java.security.cert.CertStore> r26, java.util.Date r27) throws java.security.cert.CRLException, java.p026io.IOException {
        /*
            r0 = r17
            r1 = r19
            r2 = r20
            r3 = r23
            r4 = r24
            sun.security.util.Debug r5 = debug
            if (r5 == 0) goto L_0x003f
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "DistributionPointFetcher.verifyCRL: checking revocation status for\n  SN: "
            r6.<init>((java.lang.String) r7)
            java.math.BigInteger r7 = r17.getSerialNumber()
            java.lang.String r7 = sun.security.util.Debug.toHexString(r7)
            r6.append((java.lang.String) r7)
            java.lang.String r7 = "\n  Subject: "
            r6.append((java.lang.String) r7)
            javax.security.auth.x500.X500Principal r7 = r17.getSubjectX500Principal()
            r6.append((java.lang.Object) r7)
            java.lang.String r7 = "\n  Issuer: "
            r6.append((java.lang.String) r7)
            javax.security.auth.x500.X500Principal r7 = r17.getIssuerX500Principal()
            r6.append((java.lang.Object) r7)
            java.lang.String r6 = r6.toString()
            r5.println(r6)
        L_0x003f:
            sun.security.x509.X509CRLImpl r6 = sun.security.x509.X509CRLImpl.toImpl(r19)
            sun.security.x509.IssuingDistributionPointExtension r7 = r6.getIssuingDistributionPointExtension()
            java.security.Principal r8 = r17.getIssuerDN()
            sun.security.x509.X500Name r8 = (sun.security.x509.X500Name) r8
            java.security.Principal r9 = r6.getIssuerDN()
            sun.security.x509.X500Name r9 = (sun.security.x509.X500Name) r9
            sun.security.x509.GeneralNames r10 = r18.getCRLIssuer()
            r12 = 1
            if (r10 == 0) goto L_0x00ab
            if (r7 == 0) goto L_0x00a8
            java.lang.String r5 = "indirect_crl"
            java.lang.Object r5 = r7.get(r5)
            java.lang.Boolean r5 = (java.lang.Boolean) r5
            java.lang.Boolean r14 = java.lang.Boolean.FALSE
            boolean r5 = r5.equals(r14)
            if (r5 == 0) goto L_0x006d
            goto L_0x00a8
        L_0x006d:
            java.util.Iterator r5 = r10.iterator()
            r14 = 0
            r15 = 0
        L_0x0073:
            if (r14 != 0) goto L_0x0090
            boolean r16 = r5.hasNext()
            if (r16 == 0) goto L_0x0090
            java.lang.Object r16 = r5.next()
            sun.security.x509.GeneralName r16 = (sun.security.x509.GeneralName) r16
            sun.security.x509.GeneralNameInterface r11 = r16.getName()
            boolean r13 = r9.equals(r11)
            if (r13 != r12) goto L_0x0073
            r15 = r11
            sun.security.x509.X500Name r15 = (sun.security.x509.X500Name) r15
            r14 = r12
            goto L_0x0073
        L_0x0090:
            if (r14 != 0) goto L_0x0095
            r16 = 0
            return r16
        L_0x0095:
            r16 = 0
            boolean r5 = issues(r0, r6, r4)
            if (r5 == 0) goto L_0x00a4
            java.security.PublicKey r5 = r17.getPublicKey()
            r11 = r16
            goto L_0x00ff
        L_0x00a4:
            r5 = r22
            r11 = r12
            goto L_0x00ff
        L_0x00a8:
            r16 = 0
            return r16
        L_0x00ab:
            boolean r11 = r9.equals(r8)
            if (r11 != 0) goto L_0x00ce
            if (r5 == 0) goto L_0x00cc
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "crl issuer does not equal cert issuer.\ncrl issuer: "
            r0.<init>((java.lang.String) r1)
            r0.append((java.lang.Object) r9)
            java.lang.String r1 = "\ncert issuer: "
            r0.append((java.lang.String) r1)
            r0.append((java.lang.Object) r8)
            java.lang.String r0 = r0.toString()
            r5.println(r0)
        L_0x00cc:
            r1 = 0
            return r1
        L_0x00ce:
            sun.security.x509.KeyIdentifier r5 = r17.getAuthKeyId()
            sun.security.x509.KeyIdentifier r11 = r6.getAuthKeyId()
            if (r5 == 0) goto L_0x00f0
            if (r11 != 0) goto L_0x00db
            goto L_0x00f0
        L_0x00db:
            boolean r5 = r5.equals(r11)
            if (r5 != 0) goto L_0x00fb
            boolean r5 = issues(r0, r6, r4)
            if (r5 == 0) goto L_0x00ec
            java.security.PublicKey r5 = r17.getPublicKey()
            goto L_0x00fd
        L_0x00ec:
            r5 = r22
            r11 = r12
            goto L_0x00fe
        L_0x00f0:
            boolean r5 = issues(r0, r6, r4)
            if (r5 == 0) goto L_0x00fb
            java.security.PublicKey r5 = r17.getPublicKey()
            goto L_0x00fd
        L_0x00fb:
            r5 = r22
        L_0x00fd:
            r11 = 0
        L_0x00fe:
            r15 = 0
        L_0x00ff:
            if (r11 != 0) goto L_0x0105
            if (r21 != 0) goto L_0x0105
            r13 = 0
            return r13
        L_0x0105:
            if (r7 == 0) goto L_0x0292
            java.lang.String r13 = "point"
            java.lang.Object r13 = r7.get(r13)
            sun.security.x509.DistributionPointName r13 = (sun.security.x509.DistributionPointName) r13
            if (r13 == 0) goto L_0x0234
            sun.security.x509.GeneralNames r14 = r13.getFullName()
            if (r14 != 0) goto L_0x0141
            sun.security.x509.RDN r13 = r13.getRelativeName()
            if (r13 != 0) goto L_0x0128
            sun.security.util.Debug r0 = debug
            if (r0 == 0) goto L_0x0126
            java.lang.String r1 = "IDP must be relative or full DN"
            r0.println(r1)
        L_0x0126:
            r1 = 0
            return r1
        L_0x0128:
            sun.security.util.Debug r14 = debug
            if (r14 == 0) goto L_0x013d
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r0 = "IDP relativeName:"
            r12.<init>((java.lang.String) r0)
            r12.append((java.lang.Object) r13)
            java.lang.String r0 = r12.toString()
            r14.println(r0)
        L_0x013d:
            sun.security.x509.GeneralNames r14 = getFullNames(r9, r13)
        L_0x0141:
            sun.security.x509.GeneralNames r0 = r18.getFullName()
            if (r0 != 0) goto L_0x0184
            sun.security.x509.RDN r0 = r18.getRelativeName()
            if (r0 == 0) goto L_0x014e
            goto L_0x0184
        L_0x014e:
            java.util.Iterator r0 = r10.iterator()
            r8 = 0
        L_0x0153:
            if (r8 != 0) goto L_0x0180
            boolean r10 = r0.hasNext()
            if (r10 == 0) goto L_0x0180
            java.lang.Object r10 = r0.next()
            sun.security.x509.GeneralName r10 = (sun.security.x509.GeneralName) r10
            sun.security.x509.GeneralNameInterface r10 = r10.getName()
            java.util.Iterator r12 = r14.iterator()
        L_0x0169:
            if (r8 != 0) goto L_0x0153
            boolean r13 = r12.hasNext()
            if (r13 == 0) goto L_0x0153
            java.lang.Object r8 = r12.next()
            sun.security.x509.GeneralName r8 = (sun.security.x509.GeneralName) r8
            sun.security.x509.GeneralNameInterface r8 = r8.getName()
            boolean r8 = r10.equals(r8)
            goto L_0x0169
        L_0x0180:
            if (r8 != 0) goto L_0x0234
            r8 = 0
            return r8
        L_0x0184:
            sun.security.x509.GeneralNames r0 = r18.getFullName()
            if (r0 != 0) goto L_0x01cb
            sun.security.x509.RDN r0 = r18.getRelativeName()
            if (r0 != 0) goto L_0x019b
            sun.security.util.Debug r0 = debug
            if (r0 == 0) goto L_0x0199
            java.lang.String r1 = "DP must be relative or full DN"
            r0.println(r1)
        L_0x0199:
            r1 = 0
            return r1
        L_0x019b:
            sun.security.util.Debug r12 = debug
            if (r12 == 0) goto L_0x01b0
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            java.lang.String r1 = "DP relativeName:"
            r13.<init>((java.lang.String) r1)
            r13.append((java.lang.Object) r0)
            java.lang.String r1 = r13.toString()
            r12.println(r1)
        L_0x01b0:
            if (r11 == 0) goto L_0x01c7
            int r1 = r10.size()
            r8 = 1
            if (r1 == r8) goto L_0x01c2
            if (r12 == 0) goto L_0x01c0
            java.lang.String r0 = "must only be one CRL issuer when relative name present"
            r12.println(r0)
        L_0x01c0:
            r1 = 0
            return r1
        L_0x01c2:
            sun.security.x509.GeneralNames r0 = getFullNames(r15, r0)
            goto L_0x01cb
        L_0x01c7:
            sun.security.x509.GeneralNames r0 = getFullNames(r8, r0)
        L_0x01cb:
            java.util.Iterator r1 = r14.iterator()
            r8 = 0
        L_0x01d0:
            if (r8 != 0) goto L_0x0227
            boolean r10 = r1.hasNext()
            if (r10 == 0) goto L_0x0227
            java.lang.Object r10 = r1.next()
            sun.security.x509.GeneralName r10 = (sun.security.x509.GeneralName) r10
            sun.security.x509.GeneralNameInterface r10 = r10.getName()
            sun.security.util.Debug r12 = debug
            if (r12 == 0) goto L_0x01f7
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            java.lang.String r14 = "idpName: "
            r13.<init>((java.lang.String) r14)
            r13.append((java.lang.Object) r10)
            java.lang.String r13 = r13.toString()
            r12.println(r13)
        L_0x01f7:
            java.util.Iterator r12 = r0.iterator()
        L_0x01fb:
            if (r8 != 0) goto L_0x01d0
            boolean r13 = r12.hasNext()
            if (r13 == 0) goto L_0x01d0
            java.lang.Object r8 = r12.next()
            sun.security.x509.GeneralName r8 = (sun.security.x509.GeneralName) r8
            sun.security.x509.GeneralNameInterface r8 = r8.getName()
            sun.security.util.Debug r13 = debug
            if (r13 == 0) goto L_0x0222
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            java.lang.String r15 = "pointName: "
            r14.<init>((java.lang.String) r15)
            r14.append((java.lang.Object) r8)
            java.lang.String r14 = r14.toString()
            r13.println(r14)
        L_0x0222:
            boolean r8 = r10.equals(r8)
            goto L_0x01fb
        L_0x0227:
            if (r8 != 0) goto L_0x0234
            sun.security.util.Debug r0 = debug
            if (r0 == 0) goto L_0x0232
            java.lang.String r1 = "IDP name does not match DP name"
            r0.println(r1)
        L_0x0232:
            r1 = 0
            return r1
        L_0x0234:
            java.lang.String r0 = "only_user_certs"
            java.lang.Object r0 = r7.get(r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            java.lang.Boolean r1 = java.lang.Boolean.TRUE
            boolean r0 = r0.equals(r1)
            r1 = -1
            if (r0 == 0) goto L_0x0256
            int r0 = r17.getBasicConstraints()
            if (r0 == r1) goto L_0x0256
            sun.security.util.Debug r0 = debug
            if (r0 == 0) goto L_0x0254
            java.lang.String r1 = "cert must be a EE cert"
            r0.println(r1)
        L_0x0254:
            r1 = 0
            return r1
        L_0x0256:
            java.lang.String r0 = "only_ca_certs"
            java.lang.Object r0 = r7.get(r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            java.lang.Boolean r8 = java.lang.Boolean.TRUE
            boolean r0 = r0.equals(r8)
            if (r0 == 0) goto L_0x0277
            int r0 = r17.getBasicConstraints()
            if (r0 != r1) goto L_0x0277
            sun.security.util.Debug r0 = debug
            if (r0 == 0) goto L_0x0275
            java.lang.String r1 = "cert must be a CA cert"
            r0.println(r1)
        L_0x0275:
            r1 = 0
            return r1
        L_0x0277:
            java.lang.String r0 = "only_attribute_certs"
            java.lang.Object r0 = r7.get(r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            java.lang.Boolean r1 = java.lang.Boolean.TRUE
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0292
            sun.security.util.Debug r0 = debug
            if (r0 == 0) goto L_0x0290
            java.lang.String r1 = "cert must not be an AA cert"
            r0.println(r1)
        L_0x0290:
            r1 = 0
            return r1
        L_0x0292:
            r0 = 9
            boolean[] r1 = new boolean[r0]
            if (r7 == 0) goto L_0x02a1
            java.lang.String r8 = "reasons"
            java.lang.Object r8 = r7.get(r8)
            sun.security.x509.ReasonFlags r8 = (sun.security.x509.ReasonFlags) r8
            goto L_0x02a2
        L_0x02a1:
            r8 = 0
        L_0x02a2:
            boolean[] r10 = r18.getReasonFlags()
            if (r8 == 0) goto L_0x02d3
            if (r10 == 0) goto L_0x02c7
            boolean[] r7 = r8.getFlags()
            r8 = 0
        L_0x02af:
            if (r8 >= r0) goto L_0x02e5
            int r12 = r7.length
            if (r8 >= r12) goto L_0x02c1
            boolean r12 = r7[r8]
            if (r12 == 0) goto L_0x02c1
            int r12 = r10.length
            if (r8 >= r12) goto L_0x02c1
            boolean r12 = r10[r8]
            if (r12 == 0) goto L_0x02c1
            r12 = 1
            goto L_0x02c2
        L_0x02c1:
            r12 = 0
        L_0x02c2:
            r1[r8] = r12
            int r8 = r8 + 1
            goto L_0x02af
        L_0x02c7:
            boolean[] r0 = r8.getFlags()
            java.lang.Object r0 = r0.clone()
            r1 = r0
            boolean[] r1 = (boolean[]) r1
            goto L_0x02e5
        L_0x02d3:
            if (r7 == 0) goto L_0x02d7
            if (r8 != 0) goto L_0x02e5
        L_0x02d7:
            if (r10 == 0) goto L_0x02e1
            java.lang.Object r0 = r10.clone()
            r1 = r0
            boolean[] r1 = (boolean[]) r1
            goto L_0x02e5
        L_0x02e1:
            r0 = 1
            java.util.Arrays.fill((boolean[]) r1, (boolean) r0)
        L_0x02e5:
            r0 = 0
            r7 = 0
        L_0x02e7:
            int r8 = r1.length
            if (r0 >= r8) goto L_0x02fb
            if (r7 != 0) goto L_0x02fb
            boolean r8 = r1[r0]
            if (r8 == 0) goto L_0x02f8
            int r8 = r2.length
            if (r0 >= r8) goto L_0x02f7
            boolean r8 = r2[r0]
            if (r8 != 0) goto L_0x02f8
        L_0x02f7:
            r7 = 1
        L_0x02f8:
            int r0 = r0 + 1
            goto L_0x02e7
        L_0x02fb:
            if (r7 != 0) goto L_0x02ff
            r7 = 0
            return r7
        L_0x02ff:
            if (r11 == 0) goto L_0x0387
            java.security.cert.X509CertSelector r0 = new java.security.cert.X509CertSelector
            r0.<init>()
            javax.security.auth.x500.X500Principal r7 = r9.asX500Principal()
            r0.setSubject((javax.security.auth.x500.X500Principal) r7)
            r7 = 7
            boolean[] r7 = new boolean[r7]
            r7 = {0, 0, 0, 0, 0, 0, 1} // fill-array
            r0.setKeyUsage(r7)
            sun.security.x509.AuthorityKeyIdentifierExtension r6 = r6.getAuthKeyIdExtension()
            if (r6 == 0) goto L_0x0336
            byte[] r7 = r6.getEncodedKeyIdentifier()
            if (r7 == 0) goto L_0x0325
            r0.setSubjectKeyIdentifier(r7)
        L_0x0325:
            java.lang.String r7 = "serial_number"
            java.lang.Object r6 = r6.get(r7)
            sun.security.x509.SerialNumber r6 = (sun.security.x509.SerialNumber) r6
            if (r6 == 0) goto L_0x0336
            java.math.BigInteger r6 = r6.getNumber()
            r0.setSerialNumber(r6)
        L_0x0336:
            java.util.HashSet r6 = new java.util.HashSet
            r7 = r25
            r6.<init>(r7)
            if (r5 == 0) goto L_0x0356
            if (r3 == 0) goto L_0x0348
            java.security.cert.TrustAnchor r5 = new java.security.cert.TrustAnchor
            r7 = 0
            r5.<init>(r3, r7)
            goto L_0x0353
        L_0x0348:
            r7 = 0
            javax.security.auth.x500.X500Principal r3 = r17.getIssuerX500Principal()
            java.security.cert.TrustAnchor r8 = new java.security.cert.TrustAnchor
            r8.<init>((javax.security.auth.x500.X500Principal) r3, (java.security.PublicKey) r5, (byte[]) r7)
            r5 = r8
        L_0x0353:
            r6.add(r5)
        L_0x0356:
            java.security.cert.PKIXBuilderParameters r3 = new java.security.cert.PKIXBuilderParameters     // Catch:{ InvalidAlgorithmParameterException -> 0x0380 }
            r3.<init>((java.util.Set<java.security.cert.TrustAnchor>) r6, (java.security.cert.CertSelector) r0)     // Catch:{ InvalidAlgorithmParameterException -> 0x0380 }
            r0 = r26
            r3.setCertStores(r0)
            r3.setSigProvider(r4)
            r0 = r27
            r3.setDate(r0)
            java.lang.String r0 = "PKIX"
            java.security.cert.CertPathBuilder r0 = java.security.cert.CertPathBuilder.getInstance(r0)     // Catch:{ GeneralSecurityException -> 0x0379 }
            java.security.cert.CertPathBuilderResult r0 = r0.build(r3)     // Catch:{ GeneralSecurityException -> 0x0379 }
            java.security.cert.PKIXCertPathBuilderResult r0 = (java.security.cert.PKIXCertPathBuilderResult) r0     // Catch:{ GeneralSecurityException -> 0x0379 }
            java.security.PublicKey r5 = r0.getPublicKey()     // Catch:{ GeneralSecurityException -> 0x0379 }
            goto L_0x0387
        L_0x0379:
            r0 = move-exception
            java.security.cert.CRLException r1 = new java.security.cert.CRLException
            r1.<init>((java.lang.Throwable) r0)
            throw r1
        L_0x0380:
            r0 = move-exception
            java.security.cert.CRLException r1 = new java.security.cert.CRLException
            r1.<init>((java.lang.Throwable) r0)
            throw r1
        L_0x0387:
            r0 = r19
            sun.security.provider.certpath.AlgorithmChecker.check((java.security.PublicKey) r5, (java.security.cert.X509CRL) r0)     // Catch:{ CertPathValidatorException -> 0x03f8 }
            r0.verify((java.security.PublicKey) r5, (java.lang.String) r4)     // Catch:{ GeneralSecurityException -> 0x03eb }
            java.util.Set r0 = r19.getCriticalExtensionOIDs()
            if (r0 == 0) goto L_0x03d1
            sun.security.util.ObjectIdentifier r3 = sun.security.x509.PKIXExtensions.IssuingDistributionPoint_Id
            java.lang.String r3 = r3.toString()
            r0.remove(r3)
            boolean r3 = r0.isEmpty()
            if (r3 != 0) goto L_0x03d1
            sun.security.util.Debug r1 = debug
            if (r1 == 0) goto L_0x03cf
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Unrecognized critical extension(s) in CRL: "
            r2.<init>((java.lang.String) r3)
            r2.append((java.lang.Object) r0)
            java.lang.String r2 = r2.toString()
            r1.println(r2)
            java.util.Iterator r0 = r0.iterator()
        L_0x03bd:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x03cf
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            sun.security.util.Debug r2 = debug
            r2.println(r1)
            goto L_0x03bd
        L_0x03cf:
            r1 = 0
            return r1
        L_0x03d1:
            r0 = 0
        L_0x03d2:
            int r3 = r2.length
            if (r0 >= r3) goto L_0x03e9
            boolean r3 = r2[r0]
            if (r3 != 0) goto L_0x03e3
            int r3 = r1.length
            if (r0 >= r3) goto L_0x03e1
            boolean r3 = r1[r0]
            if (r3 == 0) goto L_0x03e1
            goto L_0x03e3
        L_0x03e1:
            r3 = 0
            goto L_0x03e4
        L_0x03e3:
            r3 = 1
        L_0x03e4:
            r2[r0] = r3
            int r0 = r0 + 1
            goto L_0x03d2
        L_0x03e9:
            r0 = 1
            return r0
        L_0x03eb:
            sun.security.util.Debug r0 = debug
            if (r0 == 0) goto L_0x03f6
            sun.security.util.Debug r0 = debug
            java.lang.String r1 = "CRL signature failed to verify"
            r0.println(r1)
        L_0x03f6:
            r1 = 0
            return r1
        L_0x03f8:
            r0 = move-exception
            r1 = r0
            sun.security.util.Debug r0 = debug
            if (r0 == 0) goto L_0x0411
            sun.security.util.Debug r0 = debug
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "CRL signature algorithm check failed: "
            r2.<init>((java.lang.String) r3)
            r2.append((java.lang.Object) r1)
            java.lang.String r1 = r2.toString()
            r0.println(r1)
        L_0x0411:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.certpath.DistributionPointFetcher.verifyCRL(sun.security.x509.X509CertImpl, sun.security.x509.DistributionPoint, java.security.cert.X509CRL, boolean[], boolean, java.security.PublicKey, java.security.cert.X509Certificate, java.lang.String, java.util.Set, java.util.List, java.util.Date):boolean");
    }

    private static GeneralNames getFullNames(X500Name x500Name, RDN rdn) throws IOException {
        ArrayList arrayList = new ArrayList(x500Name.rdns());
        arrayList.add(rdn);
        X500Name x500Name2 = new X500Name((RDN[]) arrayList.toArray(new RDN[0]));
        GeneralNames generalNames = new GeneralNames();
        generalNames.add(new GeneralName((GeneralNameInterface) x500Name2));
        return generalNames;
    }

    private static boolean issues(X509CertImpl x509CertImpl, X509CRLImpl x509CRLImpl, String str) throws IOException {
        AdaptableX509CertSelector adaptableX509CertSelector = new AdaptableX509CertSelector();
        boolean[] keyUsage = x509CertImpl.getKeyUsage();
        if (keyUsage != null) {
            keyUsage[6] = true;
            adaptableX509CertSelector.setKeyUsage(keyUsage);
        }
        adaptableX509CertSelector.setSubject(x509CRLImpl.getIssuerX500Principal());
        AuthorityKeyIdentifierExtension authKeyIdExtension = x509CRLImpl.getAuthKeyIdExtension();
        adaptableX509CertSelector.setSkiAndSerialNumber(authKeyIdExtension);
        boolean match = adaptableX509CertSelector.match(x509CertImpl);
        if (!match || (authKeyIdExtension != null && x509CertImpl.getAuthorityKeyIdentifierExtension() != null)) {
            return match;
        }
        try {
            x509CRLImpl.verify(x509CertImpl.getPublicKey(), str);
            return true;
        } catch (GeneralSecurityException unused) {
            return false;
        }
    }
}
