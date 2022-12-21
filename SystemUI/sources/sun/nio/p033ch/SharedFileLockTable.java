package sun.nio.p033ch;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.channels.Channel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* renamed from: sun.nio.ch.SharedFileLockTable */
/* compiled from: FileLockTable */
class SharedFileLockTable extends FileLockTable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static ConcurrentHashMap<FileKey, List<FileLockReference>> lockMap = new ConcurrentHashMap<>();
    private static ReferenceQueue<FileLock> queue = new ReferenceQueue<>();
    private final Channel channel;
    private final FileKey fileKey;

    /* renamed from: sun.nio.ch.SharedFileLockTable$FileLockReference */
    /* compiled from: FileLockTable */
    private static class FileLockReference extends WeakReference<FileLock> {
        private FileKey fileKey;

        FileLockReference(FileLock fileLock, ReferenceQueue<FileLock> referenceQueue, FileKey fileKey2) {
            super(fileLock, referenceQueue);
            this.fileKey = fileKey2;
        }

        /* access modifiers changed from: package-private */
        public FileKey fileKey() {
            return this.fileKey;
        }
    }

    SharedFileLockTable(Channel channel2, FileDescriptor fileDescriptor) throws IOException {
        this.channel = channel2;
        this.fileKey = FileKey.create(fileDescriptor);
    }

    public void add(FileLock fileLock) throws OverlappingFileLockException {
        List list;
        List list2 = lockMap.get(this.fileKey);
        while (true) {
            if (list2 == null) {
                ArrayList arrayList = new ArrayList(2);
                synchronized (arrayList) {
                    list2 = lockMap.putIfAbsent(this.fileKey, arrayList);
                    if (list2 == null) {
                        arrayList.add(new FileLockReference(fileLock, queue, this.fileKey));
                        break;
                    }
                }
            }
            synchronized (list2) {
                list = lockMap.get(this.fileKey);
                if (list2 == list) {
                    checkList(list2, fileLock.position(), fileLock.size());
                    list2.add(new FileLockReference(fileLock, queue, this.fileKey));
                }
            }
            list2 = list;
        }
        removeStaleEntries();
    }

    private void removeKeyIfEmpty(FileKey fileKey2, List<FileLockReference> list) {
        if (list.isEmpty()) {
            lockMap.remove(fileKey2);
        }
    }

    public void remove(FileLock fileLock) {
        List list = lockMap.get(this.fileKey);
        if (list != null) {
            synchronized (list) {
                int i = 0;
                while (true) {
                    if (i >= list.size()) {
                        break;
                    }
                    FileLockReference fileLockReference = (FileLockReference) list.get(i);
                    if (((FileLock) fileLockReference.get()) == fileLock) {
                        fileLockReference.clear();
                        list.remove(i);
                        break;
                    }
                    i++;
                }
            }
        }
    }

    public List<FileLock> removeAll() {
        ArrayList arrayList = new ArrayList();
        List list = lockMap.get(this.fileKey);
        if (list != null) {
            synchronized (list) {
                int i = 0;
                while (i < list.size()) {
                    FileLockReference fileLockReference = (FileLockReference) list.get(i);
                    FileLock fileLock = (FileLock) fileLockReference.get();
                    if (fileLock == null || fileLock.acquiredBy() != this.channel) {
                        i++;
                    } else {
                        fileLockReference.clear();
                        list.remove(i);
                        arrayList.add(fileLock);
                    }
                }
                removeKeyIfEmpty(this.fileKey, list);
            }
        }
        return arrayList;
    }

    public void replace(FileLock fileLock, FileLock fileLock2) {
        List list = lockMap.get(this.fileKey);
        synchronized (list) {
            int i = 0;
            while (true) {
                if (i >= list.size()) {
                    break;
                }
                FileLockReference fileLockReference = (FileLockReference) list.get(i);
                if (((FileLock) fileLockReference.get()) == fileLock) {
                    fileLockReference.clear();
                    list.set(i, new FileLockReference(fileLock2, queue, this.fileKey));
                    break;
                }
                i++;
            }
        }
    }

    private void checkList(List<FileLockReference> list, long j, long j2) throws OverlappingFileLockException {
        for (FileLockReference fileLockReference : list) {
            FileLock fileLock = (FileLock) fileLockReference.get();
            if (fileLock != null && fileLock.overlaps(j, j2)) {
                throw new OverlappingFileLockException();
            }
        }
    }

    private void removeStaleEntries() {
        while (true) {
            FileLockReference fileLockReference = (FileLockReference) queue.poll();
            if (fileLockReference != null) {
                FileKey fileKey2 = fileLockReference.fileKey();
                List list = lockMap.get(fileKey2);
                if (list != null) {
                    synchronized (list) {
                        list.remove((Object) fileLockReference);
                        removeKeyIfEmpty(fileKey2, list);
                    }
                }
            } else {
                return;
            }
        }
    }
}
