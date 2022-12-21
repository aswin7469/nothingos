package sun.nio.p034cs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.p026io.UnsupportedEncodingException;
import java.p026io.Writer;

/* renamed from: sun.nio.cs.StreamEncoder */
public class StreamEncoder extends Writer {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;

    /* renamed from: bb */
    private ByteBuffer f904bb;

    /* renamed from: ch */
    private WritableByteChannel f905ch;

    /* renamed from: cs */
    private Charset f906cs;
    private CharsetEncoder encoder;
    private boolean haveLeftoverChar;
    private volatile boolean isOpen;
    private CharBuffer lcb;
    private char leftoverChar;
    private final OutputStream out;

    private void ensureOpen() throws IOException {
        if (!this.isOpen) {
            throw new IOException("Stream closed");
        }
    }

    public static StreamEncoder forOutputStreamWriter(OutputStream outputStream, Object obj, String str) throws UnsupportedEncodingException {
        if (str == null) {
            str = Charset.defaultCharset().name();
        }
        try {
            if (Charset.isSupported(str)) {
                return new StreamEncoder(outputStream, obj, Charset.forName(str));
            }
        } catch (IllegalCharsetNameException unused) {
        }
        throw new UnsupportedEncodingException(str);
    }

    public static StreamEncoder forOutputStreamWriter(OutputStream outputStream, Object obj, Charset charset) {
        return new StreamEncoder(outputStream, obj, charset);
    }

    public static StreamEncoder forOutputStreamWriter(OutputStream outputStream, Object obj, CharsetEncoder charsetEncoder) {
        return new StreamEncoder(outputStream, obj, charsetEncoder);
    }

    public static StreamEncoder forEncoder(WritableByteChannel writableByteChannel, CharsetEncoder charsetEncoder, int i) {
        return new StreamEncoder(writableByteChannel, charsetEncoder, i);
    }

    public String getEncoding() {
        if (isOpen()) {
            return encodingName();
        }
        return null;
    }

    public void flushBuffer() throws IOException {
        synchronized (this.lock) {
            if (isOpen()) {
                implFlushBuffer();
            } else {
                throw new IOException("Stream closed");
            }
        }
    }

    public void write(int i) throws IOException {
        write(new char[]{(char) i}, 0, 1);
    }

    public void write(char[] cArr, int i, int i2) throws IOException {
        int i3;
        synchronized (this.lock) {
            ensureOpen();
            if (i < 0 || i > cArr.length || i2 < 0 || (i3 = i + i2) > cArr.length || i3 < 0) {
                throw new IndexOutOfBoundsException();
            } else if (i2 != 0) {
                implWrite(cArr, i, i2);
            }
        }
    }

    public void write(String str, int i, int i2) throws IOException {
        if (i2 >= 0) {
            char[] cArr = new char[i2];
            str.getChars(i, i + i2, cArr, 0);
            write(cArr, 0, i2);
            return;
        }
        throw new IndexOutOfBoundsException();
    }

