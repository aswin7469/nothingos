package libcore.p030io;

import android.annotation.SystemApi;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import java.nio.ByteOrder;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
/* renamed from: libcore.io.Memory */
public final class Memory {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static native void memmove(Object obj, int i, Object obj2, int i2, long j);

    public static native byte peekByte(long j);

    public static native void peekByteArray(long j, byte[] bArr, int i, int i2);

    public static native void peekCharArray(long j, char[] cArr, int i, int i2, boolean z);

    public static native void peekDoubleArray(long j, double[] dArr, int i, int i2, boolean z);

    public static native void peekFloatArray(long j, float[] fArr, int i, int i2, boolean z);

    public static native void peekIntArray(long j, int[] iArr, int i, int i2, boolean z);

    private static native int peekIntNative(long j);

    public static native void peekLongArray(long j, long[] jArr, int i, int i2, boolean z);

    private static native long peekLongNative(long j);

    public static native void peekShortArray(long j, short[] sArr, int i, int i2, boolean z);

    private static native short peekShortNative(long j);

    public static native void pokeByte(long j, byte b);

    public static native void pokeByteArray(long j, byte[] bArr, int i, int i2);

    public static native void pokeCharArray(long j, char[] cArr, int i, int i2, boolean z);

    public static native void pokeDoubleArray(long j, double[] dArr, int i, int i2, boolean z);

    public static native void pokeFloatArray(long j, float[] fArr, int i, int i2, boolean z);

    public static native void pokeIntArray(long j, int[] iArr, int i, int i2, boolean z);

    private static native void pokeIntNative(long j, int i);

    public static native void pokeLongArray(long j, long[] jArr, int i, int i2, boolean z);

    private static native void pokeLongNative(long j, long j2);

    public static native void pokeShortArray(long j, short[] sArr, int i, int i2, boolean z);

    private static native void pokeShortNative(long j, short s);

    public static native void unsafeBulkGet(Object obj, int i, int i2, byte[] bArr, int i3, int i4, boolean z);

    public static native void unsafeBulkPut(byte[] bArr, int i, int i2, Object obj, int i3, int i4, boolean z);

    private Memory() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
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

    public static long peekLong(byte[] bArr, int i, ByteOrder byteOrder) {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            int i2 = i + 1;
            int i3 = i2 + 1;
            int i4 = ((bArr[i] & 255) << 24) | ((bArr[i2] & 255) << 16);
            int i5 = i3 + 1;
            int i6 = i4 | ((bArr[i3] & 255) << 8);
            int i7 = i5 + 1;
            int i8 = i6 | ((bArr[i5] & 255) << 0);
            int i9 = i7 + 1;
            int i10 = i9 + 1;
            int i11 = ((bArr[i9] & 255) << 16) | ((bArr[i7] & 255) << 24);
            return (((long) i8) << 32) | (((long) (((bArr[i10 + 1] & 255) << 0) | i11 | ((bArr[i10] & 255) << 8))) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
        }
        int i12 = i + 1;
        int i13 = i12 + 1;
        int i14 = ((bArr[i] & 255) << 0) | ((bArr[i12] & 255) << 8);
        int i15 = i13 + 1;
        int i16 = i14 | ((bArr[i13] & 255) << 16);
        int i17 = i15 + 1;
        int i18 = i16 | ((bArr[i15] & 255) << 24);
        int i19 = i17 + 1;
        int i20 = i19 + 1;
        int i21 = ((bArr[i19] & 255) << 8) | ((bArr[i17] & 255) << 0);
        int i22 = (bArr[i20 + 1] & 255) << 24;
        return (((long) i18) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) | (((long) (i22 | (i21 | ((bArr[i20] & 255) << 16)))) << 32);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
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

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
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

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void pokeLong(byte[] bArr, int i, long j, ByteOrder byteOrder) {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            int i2 = (int) (j >> 32);
            int i3 = i + 1;
            bArr[i] = (byte) ((i2 >> 24) & 255);
            int i4 = i3 + 1;
            bArr[i3] = (byte) ((i2 >> 16) & 255);
            int i5 = i4 + 1;
            bArr[i4] = (byte) ((i2 >> 8) & 255);
            int i6 = i5 + 1;
            bArr[i5] = (byte) ((i2 >> 0) & 255);
            int i7 = (int) j;
            int i8 = i6 + 1;
            bArr[i6] = (byte) ((i7 >> 24) & 255);
            int i9 = i8 + 1;
            bArr[i8] = (byte) ((i7 >> 16) & 255);
            bArr[i9] = (byte) ((i7 >> 8) & 255);
            bArr[i9 + 1] = (byte) ((i7 >> 0) & 255);
            return;
        }
        int i10 = (int) j;
        int i11 = i + 1;
        bArr[i] = (byte) ((i10 >> 0) & 255);
        int i12 = i11 + 1;
        bArr[i11] = (byte) ((i10 >> 8) & 255);
        int i13 = i12 + 1;
        bArr[i12] = (byte) ((i10 >> 16) & 255);
        int i14 = i13 + 1;
        bArr[i13] = (byte) ((i10 >> 24) & 255);
        int i15 = (int) (j >> 32);
        int i16 = i14 + 1;
        bArr[i14] = (byte) ((i15 >> 0) & 255);
        int i17 = i16 + 1;
        bArr[i16] = (byte) ((i15 >> 8) & 255);
        bArr[i17] = (byte) ((i15 >> 16) & 255);
        bArr[i17 + 1] = (byte) ((i15 >> 24) & 255);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void pokeShort(byte[] bArr, int i, short s, ByteOrder byteOrder) {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            bArr[i] = (byte) ((s >> 8) & 255);
            bArr[i + 1] = (byte) ((s >> 0) & 255);
            return;
        }
        bArr[i] = (byte) ((s >> 0) & 255);
        bArr[i + 1] = (byte) ((s >> 8) & 255);
    }

    public static int peekInt(long j, boolean z) {
        int peekIntNative = peekIntNative(j);
        return z ? Integer.reverseBytes(peekIntNative) : peekIntNative;
    }

    public static long peekLong(long j, boolean z) {
        long peekLongNative = peekLongNative(j);
        return z ? Long.reverseBytes(peekLongNative) : peekLongNative;
    }

    public static short peekShort(long j, boolean z) {
        short peekShortNative = peekShortNative(j);
        return z ? Short.reverseBytes(peekShortNative) : peekShortNative;
    }

    public static void pokeInt(long j, int i, boolean z) {
        if (z) {
            i = Integer.reverseBytes(i);
        }
        pokeIntNative(j, i);
    }

    public static void pokeLong(long j, long j2, boolean z) {
        if (z) {
            j2 = Long.reverseBytes(j2);
        }
        pokeLongNative(j, j2);
    }

    public static void pokeShort(long j, short s, boolean z) {
        if (z) {
            s = Short.reverseBytes(s);
        }
        pokeShortNative(j, s);
    }
}
