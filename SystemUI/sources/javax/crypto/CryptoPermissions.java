package javax.crypto;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import javax.crypto.CryptoPolicyParser;

final class CryptoPermissions extends PermissionCollection implements Serializable {
    public void add(Permission permission) {
    }

    public Enumeration elements() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public CryptoPermissions getMinimum(CryptoPermissions cryptoPermissions) {
        return null;
    }

    /* access modifiers changed from: package-private */
    public PermissionCollection getPermissionCollection(String str) {
        return null;
    }

    public boolean implies(Permission permission) {
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isEmpty() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public void load(InputStream inputStream) throws IOException, CryptoPolicyParser.ParsingException {
    }

    CryptoPermissions() {
    }
}
