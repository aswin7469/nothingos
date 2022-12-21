package java.lang;

public class IndexOutOfBoundsException extends RuntimeException {
    private static final long serialVersionUID = 234122996006267687L;

    public IndexOutOfBoundsException() {
    }

    public IndexOutOfBoundsException(String str) {
        super(str);
    }

    public IndexOutOfBoundsException(int i) {
        super("Index out of range: " + i);
    }
}
