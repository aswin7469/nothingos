package sun.nio.p035fs;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.p026io.IOException;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import sun.security.action.GetPropertyAction;

/* renamed from: sun.nio.fs.UnixFileSystem */
abstract class UnixFileSystem extends FileSystem {
    private static final String GLOB_SYNTAX = "glob";
    private static final String REGEX_SYNTAX = "regex";
    private final byte[] defaultDirectory;
    private final boolean needToResolveAgainstDefaultDirectory;
    private final UnixFileSystemProvider provider;
    /* access modifiers changed from: private */
    public final UnixPath rootDirectory;

    /* access modifiers changed from: package-private */
    public void copyNonPosixAttributes(int i, int i2) {
    }

    /* access modifiers changed from: package-private */
    public abstract FileStore getFileStore(UnixMountEntry unixMountEntry) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract Iterable<UnixMountEntry> getMountEntries();

    public final String getSeparator() {
        return "/";
    }

    public final boolean isOpen() {
        return true;
    }

    public final boolean isReadOnly() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isSolaris() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public String normalizeJavaPath(String str) {
        return str;
    }

    /* access modifiers changed from: package-private */
    public char[] normalizeNativePath(char[] cArr) {
        return cArr;
    }

    UnixFileSystem(UnixFileSystemProvider unixFileSystemProvider, String str) {
        boolean z;
        this.provider = unixFileSystemProvider;
        byte[] bytes = Util.toBytes(UnixPath.normalizeAndCheck(str));
        this.defaultDirectory = bytes;
        boolean z2 = false;
        if (bytes[0] == 47) {
            String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("sun.nio.fs.chdirAllowed", "false"));
            if (str2.length() == 0) {
                z = true;
            } else {
                z = Boolean.valueOf(str2).booleanValue();
            }
            if (z) {
                this.needToResolveAgainstDefaultDirectory = true;
            } else {
                byte[] bArr = UnixNativeDispatcher.getcwd();
                boolean z3 = bArr.length == bytes.length;
                if (z3) {
                    int i = 0;
                    while (true) {
                        if (i >= bArr.length) {
                            break;
                        } else if (bArr[i] != this.defaultDirectory[i]) {
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                z2 = z3;
                this.needToResolveAgainstDefaultDirectory = !z2;
            }
            this.rootDirectory = new UnixPath(this, "/");
            return;
        }
        throw new RuntimeException("default directory must be absolute");
    }

    /* access modifiers changed from: package-private */
    public byte[] defaultDirectory() {
        return this.defaultDirectory;
    }

    /* access modifiers changed from: package-private */
    public boolean needToResolveAgainstDefaultDirectory() {
        return this.needToResolveAgainstDefaultDirectory;
    }

    /* access modifiers changed from: package-private */
    public UnixPath rootDirectory() {
        return this.rootDirectory;
    }

    static List<String> standardFileAttributeViews() {
        return Arrays.asList("basic", "posix", "unix", "owner");
    }

    public final FileSystemProvider provider() {
        return this.provider;
    }

    public final void close() throws IOException {
        throw new UnsupportedOperationException();
    }

    public final Iterable<Path> getRootDirectories() {
        final List unmodifiableList = Collections.unmodifiableList(Arrays.asList(this.rootDirectory));
        return new Iterable<Path>() {
            public Iterator<Path> iterator() {
                try {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkRead(UnixFileSystem.this.rootDirectory.toString());
                    }
                    return unmodifiableList.iterator();
                } catch (SecurityException unused) {
                    return Collections.emptyList().iterator();
                }
            }
        };
    }

    /* renamed from: sun.nio.fs.UnixFileSystem$FileStoreIterator */
    private class FileStoreIterator implements Iterator<FileStore> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final Iterator<UnixMountEntry> entries;
        private FileStore next;

        static {
            Class<UnixFileSystem> cls = UnixFileSystem.class;
        }

        FileStoreIterator() {
            this.entries = UnixFileSystem.this.getMountEntries().iterator();
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private java.nio.file.FileStore readNext() {
            /*
                r3 = this;
            L_0x0000:
                java.util.Iterator<sun.nio.fs.UnixMountEntry> r0 = r3.entries
                boolean r0 = r0.hasNext()
                if (r0 != 0) goto L_0x000a
                r3 = 0
                return r3
            L_0x000a:
                java.util.Iterator<sun.nio.fs.UnixMountEntry> r0 = r3.entries
                java.lang.Object r0 = r0.next()
                sun.nio.fs.UnixMountEntry r0 = (sun.nio.p035fs.UnixMountEntry) r0
                boolean r1 = r0.isIgnored()
                if (r1 == 0) goto L_0x0019
                goto L_0x0000
            L_0x0019:
                java.lang.SecurityManager r1 = java.lang.System.getSecurityManager()
                if (r1 == 0) goto L_0x002a
                byte[] r2 = r0.dir()     // Catch:{ SecurityException -> 0x0000 }
                java.lang.String r2 = sun.nio.p035fs.Util.toString(r2)     // Catch:{ SecurityException -> 0x0000 }
                r1.checkRead((java.lang.String) r2)     // Catch:{ SecurityException -> 0x0000 }
            L_0x002a:
                sun.nio.fs.UnixFileSystem r1 = sun.nio.p035fs.UnixFileSystem.this     // Catch:{  }
                java.nio.file.FileStore r3 = r1.getFileStore(r0)     // Catch:{  }
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixFileSystem.FileStoreIterator.readNext():java.nio.file.FileStore");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0013, code lost:
            return r1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized boolean hasNext() {
            /*
                r2 = this;
                monitor-enter(r2)
                java.nio.file.FileStore r0 = r2.next     // Catch:{ all -> 0x0014 }
                r1 = 1
                if (r0 == 0) goto L_0x0008
                monitor-exit(r2)
                return r1
            L_0x0008:
                java.nio.file.FileStore r0 = r2.readNext()     // Catch:{ all -> 0x0014 }
                r2.next = r0     // Catch:{ all -> 0x0014 }
                if (r0 == 0) goto L_0x0011
                goto L_0x0012
            L_0x0011:
                r1 = 0
            L_0x0012:
                monitor-exit(r2)
                return r1
            L_0x0014:
                r0 = move-exception
                monitor-exit(r2)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixFileSystem.FileStoreIterator.hasNext():boolean");
        }

        public synchronized FileStore next() {
            FileStore fileStore;
            if (this.next == null) {
                this.next = readNext();
            }
            fileStore = this.next;
            if (fileStore != null) {
                this.next = null;
            } else {
                throw new NoSuchElementException();
            }
            return fileStore;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public final Iterable<FileStore> getFileStores() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkPermission(new RuntimePermission("getFileStoreAttributes"));
            } catch (SecurityException unused) {
                return Collections.emptyList();
            }
        }
        return new Iterable<FileStore>() {
            public Iterator<FileStore> iterator() {
                return new FileStoreIterator();
            }
        };
    }

    public final Path getPath(String str, String... strArr) {
        if (strArr.length != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            for (String str2 : strArr) {
                if (str2.length() > 0) {
                    if (sb.length() > 0) {
                        sb.append('/');
                    }
                    sb.append(str2);
                }
            }
            str = sb.toString();
        }
        return new UnixPath(this, str);
    }

    public PathMatcher getPathMatcher(String str) {
        int indexOf = str.indexOf(58);
        if (indexOf <= 0 || indexOf == str.length()) {
            throw new IllegalArgumentException();
        }
        String substring = str.substring(0, indexOf);
        String substring2 = str.substring(indexOf + 1);
        if (substring.equals(GLOB_SYNTAX)) {
            substring2 = Globs.toUnixRegexPattern(substring2);
        } else if (!substring.equals(REGEX_SYNTAX)) {
            throw new UnsupportedOperationException("Syntax '" + substring + "' not recognized");
        }
        final Pattern compilePathMatchPattern = compilePathMatchPattern(substring2);
        return new PathMatcher() {
            public boolean matches(Path path) {
                return compilePathMatchPattern.matcher(path.toString()).matches();
            }
        };
    }

    public final UserPrincipalLookupService getUserPrincipalLookupService() {
        return LookupService.instance;
    }

    /* renamed from: sun.nio.fs.UnixFileSystem$LookupService */
    private static class LookupService {
        static final UserPrincipalLookupService instance = new UserPrincipalLookupService() {
            public UserPrincipal lookupPrincipalByName(String str) throws IOException {
                return UnixUserPrincipals.lookupUser(str);
            }

            public GroupPrincipal lookupPrincipalByGroupName(String str) throws IOException {
                return UnixUserPrincipals.lookupGroup(str);
            }
        };

        private LookupService() {
        }
    }

    /* access modifiers changed from: package-private */
    public Pattern compilePathMatchPattern(String str) {
        return Pattern.compile(str);
    }
}
