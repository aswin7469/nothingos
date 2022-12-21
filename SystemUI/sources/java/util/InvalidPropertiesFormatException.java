package java.util;

import java.p026io.IOException;
import java.p026io.NotSerializableException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;

public class InvalidPropertiesFormatException extends IOException {
    private static final long serialVersionUID = 7763056076009360219L;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public InvalidPropertiesFormatException(Throwable th) {
        super(th == null ? null : th.toString());
        initCause(th);
    }

    public InvalidPropertiesFormatException(String str) {
        super(str);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws NotSerializableException {
        throw new NotSerializableException("Not serializable.");
    }

    private void readObject(ObjectInputStream objectInputStream) throws NotSerializableException {
        throw new NotSerializableException("Not serializable.");
    }
}
