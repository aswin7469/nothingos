package javax.net.ssl;

public abstract class SNIMatcher {
    private final int type;

    public abstract boolean matches(SNIServerName sNIServerName);

    protected SNIMatcher(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Server name type cannot be less than zero");
        } else if (i <= 255) {
            this.type = i;
        } else {
            throw new IllegalArgumentException("Server name type cannot be greater than 255");
        }
    }

    public final int getType() {
        return this.type;
    }
}
