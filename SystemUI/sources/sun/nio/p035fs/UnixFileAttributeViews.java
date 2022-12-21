package sun.nio.p035fs;

import java.nio.file.ProviderMismatchException;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.p026io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import sun.nio.p035fs.AbstractBasicFileAttributeView;
import sun.nio.p035fs.UnixUserPrincipals;

/* renamed from: sun.nio.fs.UnixFileAttributeViews */
class UnixFileAttributeViews {
    UnixFileAttributeViews() {
    }

    /* renamed from: sun.nio.fs.UnixFileAttributeViews$Basic */
    static class Basic extends AbstractBasicFileAttributeView {
        protected final UnixPath file;
        protected final boolean followLinks;

        Basic(UnixPath unixPath, boolean z) {
            this.file = unixPath;
            this.followLinks = z;
        }

        public BasicFileAttributes readAttributes() throws IOException {
            this.file.checkRead();
            try {
                return UnixFileAttributes.get(this.file, this.followLinks).asBasicFileAttributes();
            } catch (UnixException e) {
                e.rethrowAsIOException(this.file);
                return null;
            }
        }

        public void setTimes(FileTime fileTime, FileTime fileTime2, FileTime fileTime3) throws IOException {
            boolean z;
            if (fileTime != null || fileTime2 != null) {
                this.file.checkWrite();
                int openForAttributeAccess = this.file.openForAttributeAccess(this.followLinks);
                if (fileTime == null || fileTime2 == null) {
                    try {
                        UnixFileAttributes unixFileAttributes = UnixFileAttributes.get(openForAttributeAccess);
                        if (fileTime == null) {
                            fileTime = unixFileAttributes.lastModifiedTime();
                        }
                        if (fileTime2 == null) {
                            fileTime2 = unixFileAttributes.lastAccessTime();
                        }
                    } catch (UnixException e) {
                        e.rethrowAsIOException(this.file);
                    } catch (Throwable th) {
                        UnixNativeDispatcher.close(openForAttributeAccess);
                        throw th;
                    }
                }
                long j = fileTime.mo61298to(TimeUnit.MICROSECONDS);
                long j2 = fileTime2.mo61298to(TimeUnit.MICROSECONDS);
                try {
                    if (UnixNativeDispatcher.futimesSupported()) {
                        UnixNativeDispatcher.futimes(openForAttributeAccess, j2, j);
                    } else {
                        UnixNativeDispatcher.utimes(this.file, j2, j);
                    }
                } catch (UnixException e2) {
                    if (e2.errno() != UnixConstants.EINVAL || (j >= 0 && j2 >= 0)) {
                        e2.rethrowAsIOException(this.file);
                    } else {
                        z = true;
                    }
                }
                z = false;
                if (z) {
                    if (j < 0) {
                        j = 0;
                    }
                    if (j2 < 0) {
                        j2 = 0;
                    }
                    try {
                        if (UnixNativeDispatcher.futimesSupported()) {
                            UnixNativeDispatcher.futimes(openForAttributeAccess, j2, j);
                        } else {
                            UnixNativeDispatcher.utimes(this.file, j2, j);
                        }
                    } catch (UnixException e3) {
                        e3.rethrowAsIOException(this.file);
                    }
                }
                UnixNativeDispatcher.close(openForAttributeAccess);
            }
        }
    }

    /* renamed from: sun.nio.fs.UnixFileAttributeViews$Posix */
    private static class Posix extends Basic implements PosixFileAttributeView {
        private static final String GROUP_NAME = "group";
        private static final String OWNER_NAME = "owner";
        private static final String PERMISSIONS_NAME = "permissions";
        static final Set<String> posixAttributeNames = Util.newSet(basicAttributeNames, PERMISSIONS_NAME, OWNER_NAME, "group");

        public String name() {
            return "posix";
        }

        Posix(UnixPath unixPath, boolean z) {
            super(unixPath, z);
        }

        /* access modifiers changed from: package-private */
        public final void checkReadExtended() {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                this.file.checkRead();
                securityManager.checkPermission(new RuntimePermission("accessUserInformation"));
            }
        }

