package javax.crypto;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public abstract class CipherSpi {
    /* access modifiers changed from: protected */
    public abstract int engineDoFinal(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException;

    /* access modifiers changed from: protected */
    public abstract byte[] engineDoFinal(byte[] bArr, int i, int i2) throws IllegalBlockSizeException, BadPaddingException;

    /* access modifiers changed from: protected */
    public abstract int engineGetBlockSize();

    /* access modifiers changed from: protected */
    public abstract byte[] engineGetIV();

    /* access modifiers changed from: protected */
    public abstract int engineGetOutputSize(int i);

    /* access modifiers changed from: protected */
    public abstract AlgorithmParameters engineGetParameters();

    /* access modifiers changed from: protected */
    public abstract void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException;

    /* access modifiers changed from: protected */
    public abstract void engineInit(int i, Key key, SecureRandom secureRandom) throws InvalidKeyException;

    /* access modifiers changed from: protected */
    public abstract void engineInit(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException;

    /* access modifiers changed from: protected */
    public abstract void engineSetMode(String str) throws NoSuchAlgorithmException;

    /* access modifiers changed from: protected */
    public abstract void engineSetPadding(String str) throws NoSuchPaddingException;

    /* access modifiers changed from: protected */
    public abstract int engineUpdate(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws ShortBufferException;

    /* access modifiers changed from: protected */
    public abstract byte[] engineUpdate(byte[] bArr, int i, int i2);

    /* access modifiers changed from: protected */
    public int engineUpdate(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException {
        try {
            return bufferCrypt(byteBuffer, byteBuffer2, true);
        } catch (IllegalBlockSizeException unused) {
            throw new ProviderException("Internal error in update()");
        } catch (BadPaddingException unused2) {
            throw new ProviderException("Internal error in update()");
        }
    }

    /* access modifiers changed from: protected */
    public int engineDoFinal(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return bufferCrypt(byteBuffer, byteBuffer2, false);
    }

    static int getTempArraySize(int i) {
        return Math.min(4096, i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:62:0x0127  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0137  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x013b A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int bufferCrypt(java.nio.ByteBuffer r22, java.nio.ByteBuffer r23, boolean r24) throws javax.crypto.ShortBufferException, javax.crypto.IllegalBlockSizeException, javax.crypto.BadPaddingException {
        /*
            r21 = this;
            r7 = r21
            r8 = r22
            r9 = r23
            if (r8 == 0) goto L_0x0162
            if (r9 == 0) goto L_0x0162
            int r0 = r22.position()
            int r10 = r22.limit()
            int r4 = r10 - r0
            r11 = 0
            if (r24 == 0) goto L_0x001a
            if (r4 != 0) goto L_0x001a
            return r11
        L_0x001a:
            int r1 = r7.engineGetOutputSize(r4)
            int r2 = r23.remaining()
            if (r2 < r1) goto L_0x0149
            boolean r12 = r22.hasArray()
            boolean r2 = r23.hasArray()
            if (r12 == 0) goto L_0x005f
            if (r2 == 0) goto L_0x005f
            byte[] r2 = r22.array()
            int r1 = r22.arrayOffset()
            int r3 = r1 + r0
            byte[] r5 = r23.array()
            int r0 = r23.position()
            int r1 = r23.arrayOffset()
            int r6 = r1 + r0
            if (r24 == 0) goto L_0x0051
            r1 = r21
            int r1 = r1.engineUpdate(r2, r3, r4, r5, r6)
            goto L_0x0057
        L_0x0051:
            r1 = r21
            int r1 = r1.engineDoFinal(r2, r3, r4, r5, r6)
        L_0x0057:
            r8.position((int) r10)
            int r0 = r0 + r1
            r9.position((int) r0)
            return r1
        L_0x005f:
            if (r12 != 0) goto L_0x00b0
            if (r2 == 0) goto L_0x00b0
            int r13 = r23.position()
            byte[] r14 = r23.array()
            int r0 = r23.arrayOffset()
            int r0 = r0 + r13
            int r15 = getTempArraySize(r4)
            byte[] r10 = new byte[r15]
            r12 = r4
            r16 = r11
        L_0x0079:
            int r6 = java.lang.Math.min((int) r12, (int) r15)
            if (r6 <= 0) goto L_0x0082
            r8.get(r10, r11, r6)
        L_0x0082:
            if (r24 != 0) goto L_0x0095
            if (r12 == r6) goto L_0x0087
            goto L_0x0095
        L_0x0087:
            r3 = 0
            r1 = r21
            r2 = r10
            r4 = r6
            r5 = r14
            r17 = r6
            r6 = r0
            int r1 = r1.engineDoFinal(r2, r3, r4, r5, r6)
            goto L_0x00a3
        L_0x0095:
            r17 = r6
            r3 = 0
            r1 = r21
            r2 = r10
            r4 = r17
            r5 = r14
            r6 = r0
            int r1 = r1.engineUpdate(r2, r3, r4, r5, r6)
        L_0x00a3:
            int r16 = r16 + r1
            int r0 = r0 + r1
            int r12 = r12 - r17
            if (r12 > 0) goto L_0x0079
            int r13 = r13 + r16
            r9.position((int) r13)
            return r16
        L_0x00b0:
            if (r12 == 0) goto L_0x00bd
            byte[] r2 = r22.array()
            int r3 = r22.arrayOffset()
            int r3 = r3 + r0
            r13 = r2
            goto L_0x00c5
        L_0x00bd:
            int r0 = getTempArraySize(r4)
            byte[] r2 = new byte[r0]
            r13 = r2
            r3 = r11
        L_0x00c5:
            int r0 = getTempArraySize(r1)
            byte[] r1 = new byte[r0]
            r14 = r1
            r15 = r4
            r16 = r11
            r17 = r16
        L_0x00d1:
            if (r0 != 0) goto L_0x00d5
            int r1 = r13.length
            goto L_0x00d6
        L_0x00d5:
            r1 = r0
        L_0x00d6:
            int r6 = java.lang.Math.min((int) r15, (int) r1)
            if (r12 != 0) goto L_0x00e6
            if (r16 != 0) goto L_0x00e6
            if (r6 <= 0) goto L_0x00e6
            r8.get(r13, r11, r6)
            r18 = r11
            goto L_0x00e8
        L_0x00e6:
            r18 = r3
        L_0x00e8:
            if (r24 != 0) goto L_0x00ff
            if (r15 == r6) goto L_0x00ed
            goto L_0x00ff
        L_0x00ed:
            r19 = 0
            r1 = r21
            r2 = r13
            r3 = r18
            r4 = r6
            r5 = r14
            r20 = r6
            r6 = r19
            int r1 = r1.engineDoFinal(r2, r3, r4, r5, r6)     // Catch:{ ShortBufferException -> 0x0122 }
            goto L_0x010e
        L_0x00ff:
            r20 = r6
            r6 = 0
            r1 = r21
            r2 = r13
            r3 = r18
            r4 = r20
            r5 = r14
            int r1 = r1.engineUpdate(r2, r3, r4, r5, r6)     // Catch:{ ShortBufferException -> 0x0122 }
        L_0x010e:
            r2 = r20
            int r18 = r18 + r2
            int r15 = r15 - r2
            if (r1 <= 0) goto L_0x011f
            r9.put(r14, r11, r1)     // Catch:{ ShortBufferException -> 0x011b }
            int r17 = r17 + r1
            goto L_0x011f
        L_0x011b:
            r0 = move-exception
            r16 = r11
            goto L_0x0125
        L_0x011f:
            r16 = r11
            goto L_0x0131
        L_0x0122:
            r0 = move-exception
            r2 = r20
        L_0x0125:
            if (r16 != 0) goto L_0x013b
            int r0 = r7.engineGetOutputSize(r2)
            byte[] r1 = new byte[r0]
            r2 = 1
            r14 = r1
            r16 = r2
        L_0x0131:
            r3 = r18
            if (r15 > 0) goto L_0x00d1
            if (r12 == 0) goto L_0x013a
            r8.position((int) r10)
        L_0x013a:
            return r17
        L_0x013b:
            java.security.ProviderException r1 = new java.security.ProviderException
            java.lang.String r2 = "Could not determine buffer size"
            r1.<init>((java.lang.String) r2)
            java.lang.Throwable r0 = r1.initCause(r0)
            java.security.ProviderException r0 = (java.security.ProviderException) r0
            throw r0
        L_0x0149:
            javax.crypto.ShortBufferException r0 = new javax.crypto.ShortBufferException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Need at least "
            r2.<init>((java.lang.String) r3)
            r2.append((int) r1)
            java.lang.String r1 = " bytes of space in output buffer"
            r2.append((java.lang.String) r1)
            java.lang.String r1 = r2.toString()
            r0.<init>(r1)
            throw r0
        L_0x0162:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Input and output buffers must not be null"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.crypto.CipherSpi.bufferCrypt(java.nio.ByteBuffer, java.nio.ByteBuffer, boolean):int");
    }

    /* access modifiers changed from: protected */
    public byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public Key engineUnwrap(byte[] bArr, String str, int i) throws InvalidKeyException, NoSuchAlgorithmException {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public int engineGetKeySize(Key key) throws InvalidKeyException {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public void engineUpdateAAD(byte[] bArr, int i, int i2) {
        throw new UnsupportedOperationException("The underlying Cipher implementation does not support this method");
    }

    /* access modifiers changed from: protected */
    public void engineUpdateAAD(ByteBuffer byteBuffer) {
        throw new UnsupportedOperationException("The underlying Cipher implementation does not support this method");
    }
}
