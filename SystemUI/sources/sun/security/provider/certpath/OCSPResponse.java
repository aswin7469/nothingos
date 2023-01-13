package sun.security.provider.certpath;

import android.net.wifi.WifiEnterpriseConfig;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRLReason;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.security.auth.x500.X500Principal;
import sun.misc.HexDumpEncoder;
import sun.security.action.GetIntegerAction;
import sun.security.provider.certpath.OCSP;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;
import sun.security.x509.Extension;
import sun.security.x509.KeyIdentifier;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.X509CertImpl;

public final class OCSPResponse {
    private static final int CERT_STATUS_GOOD = 0;
    private static final int CERT_STATUS_REVOKED = 1;
    private static final int CERT_STATUS_UNKNOWN = 2;
    private static final int DEFAULT_MAX_CLOCK_SKEW = 900000;
    private static final int KEY_TAG = 2;
    private static final String KP_OCSP_SIGNING_OID = "1.3.6.1.5.5.7.3.9";
    private static final int MAX_CLOCK_SKEW = initializeClockSkew();
    private static final int NAME_TAG = 1;
    private static final ObjectIdentifier OCSP_BASIC_RESPONSE_OID = ObjectIdentifier.newInternal(new int[]{1, 3, 6, 1, 5, 5, 7, 48, 1, 1});
    /* access modifiers changed from: private */
    public static final Debug debug;
    private static final boolean dump;
    private static ResponseStatus[] rsvalues = ResponseStatus.values();
    /* access modifiers changed from: private */
    public static CRLReason[] values = CRLReason.values();
    private List<X509CertImpl> certs;
    private KeyIdentifier responderKeyId = null;
    private X500Principal responderName = null;
    private final byte[] responseNonce;
    private final ResponseStatus responseStatus;
    private final AlgorithmId sigAlgId;
    private final byte[] signature;
    private X509CertImpl signerCert = null;
    private final Map<CertId, SingleResponse> singleResponseMap;
    private final byte[] tbsResponseData;

    public enum ResponseStatus {
        SUCCESSFUL,
        MALFORMED_REQUEST,
        INTERNAL_ERROR,
        TRY_LATER,
        UNUSED,
        SIG_REQUIRED,
        UNAUTHORIZED
    }

    static {
        Debug instance = Debug.getInstance("certpath");
        debug = instance;
        dump = instance != null && Debug.isOn(WifiEnterpriseConfig.OCSP);
    }

    private static int initializeClockSkew() {
        Integer num = (Integer) AccessController.doPrivileged(new GetIntegerAction("com.sun.security.ocsp.clockSkew"));
        return (num == null || num.intValue() < 0) ? DEFAULT_MAX_CLOCK_SKEW : num.intValue() * 1000;
    }

