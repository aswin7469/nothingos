package java.p026io;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Formatter;
import sun.nio.p034cs.StreamDecoder;
import sun.nio.p034cs.StreamEncoder;

/* renamed from: java.io.Console */
public final class Console implements Flushable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static Console cons;
    private static boolean echoOff;

    /* renamed from: cs */
    private Charset f513cs;
    private Formatter formatter;
    private Writer out;

    /* renamed from: pw */
    private PrintWriter f514pw;
    /* access modifiers changed from: private */
    public char[] rcb;
    /* access modifiers changed from: private */
    public Object readLock;
    private Reader reader;
    private Object writeLock;

    private static native boolean echo(boolean z) throws IOException;

    private static native String encoding();

    private static native boolean istty();

    public PrintWriter writer() {
        return this.f514pw;
    }

    public Reader reader() {
        return this.reader;
    }

    public Console format(String str, Object... objArr) {
        this.formatter.format(str, objArr).flush();
        return this;
    }

    public Console printf(String str, Object... objArr) {
        return format(str, objArr);
    }

    public String readLine(String str, Object... objArr) {
        String str2;
        synchronized (this.writeLock) {
            synchronized (this.readLock) {
                if (str.length() != 0) {
                    this.f514pw.format(str, objArr);
                }
                try {
                    char[] readline = readline(false);
                    str2 = readline != null ? new String(readline) : null;
                } catch (IOException e) {
                    throw new IOError(e);
                }
            }
        }
        return str2;
    }

    public String readLine() {
        return readLine("", new Object[0]);
    }

    public char[] readPassword(String str, Object... objArr) {
        IOError iOError;
        char[] readline;
        synchronized (this.writeLock) {
            synchronized (this.readLock) {
                try {
                    echoOff = echo(false);
                    iOError = null;
                    if (str.length() != 0) {
                        this.f514pw.format(str, objArr);
                    }
                    readline = readline(true);
                    echoOff = echo(true);
                } catch (IOException e) {
                    throw new IOError(e);
                } catch (IOException e2) {
                    IOError iOError2 = new IOError(e2);
                    try {
                        echoOff = echo(true);
                    } catch (IOException e3) {
                        iOError2.addSuppressed(e3);
                    }
                    throw iOError2;
                } catch (IOException e4) {
                    iOError = new IOError(e4);
                } catch (Throwable th) {
                    throw th;
                }
                if (iOError == null) {
                    this.f514pw.println();
                } else {
                    throw iOError;
                }
            }
        }
        return readline;
    }

    public char[] readPassword() {
        return readPassword("", new Object[0]);
    }

    public void flush() {
        this.f514pw.flush();
    }

    private char[] readline(boolean z) throws IOException {
        Reader reader2 = this.reader;
        char[] cArr = this.rcb;
        int read = reader2.read(cArr, 0, cArr.length);
        if (read < 0) {
            return null;
        }
        char[] cArr2 = this.rcb;
        char c = cArr2[read - 1];
        if (c == 13 || (c == 10 && read - 1 > 0 && cArr2[read - 1] == 13)) {
            read--;
        }
        char[] cArr3 = new char[read];
        if (read > 0) {
            System.arraycopy((Object) cArr2, 0, (Object) cArr3, 0, read);
            if (z) {
                Arrays.fill(this.rcb, 0, read, ' ');
            }
        }
        return cArr3;
    }

    /* access modifiers changed from: private */
    public char[] grow() {
        char[] cArr = this.rcb;
        char[] cArr2 = new char[(cArr.length * 2)];
        System.arraycopy((Object) cArr, 0, (Object) cArr2, 0, cArr.length);
        this.rcb = cArr2;
        return cArr2;
    }

    /* renamed from: java.io.Console$LineReader */
    class LineReader extends Reader {

        /* renamed from: cb */
        private char[] f515cb = new char[1024];

        /* renamed from: in */
        private Reader f516in;
        boolean leftoverLF = false;
        private int nChars = 0;
        private int nextChar = 0;

        public void close() {
        }

        LineReader(Reader reader) {
            this.f516in = reader;
        }

        public boolean ready() throws IOException {
            return this.f516in.ready();
        }

        public int read(char[] cArr, int i, int i2) throws IOException {
            int read;
            char c;
            int i3 = i + i2;
            if (i < 0 || i > cArr.length || i2 < 0 || i3 < 0 || i3 > cArr.length) {
                throw new IndexOutOfBoundsException();
            }
            synchronized (Console.this.readLock) {
                int i4 = i;
                boolean z = false;
                do {
                    if (this.nextChar >= this.nChars) {
                        do {
                            Reader reader = this.f516in;
                            char[] cArr2 = this.f515cb;
                            read = reader.read(cArr2, 0, cArr2.length);
                        } while (read == 0);
                        if (read > 0) {
                            this.nChars = read;
                            this.nextChar = 0;
                            char[] cArr3 = this.f515cb;
                            if (!(read >= cArr3.length || (c = cArr3[read - 1]) == 10 || c == 13)) {
                                z = true;
                            }
                        } else {
                            int i5 = i4 - i;
                            if (i5 == 0) {
                                return -1;
                            }
                            return i5;
                        }
                    }
                    if (this.leftoverLF && cArr == Console.this.rcb) {
                        char[] cArr4 = this.f515cb;
                        int i6 = this.nextChar;
                        if (cArr4[i6] == 10) {
                            this.nextChar = i6 + 1;
                        }
                    }
                    this.leftoverLF = false;
                    while (true) {
                        int i7 = this.nextChar;
                        if (i7 < this.nChars) {
                            int i8 = i4 + 1;
                            char[] cArr5 = this.f515cb;
                            char c2 = cArr5[i7];
                            cArr[i4] = c2;
                            this.nextChar = i7 + 1;
                            cArr5[i7] = 0;
                            if (c2 == 10) {
                                int i9 = i8 - i;
                                return i9;
                            } else if (c2 == 13) {
                                if (i8 == i3) {
                                    if (cArr == Console.this.rcb) {
                                        cArr = Console.this.grow();
                                        int length = cArr.length;
                                    } else {
                                        this.leftoverLF = true;
                                        int i10 = i8 - i;
                                        return i10;
                                    }
                                }
                                if (this.nextChar == this.nChars && this.f516in.ready()) {
                                    Reader reader2 = this.f516in;
                                    char[] cArr6 = this.f515cb;
                                    this.nChars = reader2.read(cArr6, 0, cArr6.length);
                                    this.nextChar = 0;
                                }
                                int i11 = this.nextChar;
                                if (i11 < this.nChars && this.f515cb[i11] == 10) {
                                    cArr[i8] = 10;
                                    this.nextChar = i11 + 1;
                                    i8++;
                                }
                                int i12 = i8 - i;
                                return i12;
                            } else {
                                if (i8 == i3) {
                                    if (cArr == Console.this.rcb) {
                                        cArr = Console.this.grow();
                                        i3 = cArr.length;
                                    } else {
                                        int i13 = i8 - i;
                                        return i13;
                                    }
                                }
                                i4 = i8;
                            }
                        }
                    }
                } while (!z);
                int i14 = i4 - i;
                return i14;
            }
        }
    }

    public static Console console() {
        if (!istty()) {
            return null;
        }
        if (cons == null) {
            cons = new Console();
        }
        return cons;
    }

    private Console() {
        this(new FileInputStream(FileDescriptor.f518in), new FileOutputStream(FileDescriptor.out));
    }

    private Console(InputStream inputStream, OutputStream outputStream) {
        this.readLock = new Object();
        this.writeLock = new Object();
        String encoding = encoding();
        if (encoding != null) {
            try {
                this.f513cs = Charset.forName(encoding);
            } catch (Exception unused) {
            }
        }
        if (this.f513cs == null) {
            this.f513cs = Charset.defaultCharset();
        }
        StreamEncoder forOutputStreamWriter = StreamEncoder.forOutputStreamWriter(outputStream, this.writeLock, this.f513cs);
        this.out = forOutputStreamWriter;
        this.f514pw = new PrintWriter(forOutputStreamWriter, true) {
            public void close() {
            }
        };
        this.formatter = new Formatter((Appendable) this.out);
        this.reader = new LineReader(StreamDecoder.forInputStreamReader(inputStream, this.readLock, this.f513cs));
        this.rcb = new char[1024];
    }
}
