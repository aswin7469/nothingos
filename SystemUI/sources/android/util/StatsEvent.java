package android.util;

import android.annotation.SystemApi;
import android.os.SystemClock;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@SystemApi
public final class StatsEvent {
    public static final int ERROR_ANNOTATION_DOES_NOT_FOLLOW_FIELD = 32;
    public static final int ERROR_ANNOTATION_ID_TOO_LARGE = 128;
    public static final int ERROR_ATOM_ID_INVALID_POSITION = 8192;
    public static final int ERROR_ATTRIBUTION_CHAIN_TOO_LONG = 8;
    public static final int ERROR_ATTRIBUTION_UIDS_TAGS_SIZES_NOT_EQUAL = 4096;
    public static final int ERROR_INVALID_ANNOTATION_ID = 64;
    public static final int ERROR_LIST_TOO_LONG = 16384;
    public static final int ERROR_NO_ATOM_ID = 2;
    public static final int ERROR_NO_TIMESTAMP = 1;
    public static final int ERROR_OVERFLOW = 4;
    public static final int ERROR_TOO_MANY_ANNOTATIONS = 256;
    public static final int ERROR_TOO_MANY_FIELDS = 512;
    public static final int ERROR_TOO_MANY_KEY_VALUE_PAIRS = 16;
    private static final int LOGGER_ENTRY_MAX_PAYLOAD = 4068;
    public static final int MAX_ANNOTATION_COUNT = 15;
    public static final int MAX_ATTRIBUTION_NODES = 127;
    public static final int MAX_KEY_VALUE_PAIRS = 127;
    public static final int MAX_NUM_ELEMENTS = 127;
    private static final int MAX_PULL_PAYLOAD_SIZE = 51200;
    private static final int MAX_PUSH_PAYLOAD_SIZE = 4064;
    public static final byte TYPE_ATTRIBUTION_CHAIN = 9;
    public static final byte TYPE_BOOLEAN = 5;
    public static final byte TYPE_BYTE_ARRAY = 6;
    public static final byte TYPE_ERRORS = 15;
    public static final byte TYPE_FLOAT = 4;
    public static final byte TYPE_INT = 0;
    public static final byte TYPE_KEY_VALUE_PAIRS = 8;
    public static final byte TYPE_LIST = 3;
    public static final byte TYPE_LONG = 1;
    public static final byte TYPE_OBJECT = 7;
    public static final byte TYPE_STRING = 2;
    private final int mAtomId;
    private Buffer mBuffer;
    private final int mNumBytes;
    private final byte[] mPayload;

    private StatsEvent(int i, Buffer buffer, byte[] bArr, int i2) {
        this.mAtomId = i;
        this.mBuffer = buffer;
        this.mPayload = bArr;
        this.mNumBytes = i2;
    }

    public static Builder newBuilder() {
        return new Builder(Buffer.obtain());
    }

    public int getAtomId() {
        return this.mAtomId;
    }

    public byte[] getBytes() {
        return this.mPayload;
    }

    public int getNumBytes() {
        return this.mNumBytes;
    }

    public void release() {
        Buffer buffer = this.mBuffer;
        if (buffer != null) {
            buffer.release();
            this.mBuffer = null;
        }
    }

    public static final class Builder {
        private static final int POS_ATOM_ID = 11;
        private static final int POS_NUM_ELEMENTS = 1;
        private static final int POS_TIMESTAMP_NS = 2;
        private int mAtomId;
        private final Buffer mBuffer;
        private byte mCurrentAnnotationCount;
        private int mErrorMask;
        private byte mLastType;
        private int mNumElements;
        private int mPos;
        private int mPosLastField;
        private long mTimestampNs;
        private boolean mUsePooledBuffer;

        private Builder(Buffer buffer) {
            this.mUsePooledBuffer = false;
            this.mBuffer = buffer;
            this.mCurrentAnnotationCount = 0;
            this.mAtomId = 0;
            this.mTimestampNs = SystemClock.elapsedRealtimeNanos();
            this.mNumElements = 0;
            this.mPos = 0;
            writeTypeId((byte) 7);
            this.mPos = 2;
            writeLong(this.mTimestampNs);
        }

