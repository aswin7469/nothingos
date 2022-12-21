package java.nio.file;

import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.util.ConcurrentModificationException;
import java.util.Objects;

public final class DirectoryIteratorException extends ConcurrentModificationException {
    private static final long serialVersionUID = -6012699886086212874L;

    public DirectoryIteratorException(IOException iOException) {
        super((Throwable) Objects.requireNonNull(iOException));
    }

    public IOException getCause() {
        return (IOException) super.getCause();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (!(super.getCause() instanceof IOException)) {
            throw new InvalidObjectException("Cause must be an IOException");
        }
    }
}
