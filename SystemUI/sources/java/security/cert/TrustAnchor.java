package java.security.cert;

import java.p026io.IOException;
import java.security.PublicKey;
import javax.security.auth.x500.X500Principal;
import sun.security.x509.NameConstraintsExtension;

public class TrustAnchor {
    private final String caName;
    private final X500Principal caPrincipal;

    /* renamed from: nc */
    private NameConstraintsExtension f199nc;
    private byte[] ncBytes;
    private final PublicKey pubKey;
    private final X509Certificate trustedCert;

    public TrustAnchor(X509Certificate x509Certificate, byte[] bArr) {
        if (x509Certificate != null) {
            this.trustedCert = x509Certificate;
            this.pubKey = null;
            this.caName = null;
            this.caPrincipal = null;
            setNameConstraints(bArr);
            return;
        }
        throw new NullPointerException("the trustedCert parameter must be non-null");
    }

    public TrustAnchor(X500Principal x500Principal, PublicKey publicKey, byte[] bArr) {
        if (x500Principal == null || publicKey == null) {
            throw null;
        }
        this.trustedCert = null;
        this.caPrincipal = x500Principal;
        this.caName = x500Principal.getName();
        this.pubKey = publicKey;
        setNameConstraints(bArr);
    }

    public TrustAnchor(String str, PublicKey publicKey, byte[] bArr) {
        if (publicKey == null) {
            throw new NullPointerException("the pubKey parameter must be non-null");
        } else if (str == null) {
            throw new NullPointerException("the caName parameter must be non-null");
        } else if (str.length() != 0) {
            this.caPrincipal = new X500Principal(str);
            this.pubKey = publicKey;
            this.caName = str;
            this.trustedCert = null;
            setNameConstraints(bArr);
        } else {
            throw new IllegalArgumentException("the caName parameter must be a non-empty String");
        }
    }

    public final X509Certificate getTrustedCert() {
        return this.trustedCert;
    }

    public final X500Principal getCA() {
        return this.caPrincipal;
    }

    public final String getCAName() {
        return this.caName;
    }

    public final PublicKey getCAPublicKey() {
        return this.pubKey;
    }

    private void setNameConstraints(byte[] bArr) {
        if (bArr == null) {
            this.ncBytes = null;
            this.f199nc = null;
            return;
        }
        this.ncBytes = (byte[]) bArr.clone();
        try {
            this.f199nc = new NameConstraintsExtension(Boolean.FALSE, (Object) bArr);
        } catch (IOException e) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException(e.getMessage());
            illegalArgumentException.initCause(e);
            throw illegalArgumentException;
        }
    }

    public final byte[] getNameConstraints() {
        byte[] bArr = this.ncBytes;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        if (this.pubKey != null) {
            sb.append("  Trusted CA Public Key: " + this.pubKey.toString() + "\n");
            sb.append("  Trusted CA Issuer Name: " + String.valueOf((Object) this.caName) + "\n");
        } else {
            sb.append("  Trusted CA cert: " + this.trustedCert.toString() + "\n");
        }
        if (this.f199nc != null) {
            sb.append("  Name Constraints: " + this.f199nc.toString() + "\n");
        }
        return sb.toString();
    }
}
