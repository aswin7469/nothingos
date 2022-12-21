package sun.security.provider.certpath;

import java.net.URI;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.cert.CRLReason;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.Extension;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import sun.security.action.GetIntegerAction;
import sun.security.util.Debug;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AccessDescription;
import sun.security.x509.AuthorityInfoAccessExtension;
import sun.security.x509.GeneralName;
import sun.security.x509.URIName;
import sun.security.x509.X509CertImpl;

public final class OCSP {
    private static final int CONNECT_TIMEOUT = initializeTimeout();
    private static final int DEFAULT_CONNECT_TIMEOUT = 15000;
    static final ObjectIdentifier NONCE_EXTENSION_OID = ObjectIdentifier.newInternal(new int[]{1, 3, 6, 1, 5, 5, 7, 48, 1, 2});
    private static final Debug debug = Debug.getInstance("certpath");

    public interface RevocationStatus {

        public enum CertStatus {
            GOOD,
            REVOKED,
            UNKNOWN
        }

        CertStatus getCertStatus();

        CRLReason getRevocationReason();

        Date getRevocationTime();

        Map<String, Extension> getSingleExtensions();
    }

    private static int initializeTimeout() {
        Integer num = (Integer) AccessController.doPrivileged(new GetIntegerAction("com.sun.security.ocsp.timeout"));
        return (num == null || num.intValue() < 0) ? DEFAULT_CONNECT_TIMEOUT : num.intValue() * 1000;
    }

    private OCSP() {
    }

    public static RevocationStatus check(X509Certificate x509Certificate, X509Certificate x509Certificate2) throws IOException, CertPathValidatorException {
        try {
            X509CertImpl impl = X509CertImpl.toImpl(x509Certificate);
            URI responderURI = getResponderURI(impl);
            if (responderURI != null) {
                CertId certId = new CertId(x509Certificate2, impl.getSerialNumberObject());
                return check((List<CertId>) Collections.singletonList(certId), responderURI, x509Certificate2, (X509Certificate) null, (Date) null, (List<Extension>) Collections.emptyList()).getSingleResponse(certId);
            }
            throw new CertPathValidatorException("No OCSP Responder URI in certificate");
        } catch (IOException | CertificateException e) {
            throw new CertPathValidatorException("Exception while encoding OCSPRequest", e);
        }
    }

    public static RevocationStatus check(X509Certificate x509Certificate, X509Certificate x509Certificate2, URI uri, X509Certificate x509Certificate3, Date date) throws IOException, CertPathValidatorException {
        return check(x509Certificate, x509Certificate2, uri, x509Certificate3, date, (List<Extension>) Collections.emptyList());
    }

