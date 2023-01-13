package java.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.p026io.IOException;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class BitSet implements Cloneable, Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ADDRESS_BITS_PER_WORD = 6;
    private static final int BITS_PER_WORD = 64;
    private static final int BIT_INDEX_MASK = 63;
    private static final long WORD_MASK = -1;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("bits", long[].class)};
    private static final long serialVersionUID = 7997698588986878753L;
    private transient boolean sizeIsSticky = false;
    /* access modifiers changed from: private */
    public long[] words;
    /* access modifiers changed from: private */
    public transient int wordsInUse = 0;

    private void checkInvariants() {
    }

    /* access modifiers changed from: private */
    public static int wordIndex(int i) {
        return i >> 6;
    }

    private void recalculateWordsInUse() {
        int i = this.wordsInUse - 1;
        while (i >= 0 && this.words[i] == 0) {
            i--;
        }
        this.wordsInUse = i + 1;
    }

    public BitSet() {
        initWords(64);
        this.sizeIsSticky = false;
    }

    public BitSet(int i) {
        if (i >= 0) {
            initWords(i);
            this.sizeIsSticky = true;
            return;
        }
        throw new NegativeArraySizeException("nbits < 0: " + i);
    }

    private void initWords(int i) {
        this.words = new long[(wordIndex(i - 1) + 1)];
    }

    private BitSet(long[] jArr) {
        this.words = jArr;
        this.wordsInUse = jArr.length;
        checkInvariants();
    }

    public static BitSet valueOf(long[] jArr) {
        int length = jArr.length;
        while (length > 0 && jArr[length - 1] == 0) {
            length--;
        }
        return new BitSet(Arrays.copyOf(jArr, length));
    }

    public static BitSet valueOf(LongBuffer longBuffer) {
        LongBuffer slice = longBuffer.slice();
        int remaining = slice.remaining();
        while (remaining > 0 && slice.get(remaining - 1) == 0) {
            remaining--;
        }
        long[] jArr = new long[remaining];
        slice.get(jArr);
        return new BitSet(jArr);
    }

    public static BitSet valueOf(byte[] bArr) {
        return valueOf(ByteBuffer.wrap(bArr));
    }

    public static BitSet valueOf(ByteBuffer byteBuffer) {
        ByteBuffer order = byteBuffer.slice().order(ByteOrder.LITTLE_ENDIAN);
        int remaining = order.remaining();
        while (remaining > 0 && order.get(remaining - 1) == 0) {
            remaining--;
        }
        long[] jArr = new long[((remaining + 7) / 8)];
        order.limit(remaining);
        int i = 0;
        while (order.remaining() >= 8) {
            jArr[i] = order.getLong();
            i++;
        }
        int remaining2 = order.remaining();
        for (int i2 = 0; i2 < remaining2; i2++) {
            jArr[i] = jArr[i] | ((((long) order.get()) & 255) << (i2 * 8));
        }
        return new BitSet(jArr);
    }

    public byte[] toByteArray() {
        int i = this.wordsInUse;
        if (i == 0) {
            return new byte[0];
        }
        int i2 = i - 1;
        int i3 = i2 * 8;
        for (long j = this.words[i2]; j != 0; j >>>= 8) {
            i3++;
        }
        byte[] bArr = new byte[i3];
        ByteBuffer order = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
        for (int i4 = 0; i4 < i2; i4++) {
            order.putLong(this.words[i4]);
        }
        for (long j2 = this.words[i2]; j2 != 0; j2 >>>= 8) {
            order.put((byte) ((int) (255 & j2)));
        }
        return bArr;
    }

    public long[] toLongArray() {
        return Arrays.copyOf(this.words, this.wordsInUse);
    }

    private void ensureCapacity(int i) {
        long[] jArr = this.words;
        if (jArr.length < i) {
            this.words = Arrays.copyOf(this.words, Math.max(jArr.length * 2, i));
            this.sizeIsSticky = false;
        }
    }

    private void expandTo(int i) {
        int i2 = i + 1;
        if (this.wordsInUse < i2) {
            ensureCapacity(i2);
            this.wordsInUse = i2;
        }
    }

    private static void checkRange(int i, int i2) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("fromIndex < 0: " + i);
        } else if (i2 < 0) {
            throw new IndexOutOfBoundsException("toIndex < 0: " + i2);
        } else if (i > i2) {
            throw new IndexOutOfBoundsException("fromIndex: " + i + " > toIndex: " + i2);
        }
    }

    public void flip(int i) {
        if (i >= 0) {
            int wordIndex = wordIndex(i);
            expandTo(wordIndex);
            long[] jArr = this.words;
            jArr[wordIndex] = jArr[wordIndex] ^ (1 << i);
            recalculateWordsInUse();
            checkInvariants();
            return;
        }
        throw new IndexOutOfBoundsException("bitIndex < 0: " + i);
    }

    public void flip(int i, int i2) {
        checkRange(i, i2);
        if (i != i2) {
            int wordIndex = wordIndex(i);
            int wordIndex2 = wordIndex(i2 - 1);
            expandTo(wordIndex2);
            long j = -1 << i;
            long j2 = -1 >>> (-i2);
            if (wordIndex == wordIndex2) {
                long[] jArr = this.words;
                jArr[wordIndex] = (j2 & j) ^ jArr[wordIndex];
            } else {
                long[] jArr2 = this.words;
                jArr2[wordIndex] = jArr2[wordIndex] ^ j;
                while (true) {
                    wordIndex++;
                    if (wordIndex >= wordIndex2) {
                        break;
                    }
                    long[] jArr3 = this.words;
                    jArr3[wordIndex] = ~jArr3[wordIndex];
                }
                long[] jArr4 = this.words;
                jArr4[wordIndex2] = j2 ^ jArr4[wordIndex2];
            }
            recalculateWordsInUse();
            checkInvariants();
        }
    }

    public void set(int i) {
        if (i >= 0) {
            int wordIndex = wordIndex(i);
            expandTo(wordIndex);
            long[] jArr = this.words;
            jArr[wordIndex] = jArr[wordIndex] | (1 << i);
            checkInvariants();
            return;
        }
        throw new IndexOutOfBoundsException("bitIndex < 0: " + i);
    }

    public void set(int i, boolean z) {
        if (z) {
            set(i);
        } else {
            clear(i);
        }
    }

    public void set(int i, int i2) {
        checkRange(i, i2);
        if (i != i2) {
            int wordIndex = wordIndex(i);
            int wordIndex2 = wordIndex(i2 - 1);
            expandTo(wordIndex2);
            long j = -1 << i;
            long j2 = -1 >>> (-i2);
            if (wordIndex == wordIndex2) {
                long[] jArr = this.words;
                jArr[wordIndex] = (j2 & j) | jArr[wordIndex];
            } else {
                long[] jArr2 = this.words;
                jArr2[wordIndex] = j | jArr2[wordIndex];
                while (true) {
                    wordIndex++;
                    if (wordIndex >= wordIndex2) {
                        break;
                    }
                    this.words[wordIndex] = -1;
                }
                long[] jArr3 = this.words;
                jArr3[wordIndex2] = j2 | jArr3[wordIndex2];
            }
            checkInvariants();
        }
    }

    public void set(int i, int i2, boolean z) {
        if (z) {
            set(i, i2);
        } else {
            clear(i, i2);
        }
    }

    public void clear(int i) {
        if (i >= 0) {
            int wordIndex = wordIndex(i);
            if (wordIndex < this.wordsInUse) {
                long[] jArr = this.words;
                jArr[wordIndex] = jArr[wordIndex] & (~(1 << i));
                recalculateWordsInUse();
                checkInvariants();
                return;
            }
            return;
        }
        throw new IndexOutOfBoundsException("bitIndex < 0: " + i);
    }

    public void clear(int i, int i2) {
        int wordIndex;
        checkRange(i, i2);
        if (i != i2 && (wordIndex = wordIndex(i)) < this.wordsInUse) {
            int wordIndex2 = wordIndex(i2 - 1);
            if (wordIndex2 >= this.wordsInUse) {
                i2 = length();
                wordIndex2 = this.wordsInUse - 1;
            }
            long j = -1 << i;
            long j2 = -1 >>> (-i2);
            if (wordIndex == wordIndex2) {
                long[] jArr = this.words;
                jArr[wordIndex] = (~(j2 & j)) & jArr[wordIndex];
            } else {
                long[] jArr2 = this.words;
                jArr2[wordIndex] = (~j) & jArr2[wordIndex];
                while (true) {
                    wordIndex++;
                    if (wordIndex >= wordIndex2) {
                        break;
                    }
                    this.words[wordIndex] = 0;
                }
                long[] jArr3 = this.words;
                jArr3[wordIndex2] = (~j2) & jArr3[wordIndex2];
            }
            recalculateWordsInUse();
            checkInvariants();
        }
    }

    public void clear() {
        while (true) {
            int i = this.wordsInUse;
            if (i > 0) {
                long[] jArr = this.words;
                int i2 = i - 1;
                this.wordsInUse = i2;
                jArr[i2] = 0;
            } else {
                return;
            }
        }
    }

    public boolean get(int i) {
        if (i >= 0) {
            checkInvariants();
            int wordIndex = wordIndex(i);
            if (wordIndex < this.wordsInUse) {
                if (((1 << i) & this.words[wordIndex]) != 0) {
                    return true;
                }
            }
            return false;
        }
        throw new IndexOutOfBoundsException("bitIndex < 0: " + i);
    }

    public BitSet get(int i, int i2) {
        int i3;
        long j;
        long j2;
        checkRange(i, i2);
        checkInvariants();
        int length = length();
        int i4 = 0;
        if (length <= i || i == i2) {
            return new BitSet(0);
        }
        if (i2 > length) {
            i2 = length;
        }
        int i5 = i2 - i;
        BitSet bitSet = new BitSet(i5);
        int wordIndex = wordIndex(i5 - 1) + 1;
        int wordIndex2 = wordIndex(i);
        int i6 = i & 63;
        boolean z = i6 == 0;
        while (true) {
            i3 = wordIndex - 1;
            if (i4 >= i3) {
                break;
            }
            long[] jArr = bitSet.words;
            if (z) {
                j2 = this.words[wordIndex2];
            } else {
                long[] jArr2 = this.words;
                j2 = (jArr2[wordIndex2] >>> i) | (jArr2[wordIndex2 + 1] << (-i));
            }
            jArr[i4] = j2;
            i4++;
            wordIndex2++;
        }
        long j3 = -1 >>> (-i2);
        long[] jArr3 = bitSet.words;
        if (((i2 - 1) & 63) < i6) {
            long[] jArr4 = this.words;
            j = ((jArr4[wordIndex2 + 1] & j3) << (-i)) | (jArr4[wordIndex2] >>> i);
        } else {
            j = (this.words[wordIndex2] & j3) >>> i;
        }
        jArr3[i3] = j;
        bitSet.wordsInUse = wordIndex;
        bitSet.recalculateWordsInUse();
        bitSet.checkInvariants();
        return bitSet;
    }

    public int nextSetBit(int i) {
        if (i >= 0) {
            checkInvariants();
            int wordIndex = wordIndex(i);
            if (wordIndex >= this.wordsInUse) {
                return -1;
            }
            long j = this.words[wordIndex] & (-1 << i);
            while (j == 0) {
                wordIndex++;
                if (wordIndex == this.wordsInUse) {
                    return -1;
                }
                j = this.words[wordIndex];
            }
            return (wordIndex * 64) + Long.numberOfTrailingZeros(j);
        }
        throw new IndexOutOfBoundsException("fromIndex < 0: " + i);
    }

    public int nextClearBit(int i) {
        if (i >= 0) {
            checkInvariants();
            int wordIndex = wordIndex(i);
            if (wordIndex >= this.wordsInUse) {
                return i;
            }
            long j = (~this.words[wordIndex]) & (-1 << i);
            while (j == 0) {
                wordIndex++;
                int i2 = this.wordsInUse;
                if (wordIndex == i2) {
                    return i2 * 64;
                }
                j = ~this.words[wordIndex];
            }
            return (wordIndex * 64) + Long.numberOfTrailingZeros(j);
        }
        throw new IndexOutOfBoundsException("fromIndex < 0: " + i);
    }

    public int previousSetBit(int i) {
        if (i >= 0) {
            checkInvariants();
            int wordIndex = wordIndex(i);
            if (wordIndex >= this.wordsInUse) {
                return length() - 1;
            }
            long j = this.words[wordIndex] & (-1 >>> (-(i + 1)));
            while (j == 0) {
                int i2 = wordIndex - 1;
                if (wordIndex == 0) {
                    return -1;
                }
                j = this.words[i2];
                wordIndex = i2;
            }
            return (((wordIndex + 1) * 64) - 1) - Long.numberOfLeadingZeros(j);
        } else if (i == -1) {
            return -1;
        } else {
            throw new IndexOutOfBoundsException("fromIndex < -1: " + i);
        }
    }

    public int previousClearBit(int i) {
        if (i >= 0) {
            checkInvariants();
            int wordIndex = wordIndex(i);
            if (wordIndex >= this.wordsInUse) {
                return i;
            }
            long j = (~this.words[wordIndex]) & (-1 >>> (-(i + 1)));
            while (j == 0) {
                int i2 = wordIndex - 1;
                if (wordIndex == 0) {
                    return -1;
                }
                j = ~this.words[i2];
                wordIndex = i2;
            }
            return (((wordIndex + 1) * 64) - 1) - Long.numberOfLeadingZeros(j);
        } else if (i == -1) {
            return -1;
        } else {
            throw new IndexOutOfBoundsException("fromIndex < -1: " + i);
        }
    }

    public int length() {
        int i = this.wordsInUse;
        if (i == 0) {
            return 0;
        }
        return ((i - 1) * 64) + (64 - Long.numberOfLeadingZeros(this.words[i - 1]));
    }

    public boolean isEmpty() {
        return this.wordsInUse == 0;
    }

    public boolean intersects(BitSet bitSet) {
        for (int min = Math.min(this.wordsInUse, bitSet.wordsInUse) - 1; min >= 0; min--) {
            if ((this.words[min] & bitSet.words[min]) != 0) {
                return true;
            }
        }
        return false;
    }

    public int cardinality() {
        int i = 0;
        for (int i2 = 0; i2 < this.wordsInUse; i2++) {
            i += Long.bitCount(this.words[i2]);
        }
        return i;
    }

    public void and(BitSet bitSet) {
        if (this != bitSet) {
            while (true) {
                int i = this.wordsInUse;
                if (i <= bitSet.wordsInUse) {
                    break;
                }
                long[] jArr = this.words;
                int i2 = i - 1;
                this.wordsInUse = i2;
                jArr[i2] = 0;
            }
            for (int i3 = 0; i3 < this.wordsInUse; i3++) {
                long[] jArr2 = this.words;
                jArr2[i3] = jArr2[i3] & bitSet.words[i3];
            }
            recalculateWordsInUse();
            checkInvariants();
        }
    }

    /* renamed from: or */
    public void mo61455or(BitSet bitSet) {
        if (this != bitSet) {
            int min = Math.min(this.wordsInUse, bitSet.wordsInUse);
            int i = this.wordsInUse;
            int i2 = bitSet.wordsInUse;
            if (i < i2) {
                ensureCapacity(i2);
                this.wordsInUse = bitSet.wordsInUse;
            }
            for (int i3 = 0; i3 < min; i3++) {
                long[] jArr = this.words;
                jArr[i3] = jArr[i3] | bitSet.words[i3];
            }
            if (min < bitSet.wordsInUse) {
                System.arraycopy((Object) bitSet.words, min, (Object) this.words, min, this.wordsInUse - min);
            }
            checkInvariants();
        }
    }

    public void xor(BitSet bitSet) {
        int min = Math.min(this.wordsInUse, bitSet.wordsInUse);
        int i = this.wordsInUse;
        int i2 = bitSet.wordsInUse;
        if (i < i2) {
            ensureCapacity(i2);
            this.wordsInUse = bitSet.wordsInUse;
        }
        for (int i3 = 0; i3 < min; i3++) {
            long[] jArr = this.words;
            jArr[i3] = jArr[i3] ^ bitSet.words[i3];
        }
        int i4 = bitSet.wordsInUse;
        if (min < i4) {
            System.arraycopy((Object) bitSet.words, min, (Object) this.words, min, i4 - min);
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public void andNot(BitSet bitSet) {
        for (int min = Math.min(this.wordsInUse, bitSet.wordsInUse) - 1; min >= 0; min--) {
            long[] jArr = this.words;
            jArr[min] = jArr[min] & (~bitSet.words[min]);
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public int hashCode() {
        int i = this.wordsInUse;
        long j = 1234;
        while (true) {
            i--;
            if (i < 0) {
                return (int) ((j >> 32) ^ j);
            }
            j ^= this.words[i] * ((long) (i + 1));
        }
    }

    public int size() {
        return this.words.length * 64;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BitSet)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        BitSet bitSet = (BitSet) obj;
        checkInvariants();
        bitSet.checkInvariants();
        if (this.wordsInUse != bitSet.wordsInUse) {
            return false;
        }
        for (int i = 0; i < this.wordsInUse; i++) {
            if (this.words[i] != bitSet.words[i]) {
                return false;
            }
        }
        return true;
    }

    public Object clone() {
        if (!this.sizeIsSticky) {
            trimToSize();
        }
        try {
            BitSet bitSet = (BitSet) super.clone();
            bitSet.words = (long[]) this.words.clone();
            bitSet.checkInvariants();
            return bitSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    private void trimToSize() {
        int i = this.wordsInUse;
        long[] jArr = this.words;
        if (i != jArr.length) {
            this.words = Arrays.copyOf(jArr, i);
            checkInvariants();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        checkInvariants();
        if (!this.sizeIsSticky) {
            trimToSize();
        }
        objectOutputStream.putFields().put("bits", (Object) this.words);
        objectOutputStream.writeFields();
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readObject(java.p026io.ObjectInputStream r7) {
        /*
            r6 = this;
            java.io.ObjectInputStream$GetField r7 = r7.readFields()
            java.lang.String r0 = "bits"
            r1 = 0
            java.lang.Object r7 = r7.get((java.lang.String) r0, (java.lang.Object) r1)
            long[] r7 = (long[]) r7
            r6.words = r7
            int r7 = r7.length
            r6.wordsInUse = r7
            r6.recalculateWordsInUse()
            long[] r7 = r6.words
            int r0 = r7.length
            if (r0 <= 0) goto L_0x0026
            int r0 = r7.length
            r1 = 1
            int r0 = r0 - r1
            r2 = r7[r0]
            r4 = 0
            int r7 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r7 != 0) goto L_0x0026
            goto L_0x0027
        L_0x0026:
            r1 = 0
        L_0x0027:
            r6.sizeIsSticky = r1
            r6.checkInvariants()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.BitSet.readObject(java.io.ObjectInputStream):void");
    }

    public String toString() {
        checkInvariants();
        int i = this.wordsInUse;
        StringBuilder sb = new StringBuilder(((i > 128 ? cardinality() : i * 64) * 6) + 2);
        sb.append('{');
        int nextSetBit = nextSetBit(0);
        if (nextSetBit != -1) {
            sb.append(nextSetBit);
            while (true) {
                int i2 = nextSetBit + 1;
                if (i2 < 0 || (nextSetBit = nextSetBit(i2)) < 0) {
                    break;
                }
                int nextClearBit = nextClearBit(nextSetBit);
                do {
                    sb.append(", ");
                    sb.append(nextSetBit);
                    nextSetBit++;
                } while (nextSetBit != nextClearBit);
            }
        }
        sb.append('}');
        return sb.toString();
    }

    /* renamed from: java.util.BitSet$1BitSetSpliterator  reason: invalid class name */
    class AnonymousClass1BitSetSpliterator implements Spliterator.OfInt {
        private int est;
        private int fence;
        private int index;
        private boolean root;

        public Comparator<? super Integer> getComparator() {
            return null;
        }

        {
            this.index = r2;
            this.fence = r3;
            this.est = r4;
            this.root = r5;
        }

        private int getFence() {
            int i = this.fence;
            if (i < 0) {
                if (BitSet.this.wordsInUse >= BitSet.wordIndex(Integer.MAX_VALUE)) {
                    i = Integer.MAX_VALUE;
                } else {
                    i = BitSet.this.wordsInUse << 6;
                }
                this.fence = i;
                this.est = BitSet.this.cardinality();
                this.index = BitSet.this.nextSetBit(0);
            }
            return i;
        }

        public boolean tryAdvance(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            int fence2 = getFence();
            int i = this.index;
            if (i >= 0 && i < fence2) {
                this.index = BitSet.this.nextSetBit(i + 1, BitSet.wordIndex(fence2 - 1));
                intConsumer.accept(i);
                return true;
            } else if (i != Integer.MAX_VALUE || fence2 != Integer.MAX_VALUE) {
                return false;
            } else {
                this.index = -1;
                intConsumer.accept(Integer.MAX_VALUE);
                return true;
            }
        }

        public void forEachRemaining(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            int fence2 = getFence();
            int i = this.index;
            this.index = -1;
            if (i >= 0 && i < fence2) {
                int i2 = i + 1;
                intConsumer.accept(i);
                int r1 = BitSet.wordIndex(i2);
                int r3 = BitSet.wordIndex(fence2 - 1);
                loop0:
                while (r1 <= r3 && i2 <= fence2) {
                    long j = BitSet.this.words[r1] & (-1 << i2);
                    while (j != 0) {
                        i2 = (r1 << 6) + Long.numberOfTrailingZeros(j);
                        if (i2 >= fence2) {
                            break loop0;
                        }
                        j &= ~(1 << i2);
                        intConsumer.accept(i2);
                    }
                    r1++;
                    i2 = r1 << 6;
                }
                i = i2;
            }
            if (i == Integer.MAX_VALUE && fence2 == Integer.MAX_VALUE) {
                intConsumer.accept(Integer.MAX_VALUE);
            }
        }

        public Spliterator.OfInt trySplit() {
            int fence2 = getFence();
            int i = this.index;
            if (i < 0) {
                return null;
            }
            int i2 = Integer.MAX_VALUE;
            if (fence2 < Integer.MAX_VALUE || !BitSet.this.get(Integer.MAX_VALUE)) {
                i2 = BitSet.this.previousSetBit(fence2 - 1) + 1;
            }
            this.fence = i2;
            int i3 = (i + i2) >>> 1;
            if (i >= i3) {
                return null;
            }
            this.index = BitSet.this.nextSetBit(i3, BitSet.wordIndex(i2 - 1));
            this.root = false;
            BitSet bitSet = BitSet.this;
            int i4 = this.est >>> 1;
            this.est = i4;
            return new AnonymousClass1BitSetSpliterator(i, i3, i4, false);
        }

        public long estimateSize() {
            getFence();
            return (long) this.est;
        }

        public int characteristics() {
            return (this.root ? 64 : 0) | 16 | 1 | 4;
        }
    }

    public IntStream stream() {
        return StreamSupport.intStream(new AnonymousClass1BitSetSpliterator(0, -1, 0, true), false);
    }

    /* access modifiers changed from: private */
    public int nextSetBit(int i, int i2) {
        int wordIndex = wordIndex(i);
        if (wordIndex > i2) {
            return -1;
        }
        long j = this.words[wordIndex] & (-1 << i);
        while (j == 0) {
            wordIndex++;
            if (wordIndex > i2) {
                return -1;
            }
            j = this.words[wordIndex];
        }
        return (wordIndex * 64) + Long.numberOfTrailingZeros(j);
    }
}
