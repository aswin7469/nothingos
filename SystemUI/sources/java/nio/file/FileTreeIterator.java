package java.nio.file;

import java.nio.file.FileTreeWalker;
import java.p026io.Closeable;
import java.p026io.IOException;
import java.p026io.UncheckedIOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

class FileTreeIterator implements Iterator<FileTreeWalker.Event>, Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private FileTreeWalker.Event next;
    private final FileTreeWalker walker;

    FileTreeIterator(Path path, int i, FileVisitOption... fileVisitOptionArr) throws IOException {
        FileTreeWalker fileTreeWalker = new FileTreeWalker(Arrays.asList(fileVisitOptionArr), i);
        this.walker = fileTreeWalker;
        FileTreeWalker.Event walk = fileTreeWalker.walk(path);
        this.next = walk;
        IOException ioeException = walk.ioeException();
        if (ioeException != null) {
            throw ioeException;
        }
    }

    private void fetchNextIfNeeded() {
        if (this.next == null) {
            FileTreeWalker.Event next2 = this.walker.next();
            while (next2 != null) {
                IOException ioeException = next2.ioeException();
                if (ioeException != null) {
                    throw new UncheckedIOException(ioeException);
                } else if (next2.type() != FileTreeWalker.EventType.END_DIRECTORY) {
                    this.next = next2;
                    return;
                } else {
                    next2 = this.walker.next();
                }
            }
        }
    }

    public boolean hasNext() {
        if (this.walker.isOpen()) {
            fetchNextIfNeeded();
            return this.next != null;
        }
        throw new IllegalStateException();
    }

    public FileTreeWalker.Event next() {
        if (this.walker.isOpen()) {
            fetchNextIfNeeded();
            FileTreeWalker.Event event = this.next;
            if (event != null) {
                this.next = null;
                return event;
            }
            throw new NoSuchElementException();
        }
        throw new IllegalStateException();
    }

    public void close() {
        this.walker.close();
    }
}
