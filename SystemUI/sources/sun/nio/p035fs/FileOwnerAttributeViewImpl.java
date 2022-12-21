package sun.nio.p035fs;

import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.p026io.IOException;
import java.util.HashMap;
import java.util.Map;

/* renamed from: sun.nio.fs.FileOwnerAttributeViewImpl */
final class FileOwnerAttributeViewImpl implements FileOwnerAttributeView, DynamicFileAttributeView {
    private static final String OWNER_NAME = "owner";
    private final boolean isPosixView = false;
    private final FileAttributeView view;

    public String name() {
        return OWNER_NAME;
    }

    FileOwnerAttributeViewImpl(PosixFileAttributeView posixFileAttributeView) {
        this.view = posixFileAttributeView;
    }

    FileOwnerAttributeViewImpl(AclFileAttributeView aclFileAttributeView) {
        this.view = aclFileAttributeView;
    }

    public void setAttribute(String str, Object obj) throws IOException {
        if (str.equals(OWNER_NAME)) {
            setOwner((UserPrincipal) obj);
            return;
        }
        throw new IllegalArgumentException("'" + name() + ":" + str + "' not recognized");
    }

    public Map<String, Object> readAttributes(String[] strArr) throws IOException {
        HashMap hashMap = new HashMap();
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            String str = strArr[i];
            if (str.equals("*") || str.equals(OWNER_NAME)) {
                hashMap.put(OWNER_NAME, getOwner());
                i++;
            } else {
                throw new IllegalArgumentException("'" + name() + ":" + str + "' not recognized");
            }
        }
        return hashMap;
    }

    public UserPrincipal getOwner() throws IOException {
        if (this.isPosixView) {
            return ((PosixFileAttributeView) this.view).readAttributes().owner();
        }
        return ((AclFileAttributeView) this.view).getOwner();
    }

    public void setOwner(UserPrincipal userPrincipal) throws IOException {
        if (this.isPosixView) {
            ((PosixFileAttributeView) this.view).setOwner(userPrincipal);
        } else {
            ((AclFileAttributeView) this.view).setOwner(userPrincipal);
        }
    }
}
