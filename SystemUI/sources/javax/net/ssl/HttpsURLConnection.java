package javax.net.ssl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public abstract class HttpsURLConnection extends HttpURLConnection {
    private static final String OK_HOSTNAME_VERIFIER_CLASS = "com.android.okhttp.internal.tls.OkHostnameVerifier";
    private static SSLSocketFactory defaultSSLSocketFactory;
    protected HostnameVerifier hostnameVerifier = NoPreloadHolder.defaultHostnameVerifier;
    private SSLSocketFactory sslSocketFactory = getDefaultSSLSocketFactory();

    public abstract String getCipherSuite();

    public abstract Certificate[] getLocalCertificates();

    public abstract Certificate[] getServerCertificates() throws SSLPeerUnverifiedException;

    protected HttpsURLConnection(URL url) {
        super(url);
    }

    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        return ((X509Certificate) getServerCertificates()[0]).getSubjectX500Principal();
    }

    public Principal getLocalPrincipal() {
        Certificate[] localCertificates = getLocalCertificates();
        if (localCertificates != null) {
            return ((X509Certificate) localCertificates[0]).getSubjectX500Principal();
        }
        return null;
    }

    private static class NoPreloadHolder {
        public static HostnameVerifier defaultHostnameVerifier;

        private NoPreloadHolder() {
        }

        static {
            try {
                defaultHostnameVerifier = (HostnameVerifier) Class.forName(HttpsURLConnection.OK_HOSTNAME_VERIFIER_CLASS).getField("INSTANCE").get((Object) null);
            } catch (Exception e) {
                throw new AssertionError("Failed to obtain okhttp HostnameVerifier", e);
            }
        }
    }

    public static void setDefaultHostnameVerifier(HostnameVerifier hostnameVerifier2) {
        if (hostnameVerifier2 != null) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new SSLPermission("setHostnameVerifier"));
            }
            NoPreloadHolder.defaultHostnameVerifier = hostnameVerifier2;
            return;
        }
        throw new IllegalArgumentException("no default HostnameVerifier specified");
    }

    public static HostnameVerifier getDefaultHostnameVerifier() {
        return NoPreloadHolder.defaultHostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier2) {
        if (hostnameVerifier2 != null) {
            this.hostnameVerifier = hostnameVerifier2;
            return;
        }
        throw new IllegalArgumentException("no HostnameVerifier specified");
    }

    public static HostnameVerifier getStrictHostnameVerifier() {
        try {
            return (HostnameVerifier) Class.forName(OK_HOSTNAME_VERIFIER_CLASS).getMethod("strictInstance", new Class[0]).invoke((Object) null, new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public static void setDefaultSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        if (sSLSocketFactory != null) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkSetFactory();
            }
            defaultSSLSocketFactory = sSLSocketFactory;
            return;
        }
        throw new IllegalArgumentException("no default SSLSocketFactory specified");
    }

    public static SSLSocketFactory getDefaultSSLSocketFactory() {
        if (defaultSSLSocketFactory == null) {
            defaultSSLSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
        return defaultSSLSocketFactory;
    }

    public void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        if (sSLSocketFactory != null) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkSetFactory();
            }
            this.sslSocketFactory = sSLSocketFactory;
            return;
        }
        throw new IllegalArgumentException("no SSLSocketFactory specified");
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return this.sslSocketFactory;
    }
}
