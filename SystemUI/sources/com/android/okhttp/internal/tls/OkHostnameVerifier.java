package com.android.okhttp.internal.tls;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public final class OkHostnameVerifier implements HostnameVerifier {
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;
    private static final char DEL = '';
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier(false);
    private static final Pattern VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
    private final boolean strictWildcardMode;

    private OkHostnameVerifier(boolean z) {
        this.strictWildcardMode = z;
    }

    public static OkHostnameVerifier strictInstance() {
        return new OkHostnameVerifier(true);
    }

    public boolean verify(String str, SSLSession sSLSession) {
        try {
            return verify(str, (X509Certificate) sSLSession.getPeerCertificates()[0]);
        } catch (SSLException unused) {
            return false;
        }
    }

    public boolean verify(String str, X509Certificate x509Certificate) {
        if (verifyAsIpAddress(str)) {
            return verifyIpAddress(str, x509Certificate);
        }
        return verifyHostName(str, x509Certificate);
    }

    static boolean verifyAsIpAddress(String str) {
        return VERIFY_AS_IP_ADDRESS.matcher(str).matches();
    }

    private boolean verifyIpAddress(String str, X509Certificate x509Certificate) {
        List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        int size = subjectAltNames.size();
        for (int i = 0; i < size; i++) {
            if (str.equalsIgnoreCase(subjectAltNames.get(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyHostName(String str, X509Certificate x509Certificate) {
        if (!isPrintableAscii(str)) {
            return false;
        }
        String lowerCase = str.toLowerCase(Locale.f700US);
        List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 2);
        int size = subjectAltNames.size();
        for (int i = 0; i < size; i++) {
            if (verifyHostName(lowerCase, subjectAltNames.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static List<String> allSubjectAltNames(X509Certificate x509Certificate) {
        List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        List<String> subjectAltNames2 = getSubjectAltNames(x509Certificate, 2);
        ArrayList arrayList = new ArrayList(subjectAltNames.size() + subjectAltNames2.size());
        arrayList.addAll(subjectAltNames);
        arrayList.addAll(subjectAltNames2);
        return arrayList;
    }

    private static List<String> getSubjectAltNames(X509Certificate x509Certificate, int i) {
        String str;
        ArrayList arrayList = new ArrayList();
        try {
            Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            if (subjectAlternativeNames == null) {
                return Collections.emptyList();
            }
            for (List next : subjectAlternativeNames) {
                if (next != null) {
                    if (next.size() >= 2) {
                        Integer num = (Integer) next.get(0);
                        if (num != null) {
                            if (num.intValue() == i && (str = (String) next.get(1)) != null) {
                                arrayList.add(str);
                            }
                        }
                    }
                }
            }
            return arrayList;
        } catch (CertificateParsingException unused) {
            return Collections.emptyList();
        }
    }

    private boolean verifyHostName(String str, String str2) {
        if (str != null && str.length() != 0 && !str.startsWith(BaseIconCache.EMPTY_CLASS_NAME) && !str.endsWith("..") && str2 != null && str2.length() != 0 && !str2.startsWith(BaseIconCache.EMPTY_CLASS_NAME) && !str2.endsWith("..")) {
            if (!str.endsWith(BaseIconCache.EMPTY_CLASS_NAME)) {
                str = str + '.';
            }
            if (!str2.endsWith(BaseIconCache.EMPTY_CLASS_NAME)) {
                str2 = str2 + '.';
            }
            if (!isPrintableAscii(str2)) {
                return false;
            }
            String lowerCase = str2.toLowerCase(Locale.f700US);
            if (!lowerCase.contains("*")) {
                return str.equals(lowerCase);
            }
            if (!lowerCase.startsWith("*.") || lowerCase.indexOf(42, 1) != -1 || str.length() < lowerCase.length() || "*.".equals(lowerCase)) {
                return false;
            }
            if (this.strictWildcardMode && lowerCase.substring(2, lowerCase.length() - 1).indexOf(46) < 0) {
                return false;
            }
            String substring = lowerCase.substring(1);
            if (!str.endsWith(substring)) {
                return false;
            }
            int length = str.length() - substring.length();
            if (length <= 0 || str.lastIndexOf(46, length - 1) == -1) {
                return true;
            }
            return false;
        }
        return false;
    }

    static boolean isPrintableAscii(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (c <= ' ' || c >= 127) {
                return false;
            }
        }
        return true;
    }
}