        public Builder setAtomId(int i) {
            if (this.mAtomId == 0) {
                this.mAtomId = i;
                if (1 == this.mNumElements) {
                    writeInt(i);
                } else {
                    this.mErrorMask |= 8192;
                }
            }
            return this;
        }

        public Builder writeBoolean(boolean z) {
            writeTypeId((byte) 5);
            int i = this.mPos;
            this.mPos = i + this.mBuffer.putBoolean(i, z);
            this.mNumElements++;
            return this;
        }

        public Builder writeInt(int i) {
            writeTypeId((byte) 0);
            int i2 = this.mPos;
            this.mPos = i2 + this.mBuffer.putInt(i2, i);
            this.mNumElements++;
            return this;
        }

        public Builder writeLong(long j) {
            writeTypeId((byte) 1);
            int i = this.mPos;
            this.mPos = i + this.mBuffer.putLong(i, j);
            this.mNumElements++;
            return this;
        }

        public Builder writeFloat(float f) {
            writeTypeId((byte) 4);
            int i = this.mPos;
            this.mPos = i + this.mBuffer.putFloat(i, f);
            this.mNumElements++;
            return this;
        }

        public Builder writeString(String str) {
            writeByteArray(stringToBytes(str), (byte) 2);
            return this;
        }

        public Builder writeByteArray(byte[] bArr) {
            writeByteArray(bArr, (byte) 6);
            return this;
        }

        private void writeByteArray(byte[] bArr, byte b) {
            writeTypeId(b);
            int length = bArr.length;
            int i = this.mPos;
            int r0 = i + this.mBuffer.putInt(i, length);
            this.mPos = r0;
            this.mPos = r0 + this.mBuffer.putByteArray(r0, bArr);
            this.mNumElements++;
        }

        public Builder writeAttributionChain(int[] iArr, String[] strArr) {
            byte length = (byte) iArr.length;
            if (length != ((byte) strArr.length)) {
                this.mErrorMask |= 4096;
            } else if (length > Byte.MAX_VALUE) {
                this.mErrorMask |= 8;
            } else {
                writeTypeId((byte) 9);
                int i = this.mPos;
                this.mPos = i + this.mBuffer.putByte(i, length);
                for (int i2 = 0; i2 < length; i2++) {
                    int i3 = this.mPos;
                    this.mPos = i3 + this.mBuffer.putInt(i3, iArr[i2]);
                    byte[] stringToBytes = stringToBytes(strArr[i2]);
                    int i4 = this.mPos;
                    int r3 = i4 + this.mBuffer.putInt(i4, stringToBytes.length);
                    this.mPos = r3;
                    this.mPos = r3 + this.mBuffer.putByteArray(r3, stringToBytes);
                }
                this.mNumElements++;
            }
            return this;
        }

