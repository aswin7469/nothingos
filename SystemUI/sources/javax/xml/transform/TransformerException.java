package javax.xml.transform;

import java.p026io.OutputStream;
import java.p026io.PrintStream;
import java.p026io.PrintWriter;

public class TransformerException extends Exception {
    private static final long serialVersionUID = 975798773772956428L;
    Throwable containedException;
    SourceLocator locator;

    public SourceLocator getLocator() {
        return this.locator;
    }

    public void setLocator(SourceLocator sourceLocator) {
        this.locator = sourceLocator;
    }

    public Throwable getException() {
        return this.containedException;
    }

    public Throwable getCause() {
        Throwable th = this.containedException;
        if (th == this) {
            return null;
        }
        return th;
    }

    public synchronized Throwable initCause(Throwable th) {
        if (this.containedException != null) {
            throw new IllegalStateException("Can't overwrite cause");
        } else if (th != this) {
            this.containedException = th;
        } else {
            throw new IllegalArgumentException("Self-causation not permitted");
        }
        return this;
    }

    public TransformerException(String str) {
        super(str);
        this.containedException = null;
        this.locator = null;
    }

    public TransformerException(Throwable th) {
        super(th.toString());
        this.containedException = th;
        this.locator = null;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TransformerException(java.lang.String r2, java.lang.Throwable r3) {
        /*
            r1 = this;
            if (r2 == 0) goto L_0x0008
            int r0 = r2.length()
            if (r0 != 0) goto L_0x000c
        L_0x0008:
            java.lang.String r2 = r3.toString()
        L_0x000c:
            r1.<init>((java.lang.String) r2)
            r1.containedException = r3
            r2 = 0
            r1.locator = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.xml.transform.TransformerException.<init>(java.lang.String, java.lang.Throwable):void");
    }

    public TransformerException(String str, SourceLocator sourceLocator) {
        super(str);
        this.containedException = null;
        this.locator = sourceLocator;
    }

    public TransformerException(String str, SourceLocator sourceLocator, Throwable th) {
        super(str);
        this.containedException = th;
        this.locator = sourceLocator;
    }

    public String getMessageAndLocation() {
        StringBuilder sb = new StringBuilder();
        String message = super.getMessage();
        if (message != null) {
            sb.append(message);
        }
        SourceLocator sourceLocator = this.locator;
        if (sourceLocator != null) {
            String systemId = sourceLocator.getSystemId();
            int lineNumber = this.locator.getLineNumber();
            int columnNumber = this.locator.getColumnNumber();
            if (systemId != null) {
                sb.append("; SystemID: ");
                sb.append(systemId);
            }
            if (lineNumber != 0) {
                sb.append("; Line#: ");
                sb.append(lineNumber);
            }
            if (columnNumber != 0) {
                sb.append("; Column#: ");
                sb.append(columnNumber);
            }
        }
        return sb.toString();
    }

    public String getLocationAsString() {
        if (this.locator == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String systemId = this.locator.getSystemId();
        int lineNumber = this.locator.getLineNumber();
        int columnNumber = this.locator.getColumnNumber();
        if (systemId != null) {
            sb.append("; SystemID: ");
            sb.append(systemId);
        }
        if (lineNumber != 0) {
            sb.append("; Line#: ");
            sb.append(lineNumber);
        }
        if (columnNumber != 0) {
            sb.append("; Column#: ");
            sb.append(columnNumber);
        }
        return sb.toString();
    }

    public void printStackTrace() {
        printStackTrace(new PrintWriter((OutputStream) System.err, true));
    }

    public void printStackTrace(PrintStream printStream) {
        printStackTrace(new PrintWriter((OutputStream) printStream));
    }

    public void printStackTrace(PrintWriter printWriter) {
        if (printWriter == null) {
            printWriter = new PrintWriter((OutputStream) System.err, true);
        }
        try {
            String locationAsString = getLocationAsString();
            if (locationAsString != null) {
                printWriter.println(locationAsString);
            }
            super.printStackTrace(printWriter);
        } catch (Throwable unused) {
        }
    }
}
