package sun.nio.p035fs;

import java.nio.file.FileStore;
import java.nio.file.WatchService;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* renamed from: sun.nio.fs.LinuxFileSystem */
class LinuxFileSystem extends UnixFileSystem {
    LinuxFileSystem(UnixFileSystemProvider unixFileSystemProvider, String str) {
        super(unixFileSystemProvider, str);
    }

    public WatchService newWatchService() throws IOException {
        return new LinuxWatchService(this);
    }

    /* renamed from: sun.nio.fs.LinuxFileSystem$SupportedFileFileAttributeViewsHolder */
    private static class SupportedFileFileAttributeViewsHolder {
        static final Set<String> supportedFileAttributeViews = supportedFileAttributeViews();

        private SupportedFileFileAttributeViewsHolder() {
        }

        private static Set<String> supportedFileAttributeViews() {
            HashSet hashSet = new HashSet();
            hashSet.addAll(UnixFileSystem.standardFileAttributeViews());
            hashSet.add("dos");
            hashSet.add("user");
            return Collections.unmodifiableSet(hashSet);
        }
    }

    public Set<String> supportedFileAttributeViews() {
        return SupportedFileFileAttributeViewsHolder.supportedFileAttributeViews;
    }

    /* access modifiers changed from: package-private */
    public void copyNonPosixAttributes(int i, int i2) {
        LinuxUserDefinedFileAttributeView.copyExtendedAttributes(i, i2);
    }

    /* access modifiers changed from: package-private */
    public Iterable<UnixMountEntry> getMountEntries(String str) {
        long j;
        ArrayList arrayList = new ArrayList();
        try {
            j = LinuxNativeDispatcher.setmntent(Util.toBytes(str), Util.toBytes("r"));
            while (true) {
                UnixMountEntry unixMountEntry = new UnixMountEntry();
                if (LinuxNativeDispatcher.getmntent(j, unixMountEntry) < 0) {
                    break;
                }
                arrayList.add(unixMountEntry);
            }
            LinuxNativeDispatcher.endmntent(j);
        } catch (UnixException unused) {
        } catch (Throwable th) {
            LinuxNativeDispatcher.endmntent(j);
            throw th;
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public Iterable<UnixMountEntry> getMountEntries() {
        return getMountEntries("/proc/mounts");
    }

    /* access modifiers changed from: package-private */
    public FileStore getFileStore(UnixMountEntry unixMountEntry) throws IOException {
        return new LinuxFileStore(this, unixMountEntry);
    }
}