        public Builder writeKeyValuePairs(SparseIntArray sparseIntArray, SparseLongArray sparseLongArray, SparseArray<String> sparseArray, SparseArray<Float> sparseArray2) {
            int i;
            int i2;
            int i3;
            int size = sparseIntArray == null ? 0 : sparseIntArray.size();
            if (sparseLongArray == null) {
                i = 0;
            } else {
                i = sparseLongArray.size();
            }
            if (sparseArray == null) {
                i2 = 0;
            } else {
                i2 = sparseArray.size();
            }
            if (sparseArray2 == null) {
                i3 = 0;
            } else {
                i3 = sparseArray2.size();
            }
            int i4 = size + i + i2 + i3;
            if (i4 > 127) {
                this.mErrorMask |= 16;
            } else {
                writeTypeId((byte) 8);
                int i5 = this.mPos;
                this.mPos = i5 + this.mBuffer.putByte(i5, (byte) i4);
                for (int i6 = 0; i6 < size; i6++) {
                    int keyAt = sparseIntArray.keyAt(i6);
                    int valueAt = sparseIntArray.valueAt(i6);
                    int i7 = this.mPos;
                    this.mPos = i7 + this.mBuffer.putInt(i7, keyAt);
                    writeTypeId((byte) 0);
                    int i8 = this.mPos;
                    this.mPos = i8 + this.mBuffer.putInt(i8, valueAt);
                }
                for (int i9 = 0; i9 < i; i9++) {
                    int keyAt2 = sparseLongArray.keyAt(i9);
                    long valueAt2 = sparseLongArray.valueAt(i9);
                    int i10 = this.mPos;
                    this.mPos = i10 + this.mBuffer.putInt(i10, keyAt2);
                    writeTypeId((byte) 1);
                    int i11 = this.mPos;
                    this.mPos = i11 + this.mBuffer.putLong(i11, valueAt2);
                }
                for (int i12 = 0; i12 < i2; i12++) {
                    int keyAt3 = sparseArray.keyAt(i12);
                    int i13 = this.mPos;
                    this.mPos = i13 + this.mBuffer.putInt(i13, keyAt3);
                    writeTypeId((byte) 2);
                    byte[] stringToBytes = stringToBytes(sparseArray.valueAt(i12));
                    int i14 = this.mPos;
                    int r2 = i14 + this.mBuffer.putInt(i14, stringToBytes.length);
                    this.mPos = r2;
                    this.mPos = r2 + this.mBuffer.putByteArray(r2, stringToBytes);
                }
                for (int i15 = 0; i15 < i3; i15++) {
                    int keyAt4 = sparseArray2.keyAt(i15);
                    float floatValue = sparseArray2.valueAt(i15).floatValue();
                    int i16 = this.mPos;
                    this.mPos = i16 + this.mBuffer.putInt(i16, keyAt4);
                    writeTypeId((byte) 4);
                    int i17 = this.mPos;
                    this.mPos = i17 + this.mBuffer.putFloat(i17, floatValue);
                }
                this.mNumElements++;
            }
            return this;
        }

        public Builder writeBooleanArray(boolean[] zArr) {
            byte length = (byte) zArr.length;
            if (writeArrayInfo(length, (byte) 5)) {
                for (int i = 0; i < length; i++) {
                    int i2 = this.mPos;
                    this.mPos = i2 + this.mBuffer.putBoolean(i2, zArr[i]);
                }
                this.mNumElements++;
            }
            return this;
        }

        public Builder writeIntArray(int[] iArr) {
            byte length = (byte) iArr.length;
            if (writeArrayInfo(length, (byte) 0)) {
                for (int i = 0; i < length; i++) {
                    int i2 = this.mPos;
                    this.mPos = i2 + this.mBuffer.putInt(i2, iArr[i]);
                }
                this.mNumElements++;
            }
            return this;
        }

        public Builder writeLongArray(long[] jArr) {
            byte length = (byte) jArr.length;
            if (writeArrayInfo(length, (byte) 1)) {
                for (int i = 0; i < length; i++) {
                    int i2 = this.mPos;
                    this.mPos = i2 + this.mBuffer.putLong(i2, jArr[i]);
                }
                this.mNumElements++;
            }
            return this;
        }

        public Builder writeFloatArray(float[] fArr) {
            byte length = (byte) fArr.length;
            if (writeArrayInfo(length, (byte) 4)) {
                for (int i = 0; i < length; i++) {
                    int i2 = this.mPos;
                    this.mPos = i2 + this.mBuffer.putFloat(i2, fArr[i]);
                }
                this.mNumElements++;
            }
            return this;
        }

        public Builder writeStringArray(String[] strArr) {
            byte length = (byte) strArr.length;
            if (writeArrayInfo(length, (byte) 2)) {
                for (int i = 0; i < length; i++) {
                    byte[] stringToBytes = stringToBytes(strArr[i]);
                    int i2 = this.mPos;
                    int r3 = i2 + this.mBuffer.putInt(i2, stringToBytes.length);
                    this.mPos = r3;
                    this.mPos = r3 + this.mBuffer.putByteArray(r3, stringToBytes);
                }
                this.mNumElements++;
            }
            return this;
        }

