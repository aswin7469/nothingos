package javax.xml.datatype;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.OutputStream;
import java.p026io.PrintStream;
import java.p026io.PrintWriter;

public class DatatypeConfigurationException extends Exception {
    private static final long serialVersionUID = -1699373159027047238L;
    private Throwable causeOnJDK13OrBelow;
    private transient boolean isJDK14OrAbove;

    public DatatypeConfigurationException() {
        this.isJDK14OrAbove = false;
    }

    public DatatypeConfigurationException(String str) {
        super(str);
        this.isJDK14OrAbove = false;
    }

    public DatatypeConfigurationException(String str, Throwable th) {
        super(str);
        this.isJDK14OrAbove = false;
        initCauseByReflection(th);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DatatypeConfigurationException(Throwable th) {
        super(th == null ? null : th.toString());
        this.isJDK14OrAbove = false;
        initCauseByReflection(th);
    }

    public void printStackTrace() {
        if (this.isJDK14OrAbove || this.causeOnJDK13OrBelow == null) {
            super.printStackTrace();
        } else {
            printStackTrace0(new PrintWriter((OutputStream) System.err, true));
        }
    }

    public void printStackTrace(PrintStream printStream) {
        if (this.isJDK14OrAbove || this.causeOnJDK13OrBelow == null) {
            super.printStackTrace(printStream);
        } else {
            printStackTrace0(new PrintWriter((OutputStream) printStream));
        }
    }

    public void printStackTrace(PrintWriter printWriter) {
        if (this.isJDK14OrAbove || this.causeOnJDK13OrBelow == null) {
            super.printStackTrace(printWriter);
        } else {
            printStackTrace0(printWriter);
        }
    }

    private void printStackTrace0(PrintWriter printWriter) {
        this.causeOnJDK13OrBelow.printStackTrace(printWriter);
        printWriter.println("------------------------------------------");
        super.printStackTrace(printWriter);
    }

    private void initCauseByReflection(Throwable th) {
        this.causeOnJDK13OrBelow = th;
        try {
            getClass().getMethod("initCause", Throwable.class).invoke(this, th);
            this.isJDK14OrAbove = true;
        } catch (Exception unused) {
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            Throwable th = (Throwable) getClass().getMethod("getCause", new Class[0]).invoke(this, new Object[0]);
            if (this.causeOnJDK13OrBelow == null) {
                this.causeOnJDK13OrBelow = th;
            } else if (th == null) {
                getClass().getMethod("initCause", Throwable.class).invoke(this, this.causeOnJDK13OrBelow);
            }
            this.isJDK14OrAbove = true;
        } catch (Exception unused) {
        }
    }
}
