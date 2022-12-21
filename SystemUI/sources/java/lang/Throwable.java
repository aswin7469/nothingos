package java.lang;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.PrintStream;
import java.p026io.PrintWriter;
import java.p026io.Serializable;
import java.p026io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import libcore.util.EmptyArray;

public class Throwable implements Serializable {
    private static final String CAUSE_CAPTION = "Caused by: ";
    private static Throwable[] EMPTY_THROWABLE_ARRAY = null;
    private static final String NULL_CAUSE_MESSAGE = "Cannot suppress a null exception.";
    private static final String SELF_SUPPRESSION_MESSAGE = "Self-suppression not permitted";
    private static final String SUPPRESSED_CAPTION = "Suppressed: ";
    private static final long serialVersionUID = -3042686055658047285L;
    private transient Object backtrace;
    private Throwable cause = this;
    private String detailMessage;
    private StackTraceElement[] stackTrace = EmptyArray.STACK_TRACE_ELEMENT;
    private List<Throwable> suppressedExceptions = Collections.emptyList();

    private static native Object nativeFillInStackTrace();

    private static native StackTraceElement[] nativeGetStackTrace(Object obj);

    private static class SentinelHolder {
        public static final StackTraceElement STACK_TRACE_ELEMENT_SENTINEL;
        public static final StackTraceElement[] STACK_TRACE_SENTINEL;

        private SentinelHolder() {
        }

        static {
            StackTraceElement stackTraceElement = new StackTraceElement("", "", (String) null, Integer.MIN_VALUE);
            STACK_TRACE_ELEMENT_SENTINEL = stackTraceElement;
            STACK_TRACE_SENTINEL = new StackTraceElement[]{stackTraceElement};
        }
    }

    public Throwable() {
        fillInStackTrace();
    }

    public Throwable(String str) {
        fillInStackTrace();
        this.detailMessage = str;
    }

    public Throwable(String str, Throwable th) {
        fillInStackTrace();
        this.detailMessage = str;
        this.cause = th;
    }

    public Throwable(Throwable th) {
        String str;
        fillInStackTrace();
        if (th == null) {
            str = null;
        } else {
            str = th.toString();
        }
        this.detailMessage = str;
        this.cause = th;
    }

    protected Throwable(String str, Throwable th, boolean z, boolean z2) {
        if (z2) {
            fillInStackTrace();
        } else {
            this.stackTrace = null;
        }
        this.detailMessage = str;
        this.cause = th;
        if (!z) {
            this.suppressedExceptions = null;
        }
    }

    public String getMessage() {
        return this.detailMessage;
    }

    public String getLocalizedMessage() {
        return getMessage();
    }

    public synchronized Throwable getCause() {
        Throwable th;
        th = this.cause;
        if (th == this) {
            th = null;
        }
        return th;
    }

    public synchronized Throwable initCause(Throwable th) {
        if (this.cause != this) {
            throw new IllegalStateException("Can't overwrite cause with " + Objects.toString(th, "a null"), this);
        } else if (th != this) {
            this.cause = th;
        } else {
            throw new IllegalArgumentException("Self-causation not permitted", this);
        }
        return this;
    }

    public String toString() {
        String name = getClass().getName();
        String localizedMessage = getLocalizedMessage();
        if (localizedMessage == null) {
            return name;
        }
        return name + ": " + localizedMessage;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream printStream) {
        printStackTrace((PrintStreamOrWriter) new WrappedPrintStream(printStream));
    }

    private void printStackTrace(PrintStreamOrWriter printStreamOrWriter) {
        Set newSetFromMap = Collections.newSetFromMap(new IdentityHashMap());
        newSetFromMap.add(this);
        synchronized (printStreamOrWriter.lock()) {
            printStreamOrWriter.println(this);
            StackTraceElement[] ourStackTrace = getOurStackTrace();
            for (StackTraceElement stackTraceElement : ourStackTrace) {
                printStreamOrWriter.println("\tat " + stackTraceElement);
            }
            for (Throwable printEnclosedStackTrace : getSuppressed()) {
                printEnclosedStackTrace.printEnclosedStackTrace(printStreamOrWriter, ourStackTrace, SUPPRESSED_CAPTION, "\t", newSetFromMap);
            }
            Throwable cause2 = getCause();
            if (cause2 != null) {
                cause2.printEnclosedStackTrace(printStreamOrWriter, ourStackTrace, CAUSE_CAPTION, "", newSetFromMap);
            }
        }
    }

