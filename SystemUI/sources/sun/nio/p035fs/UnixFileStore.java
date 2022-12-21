package sun.nio.p035fs;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.file.FileStore;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.p026io.IOException;
import java.util.Arrays;
import java.util.Properties;

/* renamed from: sun.nio.fs.UnixFileStore */
abstract class UnixFileStore extends FileStore {
    private static final Object loadLock = new Object();
    private static volatile Properties props;
    private final long dev;
    private final UnixMountEntry entry;
    private final UnixPath file;

    /* renamed from: sun.nio.fs.UnixFileStore$FeatureStatus */
    enum FeatureStatus {
        PRESENT,
        NOT_PRESENT,
        UNKNOWN
    }

    /* access modifiers changed from: package-private */
    public abstract UnixMountEntry findMountEntry() throws IOException;

    private static long devFor(UnixPath unixPath) throws IOException {
        try {
            return UnixFileAttributes.get(unixPath, true).dev();
        } catch (UnixException e) {
            e.rethrowAsIOException(unixPath);
            return 0;
        }
    }

    UnixFileStore(UnixPath unixPath) throws IOException {
        this.file = unixPath;
        this.dev = devFor(unixPath);
        this.entry = findMountEntry();
    }

    UnixFileStore(UnixFileSystem unixFileSystem, UnixMountEntry unixMountEntry) throws IOException {
        UnixPath unixPath = new UnixPath(unixFileSystem, unixMountEntry.dir());
        this.file = unixPath;
        this.dev = unixMountEntry.dev() == 0 ? devFor(unixPath) : unixMountEntry.dev();
        this.entry = unixMountEntry;
    }

    /* access modifiers changed from: package-private */
    public UnixPath file() {
        return this.file;
    }

    /* access modifiers changed from: package-private */
    public long dev() {
        return this.dev;
    }

    /* access modifiers changed from: package-private */
    public UnixMountEntry entry() {
        return this.entry;
    }

    public String name() {
        return this.entry.name();
    }

    public String type() {
        return this.entry.fstype();
    }

    public boolean isReadOnly() {
        return this.entry.isReadOnly();
    }

    private UnixFileStoreAttributes readAttributes() throws IOException {
        try {
            return UnixFileStoreAttributes.get(this.file);
        } catch (UnixException e) {
            e.rethrowAsIOException(this.file);
            return null;
        }
    }

    public long getTotalSpace() throws IOException {
        UnixFileStoreAttributes readAttributes = readAttributes();
        return readAttributes.blockSize() * readAttributes.totalBlocks();
    }

    public long getUsableSpace() throws IOException {
        UnixFileStoreAttributes readAttributes = readAttributes();
        return readAttributes.blockSize() * readAttributes.availableBlocks();
    }

    public long getBlockSize() throws IOException {
        return readAttributes().blockSize();
    }

    public long getUnallocatedSpace() throws IOException {
        UnixFileStoreAttributes readAttributes = readAttributes();
        return readAttributes.blockSize() * readAttributes.freeBlocks();
    }

    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> cls) {
        cls.getClass();
        FileStoreAttributeView fileStoreAttributeView = null;
        return null;
    }

    public Object getAttribute(String str) throws IOException {
        if (str.equals("totalSpace")) {
            return Long.valueOf(getTotalSpace());
        }
        if (str.equals("usableSpace")) {
            return Long.valueOf(getUsableSpace());
        }
        if (str.equals("unallocatedSpace")) {
            return Long.valueOf(getUnallocatedSpace());
        }
        throw new UnsupportedOperationException("'" + str + "' not recognized");
    }

    public boolean supportsFileAttributeView(Class<? extends FileAttributeView> cls) {
        cls.getClass();
        if (cls == BasicFileAttributeView.class) {
            return true;
        }
        if (cls != PosixFileAttributeView.class && cls != FileOwnerAttributeView.class) {
            return false;
        }
        if (checkIfFeaturePresent("posix") != FeatureStatus.NOT_PRESENT) {
            return true;
        }
        return false;
    }

    public boolean supportsFileAttributeView(String str) {
        if (str.equals("basic") || str.equals("unix")) {
            return true;
        }
        if (str.equals("posix")) {
            return supportsFileAttributeView((Class<? extends FileAttributeView>) PosixFileAttributeView.class);
        }
        if (str.equals("owner")) {
            return supportsFileAttributeView((Class<? extends FileAttributeView>) FileOwnerAttributeView.class);
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UnixFileStore)) {
            return false;
        }
        UnixFileStore unixFileStore = (UnixFileStore) obj;
        if (this.dev != unixFileStore.dev || !Arrays.equals(this.entry.dir(), unixFileStore.entry.dir()) || !this.entry.name().equals(unixFileStore.entry.name())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long j = this.dev;
        return Arrays.hashCode(this.entry.dir()) ^ ((int) (j ^ (j >>> 32)));
    }

    public String toString() {
        return Util.toString(this.entry.dir()) + " (" + this.entry.name() + NavigationBarInflaterView.KEY_CODE_END;
    }

    /* access modifiers changed from: package-private */
    public FeatureStatus checkIfFeaturePresent(String str) {
        return FeatureStatus.UNKNOWN;
    }
}
