package sun.nio.p035fs;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.p026io.File;
import java.p026io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* renamed from: sun.nio.fs.AbstractPath */
abstract class AbstractPath implements Path {
    protected AbstractPath() {
    }

    public final boolean startsWith(String str) {
        return startsWith(getFileSystem().getPath(str, new String[0]));
    }

    public final boolean endsWith(String str) {
        return endsWith(getFileSystem().getPath(str, new String[0]));
    }

    public final Path resolve(String str) {
        return resolve(getFileSystem().getPath(str, new String[0]));
    }

    public final Path resolveSibling(Path path) {
        path.getClass();
        Path parent = getParent();
        return parent == null ? path : parent.resolve(path);
    }

    public final Path resolveSibling(String str) {
        return resolveSibling(getFileSystem().getPath(str, new String[0]));
    }

    public final Iterator<Path> iterator() {
        return new Iterator<Path>() {

            /* renamed from: i */
            private int f905i = 0;

            public boolean hasNext() {
                return this.f905i < AbstractPath.this.getNameCount();
            }

            public Path next() {
                if (this.f905i < AbstractPath.this.getNameCount()) {
                    Path name = AbstractPath.this.getName(this.f905i);
                    this.f905i++;
                    return name;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public final File toFile() {
        return new File(toString());
    }

    public final WatchKey register(WatchService watchService, WatchEvent.Kind<?>... kindArr) throws IOException {
        return register(watchService, kindArr, new WatchEvent.Modifier[0]);
    }
}