    public void flush() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            implFlush();
        }
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

    private StreamEncoder(OutputStream outputStream, Object obj, Charset charset) {
        this(outputStream, obj, charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
    }

    private StreamEncoder(OutputStream outputStream, Object obj, CharsetEncoder charsetEncoder) {
        super(obj);
        this.isOpen = true;
        this.haveLeftoverChar = false;
        this.lcb = null;
        this.out = outputStream;
        this.f905ch = null;
        this.f906cs = charsetEncoder.charset();
        this.encoder = charsetEncoder;
        if (this.f905ch == null) {
            this.f904bb = ByteBuffer.allocate(8192);
        }
    }

    private StreamEncoder(WritableByteChannel writableByteChannel, CharsetEncoder charsetEncoder, int i) {
        this.isOpen = true;
        this.haveLeftoverChar = false;
        this.lcb = null;
        this.out = null;
        this.f905ch = writableByteChannel;
        this.f906cs = charsetEncoder.charset();
        this.encoder = charsetEncoder;
        this.f904bb = ByteBuffer.allocate(i < 0 ? 8192 : i);
    }

    private void writeBytes() throws IOException {
        this.f904bb.flip();
        int limit = this.f904bb.limit();
        int position = this.f904bb.position();
        int i = position <= limit ? limit - position : 0;
        if (i > 0) {
            WritableByteChannel writableByteChannel = this.f905ch;
            if (writableByteChannel != null) {
                int write = writableByteChannel.write(this.f904bb);
            } else {
                this.out.write(this.f904bb.array(), this.f904bb.arrayOffset() + position, i);
            }
        }
        this.f904bb.clear();
    }

    private void flushLeftoverChar(CharBuffer charBuffer, boolean z) throws IOException {
        if (this.haveLeftoverChar || z) {
            CharBuffer charBuffer2 = this.lcb;
            if (charBuffer2 == null) {
                this.lcb = CharBuffer.allocate(2);
            } else {
                charBuffer2.clear();
            }
            if (this.haveLeftoverChar) {
                this.lcb.put(this.leftoverChar);
            }
            if (charBuffer != null && charBuffer.hasRemaining()) {
                this.lcb.put(charBuffer.get());
            }
            this.lcb.flip();
            while (true) {
                if (!this.lcb.hasRemaining() && !z) {
                    break;
                }
                CoderResult encode = this.encoder.encode(this.lcb, this.f904bb, z);
                if (encode.isUnderflow()) {
                    if (this.lcb.hasRemaining()) {
                        this.leftoverChar = this.lcb.get();
                        if (charBuffer != null && charBuffer.hasRemaining()) {
                            flushLeftoverChar(charBuffer, z);
                            return;
                        }
                        return;
                    }
                } else if (encode.isOverflow()) {
                    writeBytes();
                } else {
                    encode.throwException();
                }
            }
            this.haveLeftoverChar = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void implWrite(char[] cArr, int i, int i2) throws IOException {
        CharBuffer wrap = CharBuffer.wrap(cArr, i, i2);
        if (this.haveLeftoverChar) {
            flushLeftoverChar(wrap, false);
        }
        while (wrap.hasRemaining()) {
            CoderResult encode = this.encoder.encode(wrap, this.f904bb, false);
            if (encode.isUnderflow()) {
                if (wrap.remaining() == 1) {
                    this.haveLeftoverChar = true;
                    this.leftoverChar = wrap.get();
                    return;
                }
                return;
            } else if (encode.isOverflow()) {
                writeBytes();
            } else {
                encode.throwException();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void implFlushBuffer() throws IOException {
        if (this.f904bb.position() > 0) {
            writeBytes();
        }
    }

    /* access modifiers changed from: package-private */
    public void implFlush() throws IOException {
        implFlushBuffer();
        OutputStream outputStream = this.out;
        if (outputStream != null) {
            outputStream.flush();
        }
    }

    /* access modifiers changed from: package-private */
    public void implClose() throws IOException {
        flushLeftoverChar((CharBuffer) null, true);
        while (true) {
            try {
                CoderResult flush = this.encoder.flush(this.f904bb);
                if (flush.isUnderflow()) {
                    break;
                } else if (flush.isOverflow()) {
                    writeBytes();
                } else {
                    flush.throwException();
                }
            } catch (IOException e) {
                this.encoder.reset();
                throw e;
            }
        }
        if (this.f904bb.position() > 0) {
            writeBytes();
        }
        WritableByteChannel writableByteChannel = this.f905ch;
        if (writableByteChannel != null) {
            writableByteChannel.close();
        } else {
            this.out.close();
        }
    }

    /* access modifiers changed from: package-private */
    public String encodingName() {
        Charset charset = this.f906cs;
        if (charset instanceof HistoricallyNamedCharset) {
            return ((HistoricallyNamedCharset) charset).historicalName();
        }
        return charset.name();
    }
}
