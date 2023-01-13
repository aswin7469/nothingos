package java.p026io;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import sun.nio.p034cs.StreamDecoder;

/* renamed from: java.io.InputStreamReader */
public class InputStreamReader extends Reader {

    /* renamed from: sd */
    private final StreamDecoder f521sd;

    public InputStreamReader(InputStream inputStream) {
        super(inputStream);
        this.f521sd = StreamDecoder.forInputStreamReader(inputStream, (Object) this, Charset.defaultCharset());
    }

    public InputStreamReader(InputStream inputStream, String str) throws UnsupportedEncodingException {
        super(inputStream);
        if (str != null) {
            this.f521sd = StreamDecoder.forInputStreamReader(inputStream, (Object) this, str);
            return;
        }
        throw new NullPointerException("charsetName");
    }

    public InputStreamReader(InputStream inputStream, Charset charset) {
        super(inputStream);
        if (charset != null) {
            this.f521sd = StreamDecoder.forInputStreamReader(inputStream, (Object) this, charset);
            return;
        }
        throw new NullPointerException("charset");
    }

    public InputStreamReader(InputStream inputStream, CharsetDecoder charsetDecoder) {
        super(inputStream);
        if (charsetDecoder != null) {
            this.f521sd = StreamDecoder.forInputStreamReader(inputStream, (Object) this, charsetDecoder);
            return;
        }
        throw new NullPointerException("charset decoder");
    }

    public String getEncoding() {
        return this.f521sd.getEncoding();
    }

    public int read() throws IOException {
        return this.f521sd.read();
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        return this.f521sd.read(cArr, i, i2);
    }

    public boolean ready() throws IOException {
        return this.f521sd.ready();
    }

    public void close() throws IOException {
        this.f521sd.close();
    }
}
