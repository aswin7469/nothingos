package java.nio.file;

import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.SecureRandom;
import java.util.EnumSet;
import java.util.Set;
import sun.security.action.GetPropertyAction;

class TempFileHelper {
    private static final boolean isPosix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
    private static final SecureRandom random = new SecureRandom();
    private static final Path tmpdir = Paths.get((String) AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")), new String[0]);

    private TempFileHelper() {
    }

    private static Path generatePath(String str, String str2, Path path) {
        long j;
        long nextLong = random.nextLong();
        if (nextLong == Long.MIN_VALUE) {
            j = 0;
        } else {
            j = Math.abs(nextLong);
        }
        FileSystem fileSystem = path.getFileSystem();
        Path path2 = fileSystem.getPath(str + Long.toString(j) + str2, new String[0]);
        if (path2.getParent() == null) {
            return path.resolve(path2);
        }
        throw new IllegalArgumentException("Invalid prefix or suffix");
    }

    private static class PosixPermissions {
        static final FileAttribute<Set<PosixFilePermission>> dirPermissions = PosixFilePermissions.asFileAttribute(EnumSet.m1718of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE));
        static final FileAttribute<Set<PosixFilePermission>> filePermissions = PosixFilePermissions.asFileAttribute(EnumSet.m1717of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE));

        private PosixPermissions() {
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:35|36|(3:38|39|40)(3:57|41|42)) */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0071, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0074, code lost:
        if (r5 != tmpdir) goto L_0x0080;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x007f, code lost:
        throw new java.lang.SecurityException("Unable to create temporary file or directory");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0080, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0081, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0082, code lost:
        if (r0 != null) goto L_0x0084;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x008b, code lost:
        throw new java.lang.IllegalArgumentException("Invalid prefix or suffix");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x008c, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x0061 */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0061 A[LOOP:1: B:35:0x0061->B:38:0x0067, LOOP_START, SYNTHETIC, Splitter:B:35:0x0061] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.nio.file.Path create(java.nio.file.Path r5, java.lang.String r6, java.lang.String r7, boolean r8, java.nio.file.attribute.FileAttribute<?>[] r9) throws java.p026io.IOException {
        /*
            java.lang.String r0 = ""
            if (r6 != 0) goto L_0x0005
            r6 = r0
        L_0x0005:
            if (r7 != 0) goto L_0x000d
            if (r8 == 0) goto L_0x000b
            r7 = r0
            goto L_0x000d
        L_0x000b:
            java.lang.String r7 = ".tmp"
        L_0x000d:
            if (r5 != 0) goto L_0x0011
            java.nio.file.Path r5 = tmpdir
        L_0x0011:
            boolean r0 = isPosix
            if (r0 == 0) goto L_0x005d
            java.nio.file.FileSystem r0 = r5.getFileSystem()
            java.nio.file.FileSystem r1 = java.nio.file.FileSystems.getDefault()
            if (r0 != r1) goto L_0x005d
            int r0 = r9.length
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x0030
            java.nio.file.attribute.FileAttribute[] r9 = new java.nio.file.attribute.FileAttribute[r1]
            if (r8 == 0) goto L_0x002b
            java.nio.file.attribute.FileAttribute<java.util.Set<java.nio.file.attribute.PosixFilePermission>> r0 = java.nio.file.TempFileHelper.PosixPermissions.dirPermissions
            goto L_0x002d
        L_0x002b:
            java.nio.file.attribute.FileAttribute<java.util.Set<java.nio.file.attribute.PosixFilePermission>> r0 = java.nio.file.TempFileHelper.PosixPermissions.filePermissions
        L_0x002d:
            r9[r2] = r0
            goto L_0x005d
        L_0x0030:
            r0 = r2
        L_0x0031:
            int r3 = r9.length
            if (r0 >= r3) goto L_0x0047
            r3 = r9[r0]
            java.lang.String r3 = r3.name()
            java.lang.String r4 = "posix:permissions"
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L_0x0044
            r0 = r1
            goto L_0x0048
        L_0x0044:
            int r0 = r0 + 1
            goto L_0x0031
        L_0x0047:
            r0 = r2
        L_0x0048:
            if (r0 != 0) goto L_0x005d
            int r0 = r9.length
            int r0 = r0 + r1
            java.nio.file.attribute.FileAttribute[] r3 = new java.nio.file.attribute.FileAttribute[r0]
            int r4 = r9.length
            java.lang.System.arraycopy((java.lang.Object) r9, (int) r2, (java.lang.Object) r3, (int) r2, (int) r4)
            int r0 = r0 - r1
            if (r8 == 0) goto L_0x0058
            java.nio.file.attribute.FileAttribute<java.util.Set<java.nio.file.attribute.PosixFilePermission>> r9 = java.nio.file.TempFileHelper.PosixPermissions.dirPermissions
            goto L_0x005a
        L_0x0058:
            java.nio.file.attribute.FileAttribute<java.util.Set<java.nio.file.attribute.PosixFilePermission>> r9 = java.nio.file.TempFileHelper.PosixPermissions.filePermissions
        L_0x005a:
            r3[r0] = r9
            r9 = r3
        L_0x005d:
            java.lang.SecurityManager r0 = java.lang.System.getSecurityManager()
        L_0x0061:
            java.nio.file.Path r1 = generatePath(r6, r7, r5)     // Catch:{ InvalidPathException -> 0x0081 }
            if (r8 == 0) goto L_0x006c
            java.nio.file.Path r5 = java.nio.file.Files.createDirectory(r1, r9)     // Catch:{ SecurityException -> 0x0071, FileAlreadyExistsException -> 0x0061 }
            return r5
        L_0x006c:
            java.nio.file.Path r5 = java.nio.file.Files.createFile(r1, r9)     // Catch:{ SecurityException -> 0x0071, FileAlreadyExistsException -> 0x0061 }
            return r5
        L_0x0071:
            r6 = move-exception
            java.nio.file.Path r7 = tmpdir
            if (r5 != r7) goto L_0x0080
            if (r0 == 0) goto L_0x0080
            java.lang.SecurityException r5 = new java.lang.SecurityException
            java.lang.String r6 = "Unable to create temporary file or directory"
            r5.<init>((java.lang.String) r6)
            throw r5
        L_0x0080:
            throw r6
        L_0x0081:
            r5 = move-exception
            if (r0 == 0) goto L_0x008c
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.String r6 = "Invalid prefix or suffix"
            r5.<init>((java.lang.String) r6)
            throw r5
        L_0x008c:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.file.TempFileHelper.create(java.nio.file.Path, java.lang.String, java.lang.String, boolean, java.nio.file.attribute.FileAttribute[]):java.nio.file.Path");
    }

    static Path createTempFile(Path path, String str, String str2, FileAttribute<?>[] fileAttributeArr) throws IOException {
        return create(path, str, str2, false, fileAttributeArr);
    }

    static Path createTempDirectory(Path path, String str, FileAttribute<?>[] fileAttributeArr) throws IOException {
        return create(path, str, (String) null, true, fileAttributeArr);
    }
}
