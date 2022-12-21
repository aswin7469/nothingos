package sun.nio.p035fs;

import sun.misc.Unsafe;

/* renamed from: sun.nio.fs.NativeBuffers */
class NativeBuffers {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int TEMP_BUF_POOL_SIZE = 3;
    private static ThreadLocal<NativeBuffer[]> threadLocal = new ThreadLocal<>();
    private static final Unsafe unsafe = Unsafe.getUnsafe();

    private NativeBuffers() {
    }

    static NativeBuffer allocNativeBuffer(int i) {
        if (i < 2048) {
            i = 2048;
        }
        return new NativeBuffer(i);
    }

    static NativeBuffer getNativeBufferFromCache(int i) {
        NativeBuffer[] nativeBufferArr = threadLocal.get();
        if (nativeBufferArr != null) {
            int i2 = 0;
            while (i2 < 3) {
                NativeBuffer nativeBuffer = nativeBufferArr[i2];
                if (nativeBuffer == null || nativeBuffer.size() < i) {
                    i2++;
                } else {
                    nativeBufferArr[i2] = null;
                    return nativeBuffer;
                }
            }
        }
        return null;
    }

    static NativeBuffer getNativeBuffer(int i) {
        NativeBuffer nativeBufferFromCache = getNativeBufferFromCache(i);
        if (nativeBufferFromCache == null) {
            return allocNativeBuffer(i);
        }
        nativeBufferFromCache.setOwner((Object) null);
        return nativeBufferFromCache;
    }

    static void releaseNativeBuffer(NativeBuffer nativeBuffer) {
        NativeBuffer[] nativeBufferArr = threadLocal.get();
        if (nativeBufferArr == null) {
            NativeBuffer[] nativeBufferArr2 = new NativeBuffer[3];
            nativeBufferArr2[0] = nativeBuffer;
            threadLocal.set(nativeBufferArr2);
            return;
        }
        for (int i = 0; i < 3; i++) {
            if (nativeBufferArr[i] == null) {
                nativeBufferArr[i] = nativeBuffer;
                return;
            }
        }
        for (int i2 = 0; i2 < 3; i2++) {
            NativeBuffer nativeBuffer2 = nativeBufferArr[i2];
            if (nativeBuffer2.size() < nativeBuffer.size()) {
                nativeBuffer2.cleaner().clean();
                nativeBufferArr[i2] = nativeBuffer;
                return;
            }
        }
        nativeBuffer.cleaner().clean();
    }

    static void copyCStringToNativeBuffer(byte[] bArr, NativeBuffer nativeBuffer) {
        long length = (long) bArr.length;
        int i = 0;
        while (true) {
            long j = (long) i;
            if (j < length) {
                unsafe.putByte(nativeBuffer.address() + j, bArr[i]);
                i++;
            } else {
                unsafe.putByte(nativeBuffer.address() + length, (byte) 0);
                return;
            }
        }
    }

    static NativeBuffer asNativeBuffer(byte[] bArr) {
        NativeBuffer nativeBuffer = getNativeBuffer(bArr.length + 1);
        copyCStringToNativeBuffer(bArr, nativeBuffer);
        return nativeBuffer;
    }
}
