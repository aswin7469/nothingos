package java.nio.file;

import java.nio.file.attribute.BasicFileAttributes;
import java.p026io.IOException;
import java.util.Objects;

public class SimpleFileVisitor<T> implements FileVisitor<T> {
    protected SimpleFileVisitor() {
    }

    public FileVisitResult preVisitDirectory(T t, BasicFileAttributes basicFileAttributes) throws IOException {
        Objects.requireNonNull(t);
        Objects.requireNonNull(basicFileAttributes);
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult visitFile(T t, BasicFileAttributes basicFileAttributes) throws IOException {
        Objects.requireNonNull(t);
        Objects.requireNonNull(basicFileAttributes);
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult visitFileFailed(T t, IOException iOException) throws IOException {
        Objects.requireNonNull(t);
        throw iOException;
    }

    public FileVisitResult postVisitDirectory(T t, IOException iOException) throws IOException {
        Objects.requireNonNull(t);
        if (iOException == null) {
            return FileVisitResult.CONTINUE;
        }
        throw iOException;
    }
}
