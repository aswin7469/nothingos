package sun.nio.p034cs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.p026io.FileInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.Reader;
import java.p026io.UnsupportedEncodingException;

/* renamed from: sun.nio.cs.StreamDecoder */
public class StreamDecoder extends Reader {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
    private static final int MIN_BYTE_BUFFER_SIZE = 32;
    private static volatile boolean channelsAvailable = true;

    /* renamed from: bb */
    private ByteBuffer f900bb;

    /* renamed from: ch */
    private ReadableByteChannel f901ch;

    /* renamed from: cs */
    private Charset f902cs;
    private CharsetDecoder decoder;
    private boolean haveLeftoverChar;

    /* renamed from: in */
    private InputStream f903in;
    private volatile boolean isOpen;
    private char leftoverChar;
    private boolean needsFlush;

    private void ensureOpen() throws IOException {
        if (!this.isOpen) {
            throw new IOException("Stream closed");
        }
    }

    public static StreamDecoder forInputStreamReader(InputStream inputStream, Object obj, String str) throws UnsupportedEncodingException {
        if (str == null) {
            str = Charset.defaultCharset().name();
        }
        try {
            if (Charset.isSupported(str)) {
                return new StreamDecoder(inputStream, obj, Charset.forName(str));
            }
        } catch (IllegalCharsetNameException unused) {
        }
        throw new UnsupportedEncodingException(str);
    }

    public static StreamDecoder forInputStreamReader(InputStream inputStream, Object obj, Charset charset) {
        return new StreamDecoder(inputStream, obj, charset);
    }

    public static StreamDecoder forInputStreamReader(InputStream inputStream, Object obj, CharsetDecoder charsetDecoder) {
        return new StreamDecoder(inputStream, obj, charsetDecoder);
    }

    public static StreamDecoder forDecoder(ReadableByteChannel readableByteChannel, CharsetDecoder charsetDecoder, int i) {
        return new StreamDecoder(readableByteChannel, charsetDecoder, i);
    }

    public String getEncoding() {
        if (isOpen()) {
            return encodingName();
        }
        return null;
    }

    public int read() throws IOException {
        return read0();
    }

