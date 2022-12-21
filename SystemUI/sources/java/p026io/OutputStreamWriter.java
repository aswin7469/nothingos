package java.p026io;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import sun.nio.p034cs.StreamEncoder;

/* renamed from: java.io.OutputStreamWriter */
public class OutputStreamWriter extends Writer {

    /* renamed from: se */
    private final StreamEncoder f527se;

    public OutputStreamWriter(OutputStream outputStream, String str) throws UnsupportedEncodingException {
        super(outputStream);
        if (str != null) {
            this.f527se = StreamEncoder.forOutputStreamWriter(outputStream, (Object) this, str);
            return;
        }
        throw new NullPointerException("charsetName");
    }

    public OutputStreamWriter(OutputStream outputStream) {
        super(outputStream);
        try {
            String str = null;
            this.f527se = StreamEncoder.forOutputStreamWriter(outputStream, (Object) this, (String) null);
        } catch (UnsupportedEncodingException e) {
            throw new Error((Throwable) e);
        }
    }

    public OutputStreamWriter(OutputStream outputStream, Charset charset) {
        super(outputStream);
        if (charset != null) {
            this.f527se = StreamEncoder.forOutputStreamWriter(outputStream, (Object) this, charset);
            return;
        }
        throw new NullPointerException("charset");
    }

    public OutputStreamWriter(OutputStream outputStream, CharsetEncoder charsetEncoder) {
        super(outputStream);
        if (charsetEncoder != null) {
            this.f527se = StreamEncoder.forOutputStreamWriter(outputStream, (Object) this, charsetEncoder);
            return;
        }
        throw new NullPointerException("charset encoder");
    }

    public String getEncoding() {
        return this.f527se.getEncoding();
    }

    /* access modifiers changed from: package-private */
    public void flushBuffer() throws IOException {
        this.f527se.flushBuffer();
    }

    public void write(int i) throws IOException {
        this.f527se.write(i);
    }

    public void write(char[] cArr, int i, int i2) throws IOException {
        this.f527se.write(cArr, i, i2);
    }

    public void write(String str, int i, int i2) throws IOException {
        this.f527se.write(str, i, i2);
    }

    public void flush() throws IOException {
        this.f527se.flush();
    }

    public void close() throws IOException {
        this.f527se.close();
    }
}
