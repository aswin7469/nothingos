package java.nio.charset;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public abstract class CharsetDecoder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ST_CODING = 1;
    private static final int ST_END = 2;
    private static final int ST_FLUSHED = 3;
    private static final int ST_RESET = 0;
    private static String[] stateNames = {"RESET", "CODING", "CODING_END", "FLUSHED"};
    private final float averageCharsPerByte;
    private final Charset charset;
    private CodingErrorAction malformedInputAction;
    private final float maxCharsPerByte;
    private String replacement;
    private int state;
    private CodingErrorAction unmappableCharacterAction;

    /* access modifiers changed from: protected */
    public abstract CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer);

    /* access modifiers changed from: protected */
    public void implOnMalformedInput(CodingErrorAction codingErrorAction) {
    }

    /* access modifiers changed from: protected */
    public void implOnUnmappableCharacter(CodingErrorAction codingErrorAction) {
    }

    /* access modifiers changed from: protected */
    public void implReplaceWith(String str) {
    }

    /* access modifiers changed from: protected */
    public void implReset() {
    }

    public boolean isAutoDetecting() {
        return false;
    }

    private CharsetDecoder(Charset charset2, float f, float f2, String str) {
        this.malformedInputAction = CodingErrorAction.REPORT;
        this.unmappableCharacterAction = CodingErrorAction.REPORT;
        this.state = 0;
        this.charset = charset2;
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Non-positive averageCharsPerByte");
        } else if (f2 <= 0.0f) {
            throw new IllegalArgumentException("Non-positive maxCharsPerByte");
        } else if (Charset.atBugLevel("1.4") || f <= f2) {
            this.replacement = str;
            this.averageCharsPerByte = f;
            this.maxCharsPerByte = f2;
        } else {
            throw new IllegalArgumentException("averageCharsPerByte exceeds maxCharsPerByte");
        }
    }

    protected CharsetDecoder(Charset charset2, float f, float f2) {
        this(charset2, f, f2, "�");
    }

    public final Charset charset() {
        return this.charset;
    }

    public final String replacement() {
        return this.replacement;
    }

    public final CharsetDecoder replaceWith(String str) {
        if (str != null) {
            int length = str.length();
            if (length == 0) {
                throw new IllegalArgumentException("Empty replacement");
            } else if (((float) length) <= this.maxCharsPerByte) {
                this.replacement = str;
                implReplaceWith(str);
                return this;
            } else {
                throw new IllegalArgumentException("Replacement too long");
            }
        } else {
            throw new IllegalArgumentException("Null replacement");
        }
    }

    public CodingErrorAction malformedInputAction() {
        return this.malformedInputAction;
    }

    public final CharsetDecoder onMalformedInput(CodingErrorAction codingErrorAction) {
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

    public final CharsetDecoder onUnmappableCharacter(CodingErrorAction codingErrorAction) {
        if (codingErrorAction != null) {
            this.unmappableCharacterAction = codingErrorAction;
            implOnUnmappableCharacter(codingErrorAction);
            return this;
        }
        throw new IllegalArgumentException("Null action");
    }

    public final float averageCharsPerByte() {
        return this.averageCharsPerByte;
    }

    public final float maxCharsPerByte() {
        return this.maxCharsPerByte;
    }

    public final CoderResult decode(ByteBuffer byteBuffer, CharBuffer charBuffer, boolean z) {
        CoderResult decodeLoop;
        CodingErrorAction codingErrorAction;
        int i = z ? 2 : 1;
        int i2 = this.state;
        if (!(i2 == 0 || i2 == 1 || (z && i2 == 2))) {
            throwIllegalStateException(i2, i);
        }
        this.state = i;
        while (true) {
            try {
                decodeLoop = decodeLoop(byteBuffer, charBuffer);
                if (decodeLoop.isOverflow()) {
                    return decodeLoop;
                }
                if (decodeLoop.isUnderflow()) {
                    if (!z || !byteBuffer.hasRemaining()) {
                        return decodeLoop;
                    }
                    decodeLoop = CoderResult.malformedForLength(byteBuffer.remaining());
                }
                if (decodeLoop.isMalformed()) {
                    codingErrorAction = this.malformedInputAction;
                } else {
                    codingErrorAction = decodeLoop.isUnmappable() ? this.unmappableCharacterAction : null;
                }
                if (codingErrorAction == CodingErrorAction.REPORT) {
                    return decodeLoop;
                }
                if (codingErrorAction == CodingErrorAction.REPLACE) {
                    if (charBuffer.remaining() < this.replacement.length()) {
                        return CoderResult.OVERFLOW;
                    }
                    charBuffer.put(this.replacement);
                }
                if (codingErrorAction == CodingErrorAction.IGNORE || codingErrorAction == CodingErrorAction.REPLACE) {
                    byteBuffer.position(byteBuffer.position() + decodeLoop.length());
                }
            } catch (BufferUnderflowException e) {
                throw new CoderMalfunctionError(e);
            } catch (BufferOverflowException e2) {
                throw new CoderMalfunctionError(e2);
            }
        }
        return decodeLoop;
    }

    public final CoderResult flush(CharBuffer charBuffer) {
        int i = this.state;
        if (i == 2) {
            CoderResult implFlush = implFlush(charBuffer);
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
    public CoderResult implFlush(CharBuffer charBuffer) {
        return CoderResult.UNDERFLOW;
    }

    public final CharsetDecoder reset() {
        implReset();
        this.state = 0;
        return this;
    }

    public final CharBuffer decode(ByteBuffer byteBuffer) throws CharacterCodingException {
        int remaining = (int) (((float) byteBuffer.remaining()) * averageCharsPerByte());
        CharBuffer allocate = CharBuffer.allocate(remaining);
        if (remaining == 0 && byteBuffer.remaining() == 0) {
            return allocate;
        }
        reset();
        while (true) {
            CoderResult decode = byteBuffer.hasRemaining() ? decode(byteBuffer, allocate, true) : CoderResult.UNDERFLOW;
            if (decode.isUnderflow()) {
                decode = flush(allocate);
            }
            if (decode.isUnderflow()) {
                allocate.flip();
                return allocate;
            } else if (decode.isOverflow()) {
                remaining = (remaining * 2) + 1;
                CharBuffer allocate2 = CharBuffer.allocate(remaining);
                allocate.flip();
                allocate2.put(allocate);
                allocate = allocate2;
            } else {
                decode.throwException();
            }
        }
    }

    public boolean isCharsetDetected() {
        throw new UnsupportedOperationException();
    }

    public Charset detectedCharset() {
        throw new UnsupportedOperationException();
    }

    private void throwIllegalStateException(int i, int i2) {
        throw new IllegalStateException("Current state = " + stateNames[i] + ", new state = " + stateNames[i2]);
    }
}
