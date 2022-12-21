package java.security;

import java.p026io.IOException;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;

public class GuardedObject implements Serializable {
    private static final long serialVersionUID = -5240450096227834308L;
    private Guard guard;
    private Object object;

    public GuardedObject(Object obj, Guard guard2) {
        this.guard = guard2;
        this.object = obj;
    }

    public Object getObject() throws SecurityException {
        Guard guard2 = this.guard;
        if (guard2 != null) {
            guard2.checkGuard(this.object);
        }
        return this.object;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Guard guard2 = this.guard;
        if (guard2 != null) {
            guard2.checkGuard(this.object);
        }
        objectOutputStream.defaultWriteObject();
    }
}