    private int read0() throws IOException {
        synchronized (this.lock) {
            if (this.haveLeftoverChar) {
                this.haveLeftoverChar = false;
                char c = this.leftoverChar;
                return c;
            }
            char[] cArr = new char[2];
            int read = read(cArr, 0, 2);
            if (read == -1) {
                return -1;
            }
            if (read != 1) {
                if (read != 2) {
                    return -1;
                }
                this.leftoverChar = cArr[1];
                this.haveLeftoverChar = true;
            }
            char c2 = cArr[0];
            return c2;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0034, code lost:
        return 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0042, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int read(char[] r5, int r6, int r7) throws java.p026io.IOException {
        /*
            r4 = this;
            java.lang.Object r0 = r4.lock
            monitor-enter(r0)
            r4.ensureOpen()     // Catch:{ all -> 0x0057 }
            if (r6 < 0) goto L_0x0051
            int r1 = r5.length     // Catch:{ all -> 0x0057 }
            if (r6 > r1) goto L_0x0051
            if (r7 < 0) goto L_0x0051
            int r1 = r6 + r7
            int r2 = r5.length     // Catch:{ all -> 0x0057 }
            if (r1 > r2) goto L_0x0051
            if (r1 < 0) goto L_0x0051
            r1 = 0
            if (r7 != 0) goto L_0x0019
            monitor-exit(r0)     // Catch:{ all -> 0x0057 }
            return r1
        L_0x0019:
            boolean r2 = r4.haveLeftoverChar     // Catch:{ all -> 0x0057 }
            r3 = 1
            if (r2 == 0) goto L_0x0035
            char r2 = r4.leftoverChar     // Catch:{ all -> 0x0057 }
            r5[r6] = r2     // Catch:{ all -> 0x0057 }
            int r6 = r6 + 1
            int r7 = r7 + -1
            r4.haveLeftoverChar = r1     // Catch:{ all -> 0x0057 }
            if (r7 == 0) goto L_0x0033
            boolean r1 = r4.implReady()     // Catch:{ all -> 0x0057 }
            if (r1 != 0) goto L_0x0031
            goto L_0x0033
        L_0x0031:
            r1 = r3
            goto L_0x0035
        L_0x0033:
            monitor-exit(r0)     // Catch:{ all -> 0x0057 }
            return r3
        L_0x0035:
            if (r7 != r3) goto L_0x0049
            int r4 = r4.read0()     // Catch:{ all -> 0x0057 }
            r7 = -1
            if (r4 != r7) goto L_0x0043
            if (r1 != 0) goto L_0x0041
            r1 = r7
        L_0x0041:
            monitor-exit(r0)     // Catch:{ all -> 0x0057 }
            return r1
        L_0x0043:
            char r4 = (char) r4     // Catch:{ all -> 0x0057 }
            r5[r6] = r4     // Catch:{ all -> 0x0057 }
            int r1 = r1 + r3
            monitor-exit(r0)     // Catch:{ all -> 0x0057 }
            return r1
        L_0x0049:
            int r7 = r7 + r6
            int r4 = r4.implRead(r5, r6, r7)     // Catch:{ all -> 0x0057 }
            int r1 = r1 + r4
            monitor-exit(r0)     // Catch:{ all -> 0x0057 }
            return r1
        L_0x0051:
            java.lang.IndexOutOfBoundsException r4 = new java.lang.IndexOutOfBoundsException     // Catch:{ all -> 0x0057 }
            r4.<init>()     // Catch:{ all -> 0x0057 }
            throw r4     // Catch:{ all -> 0x0057 }
        L_0x0057:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0057 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p034cs.StreamDecoder.read(char[], int, int):int");
    }

    public boolean ready() throws IOException {
        boolean z;
        synchronized (this.lock) {
            ensureOpen();
            if (!this.haveLeftoverChar) {
                if (!implReady()) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public void close() throws IOException {
        synchronized (this.lock) {
            if (this.isOpen) {
                implClose();
                this.isOpen = false;
            }
        }
    }

    private boolean isOpen() {
        return this.isOpen;
    }

    private static FileChannel getChannel(FileInputStream fileInputStream) {
        if (!channelsAvailable) {
            return null;
        }
        try {
            return fileInputStream.getChannel();
        } catch (UnsatisfiedLinkError unused) {
            channelsAvailable = false;
            return null;
        }
    }

    StreamDecoder(InputStream inputStream, Object obj, Charset charset) {
        this(inputStream, obj, charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
    }

    StreamDecoder(InputStream inputStream, Object obj, CharsetDecoder charsetDecoder) {
        super(obj);
        this.isOpen = true;
        this.haveLeftoverChar = false;
        this.needsFlush = false;
        this.f902cs = charsetDecoder.charset();
        this.decoder = charsetDecoder;
        if (this.f901ch == null) {
            this.f903in = inputStream;
            this.f901ch = null;
            this.f900bb = ByteBuffer.allocate(8192);
        }
        this.f900bb.flip();
    }

    StreamDecoder(ReadableByteChannel readableByteChannel, CharsetDecoder charsetDecoder, int i) {
        this.isOpen = true;
        this.haveLeftoverChar = false;
        this.needsFlush = false;
        this.f903in = null;
        this.f901ch = readableByteChannel;
        this.decoder = charsetDecoder;
        this.f902cs = charsetDecoder.charset();
        if (i < 0) {
            i = 8192;
        } else if (i < 32) {
            i = 32;
        }
        ByteBuffer allocate = ByteBuffer.allocate(i);
        this.f900bb = allocate;
        allocate.flip();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0010, code lost:
        if (r0 < 0) goto L_0x0012;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int readBytes() throws java.p026io.IOException {
        /*
            r5 = this;
            java.nio.ByteBuffer r0 = r5.f900bb
            r0.compact()
            java.nio.channels.ReadableByteChannel r0 = r5.f901ch     // Catch:{ all -> 0x005b }
            if (r0 == 0) goto L_0x0018
            java.nio.ByteBuffer r1 = r5.f900bb     // Catch:{ all -> 0x005b }
            r2 = 1
            int r0 = sun.nio.p033ch.ChannelInputStream.read((java.nio.channels.ReadableByteChannel) r0, (java.nio.ByteBuffer) r1, (boolean) r2)     // Catch:{ all -> 0x005b }
            if (r0 >= 0) goto L_0x0047
        L_0x0012:
            java.nio.ByteBuffer r5 = r5.f900bb
            r5.flip()
            return r0
        L_0x0018:
            java.nio.ByteBuffer r0 = r5.f900bb     // Catch:{ all -> 0x005b }
            int r0 = r0.limit()     // Catch:{ all -> 0x005b }
            java.nio.ByteBuffer r1 = r5.f900bb     // Catch:{ all -> 0x005b }
            int r1 = r1.position()     // Catch:{ all -> 0x005b }
            if (r1 > r0) goto L_0x0028
            int r0 = r0 - r1
            goto L_0x0029
        L_0x0028:
            r0 = 0
        L_0x0029:
            java.io.InputStream r2 = r5.f903in     // Catch:{ all -> 0x005b }
            java.nio.ByteBuffer r3 = r5.f900bb     // Catch:{ all -> 0x005b }
            byte[] r3 = r3.array()     // Catch:{ all -> 0x005b }
            java.nio.ByteBuffer r4 = r5.f900bb     // Catch:{ all -> 0x005b }
            int r4 = r4.arrayOffset()     // Catch:{ all -> 0x005b }
            int r4 = r4 + r1
            int r0 = r2.read(r3, r4, r0)     // Catch:{ all -> 0x005b }
            if (r0 >= 0) goto L_0x003f
            goto L_0x0012
        L_0x003f:
            if (r0 == 0) goto L_0x0053
            java.nio.ByteBuffer r2 = r5.f900bb     // Catch:{ all -> 0x005b }
            int r1 = r1 + r0
            r2.position((int) r1)     // Catch:{ all -> 0x005b }
        L_0x0047:
            java.nio.ByteBuffer r0 = r5.f900bb
            r0.flip()
            java.nio.ByteBuffer r5 = r5.f900bb
            int r5 = r5.remaining()
            return r5
        L_0x0053:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x005b }
            java.lang.String r1 = "Underlying input stream returned zero bytes"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x005b }
            throw r0     // Catch:{ all -> 0x005b }
        L_0x005b:
            r0 = move-exception
            java.nio.ByteBuffer r5 = r5.f900bb
            r5.flip()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p034cs.StreamDecoder.readBytes():int");
    }

    /* access modifiers changed from: package-private */
    public int implRead(char[] cArr, int i, int i2) throws IOException {
        CharBuffer wrap = CharBuffer.wrap(cArr, i, i2 - i);
        if (wrap.position() != 0) {
            wrap = wrap.slice();
        }
        if (this.needsFlush) {
            CoderResult flush = this.decoder.flush(wrap);
            if (flush.isOverflow()) {
                return wrap.position();
            }
            if (!flush.isUnderflow()) {
                flush.throwException();
            } else if (wrap.position() == 0) {
                return -1;
            } else {
                return wrap.position();
            }
        }
        boolean z = false;
        while (true) {
            CoderResult decode = this.decoder.decode(this.f900bb, wrap, z);
            if (decode.isUnderflow()) {
                if (z || !wrap.hasRemaining() || (wrap.position() > 0 && !inReady())) {
                    break;
                } else if (readBytes() < 0) {
                    z = true;
                }
            } else if (decode.isOverflow()) {
                break;
            } else {
                decode.throwException();
            }
        }
        if (z) {
            CoderResult flush2 = this.decoder.flush(wrap);
            if (flush2.isOverflow()) {
                this.needsFlush = true;
                return wrap.position();
            }
            this.decoder.reset();
            if (!flush2.isUnderflow()) {
                flush2.throwException();
            }
        }
        if (wrap.position() != 0 || !z) {
            return wrap.position();
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public String encodingName() {
        Charset charset = this.f902cs;
        if (charset instanceof HistoricallyNamedCharset) {
            return ((HistoricallyNamedCharset) charset).historicalName();
        }
        return charset.name();
    }

    private boolean inReady() {
        try {
            InputStream inputStream = this.f903in;
            return (inputStream != null && inputStream.available() > 0) || (this.f901ch instanceof FileChannel);
        } catch (IOException unused) {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean implReady() {
        return this.f900bb.hasRemaining() || inReady();
    }

    /* access modifiers changed from: package-private */
    public void implClose() throws IOException {
        ReadableByteChannel readableByteChannel = this.f901ch;
        if (readableByteChannel != null) {
            readableByteChannel.close();
        } else {
            this.f903in.close();
        }
    }
}
