package java.p026io;

/* renamed from: java.io.SerialCallbackContext */
final class SerialCallbackContext {
    private final ObjectStreamClass desc;
    private final Object obj;
    private Thread thread = Thread.currentThread();

    public SerialCallbackContext(Object obj2, ObjectStreamClass objectStreamClass) {
        this.obj = obj2;
        this.desc = objectStreamClass;
    }

    public Object getObj() throws NotActiveException {
        checkAndSetUsed();
        return this.obj;
    }

    public ObjectStreamClass getDesc() {
        return this.desc;
    }

    public void check() throws NotActiveException {
        Thread thread2 = this.thread;
        if (thread2 != null && thread2 != Thread.currentThread()) {
            throw new NotActiveException("expected thread: " + this.thread + ", but got: " + Thread.currentThread());
        }
    }

    private void checkAndSetUsed() throws NotActiveException {
        if (this.thread == Thread.currentThread()) {
            this.thread = null;
            return;
        }
        throw new NotActiveException("not in readObject invocation or fields already read");
    }

    public void setUsed() {
        this.thread = null;
    }
}
