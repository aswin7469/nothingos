package sun.nio.p035fs;

import java.lang.ref.SoftReference;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.FileSystemException;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.p026io.IOException;
import java.util.Arrays;
import java.util.Objects;

/* renamed from: sun.nio.fs.UnixPath */
class UnixPath extends AbstractPath {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static ThreadLocal<SoftReference<CharsetEncoder>> encoder = new ThreadLocal<>();

    /* renamed from: fs */
    private final UnixFileSystem f911fs;
    private int hash;
    private volatile int[] offsets;
    private final byte[] path;
    private volatile String stringValue;

    UnixPath(UnixFileSystem unixFileSystem, byte[] bArr) {
        this.f911fs = unixFileSystem;
        this.path = bArr;
    }

    UnixPath(UnixFileSystem unixFileSystem, String str) {
        this(unixFileSystem, encode(unixFileSystem, normalizeAndCheck(str)));
    }

    static String normalizeAndCheck(String str) {
        int length = str.length();
        int i = 0;
        char c = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt == '/' && c == '/') {
                return normalize(str, length, i - 1);
            }
            checkNotNul(str, charAt);
            i++;
            c = charAt;
        }
        return c == '/' ? normalize(str, length, length - 1) : str;
    }

    private static void checkNotNul(String str, char c) {
        if (c == 0) {
            throw new InvalidPathException(str, "Nul character not allowed");
        }
    }

    private static String normalize(String str, int i, int i2) {
        if (i == 0) {
            return str;
        }
        while (i > 0 && str.charAt(i - 1) == '/') {
            i--;
        }
        if (i == 0) {
            return "/";
        }
        StringBuilder sb = new StringBuilder(str.length());
        char c = 0;
        if (i2 > 0) {
            sb.append(str.substring(0, i2));
        }
        while (i2 < i) {
            char charAt = str.charAt(i2);
            if (charAt != '/' || c != '/') {
                checkNotNul(str, charAt);
                sb.append(charAt);
                c = charAt;
            }
            i2++;
        }
        return sb.toString();
    }

    private static byte[] encode(UnixFileSystem unixFileSystem, String str) {
        SoftReference softReference = encoder.get();
        CharsetEncoder charsetEncoder = softReference != null ? (CharsetEncoder) softReference.get() : null;
        if (charsetEncoder == null) {
            charsetEncoder = Util.jnuEncoding().newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
            encoder.set(new SoftReference(charsetEncoder));
        }
        char[] normalizeNativePath = unixFileSystem.normalizeNativePath(str.toCharArray());
        int length = (int) (((double) normalizeNativePath.length) * ((double) charsetEncoder.maxBytesPerChar()));
        byte[] bArr = new byte[length];
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        CharBuffer wrap2 = CharBuffer.wrap(normalizeNativePath);
        charsetEncoder.reset();
        boolean z = true;
        if (charsetEncoder.encode(wrap2, wrap, true).isUnderflow()) {
            z = true ^ charsetEncoder.flush(wrap).isUnderflow();
        }
        if (!z) {
            int position = wrap.position();
            if (position != length) {
                return Arrays.copyOf(bArr, position);
            }
            return bArr;
        }
        throw new InvalidPathException(str, "Malformed input or input contains unmappable characters");
    }

    /* access modifiers changed from: package-private */
    public byte[] asByteArray() {
        return this.path;
    }

    /* access modifiers changed from: package-private */
    public byte[] getByteArrayForSysCalls() {
        if (getFileSystem().needToResolveAgainstDefaultDirectory()) {
            return resolve(getFileSystem().defaultDirectory(), this.path);
        }
        if (!isEmpty()) {
            return this.path;
        }
        return new byte[]{46};
    }

    /* access modifiers changed from: package-private */
    public String getPathForExceptionMessage() {
        return toString();
    }

    /* access modifiers changed from: package-private */
    public String getPathForPermissionCheck() {
        if (getFileSystem().needToResolveAgainstDefaultDirectory()) {
            return Util.toString(getByteArrayForSysCalls());
        }
        return toString();
    }

    static UnixPath toUnixPath(Path path2) {
        path2.getClass();
        if (path2 instanceof UnixPath) {
            return (UnixPath) path2;
        }
        throw new ProviderMismatchException();
    }

    private void initOffsets() {
        int i;
        if (this.offsets == null) {
            int i2 = 0;
            if (!isEmpty()) {
                i = 0;
                int i3 = 0;
                while (true) {
                    byte[] bArr = this.path;
                    if (i3 >= bArr.length) {
                        break;
                    }
                    int i4 = i3 + 1;
                    if (bArr[i3] != 47) {
                        i++;
                        while (true) {
                            byte[] bArr2 = this.path;
                            if (i4 >= bArr2.length || bArr2[i4] == 47) {
                                break;
                            }
                            i4++;
                        }
                    }
                    i3 = i4;
                }
            } else {
                i = 1;
            }
            int[] iArr = new int[i];
            int i5 = 0;
            while (true) {
                byte[] bArr3 = this.path;
                if (i2 >= bArr3.length) {
                    break;
                } else if (bArr3[i2] == 47) {
                    i2++;
                } else {
                    int i6 = i5 + 1;
                    int i7 = i2 + 1;
                    iArr[i5] = i2;
                    while (true) {
                        byte[] bArr4 = this.path;
                        if (i7 >= bArr4.length || bArr4[i7] == 47) {
                            i5 = i6;
                            i2 = i7;
                        } else {
                            i7++;
                        }
                    }
                    i5 = i6;
                    i2 = i7;
                }
            }
            synchronized (this) {
                if (this.offsets == null) {
                    this.offsets = iArr;
                }
            }
        }
    }

    private boolean isEmpty() {
        return this.path.length == 0;
    }

    private UnixPath emptyPath() {
        return new UnixPath(getFileSystem(), new byte[0]);
    }

    public UnixFileSystem getFileSystem() {
        return this.f911fs;
    }

    public UnixPath getRoot() {
        byte[] bArr = this.path;
        if (bArr.length <= 0 || bArr[0] != 47) {
            return null;
        }
        return getFileSystem().rootDirectory();
    }

    public UnixPath getFileName() {
        initOffsets();
        int length = this.offsets.length;
        if (length == 0) {
            return null;
        }
        if (length == 1) {
            byte[] bArr = this.path;
            if (bArr.length > 0 && bArr[0] != 47) {
                return this;
            }
        }
        int i = this.offsets[length - 1];
        byte[] bArr2 = this.path;
        int length2 = bArr2.length - i;
        byte[] bArr3 = new byte[length2];
        System.arraycopy((Object) bArr2, i, (Object) bArr3, 0, length2);
        return new UnixPath(getFileSystem(), bArr3);
    }

    public UnixPath getParent() {
        initOffsets();
        int length = this.offsets.length;
        if (length == 0) {
            return null;
        }
        int i = this.offsets[length - 1] - 1;
        if (i <= 0) {
            return getRoot();
        }
        byte[] bArr = new byte[i];
        System.arraycopy((Object) this.path, 0, (Object) bArr, 0, i);
        return new UnixPath(getFileSystem(), bArr);
    }

    public int getNameCount() {
        initOffsets();
        return this.offsets.length;
    }

    public UnixPath getName(int i) {
        int i2;
        initOffsets();
        if (i < 0) {
            throw new IllegalArgumentException();
        } else if (i < this.offsets.length) {
            int i3 = this.offsets[i];
            if (i == this.offsets.length - 1) {
                i2 = this.path.length - i3;
            } else {
                i2 = (this.offsets[i + 1] - i3) - 1;
            }
            byte[] bArr = new byte[i2];
            System.arraycopy((Object) this.path, i3, (Object) bArr, 0, i2);
            return new UnixPath(getFileSystem(), bArr);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public UnixPath subpath(int i, int i2) {
        int i3;
        initOffsets();
        if (i < 0) {
            throw new IllegalArgumentException();
        } else if (i >= this.offsets.length) {
            throw new IllegalArgumentException();
        } else if (i2 > this.offsets.length) {
            throw new IllegalArgumentException();
        } else if (i < i2) {
            int i4 = this.offsets[i];
            if (i2 == this.offsets.length) {
                i3 = this.path.length - i4;
            } else {
                i3 = (this.offsets[i2] - i4) - 1;
            }
            byte[] bArr = new byte[i3];
            System.arraycopy((Object) this.path, i4, (Object) bArr, 0, i3);
            return new UnixPath(getFileSystem(), bArr);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean isAbsolute() {
        byte[] bArr = this.path;
        return bArr.length > 0 && bArr[0] == 47;
    }

    private static byte[] resolve(byte[] bArr, byte[] bArr2) {
        int length = bArr.length;
        int length2 = bArr2.length;
        if (length2 == 0) {
            return bArr;
        }
        if (length == 0 || bArr2[0] == 47) {
            return bArr2;
        }
        if (length == 1 && bArr[0] == 47) {
            byte[] bArr3 = new byte[(length2 + 1)];
            bArr3[0] = 47;
            System.arraycopy((Object) bArr2, 0, (Object) bArr3, 1, length2);
            return bArr3;
        }
        int i = length + 1;
        byte[] bArr4 = new byte[(i + length2)];
        System.arraycopy((Object) bArr, 0, (Object) bArr4, 0, length);
        bArr4[bArr.length] = 47;
        System.arraycopy((Object) bArr2, 0, (Object) bArr4, i, length2);
        return bArr4;
    }

    public UnixPath resolve(Path path2) {
        byte[] bArr = toUnixPath(path2).path;
        if (bArr.length > 0 && bArr[0] == 47) {
            return (UnixPath) path2;
        }
        return new UnixPath(getFileSystem(), resolve(this.path, bArr));
    }

    /* access modifiers changed from: package-private */
    public UnixPath resolve(byte[] bArr) {
        return resolve((Path) new UnixPath(getFileSystem(), bArr));
    }

    public UnixPath relativize(Path path2) {
        int i;
        UnixPath unixPath = toUnixPath(path2);
        if (unixPath.equals(this)) {
            return emptyPath();
        }
        if (isAbsolute() != unixPath.isAbsolute()) {
            throw new IllegalArgumentException("'other' is different type of Path");
        } else if (isEmpty()) {
            return unixPath;
        } else {
            int nameCount = getNameCount();
            int nameCount2 = unixPath.getNameCount();
            int i2 = nameCount > nameCount2 ? nameCount2 : nameCount;
            int i3 = 0;
            int i4 = 0;
            while (i4 < i2 && getName(i4).equals(unixPath.getName(i4))) {
                i4++;
            }
            int i5 = nameCount - i4;
            if (i4 < nameCount2) {
                UnixPath subpath = unixPath.subpath(i4, nameCount2);
                if (i5 == 0) {
                    return subpath;
                }
                boolean isEmpty = unixPath.isEmpty();
                int length = (i5 * 3) + subpath.path.length;
                if (isEmpty) {
                    length--;
                }
                byte[] bArr = new byte[length];
                int i6 = 0;
                while (i5 > 0) {
                    int i7 = i6 + 1;
                    bArr[i6] = 46;
                    i6 = i7 + 1;
                    bArr[i7] = 46;
                    if (!isEmpty) {
                        i = i6 + 1;
                        bArr[i6] = 47;
                    } else if (i5 > 1) {
                        i = i6 + 1;
                        bArr[i6] = 47;
                    } else {
                        i5--;
                    }
                    i6 = i;
                    i5--;
                }
                byte[] bArr2 = subpath.path;
                System.arraycopy((Object) bArr2, 0, (Object) bArr, i6, bArr2.length);
                return new UnixPath(getFileSystem(), bArr);
            }
            byte[] bArr3 = new byte[((i5 * 3) - 1)];
            while (i5 > 0) {
                int i8 = i3 + 1;
                bArr3[i3] = 46;
                i3 = i8 + 1;
                bArr3[i8] = 46;
                if (i5 > 1) {
                    bArr3[i3] = 47;
                    i3++;
                }
                i5--;
            }
            return new UnixPath(getFileSystem(), bArr3);
        }
    }

    public Path normalize() {
        int i;
        int i2;
        boolean z;
        int i3;
        int nameCount = getNameCount();
        if (nameCount == 0 || isEmpty()) {
            return this;
        }
        boolean[] zArr = new boolean[nameCount];
        int[] iArr = new int[nameCount];
        boolean isAbsolute = isAbsolute();
        int i4 = nameCount;
        int i5 = 0;
        boolean z2 = false;
        while (true) {
            i = 1;
            if (i5 >= nameCount) {
                break;
            }
            int i6 = this.offsets[i5];
            if (i5 == this.offsets.length - 1) {
                i3 = this.path.length - i6;
            } else {
                i3 = (this.offsets[i5 + 1] - i6) - 1;
            }
            iArr[i5] = i3;
            byte[] bArr = this.path;
            if (bArr[i6] == 46) {
                if (i3 == 1) {
                    zArr[i5] = true;
                    i4--;
                } else if (bArr[i6 + 1] == 46) {
                    z2 = true;
                }
            }
            i5++;
        }
        if (z2) {
            while (true) {
                int i7 = -1;
                i2 = i4;
                for (int i8 = 0; i8 < nameCount; i8++) {
                    if (!zArr[i8]) {
                        if (iArr[i8] == 2) {
                            int i9 = this.offsets[i8];
                            byte[] bArr2 = this.path;
                            if (bArr2[i9] == 46 && bArr2[i9 + 1] == 46) {
                                if (i7 >= 0) {
                                    zArr[i7] = true;
                                    zArr[i8] = true;
                                    i2 -= 2;
                                    i7 = -1;
                                } else if (isAbsolute) {
                                    int i10 = 0;
                                    while (true) {
                                        if (i10 >= i8) {
                                            z = false;
                                            break;
                                        } else if (!zArr[i10]) {
                                            z = true;
                                            break;
                                        } else {
                                            i10++;
                                        }
                                    }
                                    if (!z) {
                                        zArr[i8] = true;
                                        i2--;
                                    }
                                }
                            }
                        }
                        i7 = i8;
                    }
                }
                if (i4 <= i2) {
                    break;
                }
                i4 = i2;
            }
            i4 = i2;
        }
        if (i4 == nameCount) {
            return this;
        }
        if (i4 == 0) {
            return isAbsolute ? getFileSystem().rootDirectory() : emptyPath();
        }
        int i11 = i4 - 1;
        if (isAbsolute) {
            i11++;
        }
        for (int i12 = 0; i12 < nameCount; i12++) {
            if (!zArr[i12]) {
                i11 += iArr[i12];
            }
        }
        byte[] bArr3 = new byte[i11];
        if (isAbsolute) {
            bArr3[0] = 47;
        } else {
            i = 0;
        }
        for (int i13 = 0; i13 < nameCount; i13++) {
            if (!zArr[i13]) {
                System.arraycopy((Object) this.path, this.offsets[i13], (Object) bArr3, i, iArr[i13]);
                i += iArr[i13];
                i4--;
                if (i4 > 0) {
                    bArr3[i] = 47;
                    i++;
                }
            }
        }
        return new UnixPath(getFileSystem(), bArr3);
    }

    public boolean startsWith(Path path2) {
        if (!(Objects.requireNonNull(path2) instanceof UnixPath)) {
            return false;
        }
        UnixPath unixPath = (UnixPath) path2;
        if (unixPath.path.length > this.path.length) {
            return false;
        }
        int nameCount = getNameCount();
        int nameCount2 = unixPath.getNameCount();
        if (nameCount2 == 0 && isAbsolute()) {
            return !unixPath.isEmpty();
        }
        if (nameCount2 > nameCount) {
            return false;
        }
        if (nameCount2 == nameCount && this.path.length != unixPath.path.length) {
            return false;
        }
        for (int i = 0; i < nameCount2; i++) {
            if (!Integer.valueOf(this.offsets[i]).equals(Integer.valueOf(unixPath.offsets[i]))) {
                return false;
            }
        }
        int i2 = 0;
        while (true) {
            byte[] bArr = unixPath.path;
            if (i2 >= bArr.length) {
                byte[] bArr2 = this.path;
                if (i2 >= bArr2.length || bArr2[i2] == 47) {
                    return true;
                }
                return false;
            } else if (this.path[i2] != bArr[i2]) {
                return false;
            } else {
                i2++;
            }
        }
    }

    public boolean endsWith(Path path2) {
        int nameCount;
        int nameCount2;
        if (!(Objects.requireNonNull(path2) instanceof UnixPath)) {
            return false;
        }
        UnixPath unixPath = (UnixPath) path2;
        int length = this.path.length;
        int length2 = unixPath.path.length;
        if (length2 > length) {
            return false;
        }
        if (length > 0 && length2 == 0) {
            return false;
        }
        if ((unixPath.isAbsolute() && !isAbsolute()) || (nameCount2 = unixPath.getNameCount()) > (nameCount = getNameCount())) {
            return false;
        }
        if (nameCount2 == nameCount) {
            if (nameCount == 0) {
                return true;
            }
            if (length2 != ((!isAbsolute() || unixPath.isAbsolute()) ? length : length - 1)) {
                return false;
            }
        } else if (unixPath.isAbsolute()) {
            return false;
        }
        int i = this.offsets[nameCount - nameCount2];
        int i2 = unixPath.offsets[0];
        if (length2 - i2 != length - i) {
            return false;
        }
        while (i2 < length2) {
            int i3 = i + 1;
            int i4 = i2 + 1;
            if (this.path[i] != unixPath.path[i2]) {
                return false;
            }
            i = i3;
            i2 = i4;
        }
        return true;
    }

    public int compareTo(Path path2) {
        int length = this.path.length;
        UnixPath unixPath = (UnixPath) path2;
        int length2 = unixPath.path.length;
        int min = Math.min(length, length2);
        byte[] bArr = this.path;
        byte[] bArr2 = unixPath.path;
        for (int i = 0; i < min; i++) {
            byte b = bArr[i] & 255;
            byte b2 = bArr2[i] & 255;
            if (b != b2) {
                return b - b2;
            }
        }
        return length - length2;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UnixPath) || compareTo((Path) obj) != 0) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = this.hash;
        if (i == 0) {
            int i2 = 0;
            while (true) {
                byte[] bArr = this.path;
                if (i2 >= bArr.length) {
                    break;
                }
                i = (i * 31) + (bArr[i2] & 255);
                i2++;
            }
            this.hash = i;
        }
        return i;
    }

    public String toString() {
        if (this.stringValue == null) {
            this.stringValue = this.f911fs.normalizeJavaPath(Util.toString(this.path));
        }
        return this.stringValue;
    }

    /* access modifiers changed from: package-private */
    public int openForAttributeAccess(boolean z) throws IOException {
        int i = UnixConstants.O_RDONLY;
        if (!z) {
            if (UnixConstants.O_NOFOLLOW != 0) {
                i |= UnixConstants.O_NOFOLLOW;
            } else {
                throw new IOException("NOFOLLOW_LINKS is not supported on this platform");
            }
        }
        try {
            return UnixNativeDispatcher.open(this, i, 0);
        } catch (UnixException e) {
            if (getFileSystem().isSolaris() && e.errno() == UnixConstants.EINVAL) {
                e.setError(UnixConstants.ELOOP);
            }
            if (e.errno() != UnixConstants.ELOOP) {
                e.rethrowAsIOException(this);
                return -1;
            }
            String pathForExceptionMessage = getPathForExceptionMessage();
            throw new FileSystemException(pathForExceptionMessage, (String) null, e.getMessage() + " or unable to access attributes of symbolic link");
        }
    }

    /* access modifiers changed from: package-private */
    public void checkRead() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(getPathForPermissionCheck());
        }
    }

    /* access modifiers changed from: package-private */
    public void checkWrite() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(getPathForPermissionCheck());
        }
    }

    /* access modifiers changed from: package-private */
    public void checkDelete() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(getPathForPermissionCheck());
        }
    }

    public UnixPath toAbsolutePath() {
        if (isAbsolute()) {
            return this;
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertyAccess("user.dir");
        }
        return new UnixPath(getFileSystem(), resolve(getFileSystem().defaultDirectory(), this.path));
    }

    public Path toRealPath(LinkOption... linkOptionArr) throws IOException {
        UnixFileAttributes unixFileAttributes;
        checkRead();
        UnixPath absolutePath = toAbsolutePath();
        if (Util.followLinks(linkOptionArr)) {
            try {
                return new UnixPath(getFileSystem(), UnixNativeDispatcher.realpath(absolutePath));
            } catch (UnixException e) {
                e.rethrowAsIOException(this);
            }
        }
        UnixPath rootDirectory = this.f911fs.rootDirectory();
        for (int i = 0; i < absolutePath.getNameCount(); i++) {
            UnixPath name = absolutePath.getName(i);
            if (name.asByteArray().length != 1 || name.asByteArray()[0] != 46) {
                if (name.asByteArray().length == 2 && name.asByteArray()[0] == 46 && name.asByteArray()[1] == 46) {
                    try {
                        unixFileAttributes = UnixFileAttributes.get(rootDirectory, false);
                    } catch (UnixException e2) {
                        e2.rethrowAsIOException(rootDirectory);
                        unixFileAttributes = null;
                    }
                    if (!unixFileAttributes.isSymbolicLink()) {
                        rootDirectory = rootDirectory.getParent();
                        if (rootDirectory == null) {
                            rootDirectory = this.f911fs.rootDirectory();
                        }
                    }
                }
                rootDirectory = rootDirectory.resolve((Path) name);
            }
        }
        try {
            UnixFileAttributes.get(rootDirectory, false);
        } catch (UnixException e3) {
            e3.rethrowAsIOException(rootDirectory);
        }
        return rootDirectory;
    }

    public URI toUri() {
        return UnixUriUtils.toUri(this);
    }

    public WatchKey register(WatchService watchService, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException {
        watchService.getClass();
        if (watchService instanceof AbstractWatchService) {
            checkRead();
            return ((AbstractWatchService) watchService).register(this, kindArr, modifierArr);
        }
        throw new ProviderMismatchException();
    }
}
