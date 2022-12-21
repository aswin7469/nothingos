package javax.xml.xpath;

import java.p026io.PrintStream;
import java.p026io.PrintWriter;

public class XPathException extends Exception {
    private static final long serialVersionUID = -1837080260374986980L;
    private final Throwable cause;

    public XPathException(String str) {
        super(str);
        if (str != null) {
            this.cause = null;
            return;
        }
        throw new NullPointerException("message == null");
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public XPathException(Throwable th) {
        super(th == null ? null : th.toString());
        this.cause = th;
        if (th == null) {
            throw new NullPointerException("cause == null");
        }
    }

    public Throwable getCause() {
        return this.cause;
    }

    public void printStackTrace(PrintStream printStream) {
        if (getCause() != null) {
            getCause().printStackTrace(printStream);
            printStream.println("--------------- linked to ------------------");
        }
        super.printStackTrace(printStream);
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintWriter printWriter) {
        if (getCause() != null) {
            getCause().printStackTrace(printWriter);
            printWriter.println("--------------- linked to ------------------");
        }
        super.printStackTrace(printWriter);
    }
}