    OCSPResponse(byte[] bArr) throws IOException {
        byte[] bArr2 = null;
        if (dump) {
            HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
            debug.println("OCSPResponse bytes...\n\n" + hexDumpEncoder.encode(bArr) + "\n");
        }
        DerValue derValue = new DerValue(bArr);
        if (derValue.tag == 48) {
            DerInputStream data = derValue.getData();
            int enumerated = data.getEnumerated();
            if (enumerated >= 0) {
                ResponseStatus[] responseStatusArr = rsvalues;
                if (enumerated < responseStatusArr.length) {
                    ResponseStatus responseStatus2 = responseStatusArr[enumerated];
                    this.responseStatus = responseStatus2;
                    Debug debug2 = debug;
                    if (debug2 != null) {
                        debug2.println("OCSP response status: " + responseStatus2);
                    }
                    if (responseStatus2 != ResponseStatus.SUCCESSFUL) {
                        this.singleResponseMap = Collections.emptyMap();
                        this.certs = new ArrayList();
                        this.sigAlgId = null;
                        this.signature = null;
                        this.tbsResponseData = null;
                        this.responseNonce = null;
                        return;
                    }
                    DerValue derValue2 = data.getDerValue();
                    int i = 0;
                    if (derValue2.isContextSpecific((byte) 0)) {
                        DerValue derValue3 = derValue2.data.getDerValue();
                        if (derValue3.tag == 48) {
                            DerInputStream derInputStream = derValue3.data;
                            ObjectIdentifier oid = derInputStream.getOID();
                            if (oid.equals((Object) OCSP_BASIC_RESPONSE_OID)) {
                                if (debug2 != null) {
                                    debug2.println("OCSP response type: basic");
                                }
                                DerValue[] sequence = new DerInputStream(derInputStream.getOctetString()).getSequence(2);
                                if (sequence.length >= 3) {
                                    DerValue derValue4 = sequence[0];
                                    this.tbsResponseData = derValue4.toByteArray();
                                    if (derValue4.tag == 48) {
                                        DerInputStream derInputStream2 = derValue4.data;
                                        DerValue derValue5 = derInputStream2.getDerValue();
                                        if (derValue5.isContextSpecific((byte) 0) && derValue5.isConstructed() && derValue5.isContextSpecific()) {
                                            DerValue derValue6 = derValue5.data.getDerValue();
                                            derValue6.getInteger();
                                            if (derValue6.data.available() == 0) {
                                                derValue5 = derInputStream2.getDerValue();
                                            } else {
                                                throw new IOException("Bad encoding in version  element of OCSP response: bad format");
                                            }
                                        }
                                        short s = (short) ((byte) (derValue5.tag & 31));
                                        if (s == 1) {
                                            this.responderName = new X500Principal(derValue5.getData().toByteArray());
                                            if (debug2 != null) {
                                                debug2.println("Responder's name: " + this.responderName);
                                            }
                                        } else if (s == 2) {
                                            this.responderKeyId = new KeyIdentifier(derValue5.getData().getOctetString());
                                            if (debug2 != null) {
                                                debug2.println("Responder's key ID: " + Debug.toString(this.responderKeyId.getIdentifier()));
                                            }
                                        } else {
                                            throw new IOException("Bad encoding in responderID element of OCSP response: expected ASN.1 context specific tag 0 or 1");
                                        }
                                        DerValue derValue7 = derInputStream2.getDerValue();
                                        if (debug2 != null) {
                                            debug2.println("OCSP response produced at: " + derValue7.getGeneralizedTime());
                                        }
                                        DerValue[] sequence2 = derInputStream2.getSequence(1);
                                        this.singleResponseMap = new HashMap(sequence2.length);
                                        if (debug2 != null) {
                                            debug2.println("OCSP number of SingleResponses: " + sequence2.length);
                                        }
                                        for (DerValue singleResponse : sequence2) {
                                            SingleResponse singleResponse2 = new SingleResponse(singleResponse);
                                            this.singleResponseMap.put(singleResponse2.getCertId(), singleResponse2);
                                        }
                                        if (derInputStream2.available() > 0) {
                                            DerValue derValue8 = derInputStream2.getDerValue();
                                            if (derValue8.isContextSpecific((byte) 1)) {
                                                DerValue[] sequence3 = derValue8.data.getSequence(3);
                                                for (DerValue extension : sequence3) {
                                                    Extension extension2 = new Extension(extension);
                                                    Debug debug3 = debug;
                                                    if (debug3 != null) {
                                                        debug3.println("OCSP extension: " + extension2);
                                                    }
                                                    if (extension2.getExtensionId().equals((Object) OCSP.NONCE_EXTENSION_OID)) {
                                                        bArr2 = extension2.getExtensionValue();
                                                    } else if (extension2.isCritical()) {
                                                        throw new IOException("Unsupported OCSP critical extension: " + extension2.getExtensionId());
                                                    }
                                                }
                                            }
                                        }
                                        this.responseNonce = bArr2;
                                        this.sigAlgId = AlgorithmId.parse(sequence[1]);
                                        this.signature = sequence[2].getBitString();
                                        if (sequence.length > 3) {
                                            DerValue derValue9 = sequence[3];
                                            if (derValue9.isContextSpecific((byte) 0)) {
                                                DerValue[] sequence4 = derValue9.getData().getSequence(3);
                                                this.certs = new ArrayList(sequence4.length);
                                                while (i < sequence4.length) {
                                                    try {
                                                        X509CertImpl x509CertImpl = new X509CertImpl(sequence4[i].toByteArray());
                                                        this.certs.add(x509CertImpl);
                                                        Debug debug4 = debug;
                                                        if (debug4 != null) {
                                                            debug4.println("OCSP response cert #" + (i + 1) + ": " + x509CertImpl.getSubjectX500Principal());
                                                        }
                                                        i++;
                                                    } catch (CertificateException e) {
                                                        throw new IOException("Bad encoding in X509 Certificate", e);
                                                    }
                                                }
                                                return;
                                            }
                                            throw new IOException("Bad encoding in certs element of OCSP response: expected ASN.1 context specific tag 0.");
                                        }
                                        this.certs = new ArrayList();
                                        return;
                                    }
                                    throw new IOException("Bad encoding in tbsResponseData element of OCSP response: expected ASN.1 SEQUENCE tag.");
                                }
                                throw new IOException("Unexpected BasicOCSPResponse value");
                            }
                            if (debug2 != null) {
                                debug2.println("OCSP response type: " + oid);
                            }
                            throw new IOException("Unsupported OCSP response type: " + oid);
                        }
                        throw new IOException("Bad encoding in responseBytes element of OCSP response: expected ASN.1 SEQUENCE tag.");
                    }
                    throw new IOException("Bad encoding in responseBytes element of OCSP response: expected ASN.1 context specific tag 0.");
                }
            }
            throw new IOException("Unknown OCSPResponse status: " + enumerated);
        }
        throw new IOException("Bad encoding in OCSP response: expected ASN.1 SEQUENCE tag.");
    }

