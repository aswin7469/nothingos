package sun.security.provider.certpath;

import java.p026io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import sun.security.util.Debug;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.KeyIdentifier;
import sun.security.x509.SubjectKeyIdentifierExtension;
import sun.security.x509.X509CertImpl;

public class Vertex {
    private static final Debug debug = Debug.getInstance("certpath");
    private X509Certificate cert;
    private int index = -1;
    private Throwable throwable;

    Vertex(X509Certificate x509Certificate) {
        this.cert = x509Certificate;
    }

    public X509Certificate getCertificate() {
        return this.cert;
    }

    public int getIndex() {
        return this.index;
    }

    /* access modifiers changed from: package-private */
    public void setIndex(int i) {
        this.index = i;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    /* access modifiers changed from: package-private */
    public void setThrowable(Throwable th) {
        this.throwable = th;
    }

    public String toString() {
        return certToString() + throwableToString() + indexToString();
    }

    public String certToString() {
        StringBuilder sb = new StringBuilder();
        try {
            X509CertImpl impl = X509CertImpl.toImpl(this.cert);
            sb.append("Issuer:     ");
            sb.append((Object) impl.getIssuerX500Principal());
            sb.append("\nSubject:    ");
            sb.append((Object) impl.getSubjectX500Principal());
            sb.append("\nSerialNum:  ");
            sb.append(impl.getSerialNumber().toString(16));
            sb.append("\nExpires:    ");
            sb.append(impl.getNotAfter().toString());
            sb.append("\n");
            boolean[] issuerUniqueID = impl.getIssuerUniqueID();
            if (issuerUniqueID != null) {
                sb.append("IssuerUID:  ");
                for (boolean z : issuerUniqueID) {
                    sb.append(z ? 1 : 0);
                }
                sb.append("\n");
            }
            boolean[] subjectUniqueID = impl.getSubjectUniqueID();
            if (subjectUniqueID != null) {
                sb.append("SubjectUID: ");
                for (boolean z2 : subjectUniqueID) {
                    sb.append(z2 ? 1 : 0);
                }
                sb.append("\n");
            }
            try {
                SubjectKeyIdentifierExtension subjectKeyIdentifierExtension = impl.getSubjectKeyIdentifierExtension();
                if (subjectKeyIdentifierExtension != null) {
                    KeyIdentifier keyIdentifier = subjectKeyIdentifierExtension.get("key_id");
                    sb.append("SubjKeyID:  ");
                    sb.append(keyIdentifier.toString());
                }
                AuthorityKeyIdentifierExtension authorityKeyIdentifierExtension = impl.getAuthorityKeyIdentifierExtension();
                if (authorityKeyIdentifierExtension != null) {
                    sb.append("AuthKeyID:  ");
                    sb.append(((KeyIdentifier) authorityKeyIdentifierExtension.get("key_id")).toString());
                }
            } catch (IOException e) {
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("Vertex.certToString() unexpected exception");
                    e.printStackTrace();
                }
            }
            return sb.toString();
        } catch (CertificateException e2) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("Vertex.certToString() unexpected exception");
                e2.printStackTrace();
            }
            return sb.toString();
        }
    }

    public String throwableToString() {
        StringBuilder sb = new StringBuilder("Exception:  ");
        Throwable th = this.throwable;
        if (th != null) {
            sb.append(th.toString());
        } else {
            sb.append("null");
        }
        sb.append("\n");
        return sb.toString();
    }

    public String moreToString() {
        StringBuilder sb = new StringBuilder("Last cert?  ");
        sb.append(this.index == -1 ? "Yes" : "No");
        sb.append("\n");
        return sb.toString();
    }

    public String indexToString() {
        return "Index:      " + this.index + "\n";
    }
}