        public Builder addBooleanAnnotation(byte b, boolean z) {
            if (this.mNumElements < 2) {
                this.mErrorMask |= 32;
            } else if (this.mCurrentAnnotationCount >= 15) {
                this.mErrorMask |= 256;
            } else {
                int i = this.mPos;
                int r0 = i + this.mBuffer.putByte(i, b);
                this.mPos = r0;
                int r02 = r0 + this.mBuffer.putByte(r0, (byte) 5);
                this.mPos = r02;
                this.mPos = r02 + this.mBuffer.putBoolean(r02, z);
                this.mCurrentAnnotationCount = (byte) (this.mCurrentAnnotationCount + 1);
                writeAnnotationCount();
            }
            return this;
        }

        public Builder addIntAnnotation(byte b, int i) {
            if (this.mNumElements < 2) {
                this.mErrorMask |= 32;
            } else if (this.mCurrentAnnotationCount >= 15) {
                this.mErrorMask |= 256;
            } else {
                int i2 = this.mPos;
                int r0 = i2 + this.mBuffer.putByte(i2, b);
                this.mPos = r0;
                int r02 = r0 + this.mBuffer.putByte(r0, (byte) 0);
                this.mPos = r02;
                this.mPos = r02 + this.mBuffer.putInt(r02, i);
                this.mCurrentAnnotationCount = (byte) (this.mCurrentAnnotationCount + 1);
                writeAnnotationCount();
            }
            return this;
        }

        public Builder usePooledBuffer() {
            this.mUsePooledBuffer = true;
            this.mBuffer.setMaxSize(StatsEvent.MAX_PUSH_PAYLOAD_SIZE, this.mPos);
            return this;
        }

        public StatsEvent build() {
            if (0 == this.mTimestampNs) {
                this.mErrorMask |= 1;
            }
            if (this.mAtomId == 0) {
                this.mErrorMask |= 2;
            }
            if (this.mBuffer.hasOverflowed()) {
                this.mErrorMask |= 4;
            }
            int i = this.mNumElements;
            if (i > 127) {
                this.mErrorMask |= 512;
            }
            if (this.mErrorMask == 0) {
                int unused = this.mBuffer.putByte(1, (byte) i);
            } else {
                this.mPos = 11;
                int r0 = 11 + this.mBuffer.putByte(11, (byte) 0);
                this.mPos = r0;
                int r02 = r0 + this.mBuffer.putInt(r0, this.mAtomId);
                this.mPos = r02;
                int r03 = r02 + this.mBuffer.putByte(r02, (byte) 15);
                this.mPos = r03;
                this.mPos = r03 + this.mBuffer.putInt(r03, this.mErrorMask);
                int unused2 = this.mBuffer.putByte(1, (byte) 3);
            }
            int i2 = this.mPos;
            if (this.mUsePooledBuffer) {
                int i3 = this.mAtomId;
                Buffer buffer = this.mBuffer;
                return new StatsEvent(i3, buffer, buffer.getBytes(), i2);
            }
            byte[] bArr = new byte[i2];
            System.arraycopy((Object) this.mBuffer.getBytes(), 0, (Object) bArr, 0, i2);
            this.mBuffer.release();
            return new StatsEvent(this.mAtomId, (Buffer) null, bArr, i2);
        }

        private void writeTypeId(byte b) {
            int i = this.mPos;
            this.mPosLastField = i;
            this.mLastType = b;
            this.mCurrentAnnotationCount = 0;
            this.mPos = i + this.mBuffer.putByte(i, (byte) (b & 15));
        }

        private void writeAnnotationCount() {
            int unused = this.mBuffer.putByte(this.mPosLastField, (byte) ((this.mCurrentAnnotationCount << 4) | (this.mLastType & 15)));
        }

        private static byte[] stringToBytes(String str) {
            if (str == null) {
                str = "";
            }
            return str.getBytes(StandardCharsets.UTF_8);
        }

        private boolean writeArrayInfo(byte b, byte b2) {
            if (b > Byte.MAX_VALUE) {
                this.mErrorMask |= 16384;
                return false;
            }
            writeTypeId((byte) 3);
            int i = this.mPos;
            int r0 = i + this.mBuffer.putByte(i, b);
            this.mPos = r0;
            this.mPos = r0 + this.mBuffer.putByte(r0, (byte) (b2 & 15));
            return true;
        }
    }

