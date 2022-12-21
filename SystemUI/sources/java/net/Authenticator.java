package java.net;

public abstract class Authenticator {
    private static Authenticator theAuthenticator;
    private RequestorType requestingAuthType;
    private String requestingHost;
    private int requestingPort;
    private String requestingPrompt;
    private String requestingProtocol;
    private String requestingScheme;
    private InetAddress requestingSite;
    private URL requestingURL;

    public enum RequestorType {
        PROXY,
        SERVER
    }

    /* access modifiers changed from: protected */
    public PasswordAuthentication getPasswordAuthentication() {
        return null;
    }

    private void reset() {
        this.requestingHost = null;
        this.requestingSite = null;
        this.requestingPort = -1;
        this.requestingProtocol = null;
        this.requestingPrompt = null;
        this.requestingScheme = null;
        this.requestingURL = null;
        this.requestingAuthType = RequestorType.SERVER;
    }

    public static synchronized void setDefault(Authenticator authenticator) {
        synchronized (Authenticator.class) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new NetPermission("setDefaultAuthenticator"));
            }
            theAuthenticator = authenticator;
        }
    }

    public static PasswordAuthentication requestPasswordAuthentication(InetAddress inetAddress, int i, String str, String str2, String str3) {
        PasswordAuthentication passwordAuthentication;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new NetPermission("requestPasswordAuthentication"));
        }
        Authenticator authenticator = theAuthenticator;
        if (authenticator == null) {
            return null;
        }
        synchronized (authenticator) {
            authenticator.reset();
            authenticator.requestingSite = inetAddress;
            authenticator.requestingPort = i;
            authenticator.requestingProtocol = str;
            authenticator.requestingPrompt = str2;
            authenticator.requestingScheme = str3;
            passwordAuthentication = authenticator.getPasswordAuthentication();
        }
        return passwordAuthentication;
    }

    public static PasswordAuthentication requestPasswordAuthentication(String str, InetAddress inetAddress, int i, String str2, String str3, String str4) {
        PasswordAuthentication passwordAuthentication;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new NetPermission("requestPasswordAuthentication"));
        }
        Authenticator authenticator = theAuthenticator;
        if (authenticator == null) {
            return null;
        }
        synchronized (authenticator) {
            authenticator.reset();
            authenticator.requestingHost = str;
            authenticator.requestingSite = inetAddress;
            authenticator.requestingPort = i;
            authenticator.requestingProtocol = str2;
            authenticator.requestingPrompt = str3;
            authenticator.requestingScheme = str4;
            passwordAuthentication = authenticator.getPasswordAuthentication();
        }
        return passwordAuthentication;
    }

    public static PasswordAuthentication requestPasswordAuthentication(String str, InetAddress inetAddress, int i, String str2, String str3, String str4, URL url, RequestorType requestorType) {
        PasswordAuthentication passwordAuthentication;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new NetPermission("requestPasswordAuthentication"));
        }
        Authenticator authenticator = theAuthenticator;
        if (authenticator == null) {
            return null;
        }
        synchronized (authenticator) {
            authenticator.reset();
            authenticator.requestingHost = str;
            authenticator.requestingSite = inetAddress;
            authenticator.requestingPort = i;
            authenticator.requestingProtocol = str2;
            authenticator.requestingPrompt = str3;
            authenticator.requestingScheme = str4;
            authenticator.requestingURL = url;
            authenticator.requestingAuthType = requestorType;
            passwordAuthentication = authenticator.getPasswordAuthentication();
        }
        return passwordAuthentication;
    }

    /* access modifiers changed from: protected */
    public final String getRequestingHost() {
        return this.requestingHost;
    }

    /* access modifiers changed from: protected */
    public final InetAddress getRequestingSite() {
        return this.requestingSite;
    }

    /* access modifiers changed from: protected */
    public final int getRequestingPort() {
        return this.requestingPort;
    }

    /* access modifiers changed from: protected */
    public final String getRequestingProtocol() {
        return this.requestingProtocol;
    }

    /* access modifiers changed from: protected */
    public final String getRequestingPrompt() {
        return this.requestingPrompt;
    }

    /* access modifiers changed from: protected */
    public final String getRequestingScheme() {
        return this.requestingScheme;
    }

    /* access modifiers changed from: protected */
    public URL getRequestingURL() {
        return this.requestingURL;
    }

    /* access modifiers changed from: protected */
    public RequestorType getRequestorType() {
        return this.requestingAuthType;
    }
}
