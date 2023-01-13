package sun.nio.p035fs;

import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/* renamed from: sun.nio.fs.AbstractPoller */
abstract class AbstractPoller implements Runnable {
    private final LinkedList<Request> requestList = new LinkedList<>();
    private boolean shutdown = false;

    /* renamed from: sun.nio.fs.AbstractPoller$RequestType */
    private enum RequestType {
        REGISTER,
        CANCEL,
        CLOSE
    }

    /* access modifiers changed from: package-private */
    public abstract void implCancelKey(WatchKey watchKey);

    /* access modifiers changed from: package-private */
    public abstract void implCloseAll();

    /* access modifiers changed from: package-private */
    public abstract Object implRegister(Path path, Set<? extends WatchEvent.Kind<?>> set, WatchEvent.Modifier... modifierArr);

    /* access modifiers changed from: package-private */
    public abstract void wakeup() throws IOException;

    protected AbstractPoller() {
    }

    public void start() {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                Thread thread = new Thread(this);
                thread.setDaemon(true);
                thread.start();
                return null;
            }
        });
    }

    /* access modifiers changed from: package-private */
    public final WatchKey register(Path path, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException {
        path.getClass();
        HashSet hashSet = new HashSet(kindArr.length);
        for (WatchEvent.Kind<Object> kind : kindArr) {
            if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY || kind == StandardWatchEventKinds.ENTRY_DELETE) {
                hashSet.add(kind);
            } else if (kind != StandardWatchEventKinds.OVERFLOW) {
                if (kind == null) {
                    throw new NullPointerException("An element in event set is 'null'");
                }
                throw new UnsupportedOperationException(kind.name());
            }
        }
        if (!hashSet.isEmpty()) {
            return (WatchKey) invoke(RequestType.REGISTER, path, hashSet, modifierArr);
        }
        throw new IllegalArgumentException("No events to register");
    }

    /* access modifiers changed from: package-private */
    public final void cancel(WatchKey watchKey) {
        try {
            invoke(RequestType.CANCEL, watchKey);
        } catch (IOException e) {
            throw new AssertionError((Object) e.getMessage());
        }
    }

    /* access modifiers changed from: package-private */
    public final void close() throws IOException {
        invoke(RequestType.CLOSE, new Object[0]);
    }

    /* renamed from: sun.nio.fs.AbstractPoller$Request */
    private static class Request {
        private boolean completed = false;
        private final Object[] params;
        private Object result = null;
        private final RequestType type;

        Request(RequestType requestType, Object... objArr) {
            this.type = requestType;
            this.params = objArr;
        }

        /* access modifiers changed from: package-private */
        public RequestType type() {
            return this.type;
        }

        /* access modifiers changed from: package-private */
        public Object[] parameters() {
            return this.params;
        }

        /* access modifiers changed from: package-private */
        public void release(Object obj) {
            synchronized (this) {
                this.completed = true;
                this.result = obj;
                notifyAll();
            }
        }

        /* access modifiers changed from: package-private */
        public Object awaitResult() {
            Object obj;
            synchronized (this) {
                boolean z = false;
                while (!this.completed) {
                    try {
                        wait();
                    } catch (InterruptedException unused) {
                        z = true;
                    }
                }
                if (z) {
                    Thread.currentThread().interrupt();
                }
                obj = this.result;
            }
            return obj;
        }
    }

    private Object invoke(RequestType requestType, Object... objArr) throws IOException {
        Request request = new Request(requestType, objArr);
        synchronized (this.requestList) {
            if (!this.shutdown) {
                this.requestList.add(request);
            } else {
                throw new ClosedWatchServiceException();
            }
        }
        wakeup();
        Object awaitResult = request.awaitResult();
        if (awaitResult instanceof RuntimeException) {
            throw ((RuntimeException) awaitResult);
        } else if (!(awaitResult instanceof IOException)) {
            return awaitResult;
        } else {
            throw ((IOException) awaitResult);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean processRequests() {
        synchronized (this.requestList) {
            while (true) {
                Request poll = this.requestList.poll();
                if (poll != null) {
                    if (this.shutdown) {
                        poll.release(new ClosedWatchServiceException());
                    }
                    int i = C47952.$SwitchMap$sun$nio$fs$AbstractPoller$RequestType[poll.type().ordinal()];
                    if (i == 1) {
                        Object[] parameters = poll.parameters();
                        poll.release(implRegister((Path) parameters[0], (Set) parameters[1], (WatchEvent.Modifier[]) parameters[2]));
                    } else if (i == 2) {
                        implCancelKey((WatchKey) poll.parameters()[0]);
                        poll.release((Object) null);
                    } else if (i != 3) {
                        poll.release(new IOException("request not recognized"));
                    } else {
                        implCloseAll();
                        poll.release((Object) null);
                        this.shutdown = true;
                    }
                }
            }
            while (true) {
            }
        }
        return this.shutdown;
    }

    /* renamed from: sun.nio.fs.AbstractPoller$2 */
    static /* synthetic */ class C47952 {
        static final /* synthetic */ int[] $SwitchMap$sun$nio$fs$AbstractPoller$RequestType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                sun.nio.fs.AbstractPoller$RequestType[] r0 = sun.nio.p035fs.AbstractPoller.RequestType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$sun$nio$fs$AbstractPoller$RequestType = r0
                sun.nio.fs.AbstractPoller$RequestType r1 = sun.nio.p035fs.AbstractPoller.RequestType.REGISTER     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$sun$nio$fs$AbstractPoller$RequestType     // Catch:{ NoSuchFieldError -> 0x001d }
                sun.nio.fs.AbstractPoller$RequestType r1 = sun.nio.p035fs.AbstractPoller.RequestType.CANCEL     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$sun$nio$fs$AbstractPoller$RequestType     // Catch:{ NoSuchFieldError -> 0x0028 }
                sun.nio.fs.AbstractPoller$RequestType r1 = sun.nio.p035fs.AbstractPoller.RequestType.CLOSE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.AbstractPoller.C47952.<clinit>():void");
        }
    }
}