    public static RevocationStatus check(X509Certificate x509Certificate, X509Certificate x509Certificate2, URI uri, X509Certificate x509Certificate3, Date date, List<Extension> list) throws IOException, CertPathValidatorException {
        try {
            CertId certId = new CertId(x509Certificate2, X509CertImpl.toImpl(x509Certificate).getSerialNumberObject());
            return check((List<CertId>) Collections.singletonList(certId), uri, x509Certificate2, x509Certificate3, date, list).getSingleResponse(certId);
        } catch (IOException | CertificateException e) {
            throw new CertPathValidatorException("Exception while encoding OCSPRequest", e);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v14, resolved type: java.io.InputStream} */
    /* JADX WARNING: type inference failed for: r1v1, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v13 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00f8 A[SYNTHETIC, Splitter:B:60:0x00f8] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0100 A[SYNTHETIC, Splitter:B:65:0x0100] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static sun.security.provider.certpath.OCSPResponse check(java.util.List<sun.security.provider.certpath.CertId> r10, java.net.URI r11, java.security.cert.X509Certificate r12, java.security.cert.X509Certificate r13, java.util.Date r14, java.util.List<java.security.cert.Extension> r15) throws java.p026io.IOException, java.security.cert.CertPathValidatorException {
        /*
            java.lang.String r0 = "Received HTTP error: "
            java.lang.String r1 = "connecting to OCSP service at: "
            sun.security.provider.certpath.OCSPRequest r2 = new sun.security.provider.certpath.OCSPRequest     // Catch:{ IOException -> 0x0107 }
            r2.<init>(r10, r15)     // Catch:{ IOException -> 0x0107 }
            byte[] r15 = r2.encodeBytes()     // Catch:{ IOException -> 0x0107 }
            r3 = 0
            java.net.URL r11 = r11.toURL()     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            sun.security.util.Debug r4 = debug     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            if (r4 == 0) goto L_0x0025
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            r5.<init>((java.lang.String) r1)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            r5.append((java.lang.Object) r11)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            java.lang.String r1 = r5.toString()     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            r4.println(r1)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
        L_0x0025:
            java.net.URLConnection r11 = r11.openConnection()     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            java.net.HttpURLConnection r11 = (java.net.HttpURLConnection) r11     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            int r1 = CONNECT_TIMEOUT     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            r11.setConnectTimeout(r1)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            r11.setReadTimeout(r1)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            r1 = 1
            r11.setDoOutput(r1)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            r11.setDoInput(r1)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            java.lang.String r1 = "POST"
            r11.setRequestMethod(r1)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            java.lang.String r1 = "Content-type"
            java.lang.String r5 = "application/ocsp-request"
            r11.setRequestProperty(r1, r5)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            java.lang.String r1 = "Content-length"
            int r5 = r15.length     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            java.lang.String r5 = java.lang.String.valueOf((int) r5)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            r11.setRequestProperty(r1, r5)     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            java.io.OutputStream r1 = r11.getOutputStream()     // Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
            r1.write((byte[]) r15)     // Catch:{ IOException -> 0x00df }
            r1.flush()     // Catch:{ IOException -> 0x00df }
            if (r4 == 0) goto L_0x0083
            int r15 = r11.getResponseCode()     // Catch:{ IOException -> 0x00df }
            r5 = 200(0xc8, float:2.8E-43)
            if (r15 == r5) goto L_0x0083
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00df }
            r15.<init>((java.lang.String) r0)     // Catch:{ IOException -> 0x00df }
            int r0 = r11.getResponseCode()     // Catch:{ IOException -> 0x00df }
            r15.append((int) r0)     // Catch:{ IOException -> 0x00df }
            java.lang.String r0 = " - "
            r15.append((java.lang.String) r0)     // Catch:{ IOException -> 0x00df }
            java.lang.String r0 = r11.getResponseMessage()     // Catch:{ IOException -> 0x00df }
            r15.append((java.lang.String) r0)     // Catch:{ IOException -> 0x00df }
            java.lang.String r15 = r15.toString()     // Catch:{ IOException -> 0x00df }
            r4.println(r15)     // Catch:{ IOException -> 0x00df }
        L_0x0083:
            java.io.InputStream r3 = r11.getInputStream()     // Catch:{ IOException -> 0x00df }
            int r11 = r11.getContentLength()     // Catch:{ IOException -> 0x00df }
            r15 = -1
            if (r11 != r15) goto L_0x0091
            r11 = 2147483647(0x7fffffff, float:NaN)
        L_0x0091:
            r15 = 2048(0x800, float:2.87E-42)
            if (r11 <= r15) goto L_0x0096
            goto L_0x0097
        L_0x0096:
            r15 = r11
        L_0x0097:
            byte[] r15 = new byte[r15]     // Catch:{ IOException -> 0x00df }
            r0 = 0
        L_0x009a:
            if (r0 >= r11) goto L_0x00b2
            int r4 = r15.length     // Catch:{ IOException -> 0x00df }
            int r4 = r4 - r0
            int r4 = r3.read(r15, r0, r4)     // Catch:{ IOException -> 0x00df }
            if (r4 >= 0) goto L_0x00a5
            goto L_0x00b2
        L_0x00a5:
            int r0 = r0 + r4
            int r4 = r15.length     // Catch:{ IOException -> 0x00df }
            if (r0 < r4) goto L_0x009a
            if (r0 >= r11) goto L_0x009a
            int r4 = r0 * 2
            byte[] r15 = java.util.Arrays.copyOf((byte[]) r15, (int) r4)     // Catch:{ IOException -> 0x00df }
            goto L_0x009a
        L_0x00b2:
            byte[] r11 = java.util.Arrays.copyOf((byte[]) r15, (int) r0)     // Catch:{ IOException -> 0x00df }
            if (r3 == 0) goto L_0x00be
            r3.close()     // Catch:{ IOException -> 0x00bc }
            goto L_0x00be
        L_0x00bc:
            r10 = move-exception
            throw r10
        L_0x00be:
            if (r1 == 0) goto L_0x00c6
            r1.close()     // Catch:{ IOException -> 0x00c4 }
            goto L_0x00c6
        L_0x00c4:
            r10 = move-exception
            throw r10
        L_0x00c6:
            sun.security.provider.certpath.OCSPResponse r15 = new sun.security.provider.certpath.OCSPResponse     // Catch:{ IOException -> 0x00d8 }
            r15.<init>(r11)     // Catch:{ IOException -> 0x00d8 }
            byte[] r5 = r2.getNonce()
            r0 = r15
            r1 = r10
            r2 = r12
            r3 = r13
            r4 = r14
            r0.verify(r1, r2, r3, r4, r5)
            return r15
        L_0x00d8:
            r10 = move-exception
            java.security.cert.CertPathValidatorException r11 = new java.security.cert.CertPathValidatorException
            r11.<init>((java.lang.Throwable) r10)
            throw r11
        L_0x00df:
            r10 = move-exception
            r6 = r10
            goto L_0x00e8
        L_0x00e2:
            r10 = move-exception
            r1 = r3
            goto L_0x00f6
        L_0x00e5:
            r10 = move-exception
            r6 = r10
            r1 = r3
        L_0x00e8:
            java.security.cert.CertPathValidatorException r10 = new java.security.cert.CertPathValidatorException     // Catch:{ all -> 0x00f5 }
            java.lang.String r5 = "Unable to determine revocation status due to network error"
            r7 = 0
            r8 = -1
            java.security.cert.CertPathValidatorException$BasicReason r9 = java.security.cert.CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS     // Catch:{ all -> 0x00f5 }
            r4 = r10
            r4.<init>(r5, r6, r7, r8, r9)     // Catch:{ all -> 0x00f5 }
            throw r10     // Catch:{ all -> 0x00f5 }
        L_0x00f5:
            r10 = move-exception
        L_0x00f6:
            if (r3 == 0) goto L_0x00fe
            r3.close()     // Catch:{ IOException -> 0x00fc }
            goto L_0x00fe
        L_0x00fc:
            r10 = move-exception
            throw r10
        L_0x00fe:
            if (r1 == 0) goto L_0x0106
            r1.close()     // Catch:{ IOException -> 0x0104 }
            goto L_0x0106
        L_0x0104:
            r10 = move-exception
            throw r10
        L_0x0106:
            throw r10
        L_0x0107:
            r10 = move-exception
            java.security.cert.CertPathValidatorException r11 = new java.security.cert.CertPathValidatorException
            java.lang.String r12 = "Exception while encoding OCSPRequest"
            r11.<init>(r12, r10)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.certpath.OCSP.check(java.util.List, java.net.URI, java.security.cert.X509Certificate, java.security.cert.X509Certificate, java.util.Date, java.util.List):sun.security.provider.certpath.OCSPResponse");
    }

    public static URI getResponderURI(X509Certificate x509Certificate) {
        try {
            return getResponderURI(X509CertImpl.toImpl(x509Certificate));
        } catch (CertificateException unused) {
            return null;
        }
    }

    static URI getResponderURI(X509CertImpl x509CertImpl) {
        AuthorityInfoAccessExtension authorityInfoAccessExtension = x509CertImpl.getAuthorityInfoAccessExtension();
        if (authorityInfoAccessExtension == null) {
            return null;
        }
        for (AccessDescription next : authorityInfoAccessExtension.getAccessDescriptions()) {
            if (next.getAccessMethod().equals((Object) AccessDescription.Ad_OCSP_Id)) {
                GeneralName accessLocation = next.getAccessLocation();
                if (accessLocation.getType() == 6) {
                    return ((URIName) accessLocation.getName()).getURI();
                }
            }
        }
        return null;
    }
}
