package com.android.okhttp;

import com.android.okhttp.internal.Util;
import com.android.okhttp.okio.BufferedSink;
import com.android.okhttp.okio.ByteString;
import com.android.okhttp.okio.Okio;
import com.android.okhttp.okio.Source;
import java.nio.charset.Charset;
import java.p026io.Closeable;
import java.p026io.File;
import java.p026io.IOException;

public abstract class RequestBody {
    public long contentLength() throws IOException {
        return -1;
    }

    public abstract MediaType contentType();

    public abstract void writeTo(BufferedSink bufferedSink) throws IOException;

    public static RequestBody create(MediaType mediaType, String str) {
        Charset charset = Util.UTF_8;
        if (mediaType != null && (charset = mediaType.charset()) == null) {
            charset = Util.UTF_8;
            mediaType = MediaType.parse(mediaType + "; charset=utf-8");
        }
        return create(mediaType, str.getBytes(charset));
    }

    public static RequestBody create(final MediaType mediaType, final ByteString byteString) {
        return new RequestBody() {
            public MediaType contentType() {
                return MediaType.this;
            }

            public long contentLength() throws IOException {
                return (long) byteString.size();
            }

            public void writeTo(BufferedSink bufferedSink) throws IOException {
                bufferedSink.write(byteString);
            }
        };
    }

    public static RequestBody create(MediaType mediaType, byte[] bArr) {
        return create(mediaType, bArr, 0, bArr.length);
    }

    public static RequestBody create(final MediaType mediaType, final byte[] bArr, final int i, final int i2) {
        if (bArr != null) {
            Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
            return new RequestBody() {
                public MediaType contentType() {
                    return MediaType.this;
                }

                public long contentLength() {
                    return (long) i2;
                }

                public void writeTo(BufferedSink bufferedSink) throws IOException {
                    bufferedSink.write(bArr, i, i2);
                }
            };
        }
        throw new NullPointerException("content == null");
    }

    public static RequestBody create(final MediaType mediaType, final File file) {
        if (file != null) {
            return new RequestBody() {
                public MediaType contentType() {
                    return MediaType.this;
                }

                public long contentLength() {
                    return file.length();
                }

                public void writeTo(BufferedSink bufferedSink) throws IOException {
                    Source source = null;
                    try {
                        source = Okio.source(file);
                        bufferedSink.writeAll(source);
                    } finally {
                        Util.closeQuietly((Closeable) source);
                    }
                }
            };
        }
        throw new NullPointerException("content == null");
    }
}
