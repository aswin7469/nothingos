package java.p026io;

import java.util.Objects;

/* renamed from: java.io.UncheckedIOException */
public class UncheckedIOException extends RuntimeException {
    private static final long serialVersionUID = -8134305061645241065L;

    public UncheckedIOException(String str, IOException iOException) {
        super(str, (Throwable) Objects.requireNonNull(iOException));
    }

    public UncheckedIOException(IOException iOException) {
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
