package java.p026io;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;

/* renamed from: java.io.PrintWriter */
public class PrintWriter extends Writer {
    private final boolean autoFlush;
    private Formatter formatter;
    protected Writer out;
    private PrintStream psOut;
    private boolean trouble;

    private static Charset toCharset(String str) throws UnsupportedEncodingException {
        Objects.requireNonNull(str, "charsetName");
        try {
            return Charset.forName(str);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException unused) {
            throw new UnsupportedEncodingException(str);
        }
    }

    public PrintWriter(Writer writer) {
        this(writer, false);
    }

    public PrintWriter(Writer writer, boolean z) {
        super(writer);
        this.trouble = false;
        this.psOut = null;
        this.out = writer;
        this.autoFlush = z;
    }

    public PrintWriter(OutputStream outputStream) {
        this(outputStream, false);
    }

    public PrintWriter(OutputStream outputStream, boolean z) {
        this(outputStream, z, Charset.defaultCharset());
    }

    public PrintWriter(OutputStream outputStream, boolean z, Charset charset) {
        this((Writer) new BufferedWriter(new OutputStreamWriter(outputStream, charset)), z);
        if (outputStream instanceof PrintStream) {
            this.psOut = (PrintStream) outputStream;
        }
    }

    public PrintWriter(String str) throws FileNotFoundException {
        this((Writer) new BufferedWriter(new OutputStreamWriter(new FileOutputStream(str))), false);
    }

    private PrintWriter(Charset charset, File file) throws FileNotFoundException {
        this((Writer) new BufferedWriter(new OutputStreamWriter((OutputStream) new FileOutputStream(file), charset)), false);
    }

    public PrintWriter(String str, String str2) throws FileNotFoundException, UnsupportedEncodingException {
        this(toCharset(str2), new File(str));
    }

    public PrintWriter(String str, Charset charset) throws IOException {
        this((Charset) Objects.requireNonNull(charset, "charset"), new File(str));
    }

    public PrintWriter(File file) throws FileNotFoundException {
        this((Writer) new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))), false);
    }

    public PrintWriter(File file, String str) throws FileNotFoundException, UnsupportedEncodingException {
        this(toCharset(str), file);
    }

    public PrintWriter(File file, Charset charset) throws IOException {
        this((Charset) Objects.requireNonNull(charset, "charset"), file);
    }

    private void ensureOpen() throws IOException {
        if (this.out == null) {
            throw new IOException("Stream closed");
        }
    }

    public void flush() {
        try {
            synchronized (this.lock) {
                ensureOpen();
                this.out.flush();
            }
        } catch (IOException unused) {
            this.trouble = true;
        }
    }

    public void close() {
        try {
            synchronized (this.lock) {
                Writer writer = this.out;
                if (writer != null) {
                    writer.close();
                    this.out = null;
                }
            }
        } catch (IOException unused) {
            this.trouble = true;
        }
    }

    public boolean checkError() {
        if (this.out != null) {
            flush();
        }
        Writer writer = this.out;
        if (writer instanceof PrintWriter) {
            return ((PrintWriter) writer).checkError();
        }
        PrintStream printStream = this.psOut;
        if (printStream != null) {
            return printStream.checkError();
        }
        return this.trouble;
    }

    /* access modifiers changed from: protected */
    public void setError() {
        this.trouble = true;
    }

    /* access modifiers changed from: protected */
    public void clearError() {
        this.trouble = false;
    }

    public void write(int i) {
        try {
            synchronized (this.lock) {
                ensureOpen();
                this.out.write(i);
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
    }

    public void write(char[] cArr, int i, int i2) {
        try {
            synchronized (this.lock) {
                ensureOpen();
                this.out.write(cArr, i, i2);
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
    }

    public void write(char[] cArr) {
        write(cArr, 0, cArr.length);
    }

    public void write(String str, int i, int i2) {
        try {
            synchronized (this.lock) {
                ensureOpen();
                this.out.write(str, i, i2);
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
    }

    public void write(String str) {
        write(str, 0, str.length());
    }

    private void newLine() {
        try {
            synchronized (this.lock) {
                ensureOpen();
                this.out.write(System.lineSeparator());
                if (this.autoFlush) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
    }

    public void print(boolean z) {
        write(String.valueOf(z));
    }

    public void print(char c) {
        write((int) c);
    }

    public void print(int i) {
        write(String.valueOf(i));
    }

    public void print(long j) {
        write(String.valueOf(j));
    }

    public void print(float f) {
        write(String.valueOf(f));
    }

    public void print(double d) {
        write(String.valueOf(d));
    }

    public void print(char[] cArr) {
        write(cArr);
    }

    public void print(String str) {
        write(String.valueOf((Object) str));
    }

    public void print(Object obj) {
        write(String.valueOf(obj));
    }

    public void println() {
        newLine();
    }

    public void println(boolean z) {
        synchronized (this.lock) {
            print(z);
            println();
        }
    }

    public void println(char c) {
        synchronized (this.lock) {
            print(c);
            println();
        }
    }

    public void println(int i) {
        synchronized (this.lock) {
            print(i);
            println();
        }
    }

    public void println(long j) {
        synchronized (this.lock) {
            print(j);
            println();
        }
    }

    public void println(float f) {
        synchronized (this.lock) {
            print(f);
            println();
        }
    }

    public void println(double d) {
        synchronized (this.lock) {
            print(d);
            println();
        }
    }

    public void println(char[] cArr) {
        synchronized (this.lock) {
            print(cArr);
            println();
        }
    }

    public void println(String str) {
        synchronized (this.lock) {
            print(str);
            println();
        }
    }

    public void println(Object obj) {
        String valueOf = String.valueOf(obj);
        synchronized (this.lock) {
            print(valueOf);
            println();
        }
    }

    public PrintWriter printf(String str, Object... objArr) {
        return format(str, objArr);
    }

    public PrintWriter printf(Locale locale, String str, Object... objArr) {
        return format(locale, str, objArr);
    }

    public PrintWriter format(String str, Object... objArr) {
        try {
            synchronized (this.lock) {
                ensureOpen();
                Formatter formatter2 = this.formatter;
                if (formatter2 == null || formatter2.locale() != Locale.getDefault()) {
                    this.formatter = new Formatter((Appendable) this);
                }
                this.formatter.format(Locale.getDefault(), str, objArr);
                if (this.autoFlush) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
        return this;
    }

    public PrintWriter format(Locale locale, String str, Object... objArr) {
        try {
            synchronized (this.lock) {
                ensureOpen();
                Formatter formatter2 = this.formatter;
                if (formatter2 == null || formatter2.locale() != locale) {
                    this.formatter = new Formatter((Appendable) this, locale);
                }
                this.formatter.format(locale, str, objArr);
                if (this.autoFlush) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
        return this;
    }

    public PrintWriter append(CharSequence charSequence) {
        write(String.valueOf((Object) charSequence));
        return this;
    }

    public PrintWriter append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            charSequence = "null";
        }
        return append(charSequence.subSequence(i, i2));
    }

    public PrintWriter append(char c) {
        write((int) c);
        return this;
    }
}
