package sun.security.x509;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.net.URI;
import java.net.URISyntaxException;
import java.p026io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class URIName implements GeneralNameInterface {
    private String host;
    private DNSName hostDNS;
    private IPAddressName hostIP;
    private URI uri;

    public int getType() {
        return 6;
    }

    public URIName(DerValue derValue) throws IOException {
        this(derValue.getIA5String());
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:15|16|17|18|30) */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0072, code lost:
        throw new java.p026io.IOException("invalid URI name (host portion is not a valid DNS name, IPv4 address, or IPv6 address):" + r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0055 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public URIName(java.lang.String r4) throws java.p026io.IOException {
        /*
            r3 = this;
            r3.<init>()
            java.net.URI r0 = new java.net.URI     // Catch:{ URISyntaxException -> 0x0088 }
            r0.<init>(r4)     // Catch:{ URISyntaxException -> 0x0088 }
            r3.uri = r0     // Catch:{ URISyntaxException -> 0x0088 }
            java.lang.String r0 = r0.getScheme()
            if (r0 == 0) goto L_0x0074
            java.net.URI r0 = r3.uri
            java.lang.String r0 = r0.getHost()
            r3.host = r0
            if (r0 == 0) goto L_0x0073
            r1 = 0
            char r0 = r0.charAt(r1)
            r1 = 91
            if (r0 != r1) goto L_0x004b
            java.lang.String r0 = r3.host
            int r1 = r0.length()
            r2 = 1
            int r1 = r1 - r2
            java.lang.String r0 = r0.substring(r2, r1)
            sun.security.x509.IPAddressName r1 = new sun.security.x509.IPAddressName     // Catch:{ IOException -> 0x0037 }
            r1.<init>((java.lang.String) r0)     // Catch:{ IOException -> 0x0037 }
            r3.hostIP = r1     // Catch:{ IOException -> 0x0037 }
            goto L_0x0073
        L_0x0037:
            java.io.IOException r3 = new java.io.IOException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "invalid URI name (host portion is not a valid IPv6 address):"
            r0.<init>((java.lang.String) r1)
            r0.append((java.lang.String) r4)
            java.lang.String r4 = r0.toString()
            r3.<init>((java.lang.String) r4)
            throw r3
        L_0x004b:
            sun.security.x509.DNSName r0 = new sun.security.x509.DNSName     // Catch:{ IOException -> 0x0055 }
            java.lang.String r1 = r3.host     // Catch:{ IOException -> 0x0055 }
            r0.<init>((java.lang.String) r1)     // Catch:{ IOException -> 0x0055 }
            r3.hostDNS = r0     // Catch:{ IOException -> 0x0055 }
            goto L_0x0073
        L_0x0055:
            sun.security.x509.IPAddressName r0 = new sun.security.x509.IPAddressName     // Catch:{ Exception -> 0x005f }
            java.lang.String r1 = r3.host     // Catch:{ Exception -> 0x005f }
            r0.<init>((java.lang.String) r1)     // Catch:{ Exception -> 0x005f }
            r3.hostIP = r0     // Catch:{ Exception -> 0x005f }
            goto L_0x0073
        L_0x005f:
            java.io.IOException r3 = new java.io.IOException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "invalid URI name (host portion is not a valid DNS name, IPv4 address, or IPv6 address):"
            r0.<init>((java.lang.String) r1)
            r0.append((java.lang.String) r4)
            java.lang.String r4 = r0.toString()
            r3.<init>((java.lang.String) r4)
            throw r3
        L_0x0073:
            return
        L_0x0074:
            java.io.IOException r3 = new java.io.IOException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "URI name must include scheme:"
            r0.<init>((java.lang.String) r1)
            r0.append((java.lang.String) r4)
            java.lang.String r4 = r0.toString()
            r3.<init>((java.lang.String) r4)
            throw r3
        L_0x0088:
            r3 = move-exception
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "invalid URI name:"
            r1.<init>((java.lang.String) r2)
            r1.append((java.lang.String) r4)
            java.lang.String r4 = r1.toString()
            r0.<init>(r4, r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.URIName.<init>(java.lang.String):void");
    }

    public static URIName nameConstraint(DerValue derValue) throws IOException {
        DNSName dNSName;
        String iA5String = derValue.getIA5String();
        try {
            URI uri2 = new URI(iA5String);
            if (uri2.getScheme() == null) {
                String schemeSpecificPart = uri2.getSchemeSpecificPart();
                try {
                    if (schemeSpecificPart.startsWith(BaseIconCache.EMPTY_CLASS_NAME)) {
                        dNSName = new DNSName(schemeSpecificPart.substring(1));
                    } else {
                        dNSName = new DNSName(schemeSpecificPart);
                    }
                    return new URIName(uri2, schemeSpecificPart, dNSName);
                } catch (IOException e) {
                    throw new IOException("invalid URI name constraint:" + iA5String, e);
                }
            } else {
                throw new IOException("invalid URI name constraint (should not include scheme):" + iA5String);
            }
        } catch (URISyntaxException e2) {
            throw new IOException("invalid URI name constraint:" + iA5String, e2);
        }
    }

    URIName(URI uri2, String str, DNSName dNSName) {
        this.uri = uri2;
        this.host = str;
        this.hostDNS = dNSName;
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putIA5String(this.uri.toASCIIString());
    }

    public String toString() {
        return "URIName: " + this.uri.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof URIName)) {
            return false;
        }
        return this.uri.equals(((URIName) obj).getURI());
    }

    public URI getURI() {
        return this.uri;
    }

    public String getName() {
        return this.uri.toString();
    }

    public String getScheme() {
        return this.uri.getScheme();
    }

    public String getHost() {
        return this.host;
    }

    public Object getHostObject() {
        IPAddressName iPAddressName = this.hostIP;
        if (iPAddressName != null) {
            return iPAddressName;
        }
        return this.hostDNS;
    }

    public int hashCode() {
        return this.uri.hashCode();
    }

    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        if (generalNameInterface == null || generalNameInterface.getType() != 6) {
            return -1;
        }
        URIName uRIName = (URIName) generalNameInterface;
        String host2 = uRIName.getHost();
        boolean z = false;
        if (host2.equalsIgnoreCase(this.host)) {
            return 0;
        }
        Object hostObject = uRIName.getHostObject();
        if (this.hostDNS == null || !(hostObject instanceof DNSName)) {
            return 3;
        }
        boolean z2 = this.host.charAt(0) == '.';
        if (host2.charAt(0) == '.') {
            z = true;
        }
        int constrains = this.hostDNS.constrains((DNSName) hostObject);
        if (!z2 && !z && (constrains == 2 || constrains == 1)) {
            constrains = 3;
        }
        if (z2 == z || constrains != 0) {
            return constrains;
        }
        return z2 ? 2 : 1;
    }

    public int subtreeDepth() throws UnsupportedOperationException {
        try {
            return new DNSName(this.host).subtreeDepth();
        } catch (IOException e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }
}
