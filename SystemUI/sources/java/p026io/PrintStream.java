package java.p026io;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Formatter;
import java.util.Locale;

/* renamed from: java.io.PrintStream */
public class PrintStream extends FilterOutputStream implements Appendable, Closeable {
    private final boolean autoFlush;
    private OutputStreamWriter charOut;
    private Charset charset;
    private boolean closing;
    private Formatter formatter;
    private BufferedWriter textOut;
    private boolean trouble;

    private static <T> T requireNonNull(T t, String str) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(str);
    }

    private static Charset toCharset(String str) throws UnsupportedEncodingException {
        requireNonNull(str, "charsetName");
        try {
            return Charset.forName(str);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException unused) {
            throw new UnsupportedEncodingException(str);
        }
    }

    private PrintStream(boolean z, OutputStream outputStream) {
        super(outputStream);
        this.trouble = false;
        this.closing = false;
        this.autoFlush = z;
    }

    private PrintStream(boolean z, Charset charset2, OutputStream outputStream) {
        this(outputStream, z, charset2);
    }

    public PrintStream(OutputStream outputStream) {
        this(outputStream, false);
    }

    public PrintStream(OutputStream outputStream, boolean z) {
        this(z, (OutputStream) requireNonNull(outputStream, "Null output stream"));
    }

    public PrintStream(OutputStream outputStream, boolean z, String str) throws UnsupportedEncodingException {
        this((OutputStream) requireNonNull(outputStream, "Null output stream"), z, toCharset(str));
    }

    public PrintStream(OutputStream outputStream, boolean z, Charset charset2) {
        super(outputStream);
        this.trouble = false;
        this.closing = false;
        this.autoFlush = z;
        this.charOut = new OutputStreamWriter((OutputStream) this, charset2);
        this.textOut = new BufferedWriter(this.charOut);
    }

    public PrintStream(String str) throws FileNotFoundException {
        this(false, (OutputStream) new FileOutputStream(str));
    }

    public PrintStream(String str, String str2) throws FileNotFoundException, UnsupportedEncodingException {
        this(false, toCharset(str2), (OutputStream) new FileOutputStream(str));
    }

    public PrintStream(String str, Charset charset2) throws IOException {
        this(false, (Charset) requireNonNull(charset2, "charset"), (OutputStream) new FileOutputStream(str));
    }

    public PrintStream(File file) throws FileNotFoundException {
        this(false, (OutputStream) new FileOutputStream(file));
    }

    public PrintStream(File file, String str) throws FileNotFoundException, UnsupportedEncodingException {
        this(false, toCharset(str), (OutputStream) new FileOutputStream(file));
    }

    public PrintStream(File file, Charset charset2) throws IOException {
        this(false, (Charset) requireNonNull(charset2, "charset"), (OutputStream) new FileOutputStream(file));
    }

    private void ensureOpen() throws IOException {
        if (this.out == null) {
            throw new IOException("Stream closed");
        }
    }

    public void flush() {
        synchronized (this) {
            try {
                ensureOpen();
                this.out.flush();
            } catch (IOException unused) {
                this.trouble = true;
            }
        }
    }

    private BufferedWriter getTextOut() {
        OutputStreamWriter outputStreamWriter;
        if (this.textOut == null) {
            Charset charset2 = this.charset;
            if (charset2 != null) {
                outputStreamWriter = new OutputStreamWriter((OutputStream) this, charset2);
            } else {
                outputStreamWriter = new OutputStreamWriter(this);
            }
            this.charOut = outputStreamWriter;
            this.textOut = new BufferedWriter(this.charOut);
        }
        return this.textOut;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r2.trouble = true;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0015 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() {
        /*
            r2 = this;
            monitor-enter(r2)
            boolean r0 = r2.closing     // Catch:{ all -> 0x0020 }
            if (r0 != 0) goto L_0x001e
            r0 = 1
            r2.closing = r0     // Catch:{ all -> 0x0020 }
            java.io.BufferedWriter r1 = r2.textOut     // Catch:{ IOException -> 0x0015 }
            if (r1 == 0) goto L_0x000f
            r1.close()     // Catch:{ IOException -> 0x0015 }
        L_0x000f:
            java.io.OutputStream r1 = r2.out     // Catch:{ IOException -> 0x0015 }
            r1.close()     // Catch:{ IOException -> 0x0015 }
            goto L_0x0017
        L_0x0015:
            r2.trouble = r0     // Catch:{ all -> 0x0020 }
        L_0x0017:
            r0 = 0
            r2.textOut = r0     // Catch:{ all -> 0x0020 }
            r2.charOut = r0     // Catch:{ all -> 0x0020 }
            r2.out = r0     // Catch:{ all -> 0x0020 }
        L_0x001e:
            monitor-exit(r2)     // Catch:{ all -> 0x0020 }
            return
        L_0x0020:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0020 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.PrintStream.close():void");
    }

    public boolean checkError() {
        if (this.out != null) {
            flush();
        }
        if (this.out instanceof PrintStream) {
            return ((PrintStream) this.out).checkError();
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
            synchronized (this) {
                ensureOpen();
                this.out.write(i);
                if (i == 10 && this.autoFlush) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
    }

    public void write(byte[] bArr, int i, int i2) {
        try {
            synchronized (this) {
                ensureOpen();
                this.out.write(bArr, i, i2);
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

    private void write(char[] cArr) {
        try {
            synchronized (this) {
                ensureOpen();
                BufferedWriter textOut2 = getTextOut();
                textOut2.write(cArr);
                textOut2.flushBuffer();
                this.charOut.flushBuffer();
                if (this.autoFlush) {
                    for (char c : cArr) {
                        if (c == 10) {
                            this.out.flush();
                        }
                    }
                }
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
    }

    private void write(String str) {
        try {
            synchronized (this) {
                ensureOpen();
                BufferedWriter textOut2 = getTextOut();
                textOut2.write(str);
                textOut2.flushBuffer();
                this.charOut.flushBuffer();
                if (this.autoFlush && str.indexOf(10) >= 0) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
    }

    private void newLine() {
        try {
            synchronized (this) {
                ensureOpen();
                BufferedWriter textOut2 = getTextOut();
                textOut2.newLine();
                textOut2.flushBuffer();
                this.charOut.flushBuffer();
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
        write(String.valueOf(c));
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
        synchronized (this) {
            print(z);
            newLine();
        }
    }

    public void println(char c) {
        synchronized (this) {
            print(c);
            newLine();
        }
    }

    public void println(int i) {
        synchronized (this) {
            print(i);
            newLine();
        }
    }

    public void println(long j) {
        synchronized (this) {
            print(j);
            newLine();
        }
    }

    public void println(float f) {
        synchronized (this) {
            print(f);
            newLine();
        }
    }

    public void println(double d) {
        synchronized (this) {
            print(d);
            newLine();
        }
    }

    public void println(char[] cArr) {
        synchronized (this) {
            print(cArr);
            newLine();
        }
    }

    public void println(String str) {
        synchronized (this) {
            print(str);
            newLine();
        }
    }

    public void println(Object obj) {
        String valueOf = String.valueOf(obj);
        synchronized (this) {
            print(valueOf);
            newLine();
        }
    }

    public PrintStream printf(String str, Object... objArr) {
        return format(str, objArr);
    }

    public PrintStream printf(Locale locale, String str, Object... objArr) {
        return format(locale, str, objArr);
    }

    public PrintStream format(String str, Object... objArr) {
        try {
            synchronized (this) {
                ensureOpen();
                Formatter formatter2 = this.formatter;
                if (formatter2 == null || formatter2.locale() != Locale.getDefault(Locale.Category.FORMAT)) {
                    this.formatter = new Formatter((Appendable) this);
                }
                this.formatter.format(Locale.getDefault(Locale.Category.FORMAT), str, objArr);
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
        return this;
    }

    public PrintStream format(Locale locale, String str, Object... objArr) {
        try {
            synchronized (this) {
                ensureOpen();
                Formatter formatter2 = this.formatter;
                if (formatter2 == null || formatter2.locale() != locale) {
                    this.formatter = new Formatter((Appendable) this, locale);
                }
                this.formatter.format(locale, str, objArr);
            }
        } catch (InterruptedIOException unused) {
            Thread.currentThread().interrupt();
        } catch (IOException unused2) {
            this.trouble = true;
        }
        return this;
    }

    public PrintStream append(CharSequence charSequence) {
        print(String.valueOf((Object) charSequence));
        return this;
    }

    public PrintStream append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            charSequence = "null";
        }
        return append(charSequence.subSequence(i, i2));
    }

    public PrintStream append(char c) {
        print(c);
        return this;
    }
}
