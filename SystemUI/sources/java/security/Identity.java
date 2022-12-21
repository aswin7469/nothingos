package java.security;

import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

@Deprecated(forRemoval = true, since = "1.2")
public abstract class Identity implements Principal, Serializable {
    private static final long serialVersionUID = 3609922007826600659L;
    Vector<Certificate> certificates;
    String info;
    private String name;
    private PublicKey publicKey;
    IdentityScope scope;

    protected Identity() {
        this("restoring...");
    }

    public Identity(String str, IdentityScope identityScope) throws KeyManagementException {
        this(str);
        if (identityScope != null) {
            identityScope.addIdentity(this);
        }
        this.scope = identityScope;
    }

    public Identity(String str) {
        this.info = "No further information available.";
        this.name = str;
    }

    public final String getName() {
        return this.name;
    }

    public final IdentityScope getScope() {
        return this.scope;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(PublicKey publicKey2) throws KeyManagementException {
        check("setIdentityPublicKey");
        this.publicKey = publicKey2;
        this.certificates = new Vector<>();
    }

    public void setInfo(String str) {
        check("setIdentityInfo");
        this.info = str;
    }

    public String getInfo() {
        return this.info;
    }

    public void addCertificate(Certificate certificate) throws KeyManagementException {
        check("addIdentityCertificate");
        if (this.certificates == null) {
            this.certificates = new Vector<>();
        }
        PublicKey publicKey2 = this.publicKey;
        if (publicKey2 == null) {
            this.publicKey = certificate.getPublicKey();
        } else if (!keyEquals(publicKey2, certificate.getPublicKey())) {
            throw new KeyManagementException("public key different from cert public key");
        }
        this.certificates.addElement(certificate);
    }

    private boolean keyEquals(PublicKey publicKey2, PublicKey publicKey3) {
        String format = publicKey2.getFormat();
        String format2 = publicKey3.getFormat();
        boolean z = true;
        boolean z2 = format == null;
        if (format2 != null) {
            z = false;
        }
        if (z ^ z2) {
            return false;
        }
        if (format == null || format2 == null || format.equalsIgnoreCase(format2)) {
            return Arrays.equals(publicKey2.getEncoded(), publicKey3.getEncoded());
        }
        return false;
    }

    public void removeCertificate(Certificate certificate) throws KeyManagementException {
        check("removeIdentityCertificate");
        Vector<Certificate> vector = this.certificates;
        if (vector == null) {
            return;
        }
        if (certificate == null || !vector.contains(certificate)) {
            throw new KeyManagementException();
        }
        this.certificates.removeElement(certificate);
    }

    public Certificate[] certificates() {
        Vector<Certificate> vector = this.certificates;
        if (vector == null) {
            return new Certificate[0];
        }
        Certificate[] certificateArr = new Certificate[vector.size()];
        this.certificates.copyInto(certificateArr);
        return certificateArr;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Identity)) {
            return false;
        }
        Identity identity = (Identity) obj;
        if (fullName().equals(identity.fullName())) {
            return true;
        }
        return identityEquals(identity);
    }

    /* access modifiers changed from: protected */
    public boolean identityEquals(Identity identity) {
        if (!this.name.equalsIgnoreCase(identity.name)) {
            return false;
        }
        PublicKey publicKey2 = this.publicKey;
        boolean z = publicKey2 == null;
        PublicKey publicKey3 = identity.publicKey;
        if (z ^ (publicKey3 == null)) {
            return false;
        }
        if (publicKey2 == null || publicKey3 == null || publicKey2.equals(publicKey3)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public String fullName() {
        String str = this.name;
        if (this.scope == null) {
            return str;
        }
        return str + BaseIconCache.EMPTY_CLASS_NAME + this.scope.getName();
    }

    public String toString() {
        check("printIdentity");
        String str = this.name;
        if (this.scope == null) {
            return str;
        }
        return str + NavigationBarInflaterView.SIZE_MOD_START + this.scope.getName() + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public String toString(boolean z) {
        String identity = toString();
        if (!z) {
            return identity;
        }
        String str = ((identity + "\n") + printKeys()) + "\n" + printCertificates();
        if (this.info != null) {
            return str + "\n\t" + this.info;
        }
        return str + "\n\tno additional information available.";
    }

    /* access modifiers changed from: package-private */
    public String printKeys() {
        return this.publicKey != null ? "\tpublic key initialized" : "\tno public key";
    }

    /* access modifiers changed from: package-private */
    public String printCertificates() {
        Vector<Certificate> vector = this.certificates;
        if (vector == null) {
            return "\tno certificates";
        }
        Iterator<Certificate> it = vector.iterator();
        int i = 1;
        String str = "\tcertificates: \n";
        while (it.hasNext()) {
            Certificate next = it.next();
            str = (str + "\tcertificate " + i + "\tfor  : " + next.getPrincipal() + "\n") + "\t\t\tfrom : " + next.getGuarantor() + "\n";
            i++;
        }
        return str;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    private static void check(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSecurityAccess(str);
        }
    }
}
