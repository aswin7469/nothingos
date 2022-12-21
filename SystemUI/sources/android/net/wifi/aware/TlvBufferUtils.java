package android.net.wifi.aware;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.BufferOverflowException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TlvBufferUtils {
    private TlvBufferUtils() {
    }

    public static class TlvConstructor {
        private byte[] mArray;
        private int mArrayLength;
        private ByteOrder mByteOrder = ByteOrder.BIG_ENDIAN;
        private int mLengthSize;
        private int mPosition;
        private int mTypeSize;

        public TlvConstructor(int i, int i2) {
            if (i < 0 || i > 2 || i2 <= 0 || i2 > 2) {
                throw new IllegalArgumentException("Invalid sizes - typeSize=" + i + ", lengthSize=" + i2);
            }
            this.mTypeSize = i;
            this.mLengthSize = i2;
            this.mPosition = 0;
        }

        public TlvConstructor setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
            return this;
        }

        public TlvConstructor wrap(byte[] bArr) {
            int i;
            this.mArray = bArr;
            if (bArr == null) {
                i = 0;
            } else {
                i = bArr.length;
            }
            this.mArrayLength = i;
            this.mPosition = 0;
            return this;
        }

        public TlvConstructor allocate(int i) {
            this.mArray = new byte[i];
            this.mArrayLength = i;
            this.mPosition = 0;
            return this;
        }

        public TlvConstructor allocateAndPut(List<byte[]> list) {
            if (list != null) {
                int i = 0;
                for (byte[] next : list) {
                    i += this.mTypeSize + this.mLengthSize;
                    if (next != null) {
                        i += next.length;
                    }
                }
                allocate(i);
                for (byte[] putByteArray : list) {
                    putByteArray(0, putByteArray);
                }
            }
            return this;
        }

        public TlvConstructor putByte(int i, byte b) {
            checkLength(1);
            addHeader(i, 1);
            byte[] bArr = this.mArray;
            int i2 = this.mPosition;
            this.mPosition = i2 + 1;
            bArr[i2] = b;
            return this;
        }

        public TlvConstructor putRawByte(byte b) {
            checkRawLength(1);
            byte[] bArr = this.mArray;
            int i = this.mPosition;
            this.mPosition = i + 1;
            bArr[i] = b;
            return this;
        }

        public TlvConstructor putByteArray(int i, byte[] bArr, int i2, int i3) {
            checkLength(i3);
            addHeader(i, i3);
            if (i3 != 0) {
                System.arraycopy((Object) bArr, i2, (Object) this.mArray, this.mPosition, i3);
            }
            this.mPosition += i3;
            return this;
        }

        public TlvConstructor putByteArray(int i, byte[] bArr) {
            return putByteArray(i, bArr, 0, bArr == null ? 0 : bArr.length);
        }

        public TlvConstructor putRawByteArray(byte[] bArr) {
            if (bArr == null) {
                return this;
            }
            checkRawLength(bArr.length);
            System.arraycopy((Object) bArr, 0, (Object) this.mArray, this.mPosition, bArr.length);
            this.mPosition += bArr.length;
            return this;
        }

        public TlvConstructor putZeroLengthElement(int i) {
            checkLength(0);
            addHeader(i, 0);
            return this;
        }

        public TlvConstructor putShort(int i, short s) {
            checkLength(2);
            addHeader(i, 2);
            TlvBufferUtils.pokeShort(this.mArray, this.mPosition, s, this.mByteOrder);
            this.mPosition += 2;
            return this;
        }

        public TlvConstructor putInt(int i, int i2) {
            checkLength(4);
            addHeader(i, 4);
            TlvBufferUtils.pokeInt(this.mArray, this.mPosition, i2, this.mByteOrder);
            this.mPosition += 4;
            return this;
        }

        public TlvConstructor putString(int i, String str) {
            byte[] bArr;
            int i2;
            if (str != null) {
                bArr = str.getBytes();
                i2 = bArr.length;
            } else {
                bArr = null;
                i2 = 0;
            }
            return putByteArray(i, bArr, 0, i2);
        }

        public byte[] getArray() {
            return Arrays.copyOf(this.mArray, getActualLength());
        }

        private int getActualLength() {
            return this.mPosition;
        }

        private void checkLength(int i) {
            if (this.mPosition + this.mTypeSize + this.mLengthSize + i > this.mArrayLength) {
                throw new BufferOverflowException();
            }
        }

        private void checkRawLength(int i) {
            if (this.mPosition + i > this.mArrayLength) {
                throw new BufferOverflowException();
            }
        }

        private void addHeader(int i, int i2) {
            int i3 = this.mTypeSize;
            if (i3 == 1) {
                this.mArray[this.mPosition] = (byte) i;
            } else if (i3 == 2) {
                TlvBufferUtils.pokeShort(this.mArray, this.mPosition, (short) i, this.mByteOrder);
            }
            int i4 = this.mPosition + this.mTypeSize;
            this.mPosition = i4;
            int i5 = this.mLengthSize;
            if (i5 == 1) {
                this.mArray[i4] = (byte) i2;
            } else if (i5 == 2) {
                TlvBufferUtils.pokeShort(this.mArray, i4, (short) i2, this.mByteOrder);
            }
            this.mPosition += this.mLengthSize;
        }
    }

    public static class TlvElement {
        public ByteOrder byteOrder;
        public int length;
        /* access modifiers changed from: private */
        public byte[] mRefArray;
        public int offset;
        public int type;

        private TlvElement(int i, int i2, byte[] bArr, int i3) {
            this.byteOrder = ByteOrder.BIG_ENDIAN;
            this.type = i;
            this.length = i2;
            this.mRefArray = bArr;
            this.offset = i3;
            if (i3 + i2 > bArr.length) {
                throw new BufferOverflowException();
            }
        }

        public byte[] getRawData() {
            byte[] bArr = this.mRefArray;
            int i = this.offset;
            return Arrays.copyOfRange(bArr, i, this.length + i);
        }

        public byte getByte() {
            if (this.length == 1) {
                return this.mRefArray[this.offset];
            }
            throw new IllegalArgumentException("Accesing a byte from a TLV element of length " + this.length);
        }

        public short getShort() {
            if (this.length == 2) {
                return TlvBufferUtils.peekShort(this.mRefArray, this.offset, this.byteOrder);
            }
            throw new IllegalArgumentException("Accesing a short from a TLV element of length " + this.length);
        }

        public int getInt() {
            if (this.length == 4) {
                return TlvBufferUtils.peekInt(this.mRefArray, this.offset, this.byteOrder);
            }
            throw new IllegalArgumentException("Accesing an int from a TLV element of length " + this.length);
        }

        public String getString() {
            return new String(this.mRefArray, this.offset, this.length);
        }
    }

    public static class TlvIterable implements Iterable<TlvElement> {
        /* access modifiers changed from: private */
        public byte[] mArray;
        /* access modifiers changed from: private */
        public int mArrayLength;
        /* access modifiers changed from: private */
        public ByteOrder mByteOrder = ByteOrder.BIG_ENDIAN;
        /* access modifiers changed from: private */
        public int mLengthSize;
        /* access modifiers changed from: private */
        public int mTypeSize;

        public TlvIterable(int i, int i2, byte[] bArr) {
            int i3;
            if (i < 0 || i > 2 || i2 <= 0 || i2 > 2) {
                throw new IllegalArgumentException("Invalid sizes - typeSize=" + i + ", lengthSize=" + i2);
            }
            this.mTypeSize = i;
            this.mLengthSize = i2;
            this.mArray = bArr;
            if (bArr == null) {
                i3 = 0;
            } else {
                i3 = bArr.length;
            }
            this.mArrayLength = i3;
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
            Iterator<TlvElement> it = iterator();
            boolean z = true;
            while (it.hasNext()) {
                TlvElement next = it.next();
                if (!z) {
                    sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
                }
                sb.append(" (");
                if (this.mTypeSize != 0) {
                    sb.append("T=" + next.type + NavigationBarInflaterView.BUTTON_SEPARATOR);
                }
                sb.append("L=" + next.length + ") ");
                if (next.length == 0) {
                    sb.append("<null>");
                } else if (next.length == 1) {
                    sb.append((int) next.getByte());
                } else if (next.length == 2) {
                    sb.append((int) next.getShort());
                } else if (next.length == 4) {
                    sb.append(next.getInt());
                } else {
                    sb.append("<bytes>");
                }
                if (next.length != 0) {
                    sb.append(" (S='" + next.getString() + "')");
                }
                z = false;
            }
            sb.append(NavigationBarInflaterView.SIZE_MOD_END);
            return sb.toString();
        }

        public List<byte[]> toList() {
            ArrayList arrayList = new ArrayList();
            Iterator<TlvElement> it = iterator();
            while (it.hasNext()) {
                TlvElement next = it.next();
                arrayList.add(Arrays.copyOfRange(next.mRefArray, next.offset, next.offset + next.length));
            }
            return arrayList;
        }

        public Iterator<TlvElement> iterator() {
            return new Iterator<TlvElement>() {
                private int mOffset = 0;

                public boolean hasNext() {
                    return this.mOffset < TlvIterable.this.mArrayLength;
                }

                /* JADX WARNING: type inference failed for: r0v15, types: [byte[]] */
                /* JADX WARNING: type inference failed for: r3v3, types: [byte] */
                /* JADX WARNING: type inference failed for: r0v23, types: [byte[]] */
                /* JADX WARNING: type inference failed for: r0v24, types: [byte] */
                /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r0v24, types: [byte] */
                /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r3v3, types: [byte] */
                /* JADX WARNING: Removed duplicated region for block: B:12:0x004c  */
                /* JADX WARNING: Removed duplicated region for block: B:13:0x0057  */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public android.net.wifi.aware.TlvBufferUtils.TlvElement next() {
                    /*
                        r10 = this;
                        boolean r0 = r10.hasNext()
                        if (r0 == 0) goto L_0x009a
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r0 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        int r0 = r0.mTypeSize
                        r1 = 2
                        r2 = 1
                        r3 = 0
                        if (r0 != r2) goto L_0x001d
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r0 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        byte[] r0 = r0.mArray
                        int r4 = r10.mOffset
                        byte r0 = r0[r4]
                    L_0x001b:
                        r5 = r0
                        goto L_0x0039
                    L_0x001d:
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r0 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        int r0 = r0.mTypeSize
                        if (r0 != r1) goto L_0x0038
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r0 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        byte[] r0 = r0.mArray
                        int r4 = r10.mOffset
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r5 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        java.nio.ByteOrder r5 = r5.mByteOrder
                        short r0 = android.net.wifi.aware.TlvBufferUtils.peekShort(r0, r4, r5)
                        goto L_0x001b
                    L_0x0038:
                        r5 = r3
                    L_0x0039:
                        int r0 = r10.mOffset
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r4 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        int r4 = r4.mTypeSize
                        int r0 = r0 + r4
                        r10.mOffset = r0
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r0 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        int r0 = r0.mLengthSize
                        if (r0 != r2) goto L_0x0057
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r0 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        byte[] r0 = r0.mArray
                        int r1 = r10.mOffset
                        byte r3 = r0[r1]
                        goto L_0x0071
                    L_0x0057:
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r0 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        int r0 = r0.mLengthSize
                        if (r0 != r1) goto L_0x0071
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r0 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        byte[] r0 = r0.mArray
                        int r1 = r10.mOffset
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r2 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        java.nio.ByteOrder r2 = r2.mByteOrder
                        short r3 = android.net.wifi.aware.TlvBufferUtils.peekShort(r0, r1, r2)
                    L_0x0071:
                        int r0 = r10.mOffset
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r1 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        int r1 = r1.mLengthSize
                        int r0 = r0 + r1
                        r10.mOffset = r0
                        android.net.wifi.aware.TlvBufferUtils$TlvElement r0 = new android.net.wifi.aware.TlvBufferUtils$TlvElement
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r1 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        byte[] r7 = r1.mArray
                        int r8 = r10.mOffset
                        r9 = 0
                        r4 = r0
                        r6 = r3
                        r4.<init>(r5, r6, r7, r8)
                        android.net.wifi.aware.TlvBufferUtils$TlvIterable r1 = android.net.wifi.aware.TlvBufferUtils.TlvIterable.this
                        java.nio.ByteOrder r1 = r1.mByteOrder
                        r0.byteOrder = r1
                        int r1 = r10.mOffset
                        int r1 = r1 + r3
                        r10.mOffset = r1
                        return r0
                    L_0x009a:
                        java.util.NoSuchElementException r10 = new java.util.NoSuchElementException
                        r10.<init>()
                        throw r10
                    */
                    throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.aware.TlvBufferUtils.TlvIterable.C02181.next():android.net.wifi.aware.TlvBufferUtils$TlvElement");
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    public static boolean isValid(byte[] bArr, int i, int i2) {
        return isValidEndian(bArr, i, i2, ByteOrder.BIG_ENDIAN);
    }

    /* JADX WARNING: type inference failed for: r6v0, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r2v6, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r2v6, types: [byte] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isValidEndian(byte[] r6, int r7, int r8, java.nio.ByteOrder r9) {
        /*
            if (r7 < 0) goto L_0x003c
            r0 = 2
            if (r7 > r0) goto L_0x003c
            if (r8 <= 0) goto L_0x0028
            if (r8 > r0) goto L_0x0028
            r0 = 1
            if (r6 != 0) goto L_0x000d
            return r0
        L_0x000d:
            r1 = 0
            r2 = r1
        L_0x000f:
            int r3 = r2 + r7
            int r4 = r3 + r8
            int r5 = r6.length
            if (r4 > r5) goto L_0x0022
            if (r8 != r0) goto L_0x001b
            byte r2 = r6[r3]
            goto L_0x001f
        L_0x001b:
            short r2 = peekShort(r6, r3, r9)
        L_0x001f:
            int r2 = r2 + r8
            int r2 = r2 + r3
            goto L_0x000f
        L_0x0022:
            int r6 = r6.length
            if (r2 != r6) goto L_0x0026
            goto L_0x0027
        L_0x0026:
            r0 = r1
        L_0x0027:
            return r0
        L_0x0028:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r9 = "Invalid arguments - lengthSize must be 1 or 2: lengthSize="
            r7.<init>((java.lang.String) r9)
            r7.append((int) r8)
            java.lang.String r7 = r7.toString()
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x003c:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "Invalid arguments - typeSize must be 0, 1, or 2: typeSize="
            r8.<init>((java.lang.String) r9)
            r8.append((int) r7)
            java.lang.String r7 = r8.toString()
            r6.<init>((java.lang.String) r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.aware.TlvBufferUtils.isValidEndian(byte[], int, int, java.nio.ByteOrder):boolean");
    }

    /* access modifiers changed from: private */
    public static void pokeShort(byte[] bArr, int i, short s, ByteOrder byteOrder) {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            bArr[i] = (byte) ((s >> 8) & 255);
            bArr[i + 1] = (byte) ((s >> 0) & 255);
            return;
        }
        bArr[i] = (byte) ((s >> 0) & 255);
        bArr[i + 1] = (byte) ((s >> 8) & 255);
    }

    /* access modifiers changed from: private */
    public static void pokeInt(byte[] bArr, int i, int i2, ByteOrder byteOrder) {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            int i3 = i + 1;
            bArr[i] = (byte) ((i2 >> 24) & 255);
            int i4 = i3 + 1;
            bArr[i3] = (byte) ((i2 >> 16) & 255);
            bArr[i4] = (byte) ((i2 >> 8) & 255);
            bArr[i4 + 1] = (byte) ((i2 >> 0) & 255);
            return;
        }
        int i5 = i + 1;
        bArr[i] = (byte) ((i2 >> 0) & 255);
        int i6 = i5 + 1;
        bArr[i5] = (byte) ((i2 >> 8) & 255);
        bArr[i6] = (byte) ((i2 >> 16) & 255);
        bArr[i6 + 1] = (byte) ((i2 >> 24) & 255);
    }

    /* access modifiers changed from: private */
    public static short peekShort(byte[] bArr, int i, ByteOrder byteOrder) {
        int i2;
        byte b;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            i2 = bArr[i] << 8;
            b = bArr[i + 1];
        } else {
            i2 = bArr[i + 1] << 8;
            b = bArr[i];
        }
        return (short) ((b & 255) | i2);
    }

    /* access modifiers changed from: private */
    public static int peekInt(byte[] bArr, int i, ByteOrder byteOrder) {
        int i2;
        int i3;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            int i4 = i + 1;
            int i5 = i4 + 1;
            int i6 = ((bArr[i] & 255) << 24) | ((bArr[i4] & 255) << 16);
            i2 = i6 | ((bArr[i5] & 255) << 8);
            i3 = (bArr[i5 + 1] & 255) << 0;
        } else {
            int i7 = i + 1;
            int i8 = i7 + 1;
            int i9 = ((bArr[i] & 255) << 0) | ((bArr[i7] & 255) << 8);
            i2 = i9 | ((bArr[i8] & 255) << 16);
            i3 = (bArr[i8 + 1] & 255) << 24;
        }
        return i3 | i2;
    }
}
