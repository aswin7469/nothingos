package java.nio;

import java.util.Spliterator;
import java.util.function.IntConsumer;

class CharBufferSpliterator implements Spliterator.OfInt {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final CharBuffer buffer;
    private int index;
    private final int limit;

    public int characteristics() {
        return 16464;
    }

    CharBufferSpliterator(CharBuffer charBuffer) {
        this(charBuffer, charBuffer.position(), charBuffer.limit());
    }

    CharBufferSpliterator(CharBuffer charBuffer, int i, int i2) {
        this.buffer = charBuffer;
        this.index = i > i2 ? i2 : i;
        this.limit = i2;
    }

    public Spliterator.OfInt trySplit() {
        int i = this.index;
        int i2 = (this.limit + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        CharBuffer charBuffer = this.buffer;
        this.index = i2;
        return new CharBufferSpliterator(charBuffer, i, i2);
    }

    public void forEachRemaining(IntConsumer intConsumer) {
        intConsumer.getClass();
        CharBuffer charBuffer = this.buffer;
        int i = this.limit;
        this.index = i;
        for (int i2 = this.index; i2 < i; i2++) {
            intConsumer.accept(charBuffer.getUnchecked(i2));
        }
    }

    public boolean tryAdvance(IntConsumer intConsumer) {
        intConsumer.getClass();
        int i = this.index;
        if (i < 0 || i >= this.limit) {
            return false;
        }
        CharBuffer charBuffer = this.buffer;
        this.index = i + 1;
        intConsumer.accept(charBuffer.getUnchecked(i));
        return true;
    }

    public long estimateSize() {
        return (long) (this.limit - this.index);
    }
}
