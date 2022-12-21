package org.xml.sax;

public class SAXException extends Exception {
    private Exception exception;

    public SAXException() {
        this.exception = null;
    }

    public SAXException(String str) {
        super(str);
        this.exception = null;
    }

    public SAXException(Exception exc) {
        this.exception = exc;
    }

    public SAXException(String str, Exception exc) {
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
        throw new UnsupportedOperationException("Method not decompiled: org.xml.sax.SAXException.getMessage():java.lang.String");
    }

    public Exception getException() {
        return this.exception;
    }

    public String toString() {
        Exception exc = this.exception;
        if (exc != null) {
            return exc.toString();
        }
        return super.toString();
    }
}
