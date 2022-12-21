package javax.xml.transform;

public class TransformerFactoryConfigurationError extends Error {
    private Exception exception;

    public TransformerFactoryConfigurationError() {
        this.exception = null;
    }

    public TransformerFactoryConfigurationError(String str) {
        super(str);
        this.exception = null;
    }

    public TransformerFactoryConfigurationError(Exception exc) {
        super(exc.toString());
        this.exception = exc;
    }

    public TransformerFactoryConfigurationError(Exception exc, String str) {
        super(str);
        this.exception = exc;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = r1.exception;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getMessage() {
        /*
            r1 = this;
            java.lang.String r0 = super.getMessage()
            if (r0 != 0) goto L_0x000f
            java.lang.Exception r1 = r1.exception
            if (r1 == 0) goto L_0x000f
            java.lang.String r1 = r1.getMessage()
            return r1
        L_0x000f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.xml.transform.TransformerFactoryConfigurationError.getMessage():java.lang.String");
    }

    public Exception getException() {
        return this.exception;
    }
}
