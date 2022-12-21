package java.p026io;

import java.security.Permission;

/* renamed from: java.io.FilePermission */
public final class FilePermission extends Permission implements Serializable {
    public String getActions() {
        return null;
    }

    public boolean implies(Permission permission) {
        return true;
    }

    public FilePermission(String str, String str2) {
        super(str);
    }
}