    private static final class Buffer {
        private static Object sLock = new Object();
        private static Buffer sPool;
        private byte[] mBytes;
        private int mMaxSize = StatsEvent.MAX_PULL_PAYLOAD_SIZE;
        private boolean mOverflow = false;

        /* access modifiers changed from: private */
        public static Buffer obtain() {
            Buffer buffer;
            synchronized (sLock) {
                buffer = sPool;
                if (buffer == null) {
                    buffer = new Buffer();
                }
                sPool = null;
            }
            buffer.reset();
            return buffer;
        }

        private Buffer() {
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(StatsEvent.MAX_PUSH_PAYLOAD_SIZE);
            this.mBytes = allocateDirect.hasArray() ? allocateDirect.array() : new byte[StatsEvent.MAX_PUSH_PAYLOAD_SIZE];
        }

        /* access modifiers changed from: private */
        public byte[] getBytes() {
            return this.mBytes;
        }

        /* access modifiers changed from: private */
        public void release() {
            if (this.mBytes.length <= StatsEvent.MAX_PUSH_PAYLOAD_SIZE) {
                synchronized (sLock) {
                    if (sPool == null) {
                        sPool = this;
                    }
                }
            }
        }

        private void reset() {
            this.mOverflow = false;
            this.mMaxSize = StatsEvent.MAX_PULL_PAYLOAD_SIZE;
        }

        /* access modifiers changed from: private */
        public void setMaxSize(int i, int i2) {
            this.mMaxSize = i;
            if (i2 > i) {
                this.mOverflow = true;
            }
        }

        /* access modifiers changed from: private */
        public boolean hasOverflowed() {
            return this.mOverflow;
        }

        private boolean hasEnoughSpace(int i, int i2) {
            int i3 = i + i2;
            int i4 = this.mMaxSize;
            if (i3 > i4) {
                this.mOverflow = true;
                return false;
            }
            byte[] bArr = this.mBytes;
            if (bArr.length < i4 && i3 > bArr.length) {
                int length = bArr.length;
                do {
                    length *= 2;
                } while (length <= i3);
                int i5 = this.mMaxSize;
                if (length > i5) {
                    length = i5;
                }
                this.mBytes = Arrays.copyOf(this.mBytes, length);
            }
            return true;
        }

        /* access modifiers changed from: private */
        public int putByte(int i, byte b) {
            if (!hasEnoughSpace(i, 1)) {
                return 0;
            }
            this.mBytes[i] = b;
            return 1;
        }

        /* access modifiers changed from: private */
        public int putBoolean(int i, boolean z) {
            return putByte(i, z ? (byte) 1 : 0);
        }

        /* access modifiers changed from: private */
        public int putInt(int i, int i2) {
            if (!hasEnoughSpace(i, 4)) {
                return 0;
            }
            byte[] bArr = this.mBytes;
            bArr[i] = (byte) i2;
            bArr[i + 1] = (byte) (i2 >> 8);
            bArr[i + 2] = (byte) (i2 >> 16);
            bArr[i + 3] = (byte) (i2 >> 24);
            return 4;
        }

        /* access modifiers changed from: private */
        public int putLong(int i, long j) {
            if (!hasEnoughSpace(i, 8)) {
                return 0;
            }
            byte[] bArr = this.mBytes;
            bArr[i] = (byte) ((int) j);
            bArr[i + 1] = (byte) ((int) (j >> 8));
            bArr[i + 2] = (byte) ((int) (j >> 16));
            bArr[i + 3] = (byte) ((int) (j >> 24));
            bArr[i + 4] = (byte) ((int) (j >> 32));
            bArr[i + 5] = (byte) ((int) (j >> 40));
            bArr[i + 6] = (byte) ((int) (j >> 48));
            bArr[i + 7] = (byte) ((int) (j >> 56));
            return 8;
        }

        /* access modifiers changed from: private */
        public int putFloat(int i, float f) {
            return putInt(i, Float.floatToIntBits(f));
        }

        /* access modifiers changed from: private */
        public int putByteArray(int i, byte[] bArr) {
            int length = bArr.length;
            if (!hasEnoughSpace(i, length)) {
                return 0;
            }
            System.arraycopy((Object) bArr, 0, (Object) this.mBytes, i, length);
            return length;
        }
    }
}
