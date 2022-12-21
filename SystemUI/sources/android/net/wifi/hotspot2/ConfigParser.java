package android.net.wifi.hotspot2;

import android.net.wifi.hotspot2.omadm.PpsMoParser;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.charset.StandardCharsets;
import java.p026io.ByteArrayInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.InputStreamReader;
import java.p026io.LineNumberReader;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ConfigParser {
    private static final String BOUNDARY = "boundary=";
    private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String ENCODING_BASE64 = "base64";
    private static final String TAG = "ConfigParser";
    private static final String TYPE_CA_CERT = "application/x-x509-ca-cert";
    private static final String TYPE_MULTIPART_MIXED = "multipart/mixed";
    private static final String TYPE_PASSPOINT_PROFILE = "application/x-passpoint-profile";
    private static final String TYPE_PKCS12 = "application/x-pkcs12";
    private static final String TYPE_WIFI_CONFIG = "application/x-wifi-config";

    private static class MimePart {
        public byte[] data;
        public boolean isLast;
        public String type;

        private MimePart() {
            this.type = null;
            this.data = null;
            this.isLast = false;
        }
    }

    private static class MimeHeader {
        public String boundary;
        public String contentType;
        public String encodingType;

        private MimeHeader() {
            this.contentType = null;
            this.boundary = null;
            this.encodingType = null;
        }
    }

    public static PasspointConfiguration parsePasspointConfig(String str, byte[] bArr) {
        if (!TextUtils.equals(str, TYPE_WIFI_CONFIG)) {
            Log.e(TAG, "Unexpected MIME type: " + str);
            return null;
        }
        try {
            return createPasspointConfig(parseMimeMultipartMessage(new LineNumberReader(new InputStreamReader((InputStream) new ByteArrayInputStream(Base64.decode(new String(bArr, StandardCharsets.ISO_8859_1), 0)), StandardCharsets.ISO_8859_1))));
        } catch (IOException | IllegalArgumentException e) {
            Log.e(TAG, "Failed to parse installation file: " + e.getMessage());
            return null;
        }
    }

    private static PasspointConfiguration createPasspointConfig(Map<String, byte[]> map) throws IOException {
        byte[] bArr = map.get(TYPE_PASSPOINT_PROFILE);
        if (bArr != null) {
            PasspointConfiguration parseMoText = PpsMoParser.parseMoText(new String(bArr));
            if (parseMoText == null) {
                throw new IOException("Failed to parse Passpoint profile");
            } else if (parseMoText.getCredential() != null) {
                if (parseMoText.getUpdateIdentifier() != Integer.MIN_VALUE) {
                    parseMoText.setUpdateIdentifier(Integer.MIN_VALUE);
                }
                byte[] bArr2 = map.get(TYPE_CA_CERT);
                if (bArr2 != null) {
                    try {
                        parseMoText.getCredential().setCaCertificate(parseCACert(bArr2));
                    } catch (CertificateException unused) {
                        throw new IOException("Failed to parse CA Certificate");
                    }
                }
                byte[] bArr3 = map.get(TYPE_PKCS12);
                if (bArr3 != null) {
                    try {
                        Pair<PrivateKey, List<X509Certificate>> parsePkcs12 = parsePkcs12(bArr3);
                        parseMoText.getCredential().setClientPrivateKey((PrivateKey) parsePkcs12.first);
                        parseMoText.getCredential().setClientCertificateChain((X509Certificate[]) ((List) parsePkcs12.second).toArray(new X509Certificate[((List) parsePkcs12.second).size()]));
                    } catch (IOException | GeneralSecurityException e) {
                        throw new IOException("Failed to parse PKCS12 string: " + e.getMessage());
                    }
                }
                return parseMoText;
            } else {
                throw new IOException("Passpoint profile missing credential");
            }
        } else {
            throw new IOException("Missing Passpoint Profile");
        }
    }

    private static Map<String, byte[]> parseMimeMultipartMessage(LineNumberReader lineNumberReader) throws IOException {
        String readLine;
        MimePart parseMimePart;
        MimeHeader parseHeaders = parseHeaders(lineNumberReader);
        if (!TextUtils.equals(parseHeaders.contentType, TYPE_MULTIPART_MIXED)) {
            throw new IOException("Invalid content type: " + parseHeaders.contentType);
        } else if (TextUtils.isEmpty(parseHeaders.boundary)) {
            throw new IOException("Missing boundary string");
        } else if (TextUtils.equals(parseHeaders.encodingType, ENCODING_BASE64)) {
            do {
                readLine = lineNumberReader.readLine();
                if (readLine != null) {
                } else {
                    throw new IOException("Unexpected EOF before first boundary @ " + lineNumberReader.getLineNumber());
                }
            } while (!readLine.equals("--" + parseHeaders.boundary));
            HashMap hashMap = new HashMap();
            do {
                parseMimePart = parseMimePart(lineNumberReader, parseHeaders.boundary);
                hashMap.put(parseMimePart.type, parseMimePart.data);
            } while (!parseMimePart.isLast);
            return hashMap;
        } else {
            throw new IOException("Unexpected encoding: " + parseHeaders.encodingType);
        }
    }

    private static MimePart parseMimePart(LineNumberReader lineNumberReader, String str) throws IOException {
        MimeHeader parseHeaders = parseHeaders(lineNumberReader);
        if (!TextUtils.equals(parseHeaders.encodingType, ENCODING_BASE64)) {
            throw new IOException("Unexpected encoding type: " + parseHeaders.encodingType);
        } else if (TextUtils.equals(parseHeaders.contentType, TYPE_PASSPOINT_PROFILE) || TextUtils.equals(parseHeaders.contentType, TYPE_CA_CERT) || TextUtils.equals(parseHeaders.contentType, TYPE_PKCS12)) {
            StringBuilder sb = new StringBuilder();
            String str2 = "--" + str;
            String str3 = str2 + "--";
            while (true) {
                String readLine = lineNumberReader.readLine();
                if (readLine == null) {
                    throw new IOException("Unexpected EOF file in body @ " + lineNumberReader.getLineNumber());
                } else if (readLine.startsWith(str2)) {
                    boolean equals = readLine.equals(str3);
                    MimePart mimePart = new MimePart();
                    mimePart.type = parseHeaders.contentType;
                    mimePart.data = Base64.decode(sb.toString(), 0);
                    mimePart.isLast = equals;
                    return mimePart;
                } else {
                    sb.append(readLine);
                }
            }
        } else {
            throw new IOException("Unexpected content type: " + parseHeaders.contentType);
        }
    }

    private static MimeHeader parseHeaders(LineNumberReader lineNumberReader) throws IOException {
        MimeHeader mimeHeader = new MimeHeader();
        for (Map.Entry next : readHeaders(lineNumberReader).entrySet()) {
            String str = (String) next.getKey();
            str.hashCode();
            if (str.equals(CONTENT_TRANSFER_ENCODING)) {
                mimeHeader.encodingType = (String) next.getValue();
            } else if (!str.equals(CONTENT_TYPE)) {
                Log.d(TAG, "Ignore header: " + ((String) next.getKey()));
            } else {
                Pair<String, String> parseContentType = parseContentType((String) next.getValue());
                mimeHeader.contentType = (String) parseContentType.first;
                mimeHeader.boundary = (String) parseContentType.second;
            }
        }
        return mimeHeader;
    }

    private static Pair<String, String> parseContentType(String str) throws IOException {
        String[] split = str.split(NavigationBarInflaterView.GRAVITY_SEPARATOR);
        if (split.length >= 1) {
            String trim = split[0].trim();
            String str2 = null;
            for (int i = 1; i < split.length; i++) {
                String trim2 = split[i].trim();
                if (!trim2.startsWith(BOUNDARY)) {
                    Log.d(TAG, "Ignore Content-Type attribute: " + split[i]);
                } else {
                    str2 = trim2.substring(9);
                    if (str2.length() > 1 && str2.startsWith("\"") && str2.endsWith("\"")) {
                        str2 = str2.substring(1, str2.length() - 1);
                    }
                }
            }
            return new Pair<>(trim, str2);
        }
        throw new IOException("Invalid Content-Type: " + str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x00a5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.Map<java.lang.String, java.lang.String> readHeaders(java.p026io.LineNumberReader r8) throws java.p026io.IOException {
        /*
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r1 = 0
            r2 = r1
        L_0x0007:
            java.lang.String r3 = r8.readLine()
            if (r3 == 0) goto L_0x00ad
            int r4 = r3.length()
            if (r4 == 0) goto L_0x00a3
            java.lang.String r4 = r3.trim()
            int r4 = r4.length()
            if (r4 != 0) goto L_0x001f
            goto L_0x00a3
        L_0x001f:
            r4 = 58
            int r4 = r3.indexOf((int) r4)
            java.lang.String r5 = "' @ "
            if (r4 >= 0) goto L_0x0056
            if (r2 == 0) goto L_0x0038
            r4 = 32
            r2.append((char) r4)
            java.lang.String r3 = r3.trim()
            r2.append((java.lang.String) r3)
            goto L_0x0007
        L_0x0038:
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Bad header line: '"
            r1.<init>((java.lang.String) r2)
            r1.append((java.lang.String) r3)
            r1.append((java.lang.String) r5)
            int r8 = r8.getLineNumber()
            r1.append((int) r8)
            java.lang.String r8 = r1.toString()
            r0.<init>((java.lang.String) r8)
            throw r0
        L_0x0056:
            r6 = 0
            char r7 = r3.charAt(r6)
            boolean r7 = java.lang.Character.isWhitespace((char) r7)
            if (r7 != 0) goto L_0x0085
            if (r1 == 0) goto L_0x006a
            java.lang.String r2 = r2.toString()
            r0.put(r1, r2)
        L_0x006a:
            java.lang.String r1 = r3.substring(r6, r4)
            java.lang.String r1 = r1.trim()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            int r4 = r4 + 1
            java.lang.String r3 = r3.substring(r4)
            java.lang.String r3 = r3.trim()
            r2.append((java.lang.String) r3)
            goto L_0x0007
        L_0x0085:
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Illegal blank prefix in header line '"
            r1.<init>((java.lang.String) r2)
            r1.append((java.lang.String) r3)
            r1.append((java.lang.String) r5)
            int r8 = r8.getLineNumber()
            r1.append((int) r8)
            java.lang.String r8 = r1.toString()
            r0.<init>((java.lang.String) r8)
            throw r0
        L_0x00a3:
            if (r1 == 0) goto L_0x00ac
            java.lang.String r8 = r2.toString()
            r0.put(r1, r8)
        L_0x00ac:
            return r0
        L_0x00ad:
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Missing line @ "
            r1.<init>((java.lang.String) r2)
            int r8 = r8.getLineNumber()
            r1.append((int) r8)
            java.lang.String r8 = r1.toString()
            r0.<init>((java.lang.String) r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.hotspot2.ConfigParser.readHeaders(java.io.LineNumberReader):java.util.Map");
    }

    private static X509Certificate parseCACert(byte[] bArr) throws CertificateException {
        return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bArr));
    }

    private static Pair<PrivateKey, List<X509Certificate>> parsePkcs12(byte[] bArr) throws GeneralSecurityException, IOException {
        KeyStore instance = KeyStore.getInstance("PKCS12");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        int i = 0;
        instance.load(byteArrayInputStream, new char[0]);
        byteArrayInputStream.close();
        if (instance.size() == 1) {
            String nextElement = instance.aliases().nextElement();
            if (nextElement != null) {
                ArrayList arrayList = null;
                PrivateKey privateKey = (PrivateKey) instance.getKey(nextElement, (char[]) null);
                Certificate[] certificateChain = instance.getCertificateChain(nextElement);
                if (certificateChain != null) {
                    arrayList = new ArrayList();
                    int length = certificateChain.length;
                    while (i < length) {
                        Certificate certificate = certificateChain[i];
                        if (certificate instanceof X509Certificate) {
                            arrayList.add((X509Certificate) certificate);
                            i++;
                        } else {
                            throw new IOException("Unexpceted certificate type: " + certificate.getClass());
                        }
                    }
                }
                return new Pair<>(privateKey, arrayList);
            }
            throw new IOException("No alias found");
        }
        throw new IOException("Unexpected key size: " + instance.size());
    }
}