    private void printEnclosedStackTrace(PrintStreamOrWriter printStreamOrWriter, StackTraceElement[] stackTraceElementArr, String str, String str2, Set<Throwable> set) {
        if (set.contains(this)) {
            printStreamOrWriter.println(str2 + str + "[CIRCULAR REFERENCE: " + this + NavigationBarInflaterView.SIZE_MOD_END);
            return;
        }
        set.add(this);
        StackTraceElement[] ourStackTrace = getOurStackTrace();
        int length = ourStackTrace.length - 1;
        int length2 = stackTraceElementArr.length - 1;
        while (length >= 0 && length2 >= 0 && ourStackTrace[length].equals(stackTraceElementArr[length2])) {
            length--;
            length2--;
        }
        int length3 = (ourStackTrace.length - 1) - length;
        printStreamOrWriter.println(str2 + str + this);
        for (int i = 0; i <= length; i++) {
            printStreamOrWriter.println(str2 + "\tat " + ourStackTrace[i]);
        }
        if (length3 != 0) {
            printStreamOrWriter.println(str2 + "\t... " + length3 + " more");
        }
        for (Throwable printEnclosedStackTrace : getSuppressed()) {
            printEnclosedStackTrace.printEnclosedStackTrace(printStreamOrWriter, ourStackTrace, SUPPRESSED_CAPTION, str2 + "\t", set);
        }
        Throwable cause2 = getCause();
        if (cause2 != null) {
            cause2.printEnclosedStackTrace(printStreamOrWriter, ourStackTrace, CAUSE_CAPTION, str2, set);
        }
    }

    public void printStackTrace(PrintWriter printWriter) {
        printStackTrace((PrintStreamOrWriter) new WrappedPrintWriter(printWriter));
    }

    private static abstract class PrintStreamOrWriter {
        /* access modifiers changed from: package-private */
        public abstract Object lock();

        /* access modifiers changed from: package-private */
        public abstract void println(Object obj);

        private PrintStreamOrWriter() {
        }
    }

    private static class WrappedPrintStream extends PrintStreamOrWriter {
        private final PrintStream printStream;

        WrappedPrintStream(PrintStream printStream2) {
            super();
            this.printStream = printStream2;
        }

        /* access modifiers changed from: package-private */
        public Object lock() {
            return this.printStream;
        }

        /* access modifiers changed from: package-private */
        public void println(Object obj) {
            this.printStream.println(obj);
        }
    }

    private static class WrappedPrintWriter extends PrintStreamOrWriter {
        private final PrintWriter printWriter;

        WrappedPrintWriter(PrintWriter printWriter2) {
            super();
            this.printWriter = printWriter2;
        }

        /* access modifiers changed from: package-private */
        public Object lock() {
            return this.printWriter;
        }

        /* access modifiers changed from: package-private */
        public void println(Object obj) {
            this.printWriter.println(obj);
        }
    }

    public synchronized Throwable fillInStackTrace() {
        if (!(this.stackTrace == null && this.backtrace == null)) {
            this.backtrace = nativeFillInStackTrace();
            this.stackTrace = EmptyArray.STACK_TRACE_ELEMENT;
        }
        return this;
    }

    public StackTraceElement[] getStackTrace() {
        return (StackTraceElement[]) getOurStackTrace().clone();
    }

