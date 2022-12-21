package java.nio.file;

public class ProviderMismatchException extends IllegalArgumentException {
    static final long serialVersionUID = 4990847485741612530L;

    public ProviderMismatchException() {
    }

    public ProviderMismatchException(String str) {
        super(str);
    }
}
