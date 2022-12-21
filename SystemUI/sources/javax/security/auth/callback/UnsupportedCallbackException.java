package javax.security.auth.callback;

public class UnsupportedCallbackException extends Exception {
    private static final long serialVersionUID = -6873556327655666839L;
    private Callback callback;

    public UnsupportedCallbackException(Callback callback2) {
        this.callback = callback2;
    }

    public UnsupportedCallbackException(Callback callback2, String str) {
        super(str);
        this.callback = callback2;
    }

    public Callback getCallback() {
        return this.callback;
    }
}
