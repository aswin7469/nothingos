package java.nio.file;

import java.nio.file.attribute.BasicFileAttributes;
import java.p026io.Closeable;
import java.p026io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import sun.nio.p035fs.BasicFileAttributesHolder;

class FileTreeWalker implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private boolean closed;
    private final boolean followLinks;
    private final LinkOption[] linkOptions;
    private final int maxDepth;
    private final ArrayDeque<DirectoryNode> stack = new ArrayDeque<>();

    enum EventType {
        START_DIRECTORY,
        END_DIRECTORY,
        ENTRY
    }

    private static class DirectoryNode {
        private final Path dir;
        private final Iterator<Path> iterator;
        private final Object key;
        private boolean skipped;
        private final DirectoryStream<Path> stream;

        DirectoryNode(Path path, Object obj, DirectoryStream<Path> directoryStream) {
            this.dir = path;
            this.key = obj;
            this.stream = directoryStream;
            this.iterator = directoryStream.iterator();
        }

        /* access modifiers changed from: package-private */
        public Path directory() {
            return this.dir;
        }

        /* access modifiers changed from: package-private */
        public Object key() {
            return this.key;
        }

        /* access modifiers changed from: package-private */
        public DirectoryStream<Path> stream() {
            return this.stream;
        }

        /* access modifiers changed from: package-private */
        public Iterator<Path> iterator() {
            return this.iterator;
        }

        /* access modifiers changed from: package-private */
        public void skip() {
            this.skipped = true;
        }

        /* access modifiers changed from: package-private */
        public boolean skipped() {
            return this.skipped;
        }
    }

    static class Event {
        private final BasicFileAttributes attrs;
        private final Path file;
        private final IOException ioe;
        private final EventType type;

        private Event(EventType eventType, Path path, BasicFileAttributes basicFileAttributes, IOException iOException) {
            this.type = eventType;
            this.file = path;
            this.attrs = basicFileAttributes;
            this.ioe = iOException;
        }

        Event(EventType eventType, Path path, BasicFileAttributes basicFileAttributes) {
            this(eventType, path, basicFileAttributes, (IOException) null);
        }

        Event(EventType eventType, Path path, IOException iOException) {
            this(eventType, path, (BasicFileAttributes) null, iOException);
        }

        /* access modifiers changed from: package-private */
        public EventType type() {
            return this.type;
        }

        /* access modifiers changed from: package-private */
        public Path file() {
            return this.file;
        }

        /* access modifiers changed from: package-private */
        public BasicFileAttributes attributes() {
            return this.attrs;
        }

        /* access modifiers changed from: package-private */
        public IOException ioeException() {
            return this.ioe;
        }
    }

    FileTreeWalker(Collection<FileVisitOption> collection, int i) {
        boolean z = false;
        for (FileVisitOption ordinal : collection) {
            if (C43591.$SwitchMap$java$nio$file$FileVisitOption[ordinal.ordinal()] == 1) {
                z = true;
            } else {
                throw new AssertionError((Object) "Should not get here");
            }
        }
        if (i >= 0) {
            this.followLinks = z;
            this.linkOptions = z ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
            this.maxDepth = i;
            return;
        }
        throw new IllegalArgumentException("'maxDepth' is negative");
    }

    /* renamed from: java.nio.file.FileTreeWalker$1 */
    static /* synthetic */ class C43591 {
        static final /* synthetic */ int[] $SwitchMap$java$nio$file$FileVisitOption;

        static {
            int[] iArr = new int[FileVisitOption.values().length];
            $SwitchMap$java$nio$file$FileVisitOption = iArr;
            try {
                iArr[FileVisitOption.FOLLOW_LINKS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private BasicFileAttributes getAttributes(Path path, boolean z) throws IOException {
        BasicFileAttributes basicFileAttributes;
        if (z && (path instanceof BasicFileAttributesHolder) && System.getSecurityManager() == null && (basicFileAttributes = ((BasicFileAttributesHolder) path).get()) != null && (!this.followLinks || !basicFileAttributes.isSymbolicLink())) {
            return basicFileAttributes;
        }
        try {
            return Files.readAttributes(path, BasicFileAttributes.class, this.linkOptions);
        } catch (IOException e) {
            if (this.followLinks) {
                return Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            }
            throw e;
        }
    }

    private boolean wouldLoop(Path path, Object obj) {
        Iterator<DirectoryNode> it = this.stack.iterator();
        while (it.hasNext()) {
            DirectoryNode next = it.next();
            Object key = next.key();
            if (obj == null || key == null) {
                try {
                    if (Files.isSameFile(path, next.directory())) {
                        return true;
                    }
                } catch (IOException | SecurityException unused) {
                    continue;
                }
            } else if (obj.equals(key)) {
                return true;
            }
        }
        return false;
    }

    private Event visit(Path path, boolean z, boolean z2) {
        try {
            BasicFileAttributes attributes = getAttributes(path, z2);
            if (this.stack.size() >= this.maxDepth || !attributes.isDirectory()) {
                return new Event(EventType.ENTRY, path, attributes);
            }
            if (this.followLinks && wouldLoop(path, attributes.fileKey())) {
                return new Event(EventType.ENTRY, path, (IOException) new FileSystemLoopException(path.toString()));
            }
            try {
                this.stack.push(new DirectoryNode(path, attributes.fileKey(), Files.newDirectoryStream(path)));
                return new Event(EventType.START_DIRECTORY, path, attributes);
            } catch (IOException e) {
                return new Event(EventType.ENTRY, path, e);
            } catch (SecurityException e2) {
                if (z) {
                    return null;
                }
                throw e2;
            }
        } catch (IOException e3) {
            return new Event(EventType.ENTRY, path, e3);
        } catch (SecurityException e4) {
            if (z) {
                return null;
            }
            throw e4;
        }
    }

    /* access modifiers changed from: package-private */
    public Event walk(Path path) {
        if (!this.closed) {
            return visit(path, false, false);
        }
        throw new IllegalStateException("Closed");
    }

    /* access modifiers changed from: package-private */
    public Event next() {
        IOException iOException;
        Path path;
        Event visit;
        DirectoryNode peek = this.stack.peek();
        if (peek == null) {
            return null;
        }
        do {
            if (!peek.skipped()) {
                Iterator<Path> it = peek.iterator();
                try {
                    path = it.hasNext() ? it.next() : null;
                    iOException = null;
                } catch (DirectoryIteratorException e) {
                    iOException = e.getCause();
                    path = null;
                }
            } else {
                path = null;
                iOException = null;
            }
            if (path == null) {
                try {
                    peek.stream().close();
                } catch (IOException e2) {
                    if (iOException == null) {
                        iOException = e2;
                    } else {
                        iOException.addSuppressed(e2);
                    }
                }
                this.stack.pop();
                return new Event(EventType.END_DIRECTORY, peek.directory(), iOException);
            }
            visit = visit(path, true, true);
        } while (visit == null);
        return visit;
    }

    /* access modifiers changed from: package-private */
    public void pop() {
        if (!this.stack.isEmpty()) {
            try {
                this.stack.pop().stream().close();
            } catch (IOException unused) {
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void skipRemainingSiblings() {
        if (!this.stack.isEmpty()) {
            this.stack.peek().skip();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isOpen() {
        return !this.closed;
    }

    public void close() {
        if (!this.closed) {
            while (!this.stack.isEmpty()) {
                pop();
            }
            this.closed = true;
        }
    }
}
