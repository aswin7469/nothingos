package java.util.zip;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import sun.nio.p034cs.ArrayDecoder;
import sun.nio.p034cs.ArrayEncoder;

final class ZipCoder {

    /* renamed from: cs */
    private Charset f805cs;
    private CharsetDecoder dec;
    private CharsetEncoder enc;
    private boolean isUTF8;
    private ZipCoder utf8;

    /* access modifiers changed from: package-private */
    public String toString(byte[] bArr, int i) {
        CharsetDecoder reset = decoder().reset();
        int maxCharsPerByte = (int) (((float) i) * reset.maxCharsPerByte());
        char[] cArr = new char[maxCharsPerByte];
        if (maxCharsPerByte == 0) {
            return new String(cArr);
        }
        if (!this.isUTF8 || !(reset instanceof ArrayDecoder)) {
            ByteBuffer wrap = ByteBuffer.wrap(bArr, 0, i);
            CharBuffer wrap2 = CharBuffer.wrap(cArr);
            CoderResult decode = reset.decode(wrap, wrap2, true);
            if (decode.isUnderflow()) {
                CoderResult flush = reset.flush(wrap2);
                if (flush.isUnderflow()) {
                    return new String(cArr, 0, wrap2.position());
                }
                throw new IllegalArgumentException(flush.toString());
            }
            throw new IllegalArgumentException(decode.toString());
        }
        int decode2 = ((ArrayDecoder) reset).decode(bArr, 0, i, cArr);
        if (decode2 != -1) {
            return new String(cArr, 0, decode2);
        }
        throw new IllegalArgumentException("MALFORMED");
    }

    /* access modifiers changed from: package-private */
    public String toString(byte[] bArr) {
        return toString(bArr, bArr.length);
    }

    /* access modifiers changed from: package-private */
    public byte[] getBytes(String str) {
        CharsetEncoder reset = encoder().reset();
        char[] charArray = str.toCharArray();
        int length = (int) (((float) charArray.length) * reset.maxBytesPerChar());
        byte[] bArr = new byte[length];
        if (length == 0) {
            return bArr;
        }
        if (!this.isUTF8 || !(reset instanceof ArrayEncoder)) {
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            CoderResult encode = reset.encode(CharBuffer.wrap(charArray), wrap, true);
            if (encode.isUnderflow()) {
                CoderResult flush = reset.flush(wrap);
                if (!flush.isUnderflow()) {
                    throw new IllegalArgumentException(flush.toString());
                } else if (wrap.position() == length) {
                    return bArr;
                } else {
                    return Arrays.copyOf(bArr, wrap.position());
                }
            } else {
                throw new IllegalArgumentException(encode.toString());
            }
        } else {
            int encode2 = ((ArrayEncoder) reset).encode(charArray, 0, charArray.length, bArr);
            if (encode2 != -1) {
                return Arrays.copyOf(bArr, encode2);
            }
            throw new IllegalArgumentException("MALFORMED");
        }
    }

    /* access modifiers changed from: package-private */
    public byte[] getBytesUTF8(String str) {
        if (this.isUTF8) {
            return getBytes(str);
        }
        if (this.utf8 == null) {
            this.utf8 = new ZipCoder(StandardCharsets.UTF_8);
        }
        return this.utf8.getBytes(str);
    }

    /* access modifiers changed from: package-private */
    public String toStringUTF8(byte[] bArr, int i) {
        if (this.isUTF8) {
            return toString(bArr, i);
        }
        if (this.utf8 == null) {
            this.utf8 = new ZipCoder(StandardCharsets.UTF_8);
        }
        return this.utf8.toString(bArr, i);
    }

    /* access modifiers changed from: package-private */
    public boolean isUTF8() {
        return this.isUTF8;
    }

    private ZipCoder(Charset charset) {
        this.f805cs = charset;
        this.isUTF8 = charset.name().equals(StandardCharsets.UTF_8.name());
    }

    static ZipCoder get(Charset charset) {
        return new ZipCoder(charset);
    }

    private CharsetDecoder decoder() {
        if (this.dec == null) {
            this.dec = this.f805cs.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        }
        return this.dec;
    }

    private CharsetEncoder encoder() {
        if (this.enc == null) {
            this.enc = this.f805cs.newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        }
        return this.enc;
    }
}
