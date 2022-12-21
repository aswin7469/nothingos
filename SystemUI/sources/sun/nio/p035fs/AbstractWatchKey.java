package sun.nio.p035fs;

import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* renamed from: sun.nio.fs.AbstractWatchKey */
abstract class AbstractWatchKey implements WatchKey {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final int MAX_EVENT_LIST_SIZE = 512;
    static final Event<Object> OVERFLOW_EVENT = new Event<>(StandardWatchEventKinds.OVERFLOW, null);
    private final Path dir;
    private List<WatchEvent<?>> events = new ArrayList();
    private Map<Object, WatchEvent<?>> lastModifyEvents = new HashMap();
    private State state = State.READY;
    private final AbstractWatchService watcher;

    /* renamed from: sun.nio.fs.AbstractWatchKey$State */
    private enum State {
        READY,
        SIGNALLED
    }

    protected AbstractWatchKey(Path path, AbstractWatchService abstractWatchService) {
        this.watcher = abstractWatchService;
        this.dir = path;
    }

    /* access modifiers changed from: package-private */
    public final AbstractWatchService watcher() {
        return this.watcher;
    }

    public Path watchable() {
        return this.dir;
    }

    /* access modifiers changed from: package-private */
    public final void signal() {
        synchronized (this) {
            if (this.state == State.READY) {
                this.state = State.SIGNALLED;
                this.watcher.enqueueKey(this);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x006b  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0071  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void signalEvent(java.nio.file.WatchEvent.Kind<?> r7, java.lang.Object r8) {
        /*
            r6 = this;
            java.nio.file.WatchEvent$Kind<java.nio.file.Path> r0 = java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
            r1 = 0
            if (r7 != r0) goto L_0x0007
            r0 = 1
            goto L_0x0008
        L_0x0007:
            r0 = r1
        L_0x0008:
            monitor-enter(r6)
            java.util.List<java.nio.file.WatchEvent<?>> r2 = r6.events     // Catch:{ all -> 0x0089 }
            int r2 = r2.size()     // Catch:{ all -> 0x0089 }
            if (r2 <= 0) goto L_0x0063
            java.util.List<java.nio.file.WatchEvent<?>> r3 = r6.events     // Catch:{ all -> 0x0089 }
            int r4 = r2 + -1
            java.lang.Object r3 = r3.get(r4)     // Catch:{ all -> 0x0089 }
            java.nio.file.WatchEvent r3 = (java.nio.file.WatchEvent) r3     // Catch:{ all -> 0x0089 }
            java.nio.file.WatchEvent$Kind r4 = r3.kind()     // Catch:{ all -> 0x0089 }
            java.nio.file.WatchEvent$Kind<java.lang.Object> r5 = java.nio.file.StandardWatchEventKinds.OVERFLOW     // Catch:{ all -> 0x0089 }
            if (r4 == r5) goto L_0x005c
            java.nio.file.WatchEvent$Kind r4 = r3.kind()     // Catch:{ all -> 0x0089 }
            if (r7 != r4) goto L_0x0034
            java.lang.Object r4 = r3.context()     // Catch:{ all -> 0x0089 }
            boolean r4 = java.util.Objects.equals(r8, r4)     // Catch:{ all -> 0x0089 }
            if (r4 == 0) goto L_0x0034
            goto L_0x005c
        L_0x0034:
            java.util.Map<java.lang.Object, java.nio.file.WatchEvent<?>> r3 = r6.lastModifyEvents     // Catch:{ all -> 0x0089 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0089 }
            if (r3 != 0) goto L_0x0054
            if (r0 == 0) goto L_0x004f
            java.util.Map<java.lang.Object, java.nio.file.WatchEvent<?>> r3 = r6.lastModifyEvents     // Catch:{ all -> 0x0089 }
            java.lang.Object r3 = r3.get(r8)     // Catch:{ all -> 0x0089 }
            java.nio.file.WatchEvent r3 = (java.nio.file.WatchEvent) r3     // Catch:{ all -> 0x0089 }
            if (r3 == 0) goto L_0x0054
            sun.nio.fs.AbstractWatchKey$Event r3 = (sun.nio.p035fs.AbstractWatchKey.Event) r3     // Catch:{ all -> 0x0089 }
            r3.increment()     // Catch:{ all -> 0x0089 }
            monitor-exit(r6)     // Catch:{ all -> 0x0089 }
            return
        L_0x004f:
            java.util.Map<java.lang.Object, java.nio.file.WatchEvent<?>> r3 = r6.lastModifyEvents     // Catch:{ all -> 0x0089 }
            r3.remove(r8)     // Catch:{ all -> 0x0089 }
        L_0x0054:
            r3 = 512(0x200, float:7.175E-43)
            if (r2 < r3) goto L_0x0063
            java.nio.file.WatchEvent$Kind<java.lang.Object> r7 = java.nio.file.StandardWatchEventKinds.OVERFLOW     // Catch:{ all -> 0x0089 }
            r8 = 0
            goto L_0x0064
        L_0x005c:
            sun.nio.fs.AbstractWatchKey$Event r3 = (sun.nio.p035fs.AbstractWatchKey.Event) r3     // Catch:{ all -> 0x0089 }
            r3.increment()     // Catch:{ all -> 0x0089 }
            monitor-exit(r6)     // Catch:{ all -> 0x0089 }
            return
        L_0x0063:
            r1 = r0
        L_0x0064:
            sun.nio.fs.AbstractWatchKey$Event r0 = new sun.nio.fs.AbstractWatchKey$Event     // Catch:{ all -> 0x0089 }
            r0.<init>(r7, r8)     // Catch:{ all -> 0x0089 }
            if (r1 == 0) goto L_0x0071
            java.util.Map<java.lang.Object, java.nio.file.WatchEvent<?>> r7 = r6.lastModifyEvents     // Catch:{ all -> 0x0089 }
            r7.put(r8, r0)     // Catch:{ all -> 0x0089 }
            goto L_0x007f
        L_0x0071:
            java.nio.file.WatchEvent$Kind<java.lang.Object> r8 = java.nio.file.StandardWatchEventKinds.OVERFLOW     // Catch:{ all -> 0x0089 }
            if (r7 != r8) goto L_0x007f
            java.util.List<java.nio.file.WatchEvent<?>> r7 = r6.events     // Catch:{ all -> 0x0089 }
            r7.clear()     // Catch:{ all -> 0x0089 }
            java.util.Map<java.lang.Object, java.nio.file.WatchEvent<?>> r7 = r6.lastModifyEvents     // Catch:{ all -> 0x0089 }
            r7.clear()     // Catch:{ all -> 0x0089 }
        L_0x007f:
            java.util.List<java.nio.file.WatchEvent<?>> r7 = r6.events     // Catch:{ all -> 0x0089 }
            r7.add(r0)     // Catch:{ all -> 0x0089 }
            r6.signal()     // Catch:{ all -> 0x0089 }
            monitor-exit(r6)     // Catch:{ all -> 0x0089 }
            return
        L_0x0089:
            r7 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0089 }
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.AbstractWatchKey.signalEvent(java.nio.file.WatchEvent$Kind, java.lang.Object):void");
    }

    public final List<WatchEvent<?>> pollEvents() {
        List<WatchEvent<?>> list;
        synchronized (this) {
            list = this.events;
            this.events = new ArrayList();
            this.lastModifyEvents.clear();
        }
        return list;
    }

    public final boolean reset() {
        boolean isValid;
        synchronized (this) {
            if (this.state == State.SIGNALLED && isValid()) {
                if (this.events.isEmpty()) {
                    this.state = State.READY;
                } else {
                    this.watcher.enqueueKey(this);
                }
            }
            isValid = isValid();
        }
        return isValid;
    }

    /* renamed from: sun.nio.fs.AbstractWatchKey$Event */
    private static class Event<T> implements WatchEvent<T> {
        private final T context;
        private int count = 1;
        private final WatchEvent.Kind<T> kind;

        Event(WatchEvent.Kind<T> kind2, T t) {
            this.kind = kind2;
            this.context = t;
        }

        public WatchEvent.Kind<T> kind() {
            return this.kind;
        }

        public T context() {
            return this.context;
        }

        public int count() {
            return this.count;
        }

        /* access modifiers changed from: package-private */
        public void increment() {
            this.count++;
        }
    }
}
