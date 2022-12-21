package java.nio.file;

import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.p026io.IOException;
import java.p026io.InputStream;

class CopyMoveHelper {
    private CopyMoveHelper() {
    }

    private static class CopyOptions {
        boolean copyAttributes = false;
        boolean followLinks = true;
        boolean replaceExisting = false;

        private CopyOptions() {
        }

        static CopyOptions parse(CopyOption... copyOptionArr) {
            CopyOptions copyOptions = new CopyOptions();
            for (LinkOption linkOption : copyOptionArr) {
                if (linkOption == StandardCopyOption.REPLACE_EXISTING) {
                    copyOptions.replaceExisting = true;
                } else if (linkOption == LinkOption.NOFOLLOW_LINKS) {
                    copyOptions.followLinks = false;
                } else if (linkOption == StandardCopyOption.COPY_ATTRIBUTES) {
                    copyOptions.copyAttributes = true;
                } else {
                    linkOption.getClass();
                    throw new UnsupportedOperationException("'" + linkOption + "' is not a recognized copy option");
                }
            }
            return copyOptions;
        }
    }

    private static CopyOption[] convertMoveToCopyOptions(CopyOption... copyOptionArr) throws AtomicMoveNotSupportedException {
        int length = copyOptionArr.length;
        CopyOption[] copyOptionArr2 = new CopyOption[(length + 2)];
        int i = 0;
        while (i < length) {
            StandardCopyOption standardCopyOption = copyOptionArr[i];
            if (standardCopyOption != StandardCopyOption.ATOMIC_MOVE) {
                copyOptionArr2[i] = standardCopyOption;
                i++;
            } else {
                throw new AtomicMoveNotSupportedException((String) null, (String) null, "Atomic move between providers is not supported");
            }
        }
        copyOptionArr2[length] = LinkOption.NOFOLLOW_LINKS;
        copyOptionArr2[length + 1] = StandardCopyOption.COPY_ATTRIBUTES;
        return copyOptionArr2;
    }

    static void copyToForeignTarget(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        CopyOptions parse = CopyOptions.parse(copyOptionArr);
        BasicFileAttributes readAttributes = Files.readAttributes(path, BasicFileAttributes.class, parse.followLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
        if (!readAttributes.isSymbolicLink()) {
            if (parse.replaceExisting) {
                Files.deleteIfExists(path2);
            } else if (Files.exists(path2, new LinkOption[0])) {
                throw new FileAlreadyExistsException(path2.toString());
            }
            if (readAttributes.isDirectory()) {
                Files.createDirectory(path2, new FileAttribute[0]);
            } else {
                InputStream newInputStream = Files.newInputStream(path, new OpenOption[0]);
                try {
                    Files.copy(newInputStream, path2, new CopyOption[0]);
                    if (newInputStream != null) {
                        newInputStream.close();
                    }
                } catch (Throwable th) {
                    th.addSuppressed(th);
                }
            }
            if (parse.copyAttributes) {
                try {
                    ((BasicFileAttributeView) Files.getFileAttributeView(path2, BasicFileAttributeView.class, new LinkOption[0])).setTimes(readAttributes.lastModifiedTime(), readAttributes.lastAccessTime(), readAttributes.creationTime());
                    return;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            } else {
                return;
            }
        } else {
            throw new IOException("Copying of symbolic links not supported");
        }
        throw th;
        throw th;
    }

    static void moveToForeignTarget(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        copyToForeignTarget(path, path2, convertMoveToCopyOptions(copyOptionArr));
        Files.delete(path);
    }
}