        /* access modifiers changed from: package-private */
        public final void checkWriteExtended() {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                this.file.checkWrite();
                securityManager.checkPermission(new RuntimePermission("accessUserInformation"));
            }
        }

        public void setAttribute(String str, Object obj) throws IOException {
            if (str.equals(PERMISSIONS_NAME)) {
                setPermissions((Set) obj);
            } else if (str.equals(OWNER_NAME)) {
                setOwner((UserPrincipal) obj);
            } else if (str.equals("group")) {
                setGroup((GroupPrincipal) obj);
            } else {
                super.setAttribute(str, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public final void addRequestedPosixAttributes(PosixFileAttributes posixFileAttributes, AbstractBasicFileAttributeView.AttributesBuilder attributesBuilder) {
            addRequestedBasicAttributes(posixFileAttributes, attributesBuilder);
            if (attributesBuilder.match(PERMISSIONS_NAME)) {
                attributesBuilder.add(PERMISSIONS_NAME, posixFileAttributes.permissions());
            }
            if (attributesBuilder.match(OWNER_NAME)) {
                attributesBuilder.add(OWNER_NAME, posixFileAttributes.owner());
            }
            if (attributesBuilder.match("group")) {
                attributesBuilder.add("group", posixFileAttributes.group());
            }
        }

        public Map<String, Object> readAttributes(String[] strArr) throws IOException {
            AbstractBasicFileAttributeView.AttributesBuilder create = AbstractBasicFileAttributeView.AttributesBuilder.create(posixAttributeNames, strArr);
            addRequestedPosixAttributes(readAttributes(), create);
            return create.unmodifiableMap();
        }

        public UnixFileAttributes readAttributes() throws IOException {
            checkReadExtended();
            try {
                return UnixFileAttributes.get(this.file, this.followLinks);
            } catch (UnixException e) {
                e.rethrowAsIOException(this.file);
                return null;
            }
        }

        /* access modifiers changed from: package-private */
        public final void setMode(int i) throws IOException {
            int openForAttributeAccess;
            checkWriteExtended();
            try {
                if (this.followLinks) {
                    UnixNativeDispatcher.chmod(this.file, i);
                    return;
                }
                openForAttributeAccess = this.file.openForAttributeAccess(false);
                UnixNativeDispatcher.fchmod(openForAttributeAccess, i);
                UnixNativeDispatcher.close(openForAttributeAccess);
            } catch (UnixException e) {
                e.rethrowAsIOException(this.file);
            } catch (Throwable th) {
                UnixNativeDispatcher.close(openForAttributeAccess);
                throw th;
            }
        }

        /* access modifiers changed from: package-private */
        public final void setOwners(int i, int i2) throws IOException {
            checkWriteExtended();
            try {
                if (this.followLinks) {
                    UnixNativeDispatcher.chown(this.file, i, i2);
                } else {
                    UnixNativeDispatcher.lchown(this.file, i, i2);
                }
            } catch (UnixException e) {
                e.rethrowAsIOException(this.file);
            }
        }

        public void setPermissions(Set<PosixFilePermission> set) throws IOException {
            setMode(UnixFileModeAttribute.toUnixMode(set));
        }

        public void setOwner(UserPrincipal userPrincipal) throws IOException {
            if (userPrincipal == null) {
                throw new NullPointerException("'owner' is null");
            } else if (!(userPrincipal instanceof UnixUserPrincipals.User)) {
                throw new ProviderMismatchException();
            } else if (!(userPrincipal instanceof UnixUserPrincipals.Group)) {
                setOwners(((UnixUserPrincipals.User) userPrincipal).uid(), -1);
            } else {
                throw new IOException("'owner' parameter can't be a group");
            }
        }

        public UserPrincipal getOwner() throws IOException {
            return readAttributes().owner();
        }

        public void setGroup(GroupPrincipal groupPrincipal) throws IOException {
            if (groupPrincipal == null) {
                throw new NullPointerException("'owner' is null");
            } else if (groupPrincipal instanceof UnixUserPrincipals.Group) {
                setOwners(-1, ((UnixUserPrincipals.Group) groupPrincipal).gid());
            } else {
                throw new ProviderMismatchException();
            }
        }
    }

    /* renamed from: sun.nio.fs.UnixFileAttributeViews$Unix */
    private static class Unix extends Posix {
        private static final String CTIME_NAME = "ctime";
        private static final String DEV_NAME = "dev";
        private static final String GID_NAME = "gid";
        private static final String INO_NAME = "ino";
        private static final String MODE_NAME = "mode";
        private static final String NLINK_NAME = "nlink";
        private static final String RDEV_NAME = "rdev";
        private static final String UID_NAME = "uid";
        static final Set<String> unixAttributeNames = Util.newSet(posixAttributeNames, MODE_NAME, INO_NAME, "dev", RDEV_NAME, NLINK_NAME, "uid", GID_NAME, CTIME_NAME);

        public String name() {
            return "unix";
        }

        Unix(UnixPath unixPath, boolean z) {
            super(unixPath, z);
        }

        public void setAttribute(String str, Object obj) throws IOException {
            if (str.equals(MODE_NAME)) {
                setMode(((Integer) obj).intValue());
            } else if (str.equals("uid")) {
                setOwners(((Integer) obj).intValue(), -1);
            } else if (str.equals(GID_NAME)) {
                setOwners(-1, ((Integer) obj).intValue());
            } else {
                super.setAttribute(str, obj);
            }
        }

        public Map<String, Object> readAttributes(String[] strArr) throws IOException {
            AbstractBasicFileAttributeView.AttributesBuilder create = AbstractBasicFileAttributeView.AttributesBuilder.create(unixAttributeNames, strArr);
            UnixFileAttributes readAttributes = readAttributes();
            addRequestedPosixAttributes(readAttributes, create);
            if (create.match(MODE_NAME)) {
                create.add(MODE_NAME, Integer.valueOf(readAttributes.mode()));
            }
            if (create.match(INO_NAME)) {
                create.add(INO_NAME, Long.valueOf(readAttributes.ino()));
            }
            if (create.match("dev")) {
                create.add("dev", Long.valueOf(readAttributes.dev()));
            }
            if (create.match(RDEV_NAME)) {
                create.add(RDEV_NAME, Long.valueOf(readAttributes.rdev()));
            }
            if (create.match(NLINK_NAME)) {
                create.add(NLINK_NAME, Integer.valueOf(readAttributes.nlink()));
            }
            if (create.match("uid")) {
                create.add("uid", Integer.valueOf(readAttributes.uid()));
            }
            if (create.match(GID_NAME)) {
                create.add(GID_NAME, Integer.valueOf(readAttributes.gid()));
            }
            if (create.match(CTIME_NAME)) {
                create.add(CTIME_NAME, readAttributes.ctime());
            }
            return create.unmodifiableMap();
        }
    }

    static Basic createBasicView(UnixPath unixPath, boolean z) {
        return new Basic(unixPath, z);
    }

    static Posix createPosixView(UnixPath unixPath, boolean z) {
        return new Posix(unixPath, z);
    }

    static Unix createUnixView(UnixPath unixPath, boolean z) {
        return new Unix(unixPath, z);
    }

    static FileOwnerAttributeViewImpl createOwnerView(UnixPath unixPath, boolean z) {
        return new FileOwnerAttributeViewImpl((PosixFileAttributeView) createPosixView(unixPath, z));
    }
}