    private synchronized StackTraceElement[] getOurStackTrace() {
        if (this.stackTrace != EmptyArray.STACK_TRACE_ELEMENT) {
            StackTraceElement[] stackTraceElementArr = this.stackTrace;
            if (stackTraceElementArr != null || this.backtrace == null) {
                if (stackTraceElementArr == null) {
                    return EmptyArray.STACK_TRACE_ELEMENT;
                }
                return this.stackTrace;
            }
        }
        StackTraceElement[] nativeGetStackTrace = nativeGetStackTrace(this.backtrace);
        this.stackTrace = nativeGetStackTrace;
        this.backtrace = null;
        if (nativeGetStackTrace == null) {
            return EmptyArray.STACK_TRACE_ELEMENT;
        }
        return this.stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTraceElementArr) {
        StackTraceElement[] stackTraceElementArr2 = (StackTraceElement[]) stackTraceElementArr.clone();
        int i = 0;
        while (i < stackTraceElementArr2.length) {
            if (stackTraceElementArr2[i] != null) {
                i++;
            } else {
                throw new NullPointerException("stackTrace[" + i + NavigationBarInflaterView.SIZE_MOD_END);
            }
        }
        synchronized (this) {
            if (this.stackTrace != null || this.backtrace != null) {
                this.stackTrace = stackTraceElementArr2;
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        List<Throwable> list = this.suppressedExceptions;
        this.suppressedExceptions = Collections.emptyList();
        StackTraceElement[] stackTraceElementArr = this.stackTrace;
        int i = 0;
        this.stackTrace = new StackTraceElement[0];
        if (list != null) {
            int validateSuppressedExceptionsList = validateSuppressedExceptionsList(list);
            if (validateSuppressedExceptionsList > 0) {
                ArrayList arrayList = new ArrayList(Math.min(100, validateSuppressedExceptionsList));
                for (Throwable next : list) {
                    if (next == null) {
                        throw new NullPointerException(NULL_CAUSE_MESSAGE);
                    } else if (next != this) {
                        arrayList.add(next);
                    } else {
                        throw new IllegalArgumentException(SELF_SUPPRESSION_MESSAGE);
                    }
                }
                this.suppressedExceptions = arrayList;
            }
        } else {
            this.suppressedExceptions = null;
        }
        if (stackTraceElementArr != null) {
            StackTraceElement[] stackTraceElementArr2 = (StackTraceElement[]) stackTraceElementArr.clone();
            if (stackTraceElementArr2.length < 1) {
                return;
            }
            if (stackTraceElementArr2.length != 1 || !SentinelHolder.STACK_TRACE_ELEMENT_SENTINEL.equals(stackTraceElementArr2[0])) {
                int length = stackTraceElementArr2.length;
                while (i < length) {
                    if (stackTraceElementArr2[i] != null) {
                        i++;
                    } else {
                        throw new NullPointerException("null StackTraceElement in serial stream.");
                    }
                }
                this.stackTrace = stackTraceElementArr2;
                return;
            }
            this.stackTrace = null;
        }
    }

    private int validateSuppressedExceptionsList(List<Throwable> list) throws IOException {
        int size = list.size();
        if (size >= 0) {
            return size;
        }
        throw new StreamCorruptedException("Negative list size reported.");
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        getOurStackTrace();
        StackTraceElement[] stackTraceElementArr = this.stackTrace;
        if (stackTraceElementArr == null) {
            try {
                this.stackTrace = SentinelHolder.STACK_TRACE_SENTINEL;
            } catch (Throwable th) {
                this.stackTrace = stackTraceElementArr;
                throw th;
            }
        }
        objectOutputStream.defaultWriteObject();
        this.stackTrace = stackTraceElementArr;
    }

    public final synchronized void addSuppressed(Throwable th) {
        if (th == this) {
            throw new IllegalArgumentException(SELF_SUPPRESSION_MESSAGE, th);
        } else if (th != null) {
            List<Throwable> list = this.suppressedExceptions;
            if (list != null) {
                if (list.isEmpty()) {
                    this.suppressedExceptions = new ArrayList(1);
                }
                this.suppressedExceptions.add(th);
            }
        } else {
            throw new NullPointerException(NULL_CAUSE_MESSAGE);
        }
    }

    public final synchronized Throwable[] getSuppressed() {
        if (EMPTY_THROWABLE_ARRAY == null) {
            EMPTY_THROWABLE_ARRAY = new Throwable[0];
        }
        List<Throwable> list = this.suppressedExceptions;
        if (list != null) {
            if (!list.isEmpty()) {
                return (Throwable[]) this.suppressedExceptions.toArray(EMPTY_THROWABLE_ARRAY);
            }
        }
        return EMPTY_THROWABLE_ARRAY;
    }
}
