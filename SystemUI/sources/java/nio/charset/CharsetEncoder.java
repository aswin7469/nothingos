package java.nio.charset;

import java.lang.ref.WeakReference;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

public abstract class CharsetEncoder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ST_CODING = 1;
    private static final int ST_END = 2;
    private static final int ST_FLUSHED = 3;
    private static final int ST_RESET = 0;
    private static String[] stateNames = {"RESET", "CODING", "CODING_END", "FLUSHED"};
    private final float averageBytesPerChar;
    private WeakReference<CharsetDecoder> cachedDecoder;
    private final Charset charset;
    private CodingErrorAction malformedInputAction;
    private final float maxBytesPerChar;
    private byte[] replacement;
    private int state;
    private CodingErrorAction unmappableCharacterAction;

    /* access modifiers changed from: protected */
    public abstract CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer);

    /* access modifiers changed from: protected */
    public void implOnMalformedInput(CodingErrorAction codingErrorAction) {
    }

    /* access modifiers changed from: protected */
    public void implOnUnmappableCharacter(CodingErrorAction codingErrorAction) {
    }

    /* access modifiers changed from: protected */
    public void implReplaceWith(byte[] bArr) {
    }

    /* access modifiers changed from: protected */
    public void implReset() {
    }

    protected CharsetEncoder(Charset charset2, float f, float f2, byte[] bArr) {
        this(charset2, f, f2, bArr, false);
    }

    protected CharsetEncoder(Charset charset2, float f, float f2, byte[] bArr, boolean z) {
        this.malformedInputAction = CodingErrorAction.REPORT;
        this.unmappableCharacterAction = CodingErrorAction.REPORT;
        this.state = 0;
        this.cachedDecoder = null;
        this.charset = charset2;
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Non-positive averageBytesPerChar");
        } else if (f2 <= 0.0f) {
            throw new IllegalArgumentException("Non-positive maxBytesPerChar");
        } else if (Charset.atBugLevel("1.4") || f <= f2) {
            this.replacement = bArr;
            this.averageBytesPerChar = f;
            this.maxBytesPerChar = f2;
            if (!z) {
                replaceWith(bArr);
            }
        } else {
            throw new IllegalArgumentException("averageBytesPerChar exceeds maxBytesPerChar");
        }
    }

    protected CharsetEncoder(Charset charset2, float f, float f2) {
        this(charset2, f, f2, new byte[]{63});
    }

    public final Charset charset() {
        return this.charset;
    }

    public final byte[] replacement() {
        byte[] bArr = this.replacement;
        return Arrays.copyOf(bArr, bArr.length);
    }

    public final CharsetEncoder replaceWith(byte[] bArr) {
        if (bArr != null) {
            int length = bArr.length;
            if (length == 0) {
                throw new IllegalArgumentException("Empty replacement");
            } else if (((float) length) > this.maxBytesPerChar) {
                throw new IllegalArgumentException("Replacement too long");
            } else if (isLegalReplacement(bArr)) {
                byte[] copyOf = Arrays.copyOf(bArr, bArr.length);
                this.replacement = copyOf;
                implReplaceWith(copyOf);
                return this;
            } else {
                throw new IllegalArgumentException("Illegal replacement");
            }
        } else {
            throw new IllegalArgumentException("Null replacement");
        }
    }

    public boolean isLegalReplacement(byte[] bArr) {
        CharsetDecoder charsetDecoder;
        WeakReference<CharsetDecoder> weakReference = this.cachedDecoder;
        if (weakReference == null || (charsetDecoder = weakReference.get()) == null) {
            charsetDecoder = charset().newDecoder();
            charsetDecoder.onMalformedInput(CodingErrorAction.REPORT);
            charsetDecoder.onUnmappableCharacter(CodingErrorAction.REPORT);
            this.cachedDecoder = new WeakReference<>(charsetDecoder);
        } else {
            charsetDecoder.reset();
        }
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        return !charsetDecoder.decode(wrap, CharBuffer.allocate((int) (((float) wrap.remaining()) * charsetDecoder.maxCharsPerByte())), true).isError();
    }

    public CodingErrorAction malformedInputAction() {
        return this.malformedInputAction;
    }

    public final CharsetEncoder onMalformedInput(CodingErrorAction codingErrorAction) {
        if (codingErrorAction != null) {
            this.malformedInputAction = codingErrorAction;
            implOnMalformedInput(codingErrorAction);
            return this;
        }
        throw new IllegalArgumentException("Null action");
    }

    public CodingErrorAction unmappableCharacterAction() {
        return this.unmappableCharacterAction;
    }

    public final CharsetEncoder onUnmappableCharacter(CodingErrorAction codingErrorAction) {
        if (codingErrorAction != null) {
            this.unmappableCharacterAction = codingErrorAction;
            implOnUnmappableCharacter(codingErrorAction);
            return this;
        }
        throw new IllegalArgumentException("Null action");
    }

    public final float averageBytesPerChar() {
        return this.averageBytesPerChar;
    }

    public final float maxBytesPerChar() {
        return this.maxBytesPerChar;
    }

    public final CoderResult encode(CharBuffer charBuffer, ByteBuffer byteBuffer, boolean z) {
        CoderResult encodeLoop;
        CodingErrorAction codingErrorAction;
        int i = z ? 2 : 1;
        int i2 = this.state;
        if (!(i2 == 0 || i2 == 1 || (z && i2 == 2))) {
            throwIllegalStateException(i2, i);
        }
        this.state = i;
        while (true) {
            try {
                encodeLoop = encodeLoop(charBuffer, byteBuffer);
                if (encodeLoop.isOverflow()) {
                    return encodeLoop;
                }
                if (encodeLoop.isUnderflow()) {
                    if (!z || !charBuffer.hasRemaining()) {
                        return encodeLoop;
                    }
                    encodeLoop = CoderResult.malformedForLength(charBuffer.remaining());
                }
                if (encodeLoop.isMalformed()) {
                    codingErrorAction = this.malformedInputAction;
                } else {
                    codingErrorAction = encodeLoop.isUnmappable() ? this.unmappableCharacterAction : null;
                }
                if (codingErrorAction == CodingErrorAction.REPORT) {
                    return encodeLoop;
                }
                if (codingErrorAction == CodingErrorAction.REPLACE) {
                    int remaining = byteBuffer.remaining();
                    byte[] bArr = this.replacement;
                    if (remaining < bArr.length) {
                        return CoderResult.OVERFLOW;
                    }
                    byteBuffer.put(bArr);
                }
                if (codingErrorAction == CodingErrorAction.IGNORE || codingErrorAction == CodingErrorAction.REPLACE) {
                    charBuffer.position(charBuffer.position() + encodeLoop.length());
                }
            } catch (BufferUnderflowException e) {
                throw new CoderMalfunctionError(e);
            } catch (BufferOverflowException e2) {
                throw new CoderMalfunctionError(e2);
            }
        }
        return encodeLoop;
    }

    public final CoderResult flush(ByteBuffer byteBuffer) {
        int i = this.state;
        if (i == 2) {
            CoderResult implFlush = implFlush(byteBuffer);
            if (implFlush.isUnderflow()) {
                this.state = 3;
            }
            return implFlush;
        }
        if (i != 3) {
            throwIllegalStateException(i, 3);
        }
        return CoderResult.UNDERFLOW;
    }

    /* access modifiers changed from: protected */
    public CoderResult implFlush(ByteBuffer byteBuffer) {
        return CoderResult.UNDERFLOW;
    }

    public final CharsetEncoder reset() {
        implReset();
        this.state = 0;
        return this;
    }

    public final ByteBuffer encode(CharBuffer charBuffer) throws CharacterCodingException {
        int remaining = (int) (((float) charBuffer.remaining()) * averageBytesPerChar());
        ByteBuffer allocate = ByteBuffer.allocate(remaining);
        if (remaining == 0 && charBuffer.remaining() == 0) {
            return allocate;
        }
        reset();
        while (true) {
            CoderResult encode = charBuffer.hasRemaining() ? encode(charBuffer, allocate, true) : CoderResult.UNDERFLOW;
            if (encode.isUnderflow()) {
                encode = flush(allocate);
            }
            if (encode.isUnderflow()) {
                allocate.flip();
                return allocate;
            } else if (encode.isOverflow()) {
                remaining = (remaining * 2) + 1;
                ByteBuffer allocate2 = ByteBuffer.allocate(remaining);
                allocate.flip();
                allocate2.put(allocate);
                allocate = allocate2;
            } else {
                encode.throwException();
            }
        }
    }

    /* JADX INFO: finally extract failed */
    private boolean canEncode(CharBuffer charBuffer) {
        int i = this.state;
        if (i == 3) {
            reset();
        } else if (i != 0) {
            throwIllegalStateException(i, 1);
        }
        if (!charBuffer.hasRemaining()) {
            return true;
        }
        CodingErrorAction malformedInputAction2 = malformedInputAction();
        CodingErrorAction unmappableCharacterAction2 = unmappableCharacterAction();
        try {
            onMalformedInput(CodingErrorAction.REPORT);
            onUnmappableCharacter(CodingErrorAction.REPORT);
            encode(charBuffer);
            onMalformedInput(malformedInputAction2);
            onUnmappableCharacter(unmappableCharacterAction2);
            reset();
            return true;
        } catch (CharacterCodingException unused) {
            onMalformedInput(malformedInputAction2);
            onUnmappableCharacter(unmappableCharacterAction2);
            reset();
            return false;
        } catch (Throwable th) {
            onMalformedInput(malformedInputAction2);
            onUnmappableCharacter(unmappableCharacterAction2);
            reset();
            throw th;
        }
    }

    public boolean canEncode(char c) {
        CharBuffer allocate = CharBuffer.allocate(1);
        allocate.put(c);
        allocate.flip();
        return canEncode(allocate);
    }

    public boolean canEncode(CharSequence charSequence) {
        CharBuffer charBuffer;
        if (charSequence instanceof CharBuffer) {
            charBuffer = ((CharBuffer) charSequence).duplicate();
        } else {
            charBuffer = CharBuffer.wrap(charSequence);
        }
        return canEncode(charBuffer);
    }

    private void throwIllegalStateException(int i, int i2) {
        throw new IllegalStateException("Current state = " + stateNames[i] + ", new state = " + stateNames[i2]);
    }
}
