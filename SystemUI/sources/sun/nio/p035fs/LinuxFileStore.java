package sun.nio.p035fs;

import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.p026io.IOException;
import java.util.Arrays;
import sun.nio.p035fs.UnixFileStore;

/* renamed from: sun.nio.fs.LinuxFileStore */
class LinuxFileStore extends UnixFileStore {
    private volatile boolean xattrChecked;
    private volatile boolean xattrEnabled;

    LinuxFileStore(UnixPath unixPath) throws IOException {
        super(unixPath);
    }

    LinuxFileStore(UnixFileSystem unixFileSystem, UnixMountEntry unixMountEntry) throws IOException {
        super(unixFileSystem, unixMountEntry);
    }

    /* access modifiers changed from: package-private */
    public UnixMountEntry findMountEntry() throws IOException {
        UnixPath unixPath;
        UnixPath unixPath2;
        UnixFileAttributes unixFileAttributes;
        LinuxFileSystem linuxFileSystem = (LinuxFileSystem) file().getFileSystem();
        try {
            unixPath = new UnixPath((UnixFileSystem) linuxFileSystem, UnixNativeDispatcher.realpath(file()));
        } catch (UnixException e) {
            e.rethrowAsIOException(file());
            unixPath = null;
        }
        UnixPath parent = unixPath.getParent();
        while (true) {
            UnixPath unixPath3 = unixPath;
            unixPath = parent;
            unixPath2 = unixPath3;
            if (unixPath == null) {
                break;
            }
            try {
                unixFileAttributes = UnixFileAttributes.get(unixPath, true);
            } catch (UnixException e2) {
                e2.rethrowAsIOException(unixPath);
                unixFileAttributes = null;
            }
            if (unixFileAttributes.dev() != dev()) {
                break;
            }
            parent = unixPath.getParent();
        }
        byte[] asByteArray = unixPath2.asByteArray();
        for (UnixMountEntry next : linuxFileSystem.getMountEntries("/proc/mounts")) {
            if (Arrays.equals(asByteArray, next.dir())) {
                return next;
            }
        }
        throw new IOException("Mount point not found");
    }

    private boolean isExtendedAttributesEnabled(UnixPath unixPath) {
        int openForAttributeAccess;
        try {
            openForAttributeAccess = unixPath.openForAttributeAccess(false);
            try {
                LinuxNativeDispatcher.fgetxattr(openForAttributeAccess, Util.toBytes("user.java"), 0, 0);
                UnixNativeDispatcher.close(openForAttributeAccess);
                return true;
            } catch (UnixException e) {
                if (e.errno() == UnixConstants.ENODATA) {
                    UnixNativeDispatcher.close(openForAttributeAccess);
                    return true;
                }
                UnixNativeDispatcher.close(openForAttributeAccess);
                return false;
            }
        } catch (IOException unused) {
        } catch (Throwable th) {
            UnixNativeDispatcher.close(openForAttributeAccess);
            throw th;
        }
    }

    public boolean supportsFileAttributeView(Class<? extends FileAttributeView> cls) {
        if (cls == DosFileAttributeView.class || cls == UserDefinedFileAttributeView.class) {
            UnixFileStore.FeatureStatus checkIfFeaturePresent = checkIfFeaturePresent("user_xattr");
            if (checkIfFeaturePresent == UnixFileStore.FeatureStatus.PRESENT) {
                return true;
            }
            if (checkIfFeaturePresent == UnixFileStore.FeatureStatus.NOT_PRESENT) {
                return false;
            }
            if (entry().hasOption("user_xattr")) {
                return true;
            }
            if (entry().fstype().equals("ext3") || entry().fstype().equals("ext4")) {
                return false;
            }
            if (!this.xattrChecked) {
                this.xattrEnabled = isExtendedAttributesEnabled(new UnixPath(file().getFileSystem(), entry().dir()));
                this.xattrChecked = true;
            }
            return this.xattrEnabled;
        } else if (cls != PosixFileAttributeView.class || !entry().fstype().equals("vfat")) {
            return super.supportsFileAttributeView(cls);
        } else {
            return false;
        }
    }

    public boolean supportsFileAttributeView(String str) {
        if (str.equals("dos")) {
            return supportsFileAttributeView((Class<? extends FileAttributeView>) DosFileAttributeView.class);
        }
        if (str.equals("user")) {
            return supportsFileAttributeView((Class<? extends FileAttributeView>) UserDefinedFileAttributeView.class);
        }
        return super.supportsFileAttributeView(str);
    }
}