    /* renamed from: sun.security.provider.certpath.OCSPResponse$1 */
    static /* synthetic */ class C48211 {

        /* renamed from: $SwitchMap$sun$security$provider$certpath$OCSPResponse$ResponseStatus */
        static final /* synthetic */ int[] f914x3ad85ed1;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                sun.security.provider.certpath.OCSPResponse$ResponseStatus[] r0 = sun.security.provider.certpath.OCSPResponse.ResponseStatus.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f914x3ad85ed1 = r0
                sun.security.provider.certpath.OCSPResponse$ResponseStatus r1 = sun.security.provider.certpath.OCSPResponse.ResponseStatus.SUCCESSFUL     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f914x3ad85ed1     // Catch:{ NoSuchFieldError -> 0x001d }
                sun.security.provider.certpath.OCSPResponse$ResponseStatus r1 = sun.security.provider.certpath.OCSPResponse.ResponseStatus.TRY_LATER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f914x3ad85ed1     // Catch:{ NoSuchFieldError -> 0x0028 }
                sun.security.provider.certpath.OCSPResponse$ResponseStatus r1 = sun.security.provider.certpath.OCSPResponse.ResponseStatus.INTERNAL_ERROR     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f914x3ad85ed1     // Catch:{ NoSuchFieldError -> 0x0033 }
                sun.security.provider.certpath.OCSPResponse$ResponseStatus r1 = sun.security.provider.certpath.OCSPResponse.ResponseStatus.UNAUTHORIZED     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.certpath.OCSPResponse.C48211.<clinit>():void");
        }
    }

    /* access modifiers changed from: package-private */
    public void verify(List<CertId> list, X509Certificate x509Certificate, X509Certificate x509Certificate2, Date date, byte[] bArr) throws CertPathValidatorException {
        String str;
        byte[] bArr2;
        Debug debug2;
        int i = C48211.f914x3ad85ed1[this.responseStatus.ordinal()];
        if (i == 1) {
            for (CertId next : list) {
                SingleResponse singleResponse = getSingleResponse(next);
                if (singleResponse == null) {
                    Debug debug3 = debug;
                    if (debug3 != null) {
                        debug3.println("No response found for CertId: " + next);
                    }
                    throw new CertPathValidatorException("OCSP response does not include a response for a certificate supplied in the OCSP request");
                }
                Debug debug4 = debug;
                if (debug4 != null) {
                    debug4.println("Status of certificate (with serial number " + next.getSerialNumber() + ") is: " + singleResponse.getCertStatus());
                }
            }
            if (this.signerCert == null) {
                try {
                    this.certs.add(X509CertImpl.toImpl(x509Certificate));
                    if (x509Certificate2 != null) {
                        this.certs.add(X509CertImpl.toImpl(x509Certificate2));
                    }
                    if (this.responderName != null) {
                        Iterator<X509CertImpl> it = this.certs.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            X509CertImpl next2 = it.next();
                            if (next2.getSubjectX500Principal().equals(this.responderName)) {
                                this.signerCert = next2;
                                break;
                            }
                        }
                    } else if (this.responderKeyId != null) {
                        Iterator<X509CertImpl> it2 = this.certs.iterator();
                        while (true) {
                            if (it2.hasNext()) {
                                X509CertImpl next3 = it2.next();
                                KeyIdentifier subjectKeyId = next3.getSubjectKeyId();
                                if (subjectKeyId != null && this.responderKeyId.equals(subjectKeyId)) {
                                    this.signerCert = next3;
                                    break;
                                }
                                try {
                                    subjectKeyId = new KeyIdentifier(next3.getPublicKey());
                                } catch (IOException unused) {
                                }
                                if (this.responderKeyId.equals(subjectKeyId)) {
                                    this.signerCert = next3;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                } catch (CertificateException e) {
                    throw new CertPathValidatorException("Invalid issuer or trusted responder certificate", e);
                }
            }
            X509CertImpl x509CertImpl = this.signerCert;
            if (x509CertImpl != null) {
                if (x509CertImpl.equals(x509Certificate)) {
                    Debug debug5 = debug;
                    if (debug5 != null) {
                        debug5.println("OCSP response is signed by the target's Issuing CA");
                    }
                } else if (this.signerCert.equals(x509Certificate2)) {
                    Debug debug6 = debug;
                    if (debug6 != null) {
                        debug6.println("OCSP response is signed by a Trusted Responder");
                    }
                } else if (this.signerCert.getIssuerX500Principal().equals(x509Certificate.getSubjectX500Principal())) {
                    try {
                        List<String> extendedKeyUsage = this.signerCert.getExtendedKeyUsage();
                        if (extendedKeyUsage == null || !extendedKeyUsage.contains(KP_OCSP_SIGNING_OID)) {
                            throw new CertPathValidatorException("Responder's certificate not valid for signing OCSP responses");
                        }
                        AlgorithmChecker algorithmChecker = new AlgorithmChecker(new TrustAnchor(x509Certificate, (byte[]) null));
                        algorithmChecker.init(false);
                        algorithmChecker.check((Certificate) this.signerCert, (Collection<String>) Collections.emptySet());
                        if (date == null) {
                            try {
                                this.signerCert.checkValidity();
                            } catch (CertificateException e2) {
                                throw new CertPathValidatorException("Responder's certificate not within the validity period", e2);
                            }
                        } else {
                            this.signerCert.checkValidity(date);
                        }
                        if (!(this.signerCert.getExtension(PKIXExtensions.OCSPNoCheck_Id) == null || (debug2 = debug) == null)) {
                            debug2.println("Responder's certificate includes the extension id-pkix-ocsp-nocheck.");
                        }
                        try {
                            this.signerCert.verify(x509Certificate.getPublicKey());
                            Debug debug7 = debug;
                            if (debug7 != null) {
                                debug7.println("OCSP response is signed by an Authorized Responder");
                            }
                        } catch (GeneralSecurityException unused2) {
                            this.signerCert = null;
                        }
                    } catch (CertificateParsingException e3) {
                        throw new CertPathValidatorException("Responder's certificate not valid for signing OCSP responses", e3);
                    }
                } else {
                    throw new CertPathValidatorException("Responder's certificate is not authorized to sign OCSP responses");
                }
            }
            X509CertImpl x509CertImpl2 = this.signerCert;
            if (x509CertImpl2 != null) {
                AlgorithmChecker.check(x509CertImpl2.getPublicKey(), this.sigAlgId);
                if (!verifySignature(this.signerCert)) {
                    throw new CertPathValidatorException("Error verifying OCSP Response's signature");
                } else if (bArr == null || (bArr2 = this.responseNonce) == null || Arrays.equals(bArr, bArr2)) {
                    long currentTimeMillis = date == null ? System.currentTimeMillis() : date.getTime();
                    int i2 = MAX_CLOCK_SKEW;
                    Date date2 = new Date(((long) i2) + currentTimeMillis);
                    Date date3 = new Date(currentTimeMillis - ((long) i2));
                    for (SingleResponse next4 : this.singleResponseMap.values()) {
                        Debug debug8 = debug;
                        if (debug8 != null) {
                            if (next4.nextUpdate != null) {
                                str = " until " + next4.nextUpdate;
                            } else {
                                str = "";
                            }
                            debug8.println("OCSP response validity interval is from " + next4.thisUpdate + str);
                            StringBuilder sb = new StringBuilder("Checking validity of OCSP response on: ");
                            sb.append((Object) new Date(currentTimeMillis));
                            debug8.println(sb.toString());
                        }
                        if (!date2.before(next4.thisUpdate)) {
                            if (date3.after(next4.nextUpdate != null ? next4.nextUpdate : next4.thisUpdate)) {
                            }
                        }
                        throw new CertPathValidatorException("Response is unreliable: its validity interval is out-of-date");
                    }
                } else {
                    throw new CertPathValidatorException("Nonces don't match");
                }
            } else {
                throw new CertPathValidatorException("Unable to verify OCSP Response's signature");
            }
        } else if (i == 2 || i == 3) {
            throw new CertPathValidatorException("OCSP response error: " + this.responseStatus, (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
        } else {
            throw new CertPathValidatorException("OCSP response error: " + this.responseStatus);
        }
    }

    /* access modifiers changed from: package-private */
    public ResponseStatus getResponseStatus() {
        return this.responseStatus;
    }

    private boolean verifySignature(X509Certificate x509Certificate) throws CertPathValidatorException {
        try {
            Signature instance = Signature.getInstance(this.sigAlgId.getName());
            instance.initVerify(x509Certificate.getPublicKey());
            instance.update(this.tbsResponseData);
            if (instance.verify(this.signature)) {
                Debug debug2 = debug;
                if (debug2 == null) {
                    return true;
                }
                debug2.println("Verified signature of OCSP Response");
                return true;
            }
            Debug debug3 = debug;
            if (debug3 == null) {
                return false;
            }
            debug3.println("Error verifying signature of OCSP Response");
            return false;
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            throw new CertPathValidatorException(e);
        }
    }

    /* access modifiers changed from: package-private */
    public SingleResponse getSingleResponse(CertId certId) {
        return this.singleResponseMap.get(certId);
    }

    /* access modifiers changed from: package-private */
    public X509Certificate getSignerCertificate() {
        return this.signerCert;
    }

    static final class SingleResponse implements OCSP.RevocationStatus {
        private final CertId certId;
        private final OCSP.RevocationStatus.CertStatus certStatus;
        /* access modifiers changed from: private */
        public final Date nextUpdate;
        private final CRLReason revocationReason;
        private final Date revocationTime;
        private final Map<String, java.security.cert.Extension> singleExtensions;
        /* access modifiers changed from: private */
        public final Date thisUpdate;

        private SingleResponse(DerValue derValue) throws IOException {
            if (derValue.tag == 48) {
                DerInputStream derInputStream = derValue.data;
                this.certId = new CertId(derInputStream.getDerValue().data);
                DerValue derValue2 = derInputStream.getDerValue();
                short s = (short) ((byte) (derValue2.tag & 31));
                if (s == 1) {
                    this.certStatus = OCSP.RevocationStatus.CertStatus.REVOKED;
                    Date generalizedTime = derValue2.data.getGeneralizedTime();
                    this.revocationTime = generalizedTime;
                    if (derValue2.data.available() != 0) {
                        DerValue derValue3 = derValue2.data.getDerValue();
                        if (((short) ((byte) (derValue3.tag & 31))) == 0) {
                            int enumerated = derValue3.data.getEnumerated();
                            if (enumerated < 0 || enumerated >= OCSPResponse.values.length) {
                                this.revocationReason = CRLReason.UNSPECIFIED;
                            } else {
                                this.revocationReason = OCSPResponse.values[enumerated];
                            }
                        } else {
                            this.revocationReason = CRLReason.UNSPECIFIED;
                        }
                    } else {
                        this.revocationReason = CRLReason.UNSPECIFIED;
                    }
                    if (OCSPResponse.debug != null) {
                        Debug r0 = OCSPResponse.debug;
                        r0.println("Revocation time: " + generalizedTime);
                        Debug r02 = OCSPResponse.debug;
                        r02.println("Revocation reason: " + this.revocationReason);
                    }
                } else {
                    this.revocationTime = null;
                    this.revocationReason = CRLReason.UNSPECIFIED;
                    if (s == 0) {
                        this.certStatus = OCSP.RevocationStatus.CertStatus.GOOD;
                    } else if (s == 2) {
                        this.certStatus = OCSP.RevocationStatus.CertStatus.UNKNOWN;
                    } else {
                        throw new IOException("Invalid certificate status");
                    }
                }
                this.thisUpdate = derInputStream.getGeneralizedTime();
                if (derInputStream.available() == 0) {
                    this.nextUpdate = null;
                } else {
                    DerValue derValue4 = derInputStream.getDerValue();
                    if (((short) ((byte) (derValue4.tag & 31))) == 0) {
                        this.nextUpdate = derValue4.data.getGeneralizedTime();
                        if (derInputStream.available() != 0) {
                            byte b = derInputStream.getDerValue().tag;
                        }
                    } else {
                        this.nextUpdate = null;
                    }
                }
                if (derInputStream.available() > 0) {
                    DerValue derValue5 = derInputStream.getDerValue();
                    if (derValue5.isContextSpecific((byte) 1)) {
                        DerValue[] sequence = derValue5.data.getSequence(3);
                        this.singleExtensions = new HashMap(sequence.length);
                        int i = 0;
                        while (i < sequence.length) {
                            Extension extension = new Extension(sequence[i]);
                            if (OCSPResponse.debug != null) {
                                Debug r2 = OCSPResponse.debug;
                                r2.println("OCSP single extension: " + extension);
                            }
                            if (!extension.isCritical()) {
                                this.singleExtensions.put(extension.getId(), extension);
                                i++;
                            } else {
                                throw new IOException("Unsupported OCSP critical extension: " + extension.getExtensionId());
                            }
                        }
                        return;
                    }
                    this.singleExtensions = Collections.emptyMap();
                    return;
                }
                this.singleExtensions = Collections.emptyMap();
                return;
            }
            throw new IOException("Bad ASN.1 encoding in SingleResponse");
        }

        public OCSP.RevocationStatus.CertStatus getCertStatus() {
            return this.certStatus;
        }

        /* access modifiers changed from: private */
        public CertId getCertId() {
            return this.certId;
        }

        public Date getRevocationTime() {
            return (Date) this.revocationTime.clone();
        }

        public CRLReason getRevocationReason() {
            return this.revocationReason;
        }

        public Map<String, java.security.cert.Extension> getSingleExtensions() {
            return Collections.unmodifiableMap(this.singleExtensions);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("SingleResponse:  \n");
            sb.append((Object) this.certId);
            sb.append("\nCertStatus: " + this.certStatus + "\n");
            if (this.certStatus == OCSP.RevocationStatus.CertStatus.REVOKED) {
                sb.append("revocationTime is " + this.revocationTime + "\n");
                sb.append("revocationReason is " + this.revocationReason + "\n");
            }
            sb.append("thisUpdate is " + this.thisUpdate + "\n");
            if (this.nextUpdate != null) {
                sb.append("nextUpdate is " + this.nextUpdate + "\n");
            }
            return sb.toString();
        }
    }
}
