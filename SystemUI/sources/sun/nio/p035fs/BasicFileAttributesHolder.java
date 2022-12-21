package sun.nio.p035fs;

import java.nio.file.attribute.BasicFileAttributes;

/* renamed from: sun.nio.fs.BasicFileAttributesHolder */
public interface BasicFileAttributesHolder {
    BasicFileAttributes get();

    void invalidate();
}
